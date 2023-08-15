(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			tg_PjRiskLetterModel: {},
			projectId: "",
			projectList:[],
			developerId:"",
			developerList:[],
			areaId:"",
			areaList:[],
			unitId:"",
			unitList:[],
			buildId:"",
			buildList:[],
			loadUploadList: [],
            showDelete : true,
            busiType : '21020304',
            tg_PjRiskLetterDetailList: [],
            selectItem : [],
			deleteItem: [],
			isAllSelected : false,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			theName: "",
			email: "",
			riskNotification: "",
			basicSituation: "",
			riskAssessment: "",
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			//添加
			getAddForm : getAddForm,
			addHandle: add,
			lookProject:lookProject,
			getUploadForm : getUploadForm,
			loadUpload : loadUpload,
			listItemSelectHandle: listItemSelectHandle,
			indexMethod: indexMethod,
			changePageNumber: function (data) {
				if (detailVue.pageNumber != data) {
					detailVue.pageNumber = data;
					detailVue.refresh();
				}
			},
			changeCountPerPage : function (data) {
				if (detailVue.countPerPage != data) {
					detailVue.countPerPage = data;
					detailVue.refresh();
				}
			},
			getBankData: getBankData,
			update: update,
			handleEditSave: handleEditSave,
			LogModelClose: LogModelClose,
			tg_PjRiskLetterDel: tg_PjRiskLetterDel,
			getProjectId: function (data){
				this.projectId = data.tableId;
				detailVue.lookProject();
			},
		},
		computed:{
			 
		},
		components : {
			"my-uploadcomponent":fileUpload,
			'vue-select': VueSelect,
		},
		watch:{
			selectItem : selectedItemChanged,
		}
	});

	//------------------------方法定义-开始------------------//
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
		}
	}
	function listItemSelectHandle(list) {
		detailVue.deleteItem = list;
		generalListItemSelectHandle(detailVue,list)
 	}
	function indexMethod(index) {
		return generalIndexMethod(index, detailVue)
	}
	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		detailVue.isAllSelected = (detailVue.tg_PjRiskLetterDetailList.length > 0)
		&&	(detailVue.selectItem.length == detailVue.tg_PjRiskLetterDetailList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(detailVue.selectItem.length == detailVue.tg_PjRiskLetterDetailList.length)
	    {
	    	detailVue.selectItem = [];
	    }
	    else
	    {
	    	detailVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	detailVue.tg_PjRiskLetterDetailList.forEach(function(item) {
	    		detailVue.selectItem.push(item.tableId);
	    	});
	    }
	}
	
	function lookProject()
	{
		var model = {
			interfaceVersion:this.interfaceVersion,
			tableId: detailVue.projectId,	
		}
		new ServerInterface(baseInfo.lookProjectInterface).execute(model, function(jsonObj) {
			if(jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
	            // 项目数据加载
				detailVue.developerId = jsonObj.empj_ProjectInfo.developCompanyName;
				detailVue.areaId = jsonObj.empj_ProjectInfo.cityRegionName;
			}
		});
		new ServerInterface(baseInfo.lookLetterInterface).execute(model, function(jsonObj) {
			if(jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
				detailVue.riskAssessment = jsonObj.riskAssessment;
				detailVue.riskNotification = jsonObj.riskNotification;
			}
		});
	}
	// 加载项目数据
	function getBankData() 
	{
		var model = {
				interfaceVersion:this.interfaceVersion,
		}
		new ServerInterface(baseInfo.getInterface).execute(model, function(jsonObj) {
			if(jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
                // 项目数据加载
				detailVue.projectList = jsonObj.empj_ProjectInfoList;
				detailVue.areaId = jsonObj.empj_ProjectInfoList.cityRegionName;
				detailVue.developerId = jsonObj.empj_ProjectInfoList.developCompanyName;
			}
		});
	}

	//详情操作--------------
	function refresh()
	{
		detailVue.getBankData();
		
	}
	
	function getBuilding()
	{
		new ServerInterface(baseInfo.loadBuildingInterface).execute(detailVue.getSearchForm(), function(jsonObj)
				{
					if(jsonObj.result != "success")
					{
						noty({"text":jsonObj.info,"type":"error","timeout":2000});
					}
					else
					{
						detailVue.buildList = jsonObj.buildList;
					}
				});
	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getAddForm()
	{
		var fileUploadList = detailVue.$refs.listenUploadData.uploadData;
		fileUploadList = JSON.stringify(fileUploadList);
		var list = JSON.stringify(detailVue.deleteItem);
		return {
			interfaceVersion:this.interfaceVersion,
			projectId:detailVue.projectId,
			busiCode:detailVue.busiType,
			deliveryCompany:detailVue.unitId,
			/*releaseDate:document.getElementById("warnsDate").value,*/
			riskNotification:detailVue.riskNotification,
			basicSituation:detailVue.basicSituation,
			riskAssessment:detailVue.riskAssessment,
			tg_PjRiskLetterReceiverList: list,
			smAttachmentList:fileUploadList
		}
	}

	function add()
	{
		$(baseInfo.letterAddModel).modal('show', {
		    backdrop :'static'
	    });
		var errorCheck_div = document.getElementById("letterAddDetail");
	    var errorCheckList = errorCheck_div.querySelectorAll('[data-errorInfoId]');
	    $(errorCheckList[0]).removeClass("red-input");
		$("#addEmailPjRiskLetterId").css("display","none");
		detailVue.theName = "";
		detailVue.email = "";
		
	}
	
	function update()
	{
		new ServerInterface(baseInfo.addInterface).execute(detailVue.getAddForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				enterNext2Tab(jsonObj.tableId, '项目风险函详情', 'tg/Tg_PjRiskLetterDetail.shtml',jsonObj.tableId+"21020304");
			}
		});
	}
	
	function handleEditSave(divStr)
	{
		var errorCheck_div = document.getElementById(divStr);
	    var errorCheckList = errorCheck_div.querySelectorAll('[data-errorInfoId]');
		var isPass = errorCheckForAll(divStr);
        if (!isPass) {
            return;
        }
        var addFlag = true;
        if(detailVue.tg_PjRiskLetterDetailList.length>0){
         	var tg_PjRiskLetterDetailList = detailVue.tg_PjRiskLetterDetailList;
        	for(var i = 0;i<tg_PjRiskLetterDetailList.length;i++){
        		if(detailVue.email == tg_PjRiskLetterDetailList[i].email){
        			$(errorCheckList[0]).addClass("red-input");
        			$("#addEmailPjRiskLetterId").css("display","block");
        			$("#addEmailPjRiskLetterId").html("该邮箱已存在");
        			addFlag = false;
        			return;
        		}
        	}
        }
        if(addFlag == true){
        	var model = {
        			theName:detailVue.theName,
        			email:detailVue.email,
        	}
        	detailVue.tg_PjRiskLetterDetailList.push(model);
        	detailVue.tg_PjRiskLetterDetailList.forEach(function(item, index) {
        		detailVue.$nextTick(function() {
        			detailVue.$refs.moviesTable.toggleRowSelection(detailVue.tg_PjRiskLetterDetailList[index], true);
        		})
        	});
        	$(baseInfo.letterAddModel).modal('hide');
        }
	}
	
	function LogModelClose()
	{
		$(baseInfo.letterAddModel).modal('hide');
	}
	
	function tg_PjRiskLetterDel()
	{
		var val = detailVue.deleteItem;
		var list = detailVue.tg_PjRiskLetterDetailList;
		for (var i = 0; i < val.length; i++) {
          for(var j = 0;j<list.length;j++){
        	  if(val[i].theName == list[j].theName){
        		  detailVue.deleteItem.splice(i,1);
        		  detailVue.tg_PjRiskLetterDetailList.splice(j, 1);
            	  tg_PjRiskLetterDel()
        	  }
          }
        }
	}
	
	function initData()
	{
		
	}
	
	//列表操作-----------------------获取附件参数
	function getUploadForm(){
		return{
			pageNumber : '0',
			busiType : detailVue.busiType,
			interfaceVersion:this.interfaceVersion
		}
	}
	//列表操作-----------------------页面加载显示附件类型
	function loadUpload(){
		new ServerInterface(baseInfo.loadUploadInterface).execute(detailVue.getUploadForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				$(baseInfo.errorModel).modal("show",{
					backdrop :'static'
				});
			}
			else
			{
				detailVue.loadUploadList = jsonObj.sm_AttachmentCfgList;
			}
		});
	}
	
	laydate.render({
	   elem: '#warnsDate',
	});
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.refresh();
	detailVue.initData();
	detailVue.loadUpload();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#tg_PjRiskLetterAddDiv",
	"letterAddModel":"#letterAddDetail",
	"detailInterface":"../Tg_PjRiskLetterDetail",
	"addInterface":"../Tg_PjRiskLetterAdd",
	"loadProjectInterface":"../",
	"loadUnitInterface":"../",
	"loadBuildingInterface":"../",
	"loadUploadInterface":"../Sm_AttachmentCfgList",
	"getInterface":"../Empj_ProjectInfoList",
	"lookProjectInterface":"../Empj_ProjectInfoDetail",
	"lookLetterInterface" : "../Tg_PjRiskLetterReload",
});
