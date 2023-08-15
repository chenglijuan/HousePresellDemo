/**
 * 用于打开（新增）新tab的方法
 *
 * @param tableId Number类型:当前表的tableId
 * @param tabName String类型:新tab标题的名字
 * @param url String类型:显示页面的url，例如：tgxy/Tgxy_BankAccountEscrowedEdit.shtml
 */
function enterNewTab(tableId, tabName, url) {
    var lastIndexOfSlash=url.lastIndexOf("/")
    var lastIndexOfDot=url.lastIndexOf(".")
    var urlName=url.substring(lastIndexOfSlash+1,lastIndexOfDot)
    var allTabs = $("#tabContainer").data("tabs").$element.find(".nav-tabs li a");
    var  flag = true;
    for(var i = 0;i<allTabs.length;i++){
        var tab=allTabs[i]
        var href = allTabs[i].getAttribute("href");
        var indexOfMinus=href.lastIndexOf("_")
        var tabUrl=href.substring(1,indexOfMinus)
        if(tabUrl.indexOf(urlName)!=-1){
            tabUrl=urlName
        }
        if(urlName===tabUrl){
            flag=false
            href=href.substring(1)
            //关闭当前页
            $("#tabContainer").data("tabs").remove(href);
            addNewTab(urlName,tableId,tabName,url)
            break
        }
    }
    if(flag){
        addNewTab(urlName,tableId,tabName,url)
    }

    function addNewTab(urlName,tableId,tabName,url) {
        $("#tabContainer").data("tabs").addTab({
            id: urlName+"_" + tableId,
            text: tabName,
            closeable: true,
            url: url
        });
    }
}

/**
 * 关闭当前tab
 */
function closeCurrentTab() {
    $("#tabContainer").data("tabs").remove($("#tabContainer").data("tabs").getCurrentTabId());
}

/**
 * 用于打开（新增）新tab，并且能够关闭当前tab的方法
 *
 * @param tableId Number类型:当前表的tableId
 * @param tabName String类型:新tab标题的名字
 * @param url String类型:显示页面的url，例如：tgxy/Tgxy_BankAccountEscrowedEdit.shtml
 */
function enterNewTabCloseCurrent(tableId, tabName, url) {
    closeCurrentTab()
    enterNewTab(tableId,tabName,url)
}

/**
 * 列表页进入修改页、详情页
 * @param {Number} tableId
 * @param {String} tabName
 * @param {String} url
 * @param {String} extra 需要传入的值是tableId+业务编码
 */
function enterNextTab(tableId, tabName, url,extra){
	var lastIndexOfSlash=url.lastIndexOf("/")
    var lastIndexOfDot=url.lastIndexOf(".")
    var urlName=url.substring(lastIndexOfSlash+1,lastIndexOfDot);
    var allTabs = $("#tabContainer").data("tabs").$element.find(".nav-tabs li a");
    var  flag = true;
    for(var i = 0;i<allTabs.length;i++){
        if(allTabs[i].firstElementChild.innerHTML == tabName){
        	var href = allTabs[i].getAttribute("href").split("#")[1];
        	$("#tabContainer").data("tabs").remove(href);
        	$("#tabContainer").data("tabs").addTab({
                 id: urlName + "_" + tableId,
                 text: tabName,
                 closeable: true,
                 url: url,
                 extra : extra,
             });
            flag = false;
        }
    }
    if(flag){
        $("#tabContainer").data("tabs").addTab({
            id: urlName + "_" + tableId,
            text: tabName,
            closeable: true,
            url: url,
            extra : extra,
        });
    }
}
/**
 * 关闭当前页面进入下一个页面，例如：详情页进入修改页、点击新增保存按钮或者修改保存按钮
 * @param {Number} tableId
 * @param {String} tabName
 * @param {String} url
 * @param {String} extra  需要传入的值是tableId+业务编码
 */
function enterNext2Tab(tableId, tabName, url,extra){
	var lastIndexOfSlash=url.lastIndexOf("/")
    var lastIndexOfDot=url.lastIndexOf(".")
    var urlName=url.substring(lastIndexOfSlash+1,lastIndexOfDot);
    $("#tabContainer").data("tabs").remove($("#tabContainer").data("tabs").getCurrentTabId());
    var allTabs = $("#tabContainer").data("tabs").$element.find(".nav-tabs li a");
    var  flag = true;
    for(var i = 0;i<allTabs.length;i++){
        var extraFlag = allTabs[i].getAttribute("data-extra");
        if(extraFlag !=undefined){
            if(extraFlag === extra){
                $("#tabContainer").data("tabs").showTab(urlName + "_" + tableId);
                flag = false;
            }
        }

    }
    if(flag){
        $("#tabContainer").data("tabs").addTab({
            id: urlName + "_" + tableId,
            text: tabName,
            closeable: true,
            url: url,
            extra : extra,
        });
    }
}
/**
 * 进入详情tab的方法，例如从列表进入修改界面
 *
 * @param itemList 数组类型：整个列表的list
 * @param tabName String类型：新标签的名称
 * @param url String类型：显示页面的url，例如：tgxy/Tgxy_BankAccountEscrowedEdit.shtml
 * @param errorNote String类型：错误提示
 */
function enterDetail(itemList, tabName, url, errorNote) {
    if (itemList.length == 1) {
        var tableId = itemList[0]
        enterNewTab(tableId, tabName, url)
    } else {
        noty({"text": errorNote, "type": "error", "timeout": 2000});
    }
}

/**
 * 从列表中删除一个item的方法
 *
 * @param baseDeleteInterface String类型：删除接口的url，例如：baseInfo.deleteInterface（其值为：../Tgxy_BankAccountEscrowedDelete）
 * @param tableId Number类型：当前item的tableId
 * @param onDelete function类型：删除成功执行的回调，为一个function
 */
function oneDelete(baseDeleteInterface, tableId, onDelete,interfaceVersion) {
    if(interfaceVersion===undefined)
    {
        interfaceVersion=19000101
    }
    var model = {
        interfaceVersion: interfaceVersion,
        tableId: tableId,
    };

    noty({
        layout: 'center',
        modal: true,
        text: "确认删除吗？",
        type: "confirm",
        buttons: [
            {
                addClass: "btn btn-primary",
                text: "确定",
                onClick: function ($noty) {
                    $noty.close();
                    new ServerInterface(baseDeleteInterface).execute(model, function (jsonObj) {
                        if (jsonObj.result != "success") {
                        	generalErrorModal(jsonObj)
                        }
                        else {
                            onDelete(jsonObj)
                        }
                    });
                }
            },
            {
                addClass: "btn btn-danger",
                text: "取消",
                onClick: function ($noty) {
                    $noty.close();
                }
            }
        ]
    });
}

/**
 * 从列表中删除一个item的方法
 * @param listVue Vue类型，当前List界面的Vue
 * @param poName String类型，po的名字
 * @param onDelete function类型，删除成功后的回调
 * @param interfaceVersion Number类型，接口的序号
 * @param onError function类型，删除失败后的回调
 */
function oneDelete2(listVue,poName, onDelete,interfaceVersion,onError) {
    if(interfaceVersion===undefined)
    {
        interfaceVersion=19000101
    }
    var form = {
        interfaceVersion: interfaceVersion,
        tableId: listVue.selectItem[0],
        idArr:listVue.selectItem,
    };
    serverRequest("../"+poName+"Delete",form,onDelete,function(jsonObj){
        if(onError===undefined){
            generalErrorModal(jsonObj)
        }else{
            onError(jsonObj)
        }
    })

}

/**
 * 从列表中批量删除的方法
 *
 * @param baseDeleteInterface String类型：删除批量接口的url
 * @param form Object类型：传入的删除form，必须带有interfaceVersion
 * @param onDelete function类型：删除成功后的回调
 * @param tip String类型：删除提示
 */
