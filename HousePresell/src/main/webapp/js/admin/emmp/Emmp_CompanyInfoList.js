(function (baseInfo) {
	var listVue = new Vue({
		el: baseInfo.listDivId,
		data: {
			interfaceVersion: 19000101,
			pageNumber: 1,
			countPerPage: 20,
			totalPage: 1,
			totalCount: 1,
			keyword: null,
            theType: "1",
			busiState: "",
            approvalState: "",
			selectItem: [],
			theState: 0,//正常为0，删除为1
			emmp_CompanyInfoList: [],
            orderBy : "createTimeStamp desc",

			//提示框
			successMessage: "",
			errorMessage: "",
            remindMessage: "",
			selectMessage: "",
            selectMethod: "",
            unImportList:[{
            	companyname:"常州万达地产未导入",
            	companycode:"91325487895421541",
            	registdate:"2017-05-12",
            	companyadd:"常州天宁区",
            	legalperson:"王思聪",
            	grade:"一级",
            	checked:false,
            }],
            ImportList:[],
            COMPANYNAME: "",
            SOCIALCREDITCODE: "",
            noImportNum: 0,
            importNum: 0,
            isSave: false,
			//修改按钮
            upDisabled:true,
			//删除按钮
			delDisabled:true,
			unifiedSocialCreditCode: "",
			unifiedSocialCreditCode1: "",
            diabaleShowLog: true,
            isSearch1: true,//导入时搜索按钮
            busiStateList : [
            	{tableId:"未备案",theName:"未备案"},
            	{tableId:"已备案",theName:"已备案"},
            ],
            approvalStateList : [
            	{tableId:"待提交",theName:"待提交"},
            	{tableId:"审核中",theName:"审核中"},
            	{tableId:"已完结",theName:"已完结"},
            ]
		},
		methods: {
			refresh: refresh,
			initData: initData,
			getSearchForm: getSearchForm,
			getDeleteForm: getDeleteForm,
			search: search,
			searchWithoutKey: searchWithoutKey,
			companyInfoAddHandle: companyInfoAddHandle,
			companyInfoEditHandle: companyInfoEditHandle,
			companyInfoDeleteHandle: companyInfoDeleteHandle,
            companyInfoDel: companyInfoDel,
            companyInfoDelOne: companyInfoDelOne,
			companyInfoDetailHandle: companyInfoDetailHandle,
			companyInfoExportExcelHandle: companyInfoExportExcelHandle,
            sortChange:sortChange,
			
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
			toggleSelection: toggleSelection,
			handleSelectionChange: handleSelectionChange,
			handleSelectionChange1: handleSelectionChange1,
			indexMethod: function (index) {
				if (listVue.pageNumber > 1) {
					return (listVue.pageNumber - 1) * listVue.countPerPage + (index - 0 + 1);
				}
				if (listVue.pageNumber <= 1) {
					return index - 0 + 1;
				}
			},
			getImportList: getImportList,
			getImportForm: getImportForm,
			search1: search1,
			openDialog: openDialog,
			deleteHandle: deleteHandle,
			save: save,
			getSaveForm: getSaveForm,
			allEmptyHandle: allEmptyHandle,
			allSelect: allSelect,
			checkSelectable: function(row) {
				if(listVue.ImportList.length == 0) {
					listVue.unifiedSocialCreditCode = "";
				}
				if(row.unifiedSocialCreditCode == listVue.unifiedSocialCreditCode) {
					return 0;
				}else {
					return 1;
				}
		    },
            showLog: showLog,
            busiStateChange : function(data){
            	listVue.busiState = data.tableId;
            },
            emptyBusiState : function(){
            	listVue.busiState = null;
            },
            approvalStateChange : function(data){
            	listVue.approvalState = data.tableId;
            },
            emptyApprovalState : function(){
            	listVue.approvalState = null;
            }
            
		},
		computed: {

		},
		components: {
			'vue-nav': PageNavigationVue,
			'vue-listselect': VueListSelect,
		},
		watch: {
//			pageNumber: refresh,
//			countPerPage: refresh,
			importNum: listenSave
		}
	});

	function toggleSelection(rows) {
		// if (rows) 
		// {
		// 	rows.forEach(row => {
		// 		this.$refs.multipleTable.toggleRowSelection(row);
		// 	});
		// } else {
		// 	this.$refs.multipleTable.clearSelection();
		// }		
	}
	
	function listenSave()
	{
		if(listVue.importNum == 0) {
			listVue.isSave = false;
		}else {
			listVue.isSave = true;
		}
	}

    function sortChange(column) {
        listVue.orderBy = generalOrderByColumn(column);
        refresh();
    }

	function handleSelectionChange(val) {
		listVue.selectItem = [];
		for (var index = 0; index < val.length; index++) {
			var element = val[index].tableId;
			listVue.selectItem.push(element)
		}
        if(listVue.diabaleShowLog!=undefined){
            if(listVue.selectItem.length==1){
                listVue.diabaleShowLog=false
            }else{
                listVue.diabaleShowLog=true
            }
        }
		//修改按钮
		if(val.length == 1)
		{
			if(val[0].approvalState == "审核中")
			{
				listVue.upDisabled = true;
			}
			else
			{
                listVue.upDisabled = false;
			}
		}
		else
		{
            listVue.upDisabled = true;
		}

		//删除按钮
		if(val.length > 0)
		{
			var count = 0;
            for (var i = 0; i < val.length; i++)
            {
				if(val[i].busiState == "已备案" || val[i].approvalState == "审核中")
				{
					count++;
				}
				if(count > 0)
				{
                    listVue.delDisabled = true;
				}
				else
				{
                    listVue.delDisabled = false;
				}
            }
		}
		else
		{
            listVue.delDisabled = true;
		}
	}
	
	function handleSelectionChange1(val)
	{
		if(val.length == 1) {
			var model = {
					theName: val[0].theName,
					registeredDate: val[0].registeredDate,
					address: val[0].address,
					legalPerson: val[0].legalPerson,
					qualificationGrade: val[0].qualificationGrade,
					externalId: val[0].externalId,
					unifiedSocialCreditCode: val[0].unifiedSocialCreditCode,
			}
			listVue.ImportList.push(model);
			listVue.importNum = listVue.ImportList.length;
			
			var _this = this;
			listVue.unImportList.forEach(function(item, index) {
　　　　　　          // id 是每一行的数据id
               if(val[0].theName == item.theName){
               	listVue.unImportList.splice(index, 1);
               	val[0].theName = "";
               }
	         });
	        /*val.forEach(function(val, index) {
	            _this.unImportList.forEach(function(v, i) {
	            // id 是每一行的数据id
	                if(val.id == v.id){
	                	_this.unImportList.splice(i, 1);
	                }
	            })
	         })*/
	 		listVue.noImportNum = _this.unImportList.length;
		}
	}
	//------------------------方法定义-开始------------------//

	//列表操作--------------获取"搜索列表"表单参数
	function getSearchForm() {
		return {
			interfaceVersion: this.interfaceVersion,
			pageNumber: this.pageNumber,
			countPerPage: this.countPerPage,
			totalPage: this.totalPage,
			keyword: this.keyword,
            busiState: this.busiState,
            approvalState: this.approvalState,
			theState: this.theState,
            theType: this.theType,
			orderBy: this.orderBy
		}
	}
	//列表操作--------------获取“删除资源”表单参数
	function getDeleteForm() {
		return {
			interfaceVersion: this.interfaceVersion,
			idArr: listVue.selectItem
		}
	}

	function companyInfoEditHandle()
	{
		if(listVue.selectItem.length == 1) 
		{
			enterDetail(listVue.selectItem, '机构详情修改', 'emmp/Emmp_CompanyInfoEdit.shtml');
		} 
		else 
		{
			listVue.remindMessage = "请选择一个且只选一个要修改的机构";
            $(baseInfo.remindM).modal('show', {
                backdrop :'static'
            });
		}
	}

	function companyInfoDeleteHandle() {
		if(listVue.selectItem.length == 0) 
		{
            listVue.remindMessage = "请选择要删除的机构";
            $(baseInfo.remindM).modal('show', {
                backdrop :'static'
            });
		} 
		else if (listVue.selectItem.length > 1) 
		{
            listVue.selectMessage = "确认批量删除吗？";
            listVue.selectMethod = "companyInfoDel";
            $(baseInfo.selectM).modal('show', {
                backdrop :'static'
            });
		} 
		else 
		{
            listVue.selectMessage = "确认删除吗？";
            listVue.selectMethod = "companyInfoDelOne";
            $(baseInfo.selectM).modal('show', {
                backdrop :'static'
            });
		}
	}

	function companyInfoDel() {
        new ServerInterface(baseInfo.batchDeleteInterface).execute(listVue.getDeleteForm(), function (jsonObj) {
            if (jsonObj.result != "success") {
                listVue.errorMessage = jsonObj.info;
                $(baseInfo.errorM).modal('show', {
                    backdrop :'static'
                });
            }
            else {
                listVue.successMessage = jsonObj.info;
                $(baseInfo.successM).modal('show', {
                    backdrop :'static'
                });
                listVue.selectItem = [];
                refresh();
            }
        });
	}
	
	function companyInfoDelOne() {
		var model = {
			interfaceVersion: 19000101,
			tableId: listVue.selectItem[0],
		};

        new ServerInterface(baseInfo.deleteInterface).execute(model, function (jsonObj) {
            if (jsonObj.result != "success") {
                listVue.errorMessage = jsonObj.info;
                $(baseInfo.errorM).modal('show', {
                    backdrop :'static'
                });
            }
            else {
                listVue.successMessage = jsonObj.info;
                $(baseInfo.successM).modal('show', {
                    backdrop :'static'
                });
                listVue.selectItem = [];
                refresh();
            }
        });
	}

	function companyInfoExportExcelHandle()
	{
		new ServerInterface(baseInfo.exportExcelInterface).execute(listVue.getDeleteForm(), function (jsonObj) {
			if (jsonObj.result != "success")
			{
                listVue.errorMessage = jsonObj.info;
                $(baseInfo.errorM).modal('show', {
                    backdrop :'static'
                });
			}
			else
			{
				window.location.href=jsonObj.fileDownloadPath;
				console.log(jsonObj.fileDownloadPath);

				listVue.selectItem = [];
				refresh();
			}
		});
	}
	
	//列表操作--------------刷新
	function refresh() 
	{
		new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function (jsonObj) {
			if (jsonObj.result != "success")
			{
                listVue.errorMessage = jsonObj.info;
                $(baseInfo.errorM).modal('show', {
                    backdrop :'static'
                });
			}
			else {
				var str1 = {
					val1:1,
					val2:2,
					val3:3
				}
			    var str2 = {
					val3:6,
					val4:4,
					val5:5
				};
				$.extend(str1,str2);
				//listVue.emmp_CompanyInfoList = jsonObj.emmp_CompanyInfoList;
				//console.log(listVue.$data);
				//console.log(jsonObj);
		        Object.assign(listVue.$data,jsonObj);
				//console.log(listVue.$data);
				listVue.pageNumber = jsonObj.pageNumber;
				listVue.countPerPage = jsonObj.countPerPage;
				listVue.totalPage = jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword = jsonObj.keyword;
				listVue.selectItem = [];
			}
		});
	}

	//列表操作------------搜索
	function search() {
		console.log("search");
		listVue.pageNumber = 1;
		refresh();
	}

	function searchWithoutKey () { 
		listVue.keyword = "";
		console.log("searchWithoutKey");
		listVue.pageNumber = 1;
        listVue.busiState = "";
        listVue.approvalState = "";
		refresh();
	}
	
// 	function SortChange(column, prop, order) {
// //     	console.log(column + '-' + column.prop + '-' + column.order)
//
// 		refresh();
//    }
	
	//跳转方法
	function companyInfoAddHandle() {
        enterNewTab('', '新增开发企业', 'emmp/Emmp_CompanyInfoAdd.shtml');
	}
 
	function companyInfoDetailHandle(tableId) {
		enterNewTab(tableId, '开发企业详情', 'emmp/Emmp_CompanyInfoDetail.shtml');

	}
	//根据按钮权限，动态控制，该页面的所有“可控按钮”的显示与否
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}
	function initData() {
		//获取登录者
		/*getLoginSm_User();*/
		initButtonList();
		refresh();
	}
	
	function getImportList() 
	{
		listVue.isSearch1=false;
		new ServerInterface(baseInfo.importInterface).execute(listVue.getImportForm(), function (jsonObj) {
			listVue.isSearch1=true;
			if (jsonObj.result != "success")
			{
                listVue.errorMessage = jsonObj.info;
                /*$(baseInfo.errorM).modal('show', {
                    backdrop :'static'
                });*/
			}
			else {
				
				listVue.unImportList = jsonObj.companyList;
				/*if(listVue.ImportList.length != 0) {
					listVue.ImportList.forEach(function(item, index) {
						listVue.unImportList.forEach(function(val, ind) {
							if(item.unifiedSocialCreditCode == val.unifiedSocialCreditCode) {
								listVue.unifiedSocialCreditCode = val.unifiedSocialCreditCode;
							}
						})
					})
				}*/
				listVue.noImportNum = listVue.unImportList.length;
			}
		});
	}
	
	function search1()
	{
		if(listVue.ImportList.length != 0) {
			listVue.ImportList = [];
		}
		listVue.importNum = 0;
		// 加载未导入信息列表
		listVue.getImportList();
	}
	
	function openDialog()
	{
		$(baseInfo.importM).modal('show', {
            backdrop :'static'
        });
		setTimeout(function(){
			var height = $("#companyBox").outerHeight();
			$("#loadingcomBox").outerHeight(height + "px");
		},2000);
		listVue.localProject = "";
		listVue.saleProject = "";
		listVue.unImportList = [];
		listVue.ImportList = [];
		listVue.noImportNum = 0;
		listVue.importNum = 0;
	}
	
	function deleteHandle(index,val)
	{
		var model = {
				theName: val.theName,
				registeredDate: val.registeredDate,
				address: val.address,
				legalPerson: val.legalPerson,
				qualificationGrade: val.qualificationGrade,
				externalId: val.externalId,
				unifiedSocialCreditCode: val.unifiedSocialCreditCode,
		}
		listVue.ImportList.splice(index,1);
		/*if(listVue.unImportList.length != 0) {
			listVue.unImportList.forEach(function(item, index) {
				if(item.unifiedSocialCreditCode == val.unifiedSocialCreditCode) {
					listVue.unImportList.splice(index, 1);
					listVue.unifiedSocialCreditCode = "";
				}
			})
		}*/
		listVue.unImportList.push(model);
		listVue.noImportNum = listVue.unImportList.length;
		listVue.importNum = listVue.ImportList.length;
	}
	
	function save()
	{
		$("#companyBox").hide();
		$("#loadingcomBox").show();
		listVue.isSave = false;
		new ServerInterface(baseInfo.importInterface).execute(listVue.getSaveForm(), function (jsonObj) {
			$("#loadingcomBox").hide();
			$("#companyBox").show();
			listVue.isSave = true;
			if (jsonObj.result != "success")
			{
                listVue.errorMessage = jsonObj.info;
			}
			else {
				$(baseInfo.importM).modal('hide');
				refresh();
			}
		});
	}
	
	function allEmptyHandle()
	{
		var length = listVue.ImportList.length;
		if(length != 0) {
			for(var i = 0;i < length; i++) {
				var model = {
					theName: listVue.ImportList[i].theName,
					registeredDate: listVue.ImportList[i].registeredDate,
					address: listVue.ImportList[i].address,
					legalPerson: listVue.ImportList[i].legalPerson,
					qualificationGrade: listVue.ImportList[i].qualificationGrade,
					externalId: listVue.ImportList[i].externalId,
					unifiedSocialCreditCode: listVue.ImportList[i].unifiedSocialCreditCode,
				}
				listVue.unImportList.push(model);
			}
			listVue.ImportList = [];
		}
		
		listVue.noImportNum = listVue.unImportList.length;
		listVue.importNum = 0;
	}
	
	function allSelect()
	{
		var length = listVue.unImportList.length;
		if(length != 0) {
			for(var i = 0;i < length; i++) {
				var model = {
						theName: listVue.unImportList[i].theName,
						registeredDate: listVue.unImportList[i].registeredDate,
						address: listVue.unImportList[i].address,
						legalPerson: listVue.unImportList[i].legalPerson,
						qualificationGrade: listVue.unImportList[i].qualificationGrade,
						externalId: listVue.unImportList[i].externalId,
						unifiedSocialCreditCode: listVue.unImportList[i].unifiedSocialCreditCode,
				}
				listVue.ImportList.push(model);
			}
			listVue.unImportList = [];
		}
		
		listVue.noImportNum = 0;
		listVue.importNum = listVue.ImportList.length;
	}
	
	function getSaveForm()
	{
		var list = listVue.ImportList;
		list = JSON.stringify(list);
		return {
			interfaceVersion: this.interfaceVersion,
			type: "save",
			saveCompanyList: list,
		}
	}
	
	function getImportForm()
	{
		return {
			interfaceVersion: this.interfaceVersion,
			type: "list",
			COMPANYNAME: listVue.COMPANYNAME,
			SOCIALCREDITCODE: listVue.SOCIALCREDITCODE,
		}
	}

    function showLog() {
        enterLogTabWithTemplate("Emmp_CompanyInfo", listVue)
    }
	
	//------------------------方法定义-结束------------------//

	//------------------------数据初始化-开始----------------//
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId": "#emmp_CompanyInfoListDiv",
	"listInterface": "../Emmp_CompanyInfoList",
	"deleteInterface": "../Emmp_CompanyInfoDelete",
	"batchDeleteInterface": "../Emmp_CompanyInfoBatchDelete",
	"exportExcelInterface": "../Emmp_CompanyInfoExportExcel",
	"importInterface": "../Tb_b_company",

	//其他
    "successM": "#companyInfoList_successM",
    "errorM": "#companyInfoList_errorM",
    "remindM": "#companyInfoList_remindM",
    "selectM": "#companyInfoList_selectM",
    "importM": "#Import-modal"
});
