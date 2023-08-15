(function(baseInfo){
	var listVue = new Vue({
		el : baseInfo.listDivId,
		data : {
			poName:"Emmp_BankBranch",
			interfaceVersion :19000101,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			keyword : "",
            isUsing:"",
			selectItem : [],
			isAllSelected : false,
			theState:0,//正常为0，删除为1
			userStartId:null,
			userStartList:[],
			userRecordId:null,
			userRecordList:[],
			emmp_BankBranchList:[],

            bankInfoId:"",
            bankInfoList:[],
            orderBy:"",

            diabaleShowLog: true,
            isUsingList : [
            	{tableId:"0",theName:"已启用"},
            	{tableId:"1",theName:"未启用"},
            ]
		},
		methods : {
			refresh : refresh,
			initData:initData,
            indexMethod: indexMethod,
			showLog: showLog,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			search:search,
			checkAllClicked : checkAllClicked,
            bankBranchAddHandle: bankBranchAddHandle,
            bankBranchDetailHandle : bankBranchDetailHandle,
            bankBranchEditHandle : bankBranchEditHandle,
            bankBranchDeleteHandle : bankBranchDeleteHandle,
            bankBranchGeneralDetailHandle : bankBranchGeneralDetailHandle,
			changePageNumber : function(data){
				listVue.pageNumber = data;
			},
            resetSearch:resetSearch,
            mainExportExcelHandle: mainExportExcelHandle,
            changeCountPerPage :function (data) {
                if (listVue.countPerPage != data) {
                    listVue.countPerPage = data;
                    listVue.refresh();
                }
            },
            listItemSelectHandle:listItemSelectHandle,
            changeBankInfoListener:changeBankInfoListener,
            changeBankBranchEmpty:changeBankBranchEmpty,
            changeIsUsing:changeIsUsing,
            sortChange:sortChange,
            changeIsUsing : function(data){
            	listVue.isUsing = data.tableId;
            },
            changeIsUsingEmpty : function(){
            	listVue.isUsing  = null;
            }
		},
		computed:{
			 
		},
		components : {
            'vue-select': VueSelect,
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
		var searchObj={
            interfaceVersion:listVue.interfaceVersion,
            pageNumber:listVue.pageNumber,
            countPerPage:listVue.countPerPage,
            totalPage:listVue.totalPage,
            keyword:listVue.keyword,
            theState:listVue.theState,
            userStartId:listVue.userStartId,
            userRecordId:listVue.userRecordId,
            isUsing:listVue.isUsing,
            bankId:listVue.bankInfoId,
			orderBy:listVue.orderBy,
        }
        // if(listVue.orderBy!=undefined && listVue.orderBy.length>0){
		// 	searchObj["orderBy"]=listVue.orderBy
		// }
		return searchObj
	}
	//列表操作--------------获取“删除资源”表单参数
	function getDeleteForm()
	{
		return{
			interfaceVersion:listVue.interfaceVersion,
			idArr:listVue.selectItem
		}
	}

	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		listVue.isAllSelected = (listVue.emmp_BankBranchList.length > 0)
		&&	(listVue.selectItem.length == listVue.emmp_BankBranchList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.emmp_BankBranchList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.emmp_BankBranchList.forEach(function(item) {
	    		listVue.selectItem.push(item.tableId);
	    	});
	    }
	}

	//列表操作--------------刷新
	function refresh()
	{
		getBankInfoList()
		getBankBrachList()
	}

	function getBankBrachList() {
        new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj){
            if(jsonObj.result != "success")
            {
                noty({"text":jsonObj.info,"type":"error","timeout":2000});
            }
            else
            {
                listVue.emmp_BankBranchList=jsonObj.emmp_BankBranchList;
                listVue.pageNumber=jsonObj.pageNumber;
                listVue.countPerPage=jsonObj.countPerPage;
                listVue.totalPage=jsonObj.totalPage;
                listVue.totalCount = jsonObj.totalCount;
                listVue.keyword=jsonObj.keyword;
                listVue.selectedItem=[];
                //动态跳转到锚点处，id="top"
                document.getElementById('emmp_BankBranchListDiv').scrollIntoView();
            }
        });
    }

	function getBankInfoList() {
		serverRequest("../Emmp_BankInfoForSelect",getTotalListForm(),function (jsonObj) {
			listVue.bankInfoList=jsonObj.emmp_BankInfoList
        })

    }

	function bankBranchEditHandle(tableId) {
        enterDetail(listVue.selectItem,"开户行修改","emmp/Emmp_BankBranchEdit.shtml")
    }
	
	//列表操作------------搜索
	function search()
	{
		listVue.pageNumber = 1;
		refresh();
	}

    function bankBranchAddHandle() {
		enterNewTab("",'新增开户行信息','emmp/Emmp_BankBranchAdd.shtml')
    }

    function bankBranchDetailHandle(tableId) {
		enterNewTab(tableId,"开户行详情",'emmp/Emmp_BankBranchDetail.shtml')
    }

    function bankBranchGeneralDetailHandle(tableId) {
		enterNewTab(tableId,'金融机构详情','emmp/Emmp_BankInfoDetail.shtml')
    }

    function bankBranchDeleteHandle() {
		generalDeleteModal(listVue,listVue.poName)
    }

    function resetSearch() {
        generalResetSearch(listVue,function () {
        	listVue.isUsing=""
			listVue.bankInfoId=null
            refresh()
        })
    }

    function mainExportExcelHandle()
    {
        generalExportExcel(listVue,listVue.poName,function () {
            refresh()
        })
    }

    function listItemSelectHandle(list) {
        generalListItemSelectHandle(listVue,list)
    }

    function indexMethod(index) {
        return generalIndexMethod(index, listVue)
    }

    function showLog() {
		enterLogTabWithTemplate(listVue.poName,listVue)
    }

    function changeBankInfoListener(data) {
        if (listVue.bankInfoId != data.tableId) {
            listVue.bankInfoId = data.tableId
            // listVue.refresh()
        }
    }

    function changeBankBranchEmpty() {
        if (listVue.bankInfoId != null) {
            listVue.bankInfoId = null
            // listVue.refresh()
        }
    }

    function changeIsUsing() {
		// refresh()
    }

    function sortChange(column) {
		listVue.orderBy=generalOrderByColumn(column)
		getBankBrachList()
    }
    //根据按钮权限，动态控制，该页面的所有“可控按钮”的显示与否
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}
    function initData()
	{
		initButtonList()
	}
	//------------------------方法定义-结束------------------//

    //------------------------数据初始化-开始----------------//
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#emmp_BankBranchListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"listInterface":"../Emmp_BankBranchList",
	"addInterface":"../Emmp_BankBranchAdd",
	"deleteInterface":"../Emmp_BankBranchDelete",
	"batchDeleteInterface":"../Emmp_BankBranchBatchDelete",
	"updateInterface":"../Emmp_BankBranchUpdate",
    "exportExcelInterface": "../Emmp_BankBranchExportExcel",
});
