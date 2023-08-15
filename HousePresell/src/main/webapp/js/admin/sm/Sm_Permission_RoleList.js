(function(baseInfo){
	var listVue = new Vue({
		el : baseInfo.listDivId,
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
			sm_Permission_RoleList:[],
            upDisabled:true,
            deDisabled:true,
            companyType : "",
            companyTypeList: [],
			busiType : "",
			poName : "Sm_Permission_Role",
			busiTypeList : [
				{tableId:"0",theName:"启用"},
				{tableId:"1",theName:"停用"},
			]
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
            indexMethod:indexMethod,
            listItemSelectHandle: listItemSelectHandle,
            sm_Permission_RoleDetail:sm_Permission_RoleDetail,
            sm_Permission_RoleAdd:sm_Permission_RoleAdd,
            sm_Permission_RoleEdit:sm_Permission_RoleEdit,
            sm_Permission_RoleDel:sm_Permission_RoleDel,
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
			permissionRoleExportExcel : permissionRoleExportExcel,
            onCompanyTypeChange : function(data){
            	listVue.companyType = data.tableId;
            },
            emptyCompanyType : function(){
            	listVue.companyType = null;
            },
            isUsingChange:function(data){
            	listVue.busiType = data.tableId;
            },
            emptyIsUsingChange : function(){
            	listVue.busiType = null;
            },
            sortChange:sortChange,
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue,
			'vue-listselect': VueListSelect,
		},
		watch:{
			pageNumber : refresh,
			selectItem : selectedItemChanged,
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
		getSm_BaseParameterForSelect();
		
		laydate.render({
            elem: '#date01010201',
            range:true
        });
		refresh();
    }
	
	function getSm_BaseParameterForSelect()
	{
	    generalGetParamList("8",function (list) 
	    {
	    	listVue.companyTypeList  = []
	    	list.forEach(function(item){
	    		var model = {
	    			tableId : item.theValue,
	    			theName : item.theName
	    		}
	    		listVue.companyTypeList.push(model);
	    	})
	    	//listVue.companyTypeList =list;
	    })
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
			companyType:this.companyType == "" ? null : this.companyType,
			busiType:this.busiType == "" ? null : this.busiType,
			enableDateSearchStr:document.getElementById("date01010201").value,
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

	//查询参置空
	function reset()
	{
        this.keyword = "";
        this.companyType = "";
        this.busiType = "";
        $('#date01010201').val("");
        refresh()
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
                listVue.sm_Permission_RoleList=jsonObj.sm_Permission_RoleList;
                listVue.pageNumber=jsonObj.pageNumber;
                listVue.countPerPage=jsonObj.countPerPage;
                listVue.totalPage=jsonObj.totalPage;
                listVue.totalCount = jsonObj.totalCount;
                listVue.keyword=jsonObj.keyword;
                listVue.selectedItem=[];
                //动态跳转到锚点处，id="top"
                // document.getElementById('sm_Permission_RoleListDiv').scrollIntoView();
            }
        });
    }

    //列表索引方法
    function indexMethod(index)
	{
		return generalIndexMethod(index, listVue);
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
    function sm_Permission_RoleAdd()
    {
        enterNewTab("", "新增角色", "sm/Sm_Permission_RoleAdd.shtml");
    }
    //点击“编号”进入详情页面
    function sm_Permission_RoleDetail(tableId)
    {
        enterNewTab(tableId, "角色详情", "sm/Sm_Permission_RoleDetail.shtml");
    }

    //修改
    function sm_Permission_RoleEdit()
    {
        var tableId = listVue.selectItem[0];
        enterNewTab(tableId, "角色修改", "sm/Sm_Permission_RoleEdit.shtml");
    }

    function deletePermission_Role()
	{
		new ServerInterface(baseInfo.batchDeleteInterface).execute(listVue.getDeleteForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				listVue.selectItem = [];
				refresh();
			}
		});
	}
    
    //删除
    function sm_Permission_RoleDel()
    {
    	generalSelectModal(deletePermission_Role, "确认删除");
    }

	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		listVue.isAllSelected = (listVue.sm_Permission_RoleList.length > 0)
		&&	(listVue.selectItem.length == listVue.sm_Permission_RoleList.length);
	}

	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.sm_Permission_RoleList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.sm_Permission_RoleList.forEach(function(item) {
	    		listVue.selectItem.push(item.tableId);
	    	});
	    }
	}
	
    function permissionRoleExportExcel()
	{
		var model = {
			interfaceVersion : 19000101,
			idArr : listVue.selectItem,
			keyword : listVue.keyword,
		};
		
		new ServerInterface(baseInfo.permissionRoleExportExcelInterface).execute(model, function (jsonObj) {
			if (jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				window.location.href=jsonObj.fileDownloadPath;
				listVue.selectItem = [];
			}
		});
	}
	//------------------------方法定义-结束------------------//
	function onCompanyTypeChange() {
    }

    function isUsingChange() {
    }

    function sortChange(column) {
        listVue.orderBy=generalOrderByColumn(column)
        refresh()
    }
	
	//------------------------数据初始化-开始----------------//
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#sm_Permission_RoleListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Sm_Permission_RoleList",
	"addInterface":"../Sm_Permission_RoleAdd",
    "batchDeleteInterface": "../Sm_Permission_RoleBatchDelete",
	"updateInterface":"../Sm_Permission_RoleUpdate",
	"permissionRoleExportExcelInterface":"../Sm_Permission_RoleExportExcel",
});
