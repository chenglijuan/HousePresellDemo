(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tg_DepositManagementModel: {},
			tableId : 1,
            depositPropertyStr:null,

            //附件材料
            busiType:'210106',
            loadUploadList: [],
            showDelete : false,  //附件是否可编辑
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			
			depositManagementEditHandle : depositManagementEditHandle ,
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
			console.log(detailVue.tableId);
			
			return;
		}

		getDetail();
        loadUpload();
	}

	function getDetail()
	{
		new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function(jsonObj)
		{
			console.log(jsonObj);
			
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				detailVue.tg_DepositManagementModel = jsonObj.tg_DepositManagement;

                detailVue.tg_DepositManagementModel.principalAmount = addThousands(detailVue.tg_DepositManagementModel.principalAmount);

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
                        detailVue.depositPropertyStr = "";
                        break;
                }
			}
		});
	}
	
	function depositManagementEditHandle(){
        enterNewTabCloseCurrent(detailVue.tableId, '存单正在办理修改', 'tg/Tg_DepositManagementInProgressEdit.shtml');
	}

    /********* 附件材料 相关 *********/
    function loadUpload(){
        var model = {
            pageNumber : '0',
            busiType : '210106',
            sourceId : detailVue.tableId,
            interfaceVersion:detailVue.interfaceVersion
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
                detailVue.loadUploadList = jsonObj.sm_AttachmentCfgList;
            }
        });
    }

    function initData()
	{
        getIdFormTab('', function (tableId) {
            detailVue.tableId = tableId;
            console.log(detailVue.tableId);
            //初始化
            refresh();
        });
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tg_DepositManagementInProgressDetailDiv",
	"detailInterface":"../Tg_DepositManagementDetail",
    //材料附件
    "loadUploadInterface":"../Sm_AttachmentCfgList"
});
