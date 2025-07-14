package com.sxw.myzonebackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 更新用户信息请求DTO
 */
@Setter
@Getter
public class UpdateUserRequest {
    
    // Getters and Setters
    private String nickname;
    
    @Email(message = "邮箱格式不正确")
    private String email;
    
    private String avatar;
    
    private String phone;
    
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    private String newPassword;
    
    @Size(min = 6, max = 20, message = "确认密码长度必须在6-20个字符之间")
    private String confirmPassword;
    
    private String currentPassword;

} 