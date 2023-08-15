(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
            tgpf_FundAppropriated_AFModel: {},
            tgpf_fundAppropriated_afDtltab:[],
            tgpf_fundAppropriatedtab:[],
			tableId : 1,
            disabled:true,
            currentdate:"",
            showDownFlag : true,
            flag : true,
            isPrint : false,
		},
		methods : {
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
            getUpdateForm:getUpdateForm,
            updateTgpf_FundAppropriated:updateTgpf_FundAppropriated,
            getExportForm :getExportForm,//导出参数
			exportPdf:exportPdf,//导出pdf
		},
		computed:{
			 
		},
		components : {

		},
		watch:{
		},
	});

	//------------------------方法定义-开始------------------//

    function initData()
    {
    	getIdFormTab("",function (id) {
    		detailVue.tableId=id;
    		refresh();
        });
        var date = new Date();
        var seperator1 = "-";
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        detailVue.currentdate = year + seperator1 + month + seperator1 + strDate;
    }

	function getSearchForm()
	{
		return {
			interfaceVersion:detailVue.interfaceVersion,
            fundAppropriated_AFId:detailVue.tableId,
		}
	}
	
	function getExportForm() {
		var href = window.location.href;
		return {
			interfaceVersion : this.interfaceVersion,
			sourceId : this.tableId,
			reqAddress : href,
			sourceBusiCode : "06120303",
		}
	}
	
	//列表操作-----------------------导出PDF
	function exportPdf(){
		new ServerInterface(baseInfo.exportInterface).execute(detailVue.getExportForm(), function(jsonObj){
			if (jsonObj.result != "success") {
				$(baseInfo.edModelDivId).modal({
					backdrop : 'static'
				});
				detailVue.errMsg = jsonObj.info;
			} else {
				window.open(jsonObj.pdfUrl,"_blank");
				/*window.location.href = jsonObj.pdfUrl;*/
			}
		});
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
                generalErrorModal(jsonObj);
			}
			else
			{
                detailVue.tgpf_FundAppropriated_AFModel = jsonObj.tgpf_FundAppropriated_AF;
                detailVue.tgpf_fundAppropriated_afDtltab = jsonObj.tgpf_fundAppropriated_afDtlList;
                detailVue.tgpf_fundAppropriatedtab = jsonObj.tgpf_fundAppropriatedList;
                
                /*
                 * 判断申请状态
                 */
                var applyState = detailVue.tgpf_FundAppropriated_AFModel.applyState;
                if(applyState == '6')
                {
                	detailVue.isPrint = true;
                }
            
                if(detailVue.tgpf_fundAppropriatedtab[0].approvalState == null || detailVue.tgpf_fundAppropriatedtab[0].approvalState == "" || detailVue.tgpf_fundAppropriatedtab[0].approvalState == "待提交" )
				{
					detailVue.disabled = false;
				}
				else
				{
					detailVue.disabled = true;
					detailVue.showDownFlag = false;
				}
                Vue.nextTick(function () {
                	if(detailVue.tgpf_FundAppropriated_AFModel.applyState == 4)
					{
						$('.payoutDateTime').val(detailVue.currentdate);
                        for (var i = 0; i < detailVue.tgpf_fundAppropriatedtab.length; i++)
                        {
                            laydate.render({
                                elem: '#date0612030302'+i,
                            });
                        }
					}
					else
					{
                        for (var i = 0; i < detailVue.tgpf_fundAppropriatedtab.length; i++)
                        {
                            laydate.render({
                                elem: '#date0612030302'+i,
                            });
                            $('#date0612030302'+i).val(detailVue.tgpf_fundAppropriatedtab[i].actualPayoutDate);
                        }
					}
				})
			}
		});
	}

    function getUpdateForm()
    {
        return {
            interfaceVersion:detailVue.interfaceVersion,
            buttonType:detailVue.buttonType,
            fundAppropriated_AFId:detailVue.tableId,
			remark:detailVue.tgpf_FundAppropriated_AFModel.remark,
            fundAppropriated_AFId:detailVue.tableId, //用款申请id
            tgpf_FundAppropriatedList:detailVue.tgpf_fundAppropriatedtab // 资金拨付
        }
    }

	function updateTgpf_FundAppropriated(buttonType)
	{
		detailVue.flag = false;
		
        for (var i = 0; i < detailVue.tgpf_fundAppropriatedtab.length; i++)
        {
            detailVue.tgpf_fundAppropriatedtab[i].actualPayoutDate = $('#date0612030302'+i).val();
            detailVue.tgpf_fundAppropriatedtab[i].overallPlanPayoutAmount =  commafyback(detailVue.tgpf_fundAppropriatedtab[i].overallPlanPayoutAmount);
            detailVue.tgpf_fundAppropriatedtab[i].currentPayoutAmount = commafyback(detailVue.tgpf_fundAppropriatedtab[i].currentPayoutAmount);
            detailVue.tgpf_fundAppropriatedtab[i].canPayAmount = commafyback(detailVue.tgpf_fundAppropriatedtab[i].canPayAmount);
        }

		if(buttonType == 1)
		{
            detailVue.buttonType = 1;
		}else if(buttonType == 2)
		{
            detailVue.buttonType = 2;
            detailVue.showDownFlag = false;
		}
        new ServerInterfaceJsonBody(baseInfo.updateInterface).execute(detailVue.getUpdateForm(), function(jsonObj)
		{
        	detailVue.flag = true;
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

	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tgpf_FundAppropriatedDetailDiv",
	"detailInterface":"../Tgpf_FundAppropriatedDetail",
    "updateInterface":"../Tgpf_FundAppropriatedUpdate",
    "exportInterface":"../exportPDFByWord",//导出pdf
});
