(function() {
	var projNo = window.parent.projNo;
	var buName = window.parent.projBU;
	
	String.prototype.endWith=function(str){    
		  var reg=new RegExp(str+"$");    
		  return reg.test(this);       
	};
})()


$(document).on("change", "#importInput", function () {
	$("#filePathInfo").val($(this).val());
});

$(document).on("click", "#templateDownload", function () {
	var downloadUrl = getRootPath() + '/project/projectTemplate';
	$("#templateDownload").attr('href', downloadUrl);
});
$(document).on("click", "#downloadProj", function () {
	var downloadUrl = getRootPath() + '/project/downloadNew';
	$("#downloadProj").attr('href', downloadUrl);
});

$(document).on("click", "#submitImport", function () {
	var filePath = $("#filePathInfo").val();
	if(filePath == ''){
		alert("请先选择要导入的文件!");
	}else if(!filePath.endWith('.xlsx')){
		alert("文件格式需为xlsx");
	}else{	
		var option = {
			url: getRootPath() + "/project/import",
			type: 'POST',
			dataType: 'json',
			data:{
			},
			success: function(data){
					 alert("导入成功的项目："+data.sess+"\n导入失败的项目:"+data.err);
			}
		};
		$('#uploadDialog').modal('hide');
		$("#importForm").ajaxSubmit(option);
	}
});

function getQueryString(name) {  
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");  
    var r = window.location.search.substr(1).match(reg);  
    if (r != null) return decodeURI(r[2]);  
    return null;  
}

var svnflag=0;
var dtsflag=0;
$(document).on("click", "#refrush", function () {
	dtsflag=0;
	svnflag=0;
	$('#successDiv').css('display','none');
	$('#dataAcquisition').css('display','block');
	$('#jindu').modal({
		backdrop: 'static',
		keyboard: false
    });
	$('#jindu').modal('show');
		$.ajax({
				url: getRootPath() + '/svnTask/svn',
				type: 'post',
				data: {
					no: projNo
				},
				success: function (data) {
//					alert("SVN采集完毕!");
					svnflag=1;
					$('#svnDataUpdate').css('display','block');
					refrushFlag();
				}
			});
			$.ajax({
				url: getRootPath() + '/dtsTaskController/dtsTask',
				type: 'post',
				data: {
					no: projNo
				},
				success: function () {
//					alert("DTS采集完毕!");
					dtsflag=1;
					$('#dtsDataUpdate').css('display','block');
					refrushFlag();
				}
			});
			$.ajax({
				url: getRootPath() + '/git/gitTask',
				type: 'post',
				data: {
					no: projNo
				},
				success: function () {
				}
			});
});

function refrushFlag() {  
    if(svnflag==1 && dtsflag==1){
    	$('#dataAcquisitionDel').css('display','block');
//    	$('#successDiv').css('display','block');
//    	$('#dataAcquisition').css('display','none');
//    	$('#svnDataUpdate').css('display','none');
//    	$('#dtsDataUpdate').css('display','none');
    }
}