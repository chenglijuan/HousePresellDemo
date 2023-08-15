(function(baseInfo){
	var listVue = new Vue({
		el : baseInfo.listDivId,
		data : {
			interfaceVersion :19000101,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			userStartId:null,
			userStartList:[],
			userRecordId:null,
			userRecordList:[],
			mainTableId:null,
			mainTableList:[],
			errMsg : "",
			keyword:"",
			cache : [], //储蓄一条数据
			cacheIndex : [], //位置
			cacheData  : [],
			colData:[
				{
					prop:'index', //
					label:'序号',
					width:'60',
				},
				{
					prop:'theNameOfCompany', //
					label:'开发企业',
					width:'360',
					align:"left",
					headerAlign : "center"
				},
				{
					prop:'theNameOfProject',
					label:'项目名称',
					width:'360',
					align:"left",
					headerAlign : "center"
				},
				{
					prop:'cityRegionname',
					label:'所属区域',
					width:'140',
					align:"left",
					headerAlign : "center"
				},
				{
					prop:'eCodeFromConstruction',
					label:'楼幢',
					width:'150',
					align:"left",
					headerAlign : "center"
				},
				{
					prop:'loansCountHouse',
					label:'放款户数',
					width:'140',
					align:"right",
					headerAlign : "center"
				},
				{
					prop:'income',
					label:'托管收入（元）',
					width:'180',
					align:"right",
					headerAlign : "center"
				},
				{
					prop:'payout', //
					label:'托管支出（元）',
					width:'180',
					align:"right",
					headerAlign : "center"
				},
				{
					prop:'currentFund',
					label:'余额（元）',
					width:'180',
					align:"right",
					headerAlign : "center"
				},
				{
					prop:'spilloverAmount',
					label:'溢出资金（元）',
					width:'180',
					align:"right",
					headerAlign : "center"
				},
				/*{
                  prop:'recordDate',
                  label:'入账日期',
                  width:'120',
                  align:"center",
                  headerAlign : "center"
                  },*/
				/* {
                   prop:'remarkNote',
                   label:'备注',
                   width:'870',
                   align:"left",
                   headerAlign : "center"
                   },*/
			],

			EscrowAccount_inAndOutList: [],
			companyId:'',
			projectId:'',
			qs_companyNameList : [], //页面加载显示开发企业
			qs_projectNameList : [], //显示项目名称
			recordDate:'',
			cityRegionId : "",
			sm_CityRegionInfoList: [],

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
//			getCompanyForm:getCompanyForm,
//			getObjectnameForm:getObjectnameForm,
//			getBuildingForm:getBuildingForm,
			//	loadForm : loadForm, //页面加载显示查询条件传递参数
			//getChangeCompanyForm : getChangeCompanyForm,
			//getChangeProjectForm : getChangeProjectForm,

			loadCompanyName : loadCompanyNameFun, //页面加载显示开发企业方法
			changeCompanyHandle : changeCompanyHandleFun, //改变企业名称的方法
			//changeProjectHandle : changeProjectHandleFun, //改变项目名称的方法
			changeCityRegionListener: changeCityRegionListener,
			changeCityRegionEmpty: changeCityRegionEmpty,
			combineFun:function(){
				combine(this.cache,this.cacheIndex,this.cacheData,this.colData,this.EscrowAccount_inAndOutList)
			},
			objectSpanMethod:function(row) {
				if(row.columnIndex===1){
					if(this.cacheData.length<=0){
						return false
					}
					var colNum = this.cacheData[row.rowIndex][row.columnIndex];
					if (colNum < 2) {
						return {
							rowspan: colNum,
							colspan: colNum
						}
					} else {
						return {
							rowspan: colNum,
							colspan: 1
						}
					}

				}

			},
			getCompanyId: function(data) {
				listVue.companyId = data.tableId;
				listVue.changeCompanyHandle();
			},
			emptyCompanyId : function(data){
				listVue.companyId = null;
				listVue.projectId = null;
			},
			getProjectId: function(data) {
				listVue.projectId = data.tableId;
			},
			emptyProjectId : function(data){
				listVue.projectId = null;
			},
		},
		computed:{

		},
		components : {
			'vue-nav' : PageNavigationVue,
			'vue-select': VueSelect,
			'vue-listselect': VueListSelect,
		},
		watch:{
		},
		mounted:function(){

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
			theNameOfCompany:this.companyId,//开发企业
			theNameOfProject:this.projectId,//项目
			recordDateStart:this.recordDateStart,//入账日期起始时间
			recordDateEnd:this.recordDateEnd,//入账日期结束时间
			cityRegionId:this.cityRegionId,
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
				listVue.EscrowAccount_inAndOutList=jsonObj.qs_ProjectEscrowAmount_ViewList;
				listVue.EscrowAccount_inAndOutList.forEach(function(item){
					item.loansCountHouse = addThousands(item.loansCountHouse);
					item.income = addThousands(item.income);
					item.payout = addThousands(item.payout);
					item.currentFund = addThousands(item.currentFund);
					item.spilloverAmount = addThousands(item.spilloverAmount);
					item.cityRegionname = addThousands(item.cityRegionname);
				})
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				listVue.combineFun();
				//动态跳转到锚点处，id="top"
				document.getElementById('EscrowAccount_inAndOutListDiv').scrollIntoView();
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
		/*
		loadCompanyName(listVue,baseInfo.companyNameInterface,function(jsonObj){
			listVue.qs_companyNameList = jsonObj.emmp_CompanyInfoList;
		},listVue.errMsg,baseInfo.edModelDivId);*/
	}
	//列表操作--------------------改变开发企业的方法
	function changeCompanyHandleFun() {
		changeCompanyHandle(listVue,baseInfo.changeCompanyInterface,function(jsonObj){
			listVue.qs_projectNameList = jsonObj.empj_ProjectInfoList;
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
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}
	function initData()
	{
		initButtonList();
		getCityRegionList();
	}
	//重置查询条件
	function resetSearch(){
		listVue.companyId= "";
		listVue.projectId= "";
		listVue.recordDate="";
		listVue.keyword = "";
		listVue.recordDateStart = "";
		listVue.recordDateEnd = "";
		listVue.cityRegionId = "";
	}

	function changeCityRegionListener(data) {
		if (listVue.cityRegionId != data.tableId) {
			listVue.cityRegionId = data.tableId
		}
	}

	function changeCityRegionEmpty() {
		if (listVue.cityRegionId != null) {
			listVue.cityRegionId = null
		}
	}

	function changeCityRegionEmpty() {
		if (listVue.cityRegionId != null) {
			listVue.cityRegionId = null
			// listVue.refresh()
		}
	}
	function getCityRegionList()
	{
		serverRequest("../Sm_CityRegionInfoForSelect",getTotalListForm(),function (jsonObj) {
			listVue.sm_CityRegionInfoList=jsonObj.sm_CityRegionInfoList;
		});
	}

	laydate.render({
		elem: '#ProjectEscrowAmountDateSearch'
		,range: '~'
		,done: function(value, date,enddate){
			listVue.recordDate = value;
			var str = value.split('~');
			listVue.recordDateStart = str[0];
			listVue.recordDateEnd = str[1];

		}
	});

	//------------------------方法定义-结束------------------//

	//------------------------数据初始化-开始----------------//
	listVue.loadCompanyName();
	listVue.refresh();
	listVue.initData();

	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#EscrowAccount_inAndOutListDiv",
	"edModelDivId":"#edModelEscrowAccount_inAndOutList",
	"sdModelDiveId":"#sdModelEscrowAccount_inAndOutList",
	"listInterface":"../Qs_ProjectEscrowAmount_ViewList",
	"exportInterface":"../Qs_ProjectEscrowAmount_ViewExportExcel",//导出excel接口
	"companyNameInterface" : "../Emmp_CompanyInfoList", //显示开发企业名称接口
	"changeCompanyInterface" : "../Empj_ProjectInfoList", //改变企业名称接口
//	"changeProjectInterface" : "../Empj_BuildingInfoList", //改变项目名称接口
});
