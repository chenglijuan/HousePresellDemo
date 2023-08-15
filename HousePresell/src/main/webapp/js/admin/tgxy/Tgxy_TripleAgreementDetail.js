(function (baseInfo) {
	var detailVue = new Vue({
		el: baseInfo.detailDivId,
		data: {
			interfaceVersion: 19000101,
			tgxy_TripleAgreementModel: {},
			tableId: 1,
			pageNumber: 1,
			countPerPage: 20,
			totalPage: 1,
			totalCount: 1,
			tgxy_TripleAgreementDetaillist: [], //买受人列表
			errtips: '',
			tgxy_TripleAgreementHt: {},
			smAttachmentList: [], //页面显示已上传的文件
			loadUploadList: [],
			showDelete: false,
			busiType: '06110301',
			busiCode: "06110301", //业务编码
			flag: false,
			print: true,
			signUrl: '',

		},
		methods: {
			//详情
			refresh: refresh,
			initData: initData,
			getSearchForm: getSearchForm,
			indexMethod: indexMethod,
			changePageNumber: function (data) {
				detailVue.pageNumber = data;
			},
			preCommand: preCommand,
			nextCommand: nextCommand,
			getPreForm: getPreForm,
			goToEditHandle: goToEditHandle,
			goToSPHandle: goToSPHandle,
			goToPrintHandle: goToPrintHandle,
			exportPdf: exportPdf,
			getExportForm: getExportForm,
			goToSign: goToSign,
			gotoPreview:gotoPreview,
		},
		computed: {},
		components: {
			"my-uploadcomponent": fileUpload
		},
		watch: {}
	});

	//------------------------方法定义-开始------------------//
	function indexMethod(index) {
		return generalIndexMethod(index, detailVue)
	}

	//详情操作--------------获取"机构详情"参数
	function getSearchForm() {
		return {
			interfaceVersion: this.interfaceVersion,
			tableId: this.tableId,
		}
	}

	//详情操作--------------
	function refresh() {

//		initButtonList();

		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var list = tableIdStr.split("_");
		tableIdStr = list[2];
		detailVue.tableId = tableIdStr;
		if (this.tableId == null || this.tableId < 1) {
			return;
		}

		getDetail();
	}

	function getDetail() {
		new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function (jsonObj) {
			if (jsonObj.result != "success") {
				detailVue.errtips = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
					backdrop: 'static'
				});
			} else {
				detailVue.tgxy_TripleAgreementModel = jsonObj.tgxy_TripleAgreement;
				detailVue.tgxy_TripleAgreementDetaillist = jsonObj.tgxy_BuyerInfoList;
				if (jsonObj.tgxy_TripleAgreement.approvalState == "待提交") {
					detailVue.flag = true;
				} else {
					detailVue.flag = false;
				}

				detailVue.tgxy_TripleAgreementDetaillist.forEach(function (item, index) {
					if (detailVue.tgxy_TripleAgreementDetaillist[index].certificateType == '2') {
						detailVue.tgxy_TripleAgreementDetaillist[index].certificateTypeName = "护照";
					} else {
						detailVue.tgxy_TripleAgreementDetaillist[index].certificateTypeName = "身份证";
					}
					detailVue.tgxy_TripleAgreementDetaillist[index].agentCertTypeName = "身份证";

				});

				detailVue.tgxy_TripleAgreementHt = jsonObj.tgxy_ContractInfo;
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
				if (detailVue.tgxy_TripleAgreementHt.loanAmount != undefined) {
					detailVue.tgxy_TripleAgreementHt.loanAmount = thousandsToTwoDecimal(jsonObj.tgxy_ContractInfo.loanAmount);
				} else {
					detailVue.tgxy_TripleAgreementHt.loanAmount = "0.00";
				}
				if (detailVue.tgxy_TripleAgreementHt.contractSumPrice != undefined) {
					detailVue.tgxy_TripleAgreementHt.contractSumPrice = thousandsToTwoDecimal(jsonObj.tgxy_ContractInfo.contractSumPrice);
				} else {
					detailVue.tgxy_TripleAgreementHt.contractSumPrice = "0.00";
				}
				if (detailVue.tgxy_TripleAgreementModel != undefined) {
					detailVue.tgxy_TripleAgreementHt.firstPaymentAmount = thousandsToTwoDecimal(jsonObj.tgxy_TripleAgreement.firstPaymentAmount);
				} else {
					detailVue.tgxy_TripleAgreementHt.firstPaymentAmount = "0.00";
				}
				detailVue.signUrl = jsonObj.signUrl;

			}
		});
	}

	// 上一条
	function preCommand() {
		new ServerInterface(baseInfo.preInterface).execute(detailVue.getPreForm(), function (jsonObj) {
			if (jsonObj.result != "success") {
				detailVue.errtips = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
					backdrop: 'static'
				});
			} else {
				detailVue.tgxy_TripleAgreementModel = jsonObj.tgxy_TripleAgreement;
				detailVue.tgxy_TripleAgreementHt = jsonObj.tgxy_ContractInfo;
				detailVue.tgxy_TripleAgreementDetaillist = jsonObj.tgxy_BuyerInfoList;
			}
		});
	}

	function getPreForm() {
		return {
			interfaceVersion: this.interfaceVersion,
		}
	}

	function nextCommand() {
		new ServerInterface(baseInfo.nextInterface).execute(detailVue.getPreForm(), function (jsonObj) {
			if (jsonObj.result != "success") {
				detailVue.errtips = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
					backdrop: 'static'
				});
			} else {
				detailVue.tgxy_TripleAgreementModel = jsonObj.tgxy_TripleAgreement;
				detailVue.tgxy_TripleAgreementHt = jsonObj.tgxy_ContractInfo;
				detailVue.tgxy_TripleAgreementDetaillist = jsonObj.tgxy_BuyerInfoList;
			}
		});
	}

	goToSPHandle

	function goToEditHandle() {
		enterNext2Tab(detailVue.tableId, '贷款托管三方协议修改', 'tgxy/Tgxy_TripleAgreementEdit.shtml', detailVue.tableId + "06110301");
	}

	function goToSPHandle() {

		if (confirmFile(this.loadUploadList) == true) {

			detailVue.flag = false;

			var model = {
				interfaceVersion: this.interfaceVersion,
				tableId: this.tableId,
				busiCode: this.busiType
			}

			console.log("提交三方协议START：" + (new Date()).getTime());
			new ServerInterface(baseInfo.sumitInterface).execute(model, function (jsonObj) {
				console.log("提交三方协议END：" + (new Date()).getTime());

				if (jsonObj.result != "success") {
					generalErrorModal(jsonObj);
					detailVue.flag = true;
				} else {
					generalSuccessModal();
				}

			});
		}
	}

	function goToPrintHandle() {
		var model = {
			interfaceVersion: this.interfaceVersion,
			tableId: detailVue.tableId,
		}
		new ServerInterface(baseInfo.printInterface).execute(model, function (jsonObj) {
			if (jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
//					window.location.href = "../"+jsonObj.url;

				window.open("../" + jsonObj.url);
			}
		});
	}

	function goToSign() {

		// if(confirmFile(this.loadUploadList) == true){

		detailVue.flag = false;

		var model = {
			interfaceVersion: this.interfaceVersion,
			tableId: this.tableId,
			busiCode: this.busiType
		}

		console.log("提交三方协议START：" + (new Date()).getTime());
		new ServerInterface(baseInfo.tianyinInterface).execute(model, function (jsonObj) {
			console.log("提交三方协议END：" + (new Date()).getTime());

			if (jsonObj.result != "success") {
				generalErrorModal(jsonObj);
				detailVue.flag = true;
			} else {
				generalSuccessModal();
			}

		});
		// }
	}

	function initButtonList() {
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}

	function initData() {
		initButtonList();
	}

	//列表操作-----------------------导出PDF

	function getExportForm() {
		var href = window.location.href;
		return {
			interfaceVersion: this.interfaceVersion,
			sourceId: this.tableId,
			reqAddress: href,
			sourceBusiCode: this.busiCode,
		}
	}

	function gotoPreview() {
		if(detailVue.signUrl == null  || detailVue.signUrl == ""){
			detailVue.errtips = "没有签署过的文件，请先签署文件";
			$(baseInfo.errorModel).modal('show', {
				backdrop: 'static'
			});
		}else{
			console.log("signUrl="+detailVue.signUrl);
			window.open(detailVue.signUrl, "_blank");
		}
	}

	function exportPdf() {

		detailVue.print = false;
		new ServerInterface(baseInfo.exportInterface).execute(detailVue.getExportForm(), function (jsonObj) {
			if (jsonObj.result != "success") {
				detailVue.print = true;
				$(baseInfo.edModelDivId).modal({
					backdrop: 'static'
				});
				detailVue.errMsg = jsonObj.info;
			} else {
				detailVue.print = true;
				window.open(jsonObj.pdfUrl, "_blank");
			}
		});

	}

	//------------------------方法定义-结束------------------//

	//------------------------数据初始化-开始----------------//
	detailVue.refresh();
	detailVue.initData();
//------------------------数据初始化-结束----------------//
})({
	"detailDivId": "#tgxy_TripleAgreementDiv",
	"errorModel": "#errorTripleDetail",
	"successModel": "#successTripleDetail",
	"detailInterface": "../Tgxy_TripleAgreementDetail",
	// 上一条
	"preInterface": "../Tgxy_TripleAgreementDetail",
	// 下一条
	"nextInterface": "../Tgxy_TripleAgreementDetail",

	"sumitInterface": "../Tgxy_TripleAgreementSubmit",//提交Tgxy_TripleAgreementSubmit    Tgxy_TripleAgreementApprovalProcess
	"printInterface": "../Tgxy_TripleAgreementPrint",
	"exportInterface": "../exportPDFByWord",//导出pdf
	"tianyinInterface": "../TY_SignController",//天印签章生成

});