(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			tgxy_TripleAgreement: {},
			tgxy_TripleAgreementModel: {
				roomlocation:"",
				eCodeFromPublicSecurity:'',
				cmr:'',
				
			},	
			buyerList:[],
			mesmsr:[],
			errtips:'',
			projectNameforht:'',
			projectNameforhtid:'',
			loadUploadList: [],
            showDelete : true,
            busiType : '06110301',
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			indexMethod:indexMethod,
			getSearchForm : getSearchForm,
			changePageNumber : function(data){
				detailVue.pageNumber = data;
			},
			//添加
			getAddForm : getAddForm,
			add: add,
			loadform:loadform,
			getprojectNameForm:getprojectNameForm,
			getUploadForm : getUploadForm,
			loadUpload : loadUpload,
			
		},
		computed:{
			 
		},
		components : {
			"my-uploadcomponent":fileUpload
		},
		watch:{
			
		}
	});

	//------------------------方法定义-开始------------------//
	//详情操作--------------获取"机构详情"参数
	 function indexMethod(index) {
		return generalIndexMethod(index, detailVue)
	}

	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
		}
	}

	//详情操作--------------
	function refresh()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var list = tableIdStr.split("_");
		tableIdStr = list[2];
		detailVue.tableId = tableIdStr;
	}
	function loadform(){
		projectName(); //如果不成功则需手书，查出项目列表		
	}
	function getprojectNameForm(){
		return{
			interfaceVersion:this.interfaceVersion,
			roomid: detailVue.tableId,
		}
	}
	
	 function projectName(){
	 	new ServerInterface(baseInfo.projectNameInterface).execute(detailVue.getprojectNameForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				detailVue.errtips = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
					backdrop :'static'
				 });
			}
			else
			{
				detailVue.tgxy_TripleAgreement = jsonObj.tgxy_TripleAgreement;
				detailVue.buyerList = jsonObj.buyerList;
				if(null != detailVue.buyerList && detailVue.buyerList != undefined)
				{
					detailVue.buyerList.forEach(function(item,index){
						detailVue.buyerList[index].certificateTypeName="身份证";
						detailVue.buyerList[index].agentCertTypeName="身份证";
					})
				}
				
			}
		});
	 }
	//新增中 保存
	function getAddForm()
	{
		var fileUploadList = detailVue.$refs.listenUploadData.uploadData;
		fileUploadList = JSON.stringify(fileUploadList);
		this.buyerList.forEach(function(item){
		        	var modal = {
	        			buyerName:item.buyerName,
	        			buyerType:item.buyerType,
	        			certificateType:item.certificateType,
	        			eCodeOfcertificate:item.eCodeOfcertificate,
	        			contactPhone:item.contactPhone,
	        			contactAdress:item.contactAdress,
	        			agentName:item.agentName,
	        			agentCertType:item.agentCertType,
	        			agentCertNumber:item.agentCertNumber,
	        			agentPhone:item.agentPhone,
	        			agentAddress:item.agentAddress,
		        	}
		    		detailVue.mesmsr.push(modal);
		        })
		var buyerlist = this.mesmsr;		
		buyerlist = JSON.stringify(buyerlist);

		return {
			interfaceVersion:this.interfaceVersion,
			eCodeOfContractRecord:this.tgxy_TripleAgreement.eCodeOfContractRecord,
			//tripleAgreementTimeStamp:getNowFormatDate(),//协议日期默认当天
			sellerName:this.tgxy_TripleAgreement.sellerName,
			buyerName:this.tgxy_TripleAgreement.buyerName,
			userStartId:1,//创建人id
			escrowCompany:'常州正泰房产居间服务有限公司',
			projectId:this.tgxy_TripleAgreement.projectId,//项目Id
			buildingInfoId:this.tgxy_TripleAgreement.buildingInfoId,//楼幢Id
			houseId:this.tgxy_TripleAgreement.houseId,//户室Id
			contractSumPrice:this.tgxy_TripleAgreement.contractSumPrice,//合同总价
			buildingArea:this.tgxy_TripleAgreement.buildingArea,//建筑面积
			position:this.tgxy_TripleAgreement.position,//房屋坐落
			contractSignDate:this.tgxy_TripleAgreement.contractSignDate,//合同签订日期
			paymentMethod:this.tgxy_TripleAgreement.paymentMethod,//付款方式
			syncPerson:this.tgxy_TripleAgreement.payDate,//交付日期
			eCodeOfBuilding:this.tgxy_TripleAgreement.eCodeOfBuilding,//备案系统楼幢编号
			//买受人信息
			smAttachmentList:fileUploadList,
			buyerlist:buyerlist,
			
		}
	}

	//保存
	function add()
	{
		
		new ServerInterface(baseInfo.addInterface).execute(detailVue.getAddForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				detailVue.errtips = jsonObj.info;
				$(baseInfo.errorModel).modal('show', {
					backdrop :'static'
				 });
				 
			}
			else
			{
				enterNext2Tab(jsonObj.tableId, '贷款托管三方协议详情', 'tgxy/Tgxy_TripleAgreementDetail.shtml',jsonObj.tableId+"06110301");
				refresh();
			}
		});
	}
	
	//列表操作-----------------------获取附件参数
	function getUploadForm(){
		return{
			pageNumber : '0',
			busiType : detailVue.busiType,
			interfaceVersion:this.interfaceVersion
		}
	}
	
	//列表操作-----------------------页面加载显示附件类型
	function loadUpload(){
		new ServerInterface(baseInfo.loadUploadInterface).execute(detailVue.getUploadForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				$(baseInfo.errorModel).modal("show",{
					backdrop :'static'
				});
				detailVue.errMsg = jsonObj.info;
			}
			else
			{
				detailVue.loadUploadList = jsonObj.sm_AttachmentCfgList;
			}
		});
	}
	function initData()
	{
		
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	detailVue.refresh();
	detailVue.loadform();
	detailVue.loadUpload();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#tgxy_TripleAgreementDiv10",
	"errorModel":"#errorTripleAdd1",
	"successModel":"#successTripleAdd1",
	"addInterface":"../Tgxy_TripleAgreementAdd",
	"projectNameInterface":"../Tgxy_RoomJumpTriple",//加载项目列表
	"loadUploadInterface":"../Sm_AttachmentCfgList",
});
