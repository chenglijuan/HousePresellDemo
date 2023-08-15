(function (baseInfo) {
    var listVue = new Vue({
        el: baseInfo.listDivId,
        data: {
            poName: "Empj_ProjProgInspection",
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
            developCompanyId: null,
            developCompanyList: [],
            buildingId: null,
            buildingList: [],
            empj_ProjProgInspectionList: [],
            //修改按钮
            upDisabled: true,

            approvalState: "",

            companyId: "",
            companyList: [],
            projectId: "",
            projectList: [],

            orderBy: "",
            approvalDisabled: true,//审批按钮
            busiType: "受限额度变更",
            afId: "",
            workflowId: "",
            sourceId: "",

            isNormalUser: "",
            delDisabled : true,
            exDisabled : true,
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
            showAjaxModal: showAjaxModal,
            search: search,
            checkAllClicked: checkAllClicked,
            changePageNumber: function (data) {
                listVue.pageNumber = data;
            },
            mainDetailHandle: mainDetailHandle,
            mainAddHandle: mainAddHandle,
            mainEditHandle: mainEditHandle,
            mainDeleteHandle: mainDeleteHandle,

            companyDetailHandle: companyDetailHandle,
            projectDetailHandle: projectDetailHandle,
            buildingDetailHandle: buildingDetailHandle,

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
            sortChange: sortChange,
            approvalDetail: approvalDetail,//审批详情
            delBldLimitAmount : delBldLimitAmount,
            changeApprovalState: function(data){
            	listVue.approvalState = data.tableId;
            },
            changeApprovalStateEmpty : function(){
            	listVue.approvalState = null;
            },
            exportFun:exportFun,//导出
            
        },
        computed: {},
        components: {
            'vue-nav': PageNavigationVue,
            'vue-listselect': VueListSelect,
        },
        watch: {
            pageNumber: refresh,
            selectItem: selectedItemChanged,
        }
    });

    //------------------------方法定义-开始------------------//
    function exportFun() {  //refresh
    	generalListItemSelectHandle(listVue, listVue.selectItem)
    	var model={
    		interfaceVersion:this.interfaceVersion,
    		keyword:this.keyword,
    		projectId:this.projectId,
    	}
        new ServerInterface(baseInfo.exportInter).execute(model, function (jsonObj) {
            if (jsonObj.result != "success") {
                noty({"text": jsonObj.info, "type": "error", "timeout": 2000});
            } else {
            	generalSuccessModal();
            	window.location.href="../"+jsonObj.fileURL;
            }
        });
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
            developCompanyId: this.companyId,
            projectId: this.projectId,
            buildingId: this.buildingId,
            orderBy: listVue.orderBy,
        }
    }

    //列表操作--------------获取“删除资源”表单参数
    function getDeleteForm() {
        return {
            interfaceVersion: this.interfaceVersion,
            idArr: this.selectItem
        }
    }

    //选中状态有改变，需要更新“全选”按钮状态
    function selectedItemChanged() {
        listVue.isAllSelected = (listVue.empj_ProjProgInspectionList.length > 0)
            && (listVue.selectItem.length == listVue.empj_ProjProgInspectionList.length);
    }

    //列表操作--------------“全选”按钮被点击
    function checkAllClicked() {
        if (listVue.selectItem.length == listVue.empj_ProjProgInspectionList.length) {
            listVue.selectItem = [];
        } else {
            listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
            listVue.empj_ProjProgInspectionList.forEach(function (item) {
                listVue.selectItem.push(item.tableId);
            });
        }
    }

    //列表操作--------------刷新
    function refresh() {
        new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function (jsonObj) {
            if (jsonObj.result != "success") {
                noty({"text": jsonObj.info, "type": "error", "timeout": 2000});
            } else {
                listVue.empj_ProjProgInspectionList = jsonObj.empj_ProjProgInspection_AFList;
                listVue.pageNumber = jsonObj.pageNumber;
                listVue.countPerPage = jsonObj.countPerPage;
                listVue.totalPage = jsonObj.totalPage;
                listVue.totalCount = jsonObj.totalCount;
                listVue.keyword = jsonObj.keyword;
                listVue.selectedItem = [];
                //动态跳转到锚点处，id="top"
                document.getElementById('Empj_ProjProgInspectionListDiv').scrollIntoView();
            }
        });
    }

    //列表操作------------搜索
    function search() {
        listVue.pageNumber = 1;
        refresh();
    }

    //弹出编辑模态框--更新操作
    function showAjaxModal(empj_ProjProgInspection_AFModel) {
        //empj_ProjProgInspection_AFModel数据库的日期类型参数，会导到网络请求失败
        //Vue.set(updateVue, 'empj_ProjProgInspection_AF', empj_ProjProgInspection_AFModel);
        //updateVue.$set("empj_ProjProgInspection_AF", empj_ProjProgInspection_AFModel);

        updateVue.theState = empj_ProjProgInspection_AFModel.theState;
        updateVue.busiState = empj_ProjProgInspection_AFModel.busiState;
        updateVue.eCode = empj_ProjProgInspection_AFModel.eCode;
        updateVue.userStartId = empj_ProjProgInspection_AFModel.userStartId;
        updateVue.createTimeStamp = empj_ProjProgInspection_AFModel.createTimeStamp;
        updateVue.lastUpdateTimeStamp = empj_ProjProgInspection_AFModel.lastUpdateTimeStamp;
        updateVue.userRecordId = empj_ProjProgInspection_AFModel.userRecordId;
        updateVue.recordTimeStamp = empj_ProjProgInspection_AFModel.recordTimeStamp;
        updateVue.developCompanyId = empj_ProjProgInspection_AFModel.developCompanyId;
        updateVue.eCodeOfDevelopCompany = empj_ProjProgInspection_AFModel.eCodeOfDevelopCompany;
        updateVue.projectId = empj_ProjProgInspection_AFModel.projectId;
        updateVue.theNameOfProject = empj_ProjProgInspection_AFModel.theNameOfProject;
        updateVue.eCodeOfProject = empj_ProjProgInspection_AFModel.eCodeOfProject;
        updateVue.buildingId = empj_ProjProgInspection_AFModel.buildingId;
        updateVue.eCodeOfBuilding = empj_ProjProgInspection_AFModel.eCodeOfBuilding;
        updateVue.upfloorNumber = empj_ProjProgInspection_AFModel.upfloorNumber;
        updateVue.eCodeFromConstruction = empj_ProjProgInspection_AFModel.eCodeFromConstruction;
        updateVue.eCodeFromPublicSecurity = empj_ProjProgInspection_AFModel.eCodeFromPublicSecurity;
        updateVue.recordAveragePriceOfBuilding = empj_ProjProgInspection_AFModel.recordAveragePriceOfBuilding;
        updateVue.escrowStandard = empj_ProjProgInspection_AFModel.escrowStandard;
        updateVue.deliveryType = empj_ProjProgInspection_AFModel.deliveryType;
        updateVue.orgLimitedAmount = empj_ProjProgInspection_AFModel.orgLimitedAmount;
        updateVue.currentFigureProgress = empj_ProjProgInspection_AFModel.currentFigureProgress;
        updateVue.currentLimitedRatio = empj_ProjProgInspection_AFModel.currentLimitedRatio;
        updateVue.nodeLimitedAmount = empj_ProjProgInspection_AFModel.nodeLimitedAmount;
        updateVue.totalGuaranteeAmount = empj_ProjProgInspection_AFModel.totalGuaranteeAmount;
        updateVue.cashLimitedAmount = empj_ProjProgInspection_AFModel.cashLimitedAmount;
        updateVue.effectiveLimitedAmount = empj_ProjProgInspection_AFModel.effectiveLimitedAmount;
        updateVue.expectFigureProgress = empj_ProjProgInspection_AFModel.expectFigureProgress;
        updateVue.expectLimitedRatio = empj_ProjProgInspection_AFModel.expectLimitedRatio;
        updateVue.expectLimitedAmount = empj_ProjProgInspection_AFModel.expectLimitedAmount;
        updateVue.expectEffectLimitedAmount = empj_ProjProgInspection_AFModel.expectEffectLimitedAmount;
        $(baseInfo.updateDivId).modal('show', {
            backdrop: 'static'
        });
    }

    function indexMethod(index) {
        return generalIndexMethod(index, listVue)
    }

    function listItemSelectHandle(listTotal) {
        generalListItemSelectWholeItemHandle(listVue, listTotal)
        var list = listVue.selectItem;
        if (list.length > 1 || list[0] == undefined) {
            listVue.upDisabled = true
        }else{
            if (list[0].approvalState == "待提交") {
                listVue.upDisabled = false
            } else {
                listVue.upDisabled = true
            }
        }
        
        if(listTotal.length >0){
        	listVue.delDisabled = false;
        	listVue.exDisabled = false;
        	for(var i = 0;i<listTotal.length;i++){
        		if(!(listTotal[i].approvalState == "待提交")){
        			listVue.delDisabled = true;
        			break;
        		}
        	}
        }else{
        	listVue.delDisabled = true;
        	listVue.exDisabled = true;
        }
        //审批情况禁用状态
        if (listTotal.length == 1) {
            listVue.approvalDisabled = false;
            listVue.sourceId = listTotal[0].tableId;
            listVue.afId = listTotal[0].afId;
            listVue.workflowId = listTotal[0].workflowId;
            listVue.busiType = listTotal[0].busiType;
        } else {
            listVue.approvalDisabled = true;
        }
    }
    
    function delBldLimitAmount(){
    	generalSelectModal(confirmDel,"确认删除吗？");
    }
    
    function confirmDel(){
    	var idArr = []
    	var list = listVue.selectItem;
    	for(var i = 0;i<list.length;i++){
    		idArr.push(list[i].tableId);
    	}
    	var model = {
    	    idArr : idArr,
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
    function mainAddHandle() {
        enterNewTab("", "项目进度巡查增加", "empj/Empj_ProjProgInspectionAdd.shtml")
    }

    function mainDetailHandle(tableId) {
    	console.log("tableId")
    	console.log(tableId)
        enterNewTab(tableId, "项目进度巡查详情", "empj/Empj_ProjProgInspectionDetail.shtml")
    }

    function mainEditHandle() {
        generalListItemSelectHandle(listVue, listVue.selectItem)
        var list = listVue.selectItem
        enterDetail(list, "项目进度巡查修改", "empj/Empj_ProjProgInspectionEdit.shtml")
    }

    function companyDetailHandle(tableId) {
        enterNewTab(tableId, "机构详情", "emmp/Emmp_CompanyInfoDetail.shtml")
    }

    function projectDetailHandle(tableId) {
        enterNewTab(tableId, "项目详情", "empj/Empj_ProjectInfoDetail.shtml")
    }

    function buildingDetailHandle(tableId) {
        enterNewTab(tableId, "楼幢详情", "empj/Empj_BuildingInfoDetail.shtml")
    }

    function mainDeleteHandle() {
        generalListItemSelectHandle(listVue, listVue.selectItem)
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
        console.log('listVue.poName is ' + listVue.poName)
        generalExportExcel(listVue, listVue.poName, function () {
            refresh()
        })
    }

    function showLog() {
        enterLogTab(listVue.poName)
    }

    function getCompanyList() {
        serverRequest("../Emmp_CompanyInfoForSelect", getTotalListForm(), function (jsonObj) {
            listVue.companyList = jsonObj.emmp_CompanyInfoList
        })

    }

    function changeCompanyListener(data) {
        if (listVue.companyId != data.tableId) {
            listVue.companyId = data.tableId
            listVue.projectId = ""
            getProjectList()
            // listVue.refresh()
        }
    }

    function changeCompanyEmpty() {
        if (listVue.companyId != null) {
            listVue.companyId = null
            listVue.projectId = ""
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
    function getProjectList() {
        var form = getTotalListForm()
        // if(listVue.isNormalUser==2){
        form["developCompanyId"] = listVue.companyId
        // }
        serverRequest("../Empj_ProjectInfoForSelect", form, function (jsonObj) {
            listVue.projectList = jsonObj.empj_ProjectInfoList
        })
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

    //审批情况
    function approvalDetail() {
        // approvalDetailFromHome(listVue.busiType, listVue.sourceId, listVue.afId, listVue.workflowId, 2);
        approvalDetailFromHome("项目进度巡查", listVue.sourceId, listVue.afId, listVue.workflowId, 2);
    }

    function initData() {
        initButtonList();
        getCompanyList();
        var isEnterFromBuildingTable = false
        getIdFormTab("", function (id) {
            console.log('list id is ' + id)
            if (id.indexOf("BuildingTable") != -1) {//从楼盘表进入
                isEnterFromBuildingTable = true
                var splitArr = id.split("-")
                listVue.projectId = Number(splitArr[0])
                console.log('projectId is ' + listVue.projectId)
                listVue.companyId = Number(splitArr[2])
                var buildingId=splitArr[3]
                serverRequest(baseInfo.buildingDetailInterface,{interfaceVersion:19000101,tableId:buildingId},function (jsonObj) {
                    listVue.keyword=jsonObj.empj_BuildingInfo.eCodeFromConstruction
                    listVue.refresh();
                })
            }else{

            }

        })
        // if(!isEnterFromBuildingTable){
        getLoginUserInfo(function (user) {
            listVue.user = user;
            var userType = listVue.user.theType;
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
				console.log('companyId is ' + listVue.companyId + " companyName is " + listVue.companyName);
            }
            
            getProjectList()
        })

        if(!isEnterFromBuildingTable){
            listVue.refresh();
        }
    }

    //------------------------方法定义-结束------------------//

    //------------------------数据初始化-开始----------------//
    listVue.initData();
    //------------------------数据初始化-结束----------------//
})({
    "listDivId": "#Empj_ProjProgInspectionListDiv",
    "updateDivId": "#updateModel",
    "addDivId": "#addModal",
    "exportInter": "../Empj_ProjProgInspectionExeport",//导出
    "listInterface": "../Empj_ProjProgInspection_AFList",
    "buildingDetailInterface": "../Empj_BuildingInfoDetail",
    "addInterface": "../Empj_ProjProgInspection_AFAdd",
    "deleteInterface": "../Empj_ProjProgInspectionBatchDelete",
    "updateInterface": "../Empj_ProjProgInspection_AFUpdate"
});
