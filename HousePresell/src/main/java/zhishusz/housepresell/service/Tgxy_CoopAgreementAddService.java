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
 * Service添加操作：合作协议
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgxy_CoopAgreementAddService
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
		 * xsz by time 2018-8-15 10:31:40
		 * 1.新增合作协议需要字段
		 * 合作协议编号（eCode）、银行id（）
		 * 
		 * 2.新增的编号唯一
		 * 
		 * 需求非空校验字段：
		 * 编号、银行id
		 * 
		 */

		String eCode = model.geteCode();// 编号
		// Long userStartId = model.getUserStartId();// 创建人id
		Long userStartId = model.getUserId();
		Long bankId = model.getBankId();// 银行id
		Long bankOfDepositId = model.getBankOfDepositId();// 开户行id
		String signDate = model.getSignDate();// 签署日期yyyy-MM-dd

		if (userStartId == null || userStartId < 1)
		{
			return MyBackInfo.fail(properties, "请先进行登录");
		}
		if (eCode == null || eCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "协议编号不能为空");
		}
		if (bankId == null || bankId < 1)
		{
			return MyBackInfo.fail(properties, "银行不能为空");
		}

		// 校验编号唯一性
		Tgxy_CoopAgreementForm eCodeModel = new Tgxy_CoopAgreementForm();
		eCodeModel.seteCode(eCode);
		eCodeModel.setTheState(S_TheState.Normal);
		Integer totalCount = tgxy_CoopAgreementDao
				.findByPage_Size(tgxy_CoopAgreementDao.getQuery_Size(tgxy_CoopAgreementDao.getBasicHQL(), eCodeModel));
		if (totalCount > 0)
		{
			return MyBackInfo.fail(properties, "合作协议编号：" + eCode + "已存在，无法继续新增");
		}

		// 查询关联关系
		Sm_User userStart = (Sm_User) sm_UserDao.findById(userStartId);
		Emmp_BankBranch bankOfDeposit = (Emmp_BankBranch) emmp_BankBranchDao.findById(bankOfDepositId);
		Emmp_BankInfo bank = (Emmp_BankInfo) emmp_BankInfoDao.findById(bankId);

		if (userStart == null)
		{
			return MyBackInfo.fail(properties, "查询当前登录人信息为空");
		}
		if (bank == null)
		{
			return MyBackInfo.fail(properties, "查询银行信息为空");
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

		// 信息保存对象
		Tgxy_CoopAgreement tgxy_CoopAgreement = new Tgxy_CoopAgreement();
		tgxy_CoopAgreement.setTheState(S_TheState.Normal);
		tgxy_CoopAgreement.seteCode(eCode);
		tgxy_CoopAgreement.setUserStart(userStart);
		tgxy_CoopAgreement.setUserUpdate(userStart);
		// 时间取系统当前时间
		tgxy_CoopAgreement.setCreateTimeStamp(System.currentTimeMillis());
		tgxy_CoopAgreement.setLastUpdateTimeStamp(System.currentTimeMillis());

		tgxy_CoopAgreement.setBank(bank);
		tgxy_CoopAgreement.setTheNameOfBank(bank.getTheName());

		// 如果有开户行信息，进行开户行保存
		if (null != bankOfDeposit)
		{
			tgxy_CoopAgreement.setBankOfDeposit(bankOfDeposit);
			tgxy_CoopAgreement.setTheNameOfDepositBank(bankOfDeposit.getTheName());
		}

		tgxy_CoopAgreement.setSignDate(signDate);

		Serializable tableId = tgxy_CoopAgreementDao.save(tgxy_CoopAgreement);
		
		/*
		 * xsz by time 2018-9-18 13:25:49
		 * 附件信息后台整合
		 */
		String smAttachmentList = null;
		if (null != model.getSmAttachmentList() && !model.getSmAttachmentList().trim().isEmpty())
		{
			smAttachmentList = model.getSmAttachmentList().toString();

			List<Sm_Attachment> gasList = JSON.parseArray(smAttachmentList, Sm_Attachment.class);

			for (Sm_Attachment sm_Attachment : gasList)
			{
				//查询附件配置表
				Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
				form.seteCode(sm_Attachment.getSourceType());
				Sm_AttachmentCfg sm_AttachmentCfg = smAttachmentCfgDao.findOneByQuery_T(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));
				sm_Attachment.setAttachmentCfg(sm_AttachmentCfg);
				
				sm_Attachment.setSourceId(tableId.toString());// 关联Id
				// sm_Attachment.setBusiType("Tgxy_CoopAgreement");// 业务类型
				sm_Attachment.setTheState(S_TheState.Normal);
				sm_Attachment.setUserStart(userStart);// 创建人
				sm_Attachment.setUserUpdate(userStart);// 操作人
				sm_Attachment.setCreateTimeStamp(System.currentTimeMillis());
				sm_Attachment.setLastUpdateTimeStamp(System.currentTimeMillis());
				sm_AttachmentDao.save(sm_Attachment);
			}
		}

		properties.put("tableId", new Long(tableId.toString()));
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
