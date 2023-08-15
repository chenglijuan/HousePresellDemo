(function(baseInfo) {
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion : 19000101,
			tgxy_CoopAgreementModel : {},
			tableId : 1,
			createPeo : [],
			editPeo : [],
			smAttachmentList : [], //页面显示已上传的文件
			loadUploadList : [],
			showDelete : false,
			busiType : '06110103',
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
				detailVue.tgxy_CoopAgreementModel = jsonObj.tgxy_CoopAgreement;
				detailVue.createPeo = jsonObj.tgxy_CoopAgreement.userStart;
				detailVue.editPeo = jsonObj.tgxy_CoopAgreement.userUpdate;
				detailVue.smAttachmentList = jsonObj.smAttachmentList;
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
			}
		});
	}

	function goToEditHandle()
	{
		enterNext2Tab(detailVue.tableId, '修改合作协议', 'tgxy/Tgxy_CoopAgreementEdit.shtml',detailVue.tableId+"06110103");
	}
	
	function initData() {
	}
	//------------------------方法定义-结束------------------//

	//------------------------数据初始化-开始----------------//
	detailVue.refresh();
	detailVue.initData();
//------------------------数据初始化-结束----------------//
})({
	"detailDivId" : "#tgxy_CoopAgreementDetailDiv",
	"detailInterface" : "../Tgxy_CoopAgreementDetail"
});