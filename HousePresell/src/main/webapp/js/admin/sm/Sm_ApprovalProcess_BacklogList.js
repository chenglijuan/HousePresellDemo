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
			busiState:"" ,
            isReader:"",//未读为0，已读为1
            sm_ApprovalProcess_BacklogList:[],
            readDisabled:true,//已读按钮
            delDisabled:true,//删除按钮
            poName:"Sm_ApprovalProcess_Backlog",
            sm_BaseParameterList:[],
            busiId:"",
            busiCode:"",//业务编码
		},
		methods : {
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			getSearchForm : getSearchForm,
			listItemSelectHandle: listItemSelectHandle,
            sm_ApprovalProcess_BacklogRead:sm_ApprovalProcess_BacklogRead,
            sm_ApprovalProcess_BacklogDel:sm_ApprovalProcess_BacklogDel,
            changeIsReader:changeIsReader,
			search:search,
			reset:reset,
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
			getSm_BaseParameterForSelect : getSm_BaseParameterForSelect,
			changeBaseParameter : changeBaseParameter,
			emptyBaseParameter : emptyBaseParameter,
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
    	getSm_BaseParameterForSelect();
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
            isReader:listVue.isReader,
            busiCode : listVue.busiCode,
		}
	}

	function changeIsReader()
	{
		// refresh();
    }

    function reset()
	{
		listVue.keyword ="";
		listVue.isReader = "";
		listVue.busiId = "";
		listVue.busiCode = "";
		refresh();
    }

    //列表操作------------搜索
    function search()
    {
        listVue.pageNumber = 1;
        refresh();
    }

    function indexMethod(index) {
        return generalIndexMethod(index, listVue)
    }

    function listItemSelectHandle(list) {
    	//已读按钮
    	if(list.length == 1 && list[0].readState == 0)
		{
            listVue.readDisabled = false;
		}
		else
		{
            listVue.readDisabled = true;
		}

		//删除按钮
		if(list.length > 0)
		{
            listVue.delDisabled = false;
		}
		else
		{
            listVue.delDisabled = true;
		}

        generalListItemSelectHandle(listVue,list)
    }


    //选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		listVue.isAllSelected = (listVue.sm_ApprovalProcess_BacklogList.length > 0)
		&&	(listVue.selectItem.length == listVue.sm_ApprovalProcess_BacklogList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.sm_ApprovalProcess_BacklogList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.sm_ApprovalProcess_BacklogList.forEach(function(item) {
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
				listVue.sm_ApprovalProcess_BacklogList=jsonObj.sm_ApprovalProcess_BacklogList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				// listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
			}
		});
	}

	//根据业务类型跳转到对应的页面
	function approvalDetail(busiType,sourceId,afId,workflowId,commonMessageDtlId,readState)
	{
        listVue.selectItem = [];
        listVue.selectItem.push(commonMessageDtlId);
        approvalDetailFromHome(busiType,sourceId,afId,workflowId,2);
        if(readState == 0)
		{
            sm_ApprovalProcess_BacklogRead();
		}
    }

    function sm_ApprovalProcess_BacklogRead()
	{
		var model ={
            interfaceVersion:listVue.interfaceVersion,
			idArr : listVue.selectItem,
		}
        new ServerInterface(baseInfo.updateInterface).execute(model, function(jsonObj){
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                //generalSuccessModal(jsonObj);
                refresh();
            }
        });
	}

	function sm_ApprovalProcess_BacklogDel()
	{
        generalDeleteModal2(listVue,listVue.poName);
	}
	
	//加载业务类型
	 function getSm_BaseParameterForSelect()
	    {
	    	var model={
	            interfaceVersion:listVue.interfaceVersion,
	            theState:"0",
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
	 
	 function changeBaseParameter(data)
	    {
	        if(listVue.busiId != data.tableId)
	        {
	            listVue.busiId = data.tableId;
	            listVue.busiCode = data.theValue;
	            // refresh();
	        }
	    }

	    function emptyBaseParameter()
	    {
	        if (listVue.busiId != null || listVue.busiId!="") {
	            listVue.busiId = "";
	            listVue.busiCode = "";
	            // refresh()
	        }
	    }

	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#sm_ApprovalProcess_BacklogListDiv",
	"listInterface":"../Sm_ApprovalProcess_BacklogList",
    "updateInterface":"../Sm_ApprovalProcess_BacklogUpdate",
    "getBaseParameterInterface":"../Sm_BaseParameterForSelect",
});
