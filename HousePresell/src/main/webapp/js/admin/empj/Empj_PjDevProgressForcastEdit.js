(function(baseInfo){
	var editVue = new Vue({
		el : baseInfo.editDivId,
		data : {
			interfaceVersion :19000101,
			tableId : 1,
            busiState: "未维护",
            userId: 0,
            userName: "",
			eCode: "",
            developCompanyId : "",
            developCompanyName: "",
            projectId: "",
            projectName: "",
            buildingId: "",
            eCodeOfBuilding: "",
            eCodeFromConstruction: "",
            eCodeFromPublicSecurity: "",
            position: "",
            upfloorNumber: "",
            deliveryType: "",  //1-毛坯房 2-成品房
            currentBldLimitAmountVerAfDtlId: "",
            currentFigureProgress: "",
            currentLimitedRatio: "",
            patrolPerson: "",
            patrolTimestamp: "",
            remark: "",

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
            dtlTableId: 1,  //明细表Id
            predictedFinishDatetime: "",
            progressJudgement: "0",
            causeDescriptionForDelay: "",
		},
		methods : {
			//详情
			initData: initData,
			//更新
			getUpdateForm : getUpdateForm,
			update: update,
			
            //进度明细相关
            addProgressForcastHandle: addProgressForcastHandle,
            updateProgressForcastHandle: updateProgressForcastHandle,
            deleteProgressForcastHandle: deleteProgressForcastHandle,
            listItemSelectHandle: listItemSelectHandle,
            indexMethod: function (index) {
                if (editVue.pageNumber > 1) {
                    return (editVue.pageNumber - 1) * editVue.countPerPage - 0 + (index - 0 + 1);
                }
                if (editVue.pageNumber <= 1) {
                    return index - 0 + 1;
                }
            },
            checkboxInit: checkboxInit,

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
		},
		watch:{
		}
	});

	//------------------------方法定义-开始------------------//

    //列表勾选框-------设置可操作勾选框
    function checkboxInit(row, index)
    {
        var canSelect = 1;
        var itemDtl = editVue.empj_PjDevProgressForcastDtlList[index];
        if (parseFloat(itemDtl.limitedAmount+"") >= parseFloat(editVue.currentLimitedRatio+""))
        {
            canSelect = 0;
        }
        return canSelect;
    }

	//详情操作--------------获取工程工程进度预测详情
	function getDetail()
	{
        if (editVue.tableId == null || editVue.tableId < 1)
        {
            return;
        }

        var model = {
      	 	interfaceVersion:editVue.interfaceVersion,
			tableId:editVue.tableId,
        }
		new ServerInterface(baseInfo.detailInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
                generalErrorModal(jsonObj);
			}
			else
			{
				// console.log(jsonObj);
                var empj_PjDevProgressForcast = jsonObj.empj_PjDevProgressForcast;
                editVue.tableId = empj_PjDevProgressForcast.tableId;
                editVue.busiState = empj_PjDevProgressForcast.busiState;
                editVue.eCode = empj_PjDevProgressForcast.eCode;
                editVue.eCodeFromConstruction = empj_PjDevProgressForcast.eCodeFromConstruction;
                editVue.eCodeFromPublicSecurity = empj_PjDevProgressForcast.eCodeFromPublicSecurity;
                editVue.developCompanyId = empj_PjDevProgressForcast.developCompanyId;
                editVue.developCompanyName = empj_PjDevProgressForcast.developCompanyName;
                editVue.projectId = empj_PjDevProgressForcast.projectId;
                editVue.projectName = empj_PjDevProgressForcast.projectName;
                editVue.buildingId = empj_PjDevProgressForcast.buildingId;
                editVue.eCodeOfBuilding = empj_PjDevProgressForcast.eCodeOfBuilding;
                editVue.position = empj_PjDevProgressForcast.position;
                editVue.upfloorNumber = empj_PjDevProgressForcast.upfloorNumber;
                editVue.deliveryType = empj_PjDevProgressForcast.deliveryType;
                editVue.currentBldLimitAmountVerAfDtlId = empj_PjDevProgressForcast.currentBldLimitAmountVerAfDtlId,
                editVue.currentFigureProgress = empj_PjDevProgressForcast.newCurrentFigureProgress;
                editVue.currentLimitedRatio = empj_PjDevProgressForcast.newCurrentLimitedRatio;
                editVue.patrolPerson = empj_PjDevProgressForcast.patrolPerson;
                $('#date03030201201').val(empj_PjDevProgressForcast.patrolTimestamp);
                editVue.remark = empj_PjDevProgressForcast.remark;

                editVue.empj_PjDevProgressForcastDtlList = jsonObj.empj_pjDevProgressForcastDtlList;
                var listCount = editVue.empj_PjDevProgressForcastDtlList.length;
                editVue.countPerPage = 0;
                if (listCount > 0)
                {
                    editVue.countPerPage = listCount;
                }

                getProgressNodeList();
			}
		});
	}

    //获取当前楼幢受限进度节点列表
    function getProgressNodeList()
    {
        var model = {
            interfaceVersion:editVue.interfaceVersion,
            tableId: editVue.buildingId,
            nowLimitRatio: editVue.currentLimitedRatio,
        };
        // console.log(model);
        new ServerInterface(baseInfo.progressNodeList).execute(model, function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                // console.log(jsonObj);
                editVue.figureProgressList = jsonObj.versionList;

                var progressNodeList = [];
                //调整节点列表顺序（受限额度-->100%，80% ..0%
                for (var i = editVue.figureProgressList.length - 1; i >= 0; i --) {
                    progressNodeList.push(editVue.figureProgressList[i]);
                }
                editVue.figureProgressList = progressNodeList;
            }
        });
    }
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getUpdateForm()
	{
        var dtlList = [];
        for (var i = 0; i < editVue.empj_PjDevProgressForcastDtlList.length; i++)
        {
            var itemDtl = editVue.empj_PjDevProgressForcastDtlList[i];
            var operationTimeStamp = Date.parse(new Date(itemDtl.operationDateTime));
            var beforeNodeIndex = -1;
            for (var j = 0; j < editVue.figureProgressList.length; j++){
                var itemNodeModel = editVue.figureProgressList[i=j];
                if (itemNodeModel.tableId == itemDtl.bldLimitAmountVerAfDtlId)
                {
                    beforeNodeIndex = j;
                    break;
                }
            }
            var  beforeBldLimitAmountVerAfDtlId = 0;
            if (beforeNodeIndex <= 0)
            {
                beforeBldLimitAmountVerAfDtlId = editVue.currentBldLimitAmountVerAfDtlId;
            }
            else
            {
                var itemNodeModel = editVue.figureProgressList[beforeNodeIndex - 1];
                beforeBldLimitAmountVerAfDtlId = itemNodeModel.tableId;
            }
            
            var itemModel = {
                tableId: itemDtl.tableId,
                userUpdateId:editVue.userId,
                userStartId:editVue.userId,
                buildingId: editVue.buildingId,
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
        
        var patrolTime = $('#date03030201201').val()
		return {
			interfaceVersion:this.interfaceVersion,
            tableId:editVue.tableId,
            busiState:editVue.busiState,
            userUpdateId:editVue.userId,
			developCompanyId:editVue.developCompanyId,
			projectId:editVue.projectId,
			buildingId:editVue.buildingId,
			eCodeOfBuilding:editVue.eCodeOfBuilding,
			eCodeFromConstruction:editVue.eCodeFromConstruction,
			eCodeFromPublicSecurity:editVue.eCodeFromPublicSecurity,
			// payoutType:this.payoutType,
			// currentFigureProgress:editVue.currentFigureProgress,
            currentBuildProgress:editVue.currentFigureProgress,
			patrolPerson:editVue.patrolPerson,
			patrolTimestamp:patrolTime,
			// patrolInstruction:this.patrolInstruction,
			remark:editVue.remark,
            dtlFormList: dtlList,
            buttonType: editVue.buttonType,
		}
	}
	
    //更新工程进度预测信息
	function update(buttonType)
	{
	    //楼幢的受限额度节点会变化，此处不需要判断
        // if (editVue.empj_PjDevProgressForcastDtlList.length < 1)
        // {
        //     generalErrorModal(null, "预测进度节点列表不能为空");
        //     return;
        // }
        editVue.buttonType = buttonType;
        if (buttonType == 1)
        {
            editVue.busiState = "维护中";
        }
        else
        {
            editVue.busiState = "已维护";
        }
	    console.log(editVue.getUpdateForm());
        new ServerInterfaceJsonBody(baseInfo.updateInterface).execute(editVue.getUpdateForm(), function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                generalSuccessModal();
                enterNewTabCloseCurrent(editVue.tableId,"工程进度预测详情","empj/Empj_PjDevProgressForcastDetail.shtml");
            }
        });
	}

    //选择工程进度明细
    function listItemSelectHandle(val)
    {
        editVue.selectItem = [];
        editVue.selectModel = {};
        for (var index = 0; index < val.length; index++) {
            var element = val[index].bldLimitAmountVerAfDtlId;
            editVue.selectItem.push(element)
            editVue.selectModel = val[index];
        }
        // console.log(editVue.selectItem);
    }

    //新增工程进度明细
    function addProgressForcastHandle()
    {
        editVue.alertType = 1;
        editVue.alertTitle = "新增进度预测";
    }

    //更新工程进度明细
    function updateProgressForcastHandle()
    {
        editVue.alertType = 2;
        editVue.alertTitle = "修改进度预测";
        if (editVue.selectItem.length == 1)
        {
            var theModel=editVue.selectModel;
            // console.log(theModel);
            editVue.bldLimitAmountVerAfDtlId=theModel.bldLimitAmountVerAfDtlId+"";
            editVue.stageName=theModel.stageName;
            editVue.limitedAmount=theModel.limitedAmount;
            $('#date03030201202').val(theModel.predictedFinishDatetime);
            editVue.progressJudgement=theModel.progressJudgement;
            editVue.causeDescriptionForDelay=theModel.causeDescriptionForDelay;
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

        editVue.bldLimitAmountVerAfDtlId = data.tableId;
        editVue.stageName = data.stageName;
        editVue.limitedAmount = data.limitedAmount;
        if (editVue.limitedAmount == null || editVue.limitedAmount == "")
        {
            editVue.limitedAmount = "0";
        }
    }

    function resetPredictedFigureProgress()
    {
        editVue.bldLimitAmountVerAfDtlId = "";
        editVue.stageName = "";
        editVue.limitedAmount = 100.0;
    }

    //选择进度判定
    function getProgressJudgementHandle()
    {

    }

    //新增进度预测明细
    function addOrUpdatePjDevProgressForcastDtl()
    {
        if (editVue.bldLimitAmountVerAfDtlId == null || editVue.bldLimitAmountVerAfDtlId == 0)
        {
            generalErrorModal(null, "请选择预测进度节点");
            return;
        }

        //1、判断当前节点是否已存在，如果存在，则提示存在重复节点
        if (findSameProgressNode(editVue.bldLimitAmountVerAfDtlId) == true &&
            (editVue.alertType == 1 || (editVue.alertType == 2 && editVue.bldLimitAmountVerAfDtlId != editVue.selectModel.bldLimitAmountVerAfDtlId)))
        {
            generalErrorModal(null, "存在相同的预测进度节点");
            return;
        }
        var predictedFinishDatetime = $('#date03030201202').val();
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
        if (editVue.progressJudgement == 1 && editVue.causeDescriptionForDelay.length < 1)
        {
            //滞后
            generalErrorModal(null, "进度滞后原因说明不能为空");
            return;
        }
        if (editVue.progressJudgement == 0 && editVue.causeDescriptionForDelay.length < 1)
        {
            editVue.causeDescriptionForDelay = "无"
        }

        var  model = {
            interfaceVersion:editVue.interfaceVersion,
            tableId: null,
            bldLimitAmountVerAfDtlId: editVue.bldLimitAmountVerAfDtlId,
            stageName: editVue.stageName,
            limitedAmount: editVue.limitedAmount,
            predictedFinishDatetime:predictedFinishDatetime,
            ogPredictedFinishDatetime:"",
            progressJudgement:editVue.progressJudgement,
            causeDescriptionForDelay:editVue.causeDescriptionForDelay,
            userName: editVue.userName,
            operationDateTime: getCurrentDate(),
        };

        if (editVue.alertType == 1)
        {
            // console.log("开始新增--------");
            if (addorUpdateProgressNodeInfo(true, model) == true)
            {
                clearFigureProgressAlert();
                sortPjDevProgressForcastDtlList();
            }
        }
        else
        {
            //1、找到修改的节点
            //2、修改节点的原/新日期，得出偏差天数
            //3、查看当前节点后面是否有节点，如果有，根据偏差天数顺延其后所有节点的原/新日期(编辑页面实现，且修改第一个节点，最后一个不动)
            // console.log("开始修改--------");
            if (addorUpdateProgressNodeInfo(false, model) == true)
            {
                clearFigureProgressAlert();
                sortPjDevProgressForcastDtlList();
            }
        }
    }

    //添加或修改进度节点
    function addorUpdateProgressNodeInfo(isAdd, model)
    {
        var index = -1;
        var figureProgressListCount = editVue.figureProgressList.length;
        for (var i = 0; i < figureProgressListCount; i ++)
        {
            var itemDtl = editVue.figureProgressList[i];
            if (itemDtl.tableId == model.bldLimitAmountVerAfDtlId)
            {
                index = i;
                break;
            }
        }
        // console.log("查找进度节点位置",+index+","+figureProgressListCount);
        if (index < 0) return false;

        //如果当前无进度预测详情列表，直接添加
        if (editVue.empj_PjDevProgressForcastDtlList.length < 1)
        {
            // console.log("如果当前无进度预测详情列表，直接添加");
            editVue.empj_PjDevProgressForcastDtlList.push(model);
            return true;
        }

        //寻找相邻的前后进度节点
        var forwardNode = null;
        var backwordNode = null;
        if (index - 1 < figureProgressListCount)
        {
            forwardNode = editVue.figureProgressList[index - 1];
        }
        if (index + 1 < figureProgressListCount)
        {
            backwordNode = editVue.figureProgressList[index + 1];
        }
        //寻找相邻的前后进度节点详情
        // console.log("寻找相邻的前后进度节点详情");
        var forwardProgressForcast = null;
        var backwardProgressForcase = null;
        for (var i = 0; i < editVue.empj_PjDevProgressForcastDtlList.length; i ++)
        {
            var itemModel = editVue.empj_PjDevProgressForcastDtlList[i];
            if (forwardNode != null && forwardNode.tableId == itemModel.bldLimitAmountVerAfDtlId)
            {
                forwardProgressForcast = itemModel;
            }
            if (backwordNode != null && backwordNode.tableId == itemModel.bldLimitAmountVerAfDtlId)
            {
                backwardProgressForcase = itemModel;
            }
        }

        //判断当前修改的节点是否是第一个节点，如果是，其后所有节点顺延偏差日期，但最后一个节点只能手动维护
        if (index == 0 && !isAdd)
        {
            //如果只有一个节点且是第一个节点且是修改操作，直接修改即可
            if (editVue.empj_PjDevProgressForcastDtlList.length == 1)
            {
                // console.log("只有第一个节点，直接修改");
                updateProgressForcastInfo(model);
                return true;
            }
            //拿到最后一个节点及其预测完成日期
            var lastNode = editVue.figureProgressList[figureProgressListCount - 1];
            var lastProgressForcase = null;
            for (var i = 0; i < editVue.empj_PjDevProgressForcastDtlList.length; i ++)
            {
                var itemModel = editVue.empj_PjDevProgressForcastDtlList[i];
                if (lastNode.tableId == itemModel.bldLimitAmountVerAfDtlId)
                {
                    lastProgressForcase = itemModel;
                    break;
                }
            }
            var lastProgressFinishDateTime = null;
            if (lastProgressForcase != null)
            {
                lastProgressFinishDateTime = lastProgressForcase.predictedFinishDatetime;
            }
            // console.log("拿到最后一个节点及其预测完成日期");
            //新增或更新第一个节点,判断是否小于最后一个节点日期即可
            if (lastProgressFinishDateTime != null && lastProgressFinishDateTime != "")
            {
                if (new Date(model.predictedFinishDatetime).getTime() >= new Date(lastProgressFinishDateTime).getTime())
                {
                    generalErrorModal(null, "预测完成日期不能大于最后一个进度节点日期");
                    return false;
                }
            }

            var noLastProgressNode = false;
            if (lastProgressForcase == null)
            {
                noLastProgressNode = true;
            }
            generalSelectModal(function(){
                changeBackWordProgressNodeDate(model, noLastProgressNode, lastNode, lastProgressFinishDateTime);
            }, "是否自动维护预测进度节点？", function(){
                checkCanChangeFirstProgressNode(model);
            });

            return true;
        }

        // console.log("修改正常节点");
        var predictedFinishDatetime = new  Date(model.predictedFinishDatetime).getTime();
        if (!(!isAdd && editVue.selectModel.predictedFinishDatetime == model.predictedFinishDatetime))
        {
            if (forwardProgressForcast != null && forwardProgressForcast.predictedFinishDatetime != null && forwardProgressForcast.predictedFinishDatetime != "")
            {
                if (predictedFinishDatetime <= new Date(forwardProgressForcast.predictedFinishDatetime).getTime())
                {
                    generalErrorModal(null, "预测完成日期不能小于前一进度节点日期");
                    return false;
                }
            }
            //xsz by time 2018-12-6 10:44:47
            //===============注释原因============== #bug1484
//            if (backwardProgressForcase != null && backwardProgressForcase.predictedFinishDatetime != null && backwardProgressForcase.predictedFinishDatetime != "")
//            {
//                if (predictedFinishDatetime >= new Date(backwardProgressForcase.predictedFinishDatetime).getTime())
//                {
//                    generalErrorModal(null, "预测完成日期不能大于后一进度节点日期");
//                    return false;
//                }
//            }
            //xsz by time 2018-12-6 10:44:47
            //===============注释原因============== #bug1484
        }
        //添加
        if (isAdd)
        {
            // console.log("添加");
            editVue.empj_PjDevProgressForcastDtlList.push(model);
        }
        //修改
        else
        {
            // console.log("修改");
            updateProgressForcastInfo(model);
        }
        return true;
    }

    //根据顺延偏差日期调整其后节点时间
    function changeBackWordProgressNodeDate(model, noLastProgressNode, lastNode, lastProgressFinishDateTime)
    {
        // console.log("根据顺延偏差日期调整其后节点时间");
        var canChangeIndex = editVue.empj_PjDevProgressForcastDtlList.length - 1; //无最后一个节点,直接顺延节点时间
        //进度详情列表中有最后一个节点
        if (!noLastProgressNode)
        {
            canChangeIndex = findCanChangeNodeTimeIndex(model, lastNode, lastProgressFinishDateTime, editVue.empj_PjDevProgressForcastDtlList.length);
            if (canChangeIndex < 0)
            {
                //无可修改节点
                generalErrorModal(null, "预测完成日期不能大于后一进度节点日期");
                return false;
            }
            if (canChangeIndex == 0)
            {
                //与第二个节点时间比较
                var secondProgressNode = editVue.empj_PjDevProgressForcastDtlList[1];
                if (new Date(model.predictedFinishDatetime).getTime() >= new Date(secondProgressNode.predictedFinishDatetime).getTime())
                {
                    generalErrorModal(null, "所选日期不能自动维护预测进度节点");
                    return false;
                }
            }
        }

        var deviationTimeStamp = new Date(model.predictedFinishDatetime).getTime() - new Date(editVue.selectModel.predictedFinishDatetime).getTime();
        // console.log("model"+model.predictedFinishDatetime+", selectModel"+editVue.selectModel.predictedFinishDatetime+","+deviationTimeStamp);
        updateProgressForcastInfo(model);
        if (canChangeIndex == 0) return;

        //跳过第一个进度节点
        for (var i = 1; i < editVue.empj_PjDevProgressForcastDtlList.length; i ++)
        {
            //判断是否是最后一个进度节点，判断是否在可调整范围内
            var itemModel = editVue.empj_PjDevProgressForcastDtlList[i];
            // console.log(lastNode.tableId+","+itemModel.bldLimitAmountVerAfDtlId);
            if ( lastNode.tableId != itemModel.bldLimitAmountVerAfDtlId && i <= canChangeIndex)
            {
                var itemPredictedFinishDatetimeStamp = new Date(itemModel.predictedFinishDatetime).getTime();
                itemPredictedFinishDatetimeStamp += deviationTimeStamp;

                var itemPredictedFinishDatetime = formatDateTime(itemPredictedFinishDatetimeStamp);
                // console.log("调整节点日期"+itemModel.predictedFinishDatetime+",后"+itemPredictedFinishDatetime);
                itemModel.ogPredictedFinishDatetime = itemModel.predictedFinishDatetime; //原
                itemModel.predictedFinishDatetime = itemPredictedFinishDatetime;     //新
                //判断日期顺延调整后，有没有超出最后一个节点日期
                // if (itemPredictedFinishDatetimeStamp < new Date(lastProgressFinishDateTime).getTime())
            }
        }
        //判断第一个节点和第二个节点的时间来还原第一个节点时间

        // console.log("调整OK");
    }

    //不根据顺延偏差日期调整其后节点时间
    function checkCanChangeFirstProgressNode(model)
    {
        // console.log("不根据顺延偏差日期调整其后节点时间");
        if (editVue.empj_PjDevProgressForcastDtlList.length == 1)
        {
            // console.log("修改");
            updateProgressForcastInfo(model);
            return true;
        }
        //与第二个节点时间比较
        var secondProgressNode = editVue.empj_PjDevProgressForcastDtlList[1];
        if (new Date(model.predictedFinishDatetime).getTime() >= new Date(secondProgressNode.predictedFinishDatetime).getTime())
        {
            generalErrorModal(null, "预测完成日期不能大于后一进度节点日期");
            return false;
        }
        else
        {
            // console.log("修改");
            updateProgressForcastInfo(model);
            return true;
        }
    }

    //检查当前可顺延调整节点时间的所在位置
    function findCanChangeNodeTimeIndex(model, lastNode, lastProgressFinishDateTime, indexPostion)
    {
        var deviationTimeStamp = new Date(model.predictedFinishDatetime).getTime() - new Date(editVue.selectModel.predictedFinishDatetime).getTime();
        //创建临时顺延后节点数组
        var nodeList = [];
        for (var i = 0; i < editVue.empj_PjDevProgressForcastDtlList.length; i ++)
        {
            var itemModel = editVue.empj_PjDevProgressForcastDtlList[i];
            //第一个进度节点直接更新
            if (i == 0)
            {
                var newModel = {
                    bldLimitAmountVerAfDtlId: model.bldLimitAmountVerAfDtlId,
                    predictedFinishDatetime: model.predictedFinishDatetime,
                };
                nodeList.push(newModel);
            }
            //判断是否是最后一个进度节点
            else if ( lastNode.tableId != itemModel.bldLimitAmountVerAfDtlId)
            {
                if (i > indexPostion)
                {
                    // console.log(indexPostion);
                    //index到其后节点，都已排除，不可顺延调整时间
                    var newModel = {
                        bldLimitAmountVerAfDtlId: itemModel.bldLimitAmountVerAfDtlId,
                        predictedFinishDatetime: itemModel.predictedFinishDatetime,
                    };
                    nodeList.push(newModel);
                }
                else
                {
                    var itemPredictedFinishDatetimeStamp = new Date(itemModel.predictedFinishDatetime).getTime();
                    itemPredictedFinishDatetimeStamp += deviationTimeStamp;
                    //判断日期顺延调整后，有没有超出最后一个节点日期
                    if (itemPredictedFinishDatetimeStamp < new Date(lastProgressFinishDateTime).getTime())
                    {
                        var newModel = {
                            bldLimitAmountVerAfDtlId: itemModel.bldLimitAmountVerAfDtlId,
                            predictedFinishDatetime: formatDateTime(itemPredictedFinishDatetimeStamp),
                        };
                        nodeList.push(newModel);
                    }
                    else
                    {
                        var newModel = {
                            bldLimitAmountVerAfDtlId: itemModel.bldLimitAmountVerAfDtlId,
                            predictedFinishDatetime: itemModel.predictedFinishDatetime,
                        };
                        nodeList.push(newModel);
                    }
                }
            }
        }
        // console.log("nodeList长度="+nodeList.length);
        // console.log(nodeList);
        //1、判断可调整节点时间位置(过滤前面节点日期大于后面情况)
        var index = -1;
        var isOutofDateTime = false;
        for (var i = 0; i < nodeList.length - 1; i++)
        {
            var firstFinishDatetime = nodeList[i].predictedFinishDatetime;
            var secondFinishDatetime = nodeList[i + 1].predictedFinishDatetime;
            // console.log(firstFinishDatetime+", "+secondFinishDatetime);
            if (new Date(firstFinishDatetime) >= new Date(secondFinishDatetime))
            {
                isOutofDateTime = true;
                index = i - 1;
                break;
            }
        }
        //都可调整时间
        if (!isOutofDateTime)
        {
            index = nodeList.length - 1;
        }
        if (index <= 0)
        {
            // console.log("判断可修改节点位置--="+index);
            return index;
        }
        // console.log("1、可修改节点位置="+index);

        //2、判断可调整节点时间位置（过滤节点日期超出最后节点日期情况）
        nodeList = [];
        for (var i = 0; i <= index; i++)
        {
            var itemModel = editVue.empj_PjDevProgressForcastDtlList[i];
            //第一个进度节点直接更新
            if (i == 0)
            {
                var newModel = {
                    bldLimitAmountVerAfDtlId: model.bldLimitAmountVerAfDtlId,
                    predictedFinishDatetime: model.predictedFinishDatetime,
                };
                nodeList.push(newModel);
            }
            //判断是否是最后一个进度节点
            else if ( lastNode.tableId != itemModel.bldLimitAmountVerAfDtlId)
            {
                var itemPredictedFinishDatetimeStamp = new Date(itemModel.predictedFinishDatetime).getTime();
                itemPredictedFinishDatetimeStamp += deviationTimeStamp;
                var newModel = {
                    bldLimitAmountVerAfDtlId: itemModel.bldLimitAmountVerAfDtlId,
                    predictedFinishDatetime: formatDateTime(itemPredictedFinishDatetimeStamp),
                };
                nodeList.push(newModel);
            }
        }
        // console.log(nodeList);
        for (var i = 0; i < nodeList.length; i++)
        {
            //判断当前节点有没有超出最后一个节点日期
            if (new Date(nodeList[i].predictedFinishDatetime).getTime() >= new Date(lastProgressFinishDateTime).getTime())
            {
                // console.log("过滤节点日期超出最后节点日期情况"+nodeList[i].predictedFinishDatetime);
                index = i - 1;
                break;
            }
        }
        // console.log("2、可修改节点位置="+index);

        //3、判断将要调整的节点时间是否满足要求
        // console.log("判断将要调整的节点时间是否满足要求");
        nodeList = [];
        for (var i = 0; i < editVue.empj_PjDevProgressForcastDtlList.length; i++)
        {
            var itemModel = editVue.empj_PjDevProgressForcastDtlList[i];
            //第一个进度节点直接更新
            if (i == 0)
            {
                var newModel = {
                    bldLimitAmountVerAfDtlId: model.bldLimitAmountVerAfDtlId,
                    predictedFinishDatetime: model.predictedFinishDatetime,
                };
                nodeList.push(newModel);
            }
            else if (i <= index)
            {
                var itemPredictedFinishDatetimeStamp = new Date(itemModel.predictedFinishDatetime).getTime();
                itemPredictedFinishDatetimeStamp += deviationTimeStamp;
                var newModel = {
                    bldLimitAmountVerAfDtlId: itemModel.bldLimitAmountVerAfDtlId,
                    predictedFinishDatetime: formatDateTime(itemPredictedFinishDatetimeStamp),
                };
                nodeList.push(newModel);
            }
            else
            {
                var newModel = {
                    bldLimitAmountVerAfDtlId: itemModel.bldLimitAmountVerAfDtlId,
                    predictedFinishDatetime: itemModel.predictedFinishDatetime,
                };
                nodeList.push(newModel);
            }
        }
        var isOutofDateTime = false;
        for (var i = 0; i < nodeList.length - 1; i++)
        {
            var firstFinishDatetime = nodeList[i].predictedFinishDatetime;
            var secondFinishDatetime = nodeList[i + 1].predictedFinishDatetime;
            // console.log(firstFinishDatetime+", "+secondFinishDatetime);
            if (new Date(firstFinishDatetime) >= new Date(secondFinishDatetime))
            {
                isOutofDateTime = true;
                break;
            }
        }
        if (isOutofDateTime && index > 0)
        {
            var resultIndex = findCanChangeNodeTimeIndex(model, lastNode, lastProgressFinishDateTime, index - 1);
            // console.log("跳出循环位置="+resultIndex);
            return resultIndex;
        }

        // console.log("判断可修改节点位置="+index);
        return index;
    }

    //更新选中进度节点
    function updateProgressForcastInfo(model)
    {
        editVue.selectModel.bldLimitAmountVerAfDtlId = model.bldLimitAmountVerAfDtlId;
        editVue.selectModel.stageName = model.stageName;
        editVue.selectModel.limitedAmount = model.limitedAmount;
        editVue.selectModel.ogPredictedFinishDatetime = editVue.selectModel.predictedFinishDatetime; //原
        editVue.selectModel.predictedFinishDatetime = model.predictedFinishDatetime;                //新
        editVue.selectModel.progressJudgement = model.progressJudgement;
        editVue.selectModel.causeDescriptionForDelay = model.causeDescriptionForDelay;
        editVue.selectModel.userName = model.userName;
        editVue.selectModel.operationDateTime = model.operationDateTime;
    }
    
    //删除工程进度明细
    function deleteProgressForcastHandle()
    {
        if (editVue.selectItem.length == 0)
        {
            // noty({"text":"请选择要删除的进度信息","type":"error","timeout":2000});
            generalErrorModal(null, "请选择要删除的进度节点");
            return;
        }
        //注意如果需求有变更：可以存在两个相同的预测进度节点，那么此处的判断就需要使用tableId（因为暂存前台，需要自行生成唯一）
        else if (editVue.selectItem.length > 1)
        {
            generalSelectModal(function(){
                empj_ProjectInfoBatchDel();
            }, "确认删除所选进度节点吗？");
        }
        else
        {
            var tableId = editVue.selectItem[0];
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
        var dtlList = editVue.empj_PjDevProgressForcastDtlList;
        for (var i = 0; i < dtlList.length; i++)
        {
            var itemModel = dtlList[i];
            var isHave = false;
            for (var index = 0; index < editVue.selectItem.length; index++)
            {
                var selectTableId = editVue.selectItem[index];
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
        editVue.empj_PjDevProgressForcastDtlList = newDtlList;
        clearFigureProgressAlert();
        sortPjDevProgressForcastDtlList();
    }

    //删除单个工程进度明细
    function empj_ProjectInfoOneDel(tableId)
    {
        // console.log("删除单个工程进度明细");
        var dtlList = editVue.empj_PjDevProgressForcastDtlList;
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
        $(baseInfo.dtlEditAlert).modal('hide');

        resetPredictedFigureProgress();
        editVue.predictedFinishDatetime="";
        $('#date03030201202').val("");
        editVue.progressJudgement="0";
        editVue.causeDescriptionForDelay="";
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

        editVue.empj_PjDevProgressForcastDtlList.sort(compareProgressNode);

        var listCount = editVue.empj_PjDevProgressForcastDtlList.length;
        editVue.countPerPage = 0;
        if (listCount > 0)
        {
            editVue.countPerPage = listCount;
        }
    }

    //查找数组中是否有对应的进度节点
    function findSameProgressNode(tableId)
    {
        var havaSame = false;
        for (var i = 0; i < editVue.empj_PjDevProgressForcastDtlList.length; i ++)
        {
            if (editVue.empj_PjDevProgressForcastDtlList[i].bldLimitAmountVerAfDtlId == tableId)
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
        // var currentdate = year + seperator1 + month + seperator1 + strDate;

        console.log("时分秒");
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

        console.log("----时分秒");
        return currentdate;
    }

    //时间戳转时间，格式YYYY-MM-DD
    function formatDateTime(timeStamp) {
        var date = new Date();
        date.setTime(timeStamp);
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        m = m < 10 ? ('0' + m) : m;
        var d = date.getDate();
        d = d < 10 ? ('0' + d) : d;
        var h = date.getHours();
        h = h < 10 ? ('0' + h) : h;
        var minute = date.getMinutes();
        var second = date.getSeconds();
        minute = minute < 10 ? ('0' + minute) : minute;
        second = second < 10 ? ('0' + second) : second;
        //y + '-' + m + '-' + d+' '+h+':'+minute+':'+second
        return y + '-' + m + '-' + d;
    };

    //获取登录用户信息
    function getLoginUserInfo() {
        var model = {
            interfaceVersion: editVue.interfaceVersion,
        };
        new ServerInterface(baseInfo.getLoginSm_UserInterface).execute(model, function (jsonObj) {
            if (jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                console.log(jsonObj);
                editVue.userId = jsonObj.sm_User.tableId;
                editVue.userName = jsonObj.sm_User.theName;
            }
        });
    }
    
    function initData()
	{
        laydate.render({
            elem: '#date03030201201',
            range: false
        });
        laydate.render({
            elem: '#date03030201202',
            range: false
        });

        getIdFormTab("", function (id){
            editVue.tableId = id;
            getDetail();
        });
        getLoginUserInfo();
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	editVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"editDivId":"#empj_PjDevProgressForcastEditDiv",
    "dtlEditAlert":"#editProgressForcast",
    
    "getLoginSm_UserInterface":"../Sm_UserGet",
    "progressNodeList":"../Empj_BldAccountGetLimitAmountVer", //
	"detailInterface":"../Empj_PjDevProgressForcastDetail",
    "updateInterface":"../Empj_PjDevProgressForcastUpdate",
});
