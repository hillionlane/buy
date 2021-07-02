$(function() {
	var productId = getQueryString('productId');
	// 获取商品信息
//	var productUrl = '/buy/productadmin/getproductbyid?productId=' + productId; 
	//获取该商品的两条订单信息以及买卖双方的信息及商品信息
	var ordercommentUrl='/buy/orderadmin/getordercomment?productId=' + productId;
	// 获取该商品用户信息URL
	var hispageInfoUrl = '/buy/personinfoadmin/getuserbyid'; 
	var userId = '';
	/**
	 * F1:获取商品详情信息
	 */
	$ .getJSON(
			ordercommentUrl,
					function(data) {
						if (data.success) {
							var product = data.product;
							var buyerOrder=data.buyerOrder;
							var sellerOrder=data.sellerOrder;
							var seller=data.seller;
							var buyer=data.buyer;
							var productId = product.productId;
							var productUserId=product.personInfo.userId;
							var productName=product.productName;
							var productEnableStatus=product.enableStatus;
							var buyerComment=buyerOrder.orderComment;
							var sellerComment=sellerOrder.orderComment;
							$('#product-img').attr('src', product.productImg);
							$('#completed-time').text("交易完成时间  "+
									new Date(buyerOrder.lastEditTime)
											.Format("yyyy-MM-dd hh:mm:ss"));
							$('#product-name').text(product.productName);
							$('#product-desc').text(product.productDesc);
							$('#product-price')
									.text("￥" + product.productPrice);
							
							//获取交易评论情况
							if(buyerComment)
							{
								$('#buyer-img').attr('src',buyer.profileImg);
								$('#buyer-name').text(buyer.userName);
								$('#buyer-Ocomment').text(buyerComment);
								$('#buyer-ctime').text("买家评论时间 "+
										new Date(buyerOrder.commentTime)
												.Format("yyyy-MM-dd hh:mm:ss"));
							}
							else
							{
								$('#buyer-comment').text("买家暂无评价");
							}
							if(sellerComment)
							{
								$('#seller-img').attr('src',seller.profileImg);
								$('#seller-name').text(seller.userName);
								$('#seller-Ocomment').text(sellerComment);
								$('#seller-ctime').text("卖家评论时间 "+
										new Date(sellerOrder.commentTime)
												.Format("yyyy-MM-dd hh:mm:ss"));
							}
							else
							{
								$('#seller-comment').text("卖家暂未评价");
							}   
							//点击buyer-img买家头像跳转至买家主页
							$('#buyer-img').click(function(){
								//若当前登录用户为买家，则点击买家头像，跳转至我的主页，否则跳转至他的主页
							    if (data.isbuyer) 
								{
							    	window.location.href = '/buy/localadmin/myproducts';
								}
							    else
							    {
							    	window.location.href = '/buy/localadmin/hispage?userId='
										+ buyer.userId;
							    } 
							});
							//点击seller-img卖家头像跳转至卖家主页
							$('#seller-img').click(function(){
								//若当前登录用户为卖家，则点击卖家头像，跳转至我的主页，否则跳转至他的主页
							    if (data.isseller) 
								{
							    	window.location.href = '/buy/localadmin/myproducts';
								}
							    else
							    {
							    	window.location.href = '/buy/localadmin/hispage?userId='
										+ seller.userId;
							    } 
							});
						}
						
				}); 
	//点击商品图片跳转至商品详情页面
	$('#product-img').click(function(){
		window.location.href='/buy/frontendadmin/productdetail?productId='+productId;
	});
	
	
	$.init();
});
