(function(baseInfo) {
	var listVue = new Vue({
		el: baseInfo.listDivId,
		data: {
			interfaceVersion: 19000101,
			pageNumber: 1,
			countPerPage: 3,
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
			tgxy_ToDoList: [{
					tableId: 1111,
					index: 1,
					eCode: 111,
					theNameOfBank: '中国银行',
					signDate: '2019-23-1',
					lastUpdateTimeStamp: '9023-23-1'
				},
				{
					tableId: 2222,
					index: 1,
					eCode: 111,
					theNameOfBank: '中国建行',
					signDate: '2019-23-1',
					lastUpdateTimeStamp: '9023-23-1'
				}
			],
			tgxy_NoticeList: [],
			tgxy_WarnList: [],
			tableId: null,
			tableidShow: false,
			idArr:[],
			selNum:0,
			selNum1:0,
			selNum2:0,
			activeTodo: true,
			activeNotice: true,
			activeWarn: false,
			isToDoList: true,
			isReadList: false,
			isWarnList: false,
			showToDoTab: true,
			showNoticeTab: false,
			showWarnTab: false,
			checkedEquipments: [],
			equipments: [   // 所有数据
	          {
	            id: '1',
	            childMenu: [{
	            	tableId: '1-1',
	                theTitle: '标题',
	                text: '内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容',
	                date: '2018-08-22'
	              }
	            ]
	          },
	          {
	            id: '2',
	            childMenu: [{
	                id: '1-2',
	                title: '标题',
	                text: '内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容',
	                date: '2018-08-22'
	              }
	            ]
	          },
	          {
	        	  id: '3',
		            childMenu: [{
		                id: '1-2',
		                title: '标题',
		                text: '内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容',
		                date: '2018-08-22'
		              }
		            ]  
	          },
	          {
	        	  id: '4',
		            childMenu: [{
		                id: '1-2',
		                title: '标题',
		                text: '内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容',
		                date: '2018-08-22'
		              }
		            ]  
	          }
	        ],
	        checkEquipArr: [],
	        warnType: [],
	        currentStatus: [],
	        defaultStatu: '',
	        idArr2 : [],
	        todo_num: 0,
	        read_num: 0,
	        warn_num: 0,
		},
		methods: {
			refresh: refresh,
			initData: initData,
			indexMethod: indexMethod,
			getSearchForm: getSearchForm,
			getDeleteForm: getDeleteForm,
			handleDelete: handleDelete,
			showAjaxModal: showAjaxModal,
			search: search,
			checkAllClicked: checkAllClicked,
			// 复选框选中状态改变
			handleSelectionChange: handleSelectionChange,
			handleWarnSelectionChange: handleWarnSelectionChange,
			resetInfo: resetInfo,
			exportExcel: exportExcel,
			errClose: errClose,
			succClose: succClose,
			coopMemoLogHandle: coopMemoLogHandle,
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
			handleRead: handleRead,
			handleReadNew: handleReadNew,
			handleReadWarn: handleReadWarn,
			handleConfirmation: handleConfirmation,
			handleToDoList: handleToDoList,
			handleNoticeList: handleNoticeList,
			handleWarnList: handleWarnList,
			handleChange (index, id) {
				var _this = this;
				var flag = _this.checkedEquipments[index];
		        if(flag) {
		        	_this.checkEquipArr.push(id);
		        }else {
		        	_this.checkEquipArr.splice(index, 1);
		        }
		        console.log(this.checkEquipArr);
		        if(this.checkEquipArr.length == 0) {
		        	listVue.seNum1 == 0;
		        }
		    },
		    handleDelete1: handleDelete1,
		    handleDelete2: handleDelete2,
		    getReadWarnForm: getReadWarnForm,
		    handleConduct: handleConduct,
		    openDetail: openDetail,
		    getNoticeForm: getNoticeForm,
		    getReadForm:getReadForm,
		    loadWarnType: loadWarnType,
		    loadCurrentStatu: loadCurrentStatu,
		    loadWarnList: loadWarnList,
		    getNotice1Form: getNotice1Form
		},
		created() {
	        // 初始化默认选中状态
	    },
		computed: {

		},
		components: {
			'vue-nav': PageNavigationVue
		},
		watch: {
			pageNumber: refresh,
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
		document.getElementById("todoList_Date").value = "";
		listVue.defaultStatu = "";
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

	function handleDelete() {
		if(listVue.selNum == 0){
			noty({"text":"请选择要删除的代办事项","type":"error","timeout":2000});
		}else{
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
	}

	function handleDelete1() {
		new ServerInterface(baseInfo.delete1Interface).execute(listVue.getReadWarnForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				$(baseInfo.errorModel).modal('show', {
				    backdrop :'static'
			    });
			} else {
				$(baseInfo.successModel).modal('show', {
				    backdrop :'static'
			    });
				handleNoticeList();
			}
		});
	}
	
	function handleDelete2() {
		new ServerInterface(baseInfo.delete1Interface).execute(listVue.getReadWarnForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				$(baseInfo.errorModel).modal('show', {
				    backdrop :'static'
			    });
			} else {
				$(baseInfo.successModel).modal('show', {
				    backdrop :'static'
			    });
				handleWarnList();
			}
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
	
	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged() {
		if(showToDoTab) {
			listVue.isAllSelected = (listVue.tgxy_ToDoList.length > 0) &&
				(listVue.selectItem.length == listVue.tgxy_ToDoList.length);
		}else if(showNoticeTab) {
			listVue.isAllSelected = (listVue.tgxy_NoticeList.length > 0) &&
			(listVue.selectItem.length == listVue.tgxy_NoticeList.length);
		}else {
			listVue.isAllSelected = (listVue.tgxy_WarnList.length > 0) &&
			(listVue.selectItem.length == listVue.tgxy_WarnList.length);
		}
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked() {
		if(showToDoTab) {
			if(listVue.selectItem.length == listVue.tgxy_ToDoList.length) {
				listVue.selectItem = [];
			} else {
				listVue.selectItem = []; //解决：已经选择一个复选框后，再次点击全选样式问题
				listVue.tgxy_ToDoList.forEach(function(item) {
					listVue.selectItem.push(item.tableId);
				});
			}
		}else if(showNoticeTab) {
			if(listVue.selectItem.length == listVue.tgxy_NoticeList.length) {
				listVue.selectItem = [];
			} else {
				listVue.selectItem = []; //解决：已经选择一个复选框后，再次点击全选样式问题
				listVue.tgxy_NoticeList.forEach(function(item) {
					listVue.selectItem.push(item.tableId);
				});
			}
		}else {
			if(listVue.selectItem.length == listVue.tgxy_WarnList.length) {
				listVue.selectItem = [];
			} else {
				listVue.selectItem = []; //解决：已经选择一个复选框后，再次点击全选样式问题
				listVue.tgxy_WarnList.forEach(function(item) {
					listVue.selectItem.push(item.tableId);
				});
			}
		}
	}
	//列表操作--------------刷新
	function refresh() { 
		var tableFlag = $("#tabContainer").data("tabs").getCurrentTabId();
		var tableArr = tableFlag.split("_");
		if(tableArr[2] == "wd"){
			handleNoticeList();
		}else if(tableArr[2] == "yj"){
			handleWarnList();
		}
		/*new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				noty({
					"text": jsonObj.info,
					"type": "error",
					"timeout": 2000
				});
			} else {
				
				listVue.tgxy_ToDoList = jsonObj.tgxy_ToDoList;
				listVue.pageNumber = jsonObj.pageNumber;
				listVue.countPerPage = jsonObj.countPerPage;
				listVue.totalPage = jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword = jsonObj.keyword;
				listVue.selectedItem = [];
				listVue.signDate = jsonObj.signDate;
				//动态跳转到锚点处，id="top"
				document.getElementById('index_ToDoListDiv').scrollIntoView();
			}
		});*/
	}

	//列表操作------------搜索
	function search() {
		listVue.pageNumber = 1;
		loadWarnList();
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

	function initData() {

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

	// 代办事项----------复选框选中事件
	function handleSelectionChange(val) {
        // 获取选中需要修改的数据的tableId
        var length = val.length;
        listVue.selNum = length;
        if(length > 0){
	        for(var i = 0;i < length;i++){
	        	listVue.idArr.push(val[i].tableId);
	        }
	        listVue.tableId = val[0].tableId;
        }
	}

	// 未处理预警----------复选框选中事件
	function handleWarnSelectionChange(val) {
		// 获取选中需要修改的数据的tableId
        var length = val.length;
        console.log(length)
        listVue.idArr2 = [];
        listVue.selNum2 = length;
        if(length > 0){
	        for(var i = 0;i < length;i++){
	        	listVue.idArr2.push(val[i].tableId);
	        }
        }
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
	
	function handleRead()
	{
		if(listVue.selNum == 0){
			noty({"text":"请选择要标为已读的代办事项","type":"error","timeout":2000});
		}else{
			noty({
				layout: 'center',
				modal: true,
				text: "确认更改状态为已读吗？",
				type: "confirm",
				buttons: [{
						addClass: "btn btn-primary",
						text: "确定",
						onClick: function($noty) {
							$noty.close();
							new ServerInterface(baseInfo.readInterface).execute(listVue.getDeleteForm(), function(jsonObj) {
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
	}
	
	function handleReadNew()
	{
		if(listVue.checkEquipArr.length == 0){
			noty({"text":"请选择要标为已读的公告","type":"error","timeout":2000});
		}else{
			noty({
				layout: 'center',
				modal: true,
				text: "确认更改状态为已读吗？",
				type: "confirm",
				buttons: [{
						addClass: "btn btn-primary",
						text: "确定",
						onClick: function($noty) {
							$noty.close();
							new ServerInterface(baseInfo.read1Interface).execute(listVue.getReadForm(), function(jsonObj) {
								if(jsonObj.result != "success") {
									$(baseInfo.errorModel).modal('show', {
									    backdrop :'static'
								    });
								} else {
									$(baseInfo.successModel).modal('show', {
									    backdrop :'static'
								    });
									getNoticeList();
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
	}
	
	function handleReadWarn()
	{
		new ServerInterface(baseInfo.readWarnInterface).execute(listVue.getReadWarnForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				$(baseInfo.errorModel).modal('show', {
				    backdrop :'static'
			    });
			} else {
				$(baseInfo.successModel).modal('show', {
				    backdrop :'static'
			    });
				listVue.selectItem = [];
				loadWarnList();
			}
		});
		
		/*
		
		new ServerInterface(baseInfo.readWarnInterface).execute(listVue.getReadWarnForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				$(baseInfo.errorModel).modal('show', {
				    backdrop :'static'
			    });
			} else {
				$(baseInfo.successModel).modal('show', {
				    backdrop :'static'
			    });
				listVue.selectItem = [];
				loadWarnList();
			}
		});*/
	}
	
	function getReadWarnForm()
	{
		return {
			interfaceVersion: 19000101,
			idArr: listVue.idArr2,
		}
	}
	
	function getReadForm()
	{
		return {
			interfaceVersion: 19000101,
			idArr: listVue.checkEquipArr,
		}
	}
	
	function handleConfirmation()
	{
		if(listVue.selNum == 0){
			noty({"text":"请选择要确认办理的代办事项","type":"error","timeout":2000});
		}else{
			noty({
				layout: 'center',
				modal: true,
				text: "确认办理吗？",
				type: "confirm",
				buttons: [{
						addClass: "btn btn-primary",
						text: "确定",
						onClick: function($noty) {
							$noty.close();
							new ServerInterface(baseInfo.confirmationInterface).execute(listVue.getDeleteForm(), function(jsonObj) {
								if(jsonObj.result != "success") {
									generalErrorModal(jsonObj);
								} else {
									generalSuccessModal();
									listVue.selectItem = [];
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
	}
	
	function handleToDoList()
	{
		listVue.isToDoList = true;
		listVue.isReadList = false;
		listVue.isWarnList = false;
		
		listVue.showToDoTab = true;
		listVue.showNoticeTab = false;
		listVue.showWarnTab = false;
		
		listVue.activeTodo = true;
		listVue.activeNotice = false;
		listVue.activeWarn = false;
	}
	
	function handleNoticeList()
	{
		listVue.isToDoList = false;
		listVue.isReadList = true;
		listVue.isWarnList = false;
		
		listVue.showToDoTab = false;
		listVue.showNoticeTab = true;
		listVue.showWarnTab = false;
		
		listVue.activeTodo = false;
		listVue.activeNotice = true;
		listVue.activeWarn = false;
		getNoticeList();
	}
	
	// 加载未读公告信息
	function getNoticeList()
	{
		new ServerInterface(baseInfo.loadNoticeInterface).execute(listVue.getNoticeForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				$(baseInfo.errorModel).modal('show', {
				    backdrop :'static'
			    });
			} else {
				listVue.equipments = jsonObj.Sm_CommonMessage;
				listVue.read_num = listVue.equipments.length;
			}
		});
	}
	
	function getNoticeForm()
	{
		return {
			interfaceVersion: 19000101,
		}
	}

	
	function handleWarnList()
	{
		listVue.isToDoList = false;
		listVue.isReadList = false;
		listVue.isWarnList = true;
		
		listVue.showToDoTab = false;
		listVue.showNoticeTab = false;
		listVue.showWarnTab = true;
		
		listVue.activeTodo = false;
		listVue.activeNotice = false;
		listVue.activeWarn = true;
		loadWarnType();
	}
	
	function loadWarnType()
	{
		new ServerInterface(baseInfo.warnInterface).execute(listVue.getDeleteForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				$(baseInfo.errorModel).modal('show', {
				    backdrop :'static'
			    });
			} else {
				listVue.warnType = jsonObj.warnType;
			}
		});
	}
	
	function loadCurrentStatu()
	{
		new ServerInterface(baseInfo.currentStatuInterface).execute(listVue.getDeleteForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				$(baseInfo.errorModel).modal('show', {
				    backdrop :'static'
			    });
			} else {
				listVue.currentStatus = jsonObj.currentStatus;
			}
		});
	}
	
	function handleConduct()
	{
		new ServerInterface(baseInfo.conductInterface).execute(listVue.getReadWarnForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
				listVue.currentStatus = jsonObj.currentStatus;
			}
		});
	}
	
	function openDetail(id)
	{
		$("#tabContainer").data("tabs").addTab({
			id: id,
			text: '通知公告',
			closeable: true,
			url: 'announcementDetail.shtml'
		});
	}
	
	function loadWarnList()
	{
		new ServerInterface(baseInfo.warnloadInterface).execute(listVue.getNotice1Form(), function(jsonObj) {
			if(jsonObj.result != "success") {
				$(baseInfo.errorModel).modal('show', {
				    backdrop :'static'
			    });
			} else {
				listVue.tgxy_WarnList = jsonObj.sm_CommonMessageDtlList;
				listVue.warn_num = listVue.tgxy_WarnList.length;
			}
		});
	}
	
	function getNotice1Form()
	{
		var date = "";
		console.log(document.getElementById('todoList_Date'));
		if(document.getElementById('todoList_Date') == null) {
			date = "";
		}else {
			date = document.getElementById('todoList_Date').value;
		}
		return {
			interfaceVersion: this.interfaceVersion,
			signDate: date,
			defaultStatu: listVue.defaultStatu,
		}
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.refresh();
	listVue.initData();
	// 加载预警业务类型
	listVue.loadWarnType();
	// 加载当前状态
	listVue.loadCurrentStatu();
	// 加载未处理预警
	listVue.loadWarnList();
	//------------------------数据初始化-结束----------------//
	
	// 添加日期控件
	laydate.render({
		elem: '#IndextodoList_Date',
	});
		
})({
	"listDivId": "#index_ToDoListDiv",
	"updateDivId": "#updateModel",
	"addDivId": "#addModal",
	"errorModel":"#edModel",
	"successModel":"#sdModel",
	"listInterface": "../Tgxy_CoopMemoList",
	"deleteInterface": "../Tgxy_CoopMemoBatchDelete",
	"delete1Interface": "../Tg_ComMesgWaringBatchDelete",
	"updateInterface": "../Tgxy_CoopMemoUpdate",
	"readInterface": "../Tgxy_CoopMemoUpdate",
	"read1Interface": "../Sm_CommonMessageNoticeUpdate",
	"confirmationInterface": "../Tgxy_CoopMemoUpdate",
	"warnInterface": "",
	//"currentStatuInterface": "../Tgxy_CoopMemoUpdate",
	"readWarnInterface": "../Tg_ComMesgWaringReader",//Tg_ComMesgWaringReader
	"conductInterface": "../Tg_ComMesgWaringUpdate",
	"loadNoticeInterface":"../Sm_CommonMessageNoticeList",
	"warnloadInterface":"../Tg_ComMesgWaringList"
});