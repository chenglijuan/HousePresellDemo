(function(baseInfo){
    var listVue = new Vue({
        el : baseInfo.listDivId,
        data : {
            interfaceVersion :19000101,
            pageNumber : 1,
            countPerPage :10,
            totalPage : 1,
            totalCount : 1,
            keyword : "",
            applyDate:"",
            applyState:"",
            selectItem : [],
            isAllSelected : false,
            theState:0,//正常为0，删除为1
            tgpf_FundAppropriated_AFList:[],
            upDisabled:true,//修改按钮
            deDisabled:true,//删除按钮
            poName:"Tgpf_FundAppropriated_AF",
            //--------审批详情 start-----//
            approvalDisabled:true,//审批按钮
            busiType:"",
            afId:"",
            workflowId:"",
            sourceId:"",
            //--------审批详情 end-----//
            projectId: "",
            projectList: [],
            //排序
            orderBy:null,
            projectId2 : "",
            applyStateList : [
            	{tableId:"1",theName:"申请中"},
            	{tableId:"2",theName:"已受理"},
            	{tableId:"3",theName:"统筹中"},
            	{tableId:"4",theName:"已统筹"},
            	{tableId:"5",theName:"拨付中"},
            	{tableId:"6",theName:"已拨付"},
            	{tableId:"9",theName:"已撤销"},
            ],
            applyType : "",//申请类型
        },
        methods : {
            refresh : refresh,
            initData:initData,
            getSearchForm : getSearchForm,
            getDeleteForm:getDeleteForm,
            search:search,    //搜索资源列表
            reset:reset,      //重置
            handleSelectionChange: handleSelectionChange,
            indexMethod:indexMethod,
            //----------------筛选---------------------//
            applyStateChange:function(data){
            	this.applyState = data.tableId;
            },
            applyStateChangeEmpty : function(){
            	this.applyState = null;
            },
            changeprojectListener: changeprojectListener,
            changeProjectEmpty: changeProjectEmpty,
            //----------------筛选---------------------//

            //----------------排序---------------------//
            sortChange:sortChange,
            //----------------排序---------------------//
            tgpf_FundAppropriated_AFAdd:tgpf_FundAppropriated_AFAdd,//新增
            tgpf_FundAppropriated_AFEdit:tgpf_FundAppropriated_AFEdit,//修改
            tgpf_FundAppropriated_AFDetail:tgpf_FundAppropriated_AFDetail, //用款申请详情
            tgpf_FundAppropriated_AFDel : tgpf_FundAppropriated_AFDel, //删除
            approvalDetail:approvalDetail,//审批详情
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
            'vue-listselect': VueListSelect
        },
        watch:{
//            pageNumber : refresh,
            selectItem : selectedItemChanged,
        },
        mounted: function ()
        {
            laydate.render({
                elem: '#date0612030101'
            })
        }
    });

	//------------------------方法定义-开始------------------//
  //根据按钮权限，动态控制，该页面的所有“可控按钮”的显示与否
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}
    function initData()
    {
        initButtonList();
        getProjectList();
        var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
        var array = tableIdStr.split("_");
        if (array.length == 5)
        {
            listVue.projectId = Number(array[array.length-1]);
        }
        refresh();
    }

    function getProjectList() {
        serverRequest("../Empj_ProjectInfoForSelect",getTotalListForm(),function (jsonObj) {
            listVue.projectList=jsonObj.empj_ProjectInfoList;
        })
    }

    //重置
    function  reset()
    {
        listVue.keyword="";
        $('#date0612030101').val("");
        listVue.projectId = "";
        listVue.applyState = "";
        listVue.orderBy = "";
        refresh();
    }

    function changeprojectListener(data) {
        if (listVue.projectId != data.tableId) {
            listVue.projectId = data.tableId
            // listVue.refresh()
        }
    }

    function changeProjectEmpty() {
        if (listVue.projectId != null) {
            listVue.projectId = null
            // listVue.refresh()
        }
    }

    //排序
    function  sortChange(column,prop,order)
    {
        listVue.orderBy = generalOrderByColumn(column);
        search();
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
            keyword:listVue.keyword,
            projectId:listVue.projectId,
            applyDate:$('#date0612030101').val(),
            applyState:listVue.applyState,
            orderBy:listVue.orderBy,
            applyType : listVue.applyType
        }
    }

    function getDeleteForm()
    {
        return{
            interfaceVersion:listVue.interfaceVersion,
            idArr:listVue.selectItem
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
                listVue.totalCount = jsonObj.totalCount;
                listVue.keyword=jsonObj.keyword;
                listVue.selectedItem=[];
            }
        });
    }

    //列表索引方法
    function indexMethod(index)
    {
        return generalIndexMethod(index, listVue)
    }

    //获取选中复选框所在行的tableId
    function handleSelectionChange(val)
    {
        //修改按钮禁用状态
        if(val.length == 1 && val[0].approvalState == "待提交")
        {
            listVue.upDisabled = false;
        }
        else
        {
            listVue.upDisabled = true;
        }

        //删除按钮禁用状态
        if(val.length >= 1)
        {
            var count = 0;
            for (var i = 0; i < val.length; i++)
            {
                if(val[i].approvalState != "待提交" )
                {
                    count++
                }
            }
            if(count == 0)
            {
                listVue.deDisabled = false;
            }
            else
            {
                listVue.deDisabled = true;
            }
        }
        else
        {
            listVue.deDisabled = true;
        }

        //审批情况禁用状态
        if(val.length == 1)
        {
            listVue.approvalDisabled = false;
            listVue.sourceId = val[0].tableId;
            listVue.afId = val[0].afId;
            listVue.workflowId = val[0].workflowId;
            listVue.busiType = val[0].busiType;
        }
        else
        {
            listVue.approvalDisabled = true;
        }

        listVue.selectItem = [];
        for (var index = 0; index < val.length; index++)
        {
            var element = val[index].tableId;
            listVue.selectItem.push(element);
        }
        if(val.length == 1)
        {
            listVue.projectId2 = val[0].projectId;
        }
    }

    //跳转方法 - 新增
    function tgpf_FundAppropriated_AFAdd()
    {
        enterNewTab('', '新增用款申请', 'tgpf/Tgpf_FundAppropriated_AFAdd.shtml');
    }

    //跳转方法 - 修改
    function tgpf_FundAppropriated_AFEdit()
    {
        var tableId = listVue.selectItem[0];
        var projectId = listVue.projectId2;
        var theId = tableId +'_'+projectId;
        enterNewTab(theId, '用款申请修改', 'tgpf/Tgpf_FundAppropriated_AFEdit.shtml');
    }

    //跳转方法 - 详情
    function tgpf_FundAppropriated_AFDetail(tableId,projectId)
    {
        var theId = tableId +'_'+projectId;
        enterNewTab(theId, '用款申请详情', 'tgpf/Tgpf_FundAppropriated_AFDetail.shtml');
    }

    //删除
    function tgpf_FundAppropriated_AFDel()
    {
        generalDeleteModal(listVue,listVue.poName);
    }

    //审批情况
    function approvalDetail()
    {
        approvalDetailFromHome(listVue.busiType,listVue.sourceId,listVue.afId,listVue.workflowId,2);
    }

    //选中状态有改变，需要更新“全选”按钮状态
    function selectedItemChanged()
    {
        listVue.isAllSelected = (listVue.tgpf_FundAppropriated_AFList.length > 0)
            &&	(listVue.selectItem.length == listVue.tgpf_FundAppropriated_AFList.length);
    }

    //列表操作--------------“全选”按钮被点击
    function checkAllClicked()
    {
        if(listVue.selectItem.length == listVue.tgpf_FundAppropriated_AFList.length)
        {
            listVue.selectItem = [];
        }
        else
        {
            listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
            listVue.tgpf_FundAppropriated_AFList.forEach(function(item) {
                listVue.selectItem.push(item.tableId);
            });
        }
    }

	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
    "listDivId":"#tgpf_FundAppropriated_AFListDiv",
    "listInterface":"../Tgpf_FundAppropriated_AFList",
    "addInterface":"../Tgpf_FundAppropriated_AFAdd",
    "deleteInterface":"../Tgpf_FundAppropriated_AFDelete",
    "batchDeleteInterface": "../Tgpf_FundAppropriated_AFBatchDelete",
    "updateInterface":"../Tgpf_FundAppropriated_AFUpdate"
});