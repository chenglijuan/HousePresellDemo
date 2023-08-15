(function(baseInfo){
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion : 19000101,
			empj_ProjectInfoId : "",
			empj_ProjectInfoList : [],
			buildingArea : "",
			escrowArea : "",
			parameterId : "",
			deliveryType : "",
			deliveryTypeList : [],
			upfloorNumber : "",
			downfloorNumber : "",
			remark : "",
			landMortgagor : "",
			landMortgageAmount : "",
			landMortgageState : "",
			eCodeFromConstruction : "",
			eCodeFromPublicSecurity : "",
			 //附件材料
			busiType : '03010201',
			loadUploadList: [],
			showDelete : true,  //附件是否可编辑
			userType:"",//用户类型
			emmp_CompanyInfoId : "",
			emmp_CompanyInfoList : [],
			tgpj_EscrowStandardVerMngId : "",
			tgpj_EscrowStandardVerMngList : [],
			paymentLines : "50",
			buttonType : "",
			landMortgageStateList : [
				{tableId:"0",theName:"无"},
				{tableId:"1",theName:"有"}
			],
			paymentLinesList : [
				{tableId:"50",theName:"50%"},
				{tableId:"60",theName:"60%"},
				{tableId:"70",theName:"70%"},
				{tableId:"80",theName:"80%"}
			],
			showSubFlag : true,
		},
		methods : {
			//详情
			initData : initData,
			getSearchForm : getSearchForm,
			//添加
			getAddForm : getAddForm,
			add : addBuilding,
			getProjectId : function (data){
				this.empj_ProjectInfoId = data.tableId;
			},
			companyChange : companyChange,
			getEscrowStandardVerMngId : function (data){
				this.tgpj_EscrowStandardVerMngId = data.tableId;
			},
			baseParameterChange : function (data)
		    {
				addVue.parameterId = data.tableId;
				addVue.deliveryType = data.theValue;
		    },
		    changeThousands:function(){
		    	addVue.landMortgageAmount = addThousands(addVue.landMortgageAmount);
			},
			landMortgageStateChange : function(data){
				this.landMortgageState = data.tableId;
			},
			paymentLinesChange : function(data){
				this.paymentLines = data.tableId;
			},
		},
		computed : {
			 
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
			interfaceVersion:this.interfaceVersion,
			projectId:this.empj_ProjectInfoId,
			buildingArea:this.buildingArea,
			escrowArea:this.escrowArea,
			deliveryType:this.deliveryType,
			upfloorNumber:this.upfloorNumber,
			downfloorNumber:this.downfloorNumber,
			remark:this.remark,
			landMortgageState:this.landMortgageState,
			landMortgagor : this.landMortgagor,
			landMortgageAmount : commafyback(this.landMortgageAmount),
			tgpj_EscrowStandardVerMngId : this.tgpj_EscrowStandardVerMngId,
			paymentLines : this.paymentLines,
			buttonType:this.buttonType,
			eCodeFromConstruction:this.eCodeFromConstruction,
			eCodeFromPublicSecurity:this.eCodeFromPublicSecurity,
			 //附件材料
			busiType : this.busiType,
			generalAttachmentList : this.$refs.listenUploadData.uploadData
		}
	}

	function addBuilding(buttonType)
	{
		addVue.buttonType = buttonType;
		if(buttonType == 2){
			addVue.showSubFlag = false;
        	
        } 
		if(addVue.landMortgageAmount != null && addVue.landMortgageAmount != "" && !isThousandsRealNum(addVue.landMortgageAmount))
		{
			addVue.showSubFlag = true;
			var jsonObj = {};
			jsonObj.info = "请先校验是否填写了有效的千分位格式的金额";
			generalErrorModal(jsonObj);
		}
		else
		{
			new ServerInterfaceJsonBody(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj)
			{
				addVue.showSubFlag = true;
				if(jsonObj.result != "success")
				{
					generalErrorModal(jsonObj);
				}
				else
				{
					generalSuccessModal();
					enterNewTabCloseCurrent(jsonObj.empj_BuildingInfo.tableId, '楼幢详情', 'empj/Empj_BuildingInfoDetail.shtml')
				}
			});
		}
	}
	
	function initData()
	{
		laydate.render({
			elem: '#operateTime',
			type: 'datetime'
		});
		getLoginUser();
		getEscrowStandardVerMngList();
		generalLoadFile2(addVue, addVue.busiType)
		getSm_BaseParameterForSelect();
	}
	
	function getSm_BaseParameterForSelect()
	{
	    generalGetParamList("5",function (list) 
	    {
			addVue.deliveryTypeList =list;
	    })
	}
	
	function getLoginUser()
	{
		var model = {
			interfaceVersion : 19000101,
		};
		
		new ServerInterfaceSync(baseInfo.getLoginSm_UserInterface).execute(model, function (jsonObj) {
			if (jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				addVue.userType = jsonObj.sm_User.theType;
				if(1 == addVue.userType)//表示是正泰用户能筛选企业
				{
					getCompanyInfoList();
				}
				else
				{
					addVue.emmp_CompanyInfoId = jsonObj.sm_User.developCompanyId;
					addVue.emmp_CompanyInfoList.push({
		    			tableId:jsonObj.sm_User.developCompanyId,
		    			theName:jsonObj.sm_User.theNameOfCompany,
		    		})
					getProjectList();
				}
			}
		});
	}
	
	function getCompanyInfoList()
	{
		var model = {
			interfaceVersion : 19000101,
		};
		new ServerInterface(baseInfo.companyListInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				addVue.emmp_CompanyInfoList = jsonObj.emmp_CompanyInfoList;
			}
		});
	}
	
	function companyChange(data)
	{
		if(data == null || data.tableId == "" || data.tableId == null)
		{
			addVue.empj_ProjectInfoList = [];
			addVue.empj_ProjectInfoId = "";
		}
		
		addVue.emmp_CompanyInfoId = data.tableId;
		getProjectList();
	}
	
	function getProjectList()
	{
		var model = {
			interfaceVersion : 19000101,
			theState : 0,
			busiState : '已备案',
			developCompanyId : addVue.emmp_CompanyInfoId,
		};
		new ServerInterface(baseInfo.projectListInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				addVue.empj_ProjectInfoList = jsonObj.empj_ProjectInfoList;
			}
		});
	}
	
	function getEscrowStandardVerMngList()
	{
		var model = {
			interfaceVersion : 19000101,
			theState : 0,
			busiState : "已备案",
		};
		new ServerInterface(baseInfo.escrowStandardVerMngListInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				addVue.tgpj_EscrowStandardVerMngList = jsonObj.tgpj_EscrowStandardVerMngList;
				
				this.tgpj_EscrowStandardVerMngId = addVue.tgpj_EscrowStandardVerMngList[0].tableId;
			}
		});
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	addVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#empj_BuildingInfoAddDiv",
	"detailInterface":"../Empj_BuildingInfoDetail",
	"addInterface":"../Empj_BuildingInfoAdd",
	"projectListInterface":"../Empj_ProjectInfoForSelect",
	"getLoginSm_UserInterface":"../Sm_UserGet",
	"companyListInterface":"../Emmp_CompanyInfoForSelect",
	"escrowStandardVerMngListInterface":"../Tgpj_EscrowStandardVerMngForSelect",
});
