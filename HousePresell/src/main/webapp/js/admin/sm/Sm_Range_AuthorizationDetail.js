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
			companyInfoType:null,
			tableId:null,
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
			saveEdit:saveEdit,
			rangeAuthTypeChange:rangeAuthTypeChange,
			companyInfoChange:companyInfoChange,
			sm_Range_AuthorizationEdit:sm_Range_AuthorizationEdit,
		},
		computed:{
			 
		},
		components : {
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
	
	//跳转编辑
	function sm_Range_AuthorizationEdit()
    {
        enterNext2Tab(listVue.tableId, "范围授权修改", "sm/Sm_Range_AuthorizationEdit.shtml", listVue.tableId+"010105");
    }
	
	/**
	 * 根据授权类别，加载 所有区域，所有区域及项目，所有区域项目及楼幢
	 */
	function getRangeList(rangeAuthType)
	{
		var model = {
			interfaceVersion : 19000101,
			rangeAuthType : rangeAuthType,
		}
		new ServerInterface(baseInfo.rangeListInterface).execute(model, function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				listVue.zNodes = jsonObj.sm_CityRegionInfoList;
				console.info(listVue.zNodes);
				
				initZtreeList();
				
				document.getElementById('sm_Range_AuthorizationDetailDiv').scrollIntoView();
			}
		});
	}
	function initZtreeList()
	{
		$(document).ready(function(){
			$.fn.zTree.init($("#treeDemo_RangeAuthDetail"), listVue.setting, listVue.zNodes);
			zTree = $.fn.zTree.getZTreeObj("treeDemo_RangeAuthDetail");
			//console.info(zTree.getNodes());
		});
	}
	function getDetailInfo(tableId)
	{
		var model = {
			interfaceVersion : 19000101,
			tableId : tableId,
		}
		new ServerInterface(baseInfo.detailInterface).execute(model, function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				listVue.zNodes = jsonObj.sm_CityRegionInfoList;
				initZtreeList();
				
				listVue.sm_Permission_RangeAuthorization = jsonObj.sm_Permission_RangeAuthorization;
				try {listVue.selectItem = jsonObj.sm_Permission_RangeAuthorization.idSelectList;} catch (e) {listVue.selectItem = [];}
				console.info(listVue.selectItem);
				try
				{
					Vue.nextTick(function () {
						var rangeAuthType = jsonObj.sm_Permission_RangeAuthorization.rangeAuthType;
						//console.info(rangeAuthType);
						if(rangeAuthType != null && rangeAuthType == 1)
						{
							//区域
							for(var i=0;i<listVue.selectItem.length;i++)
							{
								var node = zTree.getNodeByParam("areaId",listVue.selectItem[i]);
								//(node.getChildren() == null || node.getChildren().length == 0)
								if(node != null)
								{
									zTree.checkNode(node, true, true);
									zTree.updateNode(node); 
								}
							}
						}
						else if(rangeAuthType != null && rangeAuthType == 2)
						{
							//项目
							for(var i=0;i<listVue.selectItem.length;i++)
							{
								var node = zTree.getNodeByParam("projectId",listVue.selectItem[i]);
								//(node.getChildren() == null || node.getChildren().length == 0)
								if(node != null)
								{
									zTree.checkNode(node, true, true);
									zTree.updateNode(node); 
								}
							}
						}
						else if(rangeAuthType != null && rangeAuthType == 3)
						{
							//楼幢
							for(var i=0;i<listVue.selectItem.length;i++)
							{
								var node = zTree.getNodeByParam("buildId",listVue.selectItem[i]);
								//(node.getChildren() == null || node.getChildren().length == 0)
								if(node != null)
								{
									zTree.checkNode(node, true, true);
									zTree.updateNode(node); 
								}
							}
						}
						
						//禁用 zTree 所有节点 CheckBox 的勾选选操作。
						var nodes_UnEdit = zTree.getNodes();
						//console.info(nodes_UnEdit);
						for(var i=0;i<nodes_UnEdit.length;i++)
						{
							zTree.setChkDisabled(nodes_UnEdit[i], true, true, true);
						}
					});
				} 
				catch (e) 
				{
					listVue.selectItem = [];
				}
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
	
	function saveEdit()
	{
		var model = {
			interfaceVersion:19000101,
			tableId:listVue.tableId,
			emmp_CompanyInfoId:listVue.sm_Permission_RangeAuthorization.companyInfoId,
			rangeAuthType:listVue.sm_Permission_RangeAuthorization.rangeAuthType,
			idArr:listVue.selectItem,
			authTimeStampRange:listVue.sm_Permission_RangeAuthorization.authTimePeriod,
		}
		/*console.info(model);
		return;*/
		new ServerInterface(baseInfo.editInterface).execute(model, function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				noty({"text":jsonObj.info,"type":"success","timeout":2000});
				listVue.sm_Permission_RangeAuthorization = jsonObj.sm_Permission_RangeAuthorization;
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
		listVue.selectItem = [];
		getRangeList(listVue.sm_Permission_RangeAuthorization.rangeAuthType);
	}
	
	function companyInfoChange()
	{
		listVue.companyInfoType = $('#companyInfo_'+listVue.companyInfoId).attr('data-thetype');
	}
	
	function initData()
	{
		/*laydate.render({
			  elem: '#date_RangeAuthDetail',
			  range:true
		});*/
		
		getCompanyInfoList();

		/*var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
    	//console.info(tableIdStr);
        var array = tableIdStr.split("_");
        if (array.length > 1)
        {
        	listVue.tableId = array[1];
        	getDetailInfo(listVue.tableId);
        }*/
		
		getIdFormTab("",function (id) {
			listVue.tableId=id
            getDetailInfo(listVue.tableId);
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
        	if(listVue.sm_Permission_RangeAuthorization.rangeAuthType == 1)
        	{
        		//区域
        		var tableId = nodes[i].areaId
        		if(tableId != null)
        		{
        			listVue.selectItem.push(tableId);
        		}
        	}
        	else if(listVue.sm_Permission_RangeAuthorization.rangeAuthType == 2)
        	{
        		//项目
        		var tableId = nodes[i].projectId
        		if(tableId != null)
        		{
        			listVue.selectItem.push(tableId);
        		}
        	}
        	else if(listVue.sm_Permission_RangeAuthorization.rangeAuthType == 3)
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
	"listDivId":"#sm_Range_AuthorizationDetailDiv",
	"companyListInterface":"../Emmp_CompanyInfoForAllForSelect",
	"editInterface":"../Sm_Permission_RangeAuthorizationEdit",
	"rangeListInterface":"../Sm_CityRegionInfoListForRangeAuthAddForSelect",
	"detailInterface":"../Sm_Permission_RangeAuthorizationDetail",
});
