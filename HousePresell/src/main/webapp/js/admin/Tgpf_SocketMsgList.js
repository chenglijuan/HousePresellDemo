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
			tgpf_SocketMsgList:[]
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			tgpf_SocketMsgDelOne : tgpf_SocketMsgDelOne,
			tgpf_SocketMsgDel : tgpf_SocketMsgDel,
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
			msgLength:null,
			locationCode:null,
			msgBusinessCode:null,
			bankCode:null,
			returnCode:null,
			remark:null,
			msgDirection:null,
			msgStatus:null,
			md5Check:null,
			msgTimeStamp:null,
			eCodeOfTripleAgreement:null,
			eCodeOfPermitRecord:null,
			eCodeOfContractRecord:null,
			msgSerialno:null,
			msgContent:null,
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateTgpf_SocketMsg
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
			msgLength:null,
			locationCode:null,
			msgBusinessCode:null,
			bankCode:null,
			returnCode:null,
			remark:null,
			msgDirection:null,
			msgStatus:null,
			md5Check:null,
			msgTimeStamp:null,
			eCodeOfTripleAgreement:null,
			eCodeOfPermitRecord:null,
			eCodeOfContractRecord:null,
			msgSerialno:null,
			msgContent:null,
		},
		methods : {
			getAddForm : getAddForm,
			add : addTgpf_SocketMsg
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
			msgLength:this.msgLength,
			locationCode:this.locationCode,
			msgBusinessCode:this.msgBusinessCode,
			bankCode:this.bankCode,
			returnCode:this.returnCode,
			remark:this.remark,
			msgDirection:this.msgDirection,
			msgStatus:this.msgStatus,
			md5Check:this.md5Check,
			msgTimeStamp:this.msgTimeStamp,
			eCodeOfTripleAgreement:this.eCodeOfTripleAgreement,
			eCodeOfPermitRecord:this.eCodeOfPermitRecord,
			eCodeOfContractRecord:this.eCodeOfContractRecord,
			msgSerialno:this.msgSerialno,
			msgContent:this.msgContent,
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
			msgLength:this.msgLength,
			locationCode:this.locationCode,
			msgBusinessCode:this.msgBusinessCode,
			bankCode:this.bankCode,
			returnCode:this.returnCode,
			remark:this.remark,
			msgDirection:this.msgDirection,
			msgStatus:this.msgStatus,
			md5Check:this.md5Check,
			msgTimeStamp:this.msgTimeStamp,
			eCodeOfTripleAgreement:this.eCodeOfTripleAgreement,
			eCodeOfPermitRecord:this.eCodeOfPermitRecord,
			eCodeOfContractRecord:this.eCodeOfContractRecord,
			msgSerialno:this.msgSerialno,
			msgContent:this.msgContent,
		}
	}
	function tgpf_SocketMsgDel()
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
	function tgpf_SocketMsgDelOne(tgpf_SocketMsgId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[tgpf_SocketMsgId],
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
		listVue.isAllSelected = (listVue.tgpf_SocketMsgList.length > 0)
		&&	(listVue.selectItem.length == listVue.tgpf_SocketMsgList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgpf_SocketMsgList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgpf_SocketMsgList.forEach(function(item) {
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
				listVue.tgpf_SocketMsgList=jsonObj.tgpf_SocketMsgList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tgpf_SocketMsgListDiv').scrollIntoView();
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
	function showAjaxModal(tgpf_SocketMsgModel)
	{
		//tgpf_SocketMsgModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'tgpf_SocketMsg', tgpf_SocketMsgModel);
		//updateVue.$set("tgpf_SocketMsg", tgpf_SocketMsgModel);
		
		updateVue.theState = tgpf_SocketMsgModel.theState;
		updateVue.busiState = tgpf_SocketMsgModel.busiState;
		updateVue.eCode = tgpf_SocketMsgModel.eCode;
		updateVue.userStartId = tgpf_SocketMsgModel.userStartId;
		updateVue.createTimeStamp = tgpf_SocketMsgModel.createTimeStamp;
		updateVue.lastUpdateTimeStamp = tgpf_SocketMsgModel.lastUpdateTimeStamp;
		updateVue.userRecordId = tgpf_SocketMsgModel.userRecordId;
		updateVue.recordTimeStamp = tgpf_SocketMsgModel.recordTimeStamp;
		updateVue.msgLength = tgpf_SocketMsgModel.msgLength;
		updateVue.locationCode = tgpf_SocketMsgModel.locationCode;
		updateVue.msgBusinessCode = tgpf_SocketMsgModel.msgBusinessCode;
		updateVue.bankCode = tgpf_SocketMsgModel.bankCode;
		updateVue.returnCode = tgpf_SocketMsgModel.returnCode;
		updateVue.remark = tgpf_SocketMsgModel.remark;
		updateVue.msgDirection = tgpf_SocketMsgModel.msgDirection;
		updateVue.msgStatus = tgpf_SocketMsgModel.msgStatus;
		updateVue.md5Check = tgpf_SocketMsgModel.md5Check;
		updateVue.msgTimeStamp = tgpf_SocketMsgModel.msgTimeStamp;
		updateVue.eCodeOfTripleAgreement = tgpf_SocketMsgModel.eCodeOfTripleAgreement;
		updateVue.eCodeOfPermitRecord = tgpf_SocketMsgModel.eCodeOfPermitRecord;
		updateVue.eCodeOfContractRecord = tgpf_SocketMsgModel.eCodeOfContractRecord;
		updateVue.msgSerialno = tgpf_SocketMsgModel.msgSerialno;
		updateVue.msgContent = tgpf_SocketMsgModel.msgContent;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateTgpf_SocketMsg()
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
	function addTgpf_SocketMsg()
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
				addVue.msgLength = null;
				addVue.locationCode = null;
				addVue.msgBusinessCode = null;
				addVue.bankCode = null;
				addVue.returnCode = null;
				addVue.remark = null;
				addVue.msgDirection = null;
				addVue.msgStatus = null;
				addVue.md5Check = null;
				addVue.msgTimeStamp = null;
				addVue.eCodeOfTripleAgreement = null;
				addVue.eCodeOfPermitRecord = null;
				addVue.eCodeOfContractRecord = null;
				addVue.msgSerialno = null;
				addVue.msgContent = null;
				refresh();
			}
		});
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
	"listDivId":"#tgpf_SocketMsgListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Tgpf_SocketMsgList",
	"addInterface":"../Tgpf_SocketMsgAdd",
	"deleteInterface":"../Tgpf_SocketMsgDelete",
	"updateInterface":"../Tgpf_SocketMsgUpdate"
});
