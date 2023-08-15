(function(baseInfo){
	var editVue = new Vue({
		el : baseInfo.editDivId,
		data : {
			interfaceVersion :19000101,
			tg_DepositManagementModel: {},
			tableId : 1,
			//其他
            emmp_BankInfoList : [],
            bankInfoId: "",
            emmp_BankBranchList : [],
            bankBranchId : "",
            tgxy_BankAccountEscrowedList: [],
            escrowAcountId : "",

			depositProperty : "",
			depositPropertyList : [
				{tableId:"01",theName:"大额存单"},
				{tableId:"02",theName:"结构性存款"},
				{tableId:"03",theName:"保本理财"},
			],
			storagePeriodCompanyList : [
				{tableId:"01",theName:"年"},
				{tableId:"02",theName:"月"},
				{tableId:"03",theName:"天"},
			],

            //附件材料
            busiType: '210104',
            loadUploadList: [],
            showDelete : true,   //附件是否可编辑
            showSubFlag : true,
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			//更新
			update: update,
			bankInfoChange : bankInfoChange,
            bankBranChange: bankBranChange,
            getEscrowAcountId : function (data) {
                this.escrowAcountId = data.tableId;
            },
            changeDepositProperty: function (data) {
                this.depositProperty = data.tableId;
            },
            changeStoragePeriodCompany: function (data) {
                this.storagePeriodCompany = data.tableId;
            }
		},
		computed:{
			 
		},
		components : {
            "my-uploadcomponent":fileUpload,
            'vue-select': VueSelect
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
			tableId:editVue.tableId
		}
	}

	//详情操作--------------
	function refresh()
	{
		if (editVue.tableId == null || editVue.tableId < 1) 
		{
			return;
		}

		getDetail();
        loadUpload();
	}

	function getDetail()
	{
		new ServerInterface(baseInfo.detailInterface).execute(editVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				editVue.tg_DepositManagementModel = jsonObj.tg_DepositManagement;
				$('#startDateSelect_edit').val(editVue.tg_DepositManagementModel.startDateStr);
				$('#stopDateSelect_edit').val(editVue.tg_DepositManagementModel.stopDateStr);
                $('#takeOutDateSelect_edit').val(editVue.tg_DepositManagementModel.extractDateStr);
                editVue.bankInfoId = editVue.tg_DepositManagementModel.bankId;
                editVue.bankBranchId = editVue.tg_DepositManagementModel.bankOfDepositId;
                editVue.escrowAcountId = editVue.tg_DepositManagementModel.escrowAcountId;
                getBankInfoList();
                getBankBranchList();
                getBankAccountEscrowedList();
			}
		});
	}
	
	//详情更新操作--------------

	function update(buttonType)
	{
		if(buttonType == 2)
        {
            editVue.showSubFlag = false;
        }
		model = {
            interfaceVersion:this.interfaceVersion,
            tableId: editVue.tg_DepositManagementModel.tableId,
            busiState:editVue.tg_DepositManagementModel.busiState,
            depositState : editVue.tg_DepositManagementModel.depositState,
            eCode : editVue.tg_DepositManagementModel.eCode,
            depositProperty : editVue.tg_DepositManagementModel.depositProperty,
            bankId : editVue.bankId,
            bankOfDepositId : editVue.bankBranchId,
            escrowAcountId : editVue.escrowAcountId,
            openAccountCertificate : editVue.tg_DepositManagementModel.openAccountCertificate,
            storagePeriodCompany : editVue.tg_DepositManagementModel.storagePeriodCompany,
            storagePeriod : editVue.tg_DepositManagementModel.storagePeriod,
            startDateStr : $('#startDateSelect_edit').val(),
            stopDateStr : $('#stopDateSelect_edit').val(),
            principalAmount : editVue.tg_DepositManagementModel.principalAmount,
            annualRate : editVue.tg_DepositManagementModel.annualRate,
            expectedInterest : editVue.tg_DepositManagementModel.expectedInterest,
            floatAnnualRate : editVue.tg_DepositManagementModel.floatAnnualRate,
            agent : editVue.tg_DepositManagementModel.agent,
            extractDateStr : $('#takeOutDateSelect_edit').val(),
            realInterest : editVue.tg_DepositManagementModel.realInterest,
            realInterestRate : editVue.tg_DepositManagementModel.realInterestRate,

            //附件材料
            busiType:'210104',
            generalAttachmentList:this.$refs.listenUploadData.uploadData,

            buttonType : buttonType,

            approvalState: (buttonType == "1") ? "待提交" : "审核中"
        };

		new ServerInterfaceJsonBody(baseInfo.updateInterface).execute(mdoel, function(jsonObj)
		{
			editVue.showSubFlag = true;
			if(jsonObj.result != "success")
			{
                generalErrorModal(jsonObj);
			}
			else
			{
                generalSuccessModal();
                enterNewTabCloseCurrent(editVue.tableId, '存单提取详情', 'tg/Tg_DepositManagementTakeOutDetail.shtml');
			}
		});
	}

    function getBankInfoList()
    {
        var model = {
            interfaceVersion: editVue.interfaceVersion,
            theState : 0
        };

        new  ServerInterface(baseInfo.bankInfoListInterface).execute(model, function(jsonObj)
        {
            if (jsonObj.result != "success")
            {
                noty({"text":jsonObj.info,"type":"error","timeout":2000});
            }
            else
            {
                editVue.emmp_BankInfoList = jsonObj.emmp_BankInfoList;
            }
        });
    }

    function bankInfoChange(data)
    {
        if(editVue.bankInfoId != data.tableId)
        {
            editVue.emmp_BankBranchList = [];
            editVue.bankBranchId = "";
        }

        editVue.bankInfoId = data.tableId;
        getBankBranchList();
    }

    function getBankBranchList()
    {
        var model = {
            interfaceVersion : editVue.interfaceVersion,
            bankId : editVue.bankInfoId,
            theState : 0
        };

        new ServerInterface(baseInfo.bankBranchListInterface).execute(model, function(jsonObj)
        {
            if (jsonObj.result != "success")
            {

            }
            else
            {
                editVue.emmp_BankBranchList = jsonObj.emmp_BankBranchList;
            }
        });
    }

    function bankBranChange(data)
    {
        if(editVue.bankBranchId != data.tableId)
        {
            editVue.tgxy_BankAccountEscrowedList = [];
            editVue.escrowAcountId = "";
        }

        this.bankBranchId = data.tableId;
        getBankAccountEscrowedList();
    }

    function getBankAccountEscrowedList()
    {
        var model = {
            interfaceVersion : editVue.interfaceVersion,
            bankBranchId : editVue.bankBranchId,
            theState : 0
        };

        new ServerInterface(baseInfo.bankAccountEscrowedInterface).execute(model, function(jsonObj)
        {
            if (jsonObj.result != "success")
            {

            }
            else
            {
                editVue.tgxy_BankAccountEscrowedList = jsonObj.tgxy_BankAccountEscrowedList;
            }
        });
    }
    /********* 附件材料 相关 *********/
    function loadUpload(){
        var model = {
            pageNumber : '0',
            busiType : '210104',
            sourceId : editVue.tableId,
            interfaceVersion:editVue.interfaceVersion
        };

        new ServerInterface(baseInfo.loadUploadInterface).execute(model, function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                editVue.loadUploadList = jsonObj.sm_AttachmentCfgList;
            }
        });
    }
	
	function initData()
	{
        laydate.render({
            elem: '#takeOutDateSelect_edit'
        });

        getIdFormTab('', function (tableId) {
            editVue.tableId = tableId;
            //初始化
            refresh();
        });
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	editVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"editDivId":"#tg_DepositManagementTakeOutEditDiv",
	"detailInterface":"../Tg_DepositManagementDetail",
	"updateInterface":"../Tg_DepositManagementUpdate",
	"bankInfoListInterface":"../Emmp_BankInfoList",
	"bankBranchListInterface":"../Emmp_BankBranchList",
    //材料附件
    "loadUploadInterface":"../Sm_AttachmentCfgList"
});
