/*
	作者：tony.yang
	时间：2018-08-09
	描述：自定义data标签
*/
			$(document).ready(function() {
				$('[data-href]').each(function(){
					//=============== 解决动态获取SideBar时，IE模式下左侧导航栏404的问题 Start ============//
					//console.info($(this).attr("data-href"));
					var dataHref = $(this).attr("data-href");
					if(dataHref == null)
					{
						return;
					}
					else
					{
						//动态获取SideBar时有用
						if(dataHref.indexOf("_") > 0)
						{
							var userId = dataHref.split("_")[1];
							if(userId == null || userId.length == 0)
							{
								return;
							}
						}
					}
					//=============== 解决动态获取SideBar时，IE模式下左侧导航栏404的问题 End ============//
					
					$(this).html($.ajax({url:$(this).attr("data-href"),async:false}).responseText);
					
					$('ul[data-switchmenu]').each(function() {
						var switchUL = $(this);
						var isonlyoneopen = switchUL.data("isonlyoneopen");
				
						switchUL.find(".nav-parent").click(function() {
							var newNext = $(this).next();
							if(isonlyoneopen)
							{
								var opened = $(".nav-children:visible");
								if(!newNext.is(opened))
								{//刚被点击的，不是之前已经被打开的项-->把之前打开的项，关闭掉
									opened.slideToggle("slow");
									opened.toggleClass("open");
								}
							}
							newNext.slideToggle("slow");
							newNext.toggleClass("open");
				
							$('.nav-children').prev(".nav-parent").children(".icon-sel").css("display", "none");
							$('.nav-children').prev(".nav-parent").children(".icon").css("display", "inline-block");
							$('.nav-children').prev(".nav-parent").children(".nav-text").css("color", "#344964");
							$('.open').prev(".nav-parent").children(".icon-sel").css("display", "inline-block");
							$('.open').prev(".nav-parent").children(".icon").css("display", "none");
							$('.open').prev(".nav-parent").children(".nav-text").css("color", "#d20f1b");
						});
					});
				});
			})