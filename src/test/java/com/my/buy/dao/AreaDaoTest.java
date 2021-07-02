package com.my.buy.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test; 
import org.springframework.beans.factory.annotation.Autowired;

import com.my.buy.BaseTest;
import com.my.buy.entity.Area; 
 
 
public class AreaDaoTest extends BaseTest
{
	//用Autowired引入这个Dao，进而进行测试
	@Autowired
	private AreaDao areaDao;
	
	@Test
	@Ignore
	public void testQueryArea()
	{
		List<Area> areaList=areaDao.queryArea();
	 
		int effected=areaList.size();
		assertEquals(5, effected);
	}
	@Test
	@Ignore
	public void testQueryAreaById()
	{
		
		Area area=areaDao.queryAreaById(1L);
		assertEquals("前湖校区", area.getAreaName());
	}
	@Test 
	@Ignore
	public void testinsertArea()
	{
		Area area=new Area();
		area.setAreaName("test4");
		area.setAreaDesc("testdesc4");
		area.setPriority(5);
		area.setCreateTime(new Date());
		int effectedNum=areaDao.insertArea(area);
		assertEquals(1, effectedNum);
	}
	
	@Test
	@Ignore
	public void testUpdateArea()
	{
		Area area=new Area();
		area.setAreaDesc("updateDesc");
		area.setLastEditTime(new Date());
		area.setAreaId(6L);
		int effectedNum=areaDao.updateArea(area);
		assertEquals(1, effectedNum);
	}
	
	@Test 
	@Ignore
	public void testDeleteArea()
	{
		int effectedNum=areaDao.deleteArea(9L);
		assertEquals(1, effectedNum);
	}
	
	@Test
	public void testBatchDeleteArea()
	{
		Area area1=new Area();
		area1.setAreaId(7L);
		Area area2=new Area();
		area2.setAreaId(8L);
		List<Long> areaIdList=new ArrayList<Long>();
		areaIdList.add(area1.getAreaId());
		areaIdList.add(area2.getAreaId());
		int effectedNum=areaDao.batchDeleteArea(areaIdList);
		assertEquals(2, effectedNum);
		
	}
}

