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
import zhishusz.housepresell.controller.form.Tg_PjRiskLetterForm;
import zhishusz.housepresell.controller.form.Tg_PjRiskLetterReceiverForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tg_PjRiskLetterDao;
import zhishusz.housepresell.database.dao.Tg_PjRiskLetterReceiverDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tg_PjRiskLetter;
import zhishusz.housepresell.database.po.Tg_PjRiskLetterReceiver;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
	
/*
 * Service添加操作：项目风险函
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_PjRiskLetterAddService
{
	@Autowired
	private Tg_PjRiskLetterDao tg_PjRiskLetterDao;
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
	private Tg_PjRiskLetterReceiverDao tg_PjRiskLetterReceiverDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	
	MyDatetime myDatetime = MyDatetime.getInstance();
	
	private static final String BUSI_CODE = "21020304";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"
	
	public Properties execute(Tg_PjRiskLetterForm model)
	{
		Properties properties = new MyProperties();
		
		Sm_User user = model.getUser();

		Long projectId = model.getProjectId();
		String releaseDate = myDatetime.dateToSimpleString(System.currentTimeMillis());
		String deliveryCompany = model.getDeliveryCompany();
		String riskNotification = model.getRiskNotification();
		String basicSituation = model.getBasicSituation();
		String riskAssessment = model.getRiskAssessment();

		if(deliveryCompany == null || deliveryCompany.length() == 0)
		{
			return MyBackInfo.fail(properties, "请输入主送单位");
		}
		if(riskNotification == null || riskNotification.length() == 0)
		{
			return MyBackInfo.fail(properties, "请输入风险提示");
		}
		if(basicSituation == null || basicSituation.length() == 0)
		{
			return MyBackInfo.fail(properties, "请输入基本情况");
		}
		if(riskAssessment == null || riskAssessment.length() == 0)
		{
			return MyBackInfo.fail(properties, "请输入项目风险评估");
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
	
		Tg_PjRiskLetter tg_PjRiskLetter = new Tg_PjRiskLetter();
		tg_PjRiskLetter.setTheState(S_TheState.Normal);
		tg_PjRiskLetter.setBusiState("0");
		tg_PjRiskLetter.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE));
		tg_PjRiskLetter.setUserStart(user);
		tg_PjRiskLetter.setCreateTimeStamp(System.currentTimeMillis());
		tg_PjRiskLetter.setUserUpdate(user);
		tg_PjRiskLetter.setLastUpdateTimeStamp(System.currentTimeMillis());
		tg_PjRiskLetter.setCityRegion(sm_CityRegionInfo);
		tg_PjRiskLetter.setTheNameOfCityRegion(sm_CityRegionInfo.getTheName());
		tg_PjRiskLetter.setDevelopCompany(emmp_CompanyInfo);
		tg_PjRiskLetter.seteCodeOfDevelopCompany(emmp_CompanyInfo.getTheName());
		tg_PjRiskLetter.setProject(project);
		tg_PjRiskLetter.setTheNameOfProject(project.getTheName());
		tg_PjRiskLetter.setReleaseDate(releaseDate);
		tg_PjRiskLetter.setDeliveryCompany(deliveryCompany);
		tg_PjRiskLetter.setRiskNotification(riskNotification);
		tg_PjRiskLetter.setBasicSituation(basicSituation);
		tg_PjRiskLetter.setRiskAssessment(riskAssessment);
		Serializable entity = tg_PjRiskLetterDao.save(tg_PjRiskLetter);
		
		
		List<Tg_PjRiskLetterReceiverForm> tg_PjRiskLetterReceiverList = JSON.parseArray(model.getTg_PjRiskLetterReceiverList(), Tg_PjRiskLetterReceiverForm.class);
		
		for(Tg_PjRiskLetterReceiverForm tg_PjRiskLetterReceiverForm : tg_PjRiskLetterReceiverList)
		{
			Tg_PjRiskLetterReceiver tg_PjRiskLetterReceiver = new Tg_PjRiskLetterReceiver();
			
			tg_PjRiskLetterReceiver.setTheState(S_TheState.Normal);
//			tg_PjRiskLetterReceiver.setBusiState(S_TheState.Normal);
//			tg_PjRiskLetterReceiver.seteCode();
			tg_PjRiskLetterReceiver.setUserStart(user);
			tg_PjRiskLetterReceiver.setTg_PjRiskLetter(tg_PjRiskLetter);
			tg_PjRiskLetterReceiver.setCreateTimeStamp(System.currentTimeMillis());
			tg_PjRiskLetterReceiver.setUserUpdate(user);
			tg_PjRiskLetterReceiver.setLastUpdateTimeStamp(System.currentTimeMillis());
			tg_PjRiskLetterReceiver.setTheName(tg_PjRiskLetterReceiverForm.getTheName());
			tg_PjRiskLetterReceiver.setEmail(tg_PjRiskLetterReceiverForm.getEmail());
			tg_PjRiskLetterReceiver.setSendWay(1);
			tg_PjRiskLetterReceiver.setSendStatement(0);	
			
			tg_PjRiskLetterReceiverDao.save(tg_PjRiskLetterReceiver);
		}


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
		
		properties.put("tableId", entity.toString());
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
