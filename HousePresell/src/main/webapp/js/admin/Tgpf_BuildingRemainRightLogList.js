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
			projectId:null,
			projectList:[],
			buildingId:null,
			buildingList:[],
			buildingAccountId:null,
			buildingAccountList:[],
			buildingExtendInfoId:null,
			buildingExtendInfoList:[],
			tgpf_BuildingRemainRightLogList:[]
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			tgpf_BuildingRemainRightLogDelOne : tgpf_BuildingRemainRightLogDelOne,
			tgpf_BuildingRemainRightLogDel : tgpf_BuildingRemainRightLogDel,
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
			projectId:null,
			projectList:[],
			theNameOfProject:null,
			eCodeOfProject:null,
			buildingId:null,
			buildingList:[],
			eCodeFromConstruction:null,
			eCodeFromPublicSecurity:null,
			buildingAccountId:null,
			buildingAccountList:[],
			buildingExtendInfoId:null,
			buildingExtendInfoList:[],
			currentFigureProgress:null,
			currentLimitedRatio:null,
			nodeLimitedAmount:null,
			totalAccountAmount:null,
			billTimeStamp:null,
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateTgpf_BuildingRemainRightLog
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
			projectId:null,
			projectList:[],
			theNameOfProject:null,
			eCodeOfProject:null,
			buildingId:null,
			buildingList:[],
			eCodeFromConstruction:null,
			eCodeFromPublicSecurity:null,
			buildingAccountId:null,
			buildingAccountList:[],
			buildingExtendInfoId:null,
			buildingExtendInfoList:[],
			currentFigureProgress:null,
			currentLimitedRatio:null,
			nodeLimitedAmount:null,
			totalAccountAmount:null,
			billTimeStamp:null,
		},
		methods : {
			getAddForm : getAddForm,
			add : addTgpf_BuildingRemainRightLog
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
			projectId:this.projectId,
			buildingId:this.buildingId,
			buildingAccountId:this.buildingAccountId,
			buildingExtendInfoId:this.buildingExtendInfoId,
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
			projectId:this.projectId,
			theNameOfProject:this.theNameOfProject,
			eCodeOfProject:this.eCodeOfProject,
			buildingId:this.buildingId,
			eCodeFromConstruction:this.eCodeFromConstruction,
			eCodeFromPublicSecurity:this.eCodeFromPublicSecurity,
			buildingAccountId:this.buildingAccountId,
			buildingExtendInfoId:this.buildingExtendInfoId,
			currentFigureProgress:this.currentFigureProgress,
			currentLimitedRatio:this.currentLimitedRatio,
			nodeLimitedAmount:this.nodeLimitedAmount,
			totalAccountAmount:this.totalAccountAmount,
			billTimeStamp:this.billTimeStamp,
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
			projectId:this.projectId,
			theNameOfProject:this.theNameOfProject,
			eCodeOfProject:this.eCodeOfProject,
			buildingId:this.buildingId,
			eCodeFromConstruction:this.eCodeFromConstruction,
			eCodeFromPublicSecurity:this.eCodeFromPublicSecurity,
			buildingAccountId:this.buildingAccountId,
			buildingExtendInfoId:this.buildingExtendInfoId,
			currentFigureProgress:this.currentFigureProgress,
			currentLimitedRatio:this.currentLimitedRatio,
			nodeLimitedAmount:this.nodeLimitedAmount,
			totalAccountAmount:this.totalAccountAmount,
			billTimeStamp:this.billTimeStamp,
		}
	}
	function tgpf_BuildingRemainRightLogDel()
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
	function tgpf_BuildingRemainRightLogDelOne(tgpf_BuildingRemainRightLogId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[tgpf_BuildingRemainRightLogId],
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
		listVue.isAllSelected = (listVue.tgpf_BuildingRemainRightLogList.length > 0)
		&&	(listVue.selectItem.length == listVue.tgpf_BuildingRemainRightLogList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgpf_BuildingRemainRightLogList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgpf_BuildingRemainRightLogList.forEach(function(item) {
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
				listVue.tgpf_BuildingRemainRightLogList=jsonObj.tgpf_BuildingRemainRightLogList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tgpf_BuildingRemainRightLogListDiv').scrollIntoView();
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
	function showAjaxModal(tgpf_BuildingRemainRightLogModel)
	{
		//tgpf_BuildingRemainRightLogModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'tgpf_BuildingRemainRightLog', tgpf_BuildingRemainRightLogModel);
		//updateVue.$set("tgpf_BuildingRemainRightLog", tgpf_BuildingRemainRightLogModel);
		
		updateVue.theState = tgpf_BuildingRemainRightLogModel.theState;
		updateVue.busiState = tgpf_BuildingRemainRightLogModel.busiState;
		updateVue.eCode = tgpf_BuildingRemainRightLogModel.eCode;
		updateVue.userStartId = tgpf_BuildingRemainRightLogModel.userStartId;
		updateVue.createTimeStamp = tgpf_BuildingRemainRightLogModel.createTimeStamp;
		updateVue.userUpdateId = tgpf_BuildingRemainRightLogModel.userUpdateId;
		updateVue.lastUpdateTimeStamp = tgpf_BuildingRemainRightLogModel.lastUpdateTimeStamp;
		updateVue.userRecordId = tgpf_BuildingRemainRightLogModel.userRecordId;
		updateVue.recordTimeStamp = tgpf_BuildingRemainRightLogModel.recordTimeStamp;
		updateVue.projectId = tgpf_BuildingRemainRightLogModel.projectId;
		updateVue.theNameOfProject = tgpf_BuildingRemainRightLogModel.theNameOfProject;
		updateVue.eCodeOfProject = tgpf_BuildingRemainRightLogModel.eCodeOfProject;
		updateVue.buildingId = tgpf_BuildingRemainRightLogModel.buildingId;
		updateVue.eCodeFromConstruction = tgpf_BuildingRemainRightLogModel.eCodeFromConstruction;
		updateVue.eCodeFromPublicSecurity = tgpf_BuildingRemainRightLogModel.eCodeFromPublicSecurity;
		updateVue.buildingAccountId = tgpf_BuildingRemainRightLogModel.buildingAccountId;
		updateVue.buildingExtendInfoId = tgpf_BuildingRemainRightLogModel.buildingExtendInfoId;
		updateVue.currentFigureProgress = tgpf_BuildingRemainRightLogModel.currentFigureProgress;
		updateVue.currentLimitedRatio = tgpf_BuildingRemainRightLogModel.currentLimitedRatio;
		updateVue.nodeLimitedAmount = tgpf_BuildingRemainRightLogModel.nodeLimitedAmount;
		updateVue.totalAccountAmount = tgpf_BuildingRemainRightLogModel.totalAccountAmount;
		updateVue.billTimeStamp = tgpf_BuildingRemainRightLogModel.billTimeStamp;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateTgpf_BuildingRemainRightLog()
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
	function addTgpf_BuildingRemainRightLog()
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
				addVue.projectId = null;
				addVue.theNameOfProject = null;
				addVue.eCodeOfProject = null;
				addVue.buildingId = null;
				addVue.eCodeFromConstruction = null;
				addVue.eCodeFromPublicSecurity = null;
				addVue.buildingAccountId = null;
				addVue.buildingExtendInfoId = null;
				addVue.currentFigureProgress = null;
				addVue.currentLimitedRatio = null;
				addVue.nodeLimitedAmount = null;
				addVue.totalAccountAmount = null;
				addVue.billTimeStamp = null;
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
	"listDivId":"#tgpf_BuildingRemainRightLogListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Tgpf_BuildingRemainRightLogList",
	"addInterface":"../Tgpf_BuildingRemainRightLogAdd",
	"deleteInterface":"../Tgpf_BuildingRemainRightLogDelete",
	"updateInterface":"../Tgpf_BuildingRemainRightLogUpdate"
});
