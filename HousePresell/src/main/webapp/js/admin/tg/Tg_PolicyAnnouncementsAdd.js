(function(baseInfo) {
	var addVue = new Vue({
		el: baseInfo.addDivId,
		data: {
			interfaceVersion: 19000101,
			tableId: "",
			sm_BaseParameterList: [],
			policyType: "",
			policyTypeCode : "",
			policyTitle: "",
			policyContent:"",
			busType:"21020401",
			busiCode:"21020401",
			policyDate : getNowFormatDate(),
		},
		methods: {
			getPolicyData: getPolicyData,
			//getAddForm: getAddForm,
			//addPolicyAnnouncements: addPolicyAnnouncements,
			getPolicyType: function(data) {
				addVue.policyType = data.theName;
				addVue.policyTypeCode  = data.theValue;
				
			},
			saveHandle: function() {
				var model = {
					interfaceVersion: this.interfaceVersion,
					policyType: addVue.policyType,//公告类型名称
					policyTypeCode: addVue.policyTypeCode,//公告类型编码
					policyDate:document.getElementById('datePolicy').value,//发布时间
					policyTitle:addVue.policyTitle,//公告标题
					// policyContent:$(".froala-element").html(),//公告内容
					policyContent:addVue.policyContent,//公告内容
					policyState: 0,//公告状态
				}
				new ServerInterface(baseInfo.updateInterface).execute(model, function(jsonObj) {
					if(jsonObj.result != "success") {
						generalErrorModal(jsonObj);
					} else {
						enterNext2Tab(jsonObj.tableId, '政策公告详情', 'tg/Tg_PolicyAnnouncementsDetail.shtml',jsonObj.tableId+addVue.busType);
					}
				});
			},
			issueHandle: function() {
				var model = {
					interfaceVersion: this.interfaceVersion,
					policyType: addVue.policyType,//公告类型名称
					policyTypeCode: addVue.policyTypeCode,//公告类型编码
					policyDate:document.getElementById('datePolicy').value,//发布时间
					policyTitle:addVue.policyTitle,//公告标题
					policyContent:$(".froala-element").html(),//公告内容
					policyState: 1,//公告状态
				}
				new ServerInterface(baseInfo.updateInterface).execute(model, function(jsonObj) {
					if(jsonObj.result != "success") {
						generalErrorModal(jsonObj);
					} else {
						enterNext2Tab(jsonObj.tableId, '政策公告详情', 'tg/Tg_PolicyAnnouncementsDetail.shtml',jsonObj.tableId+addVue.busType);
					}
				});
			}
		},
		components: {
			'vue-select': VueSelect,
		}
	});

	//------------------------方法定义-开始------------------//
    
	//保存操作--------------获取"合作备忘录"参数
	/*function getAddForm() {
		var fileUploadList = addVue.$refs.listenUploadData.uploadData;
		fileUploadList = JSON.stringify(fileUploadList);
		return {
			interfaceVersion: this.interfaceVersion,
			eCode: addVue.eCode,
			userStartId: addVue.userStartId,
			tableId: addVue.tableId,
            bankId: addVue.bankId,
            theNameOfBank: addVue.theNameOfBank,
			signDate: document.getElementById('datePolicy').value,
			smAttachmentList:fileUploadList
		}
	}*/

	/*function addPolicyAnnouncements() {
		new ServerInterface(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj) {
			if(jsonObj.result != "success") {
				addVue.errMsg = jsonObj.info;
                $(baseInfo.errorModel).modal('show', {
				    backdrop :'static'
			    });
			} else {
				enterNewTab(jsonObj.tableId, '政策公告详情', 'tgxy/Tgxy_CoopMemoDetail.shtml',jsonObj.tableId+"06110102");
			}
		});
	}*/

	// 加载公告类型数据
	function getPolicyData() 
	{
		var model = {
			interfaceVersion: this.interfaceVersion,
			theState : 0,
			parametertype : 67
		}
		new ServerInterface(baseInfo.typeInterface).execute(model, function(jsonObj) {
			if(jsonObj.result != "success") {
				generalErrorModal(jsonObj);
			} else {
                addVue.sm_BaseParameterList = jsonObj.sm_BaseParameterList;
			}
		});
	}
	
	// 添加日期控件
	laydate.render({
		elem: '#datePolicy',
		max: getNowFormatDate(),
		  done: function(value, date){
			  addVue.getPolicyData=value;
		}
	});
	
	//获取当前时间
	function getNowFormatDate() {
        var date = new Date();
        var seperator1 = "-";
        var seperator2 = ":";
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate;
        return currentdate;
    }
	//------------------------方法定义-结束------------------//

	//------------------------数据初始化-开始----------------//
	addVue.getPolicyData(); // 政策公告类型
	
	//------------------------数据初始化-结束----------------//
})({
	"addDivId": "#policyAnnouncementsAddDiv",
	"typeInterface": "../Sm_BaseParameterList",
	"updateInterface": "../Sm_PolicyRecordAdd",
});