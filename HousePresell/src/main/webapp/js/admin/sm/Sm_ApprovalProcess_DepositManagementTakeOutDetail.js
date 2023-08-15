(function(baseInfo){
	var editVue = new Vue({
		el : baseInfo.editDivId,
		data : {
			interfaceVersion :19000101,
			tg_DepositManagementModel: {},
			tableId : 1,

            //附件材料
            busiType:'210104',
            loadUploadList: [],
            showDelete : false,   //附件是否可编辑
            approvalCode: "210105",
            //----------审批流start-----------//
            afId:"",//申请单Id
            workflowId:"",//结点Id
            isNeedBackup:"",//是否需要备案
            sourcePage:"",//来源页面
            //----------审批流end-----------//

        },
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
            //其他
            showModal : showModal,
            approvalHandle : function ()
            {
                approvalModalVue.buttonType = 2;
                approvalModalVue.approvalHandle();
            }
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
			tableId:editVue.tableId,
            afId:editVue.afId,
        }
	}

	//详情操作--------------
	function refresh()
	{
		if (editVue.tableId == null || editVue.tableId < 1) 
		{
			console.log(editVue.tableId);
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
//				editVue.tg_DepositManagementModel = jsonObj.tg_DepositManagement;
//                editVue.isNeedBackup = jsonObj.isNeedBackup;
//                approvalModalVue.isNeedBackup = editVue.isNeedBackup;
                

				editVue.tg_DepositManagementModel = jsonObj.tg_DepositManagement;
				
				editVue.tg_DepositManagementModel.principalAmount = addThousands(editVue.tg_DepositManagementModel.principalAmount);
				editVue.tg_DepositManagementModel.expectedInterest = addThousands(editVue.tg_DepositManagementModel.expectedInterest);
				editVue.tg_DepositManagementModel.realInterest = addThousands(editVue.tg_DepositManagementModel.realInterest);
				if( undefined != editVue.tg_DepositManagementModel.annualRate && "" != editVue.tg_DepositManagementModel.annualRate	
		               && undefined != editVue.tg_DepositManagementModel.realInterestRate && "" != editVue.tg_DepositManagementModel.realInterestRate
	                   ){
		              	// editVue.tg_DepositManagementModel.floatAnnualRate = Number(editVue.tg_DepositManagementModel.floatAnnualRate).toFixed(3);
		                 editVue.tg_DepositManagementModel.annualRate = Number(editVue.tg_DepositManagementModel.annualRate).toFixed(3);
		                 editVue.tg_DepositManagementModel.realInterestRate = Number(editVue.tg_DepositManagementModel.realInterestRate).toFixed(3);
		            }
				switch (editVue.tg_DepositManagementModel.depositProperty) {
                case "01":
                	editVue.tg_DepositManagementModel.depositProperty = "大额存单";
                    break;
                case "02":
                	editVue.tg_DepositManagementModel.depositProperty = "结构性存款";
                    break;
                case "03":
                	editVue.tg_DepositManagementModel.depositProperty = "保本理财";
                    break;
                default:
                	editVue.tg_DepositManagementModel.depositProperty = null;
                    break;
            }

            switch (editVue.tg_DepositManagementModel.storagePeriodCompany) {
                case "01":
                	editVue.tg_DepositManagementModel.storagePeriodCompany = "年";
                    break;
                case "02":
                	editVue.tg_DepositManagementModel.storagePeriodCompany = "月";
                    break;
                case "03":
                	editVue.tg_DepositManagementModel.storagePeriodCompany = "天";
                    break;
                default:
                	editVue.tg_DepositManagementModel.storagePeriodCompany = null;
                    break;
            }
				
                editVue.isNeedBackup = jsonObj.isNeedBackup;
                approvalModalVue.isNeedBackup = editVue.isNeedBackup;
			
			}
		});
	}

    /********* 附件材料 相关 *********/
    function loadUpload(){
        var model = {
            pageNumber : '0',
            busiType : '210104',
            sourceId : editVue.tableId,
            interfaceVersion:editVue.interfaceVersion,
            approvalCode : '210105'
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

    /********* 初始化方法 *********/
    function initData()
    {
        var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
        var array = tableIdStr.split("_");
        if (array.length > 1)
        {
            editVue.tableId = array[array.length-4];
            editVue.afId = array[array.length-3];
            editVue.workflowId = array[array.length-2];
            editVue.sourcePage = array[array.length-1];

            approvalModalVue.afId = editVue.afId;
            approvalModalVue.workflowId = editVue.workflowId;
            approvalModalVue.sourcePage = editVue.sourcePage;
            refresh();
        }
    }

    function showModal()
    {
        approvalModalVue.getModalWorkflowList();
    }

    //------------------------方法定义-结束------------------//

    //------------------------数据初始化-开始----------------//

    editVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"editDivId":"#Sm_ApprovalProcess_DepositManagementTakeOutDetailDiv",
	"detailInterface":"../Tg_DepositManagementDetail",
    //材料附件
    "loadUploadInterface":"../Sm_AttachmentCfgList"
});
