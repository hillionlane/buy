package com.my.buy.web.productadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.buy.dto.ImageHolder;
import com.my.buy.dto.ProductExecution;
import com.my.buy.entity.Area;
import com.my.buy.entity.PersonInfo;
import com.my.buy.entity.Product;
import com.my.buy.entity.ProductCategory;
import com.my.buy.enums.ProductStateEnum;
import com.my.buy.exceptions.ProductOperationException;
import com.my.buy.service.ProductService;
import com.my.buy.util.CodeUtil;
import com.my.buy.util.HttpServletRequestUtil;

@Controller
@RequestMapping(value="/productadmin")
public class ProductManagementController 
{
	private static final int IMAGEMAXCOUNT = 6;
	@Autowired
	private ProductService productService;
	
	/**
	 * F1:增加商品
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/addproduct",method={RequestMethod.POST})
	@ResponseBody
	private Map<String, Object> addProduct(HttpServletRequest request) throws IOException
	{
		Map<String,Object> modelMap=new HashMap<String,Object>(); 
		//验证码校验
		if(!CodeUtil.checkVerifyCode(request))
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码输入错误");
			return modelMap;
		}
		//接收前端参数的变量的初始化，包括商品，缩略图，详情图
		ObjectMapper mapper=new ObjectMapper();
		Product product=null; 
		String productStr=HttpServletRequestUtil.getString(request, "productStr");
		ImageHolder thumbnail=null;
		// 创建多部分HttpRequest请求
		MultipartHttpServletRequest multipartHttpServletRequest=null;
		List<ImageHolder> productImgList=new ArrayList<ImageHolder>();
		////创建一个通用的多部分解析器 (用来判断是否有上传流)
		CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(
					request.getSession().getServletContext());
		try	
		{
			//判断 request 是否有文件上传,即多部分请求
			if(multipartResolver.isMultipart(request))	
			{
				//转换成多部分request   
				multipartHttpServletRequest=(MultipartHttpServletRequest) request;
				//取出缩略图并构建ImageHolder对象
				CommonsMultipartFile thumbnailFile=(CommonsMultipartFile) multipartHttpServletRequest.getFile("thumbnail");
				thumbnail=new ImageHolder(thumbnailFile.getOriginalFilename(),thumbnailFile.getInputStream());
				//取出详情图列表并构建List<ImageHolder>列表对象，最多支持六张图片上传
				for(int i=0;i<IMAGEMAXCOUNT;i++)
				{
					CommonsMultipartFile productImgFile=(CommonsMultipartFile) multipartHttpServletRequest.getFile("productImg"+i);
					if(productImgFile!=null)
					{
						//若取出的第i个详情图片的文件流不为空，则将其加入详情图列表
						ImageHolder productImg=new ImageHolder(productImgFile.getOriginalFilename(), productImgFile.getInputStream());
						productImgList.add(productImg);
					}
					else
					{
						break;
					}
				}
			}
			else
			{
				modelMap.put("success", false);
				modelMap.put("errMsg", "上传图片不能为空！");
				return modelMap;
			}
		}catch(ProductOperationException e)
		{
			modelMap.put("success", true);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		try
		{
			//尝试获取前端传过来的表单string流并将其转换成Product实体类
			product=mapper.readValue(productStr, Product.class);
					
		}catch(ProductOperationException e)
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		//若	Product信息，缩略图以及详情图列表为非空，则开始进行商品添加操作
		if(product!=null&&thumbnail!=null&&productImgList.size()>0)
		{
			try
			{
				//从session中获取当前用户的Id并赋值给product，减少对前端数据的依赖
			    PersonInfo currentUser=(PersonInfo) request.getSession().getAttribute("user");
				Area area=currentUser.getArea();
				product.setPersonInfo(currentUser);
				product.setArea(area);
				//执行添加操作
				ProductExecution pe=productService.addProduct(product, thumbnail, productImgList);
				if(pe.getState()==ProductStateEnum.SUCCESS.getState())
				{
					modelMap.put("success", true);
					//如果该用户是第一次发布商品
					@SuppressWarnings("unchecked")
					List<Product> productList=(List<Product>) request.getSession().getAttribute("productList");
					if(productList==null||productList.size()==0)
					{
						productList=new ArrayList<Product>();
					}
					productList.add(pe.getProduct());
					request.getSession().setAttribute("productList", productList);
				}
				else
				{
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			}catch(ProductOperationException e)
			{
				modelMap.put("success", false);
				modelMap.put("errMsg",e.getMessage());
				return modelMap;
			}
		} 
		else
		{
			modelMap.put("success", false);
			modelMap.put("errMsg","请输入商品信息！");		
		}
		return modelMap;
	}
	/**
	 * F2:通过ProductId获取商品信息
	 * @param request
	 * @return
	 * @throws IOException
	 */
	
