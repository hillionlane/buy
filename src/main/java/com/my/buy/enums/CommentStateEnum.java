package com.my.buy.enums;

public enum CommentStateEnum 
{
	CHECK(0, "审核中"), OFFLINE(-1, "非法评论"), SUCCESS(1, "评论成功"), INNER_ERROR(-1001, "内部系统错误"), EMPTY(-1006, "评论内容为空");
	private int state;
	private String stateInfo;

	// 设置为私有，外部不可改变其值
	private CommentStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}
	public static CommentStateEnum stateOf(int state) {
		for (CommentStateEnum stateEnum : values()) {
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
