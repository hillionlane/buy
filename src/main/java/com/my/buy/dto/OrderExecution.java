package com.my.buy.dto;

import java.util.List;

import com.my.buy.entity.Order; 
import com.my.buy.enums.OrderStateEnum;
 

public class OrderExecution 
{
	private int state;
	private String stateInfo;
	//增删改时使用
	private Order order;
	private int count;
	private List<Order> orderList;
	public OrderExecution()
	{
		
	}
	public OrderExecution(OrderStateEnum stateEnum)
	{
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
	}
	public OrderExecution(OrderStateEnum stateEnum,Order order)
	{
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
		this.order=order;
	}
	public OrderExecution(OrderStateEnum stateEnum,List<Order> orderList,int count)
	{
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
		this.orderList=orderList;
		this.count=count;
	}
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getStateInfo() {
		return stateInfo;
	}
	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public List<Order> getOrderList() {
		return orderList;
	}
	public void setOrderList(List<Order> orderList) {
		this.orderList = orderList;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}
