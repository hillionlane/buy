package com.my.buy.dao;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.my.buy.BaseTest;
import com.my.buy.entity.Area;
import com.my.buy.entity.PersonInfo;

public class PersonInfoDaoTest extends BaseTest
{
	@Autowired
	private PersonInfoDao userDao;
	@Autowired
	private AreaDao areaDao;
	@Test  
	@Ignore
	public void testQueryUserById()
	{
		PersonInfo user=userDao.queryUserById(1L); 
		System.out.println(user.getArea().getAreaName());
	}
	
	@Test
	@Ignore
	public void testInsertUser()
	{
		PersonInfo user=new PersonInfo();
		user.setUserName("囷囷");
		user.setUserDesc("爱吃爱玩爱睡觉的90后颓废少女"); 
		user.setUserAddr("10栋313室");
		user.setUserGender("女");
		user.setUserPhone("15879166155");
		user.setUserType(1);
		user.setEnableStatus(1);
		Area area=areaDao.queryAreaById(2L);
		user.setArea(area);
		int effectedNum=userDao.insertUser(user);
		assertEquals(1,effectedNum);
	}
	
	@Test
	@Ignore
	public void testUpdateUser()
	{
		PersonInfo user=new PersonInfo();
		user.setUserId(12L);
		user.setUserName("小囷囷");
		user.setUserDesc("爱吃爱玩爱睡觉的90后颓废少女小囷囷"); 
		user.setUserAddr("10栋313室啊");
		user.setUserGender("女");
		user.setUserPhone("15879166155");
		user.setUserType(1);
		user.setEnableStatus(1);
		Area area=areaDao.queryAreaById(2L);
		user.setArea(area);
		int effectedNum=userDao.updateUser(user);
		assertEquals(1,effectedNum);
	}
	
	@Test 
	@Ignore
	public void testQueryUserByNameAndId()
	{
		
		PersonInfo user=userDao.queryUserByNameAndPwd("1", "1");
		System.out.println(user.getArea().getAreaName());
	}
	@Test
	@Ignore
	public void testupdateUserPwd()
	{
		PersonInfo user=new PersonInfo();
		PersonInfo user1=userDao.queryUserById(14L);
		user.setUserName("111");
		user.setPassword("111");
		user.setLastEditTime(new Date());
		
		int effectedNum=userDao.updateUserPwd(user1.getUserId(), user1.getUserName(), user.getPassword(),user1.getPassword(), new Date());
		assertEquals(1,effectedNum);
	}
	
	@Test 
	public void testqueryUserListAndCount()
	{
		PersonInfo userCondition=new PersonInfo();
//		userCondition.setUserName("小囷囷");
		userCondition.setEnableStatus(1);
		List<PersonInfo> userList=userDao.queryUserList(userCondition, 0,4);
		
//		int count=userDao.queryUserCount(userCondition, 0, 4);
		System.out.println(userList.size());
	}
	@Test
	@Ignore
	public void testQueryUserByName()
	{
		PersonInfo effectedNum=userDao.queryUserByName("小囷囷");
		System.out.println(effectedNum.getUserName());
	}
}
