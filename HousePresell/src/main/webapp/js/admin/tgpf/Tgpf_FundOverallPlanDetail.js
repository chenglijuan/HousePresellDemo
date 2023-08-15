(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tableId : 1,
			pageNumber: 1,
			countPerPage: 20,
			totalPage: 1,
			totalCount: 1,
            tgpf_FundOverallPlanModel: {},
            tgpf_fundAppropriated_afDtltab:[],
            tgpf_fundAppropriatedtab:[],
            sumAppliedPayoutAmount : 0.0,
            sumCurrentPayoutAmount : 0.0
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
		    getSummaries:getSummaries,
		    getSummariesFund : getSummariesFund,
		},
		computed:{
			 
		},
		components : {

		},
		watch:{
			
		}
	});

	//------------------------方法定义-开始------------------//

    function initData()
    {
        getIdFormTab("",function (tableId) {
            detailVue.tableId=tableId;
            refresh()
        })
    }

	//详情操作--------------
	function getSearchForm()
	{
		return {
			interfaceVersion:detailVue.interfaceVersion,
			tableId:detailVue.tableId,
		}
	}

	//详情操作--------------
	function refresh()
	{
		if (detailVue.tableId == null || detailVue.tableId < 1)
		{
			return;
		}
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
				detailVue.tgpf_FundOverallPlanModel = jsonObj.tgpf_FundOverallPlan;
                detailVue.tgpf_fundAppropriated_afDtltab = jsonObj.tgpf_fundAppropriated_afDtlList;
                detailVue.tgpf_fundAppropriatedtab = jsonObj.tgpf_FundAppropriatedList;
			}
		});
	}
	
	
	//合计
    function getSummaries(param)
    {        //param 是固定的对象，里面包含 columns与 data参数的对象 {columns: Array[4], data: Array[5]},包含了表格的所有的列与数据信息
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
            if(index == 6)//|| index == 4 || index == 5 ||
            {
                sums[index] = 0.0;
                for (var i = 0; i < data.length; i++)
                {
                    var row = data[i];
                    console.log(row);
                    var value = 0.00;
                    switch (index)
                    {
                        case 6 :
                            value = row["appliedAmountOld"];
                            break;
                    }
                    if(!isNaN(value))
                    {
                    	console.log(value)
                        sums[index] += (value - 0);
                    }
                }
                switch (index)
                {
                    case 6 :
                    	detailVue.sumAppliedPayoutAmount = sums[index];
                        sums[index] = thousandsToTwoDecimal(sums[index]);
                        break;
                  
                 
                }
            }

            
            
        }
        return sums;
    }
    
    function getSummariesFund(param){
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
            if(index == 6)//|| index == 4 || index == 5 ||
            {
                sums[index] = 0.0;
                for (var i = 0; i < data.length; i++)
                {
                    var row = data[i];
                    console.log(row);
                    var value = 0.00;
                    switch (index)
                    {
                        case 6 :
                            value = row["currentPayoutAmountOld"];
                            break;
                    }
                    if(!isNaN(value))
                    {
                        sums[index] += (value - 0);
                    }
                }
                switch (index)
                {
                    case 6 :
                    	detailVue.sumCurrentPayoutAmount = sums[index];
                        sums[index] = thousandsToTwoDecimal(sums[index]);
                        break;
                  
                 
                }
            }

            
            
        }
        return sums;
    }
    
    
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tgpf_FundOverallPlanDiv",
	"detailInterface":"../Tgpf_FundOverallPlanDetail",
});
