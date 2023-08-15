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
			theState:0,//正常为0，删除为1
			sm_MessageTemplate_CfgList:[],
			upDisabled:true,
            deDisabled:true,
            poName:"Sm_MessageTemplate_Cfg",
            orderBy:null,//排序相关
		},
		methods : {
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			getSearchForm : getSearchForm,
            getDeleteForm:getDeleteForm,
            reset:reset,
			listItemSelectHandle: listItemSelectHandle,
            sm_MessageTemplate_CfgAdd:sm_MessageTemplate_CfgAdd,
            sm_MessageTemplate_CfgEdit:sm_MessageTemplate_CfgEdit,
            sm_MessageTemplate_CfgDetail:sm_MessageTemplate_CfgDetail,
            sm_MessageTemplate_CfgDel:sm_MessageTemplate_CfgDel,
			search:search,
            sortChange:sortChange,
			checkAllClicked : checkAllClicked,
			changePageNumber : function(data){
				listVue.pageNumber = data;
			},
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue
		},
		watch:{
//			pageNumber : refresh,
			selectItem : selectedItemChanged,
		}
	});
	//------------------------方法定义-开始------------------//
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		parent.getButtonList();
	}
    function initData()
    {
    	initButtonList();
		refresh();
    }

    //排序
    function  sortChange(column,prop,order)
    {
        this.orderBy = generalOrderByColumn(column);
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
			keyword:listVue.keyword,
			theState:listVue.theState,
            orderBy:listVue.orderBy,
		}
	}
	//列表操作--------------获取“删除资源”表单参数
	function getDeleteForm()
	{
		return{
			interfaceVersion:listVue.interfaceVersion,
			idArr:listVue.selectItem
		}
	}
    //重置
    function reset()
    {
        listVue.keyword = ""
		refresh();
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
                parent.generalErrorModal(jsonObj);
            }
            else
            {
                listVue.sm_MessageTemplate_CfgList=jsonObj.sm_MessageTemplate_CfgList;
                listVue.pageNumber=jsonObj.pageNumber;
                listVue.countPerPage=jsonObj.countPerPage;
                listVue.totalPage=jsonObj.totalPage;
                listVue.totalCount = jsonObj.totalCount;
                listVue.keyword=jsonObj.keyword;
                listVue.selectedItem=[];
            }
        });
    }

    function indexMethod(index)
    {
        return generalIndexMethod(index, listVue)
    }

    function listItemSelectHandle(list)
    {
        //修改按钮禁用状态
        if(list.length==1)
        {
            listVue.upDisabled = false;
        }
        else
        {
            listVue.upDisabled = true;
        }
        //删除按钮禁用状态
        if(list.length >= 1)
        {
            listVue.deDisabled = false;
        }
        else
        {
            listVue.deDisabled = true;
        }
        generalListItemSelectHandle(listVue,list)
    }

	//新增
    function sm_MessageTemplate_CfgAdd()
	{
        parent.enterNewTabTest("", "新增消息模板配置", "test/Test_MessageTemplate_CfgAdd.shtml");
	}

    //修改
    function sm_MessageTemplate_CfgEdit()
    {
        var tableId = listVue.selectItem[0];
        parent.enterNewTabTest(tableId, "修改消息模板配置", "test/Test_MessageTemplate_CfgEdit.shtml");
    }

    //进入详情
    function sm_MessageTemplate_CfgDetail(tableId)
    {
        parent.enterNewTabTest(tableId, "消息模板配置详情", "test/Test_MessageTemplate_CfgDetail.shtml");
    }

    //删除
    function sm_MessageTemplate_CfgDel()
    {
        parent.generalDeleteModal(listVue,listVue.poName);
    }

	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		listVue.isAllSelected = (listVue.sm_MessageTemplate_CfgList.length > 0)
		&&	(listVue.selectItem.length == listVue.sm_MessageTemplate_CfgList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.sm_MessageTemplate_CfgList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.sm_MessageTemplate_CfgList.forEach(function(item) {
	    		listVue.selectItem.push(item.tableId);
	    	});
	    }
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#sm_MessageTemplate_CfgListDiv",
	"listInterface":"../../Sm_MessageTemplate_CfgList",
	"addInterface":"../../Sm_MessageTemplate_CfgAdd",
	"deleteInterface":"../../Sm_MessageTemplate_CfgDelete",
    "batchDeleteInterface": "../../Sm_MessageTemplate_CfgBatchDelete",
	"updateInterface":"../../Sm_MessageTemplate_CfgUpdate"
});
