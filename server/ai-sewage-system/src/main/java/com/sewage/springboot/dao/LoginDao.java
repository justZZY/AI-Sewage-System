package com.sewage.springboot.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

//@Mapper
public interface LoginDao {
	/**
	 * 根据用户名和密码查询对应的用户
	 */
	//@Select("select * from sys_user where username = #{username} and password = #{password} and delete_status = 1")
	JSONObject getUser(@Param("username") String username);
}
