(function(baseInfo){
	var pMapAddVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			busiState: "未备案",
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
            sm_CityRegionInfoList: [],
            sm_StreetInfoList: [],
			isNotZhengtaiUser : true,
			empj_ProjectInfoModel: {},
			introduction : "",
            buttonType : "",

            //附件材料
            busiType : '03010101',
            loadUploadList: [],
            showDelete : true,  //附件是否可编辑
            showSubFlag : true,
            
		},
		methods : {
			//详情
			initData: initData,
			getSearchForm: getSearchForm,
			//添加
			getAddForm: getAddForm,
			add: add,
			cityRegionChange: cityRegionChange,
			getDevelopCompanyId: function (data){
				// console.log(data);
				this.developCompanyId = data.tableId;
			},
			getStreetId: function (data){
				this.streetId = data.tableId;
			},
			longitudeChange: function(){
				mp.clearOverlays();
				var marker = new BMap.Marker(new BMap.Point(this.longitude, this.latitude));  // 创建标注
				mp.addOverlay(marker);
			},
			latitudeChange: function(){
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
			interfaceVersion:this.interfaceVersion,

			busiState: this.busiState,
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
            //附件材料
            busiType : this.busiType,
            generalAttachmentList : this.$refs.listenUploadData.uploadData
		}
	}

	//新增项目信息，发起审批流
	function add(buttonType)
	{
        pMapAddVue.buttonType = buttonType;
		// console.log(pMapAddVue.getAddForm());
        if(buttonType == 2){
        	pMapAddVue.showSubFlag = false;
        	
        } 
		new ServerInterfaceJsonBody(baseInfo.addInterface).execute(pMapAddVue.getAddForm(), function(jsonObj)
		{
			pMapAddVue.showSubFlag = true;
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                pMapAddVue.introduction = null;
                pMapAddVue.remark = null;

                generalSuccessModal();
                enterNewTabCloseCurrent(jsonObj.tableId,"项目详情","empj/Empj_ProjectInfoDetail.shtml");
            }
		});
	}

    //获取城市区域列表
	function getCityRegionList() 
	{
		var model = {
			interfaceVersion: 19000101,
		};
		
		new  ServerInterface(baseInfo.cityRegionInfoInterface).execute(model, function(jsonObj) {
			if (jsonObj.result != "success")
            {
				generalErrorModal(jsonObj);
            }
            else
            {
            	pMapAddVue.sm_CityRegionInfoList = jsonObj.sm_CityRegionInfoList;
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
	
	//获取街道列表
	function getStreetInfoList(cityRegionId)
	{
		var model = {
			interfaceVersion : 19000101,
			cityRegionId : cityRegionId,
		};
		
		new  ServerInterface(baseInfo.streetInfoInterface).execute(model, function(jsonObj) {
            if (jsonObj.result != "success")
            {
            	generalErrorModal(jsonObj);
            }
            else
            {
                pMapAddVue.sm_StreetInfoList = jsonObj.sm_StreetInfoList;
            }
        });
	}
	
	//获取企业列表
	function getCompanyList()
	{
		var developCompanyId = null;
		if (pMapAddVue.developCompanyId.length > 1)
		{
            developCompanyId = pMapAddVue.developCompanyId;
		}
		var model = {
			interfaceVersion : 19000101,
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
				pMapAddVue.emmp_CompanyInfoList = jsonObj.emmp_CompanyInfoList;
			}
		});
	}

	//获取用户信息
	function getLoginUserInfo()
	{
        var model = {
            interfaceVersion : pMapAddVue.interfaceVersion,
        };

        new ServerInterfaceSync(baseInfo.getLoginSm_UserInterface).execute(model, function (jsonObj) {
            if (jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
            	if (jsonObj.sm_User.theType == 1)
				{
                    //正泰用户, 可以看出所有开发企业信息
					pMapAddVue.isNotZhengtaiUser = false;
					getCompanyList();
				}
				else
				{
                    pMapAddVue.isNotZhengtaiUser = true;
					pMapAddVue.developCompanyId = jsonObj.sm_User.developCompanyId;
					
					pMapAddVue.emmp_CompanyInfoList.push({
		    			tableId:jsonObj.sm_User.developCompanyId,
		    			theName:jsonObj.sm_User.theNameOfCompany,
		    		})
				}
            }
        });
    }

	function initData()
	{
		getLoginUserInfo();
		getCityRegionList();
        generalLoadFile2(pMapAddVue, pMapAddVue.busiType);
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
    pMapAddVue.initData();
    window.pMapAddVue = pMapAddVue;
	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#empj_ProjectInfoAddDiv",
    "getLoginSm_UserInterface":"../Sm_UserGet",
    "cityRegionInfoInterface":"../Sm_CityRegionInfoForSelect",
    "streetInfoInterface":"../Sm_StreetInfoForSelect",
	"companyListInterface":"../Emmp_CompanyInfoForSelect",
	"addInterface":"../Empj_ProjectInfoAdd",
});
