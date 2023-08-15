(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			empj_ProjProgForcastManageModel: {},
			tableId : 1,
			newTableId : 1,
			
            //附件材料-工程进度巡查附件
			busiType:'03030202',
            dialogVisible: false,
            dialogImageUrl: "",
            fileType:"",
            fileList : [],
            showButton:false,
            hideShow:true,
            uploadData : [],
            smAttachmentList:[],//页面显示已上传的文件
            uploadList:[],
            showDelete : false,  //附件是否可编辑
            
            show : false,//是否显示附件
            //其他
            errMsg:"", //错误提示信息
            buildingId:"",

            trusteeshipContent:"",
            showPrintBtn : false,
            //附件材料
            loadUploadList: [],
            showSubmit : true,//提交
            buildingList : [],//楼幢信息
            
            editFile : false,
            saveFile : false,
            
            level : "",
            action : "view",
            aeecss_token : "",
            ts_id : "",
            ts_pj_id : "",
            buildingId : "",
            nowDate : "",
            
            //是否需要备案
            isNeedBackup:"",
            
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
            mainEditHandle:mainEditHandle,
            
            openFileModel : openFileModel,
            
            editFileActive : editFileActive,
            saveFileActive : saveFileActive,
            
            dialogSave : dialogSave,//保存附件信息
            
            vdeioInfo : vdeioInfo,//视频信息
            ssoLogin : ssoLogin,//网站登录
            
            //审批流
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

	
	function vdeioInfo(){
		if(detailVue.level == "build"){
			window.open("http://apits.czzhengtai.com:811/v_upload/?aeecss_token="+detailVue.aeecss_token+"&ts_id="+detailVue.ts_id+"&ts_pj_id="+detailVue.ts_pj_id+"&ts_bld_id="+detailVue.buildingId+"&action="+detailVue.action + "&jdtime=" + detailVue.nowDate);
		}else{
			window.open("http://apits.czzhengtai.com:811/v_upload/?aeecss_token="+detailVue.aeecss_token+"&ts_id="+detailVue.ts_id+"&ts_pj_id="+detailVue.ts_pj_id+"&action="+detailVue.action + "&jdtime=" + detailVue.nowDate);
		}
	}
	
	function ssoLogin() {
		new ServerInterface(baseInfo.ssoLoginInterface).execute(detailVue.getSearchForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
			} else {
				detailVue.aeecss_token = jsonObj.aeecss_token;
				console.log(detailVue.aeecss_token);
			}
		});
	}
	
	function editFileActive(){
		detailVue.editFile = false;
		detailVue.saveFile = true;
		detailVue.showDelete = true;
		detailVue.action = "";
		//enterNewTabCloseCurrent(detailVue.tableId, '工程进度巡查推送修改', 'empj/Empj_ProjProgInspectionEdit.shtml',detailVue.tableId+"03030203");
	}
	
	function saveFileActive(){
		
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
    	
    	console.log(files);
    	console.log(detailVue.buildAddFile);
    	
    	$("#fileNodeModelDetail").modal('hide');
    	
    	var model = 
		{
				interfaceVersion : detailVue.interfaceVersion,
                tableId : detailVue.buildAddFile.tableId,
                busiCode : detailVue.busiType,
                attachmentList : attachementList 
		}; 
    	
    	new ServerInterfaceJsonBody(baseInfo.editFileInterface).execute(model, function (jsonObj) {
 			
 	        if (jsonObj.result != "success") {
// 	        	generalErrorModal(jsonObj)
 	        }
 	        else 
 	        {
 	        	generalSuccessModal();
 	        }
 	    });
    	
    }
	//------------------------方法定义-开始------------------//

    
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
            interfaceVersion:detailVue.interfaceVersion,
            tableId:detailVue.tableId,
        }
	}

	//详情操作--------------
	function refresh()
	{
		getDetail();
	}

	function getDetail()
	{
		serverRequest(baseInfo.detailInterface,detailVue.getSearchForm(),function (jsonObj) {
			//主表数据
            detailVue.empj_ProjProgForcastManageModel = jsonObj.empj_PaymentBond_Manage;
            detailVue.newTableId=detailVue.empj_ProjProgForcastManageModel.tableId;
            
            if(detailVue.tableId==1){
                detailVue.tableId=detailVue.empj_ProjProgForcastManageModel.tableId
			}
            
            if(detailVue.empj_ProjProgForcastManageModel.level == "build"){
            	detailVue.level = "build";
            	
            	detailVue.tableId = detailVue.empj_ProjProgForcastManageModel.dtlId;
            	detailVue.ts_id  = detailVue.empj_ProjProgForcastManageModel.dtlId;
            	detailVue.ts_pj_id = detailVue.empj_ProjProgForcastManageModel.projectId;
            	detailVue.buildingId = detailVue.empj_ProjProgForcastManageModel.buildId;
            }else{
            	show = true;
            	detailVue.level = "project";
            	detailVue.ts_id  = detailVue.empj_ProjProgForcastManageModel.afId;
            	detailVue.ts_pj_id = detailVue.empj_ProjProgForcastManageModel.projectId;
            }
            
            //附件
            loadUpload();
            
			if(detailVue.empj_ProjProgForcastManageModel.approvalState == "待提交"){
				detailVue.editFile = true;
			}
			
			if(detailVue.empj_ProjProgForcastManageModel.webPushState == "0"){
				detailVue.empj_ProjProgForcastManageModel.webPushState = "未推送";
			}
			if(detailVue.empj_ProjProgForcastManageModel.webPushState == "1"){
				detailVue.empj_ProjProgForcastManageModel.webPushState = "推送中";
			}
			if(detailVue.empj_ProjProgForcastManageModel.webPushState == "2"){
				detailVue.empj_ProjProgForcastManageModel.webPushState = "已推送";
			}
			if(detailVue.empj_ProjProgForcastManageModel.webHandelState == "-1"){
				detailVue.empj_ProjProgForcastManageModel.webHandelState = "未审核";
			}
			if(detailVue.empj_ProjProgForcastManageModel.webHandelState == "0"){
				detailVue.empj_ProjProgForcastManageModel.webHandelState = "不通过";
			}
			if(detailVue.empj_ProjProgForcastManageModel.webHandelState == "1"){
				detailVue.empj_ProjProgForcastManageModel.webHandelState = "审核通过";
			}
			
        })
	}
	function mainEditHandle() {
        enterNewTabCloseCurrent(detailVue.tableId,"受限额度变更编辑","empj/Empj_ProjProgInspection_AFEdit.shtml")
    }

    function loadUpload() {
    	detailVue.tableId = detailVue.ts_id;
    	console.log("detailVue.tableId = " + detailVue.tableId);
        generalLoadFile2(detailVue,detailVue.busiType)
    }

    
  	
  	/**
     * 点击附件信息打开模态框
     * @author  yaojianping
     * @returns
     */
    function openFileModel(bulidInfo){
    	detailVue.buildAddFile = bulidInfo;
    	detailVue.loadUploadList = bulidInfo.attachementList;
    	
    	$("#fileNodeModelDetail").modal({
    		backdrop :'static',
    		keyboard: false
    	});
    }
  	
	function initData()
	{
		/*console.log('initData')
		getIdFormTab("",function (id) {
			if(id.indexOf("BuildingTable")!=-1){
				detailVue.buildingId=id.split("-")[0]
				console.log('buildingId is '+detailVue.buildingId)
                detailVue.tableId=1
				// refresh()
			}else{
                detailVue.tableId=id
			}
			console.log('tableId is '+detailVue.tableId)
            refresh();
			ssoLogin();
			var myDate = new Date();
			detailVue.nowDate = myDate.toLocaleDateString().split('/').join('-');

        });*/
		
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
			ssoLogin();
			/*var myDate = new Date();
			detailVue.nowDate = myDate.toLocaleDateString().split('/').join('-');*/
			
			detailVue.nowDate = getNowFormatDate();
		}
		
	}
	
	function approvalHandle () {
        approvalModalVue.buttonType = 2;
        approvalModalVue.approvalHandle();
    }
	
	function showModal() {
        approvalModalVue.getModalWorkflowList();
    }
	
	//------------------------方法定义-结束------------------//
	
	
	//------------------------数据初始化-开始----------------//
	// detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#sm_ProjProgForcastManageDetailDiv",
	//详情
	"detailInterface":"../Empj_ProjProgForcast_ManageDetail",
    //附件读取
    "loadUploadInterface":"../Sm_AttachmentCfgList",
    
    
    "submitInterface":"../Empj_ProjProgInspectionSubmit",//提交
    "editFileInterface":"../Empj_ProjProgInspectionEdit",//修改附件信息
    
    "signatureInterface":"../Sm_SignatureUploadForPath",//签章
    "ssoLoginInterface" : "../LoginVerification",// 网站SSO登录
    //模态框
    "errorModal": "#errorM",
});
