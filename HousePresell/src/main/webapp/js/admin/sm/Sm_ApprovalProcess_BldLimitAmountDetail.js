(function (baseInfo) {
    var updateVue = new Vue({
        el: baseInfo.detailDivId,
        data: {
            interfaceVersion: 19000101,
            tableId: 1,
            // empj_BuildingInfo : {
            // 	expectBuilding : {},
            // },
            empj_BldLimitAmount_AFModel: {},
            empj_BldLimitAmount_AFNew: {},
            //附件材料
            busiType: '03030101',
            loadUploadList: [],
            showDelete: false,
            buttonType: "",//按钮来源（保存、提交）
            trusteeshipContent: "",
            //UI控制
            acceptExplainControlModel:{},
            appointExplainControlModel:{},
            sceneInvestigationExplainControlModel:{},
            isAcceptExplainDisable:true,
            isAppointExplainDisable:true,
            isSceneInvestigationExplainDisable:true,
            //----------审批流start-----------//
            afId:"",//申请单Id
            workflowId:"",//结点Id
            isNeedBackup:"",//是否需要备案
            sourcePage:"",//来源页面
            //----------审批流end-----------//
        },
        methods: {
            //详情
            refresh: refresh,
            initData: initData,
            getSearchForm: getSearchForm,
            showModal: showModal,
            approvalHandle: function () {
                approvalModalVue.buttonType = 2;
                approvalModalVue.approvalHandle();
            },
            exportPdf:exportPdf,//导出pdf
            getExportForm : getExportForm,
        },
        computed: {},
        components: {
            "my-uploadcomponent": fileUpload,
            'vue-select': VueSelect,
        },
        watch: {}
    });

    //------------------------方法定义-开始------------------//
    function getExportForm() {
  		var href = window.location.href;
  		return {
  			interfaceVersion : this.interfaceVersion,
  			sourceId : this.tableId,
  			reqAddress : href,
  			sourceBusiCode : "03030101",
  		}
  	}
    
    function exportPdf(){
  		new ServerInterface(baseInfo.exportInterface).execute(updateVue.getExportForm(), function(jsonObj){
  			if (jsonObj.result != "success") {
  			} else {
  				window.open(jsonObj.pdfUrl,"_blank");
  			}
  		});
  	}
    
    //详情操作--------------获取"机构详情"参数
    function getSearchForm() {
        return {
            interfaceVersion: this.interfaceVersion,
            tableId: this.tableId,
            sourcePage:approvalModalVue.sourcePage,
            afId:this.afId,
        }
    }

    //详情操作--------------
    function refresh() {
        if (updateVue.tableId == null || updateVue.tableId < 1) {
            return;
        }

        getDetail();

        generalLoadFile2(updateVue, updateVue.busiType)
    }

    function getDetail() {
        serverRequest(baseInfo.detailInterface, updateVue.getSearchForm(), function (jsonObj) {
            updateVue.empj_BldLimitAmount_AFModel = jsonObj.empj_BldLimitAmount_AF;
            updateVue.empj_BldLimitAmount_AFNew = jsonObj.empj_BldLimitAmount_AFNew;
            updateVue.trusteeshipContent = jsonObj.trusteeshipContent;
            updateVue.isNeedBackup = jsonObj.isNeedBackup;
            approvalModalVue.isNeedBackup = updateVue.isNeedBackup;
            updateVue.acceptExplainControlModel=jsonObj.acceptExplainControlModel;
            updateVue.appointExplainControlModel=jsonObj.appointExplainControlModel;
            updateVue.sceneInvestigationExplainControlModel=jsonObj.sceneInvestigationExplainControlModel;
            updateVue.isAcceptExplainDisable=jsonObj.acceptExplainControlModel.needDisable;
            updateVue.isAppointExplainDisable=jsonObj.appointExplainControlModel.needDisable;
            updateVue.isSceneInvestigationExplainDisable=jsonObj.sceneInvestigationExplainControlModel.needDisable;
            updateVue.showDelete=!(jsonObj.sceneInvestigationExplainControlModel.needDisable);

            $('#date0303010101').val(updateVue.empj_BldLimitAmount_AFModel.appointTimeString);

            laydate.render({
                elem: '#date0303010101', //指定元素
                type: 'datetime',
                format:'yyyy-MM-dd HH:mm',
                // btns: ['clear', 'confirm'],
                // ready:formatminutes,
            });

            changeThousands()
        })
        // new ServerInterface(baseInfo.detailInterface).execute(updateVue.getSearchForm(), function(jsonObj)
        // {
        // 	if(jsonObj.result != "success")
        // 	{
        // 		generalErrorModal(jsonObj);
        // 	}
        // 	else
        // 	{
        // 		updateVue.empj_BldLimitAmount_AFModel = jsonObj.empj_BldLimitAmount_AFModel;
        // 	}
        // });
    }

    function initData() {
        var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
        var array = tableIdStr.split("_");
        if (array.length > 1) {
            updateVue.tableId = array[array.length-4];
            updateVue.afId = array[array.length-3];
            updateVue.workflowId = array[array.length-2];
            updateVue.sourcePage = array[array.length-1];

            approvalModalVue.afId = updateVue.afId;
            approvalModalVue.workflowId = updateVue.workflowId;
            approvalModalVue.sourcePage = updateVue.sourcePage;
            refresh();
        }
    }

    function changeThousands() {
        // updateVue.empj_BldLimitAmount_AFModel.recordAveragePriceOfBuilding=addThousands(updateVue.empj_BldLimitAmount_AFModel.recordAveragePriceOfBuilding)
        updateVue.empj_BldLimitAmount_AFModel.recordAveragePriceOfBuilding = addThousands(updateVue.empj_BldLimitAmount_AFModel.recordAveragePriceOfBuilding)
        updateVue.empj_BldLimitAmount_AFModel.orgLimitedAmount = addThousands(updateVue.empj_BldLimitAmount_AFModel.orgLimitedAmount)
        updateVue.empj_BldLimitAmount_AFModel.nodeLimitedAmount = addThousands(updateVue.empj_BldLimitAmount_AFModel.nodeLimitedAmount)
        updateVue.empj_BldLimitAmount_AFModel.effectiveLimitedAmount = addThousands(updateVue.empj_BldLimitAmount_AFModel.effectiveLimitedAmount)
        updateVue.empj_BldLimitAmount_AFModel.totalGuaranteeAmount = addThousands(updateVue.empj_BldLimitAmount_AFModel.totalGuaranteeAmount)
        updateVue.empj_BldLimitAmount_AFModel.expectEffectLimitedAmount = addThousands(updateVue.empj_BldLimitAmount_AFModel.expectEffectLimitedAmount)
        updateVue.empj_BldLimitAmount_AFModel.cashLimitedAmount = addThousands(updateVue.empj_BldLimitAmount_AFModel.cashLimitedAmount)
        updateVue.empj_BldLimitAmount_AFModel.expectLimitedAmount = addThousands(updateVue.empj_BldLimitAmount_AFModel.expectLimitedAmount)
    }

    function showModal() {
        approvalModalVue.extraObj = {
            sourceType: "Empj_BldLimitAmount_AF",
            tableId: updateVue.tableId,
            acceptExplain: updateVue.empj_BldLimitAmount_AFModel.acceptExplain,
            appointExplain: updateVue.empj_BldLimitAmount_AFModel.appointExplain,
            // appointTimeString: updateVue.empj_BldLimitAmount_AFModel.appointTimeString,
            appointTimeString: $('#date0303010101').val(),
            sceneInvestigationExplain: updateVue.empj_BldLimitAmount_AFModel.sceneInvestigationExplain,
            attachMent:this.$refs.listenUploadData.uploadData,
        }
        approvalModalVue.getModalWorkflowList();
    }

    //------------------------方法定义-结束------------------//

    //------------------------数据初始化-开始----------------//
    updateVue.initData();
    //------------------------数据初始化-结束----------------//
})({
    "detailDivId": "#sm_ApprovalProcess_BldLimitAmountDetailDiv",
    "detailInterface": "../Empj_BldLimitAmount_AFDetail",
    "updateInterface": "../Empj_BldLimitAmount_AFUpdate",
    "exportInterface":"../exportPDFByWord",//导出pdf
});
