(function(baseInfo){
	var updateVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion : 19000101,
			tableId : 1,
			empj_BuildingInfo : {
			},
			empj_BuildingInfoNew : {
			},
			//附件材料
			busiType : '03010201',
			loadUploadList: [],
			showDelete : true,
			tgpj_EscrowStandardVerMngId : "",
			tgpj_EscrowStandardVerMngList : [],
			buttonType : "",//按钮来源（保存、提交）
			busiId:"",
			busiCode:"",
			theValue:"",
			sm_BaseParameterList:[],
			approvalCode: "03010202",
			landMortgageAmount:"",
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			//更新
			getUpdateForm : getUpdateForm,
			update: update,
			getEscrowStandardVerMngId : function (data){
				this.tgpj_EscrowStandardVerMngId = data.tableId;
			},
			baseParameterChange:baseParameterChange,
			changeThousands:function(){
				updateVue.landMortgageAmount = addThousands(updateVue.landMortgageAmount);
			}
		},
		computed:{
			 
		},
		components : {
			"my-uploadcomponent":fileUpload,
			'vue-select': VueSelect
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
			tableId:this.tableId
		}
	}

	//详情操作--------------
	function refresh()
	{
		if (updateVue.tableId == null || updateVue.tableId < 1) 
		{
			return;
		}

		getDetail();
		
		//根据busiState按
		
		parent.generalLoadFileTest(updateVue, updateVue.busiType)
	}

	function getDetail()
	{
		new ServerInterfaceSync(baseInfo.detailInterface).execute(updateVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
                parent.generalErrorModal(jsonObj);
			}
			else
			{
				updateVue.empj_BuildingInfo = jsonObj.empj_BuildingInfo;
				updateVue.empj_BuildingInfoNew = jsonObj.empj_BuildingInfoNew;
				updateVue.busiId = jsonObj.empj_BuildingInfoNew.parameterName;
				updateVue.theValue = jsonObj.empj_BuildingInfoNew.deliveryType;
				updateVue.tgpj_EscrowStandardVerMngId = jsonObj.empj_BuildingInfoNew.tgpj_EscrowStandardVerMngId;
				updateVue.landMortgageAmount = addThousands(jsonObj.empj_BuildingInfoNew.landMortgageAmount == null ? "" : jsonObj.empj_BuildingInfoNew.landMortgageAmount);
				updateVue.empj_BuildingInfo.landMortgageAmount = addThousands(jsonObj.empj_BuildingInfo.landMortgageAmount == null ? "" : jsonObj.empj_BuildingInfo.landMortgageAmount);
				if(updateVue.empj_BuildingInfo.busiState == '未备案')
				{
					updateVue.approvalCode = '03010201';
				}
			}
		});
	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getUpdateForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:this.tableId,
			eCodeFromConstruction:this.empj_BuildingInfoNew.eCodeFromConstruction,//施工编号
			escrowArea:this.empj_BuildingInfoNew.escrowArea,//托管面积
			upfloorNumber:this.empj_BuildingInfoNew.upfloorNumber,//地上层数
			landMortgagor:this.empj_BuildingInfoNew.landMortgagor,//土地抵押权人
			eCodeFromPublicSecurity:this.empj_BuildingInfoNew.eCodeFromPublicSecurity,//公安编号
			tgpj_EscrowStandardVerMngId : this.tgpj_EscrowStandardVerMngId,//托管标准
			downfloorNumber:this.empj_BuildingInfoNew.downfloorNumber,//地下层数
			landMortgageAmount:parent.commafyback(this.landMortgageAmount),//土地抵押金额
			buildingArea:this.empj_BuildingInfoNew.buildingArea,//建筑面积
			deliveryType:this.theValue,//交付类型
			landMortgageState:this.empj_BuildingInfoNew.landMortgageState,//土地抵押状态
			paymentLines:this.empj_BuildingInfoNew.paymentLines,//支付保证上限比例
			remark:this.empj_BuildingInfoNew.remark,
			buttonType:this.buttonType,
			busiType : this.busiType,
			sourceId : this.tableId,
			generalAttachmentList : this.$refs.listenUploadData.uploadData
		}
	}

	function update(buttonType)
	{
		updateVue.buttonType = buttonType;
		if(updateVue.landMortgageAmount != null && updateVue.landMortgageAmount != "" && !isThousandsRealNum(updateVue.landMortgageAmount))
		{
			var jsonObj = {};
			jsonObj.info = "请先校验是否填写了有效的千分位格式的金额";
            parent.generalErrorModal(jsonObj);
		}
		else
		{
			new ServerInterfaceJsonBody(baseInfo.updateInterface).execute(updateVue.getUpdateForm(), function(jsonObj)
			{
				if(jsonObj.result != "success")
				{
                    parent.generalErrorModal(jsonObj);
				}
				else
				{
                    parent.generalSuccessModal();
                    parent.enterNewTabAndCloseCurrent(updateVue.tableId, '楼幢详情', 'test/Test_BuildingInfoDetail.shtml')
				}
			});
		}
	}
	
	function initData()
	{
        updateVue.tableId = parent.getTabElementUI_TableId();
        refresh();
		getSm_BaseParameterForSelect();
	}
	
	function getEscrowStandardVerMngList()
	{
		var model = {
			interfaceVersion : 19000101,
			theState : 0
		};
		new ServerInterface(baseInfo.escrowStandardVerMngListInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				parent.generalErrorModal(jsonObj);
			}
			else
			{
				updateVue.tgpj_EscrowStandardVerMngList = jsonObj.tgpj_EscrowStandardVerMngList;
			}
		});
	}
	
	function baseParameterChange(data)
    {
		updateVue.busiId = data.tableId;
		updateVue.theValue = data.theValue;
    }
	
	function getSm_BaseParameterForSelect()
	{
	    generalGetParamList("5",function (list) 
	    {
			updateVue.sm_BaseParameterList =list;
	    })
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	updateVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#empj_BuildingInfoUpdateDiv",
	"detailInterface":"../../Empj_BuildingInfoDetail",
	"updateInterface":"../../Empj_BuildingInfoUpdate",
	"escrowStandardVerMngListInterface":"../../Tgpj_EscrowStandardVerMngForSelect",
});
