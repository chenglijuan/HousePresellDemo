(function(baseInfo){
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
            userId: 0,
            userName: "",
            busiState: "维护中",
            developCompanyId : "",
            developCompanyName: "",
            projectId: "",
            projectName: "",
            buildingId: "",
            // eCode: "",
            eCodeFromConstruction: "",
            eCodeFromPublicSecurity: "",
            position: "",
            upfloorNumber: "",
            deliveryType: "",  //1-毛坯房 2-成品房
            currentBldLimitAmountVerAfDtlId: "",
            currentFigureProgress: "",
            currentLimitedRatio: 100,
            patrolPerson: "",
            patrolTimestamp: "",
            remark: "",
            emmp_CompanyInfoList: [],
            empj_ProjectInfoList: [],
            empj_BuildingInfoList: [],
            buttonType: "1",

            
            //进度详情列表相关
            pageNumber : 1,
            countPerPage : 20,
            theState: 0,//正常为0，删除为1
            selectItem: [],
            selectModel: {},
            empj_PjDevProgressForcastDtlList: [],

            alertType: 1,   //1新增、2修改
            alertTitle: "新增进度预测", //进度弹框标题
            bldLimitAmountVerAfDtlId: "", //进度节点Id,
            stageName: "",
            limitedAmount: 100.0,
            figureProgressList: [], //进度节点列表
            predictedFinishDatetime: "",
            progressJudgement: "0",
            causeDescriptionForDelay: "",

		},
		methods : {
			//详情  
			initData: initData,
			getSearchForm : getSearchForm,
            changeDevelopCompanyInfo: changeDevelopCompanyInfo,
            resetDevelopCompanyInfo: resetDevelopCompanyInfo,
            changeProjectInfo: changeProjectInfo,
            resetProjectInfo: resetProjectInfo,
            changeECodeFromConstruction: changeECodeFromConstruction,
            resetECodeFromConstruction: resetECodeFromConstruction,

			//添加
			getAddForm : getAddForm,
			add: add,
            addProgressForcastHandle: addProgressForcastHandle,
            updateProgressForcastHandle: updateProgressForcastHandle,
            deleteProgressForcastHandle: deleteProgressForcastHandle,
            listItemSelectHandle: listItemSelectHandle,
            indexMethod: function (index) {
                if (addVue.pageNumber > 1) {
                    return (addVue.pageNumber - 1) * addVue.countPerPage - 0 + (index - 0 + 1);
                }
                if (addVue.pageNumber <= 1) {
                    return index - 0 + 1;
                }
            },

            //进度明细相关
            predictedFigureProgressChange: predictedFigureProgressChange,
            resetPredictedFigureProgress: resetPredictedFigureProgress,
            getProgressJudgementHandle: getProgressJudgementHandle,
            addOrUpdatePjDevProgressForcastDtl: addOrUpdatePjDevProgressForcastDtl,
            sortPjDevProgressForcastDtlList: sortPjDevProgressForcastDtlList,
		},
		computed:{
			 
		},
		components : {
            'vue-select': VueSelect,
            // 'vue-nav': PageNavigationVue,
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
		}
	}

	//获取开发企业列表
    function getCompanyList()
    {
        var model = {
            interfaceVersion : addVue.interfaceVersion,
            theState: 0,
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
				// console.log(jsonObj);
                addVue.emmp_CompanyInfoList = jsonObj.emmp_CompanyInfoList;
            }
        });
    }

    function changeDevelopCompanyInfo(data)
    {
        // if(addVue.developCompanyId != data.tableId)
        // {
        // }
        resetDevelopCompanyInfo();

        addVue.developCompanyId = data.tableId;
        getProjectList();
    }

    function resetDevelopCompanyInfo()
    {
        addVue.empj_ProjectInfoList = [];
        addVue.projectId = "";
        addVue.empj_BuildingInfoList = [];
        clearBuildingInfo();
        clearFigureProgressList();
        resetPredictedFigureProgress();
    }

    //获取项目列表
    function getProjectList()
    {
        var model = {
            interfaceVersion:addVue.interfaceVersion,
            theState: 0,
            developCompanyId:addVue.developCompanyId,
        };
        // console.log(model);
        new ServerInterface(baseInfo.projectListInterface).execute(model, function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
//				console.log(jsonObj.emmp_CompanyInfoList);
                addVue.empj_ProjectInfoList = jsonObj.empj_ProjectInfoList;
            }
        });
    }

    function changeProjectInfo(data)
    {
        resetProjectInfo();

        addVue.projectId = data.tableId;
        getBuildingInfoList();
    }

    function resetProjectInfo()
    {
        this.empj_BuildingInfoList = [];
        clearBuildingInfo();
        clearFigureProgressList();
        resetPredictedFigureProgress();
    }

    //获取楼幢列表
	function getBuildingInfoList()
	{
        var model = {
            interfaceVersion:addVue.interfaceVersion,
            theState: 0,
            projectId:addVue.projectId,
            escrowState:"已托管",
        };
        new ServerInterface(baseInfo.buildingListInterface).execute(model, function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                // console.log(jsonObj.buildingList);
                addVue.empj_BuildingInfoList = jsonObj.empj_BuildingInfoList;
            }
        });
    }

	function changeECodeFromConstruction(data)
	{
        // console.log(data);
        resetECodeFromConstruction();

        addVue.buildingId = data.tableId;
        addVue.eCodeFromConstruction = data.eCodeFromConstruction;
        addVue.eCodeFromPublicSecurity = data.eCodeFromPublicSecurity;
        addVue.position = data.position;
        addVue.upfloorNumber = data.upfloorNumber;
        addVue.currentBldLimitAmountVerAfDtlId = data.currentBldLimitAmountVerAfDtlId,
        addVue.currentFigureProgress = data.newCurrentFigureProgress;
        addVue.currentLimitedRatio = data.newCurrentLimitedRatio;

        addVue.figureProgressList = [];
        getProgressNodeList();
    }

    function resetECodeFromConstruction()
    {
        clearBuildingInfo();
        clearFigureProgressList();
        resetPredictedFigureProgress();
    }

    //清理楼幢信息，进度节点数组
    function clearBuildingInfo()
    {
        addVue.buildingId = "";
        addVue.eCodeFromConstruction = "";
        addVue.eCodeFromPublicSecurity = "";
        addVue.position = "";
        addVue.upfloorNumber = "";
        addVue.currentBldLimitAmountVerAfDtlId = "",
        addVue.currentFigureProgress = "";
        addVue.currentLimitedRatio = 100;

        addVue.patrolPerson = "";
        addVue.patrolTimestamp = "";
        $('#date03030201101').val("");
        addVue.remark = "";

        addVue.figureProgressList = [];
    }

    //清理进度详情列表
    function clearFigureProgressList()
    {
        addVue.empj_PjDevProgressForcastDtlList = [];
    }

    //获取当前楼幢受限进度节点列表
    function getProgressNodeList()
    {
        var model = {
            interfaceVersion:addVue.interfaceVersion,
            tableId: addVue.buildingId,
            nowLimitRatio: addVue.currentLimitedRatio,
        };
        new ServerInterface(baseInfo.progressNodeList).execute(model, function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                // console.log(jsonObj);
                addVue.figureProgressList = jsonObj.versionList;

                //新增时默认添加当前节点后所有预测进度节点，无预测日期
                for (var i = addVue.figureProgressList.length - 1; i >= 0; i --)
                {
                    var itemModel = addVue.figureProgressList[i];
                    var  model = {
                        interfaceVersion:addVue.interfaceVersion,
                        bldLimitAmountVerAfDtlId: itemModel.tableId,
                        stageName: itemModel.stageName,
                        limitedAmount: itemModel.limitedAmount,
                        predictedFinishDatetime:"",
                        ogPredictedFinishDatetime:"",
                        progressJudgement:0, //0正常
                        causeDescriptionForDelay:"无",
                        userName: addVue.userName,
                        operationDateTime: getCurrentDate(),
                    };
                    addVue.empj_PjDevProgressForcastDtlList.push(model);
                }

                var progressNodeList = [];
                //调整节点列表顺序（受限额度-->100%，80% ..0%
                for (var i = addVue.figureProgressList.length - 1; i >= 0; i --) {
                    progressNodeList.push(addVue.figureProgressList[i]);
                }
                addVue.figureProgressList = progressNodeList;
            }
        });
    }

    //详情更新操作--------------获取"更新机构详情"参数
    function getAddForm()
    {
        var dtlList = [];
        for (var i = 0; i < addVue.empj_PjDevProgressForcastDtlList.length; i++)
        {
            var itemDtl = addVue.empj_PjDevProgressForcastDtlList[i];
            var operationTimeStamp = Date.parse(new Date(itemDtl.operationDateTime));
            var beforeNodeIndex = -1;
            for (var j = 0; j < addVue.figureProgressList.length; j++){
                var itemNodeModel = addVue.figureProgressList[i=j];
                if (itemNodeModel.tableId == itemDtl.bldLimitAmountVerAfDtlId)
                {
                    beforeNodeIndex = j;
                    break;
                }
            }
            var  beforeBldLimitAmountVerAfDtlId = 0;
            if (beforeNodeIndex <= 0)
            {
                beforeBldLimitAmountVerAfDtlId = addVue.currentBldLimitAmountVerAfDtlId;
            }
            else
            {
                var itemNodeModel = addVue.figureProgressList[beforeNodeIndex - 1];
                beforeBldLimitAmountVerAfDtlId = itemNodeModel.tableId;
            }

            var itemModel = {
                userStartId: addVue.userId,
                buildingId: addVue.buildingId,
                beforeBldLimitAmountVerAfDtlId: parseInt(beforeBldLimitAmountVerAfDtlId),
                bldLimitAmountVerAfDtlId: itemDtl.bldLimitAmountVerAfDtlId,
                ogPredictedFinishDatetime: itemDtl.ogPredictedFinishDatetime,
                predictedFinishDatetime: itemDtl.predictedFinishDatetime,
                progressJudgement: itemDtl.progressJudgement,
                causeDescriptionForDelay: itemDtl.causeDescriptionForDelay,
                operationTimeStamp: operationTimeStamp,
            };
            dtlList.push(itemModel);
        }

        // for (var i = addVue.figureProgressList.length - 1; i >= 0; i --) {
        //     progressNodeList.push(addVue.figureProgressList[i]);
        // }

        var patrolTime = $('#date03030201101').val();
        return {
            interfaceVersion:addVue.interfaceVersion,
            busiState:addVue.busiState,
            developCompanyId:addVue.developCompanyId,
            projectId:addVue.projectId,
            buildingId:addVue.buildingId,
            eCodeFromConstruction:addVue.eCodeFromConstruction,
            eCodeFromPublicSecurity:addVue.eCodeFromPublicSecurity,
            // payoutType:this.payoutType,
            // currentFigureProgress:addVue.currentFigureProgress,
            currentBuildProgress:addVue.currentFigureProgress,
            patrolPerson:addVue.patrolPerson,
            patrolTimestamp:patrolTime,
            remark:addVue.remark,
            dtlFormList: dtlList,
            buttonType: addVue.buttonType,

            // idArr:["10041", "10040", "10019", "10016"],
        }
    }

    //新增工程进度预测信息
    function add(buttonType)
    {
        //保存时判断是否所有节点都有预测完成日期
        if (addVue.empj_PjDevProgressForcastDtlList.length < 1)
        {
            generalErrorModal(null, "预测进度节点列表不能为空");
            return;
        }
        for (var i = 0; i < addVue.empj_PjDevProgressForcastDtlList.length; i++)
        {
            var itemModel = addVue.empj_PjDevProgressForcastDtlList[i];
            if (itemModel.predictedFinishDatetime == null || itemModel.predictedFinishDatetime == "")
            {
                generalErrorModal(null, itemModel.stageName+"节点未添加预测完成日期");
                return;
            }
        }

        addVue.buttonType = buttonType;
        if (buttonType == 1)
        {
            addVue.busiState = "维护中";
        }
        else
        {
            addVue.busiState = "已维护";
        }
        console.log(addVue.getAddForm());
        new ServerInterfaceJsonBody(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj)
        {
            $('#date03030201101').val("");
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                addVue.tableId = jsonObj.tableId;
                generalSuccessModal();
                enterNewTabCloseCurrent(jsonObj.tableId,"工程进度预测详情","empj/Empj_PjDevProgressForcastDetail.shtml");
            }
        });
    }


    //选择工程进度明细
    function listItemSelectHandle(val)
    {
        addVue.selectItem = [];
        addVue.selectModel = {};
        for (var index = 0; index < val.length; index++) {
            var element = val[index].bldLimitAmountVerAfDtlId;
            addVue.selectItem.push(element)
            addVue.selectModel = val[index];
        }
    }

    //新增工程进度明细
    function addProgressForcastHandle()
    {
        addVue.alertType = 1;
        addVue.alertTitle = "新增进度预测";
    }

    //更新工程进度明细
    function updateProgressForcastHandle()
    {
        addVue.alertType = 2;
        addVue.alertTitle = "修改进度预测";
        if (addVue.selectItem.length == 1)
        {
            var theModel=addVue.selectModel;
            addVue.bldLimitAmountVerAfDtlId=theModel.bldLimitAmountVerAfDtlId+"";
            addVue.stageName=theModel.stageName;
            addVue.limitedAmount=theModel.limitedAmount;
            $('#date03030201102').val(theModel.predictedFinishDatetime);
            addVue.progressJudgement=theModel.progressJudgement;
            addVue.causeDescriptionForDelay=theModel.causeDescriptionForDelay;
        }
        else
        {
            generalErrorModal(null, "请选择要修改的进度信息");
        }
    }

    //选择预测进度节点
    function predictedFigureProgressChange(data)
    {
        resetPredictedFigureProgress();
        
        addVue.bldLimitAmountVerAfDtlId = data.tableId;
        addVue.stageName = data.stageName;
        addVue.limitedAmount = data.limitedAmount;
        if (addVue.limitedAmount == null || addVue.limitedAmount == "")
        {
            addVue.limitedAmount = "0";
        }
    }

    function resetPredictedFigureProgress()
    {
        addVue.bldLimitAmountVerAfDtlId = "";
        addVue.stageName = "";
        addVue.limitedAmount = 100.0;
    }

    //选择进度判定
    function getProgressJudgementHandle()
    {
        
    }

    //新增进度预测明细
    function addOrUpdatePjDevProgressForcastDtl()
    {
        if (addVue.bldLimitAmountVerAfDtlId == null || addVue.bldLimitAmountVerAfDtlId == 0)
        {
            generalErrorModal(null, "请选择预测进度节点");
            return;
        }
        //1、判断当前节点是否已存在，如果存在，则提示存在重复节点
        if (findSameProgressNode(addVue.bldLimitAmountVerAfDtlId) == true &&
            (addVue.alertType == 1 || (addVue.alertType == 2 && addVue.bldLimitAmountVerAfDtlId != addVue.selectModel.bldLimitAmountVerAfDtlId)))
        {
            generalErrorModal(null, "存在相同的预测进度节点");
            return;
        }
        var predictedFinishDatetime = $('#date03030201102').val();
        if (predictedFinishDatetime.length < 1)
        {
            generalErrorModal(null, "预测完成日期不能为空");
            return;
        }
        if (new  Date(predictedFinishDatetime).getTime() < new  Date(getCurrentDate()).getTime())
        {
            generalErrorModal(null, "预测完成日期不能小于当前时间");
            return;
        }
        if (addVue.progressJudgement == 1 && addVue.causeDescriptionForDelay.length < 1)
        {
            //滞后
            generalErrorModal(null, "进度滞后原因说明不能为空");
            return;
        }
        if (addVue.progressJudgement == 0 && addVue.causeDescriptionForDelay.length < 1)
        {
            addVue.causeDescriptionForDelay = "无"
        }
        
        var  model = {
            interfaceVersion:addVue.interfaceVersion,
            bldLimitAmountVerAfDtlId: addVue.bldLimitAmountVerAfDtlId,
            stageName: addVue.stageName,
            limitedAmount: addVue.limitedAmount,
            predictedFinishDatetime:predictedFinishDatetime,
            ogPredictedFinishDatetime:"",
            progressJudgement:addVue.progressJudgement,
            causeDescriptionForDelay:addVue.causeDescriptionForDelay,
            userName: addVue.userName,
            operationDateTime: getCurrentDate(),
        };
        // console.log(model);
        //2、判断当前节点前后是否有节点，如果有，在判断当前节点日期是否大于前面节点日期，并小于后面节点日期
        if (addVue.alertType == 1)
        {
            if (addorUpdateProgressNodeInfo(true, model) == true)
            {
                // console.log("添加成功--------");
                clearFigureProgressAlert();
                sortPjDevProgressForcastDtlList();
            }
        }
        else
        {
            //1、找到修改的节点
            //2、修改节点的原/新日期，得出偏差天数
            if (addorUpdateProgressNodeInfo(false, model) == true)
            {
                // console.log("修改成功--------");
                clearFigureProgressAlert();
                sortPjDevProgressForcastDtlList();
            }
        }
    }

    //添加或修改进度节点
    function addorUpdateProgressNodeInfo(isAdd, model)
    {
        var index = -1;
        var figureProgressListCount = addVue.figureProgressList.length;
        for (var i = 0; i < figureProgressListCount; i ++)
        {
            var itemDtl = addVue.figureProgressList[i];
            if (itemDtl.tableId == model.bldLimitAmountVerAfDtlId)
            {
                index = i;
                break;
            }
        }
        // console.log("查找进度节点位置");
        if (index < 0) return false;

        // console.log("如果当前无进度预测详情列表，直接添加");
        //如果当前无进度预测详情列表，直接添加
        if (addVue.empj_PjDevProgressForcastDtlList.length < 1)
        {
            addVue.empj_PjDevProgressForcastDtlList.push(model);
            return true;
        }

        //寻找相邻的前后进度节点
        var forwardNode = null;
        var backwordNode = null;
        if (index - 1 < figureProgressListCount)
        {
            forwardNode = addVue.figureProgressList[index - 1];
        }
        if (index + 1 < figureProgressListCount)
        {
            backwordNode = addVue.figureProgressList[index + 1];
        }
        ////寻找相邻的前后进度节点详情
        var forwardProgressForcast = null;
        var backwardProgressForcase = null;
        for (var i = 0; i < addVue.empj_PjDevProgressForcastDtlList.length; i ++)
        {
            var itemModel = addVue.empj_PjDevProgressForcastDtlList[i];
            if (forwardNode != null && forwardNode.tableId == itemModel.bldLimitAmountVerAfDtlId)
            {
                forwardProgressForcast = itemModel;
            }
            if (backwordNode != null && backwordNode.tableId == itemModel.bldLimitAmountVerAfDtlId)
            {
                backwardProgressForcase = itemModel;
            }
        }
        // console.log("寻找相邻的前后进度节点详情");

        var currentDateTime = new  Date(getCurrentDate()).getTime();
        var predictedFinishDatetime = new  Date(model.predictedFinishDatetime).getTime();

//        if (!(!isAdd && addVue.selectModel.predictedFinishDatetime == model.predictedFinishDatetime))
//        {
//            if (forwardProgressForcast != null && forwardProgressForcast.predictedFinishDatetime != null && forwardProgressForcast.predictedFinishDatetime != "")
//            {
//                if (predictedFinishDatetime <= new Date(forwardProgressForcast.predictedFinishDatetime).getTime())
//                {
//                    generalErrorModal(null, "预测完成日期不能小于前一进度节点日期");
//                    return false;
//                }
//            }
//            //xsz by time 2018-12-6 10:44:47
//            //===============注释原因============== #bug1484
////            if (backwardProgressForcase != null && backwardProgressForcase.predictedFinishDatetime != null && backwardProgressForcase.predictedFinishDatetime != "")
////            {
////                if (predictedFinishDatetime >= new Date(backwardProgressForcase.predictedFinishDatetime).getTime())
////                {
////                    generalErrorModal(null, "预测完成日期不能大于后一进度节点日期");
////                    return false;
////                }
////            }
//            //xsz by time 2018-12-6 10:44:47
//            //===============注释原因============== #bug1484
//        }
        // console.log("添加");
        //添加
        if (isAdd)
        {
            // console.log("添加");
            addVue.empj_PjDevProgressForcastDtlList.push(model);
        }
        //修改
        else
        {
            // console.log("修改");
            addVue.selectModel.bldLimitAmountVerAfDtlId = model.bldLimitAmountVerAfDtlId;
            addVue.selectModel.stageName = model.stageName;
            addVue.selectModel.limitedAmount = model.limitedAmount;
            addVue.selectModel.ogPredictedFinishDatetime = addVue.selectModel.predictedFinishDatetime; //原
            addVue.selectModel.predictedFinishDatetime = model.predictedFinishDatetime;                //新
            addVue.selectModel.progressJudgement = model.progressJudgement;
            addVue.selectModel.causeDescriptionForDelay = model.causeDescriptionForDelay;
            addVue.selectModel.userName = model.userName;
            addVue.selectModel.operationDateTime = model.operationDateTime;
            // console.log(addVue.selectModel);
        }
        return true;
    }

    //删除工程进度明细
    function deleteProgressForcastHandle()
    {
        if (addVue.selectItem.length == 0)
        {
            // noty({"text":"请选择要删除的进度信息","type":"error","timeout":2000});
            generalErrorModal(null, "请选择要删除的进度节点");
            return;
        }
        //注意如果需求有变更：可以存在两个相同的预测进度节点，那么此处的判断就需要使用tableId（因为暂存前台，需要自行生成唯一）
        else if (addVue.selectItem.length > 1)
        {
            generalSelectModal(function(){
                empj_ProjectInfoBatchDel();
            }, "确认删除所选进度节点吗？");
        }
        else
        {
            var tableId = addVue.selectItem[0];
            generalSelectModal(function(){
                empj_ProjectInfoOneDel(tableId);
            }, "确认删除所选进度节点吗？");
        }
    }

    //删除多个工程进度明细
    function empj_ProjectInfoBatchDel()
    {
        // console.log("删除多个工程进度明细");
        var newDtlList = [];
        var dtlList = addVue.empj_PjDevProgressForcastDtlList;
        for (var i = 0; i < dtlList.length; i++)
        {
            var itemModel = dtlList[i];
            var isHave = false;
            for (var index = 0; index < addVue.selectItem.length; index++)
            {
                var selectTableId = addVue.selectItem[index];
                if (selectTableId == itemModel.bldLimitAmountVerAfDtlId)
                {
                    isHave = true;
                    break;
                }
            }
            if (isHave == false) {
                newDtlList.push(itemModel);
            }
        }
        addVue.empj_PjDevProgressForcastDtlList = newDtlList;
        clearFigureProgressAlert();
        sortPjDevProgressForcastDtlList();
    }

    //删除单个工程进度明细
    function empj_ProjectInfoOneDel(tableId)
    {
        // console.log("删除单个工程进度明细");
        var dtlList = addVue.empj_PjDevProgressForcastDtlList;
        for (var i = 0; i < dtlList.length; i++)
        {
            var itemModel = dtlList[i];
            if (itemModel.bldLimitAmountVerAfDtlId == tableId)
            {
                dtlList.splice(i, 1);
                break;
            }
        }
        clearFigureProgressAlert();
        sortPjDevProgressForcastDtlList();
    }
    
    //清理进度详情弹框
    function clearFigureProgressAlert()
    {
        $(baseInfo.dtlAddAlert).modal('hide');

        resetPredictedFigureProgress();
        addVue.predictedFinishDatetime="";
        $('#date03030201102').val("");
        addVue.progressJudgement="0";
        addVue.causeDescriptionForDelay="";
    }

    //重新排序进度预测明细列表
    function sortPjDevProgressForcastDtlList()
    {
        //新增，更新，删除后，需要重新排序节点，根据节点的受限额度从大到小排序
        var compareProgressNode = function (obj1, obj2) {
            var val1 = parseFloat(obj1.limitedAmount);
            var val2 = parseFloat(obj2.limitedAmount);
            if (val1 < val2){
                return 1;
            } else if (val1 > val2){
                return -1;
            } else {
                return 0;
            }
        };

        addVue.empj_PjDevProgressForcastDtlList.sort(compareProgressNode);

        var listCount = addVue.empj_PjDevProgressForcastDtlList.length;
        addVue.countPerPage = 0;
        if (listCount > 0)
        {
            addVue.countPerPage = listCount;
        }
    }

    //查找数组中是否有对应的进度节点
    function findSameProgressNode(tableId)
    {
        var havaSame = false;
        for (var i = 0; i < addVue.empj_PjDevProgressForcastDtlList.length; i ++)
        {
            if (addVue.empj_PjDevProgressForcastDtlList[i].bldLimitAmountVerAfDtlId == tableId)
            {
                havaSame = true;
                break;
            }
        }
        return havaSame;
    }
    
    //获取当前时间，格式YYYY-MM-DD
    function getCurrentDate() {
        var date = new Date();
        var seperator1 = "-";
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        var hours = date.getHours();
        var minutes = date.getMinutes();
        var seconds = date.getSeconds();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        if (hours <= 9){
            hours = "0" + hours;
        }
        if (minutes <= 9){
            minutes = "0" + minutes;
        }
        if (seconds <= 9){
            seconds = "0" + seconds;
        }
        var currentdate = year + seperator1 + month + seperator1 + strDate + " " + hours + ":" + minutes + ":" + seconds;
        return currentdate;
    }

    //获取登录用户信息
    function getLoginUserInfo() {
        var model = {
            interfaceVersion: addVue.interfaceVersion,
        };
        new ServerInterface(baseInfo.getLoginSm_UserInterface).execute(model, function (jsonObj) {
            if (jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                console.log(jsonObj);
                addVue.userId = jsonObj.sm_User.tableId;
                addVue.userName = jsonObj.sm_User.theName;
            }
        });
    }
	function initData()
	{
        laydate.render({
            elem: '#date03030201101',
            range: false
        });
        laydate.render({
            elem: '#date03030201102',
            range: false
        });

        getCompanyList();
        getLoginUserInfo();
    }
	//------------------------方法定义-结束------------------//

	//------------------------数据初始化-开始----------------//
	addVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#empj_PjDevProgressForcastAddDiv",
    "dtlAddAlert":"#addProgressForcast",

    "getLoginSm_UserInterface":"../Sm_UserGet",
    "companyListInterface":"../Emmp_CompanyInfoForSelect",
    "projectListInterface":"../Empj_ProjectInfoForSelect",
    "buildingListInterface":"../Empj_BuildingInfoList",
    "progressNodeList":"../Empj_BldAccountGetLimitAmountVer", //

	"addInterface":"../Empj_PjDevProgressForcastAdd",
    // "addDtlInterface":"../Empj_PjDevProgressForcastDtlAdd",
    // "updateDtlInterface":"../Empj_PjDevProgressForcastDtlUpdate",
    // "listDtlInterface":"../Empj_PjDevProgressForcastDtlList_Temporary",
    // "listDtlInterface":"../Empj_PjDevProgressForcastDtlList",
    // "batchDeleteInterface":"../Empj_PjDevProgressForcastDtlBatchDelete",
    // "oneDeleteInterface":"../Empj_PjDevProgressForcastDtlDelete",

});
