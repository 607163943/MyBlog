package com.my.blog.server.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.common.enums.ExceptionEnums;
import com.my.blog.common.exception.admin.AdminUserException;
import com.my.blog.common.utils.JWTUtils;
import com.my.blog.common.utils.MD5Util;
import com.my.blog.pojo.dto.UserLoginDTO;
import com.my.blog.pojo.po.User;
import com.my.blog.pojo.vo.admin.CaptchaVO;
import com.my.blog.pojo.vo.admin.UserInfo;
import com.my.blog.pojo.vo.admin.UserLoginVO;
import com.my.blog.server.mapper.UserMapper;
import com.my.blog.server.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private JWTUtils jwtUtils;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 用户登录
     *
     * @param userLoginDTO 用户登录信息
     * @return 用户信息+令牌
     */
    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        // 验证码校验
        Object captchaCodeObj = redisTemplate.opsForValue().get("user:captcha:" + userLoginDTO.getCaptchaKey());
        if (captchaCodeObj == null) {
            // 验证码过期
            throw new AdminUserException(ExceptionEnums.ADMIN_USER_CAPTCHA_EXPIRED);
        }

        String captchaCode = captchaCodeObj.toString();
        if (!userLoginDTO.getCaptchaCode().equals(captchaCode)) {
            // 验证码错误
            throw new AdminUserException(ExceptionEnums.ADMIN_USER_CAPTCHA_CODE_ERROR);
        }

        // 用户校验
        User user = lambdaQuery().eq(User::getUsername, userLoginDTO.getUsername()).one();
        if (user == null) {
            throw new AdminUserException(ExceptionEnums.ADMIN_USER_NOT_EXIST);
        }

        // 密码校验
        String passwordMd5 = MD5Util.MD5Encode(userLoginDTO.getPassword(), "UTF-8");
        if (!passwordMd5.equals(user.getPassword())) {
            throw new AdminUserException(ExceptionEnums.ADMIN_USER_PASSWORD_ERROR);
        }

        // 登陆成功
        log.info("用户{},id:{}，登陆成功！", user.getUsername(), user.getId());
        // 清除验证码缓存
        redisTemplate.opsForValue().getAndDelete("user:captcha:" + userLoginDTO.getCaptchaKey());
        // 用户信息
        UserInfo userInfo = BeanUtil.copyProperties(user, UserInfo.class);

        // 生成令牌
        String token = jwtUtils.createJWTWithUserId(user.getId());

        // 令牌存入Redis 1天过期
        redisTemplate.opsForValue().set("user:token:" + user.getId(), token, 1, TimeUnit.DAYS);

        return UserLoginVO.builder()
                .token(token)
                .userInfo(userInfo)
                .build();
    }

    /**
     * 获取验证码
     *
     * @return 验证码数据
     */
    @Override
    public CaptchaVO captcha() {
        ShearCaptcha shearCaptcha = CaptchaUtil.createShearCaptcha(150, 30, 4, 2);

        String imageBase64 = shearCaptcha.getImageBase64();

        // 验证码存入Redis
        // 用UUID作为唯一key
        String uuid = IdUtil.simpleUUID();
        redisTemplate.opsForValue().set("user:captcha:" + uuid, shearCaptcha.getCode(), 60, TimeUnit.SECONDS);

        return CaptchaVO.builder()
                .imageBase64(imageBase64)
                .captchaKey(uuid)
                .build();
    }

    /**
     * 用户登出
     */
    @Override
    public void logout() {
        // 获取登录用户数据
        User user= (User) SecurityUtils.getSubject().getPrincipal();

        // 清除缓存中的token
        redisTemplate.opsForValue().getAndDelete("user:token:" + user.getId());

        log.info("用户{},id:{}，登出成功！", user.getUsername(), user.getId());
    }
}
