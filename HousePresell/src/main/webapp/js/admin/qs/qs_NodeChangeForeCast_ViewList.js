(function(baseInfo) {
	var listVue = new Vue({
		el : baseInfo.listDivId,
		data : {
			// 公共数据
			interfaceVersion : 19000101,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,

			// =======加载参数=======STRAT=======
			errMsg : "",
			dateRange : "",
			// 预测开始日期
			billTimeStamp : "",
			// 预测结束日期
			endBillTimeStamp : "",
			// 关键字
			keyword : "",
			// 项目
			projectId : "",
			// 企业
			companyId : "",
			// 企业列表
			qs_companyNameList : [],
			// 项目列表
			qs_projectNameList : [],
			
			//=======页面参数========START========
			Qs_NodeChangeForeCast_ViewList : [],

			
		},
		methods : {
			refresh : refresh,
			initData : initData,

			indexMethod : indexMethod,

			getSearchForm : getSearchForm,
			getExportForm : getExportForm,
			search : search,
			resetSearch : resetSearch,
			exportForm : exportForm,

			// 加载企业列表
			loadCompanyName : loadCompanyNameFun,
			// 加载项目列表
			changeCompanyHandle : changeCompanyHandleFun,
			
			changePageNumber: function (data) {
				if (listVue.pageNumber != data) {
					listVue.pageNumber = data;
					listVue.refresh();
				}
			},
			changeCountPerPage : function (data) {
				if (listVue.countPerPage != data) {
					listVue.countPerPage = data;
					listVue.refresh();
				}
			},

			getCompanyId : function(data) {
				listVue.companyId = data.tableId;
				listVue.changeCompanyHandle();
			},
			getProjectId : function(data) {
				listVue.projectId = data.tableId;
			},
			emptyCompanyId : function(data) {
				listVue.companyId = null;
				listVue.projectId = null;
				listVue.qs_projectNameList = [];
			},
			emptyProjectId : function(data) {
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

	// ------------------------方法定义-开始------------------//
	// 列表操作--------------获取"搜索列表"表单参数
	function getSearchForm() {
		return {
			interfaceVersion : this.interfaceVersion,
			pageNumber : this.pageNumber,
			countPerPage : this.countPerPage,
			totalPage : this.totalPage,
			keyword : this.keyword,
			PROJECTID : this.projectId,
			COMMPANYID : this.companyId,
			billTimeStamp : this.billTimeStamp,
			endBillTimeStamp : this.endBillTimeStamp,
		}
	}

	function getExportForm() {
		return {
			interfaceVersion : this.interfaceVersion,
		}
	}
	
	function indexMethod(index) {
		return generalIndexMethod(index, listVue)
	}

	// 列表操作--------------刷新
	function refresh() {
		new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
				$(baseInfo.edModelDivId).modal({
					backdrop : 'static'
				});
				listVue.errMsg = jsonObj.info;
			} else {
				listVue.Qs_NodeChangeForeCast_ViewList = jsonObj.Qs_NodeChangeForeCast_ViewList;
				console.log(listVue.Qs_NodeChangeForeCast_ViewList);

				listVue.Qs_NodeChangeForeCast_ViewList.forEach(function(item) {
					item.cURRENTESCROWFUND = addThousand(item.cURRENTESCROWFUND);
					item.oRGLIMITEDAMOUNT = addThousand(item.oRGLIMITEDAMOUNT);
					
					item.cASHLIMITEDAMOUNT = addThousand(item.cASHLIMITEDAMOUNT);
					item.nODELIMITEDAMOUNT = addThousand(item.nODELIMITEDAMOUNT);
					item.eFFECTIVELIMITEDAMOUNT = addThousand(item.eFFECTIVELIMITEDAMOUNT);
					item.nODELIMITAMOUNT = addThousand(item.nODELIMITAMOUNT);
					item.eFFLIMITAMOUNT = addThousand(item.eFFLIMITAMOUNT);
					item.aPPAMOUNT = addThousand(item.aPPAMOUNT);
				});
				
				listVue.pageNumber = jsonObj.pageNumber;
				listVue.countPerPage = jsonObj.countPerPage;
				listVue.totalPage = jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.totalAmount = jsonObj.totalAmount;
				listVue.keyword = jsonObj.keyword;
				
//				document.getElementById('nodeChangeForeCastDiv').scrollIntoView();
			}
		});
	}

	// 列表操作-----------------------页面加载显示开发企业
	function loadCompanyNameFun() {
		var model = {
			interfaceVersion : 19000101,
			exceptZhengTai : true,
		};
		new ServerInterface(baseInfo.companyNameInterface).execute(model, function(jsonObj) {
			if (jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
				listVue.qs_companyNameList = jsonObj.emmp_CompanyInfoList;
			}
		});
	}
	// 列表操作--------------------改变开发企业的方法
	function changeCompanyHandleFun() {
		changeCompanyHandle(listVue, baseInfo.changeCompanyInterface, function(jsonObj) {
			listVue.qs_projectNameList = jsonObj.empj_ProjectInfoList;
		}, listVue.errMsg, baseInfo.edModelDivId)
	}

	// 列表操作-----------------------导出excel
	function exportForm() {
		new ServerInterface(baseInfo.exportInterface).execute(listVue.getSearchForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
				generalErrorModal(jsonObj, jsonObj.info);
			} else {
				window.location.href = "../" + jsonObj.fileURL;
			}
		});
	}

	
	function addThousand(number) {
		if(null == number) return "";
		if(number == "") return "0.00";
	    var num = number + "";
	    num = num.replace(new RegExp(",","g"),"");//默认是千分位的，先将逗号去除
	    // 正负号处理
	    var symble = "";
	    if(/^([-+]).*$/.test(num)) {
	        symble = num.replace(/^([-+]).*$/,"$1");//得到正负号
	        num = num.replace(/^([-+])(.*)$/,"$2");
	    }
	    if(/^[0-9]+(\.[0-9]+)?$/.test(num) || /^[0-9]+(\.+)?$/.test(num)) {
	        var num = num.replace(new RegExp("^[0]+","g"),"");
	        num = Number(num).toFixed(2);//保留后面两位小数
	        if(/^\./.test(num)) {
	            num = "0" + num;
	        }
	        var decimal = num.replace(/^[0-9]+(\.[0-9]+)?$/,"$1");//得到小数部分
	        var integer= num.replace(/^([0-9]+)(\.[0-9]+)?$/,"$1");//得到整数部分
	        var re=/(\d+)(\d{3})/;
	        while(re.test(integer)){
	            integer = integer.replace(re,"$1,$2");
	        }
	        if(decimal === ""){
	            return symble + integer + ".00";
	        }else{
	            return symble + integer + decimal;
	        }
	    }else{
	    	return number;
	    }
	}
	
	// 列表操作------------搜索
	function search() {
		listVue.pageNumber = 1;
		refresh();
	}
	function initButtonList() {
		// 封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}
	function initData() {
		initButtonList();
	}
	function resetSearch() {
		listVue.projectId = "";
		listVue.companyId = "";
		listVue.billTimeStamp = "";
		listVue.endBillTimeStamp = "";
		listVue.dateRange = "";
	}
	// ------------------------方法定义-结束------------------//

	/**
	 * 初始化日期插件记账日期开始
	 */
	/*laydate.render({
		elem : '#nodeChangeForeCastSearchDate',
		range : '~',
		done : function(value, date, endDate) {
			listVue.dateRange = value;
			var arr = value.split(" ~ ");
			listVue.billTimeStamp = arr[0];
			listVue.endBillTimeStamp = arr[1];
		}
	});*/
	
	laydate.render({
		  elem: '#nodeChangeForeCastSearchDate',
		  done: function(value, date){
			  listVue.billTimeStamp = value;
			  listVue.endBillTimeStamp = value;
			  listVue.dateRange = value;
			   
			  }
		});
	// ------------------------数据初始化-开始----------------//
	listVue.changeCompanyHandle();
//	listVue.refresh();
//	listVue.initData();
	// ------------------------数据初始化-结束----------------//
})({
	"listDivId" : "#nodeChangeForeCastDiv",
	"edModelDivId" : "#edModel",
	"sdModelDiveId" : "#sdModel",
	// 开发企业列表加载
	"companyNameInterface" : "../Emmp_CompanyInfoList",
	// 项目列表加载
	"changeCompanyInterface" : "../Empj_ProjectInfoList",
	// 列表加载
	"listInterface" : "../Qs_NodeChangeForeCast_ViewList",
	// 导出excel接口
	"exportInterface" : "../Qs_NodeChangeForeCast_ViewExportExcel",
});
