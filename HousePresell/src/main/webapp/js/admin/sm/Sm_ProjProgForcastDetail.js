(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			empj_ProjProgForcastModel: {},
			tableId : 1,
            //附件材料
			busiType:'03030202',
            dialogVisible: false,
            dialogImageUrl: "",
            fileType:"",
            fileList : [],
            showButton:false,
            hideShow:true,
            uploadData : [],
            smAttachmentList:[],
            uploadList:[],
            showDelete : false,
            errMsg:"",
            buildingId:"",
            trusteeshipContent:"",
            showPrintBtn : false,
            loadUploadList: [],
            showDelete : false,
            showSubmit : true,
            buildingList : [],
            editFile : false,
            saveFile : false,
            
            //审批流========
            
            //是否需要备案
            isNeedBackup:"",
            
		},
		
		methods : {
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
            openFileModel : openFileModel,
            //审批流
            showModal: showModal,
            approvalHandle: approvalHandle,
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
	
	function getSearchForm()
	{
		return {
            interfaceVersion:detailVue.interfaceVersion,
            tableId:detailVue.tableId,
        }
	}
	
	
	function refresh()
	{
		getDetail();
	}

	function getDetail()
	{
		serverRequest(baseInfo.detailInterface,detailVue.getSearchForm(),function (jsonObj) {
			//主表数据
            detailVue.empj_ProjProgForcastModel = jsonObj.empj_PaymentBond_AF;
            
            //子表数据
            detailVue.buildingList = jsonObj.empj_PaymentBond_DTLList;
            
            detailVue.trusteeshipContent=jsonObj.trusteeshipContent
            if(detailVue.tableId==1){
                detailVue.tableId=detailVue.empj_ProjProgForcastModel.tableId
			}
			if(detailVue.empj_ProjProgForcastModel.approvalState == "待提交"){
				detailVue.editFile = true;
			}
        })
	}

  	/**
     * 点击附件信息打开模态框
     * @author  yaojianping
     * @returns
     */
    function openFileModel(bulidInfo){
    	detailVue.buildAddFile = bulidInfo;
    	detailVue.loadUploadList = bulidInfo.attachementList;
    	
    	$("#fileNodeModelDetail").modal({
    		backdrop :'static',
    		keyboard: false
    	});
    }
  	
    function showModal() {
        approvalModalVue.getModalWorkflowList();
    }
    
    function initData()
	{
		var tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
		var array = tableIdStr.split("_");
		
		if (array.length > 1)
		{
            detailVue.tableId = array[array.length-4];
            detailVue.afId = array[array.length-3];
            detailVue.workflowId = array[array.length-2];
            detailVue.sourcePage = array[array.length-1];

            approvalModalVue.afId = detailVue.afId;
            approvalModalVue.workflowId = detailVue.workflowId;
            approvalModalVue.sourcePage = detailVue.sourcePage;
			refresh();
		}
	}
    
    function approvalHandle () {
        approvalModalVue.buttonType = 2;
        approvalModalVue.approvalHandle();
    }
    
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	
	detailVue.initData();
	
	//------------------------数据初始化-结束----------------//
})({
	
	"detailDivId":"#sm_ProjProgForcastDetailDiv",
	"detailInterface":"../Empj_ProjProgForcast_AFDetail",
    //模态框
    "errorModal": "#errorM",
    
});
