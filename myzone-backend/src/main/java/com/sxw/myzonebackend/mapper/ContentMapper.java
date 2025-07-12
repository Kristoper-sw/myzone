package com.sxw.myzonebackend.mapper;

import com.sxw.myzonebackend.entity.Content;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 内容数据访问层
 */
@Mapper
public interface ContentMapper {
    
    /**
     * 插入新内容
     */
    @Insert("INSERT INTO content (user_id, content_type, video_path, image_paths, diary, " +
            "latitude, longitude, location, status, like_count, comment_count, create_time, update_time) " +
            "VALUES (#{userId}, #{contentType}, #{videoPath}, #{imagePaths}, #{diary}, " +
            "#{latitude}, #{longitude}, #{location}, #{status}, #{likeCount}, #{commentCount}, " +
            "#{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertContent(Content content);
    
    /**
     * 根据用户ID查询内容列表（分页）
     */
    @Select("SELECT * FROM content WHERE user_id = #{userId} ORDER BY create_time DESC " +
            "LIMIT #{offset}, #{size}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "contentType", column = "content_type"),
        @Result(property = "videoPath", column = "video_path"),
        @Result(property = "imagePaths", column = "image_paths"),
        @Result(property = "diary", column = "diary"),
        @Result(property = "latitude", column = "latitude"),
        @Result(property = "longitude", column = "longitude"),
        @Result(property = "location", column = "location"),
        @Result(property = "status", column = "status"),
        @Result(property = "likeCount", column = "like_count"),
        @Result(property = "commentCount", column = "comment_count"),
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "updateTime", column = "update_time")
    })
    List<Content> selectContentsByUserId(@Param("userId") Long userId, 
                                        @Param("offset") Integer offset, 
                                        @Param("size") Integer size);
    
    /**
     * 根据用户ID查询内容总数
     */
    @Select("SELECT COUNT(*) FROM content WHERE user_id = #{userId}")
    int countContentsByUserId(Long userId);
    
    /**
     * 根据内容ID查询内容详情
     */
    @Select("SELECT * FROM content WHERE id = #{contentId}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "contentType", column = "content_type"),
        @Result(property = "videoPath", column = "video_path"),
        @Result(property = "imagePaths", column = "image_paths"),
        @Result(property = "diary", column = "diary"),
        @Result(property = "latitude", column = "latitude"),
        @Result(property = "longitude", column = "longitude"),
        @Result(property = "location", column = "location"),
        @Result(property = "status", column = "status"),
        @Result(property = "likeCount", column = "like_count"),
        @Result(property = "commentCount", column = "comment_count"),
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "updateTime", column = "update_time")
    })
    Content selectContentById(Long contentId);
    
    /**
     * 删除内容
     */
    @Delete("DELETE FROM content WHERE id = #{contentId} AND user_id = #{userId}")
    int deleteContent(@Param("contentId") Long contentId, @Param("userId") Long userId);
    
    /**
     * 更新内容状态
     */
    @Update("UPDATE content SET status = #{status}, update_time = NOW() " +
            "WHERE id = #{contentId} AND user_id = #{userId}")
    int updateContentStatus(@Param("contentId") Long contentId, 
                           @Param("userId") Long userId, 
                           @Param("status") Integer status);
    
    /**
     * 查询所有已发布的内容（用于地图展示）
     */
    @Select("SELECT c.*, u.username, u.nickname, u.avatar " +
            "FROM content c " +
            "LEFT JOIN user u ON c.user_id = u.id " +
            "WHERE c.status = 1 " +
            "ORDER BY c.create_time DESC")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "contentType", column = "content_type"),
        @Result(property = "videoPath", column = "video_path"),
        @Result(property = "imagePaths", column = "image_paths"),
        @Result(property = "diary", column = "diary"),
        @Result(property = "latitude", column = "latitude"),
        @Result(property = "longitude", column = "longitude"),
        @Result(property = "location", column = "location"),
        @Result(property = "status", column = "status"),
        @Result(property = "likeCount", column = "like_count"),
        @Result(property = "commentCount", column = "comment_count"),
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "updateTime", column = "update_time"),
        @Result(property = "user.userId", column = "user_id"),
        @Result(property = "user.username", column = "username"),
        @Result(property = "user.nickname", column = "nickname"),
        @Result(property = "user.avatar", column = "avatar")
    })
    List<Content> selectAllPublishedContents();
    
    /**
     * 根据区域查询内容
     */
    @Select("SELECT c.*, u.username, u.nickname, u.avatar " +
            "FROM content c " +
            "LEFT JOIN user u ON c.user_id = u.id " +
            "WHERE c.status = 1 " +
            "AND c.latitude BETWEEN #{minLat} AND #{maxLat} " +
            "AND c.longitude BETWEEN #{minLng} AND #{maxLng} " +
            "ORDER BY c.create_time DESC")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "contentType", column = "content_type"),
        @Result(property = "videoPath", column = "video_path"),
        @Result(property = "imagePaths", column = "image_paths"),
        @Result(property = "diary", column = "diary"),
        @Result(property = "latitude", column = "latitude"),
        @Result(property = "longitude", column = "longitude"),
        @Result(property = "location", column = "location"),
        @Result(property = "status", column = "status"),
        @Result(property = "likeCount", column = "like_count"),
        @Result(property = "commentCount", column = "comment_count"),
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "updateTime", column = "update_time"),
        @Result(property = "user.userId", column = "user_id"),
        @Result(property = "user.username", column = "username"),
        @Result(property = "user.nickname", column = "nickname"),
        @Result(property = "user.avatar", column = "avatar")
    })
    List<Content> selectContentsByArea(@Param("minLat") Double minLat, 
                                      @Param("maxLat") Double maxLat,
                                      @Param("minLng") Double minLng, 
                                      @Param("maxLng") Double maxLng);
} 