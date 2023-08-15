(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			tgxy_CoopAgreementVerMngModel: {
				theType:"4",
			},
			showButton:true,
			errtips:'',
			loadUploadList : [],
			showDelete : true,
			busiType : "06110202",
			
			theState1:"0",
			
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			//添加
			getAddForm : getAddForm,
			add: add,
			getUploadForm : getUploadForm,
			loadUpload : loadUpload,
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
				elem : '#dateStart',
				done: function(value, date, endDate){
					detailVue.tgxy_CoopAgreementVerMngModel.enableTimeStamp = value;
				}
			});
			/**
			 * 初始化日期插件记账日期开始
			 */
			laydate.render({
				elem : '#dateEnd',
				done: function(value, date, endDate){
					detailVue.tgxy_CoopAgreementVerMngModel.downTimeStamp = value;
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
		}
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
	//详情操作--------------
	function refresh()
	{

	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getAddForm()
	{
		var fileUploadList = detailVue.$refs.listenUploadData.uploadData;
		fileUploadList = JSON.stringify(fileUploadList);
		return {
			interfaceVersion:this.interfaceVersion,
			theState1:this.tgxy_CoopAgreementVerMngModel.theState1,
			busiState:this.busiState,
			eCode:this.eCode,
			userStartId:this.userStartId,
			createTimeStamp:this.createTimeStamp,
			lastUpdateTimeStamp:this.lastUpdateTimeStamp,
			userRecordId:this.userRecordId,
			recordTimeStamp:this.recordTimeStamp,
			theName:this.tgxy_CoopAgreementVerMngModel.theName,
			theVersion:this.tgxy_CoopAgreementVerMngModel.theVersion,
			theType:this.tgxy_CoopAgreementVerMngModel.theType,
			enableTimeStamp:this.tgxy_CoopAgreementVerMngModel.enableTimeStamp,
			downTimeStamp:this.tgxy_CoopAgreementVerMngModel.downTimeStamp,
			templateFilePath:this.tgxy_CoopAgreementVerMngModel.templateFilePath,
			smAttachmentList:fileUploadList
		}
	}

	function add()
	{
		new ServerInterface(baseInfo.addInterface).execute(detailVue.getAddForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				enterNext2Tab(jsonObj.tableId, '合作协议版本管理详情', 'tgxy/Tgxy_CoopAgreementVerMngDetail.shtml',jsonObj.tableId+"06110202");
			}
		});
	}
	
	function initData()
	{
		
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.loadUpload();
	detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#tgxy_CoopAgreementVerMngAddDiv",
	"errorModel":"#errorEscrowAdd",
	"successModel":"#successEscrowAdd",
	"detailInterface":"../Tgxy_CoopAgreementVerMngDetails",
	"addInterface":"../Tgxy_CoopAgreementVerMngAdds",
	"loadUploadInterface" : "../Sm_AttachmentCfgList",
});
