(function(baseInfo){
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			developCompanyId : "",
			eCode : "",
			projectName : "",
			theName : "",
			bankBranchId : "",
            theAccount:"",
            theNameOfBank:"",
//			busiState : ,
			theState: 0, //正常为0，删除为1
			bankBranchList : [],
			companyInfoList : [],

            //附件材料
			busiType:'200102',
            loadUploadList: [],
            showDelete : true,  //附件是否可编辑
		},
		methods : {
			//获取开发企业列表
			getCampanyInfoListForm : getCampanyInfoListForm,
			getCampanyInfoList : getCampanyInfoList,

			//获取开户行列表
			getBankBranchInfoListForm : getBankBranchInfoListForm,
			// getBankBranchInfoList : getBankBranchInfoList,
			getAddForm : getAddForm,
			bankAccountAdd : bankAccountAdd,
			initData : initData,
			add:add,
            changeBankListener:changeBankListener,
            changeCompanyListener:changeCompanyListener,

		},
		computed:{},
        components: {
            'vue-select': VueSelect,
            "my-uploadcomponent":fileUpload,
        },
        watch: {

		}
	});


	//------------------------方法定义-开始------------------//
	//获取开发企业列表
	function getCampanyInfoListForm()
	{
		return {
			interfaceVersion:addVue.interfaceVersion,
			theState: addVue.theState,
            exceptZhengTai : true,
            theType:1,
//			pageNumber: 1,
//			countPerPage: 99,
//			totalPage: 1,
//			totalCount: 1,
		}
	}

	function getCampanyInfoList()
	{
		console.log(addVue.getCampanyInfoListForm());
		new ServerInterface(baseInfo.companyListInterface).execute(addVue.getCampanyInfoListForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				console.log(jsonObj);
				addVue.companyInfoList = jsonObj.emmp_CompanyInfoList;
			}
		});
	}

	//获取开户行列表
	function getBankBranchInfoListForm()
	{
		return {
			interfaceVersion:addVue.interfaceVersion,
			pageNumber: 1,
			countPerPage: 99,
			totalPage: 1,
			totalCount: 1,
		}
	}

	function getBankBranchInfoList ()
	{
		serverRequest(baseInfo.bankBranchListInterface,getTotalListForm(),function (jsonObj) {
            addVue.bankBranchList = jsonObj.emmp_BankBranchList;
        })

	}

	//详情更新操作--------------获取"更新机构详情"参数
	function getAddForm()
	{
		return {
			interfaceVersion:addVue.interfaceVersion,
			developCompanyId:addVue.developCompanyId,
			// eCode:addVue.eCode,
			theAccount:addVue.theAccount,
			theNameOfProject :addVue.projectName,
			theName:addVue.theName,
			/*bankBranchId:addVue.bankBranchId,*/
			theNameOfBank:addVue.theNameOfBank,
			busiState:1,
//			theState:0, //正常为0，删除为1
	            //附件材料
            busiType : addVue.busiType,
            generalAttachmentList : addVue.$refs.listenUploadData.uploadData
		}
	}

	function bankAccountAdd()
	{
		new ServerInterface(baseInfo.bankAccountaddInterface).execute(addVue.getAddForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				$(baseInfo.addDivId).modal('hide');
				addVue.developCompanyId = "";
				addVue.eCode = "";
				addVue.projectName = "";
				addVue.theName = "";
				addVue.bankBranchId = "";
			}
		});
	}

    function add() {
        serverBodyRequest(baseInfo.bankAccountaddInterface, getAddForm(), function (jsonObj) {
            generalSuccessModal()
            enterNewTabCloseCurrent(jsonObj.tableId,"监管账户详情","tgpj/Tgpj_BankAccountSupervisedDetail.shtml")
        }, function (jsonObj) {
            generalErrorModal(jsonObj)
        })
    }

    function changeBankListener(data) {
        addVue.bankBranchId =data.tableId
    }

    function changeCompanyListener(data) {
        addVue.developCompanyId =data.tableId
    }

	function initData()
	{
		generalLoadFile2(addVue,addVue.busiType)
		getBankBranchInfoList();
		getCampanyInfoList();
	}
	//------------------------方法定义-结束------------------//

	//------------------------数据初始化-开始----------------//
	addVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#tgpj_BankAccountSupervisedAddDiv",
	"bankBranchListInterface":"../Emmp_BankBranchForSelect",
	"companyListInterface": "../Emmp_CompanyInfoForSelect",
	"bankAccountaddInterface":"../Tgpj_BankAccountSupervisedAdd",
});