function batchDelete(baseDeleteInterface, form, onDelete, tip) {
    if (tip == undefined) {
        tip = "确认删除吗？"
    }
    noty({
        layout: 'center',
        modal: true,
        text: tip,
        type: "confirm",
        buttons: [{
            addClass: "btn btn-primary",
            text: "确定",
            onClick: function ($noty) {
                $noty.close();
                new ServerInterface(baseDeleteInterface).execute(form, function (jsonObj) {
                    if (jsonObj.result != "success") {
                    	generalErrorModal(jsonObj);
                    }
                    else {
                        onDelete(jsonObj)
                    }
                });
            }
        }, {
            addClass: "btn btn-danger",
            text: "取消",
            onClick: function ($noty) {
                $noty.close();
            }
        }]
    });
}

/**
 * 从列表中批量删除的方法
 * @param listVue Vue类型，当前List界面的Vue
 * @param poName String类型，po的名字
 * @param onDelete function类型，删除成功后的回调
 * @param interfaceVersion Number类型，接口的序号
 * @param onError function类型，删除失败后的回调
 */
function batchDelete2(listVue,poName, onDelete,interfaceVersion,onError) {
    if(interfaceVersion===undefined)
    {
        interfaceVersion=19000101
    }
    var form={
        interfaceVersion:interfaceVersion,
        idArr:listVue.selectItem
    }
    serverRequest("../"+poName+"BatchDelete",form,onDelete,function (jsonObj) {
        if(onError===undefined){
            generalErrorModal(jsonObj)
        }else{
            onError(jsonObj)
        }
    })
}

/**
 * 通用删除方法，包括单独一个item删除和批量删除
 * @param listVue Vue类型，列表的listVue
 * @param noSelectTip String类型，没有选择任何一个选项提示信息（已弃用）
 * @param batchDeleteUrl String类型，批量删除item的接口URL
 * @param oneDeleteUrl String类型，单独一个item删除接口的URL
 * @param onBatchDeleteSuccess function类型，批量删除成功后的回调
 * @param onOneDeleteSuccess function类型，单独一个删除后的回调
 * @param interfaceVersion Number类型，版本号
 */
function generalDelete(listVue,noSelectTip,batchDeleteUrl,oneDeleteUrl,onBatchDeleteSuccess,onOneDeleteSuccess,interfaceVersion) {
    var selectItem=listVue.selectItem
    var interfaceVersionTemp
    if(interfaceVersion===undefined){
        interfaceVersionTemp=19000101
    }else{
        interfaceVersionTemp=interfaceVersion
    }
    var batchForm={interfaceVersion:interfaceVersionTemp,idArr:listVue.selectItem}
    if(listVue.selectItem.length == 0)
    {
        // noty({"text":"请选择要删除的托管账号","type":"error","timeout":2000});
        noty({"text":noSelectTip,"type":"error","timeout":2000});
    }
    else if (listVue.selectItem.length > 1)
    {
        batchDelete(batchDeleteUrl,batchForm,function (jsonObj) {
            onBatchDeleteSuccess(jsonObj)
            // listVue.selectItem=[]
            // refresh()
        })
    }
    else
    {
        var theId = listVue.selectItem[0];
        oneDelete(oneDeleteUrl,theId,function (jsonObj) {
            onOneDeleteSuccess(jsonObj)
        })
    }
}

/**
 * 通用删除的方法
 * @param listVue Vue类型，当前List界面的Vue
 * @param poName String类型，po的名字
 * @param onDelete function类型，删除成功后的回调
 * @param interfaceVersion Number类型，接口的序号
 * @param onError function类型，删除失败后的回调
 */
function generalDelete2(listVue,poName,onDelete,interfaceVersion,onError) {
    if(onDelete===undefined){
        onDelete=function () {
            generalSuccessModal()
            listVue.refresh()
        }
    }
    if(listVue.selectItem.length==1){
        oneDelete2(listVue,poName,onDelete,interfaceVersion,onError)
    }else{
        batchDelete2(listVue,poName,onDelete,interfaceVersion,onError)
    }
}

/**
 * 通用删除的方法
 * @param listVue Vue类型，当前List界面的Vue
 * @param poName String类型，po的名字
 * @param onDelete function类型，删除成功后的回调
 * @param interfaceVersion Number类型，接口的序号
 * @param onError function类型，删除失败后的回调
 */
function generalDelete3(listVue,poName,onDelete,interfaceVersion,onError) {
    if(onDelete===undefined){
        onDelete=function () {
            generalSuccessModal()
            listVue.refresh()
        }
    }
    if(interfaceVersion===undefined)
    {
        interfaceVersion=19000101
    }
    var form={
        interfaceVersion:interfaceVersion,
        idArr:listVue.selectItem
    }
    serverRequest("../"+poName+"Delete",form,onDelete,function (jsonObj) {
        if(onError===undefined){
            generalErrorModal(jsonObj)
        }else{
            onError(jsonObj)
        }
    })
}

/**
 * 获取当前精确到天的日期格式
 *
 * @returns {string}
 */
function getDayDate() {
    var date = new Date();
    var seperator1 = "-";
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = year + seperator1 + month + seperator1 + strDate;
    return currentdate;
}

/**
 * 获得当前精确到秒的日期格式
 *
 * @returns {string}
 */
function getDetailDate() {
    var date = new Date();
    var sign1 = "-";
    var sign2 = ":";
    var year = date.getFullYear() // 年
    var month = date.getMonth() + 1; // 月
    var day = date.getDate(); // 日
    var hour = date.getHours(); // 时
    var minutes = date.getMinutes(); // 分
    var seconds = date.getSeconds() //秒
    var weekArr = ['星期一', '星期二', '星期三', '星期四', '星期五', '星期六', '星期天'];
    var week = weekArr[date.getDay()];
    // 给一位数数据前面加 “0”
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (day >= 0 && day <= 9) {
        day = "0" + day;
    }
    if (hour >= 0 && hour <= 9) {
        hour = "0" + hour;
    }
    if (minutes >= 0 && minutes <= 9) {
        minutes = "0" + minutes;
    }
    if (seconds >= 0 && seconds <= 9) {
        seconds = "0" + seconds;
    }
    var currentdate = year + sign1 + month + sign1 + day + " " + hour + sign2 + minutes + sign2 + seconds + " " + week;
    return currentdate;
}

/**
 * 时间格式化
 */
//对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
// 例子：
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

/**
 * 时间戳转精确到天的日期格式YYYY-MM-DD
 *
 * @param timestamp Number类型：时间戳
 */
function timeStamp2DayDate(timestamp) {
    return moment(timestamp).format("YYYY-MM-DD");
}

/**
 * 时间戳转精确到秒的日期格式YYYY-MM-DD hh:mm:ss
 *
 * @param timestamp Number类型：时间戳
 */
function timeStamp2DetailDate(timestamp) {
    return moment(timestamp).format("YYYY-MM-DD hh:mm:ss");
}

function getTabIdArray(containerId) {
    var tableIdStr = ""
    if (containerId == "") {
        tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
    } else {
        tableIdStr = $("#" + containerId).data("tabs").getCurrentTabId();
    }
    return tableIdStr.split("_");
}

/**
 * 从Tab中获取到tableId
 *
 * @param containerId String类型：tab的ID
 * @param onSuccess function类型：获取id成功后的回调，参数中包含了id
 * @param onFail function类型：id获取失败后的回调
 */
function getIdFormTab(containerId, onSuccess, onFail) {
    // var tableIdStr = ""
    // if (containerId == "") {
    //     tableIdStr = $("#tabContainer").data("tabs").getCurrentTabId();
    // } else {
    //     tableIdStr = $("#" + containerId).data("tabs").getCurrentTabId();
    // }
    var array = getTabIdArray(containerId)
    if (array.length > 1) {
        var id = array[array.length-1]
        if (typeof (onSuccess) !== undefined) {
            onSuccess(id)
        }
    } else {
        if (typeof (onFail) !== undefined) {
            onFail()
        }
    }
}

