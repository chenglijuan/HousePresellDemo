(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tg_PjRiskAssessmentEditModel: {},
			tableId : 1,
			projectId: "",
			projectList: [],
			areaId: "",
			areaList: [],
			developerId: "",
			developerList: [],
			smAttachmentList:[],
            loadUploadList : [],
            showDelete : true,
            busiType : '21020301',
            companyName: "",
            theNameOfCityRegion:"",
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			//更新
			getUpdateForm : getUpdateForm,
			update: update,
			lookProject: lookProject,
			changeProject: changeProject,
			getProjectId: function(data) {
				this.projectId = data.tableId;
				detailVue.changeProject();
			}
		},
		computed:{
			 
		},
		components : {
			"my-uploadcomponent":fileUpload,
			'vue-select': VueSelect,
		},
		watch:{
			
		}
	});

	//------------------------方法定义-开始------------------//
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:detailVue.tableId,
			busiCode: detailVue.busiType,
		}
	}
	
	function lookProject()
	{
	}
	
	function changeProject()
	{
		console.log(detailVue.projectId);
		
		var model = {
				interfaceVersion:this.interfaceVersion,
				projectId: detailVue.projectId,
		}
		new ServerInterface(baseInfo.loadDetailInterface).execute(model, function(jsonObj)
				{
					if(jsonObj.result != "success")
					{
						generalErrorModal(jsonObj);
					}
					else
					{
						detailVue.theNameOfCityRegion = jsonObj.cityRegion.theName;
						detailVue.areaId = jsonObj.cityRegion.tableId;  // 区域id
					}
				});
	}

	//详情操作--------------
	function refresh()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var list = tableIdStr.split("_");
		tableIdStr = list[2];
		detailVue.tableId = tableIdStr;
		if (detailVue.tableId == null || detailVue.tableId < 1) 
		{
			return;
		}
		
		getDetail();
	}

	// 加载开发企业信息
	function getProjectList()
	{
		var model = {
				interfaceVersion:"19000101",
				developCompanyId: detailVue.developerId
		}
		new ServerInterface(baseInfo.loadDetailInterface).execute(model, function(jsonObj)
				{
					if(jsonObj.result != "success")
					{
						generalErrorModal(jsonObj);
					}
					else
					{
						detailVue.projectList = jsonObj.projectList;
					}
				});
	}
	
	function getDetail()
	{
		new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function(jsonObj)
				{
					if(jsonObj.result != "success")
					{
						generalErrorModal(jsonObj);
					}
					else
					{
						detailVue.tg_PjRiskAssessmentEditModel = jsonObj.tg_PjRiskAssessment;
						detailVue.theNameOfCityRegion = jsonObj.tg_PjRiskAssessment.theNameOfCityRegion;
						detailVue.companyName = jsonObj.tg_PjRiskAssessment.theCompanyName;
						detailVue.areaId = jsonObj.tg_PjRiskAssessment.cityRegionId;
						detailVue.projectId =  jsonObj.tg_PjRiskAssessment.projectId;
						detailVue.developerId = jsonObj.tg_PjRiskAssessment.developCompanyId;
						detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
						
						getProjectList();
					}
				});
	}
	
	function getAreaList()
	{
		new ServerInterface(baseInfo.areaInterface).execute(detailVue.getSearchForm(), function(jsonObj)
				{
					if(jsonObj.result != "success")
					{
						generalErrorModal(jsonObj);
					}
					else
					{
						detailVue.areaList = jsonObj.areaList;
					}
				});
	}
	
	function getDeveloperList()
	{
		new ServerInterface(baseInfo.developerInterface).execute(detailVue.getSearchForm(), function(jsonObj)
				{
					if(jsonObj.result != "success")
					{
						generalErrorModal(jsonObj);
					}
					else
					{
						detailVue.developerList = jsonObj.developerList;
					}
				});
	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getUpdateForm()
	{
		var fileUploadList = detailVue.$refs.listenUploadData.uploadData;
		fileUploadList = JSON.stringify(fileUploadList);
		return {
			interfaceVersion:this.interfaceVersion,
			cityRegionId:detailVue.areaId,
			developCompanyId:detailVue.developerId,
			projectId:detailVue.projectId,
			assessDate:document.getElementById("date2102030103").value,
			riskAssessment:detailVue.tg_PjRiskAssessmentEditModel.riskAssessment,
			smAttachmentList:fileUploadList,
			busiCode:detailVue.busiType,
			tableId:detailVue.tableId
		}
	}

	function update()
	{
		new ServerInterface(baseInfo.updateInterface).execute(detailVue.getUpdateForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				enterNext2Tab(jsonObj.tableId, '项目风险评估详情', 'tg/Tg_PjRiskAssessmentDetail.shtml',jsonObj.tableId+"21020301");
				refresh();
			}
		});
	}
	
	function initData()
	{
		
	}
	
	laydate.render({
		elem: '#date2102030103',
	});
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tg_PjRiskAssessmentEditDiv",
	"detailInterface":"../Tg_PjRiskAssessmentDetail",
	"updateInterface":"../Tg_PjRiskAssessmentUpdate",
	// 区域
	"areaInterface":"../",
	// 开发企业
	"developerInterface":"../",
	"projectInterface":"../",
	"loadDetailInterface":"../Tg_PjRiskAssessmentAddQuery"
});
