<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>可视化度量平台</title>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/resources/bootstrap/bootstrap.min.css" />
    <link rel="stylesheet" href="<%=request.getContextPath() %>/view/HTML/css/DefaultSkin.css" type="text/css" />
    <script type="text/javascript" src="<%=request.getContextPath() %>/view/HTML/js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath() %>/resources/jquery/jquery.form.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath() %>/resources/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath() %>/view/HTML/js/echarts.common.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath() %>/resources/js/comm/comm.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath() %>/resources/underscore/underscore.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath() %>/resources/bootstrap/bootstrap.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath() %>/view/HTML/js/xiangmu.js"></script>
    <style>
	.trans {
		display: none;
		width: 54px;
		height: 37px;
		opacity: 0;
		position: relative;
		margin-top: -34px;
		cursor: pointer;
	}
	.main-topmenu1{
		background: #fff;
	}
	a:hover{
		text-decoration: none;
		cursor: pointer;
	}
	.main-topmenu a:hover{
		text-decoration: none;
	}
</style>
    <script type="text/javascript">
    var projNo = getQueryString("projNo");
    var projBU = '${proj.bu}';
    var projPM = '{proj.pm}';
    var projName ='';
    var userName =''; //用户名
    var startDate = '${proj.startDate}';
    var endDate = '${proj.endDate}';
    function changeFrameHeight() {
        var ifm = document.getElementById("myiframe");
        ifm.height = document.documentElement.clientHeight-100;
    };
    window.onresize=function(){ changeFrameHeight();}
    $(function(){changeFrameHeight();});
    function changePage(page) {
        var gaikuang = document.getElementById("gaikuang");
        var jiaofujieguo = document.getElementById("jiaofujieguo");
        var jiaofuguocheng = document.getElementById("jiaofuguocheng");
        var gongchengnengli = document.getElementById("gongchengnengli");
        var gerennengli = document.getElementById("gerennengli");
        var ifm = document.getElementById("myiframe");
        var wentifengxian= document.getElementById("wentifengxian");
        gaikuang.className = "";
        jiaofujieguo.className = "";
        jiaofuguocheng.className = "";
        gongchengnengli.className = "";
        gerennengli.className = "";
        wentifengxian.className = "";
        
        if (page == 0) {
            gaikuang.className = "sel";
            ifm.src = "<%=request.getContextPath() %>/view/HTML/gaikuang.html";
        }
        else if (page == 1) {
            jiaofujieguo.className = "sel";	
            ifm.src = "<%=request.getContextPath() %>/view/HTML/jiaofujieguo.html";
        }
        else if (page == 2) {
            jiaofuguocheng.className = "sel";
            ifm.src = "<%=request.getContextPath() %>/view/HTML/jiaofuguocheng.html";
        }
        else if (page == 3) {
            gongchengnengli.className = "sel";
            ifm.src = "<%=request.getContextPath() %>/view/HTML/gongchengnengli.html";
        }
        else if (page == 4) {
            gerennengli.className = "sel";
            ifm.src = "<%=request.getContextPath() %>/view/HTML/gerennengli.html";
        }
        else if (page == 5) {
        	wentifengxian.className = "sel";
        	ifm.src = "<%=request.getContextPath() %>/view/HTML/wentifengxian.html";
        }
        return false;
    };
	function sortBy(rev){
	    //第二个参数没有传递 默认升序排列
	    if(rev ==  undefined){
	        rev = 1;
	    }else{
	        rev = (rev) ? 1 : -1;
	    }
	    return function(a,b){

	        if(a < b){
	            return rev * -1;
	        }
	        if(a > b){
	            return rev * 1;
	        }
	        return 0;
	    }
	};
	function initLeftNav(){
		$.ajax({
				url: getRootPath() + "/bu/ProjCategory",
				type: 'post',
				success: function (data) {
					var nav_ul = "";
					_.map(data, function(val, key){
						nav_ul = $('<li><a href="index.html" class="inactive"><i class="num-3"></i>'+ key + '</a>'
                    			+'<ul style="display: none"></ul></li>');
                    	$("#leftNav").append(nav_ul);
                    	var bu_ul = "";
                    	_.map(val, function(bus, buName){
                    		bu_ul = $('<li><a href="javascript:void(0)" class="active">'+ buName  +'</a> </li>');
                    		$(nav_ul).find('ul').append(bu_ul);
                    		var tbl = $('<div class="item">'+
                            	'<table width="100%" border="0" cellspacing="0" cellpadding="0"></table></div>');
                    		$(bu_ul).find('a').after(tbl);
                    		var $tr = '';
                    		var months = [];
                    		_.map(bus, function(n, i){
                    			months.push(i);
                    		});
                    		months.sort(sortBy(false));
                    		_.each(months, function(data, index){ //阻止JS中MAP自动排序
                    			_.map(bus, function(monList, mon){
                    				if(mon == data){
	                        			$tr += ' <tr><td colspan="4"><div>'+ mon +'</div>  <hr /></td></tr>' + '<tr>';
	                        			_.each(monList, function(po, index){
	                        				$tr += '<td width="25%" valign="middle"> <a href="' +getRootPath()+ '/bu/projView?projNo=' + po.no + '&buName=' + key +'" class="current">'+po.name+'</a></td>';
	                        				if( index > 0 && index % 3 == 0){
	                        					$tr += '</tr><tr>';
	                        				}
	                        			})
	                        			$tr += '</tr>';
                    				}
                        		})
                    		})
                    	
                    		$(tbl).find('table').append($($tr));
                    	})
					})
					
					initDownMenu();
					initMouseOverMenu();
						
				}
		})
		
		
	};
	
	function getProName(){
		$.ajax({
			url: getRootPath() + '/bu/projOverviewData',
			type: 'post',
			data:{
				no : projNo
			},
			success: function (data) {
				projName =data.name; 
				$('#userName').text(getCookie('username'));
				$('#projName').text(projName);
			}
		});
	};
	
	function getUpdate(){
		$.ajax({
			url: getRootPath() + '/svnTask/searchUpdateTime',
			type: 'post',
			async: false,
			data:{
				projNo : projNo
			},
			success: function (data) {
				$('#update').append(new Date(data).format('yyyy-MM-dd hh:mm:ss'));
			}
		});
	};
	
	function saveUrl() {
		var url = $("#svnurl").val();
		var version = $("#dtsurl").val();
		var username=getCookie('username');
		var giturl = $("#giturl").val();
		var isourl = $("#isourl").val(); 
		var ciurl = $("#ciurl").val(); 
		$.ajax({
			url: getRootPath() + '/svnTask/saveurl',
			type: 'post',
			data:{
				    no : projNo,
				   url : url,
			   	  user : username,
			   version : version,
			    giturl : giturl,
			    isourl : isourl,
			     ciurl : ciurl,
			},
			success: function () {
				//$('#update').append(new Date(data).format('yyyy-MM-dd hh:mm:ss'));
				$("#configuroute").modal("hide");
			}
		});
	};
	
	function getProJd(){
		$.ajax({
			url: getRootPath() + '/project/searchprojdsinfo',
			type: 'post',
			data:{
				no : projNo
			},
			success: function (data) {
				debugger;
				$("#svnurl").val(data.url);
				$("#dtsurl").val(data.version);
				$("#giturl").val(data.giturl);
				$("#isourl").val(data.isourl);
				$("#ciurl").val(data.ciurl);
			}
		});
	};
	function getCookie(name){
		   var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
		   if(arr != null){
		     return unescape(arr[2]); 
		   }else{
		     return null;
		   }
	};
	
        $(document).ready(function () {debugger;
        	if(startDate != ""){
        		$("#startDate").text(new Date(startDate).format('yyyy-MM-dd'));
        	}       	
        	if(endDate != ""){
    		$("#endDate").text(new Date(endDate).format('yyyy-MM-dd'));
        	}
        	getUpdate();
        	getProName();
    		initLeftNav();
        })
    </script>
