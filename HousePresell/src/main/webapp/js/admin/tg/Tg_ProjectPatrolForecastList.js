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
			companyId : "", //开发企业
			dateRange : "",
			tg_companyNameList : [], //页面加载显示托管项目
			tg_AreaList: [],
			tg_projectNameList : [], //显示项目名称
			tg_buildingNumberlist : [], //显示楼幢编号
		    cache : [], //储蓄一条数据
	        cacheIndex : [], //位置
	        cacheData  : [],
			bankLendingReportsList : [],
			companyList:[
			      {
			      	prop:"index",
			      	label:'序号',
			      	width:"80",
			      },
			      {
			      	prop:"cityRegion",// 区域
			      	label:'区域',
			      	width:"160",
			      	align : ""
			      },
			      {
			      	prop:"projectName",// 项目名称
			      	label:'项目名称',
			      	width:"360",
			      	align : "",
			      },
			      {
			      	prop:"eCodeFromConstruction",// 托管楼幢
			      	label:'托管楼幢',
			      	width:"140",
			      	align : "",
				  },
			      {
			      	prop:"upTotalFloorNumber",// 地上总层数
			      	label:'地上总层数',
			      	width:"110",
			      	align : "",
			      },
			      {
			      	prop:"currentLimitedNote",// 当前受限节点
			      	label:'当前受限节点',
			      	width:"140",
			      	align : "",
			      },
			      {
			      	prop:"currentBuildProgress",// 当前建设进度
			      	label:'当前建设进度',
			      	width:"140",
			      	align : "",
			      },
			      {
			      	prop:"progressOfUpdateTime",// 当前更新时间
			      	label:'当前更新时间',
			      	width:"120",
			      	align : "center",
			      },
			      {
			      	prop:"nextChangeNode",// 下一变更节点
			      	label:'下一变更节点',
			      	width:"200",
			      	align : "",
			      },
			      {
			      	prop:"forecastNextChangeTime",// 预测变更时间
			      	label:'预测变更时间',
			      	width:"120",
			      	align : "center",
			      },
			      {
			      	prop:"preSalePermits",// 预售证
			      	label:'预售证',
			      	width:"260",
			      	align : "",
			      }
			],
			areaId: "",
			mortgageId: "",
			gradeId: "",
			tableData3: [],
		    projectList: [],
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
			loadProject : loadProject, //页面加载显示托管项目方法
			changeCompanyHandle : changeCompanyHandleFun, //改变托管项目的方法
			changeProjectHandle : changeProjectHandleFun, //改变项目名称的方法
			combineFun : function(){
				combine(this.cache,this.cacheIndex,this.cacheData,this.companyList,this.bankLendingReportsList)
			} ,
		    objectSpanMethod:function(row) {
			  	if(row.columnIndex===1){
			       return checkSpanMethod(listVue.cacheData,row.rowIndex,row.columnIndex);
			  }
			  	if(row.columnIndex===2){
				       return checkSpanMethod(listVue.cacheData,row.rowIndex,row.columnIndex);
			  }
			},
			handleChange: handleChange,
			getMortgageId: function(data) {
				listVue.mortgageId = data.tableId;
			},
			emptyMortgageId : function(data){
				listVue.mortgageId = null;
			},
			emptyAreaId : function(){
				listVue.areaId = null;
				listVue.projectList = [];
				listVue.mortgageId = null;
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
			totalPage:this.totalPage,
			/*keyword:this.keyword,*/
			
			cityRegionId: listVue.areaId,
			progressOfUpdateTime: listVue.dateRange,
			projectId: listVue.mortgageId,
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
				listVue.bankLendingReportsList=jsonObj.tg_ProInspectionSchedule_ViewList;
			/*	listVue.bankLendingReportsList.forEach(function(item){
					item.loanAmountIn = addThousands(item.loanAmountIn);
				})*/
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount=jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				listVue.combineFun();
				//动态跳转到锚点处，id="top"
				document.getElementById('projectPatrolForecastListDiv').scrollIntoView();
			}
		});
	}
	
	function getExportForm(){
		return{
		   interfaceVersion:this.interfaceVersion,	
		}
	}
	//列表操作-----------------------页面加载显示托管项目
	function loadProject() {
		new ServerInterface(baseInfo.loadProjectInterface).execute(listVue.getSearchForm(), function(jsonObj){
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
	function changeCompanyHandleFun() {
		var model = {
				interfaceVersion:this.interfaceVersion,
				companyId: listVue.companyId,
		}
		new ServerInterface(baseInfo.loadProjectInterface).execute(model, function(jsonObj){
			if (jsonObj.result != "success") {
				$(baseInfo.edModelDivId).modal({
					backdrop : 'static'
				});
				listVue.errMsg = jsonObj.info;
			} else {
				listVue.tg_AreaList = jsonObj.tg_AreaList;
			}
		});
	}
	//列表操作--------------------改变项目名称的方法
	function changeProjectHandleFun() {
		changeProjectHandle(listVue,baseInfo.changeProjectInterface,function(jsonObj){
			listVue.tg_buildingNumberlist = jsonObj.empj_BuildingInfoList;
		},listVue.errMsg,baseInfo.edModelDivId)
	}
	
	function handleChange(data)
	{
		listVue.areaId = data.tableId;
		var model ={
				interfaceVersion:this.interfaceVersion,
				cityRegionId: listVue.areaId
		}
		new ServerInterface(baseInfo.loadDetailInterface).execute(model, function(jsonObj){
			if (jsonObj.result != "success") {
				$(baseInfo.edModelDivId).modal({
					backdrop : 'static'
				});
				listVue.errMsg = jsonObj.info;
			} else {
				listVue.projectList =jsonObj.empj_ProjectInfoList;
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
		listVue.areaId = "";
		listVue.dateRange = "";
		listVue.mortgageId = "";
		generalResetSearch(listVue, function () {
            refresh()
        })
	}
	//------------------------方法定义-结束------------------//
	
	/**
	 * 初始化日期插件记账日期开始
	 */
	laydate.render({
	    elem: '#date23020601',
	    done:function(value){
	    	listVue.dateRange = value;
	    }
	    
	});
	//------------------------数据初始化-开始----------------//
	listVue.loadProject();// 加载托管项目
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#projectPatrolForecastListDiv",
	"loadProjectInterface" : "../Sm_CityRegionInfoList", //托管项目
	"changeCompanyInterface" : "../Empj_ProjectInfoList", //改变企业名称接口
	"changeProjectInterface" : "../Empj_BuildingInfoList", //改变项目名称接口
	"listInterface":"../Tg_ProInspectionSchedule_ViewList",
	"exportInterface":"../Tg_ProInspectionSchedule_ViewExportExcel",//导出excel接口
	"loadDetailInterface":"../Empj_ProjectInfoList"
});
