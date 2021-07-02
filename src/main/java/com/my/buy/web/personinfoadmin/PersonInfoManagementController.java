package com.my.buy.web.personinfoadmin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.buy.dto.ImageHolder;
import com.my.buy.dto.PersonInfoExecution;
import com.my.buy.entity.PersonInfo;
import com.my.buy.enums.PersonInfoStateEnum;
import com.my.buy.service.PersonInfoService;
import com.my.buy.util.CodeUtil;
import com.my.buy.util.HttpServletRequestUtil;

@Controller
@RequestMapping(value="/personinfoadmin")
public class PersonInfoManagementController {
	@Autowired
	private PersonInfoService userService; 
	
	/**
	 * F1:增加用户
	 * @param request
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@RequestMapping(value="/adduser")
	@ResponseBody
	public Map<String, Object> addProduct(HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException
	{
		Map<String, Object> modelMap=new HashMap<String,Object>();
		ObjectMapper mapper=new ObjectMapper();
		PersonInfo user=null;
		//如果输入的验证码不满足预期
		if(!CodeUtil.checkVerifyCode(request))
		{
			modelMap.put("success",false);
			modelMap.put("errMsg", "验证码输入错误");
			//返回给前端
			return modelMap;
		}
		//获取用户信息
		String userStr=HttpServletRequestUtil.getString(request, "userStr");
		try
		{
			user=mapper.readValue(userStr, PersonInfo.class);
		}
		catch(Exception e)
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		//获取图片流
		CommonsMultipartFile profileImg=null;
		// 文件上传解析器，解析request中的信息;从request的本次会话的上下文获取本次文件上传的内容
		CommonsMultipartResolver commonsMultipartResolver=new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if(commonsMultipartResolver.isMultipart(request))
		{
			// 如果有上传的文件流，将request转换成MultipartHttpServletRequest这个对象,这个对象可以提取出文件流
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			profileImg=(CommonsMultipartFile) multipartHttpServletRequest.getFile("profileImg");
		}
		else
		{
			// 如果没有上传流则报错，因为我们图片必须上传
			modelMap.put("success", false);
			modelMap.put("errMsg", "上传图片不能为空");
			return modelMap;
		}
		//注册用户
		if(user!=null&&user.getArea()!=null)
		{
			PersonInfoExecution pie=null;
			try
			{
				ImageHolder imageHolder=new ImageHolder(profileImg.getOriginalFilename(), profileImg.getInputStream());
				pie=userService.addUser(user, imageHolder);
				if(pie.getState()==PersonInfoStateEnum.SUCCESS.getState())
				{
					modelMap.put("success", true);
				}
				else
				{
					modelMap.put("success", false);
					modelMap.put("errMsg", pie.getStateInfo());
				}
			}catch(Exception e)
			{
				modelMap.put("success",false);
				modelMap.put("errMsg",e.getMessage()); 
			}
		}
		else
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", "必须上传的信息不能为空！");
		}
		return modelMap;
	}
	/**
	 * F2:通过id获取用户信息
	 * @param request
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@RequestMapping(value="/getuserbyid",method={RequestMethod.GET})
	@ResponseBody
	public Map<String, Object> getUserById(HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException
	{
		Map<String, Object> modelMap=new HashMap<String,Object>();
		long userId=HttpServletRequestUtil.getLong(request, "userId");
		PersonInfo user=(PersonInfo) request.getSession().getAttribute("user");
		//我的页面，从session中获取
		if(userId<0)
		{
			userId=user.getUserId();
			if(userId>0)
			{
				/*PersonInfo user=userService.getUserById(userId);*/
				
