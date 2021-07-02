package com.my.buy.util;

public class PageCalculator {
	public static int calculateRowIndex(int pageIndex,int pageSize)
	{
		
		return (pageIndex>0)?pageSize*(pageIndex-1):0;
	}
}
