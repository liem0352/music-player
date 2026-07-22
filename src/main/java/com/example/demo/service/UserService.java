package com.example.demo.service;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.example.demo.tools.Constant;
import com.example.demo.tools.EncryptionTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
/**
 * UserService - 用户业务逻辑层
 *
 * <p>提供用户登录、注册功能。</p>
 * <p>密码安全：注册时通过 EncryptionTool 进行 RSA+BCrypt 双重加密，</p>
 * <p>登录时通过 EncryptionTool.decrypt 验证密码。</p>
 *
 * <p>修复说明：优化登录密码验证逻辑，移除手动 charAt(0) 判断，</p>
 * <p>将格式判断下沉到 EncryptionTool.decrypt 方法内部统一处理。</p>
 *
 * @author liem
 * @version 1.1.0
 * @since 2022-09-28
 */
@Service
@Slf4j
public class UserService {
    @Resource
    private UserMapper userMapper;
    @Autowired
    private EncryptionTool encryptionTool;

    /**
     * 用户登录验证
     *
     * @param request HTTP 请求对象（用于创建 Session）
     * @param user    包含用户名和明文密码的 User 对象
     * @return 登录成功返回 User 对象（密码已清空），失败返回错误消息字符串
     */
    public Object login(HttpServletRequest request, User user){
        User userTest = userMapper.getUserByName(user.getUsername());
        if(userTest != null){
            // 调用统一的密码验证方法（内部自动兼容 RSA+BCrypt 和纯 BCrypt 两种格式）
            if(encryptionTool.decrypt(user.getPassword(), userTest.getPassword())){
                userTest.setPassword("");  //密码隐藏
                HttpSession session = request.getSession(true);
                session.setAttribute(Constant.USER_INFO_SESSION_KEY, userTest);
                return userTest;
            }
        }
        return "用户名或密码有误";
    }

    /**
     * 用户注册
     *
     * @param user 包含用户名和明文密码的 User 对象
     * @return 注册成功返回包含跳转信息的 Map，失败返回错误消息字符串
     */
    public Object register(User user){
        User userByName = userMapper.getUserByName(user.getUsername());
        if(userByName != null){
            return "该账号已被注册！";
        }
        String encryptPassword = encryptionTool.encrypt(user.getPassword());
        user.setPassword(encryptPassword);
        int register = userMapper.register(user);
        if(register == 1){
            HashMap<String,Object> map = new HashMap<>();
            map.put("result","success");
            map.put("redirect","/login.html");
            return map;
        }else{
            return "注册失败!";
        }
    }
}
