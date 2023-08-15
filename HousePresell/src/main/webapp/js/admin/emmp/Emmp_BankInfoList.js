(function (baseInfo) {
    var listVue = new Vue({
        el: baseInfo.listDivId,
        data: {
            poName: "Emmp_BankInfo",
            interfaceVersion: 19000101,
            pageNumber: 1,
            countPerPage: 8,
            totalPage: 1,
            totalCount: 1,
            keyword: "",
            selectItem: [],
            theState: 0,//正常为0，删除为1
            userStartId: null,
            userStartList: [],
            userRecordId: null,
            userRecordList: [],
            bankId: null,
            bankList: [],
            emmp_BankInfoList: [],
            orderBy: "",

            diabaleShowLog: true,
        },
        methods: {
            refresh: refresh,
            initData: initData,
            indexMethod: indexMethod,
            showLog: showLog,
            getSearchForm: getSearchForm,
            getDeleteForm: getDeleteForm,
            bankInfoEditHandle: bankInfoEditHandle,
            bankInfoDeleteHandle: bankInfoDeleteHandle,
            search: search,
            dateFormat: dateFormat,
            bankInfoAddHandle: bankInfoAddHandle,
            bankInfoDetailHandle: bankInfoDetailHandle,
            bankInfoExportExcelHandle: bankInfoExportExcelHandle,
            changePageNumber: function (data) {
                listVue.pageNumber = data;
            },
            resetSearch: resetSearch,
            changeCountPerPage: function (data) {
                if (listVue.countPerPage != data) {
                    listVue.countPerPage = data;
                    listVue.refresh();
                }
            },
            listItemSelectHandle: listItemSelectHandle,
            sortChange: sortChange,
        },
        computed: {},
        components: {
            'vue-nav': PageNavigationVue
        },
        watch: {
            pageNumber: refresh,
        }
    });

    //------------------------方法定义-开始------------------//
    function dateFormat(row, column) {
        var date = row[column.property];
        if (date == undefined) {
            return "";
        }
        return moment(date).format("YYYY-MM-DD");
    }

    //列表操作--------------获取"搜索列表"表单参数
    function getSearchForm() {
        return {
            interfaceVersion: this.interfaceVersion,
            pageNumber: this.pageNumber,
            countPerPage: this.countPerPage,
            totalPage: this.totalPage,
            keyword: this.keyword,
            theState: this.theState,
            userStartId: this.userStartId,
            userRecordId: this.userRecordId,
            bankId: this.bankId,
            orderBy: this.orderBy
        }
    }

    //列表操作--------------获取“删除资源”表单参数
    function getDeleteForm() {
        return {
            interfaceVersion: this.interfaceVersion,
            idArr: this.selectItem
        }
    }

    function bankInfoEditHandle() {
        enterDetail(listVue.selectItem, "金融机构详情修改", 'emmp/Emmp_BankInfoEdit.shtml')
    }

    function bankInfoDeleteHandle() {
        generalDeleteModal(listVue, listVue.poName)
    }

    function bankInfoExportExcelHandle() {
        generalExportExcel(listVue, listVue.poName, function () {
            refresh()
        })
    }

    //列表操作--------------刷新
    function refresh() {
        new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function (jsonObj) {
            if (jsonObj.result != "success") {
                noty({"text": jsonObj.info, "type": "error", "timeout": 2000});
            }
            else {
                console.log(jsonObj);
                listVue.emmp_BankInfoList = jsonObj.emmp_BankInfoList;
                listVue.pageNumber = jsonObj.pageNumber;
                listVue.countPerPage = jsonObj.countPerPage;
                listVue.totalPage = jsonObj.totalPage;
                listVue.totalCount = jsonObj.totalCount;
                listVue.keyword = jsonObj.keyword;
                listVue.selectedItem = [];
                //动态跳转到锚点处，id="top"
                document.getElementById('emmp_BankInfoListDiv').scrollIntoView();
            }
        });
    }

    //列表操作------------搜索
    function search() {
        listVue.pageNumber = 1;
        refresh();
    }

    function bankInfoAddHandle() {
        enterNewTab("", "新增金融机构信息", 'emmp/Emmp_BankInfoAdd.shtml')

    }

    function bankInfoDetailHandle(tableId) {
        enterNewTab(tableId, '金融机构详情', 'emmp/Emmp_BankInfoDetail.shtml')
    }

    function resetSearch() {
        generalResetSearch(listVue, function () {
            refresh()
        })
    }

    function listItemSelectHandle(list) {
        generalListItemSelectHandle(listVue, list)
    }

    function indexMethod(index) {
        return generalIndexMethod(index, listVue)
    }

    function showLog() {
        // enterLogTab(listVue.poName)
        enterLogTabWithTemplate(listVue.poName, listVue)
    }

    function sortChange(column) {
        listVue.orderBy = generalOrderByColumn(column)
        refresh()
    }

    //根据按钮权限，动态控制，该页面的所有“可控按钮”的显示与否
    function initButtonList() {
        //封装在BaseJs中，每个页面需要控制按钮的就要
        getButtonList();
    }

    function initData() {
        initButtonList();
    }

    //------------------------方法定义-结束------------------//

    //------------------------数据初始化-开始----------------//
    listVue.refresh();
    listVue.initData();
    //------------------------数据初始化-结束----------------//
})({
    "listDivId": "#emmp_BankInfoListDiv",
    "updateDivId": "#updateModel",
    "addDivId": "#addModal",
    "listInterface": "../Emmp_BankInfoList",
    "addInterface": "../Emmp_BankInfoAdd",
    "batchDeleteInterface": "../Emmp_BankInfoBatchDelete",
    "exportExcelInterface": "../Emmp_BankInfoExportExcel",
    "deleteInterface": "../Emmp_BankInfoDelete",
    "updateInterface": "../Emmp_BankInfoUpdate"
});
