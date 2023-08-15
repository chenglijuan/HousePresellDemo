(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tgxy_BankAccountEscrowedModel: {},
			tableId : 1,
			dayDate:"",
			userStartName:"",
			bankBranch:[],
			isUse:"",
			pageNumber: 1,
            countPerPage: 10,
            totalPage: 1,
            totalCount: 1,
            //附件材料
			busiType:'200101',
			busiTypes:'200104',
            dialogVisible: false,
            dialogImageUrl: "",
            fileType:"",
            fileList : [],
            showButton:false,
            hideShow:true,
            uploadData : [],
            smAttachmentList:[],//页面显示已上传的文件
            uploadList:[],
            showDelete : false,  //附件是否可编辑
            //其他
            errMsg:"", //错误提示信息

            //附件材料
            loadUploadList: [],
            loadUploadLists: [],
            loadUploadListss : [],
            showDelete : false,  //附件是否可编辑
            showDeletes : true,  //附件是否可编辑
            theAccount:'',
            theName:'',
            buildingList:[],
            generalAttachmentList:[],
            keyword:'',
            isDisabled:false,
         
            
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getTableList: getTableList,
			getSearchForm : getSearchForm,
			getTableForm : getTableForm,
			mainDetailHandle : mainDetailHandle,
            mainEditHandle:mainEditHandle,
            cancellation:cancellation,//销户
            theNameConfirm:theNameConfirm,//销户确定
            getCancelForm:getCancelForm,
            search:search,
            resetSearch : resetSearch,//重置
            theAccountShow:theAccountShow,//托管账户列表
            changeCountPerPage: function (data) {
                if (detailVue.countPerPage != data) {
                	detailVue.countPerPage = data;
                	detailVue.getTableList();
                }
            },
            changePageNumber: function (data) {
            	detailVue.pageNumber = data;
            	detailVue.getTableList();
            },
		},
		computed:{
			 
		},
		components : {
            "my-uploadcomponent":fileUpload,
            'vue-nav': PageNavigationVue,
		},
		watch:{
			
		}
	});

	//------------------------方法定义-开始------------------//
	function mainDetailHandle(row) {
		$("#theAccountModel").modal('hide');
		detailVue.theAccount = row.theAccount
		detailVue.theName = row.theName
		detailVue.toTableId = row.toTableId
		detailVue.toECode = row.toECode
    }
	
	function getTableList()
	{
		serverRequest(baseInfo.tableInterface,detailVue.getTableForm(),function (jsonObj) {
            //子表数据
            detailVue.buildingList = jsonObj.tgxy_BankAccountEscrowedList;
            detailVue.pageNumber = jsonObj.pageNumber;
            detailVue.countPerPage = jsonObj.countPerPage;
            detailVue.totalPage = jsonObj.totalPage;
            detailVue.totalCount = jsonObj.totalCount;
            
        })
	}
	function search()
	{
		detailVue.pageNumber = 1;
		getTableList();
	}
	function resetSearch(){
		detailVue.keyword = "";
	}
	function theAccountShow()
	{
		$("#theAccountModel").modal({
    		backdrop :'static',
    		keyboard: false
    	});
		getTableList();
	}
	function cancellation()
	{
		generalLoadFileBld(detailVue)
		$("#cancelModel").modal({
			backdrop :'static',
			keyboard: false
		});
	}
	function theNameConfirm()
	{
		dialogSave();
		new ServerInterfaceJsonBody(baseInfo.cancelInterface).execute(detailVue.getCancelForm(), function(jsonObj) {
			 if(jsonObj.result != "success")
	            {
	                generalErrorModal(jsonObj);
	            }
	            else
	            {
	                generalSuccessModal();
	                $("#cancelModel").modal('hide');
	            }
			 
			 
			 getDetail();
			 
        })
	}
	
	function getCancelForm()
	{
		return {
			interfaceVersion:detailVue.interfaceVersion,
			tableId:detailVue.tableId,
			//
			theAccount :detailVue.theAccount,
			theName : detailVue.theName,
			toTableId :detailVue.toTableId,
			toECode : detailVue.toECode,
			generalAttachmentList:detailVue.loadUploadListss,
		}
	}
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:detailVue.interfaceVersion,
			tableId:detailVue.tableId,
			
		}
	}
	function getTableForm()
	{
		return {
			interfaceVersion:detailVue.interfaceVersion,
			tableId:detailVue.tableId,
			pageNumber: detailVue.pageNumber,
            countPerPage: detailVue.countPerPage,
            totalPage: detailVue.totalPage,
            keyword: detailVue.keyword,
		}
	}
	  /**
     * 初始化附件信息并加载当前界面的附件信息
     * @param vue Vue类型，当前页面的Vue
     * @param busiType String类型，业务编号
     */
	function dialogSave() {
//		console.log(detailVue.$refs.listenUploadDatas)
		detailVue.loadUploadListss = detailVue.$refs.listenUploadDatas.uploadData;
	}
    function generalLoadFileBld(detailVue) {
        var form = {
            pageNumber:"0",
            busiType : detailVue.busiTypes,
            interfaceVersion:detailVue.interfaceVersion,
            reqSource : detailVue.flag == true ? "详情" : "",//判断是否是详情页是则显示旧的附件材料
            sourceId:detailVue.tableId,
            approvalCode:detailVue.approvalCode,
            afId:detailVue.afId,
        };
        detailVue.loadUploadLists=[]
        serverRequest("../Sm_AttachmentCfgList",form,function (jsonObj) {
        	detailVue.loadUploadLists = jsonObj.sm_AttachmentCfgList;
        	
        },function (jsonObj) {
            generalErrorModal(jsonObj)
        })
    }
	//详情操作--------------
	function refresh()
	{
		if (detailVue.tableId == null || detailVue.tableId < 1)
		{
			return;
		}
		getDetail();
	}

    function getDetail()
	{
		serverRequest(baseInfo.detailInterface,detailVue.getSearchForm(),function (jsonObj) {
            detailVue.tgxy_BankAccountEscrowedModel = jsonObj.tgxy_BankAccountEscrowed

			detailVue.userStartName=detailVue.tgxy_BankAccountEscrowedModel.userStartName
			console.log('timestamp is '+detailVue.tgxy_BankAccountEscrowedModel.createTimeStamp)
			detailVue.dayDate=detailVue.tgxy_BankAccountEscrowedModel.createTimeStampString
			detailVue.isUse=isUseNumber2String(detailVue.tgxy_BankAccountEscrowedModel.busiState)
			
			detailVue.tgxy_BankAccountEscrowedModel.transferInAmount =  addThousands(detailVue.tgxy_BankAccountEscrowedModel.transferInAmount);
            detailVue.tgxy_BankAccountEscrowedModel.transferOutAmount =  addThousands(detailVue.tgxy_BankAccountEscrowedModel.transferOutAmount);
            
			if(detailVue.tgxy_BankAccountEscrowedModel.hasClosing == '1'){
				detailVue.isDisabled = true;
				loadUploads();
			}else{
				loadUpload();
			}
            
        })
	}

	function mainEditHandle() {
		enterNewTabCloseCurrent(detailVue.tableId,"托管账号修改","tgxy/Tgxy_BankAccountEscrowedEdit.shtml")
    }

    function loadUpload() {
        generalLoadFile2(detailVue,detailVue.busiType)
    }
    function loadUploads() {
    	generalLoadFile2(detailVue,detailVue.busiTypes)
    }


    function initData()
	{
		getIdFormTab("",function (id) {
            detailVue.tableId=id
            refresh()
        })
        
        
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tgxy_BankAccountEscrowedDetailDiv",
	"detailInterface":"../Tgxy_BankAccountEscrowedDetail",
	"cancelInterface":"../Tgxy_BankAccountEscrowedClosing",
	"tableInterface":"../loadingBankAccount",

    //附件读取
    "loadUploadInterface":"../Sm_AttachmentCfgList",
    //模态框
    "errorModal": "#errorM",
});
