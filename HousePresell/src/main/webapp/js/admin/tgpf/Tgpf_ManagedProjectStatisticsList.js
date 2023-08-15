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
			theState:0,// 正常为0，删除为1
			userStartId:null,
			userStartList:[],
			userRecordId:null,
			userRecordList:[],
			cityRegionId:null,
			tg_AreaList:[],
			companyId:null,
			qs_companyNameList:[],
			projectId:null,
			qs_projectNameList:[],			
			buildingId:null,
			buildingList:[],
			buildingUnitId:null,
			buildingUnitList:[],
			bankId:null,
			bankList:[],			
//			endEscrowAgRecordTime:null,			
			tgpf_ManagedProjectStatisticsList:[],
			remainSign:'',
			recordDateStart : '',
		    recordDateEnd : '',
			companyList:[
			      {
			      	prop:"index",
			      	label:'序号',
			      	width:"150px",
			      },{
				    prop:'companyGroup',  
				    label:'集团',
			      	width:"200px",
				  },{
				    prop:'companyName',
				    label:'开发企业名称',
			      	width:"200px",
					},{
				    prop:'cityRegion',
				    label:'区域',
			      	width:"200px",
					},{
				    prop:'projectName',
				    label:'托管项目',
			      	width:"200px",
				  },{
				    prop:'eCodeFromConstruction',
				    label:'托管楼幢',
			      	width:"100px",
				  },{
				    prop:'deliveryType',
				    label:'交付类型',
			      	width:"100px",
				  },{
				    prop:'upTotalFloorNumber',
				    label:'地上总层数（F）',
			      	width:"150px",
				  },{
				    prop:'escrowArea',
				    label:'托管面积（㎡）',
			      	width:"150px",
				  },{
				    prop:'recordAvgPriceOfBuilding',
				    label:'托管楼幢均价',
			      	width:"150px",
				  },{
				    prop:'orgLimitedAmount',
				    label:'初始受限额度',
			      	width:"150px",
				  },{
				    prop:'currentLimitedAmount',
				    label:'当前受限额度',
			      	width:"150px",
				  },{
				    prop:'currentBuildProgress',
				    label:'当前建设进度',
			      	width:"150px",
				  },{
				    prop:'currentLimitedNote',
				    label:'当前受限节点',
			      	width:"150px",
				  },{
				    prop:'currentEscrowFund',
				    label:'托管余额',
			      	width:"150px",
				  },{
				    prop:'amountOffset',
				    label:'支付保证额度',
//				    label:'抵充额度',
			      	width:"150px",
				  },{
				    prop:'sumFamilyNumber',
				    label:'总户数',
			      	width:"100px",
				  },{
				    prop:'signHouseNum',
				    label:'签约户数',
			      	width:"100px",
				  },{
				    prop:'recordHouseNum',
				    label:'备案户数',
			      	width:"150px",
				  },{
				    prop:'depositHouseNum',
				    label:'托管户数',
			      	width:"150px",
				  },{
				    prop:'isPresell',
				    label:'预售证（有/无）',
			      	width:"150px",
				  }
			],
			tgpf_ManagedProjectStatisticsList1:[],
			dataUpload:{
				"appid":"ossq7y44g",
				"appsecret":"yg2us2a7",
				"project":"HousePresell"
			},
			importUrl:"",
			deleCodes:[],
	        cache : [], // 储蓄一条数据
	        cacheIndex : [], // 位置
	        cacheData  : [],
	        model: {},
		},
		methods : {
			refresh : refresh,
			initData:initData,
			loadAreaList:loadAreaList,// 页面加载区域的方法
			loadCompanyName : loadCompanyNameFun, // 页面加载显示开发企业方法
			changeCompanyHandle : changeCompanyHandleFun, // 改变企业名称的方法
			changeProjectHandle : changeProjectHandleFun, // 改变项目名称的方法
			handleSelectionChange : handleSelectionChange,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			getExcelDataForm : getExcelDataForm,
			tgpf_RemainRightDel : tgpf_RemainRightDel,
			search:search,
			checkAllClicked : checkAllClicked,
			changePageNumber : function(data){
				if (listVue.pageNumber != data) {
					listVue.pageNumber = data;
					listVue.refresh();
				}
			},
			changeCountPerPage:function(data){
				if (listVue.countPerPage != data) {
					listVue.countPerPage = data;
					listVue.refresh();
				}
			},
			changeCityFun:function(data){
				listVue.cityRegionId=data.tableId;
				listVue.changeCompanyHandle();
			},
			getCompanyId: function(data) {
				listVue.companyId = data.tableId;
				listVue.changeCompanyHandle();
			},
			emptyCompanyId : function(data){
				listVue.companyId = null;
				listVue.projectId = null;
				listVue.buildId = null;
				listVue.qs_projectNameList = [];
				listVue.qs_buildingNumberlist = [];
			},
			getProjectId: function(data) {
				listVue.projectId = data.tableId;
				listVue.changeProjectHandle();
			},
			emptyProjectId : function(data){
				listVue.projectId = null;
				listVue.buildId = null;
				listVue.qs_buildingNumberlist = [];
			},
			remaindRightDifferenceExportExcelHandle : remaindRightDifferenceExportExcelHandle,
		    handlePictureCardPreview:function(file) {
		        this.dialogImageUrl = file.url;
		        this.dialogVisible = true;
		    },
		    handleReset: handleReset,
	      combineFun : function(){
				combine(this.cache,this.cacheIndex,this.cacheData,this.companyList,this.tgpf_ManagedProjectStatisticsList)
			} ,
	      objectSpanMethod:function(row) {
		  	  if(row.columnIndex === 1){
		         return checkSpanMethod(listVue.cacheData,row.rowIndex,row.columnIndex);
		      }
		  	  if(row.columnIndex === 2){
		         return checkSpanMethod(listVue.cacheData,row.rowIndex,row.columnIndex);
		      }
		  	  if(row.columnIndex === 3){
		         return checkSpanMethod(listVue.cacheData,row.rowIndex,row.columnIndex);
		      }
		  	  if(row.columnIndex === 4){
			         return checkSpanMethod(listVue.cacheData,row.rowIndex,row.columnIndex);
			      }
		  },
		  tableRowClassName: function(val) {
			  if(val.row.companyGroup == '小计：') {
				  return 'row-bg';
			  }else {
				  return "";
			  }
		  }
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue,
			'vue-select': VueSelect,
			'vue-listselect': VueListSelect,
		},
		watch:{
			// pageNumber : refresh,
// selectItem : selectedItemChanged,
		}
	});
	

	// ------------------------方法定义-开始------------------//
	 
	// 列表操作--------------获取"搜索列表"表单参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			pageNumber:this.pageNumber,
			countPerPage:this.countPerPage,
			totalPage:this.totalPage,
			totalCount : this.totalCount,
			keyword:this.keyword,
			theState:this.theState,
			userStartId:this.userStartId,
			userRecordId:this.userRecordId,
			projectId:this.projectId,
			buildingId:this.buildingId,
			buildingUnitId:this.buildingUnitId,
			bankId:this.bankId,
			cityRegion:this.cityRegion,
			cityRegionId:this.cityRegionId,
			companyId:this.companyId,
			projectId:this.projectId,
