(function(baseInfo){
	var updateVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion : 19000101,
			tableId : 1,
			empj_BuildingInfo : {
			},
			empj_BuildingInfoNew : {
			},
			//附件材料
			busiType : '03010201',
			loadUploadList: [],
			showDelete : false,
			tgpj_EscrowStandardVerMngId : "",
			tgpj_EscrowStandardVerMngList : [],
			buttonType : "",//按钮来源（保存、提交）
			approvalCode: "03010202",
			afId:null,
			isNeedBackup:false,
			sourcePage:null,
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			getEscrowStandardVerMngId : function (data){
				this.tgpj_EscrowStandardVerMngId = data.tableId;
			},
			showModal : showModal,
			approvalHandle : function ()
		    {
		        approvalModalVue.buttonType = 2;
		        approvalModalVue.approvalHandle();
		    },
		},
		computed:{
			 
		},
		components : {
			"my-uploadcomponent":fileUpload,
			'vue-select': VueSelect,
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
//			reqSource:"审批",
			afId:this.afId,
		}
	}

	//详情操作--------------
	function refresh()
	{
		if (updateVue.tableId == null || updateVue.tableId < 1) 
		{
			return;
		}

		getDetail();
		
		generalLoadFile2(updateVue, updateVue.busiType)
	}

	function getDetail()
	{
		new ServerInterfaceSync(baseInfo.detailInterface).execute(updateVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				updateVue.empj_BuildingInfo = jsonObj.empj_BuildingInfo;
				updateVue.empj_BuildingInfoNew = jsonObj.empj_BuildingInfoNew;
				updateVue.landMortgageAmount = addThousands(jsonObj.empj_BuildingInfoNew.landMortgageAmount == null ? "" : jsonObj.empj_BuildingInfoNew.landMortgageAmount);
				updateVue.isNeedBackup = jsonObj.isNeedBackup;
                approvalModalVue.isNeedBackup = updateVue.isNeedBackup;

				if(updateVue.empj_BuildingInfo.busiState == '未备案')
				{
					updateVue.approvalCode = '03010201'
				}
			}
		});
	}
	
	function initData()
	{
        var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var array = tableIdStr.split("_");
		if (array.length > 1)
		{
			updateVue.tableId = array[array.length-4];
			
			if(array[array.length-2] !=null)
			{
				approvalModalVue.workflowId = array[array.length-2];
			}
			approvalModalVue.sourcePage = array[array.length-1]; //来源页面
			updateVue.sourcePage = approvalModalVue.sourcePage;
			approvalModalVue.afId = array[array.length-3];
			updateVue.afId= array[array.length-3];
			
			refresh();
		}
        getEscrowStandardVerMngList();
	}
	
	function getEscrowStandardVerMngList()
	{
		var model = {
			interfaceVersion : 19000101,
			theState : 0,
		};
		new ServerInterface(baseInfo.escrowStandardVerMngListInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				updateVue.tgpj_EscrowStandardVerMngList = jsonObj.tgpj_EscrowStandardVerMngList;
			}
		});
	}
	
	function showModal()
	{
		approvalModalVue.getModalWorkflowList();
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	updateVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#sm_ApprovalProcess_BuildingDetailDiv",
	"detailInterface":"../Empj_BuildingInfoDetail",
	"escrowStandardVerMngListInterface":"../Tgpj_EscrowStandardVerMngForSelect",
});
