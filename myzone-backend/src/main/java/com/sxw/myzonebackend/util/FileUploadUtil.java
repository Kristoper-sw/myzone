package com.sxw.myzonebackend.util;

import com.sxw.myzonebackend.dto.FileUploadResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 文件上传工具类
 */
@Component
public class FileUploadUtil {
    
    // 上传文件的基础路径
    private static final String UPLOAD_BASE_PATH = "uploads";
    
    // 允许的视频文件类型
    private static final String[] ALLOWED_VIDEO_TYPES = {".mp4", ".avi", ".mov", ".wmv", ".flv", ".mkv"};
    
    // 允许的图片文件类型
    private static final String[] ALLOWED_IMAGE_TYPES = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp"};
    
    // 最大文件大小（100MB）
    private static final long MAX_FILE_SIZE = 100 * 1024 * 1024;
    
    /**
     * 上传视频文件
     */
    public FileUploadResponse uploadVideo(MultipartFile file) {
        return uploadFile(file, "videos", ALLOWED_VIDEO_TYPES);
    }
    
    /**
     * 上传图片文件
     */
    public FileUploadResponse uploadImage(MultipartFile file) {
        return uploadFile(file, "images", ALLOWED_IMAGE_TYPES);
    }
    
    /**
     * 通用文件上传方法
     */
    private FileUploadResponse uploadFile(MultipartFile file, String subDir, String[] allowedTypes) {
        FileUploadResponse response = new FileUploadResponse();
        
        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                response.setSuccess(false);
                response.setErrorMessage("文件不能为空");
                return response;
            }
            
            // 检查文件大小
            if (file.getSize() > MAX_FILE_SIZE) {
                response.setSuccess(false);
                response.setErrorMessage("文件大小不能超过100MB");
                return response;
            }
            
            // 获取文件扩展名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFilename);
            
            // 检查文件类型
            if (!isAllowedFileType(fileExtension, allowedTypes)) {
                response.setSuccess(false);
                response.setErrorMessage("不支持的文件类型");
                return response;
            }
            
            // 生成唯一文件名
            String uniqueFileName = generateUniqueFileName(fileExtension);
            
            // 创建上传目录
            String uploadDir = createUploadDirectory(subDir);
            
            // 保存文件
            Path filePath = Paths.get(uploadDir, uniqueFileName);
            Files.copy(file.getInputStream(), filePath);
            
            // 设置响应信息 - 返回相对路径，便于前端访问
            response.setSuccess(true);
            response.setFilePath("/uploads/" + subDir + "/" + uniqueFileName);
            response.setFileName(uniqueFileName);
            response.setFileSize(file.getSize());
            response.setFileType(fileExtension);
            
        } catch (IOException e) {
            response.setSuccess(false);
            response.setErrorMessage("文件上传失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".")).toLowerCase();
    }
    
    /**
     * 检查文件类型是否允许
     */
    private boolean isAllowedFileType(String fileExtension, String[] allowedTypes) {
        for (String allowedType : allowedTypes) {
            if (allowedType.equals(fileExtension)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 生成唯一文件名
     */
    private String generateUniqueFileName(String fileExtension) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return timestamp + "_" + uuid + fileExtension;
    }
    
    /**
     * 创建上传目录
     */
    private String createUploadDirectory(String subDir) throws IOException {
        String uploadPath = UPLOAD_BASE_PATH + File.separator + subDir;
        Path path = Paths.get(uploadPath);
        
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        
        return uploadPath;
    }
    
    /**
     * 删除文件
     */
    public boolean deleteFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            return false;
        }
    }
} 