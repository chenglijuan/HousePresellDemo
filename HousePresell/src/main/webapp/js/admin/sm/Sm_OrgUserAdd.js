(function(baseInfo){
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			sm_UserModel: {
				busiState : "1",
				isSignature : "0",
				isEncrypt : ""
			},
			developCompanyId : "",
			developCompanyType : "",
			developCompanyType1 : "",
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
			developCompanyType: "",
			emmp_CompanyInfoTypeList:[],
			type: "",
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
			//详情
			initData: initData,
			getSearchForm : getSearchForm,
			//添加
			getAddForm : getAddForm,
			addSm_User: addSm_User,
			getDevelopCompanyId : function (data){
				this.developCompanyId = data.tableId;
				this.developCompanyType1 = data.theType;
				getSm_PermissionRoleList();
			},
			getDevelopCompanyTypeId : function(data){
				this.developCompanyType = data.tableId;
				addVue.type = data.theValue;
				addVue.developCompanyId = "";
		    	addVue.emmp_CompanyInfoList = [];
				getCompanyList();
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
			saveClose: saveClose,
			cancelClose: cancelClose,
			getisSignatureId : function(data){
				addVue.sm_UserModel.isSignature = data.tableId;
			},
			emptyisSignatureId : function(){
				addVue.sm_UserModel.isSignature = null;
			},
			idTypeChange : function(data){
				addVue.sm_UserModel.idType = data.tableId;
				
			},
			emptyIdType : function(){
				addVue.sm_UserModel.idType = null;
			},
			busiStateChange : function(data){
				addVue.sm_UserModel.busiState = data.tableId;
			},
			emptyBusiState : function(){
				addVue.sm_UserModel.busiState = null;
			},
			isEncryptChange : function(data){
				addVue.sm_UserModel.isEncrypt = data.tableId;
				addVue.sm_UserModel.isSignature = "";
				addVue.ukeyNumber = "";
			},
			emptyIsEncrypt : function(){
				addVue.sm_UserModel.isEncrypt = null;
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
            companyType:addVue.developCompanyType1
        }
        new ServerInterfaceSync(baseInfo.getRoleInterface).execute(model, function(jsonObj){
            if(jsonObj.result != "success")
            {
                noty({"text":jsonObj.info,"type":"error","timeout":2000});
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
            endTimeStampStr:document.getElementById("endAddDate").value,
            busiType : this.busiType,
            ukeyNumber: addVue.ukeyNumber,
            email:addVue.sm_UserModel.email,
            theAccount: addVue.sm_UserModel.theAccount,
			generalAttachmentList : this.$refs.listenUploadData.uploadData,
			sm_Permission_RoleIdArr : this.sm_Permission_RoleIdArr,
			isSignature : addVue.sm_UserModel.isSignature,
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
        			generalErrorModal(jsonObj);
        		}
        		else
        		{
        			generalSuccessModal();
        			var tableId = jsonObj.sm_User.tableId;
                	enterNext2Tab(tableId, '用户详情', 'sm/Sm_OrgUserDetail.shtml',tableId+"010101");
        		}
        	});
    	}
    }
    
    function saveClose() {
    	new ServerInterfaceJsonBody(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj){
    		if(jsonObj.result != "success")
    		{
    			generalErrorModal(jsonObj);
    		}
    		else
    		{
    			generalSuccessModal();
    			var tableId = jsonObj.sm_User.tableId;
            	enterNext2Tab(tableId, '用户详情', 'sm/Sm_OrgUserDetail.shtml',tableId+"010101");
    		}
    	});
    	$(baseInfo.warnM).modal('hide');
    }
    
    function cancelClose() {
    	$(baseInfo.warnM).modal('hide');
    }
    
    // 获取机构类型
    function getCompanyTypeList() {
    	var model = {
    			interfaceVersion : 19000101,
    			parametertype : "8",
    			exceptZhengTai : "exceptZhengTai",
    		};
    		new ServerInterface(baseInfo.companyTypeListInterface).execute(model, function(jsonObj)
    		{
    			if(jsonObj.result != "success")
    			{
    				generalErrorModal(jsonObj);
    			}
    			else
    			{
    				addVue.emmp_CompanyInfoTypeList = jsonObj.sm_BaseParameterList;
    			}
    		});
    }
    
    //获取企业列表
	function getCompanyList()
	{
		var model = {
			interfaceVersion : 19000101,
			orgMember : "orgMember",
			theType: addVue.type,
		};
		new ServerInterface(baseInfo.companyListInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				addVue.emmp_CompanyInfoList = jsonObj.emmp_CompanyInfoList;
			}
		});
	}
	
	function initData()
	{
		laydate.render({
			elem: '#endAddDate',
		});
		var date = new Date();
		var strYear = date.getFullYear() + 1;  
		var strDay = date.getDate();  
		var strMonth = date.getMonth() + 1;
		var data = strYear + "-" + strMonth + "-" + strDay;
		document.getElementById("endAddDate").value = data;
		addVue.sm_UserModel.loginPassword = "";
		getCompanyTypeList();
		//getCompanyList();
		generalLoadFile2(addVue, addVue.busiType)
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
//			generalErrorModal("","请检查KEY是否正确插入或者是否授权!");
//		}else {
//			var hash = md5(outdata);
//			addVue.ukeyNumber = hash;
//		}
		
		var rv;
		id = TZKeyOcxOrgUserAdd.Tz_SetVenderID(6);
		phKey = TZKeyOcxOrgUserAdd.TZ_Connect(-1,0);
		console.log("phKey="+phKey);
		if(phKey == 0)
		{
			generalErrorModal("","请检查KEY是否正确插入或者是否授权!");
		}
		else
		{
			rv = TZKeyOcxOrgUserAdd.Tz_ReadKeyID(phKey);
			addVue.ukeyNumber = rv;
		}
		
    }
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	addVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#sm_OrgUserDiv",
	"warnM":"#WarningM",
	"addInterface":"../Sm_OrgUserAdd",
	"companyListInterface":"../Emmp_CompanyInfoForSpecialSelect",
	"getRoleInterface":"../Sm_Permission_RoleForSelect",
	"companyTypeListInterface":"../Sm_BaseParameterForSelect"
});
