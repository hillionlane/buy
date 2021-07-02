package com.my.buy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.buy.dao.CollectDao;
import com.my.buy.dto.CollectExecution;
import com.my.buy.entity.Collect;
import com.my.buy.enums.CollectStateEnum;
import com.my.buy.exceptions.CollectOperationException;
import com.my.buy.service.CollectService;
import com.my.buy.util.PageCalculator;

@Service
public class CollectServiceImpl implements CollectService
{
	@Autowired 
	private CollectDao collectDao;

	/**
	 * F1:添加收藏
	 */
	@Override
	@Transactional
	public CollectExecution addCollection(Collect collection) {
		 if(collection.getUserId()>0&&collection.getProduct()!=null&&collection.getProduct().getProductId()>0)
		 {
			 try
			 {
				 int effectdNum=collectDao.insertCollection(collection);
				 if(effectdNum>0)
				 {
					 return new CollectExecution(CollectStateEnum.SUCCESS,collection);
				 }
				 else
				 {
					 throw new CollectOperationException("收藏失败！");
				 }
			 }
			 catch(Exception e)
			 {
				 throw new CollectOperationException("收藏失败:"+e.getMessage());
			 }
		 }
		 else
		 {
			 return new CollectExecution(CollectStateEnum.EMPTY);
		 } 
	}

	/**
	 * F2:查询是否已被收藏
	 */
	@Override
	public Collect getCollection(long userId, long productId) {
		return collectDao.selectCollection(userId, productId);
	}

	/**
	 * F3:移除收藏
	 */
	@Override
	@Transactional
	public CollectExecution removeCollection(long userId, long productId) {
		if(userId>0&&productId>0)
		{ 
			try
			{
				int effectedNum=collectDao.deleteCollection(userId, productId);
				if(effectedNum>0)
				{
					return new CollectExecution(CollectStateEnum.SUCCESS);
				}
				else
				{
					throw new CollectOperationException("取消收藏失败！");
				}
			}catch(Exception e)
			{
				throw new CollectOperationException("取消收藏失败！"+e.getMessage());
			}
		}
		else
		{
			return new CollectExecution(CollectStateEnum.EMPTY);
		} 
	}


	/**
	 * F4:获取某用户收藏列表
	 */
	@Override
	@Transactional
	public CollectExecution getCollectListByUserId(long userId, int pageIndex, int pageSize) 
	{
		CollectExecution ce=new CollectExecution();
		if(userId>0&&pageIndex>0&&pageSize>0)
		{
		try
		{
			int rowIndex=PageCalculator.calculateRowIndex(pageIndex, pageSize);
			List<Collect> collectionList=collectDao.queryCollectListByUserId(userId, rowIndex, pageSize);
			int collectionCount=collectDao.queryCollectionCount(userId, rowIndex, pageSize);
			if(collectionList.size()>0&&collectionList!=null)
			{
				ce.setCollectionList(collectionList);
				ce.setState(CollectStateEnum.SUCCESS.getState());
				ce.setCount(collectionCount);
			}
			else
			{
				ce.setState(CollectStateEnum.INNER_ERROR.getState());
			}
		}catch(CollectOperationException e)
		{
			throw new CollectOperationException("查询我的收藏失败:"+e.getMessage());
		} 
		}
		else
		{
			ce.setState(CollectStateEnum.EMPTY.getState());
		}

		return ce;
	}
	 
	
}
