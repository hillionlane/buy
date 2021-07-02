package com.my.buy.entity;

import java.util.Date;

public class Area 
{
	 //均用引用类型，因为基本类型当不赋初值时会有默认值
    //ID
    private long areaId;
    //名稱
    private String areaName;
    //描述
    private String areaDesc;
    //权重
    private Integer priority;
    //创建时间
    private Date createTime;
    //更新时间
    private Date lastEditTime;

    public long getAreaId() {
        return areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public Integer getPriority() {
        return priority;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getLastEditTime() {
        return lastEditTime;
    }

    public void setAreaId(long areaId2) {
        this.areaId= areaId2;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setLastEditTime(Date lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

	public String getAreaDesc() {
		return areaDesc;
	}

	public void setAreaDesc(String areaDesc) {
		this.areaDesc = areaDesc;
	}
    
}
