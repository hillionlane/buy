package com.my.buy.web.frontendadmin;
/**
 * 主页面
 */
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.buy.dto.HeadLineExecution;
import com.my.buy.dto.ProductCategoryExecution;
import com.my.buy.entity.HeadLine;
import com.my.buy.entity.ProductCategory;
import com.my.buy.service.HeadLineService;
import com.my.buy.service.ProductCategoryService;

@Controller
@RequestMapping(value="/frontendadmin")
public class MainPageController 
{
	@Autowired
	private ProductCategoryService productCategoryService;
	@Autowired
	private HeadLineService headLineService;
	/**
	 * F1:初始化前端展示系统的主页信息，包括获取一级商品类别列表以及头条列表
	 * @return
	 */
	@RequestMapping(value="/getmainpageinfo")
	@ResponseBody
	private Map<String,Object> listMainPageInfo()
	{
		Map<String,Object> modelMap=new HashMap<String,Object>();
		try
		{
			//获取一级商品列表
			int productCategoryCount=productCategoryService.getProductCategoryCount(null);
			ProductCategoryExecution pce=productCategoryService.getProductCategoryList(null,1,productCategoryCount);
			modelMap.put("success", true);
			modelMap.put("productCategoryList", pce.getProductCategoryList());
		}catch(Exception e)
		{
			modelMap.put("success", false);
			modelMap.put("获取一级商品类别列表失败", e.getMessage());
		}
		try
		{
			//获取头条列表
			HeadLine headLine=new HeadLine();
			headLine.setEnableStatus(1);
			int headLineCount=headLineService.getHeadLineCount(headLine);
			HeadLineExecution he=headLineService.getHeadLineList(headLine,1,headLineCount); 
			modelMap.put("success", true);
			modelMap.put("headLineList", he.getHeadLineList());
			/*//获取头条列表
			HeadLine headLine=new HeadLine();
			headLine.setEnableStatus(1);
			List<HeadLine> headLineList=headLineService.getHeadLineList(headLine); 
			modelMap.put("success", true);
			modelMap.put("headLineList", headLineList);
		 */
		}catch(Exception e)
		{
			modelMap.put("success", false);
			modelMap.put("获取头条列表失败:", e.getMessage());
		}
		return modelMap;
	}
}
