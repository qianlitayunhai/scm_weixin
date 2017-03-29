<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<%@include file="/WEB-INF/jsp/common/scriptInc.jsp"%>
 <script>
 

    function ConT(value,row,index){
		if(row.hopauditflag=="Y"){
			return '<a  class="dhc-linkbutton l-btn l-btn-plain" onclick="javascript:ConTra('+row.hopvenid+',1)" ><span class="l-btn-left"><span class="l-btn-text icon-remove l-btn-icon-left">拒绝</span></span></a>';
		}else{
			return '<a  class="dhc-linkbutton l-btn l-btn-plain" onclick="javascript:ConTra('+row.hopvenid+',2)" ><span class="l-btn-left"><span class="l-btn-text icon-save l-btn-icon-left">通过</span></span></a>';
			
		}
	};
	function ConTra(hopvenid,status){
	
		status=status==1?"HN":"H";
		if ($CommonUI.getDataGrid("#datagrid").datagrid('getSelections').length != 1) {
			$CommonUI.alert('请选一个供应商对照');
			return;
		}
		var row =$("#datagrid").datagrid('getSelected');
		var venid=row.vendorid;
		regid=row.regid;
		$.post(
			$WEB_ROOT_PATH+'/hop/hopVendorCtrl!conAndAudit.htm',
			{
				'dto.hopVendor.hopVendorId': hopvenid,
				'dto.hopVendor.hopVenId': venid,
				'dto.flag':status,
				'dto.regId':regid
			},
			function(data){
				if(data.opFlg=="1"){
					$CommonUI.alert("操作成功!");
					$CommonUI.getDataGrid('#datagrid2').datagrid('reload');
				}
			},
			"json"
		);
	}

    $(function(){
    	$CommonUI.getDataGrid('#datagrid').datagrid({
    		onDblClickRow: function(rowIndex, rowData){
    			$CommonUI.getDataGrid('#datagrid2').datagrid({
    				url:$WEB_ROOT_PATH+'/hop/hopVendorCtrl!listHopCon.htm',
        		    queryParams: {
        		    	'dto.hopVendor.hopVenId': rowData.vendorid,
        			}
    			});
    		},
    		onClickCell:function(rowIndex, field, value){
    			if(field=="code"){
    				window.open($WEB_ROOT_PATH+'/hop/hopVendorCtrl!HistoryDetail.htm?venodrId='+$('#datagrid').datagrid('getRows')[rowIndex]['vendorid'])
    			}
    		}
    	});
    	$("#searchVen").on('click', function() {
    		$CommonUI.getDataGrid('#datagrid').datagrid({  
    		    url:$WEB_ROOT_PATH+'/ven/vendorCtrl!listRegVen.htm',
    		    queryParams: {
    		    	'dto.vendor.name': $("#venName").val(),
    		    	'dto.inputStr': $("#venInputStr").val(),
    		    	'dto.vendor.alias': $("#venAlias").val(),
    		    	'dto.auditFlag':$("#venAuditFlag").combobox('getValue'),
    			}
   		 	});
   		});
    	
    	$("#searcHop").on('click', function() {
    		$CommonUI.getDataGrid('#datagrid2').datagrid({  
    		    url:$WEB_ROOT_PATH+'/hop/hopVendorCtrl!listHopVendor.htm',
    		    queryParams: {
    		    	'dto.hopVendor.hopCode': $("#HopCode").val(),
    		    	'dto.hopVendor.hopName': $("#HopName").val(),
    		    	'dto.hopVendor.hopAlias': $("#HopAlias").val(),
    		    	'dto.flag':$("#flag").combobox('getValue'),
    		    	'dto.auditFlag':$("#auditFlag").combobox('getValue')
    			}
   		 	});
   		});
    	
    	$("#autoContrast").on('click', function() {
    		/*
    		if($("#hop").combobox('getValue').length != 1){
    			$CommonUI.alert("请选择医院!");
    			return;
    		}
    	*/
    		$.post(
     				$WEB_ROOT_PATH+'/hop/hopVendorCtrl!autoContrast.htm',
     				{
     					'dto.hopVendor.hopHopId': $("#hop").combobox('getValue'),
     				},
     				function(data){
    					if(data.dto.opFlg=="1"){
//                            $CommonUI.alert("对照成功："+data.resultContent+"条数据。");  //undefined 条数据
     						$CommonUI.alert("对照成功!"); //加提示多少条？hopVendors.size()条吧 $.get(hopVendors.size()),
     					}
     				},
     				"json"
     			);
    		});
    	
    	$("#queryZiZhi").on('click', function() {
    		if ($CommonUI.getDataGrid("#datagrid").datagrid('getSelections').length != 1) {
    			$CommonUI.alert('请选一个供应商');
    			return;
    		}
    		var row =$("#datagrid").datagrid('getSelected');
    		window.open($WEB_ROOT_PATH+'/nur/nurseCtrl!auditResult.htm?dto.vendorDto.vendor.vendorId='+row.vendorid);	
    	});
		$("#queryTimeLine").on('click', function() {
			if ($CommonUI.getDataGrid("#datagrid").datagrid('getSelections').length != 1) {
    			$CommonUI.alert('请选一个供应商');
    			return;
    		}
    		var row =$("#datagrid").datagrid('getSelected');
			window.open($WEB_ROOT_PATH+'/hop/hopVendorCtrl!HistoryDetail.htm?venodrId='+row.vendorid);
			
		});
    });
    </script>

