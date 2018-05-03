(function(){
	
	var titleUnit = {};
	
//	function initBuSel() {
//		$.ajax({
//				url: getRootPath() + '/bu/opts',
//				type: 'post',
//				async: false,//是否异步，true为异步
//				success: function (data) {
//					if(data){
//						if (data.length >0 ){
//							initProjIndicators(data[0].name);
//						}
//					}
//				}
//			});
//	};

//	function buChangeEvent(){
//		$("#bu_sel").change(function(){
//			$("#bu_name").text($(this).val());
//			$("#month-sel").val("");
//			$("#pdu-sel").val("");
//			$("#area-sel").val("");
//			$("#type_sel").val("");
//			$("#projNo").val("");
//			$("#projName").val("");
//			$("#projMgmr").val("");
//			if($("#divTable").css("display") == 'block'){
//				loadGridData(true);
//			}
//			initProjIndicators($(this).val());
//		})
//	};
	
/*
查询方法备份
	function initSelectOpts(datas, name, id, defaults){
		var bus = _.uniq(_.pluck(datas, name));
		var buopt = '<option value=""></option>';
		_.each(bus, function(value, index){
			if(value != '' &&value != null && value != 'null'){
				buopt += '<option value="'+ value +'">' + value + '</option>';
			}
		})
		$("#" + id).html(buopt);
		if(defaults){
			$("#" + id).val(defaults);
		}
	};

	function loadGridData(init, sort, order){
		var url = getRootPath() + "/bu/projDetailTab";
		if(sort){
			url += "?sort="+ sort + "&order=" + order;
		}
		   $.ajax({
		        url: url,
		        type: 'POST',
		        data: {
					'month': $("#month-sel").val(),
					'pdu': $("#pdu-sel").val(),
					'type': $("#type-sel").val(),
					'area': $("#area-sel").val(),
					'no': $("#projNo").val(),
					'name': $("#projName").val(),
					'pm': $("#projMgmr").val()
		        },
		        success: function(data){
		            var options = {
		                rownumbers: false,
		                striped: true,
		                pagination : true,
		                fitColumns:false,
		                nowrap: true,
						pagePosition : 'bottom',
						onSortColumn: function(field, order){
							loadGridData(false, field, order);
						},
			            onLoadSuccess:function(data){   
			                $('#projSummaryTable').datagrid('doCellTip',{cls:{},delay:1000, titleUnit:titleUnit, headerOverStyle:true});   
			            }  

		            };
		            titleUnit = data.titleUnit;
		            var wdth = Math.round(100 / data.gridTitles.length);
		            var lastWd = wdth * data.gridTitles.length;
		            lastWd = 100 > lastWd ? (wdth + 100 - lastWd) : ( wdth - lastWd + 100);
		           
		            _.each(data.gridTitles, function(val, index){
		            	val.sortable = true;
		            	val.width = wdth + '%';
		            	if(val.title == "项目名称"){
		            		val.formatter = function(val, row){
		            			return '<a style="color: #721b77;" title="'+val +'" href="' +getRootPath()+ '/bu/projView?projNo=' + row['项目编码'] + '&buName=' + $("#bu_sel").val() +'&a='+Math.random()+'">'+val+'</a>';
		            		};
		            	}
		            		
		            });
		            data.gridTitles[data.gridTitles.length -1].width = lastWd  + '%';
		            options.columns = [data.gridTitles];
		            $('#projSummaryTable').datagrid(options).datagrid('clientPaging');

					var p = $('#projSummaryTable').datagrid('getPager');
					$(p).pagination({
						pageSize : 20,// 每页显示的记录条数，默认为20
						pageList : [ 20, 30, 40 ],// 可以设置每页记录条数的列表
						beforePageText : '第',// 页数文本框前显示的汉字
						afterPageText : '页    共 {pages} 页',
						displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录',
					});
		
		        	var gridDatas = {};
		        	$.extend(true, gridDatas, data.gridDatas);
		            $('#projSummaryTable').datagrid("loadData", gridDatas);
		        	if(init){
		        		var datas = data.gridDatas.rows;
		        		if(!$("#month-sel").val()){
		        			initSelectOpts(datas, 'month', 'month-sel' );
		        		}
		        		if(!$("#pdu-sel").val()){
		        			initSelectOpts(datas, '产品部', 'pdu-sel', $("#pdu-sel").val());
		        		}
		        		if(!$("#area-sel").val()){
		        			initSelectOpts(datas, '地域', 'area-sel', $("#area-sel").val());
		        		}
		        		if(!$("#type-sel").val()){
		        			initSelectOpts(datas, '计费类型', 'type-sel', $("#type-sel").val());
		        		}
					}

		        }
		    })
	}
 */

/*	function initSelectOpts(datas, name, id, defaults){
		var bus = _.uniq(_.pluck(datas, name));
		var buopt = '<option value=""></option>';
		_.each(bus, function(value, index){
			if(value != '' &&value != null && value != 'null'){
				buopt += '<option value="'+ value +'">' + value + '</option>';
			}
		})
		$("#" + id).html(buopt);
		if(defaults){
			$("#" + id).val(defaults);
		}
	};
*/
	function loadGridData(init, sort, order){
		var url = getRootPath() + "/bu/projDetailTab";
		if(sort){
			url += "?sort="+ sort + "&order=" + order;
		}
		   $.ajax({
		        url: url,
		        type: 'POST',
		        async: false,//是否异步，true为异步
		        data: {
		        	'name': $("#projName").val(),
		        	'pm': $("#projMgmr").val(),
		        	'area': $("#usertype1").selectpicker("val")==null?null:$("#usertype1").selectpicker("val").join(),//转换为字符串
		        	'hwpdu': $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join(),//转换为字符串
					'hwzpdu': $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join(),//转换为字符串
					'pduSpdt': $("#usertype5").selectpicker("val")==null?null:$("#usertype5").selectpicker("val").join(),//转换为字符串
					'bu': $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join(),//转换为字符串
        			'pdu': $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join(),//转换为字符串
        			'du': $("#usertype8").selectpicker("val")==null?null:$("#usertype8").selectpicker("val").join(),//转换为字符串
        			'projectState': $("#usertype2").selectpicker("val")==null?null:$("#usertype2").selectpicker("val").join()//转换为字符串
		        },
		        success: function(data){
		            var options = {
		                rownumbers: false,
		                striped: true,
		                pagination : true,
		                fitColumns:false,
		                nowrap: true,
						pagePosition : 'bottom',
						onSortColumn: function(field, order){
							loadGridData(false, field, order);
						},
			            onLoadSuccess:function(data){   
			                $('#projSummaryTable').datagrid('doCellTip',{cls:{},delay:1000, titleUnit:titleUnit, headerOverStyle:true});   
			            }  

		            };
		            titleUnit = data.titleUnit;
		            var wdth = Math.round(100 / data.gridTitles.length);
		            var lastWd = wdth * data.gridTitles.length;
		            lastWd = 100 > lastWd ? (wdth + 100 - lastWd) : ( wdth - lastWd + 100);
		           
		            _.each(data.gridTitles, function(val, index){
		            	val.sortable = true;
		            	val.width = wdth + '%';
		            	if(val.title == "项目名称"){
		            		val.formatter = function(val, row){
		            			return '<a style="color: #721b77;" title="'+val +'" href="' +getRootPath()+ '/bu/projView?projNo=' + row['项目编码'] + '&a='+Math.random()+'">'+val+'</a>';
		            		};
		            	}
		            		
		            });
		            data.gridTitles[data.gridTitles.length -1].width = lastWd  + '%';
		            options.columns = [data.gridTitles];
		            $('#projSummaryTable').datagrid(options).datagrid('clientPaging');

					var p = $('#projSummaryTable').datagrid('getPager');
					$(p).pagination({
						pageSize : 10,// 每页显示的记录条数，默认为10
						pageList : [ 10, 20, 30, 40 ],// 可以设置每页记录条数的列表
						beforePageText : '第',// 页数文本框前显示的汉字
						afterPageText : '页    共 {pages} 页',
						displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录',
					});
		
		        	var gridDatas = {};
		        	$.extend(true, gridDatas, data.gridDatas);
		            $('#projSummaryTable').datagrid("loadData", gridDatas);
//		        	if(init){
//		        		var datas = data.gridDatas.rows;
//		        		if(!$("#month-sel").val()){
//		        			initSelectOpts(datas, 'month', 'month-sel' );
//		        		}
//		        		if(!$("#pdu-sel").val()){
//		        			initSelectOpts(datas, '产品部', 'pdu-sel', $("#pdu-sel").val());
//		        		}
//		        		if(!$("#area-sel").val()){
//		        			initSelectOpts(datas, '地域', 'area-sel', $("#area-sel").val());
//		        		}
//		        		if(!$("#type-sel").val()){
//		        			initSelectOpts(datas, '计费类型', 'type-sel', $("#type-sel").val());
//		        		}
//					}
		        }
		    })
	}

//	function bindChangeModelEvent() {
//		$("#changeStatusBtn").click(function(){
//	        var obj = document.getElementById('btnChange');
//	        if (obj.innerHTML == '切换为列表') {
//	            obj.innerHTML = '切换为图表';
//	            document.getElementById('divChart').style.display = "none";
//	            document.getElementById('divTable').style.display = "block";
//	            loadGridData(true);
//	        }
//	        else {
//	            obj.innerHTML = '切换为列表';
//	            document.getElementById('divChart').style.display = "block";
//	            document.getElementById('divTable').style.display = "none";
//	        }
//		})
//	};

	function bindQueryEvent(){
		$("#queryBtn").click(function(){
			loadGridData(false);
		})
	};
	
	function bindExportEvent(){
		$("#exportBtn").click(function(){
			var $eleForm = $("<form method='POST'></form>");
			var areaVal=$("#usertype1").selectpicker("val")==null?"":$("#usertype1").selectpicker("val").join();
			var projectStateVal=$("#usertype2").selectpicker("val")==null?"":$("#usertype2").selectpicker("val").join();
			var hwpduVal=$("#usertype3").selectpicker("val")==null?"":$("#usertype3").selectpicker("val").join();
			var hwzpduVal=$("#usertype4").selectpicker("val")==null?"":$("#usertype4").selectpicker("val").join();
			var pduSpdtVal=$("#usertype5").selectpicker("val")==null?"":$("#usertype5").selectpicker("val").join();
			var buVal=$("#usertype6").selectpicker("val")==null?"":$("#usertype6").selectpicker("val").join();
			var pduVal=$("#usertype7").selectpicker("val")==null?"":$("#usertype7").selectpicker("val").join();
			var duVal=$("#usertype8").selectpicker("val")==null?"":$("#usertype8").selectpicker("val").join();
            $eleForm.attr("action",getRootPath() + "/bu/download");
            $eleForm.append($('<input type="hidden" name="name" value="'+ $("#projName").val() +'">'));
            $eleForm.append($('<input type="hidden" name="pm" value="'+ $("#projMgmr").val() +'">'));
            $eleForm.append($('<input type="hidden" name="area" value="'+ areaVal +'">'));
            $eleForm.append($('<input type="hidden" name="projectState" value="'+ projectStateVal +'">'));
            $eleForm.append($('<input type="hidden" name="hwpdu" value="'+ hwpduVal +'">'));
            $eleForm.append($('<input type="hidden" name="hwzpdu" value="'+ hwzpduVal +'">'));
            $eleForm.append($('<input type="hidden" name="pduSpdt" value="'+ pduSpdtVal +'">'));
            $eleForm.append($('<input type="hidden" name="bu" value="'+ buVal +'">'));
            $eleForm.append($('<input type="hidden" name="pdu" value="'+ pduVal +'">'));
            $eleForm.append($('<input type="hidden" name="du" value="'+ duVal +'">'));
            $(document.body).append($eleForm);
            //提交表单，实现下载
            $eleForm.submit();
		})
	};

//	function initIndicatorUL(ulName, indicator, colName){
//		var data = indicator;
//		var wtds_ul = "";
//		_.each(data, function(val, index){
//			wtds_ul += '<li><div><a href="' +getRootPath()+ '/bu/projView?projNo=' + val.no  + '&buName=' + $("#bu_sel").val() + '"><span title="'+ val.name + '">' + (index+1) + ' ' +  val.name +'</span></a></div><b>'
//				+ val.value +'</b></li>';
//		});
//		$("#"+ulName).html(wtds_ul);
//	};
//
//	function initProjIndicators(){
//		$.ajax({
//				url: getRootPath() + "/bu/projIndicators",
//				type: 'post',
//				data: {
//					bu: $("#bu_sel").val(),
//					wtdhgbtggs: $('#wtds-div').text(),
//					bbzcssbcs: $('#lltsbyls-div').html(),
//					wswts: $('#ddqxs-div').html(),
//					dml: $('#dmzl-div').html(),
//					sdylzxxl: $('#zxyls-div').html(),
//					ddckwtjjl: $('#ddqxxgxl-div').html()
//				},
//				success: function (data) {
//					if(data){
//						initIndicatorUL('wtds', data.wtds);
//						initIndicatorUL('lltsbyls', data.llt);
//						initIndicatorUL('ddqxs', data.ddqxs);
//						initIndicatorUL('dmzl', data.xmdml);
//						initIndicatorUL('zxyls', data.zxyls);
//						initIndicatorUL('ddqxxgxl', data.ddqxxgxl);
//					}
//				}
//			});
//	};
	
//	function sortBy(rev){
//	    //第二个参数没有传递 默认升序排列
//	    if(rev ==  undefined){
//	        rev = 1;
//	    }else{
//	        rev = (rev) ? 1 : -1;
//	    }
//	    return function(a,b){
//
//	        if(a < b){
//	            return rev * -1;
//	        }
//	        if(a > b){
//	            return rev * 1;
//	        }
//	        return 0;
//	    }
//	};
	
//	function initLeftNav(){
//		$.ajax({
//				url: getRootPath() + "/bu/ProjCategory",
//				type: 'post',
//				success: function (data) {
//					var nav_ul = "";
//					_.map(data, function(val, key){
//						nav_ul = $('<li><a href="index.html" class="inactive"><i class="num-3"></i>'+ key + '</a>'
//                    			+'<ul style="display: none"></ul></li>');
//                    	$("#leftNav").append(nav_ul);
//                    	var bu_ul = "";
//                    	_.map(val, function(bus, buName){
//                    		bu_ul = $('<li><a href="javascript:void(0)" class="active">'+ buName  +'</a> </li>');
//                    		$(nav_ul).find('ul').append(bu_ul);
//                    		var tbl = $('<div class="item">'+
//                            	'<table width="100%" border="0" cellspacing="0" cellpadding="0"></table></div>');
//                    		$(bu_ul).find('a').after(tbl);
//                    		var $tr = '';
//                    		var months = [];
//                    		_.map(bus, function(n, i){
//                    			months.push(i);
//                    		});
//                    		months.sort(sortBy(false));
//                    		_.each(months, function(data, index){ //阻止JS中MAP自动排序
//                    			_.map(bus, function(monList, mon){
//                    				if(mon == data){
//	                        			$tr += ' <tr><td colspan="4"><div>'+ mon +'</div>  <hr /></td></tr>' + '<tr>';
//	                        			_.each(monList, function(po, index){
//	                        				$tr += '<td width="25%" valign="middle"> <a href="' +getRootPath()+ '/bu/projView?projNo=' + po.no + '&buName=' + key +'" class="current">'+po.name+'</a></td>';
//	                        				if( index > 0 && index % 3 == 0){
//	                        					$tr += '</tr><tr>';
//	                        				}
//	                        			})
//	                        			$tr += '</tr>';
//                    				}
//                        		})
//                    		})
//                    	
//                    		$(tbl).find('table').append($($tr));
//                    	})
//					})
//					initDownMenu();
//					initMouseOverMenu();
//				}
//		})
//	};
	
	function getCookie(name){
		   var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
		   if(arr != null){
		     return unescape(arr[2]); 
		   }else{
		     return null;
		   }
	};
	
	function initArea() {
		$.ajax({
			url: getRootPath() + '/bu/area',
			type: 'post',
			async: false,//是否异步，true为异步
			success: function (data) {
				var select_option="";
				$("#usertype1").empty();
				_.each(data, function(val, index){//(值，下标)
					select_option += "<option value="+val+">"+val+"</option>";
				})
				$("#usertype1").html(select_option);
			}
		});
	};	
	
	function initYeWuXian() {//默认加载业务线
		$.ajax({
			url: getRootPath() + '/bu/getZhongruanYWX',
			type: 'post',
			async: false,//是否异步，true为异步
			success: function (data) {
				var select_option="";
				$("#usertype6").empty();
				_.each(data, function(val, index){//(值，下标)
					select_option += "<option value="+data[index].YWXID+">"+data[index].YWXNAME+"</option>";
				})
				$("#usertype6").html(select_option);
			}
		});
	};	
	
	function querySYB(){//根据业务线选择结果显示事业部
		$("#usertype6").change(function(){
			$.ajax({
				url: getRootPath() + '/bu/getZhongruanSYB',
				type: 'post',
				async: false,//是否异步，true为异步
				data:{
					ywxval: $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join()//转换为字符串
				},
				success: function (data) {
//					var ul_li="";
//					var select_option="";
					$('#usertype7').selectpicker("val",[]);
					$("#usertype7").empty();
					$('#usertype7').prev('div.dropdown-menu').find('ul').empty();
					$('#usertype8').selectpicker("val",[]);
					$("#usertype8").empty();
					$('#usertype8').prev('div.dropdown-menu').find('ul').empty();
					$('#usertype3').selectpicker("val",[]);
					$('#usertype4').selectpicker("val",[]);
					$("#usertype4").empty();
					$('#usertype4').prev('div.dropdown-menu').find('ul').empty();
					$('#usertype5').selectpicker("val",[]);
					$("#usertype5").empty();
					$('#usertype5').prev('div.dropdown-menu').find('ul').empty();
//					_.each(data, function(val, index){//(值，下标)
//						ul_li += "<li data-original-index="+index+"><a tabindex='0' class data-tokens='null' role='option' aria-disabled='false' aria-selected='false'><span class='text'>"+data[index].SYBNAME+"</span><span class='glyphicon glyphicon-ok check-mark'></span></a></li>" 
//						select_option += "<option value="+data[index].SYBID+">"+data[index].SYBNAME+"</option>";
//					})
//					$('#usertype7').prev('div.dropdown-menu').find('ul').html(ul_li);
//					$("#usertype7").html(select_option);
					for (var i = 0; i < data.length; i++) {  
			            $('#usertype7').append("<option value=" + data[i].SYBID + ">" + data[i].SYBNAME + "</option>");  
			        } 
			        $('#usertype7').selectpicker('refresh');  
			        $('#usertype7').selectpicker('render'); 
				}
			});			
		})
	};
	
	function queryJFB(){//根据事业部选择结果显示交付部
		$("#usertype7").change(function(){
			$.ajax({
				url: getRootPath() + '/bu/getZhongruanJFB',
				type: 'post',
				async: false,//是否异步，true为异步
				data:{
					sybval: $("#usertype7").selectpicker("val")==null?null:$("#usertype7").selectpicker("val").join()//转换为字符串
				},
				success: function (data) {
//					var ul_li="";
//					var select_option="";
					$('#usertype8').selectpicker("val",[]);
					$("#usertype8").empty();
					$('#usertype8').prev('div.dropdown-menu').find('ul').empty();
//					_.each(data, function(val, index){//(值，下标)
//						ul_li += "<li data-original-index="+index+"><a tabindex='0' class data-tokens='null' role='option' aria-disabled='false' aria-selected='false'><span class='text'>"+data[index].JFBNAME+"</span><span class='glyphicon glyphicon-ok check-mark'></span></a></li>" 
//						select_option += "<option value="+data[index].JFBID+">"+data[index].JFBNAME+"</option>";
//					})
//					$('#usertype8').prev('div.dropdown-menu').find('ul').html(ul_li);
//					$("#usertype8").html(select_option);
					for (var i = 0; i < data.length; i++) {  
			            $('#usertype8').append("<option value=" + data[i].JFBID + ">" + data[i].JFBNAME + "</option>");  
			        } 
			        $('#usertype8').selectpicker('refresh');  
			        $('#usertype8').selectpicker('render'); 
				}
			});			
		})
	};
	
	function initHWCPX() {//默认加载华为产品线
		$.ajax({
			url: getRootPath() + '/bu/getHWCPX',
			type: 'post',
			async: false,//是否异步，true为异步
			success: function (data) {
				var select_option="";
				$("#usertype3").empty();
				_.each(data, function(val, index){//(值，下标)
					select_option += "<option value="+data[index].HWCPXID+">"+data[index].HWCPXNAME+"</option>";
				})
				$("#usertype3").html(select_option);
			}
		});
	};	

	function queryZCPX(){//根据华为产品线选择结果显示子产品线
		$("#usertype3").change(function(){
			$.ajax({
				url: getRootPath() + '/bu/getZCPX',
				type: 'post',
				async: false,//是否异步，true为异步
				data:{
					hwcpxval: $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join()//转换为字符串
				},
				success: function (data) {
//					var ul_li="";
//					var select_option="";
					$('#usertype4').selectpicker("val",[]);
					$("#usertype4").empty();
					$('#usertype4').prev('div.dropdown-menu').find('ul').empty();
					$('#usertype5').selectpicker("val",[]);
					$("#usertype5").empty();
					$('#usertype5').prev('div.dropdown-menu').find('ul').empty();
					$('#usertype6').selectpicker("val",[]);
					$('#usertype7').selectpicker("val",[]);
					$("#usertype7").empty();
					$('#usertype7').prev('div.dropdown-menu').find('ul').empty();
					$('#usertype8').selectpicker("val",[]);
					$("#usertype8").empty();
					$('#usertype8').prev('div.dropdown-menu').find('ul').empty();
//					_.each(data, function(val, index){//(值，下标)
//						ul_li += "<li data-original-index="+index+"><a tabindex='0' class data-tokens='null' role='option' aria-disabled='false' aria-selected='false'><span class='text'>"+data[index].ZCPXNAME+"</span><span class='glyphicon glyphicon-ok check-mark'></span></a></li>" 
//						select_option += "<option value="+data[index].ZCPXID+">"+data[index].ZCPXNAME+"</option>";
//					})
//					$('#usertype4').prev('div.dropdown-menu').find('ul').html(ul_li);
//					$("#usertype4").html(select_option);
					for (var i = 0; i < data.length; i++) {  
			            $('#usertype4').append("<option value=" + data[i].ZCPXID + ">" + data[i].ZCPXNAME + "</option>");  
			        } 
			        $('#usertype4').selectpicker('refresh');  
			        $('#usertype4').selectpicker('render'); 
				}
			});			
		})
	};

	function queryPDUorSPDT(){//根据子产品线选择结果显示PDU/SPDT
		$("#usertype4").change(function(){
			$.ajax({
				url: getRootPath() + '/bu/getPDUorSPDT',
				type: 'post',
				async: false,//是否异步，true为异步
				data:{
					zcpxval: $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join()//转换为字符串
				},
				success: function (data) {
//					var ul_li="";
//					var select_option="";
					$('#usertype5').selectpicker("val",[]);
					$("#usertype5").empty();
					$('#usertype5').prev('div.dropdown-menu').find('ul').empty();
//					_.each(data, function(val, index){//(值，下标)
//						ul_li += "<li data-original-index="+index+"><a tabindex='0' class data-tokens='null' role='option' aria-disabled='false' aria-selected='false'><span class='text'>"+data[index].PDUNAME+"</span><span class='glyphicon glyphicon-ok check-mark'></span></a></li>" 
//						select_option += "<option value="+data[index].PDUID+">"+data[index].PDUNAME+"</option>";
//					})
//					$('#usertype5').prev('div.dropdown-menu').find('ul').html(ul_li);
//					$("#usertype5").html(select_option);
					for (var i = 0; i < data.length; i++) {  
			            $('#usertype5').append("<option value=" + data[i].PDUID + ">" + data[i].PDUNAME + "</option>");  
			        } 
			        $('#usertype5').selectpicker('refresh');  
			        $('#usertype5').selectpicker('render'); 
				}
			});			
		})
	};
	
	
	
	
	//根据华为产品线选择结果显示子产品线
	function getZCPXData(){
		$.ajax({
			url: getRootPath() + '/bu/getZCPX',
			type: 'post',
			async: false,//是否异步，true为异步
			data:{
				hwcpxval: $("#usertype3").selectpicker("val")==null?null:$("#usertype3").selectpicker("val").join()//转换为字符串
			},
			success: function (data) {
//				var ul_li="";
//				var select_option="";
				$('#usertype4').selectpicker("val",[]);
//				$('#usertype4').prev('div.dropdown-menu').find('ul').empty();
				$("#usertype4").empty();
				$('#usertype5').selectpicker("val",[]);
//				$('#usertype5').prev('div.dropdown-menu').find('ul').empty();
				$("#usertype5").empty();
//				_.each(data, function(val, index){//(值，下标)
//					ul_li += "<li data-original-index="+index+"><a tabindex='0' class data-tokens='null' role='option' aria-disabled='false' aria-selected='false'><span class='text'>"+data[index].ZCPXNAME+"</span><span class='glyphicon glyphicon-ok check-mark'></span></a></li>" 
//					select_option += "<option value="+data[index].ZCPXID+">"+data[index].ZCPXNAME+"</option>";
//				})
//				$('#usertype4').prev('div.dropdown-menu').find('ul').html(ul_li);
//				$("#usertype4").html(select_option);
				for (var i = 0; i < data.length; i++) {  
		            $('#usertype4').append("<option value=" + data[i].ZCPXID + ">" + data[i].ZCPXNAME + "</option>");  
		        } 
		        $('#usertype4').selectpicker('refresh');  
		        $('#usertype4').selectpicker('render'); 
			}
		});
	}
	//根据子产品线选择结果显示PDU/SPDT		
	function getPDUorSPDTData(){//根据子产品线选择结果显示PDU/SPDT		
		$.ajax({
			url: getRootPath() + '/bu/getPDUorSPDT',
			type: 'post',
			async: false,//是否异步，true为异步
			data:{
				zcpxval: $("#usertype4").selectpicker("val")==null?null:$("#usertype4").selectpicker("val").join()//转换为字符串
			},
			success: function (data) {
//					var ul_li="";
//					var select_option="";
				$('#usertype5').selectpicker("val",[]);
//					$('#usertype5').prev('div.dropdown-menu').find('ul').empty();
				$("#usertype5").empty();
//					_.each(data, function(val, index){//(值，下标)
//						ul_li += "<li data-original-index="+index+"><a tabindex='0' class data-tokens='null' role='option' aria-disabled='false' aria-selected='false'><span class='text'>"+data[index].PDUNAME+"</span><span class='glyphicon glyphicon-ok check-mark'></span></a></li>" 
//						select_option += "<option value="+data[index].PDUID+">"+data[index].PDUNAME+"</option>";
//					})
//					$('#usertype5').prev('div.dropdown-menu').find('ul').html(ul_li);
//					$("#usertype5").html(select_option);
				for (var i = 0; i < data.length; i++) {  
		            $('#usertype5').append("<option value=" + data[i].PDUID + ">" + data[i].PDUNAME + "</option>");  
		        } 
		        $('#usertype5').selectpicker('refresh');  
		        $('#usertype5').selectpicker('render'); 
			}
		});
	}
	
	function getZhongruanSYBData(){//根据业务线选择结果显示事业部		
			$.ajax({
				url: getRootPath() + '/bu/getZhongruanSYB',
				type: 'post',
				async: false,//是否异步，true为异步
				data:{
					ywxval: $("#usertype6").selectpicker("val")==null?null:$("#usertype6").selectpicker("val").join()//转换为字符串
				},
				success: function (data) {
//					var ul_li="";
//					var select_option="";
					$('#usertype7').selectpicker("val",[]);
//					$('#usertype7').prev('div.dropdown-menu').find('ul').empty();
					$("#usertype7").empty();
					$('#usertype8').selectpicker("val",[]);
//					$('#usertype8').prev('div.dropdown-menu').find('ul').empty();
					$("#usertype8").empty();
//					_.each(data, function(val, index){//(值，下标)
//						ul_li += "<li data-original-index="+index+"><a tabindex='0' class data-tokens='null' role='option' aria-disabled='false' aria-selected='false'><span class='text'>"+data[index].SYBNAME+"</span><span class='glyphicon glyphicon-ok check-mark'></span></a></li>" 
//						select_option += "<option value="+data[index].SYBID+">"+data[index].SYBNAME+"</option>";
//					})
//					$('#usertype7').prev('div.dropdown-menu').find('ul').html(ul_li);
//					$("#usertype7").html(select_option);
					for (var i = 0; i < data.length; i++) {  
			            $('#usertype7').append("<option value=" + data[i].SYBID + ">" + data[i].SYBNAME + "</option>");  
			        } 
			        $('#usertype7').selectpicker('refresh');  
			        $('#usertype7').selectpicker('render'); 
				}
			});
	};
	
	
	
	function projectNum(){
		$.ajax({
			url: getRootPath() + '/bu/getProjectNum',
			type: 'post',
			async: false,
			success: function (data) {
				$("#countProject").text(data.COUNTPROJECT);
			}
		})
	}


	$(document).ready(function(){
		$('#userName').text(getCookie('username'));
		$('#usertype2').selectpicker("val",["在行"]);
//		initBuSel();
//		initLeftNav();
		projectNum();
		initArea();
		initHWCPX();
		initYeWuXian();
		queryZCPX();
		querySYB();
		queryPDUorSPDT();
		queryJFB();
//		buChangeEvent();
//		bindChangeModelEvent();
		bindQueryEvent();
		bindExportEvent();
//		loadGridData(false);
		var areaName=$(".list .active .areaActive",parent.document).text();
		if(''!=areaName&&undefined!=areaName){			
			$('#usertype1').selectpicker('val',areaName); 
			$('#usertype1').selectpicker('refresh');
			$('#queryBtn').click();
		}
		var depId=$(".crumbs .active",parent.document).attr("data-id");
		
		if(''!=depId&&undefined!=depId){		
			if(1==depId){
				var yijiId=$(".list .yiji>.active",parent.document).attr("data-id");
				var erjiId=$(".list .erji>.active",parent.document).attr("data-id");
				var sanjiId=$(".list .sanji>.active",parent.document).attr("data-id");				
				if(''!=erjiId&&undefined!=erjiId){
					$('#usertype6').selectpicker('val',erjiId);	
					$('#usertype6').selectpicker('refresh');
					$('#usertype6').selectpicker('render'); 
					getZhongruanSYBData();
					if(''!=sanjiId&&undefined!=sanjiId){
						$('#usertype7').selectpicker('val',sanjiId);	
						$('#usertype7').selectpicker('refresh');
						$('#usertype7').selectpicker('render'); 
					}
				}				
			}else if(2==depId){
				var yijiId=$(".list .yiji>.active",parent.document).attr("data-id");
				var erjiId=$(".list .erji>.active",parent.document).attr("data-id");
				var sanjiId=$(".list .sanji>.active",parent.document).attr("data-id");
				if(''!=yijiId&&undefined!=yijiId){
					$('#usertype3').selectpicker('val',yijiId);
					$('#usertype3').selectpicker('refresh');
					$('#usertype3').selectpicker('render'); 
					getZCPXData();
					if(''!=erjiId&&undefined!=erjiId){
						$('#usertype4').selectpicker('val',erjiId);	
						$('#usertype4').selectpicker('refresh');
						$('#usertype4').selectpicker('render'); 
						getPDUorSPDTData();
						if(''!=sanjiId&&undefined!=sanjiId){
							$('#usertype5').selectpicker('val',sanjiId);	
							$('#usertype5').selectpicker('refresh');
							$('#usertype5').selectpicker('render'); 
						}
					}
				}		
			}
			$('#queryBtn').click();
		}
		
		showDateTime();
	})
})()