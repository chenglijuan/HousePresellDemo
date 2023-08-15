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
				buildingInfoId:null,
			buildingInfoList:[],
			empj_BuildingExtendInfoList:[]
		},
		methods : {
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			listItemSelectHandle: listItemSelectHandle,
			empj_BuildingExtendInfoDelOne : empj_BuildingExtendInfoDelOne,
			empj_BuildingExtendInfoDel : empj_BuildingExtendInfoDel,
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
			buildingInfoId:null,
			buildingInfoList:[],
			presellState:null,
			eCodeOfPresell:null,
			presellDate:null,
			limitState:null,
			escrowState:null,
			landMortgageState:null,
			landMortgagor:null,
			landMortgageAmount:null,
			isSupportPGS:null,
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateEmpj_BuildingExtendInfo
		}
	});
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			theState:null,
			buildingInfoId:null,
			buildingInfoList:[],
			presellState:null,
			eCodeOfPresell:null,
			presellDate:null,
			limitState:null,
			escrowState:null,
			landMortgageState:null,
			landMortgagor:null,
			landMortgageAmount:null,
			isSupportPGS:null,
		},
		methods : {
			getAddForm : getAddForm,
			add : addEmpj_BuildingExtendInfo
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
			buildingInfoId:this.buildingInfoId,
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
			buildingInfoId:this.buildingInfoId,
			presellState:this.presellState,
			eCodeOfPresell:this.eCodeOfPresell,
			presellDate:this.presellDate,
			limitState:this.limitState,
			escrowState:this.escrowState,
			landMortgageState:this.landMortgageState,
			landMortgagor:this.landMortgagor,
			landMortgageAmount:this.landMortgageAmount,
			isSupportPGS:this.isSupportPGS,
		}
	}
	//列表操作--------------获取“更新”表单参数
	function getUpdateForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			theState:this.theState,
			buildingInfoId:this.buildingInfoId,
			presellState:this.presellState,
			eCodeOfPresell:this.eCodeOfPresell,
			presellDate:this.presellDate,
			limitState:this.limitState,
			escrowState:this.escrowState,
			landMortgageState:this.landMortgageState,
			landMortgagor:this.landMortgagor,
			landMortgageAmount:this.landMortgageAmount,
			isSupportPGS:this.isSupportPGS,
		}
	}
	function empj_BuildingExtendInfoDel()
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
	function empj_BuildingExtendInfoDelOne(empj_BuildingExtendInfoId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[empj_BuildingExtendInfoId],
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
		listVue.isAllSelected = (listVue.empj_BuildingExtendInfoList.length > 0)
		&&	(listVue.selectItem.length == listVue.empj_BuildingExtendInfoList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.empj_BuildingExtendInfoList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.empj_BuildingExtendInfoList.forEach(function(item) {
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
				listVue.empj_BuildingExtendInfoList=jsonObj.empj_BuildingExtendInfoList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('empj_BuildingExtendInfoListDiv').scrollIntoView();
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
	function showAjaxModal(empj_BuildingExtendInfoModel)
	{
		//empj_BuildingExtendInfoModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'empj_BuildingExtendInfo', empj_BuildingExtendInfoModel);
		//updateVue.$set("empj_BuildingExtendInfo", empj_BuildingExtendInfoModel);
		
		updateVue.theState = empj_BuildingExtendInfoModel.theState;
		updateVue.buildingInfoId = empj_BuildingExtendInfoModel.buildingInfoId;
		updateVue.presellState = empj_BuildingExtendInfoModel.presellState;
		updateVue.eCodeOfPresell = empj_BuildingExtendInfoModel.eCodeOfPresell;
		updateVue.presellDate = empj_BuildingExtendInfoModel.presellDate;
		updateVue.limitState = empj_BuildingExtendInfoModel.limitState;
		updateVue.escrowState = empj_BuildingExtendInfoModel.escrowState;
		updateVue.landMortgageState = empj_BuildingExtendInfoModel.landMortgageState;
		updateVue.landMortgagor = empj_BuildingExtendInfoModel.landMortgagor;
		updateVue.landMortgageAmount = empj_BuildingExtendInfoModel.landMortgageAmount;
		updateVue.isSupportPGS = empj_BuildingExtendInfoModel.isSupportPGS;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateEmpj_BuildingExtendInfo()
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
	function addEmpj_BuildingExtendInfo()
	{
		new ServerInterface(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				$(baseInfo.addDivId).modal('hide');
				addVue.buildingInfoId = null;
				addVue.presellState = null;
				addVue.eCodeOfPresell = null;
				addVue.presellDate = null;
				addVue.limitState = null;
				addVue.escrowState = null;
				addVue.landMortgageState = null;
				addVue.landMortgagor = null;
				addVue.landMortgageAmount = null;
				addVue.isSupportPGS = null;
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
	"listDivId":"#empj_BuildingExtendInfoListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Empj_BuildingExtendInfoList",
	"addInterface":"../Empj_BuildingExtendInfoAdd",
	"deleteInterface":"../Empj_BuildingExtendInfoDelete",
	"updateInterface":"../Empj_BuildingExtendInfoUpdate"
});
