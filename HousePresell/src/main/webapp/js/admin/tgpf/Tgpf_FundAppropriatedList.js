(function(baseInfo){
	var listVue = new Vue({
		el : baseInfo.listDivId,
		data : {
			interfaceVersion :19000101,
			pageNumber : 1,
            countPerPage :10,
			totalPage : 1,
			totalCount : 1,
			keyword : "",
			selectItem : [],
			isAllSelected : false,
			theState:0,//正常为0，删除为1
			tgpf_FundAppropriatedList:[],
            applyState:"",//申请单状态
            orderBy:null,
            projectId: "",
            projectList: [],
            applyStateList : [
            	{tableId:"4",theName:"已统筹"},
            	{tableId:"5",theName:"拨付中"},
            	{tableId:"6",theName:"已拨付"},
            ],
            
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
            search:search,    //搜索资源列表
            reset:reset,      //重置
            sortChange:sortChange,
            listItemSelectHandle: listItemSelectHandle,
            //----------------筛选---------------------//
            applyStateChange:function(data){
            	this.applyState = data.tableId;
            },
            applyStateChangeEmpty : function(){
            	this.applyState = null;
            },
            changeprojectListener: changeprojectListener,
            changeProjectEmpty: changeProjectEmpty,
            //----------------筛选---------------------//
            indexMethod:indexMethod,
            tgpf_FundAppropriatedDetail:tgpf_FundAppropriatedDetail,
			checkAllClicked : checkAllClicked,
            exportForm: exportForm,
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
			
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue,
            'vue-listselect': VueListSelect
		},
		watch:{
//			pageNumber : refresh,
			selectItem : selectedItemChanged,
		},
        mounted: function ()
        {
            laydate.render({
                elem: '#date0612030301',
            })
            laydate.render({
                elem: '#date0612030302',
            })
        }
	});

	//------------------------方法定义-开始------------------//

    function initData()
    {
        refresh();
        getProjectList();
    }

    function getProjectList() {
        serverRequest("../Empj_ProjectInfoForSelect",getTotalListForm(),function (jsonObj) {
            listVue.projectList=jsonObj.empj_ProjectInfoList;
        })
    }
    //重置
    function  reset()
    {
        listVue.keyword="";
        listVue.applyState="";
        listVue.projectId = "";
        $('#date0612030301').val("");
        $('#date0612030302').val(""),
        listVue.orderBy = "";
        refresh();
    }

    function changeprojectListener(data) {
        if (listVue.projectId != data.tableId) {
            listVue.projectId = data.tableId;
            // refresh()
        }
    }

    function changeProjectEmpty() {
        if (listVue.projectId != null) {
            listVue.projectId = null;
            // refresh()
        }
    }

    //排序
    function  sortChange(column,prop,order)
    {
        listVue.orderBy = generalOrderByColumn(column);
        search();
    }
	 
	//列表操作--------------获取"搜索列表"表单参数
	function getSearchForm()
	{
		return {
            interfaceVersion:listVue.interfaceVersion,
            pageNumber:listVue.pageNumber,
            countPerPage:listVue.countPerPage,
            totalPage:listVue.totalPage,
            theState:listVue.theState,
            keyword:listVue.keyword,
            projectId:listVue.projectId,
            fundAppropriatedApplyDate:$("#date0612030301").val(),
            applyState:listVue.applyState,
            payoutState1: 4,
            payoutState2: 5,
            payoutState3: 6,
            fundOverallPlanDate:$('#date0612030302').val(),
            orderBy:listVue.orderBy,
		}
	}

    //列表操作------------搜索
    function search()
    {
        listVue.pageNumber = 1;
        refresh();
    }

    //列表操作--------------刷新
    function refresh()
    {
        new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj){
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                listVue.tgpf_FundAppropriatedList=jsonObj.tgpf_FundAppropriated_AFList;

                listVue.pageNumber=jsonObj.pageNumber;
                listVue.countPerPage=jsonObj.countPerPage;
                listVue.totalPage=jsonObj.totalPage;
                listVue.totalCount = jsonObj.totalCount;
                listVue.keyword=jsonObj.keyword;
                listVue.selectedItem=[];
            }
        });
    }

    //列表索引方法
    function indexMethod(index)
    {
        return generalIndexMethod(index, listVue)
    }

    function listItemSelectHandle(list)
    {
        generalListItemSelectHandle(listVue,list)
    }


    //选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		listVue.isAllSelected = (listVue.tgpf_FundAppropriatedList.length > 0)
		&&	(listVue.selectItem.length == listVue.tgpf_FundAppropriatedList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgpf_FundAppropriatedList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgpf_FundAppropriatedList.forEach(function(item) {
	    		listVue.selectItem.push(item.tableId);
	    	});
	    }
	}

    function exportForm(){
        new ServerInterface(baseInfo.exportInterface).execute(listVue.getSearchForm(), function(jsonObj){
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj ,jsonObj.info);
            }
            else
            {
                window.location.href="../"+jsonObj.fileURL;
            }
        });
    }

    //跳转方法 - 详情
    function tgpf_FundAppropriatedDetail(tableId)
    {
        enterNewTab(tableId, "资金拨付详情", "tgpf/Tgpf_FundAppropriatedDetail.shtml");
    }
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tgpf_FundAppropriatedListDiv",
	"listInterface":"../Tgpf_FundAppropriated_AFList",
    "exportInterface": "../Tgpf_FundAppropriatedExport", //导出接口

});