(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tgxy_EscrowAgreementModel: {
				disputeResolution:'',
			},
			tableId : 1,
			
			tgxy_EscrowAgreementDetailList:[],
			creatUserUpdate:[],
			creatUserRecord:[],
			creatCityRegion:[],
			errEscrowAdd:"",
			smAttachmentList:[],//页面显示已上传的文件
			loadUploadList : [],
			showDelete : false,
			busiType : '06110201',
			afId:"",
		    workflowId: "",
		    af_busiCode:"06110201",
		    sm_ApprovalProcess_WorkflowList : [],
		    sm_ApprovalProcess_RecordList : [],
		    sm_ApprovalProcess_Handle :{},
		    selectState : "1",
		    isEdit : "0",
		    isNeedBackup:"",//是否需要备案
            sourcePage:"",//来源页面
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			EscrowAgreementEdit : EscrowAgreementEdit,
			indexMethod:indexMethod,
			errClose:errClose,
			showModal: showModal,
			approvalHandle: approvalHandle,
			editPic :editPic,
			savePic :savePic,
			exportPdf : exportPdf,
			getExportForm : getExportForm,
			bldLimitAmountDetail : bldLimitAmountDetail,//跳转受限额度版本
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
	function bldLimitAmountDetail(tableId){
//		enterNextTab(tableId, '受限额度节点详情', 'tgpj/Tgpj_BldLimitAmountVer_AFDetail.shtml',tableId+"06110201");
		enterNewTab(tableId, "受限额度节点详情", "tgpj/Tgpj_BldLimitAmountVer_AFDetail.shtml")
	}
	
	function getExportForm() {
		var href = window.location.href;
		return {
			interfaceVersion : detailVue.interfaceVersion,
			sourceId : detailVue.tableId,
			reqAddress : href,
			sourceBusiCode : detailVue.busiType,
		}
	}
	
	function exportPdf()
	{
		new ServerInterface(baseInfo.exportInterface).execute(detailVue.getExportForm(), function(jsonObj){
			if (jsonObj.result != "success") {
				$(baseInfo.edModelDivId).modal({
					backdrop : 'static'
				});
				detailVue.errMsg = jsonObj.info;
			} else {
				window.open(jsonObj.pdfUrl,"_blank");
			}
		});
	}
	
	function savePic() {
		
		detailVue.isEdit = "0";
		detailVue.showDelete = false;
		
		var fileUploadList = detailVue.$refs.listenUploadData.uploadData;
		fileUploadList = JSON.stringify(fileUploadList);
		
		var model=
		{
            interfaceVersion:detailVue.interfaceVersion,
            busiType : detailVue.busiType,
            //附件材料
            sm_AttachmentList : fileUploadList,
            picTableId : detailVue.tableId,
            
		}
		new ServerInterface(baseInfo.editPicInterface).execute(model, function(jsonObj)
				{
					if(jsonObj.result != "success")
					{
							detailVue.errEscrowAdd = jsonObj.info;
			                $(baseInfo.errorModel).modal('show', {
							    backdrop :'static'
						    });
					}
					else
					{
						getDetail();
						
					}
				});
		
		
		
	}
	
	function editPic() {
		
		detailVue.showDelete = true;
		detailVue.isEdit = "1";
		getDetail();
		
	}
	
	function indexMethod(index) {
		return generalIndexMethod(index, detailVue)
	}
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:this.tableId,
			afId: this.afId,
		    workflowId: this.workflowId
		}
	}

	//详情操作--------------
	function refresh()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var array = tableIdStr.split("_");
		detailVue.tableId = array[2];
		detailVue.afId = array[3];
		detailVue.workflowId = array[4];
		if(detailVue.tableId == null || detailVue.tableId < 1) {
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
					detailVue.errEscrowAdd = jsonObj.info;
	                $(baseInfo.errorModel).modal('show', {
					    backdrop :'static'
				    });
			}
			else
			{
				detailVue.tgxy_EscrowAgreementModel = jsonObj.tgxy_EscrowAgreement;
				detailVue.creatCityRegion = jsonObj.tgxy_EscrowAgreement.cityRegion;
				detailVue.creatUserUpdate = jsonObj.tgxy_EscrowAgreement.userUpdate;
				detailVue.creatUserRecord = jsonObj.tgxy_EscrowAgreement.userRecord;
				detailVue.tgxy_EscrowAgreementDetailList = jsonObj.tgxy_EscrowAgreement.buildingInfoList;
				
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
			}
		});
	}
	
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}
	
	function initData()
	{
		
		initButtonList();
		
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var array = tableIdStr.split("_");
		if (array.length > 1)
		{
			detailVue.tableId = array[array.length-4];
			detailVue.afId = array[array.length-3];
			if(array[array.length-2]!=null)
			{
				detailVue.workflowId = array[array.length-2];
			}
			
			approvalModalVue.sourcePage = array[array.length-1]
			approvalModalVue.workflowId = detailVue.workflowId;
			approvalModalVue.afId = detailVue.afId;
			approvalModalVue.busiCode = detailVue.af_busiCode;
			detailVue.sourcePage = approvalModalVue.sourcePage;
			refresh();
		}
	}
	//编辑跳转方法
	function EscrowAgreementEdit(){
		$("#tabContainer").data("tabs").addTab({
			id: detailVue.tableId , 
			text: '修改托管合作协议', 
			closeable: true, 
			url: 'tgxy/Tgxy_EscrowAgreementEdit.shtml'
		});
	}
	function errClose()
	{
		$(baseInfo.errorModel).modal('hide');
	}
	
	function showModal()
	{
//		var model =
//			{
//                interfaceVersion:detailVue.interfaceVersion,
//                approvalProcess_AFId : detailVue.afId,
//                busiCode:detailVue.af_busiCode,
//			}
//        new ServerInterface(baseInfo.approvalModalListInterface).execute(model, function(jsonObj)
//        {
//            if(jsonObj.result != "success")
//            {
//            	generalErrorModal(jsonObj);
//            }
//            else
//            {
//            	detailVue.sm_ApprovalProcess_WorkflowList = jsonObj.sm_ApprovalProcess_WorkflowList; // 结点信息
//            	detailVue.sm_ApprovalProcess_RecordList = jsonObj.sm_ApprovalProcess_RecordList; //审批记录
//            }
//        });
		
		approvalModalVue.getModalWorkflowList();

    }
	
	
	
	function approvalHandle(){
		var model=
			{
                interfaceVersion:detailVue.interfaceVersion,
                approvalProcessId:detailVue.workflowId, //流程Id
                theContent:detailVue.sm_ApprovalProcess_Handle.theContent,
                theAction:detailVue.sm_ApprovalProcess_Handle.theAction,
                operateTimeStamp : detailVue.sm_ApprovalProcess_Handle.operateTimeStamp,
				informPerson : detailVue.informPerson,
                informTitle :detailVue.sm_ApprovalProcess_Handle.informTitle,
                informContent:detailVue.sm_ApprovalProcess_Handle.informContent,
                busiType : detailVue.busiCode,
                //附件材料
                generalAttachmentList : this.$refs.listenUploadData.uploadData,
			}
			if(detailVue.sm_ApprovalProcess_Handle.theAction == 0) //通过操作
			{
                new ServerInterfaceJsonBody(baseInfo.passInterface).execute(model, function(jsonObj)
                {
                    if(jsonObj.result != "success")
                    {
                    	generalErrorModal(jsonObj);
                    }
                    else
					{
                        $('#examine1').modal('hide');
                        enterNewTab(null, "代办流程", "sm/Sm_ApprovalProcess_AgencyList.shtml");
					}
                });
			}
			else if(detailVue.sm_ApprovalProcess_Handle.theAction == 1)//驳回操作
			{
                new ServerInterfaceJsonBody(baseInfo.rejectInterface).execute(model, function(jsonObj)
                {
                    if(jsonObj.result != "success")
                    {
                    	generalErrorModal(jsonObj);
                    }
                    else
					{
                        $('#examine1').modal('hide');
                        enterNewTab(null, "代办流程", "sm/Sm_ApprovalProcess_AgencyList.shtml");
					}
                });
			}
	}
	
	
	
//	function succClose()
//	{
//		$(baseInfo.successModel).modal('hide');
//	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"errorModel":"#errorEscrowAdd1",
//	"successModel":"#successEscrowAdd",
	"detailDivId":"#sm_EscrowAgreementDetailDiv",
	"detailInterface":"../Tgxy_EscrowAgreementDetail",
	"approvalModalListInterface":"../Sm_ApprovalProcess_ModalList",
	"passInterface":"../Sm_ApprovalProcess_Pass",  //通过
	"rejectInterface":"../Sm_ApprovalProcess_Reject",//驳回
	"editPicInterface":"../Sm_AttachmentCommonUpdate",
	"exportInterface":"../exportPDFByWord",//导出pdf
});
