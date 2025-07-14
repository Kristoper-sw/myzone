package com.sxw.myzonebackend.service;

import com.sxw.myzonebackend.dto.ContentUploadRequest;
import com.sxw.myzonebackend.dto.MapContentResponse;
import com.sxw.myzonebackend.dto.PageResponse;
import com.sxw.myzonebackend.entity.Content;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 内容服务接口
 */
public interface ContentService {
    
    /**
     * 上传内容（包含文件上传和内容保存）
     */
    Content uploadContent(Long userId, ContentUploadRequest request, 
                         MultipartFile videoFile, List<MultipartFile> imageFiles);
    
    /**
     * 获取用户的内容列表
     */
    PageResponse<Content> getUserContents(Long userId, Integer page, Integer size);
    
    /**
     * 获取用户内容总数
     */
    int getUserContentsCount(Long userId);
    
    /**
     * 获取内容详情
     */
    Content getContentById(Long contentId);
    
    /**
     * 删除内容
     */
    boolean deleteContent(Long userId, Long contentId);
    
    /**
     * 更新内容状态
     */
    boolean updateContentStatus(Long userId, Long contentId, Integer status);
    
    /**
     * 获取所有已发布的内容（用于地图展示）
     */
    List<MapContentResponse> getAllPublishedContents();
    
    /**
     * 获取指定区域的内容
     */
    List<MapContentResponse> getContentsByArea(Double minLat, Double maxLat, 
                                             Double minLng, Double maxLng);

    /**
     * 点赞内容
     */
    boolean likeContent(Long userId, Long contentId);

    /**
     * 取消点赞
     */
    boolean unlikeContent(Long userId, Long contentId);

    /**
     * 判断用户是否已点赞
     */
    boolean hasLiked(Long userId, Long contentId);

    /**
     * 编辑内容（支持文件和字段）
     */
    Content updateContent(Long userId, Long contentId, ContentUploadRequest request, MultipartFile videoFile, List<MultipartFile> imageFiles);
} 