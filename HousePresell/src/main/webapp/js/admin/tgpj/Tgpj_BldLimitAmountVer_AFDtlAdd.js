(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			tgpj_BldLimitAmountVer_AFDtlModel: {},
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			//添加
			getAddForm : getAddForm,
			add: add,
		},
		computed:{
			 
		},
		components : {

		"my-uploadcomponent":fileUpload,
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
		}
	}

	//详情操作--------------
	function refresh()
	{

	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getAddForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			theState:this.theState,
			bldLimitAmountVerMngId:this.bldLimitAmountVerMngId,
			stageName:this.stageName,
			limitedAmount:this.limitedAmount,
	            //附件材料
			busiType:'XX',
            generalAttachmentList : this.$refs.listenUploadData.uploadData	}
	}

	function add()
	{
		new ServerInterface(baseInfo.addInterface).execute(detailVue.getAddForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				// noty({"text":jsonObj.info,"type":"error","timeout":2000});
				generalErrorModal(jsonObj)
			}
			else
			{
				generalSuccessModal()
				refresh();
			}
		});
	}
	
	function initData()
	{
		generalLoadFile2(detailVue,'XXXXXX')

		
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#tgpj_BldLimitAmountVer_AFDtlDiv",
	"detailInterface":"../Tgpj_BldLimitAmountVer_AFDtlDetail",
	"addInterface":"../Tgpj_BldLimitAmountVer_AFDtlAdd"
});
