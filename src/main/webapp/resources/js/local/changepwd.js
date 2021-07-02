$(function(){
	//修改密码的URL
	var changePwdUrl="/buy/personinfoadmin/modifyuserpwd";
	//通过userId获取用户信息
	var getUserUrl="/buy/personinfoadmin/getuserbyid";
	var user=null;
	$.getJSON(getUserUrl,function(data)
	{
		if(data.success)
		{
			user=data.user;
			$('#user-name').val(user.userName);
			$('#user-name').attr('disabled','disabled');
		}
	});
	$('#submit').click(function(){
		//获取输入的用户名
		var userName=$('#user-name').val();
		//获取输入的密码
		var password=$('#old-password').val();
		//获取新密码
		var newPassword=$('#new-password').val();
		//获取二次校验密码 
		var confirmPassword=$('#confirm-password').val();
		var verifyCodeActual=$('#j_kaptcha').val();
		if(password!=null&&password!='')
		{
			if(password!=user.password)
			{
				$.toast("旧密码输入有误！");
				return ;
			}
		}
		else
		{
			$.toast("请输入旧密码！");
			return ;
		}
		if(newPassword!=null&&confirmPassword!=null&&newPassword!=''&&confirmPassword!='')
		{
			if(newPassword.toString().length<6||newPassword.toString().length>12)
			{
				if(newPassword==password)
				{
					$.toast("新旧密码一致，无需更改！");
					return ;
				}
				$.toast("新密码长度应在6-12位之间！");
				return ;
			}
			else
			{
				if(newPassword!=confirmPassword)
				{
					$.toast("两次输入的新密码不一致！");
					return ;
				}
			}	  
		}
		else
		{
			$.toast("请输入新密码！");
			return ;
		}
		if(!verifyCodeActual)
		{
			$.toast('请输入验证码！');
			return;
		}
		//访问后台，登录
		$.ajax({
			url:changePwdUrl,
			async:false,
			cache:false,
			type:'post',
			dataType:'json',
			data:{userName:userName,password:password,newPassword:newPassword,verifyCodeActual:verifyCodeActual},
			success:function(data)
			{
				if(data.success)
				{
					$.toast('修改成功！');	 
					window.location.href='/buy/frontendadmin/mainpage';
				}
				else
				{
					$.toast('修改失败！');
				}
				//在提交完成后，不论成功或者失败，都要更换一下验证码
				$('#kaptcha_img').click();
			}
		});
	});
});