package com.my.buy.service.impl;

import java.util.Date;
import java.util.List;

import javax.naming.CommunicationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.buy.dao.CommentDao;
import com.my.buy.dto.CommentExecution;
import com.my.buy.entity.Comment;
import com.my.buy.enums.CommentStateEnum;
import com.my.buy.exceptions.CommentOperationException;
import com.my.buy.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService
{

	@Autowired
	private CommentDao commentDao;
	/**
	 * F1:添加评论
	 */
	@Override
	@Transactional
	public CommentExecution addComment(Comment comment) {
		if(comment!=null&&comment.getUser()!=null&&comment.getUser().getUserId()>0&&comment.getProductId()>0)
		{
			try
			{
				comment.setCreateTime(new Date());
				comment.setStatus(1);
				int effctedNum=commentDao.insertComment(comment);
				if(effctedNum>0)
				{
					return new CommentExecution(CommentStateEnum.SUCCESS, comment);
				}
				else
				{
					throw new CommentOperationException("添加评论失败！");
				}
			}catch(Exception e)
			{
				throw new CommentOperationException("添加评论失败:"+e.getMessage());
			}
		} 
		else
		{
			return new CommentExecution(CommentStateEnum.EMPTY);
		}
	}
	/**
	 * F2:获取评论列表
	 */

	@Override
	@Transactional
	public CommentExecution getCommentList(long productId) 
	{
		try
		{
			if(productId>0)
			{
				List<Comment> commentList=commentDao.queryCommentList(productId);
//				if(commentList.size()>0&&commentList!=null)
//				{
					return new CommentExecution(CommentStateEnum.SUCCESS, commentList);
//				}
//				else
//				{
//					throw new CommentOperationException("评论失败！");
//				}
			}
			else
			{
				return  new CommentExecution(CommentStateEnum.EMPTY);
			}
		}catch(Exception e)
		{
			throw new CommentOperationException("获取评论列表失败:"+e.getMessage());
		} 
	}
	/**
	 * F3:通过commentId删除comment
	 */
	@Override
	@Transactional
	public CommentExecution removeCommentById(long commentId) 
	{
		 Comment comment=new Comment();
		 CommentExecution ce=new CommentExecution();
		 try
		 {
			 if(commentId>0)
			 {
				 comment=commentDao.queryCommentById(commentId);
				 if(comment!=null)
				 {
					 int effectedNum=commentDao.deleteComment(commentId);
					 if(effectedNum>0)
					 {
						 ce.setComment(comment);
						 ce.setState(CommentStateEnum.SUCCESS.getState());
						 ce.setStateInfo(CommentStateEnum.SUCCESS.getStateInfo());
					 }
					 else
					 {
						 throw new CommentOperationException("该commnetId的评论不存在");
					 }
				 }
				 else
				 {
					 ce.setState(CommentStateEnum.EMPTY.getState());
					 ce.setStateInfo(CommentStateEnum.EMPTY.getStateInfo());
				 }
			 }
		 }
		 catch(Exception e)
		 {
			 throw new CommentOperationException("删除评论失败:"+e.getMessage());
		 }
		return ce;
	}

}
