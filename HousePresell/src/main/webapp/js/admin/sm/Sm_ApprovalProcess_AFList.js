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
            //------业务类型start------//
            busiId:"",
            busiCode:"",//业务编码
            sm_BaseParameterList:[],
            //------业务类型end------//
            sm_ApprovalProcess_AFList:[], //流程信息
            withdrawDisabled:true,//撤回按钮
            delDisabled:true, //删除按钮
            busiState:"",//业务状态
            sourceId:"",//业务Id (例如楼幢审批，就是楼幢Id)
            poName:"Sm_ApprovalProcess_AF",
            orderBy:"createTimeStamp desc",
            busiStateList : [
            	{tableId:"待提交",theName:"待提交"},
            	{tableId:"审核中",theName:"审核中"},
            	{tableId:"已完结",theName:"已完结"},
            	{tableId:"不通过",theName:"不通过"},
            ]
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
            changeApproval:function(data){
            	this.busiState = data.tableId;
            },
            emptyChangeApproval:function(){
            	this.busiState = "";
            },
            reset:reset,
            search:search,
            approvalDetail:approvalDetail,
            sm_ApprovalProcess_AFWithdraw:sm_ApprovalProcess_AFWithdraw,
            sm_ApprovalProcess_AFDel:sm_ApprovalProcess_AFDel,
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
//            pageNumber : refresh,
            selectItem : selectedItemChanged,
        }
    });

    //------------------------方法定义-开始------------------//
    function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}
    function initData()
    {
        laydate.render({
            elem: '#date0103020301',
            range:true
        });
        initButtonList();
        getSm_BaseParameterForSelect();
       // refresh();
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

    //重置
    function reset()
    {
        listVue.keyword = "";
        listVue.busiId = "";
        listVue.busiCode="";
        listVue.busiState="";
        $('#date0103020301').val("");
        refresh();
    }

    //排序
    function  sortChange(column,prop,order)
    {
        listVue.orderBy = generalOrderByColumn(column);
        search();
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
            orderBy:listVue.orderBy,
            approvalApplyDate:$('#date0103020301').val(),
        }
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

    function listItemSelectHandle(list)
    {
        generalListItemSelectHandle(listVue,list)
        //撤回
        if(list.length == 1 && list[0].busiState == "审核中")
        {
            listVue.withdrawDisabled = false;
        }
        else
        {
            listVue.withdrawDisabled = true;
        }

        //删除
        if(list.length > 0)
        {
            for (var i=0; i < list.length; i++)
            {
                if(list[i].busiState == "待提交")
                {
                    listVue.delDisabled = false;
                }
                else
                {
                    listVue.delDisabled = true;
                }
            }
        }
        else
        {
            listVue.delDisabled = true;
        }
    }

    //选中状态有改变，需要更新“全选”按钮状态
    function selectedItemChanged()
    {
        listVue.isAllSelected = (listVue.sm_ApprovalProcess_AFList.length > 0)
            &&	(listVue.selectItem.length == listVue.sm_ApprovalProcess_AFList.length);
    }
    //列表操作--------------“全选”按钮被点击
    function checkAllClicked()
    {
        if(listVue.selectItem.length == listVue.sm_ApprovalProcess_AFList.length)
        {
            listVue.selectItem = [];
        }
        else
        {
            listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
            listVue.sm_ApprovalProcess_AFList.forEach(function(item) {
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
                listVue.sm_ApprovalProcess_AFList = jsonObj.sm_ApprovalProcess_AFList;
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
    function approvalDetail(busiType,sourceId,currentWorkflowId,afId)
    {
        approvalDetailFromHome(busiType,sourceId,afId,currentWorkflowId,2);
    }

    //撤回
    function sm_ApprovalProcess_AFWithdraw()
    {
        var model = {
            interfaceVersion:listVue.interfaceVersion,
            tableId : listVue.selectItem[0],
        }
        new ServerInterface(baseInfo.reCallInterface).execute(model, function(jsonObj){
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

    //删除
    function sm_ApprovalProcess_AFDel()
    {
        generalDeleteModal2(listVue,listVue.poName);
    }
    //------------------------方法定义-结束------------------//

    //------------------------数据初始化-开始----------------//
    listVue.initData();
    //------------------------数据初始化-结束----------------//
})({
    "listDivId":"#sm_ApprovalProcess_AFListDiv",
    "getBaseParameterInterface":"../Sm_BaseParameterForSelect",
    "listInterface":"../Sm_ApprovalProcess_AFList",
    "reCallInterface":"../Sm_ApprovalProcess_AFRecall",
    "batchDeleteInterface": "../Sm_ApprovalProcess_AFBatchDelete",
});
