package com.sxw.myzonebackend.service.impl;

import com.sxw.myzonebackend.entity.Comment;
import com.sxw.myzonebackend.mapper.CommentMapper;
import com.sxw.myzonebackend.mapper.ContentMapper;
import com.sxw.myzonebackend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private ContentMapper contentMapper;

    @Override
    public void addComment(Long userId, Long contentId, String content, Long parentId) {
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setContentId(contentId);
        comment.setContent(content);
        comment.setParentId(parentId);
        commentMapper.insert(comment);
        // 新增：更新内容评论数
        contentMapper.updateCommentCount(contentId, 1);
    }

    @Override
    public List<Comment> getTopLevelComments(Long contentId, int page, int size) {
        int offset = (page - 1) * size;
        List<Comment> comments = commentMapper.selectTopLevelComments(contentId, offset, size);
        for (Comment comment : comments) {
            int replyCount = commentMapper.countReplies(comment.getId());
            comment.setReplyCount(replyCount);
        }
        return comments;
    }

    @Override
    public List<Comment> getReplies(Long parentId) {
        return commentMapper.selectReplies(parentId);
    }

    @Override
    public int countTopLevelComments(Long contentId) {
        return commentMapper.countTopLevelComments(contentId);
    }

    @Override
    public int countReplies(Long parentId) {
        return commentMapper.countReplies(parentId);
    }

    @Override
    public int countAllComments(Long contentId) {
        return commentMapper.countAllComments(contentId);
    }

    @Override
    @Transactional
    public boolean deleteComment(Long commentId, Long userId) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null || !comment.getUserId().equals(userId)) {
            return false;
        }
        int deleted = commentMapper.deleteById(commentId);
        if (deleted > 0) {
            contentMapper.updateCommentCount(comment.getContentId(), -1);
            return true;
        }
        return false;
    }
} 