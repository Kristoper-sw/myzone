package com.sxw.myzonebackend.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sxw.myzonebackend.dto.ContentUploadRequest;
import com.sxw.myzonebackend.dto.FileUploadResponse;
import com.sxw.myzonebackend.dto.MapContentResponse;
import com.sxw.myzonebackend.dto.PageResponse;
import com.sxw.myzonebackend.entity.Content;
import com.sxw.myzonebackend.entity.User;
import com.sxw.myzonebackend.mapper.ContentMapper;
import com.sxw.myzonebackend.service.ContentService;
import com.sxw.myzonebackend.service.UserService;
import com.sxw.myzonebackend.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 内容服务实现类
 */
@Service
public class ContentServiceImpl implements ContentService {
    
    @Autowired
    private FileUploadUtil fileUploadUtil;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ContentMapper contentMapper;
    
    @Override
    public Content uploadContent(Long userId, ContentUploadRequest request, 
                               MultipartFile videoFile, List<MultipartFile> imageFiles) {
        Content content = new Content();
        content.setUserId(userId);
        content.setContentType(request.getContentType());
        content.setDiary(request.getDiary());
        content.setLatitude(request.getLatitude());
        content.setLongitude(request.getLongitude());
        content.setLocation(request.getLocation());
        content.setStatus(1); // 默认发布状态
        content.setLikeCount(0);
        content.setCommentCount(0);
        content.setCreateTime(LocalDateTime.now());
        content.setUpdateTime(LocalDateTime.now());
        
        // 处理视频文件上传
        if (videoFile != null && !videoFile.isEmpty()) {
            FileUploadResponse videoResponse = fileUploadUtil.uploadVideo(videoFile);
            if (videoResponse.getSuccess()) {
                content.setVideoPath(videoResponse.getFilePath());
            } else {
                throw new RuntimeException("视频上传失败: " + videoResponse.getErrorMessage());
            }
        }
        
        // 处理图片文件上传
        if (imageFiles != null && !imageFiles.isEmpty()) {
            List<String> imagePaths = new ArrayList<>();
            for (MultipartFile imageFile : imageFiles) {
                if (!imageFile.isEmpty()) {
                    FileUploadResponse imageResponse = fileUploadUtil.uploadImage(imageFile);
                    if (imageResponse.getSuccess()) {
                        imagePaths.add(imageResponse.getFilePath());
                    } else {
                        throw new RuntimeException("图片上传失败: " + imageResponse.getErrorMessage());
                    }
                }
            }
            
            // 将图片路径列表转换为JSON字符串存储
            try {
                content.setImagePaths(objectMapper.writeValueAsString(imagePaths));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("图片路径序列化失败", e);
            }
        }
        
        // 保存到数据库
        int result = contentMapper.insertContent(content);
        if (result <= 0) {
            throw new RuntimeException("保存内容到数据库失败");
        }
        
        return content;
    }
    
    @Override
    public PageResponse<Content> getUserContents(Long userId, Integer page, Integer size) {
        int offset = (page - 1) * size;
        List<Content> contents = contentMapper.selectContentsByUserId(userId, offset, size);
        int total = contentMapper.countContentsByUserId(userId);
        return new PageResponse<>(contents, (long) total, page, size);
    }
    
    @Override
    public int getUserContentsCount(Long userId) {
        return contentMapper.countContentsByUserId(userId);
    }
    
    @Override
    public Content getContentById(Long contentId) {
        return contentMapper.selectContentById(contentId);
    }
    
    @Override
    public boolean deleteContent(Long userId, Long contentId) {
        int result = contentMapper.deleteContent(contentId, userId);
        return result > 0;
    }
    
    @Override
    public boolean updateContentStatus(Long userId, Long contentId, Integer status) {
        int result = contentMapper.updateContentStatus(contentId, userId, status);
        return result > 0;
    }
    
    @Override
    public List<MapContentResponse> getAllPublishedContents() {
        List<Content> contents = contentMapper.selectAllPublishedContents();
        return convertToMapContentResponses(contents);
    }
    
    @Override
    public List<MapContentResponse> getContentsByArea(Double minLat, Double maxLat, 
                                                    Double minLng, Double maxLng) {
        List<Content> contents = contentMapper.selectContentsByArea(minLat, maxLat, minLng, maxLng);
        return convertToMapContentResponses(contents);
    }
    
    /**
     * 将Content实体转换为MapContentResponse
     */
    private List<MapContentResponse> convertToMapContentResponses(List<Content> contents) {
        List<MapContentResponse> responses = new ArrayList<>();
        
        for (Content content : contents) {
            MapContentResponse response = new MapContentResponse();
            response.setId(content.getId());
            response.setLatitude(content.getLatitude());
            response.setLongitude(content.getLongitude());
            response.setLocation(content.getLocation());
            response.setTitle(content.getDiary() != null && !content.getDiary().isEmpty() 
                ? content.getDiary().substring(0, Math.min(content.getDiary().length(), 50)) + "..." 
                : "精彩内容");
            response.setDescription(content.getDiary());
            response.setImageUrl(content.getImagePaths());
            response.setVideoUrl(content.getVideoPath());
            response.setLikes(content.getLikeCount());
            response.setComments(content.getCommentCount());
            response.setCreateTime(content.getCreateTime());
            
            // 设置用户信息
            MapContentResponse.UserInfo userInfo = new MapContentResponse.UserInfo();
            if (content.getUser() != null) {
                userInfo.setId(content.getUser().getId());
                userInfo.setUsername(content.getUser().getUsername());
                userInfo.setNickname(content.getUser().getNickname());
                userInfo.setAvatar(content.getUser().getAvatar());
            } else {
                // 如果没有关联用户信息，使用默认值
                userInfo.setId(content.getUserId());
                userInfo.setUsername("user" + content.getUserId());
                userInfo.setNickname("用户" + content.getUserId());
                userInfo.setAvatar("https://placehold.co/40x40?text=U" + content.getUserId());
            }
            response.setUser(userInfo);
            
            responses.add(response);
        }
        
        return responses;
    }
} 