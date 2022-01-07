<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

<script type="text/javascript">

	$(function(){
		//刷新列表
		pageList(1,3);
		//
		$(".time1").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "top-left"
		});
		$("#searchBtn").click(function(){
			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-customerId").val($.trim($("#search-customerId").val()));
			$("#hidden-contactsId").val($.trim($("#search-contactsId").val()));
			$("#hidden-source").val($.trim($("#search-source").val()));
			$("#hidden-type").val($.trim($("#search-type").val()));
			$("#hidden-stage").val($.trim($("#search-stage").val()));
			pageList(1,3);
		})

	});
	function pageList(pageNo,pageSize){

		$("#search-name").val($.trim($("#hidden-name").val()));
		$("#search-owner").val($.trim($("#hidden-owner").val()));
		$("#search-customerId").val($.trim($("#hidden-customerId").val()));
		$("#search-contactsId").val($.trim($("#hidden-contactsId").val()));
		$("#search-source").val($.trim($("#hidden-source").val()));
		$("#search-type").val($.trim($("#hidden-type").val()));
		$("#search-stage").val($.trim($("#hidden-stage").val()));

		$.ajax({
			url:"workbench/transaction/pageList.do",
			data:{
				"pageNo":pageNo,
				"pageSize":pageSize,
				"name":$.trim($("#search-name").val()),
				"owner":$.trim($("#search-owner").val()),
				"customerId":$.trim($("#search-customerId").val()),
				"contactsId":$.trim($("#search-contactsId").val()),
				"stage":$.trim($("#search-endDate").val()),
				"type":$.trim($("#search-type").val()),
				"source":$.trim($("#search-source").val())
			},
			type:"get",
			dataType:"json",
			success:function(data){
				var html = "";
				$.each(data.dataList,function(i,e){
					html += '<tr>';
					html += '<td><input type="checkbox" /></td>';
					html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/transaction/detail.do?id='+e.id+'\';">'+e.name+'</a></td>';
					html += '<td>'+e.customerId+'</td>';
					html += '<td>'+e.stage+'</td>';
					html += '<td>'+e.type+'</td>';
					html += '<td>'+e.owner+'</td>';
					html += '<td>'+e.source+'</td>';
					html += '<td>'+e.contactsId+'</td>';
					html += '</tr>';
				})
				$("#tranTbody").html(html);

				var totalPages = data.total % pageSize == 0 ? data.total / pageSize :parseInt(data.total / pageSize)+1;
				$("#tranPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 5, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,
					//点击分页组件时，调用pageList()
					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});

			}
		})
	}
</script>
</head>
<body>



<div>
	<div style="position: relative; left: 10px; top: -10px;">
		<div class="page-header">
			<h3>交易列表</h3>
		</div>
	</div>
</div>

<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">

	<div style="width: 100%; position: absolute;top: 5px; left: 10px;">

		<div class="btn-toolbar" role="toolbar" style="height: 80px;">
			<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				<input type="hidden" id="hidden-owner">
				<input type="hidden" id="hidden-name">
				<input type="hidden" id="hidden-customerId">
				<input type="hidden" id="hidden-contactsId">
				<input type="hidden" id="hidden-source">
				<input type="hidden" id="hidden-type">
				<input type="hidden" id="hidden-stage">
				<div class="form-group">
					<div class="input-group">
						<div class="input-group-addon">所有者</div>
						<input class="form-control" type="text" id="search-owner">
					</div>
				</div>

				<div class="form-group">
					<div class="input-group">
						<div class="input-group-addon">名称</div>
						<input class="form-control" type="text" id="search-name">
					</div>
				</div>

				<div class="form-group">
					<div class="input-group">
						<div class="input-group-addon">客户名称</div>
						<input class="form-control" type="text" id="search-customerId">
					</div>
				</div>

				<br>

				<div class="form-group">
					<div class="input-group">
						<div class="input-group-addon">阶段</div>
						<select class="form-control" id="search-stage">
							<option></option>
							<c:forEach items="${stageList}" var="s">
								<option value="${s.value}">${s.text}</option>
							</c:forEach>
						</select>
					</div>
				</div>

				<div class="form-group">
					<div class="input-group">
						<div class="input-group-addon">类型</div>
						<select class="form-control" id="search-type">
							<option></option>
							<c:forEach items="${transactionTypeList}" var="s">
								<option value="${s.value}">${s.text}</option>
							</c:forEach>
						</select>
					</div>
				</div>

				<div class="form-group">
					<div class="input-group">
						<div class="input-group-addon">来源</div>
						<select class="form-control" id="search-source">
							<option></option>
							<c:forEach items="${sourceList}" var="s">
								<option value="${s.value}">${s.text}</option>
							</c:forEach>
						</select>
					</div>
				</div>

				<div class="form-group">
					<div class="input-group">
						<div class="input-group-addon">联系人名称</div>
						<input class="form-control" type="text" id="search-contactsId">
					</div>
				</div>

				<button type="button" class="btn btn-default" id="searchBtn">查询</button>

			</form>
		</div>
		<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 10px;">
			<div class="btn-group" style="position: relative; top: 18%;">
				<button type="button" class="btn btn-primary" onclick="window.location.href='workbench/transaction/add.do';"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				<button type="button" class="btn btn-default" onclick="window.location.href='edit.html';"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				<button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
			</div>


		</div>
		<div style="position: relative;top: 10px;">
			<table class="table table-hover">
				<thead>
				<tr style="color: #B3B3B3;">
					<td><input type="checkbox" /></td>
					<td>名称</td>
					<td>客户名称</td>
					<td>阶段</td>
					<td>类型</td>
					<td>所有者</td>
					<td>来源</td>
					<td>联系人名称</td>
				</tr>
				</thead>
				<tbody id="tranTbody">
					<%--<tr class="active">
						<td><input type="checkbox" /></td>
						<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">动力节点-交易01</a></td>
						<td>动力节点</td>
						<td>谈判/复审</td>
						<td>新业务</td>
						<td>zhangsan</td>
						<td>广告</td>
						<td>李四</td>
					</tr>--%>
				</tbody>
			</table>
		</div>

		<div style="height: 50px; position: relative;top: 20px;">
			<div id="tranPage">

			</div>
		</div>

	</div>

</div>
</body>
</html>