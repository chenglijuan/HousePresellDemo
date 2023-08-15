package zhishusz.housepresell.timer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaominfo.oss.sdk.ReceiveMessage;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import zhishusz.housepresell.controller.form.Empj_ProjProgForcast_AFForm;
import zhishusz.housepresell.controller.form.Empj_ProjProgForcast_DTLForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.database.dao.Empj_ProjProgForcast_AFDao;
import zhishusz.housepresell.database.dao.Empj_ProjProgForcast_DTLDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.po.Empj_ProjProgForcast_AF;
import zhishusz.housepresell.database.po.Empj_ProjProgForcast_DTL;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.fileupload.OssServerUtil;
import zhishusz.housepresell.util.picture.MatrixUtil;

@Slf4j
@Service
@Transactional(transactionManager = "transactionManager")
public class HandlerPicService {

	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;
	@Autowired
	private OssServerUtil ossUtil;

	@Autowired
	private Empj_ProjProgForcast_AFDao projProgForcast_AFDao;
	@Autowired
	private Empj_ProjProgForcast_DTLDao projProgForcast_DTLDao;

	@SuppressWarnings({ "unchecked", "static-access" })
	public void execute() {
		log.info("HandlerPicService.execute()##定时任务开始" + System.currentTimeMillis());
		System.out.println("定时任务开始" + System.currentTimeMillis());

		// 查询水印图片位置
		Sm_BaseParameterForm baseParameterForm = new Sm_BaseParameterForm();
		baseParameterForm.setTheState(S_TheState.Normal);
		baseParameterForm.setTheValue("69005");
		baseParameterForm.setParametertype("69");
		Sm_BaseParameter baseParameter = sm_BaseParameterDao
				.findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm));

		if (null == baseParameter) {
			log.info("未查询到水印图片位置！");
			return;
		}

		// 查询缩放比列
		Sm_BaseParameterForm baseParameterForm2 = new Sm_BaseParameterForm();
		baseParameterForm2.setTheState(S_TheState.Normal);
		baseParameterForm2.setTheValue("69001");
		baseParameterForm2.setParametertype("69");
		Sm_BaseParameter baseParameter2 = sm_BaseParameterDao
				.findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm2));

		if (null == baseParameter2) {
			log.info("未查询到缩放限定宽度！");
			return;
		}

		Sm_BaseParameterForm baseParameterForm3 = new Sm_BaseParameterForm();
		baseParameterForm3.setTheState(S_TheState.Normal);
		baseParameterForm3.setTheValue("69002");
		baseParameterForm3.setParametertype("69");
		Sm_BaseParameter baseParameter3 = sm_BaseParameterDao
				.findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm3));

		if (null == baseParameter3) {
			log.info("未查询到缩放限定高度！");
			return;
		}

		boolean isComplete = Boolean.TRUE;
		MatrixUtil picUtil = new MatrixUtil();

		String[] deskURL;

		// 查询需要处理图片的申请单信息
		Empj_ProjProgForcast_AFForm afModel = new Empj_ProjProgForcast_AFForm();
		afModel.setTheState(S_TheState.Normal);
		afModel.setApprovalState(S_ApprovalState.Examining);
		afModel.setHandleState("0");

		List<Empj_ProjProgForcast_AF> afList;
		Empj_ProjProgForcast_AF updateProjProgForcast_AF;

		// 子表信息
		List<Empj_ProjProgForcast_DTL> dtlList;
		Empj_ProjProgForcast_DTLForm dtlModel = new Empj_ProjProgForcast_DTLForm();
		dtlModel.setTheState(S_TheState.Normal);
		dtlModel.setHandleState("0");

		// 附件信息
		Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
		sm_AttachmentForm.setBusiType("03030202");
		sm_AttachmentForm.setTheState(S_TheState.Normal);

		// 需处理的附件信息
		List<String> attachmentList;

		// 附件信息
		List<Sm_Attachment> sm_AttachmentList;
		Sm_Attachment attachment;

		String theLink;
		String pic;
		String smallPic;

		ReceiveMessage upload;

		afList = projProgForcast_AFDao
				.findByPage(projProgForcast_AFDao.getQuery(projProgForcast_AFDao.getHandlerPicHQL(), afModel));
		
		System.out.println("满足的主表数据数" + afList.size());
		
		for (Empj_ProjProgForcast_AF empj_ProjProgForcast_AF : afList) {

			updateProjProgForcast_AF = empj_ProjProgForcast_AF;
			/*
			 * 查询子表信息 进行图片处理
			 */
			dtlModel.setAfEntity(empj_ProjProgForcast_AF);
			dtlModel.setAfId(empj_ProjProgForcast_AF.getTableId());
			dtlList = projProgForcast_DTLDao
					.findByPage(projProgForcast_DTLDao.getQuery(projProgForcast_DTLDao.getHandlerPicHQL(), dtlModel));
			
			System.out.println(empj_ProjProgForcast_AF.getCode() + "满足的子表数据数" + dtlList.size());
			
			for (Empj_ProjProgForcast_DTL dtl : dtlList) {
				try {

					sm_AttachmentForm.setSourceId(dtl.getTableId().toString());

					attachmentList = new ArrayList<>();
					// 加载所有楼幢下的相关附件信息
					sm_AttachmentList = sm_AttachmentDao.findByPage(
							sm_AttachmentDao.getQuery(sm_AttachmentDao.getHandlerPhotoHQL(), sm_AttachmentForm));
					
					System.out.println("楼幢： " + dtl.getBuildCode() + "满足的附件数据数" + sm_AttachmentList.size());
					
					if (null == sm_AttachmentList || sm_AttachmentList.size() == 0) {
						sm_AttachmentList = new ArrayList<Sm_Attachment>();
					}

					// 遍历总平图
					for (int i = 0; i < 4; i++) {

						attachment = sm_AttachmentList.get(i);
						theLink = sm_AttachmentList.get(i).getTheLink();
						pic = "";
						smallPic = "";
						
						deskURL = picUtil.addWaterMarkAndCompress(theLink, baseParameter.getTheName(),
								baseParameter2.getTheName(), baseParameter3.getTheName());

						// 高清图
						if (StrUtil.isNotBlank(deskURL[0])) {
							upload = ossUtil.upload(deskURL[0]);
							if (null != upload && null != upload.getData() && !upload.getData().isEmpty()
									&& StrUtil.isNotBlank(upload.getData().get(0).getUrl())) {

								pic = upload.getData().get(0).getUrl();
								picUtil.deleteFile(deskURL[0]);
							}
						}

						// 缩略图
						if (StrUtil.isNotBlank(deskURL[1])) {
							upload = ossUtil.upload(deskURL[1]);
							if (null != upload && null != upload.getData() && !upload.getData().isEmpty()
									&& StrUtil.isNotBlank(upload.getData().get(0).getUrl())) {

								smallPic = upload.getData().get(0).getUrl();
								picUtil.deleteFile(deskURL[1]);
							}
						}

						if ("总平图".equals(sm_AttachmentList.get(i).getAttachmentCfg().getTheName())) {
							// 总平图
							attachment.setSortNum("1");
						}else{
							// 其他附件
							attachment.setSortNum("2");
						}
						
						attachment.setTheLink(pic);
						attachment.setMd5Info(theLink + "##" + pic + "##" + smallPic);
						attachment.setRecordTimeStamp(System.currentTimeMillis());
						sm_AttachmentDao.update(attachment);
					}


					// 更新图片处理状态
					dtl.setHandleState("1");
					projProgForcast_DTLDao.update(dtl);

				} catch (Exception e) {

					isComplete = Boolean.FALSE;

					log.info("HandlerPicService.execute()##定时任务异常，异常数据：afId = " + empj_ProjProgForcast_AF.getTableId()
							+ "### dtlId = " + dtl.getTableId() + "### 异常原因：" + e.getMessage());
					System.out.println("定时任务异常：" + e.getMessage());
				}
			}

			updateProjProgForcast_AF.setHandleState("1");
			projProgForcast_AFDao.update(updateProjProgForcast_AF);

		}

		log.info("HandlerPicService.execute()##定时任务结束" + System.currentTimeMillis());
	}
}