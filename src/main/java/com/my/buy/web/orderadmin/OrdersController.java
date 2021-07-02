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
import com.my.buy.dto.ProductExecution;
import com.my.buy.entity.Order;
import com.my.buy.entity.PersonInfo;
import com.my.buy.entity.Product;
import com.my.buy.enums.OrderStateEnum;
import com.my.buy.enums.ProductStateEnum;
import com.my.buy.exceptions.OrderOperationException;
import com.my.buy.exceptions.PersonInfoOperationException;
import com.my.buy.exceptions.ProductOperationException;
import com.my.buy.service.OrderService;
import com.my.buy.service.PersonInfoService;
import com.my.buy.service.ProductService;
import com.my.buy.util.HttpServletRequestUtil;

@Controller
@RequestMapping(value = "/orderadmin", method = RequestMethod.GET)
public class OrdersController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private ProductService productService;
	@Autowired
	private PersonInfoService userService;

	/**
	 * F1:添加order
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/addorder", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> addOrder(HttpServletRequest request) throws IOException {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Order buyerOrder = new Order();
		Order sellerOrder = new Order();
		OrderExecution se = null;
		OrderExecution be = null;
		long productId = HttpServletRequestUtil.getLong(request, "productId");
		PersonInfo buyer = (PersonInfo) request.getSession().getAttribute("user");
		Product product = null;
		PersonInfo seller = null;
		if (productId > 0) {
			product = productService.getProductById(productId);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "商品Id为空！");
			return modelMap;
		}
		// 获取卖家id
		try {
			long sellerId = product.getPersonInfo().getUserId();
			if (sellerId == buyer.getUserId()) {
				modelMap.put("success", false);
				modelMap.put("errMsg", "您无法购买自己的商品！");
				return modelMap;
			}
			if (sellerId > 0) {
				seller = userService.getUserById(sellerId);
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "卖家信息为空");
				return modelMap;
			}
		} catch (PersonInfoOperationException e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		// 获取当前productId的商品
		try {
			// 当订单产生时将该商品状态置为3（即已出售）
			int productEnableStatus = 3;
			product.setEnableSatus(productEnableStatus);
			ProductExecution pe = productService.modifyProduct(product, null, null);
			if (pe.getState() != ProductStateEnum.SUCCESS.getState()) {
				modelMap.put("success", false);
				modelMap.put("errMsg", "商品出售失败！");
				return modelMap;
			}
			// 重新获取一次
			product = productService.getProductById(productId);

		} catch (ProductOperationException e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}

		// 若买卖双方信息均不为空则添加order信息
		if (buyer != null && seller != null) {
			try {
				// 将卖家的订单状态设置为我卖出的
				sellerOrder.setOrderType(2);
				sellerOrder.setProduct(product);
				sellerOrder.setUser(seller);
				se = orderService.addOrder(sellerOrder);
				// 将买家的订单状态设置为我买到的
				buyerOrder.setOrderType(1);
				buyerOrder.setProduct(product);
				buyerOrder.setUser(buyer);
				be = orderService.addOrder(buyerOrder);
				if (se.getState() == OrderStateEnum.SUCCESS.getState()
						&& be.getState() == OrderStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
					return modelMap;
				}
			} catch (OrderOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "买卖家信息为空！");
			return modelMap;
		}
		return modelMap;
	}

	/**
	 * F2:修改订单状态
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/updateorder")
	@ResponseBody
	private Map<String, Object> updateOrder(HttpServletRequest request) throws IOException {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long orderId = HttpServletRequestUtil.getLong(request, "orderId");
		// 订单状态--0.待付款/1.已付款/2.交易完成/3.交易失败
		int orderStatus = HttpServletRequestUtil.getInt(request, "orderStatus");
		// 前端的订单删除，即修改enableStatus=0，前端不可见，但不是从数据库中删除
		int enableStatus = HttpServletRequestUtil.getInt(request, "enableStatus");
		Order order = new Order();
		OrderExecution oe = null;
		if (orderId > 0) {
			try {
				// 将买卖双方的定单状态修改
				order = orderService.getOrderById(orderId);
				if (orderStatus > -1) {
					order.setOrderStatus(orderStatus);
				}
				if (enableStatus > -1) {
					order.setEnableStatus(0);
				}
				oe = orderService.modifyOrder(order);
				if (oe.getState() == OrderStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", oe.getStateInfo());
					return modelMap;
				}

			} catch (OrderOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "无该订单信息");
			return modelMap;
		}
		return modelMap;
	}

	/**
	 * F3:通过orderId删除order
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/removeorderbyid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> removeOrderById(HttpServletRequest request) throws IOException {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long orderId = HttpServletRequestUtil.getLong(request, "orderId");
		OrderExecution oe = null;
		if (orderId > 0) {
			try {
				oe = orderService.removeOrder(orderId);
				if (oe.getState() == OrderStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", oe.getStateInfo());
					return modelMap;
				}
			} catch (OrderOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "订单Id为空");
			return modelMap;
		}
		return modelMap;
	}

	/**
	 * F4:通过订单id获取订单信息
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getorderbyid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getOrderById(HttpServletRequest request) throws IOException {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long orderId = HttpServletRequestUtil.getLong(request, "orderId");
		if (orderId > 0) {
			Order order = orderService.getOrderById(orderId);
			if (order != null && order.getOrderId() > 0) {
				modelMap.put("success", true);
				modelMap.put("order", order);
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "查询不到这条订单");
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "订单Id为空");
			return modelMap;
		}
		return modelMap;
	}

	/**
	 * F5:获取订单列表
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getorderlist", method = { RequestMethod.GET })
	@ResponseBody
	private Map<String, Object> getOrderList(HttpServletRequest request) throws IOException {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 订单类型--1.我买到的/2.我卖出的
		int orderType = HttpServletRequestUtil.getInt(request, "orderType");
		// 订单状态
		int orderStatus = HttpServletRequestUtil.getInt(request, "orderStatus"); 
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		// 前台查询
		int enableStatus = HttpServletRequestUtil.getInt(request, "enableStatus");
		Order orderCondition = null;
		if (orderType > 0 && user != null && pageIndex > 0 && pageSize > 0) {
			try {
				orderCondition = compactCondition(orderType, user, orderStatus, enableStatus);
				OrderExecution oe = orderService.getOrderList(orderCondition, pageIndex, pageSize);
				if (oe.getState() == OrderStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
					modelMap.put("orderList", oe.getOrderList());
					modelMap.put("count", oe.getCount());
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", oe.getStateInfo());
					return modelMap;
				}
			} catch (OrderOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "查询信息为空");
			return modelMap;
		}
		return modelMap;
	}

	/**
	 * F5-1:将查询条件进行组合
	 * 
	 * @param orderType
	 * @param userId
	 * @return
	 */
	private Order compactCondition(int orderType, PersonInfo user, int orderStatus, int enableStatus) {
		Order orderCondition = new Order();
		if (orderType > 0) {
			orderCondition.setOrderType(orderType);
		}
		if (user != null) {
			orderCondition.setUser(user);
		}
		if (orderStatus > -1) {
			orderCondition.setOrderStatus(orderStatus);
		} 
		if (enableStatus > -1) {
			orderCondition.setEnableStatus(1);
		}
		return orderCondition;
	}

	/**
	 * F6:取消订单（买卖双方中若有一方选择取消订单，则双方订单状态均变为3（交易失败））/将订单状态修改为收款状态
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/operateorder", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> operateOrder(HttpServletRequest request) throws IOException {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		int orderStatus = HttpServletRequestUtil.getInt(request, "orderStatus");
		Order tempOrder = new Order();
		Order buyerOrder = new Order();
		Order sellerOrder = new Order();
		OrderExecution se = null;
		OrderExecution be = null;
		long productId = HttpServletRequestUtil.getLong(request, "productId");
		long orderId = HttpServletRequestUtil.getLong(request, "orderId");
		PersonInfo buyer = new PersonInfo();
		Product product = null;
		PersonInfo seller = new PersonInfo();
		long userId=-1;
		if (orderId > 0 && productId > 0) {
			try {
				tempOrder = orderService.getOrderById(orderId);
				product = productService.getProductById(productId);
				// 若订单被取消，则将该商品的enableStatus置为1（上架状态）
				if (orderStatus > -1 && orderStatus == 3) {
					product.setEnableSatus(1);
					ProductExecution pe = productService.modifyProduct(product, null, null);
					if (pe.getState() != ProductStateEnum.SUCCESS.getState()) {
						modelMap.put("success", true);
						modelMap.put("errMsg", "商品上架失败！");
						return modelMap;
					}
					product = productService.getProductById(productId);
				}
				// 如果当前传入的订单类型为1（我买到的），则将买家订单置为当前订单
				if (tempOrder.getOrderType() == 1) {
					buyerOrder = tempOrder;
					buyer = userService.getUserById(buyerOrder.getUser().getUserId());
					buyerOrder.setOrderStatus(orderStatus);
					sellerOrder = orderService.getOrderByProductIdAndUserId(product.getProductId(),
							product.getPersonInfo().getUserId());
					seller = userService.getUserById(product.getPersonInfo().getUserId());
					sellerOrder.setOrderStatus(orderStatus);

				} else {
					sellerOrder = tempOrder;
					seller = userService.getUserById(sellerOrder.getUser().getUserId());
					sellerOrder.setOrderStatus(orderStatus);
					OrderExecution orders=orderService.getOrdersByProductId(product.getProductId());
					if(orders.getState()==OrderStateEnum.SUCCESS.getState())
					{
						
						List<Order> orderList=orders.getOrderList();
						for(int i=0;i<orderList.size();i++)
						{
							if(orderList.get(i).getUser().getUserId()!=sellerOrder.getUser().getUserId())
							{
								userId=orderList.get(i).getUser().getUserId();
							}
						}
					}
					buyerOrder = orderService.getOrderByProductIdAndUserId(product.getProductId(),
							userId);
					buyer = userService.getUserById(userId);
					buyerOrder.setOrderStatus(orderStatus);

				}
				// 将买卖双方的orderStatus都置为传入的orderStatus
				be = orderService.modifyOrder(buyerOrder);
				se = orderService.modifyOrder(sellerOrder);
				if (be.getState() == OrderStateEnum.SUCCESS.getState()
						&& se.getState() == OrderStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", be.getStateInfo());
					return modelMap;
				}
			} catch (OrderOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "订单id和商品id为空");
			return modelMap;
		}
		return modelMap;
	}

	

}
