(function(baseInfo){
	var editVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tgxy_CoopMemoModel: {},
			tableId : "",
			emmp_BankInfoList: [],
			createPeo: [],
			editPeo: [],
			bankId: "",
			userUpdateId: 2,
			beforeId: "",
			theNameOfBank: "",
			signDate:"",
            errMsg: "",
            successMsg: "",
            smAttachmentList:[],
            loadUploadList : [],
            showDelete : true,
            busiType : '21020302',
            projectId: "",
            projectList: [],
            gradeId: "",
            gradeList: [],
            areaId: "",
            tg_AreaList: [],
            buisId: "",
            buisList: [],
            asa: "",
            riskLog: "",
            user: "",
            userDate: ""
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			//更新
			getUpdateForm : getUpdateForm,
			update: update,
			look: look,
			getDetailForm: getDetailForm,
			handleChange: handleChange
		},
		computed:{
			 
		},
		components : {
			"my-uploadcomponent":fileUpload
		},
		watch:{
			
		}
	});

	//------------------------方法定义-开始------------------//
	//详情操作--------------获取"合作备忘录详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId: editVue.tableId,
			beforeId: editVue.tableId,
			busiCode: editVue.busiType,
		}
	}

	//详情操作--------------
	function refresh()
	{
        
	}

	function getDetail()
	{
		new ServerInterface(baseInfo.detailInterface).execute(editVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				editVue.projectId = jsonObj.tg_PjRiskRating.theName;
				/*if(jsonObj.tg_PjRiskRating.theLevel == 0) {
					editVue.gradeId = "高";
				}else if(jsonObj.tg_PjRiskRating.theLevel == 1) {
					editVue.gradeId = "中";
				} else {
					editVue.gradeId = "低";
				}*/
				editVue.gradeId = jsonObj.tg_PjRiskRating.theLevel;
				editVue.areaId = jsonObj.tg_PjRiskRating.cityRegionName;
				editVue.asa = jsonObj.tg_PjRiskRating.operateDate;
				editVue.buisId = jsonObj.tg_PjRiskRating.developCompanyName;
				editVue.riskLog = jsonObj.tg_PjRiskRating.riskNotification;
				editVue.user = jsonObj.tg_PjRiskRating.userUpdate;
				editVue.userDate = jsonObj.tg_PjRiskRating.lastUpdateTimeStamp;
				 
				editVue.loadUploadList = jsonObj.smAttachmentCfgList;
			}
		});
	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getUpdateForm()
	{
		var fileUploadList = editVue.$refs.listenUploadData.uploadData;
		fileUploadList = JSON.stringify(fileUploadList);
		return {
			interfaceVersion:this.interfaceVersion,
			/*projectId: editVue.projectId,*/
			theLevel: editVue.gradeId,
			/*areaId: editVue.areaId,*/
			operateDate:document.getElementById("date2102030203").value,
			/*buisId: editVue.buisId,*/
			riskNotification: editVue.riskLog,
			tableId:editVue.tableId,
			smAttachmentList:fileUploadList,
		}
	}
	
	function getDetailForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			theState:this.theState,
			tableId:this.bankId,
		}
	}
	
	function handleChange()
	{
		var model = {
				interfaceVersion:this.interfaceVersion,
				projectId:editVue.projectId,
		}
		new ServerInterface(baseInfo.loadDetailInterface).execute(model, function(jsonObj)
				{
					if(jsonObj.result != "success")
					{
						generalErrorModal(jsonObj);
					}
					else
					{
						 
						editVue.gradeId = jsonObj;
					}
				});
	}

	function update()
	{
		new ServerInterface(baseInfo.updateInterface).execute(editVue.getUpdateForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				 
				enterNext2Tab(editVue.tableId, '项目风险评级详情', 'tg/Tg_ProjectRiskGradeDetail.shtml',editVue.tableId + editVue.busiType);
			}
		});
	}
	
	function look()
	{
		if(editVue.bankId != ""){
			editVue.tgxy_CoopMemoModel.signDate = document.getElementById("date2102030203").value;
			new ServerInterface(baseInfo.detailInfoInterface).execute(editVue.getDetailForm(), function(jsonObj)
					{
						if(jsonObj.result != "success")
						{
							generalErrorModal(jsonObj);
						}
						else
						{
							$(baseInfo.detailDivId).modal('hide');
							editVue.theNameOfBank = jsonObj.emmp_BankInfo.theName;
							refresh();
						}
					});
		}
	}
	
	function bankData()
	{
		getDetail();
		/*new ServerInterface(baseInfo.getInterface).execute(editVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				editVue.projectList = jsonObj.projectList;
				
				
			}
		});*/
	}
	function initData()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var list = tableIdStr.split("_");
		tableIdStr = list[2];
        editVue.tableId = tableIdStr;
        bankData();
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	editVue.initData();
	editVue.refresh();
	
	// 添加日期控件
	laydate.render({
		elem: '#date2102030203',
	});
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tg_ProjectRiskGradeEditDiv",
	"detailInterface":"../Tg_PjRiskRatingDetail",
	"getInterface": "../Emmp_BankInfoList",
	"detailInfoInterface": "../Emmp_BankInfoDetail",
	"updateInterface":"../Tg_PjRiskRatingUpdate",
	"loadDetailInterface":"../Empj_ProjectInfoDetail"
});
