package com.my.buy.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.my.buy.dto.ImageHolder;
import com.my.buy.dto.ProductCategoryExecution;
import com.my.buy.entity.HeadLine;
import com.my.buy.entity.ProductCategory;

public interface ProductCategoryService {
	
	/**
	 * F1:获取商品类别列表
	 * @param productCategoryCondition
	 * @return
	 */
	ProductCategoryExecution getProductCategoryList(@Param("productCategoryCondition")ProductCategory productCategoryCondition,int pageIndex,int pageSize);
	int getProductCategoryCount(@Param("productCategoryCondition")ProductCategory productCategoryCondition);
	
	/**
	 * F1-1:获取所有商品类别
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	List<ProductCategory> getAllProductCategoryList(int pageIndex,int pageSize);
	int getAllProductCategoryCount();
	/**
	 * F2:根据商品类别Id查询商品
	 * @param ProductCategory
	 * @return
	 */
	ProductCategory getProductCategoryById(long productCategoryId);
	
	/**
	 * F3:根据商品类别Id列表查询list
	 * @param productCategoryIdList
	 * @return
	 */
	List<ProductCategory> getProductCategoryByIds(List<Long> productCategoryIdList);
	
	/**
	 * F4:插入商品类别
	 * @param productCategory
	 * @return
	 */
	ProductCategoryExecution addProductCategory(ProductCategory productCategory,ImageHolder thumbnail);
	
	/**
	 * F5:修改商品类别
	 * @param productCategory
	 * @return
	 */
	ProductCategoryExecution modifyProductCategory(ProductCategory productCategory,ImageHolder thumbnail);
	
	/**
	 * F6:删除商品类别
	 * @param productCategoryId
	 * @return
	 */
	ProductCategoryExecution removeProductCategory(long productCategoryId);
	
	/**
	 * F7:批量删除商品类别
	 * @param productCategoryIdList
	 * @return
	 */
	ProductCategoryExecution batchRemoveProductCategory(List<Long> productCategoryIdList);
	
	
}
