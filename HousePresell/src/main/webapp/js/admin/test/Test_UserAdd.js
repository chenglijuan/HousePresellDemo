(function(baseInfo){
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			sm_UserModel: {
				busiState : 1
			},
			developCompanyId : "",
			developCompanyType : "",
			emmp_CompanyInfoList : [],
			busiType : '010101',
			loadUploadList: [],
			showDelete : true,
			//角色列表
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			sm_Permission_RoleList : [],
			sm_Permission_RoleIdArr : [],
			showPwd : true,
			hidePwd : false,
			torpType : "password",
			ukeyNumber: "",
			theCompanyName : "",
		},
		methods : {
			//详情
			initData: initData,
			getSearchForm : getSearchForm,
			//添加
			getAddForm : getAddForm,
			addSm_User: addSm_User,
			getDevelopCompanyId : function (data){
				this.developCompanyId = data.tableId;
				this.developCompanyType = data.theType;
				getSm_PermissionRoleList();
			},
			//角色列表
			changePageNumber : function(data){
				addVue.pageNumber = data;
			},
			listItemSelectHandle : listItemSelectHandle,
			indexMethod: indexMethod,
			showPwdEvent : showPwdEvent,
			hidePwdEvent : hidePwdEvent,
			getIkey : getIkey,
			saveClose1: saveClose,
			cancelClose1: cancelClose
		},
		computed:{
			 
		},
		components : {
			'vue-select': VueSelect,
			"my-uploadcomponent":fileUpload,
		},
		watch:{
			
		}
	});

	//------------------------方法定义-开始------------------//
	
	//------------------------角色相关-开始------------------//
	function listItemSelectHandle(list) 
	{
		generalListItemSelectHandle(addVue,list)
	}
	
	function indexMethod(index)
	{
		return generalIndexMethod(index, addVue);
	}
	
	//获取角色列表
    function getSm_PermissionRoleList()
	{
        var model={
            interfaceVersion:addVue.interfaceVersion,
            theState:0,
            busiType:"0",//启用
            companyType:addVue.developCompanyType
        }
        new ServerInterfaceSync(baseInfo.getRoleInterface).execute(model, function(jsonObj){
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
	//------------------------角色相关-结束------------------//
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
		}
	}

	//详情更新操作--------------获取"更新机构详情"参数
	function getAddForm()
	{
		return {
            interfaceVersion:addVue.interfaceVersion,
            busiState:addVue.sm_UserModel.busiState,
            isEncrypt:addVue.sm_UserModel.isEncrypt,
            companyId:addVue.developCompanyId,
            theName:addVue.sm_UserModel.theName,
            idType:addVue.sm_UserModel.idType,
            idNumber:addVue.sm_UserModel.idNumber,
            phoneNumber:addVue.sm_UserModel.phoneNumber,
            loginPassword:addVue.sm_UserModel.loginPassword,
            endTimeStampStr:document.getElementById("date0101010101").value,
            busiType : this.busiType,
            ukeyNumber: addVue.ukeyNumber,
            email:addVue.sm_UserModel.email,
            theAccount: addVue.sm_UserModel.theAccount,
			generalAttachmentList : this.$refs.listenUploadData.uploadData,
			sm_Permission_RoleIdArr : this.sm_Permission_RoleIdArr,
		}
	}

    function addSm_User()
    {
    	if(addVue.sm_Permission_RoleIdArr.length == 0) {
    		$(baseInfo.warnM).modal({
			    backdrop :'static'
		    });
    	}else {
	    	new ServerInterfaceJsonBody(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj){
	    		if(jsonObj.result != "success")
	    		{
	    			parent.generalErrorModal(jsonObj);
	    		}
	    		else
	    		{
	    			parent.generalSuccessModal();
	    			var tableId = jsonObj.sm_User.tableId;
	            	parent.enterNewTabAndCloseCurrent(tableId, '用户详情', 'test/Test_UserDetail.shtml',tableId+"010101");
	    		}
	    	});
    	}
    }
    
    function saveClose() {
    	new ServerInterfaceJsonBody(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj){
    		if(jsonObj.result != "success")
    		{
    			parent.generalErrorModal(jsonObj);
    		}
    		else
    		{
                parent.generalSuccessModal();
    			var tableId = jsonObj.sm_User.tableId;
                parent.enterNewTabAndCloseCurrent(tableId, '用户详情', 'test/Test_UserDetail.shtml',tableId+"010101");
    		}
    	});
    	$(baseInfo.warnM).modal('hide');
    }
    
    function cancelClose() {
    	$(baseInfo.warnM).modal('hide');
    }
    
    //获取企业列表
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
				addVue.theCompanyName = jsonObj.emmp_CompanyInfoList[0].theName;
				addVue.developCompanyId = jsonObj.emmp_CompanyInfoList[0].tableId;
				addVue.developCompanyType = jsonObj.emmp_CompanyInfoList[0].theType;
				getSm_PermissionRoleList();
				//addVue.emmp_CompanyInfoList = jsonObj.emmp_CompanyInfoList;
			}
		});
	}
	
	function initData()
	{
		laydate.render({
			elem: '#date0101010101',
		});
		var date = new Date();
		var strYear = date.getFullYear() + 1;  
		var strDay = date.getDate();  
		var strMonth = date.getMonth() + 1;
		var data = strYear + "-" + strMonth + "-" + strDay;
		document.getElementById("date0101010101").value = data;
		addVue.sm_UserModel.loginPassword = "000000";
		getCompanyList();
		parent.generalLoadFileTest(addVue, addVue.busiType)
	}
	
	//显示密码
	function showPwdEvent()
	{
		addVue.torpType = "text";
		addVue.showPwd = false;
		addVue.hidePwd = true;
	}
	//隐藏密码
	function hidePwdEvent()
	{
		addVue.torpType = "password";
		addVue.showPwd = true;
		addVue.hidePwd = false;
	}
	
	function getIkey()
    {
//    	var outdata = CInfControlAdd.HD_ReadFileCtl(0, 0x0001);
//    	if(outdata == "") {
//            parent.generalErrorModal("","请检查KEY是否正确插入或者是否授权!");
//		}else {
//			var hash = md5(outdata);
//			addVue.ukeyNumber = hash;
//		}
		
		var rv;
		id = TZKeyOcxTestUserAdd.Tz_SetVenderID(6);
		phKey = TZKeyOcxTestUserAdd.TZ_Connect(-1,0);
		console.log("phKey="+phKey);
		if(phKey == 0)
		{
			generalErrorModal("","请检查KEY是否正确插入或者是否授权!");
		}
		else
		{
			rv = TZKeyOcxTestUserAdd.Tz_ReadKeyID(phKey);
			addVue.ukeyNumber = rv;
		}
		
    }
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	addVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#sm_UserDiv",
	"warnM":"#WarningM1",
	"addInterface":"../../Sm_UserAdd",
	"companyListInterface":"../../Emmp_CompanyInfoForSpecialSelect",
	"getRoleInterface":"../../Sm_Permission_RoleForSelect",
});
