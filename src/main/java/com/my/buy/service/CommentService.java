package com.my.buy.service;

import com.my.buy.dto.CommentExecution;
import com.my.buy.entity.Comment;

public interface CommentService 
{
	/**
	 * F1:添加评论
	 * @param comment
	 * @return
	 */
	CommentExecution addComment(Comment comment);
	
	/**
	 * F2:获取该商品的评论列表
	 * @param productId
	 * @return
	 */
	CommentExecution getCommentList(long productId);
	
	/**
	 * F3:通过commentId删除评论
	 * @param commentId
	 * @return
	 */
	CommentExecution removeCommentById(long commentId);
}
