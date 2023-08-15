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
			agencyCompanyId : "", //开发企业
			reasonId : "", //楼幢号
			endBillTimeStamp : "",
			dateRange : "",
			TripartiteAgreementEvaluationList : [],
			agencyCompanyList : [],//代理公司列表
			reasonList : [],//退回原因
			fileUrl : "",
		},
		methods : {
			refresh : refresh,
			initData : initData,
			getSearchForm : getSearchForm,
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
			loadAgencyCompany : loadAgencyCompany,
			loadReason : loadReason,
			changeAgencyCompany : function(data){
				this.agencyCompanyId = data.tableId;
			},
			agencyCompanyEmpty : function(){
				this.agencyCompanyId = null;
			},
			changeReason : function(data){
				this.reasonId = data.tableId;
			},
			reasonEmpty : function(){
				this.reasonId = null
			}
		},
		computed : {

		},
		components : {
			'vue-nav' : PageNavigationVue,
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
			keyword : this.keyword,
			theState : this.theState,
			userStartId : this.userStartId,
			userRecordId : this.userRecordId,
			mainTableId : this.mainTableId,
			beginTime : this.billTimeStamp,
			endTime : this.endBillTimeStamp,
			proxyCompanyId : this.agencyCompanyId,
			rejectReasonId : this.reasonId,
		}
	}

	function indexMethod(index) {
		return generalIndexMethod(index, listVue)
	}
	//列表操作-------------------页面加载显示查询条件传递参数
	function loadForm() {
		return {
			interfaceVersion : this.interfaceVersion,
		}
	}
	//列表操作------------------页面加载显示代理公司
	function loadAgencyCompany(){
		var model = {
			interfaceVersion : this.interfaceVersion,
			theType : "11",
		};
		new ServerInterface(baseInfo.agencyCompanyInterface).execute(model, function(jsonObj) {
			if (jsonObj.result != "success") {
				$(baseInfo.edModelDivId).modal({
					backdrop : 'static'
				});
				listVue.errMsg = jsonObj.info;
			} else {
				listVue.agencyCompanyList = jsonObj.emmp_CompanyInfoList;
			}
		});
	}
	//列表操作-----------------页面加载显示退回原因
	function loadReason(){
		var model = {
			interfaceVersion : this.interfaceVersion,
			theName : "退回理由",	
		};
		new ServerInterface(baseInfo.reasonInterface).execute(model, function(jsonObj) {
			if (jsonObj.result != "success") {
				$(baseInfo.edModelDivId).modal({
					backdrop : 'static'
				});
				listVue.errMsg = jsonObj.info;
			} else {
				listVue.reasonList = [];
				jsonObj.sm_BaseParameterList.forEach(function(item){
					var model = {
						tableId : item.tableId,
						theName : item.theValue
					}
					listVue.reasonList.push(model)
				})
				//listVue.reasonList = jsonObj.sm_BaseParameterList;
			}
		});
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
				listVue.TripartiteAgreementEvaluationList = jsonObj.Tgxy_TripleAgreementReview_ViewList;
				listVue.pageNumber = jsonObj.pageNumber;
				listVue.countPerPage = jsonObj.countPerPage;
				listVue.totalPage = jsonObj.totalPage;
				listVue.keyword = jsonObj.keyword;
				listVue.selectedItem = [];
				//动态跳转到锚点处，id="top"
				document.getElementById('tripartiteAgreementEvaluationListDiv').scrollIntoView();
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
				window.location.href="../"+jsonObj.fileURL;
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
		listVue.agencyCompanyId = "";
		listVue.reasonId = "";
	}
	//------------------------方法定义-结束------------------//
	/**
	 * 初始化日期插件记账日期开始
	 */
	laydate.render({
		elem : '#tripartiteAgreementEvaluationDate',
		range: '~',
		done: function(value, date, endDate){
		  	var arr = value.split("~");
		  	listVue.dateRange = value;
		    listVue.billTimeStamp = arr[0];
		    listVue.endBillTimeStamp = arr[1];
		}
	});
	//------------------------数据初始化-开始----------------//
	listVue.loadAgencyCompany();
	listVue.loadReason();
	listVue.refresh();
	listVue.initData();
//------------------------数据初始化-结束----------------//
})({
	"listDivId" : "#tripartiteAgreementEvaluationListDiv",
	"edModelDivId" : "#edModelTripartiteAgreementEvaluation",
	"sdModelDiveId" : "#sdModelTripartiteAgreementEvaluation",
	"exportExcelId" : "#exportExcel",
	"agencyCompanyInterface":"../Emmp_CompanyInfoList",//显示代理公司接口
	"reasonInterface":"../Sm_BaseParameterList",//显示退回原因接口
	"listInterface" : "../Tgxy_TripleAgreementReview_ViewList",
	"exportInterface" : "../Tgxy_TripleAgreementReview_ViewExportExcel", //导出excel接口
});