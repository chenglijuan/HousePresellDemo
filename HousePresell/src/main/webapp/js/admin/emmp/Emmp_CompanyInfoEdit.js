(function(baseInfo){
	var editVue = new Vue({
		el : baseInfo.editDivId,
		data : {
			interfaceVersion :19000101,
			//机构信息相关
			emmp_CompanyInfoModel: {},
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
			//附件材料
            busiType:'020101',
			loadUploadList: [],
			showDelete : true,
			approvalCode: "020102",
			//其他
			errMsg:"",//错误提示信息
			busiCode:"020102",//业务编码
			buttonType:"",
			isUsedStateList : [
            	{tableId:"1",theName:"是"},
            	{tableId:"0",theName:"否"},
            ],
            flag : true,

		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			getCityRegionInfoForm : getCityRegionInfoForm,
			getCityRegionInfo : getCityRegionInfo,
			getStreetInfoForm : getStreetInfoForm, 
			getStreetInfo : getStreetInfo,
			//更新
			getUpdateForm : getUpdateForm,
			update: update,
			//机构成员列表
			getParameterNameList : getParameterNameList,
			orgMemberDeleteMakeSure : orgMemberDeleteMakeSure,
			orgMemberDeleteHandle : orgMemberDeleteHandle,
			addOrgMember: addOrgMember,

			//其他
			changePageNumber: function (data) {
				editVue.pageNumber = data;
			},
			toggleSelection: toggleSelection,
			handleSelectionChange: handleSelectionChange,
            cityRegionChange : function (data) {
                this.cityRegionId = data.tableId;
                editVue.streetId = "";
                editVue.getStreetInfo();
            },
            streetInfoChange : function (data) {
                this.streetId = data.tableId;
            },
            getIsUsedState : function(data){
            	editVue.emmp_CompanyInfoModel.isUsedState = data.tableId;
			},
			emptyIsUsedState : function(){
				editVue.emmp_CompanyInfoModel.isUsedState= null;
			},
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
	
	//获取城市区域基本信息--表单参数
	function getCityRegionInfoForm() 
	{
		return {
			interfaceVersion:this.interfaceVersion,
		}
	}
	
	function getCityRegionInfo() 
	{
		new ServerInterface(baseInfo.cityRegionInfoInterface).execute(editVue.getCityRegionInfoForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{	
				editVue.sm_CityRegionInfoList = jsonObj.sm_CityRegionInfoList;
			}
		});
	}
	
	function getStreetInfoForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			cityRegionId:this.cityRegionId
		}
	}
	
	function getStreetInfo()
	{

		new ServerInterface(baseInfo.streetInfoInterface).execute(editVue.getStreetInfoForm(), function(jsonObj){
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
				editVue.sm_StreetInfoList = jsonObj.sm_StreetInfoList;
			}
		});
	}

	function getCompanyInfoDetail()
	{
		new ServerInterfaceSync(baseInfo.detailInterface).execute(editVue.getSearchForm(), function(jsonObj)
		{
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
				editVue.emmp_CompanyInfoModel = jsonObj.emmp_CompanyInfo;
                editVue.oldObj = jsonObj.emmp_CompanyInfo.oldObj;
                $('#date0201010201').val(editVue.emmp_CompanyInfoModel.registeredDateStr);
                editVue.orgMemberList = jsonObj.emmp_OrgMemberList;
                editVue.cityRegionId = editVue.emmp_CompanyInfoModel.cityRegionId;
                editVue.streetId = editVue.emmp_CompanyInfoModel.streetId;
                getCityRegionInfo();
				getStreetInfo();
				if(editVue.emmp_CompanyInfoModel.busiState == '未备案')
				{
					editVue.approvalCode = '020101';
				}
			}
		});
	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getUpdateForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:this.tableId,
			theType:this.emmp_CompanyInfoModel.theType,
			companyGroup:this.emmp_CompanyInfoModel.companyGroup,
			theName:this.emmp_CompanyInfoModel.theName,
			registeredDateStr:$('#date0201010201').val(),
			qualificationGrade:this.emmp_CompanyInfoModel.qualificationGrade,
			legalPerson:this.emmp_CompanyInfoModel.legalPerson,
			unifiedSocialCreditCode:this.emmp_CompanyInfoModel.unifiedSocialCreditCode,
			projectLeader:this.emmp_CompanyInfoModel.projectLeader,
			cityRegionId:this.cityRegionId,
			streetId:this.streetId,
			address:this.emmp_CompanyInfoModel.address,
	    	busiCode:this.busiCode,

            contactPhone : this.emmp_CompanyInfoModel.contactPhone,

            busiState:this.emmp_CompanyInfoModel.busiState,
            approvalState:this.emmp_CompanyInfoModel.approvalState,

            isUsedState:this.emmp_CompanyInfoModel.isUsedState,

            //机构成员
            orgMemberList:this.orgMemberList,

            //附件材料
            busiType : '020101',
            sourceId : this.tableId,
            generalAttachmentList:this.$refs.listenUploadData.uploadData,
            buttonType:editVue.buttonType,
		}
	}

	function update(buttonType)
	{
		if(buttonType == 1)
		{
            editVue.buttonType = 1; //保存按钮
		}else if(buttonType == 2)
		{
			editVue.flag = false;
            editVue.buttonType = 2; //提交按钮
		}
		new ServerInterfaceJsonBody(baseInfo.updateInterface).execute(editVue.getUpdateForm(), function(jsonObj)
		{
			editVue.flag = true;
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                generalSuccessModal();
                enterNewTabCloseCurrent(editVue.tableId, '开发企业详情', 'emmp/Emmp_CompanyInfoDetail.shtml');
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
			interfaceVersion:editVue.interfaceVersion,
			theName:"职务",
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
        var isPass = errorCheckForAll1(divStr, editVue.addIdType);

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
		model = {
			pageNumber : '0',
			busiType : '020101',
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
			elem: '#date0201010201',
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

        getCompanyInfoDetail();		//获取详情
		getParameterNameList();		//获取机构成员职务字段在通用字段表里的数据
        loadUpload(); 				//获取附件材料
	}

	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//

	editVue.initData();

	//------------------------数据初始化-结束----------------//
	
})({
	"editDivId":"#emmp_CompanyInfoEditDiv",
    "detailInterface":"../Emmp_CompanyInfoDetail",
	"updateInterface":"../Emmp_CompanyInfoUpdate",
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
