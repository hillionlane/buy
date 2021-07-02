package com.my.buy.entity;

import java.util.Date;
import java.util.List;

public class Product 
{
	 private Long productId;
	    private String productName;
	    private String productDesc;
	    //简略图
	    private String productImg;
	    private String productPrice; 
	    private Integer priority;
	    private Date createTime;
	    private Date lastEditTime;
	    //0表示下架，1在前端展示系统展示,3已经出售
	    private Integer enableStatus;
	    //商品与商品图片属于一对多类别。一般通过product需要使用
	    private List<ProductImg> productImgList;
	    private ProductCategory productCategory;
	    private Area area;
	    //属于哪个用户
	    private PersonInfo personInfo;
		public PersonInfo getPersonInfo() {
			return personInfo;
		}

		public void setPersonInfo(PersonInfo personInfo) {
			this.personInfo = personInfo;
		}

		public void setEnableStatus(Integer enableStatus) {
			this.enableStatus = enableStatus;
		}

		public Long getProductId() {
	        return productId;
	    }

	    public void setProductId(Long productId) {
	        this.productId = productId;
	    }

	    public String getProductName() {
	        return productName;
	    }

	    public void setProductName(String productName) {
	        this.productName = productName;
	    }

	    public String getProductDesc() {
	        return productDesc;
	    }

	    public void setProductDesc(String productDesc) {
	        this.productDesc = productDesc;
	    }
	    public String getProductPrice() {
			return productPrice;
		}

		public void setProductPrice(String productPrice) {
			this.productPrice = productPrice;
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

	    public Date getLastEditTime() {
	        return lastEditTime;
	    }

	    public void setLastEditTime(Date lastEditTime) {
	        this.lastEditTime = lastEditTime;
	    }

	    public Integer getEnableStatus() {
	        return enableStatus;
	    }

	    public void setEnableSatus(Integer enableStatus) {
	        this.enableStatus = enableStatus;
	    }

	    public List<ProductImg> getProductImgList() {
	        return productImgList;
	    }

	    public void setProductImgList(List<ProductImg> productImgList) {
	        this.productImgList = productImgList;
	    }

	    public ProductCategory getProductCategory() {
	        return productCategory;
	    }

	    public void setProductCategory(ProductCategory productCategory) {
	        this.productCategory = productCategory;
	    }

		public String getProductImg() {
			return productImg;
		}

		public void setProductImg(String productImg) {
			this.productImg = productImg;
		}

		public Area getArea() {
			return area;
		}

		public void setArea(Area area) {
			this.area = area;
		}

 
}
