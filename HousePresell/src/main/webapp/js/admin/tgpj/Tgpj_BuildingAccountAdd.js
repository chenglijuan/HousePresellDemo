(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			tgpj_BuildingAccountModel: {},
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
			developCompanyId:this.developCompanyId,
			eCodeOfDevelopCompany:this.eCodeOfDevelopCompany,
			projectId:this.projectId,
			theNameOfProject:this.theNameOfProject,
			buildingId:this.buildingId,
			eCodeOfBuilding:this.eCodeOfBuilding,
			escrowStandard:this.escrowStandard,
			escrowArea:this.escrowArea,
			buildingArea:this.buildingArea,
			orgLimitedAmount:this.orgLimitedAmount,
			currentFigureProgress:this.currentFigureProgress,
			currentLimitedRatio:this.currentLimitedRatio,
			nodeLimitedAmount:this.nodeLimitedAmount,
			totalGuaranteeAmount:this.totalGuaranteeAmount,
			cashLimitedAmount:this.cashLimitedAmount,
			effectiveLimitedAmount:this.effectiveLimitedAmount,
			totalAccountAmount:this.totalAccountAmount,
			spilloverAmount:this.spilloverAmount,
			payoutAmount:this.payoutAmount,
			appliedNoPayoutAmount:this.appliedNoPayoutAmount,
			applyRefundPayoutAmount:this.applyRefundPayoutAmount,
			refundAmount:this.refundAmount,
			currentEscrowFund:this.currentEscrowFund,
			allocableAmount:this.allocableAmount,
			recordAvgPriceOfBuildingFromPresellSystem:this.recordAvgPriceOfBuildingFromPresellSystem,
			recordAvgPriceOfBuilding:this.recordAvgPriceOfBuilding,
			logId:this.logId,
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
	"addDivId":"#tgpj_BuildingAccountDiv",
	"detailInterface":"../Tgpj_BuildingAccountDetail",
	"addInterface":"../Tgpj_BuildingAccountAdd"
});
