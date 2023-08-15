package zhishusz.housepresell.approvalprocess;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.xiaominfo.oss.sdk.ReceiveMessage;
import com.xiaominfo.oss.sdk.client.FileBytesResponse;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.database.po.toInterface.To_Project;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.ToInterface;
import zhishusz.housepresell.util.fileupload.OssServerUtil;
import zhishusz.housepresell.util.picture.MatrixUtil;

/**
 * 项目新增：
 * 审批过后-业务逻辑处理
 */
@Transactional
public class ApprovalProcessCallBack_03010101 implements IApprovalProcessCallback
{
	@Autowired
	private Empj_ProjectInfoDao empj_projectInfoDao;

	@Autowired
	private Sm_AttachmentCfgDao sm_AttachmentCfgDao;

	@Autowired
	private Sm_AttachmentDao smAttachmentDao;

	@SuppressWarnings("unchecked")
	@Override
	public Properties execute(Sm_ApprovalProcess_Workflow approvalProcessWorkflow, BaseForm baseForm)
	{
		Properties properties = new MyProperties();

		try
		{
			String workflowEcode = approvalProcessWorkflow.geteCode();

			// 获取正在处理的申请单
			Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = approvalProcessWorkflow.getApprovalProcess_AF();

			// 获取正在处理的申请单所属的流程配置
			Sm_ApprovalProcess_Cfg sm_ApprovalProcess_Cfg = sm_ApprovalProcess_AF.getConfiguration();
			String approvalProcessWork = sm_ApprovalProcess_Cfg.geteCode() + "_" + workflowEcode;

			// 获取正在审批的项目
			Long empj_projectInfoId = sm_ApprovalProcess_AF.getSourceId();
			Empj_ProjectInfo empj_ProjectInfo = empj_projectInfoDao.findById(empj_projectInfoId);

			if (empj_ProjectInfo == null)
			{
				return MyBackInfo.fail(properties, "审批的项目不存在");
			}

			switch (approvalProcessWork)
			{
			case S_BusiCode.busiCode_03010101 + "001_ZS":
				if (S_ApprovalState.Completed.equals(sm_ApprovalProcess_AF.getBusiState())
						&& S_WorkflowBusiState.Completed.equals(approvalProcessWorkflow.getBusiState()))
				{
					String jsonStr = sm_ApprovalProcess_AF.getExpectObjJson();
					if (jsonStr != null && jsonStr.length() > 0)
					{

						empj_ProjectInfo.setBusiState(S_BusiState.HaveRecord); // 已备案
						empj_ProjectInfo.setApprovalState(S_ApprovalState.Completed); // 已完结
						empj_ProjectInfo.setUserUpdate(approvalProcessWorkflow.getUserUpdate());// 操作人
						empj_ProjectInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
						empj_ProjectInfo.setUserRecord(approvalProcessWorkflow.getUserUpdate());// 备案人
						empj_ProjectInfo.setRecordTimeStamp(System.currentTimeMillis());

						empj_projectInfoDao.save(empj_ProjectInfo);

						Sm_AttachmentForm attachmentForm = new Sm_AttachmentForm();

						String sourceId = String.valueOf(empj_projectInfoId);
						attachmentForm.setBusiType(S_BusiCode.busiCode_03010101);
						attachmentForm.setSourceId(sourceId);
						attachmentForm.setTheState(S_TheState.Normal);

						// 查询附件
						List<Sm_Attachment> smAttachmentList = smAttachmentDao
								.findByPage(smAttachmentDao.getQuery(smAttachmentDao.getBasicHQL2(), attachmentForm));

						if (null == smAttachmentList || smAttachmentList.size() == 0)
						{
							smAttachmentList = new ArrayList<Sm_Attachment>();
						}

						// 推送数据到门户网站
//						Boolean interFaceAction = toInterFaceAction(empj_ProjectInfo, empj_ProjectInfo.geteCode(),
//								smAttachmentList);
						Boolean interFaceAction = toInterFaceAction(empj_ProjectInfo, String.valueOf(empj_ProjectInfo.getTableId()),
								smAttachmentList);
						if (!interFaceAction)
						{
							properties.put(S_NormalFlag.result, S_NormalFlag.fail);
							properties.put(S_NormalFlag.info, "消息推送门户网站失败！");
						}
					}
				}
				break;
			default:
				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, "没有需要处理的回调");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();

			properties.put(S_NormalFlag.result, S_NormalFlag.fail);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_BusiError);
		}

