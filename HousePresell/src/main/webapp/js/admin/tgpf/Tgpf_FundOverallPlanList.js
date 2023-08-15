(function(baseInfo) {
    var listVue = new Vue({
        el: baseInfo.listDivId,
        data: {
            interfaceVersion: 19000101,
            pageNumber: 1,
            countPerPage: 10,
            totalPage: 1,
            totalCount: 1,
            selectItem: [],
            isAllSelected: false,
            theState: 0, //正常为0，删除为1
            userStartId: null,
            userStartList: [],
            userRecordId: null,
            userRecordList: [],
            tgpf_FundOverallPlanList: [],
            upDisabled:true,//修改按钮禁用
            deDisabled:true,//删除按钮禁用
            planDisabled:true,//用款计划和申请清单按钮禁用
            keyword: "", //关键字
            busiState:"",//业务状态
            approvalState:"",//审批状态
            poName:"Tgpf_FundOverallPlan",
            //排序
            orderBy:null,
            busiStateList :[
            	{tableId:"统筹中",theName:"统筹中"},
            	{tableId:"已统筹",theName:"已统筹"}
            ],
            approvalStateList : [
            	{tableId:"待提交",theName:"待提交"},
            	{tableId:"审核中",theName:"审核中"},
            	{tableId:"已完结",theName:"已完结"},
            ]
        },
        methods: {
            refresh: refresh,
            initData: initData,
            reset:reset,
            getSearchForm: getSearchForm,
            getDeleteForm: getDeleteForm,
            indexMethod:indexMethod,
            busiStateChange:function(data){
            	this.busiState = data.tableId;
            },
            changeBusiStateEmpty : function(){
            	this.busiState = null;
            },
            //----------------排序---------------------//
            sortChange:sortChange,
            //----------------排序---------------------//
            tgpf_FundOverallPlanAdd:tgpf_FundOverallPlanAdd,//新增
            tgpf_FundOverallPlanEdit:tgpf_FundOverallPlanEdit,//修改
            tgpf_FundOverallPlanDel: tgpf_FundOverallPlanDel,//删除
            listItemSelectHandle: listItemSelectHandle, //获取选中复选框所在行的tableId
            tgpf_FundOverallPlanListDetail:tgpf_FundOverallPlanListDetail,// //统筹详情
            tgpf_FundAppropriated_AFDetail:tgpf_FundAppropriated_AFDetail, //用户计划
            tgpf_FundOverallPlan_AFList:tgpf_FundOverallPlan_AFList,//申请清单
            search: search,
            checkAllClicked: checkAllClicked,
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
            approvalChange: function(data){
            	listVue.approvalState = data.tableId;
            },
            changeApprovalStateEmpty : function(){
            	listVue.approvalState = null;
            }
        },
        computed: {

        },
        components: {
            'vue-nav': PageNavigationVue,
            'vue-listselect': VueListSelect,
        },
        watch: {
            pageNumber: refresh,
            selectItem: selectedItemChanged,
        },
        mounted: function ()
        {
            laydate.render({
                elem: '#date0612030201'
            })
        }
    });

    //------------------------方法定义-开始------------------//

    //根据按钮权限，动态控制，该页面的所有“可控按钮”的显示与否
    function initButtonList()
    {
        //封装在BaseJs中，每个页面需要控制按钮的就要
        getButtonList();
    }
    function initData() {
        initButtonList();
        refresh();
    }

    //重置
    function  reset()
    {
        listVue.keyword="";
        $('#date0612030201').val("");
        listVue.busiState = "";
        listVue.approvalState = "";
        listVue.orderBy = "";
        refresh();
    }

    //排序
    function  sortChange(column,prop,order)
    {
        listVue.orderBy = generalOrderByColumn(column);
        search();
    }

    //列表操作--------------获取"搜索列表"表单参数
    function getSearchForm() {
        return {
            interfaceVersion: listVue.interfaceVersion,
            pageNumber: listVue.pageNumber,
            countPerPage: listVue.countPerPage,
            totalPage: listVue.totalPage,
            keyword: listVue.keyword,
            fundOverallPlanDate: $('#date0612030201').val(),
            theState: listVue.theState,
            busiState:listVue.busiState,
            approvalState:listVue.approvalState,
            orderBy:listVue.orderBy,
        }
    }

    //列表操作--------------获取“删除资源”表单参数
    function getDeleteForm() {
        return {
            interfaceVersion: listVue.interfaceVersion,
            idArr: listVue.selectItem
        }
    }

    function indexMethod(index)
    {
        return generalIndexMethod(index, listVue)
    }

    function listItemSelectHandle(list)
    {
        //修改按钮禁用状态
        if(list.length==1 && list[0].approvalState == "待提交")
        {
            listVue.upDisabled = false;
        }
        else
        {
            listVue.upDisabled = true;
        }

        //用款计划和申请清单
        if(list.length == 1)
        {
            listVue.planDisabled = false;
        }
        else
        {
            listVue.planDisabled = true;
        }

        //删除按钮禁用状态
        if(list.length >= 1)
        {
            var count = 0;
            for (var i = 0; i < list.length; i++)
            {
                if(list[i].approvalState != "待提交" )
                {
                    count++;
                }
            }
            if(count == 0)
            {
                listVue.deDisabled = false;
            }
            else
            {
                listVue.deDisabled = true;
            }
        }
        else
        {
            listVue.deDisabled = true;
        }
        generalListItemSelectHandle(listVue,list);
    }

    //列表操作--------------刷新
    function refresh() {
        new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj) {
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                listVue.tgpf_FundOverallPlanList = jsonObj.tgpf_FundOverallPlanList;
                listVue.pageNumber=jsonObj.pageNumber;
                listVue.countPerPage=jsonObj.countPerPage;
                listVue.totalPage=jsonObj.totalPage;
                listVue.totalCount = jsonObj.totalCount;
                listVue.keyword=jsonObj.keyword;
                listVue.selectedItem=[];
            }
        });
    }

    //列表操作------------搜索
    function search() {
        listVue.pageNumber = 1;
        refresh();
    }

    //新增按钮 - 跳转方法
    function tgpf_FundOverallPlanAdd()
    {
        enterNewTab("", "新增统筹", "tgpf/Tgpf_FundOverallPlanAdd.shtml");
    }

    //用款计划
    function tgpf_FundAppropriated_AFDetail()
    {
        var tableId = listVue.selectItem[0];
        enterNewTab(tableId,'用款计划','tgpf/Tgpf_FundOverallPlanDetail.shtml');
    }

    //申请清单
    function tgpf_FundOverallPlan_AFList()
    {
        var tableId = listVue.selectItem[0];
        enterNewTab(tableId,'申请清单','tgpf/Tgpf_FundOverallPlan_AFList.shtml');
    }

    //统筹详情
    function tgpf_FundOverallPlanListDetail(tableId)
    {
        enterNewTab(tableId,'资金统筹详情','tgpf/Tgpf_FundOverallPlanListDetail.shtml');
    }

    //修改
    function tgpf_FundOverallPlanEdit()
    {
        var tableId = listVue.selectItem[0];
        enterNewTab(tableId, "资金统筹修改", "tgpf/Tgpf_FundOverallPlanEdit.shtml");
    }

    //删除按钮
    function tgpf_FundOverallPlanDel()
    {
        generalDeleteModal(listVue,listVue.poName);
    }


	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{
		listVue.isAllSelected = (listVue.tgpf_FundOverallPlanList.length > 0) &&
			(listVue.selectItem.length == listVue.tgpf_FundOverallPlanList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
		if(listVue.selectItem.length == listVue.tgpf_FundOverallPlanList.length)
		{
			listVue.selectItem = [];
		}
		else
			{
			listVue.selectItem = []; //解决：已经选择一个复选框后，再次点击全选样式问题
			listVue.tgpf_FundOverallPlanList.forEach(function(item) {
				listVue.selectItem.push(item.tableId);
			});
		}
	}
	//------------------------方法定义-结束------------------//

    //------------------------数据初始化-开始----------------//
    listVue.initData();
    //------------------------数据初始化-结束----------------//
})({
    "listDivId": "#tgpf_FundOverallPlanListDiv",
    "updateDivId": "#updateModel",
    "addDivId": "#addModal",
    "listInterface": "../Tgpf_FundOverallPlanList",
    "deleteInterface": "../Tgpf_FundOverallPlanDelete",
});