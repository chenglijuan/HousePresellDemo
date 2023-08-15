(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
            selectItem: [],
			tg_WorkTimeLimitCheckDetail:[],
            busiCode:0,
            dateStr:"",
            workTimeLimit:{},
            handleTimeLimitConfig:{},
            workTimeLimitDetailList:[
                // {
                //     eCode:"jc1001",
                //     applyDate:"2018-10-13",
                //     completeDate:"2018-10-18",
                //     days:"5",
                //     timeOutDays:"2",
                // }
            ],
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			workTimeDetail:workTimeDetail,
			getSearchForm : getSearchForm,
			indexMethod: indexMethod,
            changePageNumber : function(data){
                console.log(data);
                if (detailVue.pageNumber != data) {
                    detailVue.pageNumber = data;
                    detailVue.refresh();
                }
            },
            changeCountPerPage : function (data) {
                if (detailVue.countPerPage != data) {
                    detailVue.countPerPage = data;
                    detailVue.refresh();
                }
            },
            exportExcel:exportExcel,
            getExportExcelForm:getExportExcelForm,
		},
		computed:{
			 
		},
		components : {
            'vue-nav' : PageNavigationVue
		},
		watch:{
			
		}
	});

	//------------------------方法定义-开始------------------//
	function indexMethod(index)
	{
		return generalIndexMethod(index, detailVue);
	}
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
            busiCode:this.busiCode,
            dateStr:this.dateStr,
            pageNumber:this.pageNumber,
            countPerPage:this.countPerPage,
            totalPage:this.totalPage,
		}
	}
    function getExportExcelForm()
    {
        return {
            interfaceVersion:this.interfaceVersion,
            busiCode:this.busiCode,
            dateStr:this.dateStr,
        }
    }

	//详情操作--------------
	function refresh()
	{
		getDetail();
	}

	function exportExcel()
    {
        new ServerInterface(baseInfo.detailExportExcelInterface).execute(detailVue.getExportExcelForm(), function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
                noty({"text":jsonObj.info,"type":"error","timeout":2000});
            }
            else
            {
                window.location.href=jsonObj.fileDownloadPath;
            }
        });
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
				detailVue.workTimeLimitDetailList = jsonObj.workTimeLimitDetailList;
                detailVue.workTimeLimitDetailList.forEach(function(item){
                    if (item.days != null && item.days.length > 0) {
                        item.days =  addThousands(item.days);
                    }
                    if (item.timeOutDays != null && item.timeOutDays.length > 0) {
                        item.timeOutDays =  addThousands(item.timeOutDays);
                    }
                })
				detailVue.workTimeLimit = jsonObj.workTimeLimit;
                detailVue.handleTimeLimitConfig = jsonObj.handleTimeLimitConfig;
                detailVue.pageNumber=jsonObj.pageNumber;
                detailVue.countPerPage=jsonObj.countPerPage;
                detailVue.totalPage=jsonObj.totalPage;
                detailVue.totalCount = jsonObj.totalCount;
                detailVue.selectItem = [];
			}
		});
	}
	
	function initData()
	{
        getIdFormTab('', function (id) {
            var a = id.split("--");
            detailVue.busiCode = a[0];
            detailVue.dateStr = a[1].replace("date"," - ");
            //初始化
            refresh();
        });
	}
	
	function workTimeDetail(tableId,busiType) {
		
		console.log(busiType);
		if( busiType == '06110201')//合作协议
		{
			enterNextTab(tableId, '贷款托管合作协议详情', 'tgxy/Tgxy_EscrowAgreementDetail.shtml',tableId+"06110201");
		}
		else if(busiType == '06110301')//三方协议
		{
			enterNextTab(tableId, '贷款托管三方协议详情', 'tgxy/Tgxy_TripleAgreementDetail.shtml',tableId+"06110301");
		}
		else if(busiType == '03030101')//受限额度变更
		{
			enterNewTab(tableId, "受限额度变更详情", "empj/Empj_BldLimitAmount_AFDetail.shtml")
		}
		else if(busiType == '03030100')//工程进度节点更新
		{
			enterNewTab(tableId, "工程进度节点更新审批详情", "sm/Sm_BldLimitAmountDetail.shtml")
		}
		else if(busiType == '03030102')//托管终止
		{
			enterNewTab(tableId, "托管终止详情", 'empj/Empj_BldEscrowCompletedDetail.shtml');
		}
		else if(busiType == '06120301')//用款申请
		{
//			var theId = tableId +'_'+projectId;
	        enterNewTab(tableId, '用款申请详情', 'tgpf/Tgpf_FundAppropriated_AFDetail.shtml');
		}
		else if(busiType == '06120302')//统筹详情
		{
			enterNewTab(tableId,'资金统筹详情','tgpf/Tgpf_FundOverallPlanListDetail.shtml');
		}
		else if(busiType == '06120303')//拨付
		{
			enterNewTab(tableId, "资金拨付详情", "tgpf/Tgpf_FundAppropriatedDetail.shtml");
		}
		
		else if(busiType == '06120202')//贷款未结清详情
		{
			enterNextTab(tableId, '退房退款信息-贷款未结清详情', 'tgpf/Tgpf_RefundInfoUnclearedDetail.shtml',tableId+"06120202");
		}
		else if(busiType == '06120201')//退房退款
		{
			enterNextTab(tableId, '退房退款信息详情', 'tgpf/Tgpf_RefundInfoDetail.shtml',tableId+"06120201");
		}
		else if(busiType == '06120401')//支付保证
		{
			enterNextTab(tableId, '支付保证申请详情', 'empj/Empj_PaymentGuaranteeApplicationDetail.shtml',tableId+"06120401");
		}
		
	}
	
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tg_WorkTimeLimitCheckDetailDiv",
    "detailInterface":"../Tg_WorkTimeLimitCheckDetail",
    "detailExportExcelInterface":"../Tg_WorkTimeLimitCheckDetailExportExcel",
});
