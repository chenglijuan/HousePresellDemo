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
			agentCompanyId:null,
			agentCompanyList:[],
			tgxy_CoopAgreementSettleList:[],
			errtips:'',
		},
		methods : {
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			listItemSelectHandle: listItemSelectHandle,
			tgxy_CoopAgreementSettleDelOne : tgxy_CoopAgreementSettleDelOne,
			tgxy_CoopAgreementSettleDel : tgxy_CoopAgreementSettleDel,
			Tgxy_CoopAgreementSettleAddHandle : Tgxy_CoopAgreementSettleAddHandle,
			Tgxy_CoopAgreementSettleDetailHandle : Tgxy_CoopAgreementSettleDetailHandle,
			Tgxy_CoopAgreementSettleEditHandle : Tgxy_CoopAgreementSettleEditHandle,
			checkCheckBox:checkCheckBox,
			showAjaxModal:showAjaxModal,
			search:search,
			checkAllClicked : checkAllClicked,
			changePageNumber: function (data) {
				console.log(data);
				if (listVue.pageNumber != data) {
					listVue.pageNumber = data;
					listVue.refresh();
				}
			},
			changeCountPerPage : function (data) {
				console.log(data);
				if (listVue.countPerPage != data) {
					listVue.countPerPage = data;
					listVue.refresh();
				}
			},
			exportExcel: exportExcel,
			showDelModal : showDelModal,
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue
		},
		watch:{
//			pageNumber : refresh,
			selectItem : selectedItemChanged,
		}
	});
	var updateVue = new Vue({
		el : baseInfo.updateDivId,
		data : {
			interfaceVersion :19000101,
			theState:null,
			busiState:null,
			userStartId:null,
			userStartList:[],
			createTimeStamp:null,
			lastUpdateTimeStamp:null,
			userRecordId:null,
			userRecordList:[],
			recordTimeStamp:null,
			eCode:null,
			signTimeStamp:null,
			agentCompanyId:null,
			agentCompanyList:[],
			applySettlementDate:null,
			startSettlementDate:null,
			endSettlementDate:null,
			protocolNumbers:null,
			settlementState:null,
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateTgxy_CoopAgreementSettle
		}
	});
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			theState:null,
			busiState:null,
			userStartId:null,
			userStartList:[],
			createTimeStamp:null,
			lastUpdateTimeStamp:null,
			userRecordId:null,
			userRecordList:[],
			recordTimeStamp:null,
			eCode:null,
			signTimeStamp:null,
			agentCompanyId:null,
			agentCompanyList:[],
			applySettlementDate:null,
			startSettlementDate:null,
			endSettlementDate:null,
			protocolNumbers:null,
			settlementState:null,
		},
		methods : {
			getAddForm : getAddForm,
			add : addTgxy_CoopAgreementSettle
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
			agentCompanyId:this.agentCompanyId,
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
	//列表操作--------------获取“新增”表单参数
	function getAddForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			busiState:this.busiState,
			userStartId:this.userStartId,
			createTimeStamp:this.createTimeStamp,
			lastUpdateTimeStamp:this.lastUpdateTimeStamp,
			userRecordId:this.userRecordId,
			recordTimeStamp:this.recordTimeStamp,
			eCode:this.eCode,
			signTimeStamp:this.signTimeStamp,
			agentCompanyId:this.agentCompanyId,
			applySettlementDate:this.applySettlementDate,
			startSettlementDate:this.startSettlementDate,
			endSettlementDate:this.endSettlementDate,
			protocolNumbers:this.protocolNumbers,
			settlementState:this.settlementState,
		}
	}
	//列表操作--------------获取“更新”表单参数
	function getUpdateForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			theState:this.theState,
			busiState:this.busiState,
			userStartId:this.userStartId,
			createTimeStamp:this.createTimeStamp,
			lastUpdateTimeStamp:this.lastUpdateTimeStamp,
			userRecordId:this.userRecordId,
			recordTimeStamp:this.recordTimeStamp,
			eCode:this.eCode,
			signTimeStamp:this.signTimeStamp,
			agentCompanyId:this.agentCompanyId,
			applySettlementDate:this.applySettlementDate,
			startSettlementDate:this.startSettlementDate,
			endSettlementDate:this.endSettlementDate,
			protocolNumbers:this.protocolNumbers,
			settlementState:this.settlementState,
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
	function tgxy_CoopAgreementSettleDel()
	{
		new ServerInterface(baseInfo.batchDeleteInterface).execute(listVue.getDeleteForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
//				noty({"text":jsonObj.info,"type":"error","timeout":2000});
				listVue.errtips = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
					backdrop :'static'
				 });
			}
			else
			{
				listVue.selectItem = [];
				refresh();
			}
		});
	}
	function tgxy_CoopAgreementSettleDelOne(tgxy_CoopAgreementSettleId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[tgxy_CoopAgreementSettleId],
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
	function checkCheckBox(tableId){
		listVue.selectItem  = tableId;
	}
	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		listVue.isAllSelected = (listVue.tgxy_CoopAgreementSettleList.length > 0)
		&&	(listVue.selectItem.length == listVue.tgxy_CoopAgreementSettleList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgxy_CoopAgreementSettleList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgxy_CoopAgreementSettleList.forEach(function(item) {
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
//				noty({"text":jsonObj.info,"type":"error","timeout":2000});
				listVue.errtips = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
					backdrop :'static'
				 });
			}
			else
			{
				listVue.tgxy_CoopAgreementSettleList=jsonObj.tgxy_CoopAgreementSettleList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tgxy_CoopAgreementSettleListDiv').scrollIntoView();
			}
		});
	}
	
	//列表操作------------搜索
	function search()
	{
		listVue.pageNumber = 1;
		refresh();
	}
	
	//弹出编辑模态框--更新操作
	function showAjaxModal(tgxy_CoopAgreementSettleModel)
	{
		//tgxy_CoopAgreementSettleModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'tgxy_CoopAgreementSettle', tgxy_CoopAgreementSettleModel);
		//updateVue.$set("tgxy_CoopAgreementSettle", tgxy_CoopAgreementSettleModel);
		
		updateVue.theState = tgxy_CoopAgreementSettleModel.theState;
		updateVue.busiState = tgxy_CoopAgreementSettleModel.busiState;
		updateVue.userStartId = tgxy_CoopAgreementSettleModel.userStartId;
		updateVue.createTimeStamp = tgxy_CoopAgreementSettleModel.createTimeStamp;
		updateVue.lastUpdateTimeStamp = tgxy_CoopAgreementSettleModel.lastUpdateTimeStamp;
		updateVue.userRecordId = tgxy_CoopAgreementSettleModel.userRecordId;
		updateVue.recordTimeStamp = tgxy_CoopAgreementSettleModel.recordTimeStamp;
		updateVue.eCode = tgxy_CoopAgreementSettleModel.eCode;
		updateVue.signTimeStamp = tgxy_CoopAgreementSettleModel.signTimeStamp;
		updateVue.agentCompanyId = tgxy_CoopAgreementSettleModel.agentCompanyId;
		updateVue.applySettlementDate = tgxy_CoopAgreementSettleModel.applySettlementDate;
		updateVue.startSettlementDate = tgxy_CoopAgreementSettleModel.startSettlementDate;
		updateVue.endSettlementDate = tgxy_CoopAgreementSettleModel.endSettlementDate;
		updateVue.protocolNumbers = tgxy_CoopAgreementSettleModel.protocolNumbers;
		updateVue.settlementState = tgxy_CoopAgreementSettleModel.settlementState;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateTgxy_CoopAgreementSettle()
	{
		new ServerInterface(baseInfo.updateInterface).execute(updateVue.getUpdateForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				$(baseInfo.updateDivId).modal('hide');
				refresh();
			}
		});
	}
	function addTgxy_CoopAgreementSettle()
	{
		new ServerInterface(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				$(baseInfo.addDivId).modal('hide');
				addVue.busiState = null;
				addVue.userStartId = null;
				addVue.createTimeStamp = null;
				addVue.lastUpdateTimeStamp = null;
				addVue.userRecordId = null;
				addVue.recordTimeStamp = null;
				addVue.eCode = null;
				addVue.signTimeStamp = null;
				addVue.agentCompanyId = null;
				addVue.applySettlementDate = null;
				addVue.startSettlementDate = null;
				addVue.endSettlementDate = null;
				addVue.protocolNumbers = null;
				addVue.settlementState = null;
				refresh();
			}
		});
	}
	//跳转新增三方协议结算方法
	function Tgxy_CoopAgreementSettleAddHandle() {
		$("#tabContainer").data("tabs").addTab({
			id: 'Tgpf_RefundInfoAdd', 
			text: '新增三方协议结算', 
			closeable: true, 
			url: 'tgxy/Tgxy_CoopAgreementSettleAdd.shtml'
		});
	}
	
	//跳转新增三方协议结算详情信息方法
	function Tgxy_CoopAgreementSettleDetailHandle(tableId) {
		$("#tabContainer").data("tabs").addTab({
			id: tableId , 
			text: '三方协议结算详情信息', 
			closeable: true, 
			url: 'tgxy/Tgxy_CoopAgreementSettleDetail.shtml'
		});
	}
	
	//跳转修改
	function Tgxy_CoopAgreementSettleEditHandle(){
		if(listVue.selectItem.length == "0"){
			noty({"text":"请至少选择一项数据进行修改","type":"error","timeout":2000});
		}else if(listVue.selectItem.length == "1"){
			$("#tabContainer").data("tabs").addTab({
				id: listVue.selectItem[0].tableId ,
				text: '修改三方协议结算信息', 
				closeable: true, 
				url: 'tgxy/Tgxy_CoopAgreementSettleEdit.shtml'
			});
		}else{
			noty({"text":"修改数据只能单个进行修改","type":"error","timeout":2000});
		}
		
	}
	// 导出--------数据导出为Excel
	function exportExcel() {
		flag = true;
		// 获取需要导出的table的dom节点
		var wb = XLSX.utils.table_to_book(document.querySelector('#CoopAgreementSettleTab'));
		// 写入列表数据并保存
		if(flag){
		    var wbout = XLSX.write(wb, { bookType: 'xlsx', bookSST: true, type: 'array' });
			flag = false;
		}
		console.log(wbout);
		
		try {
			// 导出
			saveAs(new Blob([wbout], { type: 'application/octet-stream' }), '三方协议结算.xlsx');
		} catch(e) {
			if(typeof console !== 'undefined') console.log(e, wbout)
		}
		return wbout
	}
	function indexMethod(index) {
		return generalIndexMethod(index, listVue)
	}

	function listItemSelectHandle(list) {
		generalListItemSelectHandle(listVue,list)
 	}

	function initData()
	{
		
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tgxy_CoopAgreementSettleListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"deleteDivId":"#delete",
	"errorModel":"#errorEscrowAdd",
	"successModel":"#successEscrowAdd",
	"listInterface":"../Tgxy_CoopAgreementSettleList",
	"addInterface":"../Tgxy_CoopAgreementSettleAdd",
	"batchDeleteInterface":"../Tgxy_CoopAgreementSettleBatchDelete",
	"updateInterface":"../Tgxy_CoopAgreementSettleUpdate"
});
