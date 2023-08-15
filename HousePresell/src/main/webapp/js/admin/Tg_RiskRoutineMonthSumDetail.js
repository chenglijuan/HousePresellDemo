(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
            theState:0,//正常为0，删除为1
            tg_RiskRoutineMonthSumDetail: {},
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
            tg_RiskRoutineCheckInfoList:[
                // {
                //     thenum:'YWBH123456',
                //     thetype:'受限额度变更',
                //     theopinion:'缺少变更原因，没有上传营业执照',
                //     therectify:'1',
                //     thesituation:'2',
                // }
            ],
            bigBusiType:"",
            dateStr:"",
		},
		methods : {
            search : search,
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			indexMethod: indexMethod,
			changePageNumber : function(data){
                console.log(data);
                if (detailVue.pageNumber != data) {
                    detailVue.pageNumber = data;
                    detailVue.refresh();
                }
			},
            changeCountPerPage : function (data) {
                console.log(data);
                if (detailVue.countPerPage != data) {
                    detailVue.countPerPage = data;
                    detailVue.refresh();
                }
            },
		},
		computed:{
			 
		},
		components : {
            'vue-nav' : PageNavigationVue,
		},
		watch:{
			
		}
	});

	//------------------------方法定义-开始------------------//
    //列表操作--------------刷新
    function refresh()
    {
        new ServerInterface(baseInfo.listInterface).execute(detailVue.getSearchForm(), function(jsonObj){
            if(jsonObj.result != "success")
            {
                noty({"text":jsonObj.info,"type":"error","timeout":2000});
            }
            else
            {
                detailVue.tg_RiskRoutineCheckInfoList=jsonObj.tg_RiskRoutineCheckInfoList;
                detailVue.tg_RiskRoutineCheckInfoList.forEach(function(item){
                	if (item.checkResult == 0 && item.isDoPush == 1) {
                        item.needRectification = 1;
					} else {
                        item.needRectification = 0;
					}
                })
                detailVue.pageNumber=jsonObj.pageNumber;
                detailVue.countPerPage=jsonObj.countPerPage;
                detailVue.totalPage=jsonObj.totalPage;
                detailVue.totalCount = jsonObj.totalCount;
                detailVue.keyword=jsonObj.keyword;
                detailVue.selectedItem=[];
                //动态跳转到锚点处，id="top"
                document.getElementById('tg_RiskRoutineCheckInfoList').scrollIntoView();
            }
        });
    }
    //列表操作------------搜索
    function search()
    {
        detailVue.pageNumber = 1;
        refresh();
    }
	function indexMethod(index)
	{
		return generalIndexMethod(index, detailVue);
	}
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
            pageNumber:this.pageNumber,
            countPerPage:this.countPerPage,
            totalPage:this.totalPage,
            bigBusiType:this.bigBusiType,
            dateStr:this.dateStr,
            theState:this.theState,
		}
	}

	function getDetail()
	{
		new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
			    detailVue.tg_RiskRoutineMonthSumDetail = jsonObj.tg_RiskRoutineMonthSumDetail;
			}
		});
	}
	
	function initData()
	{
        getIdFormTab('', function (id) {
            var a = id.split("--");
            detailVue.bigBusiType = a[0];
            detailVue.dateStr = a[1].replace("date"," - ");
            //初始化
            getDetail();
            search();
        });
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tg_RiskRoutineMonthSumDetailDiv",
    "listInterface":"../Tg_RiskRoutineCheckInfoList",
    "detailInterface":"../Tg_RiskRoutineMonthSumDetail",
});
