(function(baseInfo) {
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion : 19000101,
			tgxy_DepositAgreementModel : {},
			tableId : 1,
			createPeo : [],
			editPeo : [],
			smAttachmentList : [], //页面显示已上传的文件
			loadUploadList : [],
			showDelete : false,
			busiType : '06110101',
			orgAmount: "",
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
	//详情操作--------------获取"机构详情"参数
	function getSearchForm() {
		return {
			interfaceVersion : this.interfaceVersion,
			tableId : this.tableId,
		}
	}

	//详情操作--------------根据tableId获取详细信息
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
				detailVue.tgxy_DepositAgreementModel = jsonObj.tgxy_DepositAgreement;
				detailVue.createPeo = jsonObj.tgxy_DepositAgreement.userStart;
				detailVue.editPeo = jsonObj.tgxy_DepositAgreement.userUpdate;
				detailVue.smAttachmentList = jsonObj.smAttachmentList;
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
				detailVue.orgAmount = addThousands(jsonObj.tgxy_DepositAgreement.orgAmount);
			}
		});
	}
	
	function goToEditHandle()
	{
		enterNext2Tab(detailVue.tableId, '修改协定存款协议', 'tgxy/Tgxy_DepositAgreementEdit.shtml',detailVue.tableId+"06110101");
	}

	function initData() {
	}
	//------------------------方法定义-结束------------------//

	//------------------------数据初始化-开始----------------//
	detailVue.refresh();
	detailVue.initData();
//------------------------数据初始化-结束----------------//
})({
	"detailDivId" : "#tgxy_DepositAgreementDetailDiv",
	"detailInterface" : "../Tgxy_DepositAgreementDetail"
});