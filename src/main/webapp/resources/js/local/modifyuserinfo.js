$(function(){
  
//	var userId=getQueryString('userId');
	//修改页面(通过Id获取用户信息+区域列表信息)
	var modifyUrl="/buy/personinfoadmin/modifyuserinfo";
	//通过userId获取用户信息
	var getUserUrl="/buy/personinfoadmin/getuserbyid";
	//获取区域列表
	var getAreaUrl="/buy/areaadmin/listarea";
	//判断该用户名是否已存在
	var isNameExistUrl="/buy/localadmin/isnameexist";
	 //F1:修改
	getUserInitInfo( );
	var userId='';
	var oldUser=null;
	var oldUserName='';
	/**
	 * F1:修改用户信息
	 * 将原用户的信息返回至前台
	 */
	function getUserInitInfo()
	{
		$.getJSON(getUserUrl,function(data)
		{
			if(data.success)
			{
				oldUser=data.user;
				$('#user-name').val(oldUser.userName);
				$('#user-email').val(oldUser.userEmail);
				$('#user-phone').val(oldUser.userPhone);
				$('#user-addr').val(oldUser.userAddr);
				$('#user-desc').val(oldUser.userDesc);
				$('#password').val(oldUser.password);
				$('#check-password').val(oldUser.password);
				$('#user-age').val(oldUser.userAge);
				//从后台获取原本性别
				var userGender=oldUser.userGender;
				if(userGender=="男")
				{
					$("#user-gender").find("option[id='male']").attr("selected",true);
				}
				else
				{
					$("#user-gender").find("option[id='female']").attr("selected",true);
				}
				$('#password').attr('disabled','disabled');
				$('#check-password').attr('disabled','disabled');
				
				$.getJSON(getAreaUrl,function(data)
				{
					if(data.success)
					{
						var tempAreaHtml='';
						data.areaList.map(function(item, index) 
						{
							tempAreaHtml += '<option data-id="' + item.areaId + '">'
							+ item.areaName + '</option>';
						});
						$('#area').html(tempAreaHtml);
						$("#area option[data-id='"+oldUser.area.areaId+"']").attr("selected","selected");
					}
				});
				userId=oldUser.userId;
				oldUserName=oldUser.userName;
			}
		});
		
	}
	/**
	 * F2:点击提交，获取表单内容
	 */
	$('#submit').click(function(){
		//从前台获取信息
		var user={};
		var userName=$('#user-name').val();
		if(oldUserName!=userName)
		{
			if(userName!=null&&userName!='')
			{
				if(userName.toString().length>=3&&userName.toString().length<=12)
				{
					user.userName=userName;
				}
				else
				{
					$.toast("请将用户名长度控制在3-12位之间！"); 
					return;
				}
			}
			else
			{
				$.toast("用户名不能为空！");
				return;
			}
		}
		else
		{
			user.userName=userName;
		}
		user.userId=userId; 
		user.userName=$('#user-name').val();
		user.userGender=document.getElementById("user-gender").options[
			document.getElementById("user-gender").selectedIndex].text; 
		user.userEmail=$('#user-email').val();
		user.userPhone=$('#user-phone').val();
		user.userAge=$('#user-age').val();
		user.area={
				areaId:$('#area').find('option').not(function()
					    {
							return !this.selected;
						}).data('id')
				};
		user.userAddr=$('#user-addr').val();
		var profileImg=$('#profile-img')[0].files[0];
		user.userDesc=$('#user-desc').val();
		var formData=new FormData();
		formData.append('profileImg',profileImg);
		formData.append('userStr',JSON.stringify(user));
		var verifyCodeActual=$('#j_kaptcha').val();
		if(!verifyCodeActual)
		{
			$.toast('请输入验证码！');
			return;
		}
		//不为空
		formData.append('verifyCodeActual',verifyCodeActual);
		$.ajax({
			url: modifyUrl,
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
					$.toast("修改成功！");
					window.location.href='/buy/frontendadmin/mainpage';
					 
				}else
				{
					$.toast("修改失败！"+data.errMsg);
				}
				//在提交完成后，不论成功或者失败，都要更换一下验证码
				$('#kaptcha_img').click();
			}
		});
	});
	
});