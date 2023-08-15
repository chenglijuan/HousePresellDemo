(function(baseInfo){
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			tg_OtherRiskInfoModel: {},
			projectId : "",
			projectList : [],
			cityRegionName : "",
			developCompanyName : "",
			riskInputDate : "",
			isUsed : "0",
			riskInfo : "",
			remark : "",
			loadUploadList : [],
			showDelete : true,
			busiType : "21020305",
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			//添加
			getAddForm : getAddForm,
			add: add,
			getProjectData : getProjectData,
			getProjectId: function (data){
				this.projectId = data.tableId;
				addVue.lookProject();
			},
			lookProject:lookProject,
			getUploadForm : getUploadForm,
			loadUpload : loadUpload,
		},
		computed:{
			 
		},
		components : {
			'vue-select': VueSelect,
            "my-uploadcomponent":fileUpload
		},
		watch:{
			
		}
	});

	//------------------------方法定义-开始------------------//
	//新增操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
		}
	}
	// 加载项目数据
	function getProjectData() 
	{
		var model = {
				interfaceVersion:addVue.interfaceVersion,
		}
		new ServerInterface(baseInfo.getInterface).execute(model, function(jsonObj) {
			if(jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
                // 项目数据加载
				addVue.projectList = jsonObj.empj_ProjectInfoList;
			}
		});
	}
	function lookProject()
	{
		var model = {
			interfaceVersion:addVue.interfaceVersion,
			tableId: addVue.projectId,	
		}
		new ServerInterface(baseInfo.lookProjectInterface).execute(model, function(jsonObj) {
			if(jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
	            // 项目数据加载
				addVue.developCompanyName = jsonObj.empj_ProjectInfo.developCompanyName;
				addVue.cityRegionName = jsonObj.empj_ProjectInfo.cityRegionName;
			}
		});
	}
	//列表操作-----------------------获取附件参数
	function getUploadForm(){
		return{
			pageNumber : '0',
			busiType : addVue.busiType,
			interfaceVersion:addVue.interfaceVersion
		}
	}
	//列表操作-----------------------页面加载显示附件类型
	function loadUpload(){
		new ServerInterface(baseInfo.loadUploadInterface).execute(addVue.getUploadForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				addVue.loadUploadList = jsonObj.sm_AttachmentCfgList;
			}
		});
	}
	//详情操作--------------
	function refresh()
	{

	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getAddForm()
	{
		var fileUploadList = addVue.$refs.listenUploadData.uploadData;
		fileUploadList = JSON.stringify(fileUploadList);
		return {
			interfaceVersion:this.interfaceVersion,
			theState:this.theState,
			busiState:this.busiState,
			userStartId:this.userStartId,
			projectId:this.projectId,
			riskInputDate:this.riskInputDate,
			riskInfo:this.riskInfo,
			isUsed:this.isUsed,
			remark:this.remark,
			smAttachmentList:fileUploadList
		}
	}

	function add()
	{
		new ServerInterface(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				enterNext2Tab(jsonObj.tableId, '其他风险信息详情', 'tg/Tg_OtherRiskInfoDetail.shtml',jsonObj.tableId+"21020204");
			}
		});
	}
	
	function initData()
	{
		getProjectData();
		loadUpload();
	}
	//------------------------方法定义-结束------------------//
	laydate.render({
	  elem: '#otherRiskInfoAddDate',
	  done: function(value, date, endDate){
	    addVue.riskInputDate = value;
	  }
	});
	
	//------------------------数据初始化-开始----------------//
	addVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#tg_OtherRiskInfoAddDiv",
	"getInterface":"../Empj_ProjectInfoList",
	"detailInterface":"../Tg_OtherRiskInfoDetail",
	"addInterface":"../Tg_OtherRiskInfoAdd",
	"lookProjectInterface":"../Empj_ProjectInfoDetail",
	"loadUploadInterface" : "../Sm_AttachmentCfgList",
});
