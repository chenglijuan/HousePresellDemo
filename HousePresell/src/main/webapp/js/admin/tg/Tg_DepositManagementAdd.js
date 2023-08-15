(function(baseInfo){
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			depositState : "01",
			eCode : "",
			depositProperty : "",
//			bankId : "",
			bankOfDeposit : "",
			openAccountCertificate : "",
			escrowAcount : "",
			storagePeriodCompany : "",
			storagePeriod : "",
			startDate : "",
			stopDate : "",
			principalAmount : "",
			annualRate : null,
			expectedInterest : "",
			floatAnnualRate : "",
			agent : "",
			userCreateId : "",
			userCreateName : "",
			createTimeStamp : "",

            theStartDateStr : "",
            remark : "",
            //其他
            emmp_BankInfoList : [],
            bankInfoId: "",
            emmp_BankBranchList : [],
            bankBranchId : "",
            tgxy_BankAccountEscrowedList: [],
            escrowAcountId : "",
            calculationRule : "365",
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

            //附件材料
            busiType:'210104',
            loadUploadList: [],
            showDelete : true,   //附件是否可编辑
            showSubFlag : true,
		},
		methods : {
			//详情
			initData: initData,
			//添加
			add: add,
			bankInfoChange : bankInfoChange,
            bankBranChange : bankBranChange,
            countEndDate : countEndDate,
            getEscrowAcountId : function (data) {
                this.escrowAcountId = data.tableId;
            },
            changeDepositProperty: function (data) {
                this.depositProperty = data.tableId;
            },
            changeStoragePeriodCompany: function (data) {
                this.storagePeriodCompany = data.tableId;
                countEndDate();
                // 计算预计利息
                accrualHandle();
            },
            changeCalculationRule : function (data) {
                this.calculationRule = data.tableId;
                // 计算预计利息
                accrualHandle();
            },
            changeNumber:function (theKey){
                var value = addVue[theKey];
                if(value !="" && value !=undefined){
                	value = value.replace(/[^0-9/.]/g,''); //清除“数字”和“.”以外的字符
                    value = value.replace(/^\./g,"");  //验证第一个字符是数字而不是.
                    value = value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的.
                    value = value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
                    addVue[theKey] = value;
                    // 计算预计利息
                    accrualHandle();
                }
            },
            changeThousands:function (theKey) {
                var value = addVue[theKey];
               // value = addThousands(value);
                addVue[theKey] = value;
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
	
	//详情更新操作--------------
	function add(buttonType)
	{
		if(buttonType == 2)
        {
            addVue.showSubFlag = false;
        }
		var model = {
            interfaceVersion : addVue.interfaceVersion,
            depositState : addVue.depositState,
            depositProperty : addVue.depositProperty,
            bankId : addVue.bankInfoId,
            bankOfDepositId : addVue.bankBranchId,
            openAccountCertificate : addVue.openAccountCertificate,
            escrowAcountId : addVue.escrowAcountId,
            storagePeriodCompany : addVue.storagePeriodCompany,
            storagePeriod : addVue.storagePeriod * 1,
            startDateStr : $('#date2101040101').val(),
            stopDateStr : $('#date2101040102').val(),
            principalAmount : commafyback(addVue.principalAmount) * 1,
            annualRate : commafyback(addVue.annualRate) * 1,
            expectedInterest : commafyback(addVue.expectedInterest) * 1,
            floatAnnualRate : addVue.floatAnnualRate,//commafyback(addVue.floatAnnualRate) * 1,
            agent : addVue.agent,
            calculationRule : addVue.calculationRule,
            remark : addVue.remark,
            //附件材料
            busiType:'210104',
            generalAttachmentList:this.$refs.listenUploadData.uploadData,

            buttonType : buttonType,

            busiState:"未备案",
            approvalState: (buttonType == "1") ? "待提交" : "审核中"
            	
		};

		new ServerInterfaceJsonBody(baseInfo.addInterface).execute(model, function(jsonObj)
		{
			addVue.showSubFlag = true;
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                generalSuccessModal();
                enterNewTabCloseCurrent(jsonObj.tg_DepositManagement.tableId, '存单存入详情', 'tg/Tg_DepositManagementDetail.shtml');
            }
		});
	}

	function getBankInfoList() 
	{
		var model = {
			interfaceVersion: addVue.interfaceVersion,
            theState : 0
		};
		
		new ServerInterface(baseInfo.bankInfoListInterface).execute(model, function(jsonObj)
		{
			
			if (jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				addVue.emmp_BankInfoList = jsonObj.emmp_BankInfoList;
			}
		});
	}

    function bankInfoChange(data)
    {
        if(addVue.bankInfoId != data.tableId)
        {
            addVue.emmp_BankBranchList = [];
            addVue.bankBranchId = "";
        }

        addVue.bankInfoId = data.tableId;
        getBankBranchList();
    }

    function getBankBranchList()
    {
        var model = {
            interfaceVersion : addVue.interfaceVersion,
            bankId : addVue.bankInfoId,
            theState : 0
        };

        new ServerInterface(baseInfo.bankBranchListInterface).execute(model, function(jsonObj)
        {
            if (jsonObj.result != "success")
            {

            }
            else
            {
                addVue.emmp_BankBranchList = jsonObj.emmp_BankBranchList;
            }
        });
    }

    function bankBranChange(data)
    {
        if(addVue.bankBranchId != data.tableId)
        {
            addVue.tgxy_BankAccountEscrowedList = [];
            addVue.escrowAcountId = "";
        }

        this.bankBranchId = data.tableId;
        getBankAccountEscrowedList();
    }

    function getBankAccountEscrowedList()
    {
        var model = {
            interfaceVersion : addVue.interfaceVersion,
            bankBranchId : addVue.bankBranchId,
            theState : 0
        };

        new ServerInterface(baseInfo.bankAccountEscrowedInterface).execute(model, function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                addVue.tgxy_BankAccountEscrowedList = jsonObj.tgxy_BankAccountEscrowedList;
            }
        });
    }

    /********* 附件材料 相关 *********/

    function loadUpload(){
        model = {
            pageNumber : '0',
            busiType : '210104',
            interfaceVersion:addVue.interfaceVersion
        };

        new ServerInterface(baseInfo.loadUploadInterface).execute(model, function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                addVue.loadUploadList = jsonObj.sm_AttachmentCfgList;
            }
        });
    }

    function countEndDate()
    {

        if (addVue.theStartDateStr == null || addVue.theStartDateStr.length == 0)
        {
            if ($('#date2101040101').val() == null || $('#date2101040101').val().length == 0)
            {
                return;
            }
        }

        if (addVue.storagePeriodCompany.length > 0 && addVue.storagePeriod.length > 0)
        {
            var dateList;
            if (addVue.theStartDateStr == null || addVue.theStartDateStr.length == 0)
            {
                dateList = $('#date2101040101').val().split("-");
            }
            else
            {
                dateList = addVue.theStartDateStr.split("-");
            }

            if (dateList.length != 3)
            {
                return;
            }
            var theCount = addVue.storagePeriod;
            var theUnit  = addVue.storagePeriodCompany;
            var theYear = dateList[0] * 1;
            var theMonth = dateList[1] * 1 - 1;
            var date = dateList[2] * 1;

            var theDate  = new Date();
            theDate.setFullYear(theYear);
            theDate.setMonth(theMonth);
            theDate.setDate(date);



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



            $('#date2101040102').val(theDate.Format("yyyy-MM-dd"));

        }
    }

	function initData()
	{
		laydate.render({
            elem: '#date2101040101',
            done: function (value, date)
            {
                addVue.theStartDateStr = value;
                countEndDate();
            }
		});
		laydate.render({
            elem: '#date2101040102'
		});
		
		getBankInfoList();
        loadUpload();
	}
	
	function accrualHandle() {
		if(addVue.storagePeriod != "" && addVue.storagePeriodCompany != "" &&
				addVue.calculationRule != "" && addVue.principalAmount != "" &&
				addVue.annualRate != "") {
			// 存期单位 * 存期
			var storagePeriod = 0;
			if(addVue.storagePeriodCompany == "01") {
				storagePeriod = 365 * parseFloat(addVue.storagePeriod);
			}else if(addVue.storagePeriodCompany == "02") {
				storagePeriod = 30 * parseFloat(addVue.storagePeriod);
			}else {
				storagePeriod = parseFloat(addVue.storagePeriod);
			}
			// (存期单位 * 存期) / 计算规则
			var value = addVue.principalAmount;
            value = value.replace(/[^0-9/.]/g,''); //清除“数字”和“.”以外的字符
            value = value.replace(/^\./g,"");  //验证第一个字符是数字而不是.
            value = value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的.
            value = value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
            addVue.principalAmount = value;
			var drt = storagePeriod / parseFloat(addVue.calculationRule);
			addVue.expectedInterest = addThousands(drt * parseFloat(addVue.principalAmount) * parseFloat(addVue.annualRate) / 100);
		}
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	addVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#tg_DepositManagementAddDiv",
	"addInterface":"../Tg_DepositManagementAdd",
	"bankInfoListInterface":"../Emmp_BankInfoList",
	"bankBranchListInterface":"../Emmp_BankBranchList",
    "bankAccountEscrowedInterface":"../Tgxy_BankAccountEscrowedForSelect",
    //材料附件
    "loadUploadInterface":"../Sm_AttachmentCfgList"
});
