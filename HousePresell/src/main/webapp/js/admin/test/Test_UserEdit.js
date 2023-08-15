(function(baseInfo){
	var updateVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			sm_UserModel: {},                
			tableId : "",
			developCompanyId : "",
			developCompanyType : "",
			emmp_CompanyInfoList : [],
			busiType : '010101',
			loadUploadList: [],
			showDelete : true,
			sm_Permission_RoleIdArr : [],
			sm_Permission_RoleList : [],
			oldPwd : "",
			newPwd : "",
			surePwd : "",
			showPwd : true,
			hidePwd : false,
			torpType : "password",
			ukeyNumber: "",
		},
		methods : {
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			getUpdateForm : getUpdateForm,
			updateSm_User: updateSm_User,
			getDevelopCompanyId : function (data){
				this.developCompanyId = data.tableId;
				this.developCompanyType = data.theType;
				getSm_PermissionRoleList();
			},
			getAllCompanyType : function(data)
			{
				this.developCompanyId = "";
				this.developCompanyType = "";
				getSm_PermissionRoleList();
			},
			unLock : unLock,
			resetPassword : resetPassword,
			showPwdEvent : showPwdEvent,
			hidePwdEvent : hidePwdEvent,
			getIkey : getIkey,
			saveClose2: saveClose,
			cancelClose2: cancelClose
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
	//详情操作--------------获取"机构详情"参数

    function initData()
    {
    	laydate.render({
            elem: '#date0101010201',
            range:true
        });
    	
        // getIdFormTab("",function (id) {
        // 	updateVue.tableId=id
        //     refresh()
        // })

        updateVue.tableId = parent.window.getTabElementUI_TableId();
        refresh();
        getCompanyList();
        parent.generalLoadFileTest(updateVue, updateVue.busiType);
    }

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
		getDetail();
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
				updateVue.sm_UserModel = jsonObj.sm_User;
				updateVue.ukeyNumber = jsonObj.sm_User.ukeyNumber;
				updateVue.developCompanyId = jsonObj.sm_User.developCompanyId;
				updateVue.developCompanyType = jsonObj.sm_User.developCompanyType;
				getSm_PermissionRoleList();
				updateVue.sm_Permission_RoleIdArr = jsonObj.sm_User.sm_Permission_RoleIdList;
				document.getElementById("date0101010201").value = jsonObj.sm_User.effectiveDateStr == null ? "":jsonObj.sm_User.effectiveDateStr;
			}
		});
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
				parent.window.generalErrorModal(jsonObj);
			}
			else
			{
				updateVue.emmp_CompanyInfoList = jsonObj.emmp_CompanyInfoList;
			}
		});
	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getUpdateForm()
	{
		return {
			interfaceVersion:updateVue.interfaceVersion,
			tableId:updateVue.tableId,
			idType:updateVue.sm_UserModel.idType,
			loginPassword:updateVue.sm_UserModel.pwd,
			busiState:updateVue.sm_UserModel.busiState,//是否启用
			theName:updateVue.sm_UserModel.theName,
			idNumber:updateVue.sm_UserModel.idNumber,
            isEncrypt:updateVue.sm_UserModel.isEncrypt,//是否加密
            effectiveDateStr:document.getElementById("date0101010201").value,
			companyId:updateVue.developCompanyId,
			phoneNumber:updateVue.sm_UserModel.phoneNumber,
            busiType:this.busiType,
            ukeyNumber: updateVue.ukeyNumber,
            email:updateVue.sm_UserModel.email,
            theAccount: updateVue.sm_UserModel.theAccount,
			generalAttachmentList:this.$refs.listenUploadData.uploadData, //TODO - Glad
			sm_Permission_RoleIdArr:this.sm_Permission_RoleIdArr,
		}
	}

    function updateSm_User()
    {
    	if(updateVue.sm_Permission_RoleIdArr.length == 0) {
    		$(baseInfo.warnM).modal({
			    backdrop :'static'
		    });
    	}else {
	    	new ServerInterfaceJsonBody(baseInfo.updateInterface).execute(updateVue.getUpdateForm(), function(jsonObj){
	    		if(jsonObj.result != "success")
	    		{
                    parent.generalErrorModal(jsonObj);
	    		}
	    		else
	    		{
                    parent.generalSuccessModal(jsonObj);
                    parent.enterNewTabAndCloseCurrent(updateVue.tableId, "用户详情", "test/Test_UserDetail.shtml");
	    		}
	    	});
    	}
    }

    function saveClose() {
    	$(baseInfo.warnM).modal('hide');
    	setTimeout(function(){
    		new ServerInterfaceJsonBody(baseInfo.updateInterface).execute(updateVue.getUpdateForm(), function(jsonObj){
        		if(jsonObj.result != "success")
        		{
                    parent.generalErrorModal(jsonObj);
        		}
        		else
        		{
                    parent.generalSuccessModal();
                    parent.enterNewTabAndCloseCurrent(updateVue.tableId, "用户详情", "test/Test_UserDetail.shtml");
        		}
        	});
    	},300);
    }
    
    function cancelClose() {
    	$(baseInfo.warnM).modal('hide');
    }

    function getSm_PermissionRoleList()
	{
        var model={
            interfaceVersion:updateVue.interfaceVersion,
            theState:0,
            busiType:"0",//启用
            companyType:updateVue.developCompanyType,
            chooseUserId:updateVue.tableId,
        }
        new ServerInterfaceSync(baseInfo.getRoleInterface).execute(model, function(jsonObj){
            if(jsonObj.result != "success")
            {
            	parent.generalErrorModal(jsonObj);
            }
            else
			{
                updateVue.sm_Permission_RoleList = jsonObj.sm_Permission_RoleList;
			}
        });
    }
    
    //解锁
    function unLock()
    {
    	var model = {
			interfaceVersion : 19000101,
			tableId : updateVue.tableId,
		};
    	
		new ServerInterface(baseInfo.unLockInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
                parent.generalErrorModal(jsonObj);
			}
			else
			{
                parent.generalSuccessModal();
				getDetail();
			}
		});
    }
    
    //重置密码
    function resetPassword()
    {
    	var model = {
    		interfaceVersion : 19000101,
    		tableId : updateVue.tableId,
        };
    	
    	new ServerInterface(baseInfo.resetPwdInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
                parent.generalErrorModal(jsonObj);
			}
			else
			{
                parent.generalSuccessModal();
				getDetail();
			}
		});
    }
    
    //显示密码
    function showPwdEvent()
    {
    	updateVue.torpType = "text";
    	updateVue.showPwd = false;
    	updateVue.hidePwd = true;
    }
    //隐藏密码
    function hidePwdEvent()
    {
    	updateVue.torpType = "password";
    	updateVue.showPwd = true;
    	updateVue.hidePwd = false;
    }
    
    function getIkey()
    {
//    	var outdata = CInfControl.HD_ReadFileCtl(0, 0x0001);
//    	if(outdata == "") {
//            parent.generalErrorModal("","请检查KEY是否正确插入或者是否授权!");
//		}else {
//			updateVue.ukeyNumber = outdata;
//		}
    	
    	var rv;
		id = TZKeyOcxTestUserEdit.Tz_SetVenderID(6);
		phKey = TZKeyOcxTestUserEdit.TZ_Connect(-1,0);
		console.log("phKey="+phKey);
		if(phKey == 0)
		{
			generalErrorModal("","请检查KEY是否正确插入或者是否授权!");
		}
		else
		{
			rv = TZKeyOcxTestUserEdit.Tz_ReadKeyID(phKey);
			updateVue.ukeyNumber = rv;
		}
    	
    }
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
    updateVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#sm_UserEditDiv",
	"warnM":"#WarningM2",
	"detailInterface":"../../Sm_UserDetail",
	"updateInterface":"../../Sm_UserUpdate",
	"companyListInterface":"../../Emmp_CompanyInfoForSpecialSelect",
	"getRoleInterface":"../../Sm_Permission_RoleForSelect",
	"unLockInterface":"../../Sm_UserUnLock",
	"resetPwdInterface":"../../Sm_UserResetPwd"
});
