$(function(){
	$('#log-out').click(function(){
		//清除session
		$.ajax({
			url:"/buy/localadmin/logout",
			type:'post',
			async:false,
			cache:false,			
			dataType:'json',
			success:function(data){
				if(data.success)
				{
					window.location.href="/buy/localadmin/login" ;
					return false;
				}
				
			},
			error:function(data,error)
			{alter("注销失败！");},
		});
	});
});