(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
            errMsg :"",//存放错误提示信息
            empj_PaymentGuaranteeApplicationDetaiModel : {},
            loadUploadList : [],
            showDelete : false,
            busiType : "",
            tableId:"1",
            empj_PaymentGuaranteeApplicationDetailList : [],
            qs_companyNameList : [], //页面加载显示开发企业
			qs_projectNameList : [], //显示项目名称
			guaranteeCompanyList : [],//
			subDisabled : false,
			sm_ApprovalProcess_WorkflowList : [],
		    sm_ApprovalProcess_RecordList : [],
			sm_ApprovalProcess_Handle :{},
		    selectState : "1",
		    afId:"",
		    workflowId: "",
		    af_busiCode:"06120401",
		    sourcePage:"",//来源页面
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			indexMethod : indexMethod,
			loadCompanyName : loadCompanyNameFun, //页面加载显示开发企业方法
			changeCompanyHandle : changeCompanyHandleFun,//改变项目名称
			loadGuaranteeCompany: loadGuaranteeCompany,
			showModal : showModal,
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
	
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		
		return{
			interfaceVersion:this.interfaceVersion,
			busiCode : "06120401", //业务编码
			afId: this.afId,
		    workflowId: this.workflowId,
		    tableId : this.tableId
		}
	}
	//列表操作-----------------------页面加载显示开发企业
		function loadCompanyNameFun() {
			loadCompanyName(detailVue,baseInfo.companyNameInterface,function(jsonObj){
				detailVue.qs_companyNameList = jsonObj.emmp_CompanyInfoList;
			},detailVue.errMsg,baseInfo.edModelDivId);
		}
		//列表操作--------------------改变开发企业的方法
		function changeCompanyHandleFun() {
			var model ={
					interfaceVersion : this.interfaceVersion,
					developCompanyId : detailVue.empj_PaymentGuaranteeApplicationDetaiModel.companyId,
			}
			new ServerInterface(baseInfo.changeCompanyInterface).execute(model, function(jsonObj)
				{
					if(jsonObj.result != "success")
					{
						detailVue.errMsg = jsonObj.info;
						$(baseInfo.edModelDivId).modal('show', {
							backdrop:'static'
						});
					}
					else
					{
						detailVue.qs_projectNameList= jsonObj.empj_ProjectInfoList;
					}
				});
		}
		//新增操作-----------------------支付保证出具单位
		function loadGuaranteeCompany(){
			var model ={
					interfaceVersion : this.interfaceVersion,	
			}
			new ServerInterface(baseInfo.companyNameInterface).execute(model, function(jsonObj)
			{
				if(jsonObj.result != "success")
				{
					detailVue.errMsg = jsonObj.info;
					$(baseInfo.edModelDivId).modal('show', {
						backdrop:'static'
					});
				}
				else
				{
					detailVue.guaranteeCompanyList = jsonObj.emmp_CompanyInfoList;
				}
			});
		}
		
		
		function showModal()
		{
			approvalModalVue.getModalWorkflowList();
		}
	//详情操作--------------
	function refresh()
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
				detailVue.empj_PaymentGuaranteeApplicationDetaiModel = jsonObj.empj_PaymentGuarantee;
				detailVue.empj_PaymentGuaranteeApplicationDetaiModel.alreadyActualAmount = addThousands(detailVue.empj_PaymentGuaranteeApplicationDetaiModel.alreadyActualAmount);
				detailVue.empj_PaymentGuaranteeApplicationDetaiModel.actualAmount = addThousands(detailVue.empj_PaymentGuaranteeApplicationDetaiModel.actualAmount);
				detailVue.empj_PaymentGuaranteeApplicationDetaiModel.guaranteedAmount = addThousands(detailVue.empj_PaymentGuaranteeApplicationDetaiModel.guaranteedAmount);

				detailVue.empj_PaymentGuaranteeApplicationDetailList = jsonObj.empj_PaymentGuaranteeChildList;
				detailVue.empj_PaymentGuaranteeApplicationDetailList.forEach(function(item){
					item.buildAmountPaid = addThousands(item.buildAmountPaid);
					item.buildAmountPay = addThousands(item.buildAmountPay);
					item.totalAmountGuaranteed = addThousands(item.totalAmountGuaranteed);
					item.amountGuaranteed = addThousands(item.amountGuaranteed);
					item.paymentLines = addThousands(item.paymentLines);
					item.cashLimitedAmount = addThousands(item.cashLimitedAmount);
					item.recordAvgPriceOfBuilding = addThousands(item.recordAvgPriceOfBuilding);
					item.orgLimitedAmount = addThousands(item.orgLimitedAmount);
					item.nodeLimitedAmount = addThousands(item.nodeLimitedAmount);
					item.effectiveLimitedAmount = addThousands(item.effectiveLimitedAmount);
					item.totalAccountAmount = addThousands(item.totalAccountAmount);
					item.payoutAmount = addThousands(item.payoutAmount);
					item.appropriateFrozenAmount = addThousands(item.appropriateFrozenAmount);
					item.appliedNoPayoutAmount = addThousands(item.appliedNoPayoutAmount);
					item.spilloverAmount = addThousands(item.spilloverAmount);
					item.releaseTheAmount = addThousands(item.releaseTheAmount);
					item.buildProjectPaid = addThousands(item.buildProjectPaid);
					item.buildProjectPay = addThousands(item.buildProjectPay);
					item.paymentProportion = addThousands(item.paymentProportion);
				})
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
				detailVue.changeCompanyHandle();
				detailVue.loadGuaranteeCompany();
			}
			
		});
	}
	function indexMethod(index){
		console.log(generalIndexMethod(index,detailVue));
		return generalIndexMethod(index,detailVue);
	}
	function initData()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var array = tableIdStr.split("_");
