$(function(){
	//获取我的订单列表
	var getMyOrderComments='/buy/orderadmin/getmycommentorderlist'; 
	$.getJSON(getMyOrderComments,function(data) 
    {
		if(data.success)
		{
			// 展示到前端页面
			var html = '';
			data.myCommentOrderList.map(function(item,index) 
		    {
				var orderId=item.orderId;
				html += ''
					+ '<div class="list-block media-list"  data-order-id="'
					+ item.orderId
					+ '">'
					+ '<ul>'
					+ '<li>'
					+ '<div class="item-content">'
					+ '<div class="item-media" id="comment-user">'
					+ '<img src="'
					+ item.user.profileImg
					+ '" width="100">' 
					+ '</div>'
					+ '<div class="item-inner" id="comment-detail"><div class="item-title-row">'
					+ '<div class="item-title" style=" font-size: larger; color: lightskyblue;">'
					+ item.user.userName
					+ '</div>' 
					+'</div>'
					+ '<div class="item-subtitle">'
					+ item.orderComment
					+ '</div>'
					+' <div class="item-text" style="font-size:small; height:1rem; " >'
					+new Date(item.commentTime) .Format("yyyy-MM-dd hh:mm:ss")
					+'</div></div></div></li></ul></div>'  
					
					//点击用户头像，跳转至用户主页
					$('.comment-div').on('click','#comment-user',function() {
						window.location.href ='/buy/localadmin/hispage?userId='+item.user.userId;
							});
					//点击comment-detail div跳转至评论详情页面
					$('.comment-div').on('click','#comment-detail',function() {
						window.location.href='/buy/orderadmin/showcommentdetail?productId='+item.product.productId;
					});
		    });
			$('.comment-div').append(html);
		}
	});
	
 
});