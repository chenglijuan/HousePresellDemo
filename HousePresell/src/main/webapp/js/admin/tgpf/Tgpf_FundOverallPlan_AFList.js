(function(baseInfo){
	var listVue = new Vue({
		el : baseInfo.listDivId,
		data : {
			interfaceVersion :19000101,
			pageNumber : 1,
			countPerPage :MAX_VALUE,
			totalPage : 1,
			totalCount : 1,
			theState:0,//正常为0，删除为1
			userStartId:null,
			userStartList:[],
			userRecordId:null,
			userRecordList:[],
			tgpf_FundAppropriated_AFList:[],
			tableId : "",
		},
		methods : {
			refresh : refresh,
			search :search,
            indexMethod:indexMethod,
			initData:initData,
			getSearchForm : getSearchForm,
            tgpf_FundAppropriated_AFDetail:tgpf_FundAppropriated_AFDetail,
			changePageNumber: function (data) {
				if (listVue.pageNumber != data) {
					listVue.pageNumber = data;
					listVue.refresh();
				}
			},
			changeCountPerPage : function (data) {
				if (listVue.countPerPage != data) {
					listVue.countPerPage = data;
					listVue.refresh();
				}
			},
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue
		},
		watch:{
//			pageNumber : refresh,
		},
	});

	//------------------------方法定义-开始------------------//

    function initData()
    {
        getIdFormTab("",function (tableId) {
            listVue.tableId=tableId;
            refresh()
        })
    }
    //列表操作--------------获取"搜索列表"表单参数
    function getSearchForm()
    {
        return {
            interfaceVersion:listVue.interfaceVersion,
            pageNumber:listVue.pageNumber,
            countPerPage:listVue.countPerPage,
            totalPage:listVue.totalPage,
            theState:listVue.theState,
            fundOverallPlanId:listVue.tableId
        }
    }

    //列表操作------------搜索
    function search()
    {
        listVue.pageNumber = 1;
        refresh();
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
                listVue.tgpf_FundAppropriated_AFList=jsonObj.tgpf_FundAppropriated_AFList;
                listVue.pageNumber=jsonObj.pageNumber;
                listVue.countPerPage=jsonObj.countPerPage;
                listVue.totalPage=jsonObj.totalPage;
            }
        });
    }

    function indexMethod(index)
    {
        return generalIndexMethod(index, listVue)
    }

    //跳转方法 - 详情
    function tgpf_FundAppropriated_AFDetail(tableId,projectId)
    {
        var theId = tableId +'_'+projectId;
        enterNewTab(theId, '用款申请详情', 'tgpf/Tgpf_FundAppropriated_AFDetail.shtml');
    }
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	// listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#Tgpf_FundOverallPlan_AFListDiv",
	"listInterface":"../Tgpf_FundAppropriated_AFList",

});
