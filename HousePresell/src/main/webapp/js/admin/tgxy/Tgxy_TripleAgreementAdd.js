(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			beianno:'',
			tgxy_TripleAgreementModel: {
				roomlocation:"",
				eCodeFromPublicSecurity:'',
				cmr:'',
				
			},	
			position : "",
			printMethod : '0',//打印方式
			ProjectNames:[],
			unitRooms:[],
			Constructions:[],
			tgxy_TripleAgreementAddList:[],
			mesmsr:[],
			errtips:'',
			projectNameforht:'',
			projectNameforhtid:'',
			loadUploadList: [],
            showDelete : true,
            busiType : '06110301',
            flag:true,
            indexSel:0,
            fkfs: "",
            firstPayment:0,
            isContract:false,//是否是合同导入
            fkfsList : [
            	{tableId:"1",theName:"一次性付款"},
            	{tableId:"2",theName:"分期付款"},
            	{tableId:"3",theName:"贷款方式付款"},
            	{tableId:"4",theName:"其他方式"}
            ],
            printMethodList : [
            	{tableId:"0",theName:"机打"},
            	{tableId:"1",theName:"手工打印"},
            ],
            jfrq : ""
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
			projectChange:projectChange,
			getbuildingDetailForm:getbuildingDetailForm,
			gethouseForm:gethouseForm,
			gethouseDetaiForm:gethouseDetaiForm,
			ontractDetail:ontractDetail,
			getontractDetailForm:getontractDetailForm,
			getEscrowForm:getEscrowForm,
			buildingChange:buildingChange ,
			getmesFormhandle:getmesFormhandle,
			houseChange:houseChange,
			getUploadForm : getUploadForm,
			loadUpload : loadUpload,
			getProjectId : function(data){
				this.tgxy_TripleAgreementModel.projectid = data.tableId;
				detailVue.projectChange();
			},
			getfkfs : function(data){
				this.fkfs = data.tableId;
			},
			getPrintMethod : function(data){
				detailVue.printMethod = data.tableId;
			},
			
			/*getProject: function (data){
				this.tgxy_TripleAgreementModel.projectid = data.tableId;
				detailVue.projectChange();
			},
			getConstructions: function(data) {
				this.tgxy_TripleAgreementModel.buildingid = data.tableId;
				detailVue.buildingChange();
			},
			getUnitRooms: function(data) {
				this.tgxy_TripleAgreementModel.roomid = data.tableId;
				detailVue.houseChange();
			}*/
			
		},
		computed:{
			 
		},
		components : {
			"my-uploadcomponent":fileUpload,
			'vue-select': VueSelect,
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
		
	}
	function loadform(){
		projectName(); //如果不成功则需手书，查出项目列表		
	}
	function getontractDetailForm(){
		return{
			interfaceVersion:this.interfaceVersion,
			beianno:detailVue.beianno,//合同备案号
		}
	}
	//根据合同备案号查询合同信息
	function ontractDetail(){
		
		if(detailVue.beianno==''||detailVue.beianno==null){
			//合同备案号为空时，不进行请求
		}else{
		
			detailVue.flag = false;
			
			new ServerInterface(baseInfo.ontractDetailInterface).execute(detailVue.getontractDetailForm(), function(jsonObj)
			{
				if(jsonObj.result != "success")
				{
					detailVue.errtips = jsonObj.info;
					$(baseInfo.errorModel).modal('show', {
						backdrop :'static'
					 });
					detailVue.indexSel = 0;
					detailVue.tgxy_TripleAgreementModel.projectid = null;
					detailVue.tgxy_TripleAgreementModel.buildingid = null;
					detailVue.tgxy_TripleAgreementModel.roomid = null;
					detailVue.Constructions = [];
					detailVue.unitRooms = [];
					
					projectName();
					var length= $('#tgxy_TripleAgreementAdddiv input').length;
					for(i=0;i< length;i++){
						if(i>0){
							$($('#tgxy_TripleAgreementAdddiv input')[i]).removeAttr("disabled");
						}
					}
					detailVue.tgxy_TripleAgreementModel.cmr = "";
					detailVue.tgxy_TripleAgreementModel.roomlocation = "";
					detailVue.fkfs = "";
					detailVue.tgxy_TripleAgreementModel.msr = "";
					detailVue.tgxy_TripleAgreementModel.contractprice = "";
//					detailVue.tgxy_TripleAgreementModel.qdtime = "";
					detailVue.tgxy_TripleAgreementModel.eCodeFromPublicSecurity = "";
					detailVue.tgxy_TripleAgreementModel.mj = "";
//					detailVue.tgxy_TripleAgreementModel.jfrq = "";
					
					detailVue.tgxy_TripleAgreementModel.beianno = detailVue.beianno;
					console.log(detailVue.tgxy_TripleAgreementModel.qdtime);
				}
				else
				{
					
					detailVue.position = jsonObj.tb_b_contract.roomlocation;
					detailVue.isContract = true;
					detailVue.indexSel = 1;
					detailVue.tgxy_TripleAgreementModel = jsonObj.tb_b_contract;
					detailVue.fkfs = jsonObj.tb_b_contract.fkfs;
					detailVue.firstPayment = jsonObj.tb_b_contract.sfk;
					detailVue.jfrq = jsonObj.tb_b_contract.jfrq;
//					detailVue.beianno=jsonObj.tb_b_contract.htbh;//刷新合同备案编号
					
					if(jsonObj.buyerList!==(''||undefined)){
						$('.buyerlist').removeClass('hidden');
						detailVue.tgxy_TripleAgreementAddList= jsonObj.buyerList;//获取买受人信息	
						detailVue.tgxy_TripleAgreementAddList.forEach(function(item,index){
							detailVue.tgxy_TripleAgreementAddList[index].certificateTypeName="身份证";
							detailVue.tgxy_TripleAgreementAddList[index].agentCertTypeName="身份证";
						})
					}else{
						$('.buyerlist').addClass('hidden');
					}
					var length= $('#tgxy_TripleAgreementAdddiv input').length;
					for(i=0;i< length;i++){
						if(i>0){
							$($('#tgxy_TripleAgreementAdddiv input')[i]).attr("disabled","disabled");
						}
						
					}
					var length_select= $('#tgxy_TripleAgreementAdddiv select').length;
					for(i=0;i< length_select;i++){
						$($('#tgxy_TripleAgreementAdddiv select')[i]).attr("disabled","disabled");
					}
		
					if(detailVue.flag) {
						projectName();
					}
					console.log("1");
			  	    projectChange();
					buildingChange();
					houseChange();
		
				}
			});
		
		}
	}
	function getprojectNameForm(){
		return{
			interfaceVersion:this.interfaceVersion,
		}
	}
	//根据开发企业id查出项目下拉列表内容
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
				detailVue.ProjectNames ='';
				detailVue.ProjectNames = jsonObj.empj_ProjectInfoList;
			}
		});
	 }
	 function getmesFormhandle(){
		 	return {
		 		interfaceVersion:this.interfaceVersion,
		 		tableId:detailVue.tgxy_TripleAgreementModel.projectid,
		 	}
		 }
	  function getEscrowForm(){
	 	return {
	 		interfaceVersion:this.interfaceVersion,	 		
	 		projectId:this.tgxy_TripleAgreementModel.projectid,
	 	}
	 }
	  //项目改变带动开发企业名称和所属区域还有楼幢列表的变化（）
	 function projectChange(){
		 detailVue.tgxy_TripleAgreementModel.cmr='';
		 detailVue.Constructions =[];
		 detailVue.tgxy_TripleAgreementModel.eCodeFromPublicSecurity = '';
		 detailVue.tgxy_TripleAgreementModel.roomlocation = '';
		 detailVue.unitRooms =[];
	 	//展示开发企业名称和所属区域（根据项目Id查询项目详情：）
			new ServerInterface(baseInfo.mesInterface).execute(detailVue.getmesFormhandle(), function(jsonObj)
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
							if(detailVue.indexSel == 1) {
								detailVue.ProjectNames = jsonObj.empj_ProjectInfo;
								//detailVue.tgxy_TripleAgreementModel.projectid = jsonObj.empj_ProjectInfo.theName;
							}
						detailVue.tgxy_TripleAgreementModel.cmr=jsonObj.empj_ProjectInfo.developCompanyName;//开发企业名称
							
						}
					});
				//楼幢列表（根据选择的项目Id加载楼幢信息）
			new ServerInterface(baseInfo.escrowInterface).execute(detailVue.getEscrowForm(), function(jsonObj)
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
								detailVue.Constructions = jsonObj.empj_BuildingInfoList;
							}
						});
	 }
	 function getbuildingDetailForm(){
	 	return {
	 		interfaceVersion:this.interfaceVersion,	 		
	 		tableId:this.tgxy_TripleAgreementModel.buildingid,
	 	}
	 }
	  function gethouseForm(){
	 	return {
	 		interfaceVersion:this.interfaceVersion,	 		
	 		buildingId:this.tgxy_TripleAgreementModel.buildingid,//选择的楼幢主键

	 	}
	 }
	  
	 //楼幢信息改变带出户信息
	 function buildingChange(){
		 detailVue.tgxy_TripleAgreementModel.eCodeFromPublicSecurity = '';
//		 detailVue.tgxy_TripleAgreementModel.roomlocation = '';
		 detailVue.unitRooms =[];
	 	//根据选择的楼幢Id加载楼幢详情
	 	new ServerInterface(baseInfo.buildingDetailInterface).execute(detailVue.getbuildingDetailForm(), function(jsonObj)
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
								detailVue.tgxy_TripleAgreementModel.eCodeFromPublicSecurity = jsonObj.empj_BuildingInfo.eCodeFromPublicSecurity;
//								if(!detailVue.isContract){
//									detailVue.tgxy_TripleAgreementModel.roomlocation = jsonObj.empj_BuildingInfo.position;
//								}
								
							}
						});
		//根据选择的楼幢Id加载户室列表
		new ServerInterface(baseInfo.houseInterface).execute(detailVue.gethouseForm(), function(jsonObj)
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
								
								detailVue.unitRooms = jsonObj.empj_HouseInfoList;
								
							}
						});
	 }
	 function gethouseDetaiForm(){
	 	return {
	 		interfaceVersion:this.interfaceVersion,	 		
	 		tableId:this.tgxy_TripleAgreementModel.roomid,//选择的室号Id
			
	 	}
	 }
