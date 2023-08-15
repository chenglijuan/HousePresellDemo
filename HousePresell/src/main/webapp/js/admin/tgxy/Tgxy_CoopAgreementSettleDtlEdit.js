(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tableId : 1,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			loadUploadList: [],//附件
			showDelete : true,
			saveDisabled : false,
			subDisabled : false,
			eCode : "",//结算确认单号
			startSettlementDate : "",//结算开始日期
			settlementState : "",//结算状态
			companyName : "",//代理公司,
			endSettlementDate :"",//结算截止日期
			userUpdate : "",//操作人
			applySettlementDate : "",//申请结算日期
			protocolNumbers : "",//三方协议申请结算份数
			lastUpdateTimeStamp : "",//操作日期
			Tgxy_CoopAgreementSettleDtlEditList : [],
			errMsg : "",
			selectItem : [],
			busiType : "06110304",
			settlementDate : "",
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			indexMethod : indexMethod,
			//更新
			getUpdateForm : getUpdateForm,
			update: update,
			showModal : showModal,
			changePageNumber : function(data){
				if (detailVue.pageNumber != data) {
					detailVue.pageNumber = data;
					detailVue.showDetailTripeAggrement();
				}
			},
			changeCountPerPage:function(data){
				if (detailVue.countPerPage != data) {
					detailVue.countPerPage = data;
					detailVue.showDetailTripeAggrement();
				}
			},
			getUploadForm : getUploadForm,//获取上传附件参数
			loadUpload : loadUpload,//显示附件
			submitForm : submitForm,//提交
			print : print,//打印
			showDetailTripeAggrement : showDetailTripeAggrement,//选择日期带出结算字表信息
		},
		computed:{
			 
		},
		components : {
			"my-uploadcomponent":fileUpload,
			'vue-nav' : PageNavigationVue
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
			busiCode : this.busiType,
			pageNumber : detailVue.pageNumber,
			countPerPage : detailVue.countPerPage,
			totalPage : detailVue.totalPage,
		}
	}

	//详情操作--------------
	function refresh()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var list = tableIdStr.split("_");
		tableIdStr = list[2];
		detailVue.tableId = tableIdStr;
		if (detailVue.tableId == null || detailVue.tableId < 1) {
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
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				detailVue.eCode = jsonObj.tgxy_CoopAgreementSettle.eCode;
				detailVue.startSettlementDate = jsonObj.tgxy_CoopAgreementSettle.startSettlementDate;
				detailVue.endSettlementDate = jsonObj.tgxy_CoopAgreementSettle.endSettlementDate;
				detailVue.settlementDate = jsonObj.tgxy_CoopAgreementSettle.startSettlementDate + " ~ "+jsonObj.tgxy_CoopAgreementSettle.endSettlementDate;
				detailVue.settlementState = jsonObj.tgxy_CoopAgreementSettle.settlementState;
				detailVue.protocolNumbers = jsonObj.tgxy_CoopAgreementSettle.protocolNumbers;
				detailVue.companyName = jsonObj.tgxy_CoopAgreementSettle.companyName;
				detailVue.userUpdate = jsonObj.tgxy_CoopAgreementSettle.userUpdate;
				detailVue.applySettlementDate = jsonObj.tgxy_CoopAgreementSettle.applySettlementDate;
				detailVue.lastUpdateTimeStamp = jsonObj.tgxy_CoopAgreementSettle.lastUpdateTimeStamp;
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
//				detailVue.Tgxy_CoopAgreementSettleDtlEditList = jsonObj.tgxy_CoopAgreementSettleDtlList;
				detailVue.pageNumber=jsonObj.pageNumber;
				detailVue.countPerPage=jsonObj.countPerPage;
				detailVue.totalPage=jsonObj.totalPage;
				detailVue.totalCount = jsonObj.totalCount;
			}
		});
	}
	
	function showDetailTripeAggrement(){
		var model = {
				interfaceVersion : detailVue.interfaceVersion,
				startSettlementDate : detailVue.startSettlementDate,
				endSettlementDate : detailVue.endSettlementDate,
				pageNumber : detailVue.pageNumber,
				countPerPage : detailVue.countPerPage,
				totalPage : detailVue.totalPage,
			}
			new ServerInterface(baseInfo.loadProtocolNumbersInterface).execute(model, function(jsonObj)
			{
				if(jsonObj.result != "success")
				{
					generalErrorModal(jsonObj,jsonObj.info);
				}else{
					detailVue.protocolNumbers = jsonObj.protocolNumbers;
//					detailVue.Tgxy_CoopAgreementSettleDtlEditList = jsonObj.tgxy_CoopAgreementSettleDtlList;//协议子表
					detailVue.pageNumber=jsonObj.pageNumber;
					detailVue.countPerPage=jsonObj.countPerPage;
					detailVue.totalPage=jsonObj.totalPage;
					detailVue.totalCount = jsonObj.totalCount;
				}
			});
	}
	 function indexMethod(index) {
		return generalIndexMethod(index, detailVue)
	}
	//详情更新操作--------------获取"更新机构详情"参数
	function getUpdateForm()
	{
		detailVue.Tgxy_CoopAgreementSettleDtlEditList.forEach(function(item){
			detailVue.selectItem.push(item.tableId);
		})
		var fileUploadList = detailVue.$refs.coopAgreementSettleDtlEditUploadData.uploadData;
		fileUploadList = JSON.stringify(fileUploadList);
		return {
			interfaceVersion:this.interfaceVersion,
			userStartId:this.userStartId,
			userRecordId:this.userRecordId,
			eCode : this.eCode,
			companyName : this.companyName,
			applySettlementDate : this.applySettlementDate,
			startSettlementDate : this.startSettlementDate,
			endSettlementDate : this.endSettlementDate,
			protocolNumbers : this.protocolNumbers,
			tableId : this.tableId,
			//userUpdate : this.userUpdate,
			//lastUpdateTimeStamp : this.lastUpdateTimeStamp,
			settlementState : this.settlementState,
			//idArr : this.selectItem,
			smAttachmentList : fileUploadList,
		}
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
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				//refresh();
				detailVue.loadUploadList = jsonObj.sm_AttachmentCfgList;
			}
		});
	}
	
	function update()
	{
		new ServerInterface(baseInfo.updateInterface).execute(detailVue.getUpdateForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				enterNext2Tab(detailVue.tableId, '三方协议结算详情', 'tgxy/Tgxy_CoopAgreementSettleDtlDetail.shtml',detailVue.tableId+"06110304");
			}
		});
	}
	
	
	function submitForm(){
		var model = {
			interfaceVersion:detailVue.interfaceVersion,
			tableId : detailVue.tableId,
		}
		new ServerInterface(baseInfo.subInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				detailVue.settlementState = "1";
				//refresh();
				detailVue.saveDisabled = true;
				detailVue.subDisabled = true;
			}
		});
	}
	function showModal()
	{
		approvalModalVue.getModalWorkflowList();
	}
	function initData()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var array = tableIdStr.split("_");
