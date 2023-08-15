(function(baseInfo) {
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion : 19000101,
			tgxy_TripleAgreementModel : {},
			tableId : 1,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			tgxy_TripleAgreementDetaillist : [], //买受人列表
			errtips : '',
			tgxy_TripleAgreementHt : {},
			smAttachmentList : [], //页面显示已上传的文件
			loadUploadList : [],
			showDelete : true,
			busiType : '06110301',
			busiCode : "06110301", //业务编码
			printMethodList : [
            	{tableId:"0",theName:"机打"},
            	{tableId:"1",theName:"手工打印"},
            ],


		},
		methods : {
			//详情
			refresh : refresh,
			initData : initData,
			getSearchForm : getSearchForm,
			indexMethod : indexMethod,
			changePageNumber : function(data) {
				detailVue.pageNumber = data;
			},
			preCommand : preCommand,
			nextCommand : nextCommand,
			getPreForm : getPreForm,
			goToEditHandle : goToEditHandle,
			goToSPHandle : goToSPHandle,
			goToPrintHandle : goToPrintHandle,
			getPrintMethod : function(data){
				detailVue.tgxy_TripleAgreementModel.printMethod = data.tableId;
			},
		},
		computed : {

		},
		components : {
			"my-uploadcomponent" : fileUpload,
			'vue-select': VueSelect,
		},
		watch : {

		}
	});

	//------------------------方法定义-开始------------------//
	function indexMethod(index) {
		return generalIndexMethod(index, detailVue)
	}
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
		if (this.tableId == null || this.tableId < 1) {
			return;
		}

		getDetail();
	}

	function getDetail() {
		new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
				detailVue.errtips = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
					backdrop : 'static'
				});
			} else {
				detailVue.tgxy_TripleAgreementModel = jsonObj.tgxy_TripleAgreement;
				detailVue.tgxy_TripleAgreementDetaillist = jsonObj.tgxy_BuyerInfoList;
				
				detailVue.tgxy_TripleAgreementDetaillist.forEach(function(item, index) {
					detailVue.tgxy_TripleAgreementDetaillist[index].certificateTypeName = "身份证";
					detailVue.tgxy_TripleAgreementDetaillist[index].agentCertTypeName = "身份证";
				});
				
				detailVue.tgxy_TripleAgreementHt = jsonObj.tgxy_ContractInfo;
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
				if(detailVue.tgxy_TripleAgreementHt.loanAmount != undefined) {
					detailVue.tgxy_TripleAgreementHt.loanAmount = thousandsToTwoDecimal(jsonObj.tgxy_ContractInfo.loanAmount);
				}else {
					detailVue.tgxy_TripleAgreementHt.loanAmount = "0.00";
				}
				if(detailVue.tgxy_TripleAgreementHt.contractSumPrice != undefined) {
					detailVue.tgxy_TripleAgreementHt.contractSumPrice = thousandsToTwoDecimal(jsonObj.tgxy_ContractInfo.contractSumPrice);
				}else {
					detailVue.tgxy_TripleAgreementHt.contractSumPrice = "0.00";
				}
				if(detailVue.tgxy_TripleAgreementHt.firstPaymentAmount != undefined) {
					detailVue.tgxy_TripleAgreementHt.firstPaymentAmount = thousandsToTwoDecimal(jsonObj.tgxy_ContractInfo.firstPaymentAmount);
				}else {
					detailVue.tgxy_TripleAgreementHt.firstPaymentAmount = "0.00";
				}

			}
		});
	}

	// 上一条 
	function preCommand() {
		new ServerInterface(baseInfo.preInterface).execute(detailVue.getPreForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
				detailVue.errtips = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
					backdrop : 'static'
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
			interfaceVersion : this.interfaceVersion,
		}
	}

	function nextCommand() {
		new ServerInterface(baseInfo.nextInterface).execute(detailVue.getPreForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
				detailVue.errtips = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
					backdrop : 'static'
				});
			} else {
				detailVue.tgxy_TripleAgreementModel = jsonObj.tgxy_TripleAgreement;
				detailVue.tgxy_TripleAgreementHt = jsonObj.tgxy_ContractInfo;
				detailVue.tgxy_TripleAgreementDetaillist = jsonObj.tgxy_BuyerInfoList;
			}
		});
	}

	function goToEditHandle() {
		enterNext2Tab(detailVue.tableId, '贷款托管三方协议修改', 'tgxy/Tgxy_TripleAgreementEdit.shtml', detailVue.tableId + "06110301");
	}

	function goToSPHandle() {
		var fileUploadList = detailVue.$refs.listenUploadData.uploadData;
		fileUploadList = JSON.stringify(fileUploadList);
		var model = {
			interfaceVersion : this.interfaceVersion,
			tableId : this.tableId,
			busiCode : this.busiType,
			smAttachmentList : fileUploadList,
			printMethod : detailVue.tgxy_TripleAgreementModel.printMethod, 
		}
		new ServerInterface(baseInfo.sumitInterface).execute(model, function(jsonObj) {
			if (jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
				generalSuccessModal();
				enterNext2Tab(jsonObj.tableId, '贷款托管三方协议详情', 'tgxy/Tgxy_TripleAgreementDetail.shtml',jsonObj.tableId+"06110301");
			}
		});
	}

	function goToPrintHandle() {
	}

	function initData() {
	}


	//------------------------方法定义-结束------------------//

	//------------------------数据初始化-开始----------------//
	detailVue.refresh();
	detailVue.initData();
//------------------------数据初始化-结束----------------//
})({
	"detailDivId" : "#tgxy_TripleAgreementEditDiv",
	"errorModel" : "#errorTripleDetail",
	"successModel" : "#successTripleDetail",
	"detailInterface" : "../Tgxy_TripleAgreementDetail",
	// 上一条
	"preInterface" : "../Tgxy_TripleAgreementDetail",
	// 下一条
	"nextInterface" : "../Tgxy_TripleAgreementDetail",

	"sumitInterface" : "../Tgxy_TripleAgreementUpdate",//提交
});