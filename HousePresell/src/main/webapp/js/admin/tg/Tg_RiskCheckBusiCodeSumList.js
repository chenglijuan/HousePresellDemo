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
			tg_RiskCheckBusiCodeSumList:[],
			bigBusiType:"",
			smallBusiType:"",
			bigBusiTypeList : [
				{tableId:"030301",theName:"受限额度管理"},
				{tableId:"0303",theName:"工程进度管理"},
				{tableId:"061102",theName:"合作协议"},
				{tableId:"061103",theName:"三方协议"},
				{tableId:"061203",theName:"托管一般拨付管理"},
				{tableId:"061202",theName:"退房退款管理"},
				{tableId:"2101",theName:"决策支持管理"},
				{tableId:"061204",theName:"支付保证业务"},
			],
			smallBusiTypeList : [
				{tableId:"03030101",theName:"受限额度变更"},
				{tableId:"03030100",theName:"工程进度节点更新"},
				{tableId:"03030102",theName:"托管终止"},
				{tableId:"06110203",theName:"全额托管合作协议签署"},
				{tableId:"06110201",theName:"贷款托管合作协议签署"},
				{tableId:"06110302",theName:"全额三方托管协议签署"},
				{tableId:"06110301",theName:"贷款三方托管协议签署"},
				{tableId:"06120301",theName:"用款申请与复核"},
				{tableId:"06120201",theName:"退房退款申请-贷款已结清"},
				{tableId:"06120202",theName:"退房退款申请-贷款未结清"},
				{tableId:"210104",theName:"存单存入管理"},
				{tableId:"06120401",theName:"支付保证申请与复核"},
			]
		},
		methods : {
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			getSearchForm : getSearchForm,
			listItemSelectHandle: listItemSelectHandle,
			search:search,
			reset:function ()
			{
		        this.bigBusiType = "";
		        this.smallBusiType = "";
		        $('#date2102010301').val("");
		        generalResetSearch(listVue, function () {
		            refresh()
		        })
			},
			checkAllClicked : checkAllClicked,
			changePageNumber : function(data){
				listVue.pageNumber = data;
			},
			changeCountPerPage : function (data) {
				if (listVue.countPerPage != data) {
					listVue.countPerPage = data;
					listVue.refresh();
				}
			},
			rishCheckHandleResultDetailPageOpen : function(data){
				enterNewTab(data.spotTimeStamp+"、"+data.bigBusiValue+"、"+data.smallBusiValue, '风控抽查结果处理详情', 'tg/Tg_RiskCheckHandleResultDetail.shtml')
			},
			changeBigBusiType : function(data){
				this.bigBusiType = data.tableId;
			},
			bigBusiTypeEmpty : function(){
				this.bigBusiType = null;
			},
			changeSmallBusiType : function(data){
				this.smallBusiType = data.tableId;
			},
			smallBusiTypeEmpty : function(){
				this.smallBusiType = null;
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
			bigBusiType:this.bigBusiType,
			smallBusiType:this.smallBusiType,
			spotTimeStr:document.getElementById("date2102010301").value,
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
				listVue.tg_RiskCheckBusiCodeSumList=jsonObj.tg_RiskCheckBusiCodeSumList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tg_RiskCheckBusiCodeSumListDiv').scrollIntoView();
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

	function initData()
	{
		laydate.render({
			elem: '#date2102010301',
			type: 'month'
		});
		
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.initData();
	listVue.refresh();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tg_RiskCheckBusiCodeSumListDiv",
	"listInterface":"../Tg_RiskCheckBusiCodeSumList",
});
