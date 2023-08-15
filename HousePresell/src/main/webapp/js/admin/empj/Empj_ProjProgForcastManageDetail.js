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
            
            
            submit : false,
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			getSubmitForm : getSubmitForm,
            mainEditHandle:mainEditHandle,
            
            openFileModel : openFileModel,
            
            editFileActive : editFileActive,
            saveFileActive : saveFileActive,
            
            dialogSave : dialogSave,//保存附件信息
            
            vdeioInfo : vdeioInfo,//视频信息
            ssoLogin : ssoLogin,//网站登录
            
            submitAction : submitAction,
            saveAction : saveAction,
		},
		computed:{
			 
		},
		components : {
            "my-uploadcomponent":fileUpload
		},
		watch:{
			
		}
	});

	
	function saveAction(){
		detailVue.showDelete = false;
		detailVue.action = "view";
		dialogSave();
	}
	
	function getSubmitForm()
	{
		return {
            interfaceVersion:detailVue.interfaceVersion,
            tableId:detailVue.newTableId,
        }
	}
	
	function submitAction(){
		//submitInterface
		serverRequest(baseInfo.submitInterface,detailVue.getSubmitForm(),function (jsonObj) {
            
			if (jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
				generalSuccessModal();
				getDetail();
			}
			
        })
	}
	
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
    	var files = detailVue.$refs.listenUploadData.uploadData;
    	var attachementList = [];
    	
    	if(files.length>0){
    		for(var i = 0;i<files.length;i++){
    			attachementList.push(files[i]);
    		}
    	}
    	
    	console.log(files);
    	console.log(attachementList);
    	
    	var model = 
		{
				interfaceVersion : detailVue.interfaceVersion,
                tableId : detailVue.newTableId,
                busiCode : detailVue.busiType,
                attachementList : attachementList 
		}; 
    	
    	new ServerInterfaceJsonBody(baseInfo.editFileInterface).execute(model, function (jsonObj) {
 			
 	        if (jsonObj.result != "success") {
 	        	generalErrorModal(jsonObj)
 	        }
 	        else 
 	        {
 	        	generalSuccessModal();
 	        }
 	        
 	       getDetail();
 	    });
    	
    }
	//------------------------方法定义-开始------------------//

    
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		if(detailVue.newTableId != 1){
			console.log("detailVue.newTableId = " + detailVue.newTableId);
			return {
	            interfaceVersion:detailVue.interfaceVersion,
	            tableId:detailVue.newTableId,
	        }
		}else{
			console.log("detailVue.tableId = " + detailVue.tableId);
			return {
	            interfaceVersion:detailVue.interfaceVersion,
	            tableId:detailVue.tableId,
	        }
		}
		
	}

	//详情操作--------------
	function refresh()
	{
		getDetail();
		//loadUpload();
	}

	function getDetail()
	{
		serverRequest(baseInfo.detailInterface,detailVue.getSearchForm(),function (jsonObj) {
			//主表数据
            detailVue.empj_ProjProgForcastManageModel = jsonObj.empj_PaymentBond_Manage;
            detailVue.newTableId=detailVue.empj_ProjProgForcastManageModel.tableId;
            if(detailVue.tableId==1){
                detailVue.tableId=detailVue.empj_ProjProgForcastManageModel.tableId;
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
            
            
            //加载附件
            loadUpload();
            
			if(detailVue.empj_ProjProgForcastManageModel.approvalState == "待提交"){
				detailVue.editFile = true;
				detailVue.submit = true;
				detailVue.saveFile = false;
			}else{
				detailVue.editFile = false;
				detailVue.submit = false;
				detailVue.saveFile = false;
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
		console.log('initData')
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
			/*var myDate = new Date();
			detailVue.nowDate = myDate.toLocaleDateString().split('/').join('-');*/
			
			detailVue.nowDate = getNowFormatDate();

        });
		
	}
	//------------------------方法定义-结束------------------//
	
	
	//------------------------数据初始化-开始----------------//
	// detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#empj_ProjProgForcastManageDetailDiv",
	//详情
	"detailInterface":"../Empj_ProjProgForcast_ManageDetail",
    //附件读取
    "loadUploadInterface":"../Sm_AttachmentCfgList",
    
    
    "submitInterface":"../Empj_ProjProgForcast_ManageSubmit",//提交
    "editFileInterface":"../Empj_ProjProgForcast_ManageSave",//修改附件信息
    
    "signatureInterface":"../Sm_SignatureUploadForPath",//签章
    "ssoLoginInterface" : "../LoginVerification",// 网站SSO登录
    //模态框
    "errorModal": "#errorM",
});
