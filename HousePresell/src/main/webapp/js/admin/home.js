(function(baseInfo){
	var homeVue = new Vue({
		el : baseInfo.homeDivId,
		data : {
			interfaceVersion :19000101,
			longitude:{},	//定义经度
			latitude:{},	//定义纬度
            mapData : [],
            //zTree
			zNodes:[
				/*{"isParent":true,"name":"系统管理","pId":"0","tableId":10026,"theType":2,"id":"1"},
				{"parentUIId":10026,"isParent":true,"name":"权限管理","pId":"1","tableId":10027,"theType":2,"id":"1_1"},
				{"parentUIId":10027,"isParent":false,"name":"用户管理","pId":"1_1","tableId":10028,"theType":1,"id":"1_1_1"},
				{"parentUIId":10027,"isParent":false,"name":"角色管理","pId":"1_1","tableId":10029,"theType":1,"id":"1_1_2"},
				{"parentUIId":10027,"isParent":false,"name":"角色授权","pId":"1_1","tableId":10030,"theType":1,"id":"1_1_3"},
				{"parentUIId":10027,"isParent":false,"name":"菜单管理","pId":"1_1","tableId":10031,"theType":1,"id":"1_1_4"},
				{"parentUIId":10027,"isParent":false,"name":"范围授权","pId":"1_1","tableId":10032,"theType":1,"id":"1_1_5"},
				{"isParent":true,"name":"从业主体","pId":"0","tableId":10040,"theType":2,"id":"3"},
				{"parentUIId":10040,"isParent":true,"name":"一般从业主体","pId":"3","tableId":10041,"theType":2,"id":"3_1"},
				{"parentUIId":10041,"isParent":false,"name":"一般从业主体新增","pId":"3_1","tableId":10042,"theType":1,"id":"3_1_1"}*/
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
					enable: true,//是否显示checkbox
					chkboxType : { "Y" : "ps", "N" : "ps" },
				},
				callback: {
					beforeClick: beforeClick,
					onCheck : onCheck,//用户勾选后的回调
				}
			},
			menuCheckArr:[],//用户勾选的功能按钮Id数组10027,10028,10026
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
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getLoadForm : getLoadForm,
			loadHome:loadHome,
			showMenuModel : showMenuModel,
			getFastEntranceForm : getFastEntranceForm,
			subFastEntrance : subFastEntrance,//快捷入口确定按钮
			loadFastEntrance : loadFastEntrance,
			fastLeft : fastLeft,//点击向左
			fastRight : fastRight,//点击向右
			enterPage : enterPage,//点击快捷导航跳转页面
			showPublic : showPublic,
			enterToPublic : enterToPublic,
			enterToDb : enterToDb,
			enterToWd : enterToWd,
			enterToYj : enterToYj,
			loadDb : loadDb,
		},
		computed:{
			 
		},
		components : {

		},
		watch:{
			
		},
	});

	//------------------------方法定义-开始------------------//

     function beforeClick(treeId, treeNode, clickFlag) {
		return (treeNode.click != false);
	}
     function getFastEntranceForm(){
     	return{
     		interfaceVersion : this.interfaceVersion,
     		idArr : this.menuCheckArr
     	}
     }
     
     //选中复选框获得选中的id
     function onCheck(e, treeId, treeNode){
     	//加载用户所选菜单的详情
		//将指定的节点选中
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
	//详情操作--------------
	function refresh()
	{

	}
	
	
	
	
	//首页操作-------------------------获取页面加载参数
	function getLoadForm(){
		return{
			interfaceVersion : this.interfaceVersion,
			
		}
	}
	var mp = new BMap.Map("mapHome");    // 创建Map实例
	mp.enableScrollWheelZoom();
	//首页操作---------------------页面加载显示百度地图
	function loadHome()
	{
	
		new ServerInterface(baseInfo.loadMapInterface).execute(homeVue.getLoadForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				homeVue.mapData = jsonObj.empj_ProjectInfoList;
				homeVue.mapCenter = jsonObj.baiduMap;
				loadProjectInfoOverlay("mapHome");
				
				/*var pointMap = {
						 lat : 31.753836,
						  lng : 120.024836,
				}
				mp.centerAndZoom(pointMap, 15);*/
				// 复杂的自定义覆盖物
				/*homeVue.mapData.forEach(function(item){
					// 创建地址解析器实例
					var myGeo = new BMap.Geocoder();
					// 将地址解析结果显示在地图上,并调整地图视野
					myGeo.getPoint(item.address, function(point){
						console.log(point)
						if (point) {
							//mp.openInfoWindow(infoWindow,point);开启信息窗口
						    mp.centerAndZoom(point, 15);
							var myCompOverlay = new ComplexCustomOverlay(point, item.theName,item.theName,item.tableId);
				            mp.addOverlay(myCompOverlay);
						}else{
//							alert("您选择地址没有解析到结果!");
						}
					}, "常州市");
					if(item.latitude !="" && item.longitude !=""){
						  var point = {
							  lat : item.latitude,
							  lng : item.longitude,
						  }
						  console.log(point)
						 // 
						  var myCompOverlay = new ComplexCustomOverlay(point, item.theName,item.theName,item.tableId);
				          mp.addOverlay(myCompOverlay);
					}
				    
			   })*/
//				refresh();
                
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
	
	
	//首页操作---------------显示快捷菜单
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
	
	//首页操作----------------快捷导航设置模态框中点击确定按钮
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
	
	//首页操作----------------右侧公告通知
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
				/*$(baseInfo.menuModelDiv).modal('hide');
				homeVue.loadFastEntrance();*/
				//homeVue.sm_ApprovalProcess_WorkflowList = jsonObj.sm_ApprovalProcess_WorkflowList;
				homeVue.YjTotals = jsonObj.YjTotals;
				homeVue.DbTotals = jsonObj.DbTotals;
				/*homeVue.WdTotals = jsonObj.WdTotals;*/
			}
		})
	}
	
	//页面加载显示待办流程
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
				/*$(baseInfo.menuModelDiv).modal('hide');
				homeVue.loadFastEntrance();*/
				homeVue.sm_ApprovalProcess_WorkflowList = jsonObj.sm_ApprovalProcess_WorkflowList;
				homeVue.WdTotals = jsonObj.totalCount;
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
	
	
	
	function initData()
	{
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	homeVue.loadHome();
	homeVue.refresh();
	homeVue.loadFastEntrance();
	homeVue.loadDb();
	homeVue.showPublic();
	homeVue.initData();
	window.mapTableVue = homeVue;
	//------------------------数据初始化-结束----------------//
})({
	"homeDivId":"#homeDiv",
	"menuModelDiv" : "#menuModel",
	"loadDetailCompanyInterface" : "../Sm_HomePageProjectDetail",//点击marker显示详细信息
	"loadMapInterface":"../admin/Sm_HomePageList",//显示地图的接口
	"fastEntranceInterface" : "../Sm_FastNavigateListsByUser",//点击快捷导航发送请求
	"subFastEntranceInterface" : "../Sm_FastNavigateAddByUser",
	"loadFastEntranceInterface" : "../Sm_FastNavigateLists",
	"showPublicInterface" : "../Sm_CommonMessageNoticeLists",
	"loadDbInterface" : "../Sm_ApprovalProcess_WorkflowList"
});
