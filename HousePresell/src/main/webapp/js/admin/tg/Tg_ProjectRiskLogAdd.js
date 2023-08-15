(function(baseInfo) {
	var addVue = new Vue({
		el: baseInfo.addDivId,
		data: {
			interfaceVersion: 19000101,
			tgxy_CoopMemoModel: {},
			pageNumber: 0,
			countPerPage: 0,
			eCode: "",
			userStartId: 1,
			bankValue: 0,
			signDate: "",
			tableId: "",
			bankId: 0,
			theNameOfBank: null,
			emmp_BankInfoList: [
				{ theName: '中国银行', tableId: "0" },
				{ theName: '中国建设银行', tableId: "1" },
				{ theName: '工行', tableId: "2" }
			],
			uploadData : [],
			errMsg: "",
			
			// 附件上传相关参数
			loadUploadList: [],
            showDelete : true,
            busiType : '21020303',
			areaId: "",
			tg_AreaList: [],
			buisId: "",
			buisList: [],
			gradeId: "",
			gradeList: [],
			riskLog: "",
			projectId: "",
			projectList: "",
			asa: "",
			gradeName: "",
		},
		methods: {
			//详情
			refresh: refresh,
			initData: initData,
			//添加
			getAddForm: getAddForm,
			// 保存
			add: addCoopMemo,
			getBankData: getBankData,
			getBankForm: getBankForm,
			look: look,
			getTableForm: getTableForm,
			
			// 附件上传相关加载
			getUploadForm : getUploadForm,
			loadUpload : loadUpload,
			changeHandle: changeHandle,
			getProjectId: function (data){
				this.projectId = data.tableId;
				addVue.changeHandle();
			},
		},
		computed: {

		},
		components: {
			"my-uploadcomponent":fileUpload,
			'vue-select': VueSelect,
		},
		watch: {

		}
	});

	//------------------------方法定义-开始------------------//
	function getTableForm() {
		return {
			interfaceVersion: this.interfaceVersion,
			tableId: addVue.tableId,
		}
	}

	//详情操作--------------
	function refresh() {
		this.tgxy_CoopMemoModel = {};
		addVue.eCode = "";
		addVue.tableId = "";
		addVue.date = "";
	}

    // 根据选择的下拉框数据的id获取对应的text值
    function look()
    {
    	console.log(addVue.tableId);
		new ServerInterface(baseInfo.getBankInfoDetailInterface).execute(addVue.getTableForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{	
				console.log(jsonObj);
				addVue.bankId = addVue.tableId;
				addVue.theNameOfBank = jsonObj.emmp_BankInfo.theName;
			}
		});
    }
    
	//保存操作--------------获取"合作备忘录"参数
	function getAddForm() {
		var fileUploadList = addVue.$refs.listenUploadData.uploadData;
		fileUploadList = JSON.stringify(fileUploadList);
		return {
			interfaceVersion: this.interfaceVersion,
			eCode: addVue.eCode,
			userStartId: addVue.userStartId,
			tableId: addVue.tableId,
            bankId: addVue.bankId,
            projectId: addVue.projectId,
            logDate: document.getElementById("date2102030302").value,
            pjRiskRatingId: addVue.gradeId,
			riskLog: addVue.riskLog,
			smAttachmentList:fileUploadList
		}
	}
	function getBankForm() {
		return {
			interfaceVersion: this.interfaceVersion,
			pageNumber: this.pageNumber
		}
	}

	function addCoopMemo() {
		new ServerInterface(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
				enterNext2Tab(jsonObj.tableId, '项目风险日志详情', 'tg/Tg_ProjectRiskLogDetail.shtml',jsonObj.tableId+"21020303");
			}
		});
	}

	function initData() {

	}

	// 加载项目数据
	function getBankData() 
	{
		new ServerInterface(baseInfo.getInterface).execute(addVue.getBankForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
                // 项目数据加载
                addVue.projectList = jsonObj.empj_ProjectInfoList;
                addVue.areaId = jsonObj.empj_ProjectInfoList.cityRegionName;
                addVue.buisId = jsonObj.empj_ProjectInfoList.developCompanyName;
			}
		});
	}
	
	//列表操作-----------------------获取附件参数
		function getUploadForm(){
			return{
				pageNumber : '0',
				busiType : addVue.busiType,
				interfaceVersion:this.interfaceVersion
			}
		}
		
		//列表操作-----------------------页面加载显示附件类型
		function loadUpload(){
			new ServerInterface(baseInfo.loadUploadInterface).execute(addVue.getUploadForm(), function(jsonObj)
			{
				if(jsonObj.result != "success")
				{
					$(baseInfo.errorModel).modal("show",{
						backdrop :'static'
					});
					addVue.errMsg = jsonObj.info;
				}
				else
				{
					//refresh();
					addVue.loadUploadList = jsonObj.sm_AttachmentCfgList; 
				}
			});
		}
		
		function changeHandle()
		{
			
			addVue.gradeName = "";
			
			var model = {
			interfaceVersion: this.interfaceVersion,
			projectId: addVue.projectId,
			tableId : addVue.projectId,
			
			}
			new ServerInterface(baseInfo.detailInterface).execute(model, function(jsonObj) {
				if(jsonObj.result != "success") {
					generalErrorModal(jsonObj);
				} else {
					addVue.gradeName = jsonObj.tg_PjRiskRatingDetail.theLevel;
					if(addVue.gradeName == 0) {
						addVue.gradeName = "高"
					}
					if(addVue.gradeName == 1) {
						addVue.gradeName = "中"
					}
					if(addVue.gradeName == 2) {
						addVue.gradeName = "低"
					}
					addVue.gradeId = jsonObj.tg_PjRiskRatingDetail.tableId;
					console.log(jsonObj.tg_PjRiskRatingDetail)
				}
			});
			
			new ServerInterface(baseInfo.detail1Interface).execute(model, function(jsonObj) {
				if(jsonObj.result != "success") {
					generalErrorModal(jsonObj);
				} else {
					addVue.buisId = jsonObj.empj_ProjectInfo.developCompanyName;
					addVue.areaId = jsonObj.empj_ProjectInfo.cityRegionName;
				}
			});
		}
	
	// 添加日期控件
	laydate.render({
		elem: '#date2102030302',
	});
	//------------------------方法定义-结束------------------//

	//------------------------数据初始化-开始----------------//
	addVue.refresh();
	addVue.initData();
	addVue.getBankData();
	addVue.loadUpload();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId": "#tg_ProjectRiskLogDiv",
	"detailInterface": "../Tg_PjRiskRatingPreForRiskLogList",
	"detail1Interface": "../Empj_ProjectInfoDetail",
	"addInterface": "../Tg_RiskLogInfoAdd",
	"getInterface": "../Empj_ProjectInfoList",
	"getBankInfoDetailInterface": "../Emmp_BankInfoDetail",
	"loadUploadInterface":"../Sm_AttachmentCfgList",
});