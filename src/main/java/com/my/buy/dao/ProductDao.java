package com.my.buy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.my.buy.entity.Product;

public interface ProductDao 
{
	/**
	 * 插入商品
	 * @param product
	 * @return
	 */
	int insertProduct(Product product);
	
	/**
	 * 更新商品信息
	 * @param product
	 * @return
	 */
	int updateProduct(Product product);
	
	/**
	 * 通过商品Id查询商品
	 * @param productId
	 * @return
	 */
	Product queryProductById(long productId);
	
	/**
	 * 删除指定商品
	 * @param productId
	 * @return
	 */
	int deleteProductByProductId(long productId);
 
	  
	/**
	 * 分页查询商品列表
	 * 可输入的条件有：商品名称，商品状态，商品类别，用户id
	 * @param productCondition
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	List<Product> queryProductList(@Param("productCondition")Product productCondition,
			@Param("rowIndex")int rowIndex,@Param("pageSize") int pageSize);
	
	/**
	 * 获取某条件下的所有商品类别数量
	 * @param productCondition
	 * @return
	 */
	int queryProductCount(@Param("productCondition")Product productCondition);
	
	
}
