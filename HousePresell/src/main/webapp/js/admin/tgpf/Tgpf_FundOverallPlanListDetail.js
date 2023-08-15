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
            sumCurrentBlance:0.0,
            disabled:true,
            loading:true,
            sumsInputMoney : [],
		},
		methods: {
			refresh: refresh,
			initData: initData,
			getSearchForm: getSearchForm,
            getSummaries:getSummaries,
            tgpf_FundOverallPlanEdit:tgpf_FundOverallPlanEdit,
            tgxy_BankAccountEscrowedDetail:tgxy_BankAccountEscrowedDetail,//托管账户详情
            groupBy:groupBy,
			changePageNumber: function(data) {
				listDetailVue.pageNumber = data;
			},
		},
		computed: {

		},
		components: {
		},
		watch: {

		}
	});

	//------------------------方法定义-开始------------------//

    function initData()
	{
        getIdFormTab("",function (tableId) {
            listDetailVue.tableId=tableId;
            refresh();
        })
    }

    // 详情操作--------------
    function getSearchForm()
    {
        return {
            interfaceVersion:listDetailVue.interfaceVersion,
            pageNumber:listDetailVue.pageNumber,
            countPerPage:listDetailVue.countPerPage,
            totalPage:listDetailVue.totalPage,
            fundOverallPlanId:listDetailVue.tableId
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
            	console.log(jsonObj)
                listDetailVue.tgpf_FundOverallPlanModel = jsonObj.tgpf_FundOverallPlan;  // 统筹主表
                if(listDetailVue.tgpf_FundOverallPlanModel.approvalState == "待提交")
                {
                    listDetailVue.disabled = false;
                }
                else
                {
                    listDetailVue.disabled = true;
                }
                listDetailVue.tgpf_overallPlanAccoutList = jsonObj.tgpf_overallPlanAccoutList; // 统筹账户状况
                listDetailVue.tgpf_overallPlanAccoutList.forEach(function(item){
                	item.newCanPayAmount = Number(commafyback(item.canPayAmount));
                })
                listDetailVue.tgpf_FundAppropriatedList = jsonObj.tgpf_FundAppropriatedList; //拨付计划
                listDetailVue.overallPlanDetail =  jsonObj.fundOverallPlanDetailList; // 用款申请汇总信息
                listDetailVue.fundOverallPlanDetailList = groupBy(jsonObj.fundOverallPlanDetailList);  //用款申请汇总信息按照项目分组
                Vue.nextTick(function () {

                    var inputMoneyList = $("td").not(".is-hidden") .find("input.inputMoney");
                    
                    //同行：监管银行和托管银行是同一个银行的用绿色标识  //放款银行：入账数据对应的托管账号，有数据用黄色标记，只要有就可以，不需要一定是确认的数据
                    for (var i = 0 ; i< listDetailVue.tgpf_FundAppropriatedList.length ;i++)
                    {
                        var colorState = listDetailVue.tgpf_FundAppropriatedList[i].colorState;
                        var inputMoneryArr = []
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
                    var abc = [];
                    for(var i = 0; i<inputMoneyList.length;i++){
                    	abc.push( $(inputMoneyList[i]).val())
    	            }
                    var sumLength = listDetailVue.overallPlanDetail;
                    var newArr = [];
                    var groupArr = againGroup(abc,listDetailVue.overallPlanDetail.length);
                    var k = 0;
                    for(var j = 0;j<listDetailVue.overallPlanDetail.length;j++){
                    	if(k !=listDetailVue.overallPlanDetail.length){
                        	for(var i = 0;i<groupArr.length;i++){ 
                        		if(groupArr[i][k] ==undefined || groupArr[i][k] == ""){
                        			newArr.push(0);
                        		}else{
                        			newArr.push(groupArr[i][k]);
                        		}
                            	
                            }
                        	k++;
                        }
                    }
                    
                    listDetailVue.sumsInputMoney = newArr;
                    
                    //var groupSum = againGroup(listDetailVue.sumsInputMoney,listDetailVue.tgpf_overallPlanAccoutList.length);//数组就按照listDetailVue.tgpf_overallPlanAccoutList.length个进行分组了
              
                    
                    listDetailVue.loading = false;
                })
            }
        });
    }
    
    
    
    
    
    function againGroup (data,num){
	　　var result=[];

	　　for(var i=0,len=data.length;i<len;i+=num){

	　　　　result.push(data.slice(i,i+num));

	　　}

	　　return result;

	}

    	
    
    //合计
    function getSummaries(param)
    {        //param 是固定的对象，里面包含 columns与 data参数的对象 {columns: Array[4], data: Array[5]},包含了表格的所有的列与数据信息
        var columns = param.columns;
        
        var data = param.data;
        var sums = [];
        
        for (var index = 0; index < columns.length; index++)
        {
        	//console.log(listDetailVue.sumsInputMoney)
	        
        	var groupSum = againGroup(listDetailVue.sumsInputMoney,data.length);//数组就按照data.length个进行分组了
            var sumArr = [0,0,0,0,0,0,0];
         	for(var l = 0;l<groupSum.length;l++){
         		var groupSumG = groupSum[l];
         		var nums = 0;
         		for(var j = 0;j<groupSumG.length;j++){
         			
             		nums += (commafyback(groupSumG[j])-0);
         		}
         		sumArr.push(nums);
         		
         	}
            if(index === 0)
            {
                sums[index] = '总计';
                continue;
            }
            sums[index] = "";
            if(index == 2 || index == 6 )//|| index == 4 || index == 5 ||
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
                        
                        case 6 :
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
                    case 6 :
                        sums[index] = thousandsToTwoDecimal(sums[index]);
                        break;
                 
                }
            }
            
          if(index >6){
            	sums[index] = 0.0;
                for (var i = 0; i < data.length; i++)
                {
                    var row = data[i];
                    var value = 0.00;
                    switch (index)
                    {
                    
                        case index : 
                        	value = row["newInputMoney"+index];
                        	break;
                    }
                    if(!isNaN(value))
                    {
                        sums[index] += (value - 0);
                    }
                }
               
                switch (index)
                {
                 
                    case index : 
                    	sums[index] = thousandsToTwoDecimal(sumArr[index]);
	                	 break;
                }
            }
            
            
        }
        return sums;
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

    //托管账户详情
    function tgxy_BankAccountEscrowedDetail(tableId)
    {
        enterNewTab(tableId, "托管账户详情", "tgxy/Tgxy_BankAccountEscrowedDetail.shtml");
    }

    //跳转方法 - 修改页面
    function tgpf_FundOverallPlanEdit()
    {
        enterNewTabCloseCurrent(listDetailVue.tableId,"资金统筹修改",'tgpf/Tgpf_FundOverallPlanEdit.shtml');
    }

	//------------------------方法定义-结束------------------//

	//------------------------数据初始化-开始----------------//
	listDetailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDetaiDivId": "#tgpf_FundOverallPlanListDetailDiv",
	"listDetailInterface": "../Tgpf_FundOverallPlanListDetail", //资金统筹详情
});