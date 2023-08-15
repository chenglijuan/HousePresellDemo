(function(baseInfo){
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			guaranteeType : "",//支付保证申请类型
			ecode : "",//支付保证单号
			guaranteedAmount : "",//已落实支付保证金额
			userRecord : "",//审核人
			applyDate : "",//申请日期
			alreadyActualAmount : "",//项目建设工程已实际支付金额
			userUpdate : "",//操作人
			recordTimeStamp : "",//审核日期
			actualAmount : "",//项目建设工程待支付承保金额
			lastUpdateTimeStamp : "",//操作日期
			remark : "",//备注
			guaranteeNo : "",
            errMsg :"",//存放错误提示信息
            loadUploadList: [],
            showDelete : true,
            busiType: "06120401",
            addDisabled : false,
            submitDisabled :false,
            empj_PaymentGuaranteeApplicationAddList : [],
            guaranteeCompany : "",//出具单位
            projectId : "", //项目名称
			companyId : "", //开发企业
			qs_companyNameList : [], //页面加载显示开发企业
			qs_projectNameList : [], //显示项目名称
			guaranteeCompanyList : [],//
			selectItem : [],
			companyDisabled:false,
			guaranteeTypeList : [
				{tableId:"1",theName:"银行"},
				{tableId:"2",theName:"保险公司"},
				{tableId:"3",theName:"担保公司"},
			],
			paymentLinesList : [
				{tableId:"50",theName:"50"},
				{tableId:"60",theName:"60"},
				{tableId:"70",theName:"70"},
				{tableId:"80",theName:"80"}
			],//支付保证上限比例
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			loadForm:loadForm,
			indexMethod : indexMethod,
			//添加
			getAddForm : getAddForm,
			getUploadForm : getUploadForm,
			getBankForm : getBankForm,
			loadUpload : loadUpload,
			add: add,
			changeECodeFromConstruction : changeECodeFromConstruction,//修改施工编号
			loadCompanyName : loadCompanyNameFun, //页面加载显示开发企业方法
			changeCompanyHandle : changeCompanyHandleFun,//改变项目名称
			changeProjectHandle : changeProjectHandle,//改变企业名称
			blurActMoney : blurActMoney,//鼠标移开楼幢项目建设已实际支付金额
			blurWaitMoney : blurWaitMoney,//鼠标移开楼幢项目建设待支付承保金额
			subForm : subForm,//点击提交按钮
			loadGuaranteeCompany : loadGuaranteeCompany,
			handleSelectionChange : handleSelectionChange,
			getProjectId : function(data){
				addVue.projectId = data.tableId;
				 changeProjectHandle();
			},
			getCompanyId : function(data){
				addVue.companyId = data.tableId;
				changeCompanyHandleFun();
			},
			handleChange: function(data) {
				addVue.guaranteeCompany = data.tableId;
				if(addVue.guaranteeCompany == "1") {
					addVue.guaranteeType = "1";
				}else if(addVue.guaranteeCompany == "2") {
					addVue.guaranteeType = "2";
				}else {
					addVue.guaranteeType = "3";
				}
			},
			chioceChange : chioceChange,//选择触发事件
		},
		
		computed:{
		},
		components : {
			"my-uploadcomponent":fileUpload,
			'vue-select': VueSelect,
		},
		watch:{
			
		}
	});

	//------------------------方法定义-开始------------------//
	
	function chioceChange(row){
		console.log(row.empj_BuildingInfoId);
		console.log(row.paymentLines);
		addVue.empj_PaymentGuaranteeApplicationAddList.forEach(function(item){
			if(row.empj_BuildingInfoId == item.empj_BuildingInfoId){
				//支付保证封顶额度 = 支付保证上限比例 * 初始受限额度
				item.paymentProportion = thousandsToTwoDecimal(commafyback(item.orgLimitedAmount) * row.paymentLines / 100);
			}
		});
	}
	
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			 busiCode : "06120401", //业务编码
		}
	}

	//详情操作--------------
	function refresh()
	{
		new ServerInterface(baseInfo.loadAddInterface).execute(addVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				addVue.applyDate = jsonObj.empj_PaymentGuarantee.applyDate;
				addVue.lastUpdateTimeStamp = jsonObj.empj_PaymentGuarantee.lastUpdateTimeStamp;
				addVue.loadUploadList = jsonObj.smAttachmentCfgList;
				if(addVue.smAttachmentList !=undefined && addVue.smAttachmentList.length>0){
					addVue.hideShow = true;
				}
			}
		});
	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getAddForm()
	{
		var parentModel={
			projectId : this.projectId,
			guaranteeType : this.guaranteeType,
			guaranteedAmount : commafyback(this.guaranteedAmount),
			guaranteeNo : this.guaranteeNo,
			alreadyActualAmount : commafyback(this.alreadyActualAmount),
			companyId : this.companyId,
			guaranteeCompany : this.guaranteeCompany,
			actualAmount : commafyback(this.actualAmount),
			remark : this.remark,
			applyDate : this.applyDate
		};
		var childArr = [];
		if(addVue.selectItem !=undefined && addVue.selectItem.length>0){
			addVue.selectItem.forEach(function(item){
				console.log(item);
				
				var paymentProportion = item.paymentProportion;
				var buildProjectPaid = item.buildProjectPaid;
	    		var model = {
	    			empj_BuildingInfoId : item.empj_BuildingInfoId,
	    			buildProjectPaid : item.buildProjectPaid,
	    			buildProjectPay : item.buildProjectPay,
	    			paymentLines : commafyback(item.paymentLines),//支付保证上限比例
	    			paymentProportion : commafyback(item.paymentProportion),//支付保证封顶额度
	    		}
	    		childArr.push(model);
	    	})
		}
    	childArr = JSON.stringify(childArr);
    	parentModel = JSON.stringify(parentModel); 
		var fileUploadList = addVue.$refs.listenUploadData.uploadData;
		fileUploadList = JSON.stringify(fileUploadList);
		
		return {
			interfaceVersion:this.interfaceVersion,
			empj_PaymentGuarantee:parentModel,
			empj_PaymentGuaranteeChildList : childArr,
			smAttachmentList:fileUploadList
		}
	}
	
	function indexMethod(index){
		return generalIndexMethod(index,addVue);
	}

	//获得信息
	function getBankForm(){
		return{
			interfaceVersion:this.interfaceVersion
		}
	}
	
	//列表操作-----------------------页面加载显示开发企业
	function loadCompanyNameFun() {
		loadCompanyName(addVue,baseInfo.companyNameInterface,function(jsonObj){
			if(jsonObj.companyInfoId != null && jsonObj.companyInfoId > 0)
    		{
        		addVue.companyId = jsonObj.companyInfoId;
        		addVue.companyDisabled = true;
        		addVue.qs_companyNameList.push({
        			tableId:addVue.companyId,
        			theName:jsonObj.emmp_companyInfo.theName,
        		});
        		changeCompanyHandleFun();
    		}else{
    			addVue.qs_companyNameList = jsonObj.emmp_CompanyInfoList;	
    		}
		},addVue.errMsg,baseInfo.edModelDivId);
	}
	//列表操作--------------------改变开发企业的方法
	function changeCompanyHandleFun() {
		changeCompanyHandle(addVue,baseInfo.changeCompanyInterface,function(jsonObj){
			addVue.qs_projectNameList = jsonObj.empj_ProjectInfoList;
		},addVue.errMsg,baseInfo.edModelDivId)
	}
	//新增操作---------------------改变项目名名称
	function changeProjectHandle(){
		
		var model = {
			interfaceVersion : addVue.interfaceVersion,
			projectId : addVue.projectId,
		}
		new ServerInterface(baseInfo.changeProjectInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				addVue.empj_PaymentGuaranteeApplicationAddList = jsonObj.empj_PaymentGuaranteeChildList;
				var alreadyActAmount = null;//项目建设工程已实际支付金额
				var guaranteAmount = null;//已落实支付保证金额
				var waitMoney = null;//项目建设工程待支付承保金额
				addVue.empj_PaymentGuaranteeApplicationAddList.forEach(function(item){
					item.countBuildAmountPaid = addThousands(item.buildAmountPaid);
					item.countBuildAmountPay = addThousands(item.buildAmountPay);
					item.countTotalAmountGuaranteed = addThousands(item.totalAmountGuaranteed);
					item.countAmountGuaranteed = addThousands(item.amountGuaranteed);
//					item.paymentLines = item.paymentLines + '%';
					item.paymentLines = '';
//					item.paymentProportion = addThousands(item.paymentProportion);
					item.paymentProportion = '0.00';
					item.cashLimitedAmount = addThousands(item.cashLimitedAmount);
					item.recordAvgPriceOfBuilding = addThousands(item.recordAvgPriceOfBuilding);
					item.orgLimitedAmount = addThousands(item.orgLimitedAmount);
					item.nodeLimitedAmount = addThousands(item.nodeLimitedAmount);
					item.effectiveLimitedAmount = addThousands(item.effectiveLimitedAmount);
					item.totalAccountAmount = addThousands(item.totalAccountAmount);
					item.payoutAmount = addThousands(item.payoutAmount);
					item.appropriateFrozenAmount = addThousands(item.appropriateFrozenAmount);
					item.appliedNoPayoutAmount = addThousands(item.appliedNoPayoutAmount);
					item.spilloverAmount = addThousands(item.spilloverAmount);
					item.releaseTheAmount = addThousands(item.releaseTheAmount);
					item.oldBuildProjectPaid = item.buildProjectPaid;
					item.oldBuildProjectPay = item.buildProjectPay;
					item.buildProjectPaidDisabled = true;
					item.buildProjectPayDisabled = true;
					alreadyActAmount += parseInt(commafyback(item.countBuildAmountPaid)*100)/100;
					guaranteAmount += parseInt(commafyback(item.totalAmountGuaranteed)*100)/100;
					waitMoney += parseInt(commafyback(item.countBuildAmountPay)*100)/100;
				})
				addVue.alreadyActualAmount = addThousands(alreadyActAmount);
				addVue.guaranteedAmount = addThousands(guaranteAmount);
				addVue.actualAmount = addThousands(waitMoney);
			}
		});
	}
	
	//改变施工编号
	function changeECodeFromConstruction(val){
		var model = {
			interfaceVersion : this.interfaceVersion,
			tableId : val.tableId,
			eCodeFromConstruction : val.eCodeFromConstruction,
		}
		new ServerInterface(baseInfo.changeECodeInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				addVue.empj_PaymentGuaranteeApplicationAddList.forEach(function(item){
					if(item.tableId == val.tebleId){
						item.eCodeFromConstruction = jsonObj.eCodeFromConstruction;
					}
				})
			}
		});
	}
	//新增操作------------------楼幢项目建设已实际支付金额
	function blurActMoney(act){
		var alreadyActAmount = null;//项目建设工程已实际支付金额
		var guaranteAmount = null;//已落实支付保证金额
		/*if(act.buildProjectPaid == ""){
			act.buildProjectPaid = 0
		}*/
		addVue.empj_PaymentGuaranteeApplicationAddList.forEach(function(item){
			if(item.empj_BuildingInfoId == act.empj_BuildingInfoId){
				//楼幢项目建设已实际支付金额 buildProjectPaid
				if(act.buildProjectPaid == "" || act.buildProjectPaid == "0"){//楼幢项目建设已实际支付金额 buildProjectPaid为0
					item.countBuildAmountPaid = addThousands((parseInt(item.buildAmountPaid*100)/100-parseInt(item.oldBuildProjectPaid*100)/100) + 0);//楼幢项目建设已实际支付累计金额（元）countBuildAmountPaid  楼幢项目建设已实际支付累计金额（元）//buildAmountPaid
					if(act.buildProjectPay == "" || act.buildProjectPay == "0"){//已落实支付保证金额（元）countAmountGuaranteed
						item.countAmountGuaranteed = addThousands(0 + 0);
					}else{
						item.countAmountGuaranteed = addThousands(0 + parseInt(item.buildProjectPay*100)/100);
					}
				}else{
					if(act.buildProjectPay == "" || act.buildProjectPay == "0"){
						item.countAmountGuaranteed = addThousands(parseInt(item.buildProjectPaid*100)/100 + 0);
					}else{
						item.countAmountGuaranteed = addThousands(parseInt(item.buildProjectPaid*100)/100 + parseInt(item.buildProjectPay*100)/100);
					}
					item.countBuildAmountPaid = addThousands((parseInt(item.buildAmountPaid*100)/100-parseInt(item.oldBuildProjectPaid*100)/100) + parseInt(item.buildProjectPaid*100)/100);//楼幢项目建设已实际支付累计金额（元）
				}
				
				item.countTotalAmountGuaranteed = addThousands(parseInt(commafyback(item.countAmountGuaranteed)*100)/100 + (parseInt(item.totalAmountGuaranteed*100)/100-parseInt(item.amountGuaranteed*100)/100));//已落实支付保证累计金额（元）
			
			}
			alreadyActAmount += parseInt(commafyback(item.countBuildAmountPaid)*100)/100;
			guaranteAmount += parseInt(commafyback(item.countTotalAmountGuaranteed)*100)/100;
		})
		addVue.alreadyActualAmount = addThousands(alreadyActAmount);
		addVue.guaranteedAmount = addThousands(guaranteAmount);
	}
	//新增操作------------------楼幢项目建设待支付承保金额
	function blurWaitMoney(wait){
		var waitMoney = null;
		var guaranteAmount = null;//已落实支付保证金额
		addVue.empj_PaymentGuaranteeApplicationAddList.forEach(function(item){
			if(item.empj_BuildingInfoId == wait.empj_BuildingInfoId){
				if(wait.buildProjectPay == "" || wait.buildProjectPay==0){
					item.countBuildAmountPay = addThousands((parseInt(item.buildAmountPay*100)/100-parseInt(item.oldBuildProjectPay*100)/100) + 0);
					if(wait.buildProjectPaid == "" || wait.buildProjectPaid == 0){
						item.countAmountGuaranteed = addThousands(0);
					}else{
						item.countAmountGuaranteed = addThousands(parseInt(item.buildProjectPaid*100)/100 + 0);
					}
					
				}else{
					if(wait.buildProjectPaid == "" || wait.buildProjectPaid == "0"){
						item.countAmountGuaranteed = addThousands(0 + parseInt(item.buildProjectPay*100)/100);
					}else{
						item.countAmountGuaranteed = addThousands(parseInt(item.buildProjectPaid*100)/100 + parseInt(item.buildProjectPay*100)/100);
					}
					item.countBuildAmountPay = addThousands((parseInt(item.buildAmountPay*100)/100-parseInt(item.oldBuildProjectPay*100)/100) + parseInt(item.buildProjectPay*100)/100);
					
				}
				item.countTotalAmountGuaranteed = addThousands(parseInt(commafyback(item.countAmountGuaranteed)*100)/100 + (parseInt(item.totalAmountGuaranteed*100)/100-parseInt(item.amountGuaranteed*100)/100));
			}
			waitMoney += parseInt(commafyback(item.countBuildAmountPay)*100)/100;
			guaranteAmount += parseInt(commafyback(item.countTotalAmountGuaranteed)*100)/100;
		})
		addVue.actualAmount = addThousands(waitMoney);
		addVue.guaranteedAmount = addThousands(guaranteAmount);
	}
	
	//列表操作---选择
    function handleSelectionChange(val) {
    	addVue.selectItem = [];
    	var listval = addVue.empj_PaymentGuaranteeApplicationAddList;
    		if(val.length != 0){//当前页面没有选中的值    		
    			for(var j = 0;j < listval.length;j++){
    				// 初始化计数
    				var index = 0; 			
    				for(var i = 0;i<val.length;i++){
    					if(val[i].empj_BuildingInfoId == listval[j].empj_BuildingInfoId){
    						listval[j].buildProjectPaidDisabled = false;
    						listval[j].buildProjectPayDisabled = false;
    						break;
    					}else{    						
    						index++;
    						if(index == val.length)
    						{
    							if(!listval[j].buildProjectPaidDisabled)
    							{
        							listval[j].buildProjectPaid = 0;
        		    				listval[j].buildProjectPay = 0;
    							}
    							listval[j].buildProjectPaidDisabled = true;
    							listval[j].buildProjectPayDisabled = true;
    							blurWaitMoney(listval[j]);
    							//blurActMoney(listval[j]);
    						}
    					}  				
    				}	
    			}
    		}else{//当前页面有选中项
    			for (var k = 0; k < listval.length; k++) {
    				if(listval[k].buildProjectPaidDisabled == false)
    				{
    					listval[k].buildProjectPaid = 0;
    					listval[k].buildProjectPay =0;
    				}
    				blurWaitMoney(listval[k]);
					blurActMoney(listval[k]);
					listval[k].buildProjectPaidDisabled = true;
					listval[k].buildProjectPayDisabled = true;
        	 	}			
			}	
    		
      		if(val.length != 0) {
      			addVue.selectItem = val;
    		}
    	
    }
	
	//新增操作-----------------------支付保证出具单位
	function loadGuaranteeCompany(){
		new ServerInterface(baseInfo.guaranteeCompanyInterface).execute(addVue.getBankForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				addVue.guaranteeCompanyList = jsonObj.emmp_CompanyInfoList;
			}
		});
	}
	
	
	//点击保存
	function add()
	{
		new ServerInterface(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				enterNext2Tab(jsonObj.tableId, '支付保证申请详情', 'empj/Empj_PaymentGuaranteeApplicationDetail.shtml',jsonObj.tableId+"06120401");
			}
		});
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
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				//refresh();
				addVue.loadUploadList = jsonObj.sm_AttachmentCfgList;
			}
		});
	}
	
	//
	function loadForm(){
		new ServerInterface(baseInfo.bankInterface).execute(addVue.getBankForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				//refresh();
				addVue.tgxy_BankAccountEscrowedList = jsonObj.tgxy_BankAccountEscrowedList;
			}
		});
	}
	function choiceRefundBankAccountForm(){
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:this.theBankAccountEscrowedId,
		}
	}
	//修改银行账号带出银行名称
	function choiceRefundBankAccount(){
		new ServerInterface(baseInfo.refundBankAccountInterface).execute(addVue.choiceRefundBankAccountForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				//refresh();
				addVue.theNameOfBank = jsonObj.tgxy_BankAccountEscrowed.theNameOfBank;
			}
		});
	}
	//新增操作-----------------------点击提交按钮
	function subForm(){
		new ServerInterface(baseInfo.subInterface).execute(addVue.getBankForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				//refresh();
				addVue.theNameOfBank = jsonObj.tgxy_BankAccountEscrowed.theNameOfBank;
			}
		});
	}
	
	function initData()
	{
		
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	addVue.loadCompanyName();
	addVue.loadGuaranteeCompany();
	addVue.refresh();
	addVue.initData();

	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#Empj_PaymentGuaranteeApplicationAddDiv",
	"edModelDivId":"#ed1Model",
	"sdModelDivId":"#sd1Model",
	"loadAddInterface":"../Empj_PaymentGuaranteeApplyDetail",
	"addInterface":"../Empj_PaymentGuaranteeAdd",//点击保存接口
	"loadUploadInterface":"../Sm_AttachmentCfgList",
	"companyNameInterface" : "../Emmp_CompanyInfoList", //显示开发企业名称接口
	"changeCompanyInterface" : "../Empj_ProjectInfoList", //改变企业名称接口
	"changeECodeInterface" : "",//改变施工编号带出信息接口
	"changeProjectInterface" : "../Empj_PaymentGuaranteeApplyDetailList",//改变项目名称带出字表信息接口
	"guaranteeCompanyInterface" : "../Emmp_CompanyInfoList",//页面加载显示支付保证出具单位接口
	"getNewDataInterface" : "",//onblur事件带出当前数据的接口
	"subInterface" : "",//点击提交接口
});
