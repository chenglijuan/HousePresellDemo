(function(baseInfo){
	var listVue = new Vue({
		el : baseInfo.listDivId,
		data : {
			interfaceVersion :19000101,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			theState:0,//正常为0，删除为1
			userStartId:null,
			userStartList:[],
			userRecordId:null,
			userRecordList:[],
			mainTableId:null,
			mainTableList:[],
			keyword:"",
			dateRange : "",
			errMsg : "",
			billTimeStamp : "",//记账日期
			endBillTimeStamp : "",//记账日期结束
			cashFlowReportsList : [],
			queryKindList : [
				{tableId:1,theName:"按日查询"},
				{tableId:2,theName:"按月查询"},
				{tableId:3,theName:"按年查询"},
			],
			queryKind : 1,
			dateType : 1,
			showChart :0,
			amountIn : 0.00,
			amountOut : 0.00,
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			getExportForm : getExportForm,//获取导出excel参数
			search:search,
			changePageNumber : function(data){
				if (listVue.pageNumber != data) {
					listVue.pageNumber = data;
					listVue.refresh();
				}
			},
			changeCountPerPage:function(data){
				if (listVue.countPerPage != data) {
					listVue.countPerPage = data;
					listVue.refresh();
				}
			},
			indexMethod : indexMethod,
			resetSearch : resetSearch,//重置
			exportForm : exportForm,//导出excel
			getQueryKind : function(data){
				this.queryKind = data.tableId;
				if(this.queryKind == 1){
					listVue.dateRange = null;
					this.dateType = 1;
				}else if(this.queryKind == 2){
					listVue.dateRange = null;
					this.dateType = 2;
					
				}else if(this.queryKind == 3){
					listVue.dateRange = null;
					this.dateType = 3;
					
				}
			},
			emptyQueryKind : function(){
				listVue.dateRange = null;
				this.queryKind = 1;
				this.dateType = 1;
			},
			tableHandle : tableHandle,
			pictureHandle : pictureHandle,
			formatNum : formatNum,
			
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue,
			'vue-listselect': VueListSelect,
		},
		watch:{
		}
	});
	
	//------------------------方法定义-开始------------------//
	//金额千分位
	function formatNum(strNum) {
		if (strNum.length <= 3) {
		return strNum;
		}
		if (!/^(\+|-)?(\d+)(\.\d+)?$/.test(strNum)) {
		return strNum;
		}
		var a = RegExp.$1, b = RegExp.$2, c = RegExp.$3;
		var re = new RegExp();
		re.compile("(\\d)(\\d{3})(,|$)");
		while (re.test(b)) {
		b = b.replace(re, "$1,$2$3");
		}
		return a + "" + b + "" + c;
		}
	//列表操作--------------获取"搜索列表"表单参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			pageNumber:this.pageNumber,
			countPerPage:this.countPerPage,
			totalPage:this.totalPage,
			keyword:this.keyword,
			theState:this.theState,
			userStartId:this.userStartId,
			userRecordId:this.userRecordId,
			mainTableId:this.mainTableId,
			reconciliationState : this.reconciliationState,
			billTimeStamp : this.billTimeStamp,
			endBillTimeStamp : this.endBillTimeStamp,
			queryKind : this.queryKind,
		}
	}

	function indexMethod(index) {
		return generalIndexMethod(index, listVue)
	}

	//列表操作--------------刷新
	function refresh()
	{
		new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				listVue.errMsg = jsonObj.info;
			}
			else
			{
				listVue.cashFlowReportsList=jsonObj.tg_LoanAmountStatement_ViewList;
				listVue.cashFlowReportsList.forEach(function(item){
					item.lastAmount = addThousands(item.lastAmount);
					item.loanAmountIn = addThousands(item.loanAmountIn);
					item.depositReceipt = addThousands(item.depositReceipt);
					item.payoutAmount = addThousands(item.payoutAmount);
					item.depositExpire = addThousands(item.depositExpire);
					item.currentBalance = addThousands(item.currentBalance);
				})
				
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount=jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				
				listVue.amountIn = addThousands(jsonObj.amountIn);
				listVue.amountOut = addThousands(jsonObj.amountOut);
				
				//动态跳转到锚点处，id="top"
				document.getElementById('cashFlowReportsDiv').scrollIntoView();
			}
		});
	}
	
	function getExportForm(){
		return{
		   interfaceVersion:this.interfaceVersion,	
		}
	}
	
	
	//列表操作-----------------------导出excel
	function exportForm(){
		new ServerInterface(baseInfo.exportInterface).execute(listVue.getSearchForm(), function(jsonObj){
			if (jsonObj.result != "success") 
			{
				generalErrorModal(jsonObj, jsonObj.info);
			} 
			else 
			{
				window.location.href="../"+jsonObj.fileURL;
			}
		});
	}
	
	
	//列表操作------------搜索
	function search()
	{
		$("#cashFlowTable").show();
		$("#chartCashFlow").hide();
		listVue.pageNumber = 1;
		refresh();
	}
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}

	function initData()
	{
		initButtonList();
	}
	function resetSearch(){
		listVue.dateRange = "";
		listVue.keyword = "";
		listVue.billTimeStamp = "";
	    listVue.endBillTimeStamp = "";
	}
	
	function tableHandle(){
		//this.showChart = 0;
		$("#cashFlowTable").show();
		$("#chartCashFlow").hide();
	}
	
	function pictureHandle(){
		//this.showChart = 1;
		$("#cashFlowTable").hide();
		$("#chartCashFlow").show();
		echarts.init(document.getElementById('chartCashFlow'),'macarons').resize();
		var xDate = [],incomeData = [],payoutData = []; 
		if(this.cashFlowReportsList !=undefined && this.cashFlowReportsList.length>0){
			var cashFlowReportsList = this.cashFlowReportsList;
			for(var i = 0;i<cashFlowReportsList.length;i++){
				xDate.push(cashFlowReportsList[i].billTimeStamp);
				incomeData.push(commafyback(cashFlowReportsList[i].loanAmountIn));
				payoutData.push(commafyback(cashFlowReportsList[i].payoutAmount));
			}
			
		}
		var myChart = echarts.init(document.getElementById('chartCashFlow'),'macarons');
		options = {
				dataZoom: [//给x轴设置滚动条
					{
						start:0,//默认为0
						end: 100-1500/31,//默认为100
						type: 'slider',
						show: true,
						xAxisIndex: [0],
						handleSize: 0,//滑动条的 左右2个滑动条的大小
						height: 10,//组件高度
						left: 50, //左边的距离
						right: 40,//右边的距离
						bottom: 30,//右边的距离
						handleColor: '#ddd',//h滑动图标的颜色
						handleStyle: {
							borderColor: "#cacaca",
							borderWidth: "1",
							shadowBlur: 2,
							background: "#ddd",
							shadowColor: "#ddd",
						},
						fillerColor: new echarts.graphic.LinearGradient(1, 0, 0, 0, [{
							//给颜色设置渐变色 前面4个参数，给第一个设置1，第四个设置0 ，就是水平渐变
							//给第一个设置0，第四个设置1，就是垂直渐变
							offset: 0,
							color: '#1eb5e5'
						}, {
							offset: 1,
							color: '#5ccbb1'
						}]),
						backgroundColor: '#ddd',//两边未选中的滑动条区域的颜色
						showDataShadow: false,//是否显示数据阴影 默认auto
						showDetail: false,//即拖拽时候是否显示详细数值信息 默认true
						handleIcon: 'M-292,322.2c-3.2,0-6.4-0.6-9.3-1.9c-2.9-1.2-5.4-2.9-7.6-5.1s-3.9-4.8-5.1-7.6c-1.3-3-1.9-6.1-1.9-9.3c0-3.2,0.6-6.4,1.9-9.3c1.2-2.9,2.9-5.4,5.1-7.6s4.8-3.9,7.6-5.1c3-1.3,6.1-1.9,9.3-1.9c3.2,0,6.4,0.6,9.3,1.9c2.9,1.2,5.4,2.9,7.6,5.1s3.9,4.8,5.1,7.6c1.3,3,1.9,6.1,1.9,9.3c0,3.2-0.6,6.4-1.9,9.3c-1.2,2.9-2.9,5.4-5.1,7.6s-4.8,3.9-7.6,5.1C-285.6,321.5-288.8,322.2-292,322.2z',
						filterMode: 'filter',
					},
					//下面这个属性是里面拖到
					{
						type: 'inside',
						show: true,
						xAxisIndex: [0],
						start: 0,//默认为1
						end: 100-1500/31,//默认为100
					},
					],
					title : {
						text: '',
						subtext: '',
						textStyle:{
							fontSize:17
						}
					},
					tooltip: {
						trigger: 'axis',
						/* axisPointer: {
	            type: 'cross',
	            crossStyle: {
	                color: '#fff'
	            }
	        },*/
							showDelay : 0,
					},
					toolbox: {
						feature: {
							dataView: {show: false, readOnly: false},
							magicType: {show: false, type: ['bar']},
							restore: {show: false},
							saveAsImage: {show: false}
						}
					},
					legend: {
						data:['托管收入（万元）','托管支出（万元）']
					},
					xAxis: [
						{
							type: 'category',
							data: xDate,
							boundaryGap : true,
							axisPointer: {
								type: 'shadow'
							}
						}
						],
						yAxis: [
							{
								type: 'value',
					            name: '单位：万元',
					            min: 0,
					            /* max: 250, */
					           /* interval: 50, */
					            axisLabel: {
					                formatter: '{value}'
					            }
							}
							],
							grid: {
								left: 95,
								right:117,
							},
							series: [
								{
						            name:'托管收入（万元）',
						            type:'bar',
						            data:incomeData,
						            itemStyle:
						            {
						       	     normal:
							       	     {
							                label: 
								                {
								           		show: true, //开启显示
								           		position: 'top', //在上方显示
								           		textStyle: 
									           		{ //数值样式
//									           		    color: 'black',
									           			fontSize: 14,
									           			fontWeight: 400
									           		},
								           		formatter: function(p){
								           			return formatNum(p.value);
								           			}
								           		}
							       	     }
						            }
						        },
						        {
						            name:'托管支出（万元）',
						            type:'bar',
						            data:payoutData,
						            itemStyle:
						            {
						       	     normal:
							       	     {
							                label: 
								                {
								           		show: true, //开启显示
								           		position: 'top', //在上方显示
								           		textStyle: 
									           		{ //数值样式
//									           		    color: 'black',
									           			fontSize: 14,
									           			fontWeight: 400
									           		},
								           		formatter: function(p){
								           			return formatNum(p.value);
								           			}
								           		}
							       	     }
						            }
						        },
								]
		};
		myChart.setOption(options);
		myChart.resize();
	}
	//------------------------方法定义-结束------------------//
	
	/**
	 * 初始化日期插件记账日期开始
	 */
		laydate.render({
		    elem: '#date23011601',
		    range: '~',
		    done: function(value, date, endDate){
			    listVue.dateRange = value;
			  	var arr = value.split("~");
			    listVue.billTimeStamp = arr[0];
			    listVue.endBillTimeStamp = arr[1];
		    }
		});
		/**
		 * 初始化日期插件记账日期开始
		 */
		laydate.render({
		    elem: '#date23011602',
		    range: '~',
		    type: 'month',
		    done: function(value, date, endDate){
			    listVue.dateRange = value;
			  	var arr = value.split("~");
			    listVue.billTimeStamp = arr[0];
			    listVue.endBillTimeStamp = arr[1];
		    }
		});
		/**
		 * 初始化日期插件记账日期开始
		 */
		laydate.render({
		    elem: '#date23011603',
		    range: '~',
		    type: 'year',
		    done: function(value, date, endDate){
			    listVue.dateRange = value;
			  	var arr = value.split("~");
			    listVue.billTimeStamp = arr[0];
			    listVue.endBillTimeStamp = arr[1];
		    }
		});
	//------------------------数据初始化-开始----------------//
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#cashFlowReportsDiv",
	"edModelDivId":"#edModel",
	"sdModelDiveId":"#sdModel",
	"listInterface":"../Tg_LoanAmountStatement_ViewList",
	"exportInterface":"../Tg_LoanAmountStatement_ViewExportExcel",//导出excel接口
});
