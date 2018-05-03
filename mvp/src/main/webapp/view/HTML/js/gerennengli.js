(function(){
	var projNo = window.parent.projNo;
	var buName = window.parent.projBU;
	function search(){
		$.ajax({
				url: getRootPath() + '/svnTask/searchGeRen',
				type: 'post',
				async: false,
				data: {
					projNo: projNo
				},
				success: function (data) {
					debugger
					var addNums = 0;
					var tab = "";
					$("#gerenCode tr:not(:first)").remove();
					if(!_.isEmpty(data)){
						var chartsData ={
								january:0,
								february:0,
								march:0,
								april:0,
								may:0,
								june:0,
								july:0,
								august:0,
								september:0,
								october:0,
								november:0,
								december:0
						};
						_.each(data, function(record, key){
							record.sum= record.january
							+record.february+record.march
							+record.april+record.may
							+record.june+record.july
							+record.august+record.september
							+record.october+record.november
							+record.december;
							if(record.sum!=0){
								tab += '<tr><td>'+ record.name +'</td>'
									   +'<td>'+ record.number +'</td>'
									   +'<td>'+ record.january +'</td>'
									   +'<td>'+ record.february +'</td>'
									   +'<td>'+ record.march +'</td>'
									   +'<td>'+ record.april +'</td>'
									   +'<td>'+ record.may +'</td>'
									   +'<td>'+ record.june +'</td>'
									   +'<td>'+ record.july +'</td>'
									   +'<td>'+ record.august +'</td>'
									   +'<td>'+ record.september +'</td>'
									   +'<td>'+ record.october +'</td>'
									   +'<td>'+ record.november +'</td>'
									   +'<td>'+ record.december +'</td>'
									   +'<td>'+ record.sum+'</td></tr>';
							}
							chartsData.january+=record.january;
							chartsData.february+=record.february;
							chartsData.march+=record.march;
							chartsData.april+=record.april;
							chartsData.may+=record.may;
							chartsData.june+=record.june;
							chartsData.july+=record.july;
							chartsData.august+=record.august;
							chartsData.september+=record.september;
							chartsData.october+=record.october;
							chartsData.november+=record.november;
							chartsData.december+=record.december;
						})
					}
					$("#gerenCode tbody").append(tab);
					var montharray = [chartsData.january,chartsData.february,chartsData.march,chartsData.april,chartsData.may,chartsData.june,chartsData.july,chartsData.august,chartsData.september,chartsData.october,chartsData.november,chartsData.december];
					option1.series=[{
						data:montharray
					}];
					myChart1.setOption(option1);
				}
			});
	
	};
	$(document).ready(function(){
		search();
	})
})()