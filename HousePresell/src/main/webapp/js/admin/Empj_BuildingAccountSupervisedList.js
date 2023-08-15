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
			buildingInfoId:null,
			buildingInfoList:[],
			bankAccountSupervisedId:null,
			bankAccountSupervisedList:[],
			empj_BuildingAccountSupervisedList:[]
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			empj_BuildingAccountSupervisedDelOne : empj_BuildingAccountSupervisedDelOne,
			empj_BuildingAccountSupervisedDel : empj_BuildingAccountSupervisedDel,
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
			buildingInfoId:null,
			buildingInfoList:[],
			bankAccountSupervisedId:null,
			bankAccountSupervisedList:[],
			beginTimeStamp:null,
			endTimeStamp:null,
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateEmpj_BuildingAccountSupervised
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
			buildingInfoId:null,
			buildingInfoList:[],
			bankAccountSupervisedId:null,
			bankAccountSupervisedList:[],
			beginTimeStamp:null,
			endTimeStamp:null,
		},
		methods : {
			getAddForm : getAddForm,
			add : addEmpj_BuildingAccountSupervised
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
			buildingInfoId:this.buildingInfoId,
			bankAccountSupervisedId:this.bankAccountSupervisedId,
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
			buildingInfoId:this.buildingInfoId,
			bankAccountSupervisedId:this.bankAccountSupervisedId,
			beginTimeStamp:this.beginTimeStamp,
			endTimeStamp:this.endTimeStamp,
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
			buildingInfoId:this.buildingInfoId,
			bankAccountSupervisedId:this.bankAccountSupervisedId,
			beginTimeStamp:this.beginTimeStamp,
			endTimeStamp:this.endTimeStamp,
		}
	}
	function empj_BuildingAccountSupervisedDel()
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
	function empj_BuildingAccountSupervisedDelOne(empj_BuildingAccountSupervisedId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[empj_BuildingAccountSupervisedId],
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
		listVue.isAllSelected = (listVue.empj_BuildingAccountSupervisedList.length > 0)
		&&	(listVue.selectItem.length == listVue.empj_BuildingAccountSupervisedList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.empj_BuildingAccountSupervisedList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.empj_BuildingAccountSupervisedList.forEach(function(item) {
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
				listVue.empj_BuildingAccountSupervisedList=jsonObj.empj_BuildingAccountSupervisedList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('empj_BuildingAccountSupervisedListDiv').scrollIntoView();
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
	function showAjaxModal(empj_BuildingAccountSupervisedModel)
	{
		//empj_BuildingAccountSupervisedModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'empj_BuildingAccountSupervised', empj_BuildingAccountSupervisedModel);
		//updateVue.$set("empj_BuildingAccountSupervised", empj_BuildingAccountSupervisedModel);
		
		updateVue.theState = empj_BuildingAccountSupervisedModel.theState;
		updateVue.busiState = empj_BuildingAccountSupervisedModel.busiState;
		updateVue.eCode = empj_BuildingAccountSupervisedModel.eCode;
		updateVue.userStartId = empj_BuildingAccountSupervisedModel.userStartId;
		updateVue.createTimeStamp = empj_BuildingAccountSupervisedModel.createTimeStamp;
		updateVue.userUpdateId = empj_BuildingAccountSupervisedModel.userUpdateId;
		updateVue.lastUpdateTimeStamp = empj_BuildingAccountSupervisedModel.lastUpdateTimeStamp;
		updateVue.userRecordId = empj_BuildingAccountSupervisedModel.userRecordId;
		updateVue.recordTimeStamp = empj_BuildingAccountSupervisedModel.recordTimeStamp;
		updateVue.buildingInfoId = empj_BuildingAccountSupervisedModel.buildingInfoId;
		updateVue.bankAccountSupervisedId = empj_BuildingAccountSupervisedModel.bankAccountSupervisedId;
		updateVue.beginTimeStamp = empj_BuildingAccountSupervisedModel.beginTimeStamp;
		updateVue.endTimeStamp = empj_BuildingAccountSupervisedModel.endTimeStamp;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateEmpj_BuildingAccountSupervised()
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
	function addEmpj_BuildingAccountSupervised()
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
				addVue.buildingInfoId = null;
				addVue.bankAccountSupervisedId = null;
				addVue.beginTimeStamp = null;
				addVue.endTimeStamp = null;
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
	"listDivId":"#empj_BuildingAccountSupervisedListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Empj_BuildingAccountSupervisedList",
	"addInterface":"../Empj_BuildingAccountSupervisedAdd",
	"deleteInterface":"../Empj_BuildingAccountSupervisedDelete",
	"updateInterface":"../Empj_BuildingAccountSupervisedUpdate"
});
