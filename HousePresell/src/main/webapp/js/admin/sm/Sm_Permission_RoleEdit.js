(function(baseInfo){
	var updateVue = new Vue({
		el : baseInfo.updateDivId,
		data : {
			interfaceVersion :19000101,
			sm_Permission_RoleModel: {},
			tableId : "",
			companyType : "",
			companyTypeValue : "",
			companyTypeList: [],
			busiTypeList : [
				{tableId:"0",theName:"启用"},
				{tableId:"1",theName:"停用"},
			]
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			//更新
			getUpdateForm : getUpdateForm,
            updateSm_Permission_Role: updateSm_Permission_Role,
            update:updateSm_Permission_Role,
            changeTheType: function (data) {
				this.companyType = data.tableId;
				this.companyTypeValue = data.theValue;
			},
			isUsingChange:function(data){
				updateVue.sm_Permission_RoleModel.busiType = data.tableId;
            },
            emptyIsUsingChange : function(){
            	updateVue.sm_Permission_RoleModel.busiType = null;
            },
		},
		computed:{
			 
		},
		components : {
			'vue-select': VueSelect ,
		},
		watch:{
			
		}
	});

	//------------------------方法定义-开始------------------//

    function initData()
    {
    	laydate.render({
            elem: '#date0101020201',
        });
		laydate.render({
            elem: '#date0101020202',
            min: getDayDate(),
        });
        getIdFormTab("",function (id) {
        	updateVue.tableId=id
            refresh()
        })
        getSm_BaseParameterForSelect();
    }

    function getSm_BaseParameterForSelect()
	{
	    generalGetParamList("8",function (list) 
	    {
			updateVue.companyTypeList =list;
	    })
	}
    
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:this.tableId,
		}
	}

	//详情操作--------------
	function refresh()
	{
		if (updateVue.tableId == null || updateVue.tableId < 1)
		{
			return;
		}

		getDetail();
	}

	function getDetail()
	{
		new ServerInterface(baseInfo.detailInterface).execute(updateVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
                updateVue.sm_Permission_RoleModel = jsonObj.sm_Permission_Role;
                updateVue.companyType = jsonObj.sm_Permission_Role.sm_BaseParameter == null ? "" : jsonObj.sm_Permission_Role.sm_BaseParameter.tableId;
                updateVue.companyTypeValue = jsonObj.sm_Permission_Role.sm_BaseParameter == null ? "" : jsonObj.sm_Permission_Role.sm_BaseParameter.theValue;
                document.getElementById("date0101020201").value = jsonObj.sm_Permission_Role.enableDate;
                document.getElementById("date0101020202").value = jsonObj.sm_Permission_Role.downDate;
			}
		});
	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getUpdateForm()
	{
		return {
			interfaceVersion:updateVue.interfaceVersion,
			tableId:updateVue.tableId,
            busiType:updateVue.sm_Permission_RoleModel.busiType,
			theName:updateVue.sm_Permission_RoleModel.theName,
			companyType:updateVue.companyTypeValue,
			enableDateStr:document.getElementById("date0101020201").value,
			downDateStr:document.getElementById("date0101020202").value,
			remark:updateVue.sm_Permission_RoleModel.remark,
		}
	}

    function updateSm_Permission_Role()
    {
        new ServerInterface(baseInfo.updateInterface).execute(updateVue.getUpdateForm(), function(jsonObj)
		{
            if(jsonObj.result != "success")
            {
            	generalErrorModal(jsonObj);
            }
            else
            {
            	generalSuccessModal();
            	enterNewTabCloseCurrent(updateVue.tableId, '角色详情', 'sm/Sm_Permission_RoleDetail.shtml');
            }
        });
    }
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
    updateVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"updateDivId":"#sm_Permission_RoleEditDiv",
	"detailInterface":"../Sm_Permission_RoleDetail",
	"updateInterface":"../Sm_Permission_RoleUpdate"
});
