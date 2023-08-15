(function(baseInfo){
	var listVue = new Vue({
		el : baseInfo.listDivId,
		data : {
			interfaceVersion :19000101,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			keyword : "",
			selectItem : [],
			isAllSelected : false,
			theState:0,//正常为0，删除为1
				userStartId:null,
			userStartList:[],
			userRecordId:null,
			userRecordList:[],
			projectId:null,
			buildingId:null,
			buildingUnitId:null,
			bankId:null,
			tgpf_ManagedProjectDetailList:[],
			areaId: "",
			mortgageId: "",
			tg_AreaList: [],
			projectList: [],
			tg_projectNameList : [], //显示项目名称
			importUrl:"",
			deleCodes:[],
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
				    prop:'cityRegion', //
				    label:'区域',
				    width:'120',
				    headerAlign : "center",
				    align : "left"
				  },
				{
			    prop:'projectName', //
			    label:'项目名称',
			    width:'360',
			    headerAlign : "center",
			    align : "left"
			  },
			  {
			    prop:'eCodeFromConstruction',
			    label:'楼幢施工编号',
			     width:'160',
			     headerAlign : "center",
				 align : "left"
				},
			  {
			    prop:'forEcastArea',
			    label:'建筑面积（㎡）',
			     width:'140',
			     headerAlign : "center",
				 align : "right"
				},
			 {
			    prop:'escrowArea',
			    label:'托管面积（㎡）',
			     width:'140',
			     headerAlign : "center",
				 align : "right"
				},
			  {
			    prop:'recordAveragePrice',
			    label:'物价备案均价',
			     width:'140',
			     headerAlign : "center",
				align : "right"
				},
			  {
			    prop:'houseTotal',
			    label:'总户数',
			    width:'100',
			    headerAlign : "center",
			    align : "left"
				},
				{
			    prop:'produceOfProject', //
			    label:'项目介绍',
			    width:"870",
			    headerAlign : "center",
			    align : "left"
			  },
			],
			queryDate : "",
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			getExcelDataForm : getExcelDataForm,
			search:search,
			remaindRightDifferenceExportExcelHandle:remaindRightDifferenceExportExcelHandle,
			handleChange: function(data){
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
			},
			changeCityFunEmpty : function(){
				listVue.areaId = null;
				listVue.projectList = [];
			},
			loadProject : loadProject, //页面加载显示托管项目方法
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

		    handleReset: handleReset,
		    rowSpanMethod : function(row){
		    	
		    },
		    combine:function(){
				combine(this.cache,this.cacheIndex,this.cacheData,this.colData,this.tgpf_ManagedProjectDetailList);
			} ,
			objectSpanMethod:function(row) {
			  	if(row.columnIndex===1 || row.columnIndex=== 2 ||row.columnIndex===8){
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
    	   indexMethod : indexMethod,
    	   getMortgageId : function(data){
    		   this.mortgageId = data.tableId;
    	   },
    	   emptyMortgageId : function(){
    		   this.mortgageId = null;
    	   }
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue,
			'vue-listselect' : VueListSelect,
		},
		watch:{
			//pageNumber : refresh,
//			selectItem : selectedItemChanged,
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
			projectId: listVue.mortgageId,
			cityRegionId: listVue.areaId,
			queryDate : listVue.queryDate,
		}
	}

	//列表操作--------------获取导入的excel数据
	function getExcelDataForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			url:this.importUrl,
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
	

	
	function isInterger(o) {
		return typeof obj === 'number' && obj%1 === 0;
	}
	
	function indexMethod(index){
		return generalIndexMethod(index, listVue)
	}
	
	//列表操作--------------刷新
	function refresh()
	{
		new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				listVue.tgpf_ManagedProjectDetailList=jsonObj.tg_projectDetailOfBrowse_ViewList;
				listVue.tgpf_ManagedProjectDetailList.forEach(function(item){
					item.recordAveragePrice = addThousands(item.recordAveragePrice);
				})
				listVue.combine();
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('Tgpf_ManagedProjectDetailDiv').scrollIntoView();
			}
		});
	}
	
	//列表操作------------搜索
	function search()
	{
		listVue.pageNumber = 1;
		refresh();
	}

	function remaindRightDifferenceExportExcelHandle()
	{
		new ServerInterface(baseInfo.exportExcelInterface).execute(listVue.getSearchForm(), function (jsonObj) {
			if(jsonObj.result != "success")
			{
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				listVue.errMsg = jsonObj.info;
			}
			else
			{
				$(baseInfo.sdModelDiveId).modal({
					backdrop :'static'
				});
				
				window.location.href="../"+jsonObj.fileURL;
			}
		});
	}

	
	function initData()
	{
		listVue.selectItem = [];
		listVue.refresh();
	}
	
	function handleReset()
	{
		listVue.areaId = "";
		listVue.dateRange = "";
		listVue.mortgageId = "";
		listVue.queryDate = "";
		listVue.keyword = "";
		listVue.projectList = [];
		generalResetSearch(listVue, function () {
            refresh()
        })
	}
	
	laydate.render({
	    elem: '#managedProjectSearchStart',
	    type: 'month',
		done: function(value, date, endDate){
			listVue.queryDate = value;
			
		}
	});
	
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
//	listVue.refresh();
	listVue.loadProject();// 加载托管项目
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#Tgpf_ManagedProjectDetailDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Tg_ProjectDetailOfBrowse_ViewList",
	"loadDetailInterface":"../Empj_ProjectInfoList",
	"loadProjectInterface" : "../Sm_CityRegionInfoList", //托管项目
	"exportExcelInterface":"../Tg_ProjectDetailOfBrowse_ViewExportExcel",
});
