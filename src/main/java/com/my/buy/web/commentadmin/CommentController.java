package com.my.buy.web.commentadmin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.buy.dto.CommentExecution;
import com.my.buy.entity.Comment;
import com.my.buy.entity.PersonInfo;
import com.my.buy.enums.CollectStateEnum;
import com.my.buy.enums.CommentStateEnum;
import com.my.buy.exceptions.CollectOperationException;
import com.my.buy.exceptions.CommentOperationException;
import com.my.buy.service.CommentService;
import com.my.buy.util.HttpServletRequestUtil;

@Controller
@RequestMapping(value="/commentadmin")
public class CommentController 
{
	@Autowired 
	private CommentService commentService;
	
	
	/**
 	 * F1:添加留言
 	 * @param request
 	 * @return
 	 * @throws IOException
 	 */
 	@RequestMapping(value = "/addcomment",method=RequestMethod.GET)
 	@ResponseBody
 	private Map<String, Object> addComment(HttpServletRequest request) throws IOException 
 	{
 		Map<String, Object> modelMap = new HashMap<String, Object>();
 		long productId=HttpServletRequestUtil.getLong(request, "productId");
 		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user"); 
 		String content=HttpServletRequestUtil.getString(request, "content");
 		Comment comment=new Comment();
 		if(productId>0&&user!=null&&user.getUserId()>0)
 		{
 			try
 			{
 				comment.setUser(user);
 				comment.setProductId(productId);
 				comment.setContent(content);
 				CommentExecution ce=commentService.addComment(comment);
 				if(ce.getState()==CollectStateEnum.SUCCESS.getState())
 				{
 					modelMap.put("success", true);
 				}
 				else
 				{
 					modelMap.put("success", false);
 					modelMap.put("errMsg", ce.getStateInfo());
 				}
 			}
 			catch(CollectOperationException e)
 			{
 				modelMap.put("success", false);
 				modelMap.put("errMsg", e.getMessage());
 			}
 		}
 		else
 		{
 			modelMap.put("success", false);
 			modelMap.put("errMsg", "用户Id或商品Id为空！");
 		}
 		return modelMap;
 	}
 	/**
 	 * F2:根据评论时间获取评论列表
 	 * @param request
 	 * @return
 	 * @throws IOException
 	 */
 	@RequestMapping(value = "/getcommentlist",method=RequestMethod.GET)
 	@ResponseBody
 	private Map<String, Object> getCommentList(HttpServletRequest request) throws IOException 
 	{
 		Map<String, Object> modelMap = new HashMap<String, Object>();
 		long productId=HttpServletRequestUtil.getLong(request, "productId");
 		if(productId>0)
 		{
 			try
 			{
 				CommentExecution ce=commentService.getCommentList(productId);
 				if(ce.getState()==CommentStateEnum.SUCCESS.getState())
 				{
 					modelMap.put("success", true);
 					modelMap.put("commentList", ce.getCommentList());
 				}
 			}catch(CollectOperationException e)
 			{
 				modelMap.put("success", false);
 	 			modelMap.put("errMsg", e.getMessage());
 			}
 		}
 		else
 		{
 			modelMap.put("success", false);
	 		modelMap.put("errMsg", "商品Id为空！");
 		}
 		return modelMap;
 	}
 	/**
 	 * F3:根据commentId删除用户评论
 	 * @param request
 	 * @return
 	 * @throws IOException
 	 */
 	@RequestMapping(value = "/removecomment",method=RequestMethod.GET)
 	@ResponseBody
 	private Map<String, Object> removeComment(HttpServletRequest request) throws IOException 
 	{
 		Map<String, Object> modelMap = new HashMap<String, Object>();
 		long commentId=HttpServletRequestUtil.getLong(request, "commentId");
 		CommentExecution ce=null;
 		try
 		{
 			ce=commentService.removeCommentById(commentId);
 			if(ce.getState()==CommentStateEnum.SUCCESS.getState())
 			{
 				modelMap.put("success", true);
 			}
 			else
 			{
 				modelMap.put("success", false);
 				modelMap.put("errMsg","删除评论失败");
 			}
 		}
 		catch(CommentOperationException e)
 		{
 			modelMap.put("success", false);
 			modelMap.put("errMsg", e.getMessage());
 		}
 		return modelMap;
 	}
}
