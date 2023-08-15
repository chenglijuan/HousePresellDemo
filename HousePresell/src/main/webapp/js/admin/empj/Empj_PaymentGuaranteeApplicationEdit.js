(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			guaranteeType : "",//支付保证申请类型
			eCode : "",//支付保证单号
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
            empj_PaymentGuaranteeApplicationEditList : [],
            eCodeFromConstruction : "",
            guaranteeCompany : "",//出具单位
            projectId : "", //项目名称
			companyId : "", //开发企业
			qs_companyNameList : [], //页面加载显示开发企业
			qs_projectNameList : [], //显示项目名称
			guaranteeCompanyList : [],//
			saveDisabled : false,
			subDisabled : false,
			companyName:"",
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
			getBankForm : getBankForm,
			loadForm:loadForm,
			indexMethod : indexMethod,
			//添加
			getAddForm : getAddForm,
			getUploadForm : getUploadForm,
			loadUpload : loadUpload,
			add: add,
			changeECodeFromConstruction : changeECodeFromConstruction,//修改施工编号
			loadCompanyName : loadCompanyNameFun, //页面加载显示开发企业方法
			changeCompanyHandle : changeCompanyHandleFun,//改变项目名称
			changeProjectHandle : changeProjectHandle,//改变企业名称
			blurActMoney : blurActMoney,//鼠标移开楼幢项目建设已实际支付金额
			blurWaitMoney : blurWaitMoney,//鼠标移开楼幢项目建设待支付承保金额
			
			getDetail : getDetail,
			handleSelectionChange : handleSelectionChange,
			loadGuaranteeCompany : loadGuaranteeCompany,
			handleChange: function(data) {
				detailVue.guaranteeCompany = data.tableId;
				if(detailVue.guaranteeCompany == "1") {
					detailVue.guaranteeType = "1";
				}else if(detailVue.guaranteeCompany == "2") {
					detailVue.guaranteeType = "2";
				}else {
					detailVue.guaranteeType = "3";
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
		detailVue.empj_PaymentGuaranteeApplicationEditList.forEach(function(item){
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
			tableId:detailVue.tableId,
			busiCode : "06120401", //业务编码
		}
	}

	//详情操作--------------
	function refresh()
	{
		if (detailVue.tableId == null || detailVue.tableId < 1) 
		{
			return;
		}

		getDetail();
	}
	
	function getDetail()
	{
		new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				detailVue.applyDate = jsonObj.empj_PaymentGuarantee.applyDate;
				detailVue.lastUpdateTimeStamp = jsonObj.empj_PaymentGuarantee.lastUpdateTimeStamp;
				detailVue.guaranteeType = jsonObj.empj_PaymentGuarantee.guaranteeType;
				detailVue.ecode = jsonObj.empj_PaymentGuarantee.ecode;
				detailVue.guaranteedAmount = jsonObj.empj_PaymentGuarantee.guaranteedAmount;
				detailVue.userRecord = jsonObj.empj_PaymentGuarantee.userRecord;
				detailVue.alreadyActualAmount = jsonObj.empj_PaymentGuarantee.alreadyActualAmount;
				detailVue.userUpdate = jsonObj.empj_PaymentGuarantee.userUpdate;
				detailVue.recordTimeStamp = jsonObj.empj_PaymentGuarantee.recordTimeStamp;
				detailVue.actualAmount = jsonObj.empj_PaymentGuarantee.actualAmount;
				detailVue.remark = jsonObj.empj_PaymentGuarantee.remark;
				detailVue.guaranteeNo = jsonObj.empj_PaymentGuarantee.guaranteeNo;
				detailVue.projectId = jsonObj.empj_PaymentGuarantee.projectId;
				detailVue.companyId = jsonObj.empj_PaymentGuarantee.companyId;
				detailVue.eCode = jsonObj.empj_PaymentGuarantee.eCode; 
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
				detailVue.guaranteeCompany = jsonObj.empj_PaymentGuarantee.guaranteeCompany;
				detailVue.companyName = jsonObj.empj_PaymentGuarantee.companyName;
				detailVue.empj_PaymentGuaranteeApplicationEditList = jsonObj.empj_PaymentGuaranteeChildList;
				
				var alreadyActAmount = null;//项目建设工程已实际支付金额
				var guaranteAmount = null;//已落实支付保证金额
				var waitMoney = null;//项目建设工程待支付承保金额
				detailVue.empj_PaymentGuaranteeApplicationEditList.forEach(function(item, index){
					item.countBuildAmountPaid = addThousands(item.buildAmountPaid);
					item.countBuildAmountPay = addThousands(item.buildAmountPay);
					item.countTotalAmountGuaranteed = addThousands(item.totalAmountGuaranteed);
					item.countAmountGuaranteed = addThousands(item.amountGuaranteed);
//					item.paymentLines = item.paymentLines + '%';
					item.paymentLines = item.paymentLines;
					item.paymentProportion = addThousands(item.paymentProportion);
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
					guaranteAmount += parseInt(commafyback(item.countTotalAmountGuaranteed)*100)/100;
					waitMoney += parseInt(commafyback(item.countBuildAmountPay)*100)/100;
					console.log(item.buildProjectPaid);
                    console.log(item.buildProjectPay);
					if(item.buildProjectPaid > 0 || item.buildProjectPay > 0) {
	                    console.log("1111");
	                    detailVue.$nextTick(function() {
							detailVue.$refs.moviesTable.toggleRowSelection(detailVue.empj_PaymentGuaranteeApplicationEditList[index], true);
						})
					}
					
				})
				detailVue.alreadyActualAmount = addThousands(alreadyActAmount);
				detailVue.guaranteedAmount = addThousands(guaranteAmount);
				detailVue.actualAmount = addThousands(waitMoney);
				detailVue.changeCompanyHandle();
				//detailVue.loadGuaranteeCompany();
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
				applyDate : this.applyDate,
				tableId : this.tableId
			};
			var childArr = [];
			if(detailVue.selectItem !=undefined && detailVue.selectItem.length>0){
				detailVue.selectItem.forEach(function(item){
		    		var model = {
		    			empj_BuildingInfoId : item.empj_BuildingInfoId,
		    			buildProjectPaid : item.buildProjectPaid,
		    			buildProjectPay : item.buildProjectPay,
		    			tableId:item.tableId,
		    			paymentLines : commafyback(item.paymentLines),//支付保证上限比例
		    			paymentProportion : commafyback(item.paymentProportion),//支付保证封顶额度
		    		}
		    		childArr.push(model);
		    	})
			}
	    	childArr = JSON.stringify(childArr);
	    	parentModel = JSON.stringify(parentModel); 
			var fileUploadList = detailVue.$refs.listenUploadData.uploadData;
			fileUploadList = JSON.stringify(fileUploadList);
			
			return {
				interfaceVersion:this.interfaceVersion,
				empj_PaymentGuarantee:parentModel,
				empj_PaymentGuaranteeChildList : childArr,
				smAttachmentList:fileUploadList
			}
	}
	
	function indexMethod(index){
		return generalIndexMethod(index,detailVue);
	}
	
	//获得信息
	function getBankForm(){
		return{
			interfaceVersion:this.interfaceVersion
		}
	}
	//列表操作-----------------------页面加载显示开发企业
	function loadCompanyNameFun() {
		loadCompanyName(detailVue,baseInfo.companyNameInterface,function(jsonObj){
			detailVue.qs_companyNameList = jsonObj.emmp_CompanyInfoList;
		},detailVue.errMsg,baseInfo.edModelDivId);
	}
	//列表操作--------------------改变开发企业的方法
	function changeCompanyHandleFun() {
		var model ={
				interfaceVersion : this.interfaceVersion,
				developCompanyId : this.companyId,
		}
		new ServerInterface(baseInfo.changeCompanyInterface).execute(model, function(jsonObj)
			{
				if(jsonObj.result != "success")
				{
					generalErrorModal(jsonObj,jsonObj.info);
				}
				else
				{
					detailVue.qs_projectNameList= jsonObj.empj_ProjectInfoList;
				}
			});
	}
	//新增操作---------------------改变项目名名称
	function changeProjectHandle(){
		var model = {
			interfaceVersion : this.interfaceVersion,
			projectId : this.projectId,
		}
		new ServerInterface(baseInfo.changeProjectInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				detailVue.empj_PaymentGuaranteeApplicationEditList = jsonObj.empj_PaymentGuaranteeApplicationEditList;
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
				detailVue.empj_PaymentGuaranteeApplicationAddList.forEach(function(item){
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
		detailVue.empj_PaymentGuaranteeApplicationEditList.forEach(function(item){
			if(item.empj_BuildingInfoId == act.empj_BuildingInfoId){//楼幢项目建设已实际支付金额 buildProjectPaid
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
		detailVue.alreadyActualAmount = addThousands(alreadyActAmount);
		detailVue.guaranteedAmount = addThousands(guaranteAmount);
	}
	//新增操作------------------楼幢项目建设待支付承保金额
	function blurWaitMoney(wait){
		console.log(wait);
		var waitMoney = null;
		var guaranteAmount = null;//已落实支付保证金额
		detailVue.empj_PaymentGuaranteeApplicationEditList.forEach(function(item){
			if(item.empj_BuildingInfoId == wait.empj_BuildingInfoId){
				if(wait.buildProjectPay == "" || wait.buildProjectPay=="0"){
					item.countBuildAmountPay = addThousands((parseInt(item.buildAmountPay*100)/100-parseInt(item.oldBuildProjectPay*100)/100) + 0);
					if(wait.buildProjectPaid == "" || wait.buildProjectPaid == "0"){
						item.countAmountGuaranteed = addThousands(0 + 0);
					}else{
						item.countAmountGuaranteed = addThousands(parseInt(item.buildProjectPaid*100)/100 + 0);
					}
					
				}else{
					if(wait.buildProjectPaid == "" || wait.buildProjectPaid == "0"){
						item.countAmountGuaranteed = addThousands(0 + parseInt(item.buildProjectPay*100)/100);
					}else{
						item.countAmountGuaranteed = addThousands(parseInt(item.buildProjectPaid*100)/100 + parseInt(item.buildProjectPay*100)/100);
					}
					console.log("老数据"+(parseInt(item.oldBuildProjectPay*100)/100));
					item.countBuildAmountPay = addThousands((parseInt(item.buildAmountPay*100)/100-parseInt(item.oldBuildProjectPay*100)/100) + parseInt(item.buildProjectPay*100)/100);
					
				}
				
				item.countTotalAmountGuaranteed = addThousands(parseInt(commafyback(item.countAmountGuaranteed)*100)/100 + (parseInt(item.totalAmountGuaranteed*100)/100-parseInt(item.amountGuaranteed*100)/100));
			}
			waitMoney += parseInt(commafyback(item.countBuildAmountPay)*100)/100;
			guaranteAmount += parseInt(commafyback(item.countTotalAmountGuaranteed)*100)/100;
		})
		detailVue.actualAmount = addThousands(waitMoney);
		detailVue.guaranteedAmount = addThousands(guaranteAmount);
	}
	var selectArr = [];
	//列表操作---选择
    function handleSelectionChange(val) {
    	detailVue.selectItem = [];
    	var listval = detailVue.empj_PaymentGuaranteeApplicationEditList;
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
        		    				listval[j].buildProjectPay =0;
    							}
    							listval[j].buildProjectPaidDisabled = true;
    							listval[j].buildProjectPayDisabled = true;
    							blurWaitMoney(listval[j]);
    							blurActMoney(listval[j]);
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
    			detailVue.selectItem = val;
    		}
    	}

    
	//新增操作-----------------------支付保证出具单位
	function loadGuaranteeCompany(){
		new ServerInterface(baseInfo.guaranteeCompanyInterface).execute(detailVue.getBankForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				detailVue.guaranteeCompanyList = jsonObj.emmp_CompanyInfoList;
			}
		});
	}
	function add()
	{
		new ServerInterface(baseInfo.addInterface).execute(detailVue.getAddForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				enterNext2Tab(detailVue.tableId, '支付保证申请详情', 'empj/Empj_PaymentGuaranteeApplicationDetail.shtml',detailVue.tableId+"06120401");
			}
		});
	}
	//列表操作-----------------------获取附件参数
	function getUploadForm(){
		return{
			pageNumber : '0',
			busiType : detailVue.busiType,
			interfaceVersion:this.interfaceVersion
		}
	}
	
	//列表操作-----------------------页面加载显示附件类型
	function loadUpload(){
		new ServerInterface(baseInfo.loadUploadInterface).execute(detailVue.getUploadForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				//refresh();
				detailVue.loadUploadList = jsonObj.sm_AttachmentCfgList;
			}
		});
	}
	
	//
	function loadForm(){
		new ServerInterface(baseInfo.bankInterface).execute(detailVue.getBankForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				//refresh();
				detailVue.tgxy_BankAccountEscrowedList = jsonObj.tgxy_BankAccountEscrowedList;
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
		new ServerInterface(baseInfo.refundBankAccountInterface).execute(detailVue.choiceRefundBankAccountForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				//refresh();
				detailVue.theNameOfBank = jsonObj.tgxy_BankAccountEscrowed.theNameOfBank;
			}
		});
	}
	
	
	function initData()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var array = tableIdStr.split("_");
		detailVue.tableId = array[2];
		
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.loadUpload();
	detailVue.loadCompanyName();
	detailVue.initData();
	detailVue.refresh();
	
	
	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#Empj_PaymentGuaranteeApplicationEditDiv",
	"edModelDivId":"#ed1Model",
	"sdModelDivId":"#sd1Model",
	"detailInterface":"../Empj_PaymentGuaranteeApplyDetail",
	"addInterface":"../Empj_PaymentGuaranteeApplyUpdate",
	
	"loadUploadInterface":"../Sm_AttachmentCfgList",
	"companyNameInterface" : "../Emmp_CompanyInfoList", //显示开发企业名称接口
	"changeCompanyInterface" : "../Empj_ProjectInfoList", //改变企业名称接口
	"changeECodeInterface" : "",//改变施工编号带出信息接口
	"changeProjectInterface" : "",//改变项目名称带出字表信息接口
	"guaranteeCompanyInterface" : "../Emmp_CompanyInfoList",//页面加载显示支付保证出具单位接口
	"getNewDataInterface" : "",//onblur事件带出当前数据的接口
});
