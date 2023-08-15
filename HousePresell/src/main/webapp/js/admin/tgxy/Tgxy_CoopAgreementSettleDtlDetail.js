(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tgxy_CoopAgreementSettleDtlModel: {},
			tableId : 1,
			Tgxy_CoopAgreementSettleDtlDetailList : [],
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			loadUploadList: [],//附件
			showDelete : false,
			saveDisabled : false,
			subDisabled : true,
			errMsg : "",
			busiType : "06110304",
			subFormDisabled : false,
			showFlag: false,
			showPrintBtn : false,
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			getExportForm :getExportForm,//导出参数
			exportPdf:exportPdf,//导出pdf
			indexMethod: indexMethod,
			changePageNumber : function(data){
				if (detailVue.pageNumber != data) {
					detailVue.pageNumber = data;
					detailVue.refresh();
				}
			},
			changeCountPerPage:function(data){
				if (detailVue.countPerPage != data) {
					detailVue.countPerPage = data;
					detailVue.refresh();
				}
			},
			getUploadForm : getUploadForm,//获取上传附件参数
			loadUpload : loadUpload,//显示附件
			coopAgreementSettleDtlEditHandle : coopAgreementSettleDtlEditHandle,//修改
			reFundInfoSubmitHandle : reFundInfoSubmitHandle,
		},
		computed:{
			 
		},
		components : {
			"my-uploadcomponent":fileUpload,
			'vue-nav' : PageNavigationVue
		},
		watch:{
			
		}
	});

	//------------------------方法定义-开始------------------//
	 function indexMethod(index) {
		return generalIndexMethod(index, detailVue)
	}
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:this.tableId,
			busiCode : this.busiType,
			pageNumber : detailVue.pageNumber,
			countPerPage : detailVue.countPerPage,
			totalPage : detailVue.totalPage,
		}
	}

	//详情操作--------------
	function refresh()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		tableIdStr = tableIdStr.split("_");
		detailVue.tableId = tableIdStr[2];
		if (this.tableId == null || this.tableId < 1) 
		{
			return;
		}

		getDetail();
	}
	//列表操作-----------------------获取附件参数
	function getUploadForm(){
		return{
			pageNumber : '0',
			busiType : 'Tgpf_RefundInfo',
			interfaceVersion:this.interfaceVersion
		}
	}
	
	//列表操作-----------------------页面加载显示附件类型
	function loadUpload(){
		new ServerInterface(baseInfo.loadUploadInterface).execute(detailVue.getUploadForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});
				generalErrorModal(jsonObj);
			}
			else
			{
				//refresh();
				detailVue.loadUploadList = jsonObj.sm_AttachmentCfgList;
			}
		});
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
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
//				detailVue.Tgxy_CoopAgreementSettleDtlDetailList = jsonObj.tgxy_CoopAgreementSettleDtlList;
				detailVue.tgxy_CoopAgreementSettleDtlModel = jsonObj.tgxy_CoopAgreementSettle;
				detailVue.pageNumber=jsonObj.pageNumber;
				detailVue.countPerPage=jsonObj.countPerPage;
				detailVue.totalPage=jsonObj.totalPage;
				detailVue.totalCount = jsonObj.totalCount;
				console.log(jsonObj.tgxy_CoopAgreementSettle.settlementState);
				if(jsonObj.tgxy_CoopAgreementSettle.settlementState == 1  || jsonObj.tgxy_CoopAgreementSettle.settlementState == 2) {
					detailVue.subFormDisabled = true;
					detailVue.showFlag = true;
				}
				if(jsonObj.tgxy_CoopAgreementSettle.settlementState == 2){
					detailVue.showPrintBtn = true;
				}
			}
		});
	}
	
	//详情操作----------------------提交审批流
	function reFundInfoSubmitHandle() {
		if(confirmFile(this.loadUploadList) == true){
			new ServerInterface(baseInfo.submitmitInterface).execute(detailVue.getSearchForm(), function(jsonObj) {
				if (jsonObj.result != "success") {
					generalErrorModal(jsonObj);
				} else {
					
					if(jsonObj.signatureMap != undefined&&jsonObj.signatureMap != null)
					{
						
						$(".xszModel").modal({
	    					backdrop :'static',
	    					keyboard: false
	    				});
						
						//pdf路径
						var signaturePath = jsonObj.signatureMap.signaturePath;
						//签章关键字
						var signatureKeyword = jsonObj.signatureMap.signatureKeyword;
						
						if(signaturePath != null && signaturePath != '')
						{
							var filename = Math.random().toString(36).substr(2);
							//1.打开文件
							var isOpen = TZPdfViewer.TZOpenPdfByPath(signaturePath,1);
							if(isOpen==0){
								//2.根据关键字签章
								setTimeout(function(item){
									var pro3=TZPdfViewer.TZInsertESealByKeyWord(signatureKeyword);
									
									if(pro3==0&&TZPdfViewer.PageCount>0){
											/*
											 * 保存至服务器
											 */
								        	     TZPdfViewer.HttpInit();
								            	 TZPdfViewer.HttpAddPostString("name","1111");
								            	 TZPdfViewer.HttpAddPostCurrFile("file2","aaaa");//HttpAddPostCurrFile上传编辑器当前文件，第二个参数传"" ,随即产生Word的文件名
								                 TZPdfViewer.HttpPost("http://"+window.location.host+"/HousePresell/admin/FileUpload.jsp?filename="+filename);

								                 setTimeout(function(item){
								                 var model = 
													{
															interfaceVersion : detailVue.interfaceVersion,
															signaturePath : "C:\\uploaded\\"+filename+".pdf",
															signaturePrefixPath :signaturePath,
															fileName : filename,
															urlPath : signaturePath,//签章网络路径
															
													}
													
													new ServerInterface(baseInfo.signatureInterface).execute(model, function(jsonObj){
														$(".xszModel").modal('hide');
															generalSuccessModal();
															detailVue.refresh();
															if(tgxy_CoopAgreementSettleDtlModel.settlementState == 1 || tgxy_CoopAgreementSettleDtlModel.settlementState == 2){
																detailVue.showFlag = true;
																detailVue.subFormDisabled = true;
															};
													});
										},2000);										
										
									}else{
										generalErrorModal("","电子签章失败，建议网络环境在10M以上！");
										$(".xszModel").modal('hide');										
									}								
									
								},1000)
							}else{
								generalErrorModal("","文件打开失败！");
								$(".xszModel").modal('hide');
							}
						}else{
							generalSuccessModal();
							detailVue.refresh();
							if(tgxy_CoopAgreementSettleDtlModel.settlementState == 1 || tgxy_CoopAgreementSettleDtlModel.settlementState == 2){
								detailVue.showFlag = true;
								detailVue.subFormDisabled = true;
							}
						}
					}else{
						generalSuccessModal();
						detailVue.refresh();
						if(tgxy_CoopAgreementSettleDtlModel.settlementState == 1 || tgxy_CoopAgreementSettleDtlModel.settlementState == 2){
							detailVue.showFlag = true;
							detailVue.subFormDisabled = true;
						}
					}
				}
			});
		}
	}
	function coopAgreementSettleDtlEditHandle(){
		/*$("#tabContainer").data("tabs").addTab({
			id: detailVue.tableId , 
			text: '三方协议结算修改', 
			closeable: true, 
			url: 'tgxy/Tgxy_CoopAgreementSettleDtlDetail.shtml'
		});*/
		enterNext2Tab(detailVue.tableId, '三方协议结算修改', 'tgxy/Tgxy_CoopAgreementSettleDtlEdit.shtml',detailVue.tableId+"06110304");
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
		
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tgxy_CoopAgreementSettleDtDetaillDiv",
	"loadUploadInterface":"../Sm_AttachmentCfgList",
	"detailInterface":"../Tgxy_CoopAgreementSettleDetail",
	"submitmitInterface" : "../Tgxy_CoopAgreementSettleSubmit",//提交接口
	"exportInterface":"../exportPDFByWord",//导出pdf
});
