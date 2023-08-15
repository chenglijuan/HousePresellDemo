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
			tgxy_BuyerInfoList:[]
		},
		methods : {
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			tgxy_BuyerInfoDelOne : tgxy_BuyerInfoDelOne,
			tgxy_BuyerInfoDel : tgxy_BuyerInfoDel,
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
			buyerName:null,
			buyerType:null,
			certificateType:null,
			eCodeOfcertificate:null,
			contactPhone:null,
			contactAdress:null,
			agentName:null,
			agentCertType:null,
			agentCertNumber:null,
			agentPhone:null,
			agentAddress:null,
			contractno:null,
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateTgxy_BuyerInfo
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
			buyerName:null,
			buyerType:null,
			certificateType:null,
			eCodeOfcertificate:null,
			contactPhone:null,
			contactAdress:null,
			agentName:null,
			agentCertType:null,
			agentCertNumber:null,
			agentPhone:null,
			agentAddress:null,
			contractno:null,
		},
		methods : {
			getAddForm : getAddForm,
			add : addTgxy_BuyerInfo
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
			buyerName:this.buyerName,
			buyerType:this.buyerType,
			certificateType:this.certificateType,
			eCodeOfcertificate:this.eCodeOfcertificate,
			contactPhone:this.contactPhone,
			contactAdress:this.contactAdress,
			agentName:this.agentName,
			agentCertType:this.agentCertType,
			agentCertNumber:this.agentCertNumber,
			agentPhone:this.agentPhone,
			agentAddress:this.agentAddress,
			contractno:this.contractno,
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
			buyerName:this.buyerName,
			buyerType:this.buyerType,
			certificateType:this.certificateType,
			eCodeOfcertificate:this.eCodeOfcertificate,
			contactPhone:this.contactPhone,
			contactAdress:this.contactAdress,
			agentName:this.agentName,
			agentCertType:this.agentCertType,
			agentCertNumber:this.agentCertNumber,
			agentPhone:this.agentPhone,
			agentAddress:this.agentAddress,
			contractno:this.contractno,
		}
	}
	function tgxy_BuyerInfoDel()
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
	function tgxy_BuyerInfoDelOne(tgxy_BuyerInfoId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[tgxy_BuyerInfoId],
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
		listVue.isAllSelected = (listVue.tgxy_BuyerInfoList.length > 0)
		&&	(listVue.selectItem.length == listVue.tgxy_BuyerInfoList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgxy_BuyerInfoList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgxy_BuyerInfoList.forEach(function(item) {
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
				listVue.tgxy_BuyerInfoList=jsonObj.tgxy_BuyerInfoList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tgxy_BuyerInfoListDiv').scrollIntoView();
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
	function showAjaxModal(tgxy_BuyerInfoModel)
	{
		//tgxy_BuyerInfoModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'tgxy_BuyerInfo', tgxy_BuyerInfoModel);
		//updateVue.$set("tgxy_BuyerInfo", tgxy_BuyerInfoModel);
		
		updateVue.theState = tgxy_BuyerInfoModel.theState;
		updateVue.busiState = tgxy_BuyerInfoModel.busiState;
		updateVue.eCode = tgxy_BuyerInfoModel.eCode;
		updateVue.userStartId = tgxy_BuyerInfoModel.userStartId;
		updateVue.createTimeStamp = tgxy_BuyerInfoModel.createTimeStamp;
		updateVue.lastUpdateTimeStamp = tgxy_BuyerInfoModel.lastUpdateTimeStamp;
		updateVue.userRecordId = tgxy_BuyerInfoModel.userRecordId;
		updateVue.recordTimeStamp = tgxy_BuyerInfoModel.recordTimeStamp;
		updateVue.buyerName = tgxy_BuyerInfoModel.buyerName;
		updateVue.buyerType = tgxy_BuyerInfoModel.buyerType;
		updateVue.certificateType = tgxy_BuyerInfoModel.certificateType;
		updateVue.eCodeOfcertificate = tgxy_BuyerInfoModel.eCodeOfcertificate;
		updateVue.contactPhone = tgxy_BuyerInfoModel.contactPhone;
		updateVue.contactAdress = tgxy_BuyerInfoModel.contactAdress;
		updateVue.agentName = tgxy_BuyerInfoModel.agentName;
		updateVue.agentCertType = tgxy_BuyerInfoModel.agentCertType;
		updateVue.agentCertNumber = tgxy_BuyerInfoModel.agentCertNumber;
		updateVue.agentPhone = tgxy_BuyerInfoModel.agentPhone;
		updateVue.agentAddress = tgxy_BuyerInfoModel.agentAddress;
		updateVue.contractno = tgxy_BuyerInfoModel.contractno;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateTgxy_BuyerInfo()
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
	function addTgxy_BuyerInfo()
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
				addVue.buyerName = null;
				addVue.buyerType = null;
				addVue.certificateType = null;
				addVue.eCodeOfcertificate = null;
				addVue.contactPhone = null;
				addVue.contactAdress = null;
				addVue.agentName = null;
				addVue.agentCertType = null;
				addVue.agentCertNumber = null;
				addVue.agentPhone = null;
				addVue.agentAddress = null;
				addVue.contractno = null;
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
	"listDivId":"#tgxy_BuyerInfoListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Tgxy_BuyerInfoList",
	"addInterface":"../Tgxy_BuyerInfoAdd",
	"deleteInterface":"../Tgxy_BuyerInfoDelete",
	"updateInterface":"../Tgxy_BuyerInfoUpdate"
});
