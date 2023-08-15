(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			eCodeOfTripleAgreement:"",
			refundBankName:"",
			receiverBankName:"",
			actualRefundAmount:"",
			receiverName:"",
			refundBankAccount:"",
			receiverBankAccount:"",
			receiverType:"1",
			expiredAmount:"",
			fundOfTripleAgreement:"",
			certificateNumberOfBuyer:"",
			theNameOfProject:"",
			refundAmount:"",
			retainRightAmount:"",
			theNameOfCreditor:"",
			theNameOfBuyer:"",
			eCodeOfContractRecord:"",
			unexpiredAmount:"",
			fundFromLoan:"",
			contactPhoneOfBuyer:"",
			positionOfBuilding:"",
			eCodeOfTripleAgreement:"",
			theBankAccountEscrowedId:"",//退款账号Id
			theNameOfBank :"",
			tgxy_BankAccountEscrowedList : [],
			tripleAgreementId : "",
			projectId :"",
			buildingId:"",
			buyerId:"",
			fileType:"",
			dialogImageUrl: '',
            dialogVisible: false,
            fileList : [],
            uploadDataModal : {},
            uploadData : [],
            fileId : "",
            errMsg : "",
            loadUploadList: [],
            showDelete : true,
            busiType: "06120202",
            houseId : "",
            developCompanyName : "",
            bAccountSupervised : "",
            bBankName : "",
            bankAccountList: [],
            receiverTypeList : [
            	{tableId:"1",theName:"买受人"},
            	{tableId:"2",theName:"指定收款人"},
            ],
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			getBankForm : getBankForm,
			leave : leave,//鼠标离开事件
			choiceRefundBankAccountForm:choiceRefundBankAccountForm,
			choiceRefundBankAccount : choiceRefundBankAccount,
			loadForm:loadForm,
			getUploadForm : getUploadForm,
			loadUpload : loadUpload,
			//添加
			getAddForm : getAddForm,
			add: add,
			getBank:function(data){
				this.bAccountSupervised = data.tableId;
				for( var i = 0; i < detailVue.bankAccountList.length; i++){
					if(this.bAccountSupervised == detailVue.bankAccountList[i].theAccount){
						detailVue.bBankName = detailVue.bankAccountList[i].theNameOfBank;
					}
				}
			},
			getReceiverType : function(data){
				this.receiverType = data.tableId;
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

	//------------------------方法定义-开始------------------//
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		/*return {
			interfaceVersion:this.interfaceVersion,
		}*/
		return{
			interfaceVersion:this.interfaceVersion,
			eCodeOfTripleAgreement:this.eCodeOfTripleAgreement,
			houseId : this.houseId,
		}
	}

	//详情操作--------------
	function refresh()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var array = tableIdStr.split("_");
		if(array[3] == undefined){
			detailVue.houseId = "";
		}else{
			detailVue.houseId = array[3];
			leave();
		}
	}
	//新增操作-----------------------获取附件参数
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
			userStartId:this.userStartId,
			createTimeStamp:this.createTimeStamp,
			lastUpdateTimeStamp:this.lastUpdateTimeStamp,
			userRecordId:this.userRecordId,
			recordTimeStamp:this.recordTimeStamp,
			tripleAgreementId:this.tripleAgreementId,
			eCodeOfTripleAgreement:this.eCodeOfTripleAgreement,
			eCodeOfContractRecord:this.eCodeOfContractRecord,
			projectId:this.projectId,
			theNameOfProject:this.theNameOfProject,
			buildingId:this.buildingId,
			positionOfBuilding:this.positionOfBuilding,
			buyerId:this.buyerId,
			theNameOfBuyer:this.theNameOfBuyer,
			certificateNumberOfBuyer:this.certificateNumberOfBuyer,
			contactPhoneOfBuyer:this.contactPhoneOfBuyer,
			theNameOfCreditor:this.theNameOfCreditor,
			fundOfTripleAgreement:commafyback(this.fundOfTripleAgreement),
			fundFromLoan:commafyback(this.fundFromLoan),
			retainRightAmount:commafyback(this.retainRightAmount),
			expiredAmount:commafyback(this.expiredAmount),
			unexpiredAmount:commafyback(this.unexpiredAmount),
			refundAmount:commafyback(this.refundAmount),
			receiverType:this.receiverType,
			receiverName:this.receiverName,
			receiverBankName:this.receiverBankName,
			refundType:this.refundType,
			receiverBankAccount:this.receiverBankAccount,
			actualRefundAmount:this.actualRefundAmount,
			refundBankName:this.refundBankName,
			refundBankAccount:this.refundBankAccount,
			refundTimeStamp:this.refundTimeStamp,
			theBankAccountEscrowedId:this.theBankAccountEscrowedId,
			smAttachmentList:fileUploadList,
			developCompanyName : this.developCompanyName,
            bAccountSupervised : this.bAccountSupervised,
            bBankName : this.bBankName,
		}
	}
	
	//获得信息
	function getBankForm(){
		return{
			interfaceVersion:this.interfaceVersion
		}
	}
	
	//鼠标离开根据三方协议带出信息
	function leave(){
		if(detailVue.eCodeOfTripleAgreement != "" || detailVue.houseId != ""){
			new ServerInterface(baseInfo.queryInterface).execute(detailVue.getSearchForm(), function(jsonObj)
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
					detailVue.positionOfBuilding = jsonObj.position;
					detailVue.eCodeOfTripleAgreement = jsonObj.eCodeOfTripleAgreement;
					detailVue.contactPhoneOfBuyer = jsonObj.contactPhoneOfBuyer;
					detailVue.fundFromLoan = addThousands(jsonObj.loanAmount);
					detailVue.unexpiredAmount = addThousands(jsonObj.theAmountOfInterestUnRetained);
					detailVue.eCodeOfContractRecord = jsonObj.eCodeOfContractRecord;
					detailVue.theNameOfBuyer = jsonObj.theNameOfBuyer;
					detailVue.theNameOfCreditor = jsonObj.theNameOfCreditor;
					detailVue.retainRightAmount = addThousands(jsonObj.theAmountOfRetainedEquity);
					
					detailVue.refundAmount = addThousands(jsonObj.refundAmount);
					
					detailVue.theNameOfProject = jsonObj.theNameOfProject;
					detailVue.certificateNumberOfBuyer = jsonObj.certificateNumberOfBuyer;
					detailVue.fundOfTripleAgreement = addThousands(jsonObj.contractAmount);
					detailVue.expiredAmount = addThousands(jsonObj.theAmountOfInterestRetained);
					detailVue.actualRefundAmount = jsonObj.actualRefundAmount;
					detailVue.tripleAgreementId = jsonObj.tripleAgreementId;
					detailVue.projectId = jsonObj.projectId;
					detailVue.buildingId = jsonObj.buildingId;
					detailVue.buyerId = jsonObj.theBuyerId;
					detailVue.receiverName = jsonObj.receiverName;
					detailVue.developCompanyName = jsonObj.developCompanyName;
					detailVue.bankAccountList = jsonObj.bankAccountSupervisedList; 
					detailVue.bankAccountList.forEach(function(item){
						item.tableId = item.theAccount;
						item.theName = item.theBankAccount;
					})
				}
					});
		}
	}
	function add()
	{
		new ServerInterface(baseInfo.addInterface).execute(detailVue.getAddForm(), function(jsonObj)
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
				enterNext2Tab(jsonObj.tableId, '退房退款贷款未结清详情', 'tgpf/Tgpf_RefundInfoUnclearedDetail.shtml',jsonObj.tableId+"06120202");
				refresh();
			}
		});
	}
	
	function loadForm(){
		new ServerInterface(baseInfo.bankInterface).execute(detailVue.getBankForm(), function(jsonObj)
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
				detailVue.theNameOfBank = jsonObj.tgxy_BankAccountEscrowed.theNameOfBank;
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
	detailVue.loadForm();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#tgpf_RefundInfoUnclearedDiv",
	"edModelDivId":"#edModelRefundInfoUnclearedAdd",
	"detailInterface":"../Tgpf_RefundInfoDetail",
	"queryInterface":"../Tgpf_RefundInfoAddQuery",
	"addInterface":"../Tgpf_RefundInfoUnclearedAdd",
	"bankInterface":"../Tgxy_BankAccountEscrowedList",
	"refundBankAccountInterface":"../Tgxy_BankAccountEscrowedDetail",
	"loadUploadInterface":"../Sm_AttachmentCfgList",
});
