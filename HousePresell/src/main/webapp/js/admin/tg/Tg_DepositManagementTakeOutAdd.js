(function(baseInfo){
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			tg_DepositManagementModel: {},
			tableId : 1,
			//其他
			emmp_BankInfoList : [],
			bankInfoId: "",
			emmp_BankBranchList : [],
			bankBranchId : "",
            bankOfDeposit : "",

            realInterest : "",
            realInterestRate : "",

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
			depositPropertyStr : "",

            //附件材料
            busiType:'210104',
            loadUploadList: [],
            showDelete : true,  //附件是否可编辑
            principalAmount: 0,
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			//更新
			update: update,
			changeDepositProperty: function (data) {
                this.depositProperty = data.tableId;
            },
            changeStoragePeriodCompany: function (data) {
                this.storagePeriodCompany = data.tableId;
            },
            changeNumber:function (theKey){
                var value = addVue[theKey];
                value = value.replace(/[^0-9/.]/g,''); //清除“数字”和“.”以外的字符
                value = value.replace(/^\./g,"");  //验证第一个字符是数字而不是.
                value = value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的.
                value = value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
                addVue[theKey] = value;
                
                if(addVue[theKey] != "") {
                    // 存期 * 存期单位 / 计算规则
                    if(addVue.tg_DepositManagementModel.storagePeriodCompany == "01") {
                    	var drt = parseFloat(addVue.tg_DepositManagementModel.storagePeriod) * 365 / parseFloat(addVue.tg_DepositManagementModel.calculationRule);
                    }else if(addVue.tg_DepositManagementModel.storagePeriodCompany == "02") {
                    	var drt = parseFloat(addVue.tg_DepositManagementModel.storagePeriod) * 30 / parseFloat(addVue.tg_DepositManagementModel.calculationRule);
                    }else {
                    	var drt = parseFloat(addVue.tg_DepositManagementModel.storagePeriod) / parseFloat(addVue.tg_DepositManagementModel.calculationRule);
                    }
                    addVue.realInterestRate = addThousands(parseFloat(addVue[theKey]) / drt / parseFloat(addVue.principalAmount) * 100);
                    console.log(addVue.realInterestRate)
                    var val = addVue.realInterestRate;
                    if(val == "0.000") {
                    	addVue.realInterestRate = "0.000";
                    }else {
                        addVue.realInterestRate = Math.round(val*100)/100;
                        addVue.realInterestRate = Number(addVue.realInterestRate).toFixed(3);
                    }
                    if(addVue[theKey] == "1") {
                    	addVue.realInterestRate = "0.000";
                    }
                }else {
                	addVue.realInterestRate = "0.000";
                }
            },
            changeThousands:function (theKey) {
                var value = addVue[theKey];
                value = addThousands(value);
                addVue[theKey] = value;
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
			tableId:addVue.tableId,
		}
	}

	//详情操作--------------
	function refresh()
	{
		if (addVue.tableId == null || addVue.tableId < 1)
		{
			console.log(addVue.tableId);
			return;
		}

		getDetail();
        loadUpload();
	}

	function getDetail()
	{
		new ServerInterface(baseInfo.detailInterface).execute(addVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				addVue.tg_DepositManagementModel = jsonObj.tg_DepositManagement;
                addVue.principalAmount = addVue.tg_DepositManagementModel.principalAmount;
                addVue.tg_DepositManagementModel.principalAmount = addThousands(addVue.tg_DepositManagementModel.principalAmount);
               // addVue.tg_DepositManagementModel.annualRate = addThousands(addVue.tg_DepositManagementModel.annualRate);
                addVue.tg_DepositManagementModel.annualRate = Number(addVue.tg_DepositManagementModel.annualRate).toFixed(3);
                addVue.tg_DepositManagementModel.realInterestRate = Number(addVue.tg_DepositManagementModel.realInterestRate).toFixed(3);
                addVue.tg_DepositManagementModel.expectedInterest = addThousands(addVue.tg_DepositManagementModel.expectedInterest);
              //  addVue.tg_DepositManagementModel.floatAnnualRate = addThousands(addVue.tg_DepositManagementModel.floatAnnualRate);
                
                switch (jsonObj.tg_DepositManagement.depositProperty) {
                case "01":
                	addVue.depositPropertyStr = "大额存单";
                    break;
                case "02":
                	addVue.depositPropertyStr = "结构性存款";
                    break;
                case "03":
                	addVue.depositPropertyStr = "保本理财";
                    break;
                default:
                	addVue.depositPropertyStr = null;
                    break;
            }
                
			}
		});
	}
	
	//详情更新操作--------------
	function update()
	{
		if(null == addVue.realInterest || "" == addVue.realInterest){
			console.log("请输入实际利息！");
			generalErrorModal("","请输入实际利息！");
			return;
		}
		
			
		if(null == $('#date2101050101').val() || "" == $('#date2101050101').val()){
			console.log("请选择提取日期！");
			generalErrorModal("","请选择提取日期！");
			return;
		}
			
		var model = {
            interfaceVersion:this.interfaceVersion,
            tableId: addVue.tg_DepositManagementModel.tableId,
            busiState:addVue.tg_DepositManagementModel.busiState,
            depositState : "02",
            eCode : addVue.tg_DepositManagementModel.eCode,
            depositProperty : addVue.tg_DepositManagementModel.depositProperty,
            bankId : addVue.tg_DepositManagementModel.bankId,
            bankOfDepositId : addVue.tg_DepositManagementModel.bankOfDepositId,
            openAccountCertificate : addVue.tg_DepositManagementModel.openAccountCertificate,
            escrowAcountId : addVue.tg_DepositManagementModel.escrowAcountId,
            storagePeriodCompany : addVue.tg_DepositManagementModel.storagePeriodCompany,
            storagePeriod : addVue.tg_DepositManagementModel.storagePeriod,
            startDateStr : addVue.tg_DepositManagementModel.startDateStr,
            stopDateStr : addVue.tg_DepositManagementModel.stopDateStr,
            principalAmount : commafyback(addVue.tg_DepositManagementModel.principalAmount) * 1,
            annualRate : commafyback(addVue.tg_DepositManagementModel.annualRate) * 1,
            expectedInterest : commafyback(addVue.tg_DepositManagementModel.expectedInterest) * 1,
            floatAnnualRate : addVue.tg_DepositManagementModel.floatAnnualRate,//commafyback(addVue.tg_DepositManagementModel.floatAnnualRate) * 1,
            agent : addVue.tg_DepositManagementModel.agent,
            remark : addVue.tg_DepositManagementModel.remark,
            
            extractDateStr : $('#date2101050101').val(),
            realInterest : commafyback(addVue.realInterest) * 1,
            realInterestRate : commafyback(addVue.realInterestRate) * 1,

            busiType:'210104',
            generalAttachmentList:this.$refs.listenUploadData.uploadData,

            buttonType : '2',

            approvalState: "审核中"

        };

		new ServerInterfaceJsonBody(baseInfo.updateInterface).execute(model, function(jsonObj)
		{
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                generalSuccessModal();
                enterNewTabCloseCurrent(addVue.tableId, '存单提取详情', 'tg/Tg_DepositManagementTakeOutDetail.shtml');
            }
		});
	}

    /********* 附件材料 相关 *********/
    function loadUpload(){
        model = {
            pageNumber : '0',
            busiType : '210104',
            sourceId : addVue.tableId,
            approvalCode : '210105',
            interfaceVersion:addVue.interfaceVersion
        };

        new ServerInterface(baseInfo.loadUploadInterface).execute(model, function(jsonObj)
        {
            console.log(jsonObj);
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
	
	function initData()
	{
        laydate.render({
            elem: '#date2101050101'
        });

        getIdFormTab('', function (tableId) {
            addVue.tableId = tableId;
            console.log(addVue.tableId);
            //初始化
            refresh();
        });

	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	addVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#tg_DepositManagementTakeOutAddDiv",
	"detailInterface":"../Tg_DepositManagementDetail",
	"updateInterface":"../Tg_DepositManagementUpdate",
	"bankInfoListInterface":"../Emmp_BankInfoList",
	"bankBranchListInterface":"../Emmp_BankBranchList",
    //材料附件
    "loadUploadInterface":"../Sm_AttachmentCfgList"
});
