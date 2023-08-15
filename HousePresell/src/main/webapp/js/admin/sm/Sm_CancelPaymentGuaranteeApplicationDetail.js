(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			empj_PaymentGuarantee : {},
            showButton:false,
            errMsg :"",//存放错误提示信息
            loadUploadList: [],
            showDelete : false,
            busiType: "",
            subDisabled : false,
            hideShow : false,
            sm_ApprovalProcess_WorkflowList : [],
		    sm_ApprovalProcess_RecordList : [],
		    selectState : "1",
		    afId:"",
		    workflowId: "",
		    af_busiCode:"",
		    sm_ApprovalProcess_Handle : {},
		    tableId : 1,
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			//添加
			getDetail : getDetail,
			showModal :showModal ,
		},
		components : {
			"my-uploadcomponent":fileUpload
		},
	});

	//------------------------方法定义-开始------------------//
	
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion : this.interfaceVersion,
			tableId : detailVue.tableId,
			afId: this.afId,
		    workflowId: this.workflowId,
		    busiCode:'06120403',
		}
	}

	//详情操作--------------
	function refresh()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var array = tableIdStr.split("_");
		
        if (array.length > 1)
        {
        	detailVue.tableId = array[array.length-4];
        	console.log(detailVue.tableId);
        	detailVue.afId = array[array.length-3];
        	detailVue.workflowId = array[array.length-2];
            approvalModalVue.afId = detailVue.afId;
            approvalModalVue.workflowId = detailVue.workflowId;
            approvalModalVue.sourcePage = array[array.length-1];
            getDetail();
        }
	}
	
	function getDetail() {
		new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
				 generalErrorModal(jsonObj,jsonObj.info);
			} else {
				detailVue.empj_PaymentGuarantee = jsonObj.empj_PaymentGuarantee;
				detailVue.empj_PaymentGuarantee.actualAmount = addThousands(detailVue.empj_PaymentGuarantee.actualAmount);
				detailVue.empj_PaymentGuarantee.alreadyActualAmount = addThousands(detailVue.empj_PaymentGuarantee.alreadyActualAmount);
				detailVue.empj_PaymentGuarantee.guaranteedAmount = addThousands(detailVue.empj_PaymentGuarantee.guaranteedAmount);
				
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
				if (detailVue.loadUploadList != undefined && detailVue.loadUploadList.length > 0) {
					detailVue.hideShow = true;
				}
			}
		});
	}
	function showModal()
	{
		approvalModalVue.getModalWorkflowList();
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
	"addDivId":"#Empj_PaymentGuaranteeApplicationEditDiv",
	"detailInterface":"../Sm_ApprovalProcess_CancelPayDetail",
});