function getPoNameFromTab( onSuccess, onFail) {
    var poName=""
    var array = getTabIdArray("")
    if (array.length > 1) {
        for(var i=3;i<array.length;i++){
            poName+=array[i]+"_"
        }
        poName=poName.substring(0,poName.length-1)
        onSuccess(poName)
        // var id = array[array.length-1]
        // if (typeof (onSuccess) !== undefined) {
        //     onSuccess(id)
        // }
    } else {
        if (typeof (onFail) !== undefined) {
            onFail()
        }
    }
}

/**
 * 封装了服务端请求的方法
 *
 * @param serverUrl String类型：服务端后台接口的url，例如：baseInfo.detailInterface
 * @param form Object类型：请求的form
 * @param onSuccess function类型：成功获取信息后的回调
 * @param onError function类型：获取失败后的回调
 */
function serverRequest(serverUrl, form, onSuccess, onError) {
    new ServerInterface(serverUrl).execute(form, function (jsonObj) {
        if (jsonObj.result != "success") {
            if (onError == undefined) {
                // noty({"text": jsonObj.info, "type": "error", "timeout": 2000});
                generalErrorModal(jsonObj)
            } else {
                onError(jsonObj)
            }
        }
        else {
            onSuccess(jsonObj)
        }
    });
}

/**
 * 封装了服务端请求的方法
 *
 * @param serverUrl String类型：服务端后台接口的url，例如：baseInfo.detailInterface
 * @param form Object类型：请求的form
 * @param onSuccess function类型：成功获取信息后的回调
 * @param onError function类型：获取失败后的回调
 */
function serverBodyRequest(serverUrl, form, onSuccess, onError) {
    new ServerInterfaceJsonBody(serverUrl).execute(form, function (jsonObj) {
        if (jsonObj.result != "success") {
            if (onError == undefined) {
                // noty({"text": jsonObj.info, "type": "error", "timeout": 2000});
                generalErrorModal(jsonObj)
            } else {
                onError(jsonObj)
            }
        }
        else {
            onSuccess(jsonObj)
        }
    });
}

/**
 * 一般处理左侧序号的方法，请参考其他列表的js是怎么做的，目前已经批量生成
 *
 * @param index Number类型：列表中的index
 * @param listVue Object类型：当前的vue object
 * @returns {number}
 */
function generalIndexMethod(index, listVue) {
    if (listVue.pageNumber > 1) {
        return (listVue.pageNumber - 1) * listVue.countPerPage - 0 + (index - 0 + 1);
    }
    if (listVue.pageNumber <= 1) {
        return index - 0 + 1;
    }
}

MAX_VALUE = 2147483647

/**
 * 查询列表中所有信息的form
 *
 * @param interfaceVersion Number类型：接口版本号
 * @returns {{interfaceVersion: *, pageNumber: number, countPerPage: number, theState: number, totalPage: number, keyword: string}}
 */
function getTotalListForm(interfaceVersion) {
    if(interfaceVersion===undefined){
        interfaceVersion=19000101
    }
    if(interfaceVersion===""){
        interfaceVersion=19000101
    }
    return {
        interfaceVersion: interfaceVersion,
        pageNumber: 1,
        countPerPage: MAX_VALUE,
        theState: 0,
        totalPage: 1,
        keyword: ""
    }
}

/**
 * 处理列表中被选中的item，主要用于“修改”按钮的可选和不可选
 *
 * @param listVue Object类型：当前的vue object
 * @param list 数组类型：整个列表的list
 */
function generalListItemSelectHandle(listVue, list)
{
    listVue.selectItem = [];
    for (var index = 0; index < list.length; index++) {
        var element = list[index].tableId;
        listVue.selectItem.push(element)
    }
    if(listVue.diabaleShowLog!=undefined){
        if(listVue.selectItem.length==1){
            listVue.diabaleShowLog=false
        }else{
            listVue.diabaleShowLog=true
        }
    }
}

/**
 * 处理列表中被选中的item，用于不带tableId的list，比如受限额度节点列表，这个是个临时列表，没有tableId
 *
 * @param listVue Object类型：当前的vue object
 * @param list 数组类型：整个列表的list
 */
function generalListItemSelectWholeItemHandle(listVue, list)
{
    listVue.selectItem = [];
    for (var index = 0; index < list.length; index++) {
        var element = list[index];
        listVue.selectItem.push(element)
    }
}


function calIndex(index, pageNumber, countPerPage) {
    if (pageNumber > 1) {
        return (pageNumber - 1) * countPerPage - 0 + (index - 0 + 1);
    }
    if (pageNumber <= 1) {
        return index - 0 + 1;
    }
}

/**
 * 将数字表示的BusiState转为文字
 * @param stateNumber Number类型，BusiState
 * @returns {string}
 */
function number2BusiState(stateNumber) {
    switch (stateNumber) {
        case "1": {
            return "申请中"
        }
        case "2": {
            return "已备案"
        }
        case "3": {
            return "已删除"
        }
        default : {
            return "未定义"
        }
    }
}

/**
 * 将文字表示的BusiState转为数字
 * @param stateString String类型，BusiState的文字表示
 * @returns {number}
 */
function busiState2Number(stateString) {
    switch (stateString) {
        case "申请中": {
            return 1
        }
        case "已备案": {
            return 2
            break
        }
        case "已删除": {
            return 3
            break
        }
        default: {
            return 0
        }

    }
}

/**
 * 是否启用用的方法
 * @param isUseNumber isUse的数字形式
 * @returns {string} 返回是或者否
 */
function isUseNumber2String(isUseNumber) {
    var temp=isUseNumber.toString()
    switch (temp){
        case "0":{
            return "是"
        }
        case "1":{
            return "否"
        }
    }
}

/*************ErrorCheck Begin***************/

/**
 * 根据传进来的字符串选择相应的正则表达式返回
 *
 * @param checkStr	传进来的字符串
 * @param reg 		返回的正则表达式
 */

function getErrorCheckReg(checkStr) {
    var reg;
    switch (checkStr) {
        case "phoneNumberCheck": 	//验证手机号
            reg = /^(1[3,4,5,7,8])\d{9}$/;
            break;
        case "emailCheck": 			//验证邮箱
            reg = /^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$/;
            break;
        case "userNameCheck": 		//验证姓名
            reg = /^[\u4E00-\u9FA5]{2,}$/;
            break;
        case "passwordID":          //验证身份证号
        	reg = /^\d{6}(18|19|20)?\d{2}(0[1-9]|1[012])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i;
            break;
        default:
            reg = checkStr;
            break;
    }
    return reg;
}

/**
 * 身份证号的检查
 *
 * @param event		单个需要检查的输入框的target
 *
 */
function passErrorCheck(event, model) {
    if(model == "00") {
    	errorCheck(event);
    	passCheck($(event));
    }else {
    	 $("#" + $(event).attr('data-errorInfoId')).hide();
    	 $(event).removeClass("red-input");
         return true;
    }
}
function errorCheck(event)
{
    var $_item = $(event);

    var regStrList =  $_item.attr('data-reg').split("&&");
    for (var i = 0; i < regStrList.length; i++ )
    {
        if (regStrList[i] == "IsNull")
        {
            //非空判断
            if ($_item.val()==null || $_item.val() == "") {
                errorCheckIsNull(event);
                return false;
            }

            if (regStrList.length == 1)
            {
                $("#" + $_item.attr('data-errorInfoId')).hide();
                $_item.removeClass("red-input");
                return true;
            }
        }
        else
        {
            //如果进到这个地方说明可以为空，或者不能为空但是已判断，是空直接返回True
            if ($_item.val()==null || $_item.val() == "") {
                $("#" + $_item.attr('data-errorInfoId')).hide();
                $_item.removeClass("red-input");
                return true;
            }

            var regStr = getErrorCheckReg(regStrList[i]);
            //如果data-reg为空说明没什么特殊需要判断的要求，比如正则
            if ($_item.attr('data-reg') == "" || $_item.attr('data-reg') == null) {

                $("#" + $_item.attr('data-errorInfoId')).hide();
                $_item.removeClass("red-input");
                return true;
            }

            var reg = new RegExp(regStr);
            if (!reg.test($_item.val())) {
                $("#" + $_item.attr('data-errorInfoId')).show();
                $("#" + $_item.attr('data-errorInfoId')).text($_item.attr('data-error'));
                $_item.addClass("red-input");
                return false;
            } else {
                $("#" + $_item.attr('data-errorInfoId')).hide();
                $_item.removeClass("red-input");
                return true;
            }
        }
    }
}

