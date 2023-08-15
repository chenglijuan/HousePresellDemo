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
			tg_RiskRoutineCheckRatioConfigList:[],
			ratioConfigChangeFormList:[],
			theRatio : "",
			theRatioList : [10,20,30,40,50,60,70,80,90,100],
			sm_Permission_RoleList : [],
		},
		methods : {
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			getSearchForm : getSearchForm,
			listItemSelectHandle: listItemSelectHandle,
			checkAllClicked : checkAllClicked,
			changePageNumber : function(data){
				listVue.pageNumber = data;
			},
			updateRiskRoutineCheckRatioConfig : updateRiskRoutineCheckRatioConfig,
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
			userStartId:this.userStartId,
			userRecordId:this.userRecordId,
		}
	}
	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		listVue.isAllSelected = (listVue.tg_RiskRoutineCheckRatioConfigList.length > 0)
		&&	(listVue.selectItem.length == listVue.tg_RiskRoutineCheckRatioConfigList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tg_RiskRoutineCheckRatioConfigList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tg_RiskRoutineCheckRatioConfigList.forEach(function(item) {
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
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				listVue.tg_RiskRoutineCheckRatioConfigList=jsonObj.tg_RiskRoutineCheckRatioConfigList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tg_RiskRoutineCheckRatioConfigListDiv').scrollIntoView();
			}
		});
	}
	
	function indexMethod(index) {
		return generalIndexMethod(index, listVue)
	}

	function listItemSelectHandle(list) {
		generalListItemSelectHandle(listVue,list)
 	}

	function initData()
	{
		getSm_PermissionRoleList();
	}
	
	//获取角色列表
    function getSm_PermissionRoleList()
	{
        var model = {
            interfaceVersion:listVue.interfaceVersion,
            theState:0,
            busiType:"0",//启用
        }
        new ServerInterfaceSync(baseInfo.getRoleInterface).execute(model, function(jsonObj){
            if(jsonObj.result != "success")
            {
                noty({"text":jsonObj.info,"type":"error","timeout":2000});
            }
            else
			{
            	listVue.sm_Permission_RoleList = jsonObj.sm_Permission_RoleList;
			}
        });
    }
    
    function updateRiskRoutineCheckRatioConfig()
    {
    	var ratioConfigList = listVue.tg_RiskRoutineCheckRatioConfigList;
    	for(var i=0;i<ratioConfigList.length;i++)
		{
    		listVue.ratioConfigChangeFormList.push({
    			tableId:ratioConfigList[i].tableId,
    			theRatio:ratioConfigList[i].theRatio,
    			roleId:ratioConfigList[i].roleId,
    		})
		}
    	var model={
            interfaceVersion:listVue.interfaceVersion,
            ratioConfigChangeFormList:listVue.ratioConfigChangeFormList,
        }
    	new ServerInterfaceJsonBody(baseInfo.updateInterface).execute(model, function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
            	refresh();
            	listVue.ratioConfigChangeFormList = [];
                generalSuccessModal();
            }
        });
    }
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
    listVue.initData();
    listVue.refresh();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tg_RiskRoutineCheckRatioConfigListDiv",
	"listInterface":"../Tg_RiskRoutineCheckRatioConfigList",
	"getRoleInterface":"../Sm_Permission_RoleForSelect",
	"updateInterface":"../Tg_RiskRoutineCheckRatioConfigUpdate",
});
