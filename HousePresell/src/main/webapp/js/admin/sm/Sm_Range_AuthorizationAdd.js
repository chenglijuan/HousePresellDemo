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
			rangeAuthType:"1",//授权类别
			companyInfoList:[],
			companyInfoId:"",
			forCompanyTypeId:null,
			companyInfoType:null,
			companyTypeName:null,
			companyTypeMap:new Map(),//key:theValue,value:theName
			companyTypeValAndIdMap:new Map(),//key:theValue,value:tableId
			companyTypeList : [],
			sm_Permission_RangeAuthorization:{},
			//zTree
			zNodes:[],
			setting : {
				data: {
					simpleData: {
						enable: true
					},
				},
				view: {
					dblClickExpand: false,
					showIcon:false,
					showLine:false
				},
				check: {
					enable: true,//checkBox close
				},
				callback: {
					onCheck : onCheck,//用户勾选后的回调
				}
			},
		},
		methods : {
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			getSearchForm : getSearchForm,
			listItemSelectHandle: listItemSelectHandle,
			search:search,
			saveAdd:saveAdd,
			rangeAuthTypeChange:rangeAuthTypeChange,
			companyInfoChange:companyInfoChange,
			changeCompanyType : function (data){
				this.forCompanyTypeId = data.tableId;
				this.companyInfoType = data.theValue;
				this.companyTypeName = this.companyTypeMap.get(data.theType);
				
				this.companyInfoId = null;
				getCompanyList(this.companyInfoType);
			},
			emptyCompanyType : function (data){
				this.forCompanyTypeId = null;
				this.companyInfoType = null;
				this.companyTypeName = null;
				
				this.companyInfoId = null;
				getCompanyList(this.companyInfoType);
			},
			changeCompany : function (data){
				this.forCompanyTypeId = this.companyTypeValAndIdMap.get(data.theType);
				this.companyInfoType = data.theType;
				this.companyTypeName = this.companyTypeMap.get(data.theType);
				
				this.companyInfoId = data.tableId;
			},
			emptyCompany : function (data){
				this.forCompanyTypeId = null;
				this.companyInfoType = null;
				this.companyTypeName = null;
				
				this.companyInfoId = null;
				getCompanyList(this.companyInfoType);
			},
		},
		computed:{
			 
		},
		components : {
			'vue-select': VueSelect,
		},
		watch:{
//			pageNumber : refresh,
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
			userOperateId:this.userOperateId,
		}
	}
	//列表操作--------------获取“更新”表单参数
	function getUpdateForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			theState:this.theState,
			userOperateId:this.userOperateId,
			remoteAddress:this.remoteAddress,
			operate:this.operate,
			inputForm:this.inputForm,
			result:this.result,
			info:this.info,
			returnJson:this.returnJson,
			startTimeStamp:this.startTimeStamp,
			endTimeStamp:this.endTimeStamp,
		}
	}
	//列表操作--------------刷新
	function refresh()
	{
		
	}
	
	/**
	 * 根据授权类别，加载 所有区域，所有区域及项目，所有区域项目及楼幢
	 */
	function getRangeList()
	{
		var model = {
			interfaceVersion : 19000101,
			rangeAuthType : listVue.rangeAuthType,
		}
		new ServerInterface(baseInfo.rangeListInterface).execute(model, function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				listVue.zNodes = jsonObj.sm_CityRegionInfoList;
				
				$(document).ready(function(){
					$.fn.zTree.init($("#treeDemo_RangeAuthAdd"), listVue.setting, listVue.zNodes);
					zTree = $.fn.zTree.getZTreeObj("treeDemo_RangeAuthAdd");
					//console.info(zTree.getNodes());
				});
				
				//动态跳转到锚点处，id="top"
				document.getElementById('sm_Range_AuthorizationAddDiv').scrollIntoView();
			}
		});
	}
	function getCompanyInfoList()
	{
		var model = {
			interfaceVersion : 19000101,
			exceptZhengTai : true,
		}
		new ServerInterface(baseInfo.companyListInterface).execute(model, function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				listVue.companyInfoList = jsonObj.emmp_CompanyInfoList;
			}
		});
	}
	//列表操作------------搜索
	function search()
	{
		listVue.pageNumber = 1;
		refresh();
	}
	
	function saveAdd()
	{
		var model = {
			interfaceVersion:19000101,
			emmp_CompanyInfoId:listVue.companyInfoId,
			rangeAuthType:listVue.rangeAuthType,
			idArr:listVue.selectItem,
			authTimeStampRange:$('#date0101050101').val(),
		}
		console.info(model);
		//return;
		new ServerInterface(baseInfo.addInterface).execute(model, function(jsonObj){
			if(jsonObj.result != "success")
			{
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});
				generalErrorModal(jsonObj);
			}
			else
			{
				//noty({"text":jsonObj.info,"type":"success","timeout":2000});
				listVue.sm_Permission_RangeAuthorization = jsonObj.sm_Permission_RangeAuthorization;
				
				var tableId = listVue.sm_Permission_RangeAuthorization.tableId;
				generalSuccessModal();
				enterNext2Tab(tableId, "范围授权详情", "sm/Sm_Range_AuthorizationDetail.shtml", tableId+"010105");
			}
		});
	}
	
	function indexMethod(index) {
		return generalIndexMethod(index, listVue)
	}

	function listItemSelectHandle(list) {
		generalListItemSelectHandle(listVue,list)
 	}
	
	function rangeAuthTypeChange()
	{
		getRangeList();
	}
	
	function companyInfoChange(data)
	{
		listVue.companyInfoType = data.theType;//$('#companyInfo_'+listVue.companyInfoId).attr('data-thetype');
		listVue.companyTypeName = listVue.companyTypeMap.get(listVue.companyInfoType);
		listVue.companyInfoId = data.tableId;
	}
	
	function initData()
	{
		laydate.render({
			  elem: '#date0101050101',
			  range:true
		});
		getSm_BaseParameterForSelect();
		
		getCompanyInfoList();
		getRangeList();
	}
	//获取企业列表
	function getCompanyList(companyType)
	{
		console.info(companyType);
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
				listVue.companyInfoList = jsonObj.emmp_CompanyInfoList;
			}
		});
	}
	function getSm_BaseParameterForSelect()
	{
	    generalGetParamList("8",function (list) 
	    {
	    	listVue.companyTypeList =list;
	    	for(var i=0;i<listVue.companyTypeList.length;i++)
	    	{
	    		listVue.companyTypeMap.put(listVue.companyTypeList[i].theValue,listVue.companyTypeList[i].theName);
	    		listVue.companyTypeValAndIdMap.put(listVue.companyTypeList[i].theValue,listVue.companyTypeList[i].tableId);
	    	}
	    })
	}
	//------------------------方法定义-结束------------------//
	function onCheck(e, treeId, treeNode) 
	{
		//加载用户所选菜单的详情
		//将指定的节点选中
		zTree.cancelSelectedNode();
		zTree.selectNode(treeNode,true);
		
		listVue.selectItem = [];
        var nodes = zTree.getCheckedNodes(true);
        
        for(var i=0;i<nodes.length;i++)
        {
        	//console.info(nodes[i].tableId);
        	if(listVue.rangeAuthType == 1)
        	{
        		//区域
        		var tableId = nodes[i].areaId
        		if(tableId != null)
        		{
        			listVue.selectItem.push(tableId);
        		}
        	}
        	else if(listVue.rangeAuthType == 2)
        	{
        		//项目
        		var tableId = nodes[i].projectId
        		if(tableId != null)
        		{
        			listVue.selectItem.push(tableId);
        		}
        	}
        	else if(listVue.rangeAuthType == 3)
        	{
        		//楼幢
        		var tableId = nodes[i].buildId
        		if(tableId != null)
        		{
        			listVue.selectItem.push(tableId);
        		}
        	}
        }
        //console.info(listVue.menuCheckArr);
	}
	var zTree;
	//------------------------数据初始化-开始----------------//
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#sm_Range_AuthorizationAddDiv",
	"companyListInterface":"../Emmp_CompanyInfoForAllForSelect",
	"addInterface":"../Sm_Permission_RangeAuthorizationAdd",
	"rangeListInterface":"../Sm_CityRegionInfoListForRangeAuthAddForSelect",
});
