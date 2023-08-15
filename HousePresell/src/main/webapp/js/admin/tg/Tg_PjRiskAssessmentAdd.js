(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			tg_PjRiskAssessmentModel: {},
			projectId: "",
			areaId: "",
			areaId1: "",
			developerId: "",
			developerId1: "",
			projectList: [],
			areaList: [],
			developerList: [],
			loadUploadList: [],
            showDelete : true,
            busiType : '21020301',
            riskAssessment: '',
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			//添加
			getAddForm : getAddForm,
			add: add,
			lookProject: lookProject,
			getAreaForm: getAreaForm,
			update: update,
			getUploadForm : getUploadForm,
			loadUpload : loadUpload,
			changHandle: changHandle,
			changeProject:changeProject,
			getDeveloperId: function (data) {
				this.developerId = data.tableId;
				detailVue.changHandle();
			},
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
		}
	}

	//详情操作--------------
	function refresh()
	{

	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getAddForm()
	{
		var fileUploadList = detailVue.$refs.listenUploadData.uploadData;
		fileUploadList = JSON.stringify(fileUploadList);
		return {
			interfaceVersion:this.interfaceVersion,
			cityRegionId:detailVue.areaId1, // 区域id
			//developCompanyId:detailVue.developerId,
			developCompanyId:detailVue.developerId1,
			projectId:detailVue.projectId,
			assessDate:document.getElementById("date2102030102").value,
			riskAssessment:detailVue.riskAssessment,
			busiCode:"21020301",
			smAttachmentList:fileUploadList
		}
	}

	function add()
	{
		new ServerInterface(baseInfo.addInterface).execute(detailVue.getAddForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				generalSuccessModal();
				refresh();
			}
		});
	}
	
	// 添加日期控件
	var d = new Date();
	laydate.render({
		elem: '#date2102030102',
		value: d.getFullYear() + '-' + lay.digit(d.getMonth() + 1) + '-' + lay.digit(d.getDate())
	});
	
	function lookProject()
	{
		var model = {
				interfaceVersion : 19000101,
				exceptZhengTai : true,
			};
		new ServerInterface(baseInfo.loadAreaInterface).execute(model, function(jsonObj)
				{
					if(jsonObj.result != "success")
					{
						generalErrorModal(jsonObj);
					}
					else
					{
						detailVue.developerList = jsonObj.emmp_CompanyInfoList;
					}
				});
	}
	
	function getAreaForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			projectId: detailVue.projectId,
		}
	}
	
	function initData()
	{
	}
	
	function update()
	{
		new ServerInterface(baseInfo.updateInterface).execute(detailVue.getAddForm(), function(jsonObj)
				{
					if(jsonObj.result != "success")
					{
						generalErrorModal(jsonObj);
					}
					else
					{
						generalSuccessModal();
						enterNext2Tab(jsonObj.tableId, '项目风险评估详情', 'tg/Tg_PjRiskAssessmentDetail.shtml',jsonObj.tableId+"21020301");
					}
				});
	}
	//列表操作-----------------------获取附件参数
	function getUploadForm(){
		return{
			pageNumber : '0',
			busiType : detailVue.busiType,
			interfaceVersion:this.interfaceVersion
		}
	}
	
	//列表操作-----------------------页面加载显示附件类型
	function loadUpload(){
		new ServerInterface(baseInfo.loadUploadInterface).execute(detailVue.getUploadForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				detailVue.loadUploadList = jsonObj.sm_AttachmentCfgList;
			}
		});
	}
	
	function changHandle()
	{
		var model = {
				interfaceVersion:this.interfaceVersion,
				developCompanyId: detailVue.developerId,
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
						detailVue.developerId1 = jsonObj.companyInfo.tableId;
					}
				});
	}
	
	function changeProject()
	{
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
						detailVue.areaId = jsonObj.cityRegion.theName;
						detailVue.areaId1 = jsonObj.cityRegion.tableId;  // 区域id
					}
				});
	}
	
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.refresh();
	detailVue.initData();
	detailVue.loadUpload();
	detailVue.lookProject();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#tg_PjRiskAssessmentDiv",
	"detailInterface":"../Tg_PjRiskAssessmentDetail",
	"addInterface":"../Tg_PjRiskAssessmentAdd",
	"loadAreaInterface":"../Emmp_CompanyInfoForSelect",
	"updateInterface":"../Tg_PjRiskAssessmentAdd",
	"loadUploadInterface":"../Sm_AttachmentCfgList",
	"loadDetailInterface":"../Tg_PjRiskAssessmentAddQuery",
});
