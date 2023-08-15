package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_PaymentGuaranteeChildForm;
import zhishusz.housepresell.controller.form.Empj_PaymentGuaranteeForm;
import zhishusz.housepresell.database.dao.Empj_PaymentGuaranteeChildDao;
import zhishusz.housepresell.database.dao.Empj_PaymentGuaranteeDao;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Empj_PaymentGuarantee;
import zhishusz.housepresell.database.po.Empj_PaymentGuaranteeChild;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：楼幢-户室
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_PaymentGuaranteeApplyBatchDeleteService
{
	@Autowired
	private Empj_PaymentGuaranteeDao empj_PaymentGuaranteeDao;
	@Autowired
	private Empj_PaymentGuaranteeChildDao empj_PaymentGuaranteeChildDao;
	@Autowired
	private Sm_ApprovalProcess_DeleteService deleteService;

	public Properties execute(Empj_PaymentGuaranteeForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Empj_PaymentGuarantee empj_PaymentGuarantee = (Empj_PaymentGuarantee)empj_PaymentGuaranteeDao.findById(tableId);
			if(empj_PaymentGuarantee == null)
			{
				return MyBackInfo.fail(properties, "支付保证信息不存在！");
			}
			if( "1".equals(empj_PaymentGuarantee.getBusiState()))
			{
				return MyBackInfo.fail(properties, "正在申请中，请勿删除！");
			}
			if( "2".equals(empj_PaymentGuarantee.getBusiState()))
			{
				return MyBackInfo.fail(properties, "审核完成，不允许删除！");
			}
		
			empj_PaymentGuarantee.setTheState(S_TheState.Deleted);
			empj_PaymentGuaranteeDao.save(empj_PaymentGuarantee);
			
			Empj_PaymentGuaranteeChildForm empj_PaymentGuaranteeChildForm = new Empj_PaymentGuaranteeChildForm();
			empj_PaymentGuaranteeChildForm.setEmpj_PaymentGuarantee(empj_PaymentGuarantee);
			empj_PaymentGuaranteeChildForm.setTheState(S_TheState.Normal);
			
			Integer childCount = empj_PaymentGuaranteeChildDao.findByPage_Size(empj_PaymentGuaranteeChildDao.getQuery_Size(empj_PaymentGuaranteeChildDao.getBasicHQL(), empj_PaymentGuaranteeChildForm));
			
			List<Empj_PaymentGuaranteeChild> empj_PaymentGuaranteeChildList;
			if(childCount > 0)
			{
				empj_PaymentGuaranteeChildList = empj_PaymentGuaranteeChildDao.findByPage(empj_PaymentGuaranteeChildDao.getQuery(empj_PaymentGuaranteeChildDao.getBasicHQL(), empj_PaymentGuaranteeChildForm));
			
				for(Empj_PaymentGuaranteeChild empj_PaymentGuaranteeChild : empj_PaymentGuaranteeChildList)
				{
					
					empj_PaymentGuaranteeChild.setTheState(S_TheState.Deleted);
					empj_PaymentGuaranteeChildDao.save(empj_PaymentGuaranteeChild);
				}		
			}	
			
			//删除审批流
			deleteService.execute(tableId, model.getBusiCode());
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
