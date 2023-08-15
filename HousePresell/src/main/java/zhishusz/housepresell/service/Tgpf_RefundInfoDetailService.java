package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tgpf_RefundInfoForm;
import zhishusz.housepresell.controller.form.Tgpj_BankAccountSupervisedForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Tgpf_RefundInfoDao;
import zhishusz.housepresell.database.dao.Tgpj_BankAccountSupervisedDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Tgpf_RefundInfo;
import zhishusz.housepresell.database.po.Tgpj_BankAccountSupervised;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：退房退款-贷款已结清
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgpf_RefundInfoDetailService
{
	@Autowired
	private Tgpf_RefundInfoDao tgpf_RefundInfoDao;
	@Autowired
	private Sm_AttachmentDao smAttachmentDao;
	@Autowired
	private Sm_AttachmentCfgDao sm_AttachmentCfgDao;
	@Autowired
	private Tgpj_BankAccountSupervisedDao tgpj_BankAccountSupervisedDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_RefundInfoForm model)
	{
		Properties properties = new MyProperties();
		
		//获取业务编号
		String busiCode = model.getBusiCode();

		Long tgpf_RefundInfoId = model.getTableId();
		Tgpf_RefundInfo tgpf_RefundInfo = (Tgpf_RefundInfo) tgpf_RefundInfoDao.findById(tgpf_RefundInfoId);
		if (tgpf_RefundInfo == null || S_TheState.Deleted.equals(tgpf_RefundInfo.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Tgpf_RefundInfo(Id:" + tgpf_RefundInfoId + ")'不存在");
		}

		Sm_AttachmentForm attachmentForm = new Sm_AttachmentForm();

		String sourceId = String.valueOf(tgpf_RefundInfoId);
		attachmentForm.setBusiType(busiCode);
		attachmentForm.setSourceId(sourceId);
		attachmentForm.setTheState(S_TheState.Normal);

		// 查询附件
		List<Sm_Attachment> smAttachmentList = smAttachmentDao
				.findByPage(smAttachmentDao.getQuery(smAttachmentDao.getBasicHQL2(), attachmentForm));

		if (null == smAttachmentList || smAttachmentList.size() == 0)
		{
			smAttachmentList = new ArrayList<Sm_Attachment>();
		}

		// 查询附件类型
		List<Sm_Attachment> smList = null;
		Sm_AttachmentCfgForm attachmentCfgForm = new Sm_AttachmentCfgForm();
		attachmentCfgForm.setTheState(S_TheState.Normal);
		attachmentCfgForm.setBusiType(busiCode);
		List<Sm_AttachmentCfg> smAttachmentCfgList = sm_AttachmentCfgDao
				.findByPage(sm_AttachmentCfgDao.getQuery(sm_AttachmentCfgDao.getBasicHQL(), attachmentCfgForm));
		if (null == smAttachmentCfgList || smAttachmentCfgList.size() == 0)
		{
			smAttachmentCfgList = new ArrayList<Sm_AttachmentCfg>();
		}
		else
		{
			for (Sm_Attachment sm_Attachment : smAttachmentList)
			{
				Long cfgTableId = sm_Attachment.getAttachmentCfg().getTableId();
				
				for (Sm_AttachmentCfg sm_AttachmentCfg : smAttachmentCfgList)
				{
					if (sm_AttachmentCfg.getTableId() == cfgTableId)
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
		
		
		//查询开发企业  监管账号  开户行名称
		Empj_BuildingInfo buildingInfo = tgpf_RefundInfo.getBuilding();
		if (null == buildingInfo)
		{
			return MyBackInfo.fail(properties, "未查询到有效的楼幢基本信息");
		}
		Emmp_CompanyInfo emmp_CompanyInfo = buildingInfo.getDevelopCompany();//开发企业
		if (null == emmp_CompanyInfo)
		{
			return MyBackInfo.fail(properties, "未查询到有效的开发企业信息");
		}
		Tgpj_BankAccountSupervisedForm bankAccountForm = new Tgpj_BankAccountSupervisedForm();
		bankAccountForm.setTheState(S_TheState.Normal);
		bankAccountForm.setDevelopCompany(emmp_CompanyInfo);
		
		List<Tgpj_BankAccountSupervised> bankAccountSupervisedList = tgpj_BankAccountSupervisedDao.findByPage(tgpj_BankAccountSupervisedDao.getQuery(tgpj_BankAccountSupervisedDao.getBasicHQL(), bankAccountForm));
		
		if(null == bankAccountSupervisedList || bankAccountSupervisedList.size() < 1){
			return MyBackInfo.fail(properties, "未查询到有效的监管账户信息");
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tgpf_RefundInfo", tgpf_RefundInfo);
		properties.put("smAttachmentCfgList", smAttachmentCfgList);
		properties.put("bankAccountSupervisedList", bankAccountSupervisedList);
		return properties;
	}
}
