package com.my.buy.web.localadmin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.buy.dao.PersonInfoDao;
import com.my.buy.dto.CollectExecution;
import com.my.buy.dto.ProductExecution;
import com.my.buy.entity.Collect;
import com.my.buy.entity.PersonInfo;
import com.my.buy.entity.Product;
import com.my.buy.enums.CollectStateEnum;
import com.my.buy.exceptions.CollectOperationException;
import com.my.buy.service.CollectService;
import com.my.buy.service.PersonInfoService;
import com.my.buy.service.ProductService;
import com.my.buy.util.HttpServletRequestUtil;

@Controller
@RequestMapping(value="/localadmin")
public class LocalOperationController 
{
	@Autowired
	private ProductService productService;
	@Autowired
	private PersonInfoService userService;
	@Autowired
	private CollectService collectService;
	/**
	 * F1:返回当前用户的商品列表（根据查询条件）
	 * 条件:同一商品类别、同一用户、同一区域、模糊查询的名字、上架产品(enableStatus=1)
	 * @param request
	 * @return
	 */
	@SuppressWarnings("null")
	@RequestMapping(value="/getmyproductlist",method={RequestMethod.GET})
	@ResponseBody
	private Map<String, Object> getMyProductList(HttpServletRequest request) throws IOException
	{
		Map<String,Object> modelMap=new HashMap<String,Object>();
		//获取页码
		//试着获取用户的Id
		long userId=HttpServletRequestUtil.getLong(request, "userId");
		if(userId<0)
		{
			PersonInfo user=(PersonInfo) request.getSession().getAttribute("user");
			userId=user.getUserId();
		}
		int pageIndex=HttpServletRequestUtil.getInt(request, "pageIndex");
		//获取一页需要显示的数据条数
		int pageSize=HttpServletRequestUtil.getInt(request, "pageSize");
		//非空判断
		if((pageIndex>-1)&&(pageSize>-1))
		{
			Product productCondition=new Product();
			ProductExecution pe=null;
			productCondition.setPersonInfo(userService.getUserById(userId));
			//试着获取用户的Id 
			if(userId>0)
			{
				pe=productService.getProductList(productCondition, pageIndex, pageSize);
				modelMap.put("success", true);
				modelMap.put("productList", pe.getProductList());
				modelMap.put("count", pe.getCount());
			}
			else
			{
				modelMap.put("success", false);
				modelMap.put("errMsg", pe.getStateInfo());
			}
		}
		else
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex");
		}
		
		return modelMap;
	}
	/**
	 * F2:获取用户收藏列表
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/getcollectionlist",method={RequestMethod.GET})
	@ResponseBody
	private Map<String, Object> getCollectionList(HttpServletRequest request) throws IOException
	{
		Map<String,Object> modelMap=new HashMap<String,Object>();
		PersonInfo user=(PersonInfo) request.getSession().getAttribute("user");
		long userId=user.getUserId();
		int pageIndex=HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize=HttpServletRequestUtil.getInt(request, "pageSize");
		if(userId>0&&pageIndex>0&&pageSize>0)
		{
			try
			{
				CollectExecution ce=collectService.getCollectListByUserId(userId, pageIndex, pageSize);
				if(ce.getState()==CollectStateEnum.SUCCESS.getState())
				{
					modelMap.put("success", true);
					modelMap.put("collectionList", ce.getCollectionList());
					modelMap.put("count", ce.getCount());
					
				}
				else
				{
					modelMap.put("success", false);
					modelMap.put("errMsg", ce.getStateInfo());
				}
			}catch(CollectOperationException e)
			{
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
		}
		else
		{
			modelMap.put("success", false);
			modelMap.put("errMsg","用户Id为空或者分页信息为空");
		}
		return modelMap;
	}
}
