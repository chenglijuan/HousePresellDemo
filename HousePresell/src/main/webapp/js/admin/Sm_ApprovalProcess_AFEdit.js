(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			sm_ApprovalProcess_AFModel: {},
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
				detailVue.sm_ApprovalProcess_AFModel = jsonObj.sm_ApprovalProcess_AF;
			}
		});
	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getUpdateForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			configurationId:this.configurationId,
			companyInfoId:this.companyInfoId,
			eCode:this.eCode,
			startTimeStamp:this.startTimeStamp,
			theState:this.theState,
			busiState:this.busiState,
			sourceId:this.sourceId,
			sourceType:this.sourceType,
			orgObjJsonFilePath:this.orgObjJsonFilePath,
			expectObjJsonFilePath:this.expectObjJsonFilePath,
			attachmentList:this.attachmentList,
			workFlowList:this.workFlowList,
			currentIndex:this.currentIndex,
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
	"detailDivId":"#sm_ApprovalProcess_AFDiv",
	"detailInterface":"../Sm_ApprovalProcess_AFDetail",
	"updateInterface":"../Sm_ApprovalProcess_AFUpdate"
});
