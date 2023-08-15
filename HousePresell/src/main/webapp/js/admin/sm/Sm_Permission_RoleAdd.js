(function(baseInfo){
	var addVue = new Vue({
		el : baseInfo.addDivId,
		data : {
			interfaceVersion :19000101,
			sm_Permission_RoleModel: {
				busiType : "0"
			},
			companyType : "",
			companyTypeValue : "",
			companyTypeList: [],
			busiTypeList : [
				{tableId:"1",theName:"停用"},
            	{tableId:"0",theName:"启用"}
			]
		},
		methods : {
			//添加
			initData : initData,
			getAddForm : getAddForm,
            addSm_Permission_Role: addSm_Permission_Role,
            sm_Permission_RoleUIAdd: sm_Permission_RoleUIAdd,
            changeTheType: function (data) {
				this.companyType = data.tableId;
				this.companyTypeValue = data.theValue;
			},
		    isUsingChange:function(data){
		    	addVue.sm_Permission_RoleModel.busiType = data.tableId;
            },
            emptyIsUsingChange : function(){
            	addVue.sm_Permission_RoleModel.busiType = null;
            },
		},
		computed:{
			 
		},
		components : {
			'vue-select': VueSelect ,
		},
		watch:{
			
		}
	});

	//------------------------方法定义-开始------------------//
	function getAddForm()
	{
		return {
			interfaceVersion:addVue.interfaceVersion,
			theName:addVue.sm_Permission_RoleModel.theName,
			companyType:addVue.companyTypeValue,
			remark:addVue.sm_Permission_RoleModel.remark,
			busiType:addVue.sm_Permission_RoleModel.busiType,
			downDateStr:document.getElementById("date0101020101").value,
		}
	}
	
    function addSm_Permission_Role()
    {
        new ServerInterface(baseInfo.addInterface).execute(addVue.getAddForm(), function(jsonObj){
            if(jsonObj.result != "success")
            {
            	generalErrorModal(jsonObj);
            }
            else
            {
            	generalSuccessModal();
            	enterNewTabCloseCurrent(jsonObj.sm_Permission_Role.tableId, '角色详情', 'sm/Sm_Permission_RoleDetail.shtml');
            }
        });
    }
    
    function initData()
	{
		laydate.render({
            elem: '#date0101020101',
            min: getDayDate(),
        });
		
		getSm_BaseParameterForSelect();
	}
    
    function getSm_BaseParameterForSelect()
	{
	    generalGetParamList("8",function (list) 
	    {
			addVue.companyTypeList =list;
	    })
	}
    
    //添加
    function sm_Permission_RoleUIAdd(roleId)
    {
    	enterNewTab(roleId, "角色授权新增", "sm/Sm_Permission_RoleUIAdd.shtml");
    }
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
    addVue.initData();
    //------------------------数据初始化-结束----------------//
})({
	"addDivId":"#sm_Permission_RoleAddDiv",
	"addInterface":"../Sm_Permission_RoleAdd"
});
