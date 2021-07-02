package com.my.buy.dto;

import java.util.List;

import com.my.buy.entity.PersonInfo;
import com.my.buy.enums.PersonInfoStateEnum;

public class PersonInfoExecution {
	private int state;
	private String stateInfo;
	//操作增删改用户信息时使用
	private PersonInfo user;
	//查找全部用户时使用
	private int count;
	private List<PersonInfo> userList;
	public PersonInfoExecution()
	{
		
	}
	public PersonInfoExecution(PersonInfoStateEnum stateEnum)
	{
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
	}
	public PersonInfoExecution(PersonInfoStateEnum stateEnum,PersonInfo user)
	{
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
		this.user=user;
	}
	public PersonInfoExecution(PersonInfoStateEnum stateEnum,List<PersonInfo> userList,int count)
	{
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
		this.count=count;
		this.userList=userList;
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
	public PersonInfo getUser() {
		return user;
	}
	public void setUser(PersonInfo user) {
		this.user = user;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<PersonInfo> getUserList() {
		return userList;
	}
	public void setUserList(List<PersonInfo> userList) {
		this.userList = userList;
	}
	
}
