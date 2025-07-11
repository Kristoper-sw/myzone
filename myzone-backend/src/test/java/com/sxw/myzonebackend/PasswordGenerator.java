package com.sxw.myzonebackend;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "123456";
        String encodedPassword = encoder.encode(password);
        System.out.println("原始密码: " + password);
        System.out.println("BCrypt加密后: " + encodedPassword);
        
        // 验证密码
        boolean matches = encoder.matches(password, "$2a$10$G2tWrMYAvRpdZ/YKD/l04u2VTcKqeQKrB2fWYjfJuGMUzDObAAsN6");
        System.out.println("密码验证结果: " + matches);
    }
} 