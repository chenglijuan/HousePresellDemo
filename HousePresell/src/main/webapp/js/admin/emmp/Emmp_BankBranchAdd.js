(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			emmp_BankBranchModel: {bankId: ""},
            generalBankList:[],
            errMsg:"",

            //附件材料
			busiType:'020202',
            loadUploadList: [],
            showDelete : true,  //附件是否可编辑
			bankInfoId:"",
			
			//对接资金系统
			isDockingList : [
            	{tableId:"1",theName:"是"},
            	{tableId:"0",theName:"否"}
            ],
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			//添加
			getAddForm : getAddForm,
			add: add,
            getGeneralBankList:getGeneralBankList,
            changeBankListener:changeBankListener,
            
            getIsDockingId : function(data){
            	detailVue.emmp_BankBranchModel.isDocking = data.tableId;
			},
			emptyIsDockingId : function(){
				detailVue.emmp_BankBranchModel.isDocking = null;
			}
		},
		computed:{
			 
		},
		components : {
            'vue-select': VueSelect,
            "my-uploadcomponent":fileUpload,
		},
		watch:{
			
		}
	});

	//------------------------方法定义-开始------------------//
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:detailVue.emmp_BankBranchModel.interfaceVersion,
		}
	}

	//详情操作--------------
	function refresh()
	{

	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getAddForm()
	{
		return {
			interfaceVersion:detailVue.interfaceVersion,
			theState:detailVue.emmp_BankBranchModel.theState,
			busiState:detailVue.emmp_BankBranchModel.busiState,
			eCode:detailVue.emmp_BankBranchModel.eCode,
			userStartId:detailVue.emmp_BankBranchModel.userStartId,
			createTimeStamp:detailVue.emmp_BankBranchModel.createTimeStamp,
			lastUpdateTimeStamp:detailVue.emmp_BankBranchModel.lastUpdateTimeStamp,
			userRecordId:detailVue.emmp_BankBranchModel.userRecordId,
			recordTimeStamp:detailVue.emmp_BankBranchModel.recordTimeStamp,
			// bankId:detailVue.emmp_BankBranchModel.bankId,
			bankId:detailVue.bankInfoId,
			theName:detailVue.emmp_BankBranchModel.theName,
			shortName:detailVue.emmp_BankBranchModel.shortName,
			address:detailVue.emmp_BankBranchModel.address,
			contactPerson:detailVue.emmp_BankBranchModel.contactPerson,
			contactPhone:detailVue.emmp_BankBranchModel.contactPhone,
			leader:detailVue.emmp_BankBranchModel.leader,
			subjCode:detailVue.emmp_BankBranchModel.subjCode,
			
			desubjCode:detailVue.emmp_BankBranchModel.desubjCode,
			bblcsubjCode:detailVue.emmp_BankBranchModel.bblcsubjCode,
			jgcksubjCode:detailVue.emmp_BankBranchModel.jgcksubjCode,
			
			interbankCode:detailVue.emmp_BankBranchModel.interbankCode,
			isDocking:detailVue.emmp_BankBranchModel.isDocking,

            //附件材料
            busiType : '020202',
            generalAttachmentList : this.$refs.listenUploadData.uploadData
		}
	}

	function add()
	{
		new ServerInterfaceJsonBody(baseInfo.addInterface).execute(detailVue.getAddForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				// noty({"text":jsonObj.info,"type":"error","timeout":2000});
                // generalErrorModal(jsonObj)
				generalErrorModal(jsonObj)
			}
			else
			{
				// refresh();
                generalSuccessModal()
                enterNewTabCloseCurrent(jsonObj.tableId,"开户行详情",'emmp/Emmp_BankBranchDetail.shtml')
			}
		});
	}

	function getGeneralBankList() {
		serverRequest("../Emmp_BankInfoForSelect",getTotalListForm(),function (jsonObj) {
            detailVue.generalBankList=jsonObj.emmp_BankInfoList
        })

    }

    function changeBankListener(data) {
		detailVue.bankInfoId=data.tableId

    }
	
	function initData()
	{
		generalLoadFile2(detailVue,detailVue.busiType)
		getGeneralBankList()
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#emmp_BankBranchAddDiv",
	"detailInterface":"../Emmp_BankBranchDetail",
	"addInterface":"../Emmp_BankBranchAdd",

    //其他
    "successModal":"#successM",
    "errorModal":"#errorM",
});
