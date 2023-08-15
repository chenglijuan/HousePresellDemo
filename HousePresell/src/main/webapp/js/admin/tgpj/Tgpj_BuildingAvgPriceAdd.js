(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
            tgpj_BuildingAvgPriceModel: {},
            user: {},
            projectList: [],
            projectId: "",
            projectName: "",
			buildingList:[],
			buildingInfoId:"",
            buildingEcode:"",
            buildingEcodeFromConstruction:"",
			companyList:[],
			companyId:"",
			companyName:"",
            publicSecurity:"",
			tableId:"",
            isNormalUser:0,

            //附件材料
			busiType:'03010301',

            loadUploadList: [],
            showDelete : true,  //附件是否可编辑

            buttonType : "",
            presellPrice:"",
            isShowPresell:false,
            showSubFlag : true,
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			//添加
			getAddForm : getAddForm,
			add: add,
            buildingChangeHandle:buildingChangeHandle,
			companyChangeHandle:companyChangeHandle,
            projectChangeHandle:projectChangeHandle,
            changeThousands:function(){
                detailVue.tgpj_BuildingAvgPriceModel.recordAveragePrice = addThousands(detailVue.tgpj_BuildingAvgPriceModel.recordAveragePrice);
            },
		},
		computed:{

		},
        components: {
            "my-uploadcomponent": fileUpload,
            'vue-select': VueSelect,
        },
		watch:{

		}
	});

	//------------------------方法定义-开始------------------//
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:detailVue.interfaceVersion,
		}
	}

	//详情操作--------------
	function refresh()
	{

	}

	//详情更新操作--------------获取"更新机构详情"参数
	function getAddForm()
	{
		return {
			interfaceVersion:detailVue.interfaceVersion,
			theState:detailVue.tgpj_BuildingAvgPriceModel.theState,
			busiState:detailVue.tgpj_BuildingAvgPriceModel.busiState,
			eCode:detailVue.tgpj_BuildingAvgPriceModel.eCode,
			createTimeStamp:detailVue.tgpj_BuildingAvgPriceModel.createTimeStamp,
			lastUpdateTimeStamp:detailVue.tgpj_BuildingAvgPriceModel.lastUpdateTimeStamp,
			userRecordId:detailVue.tgpj_BuildingAvgPriceModel.userRecordId,
			recordTimeStamp:detailVue.tgpj_BuildingAvgPriceModel.recordTimeStamp,
			recordAveragePrice:commafyback(detailVue.tgpj_BuildingAvgPriceModel.recordAveragePrice),
			// buildingInfoId:detailVue.tgpj_BuildingAvgPriceModel.buildingInfoId,
            buildingInfoId:detailVue.buildingInfoId,
            remark:detailVue.tgpj_BuildingAvgPriceModel.remark,
			// averagePriceRecordDate:detailVue.tgpj_BuildingAvgPriceModel.averagePriceRecordDate,
            averagePriceRecordDateString:$('#date0301030101').val(),
			recordAveragePriceFromPresellSystem:commafyback(detailVue.tgpj_BuildingAvgPriceModel.recordAveragePriceFromPresellSystem),
            buttonType:detailVue.buttonType,
	            //附件材料
            busiType : detailVue.busiType,
            generalAttachmentList : this.$refs.listenUploadData.uploadData	}
	}

	function add(buttonType)
	{
		detailVue.buttonType=buttonType;
		if(buttonType == 2){
			detailVue.showSubFlag = false;
        } 
		/*serverBodyRequest(baseInfo.addInterface,detailVue.getAddForm(),function (jsonObj) {
			detailVue.showSubFlag = true;
            generalSuccessModal()
            enterNewTabCloseCurrent(jsonObj.tableId, "备案均价详情", "tgpj/Tgpj_BuildingAvgPriceDetail.shtml")
        })*/
		new ServerInterfaceJsonBody(baseInfo.addInterface).execute(detailVue.getAddForm(), function (jsonObj) {
			detailVue.showSubFlag = true;
	        if (jsonObj.result != "success") {
	        	generalErrorModal(jsonObj)
	        }
	        else {
	        	 generalSuccessModal()
		         enterNewTabCloseCurrent(jsonObj.tableId, "备案均价详情", "tgpj/Tgpj_BuildingAvgPriceDetail.shtml")
	        }
	    });
		
		
	}

    // function getUserDetail() {
		// serverRequest(baseInfo.userDetailInterface,{interfaceVersion:19000101,tableId:detailVue.user.tableId},function (jsonObj) {
    //         detailVue.companyId = jsonObj.developCompanyId
    //         detailVue.companyName = jsonObj.theNameOfCompany
    //     })
    // }
    //
	function getConstructionList() {
		serverRequest(baseInfo.constructionListInterface,{interfaceVersion:19000101},function (jsonObj) {
			detailVue.buildingList=jsonObj.buildingList
        })
    }

    function getPresellPrice(buildingId) {
		serverRequest("../Empj_BuildingGetPresellPrice",{interfaceVersion:19000101,tableId:buildingId},function (jsonObj) {
			detailVue.presellPrice=addThousands(jsonObj.presellPrice)
        },function () {
            
        })
    }

    function initData() {
        generalLoadFile2(detailVue, detailVue.busiType)
        getCompanyList()
        getLoginUserInfo(function (user) {
            detailVue.user = user
            detailVue.companyId = user.developCompanyId
            detailVue.companyName = user.theNameOfCompany
            // getUserDetail()
			if(detailVue.user.theType=="1"){//如果是正泰用户
				detailVue.isNormalUser=1
                detailVue.companyId =""
                detailVue.companyName =""
                detailVue.isShowPresell=true
            }else{//如果是一般用户
                detailVue.isNormalUser=0
				getProjectList()
                detailVue.isShowPresell=false
			}
        })

        laydate.render({
            elem: '#date0301030101'
        });
    }

    function getCompanyList() {
        var form=getTotalListForm()
        form["theType"]=1
        form["exceptZhengTai"]=true
		serverRequest(baseInfo.companyListInterface,form,function (jsonObj) {
			detailVue.companyList=jsonObj.emmp_CompanyInfoList
        })
    }

    function getProjectList() {
        serverRequest(baseInfo.projectListInterface,{
            interfaceVersion: 19000101,
            pageNumber: 1,
            countPerPage: MAX_VALUE,
            theState: 0,
            totalPage: 1,
            keyword: "",
            developCompanyId:detailVue.companyId,
        },function (jsonObj) {
            detailVue.projectList=jsonObj.empj_ProjectInfoList
        })
    }

    function getBuildingList() {
        serverRequest(baseInfo.buildingListInterface,{
            interfaceVersion: 19000101,
            pageNumber: 1,
            countPerPage: MAX_VALUE,
            theState: 0,
            totalPage: 1,
            keyword: "",
            projectId:detailVue.projectId,
        },function (jsonObj) {
            detailVue.buildingList=jsonObj.empj_BuildingInfoList
			for(var i=0;i<detailVue.buildingList.length;i++){
            	detailVue.buildingList[i].theName=detailVue.buildingList[i].eCodeFromConstruction
			}
        })
    }

	function buildingChangeHandle(data) {
		detailVue.buildingEcodeFromConstruction=data.eCodeFromConstruction
		detailVue.publicSecurity=data.eCodeFromPublicSecurity
		detailVue.buildingInfoId=data.tableId
		getPresellPrice(detailVue.buildingInfoId)
        // if(jsonObj.tgpj_BuildingAvgPrice.recordAveragePriceFromPresellSystem!=undefined){
         //    detailVue.presellPrice = addThousands(jsonObj.tgpj_BuildingAvgPrice.recordAveragePriceFromPresellSystem)
        // }
		isUniqueBuilding()
        // serverRequest(baseInfo.buildingListInterface,{
        //     interfaceVersion: 19000101,
			// tableId:data.tableId,
			// theState:0,
        //
        // },function (jsonObj) {
        // })

        // for(var i=0 ;i<detailVue.buildingList.length;i++){
        //     if (detailVue.buildingList[i].tableId === detailVue.buildingInfoId) {
        //         detailVue.projectName = detailVue.buildingList[i].projectName
        //         detailVue.companyName = detailVue.buildingList[i].developCompanyName
        //         detailVue.publicSecurity = detailVue.buildingList[i].eCodeFromPublicSecurity
        //         detailVue.buildingEcode = detailVue.buildingList[i].eCode
        //         detailVue.buildingInfoId=detailVue.buildingList[i].tableId
        //
        //     }
        // }
    }

    function isUniqueBuilding() {
		var form={
			interfaceVersion:19000101,
            buildingInfoId:detailVue.buildingInfoId
		}
		serverRequest("../Tgpj_BuildingAvgPriceIsUnique",form,function (jsonObj) {
			var isUnique=jsonObj.isUnique
			if(!isUnique){
				generalErrorModal(undefined,"该楼幢已经存在备案均价，无法重复添加")
			}
        })
    }

    function companyChangeHandle(data) {
		if(data.tableId!=detailVue.companyId){
            detailVue.companyId=data.tableId
            detailVue.companyName=data.theName
			detailVue.projectList=[]
			detailVue.projectName=""
			detailVue.buildingList=[]
			detailVue.buildingInfoId=""
            detailVue.buildingEcodeFromConstruction=""
            detailVue.publicSecurity=""
			getProjectList()
		}

    }

    function projectChangeHandle(data) {
		if(data.tableId!=detailVue.projectId){
            detailVue.projectId=data.tableId
            detailVue.projectName=data.theName
            detailVue.buildingList=[]
            detailVue.buildingInfoId=""
            detailVue.buildingEcodeFromConstruction=""
            detailVue.publicSecurity=""
            getBuildingList()
		}

    }
	//------------------------方法定义-结束------------------//

	//------------------------数据初始化-开始----------------//
	// detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#tgpj_BuildingAvgPriceAddDiv",
	"detailInterface":"../Tgpj_BuildingAvgPriceDetail",
	"addInterface":"../Tgpj_BuildingAvgPriceAdd",
	"userDetailInterface":"../Sm_UserDetail",
	"projectListInterface":"../Empj_ProjectInfoForSelect",
	"buildingListInterface":"../Empj_BuildingInfoForSelect",
	"companyListInterface":"../Emmp_CompanyInfoForSelect",
    "buildingDetailInterface":"../Empj_BuildingInfoDetail",
	"constructionListInterface":"../Sm_UserGetBuildingList",
});
