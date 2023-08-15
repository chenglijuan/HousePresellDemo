(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tg_PjRiskLetterModel: {},
			tableId : 1,
			tg_PjRiskLetterDetailList: [],
			selectItem : [],
			isAllSelected : false,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			// 附件上传相关参数
			smAttachmentList : [], //页面显示已上传的文件
			loadUploadList : [],
			showDelete : false,
			busiType : '21020304',
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			goToEditHandle: goToEditHandle,
			add: add,
			listItemSelectHandle: listItemSelectHandle,
			indexMethod: indexMethod,
			getExportForm : getExportForm,
			exportPdf : exportPdf,
			changePageNumber: function (data) {
				console.log(data);
				if (detailVue.pageNumber != data) {
					detailVue.pageNumber = data;
					detailVue.refresh();
				}
			},
			changeCountPerPage : function (data) {
				console.log(data);
				if (detailVue.countPerPage != data) {
					detailVue.countPerPage = data;
					detailVue.refresh();
				}
			},
			sendHandle: sendHandle,
			handleSendMail : handleSendMail
		},
		computed:{
			 
		},
		components : {
			"my-uploadcomponent" : fileUpload
		},
		watch:{
			selectItem : selectedItemChanged,
		}
	});

	//------------------------方法定义-开始------------------//
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:detailVue.tableId,
			busiCode: detailVue.busiType,
		}
	}
	function indexMethod(index) {
		return generalIndexMethod(index, detailVue)
	}
	function listItemSelectHandle(list) {
		generalListItemSelectHandle(detailVue,list)
 	}
	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		detailVue.isAllSelected = (detailVue.tg_PjRiskLetterDetailList.length > 0)
		&&	(detailVue.selectItem.length == detailVue.tg_PjRiskLetterDetailList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(detailVue.selectItem.length == detailVue.tg_PjRiskLetterDetailList.length)
	    {
	    	detailVue.selectItem = [];
	    }
	    else
	    {
	    	detailVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	detailVue.tg_PjRiskLetterDetailList.forEach(function(item) {
	    		detailVue.selectItem.push(item.tableId);
	    	});
	    }
	}
	
	function add()
	{
		
	}
	
	function goToEditHandle()
	{
		enterNext2Tab(detailVue.tableId, '项目风险函修改', 'tg/Tg_PjRiskLetterEdit.shtml',detailVue.tableId + "21020304");
	}

	//详情操作--------------
	function refresh()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var list = tableIdStr.split("_");
		tableIdStr = list[2];
		detailVue.tableId = tableIdStr;
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
				generalErrorModal(jsonObj);
			}
			else
			{
				detailVue.tg_PjRiskLetterModel = jsonObj.tg_PjRiskLetter;
				detailVue.tg_PjRiskLetterDetailList = jsonObj.tg_PjRiskLetterReceiverList;
				detailVue.smAttachmentList = jsonObj.sm_AttachmentList;
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
			}
		});
	}
	
	function sendHandle()
	{
		var model = {
				interfaceVersion:this.interfaceVersion,
				tableId: detailVue.tableId
		}
		new ServerInterface("../Tg_PjRiskLetterPush").execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				generalSuccessModal();
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
			sourceBusiCode : this.busiType
		}
	}

	function exportPdf()
	{
		/*var model = {
				interfaceVersion:this.interfaceVersion,
				tableId: detailVue.tableId
		}
		new ServerInterface("../Tg_PjRiskLetterPrint").execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				generalSuccessModal();
				window.open("../"+jsonObj.url);
			}
		});*/
		new ServerInterface(baseInfo.exportInterface).execute(detailVue.getExportForm(), function(jsonObj){
			if (jsonObj.result != "success") {
				$(baseInfo.edModelDivId).modal({
					backdrop : 'static'
				});
				detailVue.errMsg = jsonObj.info;
			} else {
				window.open(jsonObj.pdfUrl,"_blank");
				
				/*setTimeout(function(){
				aa.location.href=jsonObj.pdfUrl;
				}, 100);*/
			//	 window.open();
			}
		});
	}
	
	function handleSendMail() {
		var model = {
			interfaceVersion:this.interfaceVersion,
			tableId: detailVue.tableId
		}
		new ServerInterface("../Tg_PjRiskLetterSendMail").execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				generalSuccessModal();
				detailVue.refresh();
			}
		});
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
	"detailDivId":"#tg_PjRiskLetterDiv",
	"detailInterface":"../Tg_PjRiskLetterDetail",
	"exportInterface":"../exportPDFByWord",//导出pdf
});
