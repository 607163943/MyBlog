package com.blog.service.admin.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.common.Constants;
import com.blog.enums.FileExceptionEnum;
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
import com.blog.service.admin.FileRefService;
import com.blog.service.admin.FileAssetService;
import com.blog.service.admin.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * 用户服务实现
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 验证码有效期，单位毫秒
     */
    private static final long CAPTCHA_TIMEOUT = 5 * 60 * 1000L;

    /**
     * 验证码缓存：key 为验证码标识，value 为验证码内容
     */
    private static final TimedCache<String, String> CAPTCHA_CACHE = new TimedCache<>(CAPTCHA_TIMEOUT);

    private final FileAssetService fileAssetService;

    private final FileRefService fileRefService;

    static {
        // 每分钟清理一次过期验证码
        CAPTCHA_CACHE.schedulePrune(60 * 1000L);
    }

    @Override
    public LoginVO login(UserLoginDTO dto) {
        String username = dto.getUsername() == null ? "" : dto.getUsername().trim();
        String password = dto.getPassword() == null ? "" : dto.getPassword();

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
    @Transactional(rollbackFor = Exception.class)
    public void updateUserInfo(UpdateUserInfoDTO dto) {
        Integer userId = StpUtil.getLoginIdAsInt();
        if (getById(userId) == null) {
            throw new BusinessException(UserExceptionEnum.USER_NOT_FOUND);
        }

        String nickname = dto.getNickname() == null ? "" : dto.getNickname().trim();

        String email = dto.getEmail() == null ? "" : dto.getEmail().trim();

        String avatar = dto.getAvatar() == null ? "" : dto.getAvatar().trim();
        Integer avatarFileId = resolveAvatarFileId(dto.getAvatarFileId(), avatar);
        if (avatarFileId != null) {
            String url = fileAssetService.buildUrl(fileAssetService.getById(avatarFileId));
            if (url != null) {
                avatar = url;
            }
        }

        // updateById 默认忽略 null 字段，这里用 UpdateWrapper 以支持清空邮箱 / 头像
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<User>()
                .eq(User::getId, userId)
                .set(User::getNickname, nickname)
                .set(User::getEmail, email.isEmpty() ? null : email)
                .set(User::getAvatar, avatar.isEmpty() ? null : avatar)
                .set(User::getAvatarFileId, avatarFileId);
        update(wrapper);

        fileRefService.replaceRefs(Constants.FILE_BIZ_TYPE_USER, userId,
                avatarFileId == null
                        ? Collections.emptyList()
                        : Collections.singletonList(avatarFileId));
    }

    /**
     * 解析头像文件 ID：优先使用前端提交的 fileId，缺失时按本地 URL 反查
     */
    private Integer resolveAvatarFileId(Integer requestedFileId, String avatarUrl) {
        if (requestedFileId != null) {
            if (fileAssetService.getById(requestedFileId) == null) {
                throw new BusinessException(FileExceptionEnum.FILE_NOT_FOUND);
            }
            return requestedFileId;
        }
        if (avatarUrl == null || avatarUrl.isEmpty()) {
            return null;
        }
        return fileAssetService.resolveFileAssetIdByUrl(avatarUrl);
    }

    @Override
    public void changePassword(ChangePasswordDTO dto) {
        String oldPassword = dto.getOldPassword() == null ? "" : dto.getOldPassword();
        String newPassword = dto.getNewPassword() == null ? "" : dto.getNewPassword();
        String captchaKey = dto.getCaptchaKey() == null ? "" : dto.getCaptchaKey().trim();
        String captchaCode = dto.getCaptchaCode() == null ? "" : dto.getCaptchaCode().trim();

        // 验证码一次性使用：先取出并移除，无论校验是否通过都作废
        String cachedCode = CAPTCHA_CACHE.get(captchaKey, false);
        CAPTCHA_CACHE.remove(captchaKey);
        if (cachedCode == null || !cachedCode.equalsIgnoreCase(captchaCode)) {
            throw new BusinessException(UserExceptionEnum.CAPTCHA_ERROR);
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
