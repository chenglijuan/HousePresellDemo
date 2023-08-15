(function(baseInfo){
    var listVue = new Vue({
        el : baseInfo.listDivId,
        data : {
            interfaceVersion :19000101,
            pageNumber : 1,
            countPerPage : 20,
            totalPage : 1,
            totalCount : 1,
            theState:0,//正常为0，删除为1
            userStartId:null,
            userStartList:[],
            userRecordId:null,
            userRecordList:[],
            tg_ExpForecastList:[],

            //excel导出
            excelStartDateStr:null,
            excelStopDateStr:null
        },
        methods : {
            refresh : refresh,
            initData : initData,
            saveHandle : saveHandle,
            getSearchForm : getSearchForm,
            search : search,
            exportExcelHandle : exportExcelHandle,
            changePageNumber : function(data){
                listVue.pageNumber = data;
            },
            clearNoNum :function (obj,theKey){
                var value = obj[theKey];
                if (value != null)
                {
                    value = value.replace(/[^0-9/.]/g,''); //清除“数字”和“.”以外的字符
                    value = value.replace(/^\./g,"");  //验证第一个字符是数字而不是.
                    value = value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的.
                    value = value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
                    obj[theKey] = value;
                }
            },
            changeThousands:function (obj, theKey) {
                var value = obj[theKey];
                if (value != null)
                {
                    value = addThousands(value);
                    obj[theKey] = value;
                }
            }
        },
        filters : {
            setThousands:function(data) {
                return addThousands(data);
            }
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
        var startDateStr = null;
        var stopDateStr = null;

        var selectDateStr = $('#date21010201').val();
        if (selectDateStr != null)
        {
            startDateStr = getNowFormatDate();
            stopDateStr = selectDateStr;
        }

        return {
            interfaceVersion:this.interfaceVersion,
            pageNumber:this.pageNumber,
            countPerPage:this.countPerPage,
            totalPage:this.totalPage,
            startTimeStr:startDateStr,
            endTimeStr:stopDateStr
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
                for (var i = 0; i < jsonObj.tg_ExpForecastList.length; i += 1)
                {
                    var tg_ExpForecast = jsonObj.tg_ExpForecastList[i];
                    if (tg_ExpForecast.payForecast1 != null)
                    {
                        tg_ExpForecast.payForecast1 = addThousands(tg_ExpForecast.payForecast1);
                    }
                    if (tg_ExpForecast.payForecast2 != null)
                    {
                        tg_ExpForecast.payForecast2 = addThousands(tg_ExpForecast.payForecast2);
                    }
                    if (tg_ExpForecast.payForecast3 != null)
                    {
                        tg_ExpForecast.payForecast3 = addThousands(tg_ExpForecast.payForecast3);
                    }
                }

                listVue.tg_ExpForecastList=jsonObj.tg_ExpForecastList;
                listVue.pageNumber=jsonObj.pageNumber;
                listVue.countPerPage=jsonObj.countPerPage;
                listVue.totalPage=jsonObj.totalPage;
                listVue.totalCount = jsonObj.totalCount;
                //动态跳转到锚点处，id="top"
                document.getElementById('tg_ExpForecastListDiv').scrollIntoView();


                var selectDateStr = $('#date21010201').val();
                if (selectDateStr != null)
                {
                    listVue.excelStartDateStr = getNowFormatDate();
                    listVue.excelStopDateStr  = selectDateStr;
                }
            }
        });
    }

    //列表操作--------------更新表单参数
    function saveHandle()
    {
        for (var i = 0; i < listVue.tg_ExpForecastList.length; i += 1)
        {
            var tg_ExpForecast = listVue.tg_ExpForecastList[i];
            tg_ExpForecast.payTrendForecast = commafyback(tg_ExpForecast.payTrendForecast);
            tg_ExpForecast.applyAmount = commafyback(tg_ExpForecast.applyAmount);
            tg_ExpForecast.payableFund = commafyback(tg_ExpForecast.payableFund);
            tg_ExpForecast.nodeChangePayForecast = commafyback(tg_ExpForecast.nodeChangePayForecast);
            tg_ExpForecast.handlingFixedDeposit = commafyback(tg_ExpForecast.handlingFixedDeposit);
            tg_ExpForecast.payForecast1 = commafyback(tg_ExpForecast.payForecast1);
            tg_ExpForecast.payForecast2 = commafyback(tg_ExpForecast.payForecast2);
            tg_ExpForecast.payForecast3 = commafyback(tg_ExpForecast.payForecast3);
        }

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

    function exportExcelHandle() {
        if (listVue.excelStartDateStr == null || listVue.excelStopDateStr == null)
        {
            generalErrorModal("", "请先选择时间段并搜索");
            return;
        }

        var model = {
            interfaceVersion : listVue.interfaceVersion,
            startTimeStr: listVue.excelStartDateStr,
            endTimeStr:	listVue.excelStopDateStr
        };

        new ServerInterface(baseInfo.exportExcelInterface).execute(model, function(jsonObj)
        {
            if (jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                window.location.href=jsonObj.fileDownloadPath;
                console.log(jsonObj.fileDownloadPath);
            }
        });
    }

    //列表操作------------搜索
    function search()
    {
        listVue.pageNumber = 1;
        refresh();
    }

    function initButtonList()
    {
        //封装在BaseJs中，每个页面需要控制按钮的就要
        getButtonList();
    }

    function initData()
    {
        laydate.render({
            elem: '#date21010201',
            min: getNowFormatDate()
        });
        // refresh();
        // initButtonList();
    }
    //------------------------方法定义-结束------------------//

    //------------------------数据初始化-开始----------------//
    listVue.initData();
    //------------------------数据初始化-结束----------------//
})({
    "listDivId":"#tg_ExpForecastListDiv",
    "listInterface":"../Tg_ExpForecastList",
    "updateInterface":"../Tg_ExpForecastBatchUpdate",
    "exportExcelInterface":"../Tg_ExpForecastExportExcel"
});
