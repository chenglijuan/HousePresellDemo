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
			sm_FastNavigateList:[]
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			sm_FastNavigateDelOne : sm_FastNavigateDelOne,
			sm_FastNavigateDel : sm_FastNavigateDel,
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
			busiState:null,
			eCode:null,
			userStartId:null,
			userStartList:[],
			createTimeStamp:null,
			userUpdateId:null,
			userUpdateList:[],
			lastUpdateTimeStamp:null,
			userTableId:null,
			menuTableId:null,
			theNameOfMenu:null,
			theLinkOfMenu:null,
			orderNumber:null,
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateSm_FastNavigate
		}
	});
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			theState:null,
			busiState:null,
			eCode:null,
			userStartId:null,
			userStartList:[],
			createTimeStamp:null,
			userUpdateId:null,
			userUpdateList:[],
			lastUpdateTimeStamp:null,
			userTableId:null,
			menuTableId:null,
			theNameOfMenu:null,
			theLinkOfMenu:null,
			orderNumber:null,
		},
		methods : {
			getAddForm : getAddForm,
			add : addSm_FastNavigate
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
			busiState:this.busiState,
			eCode:this.eCode,
			userStartId:this.userStartId,
			createTimeStamp:this.createTimeStamp,
			userUpdateId:this.userUpdateId,
			lastUpdateTimeStamp:this.lastUpdateTimeStamp,
			userTableId:this.userTableId,
			menuTableId:this.menuTableId,
			theNameOfMenu:this.theNameOfMenu,
			theLinkOfMenu:this.theLinkOfMenu,
			orderNumber:this.orderNumber,
		}
	}
	//列表操作--------------获取“更新”表单参数
	function getUpdateForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			theState:this.theState,
			busiState:this.busiState,
			eCode:this.eCode,
			userStartId:this.userStartId,
			createTimeStamp:this.createTimeStamp,
			userUpdateId:this.userUpdateId,
			lastUpdateTimeStamp:this.lastUpdateTimeStamp,
			userTableId:this.userTableId,
			menuTableId:this.menuTableId,
			theNameOfMenu:this.theNameOfMenu,
			theLinkOfMenu:this.theLinkOfMenu,
			orderNumber:this.orderNumber,
		}
	}
	function sm_FastNavigateDel()
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
	function sm_FastNavigateDelOne(sm_FastNavigateId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[sm_FastNavigateId],
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
		listVue.isAllSelected = (listVue.sm_FastNavigateList.length > 0)
		&&	(listVue.selectItem.length == listVue.sm_FastNavigateList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.sm_FastNavigateList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.sm_FastNavigateList.forEach(function(item) {
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
				listVue.sm_FastNavigateList=jsonObj.sm_FastNavigateList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('sm_FastNavigateListDiv').scrollIntoView();
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
	function showAjaxModal(sm_FastNavigateModel)
	{
		//sm_FastNavigateModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'sm_FastNavigate', sm_FastNavigateModel);
		//updateVue.$set("sm_FastNavigate", sm_FastNavigateModel);
		
		updateVue.theState = sm_FastNavigateModel.theState;
		updateVue.busiState = sm_FastNavigateModel.busiState;
		updateVue.eCode = sm_FastNavigateModel.eCode;
		updateVue.userStartId = sm_FastNavigateModel.userStartId;
		updateVue.createTimeStamp = sm_FastNavigateModel.createTimeStamp;
		updateVue.userUpdateId = sm_FastNavigateModel.userUpdateId;
		updateVue.lastUpdateTimeStamp = sm_FastNavigateModel.lastUpdateTimeStamp;
		updateVue.userTableId = sm_FastNavigateModel.userTableId;
		updateVue.menuTableId = sm_FastNavigateModel.menuTableId;
		updateVue.theNameOfMenu = sm_FastNavigateModel.theNameOfMenu;
		updateVue.theLinkOfMenu = sm_FastNavigateModel.theLinkOfMenu;
		updateVue.orderNumber = sm_FastNavigateModel.orderNumber;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateSm_FastNavigate()
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
	function addSm_FastNavigate()
	{
		new ServerInterface(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				$(baseInfo.addDivId).modal('hide');
				addVue.busiState = null;
				addVue.eCode = null;
				addVue.userStartId = null;
				addVue.createTimeStamp = null;
				addVue.userUpdateId = null;
				addVue.lastUpdateTimeStamp = null;
				addVue.userTableId = null;
				addVue.menuTableId = null;
				addVue.theNameOfMenu = null;
				addVue.theLinkOfMenu = null;
				addVue.orderNumber = null;
				refresh();
			}
		});
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
	"listDivId":"#sm_FastNavigateListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Sm_FastNavigateList",
	"addInterface":"../Sm_FastNavigateAdd",
	"deleteInterface":"../Sm_FastNavigateDelete",
	"updateInterface":"../Sm_FastNavigateUpdate"
});
