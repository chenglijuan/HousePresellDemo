(function(baseInfo){
	var listVue = new Vue({
		el : baseInfo.listDivId,
		data : {
			interfaceVersion :19000101,
            poName: "Empj_PjDevProgressForcast",
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			keyword : null,
			selectItem : [],
            selectModelItem : [],
			theState: 0,//正常为0，删除为1
            busiState: "全部",
            empj_PjDevProgressForcastList:[],
            diabaleShowLog: true,

            developCompanyId: "",
            projectId: "",
            emmp_CompanyInfoList: [],
            empj_ProjectInfoList: [],
            orderBy:"",

            //修改钮是否可操作
            enableEdit: false,
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			listItemSelectHandle: listItemSelectHandle,
			empj_PjDevProgressForcastAdd: empj_PjDevProgressForcastAdd,
            empj_PjDevProgressForcastEdit: empj_PjDevProgressForcastEdit,
            showLog: showLog,
            pjDevProgressForcastExportExcelHandle: pjDevProgressForcastExportExcelHandle,
            empj_PjDevProgressForcastDetailPageOpen: empj_PjDevProgressForcastDetailPageOpen,
			search:search,
            resetSearchInfo: resetSearchInfo,
			changePageNumber : function(data){
				listVue.pageNumber = data;
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

            sortChange:sortChange,
            changeCompanyListener: changeCompanyListener,
            changeCompanyEmpty: changeCompanyEmpty,
            changeprojectListener: changeprojectListener,
            changeProjectEmpty: changeProjectEmpty,
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue,
            'vue-listselect': VueListSelect,
		},
		watch:{
//			pageNumber : refresh,
		}
	});

	//------------------------方法定义-开始------------------//

    //按钮操作------------点击新增
    function empj_PjDevProgressForcastAdd()
    {
        enterNewTab("", "新增工程进度预测", "empj/Empj_PjDevProgressForcastAdd.shtml");
    }

	//按钮操作------------点击修改
	function empj_PjDevProgressForcastEdit()
	{
        if(listVue.selectItem.length == 1)
        {
            var theModel = listVue.selectModelItem[0];
            if (theModel.busiState != "已维护")
            {
                var tableId = listVue.selectItem[0];
                tableId = 'PjDevProgressForcastEdit_' + tableId;
                enterNewTab(tableId, "编辑工程进度预测", 'empj/Empj_PjDevProgressForcastEdit.shtml');
                // $("#tabContainer").data("tabs").addTab({id: tableId , text: '修改工程进度预测', closeable: true, url: 'empj/Empj_PjDevProgressForcastEdit.shtml'});
            }
            else
            {
                // generalErrorModal(null, "已提交的工程进度预测信息不能修改");
                noty({"text":"已提交的工程进度预测信息不能修改","type":"error","timeout":2000});
            }
        }
        else
        {
            // generalErrorModal(null, "请选择一个且只选一个要修改的工程进度预测信息");
            noty({"text":"请选择一个且只选一个要修改的工程进度预测信息","type":"error","timeout":2000});
        }
    }

    //查看日志
    function showLog() {
        console.log("------asdgasgas ");
        // enterLogTab(listVue.poName+"Template",TEMPLATE_PO_PATH);
        enterLogTabWithTemplate(listVue.poName, listVue)
    }

    //按钮操作-----------导出Excel
	function pjDevProgressForcastExportExcelHandle()
	{
	    if (listVue.empj_PjDevProgressForcastList.length == 0)
        {
            generalErrorModal(null, "请先添加工程进度巡查数据");
            return;
        }

        var model = {
            interfaceVersion: listVue.interfaceVersion,
            idArr: listVue.selectItem,
            keyword: listVue.keyword,
            busiState: listVue.busiState,
        };
        new ServerInterface(baseInfo.exportExcelInterface).execute(model, function (jsonObj) {
            if (jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                window.location.href=jsonObj.fileDownloadPath;
                listVue.selectItem = [];
                listVue.selectModelItem = [];
                refresh();
            }
        });
    }

    //列表操作-----------跳转到工程进度预测详情
	function empj_PjDevProgressForcastDetailPageOpen(tableId)
	{
        tableId = 'PjDevProgressForcastDetail_' + tableId;
        enterNewTab(tableId, "工程进度预测详情", 'empj/Empj_PjDevProgressForcastDetail.shtml');
        // $("#tabContainer").data("tabs").addTab({id: tableId , text: '工程进度预测详情', closeable: true, url: 'empj/Empj_PjDevProgressForcastDetail.shtml'})
    }

    //列表操作----------关键字搜索、下拉列表、排序筛选
    function sortChange(column) {
        listVue.orderBy=generalOrderByColumn(column)
        refresh()
    }

    function changeCompanyListener(data) {
        if (listVue.developCompanyId != data.tableId) {
            listVue.developCompanyId = data.tableId
            listVue.projectId = null
            // listVue.refresh();
            getProjectList();
        }
    }

    function changeCompanyEmpty() {
        if (listVue.developCompanyId != null) {
            listVue.developCompanyId = null
            listVue.projectId = null
            // listVue.refresh();
            getProjectList();
        }
    }

    function changeprojectListener(data) {
        if (listVue.projectId != data.tableId) {
            listVue.projectId = data.tableId
            // listVue.refresh()
        }
    }

    function changeProjectEmpty() {
        if (listVue.projectId != null) {
            listVue.projectId = null
            // listVue.refresh()
        }
    }

	//按钮操作------------搜索
	function search()
	{
		listVue.pageNumber = 1;
		refresh();
	}

	//按钮操作-----------重置搜索条件
	function resetSearchInfo()
	{
		listVue.keyword = null;
		listVue.theState = 0;
		listVue.busiState = "全部";
        listVue.pageNumber = 1;
        listVue.countPerPage = 20;
        listVue.totalPage = 1;
        listVue.totalCount = 1;
        listVue.developCompanyId = "",
        listVue.projectId = "",

        getProjectList();
        refresh();
    }

	//列表操作---选择工程进度预测信息
	function listItemSelectHandle(val)
	{
        listVue.selectItem = [];
        listVue.selectModelItem = [];
        for (var index = 0; index < val.length; index++) {
            var element = val[index].tableId;
            listVue.selectItem.push(element)
            listVue.selectModelItem.push(val[index]);
        }

        if(listVue.diabaleShowLog!=undefined){
            if(listVue.selectItem.length==1){
                listVue.diabaleShowLog=false
            }else{
                listVue.diabaleShowLog=true
            }
        }

        listVue.enableEdit = true;
        for (var index = 0; index < val.length; index++) {
            var itemModel = val[index];
            if (itemModel.busiState == "已维护")
            {
                listVue.enableEdit = false;
                break;
            }
        }
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
            busiState: this.busiState,
            developCompanyId:listVue.developCompanyId,
            projectId:listVue.projectId,
            orderBy:listVue.orderBy,
        };
    }

    //列表操作--------------刷新
    function refresh()
    {
    	// console.log(listVue.getSearchForm());
        new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj){
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                // console.log(jsonObj.empj_PjDevProgressForcastList);
                listVue.empj_PjDevProgressForcastList=jsonObj.empj_PjDevProgressForcastList;
                listVue.pageNumber=jsonObj.pageNumber;
                listVue.countPerPage=jsonObj.countPerPage;
                listVue.totalPage=jsonObj.totalPage;
                listVue.totalCount = jsonObj.totalCount;
                listVue.keyword=jsonObj.keyword;
                listVue.selectedItem=[];
                //动态跳转到锚点处，id="top"
                document.getElementById('empj_PjDevProgressForcastListDiv').scrollIntoView();
            }
        });
    }

    function getCompanyList()
    {
        var form=getTotalListForm()
        form["theType"]=1; //类型选择开发企业，变量S_CompanyType
        serverRequest("../Emmp_CompanyInfoForSelect",form,function (jsonObj) {
            listVue.emmp_CompanyInfoList=jsonObj.emmp_CompanyInfoList;
        });
    }

    function getProjectList()
    {
        var form=getTotalListForm()
        form["developCompanyId"]=listVue.developCompanyId;
        serverRequest("../Empj_ProjectInfoForSelect",form,function (jsonObj) {
            listVue.empj_ProjectInfoList=jsonObj.empj_ProjectInfoList;
        });
    }

    function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}

    function getBuildingDetail(buildingId)
	{
		var model = {
				interfaceVersion : 19000101,
				tableId : buildingId,
		};
		new ServerInterface(baseInfo.buildingDetailInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				listVue.keyword = jsonObj.empj_BuildingInfo.eCodeFromConstruction;
		        listVue.refresh();
			}
		});
	}
    
	function initData()
	{
		initButtonList();
        getCompanyList();
        getIdFormTab("", function (id) {
            console.log('list id is ' + id)
            if (id.indexOf("BuildingTable") != -1) {//从楼盘表进入
                var splitArr = id.split("-")
                listVue.projectId = Number(splitArr[0])
                console.log('projectId is ' + listVue.projectId)
                listVue.developCompanyId = Number(splitArr[2])
                getProjectList();
                
                var buildingId = Number(splitArr[3])
                getBuildingDetail(buildingId);
            }
        });
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#empj_PjDevProgressForcastListDiv",
	"buildingDetailInterface":"../Empj_BuildingInfoDetail",
	"listInterface":"../Empj_PjDevProgressForcastList",
    "exportExcelInterface":"../Empj_PjDevProgressForcastExportExcel",
});
