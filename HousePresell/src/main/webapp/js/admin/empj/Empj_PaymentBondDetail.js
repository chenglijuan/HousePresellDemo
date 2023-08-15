(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
            errMsg :"",//存放错误提示信息
            empj_PaymentBondDetailModel : {},
            loadUploadList : [],
            showDelete : false,
            tableId:"1",
            empj_PaymentBondChildList : [],
            qs_companyNameList : [], //页面加载显示开发企业
			qs_projectNameList : [], //显示项目名称
			guaranteeCompanyList : [],//
			subDisabled : false,
            busiType: "",
            subBox : true,
            showSubFlag : true,
          //分页
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			hideShow : false,
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			indexMethod : indexMethod,
			editHandle : editHandle,
			loadCompanyName : loadCompanyNameFun, //页面加载显示开发企业方法
			changeCompanyHandle : changeCompanyHandleFun,//改变项目名称
			loadGuaranteeCompany: loadGuaranteeCompany,
			subForm : subForm,//点击提交按钮
			exportFile : exportFile,
			exportFile2 : exportFile2,
			
			getSummaries : getSummaries,//合计
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
		
		return{
			interfaceVersion:this.interfaceVersion,
			tableId : this.tableId,
			busiCode : "06120501", //业务编码
			buttonType:3,//详情按钮
		}
	}
	//列表操作-----------------------页面加载显示开发企业
		function loadCompanyNameFun() {
			loadCompanyName(detailVue,baseInfo.companyNameInterface,function(jsonObj){
				detailVue.qs_companyNameList = jsonObj.emmp_CompanyInfoList;
			},detailVue.errMsg,baseInfo.edModelDivId);
		}
		//列表操作--------------------改变开发企业的方法
		function changeCompanyHandleFun() {
			var model ={
					interfaceVersion : this.interfaceVersion,
					developCompanyId : detailVue.empj_PaymentBondDetailModel.companyId,
			}
			new ServerInterface(baseInfo.changeCompanyInterface).execute(model, function(jsonObj)
				{
					if(jsonObj.result != "success")
					{
						generalErrorModal(jsonObj,jsonObj.info);
					}
					else
					{
						detailVue.qs_projectNameList= jsonObj.empj_ProjectInfoList;
					}
				});
		}
		//新增操作-----------------------支付保证出具单位
		function loadGuaranteeCompany(){
			var model ={
					interfaceVersion : this.interfaceVersion,	
			}
			new ServerInterface(baseInfo.companyNameInterface).execute(model, function(jsonObj)
			{
				if(jsonObj.result != "success")
				{
					generalErrorModal(jsonObj,jsonObj.info);
				}
				else
				{
					detailVue.guaranteeCompanyList = jsonObj.emmp_CompanyInfoList;
				}
			});
		}
	//详情操作--------------
	function refresh()
	{
		new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				detailVue.empj_PaymentBondDetailModel = jsonObj.empj_PaymentBond;
				detailVue.empj_PaymentBondDetailModel.alreadyActualAmount = addThousands(detailVue.empj_PaymentBondDetailModel.alreadyActualAmount);
				detailVue.empj_PaymentBondDetailModel.actualAmount = addThousands(detailVue.empj_PaymentBondDetailModel.actualAmount);
				detailVue.empj_PaymentBondDetailModel.guaranteedAmount = addThousands(detailVue.empj_PaymentBondDetailModel.guaranteedAmount);
				detailVue.empj_PaymentBondDetailModel.guaranteedSumAmount = addThousands(detailVue.empj_PaymentBondDetailModel.guaranteedSumAmount);
				//detailVue.empj_PaymentBondDetailModel.companyName = jsonObj.empj_PaymentGuarantee.companyName;
				detailVue.empj_PaymentBondChildList = jsonObj.empj_PaymentBondChildList;
				detailVue.empj_PaymentBondChildList.forEach(function(item){
					item.orgLimitedAmount1 = item.orgLimitedAmount;
					item.nodeLimitedAmount1 = item.nodeLimitedAmount;
					item.currentEscrowFund1 = item.currentEscrowFund;
					item.spilloverAmount1 = item.spilloverAmount;
					item.controlAmount1 = item.controlAmount;
					item.releaseAmount1 = item.releaseAmount>0?item.releaseAmount:0;
					item.paymentBondAmount1 = item.paymentBondAmount;
					item.effectiveLimitedAmount1 = item.effectiveLimitedAmount;
					item.actualReleaseAmount1 = item.actualReleaseAmount>0?item.actualReleaseAmount:0;
					item.afterCashLimitedAmount1 = item.afterCashLimitedAmount>0?item.afterCashLimitedAmount:0;
					item.canApplyAmount1 = item.canApplyAmount>0?item.canApplyAmount:0;
					item.afterEffectiveLimitedAmount1 = item.afterEffectiveLimitedAmount;
					
					
					item.orgLimitedAmount = addThousands(item.orgLimitedAmount);
					item.nodeLimitedAmount = addThousands(item.nodeLimitedAmount);
					item.currentEscrowFund = addThousands(item.currentEscrowFund);
					item.spilloverAmount = addThousands(item.spilloverAmount);
					item.controlAmount = addThousands(item.controlAmount);
					item.releaseAmount = addThousands(item.releaseAmount>0?item.releaseAmount:0);
					item.paymentBondAmount = addThousands(item.paymentBondAmount);
					item.effectiveLimitedAmount = addThousands(item.effectiveLimitedAmount);
					item.actualReleaseAmount = addThousands(item.actualReleaseAmount>0?item.actualReleaseAmount:0);
					item.afterCashLimitedAmount = addThousands(item.afterCashLimitedAmount>0?item.afterCashLimitedAmount:0);
					item.afterEffectiveLimitedAmount = addThousands(item.afterEffectiveLimitedAmount);
					item.canApplyAmount = addThousands(item.canApplyAmount);
					
				});
				if(detailVue.empj_PaymentBondDetailModel.approvalState !='0' && detailVue.empj_PaymentBondDetailModel.approvalState != '待提交'){
					detailVue.subBox = false;
				}else{
					detailVue.subBox = true;
				}
				/*if(detailVue.empj_PaymentBondDetailModel.busiState == "0"){
					detailVue.subBox = true;
				}else{
					detailVue.subBox = false;
				}*/
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
				console.log(detailVue.loadUploadList)
				if (detailVue.loadUploadList != undefined && detailVue.loadUploadList.length > 0) {
					detailVue.hideShow = true;
				}
				detailVue.changeCompanyHandle();
				detailVue.loadGuaranteeCompany();
			}
			
		});
	}
	function indexMethod(index){
		return generalIndexMethod(index,detailVue);
	}
	//详情操作--------------------------点击编辑
	function editHandle(){
        /*var theTableId = 'jump_' + detailVue.tableId;
        $("#tabContainer").data("tabs").addTab({id: theTableId , text: '修改支付保证申请', closeable: true, url: 'empj/Empj_PaymentGuaranteeApplicationEdit.shtml'});*/
        enterNext2Tab(detailVue.tableId, '修改支付保函申请', 'empj/Empj_PaymentBondEdit.shtml',detailVue.tableId+"06120501");
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
		detailVue.tableId = array[2];
		refresh();
		
	}
	//合计
	function getSummaries(param){
		var columns = param.columns;
        var data = param.data;
        var sums = [];
        
        for (var index = 0; index < columns.length; index++)
        {
        	if(index === 0)
            {
                sums[index] = '总计';
                continue;
            }
        	sums[index] = 0.0; 
        	if(index === 1 || index === 2 || index === 5 || index === 6){
        		sums[index] = ""; 
        	}else{
        		for (var i = 0; i < data.length; i++)
        		{
        			var row = data[i];
        			var value = 0.00;
        			switch (index)
        			{
        				case 3 :
        					value = row["effectiveLimitedAmount1"];
        					break;
	        			case 4 :
	        				value = row["orgLimitedAmount1"];
	        				break;
	        			case 7 :
	        				value = row["nodeLimitedAmount1"];
	        				break;
	        			/*case 8 :
	        				value = row["nodeLimitedAmount1"];
	        				break;*/
	        			case 8 :
	        				value = row["currentEscrowFund1"];
	        				break;
	        			case 9 :
	        				value = row["spilloverAmount1"];
	        				break;
	        			case 10 :
	        				value = row["controlAmount1"];
	        				break;
	        			case 11 :
	        				value = row["releaseAmount1"];
	        				break;
	        			case 12 :
	        				value = row["paymentBondAmount1"];
	        				break;
	        			case 13 :
	        				value = row["actualReleaseAmount1"];
	        				break;
	        			case 14 :
	        				value = row["canApplyAmount1"];
	        				break;
	        			case 15 :
	        				value = row["afterCashLimitedAmount1"];
	        				break;
	        			case 16 :
	        				value = row["afterEffectiveLimitedAmount1"];
	        				break;
        			}
        			if(!isNaN(value))
        			{
        				sums[index] += (value - 0);
        			}
        		}
        		switch (index)
        		{
	        		case 3 :
	        			sums[index] = thousandsToTwoDecimal(sums[index]);
	        			break;
	        		case 4 :
	        			sums[index] = thousandsToTwoDecimal(sums[index]);
	        			break;
	        		case 7 :
	        			sums[index] = thousandsToTwoDecimal(sums[index]);
	        			break;
	        		case 8 :
	        			sums[index] = thousandsToTwoDecimal(sums[index]);
	        			break;
	        		case 9 :
	        			sums[index] = thousandsToTwoDecimal(sums[index]);
	        			break;
	        		case 10 :
	        			sums[index] = thousandsToTwoDecimal(sums[index]);
	        			break;
	        		case 11 :
	        			sums[index] = thousandsToTwoDecimal(sums[index]);
	        			break;
	        		case 12 :
	        			sums[index] = thousandsToTwoDecimal(sums[index]);
	        			break;
	        		case 13 :
	        			sums[index] = thousandsToTwoDecimal(sums[index]);
	        			break;
	        		case 14 :
	        			sums[index] = thousandsToTwoDecimal(sums[index]);
	        			break;
	        		case 15 :
	        			sums[index] = thousandsToTwoDecimal(sums[index]);
	        			break;
	        		case 16 :
	        			sums[index] = thousandsToTwoDecimal(sums[index]);
	        			break;
	        		/*case 17 :
	        			sums[index] = thousandsToTwoDecimal(sums[index]);
	        			break;*/
        		}
        	}
        	    	
            
        }
        return sums;
	}
	function subForm(){
		detailVue.showSubFlag = false;
		
		var model = {
				interfaceVersion:this.interfaceVersion,
				tableId : detailVue.tableId,
				busiCode : "06120501", //业务编码
		}
		
		
		//==无签章--Start--==
		/*model.isSign = "1";
		new ServerInterface(baseInfo.subInterface).execute(model, function(jsonObj) {
			if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
            	generalSuccessModal();
				refresh();
				detailVue.subBox = false;
            }
		});*/
		//==无签章--End--==
		
		
		//==签章开始==
		if(confirmFile(this.loadUploadList) == true){
			
			$(".xszModel").modal({
				backdrop :'static',
				keyboard: false
			});
			
			new ServerInterface(baseInfo.subInterface).execute(model, function(jsonObj) {
				detailVue.flag = true;
				if (jsonObj.result != "success") {
					$(".xszModel").modal('hide');
					generalErrorModal(jsonObj);
				} else {	
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
									console.log("签章结束："+Date.parse(new Date())+ "；；；签章文件返回值TZSignByPos3="+pro3);
									 console.log("保存文件至服务器开始："+Date.parse(new Date()));
							        	     TZPdfViewer.HttpInit();
							            	 TZPdfViewer.HttpAddPostString("name","1111");
							            	 TZPdfViewer.HttpAddPostCurrFile("file2","aaaa");//HttpAddPostCurrFile上传编辑器当前文件，第二个参数传"" ,随即产生Word的文件名
							                 TZPdfViewer.HttpPost("http://"+window.location.host+"/HousePresell/admin/FileUpload.jsp?filename="+filename);
											//TZPdfViewer.HttpPost("http://61.177.71.243:17000/TZPdfServers/FileUpload.jsp?filename="+filename);
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
													$(".xszModel").modal('hide');
													console.log("保存到OSSServer结束："+Date.parse(new Date()));
													if(jsonObj.result != "success")
										            {
										                generalErrorModal(jsonObj);
										            }
													else
													{
														model.isSign = "1";
														new ServerInterface(baseInfo.subInterface).execute(model, function(jsonObj) {
															if(jsonObj.result != "success")
												            {
												                generalErrorModal(jsonObj);
												            }
												            else
												            {
												            	generalSuccessModal();
																refresh();
																detailVue.subBox = false;
												            }
														});
													}
													
												});
										},12000);
							                 
								}else{
									generalErrorModal("","电子签章失败，建议网络环境在10M以上，请重新提交！");
									$(".xszModel").modal('hide');
								}							
								},10000);
							}else{
								generalErrorModal("","文件打开失败，请重新提交！");//签章文件打开失败！
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
		
		
		//======签章结束==========

	}
	
	//打印
	function exportFile(){
		var model ={
			interfaceVersion : this.interfaceVersion,
			sourceId : this.tableId,
			reqAddress : window.location.href,
			sourceBusiCode : '06120501',
		}
		new ServerInterface(baseInfo.exportInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				window.open(jsonObj.pdfUrl,"_blank");
			}
		});
	}
	
	function exportFile2(){
		var model ={
			interfaceVersion : this.interfaceVersion,
			sourceId : this.tableId,
			reqAddress : window.location.href,
			sourceBusiCode : '06120500',
		}
		new ServerInterface(baseInfo.exportInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				window.open(jsonObj.pdfUrl,"_blank");
			}
		});
	}
	
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.loadCompanyName();
	detailVue.initData();

	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#Empj_PaymentBondDetailDiv",
	"edModelDivId":"#edModelPaymentGuaranteeApplicationDetail",
	"sdModelDivId":"#sdModelPaymentGuaranteeApplicationDetail",
	"detailInterface":"../Empj_PaymentBondDetail",
	"loadUploadInterface":"../Sm_AttachmentCfgList",
	"subInterface" : "../Empj_PaymentBondSubmit",
	"companyNameInterface" : "../Emmp_CompanyInfoList", //显示开发企业名称接口
	"changeCompanyInterface" : "../Empj_ProjectInfoList", //改变企业名称接口
	"queryInterface":"../Tgpf_RefundInfoAddQuery",
	"addInterface":"../Tgpf_RefundInfoAdd",
	"bankInterface":"../Tgxy_BankAccountEscrowedList",
	"refundBankAccountInterface":"../Tgxy_BankAccountEscrowedDetail",
	"loadUploadInterface":"../Sm_AttachmentCfgList",
	
    "exportInterface":"../exportPDFByWord",//导出pdf
    "signatureInterface" : "../Sm_SignatureUploadForPath",//签章
});
