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
			sm_AttachmentList:[]
		},
		methods : {
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			listItemSelectHandle: listItemSelectHandle,
			sm_AttachmentDelOne : sm_AttachmentDelOne,
			sm_AttachmentDel : sm_AttachmentDel,
			showAjaxModal:showAjaxModal,
			search:search,
			checkAllClicked : checkAllClicked,
			changePageNumber : function(data){
				listVue.pageNumber = data;
			},
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue
		},
		watch:{
//			pageNumber : refresh,
			selectItem : selectedItemChanged,
		}
	});
	var updateVue = new Vue({
		el : baseInfo.updateDivId,
		data : {
			interfaceVersion :19000101,
			theState:null,
			userStartId:null,
			userStartList:[],
			createTimeStamp:null,
			sourceType:null,
			sourceId:null,
			fileType:null,
			totalPage:null,
			theLink:null,
			theSize:null,
			remark:null,
			md5Info:null,
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateSm_Attachment
		}
	});
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			theState:null,
			userStartId:null,
			userStartList:[],
			createTimeStamp:null,
			sourceType:null,
			sourceId:null,
			fileType:null,
			totalPage:null,
			theLink:null,
			theSize:null,
			remark:null,
			md5Info:null,
		},
		methods : {
			getAddForm : getAddForm,
			add : addSm_Attachment
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
	//列表操作--------------获取“新增”表单参数
	function getAddForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			userStartId:this.userStartId,
			createTimeStamp:this.createTimeStamp,
			sourceType:this.sourceType,
			sourceId:this.sourceId,
			fileType:this.fileType,
			totalPage:this.totalPage,
			theLink:this.theLink,
			theSize:this.theSize,
			remark:this.remark,
			md5Info:this.md5Info,
		}
	}
	//列表操作--------------获取“更新”表单参数
	function getUpdateForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			theState:this.theState,
			userStartId:this.userStartId,
			createTimeStamp:this.createTimeStamp,
			sourceType:this.sourceType,
			sourceId:this.sourceId,
			fileType:this.fileType,
			totalPage:this.totalPage,
			theLink:this.theLink,
			theSize:this.theSize,
			remark:this.remark,
			md5Info:this.md5Info,
		}
	}
	function sm_AttachmentDel()
	{
		noty({
			layout:'center',
			modal:true,
			text:"确认批量删除吗？",
			type:"confirm",
			buttons:[
				{
					addClass:"btn btn-primary",
					text:"确定",
					onClick:function($noty){
						$noty.close();
						new ServerInterface(baseInfo.deleteInterface).execute(listVue.getDeleteForm(), function(jsonObj){
							if(jsonObj.result != "success")
							{
								noty({"text":jsonObj.info,"type":"error","timeout":2000});
							}
							else
							{
								listVue.selectItem = [];
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
	function sm_AttachmentDelOne(sm_AttachmentId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[sm_AttachmentId],
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
		listVue.isAllSelected = (listVue.sm_AttachmentList.length > 0)
		&&	(listVue.selectItem.length == listVue.sm_AttachmentList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.sm_AttachmentList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.sm_AttachmentList.forEach(function(item) {
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
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				listVue.sm_AttachmentList=jsonObj.sm_AttachmentList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('sm_AttachmentListDiv').scrollIntoView();
			}
		});
	}
	
	//列表操作------------搜索
	function search()
	{
		listVue.pageNumber = 1;
		refresh();
	}
	
	//弹出编辑模态框--更新操作
	function showAjaxModal(sm_AttachmentModel)
	{
		//sm_AttachmentModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'sm_Attachment', sm_AttachmentModel);
		//updateVue.$set("sm_Attachment", sm_AttachmentModel);
		
		updateVue.theState = sm_AttachmentModel.theState;
		updateVue.userStartId = sm_AttachmentModel.userStartId;
		updateVue.createTimeStamp = sm_AttachmentModel.createTimeStamp;
		updateVue.sourceType = sm_AttachmentModel.sourceType;
		updateVue.sourceId = sm_AttachmentModel.sourceId;
		updateVue.fileType = sm_AttachmentModel.fileType;
		updateVue.totalPage = sm_AttachmentModel.totalPage;
		updateVue.theLink = sm_AttachmentModel.theLink;
		updateVue.theSize = sm_AttachmentModel.theSize;
		updateVue.remark = sm_AttachmentModel.remark;
		updateVue.md5Info = sm_AttachmentModel.md5Info;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateSm_Attachment()
	{
		new ServerInterface(baseInfo.updateInterface).execute(updateVue.getUpdateForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				$(baseInfo.updateDivId).modal('hide');
				refresh();
			}
		});
	}
	function addSm_Attachment()
	{
		new ServerInterface(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				$(baseInfo.addDivId).modal('hide');
				addVue.userStartId = null;
				addVue.createTimeStamp = null;
				addVue.sourceType = null;
				addVue.sourceId = null;
				addVue.fileType = null;
				addVue.totalPage = null;
				addVue.theLink = null;
				addVue.theSize = null;
				addVue.remark = null;
				addVue.md5Info = null;
				refresh();
			}
		});
	}
	
	function indexMethod(index) {
		return generalIndexMethod(index, listVue)
	}

	function listItemSelectHandle(list) {
		generalListItemSelectHandle(listVue,list)
 	}

	function initData()
	{
		
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#sm_AttachmentListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Sm_AttachmentList",
	"addInterface":"../Sm_AttachmentAdd",
	"deleteInterface":"../Sm_AttachmentDelete",
	"updateInterface":"../Sm_AttachmentUpdate"
});
