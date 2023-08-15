(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
        data : {
            interfaceVersion :19000101,
            pageNumber : 1,
            countPerPage : 20,
            tableId : 0,
            eCode: "",
            eCodeFromDRAD: "",
            projectId: 0,
            projectName: "",
            remark: "",
            developCompanyName: "",
            cityRegionName: "",
            address: "",
            allBldEscrowSpace: 0.0,
            currentBldEscrowSpace: 0.0,
            userUpdateName: "",
            lastUpdateTimeStamp: "",
            userRecordName: "",
            recordTimeStamp: "",
            busiState: "",
            approvalState: "",
            empj_BldEscrowCompleted_DtlList: [],
            selectItem : [],

            hasFormula : "",
            webSite : "",
            hasPush : "",


            //附件材料
            busiType : "03030102",
            loadUploadList: [],
            showDelete : false,  //附件是否可编辑

			showPrintBtn : false,
			flag: true,
        },
        methods : {
            initData: initData,
            pushBtn : pushBtn,
            lookPdf : lookPdf,
            //详情
            refresh : refresh,
            getDetailForm : getDetailForm,
            handleSelectionChange: handleSelectionChange,
			getExportForm :getExportForm,//导出参数
			getExportForm2 :getExportForm2,//导出参数
			exportPdf:exportPdf,//导出pdf
			exportPdf2:exportPdf2,//签章
            bldEscrowEditHandle: bldEscrowEditHandle,
            indexMethod: function (index) {
                if (detailVue.pageNumber > 1) {
                    return (detailVue.pageNumber - 1) * detailVue.countPerPage - 0 + (index - 0 + 1);
                }
                if (detailVue.pageNumber <= 1) {
                    return index - 0 + 1;
                }
            },
            commitPrint : commitPrint,
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

	function lookPdf(){

	    	var model={
	  			interfaceVersion : detailVue.interfaceVersion,
	  			sourceId : detailVue.tableId,
	  			reqAddress : window.location.href,
	  			sourceBusiCode : "240180",
	    	}

	  		new ServerInterface(baseInfo.exportInterface).execute(model, function(jsonObj){
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

	function pushBtn(){
		console.log('========推送========');

		new ServerInterface(baseInfo.pushInterface).execute(detailVue.getDetailForm(), function(jsonObj)
		        {
		            if(jsonObj.result != "success")
		            {
		                noty({"text":jsonObj.info,"type":"error","timeout":2000});
		            }
		            else
		            {
		            	generalSuccessModal();
		            	refresh();
		            }
		        });
	}

	function commitPrint()
	{
		var model =
		{
			interfaceVersion : detailVue.interfaceVersion,
			tableId : detailVue.tableId,
			busiCode : detailVue.busiType,
		};

		//无签章
		/*model.isSign = "1";
		new ServerInterface(baseInfo.commitPrintInterface).execute(model, function(jsonObj) {
			if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
            	generalSuccessModal();
            }
		});*/

		/*
		 * 点击提交后将提交按钮置为不可编辑
		 */
		detailVue.flag = false;

		if(confirmFile(this.loadUploadList) == true){

			$(".xszModel").modal({
				backdrop :'static',
				keyboard: false
			});

			new ServerInterface(baseInfo.commitPrintInterface).execute(model, function(jsonObj) {
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
							                 TZPdfViewer.HttpPost("http://61.177.71.243:17000/TZPdfServers/FileUpload.jsp?filename="+filename);

							                 setTimeout(function(item){
							                	 console.log("保存文件至服务器结束："+Date.parse(new Date()));
												 console.log("替换后pdf路径："+signaturePath);

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
//													generalSuccessModal();
													console.log("保存到OSSServer结束："+Date.parse(new Date()));
													if(jsonObj.result != "success")
										            {
										                generalErrorModal(jsonObj);
										            }
													else
													{
														model.isSign = "1";
														new ServerInterface(baseInfo.commitPrintInterface).execute(model, function(jsonObj) {
															if(jsonObj.result != "success")
												            {
												                generalErrorModal(jsonObj);
												            }
												            else
												            {
												            	$(".xszModel").modal('hide');
												            	generalSuccessModal();
												            	getDetail();
												            }
														});
													}

												});
										},12000);

								}else{
									generalErrorModal("","托管终止电子签章失败，建议网络环境在10M以上，请重新提交！");
									$(".xszModel").modal('hide');
								}
								},10000);
							}else{
								generalErrorModal("","托管终止文件打开失败，请重新提交！");//签章文件打开失败！
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

    //按钮操作--------跳转到托管终止编辑/修改页面
    function bldEscrowEditHandle()
    {
        enterNewTabCloseCurrent(detailVue.tableId,"编辑托管终止","empj/Empj_BldEscrowCompletedEdit.shtml");
    }

    //获取选中复选框所在行的tableId
    function handleSelectionChange(val)
    {
        //此处处理要考虑已经办理托管终止的楼幢
        detailVue.selectItem = [];
        detailVue.bldEscrowCompletedSpace = 0.0;
        for (var index = 0; index < val.length; index++){
            var element = val[index].tableId;
            detailVue.selectItem.push(element);
            detailVue.bldEscrowCompletedSpace += val[index].escrowArea;
        }
        // console.log(detailVue.selectItem);
    }


    //详情操作--------------获取"机构详情"参数
    function getDetailForm()
    {
        return {
            interfaceVersion:this.interfaceVersion,
            tableId:this.tableId,
            getDetailType:"2",
        }
    }

    //详情操作-------------
    function refresh()
    {
        if (detailVue.tableId == null || detailVue.tableId < 1)
        {
            return;
        }

        getDetail();
    }

    function getDetail()
    {
        // console.log(detailVue.getDetailForm());
        new ServerInterface(baseInfo.detailInterface).execute(detailVue.getDetailForm(), function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
                noty({"text":jsonObj.info,"type":"error","timeout":2000});
            }
            else
            {
            	detailVue.flag = true;
                // console.log(jsonObj);

                detailVue.eCode = jsonObj.empj_BldEscrowCompleted.eCode;
                detailVue.eCodeFromDRAD = jsonObj.empj_BldEscrowCompleted.eCodeFromDRAD;
                detailVue.developCompanyId = jsonObj.empj_BldEscrowCompleted.developCompanyId;
                detailVue.developCompanyName = jsonObj.empj_BldEscrowCompleted.developCompanyName;
                detailVue.projectId = jsonObj.empj_BldEscrowCompleted.projectId;
                detailVue.projectName = jsonObj.empj_BldEscrowCompleted.projectName;
                detailVue.cityRegionName = jsonObj.empj_BldEscrowCompleted.cityRegionName;
                detailVue.address = jsonObj.empj_BldEscrowCompleted.address;
                detailVue.remark = jsonObj.empj_BldEscrowCompleted.remark;

                if('1' == jsonObj.empj_BldEscrowCompleted.hasFormula){
                	detailVue.hasFormula = "是";
                }else{
                	detailVue.hasFormula = "否";
                }

                if('1' == jsonObj.empj_BldEscrowCompleted.hasPush){
                	detailVue.hasPush = "是";
                }else{
                	detailVue.hasPush = "否";
                }

                detailVue.webSite = jsonObj.empj_BldEscrowCompleted.webSite;

                detailVue.userStartId = jsonObj.empj_BldEscrowCompleted.userStartId;
                detailVue.userUpdateName = jsonObj.empj_BldEscrowCompleted.userUpdateName;
                detailVue.lastUpdateTimeStamp = jsonObj.empj_BldEscrowCompleted.lastUpdateTimeStamp;
                detailVue.userRecordId = jsonObj.empj_BldEscrowCompleted.userRecordId;
                detailVue.userRecordName = jsonObj.empj_BldEscrowCompleted.userRecordName;
                detailVue.recordTimeStamp = jsonObj.empj_BldEscrowCompleted.recordTimeStamp;
                detailVue.busiState = jsonObj.empj_BldEscrowCompleted.busiState;
                detailVue.approvalState = jsonObj.empj_BldEscrowCompleted.approvalState;

                detailVue.currentBldEscrowSpace = 0.0;
                detailVue.empj_BldEscrowCompleted_DtlList = jsonObj.empj_BldEscrowCompleted_DtlList;
                var listCount = jsonObj.empj_BldEscrowCompleted_DtlList.length;
                detailVue.countPerPage = 0;
                if (listCount > 0)
                {
                    detailVue.countPerPage = listCount;
                }
    			if(jsonObj.empj_BldEscrowCompleted.approvalState == "已完结"){
    				detailVue.showPrintBtn = true;
    			}

                for (var index = 0; index < detailVue.empj_BldEscrowCompleted_DtlList.length; index++){
                	var val = detailVue.empj_BldEscrowCompleted_DtlList[index];
                    var escrowArea = val.escrowArea;
                    if (escrowArea != null)
                    {
                        detailVue.currentBldEscrowSpace += escrowArea;
                    }
                }

                if(detailVue.currentBldEscrowSpace !=undefined && detailVue.currentBldEscrowSpace !=""){
                	detailVue.currentBldEscrowSpace = (detailVue.currentBldEscrowSpace).toFixed(2)
                }
                getBuildingInfoList();
            }
        });
    }

    //获取楼幢列表信息
    function getBuildingInfoList()
    {
        var model = {
            interfaceVersion:detailVue.interfaceVersion,
            theState:0,
            projectId:detailVue.projectId,
        };
        // console.log(model);
        new ServerInterface(baseInfo.getBuildingListInterface).execute(model, function(jsonObj) {
            if (jsonObj.result != "success")
            {
                noty({"text":jsonObj.info,"type":"error","timeout":2000});
            }
            else
            {
                detailVue.allBldEscrowSpace = 0.0;
                for (var index = 0; index < jsonObj.empj_BuildingInfoList.length; index++)
                {
                    var escrowArea = jsonObj.empj_BuildingInfoList[index].escrowArea;
                    if (escrowArea != null)
                    {
                        detailVue.allBldEscrowSpace += escrowArea;
                    }
                }

                if(detailVue.allBldEscrowSpace !=undefined && detailVue.allBldEscrowSpace !=""){
                	detailVue.allBldEscrowSpace = (detailVue.allBldEscrowSpace).toFixed(2)
                }
            }
        });
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

  	function exportPdf2(){

  		$(".xszModel").modal({
			backdrop :'static',
			keyboard: false
		});

		new ServerInterface(baseInfo.exportInterface2).execute(detailVue.getExportForm2(), function(jsonObj) {
			if (jsonObj.result != "success") {
				$(".xszModel").modal('hide');
				generalErrorModal(jsonObj);
			} else {
				if(jsonObj.signatureMap != undefined&&jsonObj.signatureMap != null)
				{
					//pdf路径
					var signaturePath = jsonObj.signatureMap.signaturePath;
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
								//var pro3= TZPdfViewer.TZSignByPos3(0,20,740,110,830);
								console.log("签章开始："+Date.parse(new Date()));
							if(TZPdfViewer.PageCount>0){

								console.log("签章结束："+Date.parse(new Date())+ "；；；签章文件返回值TZSignByPos3="+pro3);
								 console.log("保存文件至服务器开始："+Date.parse(new Date()));
						        	     TZPdfViewer.HttpInit();
						            	 TZPdfViewer.HttpAddPostString("name","1111");
						            	 TZPdfViewer.HttpAddPostCurrFile("file2","aaaa");
						                 //TZPdfViewer.HttpPost("http://61.177.71.243:17000/TZPdfServers/FileUpload.jsp?filename="+filename);

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
												$(".xszModel").modal('hide');
												console.log("保存到OSSServer结束："+Date.parse(new Date()));
												if(jsonObj.result != "success")
									            {
									                generalErrorModal(jsonObj);
									            }else{
									            	$(".xszModel").modal('hide');
									            	generalSuccessModal();
									            	getDetail();
									            }

											});
									},12000);

							}else{
								generalErrorModal("","托管终止电子签章失败，建议网络环境在10M以上，请重新提交！");
								$(".xszModel").modal('hide');
							}
							},10000);
						}else{
							generalErrorModal("","托管终止文件打开失败，请重新提交！");
							$(".xszModel").modal('hide');
						}

					}else{
						generalErrorModal("","签章文件路径错误，请重新提交！");
						$(".xszModel").modal('hide');
					}
				}else{
					generalErrorModal("","没有找到指定的文件，请重新提交！");
					$(".xszModel").modal('hide');
				}

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

  	function getExportForm2() {
  		var href = window.location.href;
  		return {
  			interfaceVersion : this.interfaceVersion,
  			tableId : this.tableId,
  			reqAddress : href,
  			sourceBusiCode : "240180",
  		}
  	}

    function initData()
    {
        getIdFormTab("", function (id){
            detailVue.tableId = id;
            refresh();
        });
        generalLoadFile2(detailVue, detailVue.busiType);
    }
    //------------------------方法定义-结束------------------//

    //------------------------数据初始化-开始----------------//
    detailVue.initData();
    //------------------------数据初始化-结束----------------//
})({
    "detailDivId":"#empj_BldEscrowCompletedDetailDiv",
    "detailInterface":"../Empj_BldEscrowCompletedDetail",
    "getBuildingListInterface":"../Empj_BuildingInfoList",
    "exportInterface":"../exportPDFByWord",//导出pdf
    "commitPrintInterface":"../Empj_BldEscrowCompletedApprovalProcess",//提交
    "signatureInterface" : "../Sm_SignatureUploadForPath",//签章
    "pushInterface":"../Empj_BldEscrowCompletedPush",
    "exportInterface2":"../Empj_BldEscrowCompletedSign",//签章
});
