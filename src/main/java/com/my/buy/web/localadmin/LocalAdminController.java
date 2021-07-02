package com.my.buy.web.localadmin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.my.buy.entity.PersonInfo;

@Controller
@RequestMapping(value = "/localadmin", method = { RequestMethod.GET })
public class LocalAdminController 
{
		// 用户注册页面
		@RequestMapping(value = "/register")
		public String goRegister( ) {
	
			return "local/register";
		}
		//登录页面
		@RequestMapping(value="/login")
		public String goLogin()
		{
			return "local/login";
		}
		//修改用户密码页面
		@RequestMapping(value="/modifyuserpwd")
		public String modifyUserPwd()
		{
			return "local/changepwd";
		}
		//修改用户信息页面
		@RequestMapping(value="/modifyuserinfo")
		public String modifyUserInfo()
		{
			return "local/modifyuserinfo";
		}
		//我的发布页面
		@RequestMapping(value="/myproducts")
		public String getProductByUserId()
		{
			return "local/myproducts";
		}
		//他的页面
		@RequestMapping(value="/hispage")
		public String getHisPage()
		{
			return "frontend/hispage";
		}
		//去到我的收藏页面
		@RequestMapping(value="showmycollection")
		public String showMyCollection()
		{
			return "local/mycollection";
		}
}
