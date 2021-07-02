$(function() {
	initializePage(); 
});
function listOrderManagementInfo() {
	$.ajax({
		url : "/buy/superadmin/listorders",
		type : "post",
		dataType : 'json',
		success : function(data) {
			//loadData加载本地数据，旧的行会被移除
			$('#orderManagementTable').datagrid('loadData', data);
		}
	});
}
function initializePage() {
	// 加载表格数据
	ajaxTable(); 
}
/** --------------table------------------* */
/**
 * 加载表格数据
 */
function ajaxTable() {
	// 加载表格
	$('#orderManagementTable')
			.datagrid(
					{   
						toolbar : 
							[{// 正上方工具栏
							text:'查询条件'
						}], 
						pageNumber : 1,
						//当从远程站点加载数据时，显示的提示信息。
						loadMsg : '数据加载中,请稍后...',
						pageSize:5,
					    pageList:[5,10,20,30], //设置每页显示多少条
					    fitColumns:true,
						//加载远程数据发生某些错误时触发。
					    nowrap:false,
						onLoadError : function() {
							alert('数据加载失败!');
						},
						//当请求远程数据时，发送的额外参数
						queryParams : {// 查询条件
							
						},
						//当用户点击一行时触发，参数包括：rowIndex：被点击行的索引，从 0 开始,rowData：被点击行对应的记录。
						onClickRow : function(rowIndex, rowData) {
							// 取消选择某行后高亮
							$('#orderManagementTable').datagrid('unselectRow',
									rowIndex);
						},
						//getData返回加载的数据
						onLoadSuccess : function() {
							var value = $('#orderManagementTable').datagrid(
									'getData')['errorMsg'];
							if (value != null) {
								alert("错误消息:" + value);
							}
						}
					}).datagrid('acceptChanges');
	var orderTypeBuffer = new StringBuffer()
	orderTypeBuffer
			.append('<select id="orderManagementFilter_orderType" class="easyui-combobox combox" style="margin :2px; padding :4px;" ');
	orderTypeBuffer
			.append('onchange="changeOrderType(this.options[this.options.selectedIndex].value)">');
	orderTypeBuffer
			.append('<option id="orderManagementFilter_ALL" value="">全部订单</option>');
	orderTypeBuffer
			.append('<option id="orderManagementFilter_BUY" value="1">买方订单</option>');
	orderTypeBuffer
			.append('<option id="productManagementFilter_SELLER" value="2">卖方订单</option></select>'); 
	var orderStatusBuffer = new StringBuffer()
	orderStatusBuffer
			.append('<select id="orderManagementFilter_orderStatus" class="easyui-combobox combox" style="margin :2px; padding :4px;" ');
	orderStatusBuffer
			.append('onchange="changeOrderStatus(this.options[this.options.selectedIndex].value)">');
	orderStatusBuffer
			.append('<option id="orderManagementStatus_ALL" value="">全部交易</option>');
	orderStatusBuffer
			.append('<option id="orderManagementStatus_PENDING" value="0">待付款交易</option>');
	orderStatusBuffer
			.append('<option id="productManagementStatus_PAID" value="1">已付款交易</option>'); 
	orderStatusBuffer
			.append('<option id="productManagementStatus_COMPLETED" value="2">已完成交易</option>'); 
	orderStatusBuffer
			.append('<option id="productManagementStatus_FAILED" value="3">失败交易</option></select>'); 
	$('.datagrid-toolbar').append(orderTypeBuffer.toString());
	$('.datagrid-toolbar').append(orderStatusBuffer.toString());
	//提交自从被加载以来或最后一次调用acceptChanges以来所有更改的数据
	// 获取DataGrid分页组件对象
	var p = $("#orderManagementTable").datagrid("getPager");
	// 设置分页组件参数
	$(p).pagination({
		onSelectPage : function(pageNumber, pageSize) {
			getDataByPageRows(pageNumber, pageSize);
		}
	});
	searchOrderInfo();
}
function changeOrderType(orderType)
{
	$('#orderTypeHd').val(orderType);
	searchOrderInfo();
}
function changeOrderStatus(orderStatus)
{
	$('#orderStatusHd').val(orderStatus);
	searchOrderInfo();
}
function searchOrderInfo() {
	var orderType = $("#orderTypeHd").val(); 
	var orderStatus = $("#orderStatusHd").val(); 
	if(orderType=="ALL"&&orderStatus=="ALL")
	{
		listOrderByTime();
	}
	else
	{
		listOrderByCondition();
	}
}
function listOrderByTime() { 
	var urlBuffer = new StringBuffer();
	urlBuffer.append("/buy/superadmin/listorders?enableStatus="); 
	urlBuffer.append(1);
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#orderManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}
function listOrderByCondition() { 
	var orderType = $("#orderTypeHd").val(); 
	var orderStatus = $("#orderStatusHd").val(); 
	var urlBuffer = new StringBuffer();
	urlBuffer.append("/buy/superadmin/listorders?orderType=");
	urlBuffer.append(orderType);
	urlBuffer.append("&orderStatus=");
	urlBuffer.append(orderStatus);
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#orderManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
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
			$('#orderManagementTable').datagrid('loadData', jsonObj);
		}
	});// ajax
}
function userFormatter(value,row,index)
{
	var user=row.user;
	return user.userId+"("+user.userName+")";
}
function productFormatter(value,row,index)
{
	var product=row.product;
	return product.productId+"("+product.productName+")";
}
function orderTypeFormater(value,row,index)
{
	var orderType=row.orderType;
	if(orderType==1)
	{
		return orderType+"("+'我买到的'+")";
	}
	else
	{
		return orderType+"("+'我卖出的'+")";
	}
}
function orderStatusFormater(value,row,index)
{
	var orderStatus=row.orderStatus;
	if(orderStatus==0)
	{
		return orderStatus+"("+'待付款'+")";
	}
	else if(orderStatus==1)
	{
		return orderStatus+"("+'已付款'+")";
	}
	else if(orderStatus==2)
	{
		return orderStatus+"("+'已完成'+")";
	}
	else if(orderStatus==3)
	{
		return orderStatus+"("+'交易失败'+")";
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
function commentTimeFormater(value,row,index)
{
	var commentTime=row.commentTime;
	return new Date(commentTime).Format("yyyy-MM-dd hh:mm:ss");
}
 
 
 
 

 

 
 
