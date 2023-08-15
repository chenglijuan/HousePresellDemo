(function (baseInfo) {
    var detailVue = new Vue({
        el: baseInfo.detailDivId,
        data: {
            interfaceVersion: 19000101,
            sm_BusiState_LogModel: {},
            tableId: 1,
            compareMember: {},
            orgMemberListOld: [],
            orgMemberListNew: [],
            isNeedShowOldOrgMemberList: false,
            differList: [],
            differListTemp: [],
            thePoName: "",
            packagePath: "",
            selectId: "",
            hasOrgMemberList: false,
            hasDevProcessList: false,
            hasRemark: false,
            hasIntro: false,

            remarkOld: "",
            remarkNew: "",

            introOld: "",
            introNew: "",

            forcastDtlListOld: [],
            forcastDtlListNew: [],

            //范围授权开始
            hasRangePermission: false,
            rangeNew: {},
            rangeOld: {},
            //zTree
            zNodesOld: [],
            settingOld: {
                data: {
                    simpleData: {
                        enable: true
                    },
                },
                view: {
                    dblClickExpand: false,
                    showIcon: false,
                    showLine: false
                },
                check: {
                    enable: true,//checkBox close
                },
                callback: {}
            },
            zNodesNew: [],
            settingNew: {
                data: {
                    simpleData: {
                        enable: true
                    },
                },
                view: {
                    dblClickExpand: false,
                    showIcon: false,
                    showLine: false
                },
                check: {
                    enable: true,//checkBox close
                },
                callback: {}
            },
            selectItemOld: [],
            selectItemNew: [],
            //范围授权结束
        },
        methods: {
            //详情
            refresh: refresh,
            initData: initData,
            getSearchForm: getSearchForm,
        },
        computed: {},
        components: {},
        watch: {}
    });

    //------------------------方法定义-开始------------------//
    //详情操作--------------获取"机构详情"参数
    function getSearchForm() {
        return {
            interfaceVersion: this.interfaceVersion,
            tableId: this.tableId,
        }
    }

    //详情操作--------------
    function refresh() {
        // console.log('getLogDetail data is')
        // console.log(data)
        // detailVue.selectLogDetail = data
        // detailVue.changeUserName=data.updateUserName
        // detailVue.changeTimeString=data.updateTimeString
        // console.log('changeUserName is '+detailVue.changeUserName)
        // console.log('changeTimeString is '+detailVue.changeTimeString)
        var form = {
            interfaceVersion: detailVue.interfaceVersion,
            theState: 0,
            sourceType: detailVue.thePoName,
            tableId: detailVue.selectId,
            packagePath: detailVue.packagePath
        }
        serverRequest(baseInfo.detailInterface, form, function (jsonObj) {
            detailVue.differListTemp = jsonObj.differ
            for (var i = 0; i < detailVue.differListTemp.length; i++) {
                var item = detailVue.differListTemp[i]
                console.log('item is ')
                console.log(item)
                console.log('item.fieldRemarkName is ' + item.fieldRemarkName)
                if (item.fieldRemarkName == "备注") {
                    console.log('item.fieldRemarkName == "remark"')
                    detailVue.hasRemark = true
                    detailVue.remarkOld = item.objAValue
                    detailVue.remarkNew = item.objBValue
                } else if (item.fieldRemarkName == "简介") {
                    detailVue.hasIntro = true
                    detailVue.introOld = item.objAValue
                    detailVue.introNew = item.objBValue
                } else if (item.fieldRemarkName == "机构列表") {
                    var objAUnique = item.objAUnique
                    var objBUnique = item.objBUnique
                    if (objAUnique.length == 0 && objBUnique.length == 0) {
                        detailVue.isNeedShowOldOrgMemberList = false
                    } else {
                        detailVue.isNeedShowOldOrgMemberList = true
                    }
                } else {
                    console.log('item.fieldRemarkName != "remark"')
                    detailVue.differList.push(item)
                }
            }

            detailVue.orgMemberListOld = jsonObj.orgMemberListOld
            detailVue.orgMemberListNew = jsonObj.orgMemberListNew

            detailVue.forcastDtlListOld = jsonObj.forcastDtlListOld
            detailVue.forcastDtlListNew = jsonObj.forcastDtlListNew


            detailVue.rangeNew = jsonObj.rangeNew
            detailVue.rangeOld = jsonObj.rangeOld

            if (detailVue.orgMemberListOld != undefined) {
                detailVue.hasOrgMemberList = true
            } else {
                detailVue.hasOrgMemberList = false
            }
            if (detailVue.forcastDtlListOld != undefined) {
                detailVue.hasDevProcessList = true
            } else {
                detailVue.hasDevProcessList = false
            }
            if (detailVue.rangeNew != undefined) {
                detailVue.hasRangePermission = true
                detailVue.zNodesOld = detailVue.rangeOld.rangeInfoAllList
                detailVue.zNodesNew = detailVue.rangeNew.rangeInfoAllList
                initZtreeList()
            } else {
                detailVue.hasRangePermission = false
            }

            // console.log('differList is ')
            // console.log(detailVue.differList)
            // var needRemoveIndex=0
            // for(var i=0;i<detailVue.differList.length;i++){
            // 	var item=detailVue.differList[i]
            //     // console.log('item is ')
            //     // console.log(item)
            //     // console.log('item is '+JSON.stringify(item))
            //     if(item.fieldName=="orgMemberList"){
            // 	    // console.log('item.fieldName=="orgMemberList"')
            // 	    detailVue.compareMember=jsonDeepCopy(item)
            //         needRemoveIndex=i
            //         detailVue.hasOrgMemberList=true
            //     }
            // }
            // if(detailVue.hasOrgMemberList){
            //     setOrgMemberList(needRemoveIndex)
            // }

        })
    }

    function initDetailTree(zTree, jsonObj) {
        // console.log('zTree is')
        // console.log(zTree)
        // console.log('initDetailTree json obj is ')
        // console.log(jsonObj)
        var selectItem = []
        try {
            // detailVue[selectItem] = jsonObj.rangeInfoSelectDetail.idSelectList;
            selectItem = jsonObj.rangeInfoSelectDetail.idSelectList;
            // detailVue[selectItem] = detailVue.rangeNew.rangeInfoSelectDetail.idSelectList;
        } catch (e) {
            // detailVue[selectItem] = [];
            selectItem = [];
        }
        // console.log('detailVue[selectItem] is ')
        // console.info(detailVue[selectItem]);
        try {
            Vue.nextTick(function () {
                var rangeAuthType = jsonObj.rangeInfoSelectDetail.rangeAuthType;
                //console.info(rangeAuthType);
                if (rangeAuthType != null && rangeAuthType == 1) { //区域
                    // for (var i = 0; i < detailVue[selectItem].length; i++) {
                    for (var i = 0; i < selectItem.length; i++) {
                        var node = zTree.getNodeByParam("areaId", selectItem[i]);
                        // node = zTree.getNodeByParam("areaId", detailVue[selectItem][i]);
                        //(node.getChildren() == null || node.getChildren().length == 0)
                        if (node != null) {
                            zTree.checkNode(node, true, true);
                            zTree.updateNode(node);
                        }
                    }
                } else if (rangeAuthType != null && rangeAuthType == 2) {//项目
                    // console.log('detailVue[selectItem] is')
                    console.log(selectItem)
                    // for (var i = 0; i < detailVue[selectItem].length; i++) {
                    for (var i = 0; i < selectItem.length; i++) {
                        // var node = zTree.getNodeByParam("projectId", detailVue[selectItem][i]);
                        var node = zTree.getNodeByParam("projectId", selectItem[i]);
                        console.log('zTree in project is')
                        console.log(zTree)
                        console.log('node in project is ')
                        console.log(node)
                        // node = zTree.getNodeByParam("projectId", detailVue[selectItem][i]);
                        //(node.getChildren() == null || node.getChildren().length == 0)
                        if (node != null) {
                            zTree.checkNode(node, true, true);
                            zTree.updateNode(node);
                        }
                    }
                } else if (rangeAuthType != null && rangeAuthType == 3) { //楼幢
                    for (var i = 0; i < selectItem.length; i++) {
                        var node = zTree.getNodeByParam("buildId", selectItem[i]);
                        // node = zTree.getNodeByParam("buildId", detailVue[selectItem][i]);
                        //(node.getChildren() == null || node.getChildren().length == 0)
                        if (node != null) {
                            zTree.checkNode(node, true, true);
                            zTree.updateNode(node);
                        }
                    }
                }
                //禁用 zTree 所有节点 CheckBox 的勾选选操作。
                var nodes_UnEdit = zTree.getNodes();
                //console.info(nodes_UnEdit);
                for (var i = 0; i < nodes_UnEdit.length; i++) {
                    zTree.setChkDisabled(nodes_UnEdit[i], true, true, true);
                }
            });
        } catch (e) {
            selectItem = [];
        }
    }

    //范围授权 开始
    function initZtreeList() {
        $(document).ready(function () {
            $.fn.zTree.init($("#treeDemo_RangeAuthNew"), detailVue.settingNew, detailVue.zNodesNew);
            zTreeNew = $.fn.zTree.getZTreeObj("treeDemo_RangeAuthNew");
            //console.info(zTree.getNodes());

            $.fn.zTree.init($("#treeDemo_RangeAuthOld"), detailVue.settingOld, detailVue.zNodesOld);
            zTreeOld = $.fn.zTree.getZTreeObj("treeDemo_RangeAuthOld");
        });

        // console.log("zTree old is ")
        // console.log(zTreeOld)
        // console.log("zTree new is ")
        // console.log(zTreeNew)
        initDetailTree(zTreeOld, detailVue.rangeOld, detailVue.selectItemOld)
        initDetailTree(zTreeNew, detailVue.rangeNew, detailVue.selectItemNew)
    }

    //范围授权结束

    function initData() {
        var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
        console.log('tableIdStr is ' + tableIdStr)
        var splitMinus = tableIdStr.split("-")
        var splitSplash = splitMinus[0].split("_")
        console.log('splitMinus[0] is ' + splitMinus[0])
        detailVue.selectId = splitSplash[splitSplash.length - 1]
        console.log('detailVue.selectId  is ' + detailVue.selectId)

        if (splitMinus[1] != undefined) {
            detailVue.packagePath = splitMinus[1].replace(/\_/g, ".")
        }
        refresh()
        // getIdFormTab("",function (tableId) {
        // console.log('tableId is '+tableId)
        // var split=tableId.split("-")
        // detailVue.selectId=split[0]
        // if(split[1]!=undefined){
        //         detailVue.packagePath=split[1].replace(/\_/g,".")
        // }
        // refresh()
        // })
        // getPoNameFromTab(function (thePoName) {
        //     detailVue.thePoName = thePoName
        //     console.log('poName is ' + thePoName)
        //     if(thePoName.indexOf("-")!=-1){
        //         var poArray=thePoName.split("-")
        //         detailVue.thePoName=poArray[0]
        //         detailVue.packagePath=poArray[1].replace(/\_/g,".")
        //         // console.log('poArray[0] is '+poArray[0])
        //         // console.log('poArray[1] is '+detailVue.packagePath)
        //     }
        //     refresh()
        // })
    }

    //------------------------方法定义-结束------------------//

    //------------------------数据初始化-开始----------------//
    // detailVue.refresh();
    detailVue.initData();
    var zTreeOld;
    var zTreeNew;
    //------------------------数据初始化-结束----------------//
})({
    "detailDivId": "#sm_BusiState_LogDiv",
    "detailInterface": "../Sm_BusiState_LogDetail",
    "listInterface": "../Sm_BusiState_LogList",
});
