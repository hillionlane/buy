package com.my.buy.entity;

import java.util.Date;

public class ProductCategory 
{
    private Long productCategoryId;
    private String productCategoryName;
    private String productCategoryDesc;
    private String productCategoryImg;
    private Integer priority;
    private Date createTime;
    private Date lastEditTime; 
    //父类
    private ProductCategory parent;
    public Long getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getProductCategoryName() {
        return productCategoryName;
    }

    public void setProductCategoryName(String productCategoryName) {
        this.productCategoryName = productCategoryName;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getProductCategoryDesc() {
		return productCategoryDesc;
	}

	public void setProductCategoryDesc(String productCategoryDesc) {
		this.productCategoryDesc = productCategoryDesc;
	}

	public Date getLastEditTime() {
		return lastEditTime;
	}

	public void setLastEditTime(Date lastEditTime) {
		this.lastEditTime = lastEditTime;
	}

	public ProductCategory getParent() {
		return parent;
	}

	public void setParent(ProductCategory parent) {
		this.parent = parent;
	}

	public String getProductCategoryImg() {
		return productCategoryImg;
	}

	public void setProductCategoryImg(String productCategoryImg) {
		this.productCategoryImg = productCategoryImg;
	}

	 
	
}
