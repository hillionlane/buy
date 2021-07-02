package com.my.buy.web.frontendadmin;
/**
 * 从主页面进入商品列表页
 * @author hzq
 *
 */
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.buy.dto.ProductCategoryExecution;
import com.my.buy.dto.ProductExecution;
import com.my.buy.entity.Area;
import com.my.buy.entity.PersonInfo;
import com.my.buy.entity.Product;
import com.my.buy.entity.ProductCategory;
import com.my.buy.service.AreaService;
import com.my.buy.service.ProductCategoryService;
import com.my.buy.service.ProductService;
import com.my.buy.util.HttpServletRequestUtil;

@Controller
@RequestMapping(value="/frontendadmin")
public class ProductListController 
{
	@Autowired
	private ProductCategoryService productCategoryService; 
	@Autowired
	private AreaService areaService;
	
	/**
	 * F1:返回商品类别列表(某一级列表下的二级列表)+区域列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/listproductpageinfo",method={RequestMethod.GET})
	@ResponseBody
	private Map<String, Object> getProductCategoryList(HttpServletRequest request)
	{
		Map<String, Object> modelMap=new HashMap<String, Object>();
		//试着从前端获取parentId
		long parentId=HttpServletRequestUtil.getLong(request, "parentId");
//		List<ProductCategory> productCategoryList=null;
		ProductCategoryExecution pce=null;
		int productCategoryCount=0;
		if(parentId!=-1)
		{
			try{
			ProductCategory productCategoryCondition=new ProductCategory();
			ProductCategory parent=new ProductCategory();
			parent.setProductCategoryId(parentId);
			productCategoryCondition.setParent(parent);
			productCategoryCount=productCategoryService.getProductCategoryCount(productCategoryCondition);
			pce=productCategoryService.getProductCategoryList(productCategoryCondition,1,productCategoryCount);
			}
			catch(Exception e)
			{
				modelMap.put("success", false);
				modelMap.put("获取商品类别列表失败", e.getMessage());
			}
		}
		else
		{

			//若parentId不存在，则取出一级productCategory列表
			try
			{
				productCategoryCount=productCategoryService.getProductCategoryCount(null);
				pce=productCategoryService.getProductCategoryList(null,1,productCategoryCount);
			}catch(Exception e)
			{
				modelMap.put("success", false);
				modelMap.put("获取商品类别列表失败", e.getMessage());
			}
		}
		modelMap.put("productCategoryList",pce.getProductCategoryList());
		//获取区域列表信息
		try
		{
			List<Area> areaList=areaService.getAreaList();
			modelMap.put("success", true);
			modelMap.put("areaList", areaList);
		}catch(Exception e)
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
	 
}
