package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tgxy_DepositAgreementForm;
import zhishusz.housepresell.database.dao.Tgxy_DepositAgreementDao;
import zhishusz.housepresell.database.po.Tgxy_DepositAgreement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;
import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.Emmp_BankInfo;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.dao.Emmp_BankBranchDao;
import zhishusz.housepresell.database.dao.Emmp_BankInfoDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;

/*
 * Service更新操作：协定存款协议
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgxy_DepositAgreementUpdateService
{
	private static final String BUSI_CODE = "06110101";
	@Autowired
	private Tgxy_DepositAgreementDao tgxy_DepositAgreementDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Emmp_BankInfoDao emmp_BankInfoDao;
	@Autowired
	private Emmp_BankBranchDao emmp_BankBranchDao;
	@Autowired
	private Tgxy_BankAccountEscrowedDao tgxy_BankAccountEscrowedDao;
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;
	@Autowired
	private Sm_AttachmentCfgDao smAttachmentCfgDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tgxy_DepositAgreementForm model)
	{
		Properties properties = new MyProperties();
		
		/*
		 * xsz by time 2018-8-22 17:25:23
		 * 修改协定存款协议
		 * 1.编号（不可修改）、银行Id、银行名称、开户行名称、托管账号、协定存款利率、起始金额、签订日期、期限、生效日期、到期日期、备注、
		 * 修改人id
		 * 
		 * 2.生效日期<到期日期
		 * 
		 */
		String eCode = model.geteCode(); // 编号
		// Long userUpdateId = model.getUserUpdateId();// 修改人Id
		Long userUpdateId = model.getUserId();// 修改人Id
		// Long bankId = model.getBankId(); // 银行Id
		// Long bankOfDepositId = model.getBankOfDepositId();// 开户行Id
		// Long escrowAccountId = model.getEscrowAccountId();// 托管账户Id
		Double depositRate = model.getDepositRate(); // 协定存款利率（%）
		Double orgAmount = model.getOrgAmount(); // 起始金额（万元）
		String signDate = model.getSignDate(); // 签订日期
		String timeLimit = model.getTimeLimit(); // 期限
		String beginExpirationDate = model.getBeginExpirationDate();// 生效日期
		String endExpirationDate = model.getEndExpirationDate(); // 到期日期
		String remark = model.getRemark(); // 备注

		/*
		 * 非空校验字段：
		 * 编号、银行、开户行、托管账户、协定存款利率、起始金额、签订日期、期限、生效日期、到期日期、创建人id
		 */
		if (userUpdateId == null || userUpdateId < 1)
		{
			return MyBackInfo.fail(properties, "请先进行登录");
		}
		// if (bankId == null || bankId < 1)
		// {
		// return MyBackInfo.fail(properties, "银行不能为空");
		// }
		// if (bankOfDepositId == null || bankOfDepositId < 1)
		// {
		// return MyBackInfo.fail(properties, "开户行不能为空");
		// }
		// if (escrowAccountId == null || escrowAccountId < 1)
		// {
		// return MyBackInfo.fail(properties, "托管账户不能为空");
		// }
		/*if (depositRate == null)
		{
			return MyBackInfo.fail(properties, "协定存款利率不能为空");
		}*/
		if (orgAmount == null || orgAmount < 1)
		{
			return MyBackInfo.fail(properties, "起始金额不能为空");
		}
		if (signDate == null || signDate.trim().isEmpty())
		{
			return MyBackInfo.fail(properties, " 签订日期不能为空");
		}
		if (timeLimit == null || timeLimit.length() == 0)
		{
			return MyBackInfo.fail(properties, "期限不能为空");
		}
		if (beginExpirationDate == null || beginExpirationDate.trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "生效日期不能为空");
		}
		if (endExpirationDate == null || endExpirationDate.trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "到期日期不能为空");
		}

		// 校验生效日期与到期日期大小关系（生效日期<到期日期）
		if (beginExpirationDate.compareTo(endExpirationDate) > 0)
		{
			return MyBackInfo.fail(properties, "生效日期：（" + beginExpirationDate + "）大于到期日期：（" + endExpirationDate + "）");
		}

		// 查询关联信息
		Sm_User userUpdate = (Sm_User) sm_UserDao.findById(userUpdateId);
		if (userUpdate == null)
		{
			return MyBackInfo.fail(properties, "查询操作人信息为空");
		}

		// Emmp_BankInfo bank = (Emmp_BankInfo)
		// emmp_BankInfoDao.findById(bankId);
		// if (bank == null)
		// {
		// return MyBackInfo.fail(properties, "查询银行信息为空");
		// }
		//
		// Emmp_BankBranch bankOfDeposit = (Emmp_BankBranch)
		// emmp_BankBranchDao.findById(bankOfDepositId);
		// if (bankOfDeposit == null)
		// {
		// return MyBackInfo.fail(properties, "查询开户行信息为空");
		// }
		//
		// Tgxy_BankAccountEscrowed escrowAccount = (Tgxy_BankAccountEscrowed)
		// tgxy_BankAccountEscrowedDao
		// .findById(escrowAccountId);
		// if (escrowAccount == null)
		// {
		// return MyBackInfo.fail(properties, "查询托管账户信息为空");
		// }

		// 根据tableId查询协定存款协议相关信息
		Long tgxy_DepositAgreementId = model.getTableId();
		Tgxy_DepositAgreement tgxy_DepositAgreement = (Tgxy_DepositAgreement) tgxy_DepositAgreementDao
				.findById(tgxy_DepositAgreementId);
		if (tgxy_DepositAgreement == null)
		{
			return MyBackInfo.fail(properties, "该信息已失效，请刷新后重试");
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

		// 修改人信息
		tgxy_DepositAgreement.setUserUpdate(userUpdate);
		// 修改时间取当前系统时间
		tgxy_DepositAgreement.setLastUpdateTimeStamp(System.currentTimeMillis());

		// tgxy_DepositAgreement.setBank(bank);
		// tgxy_DepositAgreement.setTheNameOfBank(bank.getTheName());
		//
		// tgxy_DepositAgreement.setBankOfDeposit(bankOfDeposit);
		// tgxy_DepositAgreement.setTheNameOfDepositBank(bankOfDeposit.getTheName());
		//
		// tgxy_DepositAgreement.setEscrowAccount(escrowAccount);
		// tgxy_DepositAgreement.setTheAccountOfEscrowAccount(escrowAccount.getTheAccount());

		tgxy_DepositAgreement.setDepositRate(depositRate);
		tgxy_DepositAgreement.setOrgAmount(orgAmount);
		tgxy_DepositAgreement.setSignDate(signDate);
		tgxy_DepositAgreement.setTimeLimit(timeLimit);
		tgxy_DepositAgreement.setBeginExpirationDate(beginExpirationDate);
		tgxy_DepositAgreement.setEndExpirationDate(endExpirationDate);
		tgxy_DepositAgreement.setRemark(remark);
		tgxy_DepositAgreement.seteCode(eCode);

		tgxy_DepositAgreementDao.update(tgxy_DepositAgreement);
		
		/*
		 * xsz by time 2018-8-28 18:22:29
		 * 修改附件
		 * 附件需要先进行删除操作，然后进行重新上传保存功能
		 */
		String smAttachmentJson = null;
		// 查询原本附件信息
		Sm_AttachmentForm from = new Sm_AttachmentForm();

		String sourceId = String.valueOf(tgxy_DepositAgreementId);
		from.setTheState(S_TheState.Normal);
		from.setBusiType("06110101");
		from.setSourceId(sourceId);

		// 查询附件
		List<Sm_Attachment> smAttachmentList = sm_AttachmentDao
				.findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), from));
		// 删除附件
		if (null != smAttachmentList && smAttachmentList.size() > 0)
		{
			for (Sm_Attachment sm_Attachment : smAttachmentList)
			{
				sm_Attachment.setLastUpdateTimeStamp(userUpdateId);
				sm_Attachment.setLastUpdateTimeStamp(System.currentTimeMillis());
				sm_Attachment.setTheState(S_TheState.Deleted);
				sm_AttachmentDao.save(sm_Attachment);
			}
		}

		// 重新保存附件
		smAttachmentJson = model.getSmAttachmentList().toString();
		if (null != smAttachmentJson && !smAttachmentJson.trim().isEmpty())
		{
			List<Sm_Attachment> gasList = JSON.parseArray(smAttachmentJson, Sm_Attachment.class);
			for (Sm_Attachment sm_Attachment : gasList)
			{
				//查询附件配置表
				Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
				form.seteCode(sm_Attachment.getSourceType());
				Sm_AttachmentCfg sm_AttachmentCfg = smAttachmentCfgDao.findOneByQuery_T(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));
				
				sm_Attachment.setAttachmentCfg(sm_AttachmentCfg);
				
				sm_Attachment.setSourceId(String.valueOf(tgxy_DepositAgreementId));
				sm_Attachment.setTheState(S_TheState.Normal);
				// sm_Attachment.setBusiType("Tgxy_DepositAgreement");
				sm_Attachment.setUserStart(userUpdate);
				sm_Attachment.setUserUpdate(userUpdate);
				sm_Attachment.setCreateTimeStamp(System.currentTimeMillis());
				sm_Attachment.setLastUpdateTimeStamp(System.currentTimeMillis());
				sm_AttachmentDao.save(sm_Attachment);
			}
		}

		properties.put("tableId", tgxy_DepositAgreementId);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
