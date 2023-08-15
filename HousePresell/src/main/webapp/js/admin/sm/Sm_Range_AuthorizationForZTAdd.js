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
			userInfoList:[],
			userInfoId:"",
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
			getUserInfoId : function(data){
				listVue.userInfoId = data.tableId;
			},
			emptyUserInfoId : function(data){
				listVue.userInfoId = ""
			}
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
				console.info(listVue.zNodes);
				
				$(document).ready(function(){
					$.fn.zTree.init($("#treeDemo_RangeAuthForZTAdd"), listVue.setting, listVue.zNodes);
					zTree = $.fn.zTree.getZTreeObj("treeDemo_RangeAuthForZTAdd");
					//console.info(zTree.getNodes());
				});
				
				//动态跳转到锚点处，id="top"
				document.getElementById('sm_Range_AuthorizationForZTAddDiv').scrollIntoView();
			}
		});
	}
	function getUserList()
	{
		var model = {
			interfaceVersion : 19000101,
			theType : 1,//正泰用户
		}
		new ServerInterface(baseInfo.userListInterface).execute(model, function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				listVue.userInfoList = jsonObj.sm_UserList;
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
			userInfoId:listVue.userInfoId,
			rangeAuthType:listVue.rangeAuthType,
			idArr:listVue.selectItem,
			authTimeStampRange:$('#date0101060101').val(),
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
				enterNext2Tab(tableId, "系统用户范围授权详情", "sm/Sm_Range_AuthorizationForZTDetail.shtml", tableId+"010106");
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
	
	function initData()
	{
		laydate.render({
			  elem: '#date0101060101',
			  range:true
		});
		
		getUserList();
		getRangeList();
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
	"listDivId":"#sm_Range_AuthorizationForZTAddDiv",
	"userListInterface":"../Sm_UserForSelect",
	"addInterface":"../Sm_Permission_RangeAuthorizationForZTAdd",
	"rangeListInterface":"../Sm_CityRegionInfoListForRangeAuthAddForSelect",
});
