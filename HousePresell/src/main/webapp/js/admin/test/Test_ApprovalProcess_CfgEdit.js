(function(baseInfo){
	var updateVue = new Vue({
		el : baseInfo.updateDivId,
		data : {
			interfaceVersion :19000101,
            theState:0,//正常为0，删除为1
			sm_ApprovalProcess_CfgModel: {},//配置详情
            sm_ApprovalProcess_NodeModel:{},//节点详情
            sm_ApprovalProcess_NodeList:[],//节点列表
            sm_ApprovalProcess_ConditionList:[], //审批条件列表
            sm_Permission_RoleList:[],//角色列表
            sm_BaseParameterList:[],
            sm_MessageTemplate_CfgList:[],//消息模板配置列表
            messageTemplate_cfgList:[],//选中的消息模板
            configurationId:"",//流程配置Id
            codeType:"",
            busiId:"",
            approvalModel : "",
            updateOrderNumber:"",
            selectItem:[],
            checked:[],
			tableId : 1,
            roleId:"",
            idArr:[], //存放新增结点Id
			firstLastNodeId:"",
            lastNodeId:-1,
            disabled:true,
            upDisabled:true,
            deDisabled:true,
            oldFinishPercentage:"",
			oldPassPercentage:"",
            thetemplate:[],
            OrderIndexArr: [],
            buttonType:"",
            nodeType:"", //0 :第一个结点 1:其他结点
		},
		methods : {
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
            getDeleteForm:getDeleteForm,
            listItemSelectHandle:listItemSelectHandle,
            selectedItemChanged :selectedItemChanged,
            showModal:showModal,
            getNodeDetail:getNodeDetail,
            getSm_PermissionRoleList:getSm_PermissionRoleList,
            getMessageTemplate_CfgList:getMessageTemplate_CfgList,
            sm_MessageTemplate_CfgDetail:sm_MessageTemplate_CfgDetail,
            sm_ApprovalProcess_NodeDel : sm_ApprovalProcess_NodeDel,
            roleChange:roleChange,
            checkAllClicked:checkAllClicked,
			getUpdateForm : getUpdateForm,
            updateSm_ApprovalProcess_Node:updateSm_ApprovalProcess_Node,
            updateSm_ApprovalProcess_Cfg: updateSm_ApprovalProcess_Cfg,
            addRow:addRow,
            deleteRow:deleteRow,
            // 合并单元格
            objectSpanMethod:objectSpanMethod,
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
                        if(updateVue.buttonType == 2)
                        {
                            updateVue.sm_ApprovalProcess_NodeModel.finishPercentage = updateVue.oldFinishPercentage;
                            updateVue.sm_ApprovalProcess_NodeModel.passPercentage = updateVue.oldPassPercentage;
                        }
                        updateVue.disabled = false;
                    }
                    else
                    {
                        updateVue.sm_ApprovalProcess_NodeModel.finishPercentage = "";
                        updateVue.sm_ApprovalProcess_NodeModel.passPercentage = "";
                        updateVue.disabled = true;
                    }
                },
                deep:true
            },
		}
	});

	//------------------------方法定义-开始------------------//

    function initData()
    {
        updateVue.tableId = parent.getTabElementUI_TableId();
        refresh();
    }

	function getSearchForm()
	{
		return {
			interfaceVersion:updateVue.interfaceVersion,
			tableId:updateVue.tableId,
            theState : updateVue.theState,
		}
	}

	function refresh()
	{
		if (updateVue.tableId == null || updateVue.tableId < 1)
		{
			return;
		}
        getDetail();
	}

	function getDetail()
	{
		new ServerInterface(baseInfo.detailInterface).execute(updateVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
                parent.generalErrorModal(jsonObj);
			}
			else
			{
                updateVue.OrderIndexArr = [];
                updateVue.sm_ApprovalProcess_CfgModel = jsonObj.sm_ApprovalProcess_CfgDetail;
                updateVue.configurationId = updateVue.sm_ApprovalProcess_CfgModel.tableId;
                updateVue.codeType = updateVue.sm_ApprovalProcess_CfgModel.codeType;
                updateVue.busiId =  updateVue.sm_ApprovalProcess_CfgModel.busiId;
                updateVue.sm_ApprovalProcess_NodeList = jsonObj.sm_ApprovalProcess_NodeList;
                getOrderNumber(updateVue.sm_ApprovalProcess_NodeList,updateVue.OrderIndexArr);
                updateVue.sm_ApprovalProcess_NodeList[0].orderNumber = 1+"（起始节点）";

                updateVue.sm_ApprovalProcess_ModalNodeList = jsonObj.sm_ApprovalProcess_ModalNodeList;
                updateVue.firstLastNodeId = updateVue.sm_ApprovalProcess_NodeList[updateVue.sm_ApprovalProcess_NodeList.length-1].tableId; //获取当前最后一个结点

                updateVue.messageTemplate_cfgList = updateVue.sm_ApprovalProcess_NodeModel.messageTemplate_cfgList; //选中的消息模板

                //加载完页面后 查询角色列表和 模板配置列表
                getSm_PermissionRoleList();
                getMessageTemplate_CfgList();
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
            for(var i = 0; i < updateVue.OrderIndexArr.length; i++) {
                var element = updateVue.OrderIndexArr[i]
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

    //新增按钮弹框
    function showModal()
    {
        updateVue.buttonType = 1;
        updateVue.sm_ApprovalProcess_NodeModel = [];
        updateVue.approvalModel = 0;
        updateVue.sm_ApprovalProcess_NodeModel.rejectModel = -1;//驳回模式
        updateVue.sm_ApprovalProcess_ConditionList = []
        updateVue.checked = [];

        //添加第一个结点
        if(updateVue.sm_ApprovalProcess_ModalNodeList.length == 0)
        {
            updateVue.sm_ApprovalProcess_NodeModel.orderNumber = 1+"(起始节点)";
        }
        else
        {
            updateVue.sm_ApprovalProcess_NodeModel.orderNumber = updateVue.sm_ApprovalProcess_ModalNodeList.length + 1;
        }
        updateVue.roleId = "";

    }

    //获取角色列表
    function getSm_PermissionRoleList()
    {
        new ServerInterface(baseInfo.getRoleInterface).execute(getTotalListForm(), function(jsonObj){
            if(jsonObj.result != "success")
            {
                parent.generalErrorModal(jsonObj);
            }
            else
            {
                updateVue.sm_Permission_RoleList = jsonObj.sm_Permission_RoleList;
            }
        });
    }

    //获取消息模板配置列表
    function getMessageTemplate_CfgList()
    {
        var model= {
            interfaceVersion:updateVue.interfaceVersion,
            pageNumber: 1,
            countPerPage: MAX_VALUE,
            theState: 0,
            totalPage: 1,
            keyword: "",
            busiCode:updateVue.sm_ApprovalProcess_CfgModel.busiCode,
        }
        new ServerInterface(baseInfo.getMessageTemplateInterface).execute(model, function(jsonObj){
            if(jsonObj.result != "success")
            {
                parent.generalErrorModal(jsonObj);
            }
            else
            {
                updateVue.sm_MessageTemplate_CfgList=jsonObj.sm_MessageTemplate_CfgList;
            }
        });
    }

    //进入详情
    function sm_MessageTemplate_CfgDetail(tableId)
    {
        $('#examine-add').modal('hide');
        parent.enterNewTabTest(tableId, "消息模板配置详情", "test/Test_MessageTemplate_CfgDetail.shtml");
    }


    //获取结点详情
    function getNodeDetail()
    {
        updateVue.checked = [];
        updateVue.buttonType = 2;
        var model={
            interfaceVersion:updateVue.interfaceVersion,
            tableId : updateVue.selectItem[0],
        }
        new ServerInterface(baseInfo.getNodeDetailInterface).execute(model, function(jsonObj){
            if(jsonObj.result != "success")
            {
                parent.generalErrorModal(jsonObj);
            }
            else
            {
                updateVue.sm_ApprovalProcess_NodeModel = jsonObj.sm_ApprovalProcess_Node;
                updateVue.approvalModel = updateVue.sm_ApprovalProcess_NodeModel.approvalModel;
                updateVue.roleId = updateVue.sm_ApprovalProcess_NodeModel.roleId;
                updateVue.sm_ApprovalProcess_NodeModel.orderNumber = updateVue.updateOrderNumber;
                updateVue.oldFinishPercentage = updateVue.sm_ApprovalProcess_NodeModel.finishPercentage ;
                updateVue.oldPassPercentage = updateVue.sm_ApprovalProcess_NodeModel.passPercentage;
                updateVue.sm_ApprovalProcess_ConditionList = jsonObj.sm_approvalProcess_conditionList; //条件

                updateVue.messageTemplate_cfgList = updateVue.sm_ApprovalProcess_NodeModel.messageTemplate_cfgList; //选中的消息模板

                for (var i = 0 ; i< updateVue.messageTemplate_cfgList.length ;i++)
                {
                    for (var j= 0 ; j < updateVue.sm_MessageTemplate_CfgList.length  ; j++)
                    {
                        if(updateVue.sm_MessageTemplate_CfgList[j].tableId == updateVue.messageTemplate_cfgList[i].tableId)
                        {
                            updateVue.checked.push(updateVue.messageTemplate_cfgList[i].tableId);
                            break;
                        }
                    }
                }
            }
        });
    }

    function  roleChange(date)
    {
        updateVue.roleId = date.tableId;
        updateVue.sm_ApprovalProcess_NodeModel.roleId = date.tableId;
    }

    //表格增加一行
    function addRow()
    {
        var condition = {
            theContent:"",
            nextStep:""
        }
        updateVue.sm_ApprovalProcess_ConditionList.push(condition);
    }

    //表格删除当前行
    function deleteRow(index)
    {
        updateVue.sm_ApprovalProcess_ConditionList.splice(index,1);
    }


    //新增、修改节点
    function updateSm_ApprovalProcess_Node()
    {
    	//修改界面：首次再添加新结点，先获取当前审批流程最后一个结点Id
        if(updateVue.lastNodeId < 0)
		{
            updateVue.lastNodeId = updateVue.firstLastNodeId;
		}
        if(updateVue.sm_ApprovalProcess_ModalNodeList.length == 0)
        {
            updateVue.nodeType = 0 ;
        }
        else if(updateVue.sm_ApprovalProcess_ModalNodeList.length > 0)
        {
            updateVue.nodeType = 1 ;
        }
        var model = {
            interfaceVersion:updateVue.interfaceVersion,
            configurationId:updateVue.tableId, //流程配置Id
            tableId:updateVue.selectItem[0], //结点Id
            theName:updateVue.sm_ApprovalProcess_NodeModel.theName,
            eCode:updateVue.sm_ApprovalProcess_NodeModel.eCode,
            roleId : updateVue.roleId ,
            lastNoteId:updateVue.lastNodeId,
            approvalModel:updateVue.approvalModel,
            finishPercentage:updateVue.sm_ApprovalProcess_NodeModel.finishPercentage,
            passPercentage : updateVue.sm_ApprovalProcess_NodeModel.passPercentage,
            rejectModel : updateVue.sm_ApprovalProcess_NodeModel.rejectModel,
            sm_ApprovalProcess_ConditionList : updateVue.sm_ApprovalProcess_ConditionList, // 审批条件
            checkedMessageTemplateId:updateVue.checked,
            nodeType: updateVue.nodeType,
        }
        if(updateVue.buttonType == 1)
        {
            new ServerInterfaceJsonBody(baseInfo.addNodeInterface).execute(model, function(jsonObj)
            {
                if(jsonObj.result != "success")
                {
                    generalErrorModal(jsonObj);
                }
                else
                {
                    updateVue.lastNodeId = jsonObj.tableId;
                    updateVue.idArr.push(jsonObj.tableId); //新增的结点Id
                    refresh();
                    $('#examine-add').modal('hide');
                    updateVue.sm_ApprovalProcess_NodeModel = [];
                }
            });
        }
        else if(updateVue.buttonType == 2)
        {
            new ServerInterfaceJsonBody(baseInfo.updateNodeInterface).execute(model, function (jsonObj) {
                if (jsonObj.result != "success")
                {
                    generalErrorModal(jsonObj);
                }
                else
				{
                    refresh();
                    $('#examine-add').modal('hide');
                    updateVue.sm_ApprovalProcess_NodeModel = [];
				}
            });
        }
    }

    function getDeleteForm()
    {
        return{
            interfaceVersion:updateVue.interfaceVersion,
            configurationId:updateVue.configurationId,
            idArr:updateVue.selectItem
        }
    }
    //删除节点
    function sm_ApprovalProcess_NodeDel()
    {
        if (updateVue.selectItem.length > 1)
        {
            batchDelete(baseInfo. batchDeleteInterface, updateVue.getDeleteForm(),function (jsonObj) {
                if(jsonObj.result=="success")
                {
                    for( var i=0 ;i< updateVue.selectItem.length;i++)
                    {
                        for(var j=0;j<updateVue.idArr.length;j++)
                        {
                            if(updateVue.selectItem[i] == updateVue.idArr[j])
                            {
                                updateVue.idArr.remove(updateVue.selectItem[i]);
                            }
                            continue;
                        }
                    }
                    updateVue.selectItem=[];
                    refresh();
                }
            });
        }
        else
        {
            var tableId = updateVue.selectItem[0];
            oneDelete(baseInfo.deleteInterface, tableId,function (jsonObj) {
                if(jsonObj.result=="success")
                {
                    updateVue.idArr.remove(tableId);
                    updateVue.selectItem=[];
                    refresh();
                }
            })
        }
    }

    function listItemSelectHandle(list)
    {
        //修改按钮禁用状态
        if(list.length==1)
        {
            updateVue.upDisabled = false;
        }
        else
        {
            updateVue.upDisabled = true;
        }
        //删除按钮禁用状态
        if(list.length >= 1)
        {
            updateVue.deDisabled = false;
        }
        else
        {
            updateVue.deDisabled = true;
        }

        if(list.length == 1)
        {
            updateVue.updateOrderNumber = list[0].orderNumber;
        }
        generalListItemSelectHandle(updateVue,list)
    }

    //选中状态有改变，需要更新“全选”按钮状态
    function selectedItemChanged()
    {
        updateVue.isAllSelected = (updateVue.sm_MessageTemplate_CfgList.length > 0)
            &&	(updateVue.selectItem.length == updateVue.sm_MessageTemplate_CfgList.length);
    }
    //列表操作--------------“全选”按钮被点击
    function checkAllClicked()
    {
        if(updateVue.selectItem.length == updateVue.sm_MessageTemplate_CfgList.length)
        {
            updateVue.selectItem = [];
        }
        else
        {
            updateVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
            updateVue.sm_MessageTemplate_CfgList.forEach(function(item) {
                updateVue.selectItem.push(item.tableId);
            });
        }
    }


	function getUpdateForm()
	{
		return {
			interfaceVersion:updateVue.interfaceVersion,
			tableId:updateVue.tableId,
            busiId:updateVue.busiId,
			theName:updateVue.sm_ApprovalProcess_CfgModel.theName,
			eCode:updateVue.sm_ApprovalProcess_CfgModel.eCode,
			isNeedBackup:updateVue.sm_ApprovalProcess_CfgModel.isNeedBackup,
			remark:updateVue.sm_ApprovalProcess_CfgModel.remark,
            idArr : updateVue.idArr, //结点id
		}
	}

    function updateSm_ApprovalProcess_Cfg()
    {
    	new ServerInterface(baseInfo.updateInterface).execute(updateVue.getUpdateForm(), function(jsonObj){
    		if(jsonObj.result != "success")
    		{
                parent.generalErrorModal(jsonObj);
    		}
    		else
    		{
                parent.generalSuccessModal();
                parent.enterNewTabAndCloseCurrent(updateVue.tableId, '审批流程设置详情', 'test/Test_ApprovalProcess_CfgDetail.shtml');
    		}
    	});
    }
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	updateVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"updateDivId":"#sm_ApprovalProcess_CfgEditDiv",
    "getBaseParameterInterface":"../../Sm_BaseParameterForSelect",
    "getRoleInterface":"../../Sm_Permission_RoleForSelect",
    "getMessageTemplateInterface":"../../Sm_MessageTemplate_CfgList",
	"detailInterface":"../../Sm_ApprovalProcess_CfgDetail",
    "updateInterface":"../../Sm_ApprovalProcess_CfgUpdate",
    "getNodeDetailInterface":"../../Sm_ApprovalProcess_NodeDetail",
    "addNodeInterface":"../../Sm_ApprovalProcess_NodeAdd",
    "updateNodeInterface":"../../Sm_ApprovalProcess_NodeUpdate",
    "deleteInterface":"../../Sm_ApprovalProcess_NodeDelete",
    "batchDeleteInterface": "../../Sm_ApprovalProcess_NodeBatchDelete",
});
