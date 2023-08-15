package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tgxy_DepositAgreementForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Tgxy_DepositAgreementDao;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Tgxy_DepositAgreement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：协定存款协议
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgxy_DepositAgreementDetailService
{
	@Autowired
	private Tgxy_DepositAgreementDao tgxy_DepositAgreementDao;
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;
	@Autowired
	private Sm_AttachmentCfgDao sm_AttachmentCfgDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tgxy_DepositAgreementForm model)
	{
		Properties properties = new MyProperties();

		/*
		 * xsz by time 2018-8-22 15:32:13
		 * 协定存款协议详情显示：
		 * 1.基本信息的展示
		 * 
		 * 2.附件信息的展示
		 * 
		 */

		// 协定存款协议基本信息展示
		Long tgxy_DepositAgreementId = model.getTableId();
		Tgxy_DepositAgreement tgxy_DepositAgreement = (Tgxy_DepositAgreement) tgxy_DepositAgreementDao
				.findById(tgxy_DepositAgreementId);
		if (tgxy_DepositAgreement == null || S_TheState.Deleted.equals(tgxy_DepositAgreement.getTheState()))
		{
			return MyBackInfo.fail(properties, "该信息已失效，请刷新后重试");
		}

		// 查询合作协议相关附件信息，设置附件model条件信息，进行附件条件查询 (先使用 A 方案 后期使用B方案)
		Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
		sm_AttachmentForm.setSourceId(String.valueOf(model.getTableId()));
		sm_AttachmentForm.setBusiType("06110101");
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
		form.setBusiType("06110101");
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
		properties.put("tgxy_DepositAgreement", tgxy_DepositAgreement);
		properties.put("smAttachmentCfgList", smAttachmentCfgList);

		return properties;
	}
}
