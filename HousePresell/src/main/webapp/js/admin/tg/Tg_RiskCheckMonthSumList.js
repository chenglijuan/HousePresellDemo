(function(baseInfo){
	var listVue = new Vue({
		el : baseInfo.listDivId,
		data : {
			interfaceVersion :19000101,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			selectItem : [],
			isAllSelected : false,
			theState:0,//正常为0，删除为1
			rectificationState:"",
			tg_RiskCheckMonthSumList:[],
			rectificationStateList : [
				{tableId:"已完成",theName:"已完成"},
				{tableId:"进行中",theName:"进行中"},
			]
		},
		methods : {
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			getSearchForm : getSearchForm,
			listItemSelectHandle: listItemSelectHandle,
			search:search,
			reset : function ()
			{
				listVue.rectificationState = "";
				$('#date2102010202').val("");
				generalResetSearch(listVue, function () {
		            refresh()
		        })
			},
			checkAllClicked : checkAllClicked,
			changePageNumber : function(data){
				listVue.pageNumber = data;
			},
			addRiskRoutineCheck : addRiskRoutineCheck,
			toConfig : function()
			{
				enterNewTab("", '抽查比例配置', 'tg/Tg_RiskRoutineCheckRatioConfigList.shtml')
			},
			changeCountPerPage : function (data) {
				if (listVue.countPerPage != data) {
					listVue.countPerPage = data;
					listVue.refresh();
				}
			},
			rishCheckMonthSumDetailPageOpen : function(spotTimeStamp){
				enterNewTab(spotTimeStamp+"、"+"列表", '新增例行抽查', 'tg/Tg_RiskCheckMonthSumDetail.shtml')
			},
			changeRectificationState : function(data){
				this.rectificationState = data.tableId;
			},
			rectificationStateEmpty : function(){
				this.rectificationState = null;
			}
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue,
			'vue-listselect' : VueListSelect,
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
			theState:this.theState,
			rectificationState:this.rectificationState,
			riskCheckSearchDateStr:document.getElementById("date2102010202").value,
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
				listVue.tg_RiskCheckMonthSumList=jsonObj.tg_RiskCheckMonthSumList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tg_RiskCheckMonthSumListDiv').scrollIntoView();
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
		$("#addRishCheckInfo").modal('show');
    	var model={
            interfaceVersion:listVue.interfaceVersion,
            spotTimeStr:document.getElementById("date2102010201").value
        }
    	new ServerInterface(baseInfo.addInterface).execute(model, function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
            	$("#addRishCheckInfo").modal('hide');
                generalErrorModal(jsonObj);
            }
            else
            {
            	$("#addRishCheckInfo").modal('hide');
                generalSuccessModal();
                //跳转新增页面
//                enterNewTabCloseCurrent(document.getElementById("date2102010201").value+"、"+"新增", '新增例行抽查', 'tg/Tg_RiskCheckMonthSumDetail.shtml')
                enterNewTab(document.getElementById("date2102010201").value+"、"+"新增", '新增例行抽查', 'tg/Tg_RiskCheckMonthSumDetail.shtml')
            }
        });
	}
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}
	function initData()
	{
		laydate.render({
			elem: '#date2102010201',
			type: 'month'
		});
		
		laydate.render({
			elem: '#date2102010202',
			range:true
		});
		initButtonList();
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.initData();
	listVue.refresh();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tg_RiskCheckMonthSumListDiv",
	"listInterface":"../Tg_RiskCheckMonthSumList",
	"addInterface":"../Tg_RiskRoutineCheckInfoAdd",
});
