(function(baseInfo){
	var editVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			tableId : 1,
			keyword : "",
			selectItem : [],
			theState : 0,
			isAllSelected : false,
            errMsg :"",//存放错误提示信息
            Empj_HouseholdEditList : [],
            Empj_HouseholdEditModel : {},
            Empj_HouseholdEditModel1 : {},
			saveDisabled  : false,
			delDisabled : true,
			editDisabled : true,
			roomId : "",//室号
			position : "",//房屋坐落
			floor : "",//所在楼层
			forecastArea : "",//建筑面积（预测）
			shareConsArea : "",//分摊面积（预测）
			innerconsArea : "",//套内面积（预测）
			purpose : "1",//房屋用途
			recordPrice : "",//物价备案价格
			lastTimeStampSyncRecordPriceToPresellSystem : "",//物价备案时间
			theHouseState : "",//房屋状态
			eCodeOfTripleAgreement : "",//三方协议编号
			remark : "",
			busiState :"",
			flag : false,
			addOrEdit:0,
			tableEdit: "",
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			getBankForm : getBankForm,
			loadForm:loadForm,
			indexMethod : indexMethod,
			//添加
			getAddForm : getAddForm,
			getUploadForm : getUploadForm,
			add: add,
			addHouseholdHadle : addHouseholdHadle,//新增户信息
			changePageNumber : function(data){
				//editVue.pageNumber = data;
				if (editVue.pageNumber != data) {
					editVue.pageNumber = data;
					editVue.refresh();
				}
			},
			changeCountPerPage:function(data){
				if (editVue.countPerPage != data) {
					editVue.countPerPage = data;
					editVue.refresh();
				}
			},
			showDeleteModel : showDeleteModel,
			delHouseholdHandle : delHouseholdHandle,
			editHouseholdHadle:editHouseholdHadle,
			handleSelectionChange : handleSelectionChange ,
		},
		
		computed:{
		},
		components : {
			'vue-nav' : PageNavigationVue
		},
		watch:{
//		    pageNumber : refresh,	
		},
		mounted:function(){
			laydate.render({
			    elem: '#priceFilingDate',
				done: function(value){
					editVue.lastTimeStampSyncRecordPriceToPresellSystem = value;
				}
			});
		}
	});

	//------------------------方法定义-开始------------------//
	
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			pageNumber:this.pageNumber,
			countPerPage:this.countPerPage,
			totalPage:this.totalPage,
			tableId:this.tableId
		}
	}

	//详情操作--------------
	function refresh()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		console.log(tableIdStr)
		var array = tableIdStr.split("_");
		editVue.tableId = array[2];
		if (editVue.tableId == null || editVue.tableId < 1) 
		{
			return;
		}

		getDetail();
	}

	function getDetail()
	{
		new ServerInterface(baseInfo.detailInterface).execute(editVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				editVue.Empj_HouseholdEditModel = jsonObj.empj_HouseInfo;
				editVue.Empj_HouseholdEditModel1 = jsonObj.empj_HouseInfo.building
				editVue.Empj_HouseholdEditList = jsonObj.empj_HouseInfoList;
				editVue.Empj_HouseholdEditList.forEach(function(item) {
					item.recordPrice = addThousands(item.recordPrice);
				});
				editVue.pageNumber=jsonObj.pageNumber;
				editVue.countPerPage=jsonObj.countPerPage;
				editVue.totalPage=jsonObj.totalPage;
				editVue.totalCount = jsonObj.totalCount;
				editVue.keyword=jsonObj.keyword;
				editVue.selectedItem=[];
				
			}
		});
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
			roomId : this.roomId,
			position : this.position,
			floor : this.floor,
			forecastArea : this.forecastArea,
			shareConsArea : this.shareConsArea,
			innerconsArea : this.innerconsArea,
			purpose : this.purpose,
			recordPrice : this.recordPrice,//物价备案价格
			p1 : this.lastTimeStampSyncRecordPriceToPresellSystem,//物价备案时间
			theHouseState : this.theHouseState,//房屋状态
			eCodeOfTripleAgreement : this.eCodeOfTripleAgreement,//三方协议编号
			unitInfoId : this.tableId,
			remark : this.remark,
			tableId : editVue.tableEdit
		}
	}
	
	function indexMethod(index){
		return generalIndexMethod(index,editVue);
	}
	
	//获得信息
	function getBankForm(){
		return{
			interfaceVersion:this.interfaceVersion
		}
	}
	//列表操作---选择
    function handleSelectionChange(val) {
    	if(val.length == 1){
    		editVue.editDisabled = false;
    	}else{
    		editVue.editDisabled = true;
    	}
    	if(val.length>=1){
    		editVue.delDisabled = false;
    	}else{
    		editVue.delDisabled = true;
    	}
        editVue.selectItem = [];
        for (var index = 0; index < val.length; index++) {
            var element = val[index].tableId;
            editVue.selectItem.push(element)
        }
        editVue.tableEdit = editVue.selectItem[0];
    }
    //新增操作-----------清空页面
    function clearModel(){
    	editVue.roomId = "";
		editVue.position = "";
		editVue.floor = "";
		editVue.forecastArea = "";
		editVue.shareConsArea = "";
		editVue.innerconsArea = "";
		editVue.purpose = "1";
		editVue.recordPrice = "";
		editVue.lastTimeStampSyncRecordPriceToPresellSystem = "";
		editVue.theHouseState = "";
		editVue.eCodeOfTripleAgreement = "";
		editVue.busiState = "";
		editVue.remark="";
    }
    
	//新增操作------------------新增
	function addHouseholdHadle(){
		editVue.addOrEdit = 0;
		editVue.flag = false;
		editVue.selectItem = [];
		clearModel();
		$(baseInfo.addHouseholdDivId).modal({
			backdrop:"static"
		})
	}
	
	function showDeleteModel(){
		$(baseInfo.deleteDivId).modal('show', {
			backdrop:'static'
		});
	}
	//新增操作----------------删除
	function delHouseholdHandle(){
		var model = {
			interfaceVersion:this.interfaceVersion,
			idArr : this.selectItem
		}
		new ServerInterface(baseInfo.delHouseholdInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				editVue.errMsg = jsonObj.info;
				$(baseInfo.edModelDivId).modal('show', {
					backdrop:'static'
				});
			}
			else
			{
				
				$(baseInfo.deleteDivId).modal('hide');
				$(baseInfo.sdModelDivId).modal('show', {
					backdrop:'static'
				});
				refresh();
			}
		});
	}
	//新增操作------------------编辑
	function editHouseholdHadle(){
		editVue.addOrEdit = 1;
		var model = {
			interfaceVersion:this.interfaceVersion,
			tableId : editVue.tableEdit
		}
		new ServerInterface(baseInfo.editHouseholdInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				editVue.errMsg = jsonObj.info;
				$(baseInfo.edModelDivId).modal('show', {
					backdrop:'static'
				});
			}
			else
			{
				clearModel();
				editVue.roomId = jsonObj.empj_HouseInfo.roomId;
				editVue.position = jsonObj.empj_HouseInfo.position;
				editVue.floor = jsonObj.empj_HouseInfo.floor;
				editVue.forecastArea = jsonObj.empj_HouseInfo.forecastArea;
				editVue.shareConsArea = jsonObj.empj_HouseInfo.shareConsArea;
				editVue.innerconsArea = jsonObj.empj_HouseInfo.innerconsArea;
				editVue.purpose = jsonObj.empj_HouseInfo.purpose;
				editVue.recordPrice = jsonObj.empj_HouseInfo.recordPrice;
				editVue.lastTimeStampSyncRecordPriceToPresellSystem = jsonObj.empj_HouseInfo.lastTimeStampSyncRecordPriceToPresellSystem;
				editVue.theHouseState = jsonObj.empj_HouseInfo.theHouseState;
				editVue.eCodeOfTripleAgreement = jsonObj.empj_HouseInfo.eCodeOfTripleAgreement;
				editVue.busiState = jsonObj.empj_HouseInfo.busiState;
				editVue.remark = jsonObj.empj_HouseInfo.remark;
				editVue.flag = true;
				$(baseInfo.addHouseholdDivId).modal({
					backdrop:"static"
				})
			}
		});
	}
	function add()
	{
		if(editVue.addOrEdit == 0) {
			new ServerInterface(baseInfo.addInterface).execute(editVue.getAddForm(), function(jsonObj)
			{
				if(jsonObj.result != "success")
				{
					editVue.errMsg = jsonObj.info;
					$(baseInfo.edModelDivId).modal('show', {
						backdrop:'static'
					});
				}
				else
				{
					editVue.showButton = false;
					$(baseInfo.addHouseholdDivId).modal('hide')
					refresh();
				}
			});
		}else {
			new ServerInterface(baseInfo.editInterface).execute(editVue.getAddForm(), function(jsonObj)
			{
				if(jsonObj.result != "success")
				{
					editVue.errMsg = jsonObj.info;
					$(baseInfo.edModelDivId).modal('show', {
						backdrop:'static'
					});
				}
				else
				{
					editVue.showButton = false;
					$(baseInfo.addHouseholdDivId).modal('hide')
					refresh();
				}
			});
		}
	}
	//列表操作-----------------------获取附件参数
	function getUploadForm(){
		return{
			pageNumber : '0',
			busiType : editVue.busiType,
			interfaceVersion:this.interfaceVersion
		}
	}
	//
	function loadForm(){
		new ServerInterface(baseInfo.bankInterface).execute(editVue.getBankForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				editVue.errMsg = jsonObj.info;
			}
			else
			{
				//refresh();
				editVue.tgxy_BankAccountEscrowedList = jsonObj.tgxy_BankAccountEscrowedList;
			}
		});
	}
	function choiceRefundBankAccountForm(){
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:this.theBankAccountEscrowedId,
		}
	}
	function initData()
	{
		
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	editVue.loadForm();
	editVue.refresh();
	editVue.initData();

	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#Empj_HouseholdEditDiv",
	"addHouseholdDivId" : "#addHouseholdModel",
	"deleteDivId" : "#deleteHousehold",
	"edModelDivId":"#ed1Model",
	"sdModelDivId":"#sd1Model",
	"detailInterface":"../Empj_HouseInfoDetails",
	"addInterface":"../Empj_HouseInfoAdds",
	"editHouseholdInterface" : "../Empj_HouseInfoToUpdates",
	"delHouseholdInterface" : "../Empj_HouseInfoBatchDeletes",
	"editInterface":"../Empj_HouseInfoUpdates"
});
