(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tgpf_BasicAccountVoucherDetailModel: {},
			tableId : 1,
			splitBasicAccountList : [],
			
			billTimeStamp :"",
			remark : "",
			totalTradeAmount : "",
			selectItem : [],
			idArr : [],
			editState : true,
			delState :true,
			editFlag : false,//false 新增，true 修改
			subCode : ""
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
		    splitBasicAccount : splitBasicAccount,//拆分
		    saveSplitBasicAccount : saveSplitBasicAccount,//新增拆分 
		    editSplitBasicAccount : editSplitBasicAccount,//修改
		    delSplitBasicAccount : delSplitBasicAccount,//删除
		    listItemSelectHandle: listItemSelectHandle,
		},
		computed:{
			 
		},
		components : {

		},
		watch:{
			
		},
		mounted: function ()
        {
            laydate.render({
                elem: '#dateBasciAccountVoucherDetail',
                done:function(value){
                	detailVue.billTimeStamp = value;
                }
            })
        }
	});

	//------------------------方法定义-开始------------------//
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:detailVue.tableId,
		}
	}
	
	function refresh() {
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var list = tableIdStr.split("_");
		tableIdStr = list[2];
		detailVue.tableId = tableIdStr;
		if (detailVue.tableId == null || detailVue.tableId < 1) {
			return;
		}

		getDetail();
		splitBasicAccountListHandle();
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
				detailVue.tgpf_BasicAccountVoucherDetailModel = jsonObj.tgpf_BasicAccountVoucherDetail;
				detailVue.tgpf_BasicAccountVoucherDetailModel.totalTradeAmount = addThousands(detailVue.tgpf_BasicAccountVoucherDetailModel.totalTradeAmount);
				
//				detailVue.tableId = tgpf_BasicAccountVoucherDetailModel.tableId;
				
			}
		});
	}
	
	function initData()
	{
		
	}
	
	function listItemSelectHandle(list)
    {
    	detailVue.selectItem  = list;
    	if(list.length == 1){
    		if(list[0].sendState == '0'){
    			detailVue.editState = false;
    		}else{
    			detailVue.editState = true;
    		}
    	}else{
    		detailVue.editState = true;
		}
    	if(list.length !=0){
    		for(var i = 0;i<list.length;i++){
    			if(list[i].sendState == '1'){
    				detailVue.delState = true;
    				return;
    			}else{
    				detailVue.delState = false;
    			}
    			
    		}
    	}else{
    		detailVue.delState = true;
    	}
    	
    	detailVue.idArr = [];
    	list.forEach(function(item){
    		detailVue.idArr.push(item.tableId);
    	});
    	
    	console.log(detailVue.idArr);
    }
	
	/**
	 * 拆分列表显示
	 * @returns
	 */
	function splitBasicAccountListHandle(){
		var model = {
			interfaceVersion:detailVue.interfaceVersion,
			tableId:detailVue.tableId,
		}
		new ServerInterface(baseInfo.splitInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				detailVue.splitBasicAccountList = jsonObj.tgpf_BasicAccountVoucherDtlList;
				detailVue.splitBasicAccountList.forEach(function(item){
					item.totalTradeAmount = addThousands(item.totalTradeAmount)
				})
			}
		});
	}
	
	
	
	/**
	 * 拆分
	 * @returns
	 */
	function splitBasicAccount(){
		this.editFlag = false;
		$("#splitBasicAccount").modal({
			backdrop :'static'
		})
		this.billTimeStamp = detailVue.tgpf_BasicAccountVoucherDetailModel.billTimeStamp;
		this.remark = detailVue.tgpf_BasicAccountVoucherDetailModel.remark;
		this.subCode = '21710101';
		this.totalTradeAmount = null;
	}
	/***
	 * 新增拆分
	 * @returns
	 */
	function saveSplitBasicAccount(){
		var payMoney = null,stayMoney=null;
		detailVue.splitBasicAccountList.forEach(function(item){
			payMoney += Number(commafyback(item.totalTradeAmount))
		})
		var totalTradeAmount = Number(commafyback(detailVue.tgpf_BasicAccountVoucherDetailModel.totalTradeAmount));
		stayMoney = totalTradeAmount - payMoney;
		if(this.editFlag){
			
			//修改
			/*if(stayMoney - Number(detailVue.totalTradeAmount)<0){
				generalErrorModal('','本次拆分金额超出总金额！');
			}else{
				
			}*/
			var model = {
					interfaceVersion:this.interfaceVersion,
					tableId:detailVue.idArr[0],
					billTimeStamp : detailVue.billTimeStamp,
					remark : detailVue.remark,
					totalTradeAmount : detailVue.totalTradeAmount,
					subCode : detailVue.subCode
			}
			new ServerInterface(baseInfo.editInterface).execute(model, function(jsonObj)
			{
				if(jsonObj.result != "success")
				{
					generalErrorModal(jsonObj);
				}
				else
				{
					splitBasicAccountListHandle();
					$("#splitBasicAccount").modal('hide');
					detailVue.refresh();
				}
			});
		}else{
			/*if(stayMoney - Number(detailVue.totalTradeAmount)<0){
				generalErrorModal('','本次拆分金额超出总金额！');
			}else{
				
				
			}*/
			//新增
			var model = {
					interfaceVersion:this.interfaceVersion,
					tableId:detailVue.tableId,
					billTimeStamp : detailVue.billTimeStamp,
					remark : detailVue.remark,
					totalTradeAmount : detailVue.totalTradeAmount,
					subCode : detailVue.subCode
			}
			new ServerInterface(baseInfo.addInterface).execute(model, function(jsonObj)
			{
				if(jsonObj.result != "success")
				{
					generalErrorModal(jsonObj);
				}
				else
				{
					splitBasicAccountListHandle();
					$("#splitBasicAccount").modal('hide');
					detailVue.refresh();
				}
			});
		}
	}
	/**
	 * 修改拆分
	 * @returns
	 */
	function editSplitBasicAccount(){
		this.editFlag = true;
		$("#splitBasicAccount").modal({
			backdrop :'static'
		})
		this.billTimeStamp = this.selectItem[0].billTimeStamp;
		this.remark = this.selectItem[0].remark;
		this.subCode = this.selectItem[0].subCode;
		this.totalTradeAmount = commafyback(this.selectItem[0].totalTradeAmount);
	}
	/**
	 * 删除拆分
	 * @returns
	 */
	function delSplitBasicAccount(){
		generalSelectModal(delSplitBasicAccountById,"确认删除吗？");
	}
	
	function delSplitBasicAccountById(){
		var model = {
			interfaceVersion:detailVue.interfaceVersion,
			idArr : detailVue.idArr,
		}
		new ServerInterface(baseInfo.delInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				splitBasicAccountListHandle();
				detailVue.refresh();
			}
		});
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tgpf_BasicAccountVoucherDetailDiv",
	"detailInterface":"../Tgpf_BasicAccountVoucherDetail",
	"splitInterface" : "../Tgpf_BasicAccountVoucherDtlList",
	"addInterface" :"../Tgpf_BasicAccountVoucherDtlSave",
	"editInterface" : "../Tgpf_BasicAccountVoucherDtlUpdate",
	"delInterface" : "../Tgpf_BasicAccountVoucherDtlDelete",
});
