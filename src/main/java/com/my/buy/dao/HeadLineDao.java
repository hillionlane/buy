package com.my.buy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.my.buy.entity.HeadLine;
import com.my.buy.entity.Product;

public interface HeadLineDao 
{
	/**
	 * F1:根据传入的查询条件（头条名查询头条）
	 * @param headLineCondition
	 * @return
	 */
	List<HeadLine> queryHeadLineList(@Param("headLineCondition")HeadLine headLineCondition,@Param("rowIndex")int rowIndex,@Param("pageSize") int pageSize);
	/**
	 * 获取某条件下的所有头条数量
	 * @param productCondition
	 * @return
	 */
	int queryHeadLineCount(@Param("headLineCondition")HeadLine headLineCondition);
	
	/**
	 * F2:通过id获取headline
	 * @param lineId
	 * @return
	 */
	HeadLine queryHeadLineById(long lineId);

	/**
	 * F3:获取headline列表
	 * @param lineIdList
	 * @return
	 */
	List<HeadLine> queryHeadLineByIds(List<Long> lineIdList);

	/**
	 * F4:插入headline
	 * @param headLine
	 * @return
	 */
	int insertHeadLine(HeadLine headLine);

	/**
	 * F5:修改headline
	 * @param headLine
	 * @return
	 */
	int updateHeadLine(HeadLine headLine);

	/**
	 * F6:删除headline
	 * @param headLineId
	 * @return
	 */
	int deleteHeadLine(long headLineId);

	/**
	 * F7:批量删除headlines
	 * @param lineIdList
	 * @return
	 */
	int batchDeleteHeadLine(List<Long> lineIdList);
}
