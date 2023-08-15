(function(baseInfo){
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			pageNumber : 1,
			countPerPage : 1,
			totalPage : 1,
			totalCount : 1,
			selectItem : [],
			isAllSelected : false,
			bankId : "", //银行名称的标志
			BankList : [],//银行list
			managedAccountList : [],//显示托管账号
			theAccountOfBankAccountEscrowedId:"",//托管账号的id
			tgpf_CyberBankStatementAddList : [],//数据列表
			enterTimeStamp:"",//上传日期
			theNameOfBankAccountEscrowed : "",//托管账号
			bankBranchName:"",//开户行
			deleteArr : [],//存放删除的tableId
			saveArr:[],//存放保存的数据id
            dataUpload:{},
			dialogImageUrl: '',
            dialogVisible: false,
            uploadDataModal : {},
            uploadData : [],
            billTimeStamp : "",//记账日期
            sm_attachment:null,
            uploadTimeStamp:"",
            orgFilePath:"",
            errMsg:"",
            showButton:true,
            importDisabled : false,
            delDisabled : true,
            ossLoader : {}
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			selectedItemChanged : selectedItemChanged,
			checkAllClicked : checkAllClicked,
			getSearchForm : getSearchForm,
			getLoadOssServerUtil : getLoadOssServerUtil,
			//添加
			getAddForm : getAddForm,
			getBankNameForm : getBankNameForm,//获得银行传递参数
			getTheAccountBankForm : getTheAccountBankForm,//获取托管账户传递参数
			getDeleteForm : getDeleteForm,//获取删除传递参数
			choiceBankName : choiceBankName,//选择银行名称带出托管账号
			choiceManagedAccount : choiceManagedAccount,
			tgpf_cyberBankStatementAddDel : tgpf_cyberBankStatementAddDel,//删除数据列表
			getImportForm:getImportForm,
			checkCheckBox : checkCheckBox,
			showDelModal : showDelModal,
			handleCallBack:function(response,file, fileList){//上传成功之后的回掉函数
				var modal = {
	        			theLink:response.data[0].url,
	        			fileType:response.data[0].objType,
	        			theSize:response.data[0].byteToStr,
	        			remark:response.data[0].originalName,
	        			billTimeStamp:addVue.billTimeStamp,
	        			bankId:addVue.bankId
	        	}
				addVue.sm_attachment = modal;
				addVue.orgFilePath = modal.theLink;
				
				console.log("开始请求····");
				
	    		new ServerInterface(baseInfo.importInterface).execute(addVue.getImportForm(), function(jsonObj){
	    			console.log("请求完成···");
	    			console.log(jsonObj);
					if(jsonObj.result != "success")
					{
						$(baseInfo.edModelDivId).modal({
							backdrop :'static'
						});
						addVue.errMsg = jsonObj.info;
					}
					else
					{
						//refresh();
						addVue.tgpf_CyberBankStatementAddList = jsonObj.tgpf_CyberBankStatementDtlList;
						console.log("请求···");
						console.log(addVue.tgpf_CyberBankStatementAddList);
						
						addVue.tgpf_CyberBankStatementAddList.forEach(function(item){
							item.income = addThousands(item.income);
						})
						jsonObj.tgpf_CyberBankStatementDtlList.forEach(function(item){
							addVue.saveArr.push(item.tableId);
						})
						
						addVue.uploadTimeStamp = jsonObj.uploadTimeStamp;
						
					}
				});
	    		
		    },
			add: add,
			loadForm:loadForm,
			loadOssServer : loadOssServer,
			getTableId :function(data){
				addVue.bankId = data.tableId;
				choiceBankName();
			},
			handleExceed: function() {
				generalErrorModal(null,"您上传的文件个数超出限制！");
			}
			
		},
		computed:{
			 
		},
		components : {
			'vue-select': VueSelect,
		},
		watch:{
			
		}
	});
	
	
	//------------------------方法定义-开始------------------//
	function getImportForm(){
		var importList = addVue.sm_attachment;
	    importList = JSON.stringify(importList);
	    return{
	    	interfaceVersion:this.interfaceVersion,
	    	sm_attachment : importList,
	    }
	}
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
		}
	}
	//新增操作----------获取银行名称的tableId
	function getBankNameForm(){
		return{
			interfaceVersion:this.interfaceVersion,
			bankId:this.bankId,
		}
	}
	
	//加载Oss-Server--------------获取"加载"参数
	function getLoadOssServerUtil()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			eCode:"123",
		}
	}
	
	//新增操作-------------根据托管账户Id传递的参数
	function getTheAccountBankForm(){
		return{
			interfaceVersion:this.interfaceVersion,
			tableId:this.theAccountOfBankAccountEscrowedId,
		}
	}
	//新增操作---------------获取删除传递参数
	function getDeleteForm(){
		this.deleteArr = [];
		addVue.selectItem.forEach(function(item){
			addVue.deleteArr.push(item.tableId);
		})
		return{
			interfaceVersion:this.interfaceVersion,
			idArr:this.deleteArr
		}
		
	}
	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		addVue.isAllSelected = (addVue.tgpf_RefundInfoList.length > 0)
		&&	(addVue.selectItem.length == addVue.tgpf_RefundInfoList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked(tableId)
	{
	    if(addVue.selectItem.length == addVue.tgpf_RefundInfoList.length)
	    {
	    	addVue.selectItem = [];
	    }else{
	    	addVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	addVue.tgpf_RefundInfoList.forEach(function(item) {
	    		addVue.selectItem.push(item.tableId);
	    	});
	    }
	}
	
	function showDelModal(){
		if(addVue.getDeleteForm().idArr.length == "0"){
			generalErrorModal("","请至少选择一项进行删除！");
		}else{
			$(baseInfo.deleteDivId).modal({
				backdrop :'static'
			});
		}
	}
	
	//新增操作------------删除数据列表
	function tgpf_cyberBankStatementAddDel()
	{
		$(baseInfo.deleteDivId).modal('hide');
		new ServerInterface(baseInfo.batchDeleteInterface).execute(addVue.getDeleteForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				addVue.errMsg = jsonObj.info;
			}
			else
			{
				$(baseInfo.sdModelDivId).modal({
					backdrop :'static'
				});
				addVue.tgpf_CyberBankStatementAddList.forEach(function(item){
					for(var i = 0 ;i<jsonObj.delList.length;i++){
						if(jsonObj.delList[i].tableId == item.tableId){
							item.theState = 1;
						}
					}
				})
				addVue.selectItem = [];
				refresh();
			}
		});
	}
	
	function loadOssServer(){
		new ServerInterface(baseInfo.loadOssServerInterface).execute(addVue.getLoadOssServerUtil(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				addVue.errMsg = jsonObj.info;
			}
			else
			{
				addVue.ossLoader = jsonObj.ossLoader;
				addVue.dataUpload = {
						appid :  jsonObj.ossLoader.appid,
						appsecret : jsonObj.ossLoader.appsecret,
						extra : jsonObj.ossLoader.extra,
				}
			}
		});
	}
	
	function checkCheckBox(tableId){
		if(tableId.length >=1){
			addVue.delDisabled = false;
		}else{
			addVue.delDisabled = true;
		}
		addVue.selectItem  = tableId;
	}
	
	
	
	//详情操作--------------
	function refresh()
	{

	}
	
	//详情新增操作--------------获取"更新机构详情"参数
	function getAddForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			uploadTimeStamp:this.uploadTimeStamp,
			orgFilePath:this.orgFilePath,
			billTimeStamp : this.billTimeStamp,
			idArr : this.saveArr,
			bankId:addVue.bankId,
			theAccountOfBankAccountEscrowedId:addVue.theAccountOfBankAccountEscrowedId
		}
	}
	//新增操作------------加载银行信息
	function loadForm(){
		new ServerInterface(baseInfo.loadBankInterface).execute(addVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				addVue.errMsg = jsonObj.info;
			}
			else
			{
				//refresh();
				addVue.BankList = jsonObj.emmp_BankInfoList;
			}
		});
	}
	//新增操作----------根据银行信息带出托管账号
	function choiceBankName(){
		new ServerInterface(baseInfo.managedAccountInterface).execute(addVue.getBankNameForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				//refresh();
				addVue.managedAccountList = jsonObj.tgxy_BankAccountEscrowedList;
				addVue.theNameOfBankAccountEscrowed = "";
				addVue.bankBranchName = "";
			}
		});
	}
	//新增操作-------------根据托管账户Id带出托管账户名称
	function choiceManagedAccount(){
		new ServerInterface(baseInfo.managedAccountNameInterface).execute(addVue.getTheAccountBankForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				//refresh();
				addVue.theNameOfBankAccountEscrowed = jsonObj.tgxy_BankAccountEscrowed.theName;
				addVue.bankBranchName = jsonObj.tgxy_BankAccountEscrowed.bankBranchName;
				addVue.tgpf_CyberBankStatementAddList = jsonObj.tgpf_CyberBankStatementAddList;
				
			}
		});
	}
	
	function add()
	{
		new ServerInterface(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				addVue.showButton = false;
				enterNext2Tab(jsonObj.tableId, '网银上传详情', 'tgpf/Tgpf_CyberBankStatementDetail.shtml',jsonObj.tableId+"200201");
				refresh();
			}
		});
	}
	
	function initData()
	{
		
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	addVue.loadOssServer();
	addVue.loadForm();
	addVue.refresh();
	addVue.initData();
	/**
	 * 初始化日期插件
	 */
	laydate.render({
	  elem: '#dataCyberBank200201001',
	  done: function(value, date, endDate){
	    addVue.billTimeStamp = value;
	  }
	});
	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#tgpf_CyberBankStatementAddDiv",
	"deleteDivId":"#deleteCyberBankStatementAdd",
	"edModelDivId":"#edModelCyberBankStatementAdd",
	"sdModelDivId":"#sdModelCyberBankStatementAdd",
	"detailInterface":"../Tgpf_CyberBankStatementDetail",
	"addInterface":"../Tgpf_CyberBankStatementAdd",
	"loadBankInterface" : "../Emmp_BankInfoList",
	"managedAccountInterface":"../Tgxy_BankAccountEscrowedList",
	"managedAccountNameInterface":"../Tgxy_BankAccountEscrowedDetail",
	"batchDeleteInterface":"../Tgpf_CyberBankStatementDtlBatchDelete",
	"importInterface" : "../Tgpf_CyberBankStatementDtlAdd",
	"loadOssServerInterface" : "../LoadOssServerUtil",
});
