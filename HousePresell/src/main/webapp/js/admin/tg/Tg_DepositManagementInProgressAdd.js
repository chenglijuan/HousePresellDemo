(function(baseInfo){
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			depositState : "03",
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

			//其他
			emmp_BankInfoList : [],
			bankInfoId: "",
			emmp_BankBranchList : [],
			bankBranchId : "",
            tgxy_BankAccountEscrowedList: [],
            escrowAcountId : "",

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
            busiType:'210106',
            loadUploadList: [],
            showDelete : true   //附件是否可编辑

		},
		methods : {
			//详情
			initData: initData,
			//添加
			add: add,
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
			},
            changeNumber:function (theKey){
                var value = addVue[theKey];
                value = value.replace(/[^0-9/.]/g,''); //清除“数字”和“.”以外的字符
                value = value.replace(/^\./g,"");  //验证第一个字符是数字而不是.
                value = value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的.
                value = value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
                addVue[theKey] = value;
            },
            changeThousands:function (theKey) {
                var value = addVue[theKey];
                value = addThousands(value);
                addVue[theKey] = value;
            }
		},
		computed: {
			 
		},
		components : {
            "my-uploadcomponent":fileUpload,
			'vue-select': VueSelect
		},
		watch: {
			
		}
	});

	//------------------------方法定义-开始------------------//
	
	//详情更新操作--------------
	function add()
	{
		var model = {
            interfaceVersion : addVue.interfaceVersion,
            depositState : addVue.depositState,
            depositProperty : addVue.depositProperty,
            bankId : addVue.bankInfoId,
            bankOfDepositId : addVue.bankBranchId,
            escrowAcountId : addVue.escrowAcountId,
            openAccountCertificate : addVue.openAccountCertificate,
            storagePeriodCompany : addVue.storagePeriodCompany,
            storagePeriod : addVue.storagePeriod * 1,
            startDateStr : $('#date2101060101').val(),
            principalAmount : commafyback(addVue.principalAmount) * 1,
            agent : addVue.agent,

            //附件材料
            busiType:'210106',
            generalAttachmentList:this.$refs.listenUploadData.uploadData
        };


		new ServerInterfaceJsonBody(baseInfo.addInterface).execute(model, function(jsonObj)
		{
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                generalSuccessModal();
                enterNewTabCloseCurrent(jsonObj.tg_DepositManagement.tableId, '存单存入详情', 'tg/Tg_DepositManagementInProgressDetail.shtml');
            }
		});
	}

	function getBankInfoList() 
	{
		var model = {
			interfaceVersion: addVue.interfaceVersion,
            theState : 0
		};
		
		new  ServerInterface(baseInfo.bankInfoListInterface).execute(model, function(jsonObj) 
		{
			console.log(jsonObj);
			
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
		console.log(data.tableId);
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
			console.log(jsonObj);
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
            console.log(jsonObj);
            if (jsonObj.result != "success")
            {

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
            busiType : '210106',
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

	function initData()
	{
		laydate.render({
			  elem: '#date2101060101'
		});
		
		getBankInfoList();
        loadUpload();
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	addVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#tg_DepositManagementInProgressAddDiv",
	"addInterface":"../Tg_DepositManagementAdd",
	"bankInfoListInterface":"../Emmp_BankInfoList",
	"bankBranchListInterface":"../Emmp_BankBranchList",
    "bankAccountEscrowedInterface":"../Tgxy_BankAccountEscrowedForSelect",
    //材料附件
    "loadUploadInterface":"../Sm_AttachmentCfgList"
});
