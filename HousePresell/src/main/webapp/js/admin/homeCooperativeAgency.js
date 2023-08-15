(function(baseInfo){
	var homeVue = new Vue({
		el : baseInfo.homeDivId,
		data : {
			interfaceVersion :19000101,
			longitude:{},	// 定义经度
			latitude:{},	// 定义纬度
            mapData : [],
            // zTree
			zNodes:[
				/*
				 * {"isParent":true,"name":"系统管理","pId":"0","tableId":10026,"theType":2,"id":"1"},
				 * {"parentUIId":10026,"isParent":true,"name":"权限管理","pId":"1","tableId":10027,"theType":2,"id":"1_1"},
				 * {"parentUIId":10027,"isParent":false,"name":"用户管理","pId":"1_1","tableId":10028,"theType":1,"id":"1_1_1"},
				 * {"parentUIId":10027,"isParent":false,"name":"角色管理","pId":"1_1","tableId":10029,"theType":1,"id":"1_1_2"},
				 * {"parentUIId":10027,"isParent":false,"name":"角色授权","pId":"1_1","tableId":10030,"theType":1,"id":"1_1_3"},
				 * {"parentUIId":10027,"isParent":false,"name":"菜单管理","pId":"1_1","tableId":10031,"theType":1,"id":"1_1_4"},
				 * {"parentUIId":10027,"isParent":false,"name":"范围授权","pId":"1_1","tableId":10032,"theType":1,"id":"1_1_5"},
				 * {"isParent":true,"name":"从业主体","pId":"0","tableId":10040,"theType":2,"id":"3"},
				 * {"parentUIId":10040,"isParent":true,"name":"一般从业主体","pId":"3","tableId":10041,"theType":2,"id":"3_1"},
				 * {"parentUIId":10041,"isParent":false,"name":"一般从业主体新增","pId":"3_1","tableId":10042,"theType":1,"id":"3_1_1"}
				 */
			],
			setting : {
				data: {
					simpleData: {
						enable: true
					},
				},
				view: {
					dblClickExpand: false,
					showIcon:false,
					showLine:false
				},
				check: {
					enable: true,// 是否显示checkbox
					chkboxType : { "Y" : "ps", "N" : "ps" },
				},
				callback: {
					beforeClick: beforeClick,
					onCheck : onCheck,// 用户勾选后的回调
				}
			},
			menuCheckArr:[],// 用户勾选的功能按钮Id数组10027,10028,10026
			fastEntranceList : [],
			pageNumber : 1,
			countPerPage : 6,
			totalPage : 1,
			totalCount : 1,
			infoBoxMap : new Map(),
			sm_ApprovalProcess_WorkflowList : [],
			DbTotals : "",
			WdTotals : "",
			YjTotals : "",
			showFastEntrance : false,
			mapCenter :{},
			floorAccountReportsList: [{
				theNameOfCompany: "34"
			}],
			buildAccountModel: {},
			ggData: [],
			areaId: "",
			areaNameList: [],
			keyword: "",
			initialLongitude: "",
			initialLatitude: "",
		},
		methods : {
			// 详情
			refresh : refresh,
			sss: sss,
			initData: initData,
			getLoadForm : getLoadForm,
			loadMap:loadMap,
			loadDepositProject:loadDepositProject,
			loadBuildAccount:loadBuildAccount,
			loadDepositView:loadDepositView,
			loadDepositSituation:loadDepositSituation,
			showMenuModel : showMenuModel,
			getFastEntranceForm : getFastEntranceForm,
			loadCityRegion : loadCityRegion,
			subFastEntrance : subFastEntrance,// 快捷入口确定按钮
			loadFastEntrance : loadFastEntrance,
			fastLeft : fastLeft,// 点击向左
			fastRight : fastRight,// 点击向右
			enterPage : enterPage,// 点击快捷导航跳转页面
			showPublic : showPublic,
			enterToPublic : enterToPublic,
			loadDb : loadDb,
			fullLeft: fullLeft,
			fullRight: fullRight,
			scallFull: scallFull,
			look: look,
			openDetails:function() {
				
			},
			indexMethod : function(index) {
				return generalIndexMethod(index, homeVue)
			},
			getAreaId: function(data) {
				homeVue.areaId = data.tableId;
				
				
				homeVue.look();
			},
			emptyAreaId: function(data) {
				
			},
			areaMouseOver: function() {
				$(".listSelect").css("width","200px");
				$(".listSelect").show(500);
				$(".listSelect").css("display","inline-block");
			},
			loadAreaData: function() {
				var model = {
						interfaceVersion : this.interfaceVersion,
						kind: "1",
						cityRegionId: homeVue.areaId,
						keyword: homeVue.keyword
					};
					
				new ServerInterface(baseInfo.loadMapInterface).execute(model, function(jsonObj)
				{
					if(jsonObj.result != "success")
					{
						generalErrorModal(jsonObj);
					}
					else
					{
						
						if(jsonObj.empj_ProjectInfoList.length == 0)
						{
							jsonObj.info = "未查询到有效的项目信息！";
							generalErrorModal(jsonObj);
						}
						homeVue.mapData = jsonObj.empj_ProjectInfoList;
						homeVue.mapCenter = jsonObj.baiduMap;
						homeVue.initialLongitude = jsonObj.empj_ProjectInfoList[0].longitude;
						homeVue.initialLatitude = jsonObj.empj_ProjectInfoList[0].latitude;
						loadProjectInfoOverlay("map");
						//getProjectData(jsonObj.depositProjectInfoList);
					}
				});
			},
			openRiskDetail: function() {
				enterNewTab("", "项目风险明细表", "tg/Tg_ProjectRisksDetailedList.shtml");
			}
		},
		computed:{
			 
		},
		components : {
			'vue-listselect': VueListSelect,
		},
		watch:{
			
		},
	});

	// ------------------------方法定义-开始------------------//

     function beforeClick(treeId, treeNode, clickFlag) {
		return (treeNode.click != false);
	}
     function getFastEntranceForm(){
     	return{
     		interfaceVersion : this.interfaceVersion,
     		idArr : this.menuCheckArr
     	}
     }
     
     // 选中复选框获得选中的id
     function onCheck(e, treeId, treeNode){
     	// 加载用户所选菜单的详情
		// 将指定的节点选中
		zTree.cancelSelectedNode();
		zTree.selectNode(treeNode,true);
		homeVue.menuCheckArr = [];
        var nodes = zTree.getCheckedNodes(true);
        for(var i=0;i<nodes.length;i++)
        {
        	homeVue.menuCheckArr.push(nodes[i].tableId);
        }
     }
    var zTree, rMenu;
	// 详情操作--------------
	function refresh()
	{

	}
	
	// 首页操作-------------------------获取页面加载参数
	function getLoadForm(){
		return{
			interfaceVersion : this.interfaceVersion,
			
		}
	}
	
	function look() {
		homeVue.loadMap();
		homeVue.loadDepositProject();
		homeVue.loadBuildAccount();
		var ti = setInterval(function() {
			if($("#scrollContent").html() != "") {
				new scrollTxt().init();
				clearInterval(ti);
			}
		},1000);
	}
	
	var mp = new BMap.Map("map");    // 创建Map实例
	mp.enableScrollWheelZoom();
	// 首页操作---------------------页面加载显示百度地图
	
	function loadMap(){
		var model = {
			interfaceVersion : this.interfaceVersion,
			kind: "1",
			cityRegionId: homeVue.areaId,
		};
		
	new ServerInterface(baseInfo.loadMapInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				homeVue.mapData = jsonObj.empj_ProjectInfoList;
				homeVue.mapCenter = jsonObj.baiduMap;
				homeVue.initialLongitude = jsonObj.empj_ProjectInfoList[0].longitude;
				homeVue.initialLatitude = jsonObj.empj_ProjectInfoList[0].latitude;
				loadProjectInfoOverlay("map");
			}
		});
	}
	
	function loadDepositProject(){
		var model = {
			interfaceVersion : this.interfaceVersion,
			kind: "2",
			cityRegionId: homeVue.areaId,
		};
		
	new ServerInterface(baseInfo.loadMapInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				// 公告
				/*homeVue.ggData = jsonObj.pjRiskRatingListMap;
				var cont = "";
				if(homeVue.ggData.length == 0) {
					$("#scrollContent").html("<li style='color:red'>暂无高风险项目</li>");
				}else {
					for(var i = 0 ;i < homeVue.ggData.length;i++) {
						$("#scrollContent").html("");
						cont += '<li style="color:red">' + homeVue.ggData[i].message + '</li>';
					}
					$("#scrollContent").append(cont);
				}*/
				// 项目信息
				getProjectData(jsonObj.depositProjectInfoList);
			}
		});
	}
	
	function loadCityRegion(){
		var model = {
			interfaceVersion : this.interfaceVersion,
			kind: "4",
		};
		
	new ServerInterface(baseInfo.loadMapInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				homeVue.areaNameList = jsonObj.cityRegionList;
				
				homeVue.areaId = homeVue.areaNameList[0].tableId;
				
				homeVue.loadAreaData();
				homeVue.loadMap();
				homeVue.loadDepositProject();
				homeVue.loadBuildAccount();
			}
		});
	}
	
	function getProjectData(list) {
		$("#tuoguanBox").html('');
		var cont = '';
		for(var i = 0;i < list.length;i++) {
			cont += '<tr>';
			cont += '<td style="width:100px;border-left:1px solide #dedede;cursor:pointer;border:none;border-bottom: 1px solid gainsboro!important;">'+ (i+1) +'</td>';
			cont += '<td style="width:150px;cursor:pointer;border:none;border-bottom: 1px solid gainsboro!important;" onclick="openProject(list[i].tableId)">' + list[i].depositTime + '</td>';
			cont += '<td onClick="detailHandle('+list[i].tableId+')" style="width:150px;color:#60B6E6;border-right:1px solide #dedede;cursor:pointer;border:none;border-bottom: 1px solid gainsboro!important;">' + list[i].projectName + '</td>';
			cont += '<td style="cursor:pointer;border:none;border-bottom: 1px solid gainsboro!important;" onclick="openProject(list[i].tableId)">' + list[i].buildingInfo + '</td>';
			/*cont += '<td style="border:none;border-right:1px solide #dedede;cursor:pointer;border-bottom: 1px solid gainsboro!important;"><img onClick="sss('+list[i].latitude+','+list[i].longitude+')" style="width: 20px;" src="../image/area.png"/></td>';*/
			cont += '</tr>';
		}
		//homeVue.mapCenter = {"x":list[i].latitude,"y":list[i].longitude};
		
		$("#tuoguanBox").append(cont);
	}
	
	function loadBuildAccount(){
		var model = {
			interfaceVersion : this.interfaceVersion,
			cityRegionId: homeVue.areaId,
		};
		
	new ServerInterface(baseInfo.loadBuildAccountInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				// 项目统计信息
				homeVue.buildAccountModel = jsonObj.buildAccountMap;
				homeVue.buildAccountModel.allocableAmountC = thousandsToTwoDecimal(homeVue.buildAccountModel.allocableAmountC);
				homeVue.buildAccountModel.escrowAreaC = thousandsToTwoDecimal(homeVue.buildAccountModel.escrowAreaC);
				homeVue.buildAccountModel.totalAccountAmountC = thousandsToTwoDecimal(homeVue.buildAccountModel.totalAccountAmountC);
				homeVue.buildAccountModel.currentEscrowFundC = thousandsToTwoDecimal(homeVue.buildAccountModel.currentEscrowFundC);
				homeVue.buildAccountModel.payoutAmountC = thousandsToTwoDecimal(homeVue.buildAccountModel.payoutAmountC);
				homeVue.buildAccountModel.effectiveLimitedAmountC = thousandsToTwoDecimal(homeVue.buildAccountModel.effectiveLimitedAmountC);
			}
		});
	}
	
	function loadDepositSituation(){
		var model = {
				interfaceVersion : this.interfaceVersion,
				cityRegionId: homeVue.areaId,
			};
			
		new ServerInterface(baseInfo.loadViewDepositProject).execute(model, function(jsonObj)
			{
				if(jsonObj.result != "success")
				{
					generalErrorModal(jsonObj);
				}
				else
				{
					// 托管资金分析统计
					var projectChartRight = jsonObj.depositSituationMapList;
					var xData = [],fkAreaData = [],bfAreaData = [];
					for(var i = 0 ;i < projectChartRight.length;i++) {
						// 横坐标数据
						xData.push(projectChartRight[i].queryTime);
						// 放款金额
						fkAreaData.push(projectChartRight[i].loanAmountFromBankC);
						// 拨付金额
						bfAreaData.push(projectChartRight[i].currentPayoutAmountC);
					}
					var myChart1 = echarts.init(document.getElementById('chartCoop1'));
					option1 = {
						title: {
							text: '托管资金流量统计',/*托管资金放款及拨付统计*/
							subtext: '',
							textStyle: {
								fontSize: 13
							},
							x: 25
						},
						tooltip: {
							trigger: 'axis'
						},
						legend: {
							data: ['放款金额（万元）', '拨付金额（万元）'],
							x: 260
						},
						/*
						 * toolbox: { show: true, feature: { dataZoom: {
						 * yAxisIndex: 'none' }, dataView: {readOnly: false},
						 * magicType: {type: ['line', 'bar']}, restore: {},
						 * saveAsImage: {} } },
						 */
						xAxis: {
							type: 'category',
							/* boundaryGap: false, */
							data: xData,
						},
						yAxis: {
							type: 'value',
							name: '金額（万元）',
							min:0,
							axisLabel: {
								marginRight: 30,
								formatter: '{value}'
							}
						},
						grid: {
							left: 145,
						},
						series: [{
								name: '放款金额（万元）',
								type: 'line',
								data: fkAreaData,
								/* smooth: 0.3, */
								/*
								 * markPoint: { data: [ { type: 'max', name:
								 * '最大值' }, { type: 'min', name: '最小值' } ] },
								 */
								/*
								 * markLine: { data: [ { type: 'average', name:
								 * '平均值' } ] }
								 */
							},
							{
								name: '拨付金额（万元）',
								type: 'line',
								/* smooth: 0.3, */
								data: bfAreaData,
								/*
								 * markPoint: { data: [ { type: 'max', name:
								 * '最大值' }, { type: 'min', name: '最小值' } ] },
								 */
								/*
								 * markLine: { data: [ { type: 'average', name:
								 * '平均值' } ] }
								 */
							}
						]
					};
		
					myChart1.setOption(option1);
				}
			});
	}
	
	
	function loadDepositView(){
		var model = {
			interfaceVersion : this.interfaceVersion,
			cityRegionId: homeVue.areaId,
		};
		
	new ServerInterface(baseInfo.loadViewDepositSituation).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				// 项目情况分析
				var projectChartLeft = jsonObj.depositProjectAnalysisMapList;
				projectChartLeft.forEach(function(item){
					if(item.escrowArea != null) {
						item.escrowArea = item.escrowArea.toFixed(2);
					}
					if(item.preEscrowArea != null) {
						item.preEscrowArea = item.preEscrowArea.toFixed(2);
					}
					if(item.escrowAreaBeforeYear != null) {
						item.escrowAreaBeforeYear = item.escrowAreaBeforeYear.toFixed(2);
					}
					if(item.preEscrowAreaBeforeMonth != null) {
						item.preEscrowAreaBeforeMonth = item.preEscrowAreaBeforeMonth.toFixed(2);
					}
				});
				var xData = [],tgAreaData = [],ysAreaData = [],escrowAreaBeforeYear = [],escrowAreaBeforeMonth = [],preEscrowAreaBeforeYear = [],preEscrowAreaBeforeMonth = [];
				for(var i = 0 ;i < projectChartLeft.length;i++) {
					// 横坐标数据
					xData.push(projectChartLeft[i].queryTime);
					// 托管面积
					tgAreaData.push(projectChartLeft[i].escrowArea);
					// 预售面积
					ysAreaData.push(projectChartLeft[i].preEscrowArea);
					// 同比托管面积
					escrowAreaBeforeYear.push(projectChartLeft[i].escrowAreaBeforeYear);
					// 环比托管面积
					escrowAreaBeforeMonth.push(projectChartLeft[i].escrowAreaBeforeMonth);
					// 同比预售面积
					preEscrowAreaBeforeYear.push(projectChartLeft[i].preEscrowAreaBeforeYear);
					// 环比预售面积
					preEscrowAreaBeforeMonth.push(projectChartLeft[i].preEscrowAreaBeforeMonth);
				}
				var myChart = echarts.init(document.getElementById('chartCoop'));
				options = {
						title: {
							text: '项目情况分析统计',
							subtext: '',
							textStyle: {
								fontSize: 13
							},
							x: 25
						},
						dataZoom: [// 给x轴设置滚动条
			            {
			               start:0,// 默认为0
			               end: 100-1500/31,// 默认为100
			               type: 'slider',
			               show: true,
			               xAxisIndex: [0],
			               handleSize: 0,// 滑动条的 左右2个滑动条的大小
			               height: 10,// 组件高度
			               left: 50, // 左边的距离
			               right: 40,// 右边的距离
			               bottom: 30,// 右边的距离
			               handleColor: '#ddd',// h滑动图标的颜色
			               handleStyle: {
			                   borderColor: "#cacaca",
			                   borderWidth: "1",
			                   shadowBlur: 2,
			                   background: "#ddd",
			                   shadowColor: "#ddd",
			               },
			               fillerColor: new echarts.graphic.LinearGradient(1, 0, 0, 0, [{
			                   // 给颜色设置渐变色 前面4个参数，给第一个设置1，第四个设置0 ，就是水平渐变
			                   // 给第一个设置0，第四个设置1，就是垂直渐变
			                   offset: 0,
			                   color: '#1eb5e5'
			               }, {
			                   offset: 1,
			                   color: '#5ccbb1'
			               }]),
			               backgroundColor: '#ddd',// 两边未选中的滑动条区域的颜色
			               showDataShadow: false,// 是否显示数据阴影 默认auto
			               showDetail: false,// 即拖拽时候是否显示详细数值信息 默认true
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
					    tooltip: {
					        trigger: 'axis',
					        axisPointer: {
					            type: 'cross',
					            crossStyle: {
					                color: '#999'
					            }
					        }
					    },
					    toolbox: {
					        feature: {
					            dataView: {show: false, readOnly: false},
					            magicType: {show: false, type: ['line', 'bar']},
					            restore: {show: false},
					            saveAsImage: {show: false}
					        }
					    },
					    legend: {
					        data:['托管面积（㎡）',  '托管面积同比增长（%）','托管面积环比增长（%）','预售面积（㎡）','预售面积同比增长（%）','预售面积环比增长（%）']
					    },
					    xAxis: [
					        {
					            type: 'category',
					            data : xData,// ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
					            axisPointer: {
					                type: 'shadow'
					            }
					        }
					    ],
					    yAxis: [
					        {
					            type: 'value',
					            name: '面积（㎡）',
					            min: 0,
					            /* max: 250, */
					           /* interval: 50, */
					            axisLabel: {
					                formatter: '{value}'
					            }
					        },
					        {
					            type: 'value',
					            name: '百分比（%）',
					            //min: 0,
					           /* max: 25, */
					           /* interval: 5, */
					            axisLabel: {
					                formatter: '{value}'
					            }
					        }
					    ],
						grid: {
							left: 65,
							right: 65,
						},
					    series: [
					        {
					            name:'托管面积（㎡）',
					            type:'bar',
					            data:tgAreaData,
					        },
					        {
					            name:'预售面积（㎡）',
					            type:'bar',
					            data:ysAreaData
					        },
					        {
					            name:'托管面积同比增长（%）',
					            type:'line',
					            yAxisIndex: 1,
					            smooth: 0.3,
					            data:escrowAreaBeforeYear,
					            itemStyle:{
				                  normal:{color:'#0AA860'}
				                },
					        },
					        {
					            name:'托管面积环比增长（%）',
					            type:'line',
					            lineWidth:10,
					            smooth: 0.3,
					            yAxisIndex: 1,
					            data:escrowAreaBeforeMonth,
					            itemStyle:{
				                  normal:{color:'#F3A024'}
				                },
					        },
					        {
					            name:'预售面积同比增长（%）',
					            type:'line',
					            yAxisIndex: 1,
					            smooth: 0.3,
					            data:preEscrowAreaBeforeYear,
					            itemStyle:{
					                  normal:{color:'#6F6F9E'}
					            },
					        },
					        {
					            name:'预售面积环比增长（%）',
					            type:'line',
					            yAxisIndex: 1,
					            smooth: 0.3,
					            data:preEscrowAreaBeforeMonth,
					            itemStyle:{
					                  normal:{color:'#76CCEE'}
					            },
					        }
					    ]
					};
				myChart.setOption(options);
				myChart.resize();
			}
		});
	}
	
	function loadFastEntrance(){
		if(homeVue.pageNumber == 0){
			homeVue.pageNumber = 1;
		}
		var model = {
			interfaceVersion : homeVue.interfaceVersion,	
			pageNumber : homeVue.pageNumber,
			countPerPage : homeVue.countPerPage,
			totalPage : homeVue.totalPage,
			totalCount : homeVue.totalCount,
			cityRegionId: homeVue.areaId,
		}
		new ServerInterface(baseInfo.loadFastEntranceInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				homeVue.fastEntranceList = jsonObj.sm_FastNavigateList;
				if(homeVue.fastEntranceList.length == 0){
					homeVue.pageNumber = 0;
				}else{
					homeVue.pageNumber = jsonObj.pageNumber;
				}
				homeVue.countPerPage = jsonObj.countPerPage;
				homeVue.totalPage = jsonObj.totalPage;
				homeVue.totalCount = jsonObj.totalCount;
				if(homeVue.fastEntranceList.length != "0"){
					homeVue.showFastEntrance = true;
				}else{
					homeVue.showFastEntrance = false;
				}
			}
		});
	}
	
	function fastLeft(){
		if(homeVue.pageNumber == homeVue.totalPage){
			homeVue.pageNumber = parseInt(homeVue.pageNumber)-1;
		}else if(homeVue.pageNumber == 1){
			homeVue.pageNumber = 1;
		}else{
			homeVue.pageNumber = parseInt(homeVue.pageNumber)-1;
		}
		homeVue.loadFastEntrance();		
	}
	function fastRight(){
		if(homeVue.totalPage!=0){
			if(homeVue.pageNumber == homeVue.totalPage){
				homeVue.pageNumber = homeVue.pageNumber;
			}else{
				homeVue.pageNumber = parseInt(homeVue.pageNumber)+1;
			}
			homeVue.loadFastEntrance();
		}		
	}
	
	function enterPage(en){
		enterNewTab('', en.theNameOfMenu, en.theLinkOfMenu);
	}
	
	
	// 首页操作---------------显示快捷菜单
	function showMenuModel(){
		var model = {
			interfaceVersion : this.interfaceVersion
		}
		new ServerInterface(baseInfo.fastEntranceInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
			    homeVue.zNodes = jsonObj.sm_FastNavigateList;
			    homeVue.menuCheckArr = jsonObj.idArr.split(",");
				$(document).ready(function(){
					$.fn.zTree.init($("#treeDemo"), homeVue.setting, homeVue.zNodes);
					zTree = $.fn.zTree.getZTreeObj("treeDemo");
					rMenu = $("#rMenu");
				});
				try
				{
					Vue.nextTick(function () {
						for(var i=0;i<homeVue.menuCheckArr.length;i++)
						{
							var node = zTree.getNodeByParam("tableId",homeVue.menuCheckArr[i]);
							if(node != null && (node.children == null || node.children.length == 0))
							{
								zTree.checkNode(node, true, true);
								zTree.updateNode(node); 
							}
						}
					});
				} 
				catch (e) 
				{
					homeVue.menuCheckArr = [];
				}
				$(baseInfo.menuModelDiv).modal({
					backdrop:"static"
				})
			}
		})
	}
	
	// 首页操作----------------快捷导航设置模态框中点击确定按钮
	function subFastEntrance(){
		new ServerInterface(baseInfo.subFastEntranceInterface).execute(homeVue.getFastEntranceForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				$(baseInfo.menuModelDiv).modal('hide');
				homeVue.loadFastEntrance();
			}
		})
	}
	
	// 首页操作----------------右侧公告通知
	function showPublic(){
		var model = {
				interfaceVersion : homeVue.interfaceVersion,
				pageNumber : 1,
				countPerPage : 2,	
		}
		new ServerInterface(baseInfo.showPublicInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				/*
				 * $(baseInfo.menuModelDiv).modal('hide');
				 * homeVue.loadFastEntrance();
				 */
				// homeVue.sm_ApprovalProcess_WorkflowList =
				// jsonObj.sm_ApprovalProcess_WorkflowList;
				homeVue.YjTotals = jsonObj.YjTotals;
				homeVue.DbTotals = jsonObj.DbTotals;
				/* homeVue.WdTotals = jsonObj.WdTotals; */
			}
		})
	}
	
	// 页面加载显示待办流程
	function loadDb(){
		var model = {
				interfaceVersion : homeVue.interfaceVersion,
				pageNumber: 1,
				countPerPage: 2,
				totalPage: 0,
				theState: 0,
				busiState: "审核中",
				busiId: ""
		}
		new ServerInterface(baseInfo.loadDbInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				/*
				 * $(baseInfo.menuModelDiv).modal('hide');
				 * homeVue.loadFastEntrance();
				 */
				homeVue.sm_ApprovalProcess_WorkflowList = jsonObj.sm_ApprovalProcess_WorkflowList;
				homeVue.WdTotals = jsonObj.totalCount;
			}
		})
	}
	
	function fullLeft() {
		/*$("#chartCoop1").hide();
		$("#chartCoop").show();
	    homeVue.loadDepositView();
		$(".fullBox").show();*/
		
		
		
	}
	
	
	function fullRight() {
		/*$("#chartCoop").hide();
		$("#chartCoop1").show();
	    homeVue.loadDepositSituation();
		$(".fullBox").show();*/
		
		
		
	}
	
	function scallFull() {
		$(".fullBox").hide(500);
	}
	
	function enterToPublic(tableId){
		
	}
	
	function initData()
	{
		
	}
	// ------------------------方法定义-结束------------------//
	
	// ------------------------数据初始化-开始----------------//
	homeVue.loadCityRegion();
	/*
	 * homeVue.refresh(); homeVue.loadFastEntrance(); homeVue.loadDb();
	 * homeVue.showPublic();
	 */
	homeVue.initData();
	window.mapTableVue = homeVue;
	// ------------------------数据初始化-结束----------------//
})({
	"homeDivId":"#homeCoopDiv",
	"menuModelDiv" : "#menuModel",
	"loadDetailCompanyInterface" : "../Sm_HomePageProjectDetail",// 点击marker显示详细信息
	"loadMapInterface":"../Sm_HomePageLoadByHz",
	"loadBuildAccountInterface":"../Sm_HomePageBuildAccount",
	"loadViewDepositProject":"../Sm_HomePageLoadViewDeposit",
	"loadViewDepositSituation":"../Sm_HomePageLoadViewDepositSituation",
	"empj_ProjectInfoListInterface":"../Empj_ProjectInfoList"
});

function detailHandle(tableId) {
	enterNewTab(tableId, "项目详情", "empj/Empj_BuildingInfoTableDetail.shtml")
}

function sss(x, y) {
	window.mapTableVue = homeVue;
	loadProjectInfoOverlay("bigMap");
}