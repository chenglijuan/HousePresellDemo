(function(baseInfo){
	var addVue = new Vue({
		el: baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			eCode : "",
            theType : "1",
            parameterType : "8",
            companyTypeName : "",
			companyGroup : "",
			theName : "",
			qualificationGrade : "",
			legalPerson : "",
			unifiedSocialCreditCode : "",
			projectLeader : "",
			cityRegionId : "",
			streetId : "",
			address : "",

            contactPhone : "",
            isUsedState: "1",

			sm_CityRegionInfoList : [],
			sm_StreetInfoList: [],

            //机构成员相关
            pageNumber: 1,
            countPerPage: 10,
            totalPage: 1,
            totalCount: 1,
            theState: 0,//正常为0，删除为1
            selectItem: [],
            orgMemberList: [],

			theOrgMemberId : 1,

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

            //附件材料
            busiType:'020101',
            loadUploadList: [],
            showDelete : true,  //附件是否可编辑
            qualificationGradeList : [
            	{tableId:"1",theName:"01-壹级"},
            	{tableId:"2",theName:"02-贰级"},
            	{tableId:"3",theName:"03-叁级"},
            	{tableId:"4",theName:"04-肆级"},
            	{tableId:"5",theName:"05-暂壹级"},
            	{tableId:"6",theName:"06-暂贰级"},
            	{tableId:"7",theName:"07-暂叁级"},
            	{tableId:"99",theName:"99-其他"},
            ],
            isUsedStateList : [
            	{tableId:"1",theName:"是"},
            	{tableId:"0",theName:"否"},
            ],
            flag : true,
		},
		methods : {
			getAddForm : getAddForm,
			add : addEmmp_CompanyInfo,
            getSubmitForm : getSubmitForm,
            submit : submitEmmp_CompanyInfo,
			getCityRegionInfoForm : getCityRegionInfoForm,
			initValue : initValue,
			getStreetInfoForm : getStreetInfoForm, 
			
			getStreetInfo : getStreetInfo,
			getRegionId : function(data){
				addVue.cityRegionId = data.tableId;
				addVue.getStreetInfo();
			},
			emptyRegionId : function(){
				addVue.cityRegionId = null;
				addVue.sm_StreetInfoList = [];
			},
			getIsUsedState : function(data){
				addVue.isUsedState = data.tableId;
			},
			emptyIsUsedState : function(){
				addVue.isUsedState = null;
			},
			getStreetId : function(data){
				addVue.streetId = data.tableId;
			},
			emptyStreetId : function(){
				addVue.streetId = null;
			},
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
                    return (addVue.pageNumber - 1) * addVue.countPerPage - 0 + (index - 0 + 1);
                }
                if (addVue.pageNumber <= 1) {
                    return index - 0 + 1;
                }
            },
            qualificationGradeChange : function(data){
            	addVue.qualificationGrade = data.tableId;
            },
            emptyQualificationGrade : function(){
            	addVue.qualificationGrade = null;
            }
            
		},
		computed:{
			 
		},
		components: {
			'vue-select': VueSelect ,
            'vue-nav': PageNavigationVue,
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
			companyGroup:this.companyGroup,
			theName:this.theName,
			registeredDateStr:$('#date0201010101').val(),
			qualificationGrade:this.qualificationGrade,
			legalPerson:this.legalPerson,
			unifiedSocialCreditCode:this.unifiedSocialCreditCode,
			projectLeader:this.projectLeader,
			cityRegionId:this.cityRegionId,
			streetId:this.streetId,
			address:this.address,

            contactPhone : this.contactPhone,
            isUsedState : this.isUsedState,

            busiState:"未备案",
            approvalState:"待提交",
            //机构成员
            orgMemberList:this.orgMemberList,

			//附件材料
            busiType:'020101',
            generalAttachmentList:this.$refs.listenUploadData.uploadData,

            buttonType : "1"
		}
	}
	
	function addEmmp_CompanyInfo()
	{
		new ServerInterfaceJsonBody(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj){

            console.log(jsonObj);

            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                generalSuccessModal();
                enterNewTabCloseCurrent(jsonObj.emmp_CompanyInfo.tableId, '开发企业详情', 'emmp/Emmp_CompanyInfoDetail.shtml');
            }
		});
	}

    //机构信息操作--------------获取“新增机构”表单参数
    function getSubmitForm()
    {
        return {
            interfaceVersion:this.interfaceVersion,
            theType:this.theType,
            eCode:this.eCode,
            companyGroup:this.companyGroup,
            theName:this.theName,
            registeredDateStr:$('#date0201010101').val(),
            qualificationGrade:this.qualificationGrade,
            legalPerson:this.legalPerson,
            unifiedSocialCreditCode:this.unifiedSocialCreditCode,
            projectLeader:this.projectLeader,
            cityRegionId:this.cityRegionId,
            streetId:this.streetId,
            address:this.address,

            contactPhone : this.contactPhone,
            isUsedState : this.isUsedState,

            busiState:"未备案",
            approvalState:"审核中",
            //机构成员
            orgMemberList:this.orgMemberList,

            //附件材料
            busiType:'020101',
            generalAttachmentList:this.$refs.listenUploadData.uploadData,

            buttonType : "2"
        }
    }

    function submitEmmp_CompanyInfo()
    {
    	addVue.flag = false;
        new ServerInterfaceJsonBody(baseInfo.addInterface).execute(addVue.getSubmitForm(), function(jsonObj){
        	addVue.flag = true;
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                generalSuccessModal();
                enterNewTabCloseCurrent(jsonObj.emmp_CompanyInfo.tableId, '开发企业详情', 'emmp/Emmp_CompanyInfoDetail.shtml');
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
		addVue.streetId = "";
		new ServerInterface(baseInfo.streetInfoInterface).execute(addVue.getStreetInfoForm(), function(jsonObj){
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
				addVue.sm_StreetInfoList = jsonObj.sm_StreetInfoList;
			}
		});
	}

	//获取城市区域基本信息--表单参数
	function getCityRegionInfoForm() 
	{
		return {
			interfaceVersion:this.interfaceVersion,
		}
	}
    /********* OrgMember 相关 *********/


    function toggleSelection(rows) {

    }


    function handleSelectionChange(val) {

        addVue.selectItem = [];

        for (var index = 0; index < val.length; index++) {

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
            theName:"职务",
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
        var isPass = errorCheckForAll1(divStr, addVue.addIdType);

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
        var model = {
            pageNumber : '0',
            busiType : '020101',
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
            elem: '#date0201010101',
            max: getNowFormatDate()
        });

        new ServerInterface(baseInfo.cityRegionInfoInterface).execute(addVue.getCityRegionInfoForm(), function(jsonObj){
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                addVue.sm_CityRegionInfoList = jsonObj.sm_CityRegionInfoList;
            }
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
	"addDivId":"#emmp_CompanyInfoAddDiv",
	"addInterface":"../Emmp_CompanyInfoAdd",
	"cityRegionInfoInterface":"../Sm_CityRegionInfoList",
	"streetInfoInterface":"../Sm_StreetInfoList",
	//机构成员
    "addPersonModal":"#addPerson23",
    "deleteOrgMemberAlert":"#deleteOrgMember",
    "baseParameterListInterface":"../Sm_BaseParameterList",
    //材料附件
    "loadUploadInterface":"../Sm_AttachmentCfgList"
});
