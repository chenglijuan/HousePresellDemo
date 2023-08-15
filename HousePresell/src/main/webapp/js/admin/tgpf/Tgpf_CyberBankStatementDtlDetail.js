(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tgpf_CyberBankStatementDtlModel: {},
			tableId : 1,
			Tgpf_CyberBankStatementDtlDetail : [],
			checkList : ["记账日期","贷款金额","对方账户","对方户名"],   
			flag : true,
			bankName : "",//银行名称
			billTimeStamp : "",//记账日期
			escrowedAccount : "",//托管账户
			escrowedAccountTheName : "",//托管账号名称
			errMsg : "",
			munDisabled : true,
			showFlag: true,
			tableHeight : null,
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			getConfirmFrom : getConfirmFrom,//获取对账完成确认参数
			manualReconciliation : manualReconciliation,//人工对账
			delAccount : delAccount,//删除
			confirmAccount : confirmAccount,//对账完成确认
			checkCheckBox : checkCheckBox,
			getCheckForm : getCheckForm,
			checkAccountHandle : checkAccountHandle,//选择对账类型
			getDelateForm : getDelateForm,
			getManualReconciliationForm :getManualReconciliationForm, 
			indexMethod : indexMethod,
			tableHeader :function tableHeader(row){
				if(row.rowIndex == "0" && row.columnIndex == "2"){
					return "background:#FAE5E6;textAlign:center;borderTop:1px solid #dfe6ec";
				}else if(row.rowIndex == "0" && row.columnIndex == "0" || row.rowIndex == "0" && row.columnIndex == "1" || row.rowIndex == "0" && row.columnIndex == "0" || row.rowIndex == "0" && row.columnIndex == "3"){
					return "background:white;textAlign:center;borderTop:1px solid #dfe6ec";
				}
			},
			tableCell : function(row){
				var legth = this.Tgpf_CyberBankStatementDtlDetail.length-1;
				if(row.rowIndex >=0 && row.rowIndex <legth && row.columnIndex == 8){
					return "background:#FAE5E6;textAlign:center;borderBottom:1px solid #dfe6ec";
				}else if(row.rowIndex == legth && row.columnIndex === 4){// 
					return "background:#FAE5E6;textAlign:center;borderRight:1px solid #dfe6ec";
				}else if(row.rowIndex == legth && row.columnIndex === 0 || row.rowIndex == legth && row.columnIndex === 2 || row.rowIndex == legth && row.columnIndex === 6){
					return "background:#FCF5DE;border:none;textAlign:center;";
				}else if(row.rowIndex == legth && row.columnIndex === 1 || row.rowIndex == legth && row.columnIndex === 5){
					return "background:#FCF5DE;textAlign:right;border:none"
				}else if(row.rowIndex == legth && row.columnIndex === 3 || row.rowIndex == legth && row.columnIndex === 7){
					return "background:#FCF5DE;textAlign:left;border:none"
				}
			},
			arraySpanMethod:function(row){
				var legth = this.Tgpf_CyberBankStatementDtlDetail.length-1
				if(legth == row.rowIndex){
					if(row.columnIndex === 0 || row.columnIndex === 1 || row.columnIndex === 4 ){
						return [1, 1];
					}else if(row.columnIndex === 2 || row.columnIndex === 5 || row.columnIndex === 3 || row.columnIndex === 6){
						return [1, 3];
					}else{
						return [0,0]
					}
				}
			},
			cellcb:function(row){
				var legth = this.Tgpf_CyberBankStatementDtlDetail.length-1
					 detailVue.flag  = false;	
				if(row.rowIndex == legth) {
					if(row.columnIndex == 0){
						return "myCell";
					}
					if(row.columnIndex == 1){
						return "noIndex";
					}
				}
	        }
		},
		computed:{
			 
		},
		components : {

		},
		watch:{
			flag:function(oldVal,newVal){
				detailVue.flag = detailVue.flag ;
			}
		}
	});

	//------------------------方法定义-开始------------------//
	
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:this.tableId
		}
	}

	//详情操作--------------
	function refresh()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		tableIdStr = tableIdStr.split("_");
		detailVue.tableId = tableIdStr[2];
