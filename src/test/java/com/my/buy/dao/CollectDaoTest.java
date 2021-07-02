package com.my.buy.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.my.buy.BaseTest;
import com.my.buy.entity.Collect;
import com.my.buy.entity.PersonInfo;
import com.my.buy.entity.Product;

public class CollectDaoTest extends BaseTest{
	@Autowired
	private CollectDao collectDao;
	@Test  
	public void testInsertCollection()
	{
		PersonInfo user=new PersonInfo();
		user.setUserId(1L);
		Product product=new Product();
		product.setProductId(1L);
		Collect collection=new Collect();
		collection.setUserId(user.getUserId());
		collection.setProduct(product);
		collection.setStatus(1);
		int effecteNum=collectDao.insertCollection(collection);
		assertEquals(1,effecteNum);
	}
	
	@Test
	@Ignore
	public void testSelectCollection()
	{
		 
		Collect collection=collectDao.selectCollection(1, 1);
//		assertEquals(collection.getCollectId(),16);
	}
	
	@Test
	@Ignore
	public void testDeleteCollection()
	{
		int effectedNum=collectDao.deleteCollection(1, 1);
		assertEquals(1,effectedNum);
	}
	
	@Test
	@Ignore
	public void testQueryCollectList()
	{
		List<Collect> collectionList=collectDao.queryCollectListByUserId(1L,0,2);
		System.out.println(collectionList.get(0).getProduct().getProductName());
	}
}
