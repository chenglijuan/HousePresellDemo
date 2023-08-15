(function(baseInfo) {
	var listVue = new Vue({
		el : baseInfo.listDivId,
		data : {
			interfaceVersion : 19000101,
			pageNumber : 1,
			countPerPage : 10,
			totalPage : 1,
			totalCount : 1,
			keyword : "",
			selectItem : [],
			isAllSelected : false,
			theState : 0, // 正常为0，删除为1
			userStartId : null,
			userStartList : [],
			userRecordId : null,
			userRecordList : [],
			tableId : null,
			idArr : [],
			selNum : 0,
			otherBusiCode : '',
			warnType : [],
			tgxy_WarnList : [],
			warn_num : 0,
			idArr2 : [],
			selNum2 : 0,
			currentStatus : [],
			otherBusiCodeList : [
				{tableId:"220101",theName:"企业预警"},
				{tableId:"220102",theName:"项目预警"},
				
				{tableId:"220103",theName:"楼幢预警"},
				{tableId:"220104",theName:"户信息预警"},
				
				{tableId:"220106",theName:"楼幢备案价格预警"},
				{tableId:"220107",theName:"监管账号预警"},
				
				{tableId:"220105",theName:"合同信息预警"},
				{tableId:"220108",theName:"合同状态预警"},
				
				{tableId:"220109",theName:"预售证预警"}
			],
		},
		methods : {
			handleConduct : handleConduct,
			handleDelete2 : handleDelete2,
			handleWarnSelectionChange : handleWarnSelectionChange,
			getReadWarnForm : getReadWarnForm,
			handleReadWarn : handleReadWarn,
			resetInfo : resetInfo,
			search : search,
			initData : initData,
			indexMethod : indexMethod,
			checkAllClicked : checkAllClicked,
			changePageNumber : function(data) {
				if (listVue.pageNumber != data) {
					listVue.pageNumber = data;
					listVue.loadWarnList();
				}
			},
			changeCountPerPage : function(data) {
				if (listVue.countPerPage != data) {
					listVue.countPerPage = data;
					listVue.loadWarnList();
				}
			},
			loadWarnList : loadWarnList,
			getNotice1Form : getNotice1Form,
			changeReconciliationState : function(data){
				console.log(data);
				listVue.otherBusiCode = data.tableId;
			},
			changeReconciliationStateEmpty : function(){
				listVue.otherBusiCode = null;
			}
		},
		computed : {

		},
		components : {
			'vue-nav' : PageNavigationVue,
			'vue-listselect': VueListSelect
		},
		watch : {
			// pageNumber: refresh,
			selectItem : selectedItemChanged,
		}
	});

	// ------------------------方法定义-开始------------------//

	// 选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged() {

	}
	function search() {
		listVue.pageNumber = 1;
		loadWarnList();
	}
	function resetInfo() {
		document.getElementById("todoListDate").value = "";
		listVue.otherBusiCode = "";
	}
	function getReadWarnForm() {
		return {
			interfaceVersion : 19000101,
			idArr : listVue.idArr2,
		}
	}
	// 未处理预警----------复选框选中事件
	function handleWarnSelectionChange(val) {
		// 获取选中需要修改的数据的tableId
		var length = val.length;
		listVue.idArr2 = [];
		listVue.selNum2 = length;
		if (length > 0) {
			for (var i = 0; i < length; i++) {
				listVue.idArr2.push(val[i].tableId);
			}
		}
	}
	function handleDelete2() {
		new ServerInterface(baseInfo.delete1Interface).execute(listVue
				.getReadWarnForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
				generalSuccessModal();
				loadWarnList();
			}
		});
	}
	function handleConduct() {
		new ServerInterface(baseInfo.conductInterface).execute(listVue
				.getReadWarnForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
				listVue.currentStatus = jsonObj.currentStatus;
				generalSuccessModal();
				loadWarnList();
			}
		});
	}

	function handleReadWarn() {
		new ServerInterface(baseInfo.readWarnInterface).execute(listVue
				.getReadWarnForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
				$(baseInfo.errorModel).modal('show', {
					backdrop : 'static'
				});
			} else {
				$(baseInfo.successModel).modal('show', {
					backdrop : 'static'
				});
				listVue.selectItem = [];
				loadWarnList();
			}
		});
	}
	// 列表操作--------------“全选”按钮被点击
	function checkAllClicked() {
		if (listVue.selectItem.length == listVue.tgxy_WarnList.length) {
			listVue.selectItem = [];
		} else {
			listVue.selectItem = []; // 解决：已经选择一个复选框后，再次点击全选样式问题
			listVue.tgxy_WarnList.forEach(function(item) {
				listVue.selectItem.push(item.tableId);
			});
		}
	}
	function loadWarnList() {
		new ServerInterface(baseInfo.warnloadInterface).execute(listVue
				.getNotice1Form(), function(jsonObj) {
			if (jsonObj.result != "success") {
				$(baseInfo.errorModel).modal('show', {
					backdrop : 'static'
				});
			} else {
				listVue.tgxy_WarnList = jsonObj.sm_CommonMessageDtlList;
				listVue.warn_num = listVue.tgxy_WarnList.length;
				listVue.pageNumber = jsonObj.pageNumber;
				listVue.countPerPage = jsonObj.countPerPage;
				listVue.totalPage = jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
			}
		});
	}
	function getNotice1Form() {
		var date = "";
		if (document.getElementById('todoListDate') == null) {
			date = "";
		} else {
			date = document.getElementById('todoListDate').value;
		}
		return {
			interfaceVersion : this.interfaceVersion,
			signDate : date,
			otherBusiCode : listVue.otherBusiCode,
			pageNumber : this.pageNumber,
			countPerPage : this.countPerPage,
			totalPage : this.totalPage,
			keyword : this.keyword,
		}
	}

	function indexMethod(index) {
		return generalIndexMethod(index, listVue)
	}
	function initData() {
	}

	// ------------------------方法定义-结束------------------//

	// ------------------------数据初始化-开始----------------//
	listVue.initData();
	// 加载预警业务类型
	listVue.loadWarnList();
	// ------------------------数据初始化-结束----------------//

	// 添加日期控件
	laydate.render({
		elem : '#todoListDate',
	});
})({
	"listDivId" : "#Emmp_WarningListDiv",
	"warnModel" : "#warnModel",
	"errorModel" : "#edModel",
	"successModel" : "#sdModel",
	"readWarnInterface" : "../Tg_ComMesgWaringReader",
	"warnloadInterface" : "../Tg_ComMesgWaringList",
	"delete1Interface" : "../Tg_ComMesgWaringBatchDelete",
	"conductInterface" : "../Tg_ComMesgWaringUpdate",
});