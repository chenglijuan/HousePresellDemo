(function (baseInfo) {
    var detailVue = new Vue({
        el: baseInfo.addDivId,
        data: {
            interfaceVersion: 19000101,
            emmp_BankInfoModel: {},
            errMsg: "",

            //附件材料
            busiType: '020201',
            loadUploadList: [],
            showDelete: true,  //附件是否可编辑
            theTypeList: [
                {tableId: "01", theName: "开发单位"},
                {tableId: "02", theName: "代理公司"},
                {tableId: "03", theName: "进度见证服务单位"},
                {tableId: "04", theName: "合作机构"}
            ],

            //机构成员相关
            pageNumber: 1,
            countPerPage: 10,
            totalPage: 1,
            totalCount: 1,
            theState: 0,//正常为0，删除为1
            selectItem: [],
            orgMemberList: [],

            theOrgMemberId: 1,

            //机构成员操作相关
            addTheName: "",
            addIdType: "",
            addIdNumber: "",
            addTheNameOfDepartment: "",
            parameterNameList: [],
            addParameterNameId: "",
            addParameterName: "",
            addPositionName: "",
            addPhoneNumber: "",
            addEmail: "",
            addQq: "",
            addWeixin: "",
        },
        methods: {
            //详情
            refresh: refresh,
            initData: initData,
            getSearchForm: getSearchForm,
            //添加
            getAddForm: getAddForm,
            add: add,

            //机构成员列表
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
            "my-uploadcomponent": fileUpload,
        },
        watch: {}
    });

    //------------------------方法定义-开始------------------//
    //详情操作--------------获取"机构详情"参数
    function getSearchForm() {
        return {
            interfaceVersion: detailVue.interfaceVersion,
        }
    }

    //详情操作--------------
    function refresh() {

    }

    //详情更新操作--------------获取"更新机构详情"参数
    function getAddForm() {
        return {
            interfaceVersion: detailVue.interfaceVersion,
            theState: detailVue.emmp_BankInfoModel.theState,
            busiState: detailVue.emmp_BankInfoModel.busiState,
            eCode: detailVue.emmp_BankInfoModel.eCode,
            userStartId: detailVue.emmp_BankInfoModel.userStartId,
            createTimeStamp: detailVue.emmp_BankInfoModel.createTimeStamp,
            lastUpdateTimeStamp: detailVue.emmp_BankInfoModel.lastUpdateTimeStamp,
            userRecordId: detailVue.emmp_BankInfoModel.userRecordId,
            recordTimeStamp: detailVue.emmp_BankInfoModel.recordTimeStamp,
            theName: detailVue.emmp_BankInfoModel.theName,
            shortName: detailVue.emmp_BankInfoModel.shortName,
            leader: detailVue.emmp_BankInfoModel.leader,
            address: detailVue.emmp_BankInfoModel.address,
            capitalCollectionModel: detailVue.emmp_BankInfoModel.capitalCollectionModel,
            theType: detailVue.emmp_BankInfoModel.theType,
            postalAddress: detailVue.emmp_BankInfoModel.postalAddress,
            postalPort: detailVue.emmp_BankInfoModel.postalPort,
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
            bankCode: detailVue.emmp_BankInfoModel.bankCode,
            bankNo: detailVue.emmp_BankInfoModel.bankNo,
            //机构成员
            orgMemberList: this.orgMemberList,

            //附件材料
            busiType: detailVue.busiType,
            generalAttachmentList: this.$refs.listenUploadData.uploadData,
        }
    }

    function add() {
        serverBodyRequest(baseInfo.addInterface,detailVue.getAddForm(),function (jsonObj) {
            generalSuccessModal()
            enterNewTabCloseCurrent(jsonObj.tableId, '金融机构详情', 'emmp/Emmp_BankInfoDetail.shtml')
        })
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
        serverRequest(baseInfo.baseParameterListInterface,model,function (jsonObj) {
            detailVue.parameterNameList = jsonObj.sm_BaseParameterList;
        })
    }

    function addOrgMember(divStr) {
        var isPass = errorCheckForAll(divStr);
        if (!isPass) {
            console.log("PassFailed");
            return;
        }
        for (var i = 0; i < detailVue.parameterNameList.length; i++) {
            var parameter = detailVue.parameterNameList[i];
            if (parameter.tableId == detailVue.addParameterNameId) {
                detailVue.addParameterName = parameter.theValue;
                break;
            }
        }
        var model = {
            theName: detailVue.addTheName,
            idType: detailVue.addIdType,
            idNumber: detailVue.addIdNumber,
            theNameOfDepartment: detailVue.addTheNameOfDepartment,
            parameterNameTableId: detailVue.addParameterNameId,
            parameterName: detailVue.addParameterName,
            positionName: detailVue.addPositionName,
            phoneNumber: detailVue.addPhoneNumber,
            email: detailVue.addEmail,
            qq: detailVue.addQq,
            weixin: detailVue.addWeixin,
            tableId: detailVue.theOrgMemberId,
        };

        detailVue.orgMemberList.push(model);

        detailVue.theOrgMemberId += 1; //序号自增长

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
        var newOrgMemberList = [];
        for (var i = 0; i < detailVue.orgMemberList.length; i++) {
            var needDelete = false;

            for (var j = 0; j < detailVue.selectItem.length; j++) {
                var theId = detailVue.selectItem[j];
                if (detailVue.orgMemberList[i].tableId == theId) {
                    needDelete = true;
                }
            }
            if (needDelete == false) {
                newOrgMemberList.push(detailVue.orgMemberList[i]);
            }
        }
        detailVue.orgMemberList = newOrgMemberList;
        $(baseInfo.deleteOrgMemberAlert).modal('hide');
    }


    function listItemSelectHandle(list) {
        generalListItemSelectHandle(detailVue, list)
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

    function initData() {
        getParameterNameList();		//获取机构成员职务字段在通用字段表里的数据
        generalLoadFile2(detailVue, detailVue.busiType)
    }

    //------------------------方法定义-结束------------------//

    //------------------------数据初始化-开始----------------//
    detailVue.refresh();
    detailVue.initData();
    //------------------------数据初始化-结束----------------//
})({
    "addDivId": "#emmp_BankInfoDiv",
    "detailInterface": "../Emmp_BankInfoDetail",
    "addInterface": "../Emmp_BankInfoAdd",

    //其他
    "successModal": "#successM",
    "errorModal": "#errorM",

    //机构成员
    "addPersonModal": "#addAddBankPerson",
    "deleteOrgMemberAlert": "#deleteAddBankOrgMember",
    "baseParameterListInterface": "../Sm_BaseParameterList",
});
