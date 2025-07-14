package com.sxw.myzonebackend.mapper;

import com.sxw.myzonebackend.entity.ContentLike;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ContentLikeMapper {
    @Insert("INSERT INTO content_like (user_id, content_id) VALUES (#{userId}, #{contentId})")
    int insert(ContentLike like);

    @Delete("DELETE FROM content_like WHERE user_id = #{userId} AND content_id = #{contentId}")
    int delete(@Param("userId") Long userId, @Param("contentId") Long contentId);

    @Delete("DELETE FROM content_like WHERE content_id = #{contentId}")
    int deleteByContentId(@Param("contentId") Long contentId);

    @Select("SELECT COUNT(*) FROM content_like WHERE user_id = #{userId} AND content_id = #{contentId}")
    int exists(@Param("userId") Long userId, @Param("contentId") Long contentId);
} 