(function(baseInfo) {
	var detailVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion : 19000101,
			tgxy_BankAccountEscrowedList : [],
			companyId : "",
			projectId : "",
			buildingId : "",
			guaranteeCompanyId : "",
			oprUserId : "",
			auditUsetId : "",
			showButton : true,
			errMsg : "", //存放错误提示信息
			loadUploadList : [],
			showDelete : true,
			busiType : "Empj_CancelPaymentGuaranteeApplication",
			empj_PaymentGuarantee : {},
			tableId : 1,
			theState : "",
			
		},
		methods : {
			//详情
			refresh : refresh,
			initData : initData,
			getSearchForm : getSearchForm,
			getBankForm : getBankForm,
			choiceRefundBankAccountForm : choiceRefundBankAccountForm,
			choiceRefundBankAccount : choiceRefundBankAccount,
			loadForm : loadForm,
			indexMethod : indexMethod,
			//添加
			getAddForm : getAddForm,
			getUploadForm : getUploadForm,
			loadUpload : loadUpload,
			add : add,
			getDetail : getDetail,
			
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
			tableId : detailVue.tableId,
			busiCode : "06120403"
		}
	}

	//详情操作--------------
	function refresh() {
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var array = tableIdStr.split("_");
		detailVue.tableId = array[2];
		if (detailVue.tableId == null || detailVue.tableId < 1) {
			return;
		}
		getDetail();
	}

	function getDetail() {
		new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
				generalErrorModal(jsonObj,jsonObj.info);
			} else {
				console.log(jsonObj.empj_PaymentGuarantee);
				//refresh();
				detailVue.empj_PaymentGuarantee = jsonObj.empj_PaymentGuarantee;
				detailVue.empj_PaymentGuarantee.guaranteedAmount = addThousands(detailVue.empj_PaymentGuarantee.guaranteedAmount);
				detailVue.empj_PaymentGuarantee.alreadyActualAmount = addThousands(detailVue.empj_PaymentGuarantee.alreadyActualAmount);
				detailVue.empj_PaymentGuarantee.actualAmount = addThousands(detailVue.empj_PaymentGuarantee.actualAmount);
				
				detailVue.companyId = jsonObj.empj_PaymentGuarantee.companyId;
				detailVue.projectId = jsonObj.empj_PaymentGuarantee.projectId;
				detailVue.guaranteeCompanyId = jsonObj.empj_PaymentGuarantee.guaranteeCompanyId;
				detailVue.oprUserId = jsonObj.empj_PaymentGuarantee.oprUserId;
				detailVue.auditUsetId = jsonObj.empj_PaymentGuarantee.auditUsetId;
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
				console.log(detailVue.loadUploadList);
			}
		});
	}

	//详情更新操作--------------获取"更新机构详情"参数
	function getAddForm() {
		var fileUploadList = detailVue.$refs.listenUploadData.uploadData;
		fileUploadList = JSON.stringify(fileUploadList);
		return {
			interfaceVersion : this.interfaceVersion,
//			theState : this.theState,
//			empj_PaymentGuaranteeId : this.tableId,
			tableId : this.tableId,
     		revokeNo : this.empj_PaymentGuarantee.revokeNo,
//			companyName : this.empj_PaymentGuarantee.companyName,
//			projectName : this.empj_PaymentGuarantee.projectName,
//			guaranteeCompany : this.empj_PaymentGuarantee.guaranteeCompany,
//			userUpdate : this.empj_PaymentGuarantee.userUpdate,
//			userRecord : this.empj_PaymentGuarantee.userRecord,
//			lastUpdateTimeStamp : this.empj_PaymentGuarantee.lastUpdateTimeStamp,
//			recordTimeStamp : this.empj_PaymentGuarantee.recordTimeStamp,
//			applyDate : this.empj_PaymentGuarantee.applyDate,
//			guaranteeType : this.empj_PaymentGuarantee.guaranteeType,
//			guaranteedAmount : this.empj_PaymentGuarantee.guaranteedAmount,
//			eCode : this.empj_PaymentGuarantee.eCode,
//			guaranteeNo : this.empj_PaymentGuarantee.guaranteeNo,
//			actualAmount : this.empj_PaymentGuarantee.actualAmount,
//			alreadyActualAmount : this.empj_PaymentGuarantee.alreadyActualAmount,
			remark : this.empj_PaymentGuarantee.remark,
			smAttachmentList : fileUploadList
		}
	}

	function indexMethod(index) {
		return generalIndexMethod(index, detailVue);
	}

	//获得信息
	function getBankForm() {
		return {
			interfaceVersion : this.interfaceVersion
		}
	}


	function add() {
		new ServerInterface(baseInfo.addInterface).execute(detailVue.getAddForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
				generalErrorModal(jsonObj,jsonObj.info);
			} else {
				enterNext2Tab(detailVue.tableId, '支付保证撤销详情', 'empj/Empj_CancelPaymentGuaranteeApplicationDetail.shtml', detailVue.tableId + "06120403");
			}
		});
	}
	//列表操作-----------------------获取附件参数
	function getUploadForm() {
		return {
			pageNumber : '0',
			busiType : detailVue.busiType,
			interfaceVersion : this.interfaceVersion
		}
	}

	//列表操作-----------------------页面加载显示附件类型
	function loadUpload() {
		new ServerInterface(baseInfo.loadUploadInterface).execute(detailVue.getUploadForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
				generalErrorModal(jsonObj,jsonObj.info);
			} else {
				//refresh();
				
			}
		});
	}
	
	
	//
	function loadForm() {
		new ServerInterface(baseInfo.bankInterface).execute(detailVue.getBankForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
				generalErrorModal(jsonObj,jsonObj.info);
			} else {
				//refresh();
				detailVue.tgxy_BankAccountEscrowedList = jsonObj.tgxy_BankAccountEscrowedList;
			}
		});
	}
	function choiceRefundBankAccountForm() {
		return {
			interfaceVersion : this.interfaceVersion,
			tableId : this.theBankAccountEscrowedId,
		}
	}
	//修改银行账号带出银行名称
	function choiceRefundBankAccount() {
		new ServerInterface(baseInfo.refundBankAccountInterface).execute(detailVue.choiceRefundBankAccountForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
				generalErrorModal(jsonObj,jsonObj.info);
			} else {
				//refresh();
				detailVue.theNameOfBank = jsonObj.tgxy_BankAccountEscrowed.theNameOfBank;
			}
		});
	}

	function initData() {
	}
	//------------------------方法定义-结束------------------//

	//------------------------数据初始化-开始----------------//
	detailVue.loadForm();
	detailVue.loadUpload();
	detailVue.refresh();
	detailVue.initData();

//------------------------数据初始化-结束----------------//
})({
	"addDivId" : "#Empj_CancelPaymentGuaranteeApplicationEditDiv",
	"detailInterface" : "../Empj_PaymentGuaranteeDetail",
	"queryInterface" : "../Tgpf_RefundInfoAddQuery",
	"addInterface" : "../Empj_PaymentGuaranteeUpdate",
	"bankInterface" : "../Tgxy_BankAccountEscrowedList",
	"refundBankAccountInterface" : "../Tgxy_BankAccountEscrowedDetail",
	"loadUploadInterface" : "../Sm_AttachmentCfgList",
	
});