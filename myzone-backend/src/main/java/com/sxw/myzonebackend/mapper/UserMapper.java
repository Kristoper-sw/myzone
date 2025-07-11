package com.sxw.myzonebackend.mapper;

import com.sxw.myzonebackend.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper {
    
    /**
     * 根据用户名查询用户
     */
    User findByUsername(@Param("username") String username);
    
    /**
     * 根据邮箱查询用户
     */
    User findByEmail(@Param("email") String email);
    
    /**
     * 根据ID查询用户
     */
    User findById(@Param("id") Long id);
    
    /**
     * 插入用户
     */
    int insert(User user);
    
    /**
     * 更新用户
     */
    int update(User user);
    
    /**
     * 根据ID删除用户
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 更新用户密码
     */
    int updatePassword(@Param("id") Long id, @Param("password") String password);
} 