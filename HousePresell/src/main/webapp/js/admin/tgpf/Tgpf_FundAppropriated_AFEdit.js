(function(baseInfo){
    var updateVue = new Vue({
        el : baseInfo.editDivId,
        data : {
            interfaceVersion :19000101,
            tableId : 1,
            pageNumber : 1,
            countPerPage : MAX_VALUE,
            totalPage : 1,
            totalCount : 1,
            selectItem : [],
            isAllSelected : false,
            projectId:"",
            multipleSelection:[] ,//选中的楼幢信息
            theNameOfProject:"",
            tgpf_FundAppropriated_AFModel:{},
            tgpf_FundAppropriated_AFAddtab:[], //楼幢信息
            fundAppropriated_AFAddtab:[],//
            tgpf_fundAppropriated_afDtltab:[],
            tgpf_FundOverPlanDetailltab:[],
            buttonType:"",
            showSubFlag : true,
        },
        methods : {
            refresh : refresh,
            initData: initData,
            getSearchForm : getSearchForm,
            handleSelectionChange: handleSelectionChange,
            toggleSelection:toggleSelection,
            getBuildingInfoList:getBuildingInfoList,
            getDetail:getDetail,
            summary:summary,
            generateSummary:generateSummary,
            //更新
            getUpdateForm : getUpdateForm,
            thousandsTOPoint:thousandsTOPoint,
            updateTgpf_FundAppropriated_AF: updateTgpf_FundAppropriated_AF,
            appliedAmountFocus:appliedAmountFocus,
            appliedAmountblur:appliedAmountblur,
        },
        computed:{

        },
        components : {

        },
        watch:{

        },
        mounted: function ()
        {
            laydate.render({
                elem: '#date061203010301',
            })
        },
    });

    //------------------------方法定义-开始------------------//
    function initData()
    {
        var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
        var array = tableIdStr.split("_");
        if (array.length > 1)
        {
            updateVue.tableId = array[3]; //用款申请主表id
            updateVue.projectId = array[4];
            refresh();
        }
    }

    function getSearchForm()
    {
        return {
            interfaceVersion:updateVue.interfaceVersion,
            tableId:updateVue.tableId,
            projectId:updateVue.projectId,
            startEscrow:"已托管",
            endEscrow:"托管终止",
        }
    }

    //详情操作--------------
    function refresh()
    {
        if (updateVue.tableId == null || updateVue.tableId < 1)
        {
            return;
        }

        getBuildingInfoList()
    }

    function getBuildingInfoList()
    {
        new ServerInterface(baseInfo.getBuildingInterface).execute(updateVue.getSearchForm(), function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                updateVue.tgpf_FundAppropriated_AFAddtab = jsonObj.empj_BuildingInfoList;  //根据项目id获取楼幢信息
                getDetail();
            }
        });
    }
    
    function  getDetail()
    {
        new ServerInterface(baseInfo.detailInterface).execute(updateVue.getSearchForm(), function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                updateVue.tgpf_FundAppropriated_AFModel = jsonObj.tgpf_FundAppropriated_AF;
                updateVue.tgpf_fundAppropriated_afDtltab = jsonObj.tgpf_fundAppropriated_afDtlList;
                Vue.nextTick(function () {
                    for(var i =0 ;i < updateVue.tgpf_fundAppropriated_afDtltab.length ;i++)
                    {
                        for (var j = 0; j < updateVue.tgpf_FundAppropriated_AFAddtab.length; j++)
                        {
                            if(updateVue.tgpf_fundAppropriated_afDtltab[i].buildingId == updateVue.tgpf_FundAppropriated_AFAddtab[j].tableId)
                            {
                                updateVue.tgpf_FundAppropriated_AFAddtab[j].appliedAmount = updateVue.tgpf_fundAppropriated_afDtltab[i].appliedAmount;
                                updateVue.tgpf_FundAppropriated_AFAddtab[j].bankAccountSupervisedId = updateVue.tgpf_fundAppropriated_afDtltab[i].bankAccountSupervisedId;
                                
                                updateVue.tgpf_FundAppropriated_AFAddtab[j].allocableAmount = updateVue.tgpf_fundAppropriated_afDtltab[i].allocableAmount;
                                updateVue.tgpf_FundAppropriated_AFAddtab[j].newAllocableAmount = updateVue.tgpf_fundAppropriated_afDtltab[i].newAllocableAmount;
                                updateVue.tgpf_FundAppropriated_AFAddtab[j].appliedAmountOld = updateVue.tgpf_fundAppropriated_afDtltab[i].appliedAmountOld;
                                updateVue.tgpf_FundAppropriated_AFAddtab[j].escrowStandard = updateVue.tgpf_fundAppropriated_afDtltab[i].escrowStandard;
                                updateVue.tgpf_FundAppropriated_AFAddtab[j].escrowStandard = updateVue.tgpf_fundAppropriated_afDtltab[i].escrowStandard;
                                updateVue.tgpf_FundAppropriated_AFAddtab[j].orgLimitedAmount = updateVue.tgpf_fundAppropriated_afDtltab[i].orgLimitedAmount;
                                updateVue.tgpf_FundAppropriated_AFAddtab[j].currentFigureProgress = updateVue.tgpf_fundAppropriated_afDtltab[i].currentFigureProgress;
                                updateVue.tgpf_FundAppropriated_AFAddtab[j].currentLimitedRatio = updateVue.tgpf_fundAppropriated_afDtltab[i].currentLimitedRatio;
                                updateVue.tgpf_FundAppropriated_AFAddtab[j].currentLimitedAmount = updateVue.tgpf_fundAppropriated_afDtltab[i].currentLimitedAmount;
                                updateVue.tgpf_FundAppropriated_AFAddtab[j].totalAccountAmount = updateVue.tgpf_fundAppropriated_afDtltab[i].totalAccountAmount;
                                updateVue.tgpf_FundAppropriated_AFAddtab[j].appliedPayoutAmount = updateVue.tgpf_fundAppropriated_afDtltab[i].appliedPayoutAmount;
                                updateVue.tgpf_FundAppropriated_AFAddtab[j].currentEscrowFund = updateVue.tgpf_fundAppropriated_afDtltab[i].currentEscrowFund;
                                updateVue.tgpf_FundAppropriated_AFAddtab[j].refundAmount = updateVue.tgpf_fundAppropriated_afDtltab[i].refundAmount;
                                updateVue.tgpf_FundAppropriated_AFAddtab[j].actualReleaseAmount = updateVue.tgpf_fundAppropriated_afDtltab[i].actualReleaseAmount;
                                updateVue.tgpf_FundAppropriated_AFAddtab[j].cashLimitedAmount = updateVue.tgpf_fundAppropriated_afDtltab[i].cashLimitedAmount;
                                updateVue.tgpf_FundAppropriated_AFAddtab[j].effectiveLimitedAmount = updateVue.tgpf_fundAppropriated_afDtltab[i].effectiveLimitedAmount;
                                
                                toggleSelection(updateVue.tgpf_FundAppropriated_AFAddtab[j]);
                                break;
                            }
                        }
                    }
                })
            }
        });
    }

    function toggleSelection(row)
    {
        updateVue.$refs.multipleTable.toggleRowSelection(row,true);
    }

    //用款申请信息按监管账号汇总
    function  summary(tab)
    {
        updateVue.tgpf_FundAppropriated_AFModel.totalApplyAmount = 0;
        for(var i=0;i<tab.length;i++)
        {
            updateVue.tgpf_FundAppropriated_AFModel.totalApplyAmount += (commafyback(tab[i].appliedAmount) -0);
        }
        updateVue.tgpf_FundAppropriated_AFModel.totalApplyAmount = thousandsToTwoDecimal(updateVue.tgpf_FundAppropriated_AFModel.totalApplyAmount); //本次申请总金额

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
                        dj.appliedAmount = (dj.appliedAmount - 0) + (ai.appliedAmount - 0);
                    }
                }
            }
        }
        for (var index = 0; index < dest.length; index++)
        {
            dest[index].appliedAmount = thousandsToTwoDecimal(dest[index].appliedAmount);
        }
        updateVue.tgpf_FundOverPlanDetailltab = dest;
    }


    //本次划拨申请金额获取焦点
    function appliedAmountFocus(index)
    {
        if((commafyback(updateVue.tgpf_FundAppropriated_AFAddtab[index].appliedAmount) - 0) > 0)
        {
            updateVue.tgpf_FundAppropriated_AFAddtab[index].appliedAmount = (commafyback(updateVue.tgpf_FundAppropriated_AFAddtab[index].appliedAmount) - 0 )
        }
        else
        {
            updateVue.tgpf_FundAppropriated_AFAddtab[index].appliedAmount = "";
            generateSummary();
        }
    }

    //本次划拨申请金额失去焦点
    function appliedAmountblur(ev,index)
    {
    	if(updateVue.tgpf_FundAppropriated_AFAddtab[index].newAllocableAmount !=null)
        {
    		if((updateVue.tgpf_FundAppropriated_AFAddtab[index].newAllocableAmount -0) < (ev.srcElement.value -0)){
    			if(Math.abs(((updateVue.tgpf_FundAppropriated_AFAddtab[index].newAllocableAmount -0) - (ev.srcElement.value -0)).toFixed(2))> 0.1)
    			{
    				generalErrorModal("","本次划款申请金额不得大于当前可划拨金额，请确认！");
    				// return;
    			}
    		}
        }
        if((commafyback(updateVue.tgpf_FundAppropriated_AFAddtab[index].appliedAmount) - 0) > 0)
        {
            updateVue.tgpf_FundAppropriated_AFAddtab[index].appliedAmount = thousandsToTwoDecimal(updateVue.tgpf_FundAppropriated_AFAddtab[index].appliedAmount)
        }
        else
        {
            updateVue.tgpf_FundAppropriated_AFAddtab[index].appliedAmount = "";
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
                generalErrorModal("","请输入大于0 的数字");
                return;
            }
        }
        
        updateVue.tgpf_FundOverPlanDetailltab = [];
        for(var i=0;i < updateVue.multipleSelection.length;i++)
        {
            var row = updateVue.multipleSelection[i];
            if(row.bankAccountSupervisedId != "")
            {
                var index = getListIndex(row.bankAccountSupervisedId, row.bankAccountSupervisedList);
                updateVue.tgpf_FundOverPlanDetailltab.push({
                    buildingId:row.tableId,
                    tableId:row.bankAccountSupervisedId,
                    theName: row.bankAccountSupervisedList[index].theName,
                    theAccount: row.bankAccountSupervisedList[index].theAccount,
                    appliedAmount:commafyback(row.appliedAmount)-0,
                    theNameOfProject:updateVue.tgpf_FundAppropriated_AFModel.theNameOfProject,
                });
            }
        }
        summary(updateVue.tgpf_FundOverPlanDetailltab);
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

        updateVue.selectItem = [];
        for (var index = 0; index < val.length; index++)
        {
            var element = val[index].tableId;
            updateVue.selectItem.push(element)
        }
        updateVue.multipleSelection = val;

        generateSummary();

        for (var i = 0; i < updateVue.tgpf_FundAppropriated_AFAddtab.length; i++)
        {
            var count=0;
            for (var j = 0; j < val.length; j++)
            {
                if(updateVue.tgpf_FundAppropriated_AFAddtab[i].tableId == val[j].tableId)
                {
                    count++;
                    break;
                }
            }
            if(count>0)
            {
                updateVue.tgpf_FundAppropriated_AFAddtab[i].disabled = false;
            }
            else
            {
                updateVue.tgpf_FundAppropriated_AFAddtab[i].disabled = true;
                updateVue.tgpf_FundAppropriated_AFAddtab[i].bankAccountSupervisedId = "";
                updateVue.tgpf_FundAppropriated_AFAddtab[i].appliedAmount = "";
            }
        }
    }

    //千分位转小数
    function thousandsTOPoint()
    {
        updateVue.fundAppropriated_AFAddtab = [];
        for (var i = 0; i < updateVue.multipleSelection.length; i++)
        {
            var checked = updateVue.multipleSelection[i];
            updateVue.fundAppropriated_AFAddtab.push({
                tableId:checked.tableId, // 楼幢Id
                bankAccountSupervisedId:checked.bankAccountSupervisedId, // 监管账户Id
                escrowStandard:checked.escrowStandard, // 托管标准
                appliedAmount:commafyback(checked.appliedAmount)-0,// 本次划拨申请金额
            });
        }
    }


    function getUpdateForm()
    {
        return {
            interfaceVersion:updateVue.interfaceVersion,
            tableId:updateVue.tableId, //用款申请主表id
            projectId:updateVue.projectId,
            totalApplyAmount:commafyback(updateVue.tgpf_FundAppropriated_AFModel.totalApplyAmount)-0, // 本次申请总金额
            tgpf_FundAppropriated_AFAddtab:updateVue.fundAppropriated_AFAddtab, //明细表信息
            tgpf_FundOverPlanDetailltab:updateVue.tgpf_FundOverPlanDetailltab, //汇总信息
            buttonType: updateVue.buttonType,
            applyDate:$('#date061203010301').val(),
        }
    }

    function updateTgpf_FundAppropriated_AF(buttonType)
    {
        for (var index = 0; index < updateVue.tgpf_FundOverPlanDetailltab.length; index++)
        {
            updateVue.tgpf_FundOverPlanDetailltab[index].appliedAmount = commafyback(updateVue.tgpf_FundOverPlanDetailltab[index].appliedAmount)-0;
        }

        thousandsTOPoint();

        if(buttonType == 1)
        {
            updateVue.buttonType = 1;
        }else if(buttonType == 2)
        {
        	updateVue.buttonType = 2;
        	updateVue.showSubFlag = false;
        }
        new ServerInterfaceJsonBody(baseInfo.updateInterface).execute(updateVue.getUpdateForm(), function(jsonObj)
        {
        	updateVue.showSubFlag = true;
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                generalSuccessModal();
                var theId = updateVue.tableId +"_"+ updateVue.projectId;
                enterNewTabCloseCurrent(theId , '用款申请详情', 'tgpf/Tgpf_FundAppropriated_AFDetail.shtml');
            }
        });
    }
    //------------------------方法定义-结束------------------//

    //------------------------数据初始化-开始----------------//
    updateVue.initData();
    //------------------------数据初始化-结束----------------//
})({
    "editDivId":"#tgpf_FundAppropriated_AFEditDiv",
    "getBuildingInterface":"../FundAppropriated_BuildingInfoList", //用款申请楼幢信息
    "detailInterface":"../Tgpf_FundAppropriated_AFDetail",
    "updateInterface":"../Tgpf_FundAppropriated_AFUpdate"
});
