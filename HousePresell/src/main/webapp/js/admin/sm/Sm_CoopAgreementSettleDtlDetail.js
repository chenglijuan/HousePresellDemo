(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tgxy_CoopAgreementSettleDtlModel: {},
			tableId : 1,
			Tgxy_CoopAgreementSettleDtlDetailList : [],
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			loadUploadList: [],//附件
			showDelete : false,
			saveDisabled : false,
			subDisabled : true,
			errMsg : "",
			busiType : "",
			afId : "",
			workflowId : "",
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			indexMethod: indexMethod,
			changePageNumber : function(data){
				if (detailVue.pageNumber != data) {
					detailVue.pageNumber = data;
					detailVue.refresh();
				}
			},
			changeCountPerPage:function(data){
				if (detailVue.countPerPage != data) {
					detailVue.countPerPage = data;
					detailVue.refresh();
				}
			},
			getUploadForm : getUploadForm,//获取上传附件参数
			loadUpload : loadUpload,//显示附件
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
	 function indexMethod(index) {
		return generalIndexMethod(index, detailVue)
	}
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			busiCode : "06110304", //业务编码
			afId: this.afId,
		    workflowId: this.workflowId,
		    tableId : this.tableId
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
	//列表操作-----------------------获取附件参数
	function getUploadForm(){
		return{
			pageNumber : '0',
			busiType : 'Tgpf_RefundInfo',
			interfaceVersion:this.interfaceVersion
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
	function getDetail()
	{
		new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
//				detailVue.Tgxy_CoopAgreementSettleDtlDetailList = jsonObj.tgxy_CoopAgreementSettleDtlList;
				detailVue.tgxy_CoopAgreementSettleDtlModel = jsonObj.tgxy_CoopAgreementSettle;
			}
		});
	}
	function showModal(){
		 approvalModalVue.getModalWorkflowList();
	}
	function initData()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		console.log(tableIdStr)
		var array = tableIdStr.split("_");
		if (array.length > 1) {
			detailVue.tableId = array[array.length - 4];
			detailVue.afId = array[array.length - 3];
			detailVue.workflowId = array[array.length - 2];
			approvalModalVue.afId = detailVue.afId;
			approvalModalVue.workflowId = detailVue.workflowId;
			approvalModalVue.sourcePage = array[array.length - 1];
			refresh();
		}
//		if (array.length > 1)
//		{
//			updateVue.tableId = array[array.length-4];
//			
//			if(array[array.length-2] !=null)
//			{
//				approvalModalVue.workflowId = array[array.length-2];
//			}
//			approvalModalVue.sourcePage = array[array.length-1]; //来源页面
//			approvalModalVue.afId = array[array.length-3];
//			
//			refresh();
//		}
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tgxy_CoopAgreementSettleDtlDetailDiv",
	"loadUploadInterface":"../Sm_AttachmentCfgList",
	"detailInterface":"../Sm_ApprovalProcess_CoopAgreementSettleDetail"
});
