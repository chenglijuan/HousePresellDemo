(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tgpf_SpecialFundAppropriated_AFModel: {},
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
				detailVue.tgpf_SpecialFundAppropriated_AFModel = jsonObj.tgpf_SpecialFundAppropriated_AF;
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
			userUpdateId:this.userUpdateId,
			lastUpdateTimeStamp:this.lastUpdateTimeStamp,
			userRecordId:this.userRecordId,
			recordTimeStamp:this.recordTimeStamp,
			approvalState:this.approvalState,
			developCompanyId:this.developCompanyId,
			theNameOfDevelopCompany:this.theNameOfDevelopCompany,
			projectId:this.projectId,
			theNameOfProject:this.theNameOfProject,
			buildingId:this.buildingId,
			eCodeFromConstruction:this.eCodeFromConstruction,
			eCodeFromPublicSecurity:this.eCodeFromPublicSecurity,
			buildingAccountLogId:this.buildingAccountLogId,
			bankAccountId:this.bankAccountId,
			theAccountOfBankAccount:this.theAccountOfBankAccount,
			theNameOfBankAccount:this.theNameOfBankAccount,
			theBankOfBankAccount:this.theBankOfBankAccount,
			appropriatedType:this.appropriatedType,
			appropriatedRemark:this.appropriatedRemark,
			totalApplyAmount:this.totalApplyAmount,
			applyDate:this.applyDate,
			applyState:this.applyState,
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
	"detailDivId":"#tgpf_SpecialFundAppropriated_AFDiv",
	"detailInterface":"../Tgpf_SpecialFundAppropriated_AFDetail",
	"updateInterface":"../Tgpf_SpecialFundAppropriated_AFUpdate"
});
