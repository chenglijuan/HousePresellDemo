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
			tg_IncomeForecastList:[],

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

		var selectDateStr = $('#date21010101').val();
		if (selectDateStr != null)
		{
            startDateStr = getNowFormatDate();
            stopDateStr = selectDateStr;
		}
		$("#myTab .active a").attr("data-extra",selectDateStr);
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
                for (var i = 0; i < jsonObj.tg_IncomeForecastList.length; i += 1)
                {
                    var tg_IncomeForecast = jsonObj.tg_IncomeForecastList[i];
                    if (tg_IncomeForecast.bankLending != null)
                    {
                        tg_IncomeForecast.bankLending = addThousands(tg_IncomeForecast.bankLending);
                    }
                    if (tg_IncomeForecast.incomeForecast1 != null)
                    {
                        tg_IncomeForecast.incomeForecast1 = addThousands(tg_IncomeForecast.incomeForecast1);
                    }
                    if (tg_IncomeForecast.incomeForecast2 != null)
                    {
                        tg_IncomeForecast.incomeForecast2 = addThousands(tg_IncomeForecast.incomeForecast2);
                    }
                    if (tg_IncomeForecast.incomeForecast3 != null)
                    {
                        tg_IncomeForecast.incomeForecast3 = addThousands(tg_IncomeForecast.incomeForecast3);
                    }
                }

                listVue.tg_IncomeForecastList=jsonObj.tg_IncomeForecastList;

				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				//动态跳转到锚点处，id="top"
				document.getElementById('tg_IncomeForecastListDiv').scrollIntoView();

                var selectDateStr = $('#date21010101').val();
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
		for (var i = 0; i < listVue.tg_IncomeForecastList.length; i += 1)
		{
			var tg_IncomeForecast = listVue.tg_IncomeForecastList[i];
            tg_IncomeForecast.incomeTrendForecast = commafyback(tg_IncomeForecast.incomeTrendForecast);
			tg_IncomeForecast.fixedExpire = commafyback(tg_IncomeForecast.fixedExpire);
            tg_IncomeForecast.bankLending = commafyback(tg_IncomeForecast.bankLending);
            tg_IncomeForecast.incomeForecast1 = commafyback(tg_IncomeForecast.incomeForecast1);
            tg_IncomeForecast.incomeForecast2 = commafyback(tg_IncomeForecast.incomeForecast2);
            tg_IncomeForecast.incomeForecast3 = commafyback(tg_IncomeForecast.incomeForecast3);
		}

        var model = {
            interfaceVersion : listVue.interfaceVersion,
            incomeForecastList : listVue.tg_IncomeForecastList
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
            generalErrorModal("", "请先选择时间并搜索");
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
            elem: '#date21010101',
            min: getNowFormatDate()
        });
        // refresh();
		initButtonList();
		var selectDateStr = $('#date21010101').val();
		console.log(selectDateStr)
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tg_IncomeForecastListDiv",
	"listInterface":"../Tg_IncomeForecastList",
	"updateInterface":"../Tg_IncomeForecastBatchUpdate",
    "exportExcelInterface":"../Tg_IncomeForecastExportExcel"
});
