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
			companyId:null,
			companyList:[],
			houseInfoId:null,
			houseInfoList:[],
			tgxy_ContractInfoList:[]
		},
		methods : {
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			tgxy_ContractInfoDelOne : tgxy_ContractInfoDelOne,
			tgxy_ContractInfoDel : tgxy_ContractInfoDel,
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
			eCodeOfContractRecord:null,
			companyId:null,
			companyList:[],
			theNameFormCompany:null,
			theNameOfProject:null,
			eCodeFromConstruction:null,
			houseInfoId:null,
			houseInfoList:[],
			eCodeOfHouseInfo:null,
			roomIdOfHouseInfo:null,
			contractSumPrice:null,
			buildingArea:null,
			position:null,
			contractSignDate:null,
			paymentMethod:null,
			loanBank:null,
			firstPaymentAmount:null,
			loanAmount:null,
			escrowState:null,
			payDate:null,
			eCodeOfBuilding:null,
			eCodeFromPublicSecurity:null,
			contractRecordDate:null,
			syncPerson:null,
			syncDate:null,
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateTgxy_ContractInfo
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
			eCodeOfContractRecord:null,
			companyId:null,
			companyList:[],
			theNameFormCompany:null,
			theNameOfProject:null,
			eCodeFromConstruction:null,
			houseInfoId:null,
			houseInfoList:[],
			eCodeOfHouseInfo:null,
			roomIdOfHouseInfo:null,
			contractSumPrice:null,
			buildingArea:null,
			position:null,
			contractSignDate:null,
			paymentMethod:null,
			loanBank:null,
			firstPaymentAmount:null,
			loanAmount:null,
			escrowState:null,
			payDate:null,
			eCodeOfBuilding:null,
			eCodeFromPublicSecurity:null,
			contractRecordDate:null,
			syncPerson:null,
			syncDate:null,
		},
		methods : {
			getAddForm : getAddForm,
			add : addTgxy_ContractInfo
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
			companyId:this.companyId,
			houseInfoId:this.houseInfoId,
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
			eCodeOfContractRecord:this.eCodeOfContractRecord,
			companyId:this.companyId,
			theNameFormCompany:this.theNameFormCompany,
			theNameOfProject:this.theNameOfProject,
			eCodeFromConstruction:this.eCodeFromConstruction,
			houseInfoId:this.houseInfoId,
			eCodeOfHouseInfo:this.eCodeOfHouseInfo,
			roomIdOfHouseInfo:this.roomIdOfHouseInfo,
			contractSumPrice:this.contractSumPrice,
			buildingArea:this.buildingArea,
			position:this.position,
			contractSignDate:this.contractSignDate,
			paymentMethod:this.paymentMethod,
			loanBank:this.loanBank,
			firstPaymentAmount:this.firstPaymentAmount,
			loanAmount:this.loanAmount,
			escrowState:this.escrowState,
			payDate:this.payDate,
			eCodeOfBuilding:this.eCodeOfBuilding,
			eCodeFromPublicSecurity:this.eCodeFromPublicSecurity,
			contractRecordDate:this.contractRecordDate,
			syncPerson:this.syncPerson,
			syncDate:this.syncDate,
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
			eCodeOfContractRecord:this.eCodeOfContractRecord,
			companyId:this.companyId,
			theNameFormCompany:this.theNameFormCompany,
			theNameOfProject:this.theNameOfProject,
			eCodeFromConstruction:this.eCodeFromConstruction,
			houseInfoId:this.houseInfoId,
			eCodeOfHouseInfo:this.eCodeOfHouseInfo,
			roomIdOfHouseInfo:this.roomIdOfHouseInfo,
			contractSumPrice:this.contractSumPrice,
			buildingArea:this.buildingArea,
			position:this.position,
			contractSignDate:this.contractSignDate,
			paymentMethod:this.paymentMethod,
			loanBank:this.loanBank,
			firstPaymentAmount:this.firstPaymentAmount,
			loanAmount:this.loanAmount,
			escrowState:this.escrowState,
			payDate:this.payDate,
			eCodeOfBuilding:this.eCodeOfBuilding,
			eCodeFromPublicSecurity:this.eCodeFromPublicSecurity,
			contractRecordDate:this.contractRecordDate,
			syncPerson:this.syncPerson,
			syncDate:this.syncDate,
		}
	}
	function tgxy_ContractInfoDel()
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
	function tgxy_ContractInfoDelOne(tgxy_ContractInfoId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[tgxy_ContractInfoId],
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
		listVue.isAllSelected = (listVue.tgxy_ContractInfoList.length > 0)
		&&	(listVue.selectItem.length == listVue.tgxy_ContractInfoList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgxy_ContractInfoList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgxy_ContractInfoList.forEach(function(item) {
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
				listVue.tgxy_ContractInfoList=jsonObj.tgxy_ContractInfoList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tgxy_ContractInfoListDiv').scrollIntoView();
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
	function showAjaxModal(tgxy_ContractInfoModel)
	{
		//tgxy_ContractInfoModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'tgxy_ContractInfo', tgxy_ContractInfoModel);
		//updateVue.$set("tgxy_ContractInfo", tgxy_ContractInfoModel);
		
		updateVue.theState = tgxy_ContractInfoModel.theState;
		updateVue.busiState = tgxy_ContractInfoModel.busiState;
		updateVue.eCode = tgxy_ContractInfoModel.eCode;
		updateVue.userStartId = tgxy_ContractInfoModel.userStartId;
		updateVue.createTimeStamp = tgxy_ContractInfoModel.createTimeStamp;
		updateVue.lastUpdateTimeStamp = tgxy_ContractInfoModel.lastUpdateTimeStamp;
		updateVue.userRecordId = tgxy_ContractInfoModel.userRecordId;
		updateVue.recordTimeStamp = tgxy_ContractInfoModel.recordTimeStamp;
		updateVue.eCodeOfContractRecord = tgxy_ContractInfoModel.eCodeOfContractRecord;
		updateVue.companyId = tgxy_ContractInfoModel.companyId;
		updateVue.theNameFormCompany = tgxy_ContractInfoModel.theNameFormCompany;
		updateVue.theNameOfProject = tgxy_ContractInfoModel.theNameOfProject;
		updateVue.eCodeFromConstruction = tgxy_ContractInfoModel.eCodeFromConstruction;
		updateVue.houseInfoId = tgxy_ContractInfoModel.houseInfoId;
		updateVue.eCodeOfHouseInfo = tgxy_ContractInfoModel.eCodeOfHouseInfo;
		updateVue.roomIdOfHouseInfo = tgxy_ContractInfoModel.roomIdOfHouseInfo;
		updateVue.contractSumPrice = tgxy_ContractInfoModel.contractSumPrice;
		updateVue.buildingArea = tgxy_ContractInfoModel.buildingArea;
		updateVue.position = tgxy_ContractInfoModel.position;
		updateVue.contractSignDate = tgxy_ContractInfoModel.contractSignDate;
		updateVue.paymentMethod = tgxy_ContractInfoModel.paymentMethod;
		updateVue.loanBank = tgxy_ContractInfoModel.loanBank;
		updateVue.firstPaymentAmount = tgxy_ContractInfoModel.firstPaymentAmount;
		updateVue.loanAmount = tgxy_ContractInfoModel.loanAmount;
		updateVue.escrowState = tgxy_ContractInfoModel.escrowState;
		updateVue.payDate = tgxy_ContractInfoModel.payDate;
		updateVue.eCodeOfBuilding = tgxy_ContractInfoModel.eCodeOfBuilding;
		updateVue.eCodeFromPublicSecurity = tgxy_ContractInfoModel.eCodeFromPublicSecurity;
		updateVue.contractRecordDate = tgxy_ContractInfoModel.contractRecordDate;
		updateVue.syncPerson = tgxy_ContractInfoModel.syncPerson;
		updateVue.syncDate = tgxy_ContractInfoModel.syncDate;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateTgxy_ContractInfo()
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
	function addTgxy_ContractInfo()
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
				addVue.eCodeOfContractRecord = null;
				addVue.companyId = null;
				addVue.theNameFormCompany = null;
				addVue.theNameOfProject = null;
				addVue.eCodeFromConstruction = null;
				addVue.houseInfoId = null;
				addVue.eCodeOfHouseInfo = null;
				addVue.roomIdOfHouseInfo = null;
				addVue.contractSumPrice = null;
				addVue.buildingArea = null;
				addVue.position = null;
				addVue.contractSignDate = null;
				addVue.paymentMethod = null;
				addVue.loanBank = null;
				addVue.firstPaymentAmount = null;
				addVue.loanAmount = null;
				addVue.escrowState = null;
				addVue.payDate = null;
				addVue.eCodeOfBuilding = null;
				addVue.eCodeFromPublicSecurity = null;
				addVue.contractRecordDate = null;
				addVue.syncPerson = null;
				addVue.syncDate = null;
				refresh();
			}
		});
	}
	
	function indexMethod(index) {
		return generalIndexMethod(index, listVue)
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
	"listDivId":"#tgxy_ContractInfoListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Tgxy_ContractInfoList",
	"addInterface":"../Tgxy_ContractInfoAdd",
	"deleteInterface":"../Tgxy_ContractInfoDelete",
	"updateInterface":"../Tgxy_ContractInfoUpdate"
});
