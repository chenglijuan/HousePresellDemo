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
			qualificationInformationId:null,
			qualificationInformationList:[],
			emmp_OrgMemberList:[]
		},
		methods : {
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			listItemSelectHandle: listItemSelectHandle,
			emmp_OrgMemberDelOne : emmp_OrgMemberDelOne,
			emmp_OrgMemberDel : emmp_OrgMemberDel,
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
			companyId:null,
			companyList:[],
			eCodeOfDepartment:null,
			theNameOfDepartment:null,
			theType:null,
			theName:null,
			realName:null,
			idType:null,
			idNumber:null,
			phoneNumber:null,
			email:null,
			weixin:null,
			qq:null,
			address:null,
			heardImagePath:null,
			hasQC:null,
			qualificationInformationId:null,
			qualificationInformationList:[],
			remark:null,
			logId:null,
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateEmmp_OrgMember
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
			companyId:null,
			companyList:[],
			eCodeOfDepartment:null,
			theNameOfDepartment:null,
			theType:null,
			theName:null,
			realName:null,
			idType:null,
			idNumber:null,
			phoneNumber:null,
			email:null,
			weixin:null,
			qq:null,
			address:null,
			heardImagePath:null,
			hasQC:null,
			qualificationInformationId:null,
			qualificationInformationList:[],
			remark:null,
			logId:null,
		},
		methods : {
			getAddForm : getAddForm,
			add : addEmmp_OrgMember
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
			qualificationInformationId:this.qualificationInformationId,
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
			companyId:this.companyId,
			eCodeOfDepartment:this.eCodeOfDepartment,
			theNameOfDepartment:this.theNameOfDepartment,
			theType:this.theType,
			theName:this.theName,
			realName:this.realName,
			idType:this.idType,
			idNumber:this.idNumber,
			phoneNumber:this.phoneNumber,
			email:this.email,
			weixin:this.weixin,
			qq:this.qq,
			address:this.address,
			heardImagePath:this.heardImagePath,
			hasQC:this.hasQC,
			qualificationInformationId:this.qualificationInformationId,
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
			companyId:this.companyId,
			eCodeOfDepartment:this.eCodeOfDepartment,
			theNameOfDepartment:this.theNameOfDepartment,
			theType:this.theType,
			theName:this.theName,
			realName:this.realName,
			idType:this.idType,
			idNumber:this.idNumber,
			phoneNumber:this.phoneNumber,
			email:this.email,
			weixin:this.weixin,
			qq:this.qq,
			address:this.address,
			heardImagePath:this.heardImagePath,
			hasQC:this.hasQC,
			qualificationInformationId:this.qualificationInformationId,
			remark:this.remark,
			logId:this.logId,
		}
	}
	function emmp_OrgMemberDel()
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
	function emmp_OrgMemberDelOne(emmp_OrgMemberId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[emmp_OrgMemberId],
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
		listVue.isAllSelected = (listVue.emmp_OrgMemberList.length > 0)
		&&	(listVue.selectItem.length == listVue.emmp_OrgMemberList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.emmp_OrgMemberList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.emmp_OrgMemberList.forEach(function(item) {
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
				listVue.emmp_OrgMemberList=jsonObj.emmp_OrgMemberList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('emmp_OrgMemberListDiv').scrollIntoView();
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
	function showAjaxModal(emmp_OrgMemberModel)
	{
		//emmp_OrgMemberModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'emmp_OrgMember', emmp_OrgMemberModel);
		//updateVue.$set("emmp_OrgMember", emmp_OrgMemberModel);
		
		updateVue.theState = emmp_OrgMemberModel.theState;
		updateVue.busiState = emmp_OrgMemberModel.busiState;
		updateVue.eCode = emmp_OrgMemberModel.eCode;
		updateVue.userStartId = emmp_OrgMemberModel.userStartId;
		updateVue.createTimeStamp = emmp_OrgMemberModel.createTimeStamp;
		updateVue.lastUpdateTimeStamp = emmp_OrgMemberModel.lastUpdateTimeStamp;
		updateVue.userRecordId = emmp_OrgMemberModel.userRecordId;
		updateVue.recordTimeStamp = emmp_OrgMemberModel.recordTimeStamp;
		updateVue.companyId = emmp_OrgMemberModel.companyId;
		updateVue.eCodeOfDepartment = emmp_OrgMemberModel.eCodeOfDepartment;
		updateVue.theNameOfDepartment = emmp_OrgMemberModel.theNameOfDepartment;
		updateVue.theType = emmp_OrgMemberModel.theType;
		updateVue.theName = emmp_OrgMemberModel.theName;
		updateVue.realName = emmp_OrgMemberModel.realName;
		updateVue.idType = emmp_OrgMemberModel.idType;
		updateVue.idNumber = emmp_OrgMemberModel.idNumber;
		updateVue.phoneNumber = emmp_OrgMemberModel.phoneNumber;
		updateVue.email = emmp_OrgMemberModel.email;
		updateVue.weixin = emmp_OrgMemberModel.weixin;
		updateVue.qq = emmp_OrgMemberModel.qq;
		updateVue.address = emmp_OrgMemberModel.address;
		updateVue.heardImagePath = emmp_OrgMemberModel.heardImagePath;
		updateVue.hasQC = emmp_OrgMemberModel.hasQC;
		updateVue.qualificationInformationId = emmp_OrgMemberModel.qualificationInformationId;
		updateVue.remark = emmp_OrgMemberModel.remark;
		updateVue.logId = emmp_OrgMemberModel.logId;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateEmmp_OrgMember()
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
	function addEmmp_OrgMember()
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
				addVue.companyId = null;
				addVue.eCodeOfDepartment = null;
				addVue.theNameOfDepartment = null;
				addVue.theType = null;
				addVue.theName = null;
				addVue.realName = null;
				addVue.idType = null;
				addVue.idNumber = null;
				addVue.phoneNumber = null;
				addVue.email = null;
				addVue.weixin = null;
				addVue.qq = null;
				addVue.address = null;
				addVue.heardImagePath = null;
				addVue.hasQC = null;
				addVue.qualificationInformationId = null;
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
	"listDivId":"#emmp_OrgMemberListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Emmp_OrgMemberList",
	"addInterface":"../Emmp_OrgMemberAdd",
	"deleteInterface":"../Emmp_OrgMemberDelete",
	"updateInterface":"../Emmp_OrgMemberUpdate"
});
