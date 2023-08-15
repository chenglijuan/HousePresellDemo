package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tg_RiskLogInfoForm;
import zhishusz.housepresell.database.dao.Tg_RiskLogInfoDao;
import zhishusz.housepresell.database.po.Tg_RiskLogInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tg_PjRiskRating;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tg_PjRiskRatingDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;

/*
 * Service更新操作：风险日志管理
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tg_RiskLogInfoUpdateService
{
	// 项目风险日志
	private static final String BUSI_CODE = "21020303";// 具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"
	@Autowired
	private Tg_RiskLogInfoDao tg_RiskLogInfoDao;
	@Autowired
	private Sm_AttachmentCfgDao smAttachmentCfgDao;// 附件配置
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;// 附件

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_RiskLogInfoForm model)
	{
		Properties properties = new MyProperties();
		
		// 查询日志信息
		Long tg_RiskLogInfoId = model.getTableId();
		Tg_RiskLogInfo tg_RiskLogInfo = (Tg_RiskLogInfo) tg_RiskLogInfoDao.findById(tg_RiskLogInfoId);
		if (tg_RiskLogInfo == null)
		{
			return MyBackInfo.fail(properties, "该风险日志已失效，请刷新后重试");
		}

		// 修改业务（同新增，eCode不可改）
		// 操作人
		Sm_User user = model.getUser();
		if (null == user)
		{
			return MyBackInfo.fail(properties, "请先登录");
		}

		// 设置操作人及操作时间
		tg_RiskLogInfo.setUserUpdate(user);
		tg_RiskLogInfo.setLastUpdateTimeStamp(System.currentTimeMillis());

		// 风险日志
		String riskLog2 = model.getRiskLog();
		if (null == riskLog2 || riskLog2.trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "请输入风险日志信息");
		}

		// 设置风险日志
		tg_RiskLogInfo.setRiskLog(riskLog2);

		// 日志日期
		String logDate = model.getLogDate();
		if (null == logDate || logDate.trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "请选择日志日期");
		}

		// 设置日志日期
		tg_RiskLogInfo.setLogDate(logDate);
		
		/*
		 * xsz by time 2018-11-21 14:47:06
		 * 判断附件是否必须上传
		 */
		// 判断是否有必传
		Sm_AttachmentCfgForm sm_AttachmentCfgForm = new Sm_AttachmentCfgForm();
		sm_AttachmentCfgForm.setBusiType(BUSI_CODE);
		sm_AttachmentCfgForm.setTheState(S_TheState.Normal);
		List<Sm_AttachmentCfg> sm_AttachmentCfgList = smAttachmentCfgDao
				.findByPage(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), sm_AttachmentCfgForm));

		// 先判断是否有附件传递
		List<Sm_Attachment> attachmentList;
		if (null != model.getSmAttachmentList() && !model.getSmAttachmentList().trim().isEmpty())
		{
			attachmentList = JSON.parseArray(model.getSmAttachmentList().toString(), Sm_Attachment.class);
		}
		else
		{
			attachmentList = new ArrayList<Sm_Attachment>();
		}
		
		if (null != sm_AttachmentCfgList && sm_AttachmentCfgList.size() > 0)
		{

			for (Sm_AttachmentCfg sm_AttachmentCfg : sm_AttachmentCfgList)
			{
				// 根据业务判断是否有必传的附件配置
				if (sm_AttachmentCfg.getIsNeeded())
				{
					Boolean isExistAttachment = false;
	
					if (attachmentList.size() > 0)
					{
	
						for (Sm_Attachment sm_Attachment : attachmentList)
						{
							if (sm_AttachmentCfg.geteCode().equals(sm_Attachment.getSourceType()))
							{
								isExistAttachment = true;
								break;
							}
						}
	
					}
	
					if (!isExistAttachment)
					{
						return MyBackInfo.fail(properties, sm_AttachmentCfg.getTheName() + "未上传,此附件为必须上传附件");
					}
	
				}
			}
		}


		tg_RiskLogInfoDao.save(tg_RiskLogInfo);
		
		/*
		 * xsz by time 2018-8-28 18:22:29
		 * 修改附件
		 * 附件需要先进行删除操作，然后进行重新上传保存功能
		 */
		String smAttachmentJson = null;
		// 查询原本附件信息
		Sm_AttachmentForm from = new Sm_AttachmentForm();

		String sourceId = String.valueOf(tg_RiskLogInfoId);
		from.setTheState(S_TheState.Normal);
		from.setBusiType(BUSI_CODE);
		from.setSourceId(sourceId);

		// 查询附件
		List<Sm_Attachment> smAttachmentList = sm_AttachmentDao
				.findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), from));

		// 删除附件
		if (null != smAttachmentList && smAttachmentList.size() > 0)
		{
			for (Sm_Attachment sm_Attachment : smAttachmentList)
			{
				sm_Attachment.setUserUpdate(user);// 操作人
				sm_Attachment.setLastUpdateTimeStamp(System.currentTimeMillis());// 操作时间
				sm_Attachment.setTheState(S_TheState.Deleted);
				sm_AttachmentDao.save(sm_Attachment);
			}
		}

		// 重新保存附件
		smAttachmentJson = model.getSmAttachmentList().toString();
		if (null != smAttachmentJson && !smAttachmentJson.trim().isEmpty())
		{
			List<Sm_Attachment> gasList = JSON.parseArray(smAttachmentJson, Sm_Attachment.class);
			for (Sm_Attachment sm_Attachment : gasList)
			{
				// 查询附件配置表
				Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
				form.seteCode(sm_Attachment.getSourceType());
				Sm_AttachmentCfg sm_AttachmentCfg = smAttachmentCfgDao
						.findOneByQuery_T(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));

				sm_Attachment.setAttachmentCfg(sm_AttachmentCfg);

				sm_Attachment.setSourceId(String.valueOf(tg_RiskLogInfoId));
				sm_Attachment.setUserStart(user);// 创建人
				sm_Attachment.setUserUpdate(user);// 操作人
				sm_Attachment.setCreateTimeStamp(System.currentTimeMillis());
				sm_Attachment.setLastUpdateTimeStamp(System.currentTimeMillis());
				sm_Attachment.setTheState(S_TheState.Normal);
				sm_Attachment.setBusiType(BUSI_CODE);
				sm_AttachmentDao.save(sm_Attachment);
			}

		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
