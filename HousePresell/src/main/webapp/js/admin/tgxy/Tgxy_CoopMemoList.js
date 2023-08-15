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
			tgxy_CoopMemoList: [],
			tableId: null,
			tableidShow: false,
			idArr:[],
			selNum:0
		},
		methods: {
			refresh: refresh,
			initData: initData,
			indexMethod: indexMethod,
			getSearchForm: getSearchForm,
			getDeleteForm: getDeleteForm,
			tgxy_CoopMemoDelOne: tgxy_CoopMemoDelOne,
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
			errClose: errClose,
			succClose: succClose,
			coopMemoLogHandle: coopMemoLogHandle,
			changePageNumber: function (data) {
				if (listVue.pageNumber != data) {
					listVue.pageNumber = data;
					listVue.refresh();
				}
			},
			changeCountPerPage : function (data) {
				if (listVue.countPerPage != data) {
					listVue.countPerPage = data;
					listVue.refresh();
				}
			},
			saClose: function(){
				$(baseInfo.warnModel).modal('hide');
				new ServerInterface(baseInfo.deleteInterface).execute(listVue.getDeleteForm(), function(jsonObj) {
					if(jsonObj.result != "success") {
						$(baseInfo.errorModel).modal('show', {
						    backdrop :'static'
					    });
					} else {
						$(baseInfo.successModel).modal('show', {
						    backdrop :'static'
					    });
						listVue.selectItem = [];
						refresh();
					}
				});
			},
			swClose: function(){
				$(baseInfo.warnModel).modal('hide');
			}
		},
		computed: {

		},
		components: {
			'vue-nav': PageNavigationVue
		},
		watch: {
		//	pageNumber: refresh,
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
		document.getElementById("date0611010201").value = "";
		refresh();
	}

	//列表操作--------------获取"搜索列表"表单参数
	function getSearchForm() {
		return {
			interfaceVersion: this.interfaceVersion,
			pageNumber: this.pageNumber,
			countPerPage: this.countPerPage,
			totalPage: this.totalPage,
			keyword: this.keyword,
			theState: this.theState,
			userStartId: this.userStartId,
			userRecordId: this.userRecordId,
			bankId: this.bankId,
			signDate: document.getElementById('date0611010201').value,
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
		$(baseInfo.warnModel).modal({
		    backdrop :'static'
	    });
	}
	
	function errClose()
	{
		$(baseInfo.errorModel).modal('hide');
	}
	
	function succClose()
	{
		$(baseInfo.successModel).modal('hide');
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
		if(listVue.selectItem.length == listVue.tgxy_CoopMemoList.length) {
			listVue.selectItem = [];
		} else {
			listVue.selectItem = []; //解决：已经选择一个复选框后，再次点击全选样式问题
			listVue.tgxy_CoopMemoList.forEach(function(item) {
				listVue.selectItem.push(item.tableId);
			});
		}
	}
	//列表操作--------------刷新
	function refresh() {
		new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				noty({
					"text": jsonObj.info,
					"type": "error",
					"timeout": 2000
				});
			} else {
				
				listVue.tgxy_CoopMemoList = jsonObj.tgxy_CoopMemoList;
				listVue.pageNumber = jsonObj.pageNumber;
				listVue.countPerPage = jsonObj.countPerPage;
				listVue.totalPage = jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword = jsonObj.keyword;
				listVue.selectedItem = [];
				listVue.signDate = jsonObj.signDate;
				//动态跳转到锚点处，id="top"
				document.getElementById('tgxy_CoopMemoListDiv').scrollIntoView();
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
		//tgxy_CoopMemoModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'tgxy_CoopMemo', tgxy_CoopMemoModel);
		//updateVue.$set("tgxy_CoopMemo", tgxy_CoopMemoModel);

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
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}
	function initData() {
		initButtonList();
	}

	// 点击新增按钮，打开合作备忘录新增页面
	function coopMemoAddHandle() {
		enterNewTab('', '新增合作备忘录', 'tgxy/Tgxy_CoopMemoAdd.shtml');
		//enterNextTab('Tgxy_CoopMemoAdd', '新增合作备忘录', 'tgxy/Tgxy_CoopMemoAdd.shtml',"");
		/*$("#tabContainer").data("tabs").addTab({
			id: 'Tgxy_CoopMemoAdd',
			text: '新增合作备忘录',
			closeable: true,
			url: 'tgxy/Tgxy_CoopMemoAdd.shtml'
		});*/
	}

	// 点击修改按钮，打开合作备忘录修改页面
	function coopMemoEditHandle() {
	        enterNextTab(listVue.tableId, '修改合作备忘录', 'tgxy/Tgxy_CoopMemoEdit.shtml',listVue.tableId+"06110102");
			/*$("#tabContainer").data("tabs").addTab({
				id: listVue.tableId,
				text: '修改合作备忘录',
				closeable: true,
				url: 'tgxy/Tgxy_CoopMemoEdit.shtml'
			});*/
	}

	// 查看详情
	function openDetails(tableId) {
		enterNextTab(tableId, '合作备忘录详情', 'tgxy/Tgxy_CoopMemoDetail.shtml',tableId+"06110102");
		/*$("#tabContainer").data("tabs").addTab({
			id: tableId,
			text: '合作备忘录详情',
			closeable: true,
			url: 'tgxy/Tgxy_CoopMemoDetail.shtml'
		});*/
	}
	
	// 查看日志
	function coopMemoLogHandle() {
		$("#tabContainer").data("tabs").addTab({
			id: 'Tgxy_CoopMemoLog',
			text: '新增合作备忘录',
			closeable: true,
			url: 'tgxy/Tgxy_CoopMemoLog.shtml'
		});
	}

	// 复选框选中事件
	function handleSelectionChange(val) {
        // 获取选中需要修改的数据的tableId
        var length = val.length;
        listVue.selNum = length;
        console.log("selNum:"+listVue.selNum);
        if(length > 0){
	        for(var i = 0;i < length;i++){
	        	listVue.idArr.push(val[i].tableId);
	        }
	        console.log(listVue.idArr);
	        listVue.tableId = val[0].tableId;
        }
        listVue.isAllSelected = (listVue.tgxy_CoopMemoList.length > 0) &&
		(listVue.selectItem.length == listVue.tgxy_CoopMemoList.length);
	}
	
	// 导出--------数据导出为Excel
	function exportExcel() {
		flag = true;
		// 获取需要导出的table的dom节点
		var wb = XLSX.utils.table_to_book(document.querySelector('#coopMemoTab'));
		// 写入列表数据并保存
		if(flag){
		    var wbout = XLSX.write(wb, { bookType: 'xlsx', bookSST: true, type: 'array' });
			flag = false;
		}
		console.log(wbout);
		
		try {
			// 导出
			saveAs(new Blob([wbout], { type: 'application/octet-stream' }), '合作备忘录.xlsx');
		} catch(e) {
			if(typeof console !== 'undefined') console.log(e, wbout)
		}
		return wbout
	}
	//------------------------方法定义-结束------------------//

	//------------------------数据初始化-开始----------------//
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//

	// 添加日期控件
	laydate.render({
		elem: '#date0611010201',
	});
})({
	"listDivId": "#tgxy_CoopMemoListDiv",
	"updateDivId": "#updateModel",
	"warnModel":"#warnModel",
	"addDivId": "#addModal",
	"errorModel":"#edModel",
	"successModel":"#sdModel",
	"listInterface": "../Tgxy_CoopMemoList",
	"addInterface": "../Tgxy_CoopMemoAdd",
	"deleteInterface": "../Tgxy_CoopMemoBatchDelete",
	"updateInterface": "../Tgxy_CoopMemoUpdate"
});