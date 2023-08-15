(function (baseInfo) {
    var listVue = new Vue({
        el: baseInfo.listDivId,
        data: {
            poName: "Empj_ProjProgForcast",
            interfaceVersion: 19000101,
            pageNumber: 1,
            countPerPage: 20,
            totalPage: 1,
            totalCount: 1,
            keyword: "",
//            projectName: "",
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
            empj_DailyInspectionReports: [],
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
            approvalStateList : [
            	{tableId:"待提交",theName:"待提交"},
            	{tableId:"审核中",theName:"审核中"},
            	{tableId:"已完结",theName:"已完结"},
            ],
            timeStamp :'',
            timeStampStart : '',
            timeStampEnd : '',
        },
        methods: {
            refresh: refresh,
            initData: initData,
            indexMethod: indexMethod,
            showLog: showLog,
            getSearchForm: getSearchForm,
            getExportForm: getExportForm,
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
            exportForm: exportForm,
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
            }
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
//            projectName: listVue.projectName,
            timeStampStart: listVue.timeStampStart,
            timeStampEnd: listVue.timeStampEnd,
        }
    }
    function getExportForm() {
    	return {
    		interfaceVersion: this.interfaceVersion,
    		
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
        listVue.isAllSelected = (listVue.empj_DailyInspectionReports.length > 0)
            && (listVue.selectItem.length == listVue.empj_DailyInspectionReports.length);
    }

    //列表操作--------------“全选”按钮被点击
    function checkAllClicked() {
        if (listVue.selectItem.length == listVue.empj_DailyInspectionReports.length) {
            listVue.selectItem = [];
        } else {
            listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
            listVue.empj_DailyInspectionReports.forEach(function (item) {
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
                listVue.empj_DailyInspectionReports = jsonObj.empj_ProjProgForcast_AFList;
                listVue.pageNumber = jsonObj.pageNumber;
                listVue.countPerPage = jsonObj.countPerPage;
                listVue.totalPage = jsonObj.totalPage;
                listVue.totalCount = jsonObj.totalCount;
                listVue.keyword = jsonObj.keyword;
                listVue.selectedItem = [];
                //动态跳转到锚点处，id="top"
//                document.getElementById('Empj_DailyInspectionReportsDiv').scrollIntoView();
            }
        });
    }
    function exportForm(){
		new ServerInterface(baseInfo.exportInterface).execute(listVue.getExportForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj ,jsonObj.info);
			}
			else
			{
				window.location.href="../"+jsonObj.fileURL;
			}
		});
	}
    //列表操作------------搜索
    function search() {
        listVue.pageNumber = 1;
        refresh();
    }

    //弹出编辑模态框--更新操作
    function showAjaxModal(empj_ProjProgForcast_AFModel) {
        //empj_ProjProgForcast_AFModel数据库的日期类型参数，会导到网络请求失败
        //Vue.set(updateVue, 'empj_ProjProgForcast_AF', empj_ProjProgForcast_AFModel);
        //updateVue.$set("empj_ProjProgForcast_AF", empj_ProjProgForcast_AFModel);

        updateVue.theState = empj_ProjProgForcast_AFModel.theState;
        updateVue.busiState = empj_ProjProgForcast_AFModel.busiState;
        updateVue.eCode = empj_ProjProgForcast_AFModel.eCode;
        updateVue.userStartId = empj_ProjProgForcast_AFModel.userStartId;
        updateVue.createTimeStamp = empj_ProjProgForcast_AFModel.createTimeStamp;
        updateVue.lastUpdateTimeStamp = empj_ProjProgForcast_AFModel.lastUpdateTimeStamp;
        updateVue.userRecordId = empj_ProjProgForcast_AFModel.userRecordId;
        updateVue.recordTimeStamp = empj_ProjProgForcast_AFModel.recordTimeStamp;
        updateVue.developCompanyId = empj_ProjProgForcast_AFModel.developCompanyId;
        updateVue.eCodeOfDevelopCompany = empj_ProjProgForcast_AFModel.eCodeOfDevelopCompany;
        updateVue.projectId = empj_ProjProgForcast_AFModel.projectId;
        updateVue.theNameOfProject = empj_ProjProgForcast_AFModel.theNameOfProject;
        updateVue.eCodeOfProject = empj_ProjProgForcast_AFModel.eCodeOfProject;
        updateVue.buildingId = empj_ProjProgForcast_AFModel.buildingId;
        updateVue.eCodeOfBuilding = empj_ProjProgForcast_AFModel.eCodeOfBuilding;
        updateVue.upfloorNumber = empj_ProjProgForcast_AFModel.upfloorNumber;
        updateVue.eCodeFromConstruction = empj_ProjProgForcast_AFModel.eCodeFromConstruction;
        updateVue.eCodeFromPublicSecurity = empj_ProjProgForcast_AFModel.eCodeFromPublicSecurity;
        updateVue.recordAveragePriceOfBuilding = empj_ProjProgForcast_AFModel.recordAveragePriceOfBuilding;
        updateVue.escrowStandard = empj_ProjProgForcast_AFModel.escrowStandard;
        updateVue.deliveryType = empj_ProjProgForcast_AFModel.deliveryType;
        updateVue.orgLimitedAmount = empj_ProjProgForcast_AFModel.orgLimitedAmount;
        updateVue.currentFigureProgress = empj_ProjProgForcast_AFModel.currentFigureProgress;
        updateVue.currentLimitedRatio = empj_ProjProgForcast_AFModel.currentLimitedRatio;
        updateVue.nodeLimitedAmount = empj_ProjProgForcast_AFModel.nodeLimitedAmount;
        updateVue.totalGuaranteeAmount = empj_ProjProgForcast_AFModel.totalGuaranteeAmount;
        updateVue.cashLimitedAmount = empj_ProjProgForcast_AFModel.cashLimitedAmount;
        updateVue.effectiveLimitedAmount = empj_ProjProgForcast_AFModel.effectiveLimitedAmount;
        updateVue.expectFigureProgress = empj_ProjProgForcast_AFModel.expectFigureProgress;
        updateVue.expectLimitedRatio = empj_ProjProgForcast_AFModel.expectLimitedRatio;
        updateVue.expectLimitedAmount = empj_ProjProgForcast_AFModel.expectLimitedAmount;
        updateVue.expectEffectLimitedAmount = empj_ProjProgForcast_AFModel.expectEffectLimitedAmount;
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
        } else {
            if (list[0].approvalState == "待提交") {
                listVue.upDisabled = false
            } else {
                listVue.upDisabled = true
            }
        }
        
        if(listTotal.length >0){
        	listVue.delDisabled = false;
        	for(var i = 0;i<listTotal.length;i++){
        		if(!(listTotal[i].approvalState == "待提交")){
        			listVue.delDisabled = true;
        			break;
        		}
        	}
        }else{
        	listVue.delDisabled = true;
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
        enterNewTab("", "日常巡查统计报告增加", "empj/Empj_ProjProgForcastAdd.shtml")
    }

    function mainDetailHandle(tableId) {
        enterNewTab(tableId, "工程进度巡查详情", "empj/Empj_ProjProgForcastDetail.shtml")
    }

    function mainEditHandle() {
        generalListItemSelectHandle(listVue, listVue.selectItem)
        var list = listVue.selectItem
        enterDetail(list, "日常巡查统计报告修改", "empj/Empj_ProjProgForcastEdit.shtml")
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
        	$("#openingSearchDate").val("");
            listVue.keyword = ""
            listVue.projectId = ""
            listVue.timeStamp= ""
            listVue.timeStampStart = ""
		    listVue.timeStampEnd = ""
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
             listVue.refresh()
        }
    }

    function changeProjectEmpty() {
        if (listVue.projectId != null) {
            listVue.projectId = null
             listVue.refresh()
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
        approvalDetailFromHome("日常巡查统计报告", listVue.sourceId, listVue.afId, listVue.workflowId, 2);
    }
    laydate.render({
  	  elem: '#EscrowBankFunds_listDate',
  	  done: function(value, date){
  		    listVue.timeStamp=value;
  		   
  		  }
  	});
    function initData() {
        initButtonList();
        getCompanyList();
        var isEnterFromBuildingTable = false
        getIdFormTab("", function (id) {
            console.log('list id is' + id)
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
//            if (userType == "1") {//如果是正泰用户
//                if (isEnterFromBuildingTable) {
//
//                } else {
//                    // listVue.companyId = user.developCompanyId
//                    // listVue.companyName = user.theNameOfCompany
//                    // console.log('companyId is ' + listVue.companyId + " companyName is " + listVue.companyName)
//                }
//                // getUserDetail()
//                // if(listVue.user.theType=="1"){//如果是正泰用户
//                //     listVue.isNormalUser=1
//                // }else{//如果是一般用户
//                //     listVue.isNormalUser=2
//                // }
//            } else {//如果是一般用户
//                listVue.companyId = user.developCompanyId
//                listVue.companyName = user.theNameOfCompany
//                console.log('companyId is ' + listVue.companyId + " companyName is " + listVue.companyName)
//            }
            
            if(comType == '1')
            {//开发企业
				listVue.companyId = user.developCompanyId;
				listVue.companyName = user.theNameOfCompany;
				console.log('companyId is ' + listVue.companyId + " companyName is " + listVue.companyName);
            }
            
            getProjectList()
        })
        // }
        // getProjectList();

        if(!isEnterFromBuildingTable){
            listVue.refresh();
        }
    }

    
    laydate.render({
	    elem: '#openingSearchDate',
	    range: '~',
		done: function(value, date, endDate){
		  	var arr = value.split("~");
		    
		    listVue.timeStampStart = arr[0];
		    listVue.timeStampEnd = arr[1];
		}
	});
    //------------------------方法定义-结束------------------//

    //------------------------数据初始化-开始----------------//
    // listVue.refresh();
    listVue.initData();
    //------------------------数据初始化-结束----------------//
})({
    "listDivId": "#Empj_DailyInspectionReportsDiv",
    "updateDivId": "#updateModel",
    "addDivId": "#addModal",
    "listInterface": "../Empj_ProjProgInspectionReport",
    "buildingDetailInterface": "../Empj_BuildingInfoDetail",
    "addInterface": "../Empj_ProjProgForcast_AFAdd",
    "deleteInterface": "../Empj_ProjProgForcastBatchDelete",
    "updateInterface": "../Empj_ProjProgForcast_AFUpdate",
    "exportInterface": "../Empj_ProjProgInspectionReportExport",
});
