(function(baseInfo){
	var indexVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion : 19000101,
			userName : "",
			userId : "",
			companyFlag : true,
		},
		methods : {
			initData: initData,
			getSearchForm: getSearchForm,
			sendMsg: sendMsg,
			outLogin: outLogin,
			signPrint : signPrint,
			signAttach : signAttach,
		}
	});
	var sideBarVue = new Vue({
		el : baseInfo.sideBarDivId,
		data : {
			interfaceVersion : 19000101,
			userName : "",
			userId : "",
		},
		methods : {
			
		}
	});
	var editPwdVue = new Vue({
		el : baseInfo.editPwdDivId,
		data : {
			interfaceVersion : 19000101,
			oldPwd : "",
			newPwd : "",
			torpType : "password",
			torpNewType : "password",
			showPwd : true,
			hidePwd : false,
			showNewPwd : true,
			hideNewPwd : false,
		},
		methods : {
			editPassword : editPassword,
			showPwdEvent : function ()
		    {
				editPwdVue.torpType = "text";
				editPwdVue.showPwd = false;
				editPwdVue.hidePwd = true;
		    },
			hidePwdEvent : function ()
		    {
				editPwdVue.torpType = "password";
				editPwdVue.showPwd = true;
				editPwdVue.hidePwd = false;
		    },
		    showNewPwdEvent : function ()
		    {
		    	editPwdVue.torpNewType = "text";
		    	editPwdVue.showNewPwd = false;
		    	editPwdVue.hideNewPwd = true;
		    },
			hideNewPwdEvent : function ()
		    {
				editPwdVue.torpNewType = "password";
				editPwdVue.showNewPwd = true;
				editPwdVue.hideNewPwd = false;
		    },
		}
	});

	//------------------------审批开始-----------------------//
	var approvalModalVue = new Vue({
	    el : '#examine',
	    data : {
	        interfaceVersion : 19000101,
			//审批确认按钮
	        sm_ApprovalProcess_Handle:{},
            //审批弹窗 - 审批流程
	        sm_ApprovalProcess_WorkflowList:[],
            //审批弹窗 - 审批记录
	        sm_ApprovalProcess_RecordList:[],
	        selectState:'1',
	        //获取登录用户的信息
	        userName : "",
			userId : "",
			//审批结点Id
            workflowId:"",
			//申请单Id
            afId:"",
			//是否需要备案
            isNeedBackup:false,
			//业务编码
            busiCode:"",
            //附件材料
            attachmentList: [],
            //审批附件上传
			approvalUploadCfg: {"data":{}},
			//按钮类型 - 1:审批 2：备案
			buttonType:"",
			//来源页面
            sourcePage:"",
            //上传附件按钮
            uploadDisabled:false,
			extraObj:{},

	    },
	    methods : {
	        approvalHandle:approvalHandle,
            getModalWorkflowList:getModalWorkflowList,
	        indexMethod: function (index) {
	            generalIndexMethod(index, approvalModalVue);
	        },
            approvalUploadSuccess:approvalUploadSuccess,
			//附件批量下载
            approvalBatchDownload:approvalBatchDownload,
            approvalHandlerRemove:approvalHandlerRemove,//删除回调
            approvalModalClose:approvalModalClose,
            approvalModalCancle:approvalModalCancle,
	    },
	    components : {
	    },
	});
	
	var highMeterModalVue = new Vue({
		el : '#modalHighMeter',
	    data : {
	        interfaceVersion : 19000101,
	        loaduploadlist : [],
	        fileArr : [],
	        imagesList : [],
		    uploadData : [],
	    },
	    methods : {
	    	Load : Load,
	    	Unload : Unload,
	    	Scan :Scan,//拍照
			deletePhoto : deletePhoto,//删除照片
			savePhoto : savePhoto,//保存高拍仪照片
			changesubTypeMain : changesubTypeMain,//
			changeFileType : changeFileType,
			clearRight : clearRight,
	    },
	    components : {
	    	
	    },
	})

	function approvalUploadSuccess(res,file,fileList)
	{
		//更新结点操作人信息
		if(res.status="success")
		{
            var model={
                interfaceVersion:approvalModalVue.interfaceVersion,
                tableId : approvalModalVue.workflowId,
            }
            new ServerInterface(baseInfo.updateApprovalWorkflowInterface).execute(model, function(jsonObj)
            {
                if(jsonObj.result != "success")
                {
                    generalErrorModal(jsonObj);
                }
            });
		}
        approvalModalVue.attachmentList = [];
		for(var i=0;i<fileList.length;i++)
		{
            var file =fileList[i];
            var approvalAttachment = {
                sourceType:approvalModalVue.approvalUploadCfg.eCode,
                theLink : file.response.data[0].url,
                theSize : file.raw.size,
                // fileType: file.raw.type,
                fileType: file.response.data[0].objType,
                remark : file.name,
                busiType : "01030101",
            };
            approvalModalVue.attachmentList.push(approvalAttachment);
		}
    }

    //附件删除回调
    function approvalHandlerRemove(file,fileList)
	{
        if(file.uploadUserId == approvalModalVue.userId)
        {
            var model={
                interfaceVersion:approvalModalVue.interfaceVersion,
                tableId : file.tableId
            }
            new ServerInterface(baseInfo.approvalHandlerRemoveInterface).execute(model, function(jsonObj)
            {
                if(jsonObj.result != "success")
                {
                    generalErrorModal(jsonObj);
                }
                else
				{
                    getModalWorkflowList();
				}
            });
        }
        else
		{
            fileList.push(file);
		}
    }

    // 附件批量下载
    function approvalBatchDownload(attachmentList)
	{
        var list = JSON.stringify(attachmentList);
        var model ={
            interfaceVersion:approvalModalVue.interfaceVersion,
            smAttachmentList:list,
        }
        new ServerInterface(baseInfo.approvalBatchDownloadInterface).execute(model, function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                window.location.href="../"+jsonObj.zipFileName;
            }
        });
    }

    //审批操作
	function approvalHandle()
	{
		if(approvalModalVue.buttonType == 2)
		{
            approvalModalVue.sm_ApprovalProcess_Handle.theAction = 0;
		}
	    var model=
	        {
	            interfaceVersion:approvalModalVue.interfaceVersion,
	            approvalProcessId:approvalModalVue.workflowId, //当前审批节点Id
	            theContent:approvalModalVue.sm_ApprovalProcess_Handle.theContent,
	            theAction:approvalModalVue.sm_ApprovalProcess_Handle.theAction,
                //附件材料
                busiType : "01030101",
                generalAttachmentList:approvalModalVue.attachmentList,
				//按钮
				buttonType: approvalModalVue.buttonType,
				extraObj:approvalModalVue.extraObj,
	        }
        new ServerInterfaceJsonBody(baseInfo.handlerInterface).execute(model, function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                $('#examine').modal('hide');
            	var noPresellCert=jsonObj.noPresellCert
                if (noPresellCert != undefined) {//如果预售证不是未定义，说明该楼幢缺少预售证
                    generalErrorModal(undefined, noPresellCert)
                    $('#errorModelConfirmBtn').click(function () {
                        generalHideModal('#fm')
                        approvalModalVue.sm_ApprovalProcess_Handle = {};
                        enterNewTabCloseCurrent(null, "待办流程", "sm/Sm_ApprovalProcess_AgencyList.shtml");
                    })
					// setTimeout(function () {
					// 	generalHideModal('#fm')
                    //     approvalModalVue.sm_ApprovalProcess_Handle = {};
                    //     enterNewTabCloseCurrent(null, "待办流程", "sm/Sm_ApprovalProcess_AgencyList.shtml");
                    // },2000)

				}else{
                    approvalSuccessModal(jsonObj);
                    approvalModalVue.sm_ApprovalProcess_Handle = {};
                    enterNewTabCloseCurrent(null, "待办流程", "sm/Sm_ApprovalProcess_AgencyList.shtml");
				}
            }
        });
	}

    function  getModalWorkflowList() {

		//获取附件配置信息
        var form = {
            interfaceVersion:approvalModalVue.interfaceVersion,
            pageNumber:"0",
            busiType : "01030101",
        };
        new ServerInterface(baseInfo.attachmentCfgListInterface).execute(form, function (jsonObj) {
            if (jsonObj.result != "success")
            {
                generalErrorModal(jsonObj)
            }
            else
			{
				if(jsonObj.sm_AttachmentCfgList == null && jsonObj.sm_AttachmentCfgList.length == 0)
				{
					approvalModalVue.uploadDisabled = true;
				}
				else
				{
					if(jsonObj.sm_AttachmentCfgList.length > 0)
					{
                        approvalModalVue.approvalUploadCfg= jsonObj.sm_AttachmentCfgList[0];
					}
				}
			}
        });

        approvalModalVue.sm_ApprovalProcess_Handle = {};
        //---------------------remove------------------//
        $('.approvalFirst').removeClass("active");
        $('#examineHandle').removeClass("in active");
        $('.approvalSecond').removeClass("active");
        $('#examinePro').removeClass("in active");
        $('.approvalThird').removeClass("active");
        $('#examineRecord').removeClass("in active");
        //---------------------remove------------------//

        if(approvalModalVue.sourcePage == 2 || approvalModalVue.isNeedBackup)
		{
			$('.approvalSecond').addClass("active");
			$('#examinePro').addClass("in active");
		}
		else
		{
            $('.approvalFirst').addClass("active");
            $('#examineHandle').addClass("in active");
		}

        //获取节点信息和审批记录
        var model =
            {
                interfaceVersion:approvalModalVue.interfaceVersion,
                approvalProcess_AFId : approvalModalVue.afId,
            }
        new ServerInterface(baseInfo.approvalModalListInterface).execute(model, function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                approvalModalVue.sm_ApprovalProcess_WorkflowList = jsonObj.sm_ApprovalProcess_WorkflowList; // 节点信息
                approvalModalVue.sm_ApprovalProcess_RecordList = jsonObj.sm_ApprovalProcess_RecordList; //审批记录
            }
        });
    }

    function approvalModalCancle()
	{
        approvalModalVue.sm_ApprovalProcess_Handle = {};
    }

    function approvalModalClose()
	{
    }

	//------------------------审批结束-----------------------//


	//------------------------方法定义-开始------------------//
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
		}
	}
 
	function initData()
	{
		new ServerInterface(baseInfo.detailInterface).execute(indexVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
//				indexVue.userName = jsonObj.userName;
//				getLoginSm_UserInterface可以获取到登录者姓名
			}
		});
		
		sendMsg();
	}
	
	function sendMsg()
	{
		var model = {
			interfaceVersion : 19000101,
		};
		
		new ServerInterfaceSync(baseInfo.getLoginSm_UserInterface).execute(model, function (jsonObj) {
			if (jsonObj.result != "success")
			{
				sideBarVue.userId = "";
				generalErrorModal(jsonObj);
			}
			else
			{
				indexVue.userName = jsonObj.sm_User.theName;
				indexVue.userId = jsonObj.sm_User.tableId;
				sideBarVue.userId = jsonObj.sm_User.tableId;
				approvalModalVue.userName = jsonObj.sm_User.theName;
				approvalModalVue.userId = jsonObj.sm_User.tableId;
				if(jsonObj.sm_User.developCompanyType == "" || jsonObj.sm_User.developCompanyType == "1"){
					indexVue.companyFlag = false;
				}else{
					indexVue.companyFlag = true;
				}
				$("#isEncrypt").html(jsonObj.sm_User.isEncrypt);
				addListen(jsonObj.sm_User);
				
				//=============== 解决动态获取SideBar时，IE模式下左侧导航栏404的问题 Start ============//
				//动态获取完当前登录用户的Id后，进行加载左侧SideBar
				Vue.nextTick(function () {
					loadSideBarFile();
				})	
				//=============== 解决动态获取SideBar时，IE模式下左侧导航栏404的问题 End ============//
			}
		});
	}
	
	function outLogin()
	{
		
		var model = {
				interfaceVersion : this.interfaceVersion,
				tableId : indexVue.userId,
		}
				
		new ServerInterfaceSync(baseInfo.outLoginInterface).execute(model, function (jsonObj) {
			if (jsonObj.result != "success")
			{
			}
			else
			{
				window.location.href = "login.shtml";
			}
		});
	}
	
	function editPassword(divStr)
	{
		var isPass = errorCheckForAll(divStr);

		if (!isPass) return;

		var model = {
			interfaceVersion : 19000101,
			oldPwd : editPwdVue.oldPwd,
			newPwd : editPwdVue.newPwd,
		};

	new ServerInterface(baseInfo.editPwdInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				editPwdVue.oldPwd = null;
				editPwdVue.newPwd = null;
				generalSuccessModal();
				$("#editPwd").modal('hide');
			}
		});
	}
	//---------------------高拍仪方法开始-------------------------//
	var DeviceMain;//主头
	var DeviceAssist;//副头
	var VideoMain;//主头
	var VideoAssist;//副头
	var videoCapMain;
	var videoCapAssist;	

	var PicPath;
	var initFaceDetectSuccess;
	var readIDcard = false;
	var delArr = []; 
	var modalHighMeterDiv;
	function plugin()
	{
	    return document.getElementById('view1');
	}

	function MainView()
	{
	    return document.getElementById('view1');
	}

	/* function AssistView()
	{
	    return document.getElementById('view2');
	}*/
	/*window.onload = function(){
	 $(baseInfo.highMeterDivId).on('hidden.bs.modal', function () {
		Unload();
	 });
	}*/
	function thumb1()
	{
	   return document.getElementById('thumb1');
		
	}
	function addEvent(obj, name, func)
	{
	    if (obj.attachEvent) {
	        obj.attachEvent("on"+name, func);
	    } else {
	        obj.addEventListener(name, func, false); 
	    }
	}
	function CloseVideoMain()
	{
	    if (VideoMain)
	    {
	        plugin().Video_Release(VideoMain);
	        VideoMain = null;

		    MainView().View_SetText("", 0);
	    }			
	}
	function CloseVideoAssist()
	{
	    if (VideoAssist)
	    {
	        plugin().Video_Release(VideoAssist);
	        VideoAssist = null;

	       // AssistView().View_SetText("", 0);
	    }
	}

	function OpenVideoMain()
	{
	    CloseVideoMain();

	    if (!DeviceMain)
	        return;

	    var sSubType = document.getElementById('subType1'); 								
	    var sResolution = document.getElementById('selRes1'); 	

	    var SelectType = 0;
	    var txt;
	    if(sSubType.options.selectedIndex != -1)
	    {
	        txt = sSubType.options[sSubType.options.selectedIndex].text;
	        if(txt == "YUY2")
	        {
		        SelectType = 1;
	        }
	        else if(txt == "MJPG")
	        {
		        SelectType = 2;
	        }
	        else if(txt == "UYVY")
	        {
		        SelectType = 4;
	        }
	    }

	    var nResolution = sResolution.selectedIndex;		
	    VideoMain = plugin().Device_CreateVideo(DeviceMain, nResolution, SelectType);
	    if (VideoMain)
	    {
		    MainView().View_SelectVideo(VideoMain);
		    MainView().View_SetText("打开视频中，请等待...", 0);
				
	    }
	}

	function OpenVideoAssist()
	{
	    CloseVideoAssist();

	    if (!DeviceAssist)
	        return;

	    var sSubType =  document.getElementById('subType2'); 								
	    var sResolution = document.getElementById('selRes2'); 	

	    var SelectType = 0;
	    var txt;
	    if(sSubType.options.selectedIndex != -1)
	    {
	        txt = sSubType.options[sSubType.options.selectedIndex].text;
	        if(txt == "YUY2")
	        {
		        SelectType = 1;
	        }
	        else if(txt == "MJPG")
	        {
		        SelectType = 2;
	        }
	        else if(txt == "UYVY")
	        {
		        SelectType = 4;
	        }
	    }

	    var nResolution = sResolution.selectedIndex;
				
	    VideoAssist = plugin().Device_CreateVideo(DeviceAssist, nResolution, SelectType);
	    if (VideoAssist)
	    {
		    //AssistView().View_SelectVideo(VideoAssist);
		    //AssistView().View_SetText("打开视频中，请等待...", 0);													    							
	    }	
	}

	function changesubTypeMain()
	{
	    if (DeviceMain)
	    {	
	        var sSubType = document.getElementById('subType1');
	        var sResolution = document.getElementById('selRes1'); 	
	        var SelectType = 0;
	        var txt;
	        if(sSubType.options.selectedIndex != -1)
	        {
		        var txt = sSubType.options[sSubType.options.selectedIndex].text;
		        if(txt == "YUY2")
		        {
			        SelectType = 1;
		        }
		        else if(txt == "MJPG")
		        {
			        SelectType = 2;
		        }
		        else if(txt == "UYVY")
		        {
			        SelectType = 4;
		        }
	        }
						
	        var nResolution = plugin().Device_GetResolutionCountEx(DeviceMain, SelectType);
	        sResolution.options.length = 0; 
	        for(var i = 0; i < nResolution; i++)
	        {
		        var width = plugin().Device_GetResolutionWidthEx(DeviceMain, SelectType, i);
		        var heigth = plugin().Device_GetResolutionHeightEx(DeviceMain, SelectType, i);
		        sResolution.add(new Option(width.toString() + "*" + heigth.toString())); 
	        }
	        sResolution.selectedIndex = 0;
	    }			
	}

	function changesubTypeAssist()
	{
	    if (DeviceAssist)
	    {	
	        var sSubType =  document.getElementById('subType2'); 								
	        var sResolution = document.getElementById('selRes2'); 	

	        var SelectType = 0;
	        var txt;
	        if(sSubType.options.selectedIndex != -1)
	        {
		        var txt = sSubType.options[sSubType.options.selectedIndex].text;
		        if(txt == "YUY2")
		        {
			        SelectType = 1;
		        }
		        else if(txt == "MJPG")
		        {
			        SelectType = 2;
		        }
		        else if(txt == "UYVY")
		        {
			        SelectType = 4;
		        }
	        }			

	        var nResolution = plugin().Device_GetResolutionCountEx(DeviceAssist, SelectType);
	        sResolution.options.length = 0; 
	        for(var i = 0; i < nResolution; i++)
	        {
		        var width = plugin().Device_GetResolutionWidthEx(DeviceAssist, SelectType, i);
		        var heigth = plugin().Device_GetResolutionHeightEx(DeviceAssist, SelectType, i);
		        sResolution.add(new Option(width.toString() + "*" + heigth.toString())); 
	        }
	        sResolution.selectedIndex = 0;
	    }			
	}	

	 
	 function Load()
	{
		 var _this = this;
	    //设备接入和丢失
	    //type设备类型， 1 表示视频设备， 2 表示音频设备
	    //idx设备索引
	    //dbt 1 表示设备到达， 2 表示设备丢失
	    addEvent(plugin(), 'DevChange', function (type, idx, dbt)
	    {
	    	 
			if(1 == type)//视频设备
			{
				if(1 == dbt)//设备到达
				{
					var deviceType = plugin().Global_GetEloamType(1, idx);
					if(1 == deviceType)//主摄像头
					{   
						if(null == DeviceMain)
						{
							DeviceMain = plugin().Global_CreateDevice(1, idx);	
							if(DeviceMain)
							{
								document.getElementById('lab1').innerHTML=plugin().Device_GetFriendlyName(DeviceMain)
								var sSubType = document.getElementById('subType1');
								sSubType.options.length = 0;
								var subType = plugin().Device_GetSubtype(DeviceMain);
								if (subType & 1) 
								{
									sSubType.add(new Option("YUY2"));
								}
								if (subType & 2) 
								{
									sSubType.add(new Option("MJPG"));
								}
								if (subType & 4) 
								{
									sSubType.add(new Option("UYVY"));
								}
								
								sSubType.selectedIndex = 0;
								changesubTypeMain();
								
								OpenVideoMain();
							}
						}
					}
					else if(2 == deviceType || 3 == deviceType)//辅摄像头
					{
						if(null == DeviceAssist)
						{
							DeviceAssist = plugin().Global_CreateDevice(1, idx);										
							if(DeviceAssist)
							{
								document.getElementById('lab2').innerHTML = plugin().Device_GetFriendlyName(DeviceAssist)
								var sSubType =  document.getElementById('subType2');
								sSubType.options.length = 0;
								var subType = plugin().Device_GetSubtype(DeviceAssist);
								if (subType & 1) 
								{
									sSubType.add(new Option("YUY2"));
								}
								if (subType & 2) 
								{
									sSubType.add(new Option("MJPG"));
								}
								if (subType & 4) 
								{
									sSubType.add(new Option("UYVY"));
								}
								if ((0 != (subType & 2)) && (0 != (subType & 1)))//辅摄像头优先采用mjpg模式打开 
								{
									sSubType.selectedIndex = 1;
								}
								else 
								{
									sSubType.selectedIndex = 0;
								}
								initFaceDetectSuccess = plugin().InitFaceDetect();
								
								changesubTypeAssist();
								
								OpenVideoAssist();
							}
						}
					}
				}
				else if(2 == dbt)//设备丢失
				{
					if (DeviceMain) 
					{
	                    if (plugin().Device_GetIndex(DeviceMain) == idx) 
						{
	                        CloseVideoMain();
	                        plugin().Device_Release(DeviceMain);
	                        DeviceMain = null;
							
							document.getElementById('lab1').innerHTML = "";
							document.getElementById('subType1').options.length = 0; 
							document.getElementById('selRes1').options.length = 0; 
	                    }
	                }
					
	                if (DeviceAssist) 
					{
	                    if (plugin().Device_GetIndex(DeviceAssist) == idx) 
						{
	                        CloseVideoAssist();
	                        plugin().Device_Release(DeviceAssist);
	                        DeviceAssist = null;
							
	                        document.getElementById('lab2').innerHTML = "";
	                        document.getElementById('subType2').options.length = 0; 
	                        document.getElementById('selRes2').options.length = 0; 
	                    }
	                }
				}
			}
	    });

	    addEvent(plugin(), 'Ocr', function(flag, ret)
	    {
	        if (1 == flag && 0 == ret)
	        {
		        var ret = plugin().Global_GetOcrPlainText(0);
		        alert(ret);
	        }
	    });

	    addEvent(plugin(), 'IdCard', function(ret)
	    {
	        if (1 == ret)
	        {
		        var str = GetTimeString() + "：";
		
		        for(var i = 0; i < 16; i++)
		        {
			        str += plugin().Global_GetIdCardData(i + 1);
			        str += ";";				
		        }

		        document.getElementById("idcard").value=str;	
				
				var image = plugin().Global_GetIdCardImage(1);//1表示头像， 2表示正面， 3表示反面 ...
				plugin().Image_Save(image, "C:\\idcard.jpg", 0);
				plugin().Image_Release(image);
				
				document.getElementById("idcardimg").src= "C:\\idcard.jpg";
	        }
	    });

	    addEvent(plugin(), 'Biokey', function(ret)
	    {
	        if (4 == ret)
	        {
		        // 采集模板成功
		        var mem = plugin().Global_GetBiokeyTemplateData();
		        if (mem)
		        {
			        if (plugin().Memory_Save(mem, "C:\\1.tmp"))
			        {
				        document.getElementById("biokey").value="获取模板成功，存储路径为C:\\1.tmp";
			        }						
			        plugin().Memory_Release(mem);
		        }
		
		        var img = plugin().Global_GetBiokeyImage();
		        plugin().Image_Save(img, "C:\\BiokeyImg1.jpg", 0);
		        plugin().Image_Release(img);
		
		        document.getElementById("BiokeyImg1").src= "C:\\BiokeyImg1.jpg";					
		        alert("获取指纹模板成功");
	        }
	        else if (8 == ret)
	        {
		        var mem = plugin().Global_GetBiokeyFeatureData();
		        if (mem)
		        {
			        if (plugin().Memory_Save(mem, "C:\\2.tmp"))
			        {
				        document.getElementById("biokey").value="获取特征成功，存储路径为C:\\2.tmp";
			        }						
			        plugin().Memory_Release(mem);
		        }
		
		        var img = plugin().Global_GetBiokeyImage();
		        plugin().Image_Save(img, "C:\\BiokeyImg2.jpg", 0);
		        plugin().Image_Release(img);
		
		        document.getElementById("BiokeyImg2").src= "C:\\BiokeyImg2.jpg";
		        alert("获取指纹特征成功");
	        }
	        else if (9 == ret)
	        {
		        document.getElementById("biokey").value += "\r\n刷的不错！";
	        }
	        else if (10 == ret)
	        {
		        document.getElementById("biokey").value+= "\r\n图像质量太差！";
	        }
	        else if (11 == ret)
	        {
		        document.getElementById("biokey").value+= "\r\n图像点数太少！";
	        }
	        else if (12 == ret)
	        {
		        document.getElementById("biokey").value+= "\r\n太快！";
	        }
	        else if (13 == ret)
	        {
		        document.getElementById("biokey").value+= "\r\n太慢！";
	        }
	        else if (14 == ret)
	        {
		        document.getElementById("biokey").value+= "\r\n其它质量问题！";
	        }				
	    });

	    addEvent(plugin(), 'Reader', function(type, subtype)
	    {
	        var str = "";
	        if (4 == type)
	        {
				if( 0 == subtype)//接触式CPU卡
				{
					str += "[接触式CPU卡][银行卡号]:";
					str += plugin().Global_ReaderGetCpuCreditCardNumber();
				}
				else if( 1 == subtype)//非接触式CPU卡
				{
					str += "[非接触式CPU卡] :";
					str += "[Id]:";
					str += plugin().Global_ReaderGetCpuId();
					str += "[银行卡号]:";
					str += plugin().Global_ReaderGetCpuCreditCardNumber();
					
					str += "[磁道数据]:";
					str += plugin().Global_CpuGetBankCardTrack();//磁道数据
					
					str += "[交易记录]:";
					var n = plugin().Global_CpuGetRecordNumber();//交易条数
					for(var i = 0; i < n; i++)
					{
						str += plugin().Global_CpuGetankCardRecord(i);
						str + ";";
					}			
				}							
			}
	        else if (2 == type)
	        {
		        str += "[M1卡] Id:";
		        str += plugin().Global_ReaderGetM1Id();
	        }
	        else if (3 == type)
	        {
		        str += "[Memory卡] Id:";
		        str += plugin().Global_ReaderGetMemoryId();	
	        }
	        else if (5 == type)
	        {
		        str += "[社保卡] :";
		        str += plugin().Global_ReaderGetSocialData(1);
		        str += plugin().Global_ReaderGetSocialData(2);
	        }
	        document.getElementById("reader").value=str;
	    });

	    addEvent(plugin(), 'Mag', function(ret)
	    {
	        var str = "";

	        str += "[磁卡卡号] ";
	        str += plugin().Global_MagneticCardGetNumber();

	        str += "[磁道数据]";		

	        str += "磁道1:";					
	        str += plugin(). Global_MagneticCardGetData(0);	
	        str += "磁道2:";
	        str += plugin(). Global_MagneticCardGetData(1);	
	        str += "磁道3:";
	        str += plugin(). Global_MagneticCardGetData(2);	

	        document.getElementById("mag").value=str;
	    });

	    addEvent(plugin(), 'ShenZhenTong', function(ret)
	    {
	        var str = "";

	        str += "[深圳通卡号] ";
	        str += plugin().Global_GetShenZhenTongNumber();
			
			str += "[金额:] ";
	        str += plugin().Global_GetShenZhenTongAmount();

	        str += "[交易记录:]";

			var n = plugin().Global_GetShenZhenTongCardRecordNumber();
			for(var i = 0; i < n ; i++)
			{			
				str += plugin().Global_GetShenZhenTongCardRecord( i);	
				str += ";";	
			}			
	        document.getElementById("shenzhentong").value=str;
	    });			

	    addEvent(plugin(), 'MoveDetec', function(video, id)
	    {
	        // 自动拍照事件	
	    });

	  

	    var title = document.title;
	    document.title = title + plugin().version;

	    MainView().Global_SetWindowName("view");
	  //  AssistView().Global_SetWindowName("view");
	    thumb1().Global_SetWindowName("thumb");

	    var ret;
		ret = plugin().Global_InitDevs();
		if(ret)
		{
			//进行人脸识别初始化时，视频应处于关闭状态
			 plugin().InitFaceDetect();
		}
		
		if( !plugin().Global_VideoCapInit())
		{
			alert("初始化失败！");
		}
	} 

	function Unload()
	{
	    if (VideoMain)
	    {
	        MainView().View_SetText("", 0);
	        plugin().Video_Release(VideoMain);//关闭模态框将右侧的视频关闭
	        thumb1().Thumbnail_Clear(true);//关闭模态框时将已经拍摄的图片删除
	        VideoMain = null;
	    }
	    if(DeviceMain)
	    {
	        plugin().Device_Release(DeviceMain);
	        DeviceMain = null;		
	    }
	    if (VideoAssist)
	    {
	        //AssistView().View_SetText("", 0);
	        plugin().Video_Release(VideoAssist);
	        VideoAssist = null;
	    }
	    if(DeviceAssist)
	    {
	        plugin().Device_Release(DeviceAssist);
	        DeviceAssist = null;		
	    }			


	    plugin().Global_DeinitDevs();
		
		//进行人脸识别反初始化时，视频应处于关闭状态
		plugin().DeinitFaceDetect();
	}

	function GetTimeString()
	{
		var date = new Date();
		var yy = date.getFullYear().toString();
		var mm = (date.getMonth() + 1).toString();
		var dd = date.getDate().toString();
		var hh = date.getHours().toString();
		var nn = date.getMinutes().toString();
		var ss = date.getSeconds().toString();
		var mi = date.getMilliseconds().toString();
		
		var ret = yy + mm + dd + hh + nn + ss + mi;
		return ret;
	}

	//拍照
	function Scan()
	{
		
	    if (VideoMain) 
	    {
	        var imgList = plugin().Video_CreateImageList(VideoMain, 0, 0);
	        if (imgList) {
	            var len = plugin().ImageList_GetCount(imgList);
	            for (var i = 0; i < len; i++) {
	                var img = plugin().ImageList_GetImage(imgList, i);
	                var Name = "D:\\" + GetTimeString() + ".jpg";
	                var b = plugin().Image_Save(img, Name, 0);
	                if (b) {//图片保存成功是1
	                    MainView().View_PlayCaptureEffect();
	                    thumb1().Thumbnail_Add(Name);
	                    PicPath = Name;
	                }
	               var base64 = plugin().Image_GetBase64(img,2,0);
	               var fileType = document.getElementById("fileType").value;
	               var model = {
	                 picBase64:base64,
	                 attachmentType : fileType,
	               	 fileName:PicPath,
	               	 picType : ".jpg"
	               }
	             var letFileArr = [];
	               letFileArr.push(model);
	               letFileArr = JSON.stringify(letFileArr);
	   		 	var model = {
	   		 		interfaceVersion:19000101,
	   		 		base64List :letFileArr,
	   		 	}
	                var _this = this;
		   		 	$.ajax({
		   				 type: "POST",
		   				 url:"../SaveGPYPic",
		   				 dataType:'json',
		   				 //contentType: "application/json;charset=utf-8",
		   				 data:model,
		   				 success:function(jsonObj){
		   					 if(jsonObj.result !="success"){
		   						 generalErrorModal(jsonObj,jsonObj.info);
		   					 }else{
		   						var fileBytesList = jsonObj.fileBytesList;
		   						highMeterModalVue.fileArr.push(fileBytesList[0]);
		   					 }
		   					 
		   				 },
		   				 error : function (event, XMLHttpRequest, ajaxOptions, thrownError) {
		   				 },
		   			})
	                plugin().Image_Release(img);
	            }
	            plugin().ImageList_Release(imgList);
	        }
	    }
	    if (VideoAssist) 
	    {
	        var imgList2 = plugin().Video_CreateImageList(VideoAssist, 0, 0);
	        if (imgList2) {
	            var len = plugin().ImageList_GetCount(imgList2);
	            for (var i = 0; i < len; i++) {
	                var img = plugin().ImageList_GetImage(imgList2, i);
	                var Name = "C:\\" + GetTimeString() + ".jpg";
	                var b = plugin().Image_Save(img, Name, 0);
	                
	                if (b) {
	                    //AssistView().View_PlayCaptureEffect();
	                    thumb1().Thumbnail_Add(Name);
	                }

	                plugin().Image_Release(img);
	            }

	            plugin().ImageList_Release(imgList2);
	        }
	    }
	}
	function clearRight(){
	   return thumb1().Thumbnail_Clear(true);
	}
	function RGB(r, g, b)
	{
		return r | g<<8 | b<<16;
	}
	//选择附件类型
	function changeFileType(){
		
	}
	//保存图片的方法
	 function savePhoto(){
	 	var _this =  this;
	 	_this.loaduploadlist.forEach(function(item){
				for(var i = 0;i<highMeterModalVue.fileArr.length;i++){
					if(item.eCode == highMeterModalVue.fileArr[i].extra){
						var fileModel={
							fileType:highMeterModalVue.fileArr[i].objType,
							name : highMeterModalVue.fileArr[i].originalName,
							url : highMeterModalVue.fileArr[i].url,
							sourceType : highMeterModalVue.fileArr[i].extra,
							busiType : _this.busitype,
							theSize:highMeterModalVue.fileArr[i].byteLength
						}
						item.smAttachmentList.push(fileModel);
						var uploadModal = {
							sourceType:highMeterModalVue.fileArr[i].extra,
			        		theLink:highMeterModalVue.fileArr[i].url,
			        		fileType:highMeterModalVue.fileArr[i].fileType,
			        		theSize:highMeterModalVue.fileArr[i].byteLength,
			        		remark:highMeterModalVue.fileArr[i].originalName,
			        		busiType:_this.busitype
						};
						var imgListArr = {
								sourceType:highMeterModalVue.fileArr[i].extra,
						        theLink:highMeterModalVue.fileArr[i].url,
						        fileType:highMeterModalVue.fileArr[i].objType
				    	}
						_this.imagesList.push(imgListArr);
						_this.uploadData.push(uploadModal);
					}
				}
			})
	     	console.log(_this.uploadData);
			$("#modalHighMeter").modal('hide');
	 	highMeterModalVue.fileArr = [];
	}
	/* $("#modalHighMeter").on('hidden.bs.modal', function () {
             Unload();
	 });*/
	 //删除图片的方法
	 function deletePhoto(){
	 	var len =  thumb1().Thumbnail_GetCount();
	    for (var i = 0; i < len; i++) {
	    	if(thumb1().Thumbnail_GetCheck(i)){
	    		highMeterModalVue.fileArr.splice(i,1);
	    		thumb1().Thumbnail_Remove(i,true);
	    		deletePhoto();
	    	}
	    }
	 }
	//---------------------高拍仪方法结束-------------------------//
	
	 
	function signPrint ()
	{
		//1.打开文件
//		TZPdfViewer.OnTZPdfViewerLoad();
//		var isOpen = TZPdfViewer.TZOpenPdfByPath("http://cz.zhishusz.com:19000/OssSave/bananaUpload/201812/29/87c105fa45c44638b7a4858ff4e9fdb3.pdf",1);
		var isOpen = TZPdfViewer.TZOpenPdfByPath("http://cz.zhishusz.com:19000/OssSave/bananaUpload/201901/03/41dda45e356b4cc29185a162f76f5cd9.pdf",1);

		alert("1.打开本地文件="+isOpen);
		
		//2.根据关键字签章
		var isSuccess = TZPdfViewer.TZInsertESealByKeyWord("托管机构（盖章）");
		alert("2.根据关键字签章="+isSuccess);
		
		//3.保存到本地路径
		var isSave = TZPdfViewer.SaveAs("D:\\hahadasha.pdf");
		alert("3.保存到本地路径="+isSave);
	}
	 
	
	function signAttach ()
	{
		//1.打开文件
//		TZPdfViewer.OnTZPdfViewerLoad();
//		var isOpen = TZPdfViewer.TZOpenPdfByPath("http://cz.zhishusz.com:19000/OssSave/bananaUpload/201812/29/87c105fa45c44638b7a4858ff4e9fdb3.pdf",1);
		var isOpen = TZPdfViewer.TZOpenPdfByPath("http://cz.zhishusz.com:19000/OssSave/bananaUpload/201901/03/41dda45e356b4cc29185a162f76f5cd9.pdf",1);
		alert("1.打开本地文件="+isOpen);
		
		//2.根据坐标签章
		var isSuccess = TZPdfViewer.TZSignByPos3(0,400,700,0,0);
		alert("2.根据关键字签章="+isSuccess);
		
		//3.保存到本地路径
		var isSave = TZPdfViewer.SaveAs("D:\\是非得失.pdf");
		alert("3.保存到本地路径="+isSave);
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	indexVue.initData();
	window.indexVue = indexVue;
	window.approvalModalVue = approvalModalVue;
	window.highMeterModalVue = highMeterModalVue;
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#indexDiv",
	"sideBarDivId":"#sideBarDiv",
	"editPwdDivId":"#editPwd",
	"detailInterface":"../admin/Sm_HomePageList",
	"getLoginSm_UserInterface":"../Sm_UserGet",
	"outLoginInterface":"../Sm_UserExit",
	"editPwdInterface":"../Sm_UserEditPwd",
	//---------------------审批流程开始-------------------------//
	"handlerInterface":"../Sm_ApprovalProcess_Handler",
    "approvalModalListInterface":"../Sm_ApprovalProcess_ModalList", //获取审批流程，审批记录
	"updateApprovalWorkflowInterface":"../Sm_ApprovalProcess_WorkflowUpdate", //上传附件，更新结点操作人信息
    "attachmentCfgListInterface":"../Sm_AttachmentCfgList", //获取附件配置信息
    "approvalDisagreeInterface":"../Sm_ApprovalProcess_Disagree", //审批：不通过
    "approvalBatchDownloadInterface":"../fileDownLoad", //附件全部下载
    "approvalHandlerRemoveInterface":"../Sm_AttachmentDelete", //删除附件
    //---------------------审批流程结束-------------------------//
});
