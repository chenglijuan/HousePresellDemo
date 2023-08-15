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
			userStartId:null,
			sumCount: "0",
			sumAmount: "0.00",
			userStartList:[],
			userRecordId:null,
			userRecordList:[],
			billTimeStamp:"",//搜索的时间
			tgpf_CyberBankStatementList:[],
			deleteArr:[],//删除时存入要删除的tableId
			errMsg : "",//操作失败提示信息
			cyberBankEdit : true,
			cyberBankDel : true,
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getExportForm : getExportForm,//获取导出excel参数
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			tgpf_CyberBankStatementDelOne : tgpf_CyberBankStatementDelOne,
			tgpf_CyberBankStatementDel : tgpf_CyberBankStatementDel,
			search:search,
			checkAllClicked : checkAllClicked,
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
			cyberBankStatementAddHandle : cyberBankStatementAddHandle,//新增方法
			cyberBankStatementEditHandle : cyberBankStatementEditHandle,//修改方法
			checkCheckBox : checkCheckBox,//当选中checkbox时将tableId push 到selectItem
			cyberBankStatementDetailHandle : cyberBankStatementDetailHandle,//详情方法
			indexMethod:indexMethod,//左侧序号
			resetRsearch : resetRsearch,
			exportForm : exportForm,//导出excel
			showDelModal : showDelModal,//显示提示删除模态框
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue
		},
		watch:{
			//pageNumber : refresh,
			selectItem : selectedItemChanged,
		}
	});

	//------------------------方法定义-开始------------------//
	function getExportForm(){
		return{
		   interfaceVersion:this.interfaceVersion,	
		}
	}
	//列表操作-----------------------导出excel
	function exportForm(){
		new ServerInterface(baseInfo.exportInterface).execute(listVue.getSearchForm(), function(jsonObj){
			if (jsonObj.result != "success") 
			{
				generalErrorModal(jsonObj, jsonObj.info);
			} 
			else 
			{
				window.location.href="../"+jsonObj.fileURL;
			}
		});
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
			userStartId:this.userStartId,
			userRecordId:this.userRecordId,
			billTimeStamp:this.billTimeStamp,
		}
	}
	//列表操作--------------获取“删除资源”表单参数
	function getDeleteForm()
	{
		this.deleteArr = [];
		listVue.selectItem.forEach(function(item){
			listVue.deleteArr.push(item.tableId);
		})
		return{
			interfaceVersion:this.interfaceVersion,
			idArr:this.deleteArr
		}
	}
	/*//列表操作--------------获取“新增”表单参数
	function getAddForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			busiState:this.busiState,
			eCode:this.eCode,
			userStartId:this.userStartId,
			uploadTimeStamp:this.uploadTimeStamp,
			lastUpdateTimeStamp:this.lastUpdateTimeStamp,
			userRecordId:this.userRecordId,
			recordTimeStamp:this.recordTimeStamp,
			theNameOfBank:this.theNameOfBank,
			theAccountOfBankAccountEscrowed:this.theAccountOfBankAccountEscrowed,
			theNameOfBankAccountEscrowed:this.theNameOfBankAccountEscrowed,
			theNameOfBankBranch:this.theNameOfBankBranch,
			reconciliationState:this.reconciliationState,
			reconciliationUser:this.reconciliationUser,
			orgFilePath:this.orgFilePath,
			accountOfBankAccountEscrowed:this.accountOfBankAccountEscrowed
		}
	}
	//列表操作--------------获取“更新”表单参数
	function getUpdateForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			theState:this.theState,
			busiState:this.busiState,
			eCode:this.eCode,
			userStartId:this.userStartId,
			uploadTimeStamp:this.uploadTimeStamp,
			lastUpdateTimeStamp:this.lastUpdateTimeStamp,
			userRecordId:this.userRecordId,
			recordTimeStamp:this.recordTimeStamp,
			theNameOfBank:this.theNameOfBank,
			theAccountOfBankAccountEscrowed:this.theAccountOfBankAccountEscrowed,
			theNameOfBankAccountEscrowed:this.theNameOfBankAccountEscrowed,
			theNameOfBankBranch:this.theNameOfBankBranch,
			reconciliationState:this.reconciliationState,
			reconciliationUser:this.reconciliationUser,
			orgFilePath:this.orgFilePath,
			accountOfBankAccountEscrowed:this.accountOfBankAccountEscrowed
		}
	}
	*/
	/**
	 * 初始化日期插件
	 */
	laydate.render({
	  elem: '#cyberBankSearch',
	  done: function(value, date, endDate){
	    listVue.billTimeStamp = value;
	  }
	});
	
	//列表操作----------------重置
	function resetRsearch(){
		listVue.billTimeStamp = "";
		listVue.keyword = ""
	}
	//列表操作-----------显示删除模态框
	function showDelModal(){
		if(listVue.getDeleteForm().idArr.length == "0"){
			noty({"text":"请至少选择一项进行删除","type":"error","timeout":2000});
		}else{
			$(baseInfo.deleteDivId).modal({
				backdrop :'static'
			});
		}
		
	}
	//列表操作-----------------批量删除
	function tgpf_CyberBankStatementDel()
	{
		$(baseInfo.deleteDivId).modal("hide");
		new ServerInterface(baseInfo.deleteInterface).execute(listVue.getDeleteForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				listVue.errMsg = jsonObj.info;
			}
			else
			{
				$(baseInfo.sdModelDivId).modal({
					backdrop :'static'
				});
				listVue.selectItem = [];
				refresh();
			}
		});
		
	}
	//列表操作------------------单个删除
	function tgpf_CyberBankStatementDelOne(tgpf_CyberBankStatementId)
	{
		$(baseInfo.deleteDivId).modal("hide");
		var model = {
			interfaceVersion :19000101,
			idArr:[tgpf_CyberBankStatementId],
		};
		
		noty({
			layout:'center',
			modal:true,
			text:"确认删除吗？",
			type:"confirm",
			buttons:[
				{
					addClass:"btn btn-primary",
					text:"确定",
					onClick:function($noty){
						$noty.close();
						new ServerInterface(baseInfo.deleteInterface).execute(model , function(jsonObj){
							if(jsonObj.result != "success")
							{
								noty({"text":jsonObj.info,"type":"error","timeout":2000});
							}
							else
							{
								refresh();
							}
						});
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
	
	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		listVue.isAllSelected = (listVue.tgpf_CyberBankStatementList.length > 0)
		&&	(listVue.selectItem.length == listVue.tgpf_CyberBankStatementList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgpf_CyberBankStatementList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgpf_CyberBankStatementList.forEach(function(item) {
	    		listVue.selectItem.push(item.tableId);
	    	});
	    }
	}
	//列表操作--------------刷新
	function refresh()
	{
		new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				listVue.errMsg = jsonObj.info;
			}
			else
			{
				listVue.tgpf_CyberBankStatementList=jsonObj.tgpf_CyberBankStatementList;
				listVue.tgpf_CyberBankStatementList.forEach(function(item){
					item.transactionAmount = addThousands(item.transactionAmount); 
					item.transactionCount = addThousandsCount(item.transactionCount);
				})
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.keyword=jsonObj.keyword;
				listVue.totalCount = jsonObj.totalCount;
				listVue.selectedItem=[];
				listVue.sumCount = jsonObj.sumCount;
				listVue.sumAmount = addThousands(jsonObj.sumAmount);
				//动态跳转到锚点处，id="top"
				document.getElementById('tgpf_CyberBankStatementListDiv').scrollIntoView();
			}
		});
	}
	
	//列表操作------------搜索
	function search()
	{
		listVue.pageNumber = 1;
		refresh();
	}
	
	//选中按钮，将选中的tableId push 到selectItem
	function checkCheckBox(item){
		if(item.length == "1"){
			listVue.cyberBankEdit = false; 
		}else{
			listVue.cyberBankEdit = true; 
		}
		if(item.length >= "1"){
			listVue.cyberBankDel = false; 
		}else{
			listVue.cyberBankDel = true; 
		}
		listVue.selectItem  = item;
	}
	
	//新增方法
	function cyberBankStatementAddHandle(){
		enterNewTab('', '新增网银上传', 'tgpf/Tgpf_CyberBankStatementAdd.shtml');
		/*$("#tabContainer").data("tabs").addTab({
			id: 'Tgpf_CyberBankStatementAdd', 
			text: '新增网银上传', 
			closeable: true, 
			url: 'tgpf/Tgpf_CyberBankStatementAdd.shtml'
		});*/
	}
	
	//修改方法
	function cyberBankStatementEditHandle(){
		enterNextTab(listVue.selectItem[0].tableId, '修改网银上传数据', 'tgpf/Tgpf_CyberBankStatementEdit.shtml',listVue.selectItem[0].tableId+"200201");
		
		/*if(listVue.selectItem.length == "0"){
			noty({"text":"请至少选择一项数据进行修改","type":"error","timeout":2000});
		}else if(listVue.selectItem.length == "1"){
			$("#tabContainer").data("tabs").addTab({
				id: listVue.selectItem[0].tableId, 
				text: '修改网银上传数据', 
				closeable: true, 
				url: 'tgpf/Tgpf_CyberBankStatementEdit.shtml'
			});
		}else{
			noty({"text":"修改数据只能单个进行修改","type":"error","timeout":2000});
		}*/
		
	}
	//查看详情方法
	function cyberBankStatementDetailHandle(tableId){
		enterNextTab(tableId, '修改网银上传数据', 'tgpf/Tgpf_CyberBankStatementDetail.shtml',tableId+"200201");
		/*$("#tabContainer").data("tabs").addTab({
			id: tableId, 
			text: '网银上传数据详情', 
			closeable: true, 
			url: 'tgpf/Tgpf_CyberBankStatementDetail.shtml'
		});*/
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
		initButtonList();
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	//listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tgpf_CyberBankStatementListDiv",
	"deleteDivId":"#deleteCyberBankStatementList",
	"edModelDivId":"#edModelCyberBankStatementList",
	"sdModelDivId":"#sdModelCyberBankStatementList",
	"listInterface":"../Tgpf_CyberBankStatementList",
	//"addInterface":"../Tgpf_CyberBankStatementAdd",
	"deleteInterface":"../Tgpf_CyberBankStatementBatchDelete",
	"exportInterface":"../Tgpf_CyberBankStatement_Export",//导出excel接口
	//"updateInterface":"../Tgpf_CyberBankStatementUpdate"
});
