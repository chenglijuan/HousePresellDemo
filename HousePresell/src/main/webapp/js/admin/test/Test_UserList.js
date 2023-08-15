(function(baseInfo){
	var listVue = new Vue({
		el : '#sm_UserListDiv',
		data : {
			interfaceVersion :19000101,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			keyword : "",
			selectItem : [],
			isAllSelected : false,
			theState:0,//正常为0，删除为1
			sm_UserList:[],
			upDisabled:true,
			deDisabled:true,
			developCompanyId : "",
			emmp_CompanyInfoName:"",
			emmp_CompanyInfoList : [],
			isEncrypt : "",
			busiState : "",
			applyState : "",
			poName : "Sm_User",

            orderBy:"",
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
            getDeleteForm:getDeleteForm,
            listItemSelectHandle: listItemSelectHandle,
            indexMethod:indexMethod,
            sm_UserDetail:sm_UserDetail,
            sm_UserAdd:sm_UserAdd,
            sm_UserEdit:sm_UserEdit,
			sm_UserDel : sm_UserDel,
			search:search,
			reset:reset,
			checkAllClicked : checkAllClicked,
			changePageNumber : function(data){
				listVue.pageNumber = data;
			},
			changeCountPerPage : function (data) {
				if (listVue.countPerPage != data) {
					listVue.countPerPage = data;
					listVue.refresh();
				}
			},
			userExportExcel : userExportExcel,

            changeCompanyListener:changeCompanyListener,
            changeCompanyEmpty:changeCompanyEmpty,

            onChangeEncrypt:onChangeEncrypt,
            onChangeUsing:onChangeUsing,
            onChangeLock:onChangeLock,
            sortChange:sortChange,
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue,
            'vue-listselect': VueListSelect
		},
		watch:{
//			pageNumber : refresh,
			selectItem : selectedItemChanged,
		}
	});

	//------------------------方法定义-开始------------------//

    function initData()
    {
    	// initButtonList();
    	getCompanyList();
    }
    //根据按钮权限，动态控制，该页面的所有“可控按钮”的显示与否
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}
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
			companyId:this.developCompanyId == "" ? null : this.developCompanyId,
			isEncrypt:this.isEncrypt == "" ? null : this.isEncrypt,
			busiState:this.busiState == "" ? null : this.busiState,
			applyState:this.applyState == "" ? null : this.applyState,
			orderBy:this.orderBy,
		}
	}

    //列表操作--------------获取“删除资源”表单参数
    function getDeleteForm()
    {
        return{
            interfaceVersion:this.interfaceVersion,
            idArr:this.selectItem
        }
    }

    function  reset()
    {
    	this.keyword = "";
    	this.isEncrypt = "";
    	this.busiState = "";
    	// this.developCompanyId=""; 只能看见正泰用户
        this.applyState="";
		refresh();
    }

    //列表操作------------搜索
    function search()
    {
        listVue.pageNumber = 1;
        refresh();
    }

    function indexMethod(index)
    {
        return  generalIndexMethod(index, listVue)
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

    //选中状态有改变，需要更新“全选”按钮状态
    function selectedItemChanged()
    {
        listVue.isAllSelected = (listVue.sm_UserList.length > 0)
            &&	(listVue.selectItem.length == listVue.sm_UserList.length);
    }

    //新增
    function sm_UserAdd() {
        parent.enterNewTabTest("", "新增用户信息", "test/Test_UserAdd.shtml");
    }

    //点击“编号”进入详情页面
    function sm_UserDetail(tableId)
    {
        parent.enterNewTabTest(tableId, "用户详情", "test/Test_UserDetail.shtml");
    }

    //修改
    function sm_UserEdit()
    {
        var tableId = listVue.selectItem[0];
        parent.enterNewTabTest(tableId, "用户信息修改", "test/Test_UserEdit.shtml");
    }

    function deleteUser()
	{
		new ServerInterface(baseInfo.batchDeleteInterface).execute(listVue.getDeleteForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				parent.generalErrorModal(jsonObj);
			}
			else
			{
                parent.generalSuccessModal(jsonObj);
				listVue.selectItem = [];
				refresh();
			}
		});
	}
    
    //删除
    function sm_UserDel()
    {
    	parent.generalSelectModal(deleteUser, "确认删除");
    }

	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.sm_UserList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.sm_UserList.forEach(function(item) {
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
                parent.generalErrorModal(jsonObj);
			}
			else
			{
				listVue.sm_UserList=jsonObj.sm_UserList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				// document.getElementById('sm_UserListDiv').scrollIntoView();
			}
		});
	}

	//获取机构列表
	function getCompanyList()
	{
		var model = {
			interfaceVersion : 19000101,
		};
		new ServerInterface(baseInfo.companyListInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				parent.generalErrorModal(jsonObj);
			}
			else
			{
				listVue.emmp_CompanyInfoList = jsonObj.emmp_CompanyInfoList;
				listVue.developCompanyId = jsonObj.emmp_CompanyInfoList[0].tableId;
				listVue.emmp_CompanyInfoName = jsonObj.emmp_CompanyInfoList[0].theName;
				refresh();
			}
		});
	}
	
	function userExportExcel()
	{
		var model = {
			interfaceVersion : 19000101,
			idArr : listVue.selectItem,
			keyword : listVue.keyword,
			companyId:listVue.developCompanyId == "" ? null : listVue.developCompanyId,
			isEncrypt:listVue.isEncrypt == "" ? null : listVue.isEncrypt,
			busiState:listVue.busiState == "" ? null : listVue.busiState,
			applyState:listVue.applyState == "" ? null : listVue.applyState,
		};
		
		new ServerInterface(baseInfo.userExportExcelInterface).execute(model, function (jsonObj) {
			if (jsonObj.result != "success")
			{
				parent.generalErrorModal(jsonObj);
			}
			else
			{
				window.location.href=jsonObj.fileDownloadPath;
				listVue.selectItem = [];
				refresh();
			}
		});
	}

	function changeCompanyListener(data) {
		if(listVue.developCompanyId !=date.tabId){
			listVue.developCompanyId=data.tableId
			refresh()
		}
    }

    function changeCompanyEmpty() {
        if (listVue.developCompanyId != null) {
            listVue.developCompanyId = null
            listVue.refresh()
        }
    }

    function onChangeEncrypt() {
		// refresh()
    }

    function onChangeUsing() {
        // refresh()
    }

    function onChangeLock() {
        // refresh()
    }

    function sortChange(column) {
        listVue.orderBy=generalOrderByColumn(column)
        refresh()
    }
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#sm_UserListDiv",
	"listInterface":"../../Sm_UserList",
	"batchDeleteInterface": "../../Sm_UserBatchDelete",
	"companyListInterface":"../../Emmp_CompanyInfoForSpecialSelect",
	"userExportExcelInterface":"../../Sm_UserExportExcel",
});
