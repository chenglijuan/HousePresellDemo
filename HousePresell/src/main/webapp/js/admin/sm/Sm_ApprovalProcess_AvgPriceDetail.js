(function(baseInfo){
	var updateVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion : 19000101,
			tableId : 1,
			// empj_BuildingInfo : {
            // 	expectBuilding : {},
            // },
            tgpj_BuildingAvgPrice: {},
            tgpj_BuildingAvgPriceNew: {},
			//附件材料
			busiType : '03010301',
			loadUploadList: [],
			showDelete : false,
			buttonType : "",//按钮来源（保存、提交）
            //----------审批流start-----------//
            afId:"",//申请单Id
            workflowId:"",//结点Id
            isNeedBackup:"",//是否需要备案
            sourcePage:"",//来源页面
            //----------审批流end-----------//
            approvalCode: "03010301",

            presellPrice:"",
            companyId:"",
            isShowPresell:false,
			buildingId:"",
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			showModal : showModal,
			approvalHandle : function ()
		    {
		        approvalModalVue.buttonType = 2;
		        approvalModalVue.approvalHandle();
		    },
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
			interfaceVersion:this.interfaceVersion,
			tableId:this.tableId,
            afId:this.afId,
		}
	}

	//详情操作--------------
	function refresh()
	{
		if (updateVue.tableId == null || updateVue.tableId < 1) 
		{
			return;
		}

		getDetail();
		
		generalLoadFile2(updateVue, updateVue.busiType)
	}

	function getDetail()
	{
		serverRequest(baseInfo.detailInterface,updateVue.getSearchForm(),function (jsonObj) {
            updateVue.tgpj_BuildingAvgPrice = jsonObj.tgpj_BuildingAvgPrice
            updateVue.tgpj_BuildingAvgPrice.recordAveragePrice = addThousands(jsonObj.tgpj_BuildingAvgPrice.recordAveragePrice)
			if(jsonObj.tgpj_BuildingAvgPrice.recordAveragePriceFromPresellSystem!=undefined){
                updateVue.tgpj_BuildingAvgPrice.recordAveragePriceFromPresellSystem = addThousands(jsonObj.tgpj_BuildingAvgPrice.recordAveragePriceFromPresellSystem)
			}
            updateVue.tgpj_BuildingAvgPriceNew = jsonObj.tgpj_BuildingAvgPriceNew
            updateVue.tgpj_BuildingAvgPriceNew.recordAveragePrice = addThousands(jsonObj.tgpj_BuildingAvgPriceNew.recordAveragePrice)
            updateVue.isNeedBackup=jsonObj.isNeedBackup;
            approvalModalVue.isNeedBackup = updateVue.isNeedBackup;

            if(updateVue.tgpj_BuildingAvgPrice.busiState == '已备案')
            {
                updateVue.approvalCode = '03010302'
            }
            updateVue.buildingId=jsonObj.tgpj_BuildingAvgPrice.buildingInfoId
        })
		// new ServerInterface(baseInfo.detailInterface).execute(updateVue.getSearchForm(), function(jsonObj)
		// {
		// 	if(jsonObj.result != "success")
		// 	{
		// 		generalErrorModal(jsonObj);
		// 	}
		// 	else
		// 	{
		// 		updateVue.tgpj_BuildingAvgPriceModel = jsonObj.tgpj_BuildingAvgPriceModel;
		// 	}
		// });
	}
	
	function initData()
	{
        var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var array = tableIdStr.split("_");
		if (array.length > 1)
		{
            updateVue.tableId = array[array.length-4];
            updateVue.afId = array[array.length-3];
            updateVue.workflowId = array[array.length-2];
            updateVue.sourcePage = array[array.length-1];

            approvalModalVue.afId = updateVue.afId;
            approvalModalVue.workflowId = updateVue.workflowId;
            approvalModalVue.sourcePage = updateVue.sourcePage;
			refresh();
		}

        getLoginUserInfo(function (user) {
            if (user.theType == "1") {
                updateVue.isShowPresell = true
            } else {
                updateVue.isShowPresell = false
            }
        })
	}
	
	function showModal()
	{
        approvalModalVue.extraObj = {
            sourceType: "Tgpj_BuildingAvgPrice",
            tableId: updateVue.tableId,
			buildingId:updateVue.buildingId,
			busiCode:updateVue.approvalCode,
            // attachMent:this.$refs.listenUploadData.uploadData,
        }
		approvalModalVue.getModalWorkflowList();
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	updateVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#sm_ApprovalProcess_AvgPriceDetailDiv",
	"detailInterface":"../Tgpj_BuildingAvgPriceDetail",
	"updateInterface":"../Tgpj_BuildingAvgPriceUpdate",
});
