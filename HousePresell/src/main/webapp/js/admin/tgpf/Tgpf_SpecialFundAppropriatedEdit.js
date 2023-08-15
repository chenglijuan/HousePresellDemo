(function(baseInfo){
	var editVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tgpf_SpecialFundAppropriatedEditModel: {},
			tableId : "",
			loadUploadList : [],
            showDelete : true,
            busiType : "06120603",
            tgpf_SpacialFundAppropriatedAddList : [],
            index:"",
            appropriatedTypeList : [
            	{tableId:"1",theName:"定向支付"},
            	{tableId:"2",theName:"特殊拨付"},
            	{tableId:"3",theName:"其他支付"},
            ],
            smAttachmentList : [], //页面显示已上传的文件
			loadUploadList : [],
			showDelete : true,
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			//更新
			getUpdateForm : getUpdateForm,
			update: update,
			indexMethod : indexMethod,
			getAppropriatedType : function(data){
				this.tgpf_SpecialFundAppropriatedEditModel.appropriatedType = data.tableId;
			}
		},
		computed:{
			 
		},
		components : {
			'vue-select': VueSelect,
			"my-uploadcomponent":fileUpload
		},
		watch:{
			
		},
		mounted:function(){
			
			/**
			 * 初始化日期插件
			 */
			laydate.render({
			  elem: '#specialFundDateEdit',
			  done: function(value, date, endDate){
				  editVue.tgpf_SpecialFundAppropriatedEditModel.applyDate = value;
			  }
			});
		}
	});

	//------------------------方法定义-开始------------------//
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:this.tableId,
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
		new ServerInterface(baseInfo.detailInterface).execute(editVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
                generalErrorModal(jsonObj);
			}
			else
			{
				
				editVue.loadUploadList = jsonObj.smAttachmentCfgList;
				
				editVue.tgpf_SpecialFundAppropriatedEditModel = jsonObj.tgpf_SpecialFundAppropriated_AF;
				
				//初始受限额度
				editVue.tgpf_SpecialFundAppropriatedEditModel.orgLimitedAmount = addThousands(editVue.tgpf_SpecialFundAppropriatedEditModel.orgLimitedAmount);
				//节点受限额度
				editVue.tgpf_SpecialFundAppropriatedEditModel.nodeLimitedAmount = addThousands(editVue.tgpf_SpecialFundAppropriatedEditModel.nodeLimitedAmount);
				//现金受限额度
				editVue.tgpf_SpecialFundAppropriatedEditModel.cashLimitedAmount = addThousands(editVue.tgpf_SpecialFundAppropriatedEditModel.cashLimitedAmount);
				//有效受限额度
				editVue.tgpf_SpecialFundAppropriatedEditModel.effectiveLimitedAmount = addThousands(editVue.tgpf_SpecialFundAppropriatedEditModel.effectiveLimitedAmount);
				//总入账金额
				editVue.tgpf_SpecialFundAppropriatedEditModel.totalAccountAmount = addThousands(editVue.tgpf_SpecialFundAppropriatedEditModel.totalAccountAmount);
				//当前托管余额
				editVue.tgpf_SpecialFundAppropriatedEditModel.currentEscrowFund = addThousands(editVue.tgpf_SpecialFundAppropriatedEditModel.currentEscrowFund);
				//退房退款金额（元）： 已退款金额（元） refundAmount
				editVue.tgpf_SpecialFundAppropriatedEditModel.appropriateFrozenAmount = addThousands(editVue.tgpf_SpecialFundAppropriatedEditModel.refundAmount);
				//已申请拨付金额（元）：已拨付金额（元）
				editVue.tgpf_SpecialFundAppropriatedEditModel.payoutAmount = addThousands(editVue.tgpf_SpecialFundAppropriatedEditModel.payoutAmount);
				//当前可拨付金额（元）：可划拨金额（元）
				editVue.tgpf_SpecialFundAppropriatedEditModel.allocableAmount = addThousands(editVue.tgpf_SpecialFundAppropriatedEditModel.allocableAmount);
				//本次划款申请金额
				editVue.tgpf_SpecialFundAppropriatedEditModel.totalApplyAmount = addThousands(editVue.tgpf_SpecialFundAppropriatedEditModel.totalApplyAmount);
                
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
			eCode:this.eCode,
			tableId:editVue.tableId,
			totalApplyAmount: commafyback(this.tgpf_SpecialFundAppropriatedEditModel.totalApplyAmount),
			appropriatedType:this.tgpf_SpecialFundAppropriatedEditModel.appropriatedType,
			appropriatedRemark:this.tgpf_SpecialFundAppropriatedEditModel.appropriatedRemark,
			applyDate : this.tgpf_SpecialFundAppropriatedEditModel.applyDate,
			smAttachmentList:fileUploadList,
		}
	}
	
	function indexMethod(index){
		return generalIndexMethod(index, editVue);
	}
	
	//修改
	function update()
	{
		new ServerInterface(baseInfo.updateInterface).execute(editVue.getUpdateForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
                generalErrorModal(jsonObj);
			}
			else
			{
				/*$(baseInfo.detailDivId).modal('hide');
				refresh();*/
				enterNext2Tab(editVue.tableId, '特殊拨付详情', 'tgpf/Tgpf_SpecialFundAppropriatedDetail.shtml',editVue.tableId+"061206");
			}
		});
	}
	
	function initData()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var array = tableIdStr.split("_");
		console.log(array);
		editVue.tableId = array[2];
		refresh();
	}
	
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	editVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tgpf_SpecialFundAppropriatedEditDiv",
	"detailInterface":"../Tgpf_SpecialFundAppropriated_AFDetail",
	"updateInterface":"../Tgpf_SpecialFundAppropriated_AFUpdate"
});
