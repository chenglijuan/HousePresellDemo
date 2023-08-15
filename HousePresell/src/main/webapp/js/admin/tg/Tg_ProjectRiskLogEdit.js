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
            busiType : '21020303',
            projectId: "",
            projectId1: "",
            projectList: [],
            gradeId: "",
            gradeList: [],
            areaId: "",
            tg_AreaList: [],
            buisId: "",
            buisList: [],
            logDate: "",
            riskLog: "",
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
			saveData: function () {
				editVue.logDate = document.getElementById("date2102030303").value;
			}
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
			beforeId: editVue.tableId
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
				// 项目id
				editVue.projectId = jsonObj.tg_RiskLogInfo.theNameOfProject;
				
				editVue.projectId1 = jsonObj.tg_RiskLogInfo.projectId;
				
				editVue.areaId = jsonObj.tg_RiskLogInfo.theNameOfCityRegion;
				
				editVue.buisId = jsonObj.tg_RiskLogInfo.theNameOfDevelopCompany;
				if(jsonObj.tg_RiskLogInfo.riskRating == 0) {
					editVue.gradeId = "高"
				} else if(jsonObj.tg_RiskLogInfo.riskRating == 	1) {
					editVue.gradeId = "中"
				} else if(jsonObj.tg_RiskLogInfo.riskRating == 	2){
					editVue.gradeId = "低"
				}
				
				editVue.logDate = jsonObj.tg_RiskLogInfo.logDate;
				
				editVue.riskLog = jsonObj.tg_RiskLogInfo.riskLog;
				
				editVue.tgxy_CoopMemoModel = jsonObj.tgxy_CoopMemo;
				 
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
			interfaceVersion: this.interfaceVersion,
			tableId: editVue.tableId,
			projectId: editVue.projectId1,
			/*pjRiskRatingId: editVue.gradeId,*/
			logDate: document.getElementById("date2102030303").value,
			riskLog: editVue.riskLog,
			smAttachmentList: fileUploadList,
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
				enterNext2Tab(editVue.tableId, '项目风险日志详情', 'tg/Tg_ProjectRiskLogDetail.shtml',editVue.tableId + editVue.busiType);
			}
		});
	}
	
	function look()
	{
		if(editVue.bankId != ""){
			editVue.tgxy_CoopMemoModel.signDate = document.getElementById("date2102030303").value;
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
		new ServerInterface(baseInfo.getInterface).execute(editVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				editVue.projectList = jsonObj.Empj_ProjectInfoList;
				
				getDetail();
			}
		});
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
		elem: '#date2102030303',
	});
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tg_ProjectRiskLogEditDiv",
	"detailInterface":"../Tg_RiskLogInfoDetail",
	"getInterface": "../Empj_ProjectInfoList",
	"detailInfoInterface": "../Emmp_BankInfoDetail",
	"updateInterface":"../Tg_RiskLogInfoUpdate"
});
