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
			tg_TotalEscrowAmountForecastList:[]
		},
		methods : {
			refresh : refresh,
			initData : initData,
            saveHandle : saveHandle,
			getSearchForm : getSearchForm,
			search : search,
			changePageNumber : function(data){
				listVue.pageNumber = data;
			},
            clearNoNum :function (obj,theKey){
                // console.log(obj[theKey]);

                var value = obj[theKey];
                value = value.replace(/[^0-9/.]/g,''); //清除“数字”和“.”以外的字符
                value = value.replace(/^\./g,"");  //验证第一个字符是数字而不是.
                value = value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的.
                value = value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");

                // console.log(obj);
                obj[theKey] = value;
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

		return {
			interfaceVersion:this.interfaceVersion,
			pageNumber:this.pageNumber,
			countPerPage:this.countPerPage,
			totalPage:this.totalPage,
			keyword:this.keyword,
			theState:this.theState,
			userStartId:this.userStartId,
			userRecordId:this.userRecordId,
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
				listVue.tg_TotalEscrowAmountForecastList=jsonObj.tg_TotalEscrowAmountForecastList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				//动态跳转到锚点处，id="top"
				document.getElementById('tg_TotalEscrowAmountForecastListDiv').scrollIntoView();
			}
		});
	}

    //列表操作--------------更新表单参数
    function saveHandle()
    {
        var model = {
            interfaceVersion : this.interfaceVersion,
            totalEscrowAmountForecastList : this.tg_TotalEscrowAmountForecastList
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
		listVue.pageNumber = 1;
		refresh();
	}

	function initData()
	{

	}
	//------------------------方法定义-结束------------------//

	//------------------------数据初始化-开始----------------//
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tg_TotalEscrowAmountForecastListDiv",
	"listInterface":"../Tg_TotalEscrowAmountForecastList",
	"updateInterface":"../Tg_TotalEscrowAmountForecastBatchUpdate"
});
