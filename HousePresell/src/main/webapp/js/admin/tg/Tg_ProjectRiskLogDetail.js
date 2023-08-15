(function(baseInfo) {
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion : 19000101,
			tg_ProjectRiskLogDetailModel : [],
			tableId : 1,
			createPeo : [],
			editPeo : [],
			
			// 附件上传相关参数
			smAttachmentList : [], //页面显示已上传的文件
			loadUploadList : [],
			showDelete : false,
			busiType : '21020303',
		},
		methods : {
			//详情
			refresh : refresh,
			initData : initData,
			getSearchForm : getSearchForm,
			goToEditHandle: goToEditHandle
		},
		computed : {

		},
		components : {
			"my-uploadcomponent" : fileUpload
		},
		watch : {

		}
	});

	//------------------------方法定义-开始------------------//
	//详情操作--------------获取"合作备忘录详情"参数
	function getSearchForm() {
		return {
			interfaceVersion : this.interfaceVersion,
			tableId : detailVue.tableId,
		}
	}

	//详情操作--------------
	function refresh() {
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var list = tableIdStr.split("_");
		tableIdStr = list[2];
		detailVue.tableId = tableIdStr;
		if (detailVue.tableId == null || detailVue.tableId < 1) {
			return;
		}

		getDetail();
	}

	function getDetail() {
		new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
				detailVue.tg_ProjectRiskLogDetailModel = jsonObj.tg_RiskLogInfo;
				detailVue.smAttachmentList = jsonObj.sm_AttachmentList;
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
				
				var riskLog = detailVue.tg_ProjectRiskLogDetailModel.riskRating;
				if(riskLog=='0'){
					detailVue.tg_ProjectRiskLogDetailModel.riskRating = "高";
				}else if(riskLog=='1'){
					detailVue.tg_ProjectRiskLogDetailModel.riskRating = "中";
				}else if(riskLog=='2'){
					detailVue.tg_ProjectRiskLogDetailModel.riskRating = "低";
				}else{
					detailVue.tg_ProjectRiskLogDetailModel.riskRating = "高";
				}
				
			}
		});
	}
	
	function goToEditHandle()
	{
		enterNext2Tab(detailVue.tableId, '项目风险日志修改', 'tg/Tg_ProjectRiskLogEdit.shtml',detailVue.tableId + detailVue.busiType);
	}

	function initData() {
	}
	//------------------------方法定义-结束------------------//

	//------------------------数据初始化-开始----------------//
	detailVue.refresh();
	detailVue.initData();
//------------------------数据初始化-结束----------------//
})({
	"detailDivId" : "#tg_ProjectRiskLogDetailDiv",
	"detailInterface" : "../Tg_RiskLogInfoDetail"
});