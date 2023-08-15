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
            selectModel : [],
			isAllSelected : false,
			theState:0,//正常为0，删除为1
            theType: "",
            enableState:"0",
			userStartId:null,
			userRecordId:null,
			tgpj_EscrowStandardVerMngList:[],
            orderBy:"",

            //修改、删除按钮是否可操作
            enableEdit: false,
            enableDelete: false,
            theTypeList : [
				{tableId:"1",theName:"托管金额"},
				{tableId:"2",theName:"托管比例"},
			],
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			listItemSelectHandle: listItemSelectHandle,
			tgpj_EscrowStandardVerMngDel : tgpj_EscrowStandardVerMngDel,
			search : search,
            resetSearchInfo : resetSearchInfo,
            escrowStandardVerMngAddHandle: escrowStandardVerMngAddHandle,
            escrowStandardVerMngEditHandle: escrowStandardVerMngEditHandle,
            escrowStandardVerMngDeleteHandle: escrowStandardVerMngDeleteHandle,
            tgpj_EscrowStandardVerMngDetailPageOpen: tgpj_EscrowStandardVerMngDetailPageOpen,
            escrowStandardVerMngExportExcelHandle: escrowStandardVerMngExportExcelHandle,
			checkAllClicked : checkAllClicked,
			changePageNumber : function(data){
				listVue.pageNumber = data;
			},
            indexMethod: function (index) {
                if (listVue.pageNumber > 1) {
                    return (listVue.pageNumber - 1) * listVue.countPerPage - 0 + (index - 0 + 1);
                }
                if (listVue.pageNumber <= 1) {
                    return index - 0 + 1;
                }
            },

            sortChange:sortChange,
            changeBusiState: changeBusiState,
            changeTheType : function(data){
            	this.theType = data.tableId;
            },
            theTypeEmpty : function(){
            	this.theType = null;
            }
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue,
			'vue-listselect' : VueListSelect,
		},
		watch:{
//			pageNumber : refresh,
			selectItem : selectedItemChanged,
		}
	});
	//------------------------方法定义-开始------------------//

    //列表操作----------关键字搜索、下拉列表、排序筛选
    function sortChange(column) {
        // console.log(column);
        listVue.orderBy=generalOrderByColumn(column);
        refresh();
    }

	function changeBusiState() {
        // refresh();
    }

    //按钮操作------------搜索
    function search()
    {
        listVue.pageNumber = 1;
        refresh();
    }

    //按钮操作------------重置搜索
    function resetSearchInfo() {
        listVue.keyword = "";
        listVue.theType = "";
        listVue.enableState = "0";
        listVue.pageNumber = 1;
        refresh();
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
            theType:this.theType,
            enableState:this.enableState,
            orderBy:listVue.orderBy,
            // userStartId:this.userStartId,
            // userRecordId:this.userRecordId,
        }
    }

    //列表操作--------------刷新
    function refresh()
    {
    	// console.log(listVue.getSearchForm());
        new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj){
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                listVue.tgpj_EscrowStandardVerMngList=jsonObj.tgpj_EscrowStandardVerMngList;
                listVue.pageNumber=jsonObj.pageNumber;
                listVue.countPerPage=jsonObj.countPerPage;
                listVue.totalPage=jsonObj.totalPage;
                listVue.totalCount = jsonObj.totalCount;
                listVue.keyword=jsonObj.keyword;
                listVue.selectedItem=[];
                listVue.selectModel=[];
                //动态跳转到锚点处，id="top"
                document.getElementById('tgpj_EscrowStandardVerMngListDiv').scrollIntoView();
            }
        });
    }

	//按钮操作--------------跳转到托管标准新增页面
	function escrowStandardVerMngAddHandle()
    {
        enterNewTab("", "新增托管标准版本", 'tgpj/Tgpj_EscrowStandardVerMngAdd.shtml');
        // $("#tabContainer").data("tabs").addTab({ id: 'Tgpj_EscrowStandardVerMngAdd', text: '托管标准管理新增', closeable: true, url: 'tgpj/Tgpj_EscrowStandardVerMngAdd.shtml' });
    }

    //按钮操作--------------跳转到托管标准编辑页面
    function escrowStandardVerMngEditHandle()
    {
    	if (listVue.selectItem.length == 1)
		{
            var theEscrowStandardVerMng = listVue.selectModel[0];
            if (theEscrowStandardVerMng.busiState != "已备案" && theEscrowStandardVerMng.approvalState != "审核中")
            {
                var tableId = listVue.selectItem[0];
                console.log("选中的tableId:"+tableId);
                tableId = 'EscrowStandardVerMngEdit_'+tableId;
                enterNewTab(tableId, "编辑托管标准版本", 'tgpj/Tgpj_EscrowStandardVerMngEdit.shtml');
                // $("#tabContainer").data("tabs").addTab({ id: tableId, text: '托管标准管理修改', closeable: true, url: 'tgpj/Tgpj_EscrowStandardVerMngEdit.shtml' });
            }
            else
			{
                if (theEscrowStandardVerMng.busiState == "已备案")
                {
                    noty({"text":"您选择的托管标准版本已备案","type":"error","timeout":2000});
                }
                if (theEscrowStandardVerMng.approvalState == "审核中")
                {
                    noty({"text":"您选择的托管标准版本正在审批中","type":"error","timeout":2000});
                }
			}
		}
    }

    //按钮操作------导出Excel
    function escrowStandardVerMngExportExcelHandle()
    {
        // console.log("导出Excel-----");
        var model = {
            interfaceVersion:listVue.interfaceVersion,
            keyword:listVue.keyword,
            theType:listVue.theType,
            idArr:listVue.selectItem,
        }
        // console.log(model);
        new ServerInterface(baseInfo.escrowStandardVerMngExcelInterface).execute(model, function (jsonObj) {
            if (jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                window.location.href=jsonObj.fileDownloadPath;
                listVue.selectItem = [];
                refresh();
            }
        });
    }

    //列表操作--------------跳转到托管标准详情页面
	function tgpj_EscrowStandardVerMngDetailPageOpen(tableId)
	{
        enterNewTab(tableId, "托管标准版本详情", 'tgpj/Tgpj_EscrowStandardVerMngDetail.shtml');
    }

	//选择托管标准
    function listItemSelectHandle(val)
    {
        listVue.selectItem = [];
        listVue.selectModel = [];
        for (var index = 0; index < val.length; index++) {
            var element = val[index].tableId;
            listVue.selectItem.push(element)
            listVue.selectModel = val;
        }
        // console.log(listVue.selectItem);

        listVue.enableEdit = true;
        listVue.enableDelete = true;
        for (var index = 0; index < val.length; index++) {
            var itemModel = val[index];
            if (itemModel.busiState == "已备案" || itemModel.approvalState == "审核中")
            {
                listVue.enableEdit = false;
                listVue.enableDelete = false;
                break;
            }
        }
    }

    //按钮操作--------删除托管标准操作
	function escrowStandardVerMngDeleteHandle()
	{
		if (listVue.selectItem.length == 0)
		{
            noty({"text":"请选择要删除的托管标准版本","type":"error","timeout":2000});
            // generalErrorModal(null, "请选择要删除的托管标准版本");
            return;
		}

        //删除按钮listVue.selectItem
        var disableDeleteProjectItem = new Array();
        for (var index=0; index < listVue.selectModel.length; index++)
        {
            var itemEscrowStandardVer = listVue.selectModel[index];
            if (itemEscrowStandardVer.busiState == "已备案" || itemEscrowStandardVer.approvalState == "审核中")
            {
                disableDeleteProjectItem.push(itemEscrowStandardVer.theName);
            }
        }
        if (disableDeleteProjectItem.length > 0)
        {
            console.log(disableDeleteProjectItem);
            var errorText = "托管标准版本：\""+disableDeleteProjectItem.join("，")+"\"处于已备案或审核中状态，不能删除";
            noty({"text":errorText,"type":"error","timeout":2000});

            // generalErrorModal(null, errorText);
        }
        else
        {
            generalSelectModal(function(){
                tgpj_EscrowStandardVerMngDel();
            }, "确认删除吗？");
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

    //按钮操作--------批量删除托管标准
	function tgpj_EscrowStandardVerMngDel()
	{
        new ServerInterface(baseInfo.deleteInterface).execute(listVue.getDeleteForm(), function(jsonObj){
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                listVue.selectItem = [];
                listVue.selectModel = [];
                refresh();
            }
        });
	}

	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		listVue.isAllSelected = (listVue.tgpj_EscrowStandardVerMngList.length > 0)
		&&	(listVue.selectItem.length == listVue.tgpj_EscrowStandardVerMngList.length);
	}

	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgpj_EscrowStandardVerMngList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgpj_EscrowStandardVerMngList.forEach(function(item) {
	    		listVue.selectItem.push(item.tableId);
	    	});
	    }
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
        listVue.refresh();
	}
	//------------------------方法定义-结束------------------//

	//------------------------数据初始化-开始----------------//
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tgpj_EscrowStandardVerMngListDiv",
	"listInterface":"../Tgpj_EscrowStandardVerMngList",
	"deleteInterface":"../Tgpj_EscrowStandardVerMngBatchDelete",
	"escrowStandardVerMngExcelInterface":"../Tgpj_EscrowStandardVerMngExportExcel",
});