function passCheck(event) {
	if(event.val().length == 18) {
		var code = event.val().split('');
		var factor = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ];
		var parity = [ 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 ];
		var sum = 0;
    	var ai = 0;
    	var wi = 0;
    	for (var i = 0; i < 17; i++) {
        	ai = code[i];
        	wi = factor[i];
        	sum += ai * wi;
    	}
    	var last = parity[sum % 11];
    	if(last != code[17]){
    		$("#" + event.attr('data-errorInfoId')).show();
            $("#" + event.attr('data-errorInfoId')).text(event.attr('data-error'));
            event.addClass("red-input");
        	return false;
        }
	}
}

/**
 * 输入框为空，返回对应报错信息
 *
 * @param event		单个需要检查的输入框的target
 *
 */

function errorCheckIsNull(event)
{
    var $_item = $(event);

    $("#" + $_item.attr('data-errorInfoId')).show();
    $("#" + $_item.attr('data-errorInfoId')).text($_item.attr('data-empty'));
    $_item.addClass("red-input");
}

/**
 * 整个Div下需要判断的所有 绑定 data-errorInfoId 属性的输入框的检查
 *
 * @param divStr	整体需要检查的Div的ID
 * @param isPass	是否通过整体检查，通过则继续请求，不通过则直接return (具体参照Emmp_CompanyInfoEdit.js的addOrgMember方法)
 *
 */

function errorCheckForAll (divStr)
{
    var errorCheck_div = document.getElementById(divStr);
    var errorCheckList = errorCheck_div.querySelectorAll('[data-errorInfoId]');
    var isPass = true;

    for (var i = 0 ; i < errorCheckList.length; i ++ )
    {
        if (!errorCheck(errorCheckList[i]) && isPass) {

            isPass = false;
        }
    }

    return isPass;
}

function errorCheckForAll1 (divStr,model)
{
    var errorCheck_div = document.getElementById(divStr);
    var errorCheckList = errorCheck_div.querySelectorAll('[data-errorInfoId]');
    var str = errorCheckList[2];
    var isPass = true;
    if(model == "00") {
    	if (!passErrorCheck(str, model)) {
            isPass = false;
        }
	}else {
		$("#" + $(str).attr('data-errorInfoId')).hide();
		$(str).removeClass("red-input");
	}
    for (var i = 0 ; i < errorCheckList.length; i ++ )
    {
        if (errorCheckList[i].placeholder != "请输入证件号码" && !errorCheck(errorCheckList[i]) && isPass) {

            isPass = false;
        }
    }

    return isPass;
}

/*************ErrorCheck End***************/


/**
 * 所有金额相关的数字显示千分位，将数字转换成千分位形式（用逗号隔开）
 * @param {Object} number
 * 1.小数过多显示两位小数 123 =>123.00,123.=>123.00,123.454545=>123.45
 * 2.原本是千分位修改之后，先将千分位转成不含“,”的字符串，再转成千分位
 * 3.输入的为0显示为0.00
 * 4.带负号的显示负数的千分位
 */
function addThousands(number) {
	//return Number(number).toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, '$1,');
	if(null == number || number == "") return "0.00";
/*    if(number==0){
        return number+".00";
    }else if(number==0.0){
        return number+".00";
    }*/
   
    var num = number + "";
    num = num.replace(new RegExp(",","g"),"");//默认是千分位的，先将逗号去除
    // 正负号处理
    var symble = "";
    if(/^([-+]).*$/.test(num)) {
        symble = num.replace(/^([-+]).*$/,"$1");//得到正负号
        num = num.replace(/^([-+])(.*)$/,"$2");
    }
    if(/^[0-9]+(\.[0-9]+)?$/.test(num) || /^[0-9]+(\.+)?$/.test(num)) {
        var num = num.replace(new RegExp("^[0]+","g"),"");
        num = Number(num).toFixed(2);//保留后面两位小数
        if(/^\./.test(num)) {
            num = "0" + num;
        }
        var decimal = num.replace(/^[0-9]+(\.[0-9]+)?$/,"$1");//得到小数部分
        var integer= num.replace(/^([0-9]+)(\.[0-9]+)?$/,"$1");//得到整数部分
        var re=/(\d+)(\d{3})/;
        while(re.test(integer)){
            integer = integer.replace(re,"$1,$2");
        }
        if(decimal === ""){
            return symble + integer + ".00";
        }else{
            return symble + integer + decimal;
        }
    }else{
    	return number;
    }/*else if(/^[0-9]+(\.)?$/.test(num)){
    	var num = num.replace(new RegExp("^[0]+","g"),"");
        num = Number(num).toFixed(2);//保留后面两位小数
    	console.log(num)
    	if(/^\./.test(num)) {
            num = "0" + num;
        }
        var decimal = num.replace(/^[0-9]+(\.[0-9]+)?$/,"$1");//得到小数部分
        var integer= num.replace(/^([0-9]+)(\.[0-9]+)?$/,"$1");//得到整数部分
        var re=/(\d+)(\d{3})/;
        while(re.test(integer)){
            integer = integer.replace(re,"$1,$2");
        }
        if(decimal === ""){
            return symble + integer + ".00";
        }else{
            return symble + integer + decimal;
        }
    }*/
}
function addThousandsCount(number) {
	if(null == number || number == "") return "0";
    var num = number + "";
    num = num.replace(new RegExp(",","g"),"");//默认是千分位的，先将逗号去除
    // 正负号处理
    var symble = "";
    if(/^([-+]).*$/.test(num)) {
        symble = num.replace(/^([-+]).*$/,"$1");//得到正负号
        num = num.replace(/^([-+])(.*)$/,"$2");
    }
    if(/^[0-9]+(\.[0-9]+)?$/.test(num) || /^[0-9]+(\.+)?$/.test(num)) {
        var num = num.replace(new RegExp("^[0]+","g"),"");
        if(/^\./.test(num)) {
            num = "0" + num;
        }
        var decimal = num.replace(/^[0-9]+(\.[0-9]+)?$/,"$1");//得到小数部分
        var integer= num.replace(/^([0-9]+)(\.[0-9]+)?$/,"$1");//得到整数部分
        var re=/(\d+)(\d{3})/;
        while(re.test(integer)){
            integer = integer.replace(re,"$1,$2");
        }
        if(decimal === ""){
            return symble + integer;
        }else{
            return symble + integer + decimal;
        }
    }else{
    	return number;
    }
}
/**
 * 将千分位的金额转为字符串
 * @param {Object} num
 */
function commafyback(num)
{
    var amount
    if(typeof(num) == 'string'){
        amount = num.replace(/,/g,"");
    }else{
        amount = num;
    }
    return amount;
}
/**
 * 千分位保留两位小数 by qian.wang
 * @param {Object} num
 */
function thousandsToTwoDecimal(num)
{
    return Number(num).toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, '$1,');
}

/**
 *小数转百分比（小数点后保留两位）
 */
function toPercent(point){
    var str=Number(point*100).toFixed(2);
    str+="%";
    return str;
}


