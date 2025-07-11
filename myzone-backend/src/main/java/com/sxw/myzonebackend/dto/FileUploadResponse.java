package com.sxw.myzonebackend.dto;

import lombok.Data;

/**
 * 文件上传响应DTO
 */
@Data
public class FileUploadResponse {
    /**
     * 文件路径
     */
    private String filePath;
    
    /**
     * 文件名称
     */
    private String fileName;
    
    /**
     * 文件大小
     */
    private Long fileSize;
    
    /**
     * 文件类型
     */
    private String fileType;
    
    /**
     * 上传状态
     */
    private Boolean success;
    
    /**
     * 错误信息
     */
    private String errorMessage;
} 