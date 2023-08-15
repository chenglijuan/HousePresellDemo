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
			userStartId:null,
			userStartList:[],
			userRecordId:null,
			userRecordList:[],
			bankId:null,
			bankList:[],
			Tg_DepositFloatList:[],
			bankNames:[],
			theNameOfBank:"",
			signDateRange : "",
			endExpirationDateRange : "",
			signDateStart : "",
			signDateEnd : "",
			endExpirationDateStart : "",
			endExpirationDateEnd : "",
		},
		methods : {
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			getSearchForm : getSearchForm,
			search:search,
			exportExcelHandle : exportExcelHandle,
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
			getTheNameOfBankId:function(data) {
	    	  listVue.theNameOfBank = data.tableId;
	       },
	       emptyTheNameofBankId : function(){
	    	  listVue.theNameOfBank = null; 
	       },
	       load : load,
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue,
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
			totalPage:this.totalPage,
			keyword:this.keyword,
			signDateStart : this.signDateStart,
			signDateEnd : this.signDateEnd,
			endExpirationDateStart : this.endExpirationDateStart,
			endExpirationDateEnd : this.endExpirationDateEnd,
			bankBrenchId : this.theNameOfBank
		}
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
				listVue.Tg_DepositFloatList=jsonObj.tg_DepositFloat_ViewList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('Tg_DepositFloatListDiv').scrollIntoView();
			}
		});
	}
	
	//列表操作------------搜索
	function search()
	{
		listVue.pageNumber = 1;
		refresh();
	}
	
	function indexMethod(index) {
		return generalIndexMethod(index, listVue)
	}
	
	function handleReset()
	{
		listVue.keyword = "";
		listVue.theNameOfBank = null;
		listVue.bankBrenchId = null;
		listVue.signDateRange = "";
		listVue.signDateStart = "";
		listVue.signDateEnd = "";
		listVue.endExpirationDateRange = "";
		listVue.endExpirationDateStart = "";
	     listVue.endExpirationDateEnd = "";
	}
	
	function exportExcelHandle() {
        new ServerInterface(baseInfo.exportExcelInterface).execute(listVue.getSearchForm(), function(jsonObj)
        {
            if (jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
            	window.location.href="../"+jsonObj.fileURL;
            }
        });
    }
	
	function load(){
		var model = {
			interfaceVersion:listVue.interfaceVersion,	
		}
		new ServerInterface(baseInfo.banksInterface).execute(model, function(jsonObj){
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				listVue.bankNames=jsonObj.emmp_BankBranchList;

			}
		});
	}
	
	function initData()
	{
		load();
	}
	laydate.render({
	  elem: '#date23012001',
	  range: '~',
      done: function(value, date, endDate){
    	 listVue.signDateRange = value;
	  	 var arr = value.split("~");
	     listVue.signDateStart = arr[0];
	     listVue.signDateEnd = arr[1];
      }
	});
	laydate.render({
		  elem: '#date23012002',
		  range: '~',
	      done: function(value, date, endDate){
	    	 listVue.endExpirationDateRange = value;
		  	 var arr = value.split("~");
		     listVue.endExpirationDateStart = arr[0];
		     listVue.endExpirationDateEnd = arr[1];
	      }
		});
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#Tg_DepositFloatListDiv",
	"listInterface":"../Tg_DepositFloat_ViewList",
	"banksInterface":"../Emmp_BankBranchList",
	"exportExcelInterface":"../Tg_DepositFloat_ViewExportExcel",
});
