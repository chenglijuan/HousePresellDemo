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
			sm_eCode_LogList:[]
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			sm_eCode_LogDelOne : sm_eCode_LogDelOne,
			sm_eCode_LogDel : sm_eCode_LogDel,
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
			recordState:null,
			recordRejectReason:null,
			busiCode:null,
			theYear:null,
			theMonth:null,
			theDay:null,
			ticketCount:null,
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateSm_eCode_Log
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
			recordState:null,
			recordRejectReason:null,
			busiCode:null,
			theYear:null,
			theMonth:null,
			theDay:null,
			ticketCount:null,
		},
		methods : {
			getAddForm : getAddForm,
			add : addSm_eCode_Log
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
			recordState:this.recordState,
			recordRejectReason:this.recordRejectReason,
			busiCode:this.busiCode,
			theYear:this.theYear,
			theMonth:this.theMonth,
			theDay:this.theDay,
			ticketCount:this.ticketCount,
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
			recordState:this.recordState,
			recordRejectReason:this.recordRejectReason,
			busiCode:this.busiCode,
			theYear:this.theYear,
			theMonth:this.theMonth,
			theDay:this.theDay,
			ticketCount:this.ticketCount,
		}
	}
	function sm_eCode_LogDel()
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
	function sm_eCode_LogDelOne(sm_eCode_LogId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[sm_eCode_LogId],
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
		listVue.isAllSelected = (listVue.sm_eCode_LogList.length > 0)
		&&	(listVue.selectItem.length == listVue.sm_eCode_LogList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.sm_eCode_LogList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.sm_eCode_LogList.forEach(function(item) {
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
				listVue.sm_eCode_LogList=jsonObj.sm_eCode_LogList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('sm_eCode_LogListDiv').scrollIntoView();
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
	function showAjaxModal(sm_eCode_LogModel)
	{
		//sm_eCode_LogModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'sm_eCode_Log', sm_eCode_LogModel);
		//updateVue.$set("sm_eCode_Log", sm_eCode_LogModel);
		
		updateVue.theState = sm_eCode_LogModel.theState;
		updateVue.busiState = sm_eCode_LogModel.busiState;
		updateVue.eCode = sm_eCode_LogModel.eCode;
		updateVue.userStartId = sm_eCode_LogModel.userStartId;
		updateVue.createTimeStamp = sm_eCode_LogModel.createTimeStamp;
		updateVue.userUpdateId = sm_eCode_LogModel.userUpdateId;
		updateVue.lastUpdateTimeStamp = sm_eCode_LogModel.lastUpdateTimeStamp;
		updateVue.userRecordId = sm_eCode_LogModel.userRecordId;
		updateVue.recordTimeStamp = sm_eCode_LogModel.recordTimeStamp;
		updateVue.recordState = sm_eCode_LogModel.recordState;
		updateVue.recordRejectReason = sm_eCode_LogModel.recordRejectReason;
		updateVue.busiCode = sm_eCode_LogModel.busiCode;
		updateVue.theYear = sm_eCode_LogModel.theYear;
		updateVue.theMonth = sm_eCode_LogModel.theMonth;
		updateVue.theDay = sm_eCode_LogModel.theDay;
		updateVue.ticketCount = sm_eCode_LogModel.ticketCount;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateSm_eCode_Log()
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
	function addSm_eCode_Log()
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
				addVue.recordState = null;
				addVue.recordRejectReason = null;
				addVue.busiCode = null;
				addVue.theYear = null;
				addVue.theMonth = null;
				addVue.theDay = null;
				addVue.ticketCount = null;
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
	"listDivId":"#sm_eCode_LogListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Sm_eCode_LogList",
	"addInterface":"../Sm_eCode_LogAdd",
	"deleteInterface":"../Sm_eCode_LogDelete",
	"updateInterface":"../Sm_eCode_LogUpdate"
});
