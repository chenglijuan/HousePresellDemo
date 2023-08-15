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
            busiType: "06120401",
            subBox : true,
            showSubFlag : true,
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			indexMethod : indexMethod,
			editHandle : editHandle,
			loadCompanyName : loadCompanyNameFun, //页面加载显示开发企业方法
			changeCompanyHandle : changeCompanyHandleFun,//改变项目名称
			loadGuaranteeCompany: loadGuaranteeCompany,
			subForm : subForm,//点击提交按钮
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
			tableId : this.tableId,
			busiCode : "06120401", //业务编码
			buttonType:3,//详情按钮
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
						generalErrorModal(jsonObj,jsonObj.info);
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
					generalErrorModal(jsonObj,jsonObj.info);
				}
				else
				{
					detailVue.guaranteeCompanyList = jsonObj.emmp_CompanyInfoList;
				}
			});
		}
	//详情操作--------------
	function refresh()
	{
		new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				detailVue.empj_PaymentGuaranteeApplicationDetaiModel = jsonObj.empj_PaymentGuarantee;
				detailVue.empj_PaymentGuaranteeApplicationDetaiModel.alreadyActualAmount = addThousands(detailVue.empj_PaymentGuaranteeApplicationDetaiModel.alreadyActualAmount);
				detailVue.empj_PaymentGuaranteeApplicationDetaiModel.actualAmount = addThousands(detailVue.empj_PaymentGuaranteeApplicationDetaiModel.actualAmount);
				detailVue.empj_PaymentGuaranteeApplicationDetaiModel.guaranteedAmount = addThousands(detailVue.empj_PaymentGuaranteeApplicationDetaiModel.guaranteedAmount);
				detailVue.empj_PaymentGuaranteeApplicationDetaiModel.companyName = jsonObj.empj_PaymentGuarantee.companyName;
				detailVue.empj_PaymentGuaranteeApplicationDetailList = jsonObj.empj_PaymentGuaranteeChildList;
				detailVue.empj_PaymentGuaranteeApplicationDetailList.forEach(function(item){
					item.buildAmountPaid = addThousands(item.buildAmountPaid);
					item.buildAmountPay = addThousands(item.buildAmountPay);
					item.totalAmountGuaranteed = addThousands(item.totalAmountGuaranteed);
					item.amountGuaranteed = addThousands(item.amountGuaranteed);
//					item.paymentLines = item.paymentLines + '%';
					item.paymentLines = item.paymentLines;
					item.paymentProportion = addThousands(item.paymentProportion);
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

				});
				if(detailVue.empj_PaymentGuaranteeApplicationDetaiModel.busiState == "0"){
					detailVue.subBox = true;
				}else{
					detailVue.subBox = false;
				}
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
				detailVue.changeCompanyHandle();
				detailVue.loadGuaranteeCompany();
			}
			
		});
	}
	function indexMethod(index){
		return generalIndexMethod(index,detailVue);
	}
	//详情操作--------------------------点击编辑
	function editHandle(){
        /*var theTableId = 'jump_' + detailVue.tableId;
        $("#tabContainer").data("tabs").addTab({id: theTableId , text: '修改支付保证申请', closeable: true, url: 'empj/Empj_PaymentGuaranteeApplicationEdit.shtml'});*/
        enterNext2Tab(detailVue.tableId, '修改支付保证申请', 'empj/Empj_PaymentGuaranteeApplicationEdit.shtml',detailVue.tableId+"06120401");
	}
	function initData()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var array = tableIdStr.split("_");
		detailVue.tableId = array[2];
		refresh();
		
	}
	function subForm(){
		detailVue.showSubFlag = false;
		if(confirmFile(this.loadUploadList) == true){
			var model = {
					interfaceVersion:this.interfaceVersion,
					tableId : detailVue.tableId,
					busiCode : "06120401", //业务编码
			}
			new ServerInterface(baseInfo.subInterface).execute(model, function(jsonObj)
				{
				detailVue.showSubFlag = true;
				if(jsonObj.result != "success")
				{
					generalErrorModal(jsonObj,jsonObj.info);
				}
				else
				{
					generalSuccessModal();
					refresh();
					detailVue.subBox = false;
				}
					});
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
	"detailInterface":"../Empj_PaymentGuaranteeApplyDetail",
	"loadUploadInterface":"../Sm_AttachmentCfgList",
	"subInterface" : "../Empj_PaymentGuaranteeApplyApprovalProcess",
	"companyNameInterface" : "../Emmp_CompanyInfoList", //显示开发企业名称接口
	"changeCompanyInterface" : "../Empj_ProjectInfoList", //改变企业名称接口
	"queryInterface":"../Tgpf_RefundInfoAddQuery",
	"addInterface":"../Tgpf_RefundInfoAdd",
	"bankInterface":"../Tgxy_BankAccountEscrowedList",
	"refundBankAccountInterface":"../Tgxy_BankAccountEscrowedDetail",
	"loadUploadInterface":"../Sm_AttachmentCfgList",
});
