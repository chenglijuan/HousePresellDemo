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
			keyword : "",
			rangeDate : "",
			dateRange2 : "",
			errMsg : "",
			escrowAcountName : "",
			LargeMaturityComparisonReportsList : [],
			dateRange1 :"",
			depositDateStart : "",//存入时间开始
			depositDateEnd : "",//存入时间结束
			dueDateStart : "",//到期时间开始
			dueDateEnd : "",//到期时间结束
			depositMethod: "",//存款性质
			depositMethodList : [
				{tableId:"大额存单",theName:"大额存单"},
				{tableId:"结构性存款",theName:"结构性存款"},
				{tableId:"保本理财",theName:"保本理财"},
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
			changeDepositMethod : function(data){
				this.depositMethod = data.tableId;
			},
			depositMethodEmpty : function(){
				this.depositMethod = null;
			}
		},
		computed:{
			// 
			
		},
		components : {
			'vue-nav' : PageNavigationVue,
			'vue-listselect' : VueListSelect,
		},
		watch:{
		},
		mounted:function(){
			/**
			 * 初始化日期插件记账日期开始
			 */
			laydate.render({
			    elem: '#date23010501',
			    range: '~',
			    done: function(value, date, endDate){
				    listVue.dateRange1 = value;
				  	var arr = value.split("~");
				    listVue.depositDateStart = arr[0];
				    listVue.depositDateEnd = arr[1];
			    }
			});
			laydate.render({
			    elem: '#date23010502',
			    range: '~',
			    done: function(value, date, endDate){
				    listVue.dateRange2 = value;
				  	var arr = value.split("~");
				    listVue.dueDateStart = arr[0];
				    listVue.dueDateEnd = arr[1];
			    }
			});
		}
	});
	
	//------------------------方法定义-开始------------------//
	 
	//列表操作--------------获取"搜索列表"表单参数
	function getSearchForm()
	{
		if(listVue.dateRange1 == ""){
			listVue.depositDateStart = "";
			listVue.depositDateEnd = "";//存入时间结束
		}
		if(listVue.dateRange2 == ""){
			listVue.dueDateStart = "";//到期时间开始
			listVue.dueDateEnd = "";//到期时间结束
		}
		return {
			interfaceVersion:this.interfaceVersion,
			pageNumber:this.pageNumber,
			countPerPage:this.countPerPage,
			totalPage:this.totalPage,
			keyword:this.keyword,
			theState:this.theState,
			userStartId:this.userStartId,
			userRecordId:this.userRecordId,
			depositDateStart : this.depositDateStart,//存入时间开始
			depositDateEnd : this.depositDateEnd,//存入时间结束
			dueDateStart : this.dueDateStart,//到期时间开始
			dueDateEnd : this.dueDateEnd,//到期时间结束
			depositMethod :this.depositMethod,//存款性质
		}
	}

	function indexMethod(index) {
		return generalIndexMethod(index, listVue)
	}

	//列表操作--------------刷新
	function refresh()
	{
		$('.el-table__body-wrapper').append($('.el-table__footer-wrapper table'));
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
				listVue.LargeMaturityComparisonReportsList=jsonObj.tg_BigAmountCompare_ViewList;
				listVue.LargeMaturityComparisonReportsList.forEach(function(item){
					item.depositAmount =  addThousands(item.depositAmount);
					item.expectInterest =  addThousands(item.expectInterest);
					item.realInterest =  addThousands(item.realInterest);
					item.compareDifference =  addThousands(item.compareDifference);
					
					item.interestRate =  addThousands(item.interestRate);
				})
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('largeMaturityComparisonReportsDiv').scrollIntoView();
			}
		});
		
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

	function initData()
	{
		
	}
	function resetSearch(){
		listVue.depositDateStart = "";
		listVue.depositDateEnd = "";
		listVue.dueDateStart = "";
	    listVue.dueDateEnd = "";
		listVue.dateRange1 = "";
		listVue.dateRange2 = "";
		listVue.projectName = "";
		listVue.companyName = "";
		listVue.keyword = "";
		listVue.depositMethod = "";
		generalResetSearch(listVue, function () {
            refresh()
        })
	}
	//------------------------方法定义-结束------------------//
	
	
	//------------------------数据初始化-开始----------------//
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#largeMaturityComparisonReportsDiv",
	"edModelDivId":"#edModelLargeMaturityComparisonReports",
	"sdModelDiveId":"#sdModelLargeMaturityComparisonReports",
	"listInterface":"../Tg_LargeMaturityComparisonReportsList",
	"exportInterface":"../Tg_BigAmountCompare_ViewExportExcel",//导出excel接口
});
