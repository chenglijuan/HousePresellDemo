(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tg_DepositManagementModel: {},
			tableId : 1,

            depositPropertyStr : null,
            storagePeriodCompanyStr : null,

            //附件材料
            busiType:'210104',
            loadUploadList: [],
            showDelete : false  //附件是否可编辑
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			
			depositManagementEditHandle : depositManagementEditHandle
		},
		computed:{
			 
		},
		components : {
            "my-uploadcomponent":fileUpload
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
        loadUpload();
	}

	function getDetail()
	{
		new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function(jsonObj)
		{
			
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				detailVue.tg_DepositManagementModel = jsonObj.tg_DepositManagement;

                detailVue.tg_DepositManagementModel.principalAmount = addThousands(detailVue.tg_DepositManagementModel.principalAmount);
//                detailVue.tg_DepositManagementModel.annualRate = addThousands(detailVue.tg_DepositManagementModel.annualRate);
                detailVue.tg_DepositManagementModel.expectedInterest = addThousands(detailVue.tg_DepositManagementModel.expectedInterest);
//                detailVue.tg_DepositManagementModel.floatAnnualRate = addThousands(detailVue.tg_DepositManagementModel.floatAnnualRate);
                if(undefined != detailVue.tg_DepositManagementModel.annualRate && "" != detailVue.tg_DepositManagementModel.annualRate	){
                	console.log(detailVue.tg_DepositManagementModel.floatAnnualRate)
                	// detailVue.tg_DepositManagementModel.floatAnnualRate = Number(detailVue.tg_DepositManagementModel.floatAnnualRate).toFixed(3);
                	 
                     detailVue.tg_DepositManagementModel.annualRate = Number(detailVue.tg_DepositManagementModel.annualRate).toFixed(3);
                }
               
                switch (jsonObj.tg_DepositManagement.depositProperty) {
                    case "01":
                        detailVue.depositPropertyStr = "大额存单";
                        break;
                    case "02":
                        detailVue.depositPropertyStr = "结构性存款";
                        break;
                    case "03":
                        detailVue.depositPropertyStr = "保本理财";
                        break;
                    default:
                        detailVue.depositPropertyStr = null;
                        break;
                }

                switch (jsonObj.tg_DepositManagement.storagePeriodCompany) {
                    case "01":
                        detailVue.storagePeriodCompanyStr = "年";
                        break;
                    case "02":
                        detailVue.storagePeriodCompanyStr = "月";
                        break;
                    case "03":
                        detailVue.storagePeriodCompanyStr = "天";
                        break;
                    default:
                        detailVue.storagePeriodCompanyStr = null;
                        break;
                }
			}
		});
	}
	
	function depositManagementEditHandle(){
        enterNewTabCloseCurrent(detailVue.tableId, '存单存入修改', 'tg/Tg_DepositManagementEdit.shtml');
	}

    /********* 附件材料 相关 *********/
    function loadUpload(){
        var model = {
            pageNumber : '0',
            busiType : '210104',
            sourceId : detailVue.tableId,
            approvalCode : '210104',
            interfaceVersion:detailVue.interfaceVersion
        };

        new ServerInterface(baseInfo.loadUploadInterface).execute(model, function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                detailVue.loadUploadList = jsonObj.sm_AttachmentCfgList;
            }
        });
    }

    function initData()
	{
        getIdFormTab('', function (tableId) {
            detailVue.tableId = tableId;
            //初始化
            refresh();
        });
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tg_DepositManagementDetailDiv",
	"detailInterface":"../Tg_DepositManagementDetail",
    //材料附件
    "loadUploadInterface":"../Sm_AttachmentCfgList"
});
