package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementDtlForm;
import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementForm;
import zhishusz.housepresell.database.dao.Tgpf_CyberBankStatementDao;
import zhishusz.housepresell.database.dao.Tgpf_CyberBankStatementDtlDao;
import zhishusz.housepresell.database.dao.Tgpf_DepositDetailDao;
import zhishusz.housepresell.database.po.Tgpf_CyberBankStatement;
import zhishusz.housepresell.database.po.Tgpf_CyberBankStatementDtl;
import zhishusz.housepresell.database.po.Tgpf_DepositDetail;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：网银对账-后台上传的账单原始Excel数据-主表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_CyberBankStatementBatchDeleteService
{
	@Autowired
	private Tgpf_CyberBankStatementDao tgpf_CyberBankStatementDao;
	@Autowired
	private Tgpf_CyberBankStatementDtlDao tgpf_CyberBankStatementDtlDao;
	@Autowired
	private Tgpf_DepositDetailDao tgpf_DepositDetailDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_CyberBankStatementForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Tgpf_CyberBankStatement tgpf_CyberBankStatement = (Tgpf_CyberBankStatement)tgpf_CyberBankStatementDao.findById(tableId);
			if(tgpf_CyberBankStatement == null)
			{
				return MyBackInfo.fail(properties, "'Tgpf_CyberBankStatement(Id:" + tableId + ")'不存在");
			}
			
			// 如果对账完成，不允许删除
//			String billTimeStamp = tgpf_CyberBankStatement.getBillTimeStamp();
			if( null != tgpf_CyberBankStatement.getReconciliationState() && 1 == tgpf_CyberBankStatement.getReconciliationState())
			{
				return MyBackInfo.fail(properties, "已经网银对账，不允许删除！");
			}
	
			tgpf_CyberBankStatement.setTheState(S_TheState.Deleted);
			tgpf_CyberBankStatementDao.save(tgpf_CyberBankStatement);
			
			//删除网银上传详细信息
			Tgpf_CyberBankStatementDtlForm form = new Tgpf_CyberBankStatementDtlForm();
			form.setTheState(S_TheState.Normal);
			form.setMainTable(tgpf_CyberBankStatement);
			form.setMainTableId(tableId);
			
			List<Tgpf_CyberBankStatementDtl> tgpf_CyberBankStatementList = tgpf_CyberBankStatementDtlDao.findByPage(tgpf_CyberBankStatementDtlDao.getQuery(tgpf_CyberBankStatementDtlDao.getBasicHQL2(), form));
			
			if(null != tgpf_CyberBankStatementList && tgpf_CyberBankStatementList.size() > 0){
				
				for (Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl : tgpf_CyberBankStatementList)
				{
								
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

					tgpf_CyberBankStatementDtlDao.save(tgpf_CyberBankStatementDtl);
				}
			}
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
