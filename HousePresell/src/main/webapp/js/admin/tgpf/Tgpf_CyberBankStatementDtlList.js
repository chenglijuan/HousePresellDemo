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
			mainTableId:null,
			mainTableList:[],
			//billTimeStamp:getNowFormatDate(),//记账日期
			billTimeStamp : "",
			reconciliationState:"",//对账状态
			tgpf_CyberBankStatementDtlList:[],
			cancelArr : [],//撤销的id
			errMsg : "",
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
			listItemSelectHandle: listItemSelectHandle,
			tgpf_CyberBankStatementDtlDel : tgpf_CyberBankStatementDtlDel,
			search:search,
			checkAllClicked : checkAllClicked,
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
			balanceAccount : balanceAccount,//网银对账自动对账
			checkCheckBox : checkCheckBox,//checkbox被选中
			cyberBankStatementDtlHandle : cyberBankStatementDtlHandle,//点击进入手动对账
			indexMethod : indexMethod,
			resetSearch : resetSearch,//重置
			showDelModal : showDelModal,
			exportForm : exportForm,
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
			mainTableId:this.mainTableId,
			theType : this.reconciliationState,
			billTimeStamp : this.billTimeStamp,
			accountType:"1",
		}
	}
	//列表操作--------------获取“删除资源”表单参数
	function getDeleteForm()
	{
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

	//列表操作------------选中复选框
	function checkCheckBox(tableId){
		if(tableId.length !=0){
			listVue.accDisabled = false;
		}
		listVue.selectItem  = tableId;
	}
	
	function balanceAccountAll(){
		$(baseInfo.accountCyberBankStatementDivId).modal('hide');
		$(baseInfo.waitDivId).modal({
			backdrop :'static',
			keyboard: false
		});
		var model={
				interfaceVersion:this.interfaceVersion,
				idArr:null,
				billTimeStamp : this.billTimeStamp
		}
		new ServerInterface(baseInfo.accountInterface).execute(model, function(jsonObj){
			if(jsonObj.result != "success")
			{
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});
				$(baseInfo.waitDivId).modal('hide');
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				listVue.errMsg = jsonObj.info;
			}
			else
			{
				$(baseInfo.waitDivId).modal('hide');
				refresh();
			}
		});
	}
	//列表操作---------------------自动对账
	function balanceAccount(){
		if(listVue.selectItem == 0){
			$(baseInfo.accountCyberBankStatementDivId).modal({
				backdrop : 'static'
			})
		}else{
			$(baseInfo.waitDivId).modal({
				backdrop :'static',
				keyboard: false
			});
			new ServerInterface(baseInfo.accountInterface).execute(listVue.getDeleteForm(), function(jsonObj){
				if(jsonObj.result != "success")
				{
					//noty({"text":jsonObj.info,"type":"error","timeout":2000});
					$(baseInfo.waitDivId).modal('hide');
					$(baseInfo.edModelDivId).modal({
						backdrop :'static'
					});
					listVue.errMsg = jsonObj.info;
				}
				else
				{
					$(baseInfo.waitDivId).modal('hide');
					refresh();
				}
			});
		}
	}
	
	function showDelModal(){
		if(listVue.getDeleteForm().idArr.length == "0"){
			noty({"text":"请至少选择一项进行删除","type":"error","timeout":2000});
		}else{
			$(baseInfo.deleteDivId).modal({
				backdrop :'static'
			});
		}
	}
	
	//列表操作---------------------撤销对账
	function tgpf_CyberBankStatementDtlDel()
	{
		new ServerInterface(baseInfo.deleteInterface).execute(listVue.getDeleteForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				listVue.errMsg = jsonObj.info;
			}
			else
			{
				$(baseInfo.sdModelDivId).modal({
					backdrop :'static'
				});
				listVue.selectItem = [];
				refresh();
			}
		});
	}
	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		listVue.isAllSelected = (listVue.tgpf_CyberBankStatementDtlList.length > 0)
		&&	(listVue.selectItem.length == listVue.tgpf_CyberBankStatementDtlList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgpf_CyberBankStatementDtlList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgpf_CyberBankStatementDtlList.forEach(function(item) {
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
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});\n
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				listVue.errMsg = jsonObj.info;
			}
			else
			{
				listVue.tgpf_CyberBankStatementDtlList=jsonObj.tgpf_BalanceOfAccountList;
				listVue.tgpf_CyberBankStatementDtlList.forEach(function(item){
					item.cyberBankTotalAmount = addThousands(item.cyberBankTotalAmount);
					item.centerTotalAmount = addThousands(item.centerTotalAmount);
				});
				listVue.billTimeStamp=jsonObj.billTimeStamp;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount=jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tgpf_CyberBankStatementDtlListDiv').scrollIntoView();
			}
		});
	}
	function getNowFormatDate() {
	    var date = new Date();
	    var seperator1 = "-";
	    var seperator2 = ":";
	    var month = date.getMonth() + 1;
	    var strDate = date.getDate();
	    if (month >= 1 && month <= 9) {
	        month = "0" + month;
	    }
	    if (strDate >= 0 && strDate <= 9) {
	        strDate = "0" + strDate;
	    }
	    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate;
	  return currentdate;
	
	}
	//列表操作---------------点击进入人工对账
	function cyberBankStatementDtlHandle(tableId){
		enterNextTab(tableId, '网银对账详情', 'tgpf/Tgpf_CyberBankStatementDtlDetail.shtml',listVue.billTimeStamp);
		/*$("#tabContainer").data("tabs").addTab({
			id: tableId , 
			text: '网银对账详情', 
			closeable: true, 
			url: 'tgpf/Tgpf_CyberBankStatementDtlDetail.shtml'
		});*/
	}
	
	//导出excel
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
	function listItemSelectHandle(list) {
		generalListItemSelectHandle(listVue,list)
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
		if(array[3] != undefined){
			listVue.billTimeStamp = array[3];
		}
	}
	
	
	/**
	 * 初始化日期插件
	 */
	laydate.render({
	  elem: '#accountDateSearch',
	  done: function(value, date, endDate){
	    listVue.billTimeStamp = value;
	  }
	});
	
	function resetSearch(){
		listVue.billTimeStamp = getNowFormatDate();
		listVue.reconciliationState = "";
		listVue.keyword = "";
		refresh();
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.initData();
	//listVue.refresh();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tgpf_CyberBankStatementDtlListDiv",
	"waitDivId":"#waitingModalCyberBankStatementDtlList",
	"accountCyberBankStatementDivId" : "#accountCyberBankStatementDtlList",
	"deleteDivId":"#delete",
	"edModelDivId":"#edCyberBankModel",
	"sdModelDivId":"#sdCyberBankModel",
	"listInterface":"../Tgpf_BalanceOfAccountBusContrastList",
	"addInterface":"../Tgpf_CyberBankStatementDtlAdd",
	"deleteInterface":"../tgpf_CyberBankStatementDtlCancel",
	"updateInterface":"../Tgpf_CyberBankStatementDtlUpdate",
	"accountInterface":"../tgpf_CyberBankStatementDtlCompareAll",
	"exportInterface" : "",//导出excel接口
});
