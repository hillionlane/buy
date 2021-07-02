package com.my.buy.Service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.my.buy.BaseTest;
import com.my.buy.dao.ProductDao;
import com.my.buy.dto.ProductExecution;
import com.my.buy.entity.Area;
import com.my.buy.entity.PersonInfo;
import com.my.buy.entity.Product;
import com.my.buy.service.ProductService;

public class ProductServiceTest extends BaseTest
{
	@Autowired
	private ProductService productService;

	@Test
	@Ignore
	public void testGetProductList()
	{
		Product productCondition=new Product();
//		ProductCategory productCategory=new ProductCategory();
//		productCategory.setProductCategoryId(4L);
//		productCondition.setProductCategory(productCategory);
//		PersonInfo personInfo=new PersonInfo();
//		personInfo.setUserId(1L);
//		productCondition.setPersonInfo(personInfo);
//		productCondition.setProductName("te");
		Area area=new Area();
		area.setAreaId(1);
		productCondition.setArea(area);
		ProductExecution pe=productService.getProductList(productCondition, 1, 2);
		System.out.println(pe.getState());
	}
	
	@Test
	@Ignore
	public void testDeleteProduct()
	{
//		ProductExecution pe=productService.removeProductByProductId(20L);
//		assertEquals(1,pe.getState());
	}
	
	@Test
	public void testUpdateProduct()
	{
		Product product=new Product();
		PersonInfo user=new PersonInfo();
		user.setUserId(1L);
		product.setProductName("updateservice");
		product.setProductId(1L);
		product.setEnableSatus(1);
		product.setPersonInfo(user);
		ProductExecution pe=productService.modifyProduct(product, null, null);
		assertEquals(1,pe.getState() );
	}
}
