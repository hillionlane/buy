package com.my.buy.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.my.buy.entity.PersonInfo;
import com.my.buy.entity.Product;
import com.mysql.fabric.xmlrpc.base.Data;

public interface PersonInfoDao 
{
	/**
	 * F1:通过userId获取用户信息 
	 * @return
	 */
	PersonInfo queryUserById(long userId);
	
	/**
	 * F2:增加用戶
	 * @return
	 */
	int insertUser(PersonInfo user);
	
	/**
	 * F3:修改用户信息(不包括修改密码)
	 * @param user
	 * @return
	 */
	int updateUser(PersonInfo user);
	
	/**
	 * F4:通过密码和用户名获取唯一用户（用于用户登录）
	 * @param userName
	 * @param password
	 * @return
	 */
	PersonInfo queryUserByNameAndPwd(@Param("userName")String userName,@Param("password")String password);
	
	/**
	 * F5:更新用户密码
	 * @param userId
	 * @param userName
	 * @param password
	 * @return
	 */
	int updateUserPwd(@Param("userId") long userId,@Param("userName")String userName,@Param("newPassword")String newPassword,@Param("password") String password,@Param("lastEditTime")Date lastEditTime);
	
	/**
	 * F6:分页获取用户列表
	 * @param userCondition
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	List<PersonInfo> queryUserList(@Param("userCondition")PersonInfo userCondition,
			@Param("rowIndex")int rowIndex,@Param("pageSize") int pageSize);
	
	/**
	 * F7:获取用户列表总数
	 * @param userCondition
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	int queryUserCount(@Param("userCondition")PersonInfo userCondition);
	
	/**
	 * F8:判断用户名是否已存在
	 * @param userName
	 * @return
	 */
	PersonInfo queryUserByName(String userName);
}
