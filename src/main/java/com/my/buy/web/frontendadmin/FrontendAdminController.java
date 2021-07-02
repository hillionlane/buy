package com.my.buy.web.frontendadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/frontendadmin", method = { RequestMethod.GET })
public class FrontendAdminController {
	

	// 返回主页面
	@RequestMapping(value = "/mainpage")
	public String showMainpage() {
		return "frontend/mainpage";
	}

	// 返回商品列表页面
	@RequestMapping(value = "/productlist")
	public String showProductList() {
		return "frontend/productlist";
	}

	// 返回到商品添加页面
	@RequestMapping(value = "/productoperation")
	public String addProduct() {
		return "frontend/productoperation";
	}

	// 跳转到商品详情页
	@RequestMapping(value = "/productdetail")
	public String showProductDetail() {
		return "frontend/productdetail";
	}

}
