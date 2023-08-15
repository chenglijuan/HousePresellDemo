(function(baseInfo){
    var listVue = new Vue({
        el : baseInfo.listDivId,
        data : {
            interfaceVersion :19000101,
            noticeTab: "",
            policyRecordTypeList: [],
            sm_PolicyRecordList: [],
        },
        methods : {
        	getNoticeData: function() {
        		
        	},
			getNoticeData: getNoticeData,
        }
    });

	//------------------------方法定义-开始------------------//
    
    // 公告信息
	function getNoticeData() {
		var model = {
			interfaceVersion:this.interfaceVersion,
			policyTypeCode: listVue.noticeTab
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
				listVue.policyRecordTypeList = jsonObj.policyRecordTypeList;
				// 公告内容
				listVue.sm_PolicyRecordList = jsonObj.sm_PolicyRecordList;
			}
		})
	}
	
	listVue.getNoticeData();
    
})({
    "listDivId":"#policyMoreDiv",
	"getNoticeInterface":"../Sm_HomePagePolicyRecordList",// 加载公告信息
});