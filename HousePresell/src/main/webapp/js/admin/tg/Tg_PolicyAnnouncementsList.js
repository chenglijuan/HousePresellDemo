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
			signDate: null,
			selectItem: [],
			checkSelectItem : [],
			isAllSelected: false,
			tableId: null,
			idArr:[],
			policyAnnouncementsList: [{
				policyState: 0
			},{
				policyState: 1
			},{
				policyState: 2
			}],
			policyType: "",
			sm_BaseParameterList: [],
			policyTypeCode: "",
			busType:"21020401",
			busiCode:"21020401",
			xflag: false,
			cflag: false,
			sflag: false,
		},
		methods: {
			checkCheckBox : checkCheckBox,//列表选中
			refresh: refresh,
			initData: initData,
			indexMethod: indexMethod,
			search: search,
			checkAllClicked: checkAllClicked,
			// 新增政策公告
			addHandle: addHandle,
			// 修改政策公告
			coopMemoEditHandle: coopMemoEditHandle,
			// 查看政策公告详情
			openDetails: openDetails,
			// 复选框选中状态改变
			handleSelectionChange: handleSelectionChange,
			resetInfo: resetInfo,
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
			getPolicyType: function(data) {
				listVue.policyType = data.theName;
			},
			emptyPolicyType: function(data) {
				listVue.policyType = null;
			},
			loadType: function() {
				var model = {
					interfaceVersion:this.interfaceVersion,
					theState : 0,
					parametertype : 67
				}
				new ServerInterface(baseInfo.typeInterface).execute(model, function(jsonObj) {
					if(jsonObj.result != "success") {
						generalErrorModal(jsonObj);
					} else {
						listVue.sm_BaseParameterList = jsonObj.sm_BaseParameterList;
					}
				});
			},
			deleteHandle: function() {
				var model = {
					interfaceVersion:this.interfaceVersion,
					idArr:listVue.selectItem
				}
				new ServerInterface(baseInfo.deleteInterface).execute(model, function(jsonObj) {
					if(jsonObj.result != "success") {
						generalErrorModal(jsonObj);
					} else {
						refresh();
					}
				});
			},
			backHandle: function() {
				var model = {
					interfaceVersion:this.interfaceVersion,
					tableId:listVue.selectItem[0],
					policyState: 2
				}
				new ServerInterface(baseInfo.backInterface).execute(model, function(jsonObj) {
					if(jsonObj.result != "success") {
						generalErrorModal(jsonObj);
					} else {
						refresh();
					}
				});
			}
		},
		components: {
			'vue-nav': PageNavigationVue,
			'vue-listselect': VueListSelect,
		},
		watch: {
			selectItem: selectedItemChanged,
		}
	});

	//------------------------方法定义-开始------------------//
	
	function resetInfo()
	{
		listVue.keyword = "";
		listVue.policyTypeCode = "";
		listVue.policyType = "";
		refresh();
	}
	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged() {
		listVue.isAllSelected = (listVue.policyAnnouncementsList.length > 0)
		&&	(listVue.selectItem.length == listVue.policyAnnouncementsList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked() {
		if(listVue.selectItem.length == listVue.policyAnnouncementsList.length) {
			listVue.selectItem = [];
		} else {
			listVue.selectItem = []; //解决：已经选择一个复选框后，再次点击全选样式问题
			listVue.policyAnnouncementsList.forEach(function(item) {
				listVue.selectItem.push(item.tableId);
			});
		}
	}
	//列表操作--------------刷新
	function refresh() {
		var model = {
			interfaceVersion:listVue.interfaceVersion,
			pageNumber:listVue.pageNumber,
			countPerPage:listVue.countPerPage,
			totalPage:listVue.totalPage,
			totalCount:listVue.totalCount,
			keyword:listVue.keyword,
			policyType:listVue.policyType,
			policyTypeCode:listVue.policyTypeCode
		}
		new ServerInterface(baseInfo.listInterface).execute(model, function(jsonObj) {
			if(jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
				
				listVue.policyAnnouncementsList = jsonObj.sm_PolicyRecordList;
//				console.log("listVue.policyAnnouncementsList="+listVue.policyAnnouncementsList);
				listVue.policyAnnouncementsList.forEach(function(item){
//					console.log("item="+item);
                	if(item.lastUpdateTimeStamp != undefined)
                	{
//                		console.log("item.lastUpdateTimeStamp ="+item.lastUpdateTimeStamp );
                		item.lastUpdateTimeStamp = timeStamp2DayDate(item.lastUpdateTimeStamp);
                	}
                });
				
				listVue.pageNumber = jsonObj.pageNumber;
				listVue.countPerPage = jsonObj.countPerPage;
				listVue.totalPage = jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword = jsonObj.keyword;
				listVue.selectedItem = [];
				//动态跳转到锚点处，id="top"
				document.getElementById('policyAnnouncementsListDiv').scrollIntoView();
			}
		});
	}

	//列表操作------------搜索
	function search() {
		listVue.pageNumber = 1;
		refresh();
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

	// 点击新增按钮，打开政策公告新增页面
	function addHandle() {
		enterNewTab('', '新增政策公告', 'tg/Tg_PolicyAnnouncementsAdd.shtml');
	}

	// 点击修改按钮，打开政策公告编辑页面
	function coopMemoEditHandle() {
		enterNewTab(listVue.selectItem[0], '政策公告编辑', 'tg/Tg_PolicyAnnouncementsEdit.shtml',listVue.selectItem[0]+listVue.busType);
	}

	// 查看详情
	function openDetails(tableId) {
		enterNewTab(tableId, '政策公告详情', 'tg/Tg_PolicyAnnouncementsDetail.shtml',tableId+listVue.busType);
	}

	// 复选框选中事件
	function handleSelectionChange(list) {
        generalListItemSelectHandle(listVue,list)
	}
	
	function checkCheckBox(obj){
		listVue.checkSelectItem = obj;
		if(listVue.checkSelectItem.length != 0) {
			//修改按钮(处于未发布状态)
			if(listVue.checkSelectItem.length == 1 && obj[0].policyState != "1" ) {
				listVue.selectItem = [];
				listVue.selectItem.push(obj[0].tableId);
                listVue.xflag = true;
			}else {
				listVue.xflag = false;
			}
			
			//删除按钮(处于未发布状态)
			if(listVue.checkSelectItem.length >= 1 && obj[0].policyState != "1" ) {
				listVue.selectItem = [];
				obj.forEach(function(item){
					console.log(item);
					listVue.selectItem.push(item.tableId);
				});
				console.log(listVue.selectItem);
                listVue.sflag = true;
			}else {
				listVue.sflag = false;
			}
			
			//撤销按钮(处于发布状态)
			if(listVue.checkSelectItem.length == 1 && obj[0].policyState == "1" ) {
				listVue.selectItem = [];
				listVue.selectItem.push(obj[0].tableId);
                listVue.cflag = true;
			}else {
				listVue.cflag = false;
			}
			
		}else {
				listVue.selectItem = [];
			    listVue.xflag = false;
			    listVue.cflag = false;
			    listVue.sflag = false;
		}
	}
	//------------------------方法定义-结束------------------//

	//------------------------数据初始化-开始----------------//
	listVue.loadType();
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//

	// 添加日期控件
	laydate.render({
		elem: '#date0611010201',
	});
})({
	"listDivId": "#policyAnnouncementsListDiv",
	"typeInterface":"../Sm_BaseParameterList",
	"listInterface": "../Sm_PolicyRecordList",
	"deleteInterface": "../Sm_PolicyRecordBatchDelete",
	"backInterface":"../Sm_PolicyRecordUpdate"
});