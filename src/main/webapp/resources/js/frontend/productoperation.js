$(function(){
	//获取url中的key值，并将其返回
	var productId=getQueryString('productId');
	var parentId='';
	//1.获取商品信息并返回给前台
	var productInfoUrl='/buy/productadmin/getproductbyid?productId='+productId;
	/*//2.获取商品分类列表并返回给前台
	var productCateListUrl='/buy/productcategoryadmin/getproductcategorylist';*/
	//2.获取商品一级类别+二级类别
	var get1stProCateUrl='/buy/productcategoryadmin/list1stlevelproductcategorys';
	var get2stProCateUrl='/buy/productcategoryadmin/list2stlevelproductcategorys';
	
	//3.增加商品
	var addProductUrl='/buy/productadmin/addproduct';
	//4.编辑商品信息
	var editProductUrl='/buy/productadmin/modifyproduct';
	//5.获取某商品类别的商品类别
	var getProCateById='/buy/productcategoryadmin/getproductcategorybyid';
	//判断productId是否为空，空表增加，非空表编辑
	var isEdit=productId?true:false;
	if(isEdit)
	{
		getProductInfo(productId);
	}
	else
	{
		getProductInitInfo(parentId);
	}
	/**
	 * F1:商品修改
	 * 获取商品初始信息赋值给前台表单，修改后传送至给后台
	 */
	function getProductInfo(productId)
	{
		var productCategory='';
		$.getJSON
		(productInfoUrl,function(data){
			if(data.success)
			{
			//从返回的JSON当中获取product对象的信息，并赋值给表单
			var product=data.product;
			$('#product-name').val(product.productName);
			$('#product-desc').val(product.productDesc);
			$('#priority').val(product.priority);
			$('#product-price').val(product.productPrice);  
			//获取该商品的商品类别
			var url=getProCateById+"?productCategoryId="+product.productCategory.productCategoryId;
			$.getJSON(url,function(data){
				if(data.success)
				{
					productCategory=data.productCategory;
				}
			});
			//生成商品一级类别
			$.getJSON
			(/*productCateListUrl*/get1stProCateUrl,function(data){
			if(data.success)
			{
			/*//获取原本的商品类别以及该店铺下的所有商品类别列表
			var optionHtml='';
			var optionArr=data.productCategoryList;
			var optionSelected=product.productCategory.productCategoryId;
			//生成前端的HTML商品类别列表，并默认选择编辑前的商品类别
			optionArr.map(function(item,index){
				var isSelect=optionSelected===item.productCategoryId?'selected'
						:'';
				optionHtml+='<option data-value="'
					+item.productCategoryId
					+'"'
					+isSelect
					+'>'
					+item.productCategoryName
					+'</option>';
			});
			
			$('#product-category').html(optionHtml);*/
				
				//生成一级商品列表
				var optionHtml='';
				var optionArr=data.productCategory1stList;
				// var optionSelected=product.productCategory.productCategoryId;
				//默认选择商品之前的一级类别id
				var optionSelected=productCategory.parent.productCategoryId;
				//生成前端的HTML商品类别列表，并默认选择编辑前的商品一级类别
				optionArr.map(function(item,index){
					var isSelect=optionSelected===item.productCategoryId?'selected'
							:'';
					optionHtml+='<option data-value="'
						+item.productCategoryId
						+'"'
						+isSelect
						+'>'
						+item.productCategoryName
						+'</option>';
				});
				$('#product-category-one').html(optionHtml);
				
				//生成该一级类别下的二级类别列表
				var productCate2st=get2stProCateUrl+'?parentId='+optionSelected;
				$.getJSON
				(/*productCateListUrl*/productCate2st,function(data){
				if(data.success)
				{
					var option2stHtml='';
					var option2stArr=data.productCategory2stList;
//					var optionSelected=product.productCategory.productCategoryId;
					//默认选择商品之前的类别id
					var option2stSelected=product.productCategory.productCategoryId;
					//生成前端的HTML商品类别列表，并默认选择编辑前的商品一级类别
					option2stArr.map(function(item,index){
						var isSelect=option2stSelected===item.productCategoryId?'selected'
								:'';
						option2stHtml+='<option data-value="'
							+item.productCategoryId
							+'"'
							+isSelect
							+'>'
							+item.productCategoryName
							+'</option>';
					});
					$('#product-category-two').html(option2stHtml);
				}
				});
			} 
			});
		}
		});
	}
	/**
	 * F2:商品添加
	 * 获取全部商品类别，将前台控件填充，并将填充后的信息返回给后台
	 */
	function getProductInitInfo(parentId)
	{
		/*$.getJSON(productCateListUrl,function(data){
			if(data.success)
			{
				var tempHtml="";
				data.productCategoryList.map(function(item,index)
				{
					tempHtml+='<option data-value="'+item.productCategoryId+'">'
					+item.productCategoryName+'</option>';
				});
				$('#product-category').html(tempHtml);
			}
		});*/
		//获取一级和二级类别列表返回给前端控制器
		//生成商品一级类别列表
		$.getJSON
		(get1stProCateUrl,function(data){
		if(data.success)
		{  
				var tempHtml=""; 
				var optionArr=data.productCategory1stList;
				//若不是第一次显示一级和二级类别列表则选择该类别  
				if(parentId!='')
				{ 
					// var optionSelected=product.productCategory.productCategoryId;
					//默认选择商品之前的一级类别id
					var optionSelected=parentId;
					//生成前端的HTML商品类别列表，并默认选择该指定商品一级类别
					optionArr.map(function(item,index){
					var isSelect=optionSelected===item.productCategoryId?'selected'
								:'';
					tempHtml+='<option data-value="'
						+item.productCategoryId
							+'"'
							+isSelect
							+'>'
							+item.productCategoryName
							+'</option>';
					});
					parent=
					{
						productCategoryId:parentId
					}  
					$('#product-category-one').html(tempHtml); 
				}
				else
				{
					//若是第一次显示则给其parentId赋初值并显示全部类别
					optionArr.map(function(item,index){
						tempHtml+='<option data-value="'+item.productCategoryId+'">'
							+item.productCategoryName+'</option>';
					});
					$('#product-category-one').html(tempHtml); 
					parent=
					{
							productCategoryId:$('#product-category-one').find('option').not(function(){
								return !this.selected;
							}).data('value')
					};
				}
			
		 
			//否则直接使用从前端传过来的parentId
			var productCate2st=get2stProCateUrl+'?parentId='+parent.productCategoryId;
			//生成该一级类别下的二级类别列表
			$.getJSON
			(productCate2st,function(data){
			if(data.success)
			{//生成二级商品列表
				var temp2stHtml="";
				data.productCategory2stList.map(function(item,index)
				{
					temp2stHtml+='<option data-value="'+item.productCategoryId+'">'
					+item.productCategoryName+'</option>'; 
				}); 
				$('#product-category-two').html(temp2stHtml);
			}
			});
		}
		});
	}
	//针对商品详情图空间组，若该空间组的最后一个元素发生变化（即上传了图片），
	//且空间总数未达到6个，则生成新的文件上传控件
	$('.detail-img-div').on('change','.detail-img:last-child',function(){
		if($('.detail-img').length<6)
		{
			$('#detail-img').append('<input type="file" class="detail-img">');
		}
	});
	//当以及类别标签按钮product-category-one发生变化时，调用getProductInfo(productId)这个函数
	$('#product-category-one').on('change', function() {
		var parent='';
		//加上父类Id
		parent=
		{
		  productCategoryId:$('#product-category-one').find('option').not(function(){
			  return !this.selected;
		  }).data('value')
		};
		getProductInitInfo(parent.productCategoryId);
	});
	 
	//点击提交时，获取表单内容 
	$('#submit').click(function()
	{
		//从前台获取表单信息
		var product={};
		if(isEdit)
		{
			product.productId=productId;
		}
//		product.productName=$('#product-name').val();
		var productName=$('#product-name').val();
		if(productName!=null&&productName!='')
		{
			if(productName.toString().length>0&&productName.toString().length<=16)
			{
				product.productName=productName;	
			}
			else
			{
				$.toast("商品名称长度应控制在16个字符之内！");
				return;
			}
		}
		else
		{
			$.toast("请填写商品名称！");
			return;
		}
		product.productDesc=$('#product-desc').val();
		product.productCategory=
			{
			  productCategoryId:$('#product-category-two').find('option').not(function(){
				  return !this.selected;
			  }).data('value')
			};
		product.priority=$('#priority').val();
//		product.productPrice=$('#product-price').val();
		var productPrice=$('#product-price').val();
		if(productPrice!=null&&productPrice!='')
		{
			product.productPrice=$('#product-price').val();
		}
		else
		{
			$.toast("请填写商品价格！");
			return;
		}
		var thumbnail=$('#product-img')[0].files[0];
		if(!isEdit)
		{
			if(!thumbnail)
			{
				$.toast('请上传图片！');
				return;
			}
		}
		var formData=new FormData();
		formData.append('thumbnail',thumbnail);
		//遍历商品详情图控件，获取里面的文件流
		$('.detail-img').map(
			function(index,item)
			{
				//判断该空间是否已选择了文件
				if($('.detail-img')[index].files.length>0)
				{
					//将第i个文件流赋值给key为productImgi的表单键值对里
					formData.append('productImg'+index,$('.detail-img')[index].files[0]);
				}
			});
		
		//将获取到的信息传入后台
		//点击提交按钮时，将验证码传入后台
		var verifyCodeActual=$('#j_kaptcha').val();
		//如果验证码为空
		if(!verifyCodeActual)
		{
			$.toast('请输入验证码！');
			return;
		}
		//不为空
		formData.append('verifyCodeActual',verifyCodeActual);
		formData.append('productStr',JSON.stringify(product));
		 //将数据提交至后台处理相关操作
		$.ajax({
			url:(isEdit?editProductUrl:addProductUrl),
			type:'POST',
			data:formData,
			contentType:false,
			processData:false,
			cache:false,
			success:function(data)
			{
				if(data.success)
				{
					$.toast('提交成功！');
					$('#kaptcha_img').click();
				}
				else
				{
					$.toast('提交失败！');
					$('#kaptcha_img').click();
				}
				
			}
		});
	});
});