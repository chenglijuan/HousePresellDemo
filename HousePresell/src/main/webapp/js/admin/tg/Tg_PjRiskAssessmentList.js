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
			tg_PjRiskAssessmentList:[],
			developerList: [],
			projectList: [],
			areaList: [],
			startDate: "",
			endDate: "",
			developerId: "",
			projectId: "",
			areaId: "",
		},
		methods : {
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			listItemSelectHandle: listItemSelectHandle,
			tg_PjRiskAssessmentDelOne : tg_PjRiskAssessmentDelOne,
			tg_PjRiskAssessmentDel : tg_PjRiskAssessmentDel,
			showAjaxModal:showAjaxModal,
			search:search,
			checkAllClicked : checkAllClicked,
			changePageNumber : function(data){
				listVue.pageNumber = data;
			},
			handleReset: handleReset,
			add: add,
			editHandle: editHandle,
			detailHandle: detailHandle,
			exportForm : exportForm,//导出excel
			getProjectId: function(data) {
				listVue.projectId = data.tableId;
				/*listVue.objectname();*/
			},
			getAreaId: function(data) {
				listVue.areaId = data.tableId;
			},
			getDeveloperId: function(data) {
				listVue.developerId = data.tableId;
			}
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue,
			'vue-select': VueSelect,
		},
		watch:{
			//pageNumber : refresh,
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
			assessDate:null,
			riskAssessment:null,
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateTg_PjRiskAssessment
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
			assessDate:null,
			riskAssessment:null,
		},
		methods : {
			getAddForm : getAddForm,
			add : addTg_PjRiskAssessment
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
			startDate:listVue.startDate,
			endDate:listVue.endDate,
			developerId: listVue.developerId,
			projectId: listVue.projectId,
			areaId: listVue.areaId,
		}
	}
	//列表操作--------------获取“删除资源”表单参数
	function getDeleteForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			idArr:listVue.selectItem
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
			assessDate:this.assessDate,
			riskAssessment:this.riskAssessment,
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
			assessDate:this.assessDate,
			riskAssessment:this.riskAssessment,
		}
	}
	
	function tg_PjRiskAssessmentDel()
	{
		generalSelectModal(function(){
			delAjRiskAssessment();
        }, "确认删除吗？");
	}
	
	function delAjRiskAssessment(){
		new ServerInterface(baseInfo.deleteInterface).execute(listVue.getDeleteForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				generalSuccessModal();
				listVue.selectItem = [];
				refresh();
			}
		});
	}
	function tg_PjRiskAssessmentDelOne(tg_PjRiskAssessmentId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[tg_PjRiskAssessmentId],
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
		listVue.isAllSelected = (listVue.tg_PjRiskAssessmentList.length > 0)
		&&	(listVue.selectItem.length == listVue.tg_PjRiskAssessmentList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tg_PjRiskAssessmentList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tg_PjRiskAssessmentList.forEach(function(item) {
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
				generalErrorModal(jsonObj);
			}
			else
			{
				listVue.tg_PjRiskAssessmentList=jsonObj.tg_PjRiskAssessmentList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tg_PjRiskAssessmentListDiv').scrollIntoView();
			}
		});
	}
	
	//列表操作-----------------------导出excel
	function exportForm(){
		new ServerInterface(baseInfo.exportInterface).execute(listVue.getSearchForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				listVue.errMsg = jsonObj.info;
			}
			else
			{
				$(baseInfo.sdModelDiveId).modal({
					backdrop :'static'
				});
				
				window.location.href="../"+jsonObj.fileURL;
			}
		});
	}
	
	//列表操作------------搜索
	function search()
	{
		listVue.pageNumber = 1;
		var date = document.getElementById("date2102030101").value;
		date = date.split(' - ');
		listVue.startDate = date[0];
		listVue.endDate = date[1];
		refresh();
	}
	
	function detailHandle(id)
	{
		enterNextTab(id, '风险评估详情', 'tg/Tg_PjRiskAssessmentDetail.shtml',id+"21020301");
	}
	
	//弹出编辑模态框--更新操作
	function showAjaxModal(tg_PjRiskAssessmentModel)
	{
		//tg_PjRiskAssessmentModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'tg_PjRiskAssessment', tg_PjRiskAssessmentModel);
		//updateVue.$set("tg_PjRiskAssessment", tg_PjRiskAssessmentModel);
		
		updateVue.theState = tg_PjRiskAssessmentModel.theState;
		updateVue.busiState = tg_PjRiskAssessmentModel.busiState;
		updateVue.eCode = tg_PjRiskAssessmentModel.eCode;
		updateVue.userStartId = tg_PjRiskAssessmentModel.userStartId;
		updateVue.createTimeStamp = tg_PjRiskAssessmentModel.createTimeStamp;
		updateVue.lastUpdateTimeStamp = tg_PjRiskAssessmentModel.lastUpdateTimeStamp;
		updateVue.userRecordId = tg_PjRiskAssessmentModel.userRecordId;
		updateVue.recordTimeStamp = tg_PjRiskAssessmentModel.recordTimeStamp;
		updateVue.cityRegionId = tg_PjRiskAssessmentModel.cityRegionId;
		updateVue.theNameOfCityRegion = tg_PjRiskAssessmentModel.theNameOfCityRegion;
		updateVue.developCompanyId = tg_PjRiskAssessmentModel.developCompanyId;
		updateVue.eCodeOfDevelopCompany = tg_PjRiskAssessmentModel.eCodeOfDevelopCompany;
		updateVue.projectId = tg_PjRiskAssessmentModel.projectId;
		updateVue.theNameOfProject = tg_PjRiskAssessmentModel.theNameOfProject;
		updateVue.assessDate = tg_PjRiskAssessmentModel.assessDate;
		updateVue.riskAssessment = tg_PjRiskAssessmentModel.riskAssessment;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateTg_PjRiskAssessment()
	{
		new ServerInterface(baseInfo.updateInterface).execute(updateVue.getUpdateForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				$(baseInfo.updateDivId).modal('hide');
				refresh();
			}
		});
	}
	function addTg_PjRiskAssessment()
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
				addVue.assessDate = null;
				addVue.riskAssessment = null;
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
	
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}

	function initData()
	{
		initButtonList();
	}
	// 添加日期控件
	laydate.render({
	   elem: '#date2102030101',
	   range: true
	});
	
	function handleReset()
	{
		listVue.keyword = "";
		listVue.developerId = "";
		listVue.projectId = "";
		listVue.areaId = "";
		document.getElementById("date2102030101").value = "";
	}
	
	function add()
	{
		enterNewTab('', '风险评估新增', 'tg/Tg_PjRiskAssessmentAdd.shtml');
	}
	
	function editHandle()
	{
		var tableId = listVue.selectItem[0];
		enterNextTab(tableId, '风险评估修改', 'tg/Tg_PjRiskAssessmentEdit.shtml',tableId+"21020301");
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tg_PjRiskAssessmentListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Tg_PjRiskAssessmentList",
	"addInterface":"../Tg_PjRiskAssessmentAdd",
	"deleteInterface":"../Tg_PjRiskAssessmentBatchDelete",
	"updateInterface":"../Tg_PjRiskAssessmentUpdate",
	"exportInterface":"../Tg_PjRiskAssessmentExportExportExcel"//导出excel接口
});
