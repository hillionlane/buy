$(function() {
	var loading = false;
	// 分页允许返回的最大条数，超过此数则禁止访问后台
	var maxItems = 999; 
	// 一页返回的最大条数
	var pageSize = 3;
	// 获取订单列表的URL
	var listUrl = '/buy/orderadmin/getorderlist';
	//删除订单
	var removeOrderUrl='/buy/orderadmin/removeorderbyid';
	//修改订单 
	var modifyOrderUrl='/buy/orderadmin/updateorder';
	//取消订单or将订单状态从待收款置为已完成
	var operateOrderUrl='/buy/orderadmin/operateorder';
	//添加评论
	var commentOrderUrl='/buy/orderadmin/commentorder';
	// 页码
	var pageNum = 1;
	// 获取订单类型0.待收款/1.已收款/2.交易完成/3.交易失败
	 var orderStatus='-1';
	 //限定订单类型为1（我买到的）
	 var orderType='1'; 
	// 预先加载3条信息
	addItems(pageSize, pageNum,orderStatus,orderType);
	// 获取商品类别列表以及区域列表信息
	/**
	 * F1:获取前端点击的是哪个div，根据不同的div传入不同的值
	 */
	$('#allsold').click(function() 
	{
		$('#allsold-div').empty(); 
		pageNum=1;
		orderStatus='-1';
		addItems(pageSize, pageNum,orderStatus,orderType);
	});
	$('#pendingpay').click(function() {
		$('#pendingpay-div').empty(); 
		pageNum=1;
		orderStatus='0';
		addItems(pageSize, pageNum,orderStatus,orderType);
	});
	$('#paid').click(function() {
		$('#paid-div').empty(); 
		pageNum=1;
		orderStatus='1';
		addItems(pageSize, pageNum,orderStatus,orderType);
	});
	$('#completed').click(function() {
		$('#completed-div').empty(); 
		pageNum=1;
		orderStatus='2'; 
		addItems(pageSize, pageNum,orderStatus,orderType);
	});
	$('#failed').click(function() {
		$('#failed-div').empty(); 
		pageNum=1;
		orderStatus='3';
		addItems(pageSize, pageNum,orderStatus,orderType);
	});
	/**
	 * F2:获取分页展示的订单列表信息
	 * 
	 * @param pageSize
	 *            每页显示的数据
	 * @param pageIndex
	 *            页码
	 * @returns
	 */
	function addItems(pageSize, pageIndex,orderStatus,orderType) {
		//拼接处查询的URL，赋空值默认就去掉这个条件的限制，有值就代表这个条件去
		var url = listUrl+'?' + 'pageIndex=' + pageIndex + '&pageSize='
			+ pageSize + '&orderStatus=' + orderStatus + '&orderType=' 
			+ orderType+'&enableStatus=1';
		//设定加载符，若还在后台取数据则不能再次访问后台，避免多次重复加载
		loading = true;
		//访问后台获取相应查询条件下的商品列表
		$.getJSON(url, function(data) {
			if (data.success) {
				//获取当前查询条件下的总数
				maxItems = data.count;
				var html = '';
				data.orderList.map(function(item, index) {
					html += '' + '<div class="card" data-order-id="'
							+ item.orderId + '">' + '<div class="card-header">'
							+ item.product.productName + '</div>'
							+ '<div class="card-content">'
							+ '<div class="list-block media-list">' + '<ul>'
							+ '<li class="item-content">'
							+ '<div class="item-media">' + '<img src="'
							+ item.product.productImg + '" width="100">' + '</div>'
							+ '<div class="item-inner">'
							+'<div class="item-title-row">'
							+ '<div class="item-subtitle">' + item.product.productDesc + '</div>' 
							+'<div class="item-after" style="color:red">'+"￥"+item.product.productPrice + '</div></div>'
							+'<div class="item-text"><p style="color: cadetblue;  font-size: smaller;">订单状态:'+getOrderStatus(item.orderStatus)+'</p><div>' 
							+'</li>' + '</ul>' 
							+ '</div>' 
							+ '</div>' + '<div class="card-footer">'
							+ '<p class="color-gray">'
							+ new Date(item.lastEditTime).Format("yyyy-MM-dd hh:mm:ss")
							+ '更新</p>' + '<span>'+goOperation(item.orderStatus,item.orderId,item.product.productId,item.orderComment)+'</span>'
							+ '</div>'
							+ '</div>';
				});
				//将卡片集合添加到目标HTML组件里
				if(orderStatus=='-1')
				{
					$('#allsold-div').append(html);
					//获取目前为止已显示的卡片总数，包含之前已经加载的
					var total = $('#allsold-div .card').length;
				}
				if(orderStatus=='0')
				{
					$('#pendingpay-div').append(html);
					//获取目前为止已显示的卡片总数，包含之前已经加载的
					var total = $('#pendingpay-div .card').length;
				}
				if(orderStatus=='1')
				{
					$('#paid-div').append(html);
					//获取目前为止已显示的卡片总数，包含之前已经加载的
					var total = $('#paid-div .card').length;
				}
				if(orderStatus=='2')
				{
					$('#completed-div').append(html);
					//获取目前为止已显示的卡片总数，包含之前已经加载的
					var total = $('#completed-div .card').length;
				}
				if(orderStatus=='3')
				{
					$('#failed-div').append(html);
					//获取目前为止已显示的卡片总数，包含之前已经加载的
					var total = $('#failed-div .card').length;
				}
				
				//若总数达到跟按照此前查询条件列出来的总数一致，则停止后台的加载
				if (total >= maxItems) { 
					//隐藏提示符
					$('.infinite-scroll-preloader').hide();
				}
				else
				{
					$('.infinite-scroll-preloader').show();
				}
				//否则页码加1，继续load出新的店铺
				pageNum += 1;
				//加载结束，可以再次加载了
				loading = false;
				//刷新页面，显示新加载的店铺
				$.refreshScroller();
			}
		});

	}
	//下滑屏幕自动进行分页搜索
	$(document).on('infinite', '.infinite-scroll-bottom', function() {
		if (loading)
			return;
		addItems(pageSize, pageNum,orderStatus,orderType);
	});
	 //返回显示订单状态
	function getOrderStatus(id) {
		 if(id==0)
		 {
			 return '待付款';
		 }
		 if(id==1)
		 {
			 return '等待卖家收款';
		 }
		 if(id==2)
		 {
			 return '交易成功';
		 }
		 if(id==3)
		 {
			 return '交易失败';
		 } 
	} 
	function goOperation(orderStatus,orderId,productId,orderComment) 
	{
		orderComment = orderComment || '';
		//待付款
		if(orderStatus==0)
		{
			//1.删除订单，2.取消订单 ，3.付款
			return '<a href="#" data-id="'+orderId+'" data-value="'+productId+'" class="create-pending">选择操作</a>';
		}
		//等待卖家收款
		if(orderStatus==1)
		{
			//1.删除订单， 2.取消订单，
			return '<a href="#"  data-id="'+orderId+'" data-value="'+productId+'" class="create-paid">选择操作</a>';
		}
		//已完成 
		if(orderStatus==2)
		{
			//1.删除订单，2。完成评价 
			return '<a href="#"  data-id="'+orderId+'" data-value="'+productId+'" data-comment="'+orderComment+'" class="create-completed prompt-ok">选择操作</a>';
		}
		//交易失败
		if(orderStatus==3)
		{
			//1.删除订单 
			return '<a href="#"  data-id="'+orderId+'" data-value="'+productId+'" class="create-failed">选择操作</a>';
		}
			
	}
	//orderStatus==0(待付款)
	$(document).on('click','.create-pending', function (e) {
		  var orderId=e.currentTarget.dataset.id;
		  var productId=e.currentTarget.dataset.value;
		  var orderStatus=0;
	      var buttons1 = [
	        {
	          text: '请选择',
	          label: true
	        },
	        {
	          text: '取消订单',
	          bold: true,
	          color: 'danger',
	          onClick: function() {
	        	  //将买卖双方的订单状态设置为3
	        	  var url=operateOrderUrl+'?orderId='+orderId+'&orderStatus=3'+'&productId='+productId; 
	             $.getJSON(url,function(data){
	            	 if(data.success)
	            	 {
	            		 $.alert("你已取消该订单");  
	            		 var pageNum=1;
	            		 $('#allsold-div').empty();
	            		 addItems(pageSize, pageNum,-1,orderType);
	            		 $('#pendingpay-div').empty();
	            		 addItems(pageSize, pageNum,orderStatus,orderType);
	            	 }
	            	 else
	            	 {
	            		 $.alert("取消订单失败");
	            	 }
	             });
	          }
	        },
	        {
	          text: '删除订单',
	          onClick: function() {
	        	  //只需单方面修改自己的订单
	        	  var url=modifyOrderUrl+'?orderId='+orderId+'&enableStatus=0'; 
	        	  $.getJSON(url,function(data){
		            	 if(data.success)
		            	 {
		            		 $.alert("你已删除该订单"); 
		            		 var pageNum=1;
		            		 $('#allsold-div').empty();
		            		 addItems(pageSize, pageNum,-1,orderType);
		            		 $('#pendingpay-div').empty();
		            		 addItems(pageSize, pageNum,orderStatus,orderType);
		            	 }
		            	 else
		            	 {
		            		 $.alert("删除订单失败");
		            	 }
		             });
	          }
	        },
	        {
			    text: '付款',
			    onClick: function() {
			    	//将买卖双方的订单状态设置为1(待收款)
			    	 var url=operateOrderUrl+'?orderId='+orderId+'&orderStatus=1'+'&productId='+productId;  
		             $.getJSON(url,function(data){
		            	 if(data.success)
		            	 {
		            		 $.alert("付款成功，等待卖家收款！");
		            		 var pageNum=1;
		            		 $('#allsold-div').empty();
		            		 addItems(pageSize, pageNum,-1,orderType);
		            		 $('#pendingpay-div').empty();
		            		 addItems(pageSize, pageNum,orderStatus,orderType);
		            	 }
		            	 else
		            	 {
		            		 $.alert("付款失败!");
		            	 }
		             });
			 }
			},
	        {
		          text: '查看商品详情',
		          onClick: function() {
		        	  //查看该商品详情 
		        	  window.location.href='/buy/frontendadmin/productdetail?productId='+productId;
		          }
		        }   
	      ];
	      var buttons2 = [
	        {
	          text: '取消',
	          bg: 'danger'
	        }
	      ];
	      var groups = [buttons1, buttons2];
	      $.actions(groups);
	  });
	//orderStatus==1(等待卖家收款)
	$(document).on('click','.create-paid', function (e) {
		  var orderId=e.currentTarget.dataset.id;
		  var productId=e.currentTarget.dataset.value;
		  var orderStatus=1;
	      var buttons1 = [
	        {
	          text: '请选择',
	          label: true
	        },
	        {
	          text: '取消订单',
	          bold: true,
	          color: 'danger',
	          onClick: function() {
	        	  //将买家的订单状态也设置为3
	        	  var url=operateOrderUrl+'?orderId='+orderId+'&orderStatus=3'+'&productId='+productId; 
	             $.getJSON(url,function(data){
	            	 if(data.success)
	            	 {
	            		 $.alert("你已取消该订单"); 
	            		 var pageNum=1;
	            		 $('#allsold-div').empty();
	            		 addItems(pageSize, pageNum,-1,orderType);
	            		 $('#paid-div').empty();
	            		 addItems(pageSize, pageNum,orderStatus,orderType);
	            	 }
	            	 else
	            	 {
	            		 $.alert("取消订单失败");
	            	 }
	             });
	          }
	        },
	        {
	          text: '删除订单',
	          onClick: function() {
	        	//将买家的订单状态也设置为3
	        	  var url=modifyOrderUrl+'?orderId='+orderId+'&enableStatus=0'; 
	             $.getJSON(url,function(data){
	            	 if(data.success)
	            	 {
	            		 $.alert("你已删除该订单");
	            		 var pageNum=1;
	            		 $('#allsold-div').empty();
	            		 addItems(pageSize, pageNum,-1,orderType);
	            		 $('#paid-div').empty();
	            		 addItems(pageSize, pageNum,orderStatus,orderType);
	            	 }
	            	 else
	            	 {
	            		 $.alert("删除订单失败");
	            	 }
	             });
	          }
	        },
	        {
		          text: '查看商品详情',
		          onClick: function() {
		        	  //查看该商品详情 
		        	  window.location.href='/buy/frontendadmin/productdetail?productId='+productId;
		          }
		        }
	      ];
	      var buttons2 = [
	        {
	          text: '取消',
	          bg: 'danger'
	        }
	      ];
	      var groups = [buttons1, buttons2];
	      $.actions(groups);
	  });
	//orderStatus==2(订单已完成,待评价)
	$(document).on('click','.create-completed', function (e) {
		  var orderId=e.currentTarget.dataset.id;
		  var productId=e.currentTarget.dataset.value;
		  var orderStatus=2;
		  var orderComment=e.currentTarget.dataset.comment;
	      var buttons1 = [
	        {
	          text: '请选择',
	          label: true
	        }, 
	        {
	          text: '删除订单',
	          onClick: function() {
	        	//将买家的订单状态也设置为3
	        	  var url=modifyOrderUrl+'?orderId='+orderId+'&enableStatus=0'; 
	             $.getJSON(url,function(data){
	            	 if(data.success)
	            	 {
	            		 $.alert("你已删除该订单");
	            		 var pageNum=1;
	            		 $('#allsold-div').empty();
	            		 addItems(pageSize, pageNum,-1,orderType);
	            		 $('#completed-div').empty();
	            		 addItems(pageSize, pageNum,orderStatus,orderType);
	            	 }
	            	 else
	            	 {
	            		 $.alert("删除订单失败");
	            	 }
	             });
	          }
	        }, 
		        {
		          text: '查看商品详情',
		          onClick: function() {
		        	  //查看该商品详情 
		        	  window.location.href='/buy/frontendadmin/productdetail?productId='+productId;
		          }
		        } 
	      ]; 
	      //评价交易
	      var buttons2=[ 
		        {
			        text: '评价交易',
			        onClick: function() {
			        	$.prompt('请输入评价内容:', function(value) 
								{
									if(value=='')
									{
										$.alert("评价不能为空！");
										return;
									} 
									$.ajax({
									    //将订单状态设置为4（表示订单已评价）								
										url : commentOrderUrl,
										async:false,
										cache:false,
										type : 'post',
										dataType:'json',
										data : {
											orderComment : value,orderId:orderId 
										},
										dataType : 'json',
										success : function(data) {
											if (data.success) {
												$.alert("评价成功！");
												 var pageNum=1;
							            		 $('#allsold-div').empty();
							            		 addItems(pageSize, pageNum,-1,orderType);
							            		 $('#completed-div').empty();
							            		 addItems(pageSize, pageNum,orderStatus,orderType);
											} else {
												$.alert("评价失败！");
											}
										}
									});

								});
			        }
			     },
	      ];
	      //查看评价
	      var buttons3=[ 
		        {
			        text: '查看评价',
			        onClick: function() 
			        {
			        	window.location.href='/buy/orderadmin/showcommentdetail?productId='+productId;
			        }
			     },
	      ];
	      var buttons4 = [
	        {
	          text: '取消',
	          bg: 'danger'
	        }
	      ];
	      var groups='';
	      //若该订单已评价
	      if(orderComment!=='')
	      {
	        groups= [buttons1, buttons3,buttons4];
	      }
	      else
	      {
	    	  groups= [buttons1, buttons2,buttons4];
	      }
	      $.actions(groups);
	  });
	
	//orderStatus==3(订单失败)
	$(document).on('click','.create-failed', function (e) {
		  var orderId=e.currentTarget.dataset.id;
		  var productId=e.currentTarget.dataset.value;
		  var orderStatus=3;
	      var buttons1 = [
	        {
	          text: '请选择',
	          label: true
	        },
	         
	        {
	          text: '删除订单',
	          onClick: function() {
	        	//将买家的订单状态也设置为3
	        	  var url=modifyOrderUrl+'?orderId='+orderId+'&enableStatus=0'; 
	             $.getJSON(url,function(data){
	            	 if(data.success)
	            	 {
	            		 $.alert("你已删除该订单");
	            		 var pageNum=1;
	            		 $('#allsold-div').empty();
	            		 addItems(pageSize, pageNum,-1,orderType);
	            		 $('#failed-div').empty();
	            		 addItems(pageSize, pageNum,orderStatus,orderType);
	            	 }
	            	 else
	            	 {
	            		 $.alert("删除订单失败");
	            	 }
	             });
	          }
	        },
	        {
		          text: '查看商品详情',
		          onClick: function() {
		        	  //查看该商品详情 
		        	  window.location.href='/buy/frontendadmin/productdetail?productId='+productId;
		          }
		        } 
	      ];
	      var buttons2 = [
	        {
	          text: '取消',
	          bg: 'danger'
	        }
	      ];
	      var groups = [buttons1, buttons2];
	      $.actions(groups);
	  });
	//侧边栏
	$('#me').click(function() {
		$.openPanel('#panel-left-demo');
	});

	$.init();
});