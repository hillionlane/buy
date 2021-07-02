package com.my.buy.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 工具类，判断前台提交的验证码是否符合预期
 * @author hzq
 *
 */
public class CodeUtil 
{
	public static boolean checkVerifyCode(HttpServletRequest request)
	{
	//获取katpcha实际的验证码
	//通过session.getAttribute(key)来获取session中索引名为key的对象，session需要用request.getSession来获取
	String verifyCodeExpected=(String) request.getSession().getAttribute(
			com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
	//获取前端自己输入的验证码
	//HttpServletRequestUtil为spring中用于简化HttpServletRequest的工具类
	//主要作用是将request中名字为verifyCodeActual的数据按照指定类型（String）转化
	String verifyCodeActual=HttpServletRequestUtil.getString(request, "verifyCodeActual");
	if(verifyCodeActual==null||!verifyCodeActual.equals(verifyCodeExpected))
	{
		return false;
	}
	return true;
	}
}

