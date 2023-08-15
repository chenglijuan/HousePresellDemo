(function(baseInfo) {
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion : 19000101,
			tgxy_EscrowAgreementModel : {
				disputeResolution : '',
			},
			tableId : 1,

			tgxy_EscrowAgreementDetailList : [],
			creatUserUpdate : [],
			creatUserRecord : [],
			creatCityRegion : [],
			errEscrowAdd : "",
			smAttachmentList : [], //页面显示已上传的文件
			loadUploadList : [],
			showDelete : false,
			busiType : '06110201',
			busiCode : "06110201", //业务编码
			flag: false,
			saveFlag: true,
			isPrint : false,
		},
		methods : {
			//详情
			refresh : refresh,
			initData : initData,
			getSearchForm : getSearchForm,
			EscrowAgreementEdit : EscrowAgreementEdit,
			indexMethod:indexMethod,
			errClose:errClose,
			goToEditHandle: goToEditHandle,
			handlePrint: handlePrint,
			errClose : errClose,
			goToEditHandle : goToEditHandle,
			goToSumitHandle : goToSumitHandle,
			getExportForm : getExportForm,
			exportPdf : exportPdf,
			bldLimitAmountDetail : bldLimitAmountDetail,//跳转受限额度版本
		},
		computed : {

		},
		components : {
			"my-uploadcomponent" : fileUpload
		},
		watch : {

		}
	});

	//------------------------方法定义-开始------------------//
	
	function bldLimitAmountDetail(tableId){
//		enterNextTab(tableId, '受限额度节点详情', 'tgpj/Tgpj_BldLimitAmountVer_AFDetail.shtml',tableId+"06110201");
		enterNewTab(tableId, "受限额度节点详情", "tgpj/Tgpj_BldLimitAmountVer_AFDetail.shtml")
	}
	
	function indexMethod(index) {
		return generalIndexMethod(index, detailVue)
	}
	//详情操作--------------获取"机构详情"参数
	function getSearchForm() {
		return {
			interfaceVersion : this.interfaceVersion,
			tableId : this.tableId,
			busiCode : this.busiCode,
			
		}
	}

	//详情操作--------------
	function refresh() {
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var list = tableIdStr.split("_");
		tableIdStr = list[2];
		detailVue.tableId = tableIdStr;
		if (detailVue.tableId == null || detailVue.tableId < 1) {
			return;
		}

		getDetail();
	}

	function getDetail() {
		new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
				detailVue.errEscrowAdd = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
					backdrop : 'static'
				});
			} else {
				detailVue.tgxy_EscrowAgreementModel = jsonObj.tgxy_EscrowAgreement;
				detailVue.creatCityRegion = jsonObj.tgxy_EscrowAgreement.cityRegion;
				detailVue.creatUserUpdate = jsonObj.tgxy_EscrowAgreement.userUpdate;
				detailVue.creatUserRecord = jsonObj.tgxy_EscrowAgreement.userRecord;
				detailVue.tgxy_EscrowAgreementDetailList = jsonObj.tgxy_EscrowAgreement.buildingInfoList;
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
				if(jsonObj.tgxy_EscrowAgreement.agreementState == "0" && jsonObj.tgxy_EscrowAgreement.businessProcessState == "1") {
					detailVue.flag = true;
				}
				
				//打印按钮控制，法务部门审批通过后可以打印
				/*console.log("jsonObj.workflow:"+jsonObj.workflow);
				if(jsonObj.workflow!=undefined){
					if(jsonObj.workflow == "ZS" || jsonObj.workflow == "1")
					{
						detailVue.isPrint = true;
					}
				}*/
				
				//打印按钮控制 与张春雷确认 提交后即可打印
				if(detailVue.tgxy_EscrowAgreementModel.approvalState != undefined && detailVue.tgxy_EscrowAgreementModel.approvalState != '待提交')
				{
					detailVue.isPrint = true;
				}
				
				
					
			}
		});
	}

	function initData() {
	}
	//编辑跳转方法
	function EscrowAgreementEdit() {
		$("#tabContainer").data("tabs").addTab({
			id : detailVue.tableId,
			text : '修改托管合作协议',
			closeable : true,
			url : 'tgxy/Tgxy_EscrowAgreementEdit.shtml'
		});
	}
	function errClose() {
		$(baseInfo.errorModel).modal('hide');
	}

	function goToEditHandle() {
		enterNext2Tab(detailVue.tableId, '贷款托管合作协议修改', 'tgxy/Tgxy_EscrowAgreementEdit.shtml', detailVue.tableId + "06110201");
	}
	
	function handlePrint()
	{
		var model = {
			interfaceVersion:this.interfaceVersion,
			tableId : detailVue.tableId,
		}
		new ServerInterface(baseInfo.printInterface).execute(model, function(jsonObj){
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
//				window.location.href = "../"+jsonObj.url;
				
				window.open("../"+jsonObj.url);
			}
		});
	}
	
