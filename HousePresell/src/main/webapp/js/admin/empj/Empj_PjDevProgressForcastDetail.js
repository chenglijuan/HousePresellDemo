(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tableId : 1,
            busiState : "",
            eCode: "",
            developCompanyName: "",
            projectName: "",
            eCodeOfBuilding: "",
            eCodeFromConstruction: "",
            eCodeFromPublicSecurity: "",
            position: "",
            upfloorNumber: "",
            currentBuildProgress: "",
            patrolPerson: "",
            patrolTimestamp: "",
            remark: "",

            //进度详情列表相关
            pageNumber : 1,
            countPerPage : 20,
            theState: 0,//正常为0，删除为1
            empj_PjDevProgressForcastDtlList: [],

		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
            empj_PjDevProgressForcastEdit: empj_PjDevProgressForcastEdit,
            indexMethod: function (index) {
                return index - 0 + 1;
            },
		},
		computed:{
			 
		},
		components : {

		},
		watch:{

		}
	});

	//------------------------方法定义-开始------------------//
	//按钮操作--------------跳转到工程进度预测编辑页面
	function empj_PjDevProgressForcastEdit()
	{
        // var tableId = 'PjDevProgressForcastEdit_' + detailVue.tableId;
        enterNewTabCloseCurrent(detailVue.tableId,"编辑工程进度预测","empj/Empj_PjDevProgressForcastEdit.shtml");
    }

    //详情操作--------------获取工程工程进度预测详情
    function refresh()
    {
        if (detailVue.tableId == null || detailVue.tableId < 1)
        {
            return;
        }

        getDetail();
    }

    function getDetail()
    {
        var model = {
            interfaceVersion:detailVue.interfaceVersion,
            tableId:detailVue.tableId,
        }
        new ServerInterface(baseInfo.detailInterface).execute(model, function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
                noty({"text":jsonObj.info,"type":"error","timeout":2000});
            }
            else
            {
                console.log(jsonObj);
                var empj_PjDevProgressForcast = jsonObj.empj_PjDevProgressForcast;
                detailVue.tableId = empj_PjDevProgressForcast.tableId;
                detailVue.busiState = empj_PjDevProgressForcast.busiState;
                detailVue.eCode = empj_PjDevProgressForcast.eCode;
                detailVue.eCodeFromConstruction = empj_PjDevProgressForcast.eCodeFromConstruction;
                detailVue.eCodeFromPublicSecurity = empj_PjDevProgressForcast.eCodeFromPublicSecurity;
                detailVue.developCompanyName = empj_PjDevProgressForcast.developCompanyName;
                detailVue.projectName = empj_PjDevProgressForcast.projectName;
                detailVue.eCodeOfBuilding = empj_PjDevProgressForcast.eCodeOfBuilding;
                detailVue.position = empj_PjDevProgressForcast.position;
                detailVue.upfloorNumber = empj_PjDevProgressForcast.upfloorNumber;
                detailVue.deliveryType = empj_PjDevProgressForcast.deliveryType;
                detailVue.currentBuildProgress = empj_PjDevProgressForcast.currentBuildProgress;
                detailVue.patrolPerson = empj_PjDevProgressForcast.patrolPerson;
                detailVue.patrolTimestamp = empj_PjDevProgressForcast.patrolTimestamp;
                detailVue.remark = empj_PjDevProgressForcast.remark;

                detailVue.empj_PjDevProgressForcastDtlList = jsonObj.empj_pjDevProgressForcastDtlList;
                var listCount = detailVue.empj_PjDevProgressForcastDtlList.length;
                detailVue.countPerPage = 0;
                if (listCount > 0)
                {
                    detailVue.countPerPage = listCount;
                }
            }
        });
    }

	function initData()
	{
        getIdFormTab("", function (id){
            detailVue.tableId = id;
            refresh();
        });
    }
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#empj_PjDevProgressForcastDetailDiv",
	"detailInterface":"../Empj_PjDevProgressForcastDetail"
});
