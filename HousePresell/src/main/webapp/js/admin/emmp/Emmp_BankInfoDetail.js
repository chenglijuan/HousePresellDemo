(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			emmp_BankInfoModel: {},
			tableId : 1,

            //附件材料
			busiType:'020201',
            dialogVisible: false,
            dialogImageUrl: "",
            fileType:"",
            fileList : [],
            showButton:false,
            hideShow:true,
            uploadData : [],
            smAttachmentList:[],//页面显示已上传的文件
            uploadList:[],
            loadUploadList: [],
            showDelete : false,  //附件是否可编辑
            //其他
            errMsg:"", //错误提示信息

            //机构成员相关
            pageNumber: 1,
            countPerPage: 10,
            totalPage: 1,
            totalCount: 1,
            theState: 0,//正常为0，删除为1
            selectItem: [],
            orgMemberList: [],

            //附件材料
            loadUploadList: [],
            showDelete : false,  //附件是否可编辑
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
            test:function () {
				console.log(110);
            },
            dateFormat:dateFormat,
			getSearchForm : getSearchForm,
            bankInfoEditHandle:bankInfoEditHandle,

            listItemSelectHandle:listItemSelectHandle,
            indexMethod: indexMethod,
            changePageNumber : function(data){
                detailVue.pageNumber = data;
            },
            changeCountPerPage :function (data) {
                if (detailVue.countPerPage != data) {
                    detailVue.countPerPage = data;
                    detailVue.refresh();
                }
            },

            //机构成员列表
            refreshOrgMemberList: refreshOrgMemberList,


		},
		computed:{

		},
		components : {
            "my-uploadcomponent":fileUpload
		},
		watch:{

		}
	});

	//------------------------方法定义-开始------------------//
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			tableId:detailVue.tableId,
		}
	}

	//详情操作--------------
	function refresh()
	{
		console.log('进入 refresh tableId is '+detailVue.tableId)
		if (detailVue.tableId == null || detailVue.tableId < 1)
		{
			console.log('this.tableId == null || this.tableId < 1')
			return;
		}

		getDetail();
		loadUpload()
	}

	function getDetail()
	{
		serverRequest(baseInfo.detailInterface,detailVue.getSearchForm(),function (jsonObj) {
            detailVue.emmp_BankInfoModel = jsonObj.emmp_BankInfo;
        })
	}

	function loadUpload() {
        generalLoadFile2(detailVue,detailVue.busiType)
    }
	
	function initData()
	{
		getIdFormTab("",function (tableId) {
			detailVue.tableId=tableId
			refreshOrgMemberList()
            refresh()
        })
	}

	function bankInfoEditHandle() {
		enterNewTabCloseCurrent(detailVue.tableId,"金融机构详情修改","emmp/Emmp_BankInfoEdit.shtml")
    }

    function dateFormat(date) {
        return moment(date).format("YYYY-MM-DD");
    }

    function listItemSelectHandle(list) {
        generalListItemSelectHandle(detailVue,list)
    }

    function indexMethod(index) {
        return generalIndexMethod(index, detailVue)
    }

    function refreshOrgMemberList ()
    {
        var model = {
            interfaceVersion: detailVue.interfaceVersion,
            pageNumber: detailVue.pageNumber,
            countPerPage: detailVue.countPerPage,
            totalPage: detailVue.totalPage,
            theState: detailVue.theState,
			bankId:detailVue.tableId,
        };
        serverRequest(baseInfo.orgMemberListInterface,model,function (jsonObj) {
            detailVue.orgMemberList = jsonObj.emmp_OrgMemberList;
            detailVue.pageNumber = jsonObj.pageNumber;
            detailVue.countPerPage = jsonObj.countPerPage;
            detailVue.totalPage = jsonObj.totalPage;
            detailVue.totalCount = jsonObj.totalCount;
            detailVue.selectItem = [];
        })
    }

    //------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	// detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#BankInfoDetailId",
	"detailInterface":"../Emmp_BankInfoDetail",
	//附件读取
    "loadUploadInterface":"../Sm_AttachmentCfgList",
    //模态框
    "errorModal": "#errorM",
    //机构成员
    "orgMemberListInterface":"../Emmp_OrgMemberList",
});
