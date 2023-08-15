(function(baseInfo){
	var listVue = new Vue({
		el : baseInfo.listDivId,
		data : {
			interfaceVersion :19000101,
			pageNumber : 1,
			countPerPage : 20,
			totalPage : 1,
			totalCount : 1,
			keyword : "",
			selectItem : [],
			isAllSelected : false,
			theState:0,//正常为0，删除为1
				userStartId:null,
			userStartList:[],
			userRecordId:null,
			userRecordList:[],
			projectId:null,
			tg_AreaList: [],
			areaId: "",
			projectList:[],
			buildingId:null,
			buildingList:[],
			buildingUnitId:null,
			buildingUnitList:[],
			bankId:null,
			bankList:[],
			tgpf_ManagedProjectAnalyseList:[],
			importUrl:"",
			deleCodes:[],
			tableData6: [],
			quarterId : "",//季度Id
			year : "",//年份
			monthId : "",//月份Id
			cache : [], //储蓄一条数据
		    cacheIndex : [], //位置
		    cacheData  : [],
			managedProjectList:[
		      {
		      	prop:"index",
		      	label:'序号',
		      },
		      {
			    prop:'cityRegion', //
			    label:'区域',
			  },
			  {
			    prop:'busiKind',
			    label:'业务类型',
				},
			  {
			    prop:'escrowArea',
			    label:'已签约托管面积（㎡）',
			    headerAlign : "center",
			    align : "right"
				},
			  {
			    prop:'escrowAreaRatio',
			    label:'已签约托管面积区域占比（%）',
			    headerAlign : "center",
			    align : "right"
			  }, {
			    prop:'preEscrowArea',
			    label:'已预售托管面积（㎡）',
			    headerAlign : "center",
			    align : "right"
			  },{
			    prop:'preEscrowAreaRatio',
			    label:'已预售托管面积区域占比（%）',
			    headerAlign : "center",
			    align : "right"
			  },
			],
			quarterList : [
				{tableId : 1,theName:"第一季度"},
				{tableId : 2,theName:"第二季度"},
				{tableId : 3,theName:"第三季度"},
				{tableId : 4,theName:"第四季度"},
			],
			monthArrList : [
				{tableId : 1,momthList:[{tableId:"01",theName:"一月"},{tableId:"02",theName:"二月"},{tableId:"03",theName:"三月"}]},
				{tableId : 2,momthList:[{tableId:"04",theName:"四月"},{tableId:"05",theName:"五月"},{tableId:"06",theName:"六月"}]},
				{tableId : 3,momthList:[{tableId:"07",theName:"七月"},{tableId:"08",theName:"八月"},{tableId:"09",theName:"九月"}]},
				{tableId : 4,momthList:[{tableId:"10",theName:"十月"},{tableId:"11",theName:"十一月"},{tableId:"12",theName:"十二月"}]},
			],
			momthList : [
				{tableId:"01",theName:"一月"},{tableId:"02",theName:"二月"},{tableId:"03",theName:"三月"},{tableId:"04",theName:"四月"},{tableId:"05",theName:"五月"},{tableId:"06",theName:"六月"},
				{tableId:"07",theName:"七月"},{tableId:"08",theName:"八月"},{tableId:"09",theName:"九月"},{tableId:"10",theName:"十月"},{tableId:"11",theName:"十一月"},{tableId:"12",theName:"十二月"}
			],
			quarterDisabled : true,
			monthDisabled : true,
			showCanChoice : 0,
		},
		methods : {
			refresh : refresh,
			initData:initData,
			handleSelectionChange : handleSelectionChange,
			getSearchForm : getSearchForm,
			getExcelDataForm : getExcelDataForm,
			loadProject : loadProject, //页面加载显示区域
			getExportForm : getExportForm,
			search:search,
			changePageNumber : function(data){
				if (listVue.pageNumber != data) {
					listVue.pageNumber = data;
					listVue.refresh();
				}
			},
			changeCountPerPage:function(data){
				if (listVue.countPerPage != data) {
					listVue.countPerPage = data;
					listVue.refresh();
				}
			},

			remaindRightDifferenceExportExcelHandle : remaindRightDifferenceExportExcelHandle,
			remaindRightImportHandle : remaindRightImportHandle,
		    handlePictureCardPreview:function(file) {
		        this.dialogImageUrl = file.url;
		        this.dialogVisible = true;
		    },
		    handleCallBack:function(response, file, fileList){//上传成功之后的回掉函数
//		    	var modal = {
//        			sourceType:response.data[0].extra,
//        			theLink:response.data[0].url,
//        			fileType:response.data[0].objType,
//        			theSize:response.data[0].byteToStr,
//        			remark:response.data[0].originalName
//	        	}
		    	this.importUrl = response.data[0].url
		    	this.remaindRightImportHandle()
		    },
		    parserExcelHandle : parserExcelHandle,
		    handleReset: handleReset,
		    combineFun : function(){
				combine(this.cache,this.cacheIndex,this.cacheData,this.managedProjectList,this.tgpf_ManagedProjectAnalyseList)
			},
			objectSpanMethod:function(row) {
			  	if(row.columnIndex===1){
			       return checkSpanMethod(listVue.cacheData,row.rowIndex,row.columnIndex);
			  }
			},
	       /* arraySpanMethod:function(row) {
	          if (row.id=='12987122') {
	            if (row.columnIndex === 0) {
	              return [2, 2];
	            } 
	            else if (row.columnIndex === 1) {
	              return [0, 0];
	            }
	          }
	      },
	      objectSpanMethod:function(row) {
	        if (row.columnIndex === 0) {
	          if (row.rowIndex % 3 === 0) {
	            return {
	              rowspan: 3,
	              colspan: 1
	            };
	          } else {
	            return {
	              rowspan: 0,
	              colspan: 0
	            };
	          }
	        }
	      },
	      rowSpanMethod:function() {
	    	  
	      },*/
	      changeQuarter : changeQuarter,
	      changeQuarterEmpty : function(){
	    	  listVue.quarterId = null;
	    	  this.momthList = [
					{tableId:"01",theName:"一月"},{tableId:"02",theName:"二月"},{tableId:"03",theName:"三月"},{tableId:"04",theName:"四月"},{tableId:"05",theName:"五月"},{tableId:"06",theName:"六月"},
					{tableId:"07",theName:"七月"},{tableId:"08",theName:"八月"},{tableId:"09",theName:"九月"},{tableId:"10",theName:"十月"},{tableId:"11",theName:"十一月"},{tableId:"12",theName:"十二月"}
			  ]
	    	  this.monthId = null;
	      },
	      changeCityFun : function(data){
	    	  this.areaId = data.tableId;
	      },
	      changeCityFunEmpty : function(){
	    	  this.areaId = null;
	      },
	      changeMonth : function(data){
	    	  this.monthId = data.tableId;
	      },
	      changeMonthEmpty : function(){
	    	  this.monthId = null
	      }
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue,
			'vue-listselect': VueListSelect,
		},
		watch:{
			//pageNumber : refresh,
//			selectItem : selectedItemChanged,
			year : function(currentVal,oldVal){
				if(currentVal == ""){
					listVue.quarterId = "";
					listVue.monthId = "";
					/*listVue.quarterDisabled = true;
					listVue.monthDisabled = true;*/
					listVue.showCanChoice = 0;
				}
			}
		}
	});

	//------------------------方法定义-开始------------------//
	//列表操作-------------------改变季度
	function changeQuarter(data){
		listVue.quarterId = data.tableId;
		listVue.monthId = "";
		listVue.monthArrList.forEach(function(item){
			if(listVue.quarterId == item.tableId){
				listVue.momthList = item.momthList;
			}
		})
	}
	
	
	
	//列表操作--------------获取"搜索列表"表单参数
	function getSearchForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			pageNumber:this.pageNumber,
			countPerPage:this.countPerPage,
			totalPage:this.totalPage,
			keyword:this.keyword,
			theState:this.theState,
			userStartId:this.userStartId,
			userRecordId:this.userRecordId,
			cityRegionId : this.areaId,
			queryYear:this.year,
			queryQuarter : this.quarterId,
			queryMonth : this.monthId,
		}
	}


	//列表操作--------------获取导入的excel数据
	function getExcelDataForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			url:this.importUrl,
		}
	}

	
	function isInterger(o) {
		return typeof obj === 'number' && obj%1 === 0;
	}

	function handleSelectionChange(val) {
		listVue.selectItem = [];
		for (var i = 0; i < val.length; i++) {
			var index = listVue.tgpf_ManagedProjectAnalyseList.indexOf(val[i]);
			listVue.selectItem.push(index)
		}
		listVue.selectItem.sort()
	}

	//列表操作--------------刷新
	function refresh()
	{
		new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				listVue.tgpf_ManagedProjectAnalyseList=jsonObj.tg_DepositProjectAnalysis_ViewList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount=jsonObj.totalCount;
				
				if(listVue.tgpf_ManagedProjectAnalyseList.length>0)
				{
					listVue.tgpf_ManagedProjectAnalyseList.forEach(function(item){
						
						item.escrowArea = addThousands(item.escrowArea);
						item.escrowAreaRatio = addThousands(item.escrowAreaRatio);
						item.preEscrowArea = addThousands(item.preEscrowArea);
						item.preEscrowAreaRatio = addThousands(item.preEscrowAreaRatio);
						
					});
				}
				
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				listVue.combineFun();
				document.getElementById('Tgpf_ManagedProjectAnalyseDiv').scrollIntoView();
			}
		});
	}
	
	//列表操作------------搜索
	function search(divStr)
	{
		if(listVue.year == ""){
			$("#messageManagedModal").modal({
				 backdrop :'static'
			})
			return ;
		}
		listVue.pageNumber = 1;
		refresh();
	}


	function remaindRightDifferenceExportExcelHandle()
	{
		new ServerInterface(baseInfo.exportExcelInterface).execute(listVue.getSearchForm(), function (jsonObj) {
			if(jsonObj.result != "success")
			{
				$(baseInfo.edModelDivId).modal({
					backdrop :'static'
				});
				listVue.errMsg = jsonObj.info;
			}
			else
			{
				$(baseInfo.sdModelDiveId).modal({
					backdrop :'static'
				});
				
				window.location.href="../"+jsonObj.fileURL;
			}
		});
	}
	
	function remaindRightImportHandle()
	{
		$('#uploadFilePath').fileupload({
			autoUpload: true,
			url: '../FileUploadServlet',
			dataType: 'json',
			progressall: function (e, data) {
				//var progress = parseInt(data.loaded / data.total * 100, 10);
				//progress = progress * 0.10;
				//console.info(progress+"%");
		    },
			add: function (e, data) 
			{
				var files = data.files;
				var length = files.length;
				
				data.submit();
			},
			done: function (e, data) 
			{
				//刷新文件列表，显示出刚上传的文件
//				data = data.result;
//				console.log(data);
				listVue.importUrl = data.result.fileFullPath
				listVue.parserExcelHandle()
			}
		});
	}
	
	function parserExcelHandle()
	{
		new ServerInterface(baseInfo.importExcelInterface).execute(listVue.getExcelDataForm(), function (jsonObj) {
			if (jsonObj.result != "success")
			{
				noty({ "text": jsonObj.info, "type": "error", "timeout": 2000 });
			}
			else
			{
				listVue.selectItem = [];
				listVue.tgpf_ManagedProjectAnalyseList=jsonObj.tgpf_ManagedProjectAnalyseList;
			}
		});
	}
	
	function getExportForm(){
		return{
		   interfaceVersion:this.interfaceVersion,	
		}
	}
	
	//列表操作-----------------------页面加载显示区域
	function loadProject() {
		new ServerInterface(baseInfo.loadProjectInterface).execute(listVue.getExportForm(), function(jsonObj){
			if (jsonObj.result != "success") {
				$(baseInfo.edModelDivId).modal({
					backdrop : 'static'
				});
				listVue.errMsg = jsonObj.info;
			} else {
				listVue.tg_AreaList = jsonObj.sm_CityRegionInfoList;
			}
		});
	}
	
	function initData()
	{
		listVue.selectItem = []
	}
	
	function handleReset()
	{
		listVue.keyword = "";
		listVue.areaId = "";
		listVue.year = "";
		listVue.quarterId = "";
		listVue.monthId = "";
		
	}
	//------------------------方法定义-结束------------------//
	 //年选择器
	laydate.render({
	    elem: '#managedProjectAnalyseSearch',
	    type: 'year',
	    done:function(value){
	    	listVue.year = value;
	    	/*listVue.quarterDisabled = false;
	    	listVue.monthDisabled = false;*/
	    	listVue.showCanChoice = 1;
	    	$(".listSelect .el-input__inner").css("height","28px")
	    	if(listVue.year !=""){
	    		$("#managedProjectAnalyseSearch").removeClass("red-input");
	    		$("#analyseList").hide();
	    		
	    	}
	    	
	    }
	});
	
	//------------------------数据初始化-开始----------------//
	listVue.loadProject();
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#Tgpf_ManagedProjectAnalyseDiv",
	"loadProjectInterface" : "../Sm_CityRegionInfoList", //区域
	"listInterface":"../Tg_DepositProjectAnalysis_ViewList",
	"exportExcelInterface":"../Tg_DepositProjectAnalysis_ViewExportExcel",
});
