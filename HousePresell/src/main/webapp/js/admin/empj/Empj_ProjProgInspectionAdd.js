(function(baseInfo){
    var addVue = new Vue({
        el : baseInfo.addDivId,
        data : {
            interfaceVersion :19000101,
            empj_ProjProgInspectionModel: {},
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
            busiType:'03030203',
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
            
            empj_BulidingList : [],//楼幢信息
            
            orderBy: "",
            
            contactOne : "",//联系人A
            contactTwo : "",//联系人B
            telephoneOne : "",//A联系方式
            telephoneTwo : "",//B联系方式
            countUnit : "",//总包单位
            dtlList : [],//变更楼幢信息
            selectItem : [],//选中项
            buildAddFile : {},//将附件与楼幢相关联
            
            versionList : [],//楼幢进度节点列表
            buildingIdForSave : '',
            //项目进度巡查
            code:'',
            forcastTime:'',
            forcastPeople:'',
            companyName:'',
            projectName:'',
            buildCount:'',
            
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
            
            sortChange:sortChange,//列表排序
            listItemSelectHandle: listItemSelectHandle,
            indexMethod: indexMethod,
            openFileModel :openFileModel,//打开附件信息
            dialogSave : dialogSave,//保存附件信息
            
            showLimit : showLimit,//点击施工编号
            dialogSaveLimit : dialogSaveLimit,//保存楼幢进度节点
            
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
            interfaceVersion:addVue.interfaceVersion,
        }
    }

    // 详情操作--------------
    function refresh()
    {

    }

    // 详情更新操作--------------获取"更新机构详情"参数
    function getAddForm()
    {
    	addVue.dtlList = [];
    	if(addVue.selectItem.length>0){
    		for(var i = 0;i<addVue.selectItem.length;i++){
    			var model = {};
    			model.buildingId = addVue.selectItem[i].tableId;
    			model.limitedId = addVue.selectItem[i].limitedId;
    			model.attachementList = addVue.selectItem[i].attachementList;
    			addVue.dtlList.push(model);
    		}
    	}
        return {
            interfaceVersion:addVue.interfaceVersion,
            developCompanyId:addVue.companyId ,
            projectId:addVue.projectId,
            contactOne : addVue.contactOne,
            contactTwo : addVue.contactTwo,
            telephoneOne : addVue.telephoneOne,
            telephoneTwo : addVue.telephoneTwo,
            dtlList : addVue.dtlList,
            buttonType:addVue.buttonType,
            //总包单位
            countUnit : addVue.countUnit,
            // 附件材料
            busiType : '03030203',
            }
    }

    function add(buttonType)
    {
    	
        addVue.buttonType=buttonType;
        if(buttonType == '2')
        {
        	addVue.showSubFlag = false;
        	$(".xszModel").modal({
				backdrop :'static',
				keyboard: false
			});
        	 new ServerInterfaceJsonBody(baseInfo.addInterface).execute(addVue.getAddForm(), function (jsonObj) {
     			addVue.showSubFlag = true;
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
     	  										var model1 =
     	  											{
     	  												interfaceVersion : addVue.interfaceVersion,
     	  												signaturePath : "C:\\uploaded\\"+filename+".pdf",
     	  												signaturePrefixPath :signaturePath,
     	  												fileName : filename,
     	  												urlPath : signaturePath,// 签章网络路径
     	  											}
     	  										new ServerInterface(baseInfo.signatureInterface).execute(model1, function(jsonObj2){
		 	  											$(".xszModel").modal('hide');
		 	  											console.log("保存到OSSServer结束："+Date.parse(new Date()));
//		 	  											enterNewTabCloseCurrent(jsonObj.tableId,"受限额度变更详情","empj/Empj_ProjProgInspectionDetail.shtml");
		 	  											
		 	  											if(jsonObj.result != "success")
											            {
											                generalErrorModal(jsonObj);
											            }
														else
														{
															var model = addVue.getAddForm();
															model.isSign = "1";
															new ServerInterfaceJsonBody(baseInfo.addInterface).execute(model, function(jsonObj3) {
																if(jsonObj3.result != "success")
													            {
													                generalErrorModal(jsonObj3);
													            }
													            else
													            {
													            	generalSuccessModal();
													            	enterNewTabCloseCurrent(jsonObj3.tableId,"受限额度变更详情","empj/Empj_ProjProgInspectionDetail.shtml");
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
     	  						enterNewTabCloseCurrent(jsonObj.tableId,"进度节点变更详情","empj/Empj_ProjProgInspectionDetail.shtml");
     	  					}
     	  				}else{
     	  					$(".xszModel").modal('hide');
     	  					generalSuccessModal();
     	  					enterNewTabCloseCurrent(jsonObj.tableId,"进度节点变更详情","empj/Empj_ProjProgInspectionDetail.shtml");
     	  				}
     	              }else{
     	            	 $(".xszModel").modal('hide');
     	              	generalSuccessModal();
     	                  enterNewTabCloseCurrent(jsonObj.tableId,"进度节点变更详情","empj/Empj_ProjProgInspectionDetail.shtml");
     	              }
     	        }
     	    });
        }else{
        	 new ServerInterfaceJsonBody(baseInfo.addInterface).execute(addVue.getAddForm(), function (jsonObj) {
     			addVue.showSubFlag = true;
     	        if (jsonObj.result != "success") {
     	        	generalErrorModal(jsonObj)
     	        }
     	        else {
     	        	generalSuccessModal();
	                enterNewTabCloseCurrent(jsonObj.tableId,"进度节点变更详情","empj/Empj_ProjProgInspectionDetail.shtml");
     	        }
     	    });
        }
        
        
       
    /*    serverBodyRequest(baseInfo.addInterface,addVue.getAddForm(),function (jsonObj) {
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
												interfaceVersion : addVue.interfaceVersion,
												signaturePath : "C:\\uploaded\\"+filename+".pdf",
												signaturePrefixPath :signaturePath,
												fileName : filename,
												urlPath : signaturePath,// 签章网络路径
											}
										new ServerInterface(baseInfo.signatureInterface).execute(model, function(jsonObj2){
											$(".xszModel").modal('hide');
											generalSuccessModal();
											console.log("保存到OSSServer结束："+Date.parse(new Date()));
											enterNewTabCloseCurrent(jsonObj.tableId,"受限额度变更详情","empj/Empj_ProjProgInspectionDetail.shtml");

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
						enterNewTabCloseCurrent(jsonObj.tableId,"受限额度变更详情","empj/Empj_ProjProgInspectionDetail.shtml");
					}
				}else{
					generalSuccessModal();
					enterNewTabCloseCurrent(jsonObj.tableId,"受限额度变更详情","empj/Empj_ProjProgInspectionDetail.shtml");
				}
            }else{
            	generalSuccessModal();
                enterNewTabCloseCurrent(jsonObj.tableId,"受限额度变更详情","empj/Empj_ProjProgInspectionDetail.shtml");
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
            developCompanyId:addVue.companyId,
        },function (jsonObj) {
            addVue.projectList=jsonObj.empj_ProjectInfoList
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
            projectId:addVue.projectId,
        },function (jsonObj) {
            addVue.buildingList=jsonObj.empj_BuildingInfoList;
            for(var i=0;i<addVue.buildingList.length;i++){
                addVue.buildingList[i].theName=addVue.buildingList[i].eCodeFromConstruction
            }
        })
    }

    function changeProjectListener(data) {
        if(data.tableId!=addVue.projectId){
            noSelectVersion();
            addVue.projectId=data.tableId
            addVue.projectName=data.theName
            addVue.nowBuildingModel={}
            getBuildingList()
            addVue.selectBuildingAccount={}
        }
    }

    function changeBuildingListener(data) {
        noSelectVersion()
        if(data.tableId!=addVue.buildingId){
            addVue.buildingId = data.tableId
            addVue.constructionCode = data.theName
            // addVue.nowBuildingModel.recordAvgPriceOfBuilding =""
            addVue.nowBuildingModel=data
            // getIsUniqueBuilding()
            // changeThousands()
            addVue.nowBuildingModel.recordAvgPriceOfBuilding = thousandsToTwoDecimal(addVue.nowBuildingModel.recordAvgPriceOfBuilding);

            getBuildingDetail()
        }

    }

    function getBuildingDetail() {
        var form={
            interfaceVersion:19000101,
            tableId:addVue.buildingId,
            theState:0
        }
        serverRequest(baseInfo.buildingDetailInterface,form,function (jsonObj) {
            addVue.selectBuildingAccount=jsonObj.buildingInfo.buildingAccount
            addVue.escrowStandardTypeName=jsonObj.trusteeshipContent
            if(addVue.selectBuildingAccount.orgLimitedAmount==0){
                generalErrorModal(undefined,"初始受限额度为空，请先增加备案均价")
                return
            }
            if(addVue.selectBuildingAccount.currentLimitedRatio==0){
                generalErrorModal(undefined,"该楼幢已经完成交付，无法添加受限额度变更")
                return
            }
            changeThousands()
            getProcessVersionList()

        })
    }

    function changeVersionListener(data) {
    	/*console.log(data);*/
    	
    	var limitList = data.versionList;
    	for(var i = 0; i < addVue.buildingList.length; i++){
    		if(data.tableId == addVue.buildingList[i].tableId){
    			limitList.forEach(function(item){
    				if(item.tableId == data.limitedId){
    					/*addVue.buildingList[i].limitedId = item.tableId;*/
    					addVue.buildingList[i].limitedAmount = item.limitedAmount;
    				}
    			});
    			
    			break;
    		}
    	}
    	
    	
    }
    /**
     * 点击附件信息打开模态框
     * @author  yaojianping
     * @returns
     */
    function openFileModel(bulidInfo){
    	addVue.buildAddFile = bulidInfo;
    	generalLoadFileBld(addVue);
    	$("#fileNodeModel").modal({
    		backdrop :'static',
    		keyboard: false
    	});
    }
    
    /**
     * 初始化附件信息并加载当前界面的附件信息
     * @param vue Vue类型，当前页面的Vue
     * @param busiType String类型，业务编号
     */
    function generalLoadFileBld(addVue) {
    	console.log(addVue)
        var form = {
            pageNumber:"0",
            busiType : addVue.busiType,
            interfaceVersion:addVue.interfaceVersion,
            reqSource : addVue.flag == true ? "详情" : "",//判断是否是详情页是则显示旧的附件材料
            sourceId:addVue.tableId,
            approvalCode:addVue.approvalCode,
            afId:addVue.afId,
        };
        addVue.loadUploadList=[]
        serverRequest("../Sm_AttachmentCfgList",form,function (jsonObj) {
        	addVue.loadUploadList = jsonObj.sm_AttachmentCfgList;
        	var attachementListFile = addVue.buildAddFile
        	if(attachementListFile.attachementList !=undefined && attachementListFile.attachementList.length>0){
        		for(var j = 0;j<addVue.loadUploadList.length;j++){
        			for(var i = 0;i<attachementListFile.attachementList.length;i++){
        				var model = {
    						fileType:attachementListFile.attachementList[i].fileType,
    						name:attachementListFile.attachementList[i].remark,
    						url:attachementListFile.attachementList[i].theLink,
    						sourceType:attachementListFile.attachementList[i].sourceType,
    						tableId:attachementListFile.attachementList[i].tableId,
    						busiType : attachementListFile.attachementList[i].busiType,
        				}
        				if(addVue.loadUploadList[j].eCode == attachementListFile.attachementList[i].sourceType){
        					addVue.loadUploadList[j].smAttachmentList.push(model);
        				}
        			}
        		}
        	}
        	
        },function (jsonObj) {
            generalErrorModal(jsonObj)
        })
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
    	addVue.buildAddFile.attachementList = attachementList;
    	$("#fileNodeModel").modal('hide');
    }
    
    function noSelectVersion() {
        addVue.expectFigureProgressId=""
        addVue.selectBuildingAccount.expectLimitedAmount = ""
        addVue.selectBuildingAccount.expectEffectLimitedAmount = ""
        addVue.expectLimitedAmount = "";
        addVue.expectEffectLimitedAmount = "";
        addVue.selectBuildingAccount.expectLimitedRatio = "";
    }

    function noSelectCompany() {
        noSelectProject()
        addVue.companyId = ""
        addVue.projectList=[]
        addVue.projectId=""
        addVue.buildingList=[]
        addVue.buildingId=""
        addVue.limitVerList=[]
        addVue.expectFigureProgressId=""
    }

    function noSelectProject() {
        addVue.projectId = ""
        addVue.projectName=""
        addVue.buildingList=[]
        addVue.nowBuildingModel={}
        addVue.selectBuildingAccount={}
        noSelectBuilding()
    }

    function noSelectBuilding() {
        noSelectVersion()
        addVue.buildingId = ""
        addVue.constructionCode = ""
        addVue.nowBuildingModel={}
        addVue.selectBuildingAccount=""
        addVue.escrowStandardTypeName=""
        addVue.limitVerList=[]
    }

   

    function getUserDetail() {
        getLoginUserInfo(function (user) {
            addVue.user=user;
            if(addVue.user.theType=="1"){// 如果是正泰用户
                addVue.isNormalUser=1
                getCompanyList()
            }else{// 如果是一般用户
                addVue.companyId=user.developCompanyId
                addVue.companyName = user.theNameOfCompany
                addVue.isNormalUser=0
                getProjectList()
            }
            // getProjectList()
        })
    }

    function getProcessVersionList() {
        var form={
            interfaceVersion:19000101,
            tableId:addVue.buildingId,
            nowLimitRatio:addVue.selectBuildingAccount.currentLimitedRatio
        }
        serverRequest("../Empj_BldAccountGetLimitAmountVer",form,function (jsonObj) {
            addVue.limitVerList=jsonObj.versionList
            for(var i=0;i<addVue.limitVerList.length;i++){
                var item =addVue.limitVerList[i]
                // item.theName=item.stageName+"-"+item.limitedAmount+"%"
                item.theName=item.stageName
            }
        })
    }

    function companyChangeHandle(data) {
        if(data.tableId!=addVue.companyId){
            addVue.companyId = data.tableId
            addVue.projectList=[]
            addVue.projectId=""
            addVue.buildingList=[]
            addVue.buildingId=""
            addVue.limitVerList=[]
            addVue.expectFigureProgressId=""
            getProjectList()
        }
    }

    function getCompanyList() {
        serverRequest(baseInfo.companyListInterface,getTotalListForm(),function (jsonObj) {
            addVue.companyList=jsonObj.emmp_CompanyInfoList;
        })
    }

    function changeThousands() {
        addVue.selectBuildingAccount.orgLimitedAmount = thousandsToTwoDecimal(addVue.selectBuildingAccount.orgLimitedAmount);
        addVue.selectBuildingAccount.nodeLimitedAmount = thousandsToTwoDecimal(addVue.selectBuildingAccount.nodeLimitedAmount);
        addVue.selectBuildingAccount.effectiveLimitedAmount = thousandsToTwoDecimal(addVue.selectBuildingAccount.effectiveLimitedAmount);
        addVue.selectBuildingAccount.totalGuaranteeAmount = thousandsToTwoDecimal(addVue.selectBuildingAccount.totalGuaranteeAmount);
        addVue.selectBuildingAccount.cashLimitedAmount = thousandsToTwoDecimal(addVue.selectBuildingAccount.cashLimitedAmount);
    }

    function changeVersionThousands() {
        addVue.expectLimitedAmount = thousandsToTwoDecimal(addVue.expectLimitedAmount);
        addVue.expectEffectLimitedAmount = thousandsToTwoDecimal(addVue.expectEffectLimitedAmount);
    }
    
    function sortChange(column) {
        addVue.orderBy = generalOrderByColumn(column)
        refresh()
    }
    
    /*列表勾选事件*/
    function listItemSelectHandle(listTotal) {
    	generalListItemSelectWholeItemHandle(addVue, listTotal);
    	addVue.buildingList.forEach(function(item){
    		item.disabled = true;
    		for(var i = 0 ; i < listTotal.length; i++){
    			if(item.tableId == listTotal[i].tableId){
    				item.disabled = false;
    				break;
    			}
    		}
    		
    		if(item.disabled){
    			item.limitedId = "";
    			item.limitedAmount = "";
    		}
    		
    	});
    	
    	
             
    }
    
    /**
     * 点击施工编号
     * @returns
     */
    function showLimit(val){
    	addVue.buildingIdForSave = val.tableId
    	var obj = {
			interfaceVersion:addVue.interfaceVersion,
			buildingId:val.tableId ,
    	}
    	new ServerInterfaceJsonBody(baseInfo.loadVersionInterface).execute(obj, function (jsonObj) {
 	        if (jsonObj.result != "success") {
 	        	generalErrorModal(jsonObj)
 	        }
 	        else {
 	        	$("#showLimitModel").modal({
	 	       		backdrop :'static',
	 	       		keyboard: false
	 	       	});
 	        	addVue.versionList = jsonObj.versionList;
 	        	
 	        	addVue.versionList.forEach(function(item,index){
 	        		
 	        		if(item.completeDate != ""){
 	        			item.completeDate = new Date(item.completeDate);
 	 	        		
 	 	        		console.log(item.completeDate);
 	        		}
 	        		
 	        	});
 	        	
 	        }
 	    });
    }
    /**
     * 保存楼幢进度节点
     * @returns
     */
    function dialogSaveLimit(){
    	var objAdd = {
			interfaceVersion:addVue.interfaceVersion,
			versionList:addVue.versionList,
			buildingId : addVue.buildingIdForSave
    	}
    	new ServerInterfaceJsonBody('../saveBuildindVersionList').execute(objAdd, function (jsonObj) {
 	        if (jsonObj.result != "success") {
 	        	generalErrorModal(jsonObj)
 	        }
 	        else {
 	        	generalSuccessModal();
 	        	$("#showLimitModel").modal('hide');
 	        }
 	    });
    }
    
    function indexMethod(index) {
        return generalIndexMethod(index, addVue)
    }
    
    function initData()
    {
    	generalLoadFileBld(addVue)
        getUserDetail()
      
      
    }
    // ------------------------方法定义-结束------------------//

    // ------------------------数据初始化-开始----------------//
    // addVue.refresh();
    addVue.initData();
    // ------------------------数据初始化-结束----------------//
})({
    "addDivId":"#Empj_ProjProgInspectionAddDiv",
    "detailInterface":"../Empj_ProjProgInspectionDetail",
    "addInterface":"../Empj_ProjProgInspectionSave",
    "getBuildingList":"../Sm_UserGetBuildingList",
    "projectListInterface":"../Empj_ProjectInfoForSelect",
    "buildingListInterface":"../Common_BldForBuildingList",
    "buildingDetailInterface":"../Empj_BuildingInfoDetailWithService",
    "calculateExpectLimit":"../Empj_ProjProgInspectionCalculate",
    "companyListInterface":"../Emmp_CompanyInfoForSelect",
    "signatureInterface":"../Sm_SignatureUploadForPath",// 签章
    "loadVersionInterface" : "../loadingVersionListByBuildingId",
});
