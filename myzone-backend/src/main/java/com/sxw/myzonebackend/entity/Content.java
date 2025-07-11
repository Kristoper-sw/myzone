package com.sxw.myzonebackend.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 内容实体类
 */
@Data
public class Content {
    /**
     * 内容ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 内容类型：1-短视频，2-图片，3-混合
     */
    private Integer contentType;
    
    /**
     * 日记内容（可选）
     */
    private String diary;
    
    /**
     * 视频文件路径
     */
    private String videoPath;
    
    /**
     * 图片文件路径列表（JSON格式存储）
     */
    private String imagePaths;
    
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
     * 内容状态：0-草稿，1-已发布，2-已删除
     */
    private Integer status;
    
    /**
     * 点赞数
     */
    private Integer likeCount;
    
    /**
     * 评论数
     */
    private Integer commentCount;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 关联的用户信息（用于查询时填充）
     */
    private User user;
} 