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
			operateDateBegin: "",
			operateDateEnd: "",
			gradeList : [
				{tableId:"0",theName:"高"},
				{tableId:"1",theName:"中"},
				{tableId:"2",theName:"低"},
			]
		},
		methods: {
			refresh: refresh,
			indexMethod: indexMethod,
			getSearchForm: getSearchForm,
			getDeleteForm: getDeleteForm,
			tgxy_CoopMemoDel: tgxy_CoopMemoDel,
			showAjaxModal: showAjaxModal,
			search: search,
			checkAllClicked: checkAllClicked,
			// 新增合作备忘录
			coopMemoAddHandle: coopMemoAddHandle,
			// 修改合作备忘录
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
			changeGrade : function(data){
				this.gradeId = data.tableId;
			},
			gradeEmpty : function(){
				this.gradeId = null
			}
		},
		computed: {

		},
		components: {
			'vue-nav': PageNavigationVue,
			'vue-listselect' : VueListSelect,
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
		document.getElementById("date2102030201").value = "";
		listVue.gradeId = "";
		listVue.dateRange = "";
		generalResetSearch(listVue, function () {
            refresh()
        })
	}

	//列表操作--------------获取"搜索列表"表单参数
	function getSearchForm() {
		return {
			interfaceVersion: this.interfaceVersion,
			pageNumber: this.pageNumber,
			countPerPage: this.countPerPage,
			totalPage: this.totalPage,
			keyword: listVue.keyword,
			theLevel: listVue.gradeId,
			operateDateBegin: listVue.operateDateBegin,
			operateDateEnd: listVue.operateDateEnd,
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
	function tgxy_CoopMemoDel() {
		generalSelectModal(delProjectRisk, "确认删除吗？");
	}
	
	function delProjectRisk(){
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
				
				listVue.tg_ProjectRiskLogList = jsonObj.tg_PjRiskRatingList;
				listVue.pageNumber = jsonObj.pageNumber;
				listVue.countPerPage = jsonObj.countPerPage;
				listVue.totalPage = jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword = jsonObj.keyword;
				listVue.selectedItem = [];
				listVue.signDate = jsonObj.signDate;
				//动态跳转到锚点处，id="top"
				document.getElementById('projectRiskGradeListDiv').scrollIntoView();
			}
		});
		
		// 加载企业信息
		//loadProjectData();
	}
	
	function loadProjectData()
	{
		var model = {
			interfaceVersion: this.interfaceVersion,
		}
		new ServerInterface(baseInfo.loadProjectInterface).execute(model, function(jsonObj) {
			if(jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
				listVue.projectList = jsonObj.projectList;
			}
		});
	}

	//列表操作------------搜索
	function search() {
		var str = document.getElementById("date2102030201").value;
		
		str = str.split(" - ");
		listVue.operateDateBegin = str[0];
		listVue.operateDateEnd = str[1];
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

	// 点击新增按钮，打开合作备忘录新增页面
	function coopMemoAddHandle() {
		enterNewTab('21020302', '新增项目风险评级', 'tg/Tg_ProjectRiskGradeAdd.shtml');
	}

	// 点击修改按钮，打开合作备忘录修改页面
	function coopMemoEditHandle() {
	        enterNextTab(listVue.tableId, '修改项目风险评级', 'tg/Tg_ProjectRiskGradeEdit.shtml',listVue.tableId+"21020302");
	}

	// 查看详情
	function openDetails(tableId) {
		enterNextTab(tableId, '项目风险评级详情', 'tg/Tg_ProjectRiskGradeDetail.shtml',tableId+"21020302");
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
				listVue.tg_AreaList = jsonObj.tg_AreaList;
				listVue.buisList = jsonObj.buisList;
				listVue.gradeList = jsonObj.gradeList;
			}
		});
	}
	
	function handleClick(tableId)
	{
		console.log(tableId);
	}

	// 复选框选中事件
	function handleSelectionChange(val) {
        // 获取选中需要修改的数据的tableId
		listVue.idArr = [];
        var length = val.length;
        listVue.selNum = length;
        if(length > 0){
	        for(var i = 0;i < length;i++){
	        	listVue.idArr.push(val[i].tableId);
	        }
	        listVue.tableId = val[0].tableId;
        }
        listVue.isAllSelected = (listVue.tg_ProjectRiskLogList.length > 0) &&
		(listVue.selectItem.length == listVue.tg_ProjectRiskLogList.length);
	}
	
	// 导出--------数据导出为Excel
	function exportExcel() {
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
	
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}
	function initData(){
		initButtonList();
	}
	laydate.render({
	    elem: '#date2102030201',
	    range:true,
	    done:function(value){
	    	listVue.dateRange = value;
	    }
	});
	//------------------------方法定义-结束------------------//

	//------------------------数据初始化-开始----------------//
	listVue.initData();
	listVue.refresh();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId": "#projectRiskGradeListDiv",
	"updateDivId": "#updateModel",
	"listInterface": "../Tg_PjRiskRatingList",
	"addInterface": "../Tgxy_CoopMemoAdd",
	"deleteInterface": "../Tg_PjRiskRatingBatchDelete",
	"updateInterface": "../Tgxy_CoopMemoUpdate",
	"loadProjectInterface":"../",
	"detailInterface":"../",
	"exportInterface":"../Tg_PjRiskRatingExportExcel",//导出excel接口
});