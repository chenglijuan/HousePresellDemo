(function(baseInfo){
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			tableId : 1,
			pageNumber : 1,
			countPerPage : MAX_VALUE,
			totalPage : 1,
			totalCount : 1,
			selectItem : [],
            theState:"0",
			isAllSelected : false,
            emmp_DevelopCompanyInfoList:[],//开发企业列表
            companyInfoId:"",//开发企业Id
            empj_ProjectInfoList:[], // 项目列表
            projectId:"", //项目Id
            theNameOfProject:"",//项目名称
            empj_BuildingInfoList:[],//楼幢列表
            multipleSelection:[] ,//选中的楼幢信息
            bankAccountSupervisedId:"",//监管账号Id
            tgpf_FundAppropriated_AFModel:{},   //用款基本信息
            tgpf_FundAppropriated_AFAddtab:[],     //用款申请楼幢信息
            fundAppropriated_AFAddtab:[],// 楼幢信息传到后台
            tgpf_FundOverPlanDetailltab:[], //用款申请汇总信息
            buttonType:"",//按钮类型
            applyDate:"",
            companyDisabled:false,
            showSubFlag : true,
            applyType :"0",//申请类型
		},
		methods : {
            initData: initData,
            handleSelectionChange: handleSelectionChange,
            //添加
            getAddForm: getAddForm,
            checkAllClicked: checkAllClicked,
            addTgpf_FundAppropriated_AF: addTgpf_FundAppropriated_AF,
            companyInfoChange:companyInfoChange,
            buildingInfoChange: buildingInfoChange,
            getBuildingInfoList: getBuildingInfoList,
            summary:summary,
            generateSummary:generateSummary,
            thousandsTOPoint:thousandsTOPoint,
            appliedAmountFocus:appliedAmountFocus,
            appliedAmountblur:appliedAmountblur,
        },
		computed:{

		},
		components : {
            'vue-select': VueSelect
		},
		watch:{
            selectItem : selectedItemChanged,
		},
        mounted: function ()
        {
            laydate.render({
                elem: '#date061203010201',
            })
        },
	});
	//------------------------方法定义-开始------------------//

	//初始化
    function initData()
    {
        var date = new Date();
        var seperator1 = "-";
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        var currentdate = year + seperator1 + month + seperator1 + strDate;
        addVue.applyDate = currentdate;
        $("#date061203010201").val(currentdate);

        //获取所有开发企业信息
        getDevelopCompanyInfoList();
    }

    //获取所有开发企业信息
    function getDevelopCompanyInfoList()
    {
        var model =
            {
                interfaceVersion:addVue.interfaceVersion,
                pageNumber : addVue.pageNumber,
                countPerPage : addVue.countPerPage,
                totalPage : addVue.totalPage,
                totalCount : addVue.totalCount,
                theState:addVue.theState,
                theType:"1",
            };
        new ServerInterface(baseInfo.getCompanyInfoInterface).execute(model, function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
            	if(jsonObj.companyInfoId != null && jsonObj.companyInfoId > 0)
        		{
            		addVue.companyInfoId = jsonObj.companyInfoId;
            		addVue.companyDisabled = true;
            		addVue.emmp_DevelopCompanyInfoList.push({
            			tableId:addVue.companyInfoId,
            			theName:jsonObj.emmp_companyInfo.theName,
            		});
            		getProjectInfoList();
        		}
            	else
        		{
            		addVue.emmp_DevelopCompanyInfoList = jsonObj.emmp_CompanyInfoList;
        		}
            }
        });
    }
    
    function companyInfoChange(val)
    {
        addVue.companyInfoId = val.tableId;
        if(addVue.companyInfoId == "")
        {
            addVue.empj_ProjectInfoList = [];
        }
        else
        {
            getProjectInfoList();
        }
    }

    //获取当前企业下所有项目列表信息
    function getProjectInfoList() {
        var model =
            {
                interfaceVersion:addVue.interfaceVersion,
                pageNumber : addVue.pageNumber,
                countPerPage : addVue.countPerPage,
                totalPage : addVue.totalPage,
                totalCount : addVue.totalCount,
                theState:addVue.theState,
                developCompanyId:addVue.companyInfoId,
            };
        new ServerInterface(baseInfo.getProjectInterface).execute(model, function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                addVue.empj_ProjectInfoList = jsonObj.empj_ProjectInfoList;
            }
        });
    }

    function buildingInfoChange(date)
    {
        addVue.projectId = date.tableId;
        addVue.theNameOfProject = date.theName;
        if(addVue.projectId == "")
        {
            addVue.tgpf_FundAppropriated_AFAddtab = [];
        }
        else
        {
           getBuildingInfoList();
        }
    }

    //选中项目获取该项目下所有楼幢信息

    function getBuildingInfoList()
    {
        var model =
            {
                interfaceVersion:addVue.interfaceVersion,
                projectId : addVue.projectId,
                startEscrow:"已托管",
                endEscrow:"托管终止",
                orderBy : "eCode desc",
//                orderByType : "desc",
            };
        new ServerInterface(baseInfo.getBuildingInterface).execute(model, function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                addVue.tgpf_FundAppropriated_AFAddtab=jsonObj.empj_BuildingInfoList;
            }
        });
    }


    function selectedItemChanged()
    {
        addVue.isAllSelected = (addVue.tgpf_FundAppropriated_AFAddtab.length > 0)
            &&	(addVue.selectItem.length == addVue.tgpf_FundAppropriated_AFAddtab.length);
    }

    //用款申请信息按监管账号汇总
    function  summary(tab)
    {
        addVue.tgpf_FundAppropriated_AFModel.applyTotalAmount = 0;
        for(var i=0;i<tab.length;i++)
        {
            addVue.tgpf_FundAppropriated_AFModel.applyTotalAmount += (commafyback(tab[i].appliedAmount) -0);
        }
        addVue.tgpf_FundAppropriated_AFModel.applyTotalAmount = thousandsToTwoDecimal(addVue.tgpf_FundAppropriated_AFModel.applyTotalAmount);

        var arr = tab;
        var map = {};
        var dest = [];
        for(var i = 0; i < arr.length; i++)
        {
            var ai = arr[i];
            if(!map[ai.theAccount])
            {
                dest.push(ai);
                map[ai.theAccount] = ai;
            }
            else
            {
                for(var j = 0; j < dest.length; j++)
                {
                    var dj = dest[j];
                    if(dj.theAccount == ai.theAccount)
                    {
                        dj.appliedAmount = (commafyback(dj.appliedAmount) - 0) + (commafyback(ai.appliedAmount) - 0);
                    }
                }
            }
        }
        for (var index = 0; index < dest.length; index++)
        {
            dest[index].appliedAmount = thousandsToTwoDecimal(dest[index].appliedAmount);
        }
        addVue.tgpf_FundOverPlanDetailltab = dest;
    }

    //本次划拨申请金额获取焦点
    function appliedAmountFocus(index)
    {
        if((commafyback(addVue.tgpf_FundAppropriated_AFAddtab[index].appliedAmount) - 0) > 0)
        {
            addVue.tgpf_FundAppropriated_AFAddtab[index].appliedAmount = (commafyback(addVue.tgpf_FundAppropriated_AFAddtab[index].appliedAmount) - 0 )
        }
        else
        {
            addVue.tgpf_FundAppropriated_AFAddtab[index].appliedAmount = "";
            generateSummary();
        }
    }

    //本次划拨申请金额失去焦点
    function appliedAmountblur(ev,index)
    {
    	//$event,ev,
    	if(addVue.tgpf_FundAppropriated_AFAddtab[index].newAllocableAmount !=null)
        {
    		if((addVue.tgpf_FundAppropriated_AFAddtab[index].newAllocableAmount -0) < (ev.srcElement.value -0)){
    			if(Math.abs(((addVue.tgpf_FundAppropriated_AFAddtab[index].newAllocableAmount -0) - (ev.srcElement.value -0)).toFixed(2))> 0.1)//(ev.srcElement.value -0) > (allocableAmount -0)
                {
                    generalErrorModal("","本次划款申请金额不得大于当前可划拨金额，请确认！");
                    // return;
                }
    		}
        }
    	
        if((commafyback(addVue.tgpf_FundAppropriated_AFAddtab[index].appliedAmount) - 0) > 0)
        {
            addVue.tgpf_FundAppropriated_AFAddtab[index].appliedAmount = thousandsToTwoDecimal(addVue.tgpf_FundAppropriated_AFAddtab[index].appliedAmount)
        }
        else
        {
            addVue.tgpf_FundAppropriated_AFAddtab[index].appliedAmount = "";
            generateSummary();
        }
    }
    
    
    function generateSummary(ev,allocableAmount)
    {
        //输入校验
        if(ev !=null)
        {
            if(isNaN(ev.srcElement.value))
            {
                generalErrorModal("","请输入大于0的数字");
                return;
            }
        }
        
        
        
    	addVue.tgpf_FundOverPlanDetailltab = [];

    	for(var i=0;i < addVue.multipleSelection.length;i++)
		{
    		var row = addVue.multipleSelection[i];
    		if(row.bankAccountSupervisedId != "" && row.bankAccountSupervisedId > 0)
    		{
    			var index = getListIndex(row.bankAccountSupervisedId, row.bankAccountSupervisedList);
    			addVue.tgpf_FundOverPlanDetailltab.push({
	    			buildingId:row.tableId,//楼幢Id
	    			tableId:row.bankAccountSupervisedId,//监管账户Id
	    			theName: row.bankAccountSupervisedList[index].theName,//监管账户名称
	    			theAccount: row.bankAccountSupervisedList[index].theAccount,//监管账号
	    			appliedAmount:commafyback(row.appliedAmount)-0,//本次划拨申请金额
                    theNameOfProject:addVue.theNameOfProject,//项目名称
	    		});
    		}
		}
    	summary(addVue.tgpf_FundOverPlanDetailltab);
    }

    function getListIndex(tableId, list)
    {
    	for(var i=0;i<list.length;i++)
		{
    		if(tableId == list[i].tableId)
			{
    			return i;
			}
		}
    }

    //获取选中复选框所在行的tableId
    function handleSelectionChange(val) {

        addVue.multipleSelection = val;

        generateSummary();

        addVue.selectItem = [];
        for (var index = 0; index < val.length; index++)
        {
            var element = val[index].tableId;
            addVue.selectItem.push(element)
        }

        for (var i = 0; i < addVue.tgpf_FundAppropriated_AFAddtab.length; i++)
        {
            var count=0;
            for (var j = 0; j < val.length; j++)
            {
                if(addVue.tgpf_FundAppropriated_AFAddtab[i].tableId == val[j].tableId)
                {
                    count++;
                    break;
                }
            }
            if(count>0)
            {
                addVue.tgpf_FundAppropriated_AFAddtab[i].disabled = false;
            }
            else
            {
                addVue.tgpf_FundAppropriated_AFAddtab[i].disabled = true;
                addVue.tgpf_FundAppropriated_AFAddtab[i].bankAccountSupervisedId = "";
                addVue.tgpf_FundAppropriated_AFAddtab[i].appliedAmount = "";
            }
        }
    }

    //千分位转小数
    function thousandsTOPoint()
    {
        addVue.fundAppropriated_AFAddtab = [];
        for (var i = 0; i < addVue.multipleSelection.length; i++)
        {
            var checked = addVue.multipleSelection[i];
            addVue.fundAppropriated_AFAddtab.push({
                tableId:checked.tableId, // 楼幢Id
                bankAccountSupervisedId:checked.bankAccountSupervisedId, // 监管账户Id
                escrowStandard:checked.escrowStandard, // 托管标准
                appliedAmount:commafyback(checked.appliedAmount)-0,// 本次划拨申请金额
            });
        }
    }

    //新增操作--------------
    function getAddForm()
    {
        return {
            interfaceVersion:addVue.interfaceVersion,
            developCompanyId:addVue.developCompanyId,
            projectId:addVue.projectId,
            totalApplyAmount:commafyback(addVue.tgpf_FundAppropriated_AFModel.applyTotalAmount)-0,
            tgpf_FundAppropriated_AFAddtab:addVue.fundAppropriated_AFAddtab,
            tgpf_FundOverPlanDetailltab:addVue.tgpf_FundOverPlanDetailltab,
            buttonType: addVue.buttonType,
            applyDate:$('#date061203010201').val(),
        }
    }

    function addTgpf_FundAppropriated_AF(buttonType)
    {
        for (var index = 0; index < addVue.tgpf_FundOverPlanDetailltab.length; index++)
        {
            addVue.tgpf_FundOverPlanDetailltab[index].appliedAmount = commafyback(addVue.tgpf_FundOverPlanDetailltab[index].appliedAmount)-0;
        }

        thousandsTOPoint();

        if(buttonType == 1)
        {
            addVue.buttonType = 1;
        }else if(buttonType == 2)
        {
            addVue.buttonType = 2;
            addVue.showSubFlag = false;
        }
        new ServerInterfaceJsonBody(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj){
        	addVue.showSubFlag = true;
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                generalSuccessModal();
                enterNewTabCloseCurrent(jsonObj.tableId+"_"+addVue.projectId, '用款申请详情', 'tgpf/Tgpf_FundAppropriated_AFDetail.shtml');
            }
        });
    }

    //列表操作--------------“全选”按钮被点击
    function checkAllClicked()
    {
        if(addVue.selectItem.length == addVue.tgpf_FundAppropriated_AFList.length)
        {
            addVue.selectItem = [];
        }
        else
        {
            addVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
            addVue.tgpf_FundAppropriated_AFList.forEach(function(item) {
                addVue.selectItem.push(item.tableId);
            });
        }
    }


	//------------------------方法定义-结束------------------//

	//------------------------数据初始化-开始----------------//
	addVue.initData();
})({
	"addDivId":"#tgpf_FundAppropriated_AFAddDiv",
    "getCompanyInfoInterface":"../Emmp_CompanyInfoForAllForSelect",//获取开发企业列表信息
    "getProjectInterface":"../Empj_ProjectInfoForSelect",//获取项目列表信息
    "getBuildingInterface":"../FundAppropriated_BuildingInfoList", //用款申请楼幢信息
	"addInterface":"../Tgpf_FundAppropriated_AFAdd",//新增用款申请
});