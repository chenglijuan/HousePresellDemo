(function(baseInfo){
	var mapTableVue = new Vue({
		el : baseInfo.listDivId,
		data : {
			interfaceVersion : 19000101,
			keyword : "",
			totalCount : "",
			empj_ProjectInfoList : [],
			infoBoxMap : new Map(),
			initialLongitude:null,
			initialLatitude:null,
		},
		methods : {
			initData : initData,
			search : search,
			getProject : getProject,
		},
		computed:{
			 
		},
		components : {
			
		},
		watch:{
			
		}
	});
	//------------------------方法定义-开始------------------//

	function getProject(empj_ProjectInfo)
	{
		mp.centerAndZoom(new BMap.Point(empj_ProjectInfo.longitude, empj_ProjectInfo.latitude), 14);
	}
	
	//搜索
	function search()
	{
		getEmpj_ProjectInfoList();
	}
	
	//获取项目列表
	function getEmpj_ProjectInfoList()
	{
		var model = {
				interfaceVersion:19000101,
				keyword:mapTableVue.keyword,
				theState:0,
		}
		new ServerInterface(baseInfo.empj_ProjectInfoListInterface).execute(model, function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				mapTableVue.empj_ProjectInfoList = jsonObj.empj_ProjectInfoList;
				mapTableVue.initialLongitude = jsonObj.empj_ProjectInfoList[0].longitude;
				mapTableVue.initialLatitude = jsonObj.empj_ProjectInfoList[0].latitude;
				mapTableVue.totalCount = jsonObj.totalCount;
				loadProjectInfoOverlay();
			}
		});
	}

	function initData()
	{
        getEmpj_ProjectInfoList();
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	mapTableVue.initData();
	window.mapTableVue = mapTableVue;
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#empj_BuildingInfoTableDiv",
	"empj_ProjectInfoListInterface":"../Empj_ProjectInfoList",
});
