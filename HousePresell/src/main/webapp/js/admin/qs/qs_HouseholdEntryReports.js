(function(baseInfo){
	var listVue = new Vue({
		el : baseInfo.listDivId,
		data : {
			interfaceVersion :19000101,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			theState:0,//正常为0，删除为1
			userStartId:null,
			userStartList:[],
			userRecordId:null,
			userRecordList:[],
			mainTableId:null,
			mainTableList:[],
			errMsg : "",
			billTimeStamp : "",//记账日期
			endBillTimeStamp : "",
			keyword : "",
            projectId : "", //项目名称
			companyId : "", //开发企业
			buildId : "", //楼幢号
			qs_companyNameList : [], //页面加载显示开发企业
			qs_projectNameList : [], //显示项目名称
			qs_buildingNumberlist : [], //显示楼幢编号
			householdEntryReportsList : [],
			paymentMethod : "", //付款方式
			qs_paymentMethodList : [], //页面加载付款方式list
			fundProperty : "",//资金性质
			qs_fundPropertyList : [],//页面加载显示资金性质list
			dateRange : "",//时间区间
			buyerName : "",//买受人名称
			paymentMethodList : [
				{tableId:"1",theName:"一次性付款"},
				{tableId:"2",theName:"分期付款"},
				{tableId:"3",theName:"贷款方式付款"},
				{tableId:"4",theName:"其它方式"},
			],
			fundPropertyList : [
				{tableId:"0",theName:"自有资金"},
				{tableId:"1",theName:"商业贷款"},
				{tableId:"2",theName:"公积金贷款"},
				{tableId:"3",theName:"公转商贷款"},
				{tableId:"4",theName:"公积金首付款"},
			]
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			getExportForm : getExportForm,//获取导出excel参数
			search:search,
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
			indexMethod : indexMethod,
			resetSearch : resetSearch,//重置
			exportForm : exportForm,//导出excel
			loadPayoutMethod : loadPayoutMethod, //页面加载显示付款方式方法
			loadCompanyName : loadCompanyNameFun, //页面加载显示开发企业方法
			changeCompanyHandle : changeCompanyHandleFun, //改变企业名称的方法
			changeProjectHandle : changeProjectHandleFun, //改变项目名称的方法
			loadFundProperty : loadFundProperty,//页面加载显示资金性质
			getCompanyId: function(data) {
				listVue.projectId = null;
				listVue.buildId = null;
				listVue.qs_projectNameList = [];
				listVue.qs_buildingNumberlist = [];
				listVue.companyId = data.tableId;
				listVue.changeCompanyHandle();
			},
			getProjectId: function(data) {
				listVue.buildId = null;
				listVue.qs_buildingNumberlist = [];
				listVue.projectId = data.tableId;
				listVue.changeProjectHandle();
			},
			getBuildId: function(data) {
				listVue.buildId = data.tableId;
			},
			emptyCompanyId : function(data){
				listVue.companyId = null;
				listVue.projectId = null;
				listVue.buildId = null;
				listVue.qs_projectNameList = [];
				listVue.qs_buildingNumberlist = [];
			},
			emptyProjectId : function(data){
				listVue.projectId = null;
				listVue.buildId = null;
				listVue.qs_buildingNumberlist = [];
			},
			emptyBuildId : function(data){
				listVue.buildId = null;
			},
			getPaymentMethod : function(data){
				listVue.paymentMethod = data.tableId;
			},
			emptyPaymentMethod : function(){
				listVue.paymentMethod = null;
			},
			getFundProperty : function(data){
				listVue.fundProperty = data.tableId;
			},
			emptyFundProperty : function(){
				listVue.fundProperty = null;
			},
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue,
			'vue-select': VueSelect,
			'vue-listselect': VueListSelect,
		},
		watch:{
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
			paymentMethod :this.paymentMethod,
			fundProperty :this.fundProperty,
			userRecordId:this.userRecordId,
			mainTableId:this.mainTableId,
			reconciliationState : this.reconciliationState,
			billTimeStamp : this.billTimeStamp,
			endBillTimeStamp : this.endBillTimeStamp,
			projectId : this.projectId,
			companyId : this.companyId,
			buildId : this.buildId
		}
	}

	function indexMethod(index) {
		return generalIndexMethod(index, listVue)
	}

	//列表操作--------------刷新
	function refresh()
	{
		new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				listVue.errMsg = jsonObj.info;
			}
			else
			{
				listVue.householdEntryReportsList=jsonObj.tg_HouseLoanAmount_ViewList;
				listVue.householdEntryReportsList.forEach(function(item){
					item.contractSumPrice = addThousands(item.contractSumPrice);
					item.loanAmount = addThousands(item.loanAmount);
					item.loanAmountIn = addThousands(item.loanAmountIn);
				})
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount=jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('householdEntryReportsDiv').scrollIntoView();
			}
		});
	}
	
	function getExportForm(){
		return{
		   interfaceVersion:this.interfaceVersion,	
		}
	}
	//列表操作---------------------页面加载显示付款方式
	function loadPayoutMethod() {
		var loadForm = {
			 interfaceVersion:this.interfaceVersion,	
		}
		new ServerInterface(baseInfo.payoutMethodInterface).execute(loadForm, function(jsonObj) {
			if (jsonObj.result != "success") {
				$(baseInfo.edModelDivId).modal({
					backdrop : 'static'
				});
				listVue.errMsg = jsonObj.info;
			} else {
				listVue.qs_paymentMethodList = jsonObj.qs_paymentMethodList;
			}
		});
	}
	//列表加载------------------------页面加载显示资金性质
	function loadFundProperty(){
		var loadForm = {
			 interfaceVersion:this.interfaceVersion,	
		}
		new ServerInterface(baseInfo.fundPropertyInterface).execute(loadForm, function(jsonObj) {
			if (jsonObj.result != "success") {
				$(baseInfo.edModelDivId).modal({
					backdrop : 'static'
				});
				listVue.errMsg = jsonObj.info;
			} else {
				listVue.qs_fundPropertyList = jsonObj.qs_fundPropertyList;
			}
		});
	}
	
	//列表操作-----------------------页面加载显示开发企业
	function loadCompanyNameFun() {
		var model = {
				interfaceVersion : 19000101,
				exceptZhengTai : true,
			};
		new ServerInterface(baseInfo.companyNameInterface).execute(model, function(jsonObj)
				{
					if(jsonObj.result != "success")
					{
						generalErrorModal(jsonObj);
					}
					else
					{
						listVue.qs_companyNameList = jsonObj.emmp_CompanyInfoList;
						
						if(null != listVue.qs_companyNameList && listVue.qs_companyNameList.length == 1)
						{
							listVue.companyId  =  listVue.qs_companyNameList[0].tableId;
						}
					}
				});
		
		/*loadCompanyName(listVue,baseInfo.companyNameInterface,function(jsonObj){
			listVue.qs_companyNameList = jsonObj.emmp_CompanyInfoList;
		},listVue.errMsg,baseInfo.edModelDivId);*/
	}
	//列表操作--------------------改变开发企业的方法
	function changeCompanyHandleFun() {
		changeCompanyHandle(listVue,baseInfo.changeCompanyInterface,function(jsonObj){
			listVue.qs_projectNameList = jsonObj.empj_ProjectInfoList;
		},listVue.errMsg,baseInfo.edModelDivId)
	}
	//列表操作--------------------改变项目名称的方法
	function changeProjectHandleFun() {
		changeProjectHandle(listVue,baseInfo.changeProjectInterface,function(jsonObj){
			listVue.qs_buildingNumberlist = jsonObj.empj_BuildingInfoList;
		},listVue.errMsg,baseInfo.edModelDivId)
	}
	//列表操作-----------------------导出excel
	function exportForm(){
		new ServerInterface(baseInfo.exportInterface).execute(listVue.getSearchForm(), function(jsonObj){
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
	
	
	//列表操作------------搜索
	function search()
	{
		listVue.pageNumber = 1;
		refresh();
	}
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}
	function initData()
	{
		initButtonList();
	}
	
	//列表操作------------------重置
	function resetSearch(){
		
		
		listVue.dateRange = "";
		listVue.keyword = "";
		listVue.companyId = "";
		listVue.projectId = "";
		listVue.buildId = "";
		listVue.paymentMethod = null;
		listVue.fundProperty = null;
		listVue.billTimeStamp = "";
		listVue.endBillTimeStamp = "";
		generalResetSearch(listVue, function () {
            refresh()
        })
	}
	//------------------------方法定义-结束------------------//
	/**
	 * 初始化日期插件记账日期开始
	 */
	laydate.render({
	  elem: '#householdEntrySaerchStart',
	  range: '~',
	  done: function(value, date, endDate){
	  	var arr = value.split("~");
	  	listVue.dateRange = value;
	    listVue.billTimeStamp = arr[0];
	    listVue.endBillTimeStamp = arr[1];
	  }
	});
	//------------------------数据初始化-开始----------------//
	listVue.loadCompanyName();
	listVue.loadFundProperty();
	listVue.loadPayoutMethod();
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#householdEntryReportsDiv",
	"edModelDivId":"#edModel",
	"sdModelDiveId":"#sdModel",
	"companyNameInterface" : "../Emmp_CompanyInfoList", //显示开发企业名称接口
	"changeCompanyInterface" : "../Empj_ProjectInfoList", //改变企业名称接口
	"changeProjectInterface" : "../Empj_BuildingInfoList", //改变项目名称接口
	"payoutMethodInterface" : "",//显示付款方式接口
	"fundPropertyInterface" : "",//显示资金性质接口
	"listInterface":"../Tg_HouseLoanAmount_ViewList",
	"exportInterface":"../Tg_HouseLoanAmount_ViewExportExcel",//导出excel接口
});
