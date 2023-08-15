(function(baseInfo) {
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion : 19000101,
			tgxy_EscrowAgreementModel : {
				theNameOfProject : "",
			},
			tableId : "",
			showButtonEscrow : true,
			selectItem : [],
			tgxy_EscrowAgreementEditList : [],
			chooseBuilding : [],
			isAllSelected : false,
			ProjectNames : [],
			creatCityRegion : [],
			creatUserUpdate : [],
			creatUserRecord : [],
			currProjectId : "",
			errEscrowAdd : "",
			smAttachmentList : [],
			loadUploadList : [],
			showDelete : true,
			busiType : '06110201',
			disputeResolutionList : [
            	{tableId:"1",theName:"向常州仲裁委员会申请仲裁"},
            	{tableId:"2",theName:"向有管辖权的人民法院起诉"},
            ]
		},
		methods : {
			//详情
			refresh : refresh,
			initData : initData,
			getSearchForm : getSearchForm,
			//更新
			getUpdateForm : getUpdateForm,
			update : update,

			loadForm : loadForm, //form表单更新数据
			getEscrowForm : getEscrowForm,
			gettimeForm : gettimeForm,
			indexMethod : indexMethod,
			getmesForm : getmesForm,
			projectChange : projectChange,
			getLoadForm : getLoadForm,
			loadBulidingList : loadBulidingList,
			checkCheckBox : checkCheckBox,
			errClose : errClose,
			succClose : succClose,
			getDisputeResolution : function(data){
				this.tgxy_EscrowAgreementModel.disputeResolution = data.tableId;
			},
			emptyDisputeResolution : function(){
				this.tgxy_EscrowAgreementModel.disputeResolution = "";
			},
			bldLimitAmountDetail : bldLimitAmountDetail,//跳转受限额度版本
		},
		computed : {

		},
		components : {
			"my-uploadcomponent" : fileUpload,
			'vue-select': VueSelect,
		},
		watch : {
			selectItem : selectedItemChanged,
		},
	});

	//------------------------方法定义-开始------------------//
	
	function bldLimitAmountDetail(tableId){
//		enterNextTab(tableId, '受限额度节点详情', 'tgpj/Tgpj_BldLimitAmountVer_AFDetail.shtml',tableId+"06110201");
		enterNewTab(tableId, "受限额度节点详情", "tgpj/Tgpj_BldLimitAmountVer_AFDetail.shtml")
	}
	
	function indexMethod(index) {
		return generalIndexMethod(index, detailVue)
	}




	//传出信息
	function getEscrowForm() {
		return {
			interfaceVersion : this.interfaceVersion,
			projectId : detailVue.tgxy_EscrowAgreementModel.theNameOfProject,
		}
	}
	function getmesForm() {
		return {
			interfaceVersion : this.interfaceVersion,
			tableId : detailVue.tgxy_EscrowAgreementModel.theNameOfProject,
		}
	}

	function gettimeForm() {
		return {
			interfaceVersion : this.interfaceVersion,
			contractApplicationDate : detailVue.tgxy_EscrowAgreementModel.contractApplicationDate,
		}
	}

	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged() {
		detailVue.isAllSelected = (detailVue.tgxy_EscrowAgreementEditList.length > 0)
			&& (detailVue.selectItem.length == detailVue.tgxy_EscrowAgreementEditList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked() {
		if (detailVue.selectItem.length == detailVue.tgxy_EscrowAgreementEditList.length) {
			detailVue.selectItem = [];
		} else {
			detailVue.selectItem = []; //解决：已经选择一个复选框后，再次点击全选样式问题
			detailVue.tgxy_EscrowAgreementEditList.forEach(function(item) {
				detailVue.selectItem.push(item.tableId);
			});
		}
	}


	//详情操作--------------
	function refresh() {
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var list = tableIdStr.split("_");
		tableIdStr = list[2];
		detailVue.tableId = tableIdStr;
		if (this.tableId == null || this.tableId < 1) {
			return;
		}

		getDetail();
	}
	//详情操作--------------获取"机构详情"参数
	function getSearchForm() {
		return {
			interfaceVersion : this.interfaceVersion,
			tableId : detailVue.tableId,
		}
	}
	function getLoadForm() {
		return {
			interfaceVersion : this.interfaceVersion,
//			developCompanyId : '201',
		}
	}

	function getDetail() {
		new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
				detailVue.errEscrowAdd = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
					backdrop : 'static'
				});
			} else {
				detailVue.tgxy_EscrowAgreementModel = jsonObj.tgxy_EscrowAgreement;
				detailVue.creatCityRegion = jsonObj.tgxy_EscrowAgreement.cityRegion;
				detailVue.creatUserUpdate = jsonObj.tgxy_EscrowAgreement.userUpdate;
				detailVue.creatUserRecord = jsonObj.tgxy_EscrowAgreement.userRecord;
				detailVue.chooseBuilding = jsonObj.tgxy_EscrowAgreement.buildingInfoList
				detailVue.tgxy_EscrowAgreementModel.theNameOfProject = jsonObj.tgxy_EscrowAgreement.projectId;
				detailVue.currProjectId = jsonObj.tgxy_EscrowAgreement.projectId;
				detailVue.loadUploadList = jsonObj.smAttachmentCfgList;
				loadBulidingList();
			}
		});

	}

	//详情更新操作--------------获取"更新机构详情"参数
	function getUpdateForm() {
		//获取选中的楼幢
		this.chooseArr = [];
		detailVue.selectItem.forEach(function(item) {
			detailVue.chooseArr.push(item.tableId);
		})
		var fileUploadList = detailVue.$refs.listenUploadData.uploadData;
		fileUploadList = JSON.stringify(fileUploadList);
		return {
			tableId : this.tableId,
			interfaceVersion : this.interfaceVersion,
			userUpdateId : '2',
			contractApplicationDate : this.tgxy_EscrowAgreementModel.contractApplicationDate,
			agreementVersion : detailVue.tgxy_EscrowAgreementModel.agreementVersion,
			OtherAgreedMatters : this.tgxy_EscrowAgreementModel.OtherAgreedMatters,
			disputeResolution : this.tgxy_EscrowAgreementModel.disputeResolution,
			projectId : detailVue.tgxy_EscrowAgreementModel.theNameOfProject,
			idArr : this.chooseArr,
			escrowCompany : '常州正泰房产居间服务有限公司',
			smAttachmentList : fileUploadList,
		}

	}

	function update() {
		new ServerInterface(baseInfo.updateInterface).execute(detailVue.getUpdateForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
				detailVue.errEscrowAdd = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
					backdrop : 'static'
				});
			} else {
				refresh();
				enterNext2Tab(detailVue.tableId, '贷款托管合作协议详情', 'tgxy/Tgxy_EscrowAgreementDetail.shtml',detailVue.tableId+"06110201");

			}
		});
	}

	function initData() {
	}



	//加载项目名称
	function loadForm() {
		new ServerInterface(baseInfo.loadInterface).execute(detailVue.getLoadForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
				detailVue.errEscrowAdd = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
					backdrop : 'static'
				});
			} else {
				detailVue.ProjectNames = jsonObj.empj_ProjectInfoList;
			}
		});
	}
	//楼幢信息
	function loadBulidingList() {
		new ServerInterface(baseInfo.escrowInterface).execute(detailVue.getEscrowForm(), function(jsonObj) {
			var _this = this;
			if (jsonObj.result != "success") {
				detailVue.errEscrowAdd = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
					backdrop : 'static'
				});
			} else {
				detailVue.tgxy_EscrowAgreementEditList = jsonObj.empj_BuildingInfoList;

				//将查询出的楼幢列表拼接
				detailVue.chooseBuilding.forEach(function(item, index) {

					detailVue.tgxy_EscrowAgreementEditList.push(item);

				});

				detailVue.chooseBuilding.forEach(function(itemChoose, index) {
					detailVue.tgxy_EscrowAgreementEditList.forEach(function(item, index) {
						if (itemChoose.tableId == item.tableId) {
							if (detailVue.tgxy_EscrowAgreementEditList) {
								detailVue.$nextTick(function() {
									detailVue.$refs.moviesTable.toggleRowSelection(detailVue.tgxy_EscrowAgreementEditList[index], true);
								})
							} else {
								_this.$refs.moviesTable.clearSelection();
							}

						}
					});
				});
			}
		});
	}
	function checkCheckBox(tableId) {
		this.selectItem = tableId;
	}
	//根据选择的项目信息加载楼幢信息
	function projectChange() {

		//展示开发企业名称和所属区域
		new ServerInterface(baseInfo.mesInterface).execute(detailVue.getmesForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
				detailVue.errEscrowAdd = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
					backdrop : 'static'
				});
			} else {
				detailVue.tgxy_EscrowAgreementModel.cityRegionName = jsonObj.empj_ProjectInfo.cityRegionName;
				detailVue.tgxy_EscrowAgreementModel.theNameOfDevelopCompany = jsonObj.empj_ProjectInfo.developCompanyName;

			}
		});
		//楼幢信息
		new ServerInterface(baseInfo.escrowInterface).execute(detailVue.getEscrowForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
				detailVue.errEscrowAdd = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
					backdrop : 'static'
				});
			} else {
				detailVue.tgxy_EscrowAgreementAddList = jsonObj.empj_BuildingInfoList;

			}
		});
	}
	laydate.render({
		elem : '#dete0611020103',
		max: getNowFormatDate(),
		done : function(value, date) {
			detailVue.tgxy_EscrowAgreementModel.contractApplicationDate = value;
			timeschange();
		}
	});
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
	function timeschange() {
		new ServerInterface(baseInfo.timesInterface).execute(detailVue.gettimeForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
				detailVue.errEscrowAdd = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
					backdrop : 'static'
				});
			} else {

				detailVue.tgxy_EscrowAgreementModel.agreementVersion = jsonObj.tgxy_CoopAgreementVerMngList[0].theVersion;
				detailVue.verMngId = jsonObj.tgxy_CoopAgreementVerMngList[0].tableId;

			}
		});

	}
	function errClose() {
		$(baseInfo.errorModel).modal('hide');
	}

	function succClose() {
		$(baseInfo.successModel).modal('hide');
	}

	//------------------------方法定义-结束------------------//

	//------------------------数据初始化-开始----------------//
	detailVue.loadForm();
	detailVue.refresh();
	detailVue.initData();
//------------------------数据初始化-结束----------------//
})({
	"detailDivId" : "#tgxy_EscrowAgreementDiv",
	"errorModel" : "#errorEscrowAdd12",
	"successModel" : "#successEscrowAdd12",
	"escrowInterface" : "../Empj_BuildingInfoByEscList",
	"detailInterface" : "../Tgxy_EscrowAgreementDetail",
	"updateInterface" : "../Tgxy_EscrowAgreementUpdate",
	"mesInterface" : "../Empj_ProjectInfoDetail",
	"timesInterface" : "../Tgxy_CoopAgreementVerMngList",
	"loadInterface" : "../Empj_ProjectInfoList",
});