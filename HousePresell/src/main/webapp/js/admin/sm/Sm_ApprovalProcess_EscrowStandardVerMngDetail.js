(function(baseInfo){
    var detailVue = new Vue({
        el : baseInfo.detailDivId,
        data : {
            interfaceVersion :19000101,
            tableId : 1,
            approvalState: "",
            theName: "",
            theVersion: "",
            theType: "",
            theContent: "",
            beginEndExpirationDate: "",
            hasEnable: 0,
            userUpdateName: "",
            lastUpdateTimeStamp: "",
            userRecordName: "",
            recordTimeStamp: "",
            //附件材料
            busiType : '06010101',
            loadUploadList: [],
            showDelete : false,
            //----------审批流start-----------//
            afId:"",//申请单Id
            workflowId:"",//结点Id
            isNeedBackup:"",//是否需要备案
            sourcePage:"",//来源页面
            //----------审批流end-----------//
        },
        methods : {
            //详情
            refresh : refresh,
            initData: initData,
            getSearchForm : getSearchForm,
            showModal: showModal,
            approvalHandle: approvalHandle,
        },
        computed:{

        },
        components : {
            "my-uploadcomponent":fileUpload
        },
        watch:{

        }
    });

    //------------------------方法定义-开始------------------//

    //按钮操作----审批项目信息
    function showModal()
    {
        approvalModalVue.getModalWorkflowList();
    }

    //按钮操作----备案项目信息
    function approvalHandle()
    {
        approvalModalVue.buttonType = 2;
        approvalModalVue.approvalHandle();
    }

    //详情操作--------------获取"机构详情"参数
    function getSearchForm()
    {
        return {
            interfaceVersion:this.interfaceVersion,
            tableId:this.tableId,
            getDetailType:"1",
            afId:this.afId,
        }
    }

    //详情操作--------------
    function refresh()
    {
        if (detailVue.tableId == null || detailVue.tableId < 1)
        {
            return;
        }

        getDetail();
    }

    function getDetail()
    {
        new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
                noty({"text":jsonObj.info,"type":"error","timeout":2000});
            }
            else
            {
                console.log(jsonObj);
                var detailModel = jsonObj.tgpj_EscrowStandardVerMng;
                detailVue.theName = detailModel.theName;
                detailVue.theVersion = detailModel.theVersion;
                detailVue.theType = detailModel.theType;
                detailVue.theContent = detailModel.theContent;
                detailVue.hasEnable = detailModel.hasEnable;
                detailVue.beginEndExpirationDate = detailModel.beginExpirationDate + " - " + detailModel.endExpirationDate;
                detailVue.userUpdateName = detailModel.userUpdateName;
                detailVue.lastUpdateTimeStamp = detailModel.lastUpdateTimeStamp;
                detailVue.userRecordName = detailModel.userRecordName;
                detailVue.recordTimeStamp = detailModel.recordTimeStamp;
                detailVue.approvalState = detailModel.approvalState;
                if (jsonObj.isNeedBackup != null)
                {
                    detailVue.isNeedBackup = jsonObj.isNeedBackup;
                    approvalModalVue.isNeedBackup = jsonObj.isNeedBackup;
                }
            }
        });
    }

    function initData()
    {
        // getIdFormTab("", function (id){
        //     detailVue.tableId = id;
        //     refresh();
        // });
        // console.log("进入托管标准版本审批详情页面")
        var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
        console.log(tableIdStr);
        var array = tableIdStr.split("_");
        if (array.length > 1)
        {
            detailVue.tableId = array[array.length-4];
            detailVue.afId = array[array.length-3];
            detailVue.workflowId = array[array.length-2];
            detailVue.sourcePage = array[array.length-1];

            approvalModalVue.afId = detailVue.afId;
            approvalModalVue.workflowId = detailVue.workflowId;
            approvalModalVue.sourcePage = detailVue.sourcePage;
            refresh();
        }

        generalLoadFile2(detailVue, detailVue.busiType);
    }
    //------------------------方法定义-结束------------------//

    //------------------------数据初始化-开始----------------//
    detailVue.initData();
    //------------------------数据初始化-结束----------------//
})({
    "detailDivId":"#sm_ApprovalProcess_EscrowStandardVerMngDetailDiv",
    "detailInterface":"../Tgpj_EscrowStandardVerMngDetail"
});
