(function (baseInfo) {
    var detailVue = new Vue({
        el: baseInfo.detailDivId,
        data: {
            interfaceVersion: 19000101,
            empj_BldLimitAmount_AFModel: {},
            empj_BldLimitAmount_AFModelNew: {},
            tableId: 1,

            userStartId: "10264",
            buildingList: [],
            buildingId: "",
            expectFigureProgressId: "",
            theName: "",
            constructionCode: "",
            buttonType: "",
            // nowBuildingModel:{},

            //附件材料
            busiType: '03030101',
            dialogVisible: false,
            dialogImageUrl: "",
            fileType: "",
            fileList: [],
            showButton: true,
            hideShow: false,
            uploadData: [],
            smAttachmentList: [],//页面显示已上传的文件
            uploadList: [],
            loadUploadList: [],
            showDelete: true,
            limitVerList: [],//受限额度版本节点信息
            trusteeshipContent: "",

            tempId: "",

            //其他
            errMsg: "", //错误提示信息,
            showSubFlag : true,
            // hackRefresh:"",
        },
        methods: {
            //详情
            refresh: refresh,
            initData: initData,
            getSearchForm: getSearchForm,
            //更新
            getUpdateForm: getUpdateForm,
            update: update,
            changeBuildingListener: changeBuildingListener,
            changeVersionListener: changeVersionListener,

            saveAttachment: saveAttachment,
            noSelectVersion:noSelectVersion,

        },
        computed: {},
        components: {
            'vue-select': VueSelect,
            "my-uploadcomponent": fileUpload,
        },
        watch: {}
    });

    //------------------------方法定义-开始------------------//
    //详情操作--------------获取"机构详情"参数
    function getSearchForm() {
        return {
            interfaceVersion: detailVue.interfaceVersion,
            tableId: detailVue.tableId,
        }
    }

    //详情操作--------------
    function refresh() {
        if (detailVue.tableId == null || detailVue.tableId < 1) {
            return;
        }
        // getBuildingList()
        getDetail();
        loadUpload()
    }

    function getDetail() {
        new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function (jsonObj) {
            if (jsonObj.result != "success") {
                noty({"text": jsonObj.info, "type": "error", "timeout": 2000});
            }
            else {
                console.log(jsonObj.empj_BldLimitAmount_AF)
                detailVue.empj_BldLimitAmount_AFModel = jsonObj.empj_BldLimitAmount_AF;
                detailVue.empj_BldLimitAmount_AFModelNew = jsonObj.empj_BldLimitAmount_AFNew;
                detailVue.buildingId = jsonObj.empj_BldLimitAmount_AF.buildingId
                detailVue.limitVerList = jsonObj.versionList
                detailVue.expectFigureProgressId = jsonObj.empj_BldLimitAmount_AF.expectFigureProgressId+"";
                detailVue.trusteeshipContent = jsonObj.trusteeshipContent
                detailVue.empj_BldLimitAmount_AFModel.recordAveragePriceOfBuilding=addThousands(detailVue.empj_BldLimitAmount_AFModel.recordAveragePriceOfBuilding)
                // changeThousands()
            }
        });
    }

    //详情更新操作--------------获取"更新机构详情"参数
    function getUpdateForm() {
        return {
            //附件材料
            busiType: '03030101',
            sourceId: detailVue.tableId,
            generalAttachmentList: this.$refs.listenUploadData.uploadData,

            interfaceVersion: detailVue.interfaceVersion,
            theState: detailVue.empj_BldLimitAmount_AFModel.theState,
            tableId: detailVue.tableId,
            busiState: detailVue.empj_BldLimitAmount_AFModel.busiState,
            eCode: detailVue.empj_BldLimitAmount_AFModel.eCode,
            userStartId: detailVue.empj_BldLimitAmount_AFModel.userStartId,
            lastUpdateTimeStamp: detailVue.empj_BldLimitAmount_AFModel.lastUpdateTimeStamp,
            projectId: detailVue.empj_BldLimitAmount_AFModel.projectId,
            theNameOfProject: detailVue.empj_BldLimitAmount_AFModel.theNameOfProject,
            buildingId: detailVue.buildingId,
            eCodeFromConstruction: detailVue.empj_BldLimitAmount_AFModel.eCodeFromConstruction,
            orgLimitedAmount: commafyback(detailVue.empj_BldLimitAmount_AFModel.orgLimitedAmount),
            currentFigureProgress: detailVue.empj_BldLimitAmount_AFModel.currentFigureProgress,
            currentLimitedRatio: detailVue.empj_BldLimitAmount_AFModel.currentLimitedRatio,
            nodeLimitedAmount: commafyback(detailVue.empj_BldLimitAmount_AFModel.nodeLimitedAmount),
            totalGuaranteeAmount: commafyback(detailVue.empj_BldLimitAmount_AFModel.totalGuaranteeAmount),
            cashLimitedAmount: commafyback(detailVue.empj_BldLimitAmount_AFModel.cashLimitedAmount),
            effectiveLimitedAmount: commafyback(detailVue.empj_BldLimitAmount_AFModel.effectiveLimitedAmount),
            // expectFigureProgress:detailVue.empj_BldLimitAmount_AFModel.expectFigureProgress,
            expectFigureProgressId: detailVue.expectFigureProgressId,
            expectLimitedRatio: detailVue.empj_BldLimitAmount_AFModel.expectLimitedRatio,
            expectLimitedAmount: commafyback(detailVue.empj_BldLimitAmount_AFModel.expectLimitedAmount),
            expectEffectLimitedAmount: commafyback(detailVue.empj_BldLimitAmount_AFModel.expectEffectLimitedAmount),
            remark: detailVue.empj_BldLimitAmount_AFModel.remark,
            buttonType: detailVue.buttonType,
        }
    }

    function update(buttonType) {
        detailVue.buttonType = buttonType;
        if(buttonType == '2')
        {
        	$(".xszModel").modal({
				backdrop :'static',
				keyboard: false
			});
        //	$("#signWarningText").html("正在打开pdf文件，请等待...");
        	detailVue.showSubFlag = false;
        	 new ServerInterfaceJsonBody(baseInfo.updateInterface).execute(detailVue.getUpdateForm(), function (jsonObj) {
             	detailVue.showSubFlag = true;
                 if (jsonObj.result != "success") {
                     generalErrorModal(jsonObj);
                 }
                 else {
                 	
                 	if(buttonType == '2')
                 	{
                 		console.log(jsonObj.signatureMap)
                     	if(jsonObj.signatureMap != undefined&&jsonObj.signatureMap != null)
         	    			{
         						/*$(".xszModel").modal({
         							backdrop :'static',
         							keyboard: false
         						});*/
         					
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
//	         											enterNewTabCloseCurrent(jsonObj.tableId,"受限额度变更详情","empj/Empj_BldLimitAmount_AFDetail.shtml");
	         											if(jsonObj.result != "success")
											            {
											                generalErrorModal(jsonObj);
											            }
														else
														{
															var model = detailVue.getUpdateForm();
															model.isSign = "1";
															 new ServerInterfaceJsonBody(baseInfo.updateInterface).execute(model, function(jsonObj3) {
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
                     
                 	}
                 	
//                     // $(baseInfo.detailDivId).modal('hide');
//                     generalSuccessModal()
//                     enterNewTabCloseCurrent(jsonObj.tableId, "受限额度变更详情", "empj/Empj_BldLimitAmount_AFDetail.shtml")
//                     // refresh();
                 }
             });
        }else{
        	 new ServerInterfaceJsonBody(baseInfo.updateInterface).execute(detailVue.getUpdateForm(), function (jsonObj) {
             	detailVue.showSubFlag = true;
                 if (jsonObj.result != "success") {
                     generalErrorModal(jsonObj);
                 }
                 else {
     					generalSuccessModal();
     					enterNewTabCloseCurrent(jsonObj.tableId,"受限额度变更详情","empj/Empj_BldLimitAmount_AFDetail.shtml");
                 }
             });
        }
        
       
    }

    function getBuildingList() {
        serverRequest(baseInfo.getBuildingList, {
            interfaceVersion: 19000101,
            tableId: detailVue.userStartId
        }, function (jsonObj) {
            console.log(jsonObj)
            detailVue.buildingList = jsonObj.buildingList
        })
    }

    function changeBuildingListener(data) {
        console.log(data)
        detailVue.buildingId = data.tableId
        detailVue.constructionCode = data.theName
        detailVue.theName = data.eCodeFromConstruction
        // detailVue.nowBuildingModel=data
        detailVue.empj_BldLimitAmount_AFModel.developCompanyName = data.developCompanyName
        detailVue.empj_BldLimitAmount_AFModel.projectName = data.projectName
        detailVue.empj_BldLimitAmount_AFModel.projectId = data.projectId
        detailVue.empj_BldLimitAmount_AFModel.upfloorNumber = data.upfloorNumber
        detailVue.empj_BldLimitAmount_AFModel.eCodeFromConstruction = data.eCodeFromConstruction
        detailVue.empj_BldLimitAmount_AFModel.eCodeFromPresellSystem = data.eCodeFromPresellSystem
        detailVue.empj_BldLimitAmount_AFModel.buildingArea = data.buildingArea
        detailVue.empj_BldLimitAmount_AFModel.eCodeFromPublicSecurity = data.eCodeFromPublicSecurity
        detailVue.empj_BldLimitAmount_AFModel.escrowArea = data.escrowArea
        detailVue.empj_BldLimitAmount_AFModel.escrowStandard = data.escrowStandard
        detailVue.empj_BldLimitAmount_AFModel.deliveryType = data.deliveryType
        detailVue.empj_BldLimitAmount_AFModel.theName = data.eCodeFromConstruction
        changeThousands()
        // getProcessVersionList()
    }

    function loadUpload() {
        generalLoadFile2(detailVue, detailVue.busiType)
    }

    function saveAttachment() {
        generalUploadFile(detailVue, "Tgxy_BankAccountEscrowed", baseInfo.attachmentBatchAddInterface, baseInfo.successModel)
    }

    function changeVersionListener(data) {
        detailVue.expectFigureProgressId = data.tableId
        for (var i = 0; i < detailVue.limitVerList.length; i++) {
            var item = detailVue.limitVerList[i]
            if (item.tableId == data.tableId) {
                console.log('item is ')
                console.log(item)
                var form = {
                    interfaceVersion: 19000101,
                    orgLimitedAmount: detailVue.empj_BldLimitAmount_AFModel.orgLimitedAmount,
                    expectLimitedRatio: item.limitedAmount,
                    cashLimitedAmount: detailVue.empj_BldLimitAmount_AFModel.cashLimitedAmount
                }
                serverBodyRequest(baseInfo.calculateExpectLimit, form, function (jsonObj) {
                    detailVue.empj_BldLimitAmount_AFModel.expectLimitedAmount = jsonObj.expectLimitedAmount
                    detailVue.empj_BldLimitAmount_AFModel.expectEffectLimitedAmount = jsonObj.expectEffectLimitedAmount
                    changeVersionThousands()
                })
                // detailVue.empj_BldLimitAmount_AFModel.expectLimitedAmount=item.limitedAmount*detailVue.empj_BldLimitAmount_AFModel.orgLimitedAmount/100
                detailVue.empj_BldLimitAmount_AFModel.expectLimitedRatio = item.limitedAmount

                // if(detailVue.empj_BldLimitAmount_AFModel.expectLimitedRatio<detailVue.empj_BldLimitAmount_AFModel.cashLimitedAmount){
                //    detailVue.empj_BldLimitAmount_AFModel.expectEffectLimitedAmount=detailVue.empj_BldLimitAmount_AFModel.expectLimitedRatio
                // }else{
                //    detailVue.empj_BldLimitAmount_AFModel.expectEffectLimitedAmount=detailVue.empj_BldLimitAmount_AFModel.cashLimitedAmount
                // }

                break
            }
        }
    }

    function noSelectVersion() {
        detailVue.expectFigureProgressId = ""
        detailVue.empj_BldLimitAmount_AFModel.expectLimitedAmount = ""
        detailVue.empj_BldLimitAmount_AFModel.expectEffectLimitedAmount = ""
        detailVue.empj_BldLimitAmount_AFModel.expectLimitedRatio = ""

    }

    function getProcessVersionList() {
        var form = {
            interfaceVersion: 19000101,
            tableId: detailVue.buildingId,
            nowLimitRatio: detailVue.empj_BldLimitAmount_AFModel.currentLimitedRatio,
        }
        // serverRequest("../Empj_BldGetLimitAmountVer", form, function (jsonObj) {
        serverRequest("../Empj_BldAccountGetLimitAmountVer", form, function (jsonObj) {
            detailVue.limitVerList = jsonObj.versionList
            // detailVue.hackRefresh=false
            // detailVue.expectFigureProgressId=detailVue.tempId
            // detailVue.hackRefresh=true
            // console.log('detailVue.expectFigureProgressId in get is '+detailVue.expectFigureProgressId)
            // Vue.nextTick(function(){
            //     // console.log(vm.$el.textContent) //可以得到'changed'
            //     detailVue.expectFigureProgressId=detailVue.tempId
            //     console.log('完成赋值 '+detailVue.expectFigureProgressId)
            // })


            for (var i = 0; i < detailVue.limitVerList.length; i++) {
                var item = detailVue.limitVerList[i]
                if (item.tableId = detailVue.expectFigureProgressId) {
                    console.log('找到了')
                    console.log($('#progressSelect'))
                    // $('#progressSelect').value(item.theName)
                    break
                }
            }
        })
    }

    function changeThousands() {
        detailVue.empj_BldLimitAmount_AFModel.orgLimitedAmount=addThousands(detailVue.empj_BldLimitAmount_AFModel.orgLimitedAmount)
        detailVue.empj_BldLimitAmount_AFModel.nodeLimitedAmount=addThousands(detailVue.empj_BldLimitAmount_AFModel.nodeLimitedAmount)
        detailVue.empj_BldLimitAmount_AFModel.expectLimitedAmount=addThousands(detailVue.empj_BldLimitAmount_AFModel.expectLimitedAmount)
        detailVue.empj_BldLimitAmount_AFModel.totalGuaranteeAmount=addThousands(detailVue.empj_BldLimitAmount_AFModel.totalGuaranteeAmount)
        detailVue.empj_BldLimitAmount_AFModel.cashLimitedAmount=addThousands(detailVue.empj_BldLimitAmount_AFModel.cashLimitedAmount)
    }

    function changeVersionThousands() {
        detailVue.empj_BldLimitAmount_AFModel.effectiveLimitedAmount=addThousands(detailVue.empj_BldLimitAmount_AFModel.effectiveLimitedAmount)
        detailVue.empj_BldLimitAmount_AFModel.expectEffectLimitedAmount=addThousands(detailVue.empj_BldLimitAmount_AFModel.expectEffectLimitedAmount)
    }

    function initData() {
        getIdFormTab("", function (id) {
            detailVue.tableId = id
            console.log('id is ' + detailVue.tableId)
            refresh()

        })
    }

    //------------------------方法定义-结束------------------//

    //------------------------数据初始化-开始----------------//
    // detailVue.refresh();
    detailVue.initData();
    //------------------------数据初始化-结束----------------//
})({
    "detailDivId": "#empj_BldLimitAmountEditDiv",
    "detailInterface": "../Empj_BldLimitAmount_AFDetail",
    "updateInterface": "../Empj_BldLimitAmount_AFUpdate",
    "getBuildingList": "../Sm_UserGetBuildingList",
    "calculateExpectLimit": "../Empj_BldLimitAmountCalculate",
    "signatureInterface":"../Sm_SignatureUploadForPath",//签章
});
