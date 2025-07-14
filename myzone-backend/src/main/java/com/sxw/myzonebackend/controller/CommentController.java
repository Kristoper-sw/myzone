package com.sxw.myzonebackend.controller;

import com.sxw.myzonebackend.common.Result;
import com.sxw.myzonebackend.dto.PageResponse;
import com.sxw.myzonebackend.entity.Comment;
import com.sxw.myzonebackend.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/add")
    public Result<String> addComment(@RequestBody CommentAddRequest req, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        commentService.addComment(userId, req.getContentId(), req.getContent(), req.getParentId());
        return Result.success("评论成功");
    }

    @GetMapping("/list")
    public Result<PageResponse<Comment>> getComments(@RequestParam Long contentId,
                                                    @RequestParam(defaultValue = "1") int page,
                                                    @RequestParam(defaultValue = "5") int size) {
        List<Comment> comments = commentService.getTopLevelComments(contentId, page, size);
        int total = commentService.countAllComments(contentId);
        return Result.success(new PageResponse<>(comments, (long) total, page, size));
    }

    @GetMapping("/replies")
    public Result<List<Comment>> getReplies(@RequestParam Long parentId) {
        List<Comment> replies = commentService.getReplies(parentId);
        return Result.success(replies);
    }

    @DeleteMapping("/{commentId}")
    public Result<String> deleteComment(@PathVariable Long commentId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        boolean success = commentService.deleteComment(commentId, userId);
        if (success) {
            return Result.success("评论删除成功");
        } else {
            return Result.error("无权限或评论不存在");
        }
    }

    // DTO
    public static class CommentAddRequest {
        private Long contentId;
        private String content;
        private Long parentId;
        public Long getContentId() { return contentId; }
        public void setContentId(Long contentId) { this.contentId = contentId; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public Long getParentId() { return parentId; }
        public void setParentId(Long parentId) { this.parentId = parentId; }
    }
} 