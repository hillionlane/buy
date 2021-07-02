package com.my.buy.dao;

import java.util.List;

import com.my.buy.entity.ProductImg;

public interface ProductImgDao 
{
	/**
	 * 批量添加商品详情图片
	 * @param productImg
	 * @return
	 */
	int batchInsertProductImg(List<ProductImg> productImg);
	
	/**
	 * 批量修改商品详情图片
	 * @param productImgList
	 * @return
	 */
	int updateProductImg(List<ProductImg> productImgList);
	
	/**
	 * 查询商品详情图片列表
	 * @param productId
	 * @return
	 */
	List<ProductImg> queryProductImgList(long productId);
	
	/**
	 * 删除指定商品下的详情图片(在操作指定商品之前)
	 * @param productId
	 * @return
	 */
	int deleteProductImgByProductId(long productId);
}