/**
 * 页面加载显示企业信息
 * @param {Object} listVue
 * @param {Object} companyNameInterface
 * @param {Object} company
 * @param {Object} errMsg
 * @param {Object} edModelDivId
 */
function loadCompanyName(listVue,companyNameInterface,company,errMsg,edModelDivId) {
    var model = {
        interfaceVersion : listVue.interfaceVersion,
    }
    new ServerInterface(companyNameInterface).execute(model, function(jsonObj) {
        if (jsonObj.result != "success") {
        	generalErrorModal(jsonObj,jsonObj.info);
        } else {
            company(jsonObj);
        }
    });
}
/**
 * 改变企业信息带出项目名称
 * @param {Object} listVue
 * @param {Object} changeCompanyInterface
 * @param {Object} projectFun
 * @param {Object} errMsg
 * @param {Object} edModelDivId
 */
function changeCompanyHandle(listVue,changeCompanyInterface,projectFun,errMsg,edModelDivId) {
    var model = {
        interfaceVersion : listVue.interfaceVersion,
        developCompanyId : listVue.companyId
    }
    new ServerInterface(changeCompanyInterface).execute(model, function(jsonObj) {
        if (jsonObj.result != "success") {
        	generalErrorModal(jsonObj,jsonObj.info);
        } else {
            //qs_projectNameList = jsonObj.empj_ProjectInfoList;
            projectFun(jsonObj);
        }
    });
}
/**
 * 选择项目名称带出楼幢信息
 * @param {Object} listVue
 * @param {Object} changeProjectInterface
 * @param {Object} buildingNum
 * @param {Object} errMsg
 * @param {Object} edModelDivId
 */
function changeProjectHandle(listVue,changeProjectInterface,buildingNum,errMsg,edModelDivId) {
    var model = {
        interfaceVersion : listVue.interfaceVersion,
        projectId : listVue.projectId
    }
    new ServerInterface(changeProjectInterface).execute(model, function(jsonObj) {
        if (jsonObj.result != "success") {
        	generalErrorModal(jsonObj,jsonObj.info);
        } else {
            buildingNum(jsonObj);
        }
    });
}

/**
 * 匹配行的内容是否一样，进行合并单元格
 * @param {Object} cache
 * @param {Object} cacheIndex
 * @param {Object} cacheData
 * @param {Object} colData
 * @param {Object} list
 */
function combine(cache,cacheIndex,cacheData,colData,list){
    list.forEach(function(res,i){
        cacheData[i] = [];
        colData.forEach(function(item,j){
            if (i === 0) {
                cacheData[0][j] = 1;
                cache = JSON.parse(JSON.stringify(res));
                cacheIndex[j] = 0;
            } else {
                if(res[item.prop] === cache[item.prop] && item.prop !=='index'){
                    cacheData[cacheIndex[j]][j]++;
                    cacheData[i][j] = 0
                }else{
                    cache[item.prop] = res[item.prop];
                    cacheIndex[j] = i;
                    cacheData[i][j] = 1;
                }
            }
        })
    })
}
/**
 * 合并单元格的操作
 * @param {Object} cacheDataArr
 * @param {Object} checkRow
 * @param {Object} checkColumn
 */
function checkSpanMethod(cacheDataArr,checkRow,checkColumn){
    if(cacheDataArr.length<=0){
        return false
    }
    var colNum = cacheDataArr[checkRow][checkColumn];
    if (colNum < 2) {
        return {
            rowspan: colNum,
            colspan: colNum
        }
    } else {
        return {
            rowspan: colNum,
            colspan: 1
        }
    }
}

/**
 * 将状态数字转为文字
 *
 * @param obj
 */
function theState2String(obj) {
    if(obj.theState!==undefined){
        var theState=obj.theState
        switch (theState){
            case 0:
                return "正常"
            case 1:
                return "已删除"
        }
    }else{
        return "未知"
    }
}

/**
 * 将数字转为房屋类型
 * @param obj
 * @returns {string}
 */
function houseType2String(obj) {
    if(obj.theType!==undefined){
        var theType=obj.theType
        switch (theType){
            case "0":
                return "毛坯房"
            case "1":
                return "成品房"
            default:
                return "未知类型"
        }
    }else{
        return "未知"
    }
}

/**
 * js封装map集合
 */
function Map() {
    var struct = function(key, value) {
        this.key = key;
        this.value = value;
    }

    var put = function(key, value){
        for (var i = 0; i < this.arr.length; i++) {
            if ( this.arr[i].key === key ) {
                this.arr[i].value = value;
                return;
            }
        }
        this.arr[this.arr.length] = new struct(key, value);
    }

    var get = function(key) {
        for (var i = 0; i < this.arr.length; i++) {
            if ( this.arr[i].key === key ) {
                return this.arr[i].value;
            }
        }
        return null;
    }

    var remove = function(key) {
        var v;
        for (var i = 0; i < this.arr.length; i++) {
            v = this.arr.pop();
            if ( v.key === key ) {
                continue;
            }
            this.arr.unshift(v);
        }
    }

    var size = function() {
        return this.arr.length;
    }

    var isEmpty = function() {
        return this.arr.length <= 0;
    }
    this.arr = new Array();
    this.get = get;
    this.put = put;
    this.remove = remove;
    this.size = size;
    this.isEmpty = isEmpty;
}


/**
 *
 * @param listVue Vue类型，列表的listVue
 * @param onReset function类型，回调方法，当输入框后执行的方法
 */
function generalResetSearch(listVue,onReset) {
    listVue.keyword = "";
    listVue.pageNumber = 1;
    // if(listVue.orderBy!=undefined){
    //     listVue.orderBy=""
    // }
    onReset()
}

// 获取相同编号的数组
function getOrderNumber(list, OrderIndexArr) {
    var OrderObj = {};
    list.forEach(function (element, index) {
        element.rowIndex = index;
        if (OrderObj[element.orderNumber]) {
            OrderObj[element.orderNumber].push(index);
        } else {
            OrderObj[element.orderNumber] = [];
            OrderObj[element.orderNumber].push(index);
        }
    }, this);
    for (var k in OrderObj)
    {
        if (OrderObj[k].length > 1)
        {
            OrderIndexArr.push(OrderObj[k]);
        }
    }
}

/**
 * 弹出错误信息模态框，如果jsonObj存在就只执行jsonObj，如果想自定义错误信息，则第一个参数中填""即可
 * @param jsonObj 返回信息中的Json数据
 * @param customErrorMsg 自定义错误消息
 */
function generalErrorModal(jsonObj,customErrorMsg) {
    if(jsonObj===undefined || jsonObj===null || jsonObj.info===undefined || jsonObj.info.length===0){
        if(customErrorMsg!==undefined){
            $("#errorMsgModal").html(customErrorMsg)
        }else{
            $("#errorMsgModal").html("操作失败")
        }
    }else{
        $("#errorMsgModal").html(jsonObj.info)
    }
    $("#fm").css("z-index",1000000);
    $("#fm").modal('show',{
        backdrop :'static'
    });
}

/**
 * 弹出成功模态框
 */
function generalSuccessModal() {
    $("#sm").modal('show',{
        backdrop :'static'
    });
}

/**
 * 审批操作 - 弹出成功模态框
 */
function approvalSuccessModal() {
    $("#approvalTheAcction").css("z-index",1000000);
    $("#approvalTheAcction").modal('show',{
        backdrop :'static'
    });
}

/**
 * 弹出提示框模态框的方法
 * @param confirmClick function类型，点击确定按钮后执行的方法
 * @param tipMsg String类型，弹框提示语
 * @param cancelClick function类型，点击取消按钮后执行的方法
 *
 */
function generalSelectModal(confirmClick,tipMsg,cancelClick) {
    $('#selectConfirmBtn').unbind()
    $('#selectConfirmBtn').click(confirmClick)
    $('#selectCancelBtn').unbind()
    $('#selectCancelBtn').click(cancelClick)
    if(tipMsg==undefined){
        tipMsg="默认提示信息"
    }
    $("#selectMsgModal").html(tipMsg)
    $("#selectM").modal('show', {
        backdrop: 'static'
    });
}

