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
			cityRegionId:null,
			cityRegionList:[],
			developCompanyId:null,
			developCompanyList:[],
			projectId:null,
			projectList:[],
			tg_PjRiskRatingList:[]
		},
		methods : {
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			listItemSelectHandle: listItemSelectHandle,
			tg_PjRiskRatingDelOne : tg_PjRiskRatingDelOne,
			tg_PjRiskRatingDel : tg_PjRiskRatingDel,
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
			cityRegionId:null,
			cityRegionList:[],
			theNameOfCityRegion:null,
			developCompanyId:null,
			developCompanyList:[],
			eCodeOfDevelopCompany:null,
			projectId:null,
			projectList:[],
			theNameOfProject:null,
			operateDate:null,
			theLevel:null,
			riskNotification:null,
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateTg_PjRiskRating
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
			cityRegionId:null,
			cityRegionList:[],
			theNameOfCityRegion:null,
			developCompanyId:null,
			developCompanyList:[],
			eCodeOfDevelopCompany:null,
			projectId:null,
			projectList:[],
			theNameOfProject:null,
			operateDate:null,
			theLevel:null,
			riskNotification:null,
		},
		methods : {
			getAddForm : getAddForm,
			add : addTg_PjRiskRating
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
			cityRegionId:this.cityRegionId,
			developCompanyId:this.developCompanyId,
			projectId:this.projectId,
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
			cityRegionId:this.cityRegionId,
			theNameOfCityRegion:this.theNameOfCityRegion,
			developCompanyId:this.developCompanyId,
			eCodeOfDevelopCompany:this.eCodeOfDevelopCompany,
			projectId:this.projectId,
			theNameOfProject:this.theNameOfProject,
			operateDate:this.operateDate,
			theLevel:this.theLevel,
			riskNotification:this.riskNotification,
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
			cityRegionId:this.cityRegionId,
			theNameOfCityRegion:this.theNameOfCityRegion,
			developCompanyId:this.developCompanyId,
			eCodeOfDevelopCompany:this.eCodeOfDevelopCompany,
			projectId:this.projectId,
			theNameOfProject:this.theNameOfProject,
			operateDate:this.operateDate,
			theLevel:this.theLevel,
			riskNotification:this.riskNotification,
		}
	}
	function tg_PjRiskRatingDel()
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
	function tg_PjRiskRatingDelOne(tg_PjRiskRatingId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[tg_PjRiskRatingId],
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
		listVue.isAllSelected = (listVue.tg_PjRiskRatingList.length > 0)
		&&	(listVue.selectItem.length == listVue.tg_PjRiskRatingList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tg_PjRiskRatingList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tg_PjRiskRatingList.forEach(function(item) {
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
				listVue.tg_PjRiskRatingList=jsonObj.tg_PjRiskRatingList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tg_PjRiskRatingListDiv').scrollIntoView();
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
	function showAjaxModal(tg_PjRiskRatingModel)
	{
		//tg_PjRiskRatingModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'tg_PjRiskRating', tg_PjRiskRatingModel);
		//updateVue.$set("tg_PjRiskRating", tg_PjRiskRatingModel);
		
		updateVue.theState = tg_PjRiskRatingModel.theState;
		updateVue.busiState = tg_PjRiskRatingModel.busiState;
		updateVue.eCode = tg_PjRiskRatingModel.eCode;
		updateVue.userStartId = tg_PjRiskRatingModel.userStartId;
		updateVue.createTimeStamp = tg_PjRiskRatingModel.createTimeStamp;
		updateVue.lastUpdateTimeStamp = tg_PjRiskRatingModel.lastUpdateTimeStamp;
		updateVue.userRecordId = tg_PjRiskRatingModel.userRecordId;
		updateVue.recordTimeStamp = tg_PjRiskRatingModel.recordTimeStamp;
		updateVue.cityRegionId = tg_PjRiskRatingModel.cityRegionId;
		updateVue.theNameOfCityRegion = tg_PjRiskRatingModel.theNameOfCityRegion;
		updateVue.developCompanyId = tg_PjRiskRatingModel.developCompanyId;
		updateVue.eCodeOfDevelopCompany = tg_PjRiskRatingModel.eCodeOfDevelopCompany;
		updateVue.projectId = tg_PjRiskRatingModel.projectId;
		updateVue.theNameOfProject = tg_PjRiskRatingModel.theNameOfProject;
		updateVue.operateDate = tg_PjRiskRatingModel.operateDate;
		updateVue.theLevel = tg_PjRiskRatingModel.theLevel;
		updateVue.riskNotification = tg_PjRiskRatingModel.riskNotification;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateTg_PjRiskRating()
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
	function addTg_PjRiskRating()
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
				addVue.cityRegionId = null;
				addVue.theNameOfCityRegion = null;
				addVue.developCompanyId = null;
				addVue.eCodeOfDevelopCompany = null;
				addVue.projectId = null;
				addVue.theNameOfProject = null;
				addVue.operateDate = null;
				addVue.theLevel = null;
				addVue.riskNotification = null;
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
	"listDivId":"#tg_PjRiskRatingListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Tg_PjRiskRatingList",
	"addInterface":"../Tg_PjRiskRatingAdd",
	"deleteInterface":"../Tg_PjRiskRatingDelete",
	"updateInterface":"../Tg_PjRiskRatingUpdate"
});
