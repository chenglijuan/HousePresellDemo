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
			userStartId:null,
			userStartList:[],
			userRecordId:null,
			userRecordList:[],
			companyInfoList:[],
			projectId:null,
			projectList:[],
			buildingId:null,
			buildingList:[],
			tgpf_FundAccountInfoList:[],
			selNum: "",
			idArr: [],
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			listItemSelectHandle: listItemSelectHandle,
			tgpf_FundAccountInfoDelOne : tgpf_FundAccountInfoDelOne,
			tgpf_FundAccountInfoDel : tgpf_FundAccountInfoDel,
			showAjaxModal:showAjaxModal,
			search:search,
			checkAllClicked : checkAllClicked,
			indexMethod: function (index) {
				if (listVue.pageNumber > 1) {
					return (listVue.pageNumber - 1) * listVue.countPerPage - 0 + (index - 0 + 1);
				}
				if (listVue.pageNumber <= 1) {
					return index - 0 + 1;
				}
			},
			handleFundAccountInfoEdit: handleFundAccountInfoEdit,
			handleFundAccountInfoSearch: handleFundAccountInfoSearch,
			errClose: errClose,
			succClose: succClose,
			changePageNumber: function (data) {
				console.log(data);
				if (listVue.pageNumber != data) {
					listVue.pageNumber = data;
					listVue.refresh();
				}
			},
			changeCountPerPage : function (data) {
				console.log(data);
				if (listVue.countPerPage != data) {
					listVue.countPerPage = data;
					listVue.refresh();
				}
			},
			handleReset: handleReset,
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue
		},
		watch:{
			//pageNumber : refresh,
			selectItem : selectedItemChanged,
		}
	});
	var updateVue = new Vue({
		el : baseInfo.updateDivId,
		data : {
			interfaceVersion :19000101,
			userStartId:null,
			userStartList:[],
			userRecordId:null,
			userRecordList:[],
			companyInfoList:[],
			projectId:null,
			projectList:[],
			buildingId:null,
			buildingList:[],
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateTgpf_FundAccountInfo
		}
	});
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			userStartId:null,
			userStartList:[],
			userRecordId:null,
			userRecordList:[],
			companyInfoList:[],
			projectId:null,
			projectList:[],
			buildingId:null,
			buildingList:[],
		},
		methods : {
			getAddForm : getAddForm,
			add : addTgpf_FundAccountInfo
		}
	});

	//------------------------方法定义-开始------------------//
	 
	//列表操作--------------获取"搜索列表"表单参数
	function getSearchForm()
	{
		return {
			interfaceVersion:listVue.interfaceVersion,
			pageNumber:this.pageNumber,
			countPerPage:this.countPerPage,
			totalPage:this.totalPage,
			keyword:this.keyword,
			/*userStartId:this.userStartId,
			userRecordId:this.userRecordId,
			projectId:this.projectId,
			buildingId:this.buildingId,
			theNameOfCompany:this.theNameOfCompany,
			fullNameOfCompanyFromFinanceSystem:this.fullNameOfCompanyFromFinanceSystem,
			theNameOfProject:this.theNameOfProject,
			fullNameOfProjectFromFinanceSystem:this.fullNameOfProjectFromFinanceSystem,
			eCodeFromConstruction:this.eCodeFromConstruction,
			fullNameOfBuildingFromFinanceSystem:this.fullNameOfBuildingFromFinanceSystem,
			shortNameOfBuildingFromFinanceSystem:this.shortNameOfBuildingFromFinanceSystem,
			configureUser:this.configureUser,
			configureTime:this.configureTime,*/
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
			userStartId:this.userStartId,
			userRecordId:this.userRecordId,
			projectId:this.projectId,
			buildingId:this.buildingId,
			theNameOfCompany:this.theNameOfCompany,
			fullNameOfCompanyFromFinanceSystem:this.fullNameOfCompanyFromFinanceSystem,
			theNameOfProject:this.theNameOfProject,
			fullNameOfProjectFromFinanceSystem:this.fullNameOfProjectFromFinanceSystem,
			eCodeFromConstruction:this.eCodeFromConstruction,
			fullNameOfBuildingFromFinanceSystem:this.fullNameOfBuildingFromFinanceSystem,
			shortNameOfBuildingFromFinanceSystem:this.shortNameOfBuildingFromFinanceSystem,
			configureUser:this.configureUser,
			configureTime:this.configureTime,
		}
	}
	//列表操作--------------获取“更新”表单参数
	function getUpdateForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			userStartId:this.userStartId,
			userRecordId:this.userRecordId,
			projectId:this.projectId,
			buildingId:this.buildingId,
			theNameOfCompany:this.theNameOfCompany,
			fullNameOfCompanyFromFinanceSystem:this.fullNameOfCompanyFromFinanceSystem,
			theNameOfProject:this.theNameOfProject,
			fullNameOfProjectFromFinanceSystem:this.fullNameOfProjectFromFinanceSystem,
			eCodeFromConstruction:this.eCodeFromConstruction,
			fullNameOfBuildingFromFinanceSystem:this.fullNameOfBuildingFromFinanceSystem,
			shortNameOfBuildingFromFinanceSystem:this.shortNameOfBuildingFromFinanceSystem,
			configureUser:this.configureUser,
			configureTime:this.configureTime,
		}
	}
	function tgpf_FundAccountInfoDel()
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
	function tgpf_FundAccountInfoDelOne(tgpf_FundAccountInfoId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[tgpf_FundAccountInfoId],
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
		listVue.isAllSelected = (listVue.tgpf_FundAccountInfoList.length > 0)
		&&	(listVue.selectItem.length == listVue.tgpf_FundAccountInfoList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgpf_FundAccountInfoList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgpf_FundAccountInfoList.forEach(function(item) {
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
				listVue.tgpf_FundAccountInfoList=jsonObj.tgpf_FundAccountInfoList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount=jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tgpf_FundAccountInfoListDiv').scrollIntoView();
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
	function showAjaxModal(tgpf_FundAccountInfoModel)
	{
		//tgpf_FundAccountInfoModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'tgpf_FundAccountInfo', tgpf_FundAccountInfoModel);
		//updateVue.$set("tgpf_FundAccountInfo", tgpf_FundAccountInfoModel);
		
		updateVue.userStartId = tgpf_FundAccountInfoModel.userStartId;
		updateVue.userRecordId = tgpf_FundAccountInfoModel.userRecordId;
		updateVue.projectId = tgpf_FundAccountInfoModel.projectId;
		updateVue.buildingId = tgpf_FundAccountInfoModel.buildingId;
	    updateVue.theNameOfCompany = tgpf_FundAccountInfoModel.theNameOfCompany;
	    updateVue.fullNameOfCompanyFromFinanceSystem = tgpf_FundAccountInfoModel.fullNameOfCompanyFromFinanceSystem;
		updateVue.theNameOfProject = tgpf_FundAccountInfoModel.theNameOfProject;
		updateVue.fullNameOfProjectFromFinanceSystem = tgpf_FundAccountInfoModel.fullNameOfProjectFromFinanceSystem;
		updateVue.eCodeFromConstruction = tgpf_FundAccountInfoModel.eCodeFromConstruction;
		updateVue.fullNameOfBuildingFromFinanceSystem = tgpf_FundAccountInfoModel.fullNameOfBuildingFromFinanceSystem;
		updateVue.shortNameOfBuildingFromFinanceSystem = tgpf_FundAccountInfoModel.shortNameOfBuildingFromFinanceSystem;
		updateVue.configureUser = tgpf_FundAccountInfoModel.configureUser;
		updateVue.configureTime = tgpf_FundAccountInfoModel.configureTime;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateTgpf_FundAccountInfo()
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
	function addTgpf_FundAccountInfo()
	{
		new ServerInterface(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				$(baseInfo.addDivId).modal('hide');
				addVue.userStartId = null;
				addVue.userRecordId = null;
				addVue.projectId = null;
				addVue.buildingId = null;
				addVue.theNameOfCompany = null;
				addVue.fullNameOfCompanyFromFinanceSystem = null;
				addVue.theNameOfProject = null;
				addVue.fullNameOfProjectFromFinanceSystem = null;
				addVue.eCodeFromConstruction = null;
				addVue.fullNameOfBuildingFromFinanceSystem = null;
				addVue.shortNameOfBuildingFromFinanceSystem = null;
				addVue.configureUser = null;
				addVue.configureTime = null;
				refresh();
			}
		});
	}
	
	function listItemSelectHandle(val) {
		/*generalListItemSelectHandle(listVue,list)*/
		console.log(val);
		// 获取选中需要修改的数据的tableId
		var length = val.length;
		listVue.selNum = length;
		if(length > 0) {
			for(var i = 0; i < length; i++) {
				listVue.idArr.push(val[i].tableId);
			}
			console.log("idArr:" + listVue.idArr);
			// 修改id获取
			listVue.pkId = val[0].tableId;
			console.log("pkId:" + listVue.pkId);
		}
 	}
	
	// 修改操作----------信息对照详情修改
	function handleFundAccountInfoEdit(tableId) {
		console.log("pkId:" + listVue.pkId);
		enterNextTab(listVue.pkId, '财务凭证信息对照修改', 'tgpf/Tgpf_FundAccountInfoEdit.shtml',listVue.pkId+"200403");
		/*$("#tabContainer").data("tabs").addTab({
			id: listVue.pkId,
			text: '财务凭证信息对照修改',
			closeable: true,
			url: 'tgpf/Tgpf_FundAccountInfoDetail.shtml'
		});*/
	}
	
	function handleFundAccountInfoSearch() {
		$(baseInfo.addModalId).modal('show', {
		    backdrop :'static'
	    });
		new ServerInterface(baseInfo.addSearchInterface).execute(listVue.getSearchForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				$(baseInfo.errorModel).modal('show', {
				    backdrop :'static'
			    });
				/*noty({"text":jsonObj.info,"type":"error","timeout":2000});*/
			}
			else
			{
				setTimeout(function(){ $(baseInfo.addModalId).modal('hide'); }, 1000);
				//generalSuccessModal();
				refresh();
			}
		});
	}
	function errClose()
	{
		$(baseInfo.errorModel).modal('hide');
	}
	
	function succClose()
	{
		$(baseInfo.successModel).modal('hide');
	}
	
	function handleReset()
	{
		listVue.keyword = "";
	}
  //根据按钮权限，动态控制，该页面的所有“可控按钮”的显示与否
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}
	function initData()
	{
		initButtonList();
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tgpf_FundAccountInfoListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"errorModel":"#errorF",
	"successModel":"#successF",
	"addModalId" : "#addFundAccountModal",
	"listInterface":"../Tgpf_FundAccountInfoList",
	"addInterface":"../Tgpf_FundAccountInfoAdd",
	"deleteInterface":"../Tgpf_FundAccountInfoDelete",
	"updateInterface":"../Tgpf_FundAccountInfoUpdate",
	"addSearchInterface":"../Tgpf_FundAccountInfoGetInfo"
});
