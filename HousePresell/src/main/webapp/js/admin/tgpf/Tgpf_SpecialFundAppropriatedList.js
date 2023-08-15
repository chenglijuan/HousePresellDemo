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
			tgpf_SpacialFundAppropriatedList:[
			],
			editDisabled : true,
			delDisabled : true,
			batDisabled :true,
			applyDate : "",
			timeStampStart : '',
			timeStampEnd : '',
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
			exportForm: exportForm,
			getExportForm: getExportForm,
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
			specialFundAppropriatedAddHandle : specialFundAppropriatedAddHandle,//新增界面
			specialFundAppropriatedEditHandle : specialFundAppropriatedEditHandle,//修改界面
			showDelModal : showDelModal,//删除
			getDeleteForm : getDeleteForm,//获取删除参数
			tgpf_SpecialFundDel : tgpf_SpecialFundDel,//确认删除
			showBatModal : showBatModal,//批量下载
			getBatForm : getBatForm,//获取下载参数
			tgpf_SpecialFundBat : tgpf_SpecialFundBat,//确认批量下载
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue
		},
		watch:{
			//pageNumber : refresh,
			selectItem : selectedItemChanged,
		},
        mounted: function ()
        {
            // laydate.render({
            //     elem: '#specialFundAppropriatedListDate',
            //     done:function(value){
            //     	listVue.applyDate = value;
            //     }
            // })


			laydate.render({
				elem: '#specialFundAppropriatedListDate',
				range: '~',
				done: function(value, date, endDate){
					var arr = value.split("~");

					listVue.timeStampStart = arr[0];
					listVue.timeStampEnd = arr[1];
				}
			});

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
        this.applyDate = null;
		this.timeStampStart = ""
		this.timeStampEnd = ""
		$("#specialFundAppropriatedListDate").val("");
    }
	 
	//列表操作--------------获取"搜索列表"表单参数
	function getSearchForm()
	{
		return {
            interfaceVersion:listVue.interfaceVersion,
            pageNumber:listVue.pageNumber,
            countPerPage:listVue.countPerPage,
            totalPage:listVue.totalPage,
            theState:listVue.theState,
            keyword:listVue.keyword,
            applyDate : listVue.applyDate,
			timeStampStart: listVue.timeStampStart,
			timeStampEnd: listVue.timeStampEnd,
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
                listVue.tgpf_SpacialFundAppropriatedList=jsonObj.tgpf_SpecialFundAppropriated_AFList;
                
                listVue.pageNumber=jsonObj.pageNumber;
                listVue.countPerPage=jsonObj.countPerPage;
                listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
                listVue.keyword=jsonObj.keyword;
                listVue.selectedItem=[];

            }
        });
    }

	function exportForm(){
		new ServerInterface(baseInfo.exportInterface).execute(listVue.getSearchForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj ,jsonObj.info);
			}
			else
			{
				window.location.href="../"+jsonObj.fileURL;
			}
		});
	}

	function getExportForm() {
		return {
			interfaceVersion: this.interfaceVersion,

		}
	}

    //列表索引方法
    function indexMethod(index)
    {
        return generalIndexMethod(index, listVue)
    }

    function listItemSelectHandle(list)
    {
    	listVue.selectItem  = list;
    	//修改
    	if(list.length == 1){
    		
    		if(list[0].busiState != "1")
    		{
    			listVue.editDisabled = true;
    		}
    		else
    		{
    			listVue.editDisabled = false;
    		}
    		
    	}else{
    		listVue.editDisabled = true;
    	}
    	
    	//删除
    	if(list.length >= 1){
    		console.log(listVue.selectItem);
    		for( var i = 0 ; i < listVue.selectItem.length ; i++)
    		{
				if(listVue.selectItem[i].busiState != "1")
				{
					listVue.delDisabled = true;
					break;
				}
				else
				{
					listVue.delDisabled = false;
				}
			}
    		
    	}else{
    		listVue.delDisabled = true;
    	}

		//下载 拨付完成的
		if(list.length == 1){

			if(list[0].applyState != "2")
			{
				listVue.batDisabled = true;
			}
			else
			{
				listVue.batDisabled = false;
			}

		}else{
			listVue.batDisabled = true;
		}

    	
        generalListItemSelectHandle(listVue,list);
    }


    //选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		listVue.isAllSelected = (listVue.tgpf_SpacialFundAppropriatedList.length > 0)
		&&	(listVue.selectItem.length == listVue.tgpf_SpacialFundAppropriatedList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgpf_SpacialFundAppropriatedList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgpf_SpacialFundAppropriatedList.forEach(function(item) {
	    		listVue.selectItem.push(item.tableId);
	    	});
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

	//列表操作--------------获取“删除资源”表单参数
	function getBatForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			idArr:this.selectItem
		}
	}

	
	//列表操作-----------------点击删除
	function showDelModal(){
		$(baseInfo.deleteDivId).modal({
			backdrop :'static'
		})
	}

	//列表操作-----------------点击批量下载
	function showBatModal(){
		$(baseInfo.batDivId).modal({
			backdrop :'static'
		})
	}
	//列表操作-----------------点击确认删除
	function tgpf_SpecialFundDel(){
		$(baseInfo.deleteDivId).modal("hide");
		new ServerInterface(baseInfo.batchDeleteInterface).execute(listVue.getDeleteForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				generalSuccessModal();
				listVue.selectItem = [];
				refresh();
			}
		});
	}

	//列表操作-----------------点击批量下载
	function tgpf_SpecialFundBat(){
		$(baseInfo.batDivId).modal("hide");

		$(baseInfo.batLoadDivId).modal({
			backdrop :'static'
		})

		new ServerInterface(baseInfo.batchDownloadInterface).execute(listVue.getBatForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				console.log("-----"+jsonObj.fileURL);
				// 如果是网络图片
				if(jsonObj.fileURL.indexOf("http://")!=-1){
					window.open(jsonObj.fileURL,"_blank");
				}else{
					window.open("../"+jsonObj.fileURL,"_blank");
				}

				$(baseInfo.batLoadDivId).modal("hide");
			}

		});
	}
	//列表操作-----------------点击新增
	function specialFundAppropriatedAddHandle(){
		enterNewTab('', '新增特殊资金拨付', 'tgpf/Tgpf_SpecialFundAppropriatedAdd.shtml');
	}
	//列表操作------------------点击修改
	function specialFundAppropriatedEditHandle(){
		enterNextTab(listVue.selectItem[0], '特殊资金拨付修改', 'tgpf/Tgpf_SpecialFundAppropriatedEdit.shtml',listVue.selectItem[0]+"061206");
	}
	
    //跳转方法 - 详情
    function tgpf_FundAppropriatedDetail(tableId)
    {
        enterNewTab(tableId, "特殊资金拨付详情", "tgpf/Tgpf_SpecialFundAppropriatedDetail.shtml");
    }
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tgpf_SpecialFundAppropriatedListDiv",
	"deleteDivId" : "#deleteSpecialFundoList",
	"batDivId" : "#batchDownloadList",
	"batLoadDivId" : "#deleteSpecialFundoLoad",
	"listInterface":"../Tgpf_SpecialFundAppropriated_AFList",
	"batchDeleteInterface" : "../Tgpf_SpecialFundAppropriated_AFBatchDelete",//删除接口
	"exportInterface": "../Tgpf_SpecialFundAppropriated_AFExport", //导出接口
	"batchDownloadInterface" : "../Tgpf_SpecialFund_batchDownload",//批量下载接口
});