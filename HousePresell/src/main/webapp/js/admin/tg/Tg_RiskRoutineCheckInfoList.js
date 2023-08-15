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
			tg_RiskRoutineCheckInfoList:[]
		},
		methods : {
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			getSearchForm : getSearchForm,
			listItemSelectHandle: listItemSelectHandle,
			search:search,
			checkAllClicked : checkAllClicked,
			changePageNumber : function(data){
				listVue.pageNumber = data;
			},
			addRiskRoutineCheck : addRiskRoutineCheck,
			toConfig : function()
			{
				enterNewTab("", '抽查比例配置', 'tg/Tg_RiskRoutineCheckRatioConfigList.shtml')
			}
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
			userRecordId:this.userRecordId,
		}
	}
	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		listVue.isAllSelected = (listVue.tg_RiskRoutineCheckInfoList.length > 0)
		&&	(listVue.selectItem.length == listVue.tg_RiskRoutineCheckInfoList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tg_RiskRoutineCheckInfoList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tg_RiskRoutineCheckInfoList.forEach(function(item) {
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
				listVue.tg_RiskRoutineCheckInfoList=jsonObj.tg_RiskRoutineCheckInfoList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tg_RiskRoutineCheckInfoListDiv').scrollIntoView();
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
		generalListItemSelectHandle(listVue, list)
 	}

	function addRiskRoutineCheck()
	{
    	var model={
            interfaceVersion:listVue.interfaceVersion,
            spotTimeStr:document.getElementById("nowMonth").value,
        }
    	new ServerInterface(baseInfo.addInterface).execute(model, function(jsonObj)
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
	
	function initData()
	{
		laydate.render({
			elem: '#nowMonth',
			type: 'month'
		});
		
		laydate.render({
			elem: '#spotCheckDate',
			range:true
		});
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.initData();
	listVue.refresh();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tg_RiskRoutineCheckInfoListDiv",
	"listInterface":"../Tg_RiskRoutineCheckInfoList",
	"addInterface":"../Tg_RiskRoutineCheckInfoAdd",
});
