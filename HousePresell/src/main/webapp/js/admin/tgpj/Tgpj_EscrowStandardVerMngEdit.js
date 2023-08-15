(function(baseInfo){
	var editVue = new Vue({
		el : baseInfo.editDivId,
		data : {
			interfaceVersion :19000101,
			tableId : 1,
            busiState: "未维护",
            theName: "",
            hasEnable: 0,
            theType: 1,
            standardParameter: 0.0,  //托管标准参数--金额或比例
            amount: 0.0,
            percentage: 0.0,
            beginExpirationDate: 0.0,
            endExpirationDate: 0.0,
            beginEndExpirationDate: "",
            theVersion: "",
            buttonType : "",

            //附件材料
            busiType : '06010101',
            loadUploadList: [],
            showDelete : true,
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
            getTheTypeHandle: getTheTypeHandle,
			getSearchForm : getSearchForm,
            changeThousands: changeThousands,
			//更新
			getUpdateForm : getUpdateForm,
			update: update,
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
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:this.tableId,
            getDetailType:"1",
		}
	}

	//详情操作--------------
	function refresh()
	{
		if (editVue.tableId == null || editVue.tableId < 1)
		{
			return;
		}

		getDetail();
	}

	function getDetail()
	{
		// console.log(editVue.getSearchForm());
		new ServerInterface(baseInfo.detailInterface).execute(editVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
                // console.log(jsonObj.tgpj_EscrowStandardVerMng);
                var detailModel = jsonObj.tgpj_EscrowStandardVerMng;
                editVue.tableId = detailModel.tableId;
                editVue.busiState = detailModel.busiState;
                editVue.theName = detailModel.theName;
                editVue.theVersion = detailModel.theVersion;
                editVue.theType = detailModel.theType;
                editVue.amount = detailModel.amount;
                editVue.percentage = detailModel.percentage;
                if (detailModel.theType == "2")
				{
                    editVue.standardParameter = detailModel.percentage;
				}
				else
				{
                    editVue.standardParameter = detailModel.amount;
				}
                editVue.hasEnable = detailModel.hasEnable;
                editVue.beginExpirationDate = detailModel.beginExpirationDate;
                editVue.endExpirationDate = detailModel.endExpirationDate;
                editVue.beginEndExpirationDate = detailModel.beginExpirationDate + " - " + detailModel.endExpirationDate;
                editVue.userUpdateName = detailModel.userUpdateName;
                editVue.userRecordName = detailModel.userRecordName;
                editVue.recordTimeStamp = detailModel.recordTimeStamp;

                $('#date06010101200').val(detailModel.beginExpirationDate + " - " + detailModel.endExpirationDate);
			}
		});
	}

    //选择托管标准类型
    function getTheTypeHandle()
    {
        editVue.standardParameter = "0";
    }

    //将输入文本进行千分位格式化处理
    function changeThousands()
    {
        if (editVue.theType == 1)
        {
            editVue.standardParameter = addThousands(editVue.standardParameter);
        }
    }

    //详情更新操作--------------获取"更新机构详情"参数
	function getUpdateForm()
	{
        if (editVue.theType == 2)
        {
            editVue.percentage = editVue.standardParameter;
            editVue.amount = 0.0;
        }
        else
        {
            editVue.amount = editVue.standardParameter;
            editVue.percentage = 0.0;
        }
        var enableState = true;
        // if (editVue.hasEnable == 1)
        // {
        //     enableState = false;
        // }
        var selectDateArr = $('#date06010101200').val().split(" - ");
        var startDate = 0;
        var endDate = 0;
        if (selectDateArr.length > 1)
        {
            startDate = new  Date(selectDateArr[0]+" 00:00:00").getTime();
            endDate = new  Date(selectDateArr[1]+" 23:59:59").getTime();
        }
		return {
			interfaceVersion:this.interfaceVersion,
			tableId: this.tableId,
            busiState: this.busiState,
			theName:this.theName,
			theVersion:this.theVersion,
			theType:this.theType,
            hasEnable:enableState,
			// theContent:this.theContent,
			amount:commafyback(editVue.standardParameter),
			percentage:this.percentage,
			// extendParameter1:this.extendParameter1,
			// extendParameter2:this.extendParameter2,
			beginExpirationDate:startDate,
			endExpirationDate:endDate,
            buttonType:this.buttonType,

            //附件参数
            busiType : this.busiType,
            sourceId : this.tableId,
            generalAttachmentList : this.$refs.listenUploadData.uploadData,
		}
	}

	//更新拖把标准版本信息，发起审批流
	function update(buttonType)
	{
        if (editVue.theType == 1)
        {
            if (editVue.standardParameter != null && editVue.standardParameter != "")
            {
                if (!isThousandsRealNum(editVue.standardParameter))
                {
                    generalErrorModal(null, "输入的托管金额格式不正确");
                    return;
                }
            }
            else
            {
                generalErrorModal(null, "请输入托管金额");
                return;
            }
        }

        if (editVue.theType == 2)
        {
            if (editVue.standardParameter != null && editVue.standardParameter != "")
            {
                if (!checkNumber(editVue.standardParameter))
                {
                    generalErrorModal(null, "输入的托管比例格式不正确");
                    return;
                }
            }
            else
            {
                generalErrorModal(null, "请输入托管比例");
                return;
            }
        }
        
        editVue.buttonType = buttonType;
		console.log(editVue.getUpdateForm());
		new ServerInterfaceJsonBody(baseInfo.updateInterface).execute(editVue.getUpdateForm(), function(jsonObj)
		{
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
            	console.log("-----跳转到详情"+editVue.tableId);
                generalSuccessModal();
                enterNewTabCloseCurrent(editVue.tableId,"托管标准版本详情","tgpj/Tgpj_EscrowStandardVerMngDetail.shtml");
            }
		});
	}

    //数字、小数点校验
    function checkNumber(text)
    {
        var patrn = /^\d+(\.\d+)?$/;
        var result = true;
        if (!patrn.exec(text)) {
            result = false;
        }
        return result;
    }

	function initData()
	{
        laydate.render({
            elem: '#date06010101200',
            range: true
        });
        getIdFormTab("", function (id){
            editVue.tableId = id;
            refresh();
        });
        generalLoadFile2(editVue, editVue.busiType)
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
    editVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"editDivId":"#tgpj_EscrowStandardVerMngEditDiv",
	"detailInterface":"../Tgpj_EscrowStandardVerMngDetail",
	"updateInterface":"../Tgpj_EscrowStandardVerMngUpdate"
});
