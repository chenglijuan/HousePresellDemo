(function(baseInfo){
	var updateVue = new Vue({
		el : baseInfo.updateDivId,
		data : {
			interfaceVersion :19000101,
            pageNumber : 1,
            countPerPage : 20,
			tableId : 0,
            mainTableId: 0,
            eCodeFromDRAD: "",
            projectId: 0,
            projectName: "",
            empj_ProjectInfoList: [],
            allBldEscrowSpace: 0.0,
            currentBldEscrowSpace: 0.0,
            remark: "",
            developCompanyId: 0,
            developCompanyName: "",
            cityRegionName: "",
            address: "",
            userStartId: 0,
			userStartName: "",
			createTimeStamp: "",
            userRecordId: "",
            userRecordName: "",
			recordTimeStamp: "",
			remark: "",
            empj_BuildingInfoList: [],
            empj_BldEscrowCompleted_DtlList: [],
            selectItem : [],
            multipleSelection: [],
            empj_BldEscrowCompletedAddDtltab: [],
            buttonType : "",
            

            //附件材料
            busiType : '03030102',
			busiCode : "03030102", //业务编码
            loadUploadList: [],
            showDelete : true,
            webSite : "",
            hasFormula : "1",
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
            
		},
		methods : {
            initData: initData,
            hasFormulaChange : hasFormulaChange,
			hasFormulaEmpty : hasFormulaEmpty,
			//详情
			refresh : refresh,
            getDetailForm : getDetailForm,
            //项目列表，选择项目
            getProjectInfoList: getProjectInfoList,
            projectInfoChange: projectInfoChange,
            resetProjectInfoChange: resetProjectInfoChange,
            getBuildingInfoList: getBuildingInfoList,
			//更新
			getUpdateForm : getUpdateForm,
            handleSelectionChange: handleSelectionChange,
			update: update,
            indexMethod: function (index) {
                if (updateVue.pageNumber > 1) {
                    return (updateVue.pageNumber - 1) * updateVue.countPerPage - 0 + (index - 0 + 1);
                }
                if (updateVue.pageNumber <= 1) {
                    return index - 0 + 1;
                }
            },
            checkboxInit: function checkboxInit(row, index) {
                if (row.escrowState != "已托管") {
                    return 0; //不可勾选
                }
                else{
                    return 1; //可勾选
                }
            }
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

	//------------------------方法定义-开始------------------//

	function hasFormulaChange(data){
		updateVue.hasFormula = data.tableId;
	}
	
	function hasFormulaEmpty(){
		updateVue.hasFormula = "";
	}
    //获取项目列表
    function getProjectInfoList()
    {
    	var model = {
            interfaceVersion:updateVue.interfaceVersion,
            theState:0,
            developCompanyId:updateVue.developCompanyId,
        };
        new ServerInterface(baseInfo.getProjectListInterface).execute(model, function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
                noty({"text":jsonObj.info,"type":"error","timeout":2000});
            }
            else
            {
                updateVue.empj_ProjectInfoList = jsonObj.empj_ProjectInfoList;
                // console.log(updateVue.empj_ProjectInfoList);
            }
        });
    }

    function projectInfoChange(data)
    {
        if (updateVue.projectId != data.tableId)
        {
            updateVue.empj_BldEscrowCompleted_DtlList = [];
        }
        updateVue.projectId = data.tableId;
        updateVue.cityRegionName = data.cityRegionName;
        updateVue.address = data.address;

        updateVue.empj_BuildingInfoList = [];
        getBuildingInfoList();
    }

    function resetProjectInfoChange()
    {
        updateVue.projectId = 0;
        updateVue.cityRegionName = "";
        updateVue.address = "";
        updateVue.allBldEscrowSpace = 0.0;
        updateVue.currentBldEscrowSpace = 0.0;
        updateVue.empj_BuildingInfoList = [];

        updateVue.empj_BldEscrowCompletedAddDtltab = [];
        updateVue.empj_BldEscrowCompleted_DtlList = [];
    }

    //获取楼幢列表信息
    function getBuildingInfoList()
    {
        var model = {
            interfaceVersion:updateVue.interfaceVersion,
            theState:0,
            projectId:updateVue.projectId,
        };
        // console.log(model);
        new ServerInterface(baseInfo.getBuildingListInterface).execute(model, function(jsonObj) {
            if (jsonObj.result != "success")
            {
                noty({"text":jsonObj.info,"type":"error","timeout":2000});
            }
            else
            {
            	
            	updateVue.empj_BldEscrowCompleted_DtlList
            	
            	
            	
                updateVue.empj_BuildingInfoList = jsonObj.empj_BuildingInfoList;
                var listCount = jsonObj.empj_BuildingInfoList.length;
                updateVue.countPerPage = 0;
                if (listCount > 0)
                {
                    updateVue.countPerPage = listCount;
                }
                
                for(var i = 0 ; i < listCount ; i ++){
                	for(var j = 0 ; j < updateVue.empj_BldEscrowCompleted_DtlList.length ; j ++){
                		if(updateVue.empj_BuildingInfoList[i].tableId == updateVue.empj_BldEscrowCompleted_DtlList[j].buildingId){
                			updateVue.empj_BuildingInfoList[i].eCodeFromPublicSecurity = updateVue.empj_BldEscrowCompleted_DtlList[j].eCodeFromPublicSecurity;
                			break;
                		}
                	}
                }

                //setTimeout(delaySetSelectionState, 500);
                updateVue.$nextTick(function () {
                    changeSelectionState();
                });
            }
        });
        
    }

    function changeSelectionState()
    {
        updateVue.allBldEscrowSpace = 0.0;
        for (var index = 0; index < updateVue.empj_BuildingInfoList.length; index++)
        {
            var itemBuildingInfo = updateVue.empj_BuildingInfoList[index];
            var escrowArea = itemBuildingInfo.escrowArea;
            if (escrowArea != null)
            {
                updateVue.allBldEscrowSpace += escrowArea;
            }
            for (var i = 0; i < updateVue.empj_BldEscrowCompleted_DtlList.length; i++)
            {
                if (itemBuildingInfo.tableId == updateVue.empj_BldEscrowCompleted_DtlList[i].buildingId)
                {
                    updateVue.$refs.multipleTable.toggleRowSelection(itemBuildingInfo,true);
                    break;
                }
            }
        }
        if(updateVue.allBldEscrowSpace !=undefined && updateVue.allBldEscrowSpace !=""){
        	updateVue.allBldEscrowSpace = (updateVue.allBldEscrowSpace).toFixed(2)
        }
        
        
    }

    //选择楼幢
    function selectedItemChanged()
    {
        console.log("--------------选择楼幢");
    }

    //获取选中复选框所在行的tableId
    function handleSelectionChange(val)
    {
        //此处处理要考虑已经办理托管终止的楼幢
        updateVue.multipleSelection = val;
        updateVue.selectItem = [];
        updateVue.currentBldEscrowSpace = 0.0;
        for (var index = 0; index < val.length; index++){
            var element = val[index].tableId;
            updateVue.selectItem.push(element);
            var escrowArea = val[index].escrowArea;
            if (escrowArea != null)
            {
                updateVue.currentBldEscrowSpace += escrowArea;
            }
        }
        // console.log(updateVue.selectItem);
        generateSummary();
    }

    //汇总所选楼栋明细信息
    function generateSummary()
    {
        updateVue.empj_BldEscrowCompletedAddDtltab = [];
        for (var i = 0; i < updateVue.multipleSelection.length; i++)
        {
            var row = updateVue.multipleSelection[i];
            updateVue.empj_BldEscrowCompletedAddDtltab.push({
                developCompanyId:updateVue.developCompanyId,
                projectId:updateVue.projectId,
                buildingId:row.tableId,
            });
        }
        // console.log(updateVue.empj_BldEscrowCompletedAddDtltab);
    }

	//详情操作-------------获取"机构详情"参数
	function getDetailForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:this.tableId,
            getDetailType:"1",
		}
	}

	//详情操作-----------
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
        // console.log(updateVue.getDetailForm());
		new ServerInterface(baseInfo.detailInterface).execute(updateVue.getDetailForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				// console.log(jsonObj);
				updateVue.eCode = jsonObj.empj_BldEscrowCompleted.eCode;
				updateVue.eCodeFromDRAD = jsonObj.empj_BldEscrowCompleted.eCodeFromDRAD;
				updateVue.developCompanyId = jsonObj.empj_BldEscrowCompleted.developCompanyId;
				updateVue.developCompanyName = jsonObj.empj_BldEscrowCompleted.developCompanyName;
				updateVue.projectId = jsonObj.empj_BldEscrowCompleted.projectId;
				updateVue.projectName = jsonObj.empj_BldEscrowCompleted.projectName;
				updateVue.cityRegionName = jsonObj.empj_BldEscrowCompleted.cityRegionName;
                updateVue.address = jsonObj.empj_BldEscrowCompleted.address;
				updateVue.remark = jsonObj.empj_BldEscrowCompleted.remark;
				updateVue.hasFormula = jsonObj.empj_BldEscrowCompleted.hasFormula;
				updateVue.webSite = jsonObj.empj_BldEscrowCompleted.webSite;

                updateVue.empj_BldEscrowCompleted_DtlList = jsonObj.empj_BldEscrowCompleted_DtlList;

                // console.log(updateVue.selectItem);
                updateVue.getProjectInfoList();
                updateVue.getBuildingInfoList(); //for循环比对tableId，实现回显功能，打印selectItem验证是否有数据，之后实现编辑功能
			}
		});
	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getUpdateForm()
	{
		
		for(var i = 0 ; i < updateVue.empj_BldEscrowCompletedAddDtltab.length ; i++){
			for(var j = 0 ; j < updateVue.empj_BuildingInfoList.length ; j ++ ){
				if(updateVue.empj_BldEscrowCompletedAddDtltab[i].buildingId == updateVue.empj_BuildingInfoList[j].tableId){
					updateVue.empj_BldEscrowCompletedAddDtltab[i].eCodeFromPublicSecurity = updateVue.empj_BuildingInfoList[j].eCodeFromPublicSecurity;
					break;
				}
			}
		}
		
		return {
			interfaceVersion:this.interfaceVersion,
            tableId:this.tableId,
			// userStartId:this.userStartId,
			// userRecordId:this.userRecordId,
			developCompanyId:this.developCompanyId,
			projectId:this.projectId,
            eCodeFromDRAD:this.eCodeFromDRAD,
            remark:this.remark,
            empj_BldEscrowCompletedAddDtltab:updateVue.empj_BldEscrowCompletedAddDtltab,
            buttonType:this.buttonType,

            hasFormula : updateVue.hasFormula,
            webSite : updateVue.webSite,
            //附件参数
            busiType : this.busiType,
            busiCode : this.busiCode,
            sourceId : this.tableId,
            
            
            
            generalAttachmentList : this.$refs.listenUploadData.uploadData,
            
		}
	}

    //更新托管终止信息
	function update(buttonType)
	{
        updateVue.buttonType = buttonType;
        // console.log(updateVue.getUpdateForm());
		new ServerInterfaceJsonBody(baseInfo.updateInterface).execute(updateVue.getUpdateForm(), function(jsonObj)
		{
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
            	
            	generalSuccessModal();
		        enterNewTabCloseCurrent(updateVue.tableId,"托管终止详情","empj/Empj_BldEscrowCompletedDetail.shtml");
		         
            }
		});
	}

    //提交托管终止信息，发起审批流
    function submit()
    {

    }

	function initData()
	{
        getIdFormTab("", function (id){
            updateVue.tableId = id;
            console.log(updateVue.tableId);
            refresh();  //获取托管终止申请详情信息
        });
        generalLoadFile2(updateVue, updateVue.busiType);
    }
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
    updateVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"updateDivId":"#empj_BldEscrowCompletedUpdateChangeDiv",
	"detailInterface":"../Empj_BldEscrowCompletedDetail",
	"updateInterface":"../Empj_BldEscrowCompletedChange",
    "getProjectListInterface":"../Empj_ProjectInfoForSelect",
    "getBuildingListInterface":"../Empj_BuildingInfoList"
});
