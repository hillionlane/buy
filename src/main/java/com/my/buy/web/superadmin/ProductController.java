package com.my.buy.web.superadmin;

import java.io.UnsupportedEncodingException;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.buy.dto.ImageHolder;
import com.my.buy.dto.ProductExecution;
import com.my.buy.entity.Area;
import com.my.buy.entity.ConstantForSuperAdmin;
import com.my.buy.entity.PersonInfo;
import com.my.buy.entity.Product;
import com.my.buy.entity.ProductCategory;
import com.my.buy.enums.ProductStateEnum;
import com.my.buy.exceptions.ProductOperationException;
import com.my.buy.service.ProductCategoryService;
import com.my.buy.service.ProductService;
import com.my.buy.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/superadmin")
public class ProductController 
{
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductCategoryService productCategoryService;
	
	
	/**
	 * F1:根据商品信息获取商品列表
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="/listproducts",method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> listProducts(HttpServletRequest request) throws UnsupportedEncodingException
	{
		Map<String, Object> modelMap = new HashMap<String, Object>();
		int pageIndex = HttpServletRequestUtil.getInt(request,
				ConstantForSuperAdmin.PAGE_NO);
		int pageSize = HttpServletRequestUtil.getInt(request,
				ConstantForSuperAdmin.PAGE_SIZE); 
		if (pageIndex > 0 && pageSize > 0) 
		{
			Product productCondition=new Product();
			ProductExecution pe=null;
			//试着获取状态值
			int enableStatus = HttpServletRequestUtil.getInt(request,"enableStatus");
			//试着获取一级类别Id
			long parentId=HttpServletRequestUtil.getLong(request, "parentId");
			//试着获取二级类别Id
			long productCategoryId=HttpServletRequestUtil.getLong(request, "productCategoryId");
			//试着获取区域Id
			long areaId=HttpServletRequestUtil.getLong(request, "areaId");
			//试着获取模糊查询的名字
			String productName=HttpServletRequestUtil.getString(request, "productName");
			//试着获取用户的Id
			long userId=HttpServletRequestUtil.getLong(request, "userId");
			//获取组合之后的查询条件
			if( parentId>0||productCategoryId>0||areaId>0||productName!=null||userId>0||enableStatus>=0)
			{
				productCondition= compactProductCondtionForSearch( parentId, productCategoryId,areaId,productName,userId,enableStatus);
			}
			//根据查询调价和分页信息获取店铺列表，并返回总数
			try
			{
				pe=productService.getProductList(productCondition, pageIndex, pageSize);
				modelMap.put("success", true);
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, pe.getProductList());
				modelMap.put(ConstantForSuperAdmin.TOTAL, pe.getCount());
			}catch(Exception e)
			{
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
	
	private Product compactProductCondtionForSearch( long parentId,  long productCategoryId, long areaId,
			String productName, long userId,int enableStatus) throws UnsupportedEncodingException {
		Product productCondtion=new Product();
		 if(enableStatus>=0)
		 {
			 productCondtion.setEnableSatus(enableStatus);
		 }
		 if(parentId>0)
		 {
			 //获取某一一级类别下的所有商品
			 ProductCategory parent=new ProductCategory();
			 parent.setProductCategoryId(parentId);
			 ProductCategory child=new ProductCategory();
			 child.setParent(parent);
			 productCondtion.setProductCategory(child);
		 }
		 if(productCategoryId>0)
		 {
			 //获取某个指定类别(二级)下的商品列表
			 ProductCategory productCategory=new ProductCategory();
			 productCategory.setProductCategoryId(productCategoryId);
			 productCondtion.setProductCategory(productCategory);
		 }
		 if(areaId>0)
		 {
			 //获取某个区域的所有商品
			 Area area=new Area();
			 area.setAreaId(areaId);
			 productCondtion.setArea(area);
		 }
		 if(productName!=null)
		 {
			 //获取模糊查询的商品名称
			 productCondtion.setProductName(URLDecoder.decode
						 (productName,"UTF-8")); 
		 }
		 if(userId>0)
		 {
			 //获取某个用户发布的商品列表
			 PersonInfo user=new PersonInfo();
			 user.setUserId(userId);
			 productCondtion.setPersonInfo(user);
		 }
		 return productCondtion;
	}
	/**
	 * F2:通过productId获取商品
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/searchproductbyid", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> searchProductById(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Product product=null;
		int pageIndex = HttpServletRequestUtil.getInt(request,
				ConstantForSuperAdmin.PAGE_NO);
		int pageSize = HttpServletRequestUtil.getInt(request,
				ConstantForSuperAdmin.PAGE_SIZE);
		long productId=HttpServletRequestUtil.getLong(request, "productId");
		if (pageIndex > 0 && pageSize > 0 && productId > 0) 
		{
			try
			{
				product=productService.getProductById(productId);
			}catch(Exception e)
			{
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if(product!=null)
			{
				List<Product> productList=new ArrayList<Product>();
				productList.add(product);
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, productList);
				modelMap.put(ConstantForSuperAdmin.TOTAL, 1);
				modelMap.put("success", true);
			}else
			{
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE,
						new ArrayList<Product>());
				modelMap.put(ConstantForSuperAdmin.TOTAL, 0);
				modelMap.put("success", true);
			}
			return modelMap;
		}
		else
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", "空的查询信息");
			return modelMap;
		} 
	}
	
	/**
	 * F3:修改商品信息
	 * @param shopStr
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/modifyproduct", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyProduct(String productStr,
			HttpServletRequest request) throws UnsupportedEncodingException {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		Product product=null;
		ImageHolder thumbnail=null;
		List<ImageHolder> productImgList=new ArrayList<ImageHolder>();
		try
		{
			product=mapper.readValue(productStr, Product.class);
		}catch(Exception e)
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		
		if(product!=null&&product.getProductId()!=null)
		{
			
			try
			{
				product.setProductName((product.getProductName() == null) ? null
					: (URLDecoder.decode(product.getProductName(), "UTF-8")));
				product.setProductDesc((product.getProductDesc() == null) ? null
					: (URLDecoder.decode(product.getProductDesc(), "UTF-8")));
				//开始进行商品信息变更操作
				ProductExecution pe=productService.modifyProduct(product, thumbnail, productImgList);
				if(pe.getState()==ProductStateEnum.SUCCESS.getState())
				{
					modelMap.put("success", true);
				}
				else
				{
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
				return modelMap;
				}
				catch(ProductOperationException e)
				{
					modelMap.put("success", false);
					modelMap.put("errMsg",  e.getMessage());
					return modelMap;
				}
		}
		else
		{
			modelMap.put("success", false);
			modelMap.put("errMsg",  "请输入商品信息");
		}
		return modelMap;
	}
}
