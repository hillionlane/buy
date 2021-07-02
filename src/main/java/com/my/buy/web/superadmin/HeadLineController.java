package com.my.buy.web.superadmin;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.buy.dto.HeadLineExecution;
import com.my.buy.dto.ImageHolder;
import com.my.buy.entity.ConstantForSuperAdmin;
import com.my.buy.entity.HeadLine;
import com.my.buy.enums.HeadLineStateEnum;
import com.my.buy.service.HeadLineService;
import com.my.buy.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/superadmin")
public class HeadLineController {
	@Autowired
	private HeadLineService headLineService;

	/**
	 * F1:根据查询条件获取头条列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listheadlines", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> listHeadLines(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		int pageIndex = HttpServletRequestUtil.getInt(request,
				ConstantForSuperAdmin.PAGE_NO);
		int pageSize = HttpServletRequestUtil.getInt(request,
				ConstantForSuperAdmin.PAGE_SIZE);  
		HeadLineExecution he=null;
		if(pageIndex > 0 && pageSize > 0)
		{
			try {
				Integer enableStatus = HttpServletRequestUtil.getInt(request, "enableStatus");
				HeadLine headLine = new HeadLine();
				if (enableStatus > -1) {
					headLine.setEnableStatus(enableStatus);
				}
				he = headLineService.getHeadLineList(headLine,pageIndex, pageSize);
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, he.getHeadLineList());
				modelMap.put(ConstantForSuperAdmin.TOTAL, he.getCount());
			} catch (Exception e) {
				e.printStackTrace();
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
		}
		else
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", "空的查询信息");
		}
		
		return modelMap;
	}

	/**
	 * F2:增加Headline
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/addheadline", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addHeadLine(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		HeadLine headLine = null;
		String headLineStr = HttpServletRequestUtil.getString(request, "headLineStr");
		ImageHolder thumbnail = null;
		MultipartHttpServletRequest multipartHttpServletRequest = null;
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		try {
			headLine = mapper.readValue(headLineStr, HeadLine.class);

		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		if (headLine != null&&multipartResolver.isMultipart(request)) 
		{ 
				try {
					// 转换成多部分request
					multipartHttpServletRequest = (MultipartHttpServletRequest) request;
					// 取出缩略图并构建ImageHolder对象
					CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("headTitleManagementAdd_lineImg");
					thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
					// decode可能有中文的地方
					headLine.setLineName((headLine.getLineName() == null) ? null
							: URLDecoder.decode(headLine.getLineName(), "UTF-8"));
					HeadLineExecution ae = headLineService.addHeadLine(headLine, thumbnail);
					if (ae.getState() == HeadLineStateEnum.SUCCESS.getState()) {
						modelMap.put("success", true);
					} else {
						modelMap.put("success", false);
						modelMap.put("errMsg", ae.getStateInfo());
					}
				} catch (Exception e) {
					modelMap.put("success", false);
					modelMap.put("errMsg", e.toString());
					return modelMap;
				}
		} 
		else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入头条信息");
		}
		return modelMap;
	}
	
	/**
	 * F3:修改headline
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/modifyheadline", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyHeadLine(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		HeadLine headLine = null;
		ImageHolder thumbnail = null;
		String headLineStr = HttpServletRequestUtil.getString(request,
				"headLineStr");
		MultipartHttpServletRequest multipartHttpServletRequest = null;
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		try {
			headLine = mapper.readValue(headLineStr, HeadLine.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		if (headLine != null && headLine.getLineId() != null) {
			try {
					// decode可能有中文的地方
				  headLine.setLineName((headLine.getLineName() == null) ? null
						: URLDecoder.decode(headLine.getLineName(), "UTF-8"));
					// 转换成多部分request
				 	multipartHttpServletRequest = (MultipartHttpServletRequest) request;
					// 取出缩略图并构建ImageHolder对象
					CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartHttpServletRequest
							.getFile("headTitleManagementEdit_lineImg");
					if(thumbnailFile!=null)
					{
						thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
					}
				HeadLineExecution ae = headLineService.modifyHeadLine(headLine,
						thumbnail);
				if (ae.getState() == HeadLineStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", ae.getStateInfo());
				}
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入头条信息");
		}
		return modelMap;
	}
	
	/**
	 * F5:删除headline
	 * @param headLineId
	 * @return
	 */
	@RequestMapping(value = "/removeheadline", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> removeHeadLine(Long headLineId) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (headLineId != null && headLineId > 0) {
			try {
				HeadLineExecution ae = headLineService
						.removeHeadLine(headLineId);
				if (ae.getState() == HeadLineStateEnum.SUCCESS.getState()) {
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
			modelMap.put("errMsg", "请输入头条信息");
		}
		return modelMap;
	}
	
	/**
	 * F6:批量删除headlines
	 * @param headLineIdListStr
	 * @return
	 */
	@RequestMapping(value = "/removeheadlines", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> removeHeadLines(String headLineIdListStr) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		//把json解析成list
		JavaType javaType = mapper.getTypeFactory().constructParametricType(
				ArrayList.class, Long.class);
		List<Long> headLineIdList = null;
		try {
			headLineIdList = mapper.readValue(headLineIdListStr, javaType);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		if (headLineIdList != null && headLineIdList.size() > 0) {
			try {
				HeadLineExecution ae = headLineService
						.removeHeadLineList(headLineIdList);
				if (ae.getState() == HeadLineStateEnum.SUCCESS.getState()) {
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
