(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			sm_Permission_RoleModel: {},
			tableId : 1,
			//用户列表相关
			pageNumber: 1,
			countPerPage: 10,
			totalPage: 1,
			totalCount: 1,
            sm_UserList:[],
		},
		methods : {
			//详情
			initData : initData,
			getUserList : getUserList,
			getSearchForm : getSearchForm,
            sm_Permission_RoleEdit : sm_Permission_RoleEdit,
            indexMethod : indexMethod,
            changePageNumber : function(data){
            	detailVue.pageNumber = data;
			},
			changeCountPerPage : function (data) {
				if (detailVue.countPerPage != data) {
					detailVue.countPerPage = data;
					detailVue.getUserList();
				}
			},
		},
		computed:{

        },
		components : {
			'vue-nav' : PageNavigationVue,
		},
		watch:{
			pageNumber : getUserList,
		}
	});

	//------------------------方法定义-开始------------------//

	function indexMethod(index)
	{
		return generalIndexMethod(index, detailVue);
	}
	
	//跳转编辑
	function sm_Permission_RoleEdit()
    {
        enterNewTabCloseCurrent(detailVue.tableId, "角色修改", "sm/Sm_Permission_RoleEdit.shtml");
    }
	
	function initData()
    {
		getIdFormTab("",function (id) {
			detailVue.tableId=id
			refresh()
		})
    }
	
	function refresh()
	{
		if (detailVue.tableId == null || detailVue.tableId < 1)
		{
			return;
		}

		getDetail();
		getUserList();
	}

	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:this.tableId,
		}
	}

	function getDetail()
	{
		new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function(jsonObj)
		{
            if(jsonObj.result != "success")
            {
            	generalErrorModal(jsonObj);
            }
            else
            {
                detailVue.sm_Permission_RoleModel = jsonObj.sm_Permission_Role;
            }
		});
	}
	
	function getUserList()
	{
		var model = {
			interfaceVersion:19000101,
			sm_Permission_RoleId:detailVue.tableId,	
			pageNumber:detailVue.pageNumber,
			countPerPage:detailVue.countPerPage,
			totalPage:detailVue.totalPage,
		};
		new ServerInterface(baseInfo.listInterface).execute(model, function(jsonObj)
		{
            if(jsonObj.result != "success")
            {
            	generalErrorModal(jsonObj);
            }
            else
            {
                detailVue.sm_UserList = jsonObj.sm_UserList;
                detailVue.pageNumber=jsonObj.pageNumber;
                detailVue.countPerPage=jsonObj.countPerPage;
                detailVue.totalPage=jsonObj.totalPage;
                detailVue.totalCount = jsonObj.totalCount;
            }
		});
	}
	
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#sm_Permission_RoleDetailDiv",
	"detailInterface":"../Sm_Permission_RoleDetail",
	"listInterface":"../Sm_Permission_RoleUserList",
});
