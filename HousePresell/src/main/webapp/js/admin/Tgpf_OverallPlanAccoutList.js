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
			fundOverallPlanId:null,
			fundOverallPlanList:[],
			bankAccountEscrowedId:null,
			bankAccountEscrowedList:[],
			tgpf_OverallPlanAccoutList:[]
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			tgpf_OverallPlanAccoutDelOne : tgpf_OverallPlanAccoutDelOne,
			tgpf_OverallPlanAccoutDel : tgpf_OverallPlanAccoutDel,
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
			lastUpdateTimeStamp:null,
			userRecordId:null,
			userRecordList:[],
			recordTimeStamp:null,
			fundOverallPlanId:null,
			fundOverallPlanList:[],
			eCodeOfFundOverallPlan:null,
			bankAccountEscrowedId:null,
			bankAccountEscrowedList:[],
			eCodeOfAFWithdrawMainTable:null,
			overallPlanAmount:null,
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateTgpf_OverallPlanAccout
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
			lastUpdateTimeStamp:null,
			userRecordId:null,
			userRecordList:[],
			recordTimeStamp:null,
			fundOverallPlanId:null,
			fundOverallPlanList:[],
			eCodeOfFundOverallPlan:null,
			bankAccountEscrowedId:null,
			bankAccountEscrowedList:[],
			eCodeOfAFWithdrawMainTable:null,
			overallPlanAmount:null,
		},
		methods : {
			getAddForm : getAddForm,
			add : addTgpf_OverallPlanAccout
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
			fundOverallPlanId:this.fundOverallPlanId,
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
			lastUpdateTimeStamp:this.lastUpdateTimeStamp,
			userRecordId:this.userRecordId,
			recordTimeStamp:this.recordTimeStamp,
			fundOverallPlanId:this.fundOverallPlanId,
			eCodeOfFundOverallPlan:this.eCodeOfFundOverallPlan,
			bankAccountEscrowedId:this.bankAccountEscrowedId,
			eCodeOfAFWithdrawMainTable:this.eCodeOfAFWithdrawMainTable,
			overallPlanAmount:this.overallPlanAmount,
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
			lastUpdateTimeStamp:this.lastUpdateTimeStamp,
			userRecordId:this.userRecordId,
			recordTimeStamp:this.recordTimeStamp,
			fundOverallPlanId:this.fundOverallPlanId,
			eCodeOfFundOverallPlan:this.eCodeOfFundOverallPlan,
			bankAccountEscrowedId:this.bankAccountEscrowedId,
			eCodeOfAFWithdrawMainTable:this.eCodeOfAFWithdrawMainTable,
			overallPlanAmount:this.overallPlanAmount,
		}
	}
	function tgpf_OverallPlanAccoutDel()
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
	function tgpf_OverallPlanAccoutDelOne(tgpf_OverallPlanAccoutId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[tgpf_OverallPlanAccoutId],
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
		listVue.isAllSelected = (listVue.tgpf_OverallPlanAccoutList.length > 0)
		&&	(listVue.selectItem.length == listVue.tgpf_OverallPlanAccoutList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgpf_OverallPlanAccoutList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgpf_OverallPlanAccoutList.forEach(function(item) {
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
				listVue.tgpf_OverallPlanAccoutList=jsonObj.tgpf_OverallPlanAccoutList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tgpf_OverallPlanAccoutListDiv').scrollIntoView();
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
	function showAjaxModal(tgpf_OverallPlanAccoutModel)
	{
		//tgpf_OverallPlanAccoutModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'tgpf_OverallPlanAccout', tgpf_OverallPlanAccoutModel);
		//updateVue.$set("tgpf_OverallPlanAccout", tgpf_OverallPlanAccoutModel);
		
		updateVue.theState = tgpf_OverallPlanAccoutModel.theState;
		updateVue.busiState = tgpf_OverallPlanAccoutModel.busiState;
		updateVue.eCode = tgpf_OverallPlanAccoutModel.eCode;
		updateVue.userStartId = tgpf_OverallPlanAccoutModel.userStartId;
		updateVue.createTimeStamp = tgpf_OverallPlanAccoutModel.createTimeStamp;
		updateVue.lastUpdateTimeStamp = tgpf_OverallPlanAccoutModel.lastUpdateTimeStamp;
		updateVue.userRecordId = tgpf_OverallPlanAccoutModel.userRecordId;
		updateVue.recordTimeStamp = tgpf_OverallPlanAccoutModel.recordTimeStamp;
		updateVue.fundOverallPlanId = tgpf_OverallPlanAccoutModel.fundOverallPlanId;
		updateVue.eCodeOfFundOverallPlan = tgpf_OverallPlanAccoutModel.eCodeOfFundOverallPlan;
		updateVue.bankAccountEscrowedId = tgpf_OverallPlanAccoutModel.bankAccountEscrowedId;
		updateVue.eCodeOfAFWithdrawMainTable = tgpf_OverallPlanAccoutModel.eCodeOfAFWithdrawMainTable;
		updateVue.overallPlanAmount = tgpf_OverallPlanAccoutModel.overallPlanAmount;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateTgpf_OverallPlanAccout()
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
	function addTgpf_OverallPlanAccout()
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
				addVue.lastUpdateTimeStamp = null;
				addVue.userRecordId = null;
				addVue.recordTimeStamp = null;
				addVue.fundOverallPlanId = null;
				addVue.eCodeOfFundOverallPlan = null;
				addVue.bankAccountEscrowedId = null;
				addVue.eCodeOfAFWithdrawMainTable = null;
				addVue.overallPlanAmount = null;
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
	"listDivId":"#tgpf_OverallPlanAccoutListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Tgpf_OverallPlanAccoutList",
	"addInterface":"../Tgpf_OverallPlanAccoutAdd",
	"deleteInterface":"../Tgpf_OverallPlanAccoutDelete",
	"updateInterface":"../Tgpf_OverallPlanAccoutUpdate"
});
