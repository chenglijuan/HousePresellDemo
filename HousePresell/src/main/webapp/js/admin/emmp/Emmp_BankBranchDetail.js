(function (baseInfo) {
    var detailVue = new Vue({
        el: baseInfo.detailDivId,
        data: {
            interfaceVersion: 19000101,
            emmp_BankBranchModel: {generalBankName:""},
            tableId: 1,
            //附件材料
			busiType:'020202',
            dialogVisible: false,
            dialogImageUrl: "",
            fileType:"",
            fileList : [],
            showButton:false,
            hideShow:true,
            uploadData : [],
            smAttachmentList:[],//页面显示已上传的文件
            uploadList:[],
            //附件材料
            loadUploadList: [],
            showDelete : false,  //附件是否可编辑
            //其他
            errMsg:"", //错误提示信息e
        },
        methods: {
            //详情
            refresh: refresh,
            initData: initData,
            dateFormat:dateFormat,
            getSearchForm: getSearchForm,
            bankBranchEditHandle:bankBranchEditHandle,
        },
        computed: {},
        components: {
            "my-uploadcomponent":fileUpload
        },
        watch: {}
    });

    //------------------------方法定义-开始------------------//
    //详情操作--------------获取"机构详情"参数
    function getSearchForm() {
        return {
            interfaceVersion: this.interfaceVersion,
            tableId: detailVue.tableId,
        }
    }

    //详情操作--------------
    function refresh() {
        // console.log('进入 refresh tableId is ' + detailVue.tableId)
        if (detailVue.tableId == null || detailVue.tableId < 1) {
            console.log('detailVue.tableId == null || detailVue.tableId < 1')
            return;
        }

        getDetail();
        loadUpload()
    }

    function getDetail() {
        // console.log('进入getDetail')
        // console.log('form table id is ' + detailVue.getSearchForm().tableId)
        new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function (jsonObj) {
            if (jsonObj.result != "success") {
                noty({"text": jsonObj.info, "type": "error", "timeout": 2000});
            }
            else {
                detailVue.emmp_BankBranchModel = jsonObj.emmp_BankBranch;
            }
        });
    }

    function initData() {
        getIdFormTab("",function (tableId) {
            detailVue.tableId=tableId
            refresh()
        })
    }

    function dateFormat(date) {
        return moment(date).format("YYYY-MM-DD hh:mm:ss");
    }
    
    function bankBranchEditHandle() {
        enterNewTabCloseCurrent(detailVue.tableId,"开户行详情修改",'emmp/Emmp_BankBranchEdit.shtml')
    }

    function loadUpload() {
        // generalLoadFile(detailVue,"Emmp_BankBranch",baseInfo.loadUploadInterface,baseInfo.errorModal)
        generalLoadFile2(detailVue,detailVue.busiType)
    }

    //------------------------方法定义-结束------------------//

    //------------------------数据初始化-开始----------------//
    // detailVue.refresh();
    detailVue.initData();
    //------------------------数据初始化-结束----------------//
})({
    "detailDivId": "#emmp_BankBranchDetailDiv",
    "detailInterface": "../Emmp_BankBranchDetail",

    //附件读取
    "loadUploadInterface":"../Sm_AttachmentCfgList",
    //模态框
    "errorModal": "#errorM",
});
