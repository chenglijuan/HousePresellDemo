
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
			contractApplicationDate:'',
			selectItem : [],
			isAllSelected : false,
			theState:0,//正常为0，删除为1
			userStartId:null,
			userStartList:[],
			userRecordId:null,
			userRecordList:[],
			developCompanyId:null,
			developCompanyList:[],
			cityRegionId:null,
			cityRegionList:[],
			projectId:null,
			projectList:[],
			tgxy_EscrowAgreementList:[],
			deleteArr : [],
			errEscrowAdd:"",
			flag: false,
			flag1: false,
			startDate : "",
			endDate : "",
			approvalStateSearch : "",//审批状态
			approvalStateSearchList : [//审批状态List
            	{tableId:"待提交",theName:"待提交"},
            	{tableId:"审核中",theName:"审核中"},
            	{tableId:"已完结",theName:"已完结"},
            ],
            cityRegionId : "",//区域id
			regionList : [],//区域list
		},
		methods : {
			resetInfo: resetInfo,
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			listItemSelectHandle: listItemSelectHandle,
			tgxy_EscrowAgreementDelOne : tgxy_EscrowAgreementDelOne,
			tgxy_EscrowAgreementDel : tgxy_EscrowAgreementDel,
			search:search, //搜索方法
			EscrowAgreementAddHandle:EscrowAgreementAddHandle,//新增
			EscrowAgreementEditHandle:EscrowAgreementEditHandle,//修改
			EscrowAgreementDetailHandle:EscrowAgreementDetailHandle,//详情信息
			checkCheckBox : checkCheckBox,//列表选中
			checkAllClicked : checkAllClicked,
			changePageNumber: function (data) {
				if (listVue.pageNumber != data) {
					listVue.pageNumber = data;
					listVue.refresh();
				}
			},
			changeCountPerPage : function (data) {
				if (listVue.countPerPage != data) {
					listVue.countPerPage = data;
					listVue.refresh();
				}
			},
			changeApprovalStateSearch : function(data){
            	listVue.approvalStateSearch = data.tableId;
            },
            EmptyApprovalStateSearch : function(){
            	listVue.approvalStateSearch = null;
            },
			exportExcel: exportExcel,
			errClose:errClose,
			succClose: succClose,
			showDelModal : showDelModal,
			
			//区域
			loadRegionName : loadRegionName,
			getCityRegionId: function(data) {
				listVue.cityRegionId = data.tableId;
			},
			emptyCityRegionId : function(data){
				listVue.cityRegionId = null;
			},
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue,
			'vue-listselect': VueListSelect,
		},
		watch:{
//			pageNumber : refresh,
			selectItem : selectedItemChanged,
		}
	});

	//------------------------方法定义-开始------------------//
	 
	function resetInfo() {
		listVue.keyword = "";
		listVue.contractApplicationDate = "";
		listVue.startDate = "";
		listVue.endDate = "";
		listVue.approvalStateSearch = "";
		listVue.cityRegionId = "";
		refresh();
	}
	
	//列表操作--------------获取"搜索列表"表单参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			pageNumber:this.pageNumber,
			countPerPage:this.countPerPage,
			keyword:this.keyword,
			startDate: this.startDate,
			endDate : this.endDate,
			approvalState:this.approvalStateSearch,
			cityRegionId : this.cityRegionId
		}
	}
	//列表操作---获取选中id
	function checkCheckBox(tableId){
		listVue.selectItem  = tableId;
		if(tableId.length != 0) {
			if(listVue.selectItem.length == 1 && tableId[0].businessProcessState == "1" && tableId[0].agreementState == "0") {
                listVue.flag = true;
			}else {
				listVue.flag = false;
			}
			if(listVue.selectItem.length >= 1 && tableId[0].businessProcessState == "1" && tableId[0].agreementState == "0") {
                listVue.flag1 = true;
			}else {
				listVue.flag1 = false;
			}
		}else {
			    listVue.flag = false;
			    listVue.flag1 = false;
		}
	}
	//列表操作--------------获取“删除资源”表单参数
	function getDeleteForm()
	{
		this.deleteArr = [];
		listVue.selectItem.forEach(function(item){
			listVue.deleteArr.push(item.tableId);
		})
		return{
			interfaceVersion:this.interfaceVersion,
			idArr:this.deleteArr
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
			escrowCompany:this.escrowCompany,
			agreementVersion:this.agreementVersion,
			eCodeOfAgreement:this.eCodeOfAgreement,
			contractApplicationDate:this.contractApplicationDate,
			developCompanyId:this.developCompanyId,
			eCodeOfDevelopCompany:this.eCodeOfDevelopCompany,
			theNameOfDevelopCompany:this.theNameOfDevelopCompany,
			cityRegionId:this.cityRegionId,
			projectId:this.projectId,
			theNameOfProject:this.theNameOfProject,
			buildingInfoList:this.buildingInfoList,
			OtherAgreedMatters:this.OtherAgreedMatters,
			disputeResolution:this.disputeResolution,
			businessProcessState:this.businessProcessState,
			agreementState:this.agreementState,
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
			escrowCompany:this.escrowCompany,
			agreementVersion:this.agreementVersion,
			eCodeOfAgreement:this.eCodeOfAgreement,
			contractApplicationDate:this.contractApplicationDate,
			developCompanyId:this.developCompanyId,
			eCodeOfDevelopCompany:this.eCodeOfDevelopCompany,
			theNameOfDevelopCompany:this.theNameOfDevelopCompany,
			cityRegionId:this.cityRegionId,
			projectId:this.projectId,
			theNameOfProject:this.theNameOfProject,
			buildingInfoList:this.buildingInfoList,
			OtherAgreedMatters:this.OtherAgreedMatters,
			disputeResolution:this.disputeResolution,
			businessProcessState:this.businessProcessState,
			agreementState:this.agreementState,
		}
	}
		//显示删除提示框
	function showDelModal(){
		if(listVue.getDeleteForm().idArr.length == "0"){

			noty({"text":"请至少选择一项进行删除","type":"error","timeout":2000});
		}else{
			$(baseInfo.deleteDivId).modal("show",{
				backdrop :'static'
			});
		}	
	}
