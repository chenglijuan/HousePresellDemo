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
					enable: false,//checkBox close
				},
				callback: {
					onRightClick: OnRightClick,//右击
					beforeClick: beforeClick,
					onClick: onClick,//单机
				}
			},
			//菜单项(添加、编辑)
			theType:2,//虚拟菜单
			theName:null,
			busiCode:"",
			busiCodeList:[],//业务编码列表
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
			
			//上传路径
			loadUploadObj:{"data":{}},
			node : {},
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
				//listVue.pageNumber = data;
			},
            
            //zTree 右侧弹框 click 事件声明
            addNextMenu : addNextMenu,
            delNowMenu : delNowMenu,
            
            //菜单项(添加、编辑)
            theTypeChange : theTypeChange,
            resourceUIChange : resourceUIChange,
            checkChildrenBtnListHandle : checkChildrenBtnListHandle,
            addOrUpdate : addOrUpdate,
            cancelSave : cancelSave,
            
            //图片上传
            handleAvatarSuccess : handleAvatarSuccess,
            beforeAvatarUpload : beforeAvatarUpload,
            
            //业务编码
            getBusiCodeList : getBusiCodeList, 
            baseParameterChange:baseParameterChange,
            delMenu : delMenu,
            showCancelModal : showCancelModal,
		},
		components : {
            'vue-select': VueSelect
		},
	});
	//------------------------方法定义-开始------------------//
	//重置
	function reset()
	{
		//虚拟菜单
		this.theType = 2;
		this.theName = null;
		this.busiCode = "";
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
	function refresh(sm_Permission_UIResource_New)
	{
		new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				listVue.zNodes = jsonObj.sm_Management_MenuList;
				/*console.info(listVue.zNodes);*/
				
				$(document).ready(function(){
					$.fn.zTree.init($("#treeDemo_MenuList"), listVue.setting, listVue.zNodes);
					zTree = $.fn.zTree.getZTreeObj("treeDemo_MenuList");
					//console.info(zTree.getNodes());
					rMenu = $("#rMenu_MenuList");
					
					//回显用户所选菜单的详情，并动态选中并展开所选菜单
					if(sm_Permission_UIResource_New != null)
					{
						listVue.menuId = sm_Permission_UIResource_New.tableId;
						listVue.parentUIIdForNowMenu = sm_Permission_UIResource_New.tableId;
						showDetailMenu();
						getMenuDetail(sm_Permission_UIResource_New.tableId);
						
						var node = zTree.getNodeByParam("id",sm_Permission_UIResource_New.id,null);
						console.info(zTree);
						console.info(node);
						console.info(sm_Permission_UIResource_New);
						zTree.selectNode(node,true);//指定选中ID的节点
						zTree.expandNode(node, true, false);//指定选中ID节点展开
					}
				});
				
				//动态跳转到锚点处，id="top"
				document.getElementById('sm_Management_MenuDiv').scrollIntoView();
			}
		});
	}
	
	//列表操作------------搜索
	function search()
	{
		//listVue.pageNumber = 1;
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
    	//获取上传图片路径
    	loadUpload();
    	
    	getBusiCodeList();
    	getUrlList();
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
    
    function showCancelModal(){
    	$("#confirmCancelModal").modal({
			 backdrop: 'static'
		});
    }
    
    function cancelSave()
    {
    	$("#confirmCancelModal").modal('hide');
		hideDetailMenu();
    }
    function handleAvatarSuccess(res, file) {
    	//this.iconPath = URL.createObjectURL(file.raw);
    	/*console.info(file);*/
    	console.info(res.data[0].url);
    	listVue.iconPath = res.data[0].url;
    }
    function beforeAvatarUpload(file) {
    	console.info(file.type);
    	var isJPG = isImg(file.type); 
    	var isLt2M = file.size / 1024 / 1024 < 2;
        
        if (!isJPG) 
        {
        	generalErrorModal("","您上传的图片格式不正确");
        	//noty({"text":"您上传的图片格式不正确","type":"error","timeout":2000});
        }
        
        if (!isLt2M) 
        {
        	generalErrorModal("","上传头像图片大小不能超过 2MB!");
        	//noty({"text":"","type":"error","timeout":2000});
        }
        return isJPG && isLt2M;
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
    	$('#addOrUpdateMenuDiv').show();
    	$('#noChooseMenuDiv').hide();
    }
    //隐藏菜单项
    function hideDetailMenu()
    {
    	$('#addOrUpdateMenuDiv').hide();
    	$('#noChooseMenuDiv').show();
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
				generalErrorModal(jsonObj,jsonObj.info);
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				var canChangeTheType = jsonObj.sm_Permission_UIResource.canChangeTheType;
				if(canChangeTheType == null || !canChangeTheType)
				{
					$('#theTypeSelect').attr("disabled",true);
				}
				else
				{
					$('#theTypeSelect').removeAttr("disabled");
				}
				
				try {listVue.theType = jsonObj.sm_Permission_UIResource.theType;} catch (e) {listVue.theType = '';}
				try {listVue.theName = jsonObj.sm_Permission_UIResource.theName;} catch (e) {listVue.theName = null;}
				try {listVue.busiCode = jsonObj.sm_Permission_UIResource.busiCode;} catch (e) {listVue.busiCode = "";}
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
    function addNextMenu()
    {
    	listVue.reset();
    	showDetailMenu();
    	hideRMenu();
    	
    	var nodes = zTree.getSelectedNodes();
    	if (nodes != null && nodes != undefined && nodes.length > 0)
		{
    		var node = nodes[0];
    		var name = node.name;
    		var tableId = node.tableId;
    		listVue.parentUI = {tableId:tableId,theName:name};
		}
		
    	console.info("展开右侧新增/编辑Div");
    }
    function delNowMenu()
    {
    	hideRMenu();
    	
    	var nodes = zTree.getSelectedNodes();
    	
		if (nodes == null || nodes == undefined || !nodes || nodes.length < 1)
		{
			return;
		}
		var node = nodes[0];
		listVue.node = node;
		var name = node.name;
		var theType = node.theType;
		if (theType == 1) 
		{
			$("#deletSmManagementMenuModal").modal({
				 backdrop: 'static'
			});
			$("#deletSmManagementMenuMsg").html("请谨慎操作，您确认要删除实体菜单“"+name+"”么？");
			//实体菜单
			//showDelNoty("请谨慎操作，您确认要删除实体菜单“"+name+"”么？",node);
		} 
		else if(theType == 2)
		{
			$("#deletSmManagementMenuModal").modal({
				 backdrop: 'static'
			});
			$("#deletSmManagementMenuMsg").html("请谨慎操作，您确认要删除虚拟菜单“"+name+"”么？");
			//generalSelectModal(deleteMenu(node), "请谨慎操作，您确认要删除虚拟菜单“"+name+"”么？");
			//虚拟菜单
			//showDelNoty("请谨慎操作，您确认要删除虚拟菜单“"+name+"”么？",node);
		}
		else
		{
			generalErrorModal("","菜单类型错误");
		//	noty({"text":"菜单类型错误","type":"error","timeout":2000});
		}
    }
    function showDelNoty(text,node)
	{
		noty({
			layout:'center',
			modal:true,
			text:text,
			type:"confirm",
			buttons:[
				{
					addClass:"btn btn-primary",
					text:"确定",
					onClick:function($noty){
						$noty.close();
						
						//执行删除操作
						//zTree.removeNode(node);
						deleteMenu(node);
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
    
    function delMenu(){
    	$("#deletSmManagementMenuModal").modal('hide');
    	deleteMenu(listVue.node);
    }
    
    function deleteMenu(node)
	{
		//console.info(node);
    	var model = {
			interfaceVersion : 19000101,
			tableId : node.tableId,//菜单Id
    	}
    	/*console.info(model);
    	return;*/
		new ServerInterface(baseInfo.deleteInterface).execute(model, function(jsonObj){
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
//				/noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				//refresh();
				//noty({"text":jsonObj.info,"type":"success","timeout":2000});
				generalSuccessModal()
				Vue.nextTick(function () {
					//恢复父节点样式
					zTree.removeNode(node);
					var pNode = zTree.getNodeByParam("id", node.pId, null);
					if(pNode != null)
					{
						pNode.isParent = true;
						pNode.open = true;
						zTree.updateNode(pNode);
					}
					hideDetailMenu();
				});
			}
		});
	}
    function showRMenu(type, x, y,treeNode) 
	{
		$("#rMenu_MenuList ul").show();
		
		//用户选择实体菜单，则无法添加下级菜单
		if(treeNode != null)
		{
			var theType = treeNode.theType;
			if(theType == 1)
			{
				//实体菜单
				$("#m_add_MenuList").hide();
			}
			else
			{
				$("#m_add_MenuList").show();
			}
		}
		else
		{
			$("#m_add_MenuList").show();
		}
		
		$("#m_del_MenuList").hide();
		if(treeNode != null && (treeNode.children == null || treeNode.children.length == 0))
		{
			$("#m_del_MenuList").show();
		}
		
		rMenu.css({"top":y+"px", "left":x+"px", "visibility":"visible"});

		$("body").bind("mousedown", onBodyMouseDown);
	}
	function hideRMenu() 
	{
		if (rMenu) rMenu.css({"visibility": "hidden"});
		$("body").unbind("mousedown", onBodyMouseDown);
	}
	function onBodyMouseDown(event)
	{
		if (!(event.target.id == "rMenu_MenuList" || $(event.target).parents("#rMenu_MenuList").length>0)) 
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
			$('#theTypeSelect').removeAttr("disabled");
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
			//console.info(event.clientX+","+event.clientY);
			showRMenu("root", event.clientX, event.clientY,treeNode);
		} 
		else if (treeNode && !treeNode.noR) 
		{
			zTree.selectNode(treeNode);
			//console.info(event.clientX+","+event.clientY);
			showRMenu("node", event.clientX, event.clientY,treeNode);
		}
	}
    function beforeClick(treeId, treeNode, clickFlag) {
		//console.info(clickFlag);
		return (treeNode.click != false);
	}
    
    //鼠标左键（单机），加载“菜单项”详情页
    function onClick(event, treeId, treeNode, clickFlag) {
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
    	
		var zTree = $.fn.zTree.getZTreeObj("treeDemo_MenuList");
    	zTree.expandNode(treeNode);
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
				generalErrorModal(jsonObj,jsonObj.info);
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});
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
    function getBusiCodeList()
    {
    	generalGetParamList("1",function (list) {
            for(var i =0;i<list.length;i++){
                var item=list[i]
                item.theName=item.theValue+"-"+item.theName
            }
            listVue.busiCodeList =list;
        })
        
    	/*var model = {
			interfaceVersion:19000101,
			parametertype:1,//业务编码
			pageNumber:1,
			countPerPage:10000,
    	}
    	new ServerInterface(baseInfo.busiCodeListInterface).execute(model, function(jsonObj){
    		if(jsonObj.result != "success")
    		{
    			noty({"text":jsonObj.info,"type":"error","timeout":2000});
    		}
    		else
    		{
    			listVue.busiCodeList = jsonObj.sm_BaseParameterList;
    			//console.info(jsonObj);
    		}
    	});*/
    }
    function baseParameterChange(date)
    {
    	listVue.busiCode = date.busiCode
    }
    function addOrUpdate()
    {
    	var model = {
			interfaceVersion : 19000101,
			tableId : listVue.menuId,//菜单Id，没有则系统认为是新增
			parentUIIdForNowMenu : listVue.parentUIIdForNowMenu,
			theType : listVue.theType,//菜单类型
			theName : listVue.theName,//菜单名称
			busiCode : listVue.busiCode,//业务编码
			theIndexStr : listVue.theIndex,
			resourceUIId : listVue.resourceUIId,
			iconPath : listVue.iconPath,
			remark : listVue.remark,//备注
    	}
//    	console.info(model);
//    	return;
    	new ServerInterface(baseInfo.addOrUpdateInterface).execute(model, function(jsonObj){
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				generalSuccessModal()
//				/noty({"text":jsonObj.info,"type":"success","timeout":2000});
				
				//添加
				/*if(listVue.menuId == null)
				{
					var sm_Permission_UIResource_New = jsonObj.sm_Permission_UIResource_New;
					
					//复位
					reset();
					
					var newNode = {
						id : sm_Permission_UIResource_New.id,
						isParent : sm_Permission_UIResource_New.isParent,
						name : sm_Permission_UIResource_New.name,
						pId : sm_Permission_UIResource_New.pId,
						parentUIId : sm_Permission_UIResource_New.parentUIId,
						tableId : sm_Permission_UIResource_New.tableId,
						theType : sm_Permission_UIResource_New.theType,
					};
					if (zTree.getSelectedNodes()[0]) 
					{
						newNode.checked = zTree.getSelectedNodes()[0].checked;
						zTree.addNodes(zTree.getSelectedNodes()[0], newNode);
					} 
					else 
					{
						zTree.addNodes(null, newNode);
					}
				}*/
				
				//重新加载（为了能使排序有效）
				var sm_Permission_UIResource_New = jsonObj.sm_Permission_UIResource_New;
				refresh(sm_Permission_UIResource_New);
			}
		});
    }
    //------------------------方法定义-结束------------------//
    function isImg(imgType) { // 判断是否为图片
		  if(imgType == 'image/BMP'  || imgType == 'image/bmp'  ||
		     imgType == 'image/JPEG' || imgType == 'image/jpeg' ||
		     imgType == 'image/GIF'  || imgType == 'image/gif'  ||
		     imgType == 'image/PNG'  || imgType == 'image/png'  ||
		     imgType == 'image/TIFF' || imgType == 'image/tiff' ||
		     imgType == 'image/JPG'  || imgType == 'image/jpg'  ||
		     imgType == 'image/TGA'  || imgType == 'image/tga'  ||
		     imgType == 'image/EPS'  || imgType == 'image/eps'
		  ) 
		  {
		      return true;
		  }
	}
    /********* 附件材料 相关 *********/
    function loadUpload(){
        model = {
            pageNumber : '0',
            busiType : '010104',
            interfaceVersion:listVue.interfaceVersion
        };

        new ServerInterface(baseInfo.loadUploadInterface).execute(model, function(jsonObj)
        {
            console.log(jsonObj);

            if(jsonObj.result != "success")
            {
                //listVue.errMsg = jsonObj.info;
            	//noty({"text":jsonObj.info,"type":"error","timeout":2000});
            	generalErrorModal(jsonObj,jsonObj.info);
            }
            else
            {
            	var loadUploadList = jsonObj.sm_AttachmentCfgList;
            	if(loadUploadList != null && loadUploadList.length > 0)
            	{
            		listVue.loadUploadObj = loadUploadList[0];
            	}
            }
        });
    }
	//------------------------数据初始化-开始----------------//
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#sm_Management_MenuDiv",
	"addDivId":"#addModal",
	"listInterface":"../Sm_Management_MenuList",
	"detailInterface":"../Sm_Management_MenuDetail",
	"urlListInterface":"../Sm_Permission_UIResource_UrlForSelect",
	"addOrUpdateInterface":"../Sm_Management_MenuAddOrUpdate",
	"deleteInterface":"../Sm_Management_MenuDelete",
	"busiCodeListInterface":"../Sm_BaseParameterForSelect",
	//材料附件
	"loadUploadInterface":"../Sm_AttachmentCfgList"
});
