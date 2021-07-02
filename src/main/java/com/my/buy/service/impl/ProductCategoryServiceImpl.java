package com.my.buy.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.buy.dao.ProductCategoryDao;
import com.my.buy.dao.ProductDao;
import com.my.buy.dto.HeadLineExecution;
import com.my.buy.dto.ImageHolder;
import com.my.buy.dto.ProductCategoryExecution;
import com.my.buy.entity.HeadLine;
import com.my.buy.entity.Product;
import com.my.buy.entity.ProductCategory;
import com.my.buy.enums.HeadLineStateEnum;
import com.my.buy.enums.ProductCategoryStateEnum;
import com.my.buy.exceptions.ProductCategoryOperationException;
import com.my.buy.exceptions.ProductOperationException;
import com.my.buy.service.ProductCategoryService;
import com.my.buy.util.ImageUtil;
import com.my.buy.util.PageCalculator;
import com.my.buy.util.PathUtil;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService
{

	@Autowired
	private ProductCategoryDao productCategoryDao;
	@Autowired
	private ProductDao productDao;
	/**
	 * F1:获取商品类别列表
	 * 一级、二级类别列表
	 */
	@Override
	public ProductCategoryExecution getProductCategoryList(ProductCategory productCategoryCondition,int pageIndex, int pageSize) {
		try
		{
			int rowIndex=PageCalculator.calculateRowIndex(pageIndex, pageSize);
			List<ProductCategory> productCategoryList=productCategoryDao.queryProductCategoryList(productCategoryCondition, rowIndex, pageSize);
			int productCategoryCount=productCategoryDao.queryproductCategoryCount(productCategoryCondition);
			ProductCategoryExecution pce=new ProductCategoryExecution();
			pce.setProductCategoryList(productCategoryList);
			pce.setCount(productCategoryCount);
			pce.setState(ProductCategoryStateEnum.SUCCESS.getState());
			return pce;
		}catch(Exception e)
		{
			throw new ProductOperationException("分页查询商品列表错误:"+e.getMessage());
		}  
	}
	@Override
	public int getProductCategoryCount(ProductCategory productCategoryCondition) { 
		return productCategoryDao.queryproductCategoryCount(productCategoryCondition);
	}
	/**
	 * F1-1:获取所有商品类别
	 */
	@Override
	public List<ProductCategory> getAllProductCategoryList(int pageIndex, int pageSize) {
		// TODO Auto-generated method stub
		int rowIndex=PageCalculator.calculateRowIndex(pageIndex, pageSize); 
		return productCategoryDao.queryAllProductCategoryList(rowIndex, pageSize);
	}
	@Override
	public int getAllProductCategoryCount()
	{
		return productCategoryDao.queryAllProductCategoryCount();
	}
	/**
	 * F2:根据商品类别Id查询商品
	 */
	@Override
	public ProductCategory getProductCategoryById(long productCategoryId) { 
		return productCategoryDao.queryProductCategoryById(productCategoryId);
	}
	/**
	 * F3:根据商品类别Id列表查询list
	 */
	@Override
	public List<ProductCategory> getProductCategoryByIds(List<Long> productCategoryIdList) {
		return productCategoryDao.queryProductCategoryByIds(productCategoryIdList);
	}
	
	/**
	 * F4:插入商品类别
	 */
	@Override
	@Transactional
	public ProductCategoryExecution addProductCategory(ProductCategory productCategory, ImageHolder thumbnail) {
		if(productCategory!=null)
		{
			
			productCategory.setCreateTime(new Date());
			productCategory.setLastEditTime(new Date());
			if(thumbnail!=null)
			{
				addThumbnail(productCategory, thumbnail);
			}
			try
			{
				int effectedNum=productCategoryDao.insertProductCategory(productCategory);
				if(effectedNum>0)
				{
					return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS, productCategory);
				}
				else
				{
					throw new ProductCategoryOperationException("商品类别添加失败");
				}
			}catch(Exception e)
			{
				throw new ProductCategoryOperationException("商品类别添加失败"+e.getMessage());
			}
		}
		else
		{
			return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY);
		} 
	}
	
	

	/**
	 * F5:修改商品类别
	 */
	@Override
	@Transactional
	public ProductCategoryExecution modifyProductCategory(ProductCategory productCategory, ImageHolder thumbnail) {
		if(productCategory.getProductCategoryId()>0&&productCategory.getProductCategoryId()!=null)
		{
			productCategory.setLastEditTime(new Date());
			if(thumbnail!=null)
			{
				ProductCategory tempProductCategory=productCategoryDao.queryProductCategoryById(productCategory.getProductCategoryId());
				if(tempProductCategory.getProductCategoryImg()!=null)
				{
					ImageUtil.deleteFileOrPath(tempProductCategory.getProductCategoryImg());
				}
				addThumbnail(productCategory, thumbnail);
			}
			try
			{
				int effectedNum=productCategoryDao.updateProductCategory(productCategory);
				if(effectedNum>0)
				{
					return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS, productCategory);
				}
				else
				{
					throw new ProductCategoryOperationException("商品类别修改失败");
				}
			}catch(Exception e)
			{
				throw new ProductCategoryOperationException("商品类别修改失败"+e.getMessage());
			}
		}
		else
		{
			return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY);
		} 
	}
	
	/**
	 * F6:删除商品类别
	 * 若该商品类别有图片则将该商品类别的图片删除
	 * 若该商品类别下有商品，则将该商品的商品类别置为空
	 */
	@Override
	@Transactional
	public ProductCategoryExecution removeProductCategory(long productCategoryId) {
		if(productCategoryId>0)
		{
			try{
				ProductCategory tempProductCategory=productCategoryDao.queryProductCategoryById(productCategoryId);
				if(tempProductCategory.getProductCategoryImg()!=null)
				{
					ImageUtil.deleteFileOrPath(tempProductCategory.getProductCategoryImg());
				} 
				int effectedNum=productCategoryDao.deleteProductCategory(productCategoryId);
				if(effectedNum>0)
				{
					return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
				}
				else
				{
					throw new ProductCategoryOperationException("商品类别删除失败");
				}
			}
			catch(Exception e)
			{
				throw new ProductCategoryOperationException("商品类别删除失败"+e.getMessage());
			}
		}else
		{
			return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY);
		} 
	}
	/**
	 * F7:批量删除商品类别
	 */
	@Override
	@Transactional
	public ProductCategoryExecution batchRemoveProductCategory(List<Long> productCategoryIdList) {
		if(productCategoryIdList!=null&&productCategoryIdList.size()>0)
		{
			try
			{
				List<ProductCategory> productCategoryList=productCategoryDao.queryProductCategoryByIds(productCategoryIdList);
				for(ProductCategory productCategory:productCategoryList)
				{
					if(productCategory.getProductCategoryImg()!=null)
					{
						ImageUtil.deleteFileOrPath(productCategory.getProductCategoryImg());
					}
				}
				int effectedNum=productCategoryDao.batchDeleteProductCategory(productCategoryIdList);
				if(effectedNum>0)
				{
					return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
				}
				else
				{
					throw new ProductCategoryOperationException("批量删除商品类别失败！");
				}
			}catch(Exception e)
			{
				throw new ProductCategoryOperationException("批量删除商品类别失败:"+e.getMessage());
			}
		} else
		{
			return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY);
		}
	}
	private void addThumbnail(ProductCategory productCategory, ImageHolder thumbnail) {
		// TODO Auto-generated method stub
		String dest = PathUtil.getProductCategoryImagePath();
		String thumbnailAddr = ImageUtil.generateNormalImg(thumbnail, dest);
		productCategory.setProductCategoryImg(thumbnailAddr);
	}
 
	
	
}
