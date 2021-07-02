package com.my.buy.web.headlineadmin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.buy.dto.HeadLineExecution;
import com.my.buy.entity.HeadLine;
import com.my.buy.service.HeadLineService;

@Controller
@RequestMapping(value="/headlinadmin")
public class HeadLineManagementController 
{
	@Autowired
	private HeadLineService headLineService;
	
	/**
	 * F1:获取头条列表
	 * @return
	 */
	@RequestMapping(value="/getheadlinelist")
	@ResponseBody
	private Map<String,Object> getHeadLineList()
	{
		Map<String,Object> modelMap=new HashMap<String,Object>();
		try
		{
			//获取头条列表
			HeadLine headLine=new HeadLine();
			headLine.setEnableStatus(1);
			int headLineCount=headLineService.getHeadLineCount(headLine);
			HeadLineExecution he=headLineService.getHeadLineList(headLine,1,headLineCount); 
			modelMap.put("success", true);
			modelMap.put("headLineList", he.getHeadLineList());
		 
		}catch(Exception e)
		{
			modelMap.put("success", false);
			modelMap.put("获取头条列表失败:", e.getMessage());
		}
		return modelMap;
	}
}
