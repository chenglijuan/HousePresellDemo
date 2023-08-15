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
			guaranteedSumAmount : "",//保函总金额
			controlPercentage : "",//保函最低控制线
			personOfProfits : "",//受益人
			guaranteeNo : "",
            errMsg :"",//存放错误提示信息
            loadUploadList: [],
            showDelete : true,
            busiType: "06120501",
            empj_PaymentBondEditList : [],
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
			guaranteeCompanyLsit : [
				{tableId:"1",theName:"银行"},
				{tableId:"2",theName:"保险公司"},
				{tableId:"3",theName:"担保公司"},
			],
			//分页
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			empj_PaymentBond : {},
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
			getControlPercentageId:function(data){
				detailVue.controlPercentage = data.theName;
				var guaranteedSumAmount  = null;
				var init = 0;
				detailVue.empj_PaymentBondEditList.forEach(function(item){
					item.controlAmount = addThousands((commafyback(item.orgLimitedAmount)) * (commafyback(detailVue.controlPercentage))/100);
					let releaseAmountNow = (commafyback(item.currentEscrowFund)) -  (commafyback(item.spilloverAmount)) -  (commafyback(item.controlAmount))
					item.releaseAmount =  addThousands(releaseAmountNow>0?releaseAmountNow : 0);
					if(item.paymentBondAmount && item.paymentBondAmount !=''){
						item.actualReleaseAmount = addThousands(Math.min((commafyback(item.paymentBondAmount)),(commafyback(item.releaseAmount))));
						//item.canApplyAmount = addThousands(Number(commafyback(item.spilloverAmount)),Number(commafyback(item.actualReleaseAmount)));
						item.canApplyAmount = addThousands(Number(commafyback(item.spilloverAmount)) + Number(commafyback(item.actualReleaseAmount)));
						let afterCashLimitedAmountNow = (commafyback(item.orgLimitedAmount))- (commafyback(item.actualReleaseAmount))
						item.afterCashLimitedAmount = addThousands(afterCashLimitedAmountNow>0?afterCashLimitedAmountNow:0);
						item.afterEffectiveLimitedAmount = addThousands(Math.min((commafyback(item.effectiveLimitedAmount)),(commafyback(item.afterCashLimitedAmount))));
						//item.afterEffectiveLimitedAmount = addThousands(Math.min((commafyback(item.nodeLimitedAmount)),(commafyback(item.afterCashLimitedAmount))));
						guaranteedSumAmount += (commafyback(item.paymentBondAmount))
					}
					detailVue.$set(detailVue.empj_PaymentBondEditList,init++,item);
				})
			},
			getGuaranteeTypeId:function(data){
				detailVue.guaranteeType = data.tableId;
			},
			chioceChange : chioceChange,//选择触发事件
			blurPaymentBondAmount : blurPaymentBondAmount,
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
		detailVue.empj_PaymentBondEditList.forEach(function(item){
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
			busiCode : "06120501", //业务编码
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
				detailVue.empj_PaymentBond = jsonObj.empj_PaymentBond;
				detailVue.controlPercentage = jsonObj.empj_PaymentBond.controlPercentage
				detailVue.applyDate = jsonObj.empj_PaymentBond.applyDate;
				detailVue.lastUpdateTimeStamp = jsonObj.empj_PaymentBond.lastUpdateTimeStamp;
				detailVue.guaranteeType = jsonObj.empj_PaymentBond.guaranteeType;
				detailVue.ecode = jsonObj.empj_PaymentBond.ecode;
				detailVue.guaranteedAmount = jsonObj.empj_PaymentBond.guaranteedAmount;
				detailVue.userRecord = jsonObj.empj_PaymentBond.userRecord;
				detailVue.alreadyActualAmount = jsonObj.empj_PaymentBond.alreadyActualAmount;
				detailVue.userUpdate = jsonObj.empj_PaymentBond.userUpdate;
				detailVue.recordTimeStamp = jsonObj.empj_PaymentBond.recordTimeStamp;
				detailVue.actualAmount = jsonObj.empj_PaymentBond.actualAmount;
				detailVue.remark = jsonObj.empj_PaymentBond.remark;
				detailVue.guaranteeNo = jsonObj.empj_PaymentBond.guaranteeNo;
				detailVue.projectId = jsonObj.empj_PaymentBond.projectId;
				detailVue.companyId = jsonObj.empj_PaymentBond.companyId;
				detailVue.eCode = jsonObj.empj_PaymentBond.eCode; 
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
				detailVue.guaranteeCompany = jsonObj.empj_PaymentBond.guaranteeCompany;
				detailVue.companyName = jsonObj.empj_PaymentBond.companyName;
				detailVue.personOfProfits = jsonObj.empj_PaymentBond.personOfProfits;
				detailVue.guaranteedSumAmount = addThousands(jsonObj.empj_PaymentBond.guaranteedSumAmount)
				detailVue.empj_PaymentBondEditList = jsonObj.empj_PaymentBondChildList;
				var alreadyActAmount = null;//项目建设工程已实际支付金额
				var guaranteAmount = null;//已落实支付保证金额
				var waitMoney = null;//项目建设工程待支付承保金额
				detailVue.empj_PaymentBondEditList.forEach(function(item, index){
					item.orgLimitedAmount = addThousands(item.orgLimitedAmount);
					item.nodeLimitedAmount = addThousands(item.nodeLimitedAmount);
					item.currentEscrowFund = addThousands(item.currentEscrowFund);
					item.spilloverAmount = addThousands(item.spilloverAmount);
					item.controlAmount = addThousands(item.controlAmount);
					item.releaseAmount = addThousands(item.releaseAmount>0?item.releaseAmount:0);
					item.paymentBondAmount = addThousands(item.paymentBondAmount);
					item.actualReleaseAmount = addThousands(item.actualReleaseAmount>0?item.actualReleaseAmount:0);
					item.canApplyAmount = addThousands(item.canApplyAmount>0?item.canApplyAmount:0);
					item.afterCashLimitedAmount = addThousands(item.afterCashLimitedAmount>0?item.afterCashLimitedAmount:0);
					item.afterEffectiveLimitedAmount = addThousands(item.afterEffectiveLimitedAmount);
					item.effectiveLimitedAmount = addThousands(item.effectiveLimitedAmount);
					item.buildProjectPaidDisabled = true;
					item.buildProjectPayDisabled = true;
					alreadyActAmount += (commafyback(item.countBuildAmountPaid)*100)/100;
					guaranteAmount += (commafyback(item.countTotalAmountGuaranteed)*100)/100;
					waitMoney += (commafyback(item.countBuildAmountPay)*100)/100;
					if(item.buildProjectPaid > 0 || item.buildProjectPay > 0) {
	                    detailVue.$nextTick(function() {
							detailVue.$refs.moviesTable.toggleRowSelection(detailVue.empj_PaymentBondEditList[index], true);
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
			var childArr = [];
			if(detailVue.empj_PaymentBondEditList !=undefined && detailVue.empj_PaymentBondEditList.length>0){
				detailVue.empj_PaymentBondEditList.forEach(function(item){
					item.buildingId = item.buildingId
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
			var fileUploadList = detailVue.$refs.listenUploadData.uploadData;
			fileUploadList = JSON.stringify(fileUploadList);
			
			return {
				interfaceVersion:this.interfaceVersion,
				tableId : this.empj_PaymentBond.tableId,
				eCode : this.empj_PaymentBond.eCode,
				applyDate : this.applyDate,	
				companyId : this.companyId,
				companyName : this.companyName,
				projectId : this.projectId,
				projectName : this.empj_PaymentBond.projectName,
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
				detailVue.empj_PaymentBondEditList = jsonObj.empj_PaymentBondEditList;
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
		detailVue.empj_PaymentBondEditList.forEach(function(item){
			if(item.empj_BuildingInfoId == act.empj_BuildingInfoId){//楼幢项目建设已实际支付金额 buildProjectPaid
				if(act.buildProjectPaid == "" || act.buildProjectPaid == "0"){//楼幢项目建设已实际支付金额 buildProjectPaid为0
					item.countBuildAmountPaid = addThousands(((item.buildAmountPaid*100)/100-(item.oldBuildProjectPaid*100)/100) + 0);//楼幢项目建设已实际支付累计金额（元）countBuildAmountPaid  楼幢项目建设已实际支付累计金额（元）//buildAmountPaid
					if(act.buildProjectPay == "" || act.buildProjectPay == "0"){//已落实支付保证金额（元）countAmountGuaranteed
						item.countAmountGuaranteed = addThousands(0 + 0);
					}else{
						item.countAmountGuaranteed = addThousands(0 + (item.buildProjectPay*100)/100);
					}
				}else{
					if(act.buildProjectPay == "" || act.buildProjectPay == "0"){
						item.countAmountGuaranteed = addThousands((item.buildProjectPaid*100)/100 + 0);
					}else{
						item.countAmountGuaranteed = addThousands((item.buildProjectPaid*100)/100 + (item.buildProjectPay*100)/100);
					}
					item.countBuildAmountPaid = addThousands(((item.buildAmountPaid*100)/100-(item.oldBuildProjectPaid*100)/100) + (item.buildProjectPaid*100)/100);//楼幢项目建设已实际支付累计金额（元）
				}
				
				item.countTotalAmountGuaranteed = addThousands((commafyback(item.countAmountGuaranteed)*100)/100 + ((item.totalAmountGuaranteed*100)/100-(item.amountGuaranteed*100)/100));//已落实支付保证累计金额（元）
			}
			alreadyActAmount += (commafyback(item.countBuildAmountPaid)*100)/100;
			guaranteAmount += (commafyback(item.countTotalAmountGuaranteed)*100)/100;
		})
		detailVue.alreadyActualAmount = addThousands(alreadyActAmount);
		detailVue.guaranteedAmount = addThousands(guaranteAmount);
	}
	//新增操作------------------楼幢项目建设待支付承保金额
	function blurWaitMoney(wait){
		console.log(wait);
		var waitMoney = null;
		var guaranteAmount = null;//已落实支付保证金额
		detailVue.empj_PaymentBondEditList.forEach(function(item){
			if(item.empj_BuildingInfoId == wait.empj_BuildingInfoId){
				if(wait.buildProjectPay == "" || wait.buildProjectPay=="0"){
					item.countBuildAmountPay = addThousands(((item.buildAmountPay*100)/100-(item.oldBuildProjectPay*100)/100) + 0);
					if(wait.buildProjectPaid == "" || wait.buildProjectPaid == "0"){
						item.countAmountGuaranteed = addThousands(0 + 0);
					}else{
						item.countAmountGuaranteed = addThousands((item.buildProjectPaid*100)/100 + 0);
					}
					
				}else{
					if(wait.buildProjectPaid == "" || wait.buildProjectPaid == "0"){
						item.countAmountGuaranteed = addThousands(0 + (item.buildProjectPay*100)/100);
					}else{
						item.countAmountGuaranteed = addThousands((item.buildProjectPaid*100)/100 + (item.buildProjectPay*100)/100);
					}
					console.log("老数据"+((item.oldBuildProjectPay*100)/100));
					item.countBuildAmountPay = addThousands(((item.buildAmountPay*100)/100-(item.oldBuildProjectPay*100)/100) + (item.buildProjectPay*100)/100);
					
				}
				
				item.countTotalAmountGuaranteed = addThousands((commafyback(item.countAmountGuaranteed)*100)/100 + ((item.totalAmountGuaranteed*100)/100-(item.amountGuaranteed*100)/100));
			}
			waitMoney += (commafyback(item.countBuildAmountPay)*100)/100;
			guaranteAmount += (commafyback(item.countTotalAmountGuaranteed)*100)/100;
		})
		detailVue.actualAmount = addThousands(waitMoney);
		detailVue.guaranteedAmount = addThousands(guaranteAmount);
	}
	var selectArr = [];
	//列表操作---选择
    function handleSelectionChange(val) {
    	detailVue.selectItem = [];
    	var guaranteedSumAmount = null;
    	var listval = detailVue.empj_PaymentBondEditList;
    	
    	
    	
    	
    	
    		if(val.length != 0){//当前页面没有选中的值    		
    			for(var j = 0;j < listval.length;j++){
    				// 初始化计数
    				var index = 0; 			
    				for(var i = 0;i<val.length;i++){
    					if(val[i].tableId == listval[j].tableId){
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
    							//blurWaitMoney(listval[j]);
    							//blurActMoney(listval[j]);
    							blurPaymentBondAmount(listval[j]);
    						}
    					}  
    					
    				}	
    			}
    			val.forEach(function(item){
    				guaranteedSumAmount += Number(commafyback(item.paymentBondAmount))
    			})
    			detailVue.guaranteedSumAmount = addThousands(guaranteedSumAmount)
    		}else{//当前页面有选中项
    			for (var k = 0; k < listval.length; k++) {
    				if(listval[k].buildProjectPaidDisabled == false)
    				{
    					listval[k].buildProjectPaid = 0;
    					listval[k].buildProjectPay =0;
    				}
    				//blurWaitMoney(listval[k]);
					//blurActMoney(listval[k]);
					blurPaymentBondAmount(listval[k]);
					listval[k].buildProjectPaidDisabled = true;
					listval[k].buildProjectPayDisabled = true;
        	 	}
    			detailVue.guaranteedSumAmount = 0.00
			}
      		if(val.length != 0) {
    			detailVue.selectItem = val;
    		}
    	}
	//本次保函金额
	//根据本次保函金额计算实际可替代保证额度   实际可替代保证额度 = min(最高可是放的现金额度 ： 本次保函金额)
	//业务办理后现金受限额度  = 初始托管总额 - 实际可替代保证额度
	//业务办理后有效受限额度 = min(当前节点受限额度 ： 业务办理后现金受限额度 )
	
	function blurPaymentBondAmount(val){
		var guaranteedSumAmount = null;
		detailVue.empj_PaymentBondEditList.forEach(function(item){
			if(item.paymentBondAmount && item.paymentBondAmount !=''){
				item.actualReleaseAmount = addThousands(Math.min((commafyback(item.paymentBondAmount)),(commafyback(item.releaseAmount))));
				//item.canApplyAmount = addThousands(Number(commafyback(item.spilloverAmount)),Number(commafyback(item.actualReleaseAmount)));
				item.canApplyAmount = addThousands(Number(commafyback(item.spilloverAmount)) + Number(commafyback(item.actualReleaseAmount)));
				let afterCashLimitedAmountNow = (commafyback(item.orgLimitedAmount))- (commafyback(item.actualReleaseAmount))
				item.afterCashLimitedAmount = addThousands(afterCashLimitedAmountNow>0?afterCashLimitedAmountNow:0);
				item.afterEffectiveLimitedAmount = addThousands(Math.min((commafyback(item.effectiveLimitedAmount)),(commafyback(item.afterCashLimitedAmount))));
				//item.afterEffectiveLimitedAmount = addThousands(Math.min((commafyback(item.nodeLimitedAmount)),(commafyback(item.afterCashLimitedAmount))));
				guaranteedSumAmount += Number(commafyback(item.paymentBondAmount))
			}
		})
		detailVue.guaranteedSumAmount = addThousands(guaranteedSumAmount)
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
		new ServerInterfaceJsonBody(baseInfo.addInterface).execute(detailVue.getAddForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				enterNext2Tab(detailVue.tableId, '支付保函申请详情', 'empj/Empj_PaymentBondDetail.shtml',detailVue.tableId+"06120501");
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
	"addDivId":"#Empj_PaymentBondEditDiv",
	"edModelDivId":"#ed1Model",
	"sdModelDivId":"#sd1Model",
	"detailInterface":"../Empj_PaymentBondDetail",
	"addInterface":"../Empj_PaymentBondEdit",
	
	"loadUploadInterface":"../Sm_AttachmentCfgList",
	"companyNameInterface" : "../Emmp_CompanyInfoList", //显示开发企业名称接口
	"changeCompanyInterface" : "../Empj_ProjectInfoList", //改变企业名称接口
	"changeECodeInterface" : "",//改变施工编号带出信息接口
	"changeProjectInterface" : "",//改变项目名称带出字表信息接口
	"guaranteeCompanyInterface" : "../Emmp_CompanyInfoList",//页面加载显示支付保证出具单位接口
	"getNewDataInterface" : "",//onblur事件带出当前数据的接口
});
