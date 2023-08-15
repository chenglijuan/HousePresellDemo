(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
            sm_MessageTemplate_CfgModel: {},
            roleList:[],
            sm_Permission_RoleList:[],
			tableId : 1,
            selectItem:[],
            theState:0,
            //角色启用 0：启用 ，1：禁用
            busiType:0,
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
            sm_MessageTemplate_CfgEdit:sm_MessageTemplate_CfgEdit,
		},
		computed:{
			 
		},
		components : {

		},
		watch:{
			
		}
	});

	//------------------------方法定义-开始------------------//

    function initData()
    {
        detailVue.tableId = parent.getTabElementUI_TableId();
        refresh();
    }

	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:detailVue.interfaceVersion,
			tableId:detailVue.tableId,
		}
	}

	//详情操作--------------
	function refresh()
	{
		if (detailVue.tableId == null || detailVue.tableId < 1)
		{
			return;
		}

		getDetail();
	}

	function getDetail()
	{
		new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
                parent.generalErrorModal(jsonObj);
			}
			else
			{
				detailVue.sm_MessageTemplate_CfgModel = jsonObj.sm_MessageTemplate_CfgDetail;
				detailVue.roleList = detailVue.sm_MessageTemplate_CfgModel.roleList;
				for (var i = 0 ;i< detailVue.roleList.length;i++)
                {
                    detailVue.selectItem.push(detailVue.roleList[i].tableId);
                }
			}
		});
	}

    //修改
    function sm_MessageTemplate_CfgEdit()
    {
        var tableId = detailVue.tableId;
        parent.enterNewTabAndCloseCurrent(tableId,"修改消息模板配置",'sm/Sm_MessageTemplate_CfgEdit.shtml');
    }

	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	//detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#sm_MessageTemplate_CfgDetailDiv",
	"detailInterface":"../../Sm_MessageTemplate_CfgDetail",
    "listInterface":"../../Sm_Permission_RoleList"
});
