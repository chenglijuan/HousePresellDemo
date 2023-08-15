package zhishusz.housepresell.service;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Tg_PjRiskAssessmentForm;
import zhishusz.housepresell.database.dao.Tg_PjRiskAssessmentDao;
import zhishusz.housepresell.database.po.Tg_PjRiskAssessment;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
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
 * Service添加操作：项目风险评估
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_PjRiskAssessmentAddService
{
	@Autowired
	private Tg_PjRiskAssessmentDao tg_PjRiskAssessmentDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	
	//附件
	@Autowired
	private Sm_AttachmentDao smAttachmentDao;
	@Autowired
	private Sm_AttachmentCfgDao smAttachmentCfgDao;
	
	//业务编码
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tg_PjRiskAssessmentForm model)
	{
		Properties properties = new MyProperties();

		Integer theState = S_TheState.Normal;
		Long userStartId = model.getUserId();//获取登陆人Id
		Long createTimeStamp = System.currentTimeMillis();
		Long cityRegionId = model.getCityRegionId();
		Long developCompanyId = model.getDevelopCompanyId();
		Long projectId = model.getProjectId();
		String assessDate = model.getAssessDate();
		String riskAssessment = model.getRiskAssessment();
		String busiCode = model.getBusiCode();//业务编码
		
		// 附件信息
		String smAttachmentList = null;
		if (null != model.getSmAttachmentList())
		{
			smAttachmentList = model.getSmAttachmentList().toString();
		}
		
		if(cityRegionId == null || cityRegionId < 1)
		{
			return MyBackInfo.fail(properties, "区域主键不能为空");
		}
		if(developCompanyId == null || developCompanyId < 1)
		{
			return MyBackInfo.fail(properties, "开发企业主键不能为空");
		}
		if(projectId == null || projectId < 1)
		{
			return MyBackInfo.fail(properties, "'预售项目主键不能为空");
		}
		if(assessDate == null || assessDate.length() == 0)
		{
			return MyBackInfo.fail(properties, "风险评估日期不能为空");
		}
		if(riskAssessment == null || riskAssessment.length() == 0)
		{
			return MyBackInfo.fail(properties, "风险评估不能为空");
		}

		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
		Sm_CityRegionInfo cityRegion = (Sm_CityRegionInfo)sm_CityRegionInfoDao.findById(cityRegionId);
		Emmp_CompanyInfo developCompany = (Emmp_CompanyInfo)emmp_CompanyInfoDao.findById(developCompanyId);
		Empj_ProjectInfo project = (Empj_ProjectInfo)empj_ProjectInfoDao.findById(projectId);
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "未查询到有效的用户信息");
		}
		if(cityRegion == null)
		{
			return MyBackInfo.fail(properties, "未查询到有效的区域");
		}
		if(developCompany == null)
		{
			return MyBackInfo.fail(properties, "未查询到有效的企业信息");
		}
		if(project == null)
		{
			return MyBackInfo.fail(properties, "未查询到有效的项目信息");
		}
	
		Tg_PjRiskAssessment tg_PjRiskAssessment = new Tg_PjRiskAssessment();
		tg_PjRiskAssessment.setTheState(theState);
		tg_PjRiskAssessment.seteCode(sm_BusinessCodeGetService.execute(busiCode));
		tg_PjRiskAssessment.setUserStart(userStart);
		tg_PjRiskAssessment.setCreateTimeStamp(createTimeStamp);
		tg_PjRiskAssessment.setCityRegion(cityRegion);
		tg_PjRiskAssessment.setTheNameOfCityRegion(cityRegion.getTheName());
		tg_PjRiskAssessment.setDevelopCompany(developCompany);
		tg_PjRiskAssessment.seteCodeOfDevelopCompany(developCompany.geteCode());
		tg_PjRiskAssessment.setProject(project);
		tg_PjRiskAssessment.setTheNameOfProject(project.getTheName());
		tg_PjRiskAssessment.setAssessDate(assessDate);
		tg_PjRiskAssessment.setRiskAssessment(riskAssessment);
		Serializable entity = tg_PjRiskAssessmentDao.save(tg_PjRiskAssessment);
		
		//保存附件
		List<Sm_Attachment> gasList = JSON.parseArray(smAttachmentList, Sm_Attachment.class);
		
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

		for (Sm_Attachment sm_Attachment : gasList)
		{
			//查询附件配置表
			Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
			form.seteCode(sm_Attachment.getSourceType());
			Sm_AttachmentCfg sm_AttachmentCfg = smAttachmentCfgDao.findOneByQuery_T(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));
			
			sm_Attachment.setAttachmentCfg(sm_AttachmentCfg);
			sm_Attachment.setSourceId(entity.toString());
			sm_Attachment.setTheState(S_TheState.Normal);
			smAttachmentDao.save(sm_Attachment);
		}
		
		properties.put("tableId", new Long(entity.toString()));

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
