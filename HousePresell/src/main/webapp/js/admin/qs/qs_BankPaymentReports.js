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
			totalAmount:0.00,
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
			qs_companyNameList : [], //页面加载显示开发企业
			qs_projectNameList : [], //显示项目名称
			qs_buildingNumberlist : [], //显示楼幢编号
		    cache : [], //储蓄一条数据
	        cacheIndex : [], //位置
	        cacheData  : [],
			bankPaymentReportsList : [],
			escrowAcountList : [],//托管账户
			escrowAcountId : "",
			companyList:[
			      {
			      	prop:"index",
			      	label:'序号',
			      },
			      {
				    prop:'companyName', //
				    label:'开发企业',
				    align : "left",
				    width : "360",
				    headerAlign : "center"
				  },
				  {
				    prop:'projectName',
				    label:'项目名称',
				    width : "360",
				    align : "left",
				    headerAlign : "center"
					},
				  {
				    prop:'escrowAcount',
				    label:'托管账户',
				    width : "240",
				    align : "left",
				    headerAlign : "center"
					},
				  {
				    prop:'escrowAcountShortName',
				    label:'托管账户简称',
				    align : "left",
				    width : "290",
				    headerAlign : "center"
				  }, {
				    prop:'loanOutDate',
				    label:'出账日期',
				    width : "120",
				    align : "center",
				    headerAlign : "center"
				  },{
				    prop:'loanAmountOut',
				    label:'出账金额（元）',
				    width : "160",
				    align : "right",
				    headerAlign : "center"
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
			loadBankAccountEscrowed : loadBankAccountEscrowed,//加载托管账户列表
			combineFun : function(){
				combine(this.cache,this.cacheIndex,this.cacheData,this.companyList,this.bankPaymentReportsList)
			} ,
		    objectSpanMethod:function(row) {
			  	if(row.columnIndex===1){
			       return checkSpanMethod(listVue.cacheData,row.rowIndex,row.columnIndex);
			  }
			},
			getCompanyId: function(data) {
				listVue.qs_projectNameList = [];
				listVue.projectId = null;
				listVue.companyId = data.tableId;
				listVue.changeCompanyHandle();
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
			getEscrowAcountId: function(data) {
				listVue.escrowAcountId = data.tableId;
			},
			emptyEscrowAcountId: function(data) {
				listVue.escrowAcountId = null;
			},
			
			
		},
		mounted:function(){
			 this.combineFun();
			 //this.objectSpanMethod();
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
	function loadBankAccountEscrowed()
	{
		var model = {
				interfaceVersion:this.interfaceVersion,
				countPerPage:99999,
		}
		
		new ServerInterface(baseInfo.listBankAccountEscrowedInterface).execute(model, function(jsonObj){
			if(jsonObj.result == "success")
			{
				listVue.escrowAcountList = jsonObj.tgxy_BankAccountEscrowedList;
			}
		});
		
	}
	 
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
			escrowAcount : listVue.escrowAcountId,//托管账户
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
				listVue.totalCount=jsonObj.totalCount;
				listVue.totalAmount = jsonObj.totalAmount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				listVue.combineFun();
				//动态跳转到锚点处，id="top"
				document.getElementById('bankPaymentReportsDiv').scrollIntoView();
			}
		});
	}
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
		
		/*loadCompanyName(listVue,baseInfo.companyNameInterface,function(jsonObj){
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
		listVue.dateRange = "";
		listVue.projectId = "";
		listVue.companyId = "";
		listVue.escrowAcountId = "";
		listVue.keyword = "";
		listVue.loanOutDate = "";
	    listVue.endLoanOutDate = "";
	    listVue.totalAmount = 0.00;
	}
	//------------------------方法定义-结束------------------//
	
	laydate.render({
	    elem: '#bankPaymentSearchStart',
	    range: '~',
		done: function(value, date, endDate){
			listVue.dateRange = value;
		  	var arr = value.split("~");
		    listVue.loanOutDate = arr[0];
		    listVue.endLoanOutDate = arr[1];
		}
	});
	
	//------------------------数据初始化-开始----------------//
	listVue.loadCompanyName();
	listVue.loadBankAccountEscrowed();
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#bankPaymentReportsDiv",
	"edModelDivId":"#edModel",
	"sdModelDiveId":"#sdModel",
	"companyNameInterface" : "../Emmp_CompanyInfoList", //显示开发企业名称接口
	"changeCompanyInterface" : "../Empj_ProjectInfoList", //改变企业名称接口
	"listBankAccountEscrowedInterface":"../Tgxy_BankAccountEscrowedViewPreList",//加载托管账户列表
	"listInterface":"../Tg_BankLoanOutSituation_ViewList",
	"exportInterface":"../Tg_BankLoanOutSituation_ViewExportExcel",//导出excel接口
});
