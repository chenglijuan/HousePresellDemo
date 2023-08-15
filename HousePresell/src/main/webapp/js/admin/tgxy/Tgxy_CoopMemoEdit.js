(function(baseInfo){
	var editVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tgxy_CoopMemoModel: {},
			tableId : "",
			emmp_BankInfoList: [],
			createPeo: [],
			editPeo: [],
			bankId: "",
			userUpdateId: 2,
			beforeId: "",
			theNameOfBank: "",
			signDate:"",
            errMsg: "",
            successMsg: "",
            smAttachmentList:[],
            loadUploadList : [],
            showDelete : true,
            busiType : '06110102',
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			//更新
			getUpdateForm : getUpdateForm,
			update: update,
			look: look,
			getDetailForm: getDetailForm,
			errClose: errClose,
			succClose: succClose,
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
	//详情操作--------------获取"合作备忘录详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId: editVue.tableId,
			beforeId: editVue.tableId
		}
	}

	//详情操作--------------
	function refresh()
	{
        
	}

	function getDetail()
	{
		new ServerInterface(baseInfo.detailInterface).execute(editVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				editVue.tgxy_CoopMemoModel = jsonObj.tgxy_CoopMemo;
				editVue.bankId = jsonObj.tgxy_CoopMemo.bankId;
				editVue.createPeo = jsonObj.tgxy_CoopMemo.userStart;
				editVue.editPeo = jsonObj.tgxy_CoopMemo.userUpdate;
				editVue.theNameOfBank = jsonObj.tgxy_CoopMemo.theNameOfBank;
				editVue.loadUploadList = jsonObj.smAttachmentCfgList;
			}
		});
	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getUpdateForm()
	{
		var fileUploadList = editVue.$refs.listenUploadData.uploadData;
		fileUploadList = JSON.stringify(fileUploadList);
		return {
			interfaceVersion:this.interfaceVersion,
			theState:this.theState,
			busiState:this.busiState,
			eCode:editVue.tgxy_CoopMemoModel.eCode,
			userStartId:this.userStartId,
			createTimeStamp:this.createTimeStamp,
			userUpdateId:this.userUpdateId,
			lastUpdateTimeStamp:this.lastUpdateTimeStamp,
			userRecordId:this.userRecordId,
			recordTimeStamp:this.recordTimeStamp,
			eCodeOfCooperationMemo:this.eCodeOfCooperationMemo,
			bankId:this.bankId,
			theNameOfBank:editVue.theNameOfBank,
			signDate:document.getElementById("date0611010203").value,
			userUpdateId:this.userUpdateId,
			tableId:editVue.tableId,
			smAttachmentList:fileUploadList,
		}
	}
	
	function getDetailForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			theState:this.theState,
			tableId:this.bankId,
		}
	}

	function update()
	{
		new ServerInterface(baseInfo.updateInterface).execute(editVue.getUpdateForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				editVue.errMsg = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
				    backdrop :'static'
			    });
			}
			else
			{
				 
				enterNext2Tab(editVue.tableId, '合作备忘录详情', 'tgxy/Tgxy_CoopMemoDetail.shtml',editVue.tableId+"06110102");
				refresh();
			}
		});
	}
	
	function errClose()
	{
		$(baseInfo.errorModel).modal('hide');
	}
	
	function succClose()
	{
		$(baseInfo.successModel).modal('hide');
	}
	
	function look()
	{
		if(editVue.bankId != ""){
			editVue.tgxy_CoopMemoModel.signDate = document.getElementById("date0611010203").value;
			new ServerInterface(baseInfo.detailInfoInterface).execute(editVue.getDetailForm(), function(jsonObj)
					{
						if(jsonObj.result != "success")
						{
							generalErrorModal(jsonObj);
						}
						else
						{
							$(baseInfo.detailDivId).modal('hide');
							editVue.theNameOfBank = jsonObj.emmp_BankInfo.theName;
							refresh();
						}
					});
		}
	}
	
	function bankData()
	{
		new ServerInterface(baseInfo.getInterface).execute(editVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				editVue.emmp_BankInfoList = jsonObj.emmp_BankInfoList;
				editVue.bankId = editVue.bankId;
				
				getDetail();
			}
		});
	}
	function initData()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var list = tableIdStr.split("_");
		tableIdStr = list[2];
        editVue.tableId = tableIdStr;
        bankData();
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	editVue.initData();
	editVue.refresh();
	
	// 添加日期控件
	laydate.render({
		elem: '#date0611010203',
	});
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tgxy_CoopMemoEditDiv",
	"errorModel":"#eModel",
	"successModel":"#sModel",
	"detailInterface":"../Tgxy_CoopMemoDetail",
	"getInterface": "../Emmp_BankInfoList",
	"detailInfoInterface": "../Emmp_BankInfoDetail",
	"updateInterface":"../Tgxy_CoopMemoUpdate"
});
