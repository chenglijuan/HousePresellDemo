(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			tgxy_CoopAgreementSettleModel: {},
			errtips:'',
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
			userStartId:this.userStartId,
			createTimeStamp:this.createTimeStamp,
			lastUpdateTimeStamp:this.lastUpdateTimeStamp,
			userRecordId:this.userRecordId,
			recordTimeStamp:this.recordTimeStamp,
			eCode:this.eCode,
			signTimeStamp:this.signTimeStamp,
			agentCompanyId:this.agentCompanyId,
			applySettlementDate:this.applySettlementDate,
			startSettlementDate:this.startSettlementDate,
			endSettlementDate:this.endSettlementDate,
			protocolNumbers:this.protocolNumbers,
			settlementState:this.settlementState,
		}
	}

	function add()
	{
		new ServerInterface(baseInfo.addInterface).execute(detailVue.getAddForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
//				noty({"text":jsonObj.info,"type":"error","timeout":2000});
				detailVue.errtips = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
					backdrop :'static'
				 })
			}
			else
			{
				$(baseInfo.successModel).modal('show', {
					backdrop :'static'
				 });
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
	"addDivId":"#tgxy_CoopAgreementSettleDiv",
	"errorModel":"#errorEscrowAdd",
	"successModel":"#successEscrowAdd",
	"detailInterface":"../Tgxy_CoopAgreementSettleDetail",
	"addInterface":"../Tgxy_CoopAgreementSettleAdd",
});

laydate.render({
  elem: '#StartTime2',
});
laydate.render({
  elem: '#EndTime2',
});
