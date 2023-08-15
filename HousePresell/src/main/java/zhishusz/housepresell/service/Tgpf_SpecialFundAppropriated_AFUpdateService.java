package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;

import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tgpf_SpecialFundAppropriated_AFForm;
import zhishusz.housepresell.database.dao.Tgpf_SpecialFundAppropriated_AFDao;
import zhishusz.housepresell.database.po.Tgpf_SpecialFundAppropriated_AF;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccountLog;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountLogDao;
import zhishusz.housepresell.database.po.Tgpj_BankAccountSupervised;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.dao.Tgpj_BankAccountSupervisedDao;

/*
 * Service更新操作：特殊拨付-申请主表
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgpf_SpecialFundAppropriated_AFUpdateService
{
	@Autowired
	private Tgpf_SpecialFundAppropriated_AFDao tgpf_SpecialFundAppropriated_AFDao;
	@Autowired
	private Sm_AttachmentCfgDao smAttachmentCfgDao;
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;

	public Properties execute(Tgpf_SpecialFundAppropriated_AFForm model)
	{
		Properties properties = new MyProperties();

		Sm_User user = model.getUser();
		if (null == user)
		{
			return MyBackInfo.fail(properties, "请先登录");
		}

		Long tableId = model.getTableId();
		if (null == tableId || tableId < 0)
		{
			return MyBackInfo.fail(properties, "请选择有效的申请信息");
		}

		String appropriatedType = model.getAppropriatedType();
		String appropriatedRemark = model.getAppropriatedRemark();
		Double totalApplyAmount = model.getTotalApplyAmount();
		String applyDate = model.getApplyDate();

		if (null == appropriatedType || appropriatedType.trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "请选择拨付类型");
		}

		if (null == totalApplyAmount || totalApplyAmount <= 0)
		{
			return MyBackInfo.fail(properties, "请输入有效的申请金额");
		}

		if (null == applyDate || applyDate.trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "请选择用款申请日期");
		}

//		if (null == appropriatedRemark || appropriatedRemark.trim().isEmpty())
//		{
//			return MyBackInfo.fail(properties, "请输入特殊说明");
//		}

		
		//查询原申请单信息
		Long tgpf_SpecialFundAppropriated_AFId = model.getTableId();
		Tgpf_SpecialFundAppropriated_AF tgpf_SpecialFundAppropriated_AF = (Tgpf_SpecialFundAppropriated_AF) tgpf_SpecialFundAppropriated_AFDao
				.findById(tgpf_SpecialFundAppropriated_AFId);
		if (tgpf_SpecialFundAppropriated_AF == null)
		{
			return MyBackInfo.fail(properties, "未查询到有效的申请信息，请刷新后重试");
		}
		
		Empj_BuildingInfo empj_BuildingInfo = tgpf_SpecialFundAppropriated_AF.getBuilding();
		// 获取楼幢账户信息
		Tgpj_BuildingAccount buildingAccount = empj_BuildingInfo.getBuildingAccount();
		/*
		 * xsz by time 2018-11-30 09:06:17
		 * 检查本次划款申请总金额<=当前可拨付金额，如有超出，则提示“本次划款申请金额不得大于当前可拨付金额，请确认！”
		 * 
		 * xsz by time 2018-12-6 11:27:13
		 * zcl提出需求：
		 * 发起特殊拨付申请时，需要检验“本次划款申请金额”<=“当前托管余额”；
		 */
		// if (totalApplyAmount > buildingAccount.getAllocableAmount())
		// {
		// return MyBackInfo.fail(properties, "本次划款申请金额不得大于当前可拨付金额，请确认！");
		// }
		if (totalApplyAmount > buildingAccount.getCurrentEscrowFund())
		{
			return MyBackInfo.fail(properties, "本次划款申请金额不得大于当前托管余额，请确认！");
		}
		
		tgpf_SpecialFundAppropriated_AF.setTotalApplyAmount(totalApplyAmount);
		tgpf_SpecialFundAppropriated_AF.setApplyDate(applyDate);
		tgpf_SpecialFundAppropriated_AF.setAppropriatedRemark(appropriatedRemark);
		tgpf_SpecialFundAppropriated_AF.setAppropriatedType(appropriatedType);
		
		tgpf_SpecialFundAppropriated_AF.setUserUpdate(user);
		tgpf_SpecialFundAppropriated_AF.setLastUpdateTimeStamp(System.currentTimeMillis());

		tgpf_SpecialFundAppropriated_AFDao.save(tgpf_SpecialFundAppropriated_AF);
		
		
		/*
		 * xsz by time 2018-11-7 09:14:12
		 * 修改附件
		 * 附件需要先进行删除操作，然后进行重新上传保存功能
		 */
		String smAttachmentJson = null;
		if (null != model.getSmAttachmentList())
		{
			// 查询原本附件信息
			Sm_AttachmentForm from = new Sm_AttachmentForm();

			String sourceId = String.valueOf(tableId);
			from.setTheState(S_TheState.Normal);
			from.setBusiType("06120603");
			from.setSourceId(sourceId);

			// 查询附件
			@SuppressWarnings("unchecked")
			List<Sm_Attachment> smAttachmentList = sm_AttachmentDao
					.findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), from));
			// 删除附件
			if (null != smAttachmentList && smAttachmentList.size() > 0)
			{
				for (Sm_Attachment sm_Attachment : smAttachmentList)
				{
					sm_Attachment.setUserUpdate(user);
					sm_Attachment.setLastUpdateTimeStamp(System.currentTimeMillis());
					sm_Attachment.setTheState(S_TheState.Deleted);
					sm_AttachmentDao.save(sm_Attachment);
				}
			}

			// 重新保存附件
			if (null != model.getSmAttachmentList() && !model.getSmAttachmentList().trim().isEmpty())
			{
				smAttachmentJson = model.getSmAttachmentList().toString();

				List<Sm_Attachment> gasList = JSON.parseArray(smAttachmentJson, Sm_Attachment.class);

				for (Sm_Attachment sm_Attachment : gasList)
				{
					// 查询附件配置表
					Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
					form.seteCode(sm_Attachment.getSourceType());
					Sm_AttachmentCfg sm_AttachmentCfg = smAttachmentCfgDao
							.findOneByQuery_T(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));

					sm_Attachment.setAttachmentCfg(sm_AttachmentCfg);

					sm_Attachment.setUserStart(user);
					sm_Attachment.setUserUpdate(user);
					sm_Attachment.setCreateTimeStamp(System.currentTimeMillis());
					sm_Attachment.setLastUpdateTimeStamp(System.currentTimeMillis());
					sm_Attachment.setSourceId(tableId.toString());// 关联Id
					sm_Attachment.setTheState(S_TheState.Normal);
					sm_AttachmentDao.save(sm_Attachment);
					
				}
			}
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
