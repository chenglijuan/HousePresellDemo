(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			tgpf_DepositDetailModel: {},
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			//添加
			getAddForm : getAddForm,
			add: add,
		},
		computed:{
			 
		},
		components : {

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

	//详情操作--------------
	function refresh()
	{

	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getAddForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			theState:this.theState,
			busiState:this.busiState,
			eCode:this.eCode,
			userStartId:this.userStartId,
			createTimeStamp:this.createTimeStamp,
			lastUpdateTimeStamp:this.lastUpdateTimeStamp,
			userRecordId:this.userRecordId,
			recordTimeStamp:this.recordTimeStamp,
			eCodeFromPayment:this.eCodeFromPayment,
			fundProperty:this.fundProperty,
			bankAccountEscrowedId:this.bankAccountEscrowedId,
			bankBranchId:this.bankBranchId,
			theNameOfBankAccountEscrowed:this.theNameOfBankAccountEscrowed,
			theAccountOfBankAccountEscrowed:this.theAccountOfBankAccountEscrowed,
			theNameOfCreditor:this.theNameOfCreditor,
			idType:this.idType,
			idNumber:this.idNumber,
			bankAccountForLoan:this.bankAccountForLoan,
			loanAmountFromBank:this.loanAmountFromBank,
			billTimeStamp:this.billTimeStamp,
			eCodeFromBankCore:this.eCodeFromBankCore,
			eCodeFromBankPlatform:this.eCodeFromBankPlatform,
			remarkFromDepositBill:this.remarkFromDepositBill,
			theNameOfBankBranchFromDepositBillId:this.theNameOfBankBranchFromDepositBillId,
			eCodeFromBankWorker:this.eCodeFromBankWorker,
			tripleAgreementId:this.tripleAgreementId,
			depositState:this.depositState,
			dayEndBalancingId:this.dayEndBalancingId,
			depositDatetime:this.depositDatetime,
			reconciliationTimeStampFromBusiness:this.reconciliationTimeStampFromBusiness,
			reconciliationStateFromBusiness:this.reconciliationStateFromBusiness,
			reconciliationTimeStampFromCyberBank:this.reconciliationTimeStampFromCyberBank,
			reconciliationStateFromCyberBank:this.reconciliationStateFromCyberBank,
			hasVoucher:this.hasVoucher,
			timestampFromReverse:this.timestampFromReverse,
			theStateFromReverse:this.theStateFromReverse,
			eCodeFromReverse:this.eCodeFromReverse,
		}
	}

	function add()
	{
		new ServerInterface(baseInfo.addInterface).execute(detailVue.getAddForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				refresh();
			}
		});
	}
	
	function initData()
	{
		
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#tgpf_DepositDetailDiv",
	"detailInterface":"../Tgpf_DepositDetailDetail",
	"addInterface":"../Tgpf_DepositDetailAdd"
});
