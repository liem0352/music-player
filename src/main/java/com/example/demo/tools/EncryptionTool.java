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
 * Created with IntelliJ IDEA.
 * Description:
 * User: 19833
 * Date: 2022-09-28
 * Time: 18:35
 */
@Component
@Slf4j
public class EncryptionTool {

    @Value("${private.key.url}")
    private String privateKeyUrl;

    @Value("${public.key.url}")
    private String publicKeyUrl;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private RSA rsa;



    // 字符流读取 密钥
    public String[] getKey(){
        String[] strings = new String[2];
        try(
                BufferedReader readerPri = new BufferedReader(new FileReader(privateKeyUrl));
                BufferedReader readerPub = new BufferedReader(new FileReader(publicKeyUrl))
           ){

            String readLine = null;
            StringBuilder sb = new StringBuilder();
            // 每次读取一行
            while ((readLine = readerPri.readLine()) != null) {
                sb.append(readLine);
            }
            // 将读取的第一个 钥匙 放到 数组 中
            strings[0] = sb.toString();

            // 将临时变量 sb 清空,用来存储 另一把钥匙
            sb.delete(0,sb.length());
            // 每次读取一行
            while ((readLine = readerPub.readLine()) != null) {
                sb.append(readLine);
            }
            // 将读取的第一个 钥匙 放到 数组 中
            strings[1] = sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return strings;
    }



    // 加密
    public String encrypt(String password){
        // 获取 公钥和私钥
        String[] key = getKey();

        rsa = new RSA(key[0],key[1]);
        // 通过 BCrypt (md5 + 随机加盐) 进行加密
        String encode = bCryptPasswordEncoder.encode(password);
        // 通过 非对称加密 再次包裹一层
        byte[] decrypt = rsa.encrypt(encode, KeyType.PublicKey);
        // 返回 密文
        return Base64.encode(decrypt);
    }



    // 解密
    public boolean decrypt(String nowPassword,String oldPassword){
        // 获取 公钥和私钥
        String[] key = getKey();
        rsa = new RSA(key[0],key[1]);
        // 通过非对称加密进行解密
        String result = rsa.decryptStr(oldPassword, KeyType.PrivateKey);
        // 通过 matches 方法来判断 输入的密码是否 就是 BCrypt 加密之前的密码
        return bCryptPasswordEncoder.matches(nowPassword, result);
    }
}
