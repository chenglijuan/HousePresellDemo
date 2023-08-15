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
			buildingAccountLogId:null,
			buildingAccountLogList:[],
			bankAccountId:null,
			bankAccountList:[],
			tgpf_SpecialFundAppropriated_AFList:[]
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			tgpf_SpecialFundAppropriated_AFDelOne : tgpf_SpecialFundAppropriated_AFDelOne,
			tgpf_SpecialFundAppropriated_AFDel : tgpf_SpecialFundAppropriated_AFDel,
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
			approvalState:null,
			developCompanyId:null,
			developCompanyList:[],
			theNameOfDevelopCompany:null,
			projectId:null,
			projectList:[],
			theNameOfProject:null,
			buildingId:null,
			buildingList:[],
			eCodeFromConstruction:null,
			eCodeFromPublicSecurity:null,
			buildingAccountLogId:null,
			buildingAccountLogList:[],
			bankAccountId:null,
			bankAccountList:[],
			theAccountOfBankAccount:null,
			theNameOfBankAccount:null,
			theBankOfBankAccount:null,
			appropriatedType:null,
			appropriatedRemark:null,
			totalApplyAmount:null,
			applyDate:null,
			applyState:null,
			afPayoutDate:null,
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateTgpf_SpecialFundAppropriated_AF
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
			approvalState:null,
			developCompanyId:null,
			developCompanyList:[],
			theNameOfDevelopCompany:null,
			projectId:null,
			projectList:[],
			theNameOfProject:null,
			buildingId:null,
			buildingList:[],
			eCodeFromConstruction:null,
			eCodeFromPublicSecurity:null,
			buildingAccountLogId:null,
			buildingAccountLogList:[],
			bankAccountId:null,
			bankAccountList:[],
			theAccountOfBankAccount:null,
			theNameOfBankAccount:null,
			theBankOfBankAccount:null,
			appropriatedType:null,
			appropriatedRemark:null,
			totalApplyAmount:null,
			applyDate:null,
			applyState:null,
			afPayoutDate:null,
		},
		methods : {
			getAddForm : getAddForm,
			add : addTgpf_SpecialFundAppropriated_AF
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
			buildingAccountLogId:this.buildingAccountLogId,
			bankAccountId:this.bankAccountId,
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
			approvalState:this.approvalState,
			developCompanyId:this.developCompanyId,
			theNameOfDevelopCompany:this.theNameOfDevelopCompany,
			projectId:this.projectId,
			theNameOfProject:this.theNameOfProject,
			buildingId:this.buildingId,
			eCodeFromConstruction:this.eCodeFromConstruction,
			eCodeFromPublicSecurity:this.eCodeFromPublicSecurity,
			buildingAccountLogId:this.buildingAccountLogId,
			bankAccountId:this.bankAccountId,
			theAccountOfBankAccount:this.theAccountOfBankAccount,
			theNameOfBankAccount:this.theNameOfBankAccount,
			theBankOfBankAccount:this.theBankOfBankAccount,
			appropriatedType:this.appropriatedType,
			appropriatedRemark:this.appropriatedRemark,
			totalApplyAmount:this.totalApplyAmount,
			applyDate:this.applyDate,
			applyState:this.applyState,
			afPayoutDate:this.afPayoutDate,
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
			approvalState:this.approvalState,
			developCompanyId:this.developCompanyId,
			theNameOfDevelopCompany:this.theNameOfDevelopCompany,
			projectId:this.projectId,
			theNameOfProject:this.theNameOfProject,
			buildingId:this.buildingId,
			eCodeFromConstruction:this.eCodeFromConstruction,
			eCodeFromPublicSecurity:this.eCodeFromPublicSecurity,
			buildingAccountLogId:this.buildingAccountLogId,
			bankAccountId:this.bankAccountId,
			theAccountOfBankAccount:this.theAccountOfBankAccount,
			theNameOfBankAccount:this.theNameOfBankAccount,
			theBankOfBankAccount:this.theBankOfBankAccount,
			appropriatedType:this.appropriatedType,
			appropriatedRemark:this.appropriatedRemark,
			totalApplyAmount:this.totalApplyAmount,
			applyDate:this.applyDate,
			applyState:this.applyState,
			afPayoutDate:this.afPayoutDate,
		}
	}
	function tgpf_SpecialFundAppropriated_AFDel()
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
	function tgpf_SpecialFundAppropriated_AFDelOne(tgpf_SpecialFundAppropriated_AFId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[tgpf_SpecialFundAppropriated_AFId],
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
		listVue.isAllSelected = (listVue.tgpf_SpecialFundAppropriated_AFList.length > 0)
		&&	(listVue.selectItem.length == listVue.tgpf_SpecialFundAppropriated_AFList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgpf_SpecialFundAppropriated_AFList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgpf_SpecialFundAppropriated_AFList.forEach(function(item) {
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
				listVue.tgpf_SpecialFundAppropriated_AFList=jsonObj.tgpf_SpecialFundAppropriated_AFList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tgpf_SpecialFundAppropriated_AFListDiv').scrollIntoView();
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
	function showAjaxModal(tgpf_SpecialFundAppropriated_AFModel)
	{
		//tgpf_SpecialFundAppropriated_AFModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'tgpf_SpecialFundAppropriated_AF', tgpf_SpecialFundAppropriated_AFModel);
		//updateVue.$set("tgpf_SpecialFundAppropriated_AF", tgpf_SpecialFundAppropriated_AFModel);
		
		updateVue.theState = tgpf_SpecialFundAppropriated_AFModel.theState;
		updateVue.busiState = tgpf_SpecialFundAppropriated_AFModel.busiState;
		updateVue.eCode = tgpf_SpecialFundAppropriated_AFModel.eCode;
		updateVue.userStartId = tgpf_SpecialFundAppropriated_AFModel.userStartId;
		updateVue.createTimeStamp = tgpf_SpecialFundAppropriated_AFModel.createTimeStamp;
		updateVue.userUpdateId = tgpf_SpecialFundAppropriated_AFModel.userUpdateId;
		updateVue.lastUpdateTimeStamp = tgpf_SpecialFundAppropriated_AFModel.lastUpdateTimeStamp;
		updateVue.userRecordId = tgpf_SpecialFundAppropriated_AFModel.userRecordId;
		updateVue.recordTimeStamp = tgpf_SpecialFundAppropriated_AFModel.recordTimeStamp;
		updateVue.approvalState = tgpf_SpecialFundAppropriated_AFModel.approvalState;
		updateVue.developCompanyId = tgpf_SpecialFundAppropriated_AFModel.developCompanyId;
		updateVue.theNameOfDevelopCompany = tgpf_SpecialFundAppropriated_AFModel.theNameOfDevelopCompany;
		updateVue.projectId = tgpf_SpecialFundAppropriated_AFModel.projectId;
		updateVue.theNameOfProject = tgpf_SpecialFundAppropriated_AFModel.theNameOfProject;
		updateVue.buildingId = tgpf_SpecialFundAppropriated_AFModel.buildingId;
		updateVue.eCodeFromConstruction = tgpf_SpecialFundAppropriated_AFModel.eCodeFromConstruction;
		updateVue.eCodeFromPublicSecurity = tgpf_SpecialFundAppropriated_AFModel.eCodeFromPublicSecurity;
		updateVue.buildingAccountLogId = tgpf_SpecialFundAppropriated_AFModel.buildingAccountLogId;
		updateVue.bankAccountId = tgpf_SpecialFundAppropriated_AFModel.bankAccountId;
		updateVue.theAccountOfBankAccount = tgpf_SpecialFundAppropriated_AFModel.theAccountOfBankAccount;
		updateVue.theNameOfBankAccount = tgpf_SpecialFundAppropriated_AFModel.theNameOfBankAccount;
		updateVue.theBankOfBankAccount = tgpf_SpecialFundAppropriated_AFModel.theBankOfBankAccount;
		updateVue.appropriatedType = tgpf_SpecialFundAppropriated_AFModel.appropriatedType;
		updateVue.appropriatedRemark = tgpf_SpecialFundAppropriated_AFModel.appropriatedRemark;
		updateVue.totalApplyAmount = tgpf_SpecialFundAppropriated_AFModel.totalApplyAmount;
		updateVue.applyDate = tgpf_SpecialFundAppropriated_AFModel.applyDate;
		updateVue.applyState = tgpf_SpecialFundAppropriated_AFModel.applyState;
		updateVue.afPayoutDate = tgpf_SpecialFundAppropriated_AFModel.afPayoutDate;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateTgpf_SpecialFundAppropriated_AF()
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
	function addTgpf_SpecialFundAppropriated_AF()
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
				addVue.approvalState = null;
				addVue.developCompanyId = null;
				addVue.theNameOfDevelopCompany = null;
				addVue.projectId = null;
				addVue.theNameOfProject = null;
				addVue.buildingId = null;
				addVue.eCodeFromConstruction = null;
				addVue.eCodeFromPublicSecurity = null;
				addVue.buildingAccountLogId = null;
				addVue.bankAccountId = null;
				addVue.theAccountOfBankAccount = null;
				addVue.theNameOfBankAccount = null;
				addVue.theBankOfBankAccount = null;
				addVue.appropriatedType = null;
				addVue.appropriatedRemark = null;
				addVue.totalApplyAmount = null;
				addVue.applyDate = null;
				addVue.applyState = null;
				addVue.afPayoutDate = null;
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
	"listDivId":"#tgpf_SpecialFundAppropriated_AFListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Tgpf_SpecialFundAppropriated_AFList",
	"addInterface":"../Tgpf_SpecialFundAppropriated_AFAdd",
	"deleteInterface":"../Tgpf_SpecialFundAppropriated_AFDelete",
	"updateInterface":"../Tgpf_SpecialFundAppropriated_AFUpdate"
});
