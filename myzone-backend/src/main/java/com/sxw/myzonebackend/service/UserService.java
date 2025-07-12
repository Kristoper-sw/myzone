package com.sxw.myzonebackend.service;

import com.sxw.myzonebackend.dto.LoginRequest;
import com.sxw.myzonebackend.dto.LoginResponse;
import com.sxw.myzonebackend.dto.RegisterRequest;
import com.sxw.myzonebackend.dto.UpdateUserRequest;
import com.sxw.myzonebackend.entity.User;
import com.sxw.myzonebackend.mapper.UserMapper;
import com.sxw.myzonebackend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 用户服务类
 */
@Service
@Transactional(readOnly = true)
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 用户注册
     */
    @Transactional
    public void register(RegisterRequest request) {
        // 验证密码确认
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("两次输入的密码不一致");
        }

        // 检查用户名是否已存在
        User existingUser = userMapper.findByUsername(request.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (StringUtils.hasText(request.getEmail())) {
            existingUser = userMapper.findByEmail(request.getEmail());
            if (existingUser != null) {
                throw new RuntimeException("邮箱已被注册");
            }
        }

        // 创建新用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setNickname(StringUtils.hasText(request.getNickname()) ? request.getNickname() : request.getUsername());
        user.setStatus(1); // 1-正常状态

        // 保存用户
        int result = userMapper.insert(user);
        if (result != 1) {
            throw new RuntimeException("用户注册失败");
        }
    }

    /**
     * 用户登录
     */
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        // 根据用户名查询用户
        User user = userMapper.findByUsername(request.getUsername());
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 检查用户状态
        if (user.getStatus() != 1) {
            throw new RuntimeException("用户已被禁用");
        }

        // 生成JWT令牌
        String token = jwtUtil.generateToken(user.getUsername(), user.getId());

        // 构建登录响应
        LoginResponse response = new LoginResponse();
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setNickname(user.getNickname());
        response.setEmail(user.getEmail());
        response.setAvatar(user.getAvatar());
        response.setToken(token);
        response.setExpiresIn(86400000L); // 24小时

        return response;
    }

    /**
     * 根据ID获取用户信息
     */
    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return userMapper.findById(userId);
    }

    /**
     * 根据用户名获取用户信息
     */
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    /**
     * 更新用户信息
     */
    @Transactional
    public void updateUser(User user) {
        if (user.getId() == null) {
            throw new RuntimeException("用户ID不能为空");
        }
        
        int result = userMapper.update(user);
        if (result != 1) {
            throw new RuntimeException("用户信息更新失败");
        }
    }

    /**
     * 删除用户
     */
    @Transactional
    public void deleteUser(Long userId) {
        if (userId == null) {
            throw new RuntimeException("用户ID不能为空");
        }
        
        int result = userMapper.deleteById(userId);
        if (result != 1) {
            throw new RuntimeException("用户删除失败");
        }
    }

    /**
     * 更新用户信息
     */
    @Transactional
    public void updateUserInfo(Long userId, UpdateUserRequest request) {
        // 验证当前用户是否存在
        User currentUser = userMapper.findById(userId);
        if (currentUser == null) {
            throw new RuntimeException("用户不存在");
        }

        boolean isPasswordChange =
            org.springframework.util.StringUtils.hasText(request.getCurrentPassword()) ||
            org.springframework.util.StringUtils.hasText(request.getNewPassword()) ||
            org.springframework.util.StringUtils.hasText(request.getConfirmPassword());

        if (isPasswordChange) {
            // 密码修改逻辑
            if (!org.springframework.util.StringUtils.hasText(request.getCurrentPassword())) {
                throw new RuntimeException("当前密码不能为空");
            }
            if (!org.springframework.util.StringUtils.hasText(request.getNewPassword())) {
                throw new RuntimeException("新密码不能为空");
            }
            if (!org.springframework.util.StringUtils.hasText(request.getConfirmPassword())) {
                throw new RuntimeException("请再次输入新密码");
            }
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                throw new RuntimeException("新密码与确认密码不一致");
            }
            if (request.getNewPassword().length() < 6 || request.getNewPassword().length() > 20) {
                throw new RuntimeException("新密码长度必须在6-20个字符之间");
            }
            // 校验当前密码
            if (!passwordEncoder.matches(request.getCurrentPassword(), currentUser.getPassword())) {
                throw new RuntimeException("当前密码错误");
            }
            // 更新密码
            String encodedPassword = passwordEncoder.encode(request.getNewPassword());
            int passwordResult = userMapper.updatePassword(userId, encodedPassword);
            if (passwordResult != 1) {
                throw new RuntimeException("密码更新失败");
            }
            return;
        }

        // 基本信息修改逻辑
        // 检查邮箱是否被其他用户使用
        if (org.springframework.util.StringUtils.hasText(request.getEmail()) && !request.getEmail().equals(currentUser.getEmail())) {
            User existingUser = userMapper.findByEmail(request.getEmail());
            if (existingUser != null && !existingUser.getId().equals(userId)) {
                throw new RuntimeException("邮箱已被其他用户使用");
            }
        }
        // 更新用户基本信息
        User updateUser = new User();
        updateUser.setId(userId);
        if (org.springframework.util.StringUtils.hasText(request.getNickname())) {
            if (request.getNickname().length() < 2 || request.getNickname().length() > 20) {
                throw new RuntimeException("昵称长度必须在2-20个字符之间");
            }
            updateUser.setNickname(request.getNickname());
        }
        if (org.springframework.util.StringUtils.hasText(request.getEmail())) {
            updateUser.setEmail(request.getEmail());
        }
        if (org.springframework.util.StringUtils.hasText(request.getAvatar())) {
            updateUser.setAvatar(request.getAvatar());
        }
        if (org.springframework.util.StringUtils.hasText(request.getPhone())) {
            updateUser.setPhone(request.getPhone());
        }
        int result = userMapper.update(updateUser);
        if (result != 1) {
            throw new RuntimeException("用户信息更新失败");
        }
    }

    /**
     * 获取用户信息（不包含密码）
     */
    @Transactional(readOnly = true)
    public User getUserInfo(Long userId) {
        User user = userMapper.findById(userId);
        if (user != null) {
            // 清除密码信息
            user.setPassword(null);
        }
        return user;
    }
} 