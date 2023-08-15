(function(baseInfo){
	var listVue = new Vue({
		el : baseInfo.listDivId,
		data : {
			interfaceVersion :19000101,
            poName: "Empj_BldEscrowCompleted",
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			keyword : "",
			selectItem : [],
            selectModel : [],
            bldEscrowIdArr: [],
			theState: 0,//正常为0，删除为1
            busiState: "", //审批状态 全部，未备案，已备案
            empj_BldEscrowCompletedList: [],

            developCompanyId: "",
            projectId: "",
            emmp_CompanyInfoList: [],
            empj_ProjectInfoList: [],
            orderBy:"",

            //修改按钮是否可操作
            enableEdit: false,
            enableChange: false,
            delDisabled : true,
            busiStateList :[
            	{tableId:"未备案",theName:"未备案"},
            	{tableId:"已备案",theName:"已备案"}
            ],
            hasPush : "",
            pushList : [
            	{
            		tableId:"0",
            		theName:"否"
            	},
            	{
            		tableId:"1",
            		theName:"是"
            	}
            ],
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			listItemSelectHandle: listItemSelectHandle,
			search:search,
            resetSearchInfo: resetSearchInfo,
            bldEscrowAddHandle: bldEscrowAddHandle,
            bldEscrowEditHandle: bldEscrowEditHandle,
            bldEscrowExportExcelHandle: bldEscrowExportExcelHandle,
            bldEscrowDetailHandle: bldEscrowDetailHandle,
            bldEscrowApprovalCheck: bldEscrowApprovalCheck,
            showLog: showLog,
            changePageNumber: function (data) {
//				console.log(data);
                if (listVue.pageNumber != data) {
                    listVue.pageNumber = data;
                    listVue.refresh();
                }
            },
            changeCountPerPage : function (data) {
//				console.log(data);
                if (listVue.countPerPage != data) {
                    listVue.countPerPage = data;
                    listVue.refresh();
                }
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
            changeprojectListener: changeprojectListener,
            changeProjectEmpty: changeProjectEmpty,
            delBldEscrowCompleted : delBldEscrowCompleted,
            changeBusiState: function(data){
            	listVue.busiState = data.tableId;
            },
            changeBusiStateEmpty : function(){
            	listVue.busiState = null;
            },
            bldEscrowChangeHandle : bldEscrowChangeHandle,
            
            changePush : changePush,
            changePushEmpty : changePushEmpty,
            
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
		}
	});

	//------------------------方法定义-开始------------------//
	function changePush(data){
		listVue.hasPush = data.tableId;
	}
	
	function changePushEmpty(){
		listVue.hasPush = "";
	}

	function bldEscrowChangeHandle()
	{
		if (listVue.selectItem.length == 1)
		{
            var theBldEscrowCompletedInfo = listVue.selectModel[0];
            /*if (theBldEscrowCompletedInfo.busiState == "已备案" && theBldEscrowCompletedInfo.hasPush == "0")
            {
                var tableId = listVue.selectItem[0];
                tableId = 'BldEscrowCompletedEdit_' + tableId;
                enterNewTab(tableId, "变更托管终止", 'empj/Empj_BldEscrowCompletedChange.shtml');
            }*/
            if (theBldEscrowCompletedInfo.hasPush == "0")
            {
                var tableId = listVue.selectItem[0];
                tableId = 'BldEscrowCompletedEdit_' + tableId;
                enterNewTab(tableId, "变更托管终止", 'empj/Empj_BldEscrowCompletedChange.shtml');
            }
            else
            {
            	noty({"text":"您选择的单据不符合变更条件","type":"error","timeout":2000});
            }
		}
        else
        {
            noty({"text":"请选择一个且只选一个要修改的项目","type":"error","timeout":2000});
        }
    }
    //列表操作------------关键字搜索、下拉列表、排序筛选
    function search()
    {
        listVue.pageNumber = 1;
        refresh();
    }
    function sortChange(column) {
        listVue.orderBy=generalOrderByColumn(column)
        refresh()
    }

    function changeCompanyListener(data) {
        if (listVue.developCompanyId != data.tableId) {
            listVue.developCompanyId = data.tableId
            listVue.projectId = null
            // listVue.refresh()
            getProjectList();
        }
    }

    function changeCompanyEmpty() {
        if (listVue.developCompanyId != null) {
            listVue.developCompanyId = null
            listVue.projectId = null
            // listVue.refresh()
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
    //按钮操作------重置所有查询条件
    function resetSearchInfo()
    {
    	listVue.keyword = "";
    	listVue.busiState = "";
        listVue.pageNumber = 1;
        listVue.developCompanyId = "",
        listVue.projectId = "",
        listVue.hasPush = "",
        
        refresh();
    }

    //列表操作------勾选
    function listItemSelectHandle(val) {
    }
   
    //查看日志
    function showLog() {
        enterLogTab(listVue.poName+"Template",TEMPLATE_PO_PATH)
    }

    //列表操作--------------获取"搜索列表"表单参数
    function getSearchForm()
    {
        return {
            interfaceVersion:this.interfaceVersion,
            pageNumber:this.pageNumber,
            countPerPage:this.countPerPage,
            keyword:this.keyword,
            busiState:this.busiState,
            developCompanyId:listVue.developCompanyId,
            projectId:listVue.projectId,
            orderBy:listVue.orderBy,
            hasPush : listVue.hasPush,
        }
    }

    //列表操作--------------刷新
    function refresh()
    {
        // console.log(listVue.getSearchForm());
        new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj){
            if(jsonObj.result != "success")
            {
                noty({"text":jsonObj.info,"type":"error","timeout":2000});
            }
            else
            {
                // console.log(jsonObj.empj_BldEscrowCompletedList);
                listVue.empj_BldEscrowCompletedList=jsonObj.empj_BldEscrowCompletedList;
                listVue.empj_BldEscrowCompletedList.forEach(function(item){
                	item.allBuildingArea = addThousands(item.allBuildingArea);
                	item.allEscrowArea = addThousands(item.allEscrowArea);
                })
                listVue.pageNumber=jsonObj.pageNumber;
                listVue.countPerPage=jsonObj.countPerPage;
                listVue.totalPage=jsonObj.totalPage;
                listVue.totalCount = jsonObj.totalCount;
                listVue.keyword=jsonObj.keyword;
                listVue.selectedItem=[];
                listVue.selectModel=[];
                //动态跳转到锚点处，id="top"
                document.getElementById('empj_BldEscrowCompletedListDiv').scrollIntoView();
            }
        });
    }

    //按钮操作-----导出Excel
	function bldEscrowExportExcelHandle()
	{
        console.log("导出Excel-----");
        //这里需要重新写，以前是明细表操作，现在需要换成主表操作
        // var model = {
        //     interfaceVersion:listVue.interfaceVersion,
        //     keyword:this.keyword,
        //     theState:this.theState,
        //     busiState:this.busiState,
        //     // idArr:listVue.bldEscrow_DtlArr,
        // }
        // console.log(model);
        // new ServerInterface(baseInfo.bldEscrowExportExcelInterface).execute(model, function (jsonObj) {
        //     if (jsonObj.result != "success")
        //     {
        //         noty({ "text": jsonObj.info, "type": "error", "timeout": 2000 });
        //     }
        //     else
        //     {
        //         window.location.href=jsonObj.fileDownloadPath;
        //         listVue.selectItem = [];
        //         refresh();
        //     }
        // });
    }

    //按钮操作-----跳转到添加托管终止申请页面
	function bldEscrowAddHandle()
	{
        enterNewTab("", "新增托管终止", 'empj/Empj_BldEscrowCompletedAdd.shtml');
    }

    //按钮操作-----跳转到添加托管终止编辑页面
	function bldEscrowEditHandle()
	{
		if (listVue.selectItem.length == 1)
		{
            var theBldEscrowCompletedInfo = listVue.selectModel[0];
            if (theBldEscrowCompletedInfo.busiState != "已备案" && theBldEscrowCompletedInfo.approvalState != "审核中")
            {
                var tableId = listVue.selectItem[0];
                tableId = 'BldEscrowCompletedEdit_' + tableId;
                enterNewTab(tableId, "编辑托管终止", 'empj/Empj_BldEscrowCompletedEdit.shtml');
            }
            else
            {
                if (theBldEscrowCompletedInfo.busiState == "已备案")
                {
                    noty({"text":"您选择的托管终止已备案","type":"error","timeout":2000});
                }
                if (theBldEscrowCompletedInfo.approvalState == "审核中")
                {
                    noty({"text":"您选择的托管终止正在审批中","type":"error","timeout":2000});
                }
            }
		}
        else
        {
            noty({"text":"请选择一个且只选一个要修改的项目","type":"error","timeout":2000});
        }
    }

    //按钮操作--查看审批情况
    function bldEscrowApprovalCheck()
    {
        if (listVue.selectItem.length == 1)
        {
            var bldEscrowModel = listVue.selectModel[0];
            console.log(bldEscrowModel);
            if (bldEscrowModel.approvalState != "待提交")
            {
                approvalDetailFromHome(bldEscrowModel.busiType,bldEscrowModel.tableId,bldEscrowModel.afId,bldEscrowModel.workflowId,2);
            }
            else
            {
                noty({"text":"您选择的托管终止处于待提交状态","type":"error","timeout":2000});
            }
        }
        else
        {
            noty({"text":"请选择一个且只选一个要修改的托管终止","type":"error","timeout":2000});
        }
    }

	//列表操作-----跳转到托管终止详情页面
    function bldEscrowDetailHandle(tableId) {
        enterNewTab(tableId, "托管终止详情", 'empj/Empj_BldEscrowCompletedDetail.shtml');
        var theId = 'BldEscrowCompletedDetail_' + tableId;
        // $("#tabContainer").data("tabs").addTab({ id: theId, text: '托管终止详情', closeable: true, url: 'empj/Empj_BldEscrowCompletedDetail.shtml' });
    }

    //现在托管终止列表数据
    function handleSelectionChange(val)
    {
        listVue.selectItem = [];
        listVue.selectModel = [];
        for (var index = 0; index < val.length; index++) {
            var element = val[index].tableId;
            listVue.selectItem.push(element);
            listVue.selectModel = val;
        }
//		console.log(listVue.selectItem);

        listVue.enableEdit = true;
        listVue.enableChange = true;
        for (var index = 0; index < val.length; index++) {
            var itemModel = val[index];
            if (itemModel.busiState == "已备案" || itemModel.approvalState == "审核中")
            {
                listVue.enableEdit = false;
                break;
            }
        }
        
        for (var index = 0; index < val.length; index++) {
            var itemModel = val[index];
            /*if (itemModel.busiState != "已备案" || itemModel.hasPush == "1")
            {
                listVue.enableChange = false;
                break;
            }*/
            
            if (itemModel.hasPush == "1")
            {
                listVue.enableChange = false;
                break;
            }
        }
        
        if(val.length>0){
        	listVue.delDisabled = false;
        	for(var i = 0;i<val.length;i++){
        		if(!(val[i].approvalState == "待提交")){
        			listVue.delDisabled = true;
        			break;
        		}
        	}
        }else{
        	listVue.delDisabled = true;
        }
    }
    
    function delBldEscrowCompleted(){
    	generalSelectModal(confirmDelBld,"确认删除吗？");
    }
    
    function confirmDelBld(){
    	var model = {
    	    idArr : listVue.selectItem,
    	    interfaceVersion: listVue.interfaceVersion,
    	}
    	new ServerInterface(baseInfo.deleteInterface).execute(model, function (jsonObj) {
            if (jsonObj.result != "success") {
                generalErrorModal(jsonObj,jsonObj.info);
            } else {
            	generalSuccessModal();
            	 refresh();
            }
        });
    }
	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		listVue.isAllSelected = (listVue.empj_BldEscrowCompletedList.length > 0)
		&&	(listVue.selectItem.length == listVue.empj_BldEscrowCompletedList.length);
	}

    function getCompanyList()
    {
        serverRequest("../Emmp_CompanyInfoForSelect",getTotalListForm(),function (jsonObj) {
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
            }
        });

        listVue.refresh();
    }
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#empj_BldEscrowCompletedListDiv",
	"listInterface":"../Empj_BldEscrowCompletedList",
    "bldEscrowExportExcelInterface":"../Empj_BldEscrowCompletedExportExcel",
    "deleteInterface" : "../Empj_BldEscrowCompletedBatchDelete"
});
