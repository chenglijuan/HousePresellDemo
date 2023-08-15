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
				userStartId:null,
			userStartList:[],
			userUpdateId:null,
			userUpdateList:[],
			userRecordId:null,
			userRecordList:[],
			bankBranchId:null,
			bankBranchList:[],
			tgpf_BalanceOfAccountList:[]
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			listItemSelectHandle: listItemSelectHandle,
			tgpf_BalanceOfAccountDelOne : tgpf_BalanceOfAccountDelOne,
			tgpf_BalanceOfAccountDel : tgpf_BalanceOfAccountDel,
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
			busiState:null,
			eCode:null,
			userStartId:null,
			userStartList:[],
			createTimeStamp:null,
			userUpdateId:null,
			userUpdateList:[],
			lastUpdateTimeStamp:null,
			userRecordId:null,
			userRecordList:[],
			recordTimeStamp:null,
			billTimeStamp:null,
			bankName:null,
			escrowedAccount:null,
			escrowedAccountTheName:null,
			centerTotalCount:null,
			centerTotalAmount:null,
			bankTotalCount:null,
			bankTotalAmount:null,
			cyberBankTotalCount:null,
			cyberBankTotalAmount:null,
			accountType:null,
			reconciliationDate:null,
			reconciliationState:null,
			bankBranchId:null,
			bankBranchList:[],
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateTgpf_BalanceOfAccount
		}
	});
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			theState:null,
			busiState:null,
			eCode:null,
			userStartId:null,
			userStartList:[],
			createTimeStamp:null,
			userUpdateId:null,
			userUpdateList:[],
			lastUpdateTimeStamp:null,
			userRecordId:null,
			userRecordList:[],
			recordTimeStamp:null,
			billTimeStamp:null,
			bankName:null,
			escrowedAccount:null,
			escrowedAccountTheName:null,
			centerTotalCount:null,
			centerTotalAmount:null,
			bankTotalCount:null,
			bankTotalAmount:null,
			cyberBankTotalCount:null,
			cyberBankTotalAmount:null,
			accountType:null,
			reconciliationDate:null,
			reconciliationState:null,
			bankBranchId:null,
			bankBranchList:[],
		},
		methods : {
			getAddForm : getAddForm,
			add : addTgpf_BalanceOfAccount
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
			userStartId:this.userStartId,
			userUpdateId:this.userUpdateId,
			userRecordId:this.userRecordId,
			bankBranchId:this.bankBranchId,
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
			busiState:this.busiState,
			eCode:this.eCode,
			userStartId:this.userStartId,
			createTimeStamp:this.createTimeStamp,
			userUpdateId:this.userUpdateId,
			lastUpdateTimeStamp:this.lastUpdateTimeStamp,
			userRecordId:this.userRecordId,
			recordTimeStamp:this.recordTimeStamp,
			billTimeStamp:this.billTimeStamp,
			bankName:this.bankName,
			escrowedAccount:this.escrowedAccount,
			escrowedAccountTheName:this.escrowedAccountTheName,
			centerTotalCount:this.centerTotalCount,
			centerTotalAmount:this.centerTotalAmount,
			bankTotalCount:this.bankTotalCount,
			bankTotalAmount:this.bankTotalAmount,
			cyberBankTotalCount:this.cyberBankTotalCount,
			cyberBankTotalAmount:this.cyberBankTotalAmount,
			accountType:this.accountType,
			reconciliationDate:this.reconciliationDate,
			reconciliationState:this.reconciliationState,
			bankBranchId:this.bankBranchId,
		}
	}
	//列表操作--------------获取“更新”表单参数
	function getUpdateForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			theState:this.theState,
			busiState:this.busiState,
			eCode:this.eCode,
			userStartId:this.userStartId,
			createTimeStamp:this.createTimeStamp,
			userUpdateId:this.userUpdateId,
			lastUpdateTimeStamp:this.lastUpdateTimeStamp,
			userRecordId:this.userRecordId,
			recordTimeStamp:this.recordTimeStamp,
			billTimeStamp:this.billTimeStamp,
			bankName:this.bankName,
			escrowedAccount:this.escrowedAccount,
			escrowedAccountTheName:this.escrowedAccountTheName,
			centerTotalCount:this.centerTotalCount,
			centerTotalAmount:this.centerTotalAmount,
			bankTotalCount:this.bankTotalCount,
			bankTotalAmount:this.bankTotalAmount,
			cyberBankTotalCount:this.cyberBankTotalCount,
			cyberBankTotalAmount:this.cyberBankTotalAmount,
			accountType:this.accountType,
			reconciliationDate:this.reconciliationDate,
			reconciliationState:this.reconciliationState,
			bankBranchId:this.bankBranchId,
		}
	}
	function tgpf_BalanceOfAccountDel()
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
	function tgpf_BalanceOfAccountDelOne(tgpf_BalanceOfAccountId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[tgpf_BalanceOfAccountId],
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
		listVue.isAllSelected = (listVue.tgpf_BalanceOfAccountList.length > 0)
		&&	(listVue.selectItem.length == listVue.tgpf_BalanceOfAccountList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgpf_BalanceOfAccountList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgpf_BalanceOfAccountList.forEach(function(item) {
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
				listVue.tgpf_BalanceOfAccountList=jsonObj.tgpf_BalanceOfAccountList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tgpf_BalanceOfAccountListDiv').scrollIntoView();
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
	function showAjaxModal(tgpf_BalanceOfAccountModel)
	{
		//tgpf_BalanceOfAccountModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'tgpf_BalanceOfAccount', tgpf_BalanceOfAccountModel);
		//updateVue.$set("tgpf_BalanceOfAccount", tgpf_BalanceOfAccountModel);
		
		updateVue.theState = tgpf_BalanceOfAccountModel.theState;
		updateVue.busiState = tgpf_BalanceOfAccountModel.busiState;
		updateVue.eCode = tgpf_BalanceOfAccountModel.eCode;
		updateVue.userStartId = tgpf_BalanceOfAccountModel.userStartId;
		updateVue.createTimeStamp = tgpf_BalanceOfAccountModel.createTimeStamp;
		updateVue.userUpdateId = tgpf_BalanceOfAccountModel.userUpdateId;
		updateVue.lastUpdateTimeStamp = tgpf_BalanceOfAccountModel.lastUpdateTimeStamp;
		updateVue.userRecordId = tgpf_BalanceOfAccountModel.userRecordId;
		updateVue.recordTimeStamp = tgpf_BalanceOfAccountModel.recordTimeStamp;
		updateVue.billTimeStamp = tgpf_BalanceOfAccountModel.billTimeStamp;
		updateVue.bankName = tgpf_BalanceOfAccountModel.bankName;
		updateVue.escrowedAccount = tgpf_BalanceOfAccountModel.escrowedAccount;
		updateVue.escrowedAccountTheName = tgpf_BalanceOfAccountModel.escrowedAccountTheName;
		updateVue.centerTotalCount = tgpf_BalanceOfAccountModel.centerTotalCount;
		updateVue.centerTotalAmount = tgpf_BalanceOfAccountModel.centerTotalAmount;
		updateVue.bankTotalCount = tgpf_BalanceOfAccountModel.bankTotalCount;
		updateVue.bankTotalAmount = tgpf_BalanceOfAccountModel.bankTotalAmount;
		updateVue.cyberBankTotalCount = tgpf_BalanceOfAccountModel.cyberBankTotalCount;
		updateVue.cyberBankTotalAmount = tgpf_BalanceOfAccountModel.cyberBankTotalAmount;
		updateVue.accountType = tgpf_BalanceOfAccountModel.accountType;
		updateVue.reconciliationDate = tgpf_BalanceOfAccountModel.reconciliationDate;
		updateVue.reconciliationState = tgpf_BalanceOfAccountModel.reconciliationState;
		updateVue.bankBranchId = tgpf_BalanceOfAccountModel.bankBranchId;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateTgpf_BalanceOfAccount()
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
	function addTgpf_BalanceOfAccount()
	{
		new ServerInterface(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				$(baseInfo.addDivId).modal('hide');
				addVue.busiState = null;
				addVue.eCode = null;
				addVue.userStartId = null;
				addVue.createTimeStamp = null;
				addVue.userUpdateId = null;
				addVue.lastUpdateTimeStamp = null;
				addVue.userRecordId = null;
				addVue.recordTimeStamp = null;
				addVue.billTimeStamp = null;
				addVue.bankName = null;
				addVue.escrowedAccount = null;
				addVue.escrowedAccountTheName = null;
				addVue.centerTotalCount = null;
				addVue.centerTotalAmount = null;
				addVue.bankTotalCount = null;
				addVue.bankTotalAmount = null;
				addVue.cyberBankTotalCount = null;
				addVue.cyberBankTotalAmount = null;
				addVue.accountType = null;
				addVue.reconciliationDate = null;
				addVue.reconciliationState = null;
				addVue.bankBranchId = null;
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
	"listDivId":"#tgpf_BalanceOfAccountListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Tgpf_BalanceOfAccountList",
	"addInterface":"../Tgpf_BalanceOfAccountAdd",
	"deleteInterface":"../Tgpf_BalanceOfAccountDelete",
	"updateInterface":"../Tgpf_BalanceOfAccountUpdate"
});
