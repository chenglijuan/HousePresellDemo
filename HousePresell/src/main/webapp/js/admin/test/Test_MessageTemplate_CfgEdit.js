(function(baseInfo){
	var updateVue = new Vue({
		el : baseInfo.updateDivId,
		data : {
			interfaceVersion :19000101,
            sm_MessageTemplate_CfgModel: {},
            roleList:[],
            sm_Permission_RoleList:[],
            selectItem:[],
			tableId : 1,
            busiId:"",
            sm_BaseParameterList:[],
            theState:0,
            //角色启用 0：启用 ，1：禁用
            busiType:0,
		},
		methods : {
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
            getSm_BaseParameterForSelect:getSm_BaseParameterForSelect,
            baseParameterChange:baseParameterChange,
			//更新
			getUpdateForm : getUpdateForm,
			getDetail:getDetail,
            getSm_Permission_RoleList:getSm_Permission_RoleList,
            updateSm_MessageTemplate_Cfg: updateSm_MessageTemplate_Cfg,
		},
		computed:{
			 
		},
		components : {
            'vue-select': VueSelect
		},
		watch:{
			
		}
	});

	//------------------------方法定义-开始------------------//

    function initData()
    {
        updateVue.tableId = parent.getTabElementUI_TableId();
        refresh();
    }

	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:updateVue.interfaceVersion,
			tableId:updateVue.tableId,
		}
	}

	//详情操作--------------
	function refresh()
	{
		if (updateVue.tableId == null || updateVue.tableId < 1)
		{
			return;
		}
        getSm_BaseParameterForSelect();
	}

    function getSm_BaseParameterForSelect()
    {
        generalGetParamList("1",function (list) {
            for(var i =0;i<list.length;i++){
                var item=list[i]
                item.theName=item.theValue+"-"+item.theName
            }
            updateVue.sm_BaseParameterList =list
            //消息模板配置详情
            getDetail();
        })
    }

    function baseParameterChange(date)
    {
        updateVue.busiId = date.tableId;
    }

	function getDetail()
	{
		new ServerInterface(baseInfo.detailInterface).execute(updateVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
                parent.generalErrorModal(jsonObj);
			}
			else
			{
				updateVue.sm_MessageTemplate_CfgModel = jsonObj.sm_MessageTemplate_CfgDetail;
                updateVue.busiId =  updateVue.sm_MessageTemplate_CfgModel.busiId;
                updateVue.tableId = updateVue.sm_MessageTemplate_CfgModel.tableId;
                updateVue.roleList = updateVue.sm_MessageTemplate_CfgModel.roleList;
                //获取所有角色信息
                getSm_Permission_RoleList();
			}
		});
	}

    //获取角色信息
    function  getSm_Permission_RoleList()
    {
        var model={
            interfaceVersion:updateVue.interfaceVersion,
            theState:updateVue.theState,
            busiType:updateVue.busiType,
            pageNumber : 1,
            countPerPage : 20,
            totalPage : 1,
            totalCount : 1,
            keyword:"",
        }
        new ServerInterface(baseInfo.listInterface).execute(model, function(jsonObj){
            if(jsonObj.result != "success")
            {
                parent.generalErrorModal(jsonObj);
            }
            else
            {
                updateVue.sm_Permission_RoleList = jsonObj.sm_Permission_RoleList;
                for (var i = 0 ; i< updateVue.roleList.length ;i++)
				{
					for (var j= 0 ; j < updateVue.sm_Permission_RoleList.length  ; j++)
					{
						if(updateVue.sm_Permission_RoleList[j].tableId == updateVue.roleList[i].roleId)
						{
							updateVue.selectItem.push(updateVue.roleList[i].roleId);
							break;
						}
					}
				}
            }
        });
    }

	function getUpdateForm()
	{
		return {
            interfaceVersion:updateVue.interfaceVersion,
            tableId:updateVue.tableId,
            busiId:updateVue.busiId,
            theDescribe:updateVue.sm_MessageTemplate_CfgModel.theDescribe,
            theTitle:updateVue.sm_MessageTemplate_CfgModel.theTitle,
            theContent:updateVue.sm_MessageTemplate_CfgModel.theContent,
            eCode:updateVue.sm_MessageTemplate_CfgModel.eCode,
            theName:updateVue.sm_MessageTemplate_CfgModel.theName,
			idArr:updateVue.selectItem,
		}
	}

    function updateSm_MessageTemplate_Cfg()
    {
    	new ServerInterface(baseInfo.updateInterface).execute(updateVue.getUpdateForm(), function(jsonObj){
    		if(jsonObj.result != "success")
    		{
                parent.generalErrorModal(jsonObj);
    		}
    		else
    		{
                parent.generalSuccessModal();
                parent.enterNewTabAndCloseCurrent(updateVue.tableId,"消息模板配置详情",'sm/Sm_MessageTemplate_CfgDetail.shtml')
    		}
    	});
    }
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	updateVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"updateDivId":"#sm_MessageTemplate_CfgUpdateDiv",
    "getBaseParameterInterface":"../../Sm_BaseParameterForSelect",
	"detailInterface":"../../Sm_MessageTemplate_CfgDetail",
    "listInterface":"../../Sm_Permission_RoleList",
	"updateInterface":"../../Sm_MessageTemplate_CfgUpdate"
});
