(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tg_PjRiskLetterModel: {},
			tableId : 1,
			selectItem : [],
			deleteItem: [],
			isAllSelected : false,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			tg_PjRiskLetterDetailList: [{
				theName: "11",email: "123"
			},{
				theName: "12",email: "453"
			}],
			theName: "",
			email: "",
			smAttachmentList:[],
            loadUploadList : [],
            showDelete : true,
            busiType : '21020304',
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			//更新
			getUpdateForm : getUpdateForm,
			update: update,
			listItemSelectHandle: listItemSelectHandle,
			indexMethod: indexMethod,
			changePageNumber: function (data) {
				console.log(data);
				if (detailVue.pageNumber != data) {
					detailVue.pageNumber = data;
					detailVue.refresh();
				}
			},
			changeCountPerPage : function (data) {
				console.log(data);
				if (detailVue.countPerPage != data) {
					detailVue.countPerPage = data;
					detailVue.refresh();
				}
			},
			tg_PjRiskLetterDel: tg_PjRiskLetterDel,
			handleEditSave: handleEditSave,
			LogModelClose: LogModelClose,
			addHandle: addHandle
		},
		computed:{
			 
		},
		components : {
			"my-uploadcomponent":fileUpload
		},
		watch:{
			selectItem : selectedItemChanged,
		}
	});

	//------------------------方法定义-开始------------------//
	function listItemSelectHandle(list) {
		detailVue.deleteItem = list;
		console.log(detailVue.tg_PjRiskLetterDetailList);
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
	function LogModelClose()
	{
		$(baseInfo.letterDetailModel).modal('hide');
	}
	
	function addHandle()
	{
		$(baseInfo.letterDetailModel).modal('show', {
		    backdrop :'static'
	    });
		detailVue.theName = "";
		detailVue.email = "";
		var errorCheck_div = document.getElementById("letterDetail");
	    var errorCheckList = errorCheck_div.querySelectorAll('[data-errorInfoId]');
	    $(errorCheckList[0]).removeClass("red-input");
		$("#addEmailPjRiskLetterId").css("display","none");
	}
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:detailVue.tableId,
			busiCode: detailVue.busiType,
		}
	}
	
	function tg_PjRiskLetterDel()
	{
		for (var i = 0; i < detailVue.deleteItem.length; i++) {
          var val = detailVue.deleteItem;
          val.forEach(function(val, index) {
        	  detailVue.tg_PjRiskLetterDetailList.forEach(function(v, i) {
              if (val.theName === v.theName) {
            	  detailVue.tg_PjRiskLetterDetailList.splice(i, 1);
            	  detailVue.deleteItem.splice(i, 1);
            	  tg_PjRiskLetterDel();
              }
            })
          })
        }
	}
	
	function handleEditSave(divStr)
	{
		var errorCheck_div = document.getElementById(divStr);
	    var errorCheckList = errorCheck_div.querySelectorAll('[data-errorInfoId]');
		var isPass = errorCheckForAll(divStr);
        if (!isPass) {
            return;
        }
        var editFlag = true;
        if(detailVue.tg_PjRiskLetterDetailList.length>0){
        	detailVue.tg_PjRiskLetterDetailList.forEach(function(item){
        		if(item.email == detailVue.email){
        			$(errorCheckList[0]).addClass("red-input");
        			$("#editEmailPjRiskLetterId").css("display","block");
        			$("#editEmailPjRiskLetterId").html("该邮箱已存在");
        			editFlag = false;
        			return;
        			
        		}
        	})
        }
        if(editFlag == true){
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
    		$(baseInfo.letterDetailModel).modal('hide');
        }
	}

	//详情操作--------------
	function refresh()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var list = tableIdStr.split("_");
		tableIdStr = list[2];
		detailVue.tableId = tableIdStr;
		if (this.tableId == null || this.tableId < 1) 
		{
			return;
		}

		getDetail();
	}

	function getDetail()
	{
		new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				detailVue.tg_PjRiskLetterModel = jsonObj.tg_PjRiskLetter;
				detailVue.tg_PjRiskLetterDetailList = jsonObj.tg_PjRiskLetterReceiverList;
				detailVue.tg_PjRiskLetterDetailList.forEach(function(item, index) {
					detailVue.$nextTick(function() {
						detailVue.$refs.moviesTable.toggleRowSelection(detailVue.tg_PjRiskLetterDetailList[index], true);
					})
				});
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
			}
		});
	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getUpdateForm()
	{
		var list = JSON.stringify(detailVue.deleteItem);
		var fileUploadList = detailVue.$refs.listenUploadData.uploadData;
		fileUploadList = JSON.stringify(fileUploadList);
		return {
			interfaceVersion:this.interfaceVersion,
			tableId: detailVue.tableId,
			busiCode: detailVue.busiType,
			deliveryCompany: detailVue.tg_PjRiskLetterModel.deliveryCompany,
			riskNotification: detailVue.tg_PjRiskLetterModel.riskNotification,
			basicSituation: detailVue.tg_PjRiskLetterModel.basicSituation,
			riskAssessment: detailVue.tg_PjRiskLetterModel.riskAssessment,
			tg_PjRiskLetterReceiverList: list,
			smAttachmentList:fileUploadList,
		}
	}

	function update()
	{
		new ServerInterface(baseInfo.updateInterface).execute(detailVue.getUpdateForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				generalSuccessModal();
				enterNext2Tab(detailVue.tableId, '项目风险函详情', 'tg/Tg_PjRiskLetterDetail.shtml',detailVue.tableId+"21020304");
			}
		});
	}
	
	function initData()
	{
		
	}
	// 添加日期控件
	laydate.render({
		elem: '#riskLetter',
	});
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tg_PjRiskLetterEditDiv",
	"letterDetailModel":"#letterDetail",
	"detailInterface":"../Tg_PjRiskLetterDetail",
	"updateInterface":"../Tg_PjRiskLetterUpdate"
});
