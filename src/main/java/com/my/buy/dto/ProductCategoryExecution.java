package com.my.buy.dto;

import java.util.List;

import com.my.buy.entity.ProductCategory;
import com.my.buy.enums.ProductCategoryStateEnum;

public class ProductCategoryExecution {
	// 结果状态
		private int state;
		private int count;
		// 状态标识
		private String stateInfo;
		private ProductCategory productCategory;
		// 操作的商铺类别
		private List<ProductCategory> productCategoryList;

		public ProductCategoryExecution() {
		}

		// 预约失败的构造器
		public ProductCategoryExecution(ProductCategoryStateEnum stateEnum) {
			this.state = stateEnum.getState();
			this.stateInfo = stateEnum.getStateInfo();
		}

		// 预约成功的构造器 
				public ProductCategoryExecution(ProductCategoryStateEnum stateEnum,ProductCategory productCategory) {
					this.state = stateEnum.getState();
					this.stateInfo = stateEnum.getStateInfo();
					this.productCategory=productCategory;
				}
		public ProductCategoryExecution(ProductCategoryStateEnum stateEnum,
				List<ProductCategory> productCategoryList,int count) {
			this.state = stateEnum.getState();
			this.stateInfo = stateEnum.getStateInfo();
			this.productCategoryList = productCategoryList;
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

		public List<ProductCategory> getProductCategoryList() {
			return productCategoryList;
		}

		public void setProductCategoryList(List<ProductCategory> productCategoryList) {
			this.productCategoryList = productCategoryList;
		}

		public ProductCategory getProductCategory() {
			return productCategory;
		}

		public void setProductCategory(ProductCategory productCategory) {
			this.productCategory = productCategory;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

}
