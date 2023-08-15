package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementSettleForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementSettleDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementSettle;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service更新操作：三方协议结算-主表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgxy_CoopAgreementSettleUpdateService
{
	@Autowired
	private Tgxy_CoopAgreementSettleDao tgxy_CoopAgreementSettleDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Sm_AttachmentDao smAttachmentDao;
	@Autowired
	private Sm_AttachmentCfgDao smAttachmentCfgDao;

	
	public Properties execute(Tgxy_CoopAgreementSettleForm model)
	{
		Properties properties = new MyProperties();
		
		Integer theState = model.getTheState();
		String busiState = model.getBusiState();
		Long userStartId = model.getUserStartId();
		Long createTimeStamp = model.getCreateTimeStamp();
		Long lastUpdateTimeStamp = model.getLastUpdateTimeStamp();
		Long userRecordId = model.getUserRecordId();
		Long recordTimeStamp = model.getRecordTimeStamp();
		String eCode = model.geteCode();
		String signTimeStamp = model.getSignTimeStamp();
		Long agentCompanyId = model.getAgentCompanyId();
		String applySettlementDate = model.getApplySettlementDate();
		String startSettlementDate = model.getStartSettlementDate();
		String endSettlementDate = model.getEndSettlementDate();
		Integer protocolNumbers = model.getProtocolNumbers();
		Integer settlementState = model.getSettlementState();
		
//		if(theState == null || theState < 1)
//		{
//			return MyBackInfo.fail(properties, "'theState'不能为空");
//		}
//		if(busiState == null || busiState.length()< 1)
//		{
//			return MyBackInfo.fail(properties, "'busiState'不能为空");
//		}
//		if(userStartId == null || userStartId < 1)
//		{
//			return MyBackInfo.fail(properties, "'userStart'不能为空");
//		}
//		if(createTimeStamp == null || createTimeStamp < 1)
//		{
//			return MyBackInfo.fail(properties, "'createTimeStamp'不能为空");
//		}
//		if(lastUpdateTimeStamp == null || lastUpdateTimeStamp < 1)
//		{
//			return MyBackInfo.fail(properties, "'lastUpdateTimeStamp'不能为空");
//		}
//		if(userRecordId == null || userRecordId < 1)
//		{
//			return MyBackInfo.fail(properties, "'userRecord'不能为空");
//		}
//		if(recordTimeStamp == null || recordTimeStamp < 1)
//		{
//			return MyBackInfo.fail(properties, "'recordTimeStamp'不能为空");
//		}
//		if(eCode == null || eCode.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "'eCode'不能为空");
//		}
//		if(signTimeStamp == null || signTimeStamp.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "'signTimeStamp'不能为空");
//		}
//		if(agentCompanyId == null || agentCompanyId < 1)
//		{
//			return MyBackInfo.fail(properties, "'agentCompany'不能为空");
//		}
//		if(applySettlementDate == null || applySettlementDate.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "'applySettlementDate'不能为空");
//		}
//		if(startSettlementDate == null || startSettlementDate.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "'startSettlementDate'不能为空");
//		}
//		if(endSettlementDate == null || endSettlementDate.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "'endSettlementDate'不能为空");
//		}
//		if(protocolNumbers == null || protocolNumbers < 1)
//		{
//			return MyBackInfo.fail(properties, "'protocolNumbers'不能为空");
//		}
//		if(settlementState == null || settlementState < 1)
//		{
//			return MyBackInfo.fail(properties, "'settlementState'不能为空");
//		}
//		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
//		if(userStart == null)
//		{
//			return MyBackInfo.fail(properties, "'userStart(Id:" + userStartId + ")'不存在");
//		}
//		Sm_User userRecord = (Sm_User)sm_UserDao.findById(userRecordId);
//		if(userRecord == null)
//		{
//			return MyBackInfo.fail(properties, "'userRecord(Id:" + userRecordId + ")'不存在");
//		}
//		Emmp_CompanyInfo agentCompany = (Emmp_CompanyInfo)emmp_CompanyInfoDao.findById(agentCompanyId);
//		if(agentCompany == null)
//		{
//			return MyBackInfo.fail(properties, "'agentCompany(Id:" + agentCompanyId + ")'不存在");
//		}
//	
//		Long tgxy_CoopAgreementSettleId = model.getTableId();
//		Tgxy_CoopAgreementSettle tgxy_CoopAgreementSettle = (Tgxy_CoopAgreementSettle)tgxy_CoopAgreementSettleDao.findById(tgxy_CoopAgreementSettleId);
//		if(tgxy_CoopAgreementSettle == null)
//		{
//			return MyBackInfo.fail(properties, "'Tgxy_CoopAgreementSettle(Id:" + tgxy_CoopAgreementSettleId + ")'不存在");
//		}
		
		Long tgxy_CoopAgreementSettleId = model.getTableId();
		Tgxy_CoopAgreementSettle tgxy_CoopAgreementSettle = (Tgxy_CoopAgreementSettle)tgxy_CoopAgreementSettleDao.findById(tgxy_CoopAgreementSettleId);
		Sm_User userStart = model.getUser();
		
		tgxy_CoopAgreementSettle.setTheState(S_TheState.Normal);
//		tgxy_CoopAgreementSettle.setBusiState(busiState);
//		tgxy_CoopAgreementSettle.setUserStart(userStart);
//		tgxy_CoopAgreementSettle.setCreateTimeStamp(createTimeStamp);
		tgxy_CoopAgreementSettle.setUserUpdate(userStart);
		tgxy_CoopAgreementSettle.setLastUpdateTimeStamp(System.currentTimeMillis());
//		tgxy_CoopAgreementSettle.setUserRecord(userRecord);
//		tgxy_CoopAgreementSettle.setRecordTimeStamp(recordTimeStamp);
//		tgxy_CoopAgreementSettle.seteCode(eCode);
//		tgxy_CoopAgreementSettle.setSignTimeStamp(signTimeStamp);
//		tgxy_CoopAgreementSettle.setAgentCompany(userStart.getCompany());
//		tgxy_CoopAgreementSettle.setApplySettlementDate(applySettlementDate);
//		tgxy_CoopAgreementSettle.setStartSettlementDate(startSettlementDate);
//		tgxy_CoopAgreementSettle.setEndSettlementDate(endSettlementDate);
//		tgxy_CoopAgreementSettle.setProtocolNumbers(protocolNumbers);
//		tgxy_CoopAgreementSettle.setSettlementState(settlementState);
	
		tgxy_CoopAgreementSettleDao.save(tgxy_CoopAgreementSettle);
				
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

			String sourceId = String.valueOf(tgxy_CoopAgreementSettleId);
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
					sm_Attachment.setSourceId(tgxy_CoopAgreementSettleId.toString());
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
