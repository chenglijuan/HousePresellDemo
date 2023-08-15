(function (baseInfo) {
    var detailVue = new Vue({
        el: baseInfo.detailDivId,
        data: {
            interfaceVersion: 19000101,
            tgpj_BankAccountSupervisedModel:
                {
                    tableId: "",
                    developCompany: {theName: ""},
                    userStart: {theName: ""},
                    userRecord: {theName: ""},
                    recordTimeStamp: "",
                    createTimeStamp: "",
                },
            theState:0,
            tableId: 1,
            recordDate: "",
            createDate: "",
            idState: "",
            developCompanyList: [],
            bankList: [],
            empj_BuildingInfoList:[],
            pageNumber : 1,
            countPerPage : 20,
            totalPage : 1,
            totalCount : 1,
            keyword : "",
            selectItem : [],
            companyId:"",

            //附件材料
            busiType:'200102',
            dialogVisible: false,
            dialogImageUrl: "",
            fileType:"",
            fileList : [],
            showButton:false,
            hideShow:true,
            uploadData : [],
            smAttachmentList:[],//页面显示已上传的文件
            uploadList:[],
            loadUploadList: [],
            showDelete : false,  //附件是否可编辑
            //其他
            errMsg:"", //错误提示信息

            //附件材料
            loadUploadList: [],
            showDelete : true,  //附件是否可编辑

            // //附件材料
            // busiType:'03010203',
            // dialogVisible: false,
            // dialogImageUrl: "",
            // fileType:"",
            // fileList : [],
            // showButton:true,
            // hideShow:false,
            // uploadData : [],
            // smAttachmentList:[],//页面显示已上传的文件
            // uploadList:[],
            // loadUploadList: [],
            // showDelete : true,
            // //其他
            // errMsg:"", //错误提示信息

            //选择监管楼幢
            buildingList:[],
            userStartId:"10265",//todo 需要更改
            selectBuildingConstructionCode:"",
            selectedBuilding:{
                // projecName:"",
                // beginDateString:"",
                // endDateString:"",
            },
            selectBuildingId:"",

            projectList:[],
            selectProjectId:"",
            selectProjectName:"",

            superviseBuildingList:[],
            // selectsuperviseBuildingId:"",

            isAddNew:"",
            isUsing:"",
        },
        methods: {
            //详情
            refresh: refresh,
            initData: initData,
            getSearchForm: getSearchForm,
            //更新
            getUpdateForm: getUpdateForm,
            update: update,
            changePageNumber : function(data){
                detailVue.pageNumber = data;
            },
            indexMethod: indexMethod,
            listItemSelectHandle:listItemSelectHandle,
            addBuildingHandle:addBuildingHandle,
            deleteBuildingHandle:deleteBuildingHandle,
            buildingEditHandle:buildingEditHandle,

            saveAttachment:saveAttachment,

            clickListener:clickListener,
            dialogSave:dialogSave,
            changeBuildingListener:changeBuildingListener,
            changeProjectListener:changeProjectListener,

            getSuperviseBuildingList:getSuperviseBuildingList,
        },
        computed: {},
        components : {
            'vue-nav' : PageNavigationVue,
            "my-uploadcomponent":fileUpload,
            'vue-select': VueSelect,
        },
        watch:{
            pageNumber : refresh,
            selectItem : selectedItemChanged,
        },
        mounted: function ()
        {
            laydate.render({
                range:true,
                elem: '#choosBldLimitVersion',
                done:function(value1,value2,value3){
                    //todo
                    var temp=value1.split(" - ")
                    console.log(temp)
                    detailVue.selectedBuilding.beginTimeStampString=temp[0]
                    detailVue.selectedBuilding.endTimeStampString=temp[1]
                }
            })
        }
    });

    //------------------------方法定义-开始------------------//
    //详情操作--------------获取"机构详情"参数
    function getSearchForm() {
        return {
            interfaceVersion: detailVue.interfaceVersion,
            tableId: detailVue.tableId,
        }
    }

    //详情操作--------------
    function refresh() {
        if (detailVue.tableId == null || detailVue.tableId < 1) {
            return;
        }

        getDetail();
        // getBuildingList()
        getSuperviseBuildingList()
        loadUpload()
    }

    function getDetail() {
        new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function (jsonObj) {
            if (jsonObj.result != "success") {
                noty({"text": jsonObj.info, "type": "error", "timeout": 2000});
            }
            else {
                detailVue.recordDate = timeStamp2DayDate(detailVue.recordTimeStamp)
                detailVue.createDate = timeStamp2DayDate(detailVue.createTimeStamp)
                detailVue.idState = theState2String(jsonObj.tgpj_BankAccountSupervised)
                detailVue.tgpj_BankAccountSupervisedModel = jsonObj.tgpj_BankAccountSupervised;
                detailVue.companyId=jsonObj.tgpj_BankAccountSupervised.developCompanyId
                getProjectList()
                // getBuildingList()
            }
        });
    }

    //详情更新操作--------------获取"更新机构详情"参数
    function getUpdateForm() {
        return {
            //附件材料
            busiType : detailVue.busiType,
            sourceId : detailVue.tableId,
            generalAttachmentList : this.$refs.listenUploadData.uploadData,

            interfaceVersion: detailVue.interfaceVersion,
            tableId: detailVue.tableId,
            theState: detailVue.tgpj_BankAccountSupervisedModel.theState,
            busiState: detailVue.tgpj_BankAccountSupervisedModel.busiState,
            eCode: detailVue.tgpj_BankAccountSupervisedModel.eCode,
            userStartId: detailVue.tgpj_BankAccountSupervisedModel.userStartId,
            createTimeStamp: detailVue.tgpj_BankAccountSupervisedModel.createTimeStamp,
            lastUpdateTimeStamp: detailVue.tgpj_BankAccountSupervisedModel.lastUpdateTimeStamp,
            userRecordId: detailVue.tgpj_BankAccountSupervisedModel.userRecordId,
            recordTimeStamp: detailVue.tgpj_BankAccountSupervisedModel.recordTimeStamp,
            developCompanyId: detailVue.tgpj_BankAccountSupervisedModel.developCompanyId,
            eCodeOfDevelopCompany: detailVue.tgpj_BankAccountSupervisedModel.eCodeOfDevelopCompany,
            bankId: detailVue.tgpj_BankAccountSupervisedModel.bankId,
            theNameOfBank: detailVue.tgpj_BankAccountSupervisedModel.theNameOfBank,
            shortNameOfBank: detailVue.tgpj_BankAccountSupervisedModel.shortNameOfBank,
            bankBranchId: detailVue.tgpj_BankAccountSupervisedModel.bankBranchId,
            theName: detailVue.tgpj_BankAccountSupervisedModel.theName,
            theAccount: detailVue.tgpj_BankAccountSupervisedModel.theAccount,
            remark: detailVue.tgpj_BankAccountSupervisedModel.remark,
            contactPerson: detailVue.tgpj_BankAccountSupervisedModel.contactPerson,
            contactPhone: detailVue.tgpj_BankAccountSupervisedModel.contactPhone,
            isUsing:detailVue.tgpj_BankAccountSupervisedModel.isUsing,
        }
    }

    function update() {
        generalSuccessModal()
        enterNewTabCloseCurrent(detailVue.tableId,"楼幢监管账号详情","tgpj/Tgpj_BankAccountBuildingSupervisedDetail.shtml")
        // new ServerInterfaceJsonBody(baseInfo.updateInterface).execute(detailVue.getUpdateForm(), function (jsonObj) {
        //     if (jsonObj.result != "success") {
        //         // noty({"text": jsonObj.info, "type": "error", "timeout": 2000});
        //         generalErrorModal(jsonObj)
        //     }
        //     else {
        //         generalSuccessModal()
        //         enterNewTabCloseCurrent(jsonObj.tableId,"楼幢监管账号详情","tgpj/Tgpj_BankAccountBuildingSupervisedDetail.shtml")
        //         // $(baseInfo.detailDivId).modal('hide');
        //         // refresh();
        //     }
        // });
    }

    function getProjectList() {
        var form={
            interfaceVersion:19000101,
            developCompanyId:detailVue.companyId,

        }
        serverRequest("../Empj_ProjectInfoForSelect",form,function (jsonObj) {
            detailVue.projectList=jsonObj.empj_ProjectInfoList
        })

    }

    function getCompanyList() {
        serverRequest(baseInfo.companyListInterface, getTotalListForm(), function (jsonObj) {
            detailVue.developCompanyList = jsonObj.emmp_CompanyInfoList
        })
    }

    function getBankList() {
        serverRequest(baseInfo.bankListInterface, getTotalListForm(), function (jsonObj) {
            detailVue.bankList = jsonObj.emmp_BankBranchList
        })
    }

    // function getBuildingList(){
    //     serverRequest(baseInfo.listInterface,searchListForm(),function (jsonObj) {
    //         detailVue.empj_BuildingInfoList=jsonObj.empj_BuildingInfoList
    //         // detailVue.pageNumber=jsonObj.pageNumber
    //         // detailVue.totalCount=jsonObj.totalCount
    //         // detailVue.totalPage=jsonObj.totalPage
    //         // detailVue.countPerPage=jsonObj.countPerPage
    //     })
    // }

    function indexMethod(index) {
        return generalIndexMethod(index, detailVue)
    }

    function listItemSelectHandle(list) {
        generalListItemSelectHandle(detailVue,list)
    }

    //选中状态有改变，需要更新“全选”按钮状态
    function selectedItemChanged()
    {
        detailVue.isAllSelected = (detailVue.empj_BuildingInfoList.length > 0)
            &&	(detailVue.selectItem.length == detailVue.empj_BuildingInfoList.length);
    }

    function addBuildingHandle() {
        enterNewTab("","新增楼幢信息","empj/Empj_BuildingInfoAdd.shtml")
    }

    function deleteBuildingHandle() {
        generalDeleteModal(detailVue,"Empj_BuildingAccountSupervised")
    }

    function buildingEditHandle() {
        var list = detailVue.selectItem
        enterDetail(list, "楼幢信息修改", "empj/Empj_BuildingInfoEdit.shtml")
    }

    function loadUpload() {
        generalLoadFile2(detailVue, detailVue.busiType)
    }

    function saveAttachment() {
        generalUploadFile(detailVue, "Tgpj_BankAccountBuildingSupervised", baseInfo.attachmentBatchAddInterface, baseInfo.successModel)
    }

    function clickListener(type) {
        detailVue.isAddNew = type
        if (type == "2") {
            var selectedId = detailVue.selectItem[0]
            console.log('selectedId is ')
            console.log(selectedId)
            for (var i = 0; i < detailVue.superviseBuildingList.length; i++) {
                if (detailVue.superviseBuildingList[i].tableId == selectedId) {
                    var temp = detailVue.superviseBuildingList[i]
                    detailVue.selectedBuilding = temp
                    detailVue.selectedBuilding.buildingInfoId = temp.buildingId
                    detailVue.selectedBuilding.superviseBuildingId = temp.tableId
                    detailVue.selectedBuilding.tableId = temp.buildingId
                    detailVue.selectedBuilding.theName = temp.eCodeFromConstruction
                    detailVue.selectedBuilding.eCode = temp.buildingEcode
                    detailVue.selectedBuilding.beginTimeStampString = temp.beginTimeStampString
                    detailVue.selectedBuilding.endTimeStampString = temp.endTimeStampString
                    detailVue.selectedBuilding.isUsing = temp.isUsing

                    detailVue.selectProjectId = temp.projectId
                    detailVue.selectBuildingId = temp.buildingId

                    // console.log('temp is ')
                    // console.log(temp)
                    // for(var j=0;j<detailVue.projectList.length;j++){
                    //     var project=detailVue.projectList[j]
                    //     console.log('project is ')
                    //     console.log(project)
                    //     if(project.tableId==temp.projectId){
                    //         detailVue.selectProjectId=project.tableId
                    //         detailVue.selectProjectName=project.theName
                    //         getBuildingList()
                    //     }
                    //
                    // }

                }
            }

            // for (var i = 0; i < detailVue.superviseBuildingList.length; i++) {
            //     console.log(detailVue.superviseBuildingList[i])
            //     if (detailVue.superviseBuildingList[i].tableId == selectedId) {
            //         var temp = detailVue.superviseBuildingList[i]
            //         console.log('选择的building为：')
            //         console.log(temp)
            //         detailVue.selectedBuilding=temp
            //         detailVue.selectedBuilding.buildingInfoId=temp.buildingId
            //         detailVue.selectedBuilding.superviseBuildingId=temp.tableId
            //         detailVue.selectedBuilding.tableId=temp.buildingId
            //         detailVue.selectedBuilding.theName=temp.eCodeFromConstruction
            //         detailVue.selectedBuilding.eCode=temp.buildingEcode
            //         detailVue.selectedBuilding.beginTimeStampString=temp.beginTimeStampString
            //         detailVue.selectedBuilding.endTimeStampString=temp.endTimeStampString
            //         detailVue.selectedBuilding.isUsing=temp.isUsing
            //
            //         break
            //     }
            // }
        }else if (type=="1"){
            detailVue.selectedBuilding={
                isUsing:0,
            }
            detailVue.selectBuildingId=""
            detailVue.selectProjectId=""
            $("#choosBldLimitVersion").val("")
        }
    }

    function changeProjectListener(data) {
        if(data.tableId!=detailVue.selectProjectId){
            detailVue.selectedBuilding={
                isUsing:0
            }
            // detailVue.selectedBuilding.eCode=""
            // // detailVue.selectedBuilding.projectName=data.projectName
            // detailVue.selectedBuilding.eCodeFromPublicSecurity=""
            // detailVue.selectedBuilding.theName=""
            // detailVue.selectedBuilding.buildingInfoId=""
            detailVue.selectBuildingId=""
            detailVue.buildingList=[]
        }
        for(var i=0;i<detailVue.projectList.length;i++){
            var item =detailVue.projectList[i]
            if(item.tableId==data.tableId){
                detailVue.selectProjectId=item.tableId
                detailVue.selectProjectName=item.theName
                getBuildingList()
                break
            }
        }

    }

    function getBuildingList() {
        // serverRequest(baseInfo.getBuildingList,{interfaceVersion:19000101,tableId:detailVue.companyId,projectId:detailVue.selectProjectId},function (jsonObj) {
        serverRequest("../Empj_BuildingInfoForSelect",{interfaceVersion:19000101,companyId:detailVue.companyId,projectId:detailVue.selectProjectId},function (jsonObj) {
            // detailVue.buildingList=jsonObj.buildingList
            detailVue.buildingList=jsonObj.empj_BuildingInfoList
            for(var i=0;i<detailVue.buildingList.length;i++){
                var building=detailVue.buildingList[i]
                building.theName=building.eCodeFromConstruction
            }
        })
    }

    function changeBuildingListener(data) {
        console.log(data)
        // detailVue.selectedBuilding=data
        detailVue.selectedBuilding.eCode=data.eCode
        // detailVue.selectedBuilding.projectName=data.projectName
        detailVue.selectedBuilding.eCodeFromPublicSecurity=data.eCodeFromPublicSecurity
        detailVue.selectedBuilding.theName=data.eCodeFromConstruction
        detailVue.selectedBuilding.buildingInfoId=data.tableId
        detailVue.selectBuildingId=data.tableId

        console.log('选择的buildingId is '+detailVue.selectBuildingId)
        console.log(detailVue.selectedBuilding)

    }

    function dialogSave() {
        console.log('type is ' + detailVue.isAddNew)
        console.log(detailVue.selectedBuilding)
        // console.log('form is ')
        // console.log(form)
        console.log('detailVue.selectProjectId is '+detailVue.selectProjectId)
        console.log('detailVue.selectBuildingId is '+detailVue.selectBuildingId)
        if(detailVue.selectProjectId==""){
            generalErrorModal(undefined,"请选择开发项目")
            return
        }
        if(detailVue.selectBuildingId==""){
            generalErrorModal(undefined,"请选择施工编号")
            return
        }
        if (detailVue.isAddNew == "1") {
            addBuildingSuperviseRelation()
        } else if (detailVue.isAddNew == "2") {
            updateBuildingSuperviseRelation()
        }

    }

    function getSuperviseBuildingForm() {
        return{
            interfaceVersion:detailVue.interfaceVersion,
            buildingInfoId:detailVue.selectedBuilding.buildingInfoId,
            bankAccountSupervisedId:detailVue.tableId,
            beginTimeStampString:detailVue.selectedBuilding.beginTimeStampString,
            endTimeStampString:detailVue.selectedBuilding.endTimeStampString,
            tableId:detailVue.selectedBuilding.superviseBuildingId,
            isUsing:detailVue.selectedBuilding.isUsing,
        }
    }

    function addBuildingSuperviseRelation() {
        serverRequest(baseInfo.superviseBuildingAdd,getSuperviseBuildingForm(),function () {
            generalSuccessModal()
            generalHideModal("#addBuilding")
            detailVue.selectedBuilding={}
            refresh()
        },function (jsonObj) {
            generalErrorModal(jsonObj)
        })
    }

    function updateBuildingSuperviseRelation() {
        serverRequest(baseInfo.superviseBuildingUpdate,getSuperviseBuildingForm(),function () {
            generalSuccessModal()
            generalHideModal("#addBuilding")
            detailVue.selectedBuilding={}
            refresh()
        },function (jsonObj) {
            generalErrorModal(jsonObj)
        })
    }

    function getSuperviseBuildingList() {
        var getSuperviseBuildingForm={
            interfaceVersion:detailVue.interfaceVersion,
            pageNumber:detailVue.pageNumber,
            countPerPage:detailVue.countPerPage,
            totalPage:detailVue.totalPage,
            keyword:detailVue.keyword,
            theState:detailVue.theState,
            bankAccountSupervisedId:detailVue.tableId,
        }
        serverRequest(baseInfo.superviseBuildingList, getSuperviseBuildingForm, function (jsonObj) {
            detailVue.superviseBuildingList = jsonObj.empj_BuildingAccountSupervisedList
            detailVue.pageNumber=jsonObj.pageNumber;
            detailVue.countPerPage=jsonObj.countPerPage;
            detailVue.totalPage=jsonObj.totalPage;
            detailVue.totalCount = jsonObj.totalCount;
            detailVue.keyword=jsonObj.keyword;
            detailVue.selectItem=[];
            //动态跳转到锚点处，id="top"
            // document.getElementById('emmp_BankBranchListDiv').scrollIntoView();
        })
    }


    function initData() {
        // getCompanyList()
        getBankList()
        getIdFormTab("", function (id) {
            detailVue.tableId = id
            refresh()
        })

    }

    //------------------------方法定义-结束------------------//

    //------------------------数据初始化-开始----------------//
    // detailVue.refresh();
    detailVue.initData();
    //------------------------数据初始化-结束----------------//
})({
    "detailDivId": "#tgpj_BankAccountBuildingSupervisedEditDiv",
    "detailInterface": "../Tgpj_BankAccountSupervisedDetail",
    "updateInterface": "../Tgpj_BankAccountSupervisedUpdate",
    "companyListInterface": "../Emmp_CompanyInfoForSelect",
    "bankListInterface": "../Emmp_BankBranchForSelect",
    "listInterface":"../Empj_BuildingInfoSuperviseList",
    "deleteInterface":"../Empj_BuildingInfoDelete",
    "getBuildingList":"../Emmp_CompanyGetBuildingList",
    "superviseBuildingList":"../Empj_BuildingAccountSupervisedList",
    "superviseBuildingAdd":"../Empj_BuildingAccountSupervisedAdd",
    "superviseBuildingUpdate":"../Empj_BuildingAccountSupervisedUpdate",
    "superviseBuildingBatchDelete":"../Empj_BuildingAccountSupervisedBatchDelete",
    "superviseBuildingDelete":"../Empj_BuildingAccountSupervisedDelete",

    //材料附件
    "loadUploadInterface": "../Sm_AttachmentCfgList",
    "attachmentBatchAddInterface": "../Sm_AttachmentBatchAdd",
    "attachmentListInterface": "../Sm_AttachmentList",
    //模态框
    "successModel": "#successM",
    "errorModal": "#errorM",
});
