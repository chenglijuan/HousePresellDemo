(function(baseInfo){
    var addVue = new Vue({
        el : baseInfo.addDivId,
        data : {
            interfaceVersion :19000101,
            pageNumber : 1,
            countPerPage : MAX_VALUE,
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
            developCompanyId:null,
            developCompanyList:[],
            projectId:null,
            projectList:[],
            tableId:"",
            tgpf_FundAppropriated_AFList:[],
            disabled:true,
        },
        methods : {
            refresh : refresh,
            initData:initData,
            indexMethod:indexMethod,
            getSearchForm:getSearchForm,
            getAddForm:getAddForm,
            search:search,
            reset:reset,
            tgpf_FundOverallPlanAdd:tgpf_FundOverallPlanAdd, //新增统筹
            tgpf_FundAppropriated_AFDetail:tgpf_FundAppropriated_AFDetail, //用款申请详情信息
            listItemSelectHandle:listItemSelectHandle,
            checkAllClicked : checkAllClicked,
            changePageNumber : function(data){
                addVue.pageNumber = data;
            },
        },
        computed:{

        },
        components : {
            'vue-nav' : PageNavigationVue
        },
        watch:{
            selectItem : selectedItemChanged,
        },
        mounted: function ()
        {
            laydate.render({
                elem: '#date061203020201',
                range:true,
            })
        }
    });

    //------------------------方法定义-开始------------------//

    function initData()
    {
        refresh();
    }

    //重置
    function reset()
    {
     $('#date061203020201').val("");
     refresh();
    }

    //列表操作--------------获取"搜索列表"表单参数
    function getSearchForm()
    {
        return {
            interfaceVersion:addVue.interfaceVersion,
            pageNumber:addVue.pageNumber,
            countPerPage:addVue.countPerPage,
            totalPage:addVue.totalPage,
            theState:addVue.theState,
            applyState: 2 , // 2 ： 已受理
            approvalState:"已完结",
            fundOverallPlanApplyDate:$('#date061203020201').val(),
        }
    }

    //列表操作------------搜索
    function search()
    {
        addVue.pageNumber = 1;
        refresh();
    }

    //列表操作--------------刷新
    function refresh()
    {
        new ServerInterface(baseInfo.listInterface).execute(addVue.getSearchForm(), function(jsonObj){
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                addVue.tgpf_FundAppropriated_AFList=jsonObj.tgpf_FundAppropriated_AFList;
            }
        });
    }

    //列表索引方法
    function indexMethod(index)
    {
        return generalIndexMethod(index, addVue);
    }

    function listItemSelectHandle(list)
    {
        generalListItemSelectHandle(addVue,list);
    }

    //选中状态有改变，需要更新“全选”按钮状态
    function selectedItemChanged()
    {
        addVue.isAllSelected = (addVue.tgpf_FundAppropriated_AFList.length > 0)
            &&	(addVue.selectItem.length == addVue.tgpf_FundAppropriated_AFList.length);

        if(addVue.selectItem.length >= 1)
        {
            addVue.disabled = false;
        }
        else
        {
            addVue.disabled = true;
        }
    }

    //列表操作--------------“全选”按钮被点击
    function checkAllClicked()
    {
        if(addVue.selectItem.length == addVue.tgpf_FundAppropriated_AFList.length)
        {
            addVue.selectItem = [];
        }
        else
        {
            addVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
            addVue.tgpf_FundAppropriated_AFList.forEach(function(item) {
                addVue.selectItem.push(item.tableId);
            });
        }
    }

    //用款申请详情
    function tgpf_FundAppropriated_AFDetail(tableId,projectId)
    {
        var theId = tableId +'_'+projectId;
        enterNewTab(theId, '用款申请详情', 'tgpf/Tgpf_FundAppropriated_AFDetail.shtml');
    }

    function getAddForm()
    {
        return{
            interfaceVersion:addVue.interfaceVersion,
            idArr:addVue.selectItem,
            buttonType:1,
        }
    }

    //新增统筹单 和 统筹账户信息
    function tgpf_FundOverallPlanAdd()
    {
        new ServerInterface(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj){
            if(jsonObj.result != "success")
            {
                generalErrorModal(jsonObj);
            }
            else
            {
                addVue.tableId = jsonObj.tableId;
                enterNewTabCloseCurrent(addVue.tableId,'资金统筹修改','tgpf/Tgpf_FundOverallPlanEdit.shtml');
            }
        });
    }


    //------------------------方法定义-结束------------------//

    //------------------------数据初始化-开始----------------//
    addVue.initData();
    //------------------------数据初始化-结束----------------//
})({
    "addDivId":"#tgpf_FundOverallPlanAddDiv",
    "listInterface":"../Tgpf_FundAppropriated_AFList",
    "addInterface":"../Tgpf_FundOverallPlanAdd",
});