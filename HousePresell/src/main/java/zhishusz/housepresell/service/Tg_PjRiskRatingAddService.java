package zhishusz.housepresell.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Tg_PjRiskRatingForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tg_PjRiskRatingDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tg_PjRiskRating;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
	
/*
 * Service添加操作：项目风险评级
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_PjRiskRatingAddService
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
	private Sm_AttachmentDao smAttachmentDao;
	@Autowired
	private Sm_AttachmentCfgDao smAttachmentCfgDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	
	private static final String BUSI_CODE = "21020304";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"
	
	public Properties execute(Tg_PjRiskRatingForm model)
	{
		Properties properties = new MyProperties();

		Sm_User user = model.getUser();
		
		Long projectId = model.getProjectId();
		String operateDate = model.getOperateDate();
		String theLevel = model.getTheLevel();
		String riskNotification = model.getRiskNotification();
		
	
		if(projectId == null || projectId < 1)
		{
			return MyBackInfo.fail(properties, "请选择项目");
		}
		
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

		Empj_ProjectInfo project = (Empj_ProjectInfo)empj_ProjectInfoDao.findById(projectId);
		if(project == null)
		{
			return MyBackInfo.fail(properties, "项目信息有误");
		}
		
		// 查询开发企业
		Emmp_CompanyInfo emmp_CompanyInfo = project.getDevelopCompany();
		if(emmp_CompanyInfo == null){
			return MyBackInfo.fail(properties, "为查询到有效的开发企业数据！");
		}
		
		//查询所属区域
		Sm_CityRegionInfo sm_CityRegionInfo = project.getCityRegion();
		if(sm_CityRegionInfo == null){
			return MyBackInfo.fail(properties, "为查询到有效的区域！");
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
		
		
		
		
		
		
		
		
		Tg_PjRiskRating tg_PjRiskRating = new Tg_PjRiskRating();
		tg_PjRiskRating.setTheState(S_TheState.Normal);
		tg_PjRiskRating.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE));
		tg_PjRiskRating.setUserStart(user);
		tg_PjRiskRating.setCreateTimeStamp(System.currentTimeMillis());
		tg_PjRiskRating.setUserUpdate(user);
		tg_PjRiskRating.setLastUpdateTimeStamp(System.currentTimeMillis());

		tg_PjRiskRating.setCityRegion(sm_CityRegionInfo);
		tg_PjRiskRating.setTheNameOfCityRegion(sm_CityRegionInfo.getTheName());
		tg_PjRiskRating.setDevelopCompany(emmp_CompanyInfo);
		tg_PjRiskRating.seteCodeOfDevelopCompany(emmp_CompanyInfo.geteCode());
		tg_PjRiskRating.setProject(project);
		tg_PjRiskRating.setTheNameOfProject(project.getTheName());
		tg_PjRiskRating.setOperateDate(operateDate);
		tg_PjRiskRating.setTheLevel(theLevel);
		tg_PjRiskRating.setRiskNotification(riskNotification);
		Serializable entity = tg_PjRiskRatingDao.save(tg_PjRiskRating);

		// 附件信息
		String smAttachmentList = null;
		if (null != model.getSmAttachmentList())
		{
			smAttachmentList = model.getSmAttachmentList().toString();
		}

		List<Sm_Attachment> gasList = JSON.parseArray(smAttachmentList, Sm_Attachment.class);

		if (null != gasList && gasList.size() > 0)
		{
			for (Sm_Attachment sm_Attachment : gasList)
			{
				// 查询附件配置表
				Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
				form.seteCode(sm_Attachment.getSourceType());
				Sm_AttachmentCfg sm_AttachmentCfg = smAttachmentCfgDao
						.findOneByQuery_T(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));

				sm_Attachment.setAttachmentCfg(sm_AttachmentCfg);
				sm_Attachment.setSourceId(entity.toString());
				sm_Attachment.setTheState(S_TheState.Normal);
				smAttachmentDao.save(sm_Attachment);
			}
		}
				
		properties.put("tableId",entity.toString());
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
