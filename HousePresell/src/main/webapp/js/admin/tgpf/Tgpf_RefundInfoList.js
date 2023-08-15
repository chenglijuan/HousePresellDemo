(function(baseInfo){
	var listVue = new Vue({
		el : baseInfo.listDivId,
		data : {
			interfaceVersion :19000101,
			pageNumber : 1,
			countPerPage : 10,
			totalPage : 1,
			totalCount : 2,
			keyword : "",
			createTimeStamp: null,
			refundTimeStamp: "",
			selectItem : [],
			isAllSelected : false,
			theState:0,//正常为0，删除为1
			userStartId:null,
			userStartList:[],
			userRecordId:null,
			userRecordList:[],
			tripleAgreementId:null,
			tripleAgreementList:[],
			projectId:null,
			projectList:[],
			buildingId:null,
			buildingList:[],
			buyerId:null,
			buyerList:[],
			deleteArr : [],
			tgpf_RefundInfoList:[],
			errMsg :"",//存放错误提示信息
			editDisabled : true,
			delDisabled : true,
			refundStatus : null,//退款状态
			projectId: "",
            projectList: [],
            refundStatusList : [
            	{tableId:"0",theName:"待提交"},
            	{tableId:"1",theName:"申请中"},
            	{tableId:"2",theName:"已退款"},
            ]
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			tgpf_RefundInfoDel : tgpf_RefundInfoDel,
			search:search,
			checkAllClicked : checkAllClicked,
			reFundInfoAddHandle : reFundInfoAddHandle,
			reFundInfoDetailHandle : reFundInfoDetailHandle,
			reFundInfoEditHandle : reFundInfoEditHandle,
			changeprojectListener: changeprojectListener,
			changeProjectEmpty: changeProjectEmpty,
			checkCheckBox : checkCheckBox,
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
			resetSearch : resetSearch,
			showDelModal : showDelModal,
			//deleteMakeSure : deleteMakeSure,
			indexMethod : indexMethod,
			changeRefundStatus : function(data){
				this.refundStatus = data.tableId;
			},
			changeRefundStatusEmpty : function(){
				this.refundStatus = null;
			}
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue,
			'vue-listselect': VueListSelect
		},
		watch:{
			//pageNumber : refresh,
			selectItem : selectedItemChanged,
		}
	});

	//------------------------方法定义-开始------------------//
	function changeprojectListener(data) {
        if (listVue.projectId != data.tableId) {
            listVue.projectId = data.tableId
            // listVue.refresh()
        }
    }
	
	 function changeProjectEmpty() {
	        if (listVue.projectId != null) {
	            listVue.projectId = null
	            // listVue.refresh()
	        }
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
			refundTimeStamp:this.refundTimeStamp,
			theState:this.theState,
			userStartId:this.userStartId,
			userRecordId:this.userRecordId,
			tripleAgreementId:this.tripleAgreementId,
			projectId:this.projectId,
			buildingId:this.buildingId,
			buyerId:this.buyerId,
			refundStatus:this.refundStatus,
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
	
	function indexMethod(index){
		return generalIndexMethod(index, listVue)
	}
	
	
	//显示删除提示框
	function showDelModal(){
		if(listVue.getDeleteForm().idArr.length == "0"){

			noty({"text":"请至少选择一项进行删除","type":"error","timeout":2000});
		}else{
			$(baseInfo.deleteDivId).modal({
				backdrop :'static'
			});
		}	
	}
	
	
	function tgpf_RefundInfoDel()
	{
		$(baseInfo.deleteDivId).modal("hide");
		new ServerInterface(baseInfo.batchDeleteInterface).execute(listVue.getDeleteForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				$(baseInfo.edModelDivId).modal('show', {
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
	
	
	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		listVue.isAllSelected = (listVue.tgpf_RefundInfoList.length > 0)
		&&	(listVue.selectItem.length == listVue.tgpf_RefundInfoList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked(tableId)
	{
	    if(listVue.selectItem.length == listVue.tgpf_RefundInfoList.length)
	    {
	    	listVue.selectItem = [];
	    }else{
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgpf_RefundInfoList.forEach(function(item) {
	    		listVue.selectItem.push(item.tableId);
	    	});
	    }
	}
	
	function checkCheckBox(tableId){
		listVue.selectItem  = tableId;
		console.log(listVue.selectItem);
		if(listVue.selectItem.length == 1){
			if(tableId[0].busiState == "待审批" || tableId[0].busiState == "" || tableId[0].busiState == undefined || tableId[0].busiState == "待提交"){
				listVue.editDisabled = false;
				listVue.delDisabled = false;
			}else{
				listVue.editDisabled = true;
				listVue.delDisabled = true;
			}
		}else if(listVue.selectItem.length >1){
			listVue.delDisabled = false;
			for( var i = 0 ; i < listVue.selectItem.length ; i++){
				if(!(undefined == listVue.selectItem[i].busiState ||  listVue.selectItem[i].busiState == "待审批" || listVue.selectItem[i].busiState == "" || listVue.selectItem[i].busiState == "待提交")){
					listVue.delDisabled = true;
				}
			}
			listVue.editDisabled = true;
		}else if(listVue.selectItem.length  == 0){
			listVue.editDisabled = true;
			listVue.delDisabled = true;
		}
		
	}
	//列表操作--------------刷新
	function refresh()
	{
		new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				listVue.errMsg = jsonObj.info;
			}
			else
			{
				listVue.tgpf_RefundInfoList=jsonObj.tgpf_RefundInfoList;
				listVue.tgpf_RefundInfoList.forEach(function(item){
					item.refundAmount = addThousands(item.refundAmount);
					item.actualRefundAmount = addThousands(item.actualRefundAmount);
				})
				listVue.totalCount = jsonObj.totalCount;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.keyword=jsonObj.keyword;
				listVue.createTimeStamp = jsonObj.createTimeStamp;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tgpf_RefundInfoListDiv').scrollIntoView();
			}
		});
	}
	
	//列表操作------------搜索
	function search()
	{
		listVue.pageNumber = 1;
		
		refresh();
	}
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}
	
	function initData()
	{
		initButtonList();
		getProjectList();
	}
	
	function getProjectList() {
        serverRequest("../Empj_ProjectInfoForSelect",getTotalListForm(),function (jsonObj) {
            listVue.projectList=jsonObj.empj_ProjectInfoList;
        })
    }
	//------------------------方法定义-结束------------------//
	//跳转新增退房退款方法
	function reFundInfoAddHandle() {
		enterNewTab('', '新增退房退款信息', 'tgpf/Tgpf_RefundInfoAdd.shtml');
	}
	
	//跳转新增退房退款详情信息方法
	function reFundInfoDetailHandle(tableId) {
		//$("#tabContainer").data("tabs").remove($("#tabContainer").data("tabs").getCurrentTabId());
		enterNextTab(tableId, '退房退款信息详情', 'tgpf/Tgpf_RefundInfoDetail.shtml',tableId+"06120201");
	}
	
	//跳转修改
	function reFundInfoEditHandle(){
		enterNextTab(listVue.selectItem[0].tableId, '编辑退房退款信息', 'tgpf/Tgpf_RefundInfoEdit.shtml',listVue.selectItem[0].tableId+"06120201");
	}
	function resetSearch(){
		listVue.refundTimeStamp = "";
		listVue.keyword = "";
		listVue.refundStatus = null;
		listVue.projectId = null;
		refresh();
	}
	/**
	 * 初始化日期插件
	 */
	laydate.render({
	  elem: '#refundInfoSearch',
	  done: function(value, date, endDate){
	    listVue.refundTimeStamp = value;
	  }
	});
	//------------------------数据初始化-开始----------------//
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tgpf_RefundInfoListDiv",
 	"deleteDivId":"#deleteRefundInfoList",
	"edModelDivId":"#edModelRefundInfoList",
	"sdModelDivId":"#sdModelRefundInfoList",
	"listInterface":"../Tgpf_RefundInfoList",
//	"addInterface":"../Tgpf_RefundInfoAdd",
	"deleteInterface":"../Tgpf_RefundInfoDelete",
	"batchDeleteInterface" :"../Tgpf_RefundInfoBatchDelete",
//	"updateInterface":"../Tgpf_RefundInfoUpdate"
});

