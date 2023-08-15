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
			keyword:"",
			floorAccountReportsList : [],
			companyId:'',
			projectId:'',
			buildId:'',
			qs_companyNameList : [], //页面加载显示开发企业
			qs_projectNameList : [], //显示项目名称
			qs_buildingNumberlist : [], //显示楼幢编号
			exceptZhengTai : true,//去掉正泰数据
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			getExportForm : getExportForm,//获取导出excel参数
			search:search,
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
			indexMethod : indexMethod,
			resetSearch : resetSearch,//重置
			exportForm : exportForm,//导出excel
//			getCompanyForm:getCompanyForm,
//			getObjectnameForm:getObjectnameForm,
//			getBuildingForm:getBuildingForm,
			
			loadCompanyName : loadCompanyNameFun, //页面加载显示开发企业方法
			changeCompanyHandle : changeCompanyHandleFun, //改变企业名称的方法
			changeProjectHandle : changeProjectHandleFun, //改变项目名称的方法
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
			}
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
			keyword:this.keyword,
			theNameOfCompany:this.companyId,//开发企业名称
			theNameOfProject:this.projectId,//项目名称
			eCodeFromConstruction:this.buildId,//施工楼幢
			sourceType : '0' //导出页面
		}
	}
	function indexMethod(index) {
		return generalIndexMethod(index, listVue)
	}
//	function getCompanyForm(){
//		return {
//			interfaceVersion:this.interfaceVersion,
//			
//		}
//	}
	//列表操作--------------刷新
	function refresh()
	{	//list 信息
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
				listVue.floorAccountReportsList=jsonObj.qs_BuildingAccount_ViewList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				listVue.floorAccountReportsList.forEach(function(item,index){
					item.escrowArea = thousandsToTwoDecimal(item.escrowArea);
					item.buildingArea = thousandsToTwoDecimal(item.buildingArea);
					item.orgLimitedAmount = thousandsToTwoDecimal(item.orgLimitedAmount);
					item.currentLimitedAmount = thousandsToTwoDecimal(item.currentLimitedAmount);
					item.totalAccountAmount = thousandsToTwoDecimal(item.totalAccountAmount);
					item.spilloverAmount = thousandsToTwoDecimal(item.spilloverAmount);
					item.payoutAmount = thousandsToTwoDecimal(item.payoutAmount);
					item.appliedNoPayoutAmount = thousandsToTwoDecimal(item.appliedNoPayoutAmount);
					item.applyRefundPayoutAmount = thousandsToTwoDecimal(item.applyRefundPayoutAmount);
					item.refundAmount = thousandsToTwoDecimal(item.refundAmount);
					item.currentEscrowFund = thousandsToTwoDecimal(item.currentEscrowFund);
					item.allocableAmount = thousandsToTwoDecimal(item.allocableAmount);
					if(item.recordAvgPriceBldPS != null) {
						item.recordAvgPriceBldPS = thousandsToTwoDecimal(item.recordAvgPriceBldPS);
					};
					if(item.recordAvgPriceOfBuilding != null) {
						item.recordAvgPriceOfBuilding = thousandsToTwoDecimal(item.recordAvgPriceOfBuilding);
					};
					item.zfbzPrice = thousandsToTwoDecimal(item.zfbzPrice);
					item.xjsxPrice = thousandsToTwoDecimal(item.xjsxPrice);
					item.yxsxPrice = thousandsToTwoDecimal(item.yxsxPrice);
				});
				//动态跳转到锚点处，id="top"
				document.getElementById('floorAccountReportsDiv').scrollIntoView();
			}
		});
		//开发企业 信息
//		new ServerInterface(baseInfo.companyInterface).execute(listVue.getCompanyForm(), function(jsonObj){
//			if(jsonObj.result != "success")
//			{
//				$(baseInfo.edModelDivId).modal({
//					backdrop :'static'
//				});
//				listVue.errMsg = jsonObj.info;
//			}
//			else
//			{
//				listVue.floorAccountReportsList=jsonObj.floorAccountReportsList;
//				listVue.pageNumber=jsonObj.pageNumber;
//				listVue.countPerPage=jsonObj.countPerPage;
//				listVue.totalPage=jsonObj.totalPage;
//				listVue.keyword=jsonObj.keyword;
//				listVue.selectedItem=[];
//				//动态跳转到锚点处，id="top"
//				document.getElementById('floorAccountReportsDiv').scrollIntoView();
//			}
//		});
//		objectname();
//		Building();
		
	}
//	function getObjectnameForm(){
//		return {
//			interfaceVersion:this.interfaceVersion,
//			
//		}
//	}
//	function objectname(){
//		//项目 信息
//		new ServerInterface(baseInfo.objectnameInterface).execute(listVue.getObjectnameForm(), function(jsonObj){
//			if(jsonObj.result != "success")
//			{
//				$(baseInfo.edModelDivId).modal({
//					backdrop :'static'
//				});
//				listVue.errMsg = jsonObj.info;
//			}
//			else
//			{
//				listVue.floorAccountReportsList=jsonObj.floorAccountReportsList;
//				listVue.pageNumber=jsonObj.pageNumber;
//				listVue.countPerPage=jsonObj.countPerPage;
//				listVue.totalPage=jsonObj.totalPage;
//				listVue.keyword=jsonObj.keyword;
//				listVue.selectedItem=[];
//				//动态跳转到锚点处，id="top"
//				document.getElementById('floorAccountReportsDiv').scrollIntoView();
//			}
//		});
//	}
//	function getBuildingForm(){
//		return {
//			interfaceVersion:this.interfaceVersion,
//			
//		}
//	}
//	function Building(){
//		//楼幢 信息
//		new ServerInterface(baseInfo.BuildingInterface).execute(listVue.getBuildingForm(), function(jsonObj){
//			if(jsonObj.result != "success")
//			{
//				$(baseInfo.edModelDivId).modal({
//					backdrop :'static'
//				});
//				listVue.errMsg = jsonObj.info;
//			}
//			else
//			{
//				listVue.floorAccountReportsList=jsonObj.floorAccountReportsList;
//				listVue.pageNumber=jsonObj.pageNumber;
//				listVue.countPerPage=jsonObj.countPerPage;
//				listVue.totalPage=jsonObj.totalPage;
//				listVue.keyword=jsonObj.keyword;
//				listVue.selectedItem=[];
//				//动态跳转到锚点处，id="top"
//				document.getElementById('floorAccountReportsDiv').scrollIntoView();
//			}
//		});
//	}


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
	function getExportForm(){
		return{
		   interfaceVersion:this.interfaceVersion,	
		}
	}
	
	
	//列表操作-----------------------导出excel
	function exportForm(){
		new ServerInterface(baseInfo.exportInterface).execute(listVue.getSearchForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj ,jsonObj.info);
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
	function resetSearch(){
		listVue.companyId= "";
		listVue.projectId= "";
		listVue.buildId= "";
		listVue.keyword = "";
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.loadCompanyName();
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#floorAccountReportsDiv",
	"edModelDivId":"#edModelfloorAccountReportsList",
	"sdModelDiveId":"#sdModelfloorAccountReportsList",
	"listInterface":"../Qs_BuildingAccount_ViewList",
	"exportInterface":"../Qs_BuildingAccount_ViewExportExcel",//导出excel接口
	"companyNameInterface" : "../Emmp_CompanyInfoList", //显示开发企业名称接口
	"changeCompanyInterface" : "../Empj_ProjectInfoList", //改变企业名称接口
	"changeProjectInterface" : "../Empj_BuildingInfoList", //改变项目名称接口
});
