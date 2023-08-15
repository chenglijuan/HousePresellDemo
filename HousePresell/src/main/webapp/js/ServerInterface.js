function ServerInterface(serverUrl)
{
	var execute = function(data, callBackFun, method)
	{
		jQuery.ajax({
			dataType:'json',
			contentType:"application/x-www-form-urlencoded;charset=UTF-8",
			type: method||"POST",
			url:serverUrl,
			//"data":data,//当遇到数组数据会默认加上中括号；如idArr，会被拼接成idArr[]=1a&idArr[]=2a；用jQ转换后，才会被拼接成idArr=1a&idArr=2a
			data:jQuery.param(data,true),
			beforeSend:function(){
				if($(".btn-page"))
				{
					$(".btn-page").css("display","none");
					//document.getElementById("pageLoadingDiv").style.display="none";
				}
			},
			complete : function(XMLHttpRequest, textStatus) {
				if($(".btn-page"))
				{
					$(".btn-page").css("display","block");
					//document.getElementById("pageLoadingDiv").style.display="block";
				}
		    },
			success:function(jsonObj){
				if(callBackFun)
				{
					if(jsonObj.info == "该操作需要您先登录！")
					{
						window.location.href = "login.shtml";
					}
					callBackFun(jsonObj);
				}
			},
			error : function (event, XMLHttpRequest, ajaxOptions, thrownError) {
			},
		});
	}
	
	//闭包写法，避免参数污染
	return {
		execute:execute
	}
}
function ServerInterfaceJsonBody(serverUrl)
{
	var execute = function(data, callBackFun, method)
	{
		jQuery.ajax({
			dataType:'json',
			contentType:"application/json;charset=UTF-8",
			type: method||"POST",
			url:serverUrl,
			//"data":data,//当遇到数组数据会默认加上中括号；如idArr，会被拼接成idArr[]=1a&idArr[]=2a；用jQ转换后，才会被拼接成idArr=1a&idArr=2a
			data:JSON.stringify(data),
			success:function(jsonObj){
				if(callBackFun)
				{
					if(jsonObj.info == "该操作需要您先登录！")
					{
						window.location.href = "login.shtml";
					}
					callBackFun(jsonObj);
				}
			},
			error : function (event, XMLHttpRequest, ajaxOptions, thrownError) {
				console.log("请求失败")
			},
		});
	}
	
	//闭包写法，避免参数污染
	return {
		execute:execute
	}
}
//自定义过滤器，来处理小数点
Vue.filter('showFloat',function(value,decimal){
	return value.toFixed(decimal||2);
});
//扩展jquery方法，拷贝对象，指定忽略拷贝的属性#(mgw1986-20161018-Add)
$.extend({
	//拷贝对象，可以设置忽略拷贝属性的类型级名字
	copyProperties:function(obj,ignoreType,ignoreName){
		var result =  {};
		$.each(obj,function(key,value){
			//验证忽略属性类型
			if(typeof(ignoreType) != "undefined"){
				if(value instanceof ignoreType && key != "idArr"){
					return true;
				}
			}
			if(typeof(ignoreName) != "undefined"){
				//验证属性名是否忽略
				if($.inArray(key,ignoreName) >= 0){
					return true;
				}
			}
			result[key] = value;
		});
		return result;
	}
});
//调用方式
//new ServerInterface("userMst/login").execute(LoginInfoVue.$data, function(jsonObj){
//	alert(jsonObj.message);
//});
function ServerInterfaceSync(serverUrl)
{
	var execute = function(data, callBackFun, method)
	{
		jQuery.ajax({
			dataType:'json',
			async: false,
			contentType:"application/x-www-form-urlencoded;charset=UTF-8",
			type: method||"POST",
			url:serverUrl,
			//"data":data,//当遇到数组数据会默认加上中括号；如idArr，会被拼接成idArr[]=1a&idArr[]=2a；用jQ转换后，才会被拼接成idArr=1a&idArr=2a
			data:jQuery.param(data,true),
			success:function(jsonObj){
				if(callBackFun)
				{
					if(jsonObj.info == "该操作需要您先登录！")
					{
						window.location.href = "login.shtml";
					}
					callBackFun(jsonObj);
				}
			},
			error : function(data) {
				console.log('ServerInterfaceSync error');
				console.log(data);
		    },
		});
	}
	
	//闭包写法，避免参数污染
	return {
		execute:execute
	}
}

function ServerUploadInterface(serverUrl)
{
	var execute = function(data, callBackFun, method)
	{
		jQuery.ajax({
			dataType:'json',
			contentType:"application/json;charset=UTF-8",
			type: "POST",
			url:serverUrl,
			data:jQuery.param(data,true),
			beforeSend:function(){
				if($(".btn-page"))
				{
					$(".btn-page").css("display","none");
				}
			},
			complete : function(XMLHttpRequest, textStatus) {
				if($(".btn-page"))
				{
					$(".btn-page").css("display","block");
				}
			},
			success:function(jsonObj){
				if(callBackFun)
				{
					callBackFun(jsonObj);
				}
			},
			error : function (event, XMLHttpRequest, ajaxOptions, thrownError) {
			},
		});
	}

	//闭包写法，避免参数污染
	return {
		execute:execute
	}
}


//new ServerInterfaceSync("userMst/login").execute(LoginInfoVue.$data, function(jsonObj){
//alert(jsonObj.message);
//});

//Array.prototype.indexOf = function (obj) {
//    for (var i = 0; i < this.length; i++) {
//        if (this[i] == obj) {
//            return i;
//        }
//    }
//    return -1;
//}
Array.prototype.add = function(val) {
	var index = this.indexOf(val);
	if (index == -1) {
		this.push(val);
	}
};
Array.prototype.remove = function(val) {
	var index = this.indexOf(val);
	if (index > -1) {
		this.splice(index, 1);
	}
};

var projectName = "HousePresell";
var projectPath = window.document.location.origin + "/";
if(window.document.location.href.indexOf(projectName) > -1)
{
	projectPath += projectName + "/";
}
window.projectPath = projectPath;

loadTabHeight();

function loadTabHeight() {
	var tabHeight = $("#myTab").height();
	var height = 15 + [(tabHeight / 43) - 1] * 43;
	$(".content-wrap").css("padding-top",height + "px");
	$("#empj_BuildingInfoTableDiv").css("padding-top",height + "px");
	$(".pro-wrap").css("padding-top",height + "px");
	//$("#tabContainer").css("padding-top",height + "px");
}

window.onresize = function() {
	$(".sidebar").css("height",(window.innerHeight - $(".cs-head").height()-2) +"px");
    $("#map").css("height",(window.innerHeight - $(".cs-head").height()-$("#myTab").height()-$(".home-top").height()-60)+"px");
    $("#tabBox").css("height",($("#rightBox").height()-$("#rightBoxTop").height()-30)+"px");
   // $(".cs-content").css("height",(window.innerHeight - $(".cs-head").height()-$("#myTab").height()-33)+"px");
	loadTabHeight();
}
