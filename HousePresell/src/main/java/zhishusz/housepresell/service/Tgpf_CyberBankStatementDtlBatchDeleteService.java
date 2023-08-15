package zhishusz.housepresell.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementDtlForm;
import zhishusz.housepresell.database.dao.Tgpf_CyberBankStatementDtlDao;
import zhishusz.housepresell.database.dao.Tgpf_DepositDetailDao;
import zhishusz.housepresell.database.po.Tgpf_CyberBankStatementDtl;
import zhishusz.housepresell.database.po.Tgpf_DepositDetail;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：网银对账-后台上传的账单原始Excel数据-明细表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_CyberBankStatementDtlBatchDeleteService
{
	@Autowired
	private Tgpf_CyberBankStatementDtlDao tgpf_CyberBankStatementDtlDao;
	@Autowired
	private Tgpf_DepositDetailDao tgpf_DepositDetailDao;

	public Properties execute(Tgpf_CyberBankStatementDtlForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		List<Map<String, Long>> delList = new ArrayList<Map<String, Long>>();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl = (Tgpf_CyberBankStatementDtl)tgpf_CyberBankStatementDtlDao.findById(tableId);
			if(tgpf_CyberBankStatementDtl == null)
			{
				return MyBackInfo.fail(properties, "'Tgpf_CyberBankStatementDtl(Id:" + tableId + ")'不存在");
			}
			
			Long depositDetailId = tgpf_CyberBankStatementDtl.getTgpf_DepositDetailId();
			
			if( null != depositDetailId && depositDetailId > 0)
			{
				Tgpf_DepositDetail tgpf_DepositDetail = (Tgpf_DepositDetail)tgpf_DepositDetailDao.findById(depositDetailId);
				if(tgpf_DepositDetail == null || S_TheState.Deleted.equals(tgpf_DepositDetail.getTheState()))
				{
					
				}
				else
				{
					tgpf_DepositDetail.setReconciliationStateFromCyberBank(0);
					tgpf_DepositDetail.setReconciliationTimeStampFromCyberBank(null);
					
					tgpf_DepositDetailDao.save(tgpf_DepositDetail);
				}
			}			
			
			tgpf_CyberBankStatementDtl.setTheState(S_TheState.Deleted);
			tgpf_CyberBankStatementDtl.setReconciliationState(0);
			tgpf_CyberBankStatementDtl.setReconciliationStamp(null);

			tgpf_CyberBankStatementDtl.setTgpf_DepositDetailId(null);
			Serializable serializable = tgpf_CyberBankStatementDtlDao.save(tgpf_CyberBankStatementDtl);
			
			Map<String, Long> map = new HashMap<String, Long>();
			map.put("tableId", Long.parseLong(serializable.toString()));
			delList.add(map);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("delList", delList);

		return properties;
	}
}
