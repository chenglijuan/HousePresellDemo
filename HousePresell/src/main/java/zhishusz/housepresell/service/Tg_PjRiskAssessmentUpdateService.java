package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tg_PjRiskAssessmentForm;
import zhishusz.housepresell.database.dao.Tg_PjRiskAssessmentDao;
import zhishusz.housepresell.database.po.Tg_PjRiskAssessment;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;

/*
 * Service更新操作：项目风险评估
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_PjRiskAssessmentUpdateService
{
	@Autowired
	private Tg_PjRiskAssessmentDao tg_PjRiskAssessmentDao;
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
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tg_PjRiskAssessmentForm model)
	{
		Properties properties = new MyProperties();
		
		String busiCode = "21020301";
		
		Long cityRegionId = model.getCityRegionId();
		Long developCompanyId = model.getDevelopCompanyId();
		Long projectId = model.getProjectId();
		String assessDate = model.getAssessDate();
		String riskAssessment = model.getRiskAssessment();
		
		Long tg_PjRiskAssessmentId = model.getTableId();//风险评估Id
		
		if(cityRegionId == null || cityRegionId < 1)
		{
			return MyBackInfo.fail(properties, "'区域主键不能为空");
		}
		if(developCompanyId == null || developCompanyId < 1)
		{
			return MyBackInfo.fail(properties, "开发企业主键不能为空");
		}
		if(projectId == null || projectId < 1)
		{
			return MyBackInfo.fail(properties, "预售项目主键不能为空");
		}
		if(assessDate == null || assessDate.length() == 0)
		{
			return MyBackInfo.fail(properties, "风险评估日期不能为空");
		}
		if(riskAssessment == null || riskAssessment.length() == 0)
		{
			return MyBackInfo.fail(properties, "风险评估不能为空");
		}
		if(tg_PjRiskAssessmentId == null || tg_PjRiskAssessmentId < 1)
		{
			return MyBackInfo.fail(properties, "风险评估主键不能为空");
		}
		
		Sm_CityRegionInfo cityRegion = (Sm_CityRegionInfo)sm_CityRegionInfoDao.findById(cityRegionId);
		if(cityRegion == null)
		{
			return MyBackInfo.fail(properties, "未查询到有效的区域信息");
		}
		Emmp_CompanyInfo developCompany = (Emmp_CompanyInfo)emmp_CompanyInfoDao.findById(developCompanyId);
		if(developCompany == null)
		{
			return MyBackInfo.fail(properties, "未查询到开发企业信息");
		}
		Empj_ProjectInfo project = (Empj_ProjectInfo)empj_ProjectInfoDao.findById(projectId);
		if(project == null)
		{
			return MyBackInfo.fail(properties, "未查询到项目信息");
		}
	
		Tg_PjRiskAssessment tg_PjRiskAssessment = (Tg_PjRiskAssessment)tg_PjRiskAssessmentDao.findById(tg_PjRiskAssessmentId);
		if(tg_PjRiskAssessment == null)
		{
			return MyBackInfo.fail(properties, "未查询到风险评估信息");
		}
		
		tg_PjRiskAssessment.setCityRegion(cityRegion);
		tg_PjRiskAssessment.setTheNameOfCityRegion(cityRegion.getTheName());
		tg_PjRiskAssessment.setDevelopCompany(developCompany);
		tg_PjRiskAssessment.seteCodeOfDevelopCompany(developCompany.geteCode());
		tg_PjRiskAssessment.setProject(project);
		tg_PjRiskAssessment.setTheNameOfProject(project.getTheName());
		tg_PjRiskAssessment.setAssessDate(assessDate);
		tg_PjRiskAssessment.setRiskAssessment(riskAssessment);
	
		Serializable serializable = tg_PjRiskAssessmentDao.save(tg_PjRiskAssessment);
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

			String sourceId = String.valueOf(tg_PjRiskAssessmentId);
			from.setTheState(S_TheState.Normal);
			from.setSourceId(sourceId);

			// 查询附件
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
			
			/*
			 * xsz by time 2018-11-21 14:47:06
			 * 判断附件是否必须上传
			 */
			// 判断是否有必传
			Sm_AttachmentCfgForm sm_AttachmentCfgForm = new Sm_AttachmentCfgForm();
			sm_AttachmentCfgForm.setBusiType(busiCode);
			sm_AttachmentCfgForm.setTheState(S_TheState.Normal);
			List<Sm_AttachmentCfg> sm_AttachmentCfgList = smAttachmentCfgDao
					.findByPage(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), sm_AttachmentCfgForm));

			
			if (null != sm_AttachmentCfgList && sm_AttachmentCfgList.size() > 0)
			{

				for (Sm_AttachmentCfg sm_AttachmentCfg : sm_AttachmentCfgList)
				{
					// 根据业务判断是否有必传的附件配置
					if (sm_AttachmentCfg.getIsNeeded())
					{
						Boolean isExistAttachment = false;
		
						if (gasList.size() > 0)
						{
		
							for (Sm_Attachment sm_Attachment : gasList)
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
			

			if (null != gasList && gasList.size() > 0)
			{
				for (Sm_Attachment sm_Attachment : gasList)
				{
					//查询附件配置表
					Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
					form.seteCode(sm_Attachment.getSourceType());
					Sm_AttachmentCfg sm_AttachmentCfg = smAttachmentCfgDao.findOneByQuery_T(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));
					
					sm_Attachment.setAttachmentCfg(sm_AttachmentCfg);
					sm_Attachment.setSourceId(serializable.toString());
					sm_Attachment.setTheState(S_TheState.Normal);
					smAttachmentDao.save(sm_Attachment);
				}
			}
		}

		properties.put("tableId", new Long(serializable.toString()));

		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
