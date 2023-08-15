(function(baseInfo){
	var buildingTableVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion : 19000101,
			projectId : null,
			empj_BuildingInfoList : [],
			eCodeFromConstruction : "",
			empj_BuildingInfo : {},
			empj_BuildingTableTemplateList : [],
			empj_GarageTableTemplateList : [],
			houseConstructioneCode : "",
			houseUniteCode : "",
			houseRoomId : "",
			houseDetailInfo : "",
			houseDetailInfoList : [],
			houseLoanAmountList : [],
			buildingId : null,
			buildingAvgPriceId : null,
			bldEscrowCompletedDtlId : null,
			erfloorHouseCount : null,
			erfloorGarageCount : null,
			clickBuildingId : null,
			companyId:"",
			tgpj_BuildingAccount : {},
			empj_HouseInfo : {
				position:"",
				actualArea :"",
				floor :"",
				recordPrice :"",
				roomId : "",
				shareConsArea : "",
				purpose : "",
				unitInfo :{theName:""},
				innerconsArea :"",
				busiState :"",
			},
			Tg_DepositHouseholdsDtl_View : [],
			pageNumber : 1,
			countPerPage : 10,
			totalPage : 0,
			totalCount : 0,
			keyword : "",
			
		},
		methods:{
			indexMethod: indexMethod,
			changePageNumber : function(data){
				if (buildingTableVue.pageNumber != data) {
					buildingTableVue.pageNumber = data;
					
					loadHouseList();
					
				}
			},
			changeCountPerPage:function(data){
				if (buildingTableVue.countPerPage != data) {
					buildingTableVue.countPerPage = data;
					
					loadHouseList();
					
				}
			},
			initData : initData,
			getBuildingDetail : getBuildingDetail,
			openHouseModal : openHouseModal,
			moveToBuildingDetail : function () {
				$("#business-handling").modal('hide');
				var theTableId = 'Empj_BuildingInfoDetail_' + this.buildingId;
				$("#tabContainer").data("tabs").addTab({ id: theTableId, text: '楼幢详情', closeable: true, url: 'empj/Empj_BuildingInfoDetail.shtml' });
			},
			moveToBankAccountDetail : function () {
				$("#business-handling").modal('hide');
				console.log('this.buildingId is '+this.buildingId)
                enterNewTab(this.buildingId+"-BuildingTable","楼幢监管账户","tgpj/Tgpj_BankAccountBuildingSupervisedList.shtml")
		    },
		    moveToFunAppropriated : function () {
				$("#business-handling").modal('hide');
				var theTableId ="buildindinfoTable"+"_"+ this.projectId;
                enterNewTab(theTableId,"用款计划申请","tgpf/Tgpf_FundAppropriated_AFList.shtml")
		    },
		    moveToBuildingAvgPriceDetail : function () {
				$("#business-handling").modal('hide');
				var theTableId = 'BuildingAvgPriceDetail_' + this.buildingAvgPriceId;
				// $("#tabContainer").data("tabs").addTab({ id: theTableId, text: '备案均价详情', closeable: true, url: 'tgpj/Tgpj_BuildingAvgPriceDetail.shtml' });
				if(this.buildingAvgPriceId==undefined){
					// generalErrorModal(undefined,"该楼幢暂无备案均价")
					var innerId="BldTable-"+this.projectId+"-"+this.companyId+"-"+this.buildingId
					enterNewTab(innerId,"备案均价","tgpj/Tgpj_BuildingAvgPriceList.shtml")
				}else{
                    enterNewTab(this.buildingAvgPriceId,"备案均价详情","tgpj/Tgpj_BuildingAvgPriceDetail.shtml")
				}

		    },
		    moveToBldEscrowCompletedDetail : function () {
				$("#business-handling").modal('hide');
                enterNewTab(this.projectId+"-BuildingTable-"+this.companyId,"托管终止","empj/Empj_BldEscrowCompletedList.shtml")
		    },
            moveToPjDevProgressForcastList : function () {
                $("#business-handling").modal('hide');
                enterNewTab(this.projectId+"-BuildingTable-"+this.companyId+"-"+this.buildingId,"进度巡查预测","empj/Empj_PjDevProgressForcastList.shtml")
            },
		    moveToEscrowAgreementAdd : function(){
		    	$("#business-handling").modal('hide');
				var theTableId = this.buildingId;
				enterNewTab(theTableId,"贷款托管合作协议","tgxy/Tgxy_EscrowAgreementForBuildingAdd.shtml")
				//$("#tabContainer").data("tabs").addTab({ id: theTableId, text: '贷款托管合作协议', closeable: true, url: 'tgxy/Tgxy_EscrowAgreementForBuildingAdd.shtml' });
		    },
            moveToEmpj_BldLimitAmount_AFDetail:function () {
                $("#business-handling").modal('hide');
                enterNewTab(this.projectId+"-BuildingTable-"+this.companyId+"-"+this.buildingId,"受限额度管理","empj/Empj_BldLimitAmount_AFList.shtml")
            },
			openRightModel : openRightModel,
			linkToTripleAgreementForUnitAdd : function(tableId){//点击贷款三方托管协议签署
				var theTableId = tableId;
				var model = {
					interfaceVersion : this.interfaceVersion,
					roomid: theTableId,
				}
			 	new ServerInterface("../Tgxy_RoomJumpTriple").execute(model, function(jsonObj)
				{
					if(jsonObj.result != "fail")
					{
						if(jsonObj.tgxy_TripleAgreement != undefined) {
							enterNextTab(theTableId, '新增贷款托管三方协议', 'tgxy/Tgxy_TripleAgreementForUnitAdd.shtml',theTableId+"06110301");
						}else {
							generalErrorModal(null,jsonObj.info);
						}
					}else{
						generalErrorModal(null,jsonObj.info);
					}
				});
				
				
			},
			linkToRefundInfo : function(tableId){//点击退房退款（贷款已结清）
				var theTableId = 'jump_Tgpf_RefundInfoAdd_' + tableId;
				$("#tabContainer").data("tabs").addTab({ id: theTableId, text: '新增退房退款（贷款已结清）', closeable: true, url: 'tgpf/Tgpf_RefundInfoAdd.shtml' });
			},
			linkTo_RefundInfoUncleared : function(tableId){//点击退房退款（贷款未结清）
				var theTableId = 'jump_Tgpf_RefundInfoUnclearedAdd_' + tableId;
				$("#tabContainer").data("tabs").addTab({ id: theTableId, text: '新增退房退款（贷款未结清）', closeable: true, url: 'tgpf/Tgpf_RefundInfoUnclearedAdd.shtml' });
			},
			closeHouseInfo : closeHouseInfo,
			loadHouseList : loadHouseList,//加载户托管信息
		},
		components : {
			'vue-nav' : PageNavigationVue,
		},
	})

	//------------------------方法定义-开始------------------//
	
	function indexMethod(index) {
		return generalIndexMethod(index, buildingTableVue)
	}
	
	function loadHouseList()
	{
		
		var model = {
				interfaceVersion : 19000101,
				pageNumber:buildingTableVue.pageNumber,
				countPerPage:buildingTableVue.countPerPage,
				totalPage:buildingTableVue.totalPage,
				buildingId : buildingTableVue.buildingId,
		}
		
		new ServerInterface(baseInfo.listInterface).execute(model, function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				buildingTableVue.Tg_DepositHouseholdsDtl_View=jsonObj.tg_DepositHouseholdsDtl_ViewList;
				buildingTableVue.pageNumber=jsonObj.pageNumber;
				buildingTableVue.countPerPage=jsonObj.countPerPage;
				buildingTableVue.totalPage=jsonObj.totalPage;
				buildingTableVue.totalCount = jsonObj.totalCount;
				buildingTableVue.keyword=jsonObj.keyword;
//				buildingTableVue.selectedItem=[];
				// 动态跳转到锚点处，id="top"
//				document.getElementById('tg_DepositManagementListDiv').scrollIntoView();
			}
		});
	}
	
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
		
	}
	
	function getProjectDetail(buildingId)
	{
		$("#tab-1").css("display","none");
		var model = {
				interfaceVersion : 19000101,
				tableId : buildingTableVue.projectId,
		};
		new ServerInterface(baseInfo.projectDetailInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				buildingTableVue.empj_BuildingInfoList = jsonObj.empj_ProjectInfo.empj_BuildingInfoList;
				
				if(buildingId != null) {
					getBuildingDetail(buildingId);
				}else {
					if(buildingTableVue.empj_BuildingInfoList.length > 0)
					{
						var building = buildingTableVue.empj_BuildingInfoList[0];
						getBuildingDetail(building.tableId);
					}
				}
                buildingTableVue.companyId=jsonObj.empj_ProjectInfo.developCompanyId
//                $("#tab-1").css("display","none");
        		$("#tab-1").attr("data-editNum","0302_OST_12");
			}
			
			initButtonList();
			
		});
	}
	
	function getBuildingDetail(buildingId)
	{
		buildingTableVue.clickBuildingId = buildingId;
		buildingTableVue.buildingId = buildingId;
		var model = {
				interfaceVersion : 19000101,
				tableId : buildingId,
		};
		new ServerInterface(baseInfo.buildingDetailInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj);
			}
			else
			{
				buildingTableVue.empj_BuildingInfo = jsonObj.empj_BuildingInfo;
				//土地抵押状态默认为无
				if(buildingTableVue.empj_BuildingInfo.landMortgageState == undefined || buildingTableVue.empj_BuildingInfo.landMortgageState ==null || buildingTableVue.empj_BuildingInfo.landMortgageState == '' )
				{
					buildingTableVue.empj_BuildingInfo.landMortgageState = '0';
				}
				buildingTableVue.empj_BuildingInfo.buildAmountPaid = addThousands(buildingTableVue.empj_BuildingInfo.buildAmountPaid);
				buildingTableVue.empj_BuildingInfo.buildAmountPay = addThousands(buildingTableVue.empj_BuildingInfo.buildAmountPay);
				buildingTableVue.empj_BuildingInfo.buildingArea = addThousands(buildingTableVue.empj_BuildingInfo.buildingArea);
				buildingTableVue.empj_BuildingInfo.escrowArea = addThousands(buildingTableVue.empj_BuildingInfo.escrowArea);
				buildingTableVue.empj_BuildingInfo.recordAvgPriceOfBuilding = addThousands(buildingTableVue.empj_BuildingInfo.recordAvgPriceOfBuilding);
				buildingTableVue.eCodeFromConstruction = jsonObj.empj_BuildingInfo.eCodeFromConstruction;
				buildingTableVue.empj_BuildingTableTemplateList = jsonObj.empj_BuildingInfo.empj_BuildingTableTemplateList;
				buildingTableVue.empj_GarageTableTemplateList = jsonObj.empj_BuildingInfo.empj_GarageTableTemplateList;
				buildingTableVue.erfloorHouseCount = jsonObj.empj_BuildingInfo.erfloorHouseCount;
				buildingTableVue.erfloorGarageCount = jsonObj.empj_BuildingInfo.erfloorGarageCount;
				buildingTableVue.buildingAvgPriceId = jsonObj.empj_BuildingInfo.buildingAvgPriceId;
				buildingTableVue.bldEscrowCompletedDtlId = jsonObj.empj_BuildingInfo.bldEscrowCompletedDtlId;
				console.log(jsonObj.tgpj_BuildingAccount);
				if(jsonObj.tgpj_BuildingAccount!=undefined&&jsonObj.tgpj_BuildingAccount!=null)
				{
					buildingTableVue.tgpj_BuildingAccount = jsonObj.tgpj_BuildingAccount;
					buildingTableVue.tgpj_BuildingAccount.totalAmountGuaranteed = addThousands(buildingTableVue.tgpj_BuildingAccount.totalAmountGuaranteed);
					buildingTableVue.tgpj_BuildingAccount.nodeLimitedAmount = addThousands(buildingTableVue.tgpj_BuildingAccount.nodeLimitedAmount);
					buildingTableVue.tgpj_BuildingAccount.totalAccountAmount = addThousands(buildingTableVue.tgpj_BuildingAccount.totalAccountAmount);
					buildingTableVue.tgpj_BuildingAccount.currentEscrowFund = addThousands(buildingTableVue.tgpj_BuildingAccount.currentEscrowFund);
					buildingTableVue.tgpj_BuildingAccount.applyRefundPayoutAmount = addThousands(buildingTableVue.tgpj_BuildingAccount.applyRefundPayoutAmount);
					buildingTableVue.tgpj_BuildingAccount.cashLimitedAmount = addThousands(buildingTableVue.tgpj_BuildingAccount.cashLimitedAmount);
					buildingTableVue.tgpj_BuildingAccount.spilloverAmount = addThousands(buildingTableVue.tgpj_BuildingAccount.spilloverAmount);
					buildingTableVue.tgpj_BuildingAccount.payoutAmount = addThousands(buildingTableVue.tgpj_BuildingAccount.payoutAmount);
					buildingTableVue.tgpj_BuildingAccount.refundAmount = addThousands(buildingTableVue.tgpj_BuildingAccount.refundAmount);
					buildingTableVue.tgpj_BuildingAccount.orgLimitedAmount = addThousands(buildingTableVue.tgpj_BuildingAccount.orgLimitedAmount);
					buildingTableVue.tgpj_BuildingAccount.paymentProportion = addThousands(buildingTableVue.tgpj_BuildingAccount.paymentProportion);
					buildingTableVue.tgpj_BuildingAccount.effectiveLimitedAmount = addThousands(buildingTableVue.tgpj_BuildingAccount.effectiveLimitedAmount);
					buildingTableVue.tgpj_BuildingAccount.allocableAmount = addThousands(buildingTableVue.tgpj_BuildingAccount.allocableAmount);
					buildingTableVue.tgpj_BuildingAccount.appliedNoPayoutAmount = addThousands(buildingTableVue.tgpj_BuildingAccount.appliedNoPayoutAmount);
				}
				
			}
			
			initButtonList();
			
			loadHouseList();
			
		});
	}
	
	function initData()
	{
		/*$("#tab-1").css("display","none");
		$("#tab-1").attr("data-editNum","0302_OST_12");*/
		initButtonList();
		//0302_OST_12
//		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
//		var array = tableIdStr.split("_");
//		if (array.length == 4) {
//			buildingTableVue.projectId = array[2];
//			getProjectDetail();
//		}
		
		/*getIdFormTab("",function (id) {
			console.log(id)
			buildingTableVue.projectId = id;
			getProjectDetail();
		})*/
		
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var targetId = tableIdStr.split("_")[tableIdStr.split("_").length-1];
		if(targetId.indexOf("-") != -1) {
			buildingTableVue.projectId = targetId.split("-")[0];
			getProjectDetail(targetId.split("-")[1]);
		}else {
			buildingTableVue.projectId = targetId;
			getProjectDetail(null);
		}
		
		
		$(document).on("click",function() {
			$('.house-links').css({
				display:'none',
			});
		});
		
	}
	
	function closeHouseInfo(){
		var dataFlag = $("#buildingTableModal").attr("data-flag");
		if(dataFlag == 1){
			$("#buildingTableModal").modal({
				backdrop :'static'
			});
		}
		$("#house-info").modal('hide');
	}
	
	function clearModal(){
		buildingTableVue.houseConstructioneCode = "";
		buildingTableVue.houseUniteCode = "";
		buildingTableVue.houseRoomId = "";
		buildingTableVue.empj_HouseInfo.position = "";
		buildingTableVue.empj_HouseInfo.actualArea = "";
		buildingTableVue.empj_HouseInfo.floor = "";
		buildingTableVue.empj_HouseInfo.recordPrice = "";
		buildingTableVue.empj_HouseInfo.roomId = "";
		buildingTableVue.empj_HouseInfo.shareConsArea = "";
		buildingTableVue.empj_HouseInfo.purpose = 1;
		buildingTableVue.empj_HouseInfo.unitInfo.theName = "";
		buildingTableVue.empj_HouseInfo.innerconsArea = "";
		buildingTableVue.empj_HouseInfo.busiState = 1;
		buildingTableVue.houseDetailInfoList = [];
		buildingTableVue.houseDetailInfoList = [];
		buildingTableVue.houseLoanAmountList = [];
	}
	
	
	function openHouseModal(tableId)
	{
		
		if(tableId != null)
		{
			clearModal();
			var dataFlag = $("#buildingTableModal").attr("data-flag");
			if(dataFlag == 1){
				$("#buildingTableModal").modal('hide');
			}
			$("#house-info").modal({
				backdrop :'static'
			});
			
			var model = {
					interfaceVersion : 19000101,
					tableId:tableId,
				}
				new ServerInterface(baseInfo.houseDetailInterface).execute(model, function(jsonObj)
				{
					if(jsonObj.result != "success")
					{
						generalErrorModal(jsonObj);
					}
					else
					{
						buildingTableVue.empj_HouseInfo = jsonObj.empj_HouseInfo;
						console.log(buildingTableVue.empj_HouseInfo)
						buildingTableVue.houseConstructioneCode = jsonObj.empj_HouseInfo.eCodeFromConstruction;
						buildingTableVue.houseUniteCode = jsonObj.empj_HouseInfo.eCodeOfUnitInfo;
						buildingTableVue.houseRoomId = jsonObj.empj_HouseInfo.roomId;
						buildingTableVue.houseDetailInfo = jsonObj.empj_HouseInfo;
						buildingTableVue.houseDetailInfoList = [buildingTableVue.houseDetailInfo];
						buildingTableVue.houseLoanAmountList = jsonObj.empj_HouseInfo.houseLoanAmountList;
					}
				});
		}
	}
	
	function openRightModel(tableId, e)
	{
		$('.house-links').css({
			display:'none'
		});
		
		document.oncontextmenu = function(){return false};
		
		$('#div_'+tableId).css({
			display:'block',
			top: e.pageY,
			left: e.pageX,
		});
		
		initButtonList();
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	buildingTableVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#empj_BuildingInfoTableDetailDiv",
	"projectDetailInterface":"../Empj_BuildingTableDetail",
	"buildingDetailInterface":"../Empj_BuildingInfoDetailForTable",
	"houseDetailInterface":"../Empj_HouseInfoDetail",
	"listInterface":"../Tg_DepositHouseholdsDtl_ViewList",//户托管信息查询
});
