package com.sxw.myzonebackend.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ContentLike {
    private Long id;
    private Long userId;
    private Long contentId;
    private LocalDateTime createTime;
} 