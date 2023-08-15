package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_AFForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_CfgDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import java.util.List;
import java.util.List;

/*
 * Service更新操作：审批流-申请单
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_ApprovalProcess_AFUpdateService
{
	@Autowired
	private Sm_ApprovalProcess_AFDao sm_ApprovalProcess_AFDao;
	@Autowired
	private Sm_ApprovalProcess_CfgDao sm_ApprovalProcess_CfgDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	
	public Properties execute(Sm_ApprovalProcess_AFForm model)
	{
		Properties properties = new MyProperties();
		
		Long configurationId = model.getConfigurationId();
		Long companyInfoId = model.getCompanyInfoId();
		String eCode = model.geteCode();
		Long startTimeStamp = model.getStartTimeStamp();
		Integer theState = model.getTheState();
		String busiState = model.getBusiState();
		Long sourceId = model.getSourceId();
		String sourceType = model.getSourceType();
		String orgObjJsonFilePath = model.getOrgObjJsonFilePath();
		String expectObjJsonFilePath = model.getExpectObjJsonFilePath();
		List attachmentList = model.getAttachmentList();
		List workFlowList = model.getWorkFlowList();
		Integer currentIndex = model.getCurrentIndex();
		
		if(configurationId == null || configurationId < 1)
		{
			return MyBackInfo.fail(properties, "'configuration'不能为空");
		}
		if(companyInfoId == null || companyInfoId < 1)
		{
			return MyBackInfo.fail(properties, "'companyInfo'不能为空");
		}
		if(eCode == null || eCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCode'不能为空");
		}
		if(startTimeStamp == null || startTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'startTimeStamp'不能为空");
		}
		if(theState == null || theState < 1)
		{
			return MyBackInfo.fail(properties, "'theState'不能为空");
		}
		if(busiState == null || busiState.length() < 1)
		{
			return MyBackInfo.fail(properties, "'busiState'不能为空");
		}
		if(sourceId == null || sourceId < 1)
		{
			return MyBackInfo.fail(properties, "'sourceId'不能为空");
		}
		if(sourceType == null || sourceType.length() == 0)
		{
			return MyBackInfo.fail(properties, "'sourceType'不能为空");
		}
		if(orgObjJsonFilePath == null || orgObjJsonFilePath.length() == 0)
		{
			return MyBackInfo.fail(properties, "'orgObjJsonFilePath'不能为空");
		}
		if(expectObjJsonFilePath == null || expectObjJsonFilePath.length() == 0)
		{
			return MyBackInfo.fail(properties, "'expectObjJsonFilePath'不能为空");
		}
		if(attachmentList == null)
		{
			return MyBackInfo.fail(properties, "'attachmentList'不能为空");
		}
		if(workFlowList == null)
		{
			return MyBackInfo.fail(properties, "'workFlowList'不能为空");
		}
		if(currentIndex == null || currentIndex < 1)
		{
			return MyBackInfo.fail(properties, "'currentIndex'不能为空");
		}
		Sm_ApprovalProcess_Cfg configuration = (Sm_ApprovalProcess_Cfg)sm_ApprovalProcess_CfgDao.findById(configurationId);
		if(configuration == null)
		{
			return MyBackInfo.fail(properties, "'configuration(Id:" + configurationId + ")'不存在");
		}
		Emmp_CompanyInfo companyInfo = (Emmp_CompanyInfo)emmp_CompanyInfoDao.findById(companyInfoId);
		if(companyInfo == null)
		{
			return MyBackInfo.fail(properties, "'companyInfo(Id:" + companyInfoId + ")'不存在");
		}
	
		Long sm_ApprovalProcess_AFId = model.getTableId();
		Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = (Sm_ApprovalProcess_AF)sm_ApprovalProcess_AFDao.findById(sm_ApprovalProcess_AFId);
		if(sm_ApprovalProcess_AF == null)
		{
			return MyBackInfo.fail(properties, "'Sm_ApprovalProcess_AF(Id:" + sm_ApprovalProcess_AFId + ")'不存在");
		}
		
		sm_ApprovalProcess_AF.setConfiguration(configuration);
		sm_ApprovalProcess_AF.setCompanyInfo(companyInfo);
		sm_ApprovalProcess_AF.seteCode(eCode);
		sm_ApprovalProcess_AF.setStartTimeStamp(startTimeStamp);
		sm_ApprovalProcess_AF.setTheState(theState);
		sm_ApprovalProcess_AF.setBusiState(busiState);
		sm_ApprovalProcess_AF.setSourceId(sourceId);
		sm_ApprovalProcess_AF.setSourceType(sourceType);
		sm_ApprovalProcess_AF.setOrgObjJsonFilePath(orgObjJsonFilePath);
		sm_ApprovalProcess_AF.setExpectObjJsonFilePath(expectObjJsonFilePath);
		sm_ApprovalProcess_AF.setAttachmentList(attachmentList);
		sm_ApprovalProcess_AF.setWorkFlowList(workFlowList);
		sm_ApprovalProcess_AF.setCurrentIndex(null);
		sm_ApprovalProcess_AFDao.save(sm_ApprovalProcess_AF);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
