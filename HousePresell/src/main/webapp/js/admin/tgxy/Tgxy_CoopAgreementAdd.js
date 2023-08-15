(function(baseInfo) {
	var addVue = new Vue({
		el: baseInfo.addDivId,
		data: {
			interfaceVersion: 19000101,
			tgxy_CoopAgreementModel: {},
			bankId: "",
			bankOfDepositId: "",
			userStartId: 1,
			pageNumber: 0,
			emmp_BankInfoList: [
				{ tableId: 0, theName: '银行1' },
				{ tableId: 1, theName: '银行2' },
				{ tableId: 2, theName: '银行3' }
			],
			emmp_BankBranchList: [
				{ tableId: 0, theName: '银行4' },
				{ tableId: 1, theName: '银行5' },
				{ tableId: 2, theName: '银行6' }
			],
			theNameOfBank: null,
			theNameOfDepositBank: null,
			tableId: "",
			eCode: "",
			bank: "",
			errMsg: "",
			loadUploadList: [],
            showDelete : true,
            busiType : '06110103',
		},
		methods: {
			//添加
			getAddForm: getAddForm,
			add: addCoopAgreement,
			getBankData: getBankData,
//			getBankBranchData: getBankBranchData,
			getBankForm: getBankForm,
			getBankBranchForm: getBankBranchForm,
			lookBank: lookBank,
			lookOpenBank: lookOpenBank,
			getBankDetailForm: getBankDetailForm,
			getBankBranchDetailForm: getBankBranchDetailForm,
			errClose: errClose,
			succClose: succClose,

			getUploadForm : getUploadForm,
			loadUpload : loadUpload,
			
			getBankId: function (data){
				this.bankId = data.tableId;
				addVue.lookBank();
			},
			getDepositId: function(data) {
				this.bankOfDepositId = data.tableId;
				addVue.lookOpenBank();
			}
		},
		computed: {

		},
		components: {
			"my-uploadcomponent":fileUpload,
			'vue-select': VueSelect,
		},
		watch: {

		}
	});

	//------------------------方法定义-开始------------------//
	//保存操作--------------获取"合作协议新增"参数
	function getAddForm() {
		var fileUploadList = addVue.$refs.listenUploadData.uploadData;
		fileUploadList = JSON.stringify(fileUploadList);
		return {
			interfaceVersion: this.interfaceVersion,
			eCode: addVue.tgxy_CoopAgreementModel.eCode,
			userStartId: addVue.userStartId,
			bankId: addVue.bankId,
			theNameOfBank: addVue.theNameOfBank,
			bankOfDepositId: addVue.bankOfDepositId,
			theNameOfDepositBank: addVue.theNameOfDepositBank,
			signDate: document.getElementById('date0611010302').value,
			smAttachmentList:fileUploadList
		}
	}

	// 银行数据参数组装
	function getBankForm() {
		return {
			interfaceVersion: this.interfaceVersion,
			pageNumber: this.pageNumber
		}
	}

	// 开户行数据组装
	function getBankBranchForm() {
		return {
			interfaceVersion: this.interfaceVersion,
			pageNumber: this.pageNumber
		}
	}

	// 银行名称数据组装
	function getBankDetailForm() {
		return {
			interfaceVersion: this.interfaceVersion,
			tableId: addVue.bankId,
			bankId: addVue.bankId,
		}
	}

	// 开户行名称数据组装
	function getBankBranchDetailForm() {
		return {
			interfaceVersion: this.interfaceVersion,
			tableId: addVue.bankOfDepositId,
		}
	}

	// 添加日期控件
	laydate.render({
		elem: '#date0611010302',
	});

	// 加载银行数据
	function getBankData() {
		new ServerInterface(baseInfo.bankInterface).execute(addVue.getBankForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
				// 银行列表数据加载
				addVue.emmp_BankInfoList = jsonObj.emmp_BankInfoList;
			}
		});
	}

	// 加载开户行数据
//	function getBankBranchData() {
//		new ServerInterface(baseInfo.bankBranchInterface).execute(addVue.getBankBranchForm(), function(jsonObj) {
//			if(jsonObj.result != "success") {
//				noty({ "text": jsonObj.info, "type": "error", "timeout": 2000 });
//			} else {
//				// 开户行列表数据加载
//				addVue.emmp_BankBranchList = jsonObj.emmp_BankBranchList;
//			}
//		});
//	}

	// 新增数据保存
	function addCoopAgreement() {
		new ServerInterface(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				addVue.errMsg = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
				    backdrop :'static'
			    });
			} else {
				enterNext2Tab(jsonObj.tableId, '合作协议详情', 'tgxy/Tgxy_CoopAgreementDetail.shtml',jsonObj.tableId+"06110103");
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

	// 银行名称-根据选择的下拉框数据的id获取对应的text值
	function lookBank() {
		console.log("bankId:" + addVue.bankId);
		new ServerInterface(baseInfo.bankDetailInterface).execute(addVue.getBankDetailForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
				console.log(jsonObj);
				addVue.theNameOfBank = jsonObj.emmp_BankInfo.theName;

				// 获取选中的银行对应的开户行数据
				new ServerInterface(baseInfo.bankBranchInterface).execute(addVue.getBankDetailForm(), function(jsonObj) {
					if(jsonObj.result != "success") {
						generalErrorModal(jsonObj);
					} else {
						// 开户行列表数据加载
				        addVue.emmp_BankBranchList = jsonObj.emmp_BankBranchList;
					}
				});
			}
		});
	}

	// 开户行-根据选择的下拉框数据的id获取对应的text值
	function lookOpenBank() {
		console.log("bankOfDepositId:" + addVue.bankOfDepositId);
		new ServerInterface(baseInfo.bankBranchDetailInterface).execute(addVue.getBankBranchDetailForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
				console.log(jsonObj);
				addVue.theNameOfDepositBank = jsonObj.emmp_BankBranch.theName;
			}
		});
	}
	
	//列表操作-----------------------获取附件参数
	function getUploadForm(){
		return{
			pageNumber : '0',
			busiType : addVue.busiType,
			interfaceVersion:this.interfaceVersion
		}
	}
	//列表操作-----------------------页面加载显示附件类型
	function loadUpload(){
		new ServerInterface(baseInfo.loadUploadInterface).execute(addVue.getUploadForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				$(baseInfo.errorModel).modal("show",{
					backdrop :'static'
				});
				addVue.errMsg = jsonObj.info;
			}
			else
			{
				addVue.loadUploadList = jsonObj.sm_AttachmentCfgList;
			}
		});
	}

	//------------------------方法定义-结束------------------//

	//------------------------数据初始化-开始----------------//
	// 加载银行数据
	addVue.getBankData();
	addVue.loadUpload();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId": "#tgxy_CoopAgreementAddDiv",
	"errorModel":"#eaModel",
	"successModel":"#saModel",
	// 加载银行名称数据
	"bankInterface": "../Emmp_BankInfoList",
	// 加载开户行名称数据
	"bankBranchInterface": "../Emmp_BankBranchList",
	// 加载选中的银行名称
	"bankDetailInterface": "../Emmp_BankInfoDetail",
	// 加载选中的开户行名称
	"bankBranchDetailInterface": "../Emmp_BankBranchDetail",
	// 新增合作协议数据
	"addInterface": "../Tgxy_CoopAgreementAdd",
	"loadUploadInterface":"../Sm_AttachmentCfgList",
});