(function(baseInfo) {
	var listVue = new Vue(
			{
				el : baseInfo.listDivId,
				data : {
					interfaceVersion : 19000101,
					pageNumber : 1,
					countPerPage : 20,
					totalPage : 1,
					totalCount : 1,
					keyword : "",
					selectItem : [],
					isAllSelected : false,
					theState : 0,// 正常为0，删除为1
					userStartId : null,
					userStartList : [],
					userRecordId : null,
					userRecordList : [],
//					projectId : null,
					projectList : [],
					buildingId : null,
					buildingList : [],
					buildingUnitId : null,
					buildingUnitList : [],
					bankId : null,
					bankList : [],
					tgpf_RemainRightList : [],
					fromDate : null,
					dataUpload : {
						"appid" : "ossq7y44g",
						"appsecret" : "yg2us2a7",
						"project" : "HousePresell"
					},
					importUrl : "",
					deleCodes : [],
					companyId:'',
					projectId:'',
					buildId:'',
					qs_companyNameList : [], //页面加载显示开发企业
					qs_projectNameList : [], //显示项目名称
					qs_buildingNumberlist : [], //显示楼幢编号
				},
				methods : {
					refresh : refresh,
					initData : initData,
					handleSelectionChange : handleSelectionChange,
					getSearchForm : getSearchForm,
					getDeleteForm : getDeleteForm,
					getExcelDataForm : getExcelDataForm,
					changeprojectListener : changeprojectListener,
					changeProjectEmpty : changeProjectEmpty,
					tgpf_RemainRightDel : tgpf_RemainRightDel,
					search : search,
					checkAllClicked : checkAllClicked,
					changePageNumber : function(data) {
						if (listVue.pageNumber != data) {
							listVue.pageNumber = data;
							listVue.refresh();
						}
					},
					changeCountPerPage : function(data) {
						if (listVue.countPerPage != data) {
							listVue.countPerPage = data;
							listVue.refresh();
						}
					},
					remaindRightDifferenceExportExcelHandle : remaindRightDifferenceExportExcelHandle,
					remaindRightImportHandle : remaindRightImportHandle,
					handlePictureCardPreview : function(file) {
						this.dialogImageUrl = file.url;
						this.dialogVisible = true;
					},
					handleCallBack : function(response, file, fileList) {// 上传成功之后的回掉函数
						this.importUrl = response.data[0].url
						this.remaindRightImportHandle()
					},
					parserExcelHandle : parserExcelHandle,
					handleReset : handleReset,
					
					loadCompanyName : loadCompanyNameFun, //页面加载显示开发企业方法
					changeCompanyHandle : changeCompanyHandleFun, //改变企业名称的方法
					changeProjectHandle : changeProjectHandleFun, //改变项目名称的方法
					getCompanyId: function(data) {
						listVue.projectId = null;
						listVue.buildId = null;
						listVue.qs_projectNameList = [];
						listVue.qs_buildingNumberlist = [];
						listVue.companyId = data.tableId;
						listVue.changeCompanyHandle();
					},
					emptyCompanyId : function(data){
						listVue.companyId = null;
						listVue.projectId = null;
						listVue.buildId = null;
						listVue.qs_projectNameList = [];
						listVue.qs_buildingNumberlist = [];
					},
					getProjectId: function(data) {
						listVue.buildId = null;
						listVue.qs_buildingNumberlist = [];
						listVue.projectId = data.tableId;
						listVue.changeProjectHandle();
					},
					emptyProjectId : function(data){
						listVue.projectId = null;
						listVue.buildId = null;
						listVue.qs_buildingNumberlist = [];
					},
					getBuildId: function(data) {
						listVue.buildId = data.tableId;
					},
					emptyBuildId : function(data){
						listVue.buildId = null;
					}
				},
				computed : {

				},
				components : {
					'vue-nav' : PageNavigationVue,
					'vue-listselect' : VueListSelect
				},
				watch : {
				// pageNumber : refresh,
				// selectItem : selectedItemChanged,
				}
			});

	// ------------------------方法定义-开始------------------//
	
	function loadCompanyNameFun() {
		var model = {
				interfaceVersion : 19000101,
				exceptZhengTai : true,
			};
		new ServerInterface(baseInfo.companyNameInterface).execute(model, function(jsonObj)
				{
					if(jsonObj.result != "success")
					{
						generalErrorModal(jsonObj);
					}
					else
					{
						listVue.qs_companyNameList = jsonObj.emmp_CompanyInfoList;
					}
				});
	}
	//列表操作--------------------改变开发企业的方法
	function changeCompanyHandleFun() {
		changeCompanyHandle(listVue,baseInfo.changeCompanyInterface,function(jsonObj){
			listVue.qs_projectNameList = jsonObj.empj_ProjectInfoList;
		},listVue.errMsg,baseInfo.edModelDivId)
	}
	//列表操作--------------------改变项目名称的方法
	function changeProjectHandleFun() {
		changeProjectHandle(listVue,baseInfo.changeProjectInterface,function(jsonObj){
			listVue.qs_buildingNumberlist = jsonObj.empj_BuildingInfoList;
		},listVue.errMsg,baseInfo.edModelDivId)
	}

	function changeprojectListener(data) {
		console.log(data);
		if (listVue.projectId != data.tableId) {
			listVue.projectId = data.tableId
		}
	}

	function changeProjectEmpty() {
		if (listVue.projectId != null) {
			listVue.projectId = null
			// listVue.refresh()
		}
	}

	// 列表操作--------------获取"搜索列表"表单参数
	function getSearchForm() {
		return {
			interfaceVersion : this.interfaceVersion,
			pageNumber : this.pageNumber,
			countPerPage : this.countPerPage,
			totalPage : this.totalPage,
			keyword : this.keyword,
			theState : this.theState,
			userStartId : this.userStartId,
			userRecordId : this.userRecordId,
			projectId : listVue.projectId,
			companyId : listVue.companyId,
			buildingId : listVue.buildId,
			buildingUnitId : this.buildingUnitId,
			bankId : this.bankId,
			fromDate : listVue.fromDate,
			billtTimeStampStart : listVue.fromDate,
			billtTimeStampEnd : listVue.fromDate,
		}
	}
	// 列表操作--------------获取“删除资源”表单参数
	function getDeleteForm() {
		return {
			interfaceVersion : this.interfaceVersion,
			idArr : this.selectItem
		}
	}
	// 列表操作--------------获取导入的excel数据
	function getExcelDataForm() {
		return {
			interfaceVersion : this.interfaceVersion,
			url : this.importUrl,
		}
	}
	function tgpf_RemainRightDel() {
		noty({
			layout : 'center',
			modal : true,
			text : "确认批量删除吗？",
			type : "confirm",
			buttons : [ {
				addClass : "btn btn-primary",
				text : "确定",
				onClick : function($noty) {
					$noty.close();
					for (var i = listVue.selectItem.length - 1; i >= 0; i--) {
						var item = listVue.selectItem[i];
						listVue.deleCodes.push(item.eCode);
						listVue.tgpf_RemainRightList.splice(item, 1);
					}
					listVue.selectItem = [];
				}
			}, {
				addClass : "btn btn-danger",
				text : "取消",
				onClick : function($noty) {

					$noty.close();
				}
			} ]

		});
	}

	function isInterger(o) {
		return typeof obj === 'number' && obj % 1 === 0;
	}

	function handleSelectionChange(val) {
		listVue.selectItem = [];
		for (var i = 0; i < val.length; i++) {
			var index = listVue.tgpf_RemainRightList.indexOf(val[i]);
			listVue.selectItem.push(index)
		}
		listVue.selectItem.sort()
	}

	// 列表操作--------------“全选”按钮被点击
	function checkAllClicked() {
		if (listVue.selectItem.length == listVue.tgpf_RemainRightList.length) {
			listVue.selectItem = [];
		} else {
			listVue.selectItem = [];// 解决：已经选择一个复选框后，再次点击全选样式问题
			listVue.tgpf_RemainRightList.forEach(function(item) {
				listVue.selectItem.push(item.tableId);
			});
		}
	}
	// 列表操作--------------刷新
	function refresh() {
		new ServerInterface(baseInfo.listInterface)
				.execute(
						listVue.getSearchForm(),
						function(jsonObj) {
							if (jsonObj.result != "success") {
								noty({
									"text" : jsonObj.info,
									"type" : "error",
									"timeout" : 2000
								});
							} else {
								listVue.tgpf_RemainRightList = jsonObj.tg_RetainedRightsList;
								listVue.tgpf_RemainRightList
										.forEach(function(item) {
											item.actualDepositAmount = addThousands(item.actualDepositAmount);
											item.depositAmountFromloan = addThousands(item.depositAmountFromloan);
											item.theAmount = addThousands(item.theAmount);
											item.amountOfInterestNotdue = addThousands(item.amountOfInterestNotdue);
											item.amountOfInterestNotdue = addThousands(item.amountOfInterestNotdue);
											item.amountOfInterestdue = addThousands(item.amountOfInterestdue);
										})
								listVue.pageNumber = jsonObj.pageNumber;
								listVue.countPerPage = jsonObj.countPerPage;
								listVue.totalPage = jsonObj.totalPage;
								listVue.totalCount = jsonObj.totalCount;
								listVue.keyword = jsonObj.keyword;
								listVue.selectedItem = [];
								// 动态跳转到锚点处，id="top"
								document.getElementById(
										'tgpf_RemainRightSearchDiv')
										.scrollIntoView();
							}
						});
	}

	// 列表操作------------搜索
	function search() {
		/*
		 * if(document.getElementById("remainSign").value == "") {
		 * alert("请输入拨付日期！"); return false; }
		 */
		listVue.pageNumber = 1;
		refresh();
	}

	function remaindRightDifferenceExportExcelHandle() {
		new ServerInterface(baseInfo.exportExcelInterface).execute(listVue
				.getSearchForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
				generalErrorModal(jsonObj, jsonObj.info);
			} else {
				window.location.href = "../" + jsonObj.fileDownloadPath;
				listVue.selectItem = [];
				refresh();
			}
		});
	}

	function remaindRightImportHandle() {
		$('#uploadFilePath').fileupload({
			autoUpload : true,
			url : '../FileUploadServlet',
			dataType : 'json',
			progressall : function(e, data) {
				// var progress = parseInt(data.loaded / data.total * 100, 10);
				// progress = progress * 0.10;
				// console.info(progress+"%");
			},
			add : function(e, data) {
				var files = data.files;
				var length = files.length;

				data.submit();
			},
			done : function(e, data) {
				// 刷新文件列表，显示出刚上传的文件
				// data = data.result;
				// console.log(data);
				listVue.importUrl = data.result.fileFullPath
				listVue.parserExcelHandle()
			}
		});
	}

	function getProjectList() {
		serverRequest("../Empj_ProjectInfoForSelect", getTotalListForm(),
				function(jsonObj) {
					listVue.projectList = jsonObj.empj_ProjectInfoList;
				})
	}

	function parserExcelHandle() {
		new ServerInterface(baseInfo.importExcelInterface).execute(listVue
				.getExcelDataForm(), function(jsonObj) {
			if (jsonObj.result != "success") {
				noty({
					"text" : jsonObj.info,
					"type" : "error",
					"timeout" : 2000
				});
			} else {
				listVue.selectItem = [];
				listVue.tgpf_RemainRightList = jsonObj.tgpf_RetainedRightsList;
			}
		});
	}

	function initData() {
		listVue.selectItem = [];
		getProjectList();
	}

	function handleReset() {
		listVue.keyword = "";
		listVue.remainSign = "";
		listVue.companyId= "";
		listVue.projectId= "";
		listVue.buildId= "";
		listVue.fromDate = "";
	}
	// 添加日期控件
	laydate.render({
		elem : '#remainSign',
		done : function(value, date) {
			listVue.fromDate = value;
		}
	});
	// ------------------------方法定义-结束------------------//

	// ------------------------数据初始化-开始----------------//
	listVue.loadCompanyName();
	listVue.refresh();
	listVue.initData();
	// ------------------------数据初始化-结束----------------//
})({
	"listDivId" : "#tgpf_RemainRightSearchDiv",
	"updateDivId" : "#updateModel",
	"addDivId" : "#addModal",
	"listInterface" : "../Tg_RetainedRightsList",
	"exportExcelInterface" : "../Tg_RemainRightsExportExcel",
	"companyNameInterface" : "../Emmp_CompanyInfoList", //显示开发企业名称接口
	"changeCompanyInterface" : "../Empj_ProjectInfoList", //改变企业名称接口
	"changeProjectInterface" : "../Empj_BuildingInfoList", //改变项目名称接口
});