</head>
<body>
    <div class="top" style="display: none;">
        <div class="logo">
            <a href="<%=request.getContextPath() %>/view/HTML/index.html">
                <img src="<%=request.getContextPath() %>/view/HTML/images/logo.png" /></a></div>
        <div class="daohang-index" style="float: right;">
            <%-- <a href="<%=request.getContextPath() %>/view/HTML/index.html"><i class="i_shouye"></i>首页</a><img src="<%=request.getContextPath() %>/view/HTML/images/fengexian.png" />
            <a href="<%=request.getContextPath() %>/view/mainFrame/page.html"><i class="i_guanli"></i>管理</a> --%>
            <span style="font-size: 15px;color: white;line-height: 80px;">欢迎您，</span><span id= 'userName' style="font-size: 15px;color: white;line-height: 80px;"></span><div style="width: 1.5px;height: 35px;background-color: white;display:inline-block;margin-left: 20px;margin-bottom: -10px;"></div>
            <span style="font-size: 15px;color: white;line-height: 80px;margin-left: 20px;">角色</span><div style="width:0; height:0; border:5px #ffffff solid; border-bottom-width:0; border-left-color:#2e8afc; border-right-color:#2e8afc;display: inline-block;margin-left: 10px;margin-bottom: 3px;"></div>
        </div>
        <!--<div class="login">
            <input name="" type="text" class="login-input" /><input name="" type="button" value="登录"
                class="login-button" /><input name="" type="button" value="注册" class="login-button" />
        </div>-->
    </div>
    <!-- <div class="left">
        <div class="list">
        <ul class="yiji" id="leftNav">
            </ul>
    </div> -->
    <div>
    <div class="main">
        <div class="title">
            <%-- <span>${proj.name}项目情况</span> --%>
            <span>
            	<select id="develop-sel" style="border:none; background-color: #eeeeee;">
            		<option id="projName" selected="selected" style="color: black"></option>
            	</select>
            </span>
            <span id= "update" style="font-size: 12px; float: right;margin-right: 10px;">数据更新时间:</span>
        </div>
        <div class="main-topmenu-line-xi" style= "border-top:1px #2e8afc solid; position:relative; margin: 0 10px 0 10px;">
        </div>
        <div class="main-topmenu-line-cu" style="border-top:3px #2e8afc solid; position:relative;top:1px; margin: 0 10px 0 10px;">
        </div>
        <div class="main-content" style="margin: 10px 10px 10px 10px">
        <%-- <table width="100%">
            <tr>
                <td valign="top" style="padding: 5px 5px 0px 0px;width:222px">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="xiangmu">
                        <tr>
                            <td colspan="2" style="height: 45px; background: url(<%=request.getContextPath() %>/view/HTML/images/bag.png) no-repeat; background-color: #9f701e;">
                                <div style="padding-left: 50px; padding-top: 18px; color: white; font-size: 16px">
                                    <b>项目信息</b></div>
                            </td>
                        </tr>
                        <tr>
                            <td width="35%" align="right">
                                <span style="color: #282828">项目编号：</span>
                            </td>
                            <td width="65%">
                                <span style="color: #0e6643; font-size: 14px;">${proj.no}</span>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                    <hr />
                            </td>
                        </tr>
                        <tr>
                            <td width="35%" align="right">
                                <span style="color: #282828">项目名称：</span>
                            </td>
                            <td width="65%">
                                <span style="color: #0e6643; font-size: 14px;">${proj.name}</span>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" style="vertical-align: middle; height: 30px; background-color: #d3c38c;">
                                <span style="color: #895a08; font-size: 14px; padding-left: 5px;">基础信息</span>
                            </td>
                        </tr>
                        <tr>
                           <td width="35%" align="right">
                                <span style="color: #414141;">事业部：</span>
                            </td>
                            <td width="65%">
                                <span style="color: #414141; font-size: 14px;">${proj.bu}</span>
                            </td>
                        </tr>
                             <tr>
                            <td colspan="2">
                                    <hr />
                            </td>
                        </tr>
                          <tr>
                           <td width="35%" align="right">
                                <span style="color: #414141;">产品：</span>
                            </td>
                            <td width="65%">
                                <span style="color: #414141; font-size: 14px;">${proj.pdu}</span>
                            </td>
                        </tr>
                             <tr>
                            <td colspan="2">
                                    <hr />
                            </td>
                        </tr>
                         <tr>
                           <td width="35%" align="right">
                                <span style="color: #414141;">交付部：</span>
                            </td>
                            <td width="65%">
                                <span style="color: #414141; font-size: 14px;">${proj.du}</span>
                            </td>
                        </tr>
                             <tr>
                            <td colspan="2">
                                    <hr />
                            </td>
                        </tr>
                        <tr>
                            <td width="35%" align="right">
                                <span style="color: #414141;">交付类型：</span>
                            </td>
                            <td width="65%">
                                <span style="color: #414141; font-size: 14px;">${proj.type}</span>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                    <hr/>
                            </td>
                        </tr>
                        <tr>
                            <td width="35%" align="right">
                                <span style="color: #414141">项目类型：</span>
                            </td>
                            <td width="65%">
                                <span style="color: #414141; font-size: 14px;">${proj.projectType}</span>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                    <hr />
                            </td>
                        </tr>
                        <tr>
                            <td width="35%" align="right">
                                <span style="color: #414141">开始日期：</span>
                            </td>
                            <td width="65%">
                                <span style="color: #414141; font-size: 14px;" id="startDate"></span>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                    <hr />
                            </td>
                        </tr>
                        <tr>
                            <td width="35%" align="right">
                                <span style="color: #414141">结束日期：</span>
                            </td>
                            <td width="65%">
                                <span style="color: #414141; font-size: 14px;" id="endDate"></span>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" style="vertical-align: middle; height: 30px; background-color: #d3c38c;">
                                <span style="color: #895a08; font-size: 14px; padding-left: 5px;">关键角色</span>
                            </td>
                        </tr>
                        <tr>
                            <td width="35%" align="right">
                                <span style="color: #414141">PM：</span>
                            </td>
                            <td width="65%">
                                <span style="color: #414141; font-size: 14px;">${proj.pm}</span>
                            </td>
                        </tr>
                        <tr>
                            <td width="35%" align="right">
                                <span style="color: #414141">QA：</span>
                            </td>
                            <td width="65%">
                                <span style="color: #414141; font-size: 14px;">${proj.qa}</span>
                            </td>
                        </tr>
                    </table> --%>
                </td>
                <td  valign="top">
                    <div>
                    	<div class="main-topmenu" style="float: left;height: 45px;">
	                        <a class="sel" id="gaikuang" onclick="changePage(0)">概况</a> <a id="jiaofujieguo"
	                            onclick="changePage()">需求</a><a id="gerennengli" onclick="changePage(4)">
	                            	开发</a><a id="jiaofuguocheng" onclick="changePage()">测试</a><a id="gongchengnengli" onclick="changePage()">验收</a><a id="wentifengxian" onclick="changePage(5)">问题/风险跟踪</a>
                       	</div> 
                        <span style="float: right; margin-right: 12px;margin-top: 5px;line-height: 40px;">    	
	                       	<img id="refrush" src="<%=request.getContextPath() %>/view/HTML/images/refreshicon.png" alt="刷新" width="17px" height="17px"/>&nbsp;&nbsp;
	                       	<a onclick="getProJd()" class="main-topmenu1" data-toggle="modal" data-target="#configuroute"><!-- 配置路径 -->
	                       		<img src="<%=request.getContextPath() %>/view/HTML/images/configuricon.png" alt="配置" width="17px" height="17px"/>
	                       	</a>&nbsp;&nbsp;
	                       	<a class="main-topmenu1" data-toggle="modal" data-target="#uploadDialog">
	                       		<img src="<%=request.getContextPath() %>/view/HTML/images/importicon.png" alt="导入" width="17px" height="17px"/>
	                       	</a>&nbsp;&nbsp;
	                       	<a id="downloadProj">
	                       		<img src="<%=request.getContextPath() %>/view/HTML/images/exporticon.png" alt="导出" width="17px" height="17px"/>
	                       	</a>
                       	</span>
                       	<div class="modal fade" id="uploadDialog" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
							<div class="modal-dialog">
								<div class="modal-content" style="top: 100px;">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×
										</button>
										<h4 class="modal-title" id="uploadModalLabel">
											上传项目指标数据
										</h4>
									</div>
									<div class="modal-body">
										<div style="height: 80px;">
											<div style="width:65%">
								                <div class="input-group" style="display: flex;align-items: center;">
								                   	 <div style="display:table-cell;width: 27%;">文件路径：</div><input type="text" readonly="readonly" id="filePathInfo" class="form-control">
								                    <span class="input-group-btn">
								                    	<form method="post" id="importForm"  enctype="multipart/form-data">
									                        <button class="btn btn-default" type="button" id="importBtn" style="margin-top: 4px;border-radius:0px">浏览</button>
									                        <input type="file" class="trans" id="importInput" name="file" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" ></input>
									                    </form>
								                    </span>
								                </div><!-- /input-group -->
								            </div><!-- /.col-lg-6 -->
								            <a id="templateDownload" style="width: 130px;border: 0px;" >下载手动数据模板</a>
										</div>
									</div>
									<div class="modal-footer">
										<button type="button" class="btn btn-primary" id="submitImport">
											确定
										</button>
										<button type="button" class="btn btn-default" data-dismiss="modal">
											取消
										</button>
				
									</div>
								</div><!-- /.modal-content -->
							</div><!-- /.modal-dialog -->
						</div><!-- /.modal -->
                       	<div class="modal fade" id="configuroute" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
							<div class="modal-dialog">
								<div class="modal-content" style="top: 100px;">
									<div class="modal-header" style="border-bottom-width: 5px;padding-top: 10px;padding-bottom: 10px;">
										<h4 class="modal-title" id="uploadModalLabel" style="display: inline; margin-right: 350px;">
											配置路径
										</h4>
										<button type="button" data-dismiss="modal" style="border:1px #2e8afc solid; background-color: white; color: #2e8afc; border-radius: 4px; width: 60px; height: 32px; margin-right: 15px;outline: none;">取消</button>
										<button type="button" id="saveUrl" data-dismiss="modal" style="border:1px #2e8afc solid; background-color: #2e8afc; color: white; border-radius: 4px; width: 60px; height: 32px;outline: none;"onclick="saveUrl()">保存</button>
									</div>
									<div class="modal-body" style="height: 250px;">
										<div style="height: 80px;">
											<div style="width:95%">
								                <div class="input-group" style="display: flex;align-items: center;">
							                    		<table style="width: 570px; height: 220px;">
							                    			<tr align="center" valign="middle">
							                    				<th width="100" style="text-align: center; font-size: 16px;">SVN路径</th>
							                    				<th><input type="text" name="svnurl" id="svnurl" style="height: 38px; width: 460px;" /></th>
							                    			</tr>
							                    			<tr align="center" valign="middle">
							                    				<th width="100" style="text-align: center; font-size: 16px;">DTS路径</th>
							                    				<th><input type="text" name="dtsurl" id="dtsurl" style="height: 38px; width: 460px;"placeholder="例如:product/R_version..." /></th>
							                    			</tr>
							                    			<tr align="center" valign="middle">
							                    				<th width="100" style="text-align: center; font-size: 16px;">GIT路径</th>
							                    				<th><input type="text" name="giturl" id="giturl" style="height: 38px; width: 460px;" /></th>
							                    			</tr>
							                    			<tr align="center" valign="middle">
							                    				<th width="100" style="text-align: center; font-size: 16px;">ISO路径</th>
							                    				<th><input type="text" name="isourl" id="isourl" style="height: 38px; width: 460px;" /></th>
							                    			</tr>
							                    			<tr align="center" valign="middle">
							                    				<th width="100" style="text-align: center; font-size: 16px;">CI路径</th>
							                    				<th><input type="text" name="ciurl" id="ciurl" style="height: 38px; width: 460px;" /></th>
							                    			</tr>
							                    		</table>
								                </div><!-- /input-group -->
								            </div><!-- /.col-lg-6 -->
										</div>
									</div>
								</div><!-- /.modal-content -->
							</div><!-- /.modal-dialog -->
						</div><!-- /.modal -->
						
						<div class="modal fade" id="jindu" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
							<div class="modal-dialog">
								<div class="modal-content" style="top: 100px;">
									<div class="modal-body" style="padding: 0px 5px 15px 15px;">
										<div id="successDiv" style="display: none;">
											<div style="height: 20px;">
												<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×
												</button>
											</div>
											<div style="text-align: center;margin: 10px;">
												<img src="<%=request.getContextPath() %>/view/HTML/images/success.png" alt="成功" width="17px" height="17px"/>
												<span>数据获取成功！</span>
											</div>
										</div>
										<div id="dataAcquisition" style="text-align: center;padding: 5px 10px 15px 0px;">
											<div id="dataAcquisitionDel" style="display: none;height: 20px;">
												<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×
												</button>
											</div>
											<span>数据采集中..</span>
											<img src="<%=request.getContextPath() %>/view/HTML/images/progressBar.gif" alt="进度条" width="550px" height="17px"/><br />
											<div style="margin-left: 10px;text-align: left;">
												<span>正在更新svn数据...</span><br />
												<span>正在更新dts数据...</span>
												<span id="svnDataUpdate" style="display: none;">正在更新svn数据<img src="<%=request.getContextPath() %>/view/HTML/images/success.png" alt="成功" width="17px" height="17px"/></span>
												<span id="dtsDataUpdate" style="display: none;">正在更新dts数据<img src="<%=request.getContextPath() %>/view/HTML/images/success.png" alt="成功" width="17px" height="17px"/></span>
											</div>
										</div>
									</div>
								</div><!-- /.modal-content -->
							</div><!-- /.modal-dialog -->
						</div><!-- /.modal -->
                    </div>
                    <div class="frame-content">
                        <iframe src="<%=request.getContextPath() %>/view/HTML/gaikuang.html" width="100%"  
                        id="myiframe" onload="changeFrameHeight()" style="border-color: #2e8afc;border-width: 2px;border-style: solid;"
                            frameborder="0"></iframe>
                    </div>
                </td>
            </tr>
        </table>
        </div>
    </div>
    </div>
</body>
</html>
