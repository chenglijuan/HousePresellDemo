(function(baseInfo) {
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion : 19000101,
			tg_ProjectRiskLogDetailModel : [],
			tableId : 1,
			createPeo : [],
			editPeo : [],
			tg_ProjectRiskLogTrackList: [],
			pageNumber: 1,
			countPerPage: 10,
			totalPage: 1,
			totalCount: 1,
			
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
			goToEditHandle: goToEditHandle,
			indexMethod: indexMethod,
			changePageNumber: function (data) {
				console.log(data);
				if (detailVue.pageNumber != data) {
					detailVue.pageNumber = data;
					detailVue.refresh();
				}
			},
			changeCountPerPage : function (data) {
				console.log(data);
				if (detailVue.countPerPage != data) {
					detailVue.countPerPage = data;
					detailVue.refresh();
				}
			},
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
		if(tableIdStr.indexOf("jump_") != -1) {
			var list = tableIdStr.split("_");
			tableIdStr = list[1];
		}
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
				detailVue.tg_ProjectRiskLogDetailModel = jsonObj.tg_ProjectRiskLogDetailModel;
				detailVue.tg_ProjectRiskLogTrackList = jsonObj.tg_ProjectRiskLogTrackList;
				detailVue.smAttachmentList = jsonObj.sm_AttachmentList;
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
			}
		});
	}

	function indexMethod(index) {
		return generalIndexMethod(index, listVue)
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
	"detailDivId" : "#tg_ProjectRiskLogTrackDetailDiv",
	"detailInterface" : "../Tgxy_CoopMemoDetail"
});