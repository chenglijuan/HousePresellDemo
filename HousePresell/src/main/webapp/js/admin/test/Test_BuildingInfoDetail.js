(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion : 19000101,
			empj_BuildingInfo : "",
			tableId : 1,
			//户室详情相关
			pageNumber: 1,
			countPerPage: 10,
			totalPage: 1,
			totalCount: 1,
			selectItem: [],
			empj_HouseInfoList: [],
			//附件材料
			busiType : "03010201",
			loadUploadList: [],
			showDelete : false,  //附件是否可编辑
			flag:true, //判断是否是详情页是则显示旧的附件材料
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getEmpj_HouseInfoList : getEmpj_HouseInfoList,
			getSearchForm : getSearchForm,
			//户室详情相关
			changePageNumber: function (data) {
				detailVue.pageNumber = data;
			},
			test: function () {
			},
			listItemSelectHandle : function (list) 
			{
				generalListItemSelectHandle(detailVue, list)
			},
			indexMethod: function indexMethod(index)
			{
				return generalIndexMethod(index, detailVue);
			},
			changeCountPerPage : function (data) {
				if (detailVue.countPerPage != data) {
					detailVue.countPerPage = data;
					detailVue.getEmpj_HouseInfoList();
				}
			},
			moveToEdit:moveToEdit,
			handleSynchronize: handleSynchronize,
		},
		computed:{
			 
		},
		components : {
			'vue-nav': PageNavigationVue,
			"my-uploadcomponent":fileUpload
		},
		watch:{
			pageNumber: getEmpj_HouseInfoList,
		}
	});

	//------------------------方法定义-开始------------------//
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:this.tableId,
			reqSource:"详情",
		}
	}

	//详情操作--------------
	function refresh()
	{
		if (detailVue.tableId == null || detailVue.tableId < 1) 
		{
			return;
		}

		getDetail();
		getEmpj_HouseInfoList();
        parent.generalLoadFileTest(detailVue, detailVue.busiType);
	}

	function getDetail()
	{
		new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
                parent.generalErrorModal(jsonObj);
			}
			else
			{
				detailVue.empj_BuildingInfo = jsonObj.empj_BuildingInfo;
				detailVue.empj_BuildingInfo.landMortgageAmount = addThousands(jsonObj.empj_BuildingInfo.landMortgageAmount);
			}
		});
	}
	
	//获取户室详情
	function getEmpj_HouseInfoList()
	{
		var model = {
			buildingId : detailVue.tableId,
			interfaceVersion : 19000101,
			pageNumber:detailVue.pageNumber,
			countPerPage:detailVue.countPerPage,
			totalPage:detailVue.totalPage,
		};
		new ServerInterface(baseInfo.empj_HouseInfoListInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
                parent.generalErrorModal(jsonObj);
			}
			else
			{
				detailVue.empj_HouseInfoList = jsonObj.empj_HouseInfoList;
				detailVue.pageNumber=jsonObj.pageNumber;
				detailVue.countPerPage=jsonObj.countPerPage;
				detailVue.totalPage=jsonObj.totalPage;
				detailVue.totalCount = jsonObj.totalCount;
//				detailVue.empj_HouseInfoList.forEach(function(item){
//					item.recordPrice = addThousands(item.recordPrice);
//				})
			}
		});
	}
	
	//跳转编辑页面
	function moveToEdit() 
	{
        parent.enterNewTabAndCloseCurrent(detailVue.tableId,"编辑楼幢","test/Test_BuildingInfoEdit.shtml");
	}
	
	function handleSynchronize()
	{
		var model = {
				interfaceVersion:this.interfaceVersion,
				type: "save",
				tableId : detailVue.tableId,
				
		}
		new ServerInterface(baseInfo.synchronizeListInterface).execute(model, function(jsonObj)
				{
					if(jsonObj.result != "success")
					{
						generalErrorModal(jsonObj);
					}
					else
					{
                        parent.window.generalSuccessModal();
						detailVue.refresh();
					}
				});
	}
	
	function initData()
	{
        detailVue.tableId = parent.getTabElementUI_TableId();
        refresh();
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.initData();
	
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#empj_BuildingInfoDetailDiv",
	"detailInterface":"../../Empj_BuildingInfoDetail",
	"empj_HouseInfoListInterface":"../../Empj_HouseInfoList",
	"synchronizeListInterface":"../../Tb_b_room"
});
