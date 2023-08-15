(function(baseInfo){
    var editVue = new Vue({
        el : baseInfo.editDivId,
        data : {
            interfaceVersion :19000101,
            //机构信息相关
            emmp_CompanyWitnessModel: {},
            oldObj : {},
            sm_CityRegionInfoList : [],
            cityRegionId : "",
            streetId : "",
            sm_StreetInfoList: [],
            tableId : 1,
            //机构成员相关
            theState: 0,//正常为0，删除为1
            selectItem: [],
            orgMemberList: [],
            parameterNameList: [],

            //附件材料
            loadUploadList: [],
            showDelete : false,
            approvalCode : "020107",
            //其他
            errMsg:"", //错误提示信息
            busiCode:"020106",
            //----------审批流start-----------//
            afId:"",//申请单Id
            workflowId:"",//结点Id
            isNeedBackup:"",//是否需要备案
            sourcePage:"",//来源页面
            //----------审批流end-----------//
        },
        methods : {
            //详情
            refresh : refresh,
            initData: initData,
            getSearchForm : getSearchForm,
            //机构成员列表
            getParameterNameList : getParameterNameList,
            //其他
            showModal : showModal,

            changePageNumber: function (data)
            {
                editVue.pageNumber = data;
            },
            toggleSelection: toggleSelection,
            handleSelectionChange: handleSelectionChange,
            approvalHandle : function ()
            {
                approvalModalVue.buttonType = 2;
                approvalModalVue.approvalHandle();
            }
        },
        computed:{

        },
        components : {
            "my-uploadcomponent" : fileUpload,
            'vue-select' : VueSelect
        },
        watch:{
           
        }
    });

    //------------------------方法定义-开始------------------//
    //详情操作--------------获取"机构详情"参数
    function getSearchForm()
    {
        return {
            interfaceVersion:this.interfaceVersion,
            tableId:editVue.tableId,
            reqSource:"审批",
            afId:editVue.afId
        }
    }

    function toggleSelection(rows) {

    }

    function handleSelectionChange(val)
     {
        editVue.selectItem = [];
        for (var index = 0; index < val.length; index++) {
            var element = val[index].tableId;
            editVue.selectItem.push(element)
        }
    }

    function getCompanyWitnessDetail()
    {
        new ServerInterfaceSync(baseInfo.detailInterface).execute(editVue.getSearchForm(), function(jsonObj)
        {

            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                editVue.emmp_CompanyWitnessModel = jsonObj.emmp_CompanyInfo;
                editVue.oldObj = jsonObj.emmp_CompanyInfo.oldObj;
                $('#registeredDateSelect_edit').val(editVue.emmp_CompanyWitnessModel.registeredDate);
                editVue.orgMemberList = jsonObj.emmp_OrgMemberList;
                if(editVue.emmp_CompanyWitnessModel.busiState == '未备案')
                {
                    editVue.approvalCode = '020106';
                }
                editVue.isNeedBackup = jsonObj.isNeedBackup;
                approvalModalVue.isNeedBackup = editVue.isNeedBackup;
            }
        });
    }

    /********* OrgMember 相关 *********/
    function getParameterNameList ()
    {
        //判断是否请求过通用字段列表
        if (editVue.parameterNameList.length > 0)
        {
            return;
        }

        var model = {
            interfaceVersion:editVue.interfaceVersion,
            theName:"职务"
        };

        new ServerInterface(baseInfo.baseParameterListInterface).execute(model, function (jsonObj) {
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                editVue.parameterNameList = jsonObj.sm_BaseParameterList;
            }
        });
    }

    /********* OrgMember 相关 *********/

    /********* 附件材料 相关 *********/

    function loadUpload()
    {
        model = {
            pageNumber : '0',
            busiType : '020106',
            sourceId : editVue.tableId,
            interfaceVersion:editVue.interfaceVersion,
            approvalCode : editVue.approvalCode,
            afId:editVue.afId
        };
        new ServerInterface(baseInfo.loadUploadInterface).execute(model, function(jsonObj)
        {
            console.log(jsonObj);

            if(jsonObj.result != "success")
            {
                editVue.errMsg = jsonObj.info;
                $(baseInfo.errorAttachmentModel).modal({
                    backdrop :'static'
                });
            }
            else
            {
                editVue.loadUploadList = jsonObj.sm_AttachmentCfgList;
            }
        });
    }

    /********* 附件材料 相关 *********/


    /********* 初始化方法 *********/
    function initData()
    {
        var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
        var array = tableIdStr.split("_");
        if (array.length > 1)
        {
            editVue.tableId = array[array.length-4];
            editVue.afId = array[array.length-3];
            editVue.workflowId = array[array.length-2];
            editVue.sourcePage = array[array.length-1];

            approvalModalVue.afId = editVue.afId;
            approvalModalVue.workflowId = editVue.workflowId;
            approvalModalVue.sourcePage = editVue.sourcePage;
            refresh();
        }
    }

    function refresh()
    {

        getCompanyWitnessDetail();		//获取详情
        getParameterNameList();		//获取机构成员职务字段在通用字段表里的数据
        loadUpload(); 				//获取附件材料
    }

    function showModal()
    {
        approvalModalVue.getModalWorkflowList();
    }

    //------------------------方法定义-结束------------------//

    //------------------------数据初始化-开始----------------//

    editVue.initData();

    //------------------------数据初始化-结束----------------//

})({
    "editDivId":"#Sm_ApprovalProcess_CompanyWitnessDetailDiv",
    "detailInterface":"../Emmp_CompanyWitnessDetail",
    "cityRegionInfoInterface":"../Sm_CityRegionInfoList",
    "streetInfoInterface":"../Sm_StreetInfoList",
    //机构成员
    "orgMemberListInterface":"../Emmp_OrgMemberList",
    "baseParameterListInterface":"../Sm_BaseParameterList",
    //材料附件
    // "errorAttachmentModel":"#errorAttachment",
    "loadUploadInterface":"../Sm_AttachmentCfgList",
    "attachmentBatchAddInterface":"../Sm_AttachmentBatchAdd",
    "attachmentListInterface":"../Sm_AttachmentList",
    //其他
    "successModel":"#successM",
});
