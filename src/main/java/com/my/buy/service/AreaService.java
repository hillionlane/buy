package com.my.buy.service;

import java.util.List;

import com.my.buy.dto.AreaExecution;
import com.my.buy.entity.Area;

public interface AreaService 
{
	/**
	 * F1:查找区域列表
	 * @return
	 */
	List<Area> getAreaList();
	
	/**
	 * F2:通过areaId获取区域信息
	 * @param areaId
	 * @return
	 */
	Area getAreaById(long areaId);
	/**
	 * F3:添加区域
	 * @param area
	 * @return
	 */
	AreaExecution addArea(Area area);

	/**
	 * F4：修改区域信息
	 * @param area
	 * @return
	 */
	AreaExecution modifyArea(Area area);

	/**
	 * F5:删除区域
	 * @param areaId
	 * @return
	 */
	AreaExecution removeArea(long areaId);

	/**
	 * F6:批量删除区域信息
	 * @param areaIdList
	 * @return
	 */
	AreaExecution removeAreaList(List<Long> areaIdList);
	
}

