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
			guaranteedSumAmount : "",//保函总金额
			personOfProfits : "",//受益人
			
			guaranteeNo : "",
            errMsg :"",//存放错误提示信息
            controlPercentage : 40,//保函最低控制线
            loadUploadList: [],
            showDelete : true,
            busiType: "06120501",
            addDisabled : false,
            submitDisabled :false,
            empj_PaymentBondAddList : [],
            guaranteeCompany : "",//保证机构
            projectId : "", //项目名称
			companyId : "", //开发企业
			qs_companyNameList : [], //页面加载显示开发企业
			qs_projectNameList : [], //显示项目名称
			guaranteeCompanyList : [],//
			selectItem : [],
			companyDisabled:false,
			guaranteeTypeLsit : [
				{tableId:"1",theName:"银行支付保证"},
				{tableId:"2",theName:"支付保险"},
				{tableId:"3",theName:"支付担保"},
			],
			paymentLinesList : [
				{tableId:"50",theName:"50"},
				{tableId:"60",theName:"60"},
				{tableId:"70",theName:"70"},
				{tableId:"80",theName:"80"}
			],//支付保证上限比例
			controlPercentageLsit : [
				{tableId:20,theName:20},
				{tableId:30,theName:30},
				{tableId:40,theName:40},
				{tableId:50,theName:50},
			],
			companyName :"",
			projectName : "",
			//分页
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			
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
			blurPaymentBondAmount : blurPaymentBondAmount,//本次保函金额
			addThousandsPaymentBondAmont : addThousandsPaymentBondAmont,
			comPaymentBondAmont : comPaymentBondAmont,
			subForm : subForm,//点击提交按钮
			loadGuaranteeCompany : loadGuaranteeCompany,
			handleSelectionChange : handleSelectionChange,
			getProjectId : function(data){
				addVue.projectId = data.tableId;
				addVue.projectName = data.theName
				 changeProjectHandle();
			},
			getCompanyId : function(data){
				addVue.companyId = data.tableId;
				addVue.companyName = data.theName
				changeCompanyHandleFun();
			},
			getControlPercentageId: function(data){
				addVue.controlPercentage = data.theName;
				var guaranteedSumAmount  = null;
				var init = 0;
				addVue.empj_PaymentBondAddList.forEach(function(item){
					item.controlAmount = addThousands(Number(commafyback(item.orgLimitedAmount)) * Number(commafyback(addVue.controlPercentage))/100);
					let releaseAmountNow = Number(commafyback(item.currentEscrowFund)) -  Number(commafyback(item.spilloverAmount)) -  Number(commafyback(item.controlAmount))
					item.releaseAmount =  addThousands(releaseAmountNow>0?releaseAmountNow : 0);
					if(item.paymentBondAmount && item.paymentBondAmount !=''){
						item.actualReleaseAmount = addThousands(Math.min(Number(commafyback(item.paymentBondAmount)),Number(commafyback(item.releaseAmount))));
						
						//item.canApplyAmount = addThousands(Number(commafyback(item.spilloverAmount)),Number(commafyback(item.actualReleaseAmount)));
						item.canApplyAmount = addThousands(Number(commafyback(item.spilloverAmount)) + Number(commafyback(item.actualReleaseAmount)));
						let afterCashLimitedAmountNow = Number(commafyback(item.orgLimitedAmount))- Number(commafyback(item.actualReleaseAmount))
						item.afterCashLimitedAmount = addThousands(afterCashLimitedAmountNow>0?afterCashLimitedAmountNow:0);
						item.afterEffectiveLimitedAmount = addThousands(Math.min(Number(commafyback(item.effectiveLimitedAmount)),Number(commafyback(item.afterCashLimitedAmount))));
						//item.afterEffectiveLimitedAmount = addThousands(Math.min(Number(commafyback(item.nodeLimitedAmount)),Number(commafyback(item.afterCashLimitedAmount))));
						guaranteedSumAmount += Number(commafyback(item.paymentBondAmount))
					}
					addVue.$set(addVue.empj_PaymentBondAddList,init++,item);
				})
			},
			getGuaranteeTypeId : function(data){
				addVue.guaranteeType = data.tableId;
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
			getMoneyAll: function(){
				addVue.empj_PaymentBondAddList.forEach(function(item){
					console.log(addVue.controlPercentage)
					item.controlAmount = addThousands(Number(commafyback(item.orgLimitedAmount)) * Number(commafyback(addVue.controlPercentage))/100);
					console.log(item.controlAmount )
					item.releaseAmount =  addThousands(Number(commafyback(item.currentEscrowFund)) -  Number(commafyback(item.spilloverAmount)) -  Number(commafyback(item.controlAmount)));
					if(item.paymentBondAmount && item.paymentBondAmount !=''){
						item.actualReleaseAmount = addThousands(Math.min(Number(commafyback(item.paymentBondAmount)),Number(commafyback(item.releaseAmount))));
						//item.canApplyAmount = addThousands(Number(commafyback(item.spilloverAmount)),Number(commafyback(item.actualReleaseAmount)));
						item.canApplyAmount = addThousands(Number(commafyback(item.spilloverAmount)) + Number(commafyback(item.actualReleaseAmount)));
						let afterCashLimitedAmountNow = Number(commafyback(item.orgLimitedAmount))- Number(commafyback(item.actualReleaseAmount))
						item.afterCashLimitedAmount = addThousands(afterCashLimitedAmountNow>0?afterCashLimitedAmountNow:0);
						item.afterEffectiveLimitedAmount = addThousands(Math.min(Number(commafyback(item.effectiveLimitedAmount)),Number(commafyback(item.afterCashLimitedAmount))));
//						item.afterEffectiveLimitedAmount = addThousands(Math.min(Number(commafyback(item.nodeLimitedAmount)),Number(commafyback(item.afterCashLimitedAmount))));
						guaranteedSumAmount += Number(commafyback(item.paymentBondAmount))
					}
					item.buildProjectPayDisabled = true;
				})
				addVue.empj_PaymentBondAddList = addVue.empj_PaymentBondAddList;
			}
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
		addVue.empj_PaymentBondAddList.forEach(function(item){
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
			busiCode : "06120501", //业务编码
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
				
			}
		});
	}
	
	function loadUpload(){
		var model = {
			interfaceVersion:this.interfaceVersion,
			busiType : "06120501", //业务编码
		}
		new ServerInterface(baseInfo.attachmentCfgList).execute(addVue.getSearchForm(), function(jsonObj)
				{
					if(jsonObj.result != "success")
					{
						generalErrorModal(jsonObj,jsonObj.info);
					}
					else
					{
						
						addVue.loadUploadList = jsonObj.sm_AttachmentCfgList;
						if(addVue.smAttachmentList !=undefined && addVue.smAttachmentList.length>0){
							addVue.hideShow = true;
						}
					}
				});
	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getAddForm()
	{
		
		var childArr = [];
		if(addVue.selectItem !=undefined && addVue.selectItem.length>0){
			addVue.selectItem.forEach(function(item){
				item.buildingId = item.tableId
				item.actualReleaseAmount = Number(commafyback(item.actualReleaseAmount))
				item.canApplyAmount = Number(commafyback(item.canApplyAmount))
				item.afterCashLimitedAmount = Number(commafyback(item.afterCashLimitedAmount))
				item.afterEffectiveLimitedAmount = Number(commafyback(item.afterEffectiveLimitedAmount))
				item.controlAmount = Number(commafyback(item.controlAmount))
				item.controlAmount = Number(commafyback(item.controlAmount))
				item.nodeLimitedAmount = Number(commafyback(item.nodeLimitedAmount))
				item.orgLimitedAmount = Number(commafyback(item.orgLimitedAmount))
				item.paymentBondAmount = Number(commafyback(item.paymentBondAmount))
				item.releaseAmount = Number(commafyback(item.releaseAmount))
				item.spilloverAmount = Number(commafyback(item.spilloverAmount))
				item.currentEscrowFund = Number(commafyback(item.currentEscrowFund))
				item.effectiveLimitedAmount = Number(commafyback(item.effectiveLimitedAmount))
	    		childArr.push(item);
	    	})
		}
//    	childArr = JSON.stringify(childArr);
		var fileUploadList = addVue.$refs.listenUploadData.uploadData;
		fileUploadList = JSON.stringify(fileUploadList);
		
		return {
			interfaceVersion:this.interfaceVersion,
			applyDate : this.applyDate,	
			companyId : this.companyId,
			companyName : this.companyName,
			projectId : this.projectId,
			projectName : this.projectName,
			guaranteeNo : this.guaranteeNo,
			controlPercentage : this.controlPercentage,
			guaranteedSumAmount : Number(commafyback(this.guaranteedSumAmount)),
			guaranteeCompany : this.guaranteeCompany,
			guaranteeType : this.guaranteeType,
			personOfProfits : this.personOfProfits,
			remark : this.remark,
			empj_PaymentBondChildList : childArr,
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
				addVue.empj_PaymentBondAddList = jsonObj.buildingList;
				var guaranteedSumAmount = null;
				addVue.empj_PaymentBondAddList.forEach(function(item){
					item.orgLimitedAmount = addThousands(item.orgLimitedAmount);
					item.nodeLimitedAmount = addThousands(item.nodeLimitedAmount);
					item.currentEscrowFund = addThousands(item.currentEscrowFund);
					item.spilloverAmount = addThousands(item.spilloverAmount);
					item.effectiveLimitedAmount = addThousands(item.effectiveLimitedAmount);
					item.controlAmount = addThousands(Number(commafyback(item.orgLimitedAmount)) * Number(commafyback(addVue.controlPercentage))/100);
					let releaseAmountNow = Number(commafyback(item.currentEscrowFund)) -  Number(commafyback(item.spilloverAmount)) -  Number(commafyback(item.controlAmount))
					item.releaseAmount =  addThousands(releaseAmountNow>0?releaseAmountNow : 0);
					if(item.paymentBondAmount && item.paymentBondAmount !=''){
						item.actualReleaseAmount = addThousands(Math.min(Number(commafyback(item.paymentBondAmount)),Number(commafyback(item.releaseAmount))));
						
						//item.canApplyAmount = addThousands(Number(commafyback(item.spilloverAmount)),Number(commafyback(item.actualReleaseAmount)));
						item.canApplyAmount = addThousands(Number(commafyback(item.spilloverAmount)) + Number(commafyback(item.actualReleaseAmount)));
						let afterCashLimitedAmountNow = Number(commafyback(item.orgLimitedAmount))- Number(commafyback(item.actualReleaseAmount))
						item.afterCashLimitedAmount = addThousands(afterCashLimitedAmountNow>0?afterCashLimitedAmountNow:0);
						//item.afterEffectiveLimitedAmount = addThousands(Math.min(Number(commafyback(item.nodeLimitedAmount)),Number(commafyback(item.afterCashLimitedAmount))));
						item.afterEffectiveLimitedAmount = addThousands(Math.min(Number(commafyback(item.effectiveLimitedAmount)),Number(commafyback(item.afterCashLimitedAmount))));
						guaranteedSumAmount += Number(commafyback(item.paymentBondAmount))
					}
					item.buildProjectPayDisabled = true;
				})
				addVue.guaranteedSumAmount = addThousands(guaranteedSumAmount)
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
				addVue.empj_PaymentBondAddList.forEach(function(item){
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
		addVue.empj_PaymentBondAddList.forEach(function(item){
			if(item.empj_BuildingInfoId == act.empj_BuildingInfoId){
				//楼幢项目建设已实际支付金额 buildProjectPaid
				if(act.buildProjectPaid == "" || act.buildProjectPaid == "0"){//楼幢项目建设已实际支付金额 buildProjectPaid为0
					item.countBuildAmountPaid = addThousands((Number(item.buildAmountPaid*100)/100-Number(item.oldBuildProjectPaid*100)/100) + 0);//楼幢项目建设已实际支付累计金额（元）countBuildAmountPaid  楼幢项目建设已实际支付累计金额（元）//buildAmountPaid
					if(act.buildProjectPay == "" || act.buildProjectPay == "0"){//已落实支付保证金额（元）countAmountGuaranteed
						item.countAmountGuaranteed = addThousands(0 + 0);
					}else{
						item.countAmountGuaranteed = addThousands(0 + Number(item.buildProjectPay*100)/100);
					}
				}else{
					if(act.buildProjectPay == "" || act.buildProjectPay == "0"){
						item.countAmountGuaranteed = addThousands(Number(item.buildProjectPaid*100)/100 + 0);
					}else{
						item.countAmountGuaranteed = addThousands(Number(item.buildProjectPaid*100)/100 + Number(item.buildProjectPay*100)/100);
					}
					item.countBuildAmountPaid = addThousands((Number(item.buildAmountPaid*100)/100-Number(item.oldBuildProjectPaid*100)/100) + Number(item.buildProjectPaid*100)/100);//楼幢项目建设已实际支付累计金额（元）
				}
				
				item.countTotalAmountGuaranteed = addThousands(Number(commafyback(item.countAmountGuaranteed)*100)/100 + (Number(item.totalAmountGuaranteed*100)/100-Number(item.amountGuaranteed*100)/100));//已落实支付保证累计金额（元）
			
			}
			alreadyActAmount += Number(commafyback(item.countBuildAmountPaid)*100)/100;
			guaranteAmount += Number(commafyback(item.countTotalAmountGuaranteed)*100)/100;
		})
		addVue.alreadyActualAmount = addThousands(alreadyActAmount);
		addVue.guaranteedAmount = addThousands(guaranteAmount);
	}
	//新增操作------------------楼幢项目建设待支付承保金额
	function blurWaitMoney(wait){
		var waitMoney = null;
		var guaranteAmount = null;//已落实支付保证金额
		addVue.empj_PaymentBondAddList.forEach(function(item){
			if(item.empj_BuildingInfoId == wait.empj_BuildingInfoId){
				if(wait.buildProjectPay == "" || wait.buildProjectPay==0){
					item.countBuildAmountPay = addThousands((Number(item.buildAmountPay*100)/100-Number(item.oldBuildProjectPay*100)/100) + 0);
					if(wait.buildProjectPaid == "" || wait.buildProjectPaid == 0){
						item.countAmountGuaranteed = addThousands(0);
					}else{
						item.countAmountGuaranteed = addThousands(Number(item.buildProjectPaid*100)/100 + 0);
					}
					
				}else{
					if(wait.buildProjectPaid == "" || wait.buildProjectPaid == "0"){
						item.countAmountGuaranteed = addThousands(0 + Number(item.buildProjectPay*100)/100);
					}else{
						item.countAmountGuaranteed = addThousands(Number(item.buildProjectPaid*100)/100 + Number(item.buildProjectPay*100)/100);
					}
					item.countBuildAmountPay = addThousands((Number(item.buildAmountPay*100)/100-Number(item.oldBuildProjectPay*100)/100) + Number(item.buildProjectPay*100)/100);
					
				}
				item.countTotalAmountGuaranteed = addThousands(Number(commafyback(item.countAmountGuaranteed)*100)/100 + (Number(item.totalAmountGuaranteed*100)/100-Number(item.amountGuaranteed*100)/100));
			}
			waitMoney += Number(commafyback(item.countBuildAmountPay)*100)/100;
			guaranteAmount += Number(commafyback(item.countTotalAmountGuaranteed)*100)/100;
		})
		addVue.actualAmount = addThousands(waitMoney);
		addVue.guaranteedAmount = addThousands(guaranteAmount);
	}
	
	//本次保函金额
	//根据本次保函金额计算实际可替代保证额度   实际可替代保证额度 = min(最高可是放的现金额度 ： 本次保函金额)
	//业务办理后现金受限额度  = 初始托管总额 - 实际可替代保证额度
	//业务办理后有效受限额度 = min(当前节点受限额度 ： 业务办理后现金受限额度 )
	
	function blurPaymentBondAmount(val){
		var guaranteedSumAmount = null;
		addVue.empj_PaymentBondAddList.forEach(function(item){
			if(item.paymentBondAmount && item.paymentBondAmount !=''){
				item.actualReleaseAmount = addThousands(Math.min(Number(commafyback(item.paymentBondAmount)),Number(commafyback(item.releaseAmount))));
				//item.canApplyAmount = addThousands(Number(commafyback(item.spilloverAmount)),Number(commafyback(item.actualReleaseAmount)));
				item.canApplyAmount = addThousands(Number(commafyback(item.spilloverAmount)) + Number(commafyback(item.actualReleaseAmount)));
				let afterCashLimitedAmountNow = Number(commafyback(item.orgLimitedAmount))- Number(commafyback(item.actualReleaseAmount))
				item.afterCashLimitedAmount = addThousands(afterCashLimitedAmountNow>0?afterCashLimitedAmountNow:0); 
				item.afterEffectiveLimitedAmount = addThousands(Math.min(Number(commafyback(item.effectiveLimitedAmount)),Number(commafyback(item.afterCashLimitedAmount))));
				//item.afterEffectiveLimitedAmount = addThousands(Math.min(Number(commafyback(item.nodeLimitedAmount)),Number(commafyback(item.afterCashLimitedAmount))));
				guaranteedSumAmount += Number(commafyback(item.paymentBondAmount));
			}
		})
		addVue.guaranteedSumAmount = addThousands(guaranteedSumAmount)
	}
	//
	function addThousandsPaymentBondAmont(val){
		val.paymentBondAmount = addThousands(val.paymentBondAmount)
	}
	function comPaymentBondAmont(val){
		val.paymentBondAmount = commafyback(val.paymentBondAmount)
	}
	
	//列表操作---选择
    function handleSelectionChange(val) {
    	addVue.selectItem = [];
    	var listval = addVue.empj_PaymentBondAddList;
    	var guaranteedSumAmount = null;
		if(val.length != 0){//当前页面没有选中的值    		
			for(var j = 0;j < listval.length;j++){
				// 初始化计数
				var index = 0; 			
				for(var i = 0;i<val.length;i++){
					if(val[i].tableId == listval[j].tableId){
						listval[j].buildProjectPayDisabled = false;
						break;
					}else{    						
						index++;
						if(index == val.length)
						{
							listval[j].buildProjectPayDisabled = true;
							blurPaymentBondAmount(listval[j]);
						}
					} 
					
				}	
			}
			
			val.forEach(function(item){
				if(item.paymentBondAmount !=undefined){
					guaranteedSumAmount += Number(commafyback(item.paymentBondAmount));
				}
			})
			addVue.guaranteedSumAmount = addThousands(guaranteedSumAmount)
			
		}else{//当前页面有选中项
			for (var k = 0; k < listval.length; k++) {
				if(listval[k].buildProjectPaidDisabled == false)
				{
					listval[k].buildProjectPaid = 0;
					listval[k].buildProjectPay =0;
				}
				blurWaitMoney(listval[k]);
				blurActMoney(listval[k]);
				listval[k].buildProjectPayDisabled = true;
    	 	}	
			addVue.guaranteedSumAmount = 0.00
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
//		new ServerInterface(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj)
		new ServerInterfaceJsonBody(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				enterNext2Tab(jsonObj.tableId, '支付保函申请详情', 'empj/Empj_PaymentBondDetail.shtml',jsonObj.tableId+"06120501");
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
	addVue.loadUpload();
	addVue.initData();

	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#Empj_PaymentBondAddDiv",
	"edModelDivId":"#ed1Model",
	"sdModelDivId":"#sd1Model",
	"loadAddInterface":"../Empj_PaymentGuaranteeApplyDetail",
	"attachmentCfgList":"../Sm_AttachmentCfgList",//附件
	"addInterface":"../Empj_PaymentBondAdd",//点击保存接口
	"loadUploadInterface":"../Sm_AttachmentCfgList",
	"companyNameInterface" : "../Emmp_CompanyInfoList", //显示开发企业名称接口
	"changeCompanyInterface" : "../Empj_ProjectInfoList", //改变企业名称接口
	"changeECodeInterface" : "",//改变施工编号带出信息接口
	"changeProjectInterface" : "../Empj_PaymentBondLoadBuildByProjectId",//改变项目名称带出字表信息接口
	"guaranteeCompanyInterface" : "../Emmp_CompanyInfoList",//页面加载显示支付保证出具单位接口
	"getNewDataInterface" : "",//onblur事件带出当前数据的接口
	"subInterface" : "",//点击提交接口
});
