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
            subBox : true,
            showSubFlag : true,
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			indexMethod : indexMethod,
			//添加
			getUploadForm : getUploadForm,
			loadUpload : loadUpload,
			getDetail : getDetail,
			sub : sub,//提交审批流
			editHandle : editHandle,
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
		return {
			interfaceVersion : this.interfaceVersion,
			tableId : detailVue.tableId,
			busiCode : "06120403"
		}
	}

	//详情操作--------------
	function refresh()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var array = tableIdStr.split("_");
		detailVue.tableId = array[2];
		if (detailVue.tableId == null || detailVue.tableId < 1) {
			return;
		}
		getDetail();
	}
	
	function getDetail() {
		new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
				generalErrorModal(jsonObj,jsonObj.info);
			} else {
				//refresh();
				detailVue.empj_PaymentGuarantee = jsonObj.empj_PaymentGuarantee;
				detailVue.empj_PaymentGuarantee.guaranteedAmount = addThousands(detailVue.empj_PaymentGuarantee.guaranteedAmount);
				detailVue.empj_PaymentGuarantee.alreadyActualAmount = addThousands(detailVue.empj_PaymentGuarantee.alreadyActualAmount);
				detailVue.empj_PaymentGuarantee.actualAmount = addThousands(detailVue.empj_PaymentGuarantee.actualAmount);
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
				if (detailVue.loadUploadList != undefined && detailVue.loadUploadList.length > 0) {
					detailVue.hideShow = true;
				}
				if(detailVue.empj_PaymentGuarantee.busiState == "3" || detailVue.empj_PaymentGuarantee.busiState == "4"){
					detailVue.subBox = false;
				}else{
					detailVue.subBox = true;
				}
			}
		});
	}
	
	function indexMethod(index){
		return generalIndexMethod(index,detailVue);
	}

	//列表操作-----------------------获取附件参数
	function getUploadForm(){
		return{
			pageNumber : '0',
			busiType : detailVue.busiType,
			interfaceVersion:this.interfaceVersion
		}
	}
	
	//列表操作-----------------------页面加载显示附件类型
	function loadUpload(){
		new ServerInterface(baseInfo.loadUploadInterface).execute(detailVue.getUploadForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				//refresh();
				detailVue.loadUploadList = jsonObj.sm_AttachmentCfgList;
			}
		});
	}
	
	
	
	//点击提交按钮
	function sub(){
		detailVue.showSubFlag = false;
		if(confirmFile(this.loadUploadList) == true){
			var model= {
					interfaceVersion : this.interfaceVersion,
					tableId : this.tableId,
					busiCode : "06120403"
			}
			new ServerInterface(baseInfo.subInterface).execute(model, function(jsonObj) {
				detailVue.showSubFlag = true;
				if (jsonObj.result != "success") {
					generalErrorModal(jsonObj,jsonObj.info);
				} else {
					generalSuccessModal();
					refresh();
					detailVue.subBox = true;
				}
			});
		}
	}
	
	function editHandle(){
		enterNext2Tab(detailVue.tableId, '支付保证撤销修改', 'empj/Empj_CancelPaymentGuaranteeApplicationEdit.shtml', detailVue.tableId + "06120403");
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
	"detailInterface":"../Empj_PaymentGuaranteeDetail",
	"queryInterface":"../Tgpf_RefundInfoAddQuery",
	"addInterface":"../Tgpf_RefundInfoAdd",
	"bankInterface":"../Tgxy_BankAccountEscrowedList",
	"refundBankAccountInterface":"../Tgxy_BankAccountEscrowedDetail",
	"loadUploadInterface":"../Sm_AttachmentCfgList",
	"subInterface" : "../Empj_CancelPayApprovalProcess"
});
