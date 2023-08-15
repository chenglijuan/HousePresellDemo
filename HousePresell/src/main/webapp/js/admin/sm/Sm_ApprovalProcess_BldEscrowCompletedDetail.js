(function(baseInfo){
    var detailVue = new Vue({
        el : baseInfo.detailDivId,
        data : {
            interfaceVersion :19000101,
            pageNumber : 1,
            countPerPage : 20,
            tableId : 0,
            eCode: "",
            eCodeFromDRAD: "",
            projectId: 0,
            projectName: "",
            remark: "",
            developCompanyName: "",
            cityRegionName: "",
            address: "",
            allBldEscrowSpace: 0.0,
            currentBldEscrowSpace: 0.0,
            userUpdateName: "",
            lastUpdateTimeStamp: "",
            userRecordName: "",
            recordTimeStamp: "",
            empj_BldEscrowCompleted_DtlList: [],
            selectItem : [],
            //附件材料
            busiType : "03030102",
            loadUploadList: [],
            showDelete : false,  //附件是否可编辑
            //----------审批流start-----------//
            afId:"",//申请单Id
            workflowId:"",//结点Id
            isNeedBackup:"",//是否需要备案
            sourcePage:"",//来源页面
            //----------审批流end-----------//
            
            webSite : "",
            hasFormula : "",
        },
        methods : {
            initData: initData,
            //详情
            refresh : refresh,
            getDetailForm : getDetailForm,
            showModal: showModal,
            approvalHandle: approvalHandle,
            indexMethod: function (index) {
                if (detailVue.pageNumber > 1) {
                    return (detailVue.pageNumber - 1) * detailVue.countPerPage - 0 + (index - 0 + 1);
                }
                if (detailVue.pageNumber <= 1) {
                    return index - 0 + 1;
                }
            },
            exportPdf:exportPdf,//导出pdf
            getExportForm : getExportForm,
            exportPdf2:exportPdf2,//导出pdf
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
    
    function getExportForm() {
  		var href = window.location.href;
  		return {
  			interfaceVersion : this.interfaceVersion,
  			sourceId : this.tableId,
  			reqAddress : href,
  			sourceBusiCode : this.busiType,
  		}
  	}
    //导出pdf
    function exportPdf(){
  		new ServerInterface(baseInfo.exportInterface).execute(detailVue.getExportForm(), function(jsonObj){
  			if (jsonObj.result != "success") {
  				$(baseInfo.edModelDivId).modal({
  					backdrop : 'static'
  				});
  				detailVue.errMsg = jsonObj.info;
  			} else {
  				window.open(jsonObj.pdfUrl,"_blank");
  				/*window.location.href = jsonObj.pdfUrl;*/
  			}
  		});
  	}
    
    function exportPdf2(){
    	
    	var model={
  			interfaceVersion : detailVue.interfaceVersion,
  			sourceId : detailVue.tableId,
  			reqAddress : window.location.href,
  			sourceBusiCode : "240180",
    	}
    	
  		new ServerInterface(baseInfo.exportInterface).execute(model, function(jsonObj){
  			if (jsonObj.result != "success") {
  				$(baseInfo.edModelDivId).modal({
  					backdrop : 'static'
  				});
  				detailVue.errMsg = jsonObj.info;
  			} else {
  				window.open(jsonObj.pdfUrl,"_blank");
  				/*window.location.href = jsonObj.pdfUrl;*/
  			}
  		});
  	}

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

    //获取选中复选框所在行的tableId
    function handleSelectionChange(val)
    {
        //此处处理要考虑已经办理托管终止的楼幢
        detailVue.selectItem = [];
        detailVue.bldEscrowCompletedSpace = 0.0;
        for (var index = 0; index < val.length; index++){
            var element = val[index].tableId;
            detailVue.selectItem.push(element);
            detailVue.bldEscrowCompletedSpace += val[index].escrowArea;
        }
        console.log(detailVue.selectItem);
    }


    //详情操作--------------获取"机构详情"参数
    function getDetailForm()
    {
        return {
            interfaceVersion:this.interfaceVersion,
            tableId:this.tableId,
            getDetailType:"1",
            afId:this.afId,
        }
    }

    //详情操作-------------
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
        // console.log(detailVue.getDetailForm());
        new ServerInterface(baseInfo.detailInterface).execute(detailVue.getDetailForm(), function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
                noty({"text":jsonObj.info,"type":"error","timeout":2000});
            }
            else
            {
                // console.log(jsonObj);

                detailVue.eCode = jsonObj.empj_BldEscrowCompleted.eCode;
                detailVue.eCodeFromDRAD = jsonObj.empj_BldEscrowCompleted.eCodeFromDRAD;
                detailVue.developCompanyId = jsonObj.empj_BldEscrowCompleted.developCompanyId;
                detailVue.developCompanyName = jsonObj.empj_BldEscrowCompleted.developCompanyName;
                detailVue.projectId = jsonObj.empj_BldEscrowCompleted.projectId;
                detailVue.projectName = jsonObj.empj_BldEscrowCompleted.projectName;
                detailVue.cityRegionName = jsonObj.empj_BldEscrowCompleted.cityRegionName;
                detailVue.address = jsonObj.empj_BldEscrowCompleted.address;
                detailVue.remark = jsonObj.empj_BldEscrowCompleted.remark;
                if('1' == jsonObj.empj_BldEscrowCompleted.hasFormula){
                	detailVue.hasFormula = "是";
                }else{
                	detailVue.hasFormula = "否";
                }
                detailVue.webSite = jsonObj.empj_BldEscrowCompleted.webSite;

                detailVue.userStartId = jsonObj.empj_BldEscrowCompleted.userStartId;
                detailVue.userUpdateName = jsonObj.empj_BldEscrowCompleted.userUpdateName;
                detailVue.lastUpdateTimeStamp = jsonObj.empj_BldEscrowCompleted.lastUpdateTimeStamp;
                detailVue.userRecordId = jsonObj.empj_BldEscrowCompleted.userRecordId;
                detailVue.userRecordName = jsonObj.empj_BldEscrowCompleted.userRecordName;
                detailVue.recordTimeStamp = jsonObj.empj_BldEscrowCompleted.recordTimeStamp;
                if (jsonObj.isNeedBackup != null)
                {
                    detailVue.isNeedBackup = jsonObj.isNeedBackup;
                    approvalModalVue.isNeedBackup = jsonObj.isNeedBackup;
                }

                detailVue.currentBldEscrowSpace = 0.0;
                detailVue.empj_BldEscrowCompleted_DtlList = jsonObj.empj_BldEscrowCompleted_DtlList;
                var listCount = jsonObj.empj_BldEscrowCompleted_DtlList.length;
                detailVue.countPerPage = 0;
                if (listCount > 0)
                {
                    detailVue.countPerPage = listCount;
                }

                for (var index = 0; index < detailVue.empj_BldEscrowCompleted_DtlList.length; index++){
                    var val = detailVue.empj_BldEscrowCompleted_DtlList[index];
                    var escrowArea = val.escrowArea;
                    if (escrowArea != null)
                    {
                        detailVue.currentBldEscrowSpace += escrowArea;
                    }
                }
                
                if(detailVue.currentBldEscrowSpace !=undefined && detailVue.currentBldEscrowSpace != ""){
                	detailVue.currentBldEscrowSpace = (detailVue.currentBldEscrowSpace).toFixed(2);
                }
                getBuildingInfoList();
            }
        });
    }

    //获取楼幢列表信息
    function getBuildingInfoList()
    {
        var model = {
            interfaceVersion:detailVue.interfaceVersion,
            projectId:detailVue.projectId,
        };
        new ServerInterface(baseInfo.getBuildingListInterface).execute(model, function(jsonObj) {
            if (jsonObj.result != "success")
            {
                noty({"text":jsonObj.info,"type":"error","timeout":2000});
            }
            else
            {
                detailVue.allBldEscrowSpace = 0.0;
                for (var index = 0; index < jsonObj.empj_BuildingInfoList.length; index++)
                {
                    var escrowArea = jsonObj.empj_BuildingInfoList[index].escrowArea;
                    if (escrowArea != null)
                    {
                        detailVue.allBldEscrowSpace += escrowArea;
                    }
                }
                
                if(detailVue.allBldEscrowSpace !=undefined && detailVue.allBldEscrowSpace !=""){
                	detailVue.allBldEscrowSpace = (detailVue.allBldEscrowSpace).toFixed(2)
                }
            }
        });
    }

    function initData()
    {
        var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
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
    "detailDivId":"#sm_ApprovalProcess_BldEscrowCompletedDetailDiv",
    "detailInterface":"../Empj_BldEscrowCompletedDetail",
    "getBuildingListInterface":"../Empj_BuildingInfoList",
    "exportInterface" : "../exportPDFByWord",
});
