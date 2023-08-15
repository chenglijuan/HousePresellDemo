(function(baseInfo){
	var listVue = new Vue({
		el : baseInfo.listDivId,
		data : {
			interfaceVersion :19000101,
            pageNumber : 1,
			countPerPage : 10,
			totalPage : 1,
			totalCount : 1,
			keyword : "",
			selectItem : [],
			isAllSelected : false,
			theState:0, //正常为0，删除为1
			busiState:"审核中",
            //------业务类型start------//
            busiId:"",
            busiCode:"",//业务编码
            sm_BaseParameterList:[],
            //------业务类型end------//
            sm_ApprovalProcess_WorkflowList:[],
            sm_ApprovalProcess_RecordList:[],//审批记录
            orderBy:null,
		},
		methods : {
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			getSearchForm : getSearchForm,
			listItemSelectHandle: listItemSelectHandle,
            getSm_BaseParameterForSelect:getSm_BaseParameterForSelect,
            sortChange:sortChange,
            changeBaseParameter:changeBaseParameter,
            emptyBaseParameter:emptyBaseParameter,
            reset:reset,
			search:search,
            approvalDetail:approvalDetail,
			checkAllClicked : checkAllClicked,
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
			'vue-nav' : PageNavigationVue,
            'vue-listselect': VueListSelect,
		},
		watch:{
//			pageNumber : refresh,
			selectItem : selectedItemChanged,
		}
	});

	//------------------------方法定义-开始------------------//

    function initData()
    {
        laydate.render({
            elem: '#data0103020101',
            range:true,
        })
        getSm_BaseParameterForSelect();
//        refresh();
    }

    function getSm_BaseParameterForSelect()
    {
    	var model={
            interfaceVersion:listVue.interfaceVersion,
            theState:listVue.theState,
            pageNumber : 1,
            countPerPage : MAX_VALUE,
            totalPage : 1,
            totalCount : 1,
            parametertype : "1" // 参数为1,代表业务编码
        }
        new ServerInterface(baseInfo.getBaseParameterInterface).execute(model, function(jsonObj){
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                listVue.sm_BaseParameterList = jsonObj.sm_BaseParameterList;
                refresh();
            }
        });
    }

    //排序
    function  sortChange(column,prop,order)
    {
    	var orderBy = generalOrderByColumn(column);
    	if(orderBy !=null && orderBy.length > 0)
		{
            listVue.orderBy = "approvalProcess_AF."+orderBy;
		}
        search();
    }

    function changeBaseParameter(data)
    {
        if(listVue.busiId != data.tableId)
        {
            listVue.busiId = data.tableId;
            listVue.busiCode = data.theValue;
        }
    }

    function emptyBaseParameter()
    {
        if (listVue.busiId != null || listVue.busiId!="") {
            listVue.busiId = "";
            listVue.busiCode = "";
        }
    }

    //重置
	function reset()
	{
		listVue.busiId = ""
        listVue.busiCode = "";
        listVue.keyword = "";
        listVue.orderBy = "";
        $('#data0103020101').val("");
        refresh();
    }
	 
	//列表操作--------------获取"搜索列表"表单参数
	function getSearchForm()
	{
		return {
			interfaceVersion:listVue.interfaceVersion,
			pageNumber:listVue.pageNumber,
			countPerPage:listVue.countPerPage,
			totalPage:listVue.totalPage,
			keyword:listVue.keyword,
			theState:listVue.theState,
            busiState:listVue.busiState,
			busiCode:listVue.busiCode,
            approvalApplyDate:$('#data0103020101').val(),
            orderBy:listVue.orderBy,
		}
	}

    //列表操作------------搜索
    function search()
    {
        listVue.pageNumber = 1;
        refresh();
    }

    function indexMethod(index) {
        return generalIndexMethod(index, listVue);
    }

    function listItemSelectHandle(list) {
        generalListItemSelectHandle(listVue,list);
    }


    //选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		listVue.isAllSelected = (listVue.sm_ApprovalProcess_WorkflowList.length > 0)
		&&	(listVue.selectItem.length == listVue.sm_ApprovalProcess_WorkflowList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.sm_ApprovalProcess_WorkflowList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.sm_ApprovalProcess_WorkflowList.forEach(function(item) {
	    		listVue.selectItem.push(item.tableId);
	    	});
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
				listVue.sm_ApprovalProcess_WorkflowList=jsonObj.sm_ApprovalProcess_WorkflowList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
			}
		});
	}

	//根据业务类型跳转到对应的页面
	function approvalDetail(busiType,sourceId,afId,workflowId)
	{
        approvalDetailFromHome(busiType,sourceId,afId,workflowId,1);
    }

	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#sm_ApprovalProcess_AgencyListDiv",
//    "getBaseParameterInterface":"../Sm_BaseParameterForSelect",
    "getBaseParameterInterface":"../Sm_BaseParameterForDaiBanList",
	"listInterface":"../Sm_ApprovalProcess_WorkflowList",
});
