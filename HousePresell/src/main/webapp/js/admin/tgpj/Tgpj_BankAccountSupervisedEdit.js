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
            showDelete : true,  //附件是否可编辑
            //其他
            errMsg:"", //错误提示信息
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
            saveAttachment:saveAttachment,
        },
        computed: {},
        components: {
            'vue-nav': PageNavigationVue,
            "my-uploadcomponent": fileUpload,
        },
        watch:{
            // pageNumber : refresh,
            // selectItem : selectedItemChanged,
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
        getBuildingList()
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
                // detailVue.idState = theState2String(jsonObj.tgpj_BankAccountSupervised)
                detailVue.tgpj_BankAccountSupervisedModel = jsonObj.tgpj_BankAccountSupervised;
                // detailVue.developCompanyId=jsonObj.tgpj_BankAccountSupervisedModel.developCompany.tableId
                // detailVue.bankId=jsonObj.tgpj_BankAccountSupervisedModel.bankBranch.tableId
                // detailVue.tableId = jsonObj.tgpj_BankAccountSupervisedModel.tableId
                // console.log('developCompanyId is ' + detailVue.developCompanyId)
                // console.log('bankId is ' + detailVue.bankId)
            }
        });
    }

    //详情更新操作--------------获取"更新机构详情"参数
    function getUpdateForm() {
        return {
            //附件材料
            busiType : detailVue.busiType,
            sourceId : detailVue.tableId,
            generalAttachmentList : detailVue.$refs.listenUploadData.uploadData,

            interfaceVersion: detailVue.interfaceVersion,
            tableId: detailVue.tableId,
            theState: detailVue.tgpj_BankAccountSupervisedModel.theState,
            busiState: detailVue.tgpj_BankAccountSupervisedModel.busiState,
            theAccount: detailVue.tgpj_BankAccountSupervisedModel.theAccount,
            userStartId: detailVue.tgpj_BankAccountSupervisedModel.userStartId,
            userRecordId: detailVue.tgpj_BankAccountSupervisedModel.userRecordId,
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
        serverBodyRequest(baseInfo.updateInterface,detailVue.getUpdateForm(),function(jsonObj){
            generalSuccessModal()
            enterNewTabCloseCurrent(jsonObj.tableId,"监管账户详情","tgpj/Tgpj_BankAccountSupervisedDetail.shtml")
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

    function getBuildingList(){
        serverRequest(baseInfo.listInterface,searchListForm(),function (jsonObj) {
            detailVue.empj_BuildingInfoList=jsonObj.empj_BuildingInfoList
            detailVue.pageNumber=jsonObj.pageNumber
            detailVue.totalCount=jsonObj.totalCount
            detailVue.totalPage=jsonObj.totalPage
            detailVue.countPerPage=jsonObj.countPerPage
        })
    }

    function searchListForm()
    {
        return {
            interfaceVersion:detailVue.interfaceVersion,
            pageNumber:detailVue.pageNumber,
            countPerPage:detailVue.countPerPage,
            totalPage:detailVue.totalPage,
            keyword:detailVue.keyword,
            theState:detailVue.theState,
            userStartId:detailVue.userStartId,
            userRecordId:detailVue.userRecordId,
            developCompanyId:detailVue.developCompanyId,
            bankId:detailVue.bankId,
            bankBranchId:detailVue.bankBranchId,
            theSate:detailVue.theState,
            tableId:detailVue.tableId,
        }
    }

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

    //列表操作--------------获取“删除资源”表单参数
    function getDeleteForm() {
        return {
            interfaceVersion: detailVue.interfaceVersion,
            idArr: detailVue.selectItem
        }
    }

    function loadUpload(){
        generalLoadFile2(detailVue,detailVue.busiType)
    }

    function saveAttachment() {
        generalUploadFile(detailVue,"Tgpj_BankAccountSupervised",baseInfo.attachmentBatchAddInterface)
    }

    function initData() {
        getCompanyList()
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
    "detailDivId": "#tgpj_BankAccountSupervisedEditDiv",
    "detailInterface": "../Tgpj_BankAccountSupervisedDetail",
    "updateInterface": "../Tgpj_BankAccountSupervisedUpdate",
    "companyListInterface": "../Emmp_CompanyInfoForSelect",
    "bankListInterface": "../Emmp_BankBranchForSelect",
    "listInterface":"../Empj_BuildingInfoSuperviseList",
    "deleteInterface":"../Empj_BuildingInfoDelete",

    //材料附件
    "loadUploadInterface": "../Sm_AttachmentCfgList",
    "attachmentBatchAddInterface": "../Sm_AttachmentBatchAdd",
    "attachmentListInterface": "../Sm_AttachmentList",
    //模态框
    "successModel": "#successM",
    "errorModal": "#errorM",

});
