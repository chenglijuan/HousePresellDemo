(function(baseInfo){
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			tgpf_FundAppropriatedModel: {},
			delDisabled : true,
			tgpf_SpacialFundAppropriatedAddList : [],
			isList :"",//开发企业是否时列表
			emmp_CompanyInfo : {},//企业不是列表
			emmp_CompanyInfoList : [],
			developCompanyId : "",//开发企业Id
			empj_ProjectInfoList : [],
			projectId : "",//项目Id
			empj_BuildingInfoList : [],
			tableId : "",
			empj_BuildingInfo : {},
			tgpj_BuildingAccount : {},
			appropriatedType:"1",//特殊拨付类型
			tgpj_BankAccountSupervisedList : [],
			accountId : "",
			tgpj_BankAccountSupervised : {},
			applyDate : "",
			totalApplyAmount : "",
			appropriatedRemark : "",
			theAccountOfBankAccount : "",//预售资金监管账号
			theBankOfBankAccount : "",//监管账户开户行
			theNameOfBankAccount : "",//监管账户名称
			loadUploadList : [],
            showDelete : true,
            busiType : "06120603",
            allocableAmount : 0,
            appropriatedTypeList : [
            	{tableId:"1",theName:"定向支付"},
            	{tableId:"2",theName:"特殊拨付"},
            	{tableId:"3",theName:"其他支付"},
            ]
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			//添加
			getAddForm : getAddForm,
			add: add,
			loadCompanyName : loadCompanyName,
			getProjectName : getProjectName,
			getProjectNameForm : getProjectNameForm,
			getBuildingName : getBuildingName,
			getBuildingNameForm : getBuildingNameForm,
			getCompanyId : function(data){
				addVue.developCompanyId = data.tableId;
				getProjectName();
			},
			getProjectId : function(data){
				addVue.projectId = data.tableId;
				getBuildingName();
			},
			changeECodeFromConstructionHandle : changeECodeFromConstructionHandle,
			getECodeFromConstructionHandle : getECodeFromConstructionHandle,
			changeBankAccountHandle : changeBankAccountHandle,
			getBankAccountForm : getBankAccountForm,
			getUploadForm : getUploadForm,
			loadUpload : loadUpload,
			addSpecialFund : addSpecialFund,
			getAppropriatedType : function(data){
				this.appropriatedType = data.tableId;
			}
		},
		computed:{
			 
		},
		components : {
			"my-uploadcomponent":fileUpload,
			'vue-select': VueSelect,
		},
		watch:{
			
		},
		mounted:function(){
			
			/**
			 * 初始化日期插件
			 */
			laydate.render({
			  elem: '#applyDate',
			  done: function(value, date, endDate){
			    addVue.applyDate = value;
			  }
			});
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

	//详情操作--------------
	function refresh()
	{

	}

	function getAddForm()
	{
		
		var fileUploadList = addVue.$refs.listenUploadData.uploadData;
		fileUploadList = JSON.stringify(fileUploadList);
		return {
			interfaceVersion:this.interfaceVersion,
			theState:this.theState,
			busiState:this.busiState,
			eCode:this.eCode,
			userStartId:this.userStartId,
			applyDate:this.applyDate,
			developCompanyId:this.developCompanyId,
			projectId:this.projectId,
			buildingId:this.tableId,
			bankAccountId:this.accountId,
			totalApplyAmount:this.totalApplyAmount,
			appropriatedType:this.appropriatedType,
			appropriatedRemark:this.appropriatedRemark,
			theAccountOfBankAccount : this.theAccountOfBankAccount,
			theBankOfBankAccount : this.theBankOfBankAccount,
			theNameOfBankAccount : this.theNameOfBankAccount,
			smAttachmentList:fileUploadList
		}
	}
	
	//新增操作-----------------加载项目信息
	function getProjectNameForm(){
		return {
			interfaceVersion : this.interfaceVersion,
			developCompanyId : this.developCompanyId
		}
	}
	
	//新增操作----------------根据项目信息带出楼幢信息
	function getBuildingNameForm(){
		return {
			interfaceVersion : this.interfaceVersion,
			projectId : this.projectId
		}
	}
	
	function getECodeFromConstructionHandle(){
		return {
			interfaceVersion : this.interfaceVersion,
			tableId : this.tableId
		}
	}
	
	function getBankAccountForm(){
		return {
			interfaceVersion : this.interfaceVersion,
			tableId : this.accountId
		}
	}
	
	//新增操作-----------------------加载开发企业
	function loadCompanyName(){
		new ServerInterface(baseInfo.companyInterface).execute(addVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
                generalErrorModal(jsonObj);
			}
			else
			{
				addVue.isList=jsonObj.isList;
				if(addVue.isList == 0){
					addVue.emmp_CompanyInfo = jsonObj.emmp_CompanyInfo;
					addVue.developCompanyId = addVue.emmp_CompanyInfo.tableId;
					getProjectName();
				}else{
					addVue.emmp_CompanyInfoList = jsonObj.emmp_CompanyInfoList;
				}
			}
		});
	}
	//新增操作--------------------------加载项目信息
	function getProjectName(){
		new ServerInterface(baseInfo.projectInterface).execute(addVue.getProjectNameForm(), function(jsonObj)
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
	//新增操作-------------------加载楼幢信息
	function getBuildingName(){
		new ServerInterface(baseInfo.buildingInterface).execute(addVue.getBuildingNameForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
                generalErrorModal(jsonObj);
			}
			else
			{
				addVue.empj_BuildingInfoList = jsonObj.empj_BuildingInfoList;
			}
		});
	}
	//新增操作-------------------根据楼幢加载详情
	function changeECodeFromConstructionHandle(){
		new ServerInterface(baseInfo.eCodeFromConstructionInterface).execute(addVue.getECodeFromConstructionHandle(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
                generalErrorModal(jsonObj);
			}
			else
			{//bankAccountSupervisedList
				addVue.empj_BuildingInfo = jsonObj.empj_BuildingInfo;
				addVue.tgpj_BuildingAccount = jsonObj.tgpj_BuildingAccount;
				//addVue.tgpj_BankAccountSupervisedList = jsonObj.tgpj_BankAccountSupervisedList;
				addVue.tgpj_BankAccountSupervisedList = jsonObj.empj_BuildingInfo.bankAccountSupervisedList;
				
				//初始受限额度
				addVue.tgpj_BuildingAccount.orgLimitedAmount = addThousands(addVue.tgpj_BuildingAccount.orgLimitedAmount);
				//节点受限额度
				addVue.tgpj_BuildingAccount.nodeLimitedAmount = addThousands(addVue.tgpj_BuildingAccount.nodeLimitedAmount);
				//现金受限额度
				addVue.tgpj_BuildingAccount.cashLimitedAmount = addThousands(addVue.tgpj_BuildingAccount.cashLimitedAmount);
				//有效受限额度
				addVue.tgpj_BuildingAccount.effectiveLimitedAmount = addThousands(addVue.tgpj_BuildingAccount.effectiveLimitedAmount);
				//总入账金额
				addVue.tgpj_BuildingAccount.totalAccountAmount = addThousands(addVue.tgpj_BuildingAccount.totalAccountAmount);
				//已申请拨付金额（元）：已拨付金额（元）
				addVue.tgpj_BuildingAccount.payoutAmount = addThousands(addVue.tgpj_BuildingAccount.payoutAmount);
				
				addVue.allocableAmount = addVue.tgpj_BuildingAccount.allocableAmount;
				//当前可拨付金额（元）：可划拨金额（元）
				addVue.tgpj_BuildingAccount.allocableAmount = addThousands(addVue.tgpj_BuildingAccount.allocableAmount);
				//当前托管余额
				addVue.tgpj_BuildingAccount.currentEscrowFund = addThousands(addVue.tgpj_BuildingAccount.currentEscrowFund);
				//退房退款金额（元）： 已退款金额（元） refundAmount
				addVue.tgpj_BuildingAccount.appropriateFrozenAmount = addThousands(addVue.tgpj_BuildingAccount.refundAmount);
				
			}
		});
	}
	//新增操作-------------------根据所选监管账户加载详细信息
	function changeBankAccountHandle(){
		addVue.tgpj_BankAccountSupervisedList.forEach(function(item){
			if(item.tableId == addVue.accountId){
				addVue.tgpj_BankAccountSupervised.theNameOfBank = item.theNameOfBank;
				addVue.tgpj_BankAccountSupervised.bankBranchName = item.theNameOfBankBranch;
			}
		})
		/*new ServerInterface(baseInfo.bankAccountInterface).execute(addVue.getBankAccountForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
                generalErrorModal(jsonObj);
			}
			else
			{
				addVue.tgpj_BankAccountSupervised = jsonObj.tgpj_BankAccountSupervised
			}
		});*/
	}
	
	//列表操作-----------------------获取附件参数
	function getUploadForm(){
		return{
			pageNumber : '0',
			busiType : addVue.busiType,
			interfaceVersion:this.interfaceVersion
		}
	}
	
	//列表操作-----------------------页面加载显示附件类型
	function loadUpload(){
		new ServerInterface(baseInfo.loadUploadInterface).execute(addVue.getUploadForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				addVue.errMsg = jsonObj.info;
			}
			else
			{
				//refresh();
				addVue.loadUploadList = jsonObj.sm_AttachmentCfgList;
			}
		});
	}
	//新增操作------------------------删除
	function showDelModal(){
		
	}
	
	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		listVue.isAllSelected = (listVue.tgpf_SpacialFundAppropriatedList.length > 0)
		&&	(listVue.selectItem.length == listVue.tgpf_SpacialFundAppropriatedList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgpf_SpacialFundAppropriatedList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgpf_SpacialFundAppropriatedList.forEach(function(item) {
	    		listVue.selectItem.push(item.tableId);
	    	});
	    }
	}
	
	//新增操作---------------------获取索引
	function indexMethod(index)
    {
        return generalIndexMethod(index, listVue)
    }
	
	function showAddSpecialFundModel(){
		$(baseInfo.addSpecialModel).modal({
			backdrop:"static"
		})
	}
	
	//新增操作--------------------选择特殊拨付划款信息表
	function listItemSelectHandle(list){
		generalListItemSelectHandle(listVue,list)
	}
	
	function add()
	{
		/*
		 * “本次划款申请金额”<=“可拨付金额”时，提示用户但不强制校验。
		 */
		//可拨付金额
		var allocableAmount = addVue.allocableAmount;
		//申请金额
		var totalApplyAmount = addVue.totalApplyAmount;
		
		console.log("allocableAmount"+allocableAmount);
		console.log("totalApplyAmount"+totalApplyAmount);
		
		if(allocableAmount<totalApplyAmount)
		{
			$(baseInfo.tipsAddSpecialFundDiv).modal({
			    backdrop :'static'
		    });
		}
		else
		{
			addSpecialFund();
		}
		
	}
	
	//正式新增--
	function addSpecialFund()
	{
		$(baseInfo.tipsAddSpecialFundDiv).modal('hide');
		setTimeout(function() {
			new ServerInterface(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj)
	         		{
	         			if(jsonObj.result != "success")
	         			{
	                         generalErrorModal(jsonObj);
	         			}
	         			else
	         			{
	         				//refresh();
	         				enterNext2Tab(jsonObj.tableId, '特殊拨付详情', 'tgpf/Tgpf_SpecialFundAppropriatedDetail.shtml',jsonObj.tableId+"061206");
	         			}
	         		});	
		},200);
	}
	
	function initData()
	{
		loadCompanyName();
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	var myDate = Date.parse(new Date());
	addVue.applyDate = timeStamp2DayDate(myDate);
	addVue.loadUpload();
	addVue.refresh();
	addVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#tgpf_FundAppropriatedAddDiv",
	"addSpecialModel":"#addSpecialFundModel",
	"tipsAddSpecialFundDiv":"#tipsAddSpecialFundModal1",
	"detailInterface":"../Tgpf_FundAppropriatedDetail",
	"addInterface":"../Tgpf_SpecialFundAppropriated_AFAdd",
	"companyInterface" : "../Tgpf_SpecialFundCompanyInfoPre",
	"projectInterface" : "../Tgpf_SpecialFundProjectInfoList",
	"buildingInterface" : "../Tgpf_SpecialFundBuildingInfoList",
	"eCodeFromConstructionInterface" : "../Tgpf_SpecialFundBuildingInfoDetail",
	"bankAccountInterface" : "../Tgpj_BankAccountSupervisedDetail",
	"loadUploadInterface":"../Sm_AttachmentCfgList",
});
