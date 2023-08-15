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
			companyId:null,
			companyList:[],
			projectId:null,
			projectList:[],
			bankId:null,
			bankList:[],
			bankAccountEscrowedId:null,
			bankAccountEscrowedList:[],
			tgpf_AccVoucherList:[],
			markDate: "",
			busiState: "",
			idArr: [],
			errMsg: "",
			propelDisabled : true,
			valArr:[],
			busiStateList : [
				{tableId:"0",theName:"未推送"},
				{tableId:"1",theName:"已推送"},
			],
		},
		methods : {
			refresh : refresh,
			initData:initData,
			getSearchForm : getSearchForm,
			getDeleteForm : getDeleteForm,
			listItemSelectHandle: listItemSelectHandle,
			tgpf_AccVoucherDelOne : tgpf_AccVoucherDelOne,
			tgpf_AccVoucherDel : tgpf_AccVoucherDel,
			showAjaxModal:showAjaxModal,
			search:search,
			checkAllClicked : checkAllClicked,
			changePageNumber : function(data){
				if (listVue.pageNumber != data) {
				listVue.pageNumber = data;
				listVue.refresh();
				}
			},
			changeCountPerPage:function(data){
				console.log(data);
				if (listVue.countPerPage != data) {
					listVue.countPerPage = data;
					listVue.refresh();
				}
			},
			indexMethod:indexMethod,//左侧序号
			handleReset: handleReset,
			handlePropel: handlePropel,
			getPropelForm: getPropelForm,
			succClose: succClose,
			errClose: errClose,
			subTsAgain : subTsAgain,
			changeBusiState : function(data){
				this.busiState = data.tableId;
			},
			busiStateEmpty : function(){
				this.busiState =  null;
			}
		},
		computed:{
			 
		},
		components : {
			'vue-nav' : PageNavigationVue,
			'vue-listselect' : VueListSelect,
		},
		watch:{
			//pageNumber : refresh,
			selectItem : selectedItemChanged,
		}
	});
	var updateVue = new Vue({
		el : baseInfo.updateDivId,
		data : {
			interfaceVersion :19000101,
			theState:null,
			busiState:null,
			eCode:null,
			userStartId:null,
			userStartList:[],
			createTimeStamp:null,
			lastUpdateTimeStamp:null,
			userRecordId:null,
			userRecordList:[],
			recordTimeStamp:null,
			billTimeStamp:null,
			theType:null,
			tradeCount:null,
			totalTradeAmount:null,
			contentJson:null,
			payoutTimeStamp:null,
			companyId:null,
			companyList:[],
			theNameOfCompany:null,
			projectId:null,
			projectList:[],
			theNameOfProject:null,
			bankId:null,
			bankList:[],
			theNameOfBank:null,
			DayEndBalancingState:null,
			bankAccountEscrowedId:null,
			bankAccountEscrowedList:[],
			theAccountOfBankAccountEscrowed:null,
			payoutAmount:null,
		},
		methods : {
			getUpdateForm : getUpdateForm,
			update:updateTgpf_AccVoucher
		}
	});
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			theState:null,
			busiState:null,
			eCode:null,
			userStartId:null,
			userStartList:[],
			createTimeStamp:null,
			lastUpdateTimeStamp:null,
			userRecordId:null,
			userRecordList:[],
			recordTimeStamp:null,
			billTimeStamp:null,
			theType:null,
			tradeCount:null,
			totalTradeAmount:null,
			contentJson:null,
			payoutTimeStamp:null,
			companyId:null,
			companyList:[],
			theNameOfCompany:null,
			projectId:null,
			projectList:[],
			theNameOfProject:null,
			bankId:null,
			bankList:[],
			theNameOfBank:null,
			DayEndBalancingState:null,
			bankAccountEscrowedId:null,
			bankAccountEscrowedList:[],
			theAccountOfBankAccountEscrowed:null,
			payoutAmount:null,
		},
		methods : {
			getAddForm : getAddForm,
			add : addTgpf_AccVoucher
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
			companyId:this.companyId,
			projectId:this.projectId,
			bankId:this.bankId,
			bankAccountEscrowedId:this.bankAccountEscrowedId,
			billTimeStamp:document.getElementById("testSign3").value,
			busiState:this.busiState,
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
	//列表操作--------------获取“新增”表单参数
	function getAddForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			busiState:this.busiState,
			eCode:this.eCode,
			userStartId:this.userStartId,
			createTimeStamp:this.createTimeStamp,
			lastUpdateTimeStamp:this.lastUpdateTimeStamp,
			userRecordId:this.userRecordId,
			recordTimeStamp:this.recordTimeStamp,
			billTimeStamp:this.billTimeStamp,
			theType:this.theType,
			tradeCount:this.tradeCount,
			totalTradeAmount:this.totalTradeAmount,
			contentJson:this.contentJson,
			payoutTimeStamp:this.payoutTimeStamp,
			companyId:this.companyId,
			theNameOfCompany:this.theNameOfCompany,
			projectId:this.projectId,
			theNameOfProject:this.theNameOfProject,
			bankId:this.bankId,
			theNameOfBank:this.theNameOfBank,
			DayEndBalancingState:this.DayEndBalancingState,
			bankAccountEscrowedId:this.bankAccountEscrowedId,
			theAccountOfBankAccountEscrowed:this.theAccountOfBankAccountEscrowed,
			payoutAmount:this.payoutAmount,
		}
	}
	//列表操作--------------获取“更新”表单参数
	function getUpdateForm()
	{
		return{
			interfaceVersion:this.interfaceVersion,
			theState:this.theState,
			busiState:this.busiState,
			eCode:this.eCode,
			userStartId:this.userStartId,
			createTimeStamp:this.createTimeStamp,
			lastUpdateTimeStamp:this.lastUpdateTimeStamp,
			userRecordId:this.userRecordId,
			recordTimeStamp:this.recordTimeStamp,
			billTimeStamp:this.billTimeStamp,
			theType:this.theType,
			tradeCount:this.tradeCount,
			totalTradeAmount:this.totalTradeAmount,
			contentJson:this.contentJson,
			payoutTimeStamp:this.payoutTimeStamp,
			companyId:this.companyId,
			theNameOfCompany:this.theNameOfCompany,
			projectId:this.projectId,
			theNameOfProject:this.theNameOfProject,
			bankId:this.bankId,
			theNameOfBank:this.theNameOfBank,
			DayEndBalancingState:this.DayEndBalancingState,
			bankAccountEscrowedId:this.bankAccountEscrowedId,
			theAccountOfBankAccountEscrowed:this.theAccountOfBankAccountEscrowed,
			payoutAmount:this.payoutAmount,
		}
	}
	function tgpf_AccVoucherDel()
	{
		noty({
			layout:'center',
			modal:true,
			text:"确认批量删除吗？",
			type:"confirm",
			buttons:[
				{
					addClass:"btn btn-primary",
					text:"确定",
					onClick:function($noty){
						$noty.close();
						new ServerInterface(baseInfo.deleteInterface).execute(listVue.getDeleteForm(), function(jsonObj){
							if(jsonObj.result != "success")
							{
								noty({"text":jsonObj.info,"type":"error","timeout":2000});
							}
							else
							{
								listVue.selectItem = [];
								refresh();
							}
						});
					}
				},
				{
					addClass:"btn btn-danger",
					text:"取消",
					onClick:function($noty){
						
						$noty.close();
					}
				}
			]
			
		});
	}
	function tgpf_AccVoucherDelOne(tgpf_AccVoucherId)
	{
		var model = {
			interfaceVersion :19000101,
			idArr:[tgpf_AccVoucherId],
		};
		
		noty({
			layout:'center',
			modal:true,
			text:"确认删除吗？",
			type:"confirm",
			buttons:[
				{
					addClass:"btn btn-primary",
					text:"确定",
					onClick:function($noty){
						$noty.close();
						new ServerInterface(baseInfo.deleteInterface).execute(model , function(jsonObj){
							if(jsonObj.result != "success")
							{
								noty({"text":jsonObj.info,"type":"error","timeout":2000});
							}
							else
							{
								refresh();
							}
						});
					}
				},
				{
					addClass:"btn btn-danger",
					text:"取消",
					onClick:function($noty){
						
						$noty.close();
					}
				}
			]
			
		});
	}
	//选中状态有改变，需要更新“全选”按钮状态
	function selectedItemChanged()
	{	
		listVue.isAllSelected = (listVue.tgpf_AccVoucherList.length > 0)
		&&	(listVue.selectItem.length == listVue.tgpf_AccVoucherList.length);
	}
	//列表操作--------------“全选”按钮被点击
	function checkAllClicked()
	{
	    if(listVue.selectItem.length == listVue.tgpf_AccVoucherList.length)
	    {
	    	listVue.selectItem = [];
	    }
	    else
	    {
	    	listVue.selectItem = [];//解决：已经选择一个复选框后，再次点击全选样式问题
	    	listVue.tgpf_AccVoucherList.forEach(function(item) {
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
				listVue.billTimeStamp = jsonObj.billTimeStamp;
				listVue.tgpf_AccVoucherList=jsonObj.tgpf_AccVoucherList;
				listVue.tgpf_AccVoucherList.forEach(function(item){
					item.totalTradeAmount = addThousands(item.totalTradeAmount);
				});
				listVue.pageNumber=jsonObj.pageNumber;
				listVue.countPerPage=jsonObj.countPerPage;
				listVue.totalPage=jsonObj.totalPage;
				listVue.totalCount=jsonObj.totalCount;
				listVue.keyword=jsonObj.keyword;
				listVue.selectedItem=[];
				//动态跳转到锚点处，id="top"
				//document.getElementById('tgpf_AccVoucherListDiv').scrollIntoView();
			}
		});
	}
	
	//列表操作------------搜索
	function search()
	{
		listVue.pageNumber = 1;
		refresh();
	}
	
	//弹出编辑模态框--更新操作
	function showAjaxModal(tgpf_AccVoucherModel)
	{
		//tgpf_AccVoucherModel数据库的日期类型参数，会导到网络请求失败
		//Vue.set(updateVue, 'tgpf_AccVoucher', tgpf_AccVoucherModel);
		//updateVue.$set("tgpf_AccVoucher", tgpf_AccVoucherModel);
		
		updateVue.theState = tgpf_AccVoucherModel.theState;
		updateVue.busiState = tgpf_AccVoucherModel.busiState;
		updateVue.eCode = tgpf_AccVoucherModel.eCode;
		updateVue.userStartId = tgpf_AccVoucherModel.userStartId;
		updateVue.createTimeStamp = tgpf_AccVoucherModel.createTimeStamp;
		updateVue.lastUpdateTimeStamp = tgpf_AccVoucherModel.lastUpdateTimeStamp;
		updateVue.userRecordId = tgpf_AccVoucherModel.userRecordId;
		updateVue.recordTimeStamp = tgpf_AccVoucherModel.recordTimeStamp;
		updateVue.billTimeStamp = tgpf_AccVoucherModel.billTimeStamp;
		updateVue.theType = tgpf_AccVoucherModel.theType;
		updateVue.tradeCount = tgpf_AccVoucherModel.tradeCount;
		updateVue.totalTradeAmount = tgpf_AccVoucherModel.totalTradeAmount;
		updateVue.contentJson = tgpf_AccVoucherModel.contentJson;
		updateVue.payoutTimeStamp = tgpf_AccVoucherModel.payoutTimeStamp;
		updateVue.companyId = tgpf_AccVoucherModel.companyId;
		updateVue.theNameOfCompany = tgpf_AccVoucherModel.theNameOfCompany;
		updateVue.projectId = tgpf_AccVoucherModel.projectId;
		updateVue.theNameOfProject = tgpf_AccVoucherModel.theNameOfProject;
		updateVue.bankId = tgpf_AccVoucherModel.bankId;
		updateVue.theNameOfBank = tgpf_AccVoucherModel.theNameOfBank;
		updateVue.DayEndBalancingState = tgpf_AccVoucherModel.DayEndBalancingState;
		updateVue.bankAccountEscrowedId = tgpf_AccVoucherModel.bankAccountEscrowedId;
		updateVue.theAccountOfBankAccountEscrowed = tgpf_AccVoucherModel.theAccountOfBankAccountEscrowed;
		updateVue.payoutAmount = tgpf_AccVoucherModel.payoutAmount;
		$(baseInfo.updateDivId).modal('show', {
			backdrop :'static'
		});
	}
	function updateTgpf_AccVoucher()
	{
		new ServerInterface(baseInfo.updateInterface).execute(updateVue.getUpdateForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				$(baseInfo.updateDivId).modal('hide');
				refresh();
			}
		});
	}
	function addTgpf_AccVoucher()
	{
		new ServerInterface(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				noty({"text":jsonObj.info,"type":"error","timeout":2000});
			}
			else
			{
				$(baseInfo.addDivId).modal('hide');
				addVue.busiState = null;
				addVue.eCode = null;
				addVue.userStartId = null;
				addVue.createTimeStamp = null;
				addVue.lastUpdateTimeStamp = null;
				addVue.userRecordId = null;
				addVue.recordTimeStamp = null;
				addVue.billTimeStamp = null;
				addVue.theType = null;
				addVue.tradeCount = null;
				addVue.totalTradeAmount = null;
				addVue.contentJson = null;
				addVue.payoutTimeStamp = null;
				addVue.companyId = null;
				addVue.theNameOfCompany = null;
				addVue.projectId = null;
				addVue.theNameOfProject = null;
				addVue.bankId = null;
				addVue.theNameOfBank = null;
				addVue.DayEndBalancingState = null;
				addVue.bankAccountEscrowedId = null;
				addVue.theAccountOfBankAccountEscrowed = null;
				addVue.payoutAmount = null;
				refresh();
			}
		});
	}
	
	function listItemSelectHandle(val) {
		listVue.valArr = val;
		/*generalListItemSelectHandle(val,list);*/
		// 获取选中需要修改的数据的tableId'
		 listVue.idArr = [];
        var length = val.length;
        if(length > 0){
	        for(var i = 0;i < length;i++){
	        	listVue.idArr.push(val[i].tableId);
	        	if(val[i].dayEndBalancingState == "0" || val[i].dayEndBalancingState == "1"){
					listVue.propelDisabled = true;
					return;
				}else{
					listVue.propelDisabled = false;
				}
	        }
        }else{
        	listVue.propelDisabled = true;	
        }
 	}
	
	function indexMethod(index) {
        return generalIndexMethod(index, listVue)
    }
  //根据按钮权限，动态控制，该页面的所有“可控按钮”的显示与否
	function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}
	function initData()
	{
		initButtonList();
	}
	// 添加日期控件
	laydate.render({
		elem: '#testSign3',
	});
	
	function handleReset()
	{
		this.keyword = "";
		document.getElementById("testSign3").value = "";
		this.busiState = "";
	}
	
	function handlePropel()
	{
		var arrVal = listVue.valArr;
	    var flag = true;
		for(var i = 0;i<arrVal.length;i++){
			if(arrVal[i].busiState == "1"){
				$("#confirmTs").modal({
					backdrop :'static'
				})
				flag = false;
				break;
			}
		}
		if(flag == true){
			new ServerInterface(baseInfo.propelInterface).execute(listVue.getPropelForm(), function(jsonObj){
				if(jsonObj.result != "success")
				{
					listVue.errMsg = jsonObj.info;
					$(baseInfo.eaModel).modal("show",{
						backdrop :'static'
					});
				}
				else
				{
					$(baseInfo.successDivId).modal("show",{
						backdrop :'static'
					});
					refresh();
				}
			});
		}
		
	}
	function subTsAgain(){
		$("#confirmTs").modal('hide')
		new ServerInterface(baseInfo.propelInterface).execute(listVue.getPropelForm(), function(jsonObj){
			if(jsonObj.result != "success")
			{
				listVue.errMsg = jsonObj.info;
				$(baseInfo.eaModel).modal("show",{
					backdrop :'static'
				});
			}
			else
			{
				$(baseInfo.successDivId).modal("show",{
					backdrop :'static'
				});
				refresh();
			}
		});
	
	}
	function succClose()
	{
		$(baseInfo.successDivId).modal('hide');
	}
	
	function errClose()
	{
		$(baseInfo.eaModel).modal('hide');
	}
	
	function getPropelForm()
	{
		return {
			interfaceVersion :19000101,
			idArr: listVue.idArr,
		}
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.refresh();
	listVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tgpf_AccVoucherListDiv",
	"eaModel":"#eaModel",
	"successDivId":"#saModel",
	"listInterface":"../Tgpf_AccVoucherList",
	"addInterface":"../Tgpf_AccVoucherAdd",
	"deleteInterface":"../Tgpf_AccVoucherDelete",
	"updateInterface":"../Tgpf_AccVoucherUpdate",
	"propelInterface":"../Tgpf_AccVoucherPush"
});
