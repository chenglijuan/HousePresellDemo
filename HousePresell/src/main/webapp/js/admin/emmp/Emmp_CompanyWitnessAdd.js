(function(baseInfo){
	var addVue = new Vue({
		el: baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			eCode : "",
            theType : "12",
            parameterType : "8",
            companyTypeName : "",
			theName : "",
			qualificationGrade : "",
			legalPerson : "",
			unifiedSocialCreditCode : "",
            contactPerson : "",
            contactPhone : "",
			address : "",
            isUsedState: "1",

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
            addParameterName:"",
            addPositionName:"",
            addPhoneNumber:"",
            addEmail:"",
            addQq:"",
            addWeixin:"",
            updateOrgMemberModel: {},

            //附件材料
            loadUploadList: [],
            showDelete : true,  //附件是否可编辑
            flag  : true,
		},
		methods : {
			getAddForm : getAddForm,
			add : addEmmp_CompanyWitness,
            getSubmitForm : getSubmitForm,
            submit : submitEmmp_CompanyWitness,
			initValue : initValue,

            //机构成员列表
            getParameterNameList : getParameterNameList,
            orgMemberDeleteMakeSure : orgMemberDeleteMakeSure,
            orgMemberDeleteHandle : orgMemberDeleteHandle,
            addOrgMember: addOrgMember,

            //其他
            changePageNumber: function (data) {
                addVue.pageNumber = data;
            },
            toggleSelection: toggleSelection,
            handleSelectionChange: handleSelectionChange,
            indexMethod: function (index) {
                if (addVue.pageNumber > 1) {
                    return (addVue.pageNumber - 1) * addVue.countPerPage + (index - 0 + 1);
                }
                if (addVue.pageNumber <= 1) {
                    return index - 0 + 1;
                }
            }
		},
		computed:{
			 
		},
		components: {
			'vue-select': VueSelect ,
            "my-uploadcomponent":fileUpload
		},
		watch:{
			
		}
	});

	//------------------------方法定义-开始------------------//
	 
	
	//机构信息操作--------------获取“新增机构”表单参数
	function getAddForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			theType:this.theType,
			eCode:this.eCode,
			theName:this.theName,
			registeredDateStr:$('#date0201060101').val(),
			qualificationGrade:this.qualificationGrade,
			legalPerson:this.legalPerson,
			unifiedSocialCreditCode:this.unifiedSocialCreditCode,
			address:this.address,

            busiState:"未备案",
            approvalState:"待提交",

            contactPerson : this.contactPerson,
            contactPhone : this.contactPhone,
            isUsedState : this.isUsedState,

            //机构成员
            orgMemberList:this.orgMemberList,

			//附件材料
            busiType:'020106',
            generalAttachmentList:this.$refs.listenUploadData.uploadData,

            buttonType : "1"
		}
	}
	
	function addEmmp_CompanyWitness()
	{
		console.log(addVue.getAddForm());
		new ServerInterfaceJsonBody(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj){
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                generalSuccessModal();
                enterNewTabCloseCurrent(jsonObj.emmp_CompanyInfo.tableId, '进度见证服务单位详情', 'emmp/Emmp_CompanyWitnessDetail.shtml');
            }
		});
	}

    function getSubmitForm()
    {
        return {
            interfaceVersion:this.interfaceVersion,
            theType:this.theType,
            eCode:this.eCode,
            theName:this.theName,
            registeredDateStr:$('#date0201060101').val(),
            qualificationGrade:this.qualificationGrade,
            legalPerson:this.legalPerson,
            unifiedSocialCreditCode:this.unifiedSocialCreditCode,
            address:this.address,

            busiState:"未备案",
            approvalState:"审核中",

            contactPerson : this.contactPerson,
            contactPhone : this.contactPhone,
            isUsedState : this.isUsedState,

            //机构成员
            orgMemberList:this.orgMemberList,

            //附件材料
            busiType:'020106',
            generalAttachmentList:this.$refs.listenUploadData.uploadData,

            buttonType : "2"
        }
    }

    function submitEmmp_CompanyWitness()
    {
    	addVue.flag  = true;
        new ServerInterfaceJsonBody(baseInfo.addInterface).execute(addVue.getSubmitForm(), function(jsonObj){
        	addVue.flag  = false;
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                generalSuccessModal();
                enterNewTabCloseCurrent(jsonObj.emmp_CompanyInfo.tableId, '进度见证服务单位详情', 'emmp/Emmp_CompanyWitnessDetail.shtml');
            }
        });
    }

    /********* OrgMember 相关 *********/


    function toggleSelection(rows) {

    }

    function handleSelectionChange(val) {
        addVue.selectItem = [];
        for (var index = 0; index < val.length; index++) {

			console.log(val[index]);

            var element = val[index];

            addVue.selectItem.push(element)
        }
    }

    function getParameterNameList ()
    {
        //判断是否请求过通用字段列表
        if (addVue.parameterNameList.length > 0) {
            return;
        }

        var model = {
            interfaceVersion:addVue.interfaceVersion,
            theName:"职务"
        };

        new ServerInterface(baseInfo.baseParameterListInterface).execute(model, function (jsonObj)
        {
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {

                addVue.parameterNameList = jsonObj.sm_BaseParameterList;
            }
        });
    }

    function addOrgMember(divStr)
    {
        var isPass = errorCheckForAll(divStr);

        if (!isPass) {

            console.log("PassFailed");

            return;
        }

        for (var i = 0; i < addVue.parameterNameList.length; i++ )
        {
            var parameter = addVue.parameterNameList[i];

            if (parameter.tableId == addVue.addParameterNameId)
            {
                addVue.addParameterName = parameter.theValue;
                break;
            }
        }

        var model = {
            theName:addVue.addTheName,
            idType:addVue.addIdType,
            idNumber:addVue.addIdNumber,
            theNameOfDepartment:addVue.addTheNameOfDepartment,
            parameterId:addVue.addParameterNameId,
            parameterName:addVue.addParameterName,
            positionName:addVue.addPositionName,
            phoneNumber:addVue.addPhoneNumber,
            email:addVue.addEmail,
            qq:addVue.addQq,
            weixin:addVue.addWeixin
        };

        addVue.orgMemberList.push(model);
		
        addVue.addTheName = "";
        addVue.addIdType = "";
        addVue.addIdNumber = "";
        addVue.addTheNameOfDepartment = "";
        addVue.addParameterNameId = "";
        addVue.addPositionName = "";
        addVue.addPhoneNumber = "";
        addVue.addEmail = "";
        addVue.addQq = "";
        addVue.addWeixin = "";

        $(baseInfo.addPersonModal).modal('hide');
    }

    function orgMemberDeleteHandle() {

        if(addVue.selectItem.length == 0)
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

        for (var i = 0; i < addVue.orgMemberList.length; i++)
        {
            var needDelete = false;

            for (var j = 0; j < addVue.selectItem.length; j++)
            {
                if (addVue.orgMemberList[i] == addVue.selectItem[j])
                {
                    needDelete = true;
                }
            }
			if (needDelete == false)
			{
                newOrgMemberList.push(addVue.orgMemberList[i]);
			}
        }

        addVue.orgMemberList = newOrgMemberList;

        $(baseInfo.deleteOrgMemberAlert).modal('hide');
    }

    /********* 附件材料 相关 *********/

    function loadUpload(){
        model = {
            pageNumber : '0',
            busiType : '020106',
            interfaceVersion:addVue.interfaceVersion
        };

        new ServerInterface(baseInfo.loadUploadInterface).execute(model, function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                addVue.loadUploadList = jsonObj.sm_AttachmentCfgList;
            }
        });
    }

    function initValue()
    {

        laydate.render({
            elem: '#date0201060101',
            max: getNowFormatDate()
        });

        getParameterNameList();		//获取机构成员职务字段在通用字段表里的数据

        loadUpload(); 				//获取附件材料

        getParameter(addVue.parameterType, addVue.theType, function(parameter){
            addVue.companyTypeName = parameter == null ? "" : parameter.theName;
        })
    }

	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	addVue.initValue();

	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#emmp_CompanyWitnessAddDiv",
	"addInterface":"../Emmp_CompanyWitnessAdd",
	//机构成员
    "addPersonModal":"#addPerson",
    "deleteOrgMemberAlert":"#deleteOrgMember",
    "baseParameterListInterface":"../Sm_BaseParameterList",
    //材料附件
    "loadUploadInterface":"../Sm_AttachmentCfgList"
});