/**
 * 弹出删除item的模态框
 * @param listVue Vue类型，当前List界面的Vue
 * @param poName String类型，po的名字
 * @param onDelete function类型，删除成功后的回调
 * @param oneDeleteMsg 单个删除的提示语
 * @param batchDeleteMsg 批量删除的提示语
 * @param inferfaceVersion Number类型，删除接口的版本号
 * @param onError function类型，删除失败后的回调
 */
function generalDeleteModal(listVue, poName, onDelete, oneDeleteMsg, batchDeleteMsg, inferfaceVersion, onError) {
    // generalListItemSelectHandle(listVue,listVue.selectItem)
    if (oneDeleteMsg === undefined) {
        oneDeleteMsg = "确认删除吗？"
    }
    if (batchDeleteMsg === undefined) {
        batchDeleteMsg = "确认批量删除吗？"
    }
    var tipMsg=""
    if(listVue.selectItem.length>1){
        tipMsg=batchDeleteMsg
    }else{
        tipMsg=oneDeleteMsg
    }
    generalSelectModal(function(){
        generalDelete2(listVue, poName, onDelete, inferfaceVersion, onError)
    },tipMsg)
}

//审批中心 - 我发起的 -删除
function generalDeleteModal2(listVue, poName, onDelete, oneDeleteMsg, batchDeleteMsg, inferfaceVersion, onError) {
    if (oneDeleteMsg === undefined) {
        oneDeleteMsg = "确认删除吗？"
    }
    var tipMsg=oneDeleteMsg;
    generalSelectModal(function(){
        if(onDelete===undefined)
        {
            onDelete=function () {
                generalSuccessModal();
                listVue.refresh();
            }
        }
        var form={
            interfaceVersion:19000101,
            idArr:listVue.selectItem,
        }
        serverBodyRequest("../"+poName+"BatchDelete",form,onDelete,function (jsonObj) {
            if(onError===undefined){
                generalErrorModal(jsonObj)
            }else{
                onError(jsonObj)
            }
        })
    },tipMsg)
}


function generalHideModal(divId) {
    $(divId).modal('hide');
}

/**
 * 上传附件信息
 * @param vue Vue类型，当前页面的Vue
 * @param poName String类型，po的名字，例如Emmp_BankInfo
 * @param addFileUrl String类型，提交的接口URL，一般为baseInfo.attachmentBatchAddInterface，attachmentBatchAddInterface的值为"../Sm_AttachmentBatchAdd"
 */
function generalUploadFile(vue, poName, addFileUrl) {
    var form = {
        interfaceVersion: vue.interfaceVersion,
        busiType: poName,
        sourceId: vue.tableId,
        attachmentList: vue.$refs.listenUploadData.uploadData,
    };
    serverBodyRequest(addFileUrl, form, function () {
        generalSuccessModal()
    }, function (jsonObj) {
        generalErrorModal(jsonObj)
    })
}

/**
 *
 * @param vue Vue类型，当前页面的Vue
 * @param poName String类型，po的名字，例如Emmp_BankInfo
 * @param loadFileUrl String类型，获取数据的接口URL，一般为baseInfo.loadUploadInterface，loadUploadInterface的值为"../Sm_AttachmentCfgList"
 */
function generalLoadFile(vue, poName, loadFileUrl) {
    var form = {
        pageNumber: '0',
        busiType: poName,
        sourceId: vue.tableId,
        interfaceVersion: vue.interfaceVersion
    };
    serverRequest(loadFileUrl, form, function (jsonObj) {
        vue.loadUploadList = jsonObj.sm_AttachmentCfgList;
    }, function (errObj) {
        generalErrorModal(errObj)
    })
}

/**
 * 初始化附件信息并加载当前界面的附件信息
 * @param vue Vue类型，当前页面的Vue
 * @param busiType String类型，业务编号
 */
function generalLoadFileTest(vue, busiType) {
    var form = {
        pageNumber:"0",
        busiType : busiType,
        interfaceVersion:vue.interfaceVersion,
        reqSource : vue.flag == true ? "详情" : "",//判断是否是详情页是则显示旧的附件材料
        sourceId:vue.tableId,
        approvalCode:vue.approvalCode,
        afId:vue.afId,
    };
    vue.loadUploadList=[];
    serverRequest("../../Sm_AttachmentCfgList",form,function (jsonObj) {
        vue.loadUploadList= jsonObj.sm_AttachmentCfgList;
    },function (jsonObj) {
        generalErrorModal(jsonObj)
    })
}

/**
 * 通用导出Excel的方法
 * @param listVue Vue类型，列表页面的vue
 * @param poName String类型，po的名字
 * @param onSuccess function类型，成功后的回调
 */
function generalExportExcel(listVue, poName,onSuccess) {
    serverRequest("../"+poName+"ExportExcel",{interfaceVersion:listVue.interfaceVersion,idArr:listVue.selectItem},function (jsonObj) {
        window.location.href=jsonObj.fileDownloadPath;
        listVue.selectItem = [];
        onSuccess()
        // refresh();
    },function(jsonObj){
        generalErrorModal(jsonObj)
    })
}

//根据按钮权限，动态控制，该页面的所有“可控按钮”的显示与否
function getButtonList()
{
	//勿删！！！！
	//先从后台获取最新数据，然后将Json数据传送给BaseJs中checkBtnNeedShow方法
	//此处的值为，从后台数据库请求到的按钮权限数组
	var model = {
		interfaceVersion : 19000101,
	};
	/*console.info(model);*/
	new ServerInterface("../Sm_Permission_UserBtnList").execute(model, function(jsonObj)
	{
		if(jsonObj.result != "success")
		{
			generalErrorModal(jsonObj);
		}
		else
		{
			checkBtnNeedShow(jsonObj);
		}
	});
}

/**
 * 从 Sm_Permission_UserBtnListService 中获取按钮权限数据
 *
 * @param needShowBtnList，需要显示的按钮编号数组
 * 数据格式形如：{"needShowBtnList":["0001","0002","0003","0004"]}
 */
function checkBtnNeedShow(jsonObj)
{
	//console.log(jsonObj);
    var needShowBtnList = [];
    if(jsonObj != null)
    {
        needShowBtnList = jsonObj.needShowBtnList;
        if(needShowBtnList == null || needShowBtnList == undefined)
        {
            needShowBtnList = [];
        }
    }
    $('[data-editNum]').each(function(){
        if(needShowBtnList == null || needShowBtnList.length == 0)
        {
            //隐藏
            $(this).hide();
        }
        else
        {
            if(needShowBtnList.indexOf($(this).attr("data-editNum")) > 0)
            {
                //显示
                $(this).show();
            }
           else
            {
                //隐藏
                $(this).hide();
            }
        }
    });
    $('[data-showNum]').each(function(){
        if(needShowBtnList == null || needShowBtnList.length == 0)
        {
            //隐藏
            $(this).hide();
        }
        else
        {
            if(needShowBtnList.indexOf($(this).attr("data-showNum")) > 0)
            {
            	 //显示
                $(this).show();
            }
            else
            {
                //隐藏
                $(this).hide();
            }
        }
    });
    $('[data-editTime]').each(function(){
    	//console.log(needShowBtnList);
        if(needShowBtnList == null || needShowBtnList.length == 0)
        {
            //隐藏
            $(this).hide();
        }
        else
        {
            if(needShowBtnList.indexOf($(this).attr("data-editTime")) > 0)
            {
            	 //显示
                $(this).show();
            }else{
            	 //隐藏
                $(this).hide();
            }
        }
    });
}

/**
 * 获取登录的用户信息
 * @param onGetUser function类型，获取user信息后的回调
 */
