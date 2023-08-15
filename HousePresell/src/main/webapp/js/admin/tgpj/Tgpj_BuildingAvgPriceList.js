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
            buildingInfoId: null,
            buildingInfoList: [],
            tgpj_BuildingAvgPriceList: [],
            poName: "Tgpj_BuildingAvgPrice",
            approvalState: "",

            enableDelete: false,
            companyId: "",
            companyName: "",
            companyList: [],
            projectId: "",
            projectList: [],

            orderBy:"",

            isNormalUser:"",
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
            listItemSelectHandle: listItemSelectHandle,
            showAjaxModal: showAjaxModal,
            search: search,
            checkAllClicked: checkAllClicked,
            changePageNumber: function (data) {
                listVue.pageNumber = data;
            },
            mainAddHandle: mainAddHandle,
            mainEditHandle: mainEditHandle,
            mainDetailHandle: mainDetailHandle,
            companyDetailHandle: companyDetailHandle,
            projectDetailHandle: projectDetailHandle,
            mainDeleteHandle: mainDeleteHandle,
            resetSearch: resetSearch,
            mainExportExcelHandle: mainExportExcelHandle,

            changeCountPerPage: function (data) {
                if (listVue.countPerPage != data) {
                    listVue.countPerPage = data;
                    listVue.refresh();
                }
            },

            changeCompanyListener: changeCompanyListener,
            changeCompanyEmpty: changeCompanyEmpty,
            changeprojectListener: changeprojectListener,
            changeProjectEmpty: changeProjectEmpty,
            sortChange:sortChange,
            changeApprovalState: function(data){
            	listVue.approvalState = data.tableId;
            },
            changeApprovalStateEmpty : function(){
            	listVue.approvalState = null;
            }
        },
        computed: {},
        components: {
            'vue-nav': PageNavigationVue,
            'vue-listselect': VueListSelect
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
            interfaceVersion: this.interfaceVersion,
            pageNumber: this.pageNumber,
            countPerPage: this.countPerPage,
            totalPage: this.totalPage,
            keyword: this.keyword,
            theState: this.theState,
            userStartId: this.userStartId,
            userRecordId: this.userRecordId,
            buildingInfoId: this.buildingInfoId,
            companyId:listVue.companyId,
            projectId:listVue.projectId,
            approvalState: listVue.approvalState,
            orderBy:listVue.orderBy,
        }
    }

    //选中状态有改变，需要更新“全选”按钮状态
    function selectedItemChanged() {
        listVue.isAllSelected = (listVue.tgpj_BuildingAvgPriceList.length > 0)
            && (listVue.selectItem.length == listVue.tgpj_BuildingAvgPriceList.length);
    }

    //列表操作--------------“全选”按钮被点击
    function checkAllClicked() {
        if (listVue.selectItem.length == listVue.tgpj_BuildingAvgPriceList.length) {
            listVue.selectItem = [];
        }
        else {
            listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
            listVue.tgpj_BuildingAvgPriceList.forEach(function (item) {
                listVue.selectItem.push(item.tableId);
            });
        }
    }

    //列表操作--------------刷新
    function refresh() {
        serverRequest(baseInfo.listInterface, listVue.getSearchForm(), function (jsonObj) {
            listVue.tgpj_BuildingAvgPriceList = jsonObj.tgpj_BuildingAvgPriceList;
            listVue.pageNumber = jsonObj.pageNumber;
            listVue.countPerPage = jsonObj.countPerPage;
            listVue.totalPage = jsonObj.totalPage;
            listVue.totalCount = jsonObj.totalCount;
            listVue.keyword = jsonObj.keyword;
            listVue.selectedItem = [];

            //动态跳转到锚点处，id="top"
            document.getElementById('tgpj_BuildingAvgPriceListDiv').scrollIntoView();
        })
    }

    //列表操作------------搜索
    function search() {
        listVue.pageNumber = 1;
        refresh();
    }

    //弹出编辑模态框--更新操作
    function showAjaxModal(tgpj_BuildingAvgPriceModel) {
        //tgpj_BuildingAvgPriceModel数据库的日期类型参数，会导到网络请求失败
        //Vue.set(updateVue, 'tgpj_BuildingAvgPrice', tgpj_BuildingAvgPriceModel);
        //updateVue.$set("tgpj_BuildingAvgPrice", tgpj_BuildingAvgPriceModel);

        updateVue.theState = tgpj_BuildingAvgPriceModel.theState;
        updateVue.busiState = tgpj_BuildingAvgPriceModel.busiState;
        updateVue.eCode = tgpj_BuildingAvgPriceModel.eCode;
        updateVue.userStartId = tgpj_BuildingAvgPriceModel.userStartId;
        updateVue.createTimeStamp = tgpj_BuildingAvgPriceModel.createTimeStamp;
        updateVue.lastUpdateTimeStamp = tgpj_BuildingAvgPriceModel.lastUpdateTimeStamp;
        updateVue.userRecordId = tgpj_BuildingAvgPriceModel.userRecordId;
        updateVue.recordTimeStamp = tgpj_BuildingAvgPriceModel.recordTimeStamp;
        updateVue.recordAveragePrice = tgpj_BuildingAvgPriceModel.recordAveragePrice;
        updateVue.buildingInfoId = tgpj_BuildingAvgPriceModel.buildingInfoId;
        updateVue.averagePriceRecordDate = tgpj_BuildingAvgPriceModel.averagePriceRecordDate;
        updateVue.recordAveragePriceFromPresellSystem = tgpj_BuildingAvgPriceModel.recordAveragePriceFromPresellSystem;
        $(baseInfo.updateDivId).modal('show', {
            backdrop: 'static'
        });
    }

    function indexMethod(index) {
        return generalIndexMethod(index, listVue)
    }

    function listItemSelectHandle(val) {
    	
    	if(val.length == 1)
		{
			listVue.approvalState = val[0].approvalState;
		}
    	
        generalListItemSelectHandle(listVue, val);
        
        console.log(val);
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

    function mainAddHandle() {
        enterNewTab("", "楼幢住宅备案均价增加", "tgpj/Tgpj_BuildingAvgPriceAdd.shtml")
    }

    function mainEditHandle() {
        var list = listVue.selectItem
        enterDetail(list, "备案均价修改", "tgpj/Tgpj_BuildingAvgPriceEdit.shtml")
    }

    function mainDetailHandle(tableId) {
        enterNewTab(tableId, "备案均价详情", "tgpj/Tgpj_BuildingAvgPriceDetail.shtml")
    }

    function companyDetailHandle(tableId) {
        enterNewTab(tableId, "机构详情", "emmp/Emmp_CompanyInfoDetail.shtml")
    }

    function projectDetailHandle(tableId) {
        enterNewTab(tableId, "项目详情", "empj/Empj_ProjectInfoDetail.shtml")
    }

    function mainDeleteHandle() {
        generalDeleteModal(listVue, listVue.poName)
    }

    function resetSearch() {
        generalResetSearch(listVue, function () {
            listVue.companyId = ""
            listVue.projectId = ""
            listVue.approvalState = ""
            refresh()
        })
    }

    function mainExportExcelHandle() {
        generalExportExcel(listVue, listVue.poName, function () {
            refresh()
        })
    }

    function showLog() {
        // enterLogTab(listVue.poName,undefined)
    }

    function changeCompanyListener(data) {
        if (listVue.companyId != data.tableId) {
            listVue.companyId = data.tableId
            listVue.projectId=""
            // listVue.refresh()
            getProjectList()
        }
    }

    function changeCompanyEmpty() {
        if (listVue.companyId != null) {
            listVue.companyId = null
            listVue.projectId=""
            // listVue.refresh()
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

    function getCompanyList() {
        var form=getTotalListForm()
        form["theType"]=1
        form["exceptZhengTai"]=true
        serverRequest("../Emmp_CompanyInfoForSelect",form,function (jsonObj) {
            listVue.companyList=jsonObj.emmp_CompanyInfoList
        })
    }

    function getProjectList() {
        var form=getTotalListForm()
        // if(listVue.isNormalUser==2){
            form["developCompanyId"]=listVue.companyId
        // }
        serverRequest("../Empj_ProjectInfoForSelect",form,function (jsonObj) {
            listVue.projectList=jsonObj.empj_ProjectInfoList
        })
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
        getCompanyList();
        // getProjectList();
        getLoginUserInfo(function (user) {
            listVue.user = user;
            var comType = listVue.user.developCompanyType;
            
            /**
             * xsz by time 2019-1-25 18:00:12
             * 此处直接用用户类型判断是否是正泰用户是不合理的，
             * 这样做导致了监理公司、合作机构等用户也被当做一般用户处理，造成数据查询问题，
             * 应该改为用用户所属开发企业类型判断
             */
            
            if(comType == '1')
            {//开发企业
            	listVue.companyId = user.developCompanyId;
                listVue.companyName = user.theNameOfCompany;
                console.log('companyId is '+listVue.companyId+" companyName is "+listVue.companyName);
                listVue.isNormalUser=2;
            }
            else
            {
            	listVue.isNormalUser=1
                listVue.companyId =""
            }
            
            // getUserDetail()
//            if(listVue.user.theType=="1"){//如果是正泰用户
//                listVue.isNormalUser=1
//                listVue.companyId =""
//            }else{//如果是一般用户
//                listVue.isNormalUser=2
//            }
            getProjectList()
        })
        getIdFormTab("",function (id) {
            // console.log('id is '+id)
            if(id.indexOf("BldTable")!=-1){
                var params=id.split("-")
                var projectId=params[1]
                // console.log('projectId is '+projectId)
                listVue.projectId=Number(projectId)
                listVue.companyId=Number(params[2])
                var buildingId=params[3]
                serverRequest(baseInfo.detailInterface,{interfaceVersion:19000101,tableId:buildingId},function (jsonObj) {
                    listVue.keyword=jsonObj.empj_BuildingInfo.eCodeFromConstruction
                    listVue.refresh();
                })
            }else{
                listVue.refresh();
            }
        })

    }

    //------------------------方法定义-结束------------------//

    //------------------------数据初始化-开始----------------//
    listVue.initData();
    //------------------------数据初始化-结束----------------//
})({
    "listDivId": "#tgpj_BuildingAvgPriceListDiv",
    "updateDivId": "#updateModel",
    "addDivId": "#addModal",
    "listInterface": "../Tgpj_BuildingAvgPriceList",
    "detailInterface": "../Empj_BuildingInfoDetail",
    "addInterface": "../Tgpj_BuildingAvgPriceAdd",
    "deleteInterface": "../Tgpj_BuildingAvgPriceDelete",
    "batchDeleteInterface": "../Tgpj_BuildingAvgPriceBatchDelete",
    "updateInterface": "../Tgpj_BuildingAvgPriceUpdate"
});
