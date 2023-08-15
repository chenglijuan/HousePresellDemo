(function (baseInfo) {
    var detailVue = new Vue({
        el: baseInfo.detailDivId,
        data: {
            interfaceVersion: 19000101,
            empj_ProjProgInspection_AFModel: {},
            empj_ProjProgInspection_AFModelNew: {},
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
            busiType: '03030203',
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

            editFile : false,
            saveFile : false,
            determineList:[
            	{   
            		value: '1',
                    label: '正常'
                },
                {   
                	value: '2',
                	label: '延期'
                },
                {   
                	value: '3',
                	label: '滞后'
                },
                {   
                	value: '4',
                	label: '停工'
                },
            ],
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
            openFileModel : openFileModel,
            dialogSave : dialogSave,//保存附件信息 
            editFileActive : editFileActive,
            saveFileActive : saveFileActive,

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
            	
                detailVue.empj_ProjProgInspection_AFModel = jsonObj.empj_ProjProgInspection_AF;
              //子表数据
                detailVue.buildingList = jsonObj.empj_ProjProgInspection_DTLList;
                $('#date03030203').val(jsonObj.empj_ProjProgInspection_DTLList.forcastTime);
                detailVue.limitVerList = jsonObj.versionList
                detailVue.trusteeshipContent = jsonObj.trusteeshipContent
            }
        });
    }

    //详情更新操作--------------获取"更新机构详情"参数
    function getUpdateForm() {
        return {
            //附件材料
            busiType: '03030203',
            sourceId: detailVue.tableId,
//            generalAttachmentList: this.$refs.listenUploadData.uploadData,
            interfaceVersion: detailVue.interfaceVersion,
            theState: detailVue.empj_ProjProgInspection_AFModel.theState,
            tableId: detailVue.tableId,
            busiState: detailVue.empj_ProjProgInspection_AFModel.busiState,
            eCode: detailVue.empj_ProjProgInspection_AFModel.eCode,
            //buildProgress: detailVue.empj_ProjProgInspection_AFModel.buildProgress.replace(/[^0-9]/ig,""),
            buildProgress: detailVue.empj_ProjProgInspection_AFModel.buildProgress,
            buttonType: detailVue.buttonType,
            empj_ProjProgInspection_DTL : detailVue.buildingList,
            
        }
    }

    function update(buttonType) {
        detailVue.buttonType = buttonType;
    /*    if(buttonType == '2')
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
         						$(".xszModel").modal({
         							backdrop :'static',
         							keyboard: false
         						});
         					
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
//         								console.log("打开文件结束："+Date.parse(new Date()) + "；；；打开文件返回值TZOpenPdfByPath="+isOpen);
//         								console.log("签章开始："+Date.parse(new Date()));
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
//	         											enterNewTabCloseCurrent(jsonObj.tableId,"受限额度变更详情","empj/Empj_ProjProgInspection_AFDetail.shtml");
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
													            	enterNewTabCloseCurrent(jsonObj3.tableId,"受限额度变更详情","empj/Empj_ProjProgInspection_AFDetail.shtml");
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
         						enterNewTabCloseCurrent(jsonObj.tableId,"受限额度变更详情","empj/Empj_ProjProgInspection_AFDetail.shtml");
         					}
         				}else{
         					$(".xszModel").modal('hide');
         					generalSuccessModal();
         					enterNewTabCloseCurrent(jsonObj.tableId,"受限额度变更详情","empj/Empj_ProjProgInspection_AFDetail.shtml");
         				}
                 	}
                 }
             });
        }else{*/
        	 new ServerInterfaceJsonBody(baseInfo.updateInterface).execute(detailVue.getUpdateForm(), function (jsonObj) {
             	detailVue.showSubFlag = true;
                 if (jsonObj.result != "success") {
                     generalErrorModal(jsonObj);
                 }
                 else {
//     					generalSuccessModal();
     					enterNewTabCloseCurrent(jsonObj.tableId,"项目进度巡查详情","empj/Empj_ProjProgInspectionDetail.shtml");
                 }
             });
//        }
        
       
    }

    function getBuildingList() {
        serverRequest(baseInfo.getBuildingList, {
            interfaceVersion: 19000101,
            tableId: detailVue.userStartId
        }, function (jsonObj) {
            detailVue.buildingList = jsonObj.buildingList
        })
    }

    function changeBuildingListener(data) {
        console.log(data)
        detailVue.buildingId = data.tableId
        detailVue.constructionCode = data.theName
        detailVue.theName = data.eCodeFromConstruction
        // detailVue.nowBuildingModel=data
        detailVue.empj_ProjProgInspection_AFModel.developCompanyName = data.developCompanyName
        detailVue.empj_ProjProgInspection_AFModel.projectName = data.projectName
        detailVue.empj_ProjProgInspection_AFModel.projectId = data.projectId
        detailVue.empj_ProjProgInspection_AFModel.upfloorNumber = data.upfloorNumber
        detailVue.empj_ProjProgInspection_AFModel.eCodeFromConstruction = data.eCodeFromConstruction
        detailVue.empj_ProjProgInspection_AFModel.eCodeFromPresellSystem = data.eCodeFromPresellSystem
        detailVue.empj_ProjProgInspection_AFModel.buildingArea = data.buildingArea
        detailVue.empj_ProjProgInspection_AFModel.eCodeFromPublicSecurity = data.eCodeFromPublicSecurity
        detailVue.empj_ProjProgInspection_AFModel.escrowArea = data.escrowArea
        detailVue.empj_ProjProgInspection_AFModel.escrowStandard = data.escrowStandard
        detailVue.empj_ProjProgInspection_AFModel.deliveryType = data.deliveryType
        detailVue.empj_ProjProgInspection_AFModel.theName = data.eCodeFromConstruction
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
                    orgLimitedAmount: detailVue.empj_ProjProgInspection_AFModel.orgLimitedAmount,
                    expectLimitedRatio: item.limitedAmount,
                    cashLimitedAmount: detailVue.empj_ProjProgInspection_AFModel.cashLimitedAmount
                }
                serverBodyRequest(baseInfo.calculateExpectLimit, form, function (jsonObj) {
                    detailVue.empj_ProjProgInspection_AFModel.expectLimitedAmount = jsonObj.expectLimitedAmount
                    detailVue.empj_ProjProgInspection_AFModel.expectEffectLimitedAmount = jsonObj.expectEffectLimitedAmount
                    changeVersionThousands()
                })
                // detailVue.empj_ProjProgInspection_AFModel.expectLimitedAmount=item.limitedAmount*detailVue.empj_ProjProgInspection_AFModel.orgLimitedAmount/100
                detailVue.empj_ProjProgInspection_AFModel.expectLimitedRatio = item.limitedAmount

                // if(detailVue.empj_ProjProgInspection_AFModel.expectLimitedRatio<detailVue.empj_ProjProgInspection_AFModel.cashLimitedAmount){
                //    detailVue.empj_ProjProgInspection_AFModel.expectEffectLimitedAmount=detailVue.empj_ProjProgInspection_AFModel.expectLimitedRatio
                // }else{
                //    detailVue.empj_ProjProgInspection_AFModel.expectEffectLimitedAmount=detailVue.empj_ProjProgInspection_AFModel.cashLimitedAmount
                // }

                break
            }
        }
    }

    function noSelectVersion() {
        detailVue.expectFigureProgressId = ""
        detailVue.empj_ProjProgInspection_AFModel.expectLimitedAmount = ""
        detailVue.empj_ProjProgInspection_AFModel.expectEffectLimitedAmount = ""
        detailVue.empj_ProjProgInspection_AFModel.expectLimitedRatio = ""

    }

    function getProcessVersionList() {
        var form = {
            interfaceVersion: 19000101,
            tableId: detailVue.buildingId,
            nowLimitRatio: detailVue.empj_ProjProgInspection_AFModel.currentLimitedRatio,
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
        detailVue.empj_ProjProgInspection_AFModel.orgLimitedAmount=addThousands(detailVue.empj_ProjProgInspection_AFModel.orgLimitedAmount)
        detailVue.empj_ProjProgInspection_AFModel.nodeLimitedAmount=addThousands(detailVue.empj_ProjProgInspection_AFModel.nodeLimitedAmount)
        detailVue.empj_ProjProgInspection_AFModel.expectLimitedAmount=addThousands(detailVue.empj_ProjProgInspection_AFModel.expectLimitedAmount)
        detailVue.empj_ProjProgInspection_AFModel.totalGuaranteeAmount=addThousands(detailVue.empj_ProjProgInspection_AFModel.totalGuaranteeAmount)
        detailVue.empj_ProjProgInspection_AFModel.cashLimitedAmount=addThousands(detailVue.empj_ProjProgInspection_AFModel.cashLimitedAmount)
    }

    function changeVersionThousands() {
        detailVue.empj_ProjProgInspection_AFModel.effectiveLimitedAmount=addThousands(detailVue.empj_ProjProgInspection_AFModel.effectiveLimitedAmount)
        detailVue.empj_ProjProgInspection_AFModel.expectEffectLimitedAmount=addThousands(detailVue.empj_ProjProgInspection_AFModel.expectEffectLimitedAmount)
    }

    function initData() {
        getIdFormTab("", function (id) {
            detailVue.tableId = id
            console.log('id is ' + detailVue.tableId)
            refresh()

        })
        laydate.render({
            elem: '#date03030203',
            range: false
	    });
        
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
    function editFileActive(){
		detailVue.editFile = false;
		detailVue.saveFile = true;
		detailVue.showDelete = true;
		
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
  
    
    //------------------------方法定义-结束------------------//

    //------------------------数据初始化-开始----------------//
    // detailVue.refresh();
    detailVue.initData();
    //------------------------数据初始化-结束----------------//
})({
    "detailDivId": "#empj_ProjProgInspectionEditDiv",
    "detailInterface": "../Empj_ProjProgInspection_AFDetail",
    "updateInterface": "../Empj_ProjProgInspectionSave",
//    "getBuildingList": "../Sm_UserGetBuildingList",
    "calculateExpectLimit": "../Empj_ProjProgInspectionCalculate",
    "signatureInterface":"../Sm_SignatureUploadForPath",//签章
});
