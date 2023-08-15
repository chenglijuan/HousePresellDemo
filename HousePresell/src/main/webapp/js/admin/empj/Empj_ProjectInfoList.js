(function(baseInfo){
	var listVue = new Vue({
		el : baseInfo.listDivId,
		data : {
			interfaceVersion :19000101,
            poName:"Empj_ProjectInfo",
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			keyword : "",
			selectItem : [],
            selectModel : [],
			isAllSelected : false,
			theState:0,//正常为0，删除为1
			busiState:"",
            approvalState:"全部",
			empj_ProjectInfoList:[],
            diabaleShowLog:true,
            developCompanyId: "",
            cityRegionId : "",
            emmp_CompanyInfoList: [],
            sm_CityRegionInfoList: [],
            orderBy:"",
            //修改、删除按钮是否可操作
            enableEdit: false,
            enableDelete: false,
            busiStateList :[
            	{tableId:"未备案",theName:"未备案"},
            	{tableId:"已备案",theName:"已备案"}
            ]
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
            getBatchDeleteForm : getBatchDeleteForm,
			empj_ProjectInfoDel : empj_ProjectInfoDel,
            empj_ProjectInfoBatchDel : empj_ProjectInfoBatchDel,
			search:search,
            resetSearchInfo:resetSearchInfo,
			projectInfoAddHandle: projectInfoAddHandle,
			projectInfoEditHandle: projectInfoEditHandle,
            showLog: showLog,
			projectInfoDeleteHandle: projectInfoDeleteHandle,
            projectInfoExportExcelHandle: projectInfoExportExcelHandle,
            empj_ProjectInfoDetailPageOpen : empj_ProjectInfoDetailPageOpen,
			changePageNumber : function(data){
				listVue.pageNumber = data;
				listVue.refresh();
			},
            changeCountPerPage : function (data) {
                if (listVue.countPerPage != data) {
                    listVue.countPerPage = data;                    
                }
                listVue.refresh();
            },
			handleSelectionChange: handleSelectionChange,
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
            changeCityRegionListener: changeCityRegionListener,
            changeCityRegionEmpty: changeCityRegionEmpty,
            changeBusiState: function(data){
            	listVue.busiState = data.tableId;
            },
            changeBusiStateEmpty : function(){
            	listVue.busiState = null;
            }
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue,
            'vue-listselect': VueListSelect,
		},
		watch:{
			pageNumber : refresh,
		}
	});

    //列表操作----------关键字搜索、下拉列表、排序筛选
    function sortChange(column) {
        listVue.orderBy=generalOrderByColumn(column)
        refresh()
    }

    function changeCompanyListener(data) {
        if (listVue.developCompanyId != data.tableId) {
            listVue.developCompanyId = data.tableId
            // listVue.refresh()
        }
    }

    function changeCompanyEmpty() {
        if (listVue.developCompanyId != null) {
            listVue.developCompanyId = null
            // listVue.refresh()
        }
    }

    function changeCityRegionListener(data) {
        if (listVue.cityRegionId != data.tableId) {
            listVue.cityRegionId = data.tableId
            // listVue.refresh()
        }
    }

    function changeCityRegionEmpty() {
        if (listVue.cityRegionId != null) {
            listVue.cityRegionId = null
            // listVue.refresh()
        }
    }

    function search()
    {
        listVue.pageNumber = 1;
        refresh();
    }
    
    //列表操作------------重置搜索
	function resetSearchInfo() {
        listVue.keyword = "";
        listVue.busiState = "全部";
        // listVue.approvalState = "全部";
        listVue.pageNumber = 1;
        listVue.developCompanyId = "",
        listVue.cityRegionId = "",
        refresh();
    }

    //列表操作--------------获取"搜索列表"表单参数
    function getSearchForm()
    {
        return {
            interfaceVersion:this.interfaceVersion,
            pageNumber:this.pageNumber,
            countPerPage:this.countPerPage,
            keyword:this.keyword,
            busiState: this.busiState,
            // approvalState: this.approvalState,
            // theState:this.theState,
            developCompanyId:listVue.developCompanyId,
            cityRegionId:listVue.cityRegionId,
            orderBy:listVue.orderBy,
        }
    }

    //列表操作---选择
    function handleSelectionChange(val)
    {
        listVue.selectItem = [];
        listVue.selectModel = [];
        for (var index = 0; index < val.length; index++) {
            var element = val[index].tableId;
            listVue.selectItem.push(element);
            listVue.selectModel = val;
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
            if (itemModel.approvalState == "审核中")
            {
                listVue.enableEdit = false;
                break;
            }
        }
        listVue.enableDelete = true;
        for (var index = 0; index < val.length; index++) {
            var itemModel = val[index];
            if (itemModel.busiState == "已备案" || itemModel.approvalState == "审核中")
            {
                listVue.enableDelete = false;
                break;
            }
        }
    }

    //查看日志
    function showLog() {
        enterLogTab(listVue.poName,undefined,listVue)
    }

    //列表操作--------------获取“删除资源”表单参数
	function getBatchDeleteForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			idArr:this.selectItem
		}
	}
	
	function projectInfoDeleteHandle()
	{
		if (listVue.selectItem.length == 0)
		{
            noty({"text":"请选择要删除的项目","type":"error","timeout":2000});
            return;
		}

		//删除按钮listVue.selectItem
        var disableDeleteProjectItem = new Array();
        for (var index=0; index < listVue.selectModel.length; index++)
        {
            var itemProject = listVue.selectModel[index];
            //if (itemProject.busiState == "已备案" || (itemProject.busiState == "未备案" && itemProject.approvalState == "审核中"))
            if (itemProject.busiState == "已备案" || itemProject.approvalState == "审核中")
            {
                disableDeleteProjectItem.push(itemProject.theName);
            }
        }
        if (disableDeleteProjectItem.length > 0)
        {
            var text = "项目：\""+disableDeleteProjectItem.join("，")+"\"处于已备案或审核中状态，不能删除";
            noty({"text":text,"type":"error","timeout":2000});
        }
        else
        {
            generalSelectModal(function(){
                empj_ProjectInfoBatchDel();
            }, "确认删除吗？");
        }
	}
	
	//批量删除
	function empj_ProjectInfoBatchDel()
	{
        new ServerInterface(baseInfo.batchDeleteInterface).execute(listVue.getBatchDeleteForm(), function(jsonObj){
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                listVue.selectItem = [];
                listVue.selectModel = [];
                refresh();
            }
        });
    }

    //单个删除
	function empj_ProjectInfoDel(projectInfoId)
	{
		// var model = {
        //     interfaceVersion:listVue.interfaceVersion,
        //     tableId:projectInfoId,
        // }
        //
        // new ServerInterface(baseInfo.deleteInterface).execute(model, function(jsonObj){
        //     if(jsonObj.result != "success")
        //     {
        //         noty({"text":jsonObj.info,"type":"error","timeout":2000});
        //     }
        //     else
        //     {
        //         listVue.selectItem = [];
        //         refresh();
        //     }
        // });
	}

	//列表操作--------------刷新
	function refresh()
	{
		new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
                generalErrorModal(jsonObj);
			}
			else
			{
				listVue.empj_ProjectInfoList=jsonObj.empj_ProjectInfoList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
                listVue.selectModel=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('empj_ProjectInfoListDiv').scrollIntoView();
			}
		});
	}



	//按钮操作--------导出Excel
    function projectInfoExportExcelHandle()
    {
    	var model = {
    	    interfaceVersion: listVue.interfaceVersion,
            idArr: listVue.selectItem,
            keyword: listVue.keyword,
        }
        new ServerInterface(baseInfo.exportExcelInterface).execute(model, function (jsonObj) {
            if (jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                window.location.href=jsonObj.fileDownloadPath;
                listVue.selectItem = [];
                refresh();
            }
        });
    }

    //按钮操作--------跳转到添加项目页面
    function projectInfoAddHandle(){
        // $("#tabContainer").data("tabs").addTab({ id: 'Empj_ProjectInfoAdd', text: '新增项目', closeable: true, url: 'empj/Empj_ProjectInfoAdd.shtml' });
        enterNewTab("", "新增项目", 'empj/Empj_ProjectInfoAdd.shtml');
    }

    //按钮操作-------跳转到修改项目页面
    function projectInfoEditHandle()
    {
        if(listVue.selectItem.length == 1) {
            var theProjectInfo = listVue.selectModel[0];
            if (theProjectInfo.approvalState != "审核中") {
                var tableId = listVue.selectItem[0];
                //var theTableId = 'ProjectInfoEdit_' + tableId;
                // $("#tabContainer").data("tabs").addTab({id: theTableId , text: '项目修改', closeable: true, url: 'empj/Empj_ProjectInfoEdit.shtml'});
                enterNewTab(tableId, "编辑项目", 'empj/Empj_ProjectInfoEdit.shtml');
            }
            else
            {
                noty({"text":"您选择的项目正在审批中","type":"error","timeout":2000});
            }
        }
        else
        {
            noty({"text":"请选择一个且只选一个要修改的项目","type":"error","timeout":2000});
        }
    }

    //列表操作-------跳转到项目详情页面
    function empj_ProjectInfoDetailPageOpen(tableId)
    {
        var theTableId = tableId;
        // $("#tabContainer").data("tabs").addTab({ id: theTableId, text: '项目详情', closeable: true, url: 'empj/Empj_ProjectInfoDetail.shtml' });
        enterNewTab(theTableId, "项目详情", 'empj/Empj_ProjectInfoDetail.shtml');
    }

    function getCompanyList()
    {
        serverRequest("../Emmp_CompanyInfoForSelect",getTotalListForm(),function (jsonObj) {
            listVue.emmp_CompanyInfoList=jsonObj.emmp_CompanyInfoList;
        });
    }

    function getCityRegionList()
    {
        serverRequest("../Sm_CityRegionInfoForSelect",getTotalListForm(),function (jsonObj) {
            listVue.sm_CityRegionInfoList=jsonObj.sm_CityRegionInfoList;
        });
    }

	//根据按钮权限，动态控制，该页面的所有“可控按钮”的显示与否
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}

	function initData()
	{
		initButtonList();
        getCompanyList();
        getCityRegionList();
        listVue.refresh();
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#empj_ProjectInfoListDiv",
	"listInterface":"../Empj_ProjectInfoList",
	"deleteInterface":"../Empj_ProjectInfoDelete",
	"batchDeleteInterface":"../Empj_ProjectInfoBatchDelete",
    "exportExcelInterface": "../Empj_ProjectInfoExportExcel",
});
