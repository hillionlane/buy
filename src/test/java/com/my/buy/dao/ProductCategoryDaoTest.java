package com.my.buy.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.my.buy.BaseTest;
import com.my.buy.entity.ProductCategory;

public class ProductCategoryDaoTest extends BaseTest
{
	@Autowired
	private ProductCategoryDao productCategoryDao;
	
	@Test  
	public void testQueryProductCategory()
	{
		ProductCategory parentCategory=new ProductCategory();
//		ProductCategory childCategory=new ProductCategory();
		
		 //查询父类为空的所有商品类别列表（即一级商品列表）
		/*int productCategoryCount=productCategoryDao.queryproductCategoryCount(parentCategory);*/
//		System.out.print(productCategoryCount);
// 		 List<ProductCategory> pc1=productCategoryDao.queryProductCategoryList(null,0,3);
		 List<ProductCategory> pc1=productCategoryDao.queryAllProductCategoryList(0, 3);
 		System.out.println(pc1.size());  
		 //查询父类不为空的所有商品类别列表（即所有二级商品列表）
//		List<ProductCategory> pc2=productCategoryDao.queryProductCategory(childCategory);
//		System.out.println(pc2.get(1).getParent().getProductCategoryId()); 
		//查询父类ID为2类的所有商品类别列表（即parentId=2的所有商品类别）
		/*parentCategory.setProductCategoryId(2L);
		childCategory.setParent(parentCategory);
		List<ProductCategory> pc3=productCategoryDao.queryProductCategory(childCategory);
		assertEquals(3, pc3.size());*/
	}
	
	@Test 
	@Ignore
	public void testqueryProductCategoryById()
	{
		ProductCategory productCategory=productCategoryDao.queryProductCategoryById(4L);
//		assertEquals("数码", productCategory.getProductCategoryName());
		System.out.println(productCategory.getParent().getProductCategoryId()); 
	}
	@Test 
	@Ignore
	public void testqueryProductCategoryByIds()
	{
		List<Long> productCategoryIds=new ArrayList<Long>();
		productCategoryIds.add(4L);
		productCategoryIds.add(5L);
		List<ProductCategory> productCategories=productCategoryDao.queryProductCategoryByIds(productCategoryIds);
//		int count=productCategoryDao.queryProductCategoryListCount(productCategoryIds);
//		assertEquals(2,count);
		System.out.println(productCategories.get(0).getParent().getProductCategoryId()); 
	}
	
	@Test  
	@Ignore
	public void testinsertProductCategory()
	{
		ProductCategory productCategory=new ProductCategory();
		productCategory.setProductCategoryName("testparent1");
		productCategory.setCreateTime(new Date());
		productCategory.setProductCategoryDesc("testdesc");
		productCategory.setPriority(3);
		productCategory.setProductCategoryImg("testimg1");
		ProductCategory parent=new ProductCategory(); 
		parent.setProductCategoryId(16L);
		productCategory.setParent(parent);
		int effectdNum=productCategoryDao.insertProductCategory(productCategory);
		assertEquals(1,effectdNum);
	}
	
	@Test
	@Ignore
	public void testupdateProductCategory()
	{
		ProductCategory productCategory=new ProductCategory();
		productCategory.setProductCategoryName("test2");
		productCategory.setCreateTime(new Date());
		productCategory.setProductCategoryDesc("testdesc");
		productCategory.setPriority(3);
		productCategory.setProductCategoryImg("testimg2"); 
		productCategory.setProductCategoryId(12L);
		int effectdNum=productCategoryDao.updateProductCategory(productCategory);
		assertEquals(1,effectdNum);
	}
	
	@Test   
	@Ignore
	public void testdeleteProductCategory()
	{
		int effectedNum=productCategoryDao.deleteProductCategory(16L);
		assertEquals(2,effectedNum);
	}
	@Test
	@Ignore
	public void batchDeleteProductCategory()
	{
		List<Long> productCategoryIds=new ArrayList<Long>();
		productCategoryIds.add(12L);
		productCategoryIds.add(14L);
		int effectedNum=productCategoryDao.batchDeleteProductCategory(productCategoryIds);
		assertEquals(2,effectedNum);
	}
}
