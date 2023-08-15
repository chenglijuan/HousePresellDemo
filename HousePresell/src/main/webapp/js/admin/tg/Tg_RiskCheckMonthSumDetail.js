(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion : 19000101,
			tg_RiskCheckMonthSum : {},
			tg_RiskCheckBusiCodeSumList : [],
			spotTimeStr : null,
			reqSource : null,
			theState : null,
			pageNumber : 1,
			countPerPage : 20,
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			indexMethod: function (index) {
				if (detailVue.pageNumber > 1) {
					return (detailVue.pageNumber - 1) * detailVue.countPerPage - 0 + (index - 0 + 1);
				}
				if (detailVue.pageNumber <= 1) {
					return index - 0 + 1;
				}
			},
			saveRiskCheck : saveRiskCheck,
			rishCheckBusiCodeSumDetailPageOpen : function(data){
				enterNewTab(this.spotTimeStr+"、"+data.bigBusiValue+"、"+data.smallBusiValue, '录入抽查结果', 'tg/Tg_RiskCheckBusiCodeSumDetail.shtml')
			}
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
				detailVue.tg_RiskCheckMonthSum = jsonObj.tg_RiskCheckMonthSum;
				detailVue.tg_RiskCheckBusiCodeSumList = jsonObj.tg_RiskCheckBusiCodeSumList;
			}
		});
	}
	
	function initData()
	{
		getIdFormTab("",function (id) {
			var resultArr = id.split("、");
            detailVue.spotTimeStr = resultArr[resultArr.length-2];
            detailVue.reqSource = resultArr[resultArr.length-1];
            if("新增" == detailVue.reqSource)
            {
            	detailVue.theState = 2;
            }
            if("列表" == detailVue.reqSource)
        	{
            	detailVue.theState = 0;
        	}
            refresh();
        })
	}
	
	function saveRiskCheck()
	{
		new ServerInterface(baseInfo.saveInterface).execute(detailVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				generalSuccessModal();
				enterNewTabCloseCurrent("", '风控例行抽查', 'tg/Tg_RiskCheckMonthSumList.shtml')
			}
		});
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.initData();
	
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tg_RiskCheckMonthSumDetailDiv",
	"detailInterface":"../Tg_RiskCheckMonthSumDetail",
	"saveInterface":"../Tg_RiskCheckMonthSumSave",
});
