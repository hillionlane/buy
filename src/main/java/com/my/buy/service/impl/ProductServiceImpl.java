package com.my.buy.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.buy.dao.PersonInfoDao;
import com.my.buy.dao.ProductDao;
import com.my.buy.dao.ProductImgDao;
import com.my.buy.dto.ImageHolder;
import com.my.buy.dto.ProductExecution;
import com.my.buy.entity.Area;
import com.my.buy.entity.PersonInfo;
import com.my.buy.entity.Product;
import com.my.buy.entity.ProductImg;
import com.my.buy.enums.ProductStateEnum;
import com.my.buy.exceptions.ProductOperationException;
import com.my.buy.service.ProductService;
import com.my.buy.util.ImageUtil;
import com.my.buy.util.PageCalculator;
import com.my.buy.util.PathUtil;

@Service
public class ProductServiceImpl implements ProductService
{

	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductImgDao productImgDao;
	@Autowired
	private PersonInfoDao userDao;
	/**
	 * F1:添加商品（包括对商品缩略图以及商品详情图片的处理）
	 * 1:处理缩略图，获取缩略图相对路径并赋值给product
	 * 2:往tb_product中写入商品信息，获取productId
	 * 3:结合productId批量处理商品详情图
	 * 4:将商品详情图列表批量插入tb_product_img中
	 */
	@Override
	@Transactional
	public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList)
			throws ProductOperationException 
	{
		//空值判断
		if(product!=null&&product.getPersonInfo()!=null&&product.getPersonInfo().getUserId()!=null)
		{
			String waterMarkName="@"+product.getPersonInfo().getUserName();	 
			product.setCreateTime(new Date());
			product.setLastEditTime(new Date()); 
			//给商品名获取当前操作用户的区域Id
			if(product.getPersonInfo().getArea()!=null&&product.getPersonInfo().getArea().getAreaId()>0)
			{
				product.setArea(product.getPersonInfo().getArea());
			}
			//默认为上架状态
			product.setEnableSatus(1);
			if(thumbnail!=null)
			{
				/**
				 * F1-1:添加缩略图
				 */
				addThumbnail(product,thumbnail,waterMarkName); 
			}
			try
			{
				//创建商品信息
				int effectedNum=productDao.insertProduct(product);
				if(effectedNum<0)
				{
					throw new ProductOperationException("新增商品失败");
				}
			}catch(Exception e)
			{
				throw new ProductOperationException("新增商品失败:"+e.getMessage());
			}
			//若商品详情图不为空则添加
			if(productImgList!=null&&productImgList.size()>0)
			{
				/**
				 * F1-2:添加详情图列表
				 */
				addProductImgList(product,productImgList,waterMarkName);
			} 
			return new ProductExecution(ProductStateEnum.SUCCESS,product);
			
		}
		else
		{
			return new ProductExecution(ProductStateEnum.EMPTY);
		} 
	}

	
	/**
	 * F1-1:添加缩略图
	 * 
	 * @param product
	 * @param thumbnail
	 */
	private void addThumbnail(Product product, ImageHolder thumbnail,String waterMarkName) {
		 
		String targetAddr=PathUtil.getProductImagePath(product.getPersonInfo().getUserId()); 
		String thumbnailAddr=ImageUtil.generateNormalWordImg(thumbnail, targetAddr,waterMarkName);
		product.setProductImg(thumbnailAddr);
	}


	/**
	 * F1-2:批量添加详情图列表（加入tb_product_img表中）
	 * @param product
	 * @param productImgList
	 */
	private void addProductImgList(Product product, List<ImageHolder> productImgList,String waterMarkName) {
		String dest=PathUtil.getProductImagePath(product.getPersonInfo().getUserId());
		List<ProductImg> productImgs=new ArrayList<ProductImg>();
		for(ImageHolder pi:productImgList)
		{
			String imgAddr=ImageUtil.generateNormalWordImg(pi, dest,waterMarkName);
			ProductImg productImg=new ProductImg();
			productImg.setImgAddr(imgAddr);
			productImg.setProductId(product.getProductId());
			productImg.setCreateTime(new Date());
			productImgs.add(productImg);
		}
		//若productImgs不为空，则批量添加 
		if(productImgs.size()>0)
		{
			try
			{
				int effectedNum=productImgDao.batchInsertProductImg(productImgs);
				if(effectedNum<=0)
				{
					throw new ProductOperationException("创建商品详情图失败！");
				}
			}catch(Exception e)
			{
				throw new ProductOperationException("创建商品详情图失败："+e.getMessage());
			}
		} 
		
	}
	/**
	 * F2:修改商品
	 * 1.处理缩略图，获取缩略图相对路径并赋值给product
	 * 2.往tb_product中写入商品信息，获取productId 
	 * 3:结合productId批量处理商品详情图
	 * 4:将商品详情图列表批量插入tb_product_img中
	 */
	@Override
	@Transactional
	public ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList)
			throws ProductOperationException {
		 //空值判断
		if(product!=null)
		{
			String waterMarkName=null;
			//给商品附上默认值
			product.setLastEditTime(new Date());
			//若商品缩略图不为空且有原缩略图不为空则删除原有缩略图并添加
			//现获取一遍原有信息，因为原来的信息里有原图片地址
			Product tempProduct=productDao.queryProductById(product.getProductId());
			PersonInfo productUser=userDao.queryUserById(tempProduct.getPersonInfo().getUserId());
			waterMarkName="@"+productUser.getUserName();	 
			if(thumbnail!=null)
			{
				if(tempProduct.getProductImg()!=null)
				{
					ImageUtil.deleteFileOrPath(tempProduct.getProductImg());
					product.setPersonInfo(productUser);
					addThumbnail(tempProduct, thumbnail,waterMarkName);
				}
			}
			//若果有新存入的图片详情图，则现将原来的删除，并添加新的图片
			if(productImgList!=null&&productImgList.size()>0)
			{
				deleteProductImgList(product.getProductId());
				addProductImgList(tempProduct,productImgList,waterMarkName);
			}
			try{
				//更新商品信息
				int effectedNum=productDao.updateProduct(product);
				if(effectedNum<0)
				{
					throw new ProductOperationException("更新商品失败！");
				}
				return new ProductExecution(ProductStateEnum.SUCCESS);
			}catch(Exception e)
			{
				System.out.println("错误："+e.getMessage());
				throw new ProductOperationException("update product error:"+e.getMessage());
				
			}
		}
		else
		{
			return new ProductExecution(ProductStateEnum.EMPTY);
		}  
	}

	/**
	 * F2-1:删除数据库中原来的图片并添加新图片
	 * @param productId
	 */
	private void deleteProductImgList(Long productId) {
		List<ProductImg> productImgList=productImgDao.queryProductImgList(productId);
		 for(ProductImg pi:productImgList)
		 {
			 if(pi!=null)
			 {
				 ImageUtil.deleteFileOrPath(pi.getImgAddr());
			 }
		 }
		 //删除数据库中原有图片信息
		
		 productImgDao.deleteProductImgByProductId(productId);
		
	}


	/**
	 * F3:根据组合条件分页获取相应的商品列表
	 * 条件:同一商品类别、同一用户、同一区域、模糊查询的名字、上架产品(enableStatus=1)
	 */
	@Override
	public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
		try
		{
			int rowIndex=PageCalculator.calculateRowIndex(pageIndex, pageSize);
			List<Product> productList=productDao.queryProductList(productCondition, rowIndex, pageSize);
			int productCount=productDao.queryProductCount(productCondition);
			ProductExecution pe=new ProductExecution();
			/*if(productList.size()<0||productList==null)
			{
				pe.setState(ProductStateEnum.INNER_ERROR.getState());
			}
			else
			{*/
				pe.setProductList(productList);
				pe.setCount(productCount);
				pe.setState(ProductStateEnum.SUCCESS.getState());
			/*}*/
			return pe;
		}catch(Exception e)
		{
			throw new ProductOperationException("分页查询商品列表错误:"+e.getMessage());
		} 
	}

	/**
	 * F4:根据商品Id查询商品信息
	 */
	@Override
	public Product getProductById(long productId) {
		return productDao.queryProductById(productId);
	}


	/**
	 * F5:根据商品id删除指定商品（并在删除该商品之前删除该商品下商品详情图删除）
	 */
	@Override
	@Transactional
	public ProductExecution removeProductByProductId(long productId) {
		if(productId>0)
		{
			int effectedNum=0;
			try
			{
				List<ProductImg> productImgList=productImgDao.queryProductImgList(productId);
				Product tempProduct=productDao.queryProductById(productId);
				if(tempProduct.getProductImg()!=null)
				{
					//删除项目目录下的图片
					ImageUtil.deleteFileOrPath(tempProduct.getProductImg());
					if(productImgList!=null&&productImgList.size()>0)
					{
						//删除数据库中的详情图片
//						effectedNum=productImgDao.deleteProductImgByProductId(productId);
						deleteProductImgList(productId);
					} 
				} 
					try
					{
						
						effectedNum=productDao.deleteProductByProductId(productId);
						//删除项目路径下的该商品包含的图片
						if(effectedNum>0)
						{
							return new ProductExecution(ProductStateEnum.SUCCESS);
						}
						else
						{
							return new ProductExecution(ProductStateEnum.NULL_PRODUCT);
						}
					}
					catch(Exception e)
					{
						throw new ProductOperationException("商品删除失败！"+e.getMessage());
					} 
				}  
			catch(Exception e)
			{
				throw new ProductOperationException("商品删除失败！"+e.getMessage());
			}
			
		}
		else
		{
			return new ProductExecution(ProductStateEnum.NULL_PRODUCTID);
		} 
	}
	 
}
