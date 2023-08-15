(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tgpf_BankUploadDataDetailModel: {},
			tableId : 1,
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			//更新
			getUpdateForm : getUpdateForm,
			update: update,
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
			tableId:this.tableId,
		}
	}

	//详情操作--------------
	function refresh()
	{
		if (this.tableId == null || this.tableId < 1) 
		{
			return;
		}

		getDetail();
	}

	function getDetail()
	{
		new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				detailVue.tgpf_BankUploadDataDetailModel = jsonObj.tgpf_BankUploadDataDetail;
			}
		});
	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getUpdateForm()
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
			bankId:this.bankId,
			theNameOfBank:this.theNameOfBank,
			bankBranchId:this.bankBranchId,
			theNameOfBankBranch:this.theNameOfBankBranch,
			bankAccountEscrowedId:this.bankAccountEscrowedId,
			theNameOfBankAccountEscrowed:this.theNameOfBankAccountEscrowed,
			theAccountBankAccountEscrowed:this.theAccountBankAccountEscrowed,
			theAccountOfBankAccountEscrowed:this.theAccountOfBankAccountEscrowed,
			tradeAmount:this.tradeAmount,
			enterTimeStamp:this.enterTimeStamp,
			recipientAccount:this.recipientAccount,
			recipientName:this.recipientName,
			lastCfgUser:this.lastCfgUser,
			lastCfgTimeStamp:this.lastCfgTimeStamp,
			bkpltNo:this.bkpltNo,
			eCodeOfTripleAgreement:this.eCodeOfTripleAgreement,
			reconciliationState:this.reconciliationState,
			reconciliationStamp:this.reconciliationStamp,
			remark:this.remark,
			coreNo:this.coreNo,
			reconciliationUser:this.reconciliationUser,
		}
	}

	function update()
	{
		new ServerInterface(baseInfo.updateInterface).execute(detailVue.getUpdateForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				$(baseInfo.detailDivId).modal('hide');
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
	"detailDivId":"#tgpf_BankUploadDataDetailDiv",
	"detailInterface":"../Tgpf_BankUploadDataDetailDetail",
	"updateInterface":"../Tgpf_BankUploadDataDetailUpdate"
});
