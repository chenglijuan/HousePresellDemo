(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tgxy_CoopAgreementVerMngModel: {},
			tableId : 1,
			errtips:'',
			loadUploadList : [],
			showDelete : false,
			busiType : "06110202",
			subDissabled : false,
			showButton : "true",
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			CoopAgreementVerMngEdit:CoopAgreementVerMngEdit,
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
			busiCode:this.busiType
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
				detailVue.tgxy_CoopAgreementVerMngModel = jsonObj.tgxy_CoopAgreementVerMngs;
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
				if(detailVue.tgxy_CoopAgreementVerMngModel.busiState == "1"){
					detailVue.showButton = true;
				}else{
					detailVue.showButton = false;
				}
			}
		});
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
					generalErrorModal(jsonObj,jsonObj.info);
				} else {
					$(baseInfo.successModel).modal({
						backdrop : 'static'
					});
					detailVue.showButton = false;
				}
			});
		}
	}
	//编辑跳转方法
	function CoopAgreementVerMngEdit(){
		enterNext2Tab(detailVue.tableId, '修改合作协议版本管理', 'tgxy/Tgxy_CoopAgreementVerMngEdit.shtml', detailVue.tableId + "06110202");
		/*$("#tabContainer").data("tabs").addTab({
			id: detailVue.tableId , 
			text: '修改合作协议版本管理', 
			closeable: true, 
			url: 'tgxy/Tgxy_CoopAgreementVerMngEdit.shtml'
		});*/
	}
	//------------------------方法定义-结束------------------// 
	
	//------------------------数据初始化-开始----------------//
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tgxy_CoopAgreementVerMngDetailDiv",
	"errorModel":"#errorEscrowAdd",
	"successModel":"#successCoopAgreementVerMngDetail",
	"detailInterface":"../Tgxy_CoopAgreementVerMngDetails",
	"submitmitInterface" : "../Tgxy_CoopAgreementVerMngApprovalProcess",
});
