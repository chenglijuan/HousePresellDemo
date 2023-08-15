(function(baseInfo){
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
            sm_MessageTemplate_CfgModel: {},
            theState:0,
            //角色启用 0：启用 ，1：禁用
            busiType:0,
            busiId:"",
            sm_BaseParameterList:[],
            deDisabled:false,
            selectItem:[],
            sm_Permission_RoleList:[],
		},
		methods : {
			refresh : refresh,
			initData: initData,
            getSm_BaseParameterForSelect:getSm_BaseParameterForSelect,
            baseParameterChange:baseParameterChange,
			//添加
			getAddForm : getAddForm,
            addSm_MessageTemplate_Cfg:addSm_MessageTemplate_Cfg,
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
        getSm_BaseParameterForSelect();
    }

    function getSm_BaseParameterForSelect()
    {
        generalGetParamList("1",function (list) {
            for(var i =0;i<list.length;i++){
                var item=list[i]
                item.theName=item.theValue+"-"+item.theName
            }
            addVue.sm_BaseParameterList =list;
            refresh();
        })
    }

    function baseParameterChange(date)
    {
        addVue.busiId = date.tableId;
    }

    //获取角色信息
    function  refresh()
    {
        var model={
            interfaceVersion:addVue.interfaceVersion,
            theState:addVue.theState,
            busiType:addVue.busiType,
            pageNumber : addVue.pageNumber,
            countPerPage : addVue.countPerPage,
            totalPage : addVue.totalPage,
            totalCount : addVue.totalCount,
        }
        new ServerInterface(baseInfo.listInterface).execute(model, function(jsonObj){
            if(jsonObj.result != "success")
            {
                parent.generalErrorModal(jsonObj);
            }
            else
            {
                addVue.sm_Permission_RoleList = jsonObj.sm_Permission_RoleList;
            }
        });
    }

    function getAddForm()
	{
		return {
			interfaceVersion:addVue.interfaceVersion,
            busiId:addVue.busiId,
            theDescribe:addVue.sm_MessageTemplate_CfgModel.theDescribe,
            theTitle:addVue.sm_MessageTemplate_CfgModel.theTitle,
            theContent:addVue.sm_MessageTemplate_CfgModel.theContent,
            eCode:addVue.sm_MessageTemplate_CfgModel.eCode,
            theName:addVue.sm_MessageTemplate_CfgModel.theName,
            idArr : addVue.selectItem,
		}
	}

    function addSm_MessageTemplate_Cfg()
    {
        new ServerInterface(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj){
            if(jsonObj.result != "success")
            {
                parent.generalErrorModal(jsonObj);
            }
            else
            {
                parent.generalSuccessModal();
                parent.enterNewTabAndCloseCurrent(jsonObj.tableId,"消息模板配置详情",'sm/Sm_MessageTemplate_CfgDetail.shtml')
            }
        });
    }

	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	//addVue.refresh();
	addVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#sm_MessageTemplate_CfgAddDiv",
    "getBaseParameterInterface":"../../Sm_BaseParameterForSelect",
    "listInterface":"../../Sm_Permission_RoleList",
    "addInterface":"../../Sm_MessageTemplate_CfgAdd",
});
