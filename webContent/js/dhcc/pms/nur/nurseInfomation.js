$(function(){
	//加载个人信息
	$.post(
			getContextPath() +"/nur/nurseCtrl!getUserInfo.htm",
			"",
 			function(data){
 				$("#loc").val(data.locName);
 				$("#locID").val(data.normalAccount.normalUser.locId); 
 				$("#accountAlias").val(data.normalAccount.accountAlias);
 				$("#realName").val(data.normalAccount.normalUser.realName);
 				$("#tel").val(data.normalAccount.normalUser.telephone);
 				$("#mail").val(data.normalAccount.normalUser.email);
 				//$("#destnation").val(data.destinationId);
 				$("#destnation").val(data.destinations);
 			},
 			"json"
 	);
 	
	  //修改收获地址
	  $("#editDestination").click(function(){
		  var locId=$("#locID").val();
		  window.open($WEB_ROOT_PATH+'/nur/nurseCtrl!nurDestination.htm?dto.orderDto.loc='+locId,'width=100px,heigth=700px,top=0,left=0,scrollbars=yes','_blank');
	  });
	  //保存用户个人信息
	  $("#submitBtn").on('click',function(){
			$.post(
				$WEB_ROOT_PATH+'/nur/nurseCtrl!saveInfo.htm',
				{
					'dto.normalAccountDto.normalAccount.normalUser.realName':$("#realName").val(),
					'dto.normalAccountDto.normalAccount.normalUser.telephone':$("#tel").val(),
					'dto.normalAccountDto.normalAccount.normalUser.email':$("#mail").val(),
				},
				function(data){
					if(data.opFlg==1){
						alert("保存成功!");
						window.location.href=$WEB_ROOT_PATH+"/nur/nurseCtrl!nurseManageMain.htm";
					}
				},
				'json'
			);
		});
});