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
			sendItem : [],
			isAllSelected : false,
			theState:0,//正常为0，删除为1
			tgpf_BasicAccountVoucherList:[
			],
			editDisabled : true,
			delDisabled : true,
			applyDate : "",
			busiStateList : [
				{tableId:"手续费",theName:"手续费"},
				{tableId:"结息",theName:"结息"},
				{tableId:"利息划转",theName:"利息划转"},
			],
			busiState : '',
			
			month : '',
			accountName : '',
			voucherType : '',
			propelDisabled : true
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			getSendForm : getSendForm,
            search:search,    //搜索资源列表
            reset:reset,      //重置
            listItemSelectHandle: listItemSelectHandle,
            indexMethod:indexMethod,
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
				this.voucherType = data.tableId;
			},
			busiStateEmpty : function(){
				this.voucherType =  null;
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
                elem: '#basicAccountNoListDate',
                type: 'month',
                done:function(value){
                	/*var month = value.split('-');
                	listVue.month = month[1];*/
                	listVue.month = value;
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
        this.keyword= "";
        this.month = "";
        this.voucherType= "";
        this.accountName= "";
    }
	 
	//列表操作--------------获取"搜索列表"表单参数
	function getSearchForm()
	{
		return {
            interfaceVersion:listVue.interfaceVersion,
            pageNumber:listVue.pageNumber,
            countPerPage:listVue.countPerPage,
            totalPage:listVue.totalPage,
            month:listVue.month,
            accountName:listVue.accountName,
            voucherType : listVue.voucherType,
            theState : 0
		}
	}
	
	function getSendForm()
	{
		return {
            interfaceVersion:listVue.interfaceVersion,
            pageNumber:1,
            countPerPage:99999,
            totalPage:99999,
            month:listVue.month,
            accountName:listVue.accountName,
            voucherType : listVue.voucherType,
            theState : 0
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
                listVue.tgpf_BasicAccountVoucherList=jsonObj.tgpf_BasicAccountList;
                listVue.tgpf_BasicAccountVoucherList.forEach(function(item){
                	item.totalTradeAmount = addThousands(item.totalTradeAmount)
                })
                
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
	function getPropelForm(){
    	return {
    		interfaceVersion:listVue.interfaceVersion,
    		idArr : listVue.sendItem
    	}
    }
    //推送
    function handlePropel(){
    	
    	new ServerInterface(baseInfo.listInterface).execute(listVue.getSendForm(), function(jsonObj){
            if(jsonObj.result == "success")
            {
            	listVue.sendItem = [];
            	jsonObj.tgpf_BasicAccountList.forEach(function(item){
            		listVue.sendItem.push(item.tableId)
                });
                
            	console.log(listVue.sendItem);
                new ServerInterface(baseInfo.propelInterface).execute(listVue.getPropelForm(), function(jsonObj){
                    if(jsonObj.result != "success")
                    {
                    	jsonObj.info = "未查询到推送单据！";
                        generalErrorModal(jsonObj);
                    }
                    else
                    {
                    	generalSuccessModal();
                    	listVue.refresh();
                        listVue.selectedItem=[];
                        listVue.sendItem = [];
                    }
                });
            }
        });
    	
    	
    }
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
//	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tgpf_BasicAccountListDiv",
	"deleteDivId" : "#deleteSpecialFundoList",
	"listInterface":"../Tgpf_BasicAccountList",
	"batchDeleteInterface" : "../Tgpf_SpecialFundAppropriated_AFBatchDelete",//删除接口
	"propelInterface" : "../Tgpf_BasicAccountSend"
});