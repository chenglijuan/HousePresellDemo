(function(baseInfo){
	var listVue = new Vue({
		el : baseInfo.listDivId,
		data : {
			interfaceVersion :19000101,
			tg_WorkTimeLimitCheckList:[
				// {
				// 	thetype:'合作协议',
				// 	thesum:'420',
				// 	theovertime:'320'
				// },{
				// 	thetype:'三方协议',
				// 	thesum:'320',
				// 	theovertime:'220'
				// },{
				// 	thetype:'受限额度变更',
				// 	thesum:'156',
				// 	theovertime:'56'
				// }
			],
            typeNames:[],
            sizes:[],
            timeOutCounts:[],
            dateStr:"",
		},
		methods : {
			initData:initData,
			chartBar:chartBar,
            getSearchForm : getSearchForm,
            refresh:refresh,
            handleTimeLimitConfig:handleTimeLimitConfig,
            workTimeDetail:workTimeDetail,
		},
		computed:{
			 
		},
	});

	//------------------------方法定义-开始------------------//

    function getSearchForm()
    {
        return {
            interfaceVersion:this.interfaceVersion,
            dateStr:this.dateStr,
        }
    }
    
    function initButtonList()
	{
		//封装在BaseJs中，每个页面需要控制按钮的就要
		getButtonList();
	}
	
	function initData()
	{
		laydate.render({
			elem: '#date2102010101',
			range:true,
			done: function (value, date) {
                listVue.dateStr = value;
				refresh();
            }
		});
		initButtonList();
	}
	
	function chartBar(){
		option = {
			color:['#4a66c2','#f64e4e'],
		    tooltip : {
		        trigger: 'axis',
		        axisPointer : {
		            type : 'shadow'
		        },
		    },
		    legend: {
		        data:['总业务量','超时业务量'],
		        right:'20',
		        top:'0'
		    },
		    grid: {
		        left: '60',
		        right: '20',
		    },
		    calculable : true,
		    xAxis : [
		        {
		            type : 'category',
		            // data : ['合作协议','三方协议','受限额度变更','托管终止','资金归集','托管资金一般拨付','退房退款','支付保证'],
		            axisLabel:{
		            	fontSize:14
		            }
		        }
		    ],
		    yAxis : [
		        {
		            type : 'value',
		            axisLabel:{
		            	fontSize:14
		            }
		        }
		    ],
		    series : [
		        {
		            name:'总业务量',
		            type:'bar',
		            // data:[420, 320, 156, 230, 256, 767, 135, 162],
		            itemStyle: {
						normal: {
							label: {
								show: true,
								position: 'top',
								textStyle: {
									color: '#4a66c2',
									fontSize: 14
								}
							}
						}
					},
					barWidth:15,
		        },
		        {
		            name:'超时业务量',
		            type:'bar',
		            // data:[320, 220, 56, 130, 156, 567, 95, 92],
		            itemStyle: {
						normal: {
							label: {
								show: true,
								position: 'top',
								textStyle: {
									color: '#f64e4e',
									fontSize: 14
								}
							}
						}
					},
					barWidth:15,
					barGap:'1'
		        }
		    ]
		};
        option.xAxis[0].data = listVue.typeNames;
        option.series[0].data = listVue.sizes;
        option.series[1].data = listVue.timeOutCounts;
		myChart = echarts.init(document.getElementById('chart'));
		myChart.setOption(option);
		setTimeout(function() {
			window.onresize = function() {
				myChart.resize();
			}
		}, 200)
	}

    function refresh()
    {
        new ServerInterface(baseInfo.listInterface).execute(listVue.getSearchForm(), function(jsonObj){
            if(jsonObj.result != "success")
            {
                noty({"text":jsonObj.info,"type":"error","timeout":2000});
            }
            else
            {
                // listVue.tgpf_BuildingRemainRightLogList=jsonObj.tgpf_BuildingRemainRightLogList;
                // listVue.pageNumber=jsonObj.pageNumber;
                // listVue.countPerPage=jsonObj.countPerPage;
                // listVue.totalPage=jsonObj.totalPage;
                // listVue.keyword=jsonObj.keyword;
                // listVue.selectedItem=[];
                // //动态跳转到锚点处，id="top"
                // document.getElementById('tgpf_BuildingRemainRightLogList').scrollIntoView();
                listVue.tg_WorkTimeLimitCheckList = jsonObj.tg_WorkTimeLimitCheckList;
                listVue.tg_WorkTimeLimitCheckList.forEach(function(item){
                    item.size =  addThousands(item.size);
                    item.timeOutCount =  addThousands(item.timeOutCount);
                })
                listVue.typeNames = jsonObj.typeNames;
                listVue.sizes = jsonObj.sizes;
                listVue.timeOutCounts = jsonObj.timeOutCounts;

                listVue.chartBar();
            }
        });
    }
    function handleTimeLimitConfig()
    {
	    enterNewTab("", "办理时限配置", "tg/Tg_HandleTimeLimitConfigList.shtml")
    }
	function workTimeDetail(busiCode)
	{
        enterNewTab(busiCode + "--" + listVue.dateStr.replace(" - ","date"), "工作时限检查详情", "tg/Tg_WorkTimeLimitCheckDetail.shtml")
	}
	//------------------------方法定义-结束------------------//
	
	//------------------------数据初始化-开始----------------//
	listVue.initData();
	listVue.chartBar();
	//------------------------数据初始化-结束----------------//
})({
	"listDivId":"#tg_WorkTimeLimitCheckListDiv",
    "listInterface":"../Tg_WorkTimeLimitCheckList",
});
