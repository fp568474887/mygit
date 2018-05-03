var projNo;
var buName;
(function() {
	projNo = window.parent.projNo;
	buName = window.parent.projBU;
	var units = {};
	function initData() {
		$.ajax({
			url : getRootPath() + "/bu/projOverviewData",
			type : 'POST',
			async: false,//是否异步，true为异步
			data : {
				no : projNo
			},
			success : function(data) {
				$("#name").empty();
				$("#pm").empty();
				$("#codeAmount").empty();
				$("#projectWorkload").empty();
				
				$("#startDate").empty();
				$("#endDate").empty();
				$("#progressDeviation").empty();
				$("#type").empty();
				
				$("#bu").empty();
				$("#pdu").empty();
				$("#du").empty();
				$("#hwpdu").empty();
				
				$("#hwzpdu").empty();
				$("#qa").empty();
				$("#se").empty();
				$("#projectState").empty();
				$("#isOffshore").empty();
				
				$("#projectType").empty();
				$("#coopType").empty();
				$("#pduSpdt").empty();
				$("#area").empty();
				
				$("#projectSynopsis").empty();
				
				$("#name").append(data.name);
				$("#name").attr("title",data.name);
				$("#pm").append(data.pm);
				$("#startDate").append(new Date(data.startDate).format('yyyy-MM-dd'));
				$("#endDate").append(new Date(data.endDate).format('yyyy-MM-dd'));
				$("#type").append(data.type);
				$("#bu").append(data.bu);
				$("#pdu").append(data.pdu);
				$("#du").append(data.du);
				$("#hwpdu").append(data.hwpdu);
				$("#hwzpdu").append(data.hwzpdu);
				$("#qa").append(data.qa);
				$("#projectState").append(data.projectState);
				$("#isOffshore").append(data.isOffshore);
				$("#projectType").append(data.projectType);
				$("#coopType").append(data.coopType);
				$("#pduSpdt").append(data.pduSpdt);
				$("#area").append(data.area);
				$("#projectSynopsis").append(data.projectSynopsis);
			}
		})
	};
	
	function getProjectKeyroles() {
		$.ajax({
			url : getRootPath() + "/bu/getProjectKeyrolesNo",
			type : 'POST',
			data : {
				no : projNo
			},
			success : function(data) {
				for(var i=0;i<data.length;i++){
					var tab = '<tr style="height: 30px;">'+
						'<td date-type="select" select-date="PM,产品经理,SE,MDE,BA,IA,TC,TSE,QA,TL">'+data[i].position+'</td>'+
						'<td date-type="input">'+data[i].name+'</td>'+
						'<td date-type="input">'+data[i].zrAccount+'</td>'+
						'<td date-type="input">'+data[i].hwAccount+'</td>'+
						'<td date-type="select" select-date="未通过,通过">'+data[i].rdpmExam+'</td>'+
						'<td date-type="select" select-date="未通过,通过">'+data[i].replyResults+'</td>'+
				    	'<td date-type="select" select-date="完全胜任,基本胜任,暂不胜任">'+data[i].proCompetence+'</td>'+
				    	'<td date-type="select" select-date="在岗,后备">'+data[i].status+'</td>'+
				    	'<td date-type="no"><div name="del" style="display: none;"><img style="margin: 2px;" src="images/deleteicon.png" alt="删除" width="17px" height="17px"/></div></td>'+
				    	'</tr>';
					$("#guanjianjuese tbody").append(tab);
				}
			}
		})
	}
	function getProjectSchedule() {
		$.ajax({
			url : getRootPath() + "/bu/getProjectScheduleNo",
			type : 'POST',
			data : {
				no : projNo
			},
			success : function(data) {
				for(var i=0;i<data.length;i++){
					var tab = '<tr style="height: 30px;">'+
						'<td date-type="select" select-date="CP1,CP2,CP3,迭代1,迭代2,迭代3,迭代4,迭代5,迭代6">'+data[i].node+'</td>'+
						'<td date-type="select" select-date="CP,迭代点">'+data[i].nodeType+'</td>'+
						'<td date-type="date"  name="planDate">'+new Date(data[i].planDate).format('yyyy-MM-dd')+'</td>'+
						'<td date-type="date"  name="actualDate">'+new Date(data[i].actualDate).format('yyyy-MM-dd')+'</td>'+
						'<td date-type="no"    name="deviationRate">'+data[i].deviationRate+'</td>'+
				    	'<td date-type="no"><div name="del1" style="display: none;"><img style="margin: 2px;" src="images/deleteicon.png" alt="删除" width="17px" height="17px"/></div></td>'+
				    	'</tr>';
					$("#cp-review tbody").append(tab);
				}
				
				//结束点为94%，开始点为1%
				//num=day2/day1;为了精确保留5位小数
				//sum=CP和迭代点总长度，单位为px
				//nodesum=当前节点前的节点的总长度，单位为px  或者为当前时间点之前所有的节点总长度
				//calc(num * (93% - sum) + 1% + nodesum)
				
				var milestone = $("#milestone");
				milestone.empty();
				var startDay = $("#startDate").text();
				var endDay = $("#endDate").text();
				var newtime = new Date(new Date().format('yyyy-MM-dd')).getTime();
				startOrEnd(newtime,startDay,endDay);
				var day1 = datedifference(startDay,endDay);
				
				var sum = 0;
				var newsum = 0;
				for(var i=0;i<data.length;i++){
					var nodetime1 = new Date(new Date(data[i].planDate).format('yyyy-MM-dd')).getTime();
					if(data[i].nodeType=='CP'){
						sum = sum + 65;
						if(nodetime1<newtime){
							newsum = newsum + 65;
						}else if(nodetime1==newtime){
							newsum = newsum + 32.5;
						}
					}else if(data[i].nodeType=='迭代点'){
						sum = sum + 28;
						if(nodetime1<newtime){
							newsum = newsum + 28;
						}else if(nodetime1==newtime){
							newsum = newsum + 14;
						}
					}
				}
			
				newNode(startDay,day1,endDay,sum,newsum)
				var node="";
				for(var i=0;i<data.length;i++){
					var nodetime = new Date(data[i].planDate).getTime();
					var nodesum = 0;
					for(var j=0;j<data.length;j++){
						var nodetime2 = new Date(data[j].planDate).getTime();
						if(data[j].nodeType=='CP'){
							if(nodetime2<nodetime){
								nodesum = nodesum + 65;
							}else if(nodetime2==nodetime){
								nodesum = nodesum + 32.5;
							}
						}else if(data[j].nodeType=='迭代点'){
							if(nodetime2<nodetime){
								nodesum = nodesum + 28;
							}else if(nodetime2==nodetime){
								nodesum = nodesum + 14;
							}
						}
					}
					var day2 = datedifference(startDay,new Date(data[i].planDate).format('yyyy-MM-dd'));
					var num = (day2/day1).toFixed(5);
					num ='calc('+num+' * (93% - '+sum+'px) + 1% + '+nodesum+'px)';  
					if(data[i].nodeType=='CP'){
						if(nodetime>newtime){
							node = getNode('CPbg',num,'88','22',data[i].node,data[i].planDate)
						}else{
							node = getNode('CPbgH',num,'88','22',data[i].node,data[i].planDate)
						}
					}else if(data[i].nodeType=='迭代点'){
						if(nodetime>newtime){
							node = getNode('iterationicon',num,'50','60',data[i].node,data[i].planDate)
						}else{
							node = getNode('iterationiconH',num,'50','60',data[i].node,data[i].planDate)
						}
					}
					milestone.append(node);
				}
			}
		})
	}
	function newNode(startDay,day1,endDay,sum,newsum) {
		var node="";
		var milestone = $("#milestone");
		var day3 = datedifference(startDay,new Date().format('yyyy-MM-dd'));
		var num1 = (day3/day1).toFixed(5);
		var num ='calc('+num1+' * (93% - '+sum+'px) + 1% + '+newsum+'px)';  
		if(day3>day1){
			num="94%";
		}
		node = '<div class="newicon" style="left: '+num+';" title="'+new Date().format('yyyy-MM-dd')+'"></div>';
		milestone.append(node);
		newsum = newsum+32.5;
		num ='calc('+num1+' * (93% - '+sum+'px) + 1% + '+newsum+'px)';  
		node = '<div><img class="milestonetime" src="images/timeH.png" alt="时间" width="97%" height="4px" style="width: '+num+';"/></div>';
		milestone.append(node);
	}
	function startOrEnd(newtime,startDay,endDay) {
		var node="";
		var milestone = $("#milestone");
		var nodetime = new Date(startDay).getTime();
		if(nodetime>newtime){
			node = getNode('start','1%','50','60','启动',startDay);
		}else{
			node = getNode('startH','1%','50','60','启动',startDay);
		}
		milestone.append(node);
		node = getNode('start','94%','50','60','结束',endDay);
		milestone.append(node);
	}
	function getNode(classVal,left,style1,style2,name,time) {  
		return '<div class="'+classVal+'" style="left: '+left+';">'
			+'<div style="margin-top: '+style1+'px;">'+name+'</div>'
			+'<div style="margin-top: '+style2+'px;color: #999999;">'+new Date(time).format('yyyy/MM')+'</div>'
			+'</div>';
	}
	function datedifference(sDate1, sDate2) {    //sDate1和sDate2是2006-12-18格式  
        var dateSpan,
            tempDate,
            iDays;
        sDate1 = Date.parse(sDate1);
        sDate2 = Date.parse(sDate2);
        dateSpan = sDate2 - sDate1;
        dateSpan = Math.abs(dateSpan);
        iDays = Math.floor(dateSpan / (24 * 3600 * 1000));
        return iDays
    };
	function getQueryString(name) {  
	    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");  
	    var r = window.location.search.substr(1).match(reg);  
	    if (r != null) return decodeURI(r[2]);  
	    return null;  
	}

	$(document).ready(function(){
		initData();
		getProjectKeyroles();
		getProjectSchedule();
	})
})()

