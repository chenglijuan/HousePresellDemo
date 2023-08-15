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
            //----------审批流start-----------//
            afId:"",//申请单Id
            workflowId:"",//结点Id
            isNeedBackup:"",//是否需要备案
            sourcePage:"",//来源页面
			userName:"",//登录用户名
			userId:"",//登录用户Id
            //----------审批流end-----------//
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			getEscrowStandardVerMngId : function (data){
				this.tgpj_EscrowStandardVerMngId = data.tableId;
			},
		},
		computed:{
			 
		},
		components : {
			"my-uploadcomponent":fileUpload,
			'vue-select': VueSelect,
            'approval-modal':approvalModalVue ,
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
		
		parent.generalLoadFileTest(updateVue, updateVue.busiType)
	}

	function getDetail()
	{
		new ServerInterfaceSync(baseInfo.detailInterface).execute(updateVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
                parent.generalErrorModal(jsonObj);
			}
			else
			{
				console.log(jsonObj);
				updateVue.empj_BuildingInfo = jsonObj.empj_BuildingInfo;
				updateVue.empj_BuildingInfoNew = jsonObj.empj_BuildingInfoNew;
				updateVue.landMortgageAmount = addThousands(jsonObj.empj_BuildingInfoNew.landMortgageAmount == null ? "" : jsonObj.empj_BuildingInfoNew.landMortgageAmount);
				updateVue.isNeedBackup = jsonObj.isNeedBackup;

				if(updateVue.empj_BuildingInfo.busiState == '未备案')
				{
					updateVue.approvalCode = '03010201'
				}
			}
		});
	}
	
	function initData()
	{
        var tableIdStr = parent.getTabElementUI_TableId();
		var array = tableIdStr.split("_");
		if (array.length > 1)
		{
            updateVue.tableId = array[array.length-4];
            updateVue.afId = array[array.length-3];
            updateVue.workflowId = array[array.length-2];
            updateVue.sourcePage = array[array.length-1];
			refresh();
		}
		updateVue.userName = parent.getUserName();
        updateVue.userId = parent.getUserId();
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
                parent.generalErrorModal(jsonObj);
			}
			else
			{
				updateVue.tgpj_EscrowStandardVerMngList = jsonObj.tgpj_EscrowStandardVerMngList;
			}
		});
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	updateVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#sm_ApprovalProcess_BuildingDetailDiv",
	"detailInterface":"../../Empj_BuildingInfoDetail",
	"escrowStandardVerMngListInterface":"../../Tgpj_EscrowStandardVerMngForSelect",
});
