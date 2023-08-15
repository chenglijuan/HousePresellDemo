(function(baseInfo){
	var listVue = new Vue({
		el : baseInfo.listDivId,
		data : {
			interfaceVersion : 19000101,
			selectItem : [],
			sm_Permission_UIResourceList:[],
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
					onRightClick: OnRightClick,//右击
					beforeClick: beforeClick,
					onClick : onClick,//单机
					onCheck : onCheck,//用户勾选后的回调
				}
			},
			//菜单项(添加、编辑)
			theType:2,//虚拟菜单
			theName:null,
			eCode:null,
			parentUI:{tableId:'',theName:''},
			theIndex:null,
			resourceUIId:'',//资源URL Id
			resourceUIList:[],//资源URL(全局 ALL)
			urlAndBtnRelationMap:new Map(),
			childrenBtnList:[],//功能按钮
			iconPath:null,//图标
			remark:null,//备注
			menuId:null,//用户需要编辑的Id，没有则默认为执行添加操作
			parentUIIdForNowMenu:null,//用户选中的菜单Id，目前用于“添加下级菜单”
			
			//==== 角色授权新增(业务逻辑参数) Start ======//
			roleId:null,
			sm_Permission_Role:{"tableId":null,"theName":null,"busiType":'',"eCode":null,"enableDate":null,"downDate":null},
			btnCheckArr:[],//用户勾选的菜单Id数组
			menuCheckArr:[],//用户勾选的功能按钮Id数组
			busiType:"",
			//==== 角色授权新增 End ===============//
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			listItemSelectHandle: listItemSelectHandle,
			search:search,    //搜索资源列表
            reset:reset,      //重置
            dateFormat:dateFormat,//格式化时间戳
			changePageNumber : function(data){
				listVue.pageNumber = data;
			},
            
            //菜单项(添加、编辑)
            theTypeChange : theTypeChange,
            resourceUIChange : resourceUIChange,
            checkChildrenBtnListHandle : checkChildrenBtnListHandle,
            //editRoleUI : editRoleUI,
            sm_Permission_RoleUIEdit : sm_Permission_RoleUIEdit,
		}
	});
	//------------------------方法定义-开始------------------//
	//重置
	function reset()
	{
		//虚拟菜单
		this.theType = 2;
		this.theName = null;
		this.eCode = null;
		this.parentUI = {tableId:'',theName:''};
		this.theIndex = null;
		this.resourceUIId = '';//资源URL Id
		this.iconPath = null;//图标
		this.remark = null;//备注
    	//功能按钮 All 复位
    	this.childrenBtnList = [];
    	this.menuId = null;
    }
	 
	//列表操作--------------获取"搜索列表"表单参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			userRecordId:this.userRecordId,
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

	//列表操作--------------刷新
	function refresh()
	{
		new ServerInterfaceSync(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				listVue.zNodes = jsonObj.sm_Management_MenuList;
//				console.info(listVue.zNodes);
				
				$(document).ready(function(){
					$.fn.zTree.init($("#treeDemo_RoleUIDetail"), listVue.setting, listVue.zNodes);
					zTree = $.fn.zTree.getZTreeObj("treeDemo_RoleUIDetail");
					//console.info(zTree.getNodes());
					rMenu = $("#rMenu");
				});
				
				//动态跳转到锚点处，id="top"
				//document.getElementById('sm_Permission_RoleUIDetailDiv').scrollIntoView();
			}
		});
		
		
	}
	
	//列表操作------------搜索
	function search()
	{
		listVue.pageNumber = 1;
		refresh();
	}
	
    //时间戳格式化方法
    function dateFormat(row, column)
    {
        var date = row[column.property];
        if (date == undefined) {
            return "";
        }
        return moment(date).format("YYYY-MM-DD");
    }

    function listItemSelectHandle(list) {
		generalListItemSelectHandle(listVue,list)
 	}
	
    function initData()
    {
    	getUrlList();
    	
    	/*var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
    	//console.info(tableIdStr);
    	
        var array = tableIdStr.split("_");
        if (array.length > 1)
        {
        	listVue.roleId = array[1];
        	getRoleDetail(listVue.roleId);
        }*/
    	
    	getIdFormTab("",function (id) {
		listVue.roleId=id;
		getRoleDetail(listVue.roleId);
        })
    }
    function getRoleDetail(roleId)
    {
    	//roleId = 63;//测试时用
    	//listVue.roleId = 63;//测试时用
    	//console.info(roleId);

    	var model = {
			interfaceVersion : 19000101,
			tableId : roleId,
    	}
    	new ServerInterface(baseInfo.roleDetailInterface).execute(model, function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				console.groupEnd();
				listVue.sm_Permission_Role = jsonObj.sm_Permission_Role;
				try {listVue.btnCheckArr = jsonObj.btnCheckArr;} catch (e) {listVue.btnCheckArr = [];}
				try
				{
					Vue.nextTick(function () {
						listVue.menuCheckArr = jsonObj.menuCheckArr;
						for(var i=0;i<listVue.menuCheckArr.length;i++)
						{
							var node = zTree.getNodeByParam("tableId",listVue.menuCheckArr[i]);
							//(node.getChildren() == null || node.getChildren().length == 0)
							if(node != null && (node.children == null || node.children.length == 0))
							{
								zTree.checkNode(node, true, true);
								zTree.updateNode(node); 
							}
						}
						
						console.info(zTree);
						//禁用 zTree 所有节点 CheckBox 的勾选选操作。
						var nodes_UnEdit = zTree.getNodes();
						console.info(nodes_UnEdit);
						for(var i=0;i<nodes_UnEdit.length;i++)
						{
							zTree.setChkDisabled(nodes_UnEdit[i], true, true, true);
						}
					});
				} 
				catch (e) 
				{
					listVue.menuCheckArr = [];
				}
			}
		});
    }
    //------------------ 添加/编辑 菜单项 -----------------------//
    function theTypeChange()
    {
    	//资源URL 复位
    	this.resourceUIId = '';
    	//功能按钮 All 复位
    	this.childrenBtnList = [];
    }
    
    function resourceUIChange(event)
    {
    	var childrenBtnList = listVue.urlAndBtnRelationMap.get(listVue.resourceUIId);
    	listVue.childrenBtnList = childrenBtnList;
    	//console.info(childrenBtnList);
    }
    
    function checkChildrenBtnListHandle(value)
    {
		this.chooseChildrenBtnList = value;
	}
    
    //跳转编辑
  	function sm_Permission_RoleUIEdit()
    {
  		enterNewTabCloseCurrent(listVue.sm_Permission_Role.tableId, "角色授权修改", "sm/Sm_Permission_RoleUIEdit.shtml");
    }
  	
    //显示菜单项
    function showDetailMenu()
    {
    	if(listVue.parentUIIdForNowMenu == null)
    	{
    		
    	}
    	else
    	{
    		$('#theTypeSelect').removeAttr("disabled");
    	}
    	$('#addOrUpdateMenuDiv_RoleUIDetail').show();
    	$('#noChooseMenuDiv_RoleUIDetail').hide();
    }
    //隐藏菜单项
    function hideDetailMenu()
    {
    	$('#addOrUpdateMenuDiv_RoleUIDetail').hide();
    	$('#noChooseMenuDiv_RoleUIDetail').show();
    }
    
    //------------------ 获取菜单详情信息 Start---------------------//
    function getMenuDetail(menuId)
    {
    	var model = {
			interfaceVersion :19000101,
			tableId : menuId,
    	}
    	new ServerInterface(baseInfo.detailInterface).execute(model, function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				try {listVue.theType = jsonObj.sm_Permission_UIResource.theType;} catch (e) {listVue.theType = '';}
				try {listVue.theName = jsonObj.sm_Permission_UIResource.theName;} catch (e) {listVue.theName = null;}
				try {listVue.eCode = jsonObj.sm_Permission_UIResource.eCode;} catch (e) {listVue.eCode = null;}
				try {listVue.parentUI = jsonObj.sm_Permission_UIResource.parentUI;} catch (e) {listVue.parentUI = {tableId:'',theName:''};}
				try {listVue.theIndex = jsonObj.sm_Permission_UIResource.theIndex;} catch (e) {listVue.theIndex = null;}
				try {listVue.resourceUIId = jsonObj.sm_Permission_UIResource.resourceUIId;} catch (e) {listVue.resourceUIId = '';}
				try {listVue.childrenBtnList = jsonObj.sm_Permission_UIResource.childrenBtnList;} catch (e) {listVue.childrenBtnList = [];}
				try {listVue.iconPath = jsonObj.sm_Permission_UIResource.iconPath;} catch (e) {listVue.iconPath = null;}
				try {listVue.remark = jsonObj.sm_Permission_UIResource.remark;} catch (e) {listVue.remark = null;}
			}
		});
    }
    //------------------ 获取菜单详情信息 End----------------------//
    
	//------------------ zTree Start ---------------------//
	function hideRMenu() 
	{
		if (rMenu) rMenu.css({"visibility": "hidden"});
		$("body").unbind("mousedown", onBodyMouseDown);
	}
	function onBodyMouseDown(event)
	{
		if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length>0)) 
		{
			rMenu.css({"visibility" : "hidden"});
		}
	}
	
	//鼠标右键（单机），传入父级菜单Id
    function OnRightClick(event, treeId, treeNode) {
    	//console.info(treeNode);
		if(treeNode == null)
		{
			//用户没有选择任何节点
			console.info("这里是一级菜单");
			
			//复位
			listVue.reset();
			listVue.parentUIIdForNowMenu = null;
			hideDetailMenu();
		}
		else
		{
			//用户选择特定节点
			listVue.menuId = treeNode.tableId;
			listVue.parentUIIdForNowMenu = treeNode.tableId;
			showDetailMenu();
			getMenuDetail(listVue.menuId);
			console.info("这里是子级菜单:"+treeNode.name);
		}
		
		if (!treeNode && event.target.tagName.toLowerCase() != "button" && $(event.target).parents("a").length == 0) 
		{
			zTree.cancelSelectedNode();
			//showRMenu("root", event.clientX, event.clientY,treeNode);
		} 
		else if (treeNode && !treeNode.noR) 
		{
			zTree.selectNode(treeNode);
			//showRMenu("node", event.clientX, event.clientY,treeNode);
		}
	}
    function beforeClick(treeId, treeNode, clickFlag) {
		//console.info(clickFlag);
		return (treeNode.click != false);
	}
    
    //鼠标左键（单机），加载“菜单项”详情页
    function onClick(event, treeId, treeNode, clickFlag) {
    	console.log(treeNode);
		if(treeNode != null)
		{
			//加载用户所选菜单的详情
			listVue.menuId = treeNode.tableId;
			listVue.parentUIIdForNowMenu = treeNode.tableId;
			showDetailMenu();
			getMenuDetail(treeNode.tableId);
		}
		else
		{
			listVue.menuId = null;
			listVue.parentUIIdForNowMenu = null;
		}
    	
		var zTree = $.fn.zTree.getZTreeObj("treeDemo_RoleUIDetail");
    	zTree.expandNode(treeNode);
    	
    	var childNode = treeNode.children;
    	if(childNode.length != 0) {
    		for(var i = 0;i < childNode.length;i++) {
    			if(childNode[i].checked) {
    				var id = childNode[i].tId + "_span";
    				$("#"+id).css("color","#2160C7");
    			}
    		}
    	}
	}
	function onCheck(e, treeId, treeNode) 
	{
		//加载用户所选菜单的详情
		//将指定的节点选中
		zTree.cancelSelectedNode();
		zTree.selectNode(treeNode,true);
		listVue.menuId = treeNode.tableId;
		listVue.parentUIIdForNowMenu = treeNode.tableId;
		showDetailMenu();
		getMenuDetail(treeNode.tableId);
		
		listVue.menuCheckArr = [];
        var nodes = zTree.getCheckedNodes(true);
        
        for(var i=0;i<nodes.length;i++)
        {
        	//console.info(nodes[i].tableId);
        	listVue.menuCheckArr.push(nodes[i].tableId);
        }
        //console.info(listVue.menuCheckArr);
	}
    var zTree, rMenu;
    //-------------------- zTree End ---------------------//
    function getUrlList()
    {
    	var model = {
			interfaceVersion:19000101,
    	}
    	new ServerInterface(baseInfo.urlListInterface).execute(model, function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				//console.info(jsonObj);
				listVue.resourceUIList = jsonObj.sm_Permission_UIResourceList;
				for(var i=0;i<listVue.resourceUIList.length;i++)
				{
					var resourceUIId = listVue.resourceUIList[i].tableId;
					var childrenBtnList = listVue.resourceUIList[i].childrenBtnList;
					listVue.urlAndBtnRelationMap.put(resourceUIId, childrenBtnList);
				}
				//console.info(listVue.urlAndBtnRelationMap);
			}
		});
    }
    
    /*function editRoleUI()
    {
    	var model = {
			interfaceVersion : 19000101,
			tableId : listVue.roleId,//角色Id
			busiType : listVue.sm_Permission_Role.busiType,
			btnCheckArr : listVue.btnCheckArr,//用户勾选的菜单Id数组
			menuCheckArr : listVue.menuCheckArr,//用户勾选的功能按钮Id数组
    	}
    	console.info(model);
    	return;
    	new ServerInterface(baseInfo.editInterface).execute(model, function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				noty({"text":jsonObj.info,"type":"success","timeout":2000});
				listVue.sm_Permission_Role = jsonObj.sm_Permission_Role;
			}
		});
    }*/
    //------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#sm_Permission_RoleUIDetailDiv",
	"listInterface":"../Sm_Management_MenuList",
	"detailInterface":"../Sm_Management_MenuDetail",
	"urlListInterface":"../Sm_Permission_UIResource_UrlForSelect",
	//"editInterface":"../Sm_Permission_RoleUIEdit",
	"roleDetailInterface":"../Sm_Permission_Role_DetailForRoleUI",
});
