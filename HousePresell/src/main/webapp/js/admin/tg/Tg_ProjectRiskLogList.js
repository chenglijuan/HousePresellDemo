(function(baseInfo) {
	var listVue = new Vue({
		el: baseInfo.listDivId,
		data: {
			interfaceVersion: 19000101,
			pageNumber: 1,
			countPerPage: 10,
			totalPage: 1,
			totalCount: 1,
			keyword: "",
			signDate: null,
			selectItem: [],
			isAllSelected: false,
			theState: 0, //正常为0，删除为1
			userStartId: null,
			userStartList: [],
			userRecordId: null,
			userRecordList: [],
			bankId: null,
			bankList: [],
			tg_ProjectRiskLogList: [],
			tableId: null,
			tableidShow: false,
			idArr:[],
			selNum:0,
			areaId: "",
			tg_AreaList: [],
			projectId: "",
			projectList: [],
			buisId: "",
			buisList: [],
			gradeId: "",
			gradeList: [],
			dateRange: "",
		},
		methods: {
			refresh: refresh,
			indexMethod: indexMethod,
			getSearchForm: getSearchForm,
			getDeleteForm: getDeleteForm,
			tgxy_CoopMemoDelOne: tgxy_CoopMemoDelOne,
			tgxy_CoopMemoDel: tgxy_CoopMemoDel,
			showAjaxModal: showAjaxModal,
			search: search,
			checkAllClicked: checkAllClicked,
			// 新增项目风险日志
			coopMemoAddHandle: coopMemoAddHandle,
			// 修改项目风险日志
			coopMemoEditHandle: coopMemoEditHandle,
			// 查看详情
			openDetails: openDetails,
			// 复选框选中状态改变
			handleSelectionChange: handleSelectionChange,
			resetInfo: resetInfo,
			exportExcel: exportExcel,
			changePageNumber: function (data) {
				console.log(data);
				if (listVue.pageNumber != data) {
					listVue.pageNumber = data;
					listVue.refresh();
				}
			},
			changeCountPerPage : function (data) {
				console.log(data);
				if (listVue.countPerPage != data) {
					listVue.countPerPage = data;
					listVue.refresh();
				}
			},
			changeHandle: changeHandle,
			handleClick: handleClick,
			initData : initData,
			projectRiskLogDel : projectRiskLogDel,
		},
		computed: {

		},
		components: {
			'vue-nav': PageNavigationVue
		},
		watch: {
			//pageNumber: refresh,
			selectItem: selectedItemChanged,
		}
	});
	var updateVue = new Vue({
		el: baseInfo.updateDivId,
		data: {
			interfaceVersion: 19000101,
			theState: null,
			busiState: null,
			eCode: null,
			userStartId: null,
			userStartList: [],
			createTimeStamp: null,
			userRecordId: null,
			userRecordList: [],
			recordTimeStamp: null,
			eCode: null,
			theNameOfBank: null,
			bankId: null,
			bankList: [],
			theNameOfBank: null,
			signDate: null,
			userUpdate: null,
			lastUpdateTimeStamp: null,
		},
		methods: {
			getUpdateForm: getUpdateForm,
			update: updateTgxy_CoopMemo
		}
	});
	var addVue = new Vue({
		el: baseInfo.addDivId,
		data: {
			interfaceVersion: 19000101,
			theState: null,
			busiState: null,
			eCode: null,
			userStartId: null,
			userStartList: [],
			createTimeStamp: null,
			userRecordId: null,
			userRecordList: [],
			recordTimeStamp: null,
			eCode: null,
			theNameOfBank: null,
			bankId: null,
			bankList: [],
			theNameOfBank: null,
			signDate: null,
			userUpdate: null,
			lastUpdateTimeStamp: null,
		},
		methods: {
			getAddForm: getAddForm,
			add: addTgxy_CoopMemo

		}
	});

	//------------------------方法定义-开始------------------//
	
	
	function resetInfo()
	{
		
		listVue.keyword = "";
		document.getElementById("date2102030301").value = "";
		
	}

	//列表操作--------------获取"搜索列表"表单参数
	function getSearchForm() {
		return {
			interfaceVersion: this.interfaceVersion,
			pageNumber: this.pageNumber,
			countPerPage: this.countPerPage,
			keyword: listVue.keyword,
			logDate: document.getElementById("date2102030301").value,
		}
	}
	//列表操作--------------获取“删除资源”表单参数
	function getDeleteForm() {
		return {
			interfaceVersion: this.interfaceVersion,
			idArr: listVue.idArr
		}
	}
	//列表操作--------------获取“新增”表单参数
	function getAddForm() {
		return {
			interfaceVersion: this.interfaceVersion,
			busiState: this.busiState,
			eCode: this.eCode,
			userStartId: this.userStartId,
			createTimeStamp: this.createTimeStamp,
			userRecordId: this.userRecordId,
			recordTimeStamp: this.recordTimeStamp,
			eCode: this.eCode,
			theNameOfBank: this.bank,
			bankId: this.bankId,
			theNameOfBank: this.theNameOfBank,
			signDate: this.signDate,
			userUpdate: this.userUpdate,
			lastUpdateTimeStamp: this.lastUpdateTimeStamp,
		}
	}
	//列表操作--------------获取“更新”表单参数
	function getUpdateForm() {
		return {
			interfaceVersion: this.interfaceVersion,
			theState: this.theState,
			busiState: this.busiState,
			eCode: this.eCode,
			userStartId: this.userStartId,
			createTimeStamp: this.createTimeStamp,
			userRecordId: this.userRecordId,
			recordTimeStamp: this.recordTimeStamp,
			eCode: this.eCode,
			theNameOfBank: this.theNameOfBank,
			bankId: this.bankId,
			theNameOfBank: this.theNameOfBank,
			signDate: this.signDate,
			userUpdate: this.userUpdate,
			lastUpdateTimeStamp: this.lastUpdateTimeStamp,
		}
	}

	//删除提示
	function tgxy_CoopMemoDel() {
		
		$(baseInfo.projectRiskLogDelDiv).modal({
		    backdrop :'static'
	    });
	}
	
	//删除
	function projectRiskLogDel(){
		$(baseInfo.projectRiskLogDelDiv).modal('hide');
		new ServerInterface(baseInfo.deleteInterface).execute(listVue.getDeleteForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
				generalSuccessModal();
				listVue.selectItem = [];
				refresh();
			}
		});
		
	}

	function tgxy_CoopMemoDelOne(tgxy_CoopMemoId) {
		var model = {
			interfaceVersion: 19000101,
			idArr: [tgxy_CoopMemoId],
		};

		noty({
			layout: 'center',
			modal: true,
			text: "确认删除吗？",
			type: "confirm",
			buttons: [{
					addClass: "btn btn-primary",
					text: "确定",
					onClick: function($noty) {
						$noty.close();
						new ServerInterface(baseInfo.deleteInterface).execute(model, function(jsonObj) {
							if(jsonObj.result != "success") {
								noty({
									"text": jsonObj.info,
									"type": "error",
									"timeout": 2000
								});
							} else {
								refresh();
							}
						});
					}
				},
				{
					addClass: "btn btn-danger",
					text: "取消",
					onClick: function($noty) {

						$noty.close();
					}
				}
			]

		});
	}
	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged() {
		
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked() {
		if(listVue.selectItem.length == listVue.tg_ProjectRiskLogList.length) {
			listVue.selectItem = [];
		} else {
			listVue.selectItem = []; //解决：已经选择一个复选框后，再次点击全选样式问题
			listVue.tg_ProjectRiskLogList.forEach(function(item) {
				listVue.selectItem.push(item.tableId);
			});
		}
	}
	//列表操作--------------刷新
	function refresh() {
		new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
				
				listVue.tg_ProjectRiskLogList = jsonObj.tg_RiskLogInfoList;
				//动态跳转到锚点处，id="top"
				document.getElementById('projectRiskLogListDiv').scrollIntoView();
			}
		});
		
		// 加载项目信息
		loadProjectData();
	}
	
	function loadProjectData()
	{
		var model = {
			interfaceVersion: listVue.interfaceVersion,
		}
		new ServerInterface(baseInfo.loadProjectInterface).execute(model, function(jsonObj) {
			if(jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
				listVue.projectList = jsonObj.empj_ProjectInfoList;
				listVue.areaId = jsonObj.empj_ProjectInfoList.cityRegionName;
				listVue.buisId = jsonObj.empj_ProjectInfoList.developCompanyName;
			}
		});
	}

	//列表操作------------搜索
	function search() {
		listVue.pageNumber = 1;
		refresh();
	}

	//弹出编辑模态框--更新操作
	function showAjaxModal(tgxy_CoopMemoModel) {
		updateVue.theState = tgxy_CoopMemoModel.theState;
		updateVue.busiState = tgxy_CoopMemoModel.busiState;
		updateVue.eCode = tgxy_CoopMemoModel.eCode;
		updateVue.userStartId = tgxy_CoopMemoModel.userStartId;
		updateVue.createTimeStamp = tgxy_CoopMemoModel.createTimeStamp;
		updateVue.userRecordId = tgxy_CoopMemoModel.userRecordId;
		updateVue.recordTimeStamp = tgxy_CoopMemoModel.recordTimeStamp;
		updateVue.eCode = tgxy_CoopMemoModel.eCode;
		updateVue.theNameOfBank = tgxy_CoopMemoModel.theNameOfBank;
		updateVue.bankId = tgxy_CoopMemoModel.bankId;
		updateVue.theNameOfBank = tgxy_CoopMemoModel.theNameOfBank;
		updateVue.signDate = tgxy_CoopMemoModel.signDate;
		updateVue.userUpdate = tgxy_CoopMemoModel.userUpdate;
		updateVue.lastUpdateTimeStamp = tgxy_CoopMemoModel.lastUpdateTimeStamp;
			$(baseInfo.updateDivId).modal('show', {
				backdrop: 'static'
			});
	}

	function updateTgxy_CoopMemo() {
		new ServerInterface(baseInfo.updateInterface).execute(updateVue.getUpdateForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				noty({
					"text": jsonObj.info,
					"type": "error",
					"timeout": 2000
				});
			} else {
				$(baseInfo.updateDivId).modal('hide');
				refresh();
			}
		});
	}

	function addTgxy_CoopMemo() {
		new ServerInterface(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				noty({
					"text": jsonObj.info,
					"type": "error",
					"timeout": 2000
				});
			} else {
				$(baseInfo.addDivId).modal('hide');
				addVue.busiState = null;
				addVue.eCode = null;
				addVue.userStartId = null;
				addVue.createTimeStamp = null;
				addVue.userRecordId = null;
				addVue.recordTimeStamp = null;
				addVue.eCode = null;
				addVue.theNameOfBank = null;
				addVue.bankId = null;
				addVue.theNameOfBank = null;
				addVue.signDate = null;
				addVue.userUpdate = null;
				addVue.lastUpdateTimeStamp = null;
					refresh();
			}
		});
	}

	function indexMethod(index) {
		return generalIndexMethod(index, listVue)
	}

	// 点击新增按钮，打开项目风险日志新增页面
	function coopMemoAddHandle() {
		enterNewTab('21020303', '新增项目风险日志', 'tg/Tg_ProjectRiskLogAdd.shtml');
	}

	// 点击修改按钮，打开项目风险日志修改页面
	function coopMemoEditHandle() {
	        enterNextTab(listVue.tableId, '修改项目风险日志', 'tg/Tg_ProjectRiskLogEdit.shtml',listVue.tableId+"21020303");
	}

	// 查看详情
	function openDetails(tableId) {
		enterNextTab(tableId, '项目风险日志详情', 'tg/Tg_ProjectRiskLogDetail.shtml',tableId+"21020303");
	}
	
	function changeHandle()
	{
		var model = {
			interfaceVersion: this.interfaceVersion,
			projectId: listVue.projectId,
		}
		new ServerInterface(baseInfo.detailInterface).execute(model, function(jsonObj) {
			if(jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
				listVue.gradeList = jsonObj.tg_PjRiskRatingDetail;
			}
		});
	}
	
	function handleClick(tableId)
	{
		enterNextTab(listVue.tableId, '项目风险日志跟踪', 'tg/Tg_ProjectRiskLogTrackDetail.shtml',listVue.tableId+"21020303");
	}

	// 复选框选中事件
	function handleSelectionChange(val) {
		listVue.idArr = [];
        // 获取选中需要修改的数据的tableId
        var length = val.length;
        listVue.selNum = length;
        if(length > 0){
	        for(var i = 0;i < length;i++){
	        	listVue.idArr.push(val[i].tableId);
	        }
	        console.log(listVue.idArr);
	        listVue.tableId = val[0].tableId;
        }
        listVue.isAllSelected = (listVue.tg_ProjectRiskLogList.length > 0) &&
		(listVue.selectItem.length == listVue.tg_ProjectRiskLogList.length);
	}
	
	// 导出--------数据导出为Excel
	function exportExcel() {
		flag = true;
		// 获取需要导出的table的dom节点
		var wb = XLSX.utils.table_to_book(document.querySelector('#coopMemoTab11'));
		// 写入列表数据并保存
		if(flag){
		    var wbout = XLSX.write(wb, { bookType: 'xlsx', bookSST: true, type: 'array' });
			flag = false;
		}
		console.log(wbout);
		
		try {
			// 导出
			saveAs(new Blob([wbout], { type: 'application/octet-stream' }), '项目风险日志.xlsx');
		} catch(e) {
			if(typeof console !== 'undefined') console.log(e, wbout)
		}
		return wbout
	}
	
	laydate.render({
	    elem: '#date2102030301',
	});
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}
	function initData(){
		initButtonList();
	}
	//------------------------方法定义-结束------------------//

	//------------------------数据初始化-开始----------------//
	listVue.initData();
	listVue.refresh();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId": "#projectRiskLogListDiv",
	"updateDivId": "#updateModel",
	"projectRiskLogDelDiv" : "#projectRiskLogDelModal",
	"listInterface": "../Tg_RiskLogInfoList",
	"addInterface": "../Tgxy_CoopMemoAdd",
	"deleteInterface": "../Tg_RiskLogInfoBatchDelete",
	"updateInterface": "../Tgxy_CoopMemoUpdate",
	"loadProjectInterface":"../Empj_ProjectInfoList",
	"detailInterface":"../Tg_PjRiskRatingPreForRiskLogList",
});