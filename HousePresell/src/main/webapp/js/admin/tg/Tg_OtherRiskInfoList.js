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
			cityRegionId:null,
			cityRegionList:[],
			developCompanyId:null,
			developCompanyList:[],
			projectId:null,
			projectList:[],
			tg_OtherRiskInfoList:[],
			demoList : [
				{
					theState :'99999999999999900000',
					busiState :'港澳台通行证',
					eCode :'99999999999999900000',
					userStartName :'启用',
					applyState :'1',
					lastUpdateTimeStamp :'999,999,999.99',
					userRecordName :'99,999,999.00',
					recordTimeStamp :'99,999,999.00',
					cityRegionName :'2018-12-31',
					theNameOfCityRegion :'2018-12-31 24:00:00',
					developCompanyName :'常州市武进区南夏墅戴家村浅水区项目生活区',
					eCodeOfDevelopCompany :'常州市武进区南夏墅戴家村浅水区项目生活区',
					projectName :'常州市武进区南夏墅戴',
					theNameOfProject :'常州市武进区南夏墅戴家村浅水区项目生活区常州市武进区南夏墅戴墅戴',
					riskInputDate :'张三张三张三',
					riskInfo:'9999999999999',
					isUsed :'张三话，张三话，张三话，张三话',
					phone :'99999999999，99999999999，99999999999',
					remark :'常州市武进区南夏墅戴家村浅水区项目生活区常州市武进区南夏墅戴墅戴常州市武进区南夏墅戴家村浅水区项目生活区',
					
				},
				{
					theState :'99999999999999900000',
					busiState :'身份证',
					eCode :'99999999999999900000',
					userStartName :'启用',
					applyState :'1',
					lastUpdateTimeStamp :'9,999.99',
					userRecordName :'9,999.99',
					recordTimeStamp :'9,999.99',
					cityRegionName :'2018-12-31',
					theNameOfCityRegion :'2018-12-31 24:00:00',
					developCompanyName :'常州市武进区南夏墅戴家村浅水区项目生活区',
					eCodeOfDevelopCompany :'常州市武进区南夏墅戴家村浅水区项目生活区',
					projectName :'常州市武进区南夏墅戴',
					theNameOfProject :'常州市武进区南夏墅戴家村浅水区项目生活区常州市武进区南夏墅戴墅戴',
					riskInputDate :'张三张三张三',
					riskInfo:'9999999999999',
					isUsed :'张三话，张三话，张三话，张三话',
					phone :'99999999999，99999999999，99999999999',
					remark :'常州市武进区南夏墅戴家村浅水区项目生活区常州市武进区南夏墅戴墅戴常州市武进区南夏墅戴家村浅水区项目生活区',
					
				}
			],
			editDisabled : true,
			delDisabled : true,
			sm_CityRegionInfoList : [],
			cityRegionId : "",
			riskInputDate : "",
		},
		methods : {
			refresh : refresh,
			initData:initData,
			indexMethod: indexMethod,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			listItemSelectHandle: listItemSelectHandle,
			search:search,
			checkAllClicked : checkAllClicked,
			changePageNumber : function(data){
				//listVue.pageNumber = data;
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
			otherRiskInfoAddHandle : otherRiskInfoAddHandle,//新增
			otherRiskInfoEditHandle : otherRiskInfoEditHandle,//修改
			otherRiskInfoDelHandle : otherRiskInfoDelHandle,//删除
			otherRiskInfoDetailHandle : otherRiskInfoDetailHandle,//详情
			resetSearch : resetSearch,
			showDelModal : showDelModal,
			//changeCityRegionListener : changeCityRegionListener,
			//changeCityRegionEmpty : changeCityRegionEmpty,
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue,
			'vue-listselect': VueListSelect,
		},
		watch:{
//			pageNumber : refresh,
			selectItem : selectedItemChanged,
		}
	});

	//------------------------方法定义-开始------------------//
	 
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
			cityRegionId:this.cityRegionId,
			developCompanyId:this.developCompanyId,
			projectId:this.projectId,
			riskInputDate:this.riskInputDate,
		}
	}
	//列表操作--------------获取“删除资源”表单参数
	function getDeleteForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			idArr:this.selectItem
		}
	}
	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		listVue.isAllSelected = (listVue.tg_OtherRiskInfoList.length > 0)
		&&	(listVue.selectItem.length == listVue.tg_OtherRiskInfoList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tg_OtherRiskInfoList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tg_OtherRiskInfoList.forEach(function(item) {
	    		listVue.selectItem.push(item.tableId);
	    	});
	    }
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
				listVue.tg_OtherRiskInfoList=jsonObj.tg_OtherRiskInfoList;
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount = jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				document.getElementById('tg_OtherRiskInfoListDiv').scrollIntoView();
			}
		});
	}
	
	//列表操作------------搜索
	function search()
	{
		listVue.pageNumber = 1;
		refresh();
	}
	//列表操作------------重置
	function resetSearch(){
		listVue.pageNumber = 1;
		listVue.riskInputDate = "";
		listVue.keyword = "";
		refresh();
	}
	
	function indexMethod(index) {
		return generalIndexMethod(index, listVue)
	}

	function listItemSelectHandle(list) {
		if(list.length == 1){
			listVue.editDisabled = false;
		}else{
			listVue.editDisabled = true;
		}
		if(list.length !=0){
			listVue.delDisabled = false;
		}else{
			listVue.delDisabled = true;
		}
		generalListItemSelectHandle(listVue,list)
 	}
	//列表操作------------新增
	function otherRiskInfoAddHandle(){
		enterNewTab('', '新增其他风险信息', 'tg/Tg_OtherRiskInfoAdd.shtml');
	}
	//列表操作------------修改
	function otherRiskInfoEditHandle(){
		enterNextTab(listVue.selectItem[0], '编辑其他风险信息', 'tg/Tg_OtherRiskInfoEdit.shtml',listVue.selectItem[0]+"21020204");
	}
	//列表操作------------详情
	function otherRiskInfoDetailHandle(tableId){
		enterNextTab(tableId, '其他风险信息详情', 'tg/Tg_OtherRiskInfoDetail.shtml',tableId+"21020204");
	}
	//列表操作------------显示删除模态框
	function showDelModal(){
		$(baseInfo.deleteDivId).modal({
			backdrop :'static'
		});
	}
	//列表操作------------删除
	function otherRiskInfoDelHandle(){
		$(baseInfo.deleteDivId).modal("hide");
		new ServerInterface(baseInfo.deleteInterface).execute(listVue.getDeleteForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				generalErrorModal(jsonObj,jsonObj.info);
			}
			else
			{
				generalSuccessModal();
				listVue.selectItem = [];
				refresh();
			}
		});
		
	}
	//列表操作-------------获取区域
    function getCityRegionList()
    {
        serverRequest("../Sm_CityRegionInfoForSelect",getTotalListForm(),function (jsonObj) {
            listVue.sm_CityRegionInfoList=jsonObj.sm_CityRegionInfoList;
        });
    }
/*    //列表操作-------------改变区域
    function changeCityRegionListener(data){
    	if (listVue.cityRegionId != data.tableId) {
            listVue.cityRegionId = data.tableId
            listVue.refresh()
        }
    }
    //列表操作-------------清空区域
    function changeCityRegionEmpty(){
    	if (listVue.cityRegionId != null) {
            listVue.cityRegionId = null
            listVue.refresh()
        }
    }*/
	function initData()
	{
		//getCityRegionList();
	}
	/**
	 * 初始化日期插件
	 */
	laydate.render({
	  elem: '#otherRiskInfoSearch',
	  done: function(value, date, endDate){
	    listVue.riskInputDate = value;
	  }
	});
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tg_OtherRiskInfoListDiv",
	"deleteDivId" : "#deleteOtherRiskInfoModal",
	"listInterface":"../Tg_OtherRiskInfoList",
	"deleteInterface":"../Tg_OtherRiskInfoBatchDelete",
});