//		if (array.length > 1)
//		{
//			detailVue.tableId = array[array.length-3];
//			detailVue.afId = array[array.length-2];
//			detailVue.workflowId = array[array.length-1];
//			approvalModalVue.afId = detailVue.afId;
//			approvalModalVue.workflowId = detailVue.workflowId;
//			refresh();
//		}
		
		if (array.length > 1)
		{
			
			detailVue.tableId = array[array.length-4];
			detailVue.afId = array[array.length-3];
			if(array[array.length-2]!=null)
			{
				detailVue.workflowId = array[array.length-2];
			}
			
			approvalModalVue.sourcePage = array[array.length-1]
			approvalModalVue.workflowId = detailVue.workflowId;
			approvalModalVue.afId = detailVue.afId;
			approvalModalVue.busiCode = detailVue.af_busiCode;
			refresh();
		}
	}

	//------------------------方法定义-结束------------------//
	
	//结算开始日期
	laydate.render({
	    elem: '#coopAgrementSettleDtlEditDate',
	    range: true,
	    value:detailVue.settlementDate,
		done: function(value, date, endDate){
			detailVue.settlementDate = value;
			var arr = value.split(" - ");
			detailVue.startSettlementDate = arr[0].trim();
			detailVue.endSettlementDate = arr[1].trim();
			showDetailTripeAggrement();
		}
	});
	//------------------------数据初始化-开始----------------//
	//detailVue.loadUpload();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tgxy_CoopAgreementSettleDtlEditDiv",
	"loadUploadInterface":"../Sm_AttachmentCfgList",
	"detailInterface":"../Tgxy_CoopAgreementSettleDetail",
	"updateInterface":"../Tgxy_CoopAgreementSettleUpdate",
	"subInterface" : "../Tgxy_CoopAgreementSettleSubmit",//提交接口
	"loadProtocolNumbersInterface" : "../Tgxy_CoopAgreementSettleDetailList",
});
