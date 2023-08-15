(function(baseInfo) {
	var addVue = new Vue({
		el: baseInfo.addDivId,
		data: {
			interfaceVersion: 19000101,
			tgxy_CoopMemoModel: {},
			pageNumber: 0,
			countPerPage: 0,
			eCode: "",
			userStartId: 1,
			bankValue: 0,
			signDate: "",
			tableId: "",
			bankId: 0,
			theNameOfBank: null,
			emmp_BankInfoList: [
				{ theName: '中国银行', tableId: "0" },
				{ theName: '中国建设银行', tableId: "1" },
				{ theName: '工行', tableId: "2" }
			],
			uploadData : [],
			errMsg: "",
			
			// 附件上传相关参数
			loadUploadList: [],
            showDelete : true,
            busiType : '06110102',
			/*dataUpload:{
				"appid":"oss002e5c",
				"appsecret":"egg0s9u3"
			},*/
		},
		methods: {
			//详情
			refresh: refresh,
			initData: initData,
			//添加
			getAddForm: getAddForm,
			// 保存
			add: addCoopMemo,
			getBankData: getBankData,
			getBankForm: getBankForm,
			look: look,
			getTableForm: getTableForm,
			errClose: errClose,
			succClose: succClose,
			
			// 附件上传相关加载
			getUploadForm : getUploadForm,
			loadUpload : loadUpload,
			getTableId: function (data){
				this.tableId = data.tableId;
				addVue.look();
			},
		},
		computed: {

		},
		components: {
			"my-uploadcomponent":fileUpload,
			'vue-select': VueSelect,
		},
		watch: {

		}
	});

	//------------------------方法定义-开始------------------//
	function getTableForm() {
		return {
			interfaceVersion: this.interfaceVersion,
			tableId: addVue.tableId,
		}
	}

	//详情操作--------------
	function refresh() {
		this.tgxy_CoopMemoModel = {};
		addVue.eCode = "";
		addVue.tableId = "";
		document.getElementById("date0611010202").value = "";
	}

    // 根据选择的下拉框数据的id获取对应的text值
    function look()
    {
		new ServerInterface(baseInfo.getBankInfoDetailInterface).execute(addVue.getTableForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{	
				addVue.bankId = addVue.tableId;
				addVue.theNameOfBank = jsonObj.emmp_BankInfo.theName;
			}
		});
    }
    
	//保存操作--------------获取"合作备忘录"参数
	function getAddForm() {
		var fileUploadList = addVue.$refs.listenUploadData.uploadData;
		fileUploadList = JSON.stringify(fileUploadList);
		return {
			interfaceVersion: this.interfaceVersion,
			eCode: addVue.eCode,
			userStartId: addVue.userStartId,
			tableId: addVue.tableId,
            bankId: addVue.bankId,
            theNameOfBank: addVue.theNameOfBank,
			signDate: document.getElementById('date0611010202').value,
			smAttachmentList:fileUploadList
		}
	}
	function getBankForm() {
		return {
			interfaceVersion: this.interfaceVersion,
			pageNumber: this.pageNumber
		}
	}

	function addCoopMemo() {
		new ServerInterface(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				addVue.errMsg = jsonObj.info;
                $(baseInfo.errorModel).modal('show', {
				    backdrop :'static'
			    });
			} else {
				enterNewTab(jsonObj.tableId, '合作备忘录详情', 'tgxy/Tgxy_CoopMemoDetail.shtml',jsonObj.tableId+"06110102");
				refresh();
			}
		});
	}
	
	function errClose()
	{
		$(baseInfo.errorModel).modal('hide');
	}
	
	function succClose()
	{
		$(baseInfo.successModel).modal('hide');
	}

	function initData() {

	}

	// 加载银行列表数据
	function getBankData() 
	{
		new ServerInterface(baseInfo.getInterface).execute(addVue.getBankForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
//				refresh();
                // 银行列表数据加载
                addVue.emmp_BankInfoList = jsonObj.emmp_BankInfoList;
			}
		});
	}
	
	//列表操作-----------------------获取附件参数
		function getUploadForm(){
			return{
				pageNumber : '0',
				busiType : addVue.busiType,
				interfaceVersion:this.interfaceVersion
			}
		}
		
		//列表操作-----------------------页面加载显示附件类型
		function loadUpload(){
			new ServerInterface(baseInfo.loadUploadInterface).execute(addVue.getUploadForm(), function(jsonObj)
			{
				if(jsonObj.result != "success")
				{
					$(baseInfo.errorModel).modal("show",{
						backdrop :'static'
					});
					addVue.errMsg = jsonObj.info;
				}
				else
				{
					//refresh();
					addVue.loadUploadList = jsonObj.sm_AttachmentCfgList; 
				}
			});
		}
	
	// 添加日期控件
	laydate.render({
		elem: '#date0611010202',
	});
	//------------------------方法定义-结束------------------//

	//------------------------数据初始化-开始----------------//
	addVue.refresh();
	addVue.initData();
	addVue.getBankData();
	addVue.loadUpload();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId": "#tgxy_CoopMemoAddDiv",
	"errorModel":"#errorM1",
	"successModel":"#successM1",
	"detailInterface": "../Tgxy_CoopMemoDetail",
	"addInterface": "../Tgxy_CoopMemoAdd",
	"getInterface": "../Emmp_BankInfoList",
	"getBankInfoDetailInterface": "../Emmp_BankInfoDetail",
	"loadUploadInterface":"../Sm_AttachmentCfgList",
});