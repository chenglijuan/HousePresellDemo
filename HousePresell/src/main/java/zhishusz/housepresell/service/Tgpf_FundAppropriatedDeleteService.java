package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDao;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AF;
import zhishusz.housepresell.database.po.state.S_ApplyState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_FundAppropriatedForm;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriatedDao;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：资金拨付
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_FundAppropriatedDeleteService
{
	@Autowired
	private Tgpf_FundAppropriated_AFDao tgpf_FundAppropriated_AFDao;
	@Autowired
	private Sm_ApprovalProcess_DeleteService deleteService;

	public Properties execute(Tgpf_FundAppropriatedForm model)
	{
		Properties properties = new MyProperties();

		//资金拨付审批的是用款申请单
		Long tgpf_FundAppropriated_AFId = model.getTableId();
		String busiCode = model.getBusiCode();

		Tgpf_FundAppropriated_AF tgpf_FundAppropriated_AF = (Tgpf_FundAppropriated_AF)tgpf_FundAppropriated_AFDao.findById(tgpf_FundAppropriated_AFId);
		if(tgpf_FundAppropriated_AF == null)
		{
			return MyBackInfo.fail(properties, "'用款申请单(Id:" + tgpf_FundAppropriated_AFId + ")'不存在");
		}
		for (Tgpf_FundAppropriated tgpf_fundAppropriated : tgpf_FundAppropriated_AF.getFundAppropriatedList())
		{
			tgpf_fundAppropriated.setApprovalState(S_ApprovalState.WaitSubmit); // 删除 - 资金拨付审批状态该为待提交
		}
		//申请单状态置为已统筹
		tgpf_FundAppropriated_AF.setApplyState(S_ApplyState.Alreadycoordinated); //已统筹
		tgpf_FundAppropriated_AFDao.save(tgpf_FundAppropriated_AF);

		//删除审批流
		deleteService.execute(tgpf_FundAppropriated_AFId,busiCode);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
