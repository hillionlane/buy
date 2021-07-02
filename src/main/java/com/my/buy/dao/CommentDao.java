package com.my.buy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.my.buy.entity.Comment;

public interface CommentDao 
{
	/**
	 * F1:添加评论
	 */
	int insertComment(Comment comment);
	
	/**
	 * F2:根据商品Id获取评论列表
	 * @param productId
	 * @return
	 */
	List<Comment> queryCommentList(long productId);
	
	/**
	 * F3:删除评论 
	 * @return
	 */
 	int deleteComment(long commentId);
 	
 	/**
 	 * F4:通过commentId获取comment
 	 * @param commentId
 	 * @return
 	 */
 	Comment queryCommentById(long commentId);
}
