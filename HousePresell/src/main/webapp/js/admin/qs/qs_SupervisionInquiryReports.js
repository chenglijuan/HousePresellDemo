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
			cooperationAgreementDate : "",//合作协议签订日期
			openingQuotationDate : "",//预售证开盘日期
			contractSigningDate:"",//合作协议签订日期（开始）
			endContractSigningDate:"",//合作协议签订日期（结束）
			preSaleCardDate:"",//预售证开盘日期（开始）
			endPreSaleCardDate:"",//预售证开盘日期（结束）
			eCodeOfAgreement:"",//协议编号
			projectName :"",//项目名称
			companyName : "",//开发企业
			keyword:"",//关键字
			projectId : "", //项目名称
			companyId : "", //开发企业
			buildId : "", //楼幢号
			inquiryList : [],
			qs_companyNameList : [], //页面加载显示开发企业
			qs_projectNameList : [], //显示项目名称
			qs_buildingNumberlist : [], //显示楼幢编号
			cityRegionId : "",//区域id
			regionList : [],//区域list
			 cache : [], //储蓄一条数据
	        cacheIndex : [], //位置
	        cacheData  : [],
			trusteeshipProjectList : [
			    {prop : "index",	label : "序号",width : "80",align:"center"},
			    {prop : "cityRegion",	label : "区域",width : "130"},
			    {prop : "companyName",	label : "企业信息",width : "360"},
			    {prop : "projectName",	label : "项目名称",width : "360"},
			    {prop : "upTotalFloorNumber",	label : "地上楼层数（总）",width : "140"},
			    {prop : "contractSigningDate",	label : "托管合作协议签订日期",width : "120",align:"center"},
			    {prop : "preSaleCardDate",	label : "预售证日期",width : "120",align:"center"},
			    {prop : "preSalePermits",	label : "预售许可证",width : "220"},
			    {prop : "eCodeOfAgreement", label : "协议编号",width : "220"},
			    {prop : "remark",	label : "备注",width : "870"}
			],
			
			
		completeStart : "",
	    completeEnd : "",
	    
		},
		methods : {
			refresh : refresh,
			initData:initData,
			toDetail : toDetail,
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
			loadForm : loadForm, //页面加载显示查询条件传递参数
			getChangeCompanyForm : getChangeCompanyForm,
			getChangeProjectForm : getChangeProjectForm,
			loadRegionName : loadRegionName,
			changeCompanyHandle : changeCompanyHandleFun, //改变企业名称的方法
			combineFun : function(){
				combine(this.cache,this.cacheIndex,this.cacheData,this.trusteeshipProjectList,this.inquiryList)
			} ,
		    objectSpanMethod:function(row) {
		    	if(row.columnIndex===2 || row.columnIndex===1){
		  			return checkSpanMethod(listVue.cacheData,row.rowIndex,2);
		  		}
			},
			changeRegionHandle : changeRegionHandle,
			getCityRegionId: function(data) {
				listVue.qs_projectNameList = [];
				listVue.cityRegionId = data.tableId;
//				listVue.changeRegionHandle();
				listVue.changeCompanyHandle();
			},
			emptyCityRegionId : function(data){
				listVue.cityRegionId = null;
				listVue.projectId = null;
//				listVue.qs_companyNameList = [];
				listVue.qs_projectNameList = [];
			},
			getCompanyId: function(data) {
				listVue.projectId = null;
				listVue.qs_projectNameList = [];
				listVue.companyId = data.tableId;
//				listVue.changeCompanyHandle();
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
			},
			
			loadCompanyName : loadCompanyName,
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
			companyName : listVue.companyName,
			companyId : listVue.companyId,
			completeStart : listVue.completeStart,
			completeEnd : listVue.completeEnd,
		}
	}

	
	function toDetail(tableId){
		enterNewTab(tableId, "工程进度节点更新详情", "empj/Empj_BldLimitAmountDetail.shtml")
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
	//列表操作-------------------页面加载导出excel参数
	function getExportForm() {
		return {
			interfaceVersion : this.interfaceVersion,
		}
	}
	//列表操作-------------------获取根据开发企业查项目的参数
	function getChangeCompanyForm() {
		return {
			interfaceVersion : this.interfaceVersion,
			developCompanyId : this.companyId,
		}
	}
	//列表操作-----------------获取根据项目名称查楼幢号的参数
	function getChangeProjectForm() {
		return {
			interfaceVersion : this.interfaceVersion,
			projectId : this.projectId,
		}
	}
	
	//列表操作-----------------页面加载显示区域
	function loadRegionName(){
		new ServerInterface(baseInfo.regionInterface).execute(listVue.loadForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				listVue.errMsg = jsonObj.info;
			}
			else
			{
				listVue.regionList=jsonObj.sm_CityRegionInfoList;
			}
		});
	}
	//列表操作-----------------改变区域显示名称
	function changeRegionHandle(){
		var model = {
				interfaceVersion : this.interfaceVersion,
//				cityRegionId :  this.cityRegionId,
				exceptZhengTai : true,
		}
		new ServerInterface(baseInfo.companyNameInterface).execute(model, function(jsonObj){
			if(jsonObj.result != "success")
			{
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				listVue.errMsg = jsonObj.info;
			}
			else
			{
				listVue.qs_companyNameList=jsonObj.emmp_CompanyInfoList;
			}
		});
	}
	
	//列表操作--------------刷新
	function refresh()
	{
		listVue.combineFun();
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
				listVue.inquiryList=jsonObj.reportList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				listVue.combineFun();
				//动态跳转到锚点处，id="top"
				//document.getElementById('trusteeshipProjectReportsDiv').scrollIntoView();
			}
		});
	}
	
	//列表操作--------------------改变开发企业的方法
	function changeCompanyHandleFun() {
		var model = {
				interfaceVersion : this.interfaceVersion,
				developCompanyId : this.companyId,
				cityRegionId : this.cityRegionId,
		}
		new ServerInterface(baseInfo.changeCompanyInterface).execute(model, function(jsonObj){
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
	
	//列表操作-----------------------导出excel
	function exportForm(){
		new ServerInterface(baseInfo.exportInterface).execute(listVue.getSearchForm(), function(jsonObj){
			if (jsonObj.result != "success") 
			{
				generalErrorModal(jsonObj, jsonObj.info);
			}
			else 
			{
				window.location.href="../"+jsonObj.fileDownloadPath;
			}
		});
	}
	
	function loadCompanyName() {
		var model = {
				interfaceVersion : 19000101
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
	function resetSearch(){
		$("#cooperationAgreementSearch").val("");
		$("#openingQuotationSearch").val("");
		listVue.contractSigningDate = "";
		listVue.endContractSigningDate = "";
		listVue.preSaleCardDate = "";
		listVue.endPreSaleCardDate = "";
		listVue.projectName = "";
		listVue.companyName = "";
		listVue.eCodeOfAgreement = "";
		listVue.projectId = "";
		listVue.companyId = "";
		listVue.cityRegionId = "";
		listVue.completeStart = "";
		listVue.completeEnd = "";
	}
	//------------------------方法定义-结束------------------//
	

	laydate.render({
	    elem: '#cooperationAgreementSearch',
	    range: '~',
		done: function(value, date, endDate){
			listVue.cooperationAgreementDate = value;
		  	var arr = value.split("~");
		    listVue.contractSigningDate = arr[0];
		    listVue.endContractSigningDate = arr[1];
		}
	});
	
	
	laydate.render({
	    elem: '#openingQuotationSearch',
	    range: '~',
		done: function(value, date, endDate){
			listVue.openingQuotationDate = value;
		  	var arr = value.split("~");
		    listVue.preSaleCardDate = arr[0];
		    listVue.endPreSaleCardDate = arr[1];
		    
		    listVue.completeStart = arr[0];
		    listVue.completeEnd = arr[1];
		}
	});
	//------------------------数据初始化-开始----------------//
	listVue.loadCompanyName();
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#supervisionInquiryReportsDiv",
	"edModelDivId":"#edModel",
	"sdModelDiveId":"#sdModel",
	//显示开发企业名称接口
	"companyNameInterface" : "../Empj_BldLimitAmountQueryCompanyList",
	//改变企业名称接口
	"changeCompanyInterface" : "../Empj_ProjectInfoList",
	"listInterface":"../Empj_BldLimitAmountQueryReportList",
	"regionInterface" : "../Sm_CityRegionInfoList",
	//导出excel接口
	"exportInterface":"../Empj_BldLimitAmountQueryReportExportList",
});
