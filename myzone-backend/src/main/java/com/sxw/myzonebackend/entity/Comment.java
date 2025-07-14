package com.sxw.myzonebackend.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Comment {
    private Long id;
    private Long contentId;
    private Long userId;
    private Long parentId;
    private String content;
    private LocalDateTime createTime;
    private User user;
    private Integer replyCount;
    private List<Comment> replies;
} 