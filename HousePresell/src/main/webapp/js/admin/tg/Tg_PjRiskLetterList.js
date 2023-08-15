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
			projectId:"",
			projectList:[],
			tg_PjRiskLetterList:[],
			developerId:"",
			developerList:[],
			areaId:"",
			areaList:[],
			letterDateBegin: "",
			letterDateEnd: "",
		},
		methods : {
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			listItemSelectHandle: listItemSelectHandle,
			tg_PjRiskLetterDel : tg_PjRiskLetterDel,
			showAjaxModal:showAjaxModal,
			search:search,
			checkAllClicked : checkAllClicked,
			changePageNumber : function(data){
				listVue.pageNumber = data;
			},
			getProject: getProject,
			handleProject: handleProject,
			reset:reset,
			add:add,
			editHandle:editHandle,
			openDetails: openDetails,
			objSetHandle: objSetHandle
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue
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
			releaseDate:null,
			deliveryCompany:null,
			riskNotification:null,
			basicSituation:null,
			riskAssessment:null,
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateTg_PjRiskLetter
		}
	});
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			theState:null,
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
			releaseDate:null,
			deliveryCompany:null,
			riskNotification:null,
			basicSituation:null,
			riskAssessment:null,
		},
		methods : {
			getAddForm : getAddForm,
			add : addTg_PjRiskLetter
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
			//cityRegionId:this.cityRegionId,
			//developCompanyId:this.developCompanyId,
			//projectId:this.projectId,
			letterDateBegin: listVue.letterDateBegin,
			letterDateEnd: listVue.letterDateEnd,
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
			releaseDate:this.releaseDate,
			deliveryCompany:this.deliveryCompany,
			riskNotification:this.riskNotification,
			basicSituation:this.basicSituation,
			riskAssessment:this.riskAssessment,
		}
	}
	//列表操作--------------获取“更新”表单参数
	function getUpdateForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			theState:this.theState,
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
			releaseDate:this.releaseDate,
			deliveryCompany:this.deliveryCompany,
			riskNotification:this.riskNotification,
			basicSituation:this.basicSituation,
			riskAssessment:this.riskAssessment,
		}
	}
	function tg_PjRiskLetterDel()
	{
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
	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		listVue.isAllSelected = (listVue.tg_PjRiskLetterList.length > 0)
		&&	(listVue.selectItem.length == listVue.tg_PjRiskLetterList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tg_PjRiskLetterList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tg_PjRiskLetterList.forEach(function(item) {
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
				listVue.tg_PjRiskLetterList=jsonObj.tg_PjRiskLetterList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tg_PjRiskLetterListDiv').scrollIntoView();
			}
		});
	}
	
	//列表操作------------搜索
	function search()
	{
		var str = document.getElementById("date2102030401").value;
		str = str.split(" - ");
		listVue.letterDateBegin = str[0];
		listVue.letterDateEnd = str[1];
		listVue.pageNumber = 1;
		refresh();
	}
	
	//弹出编辑模态框--更新操作
	function showAjaxModal(tg_PjRiskLetterModel)
	{
		//tg_PjRiskLetterModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'tg_PjRiskLetter', tg_PjRiskLetterModel);
		//updateVue.$set("tg_PjRiskLetter", tg_PjRiskLetterModel);
		
		updateVue.theState = tg_PjRiskLetterModel.theState;
		updateVue.userStartId = tg_PjRiskLetterModel.userStartId;
		updateVue.createTimeStamp = tg_PjRiskLetterModel.createTimeStamp;
		updateVue.lastUpdateTimeStamp = tg_PjRiskLetterModel.lastUpdateTimeStamp;
		updateVue.userRecordId = tg_PjRiskLetterModel.userRecordId;
		updateVue.recordTimeStamp = tg_PjRiskLetterModel.recordTimeStamp;
		updateVue.cityRegionId = tg_PjRiskLetterModel.cityRegionId;
		updateVue.theNameOfCityRegion = tg_PjRiskLetterModel.theNameOfCityRegion;
		updateVue.developCompanyId = tg_PjRiskLetterModel.developCompanyId;
		updateVue.eCodeOfDevelopCompany = tg_PjRiskLetterModel.eCodeOfDevelopCompany;
		updateVue.projectId = tg_PjRiskLetterModel.projectId;
		updateVue.theNameOfProject = tg_PjRiskLetterModel.theNameOfProject;
		updateVue.releaseDate = tg_PjRiskLetterModel.releaseDate;
		updateVue.deliveryCompany = tg_PjRiskLetterModel.deliveryCompany;
		updateVue.riskNotification = tg_PjRiskLetterModel.riskNotification;
		updateVue.basicSituation = tg_PjRiskLetterModel.basicSituation;
		updateVue.riskAssessment = tg_PjRiskLetterModel.riskAssessment;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateTg_PjRiskLetter()
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
	function addTg_PjRiskLetter()
	{
		new ServerInterface(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				$(baseInfo.addDivId).modal('hide');
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
				addVue.releaseDate = null;
				addVue.deliveryCompany = null;
				addVue.riskNotification = null;
				addVue.basicSituation = null;
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
	
	function getProject()
	{
		new ServerInterface(baseInfo.projectInterface).execute("", function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				listVue.projectList = jsonObj.projectList;
			}
		});
	}
	
	// 添加日期控件
	laydate.render({
	   elem: '#date2102030401',
	   range: true
	});
	
	function handleProject()
	{
		new ServerInterface(baseInfo.detailInterface).execute(getDetailInfo(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				listVue.developerList = jsonObj.developerList;
				listVue.areaList = jsonObj.areaList;
			}
		});
	}
	
	function getDetailInfo()
	{
		return {
			interfaceVersion :19000101,
			tableId:listVue.projectId
		}
	}
	
	function reset()
	{
		/*listVue.projectId = "";
		listVue.developerId = "";
		listVue.areaId = "";*/
		document.getElementById("date2102030401").value = "";
		listVue.keyword = "";
	}
	
	function add()
	{
		enterNewTab('21020304', '新增项目风险函', 'tg/Tg_PjRiskLetterAdd.shtml');
	}
	
	function editHandle()
	{
		enterNextTab(listVue.selectItem, '修改项目风险函', 'tg/Tg_PjRiskLetterEdit.shtml',listVue.selectItem+"21020304");
	}
	
	function openDetails(val)
	{
		enterNextTab(val, '项目风险函详情', 'tg/Tg_PjRiskLetterDetail.shtml',val + "21020304");
	}
	
	function objSetHandle()
	{ 
		enterNextTab("", '接受对象配置', 'tg/Tg_PjRiskLetterObjectDetail.shtml',"21020304");
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.refresh();
	listVue.initData();
	//listVue.getProject();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tg_PjRiskLetterListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Tg_PjRiskLetterList",
	"addInterface":"../Tg_PjRiskLetterAdd",
	"deleteInterface":"../Tg_PjRiskLetterBatchDelete",
	"updateInterface":"../Tg_PjRiskLetterUpdate",
	"projectInterface":"../Tg_PjRiskLetterUpdate",
	"detailInterface":"../Tg_PjRiskLetterUpdate"
});
