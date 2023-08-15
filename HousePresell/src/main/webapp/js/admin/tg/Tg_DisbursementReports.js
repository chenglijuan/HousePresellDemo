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
			payoutDate : "", //拨付日期（开始）
			endPayoutDate : "", //拨付日期（结束）
			disbursementReportsList : [],
			keyword : "", //关键字
			projectId : "", //项目名称
			companyId : "", //开发企业
			buildId : "", //楼幢号
			tg_companyNameList : [], //页面加载显示开发企业
			tg_projectNameList : [], //显示项目名称
			tg_buildingNumberlist : [], //显示楼幢编号
		},
		methods : {
			refresh : refresh,
			initData : initData,
			getSearchForm : getSearchForm,
			getExportForm : getExportForm, //获取导出excel参数
			search : search,
			changePageNumber : function(data) {
				listVue.pageNumber = data;
			},
			indexMethod : indexMethod,
			resetSearch : resetSearch, //重置
			exportForm : exportForm, //导出excel
			loadCompanyName : loadCompanyNameFun, //页面加载显示开发企业方法
			changeCompanyHandle : changeCompanyHandleFun, //改变企业名称的方法
			changeProjectHandle : changeProjectHandleFun, //改变项目名称的方法
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
			reconciliationState : this.reconciliationState,
			payoutDate : this.payoutDate,
			endPayoutDate : this.endPayoutDate,
			projectId : this.projectId,
			companyId : this.companyId,
			buildId : this.buildId
		}
	}

	function indexMethod(index) {
		return generalIndexMethod(index, listVue)
	}
	//列表操作-------------------页面加载导出excel参数
	function getExportForm() {
		return {
			interfaceVersion : this.interfaceVersion,
		}
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
				listVue.disbursementReportsList = jsonObj.tg_BuildPayout_ViewList;
				listVue.disbursementReportsList.forEach(function(item) {
					item.currentApplyPayoutAmount = addThousands(item.currentApplyPayoutAmount);
					item.currentPayoutAmount = addThousands(item.currentPayoutAmount);
				})
				listVue.pageNumber = jsonObj.pageNumber;
				listVue.countPerPage = jsonObj.countPerPage;
				listVue.totalPage = jsonObj.totalPage;
				listVue.keyword = jsonObj.keyword;
				listVue.selectedItem = [];
				//动态跳转到锚点处，id="top"
				document.getElementById('disbursementReportsDiv').scrollIntoView();
			}
		});
	}

	//列表操作-----------------------页面加载显示开发企业
	function loadCompanyNameFun() {
		loadCompanyName(listVue, baseInfo.companyNameInterface, function(jsonObj) {
			listVue.tg_companyNameList = jsonObj.emmp_CompanyInfoList;
		}, listVue.errMsg, baseInfo.edModelDivId);
	}
	//列表操作--------------------改变开发企业的方法
	function changeCompanyHandleFun() {
		changeCompanyHandle(listVue, baseInfo.changeCompanyInterface, function(jsonObj) {
			listVue.tg_projectNameList = jsonObj.empj_ProjectInfoList;
		}, listVue.errMsg, baseInfo.edModelDivId)
	}
	//列表操作--------------------改变项目名称的方法
	function changeProjectHandleFun() {
		changeProjectHandle(listVue, baseInfo.changeProjectInterface, function(jsonObj) {
			listVue.tg_buildingNumberlist = jsonObj.empj_BuildingInfoList;
		}, listVue.errMsg, baseInfo.edModelDivId)
	}
	//列表操作-----------------------导出excel
	function exportForm() {
		new ServerInterface(baseInfo.exportInterface).execute(listVue.getSearchForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
				alert("123");
				$(baseInfo.edModelDivId).modal({
					backdrop : 'static'
				});
				listVue.errMsg = jsonObj.info;
			} else {
				alert(jsonObj.fileURL);
				window.location.href = "../" + jsonObj.fileURL;
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
		listVue.payoutDate = getNowFormatDate();
		listVue.endPayoutDate = getNowFormatDate();
		listVue.projectName = "";
		listVue.companyName = "";
		listVue.eCodeFroMconstruction = "";
	}
	//------------------------方法定义-结束------------------//

	/**
	 * 初始化日期插件记账日期开始
	 */
	laydate.render({
		elem : '#disbursementSaerchStart',
		done : function(value, date, endDate) {
			listVue.payoutDate = value;
		}
	});
	/**
	 * 初始化日期插件记账日期结束
	 */
	laydate.render({
		elem : '#disbursementSaerchEnd',
		done : function(value, date, endDate) {
			listVue.endPayoutDate = value;
		}
	});
	//------------------------数据初始化-开始----------------//
	listVue.loadCompanyName();
	listVue.refresh();
	listVue.initData();
//------------------------数据初始化-结束----------------//
})({
	"listDivId" : "#disbursementReportsDiv",
	"edModelDivId" : "#edModel",
	"sdModelDiveId" : "#sdModel",
	"companyNameInterface" : "../Emmp_CompanyInfoList", //显示开发企业名称接口
	"changeCompanyInterface" : "../Empj_ProjectInfoList", //改变企业名称接口
	"changeProjectInterface" : "../Empj_BuildingInfoList", //改变项目名称接口
	"listInterface" : "../Tg_BuildPayout_ViewList",
	"exportInterface" : "../Tg_BuildPayout_ViewExportExcel", //导出excel接口
});