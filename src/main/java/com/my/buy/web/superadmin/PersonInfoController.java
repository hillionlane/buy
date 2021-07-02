package com.my.buy.web.superadmin;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.buy.dto.ImageHolder;
import com.my.buy.dto.PersonInfoExecution;
import com.my.buy.entity.ConstantForSuperAdmin;
import com.my.buy.entity.PersonInfo;
import com.my.buy.enums.PersonInfoStateEnum;
import com.my.buy.exceptions.PersonInfoOperationException;
import com.my.buy.service.PersonInfoService;
import com.my.buy.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/superadmin")
public class PersonInfoController 
{
	@Autowired
	private PersonInfoService userService;

	/**
	 * F1:获取用户列表
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/listpersonInfos", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> listPersonInfos(HttpServletRequest request) throws UnsupportedEncodingException {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		PersonInfoExecution users = null;
		int pageIndex = HttpServletRequestUtil.getInt(request,
				ConstantForSuperAdmin.PAGE_NO);
		int pageSize = HttpServletRequestUtil.getInt(request,
				ConstantForSuperAdmin.PAGE_SIZE);
		if (pageIndex > 0 && pageSize > 0) {
			try
			{
				PersonInfo userCondition = new PersonInfo();
				int enableStatus = HttpServletRequestUtil.getInt(request,
						"enableStatus");
				if (enableStatus > -1) {
					userCondition.setEnableStatus(enableStatus);
				}
				String userName=HttpServletRequestUtil.getString(request, "userName");
				if(userName!=null)
				{
					userCondition.setUserName(URLDecoder.decode(userName, "UTF-8"));
				}
				users=userService.getUserList(userCondition, pageIndex, pageSize);
			}catch(PersonInfoOperationException e)
			{
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if(users.getUserList()!=null)
			{
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE,
						users.getUserList());
				modelMap.put(ConstantForSuperAdmin.TOTAL,
						users.getCount());
				modelMap.put("success", true);
			}else
			{
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE,
						new ArrayList<PersonInfo>());
				modelMap.put(ConstantForSuperAdmin.TOTAL, 0);
				modelMap.put("success", true);
			}
		}
		else
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", "空的查询信息");
			return modelMap;
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/modifypersonInfo", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyPersonInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long userId = HttpServletRequestUtil.getLong(request, "userId");
		int enableStatus = HttpServletRequestUtil.getInt(request,
				"enableStatus");
		ImageHolder thumbnail=null;
		if (userId > 0 && enableStatus > -1) {
			try
			{
				PersonInfo user =new PersonInfo();
				user.setUserId(userId);
				user.setEnableStatus(enableStatus);
				PersonInfoExecution pe=userService.modifyUser(user, thumbnail);
				if(pe.getState()==PersonInfoStateEnum.SUCCESS.getState())
				{
					modelMap.put("success", true);
				}
				else
				{
					modelMap.put("success", false);
					modelMap.put("errMsg",pe.getStateInfo());
				}
			}catch(PersonInfoOperationException e)
			{
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		}else
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入需要修改的帐号信息");
		} 
		return modelMap; 
	}
}
