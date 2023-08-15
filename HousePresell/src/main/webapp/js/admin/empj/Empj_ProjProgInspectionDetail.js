(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			empj_ProjProgInspectionModel: {},
			tableId : 1,
            //附件材料
			busiType:'03030203',
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
            //其他
            errMsg:"", //错误提示信息
            buildingId:"",

            trusteeshipContent:"",
            showPrintBtn : false,
            //附件材料
            loadUploadList: [],
//            showDelete : false,  //附件是否可编辑
            showSubmit : true,//提交
            buildingList : [],//楼幢信息
            
            editFile : false,
            saveFile : false,
            
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			getExportForm :getExportForm,//导出参数
			exportPdf:exportPdf,//导出pdf
            mainEditHandle:mainEditHandle,
            mainSubmitHandle : mainSubmitHandle,//提交
            
            openFileModel : openFileModel,
            
            editFileActive : editFileActive,
            saveFileActive : saveFileActive,
            forecastFun : forecastFun,//预测
            
            dialogSave : dialogSave,//保存附件信息
		},
		computed:{
			 
		},
		components : {
            "my-uploadcomponent":fileUpload
		},
		watch:{
			
		}
	});

	
	function editFileActive(){
		detailVue.editFile = false;
		detailVue.saveFile = true;
		detailVue.showDelete = true;
		
		enterNewTabCloseCurrent(detailVue.tableId, '项目进度巡查修改', 'empj/Empj_ProjProgInspectionEdit.shtml',detailVue.tableId+"03030203");
	}
	
	function saveFileActive(){
		
	}
	function forecastFun() {
    	var model={
    			interfaceVersion:detailVue.interfaceVersion,
//        		keyword:this.keyword,
//        		projectId:this.projectId,
        		tableId:detailVue.tableId
        	}
    	new ServerInterface(baseInfo.forecastInter).execute(model, function (jsonObj) {
    		if (jsonObj.result != "success") {
    			noty({"text": jsonObj.info, "type": "error", "timeout": 2000});
    		} else {
    			generalSuccessModal();
    		}
    	});
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
	function mainSubmitHandle()
	{   //提交
		detailVue.showSubmit = false;
		detailVue.editFile = true;
		var model = 
		{
				interfaceVersion:detailVue.interfaceVersion,
                tableId:detailVue.tableId,
                busiCode : detailVue.busiType,
		};  
		
		//无签章
		model.isSign = "1";
		new ServerInterfaceJsonBody(baseInfo.submitInterface).execute(model, function(jsonObj3) {
			if(jsonObj3.result != "success"){
		        generalErrorModal(jsonObj3);
		    }else{
		    	generalSuccessModal();
		    }
			
			refresh();
		});
    	
		/*签章弹框*/
		/*$(".xszModel").modal({
			backdrop :'static',
			keyboard: false
		});
		
		new ServerInterfaceJsonBody(baseInfo.submitInterface).execute(model, function (jsonObj) {
			detailVue.showSubmit = true;
			if (jsonObj.result != "success"){
				generalErrorModal(jsonObj);
			}else{
				console.log(jsonObj.signatureMap);
				if (jsonObj.signatureMap != undefined && jsonObj.signatureMap != null && jsonObj.signatureMap.signaturePath != null && jsonObj.signatureMap.signaturePath != '') {
					//PDF文件路径、签章关键字
					var signaturePath = jsonObj.signatureMap.signaturePath;
					var signatureKeyword = "开发企业：";
					
					//随机生成保存文件名
					var filename = Math.random().toString(36).substr(2);
					
					//1.根据文件路径打开文件
					// isOpen 是否打开文件
					$("#signWarningText").html("打开文件中，请等待...");
					
					var isOpen = TZPdfViewer.TZOpenPdfByPath(signaturePath,1);
					setTimeout(function(item){
						if (isOpen == 0){
							//2.根据关键字进行文件签章
							// 需要设置等待延时，打开文件需要时间（时间长短由文件大小决定）
							// isSuccess 是否签章成功
							$("#signWarningText").html("正在签章中，请等待...");
							
							var isSuccess = TZPdfViewer.TZInsertESealByKeyWord(signatureKeyword);
							if (TZPdfViewer.PageCount > 0 && isSuccess == 0){
								// 3.将签章后的文件保存到服务器，
								// 再通过OSS上传至文件服务器，
								// 上传成功后再进行正式业务数据提交
								$("#signWarningText").html("正在上传文件，请等待...");
								
								TZPdfViewer.HttpInit();
								TZPdfViewer.HttpAddPostString("name","1111");
								TZPdfViewer.HttpAddPostCurrFile("file2","aaaa");
								TZPdfViewer.HttpPost("http://"+window.location.host+"/HousePresell/admin/FileUpload.jsp?filename="+filename);
								
								setTimeout(function(item){
									//构建上传参数
									var model1 = {
											interfaceVersion : detailVue.interfaceVersion,
											signaturePath : "C:\\uploaded\\"+filename+".pdf",
											signaturePrefixPath :signaturePath,
											fileName : filename,
											urlPath : signaturePath
									}
									
									new ServerInterface(baseInfo.signatureInterface).execute(model1, function(jsonObj2){
										$(".xszModel").modal('hide');
										
										if (jsonObj2.result == "success"){
											model.isSign = "1";
											new ServerInterfaceJsonBody(baseInfo.submitInterface).execute(model, function(jsonObj3) {
												if(jsonObj3.result != "success"){
											        generalErrorModal(jsonObj3);
											    }else{
											    	generalSuccessModal();
											    }
											});
										}else{
											generalErrorModal(jsonObj2);
										}
										refresh();
									});
								}, 10000);
								
							}else{
								//文件签章失败
								generalErrorModal("","文件签章失败，请重新提交！");
	 							$(".xszModel").modal('hide');
							}
						}else{
							//打开文件失败
							generalErrorModal("","文件打开失败，请重新提交！");
							$(".xszModel").modal('hide');
						}
					}, 6000);
					
				}else{
					generalErrorModal("","文件签章失败！");
					$(".xszModel").modal('hide');
				}
			}
			
			refresh();
		});*/
		    	 
	}
	
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{/*
		if(detailVue.tableId==1){
            return {
                interfaceVersion:detailVue.interfaceVersion,
                buildingId:detailVue.buildingId,
            }
		}else{
            
		}*/
		return {
            interfaceVersion:detailVue.interfaceVersion,
            tableId:detailVue.tableId,
          //  reqSource:"详情",
            // buildingId:detailVue.buildingId,
        }
	}

	//详情操作--------------
	function refresh()
	{
		// if (detailVue.tableId == null || detailVue.tableId < 1)
		// {
		// 	return;
		// }

		getDetail();
		loadUpload();

	}

	function getDetail()
	{
		serverRequest(baseInfo.detailInterface,detailVue.getSearchForm(),function (jsonObj) {
			//主表数据
            detailVue.empj_ProjProgInspectionModel = jsonObj.empj_ProjProgInspection_AF;
            
//            detailVue.empj_ProjProgInspectionModel.businessCodeA = detailVue.empj_ProjProgInspectionModel.businessCode + "A";
//            detailVue.empj_ProjProgInspectionModel.businessCodeB = detailVue.empj_ProjProgInspectionModel.businessCode + "B";
            //子表数据
            detailVue.buildingList = jsonObj.empj_ProjProgInspection_DTLList;
            
            detailVue.trusteeshipContent=jsonObj.trusteeshipContent
            if(detailVue.tableId==1){
                detailVue.tableId=detailVue.empj_ProjProgInspectionModel.tableId
			}
			if(detailVue.empj_ProjProgInspectionModel.approvalState == "待提交"){
				detailVue.editFile = true;
			}
			changeThousands();
        })
	}
	function mainEditHandle() {
        enterNewTabCloseCurrent(detailVue.tableId,"受限额度变更编辑","empj/Empj_ProjProgInspection_AFEdit.shtml")
    }

    function loadUpload() {
        generalLoadFile2(detailVue,detailVue.busiType)
    }

    function changeThousands() {
        // detailVue.empj_ProjProgInspectionModel.recordAveragePriceOfBuilding=addThousands(detailVue.empj_ProjProgInspectionModel.recordAveragePriceOfBuilding)
        detailVue.empj_ProjProgInspectionModel.recordAveragePriceOfBuilding=addThousands(detailVue.empj_ProjProgInspectionModel.recordAveragePriceOfBuilding)
        detailVue.empj_ProjProgInspectionModel.orgLimitedAmount=addThousands(detailVue.empj_ProjProgInspectionModel.orgLimitedAmount)
        detailVue.empj_ProjProgInspectionModel.nodeLimitedAmount=addThousands(detailVue.empj_ProjProgInspectionModel.nodeLimitedAmount)
        detailVue.empj_ProjProgInspectionModel.effectiveLimitedAmount=addThousands(detailVue.empj_ProjProgInspectionModel.effectiveLimitedAmount)
        detailVue.empj_ProjProgInspectionModel.totalGuaranteeAmount=addThousands(detailVue.empj_ProjProgInspectionModel.totalGuaranteeAmount)
        detailVue.empj_ProjProgInspectionModel.expectEffectLimitedAmount=addThousands(detailVue.empj_ProjProgInspectionModel.expectEffectLimitedAmount)
        detailVue.empj_ProjProgInspectionModel.cashLimitedAmount=addThousands(detailVue.empj_ProjProgInspectionModel.cashLimitedAmount)
        detailVue.empj_ProjProgInspectionModel.expectLimitedAmount=addThousands(detailVue.empj_ProjProgInspectionModel.expectLimitedAmount)
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
  			sourceBusiCode : "03030203",
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
    	
    //	generalLoadFileBld(detailVue);
    	/*var form = {
	        pageNumber:"0",
	        busiType : detailVue.busiType,
	        interfaceVersion:detailVue.interfaceVersion,
	        reqSource : detailVue.flag == true ? "详情" : "",//判断是否是详情页是则显示旧的附件材料
	        sourceId:bulidInfo.buildingId,
	       // approvalCode:detailVue.approvalCode,
	        afId:detailVue.afId,
	    };
	    detailVue.loadUploadList=[]
	    serverRequest("../Sm_AttachmentCfgList",form,function (jsonObj) {
	    	detailVue.loadUploadList = jsonObj.sm_AttachmentCfgList;
	    },function (jsonObj) {
	        generalErrorModal(jsonObj)
	    })*/
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
            refresh()

        })
		
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	// detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#empj_ProjProgInspectionDetailDiv",
	"detailInterface":"../Empj_ProjProgInspection_AFDetail",
	"exportInterface":"../exportPDFByWord",//导出pdf
	 "forecastInter": "../Empj_ProjProgInspectionForecast",//预测
    //附件读取
    "loadUploadInterface":"../Sm_AttachmentCfgList",
    "submitInterface":"../Empj_ProjProgInspectionSubmit",//提交
    "editFileInterface":"../Empj_ProjProgInspectionEdit",//修改附件信息
    
    "signatureInterface":"../Sm_SignatureUploadForPath",//签章
    //模态框
    "errorModal": "#errorM",
});
