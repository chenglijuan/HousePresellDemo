package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_PaymentGuaranteeForm;
import zhishusz.housepresell.database.dao.Empj_PaymentGuaranteeDao;
import zhishusz.housepresell.database.po.Empj_PaymentGuarantee;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除:支付申请撤销
 * by li
 * 2019-08-28
 */
@Service
@Transactional
public class Empj_PaymentGuaranteeDeleteService
{

	@Autowired
	private Empj_PaymentGuaranteeDao empj_PaymentGuaranteeDao;

	@Autowired
	private Sm_ApprovalProcess_DeleteService deleteService;
	
	public Properties execute(Empj_PaymentGuaranteeForm model)
	{
		Properties properties = new MyProperties();

		Long[] idArr = model.getIdArr();

		if (idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for (Long tableId : idArr)
		{
			Empj_PaymentGuarantee empj_PaymentGuarantee = (Empj_PaymentGuarantee) empj_PaymentGuaranteeDao
					.findById(tableId);
			if (empj_PaymentGuarantee == null)
			{
				return MyBackInfo.fail(properties, "'Empj_PaymentGuarantee(Id:" + tableId + ")'不存在");

			}
			empj_PaymentGuarantee.setRevokeNo(null);
			empj_PaymentGuaranteeDao.save(empj_PaymentGuarantee);
			
			//删除审批流
			deleteService.execute(tableId, model.getBusiCode());
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
