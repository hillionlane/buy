package com.my.buy.web.superadmin;

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
import com.my.buy.util.HttpServletRequestUtil;

@Controller
@RequestMapping(value="/superadmin")
public class LoginController 
{
	@Autowired
	private PersonInfoService userService;
	
	/**
	 * F1:登陆操作
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/logincheck", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> loginCheck(HttpServletRequest request) {
		try {
			//设置对客户端请求进行重新编码的编码
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, Object> modelMap = new HashMap<String, Object>();
		String userName = HttpServletRequestUtil.getString(request, "userName");
		String password = HttpServletRequestUtil.getString(request, "password");
		if(userName!=null&&password!=null)
		{
			PersonInfo user=userService.getUserByNameAndPwd(userName, password);
			if(user!=null)
			{
				if(user.getUserType()!=2)
				{
					modelMap.put("success", false);
					modelMap.put("errMsg", "非管理员没有权限访问");
				}
				else
				{
					modelMap.put("success", true);
					request.getSession().setAttribute("user",
							user);
				}
			}
			else
			{
				modelMap.put("success", false);
				modelMap.put("errMsg", "用户名或密码错误");
			}
		}
		else
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", "用户名和密码均不能为空");
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
}
