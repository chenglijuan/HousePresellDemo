package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tgxy_BuyerInfoForm;
import zhishusz.housepresell.controller.form.Tgxy_ContractInfoForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Tgxy_BuyerInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_ContractInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Tgxy_BuyerInfo;
import zhishusz.housepresell.database.po.Tgxy_ContractInfo;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：三方协议
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgxy_TripleAgreementDetailService
{
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;// 附件
	@Autowired
	private Tgxy_ContractInfoDao tgxy_ContractInfoDao;// 合同
	@Autowired
	private Tgxy_BuyerInfoDao tgxy_BuyerInfoDao;// 买受人
	@Autowired
	private Sm_AttachmentCfgDao sm_AttachmentCfgDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tgxy_TripleAgreementForm model)
	{
		Properties properties = new MyProperties();

		Long tgxy_TripleAgreementId = model.getTableId();
		Tgxy_TripleAgreement tgxy_TripleAgreement = (Tgxy_TripleAgreement) tgxy_TripleAgreementDao
				.findById(tgxy_TripleAgreementId);
		if (tgxy_TripleAgreement == null || S_TheState.Deleted.equals(tgxy_TripleAgreement.getTheState()))
		{
			return MyBackInfo.fail(properties, "该信息已失效，请刷新后重试");
		}

		/*
		 * 获取合同备案号
		 * 根据合同备案号查询合同信息
		 */
		Tgxy_ContractInfo tgxy_ContractInfo = new Tgxy_ContractInfo();
		String codeOfContractRecord = tgxy_TripleAgreement.geteCodeOfContractRecord();
		List<Tgxy_BuyerInfo> tgxy_BuyerInfoList = new ArrayList<Tgxy_BuyerInfo>();
		if (null != codeOfContractRecord && !codeOfContractRecord.trim().isEmpty())
		{
			Tgxy_ContractInfoForm conForm = new Tgxy_ContractInfoForm();
			conForm.seteCodeOfContractRecord(codeOfContractRecord);
			Object conQuery = tgxy_ContractInfoDao
					.findOneByQuery(tgxy_ContractInfoDao.getQuery(tgxy_ContractInfoDao.getBasicHQL(), conForm));

			if (null != conQuery)
			{
				tgxy_ContractInfo = (Tgxy_ContractInfo) conQuery;
			}

			/*
			 * 根据合同备案号查询买受人信息
			 * 
			 */
			Tgxy_BuyerInfoForm buyForm = new Tgxy_BuyerInfoForm();
			buyForm.seteCodeOfContract(codeOfContractRecord);

			tgxy_BuyerInfoList = tgxy_BuyerInfoDao
					.findByPage(tgxy_BuyerInfoDao.getQuery(tgxy_BuyerInfoDao.getBasicHQL(), buyForm));

		}

		properties.put("tgxy_ContractInfo", tgxy_ContractInfo);

		properties.put("tgxy_BuyerInfoList", tgxy_BuyerInfoList);

		// 查询三方协议相关附件信息，设置附件model条件信息，进行附件条件查询 (先使用 A 方案 后期使用B方案)
		Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
		sm_AttachmentForm.setSourceId(String.valueOf(tgxy_TripleAgreementId));
		sm_AttachmentForm.setBusiType("06110301");
		sm_AttachmentForm.setTheState(S_TheState.Normal);

		// 加载所有相关附件信息
		List<Sm_Attachment> sm_AttachmentList = sm_AttachmentDao
				.findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), sm_AttachmentForm));

		if (null == sm_AttachmentList || sm_AttachmentList.size() == 0)
		{
			sm_AttachmentList = new ArrayList<Sm_Attachment>();
		}

		// 查询同一附件类型下的所有附件信息（附件信息归类）
		List<Sm_Attachment> smList = null;

		Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
		form.setBusiType("06110301");
		form.setTheState(S_TheState.Normal);

		List<Sm_AttachmentCfg> smAttachmentCfgList = sm_AttachmentCfgDao
				.findByPage(sm_AttachmentCfgDao.getQuery(sm_AttachmentCfgDao.getBasicHQL(), form));

		if (null == smAttachmentCfgList || smAttachmentCfgList.size() == 0)
		{
			smAttachmentCfgList = new ArrayList<Sm_AttachmentCfg>();
		}
		else
		{
			for (Sm_Attachment sm_Attachment : sm_AttachmentList)
			{
				String sourceType = sm_Attachment.getSourceType();

				for (Sm_AttachmentCfg sm_AttachmentCfg : smAttachmentCfgList)
				{
					if (sm_AttachmentCfg.geteCode().equals(sourceType))
					{
						smList = sm_AttachmentCfg.getSmAttachmentList();
						if (null == smList || smList.size() == 0)
						{
							smList = new ArrayList<Sm_Attachment>();
						}
						smList.add(sm_Attachment);
						sm_AttachmentCfg.setSmAttachmentList(smList);
					}
				}
			}
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tgxy_TripleAgreement", tgxy_TripleAgreement);
		properties.put("smAttachmentCfgList", smAttachmentCfgList);

		return properties;
	}
}
