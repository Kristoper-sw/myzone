package com.sxw.myzonebackend;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {
    public static void main(String[] args) {
        // 显式指定使用 BCrypt Version 2A（兼容旧哈希）
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String password = "123456";
        String hashedPassword = "$2a$10$mq5s1brrjTJ729e6hdGKueX6Iv0ZNDZx7vlDPRFggT0YhrVWl46Dq";

        boolean matches = encoder.matches(password, hashedPassword);

        System.out.println("密码验证结果: " + matches);
        System.out.println(matches ? "✅ 密码验证成功！" : "❌ 密码验证失败！");
    }
}
