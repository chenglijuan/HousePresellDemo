(function(baseInfo) {
	var detailVue = new Vue({
		el: baseInfo.detailDivId,
		data: {
			interfaceVersion: 19000101,
			tableId: "",
			policyModel: {},
			busType:"21020401",
			busiCode:"21020401",
		},
		methods: {
			init: function() {
				var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
				var list = tableIdStr.split("_");
				detailVue.tableId = list[2];
				
				console.log("detailVue.tableId="+detailVue.tableId);
			},
			getPolicyDetailData: function() {
				var model = {
					interfaceVersion: this.interfaceVersion,
					tableId: detailVue.tableId
				}
				new ServerInterface(baseInfo.detailInterface).execute(model, function(jsonObj) {
					if(jsonObj.result != "success") {
						generalErrorModal(jsonObj);
					} else {
						detailVue.policyModel = jsonObj.sm_PolicyRecord;
						/*var obj = setInterval((function() {*/
							$(".contentBox").append(jsonObj.sm_PolicyRecord.policyContent);
						/*},500));*/
					}
				});
			},
			issueHandle: function() {
				var model = {
					interfaceVersion: detailVue.interfaceVersion,
					policyState: 1,//公告状态
					tableId : detailVue.tableId,
				}
				new ServerInterface(baseInfo.issueInterface).execute(model, function(jsonObj) {
					if(jsonObj.result != "success") {
						generalErrorModal(jsonObj);
					} else {
						detailVue.getPolicyDetailData();
						/*enterNextTab(detailVue.tableId, '政策公告详情', 'tg/Tg_PolicyAnnouncementsDetail.shtml',detailVue.tableId + detailVue.busType);*/
					}
				});
			},
			editHandle: function() {
				enterNewTabCloseCurrent(detailVue.tableId, '政策公告编辑', 'tg/Tg_PolicyAnnouncementsEdit.shtml');
			}
		},
		components: {
			'vue-select': VueSelect,
		}
	});

	//------------------------方法定义-开始------------------//

	// 添加日期控件
	laydate.render({
		elem: '#datePolicy',
	});
	//------------------------方法定义-结束------------------//

	//------------------------数据初始化-开始----------------//
	detailVue.init();
	detailVue.getPolicyDetailData(); // 政策公告详情
	
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId": "#policyAnnouncementsDetailDiv",
	"detailInterface": "../Sm_PolicyRecordDetail",
	"issueInterface": "../Sm_PolicyRecordUpdate",
});