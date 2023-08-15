(function(baseInfo) {
	var listVue = new Vue({
		el: baseInfo.listDivId,
		data: {
			interfaceVersion: 19000101,
			pageNumber: 1,
			countPerPage: 20,
			totalPage: 1,
			totalCount: 1,
			keyword: "",
			selectItem: [],
			isAllSelected: false,
			theState: 0, //正常为0，删除为1
			userStartId: null,
			userStartList: [],
			userRecordId: null,
			userRecordList: [],
			bankId: null,
			bankList: [],
			tgxy_DepositAgreementList: [],
			eCode: null,
			bankOfDeposit: null,
			beginExpirationDate: null,
			endExpirationDate: null,
			userUpdate: null,
			lastUpdateTimeStamp: null,
			idArr: [],
			selNum: 0,
			pkId: "",
			errMsg: ""
		},
		methods: {
			refresh: refresh,
			initData: initData,
			indexMethod: indexMethod,
			getSearchForm: getSearchForm,
			getDeleteForm: getDeleteForm,
			tgxy_DepositAgreementDelOne: tgxy_DepositAgreementDelOne,
			tgxy_DepositAgreementDel: tgxy_DepositAgreementDel,
			showAjaxModal: showAjaxModal,
			search: search,
			checkAllClicked: checkAllClicked,
			resetInfo: resetInfo,
			openDetails: openDetails,
			add: add,
			depositAgreementEditHandle: depositAgreementEditHandle,
			handleSelectionChange: handleSelectionChange,
			exportExcel: exportExcel,
			errClose: errClose,
			succClose: succClose,
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
			saClose2: function() {
				$(baseInfo.warnModel2).modal('hide');
				new ServerInterface(baseInfo.deleteInterface).execute(listVue.getDeleteForm(), function(jsonObj) {
					if(jsonObj.result != "success") {
						listVue.errMsg = jsonObj.info;
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
			swClose2: function() {
				$(baseInfo.warnModel2).modal('hide');
			}
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
			lastUpdateTimeStamp: null,
			userRecordId: null,
			userRecordList: [],
			recordTimeStamp: null,
			bankId: null,
			bankList: [],
			theNameOfBank: null,
			escrowAccount: null,
			depositRate: null,
			orgAmount: null,
			signDate: null,
			timeLimit: null,
			beginExpirationDate: null,
			endExpirationDate: null,
			remark: null,
			eCode: null,
			bankOfDeposit: null,
			userUpdate: null,
		},
		methods: {
			getUpdateForm: getUpdateForm,
			update: updateTgxy_DepositAgreement
		}
	});
	var addVue = new Vue({
		el: baseInfo.addDivId,
		data: {
			interfaceVersion: 19000101,
			userStartList: [],
			userRecordList: [],
			bankList: [],
			theNameOfBank: null,
			escrowAccount: null,
			depositRate: null,
			orgAmount: null,
			signDate: null,
			timeLimit: null,
			remark: null,
			eCode: null,
			bankOfDeposit: null,
			beginExpirationDate: null,
			endExpirationDate: null,
			userUpdate: null,
			lastUpdateTimeStamp: null
		},
		methods: {
			getAddForm: getAddForm,
			add: addTgxy_DepositAgreement
		}
	});

	//------------------------方法定义-开始------------------//

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
			signDate: document.getElementById("date0611010101").value,
		}
	}
	//列表操作--------------获取“删除资源”表单参数
	function getDeleteForm() {
		return {
			interfaceVersion: this.interfaceVersion,
			idArr: this.idArr
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
			lastUpdateTimeStamp: this.lastUpdateTimeStamp,
			userRecordId: this.userRecordId,
			recordTimeStamp: this.recordTimeStamp,
			bankId: this.bankId,
			theNameOfBank: this.theNameOfBank,
			escrowAccount: this.escrowAccount,
			depositRate: this.depositRate,
			orgAmount: this.orgAmount,
			signDate: this.signDate,
			timeLimit: this.timeLimit,
			beginExpirationDate: this.beginExpirationDate,
			endExpirationDate: this.endExpirationDate,
			remark: this.remark,
			eCode: this.eCode,
			bankOfDeposit: this.bankOfDeposit,
			userUpdate: this.userUpdate,
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
			lastUpdateTimeStamp: this.lastUpdateTimeStamp,
			userRecordId: this.userRecordId,
			recordTimeStamp: this.recordTimeStamp,
			bankId: this.bankId,
			theNameOfBank: this.theNameOfBank,
			escrowAccount: this.escrowAccount,
			depositRate: this.depositRate,
			orgAmount: this.orgAmount,
			signDate: this.signDate,
			timeLimit: this.timeLimit,
			beginExpirationDate: this.beginExpirationDate,
			endExpirationDate: this.endExpirationDate,
			remark: this.remark,
			eCode: this.eCode,
			bankOfDeposit: this.bankOfDeposit,
			userUpdate: this.userUpdate,
		}
	}

	function tgxy_DepositAgreementDel() {
		$(baseInfo.warnModel2).modal({
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

	function tgxy_DepositAgreementDelOne(tgxy_DepositAgreementId) {
		var model = {
			interfaceVersion: 19000101,
			idArr: [tgxy_DepositAgreementId],
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
								noty({ "text": jsonObj.info, "type": "error", "timeout": 2000 });
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
		listVue.isAllSelected = (listVue.tgxy_DepositAgreementList.length > 0) &&
			(listVue.selectItem.length == listVue.tgxy_DepositAgreementList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked() {
		if(listVue.selectItem.length == listVue.tgxy_DepositAgreementList.length) {
			listVue.selectItem = [];
		} else {
			listVue.selectItem = []; //解决：已经选择一个复选框后，再次点击全选样式问题
			listVue.tgxy_DepositAgreementList.forEach(function(item) {
				listVue.selectItem.push(item.tableId);
			});
		}
	}
	//列表操作--------------刷新
	function refresh() {
		new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				noty({ "text": jsonObj.info, "type": "error", "timeout": 2000 });
			} else {
				listVue.tgxy_DepositAgreementList = jsonObj.tgxy_DepositAgreementList;
				
				if(listVue.tgxy_DepositAgreementList!=undefined&&listVue.tgxy_DepositAgreementList.length>0)
				{
					listVue.tgxy_DepositAgreementList.forEach(function(item){
						//item.depositRate = addThousands(item.depositRate);
						item.orgAmount = addThousands(item.orgAmount);
					})
				}
				
				listVue.pageNumber = jsonObj.pageNumber;
				listVue.countPerPage = jsonObj.countPerPage;
				listVue.totalPage = jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword = jsonObj.keyword;
				listVue.selectedItem = [];
				//动态跳转到锚点处，id="top"
				document.getElementById('tgxy_DepositAgreementListDiv').scrollIntoView();
			}
		});
	}

	//列表操作------------搜索
	function search() {
		listVue.pageNumber = 1;
		refresh();
	}

	//弹出编辑模态框--更新操作
	function showAjaxModal(tgxy_DepositAgreementModel) {
		//tgxy_DepositAgreementModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'tgxy_DepositAgreement', tgxy_DepositAgreementModel);
		//updateVue.$set("tgxy_DepositAgreement", tgxy_DepositAgreementModel);

		updateVue.theState = tgxy_DepositAgreementModel.theState;
		updateVue.busiState = tgxy_DepositAgreementModel.busiState;
		updateVue.eCode = tgxy_DepositAgreementModel.eCode;
		updateVue.userStartId = tgxy_DepositAgreementModel.userStartId;
		updateVue.createTimeStamp = tgxy_DepositAgreementModel.createTimeStamp;
		updateVue.lastUpdateTimeStamp = tgxy_DepositAgreementModel.lastUpdateTimeStamp;
		updateVue.userRecordId = tgxy_DepositAgreementModel.userRecordId;
		updateVue.recordTimeStamp = tgxy_DepositAgreementModel.recordTimeStamp;
		updateVue.bankId = tgxy_DepositAgreementModel.bankId;
		updateVue.theNameOfBank = tgxy_DepositAgreementModel.theNameOfBank;
		updateVue.escrowAccount = tgxy_DepositAgreementModel.escrowAccount;
		updateVue.depositRate = tgxy_DepositAgreementModel.depositRate;
		updateVue.orgAmount = tgxy_DepositAgreementModel.orgAmount;
		updateVue.signDate = tgxy_DepositAgreementModel.signDate;
		updateVue.timeLimit = tgxy_DepositAgreementModel.timeLimit;
		updateVue.beginExpirationDate = tgxy_DepositAgreementModel.beginExpirationDate;
		updateVue.endExpirationDate = tgxy_DepositAgreementModel.endExpirationDate;
		updateVue.remark = tgxy_DepositAgreementModel.remark;
		updateVue.eCode = tgxy_DepositAgreementModel.eCode;
		updateVue.bankOfDeposit = tgxy_DepositAgreementModel.bankOfDeposit;
		updateVue.userUpdate = tgxy_DepositAgreementModel.userUpdate;
		$(baseInfo.updateDivId).modal('show', {
			backdrop: 'static'
		});
	}

	function updateTgxy_DepositAgreement() {
		new ServerInterface(baseInfo.updateInterface).execute(updateVue.getUpdateForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				noty({ "text": jsonObj.info, "type": "error", "timeout": 2000 });
			} else {
				$(baseInfo.updateDivId).modal('hide');
				refresh();
			}
		});
	}

	function addTgxy_DepositAgreement() {
		new ServerInterface(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				noty({ "text": jsonObj.info, "type": "error", "timeout": 2000 });
			} else {
				$(baseInfo.addDivId).modal('hide');
				addVue.busiState = null;
				addVue.eCode = null;
				addVue.userStartId = null;
				addVue.createTimeStamp = null;
				addVue.lastUpdateTimeStamp = null;
				addVue.userRecordId = null;
				addVue.recordTimeStamp = null;
				addVue.bankId = null;
				addVue.theNameOfBank = null;
				addVue.escrowAccount = null;
				addVue.depositRate = null;
				addVue.orgAmount = null;
				addVue.signDate = null;
				addVue.timeLimit = null;
				addVue.beginExpirationDate = null;
				addVue.endExpirationDate = null;
				addVue.remark = null;
				addVue.eCode = null;
				addVue.bankOfDeposit = null;
				addVue.userUpdate = null;
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

	function resetInfo() {
		listVue.keyword = "";
		document.getElementById("date0611010101").value = "";
		refresh();
	}

	// 详情操作----------点击协定存款协议编号进入详情页面
	function openDetails(tableId) {
		enterNextTab(tableId, '协定存款协议详情', 'tgxy/Tgxy_DepositAgreementDetail.shtml',tableId+"06110101");
		/*$("#tabContainer").data("tabs").addTab({
			id: tableId,
			text: '协定存款协议详情',
			closeable: true,
			url: 'tgxy/Tgxy_DepositAgreementDetail.shtml'
		});*/
	}

	// 新增操作----------点击新增按钮进入新增信息页面
	function add() {
		enterNewTab('', '新增协定存款协议', 'tgxy/Tgxy_DepositAgreementAdd.shtml');
		/*$("#tabContainer").data("tabs").addTab({
			id: 'Tgxy_DepositAgreementAdd',
			text: '协定存款协议新增',
			closeable: true,
			url: 'tgxy/Tgxy_DepositAgreementAdd.shtml'
		});*/
	}

	function handleSelectionChange(val) {
		console.log(val);
		// 获取选中需要修改的数据的tableId
		var length = val.length;
		listVue.selNum = length;
		if(length > 0) {
			for(var i = 0; i < length; i++) {
				listVue.idArr.push(val[i].tableId);
			}
			console.log("idArr:" + listVue.idArr);
			// 修改id获取
			listVue.pkId = val[0].tableId;
			console.log("pkId:" + listVue.pkId);
		}
	}

	function depositAgreementEditHandle() {
		console.log("pkId:" + listVue.pkId);
		enterNextTab(listVue.pkId, '修改协定存款协议', 'tgxy/Tgxy_DepositAgreementEdit.shtml',listVue.tableId+"06110101");
		/*$("#tabContainer").data("tabs").addTab({
			id: listVue.pkId,
			text: '修改协定存款协议',
			closeable: true,
			url: 'tgxy/Tgxy_DepositAgreementEdit.shtml'
		});*/
	}

	// 导出--------数据导出为Excel
	function exportExcel() {
		flag = true;
		// 获取需要导出的table的dom节点
		var wb = XLSX.utils.table_to_book(document.querySelector('#depositAgreementTab'));
		// 写入列表数据并保存
		if(flag){
		    var wbout = XLSX.write(wb, { bookType: 'xlsx', bookSST: true, type: 'array' });
			flag = false;
		}
		console.log(wbout);
		
		try {
			// 导出
			saveAs(new Blob([wbout], { type: 'application/octet-stream' }), '协定存款协议.xlsx');
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
})({
	"listDivId": "#tgxy_DepositAgreementListDiv",
	"updateDivId": "#updateModel",
	"warnModel2":"#warnModel2",
	"addDivId": "#addModal",
	"errorModel":"#errorM",
	"successModel":"#successM",
	"listInterface": "../Tgxy_DepositAgreementList",
	"addInterface": "../Tgxy_DepositAgreementAdd",
	"deleteInterface": "../Tgxy_DepositAgreementBatchDelete",
	"updateInterface": "../Tgxy_DepositAgreementUpdate"
});