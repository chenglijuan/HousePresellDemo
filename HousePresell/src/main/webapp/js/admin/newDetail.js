(function(baseInfo){
	var newVue = new Vue({
		el : baseInfo.newDivId,
		data : {
			interfaceVersion :19000101,
			tableId: "",
			sm_PolicyRecord:{},
		},
		methods : {
			loadId:loadId,
			getNoticeData: getNoticeData,
			changeFontHandle: function(index) {
				$(".contentBox").removeClass("fontBig");
				$(".contentBox").removeClass("fontSmall");
				if(index == 0) {
					$(".contentBox").addClass("fontBig");
				}
				if(index == 2) {
					$(".contentBox").addClass("fontSmall");
				}
			}
		}
	});

	//------------------------方法定义-开始------------------//
	
	// 公告信息
	function getNoticeData() {
		var model = {
			interfaceVersion:this.interfaceVersion,
			tableId: newVue.tableId
		}
		new ServerInterface(baseInfo.getNoticeInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				/*详情信息*/
				newVue.sm_PolicyRecord = jsonObj.sm_PolicyRecord;
				
				// 政策公告内容
				if(newVue.sm_PolicyRecord.policyContent != null) {
					$(".nothing").parent().hide();
					$("#content").show();
					$("#content").append(newVue.sm_PolicyRecord.policyContent);
				}else{
					$("#content").hide();
					$(".nothing").parent().show();
					$(".nothing").html("暂无相关信息！敬请期待......");
				}
			}
		})
	}
	
	function loadId() {
		newVue.tableId = getRequestParams("id");
	}
	
	function getRequestParams(param) {
		var reg = new RegExp("(^|&)" + param + "=([^&]*)(&|$)", "i");
		var r = window.location.search.substr(1).match(reg);
		if (r != null) return unescape(r[2]); returnnull;
	}
	newVue.loadId();
	newVue.getNoticeData();
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-结束----------------//
})({
	"newDivId":"#newDetailBox",
	"getNoticeInterface":"../Sm_PolicyRecordDetail",
});
