package com.sxw.myzonebackend.service;

import com.sxw.myzonebackend.entity.Comment;
import java.util.List;

public interface CommentService {
    void addComment(Long userId, Long contentId, String content, Long parentId);
    List<Comment> getTopLevelComments(Long contentId, int page, int size);
    List<Comment> getReplies(Long parentId);
    int countTopLevelComments(Long contentId);
    int countReplies(Long parentId);
    int countAllComments(Long contentId);
    boolean deleteComment(Long commentId, Long userId);
} 