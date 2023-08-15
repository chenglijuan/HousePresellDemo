(function(baseInfo){
	var editVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tgpf_CyberBankStatementModel: {},
			tableId : 1,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			selectItem : [],
			isAllSelected : false,
			tgpf_CyberBankStatementEdit:[],
			deleteArr:[],//存放删除的id，
			dataUpload:{
				"appid":"oss002e5c",
				"appsecret":"egg0s9u3"
			},
			dialogImageUrl: '',
            dialogVisible: false,
            uploadDataModal : {},
            uploadData : [],
            errMsg : "",
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,//获取删除的参数
			//更新
			tgpf_cyberBankStatementAddDel : tgpf_cyberBankStatementAddDel,//添加删除方法
			checkCheckBox : checkCheckBox,
			recover : recover,
			showDelModal : showDelModal,
			handleCallBack:function(response,file, fileList){//上传成功之后的回掉函数
				var modal = {
	        			sourceType:"营业执照",
	        			theLink:response.data[0].url,
	        			fileType:response.data[0].objType,
	        			theSize:response.data[0].byteToStr,
	        			remark:response.data[0].originalName
	        	}
	    		addVue.uploadData.push(modal);
	    		
	    		
	    		new ServerInterface(baseInfo.importInterface).execute(addVue.getImportForm(), function(jsonObj)
				{
					if(jsonObj.result != "success")
					{
						$(baseInfo.edModelDivId).modal({
							backdrop :'static'
						});
						editVue.errMsg = jsonObj.info;
						//noty({"text":jsonObj.info,"type":"error","timeout":2000});
					}
					else
					{
						//refresh();
						addVue.tgpf_CyberBankStatementEdit = jsonObj.tgpf_CyberBankStatementEdit;
						addVue.tgpf_CyberBankStatementEdit.forEach(function(item){
							item.income = addThousands(item.income);
						})
					}
				});
	    		
		    },
		},
		computed:{
			 
		},
		components : {

		},
		watch:{
			
		}
	});

	//------------------------方法定义-开始------------------//
	//详情操作--------------获取"机构详情"参数
	function getImportForm(){
		var importList = addVue.uploadData;
	    importList = JSON.stringify(importList);
	    return{
	    	interfaceVersion:this.interfaceVersion,
	    	importList : importList,
	    }
	}
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:this.tableId,
		}
	}

	//详情操作--------------
	function refresh()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var array = tableIdStr.split("_");
		editVue.tableId = array[2];
		if (editVue.tableId == null || editVue.tableId < 1) 
		{
			return;
		}
		getDetail();
	}

	function getDetail()
	{
		new ServerInterface(baseInfo.detailInterface).execute(editVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				editVue.errMsg = jsonObj.info;
			}
			else
			{
				editVue.tgpf_CyberBankStatementModel = jsonObj.tgpf_CyberBankStatement;
				editVue.tgpf_CyberBankStatementEdit = jsonObj.tgpf_CyberBankStatementDtlList;
				editVue.tgpf_CyberBankStatementEdit.forEach(function(item){
					item.income = addThousands(item.income);
				})
			}
		});
	}
	//修改操作-----------------恢复
	function recover(){
		if(editVue.getDeleteForm().idArr.length == "0"){
			generalErrorModal("","请选择需要恢复的数据！");
		}else{
			new ServerInterface(baseInfo.recoverInterface).execute(editVue.getDeleteForm(), function(jsonObj){
				if(jsonObj.result != "success")
				{
					$(baseInfo.edModelDivId).modal({
						backdrop :'static'
					});
					editVue.errMsg = jsonObj.info;
				}
				else
				{
					editVue.selectItem = [];
					refresh();
				}
			});
		}
	}
	
	//选中复选框
	function checkCheckBox(tableId){
		editVue.selectItem = tableId;
	}
	
	
	function getDeleteForm(){
		this.deleteArr = [];
		editVue.selectItem.forEach(function(item){
			editVue.deleteArr.push(item.tableId);
		})
		return{
			interfaceVersion:this.interfaceVersion,
			idArr:this.deleteArr
		}
	}
	
	function showDelModal(){
		if(editVue.getDeleteForm().idArr.length == "0"){
			generalErrorModal("","请至少选择一项进行删除！");
		}else{
			$(baseInfo.deleteDivId).modal({
				backdrop :'static'
			});
		}
	}
	
	//添加删除方法
	function tgpf_cyberBankStatementAddDel(){
		$(baseInfo.deleteDivId).modal('hide');
		new ServerInterface(baseInfo.deleteInterface).execute(editVue.getDeleteForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				//noty({"text":jsonObj.info,"type":"error","timeout":2000});
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				editVue.errMsg = jsonObj.info;
			}
			else
			{
				$(baseInfo.sdModelDivId).modal({
					backdrop :'static'
				});
				editVue.selectItem = [];
				refresh();
			}
		});
	}
	
	function initData()
	{
		
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	editVue.refresh();
	editVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tgpf_CyberBankStatementDiv",
	"deleteDivId":"#deleteCyberBankStatementEdit",
	"edModelDivId":"#edModelCyberBankStatementEdit",
	"sdModelDivId":"#sdModelCyberBankStatementEdit",
	"detailInterface":"../Tgpf_CyberBankStatementDetail",
	"deleteInterface":"../Tgpf_CyberBankStatementDtlBatchDelete",//删除的接口
	"importInterface":"",
	"recoverInterface":"../Tgpf_CyberBankStatementDtlUpdate",//恢复接口
});
