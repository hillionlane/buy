package com.my.buy.web.superadmin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.buy.dto.OrderExecution;
import com.my.buy.entity.ConstantForSuperAdmin;
import com.my.buy.entity.Order;
import com.my.buy.entity.PersonInfo;
import com.my.buy.enums.OrderStateEnum;
import com.my.buy.exceptions.OrderOperationException;
import com.my.buy.service.OrderService;
import com.my.buy.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/superadmin")
public class OrderController {
	@Autowired
	private OrderService orderService;
	
	/**
	 * F1:获取订单列表
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/listorders", method = { RequestMethod.POST})
	@ResponseBody
	private Map<String, Object> getOrderList(HttpServletRequest request) throws IOException {
		Map<String, Object> modelMap = new HashMap<String, Object>(); 
		int pageIndex = HttpServletRequestUtil.getInt(request,
				ConstantForSuperAdmin.PAGE_NO);
		int pageSize = HttpServletRequestUtil.getInt(request,
				ConstantForSuperAdmin.PAGE_SIZE);  
		if (pageIndex > 0 && pageSize > 0) { 
			 Order orderCondition = null;
			 // 订单类型--1.我买到的/2.我卖出的
			 int orderType = HttpServletRequestUtil.getInt(request, "orderType");
			 // 订单状态
			 int orderStatus = HttpServletRequestUtil.getInt(request, "orderStatus");  
			 if(orderType>0||orderStatus>-1)
			 { 
				 orderCondition = compactCondition(orderType, orderStatus);
			 }
			try
			{
				OrderExecution oe = orderService.getOrderList(orderCondition, pageIndex, pageSize);
				if (oe.getState() == OrderStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
					modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, oe.getOrderList());
					modelMap.put(ConstantForSuperAdmin.TOTAL, oe.getCount());
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
	private Order compactCondition(int orderType, int orderStatus ) {
		Order orderCondition = new Order();
		if (orderType > 0) {
			orderCondition.setOrderType(orderType);
		}
		 
		if (orderStatus > -1) {
			orderCondition.setOrderStatus(orderStatus);
		}
		 
		return orderCondition;
	}
}
