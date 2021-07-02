$(function(){
	//定义访问后台，获取头条列表以及一级类别列表的URL
	var url='/buy/frontendadmin/getmainpageinfo';
	//获取当前用户信息URL
	var personInfoUrl='/buy/personinfoadmin/getuserbyid';
	/**
	 * F1:获取当前用户信息
	 */
	$.getJSON(personInfoUrl, function(data) {
		if (data.success) 
		{
			var user=data.user;
			var html='<img src="'+user.profileImg+'" style="width: 50%;padding-top: 10px;height:50%;">';
			var myHtml='<img src="'+user.profileImg+'" style="width: 55px;  padding-top: 5px;height: 40px;">';
			$('#user-name').text(user.userName);
			/*$('#user-img').val('src',user.profileImg);*/
			$('#user-gender').text(user.userGender);
			$('#user-email').text(user.userEmail);
			$('#user-desc').text(user.userDesc);
			$('#user-addr').text(user.area.areaName+user.userAddr);
			$('#user-phone').text(user.userPhone);
			$('#user-age').text(user.userAge); 
			$('#profile-img').append(html);
			$('#user-name').attr('disabled','disabled');
			$('#user-email').attr('disabled','disabled');
			$('#user-gender').attr('disabled','disabled');
			$('#user-desc').attr('disabled','disabled');
			$('#user-addr').attr('disabled','disabled');
			$('#user-phone').attr('disabled','disabled');
			$('#user-age').attr('disabled','disabled');
			//给右上角加上头像
			$('#my-profile').append(myHtml);
		}
	});
	$.getJSON(url,function(data){
		if(data.success)
		{
			//获取后端传来的头条列表
			var headLineList=data.headLineList;
			 
			var swiperHtml='';
			//遍历头条列表，并拼接处轮播图
			headLineList.map(function(item,index){
				swiperHtml+='<div class="swiper-slide img-wrap">'
					+'<a href="'+item.lineLink
					+'"external><img class="banner-img" src="'
					+item.lineImg
					+'  "alt="'
					+item.lineName
					+'" ></a>'
					+'</div>';
			});
			//将轮播图组赋值给千幻HTML控件
			$('.swiper-wrapper').html(swiperHtml);
			//设置轮播时间为3s
			$('.swiper-container').swiper({
				autoplay:3000,
				//用户对轮播图进行操作时，是否自动停止autoplay (true表示鼠标移上去停止轮播)
				autopalyDisableOnInteraction:true
			}); 
			//获取后台传递过来的一级类别列表
			var productCategoryList=data.productCategoryList;
			var categoryHtml='';
			//遍历大类列表，拼接成文字-图的形式
			 productCategoryList.map(function(item,index){
				categoryHtml+='<div class="col-50 product-classify" data-category='
					+item.productCategoryId+'>'+'<div class="word">'
					+'<p class="product-title">'+item.productCategoryName
					+'</p>'+'<p class="product-desc">'
					+item.productCategoryDesc+'</p>'+'</div>'
					+'<div class="product-classify-img-wrap">'
					+'<img class="product-img" src="'+item.productCategoryImg
					+'" >'
					+'</div></div>';
			});
			$('.row').html(categoryHtml);
			 
		}
	});
	//若点击“我的”，则显示侧栏
	$('#me').click(function(){
		$.openPanel('#panel-left-demo');
	});
	
	$('.row').on('click','.product-classify',function(e){
		var productCategoryId=e.currentTarget.dataset.category;
		var newUrl='/buy/frontendadmin/productlist?parentId='+productCategoryId;
		//重定向newUrl
		window.location.href=newUrl;
	});
 
	
});