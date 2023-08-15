(function(baseInfo) {
	var listDetailVue = new Vue({
		el: baseInfo.listDetaiDivId,
		data: {
			interfaceVersion: 19000101,
            pageNumber : 1,
            countPerPage : MAX_VALUE,
            totalPage : 1,
            totalCount : 1,
            selectItem : [],
			tableId:"" ,
            tgpf_FundOverallPlanModel:{}, //统筹单信息
            tgpf_overallPlanAccoutList:[] ,//托管账户信息
            fundOverallPlanDetailList:[],//用款申请汇总信息
            overallPlanDetail:[],
            tgpf_OverallPlanAccount:[] ,//统筹-账户状况信息
            tgpf_FundAppropriatedList:[],//拨付计划
            //----------审批流start-----------//
            afId:"",//申请单Id
            workflowId:"",//结点Id
            isNeedBackup:"",//是否需要备案
            sourcePage:"",//来源页面
            //----------审批流end-----------//
            sumCurrentBlance:0.0,
            loading:true,
		},
		methods: {
			refresh: refresh,
			initData: initData,
			getSearchForm: getSearchForm,
            groupBy:groupBy,
            getSummaries:getSummaries,
            showModal:showModal,//审批弹窗
            approvalHandle :approvalHandle ,//备案
            tgpf_FundOverallPlan_AFList:tgpf_FundOverallPlan_AFList,//用款计划
			changePageNumber: function(data) {
				listDetailVue.pageNumber = data;
			},
			tgpf_FundOverallPlan_AFListDetail : tgpf_FundOverallPlan_AFListDetail,//申请清单
		},
		computed: {

		},
		components: {
		},
		watch: {

		}
	});

	//------------------------方法定义-开始------------------//

	function tgpf_FundOverallPlan_AFListDetail()
	{
		//申请清单
        var tableId = listDetailVue.tableId;//统筹单Id
        enterNewTab(tableId,'申请清单','tgpf/Tgpf_FundOverallPlan_AFList.shtml');
	}
	
    function initData()
	{
        var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
        var array = tableIdStr.split("_");
        if (array.length > 1)
        {
            listDetailVue.tableId = array[array.length-4];
            listDetailVue.afId = array[array.length-3];
            listDetailVue.workflowId = array[array.length-2];
            listDetailVue.sourcePage = array[array.length-1];

            approvalModalVue.afId = listDetailVue.afId;
            approvalModalVue.workflowId = listDetailVue.workflowId;
            approvalModalVue.sourcePage = listDetailVue.sourcePage;
            refresh();
        }
    }

    // 详情操作--------------
    function getSearchForm()
    {
        return {
            interfaceVersion:listDetailVue.interfaceVersion,
            pageNumber:listDetailVue.pageNumber,
            countPerPage:listDetailVue.countPerPage,
            totalPage:listDetailVue.totalPage,
            fundOverallPlanId:listDetailVue.tableId,//统筹单Id
            afId : listDetailVue.afId,//申请单Id
        }
    }

    //详情操作--------------
    function refresh()
    {
        if (listDetailVue.tableId == null || listDetailVue.tableId < 1)
        {
            return;
        }

        getListDetail(); // 获取用款计划详情信息
    }

    //用款计划详情信息
    function getListDetail()
    {
        new ServerInterface(baseInfo.listDetailInterface).execute(listDetailVue.getSearchForm(), function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                listDetailVue.isNeedBackup = jsonObj.isNeedBackup;//是否需要备案
                approvalModalVue.isNeedBackup = listDetailVue.isNeedBackup;

                listDetailVue.tgpf_FundOverallPlanModel = jsonObj.tgpf_FundOverallPlan;  // 统筹主表
                listDetailVue.tgpf_overallPlanAccoutList = jsonObj.tgpf_overallPlanAccoutList; // 统筹账户状况
                listDetailVue.tgpf_FundAppropriatedList = jsonObj.tgpf_FundAppropriatedList; //拨付计划
                listDetailVue.overallPlanDetail =  jsonObj.fundOverallPlanDetailList; // 用款申请汇总信息
                listDetailVue.fundOverallPlanDetailList = groupBy(jsonObj.fundOverallPlanDetailList);  //用款申请汇总信息按照项目分组

                Vue.nextTick(function () {

                    var inputMoneyList = $("td").not(".is-hidden") .find("input.inputMoney2");

                    //同行：监管银行和托管银行是同一个银行的用绿色标识  //放款银行：入账数据对应的托管账号，有数据用黄色标记，只要有就可以，不需要一定是确认的数据

                    for (var i = 0 ; i< listDetailVue.tgpf_FundAppropriatedList.length ;i++)
                    {
                        var colorState = listDetailVue.tgpf_FundAppropriatedList[i].colorState;
                        for(var j = 0 ;j < inputMoneyList.length ;j++)
                        {
                            var bankAccountEscrowedId = inputMoneyList[j].getAttribute("data-bankAccountEscrowedId");
                            var bankAccountSupervisedId = inputMoneyList[j].getAttribute("data-bankAccountSupervisedId");
                            var fundAppropriated_AFId = inputMoneyList[j].getAttribute("data-fundAppropriated_AFId");

                            if(bankAccountEscrowedId == listDetailVue.tgpf_FundAppropriatedList[i].bankAccountEscrowedId
                                && bankAccountSupervisedId == listDetailVue.tgpf_FundAppropriatedList[i].bankAccountSupervisedId
                                && fundAppropriated_AFId == listDetailVue.tgpf_FundAppropriatedList[i].fundAppropriated_AFId)
                            {
                                $(inputMoneyList[j]).val(listDetailVue.tgpf_FundAppropriatedList[i].overallPlanPayoutAmount);
                                if(colorState == 1)
                                {
                                    $(inputMoneyList[j]).css("background","#93d150");
                                }
                                else if(colorState == 2)
                                {
                                    $(inputMoneyList[j]).css("background","#FFFF00");
                                }
                                break;
                            }
                        }
                    }
                    listDetailVue.loading = false;
                })
            }
        });
    }

    //按项目分组
    function groupBy(row)
    {
        var arr = row;
        var map = {};
        var dest = [];
        for(var i = 0; i < arr.length; i++)
        {
            var ai = arr[i];
            var tmp = {
                supervisedBankAccountId : ai.supervisedBankAccountId,
                theNameOfBankBranch : ai.theNameOfBankBranch,
                supervisedBankAccount : ai.supervisedBankAccount,
                appliedAmount:ai.appliedAmount,
                mainTableId : ai.mainTableId
            };
            if(!map[ai.theNameOfProject])
            {
                dest.push({
                    theNameOfProject: ai.theNameOfProject,
                    data: [
                       tmp
                    ]
                });
                map[ai.theNameOfProject] = tmp;
            }
            else
            {
                for(var j = 0; j < dest.length; j++)
                {
                    var dj = dest[j];
                    if(dj.theNameOfProject == ai.theNameOfProject)
                    {
                        dj.data.push(tmp);
                        break;
                    }
                }
            }
        }
        return dest;
    }

    //合计
    function getSummaries(param)
    {
        //param 是固定的对象，里面包含 columns与 data参数的对象 {columns: Array[4], data: Array[5]},包含了表格的所有的列与数据信息
        var columns = param.columns;
        var data = param.data;
        var sums = [];
        for (var index = 0; index < columns.length; index++)
        {
            if(index === 0)
            {
                sums[index] = '总计';
                continue;
            }
            sums[index] = "";
//            if(index == 3|| index == 5 || index == 11)
//            {
//                sums[index] = 0.0;
//                for (var i = 0; i < data.length; i++)
//                {
//                    var row = data[i];
//                    var value = 0.00;
//                    switch (index)
//                    {
//                        case 3 :
//                            value = row["newCurrentBalance"];
//                            break;
//                        case 5 :
//                            value = row["income"];
//                            break;
//                        case 11 :
//                            value = row["newOverallPlanAmount"];
//                            break;
//                    }
//                    if(!isNaN(value))
//                    {
//                        sums[index] += (value - 0);
//                    }
//                }
//                switch (index)
//                {
//                    case 3 :
//                        listDetailVue.sumCurrentBlance = sums[index];
//                        sums[index] = thousandsToTwoDecimal(sums[index]);
//                        break;
//                    case 5 :
//                        if(sums[index] == 0)
//                        {
//                            sums[index] = "0.00%";
//                        }
//                        else
//                        {
//                            sums[index] = toPercent(listDetailVue.sumCurrentBlance / sums[5]);
//                        }
//                        break;
//                    case 11 :
//                        sums[index] = thousandsToTwoDecimal(sums[index]);
//                        break;
//                }
//            }
            
            if(index == 2 || index == 5 )//|| index == 4 || index == 5 ||
            {
                sums[index] = 0.0;
                for (var i = 0; i < data.length; i++)
                {
                    var row = data[i];
                    var value = 0.00;
                    switch (index)
                    {
                        case 2 :
                            value = row["newCurrentBalance"];
                            break;
                        
                        case 5 :
                            value = row["newOverallPlanAmount"];
                            break;
                    }
                    if(!isNaN(value))
                    {
                        sums[index] += (value - 0);
                    }
                }
                switch (index)
                {
                    case 2 :
                        listDetailVue.sumCurrentBlance = sums[index];
                        sums[index] = thousandsToTwoDecimal(sums[index]);
                        break;
                    case 5 :
                        sums[index] = thousandsToTwoDecimal(sums[index]);
                        break;
                }
            }
            
        }
        return sums;
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

    //用款计划
    function tgpf_FundOverallPlan_AFList()
    {
        enterNewTab(listDetailVue.tableId ,'用款计划','tgpf/Tgpf_FundOverallPlanDetail.shtml');
    }

	//------------------------方法定义-结束------------------//

	//------------------------数据初始化-开始----------------//
	listDetailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDetaiDivId": "#sm_ApprovalProcess_Tgpf_FundOverallPlan",
	"listDetailInterface": "../Tgpf_FundOverallPlanListDetail", //资金统筹详情
});