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
			busiState:0,
			Empj_HouseholdList:[],
			projectId : "", //项目名称
			companyId : "", //开发企业
			qs_companyNameList : [], //页面加载显示开发企业
			qs_projectNameList : [], //显示项目名称
			editDisabled : true,
			errMsg : "",
			projectId: "",
			projectName: "",
			qs_projectNameList: [],
			qs_buildingNumberList:[],
			eCodeOfBuilding:"",
			buildingId:"",
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			search:search,
            resetSearchInfo:resetSearchInfo,
            householdEditHandle : householdEditHandle,
			changePageNumber : function(data){
				//listVue.pageNumber = data;
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
			handleSelectionChange: handleSelectionChange,
			indexMethod: indexMethod,
			getProjectId: function(data) {
				listVue.projectId = data.tableId;
				listVue.projectName = data.theName;
				loadBuildNumber();
			},
			emptyProjectId : function(){
				listVue.projectId = null;
				listVue.projectName = null;
				listVue.buildingId = null;
				listVue.qs_buildingNumberList = [];
			},
			
			getBuildingId: function(data) {
				listVue.buildingId = data.tableId;
				
				listVue.eCodeOfBuilding = data.theName;
			},
			emptyBuildingId : function(){
               listVue.buildingId = null;
				
				listVue.eCodeOfBuilding = null;
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
			//pageNumber : refresh,
		},
		mounted:function(){
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
//		    busiState: this.busiState,
			theState:this.theState,
			projectName:this.projectName,		
			buildingId:this.buildingId,	   
//			userStartId:this.userStartId,
//			userRecordId:this.userRecordId,
//			developCompanyId:this.developCompanyId,
//			cityRegionId:this.cityRegionId,
//			streetId:this.streetId,
//			designCompanyId:this.designCompanyId,
		}
	}

	//列表操作----------序号
	function indexMethod(index){
		return generalIndexMethod(index, listVue)
	}
	
	
    //列表操作------------搜索
    function search()
    {
        listVue.pageNumber = 1;
        refresh();
    }
    
    //列表操作------------重置搜索
	function resetSearchInfo() {
		this.keyword = "";
        listVue.pageNumber = 1;
        listVue.projectId = "";
        listVue.projectName = "";
        listVue.eCodeOfBuilding = "";
        listVue.buildingId = "";
        refresh();
    }


    //列表操作---选择
    function handleSelectionChange(val) {
    	if(val.length == 1){
    		listVue.editDisabled = false;
    	}else{
    		listVue.editDisabled = true;
    	}
        listVue.selectItem = [];
        for (var index = 0; index < val.length; index++) {
            var element = val[index].tableId;
            listVue.selectItem.push(element)
        }
    }
    //列表操作---------------------修改
    function householdEditHandle(){
    	var tableId = listVue.selectItem[0];
    	enterNextTab(tableId, '编辑户信息', 'empj/Empj_HouseholdEdit.shtml',tableId+"03010205");
		/*$("#tabContainer").data("tabs").addTab({ id:"jump_"+tableId, text: '编辑户信息', closeable: true, url: 'empj/Empj_HouseholdEdit.shtml' });*/
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
				listVue.Empj_HouseholdList=jsonObj.empj_UnitInfoList;
				listVue.Empj_HouseholdList.forEach(function(item){
					item.upfloorNumber = addThousandsCount(item.upfloorNumber);
					item.downfloorNumber = addThousandsCount(item.downfloorNumber);
					item.sumFamilyNumber = addThousandsCount(item.sumFamilyNumber);
				})
				
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('Empj_HouseholdListDiv').scrollIntoView();
			}
		});
	}
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}
	
	function loadProject() {
		var model = {
				interfaceVersion: listVue.interfaceVersion,
		}
		new ServerInterface(baseInfo.loadProjectInterface).execute(model, function(jsonObj){
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				listVue.qs_projectNameList=jsonObj.empj_ProjectInfoList;
			}
	   });
	}
	
	function loadBuildNumber() {
		var model = {
			interfaceVersion: listVue.interfaceVersion,
			projectId: listVue.projectId,
		}
		new ServerInterface(baseInfo.loadBuildNumberInterface).execute(model, function(jsonObj){
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				var qs_buildingNumberList=jsonObj.empj_BuildingInfoList;
				qs_buildingNumberList.forEach(function(item){
					var model={
						tableId : item.tableId,
						theName : item.eCodeFromConstruction,
					}
					listVue.qs_buildingNumberList.push(model);
				})
			}
		});
	}
	function initData()
	{
		loadProject();
		initButtonList();
        listVue.refresh();
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#Empj_HouseholdListDiv",
	"edModelDivId":"#edModelHouseholdList",
	"sdModelDivId":"#sdModelHouseholdList",
	"listInterface":"../Empj_HouseInfoLists",//列表页面加载接口
	"loadProjectInterface":"../Empj_HouseInfoProjectNameLists",
	"loadBuildNumberInterface":"../Empj_HouseInfoBuildingecoLists"
});
