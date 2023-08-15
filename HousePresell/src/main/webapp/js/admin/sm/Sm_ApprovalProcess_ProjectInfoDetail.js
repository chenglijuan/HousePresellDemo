(function(baseInfo){
    var projectMapVue = new Vue({
        el : baseInfo.detailDivId,
        data : {
            interfaceVersion :19000101,
            tableId : 1,
            latitude:"",
            longitude:"",
            empj_ProjectInfoModel: {},
            oldProject: {},
            buttonType : "",//按钮来源（保存、提交）

            //附件材料
            busiType : "03010101",
            loadUploadList: [],
            showDelete : false,  //附件是否可编辑
            approvalCode: "03010102",
            //----------审批流start-----------//
            afId:"",//申请单Id
            workflowId:"",//结点Id
            isNeedBackup:"",//是否需要备案
            sourcePage:"",//来源页面
            //----------审批流end-----------//
        },
        methods : {
            //详情
            refresh : refresh,
            initData: initData,
            getSearchForm : getSearchForm,
            showModal: showModal,
            approvalHandle : function ()
		    {//备案项目信息
		        approvalModalVue.buttonType = 2;
		        approvalModalVue.approvalHandle();
		    },
        },
        computed:{

        },
        components : {
            "my-uploadcomponent":fileUpload
        },
        watch:{
        }
    });

    //------------------------方法定义-开始------------------//

    //按钮操作----审批项目信息
    function showModal()
    {
    	approvalModalVue.getModalWorkflowList();
    }

    //详情操作--------------获取"机构详情"参数
    function getSearchForm()
    {
        return {
            interfaceVersion:projectMapVue.interfaceVersion,
            tableId:projectMapVue.tableId,
            getDetailType:"1",
            afId:this.afId,
        }
    }

    //详情操作--------------
    function refresh()
    {
        if (projectMapVue.tableId == null || projectMapVue.tableId < 1)
        {
            return;
        }
        getDetail();
    }

    function getDetail()
    {
        console.log("------getDetail");
        new ServerInterfaceSync(baseInfo.detailInterface).execute(projectMapVue.getSearchForm(), function(jsonObj)
        {
            if(jsonObj.result != "success")
            {
                noty({"text":jsonObj.info,"type":"error","timeout":2000});
            }
            else
            {
                console.log(jsonObj);
                projectMapVue.empj_ProjectInfoModel = jsonObj.empj_ProjectInfoNew;
                projectMapVue.longitude = jsonObj.empj_ProjectInfoNew.longitude;
                projectMapVue.latitude = jsonObj.empj_ProjectInfoNew.latitude;
                projectMapVue.oldProject = jsonObj.empj_ProjectInfo;
                if (jsonObj.isNeedBackup != null)
                {
                    projectMapVue.isNeedBackup = jsonObj.isNeedBackup;
                    approvalModalVue.isNeedBackup = projectMapVue.isNeedBackup;
                }
                if(projectMapVue.oldProject.busiState == '未备案')
				{
                	projectMapVue.approvalCode = '03010101';
				}

                // if(jsonObj.empj_ProjectInfo.isNeedBackup)
                // {
                //     approvalModalVue.sourcePage = 2;
                // }
                
                 loadProjectPostionInMap();
            }
        });
    }

    //加载地图中项目位置
    function loadProjectPostionInMap()
    {
        var map = new BMap.Map("projectApprovalMap");
        var point1 = new BMap.Point(projectMapVue.longitude, projectMapVue.latitude);
        var icon1 = new BMap.Icon("../image/map_ico_location_blue.png", new BMap.Size(22,25));
        var marker1 = new BMap.Marker(point1, {icon:icon1});  // 创建标注
        map.addOverlay(marker1);            				  // 将标注添加到地图中

        if (projectMapVue.oldProject != null)
        {
            var point2 = new BMap.Point(projectMapVue.oldProject.longitude, projectMapVue.oldProject.latitude);
            var icon2 = new BMap.Icon("../image/map_ico_location.png", new BMap.Size(21,33));
            var marker2 = new BMap.Marker(point2, {icon:icon2});  // 创建标注
            map.addOverlay(marker2);
        }            				  // 将标注添加到地图中

        map.centerAndZoom(point1, 12);
        map.enableScrollWheelZoom();	  //开启鼠标滚轮缩放

        // var radius = new BMap.Bounds(point1, point2); // 范围 左下角，右上角的点位置
        // try {    // js中尽然还有try catch方法，可以避免bug引起的错误
        //     BMapLib.AreaRestriction.setBounds(map, radius); // 已map为中心，已b为范围的地图
        // } catch (e) {
        //     // 捕获错误异常
        //     alert(e);
        // }

        var loadCount = 1;
        map.addEventListener("tilesloaded",function(){
            if(loadCount == 1){
                map.centerAndZoom(point1, 12);
            }
            loadCount = loadCount + 1;
        });
    }

    function initData()
    {
        console.log("进入项目信息注册审批详情页面")
        var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
        console.log(tableIdStr);
        var array = tableIdStr.split("_");
        if (array.length > 1)
        {
            projectMapVue.tableId = array[array.length-4];
            projectMapVue.afId = array[array.length-3];
            projectMapVue.workflowId = array[array.length-2];

            approvalModalVue.afId = projectMapVue.afId;
            approvalModalVue.workflowId = projectMapVue.workflowId;
            approvalModalVue.sourcePage = array[array.length-1]
            projectMapVue.sourcePage = approvalModalVue.sourcePage;
            refresh();
        }

        generalLoadFile2(projectMapVue, projectMapVue.busiType);
    }

    //------------------------方法定义-结束------------------//

    //------------------------数据初始化-开始----------------//
    projectMapVue.initData();
    window.projectMapVue = projectMapVue;
    //------------------------数据初始化-结束----------------//
})({
    "detailDivId":"#sm_ApprovalProcess_ProjectInfoDetailDiv",
    "detailInterface":"../Empj_ProjectInfoDetail",
});
