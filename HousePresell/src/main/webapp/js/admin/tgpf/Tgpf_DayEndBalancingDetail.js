(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tgpf_DayEndBalancingModel: {},
			tableId : 1,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			selectItem : [],
			isAllSelected : false,
			Tgpf_DayEndBalancingDetail:[],
			checkArr : [],
			showChecKBox : true,
			errMsg : "",
			flag : true,
			manualDisabled : true,
			delDisabled : true,
			showFlag: true,
			tableHeight : null
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			checkCheckBox : checkCheckBox,
			getCheckForm : getCheckForm,
			getConfirmFrom : getConfirmFrom,//获取对账完成确认参数
			manualReconciliation : manualReconciliation,//人工对账
			delAccount : delAccount,//删除
			confirmAccount : confirmAccount,//对账完成确认
			showDelModel : showDelModel,
			changePageNumber : function(data){
				//detailVue.pageNumber = data;
				if (detailVue.pageNumber != data) {
					detailVue.pageNumber = data;
					detailVue.refresh();
				}
			},
			changeCountPerPage:function(data){
				if (detailVue.countPerPage != data) {
					detailVue.countPerPage = data;
					detailVue.refresh();
				}
			},
			tableHeader : function(row){
				if(row.rowIndex == "0" && row.columnIndex == "2"){
					return "background:#FAE5E6;textAlign:center;borderTop:1px solid #dfe6ec";
				}else if(row.rowIndex == "0" && row.columnIndex == "0" || row.rowIndex == "0" && row.columnIndex == "1" || row.rowIndex == "0" && row.columnIndex == "0" || row.rowIndex == "0" && row.columnIndex == "3"){
					return "background:white;textAlign:center;borderTop:1px solid #dfe6ec";
				}
			},
			tableCell : function(row){
				var legth = this.Tgpf_DayEndBalancingDetail.length-1;
				if(row.rowIndex >=0 && row.rowIndex <legth && row.columnIndex == 8){
					return "background:#FAE5E6;textAlign:center;borderBottom:1px solid #dfe6ec";
				}else if(row.rowIndex == legth && row.columnIndex === 4){// 
					return "background:#FAE5E6;textAlign:center";
				}else if(row.rowIndex == legth && row.columnIndex === 0  || row.rowIndex == legth && row.columnIndex === 6 || row.rowIndex == legth && row.columnIndex === 1 ){
					return "background:#FCF5DE;border:none;";
				}else if(row.rowIndex == legth && row.columnIndex === 5){
					return "background:#FCF5DE;textAlign:right;border:none"
				}else if(row.rowIndex == legth && row.columnIndex === 3 || row.rowIndex == legth && row.columnIndex === 7){
					return "background:#FCF5DE;textAlign:left"
				}else if(row.rowIndex == legth && row.columnIndex === 2){
					return "background:#FCF5DE;border:none;textAlign:center";
				}
			},
			arraySpanMethod:function(row){
				var legth = this.Tgpf_DayEndBalancingDetail.length-1
				if(legth == row.rowIndex){
					if(row.columnIndex === 0 ||row.columnIndex === 1 || row.columnIndex === 4 ){
						return [1, 1];
					}else if(row.columnIndex === 2 || row.columnIndex === 5 || row.columnIndex === 3 || row.columnIndex === 6){
						return [1, 3];
					}else{
						return [0,0]
					}
				}
			},
			cellcb:function(row){
				var legth = this.Tgpf_DayEndBalancingDetail.length-1
					 detailVue.flag  = false;	
				if(row.rowIndex == legth) {
					if(row.columnIndex == 0){
						return "myCell";
					}
					if(row.columnIndex == 1){
						return "noIndex";
					}
					
				}
	        },

			checked:function(){
                //首先el-table添加ref="table"引用标识
                this.$refs.tableRef.toggleRowSelection(this.Tgpf_DayEndBalancingDetail[2],true);
                if(this.$refs.tableRef.toggleRowSelection(this.Tgpf_DayEndBalancingDetail[2])){
                	showChecKBox = false;
                }
                //alert(showChecKBox);
		    },
		    tableRowClassName:function(row, index){
//		    	alert(index);
		    }
		},
		computed:{
			 
		},
		components : {

		},
		mounted:function(){
//	        this.checked();//每次更新了数据，触发这个函数即可。
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
			tableId:this.tableId,
		}
	}

	//详情操作--------------
	function refresh()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		tableIdStr = tableIdStr.split("_");
		detailVue.tableId = tableIdStr[2];
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
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});
				$(baseInfo.errDivId).modal({
					backdrop:"static"
				})
				detailVue.errMsg = jsonObj.info;
			}
			else
			{
				//billTimeStamp
				
				detailVue.tgpf_DayEndBalancingModel.bankName = jsonObj.bankName;
				detailVue.tgpf_DayEndBalancingModel.theAccount = jsonObj.theAccount;
				detailVue.tgpf_DayEndBalancingModel.theName = jsonObj.theName;
				detailVue.tgpf_DayEndBalancingModel.billTimeStamp = jsonObj.billTimeStamp;
				
				detailVue.Tgpf_DayEndBalancingDetail = jsonObj.tgpf_DepositDetailList;
				if(detailVue.Tgpf_DayEndBalancingDetail.length >=7){
					detailVue.tableHeight = 640;
				}else{
					detailVue.tableHeight = null;
				}
				if(detailVue.Tgpf_DayEndBalancingDetail.length >0){
					detailVue.Tgpf_DayEndBalancingDetail.forEach(function(item){
						item.loanAmountFromBank = addThousands(item.loanAmountFromBank);
						item.bankAmount = addThousands(item.bankAmount);
						if(item.reconciliationState != 1) {
							detailVue.showFlag = false;
						}
					})
					if(jsonObj.recState == 1) {
					    detailVue.showFlag = false;
				    }
					
					jsonObj.centerTotalAmount = addThousands(jsonObj.centerTotalAmount);
					//jsonObj.centerTotalAmount = addThousands(jsonObj.centerTotalAmount);
					
					var totalModal = {
						tripleAgreementNum : '总笔数：'+ jsonObj.centerTotalCount,
						billTimeStamp : '总金额：'+ jsonObj.centerTotalAmount,
						bankAccountForLoan : '总笔数：'+ jsonObj.bankTotalCount,
						loanAmountFromBank : '总金额：'+ jsonObj.bankTotalAmount,
					}
					detailVue.Tgpf_DayEndBalancingDetail.push(totalModal);
				}
			}
		});
		//var tableTr = $('#mytable').find('table:last').find("tr")[0];
		//var er = tableTr.length;
	//	alert(er)
		//console.log(tableTr)
	}
	
	//队长操作----------------获取选中的参数
	function getCheckForm(){
		this.checkArr = [];
		detailVue.selectItem.forEach(function(item){
			detailVue.checkArr.push(item.tableId);
		});
		return{
			interfaceVersion:this.interfaceVersion,
			idArr : this.checkArr,
		}
	}
	
	function getConfirmFrom(){
		return{
			interfaceVersion:this.interfaceVersion,
			tableId : this.tableId,
		}
	}
	
	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		detailVue.isAllSelected = (detailVue.Tgpf_DayEndBalancingDetail.length > 0)
		&&	(detailVue.selectItem.length == detailVue.Tgpf_DayEndBalancingDetail.length)
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(detailVue.selectItem.length == detailVue.Tgpf_DayEndBalancingDetail.length)
	    {
	    	detailVue.selectItem = [];
	    }
	    else
	    {
	    	detailVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	detailVue.Tgpf_DayEndBalancingDetail.forEach(function(item) {
	    		detailVue.selectItem.push(item.tableId);
	    	});
	    }
	}
	//列表操作------------选中复选框
	function checkCheckBox(tableId){
		if(tableId.length >0){
			detailVue.manualDisabled = false;
			detailVue.delDisabled = false;
		}else{
			detailVue.manualDisabled = true;
			detailVue.delDisabled = true;
		}
		
		if(tableId.length >= 1){
			for(var i = 0;i<tableId.length;i++){
				if(tableId[i].reconciliationStateFromBusiness == "1"){
					detailVue.delDisabled = true;
					return;
				}else{
					detailVue.delDisabled = false;
				}
			}
		}else{
			detailVue.delDisabled = true;
		}
		detailVue.selectItem  = tableId;
	}
	//列表操作------------人工对账
	function manualReconciliation(){
		new ServerInterface(baseInfo.manualReconciliationInterface).execute(detailVue.getCheckForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});
				$(baseInfo.errDivId).modal({
					backdrop:"static"
				})
				detailVue.errMsg = jsonObj.info;
			}
			else
			{
				refresh();
			}
		});
	}
	function showDelModel(){
		$(baseInfo.delDivId).modal({
			backdrop:"static"
		})
	}
	
	//对账操作=--------------删除
	function delAccount(){
		new ServerInterface(baseInfo.delInterface).execute(detailVue.getCheckForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});
				$(baseInfo.errDivId).modal({
					backdrop:"static"
				})
				detailVue.errMsg = jsonObj.info;
			}
			else
			{
				$(baseInfo.delDivId).modal("hide");
				refresh();
			}
		});
	}
	function  confirmAccount(){
		new ServerInterface(baseInfo.confirmAccountInterface).execute(detailVue.getConfirmFrom(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});
				$(baseInfo.errDivId).modal({
					backdrop:"static"
				})
				detailVue.errMsg = jsonObj.info;
			}
			else
			{
				$(baseInfo.successDivId).modal({
					backdrop:"static"
				})
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
	"detailDivId":"#tgpf_DayEndBalancingDiv",
	"errDivId":"#edModelDayEndBalancingDetail",
	"successDivId" : "#sdModelDayEndBalancingDetail",
	"delDivId" : "#deleteDayEndBalancingDetail",
	"detailInterface":"../tgpf_DepositDetailContrastList",
	"manualReconciliationInterface":"../Tgpf_BalanceOfAccountCompare",//人工对账
	"delInterface":"../tgpf_DepositDetailContrastDelete",//删除
	"confirmAccountInterface" : "../Tgpf_BalanceOfAccountConfirm",//对账完成确认
});
