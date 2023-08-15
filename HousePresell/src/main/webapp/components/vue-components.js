(function() {
	// --------------前端‘页码’组件
	var pageNavigationTemplate = '<div class="btn-page">'+
		'<div v-if="totalpage>1" class="inline">' +
		'<div v-if="totalpage>1" class="inline skip">' +
		'		<span>每页显示</span>'+
		'		<input class="text-center" v-model="countperpage" @blur="countPerPageChange">' +
		'		<span>条</span>'+
		'		<div class="inline btn-total">'	+
		'		<span>共<strong class="text-danger">{{totalcount}}</strong>条数据</span>'	+
		'		</div>'	+
		'</div>'+
		'		<button v-if="pagenumber!=1" v-on:click="jumpToPage(1)" type="button" class="btn btn-default"> 首页 </button>' +
		'		<button v-if="pagenumber==1" type="button" class="btn btn-default" disabled> 首页 </button>' +
		'		<button v-if="pagenumber!=1" v-on:click="lastPage" type="button" class="btn btn-default"> < 上一页 </button>' +
		'		<button v-if="pagenumber==1" type="button" class="btn btn-default" disabled> < 上一页 </button>' +
		'		<button v-on:click="jumpToPage(1)" type="button" class="btn" v-bind:class="{\'btn-active\': pagenumber == 1,\'btn-default\': pagenumber != 1}">1</button>' +

		'		<button v-if="pagenumber>6" type="button" class="btn btn-default">...</button>' +
		'		<button v-for="index in indexs" v-on:click="jumpToPage(index)" type="button" class="btn" v-bind:class="{\'btn-active\':pagenumber == index,\'btn-default\': pagenumber != index}">{{index}}</button>' +
		'		<button v-if="pagenumber<totalpage-1-sidepagecount" type="button" class="btn btn-default">...</button>' +

		'		<button v-on:click="jumpToPage(totalpage)" type="button" class="btn" v-bind:class="{\'btn-active\': pagenumber == totalpage,\'btn-default\': pagenumber != totalpage}">{{totalpage}}</button>' +
		'		<button v-if="pagenumber!=totalpage" v-on:click="nextPage" type="button" class="btn btn-default"> 下一页 > </button>' +
		'		<button v-if="pagenumber==totalpage" type="button" class="btn btn-default" disabled> 下一页 > </button>' +
		'		<button v-if="pagenumber!=totalpage" v-on:click="jumpToPage(totalpage)" type="button" class="btn btn-default"> 尾页 </button>' +
		'		<button v-if="pagenumber==totalpage" type="button" class="btn btn-default" disabled> 尾页 </button>' +
		'</div>' +
		'<div v-if="totalpage>1" class="inline skip">' +
		'		<span>跳转至</span>'+
		'		<input v-model="pages" class="text-center" @blur="jumpPage">' +
		'		<span>页</span>'+
		// ' <button v-on:click="jumpPage" type="button" class="btn btn-default
		// btn-sure">确定</button>' +
		'</div>'+
		'</div>';

	var PageNavigationVue = Vue.extend({
		template: pageNavigationTemplate,
		props: ['pagenumber', 'totalpage', 'sidepagecount', 'totalcount' , 'countperpage'],
		data: {
			pages : 1,
		},
		computed: {
			indexs: function() {
				this.pages = this.pagenumber;
				var left = this.pagenumber-this.sidepagecount;// 1-4=-3
				var right = this.pagenumber - this.sidepagecount + (this.sidepagecount*2);// 1+4=5
				if(left < 2)
				{
					right = right + (2-left);
					left = 2;
				}
				if(right > this.totalpage - 1)
				{
					left = left - (right - (this.totalpage - 1));
					if(left < 2)
					{
						left = 2;
					}
					right = this.totalpage - 1;
				}
				var ar = [];
				while(left <= right) {
					ar.push(left);
					left++;
				}
				return ar;
			}
		},
		methods: {
			jumpToPage: function(data) {
				if(data != this.pagenumber) {
					this.pagenumber = data;
					this.pageChange();
				}
			},
			lastPage: function() {
				this.pagenumber--;
				this.pageChange();
			},
			nextPage: function() {
				this.pagenumber++;
				this.pageChange();
			},
			jumpPage: function() {
				if(this.pages < 1 || this.pages == null || parseInt(this.pages)!=this.pages)
				{
					this.pagenumber = 1;
					this.pages = 1;
				}
				this.pagenumber = this.pages;

				this.pageChange();
			},
			pageChange: function() {
				this.$emit("pagechange", this.pagenumber);
			},
			countPerPageChange: function() {
				if(this.countperpage == null || parseInt(this.countperpage)!=this.countperpage)
				{
					this.countperpage = 20;
				}
				this.$emit("countperpagechange", this.countperpage);
			}
		},
	});

	window.PageNavigationVue = PageNavigationVue;

	// --------------前端‘下拉菜单’组件
	/*
	 * 用法1：通过URL获取数据，URL可以是一个动态的值，只要有变化就会重新获取数据并刷新下拉菜单 <vue-select
	 * defaultname="请选择机构类型" :thevalue="theType"
	 * :optionurl="'http://localhost/HousePresell/js/test'+具体关联参数+'.json'"
	 * @callbackfun="changeTheType"></vue-select> 用法2：通过传入list获取数据 <vue-select
	 * defaultname="请选择机构类型" :thevalue="theType" :optionlist="theTypeList"
	 * @callbackfun="changeTheType"></vue-select>
	 */

	var selectTemplate = '<el-select class="block" :disabled="avail" filterable @change="valueChange" v-model="thevalue">'
		+ '	<el-option value="">{{defaultname}}</el-option>'
		+ '	<el-option v-if="options" v-for="opt in options" :label="opt.theName" :value="opt.tableId">{{opt.theName}}</el-option>'
		+ '</el-select>';

	var VueSelect = Vue.extend({
		template : selectTemplate,
		props : ['thevalue', 'optionlist', 'optionurl', 'defaultname', 'avail'],
		data : function() {
			return {
				options : []
			}
		},
		methods : {
			valueChange : function() {
				var _theValue = this.thevalue;

				if (this.thevalue) {
					var optionSelected = this.options.filter(function (optionItem) {
						return optionItem.tableId == _theValue;
					})

					if(optionSelected.length > 0)
					{
						this.$emit("callbackfun", optionSelected[0]);
					}
				}
				else
				{
					this.$emit("emptybackfun", null);
				}
			}
		},
		mounted : function() {
			if (this.optionurl)
			{
				// url返回数据格式参考：[{theValue:"10",theName:"招投标单位"}]
				var jsonObj = $.ajax({url : this.optionurl, async : false}).responseText;

				/*
				 * setTimeout(() => { this.options = eval("(" + jsonObj + ")"); },
				 * 100);
				 */

				var _this = this;
				setTimeout(function(){
					_this.options = eval("(" + jsonObj + ")");
				}, 100);
			}
			else if (this.optionlist)
			{
				this.options = this.optionlist;
			}
		},
		watch:{
			optionurl:function(curVal, oldVal){
				if(curVal != oldVal)
				{
					if (this.optionurl)
					{
						// url返回数据格式参考：[{theValue:"10",theName:"招投标单位"}]
						var jsonObj = $.ajax({url : this.optionurl, async : false}).responseText;
						var _this = this;
						setTimeout(function(){
							_this.options = eval("(" + jsonObj + ")");
						}, 100);
					}
				}
			},
			optionlist:function(curVal, oldVal){
				if(curVal != oldVal)
				{
					if (this.optionlist)
					{
						this.options = this.optionlist;
					}
				}
			}
		}
	});

	window.VueSelect = VueSelect;

	// --------------列表页面前端‘下拉菜单’组件
	/*
	 * 用法1：通过URL获取数据，URL可以是一个动态的值，只要有变化就会重新获取数据并刷新下拉菜单 <vue-listselect
	 * defaultname="请选择机构类型" :thevalue="theType"
	 * :optionurl="'http://localhost/HousePresell/js/test'+具体关联参数+'.json'"
	 * @callbackfun="changeTheType"></vue-listselect> 用法2：通过传入list获取数据
	 * <vue-listselect defaultname="请选择机构类型" :thevalue="theType"
	 * :optionlist="theTypeList" @callbackfun="changeTheType"></vue-listselect>
	 */

	var selectTemplate_list = '<el-select class="listSelect" :disabled="avail" filterable @change="valueChange" v-model="thevalue">'
		+ '	<el-option value="">{{defaultname}}</el-option>'
		+ '	<el-option v-if="options" v-for="opt in options" :label="opt.theName" :value="opt.tableId">{{opt.theName}}</el-option>'
		+ '</el-select>';

	var VueListSelect = Vue.extend({
		template : selectTemplate_list,
		props : ['thevalue', 'optionlist', 'optionurl', 'defaultname', 'avail'],
		data : function() {
			return {
				options : []
			}
		},
		methods : {
			valueChange : function() {
				var _theValue = this.thevalue;

				if (this.thevalue) {
					var optionSelected = this.options.filter(function (optionItem) {
						return optionItem.tableId == _theValue;
					})

					if(optionSelected.length > 0)
					{
						this.$emit("callbackfun", optionSelected[0]);
					}
				}
				else
				{
					this.$emit("emptybackfun", null);
				}
			}
		},
		mounted : function() {
			if (this.optionurl)
			{
				// url返回数据格式参考：[{theValue:"10",theName:"招投标单位"}]
				var jsonObj = $.ajax({url : this.optionurl, async : false}).responseText;

				/*
				 * setTimeout(() => { this.options = eval("(" + jsonObj + ")"); },
				 * 100);
				 */

				var _this = this;
				setTimeout(function(){
					_this.options = eval("(" + jsonObj + ")");
				}, 100);
			}
			else if (this.optionlist)
			{
				this.options = this.optionlist;
			}
		},
		watch:{
			optionurl:function(curVal, oldVal){
				if(curVal != oldVal)
				{
					if (this.optionurl)
					{
						// url返回数据格式参考：[{theValue:"10",theName:"招投标单位"}]
						var jsonObj = $.ajax({url : this.optionurl, async : false}).responseText;
						var _this = this;
						setTimeout(function(){
							_this.options = eval("(" + jsonObj + ")");
						}, 100);
					}
				}
			},
			optionlist:function(curVal, oldVal){
				if(curVal != oldVal)
				{
					if (this.optionlist)
					{
						this.options = this.optionlist;
					}
				}
			}
		}
	});

	window.VueListSelect = VueListSelect;
// --------------列表页面前端‘下拉菜单’组件

	/*
	 * 用法1：通过URL获取数据，URL可以是一个动态的值，只要有变化就会重新获取数据并刷新下拉菜单长度修改 <vue-listselect
	 * defaultname="请选择机构类型" :thevalue="theType"
	 * :optionurl="'http://localhost/HousePresell/js/test'+具体关联参数+'.json'"
	 * @callbackfun="changeTheType"></vue-listselect> 用法2：通过传入list获取数据
	 * <vue-listselect defaultname="请选择机构类型" :thevalue="theType"
	 * :optionlist="theTypeList" @callbackfun="changeTheType"></vue-listselect>
	 */

	var searchTemplate_list = '<el-select class="listSearch" :disabled="avail" filterable @change="valueChange" v-model="thevalue">'
		+ '	<el-option value="">{{defaultname}}</el-option>'
		+ '	<el-option v-if="options" v-for="opt in options" :label="opt.theName" :value="opt.tableId">{{opt.theName}}</el-option>'
		+ '</el-select>';

	var VueListSearch = Vue.extend({
		template : searchTemplate_list,
		props : ['thevalue', 'optionlist', 'optionurl', 'defaultname', 'avail'],
		data : function() {
			return {
				options : []
			}
		},
		methods : {
			valueChange : function() {
				var _theValue = this.thevalue;

				if (this.thevalue) {
					var optionSelected = this.options.filter(function (optionItem) {
						return optionItem.tableId == _theValue;
					})

					if(optionSelected.length > 0)
					{
						this.$emit("callbackfun", optionSelected[0]);
					}
				}
				else
				{
					this.$emit("emptybackfun", null);
				}
			}
		},
		mounted : function() {
			if (this.optionurl)
			{
				// url返回数据格式参考：[{theValue:"10",theName:"招投标单位"}]
				var jsonObj = $.ajax({url : this.optionurl, async : false}).responseText;

				/*
				 * setTimeout(() => { this.options = eval("(" + jsonObj + ")"); },
				 * 100);
				 */

				var _this = this;
				setTimeout(function(){
					_this.options = eval("(" + jsonObj + ")");
				}, 100);
			}
			else if (this.optionlist)
			{
				this.options = this.optionlist;
			}
		},
		watch:{
			optionurl:function(curVal, oldVal){
				if(curVal != oldVal)
				{
					if (this.optionurl)
					{
						// url返回数据格式参考：[{theValue:"10",theName:"招投标单位"}]
						var jsonObj = $.ajax({url : this.optionurl, async : false}).responseText;
						var _this = this;
						setTimeout(function(){
							_this.options = eval("(" + jsonObj + ")");
						}, 100);
					}
				}
			},
			optionlist:function(curVal, oldVal){
				if(curVal != oldVal)
				{
					if (this.optionlist)
					{
						this.options = this.optionlist;
					}
				}
			}
		}
	});

	window.VueListSearch = VueListSearch;
	// --------------前端‘附件上传’组件
	var fileUploadNavigationTemplate = '<div>' +
		'<div class="detail-title" v-if="isShowUpload">' +
		'    <h1 class="inline">附件材料</h1>' +
		'<div class="inline"  style="margin:0px 10px" v-if="showdelete == true"><button class="btn btn-success" @click="showHighMeterModel()">'+
		'<img src="../image/detail_btn_ico_save.png" width="18">'+
		'<span>高拍仪</span>'+
		'</button>'+
		 '</div>'+
		 // '<a style="margin:0px 10px" v-show="downLoadFlag == true" v-if="isShowBatchBtn" href="javascript:void(0)"></a>'+
		'<a style="margin:0px 10px" v-show="downLoadFlag == true" v-if="isShowBatchBtn" href="javascript:void(0)" @click="batchDownload"><button class="btn btn-success"><img src="../../image/detail_btn_ico_download.png" width="18"><span>批量下载</span></button></a>'+
		 '</div>' +
		'<div class="cs-content" v-if="isShowUpload">' +
		'    <div class="detail-content">' +
		'<div class="col-md-12 text-right" ></div>' +
		'    <div class="form-group" v-for="(item,index) in loaduploadlist">' +
		'        <div class="detail-text2 leftText" style="display:flex">' +
		'            <span v-show="item.isNeeded" class="text-danger" style="margin-right:5px">*</span>' +
		'            <span>{{item.theName}}</span>' +
		'        </div>' +
		'        <div class="row attachments-wrap">' +
		// ' <div @click="getIndex(index)"
		// class="col-md-9 text-left">' +
		'            <div @click="getIndex(item)" class="col-md-9 text-left">' +
		'<button v-if="item.isCfgSignature == 1 && showdelete == true" type="button" @click="signature(item)" class="el-button el-button--primary el-button--small"><span>选择文件</span></button>'+
		'                <el-upload  :class="showdelete == true ? \'\' : \'image-uploader \' "' +
		'                   :action="item.data.upLoadUrl"' +
		'                   :list-type= "item.listType"' +
		'                   :accept="item.acceptFileType"' +
		'                   :on-preview="handlePictureCardPreview"' +
		'                   :on-remove="handleRemove"' +
		'                   :data="item.data"' +
		'                   :file-list="item.smAttachmentList"' +
		'                   :on-success="handleCallBack"' +
		'                   multiple' +
		'                   :limit="item.acceptFileCount"' +
		'                   :on-exceed="handleExceed"' +
		'                   :beforeUpload="beforeAvatarUpload"' +
		'                   >' +// ""
		'                   <el-button size="small" :title="\'接收上传文件\'+item.acceptFileType+\'，文件大小不小于\'+item.minFileSize+\'kb，不大于\'+item.maxFileSize+\'kb\'" type="primary" v-if="item.listType == \'text\' && item.isCfgSignature != 1">选择文件</el-button>' +
		'                   <i v-if="item.listType == \'picture-card\' && item.isCfgSignature != 1"><img src="../image/detail_pic_add.png" :title="\'接收上传文件\'+item.acceptFileType+\'，文件大小不小于\'+item.minFileSize+\'kb，不大于\'+item.maxFileSize+\'kb\'"/></i>' +
		'                </el-upload>' +
		'                <el-dialog :visible.sync="dialogVisible" @close="closeDialog" style="text-align:center;">' +
		'                    <img style="margin:0 auto" v-if=\'!hasImgFlag\' :class="{\'active\':isChoose,\'active1\':isChoose1,\'go\':rotateLeft90,\'aa\':!rotateLeft90,\'go1\':rotateRight90,\'aa\':!rotateRight90,\'go2\':rotate180,\'aa\':!rotate180}" width="100%" :src=\'imagesList[num]\'>' +
		'                    <img id="water" style="margin:0 auto;width:90%" v-else :class="{\'active\':isChoose,\'active1\':isChoose1,\'go\':rotateLeft90,\'aa\':!rotateLeft90,\'go1\':rotateRight90,\'aa\':!rotateRight90,\'go2\':rotate180,\'aa\':!rotate180}" width="100%" :src=\'imagesList[num].theLink\'>' +
		'                </el-dialog>' +

		'                <div class="el-upload__tip" style="display:none"><span>接收上传文件{{item.acceptFileType}}，文件大小不小于</span><span>{{item.minFileSize}}</span><span>kb，不大于</span><span>{{item.maxFileSize}}</span>kb</div>' +
		'            </div>' +
		'        </div><br/>' +
		'        <div class="clearfix"></div>' +
		'    </div>' +
		'    <div v-if="showControlBar" class="pictureControl">' +
		'        <div class="bgImg"><img alt="放大" title="放大" src="../image/big.png" @click="imgScc"></div>' +
		'        <div class="bgImg"><img alt="放大" title="缩小" src="../image/small.png" @click="imgScc1"></div>' +
		'        <div class="bgImg"><img alt="左旋转" title="左旋转" src="../image/rotateLeft.png" @click="imgSccRotate"></div>' +
		'        <div class="bgImg"><img alt="右旋转" title="右旋转" src="../image/rotateRight.png" @click="imgSccRotate1"></div>' +
		'        <div class="bgImg"><img alt="翻转" title="翻转" src="../image/arrTopBottom.png" @click="imgSccRotate2"></div>' +
		'        <div class="bgImg"><img alt="上一张" title="上一张" src="../image/arrowLeft.png" @click="getLeftPic"></div>' +
		'        <div class="bgImg"><img alt="下一张" title="下一张" src="../image/arrowRight.png" @click="getRightPic"></div>' +
		'    </div>' +
		'</div>' +
		'</div>' +

		'</div>' +
		'</div>';
	var fileUpload = Vue.extend({
		template: fileUploadNavigationTemplate,
		/*
         * showdelete: 修改/不可修改标志 loaduploadlist: 页面加载附件内容数据 busitype: 业务模块
         */
		props: ["loaduploadlist","showdelete","busitype"],
		data:function(){
			return{
				imagesList: [], // 预览显示图片List
				dialogVisible: false, // 图片预览显示框,
				indexFlag: "",
				uploadData : [], // 附件更新list
				isChoose:false, // 图片放大3倍标志
				isChoose1:false, // 图片缩小0.3倍标志
				rotateLeft90:false, // 图片左旋转90度
				rotateRight90:false, // 图片右旋转90度
				rotate180:false, // 图片旋转180度
				num:0, // 预览获取当前图片下标标志
				showControlBar:false, // 预览操作按钮显示/隐藏标志
				imgList: [], // 批量下载list
				hasImgFlag:false, // 新增显示标志
				isShowBatchBtn: false,
				isShowUpload: true,
				showGpy : false,
				downLoadFlag : true,
				interfaceVersion : 19000101,
			}
		},
		methods: {
			beforeAvatarUpload:function(file) {
				var testmsg=file.name.substring(file.name.lastIndexOf('.')+1);
				// var sizeClass = $(".el-upload__tip")[this.indexFlag];
				var minSize = $(".el-upload__tip").find("span:eq(1)").html();
				var maxSize = $(".el-upload__tip").find("span:eq(3)").html();
				if(file.size < (minSize * 1024) || file.size > (maxSize * 1024))
				{
					generalErrorModal("","您选择的文件大小超出限制！");
					return false;
				}
				// bugX-bug867 1.确认exe、bat可执行文件是否禁止上传
				var str = file.name.split(".");
				var arrlength = str.length;
				if(str[arrlength-1] == 'exe' || str[arrlength-1] == 'bat') {
					generalErrorModal("","禁止上传 exe 和 bat 格式的文件！");
					return false;
				}
				var item = this.indexFlag;

				if(item.acceptFileType.indexOf(str[arrlength-1]) == -1 ){
					generalErrorModal("","只接受："+item.acceptFileType+"类型的文件！");
					return false;
				}

				if(item.isCfgSignature == "1"){

					if(str[arrlength-1] != 'pdf')
					{
						generalErrorModal("","只能上传pdf文件");
						return false;
					}

				}
				// var isCfgSignature =
			},
			/*
             * handleErrCallBack(response, file, fileList){ //上传失败
             * addVue.errMsg = "请检查服务器连接!";
             * $(baseInfo.errorModel).modal('show', { backdrop :'static' }); },
             */
			handleExceed:function(files, fileList) {
				generalErrorModal("","您选择的文件个数超出限制！");
			},
			handleRemove: function(file, fileList) { // 点击删除按钮操作
				var currentUrl;
				if(file.response != undefined) {
					currentUrl = file.response.data[0].url;
				}else {
					currentUrl = file.url;
				}
				for(var i = 0;i<this.uploadData.length;i++){
					if(this.uploadData[i].theLink == currentUrl){
						this.uploadData.splice(i,1);
					}
				}


				for(var i = 0;i<this.imagesList.length;i++){
					if(this.imagesList[i].theLink == currentUrl){
						this.imagesList.splice(i,1);
					}
				}

				for(var i = 0;i<this.imgList.length;i++){
					if(this.imgList[i].theLink == currentUrl){
						this.imgList.splice(i,1);
					}
				}
				for(var i = 0;i<this.loaduploadlist.length;i++){
					var smAttachmentList = this.loaduploadlist[i].smAttachmentList;
					for(var j = 0;j<smAttachmentList.length;j++){
						if(smAttachmentList[j].url == currentUrl){
							smAttachmentList.splice(j,1);
						}
					}
				}
				if(this.imagesList == "") {
					this.hasImgFlag = false;
				}
			},
			handlePictureCardPreview:function(file) { // 图片预览
				this.showControlBar = true;
				var _this = this;
				var length = _this.imagesList.length;
				_this.imagesList = unique(_this.imagesList);
				var currentUrl;
				if(file.response != undefined) {
					currentUrl = file.response.data[0].url;
				}else {
					currentUrl = file.url;
				}
				for(var i = 0;i < length;i++) {
					if(_this.imagesList[i].theLink == currentUrl){
						_this.num = i;
					}
				}
				_this.dialogVisible = true;
			},
			handleCallBack:function(response, file, fileList){// 上传成功之后的回掉函数

				var _this = this;
				var modal = {
					sourceType:response.data[0].extra,
					theLink:response.data[0].url,
					fileType:response.data[0].objType,
					theSize:response.data[0].byteToStr,
					remark:response.data[0].originalName,
					busiType:_this.busitype,
					tableId:0,
				}
				var loaduploadlist = _this.loaduploadlist;
				console.log(loaduploadlist);
				for(var i = 0;i < loaduploadlist.length;i++){
					if(loaduploadlist[i].eCode == response.data[0].extra){
						if(loaduploadlist[i].isCfgSignature == "1"){

							var isSignature = "0";
							var model2 =
								{
									interfaceVersion : _this.interfaceVersion,
								}
							new ServerInterface("../Sm_UserGetForLogin").execute(model2, function(jsonObj){
								isSignature = jsonObj.isSignature;
								console.log("isSignature="+isSignature);
								// 具有签章权限的权限
								if(isSignature == "1")
								{
									// 判断Ukey是否插入
									id = TZKeyOcxIndex.Tz_SetVenderID(6);
									phKey = TZKeyOcxIndex.TZ_Connect(-1,0);
									console.log("phKey="+phKey);
									var rv = TZKeyOcxIndex.Tz_ReadKeyID(phKey);
									console.log("rv="+rv);

									/*
                                     * xsz by time 2019-3-8 13:44:40
                                     * 根据文件大小给出合理的延迟时间 （时间待定）
                                     * 暂定公式为：延迟时间=文件大小/1024(kb)/1024/4(区分阶段)*500
                                     */
									var fileSize = file.size;
									var mbytes = parseInt(fileSize/1024/1024/3);
									var settime1 = mbytes *800;// 打开文件
									var settime2 = mbytes *1000;// 上传文件


									console.log("文件大小mbytes="+mbytes+"fileSize="+fileSize);

									if(rv == 0) {
									}else{

										$(".xszModel").modal({
											backdrop :'static',
											keyboard: false
										});
										$("#signWarningText").html("正在打开pdf文件，请等待...");
										console.log("网络文件路径response.data[0].url："+response.data[0].url);
										var filename = Math.random().toString(36).substr(2);
										console.log("打开文件开始："+Date.parse(new Date()));
										var open = TZPdfViewer.TZOpenPdfByPath(response.data[0].url,1);
										// 2.根据关键字签章
										if(open==0) {
											setTimeout(function(item){
												console.log("打开文件结束："+Date.parse(new Date()) + "；；；打开文件返回值TZOpenPdfByPath="+open);

												console.log("签章开始："+Date.parse(new Date()));
												$("#signWarningText").html("正在正常签章中，请等待...");
												var pro3 = TZPdfViewer.TZSignByPos3(1,400,200,490,290);

												if(pro3==0&&TZPdfViewer.PageCount>0){
													$("#signWarningText").html("正在上传文件，请等待...");
													console.log("签章结束："+Date.parse(new Date())+ "；；；签章文件返回值TZSignByPos3="+pro3+",TZPdfViewer.PageCount="+TZPdfViewer.PageCount);
													/*
                                                     * 保存至服务器
                                                     */
													console.log("保存文件至服务器开始："+Date.parse(new Date()));
													TZPdfViewer.HttpInit();
													TZPdfViewer.HttpAddPostString("name","1111");
													TZPdfViewer.HttpAddPostCurrFile("file2","aaaa");// HttpAddPostCurrFile上传编辑器当前文件，第二个参数传""
													// ,随即产生Word的文件名
													TZPdfViewer.HttpPost("http://"+window.location.host+"/HousePresell/admin/FileUpload.jsp?filename="+filename);
													//TZPdfViewer.HttpPost("http://61.177.71.243:17000/TZPdfServers/FileUpload.jsp?filename="+filename);
													setTimeout(function(item){
														console.log("保存文件至服务器结束："+Date.parse(new Date()));
														var model =
															{
																interfaceVersion : _this.interfaceVersion,
																signaturePath : "C:\\uploaded\\"+filename+".pdf",
																fileName : filename,
																urlPath : response.data[0].url,//签章网络路径
															}

														new ServerInterface("../Sm_SignatureUploadForPath").execute(model, function(jsonObj){
															console.log(modal.theLink);
															$(".xszModel").modal('hide');
															if(jsonObj.result != "success")
															{
																generalErrorModal("",jsonObj.info);
															}
															else
															{
																modal.theLink = jsonObj.pdfUrl;
																console.log("保存到OSSServer结束："+Date.parse(new Date()));
															}


														});
													},2000+settime2);

												}else
												{
													$(".xszModel").modal('hide');
													generalErrorModal("","pdf文件签章失败，文件存在问题！");
												}


											},1000+settime1);

										}else{
											$(".xszModel").modal('hide');
											generalErrorModal("","ftp文件打开失败！");
										}


										/*
                                         * setTimeout(function(item){
                                         *
                                         * 保存至服务器
                                         *
                                         * if(TZPdfViewer.PageCount>0) {
                                         * console.log("保存文件至服务器开始："+Date.parse(new
                                         * Date())); TZPdfViewer.HttpInit();
                                         * TZPdfViewer.HttpAddPostString("name","1111");
                                         * TZPdfViewer.HttpAddPostCurrFile("file2","aaaa");//HttpAddPostCurrFile上传编辑器当前文件，第二个参数传""
                                         * ,随即产生Word的文件名
                                         * TZPdfViewer.HttpPost("http://"+window.location.host+"/HousePresell/admin/FileUpload.jsp?filename="+filename);
                                         * console.log("保存文件至服务器结束："+Date.parse(new
                                         * Date())); var model = {
                                         * interfaceVersion :
                                         * _this.interfaceVersion,
                                         * signaturePath :
                                         * "C:\\uploaded\\"+filename+".pdf", }
                                         *
                                         * new
                                         * ServerInterface("../Sm_SignatureUploadForPath").execute(model,
                                         * function(jsonObj){
                                         *
                                         * modal.theLink = jsonObj.pdfUrl;
                                         * console.log(modal.theLink);
                                         * $(".xszModel").modal('hide'); }); }
                                         * else {
                                         * $(".xszModel").modal('hide'); }
                                         *
                                         * },2000+settime2);
                                         */

									}

								}

							});


						}
					}
				}
				var _this = this;

				var md = {
					sourceType:response.data[0].extra,
					theLink:response.data[0].url,
					fileType:response.data[0].objType,
				}

				var imgListLength = _this.imagesList.length;
				if(imgListLength == 0) {
					this.imagesList.push(md);
				}else {
					var flag = true;
					var index;
					for(var i = imgListLength - 1 ; i >= 0; i--){
						if(this.imagesList[i].sourceType == response.data[0].extra){
							flag = false;
							index = i + 1;
							break;
						}else {
							continue;
						}
					}
					if(flag) {
						this.imagesList.push(md);
					}else {
						this.imagesList.splice(index, 0, md);
					}
				}
				this.imagesList = unique(this.imagesList);

				var imgListArr = {
					theLink:response.data[0].url,
					fileType:response.data[0].objType,
					remark:response.data[0].originalName,
				}

				_this.imgList.push(imgListArr);
				_this.uploadData.push(modal);


			},
			closeDialog:function(){ // 关闭图片预览
				this.dialogVisible = false;
				this.showControlBar = false;
			},
			imgScc:function() {
				var wValue = $(".el-dialog").width() + 45;
				$(".el-dialog").width(wValue);
			},
			imgScc1:function() {
				var wValue = $(".el-dialog").width() - 45;
				$(".el-dialog").width(wValue);
			},
			imgSccRotate:function(){
				this.rotateLeft90=!this.rotateLeft90;
				/*
                 * var current = $(".el-dialog").css("transform");
                 * if(current == "none") { current = 0; }
                 * console.log(current); current = (current-90)%360;
                 * console.log(current);
                 */
			},
			imgSccRotate1:function(){
				this.rotateRight90=!this.rotateRight90;
			},
			imgSccRotate2:function(){
				this.rotate180=!this.rotate180;
			},
			getLeftPic:function(){ // 图片上一张
				var _this = this;
				_this.imagesList = unique(_this.imagesList);
				var length = _this.imagesList.length;
				var lg = _this.num - 1;
				var currentNum = _this.num;
				if(lg >= 0) {
					var imgType = _this.imagesList[lg].sourceType;
					var currentImgType = _this.imagesList[currentNum].sourceType;
					if(currentImgType == imgType) {
						_this.num = lg;
					}else {
						_this.num = currentNum;
					}
				}else {
					_this.num = currentNum;
				}
			},
			getRightPic:function(){ // 图片下一张
				var _this = this;
				_this.imagesList = unique(_this.imagesList);
				var length = _this.imagesList.length;
				var lg = _this.num + 1;
				var currentNum = _this.num;
				if(lg <= length - 1) {
					var imgType = _this.imagesList[lg].sourceType;
					var currentImgType = _this.imagesList[currentNum].sourceType;
					if(currentImgType == imgType) {
						_this.num = lg;
					}else {
						_this.num = currentNum;
					}
				}else {
					_this.num = currentNum;
				}
			},
			batchDownload:function(){ // 批量下载
				new ServerInterface("../fileDownLoad").execute(this.getFileForm(), function(jsonObj)
				{
					if(jsonObj.result != "success")
					{
						noty({"text":jsonObj.info,"type":"error","timeout":2000});
					}
					else
					{
						window.location.href="../"+jsonObj.zipFileName;
					}
				});
			},
			getFileForm:function() {
				var list = this.uploadData;
				list = JSON.stringify(list);
				return {
					interfaceVersion:19000101,
					smAttachmentList:list,
				}
			},
			getIndex:function(index) {
				this.indexFlag = index;
			},
			showHighMeterModel : showHighMeterModel,
			mousedown:function(ev) {
				var oDiv = $("#water")[0];
				var oEvent = ev;
				var disX = 0;
				disX = oEvent.clientX - oDiv.offsetLeft;
				var disY = 0;
				disY = oEvent.clientY - oDiv.offsetTop;
				document.onmousemove=function (ev)
				{
					oEvent = ev;
					oDiv.style.marginLeft = "0px";
					var clientWidth = document.documentElement.clientWidth;
					var imgWidth = $("#water").width();
					var clientHeight = document.documentElement.clientHeight;
					var imgHeight = $("#water").height();
					var marginLeft = (clientWidth - imgWidth) / 2;
					var marginTop = (clientHeight - imgHeight) / 2;
					oDiv.style.marginLeft = oEvent.clientX - disX - marginLeft +"px";
					oDiv.style.marginTop = oEvent.clientY - disY +"px";
				}
				document.onmouseup=function()
				{
					document.onmousemove=null;
					document.onmouseup=null;
				}
			},
			signature: function(items){//签章
				var _this = this;
				var filename = Math.random().toString(36).substr(2);

				console.log("附件上传签章文件："+ filename);
				//1.选择需要签章的文件
				console.log("打开文件开始："+Date.parse(new Date()));
				var isOpen = TZPdfViewer.TZOpenPdf();
				console.log("打开文件结束："+Date.parse(new Date()));

				var file = TZPdfViewer.TZCurrentFilePath();
				console.log(file);

				var realName = file.substring(file.lastIndexOf('\\')+1,file.lastIndexOf('.'));
				console.log(realName);
				if(isOpen == 0){

					$(".xszModel").modal({
						backdrop :'static',
						keyboard: false
					});

					//2.签章文件
					setTimeout(function(item){

						console.log("签章开始："+Date.parse(new Date()));
						var isSign = TZPdfViewer.TZSignByPos3(1,400,200,490,290);
						console.log("签章结束："+Date.parse(new Date()));

						if(isSign == 0){
							console.log("保存到服务器开始："+Date.parse(new Date()));
							//3.保存到服务器上传
							TZPdfViewer.HttpInit();
							TZPdfViewer.HttpAddPostString("name","1111");
							TZPdfViewer.HttpAddPostCurrFile("file2","aaaa");//HttpAddPostCurrFile上传编辑器当前文件，第二个参数传"" ,随即产生Word的文件名
							//TZPdfViewer.HttpPost("http://"+window.location.host+"/HousePresell/admin/FileUpload.jsp?filename="+filename);
							TZPdfViewer.HttpPost("http://61.177.71.243:17000/TZPdfServers/FileUpload.jsp?filename="+filename);
							TZPdfViewer.TZClosePdf();//关闭当前打开的文件
							console.log("保存到服务器结束："+Date.parse(new Date()));

							var model =
								{
									interfaceVersion : 19000101,
									signaturePath : "C:\\uploaded\\"+filename+".pdf",
									fileName : filename,
									//								urlPath : response.data[0].url,//签章网络路径
								}
							console.log(items);
							console.log("保存到OSSServer开始："+Date.parse(new Date()));
							new ServerInterface("../Sm_SignatureUploadForPath").execute(model, function(jsonObj){
								$(".xszModel").modal('hide');
								if(jsonObj.result != "success")
								{
									generalErrorModal("",jsonObj.info);
								}
								else
								{
									console.log("theLink="+jsonObj.pdfUrl);
									//modal.theLink = jsonObj.pdfUrl;
									var pdfModel = {
										busiType:items.busiType,
										fileType:"pdf",
										name:realName+".pdf",
										sourceType:items.eCode,
										url:jsonObj.pdfUrl,
									}
									items.smAttachmentList.push(pdfModel);
									var pdfUpModel = {
										"sourceType":items.eCode,
										"theLink":jsonObj.pdfUrl,
										"fileType":"pdf",
										"theSize":"",
										"remark":realName+".pdf",
										"busiType":items.busiType,
										"tableId":0
									}
									_this.uploadData.push(pdfUpModel);

								}

							});

							console.log("保存到OSSServer结束："+Date.parse(new Date()));
						}
						else
						{
							$(".xszModel").modal('hide');
							generalErrorModal("","文件签章失败，请重新上传！");
						}

					},1000);

				}
				else
				{
					$(".xszModel").modal('hide');
					generalErrorModal("","文件签章失败，请重新上传！");
				}

			}
		},
		watch:{
			loaduploadlist:function(curVal, oldVal) { // 页面加载将已存在的附件信息加到list中
				var _this = this;
				_this.uploadData = [];
				_this.loaduploadlist = curVal;
				if(_this.loaduploadlist.length == 0) {
					_this.isShowUpload = false;
				}else {
					_this.isShowUpload = true;
				}
				// 页面加载将图片push到数组中用于预览
				if(_this.loaduploadlist != "") {
					_this.isShowBatchBtn = true;
					for(var i = 0;i<curVal.length;i++){
						if(curVal[i].smAttachmentList != "" || curVal[i].smAttachmentList != undefined) {
							var smList = curVal[i].smAttachmentList;
							if(smList == "" || smList == undefined) {
								_this.hasImgFlag = false;
							}else {
								_this.hasImgFlag = true;
								for(var j = 0;j<smList.length;j++){
									var modal = {
										sourceType:smList[j].sourceType,
										theLink:smList[j].url,
										fileType:smList[j].fileType,
									};
									var uploadModal = {
										sourceType:smList[j].sourceType,
										theLink:smList[j].url,
										fileType:smList[j].fileType,
										theSize:smList[j].theSize,
										remark:smList[j].name,
										busiType:_this.busitype,
										tableId:smList[j].tableId,
									};
									var batchModal = {
										theLink:smList[j].url,
										fileType:smList[j].fileType,
										remark:smList[j].name,
									}

									if(!isImg(smList[j].fileType)) {
										_this.imgList.push(batchModal);
									}
									_this.imagesList.push(modal);
									_this.uploadData.push(uploadModal);
								}
							}
						}
					}
				}
			},
			imagesList:function(curVal, oldVal) {
				var _this = this;
				var length = this.imagesList.length;
				if(length != 0) {
					_this.hasImgFlag = true;
					this.hasImgFlag = _this.hasImgFlag;
				}
			}
		},
		mounted:function() {
			var hrefArr = $("#tabContainer").data("tabs").getCurrentTabId().split("_");
			var hrefStr = hrefArr[hrefArr.length-2];
			var addStr = hrefStr.substr(hrefStr.length-3);
			if(addStr == "Add"){
				this.downLoadFlag = false;
			}else{
				this.downLoadFlag = true;
			}
			if(this.loaduploadlist.length == 0) {
				this.isShowUpload = false;
			}else {
				this.isShowUpload = true;
			}
		},
	});


	var count = 0 ;
	function showHighMeterModel(){
		var userAgent = navigator.userAgent;
		if (userAgent.indexOf("Chrome") > -1) {
			generalErrorModal("","请使用IE浏览器");
		}else{
			var _this = this;
			highMeterModalVue.fileArr = [];
			highMeterModalVue.clearRight();

			/*if(highMeterModalVue.clearRight() == 0){

            }else{
                generalErrorModal("","请连接高拍仪设备");
            }*/
			$("#modalHighMeter").modal({
				backdrop :'static'
			});
			$("#modalHighMeter").on('shown.bs.modal', function () {
				if(++count == 1){
					highMeterModalVue.Unload();
					highMeterModalVue.Load();
				}
			});

			highMeterModalVue.loaduploadlist = _this.loaduploadlist;
			highMeterModalVue.imagesList = _this.imagesList;
			highMeterModalVue.uploadData = _this.uploadData;
			highMeterModalVue.busitype = _this.busitype;
		}


		/*
         * modalHighMeterDiv = this.$refs.modalHighMeter;
         * $(this.$refs.modalHighMeter).modal({ backdrop :'static' });
         * this.showGpy = true; fileArr = []; //console.log(thumb1())
         * thumb1().Thumbnail_Clear(true);
         * $(this.$refs.modalHighMeter).on('shown.bs.modal', function () {
         * if(++count == 1){ Unload(); Load(); } }); //this.highMeterModel =
         * true;
         */    	}


	window.fileUpload = fileUpload;
	function unique(arr){ // 数组去重
		var res = [arr[0]];
		for(var i=1;i<arr.length;i++){
			var repeat = false;
			for(var j=0;j<res.length;j++){
				if(arr[i].theLink == res[j].theLink){
					repeat = true;
					break;
				}
			}
			if(!repeat){
				res.push(arr[i]);
			}
		}
		return res;
	}

	function isImg(imgType) { // 判断是否为图片
		if(imgType == 'BMP'  || imgType == 'bmp'  ||
			imgType == 'JPEG' || imgType == 'jpeg' ||
			imgType == 'GIF'  || imgType == 'gif'  ||
			imgType == 'PSD'  || imgType == 'psd'  ||
			imgType == 'PNG'  || imgType == 'png'  ||
			imgType == 'TIFF' || imgType == 'tiff' ||
			imgType == 'JPG'  || imgType == 'jpg'  ||
			imgType == 'TGA'  || imgType == 'tga'  ||
			imgType == 'EPS'  || imgType == 'eps') {
			return true;
		}
	}
})();