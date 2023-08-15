(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tgxy_BankAccountEscrowedModel: {bankId:"",bankBranchId:"",shortNameOfBank:""},
			tableId : 1,
			dayDate:"",
			userStartName:"",
            bankBranch:[],

            //附件材料
			busiType:'200101',
            dialogVisible: false,
            dialogImageUrl: "",
            fileType:"",
            fileList : [],
            showButton:true,
            hideShow:false,
            uploadData : [],
            smAttachmentList:[],//页面显示已上传的文件
            uploadList:[],
            loadUploadList: [],
            showDelete : true,

            //其他
            errMsg:"", //错误提示信息
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			//更新
			getUpdateForm : getUpdateForm,
			update: update,

            saveAttachment:saveAttachment,
            changeBankListener:changeBankListener,
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
		return {
			interfaceVersion:detailVue.interfaceVersion,
			tableId:detailVue.tableId,
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
		loadUpload()
	}

	function getDetail()
	{
		serverRequest(baseInfo.detailInterface,detailVue.getSearchForm(),function (jsonObj) {
            detailVue.tgxy_BankAccountEscrowedModel = jsonObj.tgxy_BankAccountEscrowed
			detailVue.dayDate=detailVue.tgxy_BankAccountEscrowedModel.createTimeStampString
			detailVue.userStartName=detailVue.tgxy_BankAccountEscrowedModel.userStartName
        })
	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getUpdateForm()
	{
		return {
            //附件材料
            busiType : detailVue.busiType,
            sourceId : detailVue.tableId,
            generalAttachmentList : this.$refs.listenUploadData.uploadData,

			interfaceVersion:detailVue.interfaceVersion,
			tableId:detailVue.tableId,
			theState:detailVue.tgxy_BankAccountEscrowedModel.theState,
			busiState:detailVue.tgxy_BankAccountEscrowedModel.busiState,
			eCode:detailVue.tgxy_BankAccountEscrowedModel.eCode,
			userStartId:detailVue.tgxy_BankAccountEscrowedModel.userStartId,
			createTimeStamp:detailVue.tgxy_BankAccountEscrowedModel.createTimeStamp,
			lastUpdateTimeStamp:detailVue.tgxy_BankAccountEscrowedModel.lastUpdateTimeStamp,
			userRecordId:detailVue.tgxy_BankAccountEscrowedModel.userRecordId,
			recordTimeStamp:detailVue.tgxy_BankAccountEscrowedModel.recordTimeStamp,
			companyId:detailVue.tgxy_BankAccountEscrowedModel.companyId,
			projectId:detailVue.tgxy_BankAccountEscrowedModel.projectId,
			bankId:detailVue.tgxy_BankAccountEscrowedModel.bankId,
			theNameOfBank:detailVue.tgxy_BankAccountEscrowedModel.theNameOfBank,
			shortNameOfBank:detailVue.tgxy_BankAccountEscrowedModel.shortNameOfBank,
			bankBranchId:detailVue.tgxy_BankAccountEscrowedModel.bankBranchId,
			theName:detailVue.tgxy_BankAccountEscrowedModel.theName,
			theAccount:detailVue.tgxy_BankAccountEscrowedModel.theAccount,
			remark:detailVue.tgxy_BankAccountEscrowedModel.remark,
			contactPerson:detailVue.tgxy_BankAccountEscrowedModel.contactPerson,
			contactPhone:detailVue.tgxy_BankAccountEscrowedModel.contactPhone,
			updatedStamp:detailVue.tgxy_BankAccountEscrowedModel.updatedStamp,
			income:detailVue.tgxy_BankAccountEscrowedModel.income,
			payout:detailVue.tgxy_BankAccountEscrowedModel.payout,
			certOfDeposit:detailVue.tgxy_BankAccountEscrowedModel.certOfDeposit,
			structuredDeposit:detailVue.tgxy_BankAccountEscrowedModel.structuredDeposit,
			breakEvenFinancial:detailVue.tgxy_BankAccountEscrowedModel.breakEvenFinancial,
			currentBalance:detailVue.tgxy_BankAccountEscrowedModel.currentBalance,
			largeRatio:detailVue.tgxy_BankAccountEscrowedModel.largeRatio,
			largeAndCurrentRatio:detailVue.tgxy_BankAccountEscrowedModel.largeAndCurrentRatio,
			financialRatio:detailVue.tgxy_BankAccountEscrowedModel.financialRatio,
			totalFundsRatio:detailVue.tgxy_BankAccountEscrowedModel.totalFundsRatio,
			isUsing:detailVue.tgxy_BankAccountEscrowedModel.isUsing,
		}
	}

	function update()
	{
		new ServerInterfaceJsonBody(baseInfo.updateInterface).execute(detailVue.getUpdateForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				// noty({"text":jsonObj.info,"type":"error","timeout":2000});
				generalErrorModal(jsonObj)
			}
			else
			{
				// $(baseInfo.detailDivId).modal('hide');
				generalSuccessModal()
                enterNewTabCloseCurrent(jsonObj.tableId, '托管账户详情', 'tgxy/Tgxy_BankAccountEscrowedDetail.shtml')
				// refresh();
			}
		});
	}

    function getBankBranch() {
        serverRequest(baseInfo.bankBranchInterface,getTotalListForm(detailVue.interfaceVersion),function (jsonObj) {
            detailVue.bankBranch = jsonObj.emmp_BankBranchList;
        })
    }

    function loadUpload(){
        generalLoadFile2(detailVue,detailVue.busiType)

    }

    function saveAttachment() {
        generalUploadFile(detailVue,"Tgxy_BankAccountEscrowed",baseInfo.attachmentBatchAddInterface,baseInfo.successModel)
    }

    function changeBankListener(data) {
        detailVue.tgxy_BankAccountEscrowedModel.bankBranchId = data.tableId
        detailVue.tgxy_BankAccountEscrowedModel.shortNameOfBank = data.shortName
    }
	
	function initData()
	{
        getBankBranch()
		getIdFormTab("",function (id) {
			console.log('initData is '+id)
			detailVue.tableId=id
			refresh()
        })
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	// detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tgxy_BankAccountEscrowedEditDiv",
	"detailInterface":"../Tgxy_BankAccountEscrowedDetail",
	"updateInterface":"../Tgxy_BankAccountEscrowedUpdate",
    "bankBranchInterface":"../Emmp_BankBranchList",

    //材料附件
    "loadUploadInterface": "../Sm_AttachmentCfgList",
    "attachmentBatchAddInterface": "../Sm_AttachmentBatchAdd",
    "attachmentListInterface": "../Sm_AttachmentList",
    //模态框
    "successModel": "#successM",
    "errorModal": "#errorM",
});
