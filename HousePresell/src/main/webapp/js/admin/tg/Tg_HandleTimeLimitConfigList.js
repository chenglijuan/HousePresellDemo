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
			userStartId:null,
			userStartList:[],
			userRecordId:null,
			userRecordList:[],
			tg_HandleTimeLimitConfigList:[
//				{
//					theType:'托管资金一般拨付',
//					completionStandard:'提交申请至审核完成',
//					limitDayNumber:'3',
//					lastCfgUser:'张三',
//					lastCfgTimeStamp:'2018-09-12',
//				}
			],
            sm_Permission_RoleList:[],
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			search:search,
			changePageNumber : function(data){
				listVue.pageNumber = data;
			},
			getUpdateForm : getUpdateForm,
			update:updateTg_HandleTimeLimitConfig,
            getDepartments : getDepartments,
            getDepartmentSearchForm : getDepartmentSearchForm,
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue
		},
		watch:{
//			pageNumber : refresh,
		}
	});

	//------------------------方法定义-开始------------------//
	 
	//列表操作--------------获取"搜索列表"表单参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
		}
	}
	//列表操作--------------获取“更新”表单参数
	function getUpdateForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			theState:this.theState,
			busiState:this.busiState,
			eCode:this.eCode,
			userStartId:this.userStartId,
			createTimeStamp:this.createTimeStamp,
			lastUpdateTimeStamp:this.lastUpdateTimeStamp,
			userRecordId:this.userRecordId,
			recordTimeStamp:this.recordTimeStamp,
			theType:this.theType,
			completionStandard:this.completionStandard,
			limitDayNumber:this.limitDayNumber,
			counterpartDepartment:this.counterpartDepartment,
			lastCfgUser:this.lastCfgUser,
			lastCfgTimeStamp:this.lastCfgTimeStamp,
		}
	}
    function getDepartmentSearchForm()
    {
        return {
            interfaceVersion:this.interfaceVersion,
            theState:0,
            busiType:"0",//启用

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
				listVue.tg_HandleTimeLimitConfigList=jsonObj.tg_HandleTimeLimitConfigList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tg_HandleTimeLimitConfigListDiv').scrollIntoView();
			}
		});
	}
	
	//列表操作------------搜索
	function search()
	{
		listVue.pageNumber = 1;
		refresh();
	}
	
	function updateTg_HandleTimeLimitConfig()
	{
		var model={
				interfaceVersion:listVue.interfaceVersion,
				tg_HandleTimeLimitConfigs:listVue.tg_HandleTimeLimitConfigList,	
			};
			new ServerInterfaceJsonBody(baseInfo.updateInterface).execute(model, function (jsonObj) {
				if (jsonObj.result != "success")
				{
					noty({ "text": jsonObj.info, "type": "error", "timeout": 2000 });
				}
				else
				{
					$(baseInfo.updateDivId).modal('hide');
					refresh();
				}
			});
	}

    function getDepartments()
    {
        new ServerInterface(baseInfo.getRoleInterface).execute(listVue.getDepartmentSearchForm(), function(jsonObj){
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

	function initData()
	{
        getDepartments();
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tg_HandleTimeLimitConfigListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Tg_HandleTimeLimitConfigList",
	"addInterface":"../Tg_HandleTimeLimitConfigAdd",
	"deleteInterface":"../Tg_HandleTimeLimitConfigDelete",
	"updateInterface":"../Tg_HandleTimeLimitConfigUpdate",
    "getRoleInterface":"../Sm_Permission_RoleForSelect",
});
