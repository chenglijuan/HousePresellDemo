(function(baseInfo){
	var listVue = new Vue({
		el : baseInfo.listDivId,
		data : {
			interfaceVersion :19000101,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			selectItem : [],
			isAllSelected : false,
			theState:0,//正常为0，删除为1
			userStartId:null,
			userStartList:[],
			userRecordId:null,
			userRecordList:[],
			tgpf_DayEndBalanceList:[],
			reconciliationState : "", //对账状态
			billTimeStamp:"",//记账日期
			cancelArr:[],
			sumCount: "",
			sumAmount: "",
			idArr: [],
			errMsg1 : "",
			balanceDisabled : true,
			balanceEndDisabled : true,
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			search:search,
			checkAllClicked : checkAllClicked,
			checkCheckBox : checkCheckBox,
			balanceAccount : balanceAccount,//对账按钮
			changePageNumber : function(data){
				//listVue.pageNumber = data;
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
			resetSearch : resetSearch,//重置
			getEmptyForm: getEmptyForm,
			succClose: succClose,
			indexMehod: indexMehod,
			getBalanceAccount: getBalanceAccount,
			showBalanceAccountModal : showBalanceAccountModal,
			showGetBalanceAccountModal : showGetBalanceAccountModal,
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue
		},
		watch:{
			//pageNumber : refresh,
			selectItem : selectedItemChanged,
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
			theState:this.theState,
			userStartId:this.userStartId,
			userRecordId:this.userRecordId,
			reconciliationState : this.reconciliationState,
			billTimeStamp : this.billTimeStamp
		}
	}
	
	function indexMehod(index)
	{
		return generalIndexMethod(index, listVue);
	}
	
	function succClose()
	{
		$(baseInfo.waitDivId).modal('hide');
	}
	
	function getEmptyForm()
	{
		/*var selectItemList = listVue.selectItem;
		
		selectItemList = JSON.stringify(selectItemList);*/
		return {
			interfaceVersion:this.interfaceVersion,
			idArr:listVue.idArr,
			billTimeStamp : this.billTimeStamp
		}
	}
	
	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		listVue.isAllSelected = (listVue.tgpf_DayEndBalanceList.length > 0)
		&&	(listVue.selectItem.length == listVue.tgpf_DayEndBalanceList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgpf_DayEndBalanceList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgpf_DayEndBalanceList.forEach(function(item) {
	    		listVue.selectItem.push(item.tableId);
	    	});
	    }
	}
	//列表操作--------------刷新
	function refresh()
	{
		new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});
				$(baseInfo.errDivId).modal("show",{
					backdrop :'static'
				});
				listVue.errMsg1 = jsonObj.info;
			}
			else
			{
				listVue.tgpf_DayEndBalanceList=jsonObj.tgpf_DayEndBalancingList;
				listVue.tgpf_DayEndBalanceList.forEach(function(item){
					item.totalAmount = addThousands(item.totalAmount);
				});
				listVue.sumCount = jsonObj.sumCount;
				listVue.sumAmount = addThousands(jsonObj.sumAmount);
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalCount=jsonObj.totalCount;
				listVue.totalPage=jsonObj.totalPage;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tgpf_DayEndBalanceListDiv').scrollIntoView();
			}
		});
	}
	
	//列表操作------------选中复选框
	function checkCheckBox(val){
		// 获取选中需要修改的数据的tableId
		 listVue.idArr = [];
		var length = val.length;
		if(length > 0) {
			for(var i = 0; i < length; i++) {
				listVue.idArr.push(val[i].tableId);
				if(val[i].recordState == "0"){
					listVue.balanceDisabled = true;
					listVue.balanceEndDisabled = true;
					return;
				}else{
					listVue.balanceDisabled = false;
					listVue.balanceEndDisabled = false;
				}
			}
		}
	}
	
	//列表操作------------日终结算
	function balanceAccount(){
		$("#confirmDayEndBalanceList").modal('hide');
		new ServerInterface(baseInfo.dayEndBalancingStateInterface).execute(listVue.getEmptyForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});
				listVue.errMsg1 = jsonObj.info;
				$(baseInfo.errDivId).modal("show",{
					backdrop :'static'
				});
			}
			else
			{
				$(baseInfo.waitDivId).modal('show', {
				    backdrop :'static'
			   });
				refresh();
			}
		});
	}
	
	//列表操作------------搜索
	function search()
	{
		listVue.pageNumber = 1;
		refresh();
	}
	
	function showBalanceAccountModal(){
		$("#confirmDayEndBalanceList").modal({
			backdrop :'static'
		});
	}
	
	function showGetBalanceAccountModal(){
		$("#confirmDayEndBalanceListAuto").modal({
			backdrop :'static'
		});
	}
	// 手动日终结算
	function getBalanceAccount()
	{
		$("#confirmDayEndBalanceListAuto").modal('hide');
		$("#waitingModalDayEndBalance").modal({
			backdrop :'static'
		});
		new ServerInterface(baseInfo.dayEndBalancingState1Interface).execute(listVue.getEmptyForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
	            $("#waitingModalDayEndBalance").modal('hide');
				
				listVue.errMsg1 = jsonObj.info;
				$(baseInfo.errDivId).modal("show",{
					backdrop :'static'
				});
			}
			else
			{
                setTimeout(function(){ 
	            	$("#waitingModalDayEndBalance").modal('hide');
                }, 300);
                setTimeout(function(){ 
                	$(baseInfo.waitDivId).modal('show', {
    				    backdrop :'static'
    			   });
                }, 400);
				
				refresh();
			}
		});
	}
	  //根据按钮权限，动态控制，该页面的所有“可控按钮”的显示与否
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}
	function initData()
	{
		initButtonList();
	}
	//------------------------方法定义-结束------------------//
	/**
	 * 初始化日期插件
	 */
	laydate.render({
	  elem: '#dayEndBalanceResearch',
	  done:function(value, date, endDate){
	    listVue.billTimeStamp = value;
	  }
	});
	//列表操作--------------重置
	function resetSearch(){
		listVue.billTimeStamp = "";
		listVue.reconciliationState = "";
		
	}
	
	//------------------------数据初始化-开始----------------//
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tgpf_DayEndBalanceListDiv",
	"waitDivId":"#waitingModalDayEndBalanceList",
	"errDivId" : "#edModelDayEndBalanceList",
	"listInterface":"../Tgpf_DayEndBalancingList",
	"dayEndBalancingStateInterface" : "../Tgpf_DayEndBalancingStateUpdate",
	"dayEndBalancingState1Interface" : "../Tgpf_DayEndBalancingManualTask",
});
