(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tgpj_BldLimitAmountVer_AFDtlModel: {},
			tableId : 1,
            nodeVersionList:[],

            pageNumber : 1,
            countPerPage : 20,
            totalPage : 1,
            totalCount : 1,
            keyword : "",
            selectItem : [],
            isAllSelected : false,
            theState:0,//正常为0，删除为1
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			//更新
			getUpdateForm : getUpdateForm,
			update: update,
            indexMethod: indexMethod,
            listItemSelectHandle:listItemSelectHandle,
            checkAllClicked : checkAllClicked,
            changePageNumber : function(data){
                detailVue.pageNumber = data;
            },
            addNodeHandle:addNodeHandle,
            nodeEditHandle:nodeEditHandle,
            deleteNodeHandle:deleteNodeHandle,
		},
		computed:{
			 
		},
		components : {
            'vue-nav' : PageNavigationVue
		},
		watch:{
//            pageNumber : refresh,
		}
	});

	//------------------------方法定义-开始------------------//
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:this.tableId,
		}
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
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				detailVue.tgpj_BldLimitAmountVer_AFDtlModel = jsonObj.tgpj_BldLimitAmountVer_AFDtl;
				getNodeVersionList()
			}
		});
	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getUpdateForm()
	{
		return {
            //附件材料
			busiType:'XX',
            sourceId : detailVue.tableId,
            generalAttachmentList : this.$refs.listenUploadData.uploadData,

			interfaceVersion:this.interfaceVersion,
			theState:this.theState,
			bldLimitAmountVerMngId:this.bldLimitAmountVerMngId,
			stageName:this.stageName,
			limitedAmount:this.limitedAmount,
		}
	}

	function update()
	{
		new ServerInterfaceJsonBody(baseInfo.updateInterface).execute(detailVue.getUpdateForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				$(baseInfo.detailDivId).modal('hide');
				refresh();
			}
		});
	}

    function indexMethod(index) {
        return generalIndexMethod(index, detailVue)
    }

    function listItemSelectHandle(list) {
        generalListItemSelectHandle(detailVue,list)
    }

    //列表操作--------------“全选”按钮被点击
    function checkAllClicked()
    {
        if(detailVue.selectItem.length == detailVue.nodeVersionList.length)
        {
            detailVue.selectItem = [];
        }
        else
        {
            detailVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
            detailVue.nodeVersionList.forEach(function(item) {
                detailVue.selectItem.push(item.tableId);
            });
        }
    }

    function getNodeVersionList() {
        serverRequest(baseInfo.getNodeList,getSearchListForm(),function(jsonObj){
            detailVue.nodeVersionList=jsonObj.tgpj_BldLimitAmountVer_AFDtlList;
            detailVue.pageNumber=jsonObj.pageNumber;
            detailVue.countPerPage=jsonObj.countPerPage;
            detailVue.totalPage=jsonObj.totalPage;
            detailVue.totalCount = jsonObj.totalCount;
            detailVue.keyword=jsonObj.keyword;
            detailVue.selectedItem=[];

        })
    }

    function addNodeHandle() {

    }

    function nodeEditHandle() {

    }

    function deleteNodeHandle() {

    }

	function initData()
	{
		getIdFormTab("",function (tableId) {
			detailVue.tableId=tableId
			refresh()
        })
		
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	// detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tgpj_BldLimitAmountVer_AFDtlDiv",
	"detailInterface":"../Tgpj_BldLimitAmountVer_AFDtlDetail",
	"updateInterface":"../Tgpj_BldLimitAmountVer_AFDtlUpdate"
});
