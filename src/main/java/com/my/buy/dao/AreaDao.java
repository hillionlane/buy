package com.my.buy.dao;

import java.util.List;

import com.my.buy.entity.Area;

public interface AreaDao 
{
	/**
	 * 列出区域列表
	 * @return
	 */
	List<Area> queryArea();
	
	/**
	 * 根据区域Id获取区域信息
	 * @return
	 */
	Area queryAreaById(long areaId);
	
	/**
	 * 插入区域信息
	 * @param area
	 * @return
	 */
	int insertArea(Area area);

	/**
	 * 修改区域信息
	 * @param area
	 * @return
	 */
	int updateArea(Area area);

	/**
	 * 删除区域信息
	 * @param areaId
	 * @return
	 */
	int deleteArea(long areaId);

	/**
	 * 批量删除区域信息
	 * @param areaIdList
	 * @return
	 */
	int batchDeleteArea(List<Long> areaIdList);
}
