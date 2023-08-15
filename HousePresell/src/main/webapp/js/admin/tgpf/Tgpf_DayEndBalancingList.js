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
			selectItem : [],
			isAllSelected : false,
			theState:0,//正常为0，删除为1
			userStartId:null,
			userStartList:[],
			userRecordId:null,
			userRecordList:[],
			tgpf_DayEndBalancingList:[],
			reconciliationState : "", //对账状态
			billTimeStamp:"",//记账日期
			cancelArr:[],
			accDisabled : true,
			reconciliationStateList : [
				{tableId:"0",theName:"未对账"},
				{tableId:"1",theName:"已对账"},
			],
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			search:search,
			checkAllClicked : checkAllClicked,
			checkCheckBox : checkCheckBox,
			balanceAccount : balanceAccount,//对账按钮
			tgpf_DayEndBalancingDel : tgpf_DayEndBalancingDel ,
			dayEndBalancingHandle : dayEndBalancingHandle,//点击进入人工对账
			indexMethod : indexMethod,
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
			exportForm : exportForm,//导出excel
			cancelBalanceAccount : cancelBalanceAccount,
			balanceAccountAll : balanceAccountAll,
			changeReconciliationState : function(data){
				listVue.reconciliationState = data.tableId;
			},
			changeReconciliationStateEmpty : function(){
				listVue.reconciliationState = null;
			}
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue,
			'vue-listselect': VueListSelect,
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
			keyword:this.keyword,
			theState:this.theState,
			userStartId:this.userStartId,
			userRecordId:this.userRecordId,
			reconciliationState : this.reconciliationState,
			billTimeStamp : this.billTimeStamp
		}
	}
	//列表操作--------------获取“删除资源”表单参数
	function getDeleteForm(){
		this.cancelArr = [];
		listVue.selectItem.forEach(function(item){
			listVue.cancelArr.push(item.tableId);
		});
		return{
			interfaceVersion:this.interfaceVersion,
			idArr:this.cancelArr
		}
	}
	
	function indexMethod(index) {
		return generalIndexMethod(index, listVue)
	}
	
	function cancelBalanceAccount(){
		$(baseInfo.deleteDivId).modal({
			backdrop : 'static'
		})
	}
	
	function tgpf_DayEndBalancingDel()
	{
		$(baseInfo.deleteDivId).modal('hide');
		new ServerInterface(baseInfo.deleteInterface).execute(listVue.getDeleteForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				generalSuccessModal();
				listVue.selectItem = [];
				refresh();
			}
		});
	}
	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		listVue.isAllSelected = (listVue.tgpf_DayEndBalancingList.length > 0)
		&&	(listVue.selectItem.length == listVue.tgpf_DayEndBalancingList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgpf_DayEndBalancingList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgpf_DayEndBalancingList.forEach(function(item) {
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
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				listVue.tgpf_DayEndBalancingList=jsonObj.tgpf_BalanceOfAccountList;
				listVue.tgpf_DayEndBalancingList.forEach(function(item){
					item.centerTotalAmount = addThousands(item.centerTotalAmount);
					item.bankTotalAmount = addThousands(item.bankTotalAmount);
				})
				listVue.billTimeStamp = jsonObj.billTimeStamp;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalCount=jsonObj.totalCount;
				listVue.totalPage=jsonObj.totalPage;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tgpf_DayEndBalancingListDiv').scrollIntoView();
			}
		});
	}
	//列表操作------------------获取撤销参数
	function getCancelForm(){
		return{
			interfaceVersion:this.interfaceVersion,
			cancelID:listVue.selectItem 
		}
	}
	
	
	//列表操作------------选中复选框
	function checkCheckBox(tableId){
		if(tableId.length !=0){
			listVue.accDisabled = false;
		}else if(tableId.length == 0){
			listVue.accDisabled = true;
		}
		listVue.selectItem  = tableId;
	}
	//列表操作------------对账按钮
	function balanceAccount(){
		if(listVue.selectItem.length == 0){
			$(baseInfo.balanceAccountDivId).modal({
				backdrop : 'static'
			})
		}else{
			$(baseInfo.waitDivId).modal({
				backdrop :'static',
				keyboard: false
			});
			new ServerInterface(baseInfo.compareInterface).execute(listVue.getDeleteForm(), function(jsonObj){
				if(jsonObj.result != "success")
				{
					$(baseInfo.waitDivId).modal('hide');
					generalErrorModal(jsonObj,jsonObj.info);
				}
				else
				{
					$(baseInfo.waitDivId).modal('hide');
					refresh();
				}
			});
		}
	}
	function balanceAccountAll(){
		$(baseInfo.balanceAccountDivId).modal('hide');
		$(baseInfo.waitDivId).modal({
			backdrop :'static',
			keyboard: false
		});
		var model={
				interfaceVersion:this.interfaceVersion,
				idArr:null,
				billTimeStamp : this.billTimeStamp
		}
		new ServerInterface(baseInfo.compareInterface).execute(model, function(jsonObj){
			if(jsonObj.result != "success")
			{
				$(baseInfo.waitDivId).modal('hide');
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				$(baseInfo.waitDivId).modal('hide');
				refresh();
			}
		});
	}
	//列表操作--------------点击进入人工对账
	function dayEndBalancingHandle(tableId){
		enterNextTab(tableId, '人工对账详情', 'tgpf/Tgpf_DayEndBalancingDetail.shtml',listVue.billTimeStamp);
		/*$("#tabContainer").data("tabs").addTab({
			id: tableId , 
			text: '人工对账详情', 
			closeable: true, 
			url: 'tgpf/Tgpf_DayEndBalancingDetail.shtml'
		});*/
	}
	
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
	  //根据按钮权限，动态控制，该页面的所有“可控按钮”的显示与否
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}
	function initData()
	{
		initButtonList();
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var array = tableIdStr.split("_");
		console.log(array[3]);
		if(array[3] != undefined){
			listVue.billTimeStamp = array[3];
		}else{
			listVue.billTimeStamp = "";
		}
	}
	//------------------------方法定义-结束------------------//
	/**
	 * 初始化日期插件
	 */
	laydate.render({
	  elem: '#dayEndBalanceingResearch',
	  done:function(value, date, endDate){
	    listVue.billTimeStamp = value;
	  }
	});
	//列表操作--------------重置
	function resetSearch(){
		listVue.billTimeStamp = "";
		listVue.keyword = "";
		listVue.reconciliationState = "";
		refresh();
		
	}
	
	//------------------------数据初始化-开始----------------//
	listVue.initData();
	listVue.refresh();
	
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tgpf_DayEndBalancingListDiv",
	"waitDivId":"#waitingModalDayEndBalancingList",
	"addDivId":"#addModal",
	"deleteDivId":"#deleteBalanceAccount",
	"balanceAccountDivId" : "#balanceAccountModal",
	"listInterface":"../Tgpf_BalanceOfAccountBusContrastList",
	"compareInterface" : "../Tgpf_BalanceOfAccountCompareList",
	"addInterface":"../Tgpf_DayEndBalancingAdd",
	"deleteInterface":"../Tgpf_BalanceOfAccountCancelList",
	"updateInterface":"../Tgpf_DayEndBalancingUpdate",
	"exportInterface" : "",//导出excel接口
});
