(function (baseInfo) {
    var listVue = new Vue({
        el: baseInfo.listDivId,
        data: {
            interfaceVersion: 19000101,
            pageNumber: 1,
            countPerPage: 20,
            totalPage: 1,
            totalCount: 1,
            keyword: "",
            selectItem: [],
            isAllSelected: false,
            theState: 0,//正常为0，删除为1
            userStartId: null,
            userStartList: [],
            userRecordId: null,
            userRecordList: [],
            companyId: null,
            companyList: [],
            projectId: null,
            projectList: [],
            bankId: null,
            bankList: [],
            bankBranchId: null,
            bankBranchList: [],
            tgxy_BankAccountEscrowedList: [],
            isUsing:"",

            poName:"Tgxy_BankAccountEscrowed",

            bankBranchId:"",
            bankBranchList:[],

            orderBy:"",

        },
        methods: {
            refresh: refresh,
            initData: initData,
            getSearchForm: getSearchForm,
            getDeleteForm: getDeleteForm,
            mainDeleteHandle:mainDeleteHandle,
            search: search,
            checkAllClicked: checkAllClicked,
            mainDetailHandle: mainDetailHandle,
            mainAddHandle: mainAddHandle,
            mainEditHandle:mainEditHandle,
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
            checkCheckBox: checkCheckBox,
            indexMethod: indexMethod,
            showLog: showLog,
            listItemSelectHandle: listItemSelectHandle,
            bankDetailHandle:bankDetailHandle,
            resetSearch:resetSearch,
            mainExportExcelHandle:mainExportExcelHandle,

            changeCountPerPage :function (data) {
                if (listVue.countPerPage != data) {
                    listVue.countPerPage = data;
                    listVue.refresh();
                }
            },

            isUsingChange:isUsingChange,

            changeBankBranchListener:changeBankBranchListener,
            changeBankBranchEmpty:changeBankBranchEmpty,
            sortChange:sortChange,
        },
        computed: {},
        components: {
            'vue-listselect': VueListSelect,
            'vue-nav': PageNavigationVue
        },
        watch: {
            pageNumber: refresh,
            selectItem: selectedItemChanged,
        }
    });

    //------------------------方法定义-开始------------------//

    //列表操作--------------获取"搜索列表"表单参数
    function getSearchForm() {
        return {
            interfaceVersion: listVue.interfaceVersion,
            pageNumber: listVue.pageNumber,
            countPerPage: listVue.countPerPage,
            totalPage: listVue.totalPage,
            keyword: listVue.keyword,
            theState: listVue.theState,
            userStartId: listVue.userStartId,
            userRecordId: listVue.userRecordId,
            companyId: listVue.companyId,
            projectId: listVue.projectId,
            bankId: listVue.bankId,
            bankBranchId: listVue.bankBranchId,
            isUsing:listVue.isUsing,
            orderBy:listVue.orderBy,
        }
    }

    //列表操作--------------获取“删除资源”表单参数
    function getDeleteForm() {
        return {
            interfaceVersion: listVue.interfaceVersion,
            idArr: listVue.selectItem
        }
    }

    //选中状态有改变，需要更新“全选”按钮状态
    function selectedItemChanged() {
        listVue.isAllSelected = (listVue.tgxy_BankAccountEscrowedList.length > 0)
            && (listVue.selectItem.length == listVue.tgxy_BankAccountEscrowedList.length);
    }

    //列表操作--------------“全选”按钮被点击
    function checkAllClicked() {
        if (listVue.selectItem.length == listVue.tgxy_BankAccountEscrowedList.length) {
            listVue.selectItem = [];
        }
        else {
            listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
            listVue.tgxy_BankAccountEscrowedList.forEach(function (item) {
                listVue.selectItem.push(item.tableId);
            });
        }
    }

    function checkCheckBox(tableId) {
        listVue.selectItem = tableId;
    }

    //列表操作--------------刷新
    function refresh() {
        new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function (jsonObj) {
            if (jsonObj.result != "success") {
                noty({"text": jsonObj.info, "type": "error", "timeout": 2000});
            }
            else {
                listVue.tgxy_BankAccountEscrowedList = jsonObj.tgxy_BankAccountEscrowedList;
                listVue.pageNumber = jsonObj.pageNumber;
                listVue.countPerPage = jsonObj.countPerPage;
                listVue.totalPage = jsonObj.totalPage;
                listVue.totalCount = jsonObj.totalCount;
                listVue.keyword = jsonObj.keyword;
                listVue.selectItem = [];
                //动态跳转到锚点处，id="top"
                document.getElementById('tgxy_BankAccountEscrowedListDiv').scrollIntoView();
            }
        });
    }

    //列表操作------------搜索
    function search() {
        listVue.pageNumber = 1;
        refresh();
    }

    function mainEditHandle(tableId) {
        var list=listVue.selectItem
        enterDetail(list,"托管账号修改","tgxy/Tgxy_BankAccountEscrowedEdit.shtml")
    }
    
    function mainDetailHandle(tableId) {
        enterNewTab(tableId, '托管账户详情', 'tgxy/Tgxy_BankAccountEscrowedDetail.shtml')
    }

    function mainAddHandle() {
        enterNewTab("", '新增托管账户', 'tgxy/Tgxy_BankAccountEscrowedAdd.shtml')
    }

    function mainDeleteHandle() {
        var canDelete=true
        for(var i=0;i<listVue.selectItem.length;i++){
            var tableId=listVue.selectItem[i]
            for(var j=0;j<listVue.tgxy_BankAccountEscrowedList.length;j++){
                var item=listVue.tgxy_BankAccountEscrowedList[j]
                if(item.tableId==tableId){
                    // console.log('item is '+item+" isUsing is "+item.isUsing)
                    if(item.isUsing==0){
                        canDelete=false
                    }
                }
            }
        }
        console.log('canDelete is '+canDelete)
        if(canDelete){
            generalDeleteModal(listVue,listVue.poName)
        }else {
            generalErrorModal(undefined,"启用中的托管账户无法删除")
        }

    }

    function indexMethod(index) {
        return generalIndexMethod(index, listVue)
    }

    function listItemSelectHandle(list) {
        generalListItemSelectHandle(listVue,list)
    }

    function bankDetailHandle(tableId) {
        enterNewTab(tableId, '开户行详情', 'emmp/Emmp_BankBranchDetail.shtml')
    }

    function resetSearch() {
        generalResetSearch(listVue,function () {
            listVue.isUsing=""
            listVue.bankBranchId=""
            refresh()
        })
    }

    function mainExportExcelHandle() {
        generalExportExcel(listVue,listVue.poName,function () {
            refresh()
        })
    }

    function showLog() {
        // enterLogTab(listVue.poName)
    }

    function changeBankBranchListener(data) {
        if (listVue.bankBranchId != data.tableId) {
            listVue.bankBranchId = data.tableId
            // listVue.refresh()
        }
    }

    function changeBankBranchEmpty() {
        if (listVue.bankBranchId != null) {
            listVue.bankBranchId = null
            // listVue.refresh()
        }
    }

    function sortChange(column) {
        listVue.orderBy=generalOrderByColumn(column);
        var orderStr = listVue.orderBy;
        if(orderStr.indexOf("bankBranchShortName desc") != -1)
        {
        	listVue.orderBy = "shortNameOfBank desc";
        }
        if(orderStr.indexOf("bankBranchShortName asc") != -1)
        {
        	listVue.orderBy = "shortNameOfBank asc";
        }
        if(orderStr.indexOf("bankBranchName desc") != -1)
        {
        	listVue.orderBy = "theNameOfBank desc";
        }
        if(orderStr.indexOf("bankBranchName asc") != -1)
        {
        	listVue.orderBy = "theNameOfBank asc";
        }
        refresh()
    }

    function isUsingChange() {
        // refresh()
    }

    function getBankBranchList() {
        serverRequest("../Emmp_BankBranchForSelect",getTotalListForm(),function (jsonObj) {
            listVue.bankBranchList=jsonObj.emmp_BankBranchList
        })
    }
    function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}
    function initData() {
    	initButtonList();
        getBankBranchList()
        refresh()
    }

    //------------------------方法定义-结束------------------//

    //------------------------数据初始化-开始----------------//
    // listVue.refresh();
    listVue.initData();
    //------------------------数据初始化-结束----------------//
})({
    "listDivId": "#tgxy_BankAccountEscrowedListDiv",
    "updateDivId": "#updateModel",
    "addDivId": "#addModal",
    "listInterface": "../Tgxy_BankAccountEscrowedList",
    "addInterface": "../Tgxy_BankAccountEscrowedAdd",
    "deleteInterface": "../Tgxy_BankAccountEscrowedDelete",
    "batchDeleteInterface": "../Tgxy_BankAccountEscrowedBatchDelete",
    "updateInterface": "../Tgxy_BankAccountEscrowedUpdate"
});
