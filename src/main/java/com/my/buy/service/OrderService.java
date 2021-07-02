package com.my.buy.service;

import java.util.List;

import com.my.buy.dto.OrderExecution;
import com.my.buy.entity.Order;

public interface OrderService 
{
	/**
	 * F1:添加order
	 * @param order
	 * @return
	 */
	OrderExecution addOrder(Order order);
	
	/**
	 * F2:修改order
	 * @param order
	 * @return
	 */
	OrderExecution modifyOrder(Order order);
	
	/**
	 * F3:通过orderId删除order
	 * @param orderId
	 * @return
	 */
	OrderExecution removeOrder(long orderId);
	
	/**
	 * F4:通过orderId获取order
	 * @param orderId
	 * @return
	 */
	Order getOrderById(long orderId);
	
	/**
	 * F5:获取某用户下的卖出或买到的商品列表
	 * @param userId
	 * @param orderType
	 * @return
	 */
	OrderExecution getOrderList(Order orderCondition,int pageIndex,int pageSize);
	
	/**
	 * F6:根据商品Id和用户Id获取订单
	 * @param productId
	 * @param userId
	 * @return
	 */
	Order getOrderByProductIdAndUserId(long productId,long userId);
	
	/**
	 * F7:通过商品Id获取该商品相关的两个订单
	 * @param productId
	 * @return
	 */
	OrderExecution getOrdersByProductId(long productId);
	
	/**
	 * F8:根据商品Id获取用户评论订单
	 * @param productIdList
	 * @param userId
	 * @return
	 */
	OrderExecution getMyCommentOrderList(List<Long> productIdList,long userId);
	
	/**
	 * F9:根据用户id获取与该用户有关的所有商品Id
	 * @param userId
	 * @return
	 */
	List<Long> getProductListByUserId(long userId);
}
