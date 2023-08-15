(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			tgxy_TripleAgreementVerMngModel: {},
			errtips:'',
			loadUploadList : [],
			showDelete : true,
			busiType :"06010103",
			theName :"",
			theState:"",
			operationUser:"",
			recordTimeStamp:"",
			theVersion:"",
			enableTimeStamp:"",
			createTimeStamp:"",
			theType:"4",
			downTimeStamp:"",
			editUser:"",
			theState1:"0",
			eCodeOfCooperationAgreement :"",
			theNameOfCooperationAgreement : "",
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
			theTypeChange:theTypeChange,
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
				elem : '#tripeAgrementAddStart',
				done: function(value, date, endDate){
					detailVue.enableTimeStamp = value;
				}
			});
			/**
			 * 初始化日期插件记账日期开始
			 */
			laydate.render({
				elem : '#tripeAgrementAddEnd',
				done: function(value, date, endDate){
					detailVue.downTimeStamp = value;
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
			theType:this.theType,
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
		new ServerInterface(baseInfo.initInterface).execute(detailVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				detailVue.eCodeOfCooperationAgreement = jsonObj.tgxy_CoopAgreementVerMng.theVersion;
				detailVue.theNameOfCooperationAgreement = jsonObj.tgxy_CoopAgreementVerMng.theName;
			}
		});
	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getAddForm()
	{
		var fileUploadList = detailVue.$refs.listenUploadData.uploadData;
		fileUploadList = JSON.stringify(fileUploadList);
		return {
			interfaceVersion:this.interfaceVersion,
			theState:this.theState,
			busiState:this.busiState,
			eCode:this.eCode,
			theState1:this.theState1,
			
			/*userStartId:this.userStartId,
			createTimeStamp:this.createTimeStamp,
			lastUpdateTimeStamp:this.lastUpdateTimeStamp,
			userRecordId:this.userRecordId,
			recordTimeStamp:this.recordTimeStamp,*/
			theName:this.theName,
			theVersion:this.theVersion,
			theType:this.theType,
			eCodeOfCooperationAgreement:this.eCodeOfCooperationAgreement,
			theNameOfCooperationAgreement:this.theNameOfCooperationAgreement,
			p1:this.enableTimeStamp,
			p2:this.downTimeStamp,
			eCodeOfCooperationAgreement : this.eCodeOfCooperationAgreement,
			theNameOfCooperationAgreement : this.theNameOfCooperationAgreement,
			templateContentStyle:this.templateContentStyle,
			smAttachmentList:fileUploadList
		}
	}

	function add()
	{
		new ServerInterface(baseInfo.addInterface).execute(detailVue.getAddForm(), function(jsonObj)
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
				enterNext2Tab(jsonObj.tableId, '三方协议版本管理详情', 'tgxy/Tgxy_TripleAgreementVerMngDetail.shtml',jsonObj.tableId+"06010103");
			}
		});
	}
	
	//合作协议类型改变时
	function theTypeChange()
	{
		new ServerInterface(baseInfo.initInterface).execute(detailVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
//				noty({"text":jsonObj.info,"type":"error","timeout":2000});
				detailVue.errtips = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
					backdrop :'static'
				 });
				detailVue.eCodeOfCooperationAgreement ="";
				detailVue.theNameOfCooperationAgreement ="";
			}
			else
			{
				detailVue.eCodeOfCooperationAgreement = jsonObj.tgxy_CoopAgreementVerMng.theVersion;
				detailVue.theNameOfCooperationAgreement = jsonObj.tgxy_CoopAgreementVerMng.theName;
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
	"addDivId":"#tgxy_TripleAgreementVerMngAddDiv",
	"errorModel":"#errorTripleAgreementAdd",
	"successModel":"#successEscrowAdd",
	"detailInterface":"../Tgxy_TripleAgreementVerMngDetails",
	"addInterface":"../Tgxy_TripleAgreementVerMngAdds",
	"loadUploadInterface":"../Sm_AttachmentCfgList",
	"initInterface" : "../Tgxy_TripleAgreementVerMngToAdds",
});
