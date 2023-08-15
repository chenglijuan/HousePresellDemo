(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tgpj_BldLimitAmountVer_AFModel: {},
			tableId : 1,
			isUsing:"",
			houseType:"",
			nodeVersionList:[],
			
            pageNumber : 1,
            countPerPage : MAX_VALUE,
            totalPage : 1,
            totalCount : 1,
            keyword : "",
            selectItem : [],
            isAllSelected : false,
            theState:0,//正常为0，删除为1
            theTypeName:"",

            //附件材料
			busiType:'06010102',
            loadUploadList: [],
            showDelete : false,  //附件是否可编辑

		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
            indexMethod: indexMethod,
            listItemSelectHandle:listItemSelectHandle,
            checkAllClicked : checkAllClicked,
            changePageNumber : function(data){
                detailVue.pageNumber = data;
            },
            mainEditHandle:mainEditHandle,
		},
		computed:{
			 
		},
		components : {
            'vue-nav' : PageNavigationVue,
            "my-uploadcomponent":fileUpload,
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
			interfaceVersion:detailVue.interfaceVersion,
			tableId:detailVue.tableId,
		}
	}

	//详情操作--------------
	function refresh()
	{
		if (detailVue.tableId == null || detailVue.tableId < 1)
		{
			return;
		}

		getDetail();
        loadUpload()
	}

	function getDetail()
	{
	    serverRequest(baseInfo.detailInterface,detailVue.getSearchForm(),function (jsonObj) {
            detailVue.tgpj_BldLimitAmountVer_AFModel = jsonObj.tgpj_BldLimitAmountVer_AF;
            detailVue.isUsing=isUseNumber2String(detailVue.tgpj_BldLimitAmountVer_AFModel.theState)
            detailVue.theTypeName=jsonObj.parameterName
            detailVue.houseType=houseType2String(detailVue.tgpj_BldLimitAmountVer_AFModel)
            getNodeVersionList()
        })
		// new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function(jsonObj)
		// {
		// 	if(jsonObj.result != "success")
		// 	{
		// 		noty({"text":jsonObj.info,"type":"error","timeout":2000});
		// 	}
		// 	else
		// 	{
		// 		detailVue.tgpj_BldLimitAmountVer_AFModel = jsonObj.tgpj_BldLimitAmountVer_AF;
		// 		detailVue.isUsing=isUseNumber2String(detailVue.tgpj_BldLimitAmountVer_AFModel.theState)
         //        detailVue.theTypeName=jsonObj.parameterName
		// 		// if(detailVue.tgpj_BldLimitAmountVer_AFModel.theState==="0"){
		// 		// 	detailVue.isUsing="是"
		// 		// }else{
         //         //    detailVue.isUsing="否"
		// 		// }
		// 		detailVue.houseType=houseType2String(detailVue.tgpj_BldLimitAmountVer_AFModel)
		// 		getNodeVersionList()
		// 	}
		// });
	}

    function getSearchListForm()
    {
        return {
            interfaceVersion:detailVue.interfaceVersion,
            pageNumber:detailVue.pageNumber,
            countPerPage:detailVue.countPerPage,
            totalPage:detailVue.totalPage,
            keyword:detailVue.keyword,
            theState:detailVue.theState,
            userStartId:detailVue.userStartId,
            userRecordId:detailVue.userRecordId,
            bldLimitAmountVerMngId:detailVue.tableId,
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

    function mainEditHandle(tableId) {
		console.log('mainEditHandle tableId is '+tableId)
		enterNewTabCloseCurrent(tableId,"受限额度节点编辑","tgpj/Tgpj_BldLimitAmountVer_AFEdit.shtml")
    }

    function loadUpload() {
        generalLoadFile2(detailVue,detailVue.busiType)
    }


    function initData()
	{
        getIdFormTab("",function (tableId) {
            detailVue.tableId=tableId
			console.log('tableId is '+detailVue.tableId)
			refresh()
        })
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	// detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tgpj_BldLimitAmountVer_AFDetailDiv",
	"detailInterface":"../Tgpj_BldLimitAmountVer_AFDetail",
	"getNodeList":"../Tgpj_BldLimitAmountVer_AFDtlList"
});
