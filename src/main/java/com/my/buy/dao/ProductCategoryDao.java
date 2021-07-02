package com.my.buy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.my.buy.entity.ProductCategory;

public interface ProductCategoryDao 
{
	/**
	 * F1:根据查询条件查找商品类别
	 * 参数为一个商品类，根据这个类的父类有无来进行相应条件的查找
	 * @param productCategoryCondition
	 * @return
	 */
	public List<ProductCategory> queryProductCategoryList(@Param("productCategoryCondition")ProductCategory productCategoryCondition,@Param("rowIndex")int rowIndex,@Param("pageSize") int pageSize);
	public int queryproductCategoryCount(@Param("productCategoryCondition")ProductCategory productCategoryCondition);
	
	/**
	 * F1:查询全部的商品类别
	 */
	public List<ProductCategory> queryAllProductCategoryList(@Param("rowIndex")int rowIndex,@Param("pageSize") int pageSize);
	public int queryAllProductCategoryCount();
	/**
	 * F2:根据商品类别Id查询商品
	 * @param ProductCategory
	 * @return
	 */
	ProductCategory queryProductCategoryById(long productCategoryId);
	
	
	
	/**
	 * F3:根据商品类别Id列表查询list
	 * @param productCategoryIdList
	 * @return
	 */
	List<ProductCategory> queryProductCategoryByIds(List<Long> productCategoryIdList);
	
	int queryProductCategoryListCount(List<Long> productCategoryIdList);
	
	/**
	 * F4:插入商品类别
	 * @param productCategory
	 * @return
	 */
	int insertProductCategory(ProductCategory productCategory);

	/**
	 * F5:修改商品类别
	 * @param productCategory
	 * @return
	 */
	int updateProductCategory(ProductCategory productCategory);

	/**
	 * F6:删除商品类别
	 * @param productCategoryId
	 * @return
	 */
	int deleteProductCategory(long productCategoryId);

	/**
	 * F7:批量删除商品类别
	 * @param productCategoryIdList
	 * @return
	 */
	int batchDeleteProductCategory(List<Long> productCategoryIdList);
	
	
	 
	
	
}
