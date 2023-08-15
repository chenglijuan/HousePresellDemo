(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			empj_HouseInfoModel: {},
			tableId : 1,
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			//更新
			getUpdateForm : getUpdateForm,
			update: update,
		},
		computed:{
			 
		},
		components : {

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
		}
	}

	//详情操作--------------
	function refresh()
	{
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
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				detailVue.empj_HouseInfoModel = jsonObj.empj_HouseInfo;
			}
		});
	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getUpdateForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			theState:this.theState,
			busiState:this.busiState,
			eCode:this.eCode,
			userStartId:this.userStartId,
			createTimeStamp:this.createTimeStamp,
			lastUpdateTimeStamp:this.lastUpdateTimeStamp,
			userRecordId:this.userRecordId,
			recordTimeStamp:this.recordTimeStamp,
			buildingId:this.buildingId,
			eCodeOfBuilding:this.eCodeOfBuilding,
			unitInfoId:this.unitInfoId,
			eCodeOfUnitInfo:this.eCodeOfUnitInfo,
			eCodeFromPresellSystem:this.eCodeFromPresellSystem,
			eCodeFromEscrowSystem:this.eCodeFromEscrowSystem,
			eCodeFromPublicSecurity:this.eCodeFromPublicSecurity,
			addressFromPublicSecurity:this.addressFromPublicSecurity,
			recordPrice:this.recordPrice,
			lastTimeStampSyncRecordPriceToPresellSystem:this.lastTimeStampSyncRecordPriceToPresellSystem,
			settlementStateOfTripleAgreement:this.settlementStateOfTripleAgreement,
			tripleAgreementId:this.tripleAgreementId,
			eCodeFromPresellCert:this.eCodeFromPresellCert,
			floor:this.floor,
			roomId:this.roomId,
			theNameOfRoomId:this.theNameOfRoomId,
			ySpan:this.ySpan,
			xSpan:this.xSpan,
			isMerged:this.isMerged,
			mergedNums:this.mergedNums,
			isOverFloor:this.isOverFloor,
			overFloors:this.overFloors,
			position:this.position,
			purpose:this.purpose,
			property:this.property,
			deliveryType:this.deliveryType,
			forecastArea:this.forecastArea,
			actualArea:this.actualArea,
			innerconsArea:this.innerconsArea,
			shareConsArea:this.shareConsArea,
			useArea:this.useArea,
			balconyArea:this.balconyArea,
			heigh:this.heigh,
			unitType:this.unitType,
			roomNumber:this.roomNumber,
			hallNumber:this.hallNumber,
			kitchenNumber:this.kitchenNumber,
			toiletNumber:this.toiletNumber,
			eCodeOfOriginalHouse:this.eCodeOfOriginalHouse,
			isOpen:this.isOpen,
			isPresell:this.isPresell,
			isMortgage:this.isMortgage,
			limitState:this.limitState,
			eCodeOfRealBuidingUnit:this.eCodeOfRealBuidingUnit,
			eCodeOfBusManage1:this.eCodeOfBusManage1,
			eCodeOfBusManage2:this.eCodeOfBusManage2,
			eCodeOfMapping:this.eCodeOfMapping,
			eCodeOfPicture:this.eCodeOfPicture,
			remark:this.remark,
			logId:this.logId,
		}
	}

	function update()
	{
		new ServerInterface(baseInfo.updateInterface).execute(detailVue.getUpdateForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				$(baseInfo.detailDivId).modal('hide');
				refresh();
			}
		});
	}
	
	function initData()
	{
		
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#empj_HouseInfoDiv",
	"detailInterface":"../Empj_HouseInfoDetail",
	"updateInterface":"../Empj_HouseInfoUpdate"
});
