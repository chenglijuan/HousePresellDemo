(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			sm_AttachmentCfgModel: {},
			tableId : 1,
			Sm_AttachmentCfgDetailList : [],
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			busiTypeFather :"",
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			changePageNumber : function(data){
				//addVue.pageNumber = data;
				if (detailVue.pageNumber != data) {
					detailVue.pageNumber = data;
					detailVue.refresh();
				}
			},
			changeCountPerPage:function(data){
				if (detailVue.countPerPage != data) {
					detailVue.countPerPage = data;
					detailVue.refresh();
				}
			},
			editAttachmentCfgHandle : editAttachmentCfgHandle,//点击编辑
			indexMethod : indexMethod,
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue
		},
		watch:{
//			pageNumber : refresh,
		}
	});

	//------------------------方法定义-开始------------------//
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:this.tableId,
			pageNumber : this.pageNumber,
			countPerPage : this.countPerPage,
			totalPage : this.totalPage,
			totalCount : this.totalCount,
		}
	}
	function indexMethod(index) {
		return generalIndexMethod(index, detailVue)
	}
	//详情操作--------------
	function refresh()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var array = tableIdStr.split("_");
		detailVue.tableId = array[2];
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
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				//detailVue.sm_AttachmentCfgModel = jsonObj.sm_AttachmentCfg;
				detailVue.sm_AttachmentCfgModel = jsonObj.sm_BaseParameter;
				detailVue.Sm_AttachmentCfgDetailList = jsonObj.sm_AttachmentCfgList;
				detailVue.busiTypeFather = jsonObj.sm_BaseParameter.theValue+"-"+jsonObj.sm_BaseParameter.theName;
				detailVue.pageNumber=jsonObj.pageNumber;
				detailVue.countPerPage=jsonObj.countPerPage;
				detailVue.totalPage=jsonObj.totalPage;
				detailVue.totalCount = jsonObj.totalCount;
			}
		});
	}
	function editAttachmentCfgHandle(){
		enterNext2Tab(detailVue.tableId, '附件类型修改', 'sm/Sm_AttachmentCfgEdit.shtml',detailVue.tableId+"010201");
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
	"detailDivId":"#sm_AttachmentCfgDiv",
	"detailInterface":"../Sm_AttachmentCfgToYw"
});
