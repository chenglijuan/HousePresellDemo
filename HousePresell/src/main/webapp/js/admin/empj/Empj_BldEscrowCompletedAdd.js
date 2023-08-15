(function(baseInfo){
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
            interfaceVersion :19000101,
            pageNumber : 1,
            countPerPage : 20,
            userId: 1,
			userType: 2,
            developCompanyId: "",  	//10132
            developCompanyName: "",
            emmp_CompanyInfoList : [],
            empj_ProjectInfoList: [],
            projectId: "",
            eCodeFromDRAD: "",
            allBldEscrowSpace: 0.0,
            currentBldEscrowSpace: 0.0,
            remark: "",
            cityRegionName: "",
            address: "",
			empj_BuildingInfoList: [],
            selectItem : [],
            multipleSelection: [],
            multipleSelection: [],
            empj_BldEscrowCompletedAddDtltab: [],
            buttonType : "",
			
            //附件材料
            busiType : '03030102',
            busiCode : "03030102", //业务编码
            loadUploadList: [],
            showDelete : true,  //附件是否可编辑
            hasFormulaList : [
            	{
            		tableId:"0",
            		theName:"否"
            	},
            	{
            		tableId:"1",
            		theName:"是"
            	}
            ],
            
            hasFormula : "1",
            webSite  : ""
		},
		methods : {
			//详情
			initData: initData,
			//添加
			getAddForm : getAddForm,
			add: add,
			hasFormulaChange : hasFormulaChange,
			hasFormulaEmpty : hasFormulaEmpty,
			//项目列表，选择项目
			getProjectInfoList: getProjectInfoList,
            companyInfoChange: companyInfoChange,
            resetCompanyInfoChange: resetCompanyInfoChange,
            projectInfoChange: projectInfoChange,
            resetProjectInfoChange: resetProjectInfoChange,
            handleSelectionChange: handleSelectionChange,
            indexMethod: function (index) {
                if (addVue.pageNumber > 1) {
                    return (addVue.pageNumber - 1) * addVue.countPerPage - 0 + (index - 0 + 1);
                }
                if (addVue.pageNumber <= 1) {
                    return index - 0 + 1;
                }
            },
            checkboxInit: function checkboxInit(row, index) {
                // console.log(row);
                if (row.escrowState != "已托管") {
                    return 0; //不可勾选
                }
                else{
                    return 1; //可勾选
                }
            },
            keyupCodeFromPublicSecurity : keyupCodeFromPublicSecurity,
		},
		computed:{
			 
		},
		components : {
            'vue-select': VueSelect,
            'vue-nav': PageNavigationVue,
            "my-uploadcomponent":fileUpload
		},
		watch:{

		}
	});

	//------------------------方法定义-开始-------------------//

	function keyupCodeFromPublicSecurity(ev,index){
		
		console.log(ev.srcElement.value);
		console.log("eCodeFromPublicSecurity:"+index);
		console.log(addVue.multipleSelection);
		console.log(addVue.empj_BuildingInfoList);
		
		for(var i=0;i < addVue.multipleSelection.length;i++){
			if(addVue.multipleSelection[i].tableId == addVue.empj_BuildingInfoList[index].tableId){
				addVue.multipleSelection[i].eCodeFromPublicSecurity = ev.srcElement.value;
				break;
			}
		}
		
	}
	//选择开发企业
	function companyInfoChange(data)
	{
		// console.log(data);
        this.developCompanyId = data.tableId;

        addVue.projectId = "";
        addVue.cityRegionName = "";
        addVue.address = "";
        addVue.allBldEscrowSpace = 0.0;
        addVue.currentBldEscrowSpace = 0.0;

        addVue.empj_BuildingInfoList = [];

        getProjectInfoList();
    }

    function resetCompanyInfoChange()
    {
        addVue.projectId = "";
        addVue.cityRegionName = "";
        addVue.address = "";
        addVue.allBldEscrowSpace = 0.0;
        addVue.currentBldEscrowSpace = 0.0;
        addVue.empj_BuildingInfoList = [];

        addVue.empj_BldEscrowCompletedAddDtltab = [];
    }

	//选择项目
	function projectInfoChange(data)
	{
        addVue.projectId = data.tableId;
        addVue.cityRegionName = data.cityRegionName;
        addVue.address = data.address;

        addVue.empj_BuildingInfoList = [];
        getBuildingInfoList();
    }
	
	function hasFormulaChange(data){
		addVue.hasFormula = data.tableId;
	}
	
	function hasFormulaEmpty(){
		addVue.hasFormula = "";
	}

    function resetProjectInfoChange()
    {
        addVue.projectId = "";
        addVue.cityRegionName = "";
        addVue.address = "";
        addVue.allBldEscrowSpace = 0.0;
        addVue.currentBldEscrowSpace = 0.0;
        addVue.empj_BuildingInfoList = [];

        addVue.empj_BldEscrowCompletedAddDtltab = [];
    }

    //获取楼幢列表信息
	function getBuildingInfoList()
	{
		var model = {
			interfaceVersion:addVue.interfaceVersion,
            theState:0,
            projectId:addVue.projectId,
		};
        // console.log(model);
		new ServerInterface(baseInfo.getBuildingListInterface).execute(model, function(jsonObj) {
			if (jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				// console.log(jsonObj);
				addVue.empj_BuildingInfoList = jsonObj.empj_BuildingInfoList;

				var listCount = jsonObj.empj_BuildingInfoList.length;
                addVue.countPerPage = 0;
				if (listCount > 0)
                {
                    addVue.countPerPage = listCount;
                }

                addVue.allBldEscrowSpace = 0.0;
				for (var index = 0; index < jsonObj.empj_BuildingInfoList.length; index++)
				{
				    var escrowArea = addVue.empj_BuildingInfoList[index].escrowArea;
				    if (escrowArea != null)
                    {
                        addVue.allBldEscrowSpace += addVue.empj_BuildingInfoList[index].escrowArea;
                    }
				}
				if(addVue.allBldEscrowSpace !="" && addVue.allBldEscrowSpace !=undefined){
					addVue.allBldEscrowSpace = (addVue.allBldEscrowSpace).toFixed(2);
				}
			}
        });
    }

    //选择楼幢
	function selectedItemChanged()
	{
		
    }

    //获取选中复选框所在行的tableId
    function handleSelectionChange(val)
	{
    	console.log(val);
		//此处处理要考虑已经办理托管终止的楼幢
        addVue.multipleSelection = val;
		addVue.selectItem = [];
        addVue.currentBldEscrowSpace = 0.0;
		for (var index = 0; index < val.length; index++){
			var element = val[index].tableId;
			var escrowArea = val[index].escrowArea;
			if (escrowArea != null)
            {
                addVue.selectItem.push(element);
                addVue.currentBldEscrowSpace += escrowArea;
            }
		}
		if(addVue.currentBldEscrowSpace !=undefined && addVue.currentBldEscrowSpace != ""){
			addVue.currentBldEscrowSpace = (addVue.currentBldEscrowSpace).toFixed(2)
		}
		generateSummary();
    }

    //汇总所选楼栋明细信息
	function generateSummary()
	{
		addVue.empj_BldEscrowCompletedAddDtltab = [];
		
		
		console.log(addVue.multipleSelection);
		
		for (var i = 0; i < addVue.multipleSelection.length; i++)
		{
			var row = addVue.multipleSelection[i];
			addVue.empj_BldEscrowCompletedAddDtltab.push({
                developCompanyId:addVue.developCompanyId,
                projectId:addVue.projectId,
                buildingId:row.tableId,
                eCodeFromPublicSecurity:row.eCodeFromPublicSecurity,
			});
		}
	}

	//添加托管终止申请
	function getAddForm()
	{
		
		for(var i = 0 ; i < addVue.empj_BldEscrowCompletedAddDtltab.length ; i++){
			for(var j = 0 ; j < addVue.empj_BuildingInfoList.length ; j ++ ){
				if(addVue.empj_BldEscrowCompletedAddDtltab[i].buildingId == addVue.empj_BuildingInfoList[j].tableId){
					addVue.empj_BldEscrowCompletedAddDtltab[i].eCodeFromPublicSecurity = addVue.empj_BuildingInfoList[j].eCodeFromPublicSecurity;
					break;
				}
			}
		}
		
		return {
            interfaceVersion:this.interfaceVersion,
			busiState:1,
			developCompanyId:this.developCompanyId,
			projectId:this.projectId,
			eCodeFromDRAD:this.eCodeFromDRAD,
			remark:this.remark,
            empj_BldEscrowCompletedAddDtltab:this.empj_BldEscrowCompletedAddDtltab,
            buttonType:this.buttonType,
            
            hasFormula : addVue.hasFormula,
            webSite : addVue.webSite,

            //附件材料
            busiType : this.busiType,
            busiCode: this.busiCode,
            generalAttachmentList : this.$refs.listenUploadData.uploadData
		}
	}

    //保存托管终止信息，发起审批流
	function add(buttonType)
	{
        addVue.buttonType = buttonType;
		new ServerInterfaceJsonBody(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj)
		{
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {		
            	addVue.saveFlag = false;
            	addVue.flag = false;
            	generalSuccessModal();
	            enterNewTabCloseCurrent(jsonObj.tableId,"托管终止详情","empj/Empj_BldEscrowCompletedDetail.shtml");
				
            }
		});
	}

	function getLoginUserInfo()
	{
        var model = {
            interfaceVersion : 19000101,
        };
        new ServerInterface(baseInfo.getLoginSm_UserInterface).execute(model, function (jsonObj) {
            if (jsonObj.result != "success")
            {
                addVue.userId = "";
                generalErrorModal(jsonObj);
            }
            else
            {
                // console.log(jsonObj);
                addVue.userId = jsonObj.sm_User.tableId;
                addVue.developCompanyName = jsonObj.sm_User.theNameOfCompany;
                addVue.userType = jsonObj.sm_User.theType;
                console.log("用户类型------"+addVue.userType);

                //非正泰用户，开发企业直接显示，无法选择，默认选中
                if (addVue.userType == 1)
                {
                    //正泰用户，可自由选择开发企业
                    getCompanyList();
                }
                else
                {
                    addVue.developCompanyId = jsonObj.sm_User.developCompanyId;//非正泰用户，开发企业直接显示，无法选择，默认选中
                    addVue.emmp_CompanyInfoList = [];
                    var theModel = {
                        tableId: addVue.developCompanyId,
                        theName: addVue.developCompanyName,
                        theType: " ",
                    };
                    addVue.emmp_CompanyInfoList.push(theModel);

                    getProjectInfoList();
                }
            }
        });
    }

    function getCompanyList()
	{
        var model = {
            interfaceVersion : 19000101,
            theState:0,
            theType : 1,   //类型选择开发企业，变量S_CompanyType
            busiState: "已备案",
        };
        new ServerInterface(baseInfo.companyListInterface).execute(model, function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
            	generalErrorModal(jsonObj);
            }
            else
            {
                addVue.emmp_CompanyInfoList = jsonObj.emmp_CompanyInfoList;

                getProjectInfoList();
            }
        });
    }

    //获取项目列表
    function getProjectInfoList()
    {
        var model = {
            interfaceVersion:addVue.interfaceVersion,
            theState:0,
            developCompanyId:addVue.developCompanyId,
        };
        new ServerInterface(baseInfo.getProjectListInterface).execute(model, function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
            	generalErrorModal(jsonObj);
            }
            else
            {
                // console.log(jsonObj);
                addVue.empj_ProjectInfoList = jsonObj.empj_ProjectInfoList;
            }
        });
    }

	function initData()
	{
        getLoginUserInfo();

        generalLoadFile2(addVue, addVue.busiType)
	}
	//------------------------方法定义-结束-----------------//
	
	//------------------------数据初始化-开始----------------//

    addVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#empj_BldEscrowCompletedAddDiv",
    "getLoginSm_UserInterface":"../Sm_UserGet",
    "companyListInterface":"../Emmp_CompanyInfoForSelect",
    "getProjectListInterface":"../Empj_ProjectInfoForSelect",
	"getBuildingListInterface":"../Empj_BuildingInfoBldEscrowList",  //Empj_BuildingInfoBldEscrowList,  ///Empj_BuildingInfoList
    "addInterface":"../Empj_BldEscrowCompletedAdd",
});
