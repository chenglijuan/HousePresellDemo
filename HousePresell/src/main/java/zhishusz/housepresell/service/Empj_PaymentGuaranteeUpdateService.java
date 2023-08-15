package zhishusz.housepresell.service;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import zhishusz.housepresell.controller.form.Empj_PaymentGuaranteeForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_PaymentGuaranteeDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_PaymentGuarantee;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service更新操作:支付保证申请撤销
 */
@Service
@Transactional
public class Empj_PaymentGuaranteeUpdateService
{
	@Autowired
	private Empj_PaymentGuaranteeDao empj_PaymentGuaranteeDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Sm_AttachmentCfgDao smAttachmentCfgDao;
	@Autowired
	private Sm_AttachmentDao smAttachmentDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	
	private static final String BUSI_CODE = "06120403";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"


	public Properties execute(Empj_PaymentGuaranteeForm model)
	{
		Properties properties = new MyProperties();
				
		String remark = model.getRemark();// 备注

		Sm_User userUpdate= model.getUser();

		Long empj_PaymentGuaranteeId = model.getTableId();
		Empj_PaymentGuarantee empj_PaymentGuarantee = (Empj_PaymentGuarantee) empj_PaymentGuaranteeDao
				.findById(empj_PaymentGuaranteeId);

		if (null == empj_PaymentGuarantee)
		{
			return MyBackInfo.fail(properties, "查询为空");
		}

		if( null == model.getRevokeNo() ||  model.getRevokeNo().length() < 1)
		{
			return MyBackInfo.fail(properties, "支付撤销单号为空");
		}
		
		empj_PaymentGuarantee.setRevokeNo(model.getRevokeNo());
		empj_PaymentGuarantee.setRemark2(remark);
		empj_PaymentGuarantee.setUserUpdate(userUpdate);
		empj_PaymentGuarantee.setLastUpdateTimeStamp(System.currentTimeMillis());
		
		empj_PaymentGuaranteeDao.save(empj_PaymentGuarantee);

		/*
		 * 修改附件
		 * 附件需要先进行删除操作，然后进行重新上传保存功能
		 */
		// 附件信息
		String smAttachmentJson = null;
		if (null != model.getSmAttachmentList() && model.getSmAttachmentList().length() > 0)
		{
			
			Sm_AttachmentForm from = new Sm_AttachmentForm();

			String sourceId = String.valueOf(empj_PaymentGuaranteeId);
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
					sm_Attachment.setSourceId(empj_PaymentGuaranteeId.toString());
					sm_Attachment.setTheState(S_TheState.Normal);
					sm_Attachment.setBusiType(BUSI_CODE);// 业务类型
					smAttachmentDao.save(sm_Attachment);
				}
			}
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}

}