var oldtable;
var oldtable2;
function tableAdd() {
	var tab = '<tr style="height: 30px;">'
		+'<td date-type="select" select-date="PM,产品经理,SE,MDE,BA,IA,TC,TSE,QA,TL">PM</td>'
		+'<td date-type="input">xxx</td><td date-type="input">000000</td><td date-type="input">000000</td>'
		+'<td date-type="select" select-date="未通过,通过">未通过</td>'
		+'<td date-type="select" select-date="未通过,通过">未通过</td>'
    	+'<td date-type="select" select-date="极好,好,一般,较差">极好</td>'
    	+'<td date-type="select" select-date="在职,储备">在职</td>'
    	+'<td date-type="no"><div name="del"><img style="margin: 2px;" src="images/deleteicon.png" alt="删除" width="17px" height="17px"/></div></td>'
    	+'</tr>';
	$("#guanjianjuese tbody").append(tab);
}
function tableAdd2() {
	var tab = '<tr style="height: 30px;">'
		+'<td date-type="select" select-date="CP1,CP2,CP3,迭代1,迭代2,迭代3,迭代4,迭代5,迭代6">CP1</td>'
		+'<td date-type="select" select-date="CP,迭代点">CP</td>'
		+'<td date-type="date" name="planDate">2018-01-01</td>'
		+'<td date-type="date" name="actualDate">2018-01-01</td>'
		+'<td date-type="no"   name="deviationRate">0%</td>'
		+'<td date-type="no"><div name="del1"><img style="margin: 2px;" src="images/deleteicon.png" alt="删除" width="17px" height="17px"/></div></td></tr>';
	$("#cp-review tbody").append(tab);
}
function delToNone(del,display) {
	$("div[name='"+del+"']").each(function(){
		$(this).css('display',display);
	});
}
function tableCancel() {
	$("#guanjianjuese tbody").empty();
	$("#guanjianjuese tbody").append(oldtable);
	delToNone('del','none');
	var edit=$("#edit");
	edit.css('display','block');
	edit.attr("data_obj","1");
	$("#guanjianjuesedo").css('display','none');
}
function tableCancel2() {
	$("#cp-review tbody").empty();
	$("#cp-review tbody").append(oldtable2);
	delToNone('del1','none');
	tableSave2();
}
function tableSave() {
	var edit=$("#edit");
	edit.css('display','block');
	edit.attr("data_obj","1");
	$("#guanjianjuesedo").css('display','none');
	delToNone('del','none');
	tableSaveToDao();
}

