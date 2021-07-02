package com.my.buy.dao;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.my.buy.BaseTest;
import com.my.buy.entity.Comment;
import com.my.buy.entity.PersonInfo;

public class CommentDaoTest extends BaseTest
{
	@Autowired
	private CommentDao commentDao;
	
	@Test 
	@Ignore
	public void testInsertComment()
	{
		PersonInfo user=new PersonInfo();
		user.setUserId(1L);
		Comment comment=new Comment();
		comment.setContent("test");
		comment.setCreateTime(new Date());
		comment.setUser(user);
		comment.setProductId(1L);
		comment.setStatus(1);
		int effectdNum=commentDao.insertComment(comment);
		assertEquals(1,effectdNum);
	}
	@Test 
	@Ignore
	public void testQueryCommentList()
	{
		List<Comment> commentList=commentDao.queryCommentList(1L);
		System.out.println(commentList.get(0).getUser().getProfileImg());
	}
	
	@Test
	@Ignore
	public void testDeleteCommentById()
	{
		int effectedNum=commentDao.deleteComment(20L);
		assertEquals(1,effectedNum);
	}
	
	@Test
	public void testQueryCommentById()
	{
		Comment comment=new Comment();
		comment=commentDao.queryCommentById(1);
		System.out.println(comment.getUser().getUserName());
	}
	
}
