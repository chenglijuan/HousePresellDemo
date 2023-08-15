(function(baseInfo){
	var listVue = new Vue({
		el : baseInfo.listDivId,
		data : {
			interfaceVersion :19000101,
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
			tgpf_RemainRightList:[],
			dataUpload:{
				"appid":"ossq7y44g",
				"appsecret":"yg2us2a7",
				"project":"HousePresell"
			},
			importUrl:null,
			deleCodes:[],
		},
		methods : {
			refresh : refresh,
			initData:initData,
			handleSelectionChange : handleSelectionChange,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			getExcelDataForm : getExcelDataForm,
			tgpf_RemainRightDel : tgpf_RemainRightDel,
			showAjaxModal:showAjaxModal,
			search:search,
			checkAllClicked : checkAllClicked,
			changePageNumber : function(data){
				listVue.pageNumber = data;
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
		    handlePictureCardPreview(file) {
		        this.dialogImageUrl = file.url;
		        this.dialogVisible = true;
		    },
		    handleCallBack(response, file, fileList){//上传成功之后的回掉函数
//		    	var modal = {
//        			sourceType:response.data[0].extra,
//        			theLink:response.data[0].url,
//        			fileType:response.data[0].objType,
//        			theSize:response.data[0].byteToStr,
//        			remark:response.data[0].originalName
//	        	}
		    	alert(a)
		    	this.importUrl = response.data[0].url
		    	this.remaindRightImportHandle()
		    },
		    parserExcelHandle : parserExcelHandle,
		    differenceHandle : differenceHandle,
		    constrast : constrast
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue
		},
		watch:{
//			pageNumber : refresh,
//			selectItem : selectedItemChanged,
		}
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
	var addVue = new Vue({
		el : baseInfo.addDivId,
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
			getAddForm : getAddForm,
			add : addTgpf_RemainRight
		}
	});

	//------------------------方法定义-开始------------------//
	 
	//列表操作--------------获取"搜索列表"表单参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			pageNumber:this.pageNumber,
			countPerPage:this.countPerPage,
			totalPage:this.totalPage,
			keyword:this.keyword,
			theState:this.theState,
			userStartId:this.userStartId,
			userRecordId:this.userRecordId,
			projectId:this.projectId,
			buildingId:this.buildingId,
			buildingUnitId:this.buildingUnitId,
			bankId:this.bankId,
		}
	}
	//列表操作--------------获取“删除资源”表单参数
	function getDeleteForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			deleCodes:this.deleCodes,
			url:this.importUrl,
		}
	}
	//列表操作--------------获取“新增”表单参数
	function getAddForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
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
	//列表操作--------------获取导入的excel数据
	function getExcelDataForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			url:this.importUrl,
		}
	}
	function tgpf_RemainRightDel()
	{
		noty({
			layout:'center',
			modal:true,
			text:"确认批量删除吗？",
			type:"confirm",
			buttons:[
				{
					addClass:"btn btn-primary",
					text:"确定",
					onClick:function($noty){
						$noty.close();
						for (var i = listVue.selectItem.length - 1; i >= 0; i--) {
							var item = listVue.selectItem[i];
							listVue.deleCodes.push(item.eCode);
							listVue.tgpf_RemainRightList.splice(item, 1);
						}
						listVue.selectItem = [];
					}
				},
				{
					addClass:"btn btn-danger",
					text:"取消",
					onClick:function($noty){
						
						$noty.close();
					}
				}
			]
			
		});
	}
	
	function isInterger(o) {
		return typeof obj === 'number' && obj%1 === 0;
	}

	function handleSelectionChange(val) {
		listVue.selectItem = [];
		for (var i = 0; i < val.length; i++) {
			var index = listVue.tgpf_RemainRightList.indexOf(val[i]);
			listVue.selectItem.push(index)
		}
		listVue.selectItem.sort(sortNumber);
//		var s = "";
//		for (var i = 0; i < listVue.selectItem.length; i++) {
//			s = s + listVue.selectItem[i] + ',';
//		}
//		alert(s);
	}
	function sortNumber(a,b) {
        return a-b
	}
	
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgpf_RemainRightList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgpf_RemainRightList.forEach(function(item) {
	    		listVue.selectItem.push(item.tableId);
	    	});
	    }
	}
	//列表操作--------------刷新
	function refresh()
	{
		new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				listVue.tgpf_RemainRightList=jsonObj.tgpf_RemainRightList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tgpf_RemainRightListDiv').scrollIntoView();
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
	function addTgpf_RemainRight()
	{
		new ServerInterface(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				$(baseInfo.addDivId).modal('hide');
				addVue.busiState = null;
				addVue.eCode = null;
				addVue.userStartId = null;
				addVue.createTimeStamp = null;
				addVue.lastUpdateTimeStamp = null;
				addVue.userRecordId = null;
				addVue.recordTimeStamp = null;
				addVue.enterTimeStamp = null;
				addVue.buyer = null;
				addVue.theNameOfCreditor = null;
				addVue.idNumberOfCreditor = null;
				addVue.eCodeOfContractRecord = null;
				addVue.eCodeOfTripleAgreement = null;
				addVue.srcBusiType = null;
				addVue.projectId = null;
				addVue.theNameOfProject = null;
				addVue.buildingId = null;
				addVue.eCodeOfBuilding = null;
				addVue.buildingUnitId = null;
				addVue.eCodeOfBuildingUnit = null;
				addVue.eCodeFromRoom = null;
				addVue.actualDepositAmount = null;
				addVue.depositAmountFromLoan = null;
				addVue.theAccountFromLoan = null;
				addVue.fundProperty = null;
				addVue.bankId = null;
				addVue.theNameOfBankPayedIn = null;
				addVue.theRatio = null;
				addVue.theAmount = null;
				addVue.limitedRetainRight = null;
				addVue.withdrawableRetainRight = null;
				addVue.currentDividedAmout = null;
				addVue.remark = null;
				refresh();
			}
		});
	}

	function remaindRightDifferenceExportExcelHandle()
	{
		new ServerInterface(baseInfo.exportExcelInterface).execute(listVue.getDeleteForm(), function (jsonObj) {
			if (jsonObj.result != "success")
			{
				noty({ "text": jsonObj.info, "type": "error", "timeout": 2000 });
			}
			else
			{
				window.location.href=jsonObj.fileDownloadPath;
				listVue.selectItem = [];
				refresh();
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
		new ServerInterface(baseInfo.importExcelInterface).execute(listVue.getExcelDataForm(), function (jsonObj) {
			if (jsonObj.result != "success")
			{
				noty({ "text": jsonObj.info, "type": "error", "timeout": 2000 });
			}
			else
			{
				listVue.selectItem = [];
				listVue.tgpf_RemainRightList=jsonObj.tgpf_RemainRightList;
			}
		});
	}
	
	function constrast()
	{
		var theId = 'tgpf_RemainRightCompareList';
		$("#tabContainer").data("tabs").addTab({ id: theId, text: '存单提取详情', closeable: true, url: 'Tgpf_RemainRightCompareList.shtml' });
	}
	
	function differenceHandle()
	{
		new ServerInterface(baseInfo.differenceInterface).execute(listVue.getDeleteForm(), function (jsonObj) {
			if (jsonObj.result != "success")
			{
				noty({ "text": jsonObj.info, "type": "error", "timeout": 2000 });
			}
			else
			{
				
			}
		});
	}
	
	function initData()
	{
		
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.refresh();
//	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tgpf_RemainRightDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Tgpf_RemainRightList",
	"addInterface":"../Tgpf_RemainRightAdd",
	"deleteInterface":"../Tgpf_RemainRightDelete",
	"updateInterface":"../Tgpf_RemainRightUpdate",
	"exportExcelInterface": "../Tgpf_RemainRightDifferenceExportExcel",
	"importExcelInterface": "../Tgpf_RemainRightImportExcel",
	"differenceInterface": "../Tgpf_RemainRightDifference"
});
