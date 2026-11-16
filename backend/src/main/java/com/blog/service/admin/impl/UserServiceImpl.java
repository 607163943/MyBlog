package com.blog.service.admin.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.enums.UserExceptionEnum;
import com.blog.exception.BusinessException;
import com.blog.mapper.UserMapper;
import com.blog.pojo.dto.admin.ChangePasswordDTO;
import com.blog.pojo.dto.admin.UpdateUserInfoDTO;
import com.blog.pojo.dto.admin.UserLoginDTO;
import com.blog.pojo.po.User;
import com.blog.pojo.vo.admin.CaptchaVO;
import com.blog.pojo.vo.admin.LoginVO;
import com.blog.pojo.vo.admin.UserInfoVO;
import com.blog.service.admin.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 昵称最大长度
     */
    private static final int MAX_NICKNAME_LENGTH = 20;

    /**
     * 邮箱最大长度，与 tb_user.email 列长度一致
     */
    private static final int MAX_EMAIL_LENGTH = 100;

    /**
     * 头像地址最大长度，与 tb_user.avatar 列长度一致
     */
    private static final int MAX_AVATAR_LENGTH = 255;

    /**
     * 新密码最小长度
     */
    private static final int MIN_PASSWORD_LENGTH = 6;

    /**
     * 新密码最大长度
     */
    private static final int MAX_PASSWORD_LENGTH = 20;

    /**
     * 验证码有效期，单位毫秒
     */
    private static final long CAPTCHA_TIMEOUT = 5 * 60 * 1000L;

    /**
     * 验证码缓存：key 为验证码标识，value 为验证码内容
     */
    private static final TimedCache<String, String> CAPTCHA_CACHE = new TimedCache<>(CAPTCHA_TIMEOUT);

    static {
        // 每分钟清理一次过期验证码
        CAPTCHA_CACHE.schedulePrune(60 * 1000L);
    }

    @Override
    public LoginVO login(UserLoginDTO dto) {
        String username = dto.getUsername() == null ? "" : dto.getUsername().trim();
        String password = dto.getPassword() == null ? "" : dto.getPassword();
        if (username.isEmpty() || password.isEmpty()) {
            throw new BusinessException(UserExceptionEnum.USERNAME_OR_PASSWORD_EMPTY);
        }

        User user = getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        // 当前阶段密码暂用明文比对
        if (user == null || !password.equals(user.getPassword())) {
            throw new BusinessException(UserExceptionEnum.USERNAME_OR_PASSWORD_ERROR);
        }

        StpUtil.login(user.getId());
        return new LoginVO(StpUtil.getTokenName(), StpUtil.getTokenValue());
    }

    @Override
    public void logout() {
        StpUtil.logout();
    }

    @Override
    public UserInfoVO getUserInfo() {
        User user = getById(StpUtil.getLoginIdAsInt());
        if (user == null) {
            throw new BusinessException(UserExceptionEnum.USER_NOT_FOUND);
        }
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfoVO);
        return userInfoVO;
    }

    @Override
    public CaptchaVO generateCaptcha() {
        // 4 位纯数字验证码，带干扰线
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(
                120, 40, new RandomGenerator("0123456789", 4), 12);
        String captchaKey = IdUtil.fastSimpleUUID();
        CAPTCHA_CACHE.put(captchaKey, captcha.getCode());
        String image = "data:image/png;base64," + captcha.getImageBase64();
        return new CaptchaVO(captchaKey, image);
    }

    @Override
    public void updateUserInfo(UpdateUserInfoDTO dto) {
        Integer userId = StpUtil.getLoginIdAsInt();
        if (getById(userId) == null) {
            throw new BusinessException(UserExceptionEnum.USER_NOT_FOUND);
        }

        String nickname = dto.getNickname() == null ? "" : dto.getNickname().trim();
        if (nickname.isEmpty()) {
            throw new BusinessException(UserExceptionEnum.NICKNAME_EMPTY);
        }
        if (nickname.length() > MAX_NICKNAME_LENGTH) {
            throw new BusinessException(UserExceptionEnum.NICKNAME_TOO_LONG);
        }

        String email = dto.getEmail() == null ? "" : dto.getEmail().trim();
        if (!email.isEmpty() && (!Validator.isEmail(email) || email.length() > MAX_EMAIL_LENGTH)) {
            throw new BusinessException(UserExceptionEnum.EMAIL_INVALID);
        }

        String avatar = dto.getAvatar() == null ? "" : dto.getAvatar().trim();
        if (avatar.length() > MAX_AVATAR_LENGTH) {
            throw new BusinessException(UserExceptionEnum.AVATAR_TOO_LONG);
        }

        // updateById 默认忽略 null 字段，这里用 UpdateWrapper 以支持清空邮箱 / 头像
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<User>()
                .eq(User::getId, userId)
                .set(User::getNickname, nickname)
                .set(User::getEmail, email.isEmpty() ? null : email)
                .set(User::getAvatar, avatar.isEmpty() ? null : avatar);
        update(wrapper);
    }

    @Override
    public void changePassword(ChangePasswordDTO dto) {
        String oldPassword = dto.getOldPassword() == null ? "" : dto.getOldPassword();
        String newPassword = dto.getNewPassword() == null ? "" : dto.getNewPassword();
        String captchaKey = dto.getCaptchaKey() == null ? "" : dto.getCaptchaKey().trim();
        String captchaCode = dto.getCaptchaCode() == null ? "" : dto.getCaptchaCode().trim();

        if (captchaKey.isEmpty() || captchaCode.isEmpty()) {
            throw new BusinessException(UserExceptionEnum.CAPTCHA_EMPTY);
        }

        // 验证码一次性使用：先取出并移除，无论校验是否通过都作废
        String cachedCode = CAPTCHA_CACHE.get(captchaKey, false);
        CAPTCHA_CACHE.remove(captchaKey);
        if (cachedCode == null || !cachedCode.equalsIgnoreCase(captchaCode)) {
            throw new BusinessException(UserExceptionEnum.CAPTCHA_ERROR);
        }

        if (oldPassword.isEmpty()) {
            throw new BusinessException(UserExceptionEnum.OLD_PASSWORD_EMPTY);
        }
        if (newPassword.length() < MIN_PASSWORD_LENGTH
                || newPassword.length() > MAX_PASSWORD_LENGTH) {
            throw new BusinessException(UserExceptionEnum.NEW_PASSWORD_LENGTH_INVALID);
        }

        User user = getById(StpUtil.getLoginIdAsInt());
        if (user == null) {
            throw new BusinessException(UserExceptionEnum.USER_NOT_FOUND);
        }
        // 当前阶段密码暂用明文比对
        if (!oldPassword.equals(user.getPassword())) {
            throw new BusinessException(UserExceptionEnum.OLD_PASSWORD_ERROR);
        }
        if (oldPassword.equals(newPassword)) {
            throw new BusinessException(UserExceptionEnum.NEW_PASSWORD_SAME);
        }

        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<User>()
                .eq(User::getId, user.getId())
                .set(User::getPassword, newPassword);
        update(wrapper);
    }
}
