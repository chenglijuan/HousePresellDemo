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
			developCompanyId:null,
			developCompanyList:[],
			emmp_DepartmentList:[]
		},
		methods : {
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			listItemSelectHandle: listItemSelectHandle,
			emmp_DepartmentDelOne : emmp_DepartmentDelOne,
			emmp_DepartmentDel : emmp_DepartmentDel,
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
			developCompanyId:null,
			developCompanyList:[],
			eCodeOfDevelopCompany:null,
			theName:null,
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateEmmp_Department
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
			developCompanyId:null,
			developCompanyList:[],
			eCodeOfDevelopCompany:null,
			theName:null,
		},
		methods : {
			getAddForm : getAddForm,
			add : addEmmp_Department
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
			developCompanyId:this.developCompanyId,
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
			developCompanyId:this.developCompanyId,
			eCodeOfDevelopCompany:this.eCodeOfDevelopCompany,
			theName:this.theName,
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
			developCompanyId:this.developCompanyId,
			eCodeOfDevelopCompany:this.eCodeOfDevelopCompany,
			theName:this.theName,
		}
	}
	function emmp_DepartmentDel()
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
	function emmp_DepartmentDelOne(emmp_DepartmentId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[emmp_DepartmentId],
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
		listVue.isAllSelected = (listVue.emmp_DepartmentList.length > 0)
		&&	(listVue.selectItem.length == listVue.emmp_DepartmentList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.emmp_DepartmentList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.emmp_DepartmentList.forEach(function(item) {
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
				listVue.emmp_DepartmentList=jsonObj.emmp_DepartmentList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('emmp_DepartmentListDiv').scrollIntoView();
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
	function showAjaxModal(emmp_DepartmentModel)
	{
		//emmp_DepartmentModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'emmp_Department', emmp_DepartmentModel);
		//updateVue.$set("emmp_Department", emmp_DepartmentModel);
		
		updateVue.theState = emmp_DepartmentModel.theState;
		updateVue.busiState = emmp_DepartmentModel.busiState;
		updateVue.eCode = emmp_DepartmentModel.eCode;
		updateVue.userStartId = emmp_DepartmentModel.userStartId;
		updateVue.createTimeStamp = emmp_DepartmentModel.createTimeStamp;
		updateVue.lastUpdateTimeStamp = emmp_DepartmentModel.lastUpdateTimeStamp;
		updateVue.userRecordId = emmp_DepartmentModel.userRecordId;
		updateVue.recordTimeStamp = emmp_DepartmentModel.recordTimeStamp;
		updateVue.developCompanyId = emmp_DepartmentModel.developCompanyId;
		updateVue.eCodeOfDevelopCompany = emmp_DepartmentModel.eCodeOfDevelopCompany;
		updateVue.theName = emmp_DepartmentModel.theName;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateEmmp_Department()
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
	function addEmmp_Department()
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
				addVue.developCompanyId = null;
				addVue.eCodeOfDevelopCompany = null;
				addVue.theName = null;
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
	"listDivId":"#emmp_DepartmentListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Emmp_DepartmentList",
	"addInterface":"../Emmp_DepartmentAdd",
	"deleteInterface":"../Emmp_DepartmentDelete",
	"updateInterface":"../Emmp_DepartmentUpdate"
});
