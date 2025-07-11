package com.sxw.myzonebackend;

import com.sxw.myzonebackend.dto.UpdateUserRequest;
import com.sxw.myzonebackend.entity.User;
import com.sxw.myzonebackend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserControllerTest {

    @Autowired
    private UserService userService;

    @Test
    public void testUpdateUserInfo() {
        // 创建测试用户
        User user = new User();
        user.setUsername("testuser");
        user.setPassword(new BCryptPasswordEncoder().encode("password123"));
        user.setNickname("测试用户");
        user.setEmail("test@example.com");
        user.setStatus(1);
        
        // 这里可以添加更多测试逻辑
        assertNotNull(user);
    }

    @Test
    public void testUpdateUserRequest() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setNickname("新昵称");
        request.setEmail("new@example.com");
        request.setCurrentPassword("password123");
        
        assertEquals("新昵称", request.getNickname());
        assertEquals("new@example.com", request.getEmail());
        assertEquals("password123", request.getCurrentPassword());
    }
} 