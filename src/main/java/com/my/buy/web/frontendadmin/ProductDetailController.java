package com.my.buy.web.frontendadmin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.buy.dto.CollectExecution;
import com.my.buy.dto.CommentExecution;
import com.my.buy.dto.ProductExecution;
import com.my.buy.entity.Area;
import com.my.buy.entity.Collect;
import com.my.buy.entity.Comment;
import com.my.buy.entity.PersonInfo;
import com.my.buy.entity.Product;
import com.my.buy.enums.CollectStateEnum;
import com.my.buy.enums.CommentStateEnum;
import com.my.buy.enums.ProductStateEnum;
import com.my.buy.exceptions.CollectOperationException;
import com.my.buy.exceptions.ProductOperationException;
import com.my.buy.service.AreaService;
import com.my.buy.service.CollectService;
import com.my.buy.service.CommentService;
import com.my.buy.service.PersonInfoService;
import com.my.buy.service.ProductService;
import com.my.buy.util.HttpServletRequestUtil;

@Controller
@RequestMapping(value = "/frontendadmin")
public class ProductDetailController {
	@Autowired
	private ProductService productService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private PersonInfoService userService;
	@Autowired
	private CollectService collectService;
	

	/**
	 * F1:获取商品区域地址
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getproductaddr")
	@ResponseBody
	private Map<String, Object> getProductById(HttpServletRequest request) throws IOException {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Product product = new Product();
		Area area = null;
		PersonInfo user = null;
		String productAddr = null;
		// 获取商品Id
		long productId = HttpServletRequestUtil.getLong(request, "productId");
		if (productId > 0) {
			product = productService.getProductById(productId);
			area = areaService.getAreaById(product.getArea().getAreaId());
			user = userService.getUserById(product.getPersonInfo().getUserId());
			productAddr = area.getAreaName() + user.getUserAddr();
			modelMap.put("success", true);
			modelMap.put("productAddr", productAddr);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg:", "商品Id为空");
		}
		return modelMap;
	}

	/**
	 * F2:查询该商品是否被收藏
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getcollection",method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getCollection(HttpServletRequest request) throws IOException 
	{
		Map<String, Object> modelMap = new HashMap<String, Object>();
		PersonInfo user=(PersonInfo) request.getSession().getAttribute("user");
		Collect collection = new Collect();
		long userId=user.getUserId();
		long productId=HttpServletRequestUtil.getLong(request, "productId");
		try
		{
			if(userId>0&&productId>0)
			{
				collection=collectService.getCollection(userId, productId);
				if(collection!=null)
				{
					//取消收藏
					modelMap.put("success", true);
				}
				else
				{
					//收藏
					modelMap.put("success", false);
				}
			}
			else
			{
				modelMap.put("errMsg","用户Id或商品Id为空");
			}
		}
		catch(CollectOperationException e)
		{
		/*	modelMap.put("success", false);*/
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
	/**
	 * F3:收藏商品 
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("null")
	@RequestMapping(value = "/addcollection",method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> collectProductOrNot(HttpServletRequest request) throws IOException {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Collect collection = new Collect();
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		long userId = user.getUserId();
		long productId = HttpServletRequestUtil.getLong(request, "productId");
		Product product=productService.getProductById(productId);
		if (userId > 0 &&product!=null&& productId > 0) 
		{	 
			//收藏
			try {
				collection.setUserId(userId);
				collection.setProduct(product);
				collection.setStatus(1);
				CollectExecution ce = collectService.addCollection(collection);
				if (ce.getState() == CollectStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
					return modelMap;
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", ce.getStateInfo());
					return modelMap;
				}
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		 }
		else
		{
			 modelMap.put("success", false);
			 modelMap.put("errMsg", "用户Id或商品Id不能为空");
			 return modelMap;
		}
	}

 	/**
 	 * F3:取消收藏操作
 	 * @param request
 	 * @return
 	 * @throws IOException
 	 */
 	@RequestMapping(value = "/removecollection",method=RequestMethod.GET)
 	@ResponseBody
 	private Map<String, Object> removeCollection(HttpServletRequest request) throws IOException {
 		Map<String, Object> modelMap = new HashMap<String, Object>();
 		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
 		long userId = user.getUserId();
  		long productId = HttpServletRequestUtil.getLong(request, "productId");
  		if (userId > 0 && productId > 0) 
		{	 
			//取消收藏
			try 
			{
				CollectExecution ce = collectService.removeCollection(userId, productId);
				if (ce.getState() == CollectStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
					return modelMap;
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", ce.getStateInfo());
					return modelMap;
				}
			}
			catch (Exception e) 
			{
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		 }
		else
		{
			 modelMap.put("success", false);
			 modelMap.put("errMsg", "用户Id或商品Id不能为空");
			 return modelMap;
		}
	} 
 	
 	/**
 	 * F4:判断当前留言用户是否为session用户，若为session用户则返回true
 	 * @param request
 	 * @return
 	 * @throws IOException
 	 */
  	@RequestMapping(value = "/ismycomment",method=RequestMethod.GET)
  	@ResponseBody
  	private Map<String, Object> isMyComment(HttpServletRequest request) throws IOException 
  	{
  		Map<String, Object> modelMap = new HashMap<String, Object>();
  		long commentUserId=HttpServletRequestUtil.getLong(request, "commentUserId");
  		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user"); 
  		long productUserId=HttpServletRequestUtil.getLong(request, "productUserId");
  		if(productUserId==user.getUserId())
  		{
  			modelMap.put("isMyProduct", true); 
  		}
  		else
  		{
  			if(commentUserId==user.getUserId())
  			{
  				modelMap.put("success", true); 
  			}
  			else
  			{
  	  			modelMap.put("success", false);
  			}
  		}
  		return modelMap;
  	}
  	
  	/**
  	 * F5:修改商品状态为下架
  	 * @param request
  	 * @return
  	 * @throws IOException
  	 */
  	@RequestMapping(value = "/changeproductstatus",method=RequestMethod.GET)
  	@ResponseBody
  	private Map<String, Object> changeProductStatus(HttpServletRequest request) throws IOException 
  	{
  		Map<String, Object> modelMap = new HashMap<String, Object>();
  		long productId=HttpServletRequestUtil.getLong(request, "productId");
  		Product product=new Product();
		ProductExecution pe=null;
  		if(productId>0)
  		{
  			try
  			{
  				 product=productService.getProductById(productId);
  				 if(product!=null)
  				 {
  					 product.setEnableSatus(0);
  					 pe=productService.modifyProduct(product, null, null);
  					 if(pe.getState()==ProductStateEnum.SUCCESS.getState())
  					 {
  						 modelMap.put("success", true);
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
  					 modelMap.put("errMsg", "该商品不存在！");
  				 }
  			}
  			catch(ProductOperationException e)
  			{
  				 modelMap.put("success", false);
  				 modelMap.put("errMsg", e.getMessage());
  			}
  		}
  		else
  		{
  			 modelMap.put("success", false);
  			 modelMap.put("errMsg","商品信息为空！");
  		}
  		return modelMap;
  	}
  	
}
