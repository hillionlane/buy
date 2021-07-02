$(function() {
	var productId = getQueryString('productId');
	// 获取商品信息
	var productUrl = '/buy/productadmin/getproductbyid?productId=' + productId;
	// 获取商品地址
	var productAddrUrl = '/buy/frontendadmin/getproductaddr?productId='
			+ productId;
	// 获取该商品用户信息URL
	var hispageInfoUrl = '/buy/personinfoadmin/getuserbyid';
	// 查询该商品是否被收藏
	var isCollect = '/buy/frontendadmin/getcollection';
	// 收藏该商品
	var addCollectionUrl = '/buy/frontendadmin/addcollection';
	// 取消收藏
	var removeCollectionUrl = '/buy/frontendadmin/removecollection';
	// 添加留言
	var addCommentUrl = '/buy/commentadmin/addcomment';
	// 查询留言列表
	var getCommentListUrl = '/buy/commentadmin/getcommentlist';
	var userId = '';
	/**
	 * F1:获取商品详情信息
	 */
	$ .getJSON(
					productUrl,
					function(data) {
						if (data.success) {
							var product = data.product;
							var productId = product.productId;
							var productUserId=product.personInfo.userId;
							var productName=product.productName;
							var productEnableStatus=product.enableStatus;
							$('#product-img').attr('src', product.productImg);
							$('#product-time').text(
									new Date(product.lastEditTime)
											.Format("yyyy-MM-dd hh:mm:ss"));
							$('#product-name').text(product.productName);
							$('#product-desc').text(product.productDesc);
							$('#product-price')
									.text("￥" + product.productPrice);
							/**
							 * F2:获取商品地址信息
							 */
							$.getJSON(productAddrUrl, function(data) {
								if (data.success) {
									var productAddr = data.productAddr;
									$('#product-addr').text(productAddr);
								}
							});
							var imgListHtml = '';
							product.productImgList
									.map(function(item, index) {
										imgListHtml += '<div> <img style="width: 100%;" src="'
												+ item.imgAddr + '"/></div>';
									});
							
							$('#imgList').html(imgListHtml);
							userId = product.personInfo.userId; 
							 
							/**
							 * F3:获取该商品用户信息
							 */
							var url = hispageInfoUrl + '?' + 'userId=' + userId;
							$.getJSON(url, function(data) {
								if (data.success) {
									var user = data.tempUser; 
									var html = '<img src="' + user.profileImg
											+ '" width="100"style=" height: 70px; ">';
									$('#user-name').text(user.userName);
									$('#user-desc').text(user.userDesc);
									$('#user-phone').text(user.userPhone);
									// $('#user-profile').attr('src',
									// user.profileImg);
									$('#user-profile').append(html); 
									
									/**
									 * 点击用户信息，跳转到该商品的用户主页面
									 */
									$('#sellor-info').on( 'click', '.item-content',
									function() 
									{
										if(data.isme)
										{
											window.location.href = '/buy/localadmin/myproducts';
										}
										else
										{
											window.location.href = '/buy/localadmin/hispage?userId='
																+ userId;
										}
									});
								}
							});
							$('#me').click(function() {
								$.openPanel('#panel-left-demo');
							});
							
							//获取商品评论列表
							getCommentList();

							/**
							 * F4:获取商品评论列表
							 */
							function getCommentList() {
								// 查询新的留言列表
								var getUrl = getCommentListUrl + '?'
										+ 'productId=' + productId; 
								$.ajaxSettings.async = false; 
								$.getJSON(getUrl,function(data) 
							     {
									if (data.success) 
									{
										// 展示到前端页面
										var html = '';
										data.commentList.map(function(item,index) 
										{
											var commentId=item.commentId;
											html += ''
											+ '<div class="list-block media-list"  data-comment-id="'
											+ item.commentId
											+ '">'
											+ '<ul>'
											+ '<li>'
											+ '<div class="item-content">'
											+ '<div class="item-media">'
											+ '<img src="'
											+ item.user.profileImg
											+ '" width="100">'
											+ '</div>'
											+ '<div class="item-inner"><div class="item-title-row">'
											+ '<div class="item-title" style=" font-size: larger; color: lightskyblue;">'
											+ item.user.userName
											+ '</div>'
											+'<div class="item-after">'
											+'<span>'+isMyComment(commentId)+'</span>'
											+'</div>'
											+'</div>'
											+ '<div class="item-subtitle">'
											+ item.content
											+ '</div>'
											+' <div class="item-text" style="font-size:small; height:1rem; " >'
											+new Date(item.createTime) .Format("yyyy-MM-dd hh:mm:ss")
											+'</div></div></div></li></ul></div>'
											
											function isMyComment(commentId)
											{
												var isMyCommentUrl='/buy/frontendadmin/ismycomment?commentUserId='+item.user.userId
													+'&productUserId='+productUserId;
												var result;
												$.getJSON(isMyCommentUrl,function(data){
													//判断该商品是否为当前登陆用户的商品，若为当前登陆用户的商品，则评论全部可以操作
													if(data.isMyProduct)
													{  
														result = '<a data-id="' + commentId + '"  href="#" class="delete" external>删除</a>'; 
													}
													else
													{
														//否则只可操作自己的评论
														if(data.success)
														{
															result = '<a data-id="' + commentId + '" href="#" class="delete" external>删除</a>'; 
														}
														else
														{
															result = '';
														}
														
													}
												});
												return result;
											} 
										});
										$('.comment-div').append(html);
									} 
									else 
									{
										return;
									}
									 
								}); 
								$.ajaxSettings.async = true; 
							}
							/**
							 *  点击删除按钮，删除该条留言，并重新刷新留言区
							 */
							$('#commentList').on('click','a',function(e){
								var target=$(e.currentTarget);
								if(target.hasClass('delete'))
								{ 
									goDelete(e.currentTarget.dataset.id);
								}
							});
							function goDelete(id)
							{ 
							 var url='/buy/commentadmin/removecomment?commentId='+id;
								$.confirm('是否确定删除该评论?',function(){
									//商品删除操作
									$.getJSON(url,
										 function(data)
										 {
											 if(data.success)
											{
												 $.toast('删除成功！');
												 $('.comment-div').empty();
												 getCommentList();
											}
											 else
											{
												 $.toast('操作失败！'); 
											}
										 }
									);
								}); 
							}
							
							/**
							 * 点击收藏按钮
							 */
							var getUrl = isCollect + '?' + 'productId='
									+ productId;
							$.getJSON(getUrl,function(data){
								if(data.success)
								{
									$("#collect").addClass('red-collect');
								}
							}); 
							$('#collect').on('click',function() 
							{
								$.getJSON(getUrl,function(data) {
								if (data.success) 
								{
									// 取消收藏
									var cancelUrl =removeCollectionUrl+'?'+'productId='+productId;
									$.getJSON(cancelUrl,function(data) 
									{
										if (data.success) 
										{
										    $.toast("已取消收藏！");
											$("#collect").removeClass('red-collect');
										} 
										else 
										{
											$.toast("取消收藏失败！");}
									});
								 } 
								 else
								 {
									// 添加收藏
								    var addUrl = addCollectionUrl+'?'+'productId='+ productId;
									$.getJSON(addUrl,function(data) 
									{
										if (data.success) 
										{
											$.toast("收藏成功！");
											$("#collect").addClass('red-collect');
										} else 
										{
											$.toast("收藏失败！");
										}
									});
									}
								}); 
					      });
							
							/**
							 * 点击留言按钮
							 */
							$(document).on(
									'click',
									'.prompt-ok',
									function() {
										$.prompt('请输入留言内容:', function(value) 
										{
											if(value=='')
											{
												$.alert("留言不能为空！");
												return;
											}
											var addUrl = addCommentUrl + '?'
													+ 'productId=' + productId;
											$.ajax({
												url : addUrl,
												type : 'GET',
												data : {
													content : value
												},
												dataType : 'json',
												success : function(data) {
													if (data.success) {
														$.alert("留言成功！");
														$('.comment-div').empty();
														getCommentList();
													} else {
														$.alert("留言失败！");
													}
												}
											});

										});
									});
							if(productEnableStatus==3)
							{
								$('#issoldout').html('已出售');
								$('#issoldout').addClass('red-sold');
								$('#issoldout').removeClass('icon');
								$('#issoldout').removeClass('icon-cart');
							}
							/**
							 * 点击购买按钮
							 */
							else
							{
								$('#getproduct').html('购买'); 
							$(document).on('click','.confirm-ok', function () {
							      $.confirm('你确定要购买"'+productName+'"吗?', function () 
							      {
							    	  //1.将该商品置为下架状态（0）
//							    	  var changeProductStatus="/buy/frontendadmin/changeproductstatus?productId="+productId;						    	  
							    	  //2.添加入买卖双方的订单表中
							    	  var addOrder="/buy/orderadmin/addorder?productId="+productId;
							    	  $.getJSON(addOrder,function(data){
							    		  if(data.success)
							    		  { 
							    			  $.toast('下单成功，请前往“我买到的”进行操作！');
									    	   //下单完成后跳转至我买到的
							    			 /* setTimeout(function(){
							    				  window.location.href='/buy/frontendadmin/productdetail?productId='+productId;
								                },3000); */
									      }  
									      else
									      {
									    	  $.alert(data.errMsg);
									      }
									    });  
							      });
							  });
						}}
					}); 
	$.init();
});
