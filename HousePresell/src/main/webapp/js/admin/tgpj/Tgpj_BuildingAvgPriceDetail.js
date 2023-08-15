(function (baseInfo) {
    var detailVue = new Vue({
        el: baseInfo.detailDivId,
        data: {
            interfaceVersion: 19000101,
            //附件材料
            busiType: '03010301',
            dialogVisible: false,
            dialogImageUrl: "",
            fileType: "",
            fileList: [],
            showButton: false,
            hideShow: true,
            uploadData: [],
            smAttachmentList: [],//页面显示已上传的文件
            uploadList: [],
            loadUploadList: [],
            showDelete: false,  //附件是否可编辑
            showsubmit : true,
            //其他
            errMsg: "", //错误提示信息

            tgpj_BuildingAvgPriceModel: {
                buildingInfo: {
                    eCode: "",
                    eCodeFromConstruction: "",
                    eCodeFromPublicSecurity: "",
                    project: {
                        theName: "",
                    },

                },
                userStart: {
                    theName: "",
                    company: {
                        theName: "",
                        eCode: "",
                        eCodeFromPublicSecurity: "",
                    }
                },
                userRecord: {
                    theName: "",
                },
            },
            tableId: 1,

            //附件材料
            loadUploadList: [],
            showDelete: false,  //附件是否可编辑

            presellPrice:"",
            companyId:"",

            isShowPresell:false,
        },
        methods: {
            //详情
            refresh: refresh,
            initData: initData,
            getSearchForm: getSearchForm,
            mainEditHandle: mainEditHandle,
            submitHandle : submitHandle,
        },
        computed: {},
        components: {
            "my-uploadcomponent": fileUpload
        },
        watch: {}
    });

    //------------------------方法定义-开始------------------//
    //----------提交
    function submitHandle()
    {
    	detailVue.showsubmit = false;
    	
    	var model = {
			interfaceVersion: detailVue.interfaceVersion,
	        tableId: detailVue.tableId,
	        buttonType:'2',
    	}
    	
    	new ServerInterface(baseInfo.submitInterface).execute(model, function (jsonObj) {
    		detailVue.showsubmit = true;
            if (jsonObj.result != "success") {
            	generalErrorModal(jsonObj);
            }
            else {
            	generalSuccessModal();
            	detailVue.initData();
            }
        });
    	 
    	
    }
    
    //详情操作--------------获取"机构详情"参数
    function getSearchForm() {
        return {
            interfaceVersion: detailVue.interfaceVersion,
            tableId: detailVue.tableId,
            reqSource: "详情",
        }
    }

    //详情操作--------------
    function refresh() {
        if (detailVue.tableId == null || detailVue.tableId < 1) {
            return;
        }

        getDetail();
        loadUpload()
    }

    function getDetail() {
        new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function (jsonObj) {
            if (jsonObj.result != "success") {
                noty({"text": jsonObj.info, "type": "error", "timeout": 2000});
            }
            else {
                detailVue.tgpj_BuildingAvgPriceModel = jsonObj.tgpj_BuildingAvgPrice;
                detailVue.tgpj_BuildingAvgPriceModel.recordAveragePrice = addThousands(jsonObj.tgpj_BuildingAvgPrice.recordAveragePrice);
                if(jsonObj.tgpj_BuildingAvgPrice.recordAveragePriceFromPresellSystem!=undefined){
                    detailVue.presellPrice = addThousands(jsonObj.tgpj_BuildingAvgPrice.recordAveragePriceFromPresellSystem)
                }
                // getPresellPrice(jsonObj.tgpj_BuildingAvgPrice.buildingInfoId)
            }
        });
    }

    function getPresellPrice(buildingId) {
        serverRequest("../Empj_BuildingGetPresellPrice",{interfaceVersion:19000101,tableId:buildingId},function (jsonObj) {
            detailVue.presellPrice=addThousands(jsonObj.presellPrice)
        })
    }

    function mainEditHandle() {
        enterNewTabCloseCurrent(detailVue.tableId, "备案均价修改", "tgpj/Tgpj_BuildingAvgPriceEdit.shtml")
    }

    function loadUpload() {
        generalLoadFile2(detailVue, detailVue.busiType)
    }

    function initData() {
        getIdFormTab("", function (id) {
            detailVue.tableId = id
            refresh()
        })
        getLoginUserInfo(function (user) {
            if (user.theType == "1") {
                detailVue.isShowPresell = true
            } else {
                detailVue.isShowPresell = false
            }
        })
    }

    //------------------------方法定义-结束------------------//

    //------------------------数据初始化-开始----------------//
    // detailVue.refresh();
    detailVue.initData();
    //------------------------数据初始化-结束----------------//
})({
    "detailDivId": "#tgpj_BuildingAvgPriceDetailDiv",
    "detailInterface": "../Tgpj_BuildingAvgPriceDetail",

    //附件读取
    "loadUploadInterface": "../Sm_AttachmentCfgList",
   //提交
    "submitInterface": "../Tgpj_BuildingAvgPriceApprovalProcess",
    //模态框
    "errorModal": "#errorM",
});