function getLoginUserInfo(onGetUser) {
    var form = {
        interfaceVersion : 19000101,
    };
    new ServerInterfaceSync("../Sm_UserGet").execute(form, function (jsonObj) {
        if (jsonObj.result != "success")
        {
            generalErrorModal(jsonObj);
            onGetUser(undefined)
        }
        else
        {
            onGetUser(jsonObj.sm_User)
        }
    });
}

TEMPLATE_PO_PATH="zhishusz.housepresell.util.objectdiffer.model."
DEFAULT_PO_PATH="zhishusz.housepresell.database.po."
function enterLogTab(poName,packagePath,listVue) {
    // console.log('poName is '+poName)
    var tableId=listVue.selectItem[0]
    // console.log('log tableId is '+tableId)
    if(packagePath!=undefined){

    }else{
        packagePath=DEFAULT_PO_PATH
    }
    packagePath=packagePath.replace(/\./g,"_")
    poName=poName+"-"+packagePath
    poName=poName+"-"+tableId
    enterNewTabTest(poName,"变更日志","test/Test_BusiState_LogList.shtml")
}

function enterLogTabWithTemplate(poName,listVue) {
    enterLogTab(poName+"Template",TEMPLATE_PO_PATH,listVue)
}

/**
 * 通过Json的方式对对象进行深拷贝（目前还未使用）
 * @param obj 需要拷贝的原对象
 * @returns {any} 拷贝完成后的对象
 */
function jsonDeepCopy(obj) {
    var jsonStr=JSON.stringify(obj)
    return JSON.parse(jsonStr)
}

/**
 * 获取选择类型
 * @param paramType String类型，参数的类型，详细请看常量信息汇总.xlsx
 * @param onSuccess function类型，回调函数，回调一个参数list
 */
function generalGetParamList(paramType,onSuccess,interfaceVersion) {
    if(interfaceVersion==undefined){
        interfaceVersion=19000101
    }
    var form={
        interfaceVersion:interfaceVersion,
        theState:0,
        parametertype : paramType
    }
    serverRequest("../../Sm_BaseParameterForSelect",form,function (jsonObj) {
        if(onSuccess!=undefined){
            onSuccess(jsonObj.sm_BaseParameterList)
        }
    })
}

/**
 * 获取公共参数信息
 * @param onGetUser function类型，获取user信息后的回调
 */
function getParameter(parameterType, theValue, onSuccess) {
    var form = {
        interfaceVersion : 19000101,
        parametertype : parameterType,
        theValue : theValue,
        theState : 0,
    };
    new ServerInterfaceSync("../Sm_BaseParameterGet").execute(form, function (jsonObj) {
        if (jsonObj.result != "success")
        {
            generalErrorModal(jsonObj);
        }
        else
        {
        	onSuccess(jsonObj.sm_BaseParameter);
        }
    });
}

//根据业务类型跳转到对应的页面
//注：sourcePage  1：待办流程列表页面 -> 审批详情页面
//                2 :已办流程列表页面，我发起的列表页面 -> 审批详情页面
function approvalDetailFromHomeTest(busiType,sourceId,afId,workflowId,sourcePage) {
    var theId = sourceId + "_" + afId + "_" + workflowId+"_"+sourcePage;
    switch (busiType) {
        case "开发企业注册":
            enterNewTabTest(theId, "开发企业注册审批", "test/Test_ApprovalProcess_CompanyInfoDetail.shtml");
            break;
        case "开发企业变更":
            enterNewTabTest(theId, "开发企业变更审批", "test/Test_ApprovalProcess_CompanyInfoDetail.shtml");
            break;
        case "代理公司注册":
            enterNewTabTest(theId, "代理公司注册审批", "test/Test_ApprovalProcess_CompanyAgencyDetail.shtml");
            break;
        case "代理公司变更":
            enterNewTabTest(theId, "代理公司变更审批", "test/Test_ApprovalProcess_CompanyAgencyDetail.shtml");
            break;
        case "进度见证服务单位注册":
            enterNewTabTest(theId, "进度见证服务单位注册审批", "test/Test_ApprovalProcess_CompanyWitnessDetail.shtml");
            break;
        case "进度见证服务单位变更":
            enterNewTabTest(theId, "进度见证服务单位变更审批", "test/Test_ApprovalProcess_CompanyWitnessDetail.shtml");
            break;
        case "合作机构注册":
            enterNewTabTest(theId, "合作机构注册审批", "test/Test_ApprovalProcess_CompanyCooperationDetail.shtml");
            break;
        case "合作机构变更":
            enterNewTabTest(theId, "合作机构变更审批", "test/Test_ApprovalProcess_CompanyCooperationDetail.shtml");
            break;
        case "项目信息注册与备案":
            enterNewTabTest(theId, "项目信息注册与备案", "test/Test_ApprovalProcess_ProjectInfoDetail.shtml");
            break;
        case "项目信息变更与备案":
            enterNewTabTest(theId, "项目信息变更与备案", "test/Test_ApprovalProcess_ProjectInfoDetail.shtml");
            break;
        case "楼幢信息变更维护":
            enterNewTabTest(theId, "楼幢信息变更维护审批", "test/Test_ApprovalProcess_BuildingDetail.shtml");
            break;
        case "楼幢信息初始维护（含楼幢、单元、户室信息维护）":
            enterNewTabTest(theId, "楼幢信息初始维护审批", "test/Test_ApprovalProcess_BuildingDetail.shtml");
            break;
        case "托管终止":
            enterNewTabTest(theId, "托管终止", "test/Test_ApprovalProcess_BldEscrowCompletedDetail.shtml");
            break;
        case "托管标准版本管理":
            enterNewTabTest(theId, "托管标准版本管理", "test/Test_ApprovalProcess_EscrowStandardVerMngDetail.shtml");
            break;
        case "退房退款申请-贷款已结清":
            enterNewTabTest(theId, "退房退款申请审批", "test/Test_RefundInfoDetail.shtml");
            break;
        case "贷款托管合作协议签署":
            enterNewTabTest(theId, "贷款托管合作协议签署审批", "test/Test_EscrowAgreementDetail.shtml");
            break;
        case "贷款三方托管协议签署":
            enterNewTabTest(theId, "贷款三方托管协议签署审批", "test/Test_TripleAgreementDetail.shtml");
            break;
        case "退房退款申请-贷款未结清":
            enterNewTabTest(theId, "退房退款申请-贷款未结清审批", "test/Test_RefundInfoUnclearedDetail.shtml");
            break;
        case "支付保证申请与复核":
            enterNewTabTest(theId, "支付保证申请与复核审批", "test/Test_PaymentGuaranteeApplicationDetail.shtml");
            break;

        case "支付保证撤销":
            enterNewTabTest(theId, "支付保证撤销审批", "test/Test_CancelPaymentGuaranteeApplicationDetail.shtml");
            break;

        case "三方协议版本管理":
            enterNewTabTest(theId, "三方协议版本管理审批", "test/Test_TripleAgreementVerMngDetail.shtml");
            break;

        case "三方协议计量结算":
            enterNewTabTest(theId, "三方协议计量结算审批", "test/Test_CoopAgreementSettleDtlDetail.shtml");
            break;

        case "合作协议版本管理":
            enterNewTabTest(theId, "合作协议版本管理审批", "test/Test_CoopAgreementVerMngDetail.shtml");
            break;

        case "用款申请与复核":
            enterNewTabTest(theId, "用款申请与复核审批", "test/Test_ApprovalProcess_FundAppropriatedAFDetail.shtml");
            break;

        case "统筹与复核":
            enterNewTabTest(theId, "统筹与复核审批", "test/Test_ApprovalProcess_FundOverallPlanDetail.shtml");
            break;

        case "资金拨付":
            enterNewTabTest(theId, "资金拨付审批", "test/Test_ApprovalProcess_FundAppropriatedDetail.shtml");
            break;

        case "备案价格初始维护":
            enterNewTabTest(theId, "备案价格初始维护审批", "test/Test_ApprovalProcess_AvgPriceDetail.shtml");
            break;

        case "备案价格变更维护":
            enterNewTabTest(theId, "备案价格变更维护审批", "test/Test_ApprovalProcess_AvgPriceDetail.shtml");
            break;

        case "受限额度变更":
            enterNewTabTest(theId, "受限额度变更", "test/Test_ApprovalProcess_BldLimitAmountDetail.shtml");
            break;
        case "受限额度节点版本管理":
            enterNewTabTest(theId, "受限额度节点版本管理", "test/Test_ApprovalProcess_BldLimitAmountVerDetail.shtml");
            break;

        case "存单存入管理":
            enterNewTabTest(theId, "存单存入审批", "test/Test_ApprovalProcess_DepositManagementDetail.shtml");
            break;
        case "存单提取管理":
            enterNewTabTest(theId, "存单提取审批", "test/Test_ApprovalProcess_DepositManagementTakeOutDetail.shtml");
            break;
        case "托管特殊拨付管理":
            enterNewTabTest(theId, "托管特殊拨付管理审批", "test/Test_SpecialFundAppropriatedDetail.shtml");
            break;
        default:
    }
}

