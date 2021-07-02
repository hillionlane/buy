package com.my.buy.entity;

public class Collect 
{
	private Long collectId;
	private Long userId; 
	private Integer status;
	private Product product;
	public Long getCollectId() {
		return collectId;
	}
	public void setCollectId(long collectId) {
		this.collectId = collectId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	
}
