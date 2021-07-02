package com.my.buy.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.buy.dao.PersonInfoDao;
import com.my.buy.dto.ImageHolder;
import com.my.buy.dto.PersonInfoExecution;
import com.my.buy.entity.PersonInfo;
import com.my.buy.entity.Product;
import com.my.buy.enums.PersonInfoStateEnum;
import com.my.buy.exceptions.PersonInfoOperationException;
import com.my.buy.service.PersonInfoService;
import com.my.buy.util.ImageUtil;
import com.my.buy.util.PageCalculator;
import com.my.buy.util.PathUtil;

@Service
public class PersonInfoServiceImpl implements PersonInfoService {

	@Autowired
	private PersonInfoDao userDao;

	/**
	 * F1:获取用户信息
	 */
	@Override
	public PersonInfo getUserById(long userId) {

		return userDao.queryUserById(userId);
	}

	/**
	 * F2:增加用户
	 */
	@Override
	@Transactional
	public PersonInfoExecution addUser(PersonInfo user,ImageHolder thumbnail) {
		// 空值判断
		if (user != null) 
		{
			try
			{
			user.setUserType(1);
			user.setCreateTime(new Date());
			user.setLastEditTime(new Date());
			//默认为正常用户
			user.setEnableStatus(1);
			int effectedNum = userDao.insertUser(user);
			if (effectedNum < 0) 
			{
				throw new PersonInfoOperationException("增加用户失败！");
			} 
			else 
			{
				if(thumbnail!=null&&thumbnail.getImage()!=null&&thumbnail.getImageName()!=null)
				{
					try
					{
						addThumbnail(user,thumbnail);
					}catch(Exception e)
					{
						throw new PersonInfoOperationException("addUserImgError:"+e.getMessage());
					}
					//更新用户信息，增加用户头像
					effectedNum=userDao.updateUser(user);
					if(effectedNum<=0)
					{
						throw new PersonInfoOperationException("更新用户信息失败！");
					}
				}
				
			}
			}catch(Exception e)
			{
				System.out.println("errMsg:"+e.getMessage());
				throw new PersonInfoOperationException("errMsg:"+e.getMessage());
				
			}
		}
		else
		{
			return new  PersonInfoExecution(PersonInfoStateEnum.NULL_USERINFO);
		}
		 return new PersonInfoExecution(PersonInfoStateEnum.SUCCESS, user);
	}

	/**
	 * F2-1:添加头像
	 * @param user
	 * @param thumbnail
	 */
	private void addThumbnail(PersonInfo user, ImageHolder thumbnail) {
		 String dest=PathUtil.getUserImagePath(user.getUserId());
		 String thumbnailAddr=ImageUtil.generateNormalImg(thumbnail, dest);
		 user.setProfileImg(thumbnailAddr);
	}

	/**
	 * F3:修改用户信息
	 */
	@Override
	public PersonInfoExecution modifyUser(PersonInfo user, ImageHolder thumbnail) {
		if(user!=null&&user.getUserId()!=null)
		{
			try
			{
				if(thumbnail!=null)
				{
					if(thumbnail.getImage()!=null&&thumbnail.getImageName()!=null)
					{
						PersonInfo tempUser=userDao.queryUserById(user.getUserId());
						if(tempUser.getProfileImg()!=null)
						{
							ImageUtil.deleteFileOrPath(tempUser.getProfileImg());
						}
						addThumbnail(user,thumbnail);
					}
				}
				user.setLastEditTime(new Date());
				int effectedNum=userDao.updateUser(user);
				if(effectedNum<=0)
				{
					return new PersonInfoExecution(PersonInfoStateEnum.INNER_ERROR);
				}
				else
				{
					return new PersonInfoExecution(PersonInfoStateEnum.SUCCESS, user);
				}
			}catch(Exception e)
			{
				throw new PersonInfoOperationException("修改用户信息失败！");
			}
		}
		else
		{
			return new PersonInfoExecution(PersonInfoStateEnum.NULL_USERID);
		} 
	}

	/**
	 * F4:通过账户名称和密码获取本地账户信息，用于登录操作验证
	 */
	@Override
	public PersonInfo getUserByNameAndPwd(String userName, String password) {
		 
		return userDao.queryUserByNameAndPwd(userName, password);
	}

	/**
	 * F5:修改用户密码
	 */
	@Override
	@Transactional
	public PersonInfoExecution modifyUserPwd(long userId, String userName, String newPassword, String password,
			Date lastEditTime) {
		//非空判断
		if(userId>0&&userName!=null&&password!=null&&newPassword!=null)
		{
			if(password.equals(newPassword))
			{
				return new PersonInfoExecution(PersonInfoStateEnum.PASSWORD_EQUALS);
			}
			try
			{
				//更新密码，并对新密码做加密
				int effectedNum=userDao.updateUserPwd(userId, userName, newPassword, password, new Date());
				if(effectedNum<=0)
				{
					//中断事务
					throw new PersonInfoOperationException("密码修改失败");
				}
				else
				{
					return new PersonInfoExecution(PersonInfoStateEnum.SUCCESS);
				}
			}catch(Exception e)
			{
				throw new PersonInfoOperationException("密码修改失败"+e.getMessage());
			}
		}
		else
		{
			return new PersonInfoExecution(PersonInfoStateEnum.NULL_USERINFO);
		}
	}

	/**
	 * F6:通過查詢條件獲取用戶列表
	 */
	@Override
	@Transactional
	public PersonInfoExecution getUserList(PersonInfo userCondition, int pageIndex, int pageSize) {
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<PersonInfo> userList=userDao.queryUserList(userCondition, rowIndex, pageSize);
		int count=userDao.queryUserCount(userCondition);
		PersonInfoExecution pe=new PersonInfoExecution();
		if(userList!=null)
		{
			pe.setState(PersonInfoStateEnum.SUCCESS.getState());
			pe.setStateInfo(PersonInfoStateEnum.SUCCESS.getStateInfo());
			pe.setUserList(userList);
			pe.setCount(count);
		}
		else
		{
			pe.setState(PersonInfoStateEnum.INNER_ERROR.getState());
		}
		return pe;
	}

	/**
	 * F7：判断该将要注册用户名是否已存在
	 */
	@Override
	public PersonInfo getUserByName(String userName) { 
		return userDao.queryUserByName(userName);
	} 
}
