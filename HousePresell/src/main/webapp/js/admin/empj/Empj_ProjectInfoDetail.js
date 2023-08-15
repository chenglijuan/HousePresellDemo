(function(baseInfo){
	var projectMapVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			empj_ProjectInfoModel: "",
			tableId : 1,
            allBuildingIdArr: [],
			//楼幢信息
			empj_BuildingInfoList : [],
			empj_UnitInfoList:[],
			empj_HouseInfoList:[],
			buildingSelectItem: [],
            unitSelectItem: [],
            houseSelectItem: [],
            
            //---------图表显示参数-----------
            pageNumber1 : 1,
			countPerPage1 : 7,
			totalPage1 : 1,
			totalCount1 : 1,
			
			projectAccount: [],
			qs_projectNameList: [],
			floorAccountReportsList : [],
			
			projectId1 : null,

			//---------图表显示参数-----------
			
            buildingpageNumber : 1,
            buildingcountPerPage : 20,
            buildingtotalPage : 1,
            buildingtotalCount : 1,

            unitpageNumber : 1,
            unitcountPerPage : 20,
            unittotalPage : 1,
            unittotalCount : 1,

            housepageNumber : 1,
            housecountPerPage : 20,
            housetotalPage : 1,
            housetotalCount : 1,

            theSwitchInfo:'1',
            latitude:"",
			longitude:"",

            //附件材料
            busiType : "03010101",
            loadUploadList: [],
            showDelete : false,  //附件是否可编辑
            theContentInfo : 0,
            iconshow : 0
		},
		methods : {
			//详情
			refresh : refresh,
            getEmpj_BuildingInfoList: getEmpj_BuildingInfoList,
            getEmpj_UnitInfoList: getEmpj_UnitInfoList,
            getEmpj_HouseInfoList: getEmpj_HouseInfoList,
			initData: initData,
			getSearchForm : getSearchForm,
            projectInfoDetailExportExcelHandle: projectInfoDetailExportExcelHandle,
			//楼幢
            buildingChangePageNumber : function(data){
				projectMapVue.buildingpageNumber = data;
			},
            buildingchangeCountPerPage : function (data) {
                if (projectMapVue.buildingcountPerPage != data) {
                    projectMapVue.buildingcountPerPage = data;
                    projectMapVue.getEmpj_BuildingInfoList();
                }
            },
            buildingindexMethod: function (index) {
                if (projectMapVue.buildingpageNumber > 1) {
                    return (projectMapVue.buildingpageNumber - 1) * projectMapVue.buildingcountPerPage - 0 + (index - 0 + 1);
                }
                if (projectMapVue.buildingpageNumber <= 1) {
                    return index - 0 + 1;
                }
            },
            //单元
            unitchangePageNumber : function(data){
                projectMapVue.unitpageNumber = data;
            },
            unitchangeCountPerPage : function (data) {
                if (projectMapVue.unitcountPerPage != data) {
                    projectMapVue.unitcountPerPage = data;
                    projectMapVue.getEmpj_UnitInfoList();
                }
            },
            unitindexMethod: function (index) {
                if (projectMapVue.unitpageNumber > 1) {
                    return (projectMapVue.unitpageNumber - 1) * projectMapVue.unitcountPerPage - 0 + (index - 0 + 1);
                }
                if (projectMapVue.unitpageNumber <= 1) {
                    return index - 0 + 1;
                }
            },
            //户室
            housechangePageNumber : function(data){
                projectMapVue.housepageNumber = data;
            },
            housechangeCountPerPage : function (data) {
                if (projectMapVue.housecountPerPage != data) {
                    projectMapVue.housecountPerPage = data;
                    projectMapVue.getEmpj_HouseInfoList();
                }
            },
            houseindexMethod: function (index) {
                if (projectMapVue.housepageNumber > 1) {
                    return (projectMapVue.housepageNumber - 1) * projectMapVue.housecountPerPage - 0 + (index - 0 + 1);
                }
                if (projectMapVue.housepageNumber <= 1) {
                    return index - 0 + 1;
                }
            },
            
            //图表方法
            getPictureAndTabData : getPictureAndTabData,
            getProjectData : getProjectData,
            changePageNumber1 : function (data) {
				if (projectMapVue.pageNumber1 != data) {
					projectMapVue.pageNumber1 = data;
					projectMapVue.getPictureAndTabData();
				}
			},
			changeCountPerPage1 : function (data) {
				if (projectMapVue.countPerPage1 != data) {
					projectMapVue.countPerPage1 = data;
					projectMapVue.getPictureAndTabData();
				}
			},
			pictureHandle: function() {
				$(".tableContent").hide();
				$("#chartForPro").show();
				echarts.init(document.getElementById('chartForPro'),'macarons').resize();
			},
			tableHandle: function() {
				$("#chartForPro").hide();
				$(".tableContent").show(500);
			},
			fullScreen: function() {
				if($(".home-top-left").css("position") == 'fixed') {
					$(".home-top-left").removeClass("fullScreen");
					$(".fullScreenBox").attr("title","全屏显示");
					$(".fullScreenBox img").attr("src","../image/fullScreen.png");
				}else {
				    $(".home-top-left").addClass("fullScreen");
					$(".fullScreenBox").attr("title","取消全屏");
					$(".fullScreenBox img").attr("src","../image/oriScreen.png");
				}
				echarts.init(document.getElementById('chartForPro'),'macarons').resize();
//				echarts.init(document.getElementById('chartForPro')).resize();
			},
			openDetails: function(tableId) {
				enterNewTab(projectMapVue.projectId1 + "-" + tableId, "楼盘表", "empj/Empj_BuildingInfoTableDetail.shtml")
			},
			indexMethod : function (index) {
                if (projectMapVue.pageNumber1 > 1) {
                    return (projectMapVue.pageNumber1 - 1) * projectMapVue.countPerPage1 - 0 + (index - 0 + 1);
                }
                if (projectMapVue.pageNumber1 <= 1) {
                    return index - 0 + 1;
                }
			},
			
			// listItemSelectHandle : listItemSelectHandle,
            houseListItemSelectHandle: houseListItemSelectHandle,
            unitListItemSelectHandle: unitListItemSelectHandle,
            buildingListItemSelectHandle: buildingListItemSelectHandle,
            projectInfoEditHandle: projectInfoEditHandle,
            openDetail : openDetail,
            openMapDetail : openMapDetail,
            showContentInfo : showContentInfo,
            hideContentInfo : hideContentInfo,
            theSwitchInfoHadle : function(data){
            	this.theSwitchInfo = data;
            	if(data == 2){
            		getEmpj_UnitInfoList();
            	}else if(data == 3){
            		getEmpj_HouseInfoList();
            	}
            },
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue,
            "my-uploadcomponent":fileUpload
		},
		watch:{
            buildingpageNumber : getEmpj_BuildingInfoList,
            unitpageNumber : getEmpj_UnitInfoList,
            housepageNumber : getEmpj_HouseInfoList,
        }
	});
	
	//----------项目详情中图表信息展示------------------
	
	//----------项目详情中图表信息展示------------------
	
	

	//------------------------方法定义-开始------------------//

	//列表操作----------选择楼幢
    function buildingListItemSelectHandle(val)
    {
        projectMapVue.buildingSelectItem = [];
        for (var index = 0; index < val.length; index++) {
            var element = val[index].tableId;
            projectMapVue.buildingSelectItem.push(element)
        }
    }

    //列表操作----------选择单元
    function unitListItemSelectHandle(val)
    {
        projectMapVue.unitSelectItem = [];
        for (var index = 0; index < val.length; index++) {
            var element = val[index].tableId;
            projectMapVue.unitSelectItem.push(element)
        }
    }

    //列表操作----------选择户室
    function houseListItemSelectHandle(val)
    {
        projectMapVue.houseSelectItem = [];
        for (var index = 0; index < val.length; index++) {
            var element = val[index].tableId;
            projectMapVue.houseSelectItem.push(element)
        }
    }

    // function listItemSelectHandle(list)
    // {
    //     generalListItemSelectHandle(projectMapVue,list)
    // }

	//按钮操作----跳转到项目信息编辑页面
	function projectInfoEditHandle()
	{
        enterNewTabCloseCurrent(projectMapVue.tableId,"编辑项目","empj/Empj_ProjectInfoEdit.shtml");
    }

	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:projectMapVue.interfaceVersion,
			tableId:projectMapVue.tableId,
            getDetailType:"2",
		}
	}

	//详情操作--------------
	function refresh()
	{
		if (projectMapVue.tableId == null || projectMapVue.tableId < 1) 
		{
			return;
		}
		getDetail();
		
		
	}

	function getDetail()
	{
		new ServerInterfaceSync(baseInfo.detailInterface).execute(projectMapVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
                generalErrorModal(jsonObj);
			}
			else
			{
				projectMapVue.empj_ProjectInfoModel = jsonObj.empj_ProjectInfo;
				projectMapVue.allBuildingIdArr = jsonObj.allBuildingIdArr;
				projectMapVue.longitude = jsonObj.empj_ProjectInfo.longitude;
				projectMapVue.latitude = jsonObj.empj_ProjectInfo.latitude;

                loadProjectPostionInMap();
                
                //加载图表信息
                getProjectData();
                getPictureAndTabData();
			}
		});
	}
	
	function getEmpj_BuildingInfoList()
	{
		var model = {
				interfaceVersion:projectMapVue.interfaceVersion,
				projectId:projectMapVue.tableId,
				theState:0,
				pageNumber:projectMapVue.buildingpageNumber,
				countPerPage:projectMapVue.buildingcountPerPage,
		}
		
		new ServerInterface(baseInfo.empj_BuildingInfolistInterface).execute(model, function(jsonObj){
			if(jsonObj.result != "success")
			{
                generalErrorModal(jsonObj);
			}
			else
			{
				projectMapVue.empj_BuildingInfoList=jsonObj.empj_BuildingInfoList;
				projectMapVue.buildingpageNumber=jsonObj.pageNumber;
				projectMapVue.buildingcountPerPage=jsonObj.countPerPage;
				projectMapVue.buildingtotalPage=jsonObj.totalPage;
				projectMapVue.buildingtotalCount = jsonObj.totalCount;
				projectMapVue.buildingSelectItem=[];
			}
		});
	}

	function getEmpj_UnitInfoList()
    {
        var model = {
            interfaceVersion:projectMapVue.interfaceVersion,
            theState:0,
            projectId:projectMapVue.tableId,
            pageNumber:projectMapVue.unitpageNumber,
            countPerPage:projectMapVue.unitcountPerPage,
        }

        new ServerInterface(baseInfo.empj_UnitInfoListInterface).execute(model, function(jsonObj){
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                projectMapVue.empj_UnitInfoList=jsonObj.empj_UnitInfoList;
                projectMapVue.unitpageNumber=jsonObj.pageNumber;
                projectMapVue.unitcountPerPage=jsonObj.countPerPage;
                projectMapVue.unittotalPage=jsonObj.totalPage;
                projectMapVue.unittotalCount = jsonObj.totalCount;
                projectMapVue.unitSelectItem=[];
            }
        });
    }

    function getEmpj_HouseInfoList()
    {
        var model = {
            interfaceVersion:projectMapVue.interfaceVersion,
            theState:0,
            projectId:projectMapVue.tableId,
            pageNumber:projectMapVue.housepageNumber,
            countPerPage:projectMapVue.housecountPerPage,
        }

        new ServerInterface(baseInfo.empj_HouseInfolistInterface).execute(model, function(jsonObj){
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                projectMapVue.empj_HouseInfoList=jsonObj.empj_HouseInfoList;
                projectMapVue.housepageNumber=jsonObj.pageNumber;
                projectMapVue.housecountPerPage=jsonObj.countPerPage;
                projectMapVue.housetotalPage=jsonObj.totalPage;
                projectMapVue.housetotalCount = jsonObj.totalCount;
                projectMapVue.houseSelectItem=[];
            }
        });
    }

    //详情操作-----导出楼幢、单元、户室信息
    function projectInfoDetailExportExcelHandle()
    {
        var buildingModel = {
            interfaceVersion: projectMapVue.interfaceVersion,
            projectId:projectMapVue.tableId,
            idArr: projectMapVue.buildingSelectItem,
        }
        var unitModel = {
            interfaceVersion: projectMapVue.interfaceVersion,
            projectId:projectMapVue.tableId,
            idArr: projectMapVue.unitSelectItem,
            buildingIdArr:projectMapVue.allBuildingIdArr,
        }
        var houseModel = {
            interfaceVersion: projectMapVue.interfaceVersion,
            projectId:projectMapVue.tableId,
            idArr: projectMapVue.houseSelectItem,
            buildingIdArr:projectMapVue.allBuildingIdArr,
        }

        if (projectMapVue.theSwitchInfo == 1)
        {
            new ServerInterface(baseInfo.buildingExportExcelInterface).execute(buildingModel, function (jsonObj) {
                if (jsonObj.result != "success")
                {
                    generalErrorModal(jsonObj);
                }
                else
                {
                    window.location.href=jsonObj.fileDownloadPath;
                    projectMapVue.buildingSelectItem = [];
                    refresh();
                }
            });
        }
        else if (projectMapVue.theSwitchInfo == 2)
        {
            new ServerInterface(baseInfo.unitExportExcelInterface).execute(unitModel, function (jsonObj) {
                if (jsonObj.result != "success")
                {
                    generalErrorModal(jsonObj);
                }
                else
                {
                    window.location.href=jsonObj.fileDownloadPath;
                    projectMapVue.unitSelectItem = [];
                    refresh();
                }
            });
        }
        else if (projectMapVue.theSwitchInfo == 3)
        {
            new ServerInterface(baseInfo.houseExportExcelInterface).execute(houseModel, function (jsonObj) {
                if (jsonObj.result != "success")
                {
                    generalErrorModal(jsonObj);
                }
                else
                {
                    window.location.href=jsonObj.fileDownloadPath;
                    projectMapVue.houseSelectItem = [];
                    refresh();
                }
            });
        }
    }

    //加载地图中项目位置
    function loadProjectPostionInMap()
    {
        var map = new BMap.Map("projectDetailMap");
        var point = new BMap.Point(119.981861,31.771397);
        map.centerAndZoom(point, 14);
        map.enableScrollWheelZoom();//开启鼠标滚轮缩放
        var point = new BMap.Point(projectMapVue.longitude, projectMapVue.latitude);
        var icon = new BMap.Icon("../image/map_ico_location.png", new BMap.Size(21,33));
        var marker = new BMap.Marker(point, {icon:icon});		// 创建标注
        map.addOverlay(marker);						    // 将标注添加到地图中

        // map.centerAndZoom(point, 12);
        map.enableScrollWheelZoom();	 				    //开启鼠标滚轮缩放

        var loadCount = 1;
        map.addEventListener("tilesloaded",function(){
            if(loadCount == 1){
                map.centerAndZoom(point, 12);
            }
            loadCount = loadCount + 1;
        });
    }

	function initData()
	{
		setTimeout(function() {
			$(".skip>span").hide();
			$(".skip input").hide();
		},1000);
		getIdFormTab("", function (id){
		    projectMapVue.tableId = id;
            refresh();
        });

		generalLoadFile2(projectMapVue, projectMapVue.busiType);
	}
	
	//查看项目楼盘表
	function openDetail() 
	{
//		var theTableId = 'Empj_BuildingInfoTableDetail_' + projectMapVue.tableId+"_"+0;
//		$("#tabContainer").data("tabs").addTab({ id: theTableId, text: '楼盘表', closeable: true, url: 'empj/Empj_BuildingInfoTableDetail.shtml' });
		var theTableId = projectMapVue.tableId;
		enterNewTab(theTableId, "楼盘表", "empj/Empj_BuildingInfoTableDetail.shtml")
	}
	function openMapDetail()
	{
//		var theTableId = 'Empj_BuildingInfoTableDetail_' + projectMapVue.empj_ProjectInfoModel.theName+"_"+0;
//		$("#tabContainer").data("tabs").addTab({ id: theTableId, text: '楼盘表', closeable: true, url: 'empj/Empj_BuildingInfoTable.shtml' });
		
		enterNewTab("", "楼盘表", "empj/Empj_BuildingInfoTable.shtml")
	}
	
	function showContentInfo(){
		this.iconshow = 1;
		this.theContentInfo = 1;
		getEmpj_BuildingInfoList();
	}
	
	function hideContentInfo(){
		this.iconshow = 0;
		this.theContentInfo = 0;
	}
	
	
	//项目详情中图表信息
	// 项目托管情况信息
	function getProjectData() {
		var model = {
			interfaceVersion:projectMapVue.interfaceVersion,
			theNameOfProject: projectMapVue.tableId,
			projectId: projectMapVue.tableId,
		}
		new ServerInterface(baseInfo.getProjectInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				projectMapVue.projectAccount = jsonObj.projectAccount;
				var obj = projectMapVue.projectAccount;
				projectMapVue.projectAccount.kbfAmount = thousandsToTwoDecimal(obj.kbfAmount);
				projectMapVue.projectAccount.bfzAmount = thousandsToTwoDecimal(obj.bfzAmount);
				projectMapVue.projectAccount.tgyeAmount = thousandsToTwoDecimal(obj.tgyeAmount);
				projectMapVue.projectAccount.zrzAmount = thousandsToTwoDecimal(obj.zrzAmount);
				projectMapVue.projectAccount.zbfAmount = thousandsToTwoDecimal(obj.zbfAmount);
//				projectMapVue.projectAccount.qyCount = thousandsToTwoDecimal(obj.qyCount);
//				projectMapVue.projectAccount.fkCount = thousandsToTwoDecimal(obj.fkCount);
			}
		})
	}
	
	// 图表信息
	function getPictureAndTabData() {
		var model = {
			interfaceVersion:projectMapVue.interfaceVersion,
			projectId: projectMapVue.tableId,
			pageNumber : projectMapVue.pageNumber1,
			countPerPage : projectMapVue.countPerPage1,
			theNameOfProject : projectMapVue.tableId,
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
//				projectMapVue.qs_projectNameList = jsonObj.projectInfoList;
				projectMapVue.projectId1 = projectMapVue.tableId;
				
				// 图信息
				var currentFigure;
				var buildList = [],yxsxPriceList = [],currentEscrowFundList = [],currentFigureProgressList = [];
				var map = jsonObj.buildingInfoListForMap;
				for(var i = 0; i < map.length; i++) {
					buildList.push(map[i].eCodeFromConstruction);
					yxsxPriceList.push(map[i].yxsxPrice/10000);
					currentEscrowFundList.push(map[i].currentEscrowFund/10000);
					
					console.log('hgjhgj'+map[i].currentFigureProgress);
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
				
				console.log("currentFigureProgressList="+currentFigureProgressList);
				var myChart = echarts.init(document.getElementById('chartForPro'));
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
							projectMapVue.buildingDetailTableId = map[i].tableId;
							break;
						}
					}
					enterNewTab(projectMapVue.projectId1 + "-" + projectMapVue.buildingDetailTableId, "楼盘表", "empj/Empj_BuildingInfoTableDetail.shtml")
					
				});
				
				$(window).resize(function () {
					console.log("resize");
					init();
					myChart.resize();
	            });
				myChart.resize();
				
				// 表信息
	           projectMapVue.floorAccountReportsList = jsonObj.buildingInfoListForList;
			   projectMapVue.floorAccountReportsList.forEach(function(item,index){
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
			    projectMapVue.pageNumber1=jsonObj.pageNumber;
				projectMapVue.countPerPage1=jsonObj.countPerPage;
				projectMapVue.totalPage1=jsonObj.totalPage;
				projectMapVue.totalCount1 = jsonObj.totalCount;
				
			}
		})
	}
	
	
	
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	projectMapVue.initData();
	window.projectMapVue = projectMapVue;
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#empj_ProjectInfoDetailDiv",
	"detailInterface":"../Empj_ProjectInfoDetail",
	"empj_BuildingInfolistInterface":"../Empj_BuildingInfoList",
    "empj_UnitInfoListInterface":"../Empj_UnitInfoListForProject",
    "empj_HouseInfolistInterface":"../Empj_HouseInfoListForProject",
    "buildingExportExcelInterface": "../Empj_ProjectInfoDetailBuildingInfoExportExcel",
    "unitExportExcelInterface": "../Empj_ProjectInfoDetailUnitInfoExportExcel",
    "houseExportExcelInterface": "../Empj_ProjectInfoDetailHouseInfoExportExcel",
    
    //项目详情图表展示
    "getProjectInterface":"../Sm_HomePageProjectAccount", // 加载项目托管情况信息
	"getPictureAndTabInterface":"../Sm_HomePageBuildingAccountList",// 加载图表信息
});
