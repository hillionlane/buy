package com.my.buy.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.my.buy.BaseTest;
import com.my.buy.entity.HeadLine;

public class HeadLineDaoTest extends BaseTest 
{
	@Autowired
	private HeadLineDao headLineDao;
	
	@Test 
	public void testQueryHeadLine()
	{
		HeadLine headLine=new HeadLine();
//		headLine.setEnableStatus(1);
		List<HeadLine> headLineList=headLineDao.queryHeadLineList(headLine,0,3);
//		assertEquals(3,headLineList.size());
//		System.out.println(headLineList.size());
		int count=headLineDao.queryHeadLineCount(headLine);
		System.out.println(count);
	}
	
	@Test
	@Ignore
	public void testqueryHeadLineById()
	{
		HeadLine headline=headLineDao.queryHeadLineById(1L);
		System.out.println(headline.getLineName());
	}
	
	@Test
	@Ignore
	public void testqueryHeadLineByIds()
	{
		List<Long> headlineIds=new ArrayList<Long>();
		headlineIds.add(1L);
		headlineIds.add(2L);
		List<HeadLine> headLines=headLineDao.queryHeadLineByIds(headlineIds);
		assertEquals(2,headLines.size());
	}
	@Test
	@Ignore
	public void testinsertHeadLine()
	{
		HeadLine headLine=new HeadLine();
		headLine.setLineName("test2");
		headLine.setLineLink("testlink2");
		headLine.setCreateTime(new Date());
		headLine.setLineImg("testImg2");
		headLine.setPriority(1);
		headLine.setEnableStatus(1);
		int effectedNum=headLineDao.insertHeadLine(headLine);
		assertEquals(1, effectedNum);
	}
	
	@Test
	@Ignore
	public void testupdateHeadLine()
	{
		HeadLine headLine=new HeadLine();
		headLine.setLineName("updatetest");
		headLine.setLineId(5L);
		int effectedNum=headLineDao.updateHeadLine(headLine);
		assertEquals(1, effectedNum);
	}
	
	@Test
	@Ignore
	public void testdeleteHeadLine()
	{
		int effectedNum=headLineDao.deleteHeadLine(5L);
		assertEquals(1, effectedNum);
	}
	
	@Test
	@Ignore
	public void testbatchDeleteHeadLine()
	{
		List<Long> headlineIds=new ArrayList<Long>();
		headlineIds.add(4L);
		headlineIds.add(6L);
		int effectedNum=headLineDao.batchDeleteHeadLine(headlineIds);
		assertEquals(2, effectedNum);
	}
}
