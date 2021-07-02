package com.my.buy.web.orderadmin;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.buy.dto.OrderExecution;
import com.my.buy.entity.Order;
import com.my.buy.entity.PersonInfo;
import com.my.buy.entity.Product;
import com.my.buy.enums.OrderStateEnum;
import com.my.buy.exceptions.OrderOperationException;
import com.my.buy.exceptions.ProductOperationException;
import com.my.buy.service.OrderService;
import com.my.buy.service.PersonInfoService;
import com.my.buy.service.ProductService;
import com.my.buy.util.HttpServletRequestUtil;

@Controller
@RequestMapping(value = "/orderadmin", method = RequestMethod.GET)
public class OrderCommentController 
{
	@Autowired
	private OrderService orderService;
	@Autowired
	private ProductService productService;
	@Autowired
	private PersonInfoService userService;
	
	/**
	 * F1:当订单处于已完成状态，对订单进行评价
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/commentorder", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> commentOrder(HttpServletRequest request) throws IOException {
		Map<String, Object> modelMap = new HashMap<String, Object>(); 
		request.setCharacterEncoding("utf-8");
		long orderId = HttpServletRequestUtil.getLong(request, "orderId"); 
		String orderComment=HttpServletRequestUtil.getString(request, "orderComment"); 
		Order order=null;
		OrderExecution oe=null; 
		if(orderComment==null)
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", "留言不能为空");
			return modelMap; 
		}
		if(orderId>0)
		{
			//获取当前订单
			try
			{
				order=orderService.getOrderById(orderId);
				if(order!=null)
				{  
					order.setOrderComment(orderComment); 
					order.setCommentTime(new Date());
					//修改当前订单的orderComment
					oe=orderService.modifyOrder(order);
					if(oe.getState()==OrderStateEnum.SUCCESS.getState())
					{
						modelMap.put("success", true);
					}
					else
					{
						modelMap.put("success", false);
						modelMap.put("errMsg", "订单修改失败");
						return modelMap;
					}
				}
				else
				{
					modelMap.put("success", false);
					modelMap.put("errMsg", "查询不到该订单");
					return modelMap;
				}
			}catch(OrderOperationException e)
			{
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		}
		else
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", "订单id为空");
			return modelMap;
		}
		return modelMap;
	}
	/**
	 * F2:获取该商品的两条订单信息以及买卖双方的信息
	 * 通过productId获取商品用户信息，通过session获取用户信息
	 * 再通过根据productId和userId获取order的方法获取与该商品有关的两条order
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getordercomment", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getOrderComment(HttpServletRequest request) throws IOException {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long productId=HttpServletRequestUtil.getLong(request, "productId"); 
		Product product=null;
		Order sellerOrder=null;
		Order buyerOrder=null; 
		PersonInfo buyer=null;
		PersonInfo seller=null;
		OrderExecution oe=null;
		PersonInfo currentUser=(PersonInfo) request.getSession().getAttribute("user");
		if(productId>0)
		{
			//通过productId获取商品用户
			try
			{ 
				product=productService.getProductById(productId);
				if(product==null)
				{
					modelMap.put("success", false);
					modelMap.put("errMsg", "商品查询失败！");
					return modelMap;
				}
			}
			catch(ProductOperationException e)
			{
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
			//通过productId和userId获取买卖双方的order
			try
			{

				oe=orderService.getOrdersByProductId(productId);
				if(oe.getState()==OrderStateEnum.SUCCESS.getState())
				{
					for(Order currentOrder:oe.getOrderList())
					{
						//我买到的
						if(currentOrder.getOrderType()==1)
						{
							buyerOrder=currentOrder;
							buyer=userService.getUserById(currentOrder.getUser().getUserId());
						}
						else
						{
							sellerOrder=currentOrder;
							seller=userService.getUserById(currentOrder.getUser().getUserId());
						}
					}
				}
				else
				{
					modelMap.put("success", false);
					modelMap.put("errMsg", "查询不到该商品的两个订单");
					return modelMap;
				}  
				if(buyerOrder!=null&&sellerOrder!=null&&buyer!=null&&seller!=null)
				{
					modelMap.put("success", true);
					modelMap.put("sellerOrder", sellerOrder);
					modelMap.put("buyerOrder", buyerOrder);
					modelMap.put("buyer", buyer);
					modelMap.put("seller", seller);
					modelMap.put("product", product);
					//判断当前用户是否为卖家，若为卖家则返回isme为true
					if(seller.getUserId()==currentUser.getUserId())
					{
						modelMap.put("isseller", true);
					}
					//判断当前用户是否为买家，若为买家则返回isme为true
					if(buyer.getUserId()==currentUser.getUserId())
					{
						modelMap.put("isbuyer", true);
					}
				}
				else
				{
					modelMap.put("success", false);
					modelMap.put("errMsg", "查询信息有误！");
					return modelMap;
				}
			}
			catch(OrderOperationException e)
			{
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
			 
		}
		else
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", "订单Id为空");
			return modelMap;
		}
		return modelMap;
	}
	
	/**
	 * F3:获取我的评论订单列表
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getmycommentorderlist", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getMyCommentOrderList(HttpServletRequest request) throws IOException {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		PersonInfo currentUser=(PersonInfo) request.getSession().getAttribute("user");
		OrderExecution oe=null;
		//通过当前用户的userId获取商品id列表
		if(currentUser!=null&&currentUser.getUserId()>0)
		{
			try
			{
				List<Long> productIdList=orderService.getProductListByUserId(currentUser.getUserId()); 
				//若与该用户有关的订单的商品id列表为空，则说明该用户暂时无交易
				if(productIdList!=null&&productIdList.size()>0)
				{
					oe=orderService.getMyCommentOrderList(productIdList, currentUser.getUserId());
					if(oe.getState()==OrderStateEnum.SUCCESS.getState())
					{
						modelMap.put("success", true);
						modelMap.put("myCommentOrderList", oe.getOrderList());
					}
					else
					{
						modelMap.put("success", false);
						modelMap.put("errMsg", "获取订单失败");
						return modelMap;
					}
				}
				else
				{
					modelMap.put("success", false);
					modelMap.put("errMsg", "当前用户无订单");
					return modelMap;
				}
			}
			catch(OrderOperationException e)
			{
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		}
		else
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", "当前用户信息为空");
			return modelMap;
		}
		return modelMap;
	}
}
