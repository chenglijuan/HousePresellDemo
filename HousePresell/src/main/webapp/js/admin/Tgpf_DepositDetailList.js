(function(baseInfo){
	var listVue = new Vue({
		el : baseInfo.listDivId,
		data : {
			interfaceVersion :19000101,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			keyword : "",
			selectItem : [],
			isAllSelected : false,
			theState:0,//正常为0，删除为1
			userStartList:[],
			userRecordList:[],
			bankAccountEscrowedList:[],
			bankBranchList:[],
			theNameOfBankBranchFromDepositBillList:[],
			tripleAgreementList:[],
			dayEndBalancingList:[],
			tgpf_DepositDetailList:[]
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			listItemSelectHandle: listItemSelectHandle,
			tgpf_DepositDetailDelOne : tgpf_DepositDetailDelOne,
			tgpf_DepositDetailDel : tgpf_DepositDetailDel,
			showAjaxModal:showAjaxModal,
			search:search,
			checkAllClicked : checkAllClicked,
			changePageNumber : function(data){
				listVue.pageNumber = data;
			},
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue
		},
		watch:{
//			pageNumber : refresh,
			selectItem : selectedItemChanged,
		}
	});
	var updateVue = new Vue({
		el : baseInfo.updateDivId,
		data : {
			interfaceVersion :19000101,
			theState:null,
			userStartList:[],
			createTimeStamp:null,
			lastUpdateTimeStamp:null,
			userRecordList:[],
			recordTimeStamp:null,
			eCodeFromPayment:null,
			fundProperty:null,
			bankAccountEscrowedList:[],
			bankBranchList:[],
			theNameOfBankAccountEscrowed:null,
			theAccountOfBankAccountEscrowed:null,
			theNameOfCreditor:null,
			idType:null,
			idNumber:null,
			bankAccountForLoan:null,
			loanAmountFromBank:null,
			billTimeStamp:null,
			eCodeFromBankCore:null,
			eCodeFromBankPlatform:null,
			remarkFromDepositBill:null,
			theNameOfBankBranchFromDepositBillList:[],
			eCodeFromBankWorker:null,
			tripleAgreementList:[],
			depositState:null,
			dayEndBalancingList:[],
			depositDatetime:null,
			reconciliationTimeStampFromBusiness:null,
			reconciliationStateFromBusiness:null,
			reconciliationTimeStampFromCyberBank:null,
			reconciliationStateFromCyberBank:null,
			hasVoucher:null,
			timestampFromReverse:null,
			theStateFromReverse:null,
			eCodeFromReverse:null,
			tripleAgreementNumber:null,
			contractFilingNumber:null,
			purchaser:null,
			nameOfDevelopment:null,
			projectName:null,
			houseLocated:null,
			loanAmount:null,
			totalAmountManaged:null,
			capitalCollectionNumber:null,
			statusOfContributions:null
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateTgpf_DepositDetail
		}
	});
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			theState:null,
			userStartList:[],
			createTimeStamp:null,
			lastUpdateTimeStamp:null,
			userRecordList:[],
			recordTimeStamp:null,
			eCodeFromPayment:null,
			fundProperty:null,
			bankAccountEscrowedList:[],
			bankBranchList:[],
			theNameOfBankAccountEscrowed:null,
			theAccountOfBankAccountEscrowed:null,
			theNameOfCreditor:null,
			idType:null,
			idNumber:null,
			bankAccountForLoan:null,
			loanAmountFromBank:null,
			billTimeStamp:null,
			eCodeFromBankCore:null,
			eCodeFromBankPlatform:null,
			remarkFromDepositBill:null,
			theNameOfBankBranchFromDepositBillList:[],
			eCodeFromBankWorker:null,
			tripleAgreementList:[],
			depositState:null,
			dayEndBalancingList:[],
			depositDatetime:null,
			reconciliationTimeStampFromBusiness:null,
			reconciliationStateFromBusiness:null,
			reconciliationTimeStampFromCyberBank:null,
			reconciliationStateFromCyberBank:null,
			hasVoucher:null,
			timestampFromReverse:null,
			theStateFromReverse:null,
			eCodeFromReverse:null,
			tripleAgreementNumber:null,
			contractFilingNumber:null,
			purchaser:null,
			nameOfDevelopment:null,
			projectName:null,
			houseLocated:null,
			loanAmount:null,
			totalAmountManaged:null,
			capitalCollectionNumber:null,
			statusOfContributions:null
		},
		methods : {
			getAddForm : getAddForm,
			add : addTgpf_DepositDetail
		}
	});

	//------------------------方法定义-开始------------------//
	 
	//列表操作--------------获取"搜索列表"表单参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			pageNumber:this.pageNumber,
			countPerPage:this.countPerPage,
			totalPage:this.totalPage,
			keyword:this.keyword,
			theState:this.theState,
		}
	}
	//列表操作--------------获取“删除资源”表单参数
	function getDeleteForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			idArr:this.selectItem
		}
	}
	//列表操作--------------获取“新增”表单参数
	function getAddForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			createTimeStamp:this.createTimeStamp,
			lastUpdateTimeStamp:this.lastUpdateTimeStamp,
			recordTimeStamp:this.recordTimeStamp,
			eCodeFromPayment:this.eCodeFromPayment,
			fundProperty:this.fundProperty,
			theNameOfBankAccountEscrowed:this.theNameOfBankAccountEscrowed,
			theAccountOfBankAccountEscrowed:this.theAccountOfBankAccountEscrowed,
			theNameOfCreditor:this.theNameOfCreditor,
			idType:this.idType,
			idNumber:this.idNumber,
			bankAccountForLoan:this.bankAccountForLoan,
			loanAmountFromBank:this.loanAmountFromBank,
			billTimeStamp:this.billTimeStamp,
			eCodeFromBankCore:this.eCodeFromBankCore,
			eCodeFromBankPlatform:this.eCodeFromBankPlatform,
			remarkFromDepositBill:this.remarkFromDepositBill,
			eCodeFromBankWorker:this.eCodeFromBankWorker,
			depositState:this.depositState,
			depositDatetime:this.depositDatetime,
			reconciliationTimeStampFromBusiness:this.reconciliationTimeStampFromBusiness,
			reconciliationStateFromBusiness:this.reconciliationStateFromBusiness,
			reconciliationTimeStampFromCyberBank:this.reconciliationTimeStampFromCyberBank,
			reconciliationStateFromCyberBank:this.reconciliationStateFromCyberBank,
			hasVoucher:this.hasVoucher,
			timestampFromReverse:this.timestampFromReverse,
			theStateFromReverse:this.theStateFromReverse,
			eCodeFromReverse:this.eCodeFromReverse,
			tripleAgreementNumber:this.tripleAgreementNumber,
			contractFilingNumber:this.contractFilingNumber,
			purchaser:this.purchaser,
			nameOfDevelopment:this.nameOfDevelopment,
			projectName:this.projectName,
			houseLocated:this.houseLocated,
			loanAmount:this.loanAmount,
			totalAmountManaged:this.totalAmountManaged,
			capitalCollectionNumber:this.capitalCollectionNumber,
			statusOfContributions:this.statusOfContributions
		}
	}
	//列表操作--------------获取“更新”表单参数
	function getUpdateForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			theState:this.theState,
			createTimeStamp:this.createTimeStamp,
			lastUpdateTimeStamp:this.lastUpdateTimeStamp,
			recordTimeStamp:this.recordTimeStamp,
			eCodeFromPayment:this.eCodeFromPayment,
			fundProperty:this.fundProperty,
			theNameOfBankAccountEscrowed:this.theNameOfBankAccountEscrowed,
			theAccountOfBankAccountEscrowed:this.theAccountOfBankAccountEscrowed,
			theNameOfCreditor:this.theNameOfCreditor,
			idType:this.idType,
			idNumber:this.idNumber,
			bankAccountForLoan:this.bankAccountForLoan,
			loanAmountFromBank:this.loanAmountFromBank,
			billTimeStamp:this.billTimeStamp,
			eCodeFromBankCore:this.eCodeFromBankCore,
			eCodeFromBankPlatform:this.eCodeFromBankPlatform,
			remarkFromDepositBill:this.remarkFromDepositBill,
			eCodeFromBankWorker:this.eCodeFromBankWorker,
			depositState:this.depositState,
			depositDatetime:this.depositDatetime,
			reconciliationTimeStampFromBusiness:this.reconciliationTimeStampFromBusiness,
			reconciliationStateFromBusiness:this.reconciliationStateFromBusiness,
			reconciliationTimeStampFromCyberBank:this.reconciliationTimeStampFromCyberBank,
			reconciliationStateFromCyberBank:this.reconciliationStateFromCyberBank,
			hasVoucher:this.hasVoucher,
			timestampFromReverse:this.timestampFromReverse,
			theStateFromReverse:this.theStateFromReverse,
			eCodeFromReverse:this.eCodeFromReverse,
			tripleAgreementNumber:this.tripleAgreementNumber,
			contractFilingNumber:this.contractFilingNumber,
			purchaser:this.purchaser,
			nameOfDevelopment:this.nameOfDevelopment,
			projectName:this.projectName,
			houseLocated:this.houseLocated,
			loanAmount:this.loanAmount,
			totalAmountManaged:this.totalAmountManaged,
			capitalCollectionNumber:this.capitalCollectionNumber,
			statusOfContributions:this.statusOfContributions
		}
	}
	function tgpf_DepositDetailDel()
	{
		noty({
			layout:'center',
			modal:true,
			text:"确认批量删除吗？",
			type:"confirm",
			buttons:[
				{
					addClass:"btn btn-primary",
					text:"确定",
					onClick:function($noty){
						$noty.close();
						new ServerInterface(baseInfo.deleteInterface).execute(listVue.getDeleteForm(), function(jsonObj){
							if(jsonObj.result != "success")
							{
								noty({"text":jsonObj.info,"type":"error","timeout":2000});
							}
							else
							{
								listVue.selectItem = [];
								refresh();
							}
						});
					}
				},
				{
					addClass:"btn btn-danger",
					text:"取消",
					onClick:function($noty){
						
						$noty.close();
					}
				}
			]
			
		});
	}
	function tgpf_DepositDetailDelOne(tgpf_DepositDetailId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[tgpf_DepositDetailId],
		};
		
		noty({
			layout:'center',
			modal:true,
			text:"确认删除吗？",
			type:"confirm",
			buttons:[
				{
					addClass:"btn btn-primary",
					text:"确定",
					onClick:function($noty){
						$noty.close();
						new ServerInterface(baseInfo.deleteInterface).execute(model , function(jsonObj){
							if(jsonObj.result != "success")
							{
								noty({"text":jsonObj.info,"type":"error","timeout":2000});
							}
							else
							{
								refresh();
							}
						});
					}
				},
				{
					addClass:"btn btn-danger",
					text:"取消",
					onClick:function($noty){
						
						$noty.close();
					}
				}
			]
			
		});
	}
	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		listVue.isAllSelected = (listVue.tgpf_DepositDetailList.length > 0)
		&&	(listVue.selectItem.length == listVue.tgpf_DepositDetailList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgpf_DepositDetailList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgpf_DepositDetailList.forEach(function(item) {
	    		listVue.selectItem.push(item.tableId);
	    	});
	    }
	}
	//列表操作--------------刷新
	function refresh()
	{
		new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				listVue.tgpf_DepositDetailList=jsonObj.tgpf_DepositDetailList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tgpf_DepositDetailListDiv').scrollIntoView();
			}
		});
	}
	
	//列表操作------------搜索
	function search()
	{
		listVue.pageNumber = 1;
		refresh();
	}
	
	//弹出编辑模态框--更新操作
	function showAjaxModal(tgpf_DepositDetailModel)
	{
		//tgpf_DepositDetailModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'tgpf_DepositDetail', tgpf_DepositDetailModel);
		//updateVue.$set("tgpf_DepositDetail", tgpf_DepositDetailModel);
		
		updateVue.theState = tgpf_DepositDetailModel.theState;
		updateVue.createTimeStamp = tgpf_DepositDetailModel.createTimeStamp;
		updateVue.lastUpdateTimeStamp = tgpf_DepositDetailModel.lastUpdateTimeStamp;
		updateVue.recordTimeStamp = tgpf_DepositDetailModel.recordTimeStamp;
		updateVue.eCodeFromPayment = tgpf_DepositDetailModel.eCodeFromPayment;
		updateVue.fundProperty = tgpf_DepositDetailModel.fundProperty;
		updateVue.theNameOfBankAccountEscrowed = tgpf_DepositDetailModel.theNameOfBankAccountEscrowed;
		updateVue.theAccountOfBankAccountEscrowed = tgpf_DepositDetailModel.theAccountOfBankAccountEscrowed;
		updateVue.theNameOfCreditor = tgpf_DepositDetailModel.theNameOfCreditor;
		updateVue.idType = tgpf_DepositDetailModel.idType;
		updateVue.idNumber = tgpf_DepositDetailModel.idNumber;
		updateVue.bankAccountForLoan = tgpf_DepositDetailModel.bankAccountForLoan;
		updateVue.loanAmountFromBank = tgpf_DepositDetailModel.loanAmountFromBank;
		updateVue.billTimeStamp = tgpf_DepositDetailModel.billTimeStamp;
		updateVue.eCodeFromBankCore = tgpf_DepositDetailModel.eCodeFromBankCore;
		updateVue.eCodeFromBankPlatform = tgpf_DepositDetailModel.eCodeFromBankPlatform;
		updateVue.remarkFromDepositBill = tgpf_DepositDetailModel.remarkFromDepositBill;
		updateVue.eCodeFromBankWorker = tgpf_DepositDetailModel.eCodeFromBankWorker;
		updateVue.depositState = tgpf_DepositDetailModel.depositState;
		updateVue.depositDatetime = tgpf_DepositDetailModel.depositDatetime;
		updateVue.reconciliationTimeStampFromBusiness = tgpf_DepositDetailModel.reconciliationTimeStampFromBusiness;
		updateVue.reconciliationStateFromBusiness = tgpf_DepositDetailModel.reconciliationStateFromBusiness;
		updateVue.reconciliationTimeStampFromCyberBank = tgpf_DepositDetailModel.reconciliationTimeStampFromCyberBank;
		updateVue.reconciliationStateFromCyberBank = tgpf_DepositDetailModel.reconciliationStateFromCyberBank;
		updateVue.hasVoucher = tgpf_DepositDetailModel.hasVoucher;
		updateVue.timestampFromReverse = tgpf_DepositDetailModel.timestampFromReverse;
		updateVue.theStateFromReverse = tgpf_DepositDetailModel.theStateFromReverse;
		updateVue.eCodeFromReverse = tgpf_DepositDetailModel.eCodeFromReverse;
		updateVue.tripleAgreementNumber = tgpf_DepositDetailModel.tripleAgreementNumber;
		updateVue.contractFilingNumber = tgpf_DepositDetailModel.contractFilingNumber;
		updateVue.purchaser = tgpf_DepositDetailModel.purchaser;
		updateVue.nameOfDevelopment = tgpf_DepositDetailModel.nameOfDevelopment;
		updateVue.projectName = tgpf_DepositDetailModel.projectName;
		updateVue.houseLocated = tgpf_DepositDetailModel.houseLocated;
		updateVue.loanAmount = tgpf_DepositDetailModel.loanAmount;
		updateVue.totalAmountManaged = tgpf_DepositDetailModel.totalAmountManaged;
		updateVue.capitalCollectionNumber = tgpf_DepositDetailModel.capitalCollectionNumber;
		updateVue.statusOfContributions = tgpf_DepositDetailModel.statusOfContributions
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateTgpf_DepositDetail()
	{
		new ServerInterface(baseInfo.updateInterface).execute(updateVue.getUpdateForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				$(baseInfo.updateDivId).modal('hide');
				refresh();
			}
		});
	}
	function addTgpf_DepositDetail()
	{
		new ServerInterface(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				$(baseInfo.addDivId).modal('hide');
				addVue.createTimeStamp = null;
				addVue.lastUpdateTimeStamp = null;
				addVue.recordTimeStamp = null;
				addVue.eCodeFromPayment = null;
				addVue.fundProperty = null;
				addVue.theNameOfBankAccountEscrowed = null;
				addVue.theAccountOfBankAccountEscrowed = null;
				addVue.theNameOfCreditor = null;
				addVue.idType = null;
				addVue.idNumber = null;
				addVue.bankAccountForLoan = null;
				addVue.loanAmountFromBank = null;
				addVue.billTimeStamp = null;
				addVue.eCodeFromBankCore = null;
				addVue.eCodeFromBankPlatform = null;
				addVue.remarkFromDepositBill = null;
				addVue.eCodeFromBankWorker = null;
				addVue.depositState = null;
				addVue.depositDatetime = null;
				addVue.reconciliationTimeStampFromBusiness = null;
				addVue.reconciliationStateFromBusiness = null;
				addVue.reconciliationTimeStampFromCyberBank = null;
				addVue.reconciliationStateFromCyberBank = null;
				addVue.hasVoucher = null;
				addVue.timestampFromReverse = null;
				addVue.theStateFromReverse = null;
				addVue.eCodeFromReverse = null;
				addVue.tripleAgreementNumber = null;
				addVue.contractFilingNumber = null;
				addVue.purchaser = null;
				addVue.nameOfDevelopment = null;
				addVue.projectName = null;
				addVue.houseLocated = null;
				addVue.loanAmount = null;
				addVue.totalAmountManaged = null;
				addVue.capitalCollectionNumber = null;
				addVue.statusOfContributions = null
				refresh();
			}
		});
	}
	
	function listItemSelectHandle(list) {
		generalListItemSelectHandle(listVue,list)
 	}

	function initData()
	{
		
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tgpf_DepositDetailListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Tgpf_DepositDetailList",
	"addInterface":"../Tgpf_DepositDetailAdd",
	"deleteInterface":"../Tgpf_DepositDetailDelete",
	"updateInterface":"../Tgpf_DepositDetailUpdate"
});
