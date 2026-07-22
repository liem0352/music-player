package com.example.demo.tools;
import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.io.*;
/**
 * EncryptionTool - 密码加密与验证工具类
 *
 * <p>采用 RSA 非对称加密 + BCrypt 哈希的双重加密策略：</p>
 * <ol>
 *   <li>BCrypt 对明文密码进行哈希（自带随机盐）</li>
 *   <li>RSA 公钥对 BCrypt 哈希值进行非对称加密</li>
 *   <li>最终密文以 Base64 编码存储到数据库</li>
 * </ol>
 *
 * <p>验证时逆向操作：RSA 解密 → BCrypt matches 比对。</p>
 *
 * <p>修复说明：decrypt 方法增加异常处理，兼容纯 BCrypt 格式的旧密码。</p>
 *
 * @author liem
 * @version 1.1.0
 * @since 2022-09-28
 */
@Component
@Slf4j
public class EncryptionTool {
    /** RSA 私钥文件路径（从环境变量读取） */
    @Value("${private.key.url}")
    private String privateKeyUrl;
    /** RSA 公钥文件路径（从环境变量读取） */
    @Value("${public.key.url}")
    private String publicKeyUrl;
    /** BCrypt 密码编码器（Spring Security 提供） */
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    /** RSA 非对称加密实例（延迟初始化） */
    private RSA rsa;

    /**
     * 从文件系统读取 RSA 公钥与私钥
     *
     * @return String[0]=私钥, String[1]=公钥
     */
    public String[] getKey(){
        String[] strings = new String[2];
        try(
                BufferedReader readerPri = new BufferedReader(new FileReader(privateKeyUrl));
                BufferedReader readerPub = new BufferedReader(new FileReader(publicKeyUrl))
           ){
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = readerPri.readLine()) != null) {
                sb.append(readLine);
            }
            strings[0] = sb.toString();
            sb.delete(0,sb.length());
            while ((readLine = readerPub.readLine()) != null) {
                sb.append(readLine);
            }
            strings[1] = sb.toString();
        } catch (IOException e) {
            log.error("读取 RSA 密钥文件失败，请检查 private.key.url 和 public.key.url 配置", e);
        }
        return strings;
    }

    /**
     * 加密密码：BCrypt 哈希 → RSA 公钥加密 → Base64 编码
     *
     * @param password 明文密码
     * @return 加密后的密文
     */
    public String encrypt(String password){
        String[] key = getKey();
        rsa = new RSA(key[0],key[1]);
        String encode = bCryptPasswordEncoder.encode(password);
        byte[] decrypt = rsa.encrypt(encode, KeyType.PublicKey);
        return Base64.encode(decrypt);
    }

    /**
     * 验证密码：支持 RSA+BCrypt 双重加密格式和纯 BCrypt 格式
     *
     * <p>修复说明：增加异常处理逻辑。</p>
     * <ol>
     *   <li>若密文以 '$' 开头，说明是纯 BCrypt 哈希，直接用 matches 验证</li>
     *   <li>否则尝试 RSA 解密后再用 BCrypt matches 验证</li>
     *   <li>RSA 解密失败时回退为纯 BCrypt 验证，兼容旧数据</li>
     * </ol>
     *
     * @param nowPassword 用户输入的明文密码
     * @param oldPassword 数据库中存储的密文
     * @return 密码是否匹配
     */
    public boolean decrypt(String nowPassword, String oldPassword){
        // 纯 BCrypt 格式密码（以 $2a$、$2b$ 等开头），直接验证
        if (oldPassword != null && oldPassword.length() > 0 && oldPassword.charAt(0) == '$') {
            return bCryptPasswordEncoder.matches(nowPassword, oldPassword);
        }
        // RSA + BCrypt 双重加密格式，先解密再验证
        try {
            String[] key = getKey();
            rsa = new RSA(key[0], key[1]);
            String result = rsa.decryptStr(oldPassword, KeyType.PrivateKey);
            return bCryptPasswordEncoder.matches(nowPassword, result);
        } catch (Exception e) {
            log.warn("RSA 解密失败，尝试纯 BCrypt 验证", e);
            return bCryptPasswordEncoder.matches(nowPassword, oldPassword);
        }
    }
}
