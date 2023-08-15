package zhishusz.housepresell.timer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.xiaominfo.oss.sdk.ReceiveMessage;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import zhishusz.housepresell.controller.form.Empj_ProjProgForcast_ManageForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.database.dao.Empj_ProjProgForcast_DTLDao;
import zhishusz.housepresell.database.dao.Empj_ProjProgForcast_ManageDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.dao.Tgpf_SocketMsgDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjProgForcast_AF;
import zhishusz.housepresell.database.po.Empj_ProjProgForcast_DTL;
import zhishusz.housepresell.database.po.Empj_ProjProgForcast_Manage;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Tgpf_SocketMsg;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.toInterface.To_ProjProgForcastPhoto;
import zhishusz.housepresell.util.ToInterface;
import zhishusz.housepresell.util.fileupload.OssServerUtil;
import zhishusz.housepresell.util.picture.MatrixUtil;

@Slf4j
@Service
@Transactional(transactionManager = "transactionManager")
public class HandlerPicAndSubmitService {

	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;
	@Autowired
	private OssServerUtil ossUtil;

	@Autowired
	private Empj_ProjProgForcast_DTLDao projProgForcast_DTLDao;
	@Autowired
	private Empj_ProjProgForcast_ManageDao manageDao;
	@Autowired
	private Tgpf_SocketMsgDao tgpf_SocketMsgDao;

	// 处理推送到网站的附件信息
	@SuppressWarnings("unchecked")
	public void handlerExecute() {

		System.out.println("handlerExecute定时任务开始" + System.currentTimeMillis());

		// 查询水印图片位置
		Sm_BaseParameterForm baseParameterForm = new Sm_BaseParameterForm();
		baseParameterForm.setTheState(S_TheState.Normal);
		baseParameterForm.setTheValue("69005");
		baseParameterForm.setParametertype("69");
		Sm_BaseParameter baseParameter = sm_BaseParameterDao
				.findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm));
		if (null == baseParameter) {
			System.out.println("handlerExecute-未查询到baseParameter水印位置" + System.currentTimeMillis());
			return;
		}

		String picAddress = baseParameter.getTheName();

		// 查询缩放比列
		Sm_BaseParameterForm baseParameterForm2 = new Sm_BaseParameterForm();
		baseParameterForm2.setTheState(S_TheState.Normal);
		baseParameterForm2.setTheValue("69001");
		baseParameterForm2.setParametertype("69");
		Sm_BaseParameter baseParameter2 = sm_BaseParameterDao
				.findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm2));
		if (null == baseParameter2) {
			System.out.println("handlerExecute-未查询到baseParameter2限定高度" + System.currentTimeMillis());
			return;
		}

		String picHeight = baseParameter2.getTheName();

		// 查询缩放比列
		Sm_BaseParameterForm baseParameterForm3 = new Sm_BaseParameterForm();
		baseParameterForm3.setTheState(S_TheState.Normal);
		baseParameterForm3.setTheValue("69002");
		baseParameterForm3.setParametertype("69");
		Sm_BaseParameter baseParameter3 = sm_BaseParameterDao
				.findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm3));
		if (null == baseParameter3) {
			System.out.println("handlerExecute-未查询到baseParameter3限定宽度" + System.currentTimeMillis());
			return;
		}

		String picWidth = baseParameter3.getTheName();

		// 查询推送网站地址
		Sm_BaseParameterForm baseParameterForm0 = new Sm_BaseParameterForm();
		baseParameterForm0.setTheState(S_TheState.Normal);
		baseParameterForm0.setTheValue("69004");
		baseParameterForm0.setParametertype("69");
		Sm_BaseParameter baseParameter0 = sm_BaseParameterDao
				.findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm0));
		if (null == baseParameter0) {
			System.out.println("handlerExecute-未查询到baseParameter0推送网站地址" + System.currentTimeMillis());
			return;
		}

		String webTheLink = baseParameter0.getTheName();

		/*
		 * 查询需要进行图片处理推送的单据信息
		 */
		List<Empj_ProjProgForcast_Manage> listManage = new ArrayList<>();
		Empj_ProjProgForcast_ManageForm manageModel = new Empj_ProjProgForcast_ManageForm();
		manageModel.setTheState(S_TheState.Normal);
		manageModel.setApprovalState(S_ApprovalState.Examining);
		manageModel.setWebPushState("1");
