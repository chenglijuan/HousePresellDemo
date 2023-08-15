(function(baseInfo){
	var listVue = new Vue({
		el : baseInfo.listDivId,
		data : {
			interfaceVersion :19000101,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			keyword : "",
			selectItem : [],
			selectTableId : [],
			isAllSelected : false,
			theState:0,//正常为0，删除为1
			userStartId:null,
			userStartList:[],
			userRecordId:null,
			userRecordList:[],
			developCompanyId : "",
			companyList:[],
			projectId:"",
			projectList:[],
			buildingId:"",
            eCodeFromConstruction:"",
			buildingList:[],
			buildingUnitId:null,
			buildingUnitList:[],
			bankId:null,
			bankList:[],
			tgpf_BuildingRemainRightLogList:[
//				{
//					theNameOfCompany:'常州万达地产集团华云路分公司',
//					theNameOfProject:'常州东湖大郡二期',
//					eCodeOfBuilding:'1号',
//					eCodeOfBuild:'1号',
//					currentLimitedRatio:'60%',
//					nodeLimitedAmount:'56，879，522，566',
//					totalAccountAmount:'56，879，522，566',
//					billTimeStamp:'2018-07-15',
//					busiState:'1',
//				}
			],
			busiState:"",
            upDisabled:true,
            busiStateList : [
				{tableId:"1",theName:"匹配成功"},
				{tableId:"2",theName:"未比对"},
				{tableId:"3",theName:"不匹配"},
			],
		},
		methods : {
			refresh : refresh,
			initData:initData,
			handleSelectionChange : handleSelectionChange,
			getSearchForm : getSearchForm,
			getCompanySearchForm : getCompanySearchForm,
			onCompanySelected : onCompanySelected,
			getProjectSearchForm : getProjectSearchForm,
			onProjectSelected : onProjectSelected,
            onBuildingSelected : onBuildingSelected,
			getBuildingSearchForm : getBuildingSearchForm,
			getExportExcelForm : getExportExcelForm,
			search:search,
			checkAllClicked : checkAllClicked,
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
			indexMethod: function (index) {
				if (listVue.pageNumber > 1) {
					return (listVue.pageNumber - 1) * listVue.countPerPage - 0 + (index - 0 + 1);
				}
				if (listVue.pageNumber <= 1) {
					return index - 0 + 1;
				}
			},
		    maintain : maintain,
		    getCompanyList : getCompanyList,
		    exportExcelHandle : exportExcelHandle,
		    reset : reset,
		    changeBusiState : function(data){
				this.busiState = data.tableId;
			},
			busiStateEmpty : function(){
				this.busiState =  null;
			}
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue,
            'vue-listselect': VueListSelect,
		},
		watch:{
		//	pageNumber : refresh,
//			selectItem : selectedItemChanged,
		}
	});

	//------------------------方法定义-开始------------------//
	 
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
			userStartId:this.userStartId,
			userRecordId:this.userRecordId,
			developCompanyId:this.developCompanyId,
			projectId:this.projectId,
			buildingId:this.buildingId,
			buildingUnitId:this.buildingUnitId,
			bankId:this.bankId,
			billTimeStamp:$("#date20030101").val(),
			busiState:this.busiState,
		}
	}
	
	function getCompanySearchForm() {
		return {
			interfaceVersion: this.interfaceVersion,
			pageNumber:0,
		}
	}
	
	function getProjectSearchForm() {
		return {
			interfaceVersion: this.interfaceVersion,
			pageNumber:0,
			developCompanyId: this.developCompanyId,
		}
	}
	
	function getBuildingSearchForm() {
		return {
			interfaceVersion: this.interfaceVersion,
			pageNumber:0,
			projectId: this.projectId,
		}
	}
	
	function getExportExcelForm() {
		return {
			interfaceVersion: this.interfaceVersion,
			buildingRemainRightLogIds: this.selectTableId,
		}
	}
	
	function isInterger(o) {
		return typeof obj === 'number' && obj%1 === 0;
	}

	function handleSelectionChange(val) {
		listVue.selectItem = [];
		listVue.selectTableId = [];
		for (var i = 0; i < val.length; i++) {
			var index = listVue.tgpf_BuildingRemainRightLogList.indexOf(val[i]);
			listVue.selectItem.push(index)
			listVue.selectTableId.push(val[i].tableId)
		}
		listVue.selectItem.sort(sortNumber);
//		var s = "";
//		for (var i = 0; i < listVue.selectItem.length; i++) {
//			s = s + listVue.selectItem[i] + ',';
//		}
//		alert(s);
        if(val.length==1)
        {
            listVue.upDisabled = false;
        }
        else
        {
            listVue.upDisabled = true;
        }
	}
	function sortNumber(a,b) {
        return a-b
	}
	
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgpf_BuildingRemainRightLogList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgpf_BuildingRemainRightLogList.forEach(function(item) {
	    		listVue.selectItem.push(item.tableId);
	    	});
	    }
	}
	//列表操作--------------刷新
	function refresh()
	{
		new ServerInterface(baseInfo.buildingRemainRightListInterface).execute(listVue.getSearchForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				listVue.tgpf_BuildingRemainRightLogList=jsonObj.tgpf_BuildingRemainRightLogList;
                listVue.tgpf_BuildingRemainRightLogList.forEach(function(item){
                    item.nodeLimitedAmount = addThousands(item.nodeLimitedAmount);
                    item.totalAccountAmount = addThousands(item.totalAccountAmount);
                    item.currentLimitedRatio = item.currentLimitedRatio + "%";
                })
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tgpf_BuildingRemainRightLogList').scrollIntoView();
			}
		});
	}
	
	//列表操作------------搜索
	function search()
	{
		listVue.pageNumber = 1;
		refresh();
	}
	
	function maintain()
	{
		if (listVue.selectItem.length > 0) {
			var theId = listVue.tgpf_BuildingRemainRightLogList[listVue.selectItem[0]].tableId;
			enterNewTab(theId, '留存权益对比', 'tgpf/Tgpf_RemainRightCompareList.shtml')
		}
	}
	
	function getCompanyList() {
		new ServerInterface(baseInfo.companyListInterface).execute(listVue.getCompanySearchForm(), function (jsonObj) {
			if (jsonObj.result != "success") {
				noty({ "text": jsonObj.info, "type": "error", "timeout": 2000 });
			}
			else {
				listVue.companyList = jsonObj.emmp_CompanyInfoList;
			}
		});
	}
	
	function onCompanySelected(data) {
		if (data == null) {
			if (listVue.developCompanyId == "") {
				return
			}
            listVue.developCompanyId = ""
		} else if (listVue.developCompanyId == data.tableId) {
			return
		} else {
            listVue.developCompanyId = data.tableId
            getProjectList()
		}
        listVue.projectList = []
        listVue.projectId = ""
        listVue.buildingList = []
        listVue.buildingId = ""
		listVue.eCodeFromConstruction = ""
	}
	
	function getProjectList()
	{
		new ServerInterface(baseInfo.projectListInterface).execute(listVue.getProjectSearchForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				listVue.projectList=jsonObj.empj_ProjectInfoList;
			}
		});
	}
	
	function onProjectSelected(data) {
        if (data == null) {
            if (listVue.projectId == "") {
                return
            }
            listVue.projectId = ""
        } else if (listVue.projectId == data.tableId) {
            return
        } else {
            listVue.projectId = data.tableId
            getBuildingList()
        }
        listVue.buildingList = []
        listVue.buildingId = ""
		listVue.eCodeFromConstruction = ""
	}
	
	function getBuildingList()
	{
		new ServerInterface(baseInfo.buildingListInterface).execute(listVue.getBuildingSearchForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				listVue.buildingList=jsonObj.empj_BuildingInfoList;
			}
		});
	}

    function onBuildingSelected(data) {
        if (data == null) {
            if (listVue.buildingId == "") {
                return
            }
            listVue.buildingId = ""
			listVue.eCodeFromConstruction = ""
        } else if (listVue.buildingId == data.tableId) {
            return
        } else {
            listVue.buildingId = data.tableId
			listVue.eCodeFromConstruction = data.eCodeFromConstruction
			console.log(listVue.eCodeFromConstruction)
        }
    }

	function exportExcelHandle()
	{
		if (listVue.selectItem.length > 0) {
			new ServerInterface(baseInfo.exportExcelInterface).execute(listVue.getExportExcelForm(), function (jsonObj) {
				if (jsonObj.result != "success")
				{
					noty({ "text": jsonObj.info, "type": "error", "timeout": 2000 });
				}
				else
				{
					window.location.href=jsonObj.fileDownloadPath;
//					listVue.selectItem = [];
//					refresh();
				}
			});
		}
	}

	function reset()
	{
		listVue.keyword = "";
		listVue.developCompanyId = "";
		listVue.projectId = "";
		listVue.buildingId = "";
		listVue.eCodeFromConstruction = ""
		$("#date20030101").val("")
		listVue.busiState = "";
        listVue.projectList = []
        listVue.buildingList = [];
        refresh();
	}
	  //根据按钮权限，动态控制，该页面的所有“可控按钮”的显示与否
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}
	function initData()
	{
		listVue.selectItem = []
		laydate.render({
			elem: '#date20030101',
		});
		initButtonList();
		listVue.getCompanyList();
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.initData();
	listVue.refresh();
	
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tgpf_RemainRightDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Tgpf_RemainRightList",
	"exportExcelInterface": "../Tgpf_RemainRightExportExcel",
	"buildingRemainRightListInterface":"../Tgpf_BuildingRemainRightLogList",
	"companyListInterface": "../Emmp_CompanyInfoForSelect",
	"projectListInterface":"../Empj_ProjectInfoList",
	"buildingListInterface":"../Empj_BuildingInfoList",
});
