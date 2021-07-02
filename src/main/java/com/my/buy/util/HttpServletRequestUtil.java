package com.my.buy.util;

import javax.servlet.http.HttpServletRequest;


//工具类，处理http传来的request参数
public class HttpServletRequestUtil {
	// 将request中传来的参数key，转换为整型
	public static int getInt(HttpServletRequest request, String key) {
		try {
			return Integer.decode(request.getParameter(key));
		}
		// 如转换失败返回-1
		catch (Exception e) {
			return -1;
		}
	}

	// 将request中传来的参数key，转换为长整型
	public static long getLong(HttpServletRequest request, String key) {
		try {
			return Long.valueOf(request.getParameter(key));
		}
		// 如转换失败返回-1
		catch (Exception e) {
			return -1;
		}
	}

	// 将request中传来的参数key，转换为double类型
	public static Double getDouble(HttpServletRequest request, String key) {
		try {
			return Double.valueOf(request.getParameter(key));
		}
		// 如转换失败返回-1
		catch (Exception e) {
			return -1d;
		}
	}

	// 将request中传来的参数key，转换为boolean类型
	public static boolean getBoolean(HttpServletRequest request, String key) {
		try {
			return Boolean.valueOf(request.getParameter(key));
		}
		// 如转换失败返回-1
		catch (Exception e) {
			return false;
		}
	}

	// 将request中传来的参数key，转换为String类型
	public static String getString(HttpServletRequest request, String key) {
		try {
			String result = request.getParameter(key);
			if (result != null) {
				// result不为空将其左右的空格去除
				result = result.trim();
			}
			if ("".equals(result)) {
				result = null;
			}
			return result;
		} catch (Exception e) {
			return null;
		}
	}
}
