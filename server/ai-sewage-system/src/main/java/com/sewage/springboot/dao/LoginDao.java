package com.sewage.springboot.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

/**
 * @author: hxy
 * @description: 登录相关dao
 * @date: 2017/10/24 11:02
 */
//@Mapper
public interface LoginDao {
	/**
	 * 根据用户名和密码查询对应的用户
	 */
	//@Select("select * from sys_user where username = #{username} and password = #{password} and delete_status = 1")
	JSONObject getUser(@Param("username") String username, @Param("password") String password);
}