</head>
<body >
	<div id="toolbar" style="height: auto">
		  <div  style="margin-bottom:5px;margin-top:5px">
			名称: <input id="venName" style="width: 100px;"
			type="text" />
			
			别名: <input id="venAlias" style="width: 100px;"
			type="text" />
			
			审核状态:
			<select class="combobox" panelHeight="auto" style="width:105px" id="venAuditFlag"  >
				<option value="0">空</option>
				<option value="1">已审核</option>
				<option value="2">审核不通过</option>
				<option value="3">未审核</option>
			</select>
			 
			<br>
			邮箱/注册名/工商执照号: <input id="venInputStr" style="width: 200px;"
			type="text" />
			<a href="#" class="linkbutton" iconCls="icon-search" id="searchVen">查询</a>
			<br>
			<a href="#" class="linkbutton" iconCls="icon-save" id="queryZiZhi">查看供应商资质</a>
			<a href="#" class="linkbutton" iconCls="icon-save" id="queryTimeLine">查看供应商时间轴</a>
		 </div>
	</div>	
    <div id="toolbar2" style="height: auto">
		  <div  style="margin-bottom:5px;margin-top:5px">
			名&nbsp;&nbsp;称: <input id="HopName" style="width: 100px;"
			type="text" />
			代&nbsp;&nbsp;码: <input id="HopCode" style="width: 100px;"
			type="text" />
			<a href="#" class="linkbutton" iconCls="icon-search" id="autoContrast">自动对照</a> 
		  </div>
		  <div  style="margin-bottom:5px;margin-top:5px">	
			状&nbsp;&nbsp;态:
			<select class="combobox" panelHeight="auto" style="width:105px" id="flag">
				<option value="0">空</option>
				<option value="1">已对照</option>
				<option value="2">未对照</option>
			</select>
			资质审核:
			<select class="combobox" panelHeight="auto" style="width:105px" id="auditFlag">
				<option value="0">空</option>
				<option value="1">已审核</option>
				<option value="2">未审核</option>
			</select>
			<!--  
			医&nbsp;&nbsp;院:
			<input style="width: 105px;"
						class="combobox" type="text" 
						 id="hop" />
			-->
			<a href="#" class="linkbutton" iconCls="icon-search" id="searcHop">查询</a>
		 </div>
	</div>
  <div class="layout" data-options="fit:'true',border:true">
        <div data-options="region:'east',title:'医院供应商',iconCls:'icon-ok',split:true" style="width:630px">
        	<table id="datagrid2" style="height: 250px"  class="datagrid"
					data-options="toolbar:'#toolbar2',
					 			 fit:true,
								 fitColumns:true,
								 singleSelect:true,
								 pagination:true,
				    			 method:'post',
				    			 rownumbers:true,
				    			 striped:true,
				    			 singleselect:true,
				    			 pageSize:15,
	    						 pageList:[15,30,45],
								 ">
								 
					<thead>
						<tr>
							<th data-options="field:'hopvenid',hidden:true">IncId ID</th>
							<th data-options="field:'hopvenname',sortable:true" width="1/6">供应商</th>
							<th data-options="field:'hopvencode',sortable:true" width="1/6">医院供应商代码</th>
							<th data-options="field:'hopname',sortable:true" width="1/6">医院</th>
							<th data-options="field:'venname',sortable:true" width="1/6">对照供应商</th>
							<th data-options="field:'venid',hidden:true"></th>
							<th data-options="field:'operate',formatter:ConT" width="1/6">对照</th>
							<!--  
							<th data-options="field:'audit',formatter:AuditT" width="1/6">资质</th>
							-->
						</tr>
					</thead>
				</table>
        
        </div>
        <div data-options="region:'center',title:'供应商(双击查看已经对照医院供应商),(单击代码查看供应商资质历史)',iconCls:'icon-ok'" >
            <table id="datagrid" style="height: 250px"  class="datagrid"
					data-options="toolbar:'#toolbar',
					 			 fit:true,
								 fitColumns:true,
								 singleSelect:true,
								 pagination:true,
				    			 method:'post',
				    			 rownumbers:true,
				    			 striped:true,
				    			 singleselect:true,
				    			 pageSize:15,
	    						 pageList:[15,30,45],
								 ">
								 
					<thead>
						<tr>
							<th data-options="field:'vendorid',hidden:true">IncId ID</th>
							
							<th data-options="field:'name',sortable:true">供应商</th>
							<th data-options="field:'taxation',sortable:true">工商执照号</th>
							<th data-options="field:'email',sortable:true">邮箱</th>
							<th data-options="field:'contact',sortable:true">联系人</th>
							<th data-options="field:'account',sortable:true">注册帐号</th>
							<th data-options="field:'tel',sortable:true">电话</th>
							<th data-options="field:'fax',sortable:true">传真</th>
							<th data-options="field:'address',sortable:true">联系地址</th>
							<th data-options="field:'code',sortable:true">代码</th>
						</tr>
					</thead>
				</table>
        </div>
    </div>  
   
</div> 
</body>
</html>