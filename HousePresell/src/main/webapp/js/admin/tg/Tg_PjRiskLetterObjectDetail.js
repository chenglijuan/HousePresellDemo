(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tg_PjRiskLetterModel: {},
			tableId : 1,
			tg_PjRiskLetterDetailList: [{
				theName: "111"
			},{
				theName: "222"
			}],
			tg_PjRiskLetterDetail1List: [{
				theName: "111"
			},{
				theName: "222"
			}],
			selectItem : [],
			selectItem1 : [],
			isAllSelected : false,
			isAllSelected1 : false,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			goToEditHandle: goToEditHandle,
			add: add,
			listItemSelectHandle: listItemSelectHandle,
			listItemSelectHandle1: listItemSelectHandle1,
			indexMethod: indexMethod,
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
			handleEditSave: handleEditSave,
			LogModelClose: LogModelClose,
			tg_PjRiskLetterDel: tg_PjRiskLetterDel
		},
		computed:{
			 
		},
		components : {

		},
		watch:{
			selectItem : selectedItemChanged,
			selectItem1 : selectedItemChanged1,
		}
	});

	//------------------------方法定义-开始------------------//
	function handleEditSave()
	{
		var model = {
				interfaceVersion:this.interfaceVersion,
				idArr:detailVue.selectItem1,
		}
		new ServerInterface("../Tg_PjRiskLetterReceiverListAdd").execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				$(baseInfo.letterObjectDetail).modal('hide');
				generalSuccessModal();
				detailVue.refresh();
			}
		});
		$(baseInfo.letterObjectDetail).modal('hide');
	}
	function LogModelClose()
	{
		$(baseInfo.letterObjectDetail).modal('hide');
	}
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:detailVue.tableId,
		}
	}
	function indexMethod(index) {
		return generalIndexMethod(index, detailVue)
	}
	function listItemSelectHandle(list) {
		generalListItemSelectHandle(detailVue,list)
		console.log(detailVue.selectItem);
 	}
	function listItemSelectHandle1(list) {
		detailVue.selectItem1 = [];
	    for (var index = 0; index < list.length; index++) {
	        var element = list[index].tableId;
	        detailVue.selectItem1.push(element)
	    }
	    console.log(detailVue.selectItem1);
 	}
	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		detailVue.isAllSelected = (detailVue.tg_PjRiskLetterDetailList.length > 0)
		&&	(detailVue.selectItem.length == detailVue.tg_PjRiskLetterDetailList.length);
	}
	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged1()
	{	
		detailVue.isAllSelected1 = (detailVue.tg_PjRiskLetterDetail1List.length > 0)
		&&	(detailVue.selectItem1.length == detailVue.tg_PjRiskLetterDetail1List.length);
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
	    
	    if(detailVue.selectItem1.length == detailVue.tg_PjRiskLetterDetail1List.length)
	    {
	    	detailVue.selectItem1 = [];
	    }
	    else
	    {
	    	detailVue.selectItem1 = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	detailVue.tg_PjRiskLetterDetail1List.forEach(function(item) {
	    		detailVue.selectItem1.push(item.tableId);
	    	});
	    }
	}
	
	function tg_PjRiskLetterDel()
	{
		var model = {
				interfaceVersion:this.interfaceVersion,
				idArr:detailVue.selectItem,
		}
		new ServerInterface("../Tg_PjRiskLetterReceiverBatchDelete").execute(model, function(jsonObj)
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
	
	function add()
	{
		$(baseInfo.letterObjectDetail).modal('show', {
		    backdrop :'static'
	    });
		new ServerInterface("../Tg_PjRiskLetterReceiverOrgMemberList").execute(detailVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				detailVue.tg_PjRiskLetterDetail1List = jsonObj.emmp_OrgMemberList;
			}
		});
	}
	
	function goToEditHandle()
	{
		enterNext2Tab(detailVue.tableId, '项目风险函修改', 'tg/Tg_PjRiskLetterEdit.shtml',detailVue.tableId + "21020304");
	}

	//详情操作--------------
	function refresh()
	{
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
				detailVue.tg_PjRiskLetterDetailList = jsonObj.tg_PjRiskLetterReceiverList;
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
	"detailDivId":"#tg_PjRiskLetterObjectDiv",
	"letterObjectDetail":"#letterObjectDetail",
	"detailInterface":"../Tg_PjRiskLetterReceiverList"
});
