(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tableId : 1,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			selectItem : [],
			isAllSelected : false,
            tgpf_FundAppropriated_AFModel:{},
            tgpf_fundAppropriated_afDtltab:[],
            tgpf_FundOverPlanDetailltab:[],
            disabled:true,
            isPrint : false,
            flag : false,
		},
		methods : {
			initData: initData,
            getSearchForm : getSearchForm,
            refresh : refresh,
            tgpf_FundAppropriated_AFEdit:tgpf_FundAppropriated_AFEdit,//修改
            getExportForm :getExportForm,//导出参数
            exportPdf:exportPdf,//导出pdf
            goToSPHandle : goToSPHandle,//提交
            tgpf_paymentBondDetail : tgpf_paymentBondDetail,
		},
		computed:{
			 
		},
		components : {

		},
		watch:{
			
		}
	});

	//------------------------方法定义-开始------------------//
	function goToSPHandle()
	{
		detailVue.disabled = true;
		detailVue.flag = false;
		new ServerInterface(baseInfo.submitInterface).execute(detailVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
                generalErrorModal(jsonObj);
			}
			else
			{
				generalSuccessModal();
			}
			
			getDetail();
		});
	}
	
    function initData()
    {
        var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
        var array = tableIdStr.split("_");
        if (array.length > 1)
        {
            detailVue.tableId = array[3]; //用款申请主表id
            detailVue.projectId = array[4];
            refresh();
        }
    }

    //列表操作-----------------------导出PDF
    function getExportForm() {
		var href = window.location.href;
		return {
			interfaceVersion : this.interfaceVersion,
			sourceId : detailVue.tableId,
			reqAddress : href,
			sourceBusiCode : "06120301",
		}
	}
    
	function exportPdf(){
		new ServerInterface(baseInfo.exportInterface).execute(detailVue.getExportForm(), function(jsonObj){
			if (jsonObj.result != "success") {
				$(baseInfo.edModelDivId).modal({
					backdrop : 'static'
				});
				detailVue.errMsg = jsonObj.info;
			} else {
				window.open(jsonObj.pdfUrl,"_blank");
			}
		});
	}

	//详情操作
	function getSearchForm()
	{
		return {
			interfaceVersion:detailVue.interfaceVersion,
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
				detailVue.tgpf_FundAppropriated_AFModel = jsonObj.tgpf_FundAppropriated_AF;
				if(detailVue.tgpf_FundAppropriated_AFModel.approvalState == "待提交")
				{
					detailVue.disabled = false;
					detailVue.flag = true;
				}
				else
				{
					detailVue.disabled = true;
					detailVue.flag = false;
				}
				
				//已拨付状态，才可以打印
				if(detailVue.tgpf_FundAppropriated_AFModel.applyState == 6)
				{
					detailVue.isPrint = true;
				}
				
				detailVue.tgpf_fundAppropriated_afDtltab = jsonObj.tgpf_fundAppropriated_afDtlList;
                detailVue.tgpf_FundOverPlanDetailltab = jsonObj.tgpf_fundOverallPlanDetailList;
			}
		});
	}
	
	function tgpf_paymentBondDetail(id){
		enterNextTab(id, '支付保函申请详情', 'empj/Empj_PaymentBondDetail.shtml',id+"06120401");
	}
	//跳转方法 - 修改页面
	function tgpf_FundAppropriated_AFEdit()
	{
        var theId = detailVue.tableId +"_"+ detailVue.projectId;
        enterNewTabCloseCurrent(theId,"用款申请修改",'tgpf/Tgpf_FundAppropriated_AFEdit.shtml');
	}

	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tgpf_FundAppropriated_AFDetailDiv",
	"detailInterface":"../Tgpf_FundAppropriated_AFDetail",
	"exportInterface":"../exportPDFByWord",//导出pdf
	"submitInterface":"../Tgpf_FundAppropriated_AFApprovalProcess" ,//提交
});
