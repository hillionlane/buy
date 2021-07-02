package com.my.buy.entity;

import java.util.Date;
import java.util.List;

public class PersonInfo 
{
	 private Long userId;
	    private String userName;
	    //头像
	    private String profileImg; 
	    private String userEmail;
	    private String userGender;
	    //是否有资格登入(禁用)0禁用，1有资格
	    private Integer enableStatus;
	    //1、顾客 2、超级管理员
	    private Integer userType;
	    private Date createTime;
	    private Date lastEditTime;
	    private Area area;
	    private String userPhone;
	    private String userAddr;
	    private String userDesc;
	    private String password;
	    private long userAge; 
	    //用户的评论列表
	    public Long getUserId() {
	        return userId;
	    }

	    public void setUserId(Long userId) {
	        this.userId = userId;
	    }

	     
	    public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}
 

		public String getProfileImg() {
	        return profileImg;
	    }

	    public void setProfileImg(String profileImg) {
	        this.profileImg = profileImg;
	    }

	    public String getUserEmail() {
	        return userEmail;
	    }

	    public void setUserEmail(String userEmail) {
	        this.userEmail = userEmail;
	    }

	    public String getUserGender() {
	        return userGender;
	    }

	    public void setUserGender(String userGender) {
	        this.userGender = userGender;
	    }

	    public Integer getEnableStatus() {
	        return enableStatus;
	    }

	    public void setEnableStatus(Integer enableStatus) {
	        this.enableStatus = enableStatus;
	    }

	    public Integer getUserType() {
	        return userType;
	    }

	    public void setUserType(Integer userType) {
	        this.userType = userType;
	    }

	    public Date getCreateTime() {
	        return createTime;
	    }

	    public void setCreateTime(Date createTime) {
	        this.createTime = createTime;
	    }

	    public Date getLastEditTime() {
	        return lastEditTime;
	    }

	    public void setLastEditTime(Date lastEditTime) {
	        this.lastEditTime = lastEditTime;
	    } 
		public Area getArea() {
			return area;
		}

		public void setArea(Area area) {
			this.area = area;
		}

		public String getUserPhone() {
			return userPhone;
		}

		public void setUserPhone(String userPhone) {
			this.userPhone = userPhone;
		}

		public String getUserAddr() {
			return userAddr;
		}

		public void setUserAddr(String userAddr) {
			this.userAddr = userAddr;
		}

		public String getUserDesc() {
			return userDesc;
		}

		public void setUserDesc(String userDesc) {
			this.userDesc = userDesc;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public long getUserAge() {
			return userAge;
		}

		public void setUserAge(long userAge) {
			this.userAge = userAge;
		}
	    
}
