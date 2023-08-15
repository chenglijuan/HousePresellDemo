package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tg_PjRiskRatingForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Tg_PjRiskRatingDao;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Tg_PjRiskRating;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：项目风险评级
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_PjRiskRatingDetailService
{
	@Autowired
	private Tg_PjRiskRatingDao tg_PjRiskRatingDao;
	@Autowired
	private Sm_AttachmentDao smAttachmentDao;
	@Autowired
	private Sm_AttachmentCfgDao sm_AttachmentCfgDao;

	public Properties execute(Tg_PjRiskRatingForm model)
	{
		Properties properties = new MyProperties();
		
		//获取业务编号
		String busiCode = model.getBusiCode();
		
		List<Sm_Attachment> smAttachmentList = new ArrayList<Sm_Attachment>();

		Long tg_PjRiskRatingId = model.getTableId();
		Tg_PjRiskRating tg_PjRiskRating = (Tg_PjRiskRating)tg_PjRiskRatingDao.findById(tg_PjRiskRatingId);
		if(tg_PjRiskRating == null || S_TheState.Deleted.equals(tg_PjRiskRating.getTheState()))
		{
			return MyBackInfo.fail(properties, "风险评级信息有误");
		}
		
		Sm_AttachmentForm attachmentForm = new Sm_AttachmentForm();

		String sourceId = String.valueOf(tg_PjRiskRatingId);
		attachmentForm.setBusiType(busiCode);
		attachmentForm.setSourceId(sourceId);
		attachmentForm.setTheState(S_TheState.Normal);
		
		// 查询附件
		smAttachmentList = smAttachmentDao
				.findByPage(smAttachmentDao.getQuery(smAttachmentDao.getBasicHQL2(), attachmentForm));

		if (null == smAttachmentList || smAttachmentList.size() == 0)
		{
			smAttachmentList = new ArrayList<Sm_Attachment>();
		}

		// 查询附件类型
		List<Sm_Attachment> smList = null;
		Sm_AttachmentCfgForm attachmentCfgForm = new Sm_AttachmentCfgForm();
		attachmentCfgForm.setTheState(S_TheState.Normal);
		attachmentCfgForm.setBusiType(busiCode);
		List<Sm_AttachmentCfg> smAttachmentCfgList = sm_AttachmentCfgDao
				.findByPage(sm_AttachmentCfgDao.getQuery(sm_AttachmentCfgDao.getBasicHQL(), attachmentCfgForm));
		if (null == smAttachmentCfgList || smAttachmentCfgList.size() == 0)
		{
			smAttachmentCfgList = new ArrayList<Sm_AttachmentCfg>();
		}
		else
		{
			for (Sm_Attachment sm_Attachment : smAttachmentList)
			{
				Long cfgTableId = sm_Attachment.getAttachmentCfg().getTableId();

				for (Sm_AttachmentCfg sm_AttachmentCfg : smAttachmentCfgList)
				{
					if (sm_AttachmentCfg.getTableId() == cfgTableId)
					{
						smList = sm_AttachmentCfg.getSmAttachmentList();
						if (null == smList || smList.size() == 0)
						{
							smList = new ArrayList<Sm_Attachment>();
						}
						smList.add(sm_Attachment);
						sm_AttachmentCfg.setSmAttachmentList(smList);
					}
				}
			}
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tg_PjRiskRating", tg_PjRiskRating);
		properties.put("smAttachmentCfgList", smAttachmentCfgList);
		
		return properties;
	}
}
