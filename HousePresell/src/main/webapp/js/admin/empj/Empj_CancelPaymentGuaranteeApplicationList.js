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
			auditTime : "",//申请日期
			applyDate : "",//申请日期,
			selectItem : [],
			isAllSelected : false,
			theState:0,//正常为0，删除为1
			busiState:0,
			Empj_CancelPaymentGuaranteeApplicationList:[],
			projectId : "", //项目名称
			companyId : "", //开发企业
			qs_companyNameList : [], //页面加载显示开发企业
			qs_projectNameList : [], //显示项目名称
			editDisabled : true,
			delDisabled : true,
			cancelDisabled : true,
			errMsg : "",
			busiStateApply : "",
			busiStateApplyList : [
				{tableId:"2",theName:"未申请"},
				{tableId:"3",theName:"撤销审核中"},
				{tableId:"4",theName:"已撤销"},
			],
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
            getBatchDeleteForm : getBatchDeleteForm,
            empj_CancelPaymentGuaranteeApplicationBatchDel : empj_CancelPaymentGuaranteeApplicationBatchDel,
			search:search,
            resetSearchInfo:resetSearchInfo,
			cancelPaymentGuaranteeApplicationEditHandle: cancelPaymentGuaranteeApplicationEditHandle,//编辑
			cancelPaymentGuaranteeApplicationDelHandle : cancelPaymentGuaranteeApplicationDelHandle,//删除
            cancelPaymentGuaranteeApplicationExportExcelHandle: cancelPaymentGuaranteeApplicationExportExcelHandle,
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
			loadCompanyName : loadCompanyNameFun, //页面加载显示开发企业方法
			changeCompanyHandle : changeCompanyHandleFun, //改变企业名称的方法
			handleSelectionChange: handleSelectionChange,
			indexMethod: indexMethod,
			cancelPaymentGuaranteeApplicationDetailHandle : cancelPaymentGuaranteeApplicationDetailHandle,
			getCompanyId: function(data) {
				listVue.companyId = data.tableId;
				listVue.changeCompanyHandle();
			},
			emptyCompanyId : function(){
				listVue.companyId = null;
				listVue.qs_projectNameList = [];
			},
			getProjectId: function(data) {
				listVue.projectId = data.tableId;
			},
			emptyProjectId : function(){
				listVue.projectId = null;
			},
			getBusiStateApply: function(data) {
				listVue.busiStateApply = data.tableId;
			},
			emptyBusiStateApply : function(){
				listVue.busiStateApply = null;
			},
			
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue,
			'vue-select': VueSelect,
			'vue-listselect': VueListSelect,
			'vue-listsearch' : VueListSearch,
		},
		watch:{
			//pageNumber : refresh,
		},
		mounted:function(){
			/**
			 * 初始化日期插件
			 */
			laydate.render({
			    elem: '#cancelPaymentGuaranteeAppDate',
				done: function(value, date, endDate){
					listVue.auditTime = value;
				}
			});
		}
	});
	//------------------------方法定义-开始------------------//

	//列表操作--------------获取"搜索列表"表单参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			pageNumber:this.pageNumber,
			countPerPage:this.countPerPage,
			totalPage:this.totalPage,
			keyword:this.keyword,
			busiState: this.busiStateApply,
			theState:this.theState,
			companyId : this.companyId,
			projectId : this.projectId,
			auditTime : this.auditTime,
			applyDate : this.auditTime
			
		}
	}

	//列表操作----------序号
	function indexMethod(index){
		return generalIndexMethod(index, listVue)
	}
	//列表操作--------------获取“删除资源”表单参数
	function getBatchDeleteForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			idArr:this.selectItem
		}
	}
	//列表操作-----------------------页面加载显示开发企业
	function loadCompanyNameFun() {
		loadCompanyName(listVue,baseInfo.companyNameInterface,function(jsonObj){
			listVue.qs_companyNameList = jsonObj.emmp_CompanyInfoList;
		},listVue.errMsg,baseInfo.edModelDivId);
	}
	//列表操作--------------------改变开发企业的方法
	function changeCompanyHandleFun() {
		changeCompanyHandle(listVue,baseInfo.changeCompanyInterface,function(jsonObj){
			listVue.qs_projectNameList = jsonObj.empj_ProjectInfoList;
		},listVue.errMsg,baseInfo.edModelDivId)
	}
	
    //列表操作------------搜索
    function search()
    {
        listVue.pageNumber = 1;
        refresh();
    }
    
    //列表操作------------重置搜索
	function resetSearchInfo() {
		this.keyword = "";
		this.companyId = "";
		this.projectId = "";
        listVue.pageNumber = 1;
        this.busiStateApply = "";
        this.auditTime = "";
        refresh();
    }


    //列表操作---选择
    function handleSelectionChange(val) {
    	if(val.length == 1){
    		listVue.editDisabled = false;
    		listVue.cancelDisabled = false;
    	}else{
    		listVue.editDisabled = true;
    		listVue.cancelDisabled = true;
    	}
    	
    	if(val.length >=1){
    		listVue.delDisabled = false;
    	}else{
    		listVue.delDisabled = true;
    	}
    	
        listVue.selectItem = [];
        for (var index = 0; index < val.length; index++) {
        	if(val[index].busiState == 3 || val[index].busiState == 4){
        		listVue.delDisabled = true;
        		listVue.editDisabled = true;
        	}else{
        		listVue.delDisabled = false;
        		listVue.editDisabled = false;
        	}
            var element = val[index].tableId;
            listVue.selectItem.push(element)
        }
    }
	//点击删除按钮
	function cancelPaymentGuaranteeApplicationDelHandle(){
		$(baseInfo.deleteDivId).modal({
			backdrop:"static"
		})
	}
	//批量删除
	function empj_CancelPaymentGuaranteeApplicationBatchDel()
	{
		$(baseInfo.deleteDivId).modal('hide');
        new ServerInterface(baseInfo.batchDeleteInterface).execute(listVue.getBatchDeleteForm(), function(jsonObj){
            if(jsonObj.result != "success")
            {
                noty({"text":jsonObj.info,"type":"error","timeout":2000});
            }
            else
            {
                listVue.selectItem = [];
                refresh();
            }
        });
   }
	//列表操作--------------刷新
	function refresh()
	{
		new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				listVue.Empj_CancelPaymentGuaranteeApplicationList=jsonObj.empj_PaymentGuaranteeList;
				listVue.Empj_CancelPaymentGuaranteeApplicationList.forEach(function(item){
					item.alreadyActualAmount = addThousands(item.alreadyActualAmount);
					item.actualAmount = addThousands(item.actualAmount);
					item.guaranteedAmount = addThousands(item.guaranteedAmount);
				})
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				listVue.busiState=jsonObj.busiState;
				//动态跳转到锚点处，id="top"
				document.getElementById('Empj_CancelPaymentGuaranteeApplicationListDiv').scrollIntoView();
			}
		});
	}



	//按钮操作--------导出Excel
    function cancelPaymentGuaranteeApplicationExportExcelHandle()
    {
    	var model = {
    	    interfaceVersion: listVue.interfaceVersion,
            idArr: listVue.selectItem,
/*            developCompanyId : listVue.companyId,
            projectNameId : listVue.projectId, //项目名称
            */
            keyword: listVue.keyword,
            companyNameId : listVue.companyId,
			projectNameId : listVue.projectId,
			auditTime : listVue.auditTime,
			applyDate : this.auditTime
        }
        new ServerInterface(baseInfo.exportExcelInterface).execute(model, function (jsonObj) {
            if (jsonObj.result != "success")
            {
                noty({ "text": jsonObj.info, "type": "error", "timeout": 2000 });
            }
            else
            {
//            	window.location.href="../"+jsonObj.fileURL;
//            	alert(jsonObj.fileDownloadPath);
                window.location.href="../"+jsonObj.fileDownloadPath;
            }
        });
    }


    //按钮操作-------跳转到修改项目页面
    function cancelPaymentGuaranteeApplicationEditHandle()
    {
        if(listVue.selectItem.length == 1)
        {
            var tableId = listVue.selectItem[0];
            console.log(tableId)
            //$("#tabContainer").data("tabs").addTab({id: tableId , text: '修改支付保证申请撤销', closeable: true, url: 'empj/Empj_CancelPaymentGuaranteeApplicationEdit.shtml'});
            enterNextTab(tableId, '支付保证撤销修改', 'empj/Empj_CancelPaymentGuaranteeApplicationEdit.shtml', tableId + "06120403");
        }
        else
        {
            noty({"text":"请选择一个且只选一个要修改的项目","type":"error","timeout":2000});
        }
    }
	function cancelPaymentGuaranteeApplicationDetailHandle(tableId) {
		//$("#tabContainer").data("tabs").addTab({ id: tableId, text: '支付保证申请详情', closeable: true, url: 'empj/Empj_CancelPaymentGuaranteeApplicationDetail.shtml' });
		enterNextTab(tableId, '支付保证申请撤销详情', 'empj/Empj_CancelPaymentGuaranteeApplicationDetail.shtml', tableId + "06120403");
	}
	
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}
	
	function initData()
	{
		initButtonList();
        listVue.refresh();
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.loadCompanyName();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#Empj_CancelPaymentGuaranteeApplicationListDiv",
	"companyNameInterface" : "../Emmp_CompanyInfoList", //显示开发企业名称接口
	"changeCompanyInterface" : "../Empj_ProjectInfoList", //改变企业名称接口
	"deleteDivId":"#deleteCancelPaymentGuaranteeApplication",
	"edModelDivId":"#edModelCancelPaymentGuaranteeApplication",
	"sdModelDivId":"#sdModelCancelPaymentGuaranteeApplication",
	"listInterface":"../Empj_PaymentGuaranteeList",//列表页面加载接口
	"batchDeleteInterface":"../Empj_PaymentGuaranteeDelete",//批量删除接口
    "exportExcelInterface": "../Empj_PaymentGuaranteeExportExcel",//导出excel接口
});
