(function(baseInfo){
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			sm_AttachmentCfgModel: {},
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			selectItem : [],
			editDisabled : true,
			delDisabled : true,
			Sm_AttachmentCfgAddList : [],
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
            userStart : "",//创建人
            createTimeStamp : "",//创建时间
            errMsg : "",
            tableId : "",
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
			//添加
			getAddForm : getAddForm,
			add: add,
			changePageNumber : function(data){
				//addVue.pageNumber = data;
				if (addVue.pageNumber != data) {
					addVue.pageNumber = data;
					addVue.changeBusiType();
				}
			},
			changeCountPerPage:function(data){
				if (addVue.countPerPage != data) {
					addVue.countPerPage = data;
					addVue.changeBusiType();
				}
			},
			addFileHadle : addFileHadle,//新增附件信息
			editFileHandle : editFileHandle,//附件修改
			delFileHandle : delFileHandle,
			saveFileType : saveFileType,//点击确定按钮
			checkAllClicked : checkAllClicked,
			listItemSelectHandle : listItemSelectHandle,
			changeBusiType : changeBusiType,//改变业务类型
			showDelModel : showDelModel,
			indexMethod : indexMethod ,
			isImageChange : function(data){
				addVue.isImage = data.tableId;
			},
			emptyIsImage : function(){
				addVue.isImage = null;	
			},
			isNeededChange : function(data){
				addVue.isNeeded = data.tableId;
			},
			emptyIsNeeded : function(){
				addVue.isNeeded = null;
			},
			listTypeChange : function(data){
				addVue.listType = data.tableId;
			},
			emptyListType : function(){
				addVue.listType = null;
			},
			isCfgSignatureChange : function(data){
				addVue.isCfgSignature = data.tableId;
			},
			emptyIsCfgSignature : function(data){
				addVue.isCfgSignature = null;
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
			selectItem : selectedItemChanged,
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
		new ServerInterface(baseInfo.detailInterface).execute(addVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				addVue.busTypeList = jsonObj.theNamelist
			}
		});
	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getAddForm()
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
	function indexMethod(index) {
		return generalIndexMethod(index, addVue)
	}
	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		addVue.isAllSelected = (addVue.Sm_AttachmentCfgAddList.length > 0)
		&&	(addVue.selectItem.length == addVue.Sm_AttachmentCfgAddList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(addVue.selectItem.length == addVue.Sm_AttachmentCfgAddList.length)
	    {
	    	addVue.selectItem = [];
	    }
	    else
	    {
	    	addVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	addVue.Sm_AttachmentCfgAddList.forEach(function(item) {
	    		addVue.selectItem.push(item.tableId);
	    	});
	    }
	}
	function indexMethod(index) {
		return generalIndexMethod(index, addVue)
	}

	function listItemSelectHandle(list) {
		if(list.length == "1"){
			addVue.editDisabled = false;
		}else{
			addVue.editDisabled = true;
		}
		if(list.length >= "1"){
			addVue.delDisabled = false;
		}else{
			addVue.delDisabled = true;
		}
		generalListItemSelectHandle(addVue,list);
 	}
	
	function changeBusiType(){
		var model = {
			interfaceVersion:this.interfaceVersion,
			busiTypeId : this.busiType,
			pageNumber:this.pageNumber,
			countPerPage:this.countPerPage,
			totalPage:this.totalPage,
		}
		new ServerInterface(baseInfo.changeBusiTypeInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				addVue.Sm_AttachmentCfgAddList = jsonObj.sm_AttachmentCfgList;
				addVue.tableId = jsonObj.baseParameter.tableId;
				addVue.pageNumber=jsonObj.pageNumber;
				addVue.countPerPage=jsonObj.countPerPage;
				addVue.totalPage=jsonObj.totalPage;
				addVue.totalCount = jsonObj.totalCount;
			}
		});
	}
	
	function clearModel(){
		addVue.theName = "";
		addVue.acceptFileCount = "";
		addVue.fileSize = "";
		addVue.remark = "";
		addVue.minFileSize = "";
		addVue.maxFileSize = "";
		addVue.isImage = "1";//是否是图片
		addVue.isNeeded = "1";//是否必须上传
		addVue.listType = "text";//附件列表类型
		addVue.checkList = [];
	}
	
	//新增操作-------------------点击新增按钮
	function addFileHadle(){
		if(addVue.busiType == ""){
			noty({"text":"请先选择业务类型","type":"error","timeout":2000});
		}else{
			addVue.selectItem = [];
			clearModel();
			$(baseInfo.addModelDivId).modal({
					backdrop:"static"
			})
		}
		
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
				clearModel()
				$(baseInfo.addModelDivId).modal({
					backdrop:"static"
				})
				addVue.theName = jsonObj.Sm_AttachmentCfg.theName;
				addVue.acceptFileCount = jsonObj.Sm_AttachmentCfg.acceptFileCount;
				addVue.fileSize = jsonObj.Sm_AttachmentCfg.fileSize;
				addVue.remark = jsonObj.Sm_AttachmentCfg.remark;
				addVue.minFileSize = jsonObj.Sm_AttachmentCfg.minFileSize;
				addVue.maxFileSize = jsonObj.Sm_AttachmentCfg.maxFileSize;
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
				addVue.isImage = isImg;//是否是图片
				addVue.isNeeded = isNeed;//是否必须上传
				addVue.listType = jsonObj.Sm_AttachmentCfg.listType;//附件列表类型
				var arr = jsonObj.Sm_AttachmentCfg.acceptFileType.split(",");
				addVue.checkList = arr;
			}
		});
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
				addVue.changeBusiType();
			}
		});
	}
	function showDelModel(){
		$(baseInfo.deleteDivId).modal({
			backdrop:"static"
		})
	}
	
	//新增操作---------------------点击确定按钮
	function saveFileType(){
		 console.log(addVue.selectItem);
		var model = {
			interfaceVersion:addVue.interfaceVersion,
			theName : addVue.theName,
			acceptFileCount : addVue.acceptFileCount,
			minFileSize : addVue.minFileSize,
			maxFileSize : addVue.maxFileSize,
			remark : addVue.remark,
			isImage : addVue.isImage,
			isNeeded : addVue.isNeeded,
			listType : addVue.listType,
			acceptFileType : addVue.checkList,
			busiTypeId : addVue.busiType,
			isCfgSignature:addVue.isCfgSignature,
			tableId:addVue.selectItem[0]
		}
		new ServerInterface(baseInfo.saveFileInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				addVue.changeBusiType()
				$(baseInfo.addModelDivId).modal("hide")
			}
		});
		//addVue.Sm_AttachmentCfgAddList.push(model);
		
	}
	function add()
	{
		if(addVue.tableId !=""){
			enterNext2Tab(addVue.tableId, '附件类型管理详情', 'sm/Sm_AttachmentCfgDetail.shtml',addVue.tableId+"010201");
		}else{
			noty({"text":"请选择业务类型","type":"error","timeout":2000});
		}
	}
	
	function initData()
	{
		
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	addVue.refresh();
	addVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#sm_AttachmentCfgDiv",
	"addModelDivId" : "#addModel",
	"deleteDivId":"#deleteAttachmentCfgAddList",
	"edModelDivId":"#edModelAttachmentCfgAdd",
	"sdModelDivId":"#sdModelAttachmentCfgAdd",
	"saveFileInterface" : "../Sm_AttachmentCfgAdd",
	"editFileInterface" : "../Sm_AttachmentCfgToUpdate",
	"delFileInterface" : "../Sm_AttachmentCfgBatchDelete",
	"changeBusiTypeInterface" : "../Sm_AttachmentCfgYwchange",
	"detailInterface":"../Sm_AttachmentCfgToAdd",
	"addInterface":"../"
});
