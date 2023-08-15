/**
 * 点击菜单计算tab高度
 */
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
   // $("#map").css("height",(window.innerHeight - $(".cs-head").height()-$("#myTab").height()-$(".home-top").height()-60)+"px");
  //  $(".cs-content").css("height",(window.innerHeight - $(".cs-head").height()-$("#myTab").height()-33)+"px");
    /*var table = $("#tabContainer .tab-content .active").find($(".el-table"));
    var topHeight = $("#tabContainer .tab-content .active").find($(".content-top")).height();
    table.css("height",$(".cs-content").height()-topHeight);*/
	loadTabHeight();
}