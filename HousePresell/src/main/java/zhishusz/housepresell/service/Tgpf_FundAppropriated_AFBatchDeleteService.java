package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.state.S_ApplyState;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_FundAppropriated_AFForm;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDao;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：申请-用款-主表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_FundAppropriated_AFBatchDeleteService
{
	@Autowired
	private Tgpf_FundAppropriated_AFDao tgpf_FundAppropriated_AFDao;
	@Autowired
	private Sm_ApprovalProcess_DeleteService deleteService;

	public Properties execute(Tgpf_FundAppropriated_AFForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		String busiCode =  S_BusiCode.busiCode7;
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Tgpf_FundAppropriated_AF tgpf_FundAppropriated_AF = (Tgpf_FundAppropriated_AF)tgpf_FundAppropriated_AFDao.findById(tableId);
			if(tgpf_FundAppropriated_AF == null)
			{
				return MyBackInfo.fail(properties, "'用款申请单(Id:" + tableId + ")'不存在");
			}

			//删除用款申请主表
			tgpf_FundAppropriated_AF.setTheState(S_TheState.Deleted);
			tgpf_FundAppropriated_AF.setApplyState(S_ApplyState.Rescinded);

			//删除用款申请明细表
			for(Tgpf_FundAppropriated_AFDtl tgpf_fundAppropriated_afDtl : tgpf_FundAppropriated_AF.getFundAppropriated_AFDtlList())
			{
				tgpf_fundAppropriated_afDtl.setTheState(S_TheState.Deleted);
			}
			//删除用款申请汇总信息
			for (Tgpf_FundOverallPlanDetail tgpf_fundOverallPlanDetail : tgpf_FundAppropriated_AF.getFundOverallPlanDetailList())
			{
				tgpf_fundOverallPlanDetail.setTheState(S_TheState.Deleted);
			}
//			tgpf_FundAppropriated_AF.getFundAppropriated_AFDtlList().clear();
//			tgpf_FundAppropriated_AF.getFundOverallPlanDetailList().clear();
			tgpf_FundAppropriated_AFDao.save(tgpf_FundAppropriated_AF);

			//删除审批流
			deleteService.execute(tableId,busiCode);
		}

		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