	@RequestMapping(value="/getproductbyid",method={RequestMethod.GET})
	@ResponseBody
	private Map<String, Object> getProductById(HttpServletRequest request) throws IOException
	{
		Map<String,Object> modelMap=new HashMap<String,Object>(); 
		Product product=new Product();
		//获取商品Id
		long productId=HttpServletRequestUtil.getLong(request, "productId");
		if(productId>0)
		{
			product=productService.getProductById(productId);
			modelMap.put("success", true);
			modelMap.put("product", product);
		}
		else
		{
			modelMap.put("success", false);
			modelMap.put("errMsg:", "商品Id为空");
		}
		return modelMap;
	}
	
	/**
	 * F3:返回商品列表（根据查询条件）
	 * 条件:同一商品类别、同一用户、同一区域、模糊查询的名字、上架产品(enableStatus=1)
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getproductlist",method={RequestMethod.GET})
	@ResponseBody
	private Map<String, Object> getProductList(HttpServletRequest request)
	{
		Map<String, Object> modelMap=new HashMap<String, Object>();
		//获取页码
		int pageIndex=HttpServletRequestUtil.getInt(request, "pageIndex");
		//获取一页需要显示的数据条数
		int pageSize=HttpServletRequestUtil.getInt(request, "pageSize");
		//非空判断
		if((pageIndex>-1)&&(pageSize>-1))
		{
			Product productCondition=null;
			ProductExecution pe=null;
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
			 //前端展示页面都是审核成功的商品
			int enableStatus=1;
			//获取组合之后的查询条件
			if(parentId>0||productCategoryId>0||areaId>0||productName!=null||userId>0||enableStatus>-1)
			{
				productCondition= compactProductCondtionForSearch(parentId,productCategoryId,areaId,productName,userId,enableStatus);
			}
			//根据查询调价和分页信息获取店铺列表，并返回总数
			pe=productService.getProductList(productCondition, pageIndex, pageSize);
			modelMap.put("success", true);
			modelMap.put("productList", pe.getProductList());
			modelMap.put("count", pe.getCount());
		}
		else
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex");
		}
		return modelMap;
	}
	/**
	 * F3-1:组合获取商品列表的查询条件
	 * @param parentId
	 * @param productCategoryId
	 * @param areaId
	 * @param productName
	 * @param userId
	 * @return
	 */
	private Product compactProductCondtionForSearch(long parentId, long productCategoryId, long areaId,
			String productName, long userId,int enableStatus) {
		 Product productCondition=new Product();
		 if(parentId>0)
		 {
			 //获取某一级类别下的所有商品
			 ProductCategory parent=new ProductCategory();
			 parent.setProductCategoryId(parentId);
			 ProductCategory child=new ProductCategory();
			 child.setParent(parent);
			 productCondition.setProductCategory(child);
		 }
		 if(productCategoryId>0)
		 {
			 //获取某个指定类别(二级)下的商品列表
			 ProductCategory productCategory=new ProductCategory();
			 productCategory.setProductCategoryId(productCategoryId);
			 productCondition.setProductCategory(productCategory);
		 }
		 if(areaId>0)
		 {
			 //获取某个区域的所有商品
			 Area area=new Area();
			 area.setAreaId(areaId);
			 productCondition.setArea(area);
		 }
		 if(productName!=null)
		 {
			 //获取模糊查询的商品名称
			 productCondition.setProductName(productName);
		 }
		 if(userId>0)
		 {
			 //获取某个用户发布的商品列表
			 PersonInfo user=new PersonInfo();
			 user.setUserId(userId);
			 productCondition.setPersonInfo(user);
		 }
		 if(enableStatus>-1)
		 {
			 productCondition.setEnableSatus(enableStatus);
		 }
		 return productCondition;
	}
	/**
	 * F4:删除商品（删除数据库中图片，以及存储在项目中的图片）
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/removeproductbyid",method={RequestMethod.GET})
	@ResponseBody
	private Map<String, Object> removeProductById(HttpServletRequest request) throws IOException
	{
		Map<String,Object> modelMap=new HashMap<String,Object>(); 
		long productId=HttpServletRequestUtil.getLong(request, "productId");
		ProductExecution pe=null;
		if(productId>0)
		{
			try
			{
				pe=productService.removeProductByProductId(productId);
				if(pe.getState()==ProductStateEnum.SUCCESS.getState())
				{
					modelMap.put("success", true);
				}
				else
				{
					modelMap.put("success", false);
					modelMap.put("errMsg", "删除商品失败！");
				}
			}
			catch(ProductOperationException e)
			{
				modelMap.put("success", false);
				modelMap.put("errMsg", "删除商品失败！");
			}
		}
		else
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", "商品Id为空！");
		}
		return modelMap;
	}
	/**
	 * F5:修改商品信息
	 */
	@RequestMapping(value="/modifyproduct",method=RequestMethod.POST)
	@ResponseBody
	private Map<String,Object> modifypProduct(HttpServletRequest request) throws IOException
	{
		Map<String,Object> modelMap=new HashMap<String,Object>();
		PersonInfo user=(PersonInfo) request.getSession().getAttribute("user");
		
		//是商品编辑时候调用还是上下架时调用
		//若为前者则进行验证码判断，后者则跳过验证码判断
		boolean statusChange=HttpServletRequestUtil.getBoolean(request, "statusChange");
		if(!statusChange)
		{
			//验证码判断
			if(!CodeUtil.checkVerifyCode(request))
			{
				modelMap.put("success", false);
				modelMap.put("errMsg", "验证码输入错误！");
				return modelMap;
			}
		}
			//接收前段参数变量的初始化，包括商品，缩略图，详情列表实体类
			ObjectMapper mapper=new ObjectMapper();
			Product product=null;
			ImageHolder thumbnail=null;
			List<ImageHolder> productImgList=new ArrayList<ImageHolder>();
			CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(request.getSession().getServletContext());
			//若存在文件流，则取出相关文件
			try
			{
			if(multipartResolver.isMultipart(request))
			{
				MultipartHttpServletRequest multipartHttpServletRequest=(MultipartHttpServletRequest) request;
				//取出缩略图
				CommonsMultipartFile thumbnailFile=(CommonsMultipartFile) multipartHttpServletRequest.getFile("thumbnail");
				if(thumbnailFile!=null)
				{
					thumbnail=new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
				}
				//取出详情图
				for(int i=0;i<IMAGEMAXCOUNT;i++)
				{
					CommonsMultipartFile productImgFile=(CommonsMultipartFile) multipartHttpServletRequest.getFile("productImg"+i);
					if(productImgFile!=null)
					{
						ImageHolder productImg=new ImageHolder(productImgFile.getOriginalFilename(), productImgFile.getInputStream());
						productImgList.add(productImg);
					}
					else
					{
						break;
					}
					
				}
			}
			}catch(Exception e)
			{
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
			try
			{
				String productStr=HttpServletRequestUtil.getString(request, "productStr");
				product=mapper.readValue(productStr, Product.class);
			}catch(Exception e)
			{
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
			//非空判断
			if(product!=null)
			{
				try
				{ 
				 Product tempProduct=productService.getProductById(product.getProductId());
				 if(tempProduct.getPersonInfo().getUserId()!=user.getUserId())
				 {
					 modelMap.put("success", false);
					 modelMap.put("errMsg", "该用户无法操作！");
					 return modelMap;
				 }
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
