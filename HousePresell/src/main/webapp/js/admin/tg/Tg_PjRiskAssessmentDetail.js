(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tg_PjRiskAssessmentModel: {},
			tableId : 1,
			
			// 附件上传相关参数
			smAttachmentList : [], //页面显示已上传的文件
			loadUploadList : [],
			showDelete : false,
			busiType : '21020301',
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			getEditHandle: getEditHandle
		},
		computed:{
		},
		components : {
			"my-uploadcomponent" : fileUpload
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
			tableId:detailVue.tableId,
			busiCode: detailVue.busiType,
		}
	}
	
	function getEditHandle()
	{
		 enterNext2Tab(detailVue.tableId, '项目风险评估修改', 'tg/Tg_PjRiskAssessmentEdit.shtml',detailVue.tableId+"21020301");
	}

	//详情操作--------------
	function refresh()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var list = tableIdStr.split("_");
		tableIdStr = list[2];
		detailVue.tableId = tableIdStr;
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
				detailVue.tg_PjRiskAssessmentModel = jsonObj.tg_PjRiskAssessment;
				detailVue.smAttachmentList = jsonObj.sm_AttachmentList;
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
			}
		});
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
	"detailDivId":"#tg_PjRiskAssessmentDiv",
	"detailInterface":"../Tg_PjRiskAssessmentDetail"
});
