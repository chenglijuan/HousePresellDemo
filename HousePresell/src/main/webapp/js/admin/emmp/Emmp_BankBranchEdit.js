(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			emmp_BankBranchModel: {generalBankName:""},
			tableId : 1,
            generalBankList:[],

            //附件材料
            busiType : '020202',
            dialogVisible: false,
            dialogImageUrl: "",
            fileType:"",
            fileList : [],
            showButton:true,
            hideShow:false,
            uploadData : [],
            smAttachmentList:[],//页面显示已上传的文件
            uploadList:[],
			bankInfoId:"",

            //附件材料
            loadUploadList: [],
            showDelete : true,

            //其他
            errMsg:"", //错误提示信息
            
          //对接资金系统
			isDockingList : [
            	{tableId:"1",theName:"是"},
            	{tableId:"0",theName:"否"}
            ],
            
            getIsDockingId : function(data){
            	detailVue.emmp_BankBranchModel.isDocking = data.tableId;
			},
			emptyIsDockingId : function(){
				detailVue.emmp_BankBranchModel.isDocking = null;
			}
			
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			//更新
			getUpdateForm : getUpdateForm,
			update: update,
            dateFormat:dateFormat,

            saveAttachment:saveAttachment,
            changeBankListener:changeBankListener,
		},
		computed:{
			 
		},
		components : {
            'vue-select': VueSelect,
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
			interfaceVersion:detailVue.interfaceVersion,
			tableId:detailVue.tableId,
		}
	}

	//详情操作--------------
	function refresh()
	{
		if (detailVue.tableId == null || detailVue.tableId < 1) 
		{
			return;
		}
        getGeneralBankList()
		getDetail();
		loadUpload()
	}

	function getDetail()
	{
	    console.log('进入bankBranchEdit getDetail')
		new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
			    console.log(jsonObj);
				detailVue.emmp_BankBranchModel = jsonObj.emmp_BankBranch;
				detailVue.bankInfoId=jsonObj.emmp_BankBranch.bankId
			}
		});
	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getUpdateForm()
	{
		return {
			interfaceVersion:detailVue.interfaceVersion,
			tableId:detailVue.tableId,
			theState:detailVue.emmp_BankBranchModel.theState,
			busiState:detailVue.emmp_BankBranchModel.busiState,
			eCode:detailVue.emmp_BankBranchModel.eCode,
			userStartId:detailVue.emmp_BankBranchModel.userStartId,
			userRecordId:detailVue.emmp_BankBranchModel.userRecordId,
			// bankId:detailVue.emmp_BankBranchModel.bankId,
			bankId:detailVue.bankInfoId,
			theName:detailVue.emmp_BankBranchModel.theName,
			shortName:detailVue.emmp_BankBranchModel.shortName,
			address:detailVue.emmp_BankBranchModel.address,
			contactPerson:detailVue.emmp_BankBranchModel.contactPerson,
			contactPhone:detailVue.emmp_BankBranchModel.contactPhone,
			leader:detailVue.emmp_BankBranchModel.leader,
			isUsing:detailVue.emmp_BankBranchModel.isUsing,
			subjCode:detailVue.emmp_BankBranchModel.subjCode,
			
			desubjCode:detailVue.emmp_BankBranchModel.desubjCode,
			bblcsubjCode:detailVue.emmp_BankBranchModel.bblcsubjCode,
			jgcksubjCode:detailVue.emmp_BankBranchModel.jgcksubjCode,
			
			interbankCode:detailVue.emmp_BankBranchModel.interbankCode,
			isDocking:detailVue.emmp_BankBranchModel.isDocking,

            //附件材料
            busiType : detailVue.busiType,
            sourceId : detailVue.tableId,
            generalAttachmentList : this.$refs.listenUploadData.uploadData,
		}
	}

	function update()
	{
		serverBodyRequest(baseInfo.updateInterface,detailVue.getUpdateForm(),function(jsonObj){
            generalSuccessModal()
            enterNewTabCloseCurrent(jsonObj.tableId,"开户行详情",'emmp/Emmp_BankBranchDetail.shtml')
		})
		// console.log(detailVue.getUpdateForm().generalAttachmentList)
	}

    function loadUpload(){
		generalLoadFile2(detailVue,detailVue.busiType)
    }

    function saveAttachment() {
        generalUploadFile(detailVue, "Emmp_BankBranch", baseInfo.attachmentBatchAddInterface)
    }

    function changeBankListener(data) {
        this.bankInfoId = data.tableId;
    }

    function initData()
	{
        getIdFormTab("",function (id) {
            detailVue.tableId=id
            refresh()
        })

    }

    function getGeneralBankList() {
        serverRequest("../Emmp_BankInfoForSelect",getTotalListForm(),function (jsonObj) {
            detailVue.generalBankList=jsonObj.emmp_BankInfoList
			console.log(detailVue.generalBankList)
        })

    }
    function dateFormat(date) {
        return moment(date).format("YYYY-MM-DD");
    }
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	// detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#emmp_BankBranchEditDiv",
	"detailInterface":"../Emmp_BankBranchDetail",
	"updateInterface":"../Emmp_BankBranchUpdate",

    //材料附件
    "loadUploadInterface": "../Sm_AttachmentCfgList",
    "attachmentBatchAddInterface": "../Sm_AttachmentBatchAdd",
    "attachmentListInterface": "../Sm_AttachmentList",
    //模态框
    "successModal": "#successM",
    "errorModal": "#errorM",
});
