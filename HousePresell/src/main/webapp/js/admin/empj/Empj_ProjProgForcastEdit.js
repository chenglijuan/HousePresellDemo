(function(baseInfo) {
	var detailVuePro = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion : 19000101,
			empj_ProjProgForcast_AFModel : {},
			empj_ProjProgForcast_AFModelNew : {},
			tableId : 1,

			userStartId : "10264",
			buildingList : [],
			buildingId : "",
			expectFigureProgressId : "",
			theName : "",
			constructionCode : "",
			buttonType : "",
			
			aeecss_token : "",
			ts_pj_id : "",
			ts_id : "",
			action : "",
			nowDate : "",
			

			// 附件材料
			busiType : '03030202',
			dialogVisible : false,
			dialogImageUrl : "",
			fileType : "",
			fileList : [],
			showButton : true,
			hideShow : false,
			uploadData : [],
			smAttachmentList : [],// 页面显示已上传的文件
			uploadList : [],
			loadUploadList : [],
			showDelete : true,
			limitVerList : [],// 受限额度版本节点信息
			trusteeshipContent : "",
			tempId : "",
			// 其他
			errMsg : "", // 错误提示信息,
			showSubFlag : true,

			editFile : false,
			saveFileEdit : false,
			dtlList : [],//变更楼幢信息
            selectItem : [],//选中项
            buildAddFile : {},//将附件与楼幢相关联
            empj_ProjProgForcast_DTL:[],
            forcastTime:""
		},
		methods : {
			// 详情
			refresh : refresh,
			initData : initData,
			getSearchForm : getSearchForm,
			// 更新
			getUpdateForm : getUpdateForm,
			update : update,
			changeBuildingListener : changeBuildingListener,
			changeVersionListener : changeVersionListener,
			saveAttachment : saveAttachment,
			noSelectVersion : noSelectVersion,
			openFileModel : openFileModel,
			dialogSave : dialogSave,// 保存附件信息
			editFileActive : editFileActive,
			saveFileActive : saveFileActive,
			projectVdeio : projectVdeio,//项目视频
			openVdeioFile : openVdeioFile,//楼幢视频
			
			ssoLogin : ssoLogin,//sso登录

		},
		computed : {},
		components : {
			'vue-select' : VueSelect,
			"my-uploadcomponent" : fileUpload,
		},
		watch : {}
	});

	// ------------------------方法定义-开始------------------//
	function projectVdeio(){
		window.open("http://apits.czzhengtai.com:811/v_upload/?aeecss_token="+detailVuePro.aeecss_token+"&ts_id="+detailVuePro.ts_id+"&ts_pj_id="+detailVuePro.ts_pj_id+"&action="+detailVuePro.action + "&jdtime=" + detailVuePro.nowDate);
	}
	
	function openVdeioFile(rowInfo){
		console.log(rowInfo);
		window.open("http://apits.czzhengtai.com:811/v_upload/?aeecss_token="+detailVuePro.aeecss_token+"&ts_id="+rowInfo.tableId+"&ts_pj_id="+detailVuePro.ts_pj_id+"&ts_bld_id="+rowInfo.buildingId+"&action="+detailVuePro.action + "&jdtime=" + detailVuePro.nowDate);
	}
	// 详情操作--------------获取"机构详情"参数
	function getSearchForm() {
		return {
			interfaceVersion : detailVuePro.interfaceVersion,
			tableId : detailVuePro.tableId,
		}
	}

	// 详情操作--------------
	function refresh() {
		if (detailVuePro.tableId == null || detailVuePro.tableId < 1) {
			return;
		}
		getDetail();
		loadUpload();
		ssoLogin();
	}
	
	function ssoLogin() {
		new ServerInterface(baseInfo.ssoLoginInterface).execute(detailVuePro.getSearchForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
				//generalErrorModal(jsonObj);
			} else {
				detailVuePro.aeecss_token = jsonObj.aeecss_token;
				console.log(detailVuePro.aeecss_token);
			}
		});
	}

	function getDetail() {
		new ServerInterface(baseInfo.detailInterface).execute(detailVuePro.getSearchForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
				noty({
					"text" : jsonObj.info,
					"type" : "error",
					"timeout" : 2000
				});
			} else {
				detailVuePro.empj_ProjProgForcast_AFModel = jsonObj.empj_PaymentBond_AF;
				$('#date03030201101').val(jsonObj.empj_PaymentBond_AF.forcastTime);
				// 子表数据
			
				detailVuePro.buildingList = jsonObj.empj_PaymentBond_DTLList;
				detailVuePro.buildingList.forEach(function(item,index){
					if(item.hasAchieve == '-1'){
						item.hasAchieve = '1';
					}
					item.buildProgress = item.buildProgress.replace(/[^0-9]/ig,"")
				});
				detailVuePro.limitVerList = jsonObj.versionList;
				detailVuePro.trusteeshipContent = jsonObj.trusteeshipContent;
				
				detailVuePro.ts_pj_id = detailVuePro.empj_ProjProgForcast_AFModel.projectId;
				detailVuePro.ts_id = detailVuePro.empj_ProjProgForcast_AFModel.tableId;
				
			}
		});
	}

	
	function getUpdateForm() {
		var forcastT=$('#date03030201101').val()
		return {
			interfaceVersion : detailVuePro.interfaceVersion,
			tableId : detailVuePro.tableId,
			eCode : detailVuePro.empj_ProjProgForcast_AFModel.eCode,
			forcastTime : forcastT,
			forcastPeople : detailVuePro.empj_ProjProgForcast_AFModel.forcastPeople,
			empj_ProjProgForcast_DTL : detailVuePro.buildingList,
			busiType : '03030202',
		}
	}
	// 详情更新操作--------------获取"更新机构详情"参数
	function update(buttonType) {
		detailVuePro.buttonType = buttonType;
		new ServerInterfaceJsonBody(baseInfo.updateInterface).execute(detailVuePro.getUpdateForm(), function(jsonObj) {
			
			detailVuePro.showSubFlag = true;
			if (jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
				
				enterNewTabCloseCurrent(detailVuePro.tableId, "工程进度巡查详情", "empj/Empj_ProjProgForcastDetail.shtml");
			}
		});
	}

	function getBuildingList() {
	}

	function changeBuildingListener(data) {
		detailVuePro.buildingId = data.tableId
		detailVuePro.constructionCode = data.theName
		detailVuePro.theName = data.eCodeFromConstruction
		// detailVuePro.nowBuildingModel=data
		detailVuePro.empj_ProjProgForcast_AFModel.developCompanyName = data.developCompanyName
		detailVuePro.empj_ProjProgForcast_AFModel.projectName = data.projectName
		detailVuePro.empj_ProjProgForcast_AFModel.projectId = data.projectId
		detailVuePro.empj_ProjProgForcast_AFModel.upfloorNumber = data.upfloorNumber
		detailVuePro.empj_ProjProgForcast_AFModel.eCodeFromConstruction = data.eCodeFromConstruction
		detailVuePro.empj_ProjProgForcast_AFModel.eCodeFromPresellSystem = data.eCodeFromPresellSystem
		detailVuePro.empj_ProjProgForcast_AFModel.buildingArea = data.buildingArea
		detailVuePro.empj_ProjProgForcast_AFModel.eCodeFromPublicSecurity = data.eCodeFromPublicSecurity
		detailVuePro.empj_ProjProgForcast_AFModel.escrowArea = data.escrowArea
		detailVuePro.empj_ProjProgForcast_AFModel.escrowStandard = data.escrowStandard
		detailVuePro.empj_ProjProgForcast_AFModel.deliveryType = data.deliveryType
		detailVuePro.empj_ProjProgForcast_AFModel.theName = data.eCodeFromConstruction
		changeThousands()
		// getProcessVersionList()
	}

	function loadUpload() {
		generalLoadFile2(detailVuePro, detailVuePro.busiType)
	}

	function saveAttachment() {
		generalUploadFile(detailVuePro, "Tgxy_BankAccountEscrowed", baseInfo.attachmentBatchAddInterface, baseInfo.successModel)
	}

	function changeVersionListener(data) {
		detailVuePro.expectFigureProgressId = data.tableId
		for (var i = 0; i < detailVuePro.limitVerList.length; i++) {
			var item = detailVuePro.limitVerList[i]
			if (item.tableId == data.tableId) {
				console.log('item is ')
				console.log(item)
				var form = {
					interfaceVersion : 19000101,
					orgLimitedAmount : detailVuePro.empj_ProjProgForcast_AFModel.orgLimitedAmount,
					expectLimitedRatio : item.limitedAmount,
					cashLimitedAmount : detailVuePro.empj_ProjProgForcast_AFModel.cashLimitedAmount
				}
				serverBodyRequest(baseInfo.calculateExpectLimit, form, function(jsonObj) {
					detailVuePro.empj_ProjProgForcast_AFModel.expectLimitedAmount = jsonObj.expectLimitedAmount
					detailVuePro.empj_ProjProgForcast_AFModel.expectEffectLimitedAmount = jsonObj.expectEffectLimitedAmount
					changeVersionThousands()
				})
				detailVuePro.empj_ProjProgForcast_AFModel.expectLimitedRatio = item.limitedAmount

				break
			}
		}
	}

	function noSelectVersion() {
		detailVuePro.expectFigureProgressId = ""
		detailVuePro.empj_ProjProgForcast_AFModel.expectLimitedAmount = ""
		detailVuePro.empj_ProjProgForcast_AFModel.expectEffectLimitedAmount = ""
		detailVuePro.empj_ProjProgForcast_AFModel.expectLimitedRatio = ""

	}

	function getProcessVersionList() {
		var form = {
			interfaceVersion : 19000101,
			tableId : detailVuePro.buildingId,
			nowLimitRatio : detailVuePro.empj_ProjProgForcast_AFModel.currentLimitedRatio,
		}
		// serverRequest("../Empj_BldGetLimitAmountVer", form, function
		// (jsonObj) {
		serverRequest("../Empj_BldAccountGetLimitAmountVer", form, function(jsonObj) {
			detailVuePro.limitVerList = jsonObj.versionList

			for (var i = 0; i < detailVuePro.limitVerList.length; i++) {
				var item = detailVuePro.limitVerList[i]
				if (item.tableId = detailVuePro.expectFigureProgressId) {
					// $('#progressSelect').value(item.theName)
					break
				}
			}
		})
	}

	function changeThousands() {
		detailVuePro.empj_ProjProgForcast_AFModel.orgLimitedAmount = addThousands(detailVuePro.empj_ProjProgForcast_AFModel.orgLimitedAmount)
		detailVuePro.empj_ProjProgForcast_AFModel.nodeLimitedAmount = addThousands(detailVuePro.empj_ProjProgForcast_AFModel.nodeLimitedAmount)
		detailVuePro.empj_ProjProgForcast_AFModel.expectLimitedAmount = addThousands(detailVuePro.empj_ProjProgForcast_AFModel.expectLimitedAmount)
		detailVuePro.empj_ProjProgForcast_AFModel.totalGuaranteeAmount = addThousands(detailVuePro.empj_ProjProgForcast_AFModel.totalGuaranteeAmount)
		detailVuePro.empj_ProjProgForcast_AFModel.cashLimitedAmount = addThousands(detailVuePro.empj_ProjProgForcast_AFModel.cashLimitedAmount)
	}

	function changeVersionThousands() {
		detailVuePro.empj_ProjProgForcast_AFModel.effectiveLimitedAmount = addThousands(detailVuePro.empj_ProjProgForcast_AFModel.effectiveLimitedAmount)
		detailVuePro.empj_ProjProgForcast_AFModel.expectEffectLimitedAmount = addThousands(detailVuePro.empj_ProjProgForcast_AFModel.expectEffectLimitedAmount)
	}

	function initData() {
		generalLoadFileBld(detailVuePro)
		getIdFormTab("", function(id) {
			detailVuePro.tableId = id
			refresh()

		})
		laydate.render({
            elem: '#date03030201101',
            range: false
	    });
		
		/*var myDate = new Date();
		detailVuePro.nowDate = myDate.toLocaleDateString().split('/').join('-');*/
		
		detailVuePro.nowDate = getNowFormatDate();
		
	}
	/**
	 * 点击附件信息打开模态框
	 * 
	 * @author yaojianping
	 * @returns
	 */
	function openFileModel(bulidInfo) {
		detailVuePro.buildAddFile = bulidInfo;
		detailVuePro.loadUploadList = bulidInfo.attachementList;
		generalLoadFileBld(detailVuePro);
		/*
		 * 控制附件可否上传 
		 * if(bulidInfo.hasAchieve == '1'){ detailVuePro.showDelete =
		 * true; }else{ detailVuePro.showDelete = false; }
		 */
		$("#fileNodeModelDetail").modal({
			backdrop : 'static',
			keyboard : false
		});
		
	}
    /**
     * 初始化附件信息并加载当前界面的附件信息
     * @param vue Vue类型，当前页面的Vue
     * @param busiType String类型，业务编号
     */
    function generalLoadFileBld(detailVuePro) {
        var form = {
            pageNumber:"0",
            busiType : detailVuePro.busiType,
            interfaceVersion:detailVuePro.interfaceVersion,
            reqSource : detailVuePro.flag == true ? "详情" : "",//判断是否是详情页是则显示旧的附件材料
            sourceId:detailVuePro.tableId,
            approvalCode:detailVuePro.approvalCode,
            afId:detailVuePro.afId,
        };
        detailVuePro.loadUploadList=[]
        serverRequest("../Sm_AttachmentCfgList",form,function (jsonObj) {
        	detailVuePro.loadUploadList = jsonObj.sm_AttachmentCfgList;
        	var attachementListFile = detailVuePro.buildAddFile
        	if(attachementListFile.attachementList !=undefined && attachementListFile.attachementList.length>0){
        		for(var j = 0;j<detailVuePro.loadUploadList.length;j++){
        			for(var i = 0;i<attachementListFile.attachementList.length;i++){
        				var model = {
    						fileType:attachementListFile.attachementList[i].fileType,
    						name:attachementListFile.attachementList[i].remark,
    						url:attachementListFile.attachementList[i].theLink,
    						sourceType:attachementListFile.attachementList[i].sourceType,
    						tableId:attachementListFile.attachementList[i].tableId,
    						busiType : attachementListFile.attachementList[i].busiType,
        				}
        				if(detailVuePro.loadUploadList[j].eCode == attachementListFile.attachementList[i].sourceType){
        					detailVuePro.loadUploadList[j].smAttachmentList.push(model);
        				}
        			}
        		}
        	}
        	
        },function (jsonObj) {
            generalErrorModal(jsonObj)
        })
    }
	function editFileActive() {
		detailVuePro.editFile = false;
		detailVuePro.showDelete = true;
	}
	function saveFileActive() {
		
	}
	/**
	 * 保存附件信息
	 * 
	 * @author yaojianping
	 * @returns
	 */
	function dialogSave() {
		var files = this.$refs.listenUploadData.uploadData;
		var attachementList = [];

		if (files.length > 0) {
			for (var i = 0; i < files.length; i++) {
				attachementList.push(files[i]);
			}
		}
		detailVuePro.buildAddFile.attachementList = attachementList;
		$("#fileNodeModelDetail").modal('hide');

	}
	// ------------------------方法定义-结束------------------//

	// ------------------------数据初始化-开始----------------//
	// detailVuePro.refresh();
	detailVuePro.initData();
	// ------------------------数据初始化-结束----------------//
})({
	"detailDivId" : "#empj_ProjProgForcastEditDiv",
	"detailInterface" : "../Empj_ProjProgForcast_AFDetail",
	"updateInterface" : "../Empj_ProjProgForcastSave",
	// "editFileInterface": "../Empj_ProjProgForcastEdit",
	// "getBuildingList": "../Sm_UserGetBuildingList",
	"calculateExpectLimit" : "../Empj_ProjProgForcastCalculate",
	"signatureInterface" : "../Sm_SignatureUploadForPath",// 签章
	"ssoLoginInterface" : "../LoginVerification",// 网站SSO登录
});
