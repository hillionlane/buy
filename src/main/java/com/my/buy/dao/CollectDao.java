package com.my.buy.dao;
 

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.my.buy.entity.Collect;

public interface CollectDao 
{
	/**
	 * 增加一行收藏数据
	 *  
	 * @return
	 */
	int insertCollection(Collect collection);
	
	/**
	 * 查询收藏记录
	 * @param userId
	 * @param productId
	 * @return
	 */
	Collect selectCollection(@Param("userId")long userId,@Param("productId")long productId);
	
	/**
	 * 删除收藏记录
	 * @param userId
	 * @param productId
	 * @return
	 */
	int deleteCollection(@Param("userId")long userId,@Param("productId")long productId);
	
	/**
	 * 分页查询我的收藏（从而获取productId的List）
	 * @param userId 
	 * @return
	 */
	List<Collect> queryCollectListByUserId(@Param("userId")long userId,@Param("rowIndex")int rowIndex,@Param("pageSize") int pageSize);
	
	/**
	 * 获取列表总数
	 * @param userId
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	int queryCollectionCount(@Param("userId")long userId,@Param("rowIndex")int rowIndex,@Param("pageSize") int pageSize);
}
