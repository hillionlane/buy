package com.my.buy.web.orderadmin;
 
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
 

@Controller
@RequestMapping(value = "/orderadmin",method=RequestMethod.GET)
public class OrderAdminController 
{
	//跳转至我卖出的订单页面
	@RequestMapping(value = "/mysold")
	public String goMySold() 
	{	
		return "local/mysold";	
	}
	//跳转到我买到的订单页面
	@RequestMapping(value = "/mybought")
	public String goMyOrder( ) 
	{	
		return "local/mybought";	
	}
	
	//跳转到评论详情页面
	@RequestMapping(value = "/showcommentdetail")
	public String showCommentDetail( ) 
	{	
		return "local/commentdetail";	
	}
	
	//跳转到我的评论页面
    @RequestMapping(value = "/mycomment")
	 public String showMyComment( ) 
	 {	
	 	return "local/mycomment";	
	 }
	
}
