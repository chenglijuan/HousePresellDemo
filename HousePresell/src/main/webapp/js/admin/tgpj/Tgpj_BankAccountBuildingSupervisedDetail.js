(function (baseInfo) {
    var detailVue = new Vue({
        el: baseInfo.detailDivId,
        data: {
            interfaceVersion: 19000101,
            tgpj_BankAccountSupervisedModel:
                {
                    developCompany: {theName: ""},
                    userStart: {theName:""},
                    userRecord: {theName:""},
                    recordTimeStamp:"",
                    createTimeStamp:"",
                    bankBranch:{theName:""},
                },
            tableId: 1,
            recordDate:"",
            createDate:"",
            idState:"",
            empj_BuildingInfoList:[],
            pageNumber : 1,
            countPerPage : 20,
            totalPage : 1,
            totalCount : 1,
            keyword : "",
            selectItem: [],

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
            showDelete : false,  //附件是否可编辑

            superviseBuildingList:[],
            theState:"0",
        },
        methods: {
            //详情
            refresh: refresh,
            initData: initData,
            getSearchForm: getSearchForm,
            mainEditHandle: mainEditHandle,
            indexMethod: indexMethod,
            changePageNumber : function(data){
                detailVue.pageNumber = data;
            },
            getSuperviseBuildingList:getSuperviseBuildingList,
        },
        computed: {},
        components: {
            'vue-nav' : PageNavigationVue,
            "my-uploadcomponent":fileUpload,
        },
        watch: {
            pageNumber : refresh,
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
        getSuperviseBuildingList()
        getBuildingList()
        getDetail();
        loadUpload()
    }

    function getDetail() {
        new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function (jsonObj) {
            if (jsonObj.result != "success") {
                noty({"text": jsonObj.info, "type": "error", "timeout": 2000});
            }
            else {
                detailVue.recordDate=timeStamp2DayDate(detailVue.recordTimeStamp)
                detailVue.createDate=timeStamp2DayDate(detailVue.createTimeStamp)
                detailVue.tgpj_BankAccountSupervisedModel = jsonObj.tgpj_BankAccountSupervised;
                detailVue.idState=theState2String(jsonObj.tgpj_BankAccountSupervised)
                console.log('idState is '+detailVue.idState)
            }
        });
    }

    function mainEditHandle() {
        enterNewTabCloseCurrent(detailVue.tableId, "楼幢监管账号修改", "tgpj/Tgpj_BankAccountBuildingSupervisedEdit.shtml")
    }

    function getBuildingList(){
        serverRequest(baseInfo.listInterface,searchListForm(),function (jsonObj) {
            detailVue.empj_BuildingInfoList=jsonObj.empj_BuildingInfoList
            // detailVue.pageNumber=jsonObj.pageNumber
            // detailVue.totalCount=jsonObj.totalCount
            // detailVue.totalPage=jsonObj.totalPage
            // detailVue.countPerPage=jsonObj.countPerPage
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
            tableId:detailVue.tableId,
        }
    }

    function indexMethod(index) {
        return generalIndexMethod(index, detailVue)
    }

    //选中状态有改变，需要更新“全选”按钮状态
    function selectedItemChanged()
    {
        detailVue.isAllSelected = (detailVue.tgpj_BankAccountSupervisedList.length > 0)
            &&	(detailVue.selectItem.length == detailVue.tgpj_BankAccountSupervisedList.length);
    }

    function loadUpload() {
        // generalLoadFile(detailVue,"Tgpj_BankAccountBuildingSupervised",baseInfo.loadUploadInterface,baseInfo.errorModal)
        generalLoadFile2(detailVue,detailVue.busiType)
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
    "detailDivId": "#tgpj_BankAccountBuildingSupervisedDetailDiv",
    "detailInterface": "../Tgpj_BankAccountSupervisedDetail",
    "listInterface":"../Empj_BuildingInfoSuperviseList",
    "superviseBuildingList":"../Empj_BuildingAccountSupervisedList",

    //附件读取
    "loadUploadInterface":"../Sm_AttachmentCfgList",
    //模态框
    "errorModal": "#errorM",
});
