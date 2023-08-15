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
            userOperateId: null,
            userOperateList: [],
            sm_BusiState_LogList: [],
            poName: "Sm_BusiState_Log",//本页面的po名字
            thePoName: "",//传进来的Po的名字
            selectLogDetail: {
                updateUserName:"",
                updateTimeString:"",
            },
            differList: [],
            changeUserName:"",
            changeTimeString:"",
            packagePath:"",
            selectId:"",

            sourceId:"",//PO列表中被选中的那个po的tableId
        },
        methods: {
            refresh: refresh,
            initData: initData,
            indexMethod: indexMethod,
            getDeleteForm: getDeleteForm,
            listItemSelectHandle: listItemSelectHandle,
            search: search,
            changePageNumber: function (data) {
                listVue.pageNumber = data;
            },
            changeCountPerPage: function (data) {
                if (listVue.countPerPage != data) {
                    listVue.countPerPage = data;
                    listVue.refresh();
                }
            },
            resetSearch:resetSearch,
            getLogDetail: getLogDetail,
        },
        computed: {},
        components: {
            'vue-nav': PageNavigationVue
        },
        watch: {
            // pageNumber: refresh,
            selectItem: selectedItemChanged,
        }
    });

    //------------------------方法定义-开始------------------//

    //列表操作--------------获取"搜索列表"表单参数
    // function getSearchForm()
    // {
    // 	return {
    // 		interfaceVersion:this.interfaceVersion,
    // 		pageNumber:this.pageNumber,
    // 		countPerPage:this.countPerPage,
    // 		totalPage:this.totalPage,
    // 		keyword:this.keyword,
    // 		theState:this.theState,
    // 		userOperateId:this.userOperateId,
    // 	}
    // }
    //列表操作--------------获取“删除资源”表单参数
    function getDeleteForm() {
        return {
            interfaceVersion: this.interfaceVersion,
            idArr: this.selectItem
        }
    }

    //选中状态有改变，需要更新“全选”按钮状态
    function selectedItemChanged() {
        listVue.isAllSelected = (listVue.sm_BusiState_LogList.length > 0)
            && (listVue.selectItem.length == listVue.sm_BusiState_LogList.length);
    }

    //列表操作--------------刷新
    function refresh() {
        var form = {
            interfaceVersion: listVue.interfaceVersion,
            theState: 0,
            sourceType: listVue.thePoName,
            keyword:listVue.keyword,
            sourceId:listVue.sourceId,
        }
        serverRequest(baseInfo.listInterface, form, function (jsonObj) {
            listVue.sm_BusiState_LogList = jsonObj.sm_BusiState_LogList;
            listVue.pageNumber = jsonObj.pageNumber;
            listVue.countPerPage = jsonObj.countPerPage;
            listVue.totalPage = jsonObj.totalPage;
            listVue.totalCount = jsonObj.totalCount;
            listVue.keyword = jsonObj.keyword;
            listVue.selectItem = [];
            //动态跳转到锚点处，id="top"
            document.getElementById('sm_BusiState_LogListDiv').scrollIntoView();
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
        generalListItemSelectHandle(listVue, list)
    }

    function getLogDetail(data) {
        // enterLogTab(listVue.thePoName)
        listVue.selectId=data.tableId
        var concatId=listVue.selectId+"-"+listVue.packagePath
        console.log('newId is '+concatId)
        parent.enterNewTabTest(concatId,"变更日志详情","sm/Sm_BusiState_LogDetail.shtml")


        // console.log('getLogDetail data is')
        // console.log(data)
        // listVue.selectLogDetail = data
        // listVue.changeUserName=data.updateUserName
        // listVue.changeTimeString=data.updateTimeString
        // console.log('changeUserName is '+listVue.changeUserName)
        // console.log('changeTimeString is '+listVue.changeTimeString)
        // var form = {
        //     interfaceVersion: listVue.interfaceVersion,
        //     theState: 0,
        //     sourceType: listVue.thePoName,
        //     tableId: data.tableId,
        //     packagePath:listVue.packagePath
        // }
        // serverRequest(baseInfo.detailInterface, form, function (jsonObj) {
        //     listVue.differList = jsonObj.differ
        //     $("#logDetail").modal('show', {
        //         backdrop: 'static'
        //     });
        // })
    }

    function resetSearch() {
        generalResetSearch(listVue, function () {
            refresh()
        })
    }

    function initData() {
        listVue.thePoName = parent.getTabElementUI_TableId();
        if(listVue.thePoName.indexOf("-")!=-1){
            var poArray=listVue.thePoName.split("-")
            listVue.thePoName=poArray[0]
            listVue.packagePath=poArray[1]
            listVue.sourceId=poArray[2]
        }
        refresh();
    }

    //------------------------方法定义-结束------------------//

    //------------------------数据初始化-开始----------------//
    // listVue.refresh();
    listVue.initData();
    //------------------------数据初始化-结束----------------//
})({
    "listDivId": "#sm_BusiState_LogListDiv",
    "updateDivId": "#updateModel",
    "addDivId": "#addModal",
    "listInterface": "../../Sm_BusiState_LogList",
    "detailInterface": "../../Sm_BusiState_LogDetail",
    "addInterface": "../../Sm_BusiState_LogAdd",
    "deleteInterface": "../../Sm_BusiState_LogDelete",
    "updateInterface": "../../Sm_BusiState_LogUpdate"
});
