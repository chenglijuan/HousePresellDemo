(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
            errMsg :"",//存放错误提示信息
            empj_PaymentBondDetailModel : {},
            loadUploadList : [],
            showDelete : false,
            busiType : "",
            tableId:"1",
            empj_PaymentBondChildList : [],
            qs_companyNameList : [], //页面加载显示开发企业
			qs_projectNameList : [], //显示项目名称
			guaranteeCompanyList : [],//
			subDisabled : false,
            busiType: "06120501",
            subBox : true,
            showSubFlag : true,
          //分页
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			
			//----------审批流start-----------//
            afId:"",//申请单Id
            workflowId:"",//结点Id
            isNeedBackup:"",//是否需要备案
            sourcePage:"",//来源页面
            //----------审批流end-----------//
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			indexMethod : indexMethod,
			editHandle : editHandle,
			loadCompanyName : loadCompanyNameFun, //页面加载显示开发企业方法
			changeCompanyHandle : changeCompanyHandleFun,//改变项目名称
			loadGuaranteeCompany: loadGuaranteeCompany,
			
			 //其他
            showModal : showModal,
            approvalHandle : function ()
            {
                approvalModalVue.buttonType = 2;
                approvalModalVue.approvalHandle();
            },
            exportFile : exportFile,
            getSummaries : getSummaries,//合计
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
		
		return{
			interfaceVersion:this.interfaceVersion,
			tableId : this.tableId,
			busiCode : "06120501", //业务编码
			buttonType:3,//详情按钮
		}
	}
	//列表操作-----------------------页面加载显示开发企业
		function loadCompanyNameFun() {
			loadCompanyName(detailVue,baseInfo.companyNameInterface,function(jsonObj){
				detailVue.qs_companyNameList = jsonObj.emmp_CompanyInfoList;
			},detailVue.errMsg,baseInfo.edModelDivId);
		}
		//列表操作--------------------改变开发企业的方法
		function changeCompanyHandleFun() {
			var model ={
					interfaceVersion : this.interfaceVersion,
					developCompanyId : detailVue.empj_PaymentBondDetailModel.companyId,
			}
			new ServerInterface(baseInfo.changeCompanyInterface).execute(model, function(jsonObj)
				{
					if(jsonObj.result != "success")
					{
						generalErrorModal(jsonObj,jsonObj.info);
					}
					else
					{
						detailVue.qs_projectNameList= jsonObj.empj_ProjectInfoList;
					}
				});
		}
		//新增操作-----------------------支付保证出具单位
		function loadGuaranteeCompany(){
			var model ={
					interfaceVersion : this.interfaceVersion,	
			}
			new ServerInterface(baseInfo.companyNameInterface).execute(model, function(jsonObj)
			{
				if(jsonObj.result != "success")
				{
					generalErrorModal(jsonObj,jsonObj.info);
				}
				else
				{
					detailVue.guaranteeCompanyList = jsonObj.emmp_CompanyInfoList;
				}
			});
		}
	//详情操作--------------
	function refresh()
	{
		new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				detailVue.empj_PaymentBondDetailModel = jsonObj.empj_PaymentBond;
				detailVue.empj_PaymentBondDetailModel.alreadyActualAmount = addThousands(detailVue.empj_PaymentBondDetailModel.alreadyActualAmount);
				detailVue.empj_PaymentBondDetailModel.actualAmount = addThousands(detailVue.empj_PaymentBondDetailModel.actualAmount);
				detailVue.empj_PaymentBondDetailModel.guaranteedAmount = addThousands(detailVue.empj_PaymentBondDetailModel.guaranteedAmount);
				detailVue.empj_PaymentBondDetailModel.guaranteedSumAmount = addThousands(detailVue.empj_PaymentBondDetailModel.guaranteedSumAmount);
				//detailVue.empj_PaymentBondDetailModel.companyName = jsonObj.empj_PaymentGuarantee.companyName;
				detailVue.empj_PaymentBondChildList = jsonObj.empj_PaymentBondChildList;
				detailVue.empj_PaymentBondChildList.forEach(function(item){
					item.orgLimitedAmount1 = item.orgLimitedAmount;
					item.nodeLimitedAmount1 = item.nodeLimitedAmount;
					item.currentEscrowFund1 = item.currentEscrowFund;
					item.spilloverAmount1 = item.spilloverAmount;
					item.controlAmount1 = item.controlAmount;
					item.releaseAmount1 = item.releaseAmount>0?item.releaseAmount:0;
					item.paymentBondAmount1 = item.paymentBondAmount;
					item.effectiveLimitedAmount1 = item.effectiveLimitedAmount;
					item.actualReleaseAmount1 = item.actualReleaseAmount>0?item.actualReleaseAmount:0;
					item.canApplyAmount1 = item.canApplyAmount>0?item.canApplyAmount:0;
					item.afterCashLimitedAmount1 = item.afterCashLimitedAmount>0?item.afterCashLimitedAmount:0;
					item.afterEffectiveLimitedAmount1 = item.afterEffectiveLimitedAmount;
					
					item.orgLimitedAmount = addThousands(item.orgLimitedAmount);
					item.nodeLimitedAmount = addThousands(item.nodeLimitedAmount);
					item.currentEscrowFund = addThousands(item.currentEscrowFund);
					item.spilloverAmount = addThousands(item.spilloverAmount);
					item.controlAmount = addThousands(item.controlAmount);
					item.releaseAmount = addThousands(item.releaseAmount);
					item.paymentBondAmount = addThousands(item.paymentBondAmount);
					item.effectiveLimitedAmount = addThousands(item.effectiveLimitedAmount);
					
					item.actualReleaseAmount = addThousands(item.actualReleaseAmount);
					item.afterCashLimitedAmount = addThousands(item.afterCashLimitedAmount);
					item.afterEffectiveLimitedAmount = addThousands(item.afterEffectiveLimitedAmount);
					
					item.canApplyAmount = addThousands(item.canApplyAmount);
				});
				if(detailVue.empj_PaymentBondDetailModel.busiState == "0"){
					detailVue.subBox = true;
				}else{
					detailVue.subBox = false;
				}
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
				detailVue.changeCompanyHandle();
				detailVue.loadGuaranteeCompany();
				
				
				 if(detailVue.empj_PaymentBondDetailModel.busiState == '未备案')
                {
                    detailVue.approvalCode = '020103';
                }
                detailVue.isNeedBackup = jsonObj.isNeedBackup;
                approvalModalVue.isNeedBackup = detailVue.isNeedBackup;
			}
			
		});
	}
	function indexMethod(index){
		return generalIndexMethod(index,detailVue);
	}
	//详情操作--------------------------点击编辑
	function editHandle(){
        /*var theTableId = 'jump_' + detailVue.tableId;
        $("#tabContainer").data("tabs").addTab({id: theTableId , text: '修改支付保证申请', closeable: true, url: 'empj/Empj_PaymentGuaranteeApplicationEdit.shtml'});*/
        enterNext2Tab(detailVue.tableId, '修改支付保函申请', 'empj/Empj_PaymentBondEdit.shtml',detailVue.tableId+"06120501");
	}
	function initData()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
        var array = tableIdStr.split("_");
        if (array.length > 1)
        {
            detailVue.tableId = array[array.length-4];
            detailVue.afId = array[array.length-3];
            detailVue.workflowId = array[array.length-2];
            detailVue.sourcePage = array[array.length-1];

            approvalModalVue.afId = detailVue.afId;
            approvalModalVue.workflowId = detailVue.workflowId;
            approvalModalVue.sourcePage = detailVue.sourcePage;
            refresh();
        }
		
	}
	//合计
	function getSummaries(param){
		var columns = param.columns;
        var data = param.data;
        var sums = [];
        
        for (var index = 0; index < columns.length; index++)
        {
        	if(index === 0)
            {
                sums[index] = '总计';
                continue;
            }
        	sums[index] = 0.0; 
        	if(index === 1 || index === 2 || index === 6 || index === 5){
        		sums[index] = ""; 
        	}else{
        		for (var i = 0; i < data.length; i++)
        		{
        			var row = data[i];
        			var value = 0.00;
        			switch (index)
        			{
        				
        				case 3 :
        					value = row["effectiveLimitedAmount1"];
        					break;
	        			case 4 :
	        				value = row["orgLimitedAmount1"];
	        				break;
	        			case 7 :
	        				value = row["nodeLimitedAmount1"];
	        				break;
	        			/*case 8 :
	        				value = row["nodeLimitedAmount1"];
	        				break;*/
	        			case 8 :
	        				value = row["currentEscrowFund1"];
	        				break;
	        			case 9 :
	        				value = row["spilloverAmount1"];
	        				break;
	        			case 10 :
	        				value = row["controlAmount1"];
	        				break;
	        			case 11 :
	        				value = row["releaseAmount1"];
	        				break;
	        			case 12 :
	        				value = row["paymentBondAmount1"];
	        				break;
	        			case 13 :
	        				value = row["actualReleaseAmount1"];
	        				break;
	        			case 14 :
	        				value = row["canApplyAmount1"];
	        				break;
	        			case 15 :
	        				value = row["afterCashLimitedAmount1"];
	        				break;
	        			case 16 :
	        				value = row["afterEffectiveLimitedAmount1"];
	        				break;
        			}
        			if(!isNaN(value))
        			{
        				sums[index] += (value - 0);
        			}
        		}
        		switch (index)
        		{
	        		case 3 :
	        			sums[index] = thousandsToTwoDecimal(sums[index]);
	        			break;
	        		case 4 :
	        			sums[index] = thousandsToTwoDecimal(sums[index]);
	        			break;
	        		case 7 :
	        			sums[index] = thousandsToTwoDecimal(sums[index]);
	        			break;
	        		case 8 :
	        			sums[index] = thousandsToTwoDecimal(sums[index]);
	        			break;
	        		case 9 :
	        			sums[index] = thousandsToTwoDecimal(sums[index]);
	        			break;
	        		case 10 :
	        			sums[index] = thousandsToTwoDecimal(sums[index]);
	        			break;
	        		case 11 :
	        			sums[index] = thousandsToTwoDecimal(sums[index]);
	        			break;
	        		case 12 :
	        			sums[index] = thousandsToTwoDecimal(sums[index]);
	        			break;
	        		case 13 :
	        			sums[index] = thousandsToTwoDecimal(sums[index]);
	        			break;
	        		case 14 :
	        			sums[index] = thousandsToTwoDecimal(sums[index]);
	        			break;
	        		case 15 :
	        			sums[index] = thousandsToTwoDecimal(sums[index]);
	        			break;
	        		case 16 :
	        			sums[index] = thousandsToTwoDecimal(sums[index]);
	        			break;
	        		/*case 17 :
	        			sums[index] = thousandsToTwoDecimal(sums[index]);
	        			break;*/
        		}
        	}
        	    	
            
        }
        return sums;
	}
	
	function showModal()
    {
        approvalModalVue.getModalWorkflowList();
    }
	//打印
	function exportFile(){
		var model ={
			interfaceVersion : this.interfaceVersion,
			sourceId : this.tableId,
			reqAddress : window.location.href,
			sourceBusiCode : '06120501',
		}
		new ServerInterface(baseInfo.exportInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				window.open(jsonObj.pdfUrl,"_blank");
			}
		});
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.loadCompanyName();
	detailVue.initData();

	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#Sm_ApprovalProcess_PaymentBondDetail",
	"edModelDivId":"#edModelPaymentGuaranteeApplicationDetail",
	"sdModelDivId":"#sdModelPaymentGuaranteeApplicationDetail",
	"detailInterface":"../Empj_PaymentBondDetail",
	"loadUploadInterface":"../Sm_AttachmentCfgList",
	"subInterface" : "../Empj_PaymentBondSubmit",
	"companyNameInterface" : "../Emmp_CompanyInfoList", //显示开发企业名称接口
	"changeCompanyInterface" : "../Empj_ProjectInfoList", //改变企业名称接口
	"queryInterface":"../Tgpf_RefundInfoAddQuery",
	"addInterface":"../Tgpf_RefundInfoAdd",
	"bankInterface":"../Tgxy_BankAccountEscrowedList",
	"refundBankAccountInterface":"../Tgxy_BankAccountEscrowedDetail",
	"loadUploadInterface":"../Sm_AttachmentCfgList",
    "exportInterface":"../exportPDFByWord",//导出pdf
});