function tableSaveToDao() {
	var tab = $('#guanjianjuese');
	var rows = tab[0].rows;
	var allTr = "";
	for(var i = 0; i<rows.length; i++ ){
		if(rows[i].cells[1].tagName=="TH"){
			continue;
		}
		allTr +='{"no":"'+projNo+'",'+
			'"name":"'+rows[i].cells[1].innerHTML+'",'+
			'"zrAccount":"'+rows[i].cells[2].innerHTML+'",'+
			'"hwAccount":"'+rows[i].cells[3].innerHTML+'",'+
			'"position":"'+rows[i].cells[0].innerHTML+'",'+
			'"rdpmExam":"'+rows[i].cells[4].innerHTML+'",'+
			'"replyResults":"'+rows[i].cells[5].innerHTML+'",'+
			'"proCompetence":"'+rows[i].cells[6].innerHTML+'",'+
			'"status":"'+rows[i].cells[7].innerHTML+
			'"},';
	}
	var jsonStr = '{"projRoles":['+allTr.substring(0,allTr.length-1)+']}';
	$.ajax({
		url : getRootPath() + "/bu/insertInfo",
		type : 'POST',
		dataType: "json",
		contentType : 'application/json;charset=utf-8', //设置请求头信息
		data : jsonStr,
		success : function(data) {
			
		}
	})
}

