(function(baseInfo){
	var detailVue = new Vue({
		el : baseInfo.detailDivId,
		data : {
			interfaceVersion :19000101,
			tgpj_BuildingAvgPriceModel: {
				buildingInfoId:"",
            },
            tgpj_BuildingAvgPriceNew:{},
			tableId : 1,
            companyId:"",
            projectList:[],
            projectId:"",
            buildingList:[],
            buildingId:"",
            company:{},
            projectName:"",
            publicSecurity:"",
            buildingEcode:"",
			constructionId:"",
			userStartId:"",
			operationDay:"",
			// nowBuilding:{
                // eCode:"",
                // eCodeFromConstruction: "",
                // eCodeFromPublicSecurity: "",
                // project: {
                //     theName: "",
                // },
			// }

            //附件材料
			busiType:'03010301',
            dialogVisible: false,
            dialogImageUrl: "",
            fileType:"",
            fileList : [],
            showButton:true,
            hideShow:false,
            uploadData : [],
            smAttachmentList:[],//页面显示已上传的文件
            uploadList:[],
            loadUploadList: [],
            showDelete : true,

            //其他
            errMsg:"", //错误提示信息

            buttonType : "",//按钮来源（保存、提交）

            recordDateDisable:"true",

            presellPrice:"",
            isShowPresell:false,
            showSubFlag : true,
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			getSearchForm : getSearchForm,
			//更新
			getUpdateForm : getUpdateForm,
			update: update,
            buildingChangeHandle:constructionChangeHandle,

            saveAttachment:saveAttachment,
            changeThousands:function(){
                detailVue.tgpj_BuildingAvgPriceNew.recordAveragePrice = addThousands(detailVue.tgpj_BuildingAvgPriceNew.recordAveragePrice);
            },
		},
		computed:{
			 
		},
		components : {
            'vue-select': VueSelect,
            "my-uploadcomponent":fileUpload,
		},
		watch:{
			
		}
	});

	//------------------------方法定义-开始------------------//
	//详情操作--------------获取"机构详情"参数
	function getSearchForm()
	{
		return {
			interfaceVersion:detailVue.interfaceVersion,
			tableId:detailVue.tableId,
		}
	}

	//详情操作--------------
	function refresh()
	{
		if (detailVue.tableId == null || detailVue.tableId < 1) 
		{
			return;
		}
        // serverRequest(baseInfo.projectListInterface,getTotalListForm(19000101),function (jsonObj) {
			// detailVue.projectList=jsonObj.empj_ProjectInfoList
        //     getDetail();
        // })
		getDetail()
		loadUpload()
	}

	function getDetail()
	{
		new ServerInterface(baseInfo.detailInterface).execute(detailVue.getSearchForm(), function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				detailVue.tgpj_BuildingAvgPriceModel = jsonObj.tgpj_BuildingAvgPrice;
				detailVue.tgpj_BuildingAvgPriceModel.recordAveragePrice = addThousands(jsonObj.tgpj_BuildingAvgPrice.recordAveragePrice);
				detailVue.tgpj_BuildingAvgPriceNew=jsonObj.tgpj_BuildingAvgPriceNew
				detailVue.projectId=detailVue.tgpj_BuildingAvgPriceModel.projectId
				detailVue.companyId=detailVue.tgpj_BuildingAvgPriceModel.companyId
				detailVue.userStartId=jsonObj.tgpj_BuildingAvgPrice.userStartId
				detailVue.buildingId=jsonObj.tgpj_BuildingAvgPrice.buildingInfoId
				detailVue.operationDay=jsonObj.tgpj_BuildingAvgPrice.createTimeStamp

                detailVue.projectName = jsonObj.tgpj_BuildingAvgPrice.projectName
                detailVue.publicSecurity = jsonObj.tgpj_BuildingAvgPrice.eCodeFromPublicSecurity
                detailVue.constructionId = jsonObj.tgpj_BuildingAvgPrice.eCodeFromConstruction
                detailVue.buildingEcode = jsonObj.tgpj_BuildingAvgPrice.buildingECode
                // detailVue.nowBuilding = jsonObj.tgpj_BuildingAvgPrice.buildingInfo
                $('#date0301030201').val(jsonObj.tgpj_BuildingAvgPrice.averagePriceRecordDateString)

				// detailVue.tgpj_BuildingAvgPriceNew.remark=detailVue.tgpj_BuildingAvgPriceModel.remark
				detailVue.tgpj_BuildingAvgPriceNew.remark=detailVue.tgpj_BuildingAvgPriceNew.remark
				// detailVue.tgpj_BuildingAvgPriceNew.recordAveragePrice=detailVue.tgpj_BuildingAvgPriceModel.recordAveragePrice
				detailVue.tgpj_BuildingAvgPriceNew.recordAveragePrice=addThousands(detailVue.tgpj_BuildingAvgPriceNew.recordAveragePrice)
				if(jsonObj.tgpj_BuildingAvgPrice.recordAveragePriceFromPresellSystem!=undefined){
                    detailVue.presellPrice=addThousands(jsonObj.tgpj_BuildingAvgPrice.recordAveragePriceFromPresellSystem)
				}
                // getPresellPrice(detailVue.buildingId)

				// if(detailVue.tgpj_BuildingAvgPriceNew.remark==null){//如果remark为空
                 //    detailVue.tgpj_BuildingAvgPriceNew.remark=detailVue.tgpj_BuildingAvgPriceModel.remark
				// }
				// if(detailVue.tgpj_BuildingAvgPriceNew.recordAveragePrice==null){
                 //    detailVue.tgpj_BuildingAvgPriceNew.recordAveragePrice=detailVue.tgpj_BuildingAvgPriceModel.recordAveragePrice
				// }

                getConstructionList()
				if(detailVue.tgpj_BuildingAvgPriceModel.busiState=="未备案"){
                    detailVue.recordDateDisable=false
				}else{
                    detailVue.recordDateDisable=true
				}
			}
		});
	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getUpdateForm()
	{
		return {
            //附件材料
            busiType : detailVue.busiType,
            sourceId : detailVue.tableId,
            generalAttachmentList : this.$refs.listenUploadData.uploadData,

			interfaceVersion:detailVue.interfaceVersion,
			tableId:detailVue.tableId,
			theState:detailVue.tgpj_BuildingAvgPriceModel.theState,
			busiState:detailVue.tgpj_BuildingAvgPriceModel.busiState,
			eCode:detailVue.tgpj_BuildingAvgPriceModel.eCode,
			userStartId:detailVue.tgpj_BuildingAvgPriceModel.userStartId,
			// createTimeStamp:detailVue.tgpj_BuildingAvgPriceModel.createTimeStamp,
			// lastUpdateTimeStamp:detailVue.tgpj_BuildingAvgPriceModel.lastUpdateTimeStamp,
			userRecordId:detailVue.tgpj_BuildingAvgPriceModel.userRecordId,
			// recordTimeStamp:detailVue.tgpj_BuildingAvgPriceModel.recordTimeStamp,
			recordAveragePrice:commafyback(detailVue.tgpj_BuildingAvgPriceNew.recordAveragePrice),
            averagePriceRecordDateString:$('#date0301030201').val(),
			// buildingInfoId:detailVue.nowBuilding.tableId,
			buildingInfoId:detailVue.buildingId,
			// averagePriceRecordDateString:detailVue.tgpj_BuildingAvgPriceModel.averagePriceRecordDateString,
            // averagePriceRecordDateString:$('#registeredDateSelect_edit').val(),
            // recordAveragePriceFromPresellSystem:detailVue.tgpj_BuildingAvgPriceModel.recordAveragePriceFromPresellSystem,
			projectId:detailVue.projectId,
			remark:detailVue.tgpj_BuildingAvgPriceNew.remark,
            buildingInfoString:JSON.stringify({
                eCodeFromConstruction:detailVue.constructionId,
                eCodeFromPublicSecurity:detailVue.publicSecurity,
                eCode:detailVue.buildingEcode,

            }),
            buttonType:detailVue.buttonType,
            busiType : detailVue.busiType,
            sourceId : detailVue.tableId,
		}
	}

	function update(buttonType)
	{
		detailVue.buttonType=buttonType;
		if(buttonType == 2){
			detailVue.showSubFlag = false;
        } 
		/*serverBodyRequest(baseInfo.updateInterface,detailVue.getUpdateForm(),function (jsonObj) {
			detailVue.showSubFlag = true;
            generalSuccessModal()
            enterNewTabCloseCurrent(jsonObj.tableId, "备案均价详情", "tgpj/Tgpj_BuildingAvgPriceDetail.shtml")
        })*/
		
		new ServerInterfaceJsonBody(baseInfo.updateInterface).execute(detailVue.getUpdateForm(), function (jsonObj) {
			detailVue.showSubFlag = true;
	        if (jsonObj.result != "success") {
	        	 generalErrorModal(jsonObj)
	        }
	        else {
	        	generalSuccessModal()
	            enterNewTabCloseCurrent(jsonObj.tableId, "备案均价详情", "tgpj/Tgpj_BuildingAvgPriceDetail.shtml")
	        }
	    });
        
        
		// new ServerInterfaceJsonBody(baseInfo.updateInterface).execute(detailVue.getUpdateForm(), function(jsonObj)
		// {
		// 	if(jsonObj.result != "success")
		// 	{
		// 		generalErrorModal(jsonObj)
		// 		// noty({"text":jsonObj.info,"type":"error","timeout":2000});
		// 	}
		// 	else
		// 	{
		// 		// $(baseInfo.detailDivId).modal('hide');
		// 		generalSuccessModal()
         //        enterNewTabCloseCurrent(jsonObj.tableId, "备案均价详情", "tgpj/Tgpj_BuildingAvgPriceDetail.shtml")
		// 		// refresh();
		// 	}
		// });
	}

    function getConstructionList() {
        serverRequest(baseInfo.constructionListInterface,{interfaceVersion:19000101,tableId:detailVue.companyId},function (jsonObj) {
            detailVue.buildingList=jsonObj.buildingList
            // detailVue.buildingId=jsonObj.tgpj_BuildingAvgPrice.buildingInfoId
            // getDetail()
        })
    }

    function constructionChangeHandle(data) {
        detailVue.buildingId = data.tableId

        // for(var i=0 ;i<detailVue.buildingList.length;i++){
        //    if (data.tableId === detailVue.buildingId) {
        detailVue.projectName = data.projectName
        detailVue.publicSecurity = data.eCodeFromPublicSecurity
        detailVue.constructionId = data.eCodeFromConstruction
        detailVue.buildingEcode = data.eCode
        detailVue.buildingId = data.tableId
        detailVue.operationDay = data.createTimeStamp
        // detailVue.nowBuilding = data
        //
        //    }
        // }
    }

    function loadUpload() {
        generalLoadFile2(detailVue,detailVue.busiType)
    }

    function saveAttachment() {
        generalUploadFile(detailVue,"Tgpj_BuildingAvgPrice",baseInfo.attachmentBatchAddInterface,baseInfo.successModel)
    }

    function getPresellPrice(buildingId) {
        serverRequest("../Empj_BuildingGetPresellPrice",{interfaceVersion:19000101,tableId:buildingId},function (jsonObj) {
            detailVue.presellPrice=addThousands(jsonObj.presellPrice)
        })
    }

	function initData()
	{
        laydate.render({
            elem: '#date0301030201'
        });
        getIdFormTab("",function (id) {
            detailVue.tableId=id
            refresh()
        })
        getLoginUserInfo(function (user) {
            if (user.theType == "1") {
                detailVue.isShowPresell = true
            } else {
                detailVue.isShowPresell = false
            }
        })
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	// detailVue.refresh();
	detailVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"detailDivId":"#tgpj_BuildingAvgPriceEditDiv",
	"detailInterface":"../Tgpj_BuildingAvgPriceDetail",
	"updateInterface":"../Tgpj_BuildingAvgPriceUpdate",
	"projectListInterface":"../Empj_ProjectInfoForSelect",
    "constructionListInterface":"../Emmp_CompanyGetBuildingList",

    //材料附件
    "loadUploadInterface": "../Sm_AttachmentCfgList",
    "attachmentBatchAddInterface": "../Sm_AttachmentBatchAdd",
    "attachmentListInterface": "../Sm_AttachmentList",
    //模态框
    "successModel": "#successM",
    "errorModal": "#errorM",
});
