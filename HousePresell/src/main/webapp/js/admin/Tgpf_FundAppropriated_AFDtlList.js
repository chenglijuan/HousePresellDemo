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
			buildingId:null,
			buildingList:[],
			mainTableId:null,
			mainTableList:[],
			bankAccountSupervisedId:null,
			bankAccountSupervisedList:[],
			tgpf_FundAppropriated_AFDtlList:[]
		},
		methods : {
			refresh : refresh,
			initData:initData,
            tgpf_FundAppropriated_AFDtlAdd:tgpf_FundAppropriated_AFDtlAdd,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			listItemSelectHandle: listItemSelectHandle,
			tgpf_FundAppropriated_AFDtlDelOne : tgpf_FundAppropriated_AFDtlDelOne,
			tgpf_FundAppropriated_AFDtlDel : tgpf_FundAppropriated_AFDtlDel,
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
			buildingId:null,
			buildingList:[],
			eCodeOfBuilding:null,
			mainTableId:null,
			mainTableList:[],
			bankAccountSupervisedId:null,
			bankAccountSupervisedList:[],
			supervisedBankAccount:null,
			allocableAmount:null,
			appliedAmount:null,
			escrowStandard:null,
			orgLimitedAmount:null,
			currentFigureProgress:null,
			currentLimitedRatio:null,
			currentLimitedAmount:null,
			totalAccountAmount:null,
			appliedPayoutAmount:null,
			currentEscrowFund:null,
			refundAmount:null,
			payoutState:null,
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateTgpf_FundAppropriated_AFDtl
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
			buildingId:null,
			buildingList:[],
			eCodeOfBuilding:null,
			mainTableId:null,
			mainTableList:[],
			bankAccountSupervisedId:null,
			bankAccountSupervisedList:[],
			supervisedBankAccount:null,
			allocableAmount:null,
			appliedAmount:null,
			escrowStandard:null,
			orgLimitedAmount:null,
			currentFigureProgress:null,
			currentLimitedRatio:null,
			currentLimitedAmount:null,
			totalAccountAmount:null,
			appliedPayoutAmount:null,
			currentEscrowFund:null,
			refundAmount:null,
			payoutState:null,
		},
		methods : {
			getAddForm : getAddForm,
			add : addTgpf_FundAppropriated_AFDtl
		}
	});

	//------------------------方法定义-开始------------------//

    //跳转方法 - 新增
    function tgpf_FundAppropriated_AFDtlAdd()
    {
        $("#tabContainer").data("tabs").addTab({id: 'Tgpf_FundAppropriated_AFDtlAdd', text: '新增用款申请', closeable: true, url: 'Tgpf_FundAppropriated_AFDtlAdd.shtml'});
    }
	 
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
			buildingId:this.buildingId,
			mainTableId:this.mainTableId,
			bankAccountSupervisedId:this.bankAccountSupervisedId,
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
			buildingId:this.buildingId,
			eCodeOfBuilding:this.eCodeOfBuilding,
			mainTableId:this.mainTableId,
			bankAccountSupervisedId:this.bankAccountSupervisedId,
			supervisedBankAccount:this.supervisedBankAccount,
			allocableAmount:this.allocableAmount,
			appliedAmount:this.appliedAmount,
			escrowStandard:this.escrowStandard,
			orgLimitedAmount:this.orgLimitedAmount,
			currentFigureProgress:this.currentFigureProgress,
			currentLimitedRatio:this.currentLimitedRatio,
			currentLimitedAmount:this.currentLimitedAmount,
			totalAccountAmount:this.totalAccountAmount,
			appliedPayoutAmount:this.appliedPayoutAmount,
			currentEscrowFund:this.currentEscrowFund,
			refundAmount:this.refundAmount,
			payoutState:this.payoutState,
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
			buildingId:this.buildingId,
			eCodeOfBuilding:this.eCodeOfBuilding,
			mainTableId:this.mainTableId,
			bankAccountSupervisedId:this.bankAccountSupervisedId,
			supervisedBankAccount:this.supervisedBankAccount,
			allocableAmount:this.allocableAmount,
			appliedAmount:this.appliedAmount,
			escrowStandard:this.escrowStandard,
			orgLimitedAmount:this.orgLimitedAmount,
			currentFigureProgress:this.currentFigureProgress,
			currentLimitedRatio:this.currentLimitedRatio,
			currentLimitedAmount:this.currentLimitedAmount,
			totalAccountAmount:this.totalAccountAmount,
			appliedPayoutAmount:this.appliedPayoutAmount,
			currentEscrowFund:this.currentEscrowFund,
			refundAmount:this.refundAmount,
			payoutState:this.payoutState,
		}
	}
	function tgpf_FundAppropriated_AFDtlDel()
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
	function tgpf_FundAppropriated_AFDtlDelOne(tgpf_FundAppropriated_AFDtlId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[tgpf_FundAppropriated_AFDtlId],
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
		listVue.isAllSelected = (listVue.tgpf_FundAppropriated_AFDtlList.length > 0)
		&&	(listVue.selectItem.length == listVue.tgpf_FundAppropriated_AFDtlList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgpf_FundAppropriated_AFDtlList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgpf_FundAppropriated_AFDtlList.forEach(function(item) {
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
				listVue.tgpf_FundAppropriated_AFDtlList=jsonObj.tgpf_FundAppropriated_AFDtlList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tgpf_FundAppropriated_AFDtlListDiv').scrollIntoView();
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
	function showAjaxModal(tgpf_FundAppropriated_AFDtlModel)
	{
		//tgpf_FundAppropriated_AFDtlModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'tgpf_FundAppropriated_AFDtl', tgpf_FundAppropriated_AFDtlModel);
		//updateVue.$set("tgpf_FundAppropriated_AFDtl", tgpf_FundAppropriated_AFDtlModel);
		
		updateVue.theState = tgpf_FundAppropriated_AFDtlModel.theState;
		updateVue.busiState = tgpf_FundAppropriated_AFDtlModel.busiState;
		updateVue.eCode = tgpf_FundAppropriated_AFDtlModel.eCode;
		updateVue.userStartId = tgpf_FundAppropriated_AFDtlModel.userStartId;
		updateVue.createTimeStamp = tgpf_FundAppropriated_AFDtlModel.createTimeStamp;
		updateVue.lastUpdateTimeStamp = tgpf_FundAppropriated_AFDtlModel.lastUpdateTimeStamp;
		updateVue.userRecordId = tgpf_FundAppropriated_AFDtlModel.userRecordId;
		updateVue.recordTimeStamp = tgpf_FundAppropriated_AFDtlModel.recordTimeStamp;
		updateVue.buildingId = tgpf_FundAppropriated_AFDtlModel.buildingId;
		updateVue.eCodeOfBuilding = tgpf_FundAppropriated_AFDtlModel.eCodeOfBuilding;
		updateVue.mainTableId = tgpf_FundAppropriated_AFDtlModel.mainTableId;
		updateVue.bankAccountSupervisedId = tgpf_FundAppropriated_AFDtlModel.bankAccountSupervisedId;
		updateVue.supervisedBankAccount = tgpf_FundAppropriated_AFDtlModel.supervisedBankAccount;
		updateVue.allocableAmount = tgpf_FundAppropriated_AFDtlModel.allocableAmount;
		updateVue.appliedAmount = tgpf_FundAppropriated_AFDtlModel.appliedAmount;
		updateVue.escrowStandard = tgpf_FundAppropriated_AFDtlModel.escrowStandard;
		updateVue.orgLimitedAmount = tgpf_FundAppropriated_AFDtlModel.orgLimitedAmount;
		updateVue.currentFigureProgress = tgpf_FundAppropriated_AFDtlModel.currentFigureProgress;
		updateVue.currentLimitedRatio = tgpf_FundAppropriated_AFDtlModel.currentLimitedRatio;
		updateVue.currentLimitedAmount = tgpf_FundAppropriated_AFDtlModel.currentLimitedAmount;
		updateVue.totalAccountAmount = tgpf_FundAppropriated_AFDtlModel.totalAccountAmount;
		updateVue.appliedPayoutAmount = tgpf_FundAppropriated_AFDtlModel.appliedPayoutAmount;
		updateVue.currentEscrowFund = tgpf_FundAppropriated_AFDtlModel.currentEscrowFund;
		updateVue.refundAmount = tgpf_FundAppropriated_AFDtlModel.refundAmount;
		updateVue.payoutState = tgpf_FundAppropriated_AFDtlModel.payoutState;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateTgpf_FundAppropriated_AFDtl()
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
	function addTgpf_FundAppropriated_AFDtl()
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
				addVue.buildingId = null;
				addVue.eCodeOfBuilding = null;
				addVue.mainTableId = null;
				addVue.bankAccountSupervisedId = null;
				addVue.supervisedBankAccount = null;
				addVue.allocableAmount = null;
				addVue.appliedAmount = null;
				addVue.escrowStandard = null;
				addVue.orgLimitedAmount = null;
				addVue.currentFigureProgress = null;
				addVue.currentLimitedRatio = null;
				addVue.currentLimitedAmount = null;
				addVue.totalAccountAmount = null;
				addVue.appliedPayoutAmount = null;
				addVue.currentEscrowFund = null;
				addVue.refundAmount = null;
				addVue.payoutState = null;
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
	"listDivId":"#tgpf_FundAppropriated_AFDtlListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Tgpf_FundAppropriated_AFDtlList",
	"addInterface":"../Tgpf_FundAppropriated_AFDtlAdd",
	"deleteInterface":"../Tgpf_FundAppropriated_AFDtlDelete",
	"updateInterface":"../Tgpf_FundAppropriated_AFDtlUpdate"
});
