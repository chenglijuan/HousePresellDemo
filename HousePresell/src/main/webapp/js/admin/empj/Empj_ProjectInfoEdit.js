(function(baseInfo){
	var pMapEditVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			busiState : "未备案",
            developCompanyId : "",
            emmp_CompanyInfoList : [],
            eCode : "",
            longitude : "",
            latitude : "",
            projectLeader : "",
            leaderPhone : "",
            remark : "",
            theName : "",
            address : "",
            cityRegionId : "",
            streetId : "",
            createTimeStamp : "",
            sm_CityRegionInfoList: [],
            sm_StreetInfoList: [],
			tableId : 1,
			introduction : "",
            isNotZhengtaiUser : true,
            buttonType : "1",//按钮来源（1保存、2提交）
            oldProject: {},

		    //附件材料
            busiType : '03010101',
            loadUploadList: [],
            showDelete : true,
            approvalCode: "03010102",
            showSubFlag : true,
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			//更新
			getUpdateForm : getUpdateForm,
			update: update,
			cityRegionChange : cityRegionChange,
            resetCityRegionChange: resetCityRegionChange,
			getDevelopCompanyId : function (data){
				this.developCompanyId = data.tableId;
			},
			getStreetId: function (data){
				this.streetId = data.tableId;
			},
			longitudeChange : function(){
				mp.clearOverlays();
				var marker = new BMap.Marker(new BMap.Point(this.longitude, this.latitude));  // 创建标注
				mp.addOverlay(marker);
			},
			latitudeChange : function(){
				mp.clearOverlays();
				var marker = new BMap.Marker(new BMap.Point(this.longitude, this.latitude));  // 创建标注
				mp.addOverlay(marker);
			},
		},
		computed:{
			 
		},
		components : {
			'vue-select': VueSelect,
            "my-uploadcomponent":fileUpload
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
			tableId:this.tableId,
            getDetailType:"1",
		}
	}

	//详情操作--------------
	function refresh()
	{
		if (pMapEditVue.tableId == null || pMapEditVue.tableId < 1) 
		{
			return;
		}

		getDetail();
	}

	function getDetail()
	{
		new ServerInterfaceSync(baseInfo.detailInterface).execute(pMapEditVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				pMapEditVue.busiState = jsonObj.empj_ProjectInfo.busiState;
				pMapEditVue.eCode = jsonObj.empj_ProjectInfo.eCode;
				pMapEditVue.developCompanyId = jsonObj.empj_ProjectInfo.developCompanyId;
				pMapEditVue.cityRegionId = jsonObj.empj_ProjectInfoNew.cityRegionId;
				pMapEditVue.address = jsonObj.empj_ProjectInfoNew.address;
				pMapEditVue.longitude = jsonObj.empj_ProjectInfoNew.longitude;
				pMapEditVue.latitude = jsonObj.empj_ProjectInfoNew.latitude;
				pMapEditVue.theName = jsonObj.empj_ProjectInfoNew.theName;
				pMapEditVue.projectLeader = jsonObj.empj_ProjectInfoNew.projectLeader;
				pMapEditVue.leaderPhone = jsonObj.empj_ProjectInfoNew.leaderPhone;
                pMapEditVue.introduction = jsonObj.empj_ProjectInfoNew.introduction;
				pMapEditVue.remark = jsonObj.empj_ProjectInfoNew.remark;

				pMapEditVue.oldProject = jsonObj.empj_ProjectInfo;
				
				if(pMapEditVue.cityRegionId != null && pMapEditVue.cityRegionId != '')
				{
					getStreetInfoList(pMapEditVue.cityRegionId)
				}				
				pMapEditVue.streetId = jsonObj.empj_ProjectInfoNew.streetId;

                loadProjectPostionInMap();
                
                if(pMapEditVue.busiState == '未备案')
				{
                	pMapEditVue.approvalCode = '03010101';
				}

			}
		});
	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getUpdateForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			busiState:this.busiState,
			tableId:this.tableId,
			eCode:this.eCode,
			developCompanyId:this.developCompanyId,
			cityRegionId:this.cityRegionId,
			streetId:this.streetId,
			address:this.address,
			introduction:this.introduction,
			longitude:this.longitude,
			latitude:this.latitude,
			theName:this.theName,
			projectLeader:this.projectLeader,
			leaderPhone:this.leaderPhone,
			remark:this.remark,
            buttonType:this.buttonType,
			//附件参数
            busiType : this.busiType,
            sourceId : this.tableId,
            generalAttachmentList : this.$refs.listenUploadData.uploadData,
		}
	}

	//更新项目信息
	function update(buttonType)
	{
        pMapEditVue.buttonType = buttonType;
        if(buttonType == 2){
        	pMapEditVue.showSubFlag = false;
        	
        } 
		new ServerInterfaceJsonBody(baseInfo.updateInterface).execute(pMapEditVue.getUpdateForm(), function(jsonObj)
		{
			pMapEditVue.showSubFlag = true;
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
                pMapEditVue.introduction = null;
                pMapEditVue.remark = null;

				generalSuccessModal();
                enterNewTabCloseCurrent(pMapEditVue.tableId,"项目详情","empj/Empj_ProjectInfoDetail.shtml");
			}
		});
	}

	//获取企业列表
	function getCompanyList()
	{
		var model = {
			interfaceVersion : 19000101,
		};
		new ServerInterface(baseInfo.companyListInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				pMapEditVue.emmp_CompanyInfoList = jsonObj.emmp_CompanyInfoList;
			}
		});
	}

	//获取城市区域列表
	function getCityRegionList() 
	{
		var model = {
			interfaceVersion: 19000101,
		};
		
		new  ServerInterfaceSync(baseInfo.cityRegionInfoInterface).execute(model, function(jsonObj) {
			if (jsonObj.result != "success")
            {
                noty({"text":jsonObj.info,"type":"error","timeout":2000});
            }
            else
            {
            	pMapEditVue.sm_CityRegionInfoList = jsonObj.sm_CityRegionInfoList;
            }
        });
    }

	function cityRegionChange(data)
	{
        if(this.cityRegionId != data.tableId)
        {
        	this.sm_StreetInfoList = [];
    		this.streetId = "";
        }
        this.cityRegionId = data.tableId;
        getStreetInfoList(this.cityRegionId);
	}

	function resetCityRegionChange()
	{
        this.cityRegionId = "";

        this.sm_StreetInfoList = [];
        this.streetId = "";
    }
	
	//获取街道列表
	function getStreetInfoList(cityRegionId)
	{
		var model = {
			interfaceVersion : 19000101,
			cityRegionId : cityRegionId,
		};
		
		new  ServerInterfaceSync(baseInfo.streetInfoInterface).execute(model, function(jsonObj) {
            if (jsonObj.result != "success")
            {
                noty({"text":jsonObj.info,"type":"error","timeout":2000});
            }
            else
            {
            	pMapEditVue.sm_StreetInfoList = jsonObj.sm_StreetInfoList;
            }
        });
	}

    //加载地图中项目位置
	var map = new BMap.Map("projectEditMap");
    function loadProjectPostionInMap()
    {
    	 //var map = new BMap.Map("projectEditMap");
         var point = new BMap.Point(119.981861,31.771397);
         map.centerAndZoom(point, 14);
         map.enableScrollWheelZoom();//开启鼠标滚轮缩放
         var point = new BMap.Point(pMapEditVue.longitude, pMapEditVue.latitude);
         var icon = new BMap.Icon("../image/map_ico_location.png", new BMap.Size(21,33));
         var marker = new BMap.Marker(point, {icon:icon});		// 创建标注
         map.addOverlay(marker);						    // 将标注添加到地图中

         // map.centerAndZoom(point, 12);
         map.enableScrollWheelZoom();	 				    //开启鼠标滚轮缩放

         var loadCount = 1;
         map.addEventListener("tilesloaded",function(){
             if(loadCount == 1){
                 map.centerAndZoom(point, 12);
             }
             loadCount = loadCount + 1;
         });
         map.addEventListener("click", showInfo);
    }
    
    function showInfo(e)
	{
    	map.clearOverlays();
		pMapEditVue.longitude = e.point.lng;
		pMapEditVue.latitude = e.point.lat;
		var icon = new BMap.Icon("../image/map_ico_location.png", new BMap.Size(21,33));
		var point = new BMap.Point(pMapEditVue.longitude, pMapEditVue.latitude);
		var marker = new BMap.Marker(point, {icon:icon});		// 创建标注
		map.addOverlay(marker); 
	}

    //获取用户信息
    function getLoginUserInfo()
    {
        var model = {
            interfaceVersion : pMapEditVue.interfaceVersion,
        };

        new ServerInterfaceSync(baseInfo.getLoginSm_UserInterface).execute(model, function (jsonObj) {
            if (jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
            	// console.log(jsonObj);
                if (jsonObj.sm_User.theType == 1)
                {
                    //正泰用户, 可以看出所有开发企业信息
                    pMapEditVue.isNotZhengtaiUser = false;
                    getCompanyList();
                }
                else
                {
                    pMapEditVue.isNotZhengtaiUser = true;
                    pMapEditVue.developCompanyId = jsonObj.sm_User.developCompanyId;

                    pMapEditVue.emmp_CompanyInfoList.push({
                        tableId:jsonObj.sm_User.developCompanyId,
                        theName:jsonObj.sm_User.theNameOfCompany,
                    })
                }
            }
        });
    }
    
	function initData()
	{
		// var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		// var array = tableIdStr.split("_");
		// if (array.length > 1) {
		// 	pMapEditVue.tableId = array[array.length - 1];
		// 	refresh();
		// }
        getIdFormTab("", function (id){
            pMapEditVue.tableId = id;
            refresh();
        });
        getLoginUserInfo();
		getCityRegionList();
		generalLoadFile2(pMapEditVue, pMapEditVue.busiType)
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	pMapEditVue.initData();
	window.pMapEditVue = pMapEditVue;
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#empj_ProjectInfoEditDiv",
    "getLoginSm_UserInterface":"../Sm_UserGet",
    "cityRegionInfoInterface":"../Sm_CityRegionInfoForSelect",
    "streetInfoInterface":"../Sm_StreetInfoForSelect",
	"companyListInterface":"../Emmp_CompanyInfoForSelect",
	"detailInterface":"../Empj_ProjectInfoDetail",
	"updateInterface":"../Empj_ProjectInfoUpdate"
});
