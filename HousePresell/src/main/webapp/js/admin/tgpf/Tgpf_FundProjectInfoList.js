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
			tgpf_FundProjectInfoList:[],
			propelStatus: '未推送',
			idArr: [],
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			tgpf_FundProjectInfoDelOne : tgpf_FundProjectInfoDelOne,
			tgpf_FundProjectInfoDel : tgpf_FundProjectInfoDel,
			showAjaxModal:showAjaxModal,
			search:search,
			checkAllClicked : checkAllClicked,
			changePageNumber : function(data){
				listVue.pageNumber = data;
			},
			indexMethod:indexMethod,//左侧序号
			handleReset: handleReset,
			handlePropel: handlePropel,
			handleApproDetail: handleApproDetail,
			listItemSelectHandle: listItemSelectHandle,
			getPropelForm: getPropelForm,
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
			payoutAmount:null,
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateTgpf_FundProjectInfo
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
			payoutAmount:null,
		},
		methods : {
			getAddForm : getAddForm,
			add : addTgpf_FundProjectInfo
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
			billTimeStamp:document.getElementById("appropriateSign").value,
			propelStatus:this.propelStatus,
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
			payoutAmount:this.payoutAmount,
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
			payoutAmount:this.payoutAmount,
		}
	}
	function tgpf_FundProjectInfoDel()
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
	function tgpf_FundProjectInfoDelOne(tgpf_FundProjectInfoId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[tgpf_FundProjectInfoId],
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
		listVue.isAllSelected = (listVue.tgpf_FundProjectInfoList.length > 0)
		&&	(listVue.selectItem.length == listVue.tgpf_FundProjectInfoList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgpf_FundProjectInfoList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgpf_FundProjectInfoList.forEach(function(item) {
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
				listVue.tgpf_FundProjectInfoList=jsonObj.tgpf_FundProjectInfoList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tgpf_FundProjectInfoListDiv').scrollIntoView();
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
	function showAjaxModal(tgpf_FundProjectInfoModel)
	{
		//tgpf_FundProjectInfoModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'tgpf_FundProjectInfo', tgpf_FundProjectInfoModel);
		//updateVue.$set("tgpf_FundProjectInfo", tgpf_FundProjectInfoModel);
		
		updateVue.theState = tgpf_FundProjectInfoModel.theState;
		updateVue.busiState = tgpf_FundProjectInfoModel.busiState;
		updateVue.eCode = tgpf_FundProjectInfoModel.eCode;
		updateVue.userStartId = tgpf_FundProjectInfoModel.userStartId;
		updateVue.createTimeStamp = tgpf_FundProjectInfoModel.createTimeStamp;
		updateVue.lastUpdateTimeStamp = tgpf_FundProjectInfoModel.lastUpdateTimeStamp;
		updateVue.userRecordId = tgpf_FundProjectInfoModel.userRecordId;
		updateVue.recordTimeStamp = tgpf_FundProjectInfoModel.recordTimeStamp;
		updateVue.buildingId = tgpf_FundProjectInfoModel.buildingId;
		updateVue.eCodeOfBuilding = tgpf_FundProjectInfoModel.eCodeOfBuilding;
		updateVue.payoutAmount = tgpf_FundProjectInfoModel.payoutAmount;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateTgpf_FundProjectInfo()
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
	function addTgpf_FundProjectInfo()
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
				addVue.payoutAmount = null;
				refresh();
			}
		});
	}
	
	function initData()
	{
		
	}
	function indexMethod(index) {
        return generalIndexMethod(index, listVue)
    }
	// 添加日期控件
	laydate.render({
		elem: '#appropriateSign',
	});
	
	function handleReset()
	{
		this.keyword = "";
		document.getElementById("appropriateSign").value = "";
		this.propelStatus = "";
	}
	
	// 推送
	function handlePropel()
	{
		new ServerInterface(baseInfo.propelInterface).execute(listVue.getPropelForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				//$(baseInfo.updateDivId).modal('hide');
				refresh();
			}
		});
	}
	
	// 拨付明细
	function handleApproDetail()
	{
		new ServerInterface(baseInfo.approDetailInterface).execute(listVue.getPropelForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				//$(baseInfo.updateDivId).modal('hide');
				refresh();
			}
		});
	}
	
	function getPropelForm()
	{
		return {
			interfaceVersion :19000101,
			idArr: listVue.idArr,
		}
	}
	
	function listItemSelectHandle(val) {
		/*generalListItemSelectHandle(val,list);*/
		// 获取选中需要修改的数据的tableId
        var length = val.length;
        if(length > 0){
	        for(var i = 0;i < length;i++){
	        	listVue.idArr.push(val[i].tableId);
	        }
	        console.log(listVue.idArr);
        }
 	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tgpf_FundProjectInfoListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Tgpf_FundProjectInfoList",
	"addInterface":"../Tgpf_FundProjectInfoAdd",
	"deleteInterface":"../Tgpf_FundProjectInfoDelete",
	"updateInterface":"../Tgpf_FundProjectInfoUpdate",
	"propelInterface":"../Tgpf_FundProjectInfoUpdate",
	"approDetailInterface":"../Tgpf_FundProjectInfoUpdate"
});
