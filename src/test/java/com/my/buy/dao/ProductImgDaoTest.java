package com.my.buy.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.my.buy.BaseTest;
import com.my.buy.entity.Product;
import com.my.buy.entity.ProductImg;

public class ProductImgDaoTest extends BaseTest
{
	@Autowired
	private ProductImgDao productImgDao;
	
	@Test
	@Ignore
	public void testIBatchInsertProductImg()
	{
		List<ProductImg> productImgList=new ArrayList<ProductImg>();
		ProductImg p1=new ProductImg();
		ProductImg p2=new ProductImg();
		p1.setPriority(1);
		p1.setCreateTime(new Date());
		p1.setImgAddr("test1");
		p1.setImgDesc("商品Id为5的详情图1");
		p1.setProductId(1L);
		p2.setPriority(2);
		p2.setCreateTime(new Date());
		p2.setImgAddr("test2");
		p2.setImgDesc("商品Id为5的详情图2");
		p2.setProductId(1L);
		productImgList.add(p1);
		productImgList.add(p2);
		int effectedNum=productImgDao.batchInsertProductImg(productImgList);
		assertEquals(2,effectedNum);
	}
	@Test
	@Ignore
	public void testQueryProductImgList()
	{
		List<ProductImg> productImgList=productImgDao.queryProductImgList(1L);
		assertEquals(6,productImgList.size());
	}
	@Test
	public void testupdateProductImg()
	{
		Product product=new Product();
		product.setProductId(16L);
		List<ProductImg> productImgList=new ArrayList<ProductImg>();
		ProductImg p1=new ProductImg();
		ProductImg p2=new ProductImg();
		p1.setImgAddr("test11");
		p2.setImgAddr("test22");
		p1.setProductId(product.getProductId());
		p2.setProductId(product.getProductId());
		productImgList.add(p1);
		productImgList.add(p2);
		int effectedNum=productImgDao.updateProductImg(productImgList);
		assertEquals(2,effectedNum);
	}
}
