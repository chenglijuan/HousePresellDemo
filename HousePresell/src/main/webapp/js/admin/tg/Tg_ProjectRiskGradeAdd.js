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
			emmp_BankInfoList: [],
			uploadData : [],
			errMsg: "",
			
			// 附件上传相关参数
			loadUploadList: [],
            showDelete : true,
            busiType : '21020302',
			areaId: "",
			tg_AreaList: [],
			buisId: "",
			buisList: [],
			gradeId: "",
			gradeList: [],
			riskLog: "",
			projectId: "",
			projectList: "",
			asa: ""
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
		document.getElementById("date2102030202").value = "";
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
			operateDate: document.getElementById("date2102030202").value,
			theLevel: addVue.gradeId,
			riskNotification: addVue.riskLog,
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
				enterNext2Tab(jsonObj.tableId, '项目风险评级详情', 'tg/Tg_ProjectRiskGradeDetail.shtml',jsonObj.tableId+"21020302");
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
			var model = {
			interfaceVersion: this.interfaceVersion,
			tableId: addVue.projectId,
			}
			new ServerInterface(baseInfo.detailInterface).execute(model, function(jsonObj) {
				if(jsonObj.result != "success") {
					generalErrorModal(jsonObj);
				} else {
					addVue.areaId = jsonObj.empj_ProjectInfo.cityRegionName;
					addVue.buisId = jsonObj.empj_ProjectInfo.developCompanyName;
				}
			});
		}
	
	// 添加日期控件
	laydate.render({
		elem: '#date2102030202',
		done:function(value){
			addVue.asa = value;
		}
	});
	//------------------------方法定义-结束------------------//

	//------------------------数据初始化-开始----------------//
	addVue.refresh();
	addVue.initData();
	addVue.getBankData();
	addVue.loadUpload();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId": "#tg_ProjectRiskGradeAddDiv",
	"detailInterface": "../Empj_ProjectInfoDetail",
	"addInterface": "../Tg_PjRiskRatingAdd",
	"getInterface": "../Empj_ProjectInfoList",
	"getBankInfoDetailInterface": "../Emmp_BankInfoDetail",
	"loadUploadInterface":"../Sm_AttachmentCfgList",
});