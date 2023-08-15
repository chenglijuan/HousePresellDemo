package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementForm;
import zhishusz.housepresell.database.dao.Emmp_BankBranchDao;
import zhishusz.housepresell.database.dao.Emmp_BankInfoDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementDao;
import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.Emmp_BankInfo;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service更新操作：合作协议
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgxy_CoopAgreementUpdateService
{
	private static final String BUSI_CODE = "06110103";
	@Autowired
	private Tgxy_CoopAgreementDao tgxy_CoopAgreementDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Emmp_BankInfoDao emmp_BankInfoDao;
	@Autowired
	private Emmp_BankBranchDao emmp_BankBranchDao;
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;
	@Autowired
	private Sm_AttachmentCfgDao smAttachmentCfgDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tgxy_CoopAgreementForm model)
	{
		Properties properties = new MyProperties();
		
		/*
		 * xsz by time 2018-8-17 14:45:42
		 * 修改合作协议需要传递的非空字段：
		 * 编号、银行id、银行名称
		 * 
		 * 1.合作协议编号不可修改
		 * 
		 */
		// String eCode = model.geteCode(); // 编号
		// Long userUpdateId = model.getUserUpdateId();// 修改人id
		Long userUpdateId = model.getUserId();
		Long bankId = model.getBankId();// 银行id
		Long bankOfDepositId = model.getBankOfDepositId();// 开户行id
		String signDate = model.getSignDate();// 签署日期yyyy-MM-dd

		if (userUpdateId == null || userUpdateId < 1)
		{
			return MyBackInfo.fail(properties, "请先进行登录");
		}
		if (bankId == null || bankId < 1)
		{
			return MyBackInfo.fail(properties, "银行不能为空");
		}

		// 查询关联修改人信息
		Sm_User userUpdate = (Sm_User) sm_UserDao.findById(userUpdateId);
		if (userUpdate == null)
		{
			return MyBackInfo.fail(properties, "查询修改人信息为空");
		}

		Emmp_BankBranch bankOfDeposit = (Emmp_BankBranch) emmp_BankBranchDao.findById(bankOfDepositId);
		Emmp_BankInfo bank = (Emmp_BankInfo) emmp_BankInfoDao.findById(bankId);

		if (bank == null)
		{
			return MyBackInfo.fail(properties, "查询银行信息为空");
		}

		// 根据tableId查询合作协议信息
		Long tgxy_CoopAgreementId = model.getTableId();
		Tgxy_CoopAgreement tgxy_CoopAgreement = (Tgxy_CoopAgreement) tgxy_CoopAgreementDao
				.findById(tgxy_CoopAgreementId);
		if (tgxy_CoopAgreement == null)
		{
			return MyBackInfo.fail(properties, "该信息信息已失效，请刷新后重试");
		}

		// 信息保存
		tgxy_CoopAgreement.setUserUpdate(userUpdate);
		// 最后修改时间取系统当前时间
		tgxy_CoopAgreement.setLastUpdateTimeStamp(System.currentTimeMillis());

		tgxy_CoopAgreement.setBank(bank);
		tgxy_CoopAgreement.setTheNameOfBank(bank.getTheName());

		if (null != bankOfDeposit)
		{
			tgxy_CoopAgreement.setBankOfDeposit(bankOfDeposit);
			tgxy_CoopAgreement.setTheNameOfDepositBank(bankOfDeposit.getTheName());
		}

		tgxy_CoopAgreement.setSignDate(signDate);
		
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

		tgxy_CoopAgreementDao.save(tgxy_CoopAgreement);
		
		/*
		 * xsz by time 2018-8-28 18:22:29
		 * 修改附件
		 * 附件需要先进行删除操作，然后进行重新上传保存功能
		 */
		String smAttachmentJson = null;
		// 查询原本附件信息
		Sm_AttachmentForm from = new Sm_AttachmentForm();

		String sourceId = String.valueOf(tgxy_CoopAgreementId);
		from.setTheState(S_TheState.Normal);
		from.setBusiType("06110103");
		from.setSourceId(sourceId);

		// 查询附件
		List<Sm_Attachment> smAttachmentList = sm_AttachmentDao
				.findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), from));
		// 删除附件
		if (null != smAttachmentList && smAttachmentList.size() > 0)
		{
			for (Sm_Attachment sm_Attachment : smAttachmentList)
			{
				sm_Attachment.setTheState(S_TheState.Deleted);
				sm_Attachment.setUserUpdate(userUpdate);// 操作人
				sm_Attachment.setLastUpdateTimeStamp(System.currentTimeMillis());
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
				
				sm_Attachment.setSourceId(String.valueOf(tgxy_CoopAgreementId));
				sm_Attachment.setTheState(S_TheState.Normal);
				// sm_Attachment.setBusiType("Tgxy_CoopAgreement");
				sm_Attachment.setUserStart(userUpdate);// 创建人
				sm_Attachment.setUserUpdate(userUpdate);// 操作人
				sm_Attachment.setCreateTimeStamp(System.currentTimeMillis());
				sm_Attachment.setLastUpdateTimeStamp(System.currentTimeMillis());
				sm_AttachmentDao.save(sm_Attachment);
			}
		}

		properties.put("tableId", tgxy_CoopAgreementId);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
