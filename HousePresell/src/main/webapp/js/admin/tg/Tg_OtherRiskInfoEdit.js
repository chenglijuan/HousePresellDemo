(function(baseInfo){
	var editVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tg_OtherRiskInfoModel: {},
			tableId : 1,
			loadUploadList : [],
			showDelete : true,
			busiType : "21020305"
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			//更新
			getUpdateForm : getUpdateForm,
			update: update,
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
		if (editVue.tableId == null || editVue.tableId < 1) 
		{
			return;
		}

		getDetail();
	}

	function getDetail()
	{
		new ServerInterface(baseInfo.detailInterface).execute(editVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				editVue.tg_OtherRiskInfoModel = jsonObj.tg_OtherRiskInfo;
				editVue.loadUploadList = jsonObj.smAttachmentCfgList;
			}
		});
	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getUpdateForm()
	{
		var fileUploadList = editVue.$refs.listenUploadData.uploadData;
		fileUploadList = JSON.stringify(fileUploadList);
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:this.tableId,
//			theState:this.theState,
//			busiState:this.busiState,
			eCode:this.tg_OtherRiskInfoModel.eCode,
			cityRegionId:this.tg_OtherRiskInfoModel.cityRegionId,
			theNameOfCityRegion:this.tg_OtherRiskInfoModel.theNameOfCityRegion,
			developCompanyId:this.tg_OtherRiskInfoModel.developCompanyId,
			companyName:this.tg_OtherRiskInfoModel.companyName,
			projectId:this.tg_OtherRiskInfoModel.projectId,
//			theNameOfProject:this.tg_OtherRiskInfoModel.theNameOfProject,
			riskInputDate:this.tg_OtherRiskInfoModel.riskInputDate,
			riskInfo:this.tg_OtherRiskInfoModel.riskInfo,
			isUsed:this.tg_OtherRiskInfoModel.isUsed,
//			userUpdate:this.tg_OtherRiskInfoModel.userUpdate,
//			lastUpdateTimeStamp : this.tg_OtherRiskInfoModel.lastUpdateTimeStamp,
			smAttachmentList:fileUploadList,
			remark:this.tg_OtherRiskInfoModel.remark,
		}
	}

	function update()
	{
		new ServerInterface(baseInfo.updateInterface).execute(editVue.getUpdateForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				enterNext2Tab(editVue.tableId, '其他风险信息详情', 'tg/Tg_OtherRiskInfoDetail.shtml',editVue.tableId+"21020204");
				refresh();
			}
		});
	}
	
	function initData()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var list = tableIdStr.split("_");
		editVue.tableId = list[list.length-1];
		editVue.refresh();
	}
	//------------------------方法定义-结束------------------//
	//tg_OtherRiskInfoModel
	//------------------------数据初始化-开始----------------//
	
	editVue.initData();
	laydate.render({
	  elem: '#otherRiskInfoEditDate',
	  done: function(value, date, endDate){
		  editVue.tg_OtherRiskInfoModel.riskInputDate = value;
	  }
	});
	
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tg_OtherRiskInfoEditDiv",
	"detailInterface":"../Tg_OtherRiskInfoDetail",
	"updateInterface":"../Tg_OtherRiskInfoUpdate"
});
