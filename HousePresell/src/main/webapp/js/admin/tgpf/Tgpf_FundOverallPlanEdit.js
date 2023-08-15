(function(baseInfo) {
    var editVue = new Vue({
        el: baseInfo.editDivId,
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
            buttonType:"" , // 1: 保存按钮  2. 提交按钮
            disabled:true,
            isFirstFundAppropriated:true, //是否是第一次统筹
            addDisabled:false,
            fundOverallPlanDate:"",//统筹日期
            isCorrect:true,
            sumCurrentBlance:0.0,
            loading:true,
            sumsInputMoney : [],
            showSubFlag : true,
        },
        methods: {
            refresh: refresh,
            initData: initData,
            getAddForm:getAddForm,
            getSearchForm: getSearchForm,
            tgxy_BankAccountEscrowedDetail:tgxy_BankAccountEscrowedDetail,//托管账户详情
            updateTgpf_FundOverallPlan:updateTgpf_FundOverallPlan,//1:保存  , 2:提交
            groupBy:groupBy,
            getSummaries:getSummaries,
            inputMoneyFocus:inputMoneyFocus,
            inputmoneyBlur:inputmoneyBlur,
            adjustAmount:adjustAmount,
            getFundAppropriatedList:getFundAppropriatedList,
            getOverallPlanAccoutList:getOverallPlanAccoutList,
            isCorrect:isCorrect,
            accountAmountTrimFocus:accountAmountTrimFocus,
            accountAmountTrimBlur:accountAmountTrimBlur,
            generateSummary:generateSummary,
            changePageNumber: function(data) {
                editVue.pageNumber = data;
            },
        },
        computed: {

        },
        components: {
        },
        watch: {
        },
        mounted: function ()
        {
            laydate.render({
                elem: '#date061203020201'
            })
        }
    });

    //------------------------方法定义-开始------------------//

    function initData()
    {
        getIdFormTab("",function (tableId) {
            editVue.tableId=tableId;
            refresh();
        })
    }

    // 详情操作--------------
    function getSearchForm()
    {
        return {
            interfaceVersion:editVue.interfaceVersion,
            pageNumber:editVue.pageNumber,
            countPerPage:editVue.countPerPage,
            totalPage:editVue.totalPage,
            fundOverallPlanId:editVue.tableId
        }
    }

    //详情操作--------------
    function refresh()
    {
        if (editVue.tableId == null || editVue.tableId < 1)
        {
            return;
        }
        getListDetail(); // 获取资金统筹详情
    }

    //资金统筹详情
    function getListDetail()
    {
        new ServerInterface(baseInfo.listDetailInterface).execute(editVue.getSearchForm(), function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                editVue.isFirstFundAppropriated = jsonObj.isFirstFundAppropriated; //是否是初次统筹
                editVue.tgpf_FundOverallPlanModel = jsonObj.tgpf_FundOverallPlan;  // 统筹主表
                if(editVue.tgpf_FundOverallPlanModel.approvalState == "待提交")
                {
                    editVue.addDisabled = false;
                }
                else
                {
                    editVue.addDisabled = true;
                }
                editVue.tgpf_overallPlanAccoutList = jsonObj.tgpf_overallPlanAccoutList; // 统筹账户状况
                editVue.tgpf_FundAppropriatedList = jsonObj.tgpf_FundAppropriatedList; //资金拨付
                editVue.overallPlanDetail =  jsonObj.fundOverallPlanDetailList; // 用款申请汇总信息
                editVue.fundOverallPlanDetailList = groupBy(jsonObj.fundOverallPlanDetailList);  //用款申请汇总信息按照项目分组

                //同行：监管银行和托管银行是同一个银行的用绿色标识  //放款银行：入账数据对应的托管账号，有数据用黄色标记，只要有就可以，不需要一定是确认的数据
                Vue.nextTick(function () {

                    var inputMoneyList = $("td").not(".is-hidden") .find("input.inputMoneyEdit");
                    for (var i = 0 ; i< editVue.tgpf_FundAppropriatedList.length ;i++)
                    {
                        var colorState = editVue.tgpf_FundAppropriatedList[i].colorState;
                        if(colorState == undefined)
                        {
                            colorState = null;
                        }
                        for(var j = 0 ;j < inputMoneyList.length ;j++)
                        {
                            var bankAccountEscrowedId = inputMoneyList[j].getAttribute("data-bankAccountEscrowedId");
                            var bankAccountSupervisedId = inputMoneyList[j].getAttribute("data-bankAccountSupervisedId");
                            var fundAppropriated_AFId = inputMoneyList[j].getAttribute("data-fundAppropriated_AFId");

                            if(bankAccountEscrowedId == editVue.tgpf_FundAppropriatedList[i].bankAccountEscrowedId
                                && bankAccountSupervisedId == editVue.tgpf_FundAppropriatedList[i].bankAccountSupervisedId
                                && fundAppropriated_AFId == editVue.tgpf_FundAppropriatedList[i].fundAppropriated_AFId)
                            {
                                $(inputMoneyList[j]).val(editVue.tgpf_FundAppropriatedList[i].overallPlanPayoutAmount);
                                if(colorState == 1)
                                {
                                    $(inputMoneyList[j]).css("background","#93d150");
                                }
                                else if(colorState == 2)
                                {
                                    $(inputMoneyList[j]).css("background","#FFFF00");
                                }
                                inputMoneyList[i].setAttribute("data-colorState",colorState);
                                break;
                            }
                        }
                    }
                    
                  /*  var indexOverPlan = 5;
                    for(var i =0;i<editVue.overallPlanDetail.length;i++){
                    	for(var j = 0;j<editVue.tgpf_overallPlanAccoutList.length;j++){
                    		editVue.tgpf_overallPlanAccoutList[j].newInputMoney5 = 0;
                    	}
                    	indexOverPlan++;
                    }
                    console.log(editVue.tgpf_overallPlanAccoutList)*/
                    
                    
                    groupSumMoney(inputMoneyList);
                    //不是首次统筹（已经保存过）
                    if(editVue.isFirstFundAppropriated == null || editVue.isFirstFundAppropriated == false)
                    {
                    	var fundOverallPlanDate = editVue.tgpf_FundOverallPlanModel.fundOverallPlanDate;
                    	if(fundOverallPlanDate == null || fundOverallPlanDate.length == 0)
                    	{
                    		//统筹日期 默认显示 当天系统时间
                    		editVue.fundOverallPlanDate = jsonObj.tgpf_FundOverallPlan.nowDateStr;
                    		$('#date061203020201').val(jsonObj.tgpf_FundOverallPlan.nowDateStr);
                    	}
                    	else 
                    	{
                    		editVue.fundOverallPlanDate = editVue.tgpf_FundOverallPlanModel.fundOverallPlanDate;
                    		$('#date061203020201').val(editVue.tgpf_FundOverallPlanModel.fundOverallPlanDate);
                    	}
                    }
                    else
                    {
                    	//统筹日期 默认显示 当天系统时间
                    	editVue.fundOverallPlanDate = jsonObj.tgpf_FundOverallPlan.nowDateStr;
                    	$('#date061203020201').val(jsonObj.tgpf_FundOverallPlan.nowDateStr);
                    }
                    editVue.loading = false;
                 
                })
            }
        });
    }
    
    function groupSumMoney(inputMoneyList){
    	  var abc = [];
          for(var i = 0; i<inputMoneyList.length;i++){
          	abc.push($(inputMoneyList[i]).val())
          }
          var sumLength = editVue.overallPlanDetail;
          var newArr = [];
          var groupArr = againGroup(abc,editVue.overallPlanDetail.length);
          var k = 0;
          for(var j = 0;j<editVue.overallPlanDetail.length;j++){
          	if(k !=editVue.overallPlanDetail.length){
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
          
          editVue.sumsInputMoney = newArr;
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
    
    function againGroup (data,num){
  	　　var result=[];

  	　　for(var i=0,len=data.length;i<len;i+=num){

  	　　　　result.push(data.slice(i,i+num));

  	　　}

  	　　return result;

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
        	var groupSum = againGroup(editVue.sumsInputMoney,data.length);//数组就按照data.length个进行分组了
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
            if(index == 2 || index == 6)
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
                        editVue.sumCurrentBlance = sums[index];
                        sums[index] = thousandsToTwoDecimal(sums[index]);
                        break;
                    
                    case 6 :
                        sums[index] = thousandsToTwoDecimal(sums[index]);
                        break;
                }
            }
            
            if(index >6){
            	sums[index] = 0.0;
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

    //输入框获取焦点
    function inputMoneyFocus(ev)
    {
    	
        if((commafyback(ev.srcElement.value) - 0) > 0)
        {
            ev.srcElement.value = (commafyback(ev.srcElement.value) - 0 );
        }
        else
        {
            ev.srcElement.value = "";
        }
        
    }

    //输入框失去焦点
    function inputmoneyBlur(ev)
    {
        if((commafyback(ev.srcElement.value) - 0) > 0)
        {
            ev.srcElement.value = thousandsToTwoDecimal(ev.srcElement.value)
        }
        else
        {
            ev.srcElement.value = "";
        }
        var inputMoneyList = $("td").not(".is-hidden") .find("input.inputMoneyEdit");
        groupSumMoney(inputMoneyList);
        
    }

    //托管账户详情
    function tgxy_BankAccountEscrowedDetail(tableId)
    {
        var theId = 'bankAccountEscrowedDetail_' + tableId;
        $("#tabContainer").data("tabs").addTab({id: theId , text: '托管账户详情', closeable: true, url: 'tgxy/Tgxy_BankAccountEscrowedDetail.shtml'});
    }


    //获取所有input中内容 - 新增拨付信息
    function  getFundAppropriatedList()
    {
        editVue.tgpf_FundAppropriatedList = [];
        var inputMoneyList = $("td").not(".is-hidden") .find("input.inputMoneyEdit");
        for(var i=0 ; i < inputMoneyList.length ;i++)
        {
            editVue.tgpf_FundAppropriatedList.push(
                {
                    bankAccountEscrowedId : inputMoneyList[i].getAttribute("data-bankAccountEscrowedId"), //托管账户id
                    bankAccountSupervisedId : inputMoneyList[i].getAttribute("data-bankAccountSupervisedId"), // 监管账户id
                    fundAppropriated_AFId : inputMoneyList[i].getAttribute("data-fundAppropriated_AFId"), // 用款申请主表id
                    overallPlanPayoutAmount : commafyback(inputMoneyList[i].value) - 0 , // 统筹拨付金额
                    canPayAmount : commafyback(inputMoneyList[i].getAttribute("data-canPayAmount")) - 0,
                    colorState:inputMoneyList[i].getAttribute("data-colorState"),
                }
            );
        }
    }

    //统筹 - 账户信息
    function getOverallPlanAccoutList()
    {
        var accountList = editVue.tgpf_overallPlanAccoutList;

        for (var i = 0; i < accountList.length; i++)
        {
            editVue.tgpf_OverallPlanAccount.push({
                tableId: accountList[i].tableId,
                overallPlanAmount: commafyback(accountList[i].overallPlanAmount)-0,
                accountAmountTrim:commafyback(accountList[i].accountAmountTrim)-0,
            })
        }
    }

    //当天入账金额调整项 输入框获取焦点将千分位转为字符串
    function accountAmountTrimFocus(ev,index)
    {
        editVue.tgpf_overallPlanAccoutList[index].accountAmountTrim = (commafyback(editVue.tgpf_overallPlanAccoutList[index].accountAmountTrim) - 0 );
    }

    //当天入账金额调整项 输入框获取失去焦点将金额转换成千分位
    function accountAmountTrimBlur(ev,index)
    {
        editVue.tgpf_overallPlanAccoutList[index].accountAmountTrim = thousandsToTwoDecimal(editVue.tgpf_overallPlanAccoutList[index].accountAmountTrim);
    }

    function adjustAmount(ev)
    {
        if(isNaN(ev.srcElement.value))
        {
            generalErrorModal("","必须输入大于0的金额");
        }
        else
        {
            for (var i = 0; i < editVue.tgpf_overallPlanAccoutList.length; i++)
            {
                var accountAmountTrim = (commafyback(editVue.tgpf_overallPlanAccoutList[i].accountAmountTrim) - 0);
                if(accountAmountTrim > 0)
                {
                    editVue.tgpf_overallPlanAccoutList[i].canPayAmount =(editVue.tgpf_overallPlanAccoutList[i].temporaryAmount - 0) + accountAmountTrim;
                    editVue.tgpf_overallPlanAccoutList[i].canPayAmount = thousandsToTwoDecimal(editVue.tgpf_overallPlanAccoutList[i].canPayAmount);
                }
            }
        }
    }

    function generateSummary(ev)
    {
        for (var i = 0; i < editVue.tgpf_overallPlanAccoutList.length; i++)
        {
            editVue.tgpf_overallPlanAccoutList[i].overallPlanAmount = 0.00;
        }

        ev.srcElement.value = commafyback(ev.srcElement.value);

        if(isNaN(ev.srcElement.value))
        {
            ev.srcElement.value = "";
            generalErrorModal("","必须输入大于0的金额");
        }
        else
        {
            var inputMoneyList = $("td").not(".is-hidden") .find("input.inputMoneyEdit");
            
            var overallPlanAmountSummary  = 0 ;
            for (var i = 0; i < editVue.tgpf_overallPlanAccoutList.length; i++)
            {
                for (var j = 0; j < inputMoneyList.length; j++)
                {
                    if(inputMoneyList[j].getAttribute("data-bankAccountEscrowedId") == editVue.tgpf_overallPlanAccoutList[i].bankAccountEscrowId)
                    {
                        editVue.tgpf_overallPlanAccoutList[i].overallPlanAmount +=  (commafyback(inputMoneyList[j].value) - 0);
                    }
                }
                overallPlanAmountSummary += (editVue.tgpf_overallPlanAccoutList[i].overallPlanAmount - 0);
            }
            
            //不知道这行代码的意义在哪里，由于现在最后一行合计受到影响，暂时注释   by:姚建平
            $(".el-table__footer tbody tr:nth-child(1) td:nth-child(7)").html(thousandsToTwoDecimal(overallPlanAmountSummary));
            
            for(var index = 0; index < editVue.tgpf_overallPlanAccoutList.length; index++)
            {
           	 editVue.tgpf_overallPlanAccoutList[index].overallPlanAmount = thousandsToTwoDecimal(editVue.tgpf_overallPlanAccoutList[index].overallPlanAmount);
           	}
            
           
        }

    }

    //输入校验
    function isCorrect()
    {
        var inputMoneyList = $("td").not(".is-hidden") .find("input.inputMoneyEdit");
        //每一列输入金额 == 申请金额
        for (var i = 0; i < editVue.overallPlanDetail.length; i++)
        {
            editVue.isCorrect = true;
            var inputTotalMoney = 0;
            for (var j = 0; j < inputMoneyList.length; j++)
            {
            	
                if(inputMoneyList[j].getAttribute("data-bankAccountSupervisedId") == editVue.overallPlanDetail[i].supervisedBankAccountId
                  && inputMoneyList[j].getAttribute("data-fundAppropriated_AFId") == editVue.overallPlanDetail[i].mainTableId)
                {
                    inputTotalMoney += (commafyback(inputMoneyList[j].value) - 0);
                }
                
            }
            
            console.log(Math.abs((commafyback(editVue.overallPlanDetail[i].appliedAmount) - 0 ) - inputTotalMoney).toFixed(2));
            if(Math.abs((commafyback(editVue.overallPlanDetail[i].appliedAmount) - 0 ) - inputTotalMoney).toFixed(2) >= 0.01)
            {
            	
            	console.log("统筹拨付金额必须等于申请拨付金额，请确认！"+commafyback(editVue.overallPlanDetail[i].appliedAmount) +'--->'+inputTotalMoney);
                generalErrorModal("","统筹拨付金额必须等于申请拨付金额，请确认！");
                editVue.isCorrect = false;
                return ;
            }
        }
        // 每一行输入金额  ==  拨付合计
        // 每一行输入金额 < = 托管可拨付金额

        for (var i = 0; i < editVue.tgpf_overallPlanAccoutList.length; i++)
        {
            var payAmount = editVue.tgpf_overallPlanAccoutList[i];
            
            var canPayAmount = commafyback(payAmount.canPayAmount)-0;
            
            var overallPlanAmount = commafyback(payAmount.overallPlanAmount)-0;
            var currentBalance = commafyback(payAmount.currentBalance)-0;
            
            /*if(overallPlanAmount > 0 && overallPlanAmount > canPayAmount)
            {
            	//console.log("申请拨付金额输不得超出托管可划拨金额，请确认！"+overallPlanAmount +'--->'+canPayAmount);
                generalErrorModal("","托管余额不足，无法进行统筹！");
                editVue.isCorrect = false;
                return;
            }*/
            //上一天网银入账金额
            /*var transactionAmount = (payAmount.transactionAmount)-0;
            console.log("transactionAmount="+transactionAmount+";currentBalance="+currentBalance+";overallPlanAmount="+overallPlanAmount);
            if(overallPlanAmount > 0 && overallPlanAmount > (canPayAmount + transactionAmount))
            {
                generalErrorModal("","托管余额不足，无法进行统筹！");
                editVue.isCorrect = false;
                return;
            }*/
            
            
            
        }
    }

    //新增操作--------------
    function getAddForm()
    {
        return {
            interfaceVersion:editVue.interfaceVersion,
            buttonType:editVue.buttonType, //按钮类型
            isFirstFundAppropriated:editVue.isFirstFundAppropriated,//是否是初次统筹
            tableId:editVue.tableId, //资金统筹id
            fundOverallPlanDate:$('#date061203020201').val(),//统筹日期
            tgpf_OverallPlanAccountList: editVue.tgpf_OverallPlanAccount, // 统筹账户列表
            tgpf_FundAppropriatedList:editVue.tgpf_FundAppropriatedList, // 资金拨付列表
        }
    }


    //新增操作--------------
    function updateTgpf_FundOverallPlan(buttonType)
    {
        getFundAppropriatedList(); //拨付信息
        getOverallPlanAccoutList();//统筹账户信息
        editVue.buttonType = buttonType;
        if (buttonType == 1)
        {
        	
        	isCorrect();
            new ServerInterfaceJsonBody(baseInfo.updateInterface).execute(editVue.getAddForm(), function (jsonObj) {
            	editVue.showSubFlag = true;
                if (jsonObj.result != "success")
                {
                    generalErrorModal(jsonObj);
                }
                else
                {
                	if(jsonObj.info.includes("开户行")){
                		//存在资金系统不支持的开户行，请在系统内完成拨付流程
                		generalErrorModal(jsonObj);
                	}else{
                		generalSuccessModal();
                	}
                    enterNewTabCloseCurrent(editVue.tableId, '资金统筹详情', 'tgpf/Tgpf_FundOverallPlanListDetail.shtml');
                }
            });
        }
       if (buttonType == 2)
        {
            //输入校验  ，保存按钮不校验，提交按钮校验输入正确性
            editVue.showSubFlag = false;
            isCorrect();
            if(editVue.isCorrect)
            {
                new ServerInterfaceJsonBody(baseInfo.updateInterface).execute(editVue.getAddForm(), function (jsonObj) {
                	editVue.showSubFlag = true;
                    if (jsonObj.result != "success")
                    {
                        generalErrorModal(jsonObj);
                    }
                    else
                    {
                    	if(jsonObj.info.includes("开户行")){
                    		//存在资金系统不支持的开户行，请在系统内完成拨付流程
                    		generalErrorModal(jsonObj);
                    	}else{
                    		generalSuccessModal();
                    	}
                        enterNewTabCloseCurrent(editVue.tableId, '资金统筹详情', 'tgpf/Tgpf_FundOverallPlanListDetail.shtml');
                    }
                });
            }else{
            	editVue.showSubFlag = true;
            }
        }
        
    }

    //------------------------方法定义-结束------------------//

    //------------------------数据初始化-开始----------------//
    editVue.initData();
    //------------------------数据初始化-结束----------------//
})({
    "editDivId": "#tgpf_FundOverallPlanEditDiv",
    "listDetailInterface": "../Tgpf_FundOverallPlanListDetail", //资金统筹详情
    "updateInterface": "../Tgpf_FundOverallPlanUpdate",//统筹
});