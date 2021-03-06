package com.my.buy.web.superadmin;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import com.fasterxml.jackson.databind.JavaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.buy.dto.AreaExecution;
import com.my.buy.entity.Area;
import com.my.buy.entity.ConstantForSuperAdmin;
import com.my.buy.enums.AreaStateEnum;
import com.my.buy.service.AreaService;
import com.my.buy.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/superadmin")
public class AreaController 
{
	@Autowired
	private AreaService areaService;
	
	/**
	 * F1:查找区域列表
	 * @return
	 */
	@RequestMapping(value = "/listareas", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> listAreas() {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<Area> list = new ArrayList<Area>();
		try {
			list = areaService.getAreaList();
			modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, list);
			modelMap.put(ConstantForSuperAdmin.TOTAL, list.size());

		} catch (Exception e) {
			e.printStackTrace();
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		return modelMap;
	}
	
	/**
	 * F2:添加区域信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/addarea", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addArea(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		String areaStr=HttpServletRequestUtil.getString(request, "areaStr");
		Area area = null;
		try {
			area = mapper.readValue(areaStr, Area.class);
			// decode可能有中文的地方
			area.setAreaName((area.getAreaName() == null) ? null : URLDecoder
					.decode(area.getAreaName(), "UTF-8"));
			area.setAreaDesc((area.getAreaDesc() == null) ? null : (URLDecoder
					.decode(area.getAreaDesc(), "UTF-8")));
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		if (area != null && area.getAreaName() != null) {
			try {
				AreaExecution ae = areaService.addArea(area);
				if (ae.getState() == AreaStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", ae.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入区域信息");
		}
		return modelMap;
	}
	
	/**
	 * F3:修改区域信息
	 * @param areaStr
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/modifyarea", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyArea(String areaStr,
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		Area area = null;
		try {
			area = mapper.readValue(areaStr, Area.class);
			area.setAreaName((area.getAreaName() == null) ? null : URLDecoder
					.decode(area.getAreaName(), "UTF-8"));
			area.setAreaDesc((area.getAreaDesc() == null) ? null : URLDecoder
					.decode(area.getAreaDesc(), "UTF-8"));
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		if (area != null && area.getAreaId()>0) {
			try {
				AreaExecution ae = areaService.modifyArea(area);
				if (ae.getState() == AreaStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", ae.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入区域信息");
		}
		return modelMap;
	}
	
	/**
	 * F4:删除区域信息
	 * @param areaId
	 * @return
	 */
	@RequestMapping(value = "/removearea", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> removeArea(Long areaId) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (areaId != null && areaId > 0) {
			try {
				AreaExecution ae = areaService.removeArea(areaId);
				if (ae.getState() == AreaStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", ae.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入区域信息");
		}
		return modelMap;
	}
	
	/**
	 * F5:批量删除区域信息
	 * @param areaIdListStr
	 * @return
	 */
	@RequestMapping(value = "/removeareas", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> removeAreas(String areaIdListStr) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		JavaType javaType = mapper.getTypeFactory().constructParametricType(
				ArrayList.class, Long.class);
		List<Long> areaIdList = null;
		try {
			areaIdList = mapper.readValue(areaIdListStr, javaType);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		if (areaIdList != null && areaIdList.size() > 0) {
			try {
				AreaExecution ae = areaService.removeAreaList(areaIdList);
				if (ae.getState() == AreaStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", ae.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入区域信息");
		}
		return modelMap;
	}

}
