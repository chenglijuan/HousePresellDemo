(function(baseInfo){
	var listVue = new Vue({
		el : baseInfo.listDivId,
		data : {
			interfaceVersion :19000101,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			keyword : "",
			selectItem : [],
			isAllSelected : false,
			theState:0,//正常为0，删除为1
				userStartId:null,
			userStartList:[],
			userUpdateId:null,
			userUpdateList:[],
			userRecordId:null,
			userRecordList:[],
			sm_AttachmentCfgList:[],
			editDisabled : true,
			delDisabled : true,
			errMsg : "",
		},
		methods : {
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			listItemSelectHandle: listItemSelectHandle,
			sm_AttachmentCfgDelOne : sm_AttachmentCfgDelOne,
			sm_AttachmentCfgDel : sm_AttachmentCfgDel,
			search:search,
			checkAllClicked : checkAllClicked,
			changePageNumber : function(data){
				//listVue.pageNumber = data;
				if (listVue.pageNumber != data) {
					listVue.pageNumber = data;
					listVue.refresh();
				}
			},
			changeCountPerPage:function(data){
				if (listVue.countPerPage != data) {
					listVue.countPerPage = data;
					listVue.refresh();
				}
			},
			resetSearch : resetSearch,//重置
			addAttachmentCfgHandle : addAttachmentCfgHandle,//点击新增
			editAttachmentCfgHandle : editAttachmentCfgHandle,//点击编辑
			delAttachmentCfgHandle : delAttachmentCfgHandle,//点击删除
			detailAttachmentCfgHandle : detailAttachmentCfgHandle,//点击详情
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue
		},
		watch:{
			//pageNumber : refresh,
			selectItem : selectedItemChanged,
		}
	});
	//------------------------方法定义-开始------------------//
	 
	//列表操作--------------获取"搜索列表"表单参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			pageNumber:this.pageNumber,
			countPerPage:this.countPerPage,
			totalPage:this.totalPage,
			keyword:this.keyword,
			theState:this.theState,
			userStartId:this.userStartId,
			userUpdateId:this.userUpdateId,
			userRecordId:this.userRecordId,
		}
	}
	//列表操作--------------获取“删除资源”表单参数
	function getDeleteForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			idArr:this.selectItem
		}
	}

	function sm_AttachmentCfgDel()
	{
		new ServerInterface(baseInfo.deleteInterface).execute(listVue.getDeleteForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});
				$(baseInfo.edModelDivId).modal({
					backdrop:"static"
				})
				listVue.errMsg = jsonObj.info;
			}
			else
			{
				$(baseInfo.deleteDivId).modal("hide")
				$(baseInfo.sdModelDivId).modal({
					backdrop:"static"
				})
				listVue.selectItem = [];
				refresh();
			}
		});
	}
	function sm_AttachmentCfgDelOne(sm_AttachmentCfgId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[sm_AttachmentCfgId],
		};
		
		noty({
			layout:'center',
			modal:true,
			text:"确认删除吗？",
			type:"confirm",
			buttons:[
				{
					addClass:"btn btn-primary",
					text:"确定",
					onClick:function($noty){
						$noty.close();
						new ServerInterface(baseInfo.deleteInterface).execute(model , function(jsonObj){
							if(jsonObj.result != "success")
							{
								noty({"text":jsonObj.info,"type":"error","timeout":2000});
							}
							else
							{
								refresh();
							}
						});
					}
				},
				{
					addClass:"btn btn-danger",
					text:"取消",
					onClick:function($noty){
						
						$noty.close();
					}
				}
			]
			
		});
	}
	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		listVue.isAllSelected = (listVue.sm_AttachmentCfgList.length > 0)
		&&	(listVue.selectItem.length == listVue.sm_AttachmentCfgList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.sm_AttachmentCfgList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.sm_AttachmentCfgList.forEach(function(item) {
	    		listVue.selectItem.push(item.tableId);
	    	});
	    }
	}
	//列表操作--------------刷新
	function refresh()
	{
		new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});
				$(baseInfo.edModelDivId).modal({
					backdrop:"static"
				})
				listVue.errMsg = jsonObj.info;
			}
			else
			{
				listVue.sm_AttachmentCfgList=jsonObj.newAttachmentCfg;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('sm_AttachmentCfgListDiv').scrollIntoView();
			}
		});
	}
	
	//列表操作------------搜索
	function search()
	{
		listVue.pageNumber = 1;
		refresh();
	}

	
	function indexMethod(index) {
		return generalIndexMethod(index, listVue)
	}

	function listItemSelectHandle(list) {
		if(list.length == "1"){
			listVue.editDisabled = false;
		}else{
			listVue.editDisabled = true;
		}
		if(list.length >= "1"){
			listVue.delDisabled = false;
		}else{
			listVue.delDisabled = true;
		}
		generalListItemSelectHandle(listVue,list);
 	}
	//列表操作----------------------重置
	function resetSearch(){
		listVue.keyword = "";
	}
	//列表操作----------------------点击新增
	function addAttachmentCfgHandle(){
		enterNewTab('', '新增附件类型', 'sm/Sm_AttachmentCfgAdd.shtml');
	}
	//列表操作----------------------点击修改
	function editAttachmentCfgHandle(){
		var tableId = listVue.selectItem[0];
		enterNextTab(tableId, '附件类型修改', 'sm/Sm_AttachmentCfgEdit.shtml',tableId+"06120201");
	}
	//列表操作----------------------点击删除
	function delAttachmentCfgHandle(){
		$(baseInfo.deleteDivId).modal({
			backdrop:"static"
		})
		//enterNextTab(tableId, '退房退款信息详情', 'sm/Sm_AttachmentCfgAdd.shtml',tableId+"06120201");
	}
	//列表操作----------------------点击详情
	function detailAttachmentCfgHandle(tableId){
		enterNextTab(tableId, '附件类型详情', 'sm/Sm_AttachmentCfgDetail.shtml',tableId+"06120201");
	}
	 //根据按钮权限，动态控制，该页面的所有“可控按钮”的显示与否
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}
	function initData()
	{
		initButtonList();
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#sm_AttachmentCfgListDiv",
	"deleteDivId":"#deleteAttachmentCfgList",
	"edModelDivId":"#edModelAttachmentCfgList",
	"sdModelDivId":"#sdModelAttachmentCfgList",
	"listInterface":"../Sm_AttachmentCfgLists",
	"deleteInterface":"../Sm_AttachmentCfgSBatchDelete",
});
