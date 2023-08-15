(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tgxy_TripleAgreementVerMngModel: {},
			tableId : 1,
			errtips:'',
			loadUploadList : [],
			showDelete : false,
			busiType : "06010103"
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			showModal : showModal,
			approvalHandle: approvalHandle,
		},
		computed:{
			 
		},
		components : {
			"my-uploadcomponent":fileUpload
		},
		watch:{
			
		}
	});
		
	//审批按钮
    function showModal()
	{
        approvalModalVue.getModalWorkflowList();
    }

    //备案按钮
    function approvalHandle()
    {
        approvalModalVue.buttonType = 2;
        approvalModalVue.approvalHandle();
    }

	//------------------------方法定义-开始------------------//
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:this.tableId,
			afId: this.afId,
		    workflowId: this.workflowId,
			busiCode : this.busiType,
		}
	}

	//详情操作--------------
	function refresh()
	{
		if (detailVue.tableId == null || detailVue.tableId < 1) 
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
//				noty({"text":jsonObj.info,"type":"error","timeout":2000});
				detailVue.errtips = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
					backdrop :'static'
				 });
			}
			else
			{
				detailVue.tgxy_TripleAgreementVerMngModel = jsonObj.Tgxy_TripleAgreementVerMng;
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
			}
		});
	}
	
	function initData()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var array = tableIdStr.split("_");
		if (array.length > 1)
		{
			/*detailVue.tableId = array[array.length-4];
			detailVue.afId = array[array.length-3];
			detailVue.workflowId = array[array.length-2];
			approvalModalVue.workflowId = detailVue.workflowId;
			approvalModalVue.sourcePage = array[array.length-1]; //来源页面
*/			
			detailVue.tableId = array[array.length-4];
			detailVue.afId = array[array.length-3];
			if(array[array.length-2]!=null)
			{
				detailVue.workflowId = array[array.length-2];
			}
			
			approvalModalVue.sourcePage = array[array.length-1]
			approvalModalVue.workflowId = detailVue.workflowId;
			approvalModalVue.afId = detailVue.afId;
			approvalModalVue.busiCode = detailVue.af_busiCode;
			
			refresh();
		}
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"errorModel":"#errorEscrowAdd",
	"successModel":"#successEscrowAdd",
	"detailDivId":"#tgxy_TripleAgreementVerMngDiv",
	"approvalModalListInterface":"../Sm_ApprovalProcess_ModalList",
	"detailInterface":"../Sm_ApprovalProcess_TripleAgreementVerMngDetail"
});
