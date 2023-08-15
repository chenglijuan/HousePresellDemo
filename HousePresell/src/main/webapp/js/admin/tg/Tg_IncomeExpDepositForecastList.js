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
			tg_IncomeExpDepositForecastList:[],
			exampleNum:0.00,
			exampleNumFormat:'0.00',
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
            clearNoNumExp :function (){
                var value = listVue.exampleNum;
                if (value != null)
                {
                    value = value.replace(/[^0-9/.]/g,''); //清除“数字”和“.”以外的字符
                    value = value.replace(/^\./g,"");  //验证第一个字符是数字而不是.
                    value = value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的.
                    value = value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
                    listVue.exampleNum = value;
                }
            },
            
            changNumExp : function(){
                listVue.exampleNumFormat = listVue.exampleNum;
                listVue.exampleNum = addThousands(listVue.exampleNum);
            },
            changeThousands:function (obj, theKey) {
                var value = obj[theKey];
                if (value != null)
                {
                    value = addThousands(value);
                    obj[theKey] = value;
                }
            },
            //清除无效数据，同时计算扣除参考值后的托管余额
            changNum : changNum,
            exampleNumHandle : exampleNumHandle,
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
	function exampleNumHandle(){
		console.log('开始、。。。。。')
		if(listVue.tg_IncomeExpDepositForecastList.length < 1){
			generalErrorModal("", "请先选择时间段并搜索");
			return;
		}else{
			if(undefined == listVue.exampleNumFormat || null == listVue.exampleNumFormat ){
				
				listVue.exampleNumFormat = 0.00;
			}
			console.log('11111开始。。。。。。')
			for (var i = 0; i < listVue.tg_IncomeExpDepositForecastList.length; i += 1) {
				listVue.tg_IncomeExpDepositForecastList[i].collocationReference = addThousands(listVue.exampleNumFormat);
				listVue.tg_IncomeExpDepositForecastList[i].collocationBalance = listVue.tg_IncomeExpDepositForecastList[i].collocationBalance1 - listVue.exampleNumFormat
			}
		}
	}
	
	function changNum(obj,theKey){
		var value = obj[theKey];
		console.log(obj);
        if (value != null)
        {
        	obj['collocationBalance'] = obj['collocationBalance1'] - value;
        	
            value = addThousands(value);
            obj[theKey] = value;
        }
	}
	 
	//列表操作--------------获取"搜索列表"表单参数
	function getSearchForm()
	{
        var startDateStr = null;
        var stopDateStr = null;

		var selectDateStr = $('#date21010301').val();
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
                for (var i = 0; i < jsonObj.tg_IncomeExpDepositForecastList.length; i += 1) {
                    var tg_IncomeExpDepositForecast = jsonObj.tg_IncomeExpDepositForecastList[i];
                    if (tg_IncomeExpDepositForecast.canDepositReference1 != null) {
                        tg_IncomeExpDepositForecast.canDepositReference1 = addThousands(tg_IncomeExpDepositForecast.canDepositReference1);
                    }

                    if (tg_IncomeExpDepositForecast.canDepositReference2 != null) {
                        tg_IncomeExpDepositForecast.canDepositReference2 = addThousands(tg_IncomeExpDepositForecast.canDepositReference2);
                    }
                    
                    tg_IncomeExpDepositForecast.collocationBalance1 = tg_IncomeExpDepositForecast.collocationBalance;
                    if (tg_IncomeExpDepositForecast.collocationReference != null) {
                    	tg_IncomeExpDepositForecast.collocationBalance = tg_IncomeExpDepositForecast.collocationBalance - tg_IncomeExpDepositForecast.collocationReference;
                        tg_IncomeExpDepositForecast.collocationReference = addThousands(tg_IncomeExpDepositForecast.collocationReference);
                    }
                    
                    
                    
                }

				listVue.tg_IncomeExpDepositForecastList=jsonObj.tg_IncomeExpDepositForecastList;
				console.log(listVue.tg_IncomeExpDepositForecastList);
				
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				//动态跳转到锚点处，id="top"
				document.getElementById('tg_IncomeExpDepositForecastListDiv').scrollIntoView();

                var selectDateStr = $('#date21010301').val();
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
        for (var i = 0; i < listVue.tg_IncomeExpDepositForecastList.length; i += 1)
        {
            var tg_IncomeExpDepositForecast = listVue.tg_IncomeExpDepositForecastList[i];
            tg_IncomeExpDepositForecast.lastDaySurplus = commafyback(tg_IncomeExpDepositForecast.lastDaySurplus);
            tg_IncomeExpDepositForecast.incomeTotal = commafyback(tg_IncomeExpDepositForecast.incomeTotal);
            tg_IncomeExpDepositForecast.expTotal = commafyback(tg_IncomeExpDepositForecast.expTotal);
            tg_IncomeExpDepositForecast.todaySurplus = commafyback(tg_IncomeExpDepositForecast.todaySurplus);
            tg_IncomeExpDepositForecast.collocationReference = commafyback(tg_IncomeExpDepositForecast.collocationReference);
            tg_IncomeExpDepositForecast.collocationBalance = commafyback(tg_IncomeExpDepositForecast.collocationBalance);
            tg_IncomeExpDepositForecast.canDepositReference1 = commafyback(tg_IncomeExpDepositForecast.canDepositReference1);
            tg_IncomeExpDepositForecast.canDepositReference2 = commafyback(tg_IncomeExpDepositForecast.canDepositReference2);
            
        }

        var model = {
            interfaceVersion : this.interfaceVersion,
            incomeExpDepositForecastList : this.tg_IncomeExpDepositForecastList
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
            elem: '#date21010301',
            min: getNowFormatDate()
        });
        // refresh();
		initButtonList();
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tg_IncomeExpDepositForecastListDiv",
	"listInterface":"../Tg_IncomeExpDepositForecastList",
	"updateInterface":"../Tg_IncomeExpDepositForecastBatchUpdate",
    "exportExcelInterface":"../Tg_IncomeExpDepositForecastExportExcel"
});
