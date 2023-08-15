(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tableId : 1,
			pageNumber : 1,
			countPerPage : MAX_VALUE,
			totalPage : 1,
			totalCount : 1,
			selectItem : [],
			isAllSelected : false,
            tgpf_FundAppropriated_AFModel:{},
            tgpf_fundAppropriated_afDtltab:[],
            tgpf_FundOverPlanDetailltab:[],
			//----------审批流start-----------//
            afId:"",//申请单Id
            //是否需要备案
            isNeedBackup:"",
			sourcePage:"",
            //----------审批流end-----------//
		},
		methods : {
			initData: initData,
            getSearchForm : getSearchForm,
            refresh : refresh,
            showModal:showModal,//审批弹窗
            approvalHandle :approvalHandle ,//备案
			
		},
		computed:{
			 
		},
		components : {

		},
		watch:{
			
		}
	});

	//------------------------方法定义-开始------------------//
    function initData()
    {
        var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
        var array = tableIdStr.split("_");
        if (array.length > 1)
        {
            detailVue.tableId = array[array.length-4];
            detailVue.afId = array[array.length-3];
            detailVue.workflowId = array[array.length-2];
            detailVue.sourcePage = array[array.length-1];

            approvalModalVue.afId = detailVue.afId;
            approvalModalVue.workflowId = detailVue.workflowId;
            approvalModalVue.sourcePage = detailVue.sourcePage;
            refresh();
        }
    }

	function getSearchForm()
	{
		return {
			interfaceVersion:detailVue.interfaceVersion,
			tableId:detailVue.tableId,
            afId : detailVue.afId,
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
	}

	function getDetail()
	{
		new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
                generalErrorModal(jsonObj);
			}
			else
			{
                detailVue.isNeedBackup = jsonObj.isNeedBackup;//是否需要备案
                approvalModalVue.isNeedBackup = detailVue.isNeedBackup;

				detailVue.tgpf_FundAppropriated_AFModel = jsonObj.tgpf_FundAppropriated_AF;
				detailVue.tgpf_fundAppropriated_afDtltab = jsonObj.tgpf_fundAppropriated_afDtlList;
                detailVue.tgpf_FundOverPlanDetailltab = jsonObj.tgpf_fundOverallPlanDetailList;
			}
		});
	}

    //审批按钮
    function showModal()
    {
        approvalModalVue.getModalWorkflowList();
    }

    //备案按钮
    function approvalHandle()
    {
        approvalModalVue.buttonType = 2;
        approvalModalVue.approvalHandle();
    }

	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#sm_ApprovalProcess_FundAppropriated_AF",
	"detailInterface":"../Tgpf_FundAppropriated_AFDetail"
});
