(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			emmp_ComAccountModel: {},
			tableId : 1,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			selectItem : [],
			buildingList: [
				{eCode:1}
			],
			projectName: "",
			eCodeFromConstruction: "",
			unitNumber: ""
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			listItemSelectHandle: listItemSelectHandle,
			indexMethod: indexMethod,
			changePageNumber : function(data){
				listVue.pageNumber = data;
			},
			EditHandle: function() {
				enterNextTab(detailVue.tableId, '单元信息修改', 'empj/Empj_BuildingEdit.shtml',detailVue.tableId+"03010204");
			}
		},
		computed:{
			 
		},
		components : {

		},
		watch:{
			
		}
	});

	//------------------------方法定义-开始------------------//
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:this.tableId,
		}
	}

	//详情操作--------------
	function refresh()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var list = tableIdStr.split("_");
		tableIdStr = list[2];
		detailVue.tableId = tableIdStr;
		if (this.tableId == null || this.tableId < 1) 
		{
			return;
		}

		getDetail();
	}

	function getDetail()
	{
		new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				detailVue.projectName = jsonObj.projectName;
				detailVue.eCodeFromConstruction = jsonObj.eCodeFromConstruction;
				detailVue.unitNumber = jsonObj.unitNumber;
				detailVue.buildingList = jsonObj.empj_UnitInfoList;
			}
		});
	}
	
	function listItemSelectHandle(list) {
		generalListItemSelectHandle(detailVue,list)
 	}
	
	function indexMethod(index) {
		return generalIndexMethod(index, detailVue)
	}
	
	function initData()
	{
		
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#buildingDetailDiv",
	"detailInterface":"../Empj_UnitInfoDetailList"
});
