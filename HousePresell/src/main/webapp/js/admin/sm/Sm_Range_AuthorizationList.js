(function(baseInfo){
	var listVue = new Vue({
		el : baseInfo.listDivId,
		data : {
			poName:"Sm_Permission_RangeAuthorization",
			interfaceVersion :19000101,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			keyword : "",
			selectItem : [],
			isAllSelected : false,
			theState:0,//正常为0，删除为1
			upDisabled:true,
			deDisabled:true,
			companyId:"",
			forCompanyTypeId:"",
			forCompanyType:"",
			companyTypeValAndIdMap:new Map(),//key:theValue,value:tableId
			companyTypeList : [],
			emmp_CompanyInfoList : [],
			sm_Range_AuthorizationList:[],
			authTimeStampRange:null,
			rangeAuthType:"",
			orderBy:null,//排序相关
			diabaleShowLog: true,//日志相关
			rangeAuthTypeList : [
				{tableId:"1",theName:"区域"},
				{tableId:"2",theName:"项目"},
				{tableId:"3",theName:"楼幢"},
			]
		},
		methods : {
			reset : reset,
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			showLog: showLog,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			listItemSelectHandle: listItemSelectHandle,
			search:search,
			checkAllClicked : checkAllClicked,
			changePageNumber : function(data){
				listVue.pageNumber = data;
			},
			changeCountPerPage : function (data) {
				if (listVue.countPerPage != data) {
					listVue.countPerPage = data;
					//listVue.refresh();
				}
			},
			sm_Permission_RangeAuthAdd:sm_Permission_RangeAuthAdd,
			sm_Permission_RangeAuthEdit:sm_Permission_RangeAuthEdit,
			sm_Permission_RangeAuthDetail:sm_Permission_RangeAuthDetail,
			sm_Permission_RangeAuthDel:sm_Permission_RangeAuthDel,
			rangeAuthExportExcel : rangeAuthExportExcel,
			
			changeCompanyType : function (data){
				this.forCompanyTypeId = data.tableId;
				this.forCompanyType = data.theValue;
				
				this.companyId = null;
				getCompanyList(this.forCompanyType);
				//search();
			},
			emptyCompanyType : function (data){
				this.forCompanyTypeId = null;
				this.forCompanyType = null;
				
				this.companyId = null;
				getCompanyList(this.forCompanyType);
				//search();
			},
			changeCompany : function (data){
				this.companyId = data.tableId;
				
				this.forCompanyTypeId = this.companyTypeValAndIdMap.get(data.theType);
				this.forCompanyType = data.theType;
				//search();
			},
			emptyCompany : function (data){
				this.companyId = null;
				
				this.forCompanyTypeId = null;
				this.forCompanyType = null;
				//search();
			},
			changeRangeAuthType : function(data){
				this.rangeAuthType = data.tableId;
			},
			emptyRangeAuthType : function(){
				this.rangeAuthType = null;
			},
			sortChange: function(column, prop, order) {
				console.log(column);//当前列
				console.log(column.prop);//当前需要排序的字段
				console.log(column.order);//排序的规则（升序、降序和默认[默认就是没排序]）
				this.orderBy = generalOrderByColumn(column)
				search();
			},
		},
		computed:{
			 
		},
		components : {
			'vue-listselect': VueListSelect,
			'vue-nav' : PageNavigationVue,
			"vue-listsearch" : VueListSearch
		},
		watch:{
			pageNumber : refresh,
			countPerPage : refresh,
			selectItem : selectedItemChanged,
		}
	});
	//------------------------方法定义-开始------------------//
	function listItemSelectHandle(list)
    {
        //修改按钮禁用状态
        if(list.length==1)
        {
            listVue.upDisabled = false;
        }
        else
        {
            listVue.upDisabled = true;
        }
        //删除按钮禁用状态
        if(list.length >= 1)
        {
            listVue.deDisabled = false;
        }
        else
        {
            listVue.deDisabled = true;
        }
        generalListItemSelectHandle(listVue,list)
    }
	
	//新增
    function sm_Permission_RangeAuthAdd()
    {
        enterNewTab("", "新增范围授权", "sm/Sm_Range_AuthorizationAdd.shtml");
    }
    //点击“编号”进入详情页面
    function sm_Permission_RangeAuthDetail(tableId)
    {
        enterNewTab(tableId, "范围授权详情", "sm/Sm_Range_AuthorizationDetail.shtml");
    }

    //修改
    function sm_Permission_RangeAuthEdit()
    {
        var tableId = listVue.selectItem[0];
        enterNewTab(tableId, "范围授权修改", "sm/Sm_Range_AuthorizationEdit.shtml");
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
			emmp_CompanyInfoId:this.companyId,
			forCompanyType:this.forCompanyType,
			rangeAuthType:this.rangeAuthType,
			orderBy:this.orderBy,
			authTimeStampRange:$('#date01010501').val(),
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
	//删除
    function sm_Permission_RangeAuthDel()
    {
        if (listVue.selectItem.length > 1)
        {
        	generalSelectModal(function(){
        		sm_Permission_RangeAuthDelExecute();
            }, "确认删除吗？");
        }
        else
        {
            listVue.idArr = listVue.selectItem[0];
            generalSelectModal(function(){
        		sm_Permission_RangeAuthDelExecute();
            }, "确认删除吗？");
        }
    }
    function sm_Permission_RangeAuthDelExecute()
  	{
          new ServerInterface(baseInfo.deleteInterface).execute(listVue.getDeleteForm(), function(jsonObj){
              if(jsonObj.result != "success")
              {
                  generalErrorModal(jsonObj);
              }
              else
              {
            	  listVue.selectItem=[];
                  refresh();
              }
          });
    }
	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		listVue.isAllSelected = (listVue.sm_Range_AuthorizationList.length > 0)
		&&	(listVue.selectItem.length == listVue.sm_Range_AuthorizationList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.sm_Range_AuthorizationList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.sm_Range_AuthorizationList.forEach(function(item) {
	    		listVue.selectItem.push(item.tableId);
	    	});
	    }
	}
	function reset()
	{
		this.keyword = "";
		this.companyId = "";
		this.forCompanyType = null;
		this.forCompanyTypeId = "";
		this.authTimeStampRange = null;
		$('#date01010501').val("");
		this.rangeAuthType = "";
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
				listVue.sm_Range_AuthorizationList=jsonObj.sm_Permission_RangeAuthorizationList;
				console.info(listVue.sm_Range_AuthorizationList);
				
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('sm_Range_AuthorizationListDiv').scrollIntoView();
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
	//根据按钮权限，动态控制，该页面的所有“可控按钮”的显示与否
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}
	function initData()
	{
		laydate.render({
			elem: '#date01010501',
			range:true
		});
		
		initButtonList();
		getCompanyList();
		getSm_BaseParameterForSelect();
		initButtonList();
	}
	
	function showLog() {
		enterLogTabWithTemplate(listVue.poName, listVue);
    }
	
	function rangeAuthExportExcel()
	{
		var model = {
			interfaceVersion : 19000101,
			idArr : listVue.selectItem,
			keyword : listVue.keyword,
		};
		
		new ServerInterface(baseInfo.rangeAuthExportExcelInterface).execute(model, function (jsonObj) {
			if (jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				window.location.href=jsonObj.fileDownloadPath;
				listVue.selectItem = [];
			}
		});
	}
	
	//获取企业列表
	function getCompanyList(companyType)
	{
		var model = {
			interfaceVersion : 19000101,
			//exceptZhengTai : true,
			theType:companyType,
		};
		console.info(model);
		new ServerInterface(baseInfo.companyListInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				listVue.emmp_CompanyInfoList = jsonObj.emmp_CompanyInfoList;
				console.info(listVue.emmp_CompanyInfoList);
			}
		});
	}
	function getSm_BaseParameterForSelect()
	{
	    generalGetParamList("8",function (list) 
	    {
	    	listVue.companyTypeList =list;
	    	for(var i = 0;i<listVue.companyTypeList.length;i++)
	    	{
	    		listVue.companyTypeValAndIdMap.put(listVue.companyTypeList[i].theValue,listVue.companyTypeList[i].tableId);
	    	}
	    })
	}
	//------------------------方法定义-结束------------------//
	//根据按钮权限，动态控制，该页面的所有“可控按钮”的显示与否
	function initButtonList()
	{
		//在BaseJs中
		getButtonList();
	}
	//------------------------数据初始化-开始----------------//
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#sm_Range_AuthorizationListDiv",
	"listInterface":"../Sm_Permission_RangeAuthorizationList",
	"deleteInterface":"../Sm_Permission_RangeAuthorizationDelete",
	"rangeAuthExportExcelInterface":"../Sm_Permission_RangeAuthorizationExportExcel",
	"companyListInterface":"../Emmp_CompanyInfoForAllForSelect",
});
