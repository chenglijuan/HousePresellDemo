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
			interestForecastList : [],
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			getExportForm : getExportForm,//获取导出excel参数
			search:search,
//			changePageNumber : function(data){
//				listVue.pageNumber = data;
//			},
			indexMethod : indexMethod,
			resetSearch : resetSearch,//重置
			exportForm : exportForm,//导出excel
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
				listVue.interestForecastList=jsonObj.tg_InterestForecast_ViewList;
				
				if(listVue.interestForecastList!=undefined&&listVue.interestForecastList.length>0)
				{
					listVue.interestForecastList.forEach(function(item){
						item.principalAmount =  addThousands(item.principalAmount);
						item.annualRate =  addThousands(item.annualRate);
						item.interest =  addThousands(item.interest);
					})
				}
				
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount=jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('interestForecastDiv').scrollIntoView();
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
		listVue.loanInDate = "";
		listVue.endLoanInDate = "";
		listVue.keyword = "";
	}
	//------------------------方法定义-结束------------------//
	
	/**
	 * 初始化日期插件记账日期开始
	 */
	laydate.render({
	    elem: '#interestForecastSearchStart',
	    done: function(value, date, endDate){
		    listVue.loanInDate = value;
	    }
	});
	laydate.render({
	    elem: '#interestForecastSearchEnd',
	    done: function(value, date, endDate){
		    listVue.endLoanInDate = value;
	    }
	});
	//------------------------数据初始化-开始----------------//
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#interestForecastDiv",
	"edModelDivId":"#edModelInquiringInterestStatisticsAccordingReports",
	"sdModelDiveId":"#sdModelInquiringInterestStatisticsAccordingReports",
	"listInterface":"../Tg_InterestForecast_ViewList",
	"exportInterface":"../Tg_InterestForecast_ViewExportExcel",//导出excel接口
});
