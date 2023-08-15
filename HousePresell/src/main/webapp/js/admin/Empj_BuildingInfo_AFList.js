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
			developCompanyId:null,
			developCompanyList:[],
			projectId:null,
			projectList:[],
			buildingId:null,
			buildingList:[],
			empj_BuildingInfo_AFList:[]
		},
		methods : {
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			listItemSelectHandle: listItemSelectHandle,
			empj_BuildingInfo_AFDelOne : empj_BuildingInfo_AFDelOne,
			empj_BuildingInfo_AFDel : empj_BuildingInfo_AFDel,
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
			developCompanyId:null,
			developCompanyList:[],
			eCodeOfDevelopCompany:null,
			projectId:null,
			projectList:[],
			theNameOfProject:null,
			eCodeOfProject:null,
			buildingId:null,
			buildingList:[],
			eCodeOfBuilding:null,
			buildingArea:null,
			escrowArea:null,
			deliveryType:null,
			upfloorNumber:null,
			downfloorNumber:null,
			landMortgageState:null,
			landMortgagor:null,
			landMortgageAmount:null,
			remark:null,
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateEmpj_BuildingInfo_AF
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
			developCompanyId:null,
			developCompanyList:[],
			eCodeOfDevelopCompany:null,
			projectId:null,
			projectList:[],
			theNameOfProject:null,
			eCodeOfProject:null,
			buildingId:null,
			buildingList:[],
			eCodeOfBuilding:null,
			buildingArea:null,
			escrowArea:null,
			deliveryType:null,
			upfloorNumber:null,
			downfloorNumber:null,
			landMortgageState:null,
			landMortgagor:null,
			landMortgageAmount:null,
			remark:null,
		},
		methods : {
			getAddForm : getAddForm,
			add : addEmpj_BuildingInfo_AF
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
			developCompanyId:this.developCompanyId,
			projectId:this.projectId,
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
			developCompanyId:this.developCompanyId,
			eCodeOfDevelopCompany:this.eCodeOfDevelopCompany,
			projectId:this.projectId,
			theNameOfProject:this.theNameOfProject,
			eCodeOfProject:this.eCodeOfProject,
			buildingId:this.buildingId,
			eCodeOfBuilding:this.eCodeOfBuilding,
			buildingArea:this.buildingArea,
			escrowArea:this.escrowArea,
			deliveryType:this.deliveryType,
			upfloorNumber:this.upfloorNumber,
			downfloorNumber:this.downfloorNumber,
			landMortgageState:this.landMortgageState,
			landMortgagor:this.landMortgagor,
			landMortgageAmount:this.landMortgageAmount,
			remark:this.remark,
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
			developCompanyId:this.developCompanyId,
			eCodeOfDevelopCompany:this.eCodeOfDevelopCompany,
			projectId:this.projectId,
			theNameOfProject:this.theNameOfProject,
			eCodeOfProject:this.eCodeOfProject,
			buildingId:this.buildingId,
			eCodeOfBuilding:this.eCodeOfBuilding,
			buildingArea:this.buildingArea,
			escrowArea:this.escrowArea,
			deliveryType:this.deliveryType,
			upfloorNumber:this.upfloorNumber,
			downfloorNumber:this.downfloorNumber,
			landMortgageState:this.landMortgageState,
			landMortgagor:this.landMortgagor,
			landMortgageAmount:this.landMortgageAmount,
			remark:this.remark,
		}
	}
	function empj_BuildingInfo_AFDel()
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
	function empj_BuildingInfo_AFDelOne(empj_BuildingInfo_AFId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[empj_BuildingInfo_AFId],
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
		listVue.isAllSelected = (listVue.empj_BuildingInfo_AFList.length > 0)
		&&	(listVue.selectItem.length == listVue.empj_BuildingInfo_AFList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.empj_BuildingInfo_AFList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.empj_BuildingInfo_AFList.forEach(function(item) {
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
				listVue.empj_BuildingInfo_AFList=jsonObj.empj_BuildingInfo_AFList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('empj_BuildingInfo_AFListDiv').scrollIntoView();
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
	function showAjaxModal(empj_BuildingInfo_AFModel)
	{
		//empj_BuildingInfo_AFModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'empj_BuildingInfo_AF', empj_BuildingInfo_AFModel);
		//updateVue.$set("empj_BuildingInfo_AF", empj_BuildingInfo_AFModel);
		
		updateVue.theState = empj_BuildingInfo_AFModel.theState;
		updateVue.busiState = empj_BuildingInfo_AFModel.busiState;
		updateVue.eCode = empj_BuildingInfo_AFModel.eCode;
		updateVue.userStartId = empj_BuildingInfo_AFModel.userStartId;
		updateVue.createTimeStamp = empj_BuildingInfo_AFModel.createTimeStamp;
		updateVue.lastUpdateTimeStamp = empj_BuildingInfo_AFModel.lastUpdateTimeStamp;
		updateVue.userRecordId = empj_BuildingInfo_AFModel.userRecordId;
		updateVue.recordTimeStamp = empj_BuildingInfo_AFModel.recordTimeStamp;
		updateVue.developCompanyId = empj_BuildingInfo_AFModel.developCompanyId;
		updateVue.eCodeOfDevelopCompany = empj_BuildingInfo_AFModel.eCodeOfDevelopCompany;
		updateVue.projectId = empj_BuildingInfo_AFModel.projectId;
		updateVue.theNameOfProject = empj_BuildingInfo_AFModel.theNameOfProject;
		updateVue.eCodeOfProject = empj_BuildingInfo_AFModel.eCodeOfProject;
		updateVue.buildingId = empj_BuildingInfo_AFModel.buildingId;
		updateVue.eCodeOfBuilding = empj_BuildingInfo_AFModel.eCodeOfBuilding;
		updateVue.buildingArea = empj_BuildingInfo_AFModel.buildingArea;
		updateVue.escrowArea = empj_BuildingInfo_AFModel.escrowArea;
		updateVue.deliveryType = empj_BuildingInfo_AFModel.deliveryType;
		updateVue.upfloorNumber = empj_BuildingInfo_AFModel.upfloorNumber;
		updateVue.downfloorNumber = empj_BuildingInfo_AFModel.downfloorNumber;
		updateVue.landMortgageState = empj_BuildingInfo_AFModel.landMortgageState;
		updateVue.landMortgagor = empj_BuildingInfo_AFModel.landMortgagor;
		updateVue.landMortgageAmount = empj_BuildingInfo_AFModel.landMortgageAmount;
		updateVue.remark = empj_BuildingInfo_AFModel.remark;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateEmpj_BuildingInfo_AF()
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
	function addEmpj_BuildingInfo_AF()
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
				addVue.developCompanyId = null;
				addVue.eCodeOfDevelopCompany = null;
				addVue.projectId = null;
				addVue.theNameOfProject = null;
				addVue.eCodeOfProject = null;
				addVue.buildingId = null;
				addVue.eCodeOfBuilding = null;
				addVue.buildingArea = null;
				addVue.escrowArea = null;
				addVue.deliveryType = null;
				addVue.upfloorNumber = null;
				addVue.downfloorNumber = null;
				addVue.landMortgageState = null;
				addVue.landMortgagor = null;
				addVue.landMortgageAmount = null;
				addVue.remark = null;
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
	"listDivId":"#empj_BuildingInfo_AFListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Empj_BuildingInfo_AFList",
	"addInterface":"../Empj_BuildingInfo_AFAdd",
	"deleteInterface":"../Empj_BuildingInfo_AFDelete",
	"updateInterface":"../Empj_BuildingInfo_AFUpdate"
});
