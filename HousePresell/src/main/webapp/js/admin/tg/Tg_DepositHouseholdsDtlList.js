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
			userStartId:null,
			userStartList:[],
			userRecordId:null,
			userRecordList:[],
			bankId:null,
			bankList:[],
			projectId : "", // 项目名称
			Tg_DepositHouseholdsDtl_View:[],
			qs_projectNameList : [], // 显示项目名称
			paymentMethod: "",
			paymentMethodList : [
				{tableId:"1",theName:"一次性付款"},
				{tableId:"2",theName:"分期付款"},
				{tableId:"3",theName:"贷款方式付款"},
				{tableId:"4",theName:"其它方式"},
			],
			tg_AreaList: [],
			areaId: "",
			tg_projectNameList : [], //显示项目名称
		},
		methods : {
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			listItemSelectHandle: listItemSelectHandle,
			tg_DepositManagementDelOne : tg_DepositManagementDelOne,
			tg_DepositManagementDel : tg_DepositManagementDel,
			showAjaxModal:showAjaxModal,
			exportExcelHandle : exportExcelHandle,
			search:search,
			loadProject : loadProject,
			checkAllClicked : checkAllClicked,
			changePageNumber : function(data){
				if (listVue.pageNumber != data) {
					listVue.pageNumber = data;
					listVue.refresh();
				}
			},
			changeCountPerPage:function(data){
				if (listVue.countPerPage != data) {
					listVue.countPerPage = data;
					listVue.refresh();
				}
			},
			/*getProjectId: function(data) {
				listVue.projectId = data.tableId;
			},*/
			handleReset: handleReset,
			getPaymentMethod : function(data){
				listVue.paymentMethod = data.tableId;
			},
			emptyPaymentMethod : function(){
				listVue.paymentMethod = null;
			},
			changeCityFun : function(data){
				listVue.areaId = data.tableId;
				listVue.projectId = null;
				listVue.tg_projectNameList = [];
				var model = {
						interfaceVersion:this.interfaceVersion,
						cityRegionId: listVue.areaId,
				}
				new ServerInterface(baseInfo.loadProjectInterface).execute(model, function(jsonObj){
					if (jsonObj.result != "success") {
						$(baseInfo.edModelDivId).modal({
							backdrop : 'static'
						});
						listVue.errMsg = jsonObj.info;
					} else {
						listVue.tg_projectNameList = jsonObj.empj_ProjectInfoList;
					}
				});
			}, //改变区域的方法
			changeCityFunEmpty : function(){
				listVue.areaId = null;
				listVue.projectId = null;
				listVue.tg_projectNameList = [];
			},
			getProjectId : function(data){
				listVue.projectId = data.tableId;
			},
			emptyProjectId : function(){
				listVue.projectId = null;
			},
			loadCity : loadCity,
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue,
			'vue-listselect': VueListSelect,
		},
		watch:{
			selectItem : selectedItemChanged,
		}
	});
	var updateVue = new Vue({
		el : baseInfo.updateDivId,
		data : {
			interfaceVersion :19000101,
			eCode:null,
			userStartId:null,
			userStartList:[],
			createTimeStamp:null,
			lastUpdateTimeStamp:null,
			userRecordId:null,
			userRecordList:[],
			recordTimeStamp:null,
			depositProperty:null,
			bankId:null,
			bankList:[],
			bankOfDeposit:null,
			escrowAcount:null,
			escrowAcountShortName:null,
			Agent:null,
			principalAmount:null,
			storagePeriod:null,
			storagePeriodCompany:null,
			annualRate:null,
			startDate:null,
			stopDate:null,
			openAccountCertificate:null,
			expectedInterest:null,
			floatAnnualRate:null,
			extractDate:null,
			realInterest:null,
			realInterestRate:null,
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateTg_DepositManagement
		}
	});
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			eCode:null,
			userStartId:null,
			userStartList:[],
			createTimeStamp:null,
			lastUpdateTimeStamp:null,
			userRecordId:null,
			userRecordList:[],
			recordTimeStamp:null,
			depositProperty:null,
			bankId:null,
			bankList:[],
			bankOfDeposit:null,
			escrowAcount:null,
			escrowAcountShortName:null,
			Agent:null,
			principalAmount:null,
			storagePeriod:null,
			storagePeriodCompany:null,
			annualRate:null,
			startDate:null,
			stopDate:null,
			openAccountCertificate:null,
			expectedInterest:null,
			floatAnnualRate:null,
			extractDate:null,
			realInterest:null,
			realInterestRate:null,
		},
		methods : {
			getAddForm : getAddForm,
			add : addTg_DepositManagement
		}
	});

	// ------------------------方法定义-开始------------------//
	
	//列表操作-----------------------页面加载显示区域
	function loadCity() {
		
		var model = 
		{
				interfaceVersion:this.interfaceVersion,
		}
		
		new ServerInterface(baseInfo.loadCityInterface).execute(model, function(jsonObj){
			if (jsonObj.result != "success") {
				$(baseInfo.edModelDivId).modal({
					backdrop : 'static'
				});
				listVue.errMsg = jsonObj.info;
			} else {
				listVue.tg_AreaList = jsonObj.sm_CityRegionInfoList;
			}
		});
	}
	 
	// 列表操作--------------获取"搜索列表"表单参数
	function getSearchForm()
	{
		if(this.paymentMethod == "") {
			return {
				interfaceVersion:this.interfaceVersion,
				pageNumber:this.pageNumber,
				countPerPage:this.countPerPage,
				totalPage:this.totalPage,
				keyword:this.keyword,
				userStartId:this.userStartId,
				userRecordId:this.userRecordId,
				bankId:this.bankId,
				cityRegionId : listVue.areaId,
				projectId : listVue.projectId,
			}
		}else {
			return {
				interfaceVersion:this.interfaceVersion,
				pageNumber:this.pageNumber,
				countPerPage:this.countPerPage,
				totalPage:this.totalPage,
				keyword:this.keyword,
				payWay:this.paymentMethod,
				userStartId:this.userStartId,
				userRecordId:this.userRecordId,
				bankId:this.bankId,
				cityRegionId : listVue.areaId,
				projectId : listVue.projectId,
			}
		}
	}
	// 列表操作--------------获取“删除资源”表单参数
	function getDeleteForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			idArr:this.selectItem
		}
	}
	// 列表操作--------------获取“新增”表单参数
	function getAddForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			eCode:this.eCode,
			userStartId:this.userStartId,
			createTimeStamp:this.createTimeStamp,
			lastUpdateTimeStamp:this.lastUpdateTimeStamp,
			userRecordId:this.userRecordId,
			recordTimeStamp:this.recordTimeStamp,
			depositProperty:this.depositProperty,
			bankId:this.bankId,
			bankOfDeposit:this.bankOfDeposit,
			escrowAcount:this.escrowAcount,
			escrowAcountShortName:this.escrowAcountShortName,
			Agent:this.Agent,
			principalAmount:this.principalAmount,
			storagePeriod:this.storagePeriod,
			storagePeriodCompany:this.storagePeriodCompany,
			annualRate:this.annualRate,
			startDate:this.startDate,
			stopDate:this.stopDate,
			openAccountCertificate:this.openAccountCertificate,
			expectedInterest:this.expectedInterest,
			floatAnnualRate:this.floatAnnualRate,
			extractDate:this.extractDate,
			realInterest:this.realInterest,
			realInterestRate:this.realInterestRate,
		}
	}
	// 列表操作--------------获取“更新”表单参数
	function getUpdateForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			eCode:this.eCode,
			userStartId:this.userStartId,
			createTimeStamp:this.createTimeStamp,
			lastUpdateTimeStamp:this.lastUpdateTimeStamp,
			userRecordId:this.userRecordId,
			recordTimeStamp:this.recordTimeStamp,
			depositProperty:this.depositProperty,
			bankId:this.bankId,
			bankOfDeposit:this.bankOfDeposit,
			escrowAcount:this.escrowAcount,
			escrowAcountShortName:this.escrowAcountShortName,
			Agent:this.Agent,
			principalAmount:this.principalAmount,
			storagePeriod:this.storagePeriod,
			storagePeriodCompany:this.storagePeriodCompany,
			annualRate:this.annualRate,
			startDate:this.startDate,
			stopDate:this.stopDate,
			openAccountCertificate:this.openAccountCertificate,
			expectedInterest:this.expectedInterest,
			floatAnnualRate:this.floatAnnualRate,
			extractDate:this.extractDate,
			realInterest:this.realInterest,
			realInterestRate:this.realInterestRate,
		}
	}
	function tg_DepositManagementDel()
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
	function tg_DepositManagementDelOne(tg_DepositManagementId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[tg_DepositManagementId],
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
	// 选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		listVue.isAllSelected = (listVue.Tg_DepositHouseholdsDtl_View.length > 0)
		&&	(listVue.selectItem.length == listVue.Tg_DepositHouseholdsDtl_View.length);
	}
	// 列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.Tg_DepositHouseholdsDtl_View.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];// 解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tg_DepositManagementList.forEach(function(item) {
	    		listVue.selectItem.push(item.tableId);
	    	});
	    }
	}
	// 列表操作--------------刷新
	function refresh()
	{
		new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				listVue.Tg_DepositHouseholdsDtl_View=jsonObj.tg_DepositHouseholdsDtl_ViewList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				// 动态跳转到锚点处，id="top"
				document.getElementById('tg_DepositManagementListDiv').scrollIntoView();
			}
		});
	}
	
	// 列表操作-----------------页面加载显示项目
	function loadProject(){
		var model = {
				interfaceVersion : this.interfaceVersion,
				exceptZhengTai : true,
		};
		
		new ServerInterface(baseInfo.loadProjectInterface).execute(model, function(jsonObj){
			if(jsonObj.result != "success")
			{
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				listVue.errMsg = jsonObj.info;
			}
			else
			{
				listVue.qs_projectNameList = jsonObj.empj_ProjectInfoList;
			}
		});
	}
	
	function exportExcelHandle() {

        new ServerInterface(baseInfo.exportExcelInterface).execute(listVue.getSearchForm(), function(jsonObj)
        {
            if (jsonObj.result != "success")
            {
            	generalErrorModal(jsonObj, jsonObj.info);
            }
            else
            {
            	window.location.href="../"+jsonObj.fileURL;
            }
        });
    }
	
	// 列表操作------------搜索
	function search()
	{
		listVue.pageNumber = 1;
		refresh();
	}
	
	// 弹出编辑模态框--更新操作
	function showAjaxModal(tg_DepositManagementModel)
	{
		// tg_DepositManagementModel数据库的日期类型参数，会导到网络请求失败
		// Vue.set(updateVue, 'tg_DepositManagement',
		// tg_DepositManagementModel);
		// updateVue.$set("tg_DepositManagement", tg_DepositManagementModel);
		
		updateVue.eCode = tg_DepositManagementModel.eCode;
		updateVue.userStartId = tg_DepositManagementModel.userStartId;
		updateVue.createTimeStamp = tg_DepositManagementModel.createTimeStamp;
		updateVue.lastUpdateTimeStamp = tg_DepositManagementModel.lastUpdateTimeStamp;
		updateVue.userRecordId = tg_DepositManagementModel.userRecordId;
		updateVue.recordTimeStamp = tg_DepositManagementModel.recordTimeStamp;
		updateVue.depositProperty = tg_DepositManagementModel.depositProperty;
		updateVue.bankId = tg_DepositManagementModel.bankId;
		updateVue.bankOfDeposit = tg_DepositManagementModel.bankOfDeposit;
		updateVue.escrowAcount = tg_DepositManagementModel.escrowAcount;
		updateVue.escrowAcountShortName = tg_DepositManagementModel.escrowAcountShortName;
		updateVue.Agent = tg_DepositManagementModel.Agent;
		updateVue.principalAmount = tg_DepositManagementModel.principalAmount;
		updateVue.storagePeriod = tg_DepositManagementModel.storagePeriod;
		updateVue.storagePeriodCompany = tg_DepositManagementModel.storagePeriodCompany;
		updateVue.annualRate = tg_DepositManagementModel.annualRate;
		updateVue.startDate = tg_DepositManagementModel.startDate;
		updateVue.stopDate = tg_DepositManagementModel.stopDate;
		updateVue.openAccountCertificate = tg_DepositManagementModel.openAccountCertificate;
		updateVue.expectedInterest = tg_DepositManagementModel.expectedInterest;
		updateVue.floatAnnualRate = tg_DepositManagementModel.floatAnnualRate;
		updateVue.extractDate = tg_DepositManagementModel.extractDate;
		updateVue.realInterest = tg_DepositManagementModel.realInterest;
		updateVue.realInterestRate = tg_DepositManagementModel.realInterestRate;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	
	function updateTg_DepositManagement()
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
	
	function addTg_DepositManagement()
	{
		new ServerInterface(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				$(baseInfo.addDivId).modal('hide');
				addVue.eCode = null;
				addVue.userStartId = null;
				addVue.createTimeStamp = null;
				addVue.lastUpdateTimeStamp = null;
				addVue.userRecordId = null;
				addVue.recordTimeStamp = null;
				addVue.depositProperty = null;
				addVue.bankId = null;
				addVue.bankOfDeposit = null;
				addVue.escrowAcount = null;
				addVue.escrowAcountShortName = null;
				addVue.Agent = null;
				addVue.principalAmount = null;
				addVue.storagePeriod = null;
				addVue.storagePeriodCompany = null;
				addVue.annualRate = null;
				addVue.startDate = null;
				addVue.stopDate = null;
				addVue.openAccountCertificate = null;
				addVue.expectedInterest = null;
				addVue.floatAnnualRate = null;
				addVue.extractDate = null;
				addVue.realInterest = null;
				addVue.realInterestRate = null;
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
	
	function handleReset()
	{
		listVue.keyword = "";
		listVue.areaId = "";
		listVue.projectId = "";
		listVue.paymentMethod = "" ;
		generalResetSearch(listVue, function () {
            refresh()
        })
	}

	function initData()
	{
		
	}
	// ------------------------方法定义-结束------------------//
	
	// ------------------------数据初始化-开始----------------//
	listVue.loadCity();
	listVue.refresh();
	listVue.initData();
	// ------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tg_DepositManagementListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Tg_DepositHouseholdsDtl_ViewList",
	"loadProjectInterface" : "../Empj_ProjectInfoList", // 改变企业名称接口
	"loadCityInterface" : "../Sm_CityRegionInfoList", //区域
	"addInterface":"../Tg_DepositManagementAdd",
	"deleteInterface":"../Tg_DepositManagementDelete",
	"updateInterface":"../Tg_DepositManagementUpdate",
	"exportExcelInterface":"../Tg_DepostitHouseHoldsDtl_ViewExportExcel",
});
