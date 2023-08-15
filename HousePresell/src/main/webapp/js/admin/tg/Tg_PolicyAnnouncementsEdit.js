(function(baseInfo){
	var editVue = new Vue({
		el : baseInfo.editDivId,
		data : {
			interfaceVersion :19000101,
			tableId : "",
            busType:"21020401",
			busiCode:"21020401",
            policyType: "",
            policyDate: "",
            policyTitle: "",
			policyContent:"",
            sm_BaseParameterList: [],
            getPolicyData : '',
		},
		methods : {
			initData: initData,
			update: update,
			getPolicyType: function(data){
				editVue.policyType = data.theName;
				editVue.policyTypeCode  = data.theValue;
			},
			loadTypeData: function() {
				var model = {
					interfaceVersion:editVue.interfaceVersion,
					theState : 0,
					parametertype : 67
				}
				new ServerInterface(baseInfo.typeInterface).execute(model, function(jsonObj)
				{
					if(jsonObj.result != "success")
					{
						generalErrorModal(jsonObj);
					}
					else
					{
						editVue.sm_BaseParameterList = jsonObj.sm_BaseParameterList;
					}
				});
				
				// 详情
				getDetail();
			},
			issueHandle:function() {
				var model = {
					interfaceVersion:editVue.interfaceVersion,
					tableId:editVue.tableId,
					policyType: editVue.policyType,//公告类型名称
					policyTypeCode: editVue.policyTypeCode,//公告类型编码
					policyDate:document.getElementById("policyDate").value,
					policyTitle:editVue.policyTitle,
					//policyContent:$(".froala-element").html(),
					policyContent:addVue.policyContent,
					policyState:1
				}
				new ServerInterface(baseInfo.updateInterface).execute(model, function(jsonObj)
				{
					if(jsonObj.result != "success")
					{
						generalErrorModal(jsonObj);
					}
					else
					{
						enterNewTabCloseCurrent(editVue.tableId, '政策公告详情', 'tg/Tg_PolicyAnnouncementsDetail.shtml');
					}
				});
			}
		},
		components : {
			'vue-select': VueSelect,
		}
	});

	//------------------------方法定义-开始------------------//
	//详情操作--------------获取"公告政策详情"参数

	function getDetail()
	{
		var model = {
			interfaceVersion:editVue.interfaceVersion,
			tableId:editVue.tableId
		}
		new ServerInterface(baseInfo.detailInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				editVue.policyType = jsonObj.sm_PolicyRecord.policyType;
				editVue.policyTypeCode = jsonObj.sm_PolicyRecord.policyTypeCode;
				editVue.policyDate = jsonObj.sm_PolicyRecord.policyDate;
				editVue.policyTitle = jsonObj.sm_PolicyRecord.policyTitle;
				editVue.policyContent = jsonObj.sm_PolicyRecord.policyContent;

				// $(".froala-element").html("");
				// $(".froala-element").html(jsonObj.sm_PolicyRecord.policyContent);
			}
		});
	} 

	function update()
	{
		var model = {
			interfaceVersion:editVue.interfaceVersion,
			tableId:editVue.tableId,
			policyType: editVue.policyType,//公告类型名称
			policyTypeCode: editVue.policyTypeCode,//公告类型编码
			policyDate:document.getElementById("policyDate").value,
			policyTitle:editVue.policyTitle,
			policyContent:editVue.policyContent,
			policyState:0
		}
		new ServerInterface(baseInfo.updateInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				enterNewTabCloseCurrent(editVue.tableId, '政策公告详情', 'tg/Tg_PolicyAnnouncementsDetail.shtml');
			}
		});
	}
	
	function initData()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var list = tableIdStr.split("_");
		editVue.tableId = list[2];
        
        // 政策公告类型加载
        editVue.loadTypeData();
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	editVue.initData();
	
	// 添加日期控件
	laydate.render({
		elem: '#policyDate',
		max: getNowFormatDate(),
		  done: function(value, date){
			  editVue.getPolicyData=value;
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
	//------------------------数据初始化-结束----------------//
})({
	"editDivId":"#policyAnnouncementsEditDiv",
	"typeInterface":"../Sm_BaseParameterList",
	"detailInterface": "../Sm_PolicyRecordDetail",
	"detailInfoInterface": "../Emmp_BankInfoDetail",
	"updateInterface":"../Sm_PolicyRecordUpdate"
});
