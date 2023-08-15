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
			
			EscriowBankFunds_viewList: [],
		   
			timeStamp:'' ,
			theNameOfBank:'',
			keyword:'',
			bankNames:[],
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			getExportForm : getExportForm,//获取导出excel参数
			search:search,
			changePageNumber: function (data) {
				if (listVue.pageNumber != data) {
					listVue.pageNumber = data;
					listVue.refresh();
				}
			},
			changeCountPerPage : function (data) {
				if (listVue.countPerPage != data) {
					listVue.countPerPage = data;
					listVue.refresh();
				}
			},
			indexMethod : indexMethod,
			resetSearch : resetSearch,//重置
			exportForm : exportForm,//导出excel
			getbanksForm:getbanksForm,
			//element-ui 合计
//			getSummaries(param) {
//	        var { columns, data } = param;
//	        var sums = [];
//	        var listLast= listVue.EscriowBankFunds_viewList[listVue.EscriowBankFunds_viewList.length-1]
//	        columns.forEach((column, index) => {
//	          if (index === 0) {
//	            sums[index] = '合计';
//	            return;
//	          }else if(index===1){
//	          	sums[index] = '';
//	          }else if(index===2){
//	          	sums[index] = addThousands(listLast.income);
//	          }else if(index===3){
//	          	sums[index] = addThousands(listLast.payout);
//	          }else if(index===4){
//	          	sums[index] = addThousands(listLast.certOfDeposit);
//	          }else if(index===5){
//	          	sums[index] = addThousands(listLast.structuredDeposit);
//	          }else if(index===6){
//	          	sums[index] = addThousands(listLast.breakEvenFinancial);
//	          }else if(index===7){
//	          	sums[index] = addThousands(listLast.currentBalance);
//	          }else if(index===8){
//	          	sums[index] = listLast.largeRatio;
//	          }else if(index===9){
//	          	sums[index] = listLast.largeAndCurrentRatio;
//	          }else if(index===10){
//		          	sums[index] = listLast.financialRatio;
//		     }else if(index===11){
//		          	sums[index] = listLast. totalFundsRatio;
//		    }else if(index===12){
//			         sums[index] = listLast.inProgress;
//			 }


//	          var values = data.map(item => Number(item[column.property]));
//	          if (!values.every(value => isNaN(value))) {
//	            sums[index] = values.reduce((prev, curr) => {
//	              var value = Number(curr);
//	              if (!isNaN(value)) {
//	                return prev + curr;
//	              } else {
//	                return prev;
//	              }
//	            }, 0);
//	            sums[index] += ' ';
//	          } else {
//	            sums[index] = 'N/A';
//	          }
//	        });
//
//	        return sums;
//	      },
	      load:load,
	      getTheNameOfBankId:function(data) {
	    	  listVue.theNameOfBank = data.tableId;
	      },
	      emptyTheNameofBankId : function(){
	    	  listVue.theNameOfBank = null; 
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
			timeStamp:this.timeStamp,
			theNameOfDepositBank:this.theNameOfBank,
			
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
				var EscriowBankFunds_viewList=jsonObj.qs_EscrowBankFunds_ViewList;
				EscriowBankFunds_viewList.forEach(function(items,index){
					items.income = addThousands(items.income);
					items.payout = addThousands(items.payout); 
					items.certOfDeposit = addThousands(items.certOfDeposit) ;
					items.structuredDeposit = addThousands(items.structuredDeposit) ;
					items.breakEvenFinancial = addThousands(items.breakEvenFinancial); 
					items.currentBalance = addThousands(items.currentBalance); 
					items.inProgressAccount = addThousands(items.inProgressAccount);
					items.transferOutAmount = addThousands(items.transferOutAmount);
					items.transferInAmount = addThousands(items.transferInAmount);

				})
				listVue.EscriowBankFunds_viewList = EscriowBankFunds_viewList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
//				document.getElementById('floorAccountReportsDiv').scrollIntoView();
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
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj ,jsonObj.info);
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
	laydate.render({
	  elem: '#EscrowBankFunds_listDate',
	  done: function(value, date){
		    listVue.timeStamp=value;
		   
		  }
	});
	function resetSearch(){
		listVue.timeStamp = '';
		listVue.theNameOfBank = "";
		listVue.keyword = "";
	}
	function getbanksForm(){
		return{
			   interfaceVersion:this.interfaceVersion,	
			}
	}
	function load(){
//		$('.el-table__body-wrapper').append($('.el-table__footer-wrapper table'));
		new ServerInterface(baseInfo.banksInterface).execute(listVue.getbanksForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				listVue.errMsg = jsonObj.info;
			}
			else
			{
				listVue.bankNames=jsonObj.emmp_BankBranchList;

			}
		});
		
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.load();
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#Qs_EscriowBankFunds_viewListDiv",
	"edModelDivId":"#edModelEscriowBankFunds_view",
	"sdModelDiveId":"#sdModelEscriowBankFunds_view",
	"listInterface":"../Qs_EscrowBankFunds_ViewList",
	"banksInterface":"../Emmp_BankBranchList",
	"exportInterface":"../Qs_EscrowBankFunds_ViewExportExcel",//导出excel接口
});
