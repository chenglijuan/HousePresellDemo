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
			busiType : "06010103",
			subDisabled : false,
			showButton : true,
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			editTripleAgreementVerMngHandle : editTripleAgreementVerMngHandle,
			SubmitHandle : SubmitHandle,
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
			interfaceVersion:this.interfaceVersion,
			tableId:this.tableId,
			busiCode : this.busiType
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
				detailVue.tgxy_TripleAgreementVerMngModel = jsonObj.Tgxy_TripleAgreementVerMngDetails;
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
				if(detailVue.tgxy_TripleAgreementVerMngModel.busiState == "1"){
					detailVue.showButton = true;
				}else{
					detailVue.showButton = false;
				}
			}
		});
	}
	
	function editTripleAgreementVerMngHandle(){
		enterNext2Tab(detailVue.tableId, '三方协议版本管理编辑', 'tgxy/Tgxy_TripleAgreementVerMngEdit.shtml', detailVue.tableId + "06010103");
	}
	
	function initData()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var array = tableIdStr.split("_");
		detailVue.tableId = array[2];
		refresh();
	}
	function SubmitHandle(){
		if(confirmFile(this.loadUploadList) == true){
			new ServerInterface(baseInfo.submitmitInterface).execute(detailVue.getSearchForm(), function(jsonObj) {
				if (jsonObj.result != "success") {
					/*	$(baseInfo.errorModel).modal({
					backdrop : 'static'
				});
				detailVue.errMsg = jsonObj.info;*/
					generalErrorModal(jsonObj,jsonObj.info);
				} else {
					/*$(baseInfo.successModel).modal({
					backdrop : 'static'
				});*/
					generalSuccessModal();
					detailVue.showButton = false;
				}
			});
		}
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"errorModel":"#errorEscrowAdd",
	"successModel":"#successEscrowAdd",
	"detailDivId":"#tgxy_TripleAgreementVerMngDetailDiv",
	"detailInterface":"../Tgxy_TripleAgreementVerMngDetails",
	"submitmitInterface":"../Tgxy_TripleAgreementVerMngApprovalProcess"
});
