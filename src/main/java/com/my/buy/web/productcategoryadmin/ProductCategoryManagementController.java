package com.my.buy.web.productcategoryadmin;
 
import java.util.ArrayList;
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
import com.my.buy.entity.ProductCategory;
import com.my.buy.exceptions.ProductCategoryOperationException;
import com.my.buy.service.ProductCategoryService;
import com.my.buy.util.HttpServletRequestUtil;
 

@Controller
@RequestMapping(value="/productcategoryadmin",method={RequestMethod.GET})
public class ProductCategoryManagementController 
{
	@Autowired
	private ProductCategoryService productCategoryService;
	
	/**
	 * F1:获取商品类别列表(所有二级商品列表)
	 */
	@RequestMapping(value="/getproductcategorylist")
	@ResponseBody
	private Map<String, Object> getProductCategoryList(HttpServletRequest request)
	{
		Map<String, Object> modelMap=new HashMap<String, Object>();
		List<ProductCategory> productCategoryList=null;
		 try
		 {
			ProductCategory productCategoryCondition=new ProductCategory();
			int productCategoryCount=productCategoryService.getProductCategoryCount(productCategoryCondition);
			ProductCategoryExecution pce=productCategoryService.getProductCategoryList(productCategoryCondition, 1, productCategoryCount);
			productCategoryList=pce.getProductCategoryList();
			modelMap.put("success", true);
			modelMap.put("productCategoryList",productCategoryList );
		}
		catch(Exception e)
		{
			modelMap.put("success", false);
			modelMap.put("获取商品类别列表失败", e.getMessage());
		}
		return modelMap;
	}
	/**
	 * F2:查询一级类别列表
	 * @return
	 */
	@RequestMapping(value = "/list1stlevelproductcategorys", method = RequestMethod.POST)
	@ResponseBody
	private  Map<String, Object> list1stlevelproductcategorys(HttpServletRequest request)
	{
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<ProductCategory> list=new ArrayList<ProductCategory>();
		 
			try
			{ 
				int productCategoryCount=productCategoryService.getProductCategoryCount(null);
				ProductCategoryExecution pce=productCategoryService.getProductCategoryList(null, 1, productCategoryCount);
				list=pce.getProductCategoryList();
				modelMap.put("success", true);
				modelMap.put("productCategory1stList",list);
			}catch(ProductCategoryOperationException e)
			{
				e.printStackTrace();
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		 
		return modelMap;
	}
	
	/**
	 * F3：查询指定父类下的所有子类（二级类别列表）
	 */
	@RequestMapping(value = "/list2stlevelproductcategorys", method = RequestMethod.POST)
	@ResponseBody
	private  Map<String, Object> list2stlevelproductcategorys(HttpServletRequest request)
	{
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<ProductCategory> list=new ArrayList<ProductCategory>();
		ProductCategory productCategoryCondition=new ProductCategory();
		//试着获取一级类别Id
		long parentId=HttpServletRequestUtil.getLong(request, "parentId");
		//获取某一级类别下的所有二级类别
			try
			{ 
				productCategoryCondition.setProductCategoryId(parentId);
			    ProductCategory parentCategory=new ProductCategory(); 
			    parentCategory=productCategoryService.getProductCategoryById(parentId);
			    productCategoryCondition.setParent(parentCategory);
			    int productCategoryCount=productCategoryService.getProductCategoryCount(productCategoryCondition);
			    ProductCategoryExecution pce=productCategoryService.getProductCategoryList(productCategoryCondition, 1, productCategoryCount);
				
				list=pce.getProductCategoryList();
				modelMap.put("success", true);
				modelMap.put("productCategory2stList",list);
			}catch(ProductCategoryOperationException e)
			{ 
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		return modelMap;
	}
	
	/**
	 * F4:通过id获取商品类别
	 */
	@RequestMapping(value = "/getproductcategorybyid", method = RequestMethod.POST)
	@ResponseBody
	private  Map<String, Object> getProductCategoryById(HttpServletRequest request)
	{
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//获取该商品类别Id
		long productCategoryId=HttpServletRequestUtil.getLong(request, "productCategoryId");
		try
		{
			
			ProductCategory productCategory=productCategoryService.getProductCategoryById(productCategoryId);
			modelMap.put("success", true);
			modelMap.put("productCategory",productCategory);
		}catch(ProductCategoryOperationException e)
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		return modelMap;
	}
	
}
 
