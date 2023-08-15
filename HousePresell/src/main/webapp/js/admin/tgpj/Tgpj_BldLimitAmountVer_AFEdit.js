(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tgpj_BldLimitAmountVer_AFModel: {},
			tableId : 1,
            //上方编辑的参数
            beginExpirationDateString:"",
            endExpirationDateString:"",

            //列表方面的参数
            nodeVersionList:[],
            pageNumber : 1,
            countPerPage : MAX_VALUE,
            totalPage : 1,
            totalCount : 1,
            keyword : "",
            selectItem : [],
            isAllSelected : false,
            theState:0,//正常为0，删除为1

            clickType:"",

            stageName:"",
            limitedAmount:"",
			remark:"",
            parameterNameList:[],
            theTypeId:"",
            theTypeList:[],
            theTypeValue:"",

            //附件材料
			busiType:'06010102',
            loadUploadList: [],
            showDelete : true,

            buttonType : "",
            modelTitle:"",
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
            clickListener:clickListener,
            dialogSave:dialogSave,
            changeTheTypeListener:changeTheTypeListener,
		},
		computed:{
			 
		},
        components: {
            'vue-nav': PageNavigationVue,
            "my-uploadcomponent": fileUpload,
            'vue-select': VueSelect,
        },
        watch:{
//            pageNumber : refresh,
        },
        mounted: function ()
        {
            laydate.render({
                elem: '#date0601010202'
            });
            // laydate.render({
            //     range:true,
            //     elem: '#editDuring',
            //     done:function(value1,value2,value3){
            //         //todo
            //         var temp=value1.split(" - ")
            //         console.log(temp)
            //         detailVue.beginExpirationDateString=temp[0]
            //         detailVue.endExpirationDateString=temp[1]
            //     }
            // })
        }
	});

    // var buildingHouseNode=jsonDeepCopy(BUILDING_HOUSE_NODE)
    // var completeHouseNode=jsonDeepCopy(COMPLETE_HOUSE_NODE)

	//------------------------方法定义-开始------------------//
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:detailVue.interfaceVersion,
			tableId:detailVue.tableId,
		}
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
            console.log('get nodeVersionList is ')
            console.log(detailVue.nodeVersionList)

        })
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

	function loadUpload() {
        generalLoadFile2(detailVue,detailVue.busiType)
    }

	function getDetail()
	{
	    serverRequest(baseInfo.detailInterface,detailVue.getSearchForm(),function(jsonObj){
            detailVue.tgpj_BldLimitAmountVer_AFModel = jsonObj.tgpj_BldLimitAmountVer_AF;
            console.log('tgpj_BldLimitAmountVer_AFModel is ')
            console.log(detailVue.tgpj_BldLimitAmountVer_AFModel)
            //todo 受限额度rebuild加上theType
            // if(detailVue.tgpj_BldLimitAmountVer_AFModel.theType==0){
            //     detailVue.parameterNameList=buildingHouseNode
            // }else{
            //     detailVue.parameterNameList=completeHouseNode
            // }
            // var temp=detailVue.tgpj_BldLimitAmountVer_AFModel.timeDuring.split(" - ")
            // detailVue.beginExpirationDateString=temp[0]
            // detailVue.endExpirationDateString=temp[1]
            detailVue.beginExpirationDateString=detailVue.tgpj_BldLimitAmountVer_AFModel.timeDuring
            for(var i=0;i<detailVue.theTypeList.length;i++){
                var item=detailVue.theTypeList[i]
                console.log('theType item is ')
                console.log(item)
                if(item.theValue==detailVue.tgpj_BldLimitAmountVer_AFModel.theType){
                    detailVue.theTypeId=item.tableId
                    detailVue.theTypeValue=item.theValue
                    break
                }
            }
            getNodeVersionList()
        })
	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getUpdateForm()
	{
	    console.log('detailVue.nodeVersionList is ')
        console.log(detailVue.nodeVersionList)
		return {
            //附件材料
            busiType : '06010102',
            sourceId : detailVue.tableId,
            generalAttachmentList : this.$refs.listenUploadData.uploadData,

			interfaceVersion:detailVue.interfaceVersion,
			theState:detailVue.tgpj_BldLimitAmountVer_AFModel.theState,
			tableId:detailVue.tableId,
			busiState:detailVue.tgpj_BldLimitAmountVer_AFModel.busiState,
			eCode:detailVue.tgpj_BldLimitAmountVer_AFModel.eCode,
			userStartId:detailVue.tgpj_BldLimitAmountVer_AFModel.userStartId,
			createTimeStamp:detailVue.tgpj_BldLimitAmountVer_AFModel.createTimeStamp,
			lastUpdateTimeStamp:detailVue.tgpj_BldLimitAmountVer_AFModel.lastUpdateTimeStamp,
			userRecordId:detailVue.tgpj_BldLimitAmountVer_AFModel.userRecordId,
			recordTimeStamp:detailVue.tgpj_BldLimitAmountVer_AFModel.recordTimeStamp,
			theName:detailVue.tgpj_BldLimitAmountVer_AFModel.theName,
			theVerion:detailVue.tgpj_BldLimitAmountVer_AFModel.theVerion,
			theType:detailVue.theTypeValue,
			limitedAmountInfoJSON:detailVue.tgpj_BldLimitAmountVer_AFModel.limitedAmountInfoJSON,
            beginExpirationDateString:$('#date0601010202').val(),
            // endExpirationDateString:detailVue.endExpirationDateString,
            isUsing:detailVue.tgpj_BldLimitAmountVer_AFModel.isUsing,
            buttonType:detailVue.buttonType,

            nodeVersionList:detailVue.nodeVersionList,


		}
	}

	function update(buttonType)
	{
        detailVue.buttonType=buttonType
        var isHasHundred=false
        var isHasZero=false
        for(var i=0;i<detailVue.nodeVersionList.length;i++){
            var item =detailVue.nodeVersionList[i]
            if(item.limitedAmount==100){
                isHasHundred=true
            }else if(item.limitedAmount==0){
                isHasZero=true
            }
        }
        if(!isHasHundred){
            generalErrorModal(undefined,"缺少受限比例为100%的节点")
            return
        }
        if(!isHasZero){
            generalErrorModal(undefined,"缺少受限比例为0%的节点")
            return
        }
        serverBodyRequest(baseInfo.updateInterface,detailVue.getUpdateForm(),function (jsonObj) {
            generalSuccessModal()
            enterNewTabCloseCurrent(jsonObj.tableId,"受限额度节点详情","tgpj/Tgpj_BldLimitAmountVer_AFDetail.shtml")
        })
		// new ServerInterfaceJsonBody(baseInfo.updateInterface).execute(detailVue.getUpdateForm(), function(jsonObj)
		// {
		// 	if(jsonObj.result != "success")
		// 	{
		// 		// noty({"text":jsonObj.info,"type":"error","timeout":2000});
         //        generalErrorModal(jsonObj)
		// 	}
		// 	else
		// 	{
		// 		// $(baseInfo.detailDivId).modal('hide');
         //        generalSuccessModal()
         //        enterNewTabCloseCurrent(jsonObj.tableId,"受限额度节点详情","tgpj/Tgpj_BldLimitAmountVer_AFDetail.shtml")
		// 		// refresh();
		// 	}
		// });
	}

    function indexMethod(index) {
        return generalIndexMethod(index, detailVue)
    }

    function listItemSelectHandle(list) {
	    console.log()
        var isFound=false
        for(var i=0;i<detailVue.nodeVersionList.length;i++){
            var item=detailVue.nodeVersionList[i]
            if(list[0]==undefined){
                isFound=false
            }else{
                if(item.stageName==list[0].stageName && item.limitedAmount==list[0].limitedAmount){
                    detailVue.selectIndex=i
                    isFound=true
                    // console.log('found index is '+i)
                }
            }
        }
        if(!isFound){
            detailVue.selectIndex=-1
        }
        generalListItemSelectWholeItemHandle(detailVue,list)
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

    function compareFunctiion(obj1, obj2) {
        var val1 = Number.parseFloat(obj1.limitedAmount);
        var val2 = Number.parseFloat(obj2.limitedAmount);
        if (val1 < val2) {
            return -1;
        } else if (val1 > val2) {
            return 1;
        } else {
            return 0;
        }
    }

    function judgeNodeCanAdd(item) {
        if(detailVue.limitedAmount==""){
            generalErrorModal(undefined,"请输入受限额度")
            return false
        }
        if(detailVue.stageName==""){
            generalErrorModal(undefined,"请输入受限节点名称")
            return false
        }
        if(detailVue.limitedAmount>100 || detailVue.limitedAmount<0){
            generalErrorModal(undefined,"受限额度输入不正确")
            return false
        }
        for(var i=0;i<detailVue.nodeVersionList.length;i++){
            if(i==detailVue.selectIndex){
                continue
            }
            var tempItem=detailVue.nodeVersionList[i]
            if(tempItem.limitedAmount==item.limitedAmount){
                generalErrorModal(undefined,"该受限额度比例已存在")
                return false
            }
        }
        for(var i=0;i<detailVue.nodeVersionList.length;i++){
            if(i==detailVue.selectIndex){
                continue
            }
            var tempItem=detailVue.nodeVersionList[i]
            if(tempItem.stageName==item.stageName){
                generalErrorModal(undefined,"该受限额度名称已存在")
                return false
            }
        }
        return true
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
        var item={
            limitedAmount:detailVue.limitedAmount,
            stageName:detailVue.stageName,
            remark:detailVue.remark,
            bldLimitAmountVerMngId:detailVue.tableId,
        }
        if(!judgeNodeCanAdd(item)){
            return
        }
        detailVue.nodeVersionList.push(item)
        detailVue.nodeVersionList.sort(compareFunctiion)
        $('#addNode').modal('hide');
    }

    function nodeEditHandle() {
        var item={
            limitedAmount:detailVue.limitedAmount,
            stageName:detailVue.stageName,
            remark:detailVue.remark,
            bldLimitAmountVerMngId:detailVue.tableId,
        }
        if(!judgeNodeCanAdd(item)){
            return
        }
        detailVue.nodeVersionList[detailVue.selectIndex]=item
        detailVue.nodeVersionList.sort(compareFunctiion)
        $('#addNode').modal('hide');
    }

    function deleteNodeHandle() {
        // generalDeleteModal(detailVue,"Tgpj_BldLimitAmountVer_AFDtl")
        // for(var i=0;i<nodeVersionList.length;i++)
        // {
        //     if()
        // }

        generalSelectModal(function () {
            var selectList=detailVue.selectItem;
            console.log(detailVue.selectItem)
            console.log(detailVue.nodeVersionList)
            for(var i=0;i<selectList.length;i++){
                detailVue.nodeVersionList.remove(selectList[i])
            }

        },"确认删除该节点吗？")
    }

    function clickListener(type) {
        detailVue.clickType=type
        if(type==1){
            detailVue.remark=""
            detailVue.stageName=""
            detailVue.limitedAmount=""
            detailVue.modelTitle="新增受限额度节点"
        }else{
            var selectId=detailVue.selectItem[0]
            var selectNode={}
            for(var i=0;i<detailVue.nodeVersionList.length;i++){
                var node=detailVue.nodeVersionList[i]
                console.log(node)
                if(selectId==node.tableId){
                    selectNode=node
                    break
                }
            }
            detailVue.remark=selectNode.remark
            detailVue.stageName=selectNode.stageName
            detailVue.limitedAmount=selectNode.limitedAmount
            detailVue.modelTitle="修改受限额度节点"
        }
    }

    function changeTheTypeListener(data) {
        console.log('data is ')
        console.log(data)
        detailVue.theTypeValue=data.theValue
        detailVue.theTypeId=data.tableId
        console.log('detailVue.theTypeValue is '+detailVue.theTypeValue)

    }

    function getTypeList() {
        generalGetParamList("5", function (list) {
            detailVue.theTypeList = list
            console.log('theType list is ')
            console.log(list)
            refresh()
        })
    }

    function dialogSave() {
        if(detailVue.clickType==1){
            addNodeHandle()
        }else if(detailVue.clickType==2){
            nodeEditHandle()
        }
    }

    function initData()
    {
        getIdFormTab("",function (tableId) {
            detailVue.tableId=tableId
            getTypeList()
            // refresh()
        })


    }
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	// detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tgpj_BldLimitAmountVer_AFEditDiv",
	"detailInterface":"../Tgpj_BldLimitAmountVer_AFDetail",
	"updateInterface":"../Tgpj_BldLimitAmountVer_AFUpdate",
    "getNodeList":"../Tgpj_BldLimitAmountVer_AFDtlList",
    "deleteInterface":"../Tgpj_BldLimitAmountVer_AFDtlDelete",
    "batchDeleteInterface":"../Tgpj_BldLimitAmountVer_AFDtlBatchDelete",
	"nodeAddInterface":"../Tgpj_BldLimitAmountVer_AFDtlAdd",
	"nodeUpdateInterface":"../Tgpj_BldLimitAmountVer_AFDtlUpdate",
});
