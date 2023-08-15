(function(baseInfo){
    var listVue = new Vue({
        el : baseInfo.listDivId,
        data : {
            interfaceVersion :19000101,
            theState:0,//正常为0，删除为1
            tg_RiskRoutineMonthSumList:[],
            keyword:"",
            dateStr:"",
        },
        methods : {
            refresh : refresh,
            initData : initData,
            saveHandle : saveHandle,
            getSearchForm : getSearchForm,
            search : search,
            reset:reset,
            riskRouteMonth:riskRouteMonth,
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

    //列表操作--------------获取"搜索列表"表单参数
    function getSearchForm()
    {

        return {
            interfaceVersion:this.interfaceVersion,
            keyword:this.keyword,
            dateStr:this.dateStr,
        }
    }

    //列表操作--------------刷新
    function refresh()
    {
        new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj){
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                listVue.tg_RiskRoutineMonthSumList=jsonObj.tg_RiskRoutineMonthSumList;
                listVue.tg_RiskRoutineMonthSumList.forEach(function(item){
                    item.sumCheckCount =  addThousands(item.sumCheckCount);
                    item.qualifiedCount =  addThousands(item.qualifiedCount);
                    item.unqualifiedCount =  addThousands(item.unqualifiedCount);
                    item.pushCount =  addThousands(item.pushCount);
                    item.handleCount =  addThousands(item.handleCount);
                })
                //动态跳转到锚点处，id="top"
                document.getElementById('tg_RiskRoutineMonthSumListDiv').scrollIntoView();
            }
        });
    }

    //列表操作--------------更新表单参数
    function saveHandle()
    {
        var model = {
            interfaceVersion : this.interfaceVersion,
            expForecastList : this.tg_ExpForecastList
        };

        console.log(model);

        new ServerInterfaceJsonBody(baseInfo.updateInterface).execute(model, function(jsonObj){
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                generalSuccessModal();
                refresh();
            }
        });
    }

    //列表操作------------搜索
    function search()
    {
        listVue.dateStr = $("#date2102010401").val();
        refresh();
    }

    function reset()
    {
        listVue.keyword="";
        $("#date2102010401").val("");
    }

    function riskRouteMonth(bigBusiType)
    {
        enterNewTab(bigBusiType + "--" + listVue.dateStr, "风控月度小结详情", "Tg_RiskRoutineMonthSumDetail.shtml")
    }

    function initData()
    {
        laydate.render({
            elem: '#date2102010401',
            type: 'month',
            value: new Date(),
            done: function (value, date) {

            }
        });
        // laydate.now(0, 'YYYY-MM');
    }
    //------------------------方法定义-结束------------------//

    //------------------------数据初始化-开始----------------//
    listVue.initData();
    listVue.search();
    //------------------------数据初始化-结束----------------//
})({
    "listDivId":"#tg_RiskRoutineMonthSumListDiv",
    "listInterface":"../Tg_RiskRoutineMonthSumList",
    "updateInterface":"../Tg_ExpForecastBatchUpdate"
});