function tableSave2() {
	var edit=$("#edit2");
	edit.css('display','block');
	edit.attr("data_obj","1");
	$("#cp-reviewdo").css('display','none');
	delToNone('del1','none');
	tableSaveToDao2();
}
function tableSaveToDao2() {
	var tab = $('#cp-review');
	var rows = tab[0].rows;
	var allTr = "";
	for(var i = 0; i<rows.length; i++ ){
		if(rows[i].cells[1].tagName=="TH"){
			continue;
		}
		allTr +='{"no":"'+projNo+'",'+
			'"node":"'+rows[i].cells[0].innerHTML+'",'+
			'"nodeType":"'+rows[i].cells[1].innerHTML+'",'+
			'"planDate":"'+rows[i].cells[2].innerHTML+'",'+
			'"actualDate":"'+rows[i].cells[3].innerHTML+'",'+
			'"deviationRate":"'+rows[i].cells[4].innerHTML+'"'+
			'},';
	}
	var jsonStr = '{"projSchedule":['+allTr.substring(0,allTr.length-1)+']}';
	$.ajax({
		url : getRootPath() + "/bu/insertInfoProjectSchedule",
		type : 'POST',
		dataType: "json",
		contentType : 'application/json;charset=utf-8', //设置请求头信息
		data : jsonStr,
		success : function(data) {
			
		}
	})
}

function tableEdit(editid,divid) {
	var edit=$("#"+editid);
	edit.css('display','none');
	if(divid=='guanjianjuesedo'){
		oldtable=$("#guanjianjuese tbody").html();
		delToNone('del','block');
	}else{
		oldtable2=$("#cp-review tbody").html();
		delToNone('del1','block');
	}
	if(edit.attr("data_obj")=="1"){
		$("#"+divid).css('display','block'); 
		edit.attr("data_obj","0")
	}
	
}

function editable(td){
	if(td[0].childNodes.length>1){
		return;
	}
	var dateType = td.attr("date-type");
	if(dateType=="input"){
		var text = td.text();
		var txt = $("<input class='form-control' style='width: 100%;height: 30px;' type='text'>").val(text);
	}else if(dateType=="date"){
			var text = td.text();
			var txt = $("<input class='form-control' max='9999-12-30' style='width: 100%;height: 30px;' type='date'>").val(text);
	}else if(dateType=="no"){
		return;
	}else if(dateType=="select"){
		var text = td.text();
		var select = "<select class='form-control' style='height: 30px;''>";
		var selectDate = td.attr("select-date").split(',');
		selectDate.forEach(function(i,index){
			select += "<option>"+i+"</option>";
		})
		select += "</select>";
		var txt = $(select).val(text);
	}else{
		var text = td.text();
		var txt = $("<input class='form-control' style='width: 100%;height: 30px;' type='text'>").val(text);
	}
	// 根据表格文本创建文本框 并加入表表中--文本框的样式自己调整
	txt.blur(function(){
		// 失去焦点，保存值。于服务器交互自己再写,最好ajax
		var newText = $(this).val();
		// 移除文本框,显示新值
		$(this).remove();
		td.text(newText);
		if(dateType=="date"){
			var planDate = td.parents('tr:first').children('td[name="planDate"]').text()
			var actualDate = td.parents('tr:first').children('td[name="actualDate"]').text()
			var startDay = $("#startDate").text();
//			var endDay = $("#endDate").text();
			var a = datedifference(startDay,planDate);
			var b = datedifference(startDay,actualDate);
			var num = (b/a)*100+"";
			if(num.indexOf(".")>0){
				num = num.substring(0,num.indexOf("."));
			}
			var deviationRate = td.parents('tr:first').children('td[name="deviationRate"]');
			
			deviationRate.text(num+"%");
		}
	});
	td.text("");
	td.append(txt);
	$(txt).focus();
}

//$(document).on("dblclick", "#guanjianjuese td", function () {
$(document).on("click", "#guanjianjuese td", function () {
	if($("#edit").attr("data_obj")=="1"){
		return;
	}
    var td = $(this);
    editable(td);
	
});
//$(document).on("dblclick", "#cp-review td", function () {
$(document).on("click", "#cp-review td", function () {
	if($("#edit2").attr("data_obj")=="1"){
		return;
	}
    var td = $(this);
    editable(td);
	
});

$(document).on("click", "div[name=del]", function () {
	$(this).parents('tr:first').remove();
});
$(document).on("click", "div[name=del1]", function () {
	$(this).parents('tr:first').remove();
});


function datedifference(sDate1, sDate2) {    //sDate1和sDate2是2006-12-18格式  
    var dateSpan,
        tempDate,
        iDays;
    sDate1 = Date.parse(sDate1);
    sDate2 = Date.parse(sDate2);
    dateSpan = sDate2 - sDate1;
    dateSpan = Math.abs(dateSpan);
    iDays = Math.floor(dateSpan / (24 * 3600 * 1000));
    return iDays
};