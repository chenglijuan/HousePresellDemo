(function(baseInfo){
	var editVue = new Vue({
		el : baseInfo.editDivId,
		data : {
			interfaceVersion :19000101,
			tg_DepositManagementModel: {
				
				calculationRule : "365",
				
			},
			tableId : 1,
			//其他
            emmp_BankInfoList : [],
            bankInfoId: "",
            emmp_BankBranchList : [],
            bankBranchId : "",
            tgxy_BankAccountEscrowedList: [],
            escrowAcountId : "",

            calculationRule : "365",
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
			calculationRuleList : [
				{tableId:"360",theName:"360"},
				{tableId:"365",theName:"365"},
			],
            theStartDateStr : "",

            //附件材料
            busiType:'210104',
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
            countEndDate : countEndDate,
			bankInfoChange : bankInfoChange,
            bankBranChange: bankBranChange,
            getEscrowAcountId : function (data) {
                this.escrowAcountId = data.tableId;
            },
            changeDepositProperty: function (data) {
                this.depositProperty = data.tableId;
            },
            changeStoragePeriodCompany: function (data) {
            	editVue.tg_DepositManagementModel.storagePeriodCompany = data.tableId;
                countEndDate();
                // 计算预计利息
                accrualHandle();
            },
            changeCalculationRule1 : function (data) {
            	editVue.calculationRule = data.tableId;
            	editVue.tg_DepositManagementModel.calculationRule = data.tableId;
            	// 计算预计利息
                accrualHandle();
            },
            changeNumber:function (theKey){
            	// 计算预计利息
                accrualHandle();
                var value = editVue.tg_DepositManagementModel[theKey];
                value = value.replace(/[^0-9/.]/g,''); //清除“数字”和“.”以外的字符
                value = value.replace(/^\./g,"");  //验证第一个字符是数字而不是.
                value = value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的.
                value = value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
                editVue.tg_DepositManagementModel[theKey] = value;
            },
            changeThousands:function (theKey) {
                var value = editVue.tg_DepositManagementModel[theKey];
              //  value = addThousands(value);
                editVue.tg_DepositManagementModel[theKey] = value;
            },
            accHandle: function() {
            	// 计算预计利息
                accrualHandle();
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
//			console.log(editVue.tableId);
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

				//千分位
                editVue.tg_DepositManagementModel.principalAmount = addThousands(editVue.tg_DepositManagementModel.principalAmount);
//                editVue.tg_DepositManagementModel.annualRate = addThousands(editVue.tg_DepositManagementModel.annualRate);
                editVue.tg_DepositManagementModel.expectedInterest = addThousands(editVue.tg_DepositManagementModel.expectedInterest);
//                editVue.tg_DepositManagementModel.floatAnnualRate = addThousands(editVue.tg_DepositManagementModel.floatAnnualRate);

				$('#date2101040201').val(editVue.tg_DepositManagementModel.startDateStr);
				$('#date2101040202').val(editVue.tg_DepositManagementModel.stopDateStr);
                editVue.bankInfoId = editVue.tg_DepositManagementModel.bankId;
                editVue.bankBranchId = editVue.tg_DepositManagementModel.bankOfDepositId;
                editVue.escrowAcountId = editVue.tg_DepositManagementModel.escrowAcountId;

                editVue.depositProperty = editVue.tg_DepositManagementModel.depositProperty;

                getBankInfoList();
                getBankBranchList();
                getBankAccountEscrowedList();
			}
		});
	}

	function update(buttonType)
	{
		if(buttonType == 2)
        {
			editVue.showSubFlag = false;
        }
		var mdoel = {
            interfaceVersion:this.interfaceVersion,
            tableId:editVue.tg_DepositManagementModel.tableId,
            busiState:editVue.tg_DepositManagementModel.busiState,
            depositState : editVue.tg_DepositManagementModel.depositState,
            eCode : editVue.tg_DepositManagementModel.eCode,
            depositProperty : editVue.depositProperty,
            bankId : editVue.bankInfoId,
            bankOfDepositId : editVue.bankBranchId,
            escrowAcountId : editVue.escrowAcountId,
            openAccountCertificate : editVue.tg_DepositManagementModel.openAccountCertificate,
            storagePeriodCompany : editVue.tg_DepositManagementModel.storagePeriodCompany,
            storagePeriod : editVue.tg_DepositManagementModel.storagePeriod * 1,
            startDateStr : $('#date2101040201').val(),
            stopDateStr : $('#date2101040202').val(),
            principalAmount : commafyback(editVue.tg_DepositManagementModel.principalAmount) * 1,
            annualRate : commafyback(editVue.tg_DepositManagementModel.annualRate) * 1,
            expectedInterest : commafyback(editVue.tg_DepositManagementModel.expectedInterest) * 1,
            floatAnnualRate : editVue.tg_DepositManagementModel.floatAnnualRate,//commafyback(editVue.tg_DepositManagementModel.floatAnnualRate) * 1,
            agent : editVue.tg_DepositManagementModel.agent,
            
            calculationRule : editVue.tg_DepositManagementModel.calculationRule,
            
            remark : editVue.tg_DepositManagementModel.remark,
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
                enterNewTabCloseCurrent(editVue.tableId, '存单存入详情', 'tg/Tg_DepositManagementDetail.shtml');
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
//			console.log(jsonObj);
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
//        console.log(data.tableId);
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
//            console.log(jsonObj);
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
//            console.log(jsonObj);
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
            approvalCode : '210104',
            interfaceVersion:editVue.interfaceVersion
        };

        new ServerInterface(baseInfo.loadUploadInterface).execute(model, function(jsonObj)
        {
//            console.log(jsonObj);
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

    function countEndDate()
    {

        if (editVue.theStartDateStr == null || editVue.theStartDateStr.length == 0)
        {
            if ($('#date2101040201').val() == null || $('#date2101040201').val().length == 0)
            {
//                console.log("return");
                return;
            }
        }

        if (editVue.tg_DepositManagementModel.storagePeriodCompany.length > 0 && editVue.tg_DepositManagementModel.storagePeriod.length > 0)
        {
            var dateList;
            if (editVue.theStartDateStr == null || editVue.theStartDateStr.length == 0)
            {
                dateList = $('#date2101040201').val().split("-");
            }
            else
            {
                dateList = editVue.theStartDateStr.split("-");
            }

            if (dateList.length != 3)
            {
                return;
            }
            var theCount = editVue.tg_DepositManagementModel.storagePeriod;
            var theUnit  = editVue.tg_DepositManagementModel.storagePeriodCompany;
            var theYear = dateList[0] * 1;
            var theMonth = dateList[1] * 1 - 1;
            var date = dateList[2] * 1;

            var theDate  = new Date();
            theDate.setFullYear(theYear);
            theDate.setMonth(theMonth);
            theDate.setDate(date);

            // console.log(dateList[0] + "_" + (dateList[1] - 1) + "_" + dateList[2]);

//            console.log(theCount + "_" + theUnit + "_" + theDate);

            if (theUnit == '01')
            {
                theDate.setFullYear(theDate.getFullYear() + theCount * 1);
            }
            else if (theUnit == '02')
            {
                theDate.setMonth(theDate.getMonth() + theCount * 1);
            }
            else
            {
                theDate.setDate(theDate.getDate() + theCount * 1);
            }

//            console.log(theDate);


            $('#date2101040202').val(theDate.Format("yyyy-MM-dd"));

        }
    }

	function initData()
	{
		laydate.render({
            elem: '#date2101040201',
            done: function (value, date)
            {
                editVue.theStartDateStr = value;
                countEndDate();
            }
		});
		laydate.render({
            elem: '#date2101040202'
		});


        getIdFormTab('', function (tableId) {
            editVue.tableId = tableId;
//            console.log(editVue.tableId);
            //初始化
            refresh();
        });
	}
	
	function accrualHandle() {
		if(editVue.tg_DepositManagementModel.storagePeriodCompany != "" && editVue.tg_DepositManagementModel.storagePeriod != "" &&
				editVue.tg_DepositManagementModel.calculationRule != "" && editVue.tg_DepositManagementModel.principalAmount != "" &&
				editVue.tg_DepositManagementModel.annualRate != "") {
			// 存期单位 * 存期
			var storagePeriod = 0;
			if(editVue.tg_DepositManagementModel.storagePeriodCompany == "01") {
				storagePeriod = 365 * parseFloat(editVue.tg_DepositManagementModel.storagePeriod);
			}else if(editVue.tg_DepositManagementModel.storagePeriodCompany == "02") {
				storagePeriod = 30 * parseFloat(editVue.tg_DepositManagementModel.storagePeriod);
			}else {
				storagePeriod = parseFloat(editVue.tg_DepositManagementModel.storagePeriod);
			}
			// (存期单位 * 存期) / 计算规则
			var drt = storagePeriod / parseFloat(editVue.tg_DepositManagementModel.calculationRule);
			var value = editVue.tg_DepositManagementModel.principalAmount;
            value = value.replace(/[^0-9/.]/g,''); //清除“数字”和“.”以外的字符
            value = value.replace(/^\./g,"");  //验证第一个字符是数字而不是.
            value = value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的.
            value = value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
            editVue.tg_DepositManagementModel.principalAmount = value;
			editVue.tg_DepositManagementModel.expectedInterest = addThousands(drt * parseFloat(editVue.tg_DepositManagementModel.principalAmount) * parseFloat(editVue.tg_DepositManagementModel.annualRate) / 100);
		    
		}
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	editVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"editDivId":"#tg_DepositManagementEditDiv",
	"detailInterface":"../Tg_DepositManagementDetail",
	"updateInterface":"../Tg_DepositManagementUpdate",
	"bankInfoListInterface":"../Emmp_BankInfoList",
	"bankBranchListInterface":"../Emmp_BankBranchList",
    "bankAccountEscrowedInterface":"../Tgxy_BankAccountEscrowedForSelect",
    //材料附件
    "loadUploadInterface":"../Sm_AttachmentCfgList"
});
