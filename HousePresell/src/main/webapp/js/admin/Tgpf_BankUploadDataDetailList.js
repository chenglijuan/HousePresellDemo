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
			userRecordId:null,
			userRecordList:[],
			bankId:null,
			bankList:[],
			bankBranchId:null,
			bankBranchList:[],
			bankAccountEscrowedId:null,
			bankAccountEscrowedList:[],
			tgpf_BankUploadDataDetailList:[]
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			listItemSelectHandle: listItemSelectHandle,
			tgpf_BankUploadDataDetailDelOne : tgpf_BankUploadDataDetailDelOne,
			tgpf_BankUploadDataDetailDel : tgpf_BankUploadDataDetailDel,
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
			lastUpdateTimeStamp:null,
			userRecordId:null,
			userRecordList:[],
			recordTimeStamp:null,
			bankId:null,
			bankList:[],
			theNameOfBank:null,
			bankBranchId:null,
			bankBranchList:[],
			theNameOfBankBranch:null,
			bankAccountEscrowedId:null,
			bankAccountEscrowedList:[],
			theNameOfBankAccountEscrowed:null,
			theAccountBankAccountEscrowed:null,
			theAccountOfBankAccountEscrowed:null,
			tradeAmount:null,
			enterTimeStamp:null,
			recipientAccount:null,
			recipientName:null,
			lastCfgUser:null,
			lastCfgTimeStamp:null,
			bkpltno:null,
			eCodeOfTripleAgreement:null,
			reconciliationState:null,
			reconciliationStamp:null,
			remark:null,
			coreno:null,
			reconciliationUser:null,
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateTgpf_BankUploadDataDetail
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
			lastUpdateTimeStamp:null,
			userRecordId:null,
			userRecordList:[],
			recordTimeStamp:null,
			bankId:null,
			bankList:[],
			theNameOfBank:null,
			bankBranchId:null,
			bankBranchList:[],
			theNameOfBankBranch:null,
			bankAccountEscrowedId:null,
			bankAccountEscrowedList:[],
			theNameOfBankAccountEscrowed:null,
			theAccountBankAccountEscrowed:null,
			theAccountOfBankAccountEscrowed:null,
			tradeAmount:null,
			enterTimeStamp:null,
			recipientAccount:null,
			recipientName:null,
			lastCfgUser:null,
			lastCfgTimeStamp:null,
			bkpltno:null,
			eCodeOfTripleAgreement:null,
			reconciliationState:null,
			reconciliationStamp:null,
			remark:null,
			coreno:null,
			reconciliationUser:null,
		},
		methods : {
			getAddForm : getAddForm,
			add : addTgpf_BankUploadDataDetail
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
			userRecordId:this.userRecordId,
			bankId:this.bankId,
			bankBranchId:this.bankBranchId,
			bankAccountEscrowedId:this.bankAccountEscrowedId,
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
			lastUpdateTimeStamp:this.lastUpdateTimeStamp,
			userRecordId:this.userRecordId,
			recordTimeStamp:this.recordTimeStamp,
			bankId:this.bankId,
			theNameOfBank:this.theNameOfBank,
			bankBranchId:this.bankBranchId,
			theNameOfBankBranch:this.theNameOfBankBranch,
			bankAccountEscrowedId:this.bankAccountEscrowedId,
			theNameOfBankAccountEscrowed:this.theNameOfBankAccountEscrowed,
			theAccountBankAccountEscrowed:this.theAccountBankAccountEscrowed,
			theAccountOfBankAccountEscrowed:this.theAccountOfBankAccountEscrowed,
			tradeAmount:this.tradeAmount,
			enterTimeStamp:this.enterTimeStamp,
			recipientAccount:this.recipientAccount,
			recipientName:this.recipientName,
			lastCfgUser:this.lastCfgUser,
			lastCfgTimeStamp:this.lastCfgTimeStamp,
			bkpltno:this.bkpltno,
			eCodeOfTripleAgreement:this.eCodeOfTripleAgreement,
			reconciliationState:this.reconciliationState,
			reconciliationStamp:this.reconciliationStamp,
			remark:this.remark,
			coreno:this.coreno,
			reconciliationUser:this.reconciliationUser,
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
			lastUpdateTimeStamp:this.lastUpdateTimeStamp,
			userRecordId:this.userRecordId,
			recordTimeStamp:this.recordTimeStamp,
			bankId:this.bankId,
			theNameOfBank:this.theNameOfBank,
			bankBranchId:this.bankBranchId,
			theNameOfBankBranch:this.theNameOfBankBranch,
			bankAccountEscrowedId:this.bankAccountEscrowedId,
			theNameOfBankAccountEscrowed:this.theNameOfBankAccountEscrowed,
			theAccountBankAccountEscrowed:this.theAccountBankAccountEscrowed,
			theAccountOfBankAccountEscrowed:this.theAccountOfBankAccountEscrowed,
			tradeAmount:this.tradeAmount,
			enterTimeStamp:this.enterTimeStamp,
			recipientAccount:this.recipientAccount,
			recipientName:this.recipientName,
			lastCfgUser:this.lastCfgUser,
			lastCfgTimeStamp:this.lastCfgTimeStamp,
			bkpltno:this.bkpltno,
			eCodeOfTripleAgreement:this.eCodeOfTripleAgreement,
			reconciliationState:this.reconciliationState,
			reconciliationStamp:this.reconciliationStamp,
			remark:this.remark,
			coreno:this.coreno,
			reconciliationUser:this.reconciliationUser,
		}
	}
	function tgpf_BankUploadDataDetailDel()
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
	function tgpf_BankUploadDataDetailDelOne(tgpf_BankUploadDataDetailId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[tgpf_BankUploadDataDetailId],
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
		listVue.isAllSelected = (listVue.tgpf_BankUploadDataDetailList.length > 0)
		&&	(listVue.selectItem.length == listVue.tgpf_BankUploadDataDetailList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgpf_BankUploadDataDetailList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgpf_BankUploadDataDetailList.forEach(function(item) {
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
				listVue.tgpf_BankUploadDataDetailList=jsonObj.tgpf_BankUploadDataDetailList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tgpf_BankUploadDataDetailListDiv').scrollIntoView();
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
	function showAjaxModal(tgpf_BankUploadDataDetailModel)
	{
		//tgpf_BankUploadDataDetailModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'tgpf_BankUploadDataDetail', tgpf_BankUploadDataDetailModel);
		//updateVue.$set("tgpf_BankUploadDataDetail", tgpf_BankUploadDataDetailModel);
		
		updateVue.theState = tgpf_BankUploadDataDetailModel.theState;
		updateVue.busiState = tgpf_BankUploadDataDetailModel.busiState;
		updateVue.eCode = tgpf_BankUploadDataDetailModel.eCode;
		updateVue.userStartId = tgpf_BankUploadDataDetailModel.userStartId;
		updateVue.createTimeStamp = tgpf_BankUploadDataDetailModel.createTimeStamp;
		updateVue.lastUpdateTimeStamp = tgpf_BankUploadDataDetailModel.lastUpdateTimeStamp;
		updateVue.userRecordId = tgpf_BankUploadDataDetailModel.userRecordId;
		updateVue.recordTimeStamp = tgpf_BankUploadDataDetailModel.recordTimeStamp;
		updateVue.bankId = tgpf_BankUploadDataDetailModel.bankId;
		updateVue.theNameOfBank = tgpf_BankUploadDataDetailModel.theNameOfBank;
		updateVue.bankBranchId = tgpf_BankUploadDataDetailModel.bankBranchId;
		updateVue.theNameOfBankBranch = tgpf_BankUploadDataDetailModel.theNameOfBankBranch;
		updateVue.bankAccountEscrowedId = tgpf_BankUploadDataDetailModel.bankAccountEscrowedId;
		updateVue.theNameOfBankAccountEscrowed = tgpf_BankUploadDataDetailModel.theNameOfBankAccountEscrowed;
		updateVue.theAccountBankAccountEscrowed = tgpf_BankUploadDataDetailModel.theAccountBankAccountEscrowed;
		updateVue.theAccountOfBankAccountEscrowed = tgpf_BankUploadDataDetailModel.theAccountOfBankAccountEscrowed;
		updateVue.tradeAmount = tgpf_BankUploadDataDetailModel.tradeAmount;
		updateVue.enterTimeStamp = tgpf_BankUploadDataDetailModel.enterTimeStamp;
		updateVue.recipientAccount = tgpf_BankUploadDataDetailModel.recipientAccount;
		updateVue.recipientName = tgpf_BankUploadDataDetailModel.recipientName;
		updateVue.lastCfgUser = tgpf_BankUploadDataDetailModel.lastCfgUser;
		updateVue.lastCfgTimeStamp = tgpf_BankUploadDataDetailModel.lastCfgTimeStamp;
		updateVue.bkpltno = tgpf_BankUploadDataDetailModel.bkpltno;
		updateVue.eCodeOfTripleAgreement = tgpf_BankUploadDataDetailModel.eCodeOfTripleAgreement;
		updateVue.reconciliationState = tgpf_BankUploadDataDetailModel.reconciliationState;
		updateVue.reconciliationStamp = tgpf_BankUploadDataDetailModel.reconciliationStamp;
		updateVue.remark = tgpf_BankUploadDataDetailModel.remark;
		updateVue.coreno = tgpf_BankUploadDataDetailModel.coreno;
		updateVue.reconciliationUser = tgpf_BankUploadDataDetailModel.reconciliationUser;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateTgpf_BankUploadDataDetail()
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
	function addTgpf_BankUploadDataDetail()
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
				addVue.lastUpdateTimeStamp = null;
				addVue.userRecordId = null;
				addVue.recordTimeStamp = null;
				addVue.bankId = null;
				addVue.theNameOfBank = null;
				addVue.bankBranchId = null;
				addVue.theNameOfBankBranch = null;
				addVue.bankAccountEscrowedId = null;
				addVue.theNameOfBankAccountEscrowed = null;
				addVue.theAccountBankAccountEscrowed = null;
				addVue.theAccountOfBankAccountEscrowed = null;
				addVue.tradeAmount = null;
				addVue.enterTimeStamp = null;
				addVue.recipientAccount = null;
				addVue.recipientName = null;
				addVue.lastCfgUser = null;
				addVue.lastCfgTimeStamp = null;
				addVue.bkpltno = null;
				addVue.eCodeOfTripleAgreement = null;
				addVue.reconciliationState = null;
				addVue.reconciliationStamp = null;
				addVue.remark = null;
				addVue.coreno = null;
				addVue.reconciliationUser = null;
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
	"listDivId":"#tgpf_BankUploadDataDetailListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Tgpf_BankUploadDataDetailList",
	"addInterface":"../Tgpf_BankUploadDataDetailAdd",
	"deleteInterface":"../Tgpf_BankUploadDataDetailDelete",
	"updateInterface":"../Tgpf_BankUploadDataDetailUpdate"
});
