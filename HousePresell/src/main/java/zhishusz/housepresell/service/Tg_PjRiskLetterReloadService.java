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
import zhishusz.housepresell.controller.form.Tg_OtherRiskInfoForm;
import zhishusz.housepresell.controller.form.Tg_PjRiskAssessmentForm;
import zhishusz.housepresell.controller.form.Tg_PjRiskLetterForm;
import zhishusz.housepresell.controller.form.Tg_PjRiskLetterReceiverForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tg_OtherRiskInfoDao;
import zhishusz.housepresell.database.dao.Tg_PjRiskAssessmentDao;
import zhishusz.housepresell.database.dao.Tg_PjRiskLetterDao;
import zhishusz.housepresell.database.dao.Tg_PjRiskLetterReceiverDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tg_OtherRiskInfo;
import zhishusz.housepresell.database.po.Tg_PjRiskAssessment;
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
public class Tg_PjRiskLetterReloadService
{
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Tg_PjRiskAssessmentDao tg_PjRiskAssessmentDao;
	@Autowired
	private Tg_OtherRiskInfoDao tg_OtherRiskInfoDao;
	
	MyDatetime myDatetime = MyDatetime.getInstance();
	
	private static final String BUSI_CODE = "21020304";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"
	
	public Properties execute(Tg_PjRiskLetterForm model)
	{
		Properties properties = new MyProperties();
		
		Long projectId = model.getTableId();
		
		String riskAssessment = "";
		
		String riskNotification = "";
		
		Tg_PjRiskAssessmentForm tg_PjRiskAssessmentForm = new Tg_PjRiskAssessmentForm();
		
		Tg_OtherRiskInfoForm tg_OtherRiskInfoForm = new Tg_OtherRiskInfoForm();
		
		if (null != projectId)
		{
			//查询预售项目
			Empj_ProjectInfo empj_ProjectInfo = empj_ProjectInfoDao.findById(projectId);
			if(empj_ProjectInfo != null)
			{
				tg_PjRiskAssessmentForm.setProject(empj_ProjectInfo);
				tg_PjRiskAssessmentForm.setTheState(S_TheState.Normal);
				
				Integer assessmentCount = tg_PjRiskAssessmentDao.findByPage_Size(tg_PjRiskAssessmentDao.getQuery_Size(tg_PjRiskAssessmentDao.getSpecialHQL(), tg_PjRiskAssessmentForm));
				
				List<Tg_PjRiskAssessment> tg_PjRiskAssessmentList;
				if(assessmentCount > 0)
				{
					tg_PjRiskAssessmentList = tg_PjRiskAssessmentDao.findByPage(tg_PjRiskAssessmentDao.getQuery(tg_PjRiskAssessmentDao.getSpecialHQL(), tg_PjRiskAssessmentForm));
					
					Tg_PjRiskAssessment tg_PjRiskAssessment = tg_PjRiskAssessmentList.get(0);
					
					riskAssessment = tg_PjRiskAssessment.getRiskAssessment();					
				}

				
				tg_OtherRiskInfoForm.setProject(empj_ProjectInfo);
				tg_OtherRiskInfoForm.setTheState(S_TheState.Normal);
				tg_OtherRiskInfoForm.setIsUsed(true);
				
				Integer totalCount = tg_OtherRiskInfoDao.findByPage_Size(tg_OtherRiskInfoDao.getQuery_Size(tg_OtherRiskInfoDao.getSpecialHQL(), tg_OtherRiskInfoForm));
				
				List<Tg_OtherRiskInfo> tg_OtherRiskInfoList;
				if(totalCount > 0)
				{
					tg_OtherRiskInfoList = tg_OtherRiskInfoDao.findByPage(tg_OtherRiskInfoDao.getQuery(tg_OtherRiskInfoDao.getSpecialHQL(), tg_OtherRiskInfoForm));
					
					Tg_OtherRiskInfo tg_OtherRiskInfo = tg_OtherRiskInfoList.get(0);
					
					riskNotification = tg_OtherRiskInfo.getRiskInfo();
				}
								
			}
		}
		
		properties.put("riskAssessment", riskAssessment);
		properties.put("riskNotification", riskNotification);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
