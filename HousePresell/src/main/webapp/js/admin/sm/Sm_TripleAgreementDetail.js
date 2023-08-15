(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tgxy_TripleAgreementModel: {},
			tableId : 1,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			tgxy_TripleAgreementDetaillist:[],//买受人列表
			errtips:'',
			tgxy_TripleAgreementHt:{},
			smAttachmentList:[],//页面显示已上传的文件
			loadUploadList : [],
			showDelete : false,
			busiType : '06110301',
			afId:"",
		    workflowId: "",
		    af_busiCode:"06110301",
		    sm_ApprovalProcess_WorkflowList : [],
		    sm_ApprovalProcess_RecordList : [],
		    sm_ApprovalProcess_Handle :{},
		    selectState: "1",
		    isNeedBackup:""
		    	
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			indexMethod:indexMethod,
			changePageNumber : function(data){
				detailVue.pageNumber = data;
			},
			preCommand: preCommand,
			nextCommand: nextCommand,
			getPreForm: getPreForm,
			goToEditHandle: goToEditHandle,
			showModal: showModal,
			approvalHandle: approvalHandle
		},
		computed:{
			 
		},
		components : {
			"my-uploadcomponent":fileUpload
		},
		watch:{
			
		}
	});

	//------------------------方法定义-开始------------------//
	function indexMethod(index) {
		return generalIndexMethod(index, detailVue)
	}
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:this.tableId,
			afId: this.afId,
		    workflowId: this.workflowId
		}
	}

	//详情操作--------------
	function refresh()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var array = tableIdStr.split("_");
		detailVue.tableId = array[2];
		detailVue.afId = array[3];
		detailVue.workflowId = array[4];
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
				detailVue.errtips = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
					backdrop :'static'
				 });
			}
			else
			{
				detailVue.tgxy_TripleAgreementModel = jsonObj.tgxy_TripleAgreement;
				detailVue.tgxy_TripleAgreementDetaillist.forEach(function(item,index){
				detailVue.tgxy_TripleAgreementDetaillist[index].certificateTypeName="身份证";
				detailVue.tgxy_TripleAgreementDetaillist[index].agentCertTypeName="身份证";
				});
				detailVue.tgxy_TripleAgreementDetaillist = jsonObj.tgxy_BuyerInfoList;
				detailVue.tgxy_TripleAgreementHt = jsonObj.tgxy_ContractInfo;
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
				
				if(detailVue.tgxy_TripleAgreementHt.loanAmount != undefined) {
					detailVue.tgxy_TripleAgreementHt.loanAmount = thousandsToTwoDecimal(jsonObj.tgxy_ContractInfo.loanAmount);
				}else {
					detailVue.tgxy_TripleAgreementHt.loanAmount = "0.00";
				}
				if(detailVue.tgxy_TripleAgreementHt.contractSumPrice != undefined) {
					detailVue.tgxy_TripleAgreementHt.contractSumPrice = thousandsToTwoDecimal(jsonObj.tgxy_ContractInfo.contractSumPrice);
				}else {
					detailVue.tgxy_TripleAgreementHt.contractSumPrice = "0.00";
				}
				if(detailVue.tgxy_TripleAgreementModel.firstPaymentAmount != undefined) {
					detailVue.tgxy_TripleAgreementHt.firstPaymentAmount = thousandsToTwoDecimal(jsonObj.tgxy_TripleAgreement.firstPaymentAmount);
				}else {
					detailVue.tgxy_TripleAgreementHt.firstPaymentAmount = "0.00";
				}
				
			}
		});
	}
	

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
    
	// 上一条 
	function preCommand()
	{
		new ServerInterface(baseInfo.preInterface).execute(detailVue.getPreForm(), function(jsonObj)
				{
					if(jsonObj.result != "success")
					{
						detailVue.errtips = jsonObj.info;
						$(baseInfo.errorModel).modal('show', {
							backdrop :'static'
						 });
					}
					else
					{
						detailVue.tgxy_TripleAgreementModel = jsonObj.tgxy_TripleAgreement;
						detailVue.tgxy_TripleAgreementHt = jsonObj.tgxy_ContractInfo;
						detailVue.tgxy_TripleAgreementDetaillist = jsonObj.tgxy_BuyerInfoList;
					}
				});
	}
	
	function getPreForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId : detailVue.tableId,
		}
	}
	
	function nextCommand()
	{
		new ServerInterface(baseInfo.nextInterface).execute(detailVue.getPreForm(), function(jsonObj)
				{
			if(jsonObj.result != "success")
			{
				detailVue.errtips = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
					backdrop :'static'
				 });
			}
			else
			{
				detailVue.tgxy_TripleAgreementModel = jsonObj.tgxy_TripleAgreement;
				detailVue.tgxy_TripleAgreementDetaillist.forEach(function(item,index){
				detailVue.tgxy_TripleAgreementDetaillist[index].certificateTypeName="身份证";
				detailVue.tgxy_TripleAgreementDetaillist[index].agentCertTypeName="身份证";
				});
				detailVue.tgxy_TripleAgreementDetaillist = jsonObj.tgxy_BuyerInfoList;
				detailVue.tgxy_TripleAgreementHt = jsonObj.tgxy_ContractInfo;
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
				detailVue.tableId = jsonObj.tgxy_TripleAgreement.tableId;
				
			}
				});
	}
	
	function goToEditHandle()
	{
		enterNext2Tab(detailVue.tableId, '贷款托管三方协议修改', 'tgxy/Tgxy_TripleAgreementEdit.shtml',detailVue.tableId+"06110301");
	}
	
	function initData()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var array = tableIdStr.split("_");
		if (array.length > 1)
		{
			
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
	detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tgxy_TripleAgreementDiv",
	"errorModel":"#errorTripleDetail",
	"successModel":"#successTripleDetail",
	"detailInterface":"../Tgxy_TripleAgreementDetail",
	// 上一条
	"preInterface":"../Tgxy_TripleAgreementNextDetail",
	// 下一条
	"nextInterface":"../Tgxy_TripleAgreementNextDetail",
	"approvalModalListInterface":"../Sm_ApprovalProcess_ModalList",
	"passInterface":"../Sm_ApprovalProcess_Pass",  //通过
	"rejectInterface":"../Sm_ApprovalProcess_Reject",//驳回

});
