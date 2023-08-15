(function(baseInfo){
	var listVue = new Vue({
		el : baseInfo.listDivId,
		data : {
			interfaceVersion :19000101,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			keyword : "",
			selectItem : [],
			isAllSelected : false,
			theState:0,//正常为0，删除为1
				userStartId:null,
			userStartList:[],
			userRecordId:null,
			userRecordList:[],
			buildingId:null,
			buildingList:[],
			unitInfoId:null,
			unitInfoList:[],
			tripleAgreementId:null,
			tripleAgreementList:[],
			empj_HouseInfoList:[]
		},
		methods : {
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			listItemSelectHandle: listItemSelectHandle,
			empj_HouseInfoDelOne : empj_HouseInfoDelOne,
			empj_HouseInfoDel : empj_HouseInfoDel,
			showAjaxModal:showAjaxModal,
			search:search,
			checkAllClicked : checkAllClicked,
			changePageNumber : function(data){
				listVue.pageNumber = data;
			},
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue
		},
		watch:{
//			pageNumber : refresh,
			selectItem : selectedItemChanged,
		}
	});
	var updateVue = new Vue({
		el : baseInfo.updateDivId,
		data : {
			interfaceVersion :19000101,
			theState:null,
			busiState:null,
			eCode:null,
			userStartId:null,
			userStartList:[],
			createTimeStamp:null,
			lastUpdateTimeStamp:null,
			userRecordId:null,
			userRecordList:[],
			recordTimeStamp:null,
			buildingId:null,
			buildingList:[],
			eCodeOfBuilding:null,
			unitInfoId:null,
			unitInfoList:[],
			eCodeOfUnitInfo:null,
			eCodeFromPresellSystem:null,
			eCodeFromEscrowSystem:null,
			eCodeFromPublicSecurity:null,
			addressFromPublicSecurity:null,
			recordPrice:null,
			lastTimeStampSyncRecordPriceToPresellSystem:null,
			settlementStateOfTripleAgreement:null,
			tripleAgreementId:null,
			tripleAgreementList:[],
			eCodeFromPresellCert:null,
			floor:null,
			roomId:null,
			theNameOfRoomId:null,
			ySpan:null,
			xSpan:null,
			isMerged:null,
			mergedNums:null,
			isOverFloor:null,
			overFloors:null,
			position:null,
			purpose:null,
			property:null,
			deliveryType:null,
			forecastArea:null,
			actualArea:null,
			innerconsArea:null,
			shareConsArea:null,
			useArea:null,
			balconyArea:null,
			heigh:null,
			unitType:null,
			roomNumber:null,
			hallNumber:null,
			kitchenNumber:null,
			toiletNumber:null,
			eCodeOfOriginalHouse:null,
			isOpen:null,
			isPresell:null,
			isMortgage:null,
			limitState:null,
			eCodeOfRealBuidingUnit:null,
			eCodeOfBusManage1:null,
			eCodeOfBusManage2:null,
			eCodeOfMapping:null,
			eCodeOfPicture:null,
			remark:null,
			logId:null,
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateEmpj_HouseInfo
		}
	});
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			theState:null,
			busiState:null,
			eCode:null,
			userStartId:null,
			userStartList:[],
			createTimeStamp:null,
			lastUpdateTimeStamp:null,
			userRecordId:null,
			userRecordList:[],
			recordTimeStamp:null,
			buildingId:null,
			buildingList:[],
			eCodeOfBuilding:null,
			unitInfoId:null,
			unitInfoList:[],
			eCodeOfUnitInfo:null,
			eCodeFromPresellSystem:null,
			eCodeFromEscrowSystem:null,
			eCodeFromPublicSecurity:null,
			addressFromPublicSecurity:null,
			recordPrice:null,
			lastTimeStampSyncRecordPriceToPresellSystem:null,
			settlementStateOfTripleAgreement:null,
			tripleAgreementId:null,
			tripleAgreementList:[],
			eCodeFromPresellCert:null,
			floor:null,
			roomId:null,
			theNameOfRoomId:null,
			ySpan:null,
			xSpan:null,
			isMerged:null,
			mergedNums:null,
			isOverFloor:null,
			overFloors:null,
			position:null,
			purpose:null,
			property:null,
			deliveryType:null,
			forecastArea:null,
			actualArea:null,
			innerconsArea:null,
			shareConsArea:null,
			useArea:null,
			balconyArea:null,
			heigh:null,
			unitType:null,
			roomNumber:null,
			hallNumber:null,
			kitchenNumber:null,
			toiletNumber:null,
			eCodeOfOriginalHouse:null,
			isOpen:null,
			isPresell:null,
			isMortgage:null,
			limitState:null,
			eCodeOfRealBuidingUnit:null,
			eCodeOfBusManage1:null,
			eCodeOfBusManage2:null,
			eCodeOfMapping:null,
			eCodeOfPicture:null,
			remark:null,
			logId:null,
		},
		methods : {
			getAddForm : getAddForm,
			add : addEmpj_HouseInfo
		}
	});

	//------------------------方法定义-开始------------------//
	 
	//列表操作--------------获取"搜索列表"表单参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			pageNumber:this.pageNumber,
			countPerPage:this.countPerPage,
			totalPage:this.totalPage,
			keyword:this.keyword,
			theState:this.theState,
			userStartId:this.userStartId,
			userRecordId:this.userRecordId,
			buildingId:this.buildingId,
			unitInfoId:this.unitInfoId,
			tripleAgreementId:this.tripleAgreementId,
		}
	}
	//列表操作--------------获取“删除资源”表单参数
	function getDeleteForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			idArr:this.selectItem
		}
	}
	//列表操作--------------获取“新增”表单参数
	function getAddForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
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
	//列表操作--------------获取“更新”表单参数
	function getUpdateForm()
	{
		return{
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
	function empj_HouseInfoDel()
	{
		noty({
			layout:'center',
			modal:true,
			text:"确认批量删除吗？",
			type:"confirm",
			buttons:[
				{
					addClass:"btn btn-primary",
					text:"确定",
					onClick:function($noty){
						$noty.close();
						new ServerInterface(baseInfo.deleteInterface).execute(listVue.getDeleteForm(), function(jsonObj){
							if(jsonObj.result != "success")
							{
								noty({"text":jsonObj.info,"type":"error","timeout":2000});
							}
							else
							{
								listVue.selectItem = [];
								refresh();
							}
						});
					}
				},
				{
					addClass:"btn btn-danger",
					text:"取消",
					onClick:function($noty){
						
						$noty.close();
					}
				}
			]
			
		});
	}
	function empj_HouseInfoDelOne(empj_HouseInfoId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[empj_HouseInfoId],
		};
		
		noty({
			layout:'center',
			modal:true,
			text:"确认删除吗？",
			type:"confirm",
			buttons:[
				{
					addClass:"btn btn-primary",
					text:"确定",
					onClick:function($noty){
						$noty.close();
						new ServerInterface(baseInfo.deleteInterface).execute(model , function(jsonObj){
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
				},
				{
					addClass:"btn btn-danger",
					text:"取消",
					onClick:function($noty){
						
						$noty.close();
					}
				}
			]
			
		});
	}
	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		listVue.isAllSelected = (listVue.empj_HouseInfoList.length > 0)
		&&	(listVue.selectItem.length == listVue.empj_HouseInfoList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.empj_HouseInfoList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.empj_HouseInfoList.forEach(function(item) {
	    		listVue.selectItem.push(item.tableId);
	    	});
	    }
	}
	//列表操作--------------刷新
	function refresh()
	{
		new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				listVue.empj_HouseInfoList=jsonObj.empj_HouseInfoList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('empj_HouseInfoListDiv').scrollIntoView();
			}
		});
	}
	
	//列表操作------------搜索
	function search()
	{
		listVue.pageNumber = 1;
		refresh();
	}
	
	//弹出编辑模态框--更新操作
	function showAjaxModal(empj_HouseInfoModel)
	{
		//empj_HouseInfoModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'empj_HouseInfo', empj_HouseInfoModel);
		//updateVue.$set("empj_HouseInfo", empj_HouseInfoModel);
		
		updateVue.theState = empj_HouseInfoModel.theState;
		updateVue.busiState = empj_HouseInfoModel.busiState;
		updateVue.eCode = empj_HouseInfoModel.eCode;
		updateVue.userStartId = empj_HouseInfoModel.userStartId;
		updateVue.createTimeStamp = empj_HouseInfoModel.createTimeStamp;
		updateVue.lastUpdateTimeStamp = empj_HouseInfoModel.lastUpdateTimeStamp;
		updateVue.userRecordId = empj_HouseInfoModel.userRecordId;
		updateVue.recordTimeStamp = empj_HouseInfoModel.recordTimeStamp;
		updateVue.buildingId = empj_HouseInfoModel.buildingId;
		updateVue.eCodeOfBuilding = empj_HouseInfoModel.eCodeOfBuilding;
		updateVue.unitInfoId = empj_HouseInfoModel.unitInfoId;
		updateVue.eCodeOfUnitInfo = empj_HouseInfoModel.eCodeOfUnitInfo;
		updateVue.eCodeFromPresellSystem = empj_HouseInfoModel.eCodeFromPresellSystem;
		updateVue.eCodeFromEscrowSystem = empj_HouseInfoModel.eCodeFromEscrowSystem;
		updateVue.eCodeFromPublicSecurity = empj_HouseInfoModel.eCodeFromPublicSecurity;
		updateVue.addressFromPublicSecurity = empj_HouseInfoModel.addressFromPublicSecurity;
		updateVue.recordPrice = empj_HouseInfoModel.recordPrice;
		updateVue.lastTimeStampSyncRecordPriceToPresellSystem = empj_HouseInfoModel.lastTimeStampSyncRecordPriceToPresellSystem;
		updateVue.settlementStateOfTripleAgreement = empj_HouseInfoModel.settlementStateOfTripleAgreement;
		updateVue.tripleAgreementId = empj_HouseInfoModel.tripleAgreementId;
		updateVue.eCodeFromPresellCert = empj_HouseInfoModel.eCodeFromPresellCert;
		updateVue.floor = empj_HouseInfoModel.floor;
		updateVue.roomId = empj_HouseInfoModel.roomId;
		updateVue.theNameOfRoomId = empj_HouseInfoModel.theNameOfRoomId;
		updateVue.ySpan = empj_HouseInfoModel.ySpan;
		updateVue.xSpan = empj_HouseInfoModel.xSpan;
		updateVue.isMerged = empj_HouseInfoModel.isMerged;
		updateVue.mergedNums = empj_HouseInfoModel.mergedNums;
		updateVue.isOverFloor = empj_HouseInfoModel.isOverFloor;
		updateVue.overFloors = empj_HouseInfoModel.overFloors;
		updateVue.position = empj_HouseInfoModel.position;
		updateVue.purpose = empj_HouseInfoModel.purpose;
		updateVue.property = empj_HouseInfoModel.property;
		updateVue.deliveryType = empj_HouseInfoModel.deliveryType;
		updateVue.forecastArea = empj_HouseInfoModel.forecastArea;
		updateVue.actualArea = empj_HouseInfoModel.actualArea;
		updateVue.innerconsArea = empj_HouseInfoModel.innerconsArea;
		updateVue.shareConsArea = empj_HouseInfoModel.shareConsArea;
		updateVue.useArea = empj_HouseInfoModel.useArea;
		updateVue.balconyArea = empj_HouseInfoModel.balconyArea;
		updateVue.heigh = empj_HouseInfoModel.heigh;
		updateVue.unitType = empj_HouseInfoModel.unitType;
		updateVue.roomNumber = empj_HouseInfoModel.roomNumber;
		updateVue.hallNumber = empj_HouseInfoModel.hallNumber;
		updateVue.kitchenNumber = empj_HouseInfoModel.kitchenNumber;
		updateVue.toiletNumber = empj_HouseInfoModel.toiletNumber;
		updateVue.eCodeOfOriginalHouse = empj_HouseInfoModel.eCodeOfOriginalHouse;
		updateVue.isOpen = empj_HouseInfoModel.isOpen;
		updateVue.isPresell = empj_HouseInfoModel.isPresell;
		updateVue.isMortgage = empj_HouseInfoModel.isMortgage;
		updateVue.limitState = empj_HouseInfoModel.limitState;
		updateVue.eCodeOfRealBuidingUnit = empj_HouseInfoModel.eCodeOfRealBuidingUnit;
		updateVue.eCodeOfBusManage1 = empj_HouseInfoModel.eCodeOfBusManage1;
		updateVue.eCodeOfBusManage2 = empj_HouseInfoModel.eCodeOfBusManage2;
		updateVue.eCodeOfMapping = empj_HouseInfoModel.eCodeOfMapping;
		updateVue.eCodeOfPicture = empj_HouseInfoModel.eCodeOfPicture;
		updateVue.remark = empj_HouseInfoModel.remark;
		updateVue.logId = empj_HouseInfoModel.logId;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateEmpj_HouseInfo()
	{
		new ServerInterface(baseInfo.updateInterface).execute(updateVue.getUpdateForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				$(baseInfo.updateDivId).modal('hide');
				refresh();
			}
		});
	}
	function addEmpj_HouseInfo()
	{
		new ServerInterface(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				$(baseInfo.addDivId).modal('hide');
				addVue.busiState = null;
				addVue.eCode = null;
				addVue.userStartId = null;
				addVue.createTimeStamp = null;
				addVue.lastUpdateTimeStamp = null;
				addVue.userRecordId = null;
				addVue.recordTimeStamp = null;
				addVue.buildingId = null;
				addVue.eCodeOfBuilding = null;
				addVue.unitInfoId = null;
				addVue.eCodeOfUnitInfo = null;
				addVue.eCodeFromPresellSystem = null;
				addVue.eCodeFromEscrowSystem = null;
				addVue.eCodeFromPublicSecurity = null;
				addVue.addressFromPublicSecurity = null;
				addVue.recordPrice = null;
				addVue.lastTimeStampSyncRecordPriceToPresellSystem = null;
				addVue.settlementStateOfTripleAgreement = null;
				addVue.tripleAgreementId = null;
				addVue.eCodeFromPresellCert = null;
				addVue.floor = null;
				addVue.roomId = null;
				addVue.theNameOfRoomId = null;
				addVue.ySpan = null;
				addVue.xSpan = null;
				addVue.isMerged = null;
				addVue.mergedNums = null;
				addVue.isOverFloor = null;
				addVue.overFloors = null;
				addVue.position = null;
				addVue.purpose = null;
				addVue.property = null;
				addVue.deliveryType = null;
				addVue.forecastArea = null;
				addVue.actualArea = null;
				addVue.innerconsArea = null;
				addVue.shareConsArea = null;
				addVue.useArea = null;
				addVue.balconyArea = null;
				addVue.heigh = null;
				addVue.unitType = null;
				addVue.roomNumber = null;
				addVue.hallNumber = null;
				addVue.kitchenNumber = null;
				addVue.toiletNumber = null;
				addVue.eCodeOfOriginalHouse = null;
				addVue.isOpen = null;
				addVue.isPresell = null;
				addVue.isMortgage = null;
				addVue.limitState = null;
				addVue.eCodeOfRealBuidingUnit = null;
				addVue.eCodeOfBusManage1 = null;
				addVue.eCodeOfBusManage2 = null;
				addVue.eCodeOfMapping = null;
				addVue.eCodeOfPicture = null;
				addVue.remark = null;
				addVue.logId = null;
				refresh();
			}
		});
	}
	
	function indexMethod(index) {
		return generalIndexMethod(index, listVue)
	}

	function listItemSelectHandle(list) {
		generalListItemSelectHandle(listVue,list)
 	}

	function initData()
	{
		
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#empj_HouseInfoListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Empj_HouseInfoList",
	"addInterface":"../Empj_HouseInfoAdd",
	"deleteInterface":"../Empj_HouseInfoDelete",
	"updateInterface":"../Empj_HouseInfoUpdate"
});
