$(function() {
	var userId=getQueryString('userId');
	var loading = false;
	// 分页允许返回的最大条数，超过此数则禁止访问后台
	var maxItems = 999;
	 // 一页返回的最大条数
	var pageSize = 3;
	 
	// 获取商品列表的URL
	var listUrl = '/buy/localadmin/getmyproductlist'; 
	//获取当前用户信息URL
	var hispageInfoUrl='/buy/personinfoadmin/getuserbyid?userId='+userId; 
	//获取商品信息并返回给前台
	var productInfoUrl='/buy/productadmin/getproductbyid';
	// 页码
	var pageNum = 1;    
	 
	// 预先加载10条店铺信息
	addItems(pageSize, pageNum);
	// 获取商品类别列表以及区域列表信息
	/**
	 * F1:获取当前用户信息
	 */
	$.getJSON(hispageInfoUrl, function(data) {
		if (data.success) 
		{
			var user = data.tempUser; 
			var html='<img src="'+user.profileImg+'" style="width: 50%;  padding-top: 10px;">';
			var myHtml='<img src="'+user.profileImg+'" style="width: 55px;  padding-top: 5px;height: 40px;">';
			$('#user-name').val(user.userName);
			/*$('#user-img').val('src',user.profileImg);*/
			$('#user-gender').val(user.userGender);
			$('#user-email').val(user.userEmail);
			$('#user-desc').text(user.userDesc);
			$('#user-addr').val(user.area.areaName+user.userAddr);
			$('#user-phone').val(user.userPhone);
			$('#user-age').val(user.userAge); 
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
	/**
	 * F2:获取分页展示的商品列表信息
	 * 
	 * @param pageSize
	 *            每页显示的数据
	 * @param pageIndex
	 *            页码
	 * @returns
	 */
	function addItems(pageSize, pageIndex) {
		//拼接处查询的URL，赋空值默认就去掉这个条件的限制，有值就代表这个条件去
		var url = listUrl + '?' + 'pageIndex=' + pageIndex + '&pageSize='
					+ pageSize+'&userId='+userId;
		//设定加载符，若还在后台取数据则不能再次访问后台，避免多次重复加载
		loading = true;
		//访问后台获取相应查询条件下的商品列表
		$.getJSON(url, function(data) {
			if (data.success) {
				//获取当前查询条件下的总数
				maxItems = data.count;
				var html = ''; 
				data.productList.map(function(item, index) 
						{
					 
					html += '' + '<div class="card" data-product-id="'
							+ item.productId + '">' + '<div class="card-header">'
							+ item.productName + '</div>'
							+ '<div class="card-content">'
							+ '<div class="list-block media-list">' + '<ul>'
							+ '<li class="item-content">'
							+ '<div class="item-media">' + '<img src="'
							+ item.productImg + '" width="44">' + '</div>'
							+ '<div class="item-inner"><div class="item-title-row">'
							+ '<div class="item-subtitle">' + item.productDesc
							+ '</div>' 
							+'<div class="item-after" style="color:red">'+"￥"+item.productPrice
							+ '</div></div>' + '</li>' + '</ul>'
							+ '</div>' + '</div>' + '<div class="card-footer">'
							+ '<p class="color-gray">'
							+ new Date(item.lastEditTime).Format("yyyy-MM-dd hh:mm:ss")
							+ '更新</p>' + '<span>'+goDetail(item.productId)+'</span>'
							+'</div>'
							+ '</div>';
				}); 
				//将卡片集合添加到目标HTML组件里
				$('.list-div').append(html);
				//获取目前为止已显示的卡片总数，包含之前已经加载的
				var total = $('.list-div .card').length;
				//若总数达到跟按照此前查询条件列出来的总数一致，则停止后台的加载
				if (total >= maxItems) { 
					//隐藏提示符
					$('.infinite-scroll-preloader').hide();
				}
				else
				{
					$('.infinite-scroll-preloader').show();
					//否则页码加1，继续load出新的店铺
					pageNum += 1;
					//加载结束，可以再次加载了
					loading = false;
					//刷新页面，显示新加载的店铺
					$.refreshScroller();
				}
			}
		});

	}
	//下滑屏幕自动进行分页搜索
	$(document).on('infinite', '.infinite-scroll-bottom', function() {
		if (loading)
			return;
		addItems(pageSize, pageNum);
	});
 
	
	/**
	*F3:点击“查看”按钮，实现页面跳转至商品详情页面
	*/
	function goDetail(id) {
		return '<a href="/buy/frontendadmin/productdetail?productId='+ id +'" external>查看详情</a>';
	}
	//侧边栏
	$('#me').click(function() {
		$.openPanel('#panel-left-demo');
	});

	$.init();
});