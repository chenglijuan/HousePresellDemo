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
			loanOutDate : "",//记账日期
			endLoanOutDate:"",
			projectId :"",//项目名称
			companyId : "",//开发企业
			keyword : "",//关键字
			dateRange : "",//日期区间
			tg_companyNameList : [], //页面加载显示开发企业
			tg_projectNameList : [], //显示项目名称
			tg_buildingNumberlist : [], //显示楼幢编号
		    cache : [], //储蓄一条数据
	        cacheIndex : [], //位置
	        cacheData  : [],
			bankPaymentReportsList : [],
			companyList:[
			      {
			      	prop:"index",
			      	label:'序号',
			      	width:"50"
			      },
			      {
				    prop:'companyName', //
				    label:'开发企业',
				    width:"130"
				  },
				  {
				    prop:'projectName',
				    label:'项目名称',
				    width:"160"
					},
				  {
				    prop:'escrowAcount',
				    label:'托管账户',
				    width:"220"
					},
				  {
				    prop:'escrowAcountShortName',
				    label:'托管账户简称',
				    width:"180"
				  }, {
				    prop:'loanOutDate',
				    label:'出账日期',
				    width:"160"
				  },{
				    prop:'loanAmountOut',
				    label:'出账金额（元）',
				    width:"160"
				  },
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
			loadCompanyName : loadCompanyNameFun, //页面加载显示开发企业方法
			changeCompanyHandle : changeCompanyHandleFun, //改变企业名称的方法
			combineFun : function(){
				combine(this.cache,this.cacheIndex,this.cacheData,this.companyList,this.bankPaymentReportsList)
			} ,
		    objectSpanMethod({ row, column, rowIndex, columnIndex }) {
			  	if(columnIndex===1){
			       return checkSpanMethod(listVue.cacheData,rowIndex,columnIndex);
			  }
			},
		},
		mounted(){
			 this.combineFun();
			 //this.objectSpanMethod();
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
			projectId : this.projectId, //项目名称
			companyId : this.companyId, //开发企业
			loanOutDate : this.loanOutDate,
			endLoanOutDate : this.endLoanOutDate,
		}
	}

	function indexMethod(index) {
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
				listVue.bankPaymentReportsList=jsonObj.tg_BankLoanOutSituation_ViewList;
				listVue.bankPaymentReportsList.forEach(function(item){
					item.loanAmountOut = addThousands(item.loanAmountOut);
				})
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('bankPaymentReportsDiv').scrollIntoView();
			}
		});
	}
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
		listVue.dateRange = "";
		listVue.projectId = "";
		listVue.companyId = "";
		listVue.keyword = "";
	}
	//------------------------方法定义-结束------------------//
	
	laydate.render({
	    elem: '#bankLendingSearchStart',
	    range: '~',
		done: function(value, date, endDate){
			console.log(value)
			listVue.dateRange = value;
		  	var arr = value.split("~");
		    listVue.loanOutDate = arr[0];
		    listVue.endLoanOutDate = arr[1];
		}
	});
	
	//------------------------数据初始化-开始----------------//
	listVue.loadCompanyName();
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#bankPaymentReportsDiv",
	"edModelDivId":"#edModel",
	"sdModelDiveId":"#sdModel",
	"companyNameInterface" : "../Emmp_CompanyInfoList", //显示开发企业名称接口
	"changeCompanyInterface" : "../Empj_ProjectInfoList", //改变企业名称接口
	"listInterface":"../Tg_BankLoanOutSituation_ViewList",
	"exportInterface":"../Tg_BankLoanOutSituation_ViewExportExcel",//导出excel接口
});