/**
 * 获取排序信息
 * pro: 排序的字段
 * order: 排序的类型
 */
function getOrderByInfo(pro, order)
{
	var orderBy = null;
	if(pro != null)
	{
		//ElementUI 排序参数获取到的排序信息
		if(order == "ascending")
		{
			//升序排序（由小到大）
			orderBy = pro+" asc";
		}
		//ElementUI 排序参数获取到的排序信息
		else if(order == "descending") 
		{
			//降序排序（由大到小）
			orderBy = pro+" desc";
		}
	}
	return orderBy;
}

function generalOrderByColumn(column) {
    return getOrderByInfo(column.prop, column.order);
}

//获取当前的日期时间 格式“yyyy-MM-dd”
function getNowFormatDate() {
    var date = new Date();
    var seperator = "-";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator + month + seperator + strDate;
    return currentdate;
}

/**
 * 用于解决在IE模式下，其加载速度大于Vue请求数据的速度导致左侧SideBar404
 * 	作者：tony.yang
 * 	时间：2018-08-09
 * 	描述：自定义data标签
 */
function loadSideBarFile()
{
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
}
//判断是否为千分位字符
function isThousandsRealNum(val)
{
	var re = /^\d{1,3}(,?\d{3})*(\.\d{1,2})?$/;
	
	return re.test(val);
}
/**
 * 判断附件是否为必须上传的
 * @author jianping.yao
 * @param {Object} loadUploadList
 * 2018.11.12
 */
function confirmFile(loadUploadList){
	for(var i = 0;i<loadUploadList.length;i++){
		if(loadUploadList[i].isNeeded == true){
			if(loadUploadList[i].smAttachmentList.length == 0){
				generalErrorModal(null,loadUploadList[i].theName+"未上传,此附件为必须上传附件"); 
				return false;
			}
		}
	}
	return true;
}
/**
 * 匹配参数
 * @author qian.wang
 * @param str,data
 * 2018.12.10
 */
function matchParam(str, data, model) {
	for(var i = 0;i < str.length;i++) {
		for(var item in data){ 
	        if(item == str[i]){
	        	createJson(item, data[item], model);
	            break;
	        } 
	    }
	}
}
function createJson(prop, val, model) {
    if(typeof val === "undefined") {
        delete model[prop];
    }
    else {
    	model[prop] = val;
    }
}


//----------------------------elementUItab------------------------//
function enterNewTabTest(tableId,title,url)
{
    var newMenu = {
        tableId:tableId,
        title:title,
        content:url,
    }
    elementUIAddTab(newMenu);
    window.indexTabsElementVue.dataTableId = tableId;
}

function elementUIAddTab(menu)
{
    var randomnumber=Math.floor(Math.random()*100000);
    var url = menu.content+"?randomnumber="+randomnumber;

    var tabs = window.indexTabsElementVue.elementUITabs;
    var urlArray = menu.content.split('/');
    var lastIndex = urlArray[urlArray.length-1].split('.');
    //判断是否是新增页面或者编辑页面
    var length = lastIndex[0].length;
    var isDetailOrEditOrList = lastIndex[0].substring(length-4,length);
    var isAdd = lastIndex[0].substring(length-3,length);
    //判断是否是列表页面
    if(isDetailOrEditOrList == 'List')
    {
        for (var i = 0; i < tabs.length; i++)
        {
            var tabs_content = tabs[i].content.split('?')[0];

            if(tabs_content == menu.content)
            {
                tabs[i].content = url;
                window.indexTabsElementVue.activeName = tabs[i].name;
                return;
            }
        }
    }
    //判断是否是同一条记录
    for (var index = 0; index < tabs.length; index++)
    {
        var tabs_content = tabs[index].content.split('?')[0];
        if(menu.content == tabs_content && menu.tableId == tabs[index].tableId)
        {
            if(isAdd != 'Add' && isDetailOrEditOrList !='Edit')
            {
                tabs[index].content = url;
            }
            window.indexTabsElementVue.activeName = tabs[index].name;
            return;
        }
    }
    var newTabName = ++window.indexTabsElementVue.tabIndex + '';
    window.indexTabsElementVue.elementUITabs.push({
        tableId:menu.tableId,
        title: menu.title,
        name: newTabName,
        content:url,
    });
    window.indexTabsElementVue.activeName = newTabName;
}

//删除tab
function elementUIRemoveTab(targetName)
{
    var tabs = window.indexTabsElementVue.elementUITabs;
    for (var index = 0; index < tabs.length; index++)
    {
        if (tabs[index].name == targetName)
        {
            var tab_content = tabs[index].content.split('?')[0];
            var urlArray = tab_content.split('/');
            var lastIndex = urlArray[urlArray.length-1].split('.');
            //判断是否是新增页面或者编辑页面
            var length = lastIndex[0].length;
            var isAdd = lastIndex[0].substring(length-3,length);
            var isEdit = lastIndex[0].substring(length-4,length);
            if(isAdd == 'Add' || isEdit == 'Edit')
            {
                $('#messageModal').modal(
                    {
                        backdrop: 'static',
                        keyboard: false,
                    }
                );
                window.indexTabsElementVue.activeName = tabs[index].name;
                window.indexTabsElementVue.tabs_index = index;
                return;
            }
            var nextTab = tabs[index + 1] || tabs[index - 1];
            if (nextTab) {
                window.indexTabsElementVue.activeName = nextTab.name;
            }
            window.indexTabsElementVue.elementUITabs.splice(index,1);
            break;
        }
    }
}

function enterNewTabAndCloseCurrent(tableId,title,content)
{
    var tabs = window.indexTabsElementVue.elementUITabs;
    var activeName =  window.indexTabsElementVue.activeName;
    window.indexTabsElementVue.elementUITabs = tabs.filter(function (tab) {
        return tab.name != activeName;
    });
    enterNewTabTest(tableId,title,content);
}

function getTabElementUI_TableId()
{
    return window.indexTabsElementVue.dataTableId+'';
}

function getUserName() {
    return window.indexVue.userName;
}

function  getUserId() {
    return window.indexVue.userId;
}

function closeAddOrEdit()
{
    $('#messageModal').hide();
    $('#messageModal').removeData('bs.modal');
    $('.modal-backdrop').remove();
    var tabs = window.indexTabsElementVue.elementUITabs;
    var tabs_index = window.indexTabsElementVue.tabs_index;
    var nextTab = tabs[tabs_index + 1] || tabs[tabs_index - 1];
    if (nextTab) {
        window.indexTabsElementVue.activeName = nextTab.name;
    }
    window.indexTabsElementVue.elementUITabs.splice(tabs_index,1);
}


//----------------------------elementUItab------------------------//