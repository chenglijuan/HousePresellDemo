(function(baseInfo){
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
            theState:0,//正常为0，删除为1
            busiType:0, //启用为0，禁用为1
            busiId:"",
            busiCode:"",//业务编码
            sm_ApprovalProcess_CfgModel: {
                isNeedBackup : -1, //是否备案
            },
            type:"",
            selectItem:[],
            checked:[],
            lastNodeId:"", //上一个节点Id
            sm_ApprovalProcess_NodeModel:{}, //节点
            approvalModel : "",
            sm_ApprovalProcess_NodeList:[],//节点列表
            sm_ApprovalProcess_ModalNodeList:[],
            sm_ApprovalProcess_ConditionList:[],//条件列表
            sm_BaseParameterList:[],
            sm_Permission_RoleList:[],//角色列表
            sm_MessageTemplate_CfgList:[],//消息模板配置列表
            messageTemplate_cfgList:[],//选中的消息模板
            updateOrderNumber:"",//修改弹框 审批步骤
			roleId:"",
            idArr:[],
            disabled:true,
            upDisabled:true,
            deDisabled:true,
            oldFinishPercentage:"",
            oldPassPercentage:"",
            buttonType:"",
            nodeType:"", //0 :第一个结点 1:其他结点
		    thetemplate:[],
		    OrderIndexArr: [],
		},
		mounted: function () {
	    },
		methods : {
			initData: initData,
			getAddForm : getAddForm,
            selectedItemChanged :selectedItemChanged,
            checkAllClicked:checkAllClicked,
            showModal:showModal,
            getNodeDetail:getNodeDetail,
            getSm_BaseParameterForSelect:getSm_BaseParameterForSelect,
            baseParameterChange:baseParameterChange,
            getSm_PermissionRoleList:getSm_PermissionRoleList,
            getMessageTemplate_CfgList:getMessageTemplate_CfgList,
            sm_MessageTemplate_CfgDetail:sm_MessageTemplate_CfgDetail,
            // messageChange:messageChange,
            roleChange:roleChange,
            addSm_ApprovalProcess_Cfg:addSm_ApprovalProcess_Cfg,
            addSm_ApprovalProcess_Node:addSm_ApprovalProcess_Node,
            getSm_ApprovalProcess_NodeList:getSm_ApprovalProcess_NodeList,
            getDeleteForm:getDeleteForm,
            sm_ApprovalProcess_NodeDel : sm_ApprovalProcess_NodeDel,
            listItemSelectHandle: listItemSelectHandle,
            addRow:addRow,
			deleteRow:deleteRow,
	      	// 合并单元格
	      	objectSpanMethod: objectSpanMethod,

        },
		computed:{
			 
		},
		components : {
            'vue-select': VueSelect
		},
		watch:{
			//监听审批模式 抢占模式禁用‘完成阀值’和‘通过阀值’下拉框
            'approvalModel' : {
                handler:function (value) {
                    if(value == 1)
                    {
                        if(addVue.buttonType == 2)
                        {
                            addVue.sm_ApprovalProcess_NodeModel.finishPercentage = addVue.oldFinishPercentage;
                            addVue.sm_ApprovalProcess_NodeModel.passPercentage = addVue.oldPassPercentage;
                        }
                        addVue.disabled = false;
                    }
                    else
                    {
                        addVue.sm_ApprovalProcess_NodeModel.finishPercentage = "";
                        addVue.sm_ApprovalProcess_NodeModel.passPercentage = "";
                        addVue.disabled = true;
                    }
                },
                deep:true
            },
		}
	});

	//------------------------方法定义-开始------------------//

    function initData()
    {
        getSm_BaseParameterForSelect();
    }
    
    function getSm_BaseParameterForSelect()
    {
        generalGetParamList("1",function (list) {
            for(var i =0;i<list.length;i++){
                var item=list[i]
                item.theName=item.theValue+"-"+item.theName
            }
            addVue.sm_BaseParameterList =list;
            //加载完页面后 获取角色列表
            getSm_PermissionRoleList();
        })
    }

    function baseParameterChange(date)
    {
        addVue.busiId = date.tableId;
        addVue.busiCode = date.busiCode
        //根据业务编码加载 消息模板列表
        getMessageTemplate_CfgList();
    }

    function showModal()
    {
        addVue.buttonType = 1;
        addVue.sm_ApprovalProcess_NodeModel = [];
        addVue.approvalModel = 0;
        addVue.sm_ApprovalProcess_NodeModel.rejectModel = -1;//驳回模式
        addVue.sm_ApprovalProcess_ConditionList = [];
        addVue.checked = [];
        //添加第一个结点
    	if(addVue.sm_ApprovalProcess_ModalNodeList.length == 0)
		{
            addVue.sm_ApprovalProcess_NodeModel.orderNumber = 1+"(起始节点)";
		}
		else
		{
            addVue.sm_ApprovalProcess_NodeModel.orderNumber = addVue.sm_ApprovalProcess_ModalNodeList.length + 1;
		}
        addVue.roleId = "";
    }

    //获取角色列表
    function getSm_PermissionRoleList()
	{
        var model={
            interfaceVersion:addVue.interfaceVersion,
            theState:addVue.theState,
            busiType:addVue.busiType,
            pageNumber : 1,
            countPerPage : MAX_VALUE,
            totalPage : 1,
            totalCount : 1,
            keyword:"",
        }
        new ServerInterface(baseInfo.getRoleInterface).execute(model, function(jsonObj){
            if(jsonObj.result != "success")
            {
                parent.generalErrorModal(jsonObj);
            }
            else
			{
                addVue.sm_Permission_RoleList = jsonObj.sm_Permission_RoleList;
			}
        });
    }

    //获取消息模板配置列表
    function getMessageTemplate_CfgList()
    {
        var model= {
            interfaceVersion:addVue.interfaceVersion,
            pageNumber: 1,
            countPerPage: MAX_VALUE,
            theState: 0,
            totalPage: 1,
            keyword: "",
            busiCode:addVue.busiCode,
        }
        new ServerInterface(baseInfo.getMessageTemplateInterface).execute(model, function(jsonObj){
            if(jsonObj.result != "success")
            {
                parent.generalErrorModal(jsonObj);
            }
            else
            {
                addVue.sm_MessageTemplate_CfgList=jsonObj.sm_MessageTemplate_CfgList;
            }
        });
    }

    //合并单元格
    function objectSpanMethod(val)
    {
        // val => {row,column,rowIndex,columnIndex}
        var rowIndex = val.rowIndex;
        var columnIndex = val.columnIndex;
        if(columnIndex === 0 || columnIndex === 1 || columnIndex === 2 || columnIndex === 3 || columnIndex === 6 || columnIndex === 7 || columnIndex === 8) {
            for(var i = 0; i < this.OrderIndexArr.length; i++) {
                var element = this.OrderIndexArr[i]
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

    //进入详情
    function sm_MessageTemplate_CfgDetail(tableId)
    {
        $('#examine-add').modal('hide');
        parent.enterNewTabTest(tableId, "消息模板配置详情", "test/Test_MessageTemplate_CfgDetail.shtml");
    }

    function  roleChange(date)
	{
		addVue.roleId = date.tableId;
        addVue.sm_ApprovalProcess_NodeModel.roleId = date.tableId;
    }

    //表格增加一行
	function addRow()
	{
		var condition = {
			theContent:"",
            nextStep:"",
		}
       addVue.sm_ApprovalProcess_ConditionList.push(condition);
    }

    //表格删除当前行
    function deleteRow(index)
	{
        addVue.sm_ApprovalProcess_ConditionList.splice(index,1)
    }

    //buttonType = 1 新增结点
    //buttonType = 2 修改结点
    function addSm_ApprovalProcess_Node()
    {
        if(addVue.sm_ApprovalProcess_ModalNodeList.length == 0)
        {
            addVue.nodeType = 0 ;
        }
        else if(addVue.sm_ApprovalProcess_ModalNodeList.length > 0)
        {
            addVue.nodeType = 1 ;
        }
        var model = {
            interfaceVersion:addVue.interfaceVersion,
            tableId:addVue.sm_ApprovalProcess_NodeModel.tableId,
            theName:addVue.sm_ApprovalProcess_NodeModel.theName,
            eCode:addVue.sm_ApprovalProcess_NodeModel.eCode,
            roleId : addVue.sm_ApprovalProcess_NodeModel.roleId,
            lastNoteId:addVue.lastNodeId,
            approvalModel:addVue.approvalModel,
            finishPercentage:addVue.sm_ApprovalProcess_NodeModel.finishPercentage,
            passPercentage : addVue.sm_ApprovalProcess_NodeModel.passPercentage,
            rejectModel : addVue.sm_ApprovalProcess_NodeModel.rejectModel,
            sm_ApprovalProcess_ConditionList : addVue.sm_ApprovalProcess_ConditionList, // 审批条件
            checkedMessageTemplateId:addVue.checked,
            nodeType: addVue.nodeType,

        }
        if(addVue.buttonType == 1)
        {
            new ServerInterfaceJsonBody(baseInfo.addNodeInterface).execute(model, function(jsonObj)
            {
                if(jsonObj.result != "success")
                {
                    parent.generalErrorModal(jsonObj);
                }
                else
                {
                    addVue.lastNodeId = jsonObj.tableId;
                    addVue.idArr.push(jsonObj.tableId);
                    getSm_ApprovalProcess_NodeList();
                    $('#examine-add').modal('hide');
                    addVue.sm_ApprovalProcess_NodeModel = [];
                }
            });
        }
        else if(addVue.buttonType == 2)
        {
            new ServerInterfaceJsonBody(baseInfo.updateNodeInterface).execute(model, function(jsonObj)
            {
                if(jsonObj.result != "success")
                {
                    parent.generalErrorModal(jsonObj);
                }
                else
                {
                    getSm_ApprovalProcess_NodeList();
                    $('#examine-add').modal('hide');
                    addVue.sm_ApprovalProcess_NodeModel = [];
                }
            });
        }
    }

    //获取结点详情
    function getNodeDetail()
    {
        addVue.checked = [];
        addVue.buttonType = 2;
        var model={
            interfaceVersion:addVue.interfaceVersion,
            tableId : addVue.selectItem[0],
        }
        new ServerInterface(baseInfo.getNodeDetailInterface).execute(model, function(jsonObj){
            if(jsonObj.result != "success")
            {
                parent.generalErrorModal(jsonObj);
            }
            else
            {
                addVue.sm_ApprovalProcess_NodeModel = jsonObj.sm_ApprovalProcess_Node;
                addVue.approvalModel = addVue.sm_ApprovalProcess_NodeModel.approvalModel;
                addVue.roleId = addVue.sm_ApprovalProcess_NodeModel.roleId;
                addVue.sm_ApprovalProcess_NodeModel.orderNumber = addVue.updateOrderNumber;
                addVue.oldFinishPercentage = addVue.sm_ApprovalProcess_NodeModel.finishPercentage ;
                addVue.oldPassPercentage = addVue.sm_ApprovalProcess_NodeModel.passPercentage;

                addVue.sm_ApprovalProcess_ConditionList = jsonObj.sm_approvalProcess_conditionList;

                addVue.messageTemplate_cfgList = addVue.sm_ApprovalProcess_NodeModel.messageTemplate_cfgList; //选中的消息模板

                for (var i = 0 ; i< addVue.messageTemplate_cfgList.length ;i++)
                {
                    for (var j= 0 ; j < addVue.sm_MessageTemplate_CfgList.length  ; j++)
                    {
                        if(addVue.sm_MessageTemplate_CfgList[j].tableId == addVue.messageTemplate_cfgList[i].tableId)
                        {
                            addVue.checked.push(addVue.messageTemplate_cfgList[i].tableId);
                            break;
                        }
                    }
                }
            }
        });
    }

    //获取审批流程结点列表信息
    function getSm_ApprovalProcess_NodeList()
	{
        addVue.sm_ApprovalProcess_NodeList = [];
        var model = {
            interfaceVersion:addVue.interfaceVersion,
            theState : addVue.theState,
            idArr : addVue.idArr,
		}
        new ServerInterface(baseInfo.listInterface).execute(model, function(jsonObj){
            if(jsonObj.result != "success")
            {
                parent.generalErrorModal(jsonObj);
            }
            else
            {
                addVue.OrderIndexArr = [];
                addVue.sm_ApprovalProcess_NodeList = jsonObj.sm_ApprovalProcess_NodeList;
                addVue.sm_ApprovalProcess_ModalNodeList = jsonObj.sm_ApprovalProcess_ModalNodeList;
                getOrderNumber(addVue.sm_ApprovalProcess_NodeList,addVue.OrderIndexArr);
                addVue.sm_ApprovalProcess_NodeList[0].orderNumber = 1+"（起始节点）";
                addVue.selectedItem=[];
            }
        });
    }

    function getDeleteForm()
    {
        return{
            interfaceVersion:addVue.interfaceVersion,
            idArr:addVue.selectItem
        }
    }
    //删除
    function sm_ApprovalProcess_NodeDel()
    {
        if (addVue.selectItem.length > 1)
        {
            batchDelete(baseInfo. batchDeleteInterface, addVue.getDeleteForm(),function (jsonObj)
            {
                if(jsonObj.result=="success")
                {
                    for( var i=0 ;i< addVue.selectItem.length;i++)
                    {
                        for(var j=0;j<addVue.idArr.length;j++)
                        {
                            if(addVue.selectItem[i] == addVue.idArr[j])
                            {
                                addVue.idArr.remove(addVue.selectItem[i]);
                            }
                            continue;
                        }
                    }
                    addVue.lastNodeId = addVue.idArr[addVue.idArr.length-1];
                    addVue.selectItem=[];
                    getSm_ApprovalProcess_NodeList();
                }
            });
        }
        else
        {
            var tableId = addVue.selectItem[0];
            oneDelete(baseInfo.deleteInterface, tableId,function (jsonObj)
            {
                if(jsonObj.result=="success")
                {
                    addVue.idArr.remove(tableId);
                    addVue.lastNodeId = addVue.idArr[addVue.idArr.length-1];
                    addVue.selectItem=[];
                    getSm_ApprovalProcess_NodeList();
                }
            })
        }
    }

    function getAddForm()
    {
        return {
            interfaceVersion:addVue.interfaceVersion,
            busiId:addVue.busiId,
            eCode:addVue.sm_ApprovalProcess_CfgModel.eCode,
            theName:addVue.sm_ApprovalProcess_CfgModel.theName,
            isNeedBackup:addVue.sm_ApprovalProcess_CfgModel.isNeedBackup,
            remark:addVue.sm_ApprovalProcess_CfgModel.remark,
            idArr : addVue.idArr, //结点id
            busiId:addVue.busiId,
        }
    }

    function addSm_ApprovalProcess_Cfg()
    {
        new ServerInterface(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj){
            if(jsonObj.result != "success")
            {
                parent.generalErrorModal(jsonObj);
            }
            else
            {
                parent.generalSuccessModal();
                parent.enterNewTabAndCloseCurrent(jsonObj.tableId,"审批流程设置详情",'test/Test_ApprovalProcess_CfgDetail.shtml')
            }
        });
    }

    function listItemSelectHandle(list)
    {
        //修改按钮禁用状态
        if(list.length==1)
        {
            addVue.upDisabled = false;
        }
        else
        {
            addVue.upDisabled = true;
        }
        //删除按钮禁用状态
        if(list.length >= 1)
        {
            addVue.deDisabled = false;
        }
        else
        {
            addVue.deDisabled = true;
        }
        if(list.length == 1)
        {
            addVue.updateOrderNumber = list[0].orderNumber;
        }
        generalListItemSelectHandle(addVue,list)
    }

    //选中状态有改变，需要更新“全选”按钮状态
    function selectedItemChanged()
    {
        addVue.isAllSelected = (addVue.sm_ApprovalProcess_NodeList.length > 0)
            &&	(addVue.selectItem.length == addVue.sm_ApprovalProcess_NodeList.length);
    }
    //列表操作--------------“全选”按钮被点击
    function checkAllClicked()
    {
        if(addVue.selectItem.length == addVue.sm_ApprovalProcess_NodeList.length)
        {
            addVue.selectItem = [];
        }
        else
        {
            addVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
            addVue.sm_ApprovalProcess_NodeList.forEach(function(item) {
                addVue.selectItem.push(item.tableId);
            });
        }
    }

	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	addVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#sm_ApprovalProcess_CfgAddDiv",
    "getBaseParameterInterface":"../../Sm_BaseParameterForSelect",
    "getRoleInterface":"../../Sm_Permission_RoleForSelect",
    "getMessageTemplateInterface":"../../Sm_MessageTemplate_CfgList",
	"addNodeInterface":"../../Sm_ApprovalProcess_NodeAdd",
    "updateNodeInterface":"../../Sm_ApprovalProcess_NodeUpdate",
    "getNodeDetailInterface":"../../Sm_ApprovalProcess_NodeDetail",
	"addInterface":"../../Sm_ApprovalProcess_CfgAdd",
    "listInterface":"../../Sm_ApprovalProcess_NodeList",
    "deleteInterface":"../../Sm_ApprovalProcess_NodeDelete",
    "batchDeleteInterface": "../../Sm_ApprovalProcess_NodeBatchDelete",
});
