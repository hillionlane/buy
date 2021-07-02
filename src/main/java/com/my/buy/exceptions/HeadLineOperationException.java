package com.my.buy.exceptions;

public class HeadLineOperationException extends RuntimeException{
	/**
	 * 生成随机序列化的ID
	 */
	private static final long serialVersionUID = 2361446884822298905L;

	public HeadLineOperationException(String msg)
	{
		super(msg);
	}
}
