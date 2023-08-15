(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tgxy_BankAccountEscrowedList:[],
			tgpf_RefundInfoModel: {
				fundFromLoan:"",
				unexpiredAmount:"",
				retainRightAmount : "",
				refundAmount : "",
				fundOfTripleAgreement : "",
				expiredAmount : "",
				actualRefundAmount : "",
				developCompanyName : "",
	            bAccountSupervised : "",
	            bBankName : "",
			},
			refundBankName:"",
			theBankAccountEscrowedId:"",
			tableId : 1,
			hideShow:false,
			fileType:"",
			dialogImageUrl: '',
            dialogVisible: false,
            fileList : [],
            uploadDataModal : {},
            uploadData : [],
            smAttachmentList:[],//页面显示已上传的文件
            showButton:true,
            errMsg : "",
            loadUploadList : [],
            showDelete : true,
            busiType: "06120201",
            bankAccountList: [],
            receiverTypeList : [
            	{tableId:"1",theName:"买受人"},
            	{tableId:"2",theName:"指定收款人"},
            ],
		},
		methods : {
			//详情
			choiceRefundBankAccountForm:choiceRefundBankAccountForm,
			choiceRefundBankAccount : choiceRefundBankAccount,
			loadForm:loadForm,
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			getBankForm : getBankForm,
			//更新
			getUpdateForm : getUpdateForm,
			update: update,
			handleRemoveList:handleRemoveList,
			getBank:getBank,
			getReceiverType : function(data){
				this.tgpf_RefundInfoModel.receiverType = data.tableId;
			}
		},
		computed:{
			 
		},
		components : {
			"my-uploadcomponent":fileUpload,
			'vue-select': VueSelect,
		},
		watch:{
			
		}
	});
	//编辑页----------------删除
	function handleRemoveList(sourceId){
		this.smAttachmentList.splice(sourceId,1);
	}
	
	function getBank() {
		for( var i = 0; i < detailVue.bankAccountList.length; i++){
			if(this.tgpf_RefundInfoModel.bAccountSupervised == detailVue.bankAccountList[i].theAccount){
				detailVue.tgpf_RefundInfoModel.bBankName = detailVue.bankAccountList[i].theNameOfBank;
			}
		}
	}
	//------------------------方法定义-开始------------------//
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:this.tableId,
			busiCode : this.busiType,
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
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				detailVue.errMsg = jsonObj.info;
			}
			else
			{
				detailVue.tgpf_RefundInfoModel = jsonObj.tgpf_RefundInfo;
				detailVue.tgpf_RefundInfoModel.fundFromLoan = addThousands(detailVue.tgpf_RefundInfoModel.fundFromLoan);
				detailVue.tgpf_RefundInfoModel.unexpiredAmount = addThousands(detailVue.tgpf_RefundInfoModel.unexpiredAmount);
				detailVue.tgpf_RefundInfoModel.retainRightAmount = addThousands(detailVue.tgpf_RefundInfoModel.retainRightAmount);
				detailVue.tgpf_RefundInfoModel.fundOfTripleAgreement = addThousands(detailVue.tgpf_RefundInfoModel.fundOfTripleAgreement);
				detailVue.tgpf_RefundInfoModel.expiredAmount = addThousands(detailVue.tgpf_RefundInfoModel.expiredAmount);
				detailVue.tgpf_RefundInfoModel.actualRefundAmount = addThousands(detailVue.tgpf_RefundInfoModel.actualRefundAmount);
				detailVue.theBankAccountEscrowedId = jsonObj.tgpf_RefundInfo.theBankAccountEscrowedId;
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
				if(detailVue.loadUploadList !=undefined && detailVue.loadUploadList.length>0){
					detailVue.hideShow = true;
				}
				detailVue.bankAccountList = jsonObj.bankAccountSupervisedList;
				
				if(detailVue.tgpf_RefundInfoModel.receiverName == undefined || detailVue.tgpf_RefundInfoModel.receiverName == ""){
					detailVue.tgpf_RefundInfoModel.receiverName =  jsonObj.tgpf_RefundInfo.theNameOfCreditor;
				}
				
				if(detailVue.tgpf_RefundInfoModel.developCompanyName == undefined || detailVue.tgpf_RefundInfoModel.developCompanyName == ""){
					detailVue.tgpf_RefundInfoModel.developCompanyName =  jsonObj.tgpf_RefundInfo.companyName;
				}
				
			}
		});
	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getUpdateForm()
	{
		var fileUploadList = detailVue.$refs.listenUploadData.uploadData;
		fileUploadList = JSON.stringify(fileUploadList);
		return {
			tableId:this.tableId,
			interfaceVersion:this.interfaceVersion,
			theState:this.tgpf_RefundInfoModel.theState,
			busiState:this.tgpf_RefundInfoModel.busiState,
			eCode:this.tgpf_RefundInfoModel.eCode,
			userStartId:this.tgpf_RefundInfoModel.userStartId,
			createTimeStamp:this.tgpf_RefundInfoModel.createTimeStamp,
			lastUpdateTimeStamp:this.tgpf_RefundInfoModel.lastUpdateTimeStamp,
			userRecordId:this.tgpf_RefundInfoModel.userRecordId,
			recordTimeStamp:this.tgpf_RefundInfoModel.recordTimeStamp,
			tripleAgreementId:this.tgpf_RefundInfoModel.tripleAgreementId,
			eCodeOfTripleAgreement:this.tgpf_RefundInfoModel.eCodeOfTripleAgreement,
			eCodeOfContractRecord:this.tgpf_RefundInfoModel.eCodeOfContractRecord,
			projectId:this.tgpf_RefundInfoModel.projectId,
			theNameOfProject:this.tgpf_RefundInfoModel.theNameOfProject,
			buildingId:this.tgpf_RefundInfoModel.buildingId,
			positionOfBuilding:this.tgpf_RefundInfoModel.positionOfBuilding,
			buyerId:this.tgpf_RefundInfoModel.buyerId,
			theNameOfBuyer:this.tgpf_RefundInfoModel.theNameOfBuyer,
			certificateNumberOfBuyer:this.tgpf_RefundInfoModel.certificateNumberOfBuyer,
			contactPhoneOfBuyer:this.tgpf_RefundInfoModel.contactPhoneOfBuyer,
			theNameOfCreditor:this.tgpf_RefundInfoModel.theNameOfCreditor,
			fundOfTripleAgreement: commafyback(this.tgpf_RefundInfoModel.fundOfTripleAgreement) ,
			fundFromLoan:commafyback(this.tgpf_RefundInfoModel.fundFromLoan),
			retainRightAmount:commafyback(this.tgpf_RefundInfoModel.retainRightAmount),
			expiredAmount:commafyback(this.tgpf_RefundInfoModel.expiredAmount),
			unexpiredAmount:commafyback(this.tgpf_RefundInfoModel.unexpiredAmount),
			refundAmount:commafyback(this.tgpf_RefundInfoModel.refundAmount),
			receiverType:this.tgpf_RefundInfoModel.receiverType,
			receiverName:this.tgpf_RefundInfoModel.receiverName,
			receiverBankName:this.tgpf_RefundInfoModel.receiverBankName,
			refundType:this.tgpf_RefundInfoModel.refundType,
			receiverBankAccount:this.tgpf_RefundInfoModel.receiverBankAccount,
			actualRefundAmount:commafyback(this.tgpf_RefundInfoModel.actualRefundAmount),
			refundBankName:this.refundBankName,
			refundBankAccount:this.tgpf_RefundInfoModel.refundBankAccount,
			refundTimeStamp:this.tgpf_RefundInfoModel.refundTimeStamp,
			theBankAccountEscrowedId:this.theBankAccountEscrowedId,
			smAttachmentList:fileUploadList,
			developCompanyName : this.tgpf_RefundInfoModel.developCompanyName,
            bAccountSupervised : this.tgpf_RefundInfoModel.bAccountSupervised,
            bBankName : this.tgpf_RefundInfoModel.bBankName,
		}
	}

	function update()
	{
		new ServerInterface(baseInfo.updateInterface).execute(detailVue.getUpdateForm(), function(jsonObj)
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
				detailVue.showButton = false;
				enterNext2Tab(detailVue.tableId, '退房退款详情', 'tgpf/Tgpf_RefundInfoDetail.shtml',detailVue.tableId+"06120201");
				refresh();
			}
		});
	}
	
	//获得信息
	function getBankForm(){
		return{
			interfaceVersion:this.interfaceVersion
		}
	}
	function loadForm(){
		new ServerInterface(baseInfo.bankInterface).execute(detailVue.getBankForm(), function(jsonObj)
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
				detailVue.tgxy_BankAccountEscrowedList = jsonObj.tgxy_BankAccountEscrowedList;
			}
		});
	}
	function choiceRefundBankAccountForm(){
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:this.theBankAccountEscrowedId,
		}
	}
	//修改银行账号带出银行名称
	function choiceRefundBankAccount(){
		new ServerInterface(baseInfo.refundBankAccountInterface).execute(detailVue.choiceRefundBankAccountForm(), function(jsonObj)
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
				detailVue.refundBankName = jsonObj.tgxy_BankAccountEscrowed.theNameOfBank;
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
	detailVue.loadForm();
	detailVue.initData();
	
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tgpf_RefundInfoEditDiv",
	"edModelDivId":"#edModelRefundEdit",
	"detailInterface":"../Tgpf_RefundInfoDetail",
	"updateInterface":"../Tgpf_RefundInfoUpdate",
	"bankInterface":"../Tgxy_BankAccountEscrowedList",
	"refundBankAccountInterface":"../Tgxy_BankAccountEscrowedDetail",
});
