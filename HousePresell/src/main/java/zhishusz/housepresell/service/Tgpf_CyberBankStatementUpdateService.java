package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementForm;
import zhishusz.housepresell.database.dao.Tgpf_CyberBankStatementDao;
import zhishusz.housepresell.database.po.Tgpf_CyberBankStatement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;

/*
 * Service更新操作：网银对账-后台上传的账单原始Excel数据-主表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_CyberBankStatementUpdateService
{
	@Autowired
	private Tgpf_CyberBankStatementDao tgpf_CyberBankStatementDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	public Properties execute(Tgpf_CyberBankStatementForm model)
	{
		Properties properties = new MyProperties();
		
		Integer theState = model.getTheState();
		String busiState = model.getBusiState();
		String eCode = model.geteCode();
		Long userStartId = model.getUserStartId();
		Long createTimeStamp = model.getCreateTimeStamp();
		Long lastUpdateTimeStamp = model.getLastUpdateTimeStamp();
		Long userRecordId = model.getUserRecordId();
		Long recordTimeStamp = model.getRecordTimeStamp();
		String theNameOfBank = model.getTheNameOfBank();
		String theAccountOfBankAccountEscrowed = model.getTheAccountOfBankAccountEscrowed();
		String theNameOfBankAccountEscrowed = model.getTheNameOfBankAccountEscrowed();
		String theNameOfBankBranch = model.getTheNameOfBankBranch();
		Integer reconciliationState = model.getReconciliationState();
		String reconciliationUser = model.getReconciliationUser();
		String orgFilePath = model.getOrgFilePath();
		
		if(theState == null || theState < 1)
		{
			return MyBackInfo.fail(properties, "'theState'不能为空");
		}
		if(busiState == null || busiState.length()< 1)
		{
			return MyBackInfo.fail(properties, "'busiState'不能为空");
		}
		if(eCode == null || eCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCode'不能为空");
		}
		if(userStartId == null || userStartId < 1)
		{
			return MyBackInfo.fail(properties, "'userStart'不能为空");
		}
		if(createTimeStamp == null || createTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'createTimeStamp'不能为空");
		}
		if(lastUpdateTimeStamp == null || lastUpdateTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'lastUpdateTimeStamp'不能为空");
		}
		if(userRecordId == null || userRecordId < 1)
		{
			return MyBackInfo.fail(properties, "'userRecord'不能为空");
		}
		if(recordTimeStamp == null || recordTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'recordTimeStamp'不能为空");
		}
		if(theNameOfBank == null || theNameOfBank.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theNameOfBank'不能为空");
		}
		if(theAccountOfBankAccountEscrowed == null || theAccountOfBankAccountEscrowed.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theAccountOfBankAccountEscrowed'不能为空");
		}
		if(theNameOfBankAccountEscrowed == null || theNameOfBankAccountEscrowed.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theNameOfBankAccountEscrowed'不能为空");
		}
		if(theNameOfBankBranch == null || theNameOfBankBranch.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theNameOfBankBranch'不能为空");
		}
		if(reconciliationState == null || reconciliationState < 1)
		{
			return MyBackInfo.fail(properties, "'reconciliationState'不能为空");
		}
		if(reconciliationUser == null || reconciliationUser.length() == 0)
		{
			return MyBackInfo.fail(properties, "'reconciliationUser'不能为空");
		}
		if(orgFilePath == null || orgFilePath.length() == 0)
		{
			return MyBackInfo.fail(properties, "'orgFilePath'不能为空");
		}
		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "'userStart(Id:" + userStartId + ")'不存在");
		}
		Sm_User userRecord = (Sm_User)sm_UserDao.findById(userRecordId);
		if(userRecord == null)
		{
			return MyBackInfo.fail(properties, "'userRecord(Id:" + userRecordId + ")'不存在");
		}
	
		Long tgpf_CyberBankStatementId = model.getTableId();
		Tgpf_CyberBankStatement tgpf_CyberBankStatement = (Tgpf_CyberBankStatement)tgpf_CyberBankStatementDao.findById(tgpf_CyberBankStatementId);
		if(tgpf_CyberBankStatement == null)
		{
			return MyBackInfo.fail(properties, "'Tgpf_CyberBankStatement(Id:" + tgpf_CyberBankStatementId + ")'不存在");
		}
		
		tgpf_CyberBankStatement.setTheState(theState);
		tgpf_CyberBankStatement.setBusiState(busiState);
		tgpf_CyberBankStatement.seteCode(eCode);
		tgpf_CyberBankStatement.setUserStart(userStart);
		tgpf_CyberBankStatement.setCreateTimeStamp(createTimeStamp);
		tgpf_CyberBankStatement.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		tgpf_CyberBankStatement.setUserRecord(userRecord);
		tgpf_CyberBankStatement.setRecordTimeStamp(recordTimeStamp);
		tgpf_CyberBankStatement.setTheNameOfBank(theNameOfBank);
		tgpf_CyberBankStatement.setTheAccountOfBankAccountEscrowed(theAccountOfBankAccountEscrowed);
		tgpf_CyberBankStatement.setTheNameOfBankAccountEscrowed(theNameOfBankAccountEscrowed);
		tgpf_CyberBankStatement.setTheNameOfBankBranch(theNameOfBankBranch);
		tgpf_CyberBankStatement.setReconciliationState(reconciliationState);
		tgpf_CyberBankStatement.setReconciliationUser(reconciliationUser);
		tgpf_CyberBankStatement.setOrgFilePath(orgFilePath);
	
		tgpf_CyberBankStatementDao.save(tgpf_CyberBankStatement);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
