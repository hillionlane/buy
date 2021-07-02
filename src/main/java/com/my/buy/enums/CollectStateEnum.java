package com.my.buy.enums;

public enum CollectStateEnum {
	CHECK(0, "审核中"), OFFLINE(-1, "非法收藏"), SUCCESS(1, "收藏成功"), INNER_ERROR(-1001, "内部系统错误"), EMPTY(-1006, "输入信息为空");
	private int state;
	private String stateInfo;

	// 设置为私有，外部不可改变其值
	private CollectStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}
	public static CollectStateEnum stateOf(int state) {
		for (CollectStateEnum stateEnum : values()) {
			if (stateEnum.getState() == state) {
				return stateEnum;
			}
		}
		return null;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}
}
