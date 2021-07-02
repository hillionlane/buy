package com.my.buy.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.my.buy.dto.CollectExecution;
import com.my.buy.entity.Collect;

public interface CollectService {
	
	/**
	 * F1:增加收藏
	 * @param collection
	 * @return
	 */
	CollectExecution addCollection(Collect collection);
	
	/**
	 * F2:查询是否已被收藏
	 * @param userId
	 * @param productId
	 * @return
	 */
	Collect getCollection(@Param("userId")long userId,@Param("productId")long productId);
	
	/**
	 * F3:移除收藏
	 * @param userId
	 * @param productId
	 * @return
	 */
	CollectExecution removeCollection(@Param("userId")long userId,@Param("productId")long productId);
	
	/**
	 * F4:获取某用户收藏列表
	 * @param userId
	 * @return
	 */
	 CollectExecution getCollectListByUserId(@Param("userId")long userId,@Param("pageIndex")int pageIndex,@Param("pageSize") int pageSize);
}
