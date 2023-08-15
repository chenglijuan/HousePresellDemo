(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			emmp_ComAccountModel: {},
			tableId : 1,
			tableIdUnit: 1,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			selectItem : [],
			buildingList: [
				{eCode:1},
				{eCode:2}
			],
			selNum: 0,
			selectRadio: 1,
			mark: "",
			idArr: [],
			upUnitList: [
				{eCode: 1,use:1},
				{eCode: 2,use:1}
			],
			upUseData: [
				{theId:"0",theName:"住宅"},
				{theId:"1",theName:"非住宅"},
			],
			downUnitList: [
				{busiState: 120,use1:2}
			],
			use: 1,
			use1: 2,
			projectName: "",
			eCodeFromConstruction: "",
			unitNumber: "",
			addTheName: "",
			addUpfloorNumber: "",
			addUpfloorHouseHoldNumber: "",
			addDownfloorNumber: "",
			addDownfloorHouseHoldNumber: "",
			selectRadioWater: "",
			selectRadioEle: "",
			addRemark: "",
			mask:0,// 新增    1：修改，
			errMsg: ""
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			listItemSelectHandle: listItemSelectHandle,
			indexMethod: indexMethod,
			changePageNumber : function(data){
				listVue.pageNumber = data;
			},
			handleEditSave: handleEditSave,
			LogModelClose: LogModelClose,
			addHandle: addHandle,
			addAndEditUnitHandle: addAndEditUnitHandle,
			resetHandle: resetHandle,
			update: update,
			unitInitHandle: unitInitHandle,
			getAddForm: getAddForm,
			deleteHandle: deleteHandle,
			getDeleteForm: getDeleteForm,
			getSearchByUnitForm: getSearchByUnitForm,
			getAdd1Form: getAdd1Form,
			getInitUpdateForm: getInitUpdateForm,
			unitDetailHandle: unitDetailHandle,
			succClose:succClose,
			errClose: errClose,
			SetIsNull:SetIsNull
		},
		computed:{
			 
		},
		components : {

		}
	});

	//------------------------方法定义-开始------------------//
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:this.tableId,
		}
	}

	//详情操作--------------
	function refresh()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var list = tableIdStr.split("_");
		tableIdStr = list[2];
		detailVue.tableId = tableIdStr;
		if (this.tableId == null || this.tableId < 1) 
		{
			return;
		}

		getDetail();
	}

	function getDetail()
	{
		new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
			}
			else
			{
				detailVue.projectName = jsonObj.projectName;
				detailVue.eCodeFromConstruction = jsonObj.eCodeFromConstruction;
				detailVue.unitNumber = jsonObj.unitNumber;
				detailVue.buildingList = jsonObj.empj_UnitInfoList;
			}
		});
	}
	
	function listItemSelectHandle(list) {
		generalListItemSelectHandle(detailVue,list);
		var length = list.length;
		detailVue.selNum = length;
		detailVue.tableIdUnit = list[0].tableId;
		console.log(length);
		if(length > 0){
	        for(var i = 0;i < length;i++){
	        	detailVue.idArr.push(list[i].tableId);
	        }
        }
 	}
	
	function indexMethod(index) {
		return generalIndexMethod(index, detailVue)
	}
	
	function handleEditSave()
	{
		if(detailVue.mask == 0){
			new ServerInterface(baseInfo.addInterface).execute(detailVue.getAddForm(), function(jsonObj)
			{
				if(jsonObj.result != "success")
				{
					detailVue.errMsg = jsonObj.info;
					$(baseInfo.LogDetailModel).modal('hide');
					$(baseInfo.errorBuild).modal('show', {
					    backdrop :'static'
				    });
				}
				else
				{
					$(baseInfo.LogDetailModel).modal('hide');
					$(baseInfo.successBuild).modal('show', {
					    backdrop :'static'
				    });
					getDetail();
				}
			});
		}else {
			new ServerInterface(baseInfo.updateInterface).execute(detailVue.getAdd1Form(), function(jsonObj)
			{
				if(jsonObj.result != "success")
				{
					detailVue.errMsg = jsonObj.info;
					$(baseInfo.LogDetailModel).modal('hide');
					$(baseInfo.errorBuild).modal('show', {
					    backdrop :'static'
				    });
				}
				else
				{
					$(baseInfo.LogDetailModel).modal('hide');
					$(baseInfo.successBuild).modal('show', {
					    backdrop :'static'
				    });
					getDetail();
				}
			});
		}
		
		SetIsNull();
	}
	
	function succClose()
	{
		$(baseInfo.successBuild).modal('hide');
	}
	
	function errClose()
	{
		$(baseInfo.errorBuild).modal('hide');
	}
	
	function getAddForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:detailVue.tableId,
			theName:detailVue.addTheName,
			upfloorNumber:detailVue.addUpfloorNumber,
			upfloorHouseHoldNumber:detailVue.addUpfloorHouseHoldNumber,
			downfloorNumber:detailVue.addDownfloorNumber,
			downfloorHouseHoldNumber:detailVue.addDownfloorHouseHoldNumber,
			elevatorNumber:detailVue.selectRadioEle,
			secondaryWaterSupply:detailVue.selectRadioWater,
			remark:detailVue.addRemark,
		}
	}
	
	function getAdd1Form()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:detailVue.tableIdUnit,
			theName:detailVue.addTheName,
			upfloorNumber:detailVue.addUpfloorNumber,
			upfloorHouseHoldNumber:detailVue.addUpfloorHouseHoldNumber,
			downfloorNumber:detailVue.addDownfloorNumber,
			downfloorHouseHoldNumber:detailVue.addDownfloorHouseHoldNumber,
			elevatorNumber:detailVue.selectRadioEle,
			secondaryWaterSupply:detailVue.selectRadioWater,
			remark:detailVue.addRemark,
		}
	}
	
	function deleteHandle()
	{
		new ServerInterface(baseInfo.deleteInterface).execute(detailVue.getDeleteForm(), function(jsonObj)
				{
					if(jsonObj.result != "success")
					{
						detailVue.errMsg = jsonObj.info;
						$(baseInfo.errorBuild).modal('show', {
						    backdrop :'static'
					    });
					}
					else
					{
						$(baseInfo.successBuild).modal('show', {
						    backdrop :'static'
					    });
						getDetail();
					}
				});
	}
	
	function getDeleteForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			idArr: detailVue.idArr
		}
	}
	
	function LogModelClose()
	{
		$(baseInfo.LogDetailModel).modal('hide');
		SetIsNull();
	}
	
	function SetIsNull()
	{
		detailVue.addRemark = "";
		detailVue.selectRadioEle = "";
		detailVue.selectRadioWater = "";
		detailVue.addDownfloorHouseHoldNumber = "";
		detailVue.addDownfloorNumber = "";
		detailVue.addUpfloorHouseHoldNumber = "";
		detailVue.addUpfloorNumber = "";
		detailVue.addTheName = "";
	}
	
	function addHandle()
	{
		detailVue.mask = 0;
		$(baseInfo.LogDetailModel).modal('show', {
		    backdrop :'static'
	    });
	}
	
	function addAndEditUnitHandle()
	{
		detailVue.mask = 1;
		$(baseInfo.LogDetailModel).modal('show', {
		    backdrop :'static'
	    });
		new ServerInterface(baseInfo.editInterface).execute(detailVue.getSearchByUnitForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
			}
			else
			{
				detailVue.addTheName = jsonObj.empj_UnitInfo.theName;
				detailVue.addUpfloorNumber = jsonObj.empj_UnitInfo.upfloorNumber;
				detailVue.addUpfloorHouseHoldNumber = jsonObj.empj_UnitInfo.upfloorHouseHoldNumber;
				detailVue.addDownfloorNumber = jsonObj.empj_UnitInfo.downfloorNumber;
				detailVue.addDownfloorHouseHoldNumber = jsonObj.empj_UnitInfo.downfloorHouseHoldNumber;
				detailVue.selectRadioEle = jsonObj.empj_UnitInfo.elevatorNumber;
				detailVue.selectRadioWater = jsonObj.empj_UnitInfo.secondaryWaterSupply;
				detailVue.addRemark = jsonObj.empj_UnitInfo.remark;
			}
		});
	}
	
	function getSearchByUnitForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:detailVue.tableIdUnit,
		}
	}
	
	function unitInitHandle()
	{
		$(baseInfo.loadDetailModel).modal('show', {
		    backdrop :'static'
	    });
		
		new ServerInterface(baseInfo.initInterface).execute(detailVue.getSearchByUnitForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
			}
			else
			{
				detailVue.upUnitList = jsonObj.empj_HouseInfoListUpfloor;
				detailVue.downUnitList = jsonObj.empj_HouseInfoListDownfloor;
			}
		});
		
	}
	
	function resetHandle()
	{
		detailVue.upUnitList.forEach(function(value,index){
		    value.forecastArea = "";
		    value.purpose = "0";
		});
		
		detailVue.downUnitList.forEach(function(value,index){
			console.log(value);
		    value.forecastArea = "";
		    value.purpose = "0";
		});
	}
	
	function update()
	{
		new ServerInterface(baseInfo.initUpdateInterface).execute(detailVue.getInitUpdateForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				detailVue.errMsg = jsonObj.info;
				$(baseInfo.loadDetailModel).modal('hide');
				$(baseInfo.errorBuild).modal('show', {
				    backdrop :'static'
			    });
			}
			else
			{
				$(baseInfo.loadDetailModel).modal('hide');
				$(baseInfo.successBuild).modal('show', {
				    backdrop :'static'
			    });
				getDetail();
			}
		});
	}
	
	function unitDetailHandle()
	{
		enterNext2Tab(detailVue.tableIdUnit, '编辑户信息', 'empj/Empj_HouseholdEdit.shtml',detailVue.tableIdUnit+"03010205");
	}
	
	function getInitUpdateForm()
	{
		var upList = JSON.stringify(detailVue.upUnitList);
		var downList = JSON.stringify(detailVue.downUnitList);
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:detailVue.tableIdUnit,
			empj_HouseInfoListUpfloor: upList,
			empj_HouseInfoListDownfloor: downList
		}
	}
	
	function initData()
	{
		console.log(detailVue.selNum);
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#buildingEditDiv",
	"LogDetailModel":"#logDetail",
	"loadDetailModel":"#loadDetail",
	"errorBuild":"#errorBuild",
	"successBuild":"#successBuild",
	"detailInterface":"../Empj_UnitInfoDetailList",
	"addInterface":"../Empj_UnitInfoAdd",
	"deleteInterface":"../Empj_UnitInfoBatchDelete",
	"editInterface":"../Empj_UnitInfoDetail",
	"updateInterface":"../Empj_UnitInfoUpdate",
	"initInterface":"../Empj_UnitInfoHouseDetail",
	"initUpdateInterface":"../Empj_UnitInfoAutoHouseAdd",
});
