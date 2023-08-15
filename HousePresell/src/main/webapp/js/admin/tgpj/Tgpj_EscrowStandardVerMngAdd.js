(function(baseInfo){
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
            busiState: "",
            theName: "",
            hasEnable: 0,
            theType: 1,
			standardParameter: 0.0,  //托管标准参数--金额或比例
            amount: 0.0,
            percentage: 0.0,
            beginExpirationDate: 0.0,
            endExpirationDate: 0.0,
            theVersion: "",
            buttonType : "",

            //附件材料
            busiType : '06010101',
            loadUploadList: [],
            showDelete : true,  //附件是否可编辑
		},
		methods : {
			initData: initData,
            getTheTypeHandle: getTheTypeHandle,
            changeThousands: changeThousands,
			//添加
			getAddForm : getAddForm,
			add: add,
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

    //选择托管标准类型
    function getTheTypeHandle()
    {
        addVue.standardParameter = "0";
    }

    //将输入文本进行千分位格式化处理
    function changeThousands()
    {
        if (addVue.theType == 1)
        {
            addVue.standardParameter = addThousands(addVue.standardParameter);
        }
    }

	//详情添加操作
	function getAddForm()
	{
        if (addVue.theType == 2)
		{
			addVue.percentage = addVue.standardParameter;
            addVue.amount = 0.0;
		}
		else
		{
            addVue.amount = addVue.standardParameter;
            addVue.percentage = 0.0;
        }
        var enableState = true;
        // if (addVue.hasEnable == 1)
        // {
        //     enableState = false;
        // }
        var selectDateArr = $('#date06010101100').val().split(" - ");
		var startDate = 0;
		var endDate = 0;
		//解决ie兼容bug
		selectDateArr[0]=selectDateArr[0].replace(new RegExp(/-/gm) ,"/");
		selectDateArr[1] = selectDateArr[1].replace(new RegExp(/-/gm) ,"/");
		if (selectDateArr.length > 1)
        {
            startDate = new Date(selectDateArr[0]+" 00:00:00").getTime();
            endDate = new  Date(selectDateArr[1]+" 23:59:59").getTime();
        }
        // console.log(selectDateArr);
		return {
			interfaceVersion:this.interfaceVersion,
            busiState: this.busiState,
			theName:this.theName,
            hasEnable:enableState,
            beginExpirationDate:startDate,
            endExpirationDate:endDate,
			theType:this.theType,
			amount:commafyback(addVue.standardParameter),
			percentage:this.percentage,
			theVersion:this.theVersion,
			// extendParameter1:this.extendParameter1,
			// extendParameter2:this.extendParameter2,
            buttonType:this.buttonType,

            //附件材料
            busiType : this.busiType,
            generalAttachmentList : this.$refs.listenUploadData.uploadData
		}
	}

	//保存托管标准版本信息，发起审批流
	function add(buttonType)
	{
        if (addVue.theType == 1)
        {
            if (addVue.standardParameter != null && addVue.standardParameter != "")
            {
                if (!isThousandsRealNum(addVue.standardParameter))
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

        if (addVue.theType == 2)
        {
            if (addVue.standardParameter != null && addVue.standardParameter != "")
            {
                if (!checkNumber(addVue.standardParameter))
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

        addVue.buttonType = buttonType;
        console.log(addVue.getAddForm());
		new ServerInterfaceJsonBody(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj)
		{
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                generalSuccessModal();  //此处注意将controller中的tableId加上
                enterNewTabCloseCurrent(jsonObj.tableId,"托管标准版本详情","tgpj/Tgpj_EscrowStandardVerMngDetail.shtml");
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
            elem: '#date06010101100',
            range: true
        });
        generalLoadFile2(addVue, addVue.busiType)
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
    addVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#tgpj_EscrowStandardVerMngAddDiv",
	"addInterface":"../Tgpj_EscrowStandardVerMngAdd"
});
