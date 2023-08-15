(function(baseInfo){
	var listVue = new Vue({
		el : baseInfo.listDivId,
		data : {
			interfaceVersion : 19000101,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			keyword : "",
			selectItem : [],
			isAllSelected : false,
			upDisabled:true,
			theState:0,//正常为0，删除为1
			busiType:'',
			sm_Permission_RoleId:null,
			sm_Permission_RoleList:[],
			orderBy:'',
			busiTypeList : [
				{tableId:"0",theName:"启用"},
				{tableId:"1",theName:"停用"},
			]
		},
		methods : {
			reset : reset,
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			listItemSelectHandle: listItemSelectHandle,
			sm_Permission_RoleUIDelOne : sm_Permission_RoleUIDelOne,
			sm_Permission_RoleUIDel : sm_Permission_RoleUIDel,
			showAjaxModal:showAjaxModal,
			search:search,
			checkAllClicked : checkAllClicked,
			changePageNumber : function(data){
				listVue.pageNumber = data;
			},
			changeCountPerPage : function (data) {
				if (listVue.countPerPage != data) {
					listVue.countPerPage = data;
					//listVue.refresh();
				}
			},
			sm_Permission_RoleUIEdit:sm_Permission_RoleUIEdit,
			sm_Permission_RoleUIDetail : sm_Permission_RoleUIDetail,
			permissionRoleUIExportExcel : permissionRoleUIExportExcel,
			sortChange: function(column, prop, order) {
				console.log(column);//当前列
				console.log(column.prop);//当前需要排序的字段
				console.log(column.order);//排序的规则（升序、降序和默认[默认就是没排序]）

				//提交到后台的参数：orderBy
				this.orderBy = generalOrderByColumn(column);
				search();
			},
			isUsingChange:function(data){
            	listVue.busiType = data.tableId;
            },
            emptyIsUsingChange : function(){
            	listVue.busiType = null;
            },
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue,
			'vue-listselect': VueListSelect,
		},
		watch:{
			pageNumber : refresh,
			countPerPage : refresh,
			selectItem : selectedItemChanged,
		}
	});
	var updateVue = new Vue({
		el : baseInfo.updateDivId,
		data : {
			interfaceVersion :19000101,
			theState:null,
			sm_Permission_RoleId:null,
			sm_Permission_RoleList:[],
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateSm_Permission_RoleUI
		}
	});
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			theState:null,
			sm_Permission_RoleId:null,
			sm_Permission_RoleList:[],
		},
		methods : {
			getAddForm : getAddForm,
			add : addSm_Permission_RoleUI
		}
	});

	//------------------------方法定义-开始------------------//
	function reset()
	{
		this.keyword = "";
		this.busiType = "";
		$('#date01010301').val("");
	}
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
			busiType:this.busiType,
			orderBy:this.orderBy,
			enableTimeStampRange:$('#date01010301').val(),
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
			sm_Permission_RoleId:this.sm_Permission_RoleId,
		}
	}
	//列表操作--------------获取“更新”表单参数
	function getUpdateForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			theState:this.theState,
			sm_Permission_RoleId:this.sm_Permission_RoleId,
		}
	}
	function sm_Permission_RoleUIDel()
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
	function sm_Permission_RoleUIDelOne(sm_Permission_RoleUIId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[sm_Permission_RoleUIId],
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
		listVue.isAllSelected = (listVue.sm_Permission_RoleList.length > 0)
		&&	(listVue.selectItem.length == listVue.sm_Permission_RoleList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.sm_Permission_RoleList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.sm_Permission_RoleList.forEach(function(item) {
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
				console.info(jsonObj);
				listVue.sm_Permission_RoleList=jsonObj.sm_Permission_RoleList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				
				//动态跳转到锚点处，id="top"
				document.getElementById('sm_Permission_RoleUIListDiv').scrollIntoView();
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
	function showAjaxModal(sm_Permission_RoleUIModel)
	{
		//sm_Permission_RoleUIModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'sm_Permission_RoleUI', sm_Permission_RoleUIModel);
		//updateVue.$set("sm_Permission_RoleUI", sm_Permission_RoleUIModel);
		
		updateVue.theState = sm_Permission_RoleUIModel.theState;
		updateVue.sm_Permission_RoleId = sm_Permission_RoleUIModel.sm_Permission_RoleId;
		updateVue.sm_Permission_UIResourceId = sm_Permission_RoleUIModel.sm_Permission_UIResourceId;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateSm_Permission_RoleUI()
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
	function addSm_Permission_RoleUI()
	{
		new ServerInterface(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				$(baseInfo.addDivId).modal('hide');
				addVue.sm_Permission_RoleId = null;
				addVue.sm_Permission_UIResourceId = null;
				refresh();
			}
		});
	}
	
	function indexMethod(index) {
		return generalIndexMethod(index, listVue)
	}

	function listItemSelectHandle(list) {
		//修改按钮禁用状态
        if(list.length==1)
        {
        	listVue.upDisabled = false;
        }
        else
        {
        	listVue.upDisabled = true;
        }
        
		generalListItemSelectHandle(listVue,list)
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
		laydate.render({
			elem: '#date01010301',
			range:true
		});
	}
	//------------------------方法定义-结束------------------//
	//修改
    function sm_Permission_RoleUIEdit()
    {
        var tableId = listVue.selectItem[0];
        enterNewTab(tableId, "角色授权修改", "sm/Sm_Permission_RoleUIEdit.shtml");
    }

    //进入详情
    function sm_Permission_RoleUIDetail(tableId)
    {
    	enterNewTab(tableId,'角色授权详情','sm/Sm_Permission_RoleUIDetail.shtml');
    }
    
    function permissionRoleUIExportExcel()
	{
		var model = {
			interfaceVersion : 19000101,
			idArr : listVue.selectItem,
			keyword : listVue.keyword,
		};
		
		new ServerInterface(baseInfo.permissionRoleUIExportExcelInterface).execute(model, function (jsonObj) {
			if (jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				window.location.href=jsonObj.fileDownloadPath;
				listVue.selectItem = [];
			}
		});
	}
	//------------------------数据初始化-开始----------------//
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#sm_Permission_RoleUIListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Sm_Permission_RoleUIList",
	"addInterface":"../Sm_Permission_RoleUIAdd",
	"deleteInterface":"../Sm_Permission_RoleUIDelete",
	"updateInterface":"../Sm_Permission_RoleUIUpdate",
	"permissionRoleUIExportExcelInterface":"../Sm_Permission_RoleUIExportExcel",
});
