(function(baseInfo){
    var detailVue = new Vue({
        el : baseInfo.addDivId,
        data : {
            interfaceVersion :19000101,
            empj_BldLimitAmount_AFModel: {},
            user:{},
            companyId:"",
            projectList:[],
            projectId:"",
            projectName:"",
            buildingList:[],
            buildingId:"",
            constructionCode:"",
            nowBuildingModel:{},
            expectFigureProgressId:"",

            buttonType:"",

            // 附件材料
            busiType:'03030101',
            loadUploadList: [],
            showDelete : true,  // 附件是否可编辑
            limitVerList:[],// 受限额度版本节点信息

            selectBuildingAccount:{},
            companyName:"",
            isNormalUser:"",
            companyList:[],
            escrowStandardTypeName:"",
            expectLimitedAmount: "",
            expectEffectLimitedAmount: "",
            showSubFlag : true,
        },
        methods : {
            // 详情
            refresh : refresh,
            initData: initData,
            getSearchForm : getSearchForm,
            // 添加
            getAddForm : getAddForm,
            add: add,
            changeBuildingListener: changeBuildingListener,
            changeProjectListener: changeProjectListener,
            changeVersionListener: changeVersionListener,
            companyChangeHandle: companyChangeHandle,
            changeThousands: changeThousands,

            noSelectVersion:noSelectVersion,
            noSelectProject:noSelectProject,
            noSelectBuilding:noSelectBuilding,
            noSelectCompany:noSelectCompany,
        },
        computed:{

        },
        components: {
            'vue-select': VueSelect,
            "my-uploadcomponent": fileUpload,
        },
        watch: {

        }
    });

    // ------------------------方法定义-开始------------------//
    // 详情操作--------------获取"机构详情"参数
    function getSearchForm()
    {
        return {
            interfaceVersion:detailVue.interfaceVersion,
        }
    }

    // 详情操作--------------
    function refresh()
    {

    }

    // 详情更新操作--------------获取"更新机构详情"参数
    function getAddForm()
    {
        return {
            interfaceVersion:detailVue.interfaceVersion,
            theState:detailVue.empj_BldLimitAmount_AFModel.theState,
            busiState:detailVue.empj_BldLimitAmount_AFModel.busiState,
            eCode:detailVue.empj_BldLimitAmount_AFModel.eCode,
            developCompanyId:detailVue.nowBuildingModel.developCompanyId,
            eCodeOfDevelopCompany:detailVue.nowBuildingModel.developCompanyEcode,
            projectId:detailVue.nowBuildingModel.projectId,
            theNameOfProject:detailVue.nowBuildingModel.projectName,
            eCodeOfProject:detailVue.empj_BldLimitAmount_AFModel.eCodeOfProject,
            buildingId:detailVue.buildingId,
            eCodeOfBuilding:detailVue.empj_BldLimitAmount_AFModel.eCodeOfBuilding,
            upfloorNumber:detailVue.nowBuildingModel.upfloorNumber,
            eCodeFromConstruction:detailVue.nowBuildingModel.eCodeFromConstruction,
            eCodeFromPublicSecurity:detailVue.nowBuildingModel.eCodeFromPublicSecurity,
            // recordAveragePriceOfBuilding:detailVue.empj_BldLimitAmount_AFModel.recordAveragePriceOfBuilding,
            escrowStandard:detailVue.nowBuildingModel.escrowStandard,
            deliveryType:detailVue.nowBuildingModel.deliveryType,
            orgLimitedAmount:commafyback(detailVue.selectBuildingAccount.orgLimitedAmount),
            currentFigureProgress:detailVue.selectBuildingAccount.currentFigureProgress,
            currentLimitedRatio:detailVue.selectBuildingAccount.currentLimitedRatio,
            nodeLimitedAmount:commafyback(detailVue.selectBuildingAccount.nodeLimitedAmount),
            totalGuaranteeAmount:commafyback(detailVue.selectBuildingAccount.totalGuaranteeAmount),
            cashLimitedAmount:commafyback(detailVue.selectBuildingAccount.cashLimitedAmount),
            effectiveLimitedAmount:commafyback(detailVue.selectBuildingAccount.effectiveLimitedAmount),
            // expectFigureProgress:detailVue.empj_BldLimitAmount_AFModel.expectFigureProgress,
            expectFigureProgressId:detailVue.expectFigureProgressId,
            expectLimitedRatio:detailVue.selectBuildingAccount.expectLimitedRatio,
            expectLimitedAmount:detailVue.selectBuildingAccount.expectLimitedAmount,
            expectEffectLimitedAmount:commafyback(detailVue.selectBuildingAccount.expectEffectLimitedAmount),
            remark:detailVue.empj_BldLimitAmount_AFModel.remark,
            buttonType:detailVue.buttonType,
            // 附件材料
            busiType : '03030101',
            generalAttachmentList : this.$refs.listenUploadData.uploadData	}
    }

    function add(buttonType)
    {
        detailVue.buttonType=buttonType;
        if(buttonType == '2')
        {
        	detailVue.showSubFlag = false;
        	$(".xszModel").modal({
				backdrop :'static',
				keyboard: false
			});
        	 new ServerInterfaceJsonBody(baseInfo.addInterface).execute(detailVue.getAddForm(), function (jsonObj) {
     			detailVue.showSubFlag = true;
     	        if (jsonObj.result != "success") {
     	        	$(".xszModel").modal('hide');
     	        	generalErrorModal(jsonObj);
     	        }
     	        else 
     	        {
     	        	  if(buttonType == '2')
     	              {
     	              	if(jsonObj.signatureMap != undefined&&jsonObj.signatureMap != null)
     	  	    			{
     	              		/*$(".xszModel").modal({
 	  							backdrop :'static',
 	  							keyboard: false
 	  						});*/
     	  					// pdf路径
     	  					var signaturePath = jsonObj.signatureMap.signaturePath;
     	  					// 签章关键字
     	  					var signatureKeyword = jsonObj.signatureMap.signatureKeyword;
     	  					
     	  					
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
     	  								var pro3=TZPdfViewer.TZInsertESealByKeyWord(signatureKeyword);
     	  								
     	  								if(pro3==0&&TZPdfViewer.PageCount>0){
     	  									// 3. 保存至服务器
     	  									console.log("签章结束："+Date.parse(new Date())+ "；；；签章文件返回值TZSignByPos3="+pro3);
     	  									console.log("保存文件至服务器开始："+Date.parse(new Date()));
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
//		 	  											enterNewTabCloseCurrent(jsonObj.tableId,"受限额度变更详情","empj/Empj_BldLimitAmount_AFDetail.shtml");
		 	  											
		 	  											if(jsonObj.result != "success")
											            {
											                generalErrorModal(jsonObj);
											            }
														else
														{
															var model = detailVue.getAddForm();
															model.isSign = "1";
															new ServerInterfaceJsonBody(baseInfo.addInterface).execute(model, function(jsonObj3) {
																if(jsonObj3.result != "success")
													            {
													                generalErrorModal(jsonObj3);
													            }
													            else
													            {
													            	generalSuccessModal();
													            	enterNewTabCloseCurrent(jsonObj3.tableId,"受限额度变更详情","empj/Empj_BldLimitAmount_AFDetail.shtml");
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
     	  						enterNewTabCloseCurrent(jsonObj.tableId,"受限额度变更详情","empj/Empj_BldLimitAmount_AFDetail.shtml");
     	  					}
     	  				}else{
     	  					$(".xszModel").modal('hide');
     	  					generalSuccessModal();
     	  					enterNewTabCloseCurrent(jsonObj.tableId,"受限额度变更详情","empj/Empj_BldLimitAmount_AFDetail.shtml");
     	  				}
     	              }else{
     	            	 $(".xszModel").modal('hide');
     	              	generalSuccessModal();
     	                  enterNewTabCloseCurrent(jsonObj.tableId,"受限额度变更详情","empj/Empj_BldLimitAmount_AFDetail.shtml");
     	              }
     	        }
     	    });
        }else{
        	 new ServerInterfaceJsonBody(baseInfo.addInterface).execute(detailVue.getAddForm(), function (jsonObj) {
     			detailVue.showSubFlag = true;
     	        if (jsonObj.result != "success") {
     	        	generalErrorModal(jsonObj)
     	        }
     	        else {
     	        	generalSuccessModal();
	                enterNewTabCloseCurrent(jsonObj.tableId,"受限额度变更详情","empj/Empj_BldLimitAmount_AFDetail.shtml");
     	        }
     	    });
        }
        
        
       
    /*    serverBodyRequest(baseInfo.addInterface,detailVue.getAddForm(),function (jsonObj) {
            // 如果执行的提交操作，判断是否具有签章资质
            if(buttonType == '2')
            {
            	if(jsonObj.signatureMap != undefined&&jsonObj.signatureMap != null)
	    			{
						$(".xszModel").modal({
							backdrop :'static',
							keyboard: false
						});
					
					// pdf路径
					var signaturePath = jsonObj.signatureMap.signaturePath;
					// 签章关键字
					var signatureKeyword = jsonObj.signatureMap.signatureKeyword;
					
					
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
								var pro3=TZPdfViewer.TZInsertESealByKeyWord(signatureKeyword);
								
								if(pro3==0&&TZPdfViewer.PageCount>0){
									// 3. 保存至服务器
									console.log("签章结束："+Date.parse(new Date())+ "；；；签章文件返回值TZSignByPos3="+pro3);
									console.log("保存文件至服务器开始："+Date.parse(new Date()));
									TZPdfViewer.HttpInit();
									TZPdfViewer.HttpAddPostString("name","1111");
									TZPdfViewer.HttpAddPostCurrFile("file2","aaaa");// HttpAddPostCurrFile上传编辑器当前文件，第二个参数传""
																										// ,随即产生Word的文件名
									TZPdfViewer.HttpPost("http://"+window.location.host+"/HousePresell/admin/FileUpload.jsp?filename="+filename);
									                 
									setTimeout(function(item){
										console.log("保存文件至服务器结束："+Date.parse(new Date()));
										var model =
											{
												interfaceVersion : detailVue.interfaceVersion,
												signaturePath : "C:\\uploaded\\"+filename+".pdf",
												signaturePrefixPath :signaturePath,
												fileName : filename,
												urlPath : signaturePath,// 签章网络路径
											}
										new ServerInterface(baseInfo.signatureInterface).execute(model, function(jsonObj2){
											$(".xszModel").modal('hide');
											generalSuccessModal();
											console.log("保存到OSSServer结束："+Date.parse(new Date()));
											enterNewTabCloseCurrent(jsonObj.tableId,"受限额度变更详情","empj/Empj_BldLimitAmount_AFDetail.shtml");

											});
									},4000);		
								}
								else{
									generalErrorModal("","电子签章失败，建议网络环境在10M以上！");// 签章文件打开失败！
									$(".xszModel").modal('hide');
								}						
								
								},2000);
							
						}else{
							generalErrorModal("","文件打开失败！");// 签章文件打开失败！
							$(".xszModel").modal('hide');
						}					
						
					}else{
						generalSuccessModal();
						enterNewTabCloseCurrent(jsonObj.tableId,"受限额度变更详情","empj/Empj_BldLimitAmount_AFDetail.shtml");
					}
				}else{
					generalSuccessModal();
					enterNewTabCloseCurrent(jsonObj.tableId,"受限额度变更详情","empj/Empj_BldLimitAmount_AFDetail.shtml");
				}
            }else{
            	generalSuccessModal();
                enterNewTabCloseCurrent(jsonObj.tableId,"受限额度变更详情","empj/Empj_BldLimitAmount_AFDetail.shtml");
            }
			
        });*/
        
        
        
        
        
    }

    function getProjectList() {
        serverRequest(baseInfo.projectListInterface,{
            interfaceVersion: 19000101,
            pageNumber: 1,
            countPerPage: MAX_VALUE,
            theState: 0,
            totalPage: 1,
            keyword: "",
            developCompanyId:detailVue.companyId,
        },function (jsonObj) {
            detailVue.projectList=jsonObj.empj_ProjectInfoList
        })
    }

    function getBuildingList() {
        serverRequest(baseInfo.buildingListInterface,{
            interfaceVersion: 19000101,
            pageNumber: 1,
            countPerPage: MAX_VALUE,
            theState: 0,
            totalPage: 1,
            keyword: "",
            projectId:detailVue.projectId,
        },function (jsonObj) {
            detailVue.buildingList=jsonObj.empj_BuildingInfoList
            for(var i=0;i<detailVue.buildingList.length;i++){
                detailVue.buildingList[i].theName=detailVue.buildingList[i].eCodeFromConstruction
            }
        })
    }

    function changeProjectListener(data) {
        if(data.tableId!=detailVue.projectId){
            noSelectVersion()
            detailVue.projectId=data.tableId
            detailVue.projectName=data.theName
            detailVue.nowBuildingModel={}
            getBuildingList()
            detailVue.selectBuildingAccount={}
        }
    }

    function changeBuildingListener(data) {
        noSelectVersion()
        if(data.tableId!=detailVue.buildingId){
            detailVue.buildingId = data.tableId
            detailVue.constructionCode = data.theName
            // detailVue.nowBuildingModel.recordAvgPriceOfBuilding =""
            detailVue.nowBuildingModel=data
            // getIsUniqueBuilding()
            // changeThousands()
            detailVue.nowBuildingModel.recordAvgPriceOfBuilding = thousandsToTwoDecimal(detailVue.nowBuildingModel.recordAvgPriceOfBuilding);

            getBuildingDetail()
        }

    }

    function getBuildingDetail() {
        var form={
            interfaceVersion:19000101,
            tableId:detailVue.buildingId,
            theState:0
        }
        serverRequest(baseInfo.buildingDetailInterface,form,function (jsonObj) {
            detailVue.selectBuildingAccount=jsonObj.buildingInfo.buildingAccount
            detailVue.escrowStandardTypeName=jsonObj.trusteeshipContent
            if(detailVue.selectBuildingAccount.orgLimitedAmount==0){
                generalErrorModal(undefined,"初始受限额度为空，请先增加备案均价")
                return
            }
            if(detailVue.selectBuildingAccount.currentLimitedRatio==0){
                generalErrorModal(undefined,"该楼幢已经完成交付，无法添加受限额度变更")
                return
            }
            changeThousands()
            getProcessVersionList()

        })
    }

    function changeVersionListener(data) {
        detailVue.expectFigureProgressId=data.tableId
        for(var i=0;i<detailVue.limitVerList.length;i++){
            var item=detailVue.limitVerList[i]
            if(item.tableId==data.tableId){
                var form={
                    interfaceVersion:19000101,
                    orgLimitedAmount:commafyback(detailVue.selectBuildingAccount.orgLimitedAmount),
                    expectLimitedRatio:item.limitedAmount,
                    cashLimitedAmount:commafyback(detailVue.selectBuildingAccount.cashLimitedAmount)
                }
                serverBodyRequest(baseInfo.calculateExpectLimit,form,function (jsonObj) {
                    // setTimeout(function () {
                    // TODO 进入回调后却没有走set text
                    detailVue.selectBuildingAccount.expectLimitedAmount = jsonObj.expectLimitedAmount
                    detailVue.selectBuildingAccount.expectEffectLimitedAmount = jsonObj.expectEffectLimitedAmount
                    detailVue.expectLimitedAmount = jsonObj.expectLimitedAmount;
                    detailVue.expectEffectLimitedAmount = jsonObj.expectEffectLimitedAmount;
                    // changeThousands()
                    changeVersionThousands()
                    // },100)
                })
                // detailVue.empj_BldLimitAmount_AFModel.expectLimitedAmount=item.limitedAmount*detailVue.empj_BldLimitAmount_AFModel.orgLimitedAmount/100
                detailVue.selectBuildingAccount.expectLimitedRatio=item.limitedAmount
                // changeThousands()
                // if(detailVue.empj_BldLimitAmount_AFModel.expectLimitedRatio<detailVue.empj_BldLimitAmount_AFModel.cashLimitedAmount){
                // detailVue.empj_BldLimitAmount_AFModel.expectEffectLimitedAmount=detailVue.empj_BldLimitAmount_AFModel.expectLimitedRatio
                // }else{
                // detailVue.empj_BldLimitAmount_AFModel.expectEffectLimitedAmount=detailVue.empj_BldLimitAmount_AFModel.cashLimitedAmount
                // }

                break
            }
        }
    }

    function noSelectVersion() {
        detailVue.expectFigureProgressId=""
        detailVue.selectBuildingAccount.expectLimitedAmount = ""
        detailVue.selectBuildingAccount.expectEffectLimitedAmount = ""
        detailVue.expectLimitedAmount = "";
        detailVue.expectEffectLimitedAmount = "";
        detailVue.selectBuildingAccount.expectLimitedRatio = "";
    }

    function noSelectCompany() {
        noSelectProject()
        detailVue.companyId = ""
        detailVue.projectList=[]
        detailVue.projectId=""
        detailVue.buildingList=[]
        detailVue.buildingId=""
        detailVue.limitVerList=[]
        detailVue.expectFigureProgressId=""
    }

    function noSelectProject() {
        detailVue.projectId = ""
        detailVue.projectName=""
        detailVue.buildingList=[]
        detailVue.nowBuildingModel={}
        detailVue.selectBuildingAccount={}
        noSelectBuilding()
    }

    function noSelectBuilding() {
        noSelectVersion()
        detailVue.buildingId = ""
        detailVue.constructionCode = ""
        detailVue.nowBuildingModel={}
        detailVue.selectBuildingAccount=""
        detailVue.escrowStandardTypeName=""
        detailVue.limitVerList=[]
    }

    // function getIsUniqueBuilding() {
    // var form={
    // interfaceVersion:19000101,
    // buildingId:detailVue.buildingId
    // }
    // serverRequest("../Empj_BldLimitAmountIsUnique",form,function (jsonObj) {
    // var isUnique=jsonObj.isUnique
    // if(!isUnique){
    // generalErrorModal(undefined,"该楼幢对应的受限额度已存在，无法重复添加")
    // }
    // })
    // }

    function getUserDetail() {
        getLoginUserInfo(function (user) {
            detailVue.user=user;
            if(detailVue.user.theType=="1"){// 如果是正泰用户
                detailVue.isNormalUser=1
                getCompanyList()
            }else{// 如果是一般用户
                detailVue.companyId=user.developCompanyId
                detailVue.companyName = user.theNameOfCompany
                detailVue.isNormalUser=0
                getProjectList()
            }
            // getProjectList()
        })
    }

    function getProcessVersionList() {
        var form={
            interfaceVersion:19000101,
            tableId:detailVue.buildingId,
            nowLimitRatio:detailVue.selectBuildingAccount.currentLimitedRatio
        }
        // serverRequest("../Empj_BldGetLimitAmountVer",form,function (jsonObj)
		// {
        serverRequest("../Empj_BldAccountGetLimitAmountVer",form,function (jsonObj) {
            detailVue.limitVerList=jsonObj.versionList
            for(var i=0;i<detailVue.limitVerList.length;i++){
                var item =detailVue.limitVerList[i]
                // item.theName=item.stageName+"-"+item.limitedAmount+"%"
                item.theName=item.stageName
            }
            // changeThousands()
        })
    }

    function companyChangeHandle(data) {
        if(data.tableId!=detailVue.companyId){
            detailVue.companyId = data.tableId
            detailVue.projectList=[]
            detailVue.projectId=""
            detailVue.buildingList=[]
            detailVue.buildingId=""
            detailVue.limitVerList=[]
            detailVue.expectFigureProgressId=""
            getProjectList()
        }
    }

    function getCompanyList() {
        serverRequest(baseInfo.companyListInterface,getTotalListForm(),function (jsonObj) {
            detailVue.companyList=jsonObj.emmp_CompanyInfoList;
        })
    }

    function changeThousands() {
        detailVue.selectBuildingAccount.orgLimitedAmount = thousandsToTwoDecimal(detailVue.selectBuildingAccount.orgLimitedAmount);
        detailVue.selectBuildingAccount.nodeLimitedAmount = thousandsToTwoDecimal(detailVue.selectBuildingAccount.nodeLimitedAmount);
        detailVue.selectBuildingAccount.effectiveLimitedAmount = thousandsToTwoDecimal(detailVue.selectBuildingAccount.effectiveLimitedAmount);
        detailVue.selectBuildingAccount.totalGuaranteeAmount = thousandsToTwoDecimal(detailVue.selectBuildingAccount.totalGuaranteeAmount);
        detailVue.selectBuildingAccount.cashLimitedAmount = thousandsToTwoDecimal(detailVue.selectBuildingAccount.cashLimitedAmount);
    }

    function changeVersionThousands() {
        detailVue.expectLimitedAmount = thousandsToTwoDecimal(detailVue.expectLimitedAmount);
        detailVue.expectEffectLimitedAmount = thousandsToTwoDecimal(detailVue.expectEffectLimitedAmount);
    }


    function initData()
    {
        generalLoadFile2(detailVue, detailVue.busiType)
        getUserDetail()
        // getProcessVersionList()
        // getBuildingList()

    }
    // ------------------------方法定义-结束------------------//

    // ------------------------数据初始化-开始----------------//
    // detailVue.refresh();
    detailVue.initData();
    // ------------------------数据初始化-结束----------------//
})({
    "addDivId":"#empj_BldLimitAmount_AFAddDiv",
    "detailInterface":"../Empj_BldLimitAmount_AFDetail",
    "addInterface":"../Empj_BldLimitAmount_AFAdd",
    "getBuildingList":"../Sm_UserGetBuildingList",
    "projectListInterface":"../Empj_ProjectInfoForSelect",
    "buildingListInterface":"../Empj_BuildingInfoForSelect",
    "buildingDetailInterface":"../Empj_BuildingInfoDetailWithService",
    "calculateExpectLimit":"../Empj_BldLimitAmountCalculate",
    "companyListInterface":"../Emmp_CompanyInfoForSelect",
    "signatureInterface":"../Sm_SignatureUploadForPath",// 签章
});
