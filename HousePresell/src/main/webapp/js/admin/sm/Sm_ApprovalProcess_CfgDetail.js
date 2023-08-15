(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
            theState:0,//正常为0，删除为1
			sm_ApprovalProcess_CfgModel: {},
            sm_ApprovalProcess_NodeList:[],
			tableId : 1,
            thetemplate:[],
            OrderIndexArr: [],
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
            sm_ApprovalProcess_CfgEdit:sm_ApprovalProcess_CfgEdit,
            // 合并单元格
            objectSpanMethod:objectSpanMethod,

		},
		computed:{
			 
		},
		components : {

		},
		watch:{
			
		}
	});

	//------------------------方法定义-开始------------------//

    function initData()
    {
        getIdFormTab("",function (tableId) {
            detailVue.tableId=tableId;
            refresh()
        })
    }

	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:detailVue.interfaceVersion,
			tableId:detailVue.tableId,
            theState : detailVue.theState,
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
	}

	function getDetail()
	{
		new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
                generalErrorModal(jsonObj);
			}
			else
			{
                detailVue.OrderIndexArr = [];
				detailVue.sm_ApprovalProcess_CfgModel = jsonObj.sm_ApprovalProcess_CfgDetail;
                detailVue.sm_ApprovalProcess_NodeList = jsonObj.sm_ApprovalProcess_NodeList;
                getOrderNumber(detailVue.sm_ApprovalProcess_NodeList,detailVue.OrderIndexArr);
                detailVue.sm_ApprovalProcess_NodeList[0].orderNumber = 1+"（起始节点）";
			}
		});
	}

	//合并单元格
    function objectSpanMethod(val)
	{
        // val => {row,column,rowIndex,columnIndex}
		var rowIndex = val.rowIndex;
		var columnIndex = val.columnIndex;
        if(columnIndex === 0 || columnIndex === 1 || columnIndex === 2 || columnIndex === 5 || columnIndex === 6 || columnIndex === 7 ) {
            for(var i = 0; i < detailVue.OrderIndexArr.length; i++) {
                var element = detailVue.OrderIndexArr[i]
                for(var j = 0; j < element.length; j++) {
                    var item = element[j]
                    if(rowIndex == item) {
                        if(j == 0) {
                            return {
                                rowspan: element.length,
                                colspan: 1
                            }
                        } else if(j != 0) {
                            return {
                                rowspan: 0,
                                colspan: 0
                            }
                        }
                    }
                }
            }
        }
    }

    //修改
    function sm_ApprovalProcess_CfgEdit()
    {
    	var tableId = detailVue.tableId;
        enterNewTabCloseCurrent(tableId,"审批流程设置修改",'sm/Sm_ApprovalProcess_CfgEdit.shtml');
    }

	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#sm_ApprovalProcess_CfgDetailDiv",
	"detailInterface":"../Sm_ApprovalProcess_CfgDetail"
});
