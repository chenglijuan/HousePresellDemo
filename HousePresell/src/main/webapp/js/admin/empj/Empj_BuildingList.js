(function(baseInfo){
	var listVue = new Vue({
		el : baseInfo.listDivId,
		data : {
			interfaceVersion :19000101,
			pageNumber : 1,
			countPerPage : 10,
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
			bankBranchId:null,
			bankBranchList:[],
			buildingList:[],
			tableId:"",
			idArr: [],
		},
		methods : {
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			listItemSelectHandle: listItemSelectHandle,
			emmp_ComAccountDelOne : emmp_ComAccountDelOne,
			emmp_ComAccountDel : emmp_ComAccountDel,
			showAjaxModal:showAjaxModal,
			search:search,
			checkAllClicked : checkAllClicked,
			changePageNumber : function(data){
				listVue.pageNumber = data;
				listVue.refresh()
			},
			changeCountPerPage : function (data) {
				if (listVue.countPerPage != data) {
					listVue.countPerPage = data;
					listVue.refresh()
				}
			},
			reset: reset,
			eidtHandle: eidtHandle,
			openDetailHandle: openDetailHandle
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
			officeAddress:null,
			officePhone:null,
			bankBranchId:null,
			bankBranchList:[],
			theNameOfBank:null,
			bankAccount:null,
			theNameOfBankAccount:null,
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateEmmp_ComAccount
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
			officeAddress:null,
			officePhone:null,
			bankBranchId:null,
			bankBranchList:[],
			theNameOfBank:null,
			bankAccount:null,
			theNameOfBankAccount:null,
		},
		methods : {
			getAddForm : getAddForm,
			add : addEmmp_ComAccount
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
			bankBranchId:this.bankBranchId,
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
			officeAddress:this.officeAddress,
			officePhone:this.officePhone,
			bankBranchId:this.bankBranchId,
			theNameOfBank:this.theNameOfBank,
			bankAccount:this.bankAccount,
			theNameOfBankAccount:this.theNameOfBankAccount,
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
			officeAddress:this.officeAddress,
			officePhone:this.officePhone,
			bankBranchId:this.bankBranchId,
			theNameOfBank:this.theNameOfBank,
			bankAccount:this.bankAccount,
			theNameOfBankAccount:this.theNameOfBankAccount,
		}
	}
	function emmp_ComAccountDel()
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
	function emmp_ComAccountDelOne(emmp_ComAccountId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[emmp_ComAccountId],
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
		listVue.isAllSelected = (listVue.buildingList.length > 0)
		&&	(listVue.selectItem.length == listVue.buildingList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.buildingList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.buildingList.forEach(function(item) {
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
				listVue.buildingList=jsonObj.empj_BuildingInfoList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('buildingDiv').scrollIntoView();
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
	function showAjaxModal(emmp_ComAccountModel)
	{
		//emmp_ComAccountModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'emmp_ComAccount', emmp_ComAccountModel);
		//updateVue.$set("emmp_ComAccount", emmp_ComAccountModel);
		
		updateVue.theState = emmp_ComAccountModel.theState;
		updateVue.busiState = emmp_ComAccountModel.busiState;
		updateVue.eCode = emmp_ComAccountModel.eCode;
		updateVue.userStartId = emmp_ComAccountModel.userStartId;
		updateVue.createTimeStamp = emmp_ComAccountModel.createTimeStamp;
		updateVue.lastUpdateTimeStamp = emmp_ComAccountModel.lastUpdateTimeStamp;
		updateVue.userRecordId = emmp_ComAccountModel.userRecordId;
		updateVue.recordTimeStamp = emmp_ComAccountModel.recordTimeStamp;
		updateVue.officeAddress = emmp_ComAccountModel.officeAddress;
		updateVue.officePhone = emmp_ComAccountModel.officePhone;
		updateVue.bankBranchId = emmp_ComAccountModel.bankBranchId;
		updateVue.theNameOfBank = emmp_ComAccountModel.theNameOfBank;
		updateVue.bankAccount = emmp_ComAccountModel.bankAccount;
		updateVue.theNameOfBankAccount = emmp_ComAccountModel.theNameOfBankAccount;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateEmmp_ComAccount()
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
	function addEmmp_ComAccount()
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
				addVue.officeAddress = null;
				addVue.officePhone = null;
				addVue.bankBranchId = null;
				addVue.theNameOfBank = null;
				addVue.bankAccount = null;
				addVue.theNameOfBankAccount = null;
				refresh();
			}
		});
	}
	
	function indexMethod(index) {
		return generalIndexMethod(index, listVue)
	}

	function listItemSelectHandle(list) {
		generalListItemSelectHandle(listVue,list);
		var length = list.length;
        if(length > 0){
	        for(var i = 0;i < length;i++){
	        	listVue.idArr.push(list[i].tableId);
	        }
	        console.log(listVue.idArr);
	        listVue.tableId = list[0].tableId;
        }
 	}
	
	function reset()
	{
		listVue.keyword = "";
	}
	
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}

	function initData()
	{
		initButtonList();
	}
	
	function eidtHandle()
	{
		console.log(listVue.tableId);
		enterNextTab(listVue.tableId, '单元信息修改', 'empj/Empj_BuildingEdit.shtml',listVue.tableId+"03010204");
	}
	
	function openDetailHandle(tableId)
	{
		
		enterNextTab(tableId, '单元信息详情', 'empj/Empj_BuildingDetail.shtml',tableId+"03010204");
		/*$("#tabContainer").data("tabs").addTab({
			id: tableId,
			text: '单元维护详情',
			closeable: true,
			url: 'buildingDetail.shtml'
		});*/
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#buildingDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Empj_UnitInfoBuildingList",
	"addInterface":"../Emmp_ComAccountAdd",
	"deleteInterface":"../Emmp_ComAccountDelete",
	"updateInterface":"../Emmp_ComAccountUpdate"
});
