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
			signDate : null,
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
			buildingInfoId:null,
			buildingInfoList:[],
			tgxy_CoopAgreementList:[],
			pkId: "",
			idArr: [],
			selNum:0
		},
		methods : {
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			tgxy_CoopAgreementDelOne : tgxy_CoopAgreementDelOne,
			tgxy_CoopAgreementDel : tgxy_CoopAgreementDel,
			showAjaxModal:showAjaxModal,
			search:search,
			checkAllClicked : checkAllClicked,
			// 合作协议查看详情
			openDetails: openDetails,
			// 新增合作协议
			coopAgreementAddHandle: coopAgreementAddHandle,
			handleSelectionChange: handleSelectionChange,
			coopAgreementEditHandle: coopAgreementEditHandle,
			resetInfo: resetInfo,
			exportExcel: exportExcel,
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
			saClose1: function() {
				$(baseInfo.warnModel1).modal('hide');
				new ServerInterface(baseInfo.deleteInterface).execute(listVue.getDeleteForm(), function(jsonObj){
					if(jsonObj.result != "success")
					{
						generalErrorModal(jsonObj);
					}
					else
					{
						generalSuccessModal();
						listVue.selectItem = [];
						refresh();
					}
				});
			},
			swClose1: function() {
				$(baseInfo.warnModel1).modal('hide');
			}
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

	//------------------------方法定义-开始------------------//
	
	function resetInfo()
	{
		listVue.keyword = "";
		document.getElementById("date0611010301").value = "";
		refresh();
	}
	 
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
			cityRegionId:this.cityRegionId,
			projectId:this.projectId,
			buildingInfoId:this.buildingInfoId,
			
			signDate:document.getElementById('date0611010301').value,
		}
	}
	//列表操作--------------获取“删除资源”表单参数
	function getDeleteForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			idArr:listVue.idArr
		}
	}
	function tgxy_CoopAgreementDel()
	{
		$(baseInfo.warnModel1).modal({
		    backdrop :'static'
	    });
	}
	function tgxy_CoopAgreementDelOne(tgxy_CoopAgreementId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[tgxy_CoopAgreementId],
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
		listVue.isAllSelected = (listVue.tgxy_CoopAgreementList.length > 0)
		&&	(listVue.selectItem.length == listVue.tgxy_CoopAgreementList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgxy_CoopAgreementList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgxy_CoopAgreementList.forEach(function(item) {
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
				listVue.tgxy_CoopAgreementList=jsonObj.tgxy_CoopAgreementList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tgxy_CoopAgreementListDiv').scrollIntoView();
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
	function showAjaxModal(tgxy_CoopAgreementModel)
	{
		//tgxy_CoopAgreementModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'tgxy_CoopAgreement', tgxy_CoopAgreementModel);
		//updateVue.$set("tgxy_CoopAgreement", tgxy_CoopAgreementModel);
		
		updateVue.theState = tgxy_CoopAgreementModel.theState;
		updateVue.busiState = tgxy_CoopAgreementModel.busiState;
		updateVue.eCode = tgxy_CoopAgreementModel.eCode;
		updateVue.theNameOfBank = tgxy_CoopAgreementModel.theNameOfBank,
		updateVue.theNameOfDepositBank = tgxy_CoopAgreementModel.theNameOfDepositBank,
		updateVue.signDate = tgxy_CoopAgreementModel.signDate,
		updateVue.userStartId = tgxy_CoopAgreementModel.userStartId;
		updateVue.createTimeStamp = tgxy_CoopAgreementModel.createTimeStamp;
		updateVue.lastUpdateTimeStamp = tgxy_CoopAgreementModel.lastUpdateTimeStamp;
		updateVue.userRecordId = tgxy_CoopAgreementModel.userRecordId;
		updateVue.recordTimeStamp = tgxy_CoopAgreementModel.recordTimeStamp;
		updateVue.escrowCompany = tgxy_CoopAgreementModel.escrowCompany;
		updateVue.theVersion = tgxy_CoopAgreementModel.theVersion;
		updateVue.developCompanyId = tgxy_CoopAgreementModel.developCompanyId;
		updateVue.theNameOfDevelopCompany = tgxy_CoopAgreementModel.theNameOfDevelopCompany;
		updateVue.cityRegionId = tgxy_CoopAgreementModel.cityRegionId;
		updateVue.projectId = tgxy_CoopAgreementModel.projectId;
		updateVue.theNameOfProject = tgxy_CoopAgreementModel.theNameOfProject;
		updateVue.buildingInfoId = tgxy_CoopAgreementModel.buildingInfoId;
		updateVue.eCodeOfBuilding = tgxy_CoopAgreementModel.eCodeOfBuilding;
		updateVue.eCodeFromConstruction = tgxy_CoopAgreementModel.eCodeFromConstruction;
		updateVue.eCodeFromPublicSecurity = tgxy_CoopAgreementModel.eCodeFromPublicSecurity;
		updateVue.OtherAgreedMatters = tgxy_CoopAgreementModel.OtherAgreedMatters;
		updateVue.disputeResolution = tgxy_CoopAgreementModel.disputeResolution;
		updateVue.busiProcessState = tgxy_CoopAgreementModel.busiProcessState;
		updateVue.agreeState = tgxy_CoopAgreementModel.agreeState;
		updateVue.userUpdate = tgxy_CoopAgreementModel,userUpdate;
		updateVue.lastUpdateTimeStamp = tgxy_CoopAgreementModel.lastUpdateTimeStamp
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	
	function indexMethod(index) {
		return generalIndexMethod(index, listVue)
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
	
	// 查看详情方法封装
	function openDetails(tableId) {
		enterNextTab(tableId, '合作协议详情', 'tgxy/Tgxy_CoopAgreementDetail.shtml',tableId+"06110103");
		/*$("#tabContainer").data("tabs").addTab({
			id: tableId,
			text: '合作协议详情',
			closeable: true,
			url: 'tgxy/Tgxy_CoopAgreementDetail.shtml'
		});*/
	}
	
	// 点击新增按钮，打开合作协议新增页面
	function coopAgreementAddHandle() {
		enterNewTab('', '新增合作协议', 'tgxy/Tgxy_CoopAgreementAdd.shtml');
		/*$("#tabContainer").data("tabs").addTab({
			id: 'Tgxy_CoopAgreementAdd',
			text: '新增合作协议',
			closeable: true,
			url: 'tgxy/Tgxy_CoopAgreementAdd.shtml'
		});*/
	}
	
	function handleSelectionChange(val)
	{
        // 获取选中需要修改的数据的tableId
        var length = val.length;
        listVue.selNum = length;
        if(length > 0){
	        for(var i = 0;i < length;i++){
	        	listVue.idArr.push(val[i].tableId);
	        }
		    // 修改id获取
		    listVue.pkId = val[0].tableId;
        }
	}
	
	// 修改页面跳转
	function coopAgreementEditHandle()
	{
		enterNextTab(listVue.pkId, '修改合作协议', 'tgxy/Tgxy_CoopAgreementEdit.shtml',listVue.tableId+"06110103");
		/*$("#tabContainer").data("tabs").addTab({
			id: listVue.pkId,
			text: '修改合作协议',
			closeable: true,
			url: 'tgxy/Tgxy_CoopAgreementEdit.shtml'
		});*/
	}
	
	// 导出--------数据导出为Excel
	function exportExcel() {
		flag = true;
		// 获取需要导出的table的dom节点
		var wb = XLSX.utils.table_to_book(document.querySelector('#coopAgreementTab'));
		// 写入列表数据并保存
		if(flag){
		    var wbout = XLSX.write(wb, { bookType: 'xlsx', bookSST: true, type: 'array' });
			flag = false;
		}
		
		try {
			// 导出
			saveAs(new Blob([wbout], { type: 'application/octet-stream' }), '合作协议.xlsx');
		} catch(e) {
			if(typeof console !== 'undefined') console.log(e, wbout)
		}
		return wbout
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tgxy_CoopAgreementListDiv",
	"updateDivId":"#updateModel",
	"warnModel1":"#warnModel1",
	"addDivId":"#addModal",
	"listInterface":"../Tgxy_CoopAgreementList",
	"deleteInterface":"../Tgxy_CoopAgreementBatchDelete",
});
