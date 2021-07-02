$(function() {
	var loading = false;
	// 分页允许返回的最大条数，超过此数则禁止访问后台
	var maxItems = 999;

	// 一页返回的最大条数
	var pageSize = 3;
	// 获取商品列表的URL
	var listUrl = '/buy/productadmin/getproductlist';
	// 获取商品类别列表以及区域列表的Url
	var searchDivUrl = '/buy/frontendadmin/listproductpageinfo';
	// 页码
	var pageNum = 1;
	// 从地址栏尝试获取parent s category id.
	var parentId = getQueryString('parentId');
	var areaId = '';
	var productCategoryId = '';
	var productName = '';
	//判断是点击“全部商品”(all)进入还是“row”()进入
	var flag=getQueryString('flag');
	// 渲染出商品类别列表以及区域列表以供搜索
	getSearchDivData();
	// 预先加载10条商品信息
	addItems(pageSize, pageNum);
	// 获取商品类别列表以及区域列表信息
	/**
	 * F1:获取商品类别列表以及区域列表信息
	 */
	function getSearchDivData() {
		// 若果传入了parentId,则取出此一级类别下面的所有二级类别
		var url = searchDivUrl + '?' + 'parentId=' + parentId;
		$.getJSON(
						url,
						function(data) {
							if (data.success) {
								var productCategoryList = data.productCategoryList;
								var html = '';
								html += '<a href="#" class="button" data-category-id="">全部类别</a>';
								// 遍历店铺列表，拼接出a标签集
								productCategoryList
										.map(function(item, index) {
											html += '<a href="#" class="button" data-category-id='
													+ item.productCategoryId
													+ '>'
													+ item.productCategoryName
													+ '</a>';
										});
								// 将拼好的类别标签嵌入前台的html组件中
								$('#productlist-search-div').html(html);
								var selectOptions = '<option value="">全部校区</option>';
								// 获取后台返回过来的区域信息列表
								var areaList = data.areaList;
								// 遍历区域信息列表，拼接处option标签集
								areaList.map(function(item, index) {
									selectOptions += '<option value="' 
											+ item.areaId + '">'
											+ item.areaName + '</option>';
								});
								// 将标签集添加进area列表里
								$('#area-search').html(selectOptions);
							}
						});
	}
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
			+ pageSize + '&parentId=' + parentId + '&areaId=' + areaId
			+ '&productCategoryId=' + productCategoryId + '&productName=' + productName;
		//设定加载符，若还在后台取数据则不能再次访问后台，避免多次重复加载
		loading = true;
		//访问后台获取相应查询条件下的商品列表
		$.getJSON(url, function(data) {
			if (data.success) {
				//获取当前查询条件下的总数
				maxItems = data.count;
				var html = '';
				data.productList.map(function(item, index) {
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
							+ '</div>' + '</li>' + '</ul>'
							+ '</div>' + '</div>' + '<div class="card-footer">'
							+ '<p class="color-gray">'
							+ new Date(item.lastEditTime).Format("yyyy-MM-dd")
							+ '更新</p>' + '<span>'+goDetail(item.productId)+'</span>'
							+ '</div>'
							+ '</div>';
				});
				//将卡片集合添加到目标HTML组件里
				$('.list-div').append(html);
				//获取目前为止已显示的卡片总数，包含之前已经加载的
				var total = $('.list-div .card').length;
				//若总数达到跟按照此前查询条件列出来的总数一致，则停止后台的加载
				if (total >= maxItems) {
//					// 加载完毕，则注销无限加载事件，以防不必要的加载
//					$.detachInfiniteScroll($('.infinite-scroll'));
//					// 删除加载提示符
//					$('.infinite-scroll-preloader').remove();
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
				/*//否则页码加1，继续load出新的店铺
				pageNum += 1;
				//加载结束，可以再次加载了
				loading = false;
				//刷新页面，显示新加载的店铺
				$.refreshScroller();*/
			}
		});

	}
	//下滑屏幕自动进行分页搜索，默认据底部50像素时加载
	$(document).on('infinite', '.infinite-scroll-bottom', function() {
		if (loading)
			{
			/*if(flag=='all')
			{
				parentId = ''; 
			}*/
				return;
			}
		addItems(pageSize, pageNum);
	});
 
	function goDetail(id) {
		return '<a href="/buy/frontendadmin/productdetail?productId='+ id +'" external>查看详情</a>';
	}
	
	//选择新的商品类别之后，重置页码，清空原先的店铺列表，按照新的类别去查询
	$('#productlist-search-div').on(
			'click',
			'.button',
			function(e) { 
				/*//若点击全部商品按钮进入，即parentId为空的情况下，当列表未加载完毕（loading=false），则应将parentId置为空
				if(!loading)
				{
					parentId='';
				}*/
				//根据flag是否为空来判断是点击“全部商品”进入的还是点击“row”进入的
				if(flag!='all')
				{
					// 如果传递过来的是一个父类下的子类
					productCategoryId = e.target.dataset.categoryId;
					//若之前已选定了别的category，则移除其选定效果，改成选定新的 
					if ($(e.target).hasClass('button-fill')) {
						$(e.target).removeClass('button-fill');
						productCategoryId = '';
					} else {
						$(e.target).addClass('button-fill').siblings()
								.removeClass('button-fill');
					}
					//由于查询条件改变，清空店铺列表再进行查询
					$('.list-div').empty();
					//页码重置
					pageNum = 1;
					addItems(pageSize, pageNum);
				}
				else
				{
					//获取大类下所有商品的信息
					parentId = e.target.dataset.categoryId;
					if ($(e.target).hasClass('button-fill')) {
						$(e.target).removeClass('button-fill');
						parentId = '';
					} else {
						$(e.target).addClass('button-fill').siblings()
								.removeClass('button-fill');
					}
					$('.list-div').empty();
					pageNum = 1;
					addItems(pageSize, pageNum);
//					//若全部加载完毕则将parentId置为空
//					if(loading)
//					{
//						parentId = '';
//					}
				}
				/*if (parentId) {
					// 如果传递过来的是一个父类下的子类
					productCategoryId = e.target.dataset.categoryId;
					//若之前已选定了别的category，则移除其选定效果，改成选定新的 
					if ($(e.target).hasClass('button-fill')) {
						$(e.target).removeClass('button-fill');
						productCategoryId = '';
					} else {
						$(e.target).addClass('button-fill').siblings()
								.removeClass('button-fill');
					}
					//由于查询条件改变，清空店铺列表再进行查询
					$('.list-div').empty();
					//页码重置
					pageNum = 1;
					addItems(pageSize, pageNum);
				} else {// 如果传递过来的父类为空，则按照父类查询
					//获取大类下所有商品的信息
					parentId = e.target.dataset.categoryId;
					if ($(e.target).hasClass('button-fill')) {
						$(e.target).removeClass('button-fill');
						parentId = '';
					} else {
						$(e.target).addClass('button-fill').siblings()
								.removeClass('button-fill');
					}
					$('.list-div').empty();
					pageNum = 1;
					addItems(pageSize, pageNum);
					//若全部加载完毕则将parentId置为空
					if(loading)
					{
						parentId = '';
					}
				}*/
			});
	//店铺名字查询框
	//input反应太快了 一旦发生改变
	$('#search').on('change', function(e) {
		productName = e.target.value;
		$('.list-div').empty();
		pageNum = 1;
		addItems(pageSize, pageNum);
	});
	//区域查询
	$('#area-search').on('change', function() {
		areaId = $('#area-search').val();
		$('.list-div').empty();
		pageNum = 1;
		addItems(pageSize, pageNum);
	});

	//侧边栏
	$('#me').click(function() {
		$.openPanel('#panel-left-demo');
	});

	$.init();
});