(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			tgpj_BldLimitAmountVer_AFModel: {
				isUsing:"0"
			},
            beginExpirationDateString:"",
            endExpirationDateString:"",

            //附件材料
			busiType:'06010102',
            loadUploadList: [],
            showDelete : true,  //附件是否可编辑
			theTypeId:"",
			theTypeList:[],
			theTypeValue:"",


            //列表方面的参数
            nodeVersionList:[],
            pageNumber : 1,
            countPerPage : 20,
            totalPage : 1,
            totalCount : 1,
            keyword : "",
            selectItem : [],
            isAllSelected : false,
            theState:0,//正常为0，删除为1

			//增加节点模态框
            clickType:"",
            stageName:"",
            limitedAmount:"",
            remark:"",

            buttonType : "",
            selectIndex:"",
            modelTitle:"",
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			//添加
			getAddForm : getAddForm,
			add: add,
			changeTheTypeListener:changeTheTypeListener,

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
            // clickListener:clickListener,
		},
		computed:{

		},
        components: {
            'vue-nav': PageNavigationVue,
            "my-uploadcomponent": fileUpload,
            'vue-select': VueSelect,
        },
		watch:{

		},
        mounted: function ()
        {
            laydate.render({
                elem: '#date0601010201'
            });
            // laydate.render({
            //     range:true,
            //     elem: '#choosBldLimitVersion',
				// done:function(value1,value2,value3){
            //         //todo
            //         var temp=value1.split(" - ")
            //         console.log(temp)
            //         detailVue.beginExpirationDateString=temp[0]
            //         detailVue.endExpirationDateString=temp[1]
            //         // console.log(value1)
            //         // console.log(value2)
            //         // console.log(value3)
            //     	// console.log('value1 is '+value1+" value2 is "+value2)
            //     // done: (value) => {
            //     // listVue.applyDate = value
            //     }
            // })
        }
	});

	//------------------------方法定义-开始------------------//
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:detailVue.interfaceVersion,
		}
	}

	//详情操作--------------
	function refresh()
	{

	}

	//详情更新操作--------------获取"更新机构详情"参数
	function getAddForm()
	{
		return {
			interfaceVersion:detailVue.interfaceVersion,
			theState:detailVue.tgpj_BldLimitAmountVer_AFModel.theState,
			// busiState:detailVue.tgpj_BldLimitAmountVer_AFModel.busiState,
			eCode:detailVue.tgpj_BldLimitAmountVer_AFModel.eCode,
			// userStartId:detailVue.tgpj_BldLimitAmountVer_AFModel.userStartId,
			theName:detailVue.tgpj_BldLimitAmountVer_AFModel.theName,
			// theVerion:detailVue.tgpj_BldLimitAmountVer_AFModel.theVerion,
			theType:detailVue.theTypeValue,
            beginExpirationDateString:$('#date0601010201').val(),
            // endExpirationDateString:detailVue.endExpirationDateString,
			isUsing:detailVue.tgpj_BldLimitAmountVer_AFModel.isUsing,
			buttonType:detailVue.buttonType,


	            //附件材料
            busiType : '06010102',
            generalAttachmentList : this.$refs.listenUploadData.uploadData,
            nodeVersionList:detailVue.nodeVersionList
		}
	}

	function add(buttonType)
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
		serverBodyRequest(baseInfo.addInterface,detailVue.getAddForm(),function (jsonObj) {
            generalSuccessModal()
            enterNewTabCloseCurrent(jsonObj.tableId,"受限额度节点详情","tgpj/Tgpj_BldLimitAmountVer_AFDetail.shtml")
        })
		// new ServerInterfaceJsonBody(baseInfo.addInterface).execute(detailVue.getAddForm(), function(jsonObj)
		// {
		// 	if(jsonObj.result != "success")
		// 	{
		// 		// noty({"text":jsonObj.info,"type":"error","timeout":2000});
		// 		generalErrorModal(jsonObj)
		// 	}
		// 	else
		// 	{
		// 		// refresh();
		// 		generalSuccessModal()
         //
        //
		// 	}
		// });
	}

	function getTypeList() {
        generalGetParamList("5", function (list) {
            detailVue.theTypeList = list
        })
    }

    function changeTheTypeListener(data) {
		console.log('data is ')
		console.log(data)
		detailVue.theTypeValue=data.theValue
        detailVue.theTypeId=data.tableId
		console.log('detailVue.theTypeValue is '+detailVue.theTypeValue)

    }

    function clickListener(type) {
        detailVue.clickType=type
        if(type==1){
            detailVue.remark=""
            detailVue.stageName=""
            detailVue.limitedAmount=""
            detailVue.modelTitle="新增受限额度节点"
        }else{
            var item=detailVue.selectItem[0]
            var selectNode=item
            detailVue.remark=item.remark
            detailVue.stageName=item.stageName
            detailVue.limitedAmount=item.limitedAmount
            detailVue.modelTitle="修改受限额度节点"
            // var selectId=detailVue.selectItem[0]
            // console.log('selectId is ')
            // console.log(selectId)
            // var selectNode={}
            // for(var i=0;i<detailVue.nodeVersionList.length;i++){
            //     var node=detailVue.nodeVersionList[i]
            //     console.log(node)
            //     if(selectId==node.tableId){
            //         selectNode=node
            //         break
            //     }
            // }
            // detailVue.remark=selectNode.remark
            // detailVue.stageName=selectNode.stageName
            // detailVue.limitedAmount=selectNode.limitedAmount
        }
    }

    function indexMethod(index) {
        return generalIndexMethod(index, detailVue)
    }

    function listItemSelectHandle(list) {
        var isFound=false
        for(var i=0;i<detailVue.nodeVersionList.length;i++){
            var item=detailVue.nodeVersionList[i]
            if(list[0]==undefined){
                isFound=false
            }else{
                if(item.stageName==list[0].stageName && item.limitedAmount==list[0].limitedAmount){
                    detailVue.selectIndex=i
                    isFound=true
                    console.log('found index is '+i)
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
	    console.log('detailVue.limitedAmount is '+detailVue.limitedAmount)
	    console.log('detailVue.stageName is '+detailVue.stageName)
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
		generalSelectModal(function () {
			var selectList=detailVue.selectItem
			for(var i=0;i<selectList.length;i++){
                detailVue.nodeVersionList.remove(selectList[i])
			}
        },"确认删除该节点吗？")
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
		generalLoadFile2(detailVue,detailVue.busiType)
		getTypeList()
	}
	//------------------------方法定义-结束------------------//

	//------------------------数据初始化-开始----------------//
	detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#tgpj_BldLimitAmountVer_AFAddDiv",
	"detailInterface":"../Tgpj_BldLimitAmountVer_AFAddDetail",
	"addInterface":"../Tgpj_BldLimitAmountVer_AFAdd"
});
