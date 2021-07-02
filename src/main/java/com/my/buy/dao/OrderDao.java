package com.my.buy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.my.buy.entity.Order;

public interface OrderDao 
{
	/**
	 * F1: 添加订单 
	 * @return
	 */
	int insertOrder(Order order);
	
	/**
	 * F2:修改订单表
	 * @param order
	 * @return
	 */
	int updateOrder(Order order);
	
	/**
	 * F3:根据订单Id删除订单(即将enableStatus置为0)
	 * @param orderId
	 * @return
	 */
	int deleteOrder(long orderId);
	
	/**
	 * F4:根据订单Id查询订单信息
	 * @param orderId
	 * @return
	 */
	Order queryOrderById(long orderId);
	
	/**
	 * F5:查询某用户下的所有订单信息
	 * @param orderCondition
	 * orderType==1(我买到的)，orderType==2(我卖出的)
	 * @return
	 */
	List<Order> queryOrderList( @Param("orderCondition")Order orderCondition,@Param("rowIndex")int rowIndex,@Param("pageSize") int pageSize);
	
	/**
	 * F6:查询订单总数
	 * @param orderCondition
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	int queryOrderCount(@Param("orderCondition")Order orderCondition,@Param("rowIndex")int rowIndex,@Param("pageSize") int pageSize);
	
	/**
	 * F7:通过商品Id和用户Id获取订单
	 * @param productId
	 * @param userId
	 * @return
	 */
	Order queryOrderByProductIdAndUserId(@Param("productId")long productId,@Param("userId")long userId);
	
	/**
	 * F8:通过商品Id获取该商品相关的两个订单
	 * @param productId
	 * @return
	 */
	List<Order> queryOrdersByProductId(long productId);
	
	/**
	 * F9:根据商品Id获取用户评论订单
	 * @param productId
	 * @param userId:当前用户
	 * @return
	 */
	List<Order> queryMyCommentOrderList(@Param("productIdList")List<Long> productIdList,@Param("userId")long userId);
	
	/**
	 * F10:根据用户id获取与该用户有关的所有商品Id
	 * @param userId
	 * @return
	 */
	List<Long> queryProductListByUserId(long userId);
}
