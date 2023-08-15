package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tgpf_RefundInfoForm;
import zhishusz.housepresell.database.dao.Tgpf_RefundInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;
import zhishusz.housepresell.database.po.Tgpf_RefundInfo;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;

/*
 * Service更新操作：退房退款-贷款已结清
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgpf_RefundInfoUpdateService
{
	@Autowired
	private Tgpf_RefundInfoDao tgpf_RefundInfoDao;
	@Autowired
	private Sm_AttachmentDao smAttachmentDao;
	@Autowired
	private Tgxy_BankAccountEscrowedDao tgxy_BankAccountEscrowed;
	@Autowired
	private Sm_AttachmentCfgDao smAttachmentCfgDao;

	public Properties execute(Tgpf_RefundInfoForm model)
	{
		Properties properties = new MyProperties();

		Long tgpf_RefundInfoId = model.getTableId();

		Integer receiverType = model.getReceiverType();// 收款人类型
		String receiverName = model.getReceiverName();// 收款人名称
		String receiverBankName = model.getReceiverBankName();// 收款银行
		String receiverBankAccount = model.getReceiverBankAccount();// 收款账号
		Long theBankAccountEscrowedId = model.getTheBankAccountEscrowedId();// 托管楼幢账号Id

		String developCompanyName = model.getDevelopCompanyName();// 买受人
		String bAccountSupervised = model.getBAccountSupervised();// 收款账号
		String bBankName = model.getBBankName();// 收款银行

		String refundTimeStamp = model.getRefundTimeStamp();

		if (tgpf_RefundInfoId == null || tgpf_RefundInfoId < 1)
		{
			return MyBackInfo.fail(properties, "未查询到有效的退房退款信息，请核对查询条件。");
		}

		Tgpf_RefundInfo tgpf_RefundInfo = (Tgpf_RefundInfo) tgpf_RefundInfoDao.findById(tgpf_RefundInfoId);
		if (tgpf_RefundInfo == null)
		{
			return MyBackInfo.fail(properties, "未查询到有效的退房退款信息，请核对查询条件。");
		}

		// 查询托管账户
		if (theBankAccountEscrowedId != null && theBankAccountEscrowedId > 1)
		{
			Tgxy_BankAccountEscrowed bankAccountEscrowed = tgxy_BankAccountEscrowed.findById(theBankAccountEscrowedId);

			if (bankAccountEscrowed == null)
			{
				return MyBackInfo.fail(properties, "未查询到有效的托管账户信息，请核对查询该条件。");
			}
			else
			{
				tgpf_RefundInfo.setRefundBankName(bankAccountEscrowed.getShortNameOfBank());
				tgpf_RefundInfo.setRefundBankAccount(bankAccountEscrowed.getTheAccount());
				tgpf_RefundInfo.setTheBankAccountEscrowed(bankAccountEscrowed);
			}
		}

		if(null != receiverType){
			if (receiverType == 1)
			{
				tgpf_RefundInfo.setReceiverType(receiverType);

				if (receiverName == null || receiverName.length() == 0)
				{
					return MyBackInfo.fail(properties, "收款人名称不能为空");
				}
				else
				{
					tgpf_RefundInfo.setReceiverName(receiverName);
				}

				if (receiverBankName == null || receiverBankName.length() == 0)
				{
					return MyBackInfo.fail(properties, "收款银行不能为空");
				}
				else
				{
					tgpf_RefundInfo.setReceiverBankName(receiverBankName);
				}
				
				if (receiverBankAccount == null || receiverBankAccount.length() == 0)
				{
					return MyBackInfo.fail(properties, "收款账号不能为空");
				}
				else
				{
					tgpf_RefundInfo.setReceiverBankAccount(receiverBankAccount);
				}
			}
			else
			{
				tgpf_RefundInfo.setReceiverType(receiverType);

				if (developCompanyName == null || developCompanyName.length() == 0)
				{
					return MyBackInfo.fail(properties, "收款人名称不能为空");
				}
				else
				{
					tgpf_RefundInfo.setReceiverName(developCompanyName);
				}
				
				if (bBankName == null || bBankName.length() == 0)
				{
					return MyBackInfo.fail(properties, "收款银行不能为空");
				}
				else
				{
					tgpf_RefundInfo.setReceiverBankName(bBankName);
				}
				
				if (bAccountSupervised == null || bAccountSupervised.length() == 0)
				{
					return MyBackInfo.fail(properties, "收款账号不能为空");
				}
				else
				{
					tgpf_RefundInfo.setReceiverBankAccount(bAccountSupervised);
				}
			}
		}

		if (refundTimeStamp != null && !refundTimeStamp.trim().isEmpty())
		{
			tgpf_RefundInfo.setRefundTimeStamp(refundTimeStamp);
		}

		Long version = tgpf_RefundInfo.getVersion();
		/* tgpf_RefundInfo.setVersion(version+1); */

		Serializable serializable = tgpf_RefundInfoDao.save(tgpf_RefundInfo);

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

			String sourceId = String.valueOf(tgpf_RefundInfoId);
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
					// 查询附件配置表
					Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
					form.seteCode(sm_Attachment.getSourceType());
					Sm_AttachmentCfg sm_AttachmentCfg = smAttachmentCfgDao
							.findOneByQuery_T(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));

					sm_Attachment.setAttachmentCfg(sm_AttachmentCfg);
					sm_Attachment.setSourceId(serializable.toString());
					sm_Attachment.setTheState(S_TheState.Normal);
					smAttachmentDao.save(sm_Attachment);
				}
			}
		}

		properties.put("tableId", new Long(serializable.toString()));

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
