(function(baseInfo) {
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion : 19000101,
			tgpf_RefundInfoModel : {
				fundFromLoan : "",
				unexpiredAmount : "",
				retainRightAmount : "",
				refundAmount : "",
				fundOfTripleAgreement : "",
				expiredAmount : "",
				actualRefundAmount : "",
			},
			tableId : 1,
			smAttachmentList : [], //页面显示已上传的文件
			loadUploadList : [],
			hideShow : false,
			errMsg : "",
			showDelete : false,
			busiCode : "06120201", //业务编码
			showEditBtn : true,
			showSubBtn : true,
		},
		methods : {
			//详情
			refresh : refresh,
			initData : initData,
			getSearchForm : getSearchForm,
			reFundInfoEditHandle : reFundInfoEditHandle, //编辑跳转方法
			reFundInfoSubmitHandle : reFundInfoSubmitHandle, //提交审批流
			getExportForm :getExportForm,//导出参数
			exportPdf:exportPdf,//导出pdf
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
			busiCode : this.busiCode,
		}
	}
	
	function getExportForm() {
		var href = window.location.href;
		return {
			interfaceVersion : this.interfaceVersion,
			sourceId : this.tableId,
			reqAddress : href,
			sourceBusiCode : this.busiCode,
		}
	}

	//详情操作--------------
	function refresh() {
		if (detailVue.tableId == null || detailVue.tableId < 1) {
			return;
		}
		getDetail();
	}
	
	//列表操作-----------------------导出PDF
	function exportPdf(){
	//	var aa=window.open("about:blank");
		new ServerInterface(baseInfo.exportInterface).execute(detailVue.getExportForm(), function(jsonObj){
			if (jsonObj.result != "success") {
				$(baseInfo.edModelDivId).modal({
					backdrop : 'static'
				});
				detailVue.errMsg = jsonObj.info;
			} else {
				window.open(jsonObj.pdfUrl,"_blank");
				
				/*setTimeout(function(){
				aa.location.href=jsonObj.pdfUrl;
				}, 100);*/
			//	 window.open();
			}
		});
	}

	function getDetail() {
		new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
				$(baseInfo.edModelDivId).modal({
					backdrop : 'static'
				});
				detailVue.errMsg = jsonObj.info;
			} else {
				detailVue.tgpf_RefundInfoModel = jsonObj.tgpf_RefundInfo;
				//如果收款人类型为2时，显示开发企业的相关信息
				if(detailVue.tgpf_RefundInfoModel.receiverType == "2"){
					detailVue.tgpf_RefundInfoModel.receiverBankAccount = jsonObj.tgpf_RefundInfo.bAccountSupervised;
					detailVue.tgpf_RefundInfoModel.receiverName = jsonObj.tgpf_RefundInfo.developCompanyName;
					detailVue.tgpf_RefundInfoModel.receiverBankName = jsonObj.tgpf_RefundInfo.bBankName;
				}
				detailVue.tgpf_RefundInfoModel.fundFromLoan = addThousands(detailVue.tgpf_RefundInfoModel.fundFromLoan);
				detailVue.tgpf_RefundInfoModel.unexpiredAmount = addThousands(detailVue.tgpf_RefundInfoModel.unexpiredAmount);
				detailVue.tgpf_RefundInfoModel.retainRightAmount = addThousands(detailVue.tgpf_RefundInfoModel.retainRightAmount);
				detailVue.tgpf_RefundInfoModel.fundOfTripleAgreement = addThousands(detailVue.tgpf_RefundInfoModel.fundOfTripleAgreement);
				detailVue.tgpf_RefundInfoModel.expiredAmount = addThousands(detailVue.tgpf_RefundInfoModel.expiredAmount);
				detailVue.tgpf_RefundInfoModel.actualRefundAmount = addThousands(detailVue.tgpf_RefundInfoModel.actualRefundAmount);
				detailVue.tgpf_RefundInfoModel.refundAmount = addThousands(detailVue.tgpf_RefundInfoModel.refundAmount);
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
				detailVue.smAttachmentList = jsonObj.smAttachmentList;
				if(jsonObj.tgpf_RefundInfo.busiState != "待提交" && jsonObj.tgpf_RefundInfo.busiState != "待审批" && jsonObj.tgpf_RefundInfo.busiState  != "" && jsonObj.tgpf_RefundInfo.busiState  != undefined){
					detailVue.showEditBtn  =  false;
					detailVue.showSubBtn = false ;
				}
				if (detailVue.smAttachmentList != undefined && detailVue.smAttachmentList.length > 0) {
					detailVue.hideShow = true;
				}
				
			}
		});
	}
	//详情操作----------------------提交审批流
	function reFundInfoSubmitHandle() {
		detailVue.showSubBtn = false;
		if(confirmFile(this.loadUploadList) == true){
			new ServerInterface(baseInfo.submitmitInterface).execute(detailVue.getSearchForm(), function(jsonObj) {
				detailVue.showSubBtn = true;
				if (jsonObj.result != "success") {
					$(baseInfo.edModelDivId).modal({
						backdrop : 'static'
					});
					console.log(jsonObj.info);
					detailVue.errMsg = jsonObj.info;
				} else {
					$(baseInfo.sdModelDivId).modal({
						backdrop : 'static'
					});
					detailVue.showEditBtn  =  false;
					detailVue.showSubBtn = false ;
				}
			});
		}
	}
	function initData() {
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var array = tableIdStr.split("_");
		detailVue.tableId = array[2];
		refresh();
	}
	//------------------------方法定义-结束------------------//
	//编辑跳转方法
	function reFundInfoEditHandle() {
		var tabStr = "Tgpf_RefundInfoDetail_" + detailVue.tableId;
		enterNext2Tab(detailVue.tableId, '退房退款详情修改', 'tgpf/Tgpf_RefundInfoEdit.shtml', detailVue.tableId + "1");
	}
	//------------------------数据初始化-开始----------------//
	detailVue.initData();
//------------------------数据初始化-结束----------------//
})({
	"detailDivId" : "#tgpf_RefundInfoDetailDiv",
	"deleteDivId" : "#delete",
	"sdModelDivId" : "#sdModelRefundInfoDetail",
	"detailInterface" : "../Tgpf_RefundInfoDetail",
	"submitmitInterface" : "../Tgpf_RefundInfoApprovalProcess",
	"exportInterface":"../exportPDFByWord",//导出pdf
});