//		if (array.length > 1)
//		{
//			detailVue.tableId = array[array.length-3];
//			detailVue.afId = array[array.length-2];
//			detailVue.workflowId = array[array.length-1];
//			approvalModalVue.afId = detailVue.afId;
//			approvalModalVue.workflowId = detailVue.workflowId;
//			refresh();
//		}
		
        if (array.length > 1)
        {
        	detailVue.tableId = array[array.length-4];
        	detailVue.afId = array[array.length-3];
        	detailVue.workflowId = array[array.length-2];
            approvalModalVue.afId = detailVue.afId;
            approvalModalVue.workflowId = detailVue.workflowId;
            approvalModalVue.sourcePage = array[array.length-1];
            detailVue.sourcePage = approvalModalVue.sourcePage;
            refresh();
        }
		
	}
	
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.loadCompanyName();
	detailVue.initData();

	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#Empj_PaymentGuaranteeApplicationDetailDiv",
	"edModelDivId":"#edModelPaymentGuaranteeApplicationDetail",
	"sdModelDivId":"#sdModelPaymentGuaranteeApplicationDetail",
	"detailInterface":"../Sm_ApprovalProcess_PaymentGuaranteeApply",
	"loadUploadInterface":"../Sm_AttachmentCfgList",
	"subInterface" : "../Empj_PaymentGuaranteeApplyApprovalProcess",
	"companyNameInterface" : "../Emmp_CompanyInfoList", //显示开发企业名称接口
	"changeCompanyInterface" : "../Empj_ProjectInfoList", //改变企业名称接口
	"queryInterface":"../Tgpf_RefundInfoAddQuery",
	"addInterface":"../Tgpf_RefundInfoAdd",
	"bankInterface":"../Tgxy_BankAccountEscrowedList",
	"refundBankAccountInterface":"../Tgxy_BankAccountEscrowedDetail",
	"loadUploadInterface":"../Sm_AttachmentCfgList",
	"approvalModalListInterface":"../Sm_ApprovalProcess_ModalList",
	 "passInterface":"../Sm_ApprovalProcess_Pass",  //通过
	 "rejectInterface":"../Sm_ApprovalProcess_Reject",//驳回
});
