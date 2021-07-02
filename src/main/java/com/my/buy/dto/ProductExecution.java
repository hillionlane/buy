package com.my.buy.dto;

import java.util.List;

import com.my.buy.entity.Product;
import com.my.buy.enums.ProductStateEnum;

public class ProductExecution {
	private int state;
	private String stateInfo;
	//操作增删改商品时使用
	private Product product;
	//商品数量
	private int count;
	//查找商品列表时使用
	private List<Product> productList;
	public ProductExecution()
	{
 
	}

	//操作失败的时候使用的构造器
	public ProductExecution(ProductStateEnum stateEnum)
	{
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
	}
	//操作成功的时候使用的构造器
	public ProductExecution(ProductStateEnum stateEnum,List<Product> productList,int count)
	{
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
		this.productList=productList;
		this.count =count;
	}
	public ProductExecution(ProductStateEnum stateEnum, Product  product )
	{
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
		this.product =product ;
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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

}
