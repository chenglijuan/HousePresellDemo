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
			projectId:null,
			projectList:[],
			buildingInfoId:null,
			buildingInfoList:[],
			unitInfoId:null,
			unitInfoList:[],
			houseInfoId:null,
			houseInfoList:[],
			selectedItem : [],
			deleteArr : [],
			settlementState : "",//结算状态
			tgxy_CoopAgreementSettleDtlList:[],
			editDisabled:true,//编辑按钮是否可点
			delDisabled:true,//删除按钮是否可点
			errMsg : "",
			fileUrl : "",
			settlementStateList :[
				{tableId:"0",theName:"未申请"},
				{tableId:"1",theName:"申请中"},
				{tableId:"2",theName:"已结算"},
			]
		},
		methods : {
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			handleSelectionChange : handleSelectionChange,
			tgxy_CoopAgreementSettleDtlDel : tgxy_CoopAgreementSettleDtlDel,
			getExportForm : getExportForm,
			search:search,
			resetSearch : resetSearch,//重置
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
			coopAgreementSettleDtlAddHandle : coopAgreementSettleDtlAddHandle,//点击新增按钮
			coopAgreementSettleDtlEditHandle : coopAgreementSettleDtlEditHandle,//点击编辑按钮
			coopAgreementSettleDtlDelHandle : coopAgreementSettleDtlDelHandle,//点击删除按钮
			coopAgreementSettleDtlDetailHandle : coopAgreementSettleDtlDetailHandle,//点击详情
			exportForm : exportForm,//导出excel
			changeSettlementState : function(data){
				this.settlementState = data.tableId;
			},
			settlementStateEmpty : function(){
				this.settlementState = null;
			}
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue,
			'vue-listselect' : VueListSelect,
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
			settlementState : this.settlementState,
		}
	}
	//列表操作--------------获取“删除资源”表单参数
	function getDeleteForm()
	{
		this.deleteArr = [];
		listVue.selectItem.forEach(function(item){
			listVue.deleteArr.push(item.tableId);
		})
		return{
			interfaceVersion:this.interfaceVersion,
			idArr:this.deleteArr
		}
	}
	
	function getExportForm(){
		return{
			interfaceVersion:this.interfaceVersion,
			idArr:this.selectItem
		}
	}
	//点击重置按钮
	function resetSearch(){
		listVue.keyword = "";
		listVue.settlementState = "";
		refresh();
	}
	//列表操作---------------选中复选框
	function handleSelectionChange(val){	
		//修改按钮禁用状态
		if(val.length != 0) {
	        if(val.length == 1){
	            listVue.editDisabled = false;
	            if(val[0].settlementState == 1 || val[0].settlementState == 2) {
	                listVue.editDisabled = true;
	            }
	        }else{
	            listVue.editDisabled = true;
	        }
	        //删除按钮禁用状态
	        if(val.length >= 1){
                listVue.delDisabled = false;
	        	if(val[0].settlementState == 1 || val[0].settlementState == 2) {
	                listVue.delDisabled = true;
	        	}
	        }else{
	        	listVue.delDisabled = true;
	        }
		}
		listVue.selectItem  = val;
	}
	//列表操作-------------------------点击新增按钮
	function coopAgreementSettleDtlAddHandle(){
		/*$("#tabContainer").data("tabs").addTab({
			id: 'Tgxy_CoopAgreementSettleDtlAdd', 
			text: '新增三方协议结算', 
			closeable: true, 
			url: 'tgxy/Tgxy_CoopAgreementSettleDtlAdd.shtml'
		});*/
		enterNewTab('', '新增三方协议结算', 'tgxy/Tgxy_CoopAgreementSettleDtlAdd.shtml');
	}
	//列表操作-------------------------点击修改按钮
	function coopAgreementSettleDtlEditHandle(){
		if(listVue.selectItem.length == "0"){
			noty({"text":"请至少选择一项数据进行修改","type":"error","timeout":2000});
		}else if(listVue.selectItem.length == "1"){
			/*$("#tabContainer").data("tabs").addTab({
				id: listVue.selectItem[0].tableId ,
				text: '三方协议结算修改', 
				closeable: true, 
				url: 'tgxy/Tgxy_CoopAgreementSettleDtlEdit.shtml'
			});*/
			enterNextTab(listVue.selectItem[0].tableId, '三方协议结算修改', 'tgxy/Tgxy_CoopAgreementSettleDtlEdit.shtml',listVue.selectItem[0].tableId+"06110304");
		}else{
			noty({"text":"修改数据只能单个进行修改","type":"error","timeout":2000});
		}
	}
	//列表操作-------------------------点击详情按钮
	function coopAgreementSettleDtlDetailHandle(tableId){
		/*$("#tabContainer").data("tabs").addTab({
			id: tableId ,
			text: '三方协议结算详情', 
			closeable: true, 
			url: 'tgxy/Tgxy_CoopAgreementSettleDtlDetail.shtml'
		});*/
		enterNextTab(tableId, '三方协议结算详情', 'tgxy/Tgxy_CoopAgreementSettleDtlDetail.shtml',tableId+"06110304");
	}
	//列表操作-------------------------点击详删除按钮
	function coopAgreementSettleDtlDelHandle(){
		$(baseInfo.deleteDivId).modal({
			backdrop :'static'
		});
	}
	
	function tgxy_CoopAgreementSettleDtlDel()
	{
		$(baseInfo.deleteDivId).modal("hide");
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
	

	
	//列表操作-------------------------点击详导出按钮
	function exportForm(){
		
		new ServerInterface(baseInfo.exportInterface).execute(listVue.getSearchForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				window.location.href="../" +jsonObj.fileURL
			}
		});
	}
	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		listVue.isAllSelected = (listVue.tgxy_CoopAgreementSettleDtlList.length > 0)
		&&	(listVue.selectItem.length == listVue.tgxy_CoopAgreementSettleDtlList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgxy_CoopAgreementSettleDtlList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgxy_CoopAgreementSettleDtlList.forEach(function(item) {
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
				listVue.tgxy_CoopAgreementSettleDtlList=jsonObj.tgxy_CoopAgreementSettleList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tgxy_CoopAgreementSettleDtlListDiv').scrollIntoView();
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
	
	//------------------------数据初始化-开始----------------//
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tgxy_CoopAgreementSettleDtlListDiv",
	"deleteDivId":"#deleteCoopAgreementSettleDtlList",
	"edModelDivId":"#edModelCoopAgreementSettleDtlList",
	"sdModelDivId":"#sdModelCoopAgreementSettleDtlList",
	"listInterface":"../Tgxy_CoopAgreementSettleList",
	"deleteInterface":"../Tgxy_CoopAgreementSettleBatchDelete",
	"exportInterface" : "../Tgxy_CoopAgreementSettleExcelList",
});
