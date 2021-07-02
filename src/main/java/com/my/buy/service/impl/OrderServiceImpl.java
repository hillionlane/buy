package com.my.buy.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.buy.dao.OrderDao;
import com.my.buy.dto.OrderExecution;
import com.my.buy.entity.Order;
import com.my.buy.enums.OrderStateEnum;
import com.my.buy.exceptions.OrderOperationException;
import com.my.buy.service.OrderService;
import com.my.buy.util.PageCalculator;

@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	private OrderDao orderDao;
	/**
	 * F1:添加order
	 */
	@Override
	@Transactional
	public OrderExecution addOrder(Order order) {
		try
		{
			if(order!=null&&order.getUser()!=null&&order.getUser().getUserId()!=null&&order.getProduct()!=null&&order.getProduct().getProductId()!=null)
			{
				order.setCreateTime(new Date());
				order.setLastEditTime(new Date());
				//设置初始状态为1
				order.setEnableStatus(1);
				//设置初始订单状态为待付款0
				order.setOrderStatus(0);
				int effectedNum=orderDao.insertOrder(order);
				if(effectedNum>0)
				{
					return new OrderExecution(OrderStateEnum.SUCCESS,order);
				}
				else
				{
					throw new OrderOperationException("订单生成失败！");
				}
			}
			else
			{
				return new OrderExecution(OrderStateEnum.EMPTY);
			}
		}
		catch(OrderOperationException e)
		{
			throw new OrderOperationException("订单生成失败:"+e.getMessage());
		} 
	}

	/**
	 * F2:修改订单表
	 */
	@Override
	@Transactional
	public OrderExecution modifyOrder(Order order) {
		try
		{
			if(order!=null&&order.getOrderStatus()>=0)
			{
				if(order.getCommentTime()==null)
				{
					order.setLastEditTime(new Date());
				}
				int effecteNum=orderDao.updateOrder(order);
				if(effecteNum>0)
				{
					return new OrderExecution(OrderStateEnum.SUCCESS,order);
				}
				else
				{
					throw new OrderOperationException("订单状态修改失败！");
				}
			}
			else
			{
				return new OrderExecution(OrderStateEnum.EMPTY);
			}
		}
		catch(OrderOperationException e)
		{
			throw new OrderOperationException("订单状态修改失败:"+e.getMessage());
		} 
	}

	/**
	 * F3:通过orderId删除order
	 */
	@Override
	@Transactional
	public OrderExecution removeOrder(long orderId) {
		try
		{
			if(orderId>0)
			{   
					int effecedNum=orderDao.deleteOrder(orderId);
					if(effecedNum>0)
					{
						return new OrderExecution(OrderStateEnum.SUCCESS);
					}
					else
					{
						throw new OrderOperationException("订单删除失败！");
					}  
			}
			else
			{
				return new OrderExecution(OrderStateEnum.EMPTY);
			}
		}
		catch(OrderOperationException e)
		{
			throw new OrderOperationException("订单删除失败:"+e.getMessage());
		} 
	}

	/**
	 * F4:通过orderId查询order
	 */
	@Override
	public Order getOrderById(long orderId) { 
		return orderDao.queryOrderById(orderId);
	}

	/**
	 * F5:查询某个用户买到或卖出的orderlist
	 */
	@Override
	@Transactional
	public OrderExecution getOrderList(Order orderCondition,int pageIndex,int pageSize) {
		try
		{  
			 int rowIndex=PageCalculator.calculateRowIndex(pageIndex, pageSize);
			 List<Order> orderList=orderDao.queryOrderList(orderCondition,rowIndex,pageSize);
			 int orderCount=orderDao.queryOrderCount(orderCondition, rowIndex, pageSize); 
			 return new OrderExecution(OrderStateEnum.SUCCESS,orderList,orderCount);   
		}
		catch(OrderOperationException e)
		{
			throw new OrderOperationException("查询订单列表失败:"+e.getMessage());
		} 
	}

	/**
	 * F6:根据商品Id和用户Id获取订单
	 */
	@Override
	public Order getOrderByProductIdAndUserId(long productId, long userId) 
	{ 
		return orderDao.queryOrderByProductIdAndUserId(productId, userId);
	}

	/**
	 * F7:通过商品Id获取该商品相关的两个订单
	 */
	@Override
	@Transactional
	public OrderExecution getOrdersByProductId(long productId) 
	{  
		if(productId>0)
		{
			try
			{
				List<Order> orderList=orderDao.queryOrdersByProductId(productId);
				if(orderList!=null&&orderList.size()>0)
				{
					return new OrderExecution(OrderStateEnum.SUCCESS, orderList, orderList.size());
				}
				else
				{
					throw new OrderOperationException("查询该商品的两条订单失败！");
				}
			}catch(OrderOperationException e)
			{
				throw new OrderOperationException(e.getMessage());
			}
		}
		else
		{
			return new OrderExecution(OrderStateEnum.EMPTY);
		} 
	}

	/**
	 * F8:根据商品Id获取用户评论订单
	 */
	@Override
	@Transactional
	public OrderExecution getMyCommentOrderList(List<Long> productIdList, long userId) 
	{
		if(productIdList!=null&&productIdList.size()>0&&userId>0)
		{
			try
			{
				List<Order> myCommentOrderList=orderDao.queryMyCommentOrderList(productIdList, userId); 
				return new OrderExecution(OrderStateEnum.SUCCESS, myCommentOrderList, myCommentOrderList.size()); 
			}
			catch(OrderOperationException e)
			{
				throw new OrderOperationException(e.getMessage());
			}
		}
		else
		{
			return new OrderExecution(OrderStateEnum.EMPTY);
		} 
	}

	/**
	 * F9:根据用户id获取与该用户有关的所有商品订单Id
	 */
	@Override  
	public List<Long> getProductListByUserId(long userId) {
		  return orderDao.queryProductListByUserId(userId); 
	}

}
