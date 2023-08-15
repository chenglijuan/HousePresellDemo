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
 * Service添加操作：协定存款协议
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgxy_DepositAgreementAddService
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
		 * xsz by time 2018-8-22 13:23:30
		 * 1.新增协定存款协议需要字段：
		 * 编号、银行Id、开户行Id、托管账户Id、协定存款利率、起始金额、签订日期、期限、生效日期、到期日期、备注、创建人id
		 * 
		 * 2.传递过来的协定存款协议编号唯一，如果存在则提示不可新增
		 * 
		 * 3.生效日期<到期日期
		 * 
		 */

		String eCode = model.geteCode(); // 编号
		// Long userStartId = model.getUserStartId(); // 创建人Id
		Long userStartId = model.getUserId();
		Long bankId = model.getBankId(); // 银行Id
		Long bankOfDepositId = model.getBankOfDepositId();// 开户行Id
		Long escrowAccountId = model.getEscrowAccountId();// 托管账户Id
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
		if (userStartId == null || userStartId < 1)
		{
			return MyBackInfo.fail(properties, "请先进行登录");
		}
		/*if (eCode == null || eCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "编号不能为空");
		}*/
		if (bankId == null || bankId < 1)
		{
			return MyBackInfo.fail(properties, "银行不能为空");
		}
		if (bankOfDepositId == null || bankOfDepositId < 1)
		{
			return MyBackInfo.fail(properties, "开户行不能为空");
		}
		if (escrowAccountId == null || escrowAccountId < 1)
		{
			return MyBackInfo.fail(properties, "托管账户不能为空");
		}
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
			return MyBackInfo.fail(properties, "签订日期不能为空");
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

		// 校验协定存款协议编号唯一性(条件：编号+正常状态（theState=0）)
		Tgxy_DepositAgreementForm eCodeModel = new Tgxy_DepositAgreementForm();
		eCodeModel.seteCode(eCode);
		eCodeModel.setTheState(S_TheState.Normal);

		Integer totalCount = tgxy_DepositAgreementDao.findByPage_Size(
				tgxy_DepositAgreementDao.getQuery_Size(tgxy_DepositAgreementDao.getBasicHQL(), eCodeModel));

		if (totalCount > 0)
		{
			return MyBackInfo.fail(properties, "协定存款协议编号：" + eCode + "已存在，无法继续新增");
		}

		// 校验托管账户
		eCodeModel = new Tgxy_DepositAgreementForm();
		eCodeModel.setTheState(S_TheState.Normal);
		eCodeModel.setEscrowAccountId(escrowAccountId);

		Integer totalCount1 = tgxy_DepositAgreementDao.findByPage_Size(
				tgxy_DepositAgreementDao.getQuery_Size(tgxy_DepositAgreementDao.getBasicHQL(), eCodeModel));

		if (totalCount1 > 0)
		{
			return MyBackInfo.fail(properties, "该托管账户已存在相关协议，无法继续新增");
		}

		// 校验生效日期与到期日期大小关系（生效日期<到期日期）
		if (beginExpirationDate.compareTo(endExpirationDate) > 0)
		{
			return MyBackInfo.fail(properties, "生效日期：（" + beginExpirationDate + "）大于到期日期：（" + endExpirationDate + "）");
		}

		// 查询关联信息
		Sm_User userStart = (Sm_User) sm_UserDao.findById(userStartId);
		if (userStart == null)
		{
			return MyBackInfo.fail(properties, "查询登录人信息为空");
		}

		Emmp_BankInfo bank = (Emmp_BankInfo) emmp_BankInfoDao.findById(bankId);
		if (bank == null)
		{
			return MyBackInfo.fail(properties, "查询银行信息为空");
		}

		Emmp_BankBranch bankOfDeposit = (Emmp_BankBranch) emmp_BankBranchDao.findById(bankOfDepositId);
		if (bankOfDeposit == null)
		{
			return MyBackInfo.fail(properties, "查询开户行信息为空");
		}

		Tgxy_BankAccountEscrowed escrowAccount = (Tgxy_BankAccountEscrowed) tgxy_BankAccountEscrowedDao
				.findById(escrowAccountId);
		if (escrowAccount == null)
		{
			return MyBackInfo.fail(properties, "查询托管账号信息为空");
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


		Tgxy_DepositAgreement tgxy_DepositAgreement = new Tgxy_DepositAgreement();

		// 新增状态默认为正常
		tgxy_DepositAgreement.setTheState(S_TheState.Normal);
		tgxy_DepositAgreement.seteCode(eCode);
		// 创建人和修改人都取创建人信息
		tgxy_DepositAgreement.setUserStart(userStart);
		tgxy_DepositAgreement.setUserUpdate(userStart);
		// 创建时间和修改时间去当前系统时间
		tgxy_DepositAgreement.setCreateTimeStamp(System.currentTimeMillis());
		tgxy_DepositAgreement.setLastUpdateTimeStamp(System.currentTimeMillis());

		tgxy_DepositAgreement.setBank(bank);
		tgxy_DepositAgreement.setTheNameOfBank(bank.getTheName());

		tgxy_DepositAgreement.setBankOfDeposit(bankOfDeposit);
		tgxy_DepositAgreement.setTheNameOfDepositBank(bankOfDeposit.getTheName());

		tgxy_DepositAgreement.setEscrowAccount(escrowAccount);
		tgxy_DepositAgreement.setTheAccountOfEscrowAccount(escrowAccount.getTheAccount());

		tgxy_DepositAgreement.setDepositRate(depositRate);
		tgxy_DepositAgreement.setOrgAmount(orgAmount);
		tgxy_DepositAgreement.setSignDate(signDate);
		tgxy_DepositAgreement.setTimeLimit(timeLimit);
		tgxy_DepositAgreement.setBeginExpirationDate(beginExpirationDate);
		tgxy_DepositAgreement.setEndExpirationDate(endExpirationDate);
		tgxy_DepositAgreement.setRemark(remark);
		Serializable tableId = tgxy_DepositAgreementDao.save(tgxy_DepositAgreement);
		
		
		/*
		 * xsz by time 2018-9-18 13:51:51
		 * 附件信息 后台整合
		 */
		String smAttachmentList = null;
		if (null != model.getSmAttachmentList()&&!model.getSmAttachmentList().trim().isEmpty())
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
				
				sm_Attachment.setUserStart(userStart);
				sm_Attachment.setUserUpdate(userStart);
				sm_Attachment.setCreateTimeStamp(System.currentTimeMillis());
				sm_Attachment.setLastUpdateTimeStamp(System.currentTimeMillis());
				sm_Attachment.setSourceId(tableId.toString());// 关联Id
//				sm_Attachment.setBusiType("Tgxy_DepositAgreement");// 业务类型
				sm_Attachment.setTheState(S_TheState.Normal);
				sm_AttachmentDao.save(sm_Attachment);
			}
		}

		properties.put("tableId", new Long(tableId.toString()));
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
