package com.my.buy.dto;

import java.util.List;

import com.my.buy.entity.Collect;
import com.my.buy.enums.CollectStateEnum;

public class CollectExecution {
	private int state;
	private String stateInfo;
	//增删收藏时使用
	private Collect collection;
	private List<Collect> collectionList;
	private int count;
	public CollectExecution()
	{
		
	}
	public CollectExecution(CollectStateEnum stateEnum)
	{
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
	}
	public CollectExecution(CollectStateEnum stateEnum,Collect collection)
	{
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
		this.collection=collection;
	}
	public CollectExecution(CollectStateEnum stateEnum,List<Collect> collectionList)
	{
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
		this.collectionList=collectionList;
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
	public Collect getCollection() {
		return collection;
	}
	public void setCollection(Collect collection) {
		this.collection = collection;
	}
	public List<Collect> getCollectionList() {
		return collectionList;
	}
	public void setCollectionList(List<Collect> collectionList) {
		this.collectionList = collectionList;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}
