(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tgxy_TripleAgreementVerMngModel: {},
			tableId : "",
			errtips:'',
			loadUploadList : [],
			showDelete : true,
			busiType :"06010103",
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			//更新
			getUpdateForm : getUpdateForm,
			update: update,
		},
		computed:{
			 
		},
		components : {
			"my-uploadcomponent":fileUpload
		},
		watch:{
			
		},
		mounted:function(){
			/**
			 * 初始化日期插件记账日期开始
			 */
			laydate.render({
				elem : '#tripeAgrementEditStart',
				done: function(value, date, endDate){
					
				}
			});
			/**
			 * 初始化日期插件记账日期开始
			 */
			laydate.render({
				elem : '#tripeAgrementEditEnd',
				done: function(value, date, endDate){
					
				}
			});
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
				/*detailVue.errtips = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
					backdrop :'static'
				 });*/
				generalErrorModal(jsonObj);
			}
			else
			{
				detailVue.tgxy_TripleAgreementVerMngModel = jsonObj.Tgxy_TripleAgreementVerMngDetails;
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList
			}
		});
	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getUpdateForm()
	{
		var fileUploadList = detailVue.$refs.listenUploadData.uploadData;
		fileUploadList = JSON.stringify(fileUploadList);
		return {
			//tableId:
			interfaceVersion:detailVue.interfaceVersion,
			theState1:detailVue.tgxy_TripleAgreementVerMngModel.theState1,
			busiState:detailVue.tgxy_TripleAgreementVerMngModel.busiState,
			eCode:detailVue.tgxy_TripleAgreementVerMngModel.eCode,
			theName:detailVue.tgxy_TripleAgreementVerMngModel.theName,
			theVersion:detailVue.tgxy_TripleAgreementVerMngModel.theVersion,
			theType:detailVue.tgxy_TripleAgreementVerMngModel.theType,
			tableId : detailVue.tableId,
			p1:detailVue.tgxy_TripleAgreementVerMngModel.enableTimeStamp,
			p2:detailVue.tgxy_TripleAgreementVerMngModel.downTimeStamp,
			eCodeOfCooperationAgreement : detailVue.tgxy_TripleAgreementVerMngModel.eCodeOfCooperationAgreement,
			theNameOfCooperationAgreement : detailVue.tgxy_TripleAgreementVerMngModel.theNameOfCooperationAgreement,
			templateContentStyle:detailVue.tgxy_TripleAgreementVerMngModel.templateContentStyle,
			smAttachmentList:fileUploadList
		}
	}

	function update()
	{
		new ServerInterface(baseInfo.updateInterface).execute(detailVue.getUpdateForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				/*detailVue.errtips = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
					backdrop :'static'
				 });*/
				generalErrorModal(jsonObj);
			}
			else
			{
				enterNext2Tab(detailVue.tableId, '三方协议版本管理详情', 'tgxy/Tgxy_TripleAgreementVerMngDetail.shtml',detailVue.tableId+"06010103");
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
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tgxy_TripleAgreementVerMngEditDiv",
	"errorModel":"#errorEscrowAdd",
	"successModel":"#successEscrowAdd",
	"detailInterface":"../Tgxy_TripleAgreementVerMngDetails",
	"updateInterface":"../Tgxy_TripleAgreementVerMngUpdates",
});
