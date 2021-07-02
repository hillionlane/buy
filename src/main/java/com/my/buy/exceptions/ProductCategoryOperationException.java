package com.my.buy.exceptions;

public class ProductCategoryOperationException extends RuntimeException{
	/**
	 * 生成随机序列化的ID
	 */
	private static final long serialVersionUID = 2361446884822298905L;

	public ProductCategoryOperationException(String msg)
	{
		super(msg);
	}
}
