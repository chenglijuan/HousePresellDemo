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
			theStateOfTripleAgreement:"",
			selectItem : [],
			isAllSelected : false,
			theState:0,//正常为0，删除为1
				userStartId:null,
			userStartList:[],
			userRecordId:null,
			userRecordList:[],
			buildingInfoId:null,
			buildingInfoList:[],
			unitInfoId:null,
			unitInfoList:[],
			tgxy_TripleAgreementList:[],
			errtips:'',
			flag: false,
			flag1: false,
			withdrawShow : false,//撤回
			theStateOfTripleAgreementList : [
				{tableId:"0",theName:"未打印"},
				{tableId:"1",theName:"已打印未上传"},
				{tableId:"2",theName:"已上传"},
				{tableId:"3",theName:"已备案"},
				{tableId:"4",theName:"备案退回"},
			],
			projectId : "", //项目名称
			companyId : "", //开发企业
			emmp_companyNameList : [], //页面加载显示开发企业
			empj_ProjectInfoList : [], //显示项目名称
			dateRange : "",
			startDate : "",
			endDate : "",
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
			tgxy_TripleAgreementDelOne : tgxy_TripleAgreementDelOne,
			tgxy_TripleAgreementDel : tgxy_TripleAgreementDel, //删除
			tripleAgreementEditHandle:tripleAgreementEditHandle,//修改
			tripleAgreementAddHandle:tripleAgreementAddHandle,//增加
			withdrawSubmitModel : withdrawSubmitModel,//撤回提示
			tgxy_TripleAgreementWithdraw : tgxy_TripleAgreementWithdraw,//撤回请求
			checkCheckBox:checkCheckBox,
			search:search,
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
			exportExcel: exportExcel,
			errClose:errClose,
			succClose: succClose,
			showDelModal : showDelModal,
			tripleAgreementHandleDetail:tripleAgreementHandleDetail,
			changeTheStateOfTripleAgreement : function(data){
				this.theStateOfTripleAgreement = data.tableId;
			},
			theStateOfTripleAgreementEmpty : function(){
				this.theStateOfTripleAgreement = null;
			},
			loadCompanyName : loadCompanyNameFun, //页面加载显示开发企业方法
			changeCompanyHandle : changeCompanyHandleFun, //改变企业名称的方法
			getCompanyId: function(data) {
				listVue.companyId = data.tableId;
				listVue.projectId = null;
				listVue.changeCompanyHandle();
			},
			emptyCompanyId : function(){
				listVue.companyId = null;
				listVue.empj_ProjectInfoList = [];
			},
			getProjectId: function(data) {
				listVue.projectId = data.tableId;
			},
			emptyProjectId : function(){
				listVue.projectId = null;
			},
			//加载区域
			loadRegionName : loadRegionName,
			//区域选择
			getCityRegionId: function(data) {
				listVue.cityRegionId = data.tableId;
				
				var model ={
						interfaceVersion:this.interfaceVersion,
						cityRegionId: listVue.cityRegionId,
				}
				new ServerInterface(baseInfo.loadDetailInterface).execute(model, function(jsonObj){
					if (jsonObj.result != "success") {
					} else {
						listVue.empj_ProjectInfoList =jsonObj.empj_ProjectInfoList;
					}
				});
				
			},
			emptyCityRegionId : function(data){
				listVue.cityRegionId = null;
				
				listVue.empj_ProjectInfoList = [];
			},
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue,
			'vue-listselect' : VueListSelect,
		},
		watch:{
//			pageNumber : refresh,
			selectItem : selectedItemChanged,
		}
	});
	
	//撤回提示框
	function withdrawSubmitModel(){
		if(listVue.selectItem.length == 0){
			noty({"text":"请至少选择一项进行撤回","type":"error","timeout":2000});
		}else{
			$(baseInfo.withdrawDivId).modal("show",{
				backdrop :'static'
			});
		}	
	}
	//撤回操作
	function tgxy_TripleAgreementWithdraw()
	{
		//隐藏提示框
		$(baseInfo.withdrawDivId).modal("hide");
		console.log("撤回点击");
		var model = 
		{
				interfaceVersion : listVue.interfaceVersion,
				tableId : listVue.selectItem[0].tableId,
				
		};
		
		new ServerInterface(baseInfo.withdrawInterface).execute(model, function(jsonObj){
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
		
	}
	
	//--------------加载区域
	function loadRegionName(){
		
		var model = 
		{
				interfaceVersion : listVue.interfaceVersion,
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
	//列表操作-----------------------页面加载显示开发企业
	function loadCompanyNameFun() {
		loadCompanyName(listVue,baseInfo.companyNameInterface,function(jsonObj){
			listVue.emmp_companyNameList = jsonObj.emmp_CompanyInfoList;
		},listVue.errMsg,baseInfo.edModelDivId);
	}
	
	//列表操作--------------------改变开发企业的方法
	function changeCompanyHandleFun() {
		changeCompanyHandle(listVue,baseInfo.changeCompanyInterface,function(jsonObj){
			listVue.empj_ProjectInfoList = jsonObj.empj_ProjectInfoList;
		},listVue.errMsg,baseInfo.edModelDivId)
	}
	
	function resetInfo() {
		listVue.theStateOfTripleAgreement = "";
		listVue.companyId = "";
		listVue.projectId = "";
		listVue.dateRange = "";
		listVue.keyword = "";
		listVue.startDate = "",
		listVue.endDate = "",
		refresh();
	}
	
	//跳转新增三方协议方法
	function tripleAgreementAddHandle() {
		enterNewTab('', '新增贷款托管三方协议', 'tgxy/Tgxy_TripleAgreementAdd.shtml');
		/*$("#tabContainer").data("tabs").addTab({
			id: 'tgxy_TripleAgreementAdd', 
			text: '新增三方协议信息', 
			closeable: true, 
			url: 'tgxy/Tgxy_TripleAgreementAdd.shtml'
		});*/
	}
	
	//跳转新增三方协议详情信息方法
	function tripleAgreementHandleDetail(tableId) {
		enterNextTab(tableId, '贷款托管三方协议详情', 'tgxy/Tgxy_TripleAgreementDetail.shtml',tableId+"06110301");
		/*$("#tabContainer").data("tabs").addTab({
			id: tableId , 
			text: '三方协议详情信息', 
			closeable: true, 
			url: 'tgxy/Tgxy_TripleAgreementDetail.shtml'
		});*/
	}
	
	//跳转修改
	function tripleAgreementEditHandle(){
		enterNextTab(listVue.selectItem[0].tableId, '修改贷款托管三方协议', 'tgxy/Tgxy_TripleAgreementEdit.shtml',listVue.selectItem[0].tableId+"06110301");
		/*$("#tabContainer").data("tabs").addTab({
			id: listVue.selectItem[0].tableId ,
			text: '修改三方协议信息', 
			closeable: true, 
			url: 'tgxy/Tgxy_TripleAgreementEdit.shtml'
		});*/
	}
	// 导出--------数据导出为Excel
	function exportExcel() {
		flag = true;
		// 获取需要导出的table的dom节点
		var wb = XLSX.utils.table_to_book(document.querySelector('#TripleAgreementListTab'));
		// 写入列表数据并保存
		if(flag){
		    var wbout = XLSX.write(wb, { bookType: 'xlsx', bookSST: true, type: 'array' });
			flag = false;
		}
		console.log(wbout);
		
		try {
			// 导出
			saveAs(new Blob([wbout], { type: 'application/octet-stream' }), '三方协议.xlsx');
		} catch(e) {
			if(typeof console !== 'undefined') console.log(e, wbout)
		}
		return wbout
	}
	//------------------------方法定义-开始------------------//
	 
	//列表操作--------------获取"搜索列表"表单参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			pageNumber:this.pageNumber,
			countPerPage:this.countPerPage,
			//totalPage:this.totalPage,
			keyword:this.keyword,
			theStateOfTripleAgreement:this.theStateOfTripleAgreement,//协议状态
			developCompanyId : this.companyId,
		    projectId : this.projectId,
		    startDate : this.startDate,
			endDate : this.endDate,
			cityRegionId : listVue.cityRegionId,//区域Id
			companyId : this.companyId,
			
		}
	}
	//列表操作--------------获取“删除资源”表单参数
	function checkCheckBox(tableId){
		listVue.selectItem  = tableId;
		if(tableId.length != 0) {
			/*if(listVue.selectItem.length == 1 && tableId[0].approvalState == '待提交') {
                listVue.flag = true;
			}else {
				listVue.flag = false;
			}
			*/
			listVue.flag = true;
	        for (var index = 0; index < listVue.selectItem.length; index++) {
	            var itemModel = listVue.selectItem[index];
	            if (itemModel.approvalState == "已完结" || itemModel.approvalState == "审核中")
	            {
	            	listVue.flag = false;
	                break;
	            }
	        }
			
			
			
			if(listVue.selectItem.length == 1 && tableId[0].approvalState == '待提交') {
                listVue.flag1 = true;
			}else {
				listVue.flag1 = false;
			}
			if(listVue.selectItem.length == 1 && tableId[0].approvalState == '已完结')
			{
				listVue.withdrawShow = true;
			}
			else
			{
				listVue.withdrawShow = false;
			}
			
		}else {
			    listVue.flag = false;
			    listVue.flag1 = false;
			    listVue.withdrawShow = false;
		}
	}
	function getDeleteForm()
	{	
		this.deleteArr = [];
		listVue.selectItem.forEach(function(item){
			listVue.deleteArr.push(item.tableId);
		})
		console.log(this.deleteArr)
		return{
			interfaceVersion:this.interfaceVersion,
			idArr:this.deleteArr,
			
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
	function tgxy_TripleAgreementDel()
	{
		new ServerInterface(baseInfo.batchDeleteInterface).execute(listVue.getDeleteForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
    			generalErrorModal(jsonObj)
			}
			else
			{
				$(baseInfo.deleteDivId).modal("hide");
				listVue.selectItem = [];
				refresh();
			}
		});
	}
	function tgxy_TripleAgreementDelOne(tgxy_TripleAgreementId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[tgxy_TripleAgreementId],
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
		listVue.isAllSelected = (listVue.tgxy_TripleAgreementList.length > 0)
		&&	(listVue.selectItem.length == listVue.tgxy_TripleAgreementList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgxy_TripleAgreementList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgxy_TripleAgreementList.forEach(function(item) {
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
				generalErrorModal(jsonObj);
			}
			else
			{
				listVue.tgxy_TripleAgreementList=jsonObj.tgxy_TripleAgreementList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tgxy_TripleAgreementListDiv').scrollIntoView();
			}
		});
	}
	
	//列表操作------------搜索
	function search()
	{
		listVue.pageNumber = 1;
		refresh();
	}

	
	function indexMethod(index) {
		return generalIndexMethod(index, listVue)
	}

	function listItemSelectHandle(list) {
		generalListItemSelectHandle(listVue,list)
 	}
	function errClose()
	{
		$(baseInfo.errorModel).modal('hide');
	}
	
	function succClose()
	{
		$(baseInfo.successModel).modal('hide');
	}
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}
	function initData()
	{
		initButtonList();
		listVue.loadCompanyName();
		listVue.loadRegionName();
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.initData();
	listVue.refresh();
	laydate.render({
	  elem: '#date0611030101',
	  range: '~',
	  done: function(value, date, endDate){
		   listVue.dateRange = value;
		   var arr = value.split("~");
		   listVue.startDate = arr[0].trim();
		   listVue.endDate = arr[1].trim();
	  }
	});
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tgxy_TripleAgreementListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"deleteDivId":"#deleteTripleAggreementlist",
	"withdrawDivId":"#withdrawShowTripleAggreementlist",//撤回提示
	"errorModel":"#errorTripleAggreementlist",
	"successModel":"#successTripleAggreementlist",
	"listInterface":"../Tgxy_TripleAgreementList",
	"batchDeleteInterface":"../Tgxy_TripleAgreementBatchDelete",
	"companyNameInterface" : "../Emmp_CompanyInfoList", //显示开发企业名称接口
	"changeCompanyInterface" : "../Empj_ProjectInfoList", //改变企业名称接口
	"regionInterface" : "../Sm_CityRegionInfoList",
	"loadDetailInterface":"../Empj_ProjectInfoList",
	"withdrawInterface" : "../Tgxy_TripleAgreementWithdraw",//三方协议撤回
});
