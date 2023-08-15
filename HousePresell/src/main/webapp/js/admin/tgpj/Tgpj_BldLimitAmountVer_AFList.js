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
            tgpj_BldLimitAmountVer_AFList: [],
            poName: "Tgpj_BldLimitAmountVer_AF",
            isUsing: "",

            upDisabled:"",
            deleteDisabled:"",
            approvalState:"",

            orderBy:"",
            approvalStateList : [
            	{tableId:"待提交",theName:"待提交"},
            	{tableId:"审核中",theName:"审核中"},
            	{tableId:"已完结",theName:"已完结"},
            ]
        },
        methods: {
            refresh: refresh,
            initData: initData,
            indexMethod: indexMethod,
            showLog: showLog,
            getSearchForm: getSearchForm,
            getDeleteForm: getDeleteForm,
            listItemSelectHandle: listItemSelectHandle,
            search: search,
            checkAllClicked: checkAllClicked,
            changePageNumber: function (data) {
                listVue.pageNumber = data;
            },
            mainDetailHandle: mainDetailHandle,
            mainAddHandle: mainAddHandle,
            mainEditHandle: mainEditHandle,
            mainDeleteHandle: mainDeleteHandle,

            resetSearch: resetSearch,
            mainExportExcelHandle: mainExportExcelHandle,

            changeCountPerPage: function (data) {
                if (listVue.countPerPage != data) {
                    listVue.countPerPage = data;
                    listVue.refresh();
                }
            },
            sortChange:sortChange,
            changeApprovalState : function(data){
            	this.approvalState = data.tableId;
            },
            approvalStateEmpty : function(){
            	this.approvalState = null;
            }
        },
        computed: {},
        components: {
            'vue-nav': PageNavigationVue,
            'vue-listselect' : VueListSelect,
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
            isUsing: listVue.isUsing,
            approvalState:listVue.approvalState,
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
        listVue.isAllSelected = (listVue.tgpj_BldLimitAmountVer_AFList.length > 0)
            && (listVue.selectItem.length == listVue.tgpj_BldLimitAmountVer_AFList.length);
    }

    //列表操作--------------“全选”按钮被点击
    function checkAllClicked() {
        if (listVue.selectItem.length == listVue.tgpj_BldLimitAmountVer_AFList.length) {
            listVue.selectItem = [];
        }
        else {
            listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
            listVue.tgpj_BldLimitAmountVer_AFList.forEach(function (item) {
                listVue.selectItem.push(item.tableId);
            });
        }
    }

    //列表操作--------------刷新
    function refresh() {
        serverRequest(baseInfo.listInterface, listVue.getSearchForm(), function (jsonObj) {
            listVue.tgpj_BldLimitAmountVer_AFList = jsonObj.tgpj_BldLimitAmountVer_AFList;
            listVue.pageNumber = jsonObj.pageNumber;
            listVue.countPerPage = jsonObj.countPerPage;
            listVue.totalPage = jsonObj.totalPage;
            listVue.totalCount = jsonObj.totalCount;
            listVue.keyword = jsonObj.keyword;
            listVue.selectedItem = [];
            //动态跳转到锚点处，id="top"
            document.getElementById('tgpj_BldLimitAmountVer_AFListDiv').scrollIntoView();
        })
    }

    //列表操作------------搜索
    function search() {
        listVue.pageNumber = 1;
        refresh();
    }

    function indexMethod(index) {
        return generalIndexMethod(index, listVue)
    }

    function listItemSelectHandle(list) {
        generalListItemSelectWholeItemHandle(listVue,list)
        var list=listVue.selectItem
        if(list.length>1 || list[0]==undefined){
            listVue.upDisabled=true
        }else{
            console.log('list is ')
            console.log(list)
            console.log('list[0] is ')
            console.log(list[0])
            if(list[0].approvalState=="待提交"){
                listVue.upDisabled=false
            }else{
                listVue.upDisabled=true
            }
        }
        if(list.length==0){
            listVue.deleteDisabled=true
        }else{
            var isDisable=false
            for(var i=0;i<list.length;i++){
                var item = list[i]
                if(item.approvalState!="待提交"){
                    isDisable=true
                    break
                }
            }
            listVue.deleteDisabled=isDisable
        }
    }

    function mainAddHandle() {
        enterNewTab("", "受限额度节点增加", "tgpj/Tgpj_BldLimitAmountVer_AFAdd.shtml")
    }

    function mainDetailHandle(tableId) {
        enterNewTab(tableId, "受限额度节点详情", "tgpj/Tgpj_BldLimitAmountVer_AFDetail.shtml")
    }

    function mainEditHandle() {
        generalListItemSelectHandle(listVue,listVue.selectItem)
        var list = listVue.selectItem
        enterDetail(list, "受限额度节点修改", "tgpj/Tgpj_BldLimitAmountVer_AFEdit.shtml")
    }

    function mainDeleteHandle() {
        generalListItemSelectHandle(listVue,listVue.selectItem)
        generalDeleteModal(listVue, listVue.poName)
    }

    function resetSearch() {
        listVue.approvalState=""
        generalResetSearch(listVue, function () {
            refresh()
        })
    }

    function mainExportExcelHandle() {
        generalExportExcel(listVue, listVue.poName, function () {
            refresh()
        })
    }

    function showLog() {
        enterLogTab(listVue.poName)
    }

    function sortChange(column) {
        listVue.orderBy=generalOrderByColumn(column)
        refresh()
    }
  //根据按钮权限，动态控制，该页面的所有“可控按钮”的显示与否
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}
    function initData() {
    	initButtonList();
        refresh()
    }

    //------------------------方法定义-结束------------------//

    //------------------------数据初始化-开始----------------//
    // listVue.refresh();
    listVue.initData();
    //------------------------数据初始化-结束----------------//
})({
    "listDivId": "#tgpj_BldLimitAmountVer_AFListDiv",
    "updateDivId": "#updateModel",
    "addDivId": "#addModal",
    "listInterface": "../Tgpj_BldLimitAmountVer_AFList",
    "addInterface": "../Tgpj_BldLimitAmountVer_AFAdd",
    "deleteInterface": "../Tgpj_BldLimitAmountVer_AFDelete",
    "batchDeleteInterface": "../Tgpj_BldLimitAmountVer_AFBatchDelete",
    "updateInterface": "../Tgpj_BldLimitAmountVer_AFUpdate"
});