//根据户号加载加载室号详情（单元）根据户室主键或户室与中间库关联外键查询户室详细信息
	function houseChange(){
		if(detailVue.isContract){
			detailVue.tgxy_TripleAgreementModel.roomlocation='';
		}
	 	new ServerInterface(baseInfo.houseDetaiInterface).execute(detailVue.gethouseDetaiForm(), function(jsonObj)
						{
							if(jsonObj.result != "success")
							{
								generalErrorModal(jsonObj);
							}
							else
							{
								detailVue.tgxy_TripleAgreementModel.roomlocation = jsonObj.empj_HouseInfo.position;
							}
						});
	 }
	//新增中 保存
	function getAddForm()
	{
		var fileUploadList = detailVue.$refs.listenUploadData.uploadData;
		fileUploadList = JSON.stringify(fileUploadList);
		this.tgxy_TripleAgreementAddList.forEach(function(item){
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
		
		if(undefined == detailVue.position || null == detailVue.position || "" == detailVue.position){
			detailVue.position = detailVue.tgxy_TripleAgreementModel.roomlocation;
		}
		

		return {
			interfaceVersion:this.interfaceVersion,
//			eCodeOfContractRecord:this.beianno,
			eCodeOfContractRecord:this.tgxy_TripleAgreementModel.beianno,//备案合同编号
			sellerName:this.tgxy_TripleAgreementModel.cmr,
			buyerName:this.tgxy_TripleAgreementModel.msr,
			userStartId:1,//创建人id
			escrowCompany:'常州正泰房产居间服务有限公司',
			projectId:this.tgxy_TripleAgreementModel.projectid,//项目Id
			buildingInfoId:this.tgxy_TripleAgreementModel.buildingid,//楼幢Id
			houseId:this.tgxy_TripleAgreementModel.roomid,//户室Id
			contractSumPrice:this.tgxy_TripleAgreementModel.contractprice,//合同总价
			buildingArea:this.tgxy_TripleAgreementModel.mj,//建筑面积
			//position:this.tgxy_TripleAgreementModel.roomlocation,
			position:detailVue.position,//房屋坐落
			contractSignDate:this.tgxy_TripleAgreementModel.qdtime,//合同签订日期
			paymentMethod:this.fkfs,//付款方式
			syncPerson:this.tgxy_TripleAgreementModel.jfrq,//交付日期
			eCodeOfBuilding:this.tgxy_TripleAgreementModel.htbh,//备案系统楼幢编号
			firstPayment:this.firstPayment,
			printMethod : detailVue.printMethod,
			payDate : detailVue.jfrq,
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
	laydate.render({
	  elem: '#tgxy_TripleAgreementAddDate1',
	});
	laydate.render({
	  elem: '#tgxy_TripleAgreementAddDate2',
	});
	
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
	detailVue.loadform();
	detailVue.refresh();
	detailVue.initData();
	detailVue.loadUpload();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#tgxy_TripleAgreementAddDiv",
	"errorModel":"#errorTripleAdd",
	"successModel":"#successTripleAdd",
	"addInterface":"../Tgxy_TripleAgreementAdd",
	"projectNameInterface":"../Empj_ProjectInfoList",//加载项目列表
	"mesInterface":"../Empj_ProjectInfoByDetail",//根据项目主键或项目与中间库关联外键查询项目详细信息
	"escrowInterface":"../Empj_BuildingInfoByList",//根据选择的项目Id加载楼幢列表
	"buildingDetailInterface":"../Empj_BuildingInfoByDetail",//根据楼幢主键或楼幢与中间库关联外键查询楼幢详细信息
	"houseInterface":"../Empj_HouseInfoByEscList",//根据选择的楼幢Id加载户室列表：
	"houseDetaiInterface":"../Empj_HouseInfoByDetail",//根据选择的室号Id加载室号详情
	"ontractDetailInterface":"../Tb_b_contractDetail",//根据合同备案号查询合同信息
	"loadUploadInterface":"../Sm_AttachmentCfgList",
});
