(function (baseInfo) {
    var listVue = new Vue({
        el: baseInfo.listDivId,
        data: {
            interfaceVersion: 19000101,
            pageNumber: 1,
            countPerPage: 20,
            totalPage: 1,
            totalCount: 1,
            keyword: "",
            selectItem: [],
            isAllSelected: false,
            theState: 0,//正常为0，删除为1
            busiState: 0,
            empj_PaymentBondList: [],
            projectId: "", //项目名称
            companyId: "", //开发企业
            qs_companyNameList: [], //页面加载显示开发企业
            qs_projectNameList: [], //显示项目名称
            editDisabled: true,
            delDisabled: true,
            cancelDisabled: true,
            errMsg: "",
            busiStateApply: "",
            applyDate: "",
            busiStateApplyList: [
                {tableId: "待提交", theName: "待提交"},
                {tableId: "审核中", theName: "审核中"},
                {tableId: "已完结", theName: "已完结"},
            ],
            approvalState: '',
        },
        methods: {
            refresh: refresh,
            initData: initData,
            getSearchForm: getSearchForm,
            getBatchDeleteForm: getBatchDeleteForm,
            empj_PaymentGuaranteeApplicationBatchDel: empj_PaymentGuaranteeApplicationBatchDel,
            search: search,
            resetSearchInfo: resetSearchInfo,
            paymentGuaranteeApplicationAddHandle: paymentGuaranteeApplicationAddHandle,//新增
            paymentGuaranteeApplicationEditHandle: paymentGuaranteeApplicationEditHandle,//编辑
            paymentGuaranteeApplicationDelHandle: paymentGuaranteeApplicationDelHandle,//删除
            paymentGuaranteeApplicationCancelHandle: paymentGuaranteeApplicationCancelHandle,//撤销
            paymentGuaranteeApplicationExportExcelHandle: paymentGuaranteeApplicationExportExcelHandle,
            changePageNumber: function (data) {
                //listVue.pageNumber = data;
                if (listVue.pageNumber != data) {
                    listVue.pageNumber = data;
                    listVue.refresh();
                }
            },
            changeCountPerPage: function (data) {
                if (listVue.countPerPage != data) {
                    listVue.countPerPage = data;
                    listVue.refresh();
                }
            },
            loadCompanyName: loadCompanyNameFun, //页面加载显示开发企业方法
            changeCompanyHandle: changeCompanyHandleFun, //改变企业名称的方法
            handleSelectionChange: handleSelectionChange,
            indexMethod: indexMethod,
            paymentGuaranteeApplicationDetailHandle: paymentGuaranteeApplicationDetailHandle,
            getCompanyId: function (data) {
                listVue.companyId = data.tableId;
                listVue.changeCompanyHandle();
            },
            emptyCompanyId: function () {
                listVue.companyId = null;
                listVue.qs_projectNameList = [];
            },
            getProjectId: function (data) {
                listVue.projectId = data.tableId;
            },
            emptyProjectId: function () {
                listVue.projectId = null;
            },
            getBusiStateApply: function (data) {
                listVue.approvalState = data.tableId;
            },
            emptyBusiStateApply: function () {
                listVue.busiStateApply = null;
            },
        },
        computed: {},
        components: {
            'vue-nav': PageNavigationVue,
            'vue-select': VueSelect,
            'vue-listselect': VueListSelect,
            'vue-listsearch': VueListSearch,
        },
        watch: {
            //pageNumber : refresh,
        },
        mounted: function () {
            /**
             * 初始化日期插件
             */
            laydate.render({
                elem: '#paymentGuaranteeAppDate',
                done: function (value, date, endDate) {
                    listVue.applyDate = value;
                }
            });
        }
    });
    //------------------------方法定义-开始------------------//

    //列表操作--------------获取"搜索列表"表单参数
    function getSearchForm() {
        return {
            interfaceVersion: this.interfaceVersion,
            pageNumber: this.pageNumber,
            countPerPage: this.countPerPage,
            totalPage: this.totalPage,
            keyword: this.keyword,
            busiState: this.busiStateApply,
            theState: this.theState,
            applyDate: this.applyDate,
            companyId: this.companyId,
            projectId: this.projectId,
            approvalState: this.approvalState,
        }
    }

    //列表操作----------序号
    function indexMethod(index) {
        return generalIndexMethod(index, listVue)
    }

    //列表操作--------------获取“删除资源”表单参数
    function getBatchDeleteForm() {
        return {
            interfaceVersion: this.interfaceVersion,
            idArr: this.selectItem
        }
    }

    //列表操作-----------------------页面加载显示开发企业
    function loadCompanyNameFun() {
        loadCompanyName(listVue, baseInfo.companyNameInterface, function (jsonObj) {
            listVue.qs_companyNameList = jsonObj.emmp_CompanyInfoList;
        }, listVue.errMsg, baseInfo.edModelDivId);
    }

    //列表操作--------------------改变开发企业的方法
    function changeCompanyHandleFun() {
        changeCompanyHandle(listVue, baseInfo.changeCompanyInterface, function (jsonObj) {
            listVue.qs_projectNameList = jsonObj.empj_ProjectInfoList;
        }, listVue.errMsg, baseInfo.edModelDivId)
    }

    //列表操作------------搜索
    function search() {
        listVue.pageNumber = 1;
        refresh();
    }

    //列表操作------------重置搜索
    function resetSearchInfo() {
        this.keyword = "";
        this.companyId = "";
        this.projectId = "";
        this.approvalState = "";
        this.paymentGuaranteeAppDate = "";
        this.applyDate = "";
        listVue.pageNumber = 1;
        refresh();
    }


    //列表操作---选择
    function handleSelectionChange(val) {
        console.log(val)
        if (val.length == 1) {
            listVue.editDisabled = false;
            listVue.cancelDisabled = false;
        } else {
            listVue.editDisabled = true;
            listVue.cancelDisabled = true;
        }

        if (val.length >= 1) {
            listVue.delDisabled = false;
        } else {
            listVue.delDisabled = true;
        }

        listVue.selectItem = [];
        for (var index = 0; index < val.length; index++) {
            if (val[index].busiState == 0) {
                listVue.cancelDisabled = true;
            }
            if (val[index].busiState == 0) {
                listVue.delDisabled = false;
                listVue.editDisabled = false;
            } else {
                listVue.delDisabled = true;
                listVue.editDisabled = true;
            }

            if (val[index].approvalState == 0 || val[index].approvalState == '待提交') {
                listVue.delDisabled = false;
                listVue.editDisabled = false;
            } else {
                listVue.delDisabled = true;
                listVue.editDisabled = true;
            }

            if (val[index].busiState == 2) {
                listVue.cancelDisabled = false;
            } else {
                listVue.cancelDisabled = true;
            }

            var element = val[index].tableId;
            listVue.selectItem.push(element)
        }
    }


    function paymentGuaranteeApplicationDelHandle() {
        $(baseInfo.deleteDivId).modal({
            backdrop: "static"
        })
    }


    //批量删除
    function empj_PaymentGuaranteeApplicationBatchDel() {
        $(baseInfo.deleteDivId).modal("hide");
        new ServerInterface(baseInfo.batchDeleteInterface).execute(listVue.getBatchDeleteForm(), function (jsonObj) {
            if (jsonObj.result != "success") {
                generalErrorModal(jsonObj);
            } else {
                generalSuccessModal();
                listVue.selectItem = [];
                refresh();
            }
        });
    }

    //列表操作--------------刷新
    function refresh() {
        new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj){
        	if(jsonObj.result != "success")
        	{
        		generalErrorModal(jsonObj,jsonObj.info);
        	}
        	else
        	{
        		listVue.empj_PaymentBondList=jsonObj.empj_PaymentBondList;
        		listVue.empj_PaymentBondList.forEach(function(item){
        			item.alreadyActualAmount = addThousands(item.alreadyActualAmount);
        			item.actualAmount = addThousands(item.actualAmount);
        			item.guaranteedAmount = addThousands(item.guaranteedAmount)
        			item.guaranteedSumAmount = addThousands(item.guaranteedSumAmount)
        		});
        		listVue.busiState=jsonObj.busiState;
        		listVue.pageNumber=jsonObj.pageNumber;
        		listVue.countPerPage=jsonObj.countPerPage;
        		listVue.totalPage=jsonObj.totalPage;
        		listVue.totalCount = jsonObj.totalCount;
        		listVue.keyword=jsonObj.keyword;
        		listVue.selectedItem=[];
        		//动态跳转到锚点处，id="top"
        		document.getElementById('Empj_PaymentBondListDiv').scrollIntoView();
        	}
        });
    }


    //按钮操作--------导出Excel
    function paymentGuaranteeApplicationExportExcelHandle() {
        var model = {
            interfaceVersion: listVue.interfaceVersion,
            idArr: listVue.selectItem,
            keyword: listVue.keyword,
        }
        new ServerInterface(baseInfo.exportExcelInterface).execute(model, function (jsonObj) {
            if (jsonObj.result != "success") {
                generalErrorModal(jsonObj, jsonObj.info);
            } else {
                window.location.href = jsonObj.fileDownloadPath;
                listVue.selectItem = [];
                refresh();
            }
        });
    }

    //按钮操作--------跳转到添加项目页面
    function paymentGuaranteeApplicationAddHandle() {
        enterNewTab('', '新增支付保函申请', 'empj/Empj_PaymentBondAdd.shtml');
    }

    //按钮操作-------跳转到修改项目页面
    function paymentGuaranteeApplicationEditHandle() {
        if (listVue.selectItem.length == 1) {
            enterNextTab(listVue.selectItem[0], '修改支付保函申请', 'empj/Empj_PaymentBondEdit.shtml', listVue.selectItem[0] + "06120401");
            /* console.log(tableId);
             var theTableId = 'Empj_PaymentGuaranteeApplicationEdit_' + tableId;
             $("#tabContainer").data("tabs").addTab({id: theTableId , text: '修改支付保函申请', closeable: true, url: 'empj/Empj_PaymentGuaranteeApplicationEdit.shtml'});*/
        } else {
            noty({"text": "请选择一个且只选一个要修改的项目", "type": "error", "timeout": 2000});
        }
    }

    //列表操作----------------------撤销按钮
    function paymentGuaranteeApplicationCancelHandle() {
        enterNextTab(listVue.selectItem[0], '撤销支付保函申请', 'empj/Empj_CancelPaymentGuaranteeApplicationEdit.shtml', listVue.selectItem[0] + "06120403");
        /*var tableId = listVue.selectItem[0];
        $("#tabContainer").data("tabs").addTab({ id:"jump_"+tableId, text: '撤销支付保函申请', closeable: true, url: 'empj/Empj_CancelPaymentGuaranteeApplicationAdd.shtml' });*/
    }

    function paymentGuaranteeApplicationDetailHandle(tableId) {
        enterNextTab(tableId, '支付保函申请详情', 'empj/Empj_PaymentBondDetail.shtml', tableId + "06120401");
        /*$("#tabContainer").data("tabs").addTab({ id: "jump_"+tableId, text: '支付保函申请详情', closeable: true, url: 'empj/Empj_PaymentGuaranteeApplicationDetail.shtml' });*/
    }

    function initButtonList() {
        //封装在BaseJs中，每个页面需要控制按钮的就要
        getButtonList();
    }

    function initData() {
        initButtonList();
        listVue.refresh();
    }

    //------------------------方法定义-结束------------------//

    //------------------------数据初始化-开始----------------//
    listVue.initData();
    listVue.loadCompanyName();
    //------------------------数据初始化-结束----------------//
})({
    "listDivId": "#Empj_PaymentBondListDiv",
    "companyNameInterface": "../Emmp_CompanyInfoList", //显示开发企业名称接口
    "changeCompanyInterface": "../Empj_ProjectInfoList", //改变企业名称接口
    "deleteDivId": "#deletePaymentBondList",
    "edModelDivId": "#edModelPaymentGuaranteeApplicationList",
    "sdModelDivId": "#sdModelPaymentGuaranteeApplicationList",
    "listInterface": "../Empj_PaymentBondList",//列表页面加载接口
    "batchDeleteInterface": "../Empj_PaymentBondBatchDelete",//批量删除接口
    "exportExcelInterface": "../",//导出excel接口
});
