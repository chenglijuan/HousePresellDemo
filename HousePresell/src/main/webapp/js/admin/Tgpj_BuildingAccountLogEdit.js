(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tgpj_BuildingAccountLogModel: {},
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
				detailVue.tgpj_BuildingAccountLogModel = jsonObj.tgpj_BuildingAccountLog;
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
			developCompanyId:this.developCompanyId,
			eCodeOfDevelopCompany:this.eCodeOfDevelopCompany,
			projectId:this.projectId,
			theNameOfProject:this.theNameOfProject,
			buildingId:this.buildingId,
			paymentId:this.paymentId,
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
			appropriateFrozenAmount:this.appropriateFrozenAmount,
			recordAvgPriceOfBuildingFromPresellSystem:this.recordAvgPriceOfBuildingFromPresellSystem,
			recordAvgPriceOfBuilding:this.recordAvgPriceOfBuilding,
			logId:this.logId,
			actualAmount:this.actualAmount,
			paymentLines:this.paymentLines,
			paymentProportion:this.paymentProportion,
			buildAmountPaid:this.buildAmountPaid,
			buildAmountPay:this.buildAmountPay,
			totalAmountGuaranteed:this.totalAmountGuaranteed,
			relatedBusiCode:this.relatedBusiCode,
			relatedBusiTableId:this.relatedBusiTableId,
			tgpj_BuildingAccountId:this.tgpj_BuildingAccountId,
			versionNo:this.versionNo,
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
	"detailDivId":"#tgpj_BuildingAccountLogDiv",
	"detailInterface":"../Tgpj_BuildingAccountLogDetail",
	"updateInterface":"../Tgpj_BuildingAccountLogUpdate"
});
