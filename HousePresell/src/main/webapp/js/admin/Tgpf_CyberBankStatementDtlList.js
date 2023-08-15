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
			billTimeStamp:getNowFormatDate(),//记账日期
			reconciliationState:"",//对账状态
			tgpf_CyberBankStatementDtlList:[],
			cancelArr : [],//撤销的id
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			listItemSelectHandle: listItemSelectHandle,
			tgpf_CyberBankStatementDtlDelOne : tgpf_CyberBankStatementDtlDelOne,
			tgpf_CyberBankStatementDtlDel : tgpf_CyberBankStatementDtlDel,
			search:search,
			checkAllClicked : checkAllClicked,
			changePageNumber : function(data){
				listVue.pageNumber = data;
			},
			balanceAccount : balanceAccount,//网银对账自动对账
			checkCheckBox : checkCheckBox,//checkbox被选中
			cyberBankStatementDtlHandle : cyberBankStatementDtlHandle,//点击进入手动对账
			indexMethod : indexMethod,
			resetSearch : resetSearch,//重置
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
			keyword:this.keyword,
			theState:this.theState,
			userStartId:this.userStartId,
			userRecordId:this.userRecordId,
			mainTableId:this.mainTableId,
			reconciliationState : this.reconciliationState,
			billTimeStamp : this.billTimeStamp
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
		listVue.selectItem  = tableId;
	}
	
	//列表操作---------------------自动对账
	function balanceAccount(){
		if(listVue.selectItem.length == "0"){
			noty({"text":"请至少选择一项数据进行自动对账","type":"error","timeout":2000});
		}else{
			$(baseInfo.waitDivId).modal({
				backdrop :'static',
				keyboard: false
			});
			new ServerInterface(baseInfo.accountInterface).execute(listVue.getDeleteForm(), function(jsonObj){
				if(jsonObj.result != "success")
				{
					noty({"text":jsonObj.info,"type":"error","timeout":2000});
				}
				else
				{
					$(baseInfo.waitDivId).modal('hide');
					refresh();
				}
			});
		}
	}
	
	//列表操作---------------------撤销对账
	function tgpf_CyberBankStatementDtlDel()
	{
		noty({
			layout:'center',
			modal:true,
			text:"确认批量删除吗？",
			type:"confirm",
			buttons:[
				{
					addClass:"btn btn-primary",
					text:"确定",
					onClick:function($noty){
						$noty.close();
						new ServerInterface(baseInfo.deleteInterface).execute(listVue.getDeleteForm(), function(jsonObj){
							if(jsonObj.result != "success")
							{
								noty({"text":jsonObj.info,"type":"error","timeout":2000});
							}
							else
							{
								listVue.selectItem = [];
								refresh();
							}
						});
					}
				},
				{
					addClass:"btn btn-danger",
					text:"取消",
					onClick:function($noty){
						
						$noty.close();
					}
				}
			]
			
		});
	}
	function tgpf_CyberBankStatementDtlDelOne(tgpf_CyberBankStatementDtlId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[tgpf_CyberBankStatementDtlId],
		};
		
		noty({
			layout:'center',
			modal:true,
			text:"确认删除吗？",
			type:"confirm",
			buttons:[
				{
					addClass:"btn btn-primary",
					text:"确定",
					onClick:function($noty){
						$noty.close();
						new ServerInterface(baseInfo.deleteInterface).execute(model , function(jsonObj){
							if(jsonObj.result != "success")
							{
								noty({"text":jsonObj.info,"type":"error","timeout":2000});
							}
							else
							{
								refresh();
							}
						});
					}
				},
				{
					addClass:"btn btn-danger",
					text:"取消",
					onClick:function($noty){
						
						$noty.close();
					}
				}
			]
			
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
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				listVue.tgpf_CyberBankStatementDtlList=jsonObj.tgpf_BalanceOfAccountList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
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
		$("#tabContainer").data("tabs").addTab({
			id: tableId , 
			text: '网银对账详情', 
			closeable: true, 
			url: 'Tgpf_CyberBankStatementDtlDetail.shtml'
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

	function initData()
	{
		
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
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tgpf_CyberBankStatementDtlListDiv",
	"waitDivId":"#waitingModal",
	"listInterface":"../Tgpf_BalanceOfAccountBusContrastList",
	"addInterface":"../Tgpf_CyberBankStatementDtlAdd",
	"deleteInterface":"../tgpf_CyberBankStatementDtlCancel",
	"updateInterface":"../Tgpf_CyberBankStatementDtlUpdate",
	"accountInterface":"../tgpf_CyberBankStatementDtlCompareAll"
});
