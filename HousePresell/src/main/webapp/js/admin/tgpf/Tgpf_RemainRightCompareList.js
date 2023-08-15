(function(baseInfo){
	var listVue = new Vue({
		el : baseInfo.listDivId,
		data : {
			interfaceVersion :19000101,
			tableId:0,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			keyword : "",
			selectItem : [],
			isAllSelected : false,
			theState:0,//正常为0，删除为1
			userStartId:null,
			userStartList:[],
			userRecordId:null,
			userRecordList:[],
			projectId:null,
			projectList:[],
			buildingId:null,
			buildingList:[],
			buildingUnitId:null,
			buildingUnitList:[],
			bankId:null,
			bankList:[],
			Tgpf_RemainRightCompareList:[
//				{
//					eCodeOfBuildingUnit:'2单元',
//					eCodeFromRoom:'504',
//					eCodeOfTripleagreement:'SFXY45678952',
//				},{
//					eCodeOfBuildingUnit:'2单元',
//					eCodeFromRoom:'504',
//					eCodeOfTripleagreement:'SFXY45678952'
//				}
			],
			dataUpload:{
				"appid":"ossq7y44g",
				"appsecret":"yg2us2a7",
				"project":"HousePresell"
			},
			importUrl:null,
			buildingRemainRightLogDetail:{building:{},buildingAccount:{},},
			hasConstrast : false,
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			getExcelDataForm : getExcelDataForm,
			getBuildingRemainRightLogDetailForm : getBuildingRemainRightLogDetailForm,
			getBuildingRemainRightLogDetail : getBuildingRemainRightLogDetail,
			showAjaxModal:showAjaxModal,
			search:search,
			checkAllClicked : checkAllClicked,
			changePageNumber : function(data){
				listVue.pageNumber = data;
			},
			headClass: function (row) {
				if(listVue.hasConstrast == true && (row.rowIndex === 0 && row.columnIndex == 6) || (row.rowIndex === 1 && row.columnIndex > 5)){
			        return 'background:#fceced';
			    }
				else if(listVue.hasConstrast == true &&(row.rowIndex === 0 && row.columnIndex == 5) || (row.rowIndex === 1 && row.columnIndex > 2)){
					return 'background:#ecfcf8';
				}
				else{
			    	return 'background:#e4f1fe';
			    }
			},
			indexMethod: function (index) {
				if (listVue.pageNumber > 1) {
					return (listVue.pageNumber - 1) * listVue.countPerPage - 0 + (index - 0 + 1);
				}
				if (listVue.pageNumber <= 1) {
					return index - 0 + 1;
				}
			},
			remaindRightDifferenceExportExcelHandle : remaindRightDifferenceExportExcelHandle,
			remaindRightImportHandle : remaindRightImportHandle,
		    parserExcelHandle : parserExcelHandle,
		    differenceHandle : differenceHandle,
		    templateDownload : templateDownload,
		    dataCorrect : dataCorrect,
		    reCalculation : reCalculation,
		    commitCalculation : commitCalculation,
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue
		},
		watch:{
//			pageNumber : refresh,
			selectItem : selectedItemChanged,
		},
	});
	var updateVue = new Vue({
		el : baseInfo.updateDivId,
		data : {
			interfaceVersion :19000101,
			theState:null,
			busiState:null,
			eCode:null,
			userStartId:null,
			userStartList:[],
			createTimeStamp:null,
			lastUpdateTimeStamp:null,
			userRecordId:null,
			userRecordList:[],
			recordTimeStamp:null,
			enterTimeStamp:null,
			buyer:null,
			theNameOfCreditor:null,
			idNumberOfCreditor:null,
			eCodeOfContractRecord:null,
			eCodeOfTripleAgreement:null,
			srcBusiType:null,
			projectId:null,
			projectList:[],
			theNameOfProject:null,
			buildingId:null,
			buildingList:[],
			eCodeOfBuilding:null,
			buildingUnitId:null,
			buildingUnitList:[],
			eCodeOfBuildingUnit:null,
			eCodeFromRoom:null,
			actualDepositAmount:null,
			depositAmountFromLoan:null,
			theAccountFromLoan:null,
			fundProperty:null,
			bankId:null,
			bankList:[],
			theNameOfBankPayedIn:null,
			theRatio:null,
			theAmount:null,
			limitedRetainRight:null,
			withdrawableRetainRight:null,
			currentDividedAmout:null,
			remark:null,
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateTgpf_RemainRight
		}
	});

	//------------------------方法定义-开始------------------//
	 
	//确定重新计算
	function commitCalculation()
	{
		$(baseInfo.commitDivId).modal({
			backdrop : 'static'
		})
	}
	
	//列表操作--------------获取"搜索列表"表单参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			pageNumber:0,
			tableId:this.tableId,
		}
	}
	//列表操作--------------获取“删除资源”表单参数
	function getDeleteForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			url:this.importUrl,
		}
	}
	//列表操作--------------获取“更新”表单参数
	function getUpdateForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			theState:this.theState,
			busiState:this.busiState,
			eCode:this.eCode,
			userStartId:this.userStartId,
			createTimeStamp:this.createTimeStamp,
			lastUpdateTimeStamp:this.lastUpdateTimeStamp,
			userRecordId:this.userRecordId,
			recordTimeStamp:this.recordTimeStamp,
			enterTimeStamp:this.enterTimeStamp,
			buyer:this.buyer,
			theNameOfCreditor:this.theNameOfCreditor,
			idNumberOfCreditor:this.idNumberOfCreditor,
			eCodeOfContractRecord:this.eCodeOfContractRecord,
			eCodeOfTripleAgreement:this.eCodeOfTripleAgreement,
			srcBusiType:this.srcBusiType,
			projectId:this.projectId,
			theNameOfProject:this.theNameOfProject,
			buildingId:this.buildingId,
			eCodeOfBuilding:this.eCodeOfBuilding,
			buildingUnitId:this.buildingUnitId,
			eCodeOfBuildingUnit:this.eCodeOfBuildingUnit,
			eCodeFromRoom:this.eCodeFromRoom,
			actualDepositAmount:this.actualDepositAmount,
			depositAmountFromLoan:this.depositAmountFromLoan,
			theAccountFromLoan:this.theAccountFromLoan,
			fundProperty:this.fundProperty,
			bankId:this.bankId,
			theNameOfBankPayedIn:this.theNameOfBankPayedIn,
			theRatio:this.theRatio,
			theAmount:this.theAmount,
			limitedRetainRight:this.limitedRetainRight,
			withdrawableRetainRight:this.withdrawableRetainRight,
			currentDividedAmout:this.currentDividedAmout,
			remark:this.remark,
		}
	}
	function getBuildingRemainRightLogDetailForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:this.tableId,
		}
	}
	//列表操作--------------获取导入的excel数据
	function getExcelDataForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			url:this.importUrl,
			buildingRemainRightLogId:this.tableId,
			billTimeStamp:this.buildingRemainRightLogDetail.billTimeStamp,
            eCodeFromConstruction:this.buildingRemainRightLogDetail.building.eCodeFromConstruction,
            theNameOfProject:this.buildingRemainRightLogDetail.theNameOfProject,
		}
	}
	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		listVue.isAllSelected = (listVue.Tgpf_RemainRightCompareList.length > 0)
		&&	(listVue.selectItem.length == listVue.Tgpf_RemainRightCompareList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.Tgpf_RemainRightCompareList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.Tgpf_RemainRightCompareList.forEach(function(item) {
	    		listVue.selectItem.push(item.tableId);
	    	});
	    }
	}
	
	function getBuildingRemainRightLogDetail()
	{
		new ServerInterface(baseInfo.buildingRemainRightLogDetail).execute(listVue.getBuildingRemainRightLogDetailForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				listVue.buildingRemainRightLogDetail=jsonObj.tgpf_BuildingRemainRightLog;
                listVue.buildingRemainRightLogDetail.buildingAccount.orgLimitedAmount= addThousands(listVue.buildingRemainRightLogDetail.buildingAccount.orgLimitedAmount);
                listVue.buildingRemainRightLogDetail.buildingAccount.cashLimitedAmount= addThousands(listVue.buildingRemainRightLogDetail.buildingAccount.cashLimitedAmount);
                listVue.buildingRemainRightLogDetail.buildingAccount.allocableAmount= addThousands(listVue.buildingRemainRightLogDetail.buildingAccount.allocableAmount);
                listVue.buildingRemainRightLogDetail.buildingAccount.refundAmount= addThousands(listVue.buildingRemainRightLogDetail.buildingAccount.refundAmount);
                listVue.buildingRemainRightLogDetail.buildingAccount.effectiveLimitedAmount= addThousands(listVue.buildingRemainRightLogDetail.buildingAccount.effectiveLimitedAmount);
                listVue.buildingRemainRightLogDetail.buildingAccount.spilloverAmount= addThousands(listVue.buildingRemainRightLogDetail.buildingAccount.spilloverAmount);
                listVue.buildingRemainRightLogDetail.buildingAccount.appropriateFrozenAmount= addThousands(listVue.buildingRemainRightLogDetail.buildingAccount.appropriateFrozenAmount);
                listVue.buildingRemainRightLogDetail.nodeLimitedAmount= addThousands(listVue.buildingRemainRightLogDetail.nodeLimitedAmount);
                listVue.buildingRemainRightLogDetail.buildingAccount.currentEscrowFund= addThousands(listVue.buildingRemainRightLogDetail.buildingAccount.currentEscrowFund);
                listVue.buildingRemainRightLogDetail.buildingAccount.payoutAmount= addThousands(listVue.buildingRemainRightLogDetail.buildingAccount.payoutAmount);
                listVue.buildingRemainRightLogDetail.buildingAccount.applyRefundPayoutAmount= addThousands(listVue.buildingRemainRightLogDetail.buildingAccount.applyRefundPayoutAmount);
                listVue.buildingRemainRightLogDetail.buildingAccount.totalAccountAmount = addThousands(listVue.buildingRemainRightLogDetail.buildingAccount.totalAccountAmount);
                
                differenceHandle()
			}
		});
	}
	
	//列表操作--------------刷新
	function refresh()
	{
//		new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj){
//			if(jsonObj.result != "success")
//			{
//				noty({"text":jsonObj.info,"type":"error","timeout":2000});
//			}
//			else
//			{
//				listVue.Tgpf_RemainRightCompareList=jsonObj.Tgpf_RemainRightCompareList;
//				//动态跳转到锚点处，id="top"
//				document.getElementById('tgpf_RemainRightListDiv').scrollIntoView();
//			}
//		});
		new ServerInterface(baseInfo.importExcelInterface).execute(listVue.getExcelDataForm(), function (jsonObj) {
			if (jsonObj.result != "success")
			{
				noty({ "text": jsonObj.info, "type": "error", "timeout": 2000 });
			}
			else
			{
				listVue.selectItem = [];
				listVue.Tgpf_RemainRightCompareList=jsonObj.Tgpf_RemainRightCompareList;
                listVue.Tgpf_RemainRightCompareList.forEach(function(item){
                    item.theAmountAddThousands = addThousands(item.theAmount);
                    item.limitedRetainRightAddThousands = addThousands(item.limitedRetainRight);
                    item.withdrawableRetainRightAddThousands = addThousands(item.withdrawableRetainRight);
                    item.theAmount_UploadAddThousands = addThousands(item.theAmount_Upload);
                    item.limitedRetainRight_UploadAddThousands = addThousands(item.limitedRetainRight_Upload);
                    item.withdrawableRetainRight_UploadAddThousands = addThousands(item.withdrawableRetainRight_Upload);
                    item.theAmount_CompareAddThousands = addThousands(item.theAmount_Compare);
                    item.limitedRetainRight_CompareAddThousands = addThousands(item.limitedRetainRight_Compare);
                    item.withdrawableRetainRight_CompareAddThousands = addThousands(item.withdrawableRetainRight_Compare);
                    item.actualDepositAmountAddThousands = addThousands(item.actualDepositAmount);
                })
			}
		});
	}
	
	//列表操作------------搜索
	function search()
	{
		listVue.pageNumber = 1;
		refresh();
	}
	
	//弹出编辑模态框--更新操作
	function showAjaxModal(tgpf_RemainRightModel)
	{
		//tgpf_RemainRightModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'tgpf_RemainRight', tgpf_RemainRightModel);
		//updateVue.$set("tgpf_RemainRight", tgpf_RemainRightModel);
		
		updateVue.theState = tgpf_RemainRightModel.theState;
		updateVue.busiState = tgpf_RemainRightModel.busiState;
		updateVue.eCode = tgpf_RemainRightModel.eCode;
		updateVue.userStartId = tgpf_RemainRightModel.userStartId;
		updateVue.createTimeStamp = tgpf_RemainRightModel.createTimeStamp;
		updateVue.lastUpdateTimeStamp = tgpf_RemainRightModel.lastUpdateTimeStamp;
		updateVue.userRecordId = tgpf_RemainRightModel.userRecordId;
		updateVue.recordTimeStamp = tgpf_RemainRightModel.recordTimeStamp;
		updateVue.enterTimeStamp = tgpf_RemainRightModel.enterTimeStamp;
		updateVue.buyer = tgpf_RemainRightModel.buyer;
		updateVue.theNameOfCreditor = tgpf_RemainRightModel.theNameOfCreditor;
		updateVue.idNumberOfCreditor = tgpf_RemainRightModel.idNumberOfCreditor;
		updateVue.eCodeOfContractRecord = tgpf_RemainRightModel.eCodeOfContractRecord;
		updateVue.eCodeOfTripleAgreement = tgpf_RemainRightModel.eCodeOfTripleAgreement;
		updateVue.srcBusiType = tgpf_RemainRightModel.srcBusiType;
		updateVue.projectId = tgpf_RemainRightModel.projectId;
		updateVue.theNameOfProject = tgpf_RemainRightModel.theNameOfProject;
		updateVue.buildingId = tgpf_RemainRightModel.buildingId;
		updateVue.eCodeOfBuilding = tgpf_RemainRightModel.eCodeOfBuilding;
		updateVue.buildingUnitId = tgpf_RemainRightModel.buildingUnitId;
		updateVue.eCodeOfBuildingUnit = tgpf_RemainRightModel.eCodeOfBuildingUnit;
		updateVue.eCodeFromRoom = tgpf_RemainRightModel.eCodeFromRoom;
		updateVue.actualDepositAmount = tgpf_RemainRightModel.actualDepositAmount;
		updateVue.depositAmountFromLoan = tgpf_RemainRightModel.depositAmountFromLoan;
		updateVue.theAccountFromLoan = tgpf_RemainRightModel.theAccountFromLoan;
		updateVue.fundProperty = tgpf_RemainRightModel.fundProperty;
		updateVue.bankId = tgpf_RemainRightModel.bankId;
		updateVue.theNameOfBankPayedIn = tgpf_RemainRightModel.theNameOfBankPayedIn;
		updateVue.theRatio = tgpf_RemainRightModel.theRatio;
		updateVue.theAmount = tgpf_RemainRightModel.theAmount;
		updateVue.limitedRetainRight = tgpf_RemainRightModel.limitedRetainRight;
		updateVue.withdrawableRetainRight = tgpf_RemainRightModel.withdrawableRetainRight;
		updateVue.currentDividedAmout = tgpf_RemainRightModel.currentDividedAmout;
		updateVue.remark = tgpf_RemainRightModel.remark;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateTgpf_RemainRight()
	{
		new ServerInterface(baseInfo.updateInterface).execute(updateVue.getUpdateForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				$(baseInfo.updateDivId).modal('hide');
				refresh();
			}
		});
	}

	function remaindRightDifferenceExportExcelHandle()
	{
		var model={
			interfaceVersion:listVue.interfaceVersion,
			tgpf_RemainRight_DtlTab:listVue.Tgpf_RemainRightCompareList,	
			billTimeStamp:listVue.buildingRemainRightLogDetail.billTimeStamp,
		};
		new ServerInterfaceJsonBody(baseInfo.exportExcelInterface).execute(model, function (jsonObj) {
			if (jsonObj.result != "success")
			{
				noty({ "text": jsonObj.info, "type": "error", "timeout": 2000 });
			}
			else
			{
				window.location.href=jsonObj.fileDownloadPath;
			}
		});
	}
	
	function remaindRightImportHandle()
	{
		
		$('#uploadFilePath').fileupload({
			autoUpload: true,
			url: '../FileUploadServlet',
			dataType: 'json',
			progressall: function (e, data) {
				//var progress = parseInt(data.loaded / data.total * 100, 10);
				//progress = progress * 0.10;
				//console.info(progress+"%");
		    },
			add: function (e, data) 
			{
				var files = data.files;
				var length = files.length;
				
				data.submit();
			},
			done: function (e, data) 
			{
				//刷新文件列表，显示出刚上传的文件
//				data = data.result;
//				console.log(data);
				listVue.importUrl = data.result.fileFullPath
				listVue.parserExcelHandle()
			}
		});
	}
	
	function parserExcelHandle()
	{
		new ServerInterface(baseInfo.differenceInterface).execute(listVue.getExcelDataForm(), function (jsonObj) {
			if (jsonObj.result != "success")
			{
				noty({ "text": jsonObj.info, "type": "error", "timeout": 2000 });
			}
			else
			{
				listVue.hasConstrast = true
				listVue.selectItem = [];
				listVue.Tgpf_RemainRightCompareList=jsonObj.Tgpf_RemainRightCompareList;
                listVue.Tgpf_RemainRightCompareList.forEach(function(item){
                    item.theAmountAddThousands = addThousands(item.theAmount);
                    item.limitedRetainRightAddThousands = addThousands(item.limitedRetainRight);
                    item.withdrawableRetainRightAddThousands = addThousands(item.withdrawableRetainRight);
                    item.theAmount_UploadAddThousands = addThousands(item.theAmount_Upload);
                    item.limitedRetainRight_UploadAddThousands = addThousands(item.limitedRetainRight_Upload);
                    item.withdrawableRetainRight_UploadAddThousands = addThousands(item.withdrawableRetainRight_Upload);
                    item.theAmount_CompareAddThousands = addThousands(item.theAmount_Compare);
                    item.limitedRetainRight_CompareAddThousands = addThousands(item.limitedRetainRight_Compare);
                    item.withdrawableRetainRight_CompareAddThousands = addThousands(item.withdrawableRetainRight_Compare);
                    item.actualDepositAmountAddThousands = addThousands(item.actualDepositAmount);
                })
			}
		});
	}
	
	function differenceHandle()
	{
		new ServerInterface(baseInfo.differenceInterface).execute(listVue.getExcelDataForm(), function (jsonObj) {
			if (jsonObj.result != "success")
			{
				noty({ "text": jsonObj.info, "type": "error", "timeout": 2000 });
			}
			else
			{
				listVue.selectItem = [];
				listVue.Tgpf_RemainRightCompareList=jsonObj.Tgpf_RemainRightCompareList;
                listVue.Tgpf_RemainRightCompareList.forEach(function(item){
                    item.theAmountAddThousands = addThousands(item.theAmount);
                    item.limitedRetainRightAddThousands = addThousands(item.limitedRetainRight);
                    item.withdrawableRetainRightAddThousands = addThousands(item.withdrawableRetainRight);
                    item.theAmount_UploadAddThousands = addThousands(item.theAmount_Upload);
                    item.limitedRetainRight_UploadAddThousands = addThousands(item.limitedRetainRight_Upload);
                    item.withdrawableRetainRight_UploadAddThousands = addThousands(item.withdrawableRetainRight_Upload);
                    item.theAmount_CompareAddThousands = addThousands(item.theAmount_Compare);
                    item.limitedRetainRight_CompareAddThousands = addThousands(item.limitedRetainRight_Compare);
                    item.withdrawableRetainRight_CompareAddThousands = addThousands(item.withdrawableRetainRight_Compare);
                    item.actualDepositAmountAddThousands = addThousands(item.actualDepositAmount);
                })
			}
		});
	}
	
	function templateDownload()
	{
		window.location.href = "../excelTemplate/remainRightTemplate.xlsx"
	}
	
	function initData()
	{
        getIdFormTab('', function (id) {
            listVue.tableId = id;
            getBuildingRemainRightLogDetail();
        });
	}
	
	function dataCorrect()
	{
		var model={
			interfaceVersion:listVue.interfaceVersion,
			tgpf_RemainRight_DtlTab:listVue.Tgpf_RemainRightCompareList,	
		};
		new ServerInterfaceJsonBody(baseInfo.correctInterface).execute(model, function (jsonObj) {
			if (jsonObj.result != "success")
			{
				noty({ "text": jsonObj.info, "type": "error", "timeout": 2000 });
			}
			else
			{
				listVue.initData();
				
				//修正完成后提示
				$("#selectMsgModal").html("托管系统留存权益数据已修正")
			    $("#selectM").modal('show', {
			        backdrop: 'static'
			    });
				
			}
		});
	}
	
	function reCalculation()
	{
		$(baseInfo.commitDivId).modal('hide');
		
		//等待弹窗
		$(baseInfo.waitDivId).modal({
			backdrop :'static',
			keyboard: false
		});
		
		var model={
			interfaceVersion:listVue.interfaceVersion,
			billTimeStamp:listVue.buildingRemainRightLogDetail.billTimeStamp,
			buildingId:listVue.buildingRemainRightLogDetail.buildingId,
			srcBusiType:"手工计算",
			tableId:listVue.buildingRemainRightLogDetail.tableId,
		};
		new ServerInterface(baseInfo.calculationInterface).execute(model, function (jsonObj) {
			if (jsonObj.result != "success")
			{
				$(baseInfo.waitDivId).modal('hide');
				generalErrorModal(jsonObj,jsonObj.info);
				
//				noty({ "text": jsonObj.info, "type": "error", "timeout": 2000 });
			}
			else
			{
				$(baseInfo.waitDivId).modal('hide');
				
				listVue.tableId = jsonObj.tgpf_BuildingRemainRightLog.tableId;
				listVue.hasConstrast = false;
				listVue.importUrl = "";
				getBuildingRemainRightLogDetail();
				differenceHandle();
			}
		});
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
//	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#Tgpf_RemainRightCompareListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"waitDivId":"#waitingModalRemainRightCompareList",
	"commitDivId" : "#commitModalRemainRightCompareList",
	"listInterface":"../Tgpf_RemainRightList",
	"updateInterface":"../Tgpf_RemainRightUpdate",
	"exportExcelInterface": "../Tgpf_RemainRightDifferenceExportExcel",
	"importExcelInterface": "../Tgpf_RemainRightImportExcel",
	"buildingRemainRightLogDetail":"../Tgpf_BuildingRemainRightLogDetail",
	"differenceInterface": "../Tgpf_RemainRightDifference",
	"correctInterface":"../Tgpf_RemainRightCorrect",
	"calculationInterface":"../Tgpf_RemainRightCalculation",
});
