(function(baseInfo){
	var loginVue = new Vue({
		el : baseInfo.loginDivId,
		data : {
			interfaceVersion :19000101,
			theName:'',
			loginPassword:'',
			ifShowflag: false,
			errMsg: "",
			outdata: "",
			active: false,
			activeIE: false,
		},
		methods : {
			//详情
			refresh : refresh,
			initData: initData,
			
			//添加
			getLoginForm : getLoginForm,
			login:login,
			/*ss:function() {
				loginVue.$nextTick(function(){
					loginVue.$refs.contentFocus.focus()
				} )
			}*/
			userLoginCommit : userLoginCommit,
		},
		computed:{
			 
		},
		components : {

		},
		watch:{
			loginPassword:function(curVal,oldVal) {
				var USER_AGENT = navigator.userAgent.toLowerCase();
			    var isChromew = /.*(chrome)\/([\w.]+).*/;
			    var isChromew = isChromew.test(USER_AGENT);
			    if(isChromew) {
					if(curVal != "") {
						loginVue.active = true;
					}else {
						loginVue.active = false;
					}
			    }else {
					if(curVal != "") {
						loginVue.activeIE = true;
					}else {
						loginVue.activeIE = false;
					}
			    }
			}
		}
	});

	//------------------------方法定义-开始------------------//


	//详情操作--------------
	function refresh()
	{

	}
	
	//详情更新操作--------------获取"更新机构详情"参数
	function getLoginForm()
	{
		return {
			interfaceVersion:this.interfaceVersion,
			theName:this.theName,//用户名
            loginPassword:this.loginPassword,//密码
            ukeyNumber: loginVue.outdata,//密钥
		}
	}

	function login()
	{
		var model = {
			interfaceVersion:this.interfaceVersion,
			theName:this.theName,//用户名
			loginPassword:this.loginPassword,//密码
            //ukeyNumber: loginVue.outdata,//密钥
			requestTime: 1,
		}
		new ServerInterface(baseInfo.loginInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				/*
				 * xsz by time 2018-12-19 11:20:01
				 * 判断是否提示已登录
				 * isMsg 不为空
				 */
				var isMsg = jsonObj.isMsg;
				console.log("isMsg="+isMsg);
				if(isMsg=="1")
				{
					$(baseInfo.tipsUserloginDiv).modal({
					    backdrop :'static'
				    });
					
				}
				else
				{
					loginVue.errMsg = jsonObj.info;
					loginVue.ifShowflag = true;
				}
			}
			else
			{
				if(jsonObj.isEncrypt == 0) {
					
					window.location.href='index.shtml';
						
				}else {
					
					try{
						
						var rv;
						id = TZKeyOcxLogin.Tz_SetVenderID(6);
						phKey = TZKeyOcxLogin.TZ_Connect(-1,0);
						console.log("phKey="+phKey);
						
						rv = TZKeyOcxLogin.Tz_ReadKeyID(phKey);
						
						loginVue.outdata = rv;
					}
					catch(e)
					{
						loginVue.errMsg = "请检查是否安装插件!";
						loginVue.ifShowflag = true;
					}
					
					if(loginVue.outdata == 0) {
						
						loginVue.errMsg = "请检查KEY是否正确插入或者是否授权!";
						loginVue.ifShowflag = true;
						
					}
					else 
					{
						var model1 = 
						{
							interfaceVersion:loginVue.interfaceVersion,
							theName:loginVue.theName,//用户名
							loginPassword:loginVue.loginPassword,//密码
				            ukeyNumber: loginVue.outdata,//密钥
				            requestTime: 2,	
						}
						new ServerInterface(baseInfo.loginInterface).execute(model1, function(jsonObj)
						{
							if(jsonObj.result != "success")
							{
								var isMsg = jsonObj.isMsg;
								console.log("isMsg="+isMsg);
								if(isMsg=="1")
								{
									$(baseInfo.tipsUserloginDiv).modal({
									    backdrop :'static'
								    });
								}
								else
								{
									loginVue.errMsg = jsonObj.info;
									loginVue.ifShowflag = true;
								}
							}
							else
							{
								window.location.href='index.shtml';
							}
							
						});
					}
				}
			}
		});
	}
	
	
	//挤掉在线用户
	function userLoginCommit()
	{
		$(baseInfo.tipsUserloginDiv).modal('hide');
		
		var model = 
		{
			interfaceVersion:loginVue.interfaceVersion,
			theName:loginVue.theName,//用户名
			loginPassword:loginVue.loginPassword,//密码
			ukeyNumber: loginVue.outdata,//密钥
		}
	
		new ServerInterface(baseInfo.loginCommitInterface).execute(model, function(jsonObj)
		{
			if(jsonObj.result != "success")
			{
				
				loginVue.errMsg = jsonObj.info;
				loginVue.ifShowflag = true;
			}
			else
			{
				window.location.href='index.shtml';
			}
		});
		
	}
	
	function initData()
	{
		
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	loginVue.refresh();
	loginVue.initData();
	//------------------------数据初始化-结束----------------//
})({
	"loginDivId":"#wrapBox",
	"tipsUserloginDiv":"#tipsUserlogin",
	"loginInterface":"../Sm_UserLogin",
	"loginCommitInterface":"../Sm_UserLoginCommit",
});
