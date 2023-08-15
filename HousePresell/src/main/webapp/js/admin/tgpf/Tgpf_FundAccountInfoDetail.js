(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tgpf_FundAccountInfoModel: {},
			tableId : 1,
			tgpf_FundAccountInfoDetailList: [],
			eCodeOfBuilding: "",
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			update: update,
			getUpdateForm: getUpdateForm,
			errClose: errClose,
			succClose: succClose,
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
			tableId:detailVue.tableId,
		}
	}
	
	function getUpdateForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:detailVue.tableId,
			fullNameOfCompanyFromFinanceSystem:detailVue.tgpf_FundAccountInfoModel.fullNameOfCompanyFromFinanceSystem,
			fullNameOfProjectFromFinanceSystem:detailVue.tgpf_FundAccountInfoModel.fullNameOfProjectFromFinanceSystem,
			shortNameOfBuildingFromFinanceSystem:detailVue.tgpf_FundAccountInfoModel.shortNameOfBuildingFromFinanceSystem,
			fullNameOfBuildingFromFinanceSystem:detailVue.tgpf_FundAccountInfoModel.fullNameOfBuildingFromFinanceSystem,
			shortNameOfBuildingFromFinanceSystem:detailVue.tgpf_FundAccountInfoModel.shortNameOfBuildingFromFinanceSystem,
			financeRemark:detailVue.tgpf_FundAccountInfoModel.financeRemark,
			depositRemark:detailVue.tgpf_FundAccountInfoModel.depositRemark,
		}
	}

	//详情操作--------------
	function refresh()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		tableIdStr = tableIdStr.split("_");
        detailVue.tableId = tableIdStr[3];
		if(detailVue.tableId == null || detailVue.tableId < 1) {
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
				$(baseInfo.errorModel).modal('show', {
				    backdrop :'static'
			    });
				/*noty({"text":jsonObj.info,"type":"error","timeout":2000});*/
			}
			else
			{
				detailVue.tgpf_FundAccountInfoModel = jsonObj.tgpf_FundAccountInfo;
				detailVue.eCodeOfBuilding = detailVue.tgpf_FundAccountInfoModel.eCodeOfBuilding;
				if(detailVue.eCodeOfBuilding == "" || detailVue.eCodeOfBuilding == undefined){
					console.log(detailVue.eCodeOfBuilding);
					detailVue.eCodeOfBuilding = "—";
				}
			}
		});
	}
	
	function update()
	{
		new ServerInterface(baseInfo.updateInterface).execute(detailVue.getUpdateForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				$(baseInfo.errorModel).modal('show', {
				    backdrop :'static'
			    });
				/*noty({"text":jsonObj.info,"type":"error","timeout":2000});*/
			}
			else
			{
				generalSuccessModal();
				detailVue.tgpf_FundAccountInfoDetailList = [];
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
		
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tgpf_FundAccountInfoEditDiv",
	"errorModel":"#errorDF",
	"successModel":"#successDF",
	"detailInterface":"../Tgpf_FundAccountInfoDetail",
	"updateInterface":"../Tgpf_FundAccountInfoUpdate"
});
