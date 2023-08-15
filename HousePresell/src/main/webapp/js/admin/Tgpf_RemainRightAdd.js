(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			tgpf_RemainRightModel: {},
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
			enterTimeStamp:this.enterTimeStamp,
			buyer:this.buyer,
			theNameOfCreditor:this.theNameOfCreditor,
			idNumberOfCreditor:this.idNumberOfCreditor,
			eCodeOfContractRecord:this.eCodeOfContractRecord,
			eCodeOfTripleAgreement:this.eCodeOfTripleAgreement,
			srcBusiType:this.srcBusiType,
			projectId:this.projectId,
			theNameOfProject:this.theNameOfProject,
			buildingId:this.buildingId,
			eCodeOfBuilding:this.eCodeOfBuilding,
			buildingUnitId:this.buildingUnitId,
			eCodeOfBuildingUnit:this.eCodeOfBuildingUnit,
			eCodeFromRoom:this.eCodeFromRoom,
			actualDepositAmount:this.actualDepositAmount,
			depositAmountFromLoan:this.depositAmountFromLoan,
			theAccountFromLoan:this.theAccountFromLoan,
			fundProperty:this.fundProperty,
			bankId:this.bankId,
			theNameOfBankPayedIn:this.theNameOfBankPayedIn,
			theRatio:this.theRatio,
			theAmount:this.theAmount,
			limitedRetainRight:this.limitedRetainRight,
			withdrawableRetainRight:this.withdrawableRetainRight,
			currentDividedAmout:this.currentDividedAmout,
			remark:this.remark,
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
	"addDivId":"#tgpf_RemainRightDiv",
	"detailInterface":"../Tgpf_RemainRightDetail",
	"addInterface":"../Tgpf_RemainRightAdd"
});
