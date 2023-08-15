package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_SpecialFundAppropriated_AFDtlForm;
import zhishusz.housepresell.controller.form.Tgpf_SpecialFundAppropriated_AFForm;
import zhishusz.housepresell.database.dao.Tgpf_SpecialFundAppropriated_AFDao;
import zhishusz.housepresell.database.dao.Tgpf_SpecialFundAppropriated_AFDtlDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_SpecialFundAppropriated_AF;
import zhishusz.housepresell.database.po.Tgpf_SpecialFundAppropriated_AFDtl;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：特殊拨付-申请主表
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgpf_SpecialFundAppropriated_AFBatchDeleteService
{
	private static String BUSI_CODE = "061206";
	@Autowired
	private Tgpf_SpecialFundAppropriated_AFDao tgpf_SpecialFundAppropriated_AFDao;
	@Autowired
	private Tgpf_SpecialFundAppropriated_AFDtlDao tgpf_SpecialFundAppropriated_AFDtlDao;
	@Autowired
	private Sm_ApprovalProcess_DeleteService deleteService;

	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_SpecialFundAppropriated_AFForm model)
	{
		Properties properties = new MyProperties();
		Sm_User user = model.getUser();
		if (null == user)
		{
			return MyBackInfo.fail(properties, "登录失效，请重新登录");
		}

		Long[] idArr = model.getIdArr();

		if (idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for (Long tableId : idArr)
		{
			Tgpf_SpecialFundAppropriated_AF tgpf_SpecialFundAppropriated_AF = (Tgpf_SpecialFundAppropriated_AF) tgpf_SpecialFundAppropriated_AFDao
					.findById(tableId);
			if (tgpf_SpecialFundAppropriated_AF == null)
			{
				return MyBackInfo.fail(properties, "'Tgpf_SpecialFundAppropriated_AF(Id:" + tableId + ")'不存在");
			}

			tgpf_SpecialFundAppropriated_AF.setTheState(S_TheState.Deleted);
			tgpf_SpecialFundAppropriated_AF.setUserUpdate(user);
			tgpf_SpecialFundAppropriated_AF.setLastUpdateTimeStamp(System.currentTimeMillis());
			tgpf_SpecialFundAppropriated_AFDao.save(tgpf_SpecialFundAppropriated_AF);

			// 删除明细表
			Tgpf_SpecialFundAppropriated_AFDtlForm dtlForm = new Tgpf_SpecialFundAppropriated_AFDtlForm();
			dtlForm.setTheState(S_TheState.Normal);
			dtlForm.setSpecialAppropriated(tgpf_SpecialFundAppropriated_AF);

			List<Tgpf_SpecialFundAppropriated_AFDtl> list;
			list = tgpf_SpecialFundAppropriated_AFDtlDao.findByPage(tgpf_SpecialFundAppropriated_AFDtlDao
					.getQuery(tgpf_SpecialFundAppropriated_AFDtlDao.getBasicHQL(), dtlForm));

			if (null != list && list.size() > 0)
			{
				for (Tgpf_SpecialFundAppropriated_AFDtl dtl : list)
				{
					dtl.setTheState(S_TheState.Deleted);
					dtl.setLastUpdateTimeStamp(System.currentTimeMillis());
					dtl.setUserUpdate(user);
					tgpf_SpecialFundAppropriated_AFDtlDao.save(dtl);
				}
			}
			
			//删除审批流
			deleteService.execute(tableId,BUSI_CODE);

		}
		
		

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
