(function(baseInfo){
	var editVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			sm_AttachmentCfgModel: {},
			tableId : 1,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			selectItem : [],
			editDisabled : true,
			delDisabled : true,
			Sm_AttachmentCfgEditList : [],
			checkList : [],
			checkItem : [],
			busTypeList : [],
			theName : "",//附件类型名称
			acceptFileCount : "",//可接收文件数量
			minFileSize : "",
			maxFileSize : "",//单个文件大小显示
			remark : "",//备注
			isImage : "1",//是否是图片
			isNeeded : "1",//是否必须上传
			listType : "text",//附件列表类型
            busiType : "",//业务类型		
            busiTypeFather : "",
            theNameId : "",
            errMsg : "",
            isCfgSignature : "0",//是否需要签章
            isImageList:[
            	{tableId:"1",theName:"是"},
            	{tableId:"0",theName:"否"}
            ],
            isNeededList : [
            	{tableId:"1",theName:"是"},
            	{tableId:"0",theName:"否"}
            ],
            listTypeList : [
            	{tableId:"text",theName:"文本"},
            	{tableId:"picture-card",theName:"图片"}
            ],
            isCfgSignatureList : [
            	{tableId:"1",theName:"是"},
            	{tableId:"0",theName:"否"}
            ],
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			//更新
			getUpdateForm : getUpdateForm,
			update: update,
			addFileHadle : addFileHadle,//新增附件信息
			editFileHandle : editFileHandle,//附件修改
			delFileHandle : delFileHandle,
			saveFileType : saveFileType,//点击确定按钮
			checkAllClicked : checkAllClicked,
			listItemSelectHandle : listItemSelectHandle,
			changePageNumber : function(data){
				//editVue.pageNumber = data;
				if (editVue.pageNumber != data) {
					editVue.pageNumber = data;
					editVue.refresh();
				}
			},
			changeCountPerPage:function(data){
				if (editVue.countPerPage != data) {
					editVue.countPerPage = data;
					editVue.refresh();
				}
			},
			showDelModel : showDelModel,
			indexMethod : indexMethod,
			isImageChange : function(data){
				editVue.isImage = data.tableId;
			},
			emptyIsImage : function(){
				editVue.isImage = null;	
			},
			isNeededChange : function(data){
				editVue.isNeeded = data.tableId;
			},
			emptyIsNeeded : function(){
				editVue.isNeeded = null;
			},
			listTypeChange : function(data){
				editVue.listType = data.tableId;
			},
			emptyListType : function(){
				editVue.listType = null;
			},
			isCfgSignatureChange : function(data){
				editVue.isCfgSignature = data.tableId;
			},
			emptyIsCfgSignature : function(data){
				editVue.isCfgSignature = null;
			}
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue,
			'vue-select': VueSelect ,
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
		editVue.tableId = array[2];
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
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				editVue.sm_AttachmentCfgModel = jsonObj.sm_BaseParameter;
				editVue.Sm_AttachmentCfgEditList = jsonObj.sm_AttachmentCfgList;
				editVue.busiTypeFather = jsonObj.sm_BaseParameter.theValue+"-"+jsonObj.sm_BaseParameter.theName;
				editVue.theNameId = jsonObj.sm_BaseParameter.theName;
				editVue.pageNumber=jsonObj.pageNumber;
				editVue.countPerPage=jsonObj.countPerPage;
				editVue.totalPage=jsonObj.totalPage;
				editVue.totalCount = jsonObj.totalCount;
			}
		});
	}
	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		editVue.isAllSelected = (editVue.Sm_AttachmentCfgEditList.length > 0)
		&&	(editVue.selectItem.length == editVue.Sm_AttachmentCfgEditList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(editVue.selectItem.length == editVue.Sm_AttachmentCfgEditList.length)
	    {
	    	editVue.selectItem = [];
	    }
	    else
	    {
	    	editVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	editVue.Sm_AttachmentCfgEditList.forEach(function(item) {
	    		editVue.selectItem.push(item.tableId);
	    	});
	    }
	}
	function indexMethod(index) {
		return generalIndexMethod(index, editVue)
	}

	function listItemSelectHandle(list) {
		if(list.length == "1"){
			editVue.editDisabled = false;
		}else{
			editVue.editDisabled = true;
		}
		if(list.length >= "1"){
			editVue.delDisabled = false;
		}else{
			editVue.delDisabled = true;
		}
		generalListItemSelectHandle(editVue,list);
 	}
	
	function clearModel(){
		editVue.theName = "";
		editVue.acceptFileCount = "";
		editVue.fileSize = "";
		editVue.remark = "";
		editVue.minFileSize = "";
		editVue.maxFileSize = "";
		editVue.isImage = "1";//是否是图片
		editVue.isNeeded = "1";//是否必须上传
		editVue.listType = "text";//附件列表类型
		editVue.checkList = [];
	}
	
	
	//新增操作-------------------点击新增按钮
	function addFileHadle(){
		editVue.selectItem = [];
		$(baseInfo.addModelDivId).modal({
			backdrop:"static"
		})	
		clearModel();
	}
	//编辑附件类型
	function editFileHandle(){
		var model = {
			interfaceVersion:this.interfaceVersion,
			tableId : this.selectItem[0]
		}
		new ServerInterface(baseInfo.editFileInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				clearModel();
				$(baseInfo.addModelDivId).modal({
					backdrop:"static"
				})
				editVue.theName = jsonObj.Sm_AttachmentCfg.theName;
				editVue.acceptFileCount = jsonObj.Sm_AttachmentCfg.acceptFileCount;
				editVue.fileSize = jsonObj.Sm_AttachmentCfg.fileSize;
				editVue.remark = jsonObj.Sm_AttachmentCfg.remark;
				editVue.minFileSize = jsonObj.Sm_AttachmentCfg.minFileSize;
				editVue.maxFileSize = jsonObj.Sm_AttachmentCfg.maxFileSize;
				var isImg,isNeed;
				if(jsonObj.Sm_AttachmentCfg.isImage){
					isImg = "1";
				}else{
					isImg = "0";
				}
				if(jsonObj.Sm_AttachmentCfg.isNeeded){
					isNeed = "1";
				}else{
					isNeed = "0";
				}
				editVue.isImage = isImg;//是否是图片
				editVue.isNeeded = isNeed;//是否必须上传
				editVue.listType = jsonObj.Sm_AttachmentCfg.listType;//附件列表类型
				var arr = jsonObj.Sm_AttachmentCfg.acceptFileType.split(",");
				editVue.checkList = arr;
				editVue.isCfgSignature = jsonObj.Sm_AttachmentCfg.isCfgSignature;
				//refresh();
				
			}
		});
	}
	
	function showDelModel(){
		$(baseInfo.deleteDivId).modal({
			backdrop:"static"
		})
	}
	
	//新增操作---------------------删除附件
	function delFileHandle(){
		var model = {
			interfaceVersion:this.interfaceVersion,
			idArr : this.selectItem
		}
		new ServerInterface(baseInfo.delFileInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				$(baseInfo.deleteDivId).modal("hide");
				refresh();
			}
		});
	}
	//新增操作---------------------点击确定按钮
	function saveFileType(){
		var model = {
			interfaceVersion:editVue.interfaceVersion,
			theName : editVue.theName,
			acceptFileCount : editVue.acceptFileCount,
			minFileSize : editVue.minFileSize,
			maxFileSize : editVue.maxFileSize,
			remark : editVue.remark,
			isImage : editVue.isImage,
			isNeeded : editVue.isNeeded,
			listType : editVue.listType,
			acceptFileType : editVue.checkList,
			busiTypeId : editVue.theNameId,
			tableId:editVue.selectItem[0],
			isCfgSignature:editVue.isCfgSignature,
		}
		new ServerInterface(baseInfo.saveFileInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				refresh();
				$(baseInfo.addModelDivId).modal("hide")
			}
		});
		
	}
	//详情更新操作--------------获取"更新机构详情"参数
	function getUpdateForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			theState:this.theState,
			busiState:this.busiState,
			eCode:this.eCode,
			userStartId:this.userStartId,
			createTimeStamp:this.createTimeStamp,
			userUpdateId:this.userUpdateId,
			lastUpdateTimeStamp:this.lastUpdateTimeStamp,
			userRecordId:this.userRecordId,
			recordTimeStamp:this.recordTimeStamp,
			busiType:this.busiType,
			theName:this.theName,
			acceptFileType:this.acceptFileType,
			acceptFileCount:this.acceptFileCount,
			maxFileSize:this.maxFileSize,
			minFileSize:this.minFileSize,
			remark:this.remark,
			isImage:this.isImage,
			isNeeded:this.isNeeded,
			listType:this.listType,
			isCfgSignature:this.isCfgSignature,
		}
	}

	function update()
	{
		
		if(editVue.tableId !=""){
			enterNext2Tab(editVue.tableId, '附件类型管理详情', 'sm/Sm_AttachmentCfgDetail.shtml',editVue.tableId+"010201");
		}else{
			noty({"text":"请选择业务类型","type":"error","timeout":2000});
		}
	}
	
	function initData()
	{
		
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	editVue.refresh();
	editVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#sm_AttachmentCfgEditDiv",
	"detailInterface":"../Sm_AttachmentCfgToYw",
	"updateInterface":"../Sm_AttachmentCfgUpdate",
	"addModelDivId" : "#addModelAttachmentCfgEdit",
	"deleteDivId":"#deleteAttachmentCfgEditList",
	"edModelDivId":"#edModelAttachmentCfgEdit",
	"sdModelDivId":"#sdModelAttachmentCfgEdit",
	"saveFileInterface" : "../Sm_AttachmentCfgAdd",
	"editFileInterface" : "../Sm_AttachmentCfgToUpdate",
	"delFileInterface" : "../Sm_AttachmentCfgBatchDelete",
	/*"saveFileInterface" : "",
	"editFileInterface" : "",
	"delFileInterface" : "",*/
});
