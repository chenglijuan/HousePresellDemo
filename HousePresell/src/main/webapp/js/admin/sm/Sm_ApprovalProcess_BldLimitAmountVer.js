(function(baseInfo){
	var updateVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion : 19000101,
			tableId : 1,
            pageNumber : 1,
            countPerPage : 20,
            totalPage : 1,
            totalCount : 1,
            keyword : "",
            selectItem : [],
            isAllSelected : false,
            theState:0,//正常为0，删除为1
            theTypeName:"",
			// empj_BuildingInfo : {
            // 	expectBuilding : {},
            // },
            // tgpj_BuildingAvgPrice: {},
            // tgpj_BuildingAvgPriceNew: {},
            tgpj_BldLimitAmountVer_AFModel: {},
            isUsing:"",
            houseType:"",
            nodeVersionList:[],
			//附件材料
			busiType : '06010102',
			loadUploadList: [],
			showDelete : false,
			buttonType : "",//按钮来源（保存、提交）
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
			showModal : showModal,
			approvalHandle : function ()
		    {
		        approvalModalVue.buttonType = 2;
		        approvalModalVue.approvalHandle();
		    },

            indexMethod: indexMethod,
            listItemSelectHandle:listItemSelectHandle,
            checkAllClicked : checkAllClicked,
            changePageNumber : function(data){
                updateVue.pageNumber = data;
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
            updateVue.tgpj_BldLimitAmountVer_AFModel = jsonObj.tgpj_BldLimitAmountVer_AF;
            updateVue.isUsing=isUseNumber2String(updateVue.tgpj_BldLimitAmountVer_AFModel.theState)
            updateVue.theTypeName=jsonObj.parameterName
            updateVue.isNeedBackup=jsonObj.isNeedBackup
            approvalModalVue.isNeedBackup=jsonObj.isNeedBackup
            updateVue.houseType=houseType2String(updateVue.tgpj_BldLimitAmountVer_AFModel)
			console.log('success get detail')
            getNodeVersionList()
        })
        // serverRequest(baseInfo.detailInterface,updateVue.getSearchForm(),function (jsonObj) {
        //     updateVue.tgpj_BuildingAvgPrice = jsonObj.tgpj_BuildingAvgPrice;
        //     updateVue.tgpj_BuildingAvgPriceNew = jsonObj.tgpj_BuildingAvgPriceNew;
        //     updateVue.isNeedBackup=jsonObj.isNeedBackup
        // })
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

    function getNodeVersionList() {
		console.log('getNodeVersionList')
        serverRequest(baseInfo.getNodeList,getSearchListForm(),function(jsonObj){
            updateVue.nodeVersionList=jsonObj.tgpj_BldLimitAmountVer_AFDtlList;
            updateVue.pageNumber=jsonObj.pageNumber;
            updateVue.countPerPage=jsonObj.countPerPage;
            updateVue.totalPage=jsonObj.totalPage;
            updateVue.totalCount = jsonObj.totalCount;
            updateVue.keyword=jsonObj.keyword;
            updateVue.selectedItem=[];

        })
    }

    function getSearchListForm()
    {
        return {
            interfaceVersion:updateVue.interfaceVersion,
            pageNumber:updateVue.pageNumber,
            countPerPage:updateVue.countPerPage,
            totalPage:updateVue.totalPage,
            keyword:updateVue.keyword,
            theState:updateVue.theState,
            userStartId:updateVue.userStartId,
            userRecordId:updateVue.userRecordId,
            bldLimitAmountVerMngId:updateVue.tableId,
        }
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
	}

    function indexMethod(index) {
        return generalIndexMethod(index, updateVue)
    }

    function listItemSelectHandle(list) {
        generalListItemSelectHandle(updateVue,list)
    }

    //列表操作--------------“全选”按钮被点击
    function checkAllClicked()
    {
        if(updateVue.selectItem.length == updateVue.nodeVersionList.length)
        {
            updateVue.selectItem = [];
        }
        else
        {
            updateVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
            updateVue.nodeVersionList.forEach(function(item) {
                updateVue.selectItem.push(item.tableId);
            });
        }
    }

    function showModal()
	{
		approvalModalVue.getModalWorkflowList();
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	updateVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#sm_ApprovalProcess_BldLimitAmountVerDetailDiv",
	"detailInterface":"../Tgpj_BldLimitAmountVer_AFDetail",
	"updateInterface":"../Tgpj_BldLimitAmountVer_AFUpdate",
    "getNodeList":"../Tgpj_BldLimitAmountVer_AFDtlList",
});
