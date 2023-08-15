(function (baseInfo) {
    var detailVue = new Vue({
        el: baseInfo.detailDivId,
        data: {
            interfaceVersion: 19000101,
            errMsg: "",
            emmp_BankInfoModel: {remark: ""},
            tableId: 1,

            //附件材料
            busiType: '020201',
            dialogVisible: false,
            dialogImageUrl: "",
            fileType: "",
            fileList: [],
            showButton: true,
            hideShow: false,
            uploadData: [],
            smAttachmentList: [],//页面显示已上传的文件
            uploadList: [],
            loadUploadList: [],
            showDelete: true,

            //机构成员相关
            pageNumber: 1,
            countPerPage: 10,
            totalPage: 1,
            totalCount: 1,
            theState: 0,//正常为0，删除为1
            selectItem: [],
            orgMemberList: [],
            //机构成员操作相关
            addTheName: "",
            addIdType: "",
            addIdNumber: "",
            addTheNameOfDepartment: "",
            parameterNameList: [],
            addParameterNameId: "",
            addPositionName: "",
            addPhoneNumber: "",
            addEmail: "",
            addQq: "",
            addWeixin: "",
            updateOrgMemberModel: {},
        },
        methods: {
            //详情
            refresh: refresh,
            initData: initData,
            getSearchForm: getSearchForm,
            //更新
            getUpdateForm: getUpdateForm,
            update: update,
            saveAttachment: saveAttachment,

            //机构成员列表
            refreshOrgMemberList: refreshOrgMemberList,
            getParameterNameList: getParameterNameList,
            orgMemberDeleteMakeSure: orgMemberDeleteMakeSure,
            orgMemberDeleteHandle: orgMemberDeleteHandle,
            addOrgMember: addOrgMember,
            //列表相关
            listItemSelectHandle: listItemSelectHandle,
            indexMethod: indexMethod,
            changePageNumber: function (data) {
                detailVue.pageNumber = data;
            },
            resetSearch: resetSearch,
            changeCountPerPage: function (data) {
                if (detailVue.countPerPage != data) {
                    detailVue.countPerPage = data;
                    detailVue.refresh();
                }
            },
            search: search,

        },
        computed: {},
        components: {
            "my-uploadcomponent": fileUpload
        },
        watch: {}
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
        getDetail();
        refreshOrgMemberList()
        loadUpload()
    }

    function getDetail() {
        serverRequest(baseInfo.detailInterface, detailVue.getSearchForm(), function (jsonObj) {
            detailVue.emmp_BankInfoModel = jsonObj.emmp_BankInfo;
        })
    }

    //详情更新操作--------------获取"更新机构详情"参数
    function getUpdateForm() {
        return {
            //附件材料
            busiType: detailVue.busiType,
            sourceId: detailVue.tableId,
            generalAttachmentList: this.$refs.listenUploadData.uploadData,

            interfaceVersion: detailVue.interfaceVersion,
            tableId: detailVue.tableId,
            theState: detailVue.emmp_BankInfoModel.theState,
            eCode: detailVue.emmp_BankInfoModel.eCode,
            userStartId: detailVue.emmp_BankInfoModel.userStartId,
            userRecordId: detailVue.emmp_BankInfoModel.userRecordId,
            theName: detailVue.emmp_BankInfoModel.theName,
            shortName: detailVue.emmp_BankInfoModel.shortName,
            leader: detailVue.emmp_BankInfoModel.leader,
            address: detailVue.emmp_BankInfoModel.address,
            capitalCollectionModel: detailVue.emmp_BankInfoModel.capitalCollectionModel,
            theType: detailVue.emmp_BankInfoModel.theType,
            contactPerson: detailVue.emmp_BankInfoModel.contactPerson,
            contactPhone: detailVue.emmp_BankInfoModel.contactPhone,
            ftpDirAddress: detailVue.emmp_BankInfoModel.ftpDirAddress,
            ftpAddress: detailVue.emmp_BankInfoModel.ftpAddress,
            ftpPort: detailVue.emmp_BankInfoModel.ftpPort,
            ftpUserName: detailVue.emmp_BankInfoModel.ftpUserName,
            ftpPwd: detailVue.emmp_BankInfoModel.ftpPwd,
            financialInstitution: detailVue.emmp_BankInfoModel.financialInstitution,
            theTypeOfPOS: detailVue.emmp_BankInfoModel.theTypeOfPOS,
            eCodeOfSubject: detailVue.emmp_BankInfoModel.eCodeOfSubject,
            eCodeOfProvidentFundCenter: detailVue.emmp_BankInfoModel.eCodeOfProvidentFundCenter,
            remark: detailVue.emmp_BankInfoModel.remark,
            bankNo: detailVue.emmp_BankInfoModel.bankNo,
            bankCode: detailVue.emmp_BankInfoModel.bankCode,
            //机构成员
            orgMemberList: this.orgMemberList,
        }
    }

    function update() {
        serverBodyRequest(baseInfo.updateInterface, detailVue.getUpdateForm(), function (jsonObj) {
            generalSuccessModal()
            enterNewTabCloseCurrent(jsonObj.tableId, '金融机构详情', 'emmp/Emmp_BankInfoDetail.shtml')
        })
    }

    /********* 附件材料 相关 开始 *********/

    function loadUpload() {
        generalLoadFile2(detailVue, detailVue.busiType)
    }

    function saveAttachment() {
        generalUploadFile(detailVue, "Emmp_BankInfo", baseInfo.attachmentBatchAddInterface, baseInfo.successModal)
    }

    //编辑页----------------删除
    function handleRemoveList(sourceId) {
        this.smAttachmentList.splice(sourceId, 1);
    }

    function listItemSelectHandle(list) {
        detailVue.selectItem = [];
        for (var index = 0; index < list.length; index++) {
            var element = list[index];
            detailVue.selectItem.push(element)
        }
        // generalListItemSelectHandle(detailVue, list)
    }

    function resetSearch() {
        generalResetSearch(detailVue, function () {
            refresh()
        })
    }

    //列表操作------------搜索
    function search() {
        detailVue.pageNumber = 1;
        refresh();
    }

    function indexMethod(index) {
        return generalIndexMethod(index, detailVue)
    }

    /********* OrgMember 相关 *********/

    function refreshOrgMemberList() {
        console.log('refreshOrgMemberList')
        var model = {
            interfaceVersion: detailVue.interfaceVersion,
            pageNumber: detailVue.pageNumber,
            countPerPage: detailVue.countPerPage,
            totalPage: detailVue.totalPage,
            theState: detailVue.theState,
            // companyId: detailVue.tableId,
            bankId: detailVue.tableId,
        };
        new ServerInterface(baseInfo.orgMemberListInterface).execute(model, function (jsonObj) {
            if (jsonObj.result != "success") {
                generalErrorModal(jsonObj);
            }
            else {
                detailVue.orgMemberList = jsonObj.emmp_OrgMemberList;
                detailVue.pageNumber = jsonObj.pageNumber;
                detailVue.countPerPage = jsonObj.countPerPage;
                detailVue.totalPage = jsonObj.totalPage;
                detailVue.totalCount = jsonObj.totalCount;
                detailVue.selectItem = [];
            }
        });
    }

    function getParameterNameList() {
        //判断是否请求过通用字段列表
        if (detailVue.parameterNameList.length > 0) {
            return;
        }
        var model = {
            interfaceVersion: detailVue.interfaceVersion,
            theName: "职务",
        };
        serverRequest(baseInfo.baseParameterListInterface, model, function (jsonObj) {
            detailVue.parameterNameList = jsonObj.sm_BaseParameterList;
        })
    }

    function addOrgMember(divStr) {
        var isPass = errorCheckForAll(divStr);
        if (!isPass) {
            return;
        }
        var model = {
            interfaceVersion: detailVue.interfaceVersion,
            theName: detailVue.addTheName,
            idType: detailVue.addIdType,
            idNumber: detailVue.addIdNumber,
            theNameOfDepartment: detailVue.addTheNameOfDepartment,
            parameterId: detailVue.addParameterNameId,
            positionName: detailVue.addPositionName,
            phoneNumber: detailVue.addPhoneNumber,
            email: detailVue.addEmail,
            qq: detailVue.addQq,
            weixin: detailVue.addWeixin,
            // companyId:detailVue.emmp_CompanyInfoModel.tableId,
            bankId: detailVue.tableId,
        };
        detailVue.orgMemberList.push(model);

        detailVue.addTheName = "";
        detailVue.addIdType = "";
        detailVue.addIdNumber = "";
        detailVue.addTheNameOfDepartment = "";
        detailVue.addParameterNameId = "";
        detailVue.addPositionName = "";
        detailVue.addPhoneNumber = "";
        detailVue.addEmail = "";
        detailVue.addQq = "";
        detailVue.addWeixin = "";

        $(baseInfo.addPersonModal).modal('hide');
    }

    function orgMemberDeleteHandle() {
        if (detailVue.selectItem.length == 0) {
            generalErrorModal("请选择要删除的机构成员");
        }
        else {
            $(baseInfo.deleteOrgMemberAlert).modal('show', {
                backdrop: 'static'
            });
        }
    }

    function orgMemberDeleteMakeSure() {
        console.log('orgMemberDeleteMakeSure')
        var newOrgMemberList = [];
        for (var i = 0; i < detailVue.orgMemberList.length; i++) {
            var needDelete = false;
            for (var j = 0; j < detailVue.selectItem.length; j++) {
                if (detailVue.orgMemberList[i] == detailVue.selectItem[j]) {
                    needDelete = true;
                }
            }
            if (needDelete == false) {
                newOrgMemberList.push(detailVue.orgMemberList[i]);
            }
        }
        console.log('newOrgMemberList is ')
        for (var i = 0; i < newOrgMemberList.length; i++) {
            var obj = newOrgMemberList[i];
            console.log(obj)
        }
        detailVue.orgMemberList = newOrgMemberList;
        $(baseInfo.deleteOrgMemberAlert).modal('hide');
    }

    function initData() {
        getIdFormTab("", function (tableId) {
            detailVue.tableId = tableId
            refresh();
        })
        getParameterNameList()
    }

    //------------------------方法定义-结束------------------//

    //------------------------数据初始化-开始----------------//
    detailVue.initData();
    //------------------------数据初始化-结束----------------//
})({
    "detailDivId": "#emmp_BankInfoDiv",
    "detailInterface": "../Emmp_BankInfoDetail",
    "updateInterface": "../Emmp_BankInfoUpdate",

    //材料附件
    "loadUploadInterface": "../Sm_AttachmentCfgList",
    "attachmentBatchAddInterface": "../Sm_AttachmentBatchAdd",
    "attachmentListInterface": "../Sm_AttachmentList",
    //模态框
    "successModal": "#successM",
    "errorModal": "#errorM",
    //机构成员
    "addPersonModal": "#addEditBankPerson",
    // "deleteOrgMemberAlert": "#deleteEditBankOrgMember",
    // "editOrgMemberAlert":"#editOrgMember",
    "orgMemberListInterface": "../Emmp_OrgMemberList",
    "orgMemberAddInterface": "../Emmp_OrgMemberAdd",
    "orgMemberEditInterface": "../Emmp_OrgMemberUpdate",
    "orgMemberDeleteInterface": "../Emmp_OrgMemberDelete",
    "orgMemberBatchDeleteInterface": "../Emmp_OrgMemberBatchDelete",
    "baseParameterListInterface": "../Sm_BaseParameterList",
    "deleteOrgMemberAlert":"#deleteOrgMember",
});
