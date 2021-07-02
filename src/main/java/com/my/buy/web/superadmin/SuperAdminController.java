package com.my.buy.web.superadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/superadmin",method={RequestMethod.GET})
public class SuperAdminController 
{
 	@RequestMapping(value="/login")
 	public String login()
 	{
 		return "superadmin/login";
 	}
 	
 	@RequestMapping(value = "/top", method = RequestMethod.GET)
	private String top() {
		return "superadmin/top";
	}
 	
 	@RequestMapping(value = "/main", method = RequestMethod.GET)
	private String main() {
		return "superadmin/main";
	}
 	@RequestMapping(value = "/areamanage", method = RequestMethod.GET)
	private String areaManagement() {
		return "superadmin/areamanage";
	}
 	
 	@RequestMapping(value = "/headlinemanage", method = RequestMethod.GET)
	private String headlineManagement() {
		return "superadmin/headlinemanage";
	}
 	
 	@RequestMapping(value = "/productcategorymanage", method = RequestMethod.GET)
	private String productCategoryManagement() {
		return "superadmin/productcategorymanage";
	}
 	
 	@RequestMapping(value = "/productmanage", method = RequestMethod.GET)
	private String productManagement() {
		return "superadmin/productmanage";
	}
 	
 	@RequestMapping(value = "/usermanage", method = RequestMethod.GET)
	private String userManagement() {
		return "superadmin/usermanage";
	}
 	
 	@RequestMapping(value = "/ordermanage", method = RequestMethod.GET)
	private String orderManagement() {
		return "superadmin/ordermanage";
	}
 }