//		manageModel.setProjectId(1288l);
		listManage = manageDao.findByPage(manageDao.getQuery(manageDao.getPushDtlHQL(), manageModel));

		//System.out.println("listManage="+listManage.size());
		//listManage= = null;

		Empj_ProjProgForcast_DTL dtl;
		Empj_ProjProgForcast_AF progForcast;

		// 附件信息Model
		Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
		sm_AttachmentForm.setBusiType("03030202");
		sm_AttachmentForm.setTheState(S_TheState.Normal);

		// 附件信息
		List<Sm_Attachment> sm_AttachmentList;
		Sm_Attachment attachment;

		String theLink;
		String pic;
		String smallPic;

		ReceiveMessage upload;

		// 图片处理类
		MatrixUtil picUtil = new MatrixUtil();

		String[] deskURL;

		// 推送信息
		To_ProjProgForcastPhoto pushVo;
		// 节点日期+楼层+上传类型
		String news_title = "";
		// 项目名称+施工编号：楼栋号+楼层+上传类型
		String news_title1 = "";
		String dqlc = "";

		// 施工编号
		String geteCodeFromConstruction = "";
		// 公安编号
		String geteCodeFromPublicSecurity;

		Double floorUpNumber;
		String floorUpNumberStr;

		Empj_BuildingInfo buildInfo;

		StringBuffer smallBuffer;
		StringBuffer buffer;

		Gson gson = new Gson();
		String jsonMap;
		String decodeStr;
		ToInterface toFace = new ToInterface();
		boolean interfaceUtil;

		Tgpf_SocketMsg tgpf_SocketMsg;
		String hasAchieve = "";

		for (Empj_ProjProgForcast_Manage manage : listManage) {
			progForcast = manage.getAfEntity();
			dtl = manage.getDtlEntity();
			if (null == dtl) {
				continue;
			}
			hasAchieve = dtl.getHasAchieve();
			//正负零前 不推送
			if("0".equals(hasAchieve)){
				continue;
			}
			System.out.println("handlerExecute楼幢：" + dtl.getBuildCode() + "START" + System.currentTimeMillis());

			// 查询相关附件信息并处理
			sm_AttachmentForm.setSourceId(dtl.getTableId().toString());
			// 加载所有楼幢下的相关附件信息
			sm_AttachmentList = sm_AttachmentDao
					.findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getHandlerPhotoHQL(), sm_AttachmentForm));
			System.out.println("楼幢： " + dtl.getBuildCode() + "满足的附件数据数" + sm_AttachmentList.size());
			if (sm_AttachmentList.size() < 4) {
				System.out.println("楼幢： " + dtl.getBuildCode() + "满足的附件数据不满足" + sm_AttachmentList.size());
				continue;
			}

			pushVo = new To_ProjProgForcastPhoto();
			pushVo.setAction("add");
			pushVo.setCate("jindu");
			pushVo.setTs_pj_id(String.valueOf(manage.getProject().getTableId()));
			pushVo.setTs_bld_id(String.valueOf(dtl.getBuildInfo().getTableId()));
			pushVo.setTs_id(String.valueOf(dtl.getTableId()));
			pushVo.setJdtime(StrUtil.isBlank(progForcast.getForcastTime()) ? "" : progForcast.getForcastTime());

			buildInfo = dtl.getBuildInfo();
			// 施工编号
			geteCodeFromConstruction = StrUtil.isBlank(buildInfo.geteCodeFromConstruction()) ? ""
					: buildInfo.geteCodeFromConstruction();
			// 公安编号
			geteCodeFromPublicSecurity = StrUtil.isBlank(buildInfo.geteCodeFromPublicSecurity()) ? ""
					: buildInfo.geteCodeFromPublicSecurity();
			// 楼层
			floorUpNumber = null == dtl.getFloorUpNumber() ? 0.00 : dtl.getFloorUpNumber();
			floorUpNumberStr = String.valueOf(floorUpNumber.intValue());

			news_title = "";
			news_title1 = "";

			if ("1".equals(dtl.getBuildProgressType())) {
				dqlc = dtl.getBuildProgress();
				news_title = "主体结构施工：" + dqlc + "层";
				news_title1 = progForcast.getProjectName() + " 施工编号：" + geteCodeFromConstruction + " 主体结构施工：" + dqlc
						+ "层";
			} else if ("2".equals(dtl.getBuildProgressType())) {
				dqlc = floorUpNumberStr;
				news_title = "外立面装饰施工：" + dtl.getBuildProgress() + "%";
				news_title1 = progForcast.getProjectName() + " 施工编号：" + geteCodeFromConstruction + " 外立面装饰施工："
						+ dtl.getBuildProgress() + "%";
			} else if ("3".equals(dtl.getBuildProgressType())) {
				dqlc = floorUpNumberStr;
				news_title = "室内装修施工：" + dtl.getBuildProgress() + "%";
				news_title1 = progForcast.getProjectName() + " 施工编号：" + geteCodeFromConstruction + " 室内装修施工："
						+ dtl.getBuildProgress() + "%";
			}

			pushVo.setDqlc(dqlc);
			pushVo.setNews_title(news_title);
			pushVo.setNews_title1(news_title1);

			smallBuffer = new StringBuffer();
			buffer = new StringBuffer();

			jsonMap = "";
			decodeStr = "";

			// 对楼幢附件信息进行遍历处理
			for (int i = 0; i < 4; i++) {

				attachment = sm_AttachmentList.get(i);
				theLink = sm_AttachmentList.get(i).getTheLink();
				pic = "";
				smallPic = "";

				deskURL = picUtil.addWaterMarkAndCompress(theLink, picAddress, picHeight, picWidth);

				System.out.println("11111楼幢： " + dtl.getBuildCode() + "处理后的附件地址" + deskURL.toString());

				// 高清图
				if (StrUtil.isNotBlank(deskURL[0])) {
					upload = ossUtil.upload(deskURL[0]);
					if (null != upload && null != upload.getData() && !upload.getData().isEmpty()
							&& StrUtil.isNotBlank(upload.getData().get(0).getUrl())) {
						pic = upload.getData().get(0).getUrl();
						// picUtil.deleteFile(deskURL[0]);
					}
				}

				// 缩略图
				if (StrUtil.isNotBlank(deskURL[1])) {
					upload = ossUtil.upload(deskURL[1]);
					if (null != upload && null != upload.getData() && !upload.getData().isEmpty()
							&& StrUtil.isNotBlank(upload.getData().get(0).getUrl())) {

						smallPic = upload.getData().get(0).getUrl();
						// picUtil.deleteFile(deskURL[1]);
					}
				}

				// 防止图片处理失败，进行二次处理
				if (StrUtil.isBlank(pic) || StrUtil.isBlank(smallPic)) {
					deskURL = picUtil.addWaterMarkAndCompress(theLink, picAddress, picHeight, picWidth);
					System.out.println("22222楼幢： " + dtl.getBuildCode() + "处理后的附件地址" + deskURL.toString());
					// 高清图
					if (StrUtil.isNotBlank(deskURL[0])) {
						upload = ossUtil.upload(deskURL[0]);
						if (null != upload && null != upload.getData() && !upload.getData().isEmpty()
								&& StrUtil.isNotBlank(upload.getData().get(0).getUrl())) {
							pic = upload.getData().get(0).getUrl();
							// picUtil.deleteFile(deskURL[0]);
						}
					}

					// 缩略图
					if (StrUtil.isNotBlank(deskURL[1])) {
						upload = ossUtil.upload(deskURL[1]);
						if (null != upload && null != upload.getData() && !upload.getData().isEmpty()
								&& StrUtil.isNotBlank(upload.getData().get(0).getUrl())) {

							smallPic = upload.getData().get(0).getUrl();
							// picUtil.deleteFile(deskURL[1]);
						}
					}
				}

				if ("总平图".equals(sm_AttachmentList.get(i).getAttachmentCfg().getTheName())) {
					// 总平图
					attachment.setSortNum("1");
				} else {
					// 其他附件
					attachment.setSortNum("2");
				}

				attachment.setTheLink(pic);
				attachment.setMd5Info(theLink + "##" + pic + "##" + smallPic);
				attachment.setRecordTimeStamp(System.currentTimeMillis());
				sm_AttachmentDao.update(attachment);

				if (buffer.length() > 0) {
					buffer.append("," + pic);
				} else {
					buffer.append(pic);
				}

				if (smallBuffer.length() > 0) {
					smallBuffer.append("," + smallPic);
				} else {
					smallBuffer.append(smallPic);
				}

				attachment.setTheLink(pic);
				attachment.setMd5Info(theLink + "##" + pic + "##" + smallPic);
				attachment.setRecordTimeStamp(System.currentTimeMillis());
				sm_AttachmentDao.update(attachment);

			}

			pushVo.setSmallpic(smallBuffer.toString());
			pushVo.setImage2(buffer.toString());

			jsonMap = gson.toJson(pushVo);
			decodeStr = Base64Encoder.encode(jsonMap);
			interfaceUtil = toFace.interfaceUtil(decodeStr, webTheLink);

			// 更新楼幢推送状态-已推送
			dtl.setWebPushState("2");
			dtl.setLastUpdateTimeStamp(System.currentTimeMillis());
			projProgForcast_DTLDao.update(dtl);

			// 更新推送管理单据状态
			manage.setWebPushState("2");
			manage.setLastUpdateTimeStamp(System.currentTimeMillis());
			manageDao.update(manage);

			tgpf_SocketMsg = new Tgpf_SocketMsg();
			tgpf_SocketMsg.setTheState(S_TheState.Normal);
			tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());
			tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());
			tgpf_SocketMsg.setMsgStatus(1);
			tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());
			tgpf_SocketMsg.setMsgDirection("ZT_TO_MH_time_submit");
			tgpf_SocketMsg.setMsgContentArchives(jsonMap);
			if (interfaceUtil) {
				tgpf_SocketMsg.setReturnCode("200");
				System.out.println("操作成功！！");
			} else {
				tgpf_SocketMsg.setReturnCode("300");
			}
			tgpf_SocketMsgDao.save(tgpf_SocketMsg);

			System.out.println("handlerExecute楼幢：" + dtl.getBuildCode() + "END" + System.currentTimeMillis());

		}
	}

}