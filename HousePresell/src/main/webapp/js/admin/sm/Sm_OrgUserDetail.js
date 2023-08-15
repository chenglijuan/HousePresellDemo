(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			sm_UserModel: {},
			tableId : 1,
			busiType : '010101',
			loadUploadList: [],
			showDelete : false,
			sm_Permission_RoleIdArr : [],
			showPwd : true,
			hidePwd : false,
			torpType : "password",
		},
		methods : {
			//详情
			refresh : refresh,
			initData : initData,
			sm_UserEdit : sm_UserEdit,
			getSearchForm : getSearchForm,
			showPwdEvent : showPwdEvent,
			hidePwdEvent : hidePwdEvent,
		},
		computed:{
			 
		},
		components : {
			"my-uploadcomponent":fileUpload,
		},
		watch:{
			
		}
	});

	//------------------------方法定义-开始------------------//

	function initData()
    {
    	getIdFormTab("",function (id) {
            detailVue.tableId=id
            refresh()
        })
        generalLoadFile2(detailVue, detailVue.busiType)
    }
	
	function refresh()
	{
		if (detailVue.tableId == null || detailVue.tableId < 1)
		{
			return;
		}

		getDetail();
	}

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
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				detailVue.sm_UserModel = jsonObj.sm_User;
				detailVue.sm_Permission_RoleIdArr = jsonObj.sm_User.sm_Permission_RoleIdList;
			}
		});
	}

    function sm_UserEdit()
    {
        enterNewTabCloseCurrent(detailVue.tableId, "用户信息修改", "sm/Sm_OrgUserEdit.shtml")
    }

	//显示密码
	function showPwdEvent()
	{
		detailVue.torpType = "text";
		detailVue.showPwd = false;
		detailVue.hidePwd = true;
	}
	//隐藏密码
	function hidePwdEvent()
	{
		detailVue.torpType = "password";
		detailVue.showPwd = true;
		detailVue.hidePwd = false;
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#sm_OrgUserDetailDiv",
	"detailInterface":"../Sm_OrgUserDetail"
});
