(function(baseInfo){
	var listVue = new Vue({
		el : baseInfo.listDivId,
		data : {
			interfaceVersion :19000101,
			pageNumber : 1,
			countPerPage : 12,
			totalPage : 1,
			totalCount : 1,
			keyword : "",
			selectItem : [],
			isAllSelected : false,
			theState:0,//正常为0，删除为1
			tgpf_BasicAccountVoucherList:[
			],
			editDisabled : true,
			delDisabled : true,
			billTimeStamp : "",
			busiStateList : [
				{tableId:"0",theName:"未推送"},
				{tableId:"1",theName:"已推送"},
			],
			sendState : '',
			propelDisabled : true,
			beginDate : '',
			endDate : '',
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
            search:search,    //搜索资源列表
            reset:reset,      //重置
            listItemSelectHandle: listItemSelectHandle,
            indexMethod:indexMethod,
            tgpf_FundAppropriatedDetail:tgpf_FundAppropriatedDetail,
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
			changeBusiState : function(data){
				this.sendState = data.tableId;
			},
			busiStateEmpty : function(){
				this.sendState =  null;
			},
			handlePropel :handlePropel,//推送
			getPropelForm : getPropelForm,
			
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue,
			'vue-listselect' : VueListSelect
		},
		watch:{
			//pageNumber : refresh,
			selectItem : selectedItemChanged,
		},
        mounted: function ()
        {
            laydate.render({
                elem: '#basicAccountListDate',
                range: '~',
	            done: function(value, date, endDate){
				    listVue.billTimeStamp = value;
				  	var arr = value.split("~");
				    listVue.beginDate = arr[0];
				    listVue.endDate = arr[1];
			    }
            })
        }
	});

	//------------------------方法定义-开始------------------//
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}
    function initData()
    {
    	initButtonList();
        refresh();
    }
    //重置
    function  reset()
    {
        this.keyword="";
        this.billTimeStamp = null;
        this.sendState =  null;
        this.beginDate = '';
        this.endDate = '';
    }
	 
	//列表操作--------------获取"搜索列表"表单参数
	function getSearchForm()
	{
		return {
            interfaceVersion:listVue.interfaceVersion,
            pageNumber:listVue.pageNumber,
            countPerPage:listVue.countPerPage,
            totalPage:listVue.totalPage,
            sendState:listVue.sendState,
            keyword:listVue.keyword,
            billTimeStamp : listVue.billTimeStamp,
            beginDate : listVue.beginDate,
            endDate : listVue.endDate,
		}
	}

    //列表操作------------搜索
    function search()
    {
        listVue.pageNumber = 1;
        refresh();
    }

    //列表操作--------------刷新
    function refresh()
    {
        new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj){
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                listVue.tgpf_BasicAccountVoucherList=jsonObj.tgpf_BasicAccountVoucherList;
                
                listVue.tgpf_BasicAccountVoucherList.forEach(function(item){
                	item.totalTradeAmount = addThousands(item.totalTradeAmount);
                });
                
                listVue.pageNumber=jsonObj.pageNumber;
                listVue.countPerPage=jsonObj.countPerPage;
                listVue.totalPage=jsonObj.totalPage;
                listVue.totalCount = jsonObj.totalCount;
                listVue.keyword=jsonObj.keyword;
                listVue.selectedItem=[];
            }
        });
    }

    //列表索引方法
    function indexMethod(index)
    {
        return generalIndexMethod(index, listVue)
    }

    function listItemSelectHandle(list)
    {
    	listVue.selectItem  = list;
    	if(list.length !=0){
    		for( var i = 0 ; i < listVue.selectItem.length ; i++)
    		{
				if(listVue.selectItem[i].sendState != "0")
				{
					listVue.propelDisabled = true;
					break;
				}
				else
				{
					listVue.propelDisabled = false;
				}
			}
    	}else{
    		listVue.propelDisabled = true;
    	}
        generalListItemSelectHandle(listVue,list);
    }
    function getPropelForm(){
    	return {
    		interfaceVersion:listVue.interfaceVersion,
    		idArr : this.selectItem
    	}
    }
    //推送
    function handlePropel(){
    	new ServerInterface(baseInfo.propelInterface).execute(listVue.getPropelForm(), function(jsonObj){
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
            	generalSuccessModal();
            	listVue.refresh();
                listVue.selectedItem=[];
            }
        });
    }
    //选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		listVue.isAllSelected = (listVue.tgpf_BasicAccountVoucherList.length > 0)
		&&	(listVue.selectItem.length == listVue.tgpf_BasicAccountVoucherList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgpf_BasicAccountVoucherList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgpf_BasicAccountVoucherList.forEach(function(item) {
	    		listVue.selectItem.push(item.tableId);
	    	});
	    }
	}
    //跳转方法 - 详情
    function tgpf_FundAppropriatedDetail(tableId)
    {
        enterNewTab(tableId, "基本户凭证详情", "tgpf/Tgpf_BasicAccountVoucherDetail.shtml");
    }
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tgpf_BasicAccountVoucherListDiv",
	"listInterface":"../Tgpf_BasicAccountVoucherList",
	"propelInterface" : "../Tgpf_BasicAccountVoucherSend"
});