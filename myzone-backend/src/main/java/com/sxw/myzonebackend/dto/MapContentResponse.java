package com.sxw.myzonebackend.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 地图内容响应DTO
 */
@Data
public class MapContentResponse {
    /**
     * 内容ID
     */
    private Long id;
    
    /**
     * 纬度
     */
    private Double latitude;
    
    /**
     * 经度
     */
    private Double longitude;
    
    /**
     * 位置描述
     */
    private String location;
    
    /**
     * 内容标题（日记内容的前50个字符）
     */
    private String title;
    
    /**
     * 内容描述（日记内容）
     */
    private String description;
    
    /**
     * 图片URL（第一张图片）
     */
    private String imageUrl;
    
    /**
     * 视频URL
     */
    private String videoUrl;
    
    /**
     * 用户信息
     */
    private UserInfo user;
    
    /**
     * 点赞数
     */
    private Integer likes;
    
    /**
     * 评论数
     */
    private Integer comments;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 用户信息内部类
     */
    @Data
    public static class UserInfo {
        private Long id;
        private String username;
        private String nickname;
        private String avatar;
    }
} 