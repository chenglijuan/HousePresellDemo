(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			empj_HouseInfoModel: {},
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			//添加
			getAddForm : getAddForm,
			add: add,
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

	function add()
	{
		new ServerInterface(baseInfo.addInterface).execute(detailVue.getAddForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
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
	"addDivId":"#empj_HouseInfoDiv",
	"detailInterface":"../Empj_HouseInfoDetail",
	"addInterface":"../Empj_HouseInfoAdd"
});
