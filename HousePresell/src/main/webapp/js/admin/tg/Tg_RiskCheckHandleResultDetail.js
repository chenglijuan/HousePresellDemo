(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion : 19000101,
			tg_RiskCheckBusiCodeSum : {},
			tg_RiskRoutineCheckInfoList : [],
			spotTimeStr : null,
			theState : 0,
			bigBusiType : "",
			smallBusiType : "",
			pageNumber : 1,
			countPerPage : 20,
			selectItem : [],
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			listItemSelectHandle: listItemSelectHandle,
			indexMethod: function (index) {
				if (detailVue.pageNumber > 1) {
					return (detailVue.pageNumber - 1) * detailVue.countPerPage - 0 + (index - 0 + 1);
				}
				if (detailVue.pageNumber <= 1) {
					return index - 0 + 1;
				}
			},
			retractRiskCheckResult : retractRiskCheckResult,
			saveCheckHandleResult : saveCheckHandleResult,
			handleRiskCheckResult : handleRiskCheckResult,
			backRiskCheckResult : backRiskCheckResult,
			riskcheck : riskcheck,
		},
		computed:{
			 
		},
		components : {
			
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
			spotTimeStr:this.spotTimeStr,
			theState:this.theState,
			bigBusiType:this.bigBusiType,
			smallBusiType:this.smallBusiType,
			isDoPush:"1",
			isCheckResult:"1",
		}
	}

	function listItemSelectHandle(list) 
	{
		detailVue.selectItem = [];
	    for (var index = 0; index < list.length; index++) {
	    	detailVue.selectItem.push(list[index])
	    }
 	}
	
	function refresh()
	{
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
				detailVue.tg_RiskCheckBusiCodeSum = jsonObj.tg_RiskCheckBusiCodeSum;
				detailVue.tg_RiskRoutineCheckInfoList = jsonObj.tg_RiskRoutineCheckInfoList;
			}
		});
	}
	 //根据按钮权限，动态控制，该页面的所有“可控按钮”的显示与否
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}
	
	function initData()
	{
		initButtonList();
		getIdFormTab("",function (id) {
			var resultArr = id.split("、");
			detailVue.spotTimeStr = resultArr[resultArr.length-3];//抽查日期
            detailVue.bigBusiType = resultArr[resultArr.length-2];//业务大类
            detailVue.smallBusiType = resultArr[resultArr.length-1];//业务小类
            refresh();
        })
	}
	
	function saveCheckHandleResult()
	{
		var rishCheckList = detailVue.selectItem;
    	detailVue.tg_RiskRoutineCheckInfoList = [];
    	console.log(rishCheckList);
    	for(var i=0;i<rishCheckList.length;i++)
		{
    		detailVue.tg_RiskRoutineCheckInfoList.push({
    			tableId:rishCheckList[i].tableId,
    			modifyFeedback:rishCheckList[i].modifyFeedback,
    			forensicConfirmation : rishCheckList[i].forensicConfirmation,
    		})
		}
    	var model={
            interfaceVersion:detailVue.interfaceVersion,
            rishCheckList:detailVue.tg_RiskRoutineCheckInfoList,
            busiCodeSummaryId:detailVue.tg_RiskCheckBusiCodeSum.tableId,
        }
    	new ServerInterfaceJsonBody(baseInfo.saveInterface).execute(model, function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
            	detailVue.tg_RiskRoutineCheckInfoList = [];
            	detailVue.selectItem = [];
            	refresh();
                generalSuccessModal();
            }
        });
	}
	
	function handleRiskCheckResult()
	{
		var rishCheckList = detailVue.selectItem;
    	detailVue.tg_RiskRoutineCheckInfoList = [];
    	for(var i=0;i<rishCheckList.length;i++)
		{
    		detailVue.tg_RiskRoutineCheckInfoList.push({
    			tableId:rishCheckList[i].tableId,
    			forensicConfirmation:rishCheckList[i].forensicConfirmation,
    		})
		}
    	var model={
            interfaceVersion:detailVue.interfaceVersion,
            rishCheckList:detailVue.tg_RiskRoutineCheckInfoList,
            busiCodeSummaryId:detailVue.tg_RiskCheckBusiCodeSum.tableId,
        }
    	new ServerInterfaceJsonBody(baseInfo.handleInterface).execute(model, function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
                refresh();
            }
            else
            {
            	generalSuccessModal();
            	detailVue.tg_RiskRoutineCheckInfoList = [];
            	detailVue.selectItem = [];
            	refresh();
            }
        });
	}
	
	function retractRiskCheckResult()
    {
    	var rishCheckList = detailVue.selectItem;
    	detailVue.tg_RiskRoutineCheckInfoList = [];
    	for(var i=0;i<rishCheckList.length;i++)
		{
    		detailVue.tg_RiskRoutineCheckInfoList.push({
    			tableId:rishCheckList[i].tableId,
    		})
		}
    	var model={
            interfaceVersion:detailVue.interfaceVersion,
            rishCheckList:detailVue.tg_RiskRoutineCheckInfoList,
            busiCodeSummaryId:detailVue.tg_RiskCheckBusiCodeSum.tableId,
        }
    	new ServerInterfaceJsonBody(baseInfo.retractInterface).execute(model, function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
                refresh();
            }
            else
            {
            	generalSuccessModal();
            	detailVue.tg_RiskRoutineCheckInfoList = [];
            	detailVue.selectItem = [];
            	refresh();
            }
        });
    }
	
	function backRiskCheckResult()
    {
    	var rishCheckList = detailVue.selectItem;
    	detailVue.tg_RiskRoutineCheckInfoList = [];
    	for(var i=0;i<rishCheckList.length;i++)
		{
    		detailVue.tg_RiskRoutineCheckInfoList.push({
    			tableId:rishCheckList[i].tableId,
    		})
		}
    	var model={
            interfaceVersion:detailVue.interfaceVersion,
            rishCheckList:detailVue.tg_RiskRoutineCheckInfoList,
            busiCodeSummaryId:detailVue.tg_RiskCheckBusiCodeSum.tableId,
        }
    	new ServerInterfaceJsonBody(baseInfo.backInterface).execute(model, function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
                refresh();
            }
            else
            {
            	generalSuccessModal();
            	detailVue.tg_RiskRoutineCheckInfoList = [];
            	detailVue.selectItem = [];
            	refresh();
            }
        });
    }
	
	function riskcheck(tableId,busiType) {
		
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
		else if(busiType == '210104')//存单管理
		{
			enterNewTab(tableId, '存单存入详情', 'tg/Tg_DepositManagementDetail.shtml');
		}
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.initData();
	
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tg_RiskCheckHandleResultDetailDiv",
	"detailInterface":"../Tg_RiskCheckBusiCodeSumDetail",
	"retractInterface":"../Tg_RiskRoutineCheckInfoRetract",
	"backInterface":"../Tg_RiskRoutineCheckInfoBack",
	"saveInterface":"../Tg_RiskRoutineCheckInfoSave",
	"handleInterface":"../Tg_RiskRoutineCheckInfoHandle",
});
