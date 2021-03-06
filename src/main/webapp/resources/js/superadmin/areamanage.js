$(function() {
	initializePage();
	listAreaManagementInfo();
});
function listAreaManagementInfo() {
	$.ajax({
		url : "/buy/superadmin/listareas",
		type : "post",
		dataType : 'json',
		success : function(data) {
			//loadData加载本地数据，旧的行会被移除
			$('#areaManagementTable').datagrid('loadData', data);
		}
	});
}
function initializePage() {
	// 加载表格数据
	ajaxTable();

	// 初始化弹出层
	setDialog_add();
	closeDialog_add();
	setDialog_edit();
	closeDialog_edit();
}
/** --------------table------------------* */
/**
 * 加载表格数据
 */
function ajaxTable() {
	// 加载表格
	$('#areaManagementTable')
			.datagrid(
					{
						toolbar : 
							[
						{// 正上方工具栏
							text : '添加新区域',
							iconCls : 'icon-add',
							handler : function() 
							{
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
						/*fit:true,*/
						fitColumns:true,
						nowarp:false, 
						// pageNumber : 1,
						//当从远程站点加载数据时，显示的提示信息。
						loadMsg : '数据加载中,请稍后...',
						// pageList:[10,30,50,100], //设置每页显示多少条
						//加载远程数据发生某些错误时触发。
						onLoadError : function() {
							alert('数据加载失败!');
						},
						//当请求远程数据时，发送的额外参数
						queryParams : {// 查询条件
							
						},
						//当用户点击一行时触发，参数包括：rowIndex：被点击行的索引，从 0 开始,rowData：被点击行对应的记录。
						onClickRow : function(rowIndex, rowData) {
							// 取消选择某行后高亮
							$('#areaManagementTable').datagrid('unselectRow',
									rowIndex);
						},
						//getData返回加载的数据
						onLoadSuccess : function() {
							var value = $('#areaManagementTable').datagrid(
									'getData')['errorMsg'];
							if (value != null) {
								alert("错误消息:" + value);
							}
						}
					}).datagrid('acceptChanges');
	//提交自从被加载以来或最后一次调用acceptChanges以来所有更改的数据
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
 * row:可以通过 row["field值"] 或者 row.field值 得到同一行其他列的值。可以是前面的列，也可以是后面的列。
 */
function optFormater(value, row, index) {
	var areaId = row.areaId;
	var areaName = row.areaName;
	var priority = row.priority;
	var areaDesc = row.areaDesc;
	var params = areaId + ",'" + areaName + "'," + priority + ",'" + areaDesc
			+ "'";
	var edit = '<a href="javascript:openDialog_edit(' + params + ')">编辑</a> | ';
	var del = '<a href="#" onclick="doDel(' + areaId + ')">删除</a>';
	return edit + del;
};

/** --------------添加操作弹出框------------------* */
// 设置弹出框的属性
function setDialog_add() {
	$('#areaManagementAdd').dialog({
		title : '区域添加',
		modal : true, // 模式窗口：窗口背景不可操作
		collapsible : true, // 可折叠，点击窗口右上角折叠图标将内容折叠起来
		resizable : true, // 可拖动边框大小
		onClose : function() { // 继承自panel的关闭事件
			areaManagementAddReset();
		}
	});
}
// 打开对话框
function openDialog_add() {
	$('#areaManagementAdd').dialog('open');
}
// 关闭对话框
function closeDialog_add() {
	$('#areaManagementAdd').dialog('close');
}
// 执行用户添加操作
function areaManagementAdd() {
	var validateResult = true;
	// easyui 表单验证
	$('#table_areaManagementAdd input').each(function() {
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
	var area = {};
	area.areaName = encodeURIComponent($("#areaManagementAdd_areaName").val());
	area.priority = $("#areaManagementAdd_priority").val();
	area.areaDesc = encodeURIComponent($("#areaManagementAdd_areaDesc").val());
	$.ajax({
		async : false,
		cache : false,
		type : 'post',
		dataType : "json",
		data : {
			areaStr : JSON.stringify(area)
		},
		url : '/buy/superadmin/addarea',// 请求的action路径
		error : function(data) {// 请求失败处理函数
			var pageData = $.parseJSON(data);
			alert(pageData.errorMessage);
		},
		success : function() {
			var message = "添加成功!";
			listAreaManagementInfo();
			$("#areaManagementAdd_message").html(messgage); 
		}
	});
}
// 用户添加页面数据重置操作
function areaManagementAddReset() {
	$("#areaManagementAdd_message").html("");
	$("#areaManagementAdd_areaName").val("");
	$("#areaManagementAdd_priority").val("");
	$("#areaManagementAdd_areaDesc").val("");
}

/** --------------编辑操作弹出框------------------* */
// 设置弹出框的属性
function setDialog_edit() {
	$('#areaManagementEdit').dialog({
		title : '区域编辑',
		modal : true, // 模式窗口：窗口背景不可操作
		collapsible : true, // 可折叠，点击窗口右上角折叠图标将内容折叠起来
		resizable : true
	// 可拖动边框大小
	});
}
// 打开对话框
function openDialog_edit(areaId, areaName, priority, areaDesc) {
	areaManagementEditReset(areaId, areaName, priority, areaDesc);
	$('#areaManagementEdit').dialog('open');
}
// 关闭对话框
function closeDialog_edit() {
	$('#areaManagementEdit').dialog('close');
}
// 根据用户id查询用户的信息
function areaManagementEditReset(areaId, areaName, priority, areaDesc) {
	$("#areaManagementEdit_message").html("");
	$("#areaManagementEdit_areaId").val(areaId);
	$("#areaManagementEdit_areaName").val(areaName);
	$("#areaManagementEdit_priority").val(priority);
	$("#areaManagementEdit_areaDesc").val(areaDesc);
}
// 执行用户编辑操作
function areaManagementEdit() {
	var validateResult = true;
	// easyui 表单验证
	$('#table_areaManagementEdit input').each(function() {
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
	var area = {};
	area.areaId = $("#areaManagementEdit_areaId").val();
	area.areaName = encodeURIComponent($("#areaManagementEdit_areaName").val());
	area.priority = $("#areaManagementEdit_priority").val();
	area.areaDesc = encodeURIComponent($("#areaManagementEdit_areaDesc").val());
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		dataType : "json",
		data : {
			areaStr : JSON.stringify(area)
		},
		url : '/buy/superadmin/modifyarea',// 请求的action路径
		error : function() {// 请求失败处理函数
			alert('请求失败');
		},
		success : function() {
			var messgage = "修改成功!";
			listAreaManagementInfo();
			$("#areaManagementEdit_message").html(messgage);
		}
	});
}

/** --------------执行删除操作------------------* */
function doDel(areaId) {
	$.messager.confirm('删除提示', '你确定永久删除该数据吗?', function(data) {
		if (data) {
			var url = '/buy/superadmin/removearea?areaId=' + areaId;
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
		type : 'POST',
		dataType : "json", 
		url : url,// 请求的action路径
		error : function() {// 请求失败处理函数
			alert('请求失败');
		},
		success : function() {
			alert("操作成功");
			listAreaManagementInfo();
		}
	});
}

/**
 * 批量操作
 * 
 * @return
 */
function batch(flag) {
	//getSelected返回第一个选中的行或者 null
	if ($('#areaManagementTable').datagrid('getSelected')) {
		// 首先如果用户选择了数据，则获取选择的数据集合
		var tempList = [];
		var areaNames = [];
		//getSelections返回所有选中的行，当没有选中的记录时，将返回空数组
		var selectedRow = $('#areaManagementTable').datagrid('getSelections');
		var jsonList = [];
		for (var i = 0; i < selectedRow.length; i++) {
			jsonList.push(selectedRow[i].areaId);
			areaNames.push(selectedRow[i].areaName);
		}
		// 删除操作
		$.messager.confirm('删除提示', '你确定永久删除下列区域吗?<br/>' + areaNames.join(','),
				function(r) {
					if (r) {
						var url = '/buy/superadmin/removeareas';
						$.ajax({
							async : false,
							cache : false,
							type : 'POST',
							dataType : "json",
							traditional: true,
							url : url,// 请求的action路径
							data:{areaIdListStr:JSON.stringify(jsonList)},
							error : function() {// 请求失败处理函数
								alert('请求失败');
							},
							success : function() {
								alert("操作成功");
								listAreaManagementInfo();
							}
						});
					}
				});

	}
}
