package com.sxw.myzonebackend.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sxw.myzonebackend.common.Result;
import com.sxw.myzonebackend.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 登录检查拦截器
 */
@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 不需要认证的路径
    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
            "/api/user/login",
            "/api/user/register",
            "/api/user/health",
            "/uploads/",
            "/static/",
            "/api/content/map/all"
    );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 处理预检请求，必须加CORS头
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            
            // 动态设置CORS头，根据请求来源
            String origin = request.getHeader("Origin");
            if ("http://localhost:3000".equals(origin) || "https://www.culturezone.space".equals(origin)) {
                response.setHeader("Access-Control-Allow-Origin", origin);
            } else {
                response.setHeader("Access-Control-Allow-Origin", "https://www.culturezone.space");
            }
            
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With, Accept");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            return false;
        }

        String requestURI = request.getRequestURI();
        log.info("拦截请求: {}", requestURI);

        // 检查是否为排除的路径
        if (isExcludedPath(requestURI)) {
            log.info("放行排除路径: {}", requestURI);
            return true;
        }

        // 获取Authorization头
        String authHeader = request.getHeader("Authorization");

        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            log.info("请求头Authorization为空或格式错误");
            sendUnauthorizedResponse(response, "未提供认证令牌");
            return false;
        }

        // 提取token
        String token = authHeader.substring(7);

        try {
            // 验证token
            if (!jwtUtil.validateToken(token)) {
                log.info("无效的认证令牌");
                sendUnauthorizedResponse(response, "无效的认证令牌");
                return false;
            }

            // 检查token是否过期
            if (jwtUtil.isTokenExpired(token)) {
                log.info("认证令牌已过期");
                sendUnauthorizedResponse(response, "认证令牌已过期");
                return false;
            }

            // 将用户信息设置到请求属性中
            String username = jwtUtil.getUsernameFromToken(token);
            Long userId = jwtUtil.getUserIdFromToken(token);

            request.setAttribute("username", username);
            request.setAttribute("userId", userId);

            log.info("令牌验证成功，用户: {}, 用户ID: {}", username, userId);
            return true;

        } catch (Exception e) {
            log.error("认证失败: {}", e.getMessage());
            sendUnauthorizedResponse(response, "认证失败: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 请求处理完成后的逻辑
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求完成后的清理逻辑
    }

    /**
     * 检查是否为排除的路径
     */
    private boolean isExcludedPath(String requestURI) {
        return EXCLUDED_PATHS.stream().anyMatch(requestURI::startsWith);
    }

    /**
     * 发送未授权响应
     */
    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        Result<String> result = Result.error(401, message);
        String jsonResponse = objectMapper.writeValueAsString(result);
        response.getWriter().write(jsonResponse);
    }
} 