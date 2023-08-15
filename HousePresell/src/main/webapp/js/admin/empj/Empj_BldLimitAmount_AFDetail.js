(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			empj_BldLimitAmount_AFModel: {},
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
            loadUploadList: [],
            showDelete : false,  //附件是否可编辑
            //其他
            errMsg:"", //错误提示信息
            buildingId:"",

            trusteeshipContent:"",
            showPrintBtn : false,
            //附件材料
            loadUploadList: [],
            showDelete : false,  //附件是否可编辑
            showSubmit : true,//提交

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
            bldLimitAmountDetail : bldLimitAmountDetail,
            bldLimitAmountHandle : bldLimitAmountHandle,//确认通过
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
	
	function bldLimitAmountHandle(tableId_In){
		var model = 
		{
			interfaceVersion : detailVue.interfaceVersion,
            tableId : tableId_In,
		};
		new ServerInterface(baseInfo.handleInterface).execute(model, function (jsonObj) {
			if (jsonObj.result != "success") 
            {
                generalErrorModal(jsonObj);
            }
            else 
            {
            	generalSuccessModal();
            }
			
			detailVue.refresh();
			
		});
        
	}
	
	function bldLimitAmountDetail(tableId){
		enterNewTab(tableId, "受限额度节点详情", "tgpj/Tgpj_BldLimitAmountVer_AFDetail.shtml")
	}
	
	function mainSubmitHandle()
	{   //提交
		detailVue.showSubmit = false;
		var model = 
		{
				interfaceVersion:detailVue.interfaceVersion,
                tableId:detailVue.tableId,
                busiCode : detailVue.busiType,
		};

    	$(".xszModel").modal({
			backdrop :'static',
			keyboard: false
		});
    	 new ServerInterfaceJsonBody(baseInfo.submitInterface).execute(model, function (jsonObj) {
    		 detailVue.showSubmit = true;
             if (jsonObj.result != "success") 
             {
                 generalErrorModal(jsonObj);
             }
             else 
             {
         		console.log(jsonObj.signatureMap)
             	if(jsonObj.signatureMap != undefined&&jsonObj.signatureMap != null)
 	    			{
 					
 					//pdf路径
 					var signaturePath = jsonObj.signatureMap.signaturePath;
 					//签章关键字
 					var signatureKeyword = jsonObj.signatureMap.signatureKeyword;
 					console.log("签章开始：adsa");
 					if(signaturePath != null && signaturePath != '')
 					{
 						
 						var filename = Math.random().toString(36).substr(2);
 						// 1.打开文件
 						console.log("打开文件开始："+Date.parse(new Date()));
 						var isOpen = TZPdfViewer.TZOpenPdfByPath(signaturePath,1);					
 						
 						if(isOpen==0){
 							// 2.根据关键字签章
 							setTimeout(function(item){
 								console.log("打开文件结束："+Date.parse(new Date()) + "；；；打开文件返回值TZOpenPdfByPath="+isOpen);
 								console.log("签章开始："+Date.parse(new Date()));
 								$("#signWarningText").html("正在正常签章中，请等待...");
 								var pro3=TZPdfViewer.TZInsertESealByKeyWord(signatureKeyword);
 								
 								if(pro3==0&&TZPdfViewer.PageCount>0){
 									// 3. 保存至服务器
 									console.log("签章结束："+Date.parse(new Date())+ "；；；签章文件返回值TZSignByPos3="+pro3);
 									console.log("保存文件至服务器开始："+Date.parse(new Date()));
 									$("#signWarningText").html("正在上传文件，请等待...");
 									TZPdfViewer.HttpInit();
 									TZPdfViewer.HttpAddPostString("name","1111");
 									TZPdfViewer.HttpAddPostCurrFile("file2","aaaa");// HttpAddPostCurrFile上传编辑器当前文件，第二个参数传""
 																										// ,随即产生Word的文件名
 									TZPdfViewer.HttpPost("http://"+window.location.host+"/HousePresell/admin/FileUpload.jsp?filename="+filename);
 									                 
 									setTimeout(function(item){
 										console.log("保存文件至服务器结束："+Date.parse(new Date()));
 										var model1 =
 											{
 												interfaceVersion : detailVue.interfaceVersion,
 												signaturePath : "C:\\uploaded\\"+filename+".pdf",
 												signaturePrefixPath :signaturePath,
 												fileName : filename,
 												urlPath : signaturePath,// 签章网络路径
 											}
 										new ServerInterface(baseInfo.signatureInterface).execute(model1, function(jsonObj2){
     											$(".xszModel").modal('hide');
     											
     											console.log("保存到OSSServer结束："+Date.parse(new Date()));
     											if(jsonObj.result != "success")
									            {
									                generalErrorModal(jsonObj);
									            }
												else
												{
													 model.isSign = "1";
													 new ServerInterfaceJsonBody(baseInfo.submitInterface).execute(model, function(jsonObj3) {
														if(jsonObj3.result != "success")
											            {
											                generalErrorModal(jsonObj3);
											            }
											            else
											            {
											            	generalSuccessModal();
											            	refresh();
											            }
													});
												}
     											
 											});
 									},12000);		
 								}
 								else{
 									generalErrorModal("","电子签章失败，建议网络环境在10M以上，请重新提交！");// 签章文件打开失败！
 									$(".xszModel").modal('hide');
 								}						
 								
 								},10000);
 							
 						}else{
 							generalErrorModal("","文件打开失败，请重新提交！");// 签章文件打开失败！
 							$(".xszModel").modal('hide');
 						}					
 						
 					}else{
 						$(".xszModel").modal('hide');
 						generalSuccessModal();
 					}
 				}else{
 					$(".xszModel").modal('hide');
 					generalSuccessModal();
 				}
         		
         		/*$(".xszModel").modal('hide');
         		refresh();*/
             }
             
         });
    	 
		/*new ServerInterface(baseInfo.submitInterface).execute(model, function(jsonObj)
		{
			detailVue.showSubmit = true;
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				
			}
		});*/
	}
	
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		if(detailVue.tableId==1){
            return {
                interfaceVersion:detailVue.interfaceVersion,
                buildingId:detailVue.buildingId,
            }
		}else{
            return {
                interfaceVersion:detailVue.interfaceVersion,
                tableId:detailVue.tableId,
                reqSource:"详情",
                // buildingId:detailVue.buildingId,
            }
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
            detailVue.empj_BldLimitAmount_AFModel = jsonObj.empj_BldLimitAmount_AF;
            detailVue.trusteeshipContent=jsonObj.trusteeshipContent
            if(detailVue.tableId==1){
                detailVue.tableId=detailVue.empj_BldLimitAmount_AFModel.tableId
			}
			if(jsonObj.empj_BldLimitAmount_AF.approvalState == "已完结"){
				detailVue.showPrintBtn = true;
			}
			
			detailVue.empj_BldLimitAmount_AFModel.nodeLimitedAmount = detailVue.empj_BldLimitAmount_AFModel.orgLimitedAmount * detailVue.empj_BldLimitAmount_AFModel.currentLimitedRatio /100;
			//detailVue.empj_BldLimitAmount_AFModel.effectiveLimitedAmount = detailVue.empj_BldLimitAmount_AFModel.orgLimitedAmount * detailVue.empj_BldLimitAmount_AFModel.currentLimitedRatio /100;
            
			if(detailVue.empj_BldLimitAmount_AFModel.nodeLimitedAmount < detailVue.empj_BldLimitAmount_AFModel.cashLimitedAmount){
				detailVue.empj_BldLimitAmount_AFModel.effectiveLimitedAmount = detailVue.empj_BldLimitAmount_AFModel.nodeLimitedAmount;
			}else{
				detailVue.empj_BldLimitAmount_AFModel.effectiveLimitedAmount = detailVue.empj_BldLimitAmount_AFModel.cashLimitedAmount;
			}
			
			
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
		// 		detailVue.empj_BldLimitAmount_AFModel = jsonObj.empj_BldLimitAmount_AF;
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
        // detailVue.empj_BldLimitAmount_AFModel.recordAveragePriceOfBuilding=addThousands(detailVue.empj_BldLimitAmount_AFModel.recordAveragePriceOfBuilding)
        detailVue.empj_BldLimitAmount_AFModel.recordAveragePriceOfBuilding=addThousands(detailVue.empj_BldLimitAmount_AFModel.recordAveragePriceOfBuilding)
        detailVue.empj_BldLimitAmount_AFModel.orgLimitedAmount=addThousands(detailVue.empj_BldLimitAmount_AFModel.orgLimitedAmount)
        detailVue.empj_BldLimitAmount_AFModel.nodeLimitedAmount=addThousands(detailVue.empj_BldLimitAmount_AFModel.nodeLimitedAmount)
        detailVue.empj_BldLimitAmount_AFModel.effectiveLimitedAmount=addThousands(detailVue.empj_BldLimitAmount_AFModel.effectiveLimitedAmount)
        detailVue.empj_BldLimitAmount_AFModel.totalGuaranteeAmount=addThousands(detailVue.empj_BldLimitAmount_AFModel.totalGuaranteeAmount)
        detailVue.empj_BldLimitAmount_AFModel.expectEffectLimitedAmount=addThousands(detailVue.empj_BldLimitAmount_AFModel.expectEffectLimitedAmount)
        detailVue.empj_BldLimitAmount_AFModel.cashLimitedAmount=addThousands(detailVue.empj_BldLimitAmount_AFModel.cashLimitedAmount)
        detailVue.empj_BldLimitAmount_AFModel.expectLimitedAmount=addThousands(detailVue.empj_BldLimitAmount_AFModel.expectLimitedAmount)
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
	
	function initData()
	{
		getButtonList();
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
	"detailDivId":"#empj_BldLimitAmount_AFDetailDiv",
	"detailInterface":"../Empj_BldLimitAmount_AFDetail",
	"exportInterface":"../exportPDFByWord",//导出pdf
    //附件读取
    "loadUploadInterface":"../Sm_AttachmentCfgList",
    "submitInterface":"../Empj_BldLimitAmount_AFApprovalProcess",//提交
    "signatureInterface":"../Sm_SignatureUploadForPath",//签章
    "handleInterface": "../Empj_BldLimitAmount_AFHandler",//确认通过
    //模态框
    "errorModal": "#errorM",
});
