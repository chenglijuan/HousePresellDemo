(function(baseInfo){
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			tgxy_DepositAgreementModel: {},
			userStartId: 2,
			bankId: "",
			bankOfDepositName: "",
			escrowAccountName: "",
			bankOfDepositId: "",
			escrowAccountId: "",
			pageNumber: 0,
			emmp_BankInfoList: [],
			emmp_BankBranchList: [],
			tgxy_BankAccountEscrowedList: [],
			errorMsg: "",
			errTitle: "",
			uploadData : [],
			loadUploadList: [],
            showDelete : true,
            busiType : '06110101',
		},
		methods : {
			getSearchForm : getSearchForm,
			//添加
			getAddForm : getAddForm,
			update: update,
			bankData: bankData,
			bankOfDepositData: bankOfDepositData,
			getBankForm: getBankForm,
			getBankOfDepositForm: getBankOfDepositForm,
			escrowAccountData: escrowAccountData,
			getEscrowAccountForm: getEscrowAccountForm,
			errClose: errClose,
			succClose: succClose,
			look: look,
			look1: look1,
			getEscrowAccountLookForm: getEscrowAccountLookForm,
			getUploadForm : getUploadForm,
			loadUpload : loadUpload,
			getEscrowAccountName : function(data){
				addVue.escrowAccountName = data.tableId;
			}
		},
		computed:{
			 
		},
		components : {
			"my-uploadcomponent":fileUpload,
			'vue-select': VueSelect,
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
		}
	}
	
	//详情更新操作--------------获取"更新协定存款协议"参数
	function getAddForm()
	{
		var fileUploadList = addVue.$refs.listenUploadData.uploadData;
		fileUploadList = JSON.stringify(fileUploadList);
		return {
			interfaceVersion:this.interfaceVersion,
			theState:this.theState,
			busiState:this.busiState,
			eCode:this.tgxy_DepositAgreementModel.eCode,
			bankId:this.bankId,
			bankOfDepositId:this.bankOfDepositName,
			escrowAccountId:this.escrowAccountName,
			userStartId:this.userStartId,
			depositRate:this.tgxy_DepositAgreementModel.depositRate,
			orgAmount:this.tgxy_DepositAgreementModel.orgAmount,
			signDate:document.getElementById("date0611010102").value,
			timeLimit:this.tgxy_DepositAgreementModel.timeLimit,
			beginExpirationDate:document.getElementById("date0611010103").value,
			endExpirationDate:document.getElementById("date0611010104").value,
			remark:this.tgxy_DepositAgreementModel.remark,
			smAttachmentList:fileUploadList
		}
	}
	
	function getEscrowAccountLookForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			bankId: addVue.bankId,
		}
	}

	function update()
	{
		new ServerInterface(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				addVue.errTitle = "保存失败，请重试"
				addVue.errorMsg = jsonObj.info;
                $(baseInfo.errorModel).modal('show', {
				    backdrop :'static'
			    });
			}
			else
			{
				enterNext2Tab(jsonObj.tableId, '协定存款协议详情', 'tgxy/Tgxy_DepositAgreementDetail.shtml',jsonObj.tableId+"06110101");

				empty();
			}
		});
	}
	
	function empty()
	{
		addVue.tgxy_DepositAgreementModel = {};
		addVue.escrowAccountName = "";
		addVue.emmp_BankBranchList = [];
		addVue.emmp_BankInfoList = [];
		document.getElementById("date0611010102").value = "";
		document.getElementById("date0611010103").value = "";
		document.getElementById("date0611010104").value = "";
	}
	
	function errClose()
	{
		$(baseInfo.errorModel).modal('hide');
	}
	
	function succClose()
	{
		$(baseInfo.successModel).modal('hide');
	}
	
	// 添加日期控件
	laydate.render({
		elem: '#date0611010102',
	});
	laydate.render({
		elem: '#date0611010103',
	});
	laydate.render({
		elem: '#date0611010104',
	});
	
	//加载--------银行名称加载
	function bankData()
	{
		new ServerInterface(baseInfo.bankInterface).execute(addVue.getBankForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
                // 银行列表数据加载
                addVue.emmp_BankInfoList = jsonObj.emmp_BankInfoList;
			}
		});
	}
	
	// 参数---------银行名称数据组装
	function getBankForm()
	{
		return {
			interfaceVersion: this.interfaceVersion,
			pageNumber: this.pageNumber,
		}
	}
	
	//加载--------开户行加载
	function bankOfDepositData()
	{
		new ServerInterface(baseInfo.bankOfDepositInterface).execute(addVue.getBankOfDepositForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
                // 银行列表数据加载
                addVue.emmp_BankBranchList = jsonObj.emmp_BankBranchList;
			}
		});
	}
	
	// 参数---------开户行数据组装
	function getBankOfDepositForm()
	{
		return {
			interfaceVersion: this.interfaceVersion,
			pageNumber: this.pageNumber,
		}
	}
	
	//加载--------银行加载
	function escrowAccountData()
	{
		new ServerInterface(baseInfo.escrowAccountInterface).execute(addVue.getEscrowAccountForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
                // 银行列表数据加载
                addVue.emmp_BankInfoList = jsonObj.emmp_BankInfoList;
 			}
		});
	}
	
	function look(data)
	{
		addVue.bankId = data.tableId;
		addVue.bankOfDepositName = "";
		addVue.escrowAccountName = "";
		addVue.tgxy_BankAccountEscrowedList = [];
		addVue.emmp_BankBranchList = [];
		if(addVue.bankId != ""){
			new ServerInterface(baseInfo.escrowAccountDetailInterface).execute(addVue.getEscrowAccountLookForm(), function(jsonObj) {
				if(jsonObj.result != "success") {
					generalErrorModal(jsonObj);
				} else {
					addVue.emmp_BankBranchList = jsonObj.emmp_BankBranchList;
				}
			});
		}
	}
	
	function look1(data)
	{
		addVue.bankOfDepositName = data.tableId;
		addVue.escrowAccountName = "";
		addVue.tgxy_BankAccountEscrowedList = [];
		var model = {
			interfaceVersion: this.interfaceVersion,
			bankBranchId: addVue.bankOfDepositName,
		}
		if(addVue.bankOfDepositName != ""){
			new ServerInterface(baseInfo.escrowAccountDetail1Interface).execute(model, function(jsonObj) {
				if(jsonObj.result != "success") {
					generalErrorModal(jsonObj);
				} else {
					addVue.tgxy_BankAccountEscrowedList = jsonObj.tgxy_BankAccountEscrowedList;
					addVue.tgxy_BankAccountEscrowedList.forEach(function(item){
						item.theName = item.theAccount;
					})
				}
			});
		}
	}
	
	// 参数---------开户行数据组装
	function getEscrowAccountForm()
	{
		return {
			interfaceVersion: this.interfaceVersion,
			pageNumber: this.pageNumber,
		}
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
				//refresh();
				addVue.loadUploadList = jsonObj.sm_AttachmentCfgList;
			}
		});
	}
	
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	addVue.escrowAccountData();
	addVue.loadUpload();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#tgxy_DepositAgreementAddDiv",
	"errorModel":"#errorM2",
	"successModel":"#successM2",
	"detailInterface":"../Tgxy_DepositAgreementDetail",
	"addInterface":"../Tgxy_DepositAgreementAdd",
	// 银行名称加载
	"bankInterface":"../Emmp_BankInfoList",
	// 开户行加载
	"bankOfDepositInterface":"../Emmp_BankBranchList",
	// 银行加载
	"escrowAccountInterface":"../Emmp_BankInfoList",
	// 托管账号详情加载
	"escrowAccountDetailInterface":"../Emmp_BankBranchList",
	"escrowAccountDetail1Interface":"../Tgxy_BankAccountEscrowedPreByBankBranchList",
	"loadUploadInterface":"../Sm_AttachmentCfgList",
});
