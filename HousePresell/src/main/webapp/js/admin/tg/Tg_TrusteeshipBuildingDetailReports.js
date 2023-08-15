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
			paymentMethod : "", //付款方式
			escrowState : "",//户托管状态
			tg_paymentMethodList : [], //页面加载付款方式list
			depositState : "", //户托管状态
			tg_depositStateList : [], //页面加载显示户托管状态list
			tg_companyNameList : [], //页面加载显示开发企业
			tg_projectNameList : [], //显示项目名称
			tg_buildingNumberlist : [], //显示楼幢编号
			fileUrl : "",
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
			loadPayoutMethod : loadPayoutMethod, //页面加载显示付款方式方法
			loadDepositState : loadDepositState, //页面加载显示户托管状态方法
			loadCompanyName : loadCompanyNameFun, //页面加载显示开发企业方法
			changeCompanyHandle : changeCompanyHandleFun, //改变企业名称的方法
			changeProjectHandle : changeProjectHandleFun, //改变项目名称的方法\
			exportExcel : exportExcel,
		},
		computed : {

		},
		components : {
			'vue-nav' : PageNavigationVue
		},
		watch : {
//			pageNumber : refresh,
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
			theState : this.theState,
			userStartId : this.userStartId,
			userRecordId : this.userRecordId,
			mainTableId : this.mainTableId,
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

	//列表操作---------------------页面加载显示付款方式
	function loadPayoutMethod() {
		new ServerInterface(baseInfo.payoutMethodInterface).execute(listVue.loadForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
				$(baseInfo.edModelDivId).modal({
					backdrop : 'static'
				});
				listVue.errMsg = jsonObj.info;
			} else {
				listVue.tg_paymentMethodList = jsonObj.qs_paymentMethodList;
			}
		});
	}

	//列表操作---------------------页面加载显示户托管状态方法
	function loadDepositState() {
		new ServerInterface(baseInfo.depositStateInterface).execute(listVue.loadForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
				$(baseInfo.edModelDivId).modal({
					backdrop : 'static'
				});
				listVue.errMsg = jsonObj.info;
			} else {
				listVue.tg_depositStateList = jsonObj.qs_depositStateList;
			}
		});
	}

	//列表操作-----------------------页面加载显示开发企业
	function loadCompanyNameFun() {
		loadCompanyName(listVue,baseInfo.companyNameInterface,function(jsonObj){
			listVue.tg_companyNameList = jsonObj.emmp_CompanyInfoList;
		},listVue.errMsg,baseInfo.edModelDivId);
	}
	//列表操作--------------------改变开发企业的方法
	function changeCompanyHandleFun() {
		changeCompanyHandle(listVue,baseInfo.changeCompanyInterface,function(jsonObj){
			listVue.tg_projectNameList = jsonObj.empj_ProjectInfoList;
		},listVue.errMsg,baseInfo.edModelDivId)
	}
	//列表操作--------------------改变项目名称的方法
	function changeProjectHandleFun() {
		changeProjectHandle(listVue,baseInfo.changeProjectInterface,function(jsonObj){
			listVue.tg_buildingNumberlist = jsonObj.empj_BuildingInfoList;
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
			if (jsonObj.result != "success") {
				$(baseInfo.edModelDivId).modal({
					backdrop : 'static'
				});
				listVue.errMsg = jsonObj.info;
			} else {
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

	function initData() {
	}
	function resetSearch() {
		listVue.dateRange = "";
		listVue.keyword = "";
		listVue.companyId = "";
		listVue.projectId = "";
		listVue.buildId = "";
		listVue.paymentMethod = "";
		listVue.escrowState = "";
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
		    listVue.billTimeStamp = arr[0];
		    listVue.endBillTimeStamp = arr[1];
		}
	});
	//------------------------数据初始化-开始----------------//
	listVue.loadCompanyName();
	listVue.loadPayoutMethod();
	listVue.loadDepositState();
	listVue.refresh();
	listVue.initData();
//------------------------数据初始化-结束----------------//
})({
	"listDivId" : "#trusteeshipBuildingDetailReportsDiv",
	"edModelDivId" : "#edModel",
	"sdModelDiveId" : "#sdModel",
	"exportExcelId" : "#exportExcel",
	"payoutMethodInterface" : "", //显示付款方式接口
	"depositStateInterface" : "", //显示托管状态接口
	"companyNameInterface" : "../Emmp_CompanyInfoList", //显示开发企业名称接口
	"changeCompanyInterface" : "../Empj_ProjectInfoList", //改变企业名称接口
	"changeProjectInterface" : "../Empj_BuildingInfoList", //改变项目名称接口
	"listInterface" : "../Tg_Build_ViewList",
	"exportInterface" : "../Tg_Build_ViewExportExcel", //导出excel接口
});