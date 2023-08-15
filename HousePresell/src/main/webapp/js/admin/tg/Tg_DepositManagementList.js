(function(baseInfo) {
	var listVue = new Vue(
			{
				el : baseInfo.listDivId,
				data : {
					interfaceVersion : 19000101,
					pageNumber : 1,
					countPerPage : 20,
					totalPage : 1,
					totalCount : 1,
					recordState : 1,
					selectItem : [],
					theState : 0,// 正常为0，删除为1
					tg_DepositManagementList : [],
					orderBy : null,

					keyword : null,
					emmp_BankBranchList : [],
					bankBranchId : "",
					depositStateList : [ {
						tableId : "01",
						theName : "存单存入"
					}, {
						tableId : "02",
						theName : "存单提取"
					} ],
					depositState : "",
					depositPropertyList : [ {
						tableId : "01",
						theName : "大额存单"
					}, {
						tableId : "02",
						theName : "结构性存款"
					}, {
						tableId : "03",
						theName : "保本理财"
					} ],
					depositProperty : "",
					listDateStr : null,
					listDateStrEnd : null,

					remindText : "提示内容",

					// 按钮控制
					canUpdate : false,
					canDelete : false,
					canTakeOut : false,
					canTs : false,//推送按钮
				},
				methods : {
					refresh : refresh,
					initData : initData,
					getSearchForm : getSearchForm,
					listItemSelectHandle : listItemSelectHandle,
					search : search,
					searchWithoutKey : searchWithoutKey,
					depositManagementAddHandle : depositManagementAddHandle,
					depositManagementEditHandle : depositManagementEditHandle,
					depositManagementTakeOutHandle : depositManagementTakeOutHandle,
					depositManagementExportExcelHandle : depositManagementExportExcelHandle,
					depositManagementDeleteHandle : depositManagementDeleteHandle,
					depositManagementDetailHandle : depositManagementDetailHandle,
					sortChange : sortChange,
					depositManagementTsHandle : depositManagementTsHandle,

					bankBranchChange : function(data) {
						if (listVue.bankBranchId != data.tableId) {
							listVue.bankBranchId = data.tableId;
						}
					},
					bankBranchEmpty : function() {
						if (listVue.bankBranchId != null) {
							listVue.bankBranchId = null;
						}
					},
					depositStateChange : function(data) {
						if (listVue.depositState != data.tableId) {
							listVue.depositState = data.tableId;
						}
					},
					depositStateEmpty : function() {
						if (listVue.depositState != null) {
							listVue.depositState = null;
						}
					},
					depositPropertyChange : function(data) {
						if (listVue.depositProperty != data.tableId) {
							listVue.depositProperty = data.tableId;
						}
					},
					depositPropertyEmpty : function() {
						if (listVue.depositProperty != null) {
							listVue.depositProperty = null;
						}
					},
					changePageNumber : function(data) {
						console.log(data);
						if (listVue.pageNumber != data) {
							listVue.pageNumber = data;
							listVue.refresh();
						}
					},
					changeCountPerPage : function(data) {
						console.log(data);
						if (listVue.countPerPage != data) {
							listVue.countPerPage = data;
							listVue.refresh();
						}
					},
					indexMethod : function(index) {
						if (listVue.pageNumber > 1) {
							return (listVue.pageNumber - 1)
									* listVue.countPerPage + (index - 0 + 1);
						}
						if (listVue.pageNumber <= 1) {
							return index - 0 + 1;
						}
					}
				},
				filters : {
					setThousands : function(data) {
						return addThousands(data);
					},
					setPercent : function(data) {
						return toPercent(data);
					}
				},
				computed : {

				},
				components : {
					'vue-nav' : PageNavigationVue,
					'vue-listselect' : VueListSelect
				},
				watch : {}
			});

	// ------------------------方法定义-开始------------------//
	
	//推送方法
	function depositManagementTsHandle()
	{
		var form=
		{
	        interfaceVersion : listVue.interfaceVersion,
	        idArr : listVue.selectItem,
		}
		
		new ServerInterface(baseInfo.tsInterface).execute(form,function(jsonObj)
			{
				if(jsonObj.result != "success")
	            {
	                generalErrorModal(jsonObj);
	            }
	            else
	            {
	                generalSuccessModal();
	                refresh();
	            }
			});
		
	}

	function listItemSelectHandle(val) {
		listVue.selectItem = [];
		for (var index = 0; index < val.length; index++) {
			var element = val[index].tableId;
			listVue.selectItem.push(element)
		}
		// console.log(listVue.selectItem);

		listVue.canUpdate = false;
		listVue.canDelete = false;
		listVue.canTakeOut = false;
		listVue.canTs = false;

		if (val.length == 1) {
			var model = val[0];
			
			if(model.approvalState == '已完结')
			{
				listVue.canTs = true;
			}
			
			if (model.busiState == '未备案') {
				if (model.approvalState == '待提交') {
					listVue.canUpdate = true;
					listVue.canDelete = true;
				}
			} else {
				if (model.approvalState != '审核中') {
					listVue.canTakeOut = true;
				}
			}
		} else if (val.length > 1) {
			listVue.canDelete = true;
			listVue.canTs = true;
			for (var i = 0; i < val.length; i += 1) {
				var model = val[i];
				if (model.busiState == '已备案' || model.approvalState != '待提交') {
					listVue.canDelete = false;
					break;
				}
				
				if(model.approvalState != '已完结')
				{
					listVue.canTs = false;
					break;
				}
			}
		}
	}

	function sortChange(column) {
		listVue.orderBy = generalOrderByColumn(column);
		refresh();
	}

	// 列表操作--------------获取"搜索列表"表单参数
	function getSearchForm() {
		return {
			interfaceVersion : this.interfaceVersion,
			pageNumber : this.pageNumber,
			countPerPage : this.countPerPage,
			totalPage : this.totalPage,
			keyword : this.keyword,
			depositState : this.depositState,
			depositProperty : this.depositProperty,
			bankOfDepositId : this.bankBranchId,
			stopDateStr : this.listDateStr,
			listDateStrEnd : this.listDateStrEnd,
			theState : this.theState,
			orderBy : this.orderBy
		}
	}

	// 列表操作--------------获取“删除资源”表单参数
	function depositManagementEditHandle() {
		if (listVue.selectItem.length == 1) {
			enterDetail(listVue.selectItem, '存单存入修改',
					'tg/Tg_DepositManagementEdit.shtml');
		} else {
			listVue.remindText = "请选择一个且只选一个要修改的存单存入";
			$(baseInfo.remindModal).modal('show', {
				backdrop : 'static'
			});
		}
	}

	function depositManagementTakeOutHandle() {
		if (listVue.selectItem.length == 1) {
			var tableId = listVue.selectItem[0];
			for (var i = 0; i < listVue.tg_DepositManagementList.length; i++) {
				if (listVue.tg_DepositManagementList[i].tableId == tableId) {
					if (listVue.tg_DepositManagementList[i].depositState == '02') {
						generalErrorModal('', '该存单已被提取');
					} else {
						enterNewTab(tableId, '存单存入提取',
								'tg/Tg_DepositManagementTakeOutAdd.shtml');
					}
				}
			}
		} else {
			listVue.remindText = "请选择一个且只选一个要提取的存单存入";
			$(baseInfo.remindModal).modal('show', {
				backdrop : 'static'
			});
		}
	}

	function depositManagementDeleteHandle() {
		generalDeleteModal(listVue, "Tg_DepositManagement");
	}

	function getBankBranchList() {
		var model = {
			interfaceVersion : listVue.interfaceVersion,
			theState : 0,
			orderBy : 'theName ASC',
		};

		new ServerInterface(baseInfo.bankBranchListInterface)
				.execute(
						model,
						function(jsonObj) {
							console.log(jsonObj);
							if (jsonObj.result != "success") {

							} else {
								listVue.emmp_BankBranchList = jsonObj.emmp_BankBranchList;
							}
						});
	}

	function depositManagementExportExcelHandle() {
		var model;
		if (this.selectedItem.length > 0) {
			model = {
				interfaceVersion : this.interfaceVersion,
				theState : this.theState,
				idArr : this.selectItem
			};
		} else {
			model = {
				interfaceVersion : this.interfaceVersion,
				keyword : this.keyword,
				depositState : this.depositState,
				depositProperty : this.depositProperty,
				bankOfDepositId : this.bankBranchId,
				stopDateStr : this.listDateStr,
				listDateStrEnd : this.listDateStrEnd,
				theState : this.theState,
				orderBy : this.orderBy
			};
		}

		new ServerInterface(baseInfo.exportExcelInterface).execute(model,
				function(jsonObj) {
					if (jsonObj.result != "success") {
						listVue.errorMessage = jsonObj.info;
						$(baseInfo.errorM).modal('show', {
							backdrop : 'static'
						});
					} else {
						window.location.href = jsonObj.fileDownloadPath;
						console.log(jsonObj.fileDownloadPath);

						listVue.selectItem = [];
						refresh();
					}
				});
	}

	// 列表操作--------------刷新
	function refresh() {

		new ServerInterface(baseInfo.depositManagementListInterface)
				.execute(
						listVue.getSearchForm(),
						function(jsonObj) {
							if (jsonObj.result != "success") {
								noty({
									"text" : jsonObj.info,
									"type" : "error",
									"timeout" : 2000
								});
							} else {
								console.log(jsonObj);

								listVue.tg_DepositManagementList = jsonObj.tg_DepositManagementList;
								listVue.pageNumber = jsonObj.pageNumber;
								listVue.countPerPage = jsonObj.countPerPage;
								listVue.totalPage = jsonObj.totalPage;
								listVue.totalCount = jsonObj.totalCount;
								listVue.selectedItem = [];
								// 动态跳转到锚点处，id="top"
								document.getElementById(
										'tg_DepositManagementListDiv')
										.scrollIntoView();
							}
						});
	}

	// 列表操作------------搜索
	function search() {
		listVue.pageNumber = 1;
		refresh();
	}

	function searchWithoutKey() {

		listVue.bankBranchId = "";
		listVue.depositState = "";
		listVue.depositProperty = "";
		listVue.keyword = null;
		listVue.listDateStr = null;
		listVue.listDateStrEnd = null;
		$('#date21010401').val("");

		listVue.pageNumber = 1;
		refresh();
	}

	// 跳转方法
	function depositManagementAddHandle() {
		enterNewTab('', '添加存单存入', 'tg/Tg_DepositManagementAdd.shtml');
	}

	function depositManagementDetailHandle(tableId) {
		enterNewTab(tableId, '存单存入详情', 'tg/Tg_DepositManagementDetail.shtml');
	}

	function initData() {
		laydate.render({
			elem : '#date21010401',
			range : '~',
			done : function(value, date, endDate) {
				if (value == null || value.length == 0) {
					listVue.listDateStr = null;
					listVue.listDateStrEnd = null;
				} else {
					var arr = value.split("~");
					listVue.listDateStr = arr[0];
					listVue.listDateStrEnd = arr[1];
				}
			}
		});

		refresh();
		getBankBranchList();
	}
	// ------------------------方法定义-结束------------------//

	// ------------------------数据初始化-开始----------------//
	listVue.initData();

	// ------------------------数据初始化-结束----------------//
})({
	"listDivId" : "#tg_DepositManagementListDiv",
	"updateDivId" : "#updateModel",
	"addDivId" : "#addModal",
	"depositManagementListInterface" : "../Tg_DepositManagementList",
	"deleteInterface" : "../Tg_DepositManagementBatchDelete",
	"exportExcelInterface" : "../Tg_DepositManagementExportExcel",
	"bankBranchListInterface" : "../Emmp_BankBranchList",
	"tsInterface" : "../Tg_DepositManagementTs",//推送接口
	// 删除
	"remindModal" : "#remindModal"
});
