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
			developCompanyId:null,
			developCompanyList:[],
			projectId:null,
			projectList:[],
			buildingId:null,
			buildingList:[],
			tgpj_BuildingAccountList:[]
		},
		methods : {
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			listItemSelectHandle: listItemSelectHandle,
			tgpj_BuildingAccountDelOne : tgpj_BuildingAccountDelOne,
			tgpj_BuildingAccountDel : tgpj_BuildingAccountDel,
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
			developCompanyId:null,
			developCompanyList:[],
			eCodeOfDevelopCompany:null,
			projectId:null,
			projectList:[],
			theNameOfProject:null,
			buildingId:null,
			buildingList:[],
			eCodeOfBuilding:null,
			escrowStandard:null,
			escrowArea:null,
			buildingArea:null,
			orgLimitedAmount:null,
			currentFigureProgress:null,
			currentLimitedRatio:null,
			nodeLimitedAmount:null,
			totalGuaranteeAmount:null,
			cashLimitedAmount:null,
			effectiveLimitedAmount:null,
			totalAccountAmount:null,
			spilloverAmount:null,
			payoutAmount:null,
			appliedNoPayoutAmount:null,
			applyRefundPayoutAmount:null,
			refundAmount:null,
			currentEscrowFund:null,
			allocableAmount:null,
			recordAvgPriceOfBuildingFromPresellSystem:null,
			recordAvgPriceOfBuilding:null,
			logId:null,
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateTgpj_BuildingAccount
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
			developCompanyId:null,
			developCompanyList:[],
			eCodeOfDevelopCompany:null,
			projectId:null,
			projectList:[],
			theNameOfProject:null,
			buildingId:null,
			buildingList:[],
			eCodeOfBuilding:null,
			escrowStandard:null,
			escrowArea:null,
			buildingArea:null,
			orgLimitedAmount:null,
			currentFigureProgress:null,
			currentLimitedRatio:null,
			nodeLimitedAmount:null,
			totalGuaranteeAmount:null,
			cashLimitedAmount:null,
			effectiveLimitedAmount:null,
			totalAccountAmount:null,
			spilloverAmount:null,
			payoutAmount:null,
			appliedNoPayoutAmount:null,
			applyRefundPayoutAmount:null,
			refundAmount:null,
			currentEscrowFund:null,
			allocableAmount:null,
			recordAvgPriceOfBuildingFromPresellSystem:null,
			recordAvgPriceOfBuilding:null,
			logId:null,
		},
		methods : {
			getAddForm : getAddForm,
			add : addTgpj_BuildingAccount
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
			developCompanyId:this.developCompanyId,
			projectId:this.projectId,
			buildingId:this.buildingId,
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
			developCompanyId:this.developCompanyId,
			eCodeOfDevelopCompany:this.eCodeOfDevelopCompany,
			projectId:this.projectId,
			theNameOfProject:this.theNameOfProject,
			buildingId:this.buildingId,
			eCodeOfBuilding:this.eCodeOfBuilding,
			escrowStandard:this.escrowStandard,
			escrowArea:this.escrowArea,
			buildingArea:this.buildingArea,
			orgLimitedAmount:this.orgLimitedAmount,
			currentFigureProgress:this.currentFigureProgress,
			currentLimitedRatio:this.currentLimitedRatio,
			nodeLimitedAmount:this.nodeLimitedAmount,
			totalGuaranteeAmount:this.totalGuaranteeAmount,
			cashLimitedAmount:this.cashLimitedAmount,
			effectiveLimitedAmount:this.effectiveLimitedAmount,
			totalAccountAmount:this.totalAccountAmount,
			spilloverAmount:this.spilloverAmount,
			payoutAmount:this.payoutAmount,
			appliedNoPayoutAmount:this.appliedNoPayoutAmount,
			applyRefundPayoutAmount:this.applyRefundPayoutAmount,
			refundAmount:this.refundAmount,
			currentEscrowFund:this.currentEscrowFund,
			allocableAmount:this.allocableAmount,
			recordAvgPriceOfBuildingFromPresellSystem:this.recordAvgPriceOfBuildingFromPresellSystem,
			recordAvgPriceOfBuilding:this.recordAvgPriceOfBuilding,
			logId:this.logId,
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
			developCompanyId:this.developCompanyId,
			eCodeOfDevelopCompany:this.eCodeOfDevelopCompany,
			projectId:this.projectId,
			theNameOfProject:this.theNameOfProject,
			buildingId:this.buildingId,
			eCodeOfBuilding:this.eCodeOfBuilding,
			escrowStandard:this.escrowStandard,
			escrowArea:this.escrowArea,
			buildingArea:this.buildingArea,
			orgLimitedAmount:this.orgLimitedAmount,
			currentFigureProgress:this.currentFigureProgress,
			currentLimitedRatio:this.currentLimitedRatio,
			nodeLimitedAmount:this.nodeLimitedAmount,
			totalGuaranteeAmount:this.totalGuaranteeAmount,
			cashLimitedAmount:this.cashLimitedAmount,
			effectiveLimitedAmount:this.effectiveLimitedAmount,
			totalAccountAmount:this.totalAccountAmount,
			spilloverAmount:this.spilloverAmount,
			payoutAmount:this.payoutAmount,
			appliedNoPayoutAmount:this.appliedNoPayoutAmount,
			applyRefundPayoutAmount:this.applyRefundPayoutAmount,
			refundAmount:this.refundAmount,
			currentEscrowFund:this.currentEscrowFund,
			allocableAmount:this.allocableAmount,
			recordAvgPriceOfBuildingFromPresellSystem:this.recordAvgPriceOfBuildingFromPresellSystem,
			recordAvgPriceOfBuilding:this.recordAvgPriceOfBuilding,
			logId:this.logId,
		}
	}
	function tgpj_BuildingAccountDel()
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
	function tgpj_BuildingAccountDelOne(tgpj_BuildingAccountId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[tgpj_BuildingAccountId],
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
		listVue.isAllSelected = (listVue.tgpj_BuildingAccountList.length > 0)
		&&	(listVue.selectItem.length == listVue.tgpj_BuildingAccountList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgpj_BuildingAccountList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgpj_BuildingAccountList.forEach(function(item) {
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
				listVue.tgpj_BuildingAccountList=jsonObj.tgpj_BuildingAccountList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tgpj_BuildingAccountListDiv').scrollIntoView();
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
	function showAjaxModal(tgpj_BuildingAccountModel)
	{
		//tgpj_BuildingAccountModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'tgpj_BuildingAccount', tgpj_BuildingAccountModel);
		//updateVue.$set("tgpj_BuildingAccount", tgpj_BuildingAccountModel);
		
		updateVue.theState = tgpj_BuildingAccountModel.theState;
		updateVue.busiState = tgpj_BuildingAccountModel.busiState;
		updateVue.eCode = tgpj_BuildingAccountModel.eCode;
		updateVue.userStartId = tgpj_BuildingAccountModel.userStartId;
		updateVue.createTimeStamp = tgpj_BuildingAccountModel.createTimeStamp;
		updateVue.lastUpdateTimeStamp = tgpj_BuildingAccountModel.lastUpdateTimeStamp;
		updateVue.userRecordId = tgpj_BuildingAccountModel.userRecordId;
		updateVue.recordTimeStamp = tgpj_BuildingAccountModel.recordTimeStamp;
		updateVue.developCompanyId = tgpj_BuildingAccountModel.developCompanyId;
		updateVue.eCodeOfDevelopCompany = tgpj_BuildingAccountModel.eCodeOfDevelopCompany;
		updateVue.projectId = tgpj_BuildingAccountModel.projectId;
		updateVue.theNameOfProject = tgpj_BuildingAccountModel.theNameOfProject;
		updateVue.buildingId = tgpj_BuildingAccountModel.buildingId;
		updateVue.eCodeOfBuilding = tgpj_BuildingAccountModel.eCodeOfBuilding;
		updateVue.escrowStandard = tgpj_BuildingAccountModel.escrowStandard;
		updateVue.escrowArea = tgpj_BuildingAccountModel.escrowArea;
		updateVue.buildingArea = tgpj_BuildingAccountModel.buildingArea;
		updateVue.orgLimitedAmount = tgpj_BuildingAccountModel.orgLimitedAmount;
		updateVue.currentFigureProgress = tgpj_BuildingAccountModel.currentFigureProgress;
		updateVue.currentLimitedRatio = tgpj_BuildingAccountModel.currentLimitedRatio;
		updateVue.nodeLimitedAmount = tgpj_BuildingAccountModel.nodeLimitedAmount;
		updateVue.totalGuaranteeAmount = tgpj_BuildingAccountModel.totalGuaranteeAmount;
		updateVue.cashLimitedAmount = tgpj_BuildingAccountModel.cashLimitedAmount;
		updateVue.effectiveLimitedAmount = tgpj_BuildingAccountModel.effectiveLimitedAmount;
		updateVue.totalAccountAmount = tgpj_BuildingAccountModel.totalAccountAmount;
		updateVue.spilloverAmount = tgpj_BuildingAccountModel.spilloverAmount;
		updateVue.payoutAmount = tgpj_BuildingAccountModel.payoutAmount;
		updateVue.appliedNoPayoutAmount = tgpj_BuildingAccountModel.appliedNoPayoutAmount;
		updateVue.applyRefundPayoutAmount = tgpj_BuildingAccountModel.applyRefundPayoutAmount;
		updateVue.refundAmount = tgpj_BuildingAccountModel.refundAmount;
		updateVue.currentEscrowFund = tgpj_BuildingAccountModel.currentEscrowFund;
		updateVue.allocableAmount = tgpj_BuildingAccountModel.allocableAmount;
		updateVue.recordAvgPriceOfBuildingFromPresellSystem = tgpj_BuildingAccountModel.recordAvgPriceOfBuildingFromPresellSystem;
		updateVue.recordAvgPriceOfBuilding = tgpj_BuildingAccountModel.recordAvgPriceOfBuilding;
		updateVue.logId = tgpj_BuildingAccountModel.logId;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateTgpj_BuildingAccount()
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
	function addTgpj_BuildingAccount()
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
				addVue.developCompanyId = null;
				addVue.eCodeOfDevelopCompany = null;
				addVue.projectId = null;
				addVue.theNameOfProject = null;
				addVue.buildingId = null;
				addVue.eCodeOfBuilding = null;
				addVue.escrowStandard = null;
				addVue.escrowArea = null;
				addVue.buildingArea = null;
				addVue.orgLimitedAmount = null;
				addVue.currentFigureProgress = null;
				addVue.currentLimitedRatio = null;
				addVue.nodeLimitedAmount = null;
				addVue.totalGuaranteeAmount = null;
				addVue.cashLimitedAmount = null;
				addVue.effectiveLimitedAmount = null;
				addVue.totalAccountAmount = null;
				addVue.spilloverAmount = null;
				addVue.payoutAmount = null;
				addVue.appliedNoPayoutAmount = null;
				addVue.applyRefundPayoutAmount = null;
				addVue.refundAmount = null;
				addVue.currentEscrowFund = null;
				addVue.allocableAmount = null;
				addVue.recordAvgPriceOfBuildingFromPresellSystem = null;
				addVue.recordAvgPriceOfBuilding = null;
				addVue.logId = null;
				refresh();
			}
		});
	}
	
	function indexMethod(index) {
		return generalIndexMethod(index, listVue)
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
	"listDivId":"#tgpj_BuildingAccountListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Tgpj_BuildingAccountList",
	"addInterface":"../Tgpj_BuildingAccountAdd",
	"deleteInterface":"../Tgpj_BuildingAccountDelete",
	"updateInterface":"../Tgpj_BuildingAccountUpdate"
});
