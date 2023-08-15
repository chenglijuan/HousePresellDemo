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
				bldLimitAmountVerMngId:null,
			bldLimitAmountVerMngList:[],
			tgpj_BldLimitAmountVer_AFDtlList:[]
		},
		methods : {
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			showLog: showLog,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			listItemSelectHandle: listItemSelectHandle,
			tgpj_BldLimitAmountVer_AFDtlDelOne : tgpj_BldLimitAmountVer_AFDtlDelOne,
			tgpj_BldLimitAmountVer_AFDtlDel : tgpj_BldLimitAmountVer_AFDtlDel,
			showAjaxModal:showAjaxModal,
			search:search,
			checkAllClicked : checkAllClicked,
			changePageNumber : function(data){
				listVue.pageNumber = data;
			},

            resetSearch:resetSearch,
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
			bldLimitAmountVerMngId:null,
			bldLimitAmountVerMngList:[],
			stageName:null,
			limitedAmount:null,
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateTgpj_BldLimitAmountVer_AFDtl
		}
	});
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			theState:null,
			bldLimitAmountVerMngId:null,
			bldLimitAmountVerMngList:[],
			stageName:null,
			limitedAmount:null,
		},
		methods : {
			getAddForm : getAddForm,
			add : addTgpj_BldLimitAmountVer_AFDtl
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
			bldLimitAmountVerMngId:this.bldLimitAmountVerMngId,
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
			bldLimitAmountVerMngId:this.bldLimitAmountVerMngId,
			stageName:this.stageName,
			limitedAmount:this.limitedAmount,
		}
	}
	//列表操作--------------获取“更新”表单参数
	function getUpdateForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			theState:this.theState,
			bldLimitAmountVerMngId:this.bldLimitAmountVerMngId,
			stageName:this.stageName,
			limitedAmount:this.limitedAmount,
		}
	}
	function tgpj_BldLimitAmountVer_AFDtlDel()
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
	function tgpj_BldLimitAmountVer_AFDtlDelOne(tgpj_BldLimitAmountVer_AFDtlId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[tgpj_BldLimitAmountVer_AFDtlId],
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
		listVue.isAllSelected = (listVue.tgpj_BldLimitAmountVer_AFDtlList.length > 0)
		&&	(listVue.selectItem.length == listVue.tgpj_BldLimitAmountVer_AFDtlList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgpj_BldLimitAmountVer_AFDtlList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgpj_BldLimitAmountVer_AFDtlList.forEach(function(item) {
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
				listVue.tgpj_BldLimitAmountVer_AFDtlList=jsonObj.tgpj_BldLimitAmountVer_AFDtlList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tgpj_BldLimitAmountVer_AFDtlListDiv').scrollIntoView();
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
	function showAjaxModal(tgpj_BldLimitAmountVer_AFDtlModel)
	{
		//tgpj_BldLimitAmountVer_AFDtlModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'tgpj_BldLimitAmountVer_AFDtl', tgpj_BldLimitAmountVer_AFDtlModel);
		//updateVue.$set("tgpj_BldLimitAmountVer_AFDtl", tgpj_BldLimitAmountVer_AFDtlModel);
		
		updateVue.theState = tgpj_BldLimitAmountVer_AFDtlModel.theState;
		updateVue.bldLimitAmountVerMngId = tgpj_BldLimitAmountVer_AFDtlModel.bldLimitAmountVerMngId;
		updateVue.stageName = tgpj_BldLimitAmountVer_AFDtlModel.stageName;
		updateVue.limitedAmount = tgpj_BldLimitAmountVer_AFDtlModel.limitedAmount;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateTgpj_BldLimitAmountVer_AFDtl()
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
	function addTgpj_BldLimitAmountVer_AFDtl()
	{
		new ServerInterface(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				$(baseInfo.addDivId).modal('hide');
				addVue.bldLimitAmountVerMngId = null;
				addVue.stageName = null;
				addVue.limitedAmount = null;
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

    function resetSearch() {
        generalResetSearch(listVue,function () {
            refresh()
        })
    }

	function showLog() {
		enterLogTab(listVue.poName)
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
	"listDivId":"#tgpj_BldLimitAmountVer_AFDtlListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Tgpj_BldLimitAmountVer_AFDtlList",
	"addInterface":"../Tgpj_BldLimitAmountVer_AFDtlAdd",
	"deleteInterface":"../Tgpj_BldLimitAmountVer_AFDtlDelete",
	"updateInterface":"../Tgpj_BldLimitAmountVer_AFDtlUpdate"
});
