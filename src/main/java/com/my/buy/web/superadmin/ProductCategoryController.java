package com.my.buy.web.superadmin;

import java.io.IOException;
import java.net.URLDecoder;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.buy.dto.ImageHolder;
import com.my.buy.dto.ProductCategoryExecution;
import com.my.buy.entity.ConstantForSuperAdmin;
import com.my.buy.entity.ProductCategory;
import com.my.buy.enums.ProductCategoryStateEnum;
import com.my.buy.exceptions.ProductCategoryOperationException;
import com.my.buy.service.ProductCategoryService;
import com.my.buy.util.HttpServletRequestUtil;

@Controller
@RequestMapping(value="/superadmin")
public class ProductCategoryController 
{
	@Autowired ProductCategoryService productCategoryService;
	
	/**
	 * 获取一级和二级列表
	 */
	@RequestMapping(value="/listallproductcategorys",method=RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> listAllProductCategorys(HttpServletRequest request)
	{
		Map<String, Object> modelMap = new HashMap<String, Object>(); 
		List<ProductCategory> listAll=new ArrayList<ProductCategory>();
		int pageIndex = HttpServletRequestUtil.getInt(request,
				ConstantForSuperAdmin.PAGE_NO);
		int pageSize = HttpServletRequestUtil.getInt(request,
				ConstantForSuperAdmin.PAGE_SIZE); 
		if (pageIndex > 0 && pageSize > 0) 
		{ 
			try
			{ 
				int count=productCategoryService.getAllProductCategoryCount();
				listAll=productCategoryService.getAllProductCategoryList(pageIndex, pageSize);
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, listAll);
				modelMap.put(ConstantForSuperAdmin.TOTAL, count); 
			}catch(ProductCategoryOperationException e)
			{
				e.printStackTrace();
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			} 
		}
		else
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", "空的查询信息");  
		}
		return modelMap; 
	}
	/**
	 * F1:查询二级商品类别列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/listproductcategorys",method=RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> listProductCategorys(HttpServletRequest request)
	{
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<ProductCategory> list=new ArrayList<ProductCategory>();
		int pageIndex = HttpServletRequestUtil.getInt(request,
				ConstantForSuperAdmin.PAGE_NO);
		int pageSize = HttpServletRequestUtil.getInt(request,
				ConstantForSuperAdmin.PAGE_SIZE); 
		ProductCategory productCategoryCondition=new ProductCategory();
		//试着获取一级类别Id
		long productCategoryId=HttpServletRequestUtil.getLong(request, "productCategoryId");
		//获取某一级类别下的所有二级类别
		if(productCategoryId>0)
		{
			if (pageIndex > 0 && pageSize > 0) 
			{
				try
				{   
				    productCategoryCondition.setProductCategoryId(productCategoryId);
				    ProductCategory parentCategory=new ProductCategory(); 
				    parentCategory=productCategoryService.getProductCategoryById(productCategoryId);
				    productCategoryCondition.setParent(parentCategory);
					  
					/*int productCategoryCount=productCategoryService.getProductCategoryCount(productCategoryCondition);*/
					ProductCategoryExecution pce=productCategoryService.getProductCategoryList(productCategoryCondition, pageIndex, pageSize);
					list=pce.getProductCategoryList();
					modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, list);
					modelMap.put(ConstantForSuperAdmin.TOTAL, list.size());
				}catch(ProductCategoryOperationException e)
				{
					e.printStackTrace();
					modelMap.put("success", false);
					modelMap.put("errMsg", e.toString());
					return modelMap;
				}
			}
			else
			{
				modelMap.put("success", false);
				modelMap.put("errMsg", "空的查询信息"); 
			}
	  }
	  //若未传入productCategoryId则获取全部的二级类别
	  else
	  {
		 int productCategoryCount=productCategoryService.getProductCategoryCount(productCategoryCondition);
		 ProductCategoryExecution pce=productCategoryService.getProductCategoryList(productCategoryCondition, 1, productCategoryCount);
		 list=pce.getProductCategoryList();
		 modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, list);
		 modelMap.put(ConstantForSuperAdmin.TOTAL, list.size());
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
				modelMap.put("list",list);
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
	 * F3:增加商品类别
	 * @param request
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@RequestMapping(value = "/addproductcategory", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addProductCategory(HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		ProductCategory productCategory=null;
		String productCategoryStr=HttpServletRequestUtil.getString(request, "productCategoryStr");
		ImageHolder thumbnail = null;
		MultipartHttpServletRequest multipartHttpServletRequest = null;
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		try
		{
			productCategory=mapper.readValue(productCategoryStr,ProductCategory.class);
		}catch(ProductCategoryOperationException e)
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		if(productCategory!=null&&multipartResolver.isMultipart(request))
		{
			try
			{
				// 转换成多部分request
				multipartHttpServletRequest = (MultipartHttpServletRequest) request;
				// 取出缩略图并构建ImageHolder对象
				CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("productCategoryManagementAdd_productCategoryImg");
				thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
				productCategory.setProductCategoryName((productCategory
						.getProductCategoryName()==null)?null:(URLDecoder
								.decode(productCategory.getProductCategoryName(),"UTF-8")));
				productCategory.setProductCategoryDesc((productCategory
						.getProductCategoryDesc()==null)?null:(URLDecoder
								.decode(productCategory.getProductCategoryDesc(),"UTF-8")));
				ProductCategoryExecution pe=productCategoryService.addProductCategory(productCategory, thumbnail);
				if(pe.getState()==ProductCategoryStateEnum.SUCCESS.getState())
				{
					modelMap.put("success", true);
				}
				else
				{
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			}
			catch(ProductCategoryOperationException e)
			{
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		}
		else
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入商品类别信息");
		} 
		return modelMap;
	}
	
	/**
	 * F3:修改productCategory
	 * @param request
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@RequestMapping(value = "/modifyproductcategory", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyProductCategory(HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		ProductCategory productCategory=null;
		ImageHolder thumbnail=null;
		String productCategoryStr=HttpServletRequestUtil.getString(request, "productCategoryStr");
		MultipartHttpServletRequest multipartHttpServletRequest = null;
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		try
		{	
			productCategory=mapper.readValue(productCategoryStr,ProductCategory.class);
		}
		catch(ProductCategoryOperationException e)
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		if(productCategory!=null&&productCategory.getProductCategoryId()!=null)
		{
			try
			{
				productCategory.setProductCategoryName((productCategory
						.getProductCategoryName()==null)?null:(URLDecoder
								.decode(productCategory.getProductCategoryName(),"UTF-8")));
				productCategory.setProductCategoryDesc((productCategory
						.getProductCategoryDesc()==null)?null:(URLDecoder
								.decode(productCategory.getProductCategoryDesc(),"UTF-8")));
				// 转换成多部分request
			 	multipartHttpServletRequest = (MultipartHttpServletRequest) request;
				// 取出缩略图并构建ImageHolder对象
				CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartHttpServletRequest
						.getFile("productCategoryManagementEdit_productCategoryImg");
				if(thumbnailFile!=null)
				{
					thumbnail=new ImageHolder(thumbnailFile.getOriginalFilename(),thumbnailFile.getInputStream());
				}
				ProductCategoryExecution pe=productCategoryService.modifyProductCategory(productCategory, thumbnail);
				if(pe.getState()==ProductCategoryStateEnum.SUCCESS.getState())
				{
					modelMap.put("success", true);
				}
				else
				{
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			}
			catch(ProductCategoryOperationException e)
			{
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		}
		else
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入商品类别信息信息");
		}
		return modelMap;
	}
	
	/**
	 * F4:删除商品类别信息
	 * @param productCategoryId
	 * @return
	 */
	@RequestMapping(value = "/removeproductcategory", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> removeProductCategroy(Long productCategoryId) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if(productCategoryId!=null&&productCategoryId>0)
		{
			try
			{
			ProductCategoryExecution pce=productCategoryService.removeProductCategory(productCategoryId);
			if(pce.getState()==ProductCategoryStateEnum.SUCCESS.getState())
			{
				modelMap.put("success", true);
			}
			else
			{
				modelMap.put("success", false);
				modelMap.put("errMsg", pce.getStateInfo());
			}
			}catch(ProductCategoryOperationException e)
			{
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		}
		else
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入商品信息");
		}
		return modelMap;
	}
	
	/**
	 * F5:批量删除商品类别
	 * @param productCategoryIdListStr
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@RequestMapping(value = "/removeproductcategories", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> removeProductCategories(String productCategoryIdListStr) throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		//把json解析成list
		JavaType javaType = mapper.getTypeFactory().constructParametricType(
				ArrayList.class, Long.class);
		List<Long> productCategoryIdList=null;
		try
		{
			productCategoryIdList=mapper.readValue(productCategoryIdListStr, javaType);
		}catch(ProductCategoryOperationException e)
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		if(productCategoryIdList!=null&&productCategoryIdList.size()>0)
		{
			try
			{
				ProductCategoryExecution pce=productCategoryService.batchRemoveProductCategory(productCategoryIdList);
				if(pce.getState()==ProductCategoryStateEnum.SUCCESS.getState())
				{
					modelMap.put("success", true);
				}else
				{
					modelMap.put("success", false);
					modelMap.put("errMsg", pce.getStateInfo());
				}
			}catch(ProductCategoryOperationException e)
			{
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		}
		else
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入商品类别信息");
		}
		return modelMap;
	}
}