//	function succClose()
//	{
//		$(baseInfo.successModel).modal('hide');
//	}

	//提交审批
	function goToSumitHandle() {
		
		/*
		 * 点击提交后将提交按钮置为不可编辑
		 */
		detailVue.flag = false;
		
		if(confirmFile(this.loadUploadList) == true){
			$(".xszModel").modal({
				backdrop :'static',
				keyboard: false
			});
			new ServerInterface(baseInfo.sumitInterface).execute(detailVue.getSearchForm(), function(jsonObj) {
				if (jsonObj.result != "success") {
					$(".xszModel").modal('hide');
					detailVue.flag = true;
					generalErrorModal(jsonObj);
				} else {
					detailVue.saveFlag = false;
					detailVue.flag = false;
					if(jsonObj.signatureMap != undefined&&jsonObj.signatureMap != null)
					{
						//pdf路径
						var signaturePath = jsonObj.signatureMap.signaturePath;
						console.log("pdf路径："+signaturePath);
						//签章关键字
						var signatureKeyword = jsonObj.signatureMap.signatureKeyword;
						
						if(signaturePath != null && signaturePath != '')
						{
							var filename = Math.random().toString(36).substr(2);
							//1.打开文件
							console.log("打开文件开始："+Date.parse(new Date()));
							var isOpen = TZPdfViewer.TZOpenPdfByPath(signaturePath,1);
							
							//2.根据关键字签章
							if(isOpen==0){							
								
								setTimeout(function(item){
									console.log("打开文件结束："+Date.parse(new Date()) + "；；；打开文件返回值TZOpenPdfByPath="+isOpen);
									var pro3=TZPdfViewer.TZInsertESealByKeyWord(signatureKeyword);
									console.log("签章开始："+Date.parse(new Date()));
								
								if(pro3==0&&TZPdfViewer.PageCount>0){
										/*
										 * 保存至服务器
										 */
									console.log("签章结束："+Date.parse(new Date())+ "；；；签章文件返回值TZSignByPos3="+pro3);
									 console.log("保存文件至服务器开始："+Date.parse(new Date()));
							        	     TZPdfViewer.HttpInit();
							            	 TZPdfViewer.HttpAddPostString("name","1111");
							            	 TZPdfViewer.HttpAddPostCurrFile("file2","aaaa");//HttpAddPostCurrFile上传编辑器当前文件，第二个参数传"" ,随即产生Word的文件名
							                 TZPdfViewer.HttpPost("http://"+window.location.host+"/HousePresell/admin/FileUpload.jsp?filename="+filename);
							                 
							                 setTimeout(function(item){
							                	 console.log("保存文件至服务器结束："+Date.parse(new Date()));
							                 var model1 = 
												{
														interfaceVersion : detailVue.interfaceVersion,
														signaturePath : "C:\\uploaded\\"+filename+".pdf",
														signaturePrefixPath :signaturePath,
														fileName : filename,
														urlPath : signaturePath,//签章网络路径
												}
												
												new ServerInterface(baseInfo.signatureInterface).execute(model1, function(jsonObj){
													
//													generalSuccessModal();
													console.log("保存到OSSServer结束："+Date.parse(new Date()));
													if(jsonObj.result != "success")
										            {
										                generalErrorModal(jsonObj);
										            }
													else
													{
														var model = detailVue.getSearchForm();
														model.isSign = "1";
														new ServerInterface(baseInfo.sumitInterface).execute(model, function(jsonObj) {
															
															$(".xszModel").modal('hide');
															
															if(jsonObj.result != "success")
												            {
												                generalErrorModal(jsonObj);
												            }
												            else
												            {
												            	generalSuccessModal();
												            }
														});
													}
													
												});
										},12000);
							                 
								}else{
									generalErrorModal("","托管合作协议电子签章失败，建议网络环境在10M以上，请重新提交！");
									$(".xszModel").modal('hide');
								}							
								},10000);
							}else{
								generalErrorModal("","托管合作协议文件打开失败，请重新提交！");//签章文件打开失败！
								$(".xszModel").modal('hide');
							}
							
						}else{
							generalErrorModal("","签章文件路径错误，请重新提交！");//签章文件路径错误
							$(".xszModel").modal('hide');
						}
					}else{
						generalErrorModal("","没有找到指定的文件，请重新提交！");//签章文件路径错误
						$(".xszModel").modal('hide');
					}
					
					
					
				}
			});
		}
	}

//列表操作-----------------------导出PDF
	
	function getExportForm() {
		var href = window.location.href;
		return {
			interfaceVersion : this.interfaceVersion,
			sourceId : this.tableId,
			reqAddress : href,
			sourceBusiCode : this.busiCode,
		}
	}
	
	function exportPdf(){
		
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
	//------------------------方法定义-结束------------------//

	//------------------------数据初始化-开始----------------//
	detailVue.refresh();
	detailVue.initData();
//------------------------数据初始化-结束----------------//
})({
	"errorModel" : "#errorEscrowAdd1",
	//	"successModel":"#successEscrowAdd",
	"detailDivId" : "#tgxy_EscrowAgreementDetailDiv",
	"detailInterface" : "../Tgxy_EscrowAgreementDetail",
	"sumitInterface" : "../Tgxy_EscrowAgreementApprovalProcess", //提交
	"printInterface" : "../Tgxy_EscrowAgreementPrint",
	"exportInterface":"../exportPDFByWord",//导出pdf
	"signatureInterface":"../Sm_SignatureUploadForPath",//签章
	

});