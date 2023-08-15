(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tgpf_CyberBankStatementModel: {},
			tableId : 1,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			selectItem : [],
			isAllSelected : false,
			tgpf_CyberBankStatementDetail:[],//初始化详情页列表
			errMsg:""
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			cyberBankStatementEditHandle : cyberBankStatementEditHandle//修改方法
		},
		computed:{
			 
		},
		components : {

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
		}
	}

	//详情操作--------------
	function refresh()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var array = tableIdStr.split("_");
		detailVue.tableId = array[2];
		
		if (this.tableId == null || this.tableId < 1) 
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
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				detailVue.errMsg = jsonObj.info;
			}
			else
			{
				detailVue.tgpf_CyberBankStatementModel = jsonObj.tgpf_CyberBankStatement;
				detailVue.tgpf_CyberBankStatementDetail = jsonObj.tgpf_CyberBankStatementDtlList;
				detailVue.tgpf_CyberBankStatementDetail.forEach(function(item){
					item.income = addThousands(item.income);
				})
			}
		});
	}
	//修改方法
	function cyberBankStatementEditHandle(){
		enterNext2Tab(detailVue.tableId, '网银上传详情修改', 'tgpf/Tgpf_CyberBankStatementEdit.shtml',detailVue.tableId+"200201");
		/*$("#tabContainer").data("tabs").addTab({
			id: detailVue.tableId , 
			text: '网银上传详情修改', 
			closeable: true, 
			url: 'tgpf/Tgpf_CyberBankStatementEdit.shtml'
		});*/
	}
	function initData()
	{
		
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tgpf_CyberBankStatementDetailDiv",
	"edModelDivId":"#edModelCyberBankStatementDetail",
	"detailInterface":"../Tgpf_CyberBankStatementDetail",
});
