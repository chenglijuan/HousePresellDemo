(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			tgxy_BankAccountEscrowedModel: {theType:0,bankBranchId:"",shortNameOfBank:""},
			dayDate:getDayDate(),
			bankBranch:[],
            errMsg:"",

            //附件材料
			busiType:'200101',
            loadUploadList: [],
            showDelete : true,  //附件是否可编辑
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			brenchForm:brenchForm,
			//添加
			getAddForm : getAddForm,
			getBankBranch:getBankBranch,
			add: add,
            changeBankListener:changeBankListener,
		},
		computed:{

		},
        components: {
            'vue-select': VueSelect,
            "my-uploadcomponent": fileUpload,
        },
        watch: {

		}
	});

	//------------------------方法定义-开始------------------//
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:detailVue.interfaceVersion,
		}
	}

	//详情操作--------------
	function refresh()
	{

	}

	//详情更新操作--------------获取"更新机构详情"参数
	function getAddForm()
	{
		return {
			interfaceVersion:detailVue.interfaceVersion,
			theState:detailVue.tgxy_BankAccountEscrowedModel.theState,
			busiState:detailVue.tgxy_BankAccountEscrowedModel.busiState,
			eCode:detailVue.tgxy_BankAccountEscrowedModel.eCode,
			userStartId:detailVue.tgxy_BankAccountEscrowedModel.userStartId,
			userRecordId:detailVue.tgxy_BankAccountEscrowedModel.userRecordId,
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
	            //附件材料
            busiType : detailVue.busiType,
            generalAttachmentList : this.$refs.listenUploadData.uploadData
		}
	}

    function add() {
        serverBodyRequest(baseInfo.addInterface, detailVue.getAddForm(), function (jsonObj) {
            generalSuccessModal()
            enterNewTabCloseCurrent(jsonObj.tableId, '托管账户详情', 'tgxy/Tgxy_BankAccountEscrowedDetail.shtml')
        })
    }

    function brenchForm()
	{
		return {
			interfaceVersion:detailVue.interfaceVersion,
			isUsing:0,
		}
	}
    
	function getBankBranch() {
	    serverRequest(baseInfo.bankBranchInterface,detailVue.brenchForm(),function (jsonObj) {
            detailVue.bankBranch = jsonObj.emmp_BankBranchList;
        })
    }

    function changeBankListener(data) {
        detailVue.tgxy_BankAccountEscrowedModel.bankBranchId = data.tableId
        detailVue.tgxy_BankAccountEscrowedModel.shortNameOfBank = data.shortName
    }

	function initData()
	{
		generalLoadFile2(detailVue,detailVue.busiType)

		getBankBranch()

	}
	//------------------------方法定义-结束------------------//

	//------------------------数据初始化-开始----------------//
	detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#tgxy_BankAccountEscrowedAddDiv",
	"detailInterface":"../Tgxy_BankAccountEscrowedDetail",
	"addInterface":"../Tgxy_BankAccountEscrowedAdd",
	"bankBranchInterface":"../Emmp_BankBranchList",

    //其他
    "successModal":"#successM",
    "errorModal":"#errorM",
});
