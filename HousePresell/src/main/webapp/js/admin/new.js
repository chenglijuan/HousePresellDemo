(function(baseInfo){
	var newVue = new Vue({
		el : baseInfo.newDivId,
		data : {
			interfaceVersion :19000101,
			noticeTab: "67001",
			policyRecordTypeList: [],
			sm_PolicyRecordList: [],
		},
		methods : {
			getNoticeData:getNoticeData,
			tabHandle: function(index, theValue) {
				$(".tabF").removeClass("firstTab");
				$(".tabF:eq('"+ index +"')").addClass("firstTab");
				newVue.noticeTab = theValue;
				newVue.getNoticeData();
			},
			detailHandle: function(tableId) {
				window.open("newDetail.shtml?id="+tableId);
			}
		}
	});

	//------------------------方法定义-开始------------------//
	
	// 公告信息
	function getNoticeData() {
		var model = {
			interfaceVersion:this.interfaceVersion,
			policyTypeCode: newVue.noticeTab
		}
		new ServerInterface(baseInfo.getNoticeInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				// 公告类型
				newVue.policyRecordTypeList = jsonObj.policyRecordTypeList;
				// 公告内容
				newVue.sm_PolicyRecordList = jsonObj.sm_PolicyRecordList;
			}
		})
	}
	
	newVue.getNoticeData();
 
/*		loginVue.outdata = CInfControlLogin.HD_ReadFileCtl(0, 0x0001);
		if(loginVue.outdata == "") {
			generalErrorModal("","请检查KEY是否正确插入或者是否授权!");
		}else {*/
			/*new ServerInterface(baseInfo.loginInterface).execute(loginVue.getLoginForm(), function(jsonObj)
			{
				if(jsonObj.result != "success")
				{
					loginVue.errMsg = jsonObj.info;
					loginVue.ifShowflag = true;
				}
				else
				{
					window.location.href='index.shtml';
//							refresh();
				}
			});*/
//		}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-结束----------------//
})({
	"newDivId":"#newBox",
	"getNoticeInterface":"../Sm_HomePagePolicyRecordInitList",
	"Sm_PolicyRecordDetail":"../Sm_PolicyRecordDetail"
});