//			date:document.getElementById("remainSign").value,
			recordDateStart : this.recordDateStart,
			recordDateEnd :this.recordDateEnd,
		}
	}
	// 列表操作--------------获取“删除资源”表单参数
	function getDeleteForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			idArr:this.selectItem
		}
	}
	
	
	
	// 获取区域列表
	function loadAreaList(){
		var model = {
				interfaceVersion : 19000101,
				exceptZhengTai : true,
			};
		new ServerInterface(baseInfo.loadCityRegionInterface).execute(model, function(jsonObj){
			if (jsonObj.result != "success") {
				$(baseInfo.edModelDivId).modal({
					backdrop : 'static'
				});
				listVue.errMsg = jsonObj.info;
			} else {
				listVue.tg_AreaList = jsonObj.sm_CityRegionInfoList;
			}
		});
	}
	
	// 列表开发企业列表
	function loadCompanyNameFun() {
		var model = {
				interfaceVersion : 19000101,
				exceptZhengTai : true,
			};
		new ServerInterface(baseInfo.companyNameInterface).execute(model, function(jsonObj)
				{
					if(jsonObj.result != "success")
					{
						generalErrorModal(jsonObj);
					}
					else
					{
						listVue.qs_companyNameList = jsonObj.emmp_CompanyInfoList;
					}
				});
	}
	
	// 列表操作--------------------改变开发企业的方法
	function changeCompanyHandleFun() {
		changeCompanyHandle(listVue,baseInfo.changeCompanyInterface,function(jsonObj){
			listVue.qs_projectNameList = jsonObj.empj_ProjectInfoList;
		},listVue.errMsg,baseInfo.edModelDivId)
	}
	
	// 列表操作--------------------改变项目名称的方法
	function changeProjectHandleFun() {
		changeProjectHandle(listVue,baseInfo.changeProjectInterface,function(jsonObj){
			listVue.qs_buildingNumberlist = jsonObj.empj_BuildingInfoList;
		},listVue.errMsg,baseInfo.edModelDivId)
	}
		
	// 列表操作--------------获取导入的excel数据
	function getExcelDataForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			url:this.importUrl,
		}
	}
	function tgpf_RemainRightDel()
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
						alert(listVue.selectItem.length)
						for (var i = listVue.selectItem.length - 1; i >= 0; i--) {
							var item = listVue.selectItem[i];
							listVue.deleCodes.push(item.eCode);
							listVue.tgpf_ManagedProjectStatisticsList.splice(item, 1);
						}
						listVue.selectItem = [];
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
	
	function isInterger(o) {
		return typeof obj === 'number' && obj%1 === 0;
	}

	function handleSelectionChange(val) {
		listVue.selectItem = [];
		for (var i = 0; i < val.length; i++) {
			var index = listVue.tgpf_ManagedProjectStatisticsList.indexOf(val[i]);
			listVue.selectItem.push(index)
		}
		listVue.selectItem.sort()
	}
	
	// 列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgpf_ManagedProjectStatisticsList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];// 解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgpf_ManagedProjectStatisticsList.forEach(function(item) {
	    		listVue.selectItem.push(item.tableId);
	    	});
	    }
	}
	// 列表操作--------------刷新
	function refresh()
	{
		new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				listVue.tgpf_ManagedProjectStatisticsList=jsonObj.tg_LoanProjectCountP_ViewList;
				listVue.model=jsonObj.accountHouseMap;
				
				listVue.tgpf_ManagedProjectStatisticsList.forEach(function(item) {
					item.escrowArea = addThousands(item.escrowArea);
					item.recordAvgPriceOfBuilding = addThousands(item.recordAvgPriceOfBuilding);
					item.orgLimitedAmount = addThousands(item.orgLimitedAmount);
					item.currentLimitedAmount = addThousands(item.currentLimitedAmount);
					item.currentEscrowFund = addThousands(item.currentEscrowFund);
					item.amountOffset = addThousands(item.amountOffset);
				});
				
				listVue.model.accountEscrowArea = addThousands(listVue.model.accountEscrowArea);
				listVue.model.accountCurrentLimitedAmount = addThousands(listVue.model.accountCurrentLimitedAmount);
				listVue.model.accountCurrentEscrowFund = addThousands(listVue.model.accountCurrentEscrowFund);
				listVue.model.accountAmountOffset = addThousands(listVue.model.accountAmountOffset);
				
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				listVue.combineFun();
				// 动态跳转到锚点处，id="top"
				document.getElementById('Tgpf_ManagedProjectStatisticsDiv').scrollIntoView();
			}
		});
	}
	
	// 列表操作------------搜索
	function search()
	{
		/*if(document.getElementById("remainSign").value == "") {
			alert("请输入协议签署日期！");
			return false;
		}*/
		listVue.pageNumber = 1;
		refresh();
	}
	
		// 导出excel
	function remaindRightDifferenceExportExcelHandle()
	{
		new ServerInterface(baseInfo.exportInterface).execute(listVue.getSearchForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				listVue.errMsg = jsonObj.info;
			}
			else
			{
				$(baseInfo.sdModelDiveId).modal({
					backdrop :'static'
				});
				
				window.location.href="../"+jsonObj.fileURL;
			}
		});
	}
	
	
	function initData()
	{
		listVue.selectItem = [];
		listVue.loadAreaList();// 页面加载区域的方法
		listVue.loadCompanyName(); // 页面加载显示开发企业方法
	}
	
	function handleReset()
	{
		listVue.keyword = "";
		listVue.cityRegionId = "";
		listVue.companyId = "";
		listVue.projectId = "";
		document.getElementById("remainSign").value = "";
		listVue.recordDateStart = "";
		listVue.recordDateEnd = "";
	}
	// 添加日期控件	
	laydate.render({
		  elem: '#remainSign'
		  ,range: '~'
		  ,done: function(value, date,enddate){
		  	listVue.remainSign = value;
		  	var str = value.split('~');
		  	listVue.recordDateStart = str[0];
		    listVue.recordDateEnd = str[1];
			
			}
		});
	// ------------------------方法定义-结束------------------//
	
	// ------------------------数据初始化-开始----------------//

	
	listVue.refresh();
	listVue.initData();
	// ------------------------数据初始化-结束----------------//
})({
	"listDivId":"#Tgpf_ManagedProjectStatisticsDiv",
	"loadCityRegionInterface" : "../Sm_CityRegionInfoList", // 区域
	"companyNameInterface" : "../Emmp_CompanyInfoList", // 显示开发企业名称接口
	"changeCompanyInterface" : "../Empj_ProjectInfoList", // 改变企业名称接口
	"changeProjectInterface" : "../Empj_BuildingInfoList", // 改变项目名称接口
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Tg_LoanProjectCountP_ViewList",
	"exportInterface":"../Tg_LoanProjectCountP_ViewExcel",
});
