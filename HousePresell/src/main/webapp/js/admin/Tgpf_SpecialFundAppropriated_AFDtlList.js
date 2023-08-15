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
			userUpdateId:null,
			userUpdateList:[],
			userRecordId:null,
			userRecordList:[],
			specialAppropriatedId:null,
			specialAppropriatedList:[],
			bankAccountEscrowedId:null,
			bankAccountEscrowedList:[],
			tgpf_SpecialFundAppropriated_AFDtlList:[]
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			tgpf_SpecialFundAppropriated_AFDtlDelOne : tgpf_SpecialFundAppropriated_AFDtlDelOne,
			tgpf_SpecialFundAppropriated_AFDtlDel : tgpf_SpecialFundAppropriated_AFDtlDel,
			showAjaxModal:showAjaxModal,
			search:search,
			checkAllClicked : checkAllClicked,
			changePageNumber : function(data){
				listVue.pageNumber = data;
			},
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
			eCode:null,
			userStartId:null,
			userStartList:[],
			createTimeStamp:null,
			userUpdateId:null,
			userUpdateList:[],
			lastUpdateTimeStamp:null,
			userRecordId:null,
			userRecordList:[],
			recordTimeStamp:null,
			specialAppropriatedId:null,
			specialAppropriatedList:[],
			theCodeOfAf:null,
			bankAccountEscrowedId:null,
			bankAccountEscrowedList:[],
			accountOfEscrowed:null,
			theNameOfEscrowed:null,
			applyRefundPayoutAmount:null,
			appliedAmount:null,
			accountBalance:null,
			billNumber:null,
			payoutChannel:null,
			payoutDate:null,
			payoutState:null,
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateTgpf_SpecialFundAppropriated_AFDtl
		}
	});
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			theState:null,
			busiState:null,
			eCode:null,
			userStartId:null,
			userStartList:[],
			createTimeStamp:null,
			userUpdateId:null,
			userUpdateList:[],
			lastUpdateTimeStamp:null,
			userRecordId:null,
			userRecordList:[],
			recordTimeStamp:null,
			specialAppropriatedId:null,
			specialAppropriatedList:[],
			theCodeOfAf:null,
			bankAccountEscrowedId:null,
			bankAccountEscrowedList:[],
			accountOfEscrowed:null,
			theNameOfEscrowed:null,
			applyRefundPayoutAmount:null,
			appliedAmount:null,
			accountBalance:null,
			billNumber:null,
			payoutChannel:null,
			payoutDate:null,
			payoutState:null,
		},
		methods : {
			getAddForm : getAddForm,
			add : addTgpf_SpecialFundAppropriated_AFDtl
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
			userUpdateId:this.userUpdateId,
			userRecordId:this.userRecordId,
			specialAppropriatedId:this.specialAppropriatedId,
			bankAccountEscrowedId:this.bankAccountEscrowedId,
		}
	}
	//列表操作--------------获取“删除资源”表单参数
	function getDeleteForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			idArr:this.selectItem
		}
	}
	//列表操作--------------获取“新增”表单参数
	function getAddForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			busiState:this.busiState,
			eCode:this.eCode,
			userStartId:this.userStartId,
			createTimeStamp:this.createTimeStamp,
			userUpdateId:this.userUpdateId,
			lastUpdateTimeStamp:this.lastUpdateTimeStamp,
			userRecordId:this.userRecordId,
			recordTimeStamp:this.recordTimeStamp,
			specialAppropriatedId:this.specialAppropriatedId,
			theCodeOfAf:this.theCodeOfAf,
			bankAccountEscrowedId:this.bankAccountEscrowedId,
			accountOfEscrowed:this.accountOfEscrowed,
			theNameOfEscrowed:this.theNameOfEscrowed,
			applyRefundPayoutAmount:this.applyRefundPayoutAmount,
			appliedAmount:this.appliedAmount,
			accountBalance:this.accountBalance,
			billNumber:this.billNumber,
			payoutChannel:this.payoutChannel,
			payoutDate:this.payoutDate,
			payoutState:this.payoutState,
		}
	}
	//列表操作--------------获取“更新”表单参数
	function getUpdateForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			theState:this.theState,
			busiState:this.busiState,
			eCode:this.eCode,
			userStartId:this.userStartId,
			createTimeStamp:this.createTimeStamp,
			userUpdateId:this.userUpdateId,
			lastUpdateTimeStamp:this.lastUpdateTimeStamp,
			userRecordId:this.userRecordId,
			recordTimeStamp:this.recordTimeStamp,
			specialAppropriatedId:this.specialAppropriatedId,
			theCodeOfAf:this.theCodeOfAf,
			bankAccountEscrowedId:this.bankAccountEscrowedId,
			accountOfEscrowed:this.accountOfEscrowed,
			theNameOfEscrowed:this.theNameOfEscrowed,
			applyRefundPayoutAmount:this.applyRefundPayoutAmount,
			appliedAmount:this.appliedAmount,
			accountBalance:this.accountBalance,
			billNumber:this.billNumber,
			payoutChannel:this.payoutChannel,
			payoutDate:this.payoutDate,
			payoutState:this.payoutState,
		}
	}
	function tgpf_SpecialFundAppropriated_AFDtlDel()
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
	function tgpf_SpecialFundAppropriated_AFDtlDelOne(tgpf_SpecialFundAppropriated_AFDtlId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[tgpf_SpecialFundAppropriated_AFDtlId],
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
		listVue.isAllSelected = (listVue.tgpf_SpecialFundAppropriated_AFDtlList.length > 0)
		&&	(listVue.selectItem.length == listVue.tgpf_SpecialFundAppropriated_AFDtlList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgpf_SpecialFundAppropriated_AFDtlList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgpf_SpecialFundAppropriated_AFDtlList.forEach(function(item) {
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
				listVue.tgpf_SpecialFundAppropriated_AFDtlList=jsonObj.tgpf_SpecialFundAppropriated_AFDtlList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tgpf_SpecialFundAppropriated_AFDtlListDiv').scrollIntoView();
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
	function showAjaxModal(tgpf_SpecialFundAppropriated_AFDtlModel)
	{
		//tgpf_SpecialFundAppropriated_AFDtlModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'tgpf_SpecialFundAppropriated_AFDtl', tgpf_SpecialFundAppropriated_AFDtlModel);
		//updateVue.$set("tgpf_SpecialFundAppropriated_AFDtl", tgpf_SpecialFundAppropriated_AFDtlModel);
		
		updateVue.theState = tgpf_SpecialFundAppropriated_AFDtlModel.theState;
		updateVue.busiState = tgpf_SpecialFundAppropriated_AFDtlModel.busiState;
		updateVue.eCode = tgpf_SpecialFundAppropriated_AFDtlModel.eCode;
		updateVue.userStartId = tgpf_SpecialFundAppropriated_AFDtlModel.userStartId;
		updateVue.createTimeStamp = tgpf_SpecialFundAppropriated_AFDtlModel.createTimeStamp;
		updateVue.userUpdateId = tgpf_SpecialFundAppropriated_AFDtlModel.userUpdateId;
		updateVue.lastUpdateTimeStamp = tgpf_SpecialFundAppropriated_AFDtlModel.lastUpdateTimeStamp;
		updateVue.userRecordId = tgpf_SpecialFundAppropriated_AFDtlModel.userRecordId;
		updateVue.recordTimeStamp = tgpf_SpecialFundAppropriated_AFDtlModel.recordTimeStamp;
		updateVue.specialAppropriatedId = tgpf_SpecialFundAppropriated_AFDtlModel.specialAppropriatedId;
		updateVue.theCodeOfAf = tgpf_SpecialFundAppropriated_AFDtlModel.theCodeOfAf;
		updateVue.bankAccountEscrowedId = tgpf_SpecialFundAppropriated_AFDtlModel.bankAccountEscrowedId;
		updateVue.accountOfEscrowed = tgpf_SpecialFundAppropriated_AFDtlModel.accountOfEscrowed;
		updateVue.theNameOfEscrowed = tgpf_SpecialFundAppropriated_AFDtlModel.theNameOfEscrowed;
		updateVue.applyRefundPayoutAmount = tgpf_SpecialFundAppropriated_AFDtlModel.applyRefundPayoutAmount;
		updateVue.appliedAmount = tgpf_SpecialFundAppropriated_AFDtlModel.appliedAmount;
		updateVue.accountBalance = tgpf_SpecialFundAppropriated_AFDtlModel.accountBalance;
		updateVue.billNumber = tgpf_SpecialFundAppropriated_AFDtlModel.billNumber;
		updateVue.payoutChannel = tgpf_SpecialFundAppropriated_AFDtlModel.payoutChannel;
		updateVue.payoutDate = tgpf_SpecialFundAppropriated_AFDtlModel.payoutDate;
		updateVue.payoutState = tgpf_SpecialFundAppropriated_AFDtlModel.payoutState;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateTgpf_SpecialFundAppropriated_AFDtl()
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
	function addTgpf_SpecialFundAppropriated_AFDtl()
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
				addVue.eCode = null;
				addVue.userStartId = null;
				addVue.createTimeStamp = null;
				addVue.userUpdateId = null;
				addVue.lastUpdateTimeStamp = null;
				addVue.userRecordId = null;
				addVue.recordTimeStamp = null;
				addVue.specialAppropriatedId = null;
				addVue.theCodeOfAf = null;
				addVue.bankAccountEscrowedId = null;
				addVue.accountOfEscrowed = null;
				addVue.theNameOfEscrowed = null;
				addVue.applyRefundPayoutAmount = null;
				addVue.appliedAmount = null;
				addVue.accountBalance = null;
				addVue.billNumber = null;
				addVue.payoutChannel = null;
				addVue.payoutDate = null;
				addVue.payoutState = null;
				refresh();
			}
		});
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
	"listDivId":"#tgpf_SpecialFundAppropriated_AFDtlListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Tgpf_SpecialFundAppropriated_AFDtlList",
	"addInterface":"../Tgpf_SpecialFundAppropriated_AFDtlAdd",
	"deleteInterface":"../Tgpf_SpecialFundAppropriated_AFDtlDelete",
	"updateInterface":"../Tgpf_SpecialFundAppropriated_AFDtlUpdate"
});
