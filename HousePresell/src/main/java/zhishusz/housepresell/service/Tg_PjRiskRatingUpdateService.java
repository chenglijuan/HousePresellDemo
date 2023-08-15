package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tg_PjRiskRatingForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tg_PjRiskRatingDao;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tg_PjRiskRating;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service更新操作：项目风险评级
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_PjRiskRatingUpdateService
{
	@Autowired
	private Tg_PjRiskRatingDao tg_PjRiskRatingDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Sm_AttachmentCfgDao smAttachmentCfgDao;
	@Autowired
	private Sm_AttachmentDao smAttachmentDao;
	
	private static final String BUSI_CODE = "21020304";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"
	
	public Properties execute(Tg_PjRiskRatingForm model)
	{
		Properties properties = new MyProperties();
		
		Sm_User user = model.getUser();
		
		Long tg_PjRiskRatingId = model.getTableId();
		Tg_PjRiskRating tg_PjRiskRating = (Tg_PjRiskRating)tg_PjRiskRatingDao.findById(tg_PjRiskRatingId);
		if(tg_PjRiskRating == null)
		{
			return MyBackInfo.fail(properties, "该风险评级信息已不存在");
		}
		
		Long projectId = model.getProjectId();
		String operateDate = model.getOperateDate();
		String theLevel = model.getTheLevel();
		String riskNotification = model.getRiskNotification();
		
	
//		if(projectId == null || projectId < 1)
//		{
//			return MyBackInfo.fail(properties, "请选择项目");
//		}
		
		if(operateDate == null || operateDate.length() == 0)
		{
			return MyBackInfo.fail(properties, "请选择风险评级日期");
		}
		if(theLevel == null || theLevel.length() == 0)
		{
			return MyBackInfo.fail(properties, "请选择项目风险评级等级");
		}
		if(riskNotification == null || riskNotification.length() == 0)
		{
			return MyBackInfo.fail(properties, "请选择风险提示");
		}	
		
		
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
	
		tg_PjRiskRating.setTheState(S_TheState.Normal);
		tg_PjRiskRating.setUserUpdate(user);
		tg_PjRiskRating.setLastUpdateTimeStamp(System.currentTimeMillis());

		tg_PjRiskRating.setOperateDate(operateDate);
		tg_PjRiskRating.setTheLevel(theLevel);
		tg_PjRiskRating.setRiskNotification(riskNotification);
		tg_PjRiskRatingDao.save(tg_PjRiskRating);
		
		/*
		 * 修改附件
		 * 附件需要先进行删除操作，然后进行重新上传保存功能
		 */
		// 附件信息
		String smAttachmentJson = null;
		if (null != model.getSmAttachmentList() && model.getSmAttachmentList().length() > 0)
		{
			// 根据退房退款ID进行查询附件功能
			Sm_AttachmentForm from = new Sm_AttachmentForm();

			String sourceId = String.valueOf(tg_PjRiskRatingId);
			from.setTheState(S_TheState.Normal);
			from.setSourceId(sourceId);

			// 查询附件
			@SuppressWarnings("unchecked")
			List<Sm_Attachment> smAttachmentList = smAttachmentDao
					.findByPage(smAttachmentDao.getQuery(smAttachmentDao.getBasicHQL2(), from));
			// 删除附件
			if (null != smAttachmentList && smAttachmentList.size() > 0)
			{
				for (Sm_Attachment sm_Attachment : smAttachmentList)
				{
					sm_Attachment.setTheState(S_TheState.Deleted);
					smAttachmentDao.save(sm_Attachment);
				}
			}

			// 重新保存附件
			smAttachmentJson = model.getSmAttachmentList().toString();
			List<Sm_Attachment> gasList = JSON.parseArray(smAttachmentJson, Sm_Attachment.class);

			if (null != gasList && gasList.size() > 0)
			{
				for (Sm_Attachment sm_Attachment : gasList)
				{
					//查询附件配置表
					Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
					form.seteCode(sm_Attachment.getSourceType());
					Sm_AttachmentCfg sm_AttachmentCfg = smAttachmentCfgDao.findOneByQuery_T(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));
					
					sm_Attachment.setAttachmentCfg(sm_AttachmentCfg);
					sm_Attachment.setSourceId(tg_PjRiskRatingId.toString());
					sm_Attachment.setTheState(S_TheState.Normal);
					smAttachmentDao.save(sm_Attachment);
				}
			}
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
