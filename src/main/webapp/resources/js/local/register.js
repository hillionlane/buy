$(function(){ 
	//注册页面
	var registerUrl="/buy/personinfoadmin/adduser";
	//获取区域列表
	var getAreaUrl="/buy/areaadmin/listarea";
	//判断该用户名是否已存在
	var isNameExistUrl="/buy/localadmin/isnameexist";
	//增加
	getUserInfo();
	var flag='';
	 
	/**
	 * F1:增加用户
	 * 获取区域列表填充至前台
	 */
	function getUserInfo()
	{
		$.getJSON(getAreaUrl,function(data){
			if(data.success)
			{
				var areaHtml='';
				data.areaList.map(function(item, index) 
				{
					areaHtml += '<option data-id="' + item.areaId + '">'
						+ item.areaName + '</option>';
				});
				$('#area').html(areaHtml);
			}
		});
	}
	//To Do 判断用户名在数据库中是否有重复的
	/**
	 * F2:点击提交，获取表单内容
	 */
	$('#submit').click(function(){
		//从前台获取信息
		var user={}; 
		//控制用户名格式
		var userName=$('#user-name').val(); 
		if(userName!=null&&userName!='')
		{
			if(userName.toString().length>=3&&userName.toString().length<=12)
			{
				var url=isNameExistUrl+'?userName='+userName;
				$.getJSON(url,
						function(data){
					if(data.success)
					{
						var userName=$('#user-name').val(); 
						user.userName=userName; 
						user.userGender=document.getElementById("user-gender").options[
							document.getElementById("user-gender").selectedIndex].text; 
						user.userEmail=$('#user-email').val(); 
						user.area={
								areaId:$('#area').find('option').not(function()
									    {
											return !this.selected;
										}).data('id')
								};
						user.userAge=$('#user-age').val(); 
						user.userDesc=$('#user-desc').val();
						//验证用户电话 
						var userPhone=$('#user-phone').val();
						if(userPhone!=null&&userPhone!='')
						{
							user.userPhone=userPhone;
						}
						else
						{
							$.toast("请填写您的联系方式！");
							return;
						}
						//验证用户地址
						var userAddr=$('#user-addr').val();
						if(userAddr!=null&&userAddr!='')
						{
							user.userAddr=userAddr;
						}
						else
						{
							$.toast("请填写您的详细地址！");
							return;
						}
						
						//验证上传头像
						var profileImg=$('#profile-img')[0].files[0];
						var userName=$('#user-name').val();
						if(!profileImg)
						{
							$.toast('请上传图片！');
							return;
						}
						//验证用户密码输入
						var password=$('#password').val();
						var checkPassword=$('#check-password').val();
						if(password!=null&&checkPassword!=null&&password!=''&&checkPassword!='')
						{
//							alert(password.toString().length);
							if((password.toString().length>=6&&password.toString().length<=12))
							{
								if(password!=checkPassword)
								{
									$.toast("两次密码不一致");
									return;
								}
								else
								{
									user.password=password;
								}
							}
							else
							{
								$.toast("请将密码长度控制在3-12位之间！");
								return;
							}
						}
						else
						{
							$.toast("请输入密码！");
							return;
						} 
						//验证验证码
						var verifyCodeActual=$('#j_kaptcha').val();
						if(!verifyCodeActual)
						{
							$.toast('请输入验证码！');
							return;
						} 
						var formData=new FormData();  
						formData.append('profileImg',profileImg);
						formData.append('userStr',JSON.stringify(user)); 
						//不为空
						formData.append('verifyCodeActual',verifyCodeActual);
						$.ajax({
							url: registerUrl,
							type:'POST',
							data:formData,
							contentType:false,
							processData:false,
							cache:false,
							//data为后台返回的数据
							success:function(data)
							{
								if(data.success)
								{
									 
									$.toast("提交成功！");
									window.location.href='/buy/localadmin/login';
									 
								}else
								{
									$.toast("提交失败！"+data.errMsg);
								}
								//在提交完成后，不论成功或者失败，都要更换一下验证码
								$('#kaptcha_img').click();
							}
						});
					}
					else  
					{
						$.toast("此用户名已存在，请重新输入！");
						return;
					}
				});
			}
			else
			{
				$.toast("请将用户名长度控制在6-12位之间！"); 
			} 
		}
		else
		{
			$.toast("用户名不能为空！");
			return;
		}
		
	});
	
});