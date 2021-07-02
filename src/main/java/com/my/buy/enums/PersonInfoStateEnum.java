package com.my.buy.enums;

public enum PersonInfoStateEnum 
{
	CHECK(0,"审核中"),OFFLINE(-1,"非法用户"),SUCCESS(1,"操作成功"),
	 INNER_ERROR(-1001,"内部系统错误"),NULL_USERINFO(-1002,"用户信息为空"),
	 NULL_USERID(-1003,"用户ID为空"),EMPTY(-1004,"输入信息为空"),PASSWORD_EQUALS(-1005,"两次密码一致");
	private int state;
	private String stateInfo;
	public int getState() {
		return state;
	}
	 
	public String getStateInfo() {
		return stateInfo;
	}
	 
	private PersonInfoStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}
	/**
	 * 根据传入的state返回相应的enum的值
	 */
	public static PersonInfoStateEnum stateOf(int state) {
		for (PersonInfoStateEnum stateEnum : values()) {
			if (stateEnum.getState() == state) {
				return stateEnum;
			}
		}
		return null;
	}
}
