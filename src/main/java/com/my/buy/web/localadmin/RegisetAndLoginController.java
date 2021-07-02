package com.my.buy.web.localadmin;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.buy.entity.PersonInfo;
import com.my.buy.service.PersonInfoService;
import com.my.buy.util.CodeUtil;
import com.my.buy.util.HttpServletRequestUtil;

@Controller
@RequestMapping(value="/localadmin",method=RequestMethod.GET)
public class RegisetAndLoginController 
{
	@Autowired
	private PersonInfoService userService;
	/**
	 * F1:登录
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="/logincheck",method=RequestMethod.POST)
	@ResponseBody
	private Map<String,Object> loginCheck(HttpServletRequest request) throws UnsupportedEncodingException
	{
		Map<String, Object> modelMap=new HashMap<String,Object>();
		request.setCharacterEncoding("utf-8");
		//获取是否需要进行验证码校验的标识符(一旦输入密码超过三次，就要进行验证码校验)
		boolean  needVerify=HttpServletRequestUtil.getBoolean(request, "needVerify");
		if(needVerify&&!CodeUtil.checkVerifyCode(request))
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码输入错误!");
			return modelMap;
		}
		//获取输入的账号
		String userName=HttpServletRequestUtil.getString(request, "userName");
		//获取输入的密码
		String password=HttpServletRequestUtil.getString(request, "password");
		//非空校验
		if(userName!=null&&password!=null)
		{
			//传入账号和密码去获取平台账号信息
			 PersonInfo user=userService.getUserByNameAndPwd(userName, password);
			if(user!=null)
			{
				//若能获取到账号信息则登录成功
				modelMap.put("success", true);
				modelMap.put("userType", user.getUserType());
				modelMap.put("user", user);
				//同事在session中设置用户信息
				request.getSession().setAttribute("user", user);
			}
			else
			{
				modelMap.put("success", false);
				modelMap.put("errMsg", "用户名和密码均不能为空!");
			}
		}
		return modelMap;
	}
	/**
	 * F2:注销（点击登出按钮时注销session）
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/logout",method=RequestMethod.POST)
	@ResponseBody
	private Map<String,Object> logout(HttpServletRequest request)
	{
		Map<String, Object> modelMap=new HashMap<String,Object>();
		//将用户session置为空
		request.getSession().setAttribute("user", null);
		modelMap.put("success", true);
		return modelMap;
	}
	
	/**
	 * F3:判断用户名是否存在
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/isnameexist",method=RequestMethod.POST)
	@ResponseBody
	private Map<String,Object> isNameExits(HttpServletRequest request)
	{
		Map<String, Object> modelMap=new HashMap<String,Object>();
		String userName=HttpServletRequestUtil.getString(request, "userName");
		PersonInfo user=null;
		if(userName!=null)
		{
			user=userService.getUserByName(userName);
			if(user!=null)
			{
				modelMap.put("success", false);
				
			}
			else
			{
				//若不存在，则返回true
				modelMap.put("success", true);
			}
		}
		else
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入的用户名不能为空！");
		}
		return modelMap;
	}
}
