package com.my.buy.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.my.buy.BaseTest;
import com.my.buy.entity.Order;
import com.my.buy.entity.PersonInfo;
import com.my.buy.entity.Product;

public class OrderDaoTest extends BaseTest
{
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private PersonInfoDao userDao;
	@Autowired
	private ProductDao productDao;
	
	@Test
	@Ignore
	public void testInsertOrder()
	{
		Order order=new Order();
		order.setOrderStatus(0);
		order.setOrderType(2);
		order.setCreateTime(new Date());
		PersonInfo user=new PersonInfo();
		user=userDao.queryUserById(1L);
		Product product=new Product();
		product=productDao.queryProductById(1L);
		order.setUser(user);
		order.setUser(user);
		order.setProduct(product);
		int effectedNum=orderDao.insertOrder(order);
		System.out.println(effectedNum);
	}
	
	@Test
	@Ignore
	public void testUpdateOrder()
	{
		Order order=new Order();
		order.setLastEditTime(new Date());
		order.setOrderStatus(1);
		order.setOrderId(16L);
		int effectedNum=orderDao.updateOrder(order);
		System.out.println(effectedNum);
	}
	
	@Test
	@Ignore
	public void testQueryOrderById()
	{
		Order order=orderDao.queryOrderById(16L);
		System.out.println(order.getUser().getUserName());
	}
	
	@Test   
	public void testQueryOrderList()
	{
		Order orderCondition=new Order();
		PersonInfo user=new PersonInfo();
//		user=userDao.queryUserById(1L);
//		orderCondition.setOrderType(2);
// 		orderCondition.setOrderStatus(0);
		orderCondition.setEnableStatus(1); 
//		user.setUserId(1L);
//		orderCondition.setUser(user);
//		System.out.println(orderCondition.getEnableStatus());
		List<Order> orderList=orderDao.queryOrderList(orderCondition, 0, 2);
		System.out.println(orderList.size());
	}
	
	@Test
	@Ignore
	public void testDeleteOrderById()
	{
		int effectedNum=orderDao.deleteOrder(17L);
		System.out.println(effectedNum);
	}
	
	@Test  
	@Ignore
	public void testqueryOrderByProductIdAndUserId()
	{
		Order order=orderDao.queryOrderByProductIdAndUserId(21L, 12L);
		System.out.println(order.getOrderId());
	}
	
	@Test
	@Ignore
	public void testqueryOrdersByProductId()
	{
		List<Order> orderList=orderDao.queryOrdersByProductId(21L);
		System.out.println(orderList.size());
	}
	
	@Test
	@Ignore
	public void testqueryMyCommentOrderList()
	{
		long userId=1;
		List<Long> productIdList=new ArrayList<Long>();
		productIdList.add(1L);
		productIdList.add(21L);
		List<Order> myCommentOrderList=orderDao.queryMyCommentOrderList(productIdList, userId);
		System.out.println(myCommentOrderList.size());
	}
	@Test
	@Ignore
	public void testqueryProductListByUserId()
	{
		List<Long> productIdList=orderDao.queryProductListByUserId(1L);
		System.out.println(productIdList.size());
	}
}
