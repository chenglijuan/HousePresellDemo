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
			userOperateId:null,
			userOperateList:[],
			sm_View_LogList:[{
				changeName:'joy',
				changeTime:'2018-7-20'
			},{
				changeName:'joy',
				changeTime:'2018-7-20'
			}]
		},
		methods : {
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			listItemSelectHandle: listItemSelectHandle,
			sm_Operate_LogDelOne : sm_Operate_LogDelOne,
			sm_Operate_LogDel : sm_Operate_LogDel,
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
			userOperateId:null,
			userOperateList:[],
			remoteAddress:null,
			operate:null,
			inputForm:null,
			result:null,
			info:null,
			returnJson:null,
			startTimeStamp:null,
			endTimeStamp:null,
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateSm_Operate_Log
		}
	});
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			theState:null,
			userOperateId:null,
			userOperateList:[],
			remoteAddress:null,
			operate:null,
			inputForm:null,
			result:null,
			info:null,
			returnJson:null,
			startTimeStamp:null,
			endTimeStamp:null,
		},
		methods : {
			getAddForm : getAddForm,
			add : addSm_Operate_Log
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
			userOperateId:this.userOperateId,
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
			userOperateId:this.userOperateId,
			remoteAddress:this.remoteAddress,
			operate:this.operate,
			inputForm:this.inputForm,
			result:this.result,
			info:this.info,
			returnJson:this.returnJson,
			startTimeStamp:this.startTimeStamp,
			endTimeStamp:this.endTimeStamp,
		}
	}
	//列表操作--------------获取“更新”表单参数
	function getUpdateForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			theState:this.theState,
			userOperateId:this.userOperateId,
			remoteAddress:this.remoteAddress,
			operate:this.operate,
			inputForm:this.inputForm,
			result:this.result,
			info:this.info,
			returnJson:this.returnJson,
			startTimeStamp:this.startTimeStamp,
			endTimeStamp:this.endTimeStamp,
		}
	}
	function sm_Operate_LogDel()
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
	function sm_Operate_LogDelOne(sm_Operate_LogId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[sm_Operate_LogId],
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
		listVue.isAllSelected = (listVue.sm_View_LogList.length > 0)
		&&	(listVue.selectItem.length == listVue.sm_View_LogList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.sm_View_LogList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.sm_View_LogList.forEach(function(item) {
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
				listVue.sm_View_LogList=jsonObj.sm_View_LogList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('sm_View_LogListDiv').scrollIntoView();
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
	function showAjaxModal(sm_Operate_LogModel)
	{
		//sm_Operate_LogModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'sm_Operate_Log', sm_Operate_LogModel);
		//updateVue.$set("sm_Operate_Log", sm_Operate_LogModel);
		
		updateVue.theState = sm_Operate_LogModel.theState;
		updateVue.userOperateId = sm_Operate_LogModel.userOperateId;
		updateVue.remoteAddress = sm_Operate_LogModel.remoteAddress;
		updateVue.operate = sm_Operate_LogModel.operate;
		updateVue.inputForm = sm_Operate_LogModel.inputForm;
		updateVue.result = sm_Operate_LogModel.result;
		updateVue.info = sm_Operate_LogModel.info;
		updateVue.returnJson = sm_Operate_LogModel.returnJson;
		updateVue.startTimeStamp = sm_Operate_LogModel.startTimeStamp;
		updateVue.endTimeStamp = sm_Operate_LogModel.endTimeStamp;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateSm_Operate_Log()
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
	function addSm_Operate_Log()
	{
		new ServerInterface(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				$(baseInfo.addDivId).modal('hide');
				addVue.userOperateId = null;
				addVue.remoteAddress = null;
				addVue.operate = null;
				addVue.inputForm = null;
				addVue.result = null;
				addVue.info = null;
				addVue.returnJson = null;
				addVue.startTimeStamp = null;
				addVue.endTimeStamp = null;
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
	"listDivId":"#sm_View_LogListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Sm_View_LogList",
	"addInterface":"../Sm_Operate_LogAdd",
	"deleteInterface":"../Sm_Operate_LogDelete",
	"updateInterface":"../Sm_Operate_LogUpdate"
});
