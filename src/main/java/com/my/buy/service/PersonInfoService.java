package com.my.buy.service;

import java.util.Date;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import com.my.buy.dao.PersonInfoDao;
import com.my.buy.dto.ImageHolder;
import com.my.buy.dto.PersonInfoExecution;
import com.my.buy.entity.PersonInfo;
import com.my.buy.entity.Product;

public interface PersonInfoService 
{ 
	/**
	 * F1:通过userId获取用户信息
	 * @return
	 */
	PersonInfo getUserById(long userId);
	
	/**
	 * F2:增加用户
	 * @param user
	 * @param thumbnail(頭像)
	 * @return
	 */
	PersonInfoExecution addUser(PersonInfo user,ImageHolder thumbnail);
	
	/**
	 * F3:修改用户信息（不包括密码）
	 * @param user
	 * @param thumbnail
	 * @return
	 */
	PersonInfoExecution modifyUser(PersonInfo user,ImageHolder thumbnail);
	/**
	 * F4:通过账户名称和密码获取本地账户信息，用于登录操作验证
	 */
	PersonInfo getUserByNameAndPwd(String userName,String password);
	
	
	/**
	 * F5:修改用户密码
	 * @param userId
	 * @param userName
	 * @param newPassword
	 * @param password
	 * @param lastEditTime
	 * @return
	 */
	PersonInfoExecution modifyUserPwd(@Param("userId") long userId,@Param("userName")String userName,@Param("newPassword")String newPassword,@Param("password") String password,@Param("lastEditTime")Date lastEditTime);

	/**
	 * F6:通過查詢條件獲取用戶列表
	 * @param productCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	PersonInfoExecution getUserList(@Param("userCondition")PersonInfo userCondition,
			@Param("pageIndex")int pageIndex,@Param("pageSize") int pageSize);
	
	/**
	 * F7:判断该将要注册用户名是否已存在
	 * @param userName
	 * @return
	 */
	PersonInfo getUserByName(String userName);
}
