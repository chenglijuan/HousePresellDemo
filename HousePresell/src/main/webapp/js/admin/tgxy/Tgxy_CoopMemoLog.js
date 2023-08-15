(function(baseInfo) {
	var logVue = new Vue({
		el: baseInfo.detailDivId,
		data: {
			interfaceVersion: 19000101,
			keyword: "",
			tgxy_CoopMemoLog: [
			    {index:1,editName:'张三',editDate:'2018-08-27'},
			    {index:2,editName:'张三',editDate:'2018-08-27'},
			    {index:4,editName:'张三',editDate:'2018-08-27'},
			    {index:3,editName:'张三',editDate:'2018-08-27'},
			],
			tgxy_CoopMemoLogCompare: [
			    {eCode:"用户编码",eCodeBefore:'编码前',eCodeAfter:'编码后'},
			    {eCode:"用户名称",eCodeBefore:'张三',eCodeAfter:'李四'},
			    {eCode:"所属机构",eCodeBefore:'呵呵',eCodeAfter:'哈哈'},
			    {eCode:"证件类型",eCodeBefore:'身份证',eCodeAfter:'驾驶证'},
			],
			editPeo: "张三",
			editDate: "2018-08-28"
		},
		methods: {
			//详情
			refresh: refresh,
			getSearchForm: getSearchForm,
			search: search,
			resetInfo: resetInfo,
			openDetails: openDetails,
			errClose: errClose,
			succClose: succClose,
			handleEditSave: handleEditSave,
			LogModelClose: LogModelClose
		},
		computed: {

		},
		components: {

		},
		watch: {

		}
	});

	//------------------------方法定义-开始------------------//
	//加载操作--------------获取"合作备忘录日志"参数
	function getSearchForm() {
		return {
			interfaceVersion: this.interfaceVersion,
//			tableId: logVue.tableId,
            keyword: logVue.keyword
		}
	}
	
	function search() {
		refresh();
	}
	
	function resetInfo() {
		logVue.keyword = "";
	}
	
	function openDetails(tableId) {
		$(baseInfo.LogDetailModel).modal('show', {
		    backdrop :'static'
	   });
	}
	
	function errClose() {
		
	}
	
	function succClose() {
		
	}
	
	function handleEditSave() {
		
	}
	
	function LogModelClose() {
		$(baseInfo.LogDetailModel).modal('hide');
	}

	//详情操作--------------
	function refresh() {
		new ServerInterface(baseInfo.listInterface).execute(logVue.getSearchForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				noty({
					"text": jsonObj.info,
					"type": "error",
					"timeout": 2000
				});
			} else {
				
				logVue.tgxy_CoopMemoLog = jsonObj.tgxy_CoopMemoLog;
				//动态跳转到锚点处，id="top"
				document.getElementById('tgxy_CoopMemoLogDiv').scrollIntoView();
			}
		});
	}
	//------------------------方法定义-结束------------------//

	//------------------------数据初始化-开始----------------//
	logVue.refresh();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId": "#tgxy_CoopMemoLogDiv",
	"LogDetailModel": "#logDetail",
	"listInterface": "../Tgxy_CoopMemoLog",
});