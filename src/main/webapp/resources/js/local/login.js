$(function(){ 
	//登入的Url
	var loginUrl="/buy/localadmin/logincheck";
	//登录次数，累计登录三次失败之后自动弹出验证码要求输入
	var loginCount=0;
	$('#submit').click(function(){
		//获取输入的用户名
		var userName=$('#user-name').val();
		//获取输入的密码
		var password=$('#password').val();
		//获取输入的验证码
		var verifyCodeActual=$('#j_kaptcha').val();
		var needVerify=false;
		//如果登录三次都失败
		if(loginCount>=3)
		{
			//那么就要验证码校验
			if(!verifyCodeActual)
			{
				$.toast('请输入验证码！');
				return;
			}
			else
			{
				needVerify=true;
			}
		}
		//访问后台，登录
		$.ajax({
			url:loginUrl,
			async:false,
			cache:false,
			type:'post',
			dataType:'json',
			data:{userName:userName,password:password,verifyCodeActual:verifyCodeActual,needVerify:needVerify},
			success:function(data)
			{ 
				if(data.success)
				{
					var user=data.user;
					if(user.enableStatus!=1)
					{
						$.toast('你的账号已禁用！');	
						return;
					}
					$.toast('登录成功！',1300); 
//					Toast('这是一个弹框',10000);
					/*alert('登录成功！');*/
					 
					if(data.userType==1)
					//若是用户则跳转至前端展示页面
					{
//						window.location.href="/buy/frontendadmin/mainpage";
						setTimeout(function(){
							window.location.href = "/buy/frontendadmin/mainpage";
		                },2000);
					} 
					if(data.userType==2)
					{
						setTimeout(function(){
						window.location.href="/buy/superadmin/main";
						 },2000);
					}
				}
				else
				{
					$.toast('登录失败！用户名或密码错误');
					loginCount++;
					if(loginCount>=3)
					{
						//登录三次，需要做验证码校验
						$('#verifyPart').show();
					}
					//在提交完成后，不论成功或者失败，都要更换一下验证码
					$('#kaptcha_img').click();
				}
			}
		});
	});
});