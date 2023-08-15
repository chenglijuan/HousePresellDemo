(function(baseInfo){
	var listVue = new Vue({
		el : baseInfo.listDivId,
		data : {
			interfaceVersion :19000101,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			selectItem : [],
			theState: 0,//正常为0，删除为1
			tg_DepositManagementList:[],

            keyword :null,
            emmp_BankBranchList : [],
            bankBranchId : "",
            depositPropertyList : [
                {tableId:"01",theName:"大额存单"},
                {tableId:"02",theName:"结构性存款"},
                {tableId:"03",theName:"保本理财"}
            ],
            depositProperty : "",
            listDateStr: null,


            remindText:"提示内容"
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			listItemSelectHandle: listItemSelectHandle,
			search:search,
			searchWithoutKey: searchWithoutKey,
			// depositManagementEditHandle:depositManagementEditHandle,
			// depositManagementDeleteHandle:depositManagementDeleteHandle,
            depositManagementDetailHandle : depositManagementDetailHandle,
            depositManagementExportExcelHandle : depositManagementExportExcelHandle,

            bankBranchChange: function (data) {
                if(listVue.bankBranchId != data.tableId)
                {
                    listVue.bankBranchId = data.tableId;
                }
            },
            bankBranchEmpty: function () {
                if(listVue.bankBranchId != null)
                {
                    listVue.bankBranchId = null;
                }
            },
            depositPropertyChange: function (data) {
                if(listVue.depositProperty != data.tableId)
                {
                    listVue.depositProperty = data.tableId;
                }
            },
            depositPropertyEmpty: function () {
                if(listVue.depositProperty != null)
                {
                    listVue.depositProperty = null;
                }
            },
			changePageNumber: function (data) {
				console.log(data);
				if (listVue.pageNumber != data) {
					listVue.pageNumber = data;
					listVue.refresh();
				}
			},
			changeCountPerPage : function (data) {
				console.log(data);
				if (listVue.countPerPage != data) {
					listVue.countPerPage = data;
					listVue.refresh();
				}
			},
			indexMethod: function (index) {
				if (listVue.pageNumber > 1) {
					return (listVue.pageNumber - 1) * listVue.countPerPage + (index - 0 + 1);
				}
				if (listVue.pageNumber <= 1) {
					return index - 0 + 1;
				}
			}
		},
        filters : {
            setThousands:function (data) {
                return addThousands(data);
            },
            setPercent:function (data) {
                return toPercent(data);
            }
        },
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue,
            'vue-listselect': VueListSelect
		},
		watch:{
		}
	});
	
	//------------------------方法定义-开始------------------//
	 
	function listItemSelectHandle(val) {
		listVue.selectItem = [];
		for (var index = 0; index < val.length; index++) {
			var element = val[index].tableId;
			listVue.selectItem.push(element)
		}
		console.log(listVue.selectItem);
	}
	
	
	//列表操作--------------获取"搜索列表"表单参数
    function getSearchForm()
    {
        return {
            interfaceVersion:this.interfaceVersion,
            pageNumber:this.pageNumber,
            countPerPage:this.countPerPage,
            totalPage:this.totalPage,
            depositState:"02",
            keyword : this.keyword,
            depositProperty:this.depositProperty,
            bankOfDepositId : this.bankBranchId,
            stopDateStr : this.listDateStr,
            theState:this.theState
        }
    }

	// function depositManagementEditHandle() {
	// 	console.log("EditHandle");
    //     if(listVue.selectItem.length == 1)
    //     {
    //         var tableId = listVue.selectItem[0];
    //         var theId = 'DepositManagementTakeOutEdit_' + tableId;
    //         $("#tabContainer").data("tabs").addTab({id: theId , text: '存单提取修改', closeable: true, url: 'tg/Tg_DepositManagementTakeOutEdit.shtml'});
    //     }
    //     else
    //     {
    //     	listVue.remindText = "请选择一个且只选一个要修改的存单提取";
    //         $(baseInfo.remindModal).modal('show', {
    //             backdrop :'static'
    //         });
    //     }
	// }


	// function depositManagementDeleteHandle() {
	// 	if(listVue.selectItem.length == 0)
	// 	{
	// 		$(baseInfo.remindModal).modal('show', {
	// 			backdrop :'static'
	// 		});
	// 	}
	// 	else if (listVue.selectItem.length > 1)
	// 	{
    //         tg_DepositManagementDel();
	// 	}
	// 	else
	// 	{
	// 		var theId = listVue.selectItem[0];
	// 		console.log(theId);
    //         tg_DepositManagementDelOne(theId);
	// 	}
	// }
	//
	//
	// function tg_DepositManagementDel()
	// {
	// 	noty({
	// 		layout:'center',
	// 		modal:true,
	// 		text:"确认批量删除吗？",
	// 		type:"confirm",
	// 		buttons:[
	// 			{
	// 				addClass:"btn btn-primary",
	// 				text:"确定",
	// 				onClick:function($noty){
	// 					$noty.close();
	// 					new ServerInterface(baseInfo.deleteInterface).execute(listVue.getDeleteForm(), function(jsonObj){
	// 						if(jsonObj.result != "success")
	// 						{
	// 							noty({"text":jsonObj.info,"type":"error","timeout":2000});
	// 						}
	// 						else
	// 						{
	// 							listVue.selectItem = [];
	// 							refresh();
	// 						}
	// 					});
	// 				}
	// 			},
	// 			{
	// 				addClass:"btn btn-danger",
	// 				text:"取消",
	// 				onClick:function($noty){
	// 					$noty.close();
	// 				}
	// 			}
	// 		]
	// 	});
	// }
	// function tg_DepositManagementDelOne(tg_DepositManagementId)
	// {
	// 	var model = {
	// 		interfaceVersion :19000101,
	// 		idArr:[tg_DepositManagementId]
	// 	};
	//
	// 	noty({
	// 		layout:'center',
	// 		modal:true,
	// 		text:"确认删除吗？",
	// 		type:"confirm",
	// 		buttons:[
	// 			{
	// 				addClass:"btn btn-primary",
	// 				text:"确定",
	// 				onClick:function($noty){
	// 					$noty.close();
	// 					new ServerInterface(baseInfo.deleteInterface).execute(model , function(jsonObj){
	// 						if(jsonObj.result != "success")
	// 						{
	// 							noty({"text":jsonObj.info,"type":"error","timeout":2000});
	// 						}
	// 						else
	// 						{
	// 							refresh();
	// 						}
	// 					});
	// 				}
	// 			},
	// 			{
	// 				addClass:"btn btn-danger",
	// 				text:"取消",
	// 				onClick:function($noty){
	// 					$noty.close();
	// 				}
	// 			}
	// 		]
	// 	});
	// }

    function getBankBranchList()
    {
        var model = {
            interfaceVersion : listVue.interfaceVersion,
            theState : 0
        };

        new ServerInterface(baseInfo.bankBranchListInterface).execute(model, function(jsonObj)
        {
            console.log(jsonObj);
            if (jsonObj.result != "success")
            {

            }
            else
            {
                listVue.emmp_BankBranchList = jsonObj.emmp_BankBranchList;
            }
        });
    }

    function depositManagementExportExcelHandle()
    {
        var model = {
            interfaceVersion:this.interfaceVersion,
            depositState:"02",
            keyword:this.keyword,
            theState:this.theState,
            idArr:this.selectItem
        };


        new ServerInterface(baseInfo.exportExcelInterface).execute(model, function (jsonObj) {
            if (jsonObj.result != "success")
            {
                listVue.errorMessage = jsonObj.info;
                $(baseInfo.errorM).modal('show', {
                    backdrop :'static'
                });
            }
            else
            {
                window.location.href=jsonObj.fileDownloadPath;
                console.log(jsonObj.fileDownloadPath);

                listVue.selectItem = [];
                refresh();
            }
        });
    }
	
	//列表操作--------------刷新
	function refresh()
	{
		new ServerInterface(baseInfo.depositManagementListInterface).execute(listVue.getSearchForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				console.log(jsonObj);					
				
				listVue.tg_DepositManagementList=jsonObj.tg_DepositManagementList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
                //动态跳转到锚点处，id="top"
				document.getElementById('tg_DepositManagementTakeOutListDiv').scrollIntoView();
			}
		});
	}
	
	//列表操作------------搜索
    function search() {
        listVue.pageNumber = 1;
        refresh();
    }

    function searchWithoutKey () {

        listVue.bankBranchId = "";
        listVue.listDateStr = null;
        listVue.depositProperty = "";
        listVue.keyword = null;
        $('#date21010501').val("");

        listVue.pageNumber = 1;
        refresh();
    }
	
	//跳转方法
	function depositManagementDetailHandle(tableId) {
		var theId = 'DepositManagementTakeOutDetail_' + tableId;
		$("#tabContainer").data("tabs").addTab({ id: theId, text: '存单提取详情', closeable: true, url: 'tg/Tg_DepositManagementTakeOutDetail.shtml' });
	}
	
	function initData()
	{
		laydate.render({
			  elem: '#date21010501',
            done: function (value, date) {
                if (value == null || value.length == 0)
                {
                    listVue.listDateStr = null;
                }
                else
                {
                    listVue.listDateStr = value;
                }
                refresh();
            }
        });

        refresh();
        getBankBranchList();
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.initData();
	
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tg_DepositManagementTakeOutListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"depositManagementListInterface":"../Tg_DepositManagementList",

    "deleteInterface": "../Tg_DepositManagementBatchDelete",
    "exportExcelInterface": "../Tg_DepositManagementExportExcel",
    "bankBranchListInterface":"../Emmp_BankBranchList",

	//删除
	"remindModal":"#remindModal",
});
