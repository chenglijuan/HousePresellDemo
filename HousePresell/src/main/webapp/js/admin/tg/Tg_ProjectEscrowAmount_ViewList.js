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
				    prop:'index', //
				    label:'序号',
				    width:'80',
				  },
				{
			    prop:'theNameOfCompany', //
			    label:'开发企业',
			    width:'300',
			  },
			  {
			    prop:'theNameOfProject',
			    label:'项目名称',
			     width:'300',
				},
			  {
			    prop:'eCodeFromConstruction',
			    label:'楼幢',
			     width:'150',
				},
			  {
			    prop:'loansCountHouse',
			    label:'放款户数',
			     width:'200',
				},
			  {
			    prop:'income',
			    label:'托管收入',
			    width:'200',
				},
				{
			    prop:'payout', //
			    label:'托管支出',
			    width:'200',
			  },
			  {
			    prop:'currentFund',
			    label:'余额',
			    width:'200',
				},
			  {
			    prop:'spilloverAmount',
			    label:'溢出资金',
			    width:'200',
				},
			  {
			    prop:'remarkNote',
			    label:'备注',
			    width:'200',
				},
			  
			],

			EscrowAccount_inAndOutList: [],     
  			companyId:'',
			projectId:'',	
			tg_companyNameList : [], //页面加载显示开发企业
			tg_projectNameList : [], //显示项目名称		
			recordDate:'',
			
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
//			getCompanyForm:getCompanyForm,
//			getObjectnameForm:getObjectnameForm,
//			getBuildingForm:getBuildingForm,
		//	loadForm : loadForm, //页面加载显示查询条件传递参数
			//getChangeCompanyForm : getChangeCompanyForm,
			//getChangeProjectForm : getChangeProjectForm,
			
			loadCompanyName : loadCompanyNameFun, //页面加载显示开发企业方法
			changeCompanyHandle : changeCompanyHandleFun, //改变企业名称的方法
			//changeProjectHandle : changeProjectHandleFun, //改变项目名称的方法
			arraySpanMethod:arraySpanMethod,
			combine:combine,
			  objectSpanMethod({ row, column, rowIndex, columnIndex }) {
			  	if(columnIndex===1){
		         if(this.cacheData.length<=0){
		           return false
		         }
		          var colNum = this.cacheData[rowIndex][columnIndex];
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
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue
		},
		watch:{
//			pageNumber : refresh,
		},
		mounted(){
	      
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
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				listVue.combine();
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
		loadCompanyName(listVue,baseInfo.companyNameInterface,function(jsonObj){
			listVue.tg_companyNameList = jsonObj.emmp_CompanyInfoList;
		},listVue.errMsg,baseInfo.edModelDivId);
	}
	//列表操作--------------------改变开发企业的方法
	function changeCompanyHandleFun() {
		changeCompanyHandle(listVue,baseInfo.changeCompanyInterface,function(jsonObj){
			listVue.tg_projectNameList = jsonObj.empj_ProjectInfoList;
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
	
	function arraySpanMethod({ row, column, rowIndex, columnIndex }) {
//		console.log(columnIndex);
//		console.log(row)
//		console.log(column);
//		console.log(rowIndex);
//		console.log(columnIndex);
//		if(rowIndex===1){
//			var length= listVue.EscrowAccount_inAndOutList[0].project.length;
//			listVue.EscrowAccount_inAndOutList.forEach(function(item,index){
//				item.project.forEach(function(itemList,Listindex){
//					
//				})
//			})
//		}
//		row.forEach(function(item,index){
//			console.log(item);
//		})
//	
//      if (rowIndex % 2 === 0) {
//        if (columnIndex === 0) {
//          return [1, 2];
//        } else if (columnIndex === 1) {
//          return [0, 0];
//        }
//      }
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
		listVue.companyId= "";
		listVue.projectId= "";
        listVue.recordDate=","
		listVue.keyword = "";
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
  function combine(){
  		var cache = this.cache; //储蓄一条数据
	var cacheIndex = this.cacheIndex; //位置
	var cacheData  = this.cacheData;
	var colData = this.colData;
//	  combine(){
        this.EscrowAccount_inAndOutList.forEach((res,i)=> {
          cacheData[i] = [];
          colData.forEach((item, j) => {
            if (i === 0) {
              cacheData[0][j] = 1;
              cache = JSON.parse(JSON.stringify(res));
              cacheIndex[j] = 0;
            } else {
              if(res[item.prop] === cache[item.prop] && item.prop !=='index'){
                cacheData[cacheIndex[j]][j]++;
                cacheData[i][j] = 0
              }else{
                cache[item.prop] = res[item.prop];
                cacheIndex[j] = i;
                cacheData[i][j] = 1;
              }
            }
          })
        })	
//    },
  }
	
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
