(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			tgpf_SpecialFundAppropriatedDetailModel: {},
			delDisabled : true,
			editDisabled : true,
			addDisabled : false,
			tgpf_SpacialFundAppropriatedAddList : [],
			tableId : "",
			appropriatedType:1,//特殊拨付类型
			accountId : "",
			totalApplyAmount : "",
			appropriatedRemark : "",
            showDelete : true,
            busiType : "061206",
            afId:"",
		    workflowId: "",
		    af_busiCode : "",
		    projectId : "",
		    tgxy_BankAccountEscrowedList : [],
		    theAccountId : "",
		    theAccount : "",
		    shortNameOfBank : "",
		    canPayAmount : "",//账户余额
	        currentBalance : "",//活期余额
	        theNameOfBankBranch : "",//托管银行
	        appliedAmount : "",
	        payoutChannel : "1",
	        payoutDate : "",
	        billNumber : "",
	        payoutDate : "",
	        selectItem : [],
	        specialEditFundId : "",
	        isEdit : false,
	        subDisabled : true,
	        showButton : true,
	        isDel : true,
	        isDelAll : true,
	        workflowState : "审核中",
	        tgpf_SpecialFundAppropriated_AFDtlEdit : {},
	        showInfo : true,
	        trusteeBankList :[],
	        theBankId : "",
//	        bankAccountEscrowed : {
//	        	shortNameOfBank : "",
//	        }
	        smAttachmentList : [], //页面显示已上传的文件
			loadUploadList : [],
			showDelete : false,
			sourcePage:"",//来源页面
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			showAddSpecialFundModel : showAddSpecialFundModel,
			showAddSpecialFundModelForEdit : showAddSpecialFundModelForEdit,
			showDelModal : showDelModal,
			listItemSelectHandle : listItemSelectHandle,
			showModal : showModal,
			getEscrowAccountFrom : getEscrowAccountFrom,
			getEscrowAccount : getEscrowAccount,
			getTrusteeBankFrom : getTrusteeBankFrom,
			getTrusteeBank : getTrusteeBank,
			changeBankAccountEscrowed : changeBankAccountEscrowed,
			subSpecialFundAppropriated : subSpecialFundAppropriated,
			sm_SpecialFundAppropriatedDel : sm_SpecialFundAppropriatedDel,
			subAllSpecialFundModel : subAllSpecialFundModel,
			subEditSpecialFundAppropriated : subEditSpecialFundAppropriated,
			showModalConfirm : showModalConfirm,
			sm_SpecialFundAppropriatedDelAll : sm_SpecialFundAppropriatedDelAll,
			showDelAllModal : showDelAllModal,
			changeBankList : changeBankList,
		},
		computed:{
			 
		},
		components : {
			"my-uploadcomponent":fileUpload,
			'vue-select': VueSelect,
		},
		watch:{
			selectItem : selectedItemChanged,
		},
		mounted:function(){
			/**
			 * 初始化日期插件
			 */
			laydate.render({
			  elem: '#payoutDateId',
			  done: function(value, date, endDate){
			    detailVue.payoutDate = value;
			  }
			});
			laydate.render({
			  elem: '#payoutDateIdEdit',
			  done: function(value, date, endDate){
			    detailVue.tgpf_SpecialFundAppropriated_AFDtlEdit.payoutDate = value;
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
			tableId:this.tableId,
			afId: this.afId,
		    workflowId: this.workflowId,
		    busiCode:'061206',
		    currentWworkflowId : this.workflowId,
		}
	}
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}
	//详情操作--------------
	function refresh()
	{
		if (detailVue.tableId == null || detailVue.tableId < 1) 
		{
			return;
		}
		initButtonList();
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
				
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
				
				console.log("jsonObj.isApprover="+jsonObj.isApprover);
				if(jsonObj.isApprover)
				{
					detailVue.showInfo = false;
				}
				
				detailVue.tgpf_SpecialFundAppropriatedDetailModel = jsonObj.tgpf_SpecialFundAppropriated_AF;
				//操作按钮控制
				if(detailVue.tgpf_SpecialFundAppropriatedDetailModel.approvalState == "审核中")
				{
					detailVue.isEdit = true;
					initButtonList();
				}
				
				detailVue.projectId = jsonObj.tgpf_SpecialFundAppropriated_AF.projectId;
				detailVue.tgpf_SpecialFundAppropriatedDetailModel.orgLimitedAmount = addThousands(detailVue.tgpf_SpecialFundAppropriatedDetailModel.orgLimitedAmount);
                detailVue.tgpf_SpecialFundAppropriatedDetailModel.nodeLimitedAmount = addThousands(detailVue.tgpf_SpecialFundAppropriatedDetailModel.nodeLimitedAmount);
				detailVue.tgpf_SpecialFundAppropriatedDetailModel.cashLimitedAmount = addThousands(detailVue.tgpf_SpecialFundAppropriatedDetailModel.cashLimitedAmount);
				detailVue.tgpf_SpecialFundAppropriatedDetailModel.effectiveLimitedAmount = addThousands(detailVue.tgpf_SpecialFundAppropriatedDetailModel.effectiveLimitedAmount);
				detailVue.tgpf_SpecialFundAppropriatedDetailModel.totalAccountAmount = addThousands(detailVue.tgpf_SpecialFundAppropriatedDetailModel.totalAccountAmount);
				detailVue.tgpf_SpecialFundAppropriatedDetailModel.currentEscrowFund = addThousands(detailVue.tgpf_SpecialFundAppropriatedDetailModel.currentEscrowFund);
				detailVue.tgpf_SpecialFundAppropriatedDetailModel.appropriateFrozenAmount = addThousands(detailVue.tgpf_SpecialFundAppropriatedDetailModel.appropriateFrozenAmount);
				detailVue.tgpf_SpecialFundAppropriatedDetailModel.payoutAmount = addThousands(detailVue.tgpf_SpecialFundAppropriatedDetailModel.payoutAmount);
				detailVue.tgpf_SpecialFundAppropriatedDetailModel.allocableAmount = addThousands(detailVue.tgpf_SpecialFundAppropriatedDetailModel.allocableAmount);
				detailVue.tgpf_SpecialFundAppropriatedDetailModel.totalApplyAmount = addThousands(detailVue.tgpf_SpecialFundAppropriatedDetailModel.totalApplyAmount);
				
				detailVue.af_busiCode = jsonObj.busiCode;
				detailVue.tgpf_SpacialFundAppropriatedAddList = jsonObj.tgpf_SpecialFundAppropriated_AF.AFDtlList;
				
				/*
				 * 获取当前申请单信息
				 * 只有当审批节点状态处于：‘审核中’的，才可以进行清空操作
				 */
				detailVue.workflowState = jsonObj.workflowState;
				if(detailVue.workflowState == '审核中')
				{
					if(detailVue.tgpf_SpacialFundAppropriatedAddList!=undefined&&detailVue.tgpf_SpacialFundAppropriatedAddList.length>0)
					{
						detailVue.isDelAll = false;
					}
				}
				
				console.log(detailVue.tgpf_SpacialFundAppropriatedAddList);
				if(detailVue.tgpf_SpacialFundAppropriatedAddList!=undefined&&detailVue.tgpf_SpacialFundAppropriatedAddList.length>0)
				{
					detailVue.addDisabled = true;
					detailVue.delDisabled = true;
					detailVue.subDisabled = true;
					detailVue.isDel = false;
					detailVue.tgpf_SpacialFundAppropriatedAddList.forEach(function(item){
						item.accountBalance = addThousands(item.accountBalance);
						item.appliedAmount = addThousands(item.appliedAmount);
						item.applyRefundPayoutAmount = addThousands(item.applyRefundPayoutAmount);
					})
				}
			}
		});
	}
	//审批操作---------------------获取托管账户参数
	function getEscrowAccountFrom(){
		return {
			interfaceVersion:this.interfaceVersion,
			projectId : this.projectId,
			bankBranchId : this.theBankId,
		}
	}
	//审批操作---------------------获取托管账户
	function getEscrowAccount(){
		new ServerInterface(baseInfo.escrowAccountInterface).execute(detailVue.getEscrowAccountFrom(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				if(jsonObj.tgxy_BankAccountEscrowedList.length>0){
					detailVue.theAccount = jsonObj.tgxy_BankAccountEscrowedList[0].theAccount;
					detailVue.theAccountId = jsonObj.tgxy_BankAccountEscrowedList[0].tableId;
					detailVue.canPayAmount = addThousands(jsonObj.tgxy_BankAccountEscrowedList[0].currentBalance);
				}
				//detailVue.theAccount = 
				//detailVue.tgxy_BankAccountEscrowedList = jsonObj.tgxy_BankAccountEscrowedList;
			}
		});
	}
	//审批操作--------------------获取托管银行
	function getTrusteeBankFrom(){
		return {
			interfaceVersion:this.interfaceVersion,
			projectId : this.projectId,
		}
	}
	//审批操作--------------------获取托管银行
	function getTrusteeBank(){
		new ServerInterface(baseInfo.bankBranchInterface).execute(detailVue.getTrusteeBankFrom(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				detailVue.trusteeBankList = jsonObj.emmp_BankBranchList;
			}
		});
	}
	
	function changeBankList(){
		detailVue.tgxy_BankAccountEscrowedList = [];
		detailVue.theAccountId = "";
		getEscrowAccount();
	}
	//审批操作------------------获取托管信息
	function changeBankAccountEscrowed(){
		detailVue.tgxy_BankAccountEscrowedList.forEach(function(item){
			if(item.tableId == detailVue.theAccountId)
			{
				detailVue.theNameOfBankBranch = item.theNameOfBankBranch;
				detailVue.canPayAmount = addThousands(item.canPayAmount);
				detailVue.currentBalance = addThousands(item.currentBalance);
			}
		})
	}
	
	function showModalConfirm()
	{
		/*
		 * 点击审批时，计算划款信息金额相加等于申请金额
		 * 否则给出提示
		 */
		var totalApplyAmount = detailVue.tgpf_SpecialFundAppropriatedDetailModel.totalApplyAmount;
		var appliedAmountCount = 0.00;
		
		console.log(detailVue.tgxy_BankAccountEscrowedList);
		detailVue.tgpf_SpacialFundAppropriatedAddList.forEach(function(item){
			
			appliedAmountCount += addThousands(item.appliedAmount);
			
		});
		
		console.log("appliedAmountCount="+appliedAmountCount+";totalApplyAmount="+totalApplyAmount);
		if(totalApplyAmount==appliedAmountCount)
		{
			showModal();
		}
		else
		{
		    $("#selectMsgModal").html("划款金额与申请金额不符，请检查后重试！")
		    $("#selectM").modal('show', {
		        backdrop: 'static'
		    });
		}
	}
	
	function showModal()
	{
		approvalModalVue.getModalWorkflowList();
    }
	
	//新增操作------------------------删除
	function showDelModal(){
		$(baseInfo.deleteSpecialDiv).modal({
			backdrop:"static"
		})
	}
	
	//清空
	function showDelAllModal(){
		$(baseInfo.deleteAllSpecialDiv).modal({
			backdrop:"static"
		})
	}
	
	
	//清空所有数据
	function sm_SpecialFundAppropriatedDelAll(){
		
		var addList = detailVue.tgpf_SpacialFundAppropriatedAddList;
		
		var idArr = [];
		if(addList.length>0)
		{
			addList.forEach(function (item){
				idArr.push(item.tableId);
			});
		}
		
		var model = {
			interfaceVersion:this.interfaceVersion,
			idArr : idArr,
		}
		
		new ServerInterface(baseInfo.deleteInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				
				detailVue.isDelAll = true;
				
				detailVue.addDisabled = false;
				detailVue.isDel = true;
				
				generalSuccessModal();
				refresh();
			}
			
			$(baseInfo.deleteAllSpecialDiv).modal("hide");
			
		});
		
	}
	
	//删除暂存态数据
	function sm_SpecialFundAppropriatedDel(){
		var addList = detailVue.tgpf_SpacialFundAppropriatedAddList;
		var tableArr = detailVue.selectItem;
		for(var i = 0;i<addList.length;i++){
			for(var j = 0;j<tableArr.length;j++){
				if(addList[i].tableId == tableArr[j]){
					addList.splice(i,1);
					$(baseInfo.deleteSpecialDiv).modal("hide");
				}
			}
		}
	}
	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		detailVue.isAllSelected = (detailVue.tgpf_SpacialFundAppropriatedAddList.length > 0)
		&&	(detailVue.selectItem.length == detailVue.tgpf_SpacialFundAppropriatedAddList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(detailVue.selectItem.length == detailVue.tgpf_SpacialFundAppropriatedAddList.length)
	    {
	    	detailVue.selectItem = [];
	    }
	    else
	    {
	    	detailVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	detailVue.tgpf_SpacialFundAppropriatedAddList.forEach(function(item) {
	    		detailVue.selectItem.push(item.tableId);
	    	});
	    }
	}
	
	function showAddSpecialFundModel(){
		clearForm();
		//getEscrowAccount();
		getTrusteeBank();
		$(baseInfo.addSpecialModel).modal({
			backdrop:"static"
		})
	}
	
	function clearForm(){
		detailVue.appliedAmount = "";
		detailVue.payoutChannel = "1";
		detailVue.payoutDate = "";
		detailVue.billNumber = "";
		detailVue.theAccountId = "";
		detailVue.theAccount = "";
		detailVue.theNameOfBankBranch = "";
		detailVue.canPayAmount = "";
		detailVue.currentBalance = "";
		detailVue.theBankId = "";
		
	}
	//审批操作-----------------修改
	function showAddSpecialFundModelForEdit(){
		clearForm();
		getEscrowAccount();
		var model = {
			interfaceVersion:this.interfaceVersion,
			idArr  : this.selectItem,
			tableId : this.selectItem[0],
		}
		$(baseInfo.editSpecialModel).modal({
			backdrop:"static"
		})
    	detailVue.specialEditFundId = this.selectItem[0];
		console.log("*****************");
		new ServerInterface(baseInfo.escrowAccountDetailInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				detailVue.tgpf_SpecialFundAppropriatedDetailModel.allocableAmount = jsonObj.tgpf_SpecialFundAppropriated_AFDtl.theCodeOfAf;
				detailVue.tgpf_SpecialFundAppropriated_AFDtlEdit = jsonObj.tgpf_SpecialFundAppropriated_AFDtl;
				/*detailVue.appliedAmount = jsonObj.tgpf_SpecialFundAppropriated_AFDtl.appliedAmount;
				detailVue.payoutChannel = jsonObj.tgpf_SpecialFundAppropriated_AFDtl.payoutChannel;
				detailVue.payoutDate = jsonObj.tgpf_SpecialFundAppropriated_AFDtl.payoutDate;
				detailVue.billNumber = jsonObj.tgpf_SpecialFundAppropriated_AFDtl.billNumber;
				detailVue.theAccountId = jsonObj.tgpf_SpecialFundAppropriated_AFDtl.bankAccountEscrowedId;
				detailVue.theNameOfBankBranch = jsonObj.tgpf_SpecialFundAppropriated_AFDtl.theNameOfEscrowed;
				detailVue.canPayAmount = jsonObj.tgpf_SpecialFundAppropriated_AFDtl.accountBalance;
				detailVue.currentBalance = jsonObj.tgpf_SpecialFundAppropriated_AFDtl.applyRefundPayoutAmount;*/
				//refresh();
				var currenttime = gettime();
				detailVue.tgpf_SpecialFundAppropriated_AFDtlEdit.payoutDate= currenttime;
			}
		});
	}

	function gettime(){
		var date = new Date();
		var seperator1 = "-";
		var month = date.getMonth() + 1;
		var strDate = date.getDate();
		if (month >= 1 && month <= 9) {
			month = "0" + month;
		}
		if (strDate >= 0 && strDate <= 9) {
			strDate = "0" + strDate;
		}
		var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate;
		return currentdate;
	}


	function subEditSpecialFundAppropriated(){

		console.log("payoutChannel==============");

		var payoutChannel = document.getElementById("editPayoutChannel").value;
		console.log("payoutChannel="+payoutChannel);

		var model = {
				interfaceVersion:this.interfaceVersion,
				tableId : this.selectItem[0],
				payoutChannel: payoutChannel,
				payoutDate: this.tgpf_SpecialFundAppropriated_AFDtlEdit.payoutDate,
				billNumber: this.tgpf_SpecialFundAppropriated_AFDtlEdit.billNumber
		}
		new ServerInterface(baseInfo.subInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				$(baseInfo.editSpecialModel).modal('hide');
				refresh();
			}
		});
	}
	//新增操作--------------------选择特殊拨付划款信息表
	function listItemSelectHandle(list){
		if(list.length == 1){
			detailVue.editDisabled = false;
		}else{
			detailVue.editDisabled = true;
		}

		
		if(detailVue.isDel)
		{
			if(list.length !=0){
				detailVue.delDisabled = false;
			}else{
				detailVue.delDisabled = true;
			}
		}
		
		generalListItemSelectHandle(detailVue,list)
	}
	
	function initData()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var array = tableIdStr.split("_");
		if (array.length > 1)
		{
			detailVue.tableId = array[array.length-4];
			detailVue.afId = array[array.length-3];
			console.log(array.length)
			if(array[array.length-2]!=null)
			{
				detailVue.workflowId = array[array.length-2];
			}
//			if(array[array.length-1] == "2"){
//				detailVue.showInfo = false;
//			}
			approvalModalVue.sourcePage = array[array.length-1]
			approvalModalVue.workflowId = detailVue.workflowId;
			approvalModalVue.afId = detailVue.afId;
			approvalModalVue.busiCode = detailVue.af_busiCode;
			detailVue.sourcePage = approvalModalVue.sourcePage;
			refresh();
		}
	}
	var count = 0;
	//审批操作---------------点击确定按钮
	function subSpecialFundAppropriated(){
		
		var isNull = false;
		//判断是否选择托管账户
		
		if(detailVue.theAccountId==undefined||detailVue.theAccountId.length==0)
		{
			isNull = true;
			$(baseInfo.tipsSpecialFundDiv3).modal('show');
			
		}
		
		if(!isNull)
		{
			detailVue.theAccountId
			
			count ++;
			var accountOfEscrowed = "";
			detailVue.tgxy_BankAccountEscrowedList.forEach(function(item){
				if(item.tableId == detailVue.theAccountId){
					accountOfEscrowed = item.theAccount;
				}
			});
			
			accountOfEscrowed = detailVue.theAccount;
			
			var bankAccountEscrowed = {
					shortNameOfBank : "",
			}
			detailVue.trusteeBankList.forEach(function(item){
				if(item.tableId == detailVue.theBankId){
					bankAccountEscrowed.shortNameOfBank = item.theName;
				}
			});
			
			/*
			* xsz by time 2018-12-12 10:45:56
			* 同一个托管账号只可以同时新增一次;
			* 本次划款金额为0时，提示不可新增
			*/
			
			var isZero = true;
			if(detailVue.appliedAmount==undefined||detailVue.appliedAmount<=0)
			{
				isZero = false;
				$(baseInfo.tipsSpecialFundDiv2).modal('show');
			}
			
			if(isZero)
			{
				var isAdd = true;
				
				console.log("detailVue.tgpf_SpacialFundAppropriatedAddList="+detailVue.tgpf_SpacialFundAppropriatedAddList);
				if(detailVue.tgpf_SpacialFundAppropriatedAddList!=undefined&&detailVue.tgpf_SpacialFundAppropriatedAddList.length>0)
				{
					detailVue.tgpf_SpacialFundAppropriatedAddList.forEach(function(item){
						console.log("item.bankAccountEscrowedId="+item.bankAccountEscrowedId+"this.theAccountId="+detailVue.theAccountId);
						if(item.bankAccountEscrowedId==detailVue.theAccountId)
						{
							isAdd = false;
							$(baseInfo.tipsSpecialFundDiv).modal('show');
							
						}
					})
				}
				
				if(isAdd)
				{
					
					var model = {
							theCodeOfAf : this.tgpf_SpecialFundAppropriatedDetailModel.eCode,
							accountOfEscrowed : accountOfEscrowed,
							accountBalance : this.currentBalance,
							appliedAmount : addThousands(this.appliedAmount),
//							payoutChannel: this.payoutChannel,
							applyRefundPayoutAmount : this.currentBalance,
							payoutDate: this.payoutDate,
							billNumber: this.billNumber,
							bankAccountEscrowedId : this.theAccountId,
							tableId : count,
							bankBranchId : this.theBankId,
							bankAccountEscrowed : bankAccountEscrowed,
							
							
						}
						detailVue.tgpf_SpacialFundAppropriatedAddList.push(model);
						$(baseInfo.addSpecialModel).modal('hide');
				}
			}
		
		}
		detailVue.subDisabled = false;
	}
	//审批操作-----------------点击提交按钮
	function subAllSpecialFundModel(){
		var SpacialFundAppropriatedSubList = [];
		detailVue.tgpf_SpacialFundAppropriatedAddList.forEach(function(item){
			var model = {
				bankAccountEscrowedId : item.bankAccountEscrowedId,
				appliedAmount : commafyback(item.appliedAmount),
				payoutChannel: item.payoutChannel,
				payoutDate: item.payoutDate,
				billNumber: item.billNumber
			}
			SpacialFundAppropriatedSubList.push(model);
		})
		SpacialFundAppropriatedSubList = JSON.stringify(SpacialFundAppropriatedSubList);
		var model = {
			interfaceVersion:this.interfaceVersion,
			specialAppropriatedId : this.tableId,
			SpacialFundAppropriatedSubList : SpacialFundAppropriatedSubList,
		}
		//提交失败后，清除添加的划款信息
		new ServerInterface(baseInfo.subInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
				
				detailVue.tgpf_SpacialFundAppropriatedAddList = [];
				$(baseInfo.addSpecialModel).modal('hide');
				
				
			}
			else
			{
				$(baseInfo.addSpecialModel).modal('hide');
//				detailVue.showButton = false;
				detailVue.addDisabled = true;
				detailVue.delDisabled = true;
				detailVue.subDisabled = true;
				
//				detailVue.isDel = false;
				
				refresh();
			}
		});
	}
	
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#Sm_SpecialFundAppropriatedDiv",
	"addSpecialModel":"#addSpecialFundModel",
	"editSpecialModel" : "#editSpecialFundModel",
	"deleteSpecialDiv":"#deleteSpecialFundModal",
	"deleteAllSpecialDiv":"#deleteAllSpecialFundModal",
	"tipsSpecialFundDiv":"#tipsSpecialFundModal",
	"tipsSpecialFundDiv2":"#tipsSpecialFundModal2",
	"tipsSpecialFundDiv3":"#tipsSpecialFundModal3",
	"detailInterface":"../Sm_ApprovalProcess_SpecialFundAppropriatedDetail",
	// "escrowAccountInterface" : "../Tgxy_BankAccountEscrowedList",
	"escrowAccountInterface" : "../Tgxy_BankAccountEscrowedViewList",
	"subInterface" : "../Tgpf_SpecialFundAppropriated_AFDtlAdd",
	"escrowAccountDetailInterface" : "../Tgpf_SpecialFundAppropriated_AFDtlDetail",
	"deleteInterface" : "../Tgpf_SpecialFundAppropriated_AFDtlBatchDelete",
	"bankBranchInterface" : "../Emmp_BankBranchList",
});
