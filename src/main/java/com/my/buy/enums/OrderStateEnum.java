package com.my.buy.enums;

public enum OrderStateEnum {
	CHECK(0, "审核中"), OFFLINE(-1, "非法订单"), SUCCESS(1, "操作成功"), INNER_ERROR(-1001, "内部系统错误"), EMPTY(-1006, "信息为空");
	private int state;
	private String stateInfo;

	// 设置为私有，外部不可改变其值
	private OrderStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}
	public static OrderStateEnum stateOf(int state) {
		for (OrderStateEnum stateEnum : values()) {
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
