(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tg_OtherRiskInfoModel: {},
			tableId : 1,
			loadUploadList : [],
			showDelete : false,
			busiType : "21020305"
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			otherRiskInfoEditHandle : otherRiskInfoEditHandle,
		},
		computed:{
			 
		},
		components : {
			 "my-uploadcomponent":fileUpload
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
		if (detailVue.tableId == null || detailVue.tableId < 1) 
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
				generalErrorModal(jsonObj);
			}
			else
			{
				detailVue.tg_OtherRiskInfoModel = jsonObj.tg_OtherRiskInfo;
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
			}
		});
	}
	
	function initData()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var list = tableIdStr.split("_");
		detailVue.tableId = list[list.length-1];
		detailVue.refresh();
	}
	function otherRiskInfoEditHandle(){
		enterNext2Tab(detailVue.tableId, '编辑其他风险信息', 'tg/Tg_OtherRiskInfoEdit.shtml',detailVue.tableId + "21020204");
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tg_OtherRiskInfoDetailDiv",
	"detailInterface":"../Tg_OtherRiskInfoDetail"
});
