(function(baseInfo){
	var homeDeveloperVue = new Vue({
		el : baseInfo.homeDivId,
		data : {
			interfaceVersion :19000101,
			pageNumber1 : 1,
			countPerPage1 : 8,
			totalPage1 : 1,
			totalCount1 : 1,
			pageNumber : 1,
			countPerPage : 6,
			totalPage : 1,
			totalCount : 1,
			projectId: "",
			longitude:{},	//定义经度
			latitude:{},	//定义纬度
            mapData : [],
            noticeFlag: true,
            //zTrees
			zNodes:[],
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
					enable: true,//是否显示checkbox
					chkboxType : { "Y" : "ps", "N" : "ps" },
				},
				callback: {
					beforeClick: beforeClick,
					onCheck : onCheck,//用户勾选后的回调
				}
			},
			menuCheckArr:[],//用户勾选的功能按钮Id数组10027,10028,10026
			fastEntranceList : [{"theLinkOfMenu":"tgxy/Tgxy_TripleAgreementList.shtml","theNameOfMenu":"贷款三方托管协议"}],
			infoBoxMap : new Map(),
			sm_ApprovalProcess_WorkflowList : [],
			DbTotals : "",
			WdTotals : "",
			YjTotals : "",
			showFastEntrance : false,
			mapCenter :{},
			qs_projectNameList: [],
			policyRecordTypeList: [],
			sm_PolicyRecordList: [],
			floorAccountReportsList : [],
			noticeTab: "000001",
			projectAccount: [],
			buildingDetailTableId: "",
			
		},
		methods : {
			//详情
			refresh : refresh,
			indexMethod : indexMethod,
			getLoadForm : getLoadForm,
			showMenuModel : showMenuModel,
			getFastEntranceForm : getFastEntranceForm,
			subFastEntrance : subFastEntrance,//快捷入口确定按钮
			loadFastEntrance : loadFastEntrance,
			fastLeft : fastLeft,//点击向左
			fastRight : fastRight,//点击向右
			enterPage : enterPage,//点击快捷导航跳转页面
			enterToPublic : enterToPublic,
			enterToDb : enterToDb,
			enterToWd : enterToWd,
			enterToYj : enterToYj,
			getProjectId: function(data) {
				homeDeveloperVue.projectId = data.tableId;
				homeDeveloperVue.qs_buildingNumberList = [];
				homeDeveloperVue.getPictureAndTabData();
			},
			emptyProjectId: function(data) {
				homeDeveloperVue.projectId = null;
				homeDeveloperVue.qs_buildingNumberList = [];
				homeDeveloperVue.getPictureAndTabData();
			},
			changePageNumber: function (data) {
				if (homeDeveloperVue.pageNumber1 != data) {
					homeDeveloperVue.pageNumber1 = data;
					homeDeveloperVue.getPictureAndTabData();
				}
			},
			changeCountPerPage : function (data) {
				if (homeDeveloperVue.countPerPage1 != data) {
					homeDeveloperVue.countPerPage1 = data;
					homeDeveloperVue.getPictureAndTabData();
				}
			},
			pictureHandle: function() {
				$(".tableContent").hide();
				$("#chart").show();
				echarts.init(document.getElementById('chart'),'macarons').resize();
			},
			getBuildingList: getBuildingList,
			tableHandle: function() {
				$("#chart").hide();
				$(".tableContent").show(500);
			},
			getSearchForm: getSearchForm,
			getNoticeData: getNoticeData,
            getProjectData: getProjectData,			
			getPictureAndTabData: getPictureAndTabData,
			fullScreen: function() {
				homeDeveloperVue.getPictureAndTabData();
				if($(".home-top-left").css("position") == 'fixed') {
					$(".home-top-left").removeClass("fullScreen");
					$(".fullScreenBox").attr("title","全屏显示");
					$(".fullScreenBox img").attr("src","../image/fullScreen.png");
					 
				}else {
				    $(".home-top-left").addClass("fullScreen");
					$(".fullScreenBox").attr("title","取消全屏");
					$(".fullScreenBox img").attr("src","../image/oriScreen.png");
				 
				}
				echarts.init(document.getElementById('chart'),'macarons').resize();
			},
			openDetails: function(tableId) {
				enterNewTab(homeDeveloperVue.projectId + "-" + tableId, "楼盘表", "empj/Empj_BuildingInfoTableDetail.shtml")
			},
			fundAppropriatedHandle: function() {
				enterNewTab('', '新增用款申请', 'tgpf/Tgpf_FundAppropriated_AFAdd.shtml');
			},
			openDetailNew: function(tableId) {
				window.open("newDetail.shtml?id="+tableId);
			},
			openMoreNew: function() {
				window.open('new.shtml');
			}
		},
		computed:{
			 
		},
		components : {
			'vue-listselect': VueListSelect,
			'vue-select': VueSelect,
			'vue-nav' : PageNavigationVue,
		},
		watch:{
			
		},
	});

	//------------------------方法定义-开始------------------//
	
	//页面改变事件监听
	window.onresize = function(){
		echarts.init(document.getElementById('chart'),'macarons').resize();
	}

     function beforeClick(treeId, treeNode, clickFlag) {
		return (treeNode.click != false);
	}
     function getFastEntranceForm(){
     	return{
     		interfaceVersion : this.interfaceVersion,
     		idArr : this.menuCheckArr
     	}
     }
     
     function indexMethod(index) {
		return generalIndexMethod(index, homeDeveloperVue)
	}
     
     //选中复选框获得选中的id
     function onCheck(e, treeId, treeNode){
     	//加载用户所选菜单的详情
		//将指定的节点选中
		zTree.cancelSelectedNode();
		zTree.selectNode(treeNode,true);
		homeDeveloperVue.menuCheckArr = [];
        var nodes = zTree.getCheckedNodes(true);
        for(var i=0;i<nodes.length;i++)
        {
        	homeDeveloperVue.menuCheckArr.push(nodes[i].tableId);
        }
     }
    var zTree, rMenu;
	//详情操作--------------
	function refresh()
	{

	}
	
	function getBuildingList() {
		new ServerInterface(baseInfo.getBuildingListInterface).execute(homeDeveloperVue.getSearchForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				homeDeveloperVue.floorAccountReportsList=jsonObj.qs_BuildingAccount_ViewList;
				homeDeveloperVue.pageNumber=jsonObj.pageNumber;
				homeDeveloperVue.countPerPage=jsonObj.countPerPage;
				homeDeveloperVue.totalPage=jsonObj.totalPage;
				homeDeveloperVue.totalCount = jsonObj.totalCount;
				homeDeveloperVue.floorAccountReportsList.forEach(function(item,index){
					item.escrowArea = thousandsToTwoDecimal(item.escrowArea);
					item.buildingArea = thousandsToTwoDecimal(item.buildingArea);
					item.orgLimitedAmount = thousandsToTwoDecimal(item.orgLimitedAmount);
					item.currentLimitedAmount = thousandsToTwoDecimal(item.currentLimitedAmount);
					item.totalAccountAmount = thousandsToTwoDecimal(item.totalAccountAmount);
					item.spilloverAmount = thousandsToTwoDecimal(item.spilloverAmount);
					item.payoutAmount = thousandsToTwoDecimal(item.payoutAmount);
					item.appliedNoPayoutAmount = thousandsToTwoDecimal(item.appliedNoPayoutAmount);
					item.applyRefundPayoutAmount = thousandsToTwoDecimal(item.applyRefundPayoutAmount);
					item.refundAmount = thousandsToTwoDecimal(item.refundAmount);
					item.currentEscrowFund = thousandsToTwoDecimal(item.currentEscrowFund);
					item.allocableAmount = thousandsToTwoDecimal(item.allocableAmount);
					if(item.recordAvgPriceBldPS != null) {
						item.recordAvgPriceBldPS = thousandsToTwoDecimal(item.recordAvgPriceBldPS);
					};
					if(item.recordAvgPriceOfBuilding != null) {
						item.recordAvgPriceOfBuilding = thousandsToTwoDecimal(item.recordAvgPriceOfBuilding);
					};
					item.zfbzPrice = thousandsToTwoDecimal(item.zfbzPrice);
					item.xjsxPrice = thousandsToTwoDecimal(item.xjsxPrice);
					item.yxsxPrice = thousandsToTwoDecimal(item.yxsxPrice);
				});
				//动态跳转到锚点处，id="top"
				document.getElementById('homeDeveloperDiv').scrollIntoView();
			}
		});
	}
	
	
	
	
	//首页操作-------------------------获取页面加载参数
	function getLoadForm(){
		return{
			interfaceVersion : this.interfaceVersion,
			
		}
	}
	function loadFastEntrance(){
		if(homeDeveloperVue.pageNumber == 0){
			homeDeveloperVue.pageNumber = 1;
		}
		var model = {
			interfaceVersion : homeDeveloperVue.interfaceVersion,	
			pageNumber : homeDeveloperVue.pageNumber,
			countPerPage : homeDeveloperVue.countPerPage,
			totalPage : homeDeveloperVue.totalPage,
			totalCount : homeDeveloperVue.totalCount,
		}
		new ServerInterface(baseInfo.loadFastEntranceInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				homeDeveloperVue.fastEntranceList = jsonObj.sm_FastNavigateList;
				if(homeDeveloperVue.fastEntranceList.length == 0){
					homeDeveloperVue.pageNumber = 0;
				}else{
					homeDeveloperVue.pageNumber = jsonObj.pageNumber;
				}
				homeDeveloperVue.countPerPage = jsonObj.countPerPage;
				homeDeveloperVue.totalPage = jsonObj.totalPage;
				homeDeveloperVue.totalCount = jsonObj.totalCount;
				if(homeDeveloperVue.fastEntranceList.length != "0"){
					homeDeveloperVue.showFastEntrance = true;
				}else{
					homeDeveloperVue.showFastEntrance = false;
				}
			}
		});
	}
	
	function fastLeft(){
		if(homeDeveloperVue.pageNumber == homeDeveloperVue.totalPage){
			homeDeveloperVue.pageNumber = parseInt(homeDeveloperVue.pageNumber)-1;
		}else if(homeDeveloperVue.pageNumber == 1){
			homeDeveloperVue.pageNumber = 1;
		}else{
			homeDeveloperVue.pageNumber = parseInt(homeDeveloperVue.pageNumber)-1;
		}
		homeDeveloperVue.loadFastEntrance();		
	}
	function fastRight(){
		if(homeDeveloperVue.totalPage!=0){
			if(homeDeveloperVue.pageNumber == homeDeveloperVue.totalPage){
				homeDeveloperVue.pageNumber = homeDeveloperVue.pageNumber;
			}else{
				homeDeveloperVue.pageNumber = parseInt(homeDeveloperVue.pageNumber)+1;
			}
			homeDeveloperVue.loadFastEntrance();
		}		
	}
	
	function enterPage(en){
		enterNewTab('', en.theNameOfMenu, en.theLinkOfMenu);
	}
	
	
	//首页操作---------------显示快捷菜单
	function showMenuModel(){
		var model = {
			interfaceVersion : this.interfaceVersion
		}
		new ServerInterface(baseInfo.fastEntranceInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
			    homeDeveloperVue.zNodes = jsonObj.sm_FastNavigateList;
			    homeDeveloperVue.menuCheckArr = jsonObj.idArr.split(",");
				$(document).ready(function(){
					$.fn.zTree.init($("#treeDeveloperDemo"), homeDeveloperVue.setting, homeDeveloperVue.zNodes);
					zTree = $.fn.zTree.getZTreeObj("treeDeveloperDemo");
					rMenu = $("#rDeveloperMenu");
				});
				try
				{
					Vue.nextTick(function () {
						for(var i=0;i<homeDeveloperVue.menuCheckArr.length;i++)
						{
							var node = zTree.getNodeByParam("tableId",homeDeveloperVue.menuCheckArr[i]);
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
					homeDeveloperVue.menuCheckArr = [];
				}
				$(baseInfo.menuModelDiv).modal({
					backdrop:"static"
				})
			}
		})
	}
	
	//首页操作----------------快捷导航设置模态框中点击确定按钮
	function subFastEntrance(){
		new ServerInterface(baseInfo.subFastEntranceInterface).execute(homeDeveloperVue.getFastEntranceForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				$(baseInfo.menuModelDiv).modal('hide');
				homeDeveloperVue.loadFastEntrance();
			}
		})
	}
	
	function enterToPublic(tableId){
		
	}
	
	function enterToDb(){
		enterNewTab("Db", '待办事项', 'sm/Sm_ApprovalProcess_BacklogList.shtml')
	}
	function enterToWd(){
		enterNewTab(240101, '待办流程', 'sm/Sm_ApprovalProcess_AgencyList.shtml');
		
	}
	function enterToYj(){
		enterNewTab('yj', '未处理预警', 'emmp/Emmp_WarningList.shtml')
	}
	
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			pageNumber:this.pageNumber,
			countPerPage:this.countPerPage,			
			keyword:this.keyword,
		}
	}
	
	// 公告信息
	function getNoticeData() {
		var model = {
			interfaceVersion:this.interfaceVersion,
			policyTypeCode: homeDeveloperVue.noticeTab
		}
		new ServerInterface(baseInfo.getNoticeInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				// 公告类型
				homeDeveloperVue.policyRecordTypeList = jsonObj.policyRecordTypeList;
				// 公告内容
				homeDeveloperVue.sm_PolicyRecordList = jsonObj.sm_PolicyRecordList;
			}
		})
	}
	
	// 项目托管情况信息
	function getProjectData() {
		var model = {
			interfaceVersion:this.interfaceVersion,
			theNameOfProject : homeDeveloperVue.projectId,
			projectId: homeDeveloperVue.projectId,
		}
		new ServerInterface(baseInfo.getProjectInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				// 公告类型
				homeDeveloperVue.projectAccount = jsonObj.projectAccount;
				var obj = homeDeveloperVue.projectAccount;
				homeDeveloperVue.projectAccount.kbfAmount = thousandsToTwoDecimal(obj.kbfAmount);
				homeDeveloperVue.projectAccount.bfzAmount = thousandsToTwoDecimal(obj.bfzAmount);
				homeDeveloperVue.projectAccount.tgyeAmount = thousandsToTwoDecimal(obj.tgyeAmount);
				homeDeveloperVue.projectAccount.zrzAmount = thousandsToTwoDecimal(obj.zrzAmount);
				homeDeveloperVue.projectAccount.zbfAmount = thousandsToTwoDecimal(obj.zbfAmount);
//				homeDeveloperVue.projectAccount.qyCount = thousandsToTwoDecimal(obj.qyCount);
//				homeDeveloperVue.projectAccount.fkCount = thousandsToTwoDecimal(obj.fkCount);
			}
		})
	}
	
	// 图表信息
	function getPictureAndTabData() {
		var model = {
			interfaceVersion:this.interfaceVersion,
			projectId: homeDeveloperVue.projectId,
			pageNumber : homeDeveloperVue.pageNumber1,
			countPerPage : homeDeveloperVue.countPerPage1,
			theNameOfProject : homeDeveloperVue.projectId,
		}
		new ServerInterface(baseInfo.getPictureAndTabInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				// 項目列表
				homeDeveloperVue.qs_projectNameList = jsonObj.projectInfoList;
				//如果未选择项目，则默认显示第一个项目
				if(homeDeveloperVue.projectId == null || homeDeveloperVue.projectId == "")
				{
					homeDeveloperVue.projectId = homeDeveloperVue.qs_projectNameList[0].tableId;
				}
				
				
				// 图信息homeDeveloperVue.projectAccount = jsonObj.projectAccount;
				var currentFigure;
				var buildList = [],yxsxPriceList = [],currentEscrowFundList = [],currentFigureProgressList = [];
				var map = jsonObj.buildingInfoListForMap;
				for(var i = 0; i < map.length; i++) {
					buildList.push(map[i].eCodeFromConstruction);
					yxsxPriceList.push(map[i].yxsxPrice/10000);
					currentEscrowFundList.push(map[i].currentEscrowFund/10000);
					
					if(map[i].currentFigureProgress == '正负零前')
					{
						currentFigure = 1;
					}
					else if(map[i].currentFigureProgress == '正负零')
					{
						currentFigure = 2;
					}else if(map[i].currentFigureProgress == '主体结构达到1/2')
					{
						currentFigure = 3;
					}else if(map[i].currentFigureProgress == '主体结构1/2')
					{
						currentFigure = 3;
					}else if(map[i].currentFigureProgress == '主体结构封顶')
					{
						currentFigure = 4;
					}else if(map[i].currentFigureProgress == '外立面装饰工程完成')
					{
						currentFigure = 5;
					}else if(map[i].currentFigureProgress == '室内装修工程完成')
					{
						currentFigure = 6;
					}else if(map[i].currentFigureProgress == '完成交付使用备案')
					{
						currentFigure = 7;
					}else{
						currentFigure = 0;
					}
					
					currentFigureProgressList.push(currentFigure);
				}
//				 var activeChart = echarts.init(document.getElementById('active'),'macarons');
				var myChart = echarts.init(document.getElementById('chart'),'macarons');
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
				        formatter:function(params)//数据格式
		                    {
//				        	console.log("params="+params);
		                    var relVal = params[0].name+"<br/>";
		        			
		                    relVal += params[0].seriesName+ ' : ' + thousandsToTwoDecimal(params[0].value)+"<br/>";
		                    relVal +=params[1].seriesName+ ' : ' +thousandsToTwoDecimal(params[1].value)+"<br/>";
		        
		        			var nameformater;
					        if(params[2].value == 1) {
					        	nameformater='正负零前';
			            	}else if(params[2].value == 2) {
			            		nameformater='正负零';
			            	}else if(params[2].value == 3) {
			            		nameformater='主体结构达到1/2';
			            	}else if(params[2].value == 4) {
			            		nameformater='主体结构封顶';
			            	}else if(params[2].value == 5) {
			            		nameformater='外立面装饰工程完成';
			            	}else if(params[2].value == 6) {
			            		nameformater='室内装修工程完成';
			            	}else if(params[2].value == 7) {
			            		nameformater='完成交付使用备案';
			            	}else{
			            		nameformater='无';
			            	}
					        relVal += params[2].seriesName+ ' : ' + nameformater;
//		                    relVal += params[2].seriesName+ ' : ' + params[2].value;
//		        			console.log("relVal="+relVal);
		                    return relVal;
		                    },
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
				        data:['受限额度','托管余额','进度节点']
				    },
				    xAxis: [
				        {
				            type: 'category',
				            data: buildList,
				            boundaryGap : true,
				            axisPointer: {
				                type: 'shadow'
				            }
				        }
				    ],
				    yAxis: [
				        {
				            type: 'value',
				            name: '受限额度与托管余额（万元）',
				            nameTextStyle:{
				                fontSize:14.5, 
				                fontWeight: 'bold', // 粗细
				            },
				            min: 0,/*
				            max: ,*/
				            interval: 1000,
				            axisLabel: {
				                formatter: '{value}'
				            },splitLine: {
					            show: false
					        },
				        },
				        {
				            type: 'value',
				            name: '进度节点',
				            nameTextStyle:{
				                fontSize:14.5, 
				                fontWeight: 'bold', // 粗细
				            },
				            min: 0,
				            max: 7,
				            interval: 1,
				            splitLine: {
					            show: false
					        },
				            axisLabel: {
				            	formatter:function(value){
				            		var texts = [];
				                	if(value == 1) {
//				                		return '正负零前';
				                		texts.push('正负零前');
				                	}else if(value == 2) {
//				                		return '正负零';
				                		texts.push('正负零');
				                	}else if(value == 3) {
//				                		return '主体结构达到1/2';
				                		texts.push('主体结构达到1/2');
				                	}else if(value == 4) {
//				                		return '主体结构封顶';
				                		texts.push('主体结构封顶');
				                	}else if(value == 5) {
//				                		return '外立面装饰工程完成';
				                		texts.push('外立面装饰工程完成');
				                	}else if(value == 6) {
//				                		return '室内装修工程完成';
				                		texts.push('室内装修工程完成');
				                	}else if(value == 7) {
//				                		return '完成交付使用备案';
				                		texts.push('完成交付使用备案');
				                	}
				                	return texts;
				                	
				                }
				            }
				        }
				    ],
				    grid: {
				        left: 95,
				        right:117,
				    },
				    series: [
				        {
				            name:'受限额度',
				            type:'bar',
				            barMaxWidth:'30',
				            itemStyle:{
				                  normal:{color:'#0086CB'}
				            },
//				            barCategoryGap:'20%',
				            data:yxsxPriceList,
				        },
				        {
				            name:'托管余额',
				            type:'bar',
				            barMaxWidth:'30',
				            itemStyle:{
				                  normal:{color: '#96BD48'}
				            },
//				            barCategoryGap:'20%',
				            data:currentEscrowFundList,
				        },
				        {
				            name:'进度节点',
				            type:'bar',
				            yAxisIndex: 1,
				            barMaxWidth:'40',
				            itemStyle:{
				                  normal:{color: '#FF9303'}
				            },
//				            barCategoryGap:'20%',
				            data:currentFigureProgressList,
				        }
				    ]
				};
				myChart.setOption(options);
				
				myChart.on('click',function(buildName) {
					var name = buildName.name;
					for(var i = 0 ;i < map.length; i++) {
						if(name == map[i].eCodeFromConstruction) {
							homeDeveloperVue.buildingDetailTableId = map[i].tableId;
							break;
						}
					}
					enterNewTab(homeDeveloperVue.projectId + "-" + homeDeveloperVue.buildingDetailTableId, "楼盘表", "empj/Empj_BuildingInfoTableDetail.shtml")
					
				});
				
				$(window).resize(function () {
					console.log("resize");
					init();
					myChart.resize();
	            });
				 
				// 表信息
	           homeDeveloperVue.floorAccountReportsList = jsonObj.buildingInfoListForList;
			   homeDeveloperVue.floorAccountReportsList.forEach(function(item,index){
					item.escrowArea = thousandsToTwoDecimal(item.escrowArea);
					item.buildingArea = thousandsToTwoDecimal(item.buildingArea);
					item.orgLimitedAmount = thousandsToTwoDecimal(item.orgLimitedAmount);
					item.currentLimitedAmount = thousandsToTwoDecimal(item.currentLimitedAmount);
					item.totalAccountAmount = thousandsToTwoDecimal(item.totalAccountAmount);
					item.spilloverAmount = thousandsToTwoDecimal(item.spilloverAmount);
					item.payoutAmount = thousandsToTwoDecimal(item.payoutAmount);
					item.appliedNoPayoutAmount = thousandsToTwoDecimal(item.appliedNoPayoutAmount);
					item.applyRefundPayoutAmount = thousandsToTwoDecimal(item.applyRefundPayoutAmount);
					item.refundAmount = thousandsToTwoDecimal(item.refundAmount);
					item.currentEscrowFund = thousandsToTwoDecimal(item.currentEscrowFund);
					item.allocableAmount = thousandsToTwoDecimal(item.allocableAmount);
					if(item.recordAvgPriceBldPS != null) {
						item.recordAvgPriceBldPS = thousandsToTwoDecimal(item.recordAvgPriceBldPS);
					};
					if(item.recordAvgPriceOfBuilding != null) {
						item.recordAvgPriceOfBuilding = thousandsToTwoDecimal(item.recordAvgPriceOfBuilding);
					};
					item.zfbzPrice = thousandsToTwoDecimal(item.zfbzPrice);
					item.xjsxPrice = thousandsToTwoDecimal(item.xjsxPrice);
					item.yxsxPrice = thousandsToTwoDecimal(item.yxsxPrice);
				});
			    homeDeveloperVue.pageNumber1=jsonObj.pageNumber;
				homeDeveloperVue.countPerPage1=jsonObj.countPerPage;
				homeDeveloperVue.totalPage1=jsonObj.totalPage;
				homeDeveloperVue.totalCount1 = jsonObj.totalCount;
				
			}
		})
		setTimeout(function() {
			$(".skip>span").hide();
			$(".skip input").hide();
		},1000);
	}
	
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	homeDeveloperVue.refresh();
	homeDeveloperVue.loadFastEntrance();
	
	// 加载公告信息
	homeDeveloperVue.getNoticeData();
	
	// 加载项目托管情况信息
	homeDeveloperVue.getProjectData();
	
	// 加载楼幢图表信息
	homeDeveloperVue.getPictureAndTabData();
	//------------------------数据初始化-结束----------------//
})({
	"homeDivId":"#homeDeveloperDiv",
	"menuModelDiv" : "#menuDeveloperModel",
	"loadDetailCompanyInterface" : "../Sm_HomePageProjectDetail",//点击marker显示详细信息
	"loadMapInterface":"../admin/Sm_HomePageList",//显示地图的接口
	"fastEntranceInterface" : "../Sm_FastNavigateListsByUser",//点击快捷导航发送请求
	"subFastEntranceInterface" : "../Sm_FastNavigateAddByUser",
	"loadFastEntranceInterface" : "../Sm_FastNavigateLists",
	"getBuildingListInterface":"../Sm_HomePageBuildingAccountList",
	"getNoticeInterface":"../Sm_HomePagePolicyRecordList",// 加载公告信息
	"getProjectInterface":"../Sm_HomePageProjectAccount", // 加载项目托管情况信息
	"getPictureAndTabInterface":"../Sm_HomePageBuildingAccountList",// 加载图表信息
});
