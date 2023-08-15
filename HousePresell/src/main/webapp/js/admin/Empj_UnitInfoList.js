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
			buildingId:null,
			buildingList:[],
			empj_UnitInfoList:[]
		},
		methods : {
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			listItemSelectHandle: listItemSelectHandle,
			empj_UnitInfoDelOne : empj_UnitInfoDelOne,
			empj_UnitInfoDel : empj_UnitInfoDel,
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
			buildingId:null,
			buildingList:[],
			eCodeOfBuilding:null,
			theName:null,
			upfloorNumber:null,
			upfloorHouseHoldNumber:null,
			downfloorNumber:null,
			downfloorHouseHoldNumber:null,
			elevatorNumber:null,
			hasSecondaryWaterSupply:null,
			remark:null,
			logId:null,
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateEmpj_UnitInfo
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
			buildingId:null,
			buildingList:[],
			eCodeOfBuilding:null,
			theName:null,
			upfloorNumber:null,
			upfloorHouseHoldNumber:null,
			downfloorNumber:null,
			downfloorHouseHoldNumber:null,
			elevatorNumber:null,
			hasSecondaryWaterSupply:null,
			remark:null,
			logId:null,
		},
		methods : {
			getAddForm : getAddForm,
			add : addEmpj_UnitInfo
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
			buildingId:this.buildingId,
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
			buildingId:this.buildingId,
			eCodeOfBuilding:this.eCodeOfBuilding,
			theName:this.theName,
			upfloorNumber:this.upfloorNumber,
			upfloorHouseHoldNumber:this.upfloorHouseHoldNumber,
			downfloorNumber:this.downfloorNumber,
			downfloorHouseHoldNumber:this.downfloorHouseHoldNumber,
			elevatorNumber:this.elevatorNumber,
			hasSecondaryWaterSupply:this.hasSecondaryWaterSupply,
			remark:this.remark,
			logId:this.logId,
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
			buildingId:this.buildingId,
			eCodeOfBuilding:this.eCodeOfBuilding,
			theName:this.theName,
			upfloorNumber:this.upfloorNumber,
			upfloorHouseHoldNumber:this.upfloorHouseHoldNumber,
			downfloorNumber:this.downfloorNumber,
			downfloorHouseHoldNumber:this.downfloorHouseHoldNumber,
			elevatorNumber:this.elevatorNumber,
			hasSecondaryWaterSupply:this.hasSecondaryWaterSupply,
			remark:this.remark,
			logId:this.logId,
		}
	}
	function empj_UnitInfoDel()
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
	function empj_UnitInfoDelOne(empj_UnitInfoId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[empj_UnitInfoId],
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
		listVue.isAllSelected = (listVue.empj_UnitInfoList.length > 0)
		&&	(listVue.selectItem.length == listVue.empj_UnitInfoList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.empj_UnitInfoList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.empj_UnitInfoList.forEach(function(item) {
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
				listVue.empj_UnitInfoList=jsonObj.empj_UnitInfoList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('empj_UnitInfoListDiv').scrollIntoView();
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
	function showAjaxModal(empj_UnitInfoModel)
	{
		//empj_UnitInfoModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'empj_UnitInfo', empj_UnitInfoModel);
		//updateVue.$set("empj_UnitInfo", empj_UnitInfoModel);
		
		updateVue.theState = empj_UnitInfoModel.theState;
		updateVue.busiState = empj_UnitInfoModel.busiState;
		updateVue.eCode = empj_UnitInfoModel.eCode;
		updateVue.userStartId = empj_UnitInfoModel.userStartId;
		updateVue.createTimeStamp = empj_UnitInfoModel.createTimeStamp;
		updateVue.lastUpdateTimeStamp = empj_UnitInfoModel.lastUpdateTimeStamp;
		updateVue.userRecordId = empj_UnitInfoModel.userRecordId;
		updateVue.recordTimeStamp = empj_UnitInfoModel.recordTimeStamp;
		updateVue.buildingId = empj_UnitInfoModel.buildingId;
		updateVue.eCodeOfBuilding = empj_UnitInfoModel.eCodeOfBuilding;
		updateVue.theName = empj_UnitInfoModel.theName;
		updateVue.upfloorNumber = empj_UnitInfoModel.upfloorNumber;
		updateVue.upfloorHouseHoldNumber = empj_UnitInfoModel.upfloorHouseHoldNumber;
		updateVue.downfloorNumber = empj_UnitInfoModel.downfloorNumber;
		updateVue.downfloorHouseHoldNumber = empj_UnitInfoModel.downfloorHouseHoldNumber;
		updateVue.elevatorNumber = empj_UnitInfoModel.elevatorNumber;
		updateVue.hasSecondaryWaterSupply = empj_UnitInfoModel.hasSecondaryWaterSupply;
		updateVue.remark = empj_UnitInfoModel.remark;
		updateVue.logId = empj_UnitInfoModel.logId;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateEmpj_UnitInfo()
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
	function addEmpj_UnitInfo()
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
				addVue.buildingId = null;
				addVue.eCodeOfBuilding = null;
				addVue.theName = null;
				addVue.upfloorNumber = null;
				addVue.upfloorHouseHoldNumber = null;
				addVue.downfloorNumber = null;
				addVue.downfloorHouseHoldNumber = null;
				addVue.elevatorNumber = null;
				addVue.hasSecondaryWaterSupply = null;
				addVue.remark = null;
				addVue.logId = null;
				refresh();
			}
		});
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
	"listDivId":"#empj_UnitInfoListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Empj_UnitInfoList",
	"addInterface":"../Empj_UnitInfoAdd",
	"deleteInterface":"../Empj_UnitInfoDelete",
	"updateInterface":"../Empj_UnitInfoUpdate"
});
