package com.my.buy.service;

import java.util.List;

import com.my.buy.dto.ImageHolder;
import com.my.buy.dto.ProductExecution;
import com.my.buy.entity.Product;
import com.my.buy.exceptions.ProductOperationException;

public interface ProductService 
{
	/**
	 * F1:增加商品
	 * @param product
	 * @param thumbnail
	 * @param productImgList
	 * @return
	 */
	ProductExecution addProduct(Product product,ImageHolder thumbnail,List<ImageHolder> productImgList)throws ProductOperationException;
	
	/**
	 * F2:修改商品信息
	 * @param product
	 * @param thumbnail
	 * @param productImgList
	 * @return
	 * @throws ProductOperationException
	 */
	ProductExecution modifyProduct(Product product,ImageHolder thumbnail,List<ImageHolder> productImgList)throws ProductOperationException;
	
	/**
	 * F3:根据对应的查询条件分页获取商品列表
	 * @param productCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	ProductExecution getProductList(Product productCondition,int pageIndex,int pageSize);
	
	/**
	 * F4:根据商品id获取指定商品信息
	 * @param peoductId
	 * @return
	 */
	Product getProductById(long productId);
	
	/**
	 * F5:根据商品id删除指定商品（并在删除该商品之前删除该商品下商品详情图删除）
	 * @param productId
	 * @return
	 */
	ProductExecution removeProductByProductId(long productId);
}