//		detailVue.tableId = tableIdStr;
		if (detailVue.tableId == null || detailVue.tableId < 1) 
		{
			return;
		}

		getDetail();
	}
	//网银对账人工对账-----------------获取确认对账参数
	function getConfirmFrom(){
		return{
			interfaceVersion:this.interfaceVersion,
			tableId : this.tableId
		}
	}
	
	function indexMethod(index) {
		return generalIndexMethod(index, detailVue)
	}
	
	function getDetail()
	{
	//	alert("detail")
	    detailVue.showFlag = true;
		new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				detailVue.errMsg = jsonObj.info;
			}
			else
			{
				detailVue.bankName = jsonObj.bankName;
				detailVue.billTimeStamp = jsonObj.billTimeStamp;
				detailVue.escrowedAccount = jsonObj.escrowedAccount;
				detailVue.escrowedAccountTheName = jsonObj.escrowedAccountTheName;
				detailVue.Tgpf_CyberBankStatementDtlDetail = jsonObj.tgpf_CyberBankStatementDtlList;
				if(detailVue.Tgpf_CyberBankStatementDtlDetail.length >=7){
					detailVue.tableHeight = 640;
				}else{
					detailVue.tableHeight = null;
				}
				detailVue.Tgpf_CyberBankStatementDtlDetail.forEach(function(item){
					item.income = addThousands(item.income);
					item.busIncome = addThousands(item.busIncome);
					if(item.reconciliationState != 1) {
						detailVue.showFlag = false;
					}
				})
				console.log(jsonObj.recState);
				if(jsonObj.recState == "1") {
					    console.log("2");
					    detailVue.showFlag = false;
				}
				if(detailVue.Tgpf_CyberBankStatementDtlDetail.length >0){
					jsonObj.cyberBankTotalAmount = addThousands(jsonObj.cyberBankTotalAmount);
					jsonObj.centerTotalAmount = addThousands(jsonObj.centerTotalAmount);
					var totalModal = {
						tradeTimeStamp : '总笔数：'+ jsonObj.cyberBankTotalCount,
						recipientAccount : '总金额：'+ jsonObj.cyberBankTotalAmount,
						income : '总笔数：'+ jsonObj.centerTotalCount,
						remark : '总金额：'+ jsonObj.centerTotalAmount,
					}
					detailVue.Tgpf_CyberBankStatementDtlDetail.push(totalModal);
				}
				console.log(detailVue.showFlag);
			}
		});
	}
	
	//对账操作-------------------选中对账类型且进行自动对账
	function checkAccountHandle(value){
		this.checkList = value;
		new ServerInterface(baseInfo.autoAccountInterface).execute(detailVue.getCheckForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				detailVue.errMsg = jsonObj.info;
			}
			else
			{
				refresh();
			}
		});
	}
	
	
	
	//列表操作------------选中复选框
	function checkCheckBox(tableId){
		if(tableId.length !=0){
			detailVue.munDisabled = false;
		}else{
			detailVue.munDisabled = true;
		}
		detailVue.selectItem  = tableId;
	}
	//队长操作----------------获取选中的参数
	function getCheckForm(){
		var checkModal = {
			tableId:detailVue.tableId,
			tradeTimeStamp : "",
			recipientAccount : "",
			recipientName : "",
			tradeAmount : "",
			remark : "",
		};
		if(this.checkList.length != 0){
			this.checkList.forEach(function(item){
				if(item == "记账日期"){
					checkModal.tradeTimeStamp = "记账日期";
				}else if(item == "对方账户"){
					checkModal.recipientAccount = "对方账户";
				}else if(item == "对方户名"){
					checkModal.recipientName = "对方户名";
				}else if(item == "贷款金额"){
					checkModal.tradeAmount = "贷款金额";
				}else if(item == "摘要"){
					checkModal.remark = "摘要";
				}
			})
		}
		checkModal = JSON.stringify(checkModal);
		return{
			interfaceVersion:this.interfaceVersion,
			tgpf_CyberBankStatementDtlContrastDetailList : checkModal,
		}
	}
	//列表操作------------------------获取人工对账传递参数
	function getManualReconciliationForm(){
		var selectArr = [];
		detailVue.selectItem.forEach(function(item){
			var selectModal = {
				tableId : item.tableId,
				cyBankTripleAgreementNum : item.cyBankTripleAgreementNum
			};
			selectArr.push(selectModal);
		});
		var modal = {
				tgpf_CyberBankStatementDtl :selectArr
		}
		modal = JSON.stringify(modal);
		return {
			interfaceVersion:this.interfaceVersion,
			tgpf_CyberBankStatementDtlContrastDetailList : modal,
		}
	}
	
	
	
	//对账操作-------------删除参数
	function getDelateForm(){
		this.checkArr = [];
		detailVue.selectItem.forEach(function(item){
			detailVue.checkArr.push(item.tableId);
		});
		return{
			interfaceVersion:this.interfaceVersion,
			idArr : this.checkArr,
		}
	}
	
	//列表操作------------人工对账
	function manualReconciliation(){
		
		new ServerInterface(baseInfo.manualReconciliationInterface).execute(detailVue.getManualReconciliationForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				detailVue.errMsg = jsonObj.info;
			}
			else
			{
				refresh();
			}
		});
	}
	//对账操作=--------------删除
	function delAccount(){
		new ServerInterface(baseInfo.delInterface).execute(detailVue.getDelateForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				detailVue.errMsg = jsonObj.info;
			}
			else
			{
				$(baseInfo.successDivId).modal({
					backdrop:"static"
				})
				refresh();
			}
		});
	}
	
	//网银对账人工对账-----------------确认对账
	function  confirmAccount(){
		new ServerInterface(baseInfo.confirmAccountInterface).execute(detailVue.getConfirmFrom(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				detailVue.errMsg = jsonObj.info;
			}
			else
			{
				$(baseInfo.successDivId).modal({
					backdrop :'static'
				});
				refresh();
			}
		});
	}
	function initData()
	{
		
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tgpf_CyberBankStatementDtlDiv",
	"edModelDivId":"#edModelCyberBankStatementDtlDetail",
	"successDivId" : "#sdModelCyberBankStatementDtlDetail",
	"detailInterface":"../tgpf_CyberBankStatementDtlContrastDetailList",
	"confirmAccountInterface":"../Tgpf_CyberBankStatementDtlConfirm",
	"manualReconciliationInterface":"../tgpf_CyberBankStatementDtlCompare",
	"delInterface":"../tgpf_CyberBankStatementDtlContrastDelete",
	"autoAccountInterface":"../tgpf_CyberBankStatementDtlAutoCompare",//自动对账
});
