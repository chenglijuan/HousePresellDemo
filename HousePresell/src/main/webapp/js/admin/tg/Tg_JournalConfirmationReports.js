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
			loanInDate : "",
			endLoanInDate : "",
			errMsg : "",
			escrowAcountName : "",
			journalConfirmationReportsList : [],
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
			getSummaries(param) {
		        var { columns, data } = param;
		        var sums = [];
		        columns.forEach((column, index) => {
		          if (index === 0) {
		            sums[index] = '总计';
		            return;
		          }
		          var values = data.map(item => Number(item[column.property]));
		          if (!values.every(value => isNaN(value))) {
		            sums[index] = values.reduce((prev, curr) => {
		              var value = Number(curr);
		              if (!isNaN(value)) {
		                return prev + curr;
		              } else {
		                return prev;
		              }
		            }, 0);
		            sums[index];
		          } else {
		            sums[index] = '--';
		          }
		        });
		        return sums;
		      }
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
			loanInDate : this.loanInDate,
			endLoanInDate : this.endLoanInDate,
			escrowAcountName : this.escrowAcountName,
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
				listVue.journalConfirmationReportsList=jsonObj.tg_JournalCount_ViewList;
				listVue.journalConfirmationReportsList.forEach(function(item){
					item.totalTradeAmount =  addThousands(item.totalTradeAmount);
					item.aToatlLoanAmout =  addThousands(item.aToatlLoanAmout);
					item.bToatlLoanAmout =  addThousands(item.bToatlLoanAmout);
					item.oToatlLoanAmout =  addThousands(item.oToatlLoanAmout);
					item.aFristToatlLoanAmout =  addThousands(item.aFristToatlLoanAmout);
					item.aToBusinessToatlLoanAmout =  addThousands(item.aToBusinessToatlLoanAmout);
				})
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('journalConfirmationReportsDiv').scrollIntoView();
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
		listVue.billTimeStamp = getNowFormatDate();
		listVue.projectName = "";
		listVue.companyName = "";
	}
	//------------------------方法定义-结束------------------//
	
	/**
	 * 初始化日期插件记账日期开始
	 */
	laydate.render({
	    elem: '#journalConfirmationSearchStart',
	    range: '~',
	    done: function(value, date, endDate){
		    listVue.rangeDate = value;
		  	var arr = value.split("~");
		    listVue.loanInDate = arr[0];
		    listVue.endLoanInDate = arr[1];
	    }
	});
	//------------------------数据初始化-开始----------------//
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#journalConfirmationReportsDiv",
	"edModelDivId":"#edModel",
	"sdModelDiveId":"#sdModel",
	"listInterface":"../Tg_JournalCount_ViewList",
	"exportInterface":"../Tg_JournalCount_ViewExportExcel",//导出excel接口
});
