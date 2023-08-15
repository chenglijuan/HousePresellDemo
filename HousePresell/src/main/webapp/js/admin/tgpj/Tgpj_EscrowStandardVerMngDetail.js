(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tableId : 1,
			busiState : "",
            approvalState: "",
            theName: "",
            theVersion: "",
            theType: "",
            theContent: "",
            beginEndExpirationDate: "",
            hasEnable: 0,
            userUpdateName: "",
            lastUpdateTimeStamp: "",
            userRecordName: "",
            recordTimeStamp: "",

            //附件材料
            busiType : '06010101',
            loadUploadList: [],
            showDelete : false,
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
            escrowStandardVerMngEditHandle: escrowStandardVerMngEditHandle,
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
	//按钮操作-------------跳转到托管标准编辑页面
	function escrowStandardVerMngEditHandle()
	{
        enterNewTabCloseCurrent(detailVue.tableId,"编辑托管标准版本","tgpj/Tgpj_EscrowStandardVerMngEdit.shtml");
    }

	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:this.tableId,
            getDetailType:"2",
		}
	}

	//详情操作--------------
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
		new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				// console.log(jsonObj.tgpj_EscrowStandardVerMng);
				var detailModel = jsonObj.tgpj_EscrowStandardVerMng;
				detailVue.theName = detailModel.theName;
				detailVue.theVersion = detailModel.theVersion;
				detailVue.theType = detailModel.theType;
				detailVue.theContent = detailModel.theContent;
				detailVue.hasEnable = detailModel.hasEnable;
				detailVue.beginEndExpirationDate = detailModel.beginExpirationDate + " - " + detailModel.endExpirationDate;
				detailVue.userUpdateName = detailModel.userUpdateName;
				detailVue.lastUpdateTimeStamp = detailModel.lastUpdateTimeStamp;
				detailVue.userRecordName = detailModel.userRecordName;
				detailVue.recordTimeStamp = detailModel.recordTimeStamp;
                detailVue.approvalState = detailModel.approvalState;
                detailVue.busiState = detailModel.busiState;
			}
		});
	}
	
	function initData()
	{
        getIdFormTab("", function (id){
            detailVue.tableId = id;
            refresh();
        });
        // var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
        // detailVue.tableId = tableIdStr;
        // refresh();
        generalLoadFile2(detailVue, detailVue.busiType);
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tgpj_EscrowStandardVerMngDetailDiv",
	"detailInterface":"../Tgpj_EscrowStandardVerMngDetail"
});
