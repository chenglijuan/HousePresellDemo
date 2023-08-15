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
			userStartList:[],
			userRecordId:null,
			userRecordList:[],
			tgxy_CoopAgreementVerMngList:[],
			errtips:'',
			editDisabled : true,
			delDisabled : true, 
			versionList : [],//版本号
			enableTimeStamp : "",
			downTimeStamp : "",
		},
		methods : {
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			listItemSelectHandle: listItemSelectHandle,
			tgxy_CoopAgreementVerMngDelOne : tgxy_CoopAgreementVerMngDelOne,
			tgxy_CoopAgreementVerMngDel : tgxy_CoopAgreementVerMngDel,
			tgxy_CoopAgreementVerMngDetail : tgxy_CoopAgreementVerMngDetail,
			checkCheckBox : checkCheckBox,
			tgxy_CoopAgreementVerMngListAdd : tgxy_CoopAgreementVerMngListAdd,
			tgxy_CoopAgreementVerMngListEdit : tgxy_CoopAgreementVerMngListEdit,
			search:search,
			checkAllClicked : checkAllClicked,
			changePageNumber: function (data) {
				if (listVue.pageNumber != data) {
					listVue.pageNumber = data;
					listVue.refresh();
				}
			},
			changeCountPerPage : function (data) {
				if (listVue.countPerPage != data) {
					listVue.countPerPage = data;
					listVue.refresh();
				}
			},
			exportExcel: exportExcel,
			showDelModal : showDelModal,
			loadVersion : loadVersion,
			reset : reset,
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue
		},
		watch:{
//			pageNumber : refresh,
			selectItem : selectedItemChanged,
		},
		mounted:function(){
			laydate.render({
			    elem: '#tgxy_vermngtime',
			    range: '~',
				done: function(value, date, endDate){
				  	var arr = value.split("~");
				    listVue.enableTimeStamp = arr[0];
				    listVue.downTimeStamp = arr[1];
				}
			}); 
		}
	});
	//------------------------方法定义-开始------------------//
	//跳转新增方法
	function tgxy_CoopAgreementVerMngListAdd() {
		enterNewTab('', '新增合作协议版本管理', 'tgxy/Tgxy_CoopAgreementVerMngAdd.shtml');
	}
	
	//跳转详情信息方法
	function tgxy_CoopAgreementVerMngDetail(tableId) {
		enterNextTab(tableId, '合作协议版本管理详情', 'tgxy/Tgxy_CoopAgreementVerMngDetail.shtml',tableId+"06110202");
	}
	
	//跳转修改
	function tgxy_CoopAgreementVerMngListEdit(){
		enterNextTab(listVue.selectItem[0], '修改合作协议版本管理', 'tgxy/Tgxy_CoopAgreementVerMngEdit.shtml',listVue.selectItem[0]+"06110202");	
	}

	 function checkCheckBox(tableId){
		listVue.selectItem  = tableId;
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
	
	//列表操作-------------------重置
		function reset(){
			this.keyword = "";
		}
	//列表操作-------------------页面加载版本号
	function loadVersion(){
		var model = {
			interfaceVersion:this.interfaceVersion,
		}
		new ServerInterface(baseInfo.loadVersionInterface).execute(model, function(jsonObj){
			if(jsonObj.result != "success")
			{
				detailVue.errtips = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
					backdrop :'static'
				});
			}
			else
			{
				listVue.versionList = jsonObj.versionList;
			}
		});
	}
	
	
	function showDelModal(){
		if(listVue.getDeleteForm().idArr.length == "0"){

			noty({"text":"请至少选择一项进行删除","type":"error","timeout":2000});
		}else{
			$(baseInfo.deleteDivId).modal({
				backdrop :'static'
			});
		}	
	}

	
	function tgxy_CoopAgreementVerMngDel()
	{
		new ServerInterface(baseInfo.batchDeleteInterface).execute(listVue.getDeleteForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
//				noty({"text":jsonObj.info,"type":"error","timeout":2000});
				detailVue.errtips = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
					backdrop :'static'
				 });
			}
			else
			{
				$(baseInfo.deleteDivId).modal('hide');
				listVue.selectItem = [];
				refresh();
			}
		});
	}
	function tgxy_CoopAgreementVerMngDelOne(tgxy_CoopAgreementVerMngId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[tgxy_CoopAgreementVerMngId],
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
		listVue.isAllSelected = (listVue.tgxy_CoopAgreementVerMngList.length > 0)
		&&	(listVue.selectItem.length == listVue.tgxy_CoopAgreementVerMngList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgxy_CoopAgreementVerMngList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgxy_CoopAgreementVerMngList.forEach(function(item) {
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
//				noty({"text":jsonObj.info,"type":"error","timeout":2000});
				detailVue.errtips = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
					backdrop :'static'
				 });
			}
			else
			{
				listVue.tgxy_CoopAgreementVerMngList=jsonObj.tgxy_CoopAgreementVerMngLists;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tgxy_CoopAgreementVerMngListDiv').scrollIntoView();
			}
		});
	}
	
	//列表操作------------搜索
	function search()
	{
		listVue.pageNumber = 1;
		refresh();
	}

	// 导出--------数据导出为Excel
	function exportExcel() {
		new ServerInterface(baseInfo.exportInterface).execute(listVue.getSearchForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				detailVue.errtips = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
					backdrop :'static'
				 });
			}
			else
			{
				/*listVue.selectItem = [];
				refresh();*/
				window.location.href="../"+jsonObj.fileURL;
			}
		});
		/*flag = true;
		// 获取需要导出的table的dom节点
		var wb = XLSX.utils.table_to_book(document.querySelector('#CoopAgreementVerMngListTab'));
		// 写入列表数据并保存
		if(flag){
		    var wbout = XLSX.write(wb, { bookType: 'xlsx', bookSST: true, type: 'array' });
			flag = false;
		}
		console.log(wbout);
		
		try {
			// 导出
			saveAs(new Blob([wbout], { type: 'application/octet-stream' }), '合作协议版本管理.xlsx');
		} catch(e) {
			if(typeof console !== 'undefined') console.log(e, wbout)
		}
		return wbout*/
		
	}
	function indexMethod(index) {
		return generalIndexMethod(index, listVue)
	}
	
	function listItemSelectHandle(list) {
	//	var falgArr = [];
		generalListItemSelectHandle(listVue,list);
		if(list.length == 1){
			if(list[0].busiState == '1'){
			   listVue.editDisabled = false;
			}else if(list[0].busiState == '2' || list[0].busiState == '3'){
			   listVue.editDisabled = true; 
			}
			
		}else{
			listVue.editDisabled = true;
		}
		if(list.length >= 1){
			for(var i = 0;i<list.length;i++){
				//falgArr.push(list[i].busiState);
				if(list[i].busiState == "2" || list[i].busiState == "3"){
					listVue.delDisabled = true;
					return;
				}else{
					listVue.delDisabled = false;
				}
			}
		}else{
			listVue.delDisabled = true;
		}
		
		/*for(var j = 0;j<falgArr.length;j++){
			if(falgArr[j] == "2" || falgArr[j] == "3"){
				listVue.delDisabled = true;
				return;
			}else{
				listVue.delDisabled = false;
			}
		}*/
 	}
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
	listVue.loadVersion();
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tgxy_CoopAgreementVerMngListDiv",
	"errorModel":"#errorEscrowAdd",
	"successModel":"#successEscrowAdd",
	"deleteDivId":"#delete",
	"listInterface":"../Tgxy_CoopAgreementVerMngLists",
	"batchDeleteInterface":"../Tgxy_CoopAgreementVerMngBatchDeletes",
	"loadVersionInterface" : "",//页面加载显示版本号
	"exportInterface" : "",
});
