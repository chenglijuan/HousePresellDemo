(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			eCode : "",//结算确认单号
			startSettlementDate : "",//结算开始日期
			settlementState : "",//结算状态
			companyName : "",//代理公司,
			endSettlementDate :"",//结算截止日期
			userUpdate : "",//操作人
			applySettlementDate : "",//申请结算日期
			protocolNumbers : "",//三方协议申请结算份数
			lastUpdateTimeStamp : "",//操作日期
			Tgxy_CoopAgreementSettleDtlAddList : [],
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			loadUploadList: [],//附件
			showDelete : true,
			saveDisabled : true,
			subDisabled : true,
			selectItem : [],
			errMsg : "",
			tableId : "",
			busiType: "06110304",
			settlementDate : "",
			beforeNumbers : "",//三方协议过滤前份数
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			indexMethod: indexMethod,
			//添加
			getAddForm : getAddForm,
			add: add,
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
			showDetailTripeAggrement : showDetailTripeAggrement,
		},
		computed:{
			 
		},
		components : {
			"my-uploadcomponent":fileUpload,
			'vue-nav' : PageNavigationVue
		},
		watch:{
		},
		mounted:function(){
			
		}
	});
	var timestamp=new Date().getTime();
	detailVue.settlementDate = timeStamp2DayDate(timestamp)+" - "+timeStamp2DayDate(timestamp);
	//结算开始日期
	laydate.render({
	    elem: '#coopAgrementSettleAddDate',
	    range: true,
		done: function(value, date, endDate){
			detailVue.settlementDate = value;
			var arr = value.split(" - ");
			detailVue.startSettlementDate = arr[0].trim();
			detailVue.endSettlementDate = arr[1].trim();
			showDetailTripeAggrement();
		}
	});
	
	
	function showDetailTripeAggrement(){
		var model = {
				interfaceVersion : detailVue.interfaceVersion,
				startSettlementDate : detailVue.startSettlementDate,
				endSettlementDate : detailVue.endSettlementDate,
				pageNumber : detailVue.pageNumber,
				countPerPage : detailVue.countPerPage,
				totalPage : detailVue.totalPage,
			}
		
		detailVue.saveDisabled = true;
			new ServerInterface(baseInfo.loadProtocolNumbersInterface).execute(model, function(jsonObj)
			{
				if(jsonObj.result != "success")
				{
					generalErrorModal(jsonObj,jsonObj.info);
				}
				else
				{
					detailVue.protocolNumbers = jsonObj.protocolNumbers;
					detailVue.beforeNumbers = jsonObj.beforeNumbers;
//					detailVue.Tgxy_CoopAgreementSettleDtlAddList = jsonObj.tgxy_CoopAgreementSettleDtlList;//协议子表
					detailVue.pageNumber=jsonObj.pageNumber;
					detailVue.countPerPage=jsonObj.countPerPage;
					detailVue.totalPage=jsonObj.totalPage;
					detailVue.totalCount = jsonObj.totalCount;
				}
				
				detailVue.saveDisabled = false;
			});
	}
	
	
	//------------------------方法定义-开始------------------//
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			busiCode:this.busiType,
		}
	}
	
	//新增操作--------------新增根据当前用户显示数据
	function refresh()
	{
		new ServerInterface(baseInfo.loadInfoInterface).execute(detailVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				detailVue.eCode = jsonObj.tgxy_CoopAgreementSettle.eCode;
				detailVue.startSettlementDate = jsonObj.tgxy_CoopAgreementSettle.startSettlementDate;
				detailVue.endSettlementDate = jsonObj.tgxy_CoopAgreementSettle.endSettlementDate;
				detailVue.settlementState = jsonObj.tgxy_CoopAgreementSettle.settlementState;
				detailVue.protocolNumbers = jsonObj.tgxy_CoopAgreementSettle.protocolNumbers;
				detailVue.beforeNumbers = jsonObj.tgxy_CoopAgreementSettle.beforeNumbers;
				detailVue.companyName = jsonObj.tgxy_CoopAgreementSettle.companyName;
				detailVue.userUpdate = jsonObj.tgxy_CoopAgreementSettle.userUpdate;
				detailVue.applySettlementDate = jsonObj.tgxy_CoopAgreementSettle.applySettlementDate;
				detailVue.lastUpdateTimeStamp = jsonObj.tgxy_CoopAgreementSettle.lastUpdateTimeStamp;
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
//				detailVue.Tgxy_CoopAgreementSettleDtlAddList = jsonObj.tgxy_CoopAgreementSettleDtlList;
			}
		});
	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getAddForm()
	{
		
		detailVue.Tgxy_CoopAgreementSettleDtlAddList.forEach(function(item){
			detailVue.selectItem.push(item.tableId);
		})
		var fileUploadList = detailVue.$refs.coopAgreementSettleDtlAddUploadData.uploadData;
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
			beforeNumbers : this.beforeNumbers,
			//userUpdate : this.userUpdate,
			//lastUpdateTimeStamp : this.lastUpdateTimeStamp,
			settlementState : this.settlementState,
			//idArr : this.selectItem,
			smAttachmentList : fileUploadList,
		}
	}
	
    function indexMethod(index) {
		return generalIndexMethod(index, detailVue)
	}

    //列表操作-----------------------获取附件参数
	function getUploadForm(){
		return{
			pageNumber : '0',
			busiType : this.busiType,
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
	
	function add()
	{
		$(baseInfo.waitingModalCoopAgreementSettleDtlAddDivId).modal({
			backdrop :'static'
		})
		new ServerInterface(baseInfo.addInterface).execute(detailVue.getAddForm(), function(jsonObj)
		{
			$(baseInfo.waitingModalCoopAgreementSettleDtlAddDivId).modal('hide');
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				//refresh();
				/*detailVue.saveDisabled = true;
				detailVue.subDisabled = false;
				detailVue.tableId = jsonObj.tableId*/
				setTimeout(function(){
					enterNext2Tab(jsonObj.tableId, '三方协议结算详情', 'tgxy/Tgxy_CoopAgreementSettleDtlDetail.shtml',jsonObj.tableId+"06110304");
				},500);
			}
		});
	}
	//新增操作------------------点击提交按钮
	function submitForm(){
		var model = {
			interfaceVersion:detailVue.interfaceVersion,
			tableId : detailVue.tableId,
		}
		new ServerInterface(baseInfo.subInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				//refresh();
				detailVue.saveDisabled = true;
				detailVue.subDisabled = true;
			}
		});
	}
	
	//点击打印
	function print(){
		
	}
	function initData()
	{
		
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	//detailVue.loadUpload();
	detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#tgxy_CoopAgreementSettleDtlAddDiv",
	"edModelDivId":"#edModelCoopAgreementSettleDtlAdd",
	"sdModelDivId":"#sdModelCoopAgreementSettleDtlAdd",
	"waitingModalCoopAgreementSettleDtlAddDivId" :"#waitingModalCoopAgreementSettleDtlAdd1",
	"loadInfoInterface" : "../Tgxy_CoopAgreementSettleDetail",//新增根据当前用户显示数据接口
	"loadProtocolNumbersInterface" : "../Tgxy_CoopAgreementSettleDetailList",
	"loadUploadInterface":"../Sm_AttachmentCfgList",
	"detailInterface":"../Tgxy_CoopAgreementSettleDetail",
	"addInterface":"../Tgxy_CoopAgreementSettleAdd",
	"subInterface" : "",//提交接口
});