		return properties;
	}

	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;

	private Log log = LogFactory.getCurrentLogFactory().createLog(ApprovalProcessCallBack_03010101.class);

	@Autowired
	private OssServerUtil ossUtil;

	/**
	 * 系统推送数据到门户网站
	 * 
	 * @param model
	 * @param eCode
	 * @param attachmentList
	 */
	public Boolean toInterFaceAction(Empj_ProjectInfo empj_ProjectInfo, String eCode,
			List<Sm_Attachment> attachmentList)
	{
		// 查询地址
		Sm_BaseParameterForm baseParameterForm0 = new Sm_BaseParameterForm();
		baseParameterForm0.setTheState(S_TheState.Normal);
		baseParameterForm0.setTheValue("69004");
		baseParameterForm0.setParametertype("69");
		Sm_BaseParameter baseParameter0 = sm_BaseParameterDao
				.findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm0));

		if (null == baseParameter0)
		{
			log.equals("未查询到配置路径！");

			return false;
		}
		
		// 查询水印图片位置
		Sm_BaseParameterForm baseParameterForm = new Sm_BaseParameterForm();
		baseParameterForm.setTheState(S_TheState.Normal);
		baseParameterForm.setTheValue("69003");
		baseParameterForm.setParametertype("69");
		Sm_BaseParameter baseParameter = sm_BaseParameterDao
				.findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm));

		if (null == baseParameter)
		{
			log.equals("未查询到水印图片位置！");

			return false;
		}

		// 查询缩放比列
		Sm_BaseParameterForm baseParameterForm2 = new Sm_BaseParameterForm();
		baseParameterForm2.setTheState(S_TheState.Normal);
		baseParameterForm2.setTheValue("69001");
		baseParameterForm2.setParametertype("69");
		Sm_BaseParameter baseParameter2 = sm_BaseParameterDao
				.findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm2));

		if (null == baseParameter2)
		{
			log.equals("未查询到缩放限定宽度！");

			return false;
		}

		Sm_BaseParameterForm baseParameterForm3 = new Sm_BaseParameterForm();
		baseParameterForm3.setTheState(S_TheState.Normal);
		baseParameterForm3.setTheValue("69002");
		baseParameterForm3.setParametertype("69");
		Sm_BaseParameter baseParameter3 = sm_BaseParameterDao
				.findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm3));

		if (null == baseParameter3)
		{
			log.equals("未查询到缩放限定高度！");

			return false;
		}

		To_Project project = new To_Project();
		project.setAction("add");
		project.setCate("pj");
		project.setPj_title(empj_ProjectInfo.getTheName());
		project.setTs_pj_id(eCode);
		project.setPj_area_name(empj_ProjectInfo.getCityRegion().getTheName());
		project.setTs_area_id(String.valueOf(empj_ProjectInfo.getCityRegion().getTableId()));
		project.setPj_kfs(empj_ProjectInfo.getDevelopCompany().getTheName());
		project.setPj_dz(empj_ProjectInfo.getAddress());
		project.setPj_ysz("");

		StringBuffer buffer = new StringBuffer();

		if (attachmentList != null && attachmentList.size() > 0)
		{
			for (Sm_Attachment attachment : attachmentList)
			{
				String sourceType = attachment.getSourceType();

				Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
				form.seteCode(sourceType);
				Sm_AttachmentCfg sm_AttachmentCfg = sm_AttachmentCfgDao
						.findOneByQuery_T(sm_AttachmentCfgDao.getQuery(sm_AttachmentCfgDao.getBasicHQL(), form));

				if (null != sm_AttachmentCfg)
				{
					if (sm_AttachmentCfg.getTheName().contains("鸟瞰图"))
					{
						MatrixUtil picUtil = new MatrixUtil();

						String deskURL = picUtil.compressionPic(attachment.getTheLink(), baseParameter.getTheName(),
								baseParameter2.getTheName(), baseParameter3.getTheName());

						// 保存oss
						ReceiveMessage upload = ossUtil.upload(deskURL);

						if (null == upload)
						{
							log.equals("连接Oss-Server失败！");

							return false;
						}

						FileBytesResponse ossMessage = upload.getData().get(0);

						if (null == ossMessage)
						{
							log.equals("上传文件失败！");

							return false;
						}

						// 删除文件
						MatrixUtil.deleteFile(deskURL);

						String httpUrl = ossMessage.getUrl();

						buffer.append(httpUrl);
					}
				}
			}
		}

		project.setPj_pic(buffer.toString());
		project.setPj_content(empj_ProjectInfo.getIntroduction());
		project.setTs_jl_name("");
		project.setTs_jl_id("");
		project.setPj_longitude(Double.toString(empj_ProjectInfo.getLongitude()));
		project.setPj_latitude(Double.toString(empj_ProjectInfo.getLatitude()));

		Gson gson = new Gson();

		String jsonMap = gson.toJson(project);

		System.out.println(jsonMap);

		String decodeStr = Base64Encoder.encode(jsonMap);

		System.out.println(decodeStr);

		ToInterface toFace = new ToInterface();

		return toFace.interfaceUtil(decodeStr,baseParameter0.getTheName());
	}
}
