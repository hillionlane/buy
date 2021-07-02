$(function() {
	initializePage();
});

function initializePage() {
	// 加载表格数据
	ajaxTable();
	// 初始化弹出层
 	setDialog_edit();
 	closeDialog_edit();
	intializeDynamicData();
}

function intializeDynamicData() {
	$.ajax({
		url : "/buy/superadmin/listproductcategorys",
		type : "post",
		dataType : 'json',
		success : function(data) {
			var productCategoryList = data.rows;
			listProductCategorys(productCategoryList);
			searchProductInfo();
		}
	});
}

function listProductCategorys(productCategoryList) {
	if ((productCategoryList != undefined) && (productCategoryList.length > 0)) {
		var productCategoryBuffer = new StringBuffer();
		/**
		 * <option value="categoryId">categoryName</option>
		 */
		for (var i = 0; i < productCategoryList.length; i++) {
			productCategoryBuffer.append('<option value="');
			productCategoryBuffer.append(productCategoryList[i].productCategoryId);
			productCategoryBuffer.append('">');
			productCategoryBuffer.append(productCategoryList[i].productCategoryName);
			productCategoryBuffer.append('</option>');
		}
		$("#productManagementFilter_productCategory").append(
				productCategoryBuffer.toString());
		$("#productManagementEdit_productCategory").append(
				productCategoryBuffer.toString());
	}
}

function searchProductInfo() {
	var enalbeStatus = $("#enalbeStatusHd").val();
	var productCategoryId = $("#productCategoryIdHd").val();
	var searchCondition = $("#searchConditionHd").val();
	if (searchCondition == "byProductName") {
		listProductByName();
	} else if (searchCondition == "byProductId") {
		listProductByProductId();
	}else if(searchCondition == "byUserId")
	{
		listProductByUserId();
	}
	else if(searchCondition == "byAreaId")
	{
		listProductByAreaId();
	}
	else 
	{
		if (productCategoryId == "ALL") {
			listProductsByPriority();
		} else {
			listProductsByProductCategory();
		}
	}
}

