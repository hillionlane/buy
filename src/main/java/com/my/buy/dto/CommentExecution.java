package com.my.buy.dto;

import java.util.List;

import com.my.buy.entity.Collect;
import com.my.buy.entity.Comment;
import com.my.buy.enums.CollectStateEnum;
import com.my.buy.enums.CommentStateEnum;

public class CommentExecution 
{
	private int state;
	private String stateInfo;
	//增删收藏时使用
	private Comment comment;
	private List<Comment> commentList;
	public CommentExecution()
	{
		
	}
	public CommentExecution(CommentStateEnum stateEnum)
	{
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
	}
	public CommentExecution(CommentStateEnum stateEnum,Comment comment)
	{
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
		this.comment=comment;
	}
	public CommentExecution(CommentStateEnum stateEnum,List<Comment> commentList)
	{
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
		this.commentList=commentList;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getStateInfo() {
		return stateInfo;
	}
	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}
	public Comment getComment() {
		return comment;
	}
	public void setComment(Comment comment) {
		this.comment = comment;
	}
	public List<Comment> getCommentList() {
		return commentList;
	}
	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}
}