				modelMap.put("success", true);
				modelMap.put("user", user);
			}
			else
			{
				modelMap.put("success", false);
				modelMap.put("errMsg", "用户ID为空！");
			}
		}
		//他的页面，从地址栏传入userId
		else
		{
				PersonInfo tempUser=userService.getUserById(userId);
				//如果传入的用户Id和当前登陆的用户是同一个用户，则返回true
				if(tempUser.getUserId()==user.getUserId())
				{
					modelMap.put("isme", true);
				}
				modelMap.put("success", true);
				modelMap.put("tempUser", tempUser);
		}
		return modelMap;
	}
	 
	/**
	 * F3:修改用户密码
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/modifyuserpwd",method={RequestMethod.POST})
	@ResponseBody
	public Map<String, Object> modifyUserPwd(HttpServletRequest request) throws UnsupportedEncodingException {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		request.setCharacterEncoding("UTF-8");
		// 验证码校验
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码输入有误");
			return modelMap;
		}
		// 获取账号
		String userName = HttpServletRequestUtil.getString(request, "userName");
		// 获取原密码
		String password = HttpServletRequestUtil.getString(request, "password");
		// 获取新密码
		String newPassword = HttpServletRequestUtil.getString(request, "newPassword");
		// 从session中获取当前用户信息(用户登陆之后会把该用户存储在session中)
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		if (userName != null && password != null && newPassword != null && user != null && user.getUserId() != null) 
		{
			if (password.equals(newPassword)) {
				modelMap.put("success", false);
				modelMap.put("errMsg", "新旧密码一致,无须修改！");
				return modelMap;
			}
			try
			{
				//查看原先账号，看看与输入的账号是否一致，不一致则认为是非法操作
				PersonInfo tempUser=userService.getUserById(user.getUserId());
				if(tempUser==null||!tempUser.getUserName().equals(userName))
				{
					modelMap.put("success", false);
					modelMap.put("errMsg", "输入的账号非本次登录的账号");
					return modelMap;
				}
				//修改平台账号的用户密码
				PersonInfoExecution pie=userService.modifyUserPwd(user.getUserId(), userName,  newPassword,password, new Date());
				if(pie.getState()==PersonInfoStateEnum.SUCCESS.getState())
				{
					//将当前已修改的用户的信息放入session中
					PersonInfo currentUser=userService.getUserById(user.getUserId());
					request.getSession().setAttribute("user", currentUser);
					modelMap.put("success", true);
				}
				else
				{
					modelMap.put("success", false);
					modelMap.put("errMsg", pie.getStateInfo());
				}
			}
			catch(Exception e)
			{
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		}
		else
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入密码!");
		}
		return modelMap;
	}

	 /**
	 * F4:修改用户信息(不包括密码)
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	 @RequestMapping(value="/modifyuserinfo")
	 @ResponseBody
	 public Map<String, Object> modifyUserInfo(HttpServletRequest request) throws UnsupportedEncodingException
	 {
		 
		 Map<String, Object> modelMap = new HashMap<String, Object>();
		 request.setCharacterEncoding("UTF-8");
		 ObjectMapper mapper=new ObjectMapper(); 
		 PersonInfo user=null;
			// 验证码校验
			if (!CodeUtil.checkVerifyCode(request)) 
			{
				modelMap.put("success", false);
				modelMap.put("errMsg", "验证码输入有误");
				return modelMap;
			}
			//获取用户信息
			String userStr=HttpServletRequestUtil.getString(request, "userStr");
			try
			{
				user=mapper.readValue(userStr, PersonInfo.class);
			}
			catch(Exception e)
			{
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			
			//获取图片流
			CommonsMultipartFile profileImg=null;
			// 文件上传解析器，解析request中的信息;从request的本次会话的上下文获取本次文件上传的内容
			CommonsMultipartResolver commonsMultipartResolver=new CommonsMultipartResolver(
					request.getSession().getServletContext());
			if(commonsMultipartResolver.isMultipart(request))
			{
				// 如果有上传的文件流，将request转换成MultipartHttpServletRequest这个对象,这个对象可以提取出文件流
				MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
				profileImg=(CommonsMultipartFile) multipartHttpServletRequest.getFile("profileImg");
			}
			//修改用户信息
			if(user!=null&&user.getArea()!=null)
			{
				//判断用户新修改的用户名在数据库中是否已存在
//				PersonInfo oldUser=userService.getUserById(user.getUserId());
				PersonInfo newUser=userService.getUserByName(user.getUserName());
				if(newUser!=null&&newUser.getUserId()!=user.getUserId())
				{
					modelMap.put("success", false);
					modelMap.put("errMsg", "该用户名已存在,请重新输入！");
					return modelMap;
				}
				PersonInfoExecution pie=null;
				try
				{
					ImageHolder imageHolder=null;
					if(profileImg!=null)
					{
						imageHolder=new ImageHolder(profileImg.getOriginalFilename(), profileImg.getInputStream());
					}
					pie=userService.modifyUser(user, imageHolder);
					if(pie.getState()==PersonInfoStateEnum.SUCCESS.getState())
					{
						//将当前已修改的用户的信息放入session中
						PersonInfo currentUser=userService.getUserById(user.getUserId());
						request.getSession().setAttribute("user", currentUser);
						modelMap.put("success", true);
					}
					else
					{
						modelMap.put("success", false);
						modelMap.put("errMsg", pie.getStateInfo());
					}
				}catch(Exception e)
				{
					modelMap.put("success",false);
					modelMap.put("errMsg",e.getMessage()); 
				}
			}
			else
			{
				modelMap.put("success", false);
				modelMap.put("errMsg", "必须上传的信息不能为空！");
			}
			return modelMap;
	 }
	 
}
