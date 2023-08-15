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
			tableId : 1,
			errMsg  : "",
			loadUploadList: [],
            showDelete : false,
            busiType: "Tgpf_RefundInfoUncleared",
            eCode : "12322",
            busiCode : "06120202", //业务编码
            subDisabled :false,
            showEditBtn : true,
			showSubBtn : true,
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			reFundInfoEditHandle : reFundInfoEditHandle,//编辑跳转方法
			getUploadForm : getUploadForm,
			loadUpload : loadUpload,
			reFundInfoSubmitHandle : reFundInfoSubmitHandle,
			getExportForm :getExportForm,//导出参数
			exportPdf:exportPdf,//导出pdf
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
	//列表操作-----------------------获取附件参数
	function getUploadForm(){
		return{
			pageNumber : '0',
			busiType : 'Tgxy_CoopMemo',
			interfaceVersion:this.interfaceVersion
		}
	}
	
	function getExportForm() {
		var href = window.location.href;
		return {
			interfaceVersion : this.interfaceVersion,
			sourceId : this.tableId,
			reqAddress : href,
			sourceBusiCode : this.busiCode,
		}
	}
	
	//列表操作-----------------------页面加载显示附件类型
	function loadUpload(){
		new ServerInterface(baseInfo.loadUploadInterface).execute(detailVue.getUploadForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				detailVue.errMsg = jsonObj.info;
			}
			else
			{
				//refresh();
				detailVue.loadUploadList = jsonObj.sm_AttachmentCfgList;
			}
		});
	}
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:this.tableId,
			busiCode : this.busiCode,
		}
	}
	//详情操作----------------------提交审批流
	function reFundInfoSubmitHandle() {
		detailVue.showSubBtn = false;
		if(confirmFile(this.loadUploadList) == true){
			new ServerInterface(baseInfo.submitmitInterface).execute(detailVue.getSearchForm(), function(jsonObj) {
				detailVue.showSubBtn = true;
				if (jsonObj.result != "success") {
					$(baseInfo.edModelDivId).modal({
						backdrop : 'static'
					});
					detailVue.errMsg = jsonObj.info;
				} else {
					detailVue.showEditBtn  =  false;
					detailVue.showSubBtn = false ;
					$(baseInfo.sdModelDivId).modal({
						backdrop : 'static'
					});
				}
			});
		}
	}
	//详情操作--------------
	function refresh()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var array = tableIdStr.split("_");
		detailVue.tableId = array[2];
		if (this.tableId == null || this.tableId < 1) 
		{
			return;
		}
		getDetail();
	}
	
	//列表操作-----------------------导出PDF
	function exportPdf(){
		new ServerInterface(baseInfo.exportInterface).execute(detailVue.getExportForm(), function(jsonObj){
			if (jsonObj.result != "success") {
				$(baseInfo.edModelDivId).modal({
					backdrop : 'static'
				});
				detailVue.errMsg = jsonObj.info;
			} else {
				window.open(jsonObj.pdfUrl,"_blank");
				/*window.location.href = jsonObj.pdfUrl;*/
			}
		});
	}

	function getDetail()
	{
		new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				detailVue.errMsg = jsonObj.info;
			}
			else
			{
				detailVue.tgpf_RefundInfoModel = jsonObj.tgpf_RefundInfoUnclearedDetail;
				//如果收款人类型为2时，显示开发企业的相关信息
				if(detailVue.tgpf_RefundInfoModel.receiverType == "2"){
					detailVue.tgpf_RefundInfoModel.receiverBankAccount = jsonObj.tgpf_RefundInfoUnclearedDetail.bAccountSupervised;
					detailVue.tgpf_RefundInfoModel.receiverName = jsonObj.tgpf_RefundInfoUnclearedDetail.developCompanyName;
					detailVue.tgpf_RefundInfoModel.receiverBankName = jsonObj.tgpf_RefundInfoUnclearedDetail.bBankName;
				}
				detailVue.tgpf_RefundInfoModel.fundFromLoan = addThousands(detailVue.tgpf_RefundInfoModel.fundFromLoan);
				detailVue.tgpf_RefundInfoModel.unexpiredAmount = addThousands(detailVue.tgpf_RefundInfoModel.unexpiredAmount);
				detailVue.tgpf_RefundInfoModel.retainRightAmount = addThousands(detailVue.tgpf_RefundInfoModel.retainRightAmount);
				detailVue.tgpf_RefundInfoModel.fundOfTripleAgreement = addThousands(detailVue.tgpf_RefundInfoModel.fundOfTripleAgreement);
				detailVue.tgpf_RefundInfoModel.expiredAmount = addThousands(detailVue.tgpf_RefundInfoModel.expiredAmount);
				detailVue.tgpf_RefundInfoModel.actualRefundAmount = addThousands(detailVue.tgpf_RefundInfoModel.actualRefundAmount);
				detailVue.tgpf_RefundInfoModel.refundAmount = addThousands(detailVue.tgpf_RefundInfoModel.refundAmount);
				if(jsonObj.tgpf_RefundInfoUnclearedDetail.busiState != "待提交" && jsonObj.tgpf_RefundInfoUnclearedDetail.busiState != "待审批" && jsonObj.tgpf_RefundInfoUnclearedDetail.busiState  != "" && jsonObj.tgpf_RefundInfoUnclearedDetail.busiState != undefined){
					detailVue.showEditBtn  =  false;
					detailVue.showSubBtn = false ;
				}
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
				if(detailVue.smAttachmentList !=undefined && detailVue.smAttachmentList.length>0){
					detailVue.hideShow = true;
				}
			}
		});
	}
	
	function initData()
	{
		
	}
	//------------------------方法定义-结束------------------//
	//编辑跳转方法
	function reFundInfoEditHandle(){
		enterNext2Tab(detailVue.tableId, '退房退款信息详情', 'tgpf/Tgpf_RefundInfoUnclearedEdit.shtml',detailVue.eCode);
	}
	//------------------------数据初始化-开始----------------//
	/*detailVue.loadUpload();*/
	detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tgpf_RefundInfoUnclearedDetail",
	"edModelDivId":"#edModelRefundInfoUnclearedDetail",
	"sdModelDivId":"#sdModelRefundInfoUnclearedDetail",
	"detailInterface":"../Tgpf_RefundInfoUnclearedDetail",
	"loadUploadInterface":"../Sm_AttachmentCfgList",
	"submitmitInterface" : "../Tgpf_RefundInfoApprovalProcess",//点击提交
	"exportInterface":"../exportPDFByWord",//导出pdf
});
