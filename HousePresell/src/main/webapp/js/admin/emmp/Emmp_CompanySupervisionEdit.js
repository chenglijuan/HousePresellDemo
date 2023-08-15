(function(baseInfo){
	var editVue = new Vue({
		el : baseInfo.editDivId,
		data : {
			interfaceVersion :19000101,
			//机构信息相关
			emmp_CompanySupervisionModel: {},
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

			//机构成员操作相关
			addTheName:"",
			addIdType:"",
			addIdNumber:"",
			addTheNameOfDepartment:"",
			parameterNameList:[],
			addParameterNameId:"",
			addPositionName:"",
			addPhoneNumber:"",
			addEmail:"",
			addQq:"",
			addWeixin:"",
			// updateOrgMemberModel: {},

			//附件材料
			loadUploadList: [],
			showDelete : true,
            approvalCode: "020132",
			//其他
			errMsg:"", //错误提示信息
			busiCode:"020131",	//业务编码
			flag : true,
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			//更新
			update: update,
			//机构成员列表
			getParameterNameList : getParameterNameList,
			orgMemberDeleteMakeSure : orgMemberDeleteMakeSure,
			orgMemberDeleteHandle : orgMemberDeleteHandle,
			addOrgMember: addOrgMember,

			//其他
			toggleSelection: toggleSelection,
			handleSelectionChange: handleSelectionChange,

		},
		computed:{
			 
		},
		components : {
            "my-uploadcomponent":fileUpload,
            'vue-select': VueSelect
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
			tableId:editVue.tableId
		}
	}
	
	function toggleSelection(rows) {

	}

	function handleSelectionChange(val) {
		editVue.selectItem = [];
		for (var index = 0; index < val.length; index++) {
			var element = val[index];
			editVue.selectItem.push(element)
		}

	}

	function getCompanySupervisionDetail()
	{
		new ServerInterfaceSync(baseInfo.detailInterface).execute(editVue.getSearchForm(), function(jsonObj)
		{
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {

				editVue.emmp_CompanySupervisionModel = jsonObj.emmp_CompanyInfo;
                editVue.oldObj = jsonObj.emmp_CompanyInfo.oldObj;
                $('#date0201310201').val(editVue.emmp_CompanySupervisionModel.registeredDateStr);
                editVue.orgMemberList = jsonObj.emmp_OrgMemberList;
                if(editVue.emmp_CompanySupervisionModel.busiState == '未备案')
                {
                    editVue.approvalCode = '020131';
                }
			}
		});
	}
	
	//详情更新操作--------------获取"更新机构详情"参数

	function update(buttonType)
	{
		editVue.flag = false;
		var model = {
			interfaceVersion:this.interfaceVersion,
			tableId:this.tableId,
			theType:this.emmp_CompanySupervisionModel.theType,
			companyGroup:this.emmp_CompanySupervisionModel.companyGroup,
			theName:this.emmp_CompanySupervisionModel.theName,
			registeredDateStr:$('#date0201310201').val(),
			qualificationGrade:this.emmp_CompanySupervisionModel.qualificationGrade,
			legalPerson:this.emmp_CompanySupervisionModel.legalPerson,
			unifiedSocialCreditCode:this.emmp_CompanySupervisionModel.unifiedSocialCreditCode,

            contactPerson : this.emmp_CompanySupervisionModel.contactPerson,
            contactPhone : this.emmp_CompanySupervisionModel.contactPhone,

			address:this.emmp_CompanySupervisionModel.address,
	    	busiCode:this.busiCode,

            busiState:this.emmp_CompanySupervisionModel.busiState,

            isUsedState:this.emmp_CompanySupervisionModel.isUsedState,

            //机构成员
            orgMemberList:this.orgMemberList,

            //附件材料
            busiType : '020131',
            sourceId : this.tableId,
            generalAttachmentList:this.$refs.listenUploadData.uploadData,

            buttonType: buttonType,
            approvalState : (buttonType == '2') ? '审核中' : this.emmp_CompanySupervisionModel.approvalState
		};


		new ServerInterfaceJsonBody(baseInfo.updateInterface).execute(model, function(jsonObj)
		{
			editVue.flag = true;
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                generalSuccessModal();
                enterNewTabCloseCurrent(editVue.tableId, '监理机构详情', 'emmp/Emmp_CompanySupervisionDetail.shtml');
			}
		});
	}

	/********* OrgMember 相关 *********/
	
	function getParameterNameList ()
	{
		//判断是否请求过通用字段列表
		if (editVue.parameterNameList.length > 0) {
			return;
		}

		var model = {
			interfaceVersion : editVue.interfaceVersion,
			theName : "职务"
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
	
	function addOrgMember(divStr)
	{
		var isPass = errorCheckForAll(divStr);
		
		if (!isPass) {

			return;
		}

        var model = {
            theName:editVue.addTheName,
            idType:editVue.addIdType,
            idNumber:editVue.addIdNumber,
            theNameOfDepartment:editVue.addTheNameOfDepartment,
            parameterId:editVue.addParameterNameId,
            parameterName:editVue.addParameterName,
            positionName:editVue.addPositionName,
            phoneNumber:editVue.addPhoneNumber,
            email:editVue.addEmail,
            qq:editVue.addQq,
            weixin:editVue.addWeixin,
			// companyId:editVue.tableId
        };

        editVue.orgMemberList.push(model);

        editVue.addTheName = "";
        editVue.addIdType = "";
        editVue.addIdNumber = "";
        editVue.addTheNameOfDepartment = "";
        editVue.addParameterNameId = "";
        editVue.addPositionName = "";
        editVue.addPhoneNumber = "";
        editVue.addEmail = "";
        editVue.addQq = "";
        editVue.addWeixin = "";

        $(baseInfo.addPersonModal).modal('hide');
	}

	function orgMemberDeleteHandle() {

        if(editVue.selectItem.length == 0)
        {
            generalErrorModal("请选择要删除的机构成员");
        }
        else
        {

			$(baseInfo.deleteOrgMemberAlert).modal('show', {
				backdrop :'static'
			});
		}
	}
	
    function orgMemberDeleteMakeSure() {

        var newOrgMemberList = [];

        for (var i = 0; i < editVue.orgMemberList.length; i++)
        {
            var needDelete = false;

            for (var j = 0; j < editVue.selectItem.length; j++)
            {
                if (editVue.orgMemberList[i] == editVue.selectItem[j])
                {
                    needDelete = true;
                }
            }
            if (needDelete == false)
            {
                newOrgMemberList.push(editVue.orgMemberList[i]);
            }
        }

        editVue.orgMemberList = newOrgMemberList;

        $(baseInfo.deleteOrgMemberAlert).modal('hide');
    }

	/********* OrgMember 相关 *********/


	/********* 附件材料 相关 *********/

	function loadUpload(){
		var model = {
			pageNumber : '0',
			busiType : '020131',
            sourceId : editVue.tableId,
			interfaceVersion:editVue.interfaceVersion,
            approvalCode : editVue.approvalCode
		};
		
		new ServerInterface(baseInfo.loadUploadInterface).execute(model, function(jsonObj)
		{
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

        laydate.render({
            elem: '#date0201310201',
            max: getNowFormatDate()
        });

        getIdFormTab('', function (tableId) {
            editVue.tableId = tableId;

            //初始化
            refresh();
        });
	}

	function refresh()
	{

        getCompanySupervisionDetail();		//获取详情
		getParameterNameList();		//获取机构成员职务字段在通用字段表里的数据
        loadUpload(); 				//获取附件材料
	}

	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//

	editVue.initData();

	//------------------------数据初始化-结束----------------//
	
})({
	"editDivId":"#emmp_CompanySupervisionEditDiv",
	"detailInterface":"../Emmp_CompanySupervisionDetail",
	"updateInterface":"../Emmp_CompanySupervisionUpdate",
	"cityRegionInfoInterface":"../Sm_CityRegionInfoList",
	"streetInfoInterface":"../Sm_StreetInfoList",
	//机构成员
	"addPersonModal":"#addPerson",

	"deleteOrgMemberAlert":"#deleteOrgMember",
	// "editOrgMemberAlert":"#editOrgMember",
	"orgMemberListInterface":"../Emmp_OrgMemberList",
	"orgMemberAddInterface":"../Emmp_OrgMemberAdd",
	"orgMemberEditInterface":"../Emmp_OrgMemberUpdate",
	"orgMemberDeleteInterface": "../Emmp_OrgMemberDelete",
	"orgMemberBatchDeleteInterface": "../Emmp_OrgMemberBatchDelete",
	"baseParameterListInterface":"../Sm_BaseParameterList",
	//材料附件	
	// "errorAttachmentModel":"#errorAttachment",
	"loadUploadInterface":"../Sm_AttachmentCfgList",
	"attachmentBatchAddInterface":"../Sm_AttachmentBatchAdd",
	"attachmentListInterface":"../Sm_AttachmentList",
	//其他
	"successModel":"#successM",
});
