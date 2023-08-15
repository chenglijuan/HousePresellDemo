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
			keyword:"",
			CheckingAccountList : [	],
			theNameofBank:'',
			theNameofEscrow:'',
			bankNames:[],
			escrowNames:[],
			billTimeStamp:'',
			recordDateStart:'',
  	        recordDateEnd:'',
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
			getBanksForm:getBanksForm,
			getEscrowNumForm:getEscrowNumForm,
			load:load,
			objectname:objectname,
			
			
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
			keyword:this.keyword,
			recordDateStart:this.recordDateStart,//入账日期起始时间
			recordDateEnd:this.recordDateEnd,//入账日期结束时间			
			theNameOfBank:this.theNameofBank,//银行名称
			theNameOfEscrow:this.theNameofEscrow,//托管账户名称
		}
	}

	function indexMethod(index) {
		return generalIndexMethod(index, listVue)
	}
	function getBanksForm(){
		return {
			interfaceVersion:this.interfaceVersion,
			
		}
	}
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
				listVue.CheckingAccountList=jsonObj.qs_RecordAmount_ViewList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('CheckingAccountDiv').scrollIntoView();
			}
		});
		
		
		
		
	}
	function load(){
		//银行 信息
				new ServerInterface(baseInfo.banksInterface).execute(listVue.getBanksForm(), function(jsonObj){
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
						objectname();
					}
				});
	}
	function getEscrowNumForm(){
		return {
			interfaceVersion:this.interfaceVersion,
			bankBranchId:this.theNameofBank,//开户行主键
			
		}
	}
	function objectname(){
		//托管账号信息
		new ServerInterface(baseInfo.EscrowNumInterface).execute(listVue.getEscrowNumForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				listVue.errMsg = jsonObj.info;
			}
			else
			{
				listVue.escrowNames=jsonObj.tgxy_BankAccountEscrowedList;
				
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
		listVue.billTimeStamp = "";
		listVue.theNameofBank = "";
		listVue.theNameofEscrow = "";
		listVue.keyword = "";
	}
	
laydate.render({
  elem: '#checkingaccountDateSearch'
  ,range: '~'
  ,done: function(value, date,enddate){
  	listVue.billTimeStamp = value;
  	var str = value.split('~');
  	listVue.recordDateStart = str[0];
    listVue.recordDateEnd = str[1];
	
	}
});
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
listVue.load();
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#CheckingAccountDiv",
	"edModelDivId":"#edModelCheckingAccountList",
	"sdModelDiveId":"#sdModelCheckingAccountList",
	"listInterface":"../Qs_RecordAmount_ViewList",
	"banksInterface":"../Emmp_BankBranchList",//查询条件银行
	"EscrowNumInterface":"../Tgxy_BankAccountEscrowedList", //查询条件托管账号
	"exportInterface":"../Qs_RecordAmount_ViewExportExcel",//导出excel接口	
});
