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
            theType: "21",
            busiState: "",
            approvalState: "",
            selectItem: [],
            theState: 0,//正常为0，删除为1
            emmp_CompanyCooperationList: [],
            orderBy : "",

            //提示框
            successMessage: "",
            errorMessage: "",
            remindMessage: "",
            selectMessage: "",
            selectMethod: "",
            //修改按钮
            upDisabled:true,
            //删除按钮
            delDisabled:true,
            diabaleShowLog: true,
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
            CompanyCooperationAddHandle: CompanyCooperationAddHandle,
            CompanyCooperationEditHandle: CompanyCooperationEditHandle,
            CompanyCooperationDeleteHandle: CompanyCooperationDeleteHandle,
            CompanyCooperationDel: CompanyCooperationDel,
            CompanyCooperationDelOne: CompanyCooperationDelOne,
            CompanyCooperationDetailHandle: CompanyCooperationDetailHandle,
            CompanyCooperationExportExcelHandle: CompanyCooperationExportExcelHandle,
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
            test: function () {
                console.log("test");
            },
            toggleSelection: toggleSelection,
            handleSelectionChange: handleSelectionChange,
            indexMethod: function (index) {
                if (listVue.pageNumber > 1) {
					return (listVue.pageNumber - 1) * listVue.countPerPage + (index - 0 + 1);
                }
                if (listVue.pageNumber <= 1) {
                    return index - 0 + 1;
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
        console.log(listVue.selectItem);
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

            // userStartId:this.userStartId,
            // userRecordId:this.userRecordId,
            // financialAccountId:this.financialAccountId,
            // cityRegionId:this.cityRegionId,
            // streetId:this.streetId,
        }
    }
    //列表操作--------------获取“删除资源”表单参数
    function getDeleteForm() {
        return {
            interfaceVersion: this.interfaceVersion,
            idArr: listVue.selectItem
        }
    }


    function CompanyCooperationEditHandle() {
        if(listVue.selectItem.length == 1)
        {
            enterDetail(listVue.selectItem, '合作机构修改', 'emmp/Emmp_CompanyCooperationEdit.shtml');
        }
        else
        {
            listVue.remindMessage = "请选择一个且只选一个要修改的机构";
            $(baseInfo.remindM).modal('show', {
                backdrop :'static'
            });
        }
    }

    function CompanyCooperationDeleteHandle() {
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
            listVue.selectMethod = "CompanyCooperationDel";
            $(baseInfo.selectM).modal('show', {
                backdrop :'static'
            });
        }
        else
        {
            listVue.selectMessage = "确认删除吗？";
            listVue.selectMethod = "CompanyCooperationDelOne";
            $(baseInfo.selectM).modal('show', {
                backdrop :'static'
            });
        }
    }

    function CompanyCooperationDel() {
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

    function CompanyCooperationDelOne() {
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

    function CompanyCooperationExportExcelHandle()
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
    function refresh() {

        new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function (jsonObj) {
            if (jsonObj.result != "success")
            {
                listVue.errorMessage = jsonObj.info;
                $(baseInfo.errorM).modal('show', {
                    backdrop :'static'
                });
            }
            else {
                listVue.emmp_CompanyCooperationList = jsonObj.emmp_CompanyInfoList;
                listVue.pageNumber = jsonObj.pageNumber;
                listVue.countPerPage = jsonObj.countPerPage;
                listVue.totalPage = jsonObj.totalPage;
                listVue.totalCount = jsonObj.totalCount;
                listVue.keyword = jsonObj.keyword;
                listVue.selectItem = [];
                //动态跳转到锚点处，id="top"
                document.getElementById('emmp_CompanyCooperationListDiv').scrollIntoView();
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

    function SortChange(column, prop, order) {
//     	console.log(column + '-' + column.prop + '-' + column.order)

        refresh();
    }

    //跳转方法
    function CompanyCooperationAddHandle() {
        enterNewTab('', '新增合作机构', 'emmp/Emmp_CompanyCooperationAdd.shtml');
    }

    function CompanyCooperationDetailHandle(tableId) {
        enterNewTab(tableId, '合作机构详情', 'emmp/Emmp_CompanyCooperationDetail.shtml');
    }
  //根据按钮权限，动态控制，该页面的所有“可控按钮”的显示与否
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}

    function showLog() {
        enterLogTabWithTemplate("Emmp_CompanyInfo", listVue)
    }

    function initData() {
        //获取登录者
        /*getLoginSm_User();*/
        initButtonList();
        refresh();
    }

    //------------------------方法定义-结束------------------//

    //------------------------数据初始化-开始----------------//
    listVue.initData();
    //------------------------数据初始化-结束----------------//
})({
    "listDivId": "#emmp_CompanyCooperationListDiv",
    "listInterface": "../Emmp_CompanyCooperationList",
    "deleteInterface": "../Emmp_CompanyCooperationDelete",
    "batchDeleteInterface": "../Emmp_CompanyCooperationBatchDelete",
    "exportExcelInterface": "../Emmp_CompanyCooperationExportExcel",

    //其他
    "successM": "#companyCooperationList_successM",
    "errorM": "#companyCooperationList_errorM",
    "remindM": "#companyCooperationList_remindM",
    "selectM": "#companyCooperationList_selectM"
});