//	删除的方法
	function tgxy_EscrowAgreementDel()
	{
		$(baseInfo.deleteDivId).modal('hide');
		new ServerInterface(baseInfo.batchDeleteInterface).execute(listVue.getDeleteForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				$(baseInfo.successModel).modal('show', {
					backdrop :'static'
				 });
				listVue.selectItem = [];
				refresh();
			}
		});
	}
	function tgxy_EscrowAgreementDelOne(tgxy_EscrowAgreementId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[tgxy_EscrowAgreementId],
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
		listVue.isAllSelected = (listVue.tgxy_EscrowAgreementList.length > 0)
		&&	(listVue.selectItem.length == listVue.tgxy_EscrowAgreementList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgxy_EscrowAgreementList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgxy_EscrowAgreementList.forEach(function(item) {
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
//				noty({"text":jsonObj.info,"type":"error","timeout":2000});
				listVue.errEscrowAdd = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
					backdrop :'static'
				 });
			}
			else
			{
				listVue.tgxy_EscrowAgreementList=jsonObj.tgxy_EscrowAgreementList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tgxy_EscrowAgreementListDiv').scrollIntoView();
			}
		});
	}
	
	//列表操作------------搜索
	function search()
	{
		listVue.contractApplicationDate = document.getElementById("dete0611020101").value;
		listVue.pageNumber = 1;
		refresh();
	}
	
	function indexMethod(index) {
		return generalIndexMethod(index, listVue)
	}

	function listItemSelectHandle(list) {
		generalListItemSelectHandle(listVue,list)
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
	function errClose()
	{
		$(baseInfo.errorModel).modal('hide');
	}
	
	function succClose()
	{
		$(baseInfo.successModel).modal('hide');
	}
	//------------------------方法定义-结束------------------//
	//列表操作-----------------页面加载显示区域
	function loadRegionName(){
		var model = {
				interfaceVersion : this.interfaceVersion
		};
		new ServerInterface(baseInfo.regionInterface).execute(model, function(jsonObj){
			if(jsonObj.result != "success")
			{
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				listVue.errMsg = jsonObj.info;
			}
			else
			{
				listVue.regionList=jsonObj.sm_CityRegionInfoList;
			}
		});
	}
	
	//跳转新增合作协议方法
	function EscrowAgreementAddHandle() {
		enterNewTab('', '新增贷款托管合作协议', 'tgxy/Tgxy_EscrowAgreementAdd.shtml');
		/*$("#tabContainer").data("tabs").addTab({
			id: 'Tgxy_EscrowAgreementAdd', 
			text: '新增托管合作协议', 
			closeable: true, 
			url: 'tgxy/Tgxy_EscrowAgreementAdd.shtml'
		});*/
	}
	
	//跳转新增合作协议详情信息方法
	function EscrowAgreementDetailHandle(tableId) {
		enterNextTab(tableId, '贷款托管合作协议详情', 'tgxy/Tgxy_EscrowAgreementDetail.shtml',tableId+"06110201");
		/*$("#tabContainer").data("tabs").addTab({
			id: tableId , 
			text: '托管合作协议详情信息', 
			closeable: true, 
			url: 'tgxy/Tgxy_EscrowAgreementDetail.shtml'
		});*/
	}
	
	//跳转修改
	function EscrowAgreementEditHandle(){
		enterNextTab(listVue.selectItem[0].tableId, '修改贷款托管合作协议', 'tgxy/Tgxy_EscrowAgreementEdit.shtml',listVue.selectItem[0].tableId+"06110201");
		/*$("#tabContainer").data("tabs").addTab({
			id: listVue.selectItem[0].tableId ,
			text: '修改托管合作协议', 
			closeable: true, 
			url: 'tgxy/Tgxy_EscrowAgreementEdit.shtml'
		});*/
	}
	// 导出--------数据导出为Excel
	function exportExcel() {
		flag = true;
		// 获取需要导出的table的dom节点
		var wb = XLSX.utils.table_to_book(document.querySelector('#EscrowAgreementTab'));
		// 写入列表数据并保存
		if(flag){
		    var wbout = XLSX.write(wb, { bookType: 'xlsx', bookSST: true, type: 'array' });
			flag = false;
		}
		
		try {
			// 导出
			saveAs(new Blob([wbout], { type: 'application/octet-stream' }), '托管合作协议签署.xlsx');
		} catch(e) {
			if(typeof console !== 'undefined') console.log(e, wbout)
		}
		return wbout
	}
		function errClose()
	{
		$(baseInfo.errorModel).modal('hide');
	}
	
//	function succClose()
//	{
//		$(baseInfo.successModel).modal('hide');
//	}
	//------------------------数据初始化-开始----------------//
	listVue.loadRegionName();
	listVue.refresh();
	listVue.initData();
	laydate.render({
	  elem: '#dete0611020101',
	  range: '~',
	  done: function(value, date, endDate){
		   listVue.contractApplicationDate = value;
		   var arr = value.split("~");
		   listVue.startDate = arr[0].trim();
		   listVue.endDate = arr[1].trim();
	  }
	});
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tgxy_EscrowAgreementListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"deleteDivId":"#delete", //删除 模态框
	"errorModel":"#errorEscrowAdd",//失败 模态框
	"successModel":"#successEscrowAdd",//成功 模态框
	"listInterface":"../Tgxy_EscrowAgreementList",
	"addInterface":"../Tgxy_EscrowAgreementAdd",
	"batchDeleteInterface":"../Tgxy_EscrowAgreementBatchDelete",
	"updateInterface":"../Tgxy_EscrowAgreementUpdate",
	"regionInterface" : "../Sm_CityRegionInfoList",//区域加载
});
