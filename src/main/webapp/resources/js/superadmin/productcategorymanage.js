$(function() {
	initializePage();
	listProductCategoryManagementInfo();
});
function initializePage() {
	// 加载表格数据
	ajaxTable();

	// 初始化弹出层
	setDialog_add();
	closeDialog_add();
	setDialog_edit();
	closeDialog_edit();

	$.ajax({
		url : "/buy/superadmin/list1stlevelproductcategorys",
		type : "post",
		dataType : 'json',
		success : function(data) {
			var productCategoryList = data.list;
			if ((productCategoryList != undefined)
					&& (productCategoryList.length > 0)) {
				var productCategoryBuffer = new StringBuffer();
				/**
				 * <option value="productCategoryId">productCategoryName</option>
				 */
				for (var i = 0; i < productCategoryList.length; i++) {
					productCategoryBuffer.append('<option value="');
					productCategoryBuffer
							.append(productCategoryList[i].productCategoryId);
					productCategoryBuffer.append('">');
					productCategoryBuffer
							.append(productCategoryList[i].productCategoryName);
					productCategoryBuffer.append('</option>');
				}
				productCategoryBuffer.append('<option value=""></option>');
				$("#productCategoryManagementAdd_parentId").append(
						productCategoryBuffer.toString());
				$("#productCategoryManagementEdit_parentId").append(
						productCategoryBuffer.toString());
				$("#productCategoryManagementAdd_parentId").val("");
				list1stProductCategorys(productCategoryList);
			}
		}
	});
}
function list1stProductCategorys(productCategoryList) {
	if ((productCategoryList != undefined) && (productCategoryList.length > 0)) {
		var product1stCategoryBuffer = new StringBuffer();
		/**
		 * <option value="categoryId">categoryName</option>
		 */
		for (var i = 0; i < productCategoryList.length; i++) {
			product1stCategoryBuffer.append('<option value="');
			product1stCategoryBuffer.append(productCategoryList[i].productCategoryId);
			product1stCategoryBuffer.append('">');
			product1stCategoryBuffer.append(productCategoryList[i].productCategoryName);
			product1stCategoryBuffer.append('</option>');
		}
		$("#productCateManagementFilter_productCategory").append(
				product1stCategoryBuffer.toString());
		/*$("#productCateManagementEdit_productCategory").append(
				productCategoryBuffer.toString());*/
	}
}
function listProductCategoryManagementInfo() 
{
	var productCategoryId=$("#productCategoryIdHd").val();
	if(productCategoryId=="ALL")
	{
		listAllCategories();
	}
	else
	{
		listCategoryById();
	}
	/*$.ajax({
		url : "/buy/superadmin/listallproductcategorys",
		type : "post",
		dataType : 'json',
		success : function(data) {
			$('#productCategoryManagementTable').datagrid('loadData', data);
		}
	});*/
	
} 
function listAllCategories()
{
	/*var enalbeStatus = $("#enalbeStatusHd").val();*/
	var urlBuffer = new StringBuffer();
	urlBuffer.append("/buy/superadmin/listallproductcategorys?enableStatus=1"); 
	/*urlBuffer.append(enalbeStatus);*/
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#productCategoryManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}
function listCategoryById()
{
	var productCategoryId=$("#productCategoryIdHd").val();
	/*if(productCategoryId=="")
	{
		return;
	}*/
	var urlBuffer = new StringBuffer();
	urlBuffer.append("/buy/superadmin/listproductcategorys?productCategoryId=");
	urlBuffer.append(productCategoryId);
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#productCategoryManagementTable").datagrid("getPager").pagination(
	"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}
/** --------------table------------------* */
/**
 * 加载表格数据
 */
function ajaxTable() {
	// 加载表格
	//list1stlevelproductcategorys
	$('#productCategoryManagementTable').datagrid(
			{
				toolbar : [ {// 正上方工具栏
					text : '添加新类别',
					iconCls : 'icon-add',
					handler : function() {
						// 点击工具栏运行的js方法
						openDialog_add();
					}
				}, '-', {
					text : '批量删除',
					iconCls : 'icon-cancel',
					handler : function() {
						batch('delete');
					}
				} ],
				pageNumber : 1,
				pageSize:5,
				loadMsg : '数据加载中,请稍后...',
				pageList : [5,10, 20, 30 ], // 设置每页显示多少条
				fitColumns:true, 
				nowarp:false, 
				onLoadError : function() {
					alert('数据加载失败!');
				},
				queryParams : {// 查询条件
				},
				onClickRow : function(rowIndex, rowData) {
					// 取消选择某行后高亮
					$('#productCategoryManagementTable').datagrid('unselectRow',
							rowIndex);
				},
				onLoadSuccess : function() {
					var value = $('#productCategoryManagementTable').datagrid(
							'getData')['errorMsg'];
					if (value != null) {
						alert("错误消息:" + value);
					}
				}
			}).datagrid('acceptChanges');
	var productCategoryBuffer = new StringBuffer()
	productCategoryBuffer
	.append('<select id="productCateManagementFilter_productCategory" class="easyui-combobox combox" style="margin :2px; padding :4px;" ');
	productCategoryBuffer
	.append('onchange="changeFilterProductCategory(this.options[this.options.selectedIndex].value)">');
	productCategoryBuffer
	.append('<option id="productCateManagementFilter_ALLCATE" value="ALL">全部类别</option></select>');
	$('.datagrid-toolbar').append(productCategoryBuffer.toString());
	// 获取DataGrid分页组件对象
	var p = $("#productCategoryManagementTable").datagrid("getPager");
	// 设置分页组件参数
 	$(p).pagination({
		onSelectPage : function(pageNumber, pageSize) {
			getDataByPageRows(pageNumber, pageSize);
		}
	}); 
}
function getDataByPageRows(pageNum, rowsLimit) {
	pageNum = pageNum || 1; // 设置默认的页号
	rowsLimit = rowsLimit || 5;// 设置默认的每页记录数
	var urlBuffer = new StringBuffer();
	urlBuffer.append($("#urlHd").val());
	urlBuffer.append("&page=");
	urlBuffer.append(pageNum);
	urlBuffer.append("&rows=");
	urlBuffer.append(rowsLimit);
	$.ajax({
		contentType : "application/x-www-form-urlencoded; charset=utf-8",
		type : "post",
		dataType : 'json', // 注意格式是html，不是json
		url : urlBuffer.toString(),
		error : function() { // ajax请求失败
			$.messager.show({
				title : '失败信息',
				msg : '加载内容失败',
				timeout : 0,
				showType : 'fade'
			});
		},
		success : function(jsonObj, textStatus, jqXHR) { // 请求成功，将返回的数据（一页的记录数）绑定到
			// datagrid控件
			$('#productCategoryManagementTable').datagrid('loadData', jsonObj);
		}
	});// ajax
}
function changeFilterProductCategory(productCategoryId) {
	$('#productCategoryIdHd').val(productCategoryId);
	listProductCategoryManagementInfo();
}
function parentFormater(value, row, index) 
{
	var parent=row.parent;
	if(parent!=null)
		return  parent.productCategoryId+"("+parent.productCategoryName+")";
	else
		return;
}
function imgFormater(value, row, index) {
	var productCategoryImg = row.productCategoryImg;
	return '<img src="' + productCategoryImg + '" width="100px" height="60px">';
}
function createTimeFormater(value,row,index)
{
	var createTime=row.createTime;
	return new Date(createTime).Format("yyyy-MM-dd hh:mm:ss");
}
function lastEditTimeFormater(value,row,index)
{
	var lastEditTime=row.lastEditTime;
	return new Date(lastEditTime).Format("yyyy-MM-dd hh:mm:ss");
}
/**
 * 设置操作列的信息 参数说明 value 这个可以不管，但是要使用后面 row 和index 这个参数是必须的 row 当前行的数据 index
 * 当前行的索引 从0 开始
 */
function optFormater(value, row, index) {
	var productCategoryId = row.productCategoryId;
	var productCategoryName = row.productCategoryName;
	var productCategoryDesc = row.productCategoryDesc;
	if(row.parent!=null)
	{
		var parentId =row.parent.productCategoryId /*parentFormater(value, row, index)*/;
	}
	var priority = row.priority;
	var params = productCategoryId + ",'" + productCategoryName + "','"
			+ productCategoryDesc + "'," + parentId + "," + priority;
	var edit = '<a href="javascript:openDialog_edit(' + params + ')">编辑</a> | ';
	var del = '<a href="#" onclick="doDel(' + productCategoryId + ')">删除</a>';
	return edit + del;
};

/** --------------添加操作弹出框------------------* */
// 设置弹出框的属性
function setDialog_add() {
	$('#productCategoryManagementAdd').dialog({
		title : '类别添加',
		modal : true, // 模式窗口：窗口背景不可操作
		collapsible : true, // 可折叠，点击窗口右上角折叠图标将内容折叠起来
		resizable : true, // 可拖动边框大小
		onClose : function() { // 继承自panel的关闭事件
			productCategoryManagementAddReset();
		}
	});
}
// 打开对话框
function openDialog_add() {
	$('#productCategoryManagementAdd').dialog('open');
}
// 关闭对话框
function closeDialog_add() {
	$('#productCategoryManagementAdd').dialog('close');
}
// 执行用户添加操作
function productCategoryManagementAdd() {
	var validateResult = true;
	// easyui 表单验证
	$('#table_productCategoryManagementAdd input').each(function() {
		if ($(this).attr('required') || $(this).attr('validType')) {
			if (!$(this).validatebox('isValid')) {
				// 如果验证不通过，则返回false
				validateResult = false;
				return;
			}
		}
	});
	if (validateResult == false) {
		return;
	}
	var pic = $("#productCategoryManagementAdd_productCategoryImg").val();
	if (pic.length <= 0) {
		$("#productCategoryManagementAdd_message").html("请上传图片！");
		return;
	}
	var productCategory = {};
	productCategory.productCategoryName = encodeURIComponent($(
			"#productCategoryManagementAdd_productCategoryName").val());
	productCategory.productCategoryDesc = encodeURIComponent($(
			"#productCategoryManagementAdd_productCategoryDesc").val());
	
	productCategory.parent= {
			productCategoryId:encodeURIComponent($("#productCategoryManagementAdd_parentId").val())
			};
	productCategory.priority = encodeURIComponent($("#productCategoryManagementAdd_priority").val());
	var options = {
		type : 'post',
		data : {
			productCategoryStr : JSON.stringify(productCategory),
			thumbnail:pic
		},
		url : '/buy/superadmin/addproductcategory',// 请求的action路径
		error : function(data) {// 请求失败处理函数
			alert(data);
		},
		success : function() {
			var messgage = "添加成功!";
			listProductCategoryManagementInfo();
			$("#productCategoryManagementAdd_message").html(messgage);
		}
	};
	$("#productCategoryFormAdd").ajaxSubmit(options);
}
// 用户添加页面数据重置操作
function productCategoryManagementAddReset() {
	$("#productCategoryManagementAdd_message").html("");
	$("#productCategoryManagementAdd_productCategoryName").val("");
	$("#productCategoryManagementAdd_productCategoryDesc").val("");
	$("#productCategoryManagementAdd_parentId").val("");
	$("#productCategoryManagementAdd_priority").val("");
	var file = $("#productCategoryManagementAdd_productCategoryImg");
	file.after(file.clone().val(""));
	file.remove();
}

/** --------------编辑操作弹出框------------------* */
// 设置弹出框的属性
function setDialog_edit() {
	$('#productCategoryManagementEdit').dialog({
		title : '类别编辑',
		modal : true, // 模式窗口：窗口背景不可操作
		collapsible : true, // 可折叠，点击窗口右上角折叠图标将内容折叠起来
		resizable : true
	// 可拖动边框大小
	});
}
// 打开对话框
function openDialog_edit(productCategoryId, productCategoryName, productCategoryDesc,
		parentId, priority) {
	productCategoryManagementEditReset(productCategoryId, productCategoryName,
			productCategoryDesc, parentId, priority);
	$('#productCategoryManagementEdit').dialog('open');
}
// 关闭对话框
function closeDialog_edit() {
	$('#productCategoryManagementEdit').dialog('close');
}
// 根据用户id查询用户的信息
function productCategoryManagementEditReset(productCategoryId, productCategoryName,
		productCategoryDesc, parentId, priority) {
	$("#productCategoryManagementEdit_message").html("");
	$("#productCategoryManagementEdit_productCategoryId").val(productCategoryId);
	$("#productCategoryManagementEdit_productCategoryName").val(productCategoryName);
	$("#productCategoryManagementEdit_productCategoryDesc").val(productCategoryDesc);
	$("#productCategoryManagementEdit_parentId").val(parentId);
	$("#productCategoryManagementEdit_priority").val(priority);
	var file = $("#productCategoryManagementEdit_productCategoryImg");
	file.after(file.clone().val(""));
	file.remove();
}

// 执行用户编辑操作
function productCategoryManagementEdit() {
	var validateResult = true;
	// easyui 表单验证
	$('#table_productCategoryManagementEdit input').each(function() {
		if ($(this).attr('required') || $(this).attr('validType')) {
			if (!$(this).validatebox('isValid')) {
				// 如果验证不通过，则返回false
				validateResult = false;
				return;
			}
		}
	});
	if (validateResult == false) {
		return;
	}
	var pic = $("#productCategoryManagementEdit_productCategoryImg").val(); 
	var productCategory = {};
	productCategory.productCategoryId = $(
			"#productCategoryManagementEdit_productCategoryId").val();
	productCategory.productCategoryName = encodeURIComponent($(
			"#productCategoryManagementEdit_productCategoryName").val());
	productCategory.productCategoryDesc = encodeURIComponent($(
			"#productCategoryManagementEdit_productCategoryDesc").val());
	productCategory.parent= {
			productCategoryId:encodeURIComponent($("#productCategoryManagementEdit_parentId").val())
			};
	productCategory.priority = encodeURIComponent($("#productCategoryManagementEdit_priority").val());
	var options = {
		type : 'post',
		data : {
			productCategoryStr : JSON.stringify(productCategory),
			thumbnail : pic
		},
		url : '/buy/superadmin/modifyproductcategory',// 请求的action路径
		error : function() {// 请求失败处理函数
			alert('请求失败');
		},
		success : function() {
			var messgage = "修改成功!";
			listProductCategoryManagementInfo();
			$("#productCategoryManagementEdit_message").html(messgage);
		}
	};
	$("#productCategoryFormEdit").ajaxSubmit(options);
}

/** --------------执行删除操作------------------* */
function doDel(productCategoryId) {
	$.messager.confirm('删除提示', '你确定永久删除该数据吗?', function(r) {
		if (r) {
			var url = '/buy/superadmin/removeproductcategory?productCategoryId=' + productCategoryId;
			changeStatus(url);
		}
	});
}
/**
 * 修改状态的Ajax
 * 
 * @param url
 * @return
 */
function changeStatus(url) {
	$.ajax({
		async : false,
		cache : false,
		type : 'post',
		dataType : "json",
		url : url,// 请求的action路径
		error : function() {// 请求失败处理函数
			alert('请求失败');
		},
		success : function() {
			alert("操作成功");
			listProductCategoryManagementInfo();
		}
	});
}

/**
 * 批量操作
 * 
 * @return
 */
function batch(flag) {
	if ($('#productCategoryManagementTable').datagrid('getSelected')) {
		// 首先如果用户选择了数据，则获取选择的数据集合
		var productCategoryNames = [];
		var selectedRow = $('#productCategoryManagementTable').datagrid(
				'getSelections');
		var jsonList = [];
		for (var i = 0; i < selectedRow.length; i++) {
			jsonList.push(selectedRow[i].productCategoryId);
			productCategoryNames.push(selectedRow[i].productCategoryName);
		}

		// 删除操作
		$.messager.confirm('删除提示', '你确定永久删除下列消息吗?<br/>'
				+ productCategoryNames.join(','), function(r) {
			if (r) {
				var url = '/buy/superadmin/removeproductcategories';
				$.ajax({
					async : false,
					cache : false,
					type : 'post',
					dataType : "json",
					data:{productCategoryIdListStr:JSON.stringify(jsonList)},
					url : url,// 请求的action路径
					error : function() {// 请求失败处理函数
						alert('请求失败');
					},
					success : function() {
						alert("操作成功");
						listProductCategoryManagementInfo();
					}
				});
			}
		});
	}
}
