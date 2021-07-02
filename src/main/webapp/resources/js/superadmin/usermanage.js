$(function() {
	initializePage();
});

function initializePage() {
	// 加载表格数据
	ajaxTable();
	// 初始化弹出层
	setDialog_edit();
	closeDialog_edit();
	searchAccountInfo()
}

function searchAccountInfo() {
	var userName = $("#searchInfoHd").val();
	if (userName == "") {
		listUsers();
	} else {
		listUsersByUserName();
	}
}

function listUsers() {
	var enalbeStatus = $("#enalbeStatusHd").val();
	var urlBuffer = new StringBuffer();
	urlBuffer.append("/buy/superadmin/listpersonInfos?enableStatus=");
	urlBuffer.append(enalbeStatus);
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#userManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}

function listUsersByUserName() {
	var userName = $("#searchInfoHd").val();
	if (userName == "") {
		return;
	}
	var enalbeStatus = $("#enalbeStatusHd").val();
	var urlBuffer = new StringBuffer();
	urlBuffer.append("/buy/superadmin/listpersonInfos?enableStatus=");
	urlBuffer.append(enalbeStatus);
	urlBuffer.append("&userName=");
	urlBuffer.append(userName);
	$('#urlHd').val(urlBuffer.toString());
	var obj = $("#userManagementTable").datagrid("getPager").pagination(
			"options");
	getDataByPageRows(obj.pageNumber, obj.pageSize);
}

/** --------------table------------------* */
/**
 * 加载表格数据
 */
function ajaxTable() {
	// 加载表格
	$('#userManagementTable').datagrid(
			{
				toolbar : [ {
					text : '操作列表'
				} ],
				pageNumber : 1,
				loadMsg : '数据加载中,请稍后...',
				pageSize:5,
				pageList : [5,10, 20,30 ], // 设置每页显示多少条
				onLoadError : function() {
					alert('数据加载失败!');
				},
				queryParams : {// 查询条件
				},
				fitColumns:true,
				nowrap:false,
				onClickRow : function(rowIndex, rowData) {
					// 取消选择某行后高亮
					$('#userManagementTable').datagrid('unselectRow',
							rowIndex);
				},
				onLoadSuccess : function() {
					var value = $('#userManagementTable').datagrid(
							'getData')['errorMsg'];
					if (value != null) {
						alert("错误消息:" + value);
					}
				}
			}).datagrid('acceptChanges');
	var enableStatusBuffer = new StringBuffer()
	enableStatusBuffer
			.append('<select id="userManagementFilter_enableStatus" class="easyui-combobox combox" style="margin :2px; padding :4px;" ');
	enableStatusBuffer
			.append('onchange="changeFilterStatus(this.options[this.options.selectedIndex].value)">');
	enableStatusBuffer
			.append('<option id="userManagementFilter_ALL" class="option" value="">全部</option>');
	enableStatusBuffer
			.append('<option id="userManagementFilter_YES" class="option" value="1">启用</option>');
	enableStatusBuffer
			.append('<option id="userManagementFilter_NO" class="option" value="0">禁用</option></select>');
	var searchBoxBuffer = new StringBuffer();
	searchBoxBuffer.append('<input type="text" ');
	searchBoxBuffer
			.append('id="userManagementSearch_searchBox" onkeydown="if(event.keyCode==13){changeSearchInfo();}"');
	searchBoxBuffer
			.append('placeholder="请输入要搜索的用戶名" style="resize: none; height=60px; font-size: 37px;margin: 2px;" onkeydown="searchInfoKeyDown(e)"></input>');
	searchBoxBuffer.append('<input type="button" id="searchBtn" style="height: 50px;width: 100px; font-size: 20px;" value="搜索"');
	searchBoxBuffer
			.append('style="margin-right: 0.5em;" onclick="searchInfo()"/>');
	$('.datagrid-toolbar').append(enableStatusBuffer.toString());
	$('.datagrid-toolbar').append(searchBoxBuffer.toString());
	// 获取DataGrid分页组件对象
	var p = $("#userManagementTable").datagrid("getPager");
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
			$('#userManagementTable').datagrid('loadData', jsonObj);
		}
	});// ajax
}

function changeFilterStatus(status) {
	$('#enalbeStatusHd').val(status);
	searchAccountInfo();
}

function changeSearchInfo() {
	var info = $('#userManagementSearch_searchBox').val();
	$('#searchInfoHd').val(info);
}

function searchInfoKeyDown(e) {
	if (e.keyCode == 13) {
		searchInfo();
	}
}

function searchInfo() {
	var info = $('#userManagementSearch_searchBox').val().trim();
	$('#searchInfoHd').val(info);
	if (info == "") { 
		return;
	}
	searchAccountInfo();
}
/**
 * 设置操作列的信息 参数说明 value 这个可以不管，但是要使用后面 row 和index 这个参数是必须的 row 当前行的数据 index
 * 当前行的索引 从0 开始
 */
function optFormater(value, row, index) {
	var userName = row.userName;
	var userId = row.userId;
	var enableStatus = row.enableStatus;
	var params = userId + "," + enableStatus + ",'" + userName + "'";
	var edit = '<a href="javascript:openDialog_edit(' + params + ')">编辑</a>';
	return edit;
};

/** --------------编辑操作弹出框------------------* */
// 设置弹出框的属性
function setDialog_edit() {
	$('#userManagementEdit').dialog({
		title : '帐号编辑',
		modal : true, // 模式窗口：窗口背景不可操作
		collapsible : true, // 可折叠，点击窗口右上角折叠图标将内容折叠起来
		resizable : true
	// 可拖动边框大小
	});
}
// 打开对话框
function openDialog_edit(userId, enableStatus, userName) {
	userManagementEditReset(userId, enableStatus, userName);
	$('#userManagementEdit').dialog('open');
}
// 关闭对话框
function closeDialog_edit() {
	$('#userManagementEdit').dialog('close');
}
// 根据用户id查询用户的信息
function userManagementEditReset(userId, enableStatus, userName) {
	$("#userManagementEdit_message").html("");
	$("#userManagementEdit_name").val(userName);
	$("#userManagementEdit_userId").val(userId);
	$("#userManagementEdit_enableStatus").val(enableStatus);
}
// 执行用户编辑操作
function userManagementEdit() {
	var validateResult = true;
	// easyui 表单验证
	$('#table_userManagementEdit input').each(function() {
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
	var userId = encodeURIComponent($("#userManagementEdit_userId").val());
	var enableStatus = encodeURIComponent($(
			"#userManagementEdit_enableStatus").val());
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		dataType : "json",
		data : {
			userId : userId,
			enableStatus : enableStatus
		},
		url : '/buy/superadmin/modifypersonInfo',// 请求的action路径 
		success : function(data) 
		{
			if(data.success)
			{
				var messgage = "修改成功!";
				searchAccountInfo();
				$("#userManagementEdit_message").html(messgage);
			}
			else
			{
				alert('请求失败');
			}
		}
	});
}
function imgFormater(value, row, index) {
	var profileImg = row.profileImg;
	return '<img src="' + profileImg + '" width="100px" height="60px">';
}
function typeFormater(value, row, index)
{
	var userType=row.userType;
	if(userType==1)
	{
		return '普通用户';
	}
	else
	{
		return '管理员';
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
		success : function(data) 
		{
			if(data.success)
			{
				alert("操作成功");
				searchAccountInfo();
		
			}
			else
			{
				alert('请求失败');
			}
		}
	});
}
