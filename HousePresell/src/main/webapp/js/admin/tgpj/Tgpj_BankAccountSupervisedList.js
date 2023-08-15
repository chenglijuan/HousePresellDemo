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
            selectWholeItem : [],
			isAllSelected : false,
			theState:0,//正常为0，删除为1
				userStartId:null,
			userStartList:[],
			userRecordId:null,
			userRecordList:[],
			developCompanyId:null,
			developCompanyList:[],
			bankId:null,
			bankList:[],
			bankBranchId:null,
			bankBranchList:[],
			tgpj_BankAccountSupervisedList:[],
			isUsing:"",

            companyId: "",
            companyList: [],

            orderBy:"",

            poName:"Tgpj_BankAccountSupervised",

            isEditDisable:true,
		},
		methods : {
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			showLog: showLog,
			getSearchForm : getSearchForm,
            mainDeleteHandle:mainDeleteHandle,
			showAjaxModal:showAjaxModal,
			search:search,
			checkAllClicked : checkAllClicked,
			changePageNumber : function(data){
				listVue.pageNumber = data;
			},
            mainDetailHandle:mainDetailHandle,
            mainAddHandle: mainAddHandle,
            listItemSelectHandle: listItemSelectHandle,
            companyDetailHandle:companyDetailHandle,
            bankBranchDetailHandle:bankBranchDetailHandle,
            mainEditHandle:mainEditHandle,
            resetSearch:resetSearch,
            mainExportExcelHandle:mainExportExcelHandle,

            changeCountPerPage :function (data) {
                if (listVue.countPerPage != data) {
                    listVue.countPerPage = data;
                    listVue.refresh();
                }
            },
            isUsingChange:isUsingChange,

            changeCompanyListener: changeCompanyListener,
            changeCompanyEmpty: changeCompanyEmpty,
            sortChange:sortChange,
		},
		computed:{

		},
		components : {
			'vue-nav' : PageNavigationVue,
            'vue-listselect': VueListSelect
		},
		watch:{
			pageNumber : refresh,
			selectItem : selectedItemChanged,
		}
	});

	//------------------------方法定义-开始------------------//

	//列表操作--------------获取"搜索列表"表单参数
	function getSearchForm()
	{
		return {
			interfaceVersion:listVue.interfaceVersion,
			pageNumber:listVue.pageNumber,
			countPerPage:listVue.countPerPage,
			totalPage:listVue.totalPage,
			keyword:listVue.keyword,
			theState:listVue.theState,
			userStartId:listVue.userStartId,
			userRecordId:listVue.userRecordId,
			developCompanyId:listVue.companyId,
			bankId:listVue.bankId,
			bankBranchId:listVue.bankBranchId,
			isUsing:listVue.isUsing,
            orderBy:listVue.orderBy,
		}
	}

	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{
		listVue.isAllSelected = (listVue.tgpj_BankAccountSupervisedList.length > 0)
		&&	(listVue.selectItem.length == listVue.tgpj_BankAccountSupervisedList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgpj_BankAccountSupervisedList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgpj_BankAccountSupervisedList.forEach(function(item) {
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
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				listVue.tgpj_BankAccountSupervisedList=jsonObj.tgpj_BankAccountSupervisedList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tgpj_BankAccountSupervisedListDiv').scrollIntoView();
			}
		});
	}

	//列表操作------------搜索
	function search()
	{
		listVue.pageNumber = 1;
		refresh();
	}

	//弹出编辑模态框--更新操作
	function showAjaxModal(tgpj_BankAccountSupervisedModel)
	{
		//tgpj_BankAccountSupervisedModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'tgpj_BankAccountSupervised', tgpj_BankAccountSupervisedModel);
		//updateVue.$set("tgpj_BankAccountSupervised", tgpj_BankAccountSupervisedModel);

		updateVue.theState = tgpj_BankAccountSupervisedModel.theState;
		updateVue.busiState = tgpj_BankAccountSupervisedModel.busiState;
		updateVue.eCode = tgpj_BankAccountSupervisedModel.eCode;
		updateVue.userStartId = tgpj_BankAccountSupervisedModel.userStartId;
		updateVue.createTimeStamp = tgpj_BankAccountSupervisedModel.createTimeStamp;
		updateVue.lastUpdateTimeStamp = tgpj_BankAccountSupervisedModel.lastUpdateTimeStamp;
		updateVue.userRecordId = tgpj_BankAccountSupervisedModel.userRecordId;
		updateVue.recordTimeStamp = tgpj_BankAccountSupervisedModel.recordTimeStamp;
		updateVue.developCompanyId = tgpj_BankAccountSupervisedModel.developCompanyId;
		updateVue.eCodeOfDevelopCompany = tgpj_BankAccountSupervisedModel.eCodeOfDevelopCompany;
		updateVue.bankId = tgpj_BankAccountSupervisedModel.bankId;
		updateVue.theNameOfBank = tgpj_BankAccountSupervisedModel.theNameOfBank;
		updateVue.shortNameOfBank = tgpj_BankAccountSupervisedModel.shortNameOfBank;
		updateVue.bankBranchId = tgpj_BankAccountSupervisedModel.bankBranchId;
		updateVue.theName = tgpj_BankAccountSupervisedModel.theName;
		updateVue.theAccount = tgpj_BankAccountSupervisedModel.theAccount;
		updateVue.remark = tgpj_BankAccountSupervisedModel.remark;
		updateVue.contactPerson = tgpj_BankAccountSupervisedModel.contactPerson;
		updateVue.contactPhone = tgpj_BankAccountSupervisedModel.contactPhone;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}

	function indexMethod(index) {
		return generalIndexMethod(index, listVue)
	}

	function mainDetailHandle(tableId) {
		console.log('mainDetailHandle tableId is '+tableId)
		enterNewTab(tableId,"监管账户详情","tgpj/Tgpj_BankAccountSupervisedDetail.shtml")
    }

    function mainAddHandle() {
		enterNewTab("","新增监管账户","tgpj/Tgpj_BankAccountSupervisedAdd.shtml")
    }

    function mainDeleteHandle() {
        generalDeleteModal(listVue,listVue.poName,undefined,"确认注销吗？","确认批量注销吗？")
    }

    function listItemSelectHandle(list) {
        // generalListItemSelectHandle(listVue,list)
        listVue.selectItem = [];
        listVue.selectWholeItem=[];
        for (var index = 0; index < list.length; index++) {
            var element = list[index].tableId;
            listVue.selectItem.push(element)
            listVue.selectWholeItem.push(list[index])
        }
        if(listVue.selectWholeItem.length==1){
            var selectItem=listVue.selectWholeItem[0]
			console.log('selectItem is ')
			console.log(selectItem)
            if(selectItem.isUsing==1){
                listVue.isEditDisable=true
				console.log('isEditDisable=true')
            }else{
                listVue.isEditDisable=false
                console.log('isEditDisable=false')
            }
        }
    }

    function companyDetailHandle(tableId) {
		enterNewTab(tableId,"开发企业详情","emmp/Emmp_CompanyInfoDetail.shtml")
    }

    function bankBranchDetailHandle(tableId) {
        enterNewTab(tableId,"开户行详情","emmp/Emmp_BankBranchDetail.shtml")
    }

    function mainEditHandle() {
        var list = listVue.selectItem
        enterDetail(list, "监管账号修改", "tgpj/Tgpj_BankAccountSupervisedEdit.shtml")
    }

    function resetSearch() {
        generalResetSearch(listVue,function () {
        	listVue.isUsing=""
            listVue.companyId = ""
            refresh()
        })
    }

    function mainExportExcelHandle() {
		generalExportExcel(listVue,listVue.poName,function () {
			refresh()
        })
    }

	function showLog() {
		// enterLogTab(listVue.poName)
	}

    function changeCompanyListener(data) {
        if (listVue.companyId != data.tableId) {
            listVue.companyId = data.tableId
            // listVue.refresh()
        }
    }

    function changeCompanyEmpty() {
        if (listVue.companyId != null) {
            listVue.companyId = null
            // listVue.refresh()
        }
    }

    function getCompanyList() {
		var form=getTotalListForm()
		form["theType"]=1
		form["exceptZhengTai"]=true
        serverRequest("../Emmp_CompanyInfoForSelect",form,function (jsonObj) {
            listVue.companyList=jsonObj.emmp_CompanyInfoList
        })
    }

    function sortChange(column) {
        listVue.orderBy=generalOrderByColumn(column)
        refresh()
    }


    function isUsingChange() {
		// refresh()
    }
    
    function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}

    function initData()
	{
		initButtonList();
        getCompanyList();
        listVue.refresh();
	}
	//------------------------方法定义-结束------------------//

	//------------------------数据初始化-开始----------------//
	// listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tgpj_BankAccountSupervisedListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Tgpj_BankAccountSupervisedList",
	"addInterface":"../Tgpj_BankAccountSupervisedAdd",
	"deleteInterface":"../Tgpj_BankAccountSupervisedDelete",
	"batchDeleteInterface":"../Tgpj_BankAccountSupervisedBatchDelete",
	"updateInterface":"../Tgpj_BankAccountSupervisedUpdate"
});
