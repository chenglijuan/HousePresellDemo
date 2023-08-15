(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tgxy_CoopAgreementVerMngModel: {},
			tableId : 1,
			errtips:'',
			loadUploadList : [],
			showDelete : true,
			busiType : "06110202",
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
				elem : '#dateStartCoop',
				done: function(value, date, endDate){
					detailVue.tgxy_CoopAgreementVerMngModel.enableTimeStamp = value;
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
			busiCode:this.busiType,
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
			interfaceVersion:this.interfaceVersion,
			tableId:this.tgxy_CoopAgreementVerMngModel.tableId,
			theState1:this.tgxy_CoopAgreementVerMngModel.theState1,
			busiState:this.busiState,
			eCode:this.eCode,
			/*userStartId:this.userStartId,
			createTimeStamp:this.tgxy_CoopAgreementVerMngModel.createTimeStamp,
			lastUpdateTimeStamp:this.tgxy_CoopAgreementVerMngModel.lastUpdateTimeStamp,
			userRecordId:this.tgxy_CoopAgreementVerMngModel.userRecordId,
			recordTimeStamp:this.tgxy_CoopAgreementVerMngModel.recordTimeStamp,*/
			theName:this.tgxy_CoopAgreementVerMngModel.theName,
			theVersion:this.tgxy_CoopAgreementVerMngModel.theVersion,
			theType:this.tgxy_CoopAgreementVerMngModel.theType,
			enableTimeStamp:this.tgxy_CoopAgreementVerMngModel.enableTimeStamp,
			downTimeStamp:this.tgxy_CoopAgreementVerMngModel.downTimeStamp,
			templateFilePath:this.tgxy_CoopAgreementVerMngModel.templateFilePath,
			smAttachmentList:fileUploadList
		}
	}

	function update()
	{
		new ServerInterface(baseInfo.updateInterface).execute(detailVue.getUpdateForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
//				noty({"text":jsonObj.info,"type":"error","timeout":2000});
				/*detailVue.errtips = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
					backdrop :'static'
				 });*/
				generalErrorModal(jsonObj);
			}
			else
			{
				/*$(baseInfo.detailDivId).modal('hide');
				refresh();*/
				enterNext2Tab(detailVue.tableId, '合作协议版本管理详情', 'tgxy/Tgxy_CoopAgreementVerMngDetail.shtml',detailVue.tableId+"06110202");
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
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tgxy_CoopAgreementVerMngEditDiv",
	"errorModel":"#errorEscrowAdd",
	"successModel":"#successEscrowAdd",
	"detailInterface":"../Tgxy_CoopAgreementVerMngDetails",
	"updateInterface":"../Tgxy_CoopAgreementVerMngUpdates"
});
