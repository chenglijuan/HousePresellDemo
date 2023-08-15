(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			tgxy_EscrowAgreementModel: {
				contractApplicationDate:getNowFormatDate(),
				disputeResolution:'1',
				theNameOfProject:'',//交互完改动：[]改为''
				agreementVersion:'',
				OtherAgreedMatters:'',
				theNameOfDevelopCompany:'',//开发企业
				cityRegionName:'',
				escrowCompany:'常州正泰房产居间服务有限公司'
			},
			building: {
				eCode:"",
			    cityRegions:"",
		        theNameOfProject:"",
		        eCodeFromPublicSecurity :"",
			},
			
			Empj_BuildingInfoList:[],
			eCodeFromConstruction:[],
			empj_BuildingInfoList:[],
			eCodeOfBuildingInfo:'',
			eCodeFromConstructions:[],
			//eCodeFromConstructions:'',
			buildingInfoList:[],
			selectItem : [],
			tgxy_EscrowAgreementAddList:[],
			isAllSelected : false,
			getLoadForm:getLoadForm,
			ProjectNames:[],
			verMngId:'',
			buildingId:[],
			projectId:"",
			errEscrowAdd:'',
			loadUploadList: [],
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
			initData: initData,
			getSearchForm : getSearchForm,
			//添加
			getAddForm : getAddForm,
			add: add,
			loadForm:loadForm, //form表单更新数据
			getEscrowForm:getEscrowForm,
			checkCheckBox:checkCheckBox,
			checkAllClicked:checkAllClicked,
			gettimeForm :gettimeForm ,
//			getNowFormatDate:getNowFormatDate,
			projectChange : projectChange,
			getmesForm:getmesForm,
			errClose:errClose,
			succClose: succClose,
			indexMethod:indexMethod,
			getUploadForm : getUploadForm,
			loadUpload : loadUpload,
			getProject: function (data){
				this.tgxy_EscrowAgreementModel.theNameOfProject = data.tableId;
				detailVue.projectChange();
			},
			getDisputeResolution : function(data){
				this.tgxy_EscrowAgreementModel.disputeResolution = data.tableId;
			},
			emptyDisputeResolution : function(){
				this.tgxy_EscrowAgreementModel.disputeResolution = "";
			},
			bldLimitAmountDetail : bldLimitAmountDetail,//跳转受限额度版本
			
		},
		computed:{
			 
		},
		components : {
			"my-uploadcomponent":fileUpload,
			'vue-select': VueSelect,
		},
		watch:{
			selectItem : selectedItemChanged,
		}
	});

	//------------------------方法定义-开始------------------//

	function bldLimitAmountDetail(tableId){
//		enterNextTab(tableId, '受限额度节点详情', 'tgpj/Tgpj_BldLimitAmountVer_AFDetail.shtml',tableId+"06110201");
		enterNewTab(tableId, "受限额度节点详情", "tgpj/Tgpj_BldLimitAmountVer_AFDetail.shtml")
	}
	
	  function indexMethod(index) {
		return generalIndexMethod(index, detailVue)
	}
	//详情操作--------------获取"机构详情"参数
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
	function checkCheckBox(tableId){
		detailVue.selectItem  = tableId;
	}

	
	
		//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		detailVue.isAllSelected = (detailVue.tgxy_EscrowAgreementAddList.length > 0)
		&&	(detailVue.selectItem.length == detailVue.tgxy_EscrowAgreementAddList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(detailVue.selectItem.length == detailVue.tgxy_EscrowAgreementAddList.length)
	    {
	    	detailVue.selectItem = [];
	    }
	    else
	    {
	    	detailVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	detailVue.tgxy_EscrowAgreementAddList.forEach(function(item) {
	    		detailVue.selectItem.push(item.tableId);
	    	});
	    }
	}

	function getLoadForm(){
		return {
			interfaceVersion:this.interfaceVersion,
			busiState:'已备案',
		}
	}
	function gettimeForm(){
		return {
			interfaceVersion:this.interfaceVersion,
			contractApplicationDate:detailVue.tgxy_EscrowAgreementModel.contractApplicationDate,
		}
	}
		//获得信息
	function getEscrowForm(){
		return{
			interfaceVersion:this.interfaceVersion,
			projectId:detailVue.tgxy_EscrowAgreementModel.theNameOfProject,
		}
	}
	function getmesForm(){
		return{
			interfaceVersion:this.interfaceVersion,
			tableId:detailVue.tgxy_EscrowAgreementModel.theNameOfProject,
		}
	}
	function loadForm(){
		//加载项目名称
		new ServerInterface(baseInfo.loadInterface).execute(detailVue.getLoadForm(), function(jsonObj)
			{
				if(jsonObj.result != "success")
				{
					detailVue.errEscrowAdd = jsonObj.info;
		                $(baseInfo.errorModel).modal('show', {
						    backdrop :'static'
					    });
				}
				else
				{					
					detailVue.ProjectNames = jsonObj.empj_ProjectInfoList;
					
			}
		});
		//时间改变加载出对应的楼幢和项目信息
		timeschange();		
	}
	function timeschange(){
		new ServerInterface(baseInfo.timesInterface).execute(detailVue.gettimeForm(), function(jsonObj)
					{
						if(jsonObj.result != "success")
						{
							detailVue.errEscrowAdd = jsonObj.info;
				                $(baseInfo.errorModel).modal('show', {
								    backdrop :'static'
							    });
						}
						else
						{
							if(jsonObj.tgxy_CoopAgreementVerMngList != undefined) {
								
							}
							detailVue.tgxy_EscrowAgreementModel.agreementVersion = jsonObj.tgxy_CoopAgreementVerMngList[0].theVersion;
							detailVue.verMngId =jsonObj.tgxy_CoopAgreementVerMngList[0].tableId;
							
						}
					});
		
	}
	//根据选择的项目信息加载楼幢信息
	function projectChange(){
		
		//展示开发企业名称和所属区域
			new ServerInterface(baseInfo.mesInterface).execute(detailVue.getmesForm(), function(jsonObj)
					{
						if(jsonObj.result != "success")
						{
							detailVue.errEscrowAdd = jsonObj.info;
				                $(baseInfo.errorModel).modal('show', {
								    backdrop :'static'
							    });
						}
						else
						{
						detailVue.tgxy_EscrowAgreementModel.cityRegionName=jsonObj.empj_ProjectInfo.cityRegionName;
							detailVue.tgxy_EscrowAgreementModel.theNameOfDevelopCompany=jsonObj.empj_ProjectInfo.developCompanyName;
							
						}
					});
				//楼幢信息
		new ServerInterface(baseInfo.escrowInterface).execute(detailVue.getEscrowForm(), function(jsonObj)
						{
							if(jsonObj.result != "success")
							{
									detailVue.errEscrowAdd = jsonObj.info;
					                $(baseInfo.errorModel).modal('show', {
									    backdrop :'static'
								    });
							}
							else
							{
								detailVue.tgxy_EscrowAgreementAddList = jsonObj.empj_BuildingInfoList;
								
							}
						});
	}
	//详情更新操作--------------获取"更新机构详情"参数
	function getAddForm()
	{
//获取选中的楼幢
		this.chooseArr = [];
		detailVue.selectItem.forEach(function(item){
			detailVue.chooseArr.push(item.tableId);
		})
		var fileUploadList = detailVue.$refs.listenUploadData.uploadData;
		fileUploadList = JSON.stringify(fileUploadList);
		return{
			interfaceVersion:this.interfaceVersion,
			escrowCompany:'常州正泰房产居间服务有限公司',
			userStartId:'1',
			contractApplicationDate:this.tgxy_EscrowAgreementModel.contractApplicationDate,
			agreementVersion:detailVue.tgxy_EscrowAgreementModel.agreementVersion,
			OtherAgreedMatters:this.tgxy_EscrowAgreementModel.OtherAgreedMatters,
			disputeResolution:this.tgxy_EscrowAgreementModel.disputeResolution,
			projectId:detailVue.tgxy_EscrowAgreementModel.theNameOfProject,
			idArr:this.chooseArr,
			smAttachmentList:fileUploadList
		}

	}
	//加载签约申请日期
	laydate.render({
	  elem: '#dete0611020102',
	  max: getNowFormatDate(),
	  done: function(value, date){
	    detailVue.tgxy_EscrowAgreementModel.contractApplicationDate=value;
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


	function errClose()
	{
		$(baseInfo.errorModel).modal('hide');
	}
	
	function succClose()
	{
		$(baseInfo.successModel).modal('hide');
	}
	function add()
	{
		new ServerInterface(baseInfo.addInterface).execute(detailVue.getAddForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				detailVue.errEscrowAdd = jsonObj.info;
                $(baseInfo.errorModel).modal('show', {
				    backdrop :'static'
			    });
			}
			else
			{
				enterNext2Tab(jsonObj.tableId, '贷款托管合作协议详情', 'tgxy/Tgxy_EscrowAgreementDetail.shtml',jsonObj.tableId+"06110201");
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
	detailVue.initData();
	detailVue.loadForm();
	detailVue.loadUpload();
	//------------------------数据初始化-结束----------------//
})({
	"addDivId":"#tgxy_EscrowAgreementAddDiv",
	"errorModel":"#errorEscrowAdd1",
	"successModel":"#successEscrowAdd1",
	"detailInterface":"../Tgxy_EscrowAgreementDetail",
	"escrowInterface":"../Empj_BuildingInfoByEscList",
	"loadInterface":"../Empj_ProjectInfoList",
	"timesInterface":"../Tgxy_CoopAgreementVerMngList",
	"addInterface":"../Tgxy_EscrowAgreementAdd",
    "mesInterface":"../Empj_ProjectInfoDetail",
	"loadUploadInterface":"../Sm_AttachmentCfgList",
});

