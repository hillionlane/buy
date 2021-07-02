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
import com.my.buy.entity.PersonInfo;
import com.my.buy.entity.Product;
import com.my.buy.entity.ProductCategory;

public class ProductDaoTest extends BaseTest 
{
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductImgDao productImgDao;
	@Test  
	@Ignore
	public void testQueryProductById()
	{
		Product product=new Product();
		product=productDao.queryProductById(1L);
		System.out.println(product.getProductCategory().getProductCategoryName());
	}
	
	@Test 
	public void testQueryProductListAndCount()
	{
		Product productCondition=new Product();
//		ProductCategory productCategory=new ProductCategory();
//		productCategory.setProductCategoryId(4L);
//		productCondition.setProductCategory(productCategory);
//		PersonInfo personInfo=new PersonInfo();
//		personInfo.setUserId(1L);
//		productCondition.setPersonInfo(personInfo);
//		productCondition.setProductName("te");
//		System.out.println(productCondition.getEnableStatus());
		Area area=new Area();
		area.setAreaId(1);
		productCondition.setArea(area);
		List<Product> products=productDao.queryProductList(productCondition, 0, 2);
		int effectedNum=productDao.queryProductCount(productCondition);
		System.out.println(products.get(0).getArea().getAreaName());
		System.out.println(products.get(0).getPersonInfo().getUserName());
	}
	
	@Test
	@Ignore
	public void testInsertProduct()
	{
		Product product=new Product();
		product.setProductName("testname");
		product.setProductDesc("这是测试类");
		PersonInfo user=new PersonInfo();
		user.setUserId(1L);
		product.setPersonInfo(user);
		product.setProductPrice("20");
		product.setCreateTime(new Date());
		ProductCategory pc=new ProductCategory();
		pc.setProductCategoryId(2L);
		product.setProductCategory(pc);
		Area area=new Area();
		area.setAreaId(1L);
		product.setArea(area);
		product.setEnableSatus(1);
		product.setPriority(1);
		int effectedNum=productDao.insertProduct(product);
		assertEquals(1, effectedNum);
	}
	@Test
	@Ignore
	public void testDeleteProductByProductId()
	{
		int effecteNum1=productImgDao.deleteProductImgByProductId(20L);
		assertEquals(2,effecteNum1);
		int effectedNum2=productDao.deleteProductByProductId(20L);
		assertEquals(1, effectedNum2);
	}
	
	@Test
	@Ignore
	public void testUpdateProduct()
	{
		Product product=new Product();
		product.setProductName("update");
		product.setProductId(1L);
		product.setEnableSatus(0);
		int effectedNum=productDao.updateProduct(product);
		assertEquals(1,effectedNum );
	}
}