function listProductByName() {
	var productName = $("#searchInfoHd").val();
	if (productName == "") {
		return;
	}
	var urlBuffer = new StringBuffer();
	urlBuffer.append("listproducts?productName=");
	urlBuffer.append(encodeURIComponent(encodeURIComponent(productName)));
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#productManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}

function listProductByProductId() {
	var productId = $("#searchInfoHd").val();
	if (productId == "") {
		return;
	}
	var urlBuffer = new StringBuffer();
	urlBuffer.append("searchproductbyid?productId=");
	urlBuffer.append(productId);
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#productManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}
function listProductByUserId() {
	var userId = $("#searchInfoHd").val();
	if (userId == "") {
		return;
	}
	var urlBuffer = new StringBuffer();
	urlBuffer.append("listproducts?userId=");
	urlBuffer.append(userId);
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#productManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}
function listProductByAreaId() {
	var areaId = $("#searchInfoHd").val();
	if (areaId == "") {
		return;
	}
	var urlBuffer = new StringBuffer();
	urlBuffer.append("listproducts?areaId=");
	urlBuffer.append(areaId);
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#productManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}
function listProductsByPriority() {
	var enalbeStatus = $("#enalbeStatusHd").val();
	var urlBuffer = new StringBuffer();
	urlBuffer.append("/buy/superadmin/listproducts?enableStatus=");
	urlBuffer.append(enalbeStatus);
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#productManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}

function listProductsByProductCategory() {
	var productCategoryId = $("#productCategoryIdHd").val();
	var enalbeStatus = $("#enalbeStatusHd").val();
	var urlBuffer = new StringBuffer();
	urlBuffer.append("/buy/superadmin/listproducts?productCategoryId=");
	urlBuffer.append(productCategoryId);
	urlBuffer.append("&enableStatus=");
	urlBuffer.append(enalbeStatus);
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#productManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}

/** --------------table------------------* */
/**
 * 加载表格数据
 */
function ajaxTable() {
	// 加载表格
	$('#productManagementTable')
			.datagrid(
					{
						toolbar : [ {
							text : '查询条件'
						} ],
						pageNumber : 1,
						pageSize:5,
						loadMsg : '数据加载中,请稍后...',
						pageList : [5, 10, 20, 30 ], // 设置每页显示多少条
						onLoadError : function() {
							alert('数据加载失败!');
						}, 
						fitColumns:true,
						nowrap:false,
						queryParams : {// 查询条件
						},
						onClickRow : function(rowIndex, rowData) {
							// 取消选择某行后高亮
							$('#productManagementTable').datagrid('unselectRow',
									rowIndex);
						},
						onLoadSuccess : function() {
							var value = $('#productManagementTable').datagrid(
									'getData')['errorMsg'];
							if (value != null) {
								alert("错误消息:" + value);
							}
						}
					}).datagrid('acceptChanges');
	
	var enableStatusBuffer = new StringBuffer()
	enableStatusBuffer
			.append('<select id="productManagementFilter_enableStatus" class="easyui-combobox combox" style="margin :2px; padding :4px;" ');
	enableStatusBuffer
			.append('onchange="changeFilterStatus(this.options[this.options.selectedIndex].value)">');
	enableStatusBuffer
			.append('<option id="productManagementFilter_ALL"   value="">全部</option>');
	enableStatusBuffer
			.append('<option id="productManagementFilter_YES"   value="1">启用</option>');
	enableStatusBuffer
			.append('<option id="productManagementFilter_APPLY"   value="3">已出售</option>');
	enableStatusBuffer
			.append('<option id="productManagementFilter_NO"   value="0">禁用</option></select>');
	var productCategoryBuffer = new StringBuffer()
	productCategoryBuffer
			.append('<select id="productManagementFilter_productCategory" class="easyui-combobox combox" style="margin :2px; padding :4px;" ');
	productCategoryBuffer
			.append('onchange="changeFilterProductCategory(this.options[this.options.selectedIndex].value)">');
	productCategoryBuffer
			.append('<option id="productManagementFilter_ALLCATE" value="ALL">全部类别</option></select>');
	var searchConditionBuffer = new StringBuffer()
	searchConditionBuffer
			.append('<select id="productManagementSearch_searchCondition" class="easyui-combobox combox" style="margin :2px; padding :4px;" ');
	searchConditionBuffer
			.append('onchange="changeSearchCondition(this.options[this.options.selectedIndex].value)">');
	searchConditionBuffer
			.append('<option id="productManagementSearch_ALL"   value="ALL">按筛选条件查询</option>');
	searchConditionBuffer
			.append('<option id="productManagementSearch_NAME"   value="byProductName">按商品名称查询</option>');
	searchConditionBuffer
			.append('<option id="productManagementSearch_PRODUCTID"   value="byProductId">按商品ID查询</option>');
	searchConditionBuffer
		.append('<option id="productManagementSearch_USERID"   value="byUserId">按用户ID查询</option>');
	searchConditionBuffer
		.append('<option id="productManagementSearch_AREAID"   value="byAreaId">按地域ID查询</option></select>');
	var searchBoxBuffer = new StringBuffer();
	searchBoxBuffer.append('<input type="text" ');
	searchBoxBuffer
			.append('id="productManagementSearch_searchBox"  onkeydown="if(event.keyCode==13){changeSearchInfo();}"');
	searchBoxBuffer
			.append(' style="resize: none; width: 200px ;height=60px ;font-size: 37px;margin: 2px;" onkeydown="searchInfoKeyDown(e)"></input>');
	searchBoxBuffer.append('<input type="button" id="searchBtn" style="height: 50px;width: 100px; font-size: 20px;" value="搜索"');
	searchBoxBuffer
			.append('style="margin-right: 0.5em;" onclick="searchInfo()"/>');
	$('.datagrid-toolbar').append(enableStatusBuffer.toString());
	$('.datagrid-toolbar').append(productCategoryBuffer.toString());
	$('.datagrid-toolbar').append(searchConditionBuffer.toString());
	$('.datagrid-toolbar').append(searchBoxBuffer.toString());
	$("#productManagementSearch_searchBox").hide();
	$("#searchBtn").hide();
	// 获取DataGrid分页组件对象
	var p = $("#productManagementTable").datagrid("getPager");
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
			$('#productManagementTable').datagrid('loadData', jsonObj);
		}
	});// ajax
}

function changeFilterStatus(status) {
	$('#enalbeStatusHd').val(status);
	searchProductInfo();
}

function changeFilterProductCategory(productCategoryId) {
	$('#productCategoryIdHd').val(productCategoryId);
	searchProductInfo();
}

function changeSearchCondition(condition) {
	$("#searchConditionHd").val(condition);
	if (condition == "byProductName") {
		$("#enalbeStatusHd").val("");
		$("#productCategoryIdHd").val("");
		$("#productManagementFilter_enableStatus").hide();
		$("#productManagementFilter_productCategory").hide();
		$("#productManagementSearch_searchBox").show();
		$("#searchBtn").show();
	} else if (condition == "byProductId") {
		$("#enalbeStatusHd").val("");
		$("#productCategoryIdHd").val("");
		$("#productManagementFilter_enableStatus").hide();
		$("#productManagementFilter_productCategory").hide();
		$("#productManagementSearch_searchBox").show();
		$("#searchBtn").show();
	}else if(condition == "byUserId") {
		$("#enalbeStatusHd").val("");
		$("#productCategoryIdHd").val("");
		$("#productManagementFilter_enableStatus").hide();
		$("#productManagementFilter_productCategory").hide();
		$("#productManagementSearch_searchBox").show();
		$("#searchBtn").show();
	}else if(condition == "byAreaId") {
		$("#enalbeStatusHd").val("");
		$("#productCategoryIdHd").val("");
		$("#productManagementFilter_enableStatus").hide();
		$("#productManagementFilter_productCategory").hide();
		$("#productManagementSearch_searchBox").show();
		$("#searchBtn").show();
	}
	else  {
		$("#enalbeStatusHd").val("ALL");
		$("#productCategoryIdHd").val("ALL");
		$("#searchInfoHd").val("");
		$("#productManagementFilter_enableStatus").show();
		$("#productManagementFilter_productCategory").show();
		$("#productManagementSearch_searchBox").hide();
		$("#searchBtn").hide();
	}
	searchProductInfo();
}

function changeSearchInfo() {
	var info = $('#productManagementSearch_searchBox').val();
	$('#searchInfoHd').val(info);
}

function searchInfoKeyDown(e) {
	if (e.keyCode == 13) {
		searchInfo();
	}
}

function searchInfo() {
	var info = $('#productManagementSearch_searchBox').val().trim();
	$('#searchInfoHd').val(info);
	if (info == "") {
		return;
	}
	searchProductInfo();
}
function productCategoryFormatter(value,row,index)
{
	var productCategory=row.productCategory;
	return productCategory.productCategoryId+"("+productCategory.productCategoryName+")";
}
function areaFormatter(value,row,index)
{
	var area=row.area;
	return area.areaId+"("+area.areaName+")";
}

function userFormatter(value,row,index)
{
	var user=row.personInfo;
	return user.userId+"("+user.userName+")";
}
function enableStatusFormatter(value,row,index)
{
	var enableStatus=row.enableStatus;
	if(enableStatus==0)
	{
		return enableStatus+"("+'禁用'+")"
	}
	else if(enableStatus==1)
	{
		return enableStatus+"("+'启用'+")"
	}
	else if(enableStatus==3)
	{
		return enableStatus+"("+'已出售'+")"
	}
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
	var productName = row.productName;
	var productCategoryId = row.productCategory.productCategoryId;/*productCategoryFormatter(value,row,index);*/
	var productId = row.productId;
	var priority = row.priority; 
	var productDesc=row.productDesc;
	var productPrice=row.productPrice;
	var enableStatus = row.enableStatus;  
	var params = productId + "," + priority + ",'" + enableStatus +"','" +productDesc+"','"+productPrice+"','"
		+ productName + "'," + productCategoryId;
	var edit = '<a href="javascript:openDialog_edit(' + params + ')">编辑</a>';
	return edit;
};

/** --------------编辑操作弹出框------------------* */
function changeEnableStatus4Edit(status) {
	if (status == "0") {
		$("#productManagementEdit_supportProductCategory").val("0");
	} else {
		$("#productManagementEdit_supportProductCategory").val("1");
	}
}
// 设置弹出框的属性
function setDialog_edit() {
	$('#productManagementEdit').dialog({
		title : '商品编辑',
		modal : true, // 模式窗口：窗口背景不可操作
		collapsible : true, // 可折叠，点击窗口右上角折叠图标将内容折叠起来
		resizable : true
	});
}
// 打开对话框
function openDialog_edit(productId, priority, enableStatus, productName,productDesc,productPrice,
		productCategoryId) {
	productManagementEditReset(productId, priority, enableStatus, productName,productDesc,productPrice,
			productCategoryId);
	$('#productManagementEdit').dialog('open');
}
// 关闭对话框
function closeDialog_edit() {
	$('#productManagementEdit').dialog('close');
} 
 // 根据用户id查询用户的信息
function productManagementEditReset(productId, priority, enableStatus,productDesc,productPrice,
		productName, productCategoryId) {
	$("#productManagementEdit_message").html("");
	$("#productManagementEdit_productName").val(productName);
	$("#productManagementEdit_productId").val(productId);
	$("#productManagementEdit_priority").val(priority);
	$("#productManagementEdit_enableStatus").val(enableStatus);
	$("#productManagementEdit_productDesc").val(productDesc);
	$("#productManagementEdit_productPrice").val(productPrice);
	$("#productManagementEdit_productCategory").val(productCategoryId);
} 
// 执行用户编辑操作
function productManagementEdit() {
	var validateResult = true;
	// easyui 表单验证
	$('#table_productManagementEdit input').each(function() {
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
	var product = {};
	product.productId = encodeURIComponent($("#productManagementEdit_productId").val());
	product.productName = encodeURIComponent($("#productManagementEdit_productName").val());
	product.productCategoryId = encodeURIComponent($(
			"#productManagementEdit_productCategory").val());
	product.productCategory = {
	productCategoryId:encodeURIComponent($("#productManagementEdit_productCategory").val())};
	product.productDesc=encodeURIComponent($("#productManagementEdit_productDesc").val());
	product.productPrice=encodeURIComponent($("#productManagementEdit_productPrice").val());
	product.priority = encodeURIComponent($("#productManagementEdit_priority").val());
	product.enableStatus = encodeURIComponent($("#productManagementEdit_enableStatus")
			.val());
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		dataType : "json",
		data : {
			productStr : JSON.stringify(product)
		},
		url : '/buy/superadmin/modifyproduct',// 请求的action路径
		success : function(data) 
		{
			if(data.success)
			{
				var messgage = "修改成功!";
				searchProductInfo();
				$("#productManagementEdit_message").html(messgage);
			}
			else
			{
				alert(data.errMsg);
				var messgage = "修改失败!";
				$("#productManagementEdit_message").html(messgage);
			}
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
			searchProductInfo();
		}
	});
}
