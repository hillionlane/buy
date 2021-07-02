package com.my.buy.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.my.buy.dto.HeadLineExecution;
import com.my.buy.dto.ImageHolder;
import com.my.buy.entity.HeadLine;

public interface HeadLineService 
{
	/**
	 * F1:根据查询条件获取相应的头条列表
	 * @param headLineCondition
	 * @return
	 */
	HeadLineExecution getHeadLineList(@Param("headLineCondition")HeadLine headLineCondition,int pageIndex,int pageSize);
	
	int getHeadLineCount(@Param("headLineCondition")HeadLine headLineCondition);
	/**
	 * F2:添加headline
	 * @param headLine
	 * @param thumbnail
	 * @return
	 */
	HeadLineExecution addHeadLine(HeadLine headLine,
			ImageHolder thumbnail);

	/**
	 * F3:修改headline
	 * @param headLine
	 * @param thumbnail
	 * @param thumbnailChange
	 * @return
	 */
	HeadLineExecution modifyHeadLine(HeadLine headLine,
			ImageHolder thumbnail);

	/**
	 * F4:根据Id删除headline
	 * @param headLineId
	 * @return
	 */
	HeadLineExecution removeHeadLine(long headLineId);

	/**
	 * F5:批量删除headlines
	 * @param headLineIdList
	 * @return
	 */
	HeadLineExecution removeHeadLineList(List<Long> headLineIdList);
}

