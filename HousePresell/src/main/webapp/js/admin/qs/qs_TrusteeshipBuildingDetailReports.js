(function(baseInfo) {
	var listVue = new Vue({
		el : baseInfo.listDivId,
		data : {
			interfaceVersion : 19000101,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			theState : 0, //正常为0，删除为1
			userStartId : null,
			userStartList : [],
			userRecordId : null,
			userRecordList : [],
			mainTableId : null,
			mainTableList : [],
			errMsg : "",
			keyword : "",
			billTimeStamp : "", //记账日期
			projectId : "", //项目名称
			companyId : "", //开发企业
			buildId : "", //楼幢号
			endBillTimeStamp : "",
			dateRange : "",
			TrusteeshipBuildingDetailReportsList : [],
			paymentMethod : null, //付款方式
			escrowState : null,//户托管状态
			//qs_paymentMethodList : [], //页面加载付款方式list
			//depositState : "", //户托管状态
			//qs_depositStateList : [], //页面加载显示户托管状态list
			qs_companyNameList : [], //页面加载显示开发企业
			qs_projectNameList : [], //显示项目名称
			qs_buildingNumberlist : [], //显示楼幢编号
			fileUrl : "",
			paymentMethodList : [
				{tableId:"1",theName:"一次性付款"},
				{tableId:"2",theName:"分期付款"},
				{tableId:"3",theName:"贷款方式付款"},
				{tableId:"4",theName:"其他"},
			],
			escrowStateList : [
				{tableId:"0",theName:"未托管"},
				{tableId:"1",theName:"已托管"},
				{tableId:"2",theName:"申请托管终止"},
				{tableId:"3",theName:"托管终止"},
			]
		},
		methods : {
			refresh : refresh,
			initData : initData,
			getSearchForm : getSearchForm,
			getExportForm : getExportForm, //获取导出excel参数
			search : search,
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
			resetSearch : resetSearch, //重置
			exportForm : exportForm, //导出excel
			loadForm : loadForm, //页面加载显示查询条件传递参数
			//loadPayoutMethod : loadPayoutMethod, //页面加载显示付款方式方法
			//loadDepositState : loadDepositState, //页面加载显示户托管状态方法
			loadCompanyName : loadCompanyNameFun, //页面加载显示开发企业方法
			changeCompanyHandle : changeCompanyHandleFun, //改变企业名称的方法
			changeProjectHandle : changeProjectHandleFun, //改变项目名称的方法\
			exportExcel : exportExcel,
			getCompanyId: function(data) {
				listVue.projectId = null;
				listVue.buildId = null;
				listVue.qs_projectNameList = [];
				listVue.qs_buildingNumberlist = [];
				listVue.companyId = data.tableId;
				listVue.changeCompanyHandle();
			},
			emptyCompanyId : function(data){
				listVue.companyId = null;
				listVue.projectId = null;
				listVue.buildId = null;
				listVue.qs_projectNameList = [];
				listVue.qs_buildingNumberlist = [];
			},
			getProjectId: function(data) {
				listVue.buildId = null;
				listVue.qs_buildingNumberlist = [];
				listVue.projectId = data.tableId;
				listVue.changeProjectHandle();
			},
			emptyProjectId : function(data){
				listVue.projectId = null;
				listVue.buildId = null;
				listVue.qs_buildingNumberlist = [];
			},
			getBuildId: function(data) {
				listVue.buildId = data.tableId;
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
			getEscrowState : function(data){
				listVue.escrowState = data.tableId;
			},
			emptyEscrowState : function(){
				listVue.escrowState = null;
			}
			/*getPaymentMethod: function(data) {
				listVue.paymentMethod = data.tableId;
			},
			getEscrowState: function(data) {
				listVue.escrowState = data.tableId;
			}*/
		},
		computed : {

		},
		components : {
			'vue-nav' : PageNavigationVue,
			'vue-select': VueSelect,
			'vue-listselect': VueListSelect,
		},
		watch : {
		}
	});

	//------------------------方法定义-开始------------------//

	//列表操作--------------获取"搜索列表"表单参数
	function getSearchForm() {
		return {
			interfaceVersion : this.interfaceVersion,
			pageNumber : this.pageNumber,
			countPerPage : this.countPerPage,
			totalPage : this.totalPage,
			keyword : this.keyword,
/*			theState : this.theState,
			userStartId : this.userStartId,
			userRecordId : this.userRecordId,
			mainTableId : this.mainTableId,*/		
			escrowState : this.escrowState,
			paymentMethod : this.paymentMethod,
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
	function exportExcel(){
		window.location.href="../"+listVue.fileUrl;
	}
	
	//列表操作-------------------页面加载显示查询条件传递参数
	function loadForm() {
		return {
			interfaceVersion : this.interfaceVersion,
		}
	}
	//列表操作-------------------页面加载导出excel参数
	function getExportForm() {
		return {
			interfaceVersion : this.interfaceVersion,
		}
	}

	/*//列表操作---------------------页面加载显示付款方式
	function loadPayoutMethod() {
		new ServerInterface(baseInfo.payoutMethodInterface).execute(listVue.loadForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
				$(baseInfo.edModelDivId).modal({
					backdrop : 'static'
				});
				listVue.errMsg = jsonObj.info;
			} else {
				listVue.qs_paymentMethodList = jsonObj.qs_paymentMethodList;
			}
		});
	}*/

	/*//列表操作---------------------页面加载显示户托管状态方法
	function loadDepositState() {
		new ServerInterface(baseInfo.depositStateInterface).execute(listVue.loadForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
				$(baseInfo.edModelDivId).modal({
					backdrop : 'static'
				});
				listVue.errMsg = jsonObj.info;
			} else {
				listVue.qs_depositStateList = jsonObj.qs_depositStateList;
			}
		});
	}*/

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
	//列表操作--------------刷新
	function refresh() {
		new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
				$(baseInfo.edModelDivId).modal({
					backdrop : 'static'
				});
				listVue.errMsg = jsonObj.info;
			} else {
				listVue.TrusteeshipBuildingDetailReportsList = jsonObj.tg_Build_ViewList;
				listVue.TrusteeshipBuildingDetailReportsList.forEach(function(item) {
					item.contractSumPrice = addThousands(item.contractSumPrice);
					item.firstPaymentAmount = addThousands(item.firstPaymentAmount);
					item.loanAmount = addThousands(item.loanAmount);
					item.loanAmountIn = addThousands(item.loanAmountIn);
					item.theAmountOfRetainedequity = addThousands(item.theAmountOfRetainedequity);
				})
				listVue.pageNumber = jsonObj.pageNumber;
				listVue.countPerPage = jsonObj.countPerPage;
				listVue.totalPage = jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword = jsonObj.keyword;
				listVue.selectedItem = [];
				//动态跳转到锚点处，id="top"
				document.getElementById('trusteeshipBuildingDetailReportsDiv').scrollIntoView();
			}
		});
	}
	//列表操作-----------------------导出excel
	function exportForm() {
		new ServerInterface(baseInfo.exportInterface).execute(listVue.getSearchForm(), function(jsonObj) {
			if (jsonObj.result != "success") 
			{
				generalErrorModal(jsonObj, jsonObj.info);
			} 
			else
			{
				listVue.fileUrl = jsonObj.fileURL;
				exportExcel();
			}
		});
	}


	//列表操作------------搜索
	function search() {
		listVue.pageNumber = 1;
		refresh();
	}
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}
	function initData() {
		initButtonList();
	}
	function resetSearch() {
		listVue.dateRange = "";
		listVue.keyword = "";
		listVue.companyId = "";
		listVue.projectId = "";
		listVue.buildId = "";
		listVue.paymentMethod = "";
		listVue.escrowState = "";
		listVue.billTimeStamp = "";
		listVue.endBillTimeStamp = "";
		listVue.qs_projectNameList = [];
		generalResetSearch(listVue, function () {
            refresh()
        })
	}
	//------------------------方法定义-结束------------------//
	/**
	 * 初始化日期插件记账日期开始
	 */
	laydate.render({
		elem : '#accountDateSearchStart',
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
	//listVue.loadPayoutMethod();
	//listVue.loadDepositState();
	listVue.refresh();
	listVue.initData();
//------------------------数据初始化-结束----------------//
})({
	"listDivId" : "#trusteeshipBuildingDetailReportsDiv",
	"edModelDivId" : "#edModel",
	"sdModelDiveId" : "#sdModel",
	"exportExcelId" : "#exportExcel",
	//"payoutMethodInterface" : "", //显示付款方式接口
	//"depositStateInterface" : "", //显示托管状态接口
	"companyNameInterface" : "../Emmp_CompanyInfoForSelect", //显示开发企业名称接口
	"changeCompanyInterface" : "../Empj_ProjectInfoList", //改变企业名称接口
	"changeProjectInterface" : "../Empj_BuildingInfoList", //改变项目名称接口
	"listInterface" : "../Tg_Build_ViewList",
	"exportInterface" : "../Tg_Build_ViewExportExcel", //导出excel接口
});