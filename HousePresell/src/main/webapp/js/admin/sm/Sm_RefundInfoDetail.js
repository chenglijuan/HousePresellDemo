(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tgpf_RefundInfoModel: {
				fundFromLoan:"",
				unexpiredAmount:"",
				retainRightAmount : "",
				refundAmount : "",
				fundOfTripleAgreement : "",
				expiredAmount : "",
				actualRefundAmount : "",
			},
			sm_ApprovalProcess_Handle :{},
			tableId : 1,
		    smAttachmentList:[],//页面显示已上传的文件
		    loadUploadList  : [],
		    hideShow:false,
		    errMsg : "",
		    showDelete : false,
		    sm_ApprovalProcess_WorkflowList : [],
		    sm_ApprovalProcess_RecordList : [],
		    selectState : "1",
		    afId:"",
		    workflowId: "",
		    af_busiCode:"",
		    sm_Permission_Role : {},
		    theBankAccountEscrowedId : "",
		    tgxy_BankAccountEscrowedList : [],
		    theNameOfBank : "",
		    updateDisabled : false,
		    selectBank : false,
		    isUseButton : false,
		    refundTimeStamp:"",
		    sourcePage:"",//来源页面
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			showModal : showModal,
			indexMethod : indexMethod,
			choiceRefundBankAccount : choiceRefundBankAccount,
			choiceRefundBankAccountForm : choiceRefundBankAccountForm,
			loadForm : loadForm,
			getBankForm : getBankForm,
			getBankId : function(data){
				detailVue.theBankAccountEscrowedId = data;
				choiceRefundBankAccount();
			},
		   update : update,
		},
		computed:{
			 
		},
		components : {
			"my-uploadcomponent":fileUpload,
			'vue-select': VueSelect,
		},
		watch:{
			
		},
	});

	//------------------------方法定义-开始------------------//
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:this.tableId,
			afId: this.afId,
		    workflowId: this.workflowId,
		    busiCode:'06120201',
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
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				detailVue.errMsg = jsonObj.info;
			}
			else
			{
				detailVue.tgpf_RefundInfoModel = jsonObj.tgpf_RefundInfo;
				detailVue.theNameOfBank = jsonObj.tgpf_RefundInfo.refundBankName;
				detailVue.theBankAccountEscrowedId =  jsonObj.tgpf_RefundInfo.theBankAccountEscrowedId;
				//如果收款人类型为2时，显示开发企业的相关信息
				if(detailVue.tgpf_RefundInfoModel.receiverType == "2"){
					detailVue.tgpf_RefundInfoModel.receiverBankAccount = jsonObj.tgpf_RefundInfo.bAccountSupervised;
					detailVue.tgpf_RefundInfoModel.receiverName = jsonObj.tgpf_RefundInfo.developCompanyName;
					detailVue.tgpf_RefundInfoModel.receiverBankName = jsonObj.tgpf_RefundInfo.bBankName;
				}
				detailVue.tgpf_RefundInfoModel.fundFromLoan = addThousands(detailVue.tgpf_RefundInfoModel.fundFromLoan);
				detailVue.tgpf_RefundInfoModel.unexpiredAmount = addThousands(detailVue.tgpf_RefundInfoModel.unexpiredAmount);
				detailVue.tgpf_RefundInfoModel.retainRightAmount = addThousands(detailVue.tgpf_RefundInfoModel.retainRightAmount);
				detailVue.tgpf_RefundInfoModel.fundOfTripleAgreement = addThousands(detailVue.tgpf_RefundInfoModel.fundOfTripleAgreement);
				detailVue.tgpf_RefundInfoModel.expiredAmount = addThousands(detailVue.tgpf_RefundInfoModel.expiredAmount);
				detailVue.tgpf_RefundInfoModel.actualRefundAmount = addThousands(detailVue.tgpf_RefundInfoModel.actualRefundAmount);
				detailVue.tgpf_RefundInfoModel.refundAmount = addThousands(detailVue.tgpf_RefundInfoModel.refundAmount);
				
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
				detailVue.smAttachmentList = jsonObj.smAttachmentList;
				detailVue.af_busiCode = jsonObj.busiCode;
				
				var  isUse = jsonObj.isUseButten;
				if(isUse == '1'){
					detailVue.updateDisabled = true;
					detailVue.selectBank = true;
					detailVue.isUseButton = true;
				};
				detailVue.sm_Permission_Role = jsonObj.sm_Permission_Role;
				if(detailVue.smAttachmentList !=undefined && detailVue.smAttachmentList.length>0){
					detailVue.hideShow = true;
				}
			}
		});
	}
	function getBankForm(){
		return{
			interfaceVersion:this.interfaceVersion
		}
	}
	//
	function loadForm(){
		new ServerInterface(baseInfo.bankInterface).execute(detailVue.getBankForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				detailVue.errMsg = jsonObj.info;
			}
			else
			{
				//refresh();
				detailVue.tgxy_BankAccountEscrowedList = jsonObj.tgxy_BankAccountEscrowedList;
			}
		});
	}
	
	//-------------------选择退款账号
	function choiceRefundBankAccountForm(){
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:this.theBankAccountEscrowedId,
		}
	}
	//修改银行账号带出银行名称
	function choiceRefundBankAccount(){
		new ServerInterface(baseInfo.refundBankAccountInterface).execute(detailVue.choiceRefundBankAccountForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				detailVue.errMsg = jsonObj.info;
			}
			else
			{
				//refresh();
				detailVue.theNameOfBank = jsonObj.tgxy_BankAccountEscrowed.shortNameOfBank;
			}
		});
	}
	
	function update(){
		var model = {
				interfaceVersion:this.interfaceVersion,
				theBankAccountEscrowedId : this.theBankAccountEscrowedId,
				tableId:this.tableId,
				refundTimeStamp: this.refundTimeStamp,
		}
		new ServerInterface(baseInfo.updateInterface).execute(model, function(jsonObj)
				{
					if(jsonObj.result != "success")
					{
						$(baseInfo.edModelDivId).modal({
							backdrop :'static'
						});
						detailVue.errMsg = jsonObj.info;
					}
					else
					{
						//refresh();
						detailVue.updateDisabled = true;
						  detailVue.selectBank =  true;
						  getDetail();
					}
				});
	}
	
	function showModal(){
		approvalModalVue.getModalWorkflowList();
	}
	
	function indexMethod(){
		
	}
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}
	function initData()
	{
	   getButtonList();
	   
	   /*setTimeout(function(){
           var editFlag =  $("#editFlag").html();
           if(editFlag == "true"){
        	   detailVue.selectBank =  true;
           }
	  },200);*/
	   //ndexVue.companyFlag;
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
			detailVue.sourcePage = approvalModalVue.sourcePage;
			refresh();
		}
	}
	//------------------------数据初始化-开始----------------//
	/**
	 * 初始化日期插件
	 */
	laydate.render({
	  elem: '#dayEndBalanceResearch',
	  done:function(value, date, endDate){
		  detailVue.refundTimeStamp = value;
	  }
	});
	detailVue.loadForm();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tgpf_RefundInfoDetailDiv",
	"deleteDivId":"#delete",
	"bankInterface":"../Tgxy_BankAccountEscrowedList",
	"refundBankAccountInterface":"../Tgxy_BankAccountEscrowedDetail",
	"detailInterface":"../Sm_ApprovalProcess_RefundInfoDetail",
	"updateInterface":"../Tgpf_RefundInfoUpdate",
});
