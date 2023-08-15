(function() {
	//--------------前端‘页码’组件
	var pageNavigationTemplate = '<div class="btn-page" id="pageLoadingDiv">'+
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
//				 '		<button v-on:click="jumpPage" type="button" class="btn btn-default btn-sure">确定</button>'	+
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
				var left = this.pagenumber-this.sidepagecount;//1-4=-3
				var right = this.pagenumber - this.sidepagecount + (this.sidepagecount*2);//1+4=5
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
	
	//--------------前端‘下拉菜单’组件
	/*
	 * 用法1：通过URL获取数据，URL可以是一个动态的值，只要有变化就会重新获取数据并刷新下拉菜单
		<vue-select defaultname="请选择机构类型" :thevalue="theType" :optionurl="'http://localhost/HousePresell/js/test'+具体关联参数+'.json'" @callbackfun="changeTheType"></vue-select>
	 * 用法2：通过传入list获取数据
		<vue-select defaultname="请选择机构类型" :thevalue="theType" :optionlist="theTypeList" @callbackfun="changeTheType"></vue-select>
	 * */
	
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
				//url返回数据格式参考：[{theValue:"10",theName:"招投标单位"}]
				var jsonObj = $.ajax({url : this.optionurl, async : false}).responseText;

				/*setTimeout(() => {
					this.options = eval("(" + jsonObj + ")");
				}, 100);*/

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
						//url返回数据格式参考：[{theValue:"10",theName:"招投标单位"}]
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
	
	//--------------列表页面前端‘下拉菜单’组件
	/*
	 * 用法1：通过URL获取数据，URL可以是一个动态的值，只要有变化就会重新获取数据并刷新下拉菜单
		<vue-listselect defaultname="请选择机构类型" :thevalue="theType" :optionurl="'http://localhost/HousePresell/js/test'+具体关联参数+'.json'" @callbackfun="changeTheType"></vue-listselect>
	 * 用法2：通过传入list获取数据
		<vue-listselect defaultname="请选择机构类型" :thevalue="theType" :optionlist="theTypeList" @callbackfun="changeTheType"></vue-listselect>
	 * */
	
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
				//url返回数据格式参考：[{theValue:"10",theName:"招投标单位"}]
				var jsonObj = $.ajax({url : this.optionurl, async : false}).responseText;
				
				/*setTimeout(() => {
					this.options = eval("(" + jsonObj + ")");
				}, 100);*/
				
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
						//url返回数据格式参考：[{theValue:"10",theName:"招投标单位"}]
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
	
	//--------------前端‘附件上传’组件
	var fileUploadNavigationTemplate = '<div>' +
		                               '<div class="detail-title" v-if="isShowUpload">' +
	                                   '    <h1 class="inline">附件材料</h1>' +
	                                   '<div class="inline"  style="margin:0px 10px" v-if="showdelete == true"><button class="btn btn-success" @click="showHighMeterModel()">'+
		                   				'<img src="../../image/detail_btn_ico_save.png" width="18">'+
		                   				'<span>高拍仪</span>'+
		                   		    	'</button>'+
		                   				'</div>'+
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
	                                   '            <div @click="getIndex(index)" class="col-md-9 text-left">' +
	                                   '                <el-upload :class="showdelete == true ? \'\' : \'image-uploader \' "' +
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
	                                   '                   >' +//""
	                                   '                   <el-button size="small" :title="\'接收上传文件\'+item.acceptFileType+\'，文件大小不小于\'+item.minFileSize+\'kb，不大于\'+item.maxFileSize+\'kb\'" type="primary" v-if="item.listType == \'text\'">选择文件</el-button>' +
	                                   '                   <i v-if="item.listType == \'picture-card\'"><img src="../../image/detail_pic_add.png" :title="\'接收上传文件\'+item.acceptFileType+\'，文件大小不小于\'+item.minFileSize+\'kb，不大于\'+item.maxFileSize+\'kb\'"/></i>' +
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
	                                   '        <div class="bgImg"><img alt="放大" title="放大" src="../../image/big.png" @click="imgScc"></div>' +
									   '        <div class="bgImg"><img alt="放大" title="缩小" src="../../image/small.png" @click="imgScc1"></div>' +
									   '        <div class="bgImg"><img alt="左旋转" title="左旋转" src="../../image/rotateLeft.png" @click="imgSccRotate"></div>' +
									   '        <div class="bgImg"><img alt="右旋转" title="右旋转" src="../../image/rotateRight.png" @click="imgSccRotate1"></div>' +
									   '        <div class="bgImg"><img alt="翻转" title="翻转" src="../../image/arrTopBottom.png" @click="imgSccRotate2"></div>' +
									   '        <div class="bgImg"><img alt="上一张" title="上一张" src="../../image/arrowLeft.png" @click="getLeftPic"></div>' +
									   '        <div class="bgImg"><img alt="下一张" title="下一张" src="../../image/arrowRight.png" @click="getRightPic"></div>' +
									   '    </div>' +
                                       '</div>' +
                                       '</div>' +
                                      
                                       '</div>' +
                                       '</div>';
		var fileUpload = Vue.extend({
			template: fileUploadNavigationTemplate,
			/*
			 * showdelete: 修改/不可修改标志
			 * loaduploadlist: 页面加载附件内容数据
			 * busitype: 业务模块
			 */
			props: ["loaduploadlist","showdelete","busitype"],
			data:function(){
		       return{
		    	    imagesList: [], // 预览显示图片List
					dialogVisible: false, // 图片预览显示框,
					indexFlag: 0,
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
	           }
			},
			methods: {
				beforeAvatarUpload:function(file) {
					var testmsg=file.name.substring(file.name.lastIndexOf('.')+1);
					var sizeClass = $(".el-upload__tip")[this.indexFlag];
					var minSize = $(".el-upload__tip").find("span:eq(1)").html();
					var maxSize = $(".el-upload__tip").find("span:eq(3)").html();
					if(file.size < (minSize * 1024) || file.size > (maxSize * 1024)) 
					{
						generalErrorModal("","您选择的文件大小超出限制！");
						return false;
					}
					
					// bugX-bug867   1.确认exe、bat可执行文件是否禁止上传
					var str = file.name.split(".");
					if(str[1] == 'exe' || str[1] == 'bat') {
						generalErrorModal("","禁止上传 exe 和 bat 格式的文件！");
						return false;
					}
				},
				/*handleErrCallBack(response, file, fileList){ //上传失败
			    	addVue.errMsg = "请检查服务器连接!";
	                $(baseInfo.errorModel).modal('show', {
					    backdrop :'static'
				    });
			    },*/
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
			    handleCallBack:function(response, file, fileList){//上传成功之后的回掉函数
			    	console.log(response);
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
	            	/*var current = $(".el-dialog").css("transform");
	            	if(current == "none") {
	            		current = 0;
	            	}
	            	console.log(current);
	            	current = (current-90)%360;
	            	console.log(current);*/
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
	            	new ServerInterface("../../fileDownLoad").execute(this.getFileForm(), function(jsonObj)
	    			{
	    				if(jsonObj.result != "success")
	    				{
	    					noty({"text":jsonObj.info,"type":"error","timeout":2000});
	    				}
	    				else
	    				{
	    					window.location.href="../../"+jsonObj.zipFileName;
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
	        		console.log(oDiv);
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
					//页面加载将图片push到数组中用于预览
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
				/*var hrefArr = $("#tabContainer").data("tabs").getCurrentTabId().split("_");
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
				}*/
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
		    	if(highMeterModalVue.clearRight() == 0){
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
		    	}else{
		    		generalErrorModal("","请连接高拍仪设备");
		    	}
		    }
			
			
			/*modalHighMeterDiv = this.$refs.modalHighMeter;
    		$(this.$refs.modalHighMeter).modal({
    			backdrop :'static'
    		});
			this.showGpy = true;
    		fileArr = [];
    		//console.log(thumb1())
    		thumb1().Thumbnail_Clear(true);
    		$(this.$refs.modalHighMeter).on('shown.bs.modal', function () {
    			 if(++count == 1){
    	             Unload();
    	             Load();
    			}
    		 });
    		//this.highMeterModel = true;
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

    //---------------------------------------------------审批start--------------------------------------------//
    var approvalModelTemplate =
        '<div>'+
			'<button  v-if="isneedbackup" class="btn btn-warning"  @click="approvalModalOpen">' +
			'<img src="../../image/list_btn_ico_examine.png" width="18">' +
			'<span>审批记录</span>' +
			'</button>' +
			'<button  v-if="isneedbackup && sourcepage == 1" class="btn btn-warning" @click="approvalHandle">' +
			'<img src="../../image/detail_btn_ico_record.png" width="18">' +
			'<span>备案</span>' +
			'</button>' +
			'<button v-if="!isneedbackup" class="btn btn-warning" @click="approvalModalOpen">' +
			'<img src="../../image/list_btn_ico_examine.png" width="18">' +
			'<span>审批</span>' +
			'</button>'+
			'<div class="modal fade approvalModalBody" id="approvalModal" tabindex="-1" role="dialog" aria-hidden="true"> ' +
			' <div class="modal-dialog examine-dialog"> ' +
			'  <div class="modal-content"> ' +
			'   <div class="modal-header"> ' +
			'    <button type="button" class="close" @click="approvalModalClose">x</button> ' +
			'    <h4 class="modal-title csm-title"> 审批 </h4> '+
			'   </div> ' +
			'   <div class="modal-body"> ' +
			'    <div class="examine-body"> ' +
			'     <ul class="examine-tab nav nav-tabs" style="position: static;"> ' +
			'      <li class="approvalFirst" v-if="sourcepage == 1 && !isneedbackup"><a href="#approvalTabFirst" data-toggle="tab" @click="selectState = 1">审批处理</a></li> ' +
			'      <li class="approvalSecond"><a href="#approvalTabSecond" data-toggle="tab" @click="selectState = 2">审批流程</a></li> ' +
			'      <li class="approvalThird"><a href="#approvalTabThird" data-toggle="tab" @click="selectState = 3">审批记录</a></li> ' +
			'     </ul> ' +
			'     <div class="tab-content"> ' +
			'      <div class="tab-pane fade tab-first" id="approvalTabFirst"  v-if="sourcepage == 1"> ' +
			'       <div class="row tab-first"> ' +
			'        <div class="col-md-6"> ' +
			'         <div class="form-group"> ' +
			'          <span class="detail-text1">审批人员：</span> ' +
			'          <div class="detail-info1"> ' +
			'           <div class="detail-unEdit"> ' +
			'            <input type="text" disabled="disabled" class="form-control" :value="username"> ' +
			'           </div> ' +
			'          </div> ' +
			'         </div> ' +
			'        </div> ' +
			'        <div class="col-md-12"> ' +
			'         <div class="form-group"> ' +
			'          <span class="detail-text1 cs-mt-0">审批结果：</span> ' +
			'          <div class="detail-info1"> ' +
			'           <el-radio-group v-model="sm_ApprovalProcess_Handle.theAction"> ' +
			'            <el-radio  :label="0">通过</el-radio> ' +
			'            <el-radio  :label="2">不通过</el-radio> ' +
			'            <el-radio  :label="1">驳回</el-radio> ' +
			'           </el-radio-group> ' +
			'          </div> ' +
			'         </div> ' +
			'        </div> ' +
			'        <div class="col-md-12"> ' +
			'         <div class="form-group"> ' +
			'          <span class="detail-text1 cs-mt-0">审批评语：</span> ' +
			'          <div class="detail-info1"> ' +
			'           <el-input type="textarea" :autosize="{ minRows: 3}" placeholder="请输入审批评语" v-model="sm_ApprovalProcess_Handle.theContent"></el-input> ' +
			'           <div class="attachments2"> ' +
			'            <el-upload ' +
			'              class="upload-demo" ' +
			'              :action="approvalUploadCfg.data.upLoadUrl" ' +
			'               multiple ' +
			'              :accept="approvalUploadCfg.acceptFileType" ' +
			'              :data="approvalUploadCfg.data" ' +
			'              :on-success="approvalUploadSuccess"> ' +
			'             <el-button size="small" type="primary" :disabled="uploadDisabled">上传附件</el-button> ' +
			'            </el-upload> ' +
			'           </div> ' +
			'          </div> ' +
			'         </div> ' +
			'        </div> ' +
			'       </div> ' +
			'      </div> ' +
			'      <div class="tab-pane fade tab-second" id="approvalTabSecond"> ' +
			'       <el-table :data="sm_ApprovalProcess_WorkflowList" border stripe :default-sort = "{prop: \'index\'}" > ' +
						'<el-table-column align="center" type="index" width="100" label="审批步骤" fixed ></el-table-column>\n' +
			'       	 <el-table-column prop="theName" label="步骤名称" min-width="80"></el-table-column>\n' +
			'       	 <el-table-column prop="approvalModel" label="审批模式" min-width="80">\n' +
			'       	 <template scope="scope">\n' +
			'         		 <span v-if="scope.row.approvalModel == 0">抢占</span>\n' +
			'        		  <span v-if="scope.row.approvalModel == 1">会签</span>\n' +
			'        	 </template>\n' +
			'        	</el-table-column>\n' +
			'       	 <el-table-column prop="roleName" label="处理角色" min-width="120"></el-table-column>\n' +
			'       	 <el-table-column align="center" prop="operateTimeStamp" label="处理时间" width="180"></el-table-column>\n' +
			'       	 <el-table-column prop="busiState" label="审批进度" min-width="120"></el-table-column>'+
			'       </el-table> ' +
			'      </div> ' +
			'      <div class="tab-pane fade tab-third" id="approvalTabThird"> ' +
			'       <el-table :data="sm_ApprovalProcess_RecordList" border stripe :default-sort = "{prop: \'index\'}" > ' +
			'        <el-table-column align="center" type="index" width="80" label="序号"></el-table-column> ' +
			'        <el-table-column prop="theName" label="步骤名称" min-width="120"></el-table-column> ' +
			'        <el-table-column prop="approvalModel" label="审批模式" min-width="120"> ' +
			'         <template scope="scope"> ' +
			'          <span v-if="scope.row.approvalModel == 0">抢占</span> ' +
			'          <span v-if="scope.row.approvalModel == 1">会签</span> ' +
			'         </template> ' +
			'        </el-table-column> ' +
			'        <el-table-column prop="userOperate" label="处理人" min-width="120"></el-table-column> ' +
			'        <el-table-column align="center" prop="operateTimeStamp" label="处理时间" min-width="200"></el-table-column> ' +
			'        <el-table-column prop="theAction" label="审批结果" min-width="120"> ' +
			'         <template scope="scope"> ' +
			'          <div v-if="scope.row.theAction == 0" class="text-success">同意</div> ' +
			'          <div v-if="scope.row.theAction == 1" class="text-danger">驳回</div> ' +
			'          <div v-if="scope.row.theAction == 2" class="text-warning">不通过</div> ' +
			'         </template> ' +
			'        </el-table-column> ' +
			'        <el-table-column prop="theContent" label="审批评语" min-width="120"></el-table-column> ' +
			'        <el-table-column label="附件" min-width="350"> ' +
			'         <template scope="scope"> ' +
			'          <div v-for="item in scope.row.sm_attachmentCfgList"> ' +
			'           <el-upload ' +
			'             class="upload-demo prohibitDelete" ' +
			'             :file-list="item.attchmentList" ' +
			'             :on-remove="approvalHandlerRemove"> ' +
			'           </el-upload> ' +
			'           <button v-if="item.attchmentList.length > 0" class="btn btn-all" @click="approvalBatchDownload(item.attchmentList)">全部下载</button> ' +
			'          </div> ' +
			'         </template> ' +
			'        </el-table-column> ' +
			'       </el-table> ' +
			'      </div> ' +
			'     </div> ' +
			'    </div> ' +
			'   </div> ' +
			'   <div class="modal-footer" v-if="selectState == 1 && sourcepage == 1 && !isneedbackup"> ' +
			'    <button type="button" class="btn foot-sure" @click="approvalHandle"> ' +
			'     确定 ' +
			'    </button> ' +
			'    <button type="button" class="btn foot-cancel" @click="approvalModalClose"> ' +
			'     取消 ' +
			'    </button> ' +
			'   </div> ' +
			'  </div><!-- /.modal-content --> ' +
			' </div><!-- /.modal --> ' +
			'</div>'+
			'<div class="modal fade" id="approvalPromptModal" tabindex="-1" role="dialog" aria-hidden="true">' +
			' <div class="modal-dialog modal-del">' +
			'  <div class="modal-content">' +
			'   <div class="modal-body csm-body">' +
			'    <div class="del-info">' +
			'     <div class="del-text1 font-bold">是否确认关闭审批弹窗?</div>' +
			'    </div>' +
			'   </div>' +
			'   <div class="modal-footer">' +
			'    <button type="button" class="btn foot-sure" @click="approvalPromptModalConfirm">确定</button>' +
			'    <button type="button" class="btn foot-cancel" @click="approvalPromptModalCancle">取消</button>' +
			'   </div>' +
			'  </div><!-- /.modal-content -->' +
			' </div><!-- /.modal -->' +
			'</div>'+
        '</div>';


    var approvalModalVue = Vue.extend({
        template: approvalModelTemplate,
        props: ['afid','sourcepage','workflowid','isneedbackup','username','userid'],
        data: function()
        {
            return{
                interfaceVersion : 19000101,
                sm_ApprovalProcess_Handle:{},  //审批内容
                sm_ApprovalProcess_WorkflowList:[], //审批弹窗 - 审批流程
                sm_ApprovalProcess_RecordList:[], //审批弹窗 - 审批记录
                selectState:'1',
                busiCode:"",//业务编码
                attachmentList: [],   //附件列表
                approvalUploadCfg: {"data":{}}, //附件配置信息
                buttonType:"",   //按钮类型 - 1:审批 2：备案
                uploadDisabled:false,  //上传附件按钮
            }
        },
		created:function()
		{
			var _this = this;
            //获取附件配置信息
            var model = {
                interfaceVersion:_this.interfaceVersion,
                pageNumber:"0",
                busiType : "01030101",
            };
            new ServerInterface("../../Sm_AttachmentCfgList").execute(model, function (jsonObj) {
                if (jsonObj.result != "success")
                {
                    parent.generalErrorModal(jsonObj);
                }
                else
                {
                    if(jsonObj.sm_AttachmentCfgList == null && jsonObj.sm_AttachmentCfgList.length == 0)
                    {
                        _this.uploadDisabled = true;
                    }
                    else
                    {
                        if(jsonObj.sm_AttachmentCfgList.length > 0)
                        {
                            _this.approvalUploadCfg= jsonObj.sm_AttachmentCfgList[0];
                        }
                    }
                }
            });
		},
        methods:{
            //审批弹窗打开
            approvalModalOpen:function ()
            {
				var _this = this;
                _this.sm_ApprovalProcess_Handle = {};
                _this.selectState = '1';

                //---------------------remove------------------//
                $('.approvalFirst').removeClass("active");
                $('#approvalTabFirst').removeClass("in active");
                $('.approvalSecond').removeClass("active");
                $('#approvalTabSecond').removeClass("in active");
                $('.approvalThird').removeClass("active");
                $('#approvalTabThird').removeClass("in active");
                //---------------------remove------------------//

                if(_this.sourcepage == 2 || _this.isneedbackup)
                {
                    $('.approvalSecond').addClass("active");
                    $('#approvalTabSecond').addClass("in active");
                }
                else
                {
                    $('.approvalFirst').addClass("active");
                    $('#approvalTabFirst').addClass("in active");
                }

                _this.getWorkflowList();

                $('#approvalModal').modal(
                    {
                        backdrop: 'static',
                        keyboard: false,
                    }
                );
            },
            //审批弹窗关闭 - 打开二次确认弹窗
            approvalModalClose:function () {
                $('#approvalPromptModal').modal(
                    {
                        backdrop: 'static',
                        keyboard: false,
                    }
                );
            },
            //提示框确认
            approvalPromptModalConfirm:function ()
            {
                $('#approvalPromptModal').hide();
                $('#approvalModal').hide();
                $('.modal-backdrop').remove();
                $('#approvalPromptModal').removeData('bs.modal');
                $('#approvalModal').removeData('bs.modal');
            },
            //提示框取消
            approvalPromptModalCancle:function()
            {
                $('#approvalPromptModal').hide();
                var _backdrop = $('.modal-backdrop');
                _backdrop[_backdrop.length -1].remove();
                $('#approvalPromptModal').removeData('bs.modal');
            },
			getWorkflowList:function()
			{
                //获取审批流程和审批记录
                var _this = this;
                var model =
                    {
                        interfaceVersion:_this.interfaceVersion,
                        approvalProcess_AFId : _this.afid,
                    }
                new ServerInterface("../../Sm_ApprovalProcess_ModalList").execute(model, function(jsonObj)
                {
                    if(jsonObj.result != "success")
                    {
                        parent.generalErrorModal(jsonObj);
                    }
                    else
                    {
                        _this.sm_ApprovalProcess_WorkflowList = jsonObj.sm_ApprovalProcess_WorkflowList; // 节点信息
                        _this.sm_ApprovalProcess_RecordList = jsonObj.sm_ApprovalProcess_RecordList; //审批记录
                    }
                });
			},
			//审批操作
            approvalHandle: function ()
            {
            	var _this = this;
                if(_this.buttonType == 2)
                {
                    _this.sm_ApprovalProcess_Handle.theAction = 0;
                }
                var model=
                    {
                        interfaceVersion:_this.interfaceVersion,
                        approvalProcessId:_this.workflowid, //当前审批节点Id
                        theContent:_this.sm_ApprovalProcess_Handle.theContent,
                        theAction:_this.sm_ApprovalProcess_Handle.theAction,
                        //附件材料
                        busiType : "01030101",
                        generalAttachmentList:_this.attachmentList,
                        //按钮
                        buttonType: _this.buttonType,
                        extraObj:_this.extraObj,
                    }
                new ServerInterfaceJsonBody("../../Sm_ApprovalProcess_Handler").execute(model, function(jsonObj)
                {
                    if(jsonObj.result != "success")
                    {
                        parent.generalErrorModal(jsonObj);
                    }
                    else
                    {
                        $('#approvalModal').modal('hide');
                        var noPresellCert=jsonObj.noPresellCert;
                        if(noPresellCert!=undefined){//如果预售证不是未定义，说明该楼幢缺少预售证
                            parent.generalErrorModal(undefined,noPresellCert)
                            setTimeout(function () {
                                generalHideModal('#fm')
                                parent.enterNewTabAndCloseCurrent(null, "待办流程", "test/Test_ApprovalProcess_AgencyList.shtml");
                            },2000)

                        }else{
                            parent.enterNewTabAndCloseCurrent(null, "待办流程", "test/Test_ApprovalProcess_AgencyList.shtml");
                            parent.approvalSuccessModal(jsonObj);
                        }
                    }
                });
            },
            indexMethod: function (index) {
                generalIndexMethod(index, this);
            },
            //上传附件
            approvalUploadSuccess:  function (res,file,fileList)
            {
            	var _this = this;
                //更新结点操作人信息
                if(res.status="success")
                {
                    var model={
                        interfaceVersion:_this.interfaceVersion,
                        tableId : _this.workflowid,
                    }
                    new ServerInterface("../../Sm_ApprovalProcess_WorkflowUpdate").execute(model, function(jsonObj)
                    {
                        if(jsonObj.result != "success")
                        {
                            parent.generalErrorModal(jsonObj);
                        }
                    });
                }
                _this.attachmentList = [];
                for(var i=0;i<fileList.length;i++)
                {
                    var file =fileList[i];
                    var approvalAttachment = {
                        sourceType:_this.approvalUploadCfg.eCode,
                        theLink : file.response.data[0].url,
                        theSize : file.raw.size,
                        fileType: file.response.data[0].objType,
                        remark : file.name,
                        busiType : "01030101",
                    };
                    _this.attachmentList.push(approvalAttachment);
                }
            },
            //附件批量下载
            approvalBatchDownload:  function (attachmentList)
            {
            	var _this = this;
                var list = JSON.stringify(attachmentList);
                var model ={
                    interfaceVersion:_this.interfaceVersion,
                    smAttachmentList:list,
                }
                new ServerInterface("../../fileDownLoad").execute(model, function(jsonObj)
                {
                    if(jsonObj.result != "success")
                    {
                        parent.generalErrorModal(jsonObj);
                    }
                    else
                    {
                        window.location.href="../../"+jsonObj.zipFileName;
                    }
                });
            }
            ,
            //附件删除
            approvalHandlerRemove: function (file,fileList)
            {
            	var _this = this;
                if(file.uploadUserId == _this.userid)
                {
                    var model={
                        interfaceVersion:_this.interfaceVersion,
                        tableId : file.tableId
                    }
                    new ServerInterface("../../Sm_AttachmentDelete").execute(model, function(jsonObj)
                    {
                        if(jsonObj.result != "success")
                        {
                            parent.generalErrorModal(jsonObj);
                        }
                        else
						{
                            _this.getWorkflowList();
						}
                    });
                }
                else
                {
                    fileList.push(file);
                }
            },
        },
    });

    window.approvalModalVue = approvalModalVue;
//---------------------------------------------------审批end--------------------------------------------//
})();