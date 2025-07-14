package com.sxw.myzonebackend.controller;

import com.sxw.myzonebackend.common.Result;
import com.sxw.myzonebackend.dto.ContentUploadRequest;
import com.sxw.myzonebackend.dto.MapContentResponse;
import com.sxw.myzonebackend.dto.PageResponse;
import com.sxw.myzonebackend.entity.Content;
import com.sxw.myzonebackend.service.ContentService;
import com.sxw.myzonebackend.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 内容控制器
 */
@RestController
@RequestMapping("/api/content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    /**
     * 上传内容（包含文件和日记）
     */
    @PostMapping("/upload")
    public Result<Content> uploadContent(
            @RequestParam("contentType") Integer contentType,
            @RequestParam("title") String title,
            @RequestParam(value = "diary", required = false) String diary,
            @RequestParam(value = "videoFile", required = false) MultipartFile videoFile,
            @RequestParam(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
            @RequestParam(value = "latitude", required = false) Double latitude,
            @RequestParam(value = "longitude", required = false) Double longitude,
            @RequestParam(value = "location", required = false) String location,
            HttpServletRequest request) {
        try {
            // 获取用户ID
            Long userId = (Long) request.getAttribute("userId");

            // 构建请求对象
            ContentUploadRequest uploadRequest = new ContentUploadRequest();
            uploadRequest.setContentType(contentType);
            uploadRequest.setTitle(title);
            uploadRequest.setDiary(diary);
            uploadRequest.setLatitude(latitude);
            uploadRequest.setLongitude(longitude);
            uploadRequest.setLocation(location);

            // 验证内容类型和文件
            if (contentType == 1 && (videoFile == null || videoFile.isEmpty())) {
                return Result.error("短视频类型必须上传视频文件");
            }
            
            if (contentType == 2 && (imageFiles == null || imageFiles.isEmpty())) {
                return Result.error("图片类型必须上传至少一张图片");
            }

            Content content = contentService.uploadContent(userId, uploadRequest, videoFile, imageFiles);
            return Result.success("内容上传成功", content);
            
        } catch (Exception e) {
            return Result.error("内容上传失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户的内容列表
     */
    @GetMapping("/user/{userId}")
    public Result<PageResponse<Content>> getUserContents(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request) {
        try {
            // 验证用户权限（只能查看自己的内容）
            Long currentUserId = (Long) request.getAttribute("userId");

            if (!currentUserId.equals(userId)) {
                return Result.error("无权限查看其他用户的内容");
            }

            PageResponse<Content> contents = contentService.getUserContents(userId, page, size);
            return Result.success(contents);
            
        } catch (Exception e) {
            return Result.error("获取内容列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取内容详情
     */
    @GetMapping("/{contentId}")
    public Result<Content> getContentById(@PathVariable Long contentId) {
        try {
            Content content = contentService.getContentById(contentId);
            if (content == null) {
                return Result.error("内容不存在");
            }
            return Result.success(content);
        } catch (Exception e) {
            return Result.error("获取内容详情失败: " + e.getMessage());
        }
    }

    /**
     * 删除内容
     */
    @DeleteMapping("/{contentId}")
    public Result<String> deleteContent(@PathVariable Long contentId, HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");

            boolean success = contentService.deleteContent(userId, contentId);
            if (success) {
                return Result.success("内容删除成功");
            } else {
                return Result.error("内容删除失败");
            }
        } catch (Exception e) {
            return Result.error("删除内容失败: " + e.getMessage());
        }
    }

    /**
     * 更新内容状态
     */
    @PutMapping("/{contentId}/status")
    public Result<String> updateContentStatus(
            @PathVariable Long contentId,
            @RequestParam Integer status,
            HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");

            boolean success = contentService.updateContentStatus(userId, contentId, status);
            if (success) {
                return Result.success("内容状态更新成功");
            } else {
                return Result.error("内容状态更新失败");
            }
        } catch (Exception e) {
            return Result.error("更新内容状态失败: " + e.getMessage());
        }
    }

    /**
     * 编辑内容（支持文件和字段）
     */
    @PutMapping("/{contentId}")
    public Result<Content> updateContent(
            @PathVariable Long contentId,
            @RequestParam("contentType") Integer contentType,
            @RequestParam("title") String title,
            @RequestParam(value = "diary", required = false) String diary,
            @RequestParam(value = "videoFile", required = false) MultipartFile videoFile,
            @RequestParam(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
            @RequestParam(value = "latitude", required = false) Double latitude,
            @RequestParam(value = "longitude", required = false) Double longitude,
            @RequestParam(value = "location", required = false) String location,
            HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            ContentUploadRequest uploadRequest = new ContentUploadRequest();
            uploadRequest.setContentType(contentType);
            uploadRequest.setTitle(title);
            uploadRequest.setDiary(diary);
            uploadRequest.setLatitude(latitude);
            uploadRequest.setLongitude(longitude);
            uploadRequest.setLocation(location);
            Content updated = contentService.updateContent(userId, contentId, uploadRequest, videoFile, imageFiles);
            return Result.success("内容更新成功", updated);
        } catch (Exception e) {
            return Result.error("内容更新失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有已发布的内容（用于地图展示）
     */
    @GetMapping("/map/all")
    public Result<List<MapContentResponse>> getAllMapContents() {
        try {
            List<MapContentResponse> contents = contentService.getAllPublishedContents();
            return Result.success(contents);
        } catch (Exception e) {
            return Result.error("获取地图内容失败: " + e.getMessage());
        }
    }

    /**
     * 获取指定区域的内容
     */
    @GetMapping("/map/area")
    public Result<List<MapContentResponse>> getContentsByArea(
            @RequestParam Double minLat,
            @RequestParam Double maxLat,
            @RequestParam Double minLng,
            @RequestParam Double maxLng) {
        try {
            List<MapContentResponse> contents = contentService.getContentsByArea(minLat, maxLat, minLng, maxLng);
            return Result.success(contents);
        } catch (Exception e) {
            return Result.error("获取区域内容失败: " + e.getMessage());
        }
    }

    /**
     * 点赞内容
     */
    @PostMapping("/{contentId}/like")
    public Result<String> likeContent(@PathVariable Long contentId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (contentService.likeContent(userId, contentId)) {
            return Result.success("点赞成功");
        } else {
            return Result.error("已点赞或点赞失败");
        }
    }

    /**
     * 取消点赞
     */
    @PostMapping("/{contentId}/unlike")
    public Result<String> unlikeContent(@PathVariable Long contentId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (contentService.unlikeContent(userId, contentId)) {
            return Result.success("取消点赞成功");
        } else {
            return Result.error("未点赞或取消失败");
        }
    }

    /**
     * 查询当前用户是否已点赞该内容
     */
    @GetMapping("/{contentId}/liked")
    public Result<Boolean> hasLiked(@PathVariable Long contentId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        boolean liked = contentService.hasLiked(userId, contentId);
        return Result.success(liked);
    }
} 