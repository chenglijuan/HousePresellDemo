(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			tgpf_BalanceOfAccountModel: {},
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
			userUpdateId:this.userUpdateId,
			lastUpdateTimeStamp:this.lastUpdateTimeStamp,
			userRecordId:this.userRecordId,
			recordTimeStamp:this.recordTimeStamp,
			billTimeStamp:this.billTimeStamp,
			bankName:this.bankName,
			escrowedAccount:this.escrowedAccount,
			escrowedAccountTheName:this.escrowedAccountTheName,
			centerTotalCount:this.centerTotalCount,
			centerTotalAmount:this.centerTotalAmount,
			bankTotalCount:this.bankTotalCount,
			bankTotalAmount:this.bankTotalAmount,
			cyberBankTotalCount:this.cyberBankTotalCount,
			cyberBankTotalAmount:this.cyberBankTotalAmount,
			accountType:this.accountType,
			reconciliationDate:this.reconciliationDate,
			reconciliationState:this.reconciliationState,
			bankBranchId:this.bankBranchId,
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
	"addDivId":"#tgpf_BalanceOfAccountDiv",
	"detailInterface":"../Tgpf_BalanceOfAccountDetail",
	"addInterface":"../Tgpf_BalanceOfAccountAdd"
});
