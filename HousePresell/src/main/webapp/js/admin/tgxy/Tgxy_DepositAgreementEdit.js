(function(baseInfo){
	var editVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tgxy_DepositAgreementModel: {},
			tableId : 1,
			userUpdateId: 1,
			bankId: "",
			bankOfDepositName: "",
			escrowAccountName: "",
			emmp_BankInfoList: [],
			emmp_BankBranchList: [],
			tgxy_BankAccountEscrowedList: [],
			pkId: "",
			createPeo: [],
			editPeo: [],
			bankOfDepositId: "",
			errMsg: "",
			smAttachmentList:[],
            loadUploadList : [],
            showDelete : true,
            busiType : '06110101',
            successMsg: ""
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			//更新
			getUpdateForm : getUpdateForm,
			update: update,
			bankData: bankData,
			bankBranchData: bankBranchData,
			getEscrowAccountdata: getEscrowAccountdata,
			look: look,
			errClose: errClose,
			succClose: succClose,
			getEscrowAccountLookForm: getEscrowAccountLookForm,
		},
		computed:{
			 
		},
		components : {
			"my-uploadcomponent":fileUpload
		},
		watch:{
			
		}
	});

	//------------------------方法定义-开始------------------//
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:editVue.pkId,
		}
	}

	//详情操作--------------
	function refresh()
	{
		if (this.tableId == null || this.tableId < 1) 
		{
			return;
		}
	}

	function getDetail()
	{
		new ServerInterface(baseInfo.detailInterface).execute(editVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				editVue.tgxy_DepositAgreementModel = jsonObj.tgxy_DepositAgreement;
				editVue.escrowAccountName = jsonObj.tgxy_DepositAgreement.escrowAccount.tableId;
				editVue.createPeo = jsonObj.tgxy_DepositAgreement.userStart;
				editVue.editPeo = jsonObj.tgxy_DepositAgreement.userUpdate;
				editVue.bankOfDepositId = jsonObj.tgxy_DepositAgreement.bankOfDepositId;
				editVue.smAttachmentList = jsonObj.smAttachmentList;
				editVue.loadUploadList = jsonObj.smAttachmentCfgList;
				
				editVue.bankId = 1;
				editVue.emmp_BankInfoList.push({
					tableId: 1,
					theName: jsonObj.tgxy_DepositAgreement.theNameOfBank
				});
				editVue.bankOfDepositName = 1;
				editVue.emmp_BankBranchList.push({
					tableId: 1,
					theName: jsonObj.tgxy_DepositAgreement.bankOfDeposit
				});
			}
		});
	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getUpdateForm()
	{
		var fileUploadList = editVue.$refs.listenUploadData.uploadData;
		fileUploadList = JSON.stringify(fileUploadList);
		return {
			interfaceVersion:this.interfaceVersion,
			eCode:this.tgxy_DepositAgreementModel.eCode,
			bankId:this.bankId,
			bankOfDepositId:editVue.bankOfDepositId,
			escrowAccountId:this.escrowAccountName,
			depositRate:this.tgxy_DepositAgreementModel.depositRate,
			orgAmount:this.tgxy_DepositAgreementModel.orgAmount,
			signDate:document.getElementById("date0611010107").value,
			timeLimit:this.tgxy_DepositAgreementModel.timeLimit,
			beginExpirationDate:document.getElementById("date0611010105").value,
			endExpirationDate:document.getElementById("date0611010106").value,
			remark:this.tgxy_DepositAgreementModel.remark,
			bankId:this.bankId,
			userUpdateId:this.userUpdateId,
			tableId:this.pkId,
			smAttachmentList:fileUploadList,
		}
	}

	function update()
	{
		new ServerInterface(baseInfo.updateInterface).execute(editVue.getUpdateForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				editVue.errMsg = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
				    backdrop :'static'
			    });
			}
			else
			{
				enterNext2Tab(editVue.pkId, '协定存款协议详情', 'tgxy/Tgxy_DepositAgreementDetail.shtml',editVue.pkId+"06110101");
				refresh();
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
	
	function initData()
	{
		
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var list = tableIdStr.split("_");
		tableIdStr = list[2];
        editVue.pkId = tableIdStr;
        getEscrowAccountdata();
	}
	
	// 获取银行名称数据
	function bankData()
	{
		new ServerInterface(baseInfo.bankInterface).execute(editVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				editVue.emmp_BankInfoList = jsonObj.emmp_BankInfoList;
                bankBranchData();
			}
		});
	}
	
	// 获取开户行数据
	function bankBranchData()
	{
		new ServerInterface(baseInfo.bankBranchInterface).execute(editVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				editVue.emmp_BankBranchList = jsonObj.emmp_BankBranchList;
			}
		});
		
	}
	
	// 获取托管账号数据
	function getEscrowAccountdata()
	{
		new ServerInterface(baseInfo.accountEscrowedInterface).execute(editVue.getSearchForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
                // 托管账号数据加载
				editVue.tgxy_BankAccountEscrowedList = jsonObj.tgxy_BankAccountEscrowedList;
			}
		});

		getDetail();
	}
	
	function look()
	{
		editVue.tgxy_DepositAgreementModel.signDate = document.getElementById("date0611010107").value;
		editVue.tgxy_DepositAgreementModel.beginExpirationDate = document.getElementById("date0611010105").value;
		editVue.tgxy_DepositAgreementModel.endExpirationDate = document.getElementById("date0611010106").value;
		new ServerInterface(baseInfo.escrowAccountDetailInterface).execute(editVue.getEscrowAccountLookForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
				editVue.bankId = jsonObj.tgxy_BankAccountEscrowed.bankId;
				editVue.emmp_BankInfoList.push({
                	tableId:editVue.bankId,
                	theName:jsonObj.tgxy_BankAccountEscrowed.bank.theName
                });
				editVue.bankOfDepositName = jsonObj.tgxy_BankAccountEscrowed.bankBranchId;
				editVue.emmp_BankBranchList.push({
                	tableId:editVue.bankOfDepositName,
                	theName:jsonObj.tgxy_BankAccountEscrowed.bankBranch.theName
                });
                
			}
		});
	}
	
	function getEscrowAccountLookForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId: editVue.escrowAccountName,
		}
	}

	
	// 添加日期控件
	laydate.render({
		elem: '#date0611010107',
	});
	laydate.render({
		elem: '#date0611010105',
	});
	laydate.render({
		elem: '#date0611010106',
	});
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	editVue.refresh();
	editVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tgxy_DepositAgreementDiv",
	"errorModel":"#depositErrorM",
	"successModel":"#depositSuccessM",
	"detailInterface":"../Tgxy_DepositAgreementDetail",
	"updateInterface":"../Tgxy_DepositAgreementUpdate",
	"bankInterface":"../Emmp_BankInfoList",
	"bankBranchInterface":"../Emmp_BankBranchList",
	"accountEscrowedInterface":"../Tgxy_BankAccountEscrowedList",
	// 托管账号详情加载
	"escrowAccountDetailInterface":"../Tgxy_BankAccountEscrowedDetail",
});
