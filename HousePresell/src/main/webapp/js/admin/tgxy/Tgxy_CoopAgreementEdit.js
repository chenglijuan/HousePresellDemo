(function(baseInfo) {
	var editVue = new Vue({
		el: baseInfo.detailDivId,
		data: {
			interfaceVersion: 19000101,
			tgxy_CoopAgreementModel: {},
			tableId: 1,
			pkId: "",
			bankId: "",
			depositBankId: "",
			createPeo: [],
			editPeo: [],
			theNameOfBank: null,
			theNameOfDepositBank: null,
			emmp_BankInfoList: [],
			emmp_BankBranchList: [],
			userUpdateId: 2,
			bankOfDepositId: "",
            errMsg: "",
            successMsg: "",
            smAttachmentList:[],
            loadUploadList : [],
            showDelete : true,
            busiType : '06110103',
		},
		methods: {
			initData: initData,
			getSearchForm: getSearchForm,
			//更新
			getUpdateForm: getUpdateForm,
			update: update,
			bankData: bankData,
			bankBranchData: bankBranchData,
			lookBank: lookBank,
			lookOpenBank: lookOpenBank,
			getDetailForm: getDetailForm,
			getDetailBranchForm: getDetailBranchForm,
			getInitDetailForm: getInitDetailForm,
			getBankBranchForm: getBankBranchForm,
			errClose: errClose,
			succClose: succClose,
		},
		computed: {

		},
		components: {
			"my-uploadcomponent":fileUpload
		},
		watch: {

		}
	});

	//------------------------方法定义-开始------------------//
	//详情操作--------------获取"机构详情"参数
	function getSearchForm() {
		return {
			interfaceVersion: this.interfaceVersion,
			tableId: this.tableId,
		}
	}
	
	function getBankBranchForm() {
		return {
			interfaceVersion: this.interfaceVersion,
			bankId: editVue.bankId,
		}
	}

	function getInitDetailForm() {
		return {
			interfaceVersion: this.interfaceVersion,
			tableId: this.pkId,
		}
	}

	function getDetail() {
		new ServerInterface(baseInfo.detailInterface).execute(editVue.getInitDetailForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
				editVue.tgxy_CoopAgreementModel = jsonObj.tgxy_CoopAgreement;
				editVue.bankId = jsonObj.tgxy_CoopAgreement.bankId;
				editVue.depositBankId = jsonObj.tgxy_CoopAgreement.bankOfDepositId;
				editVue.createPeo = jsonObj.tgxy_CoopAgreement.userStart;
				editVue.editPeo = jsonObj.tgxy_CoopAgreement.userUpdate;
				editVue.theNameOfBank = jsonObj.tgxy_CoopAgreement.theNameOfBank;
				editVue.theNameOfDepositBank = jsonObj.tgxy_CoopAgreement.theNameOfDepositBank;
				editVue.smAttachmentList = jsonObj.smAttachmentList;
				editVue.loadUploadList = jsonObj.smAttachmentCfgList;
				bankBranchData();
			}
		});
	}

	//更新操作--------------获取"获取合作协议修改"参数
	function getUpdateForm() {
		var fileUploadList = editVue.$refs.listenUploadData.uploadData;
		fileUploadList = JSON.stringify(fileUploadList);
 
		return {
			interfaceVersion: this.interfaceVersion,
			theState: this.theState,
			busiState: this.busiState,
			eCode: this.eCode,
			userStartId: this.userStartId,
			createTimeStamp: this.createTimeStamp,
			userUpdateId: this.userUpdateId,
			lastUpdateTimeStamp: this.lastUpdateTimeStamp,
			userRecordId: this.userRecordId,
			recordTimeStamp: this.recordTimeStamp,
			bankId: this.bankId,
			theNameOfBank: this.theNameOfBank,
			signDate: document.getElementById("date0611010303").value,
			bankOfDepositId: this.depositBankId,
			theNameOfDepositBank: this.theNameOfDepositBank,
			tableId: this.pkId,
			smAttachmentList:fileUploadList,
		}
	}

	function update() {
		new ServerInterface(baseInfo.updateInterface).execute(editVue.getUpdateForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				editVue.errMsg = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
				    backdrop :'static'
			    });
			} else {
				enterNext2Tab(editVue.pkId, '合作协议详情', 'tgxy/Tgxy_CoopAgreementDetail.shtml',editVue.pkId+"06110103");
			}
		});
	}
	
	function errClose()
	{
		$(baseInfo.errorModel).modal('hide');
	}
	
	function succClose()
	{
		$(baseInfo.successModel).modal('hide');
	}

	function initData() {
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var list = tableIdStr.split("_");
		tableIdStr = list[2];
		editVue.pkId = tableIdStr;
		bankData();
	}

	// 获取银行名称数据
	function bankData() {
		new ServerInterface(baseInfo.bankInterface).execute(editVue.getSearchForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
				editVue.emmp_BankInfoList = jsonObj.emmp_BankInfoList;
				//				editVue.bankId = editVue.bankId;
				//				console.log("tableId:"+editVue.tableId);
				getDetail();
			}
		});
	}

	// 获取开户行数据
	function bankBranchData() {
		new ServerInterface(baseInfo.bankBranchInterface).execute(editVue.getBankBranchForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
				editVue.emmp_BankBranchList = jsonObj.emmp_BankBranchList;
			}
		});
	}

	// 银行名称数据监听
	function lookBank() {
		if(editVue.bankId != ""){
			editVue.tgxy_CoopAgreementModel.signDate = document.getElementById("date0611010303").value;
			new ServerInterface(baseInfo.detailInfoInterface).execute(editVue.getDetailForm(), function(jsonObj) {
				if(jsonObj.result != "success") {
					generalErrorModal(jsonObj);
				} else {
					$(baseInfo.detailDivId).modal('hide');
					editVue.theNameOfBank = jsonObj.emmp_BankInfo.theName;
					// 选中银行对应的开户行数据加载
					new ServerInterface(baseInfo.bankBranchInterface).execute(editVue.getBankBranchForm(), function(jsonObj) {
						if(jsonObj.result != "success") {
							generalErrorModal(jsonObj);
						} else {
							editVue.emmp_BankBranchList = jsonObj.emmp_BankBranchList;
							editVue.depositBankId = "";
						}
					});
				}
			});
		}else{
			editVue.emmp_BankBranchList = [];
		}
	}

	function getDetailForm() {
		return {
			interfaceVersion: this.interfaceVersion,
			theState: this.theState,
			tableId: this.bankId,
		}
	}

	// 开户行数据监听
	function lookOpenBank() {
		if(this.depositBankId != undefined) {
			editVue.tgxy_CoopAgreementModel.signDate = document.getElementById("date0611010303").value;
			new ServerInterface(baseInfo.detailBranchInfoInterface).execute(editVue.getDetailBranchForm(), function(jsonObj) {
				if(jsonObj.result != "success") {
					generalErrorModal(jsonObj);
				} else {
					$(baseInfo.detailDivId).modal('hide');
					editVue.theNameOfDepositBank = jsonObj.emmp_BankBranch.theName;
				}
			});
		}
	}

	function getDetailBranchForm() {
		return {
			interfaceVersion: this.interfaceVersion,
			theState: this.theState,
			tableId: this.depositBankId,
		}
	}

	// 添加日期控件
	laydate.render({
		elem: '#date0611010303',
	});
	//------------------------方法定义-结束------------------//

	//------------------------数据初始化-开始----------------//
	editVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId": "#tgxy_CoopAgreementEditDiv",
	"errorModel": "#errorM",
	"successModel": "#successM",
	"detailInterface": "../Tgxy_CoopAgreementDetail",
	// 加载银行名称数据
	"bankInterface": "../Emmp_BankInfoList",
	// 加载开户行数据
	"bankBranchInterface": "../Emmp_BankBranchList",
	// 监听银行改变数据
	"detailInfoInterface": "../Emmp_BankInfoDetail",
	// 监听开户行改变数据
	"detailBranchInfoInterface": "../Emmp_BankBranchDetail",
	"updateInterface": "../Tgxy_CoopAgreementUpdate"
});