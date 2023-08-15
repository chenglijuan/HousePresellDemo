(function(baseInfo){
	var listVue = new Vue({
		el : baseInfo.listDivId,
		data : {
			interfaceVersion :19000101,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			keyword : null,
			recordState: 1,
			selectItem : [],
			theState:0,//正常为0，删除为1
			tg_DepositManagementList:[],

            remindText:"提示内容",

            depositPropertyList : [
                {tableId:"01",theName:"大额存单"},
                {tableId:"02",theName:"结构性存款"},
                {tableId:"03",theName:"保本理财"}
            ],
            depositProperty : "",

            startDate : null,
            stopDate : null

		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			listItemSelectHandle: listItemSelectHandle,
			search:search,
			searchWithoutKey: searchWithoutKey,
			depositManagementAddHandle:depositManagementAddHandle,
			depositManagementEditHandle:depositManagementEditHandle,
			depositManagementDeleteHandle:depositManagementDeleteHandle,
            depositManagementExportExcelHandle : depositManagementExportExcelHandle,
            depositManagementDetailHandle : depositManagementDetailHandle,

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
            depositState:"03",
            depositProperty : this.depositProperty,
            startDateStr : this.startDate,
            stopDateStr : this.stopDate,
			keyword:this.keyword,
			theState:this.theState
		}
	}
	
	function depositManagementEditHandle() {
		console.log("EditHandle");
        if(listVue.selectItem.length == 1)
        {
            enterDetail(listVue.selectItem, '存单正在办理修改', 'tg/Tg_DepositManagementInProgressEdit.shtml');
        }
        else
        {
        	listVue.remindText = "请选择一个且只选一个要修改的存单存入";
            $(baseInfo.remindModal).modal('show', {
                backdrop :'static'
            });
        }
	}

    function depositManagementDeleteHandle() {
        generalDeleteModal(listVue,"Tg_DepositManagement")
    }

    function depositManagementExportExcelHandle()
    {
        var model;
        if (this.selectedItem.length > 0)
        {
            model = {
                interfaceVersion:this.interfaceVersion,
                theState:this.theState,
                idArr:this.selectItem
            };
        }
        else
        {
            model = {
                interfaceVersion:this.interfaceVersion,
                depositState:"03",
                depositProperty : this.depositProperty,
                startDateStr : this.startDate,
                stopDateStr : this.stopDate,
                keyword:this.keyword,
                theState:this.theState
            };
        }

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
		console.log(listVue.getSearchForm());

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
				document.getElementById('tg_DepositManagementInProgressListDiv').scrollIntoView();
			}
		});
	}
	
	//列表操作------------搜索
	function search() {
		listVue.pageNumber = 1;
		refresh();
	}

	function searchWithoutKey () { 
		listVue.keyword = null;
        $('#date21010601').val("");
        listVue.startDate= null;
        listVue.stopDate = null;
        listVue.depositProperty = null;
		listVue.pageNumber = 1;
		refresh();
	}
	
	//跳转方法
	function depositManagementAddHandle() {
        enterNewTab('', '添加存单正在办理', 'tg/Tg_DepositManagementInProgressAdd.shtml');
	}
		
	function depositManagementDetailHandle(tableId) {
        enterNewTab(tableId, '存单正在办理详情', 'tg/Tg_DepositManagementInProgressDetail.shtml');
	}
	
	function initData()
	{
		laydate.render({
			elem: '#date21010601',
			range: true,
            done: function (value, date) {
                if (value == null || value.length == 0)
                {
                    listVue.startDate= null;
                    listVue.stopDate = null;
                }
                else
                {
                    var selectDateArr = value.split(" - ");
                    if (selectDateArr.length == 2)
                    {
                        listVue.startDate= selectDateArr[0];
                        listVue.stopDate = selectDateArr[1];
                    }
                    else
                    {
                        listVue.startDate= null;
                        listVue.stopDate = null;
                    }
                }
                refresh();
            }
		});

		refresh();
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.initData();
	
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tg_DepositManagementInProgressListDiv",
	"updateDivId":"#updateModel",
	"addDivId":"#addModal",
	"depositManagementListInterface":"../Tg_DepositManagementList",
    "deleteInterface": "../Tg_DepositManagementBatchDelete",
    "exportExcelInterface": "../Tg_DepositManagementExportExcel",
	//删除
	"remindModal":"#remindModal",
});
