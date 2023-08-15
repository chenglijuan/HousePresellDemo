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
			isSignatureList : [
	            {tableId:"1",theName:"是"},
	            {tableId:"0",theName:"否"}
	        ],
	        isEncryptList : [
	        	{tableId:"1",theName:"是"},
            	{tableId:"0",theName:"否"}
	        ],
	        busiStateList : [
	        	{tableId:"1",theName:"是"},
	            {tableId:"0",theName:"否"}
	        ],
	        idTypeList : [
	        	{tableId:"1",theName:"居民身份证"},
	            {tableId:"2",theName:"护照"},
	            {tableId:"3",theName:"军官证"},
	            {tableId:"4",theName:"港澳台居民通行证"},
	            {tableId:"5",theName:"户口簿"},
	            {tableId:"6",theName:"其他证件"}
	        ]
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
			saveClose3: saveClose,
			cancelClose3: cancelClose,
			getisSignatureId : function(data){
				updateVue.sm_UserModel.isSignature = data.tableId;
			},
			emptyisSignatureId : function(){
				updateVue.sm_UserModel.isSignature = null;
			},
			idTypeChange : function(data){
				updateVue.sm_UserModel.idType = data.tableId;
			},
			emptyIdType : function(){
				updateVue.sm_UserModel.idType = null;
			},
			busiStateChange : function(data){
				updateVue.sm_UserModel.busiState = data.tableId;
			},
			emptyBusiState : function(){
				updateVue.sm_UserModel.busiState = null;
			},
			getIsEncryptId : function(data){
				console.log(data);
				updateVue.sm_UserModel.isEncrypt = data.tableId;
				
				updateVue.sm_UserModel.isSignature = "";
				updateVue.ukeyNumber = "";
			},
			emptyIsEncryptId : function(){
				updateVue.sm_UserModel.isEncrypt = null;
			},
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
            elem: '#effectiveEditDate',
            range:true
        });
    	
        getIdFormTab("",function (id) {
        	updateVue.tableId=id
            refresh()
        })
        
        getCompanyList();
        generalLoadFile2(updateVue, updateVue.busiType)
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
				generalErrorModal(jsonObj);
			}
			else
			{
				updateVue.sm_UserModel = jsonObj.sm_User;
				updateVue.sm_UserModel.isEncrypt = updateVue.sm_UserModel.isEncrypt + "";
				updateVue.ukeyNumber = jsonObj.sm_User.ukeyNumber;
				updateVue.developCompanyId = jsonObj.sm_User.developCompanyId;
				updateVue.developCompanyType = jsonObj.sm_User.developCompanyType;
				getSm_PermissionRoleList();
				updateVue.sm_Permission_RoleIdArr = jsonObj.sm_User.sm_Permission_RoleIdList;
				document.getElementById("effectiveEditDate").value = jsonObj.sm_User.effectiveDateStr == null ? "":jsonObj.sm_User.effectiveDateStr;
			}
		});
	}
	
	//获取企业列表
	function getCompanyList()
	{
		var model = {
			interfaceVersion : 19000101,
			orgMember : "orgMember",
		};
		new ServerInterface(baseInfo.companyListInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
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
            effectiveDateStr:document.getElementById("effectiveEditDate").value,
			companyId:updateVue.developCompanyId,
			phoneNumber:updateVue.sm_UserModel.phoneNumber,
            busiType:this.busiType,
            ukeyNumber: updateVue.ukeyNumber,
            email:updateVue.sm_UserModel.email,
            theAccount: updateVue.sm_UserModel.theAccount,
			generalAttachmentList:this.$refs.listenUploadData.uploadData,
			sm_Permission_RoleIdArr:this.sm_Permission_RoleIdArr,
			isSignature : updateVue.sm_UserModel.isSignature,
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
	    			generalErrorModal(jsonObj);
	    		}
	    		else
	    		{
	    			generalSuccessModal();
	            	enterNewTabCloseCurrent(updateVue.tableId, '机构用户详情', 'sm/Sm_OrgUserDetail.shtml')
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
	    			generalErrorModal(jsonObj);
	    		}
	    		else
	    		{
	    			generalSuccessModal();
	            	enterNewTabCloseCurrent(updateVue.tableId, '机构用户详情', 'sm/Sm_OrgUserDetail.shtml')
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
            	generalErrorModal(jsonObj);
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
				generalErrorModal(jsonObj);
			}
			else
			{
				generalSuccessModal();
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
				generalErrorModal(jsonObj);
			}
			else
			{
				generalSuccessModal();
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
//    	console.log(outdata);
//    	if(outdata == "") {
//			generalErrorModal("","请检查KEY是否正确插入或者是否授权!");
//		}else {
//			updateVue.ukeyNumber = outdata;
//			console.log(updateVue.ukeyNumber);
//		}
    	
    	var rv;
		id = TZKeyOcxOrgUserEdit.Tz_SetVenderID(6);
		phKey = TZKeyOcxOrgUserEdit.TZ_Connect(-1,0);
		console.log("phKey="+phKey);
		if(phKey == 0)
		{
			generalErrorModal("","请检查KEY是否正确插入或者是否授权!");
		}
		else
		{
			rv = TZKeyOcxOrgUserEdit.Tz_ReadKeyID(phKey);
			updateVue.ukeyNumber = rv;
		}
    	
    }
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
    updateVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#sm_OrgUserEditDiv",
	"warnM":"#WarningM3",
	"detailInterface":"../Sm_OrgUserDetail",
	"updateInterface":"../Sm_OrgUserUpdate",
	"companyListInterface":"../Emmp_CompanyInfoForSpecialSelect",
	"getRoleInterface":"../Sm_Permission_RoleForSelect",
	"unLockInterface":"../Sm_UserUnLock",
	"resetPwdInterface":"../Sm_UserResetPwd"
});
