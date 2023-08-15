(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			empj_BldLimitAmountModel: {
				appointTimeStamp : "",
			},
			tableId : 1,
            //附件材料
			busiType:'03030101',
            dialogVisible: false,
            dialogImageUrl: "",
            fileType:"",
            fileList : [],
            showButton:false,
            hideShow:true,
            uploadData : [],
            smAttachmentList:[],//页面显示已上传的文件
            uploadList:[],
            showDelete : true,  //附件是否可编辑
            //其他
            errMsg:"", //错误提示信息
            buildingId:"",

            trusteeshipContent:"",
            showPrintBtn : false,
            //附件材料
            loadUploadList: [],
            showDelete : false,  //附件是否可编辑
            showSubmit : true,//提交
            buildingList : [],//楼幢信息
            
          //----------审批流start-----------//
            afId:"",//申请单Id
            workflowId:"",//结点Id
            isNeedBackup:"",//是否需要备案
            sourcePage:"",//来源页面
            //----------审批流end-----------//
            
            approvalInfo : "",//评语
          //A评语
            resultInfoOne : "",
          //B评语
            resultInfoTwo : "",
            checkBuild : {},
            
            buildAddFile : [],
            isAppointTimeDisable : false,//预约时间是否可选择
            
            // == 用户角色 ==
            //默认管理员
            userType : "all",
            workflowCode : "",
            // == 用户角色 ==
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			getExportForm :getExportForm,//导出参数
			exportPdf:exportPdf,//导出pdf
            mainEditHandle:mainEditHandle,
            
            openFileModel : openFileModel,
            
            showModal: showModal,
            approvalHandle: function () {
                approvalModalVue.buttonType = 2;
                approvalModalVue.approvalHandle();
            },
            changeApprovalResultHandle : changeApprovalResultHandle,
            dialogSaveComment : dialogSaveComment,
            closeCommentModel : closeCommentModel,
            
            changeApprovalResultAHandle : changeApprovalResultAHandle,
            dialogSaveCommentA : dialogSaveCommentA,
            closeCommentModelA : closeCommentModelA,
            
            changeApprovalResultBHandle : changeApprovalResultBHandle,
            dialogSaveCommentB : dialogSaveCommentB,
            closeCommentModelB : closeCommentModelB,
            
            //保存附件
            dialogSave : dialogSave,
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
	
	
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var array = tableIdStr.split("_");
        if (array.length > 1)
        {
            detailVue.tableId = array[2]; //用款申请主表id
        }
		return {
            interfaceVersion:detailVue.interfaceVersion,
            tableId:detailVue.tableId,
            //afId:detailVue.afId,
        }
	}

	//详情操作--------------
	function refresh()
	{
		getDetail();
		loadUpload();
	}

	function getDetail()
	{
		serverRequest(baseInfo.detailInterface,detailVue.getSearchForm(),function (jsonObj) {
            detailVue.empj_BldLimitAmountModel = jsonObj.empj_BldLimitAmount;
            if(detailVue.empj_BldLimitAmountModel.appointTimeStamp == "--"){
            	detailVue.empj_BldLimitAmountModel.appointTimeStamp = ""
            }
            
            detailVue.empj_BldLimitAmountModel.businessCodeA = detailVue.empj_BldLimitAmountModel.businessCode + "A";
            detailVue.empj_BldLimitAmountModel.businessCodeB = detailVue.empj_BldLimitAmountModel.businessCode + "B";
            
            detailVue.userType = jsonObj.userType;
            
            detailVue.workflowCode = detailVue.empj_BldLimitAmountModel.workflowCode;
            
            //预约时间编辑
            if("WQCS" != detailVue.empj_BldLimitAmountModel.workflowCode){
            	detailVue.isAppointTimeDisable = true;
            }
            
            //附件编辑
            if("JDJZ" == detailVue.empj_BldLimitAmountModel.workflowCode){
            	detailVue.showDelete = true;
            }
            
            
            
            detailVue.buildingList = jsonObj.empj_BldLimitAmount.dtlList;
            detailVue.trusteeshipContent=jsonObj.trusteeshipContent;
            
            if(detailVue.tableId==1){
                detailVue.tableId=detailVue.empj_BldLimitAmountModel.tableId
			}
            
            if(detailVue.empj_BldLimitAmountModel.appointmentDateOne == undefined){
            	detailVue.empj_BldLimitAmountModel.appointmentDateOne = ''
            }
            if(detailVue.empj_BldLimitAmountModel.appointmentDateTwo == undefined){
            	detailVue.empj_BldLimitAmountModel.appointmentDateTwo = ''
            }
/*			if(jsonObj.empj_BldLimitAmount_AF.approvalState == "已完结"){
				detailVue.showPrintBtn = true;
			}*/
            
            
			changeThousands()
        })
		// new ServerInterface().execute(detailVue.getSearchForm(), function(jsonObj)
		// {
		// 	if(jsonObj.result != "success")
		// 	{
		// 		noty({"text":jsonObj.info,"type":"error","timeout":2000});
		// 	}
		// 	else
		// 	{
		// 		detailVue.empj_BldLimitAmountModel = jsonObj.empj_BldLimitAmount_AF;
		// 	}
		// });
	}

	function mainEditHandle() {
        enterNewTabCloseCurrent(detailVue.tableId,"受限额度变更编辑","empj/Empj_BldLimitAmount_AFEdit.shtml")
    }

    function loadUpload() {
        generalLoadFile2(detailVue,detailVue.busiType)
    }

    function changeThousands() {
        // detailVue.empj_BldLimitAmountModel.recordAveragePriceOfBuilding=addThousands(detailVue.empj_BldLimitAmountModel.recordAveragePriceOfBuilding)
        detailVue.empj_BldLimitAmountModel.recordAveragePriceOfBuilding=addThousands(detailVue.empj_BldLimitAmountModel.recordAveragePriceOfBuilding)
        detailVue.empj_BldLimitAmountModel.orgLimitedAmount=addThousands(detailVue.empj_BldLimitAmountModel.orgLimitedAmount)
        detailVue.empj_BldLimitAmountModel.nodeLimitedAmount=addThousands(detailVue.empj_BldLimitAmountModel.nodeLimitedAmount)
        detailVue.empj_BldLimitAmountModel.effectiveLimitedAmount=addThousands(detailVue.empj_BldLimitAmountModel.effectiveLimitedAmount)
        detailVue.empj_BldLimitAmountModel.totalGuaranteeAmount=addThousands(detailVue.empj_BldLimitAmountModel.totalGuaranteeAmount)
        detailVue.empj_BldLimitAmountModel.expectEffectLimitedAmount=addThousands(detailVue.empj_BldLimitAmountModel.expectEffectLimitedAmount)
        detailVue.empj_BldLimitAmountModel.cashLimitedAmount=addThousands(detailVue.empj_BldLimitAmountModel.cashLimitedAmount)
        detailVue.empj_BldLimitAmountModel.expectLimitedAmount=addThousands(detailVue.empj_BldLimitAmountModel.expectLimitedAmount)
    }
    
  //列表操作-----------------------导出PDF
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
  	
  	function getExportForm() {
  		var href = window.location.href;
  		return {
  			interfaceVersion : this.interfaceVersion,
  			sourceId : this.tableId,
  			reqAddress : href,
  			sourceBusiCode : this.busiType,
  		}
  	}
	
  	
  	/**
     * 点击附件信息打开模态框
     * @author  yaojianping
     * @returns
     */
    function openFileModel(bulidInfo){
    	detailVue.buildAddFile = bulidInfo;
    	detailVue.loadUploadList = bulidInfo.attachementList;
    	var attachementListFile = detailVue.buildAddFile;
    	if(attachementListFile.files !=undefined && attachementListFile.files.length>0){
    		for(var j = 0;j<detailVue.loadUploadList.length;j++){
    			detailVue.loadUploadList[j].smAttachmentList = [];
    			for(var i = 0;i<attachementListFile.files.length;i++){
    				var model = {
						fileType:attachementListFile.files[i].fileType,
						name:attachementListFile.files[i].remark,
						url:attachementListFile.files[i].theLink,
						sourceType:attachementListFile.files[i].sourceType,
						tableId:attachementListFile.files[i].tableId,
						busiType : attachementListFile.files[i].busiType,
    				}
    				if(detailVue.loadUploadList[j].eCode == attachementListFile.files[i].sourceType){
    					detailVue.loadUploadList[j].smAttachmentList.push(model);
    				}
    			}
    		}
    	}
    	$("#fileNodeModelDetailSub").modal({
    		backdrop :'static',
    		keyboard: false
    	});
    /*//	generalLoadFileBld(detailVue);
    	var form = {
	        pageNumber:"0",
	        busiType : detailVue.busiType,
	        interfaceVersion:detailVue.interfaceVersion,
	        reqSource : detailVue.flag == true ? "详情" : "",//判断是否是详情页是则显示旧的附件材料
	        sourceId:bulidInfo.tableId,
	       // approvalCode:detailVue.approvalCode,
	        afId:detailVue.afId,
	    };
	    detailVue.loadUploadList=[]
	    serverRequest("../Sm_AttachmentCfgList",form,function (jsonObj) {
	    	detailVue.loadUploadList = jsonObj.sm_AttachmentCfgList;
	    },function (jsonObj) {
	        generalErrorModal(jsonObj)
	    })
    	*/
    }
  	
    function showModal() {
        approvalModalVue.extraObj = {
            tableId: detailVue.tableId,
            appointTimeString: $('#date0303010003').val(),
            appointTimeOneString: $('#date030301000A').val(),
            appointTimeTwoString: $('#date030301000B').val(),
            
            userType : detailVue.userType,
            dtlList : detailVue.buildingList
        }
        
        approvalModalVue.getModalWorkflowList();
    }
    /**
     * 审批楼幢是否通过
     * @returns
     */
    function changeApprovalResultHandle(val){
    	detailVue.checkBuild = val;
    	val.approvalInfo = "";
    	if(val.approvalResult == '0'){
    		$("#commentModel").modal({
        		backdrop :'static',
        		keyboard: false
        	});
    	}else{
    		$("#commentModel").model('hide');
    		
    	}
    }
    
    /**
     * A审批楼幢是否通过
     * @returns
     */
    function changeApprovalResultAHandle(val){
    	detailVue.checkBuild = val;
    	val.resultInfoOne = "";
    	if(val.resultOne == '0'){
    		$("#commentModelA").modal({
        		backdrop :'static',
        		keyboard: false
        	});
    	}else{
    		$("#commentModelA").model('hide');
    		
    	}
    }
    
    /**
     * B审批楼幢是否通过
     * @returns
     */
    function changeApprovalResultBHandle(val){
    	detailVue.checkBuild = val;
    	val.resultInfoTwo = "";
    	if(val.resultTwo == '0'){
    		$("#commentModelB").modal({
        		backdrop :'static',
        		keyboard: false
        	});
    	}else{
    		$("#commentModelB").model('hide');
    		
    	}
    }
    
    
    /**
     * 保存评语
     * @returns
     */
    function dialogSaveComment(){
    	if(detailVue.approvalInfo == ""){
    		generalErrorModal("","评语不能为空");
    	}else{
    		detailVue.checkBuild.approvalInfo = detailVue.approvalInfo;
    		$("#commentModel").modal('hide');
    		detailVue.approvalInfo = "";
    	}
    	
    }
    
    /**
     * A保存评语
     * @returns
     */
    function dialogSaveCommentA(){
    	if(detailVue.resultInfoOne == ""){
    		generalErrorModal("","评语不能为空");
    	}else{
    		detailVue.checkBuild.resultInfoOne = detailVue.resultInfoOne;
    		$("#commentModelA").modal('hide');
    		detailVue.resultInfoOne = "";
    	}
    	
    }
    
    /**
     * B保存评语
     * @returns
     */
    function dialogSaveCommentB(){
    	if(detailVue.resultInfoTwo == ""){
    		generalErrorModal("","评语不能为空");
    	}else{
    		detailVue.checkBuild.resultInfoTwo = detailVue.resultInfoTwo;
    		$("#commentModelB").modal('hide');
    		detailVue.resultInfoTwo = "";
    	}
    	
    }
    /**
     * 点击取消或者关闭事件
     * 将不通过改为通过
     * @returns
     */
    function closeCommentModel(){
    	$("#commentModel").modal('hide');
    	detailVue.approvalInfo = "";
    	if(detailVue.checkBuild.approvalResult == '0'){
			detailVue.checkBuild.approvalResult = '1';
		}
    }
    
    /**
     * A点击取消或者关闭事件
     * 将不通过改为通过
     * @returns
     */
    function closeCommentModelA(){
    	$("#commentModelA").modal('hide');
    	detailVue.resultInfoOne = "";
    	if(detailVue.checkBuild.resultOne == '0'){
			detailVue.checkBuild.resultOne = '1';
		}
    }
    
    /**
     * B点击取消或者关闭事件
     * 将不通过改为通过
     * @returns
     */
    function closeCommentModelB(){
    	$("#commentModelB").modal('hide');
    	detailVue.resultInfoTwo = "";
    	if(detailVue.checkBuild.resultTwo == '0'){
			detailVue.checkBuild.resultTwo = '1';
		}
    }
   
    /**
     * 保存附件信息
     * @author  yaojianping
     * @returns
     */
    function dialogSave(){
    	var files = this.$refs.listenUploadData.uploadData;
    	var attachementList = [];
    	if(files.length>0){
    		for(var i = 0;i<files.length;i++){
    			attachementList.push(files[i]);
    		}
    	}
    	detailVue.buildAddFile.files = attachementList;
    	$("#fileNodeModelDetailSub").modal('hide');
    	
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
            
            console.log("sourcePage=" + approvalModalVue.sourcePage);
			refresh();
		}
		 var myDate = new Date();
		laydate.render({
            elem: '#date0303010003',
            min: myDate.toLocaleString(),
            
        });
		
		laydate.render({
            elem: '#date030301000A',
            type: 'datetime',
            min: myDate.toLocaleString(),
            done: function(value, date){
                detailVue.empj_BldLimitAmountModel.appointmentDateOne = value
            }
        });
		
		laydate.render({
            elem: '#date030301000B',
            type: 'datetime',
            min: myDate.toLocaleString(),
            done: function(value, date){
                detailVue.empj_BldLimitAmountModel.appointmentDateTwo = value
            }
        });
		
		
/*		
		getIdFormTab("",function (id) {
			if(id.indexOf("BuildingTable")!=-1){
				detailVue.buildingId=id.split("-")[0]
                detailVue.tableId=1
				// refresh()
			}else{
                detailVue.tableId=id
			}
            refresh()

        })*/
		
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	// detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#sm_BldLimitAmountDetailDiv",
	"detailInterface":"../Empj_BldLimitAmountApprovalDetail",
	"exportInterface":"../exportPDFByWord",//导出pdf
    //附件读取
    "loadUploadInterface":"../Sm_AttachmentCfgList",
    "submitInterface":"../Empj_BldLimitAmountSubmit",//提交
    "signatureInterface":"../Sm_SignatureUploadForPath",//签章
    //模态框
    "errorModal": "#errorM",
});
