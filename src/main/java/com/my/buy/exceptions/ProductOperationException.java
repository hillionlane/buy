package com.my.buy.exceptions;

public class ProductOperationException extends RuntimeException
{
	/**
	 * 生成随机序列化的ID
	 */
	private static final long serialVersionUID = 2361446884822298905L;

	public ProductOperationException(String msg)
	{
		super(msg);
	}
}

