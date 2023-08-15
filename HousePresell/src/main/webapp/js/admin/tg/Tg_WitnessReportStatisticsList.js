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
			keyword : "",
            projectId : "", //项目名称
			companyId : "", //开发企业
			dateRange : "",
			billTimeStamp : "",//见证报告开始日期
			endBillTimeStamp : "",//见证报告结束日期
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
			      },
			      {
			      	prop:"projectName",// 项目名称
			      	label:'项目名称',
			      	width:"360",
			      	align : ""
			      },
			      {
			      	prop:"projectArea",// 项目区域
			      	label:'项目区域',
			      	width:"160",
			      	align : ""
			      },
			      {
			      	prop:"constructionNumber",// 施工编号
			      	label:'施工编号',
			      	width:"200",
			      	align : ""
			      },
			      {
			      	prop:"witnessNode",// 见证节点
			      	label:'见证节点',
			      	width:"200",
			        align : ""
			      },
			      {
			      	prop:"supervisionCompany",// 监理公司
			      	label:'进度见证服务单位',
			      	align : ""
			      },
			      {
			      	prop:"witnessAppoinTime",// 见证预约时间
			      	label:'见证预约时间',
			      	width:"120",
			      	align : "center"
			      },
			      {
			      	prop:"reportUploadTime",// 报告上传时间
			      	label:'报告上传时间',
			      	width:"120",
			      	align : "center"
			      }
			],
			areaId: "",
			mortgageId: "",
			gradeId: "",
			tableData3: [{
		          date: '2016-05-03',
		          name: '王小虎',
		          province: '上海',
		          city: '普陀区',
		          address: '上海市普陀区金沙江路 1518 弄',
		          zip: 200333
		        },{
			          date: '2016-05-03',
			          name: '王小虎',
			          province: '上海',
			          city: '普陀区',
			          address: '上海市普陀区金沙江路 1518 弄',
			          zip: 200333
			        },{
				          date: '2016-05-03',
				          name: '王小虎',
				          province: '上海',
				          city: '普陀区',
				          address: '上海市普陀区金沙江路 1518 弄',
				          zip: 200333
				        }],
		    supervisionId: "",
		    supervisionList: [],
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
			loadProject : loadProject, //页面加载显示所属区域方法
			changeCompanyHandle : changeCompanyHandleFun, //改变托管项目的方法
			changeProjectHandle : changeProjectHandleFun, //改变项目名称的方法
			combineFun : function(){
				combine(this.cache,this.cacheIndex,this.cacheData,this.companyList,this.bankLendingReportsList)
			} ,
		    objectSpanMethod:function(row) {
			  	if(row.columnIndex===1){
			       return checkSpanMethod(listVue.cacheData,row.rowIndex,row.columnIndex);
			  }
			},
			handleChange: handleChange,
			getSupervisionId: function(data) {
				listVue.supervisionId = data.tableId;
			},
			emptySupervisionId : function(data){
				listVue.supervisionId = null;
			},
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
		//	pageNumber : refresh,
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
			billTimeStamp:this.billTimeStamp,
			endBillTimeStamp:this.endBillTimeStamp,
			
			projectAreaId: listVue.areaId,
//			reportUploadTime: document.getElementById("date23020201").value,
			supervisionCompanyId: listVue.supervisionId,
			projectNameId: listVue.mortgageId,
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
				listVue.bankLendingReportsList=jsonObj.Tg_WitnessStatisticsList;
				listVue.tg_AreaList=jsonObj.Sm_CityRegionInfolist;
				
				listVue.bankLendingReportsList.forEach(function(item){
					item.loanAmountIn = addThousands(item.loanAmountIn);
				})
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount=jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				listVue.combineFun();
				//动态跳转到锚点处，id="top"
				document.getElementById('witnessReportStatisticsListDiv').scrollIntoView();
			}
		});
	}
	
	function getExportForm(){
		return{
		   interfaceVersion:this.interfaceVersion,	
		}
	}
	//列表操作-----------------------页面加载区域项目
	function loadProject() {
		new ServerInterface(baseInfo.loadProjectInterface).execute(listVue.getSearchForm(), function(jsonObj){
			if (jsonObj.result != "success") {
				$(baseInfo.edModelDivId).modal({
					backdrop : 'static'
				});
				listVue.errMsg = jsonObj.info;
			} else {
				listVue.tg_companyNameList = jsonObj.tg_companyNameList;
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
		listVue.areaId = data.tableId
		listVue.supervisionId = "";
		listVue.mortgageId = "";
		var model ={
				interfaceVersion:this.interfaceVersion,
				tableId: listVue.areaId,
		}
		new ServerInterface(baseInfo.loadDetailInterface).execute(model, function(jsonObj){
			if (jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
				listVue.projectList = jsonObj.empj_ProjectInfolist;
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
		//listVue.dateRange = document.getElementById("date23020201").value;
		listVue.billTimeStamp=this.billTimeStamp;
		listVue.endBillTimeStamp=this.endBillTimeStamp;
		listVue.pageNumber = 1;
		refresh();
	}

	function initData()
	{
		var model ={
				interfaceVersion:this.interfaceVersion,
		}
		new ServerInterface(baseInfo.loadDetailInterface).execute(model, function(jsonObj){
			if (jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
				listVue.supervisionList = jsonObj.emmp_CompanyInfoList;
			}
		});
	}
	
	function resetSearch(){
		listVue.areaId = "";
		listVue.companyId = "";
		istVue.dateRange = "";
		istVue.projectId = "";
		istVue.billTimeStamp = "";
		istVue.endBillTimeStamp = "";
	}
	//------------------------方法定义-结束------------------//
	
	/**
	 * 初始化日期插件记账日期开始
	 */
	laydate.render({
	    elem: '#date23020201',
	    range: '~',
	    done: function(value, date, endDate){
		    listVue.dateRange = value;
		  	var arr = value.split("~");
		    listVue.billTimeStamp = arr[0];
		    listVue.endBillTimeStamp = arr[1];
	    }
	    
	});
	//------------------------数据初始化-开始----------------//
	listVue.loadProject();// 加载托管项目
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#witnessReportStatisticsListDiv",
	"edModelDivId":"#edModel",
	"sdModelDiveId":"#sdModel",
	"loadProjectInterface" : "../Emmp_CompanyInfoList", //托管项目
	"changeCompanyInterface" : "../Empj_ProjectInfoList", //改变企业名称接口
	"changeProjectInterface" : "../Empj_BuildingInfoList", //改变项目名称接口
	"listInterface":"../Tg_WitnessStatisticsList",
	"exportInterface":"../Tg_WitnessStatisticsExportExcel",//导出excel接口
	"loadDetailInterface":"../Tg_WitnessStatisticsbyarea"
});
