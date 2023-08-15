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
			developCompanyId:null,
			developCompanyList:[],
			projectId:null,
			projectList:[],
			buildingId:null,
			buildingList:[],
			paymentId:null,
			paymentList:[],
			tgpj_BuildingAccountId:null,
			tgpj_BuildingAccountList:[],
			tgpj_BuildingAccountLogList:[]
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			tgpj_BuildingAccountLogDelOne : tgpj_BuildingAccountLogDelOne,
			tgpj_BuildingAccountLogDel : tgpj_BuildingAccountLogDel,
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
			developCompanyId:null,
			developCompanyList:[],
			eCodeOfDevelopCompany:null,
			projectId:null,
			projectList:[],
			theNameOfProject:null,
			buildingId:null,
			buildingList:[],
			paymentId:null,
			paymentList:[],
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
			appropriateFrozenAmount:null,
			recordAvgPriceOfBuildingFromPresellSystem:null,
			recordAvgPriceOfBuilding:null,
			logId:null,
			actualAmount:null,
			paymentLines:null,
			paymentProportion:null,
			buildAmountPaid:null,
			buildAmountPay:null,
			totalAmountGuaranteed:null,
			relatedBusiCode:null,
			relatedBusiTableId:null,
			tgpj_BuildingAccountId:null,
			tgpj_BuildingAccountList:[],
			versionNo:null,
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateTgpj_BuildingAccountLog
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
			developCompanyId:null,
			developCompanyList:[],
			eCodeOfDevelopCompany:null,
			projectId:null,
			projectList:[],
			theNameOfProject:null,
			buildingId:null,
			buildingList:[],
			paymentId:null,
			paymentList:[],
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
			appropriateFrozenAmount:null,
			recordAvgPriceOfBuildingFromPresellSystem:null,
			recordAvgPriceOfBuilding:null,
			logId:null,
			actualAmount:null,
			paymentLines:null,
			paymentProportion:null,
			buildAmountPaid:null,
			buildAmountPay:null,
			totalAmountGuaranteed:null,
			relatedBusiCode:null,
			relatedBusiTableId:null,
			tgpj_BuildingAccountId:null,
			tgpj_BuildingAccountList:[],
			versionNo:null,
		},
		methods : {
			getAddForm : getAddForm,
			add : addTgpj_BuildingAccountLog
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
			developCompanyId:this.developCompanyId,
			projectId:this.projectId,
			buildingId:this.buildingId,
			paymentId:this.paymentId,
			tgpj_BuildingAccountId:this.tgpj_BuildingAccountId,
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
			developCompanyId:this.developCompanyId,
			eCodeOfDevelopCompany:this.eCodeOfDevelopCompany,
			projectId:this.projectId,
			theNameOfProject:this.theNameOfProject,
			buildingId:this.buildingId,
			paymentId:this.paymentId,
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
			appropriateFrozenAmount:this.appropriateFrozenAmount,
			recordAvgPriceOfBuildingFromPresellSystem:this.recordAvgPriceOfBuildingFromPresellSystem,
			recordAvgPriceOfBuilding:this.recordAvgPriceOfBuilding,
			logId:this.logId,
			actualAmount:this.actualAmount,
			paymentLines:this.paymentLines,
			paymentProportion:this.paymentProportion,
			buildAmountPaid:this.buildAmountPaid,
			buildAmountPay:this.buildAmountPay,
			totalAmountGuaranteed:this.totalAmountGuaranteed,
			relatedBusiCode:this.relatedBusiCode,
			relatedBusiTableId:this.relatedBusiTableId,
			tgpj_BuildingAccountId:this.tgpj_BuildingAccountId,
			versionNo:this.versionNo,
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
			developCompanyId:this.developCompanyId,
			eCodeOfDevelopCompany:this.eCodeOfDevelopCompany,
			projectId:this.projectId,
			theNameOfProject:this.theNameOfProject,
			buildingId:this.buildingId,
			paymentId:this.paymentId,
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
			appropriateFrozenAmount:this.appropriateFrozenAmount,
			recordAvgPriceOfBuildingFromPresellSystem:this.recordAvgPriceOfBuildingFromPresellSystem,
			recordAvgPriceOfBuilding:this.recordAvgPriceOfBuilding,
			logId:this.logId,
			actualAmount:this.actualAmount,
			paymentLines:this.paymentLines,
			paymentProportion:this.paymentProportion,
			buildAmountPaid:this.buildAmountPaid,
			buildAmountPay:this.buildAmountPay,
			totalAmountGuaranteed:this.totalAmountGuaranteed,
			relatedBusiCode:this.relatedBusiCode,
			relatedBusiTableId:this.relatedBusiTableId,
			tgpj_BuildingAccountId:this.tgpj_BuildingAccountId,
			versionNo:this.versionNo,
		}
	}
	function tgpj_BuildingAccountLogDel()
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
	function tgpj_BuildingAccountLogDelOne(tgpj_BuildingAccountLogId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[tgpj_BuildingAccountLogId],
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
		listVue.isAllSelected = (listVue.tgpj_BuildingAccountLogList.length > 0)
		&&	(listVue.selectItem.length == listVue.tgpj_BuildingAccountLogList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgpj_BuildingAccountLogList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgpj_BuildingAccountLogList.forEach(function(item) {
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
				listVue.tgpj_BuildingAccountLogList=jsonObj.tgpj_BuildingAccountLogList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tgpj_BuildingAccountLogListDiv').scrollIntoView();
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
	function showAjaxModal(tgpj_BuildingAccountLogModel)
	{
		//tgpj_BuildingAccountLogModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'tgpj_BuildingAccountLog', tgpj_BuildingAccountLogModel);
		//updateVue.$set("tgpj_BuildingAccountLog", tgpj_BuildingAccountLogModel);
		
		updateVue.theState = tgpj_BuildingAccountLogModel.theState;
		updateVue.busiState = tgpj_BuildingAccountLogModel.busiState;
		updateVue.eCode = tgpj_BuildingAccountLogModel.eCode;
		updateVue.userStartId = tgpj_BuildingAccountLogModel.userStartId;
		updateVue.createTimeStamp = tgpj_BuildingAccountLogModel.createTimeStamp;
		updateVue.userUpdateId = tgpj_BuildingAccountLogModel.userUpdateId;
		updateVue.lastUpdateTimeStamp = tgpj_BuildingAccountLogModel.lastUpdateTimeStamp;
		updateVue.userRecordId = tgpj_BuildingAccountLogModel.userRecordId;
		updateVue.recordTimeStamp = tgpj_BuildingAccountLogModel.recordTimeStamp;
		updateVue.developCompanyId = tgpj_BuildingAccountLogModel.developCompanyId;
		updateVue.eCodeOfDevelopCompany = tgpj_BuildingAccountLogModel.eCodeOfDevelopCompany;
		updateVue.projectId = tgpj_BuildingAccountLogModel.projectId;
		updateVue.theNameOfProject = tgpj_BuildingAccountLogModel.theNameOfProject;
		updateVue.buildingId = tgpj_BuildingAccountLogModel.buildingId;
		updateVue.paymentId = tgpj_BuildingAccountLogModel.paymentId;
		updateVue.eCodeOfBuilding = tgpj_BuildingAccountLogModel.eCodeOfBuilding;
		updateVue.escrowStandard = tgpj_BuildingAccountLogModel.escrowStandard;
		updateVue.escrowArea = tgpj_BuildingAccountLogModel.escrowArea;
		updateVue.buildingArea = tgpj_BuildingAccountLogModel.buildingArea;
		updateVue.orgLimitedAmount = tgpj_BuildingAccountLogModel.orgLimitedAmount;
		updateVue.currentFigureProgress = tgpj_BuildingAccountLogModel.currentFigureProgress;
		updateVue.currentLimitedRatio = tgpj_BuildingAccountLogModel.currentLimitedRatio;
		updateVue.nodeLimitedAmount = tgpj_BuildingAccountLogModel.nodeLimitedAmount;
		updateVue.totalGuaranteeAmount = tgpj_BuildingAccountLogModel.totalGuaranteeAmount;
		updateVue.cashLimitedAmount = tgpj_BuildingAccountLogModel.cashLimitedAmount;
		updateVue.effectiveLimitedAmount = tgpj_BuildingAccountLogModel.effectiveLimitedAmount;
		updateVue.totalAccountAmount = tgpj_BuildingAccountLogModel.totalAccountAmount;
		updateVue.spilloverAmount = tgpj_BuildingAccountLogModel.spilloverAmount;
		updateVue.payoutAmount = tgpj_BuildingAccountLogModel.payoutAmount;
		updateVue.appliedNoPayoutAmount = tgpj_BuildingAccountLogModel.appliedNoPayoutAmount;
		updateVue.applyRefundPayoutAmount = tgpj_BuildingAccountLogModel.applyRefundPayoutAmount;
		updateVue.refundAmount = tgpj_BuildingAccountLogModel.refundAmount;
		updateVue.currentEscrowFund = tgpj_BuildingAccountLogModel.currentEscrowFund;
		updateVue.allocableAmount = tgpj_BuildingAccountLogModel.allocableAmount;
		updateVue.appropriateFrozenAmount = tgpj_BuildingAccountLogModel.appropriateFrozenAmount;
		updateVue.recordAvgPriceOfBuildingFromPresellSystem = tgpj_BuildingAccountLogModel.recordAvgPriceOfBuildingFromPresellSystem;
		updateVue.recordAvgPriceOfBuilding = tgpj_BuildingAccountLogModel.recordAvgPriceOfBuilding;
		updateVue.logId = tgpj_BuildingAccountLogModel.logId;
		updateVue.actualAmount = tgpj_BuildingAccountLogModel.actualAmount;
		updateVue.paymentLines = tgpj_BuildingAccountLogModel.paymentLines;
		updateVue.paymentProportion = tgpj_BuildingAccountLogModel.paymentProportion;
		updateVue.buildAmountPaid = tgpj_BuildingAccountLogModel.buildAmountPaid;
		updateVue.buildAmountPay = tgpj_BuildingAccountLogModel.buildAmountPay;
		updateVue.totalAmountGuaranteed = tgpj_BuildingAccountLogModel.totalAmountGuaranteed;
		updateVue.relatedBusiCode = tgpj_BuildingAccountLogModel.relatedBusiCode;
		updateVue.relatedBusiTableId = tgpj_BuildingAccountLogModel.relatedBusiTableId;
		updateVue.tgpj_BuildingAccountId = tgpj_BuildingAccountLogModel.tgpj_BuildingAccountId;
		updateVue.versionNo = tgpj_BuildingAccountLogModel.versionNo;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateTgpj_BuildingAccountLog()
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
	function addTgpj_BuildingAccountLog()
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
				addVue.developCompanyId = null;
				addVue.eCodeOfDevelopCompany = null;
				addVue.projectId = null;
				addVue.theNameOfProject = null;
				addVue.buildingId = null;
				addVue.paymentId = null;
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
				addVue.appropriateFrozenAmount = null;
				addVue.recordAvgPriceOfBuildingFromPresellSystem = null;
				addVue.recordAvgPriceOfBuilding = null;
				addVue.logId = null;
				addVue.actualAmount = null;
				addVue.paymentLines = null;
				addVue.paymentProportion = null;
				addVue.buildAmountPaid = null;
				addVue.buildAmountPay = null;
				addVue.totalAmountGuaranteed = null;
				addVue.relatedBusiCode = null;
				addVue.relatedBusiTableId = null;
				addVue.tgpj_BuildingAccountId = null;
				addVue.versionNo = null;
				refresh();
			}
		});
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
	"listDivId":"#tgpj_BuildingAccountLogListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Tgpj_BuildingAccountLogList",
	"addInterface":"../Tgpj_BuildingAccountLogAdd",
	"deleteInterface":"../Tgpj_BuildingAccountLogDelete",
	"updateInterface":"../Tgpj_BuildingAccountLogUpdate"
});
