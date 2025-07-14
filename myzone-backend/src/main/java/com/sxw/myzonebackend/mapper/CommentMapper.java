package com.sxw.myzonebackend.mapper;

import com.sxw.myzonebackend.entity.Comment;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface CommentMapper {
    @Insert("INSERT INTO comment (content_id, user_id, parent_id, content) VALUES (#{contentId}, #{userId}, #{parentId}, #{content})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Comment comment);

    @Select("SELECT c.*, u.id as user_id, u.username, u.nickname, u.avatar FROM comment c LEFT JOIN user u ON c.user_id = u.id WHERE c.content_id = #{contentId} AND c.parent_id IS NULL ORDER BY c.create_time DESC LIMIT #{offset}, #{size}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "contentId", column = "content_id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "parentId", column = "parent_id"),
        @Result(property = "content", column = "content"),
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "user.userId", column = "user_id"),
        @Result(property = "user.username", column = "username"),
        @Result(property = "user.nickname", column = "nickname"),
        @Result(property = "user.avatar", column = "avatar")
    })
    List<Comment> selectTopLevelComments(@Param("contentId") Long contentId, @Param("offset") int offset, @Param("size") int size);

    @Select("SELECT c.*, u.id as user_id, u.username, u.nickname, u.avatar FROM comment c LEFT JOIN user u ON c.user_id = u.id WHERE c.parent_id = #{parentId} ORDER BY c.create_time ASC")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "contentId", column = "content_id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "parentId", column = "parent_id"),
        @Result(property = "content", column = "content"),
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "user.userId", column = "user_id"),
        @Result(property = "user.username", column = "username"),
        @Result(property = "user.nickname", column = "nickname"),
        @Result(property = "user.avatar", column = "avatar")
    })
    List<Comment> selectReplies(@Param("parentId") Long parentId);

    @Select("SELECT COUNT(*) FROM comment WHERE content_id = #{contentId} AND parent_id IS NULL")
    int countTopLevelComments(@Param("contentId") Long contentId);

    @Select("SELECT COUNT(*) FROM comment WHERE parent_id = #{parentId}")
    int countReplies(@Param("parentId") Long parentId);

    /**
     * 统计所有评论（含回复）
     */
    @Select("SELECT COUNT(*) FROM comment WHERE content_id = #{contentId}")
    int countAllComments(@Param("contentId") Long contentId);

    @Select("SELECT * FROM comment WHERE id = #{commentId}")
    Comment selectById(@Param("commentId") Long commentId);

    @Delete("DELETE FROM comment WHERE id = #{commentId}")
    int deleteById(@Param("commentId") Long commentId);

    @Delete("DELETE FROM comment WHERE content_id = #{contentId}")
    int deleteByContentId(@Param("contentId") Long contentId);
} 