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
			InquiringInterestStatisticsAccordingReportsList : [],
			loadTime : "",///存入时间
			expirationTime : "",//到期时间
			bankBranchId : null,//开户行id
			bankBranchList : [],//银行列表
			
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
			getBankId:function(data){
				listVue.bankBranchId = data.tableId;
			},
			emptyBankId : function(data){
				listVue.bankBranchId = null;
			},
			getBankBreacList : getBankBreacList,
		},
		computed:{
			 
		},
		components : {
			'vue-select': VueSelect,
			'vue-listselect': VueListSelect,
			'vue-nav' : PageNavigationVue
		},
		watch:{
//			pageNumber : refresh,
		},
		mounted:function(){
			/**
			 * 初始化日期插件记账日期开始
			 */
			laydate.render({
		    elem: '#date23010601',
			    range: '~',
			    done: function(value, date, endDate){
				    listVue.rangeDate = value;
				  	var arr = value.split("~");
				    listVue.loadTime = arr[0];
				    listVue.expirationTime = arr[1];
			    }
			});
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
			bankBranchId : this.bankBranchId,
			loadTimeStart : this.loadTime,
			expirationTimeEnd : this.expirationTime
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
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				listVue.InquiringInterestStatisticsAccordingReportsList = jsonObj.tg_AccountabilityEnquiryList;
				listVue.InquiringInterestStatisticsAccordingReportsList.forEach(function(item){
					item.amountDeposited = addThousands(item.amountDeposited);
					item.interestRate = addThousands(item.interestRate);
				})
				//动态跳转到锚点处，id="top"
				document.getElementById('InquiringInterestStatisticsAccordingReportsDiv').scrollIntoView();
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
				generalErrorModal(jsonObj,jsonObj.info);
			} else {
				window.location.href="../"+jsonObj.fileDownloadPath;
			}
		});
	}
	//列表操作------------搜索
	function search()
	{
		listVue.pageNumber = 1;
		refresh();
	}
	
	//获取企业列表
	function getBankBreacList()
	{
		var model = {
			interfaceVersion : listVue.interfaceVersion,
			theState : 0
		};
		new ServerInterface(baseInfo.bankListInterface).execute(model, function(jsonObj)
		{
			if (jsonObj.result != "success") {

			} else {
				listVue.bankBranchList = jsonObj.emmp_BankBranchList;
			}
		});
	}
	
	function initData()
	{
		
	}
	function resetSearch(){
		listVue.rangeDate = "";
		listVue.bankBranchId = "",//开户行id
		listVue.loadTime = "";
	    listVue.expirationTime = "";
		
	}
	//------------------------方法定义-结束------------------//
	

	//------------------------数据初始化-开始----------------//
	listVue.getBankBreacList();
//	listVue.refresh();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#InquiringInterestStatisticsAccordingReportsDiv",
	"listInterface":"../Tg_AccountabilityEnquiryList",
	"exportInterface":"../Tg_AccountabilityEnquiryExportExcel",//导出excel接口
	"bankListInterface" : "../Emmp_BankBranchList",//获取开户行列表
});
