package com.my.buy.service.impl;

import java.awt.Image;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.buy.dao.HeadLineDao;
import com.my.buy.dto.HeadLineExecution;
import com.my.buy.dto.ImageHolder;
import com.my.buy.entity.HeadLine;
import com.my.buy.enums.HeadLineStateEnum;
import com.my.buy.exceptions.HeadLineOperationException;
import com.my.buy.exceptions.ProductOperationException;
import com.my.buy.service.HeadLineService;
import com.my.buy.util.ImageUtil;
import com.my.buy.util.PageCalculator;
import com.my.buy.util.PathUtil;
 
@Service
public class HeadLineServiceImpl implements HeadLineService{

	@Autowired
	private HeadLineDao headLineDao;
	
	/**
	 * F1:根据查询条件获取头条列表
	 */
	@Override
	public HeadLineExecution getHeadLineList(HeadLine headLineCondition,int pageIndex, int pageSize) {
		try
		{
			int rowIndex=PageCalculator.calculateRowIndex(pageIndex, pageSize);
			List<HeadLine> headLineList=headLineDao.queryHeadLineList(headLineCondition,rowIndex,pageSize);
			int headLineCount=headLineDao.queryHeadLineCount(headLineCondition);
			HeadLineExecution he=new HeadLineExecution();
			he.setHeadLineList(headLineList);
			he.setCount(headLineCount);
			he.setState(HeadLineStateEnum.SUCCESS.getState());
			return he;
		}catch(Exception e)
		{
			throw new ProductOperationException("分页查询商品列表错误:"+e.getMessage());
		} 
	}

	/**
	 * F2:增加Headline
	 */
	@Override
	@Transactional
	public HeadLineExecution addHeadLine(HeadLine headLine, ImageHolder thumbnail) {
		if (headLine != null) {
			headLine.setCreateTime(new Date());
			headLine.setLastEditTime(new Date());
			if (thumbnail != null) {
				addThumbnail(headLine, thumbnail);
			}
			try {
				int effectedNum = headLineDao.insertHeadLine(headLine);
				if (effectedNum > 0) {
					return new HeadLineExecution(HeadLineStateEnum.SUCCESS,
							headLine);
				} else {
					throw new HeadLineOperationException("头条添加失败");
				}
			} catch (Exception e) {
				throw new HeadLineOperationException("添加区域信息失败:" + e.toString());
			}
		} else {
			return new HeadLineExecution(HeadLineStateEnum.EMPTY);
		}
	}

	/**
	 * F3:修改headline
	 */
	@Override
	@Transactional
	public HeadLineExecution modifyHeadLine(HeadLine headLine, ImageHolder thumbnail) {
		if (headLine.getLineId() != null && headLine.getLineId() > 0) {
			headLine.setLastEditTime(new Date());
			if (thumbnail != null) {
				HeadLine tempHeadLine = headLineDao.queryHeadLineById(headLine
						.getLineId());
				if (tempHeadLine.getLineImg() != null) {
					ImageUtil.deleteFileOrPath(tempHeadLine.getLineImg());
				}
				addThumbnail(headLine, thumbnail);
			}
			try {
				int effectedNum = headLineDao.updateHeadLine(headLine);
				if (effectedNum > 0) {
					 
					return new HeadLineExecution(HeadLineStateEnum.SUCCESS,
							headLine);
				} else {
					throw new HeadLineOperationException("更新头条信息失败");
				}
			} catch (Exception e) {
				throw new HeadLineOperationException("更新头条信息失败:" + e.toString());
			}
		} else {
			return new HeadLineExecution(HeadLineStateEnum.EMPTY);
		}
	}

	/**
	 * F4:删除Headline
	 */
	@Override
	@Transactional
	public HeadLineExecution removeHeadLine(long headLineId) {
		if (headLineId > 0) {
			try {
				HeadLine tempHeadLine = headLineDao
						.queryHeadLineById(headLineId);
				if (tempHeadLine.getLineImg() != null) {
					ImageUtil.deleteFileOrPath(tempHeadLine.getLineImg());
				}
				int effectedNum = headLineDao.deleteHeadLine(headLineId);
				if (effectedNum > 0) {
					 
					return new HeadLineExecution(HeadLineStateEnum.SUCCESS);
				} else {
					throw new HeadLineOperationException("头条删除失败！");
				}
			} catch (Exception e) {
				throw new HeadLineOperationException("删除头条信息失败:" + e.toString());
			}
		} else {
			return new HeadLineExecution(HeadLineStateEnum.EMPTY);
		}
	}

	/**
	 * F5:批量删除headlines
	 */
	@Override
	@Transactional
	public HeadLineExecution removeHeadLineList(List<Long> headLineIdList) {
		if (headLineIdList != null && headLineIdList.size() > 0) {
			try {
				List<HeadLine> headLineList = headLineDao
						.queryHeadLineByIds(headLineIdList);
				for (HeadLine headLine : headLineList) {
					if (headLine.getLineImg() != null) {
						ImageUtil.deleteFileOrPath(headLine.getLineImg());
					}
				}
				int effectedNum = headLineDao
						.batchDeleteHeadLine(headLineIdList);
				if (effectedNum > 0) {
					 
					return new HeadLineExecution(HeadLineStateEnum.SUCCESS);
				} else {
					throw new RuntimeException("删除头条信息失败");
				}
			} catch (Exception e) {
				throw new HeadLineOperationException("删除头条信息失败:" + e.toString());
			}
		} else {
			return new HeadLineExecution(HeadLineStateEnum.EMPTY);
		}
	}
	private void addThumbnail(HeadLine headLine, ImageHolder thumbnail) {
		String dest = PathUtil.getHeadLineImagePath();
		String thumbnailAddr = ImageUtil.generateNormalImg(thumbnail, dest);
		headLine.setLineImg(thumbnailAddr);
	}

	@Override
	public int getHeadLineCount(HeadLine headLineCondition) {
		// TODO Auto-generated method stub
		return headLineDao.queryHeadLineCount(headLineCondition);
	}
}
