(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			empj_BuildingInfo_AFModel: {},
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
			eCodeOfProject:this.eCodeOfProject,
			buildingId:this.buildingId,
			eCodeOfBuilding:this.eCodeOfBuilding,
			buildingArea:this.buildingArea,
			escrowArea:this.escrowArea,
			deliveryType:this.deliveryType,
			upfloorNumber:this.upfloorNumber,
			downfloorNumber:this.downfloorNumber,
			landMortgageState:this.landMortgageState,
			landMortgagor:this.landMortgagor,
			landMortgageAmount:this.landMortgageAmount,
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
	"addDivId":"#empj_BuildingInfo_AFDiv",
	"detailInterface":"../Empj_BuildingInfo_AFDetail",
	"addInterface":"../Empj_BuildingInfo_AFAdd"
});
