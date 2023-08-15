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
			dateRange : getDate(),
			tg_AreaList: [],
			tg_projectNameList : [], //显示项目名称
		    cache : [], //储蓄一条数据
	        cacheIndex : [], //位置
	        cacheData  : [],
			bankLendingReportsList : [],
			companyList:[
			      
			],
			closeId: "",
			closeIded: "",
			areaId: "",
			mortgageId: "",
			gradeId: "",
			tableData3: [],
			mortgageList : [
				{tableId:"未抵押",theName:"未抵押"},
				{tableId:"已抵押",theName:"已抵押"},
			],
			gradeList : [
				{tableId:"高",theName:"高"},
				{tableId:"中",theName:"中"},
				{tableId:"低",theName:"低"},
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
			loadCity : loadCity, //页面加载显示区域方法
			changeCityFun : function(data){
				listVue.areaId = data.tableId;
				
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
				listVue.tg_projectNameList = [];
			},
			combineFun : function(){
				combine(this.cache,this.cacheIndex,this.cacheData,this.companyList,this.bankLendingReportsList)
			} ,
		    objectSpanMethod :function(row) {
			  	if(row.columnIndex===1){
			       return checkSpanMethod(listVue.cacheData,row.rowIndex,row.columnIndex);
			  }
			},
			indexMethod: indexMethod,
			getProjectId : function(data){
				this.projectId = data.tableId;
			},
			emptyProjectId : function(){
				this.projectId = null
			},
			getMortgageId : function(data){
				this.mortgageId = data.tableId;
			},
			emptyMortgageId : function(){
				this.mortgageId = null
			},
			getGradeId : function(data){
				this.gradeId = data.tableId;
			},
			emptyGradeId : function(){
				this.gradeId = null
			},
			
		},
		computed:{
			
		},
		components : {
			'vue-nav' : PageNavigationVue,
			'vue-listselect': VueListSelect,
		},
		watch:{
//			pageNumber : refresh,
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
			userRecordId:this.userRecordId,
			mainTableId:this.mainTableId,
			billTimeStamp : this.billTimeStamp,
			endBillTimeStamp : this.endBillTimeStamp,
			
			managedProjectsId: listVue.projectId,
			areaId: listVue.areaId,
			dateRange: this.dateRange,
			attachment: listVue.closeId,
			attachmented: listVue.closeIded,
			landMortgage: listVue.mortgageId,
			riskRating: listVue.gradeId,
		}
	}

	function indexMethod(index) {
		console.log(generalIndexMethod(index, listVue));
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
				listVue.tableData3=jsonObj.tg_ProjectRiskList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				listVue.combineFun();
				//动态跳转到锚点处，id="top"
				document.getElementById('projectRisksDetailListDiv').scrollIntoView();
			}
		});
	}
	
	function getExportForm(){
		return{
		   interfaceVersion:this.interfaceVersion,	
		}
	}
	//列表操作-----------------------页面加载显示区域
	function loadCity() {
		new ServerInterface(baseInfo.loadCityInterface).execute(listVue.getExportForm(), function(jsonObj){
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
	//列表操作--------------------改变托管项目的方法
	/*function changeCityFun() {
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
	}*/
	
	//列表操作-----------------------导出excel
	function exportForm(){
		new ServerInterface(baseInfo.exportInterface).execute(listVue.getSearchForm(), function(jsonObj){
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
	function search()
	{
		listVue.dateRange = document.getElementById("bankLendingSearchStart1").value;
		listVue.pageNumber = 1;
		refresh();
	}

	function initData()
	{
		
	}
	function resetSearch(){
		document.getElementById("bankLendingSearchStart1").value = "";
		listVue.companyId = "";
		listVue.areaId = "";
//		listVue.dateRange = "";
		listVue.closeId = "";
		listVue.closeIded = "";
		listVue.mortgageId = "";
		listVue.gradeId = "";
		listVue.projectId = "";
		listVue.tg_projectNameList = [];
		generalResetSearch(listVue, function () {
            refresh()
        })
	}

	function indexMethod(index) {
		return generalIndexMethod(index, listVue)
	}
	
	/*获取当天日期YYYY-MM-DD*/
	function getDate(){ 
	     var nowDate = new Date();
	     var year = nowDate.getFullYear();
	     var month = nowDate.getMonth() + 1 < 10 ? "0" + (nowDate.getMonth() + 1): nowDate.getMonth() + 1;
	     var day = nowDate.getDate() < 10 ? "0" + nowDate.getDate() : nowDate.getDate();
	     var dateStr =year + "-" + month + "-" + day; 
	     return dateStr;
	}
	
	//------------------------方法定义-结束------------------//
	
	/**
	 * 初始化日期插件记账日期开始
	 */
	laydate.render({
	    elem: '#bankLendingSearchStart1',
	    range:false,
//	    type: 'month',
//	    value: getDate(),
	    done:function(value){
	    	listVue.dateRange = value;
	    }
	});
	//------------------------数据初始化-开始----------------//
	listVue.loadCity();// 加载托管项目
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#projectRisksDetailListDiv",
	"edModelDivId":"#edModel",
	"sdModelDiveId":"#sdModel",
	"loadCityInterface" : "../Sm_CityRegionInfoList", //区域
	"loadProjectInterface":"../Empj_ProjectInfoList", //项目
	"listInterface":"../Tg_ProjectRiskList",
	"exportInterface":"../Tg_ProjectRiskViewExportExcel",//导出excel接口
});
