(function(baseInfo){
	var listVue = new Vue({
		el : baseInfo.listDivId,
		data : {
			poName:"Empj_BuildingInfo",
			interfaceVersion :19000101,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			keyword : "",
			selectItem : [],
			selectItem1 : [],
			isAllSelected : false,
			isAllSelected1 : false,
			theState:0,//正常为0，删除为1
			escrowState : "",
			empj_BuildingInfoList:[],
			sm_User:null,
			localProject: "",
			projectListByLocal: [],
			saleProject: "",
			projectList: [],
			eCodeFromConstruction: "",
			errorMessage: "",
			successMessage: "",
			noImportNum: 0,
			unImportList: [],
			importNum:0,
			ImportList:[],
			approvalState : "",
			//筛选
			approvalStateSearch : "",
			companyId: "",
            companyList: [],
            projectId: "",
            projectList: [],
            orderBy : "",

            diabaleShowLog: true,
            isSearch1: true,//导入时搜索按钮
            isSave:true,//导入保存时按钮控制
		},
		methods : {
			showLog: showLog,
			refresh : refresh,
			initData : initData,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			empj_BuildingInfoDelete : empj_BuildingInfoDelete,
			search : search,
			reset : reset,
			checkAllClicked : checkAllClicked,
			empj_BuildingInfoAddPageOpen : empj_BuildingInfoAddPageOpen,
			empj_BuildingInfoDetailPageOpen : empj_BuildingInfoDetailPageOpen,
			empj_BuildingInfoEditPageOpen : empj_BuildingInfoEditPageOpen,
			changePageNumber : function(data){
				listVue.pageNumber = data;
			},
			changeCountPerPage : function (data) {
				if (listVue.countPerPage != data) {
					listVue.countPerPage = data;
					listVue.refresh();
				}
			},
			indexMethod: indexMethod,
			listItemSelectHandle : listItemSelectHandle,
			buildingInfoExportExcel : buildingInfoExportExcel,
			openDialog: openDialog,
			search1: search1,
			getImportList: getImportList,
			getImportForm: getImportForm,
			handleSelectionChange1: handleSelectionChange1,
			allEmptyHandle: allEmptyHandle,
			save: save,
			deleteHandle: deleteHandle,
			getSaveForm:getSaveForm,
			allSelect: allSelect,
			changeCompanyListener: function (data) {
		        if (listVue.companyId != data.tableId) {
		            listVue.companyId = data.tableId
		            getProjectList();
		        }
		    },
            changeCompanyEmpty: function () {
                if (listVue.companyId != null) {
                    listVue.companyId = null
                }
            },
            changeprojectListener: function (data) {
                if (listVue.projectId != data.tableId) {
                    listVue.projectId = data.tableId
                }
            },
            changeProjectEmpty: function () {
                if (listVue.projectId != null) {
                    listVue.projectId = null
                }
            },
            sortChange : function (column) {
                listVue.orderBy = generalOrderByColumn(column)
                refresh();
            }
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue,
			'vue-listselect': VueListSelect,
		},
		watch:{
//			pageNumber : refresh,
			selectItem : selectedItemChanged,
			selectItem1 : selectedItemChanged1,
		}
	});

	//------------------------方法定义-开始------------------//
	 
	function indexMethod(index)
	{
		return generalIndexMethod(index, listVue);
	}
	
	function listItemSelectHandle(list) 
	{
		if(list.length == 1)
		{
			listVue.approvalState = list[0].approvalState;
		}
		generalListItemSelectHandle(listVue,list)
	}
	
	//列表操作--------------获取"搜索列表"表单参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			pageNumber:this.pageNumber,
			countPerPage:this.countPerPage,
			totalPage:this.totalPage,
			keyword:this.keyword,
			theState:this.theState,
			escrowState:this.escrowState,
			approvalState:this.approvalStateSearch,
			developCompanyId:this.companyId,
			projectId:this.projectId,
			orderBy:this.orderBy,
		}
	}
	//列表操作--------------获取“删除资源”表单参数
	function getDeleteForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			idArr:this.selectItem
		}
	}
	
	function deleteBuilding()
	{
		new ServerInterface(baseInfo.deleteInterface).execute(listVue.getDeleteForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				parent.generalErrorModal(jsonObj);
			}
			else
			{
				listVue.selectItem = [];
				refresh();
			}
		});
	}
	
	function empj_BuildingInfoDelete()
	{
		parent.generalSelectModal(deleteBuilding, "确认删除");
	}
	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{
		listVue.isAllSelected = (listVue.empj_BuildingInfoList.length > 0)
		&&	(listVue.selectItem.length == listVue.empj_BuildingInfoList.length);
	}
	function selectedItemChanged1()
	{
		listVue.isAllSelected = (listVue.unImportList.length > 0)
		&&	(listVue.selectItem1.length == listVue.unImportList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.empj_BuildingInfoList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.empj_BuildingInfoList.forEach(function(item) {
	    		listVue.selectItem.push(item.tableId);
	    	});
	    }
	}
	//列表操作--------------刷新
	function refresh()
	{
		new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
                parent.generalErrorModal(jsonObj);
			}
			else
			{
				listVue.empj_BuildingInfoList=jsonObj.empj_BuildingInfoList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('empj_BuildingInfoListDiv').scrollIntoView();
			}
		});
	}
	
	//列表操作------------搜索
	function search()
	{
		listVue.pageNumber = 1;
		refresh();
	}
	
	function reset()
	{
		listVue.keyword = "";
		listVue.escrowState = "";
		listVue.approvalStateSearch = "";
		listVue.projectId = "";
		listVue.companyId = "";
	}
	
	//跳转新增页面
	function empj_BuildingInfoAddPageOpen() 
	{
        parent.enterNewTabTest("", "新增楼幢", "test/Test_BuildingInfoAdd.shtml");
	}
	
	//跳转详情页面
	function empj_BuildingInfoDetailPageOpen(tableId) 
	{
        parent.enterNewTabTest(tableId, "楼幢详情", "test/Test_BuildingInfoDetail.shtml");
	}
	
	//跳转编辑页面
	function empj_BuildingInfoEditPageOpen() 
	{
        parent.enterNewTabTest(listVue.selectItem, "编辑楼幢", "test/Test_BuildingInfoEdit.shtml");
	}
	
	function buildingInfoExportExcel()
	{
		var model = {
			interfaceVersion : 19000101,
			idArr : listVue.selectItem,
			keyword : listVue.keyword,
		};
		
		new ServerInterface(baseInfo.buildingExportExcelInterface).execute(model, function (jsonObj) {
			if (jsonObj.result != "success")
			{
                parent.generalErrorModal(jsonObj);
			}
			else
			{
				window.location.href=jsonObj.fileDownloadPath;
				listVue.selectItem = [];
				refresh();
			}
		});
	}
	
	function openDialog()
	{
		$(baseInfo.importM).modal({
            backdrop :'static'
        });
		setTimeout(function(){
			var height = $("#buildBox").outerHeight();
			$("#loadingBox").outerHeight(height + "px");
		},2000);
		listVue.localProject = "";
		listVue.saleProject = "";
		listVue.unImportList = [];
		listVue.ImportList = [];
		listVue.noImportNum = 0;
		listVue.importNum = 0;
		
		// 加载本地项目列表信息
		loadLocalList();
		// 加载预售项目列表信息
		loadSaleList();
	}
	
	function loadLocalList()
	{
		var model = {
				interfaceVersion : 19000101,
				type : "listByLocal",
			};
		new ServerInterface(baseInfo.loadLocalInterface).execute(model, function (jsonObj) {
			if (jsonObj.result != "success")
			{
                parent.generalErrorModal(jsonObj);
			}
			else
			{
				listVue.projectListByLocal=jsonObj.projectListByLocal;
			}
		});
	}
	
	function loadSaleList()
	{
		var model = {
				interfaceVersion : 19000101,
				type : "list",
			};
		new ServerInterface(baseInfo.loadLocalInterface).execute(model, function (jsonObj) {
			if (jsonObj.result != "success")
			{
                parent.generalErrorModal(jsonObj);
			}
			else
			{
				listVue.projectList = jsonObj.projectList;
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

	function getImportList() 
	{
		listVue.isSearch1=false;
		new ServerInterface(baseInfo.importInterface).execute(listVue.getImportForm(), function (jsonObj) {
			listVue.isSearch1=true;
			if (jsonObj.result != "success")
			{
                listVue.errorMessage = jsonObj.info;
                parent.generalErrorModal(jsonObj);
			}
			else {
				listVue.unImportList = jsonObj.buildingList;
				listVue.noImportNum = listVue.unImportList.length;
			}
		});
	}
	
	function getImportForm()
	{
		return {
			interfaceVersion : 19000101,
			type: "list",
			PROJECTID:listVue.saleProject,
			BUILDINGNO:listVue.eCodeFromConstruction
		}
	}
	
	function handleSelectionChange1(val)
	{
		if(val.length == 1) {
			var model = {
					externalId: val[0].externalId,
					theNameFromPresellSystem: val[0].theNameFromPresellSystem,
					eCodeFromConstruction: val[0].eCodeFromConstruction,
					eCodeFromPublicSecurity: val[0].eCodeFromPublicSecurity,
					unitNumber: val[0].unitNumber,
					buildingArea: val[0].buildingArea,
					position: val[0].position,
					upfloorNumber: val[0].upfloorNumber,
					downfloorNumber: val[0].downfloorNumber,
					endDate: val[0].endDate,
					remark: val[0].remark,
					eCodeOfProjectFromPresellSystem: val[0].eCodeOfProjectFromPresellSystem,
					eCodeFromPresellCert: val[0].eCodeFromPresellCert,
					
			}
			listVue.ImportList.push(model);
			listVue.importNum = listVue.ImportList.length;
			
			listVue.unImportList.forEach(function(item, index) {
　　　　　　　          // id 是每一行的数据id
                if(val[0].theNameFromPresellSystem == item.theNameFromPresellSystem){
                	listVue.unImportList.splice(index, 1);
                	val[0].theNameFromPresellSystem = "";
                }
	         })
	 		listVue.noImportNum = listVue.unImportList.length;
		}
		listVue.selectItem = [];
	}
	
	function allEmptyHandle()
	{
		var length = listVue.ImportList.length;
		if(length != 0) {
			for(var i = 0;i < length; i++) {
				var model = {
					externalId: listVue.ImportList[i].externalId,
					theNameFromPresellSystem: listVue.ImportList[i].theNameFromPresellSystem,
					eCodeFromConstruction: listVue.ImportList[i].eCodeFromConstruction,
					eCodeFromPublicSecurity: listVue.ImportList[i].eCodeFromPublicSecurity,
					unitNumber: listVue.ImportList[i].unitNumber,
					buildingArea: listVue.ImportList[i].buildingArea,
					position: listVue.ImportList[i].position,
					upfloorNumber: listVue.ImportList[i].upfloorNumber,
					downfloorNumber: listVue.ImportList[i].downfloorNumber,
					endDate: listVue.ImportList[i].endDate,
					remark: listVue.ImportList[i].remark,
					eCodeOfProjectFromPresellSystem: listVue.ImportList[i].eCodeOfProjectFromPresellSystem,
					eCodeFromPresellCert: listVue.ImportList[i].eCodeFromPresellCert,
				}
				listVue.unImportList.push(model);
			}
			listVue.ImportList = [];
		}
		
		listVue.noImportNum = listVue.unImportList.length;
		listVue.importNum = 0;
	}
	
	function save()
	{
		$("#buildBox").hide();
		$("#loadingBox").show();
		listVue.isSave = false;
		new ServerInterface(baseInfo.importInterface).execute(listVue.getSaveForm(), function (jsonObj) {
			$("#loadingBox").hide();
			$("#buildBox").show();
			listVue.isSave = true;
			if (jsonObj.result != "success")
			{
                listVue.errorMessage = jsonObj.info;
                parent.generalErrorModal(jsonObj);
			}
			else {
				$(baseInfo.importM).modal('hide');
				parent.generalSuccessModal();
				refresh();
			}
		});
	}
	
	function getSaveForm()
	{
		var list = listVue.ImportList;
		list = JSON.stringify(list);
		return {
			interfaceVersion: this.interfaceVersion,
			type: "save",
			projectLocalId: listVue.localProject,
			saveBuildingList: list,
		}
	}
	
	function allSelect()
	{
		var length = listVue.unImportList.length;
		if(length != 0) {
			for(var i = 0;i < length; i++) {
				var model = {
					externalId: listVue.unImportList[i].externalId,
					theNameFromPresellSystem: listVue.unImportList[i].theNameFromPresellSystem,
					eCodeFromConstruction: listVue.unImportList[i].eCodeFromConstruction,
					eCodeFromPublicSecurity: listVue.unImportList[i].eCodeFromPublicSecurity,
					unitNumber: listVue.unImportList[i].unitNumber,
					buildingArea: listVue.unImportList[i].buildingArea,
					position: listVue.unImportList[i].position,
					upfloorNumber: listVue.unImportList[i].upfloorNumber,
					downfloorNumber: listVue.unImportList[i].downfloorNumber,
					endDate: listVue.unImportList[i].endDate,
					remark: listVue.unImportList[i].remark,
					eCodeOfProjectFromPresellSystem: listVue.unImportList[i].eCodeOfProjectFromPresellSystem,
					eCodeFromPresellCert: listVue.unImportList[i].eCodeFromPresellCert,
				}
				listVue.ImportList.push(model);
			}
			listVue.unImportList = [];
		}
		
		listVue.noImportNum = 0;
		listVue.importNum = listVue.ImportList.length;
	}
	
	function deleteHandle(index,val)
	{
		var model = {
				externalId: val.externalId,
				theNameFromPresellSystem: val.theNameFromPresellSystem,
				eCodeFromConstruction: val.eCodeFromConstruction,
				eCodeFromPublicSecurity: val.eCodeFromPublicSecurity,
				unitNumber: val.unitNumber,
				buildingArea: val.buildingArea,
				position: val.position,
				upfloorNumber: val.upfloorNumber,
				downfloorNumber: val.downfloorNumber,
				endDate: val.endDate,
				remark: val.remark,
				eCodeOfProjectFromPresellSystem: val.eCodeOfProjectFromPresellSystem,
				eCodeFromPresellCert: val.eCodeFromPresellCert,
		}
		listVue.ImportList.splice(index,1);
		listVue.unImportList.push(model);
		listVue.noImportNum = listVue.unImportList.length;
		listVue.importNum = listVue.ImportList.length;
	}
	//根据按钮权限，动态控制，该页面的所有“可控按钮”的显示与否
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		parent.getButtonList();
	}
	function initData()
	{
		initButtonList();
		getCompanyList();
	}
	
	function showLog() {
        parent.enterLogTab(listVue.poName,undefined,listVue)
    }
	
	function getCompanyList() {
        serverRequest("../../Emmp_CompanyInfoForSelect",getTotalListForm(),function (jsonObj) {
            listVue.companyList=jsonObj.emmp_CompanyInfoList
        })

    }
	
	function getProjectList() {
		var model = getTotalListForm();
		model["developCompanyId"] = listVue.companyId;
		
        serverRequest("../../Empj_ProjectInfoForSelect",model,function (jsonObj) {
            listVue.projectList=jsonObj.empj_ProjectInfoList
        })
    }
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.initData();
	listVue.refresh();
	
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#empj_BuildingInfoListDiv",
    "importM": "#Import-modal1",
	"listInterface":"../../Empj_BuildingInfoList",
	"deleteInterface":"../../Empj_BuildingInfoDelete",
	"buildingExportExcelInterface":"../../Empj_BuildingInfoExportExcel",
	"getLoginSm_UserInterface":"../../Sm_UserGet",
	"importInterface": "../../Tb_b_building",
	"loadLocalInterface":"../../Tb_b_project",
});
