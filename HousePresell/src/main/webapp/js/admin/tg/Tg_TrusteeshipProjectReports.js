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
			trusteeshipProjectReportsList : [],
			tg_companyNameList : [], //页面加载显示开发企业
			tg_projectNameList : [], //显示项目名称
			tg_buildingNumberlist : [], //显示楼幢编号
			cityRegionId : "",//区域id
			regionList : [],//区域list
			 cache : [], //储蓄一条数据
	        cacheIndex : [], //位置
	        cacheData  : [],
			trusteeshipProjectList : [
			    {prop : "index",	label : "序号",width : "50"},
			    {prop : "cityRegion",	label : "区域",width : "130"},
			    {prop : "companyName",	label : "企业信息",width : "240"},
			    {prop : "projectName",	label : "项目名称",width : "240"},
			    {prop : "upTotalFloorNumber",	label : "地上楼层数（总）",width : "220"},
			    {prop : "contractSigningDate",	label : "托管合作协议签订日期",width : "220"},
			    {prop : "preSaleCardDate",	label : "预售证日期",width : "220"},
			    {prop : "preSalePermits",	label : "预售许可证",width : "220"},
			    {prop : "eCodeOfAgreement", label : "协议编号",width : "220"},
			    {prop : "remark",	label : "备注",width : "240"}
			]
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			getExportForm : getExportForm,//获取导出excel参数
			search:search,
			changePageNumber : function(data){
				listVue.pageNumber = data;
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
				combine(this.cache,this.cacheIndex,this.cacheData,this.trusteeshipProjectList,this.trusteeshipProjectReportsList)
			} ,
		    objectSpanMethod({ row, column, rowIndex, columnIndex }) {
		    	if(columnIndex===2 || columnIndex===1){
		  			return checkSpanMethod(listVue.cacheData,rowIndex,2);
		  		}
			},
			changeRegionHandle : changeRegionHandle,
		},
		
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue
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
			reconciliationState : this.reconciliationState,
			projectId : this.projectId,
			companyId : this.companyId,
			cityRegionId : this.cityRegionId,
			contractSigningDate: this.contractSigningDate,
			endContractSigningDate: this.endContractSigningDate,
			preSaleCardDate: this.preSaleCardDate,
			endPreSaleCardDate: this.endPreSaleCardDate,
			eCodeOfAgreement:this.eCodeOfAgreement
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
				cityRegionId :  this.cityRegionId,
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
				listVue.tg_companyNameList=jsonObj.emmp_CompanyInfoList;
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
				listVue.trusteeshipProjectReportsList=jsonObj.tg_LoanProjectCountM_ViewList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				listVue.combineFun();
				//动态跳转到锚点处，id="top"
				document.getElementById('trusteeshipProjectReportsDiv').scrollIntoView();
			}
		});
	}
	//列表操作--------------------改变开发企业的方法
	function changeCompanyHandleFun() {
		var model = {
				interfaceVersion : this.interfaceVersion,
				developCompanyId : this.companyId,
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
				listVue.tg_projectNameList = jsonObj.empj_ProjectInfoList;
			}
		});
	}
	
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
		listVue.pageNumber = 1;
		refresh();
	}

	function initData()
	{
		
	}
	function resetSearch(){
		listVue.contractSigningDate = getNowFormatDate();
		listVue.endContractSigningDate = getNowFormatDate();
		listVue.preSaleCardDate = getNowFormatDate();
		listVue.endPreSaleCardDate = getNowFormatDate();
		listVue.projectName = "";
		listVue.companyName = "";
		listVue.eCodeOfAgreement = "";
	}
	//------------------------方法定义-结束------------------//
	
	/**
	 * 初始化日期插件合作协议签订日期开始
	 */
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
	/**
	 * 初始化日期插件预售证开盘日期结束
	 */
	laydate.render({
	    elem: '#openingQuotationSearch',
	    range: '~',
		done: function(value, date, endDate){
			listVue.openingQuotationDate = value;
		  	var arr = value.split("~");
		    listVue.preSaleCardDate = arr[0];
		    listVue.endPreSaleCardDate = arr[1];
		}
	});
	//------------------------数据初始化-开始----------------//
//	listVue.loadCompanyName();
	listVue.loadRegionName();
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#trusteeshipProjectReportsDiv",
	"edModelDivId":"#edModel",
	"sdModelDiveId":"#sdModel",
	"companyNameInterface" : "../Emmp_CompanyInfoList", //显示开发企业名称接口
	"changeCompanyInterface" : "../Empj_ProjectInfoList", //改变企业名称接口
	"listInterface":"../Tg_LoanProjectCountM_ViewList",
	"regionInterface" : "../Sm_CityRegionInfoList",
	"exportInterface":"../Tg_LoanProjectCountM_ViewExportExcel",//导出excel接口
});
