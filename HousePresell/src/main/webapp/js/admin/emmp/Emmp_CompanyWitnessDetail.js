(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			emmp_CompanyWitnessModel: {},
			theTypeName: "",
			qualificationGradeName: "",
			tableId : 1,
            isUsedStateStr: "",

            //机构成员相关
            pageNumber: 1,
            countPerPage: 10,
            totalPage: 1,
            totalCount: 1,
            theState: 0,//正常为0，删除为1
            selectItem: [],
            orgMemberList: [],

            //附件材料
            loadUploadList: [],
            showDelete : false,  //附件是否可编辑
            approvalCode: "020107",
            //其他
            errMsg:"", //错误提示信息
            //修改按钮
            upDisabled:true
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
            //机构成员列表
            refreshOrgMemberList: refreshOrgMemberList,
			
			companyWitnessEditHandle : companyWitnessEditHandle,

            //其他
            changePageNumber: function (data) {
                console.log(data);
                if (detailVue.pageNumber != data) {
                    detailVue.pageNumber = data;
                    detailVue.refreshOrgMemberList();
                }
            },
            changeCountPerPage : function (data) {
                console.log(data);
                if (detailVue.countPerPage != data) {
                    detailVue.countPerPage = data;
                    detailVue.refreshOrgMemberList();
                }
            },
            indexMethod: function (index) {
                if (detailVue.pageNumber > 1) {
                    return (detailVue.pageNumber - 1) * detailVue.countPerPage + (index - 0 + 1);
                }
                if (detailVue.pageNumber <= 1) {
                    return index - 0 + 1;
                }
            },
		},
		computed:{
			 
		},
		components : {
            'vue-nav': PageNavigationVue,
            "my-uploadcomponent":fileUpload
		},
		watch:{
			
		}
	});

	//------------------------方法定义-开始------------------//
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion : this.interfaceVersion,
			tableId : detailVue.tableId,
            reqSource : "详情"
		}
	}

	function getDetail()
	{
		new ServerInterfaceSync(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function(jsonObj)
		{

            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {

				detailVue.emmp_CompanyWitnessModel = jsonObj.emmp_CompanyInfo;
                if(detailVue.emmp_CompanyWitnessModel.busiState == '未备案')
                {
                    detailVue.approvalCode = '020106';
                }
                if(detailVue.emmp_CompanyWitnessModel.approvalState == "审核中")
                {
                    detailVue.upDisabled = true;
                }
                else
                {
                    detailVue.upDisabled = false;
                }
                switch (jsonObj.emmp_CompanyInfo.isUsedState) {
                    case "1":
                        detailVue.isUsedStateStr = "是";
                        break;
                    default:
                        detailVue.isUsedStateStr = "否";
                        break;
                }
			}
		});
	}

	//跳转方法
	function companyWitnessEditHandle(tableId) {
        enterNewTabCloseCurrent(tableId, '进度见证服务单位修改', 'emmp/Emmp_CompanyWitnessEdit.shtml');
	}

    /********* OrgMember 相关 *********/

    function refreshOrgMemberList ()
    {
        var model = {
            interfaceVersion: detailVue.interfaceVersion,
            pageNumber: detailVue.pageNumber,
            countPerPage: detailVue.countPerPage,
            totalPage: detailVue.totalPage,
            theState: detailVue.theState,
            companyId: detailVue.tableId,
        };

        new ServerInterface(baseInfo.orgMemberListInterface).execute(model, function (jsonObj) {

            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {

                detailVue.orgMemberList = jsonObj.emmp_OrgMemberList;
                detailVue.pageNumber = jsonObj.pageNumber;
                detailVue.countPerPage = jsonObj.countPerPage;
                detailVue.totalPage = jsonObj.totalPage;
                detailVue.totalCount = jsonObj.totalCount;
                detailVue.selectItem = [];
            }
        });
    }


    /********* 附件材料 相关 *********/

    function loadUpload(){
        model = {
            pageNumber : '0',
            busiType : '020106',
            sourceId : detailVue.tableId,
            // reqSource : "详情",
            approvalCode : detailVue.approvalCode,
            interfaceVersion:detailVue.interfaceVersion
        };

        new ServerInterface(baseInfo.loadUploadInterface).execute(model, function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {

                detailVue.loadUploadList = jsonObj.sm_AttachmentCfgList;
            }
        });
    }

    /********* 附件材料 相关 *********/

	function initData()
	{
        getIdFormTab('', function (tableId) {
            detailVue.tableId = tableId;

            //初始化
            refresh();
        })
	}


    //详情操作--------------
    function refresh()
    {
        getDetail();
        refreshOrgMemberList(); 	//获取机构成员列表
        loadUpload(); 				//获取附件材料
    }

	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#emmp_CompanyWitnessDetailDiv",
	"detailInterface":"../Emmp_CompanyWitnessDetail",
    //机构成员
    "orgMemberListInterface":"../Emmp_OrgMemberList",
    //材料附件
    "loadUploadInterface":"../Sm_AttachmentCfgList"
});
