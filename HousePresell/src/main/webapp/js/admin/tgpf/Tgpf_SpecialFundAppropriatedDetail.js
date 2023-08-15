(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
            tgpf_FundAppropriated_AFModel: {},
            tgpf_fundAppropriated_afDtltab:[],
            tgpf_fundAppropriatedtab:[],
			tableId : 1,
            disabled:false,
            tgpf_SpecialFundAppropriatedDetailModel : {},
            showButton : true,
            busiType : '06120603',
			busiCode : "061206", // 业务编码
			tgpf_SpacialFundAppropriatedAddList : [],
			isEdit : true,
			smAttachmentList : [], //页面显示已上传的文件
			loadUploadList : [],
			showDelete : false,
			showHk : false,
		},
		methods : {
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
            getUpdateForm:getUpdateForm,
            updateTgpf_FundAppropriated:updateTgpf_FundAppropriated,
            SpecialFundAppropriatedEditHandle : SpecialFundAppropriatedEditHandle,
            SpecialFundAppropriatedCommitHandle : SpecialFundAppropriatedCommitHandle,
            exportPdf:exportPdf,//导出pdf
            getExportForm : getExportForm,
            userGet : userGet,//用户获取
		},
		computed:{
			 
		},
		components : {
			"my-uploadcomponent" : fileUpload
		},
		watch:{
		},
	});

	// ------------------------方法定义-开始------------------//
	
	// 提交操作
	function SpecialFundAppropriatedCommitHandle()
	{
		
		if(confirmFile(this.loadUploadList) == true){
			var model = {
					interfaceVersion : this.interfaceVersion,
					tableId : this.tableId,
					busiCode : this.busiCode
			}
			
			new ServerInterface(baseInfo.sumitInterface).execute(model, function(jsonObj) {
				if (jsonObj.result != "success") {
					generalErrorModal(jsonObj);
				} else {
					generalSuccessModal();
					
					detailVue.isEdit = false;
					
				}
			});
			
			getDetail();
		}
	}

    function initData()
    {
    	userGet();
    	
    	getIdFormTab("",function (id) {
    		detailVue.tableId=id;
    		refresh();
        });
    }

	function getSearchForm()
	{
		return {
			interfaceVersion:detailVue.interfaceVersion,
            fundAppropriated_AFId:detailVue.tableId,
            tableId:detailVue.tableId,
		}
	}

	// 详情操作--------------
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
                generalErrorModal(jsonObj);
			}
			else
			{
				
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
                detailVue.tgpf_SpecialFundAppropriatedDetailModel = jsonObj.tgpf_SpecialFundAppropriated_AF;
                
                if(detailVue.tgpf_SpecialFundAppropriatedDetailModel.busiState != "1")
                {
                	detailVue.isEdit = false;
                }
                
                detailVue.tgpf_SpecialFundAppropriatedDetailModel.orgLimitedAmount = addThousands(detailVue.tgpf_SpecialFundAppropriatedDetailModel.orgLimitedAmount);
                detailVue.tgpf_SpecialFundAppropriatedDetailModel.nodeLimitedAmount = addThousands(detailVue.tgpf_SpecialFundAppropriatedDetailModel.nodeLimitedAmount);
				detailVue.tgpf_SpecialFundAppropriatedDetailModel.cashLimitedAmount = addThousands(detailVue.tgpf_SpecialFundAppropriatedDetailModel.cashLimitedAmount);
				detailVue.tgpf_SpecialFundAppropriatedDetailModel.effectiveLimitedAmount = addThousands(detailVue.tgpf_SpecialFundAppropriatedDetailModel.effectiveLimitedAmount);
				detailVue.tgpf_SpecialFundAppropriatedDetailModel.totalAccountAmount = addThousands(detailVue.tgpf_SpecialFundAppropriatedDetailModel.totalAccountAmount);
				detailVue.tgpf_SpecialFundAppropriatedDetailModel.currentEscrowFund = addThousands(detailVue.tgpf_SpecialFundAppropriatedDetailModel.currentEscrowFund);
				detailVue.tgpf_SpecialFundAppropriatedDetailModel.appropriateFrozenAmount = addThousands(detailVue.tgpf_SpecialFundAppropriatedDetailModel.appropriateFrozenAmount);
				detailVue.tgpf_SpecialFundAppropriatedDetailModel.payoutAmount = addThousands(detailVue.tgpf_SpecialFundAppropriatedDetailModel.payoutAmount);
				detailVue.tgpf_SpecialFundAppropriatedDetailModel.allocableAmount = addThousands(detailVue.tgpf_SpecialFundAppropriatedDetailModel.allocableAmount);
				detailVue.tgpf_SpecialFundAppropriatedDetailModel.totalApplyAmount = addThousands(detailVue.tgpf_SpecialFundAppropriatedDetailModel.totalApplyAmount);
					
				detailVue.tgpf_SpacialFundAppropriatedAddList = jsonObj.tgpf_SpecialFundAppropriated_AF.AFDtlList;
				
				if(detailVue.tgpf_SpacialFundAppropriatedAddList!=undefined&&detailVue.tgpf_SpacialFundAppropriatedAddList.length>0)
				{
					detailVue.tgpf_SpacialFundAppropriatedAddList.forEach(function(item){
						item.accountBalance = addThousands(item.accountBalance);
						item.appliedAmount = addThousands(item.appliedAmount);
						item.applyRefundPayoutAmount = addThousands(item.applyRefundPayoutAmount);
					})
				}
			}
		});
	}
	
	// 点击修改
	function SpecialFundAppropriatedEditHandle(){
		enterNextTab(detailVue.tableId, '特殊资金拨付修改', 'tgpf/Tgpf_SpecialFundAppropriatedEdit.shtml',detailVue.tableId+"061206");
	}
    function getUpdateForm()
    {
        return {
            interfaceVersion:detailVue.interfaceVersion,
            buttonType:detailVue.buttonType,
            fundAppropriated_AFId:detailVue.tableId,
			remark:detailVue.tgpf_FundAppropriated_AFModel.remark,
            fundAppropriated_AFId:detailVue.tableId, // 用款申请id
            tgpf_FundAppropriatedList:detailVue.tgpf_fundAppropriatedtab // 资金拨付
        }
    }

	function updateTgpf_FundAppropriated(buttonType)
	{
        for (var i = 0; i < detailVue.tgpf_fundAppropriatedtab.length; i++)
        {
            detailVue.tgpf_fundAppropriatedtab[i].actualPayoutDate = $('#tgpf_FundAppropriatedDetail'+i).val();
        }

		if(buttonType == 1)
		{
            detailVue.buttonType = 1;
		}else if(buttonType == 2)
		{
            detailVue.buttonType = 2;
		}
        new ServerInterfaceJsonBody(baseInfo.updateInterface).execute(detailVue.getUpdateForm(), function(jsonObj)
		{
            if (jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
			{
                generalSuccessModal();
                getDetail();
			}
        });

	}
	
	//列表操作-----------------------导出PDF
	function getExportForm() {
		var href = window.location.href;
		return {
			interfaceVersion : this.interfaceVersion,
			sourceId : this.tableId,
			reqAddress : href,
			sourceBusiCode : "06120603",
		}
	}
	
	function exportPdf(){
		
		new ServerInterface(baseInfo.exportInterface).execute(detailVue.getExportForm(), function(jsonObj){
			if (jsonObj.result != "success") {
				$(baseInfo.edModelDivId).modal({
					backdrop : 'static'
				});
				detailVue.errMsg = jsonObj.info;
			} else {
				window.open(jsonObj.pdfUrl,"_blank");
				
			}
		});
	}
	
	function userGet(){
			new ServerInterface(baseInfo.userGetInterface).execute(detailVue.getExportForm(), function(jsonObj){
				if (jsonObj.result != "success") {
					
				} else {
					console.log(jsonObj);
					if(jsonObj.sm_User.theType == 1)
					{
						detailVue.showHk=true;
					}
				}
			});
		}

	// ------------------------方法定义-结束------------------//
	
	// ------------------------数据初始化-开始----------------//
	detailVue.initData();
	// ------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tgpf_FundAppropriatedDetailDiv",
	"userGetInterface":"../Sm_UserGet",//获取用户信息
	"detailInterface":"../Tgpf_SpecialFundAppropriated_AFDetail",
    // "updateInterface":"../Tgpf_FundAppropriatedUpdate",
	"sumitInterface":"../Tgpf_SpecialFundAppropriated_AFApprovalProcess",
	"exportInterface":"../exportPDFByWord",//导出pdf
});
