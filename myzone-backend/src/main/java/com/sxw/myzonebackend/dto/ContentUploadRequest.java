package com.sxw.myzonebackend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 内容上传请求DTO
 */
@Data
public class ContentUploadRequest {
    /**
     * 内容类型：1-短视频，2-图片，3-混合
     */
    @NotNull(message = "内容类型不能为空")
    private Integer contentType;
    
    /**
     * 日记内容（可选）
     */
    private String diary;
    
    /**
     * 视频文件（可选）
     */
    private String videoFile;
    
    /**
     * 图片文件列表（可选）
     */
    private List<String> imageFiles;
    
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
} 