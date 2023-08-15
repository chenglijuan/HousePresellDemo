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
			billTimeStamp : "", //记账日期开始
			endBillTimeStamp : "", //记账日期结束
			keyword : "", //关键字
			dateRange : "", //日期区间
			trusteeshipBuildingReportsList : [],
			bankBranchList : [], //开户行
			bankBranchId : null, //开户行Id
			projectId : "", //项目名称
			companyId : "", //开发企业
			qs_companyNameList : [], //页面加载显示开发企业
			qs_projectNameList : [], //显示项目名称
		},
		methods : {
			refresh : refresh,
			initData : initData,
			getSearchForm : getSearchForm,
			getExportForm : getExportForm, //获取导出excel参数
			search : search,
			loadCompanyName : loadCompanyNameFun, //页面加载显示开发企业方法
			changeCompanyHandle : changeCompanyHandleFun, //改变企业名称的方法
			changePageNumber : function(data) {
				if (listVue.pageNumber != data) {
					listVue.pageNumber = data;
					listVue.refresh();
				}
			},
			changeCountPerPage : function(data) {
				if (listVue.countPerPage != data) {
					listVue.countPerPage = data;
					listVue.refresh();
				}
			},
			indexMethod : indexMethod,
			resetSearch : resetSearch, //重置
			exportForm : exportForm, //导出excel
			getBankBranchList : getBankBranchList, //预加载显示开户行
			getBankBranchId : function(data) {
				listVue.bankBranchId = data.tableId;
				if (listVue.billTimeStamp != "") {
					listVue.dateRange = listVue.billTimeStamp + " ~ " + listVue.endBillTimeStamp;
				}
			},
			emptyBankBranchId : function(data) {
				listVue.bankBranchId = null;
			},
			getCompanyId: function(data) {
				listVue.companyId = data.tableId;
				listVue.changeCompanyHandle();
			},
			getProjectId: function(data) {
				listVue.projectId = data.tableId;
			},
			emptyCompanyId : function(data){
				listVue.companyId = null;
				listVue.projectId = null;
				listVue.qs_projectNameList = [];
			},
			emptyProjectId : function(data){
				listVue.projectId = null;
			}
		},
		computed : {

		},
		components : {
			'vue-nav' : PageNavigationVue,
			'vue-select' : VueSelect,
			'vue-listselect' : VueListSelect,
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
			keyword : listVue.keyword,
			billTimeStamp : this.billTimeStamp,
			endBillTimeStamp : this.endBillTimeStamp,
			bankBranchId : listVue.bankBranchId,
			projectId : listVue.projectId,
			companyId : listVue.companyId,
		}
	}
	
	//列表操作--------------------改变开发企业的方法
	function changeCompanyHandleFun() {
		changeCompanyHandle(listVue,baseInfo.changeCompanyInterface,function(jsonObj){
			listVue.qs_projectNameList = jsonObj.empj_ProjectInfoList;
		},listVue.errMsg,baseInfo.edModelDivId)
	}

	function indexMethod(index) {
		return generalIndexMethod(index, listVue)
	}

	function getBankBranchList() {
		var model = {
			interfaceVersion : listVue.interfaceVersion,
			theState : 0
		};

		new ServerInterface(baseInfo.bankBranchListInterface).execute(model, function(jsonObj) {
			if (jsonObj.result != "success") {

			} else {
				listVue.bankBranchList = jsonObj.emmp_BankBranchList;
			}
		});
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
				listVue.trusteeshipBuildingReportsList = jsonObj.tgpf_DepositDetailList;
				listVue.trusteeshipBuildingReportsList.forEach(function(item) {
					item.contractAmount = addThousands(item.contractAmount);
					item.loanAmount = addThousands(item.loanAmount);
					item.loanAmountFromBank = addThousands(item.loanAmountFromBank);
				})
				listVue.pageNumber = jsonObj.pageNumber;
				listVue.countPerPage = jsonObj.countPerPage;
				listVue.totalPage = jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword = listVue.keyword;
				if (listVue.billTimeStamp != "") {
					listVue.dateRange = listVue.billTimeStamp + " ~ " + listVue.endBillTimeStamp;
				}
				listVue.bankBranchId = listVue.bankBranchId;
				listVue.selectedItem = [];
				//动态跳转到锚点处，id="top"
				document.getElementById('tripleAgreementEnterAccountDiv').scrollIntoView();
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
		listVue.billTimeStamp = "";
		listVue.endBillTimeStamp = "";
		listVue.dateRange = "";
		listVue.keyword = "";
		listVue.bankBranchId = "";
		listVue.projectId = ""; //项目名称
		listVue.companyId = ""; //开发企业
	}
	//------------------------方法定义-结束------------------//

	/**
	 * 初始化日期插件记账日期开始
	 */
	laydate.render({
		elem : '#tripleAgreementEnterAccountSaerchStart',
		range : '~',
		done : function(value, date, endDate) {
			var arr = value.split("~");
			listVue.billTimeStamp = arr[0].trim();
			listVue.endBillTimeStamp = arr[1].trim();
		}
	});
	//------------------------数据初始化-开始----------------//
	listVue.getBankBranchList();
	listVue.loadCompanyName();
	listVue.refresh();
	listVue.initData();
//------------------------数据初始化-结束----------------//
})({
	"listDivId" : "#tripleAgreementEnterAccountDiv",
	"edModelDivId" : "#edModel",
	"sdModelDiveId" : "#sdModel",
	"listInterface" : "../Tg_TripleOfDepositDetail",
	"companyNameInterface" : "../Emmp_CompanyInfoList", //显示开发企业名称接口
	"changeCompanyInterface" : "../Empj_ProjectInfoList", //改变企业名称接口
	"bankBranchListInterface" : "../Emmp_BankBranchList",
	"exportInterface" : "../Tg_TripleOfDepositDetailExportExcel", //导出excel接口
});